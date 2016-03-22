/*
* Copyright 2011 red.es
* Autores: Arnaiz Consultores.
*
** Licencia con arreglo a la EUPL, Versión 1.1 o -en cuanto
* sean aprobadas por la Comision Europea- versiones
* posteriores de la EUPL (la <<Licencia>>);
* Solo podra usarse esta obra si se respeta la Licencia.
* Puede obtenerse una copia de la Licencia en:
*
* http://ec.europa.eu/idabc/eupl5
*
* Salvo cuando lo exija la legislacion aplicable o se acuerde.
* por escrito, el programa distribuido con arreglo a la
* Licencia se distribuye <<TAL CUAL>>,
* SIN GARANTIAS NI CONDICIONES DE NINGUN TIPO, ni expresas
* ni implicitas.
* Vease la Licencia en el idioma concreto que rige
* los permisos y limitaciones que establece la Licencia.
*/
package es.mitc.redes.urbanismoenred.servicios.validacion;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.Timeout;
import javax.ejb.TimerConfig;
import javax.ejb.TimerService;

import org.apache.log4j.Logger;

import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Tramite;
import es.mitc.redes.urbanismoenred.data.validacion.Proceso;
import es.mitc.redes.urbanismoenred.servicios.comunes.GestionIntroduccionFIPenSistemaLocal;
import es.mitc.redes.urbanismoenred.servicios.dal.ExcepcionPersistencia;
import es.mitc.redes.urbanismoenred.servicios.seguridad.Usuario;
import es.mitc.redes.urbanismoenred.servicios.validacion.ServicioResultadosValidacionInterface.TipoResultado;
import es.mitc.redes.urbanismoenred.utils.excepciones.RedesException;

/**
 * Session Bean implementation class GestorContextosValidacionBean
 */
@Singleton(name = "GestorContextosValidacion")
@Startup
public class GestorContextosValidacionBean implements GestorContextosValidacionLocal, PropertyChangeListener {
	
	private class ComparadorContexos implements Comparator<ContextoValidacion> {

		@Override
		public int compare(ContextoValidacion c1, ContextoValidacion c2) {
			int resultado = ((Integer)c1.getParametro(PARAMETRO_ORDEN)).compareTo((Integer)c2.getParametro(PARAMETRO_ORDEN));
			if (resultado == 0) {
				resultado = ((Integer)c1.getParametro(PARAMETRO_ITERACION)).compareTo((Integer)c2.getParametro(PARAMETRO_ITERACION));
			}
			return resultado;
		}
		
	}
	
	private static final Logger log = Logger.getLogger(GestorContextosValidacionBean.class);

	private static final String PARAMETRO_AMBITO = "Plan.Ambito";
	private static final String PARAMETRO_ORDEN = "Plan.orden";
	private static final String PARAMETRO_ITERACION = "Plan.iteracion";
	private static final long TIMEOUT_SUBIDA = 60000; // cada minuto revisamos si hay algún contexto colgado.
	private Map<String, ContextoValidacion> contextos = new HashMap<String, ContextoValidacion>();
	// Con la implementación actual del mecanismo de subida de ficheos, un contexto
	// puede quedarse congelado en el estado subiendo si durante la subida del archivo el cliente
	// corta la conexión por el motivo que sea.
	// Para evitar esto se mantiene una lista con los contextos que se encuentran en este estado
	// y se revisa periódicamente que no se ha quedado colgado.
	private List<ContextoValidacion> contextosSubiendo = new ArrayList<ContextoValidacion>();
	@EJB
	private GestionIntroduccionFIPenSistemaLocal gestorFips; 
	@EJB
	private ServicioResultadosValidacionLocal gestorProcesos;
	
	@Resource
	private TimerService timerService;
	
    /**
     * Default constructor. 
     */
    public GestorContextosValidacionBean() {
    }
    
    /*
     * (non-Javadoc)
     * @see es.mitc.redes.urbanismoenred.servicios.validacion.GestorContextosValidacionLocal#agregarContexto(es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Tramite)
     */
    @Lock(LockType.WRITE)
	@Override
	public ContextoValidacion agregarContexto(Tramite tramite) throws ExcepcionValidacion {
    	ContextoValidacion contexto = generarContexto(tramite, false);
    	contextos.put(tramite.getCodigofip(), contexto);
    	contexto.agregarListener(ContextoValidacion.ESTADO,this);
    	return contexto;
    }
	
    /**
	 * 
	 */
	@SuppressWarnings("unused")
	@Timeout
	private void ejecutarTareas() {
		revisarSubidas();
		generarContextosValidacion();
	}

    /**
     * Genera el contexto de validación a partir del código de FIP.
     * 
     * @param codigoFip
     * @param usuario
     * @param desplegado 
     * @return
     * @throws ExcepcionValidacion 
     */
    private ContextoValidacion generarContexto(Tramite tramite, boolean desplegado) throws ExcepcionValidacion {
    	ContextoValidacion contexto= new ContextoValidacion(tramite.getCodigofip());
    	// Para saber si se ha guardado correctamente compruebo si el trámite
    	// tiene entidades y determinaciones.
    	if (desplegado && 
    			tramite.getDeterminacions().size() > 0 && 
    			tramite.getEntidads().size() > 0) {
    		contexto.setEstado(Estado.GUARDADO);
    	} else
    		contexto.setEstado(Estado.PENDIENTE);
    	
		List<Proceso> procesos = gestorProcesos.getProcesos(tramite.getCodigofip());
		Date ultimaValidacion = null;
		for (Proceso proceso : procesos) {
			contexto.putParametro(ContextoValidacion.ID_VALIDACION, proceso.getIden());
			if (proceso.getConsolidado() != null) {
				contexto.setEstado(Estado.CONSOLIDADO);
				log.warn("El trámite " + contexto.getCodigoFip() + "no está consolidado pero tiene un proceso de validación consolidado.");
				break;
			}
			
			if (proceso.getFin() != null) {
				if (ultimaValidacion == null || ultimaValidacion.after(proceso.getFin())) {
					
					try {
						if (gestorProcesos.getResultados(proceso.getIden(), TipoResultado.ERROR, TipoValidacion.TODAS).size() >0) {
							contexto.setEstado(Estado.VALIDACION_ERRONEA);
						} else {
							contexto.setEstado(Estado.VALIDADO);
						}
					} catch (ExcepcionPersistencia e) {
						throw new ExcepcionValidacion("Problemas al intentar recuperar los errores de un proceso: " + e.getMessage());
					}
				}
			} else {
				throw new ExcepcionValidacion("El proceso " + proceso.getIden() + " se cerró sin haber finalizado.");
			}
		}
		contexto.putParametro(ContextoValidacion.NOMBRE_PLAN, tramite.getPlan().getNombre());
		contexto.putParametro(PARAMETRO_AMBITO, tramite.getPlan().getIdambito());
		contexto.putParametro(PARAMETRO_ORDEN, tramite.getPlan().getOrden());
		contexto.putParametro(PARAMETRO_ITERACION, tramite.getIteracion());
		
		return contexto;
    }

    /**
	 * 
	 */
	private void generarContextosValidacion() {
		try {
			ContextoValidacion contexto;
			// Genero los contextos de los trámites que no están consolidados
			// en base de datos y que no han subido FIP todavía al servidor
			for (Tramite tramite : gestorFips.getTramitesPendientes() ) {
				if (!contextos.containsKey(tramite.getCodigofip())) {
					try {
						contexto = generarContexto(tramite, false);
						if (!contexto.getEstado().equals(Estado.CONSOLIDADO)) {
							contextos.put(tramite.getCodigofip(), contexto);
							contexto.agregarListener(ContextoValidacion.ESTADO,this);
						}
					} catch (ExcepcionValidacion ev) {
						log.warn(ev.getMessage());
					}
				}
			}
			// Genero los contextos de los trámites que no están consolidados
			// en base de datos pero que ya han subido algún fichero 
			for (Tramite tramite : gestorFips.getTramitesDesplegados()) {
				if (!contextos.containsKey(tramite.getCodigofip())) {
					try {
						contexto = generarContexto(tramite, true);
						if (!contexto.getEstado().equals(Estado.CONSOLIDADO)) {
							contextos.put(tramite.getCodigofip(),contexto);
							contexto.agregarListener(ContextoValidacion.ESTADO,this);
						}
					} catch (ExcepcionValidacion ev) {
						log.warn(ev.getMessage());
					}
				}
			}
		} catch (RedesException e) {
			log.warn(e.getMessage(),e);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.servicios.validacion.GestorContextosValidacionLocal#getContexto(java.lang.String)
	 */
	@Override
	public ContextoValidacion getContexto(String codigoFip) {
		return contextos.get(codigoFip);
	}
	
	/*
     * (non-Javadoc)
     * @see es.mitc.redes.urbanismoenred.servicios.validacion.GestorContextosValidacionLocal#getContextos(es.mitc.redes.urbanismoenred.servicios.seguridad.Usuario)
     */
    @Lock(LockType.READ)
	@Override
	public ContextoValidacion[] getContextos(Usuario usuario) {
		List<ContextoValidacion> resultado = new ArrayList<ContextoValidacion>();
		Integer ambito;
		for(ContextoValidacion contexto: contextos.values()) {
			ambito = (Integer)contexto.getParametro(PARAMETRO_AMBITO);
			if (usuario.puedeValidar(ambito)) {
				resultado.add(contexto);
			}
		}
		ContextoValidacion[] arrayContextos = resultado.toArray(new ContextoValidacion[0]);
		Arrays.sort(arrayContextos,new ComparadorContexos());
		return arrayContextos;
	}
	
	/**
     * 
     */
    @SuppressWarnings("unused")
	@PostConstruct
	private void postConstruct() {
    	generarContextosValidacion();
    	
    	timerService.createIntervalTimer(TIMEOUT_SUBIDA, TIMEOUT_SUBIDA, new TimerConfig("Temporizador subida", false));
    }
	
	/*
     * (non-Javadoc)
     * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
     */
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		ContextoValidacion contexto = (ContextoValidacion)evt.getSource();
		// Por ahora sólo estoy suscrito a los eventos de cambio de estado.
		Estado nuevoEstado = (Estado)evt.getNewValue();
		
		if (nuevoEstado.equals(Estado.SUBIENDO)) {
			contextosSubiendo.add(contexto);
		} else {
			contextosSubiendo.remove(contexto);
		}
		
		if (nuevoEstado.equals(Estado.CONSOLIDADO)) {
			// El trámite ya está consolidado, por lo que no tiene sentido
			// mantener su contexto de validación.
			contextos.remove(contexto.getCodigoFip());
		}
	}

	/**
	 * Función que comprueba si los contextos en subida se han quedado bloqueados
	 */
	private void revisarSubidas() {
//		log.debug("Temporizador de subida invocado");
		final String progresoSubida = "subida.progreso"; 
		for (ContextoValidacion contexto : contextosSubiendo) {
			if (contexto.getParametro(progresoSubida) != null) {
				if (contexto.getParametro(progresoSubida).equals(contexto.getParametro(ContextoValidacion.PROGRESO))) {
					// Contexto subiendo no ha recibido actualizaciones, se cancela.
					log.warn("Contexto " + contexto.getCodigoFip() + " no ha progresado. Cancelando.");
					contexto.putParametro(progresoSubida, null);
					contexto.setEstado((Estado)contexto.getParametro(ContextoValidacion.ESTADO_INICIAL));
					contexto.putParametro(ContextoValidacion.USUARIO, null);
					
				} else {
					contexto.putParametro(progresoSubida, contexto.getParametro(ContextoValidacion.PROGRESO));
				}
			} else {
				contexto.putParametro(progresoSubida, contexto.getParametro(ContextoValidacion.PROGRESO));
			}
		}
	}

}

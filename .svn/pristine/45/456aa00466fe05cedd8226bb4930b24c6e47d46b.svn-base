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

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Tramite;
import es.mitc.redes.urbanismoenred.data.validacion.Error;
import es.mitc.redes.urbanismoenred.data.validacion.Proceso;
import es.mitc.redes.urbanismoenred.data.validacion.Resultado;
import es.mitc.redes.urbanismoenred.data.validacion.ResultadoId;
import es.mitc.redes.urbanismoenred.data.validacion.Validacion;
import es.mitc.redes.urbanismoenred.servicios.comunes.GestionIntroduccionFIPenSistemaLocal;
import es.mitc.redes.urbanismoenred.servicios.dal.ExcepcionPersistencia;
import es.mitc.redes.urbanismoenred.servicios.seguridad.Usuario;
import es.mitc.redes.urbanismoenred.utils.excepciones.RedesException;

@Stateless
public class ServicioResultadosValidacionBean implements ServicioResultadosValidacionLocal {
	
	private static final Logger log = Logger.getLogger(ServicioResultadosValidacionBean.class);
    
    @PersistenceContext(unitName = "rpmv2")
    private EntityManager em;
    
    @EJB
    private GestionIntroduccionFIPenSistemaLocal gestorfip;

    // -- METODOS -- //
    
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    private Proceso crearProcesoValidacion(String idFIP) throws ExcepcionPersistencia {
    	Proceso proceso = new Proceso();
    	proceso.setIdfip(idFIP);
    	proceso.setInicio(GregorianCalendar.getInstance().getTime());
    	try {
			proceso.setBackup(gestorfip.getUltimaVersionFIP(idFIP));
		} catch (RedesException e) {
			throw new ExcepcionPersistencia("No se puede obtener la ruta al fichero FIP. Causa: " + e.getMessage(), e);
		}
    	
    	em.persist(proceso);
    	
    	return proceso;
    }
    
    /*
     * (non-Javadoc)
     * @see es.mitc.redes.urbanismoenred.servicios.validacion.ServicioResultadosValidacionLocal#finalizarProceso(java.lang.String)
     */
    @Override
	public void finalizarProceso(String idFip) throws ExcepcionPersistencia {
		Proceso proceso = getProcesoActivo(idFip);
		
		if (proceso != null) {
			proceso.setFin(GregorianCalendar.getInstance().getTime());
		} else {
			throw new ExcepcionPersistencia("No existe proceso activo para " + idFip);
		}
		
	}
    
    /*
     * (non-Javadoc)
     * @see es.mitc.redes.urbanismoenred.servicios.validacion.ServicioResultadosValidacionLocal#getProceso(int)
     */
    @Override
	public Proceso getProceso(int identificador) {
		return em.find(Proceso.class, identificador);
	}
	
	/**
	 * Devuelve el proceso de validación que se está ejecutando actualmente sobre
	 * un FIP.
	 * 
	 * @param idFip Código del FIP que se está validando.
	 * @return Proceso de validación o null si no hay procesos iniciados y no 
	 * finalizados.
	 */
	private Proceso getProcesoActivo(String idFip) {
    	Proceso proceso = null;
    	
    	try {
    		proceso= (Proceso)em.createNamedQuery("Proceso.buscarActivo")
				.setParameter("idFip", idFip).getSingleResult();
    		
	    } catch (NoResultException nre) {
			return null;
		} catch (NonUniqueResultException nure) {
			return null;
		}
	    
		return proceso;
	}
    /*
     * (non-Javadoc)
     * @see es.mitc.redes.urbanismoenred.servicios.validacion.ServicioResultadosValidacionLocal#getProcesoConsolidado(java.lang.String)
     */
	@Override
	public Proceso getProcesoConsolidado(String idFip) {
		try {
			return (Proceso)em.createNamedQuery("Proceso.estaConsolidado")
				.setParameter("idFip", idFip)
				.getSingleResult();
		} catch (NoResultException nre) {
			return null;
		} catch (NonUniqueResultException nure) {
			return null;
		}
	}
 
   /*
     * (non-Javadoc)
     * @see es.mitc.redes.urbanismoenred.servicios.validacion.ServicioResultadosValidacionLocal#getProcesos()
     */
    @SuppressWarnings("unchecked")
	@Override
	public List<Proceso> getProcesos() {
		return (List<Proceso>)em.createNamedQuery("Proceso.obtenerTodos")
				.getResultList();
	}
    
   /*
     * (non-Javadoc)
     * @see es.mitc.redes.urbanismoenred.servicios.validacion.ServicioResultadosValidacionLocal#getProcesos(java.lang.String)
     */
    @SuppressWarnings("unchecked")
	@Override
	public List<Proceso> getProcesos(String idFip) {
		return (List<Proceso>)em.createNamedQuery("Proceso.buscarPorFip")
			.setParameter("idFip", idFip).getResultList();
	}

    /*
     * (non-Javadoc)
     * @see es.mitc.redes.urbanismoenred.servicios.validacion.ServicioResultadosValidacionLocal#getProcesos(es.mitc.redes.urbanismoenred.servicios.seguridad.Usuario)
     */
	@SuppressWarnings("unchecked")
	@Override
	public List<Proceso> getProcesos(Usuario usuario) {
		List<Proceso> todos = (List<Proceso>)em.createNamedQuery("Proceso.obtenerTodos")
				.getResultList();
		Query query = em.createNamedQuery("Tramite.findTramiteFromCodFip");
		Tramite tramite;
		List<Proceso> resultados = new ArrayList<Proceso>();
		for (Proceso proceso: todos) {
			try {
				tramite = (Tramite)query.setParameter("codigoFip", proceso.getIdfip())
						.getSingleResult();
				
				if (usuario.puedeValidar(tramite.getPlan().getIdambito())) {
					resultados.add(proceso);
				}
			} catch (NoResultException nre) {
				log.warn("No se agrega el proceso " + proceso.getIden() + " porque el trámite " + proceso.getIdfip() + " no se encuentra en base de datos.");
			}
		}
		return resultados;
	}
	
	/*
    * (non-Javadoc)
    * @see es.mitc.redes.urbanismoenred.servicios.validacion.ServicioResultadosValidacionLocal#getResultados(int, es.mitc.redes.urbanismoenred.servicios.validacion.ServicioResultadosValidacionInterface.TipoResultado, es.mitc.redes.urbanismoenred.servicios.validacion.TipoValidacion)
    */
    @SuppressWarnings("unchecked")
    @Override
	public List<Resultado> getResultados(int idProceso, TipoResultado tipo,
			TipoValidacion codigoValidacion) throws ExcepcionPersistencia {
    	
    	String sql = "SELECT DISTINCT r FROM "+
			"es.mitc.redes.urbanismoenred.data.validacion.Resultado r LEFT JOIN FETCH r.errors" + 
			" WHERE r.proceso.iden = " + idProceso;

		switch (tipo) {
			case AVISO:
					sql += " AND r.exito = false AND r.validacion.tipoerror = 2";
			case ERROR:
					sql += " AND r.exito = false AND r.validacion.tipoerror = 1";
				break;
			case CORRECTA:
					sql += " AND r.exito = true";
				break;
			case TODAS:
			default:
				break;
		}
		
		if (codigoValidacion != null && !TipoValidacion.TODAS.equals(codigoValidacion)) {
			sql += " AND r.validacion.tipo = "+codigoValidacion;
		}
		
		return (List<Resultado>)em.createQuery(sql).getResultList();
	}

	/*
    * (non-Javadoc)
    * @see es.mitc.redes.urbanismoenred.servicios.validacion.ServicioResultadosValidacionLocal#getResultados(java.lang.String, es.mitc.redes.urbanismoenred.servicios.validacion.ServicioResultadosValidacionInterface.TipoResultado, es.mitc.redes.urbanismoenred.servicios.validacion.TipoValidacion)
    */
	@Override
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Resultado> getResultados(String idFip, TipoResultado tipo,
			TipoValidacion codigoValidacion) throws ExcepcionPersistencia {
    	Proceso proceso = getProcesoActivo(idFip);
    	if (proceso == null) {
    		throw new ExcepcionPersistencia("No hay proceso activo para FIP " + idFip);
    	}
    	
		return getResultados(proceso.getIden(), tipo, codigoValidacion);
	}

	/*
	 * (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.servicios.validacion.ServicioResultadosValidacionLocal#iniciarProceso(java.lang.String)
	 */
	@Override
	public int iniciarProceso(String idFip) throws ExcepcionPersistencia {
		// Se necesita invocar a este método porque si se le invoca desde un
		// método que antes hayta invocado a getProcesos, la query de getProcesoActivo
		// devuelve el resultado de getProcesos, lo que provoca en muchas ocasiones errores
		// inesperados al devolver más de un resultado.
		em.clear();
		
		Proceso proceso = getProcesoActivo(idFip);
		
		if (proceso == null) {
			proceso = crearProcesoValidacion(idFip);
		} else {
			throw new ExcepcionPersistencia("Ya existe un proceso iniciado para el trámite " + idFip);
		}
		
		return proceso.getIden();
	}

	/*
	 * (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.servicios.validacion.ServicioResultadosValidacionLocal#registrarResultado(java.lang.String, int, boolean, java.util.List)
	 */
	@Override
	public void registrarResultado( String idFip, int idValidacion, boolean exito, List<String> resultados) throws ExcepcionPersistencia{
		
		// Primero se comprueba si ya hay definido un proceso activo para ese FIP.
		Proceso proceso = getProcesoActivo(idFip);
		
		if (proceso == null) {
			throw new ExcepcionPersistencia("No hay un proceso de validación iniciado para el tramite " + idFip);
		}
		
		try {
			Validacion validacion = (Validacion)em.createNamedQuery("Validacion.buscarPorId")
				.setParameter("id", idValidacion)
				.getSingleResult();
			
			// Por último se crea el resultado de validación 
			Resultado resultado = new Resultado();
			ResultadoId	id = new ResultadoId();
			id.setIdproceso(proceso.getIden());
			id.setIdvalidacion(validacion.getIden());
			resultado.setId(id);
			resultado.setValidacion(validacion);
			resultado.setExito(exito);
			resultado.setFecha(GregorianCalendar.getInstance().getTime());
			resultado.setProceso(proceso);
			
			em.persist(resultado);
			
			if (!exito) {
				Error error;
				if (!resultados.isEmpty()) {
					for (String datos : resultados) {
						error = new Error();
						error.setResultado(resultado);
						
						error.setMensaje(datos);
						em.persist(error);
					}
				} else {
					error = new Error();
					error.setResultado(resultado);
					error.setMensaje("No se han encontrado elementos que cumplan la validación.");
					em.persist(error);
				}
			}
		} catch (NoResultException nre) {
			throw new ExcepcionPersistencia("No se ha encontrado validación con id: " + idValidacion,nre);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.servicios.validacion.ServicioResultadosValidacionLocal#registrarResultado(java.lang.String, es.mitc.redes.urbanismoenred.data.validacion.Validacion, boolean, java.util.List)
	 */
	@Override
	public void registrarResultado(String idFip, Validacion validacion, boolean exito,
			List<?> resultados) throws ExcepcionPersistencia {
		// Primero se comprueba si ya se ha definido un proceso para ese FIP.
		Proceso proceso = getProcesoActivo(idFip);
		
		if (proceso == null) {
			throw new ExcepcionPersistencia("No hay un proceso de validación iniciado para el tramite " + idFip);
		}
		
		// Por último se crea el resultado de validación 
		Resultado resultado = new Resultado();
		ResultadoId	id = new ResultadoId();
		id.setIdproceso(proceso.getIden());
		id.setIdvalidacion(validacion.getIden());
		resultado.setId(id);
		resultado.setValidacion(validacion);
		resultado.setExito(exito);
		resultado.setFecha(GregorianCalendar.getInstance().getTime());
		resultado.setProceso(proceso);
		
		em.persist(resultado);
		
		if (!exito) {
			Error error;
			if (!resultados.isEmpty()) {
				String mensaje;
				for (Object datos : resultados) {
					error = new Error();
					error.setResultado(resultado);
					if (datos.getClass().isArray()) {
						Object valores[] = (Object[])datos;
						mensaje = validacion.getMensaje();
						for (int i = 0; i < valores.length; i++) {
							mensaje = mensaje.replace("%"+(i+1), valores[i].toString());
						}
					} else {
						mensaje = validacion.getMensaje().replace("%1", datos.toString());
					}
					error.setMensaje(mensaje);
					em.persist(error);
				}
			} else {
				error = new Error();
				error.setResultado(resultado);
				error.setMensaje("No se han encontrado elementos que cumplan la validación.");
				em.persist(error);
			}
		}
	}
}




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
package es.mitc.redes.urbanismoenred.servicios.refundido;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import es.mitc.redes.urbanismoenred.data.rpm.diccionario.Instrumentotipooperacionplan;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Operacionplan;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Determinacion;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Entidad;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Tramite;
import es.mitc.redes.urbanismoenred.servicios.dal.EliminadorEntidadesRefundidoLocal;
import es.mitc.redes.urbanismoenred.servicios.refundido.ContextoRefundido.LOG;

/**
 * Session Bean implementation class LimpiadorRefundido
 * 
 * @author Arnaiz Consultores
 */
@Stateless
public class LimpiadorRefundido implements LimpiadorRefundidoLocal {
	
	private static final int TIPOOPERACIONPLAN_LEVANTAMIENTOPARCIAL = 8;
	private static final int TIPOOPERACIONPLAN_LEVANTAMIENTOTOTAL = 6;
	private static final int TIPOOPERACIONPLAN_MODIFICACION = 1;
	private static final int TIPOOPERACIONPLAN_SUSPENSIONPARCIAL = 7;
	private static final int TIPOOPERACIONPLAN_SUSPENSIONTOTAL = 3;

	private static final String TRAMITES_ORDENADOS = "refundido.tramites.ordenados";
	private static final int CARACTER_UNIDAD = 18;
	private static final int CARACTER_ADSCRIPCION = 19;

	@PersistenceContext(unitName = "rpmv2")
	private EntityManager em;
	
	@EJB (beanName = "EliminadorSqlEntidadesRefundido")
	private EliminadorEntidadesRefundidoLocal eliminador;
	
	@EJB
	private GestorAportacionesLocal gestorAportaciones;
	@EJB
	private OperadorDeterminacionesRefundidoLocal operadorDeterminaciones;
	
	@EJB
	private OperadorEntidadesRefundidoLocal operadorEntidades;
	
    /**
     * Default constructor. 
     */
    public LimpiadorRefundido() {
        
    }
    
    /**
	 * 
	 * @param determinacion
     * @param contexto 
	 */
	private void eliminarNoAplicadas(Determinacion determinacion, ContextoRefundido contexto) {
		if (!gestorAportaciones.determinacionEsAportada(determinacion.getIden(), contexto)) {
			if (em.contains(determinacion)) {
				em.refresh(determinacion);
				for (Determinacion hija : determinacion.getDeterminacionsForIdpadre()) {
					eliminarNoAplicadas(hija, contexto);
				}
				em.refresh(determinacion);
				if (determinacion.getDeterminacionsForIdpadre().isEmpty() 
						&& determinacion.getEntidaddeterminacions().isEmpty()
						&& determinacion.getIdcaracter() != CARACTER_UNIDAD
						&& determinacion.getIdcaracter() != CARACTER_ADSCRIPCION) {
					eliminador.eliminar(determinacion);
				}
				em.flush();
			}
		}
	}

	/**
	 * 
	 * @param contexto
	 */
	@SuppressWarnings("unchecked")
	private void limpiarDeterminaciones(ContextoRefundido contexto) {
		Tramite refundido = (Tramite) contexto.getParametro(ContextoRefundido.TRAMITE_REFUNDIDO);
		
		em.refresh(refundido);
		
		List<Determinacion> determinaciones= em.createNamedQuery("refundido.Determinacion.obtenerPorTramiteYCaracter")
				.setParameter("idTramite", refundido.getIden())
				.setParameter("idCaracter", ClsDatos.ID_CARACTER_OPERADORA)
				.getResultList();
		
		for(Determinacion determinacion : determinaciones ) {
			contexto.logTraducido(LOG.INFO, "refundido.limpiar.tramite.determinacion.operadora", determinacion.getCodigo());
			eliminador.eliminar(determinacion);
		}
		
		em.flush();
		
		// Eliminamos carpetas vacías.
		List<Determinacion> determinacionesAportadas = operadorDeterminaciones.getAportadas(contexto);
				
		for (Determinacion determinacion : determinacionesAportadas) {
			if (em.contains(determinacion)) {
				em.refresh(determinacion);
				if (determinacion.getDeterminacionsForIdpadre().size() == 0 
						&& determinacion.getDeterminacionsForIddeterminacionbase().size() == 0) {
					eliminador.eliminar(determinacion);
				}
			}
		}
		
		em.flush();
		
		// Elimino las aportaciones según tipo de plan operador.
		es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Tramite[] tramites = (es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Tramite[]) contexto.getParametro(TRAMITES_ORDENADOS);
		Instrumentotipooperacionplan itop;
		Determinacion aportadas;
		for (es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Tramite tramite: tramites) {
			// Obtenemos los planes contra los que opera este trámite.
			for (Operacionplan op : tramite.getPlan().getOperacionplansForIdplanoperador()) {
				itop = em.find(Instrumentotipooperacionplan.class, op.getIdinstrumentotipooperacion());
						
				switch (itop.getTipooperacionplan().getIden()) {
					case TIPOOPERACIONPLAN_MODIFICACION:
							aportadas = operadorDeterminaciones.obtenerCarpetaDeterminacionesAportadas(op.getPlanByIdplanoperador(), contexto, false);
							if (aportadas != null) {
								eliminarNoAplicadas(aportadas, contexto);
							}
						break;
					case TIPOOPERACIONPLAN_SUSPENSIONTOTAL:
					case TIPOOPERACIONPLAN_LEVANTAMIENTOTOTAL:
					case TIPOOPERACIONPLAN_SUSPENSIONPARCIAL:
					case TIPOOPERACIONPLAN_LEVANTAMIENTOPARCIAL:
						aportadas = operadorDeterminaciones.obtenerCarpetaDeterminacionesAportadas(op.getPlanByIdplanoperador(), contexto, false);
						if (aportadas != null) {
							eliminador.eliminar(aportadas);
						}
						break;
					default:
					break;
				}
			}
		}
		
		em.flush();
		
		Determinacion carpetaDeterminaciones = operadorDeterminaciones.obtenerCarpetaDeterminacionesAportadas(contexto);
		
		em.refresh(carpetaDeterminaciones);
		
		if (carpetaDeterminaciones.getDeterminacionsForIdpadre().size() == 0 && carpetaDeterminaciones.getDeterminacionsForIddeterminacionbase().size() == 0) {
			eliminador.eliminar(carpetaDeterminaciones);
		}
		
		em.flush();
	}

	/**
     * 
     * @param contexto
     * @throws ExcepcionRefundido 
     */
    @SuppressWarnings("unchecked")
	private void limpiarEntidades(ContextoRefundido contexto) throws ExcepcionRefundido {
    	Tramite refundido = (Tramite) contexto.getParametro(ContextoRefundido.TRAMITE_REFUNDIDO);
		
		em.refresh(refundido);
    	
    	Query consulta = em.createNamedQuery("refundido.Determinacion.obtenerGrupoEntidad")
				.setParameter("idCaracter",ClsDatos.ID_CARACTER_GRUPODEENTIDADES);
    	List<Determinacion> determinaciones;
    	
		for (Entidad entidad : refundido.getEntidads()) {
			determinaciones = consulta.setParameter("idEntidad", entidad.getIden())
					.getResultList();
			if (determinaciones.size() > 0 &&
					determinaciones.get(0).getCodigo().equals(ClsDatos.CODIGO_DETERMINACIONBASE_GRUPO_OPERADOR)){
				contexto.logTraducido(LOG.INFO, "refundido.limpiar.tramite.entidad.operadora", entidad.getCodigo());
				eliminador.eliminar(entidad);
			}
		}
		
		List<Entidad> entidadesAportadas = operadorEntidades.getAportadas(contexto);
		
		for(Entidad entidad : entidadesAportadas) {
			if (em.contains(entidad)) {
				em.refresh(entidad);
				if (entidad.getEntidadsForIdpadre().size() == 0 && entidad.getEntidadsForIdentidadbase().size() == 0) {
					eliminador.eliminar(entidad);
				}
			}
		}
		
		em.flush();
		
		// Elimino las aportaciones según tipo de plan operador.
		es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Tramite[] tramites = (es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Tramite[]) contexto.getParametro(TRAMITES_ORDENADOS);
		Instrumentotipooperacionplan itop;
		for (es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Tramite tramite: tramites) {
			// Obtenemos los planes contra los que opera este trámite.
			for (Operacionplan op : tramite.getPlan().getOperacionplansForIdplanoperador()) {
				itop = em.find(Instrumentotipooperacionplan.class, op.getIdinstrumentotipooperacion());
				
				switch (itop.getTipooperacionplan().getIden()) {
					case TIPOOPERACIONPLAN_MODIFICACION:
							Entidad ambito = operadorEntidades.obtenerAmbitoAplicacion(op.getPlanByIdplanoperador(), (int)contexto.getParametro(ContextoRefundido.ID_PROCESO));
							if (ambito != null) {
								Entidad aportadas = ambito.getEntidadByIdpadre();
								
								eliminador.eliminar(ambito);
								
								em.refresh(aportadas);
								
								if (aportadas.getEntidadsForIdpadre().isEmpty()) {
									eliminador.eliminar(aportadas);
								}
							}
						break;
					case TIPOOPERACIONPLAN_SUSPENSIONTOTAL:
					case TIPOOPERACIONPLAN_LEVANTAMIENTOTOTAL:
					case TIPOOPERACIONPLAN_SUSPENSIONPARCIAL:
					case TIPOOPERACIONPLAN_LEVANTAMIENTOPARCIAL:
						Entidad aportadas = operadorEntidades.obtenerCarpetaEntidadesAportadas(op.getPlanByIdplanoperador(), contexto, false);
						if (aportadas != null) {
							eliminador.eliminar(aportadas);
						}
						break;
					default:
					break;
				}
			}
		}
		
		em.flush();
		
		Entidad carpetaEntidades = operadorEntidades.obtenerCarpetaEntidadesAportadas(contexto);
		if (carpetaEntidades != null && em.contains(carpetaEntidades)) {
			em.refresh(carpetaEntidades);
			if (carpetaEntidades.getEntidadsForIdpadre().size() == 0 && carpetaEntidades.getEntidadsForIdentidadbase().size() == 0) {
				eliminador.eliminar(carpetaEntidades);
			}
		}
		
		em.flush();
    }

	/*
     * (non-Javadoc)
     * @see es.mitc.redes.urbanismoenred.servicios.refundido.LimpiadorRefundidoLocal#limpiarRefundido(es.mitc.redes.urbanismoenred.servicios.refundido.ContextoRefundido)
     */
	@Override
	public void limpiarRefundido(ContextoRefundido contexto)
			throws ExcepcionRefundido {
		
		limpiarEntidades(contexto);
		
		limpiarDeterminaciones(contexto);
	}

}

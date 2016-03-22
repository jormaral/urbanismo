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
package es.mitc.redes.urbanismoenred.servicios.refundido.operaciones.planes;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;

import es.mitc.redes.urbanismoenred.data.rpm.refundido.Determinacion;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Entidad;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Entidaddeterminacion;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Tramite;
import es.mitc.redes.urbanismoenred.servicios.refundido.ClsDatos;
import es.mitc.redes.urbanismoenred.servicios.refundido.ContextoRefundido;
import es.mitc.redes.urbanismoenred.servicios.refundido.ContextoRefundido.LOG;
import es.mitc.redes.urbanismoenred.servicios.refundido.ExcepcionRefundido;
import es.mitc.redes.urbanismoenred.servicios.refundido.GestorGeometriasLocal;
import es.mitc.redes.urbanismoenred.servicios.refundido.OperadorDeterminacionesRefundidoLocal;
import es.mitc.redes.urbanismoenred.servicios.refundido.OperadorEntidadesRefundidoLocal;
import es.mitc.redes.urbanismoenred.servicios.refundido.ReplicadorRefundidoLocal;

/**
 * @author Arnaiz Consultores
 *
 */
@Stateless(name = "refundido.operacion.planes.sustituir")
public class Sustituir extends IncorporacionAbstract implements EjecutorLocal {
	
	private static final Logger log = Logger.getLogger(Sustituir.class);
	
	private static final String DETERMINACIONES_A_ELIMINAR = "refundido.operaciones.plan.eliminar.determinaciones";
	private static final String ENTIDADES_A_ELIMINAR = "refundido.operaciones.plan.eliminar.entidades";
	
	@PersistenceContext(unitName = "rpmv2")
	private EntityManager em;
	@EJB
	private GestorGeometriasLocal gestorGeometrias;
	@EJB
	private OperadorDeterminacionesRefundidoLocal operadorDeterminaciones;
	@EJB
	private OperadorEntidadesRefundidoLocal operadorEntidades;
	@EJB(beanName = "refundido.replicador.sql")
	private ReplicadorRefundidoLocal replicador;

	/* (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.servicios.refundido.operaciones.planes.EjecutorPlanesLocal#ejecutar(int, int, es.mitc.redes.urbanismoenred.servicios.refundido.ContextoRefundido)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void ejecutar(int idOperado, int idOperador,
			ContextoRefundido contexto) throws ExcepcionRefundido {
		ResourceBundle traductor = (ResourceBundle)contexto.getParametro(ContextoRefundido.TRADUCTOR);
		
		es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Plan planOperador = em.find(es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Plan.class, idOperador);
		
		if (planOperador != null) {
			es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Plan planOperado = em.find(es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Plan.class, idOperado);
			
			contexto.log(LOG.INFO, String.format(traductor.getString("refundido.operacion.plan.sustitucion.mensaje"), planOperado.getCodigo(), planOperador.getCodigo()));
			// Se incorporan los elementos del plan operador.
			contexto.putParametro(RECALCULAR, true);
			es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Tramite tramiteIncorporado = incorporarPlan(planOperador, contexto);
	
			// Se verifica si hay entidades suspendidas y si las hay se recuperan
			// aquellas que intersecten con las entidades suspendidas.
			
			// Si el trámite sustituto tiene algunas entidades suspendidas, hay que incorporar del
	        //  trámite sustituído, las entidades que caen debajo de aquellas y que son
	        //  del mismo grupo de entidades. De esta manera, quedará vigente todo el trámite
	        //  sustituto, excepto en aquellos sitios donde esté suspendido, donde seguirá
	        //  vigente el contenido del trámite sustituído.
	        List<es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidad> entidadesSuspendidas = em.createNamedQuery("Entidad.BuscarSuspendidasPorTramite")
	        		.setParameter("idTramite", tramiteIncorporado.getIden())
	        		.getResultList();
	        
	        if (entidadesSuspendidas.size()>0) {
	        	contexto.log(LOG.AVISO, traductor.getString("refundido.operacion.plan.sustituir.aviso.entidadesSuspendidas"));
	        	
	        	Entidad carpetaEntidadesAportadas = null;
	            Entidad[] entidadesIntersectadas;
	            Determinacion grupoEntidadSuspendida;
	            Determinacion grupoEntidadIntersectada;
	            Entidad entidad;
	            Integer idProceso = (Integer)contexto.getParametro(ContextoRefundido.ID_PROCESO);
	            
	            // Utilizaré esta lista más adelante para repasar qué determinaciones
	            // deben ser también recuperadas.
	            List<Entidad> entidadesRecuperadas = new ArrayList<Entidad>();
	            List<Integer> entidadesEliminar = (List<Integer>)contexto.getParametro(ENTIDADES_A_ELIMINAR);
	            
	            for (es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidad entidadPlaneamiento : entidadesSuspendidas){
	            	entidad = replicador.getReferencia(idProceso, Entidad.class, entidadPlaneamiento.getIden());
	            	if (entidad != null) {
	                	// Se calcula qué entidades intersectan con la suspendida.
	                	entidadesIntersectadas = gestorGeometrias.calcularInterseccion(entidad, entidadesEliminar);
	                	
	                	// Sólo se incorporan aquellas entidades que intersecten y
	                	// pertenezcan al mismo grupo.
	                	try {
	    	            	grupoEntidadSuspendida = (Determinacion) em.createNamedQuery("refundido.Determinacion.obtenerGrupoEntidad")
	    	            			.setParameter("idEntidad", entidad.getIden())
	    	            			.setParameter("idCaracter", ClsDatos.ID_CARACTER_GRUPODEENTIDADES)
	    	            			.getSingleResult();
	    	            	
	    	            	for (Entidad intersectada : entidadesIntersectadas) {
	    	            		try {
	    	            			grupoEntidadIntersectada = (Determinacion) em.createNamedQuery("refundido.Determinacion.obtenerGrupoEntidad")
	    	    	            			.setParameter("idEntidad", intersectada.getIden())
	    	    	            			.setParameter("idCaracter", ClsDatos.ID_CARACTER_GRUPODEENTIDADES)
	    	    	            			.getSingleResult();
	    	            			if (grupoEntidadIntersectada.getIden() == grupoEntidadSuspendida.getIden()) {
	    	            				if (carpetaEntidadesAportadas == null) {
	    	            					carpetaEntidadesAportadas = operadorEntidades.obtenerCarpetaEntidadesAportadas(planOperado, contexto, true);
	    	            					// Si está marcada para eliminar lo cancelo, ya que se van a dejar
	    	            		        	// aquí las entidades que intersecten con las suspendidas.
	    	            					entidadesEliminar.remove(new Integer(carpetaEntidadesAportadas.getIden()));
	    	            		        	
	    	            				}
	    	            				
	    	            				// Se mueve la entidad a la carpeta de entidades aportadas.
	    	            				mover(intersectada, carpetaEntidadesAportadas, entidadesRecuperadas, entidadesEliminar);
	    	            			}
	    	                	} catch (NoResultException nre) {
	    	                		throw new ExcepcionRefundido("No se ha encontrado grupo de entidad asociado a la entidad suspendida " + entidad.getCodigo(), 8006);
	    	                	} catch (NonUniqueResultException nure) {
	    	                		throw new ExcepcionRefundido("Se han encontrado múltiples grupos de entidad asociados a la entidad suspendida " + entidad.getCodigo(), 8007);
	    	                	}
	    	            	}
	                	} catch (NoResultException nre) {
	                		throw new ExcepcionRefundido("No se ha encontrado grupo de entidad asociado a la entidad suspendida " + entidad.getCodigo(), 8008);
	                	} catch (NonUniqueResultException nure) {
	                		throw new ExcepcionRefundido("Se han encontrado múltiples grupos de entidad asociados a la entidad suspendida " + entidad.getCodigo(), 8009);
	                	}
	            	} else {
	            		log.warn("No se ha encontrado correspondencia con la entidad " + entidadPlaneamiento.getIden() + " al ejecutar la sustitución del plan.");
	            	}
	            }
	            
	            // Si se han recuperado entidades, se recuperan también las 
	            // determinaciones asociadas.
	            if (entidadesRecuperadas.size() > 0) {
	            	recuperarDeterminaciones(entidadesRecuperadas, contexto, planOperado.getCodigo());
	            }
	        }
	        em.flush();
	        
	        Tramite tramiteRefundido = (Tramite) contexto.getParametro(ContextoRefundido.TRAMITE_REFUNDIDO);
	        
	        // Recalculamos el orden de las carpetas de aportadas para que 
	        // siempre sean las últimas.
	        Integer orden = (Integer)em.createNamedQuery("refundido.Entidad.obtenerOrdenMaximo")
	        		.setParameter("idTramite", tramiteRefundido.getIden())
	        		.getSingleResult();
	        Entidad aportadas = operadorEntidades.obtenerCarpetaEntidadesAportadas(contexto);
	        
	        if (aportadas.getOrden() < orden) {
	        	aportadas.setOrden(orden+1);
	        }
	        orden = (Integer)em.createNamedQuery("refundido.Determinacion.obtenerOrdenMaximo")
	        		.setParameter("idTramite", tramiteRefundido.getIden())
	        		.getSingleResult();
	        
	        Determinacion determinacionAportadas = operadorDeterminaciones.obtenerCarpetaDeterminacionesAportadas(contexto);
	        
	        if (determinacionAportadas.getOrden() < orden) {
	        	determinacionAportadas.setOrden(orden+1);
	        }
	        
		} else {
			throw new ExcepcionRefundido(String.format(traductor.getString("refundido.operacion.plan.sustitucion.error.noOperador"), idOperador), 8010);
		}
	}

	/**
	 * Mueve una entidad marcada para borrar a la carpeta destino. Si la 
	 * entidad a mover tiene padre será el padre el que se intente pasar a la
	 * carpeta destino para mantener la jerarquía original.
	 * 
	 * @param entidad Entidad que se va a mover.
	 * @param destino Entidad destino.
	 * @param entidadesRecuperadas Lista de entidades marcadas para ser 
	 * recuperadas
	 * @param entidadesEliminar Lista de entidades marcadas para ser eliminadas
	 */
	private void mover(Entidad entidad, Entidad destino,
			List<Entidad> entidadesRecuperadas, List<Integer> entidadesEliminar) {
		if (entidadesEliminar.contains(entidad.getIden())) {
			log.debug("Cancelando la eliminación de la entidad con id " + entidad.getIden() + " " + entidad.getCodigo() + " nombre: " + entidad.getNombre());
			if (entidad.getEntidadByIdpadre() != null) {
				mover(entidad.getEntidadByIdpadre(), destino, entidadesRecuperadas, entidadesEliminar);
			} else {
				entidad.setEntidadByIdpadre(destino);
			}
			
			if (entidad.getEntidadByIdentidadbase() != null) {
				mover(entidad.getEntidadByIdentidadbase(),destino, entidadesRecuperadas, entidadesEliminar);
			}
			
			entidadesRecuperadas.add(entidad);
			// Se cancela la eliminación de la entidad intersectada.
			entidadesEliminar.remove(new Integer(entidad.getIden()));
		}
		
	}

	/*
	 * (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.servicios.refundido.operaciones.planes.IncorporacionAbstract#getReplicador()
	 */
	@Override
	protected ReplicadorRefundidoLocal getReplicador() {
		return replicador;
	}

	@Override
	protected EntityManager getEm() {
		return em;
	}
	
	/**
	 * Cancela la eliminación de aquellas determinaciones que directa o 
	 * indirectamente estén relacionadas con entidades recuperadas.
	 * Se van a recuperar las determinaciones relacionadas con la entidad
	 * a través de la clase entidaddeterminacion.
	 * Las determinaciones recuperadas se reubicarán en la carpeta de 
	 * determinaciones aportadas por el plan sustituido.
	 * 
	 * @param entidadesRecuperadas Lista de entidades recuperadas por su 
	 * intersección con entidades suspendidas del plan sustituto.
	 * @param contexto 
	 * @param codigoPlan 
	 */
	@SuppressWarnings("unchecked")
	private void recuperarDeterminaciones(
			List<Entidad> entidadesRecuperadas,
			ContextoRefundido contexto, 
			String codigoPlan) {
		for (Entidad entidad : entidadesRecuperadas) {
			em.refresh(entidad);
			for (Entidaddeterminacion ed : entidad.getEntidaddeterminacions()) {
				recuperarDeterminaciones(ed.getDeterminacion(), (List<Integer>)contexto.getParametro(DETERMINACIONES_A_ELIMINAR));
			}
		}
	}

	/**
	 * Cancela la eliminación de aquellas determinaciones que directa o 
	 * indirectamente estén relacionadas con entidades recuperadas.
	 * 
	 * Se van a recuperar aquellas determinaciones y sus determinaciones 
	 * original, padre y base.
	 * 
	 * @param determinacion Determinación que debe ser recuperada
	 * @param paraEliminar Contenedor de las determinaciones a eliminar.
	 */
	private void recuperarDeterminaciones(Determinacion determinacion,
			List<Integer> determinacionesEliminar) {
		if (determinacionesEliminar.contains(determinacion.getIden())) {
			em.refresh(determinacion);
			determinacionesEliminar.remove(new Integer(determinacion.getIden()));
			
			recuperarDeterminaciones(determinacion.getDeterminacionByIdpadre(), determinacionesEliminar);
			recuperarDeterminaciones(determinacion.getDeterminacionByIddeterminacionbase(), determinacionesEliminar);
			recuperarDeterminaciones(determinacion.getDeterminacionByIddeterminacionoriginal(), determinacionesEliminar);
		}
	}

}

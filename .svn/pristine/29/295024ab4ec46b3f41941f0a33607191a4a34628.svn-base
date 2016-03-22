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

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;

import es.mitc.redes.urbanismoenred.data.rpm.refundido.Determinacion;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Documento;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Entidad;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Tramite;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Traza;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.TrazaId;
import es.mitc.redes.urbanismoenred.servicios.refundido.ContextoRefundido;
import es.mitc.redes.urbanismoenred.servicios.refundido.OperadorDeterminacionesRefundidoLocal;
import es.mitc.redes.urbanismoenred.servicios.refundido.OperadorEntidadesRefundidoLocal;
import es.mitc.redes.urbanismoenred.servicios.refundido.ContextoRefundido.LOG;
import es.mitc.redes.urbanismoenred.servicios.refundido.ExcepcionRefundido;

/**
 * @author Arnaiz Consultores
 *
 */
@Stateless(name = "refundido.operacion.planes.proponerEliminar")
public class ProponerEliminar implements EjecutorLocal {
	
	private static final String DETERMINACIONES_A_ELIMINAR = "refundido.operaciones.plan.eliminar.determinaciones";
	private static final String DOCUMENTOS_A_ELIMINAR = "refundido.operaciones.plan.eliminar.documentos";
	private static final String ENTIDADES_A_ELIMINAR = "refundido.operaciones.plan.eliminar.entidades";
	
	private static final Logger log = Logger.getLogger(ProponerEliminar.class);
	
	@PersistenceContext(unitName = "rpmv2")
	private EntityManager em;
	
	@EJB
	private OperadorDeterminacionesRefundidoLocal operadorDeterminaciones;
	@EJB
	private OperadorEntidadesRefundidoLocal operadorEntidades;

	/* (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.servicios.refundido.operaciones.planes.EjecutorPlanesLocal#ejecutar(int, int, es.mitc.redes.urbanismoenred.servicios.refundido.ContextoRefundido)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void ejecutar(int idOperado, int idOperador,
			ContextoRefundido contexto) throws ExcepcionRefundido {
		es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Plan operador = em.find(es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Plan.class, idOperador);
		es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Plan operado = em.find(es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Plan.class, idOperado);
		
		contexto.logTraducido(LOG.INFO, "refundido.operacion.plan.proponer.eliminar.mensaje", operado.getCodigo(), operador.getCodigo());
		
		if (contexto.getParametro(DETERMINACIONES_A_ELIMINAR) == null)
			contexto.putParametro(DETERMINACIONES_A_ELIMINAR, new ArrayList<Integer>());
		if (contexto.getParametro(DOCUMENTOS_A_ELIMINAR) == null)
			contexto.putParametro(DOCUMENTOS_A_ELIMINAR, new ArrayList<Integer>());
		if (contexto.getParametro(ENTIDADES_A_ELIMINAR) == null)
			contexto.putParametro(ENTIDADES_A_ELIMINAR, new ArrayList<Integer>());
		proponerEliminar(em.find(es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Plan.class, idOperado), contexto);
		
		log.debug("Documentos propuestos para eliminar: " + ((List<Integer>)contexto.getParametro(DOCUMENTOS_A_ELIMINAR)).size());
		log.debug("Determinaciones propuestas para eliminar: " + ((List<Integer>)contexto.getParametro(DETERMINACIONES_A_ELIMINAR)).size());
		log.debug("Entidades propuestas para eliminar: " + ((List<Integer>)contexto.getParametro(ENTIDADES_A_ELIMINAR)).size());
	}
	
	/**
	 * 
	 * @param determinacion
	 * @param paraEliminar
	 */
	private void proponerEliminar(Determinacion determinacion, List<Integer> paraEliminar) {
		em.refresh(determinacion);
		if (!paraEliminar.contains(determinacion.getIden())) {
			paraEliminar.add(determinacion.getIden());
			for(Determinacion hija : determinacion.getDeterminacionsForIdpadre()) {
				proponerEliminar(hija,paraEliminar);
			}
		}
	}

	/**
	 * 
	 * @param entidad
	 * @param paraEliminar
	 */
	private void proponerEliminar(Entidad entidad, List<Integer> paraEliminar) {
		em.refresh(entidad);
		if (!paraEliminar.contains(entidad.getIden())) {
			paraEliminar.add(entidad.getIden());
			
			for(Entidad hija : entidad.getEntidadsForIdpadre()) {
				proponerEliminar(hija,paraEliminar);
			}
		}
	}
	
	/**
	 * La sustitución consta de cuatro pasos:
	 *  1 Proponer los elementos del plan sustituido que se van a eliminar.
	 *  2 Incorporar los elementos del plan que sustituye
	 *  3 Aplicar las incorporaciones indicadas explícitamente en el plan que
	 *  sustituye
	 *  4 Eliminar los elementos del plan sustituido que no hayan sido 
	 *  incorporados en el paso 3.
	 *  
	 *  Ese método ejecuta el paso 1.
	 *
	 * @param planOperado
	 * @param contexto
	 * @throws ExcepcionRefundido
	 */
	@SuppressWarnings("unchecked")
	private void proponerEliminar(es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Plan planOperado, ContextoRefundido contexto) throws ExcepcionRefundido {
		
		int idProceso = (int) contexto.getParametro(ContextoRefundido.ID_PROCESO);
		Tramite refundido = (Tramite) contexto.getParametro(ContextoRefundido.TRAMITE_REFUNDIDO);
		// Cuando se propone un plan para eliminar se proponen también sus hijos
		for (es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Plan hijo : planOperado.getPlansForIdpadre()) {
			proponerEliminar(hijo, contexto);
		}
		
		
		// Si es una figura principal sus entidades y determinaciones colgarán
		// directamente del trámite. En otro caso, colgarán de la carpeta
		// de entidades/determinaciones aportadas por...
		if (planOperado.getPlanByIdpadre() == null) {
			// DETERMINACIONES
			Determinacion determinacionesAportadas = operadorDeterminaciones.obtenerCarpetaDeterminacionesAportadas(planOperado, contexto, true);
			
			// La determinación aportada va a ser la única, por lo que le cambiamos su apartado		
			determinacionesAportadas.setApartado("1");
			
			List<Determinacion> raices = em.createNamedQuery("refundido.Determinacion.obtenerRaiz")
					.setParameter("idTramite", refundido.getIden())
					.getResultList();
			
			Determinacion carpetaAportadas = operadorDeterminaciones.obtenerCarpetaDeterminacionesAportadas(contexto);
			
			for (Determinacion hija: raices) {
				em.refresh(hija);
				if (hija.getIden() != carpetaAportadas.getIden()) {
					hija.setDeterminacionByIdpadre(determinacionesAportadas);
					em.flush();
					proponerEliminar(hija, (List<Integer>) contexto.getParametro(DETERMINACIONES_A_ELIMINAR));
				} else {
					// De la carpeta de determinaciones aportadas movemos las
					// correspondientes a cada plan.
					for(Determinacion aportadas : hija.getDeterminacionsForIdpadre()) {
						if (aportadas.getIden() != determinacionesAportadas.getIden()) {
							aportadas.setDeterminacionByIdpadre(determinacionesAportadas);
							em.flush();
						}
						proponerEliminar(aportadas, (List<Integer>) contexto.getParametro(DETERMINACIONES_A_ELIMINAR));
					}
				}
			}
			
			// ENTIDADES
			Entidad entidadesAportadas = operadorEntidades.obtenerCarpetaEntidadesAportadas(planOperado, contexto, true);
			
			List<Entidad> entidades = em.createNamedQuery("refundido.Entidad.obtenerRaiz")
					.setParameter("idTramite", refundido.getIden())
					.getResultList();
			
			Entidad carpetaEntidadesAportadas = operadorEntidades.obtenerCarpetaEntidadesAportadas(contexto);
			
			for (Entidad hija: entidades) {
				em.refresh(hija);
				if (hija.getIden() != carpetaEntidadesAportadas.getIden()) {
					hija.setEntidadByIdpadre(entidadesAportadas);
					em.flush();
					proponerEliminar(hija, (List<Integer>) contexto.getParametro(ENTIDADES_A_ELIMINAR));
				} else {
					// De la carpeta de determinaciones aportadas movemos las
					// correspondientes a cada plan.
					for(Entidad aportadas : hija.getEntidadsForIdpadre()) {
						if (aportadas.getIden() != entidadesAportadas.getIden()) {
							aportadas.setEntidadByIdpadre(entidadesAportadas);
							em.flush();
						} 
						proponerEliminar(aportadas, (List<Integer>) contexto.getParametro(ENTIDADES_A_ELIMINAR));
					}
				}
			}
			
			// DOCUMENTOS
			
			// Sólo se marcan los documentos del trámite, los que pertenecen a
			// determinaciones, a entidades, o a casos se eliminarán si se
			// elimina su elemento asociado.
			em.refresh(refundido);
			List<Integer> paraEliminar =  (List<Integer>) contexto.getParametro(DOCUMENTOS_A_ELIMINAR);
			for(Documento documento : refundido.getDocumentos()) {
				if (documento.getDocumentocasos().size() == 0 && documento.getDocumentodeterminacions().size() == 0 && documento.getDocumentoentidads().size() == 0) {
					if (!paraEliminar.contains(documento.getIden())) {
						paraEliminar.add(documento.getIden());
					}
				}
			}
		} else {
			Determinacion determinacionesAportadas = operadorDeterminaciones.obtenerCarpetaDeterminacionesAportadas(planOperado, contexto, false);
			
			if (determinacionesAportadas != null) {
				proponerEliminar(determinacionesAportadas, (List<Integer>) contexto.getParametro(DETERMINACIONES_A_ELIMINAR));
			}
			
			Entidad entidadesAportadas = operadorEntidades.obtenerCarpetaEntidadesAportadas(planOperado, contexto, true);
			
			if (entidadesAportadas != null) {
				proponerEliminar(entidadesAportadas, (List<Integer>) contexto.getParametro(ENTIDADES_A_ELIMINAR));
			}
			
			List<es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Tramite> vigentes = em.createNamedQuery("Tramite.findVigente")
					.setParameter("idPlan", planOperado.getIden())
					.getResultList();
			if (vigentes.size() > 0) {
				es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Tramite tramite = vigentes.get(0);
				
				List<es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Documento> documentos = em.createNamedQuery("Documento.buscarPorTramite")
						.setParameter("idTramite", tramite.getIden()).getResultList();
				Traza traza;
				List<Integer> paraEliminar =  (List<Integer>) contexto.getParametro(DOCUMENTOS_A_ELIMINAR);
				for (es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Documento doc : documentos) {
					traza = em.find(Traza.class, new TrazaId(idProceso, Documento.class.getSimpleName(), doc.getIden()));
					if (traza != null && !paraEliminar.contains(traza.getIdrefundido())) {
						paraEliminar.add(traza.getIdrefundido());
					}
				}
			} else {
				throw new ExcepcionRefundido("El plan operador " + planOperado.getCodigo() + " no tiene trámite vigente.", 8005);
			}
		}
		
		em.flush();
	}

}

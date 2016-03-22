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
package es.mitc.redes.urbanismoenred.servicios.refundido.operaciones.determinaciones;

import java.util.List;
import java.util.ResourceBundle;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import es.mitc.redes.urbanismoenred.data.rpm.refundido.Determinacion;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Entidaddeterminacionregimen;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Opciondeterminacion;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Regimenespecifico;
import es.mitc.redes.urbanismoenred.servicios.dal.EliminadorEntidadesRefundidoLocal;
import es.mitc.redes.urbanismoenred.servicios.refundido.ContextoRefundido;
import es.mitc.redes.urbanismoenred.servicios.refundido.ContextoRefundido.LOG;
import es.mitc.redes.urbanismoenred.servicios.refundido.CopiadorRefundidoLocal;
import es.mitc.redes.urbanismoenred.servicios.refundido.ExcepcionRefundido;

/**
 * 
 * Se efectúan las siguientes acciones relacionadas con la determinación que
 * se va a eliminar:
 * 1 - Sus determinaciones hijas se cuelgan de su determinación padre, en Determinacion.
 * 2 - Se ponen a "null" los idOpcionDeterminacion de EntidadDeterminacionRegimen que
 *     apunten alguna de las opciones que se van a eliminar, y se añade un régimen específico
 *     que avise de que las opciones se han eliminado debido a una operación.
 * 3 - Se ponen a "null" los idDeterminacionRegimen de EntidadDeterminacionRegimen que
 *     apunten a la determinación que se va a borrar, y se añade un régimen específico que
 *     avise de que la determinación de régimen se ha eliminado por una operación.         
 * 4 - Se eliminan la determinación operada y sus dependencias.
 * 
 * @author Arnaiz Consultores
 *
 */
@Stateless(name = "refundido.operacion.determinaciones.eliminacion")
public class Eliminar implements EjecutorLocal {
	
	@EJB ( beanName = "EliminadorEntidadesRefundido")
	private EliminadorEntidadesRefundidoLocal eliminadorEntidades;
	
	@PersistenceContext(unitName = "rpmv2")
	private EntityManager em;
	
	@EJB
	private CopiadorRefundidoLocal referencias;

	/* (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.servicios.refundido.operaciones.determinaciones.EjecutorLocal#ejecutar(int, int, es.mitc.redes.urbanismoenred.servicios.refundido.ContextoRefundido)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void ejecutar(int idOperada, int idOperadora,
			ContextoRefundido contexto) throws ExcepcionRefundido {
		es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Determinacion determinacionOperada = em.find(es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Determinacion.class, idOperada);
		es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Determinacion determinacionOperadora = em.find(es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Determinacion.class, idOperadora);
		
		contexto.logTraducido(LOG.INFO, "refundido.operacion.determinacion.eliminacion.mensaje", 
				determinacionOperada.getCodigo(), 
				determinacionOperada.getTramite().getPlan().getCodigo(),
				determinacionOperadora.getCodigo(),
				determinacionOperadora.getTramite().getPlan().getCodigo());
		
		Determinacion eliminada = referencias.getReferencia((int)contexto.getParametro(ContextoRefundido.ID_PROCESO), Determinacion.class, idOperada);
		Determinacion operadora = referencias.getReferencia((int)contexto.getParametro(ContextoRefundido.ID_PROCESO), Determinacion.class, idOperadora);
		
		if (!operadora.isBsuspendida()) {
			if (eliminada != null) {
				Integer orden;
				//  1 - Sus determinaciones hijas se cuelgan de su determinación padre, en Determinacion.
	        	for(Determinacion hija : eliminada.getDeterminacionsForIdpadre()) {
	        		hija.setDeterminacionByIdpadre(eliminada.getDeterminacionByIdpadre() == null? null: eliminada.getDeterminacionByIdpadre());
	        	}
	            
	        	// 2 - EntidadDeterminacionRegimen --> idOpcionDeterminacion=null
	            List<Opciondeterminacion> ods = em.createNamedQuery("refundido.Opciondeterminacion.buscarPorDetOValorRef")
	            		.setParameter("idDeterminacion", eliminada.getIden())
	            		.setParameter("idValorRef", eliminada.getIden())
	            		.getResultList();
	            
	            ResourceBundle traductor = (ResourceBundle)contexto.getParametro(ContextoRefundido.TRADUCTOR);
	            
	            Regimenespecifico regesp;
	            Query consulta = em.createNamedQuery("refundido.Regimenespecifico.obtenerMaxOrden");
	            for (Opciondeterminacion od: ods) {
	            	for (Entidaddeterminacionregimen edr : od.getEntidaddeterminacionregimens()) {
	            		edr.setOpciondeterminacion(null);
	            		regesp = new Regimenespecifico();
	            		regesp.setEntidaddeterminacionregimen(edr);
	            		regesp.setNombre(traductor.getString("refundido.valorModificado"));
	            		regesp.setTexto(
	            			String.format(traductor.getString("refundido.operacion.textoDeterminacionEliminada"), 
	            				determinacionOperada.getNombre(), 
	            				determinacionOperada.getTramite().getPlan().getCodigo()));
	            		
	            		orden = (Integer)consulta.setParameter("idEntDetReg", edr.getIden()).getSingleResult();
	            		
	            		regesp.setOrden(orden != null ? orden :1);
	            		em.persist(regesp);
	            		em.flush();
	            	}
	            }

	            // 3 - EntidadDeterminacionRegimen --> idDeterminacionRegimen=null
	            
	            for (Entidaddeterminacionregimen edr : eliminada.getEntidaddeterminacionregimens()) {
	            	
	            	edr.setDeterminacion(null);
	            	
	            	regesp = new Regimenespecifico();
	        		regesp.setEntidaddeterminacionregimen(edr);
	        		regesp.setNombre(traductor.getString("refundido.valorModificado"));
	        		regesp.setTexto(String.format(traductor.getString("refundido.operacion.textoDeterminacionRegimenEliminada"), 
	        				determinacionOperada.getNombre(), 
	        				determinacionOperada.getTramite().getPlan().getCodigo()));
	        		orden = (Integer)consulta.setParameter("idEntDetReg", edr.getIden()).getSingleResult();
	        		regesp.setOrden(orden != null ? orden :1);
	        		em.persist(regesp);
	            }

	            em.flush();
	            
	            //  4 - Elimina determinación
	            eliminadorEntidades.eliminar(eliminada);
	            
	            em.flush();
			} else {
				contexto.logTraducido(LOG.AVISO, "refundido.operacion.determinacion.eliminacion.aviso.determinacionEliminada");
			}
		} else {
			contexto.logTraducido(LOG.AVISO,"refundido.operacion.determinacion.aviso.operadoraSuspendida");
		}
	} 

}

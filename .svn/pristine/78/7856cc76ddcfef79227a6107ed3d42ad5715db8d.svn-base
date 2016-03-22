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

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import es.mitc.redes.urbanismoenred.data.rpm.refundido.Determinacion;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Relacion;
import es.mitc.redes.urbanismoenred.servicios.dal.EliminadorEntidadesRefundidoLocal;
import es.mitc.redes.urbanismoenred.servicios.refundido.ContextoRefundido;
import es.mitc.redes.urbanismoenred.servicios.refundido.ContextoRefundido.LOG;
import es.mitc.redes.urbanismoenred.servicios.refundido.CopiadorRefundidoLocal;
import es.mitc.redes.urbanismoenred.servicios.refundido.ExcepcionRefundido;

/**
 * 
 * Se compone de cinco pasos:
 * 1 Eliminación normativa. Se eliminan todas las propiedades y vectores
 * relacionados con la regulación
 * 2 Aplicación de la operación adicionNormativa
 * 3 Sustituir nombre y texto
 * 
 * @author Arnaiz Consultores
 *
 */
@Stateless(name = "refundido.operacion.determinaciones.sustitucionNormativa")
public class SustitucionNormativa implements EjecutorLocal {
	
	@EJB ( beanName = "EliminadorEntidadesRefundido")
	private EliminadorEntidadesRefundidoLocal eliminadorEntidades;
	
	@PersistenceContext(unitName = "rpmv2")
	private EntityManager em;
	
	@EJB
	private CopiadorRefundidoLocal referencias;
	
	@EJB (beanName = "refundido.operacion.determinaciones.adicionNormativa" )
	private EjecutorLocal adicion;

	/* (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.servicios.refundido.operaciones.determinaciones.EjecutorLocal#ejecutar(int, int, es.mitc.redes.urbanismoenred.servicios.refundido.ContextoRefundido)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void ejecutar(int idOperada, int idOperadora,
			ContextoRefundido contexto) throws ExcepcionRefundido {
		
		es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Determinacion operadoraPlaneamiento = em.find(es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Determinacion.class, 
				 idOperadora);
		es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Determinacion operadaPlaneamiento = em.find(es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Determinacion.class, 
				 idOperada);
		
		contexto.logTraducido(LOG.INFO, "refundido.operacion.determinacion.sustitucion.mensaje", 
				operadoraPlaneamiento.getCodigo(), 
				operadoraPlaneamiento.getTramite().getPlan().getCodigo(), 
				operadaPlaneamiento.getCodigo(),
				operadaPlaneamiento.getTramite().getPlan().getCodigo());
		
		Determinacion operada = referencias.getReferencia((int)contexto.getParametro(ContextoRefundido.ID_PROCESO), Determinacion.class, idOperada);	
    	Determinacion operadora = referencias.getReferencia((int)contexto.getParametro(ContextoRefundido.ID_PROCESO), Determinacion.class, idOperadora);
		
		if (operada != null && operadora != null) {
			if (!operadora.isBsuspendida()) {
				// 1 Eliminación normativa
				// 1.1 Regulaciones específicas
				List<Relacion> relaciones = em.createNamedQuery("refundido.Relacion.obtenerRegulacionesEspecificas")
						.setParameter("idDeterminacion", operada.getIden())
						.getResultList();
				for (Relacion regulacion : relaciones) {
					eliminadorEntidades.eliminar(regulacion);
				}
				
				// 1.2 Relaciones donde la determinación operada es la determinación regulada
				relaciones = em.createNamedQuery("refundido.Relacion.obtenerReguladas")
						.setParameter("idDeterminacion", operada.getIden())
						.getResultList();
				
				for (Relacion relacion : relaciones) {
					eliminadorEntidades.eliminar(relacion);
				}
				
				// 2 Adición normativa
				adicion.ejecutar(idOperada, idOperadora, contexto);
				
				// 3 Sustitución de nombre, texto y apartado
				operada.setApartado(operadora.getApartado());
				operada.setNombre(operadora.getNombre());
				operada.setTexto(operadora.getTexto());
			} else {
				contexto.logTraducido(LOG.AVISO,"refundido.operacion.determinacion.aviso.operadoraSuspendida");
			}
		} else {
    		
    		if (operada == null) {
    			contexto.logTraducido(LOG.AVISO,"refundido.operacion.determinacion.aviso.noOperada");
    		}
    		
    		if (operadora == null) {
    			contexto.logTraducido(LOG.AVISO, "refundido.operacion.determinacion.aviso.noOperadora");
    		} 		
    	}
	}
}

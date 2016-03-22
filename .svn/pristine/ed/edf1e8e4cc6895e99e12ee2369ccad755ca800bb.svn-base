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

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Plan;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Determinacion;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Documento;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Entidad;
import es.mitc.redes.urbanismoenred.servicios.dal.EliminadorEntidadesRefundidoLocal;
import es.mitc.redes.urbanismoenred.servicios.refundido.ContextoRefundido;
import es.mitc.redes.urbanismoenred.servicios.refundido.ExcepcionRefundido;

/**
 * @author Arnaiz Consultores
 *
 */
@Stateless(name = "refundido.operacion.planes.eliminarPropuestos")
public class EliminarPropuestos implements EjecutorLocal {
	
	private static final String DETERMINACIONES_A_ELIMINAR = "refundido.operaciones.plan.eliminar.determinaciones";
	private static final String DOCUMENTOS_A_ELIMINAR = "refundido.operaciones.plan.eliminar.documentos";
	private static final String ENTIDADES_A_ELIMINAR = "refundido.operaciones.plan.eliminar.entidades";
	
	@PersistenceContext(unitName = "rpmv2")
	private EntityManager em;

	@EJB (beanName = "EliminadorEntidadesRefundido")
	private EliminadorEntidadesRefundidoLocal eliminador;
	

	/* (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.servicios.refundido.operaciones.planes.EjecutorPlanesLocal#ejecutar(int, int, es.mitc.redes.urbanismoenred.servicios.refundido.ContextoRefundido)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void ejecutar(int idOperado, int idOperador,
			ContextoRefundido contexto) throws ExcepcionRefundido {
		Plan operador = em.find(Plan.class, idOperador);
		Plan operado = em.find(Plan.class, idOperado);
		
		contexto.logTraducido(ContextoRefundido.LOG.INFO, "refundido.operacion.plan.eliminar.propuestos.mensaje", operado.getCodigo(), operador.getCodigo());
		
		Documento doc;
		List<Integer> paraEliminar = (List<Integer>) contexto.getParametro(DOCUMENTOS_A_ELIMINAR);
		if (paraEliminar != null) {
			for (Integer idDocumento : paraEliminar) {
				doc = em.find(Documento.class, idDocumento);
				
				if (doc != null) {
					eliminador.eliminar(doc);
				}
			}
			
			paraEliminar.clear();
		}
		
		paraEliminar = (List<Integer>) contexto.getParametro(ENTIDADES_A_ELIMINAR);
		if (paraEliminar != null) {
			Entidad ent;
			for (Integer idEntidad : paraEliminar) {
				ent = em.find(Entidad.class, idEntidad);
				if (ent != null) {
					eliminador.eliminar(ent);
				}
			}
			
			paraEliminar.clear();
		}
		
		paraEliminar = (List<Integer>) contexto.getParametro(DETERMINACIONES_A_ELIMINAR);
		if (paraEliminar != null) {
			Determinacion det;
			for(Integer idDeterminacion : paraEliminar) {
				det = em.find(Determinacion.class, idDeterminacion);
				if (det != null) {
					eliminador.eliminar(det);
				}
			}
			
			paraEliminar.clear();
		}
	}

}

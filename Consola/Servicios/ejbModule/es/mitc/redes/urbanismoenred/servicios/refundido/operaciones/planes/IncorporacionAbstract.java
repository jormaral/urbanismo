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

import javax.persistence.EntityManager;

import es.mitc.redes.urbanismoenred.data.rpm.refundido.Tramite;
import es.mitc.redes.urbanismoenred.servicios.refundido.ContextoRefundido;
import es.mitc.redes.urbanismoenred.servicios.refundido.ExcepcionRefundido;
import es.mitc.redes.urbanismoenred.servicios.refundido.ReplicadorRefundidoLocal;

/**
 * @author Arnaiz Consultores
 *
 */
public abstract class IncorporacionAbstract {
	
	protected static final String RECALCULAR = "refundido.incorporacion.recalcular";
	/**
	 * La operación de incorporación introduce todo el contenido de un 
	 * trámite en el trámite refundido.
	 * Si no existe el trámite refundido lo crea, creando el plan 
	 * correspondiente.
	 * 
	 * @param planOperador Plan que se va a incorporar.
	 * @param contexto Contexto de refundido sobre el que se ejecuta.
	 * @return Devuelve el trámite incorporado al refundido.
	 * @throws ExcepcionRefundido 
	 */
	@SuppressWarnings("unchecked")
	protected es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Tramite incorporarPlan( es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Plan planOperador,
			ContextoRefundido contexto) throws ExcepcionRefundido {
		Tramite tramiteRefundido = (Tramite)contexto.getParametro(ContextoRefundido.TRAMITE_REFUNDIDO);
		
		if (tramiteRefundido == null) {
			es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Tramite esqueleto = (es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Tramite)contexto.getParametro(ContextoRefundido.ESQUELETO_TRAMITE_REFUNDIDO);
			
			tramiteRefundido = getReplicador().copiar((int)contexto.getParametro(ContextoRefundido.ID_PROCESO), 
					esqueleto,
					null,
					false);
			
			contexto.putParametro(ContextoRefundido.TRAMITE_REFUNDIDO, tramiteRefundido);
			contexto.putParametro(RECALCULAR, false);
		} 
		
		List<es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Tramite> vigentes = getEm().createNamedQuery("Tramite.findVigente")
				.setParameter("idPlan", planOperador.getIden())
				.getResultList();
		if (vigentes.size() > 0) {
			
			getReplicador().copiar((Integer)contexto.getParametro(ContextoRefundido.ID_PROCESO), 
					vigentes.get(0), 
					tramiteRefundido,
					contexto.getParametro(RECALCULAR) != null ? (boolean)contexto.getParametro(RECALCULAR) : false);
			
			return vigentes.get(0);
		} else {
			throw new ExcepcionRefundido("El plan operador " + planOperador.getCodigo() + " no tiene trámite vigente.", 8004);
		}
	}
	
	/**
	 * 
	 * @return
	 */
	protected abstract ReplicadorRefundidoLocal getReplicador();
	
	/**
	 * 
	 * @return
	 */
	protected abstract EntityManager getEm();
}

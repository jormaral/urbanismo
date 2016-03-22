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

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import es.mitc.redes.urbanismoenred.data.rpm.refundido.Determinacion;
import es.mitc.redes.urbanismoenred.servicios.refundido.ContextoRefundido;
import es.mitc.redes.urbanismoenred.servicios.refundido.ContextoRefundido.LOG;
import es.mitc.redes.urbanismoenred.servicios.refundido.CopiadorRefundidoLocal;
import es.mitc.redes.urbanismoenred.servicios.refundido.ExcepcionRefundido;

/**
 * Marca la determinación operada como no suspendida.
 * 
 * @author Arnaiz Consultores
 *
 */
@Stateless( name = "refundido.operacion.determinaciones.levantamientoSuspension")
public class LevantamientoSuspension implements EjecutorLocal {
	
	@PersistenceContext(unitName = "rpmv2")
	private EntityManager em;
	
	@EJB
	private CopiadorRefundidoLocal referencias;

	/* (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.servicios.refundido.operaciones.determinaciones.EjecutorLocal#ejecutar(int, int, es.mitc.redes.urbanismoenred.servicios.refundido.ContextoRefundido)
	 */
	@Override
	public void ejecutar(int idOperada, int idOperadora,
			ContextoRefundido contexto) throws ExcepcionRefundido {
		es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Determinacion determinacionOperada = em.find(es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Determinacion.class, idOperada);
		es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Determinacion determinacionOperadora = em.find(es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Determinacion.class, idOperadora);
		
		contexto.logTraducido(LOG.INFO, "refundido.operacion.determinacion.levantamiento.mensaje",
				determinacionOperada.getCodigo(), 
				determinacionOperada.getTramite().getPlan().getCodigo(), 
				determinacionOperadora.getCodigo(), 
				determinacionOperadora.getTramite().getPlan().getCodigo());
		
		Determinacion operada = referencias.getReferencia((int)contexto.getParametro(ContextoRefundido.ID_PROCESO), Determinacion.class, idOperada);
		Determinacion operadora = referencias.getReferencia((int)contexto.getParametro(ContextoRefundido.ID_PROCESO), Determinacion.class, idOperadora);
	
		if (operadora != null && !operadora.isBsuspendida()) {
			if (operada != null) {
				operada.setBsuspendida(false);
			} else {
				contexto.logTraducido(LOG.AVISO,"refundido.operacion.determinacion.aviso.noOperada");
			}
		} else {
			if (operadora == null) {
				contexto.logTraducido(LOG.AVISO,"refundido.operacion.determinacion.aviso.noOperadora");
			} else {
				contexto.logTraducido(LOG.AVISO,"refundido.operacion.determinacion.aviso.operadoraSuspendida");
			}
		}
	}

}

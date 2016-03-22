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

import java.util.ResourceBundle;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import es.mitc.redes.urbanismoenred.servicios.refundido.ContextoRefundido;
import es.mitc.redes.urbanismoenred.servicios.refundido.ContextoRefundido.LOG;
import es.mitc.redes.urbanismoenred.servicios.refundido.ExcepcionRefundido;
import es.mitc.redes.urbanismoenred.servicios.refundido.ReplicadorRefundidoLocal;

/**
 * La operación de incorporación introduce todo el contenido de un 
 * trámite en el trámite refundido.
 * Si no existe el trámite refundido lo crea, creando el plan 
 * correspondiente.
 * 
 * @author Arnaiz Consultores
 *
 */
@Stateless(name = "refundido.operacion.planes.incorporacion")
public class Incorporacion extends IncorporacionAbstract implements EjecutorLocal {
	
	@PersistenceContext(unitName = "rpmv2")
	private EntityManager em;
	
	@EJB (beanName = "refundido.replicador.sql")
	private ReplicadorRefundidoLocal replicador;

	/* (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.servicios.refundido.operaciones.planes.EjecutorPlanesLocal#ejecutar(int, int, es.mitc.redes.urbanismoenred.servicios.refundido.ContextoRefundido)
	 */
	@Override
	public void ejecutar(int idOperado, int idOperador,
			ContextoRefundido contexto) throws ExcepcionRefundido {
		
		es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Plan planOperador = em.find(es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Plan.class, idOperador);
		
		if (planOperador != null) {
			contexto.logTraducido(LOG.INFO, "refundido.operacion.plan.incorporacion.mensaje", planOperador.getCodigo());
			contexto.putParametro(RECALCULAR, false);
			this.incorporarPlan(planOperador, contexto);
		} else {
			ResourceBundle traductor = (ResourceBundle) contexto.getParametro(ContextoRefundido.TRADUCTOR);
			throw new ExcepcionRefundido(String.format(traductor.getString("refundido.operacion.plan.incorporacion.error.noOperador"), idOperador), 8003);
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

	/*
	 * (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.servicios.refundido.operaciones.planes.IncorporacionAbstract#getEm()
	 */
	@Override
	protected EntityManager getEm() {
		return em;
	}

}

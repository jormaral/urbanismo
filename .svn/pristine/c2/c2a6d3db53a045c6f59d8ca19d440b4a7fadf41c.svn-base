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
import java.util.ResourceBundle;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Plan;
import es.mitc.redes.urbanismoenred.servicios.refundido.ContextoRefundido;
import es.mitc.redes.urbanismoenred.servicios.refundido.ExcepcionRefundido;
import es.mitc.redes.urbanismoenred.servicios.refundido.GestorAportacionesLocal;

/**
 * Esta operación se ha incluido como una optimización del proceso de aportación 
 * de los datos de un trámite al refundido.
 * 
 * Es imprescindible que los datos del plan operado se incorpore antes que los
 * del plan operador, para que se aporten correctamente todos los datos del
 * plan operador.
 * 
 * 
 * @author Arnaiz Consultores
 *
 */
@Stateless(name = "refundido.operacion.planes.aportacion")
public class Aportacion implements EjecutorLocal {
	
	@PersistenceContext(unitName = "rpmv2")
	private EntityManager em;
	
	@EJB
	private GestorAportacionesLocal gestorAportaciones; 

	/* (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.servicios.refundido.operaciones.planes.EjecutorLocal#ejecutar(int, int, es.mitc.redes.urbanismoenred.servicios.refundido.ContextoRefundido)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void ejecutar(int idOperado, int idOperador,
			ContextoRefundido contexto) throws ExcepcionRefundido {
		
		Plan operado = em.find(Plan.class, idOperado);
		
		contexto.logTraducido(ContextoRefundido.LOG.INFO, "refundido.operacion.plan.aportacion.mensaje", operado.getCodigo());
		
		List<es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Tramite> vigentes = em.createNamedQuery("Tramite.findVigente")
				.setParameter("idPlan", operado.getIden())
				.getResultList();
		if (vigentes.size() > 0) {
			gestorAportaciones.aportar(vigentes.get(0), contexto);
		} else {
			ResourceBundle traductor = (ResourceBundle) contexto.getParametro(ContextoRefundido.TRADUCTOR);
			
			throw new ExcepcionRefundido(traductor.getString("refundido.operacion.plan.aportacion.error.sintramites"), 8001);
		}
		
		Plan operador = em.find(Plan.class, idOperador);
		
		contexto.logTraducido(ContextoRefundido.LOG.INFO, "refundido.operacion.plan.aportacion.mensaje", operador.getCodigo());
		
		vigentes = em.createNamedQuery("Tramite.findVigente")
				.setParameter("idPlan", operador.getIden())
				.getResultList();
		if (vigentes.size() > 0) {
			gestorAportaciones.aportar(vigentes.get(0), contexto);
		} else {
			ResourceBundle traductor = (ResourceBundle) contexto.getParametro(ContextoRefundido.TRADUCTOR);
			
			throw new ExcepcionRefundido(traductor.getString("refundido.operacion.plan.aportacion.error.sintramites"), 8002);
		}
	}

}

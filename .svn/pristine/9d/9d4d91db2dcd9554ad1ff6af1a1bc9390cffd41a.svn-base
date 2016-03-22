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

import java.util.ResourceBundle;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import es.mitc.redes.urbanismoenred.servicios.refundido.ContextoRefundido;
import es.mitc.redes.urbanismoenred.servicios.refundido.ExcepcionRefundido;
import es.mitc.redes.urbanismoenred.servicios.refundido.GestorAportacionesLocal;
import es.mitc.redes.urbanismoenred.servicios.refundido.OperadorDeterminacionesRefundidoLocal;
import es.mitc.redes.urbanismoenred.servicios.refundido.ContextoRefundido.LOG;

/**
 * Se incorpora la determinación operada al trámite refundido reubicándola
 * bajo la carpeta de determinaciones Aportadas por del plan al que pertenece
 * la operadora. 
 * 
 * Si la determinación operadora tiene una determinación padre
 * también se aportará para mantener la jerarquía y el orden original.
 * 
 * @author Arnaiz Consultores
 *
 */
@Stateless(name = "refundido.operacion.determinaciones.aportacion")
public class Aportacion implements EjecutorLocal {
	
	@PersistenceContext(unitName = "rpmv2")
	private EntityManager em;
	
	@EJB
	private GestorAportacionesLocal gestorAportaciones;
	
	@EJB
	private OperadorDeterminacionesRefundidoLocal operadorDeterminaciones;

	/* (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.servicios.refundido.operaciones.determinaciones.EjecutorLocal#ejecutar(int, int, es.mitc.redes.urbanismoenred.servicios.refundido.ContextoRefundido)
	 */
	@Override
	public void ejecutar(int idOperada, int idOperadora,
			ContextoRefundido contexto) throws ExcepcionRefundido {
		es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Determinacion operadora = em.find(es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Determinacion.class, idOperadora);
		
		if (operadora != null) {
			contexto.logTraducido(LOG.INFO, "refundido.operacion.determinacion.aportacion.mensaje", 
					operadora.getCodigo(), 
					operadora.getTramite().getPlan().getCodigo());
			if (!operadora.isBsuspendida()) {
				gestorAportaciones.aportar(operadora, 
						operadorDeterminaciones.obtenerCarpetaDeterminacionesAportadas(operadora.getTramite().getPlan(), contexto, true), 
						null, 
						contexto);
			} else {
				contexto.logTraducido(LOG.AVISO,"refundido.operacion.determinacion.aviso.operadoraSuspendida");
			}
		} else {
			ResourceBundle traductor = (ResourceBundle) contexto.getParametro(ContextoRefundido.TRADUCTOR);
			throw new ExcepcionRefundido(String.format(traductor.getString("refundido.excepcion.determinacion.aportar.noOperadora"), idOperadora), 6001);
		}
	}

}

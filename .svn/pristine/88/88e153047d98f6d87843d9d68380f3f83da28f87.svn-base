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

import es.mitc.redes.urbanismoenred.data.rpm.refundido.Determinacion;
import es.mitc.redes.urbanismoenred.servicios.refundido.ContextoRefundido;
import es.mitc.redes.urbanismoenred.servicios.refundido.ContextoRefundido.LOG;
import es.mitc.redes.urbanismoenred.servicios.refundido.CopiadorRefundidoLocal;
import es.mitc.redes.urbanismoenred.servicios.refundido.ExcepcionRefundido;
import es.mitc.redes.urbanismoenred.servicios.refundido.GestorAportacionesLocal;

/**
 * 
 * Se cambia el padre de la determinacion operadora y se le asigna el padre
 * de la determinación operada, asignándole el orden siguiente al de la
 * operada.
 * 
 * @author Arnaiz Consultores
 *
 */
@Stateless(name = "refundido.operacion.determinaciones.aportacionSobreAnterior")
public class AportacionSobreAnterior implements EjecutorLocal {
	
	@PersistenceContext(unitName = "rpmv2")
	private EntityManager em;
	
	@EJB
	private CopiadorRefundidoLocal copiador;
	@EJB
	private GestorAportacionesLocal gestorAportaciones;

	/* (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.servicios.refundido.operaciones.determinaciones.EjecutorLocal#ejecutar(int, int, es.mitc.redes.urbanismoenred.servicios.refundido.ContextoRefundido)
	 */
	@Override
	public void ejecutar(int idOperada, int idOperadora,
			ContextoRefundido contexto) throws ExcepcionRefundido {
		
		es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Determinacion aportada = em.find(es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Determinacion.class, idOperadora);
		
		Determinacion referencia = copiador.getReferencia((Integer)contexto.getParametro(ContextoRefundido.ID_PROCESO), Determinacion.class, idOperada);
		
		if (aportada != null) {
			contexto.logTraducido(LOG.INFO, "refundido.operacion.determinacion.aportarAnterior.mensaje", aportada.getCodigo(), aportada.getTramite().getPlan().getCodigo());
			if (!aportada.isBsuspendida()) {
				if (referencia != null) {
					gestorAportaciones.aportar(aportada, 
							referencia.getDeterminacionByIdpadre(), 
							referencia.getOrden()+1, contexto);
				} else {
					contexto.logTraducido(LOG.AVISO,"refundido.operacion.determinacion.aviso.noOperada");
				}
			} else {
				contexto.logTraducido(LOG.AVISO,"refundido.operacion.determinacion.aviso.operadoraSuspendida");
			}
		} else {
			ResourceBundle traductor = (ResourceBundle)contexto.getParametro(ContextoRefundido.TRADUCTOR);
			throw new ExcepcionRefundido(String.format(traductor.getString("refundido.excepcion.determinacion.aportar.noOperadora"), idOperadora), 6002);
		}
	}

}

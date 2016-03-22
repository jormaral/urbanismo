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
package es.mitc.redes.urbanismoenred.servicios.refundido.operaciones.entidades;

import java.util.ResourceBundle;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import es.mitc.redes.urbanismoenred.data.rpm.refundido.Entidad;
import es.mitc.redes.urbanismoenred.servicios.refundido.ContextoRefundido;
import es.mitc.redes.urbanismoenred.servicios.refundido.ContextoRefundido.LOG;
import es.mitc.redes.urbanismoenred.servicios.refundido.CopiadorRefundidoLocal;
import es.mitc.redes.urbanismoenred.servicios.refundido.ExcepcionRefundido;
import es.mitc.redes.urbanismoenred.servicios.refundido.GestorAportacionesLocal;

/**
 * 
 * @author Arnaiz Consultores
 *
 */
@Stateless(name = "refundido.operacion.entidades.aportacionSobreAnterior")
public class AportacionSobreAnterior implements EjecutorLocal {
	
	@PersistenceContext(unitName = "rpmv2")
	private EntityManager em;
	
	@EJB
	private GestorAportacionesLocal gestorAportaciones;
	@EJB
	private CopiadorRefundidoLocal referencias;


	/* (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.servicios.refundido.operaciones.entidades.EjecutorLocal#ejecutar(int, int, es.mitc.redes.urbanismoenred.servicios.refundido.ContextoRefundido)
	 */
	@Override
	public void ejecutar(int idOperada, int idOperadora,
			ContextoRefundido contexto) throws ExcepcionRefundido {
		
		es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidad operadoraPlaneamiento = em.find(es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidad.class, idOperadora);
		es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidad operadaPlaneamiento = em.find(es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidad.class, idOperada);
		
		contexto.logTraducido(LOG.INFO, "refundido.operacion.entidad.aportacionSobreAnterior.mensaje", 
				operadoraPlaneamiento.getCodigo(), 
				operadoraPlaneamiento.getTramite().getPlan().getCodigo(), 
				operadaPlaneamiento.getCodigo(),
				operadaPlaneamiento.getTramite().getPlan().getCodigo());
		
		Entidad operadora = referencias.getReferencia((int)contexto.getParametro(ContextoRefundido.ID_PROCESO), Entidad.class, idOperadora);
		if (operadora != null) {
			if (!operadora.isBsuspendida()) {
				Entidad operada = gestorAportaciones.aportar(operadaPlaneamiento, null, null, contexto);
				
				if (operada != null) {
					if (operada.getEntidadByIdpadre() != null) {
						gestorAportaciones.aportar(operadoraPlaneamiento,
								operada.getEntidadByIdpadre(), 
								operada.getOrden()+1,
								contexto);
					} else {
						ResourceBundle traductor = (ResourceBundle)contexto.getParametro(ContextoRefundido.TRADUCTOR);
						
						throw new ExcepcionRefundido(String.format(traductor.getString("refundido.operacion.entidad.aportacionSobreAnterior.error.noPadre"), operada.getCodigo(), operada.getTramite().getPlan().getCodigo()), 7011);
					}
				} else {
					contexto.logTraducido(LOG.AVISO,"refundido.operacion.entidad.aviso.noOperada");
				}
			} else {
				contexto.logTraducido(LOG.AVISO,"refundido.operacion.entidad.aviso.operadoraSuspendida");
			}
		} else {
			contexto.logTraducido(LOG.AVISO,"refundido.operacion.entidad.aviso.noOperadora");
		}
	}

}

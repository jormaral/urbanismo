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

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import es.mitc.redes.urbanismoenred.data.rpm.refundido.Entidad;
import es.mitc.redes.urbanismoenred.servicios.dal.EliminadorEntidadesRefundidoLocal;
import es.mitc.redes.urbanismoenred.servicios.refundido.ContextoRefundido;
import es.mitc.redes.urbanismoenred.servicios.refundido.ContextoRefundido.LOG;
import es.mitc.redes.urbanismoenred.servicios.refundido.ExcepcionRefundido;
import es.mitc.redes.urbanismoenred.servicios.refundido.GestorAportacionesLocal;

/**
 * 
 * Se efectúan las siguientes acciones relacionadas con la entidad operada que
 * se va a eliminar:
 * 1 - Reasigna los idPadre de los hijos, al idPadre de la entidad.
 * 2 - Se elimina la entidad.
 * 
 * @author Arnaiz Consultores
 *
 */
@Stateless( name = "refundido.operacion.entidades.eliminacion")
public class Eliminacion implements EjecutorLocal {
	
	@PersistenceContext(unitName = "rpmv2")
	private EntityManager em;
	
	@EJB
	private GestorAportacionesLocal gestorAportaciones;
	
	@EJB ( beanName = "EliminadorEntidadesRefundido" )
	private EliminadorEntidadesRefundidoLocal eliminador;

	/* (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.servicios.refundido.operaciones.entidades.EjecutorLocal#ejecutar(int, int, es.mitc.redes.urbanismoenred.servicios.refundido.ContextoRefundido)
	 */
	@Override
	public void ejecutar(int idOperada, int idOperadora,
			ContextoRefundido contexto) throws ExcepcionRefundido {
		es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidad operadoraPlaneamiento = em.find(es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidad.class, idOperadora);
		es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidad operadaPlaneamiento = em.find(es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidad.class, idOperada);
		
		contexto.logTraducido(LOG.INFO, "refundido.operacion.entidad.eliminacion.mensaje", 
				operadaPlaneamiento.getCodigo(),
				operadaPlaneamiento.getTramite().getPlan().getCodigo(), 
				operadoraPlaneamiento.getCodigo(), 
				operadoraPlaneamiento.getTramite().getPlan().getCodigo());
		
		Entidad operada = gestorAportaciones.aportar(operadaPlaneamiento, null, null, contexto);
		Entidad operadora = gestorAportaciones.aportar(operadoraPlaneamiento, null, null, contexto);
		
		if (operada != null && operadora != null) {
			if (!operadora.isBsuspendida()) {
				for(Entidad hija : operada.getEntidadsForIdpadre()) {
					hija.setEntidadByIdpadre(operada.getEntidadByIdpadre());
				}
				
				eliminador.eliminar(operada);
			} else {
				contexto.logTraducido(LOG.AVISO,"refundido.operacion.entidad.aviso.operadoraSuspendida");
			}
		} else {
			if (operada == null) {
    			contexto.logTraducido(LOG.AVISO,"refundido.operacion.entidad.aviso.noOperada");
    		}
			if (operadora == null) {
    			contexto.logTraducido(LOG.AVISO, "refundido.operacion.entidad.aviso.noOperadora");
    		}
		}
	}

}

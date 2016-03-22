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
import es.mitc.redes.urbanismoenred.servicios.refundido.ContextoRefundido;
import es.mitc.redes.urbanismoenred.servicios.refundido.ContextoRefundido.LOG;
import es.mitc.redes.urbanismoenred.servicios.refundido.CopiadorRefundidoLocal;
import es.mitc.redes.urbanismoenred.servicios.refundido.ExcepcionRefundido;
import es.mitc.redes.urbanismoenred.servicios.refundido.GestorAportacionesLocal;
import es.mitc.redes.urbanismoenred.servicios.refundido.OperadorEntidadesRefundidoLocal;

/**
 * Aporta la entidad al refundido bajo la carpeta "entidades aportadas por [...]"
 * correspondientes al plan operador.
 * 
 * @author Arnaiz Consultores
 *
 */
@Stateless( name = "refundido.operacion.entidades.aportacion")
public class Aportacion implements EjecutorLocal {
	
	@PersistenceContext(unitName = "rpmv2")
	private EntityManager em;
	
	@EJB
	private GestorAportacionesLocal gestorAportaciones;
	
	@EJB
	private OperadorEntidadesRefundidoLocal operadorEntidades;
	
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
		
		contexto.logTraducido(LOG.INFO, "refundido.operacion.entidad.aportacion.mensaje", 
				operadoraPlaneamiento.getCodigo(), 
				operadoraPlaneamiento.getTramite().getPlan().getCodigo(), 
				operadaPlaneamiento.getCodigo(),
				operadaPlaneamiento.getTramite().getPlan().getCodigo());
		
		Entidad operadora = referencias.getReferencia((int)contexto.getParametro(ContextoRefundido.ID_PROCESO), Entidad.class, idOperadora);
		if (operadora != null) {
			if (!operadora.isBsuspendida()) {
				Entidad aportadas = operadorEntidades.obtenerCarpetaEntidadesAportadas(operadoraPlaneamiento.getTramite().getPlan(), contexto, true);
				
				int orden = 0;
				
				em.refresh(aportadas);
				
				for (Entidad hija : aportadas.getEntidadsForIdpadre()) {
					if (hija.getOrden() > orden) {
						orden = hija.getOrden();
					}
				}
				// Aporta la entidad al plan, no a una entidad concreta.
				gestorAportaciones.aportar(operadoraPlaneamiento,
						aportadas, 
						++orden,
						contexto);
			} else {
				contexto.logTraducido(LOG.AVISO,"refundido.operacion.entidad.aviso.operadoraSuspendida");
			}
		} else {
			contexto.logTraducido(LOG.AVISO,"refundido.operacion.entidad.aviso.noOperadora");
		}
	}

}

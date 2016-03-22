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
package es.mitc.redes.urbanismoenred.servicios.refundido.operaciones.relaciones;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidad;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Relacion;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Tramite;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Vectorrelacion;
import es.mitc.redes.urbanismoenred.servicios.refundido.ContextoRefundido;
import es.mitc.redes.urbanismoenred.servicios.refundido.ContextoRefundido.LOG;
import es.mitc.redes.urbanismoenred.servicios.refundido.CopiadorRefundidoLocal;
import es.mitc.redes.urbanismoenred.servicios.refundido.ExcepcionRefundido;

/**
 * @author Arnaiz Consultores
 *
 */
@Stateless( name = "refundido.operacion.relaciones.adicion")
public class Adicion implements EjecutorLocal {
	
	private static final int ENTIDAD_DESTINO = 5;

	private static final int ENTIDAD_ORIGEN = 4;

	@PersistenceContext(unitName = "rpmv2")
	private EntityManager em;
	
	@EJB
	private CopiadorRefundidoLocal copiador;
	

	/* (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.servicios.refundido.operaciones.relaciones.EjecutorLocal#ejecutar(int, es.mitc.redes.urbanismoenred.servicios.refundido.ContextoRefundido)
	 */
	@Override
	public void ejecutar(int idrelacion, ContextoRefundido contexto)
			throws ExcepcionRefundido {
		Relacion relacion = em.find(Relacion.class, idrelacion);
		Entidad origen = null;
		Entidad destino = null;
		for (Vectorrelacion vr : relacion.getVectorrelacions()) {
			if (vr.getIddefvector() == ENTIDAD_DESTINO) {
				origen = em.find(Entidad.class, vr.getValor());
			} else if (vr.getIddefvector() == ENTIDAD_ORIGEN) {
				destino = em.find(Entidad.class, vr.getValor());
			}
		}
		
		if (origen != null && destino != null) {
			contexto.logTraducido(LOG.INFO, "refundido.operacion.relacion.adicion.mensaje", origen.getCodigo(), 
					origen.getTramite().getPlan().getCodigo(), 
					destino.getCodigo(), 
					destino.getTramite().getPlan().getCodigo());
			
			copiador.copiar((int)contexto.getParametro(ContextoRefundido.ID_PROCESO), 
					relacion, 
					null, 
					(Tramite)contexto.getParametro(ContextoRefundido.TRAMITE_REFUNDIDO));
		} else {
			contexto.logTraducido(LOG.AVISO, "refundido.operacion.relacion.adicion.aviso");
		}
	}

}

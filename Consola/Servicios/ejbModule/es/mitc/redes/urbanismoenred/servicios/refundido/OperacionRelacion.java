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
package es.mitc.redes.urbanismoenred.servicios.refundido;

import java.util.ResourceBundle;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import es.mitc.redes.urbanismoenred.servicios.refundido.ContextoRefundido.LOG;
import es.mitc.redes.urbanismoenred.servicios.refundido.operaciones.relaciones.EjecutorLocal;

/**
 * @author Arnaiz Consultores
 *
 */
public class OperacionRelacion extends OperacionRefundido {
	
	public static final int ADICION = 3002;
	public static final int ELIMINACION = 3001;

	private int relacion;

	/**
	 * @param tipo
	 */
	public OperacionRelacion(int relacion, int tipo) {
		super(tipo);
		this.relacion = relacion;
	}

	/**
	 * @return the relacion
	 */
	public int getRelacion() {
		return relacion;
	}

	/*
	 * (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.servicios.refundido.OperacionRefundido#ejecutar(es.mitc.redes.urbanismoenred.servicios.refundido.ContextoRefundido)
	 */
	@Override
	public void ejecutar(ContextoRefundido contexto) throws ExcepcionRefundido {
		if (!isCancelada()) {
			InitialContext icontext;
			try {
				icontext = new InitialContext();
				EjecutorLocal ejecutor = (EjecutorLocal)icontext.lookup("java:global/UrbanismoEnRedV2/Urbanismoenred-serviciosV2/" + ejecutores.get(String.valueOf(getTipo())));
				
				ejecutor.ejecutar(getRelacion(), contexto);
			} catch (NamingException ne) {
				ResourceBundle traductor = (ResourceBundle) contexto.getParametro(ContextoRefundido.TRADUCTOR);
				throw new ExcepcionRefundido(String.format(traductor.getString("refundido.operacion.relacion.error.ejecucion"), getTipo()), 5004);
			}
		} else {
			ResourceBundle traductor = (ResourceBundle) contexto.getParametro(ContextoRefundido.TRADUCTOR);
			contexto.logTraducido(LOG.INFO, "refundido.operacion.relacion.cancelada", traductor.getString(String.valueOf(getTipo())));
		}
	}

}

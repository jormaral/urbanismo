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

import org.apache.log4j.Logger;

import es.mitc.redes.urbanismoenred.servicios.refundido.ContextoRefundido.LOG;
import es.mitc.redes.urbanismoenred.servicios.refundido.operaciones.determinaciones.EjecutorLocal;

/**
 * @author Arnaiz Consultores
 *
 */
public class OperacionDeterminaciones extends OperacionRefundido {
	
	private static final Logger log = Logger.getLogger(OperacionDeterminaciones.class);
	
	public final static int ELIMINACION = 1001;
	public final static int ADICION_NORMATIVA = 1004;
	public final static int SUSTITUCION_NORMATIVA_COMPLETA = 1006;
	public final static int SUSPENSION = 1007;
	public final static int ADICION_VALOR_REFERENCIA = 1008;
	public final static int APORTACION = 1009;
	public final static int LEVANTAMIENTO_SUSPENSION = 1011;
	public final static int APORTACION_SOBRE_PADRE = 1012;
	public final static int APORTACION_SOBRE_ANTERIOR = 1013;
	
	private int operada;
	private int operadora;
	
	public OperacionDeterminaciones(int operadora, int operada, int tipo) {
		super(tipo);
		this.operadora = operadora;
		this.operada = operada;
	}

	/**
	 * @return the operada
	 */
	public int getOperada() {
		return operada;
	}

	/**
	 * @return the operadora
	 */
	public int getOperadora() {
		return operadora;
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
				
				ejecutor.ejecutar(operada, operadora, contexto);
			} catch (NamingException ne) {
				ResourceBundle traductor = (ResourceBundle) contexto.getParametro(ContextoRefundido.TRADUCTOR);
				log.warn(String.format(traductor.getString("refundido.operacion.determinacion.error.ejecutor"), getTipo()) + " " + ne.getMessage());
				throw new ExcepcionRefundido(String.format(traductor.getString("refundido.operacion.determinacion.error.ejecutor"), getTipo()), 5001);
			}
		} else {
			ResourceBundle traductor = (ResourceBundle) contexto.getParametro(ContextoRefundido.TRADUCTOR);
			contexto.logTraducido(LOG.INFO, "refundido.operacion.determinacion.cancelada", traductor.getString(String.valueOf(getTipo())));
		}
	}
}

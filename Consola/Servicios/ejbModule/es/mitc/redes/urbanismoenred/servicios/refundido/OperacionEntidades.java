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
import es.mitc.redes.urbanismoenred.servicios.refundido.operaciones.entidades.EjecutorLocal;

/**
 * @author Arnaiz Consultores
 *
 */
public class OperacionEntidades extends OperacionRefundido {
	
	private static final Logger log = Logger.getLogger(OperacionEntidades.class);
	
	public final static int ELIMINACION = 2001;
	public final static int ACUMULACION_COMPLETA = 2002;
	public final static int ADICION_NORMATIVA = 2003;
	public final static int ADICION_GRAFICA = 2004;
	public final static int SUSTRACCION_GRAFICA = 2005;
	public final static int SUSTITUCION_NORMATIVA_COMPLETA = 2006;
	public final static int SUSTITUCION_GRAFICA = 2007;
	public final static int SUSPENSION_TOTAL = 2008;
	public final static int SUSTITUCION_NORMATIVA_PARCIAL = 2009;
	public final static int SUPERPOSICION_COMPLETA = 2010;
	public final static int APORTACION = 2011;
	public final static int ACUMULACION_NORMAS_GENERALES = 2012;
	public final static int ACUMULACION_USOS = 2013;
	public final static int ACUMULACION_ACTOS = 2014;
	public final static int CREACION_GRAFICA = 2015;
	public final static int SUPERPOSICION_NORMAS_GENERALES = 2017;
	public final static int SUPERPOSICION_USOS = 2018;
	public final static int SUPERPOSICION_ACTOS = 2019;
	public final static int DESTRUCCION_GRAFICA = 2020;
	public final static int HERENCIA_CLAVE = 2021;
	public final static int SUSPENSION_PARCIAL = 2022;
	public final static int INCORPORACION = 2025;
	public final static int LEVANTAMIENTO_TOTAL_SUSPENSION = 2026;
	public final static int APORTACION_SOBRE_PADRE = 2027;
	public final static int APORTACION_SOBRE_ANTERIOR = 2028;
	public final static int LEVANTAMIENTO_PARCIAL_SUSPENSION = 2029;
	public final static int INCORPORACION_TRAMITE = 2030;

	private int operada;
	private int operadora;
	
	public OperacionEntidades(int operadora, int operada, int tipo) {
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
				log.warn(String.format(traductor.getString("refundido.operacion.entidad.error.ejecutor"), getTipo()) + " " + ne.getMessage());
				throw new ExcepcionRefundido(String.format(traductor.getString("refundido.operacion.entidad.error.ejecutor"), getTipo()), 5002);
			}
		} else {
			ResourceBundle traductor = (ResourceBundle) contexto.getParametro(ContextoRefundido.TRADUCTOR);
			contexto.logTraducido(LOG.INFO, "refundido.operacion.entidad.cancelada", traductor.getString(String.valueOf(getTipo())));
		}
	}

}

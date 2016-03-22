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

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author Arnaiz Consultores
 *
 */
public abstract class OperacionRefundido {
	 
	protected static Properties ejecutores;
	
	private boolean cancelada = false;
	private boolean ejecutada = false;
	private int tipo;
	
	static {
		ejecutores = new Properties();
		InputStream is = OperacionRefundido.class.getResourceAsStream("/es/mitc/redes/urbanismoenred/servicios/refundido/operaciones/operaciones.properties");
		
		try {
			ejecutores.load(is);
			is.close();
		} catch (IOException e) {
			throw new RuntimeException("Error al cargar fichero de operaciones de refundido.", e);
		}
	}

	public OperacionRefundido(int tipo) {
		this.tipo = tipo;
	}

	/**
	 * @return the tipo
	 */
	public int getTipo() {
		return tipo;
	}

	/**
	 * @return the cancelada
	 */
	public boolean isCancelada() {
		return cancelada;
	}

	/**
	 * @return the ejecutada
	 */
	public boolean isEjecutada() {
		return ejecutada;
	}
	
	/**
	 * @param cancelada the cancelada to set
	 */
	public void setCancelada(boolean cancelada) {
		this.cancelada = cancelada;
	}

	/**
	 * @param ejecutada the ejecutada to set
	 */
	public void setEjecutada(boolean ejecutada) {
		this.ejecutada = ejecutada;
	}
	
	public abstract void ejecutar(ContextoRefundido contexto) throws ExcepcionRefundido;
}

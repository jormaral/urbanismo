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

import javax.ejb.ApplicationException;

import es.mitc.redes.urbanismoenred.utils.excepciones.RedesException;

/**
 * @author Arnaiz Consultores
 *
 */
@ApplicationException(rollback = true)
public class ExcepcionRefundido extends RedesException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8928735985072171066L;
	private int codigo;

	/**
	 * @param string
	 */
	public ExcepcionRefundido(String string, int codigo) {
		super(string);
		this.codigo = codigo;
	}

	/**
	 * @param string
	 * @param causa
	 */
	public ExcepcionRefundido(String string, int codigo, Throwable causa) {
		super(string, causa);
		this.codigo = codigo;
	}

	public int getCodigo() {
		return codigo;
	}
}

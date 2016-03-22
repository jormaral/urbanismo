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
package es.mitc.redes.urbanismoenred.servicios.ficha;

import es.mitc.redes.urbanismoenred.servicios.dal.ExcepcionPersistencia;

/**
 * @author Arnaiz Consultores
 *
 */
public class ExcepcionFicha extends ExcepcionPersistencia {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6534094525223860150L;

	/**
	 * 
	 */
	//private static final long serialVersionUID = 3568880512034939043L;
	

	/**
	 * 
	 */
	public ExcepcionFicha() {
	}

	/**
	 * @param message
	 */
	public ExcepcionFicha(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public ExcepcionFicha(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public ExcepcionFicha(String message, Throwable cause) {
		super(message, cause);
	}

}

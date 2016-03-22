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
package es.mitc.redes.urbanismoenred.servicios.seguridad;

import es.mitc.redes.urbanismoenred.utils.excepciones.RedesException;

/**
 * @author Arnaiz Consultores
 *
 */
public class ExcepcionSeguridad extends RedesException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5716579305754359250L;

	/**
	 * @param string
	 */
	public ExcepcionSeguridad(String string) {
		super(string);
	}

	/**
	 * @param string
	 * @param causa
	 */
	public ExcepcionSeguridad(String string, Throwable causa) {
		super(string, causa);
	}

}

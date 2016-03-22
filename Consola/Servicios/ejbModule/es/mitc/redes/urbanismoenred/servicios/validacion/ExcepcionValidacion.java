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
package es.mitc.redes.urbanismoenred.servicios.validacion;

import javax.ejb.ApplicationException;

import es.mitc.redes.urbanismoenred.utils.excepciones.RedesException;

/**
 * Excepción genérica producida en el proceso de validación.
 * 
 * @author Arnaiz Consultores
 *
 */
@ApplicationException(rollback = true)
public class ExcepcionValidacion extends RedesException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3697260707425321661L;

	/**
	 * @param string
	 */
	public ExcepcionValidacion(String string) {
		super(string);
	}
	
	/**
	 * @param string
	 */
	public ExcepcionValidacion(String string, int proceso, Throwable causa) {
		super(string,causa);
	}
}

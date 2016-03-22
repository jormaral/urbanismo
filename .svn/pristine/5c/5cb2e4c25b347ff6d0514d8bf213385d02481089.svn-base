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
package es.mitc.redes.urbanismoenred.consola.util.helpers;

import java.util.Map;

import javax.ejb.Local;

/**
 * 
 * @author Arnaiz Consultores
 *
 */
@Local
public interface IJsonHelper {
	/**
	 * Devuelve la representación en formato JSON del objeto. Puede o no 
	 * indicarse el identificador del objeto concreto que se desea recuperar.
	 * 
	 * @param id Identificador del objeto devuelto. Si no se desea un objeto 
	 * específico hay que indicar el valor nulo.
	 * @param idioma Idioma en el que se deben mostrar los textos traducibles.
	 * @return Correspondencias entre los campos del objeto y sus valores.
	 */
	public Map<String, Object> marshal(Object id, String idioma);
}

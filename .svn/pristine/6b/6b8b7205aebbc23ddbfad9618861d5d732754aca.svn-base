/*
* Copyright 2011 red.es
* Autores: IDOM Consulting
*
* Licencia con arreglo a la EUPL, Versiï¿½n 1.1 o -en cuanto
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


package com.mitc.redes.editorfip.servicios.basicos.comunes;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;

import javax.ejb.Local;

/**
 * 
 * Servicio basico que ejecuta busquedas filtradas en cualquier tabla, 
 * devolviendo resultados paginados y ordenados.
 * 
 * @author fguerrero
 *
 */
@Local
public interface ServicioBusquedaGenerica {
	
	/**
	 * Realiza la busqueda en todos los campos de una entidad.
	 * 
	 * @param entidad entidad (Tabla) en la cual se quiere buscar. 
	 * @param filtro cadena de texto que contiene el filtro de busqueda
	 * @param pagina numero de pagina de la cual se desean obtener los resultados
	 * @param maxResul numero maximo de resultados por pagina que se desea obtener.
	 * @return lista de objetos que contiene el resultado de la busqueda.
	 * @throws ClassNotFoundException En el caso en el que se envíe el nombre de una clase inexistente.
	 */
	public HashMap buscarEnTodosCampos(String entidad, String filtro, int pagina, int maxResul) throws ClassNotFoundException;
	
	/**
	 * Realiza la busqueda en campos concretos de una entidad.
	 * 
	 * @param entidad entidad (Tabla) en la cual se quiere buscar.
	 * @param filtro cadena de texto que contiene el filtro de busqueda
	 * @param campos campos de la entidad en los cuales se desea buscar
	 * @param pagina numero de pagina de la cual se desean obtener los resultados
	 * @param maxResul numero maximo de resultados por pagina que se desea obtener.
	 * @return lista de objetos que contiene el resultado de la busqueda.
	 * @throws ClassNotFoundException En el caso en el que se envíe el nombre de una clase inexistente.
	 */
	public HashMap buscarEnCamposConcretos(String entidad, String filtro, List<Field> campos, int pagina, int maxResul) throws ClassNotFoundException;
	
}

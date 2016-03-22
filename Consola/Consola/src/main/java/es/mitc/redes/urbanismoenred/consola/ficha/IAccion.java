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
package es.mitc.redes.urbanismoenred.consola.ficha;

import java.io.IOException;

import javax.ejb.Local;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Interfaz para la ejecución de acciones de validación en un entorno Web con 
 * servlets y JSP.
 * 
 * @author Javi
 *
 */
@Local
public interface IAccion {
	/**
	 * Ejecuta una acción de configuración de ficha dentro del contexto de ejecución de un 
	 * servlet o JSP.
	 * 
	 * @param request Parámetro request de un servlet.
	 * @param response Parámetro response de un servlet.
	 * @throws IOException
	 */
	void ejecutar(HttpServletRequest request, HttpServletResponse response) throws IOException;
}

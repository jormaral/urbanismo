/**
 * 
 */
package es.mitc.redes.urbanismoenred.consola.visor;

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
	 * Ejecuta una acción de previsor dentro del contexto de ejecución de un 
	 * servlet o JSP.
	 * 
	 * @param request Parámetro request de un servlet.
	 * @param response Parámetro response de un servlet.
	 * @throws IOException
	 */
	void ejecutar(HttpServletRequest request, HttpServletResponse response) throws IOException;
}

/*
* Copyright 2011 red.es
* Autores: Arnaiz Consultores.
*
** Licencia con arreglo a la EUPL, Versión 1.1 o -en cuanto
** sean aprobadas por la Comision Europea- versiones
** posteriores de la EUPL (la <<Licencia>>);
** Solo podra usarse esta obra si se respeta la Licencia.
* Puede obtenerse una copia de la Licencia en:
*
* http://ec.europa.eu/idabc/eupl5
*
** Salvo cuando lo exija la legislacion aplicable o se acuerde
* por escrito, el programa distribuido con arreglo a la
* Licencia se distribuye <<TAL CUAL>>,
** SIN GARANTIAS NI CONDICIONES DE NINGUN TIPO, ni expresas
** ni implicitas.
** Vease la Licencia en el idioma concreto que rige
* los permisos y limitaciones que establece la Licencia.
*/
package es.mitc.redes.urbanismoenred.servicios.dal;

/**
 * Interfaz que permite recibir eventos generados desde los gestores 
 * encargados de almacenar y recuperar los FIP en base de datos.
 * 
 * @author Arnaiz Consultores
 *
 */
public interface GestorFipListener {
	
	static final int PASOS_BORRADO = 7;
	static final int PASOS_GUARDADO = 9;
	
	/**
	 * Método invocado cada vez que se actualiza el progreso de guardado o 
	 * borrado de FIPs.
	 * El progreso se divide en pasos y acciones. Cada acción tiene un 
	 * número de pasos y en cada paso se ejecutan un número de acciones.
	 * 
	 * @param paso Número de paso actual
	 * @param total número de pasos a ejecutar
	 * @param accion Número de acción actual
	 * @param totalAcciones Número de acciones a ejecutar en este paso.
	 */
	public void estadoActualizado(int paso, int total, int accion, int totalAcciones);
}

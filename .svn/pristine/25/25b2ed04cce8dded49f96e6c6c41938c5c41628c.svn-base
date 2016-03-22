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
package es.mitc.redes.urbanismoenred.servicios.consolidacion;

import javax.ejb.Local;

import es.mitc.redes.urbanismoenred.servicios.seguridad.Usuario;

/**
 * Interfaz que define la funcionalidad del servicio de consolidación de 
 * trámites.
 * 
 * @author Arnaiz Consultores
 *
 */
@Local
public interface ServicioConsolidacionLocal {
	
	/**
	 * Consolida un proceso de validación dado. Para realizar el proceso
	 * de consolidación es necesario proporcionar un usuario válido.
	 * 
	 * @param usuario Usuario que realiza el proceso de consolidación.
	 * @param proceso Identificador del proceso de validación correspondiente
	 * al trámite a validar.
	 * @throws ExcepcionConsolidacion
	 */
	void consolidar(Usuario usuario, int proceso) throws ExcepcionConsolidacion;
	
	/**
	 * Devuelve todos los procesos consolidables por un usuario.
	 * 
	 * @return Usuario que realizará el proceso de consolidación.
	 */
	ProcesoConsolidable[] getProcesosConsolidables(Usuario usuario);
}

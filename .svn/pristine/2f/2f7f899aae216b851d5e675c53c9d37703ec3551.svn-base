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

import javax.ejb.Local;

/**
 * Interfaz que define la funcionalidad proporcionada por el servicio de
 * validación de integridad.
 * 
 * 
 * @author Arnaiz Consultores
 *
 */
@Local
public interface ServicioValidacionIntegridadLocal {
	
	/**
	 * Valida la integridad de un FIP. 
	 * La validación de integridad consiste en los siguientes pasos:
	 * 1- Comprobar que el trámite del FIP ya se encuentra en base de datos.
	 * 2- Comprobar que el usuario que solicita la validación tiene permisos.
	 * 3- Comprobar si ya existe enbase de datos el FIP a validar
	 * 4- Comprobar que el FIP no haya caducado.
	 * 5- Comprobar que los documentos incluidos en el FIP se encuentran en el sistema
	 * 
	 * @param fip Objeto FIP a validar
	 * @param nombreUsuario Usuario que solicita la validación
	 * @return Verdadero si la validación fue correcta, falso en caso de que se encontraran errores.
	 * @throws ExcepcionValidacion
	 */
	public boolean validar(String fip, String nombreUsuario) throws ExcepcionValidacion;
}

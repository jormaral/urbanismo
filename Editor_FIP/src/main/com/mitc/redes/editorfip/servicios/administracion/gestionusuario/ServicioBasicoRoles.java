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

package com.mitc.redes.editorfip.servicios.administracion.gestionusuario;

import java.util.List;

import javax.ejb.Local;

import com.mitc.redes.editorfip.entidades.rpm.administracionusuarios.Rol;

/**
 * Este servicio se encarga de las operaciones referentes a los roles de usuario.
 * 
 * @author fguerrero
 *
 */
@Local
public interface ServicioBasicoRoles {
	
	/**
	 * Método que devuelve la lista de todos los roles existentes en el sistema.
	 * @return lista de objetos de tipo Rol.
	 */
	public List<Rol> obtenerRoles();
	
	/**
	 * Método que devuelve un valor en formato boolean el cual indica si ya se ha guardado 
	 * en base de datos un DNI concreto
	 * 
	 * @param dni DNI a buscar en formato String.
	 * @return valor que indica si existe o no el DNI.
	 */
	public boolean existeDniUsuario(String dni);
	
	/**
	 * Método que devuelve un valor en formato boolean el cual indica si ya se ha guardado 
	 * en base de datos un DNI concreto en base al identificador de un usuario.
	 * 
	 * @param dni DNI a buscar en formato String.
	 * @param username Identificador del usuario al cual pertenece dicho DNI.
	 * @return valor que indica si existe o no el DNI.
	 */
	public boolean existeDniUsuario(String dni, String username);
}

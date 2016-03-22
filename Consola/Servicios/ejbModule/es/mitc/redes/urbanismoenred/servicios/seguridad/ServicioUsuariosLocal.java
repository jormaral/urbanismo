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

import javax.ejb.Local;

import es.mitc.redes.urbanismoenred.servicios.dal.ExcepcionPersistencia;

/**
 * Interfaz que define la funcionalidad ofrecida por el servicio de usuarios.
 * 
 * @author Arnaiz Consultores
 *
 */
@Local
public interface ServicioUsuariosLocal {
	
	/**
	 * Elimina un usuario de base de datos.
	 * @param identificador Identificador del usuario.
	 * @throws ExcepcionPersistencia
	 */
	void borrarUsuario(int identificador) throws ExcepcionPersistencia;
	
	/**
	 * Elimina un usuario de base de datos.
	 * 
	 * @param nombre Nombre del usuario.
	 * @throws ExcepcionPersistencia
	 */
	void borrarUsuario(String nombre) throws ExcepcionPersistencia;
	
	/**
	 * Indica si ya existe un usuario en base de datos con ese nombre.
	 * 
	 * @param nombre Nombre de usuario
	 * @return Verdadero si existe un usuario con ese nombre y falso en caso contrario.
	 */
	boolean existeNombre(String nombre);
	
	/**
	 * Devuelve la lista de los ámbitos existentes en el sistema que 
	 * coincidan con el nombre especificado. 
	 * 
	 * @param nombre Nombre del ámbito traducido.
	 * @param idioma Idioma en el que se encuentra escrito el ámbito
	 * @return Lista de ambitos. Si no hay coincidencias devuelve una lista 
	 * vacía.
	 */
	Ambito[] getAmbitos(String nombre, String idioma);
	
	/**
	 * Devuelve todos los roles dados de alta en base de datos.
	 * @return
	 */
	Rol[] getRoles();
	
	/**
	 * Devuelve el usuario cuyo identificador se corresponde con el pasado como 
	 * parámetro.
	 * 
	 * @param identificador Identificador de usuario
	 * @return Datos del usuario o valor nulo si no hay usuario con ese nombre.
	 */
	Usuario getUsuario(int identificador);
	
	/**
	 * Devuelve el usuario cuyo nombre se corresponde con el pasado como 
	 * parámetro.
	 * 
	 * @param nombre Nombre de usuario
	 * @return Datos del usuario o valor nulo si no hay usuario con ese nombre.
	 */
	Usuario getUsuario(String nombre);
	
	/**
	 * Devuelve el usuario cuyo DN se corresponde con el pasado como parámetro.
	 * 
	 * @param dni DNI del usuario.
	 * @return Datos del usuario o valor nulo si no hay usuario con ese DNI.
	 */
	Usuario getUsuarioPorDNI(String dni);
	
	/**
	 * Devuelve todos los usuarios dados de alta en el sistema.
	 * 
	 * @return Lista de usuarios del sistema.
	 */
	Usuario[] getUsuarios();
	
	/**
	 * Guarda los datos de un usuario. Si ya existe un usuario con ese nombre
	 * lo modifica, si no, lo corea.
	 * 
	 * @param usuario Datos de usuario a guardar.
	 * @throws ExcepcionPersistencia
	 */
	void guardarUsuario(Usuario usuario) throws ExcepcionPersistencia;
}

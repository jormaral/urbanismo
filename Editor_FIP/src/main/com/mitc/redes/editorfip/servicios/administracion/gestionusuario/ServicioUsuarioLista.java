/*
* Copyright 2011 red.es
* Autores: IDOM Consulting
*
* Licencia con arreglo a la EUPL, Versi�n 1.1 o -en cuanto
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

import java.util.HashMap;
import java.util.List;

import javax.ejb.Local;
import javax.ejb.Remove;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import com.mitc.redes.editorfip.entidades.rpm.administracionusuarios.Usuario;
import com.mitc.redes.editorfip.servicios.basicos.comunes.plantillas.ListaGenerica;

/**
 * Servicio que se encarga de las operaciones referentes a la lista de usuarios. 
 * 
 * @author fguerrero
 *
 */
@Local
public interface ServicioUsuarioLista extends ListaGenerica {
	
	/**
	 * Obtiene la lista completa de todos los usuarios del sistema.
	 * 
	 * @return lista de objetos de tipo Usuario.
	 */
	public List<Usuario> getListaUsuarios();
	
	
	/**
	 * 
	 */
	public void darDealta();
	
	/**
	 * 
	 * Metodo que da de baja los usuarios seleccionados. En caso de
	 * que no se haya seleccionado ningun usuario, se muestra un 
	 * mensaje de error en la pantalla.
	 * 
	 */
	public void darDebaja() ;
	
	/**
	 * Metodo que carga los datos del usuario seleccionado y redirige
	 * a la pagina de detalles de usuario.
	 * 
	 * @return cadena que se utiliza en el proceso de redireccion.
	 */
	public String verDetalle();
	
	/**
	 * Metodo(Action Listener) que carga los datos del usuario seleccionado y redirige
	 * a la pagina de detalles de usuario.
	 * 
	 * @param ae
	 * @return cadena que se utiliza en el proceso de redireccion.
	 */
	public String verDetalleAL(ActionEvent ae);
	
	/**
	 * Metodo utilizado en el campo de texto de busqueda que carga los datos 
	 * del usuario seleccionado y redirige a la pagina de detalles de usuario.
	 * 
	 * @return cadena que se utiliza en el proceso de redireccion.
	 */
	public String verDetalleBuscar();
	
	/**
	 * 
	 * Metodo que obtiene un HashMap en el cual se encuentran todos los 
	 * identificadores de usuario asociados a una propiedad de tipo boolean
	 * que indica si dicho usuario se encuentra seleccionado.
	 * 
	 * @return HashMap<[Identificador], [Seleccionado?]>
	 */
	public HashMap getSeleccionUsuarios();
	
	/**
	 * Modifica los datos de la lista HashMap que indica aquellos usuarios que
	 * se encuentran seleccionados.
	 * 
	 * @param seleccionUsuarios
	 */
	public void setSeleccionUsuarios(HashMap seleccionUsuarios);
	
	/**
	 * Obtiene el identificador del usuario seleccionado
	 * 
	 * @return Identificador en formato String
	 */
	public String getUsernameUsuarioSel();
	
	/**
	 * Modifica la propiedad que indica el usuario actualmente seleccionado
	 * 
	 * @param usernameUsuarioSel Identificador en formato String
	 */
	public void setUsernameUsuarioSel(String usernameUsuarioSel);
	
	/**
	 * Obtiene una lista de objetos tipo SelectItem los cuales son el 
	 * resultado de la busqueda realizada en base a un filtro.
	 * 
	 * @return
	 */
	public List<SelectItem> getResultadosBusqueda();
	
	/**
	 * Modifica el resultado de la busqueda de usuarios.
	 * 
	 * @param resultadosBusqueda
	 */
	public void setResultadosBusqueda(List<SelectItem> resultadosBusqueda);
	
	/**
	 * Metodo (evento) que se lanza para actualizar la lista actual de usuarios 
	 * y refrescar los cambios en la pantalla.
	 * 
	 * @param event
	 */
	public void actualizarListaUsuarios(ValueChangeEvent event);
	
	/**
	 * Modifica la propiedad que indica el usuario actualmente seleccionado
	 * 
	 * @param usuarioSel Identificador en formato String
	 */
	public void setUsuarioSel(String usuarioSel);
	
	/**
	 * Obtiene el identificador del usuario seleccionado
	 * 
	 * @return Identificador en formato String
	 */
	public String getUsuarioSel();
	
	@Remove
	public void remove();
	

}

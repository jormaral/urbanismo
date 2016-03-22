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
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import com.mitc.redes.editorfip.entidades.comunes.ParIdentificadorTexto;
import com.mitc.redes.editorfip.entidades.rpm.administracionusuarios.Rol;
 /**
  * 
  * 
  * @author fguerrero
  *
  */
@Local
public interface ServicioUsuario {
	
	/** 
	 * M�todo que carga los datos de un usuario en base al identificador 
	 * del mismo.
	 * 
	 * @param username Identificador del usuario a cargar.
	 */
	public void cargarUsuario(String username);
	
	/**
	 * 
	 * Metodo que guarda los datos de un usuario en base de datos. Este metodo 
	 * realiza las validaciones pertinentes y verifica si el usuario esta o no 
	 * dado de alta en el sistema para as� modificalo o crearlo.
	 * 
	 * @return "success" si se ha hecho de forma correcta o "" si ha ocurrido un
	 * error en la operaci�n. Este parametro de salida se utlizar� para realizar 
	 * las tareas de redirecci�n de la pagina.
	 * 
	 */
	public String guardarUsuario();
	
	/**
	 * M�todo que se encarga de eliminar y borrar los datos de un usuario.
	 */
	public void eliminarUsuario();
	
	/**
	 * M�todo que recarga la pagina con sus datos iniciales, descartando 
	 * cualquier cambio realizado despues de haber inicializado el usuario
	 * a modificar.
	 */
	public void descartarCambios();
	
	/**
	 * M�todo que selecciona y a�ade un �mbito a la lista actual de �mbitos 
	 * del usuario.
	 *  
	 * @param idAmbitoSel Id del ambito seleccionado.
	 */
	public void seleccionarAmbito(Integer idAmbitoSel);	
	
	/**
	 * Este m�todo elimina un ambito de la lista de ambitos de usuario. Los 
	 * cambios realizados en la lista no se reflejan en base de datos hasta que 
	 * el usuario pulse el boton de "Guardar".
	 * 
	 * @param idAmbito Id del ambito seleccionado.
	 */
	public void eliminarAmbito(Integer idAmbito);
	
	/**
	 * Metodo que devuelve el valos de la propiedad que contiene la lista de 
	 * ambitos actualmente asginados al usuario.
	 * 
	 * @return Lista de ambitos en formato ParIdentificadorTexto
	 */
	public List<ParIdentificadorTexto> getAmbitosUsuario();
	
	/**
	 * Metodo que modifica los ambitos asignados del usuario.
	 * @param ambitosUsuario Lista de ambitos en formato ParIdentificadorTexto
	 */
	public void setAmbitosUsuario(List<ParIdentificadorTexto> ambitosUsuario);	
	
	/**
	 * Metodo que indica si el usuario cargado es nuevo o no.
	 * 
	 * @return true si se trata de un usuario nuevo, false si se trata de 
	 * us usuario existente en BD.
	 */
	public boolean isEsUsuarioNuevo();
	
	/**
	 * M�todo que permite mostrar el panel de seleccion de �mbitos.
	 * 
	 * @return true si se renderiza, false en caso contrario.
	 */
	public boolean isMostrarPanelAmbitos();
	
	/**
	 * M�todo que modifica el valor de la propiedad que renderiza el 
	 * panel de seleccion de ambitos.
	 * 
	 * @param mostrarPanelAmbitos
	 */
	public void setMostrarPanelAmbitos(boolean mostrarPanelAmbitos);
	
	/**
	 * M�todo que permite mostrar el panel de seleccion de tramites.
	 * 
	 * @return true si se renderiza, false en caso contrario.
	 */
	public boolean isMostrarPanelTramites();
	
	/**
	 * M�todo que modifica el valor de la propiedad que renderiza el 
	 * panel de seleccion de ambitos.
	 * 
	 * @param mostrarPanelTramites
	 */
	public void setMostrarPanelTramites(boolean mostrarPanelTramites); 
	
	/**
	 * M�todo (evento) que lanza la ventana de pop-up de seleccion de �mbitos
	 * 
	 * @param actionEvent parametro obligatorio para lanzar el evento.
	 */
	public void mostrarPanelAmbitosAL(ActionEvent actionEvent);
	
	/**
	 * M�todo (evento) que lanza la ventana de pop-up de seleccion de tramites
	 * 
	 * @param actionEvent parametro obligatorio para lanzar el evento.
	 */
	public void mostrarPanelTramitesAL(ActionEvent actionEvent);
	
	/**
	 * M�todo que devuelve el Id del ambito seleccionado para ser a�adido.
	 * @return Id del ambito seleccionado.
	 */
	public Integer getIdAmbSel();
	
	/**
	 * M�todo que modifica el id del ambito seleccionado para ser a�adido.
	 * 
	 * @param idAmbSel Id del ambito seleccionado.
	 */
	public void setIdAmbSel(Integer idAmbSel);
	
	/**
	 * M�todo que devuelve el Id del tramite seleccionado para ser a�adido.
	 * @return Id del tramite seleccionado.
	 */
	public Integer getIdTramiteSel();
	
	/**
	 * M�todo que modifica el id del tramite seleccionado para ser a�adido.
	 * 
	 * @param idAmbSel Id del tramite seleccionado.
	 */
	public void setIdTramiteSel(Integer idTramiteSel);
	
	/**
	 * Metodo que a�ade multiples tramites a la lista de tramites asignados 
	 * al usuario.
	 * 
	 * @param idsTramites Id's de los tramites a a�adir.
	 */
	public void seleccionarTramite(List<Integer> idsTramites);
	
	/**
	 * Elimina un tramite de la lista de tramites asignados.Los 
	 * cambios realizados en la lista no se reflejan en base de datos hasta que 
	 * el usuario pulse el boton de "Guardar".
	 * 
	 * @param idTramite Id del tramite a eliminar.
	 */
	public void eliminarTramite(Integer idTramite);
	
	/**
	 * Obtiene el nombre de un �mbito en base a du ID.
	 * 
	 * @param idAmbito Id del �mbito del cual se quiere obtener el 
	 * nombre.
	 * 
	 * @return nombre del ambito en formato String.
	 */
	public String obtenerNombreAmbito(Integer idAmbito);
	
	/**
	 * Obtiene la lista de todos los roles existente en el sistema.
	 * 
	 * @return Lista de roles en formato SelectItem.
	 */
	public List<SelectItem> getListaRoles();
	
	/**
	 * Modifica la lista actual de roles. No refleja los datos en BD.
	 * 
	 * @param listaRoles lista de roles en formato SelectItem
	 */
	public void setListaRoles(List<SelectItem> listaRoles);
	
	/**
	 * Obtiene el codigo del actual rol seleccionado
	 * 
	 * @return codigo en formato String
	 */
	public String getCodigoRolSeleccionado();
	
	/**
	 * 
	 * Modifica el codigo del actual rol seleccionado.
	 * 
	 * @param codigoRolSeleccionado codigo del rol en formato String.
	 */
	public void setCodigoRolSeleccionado(String codigoRolSeleccionado);
	
	/**
	 * Metodo que indica si el usuario a cargado posee el rol de Administrador.
	 * 
	 * @return true si posee el rol, false en caso contrario.
	 */
	public boolean esAdministrador();
	
	/**
	 * Obtiene el valor del campo de la contrase�a.
	 * 
	 * @return contrase�a en formato String
	 */
	public String getNuevaPassword();
	
	/**
	 * Modifica el valor del campo de la contrase�a.
	 * 
	 * @param nuevaPassword nuevo valor de la contrase�a
	 */
	public void setNuevaPassword(String nuevaPassword);
	
	/**
	 * Obtiene el valor del campo de confirmacion de la contrase�a.
	 * 
	 * @return contrase�a en formato String
	 */
	public String getConfirmPassword();
	
	/**
	 * Modifica el valor del campo de confirmacion de la contrase�a.
	 * 
	 * @param nuevaPassword nuevo valor de la contrase�a
	 */
	public void setConfirmPassword(String confirmPassword);
	
	/**
	 * Metodo 'Validator' que indica si se ha introducido una direccion de 
	 * correo electronico correcta: [nombre]@[dominio].[extension]
	 * 
	 * @param context
	 * @param component
	 * @param value
	 */
	public void validatorEmail(FacesContext context, UIComponent component, Object value);
	
	/**
	 * Metodo 'Validator' que indica si se ha introducido un identificador de usuario que 
	 * ya no se este utilizando en el sistema, en el caso en que el usuario sea nuevo.
	 * 
	 * @param context
	 * @param component
	 * @param value
	 */
	public void validatorUsername(FacesContext context, UIComponent component, Object value);
	
	// TODO: TEMPORAL!!!
	public List<SelectItem> getAmbitosPadres();
	public void setAmbitosPadres(List<SelectItem> ambitosPadres);
	
	public List<SelectItem> getPlanesPadres();
	public void setPlanesPadres(List<SelectItem> planesPadres);
	
	/* CODIGO TEMPORAL PARA EL TREE 
	public void contraerExpadir(NodeExpandedEvent event);
	public List<ParIdentificadorTexto> obtenerAmbitosHijos(Integer idPadre);
	*/
	
	
	
	
	
	
	@Remove
	public void remove();

}

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

import javax.ejb.Local;
import javax.ejb.Remove;
import javax.faces.event.ActionEvent;
import javax.swing.tree.DefaultTreeModel;

import com.mitc.redes.editorfip.servicios.genericos.GestionArbolGenericoIF;

/**
 * Este servicio se encarga de la gestión del arbol de selección del plan encargado.
 * 
 * @author fguerrero
 *
 */
@Local
public interface SeleccionArbolPlanEncargado extends GestionArbolGenericoIF
{
	public void menuEvent(ActionEvent event);

	public void nodeSelected(ActionEvent event);

	public void cargarArbolInicial();
	
	/**
	 * Método que devuelve los datos del árbol.
	 * @return modelo de datos en el que se encuentran los nodos padres y sus respectivos hijos.
	 */
	public DefaultTreeModel getModel();
	
	/**
	 * Método que modifica los datos del árbol.
	 * @param model modelo de datos en el que se encuentran los nodos padres y sus respectivos hijos.
	 */
	public void setModel(DefaultTreeModel model);
	
	
	/**
	 * Método (evento) que lanza el pop-up de seleccion.
	 * @param actionEvent parametro obligatorio para lanzar el evento.
	 */
	public void mostrarPanelPopupSelector(ActionEvent actionEvent);
	
	/**
	 * Método (evento) que cierra el pop-up de seleccion.
	 * @param actionEvent parametro obligatorio para lanzar el evento.
	 */
	public void cerrarPanelPopupSelector(ActionEvent actionEvent);
	
	/**
	 * Método que devuelve el valor que decide si se muestra o no el panel de pop-up.
	 * @return valor de renderizado(true o false).
	 */
	public boolean isMostrarPanelPopup();
	
	/**
	 * Método que modifica el valor de la propiedad que permite renderizar(visualizar) el pop-up de seleccion.
	 * @param mostrarPanelPopup nuevo valor de la propiedad en formato boolean.
	 */
	public void setMostrarPanelPopup(boolean mostrarPanelPopup);

	@Remove
	public void destroy();

}

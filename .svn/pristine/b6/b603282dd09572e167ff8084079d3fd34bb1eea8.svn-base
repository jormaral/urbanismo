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
import javax.ejb.Remove;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import com.mitc.redes.editorfip.servicios.genericos.ArbolGenericoObject;
import com.mitc.redes.editorfip.servicios.genericos.GestionArbolGenericoIF;

/**
 * Este servicio se encarga de la gestión del arbol de asignacion de tramites al usuario.
 * 
 * @author fguerrero
 *
 */
@Local
public interface GestionArbolAsignacionTramite extends GestionArbolGenericoIF
{
	/**
	 * Método (evento) que se lanza cuando el árbol es activado por el usuario.
	 * @param event parametro obligatorio para lanzar el evento.
	 */
	public void menuEvent(ActionEvent event);

	/**
	 * Método (evento) que se lanza cuando el usuario selecciona un nodo del árbol.
	 * @param event parametro obligatorio para lanzar el evento.
	 */
	public void nodeSelected(ActionEvent event);

	/**
	 * Método que carga los datos iniciales del arbol.
	 */
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

	@Remove
	public void destroy();
	
	/**
	 * Método (evento) que selecciona un nodo hijo.
	 * @param e parametro obligatorio para lanzar el evento.
	 */
	public void processValueChange(ValueChangeEvent e);
	
	/**
	 * Método (evento) que selecciona un nodo padre y todos sus respectivos hijos.
	 * @param e parametro obligatorio para lanzar el evento.
	 */
	public void processMultiValueChange(ActionEvent e);
	
	/**
	 * Método que marca o desmarca un nodo padre y todos sus respectivos hijos en base a un nodo.
	 * @param nodoArbol datos del arbol en el cual se van a desplegar los nodos.
	 * @param seleccionado valor que indica si se var a marcar(true) o desmarcar(false) los nodos.
	 */
	public void marcarHijos(DefaultMutableTreeNode nodoArbol, boolean seleccionado);
	
	/**
	 * Método que devuelve una lista con los id's de los tramites seleccionados en el arbol.
	 * @return lista con los id's en formato Integer.
	 */
	public List<Integer> obtenerIdsTramitesSeleccionados();
	
	/**
	 * Método que devuelve una lista con los tramites seleccionados en el arbol.
	 * @return lista con los trámites en formato ArbolGenericoObject.
	 */
	public List<ArbolGenericoObject> getSeleccionados();

	/**
	 * Método que modifica los datos de la lista de trámites seleccionados.
	 * @param seleccionados lista con los trámites seleccionados en formato ArbolGenericoObject.
	 */
	public void setSeleccionados(List<ArbolGenericoObject> seleccionados);
	
	/**
	 * Método que indica si se puede o no renderizar(visualizar) el boton de seleccion de tramite.
	 * @return verdadero o falso en el caso en que se haya seleccionado un nodo.
	 */
	public boolean renderizarBtnSelTramite();
}

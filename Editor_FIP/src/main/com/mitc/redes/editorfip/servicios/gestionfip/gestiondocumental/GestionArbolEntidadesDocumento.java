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

package com.mitc.redes.editorfip.servicios.gestionfip.gestiondocumental;

import javax.ejb.Local;
import javax.ejb.Remove;
import javax.faces.event.ActionEvent;
import javax.swing.tree.DefaultTreeModel;

import com.mitc.redes.editorfip.servicios.genericos.GestionArbolGenericoIF;

/**
 * Este servicio se encarga de la gestión del arbol de asignacion de entidades al documento.
 * 
 * @author fguerrero
 *
 */
@Local
public interface GestionArbolEntidadesDocumento extends GestionArbolGenericoIF
{
	/**
	 * Método (evento) que guarda los datos de la entidad base.
	 * @param event parametro obligatorio para lanzar el evento.
	 */
	public void guardarEntidadBase (ActionEvent event);
	
	/**
	 * Método (evento) que actualiza los datos del arbol.
	 * @param event parametro obligatorio para lanzar el evento.
	 */
	public void refrescar(ActionEvent e);
	
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
	

	@Remove
	public void destroy();

}

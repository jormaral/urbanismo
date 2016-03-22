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

package com.mitc.redes.editorfip.servicios.genericos;

import java.util.ArrayList;

import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;

import com.icesoft.faces.context.effects.Effect;

/**
 * Este es el servicio padre en el cual se encuentran todos los metodos que 
 * hacen falta a la hora de gestionar cualquier tipo de arbol jerárquico de seleccion.
 * 
 * @author fguerrero
 *
 */
public interface GestionArbolGenericoIF {

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
	 * Método (evento) que se controla el efecto gráfico que se produce al seleccionar un nodo.
	 * @param event parametro obligatorio para lanzar el evento.
	 */
	public void effectChangeListener(ValueChangeEvent event);
	
	/**
	 * Método que devuelve los datos del efecto gráfico de selección.
	 * @return efecto gráfico en formato Effect.
	 */
	public Effect getValueChangeEffect();

	/**
	 * Método que modifica los datos del efecto gráfico de seleccion.
	 * @param valueChangeEffect efecto gráfico en formato Effect.
	 */	
	public void setValueChangeEffect(Effect valueChangeEffect);

	/**
	 * Método que devuelve los datos del árbol jerárquico.
	 * @return datos del arbol en formato com.icesoft.faces.component.tree.Tree
	 */	
	public com.icesoft.faces.component.tree.Tree getTreeComponent();
	
	/**
	 * Método que modifica los datos del del árbol jerárquico.
	 * @param treeComponent datos del arbol en formato com.icesoft.faces.component.tree.Tree
	 */	
	public void setTreeComponent(
			com.icesoft.faces.component.tree.Tree treeComponent);

	/**
	 * Método que devuelve el objeto seleccionado.
	 * @return datos del objeto seleccionado en formato ArbolGenericoObject
	 */	
	public ArbolGenericoObject getSelectedUserObject();
	
	/**
	 * Método que devuelve una lista de objetos referentes a la ruta jerárquica del objeto seleccionado.
	 * @return lista de objetos en orden jerárquico tomando como nodo padre el nodo seleccionado.
	 */
	public ArrayList getSelectedTreePath();

}
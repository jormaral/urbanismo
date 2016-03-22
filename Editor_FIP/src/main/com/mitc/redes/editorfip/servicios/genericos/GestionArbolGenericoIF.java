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

package com.mitc.redes.editorfip.servicios.genericos;

import java.util.ArrayList;

import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;

import com.icesoft.faces.context.effects.Effect;

/**
 * Este es el servicio padre en el cual se encuentran todos los metodos que 
 * hacen falta a la hora de gestionar cualquier tipo de arbol jer�rquico de seleccion.
 * 
 * @author fguerrero
 *
 */
public interface GestionArbolGenericoIF {

	/**
	 * M�todo (evento) que se lanza cuando el �rbol es activado por el usuario.
	 * @param event parametro obligatorio para lanzar el evento.
	 */
	public void menuEvent(ActionEvent event);
	
	/**
	 * M�todo (evento) que se lanza cuando el usuario selecciona un nodo del �rbol.
	 * @param event parametro obligatorio para lanzar el evento.
	 */
	public void nodeSelected(ActionEvent event);
	
	/**
	 * M�todo que carga los datos iniciales del arbol.
	 */
	public void cargarArbolInicial();

	/**
	 * M�todo (evento) que se controla el efecto gr�fico que se produce al seleccionar un nodo.
	 * @param event parametro obligatorio para lanzar el evento.
	 */
	public void effectChangeListener(ValueChangeEvent event);
	
	/**
	 * M�todo que devuelve los datos del efecto gr�fico de selecci�n.
	 * @return efecto gr�fico en formato Effect.
	 */
	public Effect getValueChangeEffect();

	/**
	 * M�todo que modifica los datos del efecto gr�fico de seleccion.
	 * @param valueChangeEffect efecto gr�fico en formato Effect.
	 */	
	public void setValueChangeEffect(Effect valueChangeEffect);

	/**
	 * M�todo que devuelve los datos del �rbol jer�rquico.
	 * @return datos del arbol en formato com.icesoft.faces.component.tree.Tree
	 */	
	public com.icesoft.faces.component.tree.Tree getTreeComponent();
	
	/**
	 * M�todo que modifica los datos del del �rbol jer�rquico.
	 * @param treeComponent datos del arbol en formato com.icesoft.faces.component.tree.Tree
	 */	
	public void setTreeComponent(
			com.icesoft.faces.component.tree.Tree treeComponent);

	/**
	 * M�todo que devuelve el objeto seleccionado.
	 * @return datos del objeto seleccionado en formato ArbolGenericoObject
	 */	
	public ArbolGenericoObject getSelectedUserObject();
	
	/**
	 * M�todo que devuelve una lista de objetos referentes a la ruta jer�rquica del objeto seleccionado.
	 * @return lista de objetos en orden jer�rquico tomando como nodo padre el nodo seleccionado.
	 */
	public ArrayList getSelectedTreePath();

}
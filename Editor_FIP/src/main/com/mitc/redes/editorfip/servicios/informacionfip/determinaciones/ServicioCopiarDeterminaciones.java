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


package com.mitc.redes.editorfip.servicios.informacionfip.determinaciones;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Remove;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import com.mitc.redes.editorfip.servicios.genericos.ArbolGenericoObject;
import com.mitc.redes.editorfip.servicios.genericos.GestionArbolGenericoIF;

@Local
public interface ServicioCopiarDeterminaciones extends GestionArbolGenericoIF
{
	public void menuEvent(ActionEvent event);

	public void nodeSelected(ActionEvent event);

	public void cargarArbolInicial();
	
	/**
	 * Obtiene el valor de la propiedad ''
	 * 
	 * @return valor de tipo DefaultTreeModel
	 */
	public DefaultTreeModel getModel();

	/**
	 * Modifica el valor de la propiedad '@param model'
	 * 
	 * @param model valor de tipo 
	 */
	public void setModel(DefaultTreeModel model);

	@Remove
	public void destroy();
	
	public void processValueChange(ValueChangeEvent e);
	
	public void processMultiValueChange(ActionEvent e);
	
	public void marcarHijos(DefaultMutableTreeNode nodoArbol, boolean seleccionado);
	
	public List<Integer> obtenerIdsTramitesSeleccionados();
	
	/**
	 * Obtiene el valor de la propiedad ''
	 * 
	 * @return valor de tipo List<ArbolGenericoObject>
	 */
	public List<ArbolGenericoObject> getSeleccionados();

	/**
	 * Modifica el valor de la propiedad '@param seleccionados'
	 * 
	 * @param seleccionados valor de tipo 
	 */
	public void setSeleccionados(List<ArbolGenericoObject> seleccionados);
	
	public boolean renderizarBtnSelTramite();
	
	public void copiarSeleccionados(int idEntidad);
}

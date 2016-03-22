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


package com.mitc.redes.editorfip.servicios.produccionfip.gestionentidades;

import java.util.List;

import javax.ejb.Local;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import com.mitc.redes.editorfip.entidades.interfazgrafico.CondicionUrbanisticaSimplificadaDTO;
import com.mitc.redes.editorfip.servicios.genericos.ArbolGenericoObject;
import com.mitc.redes.editorfip.servicios.genericos.GestionArbolGenericoIF;

@Local
public interface GestionArbolCopiarCU extends GestionArbolGenericoIF
{
	
	public void copiarCUsSeleccionadasEnNuevaEntidad ();
	
	public void menuEvent(ActionEvent event);

	public void nodeSelected(ActionEvent event);

	public void cargarArbolInicial();
	
	public DefaultTreeModel getModel();

	public void setModel(DefaultTreeModel model);

	
	public void destroy();
	
	public void processValueChange(ValueChangeEvent e);
	
	public void processMultiValueChange(ActionEvent e);
	
	public void marcarHijos(DefaultMutableTreeNode nodoArbol, boolean seleccionado);
	
	public List<Integer> obtenerIdsTramitesSeleccionados();
	
	public List<ArbolGenericoObject> getSeleccionados();

	public void setSeleccionados(List<ArbolGenericoObject> seleccionados);
	
	public void setCondicionesSeleccionadas(List<CondicionUrbanisticaSimplificadaDTO> condicionesSeleccionadas);

	public List<CondicionUrbanisticaSimplificadaDTO> getCondicionesSeleccionadas();
	
	public boolean renderizarBtnSelTramite();
}

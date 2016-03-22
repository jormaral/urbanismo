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

import com.icesoft.faces.component.ext.RowSelectorEvent;
import com.mitc.redes.editorfip.entidades.interfazgrafico.CondicionUrbanisticaSimplificadaDTO;
import com.mitc.redes.editorfip.servicios.genericos.SortableListIF;

@Local
public interface ListaCUSimplificada extends SortableListIF
{
	public boolean isEntidadAplicadaGrupoAplicacion();
	
	public void refrescarLista ();
	
	public List<CondicionUrbanisticaSimplificadaDTO> getCuSimplificadaList();
	
	public String getNombreColumnName();
	
	public void sort();
	
	public boolean isDefaultAscending(String sortColumn);
	
	public void seleccionarTodas(boolean sel);
	
	public void rowSelectionListener (RowSelectorEvent event);
	
	/**
	 * Modifica el valor de la propiedad '@param seleccionadasTodas'
	 * 
	 * @param seleccionadasTodas valor de tipo 
	 */
	/**
	 * Modifica el valor de la propiedad '@param seleccionadasTodas'
	 * 
	 * @param seleccionadasTodas valor de tipo 
	 */
	public void setSeleccionadasTodas(boolean seleccionadasTodas);

	public boolean isSeleccionadasTodas();
	
	public boolean isEsAlgunaSeleccionada();
	
	public void seleccionarEntidadesCopiar();
	
	public void llamarPopUpCU();

	public void llamarPopUpGA();
	
	public void seleccionarEntidadesCopiarListener(ActionEvent ae) ;
	
	

}
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

package com.mitc.redes.editorfip.servicios.gestionfip.gestionplaneamientoencargado;

import java.util.HashMap;
import java.util.List;

import javax.ejb.Local;
import javax.ejb.Remove;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import com.mitc.redes.editorfip.entidades.rpm.gestionfip.PlanesEncargados;
import com.mitc.redes.editorfip.servicios.basicos.comunes.plantillas.ListaGenerica;

/**
 * Servicio que gestiona las operaciones relacionadas con la lista de
 * planes encargados.
 * 
 * @author fguerrero
 *
 */
@Local
public interface GestionPlaneamientoEncargadoList extends ListaGenerica {
	
	
	/**
	 * Obtiene el valor de la propiedad ''
	 * 
	 * @return valor de tipo List<SelectItem>
	 */
	public List<SelectItem> getResultadosBusqueda();
	/**
	 * Modifica el valor de la propiedad '@param resultadosBusqueda'
	 * 
	 * @param resultadosBusqueda valor de tipo 
	 */
	public void setResultadosBusqueda(List<SelectItem> resultadosBusqueda);
	/**
	 * Obtiene el valor de la propiedad ''
	 * 
	 * @return valor de tipo long
	 */
	public long getPlanEncargadoSel();
	/**
	 * Modifica el valor de la propiedad '@param planEncargadoSel'
	 * 
	 * @param planEncargadoSel valor de tipo 
	 */
	public void setPlanEncargadoSel(long planEncargadoSel);
	
	/**
	 * Obtiene el valor de la propiedad ''
	 * 
	 * @return valor de tipo List<PlanesEncargados>
	 */
	public List<PlanesEncargados> getListaPlanesEncargados();
	public void darDebaja() ;
	public String verDetalle();
	public String verDetalleAL(ActionEvent ae);
	/**
	 * Obtiene el valor de la propiedad ''
	 * 
	 * @return valor de tipo String
	 */
	public String getIdPlanSeleccionado();
	
	public void actualizarListaPlanesEncargados(ValueChangeEvent event);
	
	/**
	 * Obtiene el valor de la propiedad ''
	 * 
	 * @return valor de tipo HashMap
	 */
	public HashMap getSeleccion();
	
	@Remove
	public void remove();
	

}

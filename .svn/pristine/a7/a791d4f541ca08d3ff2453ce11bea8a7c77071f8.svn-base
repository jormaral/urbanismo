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

package com.mitc.redes.editorfip.servicios.informacionfip.busqueda;

import java.util.List;

import javax.ejb.Local;
import javax.faces.model.SelectItem;

import com.mitc.redes.editorfip.entidades.busqueda.BusquedaOperacionDTO;
import com.mitc.redes.editorfip.entidades.busqueda.FiltrosDTO;

@Local
public interface ServicioBusquedaOperacion {
	
	
	public void buscar();
	
	public void destroy();
	
	/**
	 * Obtiene el valor de la propiedad ''
	 * 
	 * @return valor de tipo List<BusquedaOperacionDTO>
	 */
	public List<BusquedaOperacionDTO> getResultado();
	
	/**
	 * Obtiene el valor de la propiedad ''
	 * 
	 * @return valor de tipo FiltrosDTO
	 */
	public FiltrosDTO getFiltros();
	
	/**
	 * Obtiene el valor de la propiedad ''
	 * 
	 * @return valor de tipo List<SelectItem>
	 */
	public List<SelectItem> getListaTiposFiltro();

	/**
	 * Obtiene el valor de la propiedad ''
	 * 
	 * @return valor de tipo List<SelectItem>
	 */
	public List<SelectItem> getListaClaseOperacion();
	
	/**
	 * Obtiene el valor de la propiedad ''
	 * 
	 * @return valor de tipo List<SelectItem>
	 */
	public List<SelectItem> getListaTipoOperacion();
	
	public List<SelectItem> tipoOperacionEntidad();
	
	public void verDetalleOperacion(int idOperacion, String clase);
	
	//** NAVEGACION **//
	/**
	 * Obtiene el valor de la propiedad ''
	 * 
	 * @return valor de tipo boolean
	 */
	public boolean isRenderizarAnterior();	
	/**
	 * Obtiene el valor de la propiedad ''
	 * 
	 * @return valor de tipo boolean
	 */
	public boolean isRenderizarSiguiente();	
	public void siguientePag();	
	public void anteriorPag();	
	public void ultimo();	
	public void primero();
	
	public List<SelectItem> getListaTipoPlan();
}
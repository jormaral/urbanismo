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
import javax.faces.model.SelectItem;

import com.mitc.redes.editorfip.entidades.interfazgrafico.EntidadBusquedaDTO;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Entidad;

@Local
public interface GestionEntidades {

	
	public int getIdEntidadOLD();
	/**
	 * Obtiene el valor de la propiedad ''
	 * 
	 * @return valor de tipo Entidad
	 */
	public Entidad getEntidad();
	/**
	 * Modifica el valor de la propiedad '@param entidad'
	 * 
	 * @param entidad valor de tipo 
	 */
	public void setEntidad(Entidad entidad);
	public void guardarEntidad(ActionEvent action);
	/**
	 * Obtiene el valor de la propiedad ''
	 * 
	 * @return valor de tipo int
	 */
	public int getIdEntidad();
	/**
	 * Modifica el valor de la propiedad '@param idEntidad'
	 * 
	 * @param idEntidad valor de tipo 
	 */
	public void setIdEntidad(int idEntidad);

	// Borrar
	public void borrarEntidadYCUEHijas(ActionEvent action);
	
	// Duplicar Entidad con CU
	public void duplicarEntidadConCUs(ActionEvent action);


	public void prepararParaCrear();

	public String obtenerVistaActual();


	/**
	 * Obtiene el valor de la propiedad ''
	 * 
	 * @return valor de tipo int
	 */
	public int getIdEntidadSeleccionado();
	public void actualizarLista(ValueChangeEvent event);
	public void actualizarListaEntidades(ValueChangeEvent event);
	/**
	 * Obtiene el valor de la propiedad ''
	 * 
	 * @return valor de tipo List<SelectItem>
	 */
	public List<SelectItem> getResultadosBusqueda();
	/**
	 * Obtiene el valor de la propiedad ''
	 * 
	 * @return valor de tipo List<EntidadBusquedaDTO>
	 */
	public List<EntidadBusquedaDTO> getResultadosEntidad();

	/**
	 * Obtiene el valor de la propiedad ''
	 * 
	 * @return valor de tipo boolean
	 */
	public boolean isFiltroDetAplicada();
	/**
	 * Modifica el valor de la propiedad '@param filtroDetAplicada'
	 * 
	 * @param filtroDetAplicada valor de tipo 
	 */
	public void setFiltroDetAplicada(boolean filtroDetAplicada);

	/**
	 * Obtiene el valor de la propiedad ''
	 * 
	 * @return valor de tipo boolean
	 */
	public boolean isFiltroNombre();
	/**
	 * Modifica el valor de la propiedad '@param filtroNombre'
	 * 
	 * @param filtroNombre valor de tipo 
	 */
	public void setFiltroNombre(boolean filtroNombre);

	/**
	 * Obtiene el valor de la propiedad ''
	 * 
	 * @return valor de tipo boolean
	 */
	public boolean isFiltroClave();
	/**
	 * Modifica el valor de la propiedad '@param filtroClave'
	 * 
	 * @param filtroClave valor de tipo 
	 */
	public void setFiltroClave(boolean filtroClave);

	/**
	 * Obtiene el valor de la propiedad ''
	 * 
	 * @return valor de tipo boolean
	 */
	public boolean isMostrarPanelEntidadBase();
	/**
	 * Modifica el valor de la propiedad '@param mostrarPanelEntidadBase'
	 * 
	 * @param mostrarPanelEntidadBase valor de tipo 
	 */
	public void setMostrarPanelEntidadBase(boolean mostrarPanelEntidadBase);

	/**
	 * Obtiene el valor de la propiedad ''
	 * 
	 * @return valor de tipo boolean
	 */
	public boolean isMostrarPanelEntidadPadre();
	/**
	 * Modifica el valor de la propiedad '@param mostrarPanelEntidadPadre'
	 * 
	 * @param mostrarPanelEntidadPadre valor de tipo 
	 */
	public void setMostrarPanelEntidadPadre(boolean mostrarPanelEntidadPadre);

	/**
	 * Obtiene el valor de la propiedad ''
	 * 
	 * @return valor de tipo boolean
	 */
	public boolean isMostrarPanelFiltros();
	/**
	 * Modifica el valor de la propiedad '@param mostrarPanelFiltros'
	 * 
	 * @param mostrarPanelFiltros valor de tipo 
	 */
	public void setMostrarPanelFiltros(boolean mostrarPanelFiltros);
	public void aceptarFiltros(ActionEvent event);
	public void mostrarPanel();

	public void mostrarPanelBaseAL(ActionEvent event);
	public void mostrarPanelPadreAL(ActionEvent event);

	/**
	 * Obtiene el valor de la propiedad ''
	 * 
	 * @return valor de tipo int
	 */
	public int getIdEntidadBase();
	/**
	 * Modifica el valor de la propiedad '@param idEntidadBase'
	 * 
	 * @param idEntidadBase valor de tipo 
	 */
	public void setIdEntidadBase(int idEntidadBase);

	/**
	 * Obtiene el valor de la propiedad ''
	 * 
	 * @return valor de tipo int
	 */
	public int getIdEntidadPadre();
	/**
	 * Modifica el valor de la propiedad '@param idEntidadPadre'
	 * 
	 * @param idEntidadPadre valor de tipo 
	 */
	public void setIdEntidadPadre(int idEntidadPadre);

	public void cancelar();


	// ------------------
	// GEOMETRIAS
	// -----------------
	public void borrarGeometriaEntidad(ActionEvent action);
	public void guardarGeometriaEntidad();
	/**
	 * Obtiene el valor de la propiedad ''
	 * 
	 * @return valor de tipo String
	 */
	public String getGeometriaWKT();
	/**
	 * Modifica el valor de la propiedad '@param geometriaWKT'
	 * 
	 * @param geometriaWKT valor de tipo 
	 */
	public void setGeometriaWKT(String geometriaWKT);

	public void destroy();
	
	public void guardarGeometriaShapeEntidad(String geometria, String nombreBase);
	
	public void cerrarAnadirGeometriaShape(String nombreBase);
	
	public void cerrarVentanaFiltros(ActionEvent event);
}

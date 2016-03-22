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

package com.mitc.redes.editorfip.servicios.produccionfip.gestionadscripciones;

import javax.ejb.Local;

import com.mitc.redes.editorfip.entidades.interfazgrafico.AdscripcionesDTO;
import com.mitc.redes.editorfip.entidades.interfazgrafico.AdscripcionesTramiteDTO;
import com.mitc.redes.editorfip.entidades.interfazgrafico.UnidadTramiteDTO;

@Local
public interface GestionAdscripciones
{
    
	
	public void nuevaAdscripcion();
	
	
	public void verAdscripcion(int idAdscripcion, String ruta);
	
	
	public void borrarAdscripcion(int idAdscripcion);
	 
	
	public void guardarAdscripcion();
	
	
	/**
	 * Obtiene el valor de la propiedad ''
	 * 
	 * @return valor de tipo AdscripcionesDTO
	 */
	public AdscripcionesDTO getAdscripcionesDTO();
	
	
	/**
	 * Modifica el valor de la propiedad '@param adscripcionesDTO'
	 * 
	 * @param adscripcionesDTO valor de tipo 
	 */
	public void setAdscripcionesDTO(AdscripcionesDTO adscripcionesDTO);
	
	
	// ---------
	// Carga de datos
	// --------
	
	/**
	 * Metodo que carga la entidad origen
	 * @param idEntidadOrigen
	 * @param nombreEntidadOrigen
	 */
	public void cargarDatosEntidadOrigen(int idEntidadOrigen, String nombreEntidadOrigen);
	
	
	/**
	 * Metodo que carga la entidad destino
	 * @param idEntidadDestino
	 * @param nombreEntidadDestino
	 */
	public void cargarDatosEntidadDestino(int idEntidadDestino, String nombreEntidadDestino);
	
	
	/**
	 * 
	 * @param unidad
	 * @param volverPag
	 */
	public void cargarDatosDeterminacionUnidad(UnidadTramiteDTO unidad, String volverPag);
	
	
	/**
	 * 
	 * @param tipoAdscripcion
	 * @param volverPag
	 */
	public void cargarDatosDeterminacionTipo(AdscripcionesTramiteDTO tipoAdscripcion, String volverPag);
	
	
	// Paneles POPUP
	
	/**
	 * Obtiene el valor de la propiedad ''
	 * 
	 * @return valor de tipo boolean
	 */
	public boolean isMostrarPanelEntidadDestino();
	
	/**
	 * Modifica el valor de la propiedad '@param mostrarPanelEntidadDestino'
	 * 
	 * @param mostrarPanelEntidadDestino valor de tipo 
	 */
	public void setMostrarPanelEntidadDestino(boolean mostrarPanelEntidadDestino);
	
	/**
	 * Obtiene el valor de la propiedad ''
	 * 
	 * @return valor de tipo boolean
	 */
	public boolean isMostrarPanelEntidadOrigen();
	
	/**
	 * Modifica el valor de la propiedad '@param mostrarPanelEntidadOrigen'
	 * 
	 * @param mostrarPanelEntidadOrigen valor de tipo 
	 */
	public void setMostrarPanelEntidadOrigen(boolean mostrarPanelEntidadOrigen);
	
	/**
	 * Obtiene el valor de la propiedad ''
	 * 
	 * @return valor de tipo boolean
	 */
	public boolean isMostrarPanelTipoAdscripcion();
	
	/**
	 * Modifica el valor de la propiedad '@param mostrarPanelTipoAdscripcion'
	 * 
	 * @param mostrarPanelTipoAdscripcion valor de tipo 
	 */
	public void setMostrarPanelTipoAdscripcion(boolean mostrarPanelTipoAdscripcion);
	
	/**
	 * Obtiene el valor de la propiedad ''
	 * 
	 * @return valor de tipo boolean
	 */
	public boolean isMostrarPanelUnidadAdscripcion();
	
	/**
	 * Modifica el valor de la propiedad '@param mostrarPanelUnidadAdscripcion'
	 * 
	 * @param mostrarPanelUnidadAdscripcion valor de tipo 
	 */
	public void setMostrarPanelUnidadAdscripcion(boolean mostrarPanelUnidadAdscripcion);
	
	
	public void destroy();
	

}

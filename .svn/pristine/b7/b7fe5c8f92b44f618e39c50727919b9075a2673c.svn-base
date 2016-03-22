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

package com.mitc.redes.editorfip.servicios.produccionfip.gestionoperaciones;

import java.util.List;

import javax.ejb.Local;
import javax.faces.model.SelectItem;

import org.jboss.seam.faces.FacesManager;

import com.mitc.redes.editorfip.entidades.interfazgrafico.AdscripcionesDTO;
import com.mitc.redes.editorfip.entidades.interfazgrafico.AdscripcionesTramiteDTO;
import com.mitc.redes.editorfip.entidades.interfazgrafico.UnidadTramiteDTO;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Operaciondeterminacion;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Operacionentidad;

@Local
public interface GestionOperaciones {
	
	
	// -------
	// Adscripciones
	// -----
	public boolean isMostrarPanelTipoAdscripcion();


	public void setMostrarPanelTipoAdscripcion(boolean mostrarPanelTipoAdscripcion);


	public boolean isMostrarPanelUnidadAdscripcion();


	public void setMostrarPanelUnidadAdscripcion(boolean mostrarPanelUnidadAdscripcion);
	
	public void cargarDatosDeterminacionUnidad(UnidadTramiteDTO unidad, String volverPag);

	public void cargarDatosDeterminacionTipo(AdscripcionesTramiteDTO tipoAdscripcion, String volverPag);
	
	public void nuevaAdscripcion();

	
	public AdscripcionesDTO getAdscripcionesDTO();


	public void setAdscripcionesDTO(AdscripcionesDTO adscripcionesDTO);
	

	// ------------------------------
	// OPERACIONES
	// ------------------------------

	// ------------------------------
	// OPERACIONES ENTIDAD
	// ------------------------------
	
	public String nuevoOperacionEntidad();

	public String borrarOperacionEntidad(int idOpEnt);

	/**
	 * Obtiene el valor de la propiedad ''
	 * 
	 * @return valor de tipo String
	 */
	public String guardarOperacionEntidad();
	
	public List<SelectItem> tipoOperacionEntidad();
	
	/**
	 * Obtiene el valor de la propiedad ''
	 * 
	 * @return valor de tipo Operacionentidad
	 */
	public Operacionentidad getOperacionEntidad();
	
	/**
	 * Modifica el valor de la propiedad '@param operacionEntidad'
	 * 
	 * @param operacionEntidad valor de tipo 
	 */
	public void setOperacionEntidad(Operacionentidad operacionEntidad);
	
	/**
	 * Obtiene el valor de la propiedad ''
	 * 
	 * @return valor de tipo int
	 */
	public int getIdTipoOperacionEntidad();

	/**
	 * Modifica el valor de la propiedad '@param idTipoOperacionEntidad'
	 * 
	 * @param idTipoOperacionEntidad valor de tipo 
	 */
	public void setIdTipoOperacionEntidad(int idTipoOperacionEntidad);
	
	public void seleccionarEntidadOperada(int idEntidad);
	
	public void seleccionarEntidadOperadora(int idEntidad);
	
	/**
	 * Obtiene el valor de la propiedad ''
	 * 
	 * @return valor de tipo boolean
	 */
	public boolean isMostrarPopUpEntidadOperadora();
	/**
	 * Modifica el valor de la propiedad '@param mostrarPopUpEntidadOperadora'
	 * 
	 * @param mostrarPopUpEntidadOperadora valor de tipo 
	 */
	public void setMostrarPopUpEntidadOperadora(boolean mostrarPopUpEntidadOperadora);

	/**
	 * Obtiene el valor de la propiedad ''
	 * 
	 * @return valor de tipo boolean
	 */
	public boolean isMostrarPopUpEntidadOperada();
	/**
	 * Modifica el valor de la propiedad '@param mostrarPopUpEntidadOperada'
	 * 
	 * @param mostrarPopUpEntidadOperada valor de tipo 
	 */
	public void setMostrarPopUpEntidadOperada(boolean mostrarPopUpEntidadOperada);

	// ------------------------------
	// OPERACIONES DETERMINACION
	// ------------------------------

	public String nuevoOperacionDeterminacion();

	public String borrarOperacionDeterminacion(int idOpDet);

	/**
	 * Obtiene el valor de la propiedad ''
	 * 
	 * @return valor de tipo String
	 */
	public String guardarOperacionDeterminacion();
	
	public List<SelectItem> tipoOperacionDeterminacion();
	
	/**
	 * Obtiene el valor de la propiedad ''
	 * 
	 * @return valor de tipo Operaciondeterminacion
	 */
	public Operaciondeterminacion getOperacionDeterminacion();
	
	/**
	 * Modifica el valor de la propiedad '@param operacionDeterminacion'
	 * 
	 * @param operacionDeterminacion valor de tipo 
	 */
	public void setOperacionDeterminacion(Operaciondeterminacion operacionDeterminacion);
	
	/**
	 * Obtiene el valor de la propiedad ''
	 * 
	 * @return valor de tipo int
	 */
	public int getIdTipoOperacionDeterminacion();

	/**
	 * Modifica el valor de la propiedad '@param idTipoOperacionDeterminacion'
	 * 
	 * @param idTipoOperacionDeterminacion valor de tipo 
	 */
	public void setIdTipoOperacionDeterminacion(int idTipoOperacionDeterminacion);
	
	public void seleccionarDeterminacionOperada(int idDeterminacion);
	
	public void seleccionarDeterminacionOperadora(int idDeterminacion);
	
	/**
	 * Obtiene el valor de la propiedad ''
	 * 
	 * @return valor de tipo boolean
	 */
	public boolean isMostrarPopUpDeterminacionOperadora();
	/**
	 * Modifica el valor de la propiedad '@param mostrarPopUpDeterminacionOperadora'
	 * 
	 * @param mostrarPopUpDeterminacionOperadora valor de tipo 
	 */
	public void setMostrarPopUpDeterminacionOperadora(
			boolean mostrarPopUpDeterminacionOperadora);

	/**
	 * Obtiene el valor de la propiedad ''
	 * 
	 * @return valor de tipo boolean
	 */
	public boolean isMostrarPopUpDeterminacionOperada();
	/**
	 * Modifica el valor de la propiedad '@param mostrarPopUpDeterminacionOperada'
	 * 
	 * @param mostrarPopUpDeterminacionOperada valor de tipo 
	 */
	public void setMostrarPopUpDeterminacionOperada(
			boolean mostrarPopUpDeterminacionOperada);

	public void destroy();
	
	/**
	 * Obtiene el valor de la propiedad ''
	 * 
	 * @return valor de tipo Integer
	 */
	public Integer getIdOperacion();
	/**
	 * Modifica el valor de la propiedad '@param idOperacion'
	 * 
	 * @param idOperacion valor de tipo 
	 */
	public void setIdOperacion(Integer idOperacion);
	

}

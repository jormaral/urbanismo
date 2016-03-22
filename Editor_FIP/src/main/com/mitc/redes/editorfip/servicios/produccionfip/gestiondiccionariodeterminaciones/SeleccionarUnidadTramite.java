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

package com.mitc.redes.editorfip.servicios.produccionfip.gestiondiccionariodeterminaciones;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Remove;

import com.mitc.redes.editorfip.entidades.interfazgrafico.UnidadTramiteDTO;
import com.mitc.redes.editorfip.servicios.genericos.SortableListIF;

@Local
public interface SeleccionarUnidadTramite extends SortableListIF
{
    
	
	/**
	 * Obtiene el valor de la propiedad ''
	 * 
	 * @return valor de tipo List<UnidadTramiteDTO>
	 */
	public List<UnidadTramiteDTO> getUnidades();
	
	/**
	 * Obtiene el valor de la propiedad ''
	 * 
	 * @return valor de tipo String
	 */
	public String getNombreColumnName();
	
	/**
	 * Obtiene el valor de la propiedad ''
	 * 
	 * @return valor de tipo String
	 */
	public String getOrdenColumnName();
	
	public void sort();
	
	/**
	 * Obtiene el valor de la propiedad ''
	 * 
	 * @return valor de tipo boolean
	 */
	public boolean isDefaultAscending(String sortColumn);
	
	/**
	 * Obtiene el valor de la propiedad ''
	 * 
	 * @return valor de tipo Integer
	 */
	public Integer getIdTipoTramiteTrabajoSU();
	
	/**
	 * Modifica el valor de la propiedad '@param idTipoTramiteTrabajoSU'
	 * 
	 * @param idTipoTramiteTrabajoSU valor de tipo 
	 */
	public void setIdTipoTramiteTrabajoSU(Integer idTipoTramiteTrabajoSU);	
	
	public void seleccionarUnidad(int idDetUnidad);
	
	/**
	 * Obtiene el valor de la propiedad ''
	 * 
	 * @return valor de tipo UnidadTramiteDTO
	 */
	public UnidadTramiteDTO obtenerUnidadSeleccionada();
	
	public void deSeleccionarTodasUnidad();
	
	@Remove
	public void destroy();

}
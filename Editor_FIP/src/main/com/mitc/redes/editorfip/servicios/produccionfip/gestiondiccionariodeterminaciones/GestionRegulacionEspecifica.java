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

import javax.ejb.Local;
import javax.ejb.Remove;
import javax.faces.event.ActionEvent;

import com.mitc.redes.editorfip.entidades.interfazgrafico.RegulacionEspecificaDTO;

@Local
public interface GestionRegulacionEspecifica {
	
	/**
	 * Obtiene el valor de la propiedad ''
	 * 
	 * @return valor de tipo RegulacionEspecificaDTO
	 */
	public RegulacionEspecificaDTO getRegulacionEspecifica();
	
	/**
	 * Modifica el valor de la propiedad '@param regulacionEspecifica'
	 * 
	 * @param regulacionEspecifica valor de tipo 
	 */
	public void setRegulacionEspecifica(RegulacionEspecificaDTO regulacionEspecifica);
	
	/**
	 * Modifica el valor de la propiedad '@param idRelacion'
	 * 
	 * @param idRelacion valor de tipo 
	 */
	public void setIdRelacion(int idRelacion);
		
	public void nuevoRegulacionEspecifica();
	
	public void borrarRegulacionEspecifica(ActionEvent event);
	
	public void guardarRegulacionEspecifica(ActionEvent event);
	
	public void cargarDatosPadre ();
	
	public String obtenerViewRedirect(String pageRedirect);
	
	@Remove
	public void destroy();
	
}

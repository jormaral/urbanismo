/*
* Copyright 2011 red.es
* Autores: IDOM Consulting
*
* Licencia con arreglo a la EUPL, Versiï¿½n 1.1 o -en cuanto
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

package com.mitc.redes.editorfip.servicios.basicos.fip.adscripciones;

import java.util.List;

import javax.ejb.Local;

import com.mitc.redes.editorfip.entidades.interfazgrafico.AdscripcionesDTO;

/**
 * Servicio que gestiona las adscripciones
 * 
 * @author fguerrero
 *
 */

@Local
public interface ServicioBasicoAdscripciones
{
	
	/**
	 * Crear una adscripción nueva
	 * 
	 * @param adscrp objeto con los datos de la nueva adscripción
	 * @param idTramite id del tramite actual de trabajo
	 * @return ID de la nueva adscripcion
	 */
	public int crearAdscripcion(AdscripcionesDTO adscrp, int idTramite);
	
	
	/**
	 * Obtiene todas las adscripciones de un trámite en concreto
	 * 
	 * @param idTramite id del tramite actual de trabajo 
	 * @return lista de adscripciones de dicho trámite
	 */
	public List<AdscripcionesDTO> obtenerListaAdscripcionesDeTramite(int idTramite);
	
	
	/**
	 * Obtiene una lista reducida de adscripciones de un trámite en concreto
	 * 
	 * @param idTramite id del tramite actual de trabajo 
	 * @return lista de adscripciones de dicho trámite
	 */
	public List<AdscripcionesDTO> obtenerListaAdscripcionesReducidaDeTramite(int idTramite);
	
	/**
	 * Busca una adscripción en base a su ID.
	 * 
	 * @param idRelacion ID de la adscripción
	 * @return Adscripcioón asociada al ID.
	 */
	public AdscripcionesDTO buscarAdscripcion(int idRelacion);
	
	/**
	 * 
	 * Elimina una adscripción de BD.
	 * 
	 * @param idRelacion id de la adscripción a borrar.
	 * @return true si la operación de borrado se ha realizado 
	 * correctamente, false en caso contrario.
	 */
	public boolean borrarAdscripcion(int idRelacion);
	
	
	
	
}

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

package com.mitc.redes.editorfip.servicios.basicos.fip.entidades;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Remove;

import com.mitc.redes.editorfip.entidades.interfazgrafico.EntidadBusquedaDTO;
import com.mitc.redes.editorfip.entidades.rpm.gestionfip.ColoresEntidades;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Entidad;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Entidadlin;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Entidadpnt;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Entidadpol;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Regimenespecifico;

/**
 * Servicio CRUD(Create-Read-Update-Delete) de entidades.
 * 
 * @author fguerrero
 *
 */
@Local
public interface ServicioCRUDEntidades
{
	/**
	 * Inserta un nuevo registro de tipo Entidad en BD.
	 * 
	 * @param entidad objeto con los datos del nuevo registo.
	 * @return ID generado en la operacion de creación.
	 */
	public int create(Entidad entidad);
	
	/**
	 * Actualiza los datos de una entidad existente en BD.
	 * 
	 * @param entidad objeto con los datos ya actualizados.
	 */
	public void edit(Entidad entidad);
		
	/**
	 * Inserta un nuevo registro de tipo ColoresEntidades en BD.
	 * 
	 * @param entidad objeto con los datos del nuevo registo.
	 * @return ID generado en la operacion de creación.
	 */
	public int createColor(ColoresEntidades entidad);
	
	/**
	 * Actualiza los datos de una entidad en existente en BD, en la tabla coloresentidades.
	 * 
	 * @param entidad objeto con los datos ya actualizados.
	 */
	
	public void editColor(ColoresEntidades entidad);
	
	/**
	 * Devuelve una busqueda filtrada por nombre y tramite que sera utilizada 
	 * para autocompletar un campo de texto de busqueda de entidades.
	 * 
	 * @param word filtro para el nombre.
	 * @param idTramite Id del trámite actual de trabajo.
	 * @return lista de objetos de tipo Entidad.
	 */
	
	/**
	 * 
	 */
	public void borrarColorDeEntidad(int idEntidad);
	
	public List<Entidad> autocomplete(String word, int idTramite);
	
	/**
	 * Devuelve una busqueda filtrada por tramite que sera utilizada 
	 * para autocompletar un campo de texto de busqueda de entidades.
	 * 
	 * @param word palabra 'filtro' a buscar.
	 * @param idTramite Id del trámite actual de trabajo.
	 * @param nombre nombre de la entidad.
	 * @param determinacion incluye determinación.
	 * @param referencia incluye referencia.
	 * @return lista de objetos de tipo EntidadBusquedaDTO.
	 */
	public List<EntidadBusquedaDTO> autocompleteFiltros(String word, int idTramite, boolean nombre, boolean determinacion, boolean referencia);

	/**
	 * Elimina la entidad y sus respectivas condiciones urbanísticas de 
	 * forma recursiva.
	 * 
	 * @param idEntidad ID de la entidad a eliminar.
	 */
	public void borrarEntidadYCURecursivo(int idEntidad);
	
	/**
	 * Elimina las condiciones urbanísticas asociadas a una entidad
	 * de forma recursiva.
	 * 
	 * @param idEntDet
	 */
	public void borraCURecursivo(int idEntDet);
	
	
	/**
	 * Elimina un regimen especifico y todos los regimenes especificos
	 * asociados a este mismo. 
	 * 
	 * @param re regimen padre.
	 */
	public void borraRegimenEspecificoRecursivo(Regimenespecifico re);
	
	/**
	 * Obtiene una lista de regimenes especificos en base a un regimen 
	 * padre.
	 * 
	 * @param idPadre ID del regimen padre.
	 * @return lista de regimenes 'hijos'.
	 */
	public List<Regimenespecifico> obtenerRegimenesEspecificosPorIdPadre(int idPadre);
	
	/**
	 * Obtiene una lista de entidades especificos en base a una entidad 
	 * padre.
	 * 
	 * @param idPadre ID de la entidad padre.
	 * @return lista de entidades 'hijas'.
	 */
	public List<Entidad> obtenerEntidadesPorIdPadre(int idPadre);
	
	
	// -----------------
	// GEOMETRIAS
	// ------------------
	
	
	
	/**
	 * Elimina una geometría asociada a una entidad.
	 * 
	 * @param idEntidad ID de la entidad asociada a la geometría a eliminar.
	 */
	public void borrarGeometriaDeEntidad(int idEntidad);
	
	/**
	 * Verifica si una entidad tiene asociada una geometría.
	 * 
	 * @param idEntidad ID de la entidad a verificar.
	 * @return
	 */
	public boolean tieneGeometriaEntidad (int idEntidad);
	
	@Remove
    public void destroy();
}

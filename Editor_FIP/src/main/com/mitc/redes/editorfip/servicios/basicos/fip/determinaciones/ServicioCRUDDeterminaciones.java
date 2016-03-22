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

package com.mitc.redes.editorfip.servicios.basicos.fip.determinaciones;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Remove;

import com.mitc.redes.editorfip.entidades.interfazgrafico.DeterminacionBusquedaDTO;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Determinacion;

/**
 * Servicio básico CRUD(Create-Read-Update-Delete) de Determinaciones.
 * 
 * @author fguerrero
 *
 */
@Local
public interface ServicioCRUDDeterminaciones
{
	/**
	 * Borra una determinación de forma jerárquica (Incluyendo sus hijas 'directas').
	 * 
	 * @param idDeterminacion id de la determinación a eliminar.
	 * @throws Exception
	 */
	public void borrarDeterminacionesRecursivo(int idDeterminacion) throws Exception;
	
	/**
	 * Borra una determinación y todas las condiciones urbanisticas asociadas a 
	 * esta misma (No elimina hijas de forma recursiva). 
	 * 
	 * @param idDeterminacion id de la determinación a eliminar.
	 * @throws Exception
	 */
	public void borrarCUsAsociadasADeterminacion(int idDeterminacion)throws Exception;
	
	/**
	 * Borra una determinación de forma jerárquica (Incluyendo sus hijas 'directas') ademas
	 * de eliminar todas las condiciones urbanisticas asociadas a dicha determinación.
	 * 
	 * @param idDeterminacion id de la determinación a eliminar.
	 * @throws Exception
	 */
	public void borrarDeterminacionesYCUsAsociadasADeterminacionRecursivo(int idDeterminacion) throws Exception;
	
	/**
	 * Inserta un registro nuevo en la tabla de determinaciones.
	 * 
	 * @param determinacion objeto de tipo Determinacion con los datos nuevos.
	 * @return ID asignado a la nueva determinación despues de haberse insertado en BD.
	 */
	public int create(Determinacion determinacion);
	
	/**
	 * Actualiza los datos de una determinación ya existente en BD.
	 * 
	 * @param determinacion objeto de tipo Determinación con sus datos ya actualizados.
	 */
	public void edit(Determinacion determinacion);
	
	/**
	 * Metodo que se utilíza para autocompletar un campo de texto de busqueda de 
	 * determinaciones.
	 * 
	 * @param word palabra 'filtro' introducida en el campo de texto.
	 * @param idTramite ID del trámite actual de trabajo.
	 * @return resultado de la busqueda.
	 */
	public List<Determinacion> autocomplete(String word, int idTramite);
	
	/**
	 * Metodo que se utilíza para autocompletar un campo de texto de busqueda de 
	 * determinaciones. Incluye varios filtros de busqueda.
	 * 
	 * @param word palabra 'filtro' introducida en el campo de texto.
	 * @param idTramite ID del trámite actual de trabajo.
	 * @param filtroNombre 
	 * @param filtroApartado
	 * @param filtroDeterminacion
	 * @return resultado de la busqueda.
	 */
	 public List<DeterminacionBusquedaDTO> autocompleteFiltros(String word, int idTramite, boolean filtroNombre,
	    		boolean filtroApartado, boolean filtroDeterminacion);
	 
	 public String nextCodigo(int idBDTramite);
	    		
	@Remove
    public void destroy();
}

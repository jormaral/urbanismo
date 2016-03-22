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

import com.mitc.redes.editorfip.entidades.busqueda.FiltrosDTO;
import com.mitc.redes.editorfip.entidades.interfazgrafico.CondicionUrbanisticaSimplificadaDTO;
import com.mitc.redes.editorfip.entidades.interfazgrafico.DeterminacionDTO;

/**
 * Servicio con operaciones basicas a realizar sobre Condiciones Urbanísticas.
 * 
 * 
 * @author fguerrero
 *
 */
@Local
public interface ServicioBasicoCondicionesUrbanisticas {

	
	
	// ------------------------------
    // CONDICIONES URBANISTICAS
    // ------------------------------	
	
	 public List<DeterminacionDTO> obtenerDeterminacionesAplicablesComoCUPorTramite (int idEntidad, int idTramite);
	   
	
	/**
	 * Obtiene una lista de determinaciones las cuales pueden ser aplicadas como 
	 * condiciones urbanísticas.
	 * 
	 * @param idEntidad ID de la entidad de la cual se va a obtener la lista.
	 * @return lista de determinaciones en formato determinacionDTO 
	 * 
	 */
	public List<DeterminacionDTO> obtenerDeterminacionesAplicablesComoCU (int idEntidad);
	
	/**
	 * Obtiene una lista simplificada con todas las condiciones urbanísticas refenrentes a 
	 * una entidad 
	 * 
	 * @param idEntidad ID de la entidad sobre la cual se va a operar.
	 * @return lista de condiciones urbanísticas en formato CondicionUrbanisticaSimplificadaDTO
	 */
	public List<CondicionUrbanisticaSimplificadaDTO> listadoTodasCUSimplificadaDeEntidad (int idEntidad);
	
	/**
	 * Obtiene el valor de referencia correspondiente a una condición urbanística de una 
	 * entidad aplicada a un grupo de aplicación.
	 * 
	 * @param idEntidad ID de la entidad sobre la cual se va a operar.
	 * @return valor de referencia en formato Integer.
	 */
	public int obtenerVRdeCUdeEntidadAplicadaAGA(int idEntidad);
	
	
	/**
	 * Crea una nueva condición urbanística.
	 * 
	 * @param cuSimplificada objeto con los datos de la nueva condición.
	 * @return ID de la nueva condición.
	 */
	public int crearCU(CondicionUrbanisticaSimplificadaDTO cuSimplificada);

	/**
	 * Obtiene una lista con todas las condiciones urbanísticas asociadas a un trámite.
	 * 
	 * @param idTramite ID del tramite sobre el cual se desea obtener las condiciones.
	 * @return lista con objetos del tipo CondicionUrbanisticaSimplificadaDTO.
	 */
	public List<CondicionUrbanisticaSimplificadaDTO> listadoTodasCUSimplificadaDeTramite(
			int idTramite);

	
	/**
	 * Obtiene el nombre de la entidad en base a su identificador.
	 * 
	 * @param idEntidad ID de la entidad.
	 * @return nombre en formato String.
	 */
	public String nombreEntidad(int idEntidad);

	/**
	 * Obtiene el nombre de la determinación en base a su identificador.
	 * 
	 * @param idDeterminacion ID de la determinación.
	 * @return nombre en formato String.
	 */
	public String nombreDeterminacion(int idDeterminacion);
	
	/**
	 * Actualiza los datos de una condición urbanística existente en BD.
	 * 
	 * @param cuSimplificada objeto con los datos modificados de la condición 
	 * urbanística.
	 * @return ID de la condición modificada.
	 */
	public int actualizarCU(CondicionUrbanisticaSimplificadaDTO cuSimplificada);
	
	/**
	 * Crea una nueva condición urbanística en base a un grupo de aplicación
	 * 
	 * @param cuSimplificada objeto con los datos modificados de la condición 
	 * urbanística.
	 * @return ID de la condición modificada.
	 */
	public int crearCUGrupoAplicacion(
			CondicionUrbanisticaSimplificadaDTO cuSimplificada);

	/**
	 * Verifica si una entidad tiene asignado un grupo de aplicación.
	 * 
	 * @param idEntidad ID de la entidad a verificar.
	 * @return resultado de la verificación.
	 */
	public boolean tieneAsignadaEntidadGrupoAplicacion(int idEntidad);
	
	/**
	 * Obtiene el identificador de la determinación asociada a un grupo de 
	 * aplicación en base a un trámite.
	 * 
	 * @param idTramite ID del trámite el cual tiene asociado el frupo de aplicación.
	 * @return ID de la determinación. (0 en caso de que no tenga asociada ninguna determinación).
	 */
	public int obtenerIdDeterminacionGrupoAplicacionDeTramite(int idTramite);
	
	/**
	 * Elimina un regimen de BD.
	 * 
	 * @param idRegimen ID del regimen a eliminar.
	 * @return resultado de la operacion: true en el caso en que la operación se haya realizado 
	 * de forma correcta, false en caso contrario.
	 */
	public boolean borrarRegimen(int idRegimen);
	
	
	/**
	 * Obtiene el numero de entidades las cuales no tienen asociado ningun grupo de aplicación.
	 * 
	 * @param idTramite ID del tramite asociado a los grupos de aplicación.
	 * @return cantidad de entidad en formato Integer.
	 */
	public int entidadesSinGrupoAplicacion (int idTramite);
	
	/**
     * Funcion que generará automaticamente los grupos de aplicacion para las entidades del tramite que no lo tengan asignado
     * 
     * Devuelve:
     * 0 Si no ha habido ningun error
     * 1 error: no hay entidades para ese tramite
     * 2 error: no especificado
     * 3 error: No se ha encontrado clave de busqueda para la entidad
     * 4 error: No se ha encontrado el valor de referencia de la clave de busqueda y del tramite
     * 
     * @param idTramite
     * @return numero de grupos de aplicación generados.
     */
	public int generarAutomaticamenteGrupoAplicacionParaEntidadesNuevoconPlanBase (int idTramite, int idTramiteBase);
	
	/**
	 * Devuelve un listado filtrado de condiciones urbanísticas referente a la busqueda avanzada.
	 * 
	 * @param filtros objetos con los filtros los cuales se van a aplicar a la lista.
	 * @param idTramite ID del trámite actual de trabajo.
	 * @return lista filtrada de objetos en formato CondicionUrbanisticaSimplificadaDTO.
	 */
	public List<CondicionUrbanisticaSimplificadaDTO> resultadosBusquedaAvanzadaCondicion(FiltrosDTO filtros, int idTramite);
	
	/**
	 * Funcion que devuelve un listado de las entidades de un tramite que se pasa como parametro que estan aplicadas al codigoGrupo que
	 * se pasa como parametro.
	 * 
	 * @param idTramite
	 * @param codigoGrupo
	 * @return
	 */
	public List<Integer> entidadesDeTramiteAplicadasAUnGrupo (int idTramite, String codigoGrupo);
	

}

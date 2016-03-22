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

package com.mitc.redes.editorfip.servicios.basicos.fip.operaciones;

import java.util.List;

import javax.ejb.Local;

import com.mitc.redes.editorfip.entidades.busqueda.BusquedaOperacionDTO;
import com.mitc.redes.editorfip.entidades.busqueda.FiltrosDTO;
import com.mitc.redes.editorfip.entidades.interfazgrafico.OperacionDeterminacionDTO;
import com.mitc.redes.editorfip.entidades.interfazgrafico.OperacionEntidadDTO;
import com.mitc.redes.editorfip.entidades.interfazgrafico.OperacionesDTO;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Operacion;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Operaciondeterminacion;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Operacionentidad;

/**
 * Servicio basico de objetos de tipo Operación.
 * 
 * @author fguerrero
 *
 */
@Local
public interface ServicioBasicoOperaciones {

	
	// ------------------------------
    // OPERACIONES
    // ------------------------------
	
	/**
	 * Inserta un nuevo registro de tipo Operacion.
	 * 
	 * @param operacion objeto con los datos de la nueva operacion.
	 * 
	 */
	public void createOperacion(Operacion operacion);
	
	/**
	 * Actualiza los datos de una operacion ya existente en BD.
	 * 
	 * @param operacion datos a actualizar.
	 */
	public void editOperacion(Operacion operacion);
	
	/**
	 * Elimina una operacion de BD.
	 * 
	 * @param operacion operacion a eliminar.
	 */
	public void removeOperacion(Operacion operacion);
	
	/**
	 * Busca y devuelve una operacion en base a su ID.
	 * 
	 * @param id ID de la operacion a buscar.
	 * @return objeto de tipo Operacion encontrado.
	 */
	public Operacion findOperacion(Object id);
	
	/**
	 * Busca y devuelve una operacion en base a su trámite y su Orden.
	 * 
	 * @param orden orden de la operacion a buscar.
	 * @param idTramite ID del tramite asociado a la operacion a buscar.
	 * @return objeto de tipo Operacion encontrado.
	 */
	public Operacion findOperacionByOrden(int orden, int idTramite);
	
	/**
	 * Busca el orden maximo de la lista de entidades asociadas a un trámite.
	 * 
	 * @param idTramite ID del tramite asociado a las entidades.
	 * @return orden maximo en formato Integer.
	 */
	public int findOrdenMaximo(int idTramite);
	
	/**
	 * Devuelve la lista completa de todas las operaciones existentes en el 
	 * sistema.
	 * 
	 * @return lista de objetos de tipo Operacion.
	 */
	public List<Operacion> findAllOperacion();
	
	/**
	 * Devuelve la lista completa de operaciones asociadas a un tramite.
	 * 
	 * @param idTramite ID del trámite asociado a las operaciones.
	 * @return lista de operaciones en formato OperacionesDTO.
	 */
	public List<OperacionesDTO> obtenerListaOperacionesDTO(int idTramite);
	
	// ------------------------------
    // OPERACIONES ENTIDAD
    // ------------------------------
	
	/**
	 * Inserta un nuevo registro de tipo Operacionentidad.
	 * 
	 * @param operacionentidad objeto con los datos de la nueva Operacion-entidad.
	 * 
	 */
	public void createOperacionentidad(Operacionentidad operacionentidad);
	
	/**
	 * Actualiza los datos de una operacion-entidad ya existente en BD.
	 * 
	 * @param operacionentidad datos a actualizar.
	 */
	public void editOperacionentidad(Operacionentidad operacionentidad);
	
	/**
	 * Elimina una operacion-entidad de BD.
	 * 
	 * @param operacionentidad operacion-entidad a eliminar.
	 */
	public void removeOperacionentidad(Operacionentidad operacionentidad);
	
	/**
	 * Busca y devuelve una operacion-entidad en base a su ID.
	 * 
	 * @param id ID de la operacion-entidad a buscar.
	 * @return objeto de tipo Operacionentidad encontrado.
	 */
	public Operacionentidad findOperacionentidad(Object id);
	
	/**
	 * Devuelve la lista completa de todas las operaciones-entidad existentes en el 
	 * sistema.
	 * 
	 * @return lista de objetos de tipo Operacionentidad.
	 */
	public List<Operacionentidad> findAllOperacionentidad();
    
	/**
	 * Devuelve la lista completa de operaciones-entidad asociadas a un tramite.
	 * 
	 * @param idTramite ID del trámite asociado a las operaciones.
	 * @return lista de operaciones en formato Operacionentidad.
	 */
	public List<Operacionentidad> obtenerListaOperacionEntidad(int idTramite);
	
	/**
	 * Devuelve la lista completa de operaciones-entidad asociadas a un tramite.
	 * 
	 * @param idTramite ID del trámite asociado a las operaciones.
	 * @return lista de operaciones en formato OperacionEntidadDTO.
	 */
	public List<OperacionEntidadDTO> obtenerListaOperacionEntidadDTO(int idTramite);
	
	/**
	 * Busca y devuelve el tipo de una operacion-entidad.
	 * 
	 * @param id ID de la operacion-entidad.
	 * @return tipo en formato String.
	 */
	public String tipoOperacionEntidadNombre(int id);
	
	/**
	 * Busca y devuelve una operacion-entidad en base a su ID.
	 * 
	 * @param id ID de la operacion a buscar.
	 * @return objeto de tipo OperacionEntidadDTO encontrado.
	 */
	public OperacionEntidadDTO obtenerOperacionEntidadDTO(String idOpEnt);
	
	/**
	 * Devuelve una lista de objetos referente a la lista de tipos de 
	 * operación.
	 * 
	 * @return lista de tipos de operación en formato Object[].
	 */
	public List<Object[]> tipoOperacionEntidad();
	
	/**
	 * Busca un tipo de operación por su nombre y devuelve su ID.
	 * 
	 * @param tipoOperacionEntNombre
	 * @return ID del tipo de operación encontrado.
	 */
	public int tipoOperacionEntId(String tipoOperacionEntNombre);
	
	
	// ------------------------------
    // OPERACIONES DETERMINACION
    // ------------------------------
	
	/**
	 * Inserta un nuevo registro de tipo Operaciondeterminacion.
	 * 
	 * @param operaciondeterminacion objeto con los datos de la nueva Operacion-determinacion.
	 * 
	 */
	public void createOperaciondeterminacion(Operaciondeterminacion operaciondeterminacion);
	
	/**
	 * Actualiza los datos de una operacion-determinacion ya existente en BD.
	 * 
	 * @param operaciondeterminacion datos a actualizar.
	 */
	public void editOperaciondeterminacion(Operaciondeterminacion operaciondeterminacion);
	
	/**
	 * Elimina una operacion-determinacion de BD.
	 * 
	 * @param operaciondeterminacion operacion-determinacion a eliminar.
	 */
	public void removeOperaciondeterminacion(Operaciondeterminacion operaciondeterminacion);
	
	/**
	 * Busca y devuelve una operacion-determinacion en base a su ID.
	 * 
	 * @param id ID de la operacion-determinacion a buscar.
	 * @return objeto de tipo Operaciondeterminacion encontrado.
	 */
	public Operaciondeterminacion findOperaciondeterminacion(Object id);
	
	/**
	 * Devuelve la lista completa de todas las operaciones-determinacion existentes en el 
	 * sistema.
	 * 
	 * @return lista de objetos de tipo Operaciondeterminacion.
	 */
	public List<Operaciondeterminacion> findAllOperaciondeterminacion();
	
	/**
	 * Devuelve la lista completa de operaciones-determinacion asociadas a un tramite.
	 * 
	 * @param idTramite ID del trámite asociado a las operaciones.
	 * @return lista de operaciones en formato Operaciondeterminacion.
	 */
	public List<Operaciondeterminacion> obtenerListaOperacionDeterminacion(int idTramite);
		
	/**
	 * Busca y devuelve el tipo de una operacion-determinacion.
	 * 
	 * @param id ID de la operacion-determinacion.
	 * @return tipo en formato String.
	 */
	public String tipoOperacionDetNombre(int idTipoOperacionDet);
		
	/**
	 * Busca un tipo de operación por su nombre y devuelve su ID.
	 * 
	 * @param tipoOperacionEntNombre
	 * @return ID del tipo de operación encontrado.
	 */
	public int tipoOperacionDetId(String tipoOperacionDetNombre);
		
	/**
	 * Devuelve la lista completa de operaciones-determinacion asociadas a un tramite.
	 * 
	 * @param idTramite ID del trámite asociado a las operaciones.
	 * @return lista de operaciones en formato OperacionDeterminacionDTO.
	 */
	public List<OperacionDeterminacionDTO> obtenerListaOperacionDeterminacionDTO(int idTramite);
		
	/**
	 * Busca y devuelve una operacion-determinacion en base a su ID.
	 * 
	 * @param id ID de la operacion a buscar.
	 * @return objeto de tipo OperacionDeterminacionDTO encontrado.
	 */
	public OperacionDeterminacionDTO obtenerOperacionDeterminacionDTO(String idOpDet);
	
	/**
	 * Devuelve una lista de objetos referente a la lista de tipos de 
	 * operación.
	 * 
	 * @return lista de tipos de operación en formato Object[].
	 */
	public List<Object[]> tipoOperacionDeterminacion();
	
	/**
	 * Realiza una busqueda filtrada y devuelve los resultados en formato de lista.
	 * 
	 * @param filtros objeto con los filtros a aplicar sobre la lista
	 * @param idTramite ID del trámite actual de trabajo.
	 * @return lista con objetos de tipo BusquedaOperacionDTO.
	 */
	public List<BusquedaOperacionDTO> resultadosBusquedaAvanzadaOperacion(FiltrosDTO filtros, int idTramite);
	
	

}

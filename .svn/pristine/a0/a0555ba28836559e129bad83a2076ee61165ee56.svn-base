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

import com.mitc.redes.editorfip.entidades.busqueda.BusquedaEntidadDTO;
import com.mitc.redes.editorfip.entidades.busqueda.FiltrosDTO;
import com.mitc.redes.editorfip.entidades.comunes.ParIdentificadorTexto;
import com.mitc.redes.editorfip.entidades.interfazgrafico.EntidadDTO;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Determinacion;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Entidad;

/**
 * Servicio con operaciones basicas sobre entidades.
 * 
 * @author fguerrero
 *
 */
@Local
public interface ServicioBasicoEntidades {

	
	// ------------------------------
    // GENERICAS
    // ------------------------------
	
	/**
	 * Devuelve el objeto de tipo Entidad asociado al ID indicado.
	 * 
	 * @param id ID de la entidad.
	 * @return  Entidad asociada al ID.
	 * 
	 */
	public Entidad buscarEntidad(Object id);
	
	/**
	 * Obtiene el orden que se asignará a una nueva entidad.
	 * 
	 * @param idTramite ID del trámite actual de trabajo.
	 * @param idEntPadre ID de la entidad padre, si procede.
	 * @return orden en formato Integer.
	 */
	public int obtenerOrdenNuevaEntidad(int idTramite, int idEntPadre);
	
	/**
	 * Obtiene el codigo que se asignará a una nueva entidad.
	 * 
	 * @param idTramite ID del trámite actual de trabajo.
	 * @param idEntPadre ID de la entidad padre, si procede.
	 * @return codigo en formato String.
	 */
	public String obtenerCodigoNuevaEntidad(int idTramite, int idEntPadre);
	
	/**
	 * Devuelve el identificador de una entidad en base al trámite y la 
	 * clave de la misma. 
	 * 
	 * @param idTramite ID del trámite actual de trabajo.
	 * @param clave
	 * @return
	 */
	public int buscarEntidadPorTramiteYClave(int idTramite, String clave);
	
	// ------------------------------
    // ARBOL ENTIDADES
    // ------------------------------
	
	/**
	 * Obtiene la lista de entidades raices del arbol de selección de entidades.
	 * 
	 * @param idTramite ID del trámite actual de trabajo.
	 * @return lista de entidades raices en formato ParIdentificadorTexto.
	 */
	public List<ParIdentificadorTexto> getEntidadesRaices(int idTramite);
	
	/**
	 * Obtiene la lista de entidades hijas en base a una entidad padre.
	 * 
	 * @param idPadre ID de la entidad padre.
	 * @return lista de entidades hijas en formato ParIdentificadorTexto.
	 */
	public List<ParIdentificadorTexto> getEntidadesHijas(int idPadre);

	/**
	 * Obtiene la lista de entidades hijas asociadas a sus respectivas 
	 * condiciones urbanísticas en base a una entidad padre.
	 * 
	 * @param idPadre ID de la entidad padre.
	 * @return lista de entidades hijas en formato ParIdentificadorTexto.
	 */
	public List<ParIdentificadorTexto> getEntidadesCUHijas(int idPadre);

	/**
	 * Obtiene la lista de entidades raices asociadas a sus respectivas 
	 * condiciones urbanísticas.
	 * 
	 * @param idTramite ID del trámite actual de trabajo.
	 * @return lista de entidades raices en formato ParIdentificadorTexto.
	 */
	public List<ParIdentificadorTexto> getEntidadesCURaices(int idTramite);
	
	// ------------------------------
    // GENERICAS
    // ------------------------------
	
	/**
	 * Verifica si un trámite tiene asignada alguna entidad.
	 * 
	 * @param idTramite ID del trámite actual de trabajo.
	 * @return true si existe alguna entidad asociada al trámite, false en caso contrario.
	 */
	public boolean tieneEntidadTramite(int idTramite);
	
	/**
	 * Obtiene una entidad equivalente en base a su nombre y al trámite base.
	 * 
	 * @param nombre nombre de la entidad.
	 * @param idTramiteBase ID del tramite base.
	 * @return objeto de tipo Entidad encontrado.
	 */
	public Entidad obtenerEquivalente(String nombre, int idTramiteBase);
	
	/**
	 * Elimina todas las entidad asociadas a un trámite.
	 * 
	 * @param idTramite ID del trámite del cual se quiere eliminar las entidades.
	 */
	public void borrarEntidadesTramite(int idTramite);
	
	/**
	 * Obtiene el codigo que será asignado a una nueva entidad.
	 * 
	 * @param idBDTramite ID del trámite actual de trabajo.
	 * @return codigo en formato String.
	 */
	public String nextCodigo(String idBDTramite);
	
	// ------------------------------
    // BUSQUEDA
    // ------------------------------
	
	/**
	 * Realiza una busqueda avanzada de entidades en base a filtros predefinidos.
	 * 
	 * @param filtros objeto con los datos de filtro de la lista.
	 * @param idTramite ID del trámite actual de trabajo.
	 * @return lista filtrada de entidades en formato BusquedaEntidadDTO
	 */
	public List<BusquedaEntidadDTO> resultadosBusquedaAvanzadaTramite(FiltrosDTO filtros, int idTramite);
	
	// ------------------------------
    // GESTION DOCUMENTAL
    // ------------------------------

	/**
     * Método que devuelve las determinaciones asociadas a un documento
     * @param idDoc
     * @return listado de determinacionDTO
     */     
    public List<EntidadDTO> obtenerEntidadesDocumento(int idDoc);
    
    /**
     * Elimina la relación existente entre una entidad y un documento.
     * 
     * @param idEntidad ID de la entidad de la cual se desea borrar la relación.
     * @param idDoc ID del documento del cual se desea borrar la relación.
     */
    public void borrarRelacionEntidadConDocumento(int idEntidad, int idDoc);
    
    public Entidad equivalenciaEntidadPlanBase(String equivalencia, int idTramiteBase);
    
    public int obtenerIdGrupoAplicacionPlanBase(String etiqueta, int idTramiteBase);

    public Entidad obtenerEntidadPorTramiteYClave(int idTramite, String clave);
}

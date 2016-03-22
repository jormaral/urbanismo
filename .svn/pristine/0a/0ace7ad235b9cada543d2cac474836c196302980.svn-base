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

package com.mitc.redes.editorfip.servicios.basicos.fip.documentos;

import java.util.List;

import javax.ejb.Local;
import javax.faces.model.SelectItem;

import com.mitc.redes.editorfip.entidades.busqueda.BusquedaDocumentoDTO;
import com.mitc.redes.editorfip.entidades.busqueda.FiltrosDTO;
import com.mitc.redes.editorfip.entidades.interfazgrafico.DocumentoDTO;

/**
 * Servicio con las operaciones basicas a realizar sobre documentos.
 * 
 * @author fguerrero
 *
 */
@Local
public interface ServicioBasicoDocumentos {
	
	/**
	 * Obtiene la lista completa de grupos de documentos.
	 * 
	 * @return lista con objetos de tipo SelectItem.
	 */
	public List<SelectItem> grupoDocumentoList();
	
	/**
	 * Obtiene la lista completa de tipos de documentos.
	 * 
	 * @return lista con objetos de tipo SelectItem.
	 */
	public List<SelectItem> tiposDocumentoList();
	
	/**
	 * Obtiene la lista completa de grupos de documentos para un nuevo registro.
	 * 
	 * @return lista con objetos de tipo SelectItem.
	 */
	public List<SelectItem> grupoDocumentoAltaList();
	
	/**
	 * Obtiene la lista completa de grupos de documentos para un nuevo registro.
	 * 
	 * @return lista con objetos de tipo SelectItem.
	 */
	public List<SelectItem> tiposDocumentoAltaList();
	
	/**
	 * Obtiene la lista de documentos resultante de una busqueda filtrada.
	 * 
	 * @param filtros objeto en el cual se encuentran los detalles de la busqueda.
	 * @param idTramite ID del trámite actual de trabajo.
	 * @return lista filtrada de documentos.
	 */
	public List<BusquedaDocumentoDTO> resultadosBusquedaAvanzadaDocumento(FiltrosDTO filtros, int idTramite);
	
	/**
	 * 
	 * 
	 * @param idTramite
	 * @return
	 */
	public List<DocumentoDTO> obtenerListaDocumentoDTO(int idTramite);
	
	/**
	 * Crea un objeto del tipo Documentodeterminacion para relacionar una determinacion y un documento
	 * @param idDet identificador de la determinacion
	 * @param idDoc identificador del documento
	 */
	public void crearDocumentoDeterminacion(int idDet, int idDoc);
	
	/**
	 * Crea un objeto del tipo Documentoentidad para relacionar una entidad y un documento
	 * @param idEnt identificador de la entidad
	 * @param idDoc identificador del documento
	 */
	public void crearDocumentoEntidad(int idEnt, int idDoc);
	
	/**
	 * Obtiene una lista de documentos 'hojas' en base a un documento padre.
	 * 
	 * @param idDoc ID del documento padre.
	 * @return lista de documentos 'hojas'
	 */
	public List<DocumentoDTO> obtenerListaDocumentoHoja(int idDoc);
}
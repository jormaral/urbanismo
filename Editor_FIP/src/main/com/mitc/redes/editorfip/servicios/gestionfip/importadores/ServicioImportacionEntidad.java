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

package com.mitc.redes.editorfip.servicios.gestionfip.importadores;

import java.util.EventObject;
import java.util.List;

import javax.ejb.Local;
import javax.ejb.Remove;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import com.icesoft.faces.component.inputfile.FileInfo;

/**
 * Servicio que gestiona las operaciones referentes a la importación de 
 * entidades.
 * 
 * @author fguerrero
 *
 */
@Local
public interface ServicioImportacionEntidad 
{
	@Remove
	public void destroy();
	
	/**
	 * Método(evento) que se encarga de subir archivos fisicos al servidor.
	 * 
	 * @param actionEvent parametro obligatorio para lanzar el evento.
	 */
	public void uploadActionListener(ActionEvent actionEvent);
	
	/**
	 * Método(evento) que se encarga de visualizar el progreso actual de 
	 * la subida de un fichero.
	 * 
	 * @param event parametro obligatorio para lanzar el evento.
	 */
	public void progressListener(EventObject event);
	/**
	 * Obtiene el valor de la propiedad 'currentFile'
	 * 
	 * @return valor de tipo FileInfo
	 */
	public FileInfo getCurrentFile();
	
	/**
     * Obtiene el valor de la propiedad 'fileProgress'
     * 
     * @return valor de tipo int
     */
    public int getFileProgress();
    
    /**
     * Obtiene el valor de la propiedad 'sheetListItems'
     * 
     * @return valor de tipo List(SelectItem)
     */
    public List<SelectItem> getSheetListItems();
	/**
	 * Modifica el valor de la propiedad '@param sheetListItems'
	 * 
	 * @param sheetListItems valor de tipo List(SelectItem)
	 */
	public void setSheetListItems(List<SelectItem> sheetListItems);
	
	/**
	 * Obtiene el valor de la propiedad 'selectedSheet'
	 * 
	 * @return valor de tipo int[]
	 */
	public int[] getSelectedSheet();
	/**
	 * Modifica el valor de la propiedad 'selectedSheet'
	 * 
	 * @param selectedSheet valor de tipo 
	 */
	public void setSelectedSheet(int[] selectedSheet);
	
	/**
	 * Método que se encarga de importar entidades apartir
	 * un fichero en formato EXCEL.
	 * 
	 */
	public void importarENExcel();
	
	/**
	 * Obtiene el valor de la propiedad 'listadoErroresImportacion'
	 * 
	 * @return valor de tipo List<String>
	 */
	public List<String> getListadoErroresImportacion();
}
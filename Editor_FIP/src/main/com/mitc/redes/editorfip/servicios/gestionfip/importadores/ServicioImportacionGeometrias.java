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
import javax.faces.model.DataModel;
import javax.faces.model.SelectItem;

import com.icesoft.faces.component.inputfile.FileInfo;
import com.mitc.redes.editorfip.entidades.interfazgrafico.DocumentoDTO;
import com.mitc.redes.editorfip.entidades.interfazgrafico.GeometriaDTO;

/**
 * Servicio que gestiona las operaciones referentes a la importación de 
 * geometrías.
 * 
 * @author fguerrero
 *
 */
@Local
public interface ServicioImportacionGeometrias 
{
	@Remove
public void destroy();
	
	/**
	 * Mètodo(evento) que se encarga de subir archivos fisicos al servidor.
	 * 
	 * @param actionEvent parametro obligatorio para lanzar el evento.
	 */
	public void uploadActionListener(ActionEvent actionEvent);
	
	public void uploadActionListenerSHP(ActionEvent actionEvent);
	public void uploadActionListenerSHX(ActionEvent actionEvent);
	public void uploadActionListenerDBF(ActionEvent actionEvent);
	
	/**
	 * Mètodo(evento) que se encarga de visualizar el progreso actual de 
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
     * Método encargado de la importación de geometrías.
     * 
     */
    public void importarGeometria ();
	
    /**
     * Método encargado de la importación de geometrías a una entidad.
     * 
     */
    public void importarGeometriaToEntidad ();
    
	/**
	 * Obtiene el valor de la propiedad 'listaErroresImportación'
	 * 
	 * @return valor de tipo List<String>
	 */
	public List<String> getListadoErroresImportacion();
	
	/**
	 * Obtiene el valor de la propiedad 'geometriaLista'
	 * 
	 * @return valor de tipo List<GeometriaDTO>
	 */
	public List<GeometriaDTO> getGeometriaLista();
	
	/**
	 * Obtiene el valor de la propiedad que indica si es 
	 * posible realizar la importación.
	 * 
	 */
	public boolean isPuedeImportar();
	
	/**
	 * Obtiene el valor de la propiedad 'archivosTemporales'
	 * 
	 * @return valor de tipo List<DocumentoDTO>
	 */
	public List<DocumentoDTO> getArchivosTemporales();
	
	/**
	 * Obtiene el valor de la propiedad que indica si los archivos
	 * subidos son validos.
	 * 
	 */
	public boolean archivosValidos();
	
	/**
	 * Metodo que se encarga de reiniciar los elementos.
	 * 
	 */
	public void reiniciarElementos();
	
	/**
	 * Obtiene el valor de la propiedad 'nombreFichero'
	 * 
	 * @return valor de tipo String
	 */
	public String getNombreFichero();
	
	public void setGeometriaLista(List<GeometriaDTO> geometriaLista);
	
	public void setArchivosTemporales(List<DocumentoDTO> archivosTemporales);
	
	
}
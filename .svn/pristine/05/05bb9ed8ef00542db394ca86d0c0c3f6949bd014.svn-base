package com.mitc.redes.editorfip.servicios.ayuda.faq;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Remove;
import javax.faces.event.ActionEvent;

import org.jboss.seam.faces.FacesManager;

import com.icesoft.faces.component.InputTextTag;
import com.icesoft.faces.component.PanelGridTag;
import com.icesoft.faces.component.ext.HtmlInputText;
import com.icesoft.faces.component.inputfile.FileInfo;
import com.icesoft.faces.component.inputfile.InputFile;
import com.icesoft.faces.context.Resource;
import com.mitc.redes.editorfip.entidades.rpm.ayuda.Manual;

@Local
public interface GestionManuales {
	
	/**
	 * Devuelve el listado completos de manuales en el sistema.
	 * 
	 * @return lista con objetos de tipo Manual.
	 */
	public List<Manual> getResultado();
	
	/**
	 * Metodo que obtiene un objeto de tipo Resource que contiene 
	 * todos los datos (incluido el fichero) de un documento guardado
	 * en el sistema, en base a su nombre.
	 * 
	 * @param nombre
	 * @return
	 */
	public Resource obtenerFichero(String nombre);
	
	/**
	 * Obtiene el objeto del tipo Manual sobre el que se estan 
	 * realizando los cambios.
	 * 
	 * @return nuevo objeto de tipo Manual.
	 */
	public Manual getManualNuevo();
	
	/**
	 * Modifica el objeto del tipo Manual sobre el que se estan 
	 * realizando los cambios.
	 * 
	 * @param manualNuevo objeto nuevo.
	 */
	public void setManualNuevo(Manual manualNuevo);
	
	/**
	 * Modifica la propiedad 'Binding' del campo de texto en pantalla
	 * que muestra el nombre del fichero subido.
	 * 
	 * @param nombreFicheroBinding propiedad de tipo HtmlInputText
	 */
	public void setNombreFicheroBinding(HtmlInputText nombreFicheroBinding);
	
	/**
	 * Obtiene la propiedad 'Binding' del campo de texto en pantalla
	 * que muestra el nombre del fichero subido.
	 * 
	 * @return propiedad de tipo HtmlInputText
	 */
	public HtmlInputText getNombreFicheroBinding();
	
	/**
	 * Metodo que adjunta un fichero en formato InputFile al actual manual en
	 * gestión. No refleja los cambios en DB hasta que no se pulse el boton guardar.
	 * 
	 * @param inputFile fichero a adjuntar.
	 */
	public void adjuntarArchivo(InputFile inputFile);
	
	/**
	 * Crea un manual nuevo con los datos introducidos y guarda el fichero 
	 * en su correspondiente ruta asociandolo al manual mediante su nombre.
	 */
	public void guardarDocumento();
	
	/**
	 * Cancela los cambios y redirige a la pagina con la lista de manuales.
	 * 
	 */
	public void cancelarSubida();
	
	/**
	 * Borra un manual de la lista y refresca la pagina.
	 * 
	 * @param idManual ID del manual a borrar.
	 */
	public void eliminarManual(long idManual);
	
	@Remove
	public void destroy();

}

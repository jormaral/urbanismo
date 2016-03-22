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

package com.mitc.redes.editorfip.servicios.gestionfip.gestiondocumental;

import java.io.File;
import java.util.EventObject;
import java.util.List;

import javax.ejb.Local;
import javax.ejb.Remove;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import com.icesoft.faces.component.inputfile.FileInfo;
import com.icesoft.faces.context.Resource;
import com.mitc.redes.editorfip.entidades.interfazgrafico.DocumentoDTO;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Documento;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Documentoshp;

/**
 * Servicio que gestiona todas las operaciones referentes al sistema 
 * documental.
 * 
 * @author fguerrero
 *
 */
@Local
public interface ServicioGestionDocumental{
		
	@Remove
	public void remove();
	
	/**
	 * Obtiene la lista con los tipos de documentos permitidos.
	 * 
	 * @return lista de documentos en formato SelectItem
	 */
	public List<SelectItem> getListaTipoDocumento();
	
	/**
	 * Modifica la lista con los tipos de documentos permitidos.
	 * 
	 * @return lista de documentos en formato SelectItem
	 */
	public List<SelectItem> getListaGrupoDocumento();
	
	/**
	 * Elimina un documento de BD.
	 * 
	 * @param identificador ID del documento a eliminar.
	 */
	public void borrar(int identificador);
	
	/**
	 * Obtiene el documento con el que se esta trabajando.
	 * 
	 * @return objeto de tipo Documento.
	 */
	public Documento getDocumento();
	
	/**
	 * Modifica el documento con el que se esta trabajando.
	 * 
	 * @return objeto de tipo Documento.
	 */
	public void setDocumento(Documento documento);
	
	/**
	 * Obtiene la informacion del fichero con el que se esta
	 * trabajando.
	 * 
	 * @return objeto de tipo FileInfo.
	 */
	public FileInfo getCurrentFile();
	
	/**
	 * Método (ActioListener) que se lanza al pulsar el boton 
	 * de "Subir" del componente 'ice:inputFile'
	 * 
	 * @param actionEvent parametro obligatorio para poder lanzar el evento.
	 */
	public void uploadActionListener(ActionEvent actionEvent);
	
	/**
	 * Parametro utilizado por el componente 'ice:fileProgess'.
	 * 
	 * @return numero que indica el avanze de la barra de progreso.
	 */
	public int getFileProgress();
	
	/**
	 * Método (ActionListener) que se lanza al subir un fichero, para 
	 * activar la barra de progeso de subida.
	 * 
	 * @param event parametro obligatorio para poder lanzar el evento.
	 */
	public void progressListener(EventObject event);
	
	/**
	 * Obtiene el fichero subido el cual se adjunta al objeto de tipo
	 * documento.
	 * 
	 * @return objeto de tipo File.
	 */
	public File getUploadedFile();
	
	/**
	 * Modifica el fichero subido el cual se adjunta al objeto de tipo
	 * documento.
	 * 
	 * @param uploadedFile objeto de tipo File en el cual se encuentran 
	 * los detalles del fichero subido.
	 */
	public void setUploadedFile(File uploadedFile);
	
	/**
	 * Modifica la informacion del fichero con el que se esta
	 * trabajando.
	 * 
	 * @param currentFile objeto de tipo FileInfo.
	 */
	public void setCurrentFile(FileInfo currentFile);
	
	/**
	 * Obtiene el valor del parametero que indica si se muestra 
	 * o no el panel de asociación de determinaciones.
	 * 
	 * @return parametro de tipo Boolean (true o false).
	 */
	public boolean isMostrarPlanelDeterminaciones();
	
	/**
	 * Modifica el valor del parametero que indica si se muestra 
	 * o no el panel de asociación de determinaciones.
	 * 
	 * @param mostrarPlanelDeterminaciones parametro de tipo Boolean (true o false).
	 */
	public void setMostrarPlanelDeterminaciones(boolean mostrarPlanelDeterminaciones);
	
	/**
	 *  Obtiene el valor del parametero que indica si se muestra 
	 * o no el panel de asociación de entidades.
	 * 
	 * @return parametro de tipo Boolean (true o false).
	 */
	public boolean isMostrarPlanelEntidades();
	
	/**
	 * Modifica el valor del parametero que indica si se muestra 
	 * o no el panel de asociación de entidades.
	 * 
	 * @param mostrarPlanelEntidades parametro de tipo Boolean (true o false).
	 */
	public void setMostrarPlanelEntidades(boolean mostrarPlanelEntidades);
	
	/**
	 * Carga el documento con el que se va a trabajar mediante su 
	 * identificador en formato String.
	 * 
	 * @param documentoIden Identificador del documento en formato String
	 */
	public void cargarDocumento(String documentoIden);
	
	/**
	 * Obtiene el valor del parametero que indica si los datos 
	 * del documento se han cargado correctamente.
	 * 
	 * @return parametro de tipo Boolean (true o false).
	 */
	public boolean isDatosCargados();
	
	/**
	 * Modifica el valor del parametero que indica si los datos 
	 * del documento se han cargado correctamente.
	 * 
	 * @param datosCargados parametro de tipo Boolean (true o false).
	 */
	public void setDatosCargados(boolean datosCargados);
	
	/**
	 * Método que valida los datos del formulario y renderiza el boton
	 * que permite adjuntar archivos al documento.
	 * 
	 */
	public void adjuntarFichero();
	
	/**
	 * Obtiene el ID del documento
	 * 
	 * @return ID en formato Integer.
	 */
	public Integer getIdDocumento();
	
	/**
	 * Modifica el ID del documento.
	 * 
	 * @param idDocumento ID en formato Integer
	 */
	public void setIdDocumento(Integer idDocumento);
	
	/**
	 * Obtiene el ID de la determinación que se asociará al 
	 * dcoumento.
	 * 
	 * @return ID de la determinación en formato Integer.
	 */
	public int getIdDeterminacion();
	
	/**
	 * Obtiene el ID de la determinación que se asociará al 
	 * dcoumento.
	 * 
	 * @param idDeterminacion ID de la determinación en formato Integer.
	 */
	public void setIdDeterminacion(int idDeterminacion);
	
	/**
	 * Crea una instancia de Documentodeterminacion para relacionar un documento con una determinacion
	 * @param idDet identificador de la determinación a relacionar
	 * @param idDoc identificador del documento a relacionar
	 */
	public void crearRelacionDocumentoDeterminacion(int idDet, int idDoc);
	
	/**
	 * Crea una instancia de Documentoentidad para relacionar un documento con una determinacion
	 * @param idDet identificador de la determinación a relacionar
	 * @param idDoc identificador del documento a relacionar
	 */
	public void crearRelacionDocumentoEntidad(int idEnt, int idDoc);
	
	/**
	 * Borra un archivo de disco del documento tipo carpeta
	 * @param archivo nombre del archivo a borrar
	 */
	public void borrarArchivo(String archivo);
	
	/**
	 * Elimina una determinación asociada al documento.
	 * 
	 * @param idDeterminacion id de la determinación a eliminar.
	 */
	public void borrarDeterminacionDocumento(int idDeterminacion);
	
	/**
	 * Elimina una entidad asociada al documento.
	 * 
	 * @param idDeterminacion id de la entidad.
	 */
	public void borrarEntidadDocumento(int idDeterminacion);
	
	/**
	 * devuelve la lista de archivos de documento tipo carpeta
	 * @return
	 */
	public List<DocumentoDTO> getArchivosDocumento();
	
	/**
	 * Borra un archivo fisico guardado en disco.
	 * 
	 * @param archivo nombre del archivo
	 */
	public void borrarArchivoDisco(String archivo);
	
	/**
	 * Obtiene el valor de la propiedad 'mostrarPanelHoja'.
	 * 
	 * @return parametro de tipo Boolean (true o false).
	 */
	public boolean isMostrarPlanelHoja();
	
	/**
	 * Obtiene el valor de la propiedad 'documentoshp'.
	 * 
	 * @return parametro de tipo Documentoshp
	 * 
	 * @return
	 */
	public Documentoshp getDocumentoshp();
	
	/**
	 * Obtiene los archivos temporales de una hoja.
	 * 
	 * @return lista de objetos tipo DocumnentoDTO
	 */
	public List<DocumentoDTO> getArchivosTemporalesHoja();
	
	/**
	 * Método(evento) que se lanzar al subir un fichero-hoja.
	 * 
	 * @param actionEvent parametro obligatorio para lanzar el evento.
	 */
	public void uploadFilehoja(ActionEvent actionEvent);
	
	/**
	 * Obtiene el valor del parametero que indica si los datos de una
	 * hoja estan cargados.
	 * 
	 * @return parametro de tipo Boolean (true o false).
	 * 
	 */
	public boolean isHojaCargada();
	
	/**
	 * Método que guarda una hoja en el sistema.
	 * 
	 */
	public void guardarHoja();
	
	/**
	 *  Obtiene el valor del parametero que indica si los datos de una
	 * hoja son validos.
	 * 
	 * @return parametro de tipo Boolean (true o false).
	 * 
	 */
	public boolean hojaValida();
	
	/**
	 * Obtiene una lista con las posibles extensiones de ficheros.
	 * 
	 * @return lista en de objetos tipo SelectItem.
	 */
	public List<SelectItem> getListaExtension();
	
	/**
	 * Obtiene la extension actual del fichero.
	 * 
	 * @return extension en formato String
	 */
	public String getExtensionArchivo();
	
	/**
	 * Modifica la extension actual del fichero.
	 * 
	 * @param extensionArchivo extension en formato String
	 */
	public void setExtensionArchivo(String extensionArchivo);
	
	/**
	 * Borra un archivo fisico de tipo 'hoja' guardado en disco.
	 * 
	 * @param archivo nombre del fichero.
	 */
	public void borrarArchivoHojaDisco(String archivo);
	
	/**
	 * Borra una hoja del sistema.
	 * 
	 * @param iden ID de la hoja a eliminar.
	 */
	public void borrarHoja(int iden);
	
	/**
	 * Elimina y recarga un documento.
	 * 
	 */
	public void borrarShpCargarDocumento();
	
	/**
	 * Metodo que acepta una hoja del documento.
	 * 
	 */
	public void aceptarHoja();
	
	/**
	 * Obtiene los datos de un fichero fisico subido.
	 * 
	 * @param nombre nombre del fichero.
	 * @return datos del fichero en formato Resource.
	 */
	public Resource obtenerFichero(String nombre);
	
	public Resource obtenerFicheroPlanVigente(int identificador);
	
	public String obtenerNombreFichero(int idDoc);
	
	/**
	 * carga un documento redireccionando hacia una pagina concreta.
	 * 
	 * @param documentoId ID del documento a cargar.
	 */
	public void cargarDocumentoRedireccionando(String documentoId);
	
}

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
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.EventObject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;

import org.hibernate.mapping.Array;
import org.hibernate.validator.Length;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.web.RequestParameter;
import org.jboss.seam.faces.FacesManager;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.international.StatusMessage.Severity;
import org.jboss.seam.log.Log;

import com.icesoft.faces.component.inputfile.FileInfo;
import com.icesoft.faces.component.inputfile.InputFile;
import com.icesoft.faces.context.Resource;
import com.icesoft.faces.context.Resource.Options;
import com.mitc.redes.editorfip.entidades.interfazgrafico.DocumentoDTO;
import com.mitc.redes.editorfip.entidades.rpm.gestionfip.FipsCargados;
import com.mitc.redes.editorfip.entidades.rpm.gestionfip.PlanesEncargados;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Documento;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Documentoshp;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Tramite;
import com.mitc.redes.editorfip.servicios.basicos.diccionario.ServicioBasicoAmbitos;
import com.mitc.redes.editorfip.servicios.basicos.diccionario.ServicioBasicoPlanes;
import com.mitc.redes.editorfip.servicios.basicos.fip.determinaciones.ServicioBasicoDeterminaciones;
import com.mitc.redes.editorfip.servicios.basicos.fip.documentos.ServicioBasicoDocumentos;
import com.mitc.redes.editorfip.servicios.basicos.fip.entidades.ServicioBasicoEntidades;
import com.mitc.redes.editorfip.servicios.sesion.VariablesSesionGenerales;
import com.mitc.redes.editorfip.servicios.sesion.VariablesSesionUsuario;
import com.mitc.redes.editorfip.utilidades.Constantes;
import com.mitc.redes.editorfip.utilidades.Textos;

@Scope(ScopeType.SESSION)
@Stateful
@Name("servicioGestionDocumental")
public class ServicioGestionDocumentalBean implements ServicioGestionDocumental{
	
	@In 
    EntityManager entityManager;
	
	@In(create=true)
    FacesMessages facesMessages;
	
	@Logger 
	private Log log;
	
	@In (create = true)
	ServicioBasicoAmbitos servicioBasicoAmbitos;
	
	@In (create = true)
	ServicioBasicoPlanes servicioBasicoPlanes;
	
	@In (create = true, required = false)
	ServicioBasicoDocumentos servicioBasicoDocumentos;
	
	@In (create = true, required = false)
   	VariablesSesionUsuario variablesSesionUsuario;
	
	@In (create = true, required = false)
   	VariablesSesionGenerales variablesSesionGenerales;
	
	@In(create=true)
    ServicioBasicoDeterminaciones servicioBasicoDeterminaciones;
	
	@In(create=true)
    ServicioBasicoEntidades servicioBasicoEntidades;
	
	List<DocumentoDTO> archivosDocumento;
	
	List<DocumentoDTO> archivosTemporalesHoja;
	
	@RequestParameter("idDocumento") 
	Integer idDocumento;
	
	private List<SelectItem> listaTipoDocumento;
	
	private List<SelectItem> listaExtension = new ArrayList<SelectItem>();
    
    private List<SelectItem> listaGrupoDocumento;
    
    private FileInfo currentFile;
    private File uploadedFile;
	private int fileProgress;
    
	private boolean mostrarPlanelDeterminaciones;
	private boolean mostrarPlanelEntidades;
	
	private boolean mostrarPlanelHoja;
	
    private Documento documento = new Documento();
    private Documentoshp documentoshp = new Documentoshp();
    
    public boolean datosCargados = false;
    public boolean hojaCargada = false;
    
    private int idDeterminacion;
    private String extensionArchivo;
    
    private String rutaFicheros = "";
   
	public void borrar(int identificador)
	{
		Documento documento = entityManager.find(Documento.class, identificador);
		
		// Tengo que comprobar si tiene Documentoshp, es decir, planos georeferenciados y borrarlos
		String queryDocShp = " from Documentoshp docshp where docshp.documento.iden ="+documento.getIden();
		
		List<Documentoshp> docList = (List<Documentoshp>) entityManager.createQuery(queryDocShp).getResultList();
		
		for (Documentoshp dshp : docList)
		{
			entityManager.remove(dshp);
		}
		
		entityManager.remove(documento);
		entityManager.flush();
	}
	
	
	public List<SelectItem> getListaTipoDocumento() {
		listaTipoDocumento = servicioBasicoDocumentos.tiposDocumentoAltaList();
		return listaTipoDocumento;
	}

	public void setListaTipoDocumento(List<SelectItem> listaTipoDocumento) {
		this.listaTipoDocumento = listaTipoDocumento;
	}

	public List<SelectItem> getListaGrupoDocumento() {
		listaGrupoDocumento = servicioBasicoDocumentos.grupoDocumentoAltaList();
		return listaGrupoDocumento;
	}

	public void setListaGrupoDocumento(List<SelectItem> listaGrupoDocumento) {
		this.listaGrupoDocumento = listaGrupoDocumento;
	}

	public Documento getDocumento() {
		return documento;
	}

	public void setDocumento(Documento documento) {
		this.documento = documento;
	}
	
	public FileInfo getCurrentFile() {
		return currentFile;
	}


    public int getFileProgress() {
        return fileProgress;
    }
    
    public File getUploadedFile() {
		return uploadedFile;
	}

	public void setUploadedFile(File uploadedFile) {
		this.uploadedFile = uploadedFile;
	}

	public void setCurrentFile(FileInfo currentFile) {
		this.currentFile = currentFile;
	}
	

	public boolean isDatosCargados() {
		return datosCargados;
	}

	public void setDatosCargados(boolean datosCargados) {
		this.datosCargados = datosCargados;
	}

	public void adjuntarFichero() {
		if(documento != null){
			if (documento.getIden()==0){
				
				// Comprobación de que el nombre de la carpeta es correcto
				Tramite tramite = entityManager.find(Tramite.class, variablesSesionUsuario.getIdTramiteEncargadoTrabajo());
		        documento.setTramite(tramite);
		        documento.setArchivo("");
				entityManager.persist(documento);
				entityManager.flush();
				datosCargados = true;
				
			}
			else{
				entityManager.merge(documento);
				entityManager.flush();
				datosCargados = true;
				leerArchivos();
			}
			
			facesMessages.addFromResourceBundle("documento_guardado_correctamente_anexar_ficheros", null);
			variablesSesionGenerales.setIdDocumentoSeleccionado(documento.getIden());
			//leerArchivos();
		}
	}
	
	public void guardarHoja() {
		
		// Comprobación de que el nombre de la carpeta es correcto
		
		if(Textos.validarExpresion(Constantes.EXPRESION_REGULAR_NOMBRE_CARPETA, documentoshp.getHoja())) {
			documento = entityManager.find(Documento.class, variablesSesionGenerales.getIdDocumentoSeleccionado());
			documentoshp.setDocumento(documento);
			
			documentoshp.setHoja(documentoshp.getHoja() + "." + extensionArchivo);
			
			entityManager.persist(documentoshp);
			entityManager.flush();
			hojaCargada = true;
			
			System.out.println("RUBEN TIENE RAZON");
			
			variablesSesionGenerales.setIdDocumentoShpSeleccionado(documentoshp.getIden());
			leerArchivosHoja();
		}
		else
		{
			facesMessages.addFromResourceBundle(Severity.ERROR,"El nombre del titulo debe ser alfanumético y sin espacios.", null);
		}
		
	}

	
	public void uploadActionListener(ActionEvent actionEvent) {

		log.info("Subiendo fichero ...");
		
		// Datos del fichero reciŽn subido
        InputFile inputFile = (InputFile) actionEvent.getSource();
        currentFile = inputFile.getFileInfo();
        String clientSideFileName = currentFile.getFileName();
        log.info("Fichero subido: " + clientSideFileName);
        File uploadedFile = currentFile.getFile();
		
		if(Textos.validarExpresion(Constantes.EXPRESION_REGULAR_NOMBRE_ARCHIVO, clientSideFileName)) {
			log.info("Fichero subido: " + clientSideFileName);
			
			documento = entityManager.find(Documento.class, variablesSesionGenerales.getIdDocumentoSeleccionado());
			
	    	// Directorio donde se almacenar‡n los fips1
	    	String dir = System.getProperty("jboss.home.dir") + File.separator + 
	    		"var" + File.separator + "FIPs.war" + File.separator + "documentos" + 
	    		File.separator + variablesSesionUsuario.getIdTramiteEncargadoTrabajo() + 
	    		File.separator + documento.getIdgrupodocumento();
	    	log.info("Directorio de los FIPs 1: " + dir);
	    	
	    	// Primero borramos el documento anterior si existia
	    	if (documento.getArchivo()!=null && !documento.getArchivo().equals(""))
	    	{
	    		File oldFile = new File(dir, documento.getArchivo());
				oldFile.delete();
	    	}
	    	
	    	File directorio = new File(dir);
			directorio.mkdirs();
	    	
	    	File newFile = new File(dir, uploadedFile.getName());
	    	
	        // Mover el fichero subido al sitio donde estar‡n todos los ficheros fips1
	        uploadedFile.renameTo(newFile);
	        log.info("Fichero movido corrŽctamente a " + dir);
	        
	        // Actualizamos el nombre del archivo
	    	log.info("Nombre del documento--" + documento.getNombre());
	    	log.info("Archivo del documento--" + documento.getArchivo());
	    	
	    	documento.setArchivo(clientSideFileName);
	        entityManager.merge(documento);
	        entityManager.flush();
	        
	        datosCargados = false;
	        
	        facesMessages.addFromResourceBundle("fichero_anexado_correctamente", null);
	        
		} else {
			facesMessages.addFromResourceBundle(Severity.ERROR,"El nombre del fichero no es valido", null);
		}
	}
	
	public void uploadFilehoja(ActionEvent actionEvent) {

		log.info("Subiendo fichero ...");
		
		// Datos del fichero reciŽn subido
        InputFile inputFile = (InputFile) actionEvent.getSource();
        currentFile = inputFile.getFileInfo();
        String clientSideFileName = currentFile.getFileName();
        log.info("Fichero subido: " + clientSideFileName);
        File uploadedFile = currentFile.getFile();
		
        documentoshp = entityManager.find(Documentoshp.class, variablesSesionGenerales.getIdDocumentoShpSeleccionado());
        
        log.info(documentoshp.getHoja().substring(0, documentoshp.getHoja().length()-4));
        
		if(Textos.validarExpresion(Constantes.EXPRESION_REGULAR_NOMBRE_ARCHIVO, clientSideFileName)
				&& clientSideFileName.contains(documentoshp.getHoja().substring(0, documentoshp.getHoja().length()-4) + ".")
				&& validarArchivo(clientSideFileName, documentoshp.getHoja().substring(documentoshp.getHoja().length()-3, documentoshp.getHoja().length()))) {
			log.info("Fichero subido: " + clientSideFileName);
			
			documento = entityManager.find(Documento.class, variablesSesionGenerales.getIdDocumentoSeleccionado());
			
	    	// Directorio donde se almacenar‡n los fips1
	    	String dir = System.getProperty("jboss.home.dir") + File.separator + 
	    		"var" + File.separator + "FIPs.war" + File.separator + "documentos" + 
	    		File.separator + variablesSesionUsuario.getIdTramiteEncargadoTrabajo() + 
	    		File.separator + documento.getIdgrupodocumento() + 
	    		File.separator + documento.getArchivo() + 
	    		File.separator + "PG";
	    	log.info("Directorio de los FIPs 1: " + dir);
	    	
	    	File directorio = new File(dir);
			directorio.mkdirs();
	    	
	    	File newFile = new File(dir, uploadedFile.getName());
	    	
	        // Mover el fichero subido al sitio donde estar‡n todos los ficheros fips1
	        uploadedFile.renameTo(newFile);
	        log.info("Fichero movido corrŽctamente a " + dir);
	        
	        hojaCargada = true;
	        
	        leerArchivosHoja();
	        
		} else {
			hojaCargada = true;
	        documento = entityManager.find(Documento.class, variablesSesionGenerales.getIdDocumentoSeleccionado());
			facesMessages.addFromResourceBundle(Severity.ERROR,"El nombre del fichero no es valido", null);
		}
	}

    /**
     * <p>This method is bound to the inputFile component and is executed
     * multiple times during the file upload process.  Every call allows
     * the user to finds out what percentage of the file has been uploaded.
     * This progress information can then be used with a progressBar component
     * for user feedback on the file upload progress. </p>
     *
     * @param event holds a InputFile object in its source which can be probed
     *              for the file upload percentage complete.
     */

	public void progressListener(EventObject event) {
        InputFile ifile = (InputFile) event.getSource();
        fileProgress = ifile.getFileInfo().getPercent();
	}

	public boolean isMostrarPlanelDeterminaciones() {
		return mostrarPlanelDeterminaciones;
	}

	public void setMostrarPlanelDeterminaciones(boolean mostrarPlanelDeterminaciones) {
		this.mostrarPlanelDeterminaciones = mostrarPlanelDeterminaciones;
	}
	
	public void cargarDocumento(String documentoId)
	{
		documentoshp = new Documentoshp();
		extensionArchivo = "";
		
		if (documentoId!=null && !documentoId.equals("0") && !documentoId.equals("")){
			documento = entityManager.find(Documento.class, Integer.parseInt(documentoId));
			archivosDocumento = new ArrayList<DocumentoDTO>();
			datosCargados = false;
			leerArchivos();
			variablesSesionGenerales.setIdDocumentoSeleccionado(documento.getIden());
		}
		else{
			documento = new Documento();
			documento.setArchivo("");
			datosCargados = false;
			archivosDocumento = new ArrayList<DocumentoDTO>();
			variablesSesionGenerales.setIdDocumentoSeleccionado(0);
		}
	}
	
	public void cargarDocumentoRedireccionando(String documentoId)
	{
		documentoshp = new Documentoshp();
		extensionArchivo = "";
		
		if (documentoId!=null && !documentoId.equals("0") && !documentoId.equals("")){
			documento = entityManager.find(Documento.class, Integer.parseInt(documentoId));
			archivosDocumento = new ArrayList<DocumentoDTO>();
			datosCargados = false;
			leerArchivos();
			variablesSesionGenerales.setIdDocumentoSeleccionado(documento.getIden());
			
			if (documento.getArchivo()!=null && documento.getArchivo().contains("."))
				FacesManager.instance().redirect("/produccionfip/gestiondocumental/GestionDocumentalDetalle.xhtml");
			else
				FacesManager.instance().redirect("/produccionfip/gestiondocumental/GestionDocumentalCarpetaDetalle.xhtml");
		}
		
	}
	
	public void borrarShpCargarDocumento()
	{
		hojaCargada = false;
		extensionArchivo = "";
		// Borramos el documento
		if (variablesSesionGenerales.getIdDocumentoShpSeleccionado()!=0){
			documentoshp = entityManager.find(Documentoshp.class, variablesSesionGenerales.getIdDocumentoShpSeleccionado());
			documento = entityManager.find(Documento.class, variablesSesionGenerales.getIdDocumentoSeleccionado());
			leerArchivosHoja();
			// Borramos los archivos
			for(DocumentoDTO documento : archivosTemporalesHoja)
			{
				borrarArchivoHojaDiscoSinRecargar(documento.getArchivo());
			}
			
			entityManager.remove(documentoshp);
			entityManager.flush();
		}
		
		documentoshp = new Documentoshp();
		archivosTemporalesHoja = new ArrayList<DocumentoDTO>();
		
		variablesSesionGenerales.setIdDocumentoShpSeleccionado(0);
		
		Map<String,Object> mapa = new HashMap<String, Object>();
		mapa.put("idDocumento", variablesSesionGenerales.getIdDocumentoSeleccionado());
		FacesManager.instance().redirect("/produccionfip/gestiondocumental/GestionDocumentalCarpetaDetalle.xhtml", mapa, true);
	}
	
	public void aceptarHoja()
	{
		documentoshp = new Documentoshp();
		archivosTemporalesHoja = new ArrayList<DocumentoDTO>();
		
		variablesSesionGenerales.setIdDocumentoShpSeleccionado(0);
		
		Map<String,Object> mapa = new HashMap<String, Object>();
		mapa.put("idDocumento", variablesSesionGenerales.getIdDocumentoSeleccionado());
		FacesManager.instance().redirect("/produccionfip/gestiondocumental/GestionDocumentalCarpetaDetalle.xhtml", mapa, true);
	}

	@Remove
	public void remove() {
		// TODO Auto-generated method stub
		
	}

	public boolean isMostrarPlanelEntidades() {
		return mostrarPlanelEntidades;
	}

	public void setMostrarPlanelEntidades(boolean mostrarPlanelEntidades) {
		this.mostrarPlanelEntidades = mostrarPlanelEntidades;
	}

	public Integer getIdDocumento() {
		return idDocumento;
	}

	public void setIdDocumento(Integer idDocumento) {
		this.idDocumento = idDocumento;
	}

	public int getIdDeterminacion() {
		return idDeterminacion;
	}

	public void setIdDeterminacion(int idDeterminacion) {
		this.idDeterminacion = idDeterminacion;
	}
	
	public void crearRelacionDocumentoDeterminacion(int idDet, int idDoc)
	{
		servicioBasicoDocumentos.crearDocumentoDeterminacion(idDet, idDoc);
		facesMessages.addFromResourceBundle(Severity.INFO,"determinacion_asociada_correctamente", null);
	}
	
	public void crearRelacionDocumentoEntidad(int idEnt, int idDoc)
	{
		servicioBasicoDocumentos.crearDocumentoEntidad(idEnt, idDoc);
		facesMessages.addFromResourceBundle(Severity.INFO,"entidad_asociada_correctamente", null);
	}
	
	public void borrarDeterminacionDocumento(int idDeterminacion){
		
		int idDoc = variablesSesionGenerales.getIdDocumentoSeleccionado();
			
		try
		{
			servicioBasicoDeterminaciones.borrarRelacionDeterminacionConDocumento(idDeterminacion, idDoc);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	public void borrarEntidadDocumento(int idDeterminacion){
		
		int idDoc = variablesSesionGenerales.getIdDocumentoSeleccionado();
		try
		{
			servicioBasicoEntidades.borrarRelacionEntidadConDocumento(idDeterminacion, idDoc);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	public void borrarArchivo(String archivo)
	{
		log.info("Borramos el archivo--" + archivo);
	}


	public List<DocumentoDTO> getArchivosDocumento() {
		return archivosDocumento;
	}


	public void setArchivosDocumento(List<DocumentoDTO> archivosDocumento) {
		this.archivosDocumento = archivosDocumento;
	}
	
	public boolean isMostrarPlanelHoja() {
		return mostrarPlanelHoja;
	}

	public void setMostrarPlanelHoja(boolean mostrarPlanelHoja) {
		this.mostrarPlanelHoja = mostrarPlanelHoja;
	}

	public Documentoshp getDocumentoshp() {
		return documentoshp;
	}

	public void setDocumentoshp(Documentoshp documentoshp) {
		this.documentoshp = documentoshp;
	}

	public List<DocumentoDTO> getArchivosTemporalesHoja() {
		return archivosTemporalesHoja;
	}

	public void setArchivosTemporalesHoja(List<DocumentoDTO> archivosTemporalesHoja) {
		this.archivosTemporalesHoja = archivosTemporalesHoja;
	}

	public boolean isHojaCargada() {
		return hojaCargada;
	}

	public void setHojaCargada(boolean hojaCargada) {
		this.hojaCargada = hojaCargada;
	}

	public String getExtensionArchivo() {
		return extensionArchivo;
	}

	public void setExtensionArchivo(String extensionArchivo) {
		this.extensionArchivo = extensionArchivo;
	}

	public List<SelectItem> getListaExtension() {
		
		SelectItem jpg = new SelectItem();
		jpg.setValue("jpg");
		jpg.setLabel("JPG");
		
		SelectItem png = new SelectItem();
		png.setValue("png");
		png.setLabel("PNG");
		
		SelectItem tif = new SelectItem();
		tif.setValue("tif");
		tif.setLabel("TIF");
		
		listaExtension = new ArrayList<SelectItem>();
		
		listaExtension.add(tif);
		listaExtension.add(jpg);
		listaExtension.add(png);
		
		return listaExtension;
	}


	public void setListaExtension(List<SelectItem> listaExtension) {
		this.listaExtension = listaExtension;
	}


	public void borrarArchivoDisco(String archivo)
	{	
		 if(documento.getIden() != 0 && !documento.getArchivo().isEmpty()) {
		 	String dir = System.getProperty("jboss.home.dir") + File.separator + 
    		"var" + File.separator + "FIPs.war" + File.separator + "documentos" + 
    		File.separator + variablesSesionUsuario.getIdTramiteEncargadoTrabajo() + 
    		File.separator + documento.getIdgrupodocumento() + 
    		File.separator + documento.getArchivo();
    		File oldFile = new File(dir, archivo);
    		oldFile.delete();
		 } 
		 
		 leerArchivos();
	}
	
	public void borrarHoja(int iden)
	{	
		documentoshp = entityManager.find(Documentoshp.class, iden);
		
		leerArchivosHoja();
		// Borramos los archivos
		for(DocumentoDTO documento : archivosTemporalesHoja)
		{
			borrarArchivoHojaDiscoSinRecargar(documento.getArchivo());
		}
		
		Documentoshp shp = entityManager.find(Documentoshp.class, iden);
		entityManager.remove(shp);
		entityManager.flush();
		
		//Reiniciamos variables
		documentoshp = new Documentoshp();
		variablesSesionGenerales.setIdDocumentoShpSeleccionado(0);
		archivosTemporalesHoja = new ArrayList<DocumentoDTO>();
		leerArchivos();
	}
	
	
	public void borrarArchivoHojaDisco(String archivo)
	{	
		if (documento==null)
			documento = entityManager.find(Documento.class, variablesSesionGenerales.getIdDocumentoSeleccionado());
		
		documentoshp = entityManager.find(Documentoshp.class, variablesSesionGenerales.getIdDocumentoShpSeleccionado());
		
		 if(documento.getIden() != 0 && !documento.getArchivo().isEmpty()) {
		 	String dir = System.getProperty("jboss.home.dir") + File.separator + 
			"var" + File.separator + "FIPs.war" + File.separator + "documentos" + 
			File.separator + variablesSesionUsuario.getIdTramiteEncargadoTrabajo() + 
			File.separator + documento.getIdgrupodocumento() + 
			File.separator + documento.getArchivo() + 
			File.separator + documentoshp.getHoja().substring(0, documentoshp.getHoja().length()-4);
			File oldFile = new File(dir, archivo);
			oldFile.delete();
		 } 
		 
		 hojaCargada = false;
		 leerArchivosHoja();
	}
	
	public void borrarArchivoHojaDiscoSinRecargar(String archivo)
	{	
		if (documento==null)
			documento = entityManager.find(Documento.class, variablesSesionGenerales.getIdDocumentoSeleccionado());
		
		documentoshp = entityManager.find(Documentoshp.class, variablesSesionGenerales.getIdDocumentoShpSeleccionado());
		
		 if(documento.getIden() != 0 && !documento.getArchivo().isEmpty()) {
		 	String dir = System.getProperty("jboss.home.dir") + File.separator + 
			"var" + File.separator + "FIPs.war" + File.separator + "documentos" + 
			File.separator + variablesSesionUsuario.getIdTramiteEncargadoTrabajo() + 
			File.separator + documento.getIdgrupodocumento() + 
			File.separator + documento.getArchivo() + 
			File.separator + "PG";
		 	
		 	log.info(dir);
		 	
			File oldFile = new File(dir, archivo);
			oldFile.delete();
		 } 
		 
	}
	
	private void leerArchivos()
	{
		
		String dir = System.getProperty("jboss.home.dir") + File.separator + 
		"var" + File.separator + "FIPs.war" + File.separator + "documentos" + 
		File.separator + variablesSesionUsuario.getIdTramiteEncargadoTrabajo() + 
		File.separator + documento.getIdgrupodocumento() + File.separator;
		
		log.info(dir);
		
		archivosDocumento = new ArrayList<DocumentoDTO>();
		
		File directorio = new File(dir);
		String [] ficheros = directorio.list();

		if (ficheros!=null){
			for (int i = 0; i < ficheros.length; i++) {
		        log.info("Contenido del fichero " + ficheros[i]);
		        
		        // Si el documento es la carpeta PG no lo metemos
		        if (ficheros[i].contains(".")){
			        DocumentoDTO documento = new DocumentoDTO();
			        documento.setArchivo(ficheros[i]);
			        archivosDocumento.add(documento);
		        }
			}
		}
			
	}
	
	private void leerArchivosHoja()
	{
		
		if (documentoshp.getIden()>0){
			String dir = System.getProperty("jboss.home.dir") + File.separator + 
			"var" + File.separator + "FIPs.war" + File.separator + "documentos" + 
			File.separator + variablesSesionUsuario.getIdTramiteEncargadoTrabajo() + 
			File.separator + documento.getIdgrupodocumento() + File.separator + 
			documento.getArchivo() + File.separator + "PG" + File.separator;
			
			log.info(dir);
			
			archivosTemporalesHoja = new ArrayList<DocumentoDTO>();
			
			File directorio = new File(dir);
			String [] ficheros = directorio.list();
			if (ficheros!=null){
				for (int i = 0; i < ficheros.length; i++) {
			        log.info("Contenido del fichero " + ficheros[i]);
			        if (ficheros[i].contains(documentoshp.getHoja().substring(0, documentoshp.getHoja().length()-4) + ".")){
				        DocumentoDTO documento = new DocumentoDTO();
				        documento.setArchivo(ficheros[i]);
				        documento.setEsHoja("No");
				        
				        archivosTemporalesHoja.add(documento);
			        }
				}
			}
		}
	}
	
	public boolean hojaValida()
	{
		boolean validoRaiz = false;
		boolean validoAdjunto = false;
		
		if (documentoshp!=null && documentoshp.getIden()!=0 && archivosTemporalesHoja!=null){
			if (archivosTemporalesHoja.size()==2){
				String extension = documentoshp.getHoja().substring(documentoshp.getHoja().length()-3, documentoshp.getHoja().length());
				for(DocumentoDTO documento : archivosTemporalesHoja)
				{
					// Caso JPG
					
					if (extension.equals("jpg")){
						if (documento.getArchivo().equals(documentoshp.getHoja()))
							validoRaiz=true;
						if (documento.getArchivo().contains(".jpw") || documento.getArchivo().contains(".jpgw"))
							validoAdjunto = true;
					}
					else if (extension.equals("png")){
						if (documento.getArchivo().equals(documentoshp.getHoja()))
							validoRaiz=true;
						if (documento.getArchivo().contains(".pgw") || documento.getArchivo().contains(".pngw"))
							validoAdjunto = true;
					}
					else if (extension.equals("tif")){
						if (documento.getArchivo().equals(documentoshp.getHoja()))
							validoRaiz=true;
						if (documento.getArchivo().contains(".tfw") || documento.getArchivo().contains(".tifw"))
							validoAdjunto = true;
					}
					
				}
			}
			if (!validoRaiz || !validoAdjunto)
				facesMessages.addFromResourceBundle(Severity.ERROR,"Debe contener dos documentos válidos.", null);
		}
		
		return (validoRaiz && validoAdjunto);
	}
	
	public boolean validarArchivo(String documento, String extension)
	{
		boolean valido = false;
		if (extension.equals("jpg")){
			if (documento.equals(documentoshp.getHoja()))
				valido=true;
			if (documento.contains(".jpw") || documento.contains(".jpgw"))
				valido = true;
		}
		else if (extension.equals("png")){
			if (documento.equals(documentoshp.getHoja()))
				valido=true;
			if (documento.contains(".pgw") || documento.contains(".pngw"))
				valido = true;
		}
		else if (extension.equals("tif")){
			if (documento.equals(documentoshp.getHoja()))
				valido=true;
			if (documento.contains(".tfw") || documento.contains(".tifw"))
				valido = true;
		}
		
		return valido;
	}
	
	
	public String getRutaFicheros() {
		return rutaFicheros;
	}


	public void setRutaFicheros(String rutaFicheros) {
		this.rutaFicheros = rutaFicheros;
	}
	
	public Resource obtenerFichero(String nombre) {
		
		rutaFicheros = System.getProperty("jboss.home.dir") + File.separator + 
		"var" + File.separator + "FIPs.war" + File.separator + "documentos" + 
		File.separator + variablesSesionUsuario.getIdTramiteEncargadoTrabajo() + 
		File.separator + documento.getIdgrupodocumento() + File.separator;
		
		Resource fichero = new Fichero(nombre);
		return fichero;
	}
	
	
	public String obtenerNombreFichero(int idDoc)
	{
		log.info("[obtenerNombreFichero] iddocumento: " + idDoc);
		
		String nombreDoc = "defecto.pdf";
		
		Documento documento = entityManager.find(Documento.class, idDoc);
		
		String rutaNombreArchivo = documento.getArchivo();
		log.info("[obtenerNombreFichero] rutaNombreArchivo: " + rutaNombreArchivo);
		
		String[] rutaNombreArchivoSplit = rutaNombreArchivo.split(Pattern.quote("\\"));
		
		
		
		String nombreArchivo = rutaNombreArchivoSplit[rutaNombreArchivoSplit.length-1];
		
		nombreDoc = nombreArchivo;
		log.info("[obtenerNombreFichero] nombreArchivo: " + nombreDoc);
		
		
		return nombreDoc;
		
		
	}
	public Resource obtenerFicheroPlanVigente(int identificador)
	{
		
		log.info("[obtenerFicheroPlanVigente] iddocumento: " + identificador);
		Documento documento = entityManager.find(Documento.class, identificador);
		
		String rutaNombreArchivo = documento.getArchivo();
		log.info("[obtenerFicheroPlanVigente] rutaNombreArchivo: " + rutaNombreArchivo);
		
		// Tengo que obtener el UUID que se le asigno a los documentos fisicos en la ruta al desplegar
		
		// Lo primero es buscar el PlanesEncargado del tramite de ese documento (tramive vigente)
		String queryPlanesEncargado = " select pe from PlanesEncargados pe where pe.tramiteVigente.iden = "+documento.getTramite().getIden();
		
		PlanesEncargados planEncarg = (PlanesEncargados) entityManager.createQuery(queryPlanesEncargado).getSingleResult();
		
		int idPlanEncargado = planEncarg.getId().intValue();
		
		// Busco ahora el FipCargado
		String queryFipCargado = " select fc from FipsCargados fc where fc.idplaneamientoencargado = "+idPlanEncargado;
		FipsCargados fipCargado = (FipsCargados) entityManager.createQuery(queryFipCargado).getSingleResult();
		
		rutaFicheros = fipCargado.getRuta()	+ File.separator;
		
		String[] rutaNombreArchivoSplit = rutaNombreArchivo.split(Pattern.quote("\\"));
		
		for (int i=0; i<rutaNombreArchivoSplit.length-1;i++)
		{
			rutaFicheros +=rutaNombreArchivoSplit[i]+ File.separator;
		}
		
		String nombreArchivo = rutaNombreArchivoSplit[rutaNombreArchivoSplit.length-1];
		
		log.info("[obtenerFicheroPlanVigente] rutaFicheros: " + rutaFicheros);
		log.info("[obtenerFicheroPlanVigente] nombreArchivo: " + nombreArchivo);
		
		File ficheroCompr = new File(rutaFicheros, nombreArchivo);
		
		log.info("[obtenerFicheroPlanVigente] ficheroCompr ruta completa: " + ficheroCompr.getAbsolutePath());
		log.info("[obtenerFicheroPlanVigente] ficheroCompr exite: " + ficheroCompr.exists());
		
		Resource fichero = new Fichero(nombreArchivo);
		return fichero;
	}

	private class Fichero implements Resource, Serializable {

		private String nombre;
		private final Date lastModified;
	    private InputStream inputStream;
		
		public Fichero(String nombre) {
			this.nombre = nombre;
			this.lastModified = new Date();
		}

		@Override
		public String calculateDigest() {
			return nombre;
		}

		@Override
		public Date lastModified() {
			return lastModified;
		}

		@Override
		public InputStream open() throws IOException {
			
			File fichero = new File(rutaFicheros, nombre);
			log.info("[Fichero] rutaFicheros: " + rutaFicheros);
			log.info("[Fichero] nombre: " + nombre);
			
			if(fichero.exists()) {
				inputStream = new FileInputStream(fichero);
			} else {
				inputStream = new FileInputStream(new File("Fichero_Incorrecto.info"));
			}		
			
			return inputStream;
		}

		@Override
		public void withOptions(Options arg0) throws IOException {			
		}
		
	}
	
}

/*
* Copyright 2011 red.es
* Autores: IDOM Consulting
*
* Licencia con arreglo a la EUPL, Versi�n 1.1 o -en cuanto
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

package com.mitc.redes.editorfip.servicios.procesamientofip.generacionfip2;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;
import java.util.Date;

import javax.ejb.Remove;
import javax.ejb.Stateless;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.web.RequestParameter;

import com.icesoft.faces.context.Resource;
import com.mitc.redes.editorfip.utilidades.Textos;


@Stateless
@Name("resourceBean")
public class OutputResourceBean implements OutputResource{
    
    private Resource imgResource;
    private Resource pdfResource;
    private Resource pdfResourceDynFileName;
    private Resource xmlResource;
    private String fileName = "Choose-a-new-file-name";
    public static final String RESOURCE_PATH = "/WEB-INF/classes/";
    
    @RequestParameter
	private String urlFichero;
    


    public OutputResourceBean(){
        try{
            //FacesContext fc = FacesContext.getCurrentInstance();
            //ExternalContext ec = fc.getExternalContext();
            //imgResource = new MyResource(ec,"logo.jpg");
            //pdfResource =  new MyResource(ec,"WP_Security_Whitepaper.pdf");
            //pdfResource = new MyResource(ec,"archivo.xml", urlFichero);
            //xmlResource =  new MyResource(ec,"archivo.xml");
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public Resource getImgResource() {
        return imgResource;
    }

    
    public String getUrlFichero() {
		return urlFichero;
	}

	public void setUrlFichero(String urlFichero) {
		this.urlFichero = urlFichero;
	}

	public Resource getPdfResource(){
		FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        pdfResource = new MyResource(ec,"archivo.xml", "/FIPs/refundido/Herrera/fip756.xml");
        return pdfResource;
    }
    
	public Resource obtenerResource(String url){
		FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        pdfResource = new MyResource(ec,"archivo.xml", url);
        return pdfResource;
    }
	
    public Resource getXmlResource(){
    	
        return xmlResource;
    }
       
    public static byte[] toByteArray(InputStream input) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] buf = new byte[4096];
        int len = 0;
        while ((len = input.read(buf)) > -1) output.write(buf, 0, len);
        return output.toByteArray();
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Resource getPdfResourceDynFileName() {
        return pdfResourceDynFileName;
    }

    public void setPdfResourceDynFileName(Resource pdfResourceDynFileName) {
        this.pdfResourceDynFileName = pdfResourceDynFileName;
    }
    
    @Remove
	public void remove() {		
	}
}

class MyResource implements Resource, Serializable{
    private String resourceName;
    private InputStream inputStream;
    private final Date lastModified;
    private ExternalContext extContext;
    private String urlFichero;
    
    public MyResource(ExternalContext ec, String resourceName, String urlFichero) {
        this.extContext = ec;
        this.resourceName = resourceName;
        this.lastModified = new Date();   
        this.urlFichero = urlFichero;
    }
    
    /**
     * This intermediate step of reading in the files from the JAR, into a
     * byte array, and then serving the Resource from the ByteArrayInputStream,
     * is not strictly necessary, but serves to illustrate that the Resource
     * content need not come from an actual file, but can come from any source,
     * and also be dynamically generated. In most cases, applications need not
     * provide their own concrete implementations of Resource, but can instead
     * simply make use of com.icesoft.faces.context.ByteArrayResource,
     * com.icesoft.faces.context.FileResource, com.icesoft.faces.context.JarResource.
     */
    public InputStream open() throws IOException {
        if (inputStream == null) {   
        	String urlBase = Textos.getCadena("fipeditor", "url_base");
        	URL url  = new URL(urlBase + urlFichero);
	        
	          
	        // Copy resource to local file, use remote file
	        // if no local file name specified
	        inputStream = url.openStream();
	        byte[] byteArray = OutputResourceBean.toByteArray(inputStream);
	        inputStream = new ByteArrayInputStream(byteArray);
        	
        }
        return inputStream;
    }
    
    public String calculateDigest() {
        return resourceName;
    }

    public Date lastModified() {
        return lastModified;
    }
    
    public String getUrlFichero() {
		return urlFichero;
	}

	public void setUrlFichero(String urlFichero) {
		this.urlFichero = urlFichero;
	}

	public void withOptions(Options arg0) throws IOException {
    }
}   


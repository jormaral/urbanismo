package com.mitc.redes.editorfip.servicios.ayuda.faq;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PushbackReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.ejb.Stateless;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.render.RenderKit;
import javax.faces.render.Renderer;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.faces.FacesManager;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.international.StatusMessage.Severity;
import org.jboss.seam.international.StatusMessages;
import org.jboss.seam.log.Log;


import com.icesoft.faces.component.InputTextTag;
import com.icesoft.faces.component.ext.HtmlInputText;
import com.icesoft.faces.component.inputfile.FileInfo;
import com.icesoft.faces.component.inputfile.InputFile;
import com.icesoft.faces.context.Resource;
import com.mitc.redes.editorfip.entidades.rpm.ayuda.Faq;
import com.mitc.redes.editorfip.entidades.rpm.ayuda.Manual;
import com.mitc.redes.editorfip.servicios.ayuda.faq.GestionManuales;

@Stateful
@Name("gestionManuales")
public class GestionManualesBean implements GestionManuales {
	
	@Logger
	private Log log;

	@In (create = true, required = false)
	FacesMessages facesMessages;

	@PersistenceContext
	EntityManager em;
	
	private List<Manual> resultado;
	
	private Manual manualNuevo = new Manual();
	
	private static final String RUTA_MANUALES = System.getProperty("jboss.home.dir") + 
												File.separator +  "var" 
												+ File.separator + "FIPs.war"
												+ File.separator + "manuales";
	
	private HtmlInputText nombreFicheroBinding;

	private FileInfo infoFichero = new FileInfo();

	public List<Manual> getResultado() {
		log.debug("[obtenerListadoFaq] Se van a obtener las faqs");

		if(resultado == null) {
			resultado = new ArrayList<Manual>();

			String queryFaqOrden = "SELECT m " 
					+ " FROM Manual m "
					+ " ORDER BY m.orden ASC";

			try {
				resultado = (ArrayList<Manual>) em.createQuery(queryFaqOrden).getResultList();

			} catch (NoResultException e) {
				log.error("[obtenerListadoFaq] No se han encontrado manuales:"+ e.getMessage());
				e.printStackTrace();

			} catch (Exception ex) {
				log.error("[obtenerListadoFaq] Error en la busqueda de manuales:"+ ex.getMessage());
				ex.printStackTrace();

			}
		}
		

		log.debug("[obtenerListadoFaq] Numero de Manuales obtenidos: "+ resultado.size());
		return resultado;
	}
	
	public void adjuntarArchivo(InputFile inputFile) {
		// Obtenemos los datos del componente y del fichero cargado
		if(inputFile != null) {
			infoFichero = inputFile.getFileInfo();
			nombreFicheroBinding.setValue(infoFichero.getFileName());
		}
		
	}
	
	public void guardarDocumento() {
		
		if(infoFichero != null && infoFichero.getFile() != null) {
			
			File directorio = new File(RUTA_MANUALES);
			if(!directorio.exists())
				directorio.mkdirs();
			
			File ficheroFile = infoFichero.getFile();
			
			// Nos creamos nuestro nuevo fichero en la ruta indicada
			File nuevoFichero = new File(RUTA_MANUALES, infoFichero.getFileName());
			try {
				
				// cargamos los datos del fichero subido
				InputStream fichero = new FileInputStream(ficheroFile);
				
				// Escribimos los datos en el nuevo fichero
				OutputStream os = new FileOutputStream(nuevoFichero);
				byte buf[] = new byte[1024];
				int len;
				
				while((len = fichero.read(buf))>0) {
					os.write(buf, 0, len);
				}
				
				os.close();
				fichero.close();
				
				manualNuevo.setArchivo(nuevoFichero.getName());
				em.persist(manualNuevo);
				
				FacesManager.instance().redirect("/ayuda/manuales/Manuales.xhtml");
				
				manualNuevo = new Manual();
				infoFichero = new FileInfo();				
				facesMessages.addFromResourceBundle("manual_ok", null);
				
			} catch (Exception e) {
				e.printStackTrace();
				log.error("ERROR AL GUARDAR EL DOCUMENTO: " + e.getLocalizedMessage(), null);
				facesMessages.addFromResourceBundle(Severity.ERROR, "manual_error", null);
			}
		} else {
			facesMessages.addFromResourceBundle(Severity.ERROR, "manual_no_fich", null);
		}		
	}
	
	public void cancelarSubida() {
		FacesManager.instance().redirect("/ayuda/manuales/Manuales.xhtml");		
		manualNuevo = new Manual();
		infoFichero = new FileInfo();	
	}
	
	public void eliminarManual(long idManual) {
		
		Manual manual = (Manual)em.find(Manual.class, idManual);
		File fichero = new File(RUTA_MANUALES, manual.getArchivo());
		if(fichero.exists()) {
			fichero.delete();
		}
		
		em.remove(manual);
		facesMessages.addFromResourceBundle(Severity.ERROR, "manual_del_ok", null);
		
	}

	public void setResultado(ArrayList<Manual> resultado) {
		this.resultado = resultado;
	}	

	public Manual getManualNuevo() {
		return manualNuevo;
	}

	public void setManualNuevo(Manual manualNuevo) {
		this.manualNuevo = manualNuevo;
	}
	
	public HtmlInputText getNombreFicheroBinding() {
		return nombreFicheroBinding;
	}

	public void setNombreFicheroBinding(HtmlInputText nombreFicheroBinding) {
		this.nombreFicheroBinding = nombreFicheroBinding;
	}

	public Resource obtenerFichero(String nombre) {
		
		Resource fichero = new Fichero(nombre);
		return fichero;
	}
	
	@Remove
	public void destroy() {}

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
			
			File fichero = new File(RUTA_MANUALES, nombre);
			
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

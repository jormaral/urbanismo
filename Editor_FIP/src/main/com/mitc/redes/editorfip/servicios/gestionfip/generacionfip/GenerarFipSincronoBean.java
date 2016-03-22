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

package com.mitc.redes.editorfip.servicios.gestionfip.generacionfip;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.faces.FacesManager;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.international.StatusMessage.Severity;
import org.jboss.seam.international.StatusMessages;
import org.jboss.seam.log.Log;

import com.mitc.redes.editorfip.entidades.rpm.gestionfip.FipsGenerados;
import com.mitc.redes.editorfip.servicios.basicos.fip.tramites.ServicioBasicoTramites;
import com.mitc.redes.editorfip.servicios.gestionfip.gestionprerefundido.ServicioBasicoPrerefundido;
import com.mitc.redes.editorfip.servicios.procesamientofip.generacionfip2.ConversionFIPXML;



@Stateless
@Name("generarFipSincrono")
public class GenerarFipSincronoBean implements GenerarFipSincrono
{
    @Logger private Log log;
    
    @PersistenceContext
	EntityManager em;
    
    @In(create = true, required = false)
    ConversionFIPXML conversionFIPXML;
    
    @In(create = true)
	FacesMessages facesMessages;
    
    @In(create = true, required = false)
	ServicioBasicoPrerefundido servicioBasicoPrerefundido;
    
    @In(create = true, required = false)
	ServicioBasicoTramites servicioBasicoTramites;
    
    @In(create = true, required = false)
    ListadoFipGenerado listadoFipGenerado;
    
    @In StatusMessages statusMessages;
    
    public void borrarFIP2(int idFIP2) {
		
		log.debug("[borrarFIP2] idFIP2="+idFIP2);
		
		Long idFIP2Long = Long.valueOf(idFIP2);
		
		try {
			FipsGenerados fip2 = em.find(FipsGenerados.class, idFIP2Long);			
			em.remove(fip2);
			
			statusMessages.addFromResourceBundle(Severity.INFO, "Se ha borrado el fip2 correctamente", null);
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
			statusMessages.addFromResourceBundle(Severity.ERROR, "Se han producido problemas al borrar el fip2 y no se ha podido llevar a cabo", null);
			
		}
		
		
		log.debug("[borrarFIP2] Fin");
		
	}
    
    public void generarFIP2ZIP(int idFIP2) {
		
		log.debug("[generarFIP2ZIP] idFIP2="+idFIP2);
		
		Long idFIP2Long = Long.valueOf(idFIP2);
		
		try {
			FipsGenerados fip2 = em.find(FipsGenerados.class, idFIP2Long);	
			
			ZipUsingJavaUtil zipClass = new ZipUsingJavaUtil();
			
			// Averiguo el nombre del XML para ponerselo al ZIP
			String nombreXML = fip2.getNombreArchivo();
			
			String[] ficheroXMLExtension = nombreXML.split("\\.");
            String nombreBase= ficheroXMLExtension[0];
            
            String nombreZIP = nombreBase+".zip";
            //String nombreZIPTemporal = nombreBase+"temporal.zip";
            
            // Tengo que averiguar cual es el idTramite para buscar los documentos
            int idTramiteEncargado = fip2.getIdTramiteEncargado();
            
            // La ruta donde estan los documentos
            String rutaDocumentos = System.getProperty("jboss.home.dir") + File.separator
			+ "var" + File.separator + "FIPs.war" + File.separator
			+ "documentos" + File.separator + idTramiteEncargado + File.separator;
            
            // La ruta donde voy a poner el ZIP
            String rutaZIP = System.getProperty("jboss.home.dir") + File.separator
			+ "var" + File.separator + "FIPs.war" + File.separator
			+ "refundido" + File.separator + idTramiteEncargado + File.separator;
            
			
			// Genero el ZIP con los documentos			
			zipClass.zipFiles(rutaDocumentos, rutaZIP+nombreZIP);
			
			// Le anado al ZIP el fip.xml correspondiente
			
			File fileZip = new File (rutaZIP+nombreZIP);
			File nuevofichero = new File (rutaZIP+nombreXML);
			File[] arrayFile = new File[1];
			arrayFile[0] = nuevofichero;
			
			addFilesToExistingZip(fileZip,arrayFile);
						
			
			// Actualizo el listado de los FIP2
			listadoFipGenerado.refrescarLista();
			
			// Actualizo el fip2 con la ruta del ZIP
			String rutaDescargaZIP = File.separator + "FIPs" + File.separator + "refundido" + File.separator + + idTramiteEncargado + File.separator + nombreZIP;
			fip2.setFipzip(rutaDescargaZIP);
			em.merge(fip2);
			em.flush();
			
			
			
			statusMessages.addFromResourceBundle(Severity.INFO, "Se ha generado el ZIP del fip2 correctamente. Pulse en refrescar para poder visualizar el enlace", null);
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
			statusMessages.addFromResourceBundle(Severity.ERROR, "Se han producido problemas al generar el ZIP del fip2 y no se ha podido llevar a cabo", null);
			
			
		}
		
		
		log.debug("[generarFIP2ZIP] Fin");
		
	}
	
    private void addFilesToExistingZip(File zipFile,
            File[] files) throws IOException {
           // get a temp file
       File tempFile = File.createTempFile(zipFile.getName(), null);
           // delete it, otherwise you cannot rename your existing zip to it.
       tempFile.delete();

       boolean renameOk=zipFile.renameTo(tempFile);
       if (!renameOk)
       {
           throw new RuntimeException("could not rename the file "+zipFile.getAbsolutePath()+" to "+tempFile.getAbsolutePath());
       }
       byte[] buf = new byte[1024];

       ZipInputStream zin = new ZipInputStream(new FileInputStream(tempFile));
       ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFile));

       ZipEntry entry = zin.getNextEntry();
       while (entry != null) {
           String name = entry.getName();
           boolean notInFiles = true;
           for (File f : files) {
               if (f.getName().equals(name)) {
                   notInFiles = false;
                   break;
               }
           }
           if (notInFiles) {
               // Add ZIP entry to output stream.
               out.putNextEntry(new ZipEntry(name));
               // Transfer bytes from the ZIP file to the output file
               int len;
               while ((len = zin.read(buf)) > 0) {
                   out.write(buf, 0, len);
               }
           }
           entry = zin.getNextEntry();
       }
       // Close the streams        
       zin.close();
       // Compress the files
       for (int i = 0; i < files.length; i++) {
           InputStream in = new FileInputStream(files[i]);
           // Add ZIP entry to output stream.
           out.putNextEntry(new ZipEntry(files[i].getName()));
           // Transfer bytes from the file to the ZIP file
           int len;
           while ((len = in.read(buf)) > 0) {
               out.write(buf, 0, len);
           }
           // Complete the entry
           out.closeEntry();
           in.close();
       }
       // Complete the ZIP file
       out.close();
       tempFile.delete();
   }
    
    public void generarFipSincrono(int idTramite)
    {
    	
    	log.info("[generarFipSincrono] idTramite="+idTramite);
    	
    	String os = System.getProperty("os.name").toLowerCase();
		//String nombreAmbito = nombreAmbitoDelTramite(idTramite);
		
		String nombreArchivo = "fip"  + "_" + idTramite + "_" + new Date().getTime() + ".xml";
		
		String dir = System.getProperty("jboss.home.dir") + File.separator
				+ "var" + File.separator + "FIPs.war" + File.separator
				+ "refundido" + File.separator + idTramite + File.separator;
		String fileDir = System.getProperty("jboss.home.dir") + File.separator
				+ "var" + File.separator + "FIPs.war" + File.separator
				+ "refundido" + File.separator + idTramite + File.separator
				+ nombreArchivo;
		
		
		
		String url = "/FIPs/refundido/" + idTramite + "/" + nombreArchivo;
		log.info(dir);
		log.info(fileDir);
		
		// Cambiar estado a CREANDO
		FipsGenerados fip = new FipsGenerados();
		fip.setEstado("CREANDO");
		fip.setXml(fileDir);
		fip.setUrl(url);
		fip.setFipzip("NOCREADO");
		
		// Miramos que versión toca
		fip.setVersion(servicioBasicoPrerefundido.obtenerSiguienteVersion(idTramite));

		fip.setFechaGeneracion(new Date());
		fip.setIdTramiteEncargado(idTramite);
		fip.setNombre(servicioBasicoTramites.nombreTramite(idTramite));
		fip.setNombreArchivo(nombreArchivo);
		
		
		try
		{
			em.persist(fip);
			em.flush();
			
			// Llamo al Asincrono
			log.info("[generarFipSincrono] Llamo al asincrono");
			conversionFIPXML.crearFIP2Asincrono(idTramite, fip);
			
			facesMessages.addFromResourceBundle(Severity.INFO,"Se esta generando el FIP2.xml. Pulse en el enlace 'Ir Listado FIP2 Generados'.", null);
			//FacesManager.instance().redirect("/gestionfip/generacionfip2/Fip2GeneradosList.xhtml");
		}
		catch (Exception e)
		{
			facesMessages.addFromResourceBundle(Severity.ERROR, "Error al crear el FIP2", null); 
		}
		
		
		

    		
    	
    }
    
    public String nombreAmbitoDelTramite(int idTramite) {
		String nombreAmbito = "NoEncontrado";

		String queryNombreAmbito = "SELECT tra.texto "
				+ " FROM Tramite tram, "
				+ " Plan pl, "
				+ " Ambito amb, "
				+ " Literal lit, "
				+ " Traduccion tra "
				+ " WHERE tram.iden="
				+ idTramite
				+ " AND pl.iden = tram.plan.iden AND amb.iden = pl.idambito AND lit.iden = amb.literal.iden AND tra.literal.iden = lit.iden";

		try {

			Query qPlan = em.createQuery(queryNombreAmbito);

			nombreAmbito = (String) qPlan.getSingleResult();

		} catch (Exception ex) {
			ex.printStackTrace();
			log.warn("[nombreAmbitoDelTramite] Error al obtener el nombre del ambito para el idTramite:"
					+ idTramite);
		}
 
		return nombreAmbito;
	}
    
   
    
}
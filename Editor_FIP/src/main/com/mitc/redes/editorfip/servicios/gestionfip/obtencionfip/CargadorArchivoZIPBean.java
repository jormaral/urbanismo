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

package com.mitc.redes.editorfip.servicios.gestionfip.obtencionfip;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Enumeration;
import java.util.EventObject;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import javax.ejb.Remove;
import javax.ejb.Stateless;
import javax.faces.event.ActionEvent;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.international.StatusMessage.Severity;
import org.jboss.seam.log.Log;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.icesoft.faces.component.inputfile.FileInfo;
import com.icesoft.faces.component.inputfile.InputFile;
import com.mitc.redes.editorfip.entidades.rpm.gestionfip.FipsCargados;
import com.mitc.redes.editorfip.excepciones.EditorFIPException;

import es.mitc.redes.urbanismoenred.data.fip.FIP;

@Stateless
@Name ("cargadorArchivoZIP")
public class CargadorArchivoZIPBean implements CargadorArchivoZIP{

	
	

	
	@Logger private Log log;
	
	// Conexion con el bean de gestion de recintos
    @In (create = true)
    ServicioCargaXML servicioCargaXML;
    
	@In(create=true)
    FacesMessages facesMessages;
    
    @PersistenceContext
    EntityManager entityManager;
    
	
	private FileInfo currentFile;
	private int fileProgress;
	private Document xmlDoc;

	public FileInfo getCurrentFile() {
		return currentFile;
	}


    public int getFileProgress() {
        return fileProgress;
    }

	
	
	public void uploadFIPZIPActionListener(ActionEvent actionEvent) {

		log.info("[uploadFIPZIPActionListener] Subiendo fichero ...");
		
		// Datos del fichero reciŽn subido
        InputFile inputFile = (InputFile) actionEvent.getSource();
        currentFile = inputFile.getFileInfo();
        String clientSideFileName = currentFile.getFileName();
        log.info("[uploadFIPZIPActionListener] Fichero subido: " + clientSideFileName);
        File uploadedFile = currentFile.getFile();
        //log.debug("EEEEEEEEEEEEEE-" + clientSideFileName.substring(clientSideFileName.length()-4, clientSideFileName.length()-1) + "-EEEE");
   
        
        // Extensi—n del fichero subido
        String extension = clientSideFileName.substring(clientSideFileName.length()-3, clientSideFileName.length());
        log.info("[uploadFIPZIPActionListener] Extensi—n del fichero: " + extension);
        
        // Comprobamos que la extensi—n es correcta
        if (extension.equals("zip")) {

	        //File uploadDirectory = uploadedFile.getParentFile();
	
	        // Nombre del fichero
	        UUID uuid = UUID.randomUUID();
	        
	        // Me tengo que llevar el ZIP a otra ubicacion
	        String newFileZIPName = "fip-" + uuid.toString() + ".zip";
	        // Directorio donde se almacenar‡n los fips1
        	String fips1Dir = System.getProperty("jboss.home.dir") +  File.separator + "FIP1ZIP"+ File.separator + "fip-" + uuid.toString();
        	log.info("[uploadFIPZIPActionListener] Directorio de los FIPs 1: " + fips1Dir);
        	// Creo el directofio
        	File creoDirectorio = new File (fips1Dir);
        	creoDirectorio.mkdirs();
        	
        	
        	File newZIPFile = new File(fips1Dir, newFileZIPName);
        	
	        // Mover el fichero subido al sitio donde estar‡n todos los ficheros fips1
	        uploadedFile.renameTo(newZIPFile);
	        log.info("[uploadFIPZIPActionListener] Fichero ZIP movido corrŽctamente a " + fips1Dir);
	        
	        
	        try
	        {
		        // Tengo que descomprimir el ZIP
		        //descomprimir(fips1Dir, newFileZIPName);
		        
		        unzip(newZIPFile.getAbsolutePath());
		        
		        // Una vez descomprimido, borramos el ZIP
		        newZIPFile.delete();
		        
		        // Compruebo que en la carpeta descomprimida este el fip.xml
		        try
		        {
		        	String rutaCarpetaZIP = fips1Dir + File.separator + "fip-" + uuid.toString();
		        	File newXMLFile = new File(rutaCarpetaZIP, "fip.xml");
		        	
		        	if (newXMLFile.exists())
		        	{
		        		 String newFileName = "fip-" + uuid.toString() + ".xml";
	
		 	         	
		 	         	File newFile = new File(rutaCarpetaZIP, newFileName);
		 	         	
		 	 	        // Mover el fichero subido al sitio donde estar‡n todos los ficheros fips1
		 	         	newXMLFile.renameTo(newFile);
		 	 	        log.info("[uploadFIPZIPActionListener] Fichero XML movido corrŽctamente a " + rutaCarpetaZIP);
		 	 	        
		 	 	        try {
		 	 	        	
		 	 	        	
		 	 	        	 FIP fip = null;
	
		 	 	   		
		 	 	   		
		 	 	   		log.debug("Cargando FIP xml: "+ newFile.getAbsolutePath()  );
		 	 	   		
		 	 	   		if (servicioCargaXML==null)
		 	 	   		{
		 	 	   			log.debug("servicioCargaXML==null");
		 	 	   		}
		 	 	   		else
		 	 	   		{
		 	 	   		log.debug("servicioCargaXML no null");
		 	 	   		}
		 	 	   		
		 	 	   		
		 	 	   		fip = servicioCargaXML.obtenerObjetoFIPdelXML( newFile.getAbsolutePath() );
		 	 	   		
		 	 	   		// Compruebo si tiene Planeamiento Encargado
		 	 	   		
		 	 	   		if (fip.getPLANEAMIENTOENCARGADO()!=null)
		 	 	   		{
		 	 	   			int idambito = new Integer(fip.getPLANEAMIENTOENCARGADO().getAmbito());
		 					
		 					log.info("[uploadFIPZIPActionListener] Id de ‡mbito del FIP1: " + idambito);
		 					
		 					// Creamos nuevo registro en la tabla de FIPs1 subidos
		 			        FipsCargados fc = new FipsCargados();
		 			        fc.setIdambito(idambito);
		 			        fc.setConsolidado(false);
		 			        fc.setIdentificador(newFileName);
		 			        //fc.setRuta(uploadDirectory.getPath());
		 			        fc.setRuta(rutaCarpetaZIP);
		 			        fc.setFechaIntroduccion(new Date());
		 			        
		 			        // Persistimos
		 			        log.info("[uploadFIPZIPActionListener] Guardando info del nuevo fichero subido en BD ... ");
		 			        entityManager.persist(fc);
		 			        entityManager.flush();
		 			        
		 			        facesMessages.addFromResourceBundle(Severity.INFO,"El FIP1 ha sido cargado correctamente. Pulse sobre 'Ver listado FIP1 cargados' para continuar", null);
	
		 	 	   		}
		 	 	   		else
		 	 	   		{
		 	 	   			// No hay PLANEAMIENTO-ENCARGADO, aviso de que no se continua
	 	 					facesMessages.addFromResourceBundle(Severity.ERROR,"El FIP1 no contiene Planeamiento Encargado. No se puede importar. Asegurese que el FIP1 tenga un Planeamiento Encargado y reintente la carga", null);
	
		 	 	   		}
		 	 	        	
		 	 		    
		 	 	        
		 	 				
		 	 	        
		 	 	        } 
		 	 	        catch (EditorFIPException e) {
		 	 				e.printStackTrace();
		 	 				facesMessages.addFromResourceBundle(Severity.ERROR,"ERROR: Problemas de Integridad. El FIP.xml no es integro: "+e.getMessage());
	
		 	 			} 
		 	 	        
		 	 	        catch (Exception e) {
		 	 				e.printStackTrace();
		 	 				facesMessages.addFromResourceBundle(Severity.ERROR,"ERROR: Problemas al cargar el FIP. Vuelva a cargarlo de nuevo");
	
		 	 			} 
		        	}
		        	else
		        	{
		        		facesMessages.addFromResourceBundle(Severity.ERROR,"ERROR: No se encuentra el fichero 'fip.xml' dentro de la raiz del ZIP. Compruebe el fichero ZIP y vuelva a cargarlo de nuevo");
		        	}
		        	
		        	
		        }
		        catch (Exception e) {
	 				// TODO Auto-generated catch block
	 				e.printStackTrace();
	 				facesMessages.addFromResourceBundle(Severity.ERROR,"ERROR: No se encuentra el fichero 'fip.xml' dentro de la raiz del ZIP");
	 			}
	        }
	        catch (Exception e) {
 				// TODO Auto-generated catch block
 				e.printStackTrace();
 				facesMessages.addFromResourceBundle(Severity.ERROR,"ERROR: Al descomprimir el ZIP");
 			}
	        
	        
	       
        
        } else {
        	facesMessages.addFromResourceBundle(Severity.ERROR,"ERROR: El tipo de archivo requerido es ZIP");
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

	
	
	public void progressListenerFIPZIP(EventObject event) {
        InputFile ifile = (InputFile) event.getSource();
        fileProgress = ifile.getFileInfo().getPercent();
	}
	
	@Remove
	public void destroy(){};
	
	/*
	 * Evalua un XPath
	 */
	public NodeList evaluateXPath(String xpathString) { 
		NodeList nodes=null;
		XPath xpath;
	    XPathExpression expr;
	    
	    // Creamos objetos para el xpath
		XPathFactory factory = XPathFactory.newInstance();
	    xpath = factory.newXPath();
	    
		try {
			expr = xpath.compile(xpathString);
		    Object result = expr.evaluate(xmlDoc, XPathConstants.NODESET);
		    nodes = (NodeList) result;
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return nodes;
	}
	
	public Map<String,Object> nodeToMap(Node node ) {
		
		// Cogemos los atributos del nodo
		NamedNodeMap attrs = node.getAttributes();
		Map<String,Object> attrMap = new HashMap<String, Object>();

		// Por cada atributo lo vamos metiendo en el map
		Attr attr;
		if (attrs!=null) {
			for (int a = 0; a < attrs.getLength(); a++) {
				attr = (Attr) attrs.item(a);
				attrMap.put(attr.getName(), attr.getValue());
			}
		}
		
		return attrMap;
	}
	
	private void descomprimir(String zipName, String zipFIPName) throws EditorFIPException {
		try {
			
			
			FileInputStream fin = new FileInputStream(zipName+ File.separator + zipFIPName);
			ZipInputStream zis = new ZipInputStream(fin);
			
            log.info("Descomprimiendo fichero zip " + zipName);

            ZipEntry entry;
            FileOutputStream fout;
            BufferedOutputStream bos;
            int size;
            File f;
            byte[] buffer = new byte[4096];
            
            int total = 0;
            int c = 0;
            
            // Lo recorro una vez para ver su tamaÃ±o
            while ((entry = zis.getNextEntry()) != null) {
            	total++;
            	zis.closeEntry();
            }
            zis.close();
            fin.close();
           
            fin = new FileInputStream(zipName+ File.separator + zipFIPName);
            zis = new ZipInputStream(fin);
            
            // Luego procedo a descomprimir
            while ((entry = zis.getNextEntry()) != null) {
            	c++;
                log.info("Descomprimiendo el fichero " + entry.getName() + " " + c +"/"+total);
                
                if (entry.isDirectory()) {
                	f = new File(zipName + File.separator + entry.getName());
                	if (!f.exists()) {
                		if (!f.mkdirs()) {
                			log.warn("No se ha podido crear el directorio " + 
                					zipName + 
                					File.separator + 
                					entry.getName());
                		}
                	}
                	
                } else{
                	// En ocasiones un archivo se presenta antes que su directorio.
                	// esta comprobaciÃ³n nos evitarÃ¡ sustos.
                	f = new File(zipName + File.separator + entry.getName());
                	f = new File(f.getParent());
                	
                	if (!f.exists()) {
                		if (!f.mkdirs()) {
                			log.warn("No se ha podido crear el directorio " + 
                					f.getPath());
                		}
                	}
                	
                	fout = new FileOutputStream(zipName + File.separator + entry.getName());
                	
                	bos = new BufferedOutputStream(fout, buffer.length);
                	while ((size = zis.read(buffer, 0, buffer.length)) != -1) {
                		bos.write(buffer, 0, size);
                    }
                	bos.flush();
                    bos.close();
                    zis.closeEntry();
                    fout.close();
                }   
                
            }
            zis.close();
            fin.close();
        } catch (IOException ioe) {
            log.error("Se ha producido un error descomprimiendo el fichero zip. ", ioe);
            
            throw new EditorFIPException("Fichero ZIP invÃ¡lido ");
        }
	}
	
	 private void unzip(String strZipFile) {
         
         try
         {
                 /*
                  * STEP 1 : Create directory with the name of the zip file
                  *
                  * For e.g. if we are going to extract c:/demo.zip create c:/demo
                  * directory where we can extract all the zip entries
                  *
                  */
                 File fSourceZip = new File(strZipFile);
                 String zipPath = strZipFile.substring(0, strZipFile.length()-4);
                 File temp = new File(zipPath);
                 temp.mkdir();
                 log.info(zipPath + " created");
                
                 /*
                  * STEP 2 : Extract entries while creating required
                  * sub-directories
                  *
                  */
                 ZipFile zipFile = new ZipFile(fSourceZip);
                 Enumeration e = zipFile.entries();
                
                 while(e.hasMoreElements())
                 {
                         ZipEntry entry = (ZipEntry)e.nextElement();
                         File destinationFilePath = new File(zipPath,entry.getName());

                         //create directories if required.
                         destinationFilePath.getParentFile().mkdirs();
                        
                         //if the entry is directory, leave it. Otherwise extract it.
                         if(entry.isDirectory())
                         {
                                 continue;
                         }
                         else
                         {
                                 log.info("Extracting " + destinationFilePath);
                                
                                 /*
                                  * Get the InputStream for current entry
                                  * of the zip file using
                                  *
                                  * InputStream getInputStream(Entry entry) method.
                                  */
                                 BufferedInputStream bis = new BufferedInputStream(zipFile.getInputStream(entry));
                                                                                                                
                                 int b;
                                 byte buffer[] = new byte[1024];

                                 /*
                                  * read the current entry from the zip file, extract it
                                  * and write the extracted file.
                                  */
                                 FileOutputStream fos = new FileOutputStream(destinationFilePath);
                                 BufferedOutputStream bos = new BufferedOutputStream(fos,
                                                                 1024);

                                 while ((b = bis.read(buffer, 0, 1024)) != -1) {
                                                 bos.write(buffer, 0, b);
                                 }
                                
                                 //flush the output stream and close it.
                                 bos.flush();
                                 bos.close();
                                
                                 //close the input stream.
                                 bis.close();
                         }
                 }
         }
         catch(IOException ioe)
         {
                 log.error("IOError :" + ioe);
         }
        
 }
}

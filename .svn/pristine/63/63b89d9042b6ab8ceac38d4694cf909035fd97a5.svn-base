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

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.EventObject;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.ejb.Remove;
import javax.ejb.Stateless;
import javax.faces.event.ActionEvent;
import javax.persistence.EntityManager;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
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
import org.xml.sax.SAXException;

import com.icesoft.faces.component.inputfile.FileInfo;
import com.icesoft.faces.component.inputfile.InputFile;
import com.mitc.redes.editorfip.entidades.rpm.gestionfip.FipsCargados;
import com.mitc.redes.editorfip.excepciones.EditorFIPException;

import es.mitc.redes.urbanismoenred.data.fip.FIP;

@Stateless
@Name ("cargadorArchivo")
public class CargadorArchivoBean implements CargadorArchivo{

	@In 
    EntityManager entityManager;
	
	@In(create=true)
    FacesMessages facesMessages;
	
	
    @In (create = true)
    ServicioCargaXML servicioCargaXML;
	
	@Logger private Log log;
	
	private FileInfo currentFile;
	private int fileProgress;
	private Document xmlDoc;

	public FileInfo getCurrentFile() {
		return currentFile;
	}


    public int getFileProgress() {
        return fileProgress;
    }

	public void uploadActionListener(ActionEvent actionEvent) {

		log.info("Subiendo fichero ...");
		
		// Datos del fichero reciŽn subido
        InputFile inputFile = (InputFile) actionEvent.getSource();
        currentFile = inputFile.getFileInfo();
        String clientSideFileName = currentFile.getFileName();
        log.info("Fichero subido: " + clientSideFileName);
        File uploadedFile = currentFile.getFile();
        //log.debug("EEEEEEEEEEEEEE-" + clientSideFileName.substring(clientSideFileName.length()-4, clientSideFileName.length()-1) + "-EEEE");
   
        
        // Extensi—n del fichero subido
        String extension = clientSideFileName.substring(clientSideFileName.length()-3, clientSideFileName.length());
        log.info("Extensi—n del fichero: " + extension);
        
        // Comprobamos que la extensi—n es correcta
        if (extension.equals("xml")) {

	        //File uploadDirectory = uploadedFile.getParentFile();
	
	        // Nombre del fichero
	        UUID uuid = UUID.randomUUID();
	        String newFileName = "fip-" + uuid.toString() + ".xml";

        	// Directorio donde se almacenar‡n los fips1
        	String fips1Dir = System.getProperty("jboss.home.dir") + File.separator + "var" + File.separator + "FIPs1";
        	log.info("Directorio de los FIPs 1: " + fips1Dir);
        	File newFile = new File(fips1Dir, newFileName);
        	
	        // Mover el fichero subido al sitio donde estar‡n todos los ficheros fips1
	        uploadedFile.renameTo(newFile);
	        log.info("Fichero movido corrŽctamente a " + fips1Dir);
	        
	        try {
		    
	        	 FIP fip = null;
	        	fip = servicioCargaXML.obtenerObjetoFIPdelXML( newFile.getAbsolutePath() );
				
				// Cogemos el ‡mbito
				if(fip.getPLANEAMIENTOENCARGADO()!=null) {
 	 	   			
					int idambito = new Integer(fip.getPLANEAMIENTOENCARGADO().getAmbito());
 					
 					log.info("Id de ‡mbito del FIP1: " + idambito);
					
					// Creamos nuevo registro en la tabla de FIPs1 subidos
			        FipsCargados fc = new FipsCargados();
			        fc.setIdambito(idambito);
			        fc.setConsolidado(false);
			        fc.setIdentificador(newFileName);
			        //fc.setRuta(uploadDirectory.getPath());
			        fc.setRuta(System.getProperty("jboss.home.dir") + File.separator + "var" + File.separator + "FIPs1");
			        fc.setFechaIntroduccion(new Date());
			        
			        // Persistimos
			        log.info("Guardando info del nuevo fichero subido en BD ... ");
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

        
        } else {
        	facesMessages.addFromResourceBundle("tipo_de_archivo_no_xml", null);
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
}

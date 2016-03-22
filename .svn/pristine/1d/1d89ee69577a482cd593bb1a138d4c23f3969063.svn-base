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

package com.mitc.redes.editorfip.servicios.gestionfip.obtencionfip;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javax.ejb.Stateless;
import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.log.Log;

import com.mitc.redes.editorfip.excepciones.EditorFIPException;

import es.mitc.redes.urbanismoenred.data.fip.FIP;

@Stateless
@Name ("servicioCargaXML")
public class ServicioCargaXMLBean implements ServicioCargaXML{

	@Logger private Log log;

	   /*
	    * (non-Javadoc)
	    * @see es.mitc.redes.urbanismoenred.servicios.comunes.ServicioGestionXMLLocal#obtenerObjetoFIPdelXML(java.lang.String)
	    */
	    
	    public FIP obtenerObjetoFIPdelXML(String xmlFile) throws EditorFIPException {
	    	return obtenerObjetoFIPdelXML(xmlFile, true);
		}

	    /*
	     * (non-Javadoc)
	     * @see es.mitc.redes.urbanismoenred.servicios.basicos.validacion.ServicioGestionXMLLocal#obtenerObjetoFIPdelXML(java.lang.String, boolean)
	     */
		
		public FIP obtenerObjetoFIPdelXML(String xmlFile, boolean validar)
				throws EditorFIPException {
			log.debug("Servicio que obtiene el objeto FIP del fichero xml: " + xmlFile);
	    	FIP fip = null;
			JAXBContext jaxbContext = null;
			try {
				jaxbContext = JAXBContext.newInstance("es.mitc.redes.urbanismoenred.data.fip");
			
				Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
				
				// Si está activada la validación se valida el xml contra el esquema
				if (validar) {
					SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI); 
					String sourcexsdfipfilepath = System.getProperty("jboss.home.dir") + File.separator + "var" + File.separator + "xsd"+ File.separator + "fip.xsd";
			        Schema schema = sf.newSchema(new File(sourcexsdfipfilepath)); 
			        unmarshaller.setSchema(schema);
				}
				FileInputStream fis = new FileInputStream( xmlFile );
				fip = (FIP) unmarshaller.unmarshal( fis );
			} catch (JAXBException e) {
				String mensaje = "Error desconocido, consulte al administrador";
				if (e.getMessage() != null) {
					mensaje = e.getMessage();
				} else if (e.getLinkedException() != null) {
					mensaje = e.getLinkedException().getMessage();
				}
				log.error("JAXB Error parsing xml: " + mensaje, e );
				throw new EditorFIPException("Error al procesar el XML. Causa:" + mensaje);
			} catch (FileNotFoundException e) {
				log.error("Error loading file, file no found: " + e.getMessage(), e );
				throw new EditorFIPException("No se puede cargar el XML porque no existe el fichero " + xmlFile);
			} catch (Exception e) {
				log.error("Unknow error when getting xml file to object: " + e.getMessage(), e );
				throw new EditorFIPException("Unknow error when getting xml file to object: " + xmlFile);
			}
		
			log.debug("Objeto FIP creado a partir del XML");
		
			return fip;
		}
}

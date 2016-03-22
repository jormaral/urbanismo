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

package com.mitc.redes.editorfip.servicios.informacionfip.consultafichaurbanisticas;


import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;


/**
 *
 * @author a.centeno
 */
public class XMLws {

    public static String XmlDocToString(Document aXML){
         String xmlString;

         try
         {
           Transformer transformer = TransformerFactory.newInstance().newTransformer();
           StreamResult result = new StreamResult(new StringWriter());
           DOMSource source = new DOMSource(aXML);
           transformer.transform(source, result);

           xmlString = result.getWriter().toString();
         }
         catch(Exception ex)
         {
            xmlString= "";
         }
         return xmlString;
    }

    public static Document XmlDocFromString(String aXML){
        Document doc=null;
        
        try {
            // Create a Reader Object
            Reader xmlReader = new StringReader(aXML);
            // Create an InputSource
            InputSource is = new InputSource(xmlReader);
            // Get the DocumentBuilder
            DocumentBuilder docBuild = (DocumentBuilder) ((DocumentBuilderFactory)DocumentBuilderFactory.newInstance()).newDocumentBuilder();
            // Parse the XML file
            doc = (Document) docBuild.parse (is);

        } catch (SAXException ex) {
            Logger.getLogger(XMLws.class.getName()).log(Level.SEVERE, "Error parseando: {0}", aXML);
        } catch (IOException ex) {
            Logger.getLogger(XMLws.class.getName()).log(Level.SEVERE,  "Error parseando: {0}", aXML);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(XMLws.class.getName()).log(Level.SEVERE,  "Error parseando: {0}", aXML);
        }
        return doc;
    }

    public static NodeList findNode(String busqueda, Element aElem) throws Exception {
        try{


            XPath xpath = XPathFactory.newInstance().newXPath();
            String expression = busqueda;
            NodeList nodes = (NodeList) xpath.evaluate(expression, aElem,XPathConstants.NODESET);

            return nodes;
        }catch (Exception ex){
            return null;
        }

}


    public static Document generarXMLDOC(String rootName){
        Document dom=null;

        try
        {
              //GENERO LA IMPLEMENTACION
                  DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                  factory.setNamespaceAware(true);
                  DocumentBuilder builder = factory.newDocumentBuilder();
                  DOMImplementation impl = builder.getDOMImplementation();

              //GENERO EL DOCUMENTO
                  dom = impl.createDocument(null,rootName, null);

        }
        catch(ParserConfigurationException p)
        {
            p.printStackTrace();
            dom=null;
        }
        return dom;
    }
}



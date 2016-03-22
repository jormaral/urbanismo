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


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;



public class XML {

    

    public static String XmlDocToString(Document aXML) {
        String xmlString;

        try {
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            StreamResult result = new StreamResult(new StringWriter());
            DOMSource source = new DOMSource(aXML);
            transformer.transform(source, result);

            xmlString = result.getWriter().toString();
        } catch (Exception ex) {
        	ex.printStackTrace();
            xmlString = "";
        }
        return xmlString;
    }

    public static Document generarXMLDOC(String rootName) {
        Document dom = null;

        try {
            //GENERO LA IMPLEMENTACION
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            DocumentBuilder builder = factory.newDocumentBuilder();
            DOMImplementation impl = builder.getDOMImplementation();

            //GENERO EL DOCUMENTO

            dom = impl.createDocument(null, rootName, null);
            dom.createCDATASection(rootName);

        } catch (ParserConfigurationException p) {
        	
            p.printStackTrace();
        }
        return dom;
    }

    public static void generarXMLFile_De_XMLDOC(Document doc, String rutaSalida, String nombreArchivo) throws TransformerConfigurationException, TransformerException, FileNotFoundException, IOException, TransformerConfigurationException, TransformerException {
        //TRANSFORMO EL XMLDOM A STRING

        TransformerFactory tfactory = TransformerFactory.newInstance();
        Transformer transformer = tfactory.newTransformer();

        StringWriter sw = new StringWriter();
        StreamResult result = new StreamResult(sw);

        DOMSource source = new DOMSource(doc);
        transformer.transform(source, result);
        String xmlString = sw.toString();

        //GENERO EL FICHERO XML EN LA RUTA RECIBIDA
        File fileCarpetas = new File(rutaSalida);
        File file = new File(fileCarpetas, nombreArchivo);
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));

        bw.write(xmlString);
        bw.flush();
        bw.close();
    }
}


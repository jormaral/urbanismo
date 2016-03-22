package es.mitc.redes.urbanismoenred.utils.recursos.xml;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import org.apache.log4j.Level;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.apache.log4j.Logger;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 *
 * @author a.centeno
 */
public class XML {

    private static final Logger log = Logger.getLogger(XML.class);

//    public static String XmlDocToString(Document aXML) {
//        String xmlString;
//
//        try {
//            Transformer transformer = TransformerFactory.newInstance().newTransformer();
//            StreamResult result = new StreamResult(new StringWriter());
//            DOMSource source = new DOMSource(aXML);
//            transformer.transform(source, result);
//
//            xmlString = result.getWriter().toString();
//        } catch (Exception ex) {
//            log.error("Se ha producido un error creando el documento XML. " + ex);
//            xmlString = "";
//        }
//        return xmlString;
//    }

    public static String XmlDocToString(Node nodo) {
        String xmlString;

        try {
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            StreamResult result = new StreamResult(new StringWriter());
            DOMSource source = new DOMSource(nodo);
            transformer.transform(source, result);

            xmlString = result.getWriter().toString();
        } catch (Exception ex) {
            log.error("Se ha producido un error creando el documento XML. " + ex);
            xmlString = "";
        }
        return xmlString;
    }
    
    public static Element AddNode(Element padre, String nombre) {
        Element aNode = padre.getOwnerDocument().createElement(nombre);
        padre.appendChild(aNode);
        return aNode;
    }

    public static Element AddNode(DocumentFragment padre, String nombre) {
        Element aNode = padre.getOwnerDocument().createElement(nombre);
        padre.appendChild(aNode);
        return aNode;
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
            log.error("Se ha producido un error creando el documento XML. \n" + p.getMessage(), p);
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

    public static NodeList findNode(String busqueda, Element aElem) throws Exception {
        try {
            XPath xpath = XPathFactory.newInstance().newXPath();
            String expression = busqueda;
            NodeList nodes = (NodeList) xpath.evaluate(expression, aElem, XPathConstants.NODESET);

            return nodes;
        } catch (Exception ex) {
            return null;
        }
    }

    public static Document XmlDocFromString(String aXML) {
        Document doc = null;

        try {
            // Create a Reader Object
            Reader xmlReader = new StringReader(aXML);
            // Create an InputSource
            InputSource is = new InputSource(xmlReader);
            // Get the DocumentBuilder
            DocumentBuilder docBuild = (DocumentBuilder) ((DocumentBuilderFactory) DocumentBuilderFactory.newInstance()).newDocumentBuilder();
            // Parse the XML file
            doc = (Document) docBuild.parse(is);

        } catch (SAXException ex) {
            log.error("Error parseando: " + aXML);
        } catch (IOException ex) {
            log.error("Error parseando: " + aXML);
        } catch (ParserConfigurationException ex) {
            log.error("Error parseando: " + aXML);
        }
        return doc;
    }
}

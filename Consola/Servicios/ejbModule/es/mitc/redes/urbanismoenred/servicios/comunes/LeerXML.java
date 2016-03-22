package es.mitc.redes.urbanismoenred.servicios.comunes;

import java.io.File;
import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class LeerXML {

    public static Document leer(String Origen) {
        Document doc = null;
        try {

            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            doc = docBuilder.parse(new File(Origen.trim()));

            // normalize text representation
            doc.getDocumentElement().normalize();
            System.out.println("********** Fichero Leido **********");
            System.out.println(doc.getDocumentElement().getNodeName());
        } catch (SAXParseException err) {
            System.out.println("** Parsing error" + ", line " + err.getLineNumber() + ", uri " + err.getSystemId());
            System.out.println(" " + err.getMessage());

        } catch (SAXException e) {
            Exception x = e.getException();
            ((x == null) ? e : x).printStackTrace();

        } catch (Throwable t) {
            t.printStackTrace();
        }
        return doc;
    }
}
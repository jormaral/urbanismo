/*
 * Copyright 2011 red.es
 * Autores: Arnaiz Consultores.
 *
 ** Licencia con arreglo a la EUPL, Versión 1.1 o -en cuanto
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
package es.mitc.redes.urbanismoenred.consola.visor.acciones;

import es.mitc.redes.urbanismoenred.consola.visor.IAccion;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Tramite;
import es.mitc.redes.urbanismoenred.servicios.configuracion.ConfiguracionVisorLocal;
import es.mitc.redes.urbanismoenred.servicios.planeamiento.ServicioPlaneamientoLocal;
import es.mitc.redes.urbanismoenred.utils.configuracion.RedesConfig;
import es.mitc.redes.urbanismoenred.utils.recursos.textos.Textos;
import es.mitc.redes.urbanismoenred.utils.recursos.xml.XML;
import java.io.*;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * @author Arnaiz Consultores
 *
 */
@Stateless(name = "GET_CAPAS_VISOR")
public class GetCapasVisor implements IAccion {

    /*
     * (non-Javadoc) @see
     * es.mitc.redes.urbanismoenred.consola.planeamiento.IAccion#ejecutar(javax.servlet.http.HttpServletRequest,
     * javax.servlet.http.HttpServletResponse)
     */

    @Override
    public void ejecutar(HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        String directorio = RedesConfig.getREDES_PATH() + File.separator + "conf" + File.separator + "consola";

        File dir = new File(directorio);

        File file = new File(dir, "capas.xml");

        if (!file.exists()) {
            Logger.getLogger(GetCapasVisor.class.getName()).log(Level.ERROR, "El archivo de configuración perfiles.xml (" + file.getAbsolutePath() + ")" + " no existe");
            response.getWriter().print("");
        }
        InputStream configFIS;
        try {
            configFIS = new FileInputStream(file);
            Document xml = readXml(configFIS);
            response.getWriter().print(XML.XmlDocToString(xml));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(GetCapasVisor.class.getName()).log(Level.ERROR, "No se encuentra el fichero " + file.toString(), ex);
            response.getWriter().print("");
        } catch (Exception ex) {
            Logger.getLogger(GetCapasVisor.class.getName()).log(Level.ERROR, "Error leyendo el fichero " + file.toString(), ex);
            response.getWriter().print("");
        }

        response.getWriter().flush();
    }

    private Document readXml(InputStream is) throws SAXException, IOException,
            ParserConfigurationException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        dbf.setValidating(false);
        dbf.setIgnoringComments(false);
        dbf.setIgnoringElementContentWhitespace(true);
        dbf.setNamespaceAware(true);
        // dbf.setCoalescing(true);
        // dbf.setExpandEntityReferences(true);

        DocumentBuilder db = null;
        db = dbf.newDocumentBuilder();
        //db.setEntityResolver(new NullResolver());
        // db.setErrorHandler( new MyErrorHandler());

        return db.parse(is);
    }
}

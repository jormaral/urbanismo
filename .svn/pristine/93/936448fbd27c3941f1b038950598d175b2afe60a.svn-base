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
import es.mitc.redes.urbanismoenred.utils.recursos.textos.Textos;
import es.mitc.redes.urbanismoenred.utils.recursos.xml.XML;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;

import java.io.InputStreamReader;
import java.net.URL;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * @author Arnaiz Consultores
 *
 */
@Stateless(name = "GET_PERFILES_VISOR")
public class GetPerfilesVisor implements IAccion {

    /* (non-Javadoc)
     * @see es.mitc.redes.urbanismoenred.consola.planeamiento.IAccion#ejecutar(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @EJB
    private ServicioPlaneamientoLocal servicioPlanes;

    @EJB
    private ConfiguracionVisorLocal confVisor;
    
    @Override
    public void ejecutar(HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        try {
            int idTramite = Integer.valueOf(request.getParameter("idTramite"));
            InputStream inStr=confVisor.getPerfiles();
            Tramite tramite = servicioPlanes.get(Tramite.class, idTramite);
            int idAmbito = tramite.getPlan().getIdambito();
            Document doc =readXml(inStr);// XML..XmlDocFromString(ret);
            NodeList lista = XML.findNode("/visores/visor[@idAmbito=" + idAmbito + "]", doc.getDocumentElement());
            if (lista.getLength()==0){
                lista = XML.findNode("/visores/visor", doc.getDocumentElement());
            }
            if (lista.getLength()>0){
                response.getWriter().print(XML.XmlDocToString(lista.item(0)));  
            }else{
                Logger.getLogger(GetPerfilesVisor.class.getName()).log(Level.ERROR, "Sin configuraciones en perfiles.xml");
                response.getWriter().print("");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            response.getWriter().print("");
        }
        response.getWriter().flush();
    }
    
    private  Document readXml(InputStream is) throws SAXException, IOException,
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

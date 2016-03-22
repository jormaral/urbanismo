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
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Plan;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Tramite;
import es.mitc.redes.urbanismoenred.servicios.planeamiento.ServicioPlaneamientoLocal;
import es.mitc.redes.urbanismoenred.utils.recursos.textos.Textos;
import es.mitc.redes.urbanismoenred.utils.recursos.xml.XML;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.WebServiceException;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

/**
 * @author Arnaiz Consultores
 *
 */
@Stateless(name = "CREAR_WMS")
public class CrearWMS implements IAccion {

    /* (non-Javadoc)
     * @see es.mitc.redes.urbanismoenred.consola.planeamiento.IAccion#ejecutar(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */

    @Override
    public void ejecutar(HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        String urlWS = request.getSession().getServletContext().getInitParameter("wsdl-url");
        String idHoja = request.getParameter("idHoja");
        es.mitc.redes.urbanismoenred.consola.visor.webservices.UrbrWS_Service service=null;
        try{
            service = new es.mitc.redes.urbanismoenred.consola.visor.webservices.UrbrWS_Service(new URL(urlWS),new QName("http://serviciosweb.urbanismoenred.redes.mitc.es/", "urbrWS"));
        }catch (WebServiceException ex){
            service = new es.mitc.redes.urbanismoenred.consola.visor.webservices.UrbrWS_Service(new URL(urlWS),new QName("http://serviciosweb.urbanismoenred.redes.mitc.es/", "urbrWSService"));
        }
        es.mitc.redes.urbanismoenred.consola.visor.webservices.UrbrWS port = service.getUrbrWSPort();
        ((BindingProvider) port).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, urlWS);
        String result = port.creaWMSDoc(Integer.valueOf(idHoja));
        out.print(result);
        out.close();
    }
}

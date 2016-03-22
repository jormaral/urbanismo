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
package DATA;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.ws.BindingProvider;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.Set;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.namespace.QName;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.InputStreamRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.io.IOUtils;

public class DAO {

    public static String getVersion(String urlWS) {
        String version = "V: 2.0.0";
        return version;
    }

    public static String consultaGrafica(String urlWS, String wkt, String srs, String idioma) {
        String result = "";

        try { // Call Web Service Operation
            es.mitc.redes.urbanismoenred.serviciosweb.UrbrWS_Service service = new es.mitc.redes.urbanismoenred.serviciosweb.UrbrWS_Service(new URL(urlWS), new QName("http://serviciosweb.urbanismoenred.redes.mitc.es/", "urbrWS"));
            es.mitc.redes.urbanismoenred.serviciosweb.UrbrWS port = service.getUrbrWSPort();
            ((BindingProvider) port).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, urlWS);

            // TODO process result here
            result = port.consultaGrafica(wkt, srs, idioma);
        } catch (Exception ex) {
            result = "ERROR.consultaGrafica." + ex.getMessage();
            ex.printStackTrace();
        }
        return result;

    }

    public static String consultaGraficaExtendida(String urlWS, String wkt, String srs, String idioma) {
        String result = "";

        try { // Call Web Service Operation
            es.mitc.redes.urbanismoenred.serviciosweb.UrbrWS_Service service = new es.mitc.redes.urbanismoenred.serviciosweb.UrbrWS_Service(new URL(urlWS), new QName("http://serviciosweb.urbanismoenred.redes.mitc.es/", "urbrWS"));
            es.mitc.redes.urbanismoenred.serviciosweb.UrbrWS port = service.getUrbrWSPort();
            ((BindingProvider) port).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, urlWS);

            // TODO process result here
            result = port.consultaGraficaExtendida(wkt, srs, idioma);
        } catch (Exception ex) {
            result = "ERROR.consultaGrafica." + ex.getMessage();
            ex.printStackTrace();
        }
        return result;

    }
    
    public static String creaWMSDoc(String urlWS, String idHoja) {
        String result = "";


        try { // Call Web Service Operation
            es.mitc.redes.urbanismoenred.serviciosweb.UrbrWS_Service service = new es.mitc.redes.urbanismoenred.serviciosweb.UrbrWS_Service(new URL(urlWS), new QName("http://serviciosweb.urbanismoenred.redes.mitc.es/", "urbrWS"));
            es.mitc.redes.urbanismoenred.serviciosweb.UrbrWS port = service.getUrbrWSPort();
            ((BindingProvider) port).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, urlWS);
            // TODO initialize WS operation arguments here

            // TODO process result here
            result = port.creaWMSDoc(Integer.valueOf(idHoja));
            System.out.println("Result = " + result);
        } catch (Exception ex) {
            result = "ERROR.creaWMSDoc." + ex.getMessage();
        }

        return result;

    }

    public static String getLeyenda(String urlWS, String idHoja) {
        String result = "";


        try { // Call Web Service Operation
            es.mitc.redes.urbanismoenred.serviciosweb.UrbrWS_Service service = new es.mitc.redes.urbanismoenred.serviciosweb.UrbrWS_Service(new URL(urlWS), new QName("http://serviciosweb.urbanismoenred.redes.mitc.es/", "urbrWS"));
            es.mitc.redes.urbanismoenred.serviciosweb.UrbrWS port = service.getUrbrWSPort();
            ((BindingProvider) port).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, urlWS);
            // TODO initialize WS operation arguments here

            // TODO process result here
            result = port.getLeyenda(Integer.valueOf(idHoja));
            System.out.println("Result = " + result);
        } catch (Exception ex) {
            result = "ERROR.creaWMSDoc." + ex.getMessage();
        }

        return result;

    }

    public static void redirigePost(HttpServletRequest request, HttpServletResponse response) {

        String requestURL = request.getParameter("url");
        if (requestURL == null) {
            System.out.println("requestURL could not be resolved.");
            return;
        }

        try {

            HttpClient client = new HttpClient();
            if (request.getMethod().toLowerCase().equals("get")) {
                String decodedURL = URLDecoder.decode(requestURL, "UTF-8");
                StringBuilder getUrl = new StringBuilder(decodedURL);
                Set<String> parameters = request.getParameterMap().keySet();
                boolean first = true;
                for (String p : parameters) {
                    if (p.equals("url")) {
                        continue; // skip the url parameter
                    }
                    if (p.startsWith("wicket:")) {
                        continue; // skip the wicket parameters
                    }
                    String value = request.getParameter(p);
                    if (first) {
                        getUrl.append("?");
                        first = false;
                    } else {
                        getUrl.append("&");
                    }
                    getUrl.append(p);
                    getUrl.append("=");
                    getUrl.append(URLEncoder.encode(value, "UTF-8"));
                }
                //System.out.println("Get = " + getUrl.toString());
                GetMethod getMethod = new GetMethod(getUrl.toString());
                int proxyResponseCode = client.executeMethod(getMethod);
                //System.out.println("redirected get, code = " + proxyResponseCode);
                if (proxyResponseCode == 200) {
                    // Pass response headers back to the client
                    Header[] headerArrayResponse = getMethod.getResponseHeaders();
                    for (Header header : headerArrayResponse) {
                        response.setHeader(header.getName(), header.getValue());
                    }
                    // Send the content to the client
                    InputStream inputStreamProxyResponse = getMethod.getResponseBodyAsStream();
                    ServletOutputStream output = response.getOutputStream();
                    output.flush();
                    output.write(IOUtils.toByteArray(inputStreamProxyResponse));
                } else {
                    //System.out.println("redirected get, code = " + proxyResponseCode);
                }
            } else if (request.getMethod().toLowerCase().equals("post")) {
                // need to get the first line and get the url.
                if (request.getContentType().startsWith("application/xml") || request.getContentType().startsWith("application/json")) {
                    String decodedURL = URLDecoder.decode(requestURL, "UTF-8");
                    PostMethod pm = new PostMethod(decodedURL);
                    Set<String> parameters = request.getParameterMap().keySet();

                    for (String p : parameters) {
                        String value = request.getParameter(p);
                        pm.setParameter(p, value);
                    }
                    pm.setContentChunked(true);
                    /*
                    int test=0;
                    if (test==1){     
                    BufferedReader data = request.getReader();
                    try {
                    StringBuilder spec = new StringBuilder();
                    String cur;
                    while ((cur = data.readLine()) != null) {
                    spec.append(cur).append("\n");
                    }
                    System.out.println(spec.toString());
                    } catch (Exception ex){
                    System.out.println(ex.toString());     
                    
                    } finally {
                    if(data != null) {
                    data.close();
                    }
                    }
                    }
                     */
                    InputStreamRequestEntity iReq = new InputStreamRequestEntity(request.getInputStream());
                    pm.setRequestEntity(iReq);
                    int proxyResponseCode = client.executeMethod(pm);
                    //System.out.println("redirected post, code = " + proxyResponseCode);
                    if (proxyResponseCode == 200) {
                        // Pass response headers back to the client
                        Header[] headerArrayResponse = pm.getResponseHeaders();
                        for (Header header : headerArrayResponse) {
                            response.setHeader(header.getName(), header.getValue());
                        }
                        // Send the content to the client

                        InputStream inputStreamProxyResponse = pm.getResponseBodyAsStream();
                        ServletOutputStream output = response.getOutputStream();
                        output.flush();
                        output.write(IOUtils.toByteArray(inputStreamProxyResponse));
                    } else {
                        //System.out.println("redirected get, code = " + proxyResponseCode);
                        PrintWriter output = response.getWriter();
                        output.print(leerURL(requestURL));
                        output.close();
                    }
                } else {
                    //System.out.println("redirected get, code = " + proxyResponseCode);
                    PrintWriter output = response.getWriter();
                    output.print(leerURL(requestURL));
                    output.close();
                }
                //output.flush();
            } else {
                // unsupported
                // fall through
            }

        } catch (Exception e) {
            //System.out.println("getInputStream() failed");
            System.out.println(e.toString());
        }

    }

    public static String leerURL(String urlString) {
        String estado = "error COMIENZO - " + urlString;

        if (urlString != null && !urlString.isEmpty()) {
            try {
                //Indicamos la URL donde nos conectamos

                URL url = new URL(urlString);
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(
                        url.openStream(), Charset.forName("UTF-8")));

                String inputLine;
                estado="";
                while ((inputLine = in.readLine()) != null) {
                    estado+=inputLine;
                }

                in.close();
            } catch (MalformedURLException me) {
                estado = "ERROR.URL." + me.getMessage();
            } catch (IOException ioe) {
                estado = "Error.IO." + ioe.getMessage();
            } catch (Exception ex) {
                estado = "Error: " + ex.getMessage();
            }
        }
        return estado;
    }

    public static String entidadesFromNombre(String urlWS, String nombre, String idAmbito) {
        String result = "";
        try { // Call Web Service Operation
            es.mitc.redes.urbanismoenred.serviciosweb.UrbrWS_Service service = new es.mitc.redes.urbanismoenred.serviciosweb.UrbrWS_Service(new URL(urlWS), new QName("http://serviciosweb.urbanismoenred.redes.mitc.es/", "urbrWS"));
            es.mitc.redes.urbanismoenred.serviciosweb.UrbrWS port = service.getUrbrWSPort();
            ((BindingProvider) port).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, urlWS);
            // TODO process result here
            result = port.entidadesFromNombre(nombre, Integer.valueOf(idAmbito).intValue());
        } catch (Exception ex) {
            result = "ERROR.entidadesFromNombre." + ex.getMessage();
        }
        return result;
    }

    public static String planesFromNombre(String urlWS, String nombre, String idAmbito) {

        String result = "";
        try { // Call Web Service Operation
            es.mitc.redes.urbanismoenred.serviciosweb.UrbrWS_Service service = new es.mitc.redes.urbanismoenred.serviciosweb.UrbrWS_Service(new URL(urlWS), new QName("http://serviciosweb.urbanismoenred.redes.mitc.es/", "urbrWS"));
            es.mitc.redes.urbanismoenred.serviciosweb.UrbrWS port = service.getUrbrWSPort();
            ((BindingProvider) port).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, urlWS);
            // TODO process result here
            result = port.planesFromNombre(nombre, Integer.valueOf(idAmbito).intValue());
        } catch (Exception ex) {
            result = "ERROR.planesFromNombre." + ex.getMessage();
        }
        return result;
    }

    public static long DameIdAmbito(String urlWS, double x, double y, String srs) {

        int result;

        try { // Call Web Service Operation
            es.mitc.redes.urbanismoenred.serviciosweb.UrbrWS_Service service = new es.mitc.redes.urbanismoenred.serviciosweb.UrbrWS_Service(new URL(urlWS), new QName("http://serviciosweb.urbanismoenred.redes.mitc.es/", "urbrWS"));
            es.mitc.redes.urbanismoenred.serviciosweb.UrbrWS port = service.getUrbrWSPort();
            ((BindingProvider) port).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, urlWS);

            // TODO process result here
            result = port.idAmbito(x, y, srs);
        } catch (Exception ex) {
            ex.printStackTrace();
            result = -1;
        }

        return result;

    }

    public static String DameAmbitos(String urlWS, String idioma) {
        String result = "";

        try { // Call Web Service Operation
            es.mitc.redes.urbanismoenred.serviciosweb.UrbrWS_Service service = new es.mitc.redes.urbanismoenred.serviciosweb.UrbrWS_Service(new URL(urlWS), new QName("http://serviciosweb.urbanismoenred.redes.mitc.es/", "urbrWS"));
            es.mitc.redes.urbanismoenred.serviciosweb.UrbrWS port = service.getUrbrWSPort();
            ((BindingProvider) port).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, urlWS);
            // TODO process result here
            result = port.ambitosWithPlan(idioma);
        } catch (Exception ex) {
            ex.printStackTrace();
            result = "ERROR.DameAmbitos." + ex.getMessage();
        }

        return result;

    }

    public static String extensionEntidad(String urlWS, String idEntidad, String srs) {
        String result = "";

        try { // Call Web Service Operation
            es.mitc.redes.urbanismoenred.serviciosweb.UrbrWS_Service service = new es.mitc.redes.urbanismoenred.serviciosweb.UrbrWS_Service(new URL(urlWS), new QName("http://serviciosweb.urbanismoenred.redes.mitc.es/", "urbrWS"));
            es.mitc.redes.urbanismoenred.serviciosweb.UrbrWS port = service.getUrbrWSPort();
            ((BindingProvider) port).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, urlWS);
            // TODO process result here
            result = port.extensionEntidad(Integer.valueOf(idEntidad).intValue(), srs);
        } catch (Exception ex) {
            ex.printStackTrace();
            result = "ERROR.extensionEntidad." + ex.getMessage();
        }

        return result;

    }

    public static String extensionPlan(String urlWS, String idEntidad, String srs) {
        String result = "";

        try { // Call Web Service Operation
            es.mitc.redes.urbanismoenred.serviciosweb.UrbrWS_Service service = new es.mitc.redes.urbanismoenred.serviciosweb.UrbrWS_Service(new URL(urlWS), new QName("http://serviciosweb.urbanismoenred.redes.mitc.es/", "urbrWS"));
            es.mitc.redes.urbanismoenred.serviciosweb.UrbrWS port = service.getUrbrWSPort();
            ((BindingProvider) port).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, urlWS);
            // TODO process result here
            result = port.extensionPlan(Integer.valueOf(idEntidad).intValue(), srs);
        } catch (Exception ex) {
            ex.printStackTrace();
            result = "ERROR.extensionPlan." + ex.getMessage();
        }

        return result;

    }

    public static String extensionAmbito(String urlWS, String idEntidad, String srs) {
        String result = "";

        try { // Call Web Service Operation
            es.mitc.redes.urbanismoenred.serviciosweb.UrbrWS_Service service = new es.mitc.redes.urbanismoenred.serviciosweb.UrbrWS_Service(new URL(urlWS), new QName("http://serviciosweb.urbanismoenred.redes.mitc.es/", "urbrWS"));
            es.mitc.redes.urbanismoenred.serviciosweb.UrbrWS port = service.getUrbrWSPort();
            ((BindingProvider) port).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, urlWS);
            // TODO process result here
            result = port.extensionAmbito(Integer.valueOf(idEntidad).intValue(), srs);
        } catch (Exception ex) {
            ex.printStackTrace();
            result = "ERROR.extensionAmbito." + ex.getMessage();
        }

        return result;

    }

    public static String geometriaEntidad(String urlWS, int idEntidad, int maxPuntosGeometriaRespuesta, java.lang.String srs) {
        String result = "";

        try { // Call Web Service Operation
            es.mitc.redes.urbanismoenred.serviciosweb.UrbrWS_Service service = new es.mitc.redes.urbanismoenred.serviciosweb.UrbrWS_Service(new URL(urlWS), new QName("http://serviciosweb.urbanismoenred.redes.mitc.es/", "urbrWS"));
            es.mitc.redes.urbanismoenred.serviciosweb.UrbrWS port = service.getUrbrWSPort();
            ((BindingProvider) port).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, urlWS);
            // TODO process result here
            result = port.geometriaEntidad(idEntidad, maxPuntosGeometriaRespuesta, srs);
        } catch (Exception ex) {
            ex.printStackTrace();
            result = "ERROR.extensionAmbito." + ex.getMessage();
        }

        return result;
    }

    public static String geometriaPlan(String urlWS, int idPlan, java.lang.String srs) {
        String result = "";

        try { // Call Web Service Operation
            es.mitc.redes.urbanismoenred.serviciosweb.UrbrWS_Service service = new es.mitc.redes.urbanismoenred.serviciosweb.UrbrWS_Service(new URL(urlWS), new QName("http://serviciosweb.urbanismoenred.redes.mitc.es/", "urbrWS"));
            es.mitc.redes.urbanismoenred.serviciosweb.UrbrWS port = service.getUrbrWSPort();
            ((BindingProvider) port).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, urlWS);
            // TODO process result here
            result = port.geometriaPlan(idPlan, srs);
        } catch (Exception ex) {
            ex.printStackTrace();
            result = "ERROR.extensionAmbito." + ex.getMessage();
        }

        return result;
    }

    public static String entidadesFromNombrePlan(String urlWS, java.lang.String nombre, java.lang.String nombrePlan, int idAmbito) {

        String result = "";

        try { // Call Web Service Operation
            es.mitc.redes.urbanismoenred.serviciosweb.UrbrWS_Service service = new es.mitc.redes.urbanismoenred.serviciosweb.UrbrWS_Service(new URL(urlWS), new QName("http://serviciosweb.urbanismoenred.redes.mitc.es/", "urbrWS"));
            es.mitc.redes.urbanismoenred.serviciosweb.UrbrWS port = service.getUrbrWSPort();
            ((BindingProvider) port).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, urlWS);
            result = port.entidadesFromNombrePlan(nombre, nombrePlan, idAmbito);
        } catch (Exception ex) {
            ex.printStackTrace();
            result = "ERROR.extensionAmbito." + ex.getMessage();
        }

        return result;
    }
}

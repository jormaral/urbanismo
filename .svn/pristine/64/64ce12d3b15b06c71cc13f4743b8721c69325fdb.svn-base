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
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Arnaiz Consultores
 *
 */
@Stateless(name = "CROSS-DOMAIN")
public class CrossDomain implements IAccion {

    /* (non-Javadoc)
     * @see es.mitc.redes.urbanismoenred.consola.planeamiento.IAccion#ejecutar(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */

    @Override
    public void ejecutar(HttpServletRequest request,
            HttpServletResponse response) throws IOException {

        BufferedInputStream webToProxyBuf = null;
        BufferedOutputStream proxyToClientBuf = null;
        HttpURLConnection con = null;

        try {
            int statusCode;
            int oneByte;
            String methodName;
            String headerText;

            String urlString = request.getParameter("url");


            URL url = new URL(urlString);

            con = (HttpURLConnection) url.openConnection();

            methodName = request.getMethod();
//            con.setRequestMethod(methodName);
//            con.setDoOutput(true);
//            con.setDoInput(true);
//            con.setFollowRedirects(false);
//            con.setUseCaches(true);

//            for (Enumeration e = request.getHeaderNames(); e.hasMoreElements();) {
//                String headerName = e.nextElement().toString();
//                con.setRequestProperty(headerName, request.getHeader(headerName));
//            }

            con.connect();

//            if (methodName.equals("POST")) {
//                BufferedInputStream clientToProxyBuf = new BufferedInputStream(request.getInputStream());
//                BufferedOutputStream proxyToWebBuf = new BufferedOutputStream(con.getOutputStream());
//
//                while ((oneByte = clientToProxyBuf.read()) != -1) {
//                    proxyToWebBuf.write(oneByte);
//                }
//
//                proxyToWebBuf.flush();
//                proxyToWebBuf.close();
//                clientToProxyBuf.close();
//            }

            statusCode = con.getResponseCode();
            response.setStatus(statusCode);

            for (Iterator i = con.getHeaderFields().entrySet().iterator(); i.hasNext();) {
                Map.Entry mapEntry = (Map.Entry) i.next();
                if (mapEntry.getKey() != null) {
                    response.setHeader(mapEntry.getKey().toString(), ((List) mapEntry.getValue()).get(0).toString());
                }
            }

            webToProxyBuf = new BufferedInputStream(con.getInputStream());
            proxyToClientBuf = new BufferedOutputStream(response.getOutputStream());

            while ((oneByte = webToProxyBuf.read()) != -1) {
                proxyToClientBuf.write(oneByte);
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        } finally {
            if (proxyToClientBuf != null) {
                proxyToClientBuf.flush();
                proxyToClientBuf.close();
            }
            if (proxyToClientBuf != null) {
                webToProxyBuf.close();
            }
            if (con != null) {
                con.disconnect();
            }
        }

    }
}

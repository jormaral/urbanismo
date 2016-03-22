/*
 * Copyright 2011 red.es
 * Autores: Arnaiz Consultores.
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
package es.mitc.redes.urbanismoenred.ficha;

import com.vividsolutions.jts.geom.Geometry;
import es.mitc.redes.urbanismoenred.serviciosweb.Textos;
import es.mitc.redes.urbanismoenred.serviciosweb.utils.GeoUtils;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.rmi.PortableRemoteObject;
import javax.servlet.ServletOutputStream;
import javax.sql.DataSource;
import net.sf.jasperreports.engine.*;

/**
 *
 * @author a.centeno
 */
public class FichaGenerica extends HttpServlet {

    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // response.setContentType("text/html;charset=UTF-8");
        //PrintWriter out = response.getWriter();

        //response.setContentType("text/xml");


        ServletOutputStream ouputStream;

        try {
            Logger.getLogger(FichaGenerica.class.getName()).log(Level.INFO, "Peticion de ficha generica");

            Geometry geoConsulta = GeoUtils.GeoFromWKT("POINT(" + request.getParameter("X") + " " + request.getParameter("Y") + ")");

            String srs = request.getParameter("SRS");
            if (srs != null && !srs.equals("")) {
                geoConsulta = GeoUtils.TransformGeometry(geoConsulta, srs, Textos.getTexto("urbrWS", "SRS_Datos"));
            }

            JasperReport aReport = JasperCompileManager.compileReport(System.getenv("REDES_PATH") + File.separator + "conf" + File.separator + "PlantillasGenericas" + File.separator + request.getParameter("Plantilla") + ".jrxml");


            HashMap hashMap = new HashMap();
            hashMap.put("SRS", Textos.getTexto("urbrWS", "SRS_Datos"));
            hashMap.put("BBOX", GeoUtils.getWMS_BBOX(geoConsulta));
            hashMap.put("ID_AMBITO", Integer.parseInt(request.getParameter("idAmbito")));
            hashMap.put("X", geoConsulta.getCentroid().getX());
            hashMap.put("Y", geoConsulta.getCentroid().getY());

            String jndiPath = "java:/RPMV2"; //The exact prefix here has a lot to do with clustering, etc., but if you are using one JBoss instance standalone, this works.
            Context ctx = new InitialContext();
            DataSource ds = (DataSource) PortableRemoteObject.narrow(ctx.lookup(jndiPath), DataSource.class);
            Connection c = ds.getConnection();
            JasperPrint jasperPrint = JasperFillManager.fillReport(aReport, hashMap, c);

            byte[] bytes = JasperExportManager.exportReportToPdf(jasperPrint);

            response.setContentType("application/pdf");
            response.setContentLength(bytes.length);

            ouputStream = response.getOutputStream();

            ouputStream.write(bytes, 0, bytes.length);

            ouputStream.flush();

            ouputStream.close();
            c.close();

        } catch (Exception e) {
            e.printStackTrace();
            Logger.getLogger(FichaGenerica.class.getName()).log(Level.SEVERE, null, e);
        } finally {
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">

    /**
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Generación de ficha genérica";
    }// </editor-fold>
}

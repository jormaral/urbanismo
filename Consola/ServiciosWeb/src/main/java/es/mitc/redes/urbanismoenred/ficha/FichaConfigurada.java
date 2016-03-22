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
import es.mitc.redes.urbanismoenred.data.rpm.diccionario.Instrumentoplan;
import es.mitc.redes.urbanismoenred.data.rpm.diccionario.Tipotramite;
import es.mitc.redes.urbanismoenred.data.rpm.ficha.Ficha;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidad;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Plan;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Tramite;
import es.mitc.redes.urbanismoenred.servicios.comunes.EjecutaQueryLocal;
import es.mitc.redes.urbanismoenred.servicios.planeamiento.ServicioDiccionariosLocal;
import es.mitc.redes.urbanismoenred.servicios.ficha.ServicioFichaLocal;
import es.mitc.redes.urbanismoenred.servicios.planeamiento.ModalidadPlanes;
import es.mitc.redes.urbanismoenred.servicios.planeamiento.ServicioPlaneamientoLocal;
import es.mitc.redes.urbanismoenred.serviciosweb.Textos;
import es.mitc.redes.urbanismoenred.serviciosweb.XMLws;
import es.mitc.redes.urbanismoenred.serviciosweb.consultasCatastro;
import es.mitc.redes.urbanismoenred.serviciosweb.utils.GeoUtils;
import es.mitc.redes.urbanismoenred.utils.serviciosweb.ExcepcionPublicacion;
import es.mitc.redes.urbanismoenred.utils.serviciosweb.ServicioPublicacionLocal;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.w3c.dom.Element;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletOutputStream;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRXmlDataSource;
import org.w3c.dom.Document;

/**
 *
 * @author a.centeno
 */
public class FichaConfigurada extends HttpServlet {

    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    //
    ServicioFichaLocal _srvFichas = null;// getServicioFichaLocal();
    ServicioPublicacionLocal _srvPub = null;//getServicioPublicacionLocal();
    ServicioDiccionariosLocal _srvDic = null;//getServicioDiccionariosLocal();
    ServicioPlaneamientoLocal _srvPlan = null;//getServicioPlaneamientoLocal();
    
    private ServicioFichaLocal srvFichas(){
        if (_srvFichas==null){
            _srvFichas = getServicioFichaLocal();
        }
        return _srvFichas;
    }
    
    private ServicioPublicacionLocal srvPub(){
        if (_srvPub==null){
            _srvPub = getServicioPublicacionLocal();
        }
        return _srvPub;
    }
    
    private ServicioDiccionariosLocal srvDic(){
        if (_srvDic==null){
            _srvDic = getServicioDiccionariosLocal();
        }
        return _srvDic;
    }
    
    private ServicioPlaneamientoLocal srvPlan(){
        if (_srvPlan==null){
            _srvPlan = getServicioPlaneamientoLocal();
        }
        return _srvPlan;
    }
    
    private ServicioPlaneamientoLocal getServicioPlaneamientoLocal() {
        Context context;
        try {
            context = new InitialContext();
            // Conexion con el bean de validacion de recintos
            return (ServicioPlaneamientoLocal) context.lookup("java:global/UrbanismoEnRedV2/Urbanismoenred-serviciosV2/ServicioPlaneamiento!es.mitc.redes.urbanismoenred.servicios.planeamiento.ServicioPlaneamientoLocal");

        } catch (NamingException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    private EjecutaQueryLocal getEjecutaQueryLocal() {
        Context context;
        try {
            context = new InitialContext();
            // Conexion con el bean de validacion de recintos
            return (EjecutaQueryLocal) context.lookup("java:global/UrbanismoEnRedV2/Urbanismoenred-serviciosV2/EjecutaQuery!es.mitc.redes.urbanismoenred.servicios.comunes.EjecutaQueryLocal");

        } catch (NamingException e) {
            e.printStackTrace();
            return null;
        }
    }

    private ServicioFichaLocal getServicioFichaLocal() {
        Context context;
        try {
            context = new InitialContext();
            // Conexion con el bean de validacion de recintos
            return (ServicioFichaLocal) context.lookup("java:global/UrbanismoEnRedV2/Urbanismoenred-serviciosV2/ServicioFicha!es.mitc.redes.urbanismoenred.servicios.ficha.ServicioFichaLocal");

        } catch (NamingException e) {
            e.printStackTrace();
            return null;
        }
    }

    private ServicioPublicacionLocal getServicioPublicacionLocal() {
        Context context;
        try {
            context = new InitialContext();
            // Conexion con el bean de validacion de recintos
            return (ServicioPublicacionLocal) context.lookup("java:global/UrbanismoEnRedV2/Urbanismoenred-serviciosV2/ServicioPublicacion!es.mitc.redes.urbanismoenred.utils.serviciosweb.ServicioPublicacionLocal");

        } catch (NamingException e) {
            e.printStackTrace();
            return null;
        }
    }

    private ServicioDiccionariosLocal getServicioDiccionariosLocal() {
        Context context;
        try {
            context = new InitialContext();
            // Conexion con el bean de validacion de recintos
            return (ServicioDiccionariosLocal) context.lookup("java:global/UrbanismoEnRedV2/Urbanismoenred-serviciosV2/ServicioDiccionarios!es.mitc.redes.urbanismoenred.servicios.planeamiento.ServicioDiccionariosLocal");

        } catch (NamingException e) {
            e.printStackTrace();
            return null;
        }
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        //response.setContentType("text/html;charset=UTF-8");
        //PrintWriter out = response.getWriter();

        //response.setContentType("text/xml");

        Ficha ficha = null;
        Element aNode;
        Element aNodePlanes;
        ServletOutputStream ouputStream;
        NumberFormat numberFormatter = NumberFormat.getNumberInstance(Locale.getDefault());
        numberFormatter.setMaximumFractionDigits(3);
        String outPutFormat = "pdf";
        try {
            if (request.getParameter("outPutFormat") != null) {
                if (request.getParameter("outPutFormat").equals("pdf")) {
                    outPutFormat = "pdf";
                } else if (request.getParameter("outPutFormat").equals("html")) {
                    outPutFormat = "html";
                } else {
                    outPutFormat = "pdf";
                }
            }
            Logger.getLogger(FichaConfigurada.class.getName()).log(Level.INFO, "Peticion de ficha urbanistica configurada");

            Geometry geoConsulta = GeoUtils.GeoFromWKT("POINT(" + request.getParameter("X") + " " + request.getParameter("Y") + ")");
            if (request.getParameter("X") != null && !request.getParameter("X").equals("")) {
                geoConsulta = GeoUtils.TransformGeometry(geoConsulta, request.getParameter("SRS"), Textos.getTexto("urbrWS", "SRS_Datos"));
            }


            EjecutaQueryLocal exec = getEjecutaQueryLocal();

            ficha = srvFichas().get(Ficha.class, Long.valueOf(request.getParameter("Ficha")));
            Integer idAmbito;
            if (request.getParameter("idAmbito")!=null && request.getParameter("idAmbito")!=""){
                idAmbito=Integer.valueOf(request.getParameter("idAmbito"));
            }else{
                idAmbito=srvPlan().get(Plan.class,srvPlan().get(Tramite.class,(int)ficha.getIdtramite()).getPlan().getIden()).getIdambito();
            }
            
            Entidad[] entidades = srvPub().findEntidadesRefundidoFromGeo(geoConsulta.toText(), idAmbito);

            String inIdens = null;
            for (Entidad ent : entidades) {
                if (inIdens == null) {
                    inIdens = String.valueOf(ent.getIden());
                } else {
                    inIdens = inIdens.concat("," + String.valueOf(ent.getIden()));
                }
            }

            if (inIdens == null) {
                response.setContentType("text/html");
                PrintWriter aPrint = response.getWriter();
                aPrint.print("Sin datos de planeamiento");
                aPrint.flush();
                aPrint.close();
            } else {
                //Llamada a procedimiento alacenado (sustituir para la versión 2)
                String xml = null;
                List lista = exec.selectSQL("SELECT ficha.\"getXML\"(array[" + inIdens + "]," + Integer.valueOf(request.getParameter("Ficha")) + ")");
                if (lista != null && !lista.isEmpty()) {
                    xml = lista.get(0).toString();
                }

                if (xml != null) {
                    xml = "<FICHA>" + xml + "</FICHA>";
                    Document aXMLRetorno = XMLws.XmlDocFromString(xml);
                    String situacion;
                    situacion = consultasCatastro.InverseGeocode(Double.valueOf(request.getParameter("X")), Double.valueOf(request.getParameter("Y")), request.getParameter("SRS"));
                    /*if (situacion==null){
                    if ( request.getParameter("SRS") !=null){
                    situacion = "X="+ request.getParameter("X") + " Y=" + request.getParameter("Y") + "  ( " + request.getParameter("SRS") + " )";
                    }else{
                    situacion = "X="+ request.getParameter("X") + " Y=" + request.getParameter("Y");
                    }
                    }*/
                    aNode = XMLws.AddNode(aXMLRetorno.getDocumentElement(), "SITUACION");
                    String refCatastral = consultasCatastro.referenciaCatastral(Double.valueOf(request.getParameter("X")), Double.valueOf(request.getParameter("Y")), request.getParameter("SRS"));
                    if (refCatastral != null && !refCatastral.equals("") && !refCatastral.isEmpty()) {
                        aNode.setAttribute("refCatastral", refCatastral);
                    }
                    aNode.setAttribute("x", numberFormatter.format(geoConsulta.getCentroid().getX()));
                    aNode.setAttribute("y", numberFormatter.format(geoConsulta.getCentroid().getY()));
                    aNode.setTextContent(situacion);
                    aNode = null;
                    String aIdioma = "es";
                    if (request.getParameter("Idioma") != null) {
                        aIdioma = request.getParameter("Idioma");
                    }
                    aNodePlanes = XMLws.AddNode(aXMLRetorno.getDocumentElement(), "PLANEAMIENTO");//aXMLRetorno.createElement("PLANEAMIENTO");
                    fillPlaneamientoVigente(aNodePlanes, geoConsulta, idAmbito, aIdioma);


                    JasperReport aReport = JasperCompileManager.compileReport(ficha.getPath());//System.getenv("REDES_PATH") + File.separator + "conf" + File.separator + "PlantillaBurgos.jrxml");

                    JRXmlDataSource xmlDataSource = new JRXmlDataSource(aXMLRetorno, ficha.getXpath());// "FICHA/entidades/entidad/conjunto/row");

                    HashMap hashMap = new HashMap();
                    hashMap.put("SRS", Textos.getTexto("urbrWS", "SRS_Datos"));
                    hashMap.put("BBOX", GeoUtils.getWMS_BBOX(geoConsulta));
                    hashMap.put("ID_AMBITO", idAmbito);
                    JasperPrint jasperPrint = JasperFillManager.fillReport(aReport, hashMap, xmlDataSource);

                    byte[] bytes = null;
                    if ("html".equals(outPutFormat)) {
                        response.setContentType("text/html");
                        File temp = File.createTempFile("urbr", ".jasperTmp");
                        temp.deleteOnExit();
                        JasperExportManager.exportReportToHtmlFile(jasperPrint,temp.getAbsolutePath());
                        FileInputStream fis = new FileInputStream(temp);
                        BufferedInputStream bis = new BufferedInputStream(fis);
                        bytes = new byte[(int) temp.length()];
                        bis.read(bytes);
                        fis.close();
                        try{
                            temp.delete();
                        }catch (Exception e){
                            Logger.getLogger(FichaConfigurada.class.getName()).log(Level.INFO, "No se pudo borrar el temporal: {0}", e.getMessage());
                        }
                    } else {
                        response.setContentType("application/pdf");
                        bytes = JasperExportManager.exportReportToPdf(jasperPrint);
                    }
                    response.setContentLength(bytes.length);

                    ouputStream = response.getOutputStream();

                    ouputStream.write(bytes, 0, bytes.length);

                    ouputStream.flush();

                    ouputStream.close();
                }else{
                    response.setContentType("text/html");
                    PrintWriter aPrint = response.getWriter();
                    aPrint.print("Sin datos de planeamiento para esta ficha");
                    aPrint.flush();
                    aPrint.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Logger.getLogger(FichaConfigurada.class.getName()).log(Level.SEVERE, null, e);
        } finally {
        }
    }

    private void fillPlaneamientoVigente(Element padre, Geometry geoConsulta, Integer idAmbito, String idioma) throws ExcepcionPublicacion {
        Plan[] planes = srvPub().findPlanesGeom(geoConsulta.toText(), idAmbito, false);
        Integer idtipoTramiteRefundido = Integer.valueOf(es.mitc.redes.urbanismoenred.utils.recursos.textos.Textos.getTexto("diccionario", "tipotramite.refundidoautomatico"));
        ServicioDiccionariosLocal servicioDiccionario = getServicioDiccionariosLocal();
        for (Plan plan : planes) {
            Tramite aTramite = srvPub().findUltimoTramiteConsolidado(plan.getIden());
            if (aTramite != null) {
                if (aTramite.getIdtipotramite() != idtipoTramiteRefundido) {
                    Element node = XMLws.AddNode(padre, "PLAN");

                    Instrumentoplan instrumento = servicioDiccionario.getInstrumento(plan.getIden());

                    if (instrumento != null) {
                        node.setAttribute("instrumento", servicioDiccionario.getTraduccion(Instrumentoplan.class, instrumento.getIden(), "es"));
                    }

                    node.setAttribute("nombre", plan.getNombre());
                    node.setAttribute("tramite", srvDic().getTraduccion(Tipotramite.class, aTramite.getIdtipotramite(), idioma));
                    node.setAttribute("fecha", DateFormat.getDateInstance().format(aTramite.getFecha()));
                    node.setAttribute("iteracion", String.valueOf(aTramite.getIteracion()));
                }
            }
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
        return "Generación de ficha por configuración";
    }// </editor-fold>
}

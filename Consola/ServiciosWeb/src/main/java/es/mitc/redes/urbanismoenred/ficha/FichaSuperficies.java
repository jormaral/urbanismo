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
import es.mitc.redes.urbanismoenred.data.rpm.diccionario.Ambito;
import es.mitc.redes.urbanismoenred.data.rpm.explotacion.Peticion;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Determinacion;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidad;
import es.mitc.redes.urbanismoenred.servicios.planeamiento.ServicioDiccionariosLocal;
import es.mitc.redes.urbanismoenred.servicios.planeamiento.ServicioPlaneamientoLocal;
import es.mitc.redes.urbanismoenred.serviciosweb.Textos;
import es.mitc.redes.urbanismoenred.serviciosweb.XMLws;
import es.mitc.redes.urbanismoenred.serviciosweb.utils.GeoUtils;
import es.mitc.redes.urbanismoenred.utils.serviciosweb.ServicioPublicacionLocal;
import java.io.File;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;

import java.util.List;
import java.util.UUID;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
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
public class FichaSuperficies extends HttpServlet {

    /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    private Document aXMLRetorno;
    private ServicioPlaneamientoLocal _srvPlan = getServicioPlaneamientoLocal();
    private ServicioPublicacionLocal _srvPub = getServicioPublicacionLocal();
    private ServicioDiccionariosLocal _srvDic = getServicioDiccionariosLocal();

    private ServicioPublicacionLocal srvPub() {
        if (_srvPub == null) {
            _srvPub = getServicioPublicacionLocal();
        }
        return _srvPub;
    }

    private ServicioDiccionariosLocal srvDic() {
        if (_srvDic == null) {
            _srvDic = getServicioDiccionariosLocal();
        }
        return _srvDic;
    }

    private ServicioPlaneamientoLocal srvPlan() {
        if (_srvPlan == null) {
            _srvPlan = getServicioPlaneamientoLocal();
        }
        return _srvPlan;
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

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // response.setContentType("text/html;charset=UTF-8");
        //PrintWriter out = response.getWriter();

        //response.setContentType("text/xml");

        //urbrWS ServicioWeb = new urbrWS();
        Element aNode;
        Element aNodeGrupo;
        ServletOutputStream ouputStream;
        List<Entidad> aEntidades;
        List<Double> superficies;
        List<Double> superficies_int;
        NodeList aListaGrupos;
        Element aNodeAmbito;
        Peticion aPeticion;

        try {
            Logger.getLogger(FichaSuperficies.class.getName()).log(Level.INFO, "Peticion de ficha de superficies");

            aXMLRetorno = XMLws.generarXMLDOC("FICHA");
            Geometry geoConsulta = GeoUtils.GeoFromWKT(request.getParameter("WKT"));

            String srs = request.getParameter("SRS");
            if (srs != null && !srs.equals("")) {
                geoConsulta = GeoUtils.TransformGeometry(geoConsulta, srs, Textos.getTexto("urbrWS", "SRS_Datos"));
            }

            aPeticion = new Peticion();
            aPeticion.setGeom(GeoUtils.GeoToWKB(geoConsulta));
            aPeticion.setCodigo(UUID.randomUUID().toString());
            srvPub().saveGeomPeticion(aPeticion);


            aNodeAmbito = XMLws.AddNode(aXMLRetorno.getDocumentElement(), "AMBITO");
            aNodeAmbito.setAttribute("nombre", srvDic().getTraduccion(Ambito.class, Integer.valueOf(request.getParameter("idAmbito")), "es"));
            aEntidades = new ArrayList<Entidad>();
            superficies = new ArrayList<Double>();
            superficies_int = new ArrayList<Double>();
            srvPub().findSuperficiesRefundido(GeoUtils.GeoToWKT(geoConsulta), Integer.valueOf(request.getParameter("idAmbito")), aEntidades, superficies, superficies_int);
            for (int iEnt = 0; iEnt < aEntidades.size() - 1; iEnt++) {
                if (geoConsulta.getArea() == 0 || superficies_int.get(iEnt) > 0.5) {
                    Entidad aEntidad = aEntidades.get(iEnt);
                    if (!aEntidad.isBsuspendida()) {
                        Determinacion Grupo = srvPlan().getGrupoEntidad(aEntidad.getIden());
                        if (Grupo != null) {
                            aListaGrupos = XMLws.findNode("GRUPO[@id=\"" + Grupo.getIden() + "\"]", aNodeAmbito);
                            if (aListaGrupos == null || aListaGrupos.getLength() == 0) {
                                aNodeGrupo = aNodeAmbito.getOwnerDocument().createElement("GRUPO");
                                aNodeGrupo.setAttribute("id", String.valueOf(Grupo.getIden()));
                                aNodeGrupo.setAttribute("nombre", Grupo.getNombre());
                                aNodeGrupo.setAttribute("orden", String.valueOf(Grupo.getOrden()));


                                for (int iNodo = 0; iNodo < aNodeAmbito.getChildNodes().getLength(); iNodo++) {
                                    Element aNodoAfter = (Element) aNodeAmbito.getChildNodes().item(iNodo);
                                    if (Grupo.getOrden() < Integer.valueOf(aNodoAfter.getAttribute("orden"))) {
                                        aNodeAmbito.insertBefore(aNodeGrupo, aNodoAfter);
                                        break;
                                    } else if (Grupo.getOrden() == Integer.valueOf(aNodoAfter.getAttribute("orden"))) {
                                        if (Grupo.getIden() < Integer.valueOf(aNodoAfter.getAttribute("id"))) {
                                            aNodeAmbito.insertBefore(aNodeGrupo, aNodoAfter);
                                            break;
                                        }
                                    }
                                }
                                if (aNodeGrupo.getParentNode() == null) {
                                    aNodeAmbito.appendChild(aNodeGrupo);
                                }
                            } else {
                                aNodeGrupo = (Element) aListaGrupos.item(0);
                            }

                            aNode = XMLws.AddNode(aNodeGrupo, "ENTIDAD");// aXMLRetorno.createElement("ENTIDAD");
                            aNodeGrupo.appendChild(aNode);
                            fillEntidad(aEntidad, aNode, superficies.get(iEnt), superficies_int.get(iEnt));
                        }
                    }
                }
            }
            JasperReport aReport = JasperCompileManager.compileReport(System.getenv("REDES_PATH") + File.separator + "conf" + File.separator + "PlantillaSuperficies.jrxml");


            JRXmlDataSource xmlDataSource = new JRXmlDataSource(aXMLRetorno, "FICHA/AMBITO/GRUPO/ENTIDAD");


            HashMap hashMap = new HashMap();
            hashMap.put("SRS", Textos.getTexto("urbrWS", "SRS_Datos"));
            hashMap.put("BBOX", GeoUtils.getWMS_BBOX(geoConsulta));
            hashMap.put("ID_PETICION", aPeticion.getCodigo());
            hashMap.put("ID_AMBITO", Integer.parseInt(request.getParameter("idAmbito")));
            if (request.getParameter("entidadQuery") != null) {
                hashMap.put("ENTIDAD_PETICION", request.getParameter("entidadQuery"));
            }
            JasperPrint jasperPrint = JasperFillManager.fillReport(aReport, hashMap, xmlDataSource);

            byte[] bytes = JasperExportManager.exportReportToPdf(jasperPrint);

            response.setContentType("application/pdf");
            response.setContentLength(bytes.length);

            ouputStream = response.getOutputStream();

            ouputStream.write(bytes, 0, bytes.length);

            ouputStream.flush();

            ouputStream.close();

        } catch (Exception e) {
            e.printStackTrace();
            Logger.getLogger(FichaSuperficies.class.getName()).log(Level.SEVERE, null, e);
        } finally {
        }
    }

    private void fillEntidad(Entidad aEntidad, Element aNodeEntidad, double areaEntidad, double areaInterseccion) {
        try {
            NumberFormat formato = NumberFormat.getInstance();
            formato.setMaximumFractionDigits(2);
            aNodeEntidad.setAttribute("id", String.valueOf(aEntidad.getIden()));
            aNodeEntidad.setAttribute("clave", aEntidad.getClave());
            aNodeEntidad.setAttribute("nombre", aEntidad.getNombre());
            aNodeEntidad.setAttribute("orden", String.valueOf(aEntidad.getOrden()));
            if (areaEntidad > 0.5) {
                aNodeEntidad.setAttribute("superficie", formato.format(areaEntidad));
            }
            if (areaInterseccion > 0.5) {
                aNodeEntidad.setAttribute("superficie_interseccion", formato.format(areaInterseccion));
            }
        } catch (Exception ex) {
            Logger.getLogger(FichaSuperficies.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP
     * <code>GET</code> method.
     *
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
     * Handles the HTTP
     * <code>POST</code> method.
     *
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
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Generación de XML para ficha urbanística";
    }// </editor-fold>
}

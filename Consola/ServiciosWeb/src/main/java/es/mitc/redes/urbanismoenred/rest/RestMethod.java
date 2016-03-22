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
package es.mitc.redes.urbanismoenred.rest;

import com.vividsolutions.jts.geom.Geometry;
import es.mitc.redes.urbanismoenred.data.rpm.diccionario.Ambito;
import es.mitc.redes.urbanismoenred.data.rpm.diccionario.Grupodocumento;
import es.mitc.redes.urbanismoenred.data.rpm.diccionario.Tipotramite;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Documento;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Plan;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Tramite;
import es.mitc.redes.urbanismoenred.servicios.configuracion.ConfiguracionVisorLocal;


import es.mitc.redes.urbanismoenred.servicios.planeamiento.ModalidadPlanes;
import es.mitc.redes.urbanismoenred.servicios.planeamiento.ServicioDiccionariosLocal;
import es.mitc.redes.urbanismoenred.servicios.planeamiento.ServicioPlaneamientoLocal;
import es.mitc.redes.urbanismoenred.serviciosweb.Textos;
import es.mitc.redes.urbanismoenred.serviciosweb.XMLws;
import es.mitc.redes.urbanismoenred.serviciosweb.utils.GeoUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import java.io.IOException;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.xml.sax.*;
import org.xml.sax.helpers.*;

import es.mitc.redes.urbanismoenred.serviciosweb.urbrWS;
import es.mitc.redes.urbanismoenred.serviciosweb.utils.ParIdentificadorTexto;
import es.mitc.redes.urbanismoenred.utils.serviciosweb.ServicioPublicacionLocal;
import java.io.DataInputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URLEncoder;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletOutputStream;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 *
 * @author Alvaro.Martin
 */
public class RestMethod extends HttpServlet {

    private Logger log;

    ServicioPlaneamientoLocal srvPlan = getServicioPlaneamientoLocal();
    ServicioPublicacionLocal srvPub = getServicioPublicacionLocal();
    ServicioDiccionariosLocal srvDic = getServicioDiccionariosLocal();
    ConfiguracionVisorLocal cvSVC=getConfiguracionVisorLocal();


    private ConfiguracionVisorLocal getConfiguracionVisorLocal(){
        //CONTEXTO
        Context context;
        try {
            context = new InitialContext();
            return (ConfiguracionVisorLocal) context.lookup("java:global/UrbanismoEnRedV2/Urbanismoenred-serviciosV2/ConfiguradorVisor!es.mitc.redes.urbanismoenred.servicios.configuracion.ConfiguracionVisorLocal");                             
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
    
    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        log = Logger.getLogger(RestMethod.class.getName());
        response.setContentType("text/html;charset=UTF-8");
        //PrintWriter out = response.getWriter();
        try {


            String wsName = request.getParameter("wsName");
            if (wsName == null) {
                response.getWriter().write("'No se ha indicado el servicio'");
                return; // al hacer return se pasa por el finally
            }

            if (wsName.equalsIgnoreCase("GET_NODOS_AMBITOS_DE_COOR_VIS")) {
                this.getNodosAmbitosDeCoord_XML(request, response);
                response.getWriter().close();
            }else if (wsName.equalsIgnoreCase("GET_NODOS_AMBITOS_VIS")) {
                this.getNodosAmbitos_XML(request, response);
                response.getWriter().close();
            } else if (wsName.equalsIgnoreCase("GET_NODOS_DE_AMBITO")) {
                this.getNodosDeAmbito(request, response);
                response.getWriter().close();
            } else if (wsName.equalsIgnoreCase("GET_NODOS_PLANES_DE_AMBITO")) {
                this.getNodosPlanesDeAmbito(request, response);
                response.getWriter().close();
            } else if (wsName.equalsIgnoreCase("GET_NODOS_AMBITOS_DE_AMBITO")) {
                this.getNodosAmbitosDeAmbito(request, response);
                response.getWriter().close();
            } else if (wsName.equalsIgnoreCase("GET_NODOS_PLANES_DE_AMBITO_DE_COORD")) {
                this.getNodosPlanesDeAmbitoCoor(request, response);
                response.getWriter().close();
            } else if (wsName.equalsIgnoreCase("GET_NODOS_INFO_DE_PLAN")) {
                this.getNodosInfoDePlan(request, response);
                response.getWriter().close();
            } else if (wsName.equalsIgnoreCase("GET_NODOS_INFO_DE_PLAN_DE_COOR")) {
                this.getNodosInfoDePlanCoor(request, response);
                response.getWriter().close();
            } else if (wsName.equalsIgnoreCase("GET_NODOS_TRAMS_DE_PLAN")) {
                this.getNodosTramitesDePlan(request, response);
                response.getWriter().close();
            } else if (wsName.equalsIgnoreCase("GET_NODOS_PLANES_DE_PLAN")) {
                this.getNodosPlanesDePlan(request, response);
                response.getWriter().close();
            } else if (wsName.equalsIgnoreCase("GET_NODOS_PLANES_DE_PLAN_DE_COOR")) {
                this.getNodosPlanesDePlanCoor(request, response);
                response.getWriter().close();
            } else if (wsName.equalsIgnoreCase("GET_NODOS_DOCS_DE_TRAM")) {
                this.getNodosDocsDeTram(request, response);
                response.getWriter().close();
            } else if (wsName.equalsIgnoreCase("GET_DOC_URL")) {
                this.getDocURL(request, response);
                response.getWriter().close();
            } else if (wsName.equalsIgnoreCase("GET_LAYERCONFIG_DE_AMBITO")) {
                this.getLayerConfigDeAmbito(request, response);
            }
        } finally {
            // Aquí no puede ir un response.getWriter().close() porque si la
            // llamada ha sido a GET_LAYERCONFIG_DE_AMBITO y se llama después de
            // response.getOutputStream, se genera una excepción
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
        return "Short description";
    }// </editor-fold>

    public static boolean chkParams(HttpServletRequest request, String... paramNames) {
        boolean res = true;
        for (int i = 0; i < paramNames.length; i++) {
            res = res && request.getParameter(paramNames[i]) != null;
        }
        return res;
    }

    
    private void getNodosAmbitos_XML(HttpServletRequest request, HttpServletResponse response) {
        try {

            if (!RestMethod.chkParams(request, "rutaServ", "idioma")) {
                response.getWriter().print("Solicitud incompleta");
                return;
            }

            response.setContentType("text/xml;charset=UTF-8");


            // Control de parámetros de llamada
            int idAmbito=0;
            if (request.getParameter("idAmbito")!=null && !request.getParameter("idAmbito").equalsIgnoreCase("null")){
                try {
                    idAmbito = Integer.parseInt(request.getParameter("idAmbito"));
                } catch (Exception e) {
                    // el parámetro idTramite no es un número
                    response.getWriter().print("'El parámetro idAmbito no es un número'");
                    return;
                }
            }
            
            String rutaServ = request.getParameter("rutaServ");
            String idioma = request.getParameter("idioma");

            List<ParIdentificadorTexto> lreg = this.getNodosAmbitos_XML_Aux(idAmbito, idioma);

             Document aXML;
            aXML=XMLws.generarXMLDOC("nodes");
            for (ParIdentificadorTexto reg : lreg) {
                Element aNode=aXML.createElement("node");
                aNode.setAttribute("text",  reg.getTexto());
                aNode.setAttribute("tipo",  reg.getTipo());
                aNode.setAttribute("idNode",  String.valueOf(reg.getIdBaseDatos()));
                aNode.setAttribute("load",  rutaServ + "RestMethod?wsName=GET_NODOS_DE_AMBITO&idAmb=" + reg.getIdBaseDatos() + "&rutaServ=" + URLEncoder.encode(rutaServ) + "&idioma=" + URLEncoder.encode(idioma));
                for (int iNodo=0;iNodo< aXML.getDocumentElement().getChildNodes().getLength();iNodo++)
                {
                    Element aNodoAfter=(Element) aXML.getDocumentElement().getChildNodes().item(iNodo);
                    if (reg.getTexto().compareTo(aNodoAfter.getAttribute("text"))<0){
                        aXML.getDocumentElement().insertBefore(aNode,aNodoAfter);
                        break;
                    }
                }
                if (aNode.getParentNode()==null){
                    aXML.getDocumentElement().appendChild(aNode);
                }
            }
            PrintWriter pw =response.getWriter();
            pw.println(XMLws.XmlDocToString(aXML));
            pw.flush();
           /* OutputFormat of = new OutputFormat("XML", "UTF-8", true);
            of.setIndent(1);
            of.setIndenting(true);

            XMLSerializer serializer = new XMLSerializer(response.getWriter(),of);
            ContentHandler hd = serializer.asContentHandler();
            hd.startDocument();
            
            // si el ámbito tiene ámbitos hijos añadimos el nodo de ámbitos
            
            hd.startElement("", "", "nodes",null);
            for (ParIdentificadorTexto reg : lreg) {

                // <node text="X" tipo="tramite" idNode="X"/>
                AttributesImpl atts = new AttributesImpl();
                atts.addAttribute("", "", "text", "", reg.getTexto());
                atts.addAttribute("", "", "tipo", "", reg.getTipo());
                atts.addAttribute("", "", "idNode", "", String.valueOf(reg.getIdBaseDatos()));
                atts.addAttribute("", "", "load", "", rutaServ + "RestMethod?wsName=GET_NODOS_DE_AMBITO&idAmb=" + reg.getIdBaseDatos() + "&rutaServ=" + URLEncoder.encode(rutaServ) + "&idioma=" + URLEncoder.encode(idioma));
                hd.startElement("", "", "node", atts);
                hd.endElement("", "", "node");

            }
            // </nodes>
            hd.endElement("", "", "nodes");
            hd.endDocument();

            response.getWriter().flush();*/
        } catch (Exception ex) {
            Logger.getLogger(RestMethod.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
       
    private void getNodosAmbitosDeCoord_XML(HttpServletRequest request, HttpServletResponse response) {
        try {

            if (!RestMethod.chkParams(request, "x", "y", "srs", "rutaServ", "idioma")) {
                response.getWriter().print("Solicitud incompleta");
                return;
            }

            response.setContentType("text/xml;charset=UTF-8");


            // Control de parámetros de llamada
            double coorX;
            try {
                coorX = Double.parseDouble(request.getParameter("x"));
            } catch (Exception e) {
                // el parámetro idTramite no es un número
                response.getWriter().print("'El parámetro x no es un número'");
                return;
            }

            double coorY;
            try {
                coorY = Double.parseDouble(request.getParameter("y"));
            } catch (Exception e) {
                // el parámetro idTramite no es un número
                response.getWriter().print("'El parámetro y no es un número'");
                return;
            }

            String srs = request.getParameter("srs");
            String rutaServ = request.getParameter("rutaServ");
            String idioma = request.getParameter("idioma");

            List<ParIdentificadorTexto> lreg = this.getNodosAmbitosDeCoor_XML_Aux(coorX, coorY, srs, idioma);

            Document aXML;
            aXML=XMLws.generarXMLDOC("nodes");
            for (ParIdentificadorTexto reg : lreg) {
                Element aNode=aXML.createElement("node");
                aNode.setAttribute("text",  reg.getTexto());
                aNode.setAttribute("tipo",  reg.getTipo());
                aNode.setAttribute("idNode",  String.valueOf(reg.getIdBaseDatos()));
                aNode.setAttribute("load",  rutaServ + "RestMethod?wsName=GET_NODOS_PLANES_DE_AMBITO_DE_COORD&idAmb=" + reg.getIdBaseDatos() + "&x=" + coorX + "&y=" + coorY + "&srs=" + srs + "&rutaServ=" + URLEncoder.encode(rutaServ) + "&idioma=" + URLEncoder.encode(idioma));
                for (int iNodo=0;iNodo< aXML.getDocumentElement().getChildNodes().getLength();iNodo++)
                {
                    Element aNodoAfter=(Element) aXML.getDocumentElement().getChildNodes().item(iNodo);
                    if (reg.getTexto().compareTo(aNodoAfter.getAttribute("text"))<0){
                        aXML.getDocumentElement().insertBefore(aNode,aNodoAfter);
                        break;
                    }
                }
                if (aNode.getParentNode()==null){
                    aXML.getDocumentElement().appendChild(aNode);
                }
            }
            PrintWriter pw =response.getWriter();
            pw.println(XMLws.XmlDocToString(aXML));
            pw.flush();
            /*
            OutputFormat of = new OutputFormat("XML", "UTF-8", true);
            of.setIndent(1);
            of.setIndenting(true);

            XMLSerializer serializer = new XMLSerializer(response.getWriter(), of);
            ContentHandler hd = serializer.asContentHandler();
            
            hd.startDocument();
            hd.startElement("", "", "nodes", null);
            for (ParIdentificadorTexto reg : lreg) {
                AttributesImpl atts = new AttributesImpl();
                atts.addAttribute("", "", "text", "", reg.getTexto());
                atts.addAttribute("", "", "tipo", "", reg.getTipo());
                atts.addAttribute("", "", "idNode", "", String.valueOf(reg.getIdBaseDatos()));
                atts.addAttribute("", "", "load", "", rutaServ + "RestMethod?wsName=GET_NODOS_PLANES_DE_AMBITO_DE_COORD&idAmb=" + reg.getIdBaseDatos() + "&x=" + coorX + "&y=" + coorY + "&srs=" + srs + "&rutaServ=" + URLEncoder.encode(rutaServ) + "&idioma=" + URLEncoder.encode(idioma));
                hd.startElement("", "", "node", atts);
                hd.endElement("", "", "node");

            }
            hd.endElement("", "", "nodes");
            hd.endDocument();
             
             

            response.getWriter().flush();*/
        } catch (Exception ex) {
            Logger.getLogger(RestMethod.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
     private List<ParIdentificadorTexto> getNodosAmbitos_XML_Aux(int idAmb, String idioma) {
        List<ParIdentificadorTexto> res = new ArrayList<ParIdentificadorTexto>();
        try {

            
            urbrWS urbr=new urbrWS();
            if (idAmb == -1) {
                return res;
            }

            ParIdentificadorTexto item;
            //Ambito amb = ab.getAmbito(idAmb);
            if (idAmb==0){
                Ambito[] ambitos= srvPub.findAmbitosWithPlan();
                for (Ambito amb : ambitos){
                    item = new ParIdentificadorTexto(amb.getIden(), srvDic.getTraduccion(Ambito.class, amb.getIden(), idioma), "ambito");
                    res.add(item);
                }
            }else{
                item = new ParIdentificadorTexto(idAmb, srvDic.getTraduccion(Ambito.class, idAmb, idioma), "ambito");
                res.add(item);
            }

        } catch (Exception ex) {
            Logger.getLogger(RestMethod.class.getName()).log(Level.SEVERE, null, ex);
        }

        return res;
    }

     
    private List<ParIdentificadorTexto> getNodosAmbitosDeCoor_XML_Aux(Double coorX, Double coorY, String srs, String idioma) {
        List<ParIdentificadorTexto> res = new ArrayList<ParIdentificadorTexto>();
        try {

            urbrWS ws = new urbrWS();
            int idAmb = ws.idAmbito(coorX, coorY, srs);

            if (idAmb == -1) {
                return res;
            }


            ParIdentificadorTexto item = new ParIdentificadorTexto(idAmb, srvDic.getTraduccion(Ambito.class, idAmb, idioma), "ambito");
            res.add(item);

        } catch (Exception ex) {
            Logger.getLogger(RestMethod.class.getName()).log(Level.SEVERE, null, ex);
        }

        return res;
    }

    private void getNodosPlanesDeAmb_XML(HttpServletRequest request, HttpServletResponse response) {
        try {

            if (!RestMethod.chkParams(request, "idAmb", "rutaServ", "idioma")) {
                response.getWriter().print("Solicitud incompleta");
                return;
            }

            response.setContentType("text/xml;charset=UTF-8");


            // Control de parámetros de llamada
            int idAmb;
            try {
                idAmb = Integer.parseInt(request.getParameter("idAmb"));
            } catch (Exception e) {
                // el parámetro idTramite no es un número
                response.getWriter().print("'El parámetro idAmb no es un número'");
                return;
            }


            String rutaServ = request.getParameter("rutaServ");
            String idioma = request.getParameter("idioma");

            
            Plan[] lplans = srvPlan.getPlanesRaiz(idAmb, ModalidadPlanes.RPM);

            OutputFormat of = new OutputFormat("XML", "UTF-8", true);
            of.setIndent(1);
            of.setIndenting(true);

            XMLSerializer serializer = new XMLSerializer(response.getWriter(), of);
            ContentHandler hd = serializer.asContentHandler();
            hd.startDocument();
            // <nodes>
            hd.startElement("", "", "nodes", null);
            for (Plan pln : lplans) {

                // <node text="X" tipo="tramite" idNode="X"/>
                AttributesImpl atts = new AttributesImpl();
                atts.addAttribute("", "", "text", "", pln.getNombre());
                atts.addAttribute("", "", "tipo", "", "plan");
                atts.addAttribute("", "", "idNode", "", String.valueOf(pln.getIden()));
                atts.addAttribute("", "", "load", "", rutaServ + "RestMethod?wsName=GET_NODOS_TRAMITES_DE_PLAN_VIS&idPlan=" + pln.getIden() + "&rutaServ=" + URLEncoder.encode(rutaServ) + "&idioma=" + URLEncoder.encode(idioma));
                hd.startElement("", "", "node", atts);
                hd.endElement("", "", "node");

            }
            // </nodes>
            hd.endElement("", "", "nodes");
            hd.endDocument();

            response.getWriter().flush();
        } catch (Exception ex) {
            Logger.getLogger(RestMethod.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void getNodosPlanesDeAmbitoCoor(HttpServletRequest request, HttpServletResponse response) {
        try {

            if (!RestMethod.chkParams(request,"x", "y", "srs", "idAmb", "idioma", "rutaServ")) {
                response.getWriter().print("Solicitud incompleta");
                return;
            }

            response.setContentType("text/xml;charset=UTF-8");

            // Recuperar los parámetros de la llamada
            double coorX;
            try {
                coorX = Double.parseDouble(request.getParameter("x"));
            } catch (Exception e) {
                // el parámetro idTramite no es un número
                response.getWriter().print("'El parámetro x no es un número'");
                return;
            }

            double coorY;
            try {
                coorY = Double.parseDouble(request.getParameter("y"));
            } catch (Exception e) {
                // el parámetro idTramite no es un número
                response.getWriter().print("'El parámetro y no es un número'");
                return;
            }

            String srs = request.getParameter("srs");
            
            Geometry geom=GeoUtils.GeoFromWKT("POINT(" + coorX + " " + coorY + ")");
            geom=GeoUtils.TransformGeometry(geom,srs, Textos.getTexto("urbrWS", "SRS_Datos"));
            
            int idAmbito = Integer.valueOf(request.getParameter("idAmb"));
            String idioma = request.getParameter("idioma");
            String rutaServ = request.getParameter("rutaServ");

            
            Plan[] lplans =  srvPub.findPlanesGeom(geom.toText(), idAmbito,true);

            OutputFormat of = new OutputFormat("XML", "UTF-8", true);
            of.setIndent(1);
            of.setIndenting(true);
            XMLSerializer serializer = new XMLSerializer(response.getWriter(), of);
            ContentHandler hd = serializer.asContentHandler();
            hd.startDocument();
            // <nodes>
            hd.startElement("", "", "nodes", null);


            for (Plan pln : lplans) {
                // <node text="X" tipo="plan" idNode="Y" />
                AttributesImpl atts = new AttributesImpl();
                atts.addAttribute("", "", "text", "", pln.getNombre());
                atts.addAttribute("", "", "tipo", "", "plan");
                atts.addAttribute("", "", "idNode", "", String.valueOf(pln.getIden()));
                atts.addAttribute("", "", "load", "", rutaServ + "RestMethod?wsName=GET_NODOS_INFO_DE_PLAN_DE_COOR&x=" + coorX + "&y=" + coorY + "&srs=" + srs + "&idPlan=" + pln.getIden() + "&idioma=" + URLEncoder.encode(idioma) + "&rutaServ=" + URLEncoder.encode(rutaServ));
                hd.startElement("", "", "node", atts);
                hd.endElement("", "", "node");
            }

            // </nodes>
            hd.endElement("", "", "nodes");
            hd.endDocument();
            response.getWriter().flush();


        } catch (Exception ex) {
            Logger.getLogger(RestMethod.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void getNodosAmbitosDeAmbito(HttpServletRequest request, HttpServletResponse response) {
        try {

            if (!RestMethod.chkParams(request, "idAmbito", "idioma", "rutaServ")) {
                response.getWriter().print("Solicitud incompleta");
                return;
            }

            response.setContentType("text/xml;charset=UTF-8");

            // Recuperar los parámetros de la llamada
            int idAmbito = Integer.valueOf(request.getParameter("idAmbito"));
            
            String idioma = request.getParameter("idioma");
            String rutaServ = request.getParameter("rutaServ");

            
            Ambito[] ambitos = srvPub.findAmbitosHijosWithPlan(idAmbito);

             Document aXML;
            aXML=XMLws.generarXMLDOC("nodes");
            for (Ambito amb : ambitos) {
                Element aNode=aXML.createElement("node");
                String nombre=srvDic.getTraduccion(Ambito.class, amb.getIden(), idioma);
                aNode.setAttribute("text", nombre);
                aNode.setAttribute("tipo",  "ambito");
                aNode.setAttribute("idNode",  String.valueOf(amb.getIden()));
                aNode.setAttribute("load",  rutaServ + "RestMethod?wsName=GET_NODOS_DE_AMBITO&idAmb=" + amb.getIden() + "&idioma=" + URLEncoder.encode(idioma) + "&rutaServ=" + URLEncoder.encode(rutaServ));
                for (int iNodo=0;iNodo< aXML.getDocumentElement().getChildNodes().getLength();iNodo++)
                {
                    Element aNodoAfter=(Element) aXML.getDocumentElement().getChildNodes().item(iNodo);
                    if (nombre.compareTo(aNodoAfter.getAttribute("text"))<0){
                        aXML.getDocumentElement().insertBefore(aNode,aNodoAfter);
                        break;
                    }
                }
                if (aNode.getParentNode()==null){
                    aXML.getDocumentElement().appendChild(aNode);
                }
            }
            PrintWriter pw =response.getWriter();
            pw.println(XMLws.XmlDocToString(aXML));
            pw.flush();
            
        } catch (Exception ex) {
            Logger.getLogger(RestMethod.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void getNodosDeAmbito(HttpServletRequest request, HttpServletResponse response) {
        try {

            if (!RestMethod.chkParams(request, "idAmb", "idioma", "rutaServ")) {
                response.getWriter().print("Solicitud incompleta");
                return;
            }

            response.setContentType("text/xml;charset=UTF-8");

            // Recuperar los parámetros de la llamada
            int idAmbito = Integer.valueOf(request.getParameter("idAmb"));
            
            String idioma = request.getParameter("idioma");
            String rutaServ = request.getParameter("rutaServ");

            OutputFormat of = new OutputFormat("XML", "UTF-8", true);
            of.setIndent(1);
            of.setIndenting(true);
            XMLSerializer serializer = new XMLSerializer(response.getWriter(), of);
            ContentHandler hd = serializer.asContentHandler();
            hd.startDocument();
            // <nodes>
            hd.startElement("", "", "nodes", null);

            
            int numAmbitos = srvPub.findAmbitosHijosWithPlan(idAmbito).length;
            if (numAmbitos > 0) {
                AttributesImpl atts = new AttributesImpl();
                atts = new AttributesImpl();
                atts.addAttribute("", "", "text", "", "Ámbitos (" + numAmbitos + ")");
                atts.addAttribute("", "", "load", "", rutaServ + "RestMethod?wsName=GET_NODOS_AMBITOS_DE_AMBITO&idAmbito=" + idAmbito + "&idioma=" + URLEncoder.encode(idioma) + "&rutaServ=" + URLEncoder.encode(rutaServ));
                hd.startElement("", "", "node", atts);
                hd.endElement("", "", "node");
            }
            
           
            Plan[] lplans = srvPub.findPlanesRaiz(idAmbito);
            int numPlanes = lplans.length;
            if (numPlanes > 0) {
                AttributesImpl atts = new AttributesImpl();
                atts = new AttributesImpl();
                atts.addAttribute("", "", "text", "", "Planes (" + numPlanes + ")");
                atts.addAttribute("", "", "load", "", rutaServ + "RestMethod?wsName=GET_NODOS_PLANES_DE_AMBITO&idAmb=" + idAmbito + "&idioma=" + URLEncoder.encode(idioma) + "&rutaServ=" + URLEncoder.encode(rutaServ));
                hd.startElement("", "", "node", atts);
                hd.endElement("", "", "node");
            }
            
            /*for (ParIdentificadorTexto pln : lplans) {
                // <node text="X" tipo="plan" idNode="Y" />
                AttributesImpl atts = new AttributesImpl();
                atts.addAttribute("", "", "text", "", pln.getTexto());
                atts.addAttribute("", "", "tipo", "", "plan");
                atts.addAttribute("", "", "idNode", "", String.valueOf(pln.getIdBaseDatos()));
                atts.addAttribute("", "", "load", "", rutaServ + "RestMethod?wsName=GET_NODOS_INFO_DE_PLAN&idPlan=" + pln.getIdBaseDatos() + "&idioma=" + URLEncoder.encode(idioma) + "&rutaServ=" + URLEncoder.encode(rutaServ));
                hd.startElement("", "", "node", atts);
                hd.endElement("", "", "node");
            }*/

            // </nodes>
            hd.endElement("", "", "nodes");
            hd.endDocument();
            response.getWriter().flush();


        } catch (Exception ex) {
            Logger.getLogger(RestMethod.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void getNodosPlanesDeAmbito(HttpServletRequest request, HttpServletResponse response) {
        try {

            if (!RestMethod.chkParams(request, "idAmb", "idioma", "rutaServ")) {
                response.getWriter().print("Solicitud incompleta");
                return;
            }

            response.setContentType("text/xml;charset=UTF-8");

            // Recuperar los parámetros de la llamada
            int idAmbito = Integer.valueOf(request.getParameter("idAmb"));
            
            String idioma = request.getParameter("idioma");
            String rutaServ = request.getParameter("rutaServ");

            OutputFormat of = new OutputFormat("XML", "UTF-8", true);
            of.setIndent(1);
            of.setIndenting(true);
            XMLSerializer serializer = new XMLSerializer(response.getWriter(), of);
            ContentHandler hd = serializer.asContentHandler();
            hd.startDocument();
            // <nodes>
            hd.startElement("", "", "nodes", null);

            
            
            
            Plan[] lplans = srvPub.findPlanesRaiz(idAmbito) ;
           
            for (Plan pln : lplans) {
                // <node text="X" tipo="plan" idNode="Y" />
                AttributesImpl atts = new AttributesImpl();
                atts.addAttribute("", "", "text", "", pln.getNombre());
                atts.addAttribute("", "", "tipo", "", "plan");
                atts.addAttribute("", "", "idNode", "", String.valueOf(pln.getIden()));
                atts.addAttribute("", "", "load", "", rutaServ + "RestMethod?wsName=GET_NODOS_INFO_DE_PLAN&idPlan=" + pln.getIden() + "&idioma=" + URLEncoder.encode(idioma) + "&rutaServ=" + URLEncoder.encode(rutaServ));
                hd.startElement("", "", "node", atts);
                hd.endElement("", "", "node");
            }

            // </nodes>
            hd.endElement("", "", "nodes");
            hd.endDocument();
            response.getWriter().flush();


        } catch (Exception ex) {
            Logger.getLogger(RestMethod.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

       
    private void getNodosInfoDePlanCoor(HttpServletRequest request, HttpServletResponse response) {
        try {

            if (!RestMethod.chkParams(request,"x","y","srs","idPlan", "idioma", "rutaServ")) {
                response.getWriter().print("Solicitud incompleta");
                return;
            }

            response.setContentType("text/xml;charset=UTF-8");

            // Recuperar los parámetros de la llamada
            double coorX;
            try {
                coorX = Double.parseDouble(request.getParameter("x"));
            } catch (Exception e) {
                // el parámetro idTramite no es un número
                response.getWriter().print("'El parámetro x no es un número'");
                return;
            }

            double coorY;
            try {
                coorY = Double.parseDouble(request.getParameter("y"));
            } catch (Exception e) {
                // el parámetro idTramite no es un número
                response.getWriter().print("'El parámetro y no es un número'");
                return;
            }

            String srs = request.getParameter("srs");
            
            Geometry geom=GeoUtils.GeoFromWKT("POINT(" + coorX + " " + coorY + ")");
            geom=GeoUtils.TransformGeometry(geom,srs, Textos.getTexto("urbrWS", "SRS_Datos"));
            
            int idPlan = Integer.valueOf(request.getParameter("idPlan"));
            String idioma = request.getParameter("idioma");
            String rutaServ = request.getParameter("rutaServ");
            // Obtener el usuario logueado en la consola


           
            OutputFormat of = new OutputFormat("XML", "UTF-8", true);
            of.setIndent(1);
            of.setIndenting(true);
            XMLSerializer serializer = new XMLSerializer(response.getWriter(), of);
            ContentHandler hd = serializer.asContentHandler();
            hd.startDocument();
            // <nodes>
            hd.startElement("", "", "nodes", null);


            // <node text="X" tipo="plan" idNode="Y" />
            AttributesImpl atts = new AttributesImpl();


            // si el plan tiene trámites añadimos el nodo de trámites
            int numTrams = srvPlan.getTramitesPorPlan(idPlan, ModalidadPlanes.RPM).length;
            if (numTrams > 0) {
                atts = new AttributesImpl();
                atts.addAttribute("", "", "text", "", "Trámites (" + numTrams + ")");
                atts.addAttribute("", "", "load", "", rutaServ + "RestMethod?wsName=GET_NODOS_TRAMS_DE_PLAN&idPlan=" + idPlan + "&idioma=" + URLEncoder.encode(idioma) + "&rutaServ=" + URLEncoder.encode(rutaServ));
                hd.startElement("", "", "node", atts);
                hd.endElement("", "", "node");
            }

            // si el plan tiene planes hijos añadimos el nodo de planes
            int numPlanes = srvPub.findPlanesHijosGeom(geom.toText(), idPlan).length;
            
            if (numPlanes > 0) {
                atts = new AttributesImpl();
                atts.addAttribute("", "", "text", "", "Planes (" + numPlanes + ")");
                atts.addAttribute("", "", "load", "", rutaServ + "RestMethod?wsName=GET_NODOS_PLANES_DE_PLAN_DE_COOR&x=" + coorX + "&y=" + coorY + "&srs=" + srs + "&idPlan=" + idPlan + "&idioma=" + URLEncoder.encode(idioma) + "&rutaServ=" + URLEncoder.encode(rutaServ));
                hd.startElement("", "", "node", atts);
                hd.endElement("", "", "node");
            }


            // </nodes>
            hd.endElement("", "", "nodes");
            hd.endDocument();
            response.getWriter().flush();


        } catch (Exception ex) {
            Logger.getLogger(RestMethod.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

          
    private void getNodosInfoDePlan(HttpServletRequest request, HttpServletResponse response) {
        try {

            if (!RestMethod.chkParams(request, "idPlan", "idioma", "rutaServ")) {
                response.getWriter().print("Solicitud incompleta");
                return;
            }

            response.setContentType("text/xml;charset=UTF-8");

            
            int idPlan = Integer.valueOf(request.getParameter("idPlan"));
            String idioma = request.getParameter("idioma");
            String rutaServ = request.getParameter("rutaServ");
            // Obtener el usuario logueado en la consola


            OutputFormat of = new OutputFormat("XML", "UTF-8", true);
            of.setIndent(1);
            of.setIndenting(true);
            XMLSerializer serializer = new XMLSerializer(response.getWriter(), of);
            ContentHandler hd = serializer.asContentHandler();
            hd.startDocument();
            // <nodes>
            hd.startElement("", "", "nodes", null);


            // <node text="X" tipo="plan" idNode="Y" />
            AttributesImpl atts = new AttributesImpl();


            // si el plan tiene trámites añadimos el nodo de trámites
            int numTrams = srvPlan.getTramitesPorPlan(idPlan, ModalidadPlanes.RPM).length;
            if (numTrams > 0) {
                atts = new AttributesImpl();
                atts.addAttribute("", "", "text", "", "Trámites (" + numTrams + ")");
                atts.addAttribute("", "", "load", "", rutaServ + "RestMethod?wsName=GET_NODOS_TRAMS_DE_PLAN&idPlan=" + idPlan + "&idioma=" + URLEncoder.encode(idioma) + "&rutaServ=" + URLEncoder.encode(rutaServ));
                hd.startElement("", "", "node", atts);
                hd.endElement("", "", "node");
            }

            // si el plan tiene planes hijos añadimos el nodo de planes
            int numPlanes = srvPub.findPlanesHijos(idPlan).length;
            if (numPlanes > 0) {
                atts = new AttributesImpl();
                atts.addAttribute("", "", "text", "", "Planes (" + numPlanes + ")");
                atts.addAttribute("", "", "load", "", rutaServ + "RestMethod?wsName=GET_NODOS_PLANES_DE_PLAN&idPlan=" + idPlan + "&idioma=" + URLEncoder.encode(idioma) + "&rutaServ=" + URLEncoder.encode(rutaServ));
                hd.startElement("", "", "node", atts);
                hd.endElement("", "", "node");
            }
            

            // </nodes>
            hd.endElement("", "", "nodes");
            hd.endDocument();
            response.getWriter().flush();


        } catch (Exception ex) {
            Logger.getLogger(RestMethod.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void getNodosTramitesDePlan(HttpServletRequest request, HttpServletResponse response) {
        try {

            if (!RestMethod.chkParams(request, "idPlan", "idioma", "rutaServ")) {
                response.getWriter().print("Solicitud incompleta");
                return;
            }

            response.setContentType("text/xml;charset=UTF-8");

            // Recuperar los parámetros de la llamada
            int idPlan = Integer.valueOf(request.getParameter("idPlan"));
            String rutaServ = request.getParameter("rutaServ");
            String idioma = request.getParameter("idioma");
            // Obtener el usuario logueado en la consola


            Tramite[] ltrams = srvPlan.getTramitesPorPlan(idPlan, ModalidadPlanes.RPM);

            OutputFormat of = new OutputFormat("XML", "UTF-8", true);
            of.setIndent(1);
            of.setIndenting(true);
            XMLSerializer serializer = new XMLSerializer(response.getWriter(), of);
            ContentHandler hd = serializer.asContentHandler();
            hd.startDocument();
            // <nodes>
            hd.startElement("", "", "nodes", null);
            for (Tramite trm : ltrams) {
                AttributesImpl atts = new AttributesImpl();
                atts.addAttribute("", "", "text", "", srvDic.getTraduccion(Tipotramite.class, trm.getIdtipotramite(), idioma));
                atts.addAttribute("", "", "tipo", "", "tramite");
                atts.addAttribute("", "", "idNode", "", String.valueOf(trm.getIden()));
                atts.addAttribute("", "", "load", "", rutaServ + "RestMethod?wsName=GET_NODOS_DOCS_DE_TRAM&idTram=" + trm.getIden() + "&idioma=" + URLEncoder.encode(idioma) + "&rutaServ=" + URLEncoder.encode(rutaServ));
                hd.startElement("", "", "node", atts);
                hd.endElement("", "", "node");
            }
            // </nodes>
            hd.endElement("", "", "nodes");
            hd.endDocument();
            response.getWriter().flush();


        } catch (Exception ex) {
            Logger.getLogger(RestMethod.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
    private void getNodosPlanesDePlanCoor(HttpServletRequest request, HttpServletResponse response) {
        try {

            if (!RestMethod.chkParams(request,"x","y","srs", "idPlan", "rutaServ", "idioma")) {
                response.getWriter().print("Solicitud incompleta");
                return;
            }

            response.setContentType("text/xml;charset=UTF-8");

           // Recuperar los parámetros de la llamada
            double coorX;
            try {
                coorX = Double.parseDouble(request.getParameter("x"));
            } catch (Exception e) {
                // el parámetro idTramite no es un número
                response.getWriter().print("'El parámetro x no es un número'");
                return;
            }

            double coorY;
            try {
                coorY = Double.parseDouble(request.getParameter("y"));
            } catch (Exception e) {
                // el parámetro idTramite no es un número
                response.getWriter().print("'El parámetro y no es un número'");
                return;
            }

            String srs = request.getParameter("srs");
            
            Geometry geom=GeoUtils.GeoFromWKT("POINT(" + coorX + " " + coorY + ")");
            geom=GeoUtils.TransformGeometry(geom,srs, Textos.getTexto("urbrWS", "SRS_Datos"));
            
            int idPlanPadre = Integer.valueOf(request.getParameter("idPlan"));
            String rutaServ = request.getParameter("rutaServ");
            String idioma = request.getParameter("idioma");
            // Obtener el usuario logueado en la consola


            Plan[] lplans = srvPub.findPlanesHijosGeom(geom.toText(),idPlanPadre);

            OutputFormat of = new OutputFormat("XML", "UTF-8", true);
            of.setIndent(1);
            of.setIndenting(true);
            XMLSerializer serializer = new XMLSerializer(response.getWriter(), of);
            ContentHandler hd = serializer.asContentHandler();
            hd.startDocument();
            // <nodes>
            hd.startElement("", "", "nodes", null);

            for (Plan pln : lplans) {
                // <node text="X" tipo="plan" idNode="Y" />
                AttributesImpl atts = new AttributesImpl();
                atts.addAttribute("", "", "text", "", pln.getNombre());
                atts.addAttribute("", "", "tipo", "", "plan");
                atts.addAttribute("", "", "idNode", "", String.valueOf(pln.getIden()));
                hd.startElement("", "", "node", atts);

                // si el plan tiene trámites añadimos el nodo de trámites
                
                int numTrams = srvPlan.getTramitesPorPlan(pln.getIden(), ModalidadPlanes.RPM).length;
                if (numTrams > 0) {
                    atts = new AttributesImpl();
                    atts.addAttribute("", "", "text", "", "Trámites (" + numTrams + ")");
                    atts.addAttribute("", "", "load", "", rutaServ + "RestMethod?wsName=GET_NODOS_TRAMS_DE_PLAN&idPlan=" + pln.getIden() + "&rutaServ=" + URLEncoder.encode(rutaServ) + "&idioma=" + URLEncoder.encode(idioma));
                    hd.startElement("", "", "node", atts);
                    hd.endElement("", "", "node");
                }

                // si el plan tiene planes hijos añadimos el nodo de planes
                int numPlanes = srvPub.findPlanesHijosGeom(geom.toText(),pln.getIden()).length;
                if (numPlanes > 0) {
                    atts = new AttributesImpl();
                    atts.addAttribute("", "", "text", "", "Planes (" + numPlanes + ")");
                    atts.addAttribute("", "", "load", "", rutaServ + "RestMethod?wsName=GET_NODOS_PLANES_DE_PLAN&idPlan=" + pln.getIden() + "&rutaServ=" + URLEncoder.encode(rutaServ) + "&idioma=" + URLEncoder.encode(idioma));
                    hd.startElement("", "", "node", atts);
                    hd.endElement("", "", "node");
                }

                hd.endElement("", "", "node");

            }
            // </nodes>
            hd.endElement("", "", "nodes");
            hd.endDocument();
            response.getWriter().flush();


        } catch (Exception ex) {
            Logger.getLogger(RestMethod.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

             
    private void getNodosPlanesDePlan(HttpServletRequest request, HttpServletResponse response) {
        try {

            if (!RestMethod.chkParams(request, "idPlan", "rutaServ", "idioma")) {
                response.getWriter().print("Solicitud incompleta");
                return;
            }

            response.setContentType("text/xml;charset=UTF-8");

            // Recuperar los parámetros de la llamada
            int idPlanPadre = Integer.valueOf(request.getParameter("idPlan"));
            String rutaServ = request.getParameter("rutaServ");
            String idioma = request.getParameter("idioma");
            // Obtener el usuario logueado en la consola


            Plan[] lplans = srvPlan.getPlanesHijos(idPlanPadre,null,ModalidadPlanes.RPM);

            OutputFormat of = new OutputFormat("XML", "UTF-8", true);
            of.setIndent(1);
            of.setIndenting(true);
            XMLSerializer serializer = new XMLSerializer(response.getWriter(), of);
            ContentHandler hd = serializer.asContentHandler();
            hd.startDocument();
            // <nodes>
            hd.startElement("", "", "nodes", null);


            for (Plan pln : lplans) {
                // <node text="X" tipo="plan" idNode="Y" />
                pln=srvPlan.get(Plan.class,pln.getIden());
                AttributesImpl atts = new AttributesImpl();
                atts.addAttribute("", "", "text", "", pln.getNombre());
                atts.addAttribute("", "", "tipo", "", "plan");
                atts.addAttribute("", "", "idNode", "", String.valueOf(pln.getIden()));
                hd.startElement("", "", "node", atts);

                // si el plan tiene trámites añadimos el nodo de trámites
                int numTrams = srvPlan.getTramitesPorPlan(pln.getIden(), ModalidadPlanes.RPM).length;
                if (numTrams > 0) {
                    atts = new AttributesImpl();
                    atts.addAttribute("", "", "text", "", "Trámites (" + numTrams + ")");
                    atts.addAttribute("", "", "load", "", rutaServ + "RestMethod?wsName=GET_NODOS_TRAMS_DE_PLAN&idPlan=" + pln.getIden() + "&rutaServ=" + URLEncoder.encode(rutaServ) + "&idioma=" + URLEncoder.encode(idioma));
                    hd.startElement("", "", "node", atts);
                    hd.endElement("", "", "node");
                }

                // si el plan tiene planes hijos añadimos el nodo de planes
                int numPlanes = srvPub.findPlanesHijos(pln.getIden()).length;
                if (numPlanes > 0) {
                    atts = new AttributesImpl();
                    atts.addAttribute("", "", "text", "", "Planes (" + numPlanes + ")");
                    atts.addAttribute("", "", "load", "", rutaServ + "RestMethod?wsName=GET_NODOS_PLANES_DE_PLAN&idPlan=" + pln.getIden() + "&rutaServ=" + URLEncoder.encode(rutaServ) + "&idioma=" + URLEncoder.encode(idioma));
                    hd.startElement("", "", "node", atts);
                    hd.endElement("", "", "node");
                }

                hd.endElement("", "", "node");

            }
            // </nodes>
            hd.endElement("", "", "nodes");
            hd.endDocument();
            response.getWriter().flush();


        } catch (Exception ex) {
            Logger.getLogger(RestMethod.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void getNodosDocsDeTram(HttpServletRequest request, HttpServletResponse response) {
        try {

            if (!RestMethod.chkParams(request, "idTram", "idioma", "rutaServ")) {
                response.getWriter().print("Solicitud incompleta");
                return;
            }

            response.setContentType("text/xml;charset=UTF-8");

            // Recuperar los parámetros de la llamada
            int idTram = Integer.valueOf(request.getParameter("idTram"));
            String rutaServ = request.getParameter("rutaServ");
            String idioma = request.getParameter("idioma");
            // Obtener el usuario logueado en la consola


            
            Documento[] hdocs = srvPlan.getDocumentosTramite(idTram);

            OutputFormat of = new OutputFormat("XML", "UTF-8", true);
            of.setIndent(1);
            of.setIndenting(true);
            XMLSerializer serializer = new XMLSerializer(response.getWriter(), of);
            
            Document aXML;
            aXML=XMLws.generarXMLDOC("nodes");
            for (Documento doc : hdocs) {
                Element aNodeGrupo=null;
                NodeList aLista = XMLws.findNode("node[@id=\"grp_" + doc.getIdgrupodocumento() + "\"]", aXML.getDocumentElement());
                if (aLista == null || aLista.getLength() == 0) {
                    aNodeGrupo = XMLws.AddNode(aXML.getDocumentElement(), "node");
                    aNodeGrupo.setAttribute("id", "grp_" + doc.getIdgrupodocumento());
                    aNodeGrupo.setAttribute("tipo", "grupodocumentos");
                } else {
                    aNodeGrupo = (Element) aLista.item(0);
                }
                if (aNodeGrupo!=null){
                    Element aNodeDoc=XMLws.AddNode(aNodeGrupo,"node");
                    aNodeDoc.setAttribute("text", doc.getNombre());
                    aNodeDoc.setAttribute("tipo", "documento");
                    aNodeDoc.setAttribute("idNode", String.valueOf(doc.getIden()));

                    aNodeGrupo.setAttribute("text", srvDic.getTraduccion(Grupodocumento.class, doc.getIdgrupodocumento(), idioma) + " (" + aNodeGrupo.getChildNodes().getLength() + ")" );
                }
            }
            PrintWriter pw =response.getWriter();
            pw.println(XMLws.XmlDocToString(aXML));
            pw.flush();

        } catch (Exception ex) {
            Logger.getLogger(RestMethod.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void getDocURL(HttpServletRequest request, HttpServletResponse response) {
        try {

            if (!RestMethod.chkParams(request, "idDoc", "idioma")) {
                response.getWriter().print("Solicitud incompleta");
                return;
            }
            
            int idDoc = Integer.valueOf(request.getParameter("idDoc"));
            String idioma = request.getParameter("idioma");

            urbrWS ws = new urbrWS();
            String url = ws.urlDoc(idDoc);


            response.getWriter().print(url);
            

        } catch (Exception ex) {
            Logger.getLogger(RestMethod.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void getLayerConfigDeAmbito(HttpServletRequest request, HttpServletResponse response) throws IOException {

        try {

            if (!RestMethod.chkParams(request, "idAmbito")) {
                response.getWriter().print("Parámetros de llmada incompletos");
                log.log(Level.FINER, "[ActionServletConfig.getLayerconfigDeAmbito] Parámetros de llamada incompletos");
                return;
            }

            int idAmbito;
            try {
                idAmbito = Integer.valueOf(request.getParameter("idAmbito"));
            } catch (Exception e) {
                log.log(Level.FINE, "[ActionServletConfig.getLayerconfigDeAmbito] El parámetro idAmbito debe tener valor numérico", e);
                response.getWriter().print("El parámetro idAmbito debe tener valor numérico");
                return;
            }

            InputStream configIS = cvSVC.getLayerConfigDeAmbito(idAmbito);
            if (configIS == null) {
                response.getWriter().print("No se ha podido encontrar la configuración para el ámbito solicitado");
                log.log(Level.FINER, "[ActionServletConfig.getLayerconfigDeAmbito] No se ha podido encontrar la configuración para el ámbito solicitado " + idAmbito);
                return;
            }
            //
            // Stream to the requester
            //
            int length = 0;

            byte[] bbuf = new byte[1024 * 256];
            DataInputStream in = new DataInputStream(configIS);
            ServletOutputStream op = response.getOutputStream();
            response.setContentType("text/xml");
            while ((in != null) && ((length = in.read(bbuf)) != -1)) {
                op.write(bbuf, 0, length);
            }

            in.close();
            op.flush();
            op.close();


        } catch (Exception e) {
            log.log(Level.SEVERE, "[ActionServletConfig.getLayerConfigDeAmbito] Error inesperado ", e);
            response.getWriter().print("Error inesperado " + e.getMessage());
        }
    }
}



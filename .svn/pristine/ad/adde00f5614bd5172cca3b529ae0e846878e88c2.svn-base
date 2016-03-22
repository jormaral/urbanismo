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
import es.mitc.redes.urbanismoenred.data.rpm.diccionario.Tipotramite;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Casoentidaddeterminacion;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Determinacion;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidad;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidaddeterminacion;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidaddeterminacionregimen;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Opciondeterminacion;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Plan;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Regimenespecifico;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.RegulacionEspecifica;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Tramite;
import es.mitc.redes.urbanismoenred.servicios.planeamiento.ServicioDiccionariosLocal;
import es.mitc.redes.urbanismoenred.servicios.planeamiento.ServicioPlaneamientoLocal;
import es.mitc.redes.urbanismoenred.serviciosweb.Textos;
import es.mitc.redes.urbanismoenred.serviciosweb.XMLws;
import es.mitc.redes.urbanismoenred.serviciosweb.consultasCatastro;
import es.mitc.redes.urbanismoenred.serviciosweb.utils.GeoUtils;
import es.mitc.redes.urbanismoenred.serviciosweb.urbrWS;
import es.mitc.redes.urbanismoenred.serviciosweb.utils.GestionEntidades;
import es.mitc.redes.urbanismoenred.utils.serviciosweb.ExcepcionPublicacion;
import es.mitc.redes.urbanismoenred.utils.serviciosweb.ServicioPublicacionLocal;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
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
public class FichaUrbanistica extends HttpServlet {

    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    //
    ServicioPlaneamientoLocal _srvPlan = null;// getServicioPlaneamientoLocal();
    ServicioPublicacionLocal _srvPub = null;//getServicioPublicacionLocal();
    ServicioDiccionariosLocal _srvDic = null;//getServicioDiccionariosLocal();

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

        //response.setContentType("text/html;charset=UTF-8");
        //PrintWriter out = response.getWriter();

        //response.setContentType("text/xml");

        urbrWS ServicioWeb = new urbrWS();
        int i;
        int iEntidad;
        Element aNode;
        Element aNode2;
        Element aNodeDef;
        Element aNodeGrupo;
        Element aNodeCapa;
        Element aNodePlanes;
        NodeList NodosEntidad;
        ServletOutputStream ouputStream;
        Document aXMLRetorno;
        try {

            Logger.getLogger(FichaUrbanistica.class.getName()).log(Level.INFO, "Peticion de ficha urbanistica");
            Document aXML;

            aXMLRetorno = XMLws.generarXMLDOC("FICHA");

            Geometry geoConsulta = GeoUtils.GeoFromWKT("POINT(" + request.getParameter("X") + " " + request.getParameter("Y") + ")");
            if (request.getParameter("X") != null && !request.getParameter("X").equals("")) {
                geoConsulta = GeoUtils.TransformGeometry(geoConsulta, request.getParameter("SRS"), Textos.getTexto("urbrWS", "SRS_Datos"));
            }
            GestionEntidades eb = new GestionEntidades();
            aXML = eb.consultaEntidades(srvPlan(),srvPub(), srvDic(),"POINT(" + request.getParameter("X") + " " + request.getParameter("Y") + ")", request.getParameter("SRS"), request.getParameter("Idioma"));
            if (aXML != null) {
                //response.setContentType("application/pdf");

                NodeList aNodeList;
                NodeList aNodeListCapa;

                aNodeList = XMLws.findNode("/XML/ENTIDADES/AMBITO/CAPA/GRUPO", aXML.getDocumentElement());

                if (aNodeList == null || aNodeList.getLength() == 0) {
                    response.setContentType("text/html");
                    PrintWriter aPrint = response.getWriter();

                    aPrint.print("Sin datos de planeamiento");

                    aPrint.flush();

                    aPrint.close();
                } else {
                    String situacion;
                    situacion = consultasCatastro.InverseGeocode(Double.valueOf(request.getParameter("X")), Double.valueOf(request.getParameter("Y")), request.getParameter("SRS"));
                    if (situacion == null) {
                        if (request.getParameter("SRS") != null) {
                            situacion = "X=" + request.getParameter("X") + " Y=" + request.getParameter("Y") + "  ( " + request.getParameter("SRS") + " )";
                        } else {
                            situacion = "X=" + request.getParameter("X") + " Y=" + request.getParameter("Y");
                        }
                    }
                    aNode = this.AddNode(aXMLRetorno.getDocumentElement(), "SITUACION");
                    String refCatastral = consultasCatastro.referenciaCatastral(Double.valueOf(request.getParameter("X")), Double.valueOf(request.getParameter("Y")), request.getParameter("SRS"));
                    if (refCatastral != null && !refCatastral.equals("") && !refCatastral.isEmpty()) {
                        aNode.setAttribute("refCatastral", refCatastral);
                    }
                    aNode.setTextContent(situacion);
                    aNode = null;

                    aNodePlanes = this.AddNode(aXMLRetorno.getDocumentElement(), "PLANEAMIENTO");//aXMLRetorno.createElement("PLANEAMIENTO");
                    
                    fillPlaneamientoVigente(aNodePlanes, geoConsulta,Integer.parseInt(request.getParameter("idAmbito")),"es");
                    //aXMLRetorno.getDocumentElement().appendChild(aNodePlanes);

                    aNodeListCapa = XMLws.findNode("/XML/ENTIDADES/AMBITO/CAPA", aXML.getDocumentElement());

                    aNodeDef = aXMLRetorno.createElement("DEFINICIONES");
                    aXMLRetorno.getDocumentElement().appendChild(aNodeDef);
                    for (int iCapa = 0; iCapa < aNodeListCapa.getLength(); iCapa++) {
                        aNodeCapa = aXMLRetorno.createElement("CAPA");
                        aNodeCapa.setAttribute("nombre", ((Element) aNodeListCapa.item(iCapa)).getAttribute("nombre"));
                        aNodeCapa.setAttribute("orden", ((Element) aNodeListCapa.item(iCapa)).getAttribute("orden"));
                        aXMLRetorno.getDocumentElement().appendChild(aNodeCapa);

                        aNodeGrupo = (Element) aNodeListCapa.item(iCapa);
                        aNodeList = XMLws.findNode("GRUPO", aNodeGrupo);
                        for (i = 0; i < aNodeList.getLength(); i++) {
                            aNode2 = (Element) aNodeList.item(i);

                            aNode = AddNode(aNodeCapa, "GRUPO");
                            aNode.setAttribute("nombre", aNode2.getAttribute("nombre"));

                            NodosEntidad = aNode2.getChildNodes();

                            for (iEntidad = 0; iEntidad < NodosEntidad.getLength(); iEntidad++) {
                                fillEntidad((Element) NodosEntidad.item(iEntidad), aNode, aNodeDef);
                            }
//                            aNodeCapa.appendChild(aNode);

                            aNode = null;
                        }
                    }

                    aNode = null;

                    JasperReport aReport = JasperCompileManager.compileReport(System.getenv("REDES_PATH") + File.separator + "conf" + File.separator + "PlantillaFicha.jrxml");

                    JRXmlDataSource xmlDataSource = new JRXmlDataSource(aXMLRetorno, "FICHA/CAPA/GRUPO/ENTIDADES/ENTIDAD/CONDICIONES/CONDICION/CASOS/CASO/VALORES/VALOR/REGIMENES-ESPECIFICOS/REGIMEN-ESPECIFICO"); //"/FICHA/DEFINICIONES/DEFINICION"

                    HashMap hashMap = new HashMap();
                    hashMap.put("SRS", Textos.getTexto("urbrWS", "SRS_Datos"));
                    hashMap.put("BBOX", GeoUtils.getWMS_BBOX(geoConsulta));
                    hashMap.put("ID_AMBITO", Integer.parseInt(request.getParameter("idAmbito")));
                    JasperPrint jasperPrint = JasperFillManager.fillReport(aReport, hashMap, xmlDataSource);

                    byte[] bytes = JasperExportManager.exportReportToPdf(jasperPrint);

                    response.setContentType("application/pdf");
                    response.setContentLength(bytes.length);

                    ouputStream = response.getOutputStream();

                    ouputStream.write(bytes, 0, bytes.length);

                    ouputStream.flush();

                    ouputStream.close();
                }
            } else {
                response.setContentType("text/html");
                PrintWriter aPrint = response.getWriter();

                aPrint.print("Sin datos de planeamiento");

                aPrint.flush();

                aPrint.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Logger.getLogger(FichaUrbanistica.class.getName()).log(Level.SEVERE, null, e);
        } finally {
        }
    }

    private void fillPlaneamientoVigente(Element padre, Geometry geoConsulta,int idAmbito,String idioma) throws ExcepcionPublicacion {
        Plan[] planes = srvPub().findPlanesGeom(geoConsulta.toText(),idAmbito,false);
        Integer idtipoTramiteRefundido=Integer.valueOf(es.mitc.redes.urbanismoenred.utils.recursos.textos.Textos.getTexto("diccionario", "tipotramite.refundidoautomatico"));
        for (Plan plan : planes) {
            Tramite aTramite = srvPub().findUltimoTramiteConsolidado(plan.getIden());
            if (aTramite != null) {
                if (aTramite.getIdtipotramite()!=idtipoTramiteRefundido) {
                    Element node = this.AddNode(padre, "PLAN");
                    node.setAttribute("nombre", plan.getNombre());
                    node.setAttribute("tramite", srvDic().getTraduccion(Tipotramite.class, aTramite.getIdtipotramite(), idioma));
                    node.setAttribute("iteracion", String.valueOf(aTramite.getIteracion()));
                }
            }
        }
    }

    private String textoRegimenEspecifico(Regimenespecifico aRegEsp) {
        String aRespuesta = "";

        if (aRegEsp != null) {
            //aRespuesta = "- " + aRegEsp.getNombre();
            if (aRegEsp.getTexto() != null && !aRegEsp.getTexto().equals("")) {
                aRespuesta = aRespuesta + aRegEsp.getTexto();
            }
            Regimenespecifico[] regEspHijos=srvPlan().getRegimenesEspecificosHijos(aRegEsp.getIden());
            for (Regimenespecifico aHijo : regEspHijos) {
                aRespuesta = aRespuesta + "\n" + textoRegimenEspecifico(aHijo);
            }
        }

        return aRespuesta;
    }

    private void fillEntidad(Element aNode, Element aNodeGrupo, Element aNodeDefiniciones) {
        try {
            Entidad aEntidad;
            int iCaso;
            String aTexto;
            int idCaracterGrupoEntidad = 20;
            Element aNodeCondiciones;
            Element aNodeCondicion;
            Element aNodeCasos;
            Element aNodeCaso;
            Element aNodeValores;
            Element aNodeValor;
            Element aNodeRegEsps;
            Element aNodeRegEsp;
            Element aNodeEntidades;
            Element aNodeEntidad;
            Element aNodeDefinicion;
            Element aNodeAdscripciones;
            Element aNodeAdscripcion;
            NodeList aLista;
            String aTipoDet;
            int aOrdenTipoDet;
            String[] valores;

            aNodeEntidades = AddNode(aNodeGrupo, "ENTIDADES");

            aNodeEntidad = AddNode(aNodeEntidades, "ENTIDAD");
            aNodeEntidad.setAttribute("clave", aNode.getAttribute("clave"));
            aNodeEntidad.setAttribute("nombre", aNode.getAttribute("nombre"));

            aEntidad = srvPlan().get(Entidad.class,Integer.valueOf(aNode.getAttribute("id")));
            Determinacion[] dets = srvPlan().getDeterminacionesEntidad(aEntidad.getIden());

            
            aNodeEntidad.setAttribute("orden", String.valueOf(aEntidad.getOrden()));

            idCaracterGrupoEntidad = Integer.valueOf(es.mitc.redes.urbanismoenred.utils.recursos.textos.Textos.getTexto("diccionario", "caracterdeterminacion.grupoentidades"));

            if (dets.length > 1) {
                aNodeCondiciones = AddNode(aNodeEntidad, "CONDICIONES");
                for (Determinacion aDet : dets) {
                    Determinacion det=srvPlan().get(Determinacion.class,aDet.getIden());
                    if (aDet.getIdcaracter() != idCaracterGrupoEntidad) {
                        //Definición de la determinación
                        aLista = XMLws.findNode("DEFINICION[@id=\"" + det.getIden() + "\"]", aNodeDefiniciones);
                        if (aLista == null || aLista.getLength() == 0) {
                            aNodeDefinicion = this.AddNode(aNodeDefiniciones, "DEFINICION");
                            aNodeDefinicion.setAttribute("id", String.valueOf(det.getIden()));
                            aNodeDefinicion.setAttribute("nombre", det.getNombre());
                            aNodeDefinicion.setAttribute("apartado", det.getApartadoCompleto());
                            aNodeDefinicion.setAttribute("orden", String.valueOf(det.getOrden()));
                        } else {
                            aNodeDefinicion = (Element) aLista.item(0);
                        }
                        String definicion = "";
                        if (det.getTexto() != null && !det.getTexto().equals("")) {
                            definicion = definicion + det.getTexto() + "\n";
                        }
                        RegulacionEspecifica[] regulaciones= srvPlan().getRegulacionesEspecificas(det.getIden());
                        if (regulaciones != null) {
                            for (RegulacionEspecifica regesp : regulaciones) {
                                definicion = definicion + regesp.getNombre() + " :  " + regesp.getTexto() + "\n";
                            }
                        }
                        aNodeDefinicion.setTextContent(definicion);
                        
                        aNodeCondicion = this.AddNode(aNodeCondiciones, "CONDICION");
                        aNodeCondicion.setAttribute("determinacion", det.getNombre());
                        aNodeCondicion.setAttribute("iddeterminacion", String.valueOf(det.getIden()));
                        aNodeCondicion.setAttribute("apartado", det.getApartadoCompleto());
                        aNodeCondicion.setAttribute("ordendeterminacion", String.valueOf(det.getOrden()));

                        
                        valores = es.mitc.redes.urbanismoenred.utils.recursos.textos.Textos.getTexto("consola", "caracterregimendirecto").split(",");
                        aTipoDet = "";
                        aOrdenTipoDet = 0;
                        for (String valor : valores) {
                            if (det.getIdcaracter() == Integer.valueOf(valor)) {
                                aTipoDet = "RD";
                                aOrdenTipoDet = 1;
                                break;
                            }
                        }
                        if (aTipoDet.equals("")) {
                            valores = es.mitc.redes.urbanismoenred.utils.recursos.textos.Textos.getTexto("consola", "caracteruso").split(",");
                            for (String valor : valores) {
                                if (det.getIdcaracter() == Integer.valueOf(valor)) {
                                    aTipoDet = "USO";
                                    aOrdenTipoDet = 2;
                                    break;
                                }
                            }
                        }
                        if (aTipoDet.equals("")) {
                            valores = es.mitc.redes.urbanismoenred.utils.recursos.textos.Textos.getTexto("consola", "caracteracto").split(",");
                            for (String valor : valores) {
                                if (det.getIdcaracter() == Integer.valueOf(valor)) {
                                    aTipoDet = "ACTO";
                                    aOrdenTipoDet = 3;
                                    break;
                                }
                            }
                        }
                        //if (aCond.getDeterminacion().getIdcaracter()== Textos.getTexto("consola",

                        aNodeCondicion.setAttribute("tipo", aTipoDet);
                        aNodeCondicion.setAttribute("orden", String.valueOf(aOrdenTipoDet));


                        iCaso = 0;
                        aNodeCasos = this.AddNode(aNodeCondicion, "CASOS");
                        Casoentidaddeterminacion[] casos=srvPlan().getCasos(aEntidad.getIden(), det.getIden());
                        for (Casoentidaddeterminacion aCaso : casos) {
                            aNodeCaso = AddNode(aNodeCasos, "CASO");
                            //if (aCond.getCasoentidaddeterminacions().size()>1){
                            if (aCaso.getNombre() != null) {
                                aNodeCaso.setAttribute("nombre", aCaso.getNombre());
                            } else {
                                aNodeCaso.setAttribute("nombre", "Caso " + String.valueOf(iCaso));
                            }
                            iCaso++;
                            //}

                            aNodeValores = this.AddNode(aNodeCaso, "VALORES");
                            Entidaddeterminacionregimen[] valoresCaso=srvPlan().getRegimenDeCaso(aCaso.getIden());
                            for (Entidaddeterminacionregimen aReg : valoresCaso) {
                                //Determinación de Régimen
                                aNodeValor = this.AddNode(aNodeValores, "VALOR");
                                if (aReg.getDeterminacion() != null) {
                                    aTexto = "";
                                    if (aReg.getOpciondeterminacion() != null) {
                                        aTexto = aTexto + srvPlan().get(Opciondeterminacion.class,aReg.getOpciondeterminacion().getIden()).getDeterminacionByIddeterminacionvalorref().getNombre();
                                    }
                                    Determinacion detRegimen=srvPlan().get(Determinacion.class, aReg.getDeterminacion().getIden());
                                    aNodeValor.setAttribute("determinacionRegimen", detRegimen.getNombre());
                                    aNodeValor.setAttribute("valor", aTexto);
                                    aNodeValor.setAttribute("ordenDetRegimen", String.valueOf(detRegimen.getOrden()));
                                } else {
                                    if (aReg.getOpciondeterminacion() == null) {
                                        if (aReg.getValor() != null && !aReg.getValor().equals("")) {
                                            Determinacion aUnidad = srvPlan().getUnidad(det.getIden());
                                            if (aUnidad != null ) {
                                                aNodeValor.setAttribute("valor", aReg.getValor() + " " + aUnidad.getNombre());
                                            } else {
                                                aNodeValor.setAttribute("valor", aReg.getValor());
                                            }
                                        }
                                    } else {
                                        aNodeValor.setAttribute("valor", srvPlan().get(Opciondeterminacion.class,aReg.getOpciondeterminacion().getIden()).getDeterminacionByIddeterminacionvalorref().getNombre());
                                    }
                                }
                                //Caso de aplicación
                                if (aReg.getCasoentidaddeterminacionByIdcasoaplicacion() != null) {
                                    aNodeValor.setAttribute("caso_aplicacion",srvPlan().get(Determinacion.class,srvPlan().get(Entidaddeterminacion.class, srvPlan().get(Casoentidaddeterminacion.class, aReg.getCasoentidaddeterminacionByIdcasoaplicacion().getIden()).getEntidaddeterminacion().getIden()).getDeterminacion().getIden()).getNombre());
                                }
                                //Régimen Específico
                                Regimenespecifico[] regs=srvPlan().getRegimenesEspecificos(aReg.getIden());
                                if (regs.length > 0) {
//                                    aNodeRegEsps = aXMLRetorno.createElement("REGIMENES-ESPECIFICOS");
//                                    aNodeValor.appendChild(aNodeRegEsps);
                                    aNodeRegEsps = AddNode(aNodeValor, "REGIMENES-ESPECIFICOS");
                                    for (Regimenespecifico aRegEsp : regs) {
                                        if (aRegEsp.getRegimenespecifico() == null) { //Solo los padres
                                            aNodeRegEsp = this.AddNode(aNodeRegEsps, "REGIMEN-ESPECIFICO");
                                            aNodeRegEsp.setAttribute("nombre", aRegEsp.getNombre());
                                            aNodeRegEsp.setTextContent(textoRegimenEspecifico(aRegEsp));
                                        }
                                    }
                                } else {
                                    aNodeRegEsps = AddNode(aNodeValor, "REGIMENES-ESPECIFICOS");
                                    aNodeRegEsp = AddNode(aNodeRegEsps, "REGIMEN-ESPECIFICO");
                                }
                            }
                            //Vinculos
                            Casoentidaddeterminacion[] casosVinc=srvPlan().getCasosVinculados(aCaso.getIden());
                            for (Casoentidaddeterminacion casoVinc : casosVinc) {
                                Element aNodeVinculo = this.AddNode(aNodeCaso, "VINCULO");
                                aNodeVinculo.setAttribute("caso_vinculado", srvPlan().get(Determinacion.class, srvPlan().get(Entidaddeterminacion.class,casoVinc.getEntidaddeterminacion().getIden()).getDeterminacion().getIden()).getNombre());
                            }
                        }
                        aNodeCasos.setAttribute("ncasos", String.valueOf(aNodeCasos.getChildNodes().getLength()));
                    } else {
                        //Logger.getLogger(FichaUrbanistica.class.getName()).log(Level.SEVERE, null, "Grupo");
                    }
                }
            } else {
                aNodeCondiciones = AddNode(aNodeEntidad, "CONDICIONES");
                aNodeCondicion = AddNode(aNodeCondiciones, "CONDICION");
                aNodeCasos = AddNode(aNodeCondicion, "CASOS");
                aNodeCaso = AddNode(aNodeCasos, "CASO");
                aNodeValores = AddNode(aNodeCaso, "VALORES");
                aNodeValor = AddNode(aNodeValores, "VALOR");
                aNodeRegEsps = AddNode(aNodeValor, "REGIMENES-ESPECIFICOS");
                aNodeRegEsp = AddNode(aNodeRegEsps, "REGIMEN-ESPECIFICO");
            }
            NodeList nodeAdscripciones = XMLws.findNode("ADSCRIPCION", aNode);
            aNodeAdscripciones = AddNode(aNodeEntidad, "ADSCRIPCIONES");
            for (int iAds = 0; iAds < nodeAdscripciones.getLength(); iAds++) {
                Element ads = (Element) nodeAdscripciones.item(iAds);
                aNodeAdscripcion = AddNode(aNodeAdscripciones, "ADSCRIPCION");
                if (ads.getAttribute("tipo") != null) {
                    aNodeAdscripcion.setAttribute("tipo", ads.getAttribute("tipo"));
                }
                if (ads.getAttribute("origen") != null) {
                    aNodeAdscripcion.setAttribute("origen", ads.getAttribute("origen"));
                }
                if (ads.getAttribute("destino") != null) {
                    aNodeAdscripcion.setAttribute("destino", ads.getAttribute("destino"));
                }
                if (ads.getAttribute("cuantia") != null) {
                    aNodeAdscripcion.setAttribute("cuantia", ads.getAttribute("cuantia"));
                }
                if (ads.getAttribute("unidad") != null) {
                    aNodeAdscripcion.setAttribute("unidad", ads.getAttribute("unidad"));
                }
                if (ads.getAttribute("texto") != null) {
                    aNodeAdscripcion.setAttribute("texto", ads.getAttribute("texto"));
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(FichaUrbanistica.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private Element AddNode(Element padre, String nombre) {
        Element aNode = padre.getOwnerDocument().createElement(nombre);
        padre.appendChild(aNode);
        return aNode;
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
        return "Generación de XML para ficha urbanística";
    }// </editor-fold>
}

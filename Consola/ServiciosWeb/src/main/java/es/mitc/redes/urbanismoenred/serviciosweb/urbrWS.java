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
package es.mitc.redes.urbanismoenred.serviciosweb;

import es.mitc.redes.urbanismoenred.serviciosweb.utils.GeoUtils;
import com.vividsolutions.jts.geom.Envelope;
import es.mitc.redes.urbanismoenred.data.rpm.diccionario.Ambito;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Documento;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidad;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Plan;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Tramite;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Documentoshp;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import com.vividsolutions.jts.geom.Geometry;
import es.mitc.redes.urbanismoenred.geoserverapi.urbrGeoserver;
import com.vividsolutions.jts.geom.Point;
import es.mitc.redes.urbanismoenred.data.rpm.diccionario.Centroproduccion;
import es.mitc.redes.urbanismoenred.data.rpm.diccionario.Grupodocumento;
import es.mitc.redes.urbanismoenred.data.rpm.diccionario.Tipodocumento;
import es.mitc.redes.urbanismoenred.data.rpm.diccionario.Tipotramite;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Determinacion;
import es.mitc.redes.urbanismoenred.servicios.planeamiento.ModalidadPlanes;
import es.mitc.redes.urbanismoenred.servicios.planeamiento.ServicioDiccionariosLocal;
import es.mitc.redes.urbanismoenred.servicios.planeamiento.ServicioPlaneamientoLocal;
import es.mitc.redes.urbanismoenred.serviciosweb.utils.GestionEntidades;
import es.mitc.redes.urbanismoenred.utils.serviciosweb.ServicioPublicacionLocal;
import java.io.File;
import org.apache.commons.io.FileUtils;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.CDATASection;
/**
 *
 * @author a.centeno
 */
@WebService(serviceName = "urbrWS")
public class urbrWS {

    ServicioPlaneamientoLocal _srvPlan = null;// getServicioPlaneamientoLocal();
    ServicioPublicacionLocal _srvPub = null;// getServicioPublicacionLocal();
    ServicioDiccionariosLocal _srvDic = null;// getServicioDiccionariosLocal();

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
    //-----------------------------------------------------------------

    /**
     * Web service operation
     */
    @WebMethod(operationName = "ambitosPadre")
    public String ambitosPadre(@WebParam(name = "idioma") String idioma) {

        //TODO write your implementation code here:
        try {
            Ambito[] Ambitos;
            Element aNode;
            Element aNodeValue;
            Document aXML;


            if (idioma == null || idioma.isEmpty()) {
                idioma = "es";
            }
            Ambitos = srvPub().findAmbitosPadres();
            aXML = XMLws.generarXMLDOC("AMBITOS");
            for (Ambito aAmbito : Ambitos) {
                aNode = aXML.createElement("AMBITO");
                aNodeValue = aXML.createElement("id");
                aNodeValue.setTextContent(String.valueOf(aAmbito.getIden()));
                aNode.appendChild(aNodeValue);
                aNodeValue = null;
                aNodeValue = aXML.createElement("nombre");
                aNodeValue.setTextContent(srvDic().getTraduccion(Ambito.class, aAmbito.getIden(), idioma));
                aNode.appendChild(aNodeValue);
                aNodeValue = null;
                aXML.getDocumentElement().appendChild(aNode);
            }

            return XMLws.XmlDocToString(aXML);
        } catch (Exception ex) {
            Logger.getLogger(urbrWS.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }


    }

    @WebMethod(operationName = "ambitosWithPlan")
    public String ambitosWithPlan(@WebParam(name = "idioma") String idioma) {

        //TODO write your implementation code here:
        try {
            Ambito[] Ambitos;
            Element aNode;
            Element aNodeValue;
            Document aXML;
            if (idioma == null || idioma.isEmpty()) {
                idioma = "es";
            }
            Ambitos = srvPub().findAmbitosWithPlan();
            aXML = XMLws.generarXMLDOC("AMBITOS");
            for (Ambito aAmbito : Ambitos) {
                aNode = aXML.createElement("AMBITO");
                aNodeValue = aXML.createElement("id");
                aNodeValue.setTextContent(String.valueOf(aAmbito.getIden()));
                aNode.appendChild(aNodeValue);
                aNodeValue = null;
                aNodeValue = aXML.createElement("nombre");
                aNodeValue.setTextContent(srvDic().getTraduccion(Ambito.class, aAmbito.getIden(), idioma));
                aNode.appendChild(aNodeValue);
                aNodeValue = null;
                for (int iNodo = 0; iNodo < aXML.getDocumentElement().getChildNodes().getLength(); iNodo++) {
                    Element aNodoAfter = (Element) aXML.getDocumentElement().getChildNodes().item(iNodo);
                    if (srvDic().getTraduccion(Ambito.class, aAmbito.getIden(), idioma).compareTo(aNodoAfter.getLastChild().getTextContent()) < 0) {
                        aXML.getDocumentElement().insertBefore(aNode, aNodoAfter);
                        break;
                    }
                }
                if (aNode.getParentNode() == null) {
                    aXML.getDocumentElement().appendChild(aNode);
                }

            }
            return XMLws.XmlDocToString(aXML);
        } catch (Exception ex) {
            Logger.getLogger(urbrWS.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }


    }

    @WebMethod(operationName = "provinciasCallejero")
    public String provinciasCallejero() {
        //TODO write your implementation code here:
        try {
            return XMLws.XmlDocToString(consultasCatastro.getProvincias());
        } catch (Exception ex) {
            Logger.getLogger(urbrWS.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @WebMethod(operationName = "tiposCalle")
    public String tiposCalle() {
        //TODO write your implementation code here:
        try {
            return XMLws.XmlDocToString(consultasCatastro.getTiposVias());
        } catch (Exception ex) {
            Logger.getLogger(urbrWS.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @WebMethod(operationName = "getVias")
    public String getVias(@WebParam(name = "provincia") String provincia,
            @WebParam(name = "municipio") String municipio,
            @WebParam(name = "abrevTipo") String abrevTipo,
            @WebParam(name = "nombreVia") String nombreVia) {
        //TODO write your implementation code here:
        try {
            if (abrevTipo.equals("?")) {
                abrevTipo = "";
            }
            if (nombreVia.equals("?")) {
                nombreVia = "";
            }

            return XMLws.XmlDocToString(consultasCatastro.getVias(provincia, municipio, abrevTipo, nombreVia));
        } catch (Exception ex) {
            Logger.getLogger(urbrWS.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }



    @WebMethod(operationName = "getLeyenda")
    public String getLeyenda(@WebParam(name = "idHoja") int idHoja) {
        //TODO write your implementation code here:
        try {
            Documentoshp aHoja;
            aHoja = srvPlan().get(Documentoshp.class, idHoja);
            if (aHoja != null) {
                String urlRetorno = Textos.getTexto("urbrWS", "URL_Documentos") + "?leyenda=true&hoja=" + aHoja.getHoja() + "&idDoc=" + aHoja.getDocumento().getIden();
                return urlRetorno;
            } else {
                return null;
            }
        } catch (Exception ex) {
            Logger.getLogger(urbrWS.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @WebMethod(operationName = "creaWMSDoc")
    public String creaWMSDoc(@WebParam(name = "idHoja") int idHoja) {
        //TODO write your implementation code here:
        try {
            Documentoshp aHoja;
            aHoja = srvPlan().get(Documentoshp.class, idHoja);
            if (aHoja != null) {
                Documento aDocumento = srvPlan().get(Documento.class, aHoja.getDocumento().getIden());
                Tramite tram = srvPlan().get(Tramite.class, aDocumento.getTramite().getIden());
                Geometry aHojaGeom = GeoUtils.GeoFromWKT(aHoja.getGeom());
                File dir=srvPlan().getArchivo(tram.getCodigofip(),aDocumento.getArchivo());
                return urbrGeoserver.creaServicioImagen(dir.getAbsolutePath() + File.separator + aHoja.getHoja().toLowerCase(), idHoja, aHojaGeom.getEnvelopeInternal().getMinX(), aHojaGeom.getEnvelopeInternal().getMinY(), aHojaGeom.getEnvelopeInternal().getMaxX(), aHojaGeom.getEnvelopeInternal().getMaxY());
                //return urbrGeoserver.creaServicioImagen(Textos.getTexto("urbrWS", "Path_Docs") + "/" + tram.getCodigofip() + "/" + aDocumento.getArchivo() + "/" + aHoja.getHoja().toLowerCase(), idHoja, aHojaGeom.getEnvelopeInternal().getMinX(), aHojaGeom.getEnvelopeInternal().getMinY(), aHojaGeom.getEnvelopeInternal().getMaxX(), aHojaGeom.getEnvelopeInternal().getMaxY());
            } else {
                return null;
            }
        } catch (Exception ex) {
            Logger.getLogger(urbrWS.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @WebMethod(operationName = "coordenadasRefCatastral")
    public String coordenadasRefCatastral(
            @WebParam(name = "refCatastral") String refCatastral,
            @WebParam(name = "srs") String srs) {
        //TODO write your implementation code here:

        try {
            return consultasCatastro.CoordenadasRefCatastral(refCatastral, srs);
        } catch (Exception ex) {
            Logger.getLogger(urbrWS.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @WebMethod(operationName = "referenciaCatastral")
    public String referenciaCatastral(
            @WebParam(name = "provincia") String provincia,
            @WebParam(name = "municipio") String municipio,
            @WebParam(name = "abrevTipo") String abrevTipo,
            @WebParam(name = "nombreVia") String nombreVia,
            @WebParam(name = "numero") String numero) {
        //TODO write your implementation code here:
        Document aXML;
        Document aResp;
        NodeList aLista;
        int i;
        Node aNode;
        Node aNodeReferencia;

        try {
            aResp = consultasCatastro.getNumero(provincia, municipio, abrevTipo, nombreVia, numero,true);
            aLista = XMLws.findNode("/consulta_numerero/numerero/nump/pc", aResp.getDocumentElement());
            if (aLista == null || aLista.getLength()==0) {
                aResp = consultasCatastro.getNumero(provincia, municipio, abrevTipo, nombreVia, numero,false);
                aLista = XMLws.findNode("/consulta_numerero/numerero/nump/pc", aResp.getDocumentElement());
            }
            if (aLista != null) {
                aXML = XMLws.generarXMLDOC("REFERENCIAS");
                for (i = 0; i < aLista.getLength(); i++) {
                    aNode = aLista.item(i);
                    aNodeReferencia = aXML.createElement("REFERENCIA");
                    aNodeReferencia.setTextContent(aNode.getTextContent().replace(" ", ""));
                    aXML.getDocumentElement().appendChild(aNodeReferencia);
                }
                return XMLws.XmlDocToString(aXML);
            } else {
                return null;
            }
        } catch (Exception ex) {
            Logger.getLogger(urbrWS.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @WebMethod(operationName = "getCoordenadas")
    public String getCoordenadas(
            @WebParam(name = "provincia") String provincia,
            @WebParam(name = "municipio") String municipio,
            @WebParam(name = "abrevTipo") String abrevTipo,
            @WebParam(name = "nombreVia") String nombreVia,
            @WebParam(name = "numero") String numero,
            @WebParam(name = "srs") String srs) {
        //TODO write your implementation code here:

        Document aResp;
        NodeList aLista;
        Node aNode;


        try {
			aResp = consultasCatastro.getNumero(provincia, municipio, abrevTipo, nombreVia, numero,true);
            aLista = XMLws.findNode("/consulta_numerero/numerero/nump/pc", aResp.getDocumentElement());
            if (aLista == null || aLista.getLength() == 0) {
                aResp = consultasCatastro.getNumero(provincia, municipio, abrevTipo, nombreVia, numero,false);
                aLista = XMLws.findNode("/consulta_numerero/numerero/nump/pc", aResp.getDocumentElement());
            }
            if (aLista != null && aLista.getLength() > 0) {
                aNode = aLista.item(0);
                return coordenadasRefCatastral(aNode.getTextContent().replace(" ", ""), srs);
            } else {
                return null;
            }


        } catch (Exception ex) {
            Logger.getLogger(urbrWS.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @WebMethod(operationName = "municipiosCallejero")
    public String municipiosCallejero(
            @WebParam(name = "provincia") String provincia,
            @WebParam(name = "municipio") String municipio) {
        //TODO write your implementation code here:
        try {
            if (municipio.equals("?")) {
                municipio = "";
            }

            return XMLws.XmlDocToString(consultasCatastro.getMunicipios(provincia, municipio));
        } catch (Exception ex) {
            Logger.getLogger(urbrWS.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @WebMethod(operationName = "ambitosHijo")
    public String ambitosHijo(
            @WebParam(name = "idioma") String idioma,
            @WebParam(name = "idPadre") int idPadre) {

        //TODO write your implementation code here:
        try {
            Ambito[] Ambitos;
            Element aNode;
            Element aNodeValue;
            Document aXML;
            if (idioma == null || idioma.isEmpty()) {
                idioma = "es";
            }
            Ambitos = srvPub().findAmbitosHijos(idPadre);
            aXML = XMLws.generarXMLDOC("AMBITOS");
            for (Ambito aAmbito : Ambitos) {
                aNode = aXML.createElement("AMBITO");
                aNodeValue = aXML.createElement("id");
                aNodeValue.setTextContent(String.valueOf(aAmbito.getIden()));
                aNode.appendChild(aNodeValue);
                aNodeValue = null;
                aNodeValue = aXML.createElement("nombre");
                aNodeValue.setTextContent(srvDic().getTraduccion(Ambito.class, aAmbito.getIden(), idioma));
                aNode.appendChild(aNodeValue);
                aNodeValue = null;
                aXML.getDocumentElement().appendChild(aNode);
            }

            return XMLws.XmlDocToString(aXML);
        } catch (Exception ex) {
            Logger.getLogger(urbrWS.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }


    }

    @WebMethod(operationName = "entidadesFromNombre_Plan")
    public String entidadesFromNombre_Plan(
            @WebParam(name = "nombre") String Nombre,
            @WebParam(name = "nombrePlan") String NombrePlan,
            @WebParam(name = "idAmbito") int idAmbito) {

        Entidad[] aEntidades;
        Element aNode;
        Document aXML;

        try {
            aXML = XMLws.generarXMLDOC("ENTIDADES");
            aEntidades = srvPub().findEntidadesLikeName_Plan(Nombre, NombrePlan, idAmbito);
            if (aEntidades != null) {
                for (Entidad aEntidad : aEntidades) {
                    String[] wkt = srvPlan().getGeometriaEntidad(aEntidad.getIden());
                    if (wkt != null && wkt.length > 0) {
                        Geometry geom = GeoUtils.GeoFromWKT(wkt[0]);
                        if (geom != null) {
                            aNode = aXML.createElement("ENTIDAD");
                            aXML.getDocumentElement().appendChild(aNode);
                            aNode.setAttribute("id", String.valueOf(aEntidad.getIden()));
                            aNode.setAttribute("nombre", aEntidad.getNombre());
                            aNode.setAttribute("geo_type", geom.getGeometryType().toUpperCase());
                        }
                    }
                }
            }
            return XMLws.XmlDocToString(aXML);
        } catch (Exception ex) {
            Logger.getLogger(urbrWS.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }


    }

    @WebMethod(operationName = "entidadesFromNombre")
    public String entidadesFromNombre(
            @WebParam(name = "nombre") String Nombre,
            @WebParam(name = "idAmbito") int idAmbito) {

        Entidad[] aEntidades;
        Element aNode;
        Document aXML;

        try {
            aXML = XMLws.generarXMLDOC("ENTIDADES");
            aEntidades = srvPub().findEntidadesLikeName(Nombre, idAmbito);
            if (aEntidades != null) {
                for (Entidad aEntidad : aEntidades) {
                    String[] wkt = srvPlan().getGeometriaEntidad(aEntidad.getIden());
                    if (wkt != null && wkt.length > 0) {
                        Geometry geom = GeoUtils.GeoFromWKT(wkt[0]);
                        if (geom != null) {
                            aNode = aXML.createElement("ENTIDAD");
                            aXML.getDocumentElement().appendChild(aNode);
                            aNode.setAttribute("id", String.valueOf(aEntidad.getIden()));
                            aNode.setAttribute("nombre", aEntidad.getNombre());
                            aNode.setAttribute("geo_type", geom.getGeometryType().toUpperCase());
                        }
                    }
                }
            }
            return XMLws.XmlDocToString(aXML);
        } catch (Exception ex) {
            Logger.getLogger(urbrWS.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @WebMethod(operationName = "planesFromNombre")
    public String planesFromNombre(
            @WebParam(name = "nombre") String Nombre,
            @WebParam(name = "idAmbito") int idAmbito) {


        Plan[] aPlanes;
        Element aNode;
        Document aXML;

        try {
            aXML = XMLws.generarXMLDOC("PLANES");
            aPlanes = srvPub().findPlanesLikeName(Nombre, idAmbito);

            for (Plan aPlan : aPlanes) {
                if (!srvPub().isPlanRefundido(aPlan.getIden())) {
                    String[] wkt = srvPlan().getPoligonosPlan(aPlan.getIden());
                    if (wkt != null && wkt.length > 0) {
                        Geometry geom = GeoUtils.GeoFromWKT(wkt[0]);
                        if (geom != null) {
                            aNode = aXML.createElement("PLAN");
                            aXML.getDocumentElement().appendChild(aNode);
                            aNode.setAttribute("nombre", aPlan.getNombre());
                            aNode.setAttribute("id", String.valueOf(aPlan.getIden()));
                            aNode.setAttribute("geo_type", geom.getGeometryType().toUpperCase());
                        }
                    }
                }
            }

            return XMLws.XmlDocToString(aXML);
        } catch (Exception ex) {
            Logger.getLogger(urbrWS.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }


    }

    @WebMethod(operationName = "estadoRegistro")
    public String estadoRegistro(
            @WebParam(name = "idAmbito") int idAmbito,
            @WebParam(name = "idioma") String Idioma) {

        Plan[] aPlanes;
        Element aNode;
        Element aNodeTramites = null;
        Element aNodeTramite = null;

        Document aXML;

        try {
            if (Idioma == null || Idioma.isEmpty()) {
                Idioma = "es";
            }
            aXML = XMLws.generarXMLDOC("PLANES");
            aPlanes = srvPub().findPlanesLikeName("", idAmbito);
            aXML.getDocumentElement().setAttribute("count", String.valueOf(aPlanes.length));
            for (Plan aPlan : aPlanes) {
                aNode = aXML.createElement("PLAN");
                aXML.getDocumentElement().appendChild(aNode);
                aNode.setAttribute("nombre", aPlan.getNombre());
                aNode.setAttribute("id", String.valueOf(aPlan.getIden()));
                Tramite[] tramites = srvPlan().getTramitesPorPlan(aPlan.getIden(), ModalidadPlanes.RPM);
                for (Tramite aTramite : tramites) {
                    if (aNodeTramites == null) {
                        aNodeTramites = aXML.createElement("TRAMITES");
                        aNodeTramites.setAttribute("count", String.valueOf(tramites.length));
                        aNode.appendChild(aNodeTramites);
                    }
                    aNodeTramite = aXML.createElement("TRAMITE");
                    aNodeTramite.setAttribute("id", String.valueOf(aTramite.getIden()));
                    aNodeTramite.setAttribute("nombre", srvDic().getTraduccion(Tipotramite.class, aTramite.getIdtipotramite(), Idioma));
                    aNodeTramite.setAttribute("iteracion", String.valueOf(aTramite.getIteracion()));
                    aNodeTramites.appendChild(aNodeTramite);

                }
                aNodeTramites = null;
                aXML.getDocumentElement().appendChild(aNode);
            }

            return XMLws.XmlDocToString(aXML);
        } catch (Exception ex) {
            Logger.getLogger(urbrWS.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }


    }

    @WebMethod(operationName = "gisPrevio")
    public String gisPrevio() {
        return Textos.getTexto("urbrWS", "gis_Previo");
    }

    @WebMethod(operationName = "estadoDocumentosTramite")
    public String estadoDocumentosTramite(
            @WebParam(name = "idTramite") int idTramite,
            @WebParam(name = "idioma") String Idioma) {

        Tipodocumento aTipoDoc;
        NodeList aNodeList;
        double aSize;
        Element aNodeTipoDoc = null;
        Document aXML = null;

        try {
            if (Idioma == null || Idioma.isEmpty()) {
                Idioma = "es";
            }
            aSize = 0;
            Integer nHojas = 0;
            Documento[] docs = srvPlan().getDocumentosTramite(idTramite);
            Tramite tramite = srvPlan().get(Tramite.class,idTramite);
            for (Documento aDocumento : docs) {
                if (aXML == null) {
                    aXML = XMLws.generarXMLDOC("DOCUMENTOS");
                    aXML.getDocumentElement().setAttribute("idTramite", String.valueOf(idTramite));
                    aXML.getDocumentElement().setAttribute("count", String.valueOf(docs.length));
                }
                Documentoshp[] hojas = srvPlan().getHojas(aDocumento.getIden());
                if (hojas != null) {
                    nHojas = nHojas + hojas.length;
                }
                aSize = aSize + getSizeDocument(aDocumento,tramite.getCodigofip());
                aNodeList = XMLws.findNode(srvDic().getTraduccion(Tipodocumento.class, aDocumento.getIdtipodocumento(), Idioma), aXML.getDocumentElement());
                if (aNodeList != null && aNodeList.getLength() > 0) {
                    aNodeTipoDoc = (Element) aNodeList.item(0);
                    aNodeTipoDoc.setAttribute("count", String.valueOf(Integer.parseInt(aNodeTipoDoc.getAttribute("count")) + 1));
                } else {
                    aNodeTipoDoc = XMLws.AddNode(aXML.getDocumentElement(), srvDic().getTraduccion(Tipodocumento.class, aDocumento.getIdtipodocumento(), Idioma));
                    aNodeTipoDoc.setAttribute("count", "1");
                }
            }
            if (aXML != null) {
                aXML.getDocumentElement().setAttribute("geo", String.valueOf(nHojas));
                if (aSize>0){
                    aXML.getDocumentElement().setAttribute("sizeMB", String.valueOf(aSize / Math.pow(1024, 2)));
                }
            }

            return XMLws.XmlDocToString(aXML);

        } catch (Exception ex) {
            Logger.getLogger(urbrWS.class.getName()).log(Level.SEVERE, null, ex);
            return "";
        }
    }

    @WebMethod(operationName = "fechaRefundido")
    public String fechaRefundido(
            @WebParam(name = "idAmbito") int idAmbito) {
		return getDatosRefundido(idAmbito, Textos.getTexto("urbrWS", "URL_FIPs"));
    }

    /**
     * El 13/02/2012 se solicita que se pueda obtener sólo el XML del FIP 
     * correspondiente a un ámbito. 
     * Se opta por no modificar el comportamiento ni la interfaz que ya existe
     * del servicio Web, por lo que se añade este método nuevo.
     * Se pide igualmente que la llamada a GetDocumento no se modifique, pero
     * que se utilice una URL que no genere problemas a la hora de ser 
     * decodificada, por lo que se crea un nuevo servlet cuya URL debe estar
     * especificada en la propiedad URL_FIPs del archivo urbrWS.properties.
     * 03/04/2013: se solicita que fechaRefundido devuelva sólo el XML y que
     * se cambie fechaRefundidoXML por fechaRefundidoConDocumentos
     * 
     * @param idAmbito
     * @return
     */
    @WebMethod(operationName = "fechaRefundidoConDocumentos")
    public String fechaRefundidoConDocumentos(
            @WebParam(name = "idAmbito") int idAmbito) {
		return getDatosRefundido(idAmbito, Textos.getTexto("urbrWS", "URL_Documentos") + "?fip1=true&idAmbito=");
    }
	
	private String getDatosRefundido(int idAmbito, String url) {
    	Tramite aTramite;
        Document aXML = null;

        try {
            aXML = XMLws.generarXMLDOC("REFUNDIDO");
            aTramite = srvPub().findUltimoTramiteRefundido(idAmbito);
            if (aTramite!=null){
                aXML.getDocumentElement().setAttribute("fecha", aTramite.getFechaconsolidacion().toString());
                
                CDATASection cdata=aXML.createCDATASection(url+idAmbito);
                aXML.getDocumentElement().appendChild(cdata);
                //aXML.getDocumentElement().setAttribute("fip1", Textos.getTexto("urbrWS", "URL_Documentos") + "?fip1=true&idAmbito=" + idAmbito);
                String strXml= XMLws.XmlDocToString(aXML);
                Logger.getLogger(urbrWS.class.getName()).log(Level.INFO, "Cadena devuelta: " + strXml);
                return strXml;
            }else{
                return null;
            }
        } catch (Exception ex) {
            Logger.getLogger(urbrWS.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @WebMethod(operationName = "consultaGraficaExtendida")
    public String consultaGraficaExtendida(
            @WebParam(name = "wktGeometry") String wktGeometry,
            @WebParam(name = "srs") String srs,
            @WebParam(name = "idioma") String Idioma) {

        //TODO write your implementation code here:
        try {
            Document aXML;
            Element aNode;
            Geometry aGeometriaTransformada;
            Geometry aGeometriaConsulta;


            aGeometriaConsulta = GeoUtils.GeoFromWKT(wktGeometry);

            if (srs != null) {
                if (srs.equals("")) {
                    aGeometriaTransformada = aGeometriaConsulta;
                    srs = Textos.getTexto("urbrWS", "SRS_Datos");
                } else {
                    aGeometriaTransformada = GeoUtils.TransformGeometry(aGeometriaConsulta, srs, Textos.getTexto("urbrWS", "SRS_Datos"));
                }
            } else {
                aGeometriaTransformada = aGeometriaConsulta;
            }

            aXML = XMLws.generarXMLDOC("RESPUESTA");
            aNode = aXML.createElement("SITUACION");
            aNode.setTextContent(consultasCatastro.InverseGeocode(aGeometriaConsulta.getCentroid().getX(), aGeometriaConsulta.getCentroid().getY(), srs));
            aXML.getDocumentElement().appendChild(aNode);
            aNode = null;
            GestionEntidades eb = new GestionEntidades();
            aXML.getDocumentElement().appendChild(eb.getEntidadesParaConsultaExtendida(srvPlan(),srvPub(), srvDic(), aGeometriaTransformada, aXML, Idioma));
            aXML.getDocumentElement().appendChild(getPlanos(aGeometriaTransformada, aXML, Idioma));

            return XMLws.XmlDocToString(aXML);
        } catch (Exception ex) {
            Logger.getLogger(urbrWS.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }


    }

    @WebMethod(operationName = "consultaGrafica")
    public String consultaGrafica(
            @WebParam(name = "wktGeometry") String wktGeometry,
            @WebParam(name = "srs") String srs,
            @WebParam(name = "idioma") String Idioma) {

        //TODO write your implementation code here:
        try {
            Document aXML;
            Element aNode;
            Geometry aGeometriaTransformada;
            Geometry aGeometriaConsulta;


            aGeometriaConsulta = GeoUtils.GeoFromWKT(wktGeometry);

            if (srs != null) {
                if (srs.equals("")) {
                    aGeometriaTransformada = aGeometriaConsulta;
                    srs = Textos.getTexto("urbrWS", "SRS_Datos");
                } else {
                    aGeometriaTransformada = GeoUtils.TransformGeometry(aGeometriaConsulta, srs, Textos.getTexto("urbrWS", "SRS_Datos"));
                }
            } else {
                aGeometriaTransformada = aGeometriaConsulta;
            }

            aXML = XMLws.generarXMLDOC("RESPUESTA");
            aNode = aXML.createElement("SITUACION");
            aNode.setTextContent(consultasCatastro.InverseGeocode(aGeometriaConsulta.getCentroid().getX(), aGeometriaConsulta.getCentroid().getY(), srs));
            aXML.getDocumentElement().appendChild(aNode);
            aNode = null;
            GestionEntidades eb = new GestionEntidades();
            aXML.getDocumentElement().appendChild(eb.getEntidades(srvPlan(),srvPub(), srvDic(),aGeometriaTransformada, aXML, Idioma));
            aXML.getDocumentElement().appendChild(getPlanos(aGeometriaTransformada, aXML, Idioma));

            return XMLws.XmlDocToString(aXML);
        } catch (Exception ex) {
            Logger.getLogger(urbrWS.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }


    }

    @WebMethod(operationName = "idAmbito")
    public int idAmbito(
            @WebParam(name = "X") Double X,
            @WebParam(name = "Y") Double Y,
            @WebParam(name = "srs") String srs) {

        //TODO write your implementation code here:
        try {

            Point aPunto = (Point) GeoUtils.GeoFromWKT("POINT(" + X + " " + Y + ")");
            aPunto = (Point) GeoUtils.TransformGeometry(aPunto, srs, Textos.getTexto("urbrWS", "SRS_Datos"));
            if (aPunto!=null){
                Ambito[] aAmbitos = srvPub().findAmbitosFromGeo(aPunto.toText());
                if (aAmbitos != null && aAmbitos.length > 0) {
                    return aAmbitos[0].getIden();
                } else {
                    return -1;
                }
            }else{
                return -1;
            }
        } catch (Exception ex) {
            Logger.getLogger(urbrWS.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        }


    }

    @WebMethod(operationName = "extensionAmbito")
    public String extensionAmbito(
            @WebParam(name = "idAmbito") int idAmbito,
            @WebParam(name = "srs") String srs) {

        //TODO write your implementation code here:
        try {
            String[] aWKT = srvDic().getPoligonosAmbito(idAmbito);
            if (aWKT != null) {
                Geometry aExtension = GeoUtils.GeoFromWKT(aWKT[0]);
                aExtension = GeoUtils.TransformGeometry(aExtension, Textos.getTexto("urbrWS", "SRS_Datos"), srs);
                Envelope aEnvelope;
                aEnvelope = aExtension.getEnvelope().getEnvelopeInternal();
                return aEnvelope.getMinX() + "|" + aEnvelope.getMinY() + "|" + aEnvelope.getMaxX() + "|" + aEnvelope.getMaxY();
            } else {
                return "|||";
            }
        } catch (Exception ex) {
            Logger.getLogger(urbrWS.class.getName()).log(Level.SEVERE, null, ex);
            return "|||";
        }
    }

    @WebMethod(operationName = "extensionEntidad")
    public String extensionEntidad(
            @WebParam(name = "idEntidad") int idEntidad,
            @WebParam(name = "srs") String srs) {

        //TODO write your implementation code here:
        try {
            String[] wkt = srvPlan().getGeometriaEntidad(idEntidad);
            if (wkt != null && wkt.length > 0) {
                Geometry aGeom = null;
                if (wkt.length > 1) {
                    aGeom = GeoUtils.unionGeoms(wkt);
                } else {
                    aGeom = GeoUtils.GeoFromWKT(wkt[0]);
                }
                aGeom = GeoUtils.TransformGeometry(aGeom, Textos.getTexto("urbrWS", "SRS_Datos"), srs);
                Envelope aEnvelope = aGeom.getEnvelope().getEnvelopeInternal();
                return aEnvelope.getMinX() + "|" + aEnvelope.getMinY() + "|" + aEnvelope.getMaxX() + "|" + aEnvelope.getMaxY();
            } else {
                return null;
            }
        } catch (Exception ex) {
            Logger.getLogger(urbrWS.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @WebMethod(operationName = "geometriaEntidad")
    public String geometriaEntidad(
            @WebParam(name = "idEntidad") int idEntidad,
            @WebParam(name = "maxPuntosGeometriaRespuesta") int MaxPuntosGeometriaRespuesta,
            @WebParam(name = "srs") String srs) {

        //TODO write your implementation code here:
        try {

            String[] wkt = srvPlan().getGeometriaEntidad(idEntidad);
            if (wkt != null && wkt.length > 0) {
                Geometry aGeom = null;
                if (wkt.length > 1) {
                    aGeom = GeoUtils.unionGeoms(wkt);
                } else {
                    aGeom = GeoUtils.GeoFromWKT(wkt[0]);
                }

                aGeom = GeoUtils.TransformGeometry(aGeom, Textos.getTexto("urbrWS", "SRS_Datos"), srs);
                if (MaxPuntosGeometriaRespuesta == 0 || aGeom.getCoordinates().length <= MaxPuntosGeometriaRespuesta) {
                    return aGeom.toText();
                } else {
                    return null;
                }
            } else {
                return null;
            }
        } catch (Exception ex) {
            Logger.getLogger(urbrWS.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @WebMethod(operationName = "extensionPlan")
    public String extensionPlan(
            @WebParam(name = "idPlan") int idPlan,
            @WebParam(name = "srs") String srs) {

        //TODO write your implementation code here:
        try {
            String[] wkt = srvPlan().getPoligonosPlan(idPlan);
            if (wkt != null && wkt.length > 0) {
                Geometry aGeom = null;
                if (wkt.length > 1) {
                    aGeom = GeoUtils.unionGeoms(wkt);
                } else {
                    aGeom = GeoUtils.GeoFromWKT(wkt[0]);
                }
                aGeom = GeoUtils.TransformGeometry(aGeom, Textos.getTexto("urbrWS", "SRS_Datos"), srs);
                Envelope aEnvelope = aGeom.getEnvelope().getEnvelopeInternal();
                return aEnvelope.getMinX() + "|" + aEnvelope.getMinY() + "|" + aEnvelope.getMaxX() + "|" + aEnvelope.getMaxY();
            } else {
                return "";
            }
        } catch (Exception ex) {
            Logger.getLogger(urbrWS.class.getName()).log(Level.SEVERE, null, ex);
            return "";
        }


    }

    @WebMethod(operationName = "geometriaPlan")
    public String geometriaPlan(
            @WebParam(name = "idPlan") int idPlan,
            @WebParam(name = "srs") String srs) {

        //TODO write your implementation code here:
        try {
            String[] wkt = srvPlan().getPoligonosPlan(idPlan);
            if (wkt != null) {
                Geometry aGeo = null;
                if (wkt.length > 1) {
                    aGeo = GeoUtils.unionGeoms(wkt);
                } else {
                    aGeo = GeoUtils.GeoFromWKT(wkt[0]);
                }
                aGeo = GeoUtils.TransformGeometry(aGeo, Textos.getTexto("urbrWS", "SRS_Datos"), srs);
                return aGeo.toText();
            } else {
                return "";
            }
        } catch (Exception ex) {
            Logger.getLogger(urbrWS.class.getName()).log(Level.SEVERE, null, ex);
            return "";
        }


    }
    //-------------------------------------------
    //               METADATOS
    //-------------------------------------------

    @WebMethod(operationName = "metadatosPlan")
    public String metadatosPlan(
            @WebParam(name = "idPlan") int idPlan,
            @WebParam(name = "idioma") String idioma) {

        Element aNode;
        Document aXML;
        //TODO write your implementation code here:
        try {
            if (idioma == null || idioma.isEmpty()) {
                idioma = "es";
            }
            Plan aPlan;
            Plan[] aPlanes;

            aPlan = srvPlan().get(Plan.class, idPlan);

            if (aPlan != null) {
                aXML = XMLws.generarXMLDOC("PLAN");

                aNode = aXML.createElement("NOMBRE");
                aNode.setTextContent(aPlan.getNombre());
                aXML.getDocumentElement().appendChild(aNode);

                aNode = aXML.createElement("HIJOS");
                aPlanes = srvPlan().getPlanesHijos(idPlan, null, ModalidadPlanes.RPM);
                if (aPlanes != null) {
                    aNode.setTextContent(String.valueOf(aPlanes.length));
                } else {
                    aNode.setTextContent("0");
                }
                aXML.getDocumentElement().appendChild(aNode);

                aNode = aXML.createElement("TRAMITES");

                aNode.setTextContent(String.valueOf(srvPlan().getTramitesPorPlan(idPlan, ModalidadPlanes.RPM).length));
                aXML.getDocumentElement().appendChild(aNode);

                return XMLws.XmlDocToString(aXML);
            } else {
                return "";
            }

        } catch (Exception ex) {
            Logger.getLogger(urbrWS.class.getName()).log(Level.SEVERE, null, ex);
            return "";
        }
    }

    @WebMethod(operationName = "metadatosTramite")
    public String metadatosTramite(
            @WebParam(name = "idTramite") int idTramite,
            @WebParam(name = "idioma") String idioma) {

        Element aNode;
        Document aXML;
        //TODO write your implementation code here:
        try {
            if (idioma == null || idioma.isEmpty()) {
                idioma = "es";
            }
            Tramite aTramite;

            aTramite = srvPlan().get(Tramite.class, idTramite);

            if (aTramite != null) {
                aXML = XMLws.generarXMLDOC("TRAMITE");

                aNode = aXML.createElement("PRODUCTOR");
                aNode.setTextContent(srvDic().getTraduccion(Centroproduccion.class, aTramite.getIdcentroproduccion(), idioma));
                aXML.getDocumentElement().appendChild(aNode);

                aNode = aXML.createElement("CREACION");
                aNode.setTextContent(aTramite.getFecha().toString());
                aXML.getDocumentElement().appendChild(aNode);

                aNode = aXML.createElement("CONSOLIDACION");
                aNode.setTextContent(aTramite.getFechaconsolidacion().toString());
                aXML.getDocumentElement().appendChild(aNode);

                aNode = aXML.createElement("TIPO");
                aNode.setTextContent(srvDic().getTraduccion(Tipotramite.class, aTramite.getIdtipotramite(), idioma));
                aXML.getDocumentElement().appendChild(aNode);

                aNode = aXML.createElement("REFUNDIBLE");
                if (srvPlan().isTramiteRefundible(idTramite)) {
                    aNode.setTextContent("true");
                } else {
                    aNode.setTextContent("false");
                }

                aXML.getDocumentElement().appendChild(aNode);

                return XMLws.XmlDocToString(aXML);
            } else {
                return "";
            }

        } catch (Exception ex) {
            Logger.getLogger(urbrWS.class.getName()).log(Level.SEVERE, null, ex);
            return "";
        }
    }
    //-------------------------------------------

    @WebMethod(operationName = "urlDoc")
    public String urlDoc(
            @WebParam(name = "idDoc") int idDoc) {
        try {
            return Textos.getTexto("urbrWS", "URL_Documentos") + "?idDoc=" + idDoc;
        } catch (Exception ex) {
            Logger.getLogger(urbrWS.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @WebMethod(operationName = "determinacionesPadre")
    public String determinacionesPadre(
            @WebParam(name = "idTramite") int idTramite) {

        Determinacion[] aDeterminaciones;
        Element aNode;
        Document aXML;

        try {
            aXML = XMLws.generarXMLDOC("DETERMINACIONES");
            aDeterminaciones = srvPlan().getDeterminacionesRaiz(idTramite);

            for (Determinacion aDeterminacion : aDeterminaciones) {
                aNode = aXML.createElement("DETERMINACION");
                aXML.getDocumentElement().appendChild(aNode);
                aNode.setAttribute("id", String.valueOf(aDeterminacion.getIden()));
                aNode.setAttribute("apartado", aDeterminacion.getApartado());
                aNode.setAttribute("nombre", aDeterminacion.getNombre());
            }

            return XMLws.XmlDocToString(aXML);
        } catch (Exception ex) {
            Logger.getLogger(urbrWS.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @WebMethod(operationName = "determinacionesHijas")
    public String determinacionesHijas(
            @WebParam(name = "idPadre") int idPadre) {

        Determinacion[] aDeterminaciones;
        Element aNode;
        Document aXML;

        try {
            aXML = XMLws.generarXMLDOC("DETERMINACIONES");
            aDeterminaciones = srvPlan().getDeterminacionesHijas(idPadre);

            for (Determinacion aDeterminacion : aDeterminaciones) {
                aNode = aXML.createElement("DETERMINACION");
                aXML.getDocumentElement().appendChild(aNode);
                aNode.setAttribute("id", String.valueOf(aDeterminacion.getIden()));
                aNode.setAttribute("apartado", aDeterminacion.getApartado());
                aNode.setAttribute("nombre", aDeterminacion.getNombre());
            }

            return XMLws.XmlDocToString(aXML);
        } catch (Exception ex) {
            Logger.getLogger(urbrWS.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @WebMethod(operationName = "planesPadre")
    public String planesPadre(
            @WebParam(name = "idAmbito") int idAmbito) {

        Plan[] aPlanes;
        Element aNode;
        Document aXML;

        try {
            aXML = XMLws.generarXMLDOC("PLANES");
            aPlanes = srvPlan().getPlanesRaiz(idAmbito, ModalidadPlanes.RPM);

            for (Plan aPlan : aPlanes) {
                if (!srvPub().isPlanRefundido(aPlan.getIden())) {
                    aNode = aXML.createElement("PLAN");
                    aXML.getDocumentElement().appendChild(aNode);
                    aNode.setAttribute("id", String.valueOf(aPlan.getIden()));
                    aNode.setAttribute("nombre", aPlan.getNombre());
                }
            }

            return XMLws.XmlDocToString(aXML);
        } catch (Exception ex) {
            Logger.getLogger(urbrWS.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }


    }

    @WebMethod(operationName = "planesHijo")
    public String planesHijo(
            @WebParam(name = "idPadre") int idPadre) {

        //TODO write your implementation code here:
        try {
            Plan[] aPlanes;
            Element aNode;
            Document aXML;


            aPlanes = srvPlan().getPlanesHijos(idPadre, null, ModalidadPlanes.RPM);
            aXML = XMLws.generarXMLDOC("PLANES");
            for (Plan aPlan : aPlanes) {
                aPlan = srvPlan().get(Plan.class, aPlan.getIden());
                aNode = aXML.createElement("PLAN");
                aNode.setAttribute("id", String.valueOf(aPlan.getIden()));
                aNode.setAttribute("nombre", aPlan.getNombre());
                aXML.getDocumentElement().appendChild(aNode);
            }

            return XMLws.XmlDocToString(aXML);
        } catch (Exception ex) {
            Logger.getLogger(urbrWS.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }


    }

    @WebMethod(operationName = "entidadesFromClave")
    public String entidadesFromClave(
            @WebParam(name = "Clave") String Clave,
            @WebParam(name = "idAmbito") int idAmbito) {

        Entidad[] aEntidades;
        Element aNode;
        Document aXML;

        try {
            aXML = XMLws.generarXMLDOC("ENTIDADES");
            aEntidades = srvPub().findEntidadesLikeClave(Clave, idAmbito);

            for (Entidad aEntidad : aEntidades) {
                String[] wkt = srvPlan().getGeometriaEntidad(aEntidad.getIden());
                if (wkt != null && wkt.length > 0) {
                    Geometry aGeom = null;
                    if (wkt.length > 1) {
                        aGeom = GeoUtils.unionGeoms(wkt);
                    } else {
                        aGeom = GeoUtils.GeoFromWKT(wkt[0]);
                    }
                    aNode = aXML.createElement("ENTIDAD");
                    aXML.getDocumentElement().appendChild(aNode);
                    aNode.setAttribute("id", String.valueOf(aEntidad.getIden()));
                    aNode.setAttribute("clave", aEntidad.getClave());
                    aNode.setAttribute("nombre", aEntidad.getNombre());
                    aNode.setAttribute("geo_type", aGeom.getGeometryType().toUpperCase());
                }
            }

            return XMLws.XmlDocToString(aXML);
        } catch (Exception ex) {
            Logger.getLogger(urbrWS.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }


    }

    /**********************************************************************************************************************/
    private double getSizeDocument(Documento doc,String codigoTramite) {
        try {
            String archivo = doc.getArchivo();
            if (archivo != null) {
                if (!archivo.equals("")) {
                    //String aRaiz = Textos.getTexto("urbrWS", "Path_Docs");
                    //File file = new File(aRaiz + java.io.File.separator + codigoTramite + java.io.File.separator +  archivo);
                    File file = srvPlan().getArchivo(codigoTramite, doc.getArchivo());
                    if (file.isDirectory()) {
                        return FileUtils.sizeOfDirectory(file);
                    } else {
                        return file.length();
                    }
                } else {
                    return 0;
                }
            } else {
                return 0;
            }
        } catch (Exception ex) {
            Logger.getLogger(urbrWS.class.getName()).log(Level.WARNING, "Error accediendo a ficheros", ex);
            return 0;
        }
    }

    private DocumentFragment getPlanos(Geometry aGeo, Document aXML, String Idioma) {
        Documentoshp[] aDocumentosShp;
        NodeList aLista = null;
        Element aNode;
        Element aNodeGrupo;
        Element aNodePlan;
        Element aNodeTramite;
        Element aNodeDocumentos;
        DocumentFragment aXMLFrag;
        Documento aDocumento;

        try {
            int IdAmb = idAmbito(aGeo.getCentroid().getX(), aGeo.getCentroid().getY(), Textos.getTexto("urbrWS", "SRS_Datos"));
            aDocumentosShp = srvPub().findPlanosRefundidoFromGeo(GeoUtils.GeoToWKT(aGeo), IdAmb);
            aNodeDocumentos = aXML.createElement("PLANOS");
            aNodeDocumentos.setAttribute("count", String.valueOf(aDocumentosShp.length));
            for (Documentoshp aDocShp : aDocumentosShp) {
                aDocumento = srvPlan().get(Documento.class, aDocShp.getDocumento().getIden());
                Tramite tramite = srvPlan().get(Tramite.class, aDocumento.getTramite().getIden());
                Plan plan = srvPlan().get(Plan.class, tramite.getPlan().getIden());
                aLista = XMLws.findNode("PLAN[@id=\"" + plan.getIden() + "\"]", aNodeDocumentos);
                if (aLista == null || aLista.getLength() == 0) {
                    aNodePlan = aXML.createElement("PLAN");
                    aNodePlan.setAttribute("id", String.valueOf(plan.getIden()));
                    aNodePlan.setAttribute("nombre", plan.getNombre());
                    aNodeDocumentos.appendChild(aNodePlan);
                } else {
                    aNodePlan = (Element) aLista.item(0);
                }

                aLista = XMLws.findNode("TRAMITE[@id=\"" + tramite.getIden() + "\"]", aNodePlan);
                if (aLista == null || aLista.getLength() == 0) {
                    aNodeTramite = aXML.createElement("TRAMITE");
                    aNodeTramite.setAttribute("id", String.valueOf(tramite.getIden()));
                    aNodeTramite.setAttribute("nombre", srvDic().getTraduccion(Tipotramite.class, tramite.getIdtipotramite(), Idioma));
                    aNodePlan.appendChild(aNodeTramite);
                } else {
                    aNodeTramite = (Element) aLista.item(0);
                }

                aLista = XMLws.findNode("GRUPO[@id=\"" + aDocumento.getIdgrupodocumento() + "\"]", aNodeTramite);
                if (aLista == null || aLista.getLength() == 0) {
                    aNodeGrupo = aXML.createElement("GRUPO");
                    aNodeGrupo.setAttribute("id", String.valueOf(aDocumento.getIdgrupodocumento()));
                    aNodeGrupo.setAttribute("nombre", srvDic().getTraduccion(Grupodocumento.class, aDocumento.getIdgrupodocumento(), Idioma));
                    aNodeTramite.appendChild(aNodeGrupo);
                } else {
                    aNodeGrupo = (Element) aLista.item(0);
                }
                aNode = aXML.createElement("PLANO");
                aNodeGrupo.appendChild(aNode);
                aNode.setAttribute("nombre", aDocumento.getNombre());
                aNode.setAttribute("idDoc", String.valueOf(aDocShp.getIden()));
                aNode.setAttribute("hoja", aDocShp.getHoja());


            }
            aXMLFrag = aXML.createDocumentFragment();
            aXMLFrag.appendChild(aNodeDocumentos);
            return aXMLFrag;
        } catch (Exception ex) {
            Logger.getLogger(urbrWS.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "getVersion")
    public String getVersion() {
        //TODO write your implementation code here:
        return "2.0.0";
    }
}

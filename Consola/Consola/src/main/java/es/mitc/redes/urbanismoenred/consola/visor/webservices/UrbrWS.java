
package es.mitc.redes.urbanismoenred.consola.visor.webservices;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.1.3-b02-
 * Generated source version: 2.1
 * 
 */
@WebService(name = "urbrWS", targetNamespace = "http://serviciosweb.urbanismoenred.redes.mitc.es/")
@XmlSeeAlso({
    ObjectFactory.class
})
public interface UrbrWS {


    /**
     * 
     * @param idTramite
     * @return
     *     returns java.lang.String
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "determinacionesPadre", targetNamespace = "http://serviciosweb.urbanismoenred.redes.mitc.es/", className = "es.mitc.redes.urbanismoenred.consola.visor.webservices.DeterminacionesPadre")
    @ResponseWrapper(localName = "determinacionesPadreResponse", targetNamespace = "http://serviciosweb.urbanismoenred.redes.mitc.es/", className = "es.mitc.redes.urbanismoenred.consola.visor.webservices.DeterminacionesPadreResponse")
    public String determinacionesPadre(
        @WebParam(name = "idTramite", targetNamespace = "")
        int idTramite);

    /**
     * 
     * @param idTramite
     * @param idioma
     * @return
     *     returns java.lang.String
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "metadatosTramite", targetNamespace = "http://serviciosweb.urbanismoenred.redes.mitc.es/", className = "es.mitc.redes.urbanismoenred.consola.visor.webservices.MetadatosTramite")
    @ResponseWrapper(localName = "metadatosTramiteResponse", targetNamespace = "http://serviciosweb.urbanismoenred.redes.mitc.es/", className = "es.mitc.redes.urbanismoenred.consola.visor.webservices.MetadatosTramiteResponse")
    public String metadatosTramite(
        @WebParam(name = "idTramite", targetNamespace = "")
        int idTramite,
        @WebParam(name = "idioma", targetNamespace = "")
        String idioma);

    /**
     * 
     * @param municipio
     * @param provincia
     * @return
     *     returns java.lang.String
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "municipiosCallejero", targetNamespace = "http://serviciosweb.urbanismoenred.redes.mitc.es/", className = "es.mitc.redes.urbanismoenred.consola.visor.webservices.MunicipiosCallejero")
    @ResponseWrapper(localName = "municipiosCallejeroResponse", targetNamespace = "http://serviciosweb.urbanismoenred.redes.mitc.es/", className = "es.mitc.redes.urbanismoenred.consola.visor.webservices.MunicipiosCallejeroResponse")
    public String municipiosCallejero(
        @WebParam(name = "provincia", targetNamespace = "")
        String provincia,
        @WebParam(name = "municipio", targetNamespace = "")
        String municipio);

    /**
     * 
     * @return
     *     returns java.lang.String
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "tiposCalle", targetNamespace = "http://serviciosweb.urbanismoenred.redes.mitc.es/", className = "es.mitc.redes.urbanismoenred.consola.visor.webservices.TiposCalle")
    @ResponseWrapper(localName = "tiposCalleResponse", targetNamespace = "http://serviciosweb.urbanismoenred.redes.mitc.es/", className = "es.mitc.redes.urbanismoenred.consola.visor.webservices.TiposCalleResponse")
    public String tiposCalle();

    /**
     * 
     * @param idPadre
     * @param idioma
     * @return
     *     returns java.lang.String
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "ambitosHijo", targetNamespace = "http://serviciosweb.urbanismoenred.redes.mitc.es/", className = "es.mitc.redes.urbanismoenred.consola.visor.webservices.AmbitosHijo")
    @ResponseWrapper(localName = "ambitosHijoResponse", targetNamespace = "http://serviciosweb.urbanismoenred.redes.mitc.es/", className = "es.mitc.redes.urbanismoenred.consola.visor.webservices.AmbitosHijoResponse")
    public String ambitosHijo(
        @WebParam(name = "idioma", targetNamespace = "")
        String idioma,
        @WebParam(name = "idPadre", targetNamespace = "")
        int idPadre);

    /**
     * 
     * @param abrevTipo
     * @param municipio
     * @param nombreVia
     * @param provincia
     * @return
     *     returns java.lang.String
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getVias", targetNamespace = "http://serviciosweb.urbanismoenred.redes.mitc.es/", className = "es.mitc.redes.urbanismoenred.consola.visor.webservices.GetVias")
    @ResponseWrapper(localName = "getViasResponse", targetNamespace = "http://serviciosweb.urbanismoenred.redes.mitc.es/", className = "es.mitc.redes.urbanismoenred.consola.visor.webservices.GetViasResponse")
    public String getVias(
        @WebParam(name = "provincia", targetNamespace = "")
        String provincia,
        @WebParam(name = "municipio", targetNamespace = "")
        String municipio,
        @WebParam(name = "abrevTipo", targetNamespace = "")
        String abrevTipo,
        @WebParam(name = "nombreVia", targetNamespace = "")
        String nombreVia);

    /**
     * 
     * @param idioma
     * @return
     *     returns java.lang.String
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "ambitosWithPlan", targetNamespace = "http://serviciosweb.urbanismoenred.redes.mitc.es/", className = "es.mitc.redes.urbanismoenred.consola.visor.webservices.AmbitosWithPlan")
    @ResponseWrapper(localName = "ambitosWithPlanResponse", targetNamespace = "http://serviciosweb.urbanismoenred.redes.mitc.es/", className = "es.mitc.redes.urbanismoenred.consola.visor.webservices.AmbitosWithPlanResponse")
    public String ambitosWithPlan(
        @WebParam(name = "idioma", targetNamespace = "")
        String idioma);

    /**
     * 
     * @param abrevTipo
     * @param municipio
     * @param nombreVia
     * @param srs
     * @param provincia
     * @param numero
     * @return
     *     returns java.lang.String
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getCoordenadas", targetNamespace = "http://serviciosweb.urbanismoenred.redes.mitc.es/", className = "es.mitc.redes.urbanismoenred.consola.visor.webservices.GetCoordenadas")
    @ResponseWrapper(localName = "getCoordenadasResponse", targetNamespace = "http://serviciosweb.urbanismoenred.redes.mitc.es/", className = "es.mitc.redes.urbanismoenred.consola.visor.webservices.GetCoordenadasResponse")
    public String getCoordenadas(
        @WebParam(name = "provincia", targetNamespace = "")
        String provincia,
        @WebParam(name = "municipio", targetNamespace = "")
        String municipio,
        @WebParam(name = "abrevTipo", targetNamespace = "")
        String abrevTipo,
        @WebParam(name = "nombreVia", targetNamespace = "")
        String nombreVia,
        @WebParam(name = "numero", targetNamespace = "")
        String numero,
        @WebParam(name = "srs", targetNamespace = "")
        String srs);

    /**
     * 
     * @return
     *     returns java.lang.String
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "provinciasCallejero", targetNamespace = "http://serviciosweb.urbanismoenred.redes.mitc.es/", className = "es.mitc.redes.urbanismoenred.consola.visor.webservices.ProvinciasCallejero")
    @ResponseWrapper(localName = "provinciasCallejeroResponse", targetNamespace = "http://serviciosweb.urbanismoenred.redes.mitc.es/", className = "es.mitc.redes.urbanismoenred.consola.visor.webservices.ProvinciasCallejeroResponse")
    public String provinciasCallejero();

    /**
     * 
     * @param srs
     * @param idAmbito
     * @return
     *     returns java.lang.String
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "extensionAmbito", targetNamespace = "http://serviciosweb.urbanismoenred.redes.mitc.es/", className = "es.mitc.redes.urbanismoenred.consola.visor.webservices.ExtensionAmbito")
    @ResponseWrapper(localName = "extensionAmbitoResponse", targetNamespace = "http://serviciosweb.urbanismoenred.redes.mitc.es/", className = "es.mitc.redes.urbanismoenred.consola.visor.webservices.ExtensionAmbitoResponse")
    public String extensionAmbito(
        @WebParam(name = "idAmbito", targetNamespace = "")
        int idAmbito,
        @WebParam(name = "srs", targetNamespace = "")
        String srs);

    /**
     * 
     * @param wktGeometry
     * @param idioma
     * @param srs
     * @return
     *     returns java.lang.String
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "consultaGraficaExtendida", targetNamespace = "http://serviciosweb.urbanismoenred.redes.mitc.es/", className = "es.mitc.redes.urbanismoenred.consola.visor.webservices.ConsultaGraficaExtendida")
    @ResponseWrapper(localName = "consultaGraficaExtendidaResponse", targetNamespace = "http://serviciosweb.urbanismoenred.redes.mitc.es/", className = "es.mitc.redes.urbanismoenred.consola.visor.webservices.ConsultaGraficaExtendidaResponse")
    public String consultaGraficaExtendida(
        @WebParam(name = "wktGeometry", targetNamespace = "")
        String wktGeometry,
        @WebParam(name = "srs", targetNamespace = "")
        String srs,
        @WebParam(name = "idioma", targetNamespace = "")
        String idioma);

    /**
     * 
     * @param nombre
     * @param idAmbito
     * @return
     *     returns java.lang.String
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "planesFromNombre", targetNamespace = "http://serviciosweb.urbanismoenred.redes.mitc.es/", className = "es.mitc.redes.urbanismoenred.consola.visor.webservices.PlanesFromNombre")
    @ResponseWrapper(localName = "planesFromNombreResponse", targetNamespace = "http://serviciosweb.urbanismoenred.redes.mitc.es/", className = "es.mitc.redes.urbanismoenred.consola.visor.webservices.PlanesFromNombreResponse")
    public String planesFromNombre(
        @WebParam(name = "nombre", targetNamespace = "")
        String nombre,
        @WebParam(name = "idAmbito", targetNamespace = "")
        int idAmbito);

    /**
     * 
     * @param idAmbito
     * @return
     *     returns java.lang.String
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "fechaRefundido", targetNamespace = "http://serviciosweb.urbanismoenred.redes.mitc.es/", className = "es.mitc.redes.urbanismoenred.consola.visor.webservices.FechaRefundido")
    @ResponseWrapper(localName = "fechaRefundidoResponse", targetNamespace = "http://serviciosweb.urbanismoenred.redes.mitc.es/", className = "es.mitc.redes.urbanismoenred.consola.visor.webservices.FechaRefundidoResponse")
    public String fechaRefundido(
        @WebParam(name = "idAmbito", targetNamespace = "")
        int idAmbito);

    /**
     * 
     * @param abrevTipo
     * @param municipio
     * @param nombreVia
     * @param provincia
     * @param numero
     * @return
     *     returns java.lang.String
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "referenciaCatastral", targetNamespace = "http://serviciosweb.urbanismoenred.redes.mitc.es/", className = "es.mitc.redes.urbanismoenred.consola.visor.webservices.ReferenciaCatastral")
    @ResponseWrapper(localName = "referenciaCatastralResponse", targetNamespace = "http://serviciosweb.urbanismoenred.redes.mitc.es/", className = "es.mitc.redes.urbanismoenred.consola.visor.webservices.ReferenciaCatastralResponse")
    public String referenciaCatastral(
        @WebParam(name = "provincia", targetNamespace = "")
        String provincia,
        @WebParam(name = "municipio", targetNamespace = "")
        String municipio,
        @WebParam(name = "abrevTipo", targetNamespace = "")
        String abrevTipo,
        @WebParam(name = "nombreVia", targetNamespace = "")
        String nombreVia,
        @WebParam(name = "numero", targetNamespace = "")
        String numero);

    /**
     * 
     * @param srs
     * @param idPlan
     * @return
     *     returns java.lang.String
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "geometriaPlan", targetNamespace = "http://serviciosweb.urbanismoenred.redes.mitc.es/", className = "es.mitc.redes.urbanismoenred.consola.visor.webservices.GeometriaPlan")
    @ResponseWrapper(localName = "geometriaPlanResponse", targetNamespace = "http://serviciosweb.urbanismoenred.redes.mitc.es/", className = "es.mitc.redes.urbanismoenred.consola.visor.webservices.GeometriaPlanResponse")
    public String geometriaPlan(
        @WebParam(name = "idPlan", targetNamespace = "")
        int idPlan,
        @WebParam(name = "srs", targetNamespace = "")
        String srs);

    /**
     * 
     * @param srs
     * @param y
     * @param x
     * @return
     *     returns int
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "idAmbito", targetNamespace = "http://serviciosweb.urbanismoenred.redes.mitc.es/", className = "es.mitc.redes.urbanismoenred.consola.visor.webservices.IdAmbito")
    @ResponseWrapper(localName = "idAmbitoResponse", targetNamespace = "http://serviciosweb.urbanismoenred.redes.mitc.es/", className = "es.mitc.redes.urbanismoenred.consola.visor.webservices.IdAmbitoResponse")
    public int idAmbito(
        @WebParam(name = "X", targetNamespace = "")
        Double x,
        @WebParam(name = "Y", targetNamespace = "")
        Double y,
        @WebParam(name = "srs", targetNamespace = "")
        String srs);

    /**
     * 
     * @param nombre
     * @param idAmbito
     * @param nombrePlan
     * @return
     *     returns java.lang.String
     */
    @WebMethod(operationName = "entidadesFromNombre_Plan")
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "entidadesFromNombre_Plan", targetNamespace = "http://serviciosweb.urbanismoenred.redes.mitc.es/", className = "es.mitc.redes.urbanismoenred.consola.visor.webservices.EntidadesFromNombrePlan")
    @ResponseWrapper(localName = "entidadesFromNombre_PlanResponse", targetNamespace = "http://serviciosweb.urbanismoenred.redes.mitc.es/", className = "es.mitc.redes.urbanismoenred.consola.visor.webservices.EntidadesFromNombrePlanResponse")
    public String entidadesFromNombrePlan(
        @WebParam(name = "nombre", targetNamespace = "")
        String nombre,
        @WebParam(name = "nombrePlan", targetNamespace = "")
        String nombrePlan,
        @WebParam(name = "idAmbito", targetNamespace = "")
        int idAmbito);

    /**
     * 
     * @param idioma
     * @return
     *     returns java.lang.String
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "ambitosPadre", targetNamespace = "http://serviciosweb.urbanismoenred.redes.mitc.es/", className = "es.mitc.redes.urbanismoenred.consola.visor.webservices.AmbitosPadre")
    @ResponseWrapper(localName = "ambitosPadreResponse", targetNamespace = "http://serviciosweb.urbanismoenred.redes.mitc.es/", className = "es.mitc.redes.urbanismoenred.consola.visor.webservices.AmbitosPadreResponse")
    public String ambitosPadre(
        @WebParam(name = "idioma", targetNamespace = "")
        String idioma);

    /**
     * 
     * @param maxPuntosGeometriaRespuesta
     * @param srs
     * @param idEntidad
     * @return
     *     returns java.lang.String
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "geometriaEntidad", targetNamespace = "http://serviciosweb.urbanismoenred.redes.mitc.es/", className = "es.mitc.redes.urbanismoenred.consola.visor.webservices.GeometriaEntidad")
    @ResponseWrapper(localName = "geometriaEntidadResponse", targetNamespace = "http://serviciosweb.urbanismoenred.redes.mitc.es/", className = "es.mitc.redes.urbanismoenred.consola.visor.webservices.GeometriaEntidadResponse")
    public String geometriaEntidad(
        @WebParam(name = "idEntidad", targetNamespace = "")
        int idEntidad,
        @WebParam(name = "maxPuntosGeometriaRespuesta", targetNamespace = "")
        int maxPuntosGeometriaRespuesta,
        @WebParam(name = "srs", targetNamespace = "")
        String srs);

    /**
     * 
     * @param idAmbito
     * @return
     *     returns java.lang.String
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "planesPadre", targetNamespace = "http://serviciosweb.urbanismoenred.redes.mitc.es/", className = "es.mitc.redes.urbanismoenred.consola.visor.webservices.PlanesPadre")
    @ResponseWrapper(localName = "planesPadreResponse", targetNamespace = "http://serviciosweb.urbanismoenred.redes.mitc.es/", className = "es.mitc.redes.urbanismoenred.consola.visor.webservices.PlanesPadreResponse")
    public String planesPadre(
        @WebParam(name = "idAmbito", targetNamespace = "")
        int idAmbito);

    /**
     * 
     * @param srs
     * @param idEntidad
     * @return
     *     returns java.lang.String
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "extensionEntidad", targetNamespace = "http://serviciosweb.urbanismoenred.redes.mitc.es/", className = "es.mitc.redes.urbanismoenred.consola.visor.webservices.ExtensionEntidad")
    @ResponseWrapper(localName = "extensionEntidadResponse", targetNamespace = "http://serviciosweb.urbanismoenred.redes.mitc.es/", className = "es.mitc.redes.urbanismoenred.consola.visor.webservices.ExtensionEntidadResponse")
    public String extensionEntidad(
        @WebParam(name = "idEntidad", targetNamespace = "")
        int idEntidad,
        @WebParam(name = "srs", targetNamespace = "")
        String srs);

    /**
     * 
     * @param wktGeometry
     * @param idioma
     * @param srs
     * @return
     *     returns java.lang.String
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "consultaGrafica", targetNamespace = "http://serviciosweb.urbanismoenred.redes.mitc.es/", className = "es.mitc.redes.urbanismoenred.consola.visor.webservices.ConsultaGrafica")
    @ResponseWrapper(localName = "consultaGraficaResponse", targetNamespace = "http://serviciosweb.urbanismoenred.redes.mitc.es/", className = "es.mitc.redes.urbanismoenred.consola.visor.webservices.ConsultaGraficaResponse")
    public String consultaGrafica(
        @WebParam(name = "wktGeometry", targetNamespace = "")
        String wktGeometry,
        @WebParam(name = "srs", targetNamespace = "")
        String srs,
        @WebParam(name = "idioma", targetNamespace = "")
        String idioma);

    /**
     * 
     * @param idAmbito
     * @param clave
     * @return
     *     returns java.lang.String
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "entidadesFromClave", targetNamespace = "http://serviciosweb.urbanismoenred.redes.mitc.es/", className = "es.mitc.redes.urbanismoenred.consola.visor.webservices.EntidadesFromClave")
    @ResponseWrapper(localName = "entidadesFromClaveResponse", targetNamespace = "http://serviciosweb.urbanismoenred.redes.mitc.es/", className = "es.mitc.redes.urbanismoenred.consola.visor.webservices.EntidadesFromClaveResponse")
    public String entidadesFromClave(
        @WebParam(name = "Clave", targetNamespace = "")
        String clave,
        @WebParam(name = "idAmbito", targetNamespace = "")
        int idAmbito);

    /**
     * 
     * @param idPadre
     * @return
     *     returns java.lang.String
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "planesHijo", targetNamespace = "http://serviciosweb.urbanismoenred.redes.mitc.es/", className = "es.mitc.redes.urbanismoenred.consola.visor.webservices.PlanesHijo")
    @ResponseWrapper(localName = "planesHijoResponse", targetNamespace = "http://serviciosweb.urbanismoenred.redes.mitc.es/", className = "es.mitc.redes.urbanismoenred.consola.visor.webservices.PlanesHijoResponse")
    public String planesHijo(
        @WebParam(name = "idPadre", targetNamespace = "")
        int idPadre);

    /**
     * 
     * @param srs
     * @param idPlan
     * @return
     *     returns java.lang.String
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "extensionPlan", targetNamespace = "http://serviciosweb.urbanismoenred.redes.mitc.es/", className = "es.mitc.redes.urbanismoenred.consola.visor.webservices.ExtensionPlan")
    @ResponseWrapper(localName = "extensionPlanResponse", targetNamespace = "http://serviciosweb.urbanismoenred.redes.mitc.es/", className = "es.mitc.redes.urbanismoenred.consola.visor.webservices.ExtensionPlanResponse")
    public String extensionPlan(
        @WebParam(name = "idPlan", targetNamespace = "")
        int idPlan,
        @WebParam(name = "srs", targetNamespace = "")
        String srs);

    /**
     * 
     * @param idHoja
     * @return
     *     returns java.lang.String
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "creaWMSDoc", targetNamespace = "http://serviciosweb.urbanismoenred.redes.mitc.es/", className = "es.mitc.redes.urbanismoenred.consola.visor.webservices.CreaWMSDoc")
    @ResponseWrapper(localName = "creaWMSDocResponse", targetNamespace = "http://serviciosweb.urbanismoenred.redes.mitc.es/", className = "es.mitc.redes.urbanismoenred.consola.visor.webservices.CreaWMSDocResponse")
    public String creaWMSDoc(
        @WebParam(name = "idHoja", targetNamespace = "")
        int idHoja);

    /**
     * 
     * @param idHoja
     * @return
     *     returns java.lang.String
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getLeyenda", targetNamespace = "http://serviciosweb.urbanismoenred.redes.mitc.es/", className = "es.mitc.redes.urbanismoenred.consola.visor.webservices.GetLeyenda")
    @ResponseWrapper(localName = "getLeyendaResponse", targetNamespace = "http://serviciosweb.urbanismoenred.redes.mitc.es/", className = "es.mitc.redes.urbanismoenred.consola.visor.webservices.GetLeyendaResponse")
    public String getLeyenda(
        @WebParam(name = "idHoja", targetNamespace = "")
        int idHoja);

    /**
     * 
     * @param idPadre
     * @return
     *     returns java.lang.String
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "determinacionesHijas", targetNamespace = "http://serviciosweb.urbanismoenred.redes.mitc.es/", className = "es.mitc.redes.urbanismoenred.consola.visor.webservices.DeterminacionesHijas")
    @ResponseWrapper(localName = "determinacionesHijasResponse", targetNamespace = "http://serviciosweb.urbanismoenred.redes.mitc.es/", className = "es.mitc.redes.urbanismoenred.consola.visor.webservices.DeterminacionesHijasResponse")
    public String determinacionesHijas(
        @WebParam(name = "idPadre", targetNamespace = "")
        int idPadre);

    /**
     * 
     * @return
     *     returns java.lang.String
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getVersion", targetNamespace = "http://serviciosweb.urbanismoenred.redes.mitc.es/", className = "es.mitc.redes.urbanismoenred.consola.visor.webservices.GetVersion")
    @ResponseWrapper(localName = "getVersionResponse", targetNamespace = "http://serviciosweb.urbanismoenred.redes.mitc.es/", className = "es.mitc.redes.urbanismoenred.consola.visor.webservices.GetVersionResponse")
    public String getVersion();

    /**
     * 
     * @param nombre
     * @param idAmbito
     * @return
     *     returns java.lang.String
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "entidadesFromNombre", targetNamespace = "http://serviciosweb.urbanismoenred.redes.mitc.es/", className = "es.mitc.redes.urbanismoenred.consola.visor.webservices.EntidadesFromNombre")
    @ResponseWrapper(localName = "entidadesFromNombreResponse", targetNamespace = "http://serviciosweb.urbanismoenred.redes.mitc.es/", className = "es.mitc.redes.urbanismoenred.consola.visor.webservices.EntidadesFromNombreResponse")
    public String entidadesFromNombre(
        @WebParam(name = "nombre", targetNamespace = "")
        String nombre,
        @WebParam(name = "idAmbito", targetNamespace = "")
        int idAmbito);

    /**
     * 
     * @param idioma
     * @param idPlan
     * @return
     *     returns java.lang.String
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "metadatosPlan", targetNamespace = "http://serviciosweb.urbanismoenred.redes.mitc.es/", className = "es.mitc.redes.urbanismoenred.consola.visor.webservices.MetadatosPlan")
    @ResponseWrapper(localName = "metadatosPlanResponse", targetNamespace = "http://serviciosweb.urbanismoenred.redes.mitc.es/", className = "es.mitc.redes.urbanismoenred.consola.visor.webservices.MetadatosPlanResponse")
    public String metadatosPlan(
        @WebParam(name = "idPlan", targetNamespace = "")
        int idPlan,
        @WebParam(name = "idioma", targetNamespace = "")
        String idioma);

    /**
     * 
     * @param refCatastral
     * @param srs
     * @return
     *     returns java.lang.String
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "coordenadasRefCatastral", targetNamespace = "http://serviciosweb.urbanismoenred.redes.mitc.es/", className = "es.mitc.redes.urbanismoenred.consola.visor.webservices.CoordenadasRefCatastral")
    @ResponseWrapper(localName = "coordenadasRefCatastralResponse", targetNamespace = "http://serviciosweb.urbanismoenred.redes.mitc.es/", className = "es.mitc.redes.urbanismoenred.consola.visor.webservices.CoordenadasRefCatastralResponse")
    public String coordenadasRefCatastral(
        @WebParam(name = "refCatastral", targetNamespace = "")
        String refCatastral,
        @WebParam(name = "srs", targetNamespace = "")
        String srs);

    /**
     * 
     * @param idDoc
     * @return
     *     returns java.lang.String
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "urlDoc", targetNamespace = "http://serviciosweb.urbanismoenred.redes.mitc.es/", className = "es.mitc.redes.urbanismoenred.consola.visor.webservices.UrlDoc")
    @ResponseWrapper(localName = "urlDocResponse", targetNamespace = "http://serviciosweb.urbanismoenred.redes.mitc.es/", className = "es.mitc.redes.urbanismoenred.consola.visor.webservices.UrlDocResponse")
    public String urlDoc(
        @WebParam(name = "idDoc", targetNamespace = "")
        int idDoc);

    /**
     * 
     * @param idTramite
     * @param idioma
     * @return
     *     returns java.lang.String
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "estadoDocumentosTramite", targetNamespace = "http://serviciosweb.urbanismoenred.redes.mitc.es/", className = "es.mitc.redes.urbanismoenred.consola.visor.webservices.EstadoDocumentosTramite")
    @ResponseWrapper(localName = "estadoDocumentosTramiteResponse", targetNamespace = "http://serviciosweb.urbanismoenred.redes.mitc.es/", className = "es.mitc.redes.urbanismoenred.consola.visor.webservices.EstadoDocumentosTramiteResponse")
    public String estadoDocumentosTramite(
        @WebParam(name = "idTramite", targetNamespace = "")
        int idTramite,
        @WebParam(name = "idioma", targetNamespace = "")
        String idioma);

    /**
     * 
     * @param idioma
     * @param idAmbito
     * @return
     *     returns java.lang.String
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "estadoRegistro", targetNamespace = "http://serviciosweb.urbanismoenred.redes.mitc.es/", className = "es.mitc.redes.urbanismoenred.consola.visor.webservices.EstadoRegistro")
    @ResponseWrapper(localName = "estadoRegistroResponse", targetNamespace = "http://serviciosweb.urbanismoenred.redes.mitc.es/", className = "es.mitc.redes.urbanismoenred.consola.visor.webservices.EstadoRegistroResponse")
    public String estadoRegistro(
        @WebParam(name = "idAmbito", targetNamespace = "")
        int idAmbito,
        @WebParam(name = "idioma", targetNamespace = "")
        String idioma);

    /**
     * 
     * @return
     *     returns java.lang.String
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "gisPrevio", targetNamespace = "http://serviciosweb.urbanismoenred.redes.mitc.es/", className = "es.mitc.redes.urbanismoenred.consola.visor.webservices.GisPrevio")
    @ResponseWrapper(localName = "gisPrevioResponse", targetNamespace = "http://serviciosweb.urbanismoenred.redes.mitc.es/", className = "es.mitc.redes.urbanismoenred.consola.visor.webservices.GisPrevioResponse")
    public String gisPrevio();

}

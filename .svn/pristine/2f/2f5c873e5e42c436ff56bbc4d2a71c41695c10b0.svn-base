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
package es.mitc.redes.urbanismoenred.serviciosweb.utils;

import com.vividsolutions.jts.geom.Geometry;
import es.mitc.redes.urbanismoenred.data.rpm.diccionario.Ambito;
import es.mitc.redes.urbanismoenred.data.rpm.explotacion.Capa;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Casoentidaddeterminacion;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Determinacion;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidad;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidaddeterminacionregimen;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Opciondeterminacion;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Regimenespecifico;
import es.mitc.redes.urbanismoenred.servicios.planeamiento.PropiedadesAdscripcion;
import es.mitc.redes.urbanismoenred.servicios.planeamiento.ServicioDiccionariosLocal;
import es.mitc.redes.urbanismoenred.servicios.planeamiento.ServicioPlaneamientoLocal;
import es.mitc.redes.urbanismoenred.serviciosweb.Textos;
import es.mitc.redes.urbanismoenred.serviciosweb.XMLws;
import es.mitc.redes.urbanismoenred.serviciosweb.urbrWS;
import es.mitc.redes.urbanismoenred.utils.serviciosweb.ServicioPublicacionLocal;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 *
 * @author Alvaro.Martin
 */
public class GestionEntidades {

    urbrWS servicio = new urbrWS();

    public Document consultaEntidades(
            ServicioPlaneamientoLocal srvPlan,ServicioPublicacionLocal srvPub,ServicioDiccionariosLocal srvDic,
            String wktGeometry,
            String srs,
            String Idioma) {

        try {
            Document aXML;
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

            aXML = XMLws.generarXMLDOC("XML");
            aXML.getDocumentElement().appendChild(getEntidades(srvPlan,srvPub,srvDic,aGeometriaTransformada, aXML, Idioma));
            return aXML;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }


    }

    public DocumentFragment getEntidadesParaConsultaExtendida(ServicioPlaneamientoLocal srvPlan,ServicioPublicacionLocal srvPub,ServicioDiccionariosLocal srvDic,Geometry aGeo, Document aXML, String Idioma) {
        Entidad[] aEntidades;
        Ambito[] aAmbitos;
        NodeList aLista = null;
        NodeList aListaGrupos = null;
        Element aNode;
        Element aNodeGrupo;
        Element aNodeCapa;
        Element aNodeAmbitos;
        Element aNodeEntidades;
        DocumentFragment aXMLFrag;

        try {


            aAmbitos = srvPub.findAmbitosFromGeo(aGeo.toText());
            aNodeEntidades = aXML.createElement("ENTIDADES");
            for (Ambito Amb : aAmbitos) {

                aEntidades = srvPub.findEntidadesRefundidoFromGeo(GeoUtils.GeoToWKT(aGeo), Amb.getIden());

                aNodeAmbitos = aXML.createElement("AMBITO");
                aNodeAmbitos.setAttribute("id", String.valueOf(Amb.getIden()));
                aNodeAmbitos.setAttribute("nombre", srvDic.getTraduccion(Ambito.class, Amb.getIden(), Idioma));

                for (Entidad aEntidad : aEntidades) {
                    Determinacion grupoDet=srvPlan.getGrupoEntidad(aEntidad.getIden());
                    if (grupoDet != null) {
                        Capa[] capas = srvPub.findCapaFromCodigoGrupo(grupoDet.getCodigo());
                        if (capas != null) {
                            for (Capa capa : capas) {
                                aLista = XMLws.findNode("CAPA[@id=\"" + capa.getIden() + "\"]", aNodeAmbitos);
                                if (aLista == null || aLista.getLength() == 0) {
                                    aNodeCapa = aXML.createElement("CAPA");
                                    aNodeCapa.setAttribute("id", String.valueOf(capa.getIden()));
                                    aNodeCapa.setAttribute("nombre", capa.getNombre());
                                    aNodeCapa.setAttribute("orden", String.valueOf(capa.getOrden()));
                                    for (int iNodo = 0; iNodo < aNodeAmbitos.getChildNodes().getLength(); iNodo++) {
                                        Element aNodoAfter = (Element) aNodeAmbitos.getChildNodes().item(iNodo);
                                        if (capa.getOrden() < Integer.parseInt(aNodoAfter.getAttribute("orden"))) {
                                            aNodeAmbitos.insertBefore(aNodeCapa, aNodoAfter);
                                            break;
                                        }
                                    }
                                    if (aNodeCapa.getParentNode() == null) {
                                        aNodeAmbitos.appendChild(aNodeCapa);
                                    }
                                } else {
                                    aNodeCapa = (Element) aLista.item(0);
                                }

                                aListaGrupos = XMLws.findNode("GRUPO[@id=\"" + grupoDet.getIden() + "\"]", aNodeCapa);
                                if (aListaGrupos == null || aListaGrupos.getLength() == 0) {
                                    aNodeGrupo = XMLws.AddNode(aNodeCapa, "GRUPO");
                                    aNodeGrupo.setAttribute("id", String.valueOf(grupoDet.getIden()));
                                    aNodeGrupo.setAttribute("nombre", grupoDet.getNombre());
                                } else {
                                    aNodeGrupo = (Element) aListaGrupos.item(0);
                                }
                                /*aNodeEntidad = aXML.createElement("ENTIDAD");
                                aNodeGrupo.appendChild(aNodeEntidad);
                                aNodeEntidad.setAttribute("id", String.valueOf(aEntidad.getIden()));
                                aNodeEntidad.setAttribute("clave", aEntidad.getClave());
                                aNodeEntidad.setAttribute("nombre", aEntidad.getNombre());
                                aNodeCapa.appendChild(aNodeGrupo);*/

                                //RELLENO ENTIDADES

                                fillEntidadExtendida(srvPlan,aXML, aEntidad, aNodeGrupo/*, aNodeDef*/);

                                //FIN DE RELLENO DE ENTIDADES
                            }
                        }
                    }


                }
                aNodeEntidades.appendChild(aNodeAmbitos);
            }
            aXMLFrag = aXML.createDocumentFragment();
            aXMLFrag.appendChild(aNodeEntidades);
            return aXMLFrag;
        } catch (Exception ex) {
            Logger.getLogger(GestionEntidades.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    private void fillEntidadExtendida(ServicioPlaneamientoLocal srvPlan,Document aXML, Entidad aEntidad, Element aNodeGrupo/*, Element aNodeDefiniciones*/) {
        try {
            Determinacion[] detsAplicadas;
            int iCaso;
            String aTexto;
            String aTextoNormalizado;
            int idCaracterGrupoEntidad = 20;
            Element aNodeCondiciones;
            Element aNodeCondicion;
            Element aNodeCasos;
            Element aNodeCaso;
            Element aNodeValores;
            Element aNodeValor;
            Element aNodeRegEsps;
            Element aNodeRegEsp;
            Element aNodeEntidad;
            Element aNodeDefinicion;
            NodeList aLista;
            String aTipoDet;
            int aOrdenTipoDet;
            String[] valores;



            aNodeEntidad = XMLws.AddNode(aNodeGrupo, "ENTIDAD");
            aNodeEntidad.setAttribute("clave", aEntidad.getClave());
            aNodeEntidad.setAttribute("id", String.valueOf(aEntidad.getIden()));
            aNodeEntidad.setAttribute("nombre", aEntidad.getNombre());
            aNodeEntidad.setAttribute("orden", String.valueOf(aEntidad.getOrden()));

            idCaracterGrupoEntidad = Integer.valueOf(es.mitc.redes.urbanismoenred.utils.recursos.textos.Textos.getTexto("diccionario", "caracterdeterminacion.grupoentidades"));
            detsAplicadas = srvPlan.getDeterminacionesEntidad(aEntidad.getIden());
            if (detsAplicadas.length > 1) { //Excluímos el grupo
                aNodeCondiciones = XMLws.AddNode(aNodeEntidad, "CONDICIONES");
                for (Determinacion aDet : detsAplicadas) {
                    if (aDet.getIdcaracter() != idCaracterGrupoEntidad) {
                        aNodeCondicion = aXML.createElement("CONDICION");
                        aNodeCondicion.setAttribute("determinacion", aDet.getNombre());
                        if (aDet.getDeterminacionByIddeterminacionbase()!=null){
                            Determinacion detBase = srvPlan.get(Determinacion.class, aDet.getDeterminacionByIddeterminacionbase().getIden());
                            aNodeCondicion.setAttribute("determinacion_normalizada", detBase.getNombre());
                        }
                        aNodeCondicion.setAttribute("iddeterminacion", String.valueOf(aDet.getIden()));
                        aNodeCondicion.setAttribute("apartado", aDet.getApartadoCompleto());
                        aNodeCondicion.setAttribute("ordendeterminacion", String.valueOf(aDet.getOrden()));

                        valores = es.mitc.redes.urbanismoenred.utils.recursos.textos.Textos.getTexto("consola", "caracterregimendirecto").split(",");
                        aTipoDet = "";
                        aOrdenTipoDet = 0;
                        for (String valor : valores) {
                            if (aDet.getIdcaracter() == Integer.valueOf(valor)) {
                                aTipoDet = "RD";
                                aOrdenTipoDet = 1;
                                break;
                            }
                        }
                        if (aTipoDet.equals("")) {
                            valores = es.mitc.redes.urbanismoenred.utils.recursos.textos.Textos.getTexto("consola", "caracteruso").split(",");
                            for (String valor : valores) {
                                if (aDet.getIdcaracter() == Integer.valueOf(valor)) {
                                    aTipoDet = "USO";
                                    aOrdenTipoDet = 2;
                                    break;
                                }
                            }
                        }
                        if (aTipoDet.equals("")) {
                            valores = es.mitc.redes.urbanismoenred.utils.recursos.textos.Textos.getTexto("consola", "caracteracto").split(",");
                            for (String valor : valores) {
                                if (aDet.getIdcaracter() == Integer.valueOf(valor)) {
                                    aTipoDet = "ACTO";
                                    aOrdenTipoDet = 3;
                                    break;
                                }
                            }
                        }
                        //if (aCond.getDeterminacion().getIdcaracter()== Textos.getTexto("consola",
                        aNodeCondicion.setAttribute("tipo", aTipoDet);
                        aNodeCondicion.setAttribute("orden", String.valueOf(aOrdenTipoDet));
                        aNodeCondiciones.appendChild(aNodeCondicion);

                        iCaso = 0;
                        aNodeCasos = aXML.createElement("CASOS");
                        aNodeCondicion.appendChild(aNodeCasos);
                        Casoentidaddeterminacion[] casos = srvPlan.getCasos(aEntidad.getIden(), aDet.getIden());
                        for (Casoentidaddeterminacion aCaso : casos) {
                            aNodeCaso = aXML.createElement("CASO");


                            //if (aCond.getCasoentidaddeterminacions().size()>1){
                            if (aCaso.getNombre() != null) {
                                aNodeCaso.setAttribute("nombre", aCaso.getNombre());
                            } else {
                                aNodeCaso.setAttribute("nombre", "Caso " + String.valueOf(iCaso));
                            }
                            iCaso++;
                            //}
                            aNodeCasos.appendChild(aNodeCaso);
                            aNodeValores = aXML.createElement("VALORES");
                            aNodeCaso.appendChild(aNodeValores);
                            Entidaddeterminacionregimen[] regimenes = srvPlan.getRegimenDeCaso(aCaso.getIden());
                            for (Entidaddeterminacionregimen aReg : regimenes) {
                                //Detterminación de Régimen
                                aNodeValor = aXML.createElement("VALOR");
                                aNodeValores.appendChild(aNodeValor);
                                if (aReg.getDeterminacion() != null) {
                                    Determinacion detRegimen = srvPlan.get(Determinacion.class, aReg.getDeterminacion().getIden());
                                    aTexto = detRegimen.getNombre() + ": ";
                                    /*aTextoNormalizado="";
                                    if (aReg.getDeterminacion().getDeterminacionByIddeterminacionbase()!=null){
                                    aTextoNormalizado = aReg.getDeterminacion().getDeterminacionByIddeterminacionbase().getNombre() + ": ";
                                    }*/
                                    if (aReg.getOpciondeterminacion() != null) {
                                        Determinacion detValor = srvPlan.get(Determinacion.class, srvPlan.get(Opciondeterminacion.class, aReg.getOpciondeterminacion().getIden()).getDeterminacionByIddeterminacionvalorref().getIden());
                                        aTexto = aTexto + detValor.getNombre();
                                        /*if (aReg.getOpciondeterminacion().getDeterminacionByIddeterminacionvalorref().getDeterminacionByIddeterminacionbase()!=null){
                                        aTextoNormalizado = aTextoNormalizado + aReg.getOpciondeterminacion().getDeterminacionByIddeterminacionvalorref().getDeterminacionByIddeterminacionbase().getNombre();
                                        }*/
                                    }
                                    aNodeValor.setAttribute("valor", aTexto);
                                    aNodeValor.setAttribute("ordenDetRegimen", String.valueOf(detRegimen.getOrden()));
                                } else {
                                    if (aReg.getOpciondeterminacion() == null) {
                                        if (aReg.getValor() != null && !aReg.getValor().equals("")) {
                                            Determinacion aUnidad = srvPlan.getUnidad(aDet.getIden());
                                            if (aUnidad != null) {
                                                aNodeValor.setAttribute("valor", aReg.getValor() + " " + aUnidad.getNombre());
                                            } else {
                                                aNodeValor.setAttribute("valor", aReg.getValor());
                                            }
                                        }
                                    } else {
                                        Determinacion detValor = srvPlan.get(Determinacion.class, srvPlan.get(Opciondeterminacion.class, aReg.getOpciondeterminacion().getIden()).getDeterminacionByIddeterminacionvalorref().getIden());
                                        aNodeValor.setAttribute("valor", detValor.getNombre());
                                        if (detValor.getDeterminacionByIddeterminacionbase()!=null){
                                            Determinacion detValorBase = srvPlan.get(Determinacion.class, detValor.getDeterminacionByIddeterminacionbase().getIden());
                                            aNodeValor.setAttribute("valor_normalizado", detValorBase.getNombre());
                                        }
                                    }
                                }
                                //Régimen Específico
                                Regimenespecifico[] regEsps = srvPlan.getRegimenesEspecificos(aReg.getIden());
                                if (regEsps.length > 0) {
//                                    aNodeRegEsps = aXML.createElement("REGIMENES-ESPECIFICOS");
//                                    aNodeValor.appendChild(aNodeRegEsps);
                                    aNodeRegEsps = XMLws.AddNode(aNodeValor, "REGIMENES-ESPECIFICOS");
                                    for (Regimenespecifico aRegEsp : regEsps) {
                                        aNodeRegEsp = aXML.createElement("REGIMEN-ESPECIFICO");
                                        aNodeRegEsp.setTextContent(textoRegimenEspecifico(srvPlan,aRegEsp));
                                        aNodeRegEsps.appendChild(aNodeRegEsp);
                                    }
                                } else {
                                    aNodeRegEsps = XMLws.AddNode(aNodeValor, "REGIMENES-ESPECIFICOS");
                                    aNodeRegEsp = XMLws.AddNode(aNodeRegEsps, "REGIMEN-ESPECIFICO");
                                }
                            }
                            //document.add(new Paragraph(" "));
                        }
                        aNodeCasos.setAttribute("ncasos", String.valueOf(aNodeCasos.getChildNodes().getLength()));
                    }
                }
            } else {
                aNodeCondiciones = XMLws.AddNode(aNodeEntidad, "CONDICIONES");
                aNodeCondicion = XMLws.AddNode(aNodeCondiciones, "CONDICION");
                aNodeCasos = XMLws.AddNode(aNodeCondicion, "CASOS");
                aNodeCaso = XMLws.AddNode(aNodeCasos, "CASO");
                aNodeValores = XMLws.AddNode(aNodeCaso, "VALORES");
                aNodeValor = XMLws.AddNode(aNodeValores, "VALOR");
                aNodeRegEsps = XMLws.AddNode(aNodeValor, "REGIMENES-ESPECIFICOS");
                aNodeRegEsp = XMLws.AddNode(aNodeRegEsps, "REGIMEN-ESPECIFICO");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private String textoRegimenEspecifico(ServicioPlaneamientoLocal srvPlan,Regimenespecifico aRegEsp) {
        String aRespuesta = "";

        if (aRegEsp != null) {
            aRespuesta = aRegEsp.getNombre();
            aRespuesta = aRespuesta + ":\n\n" + aRegEsp.getTexto();
            Regimenespecifico[] hijos = srvPlan.getRegimenesEspecificosHijos(aRegEsp.getIden());
            for (Regimenespecifico aHijo : hijos) {
                aRespuesta = aRespuesta + textoRegimenEspecifico(srvPlan,aHijo);
            }
        }

        return aRespuesta;
    }

    public DocumentFragment getEntidades(ServicioPlaneamientoLocal srvPlan,ServicioPublicacionLocal srvPub,ServicioDiccionariosLocal srvDic,Geometry aGeo, Document aXML, String Idioma) {
        Entidad[] aEntidades;
        Ambito[] aAmbitos;
        NodeList aLista = null;
        NodeList aListaGrupos = null;
        Element aNode;
        Element aNodeAdscripcion;
        Element aNodeGrupo;
        Element aNodeCapa;
        Element aNodeEntidades;
        Element aNodeAmbito;
        DocumentFragment aXMLFrag;

        try {

            aAmbitos = srvPub.findAmbitosFromGeo(aGeo.toText());
            aNodeEntidades = aXML.createElement("ENTIDADES");
            for (Ambito Amb : aAmbitos) {
                aEntidades = srvPub.findEntidadesRefundidoFromGeo(GeoUtils.GeoToWKT(aGeo), Amb.getIden());

                aNodeAmbito =XMLws.AddNode(aNodeEntidades, "AMBITO");
                aNodeAmbito.setAttribute("id", String.valueOf(Amb.getIden()));
                aNodeAmbito.setAttribute("nombre", srvDic.getTraduccion(Ambito.class, Amb.getIden(), Idioma));

                for (Entidad aEntidad : aEntidades) {
                    //ACR: Cambio motivado por las incidencias INC000000264195 y INC000000264191
                    if (!aEntidad.isBsuspendida()) {
                        Determinacion grupo = srvPlan.getGrupoEntidad(aEntidad.getIden());
                        if (grupo != null) {
                            Capa[] capas = srvPub.findCapaFromCodigoGrupo(grupo.getCodigo());
                            if (capas != null) {
                                for (Capa capa : capas) {
                                    aLista = XMLws.findNode("CAPA[@id=\"" + capa.getIden() + "\"]", aNodeAmbito);
                                    if (aLista == null || aLista.getLength() == 0) {
                                        aNodeCapa = aXML.createElement("CAPA");
                                        aNodeCapa.setAttribute("id", String.valueOf(capa.getIden()));
                                        aNodeCapa.setAttribute("nombre", capa.getNombre());
                                        aNodeCapa.setAttribute("orden", String.valueOf(capa.getOrden()));
                                        for (int iNodo = 0; iNodo < aNodeAmbito.getChildNodes().getLength(); iNodo++) {
                                            Element aNodoAfter = (Element) aNodeAmbito.getChildNodes().item(iNodo);
                                            if (capa.getOrden() < Integer.parseInt(aNodoAfter.getAttribute("orden"))) {
                                                aNodeAmbito.insertBefore(aNodeCapa, aNodoAfter);
                                                break;
                                            }
                                        }
                                        if (aNodeCapa.getParentNode() == null) {
                                            aNodeAmbito.appendChild(aNodeCapa);
                                        }
                                    } else {
                                        aNodeCapa = (Element) aLista.item(0);
                                    }

                                    aListaGrupos = XMLws.findNode("GRUPO[@id=\"" + grupo.getIden() + "\"]", aNodeCapa);
                                    if (aListaGrupos == null || aListaGrupos.getLength() == 0) {
                                        aNodeGrupo = aXML.createElement("GRUPO");
                                        aNodeGrupo.setAttribute("id", String.valueOf(grupo.getIden()));
                                        aNodeGrupo.setAttribute("nombre", grupo.getNombre());
                                        aNodeCapa.appendChild(aNodeGrupo);
                                    } else {
                                        aNodeGrupo = (Element) aListaGrupos.item(0);
                                    }
                                    aNode = aXML.createElement("ENTIDAD");
                                    aNodeGrupo.appendChild(aNode);
                                    aNode.setAttribute("id", String.valueOf(aEntidad.getIden()));
                                    aNode.setAttribute("clave", aEntidad.getClave());
                                    aNode.setAttribute("nombre", aEntidad.getNombre());

                                    PropiedadesAdscripcion[] ads = srvPlan.getPropiedadesAdscripcionEntidad(aEntidad.getIden());
                                    for (PropiedadesAdscripcion ad : ads) {
                                        aNodeAdscripcion = aXML.createElement("ADSCRIPCION");
                                        if (ad.getTipo() != null) {
                                            aNodeAdscripcion.setAttribute("tipo", ad.getTipo().getNombre());
                                        }
                                        if (ad.getDestino().getIden() != aEntidad.getIden()) {
                                            aNodeAdscripcion.setAttribute("destino", ad.getDestino().getNombre());
                                            aNodeAdscripcion.setAttribute("id_destino", String.valueOf(ad.getDestino().getIden()));
                                        }
                                        if (ad.getOrigen().getIden() != aEntidad.getIden()) {
                                            aNodeAdscripcion.setAttribute("origen", ad.getOrigen().getNombre());
                                            aNodeAdscripcion.setAttribute("id_origen", String.valueOf(ad.getOrigen().getIden()));
                                        }
                                        aNodeAdscripcion.setAttribute("cuantia", String.valueOf(ad.getCuantia()));
                                        if (ad.getUnidad() != null) {
                                            aNodeAdscripcion.setAttribute("unidad", ad.getUnidad().getNombre());
                                        }
                                        if (ad.getTexto() != null && !ad.getTexto().isEmpty()) {
                                            aNodeAdscripcion.setAttribute("texto", ad.getTexto());
                                        }
                                        aNode.appendChild(aNodeAdscripcion);
                                    }
                                }
                            }
                        }


                    }
                }
            }
            aXMLFrag = aXML.createDocumentFragment();
            aXMLFrag.appendChild(aNodeEntidades);
            return aXMLFrag;
        } catch (Exception ex) {
            Logger.getLogger(GestionEntidades.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
}

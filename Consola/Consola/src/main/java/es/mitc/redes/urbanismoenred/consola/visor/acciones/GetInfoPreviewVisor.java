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

import com.vividsolutions.jts.geom.Geometry;
import es.mitc.redes.urbanismoenred.consola.util.helpers.GeometryHelper;
import es.mitc.redes.urbanismoenred.consola.visor.IAccion;
import es.mitc.redes.urbanismoenred.data.rpm.explotacion.Capa;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Casoentidaddeterminacion;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Determinacion;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Documento;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Documentoshp;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidad;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidaddeterminacionregimen;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Opciondeterminacion;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Regimenespecifico;
import es.mitc.redes.urbanismoenred.servicios.planeamiento.ServicioPlaneamientoLocal;
import es.mitc.redes.urbanismoenred.utils.recursos.textos.Textos;
import es.mitc.redes.urbanismoenred.utils.recursos.xml.XML;
import es.mitc.redes.urbanismoenred.utils.serviciosweb.ServicioPublicacionLocal;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * @author Arnaiz Consultores
 *
 */
@Stateless(name = "GET_INFO_PREVIEW_VISOR")
public class GetInfoPreviewVisor implements IAccion {

    /* (non-Javadoc)
     * @see es.mitc.redes.urbanismoenred.consola.planeamiento.IAccion#ejecutar(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @EJB
    private ServicioPublicacionLocal servicioPub;
    @EJB
    private ServicioPlaneamientoLocal srvPlan;

    @Override
    public void ejecutar(HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        try {
            int idTramite = Integer.valueOf(request.getParameter("idTramite"));
            String strGeom = "POINT(" + request.getParameter("X") + " " + request.getParameter("Y") + ")";
            Geometry geom = GeometryHelper.GeoFromWKT(strGeom);
            Document aXML = null;
            if (geom != null) {
                if (request.getParameter("srs") != null) {
                    geom = GeometryHelper.TransformGeometry(geom, request.getParameter("srs"), Textos.getTexto("consola", "projection"));
                }
                Entidad[] entidades = servicioPub.findEntidadesFromGeo(geom.toText(), idTramite);
                aXML = XML.generarXMLDOC("RESPUESTA");
                aXML.getDocumentElement().appendChild(this.getEntidadesParaConsultaExtendida(entidades, aXML, "es"));
                aXML.getDocumentElement().appendChild(this.getPlanos(aXML, geom, idTramite));
            }

            if (aXML != null) {
                response.getWriter().print(XML.XmlDocToString(aXML));
            } else {
                response.getWriter().print("");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            response.getWriter().print("");
        }
        response.getWriter().flush();
    }

    private DocumentFragment getPlanos(Document aXML, Geometry geom, Integer idTramite) {
        try {
            DocumentFragment aXMLFrag = aXML.createDocumentFragment();
            Element aNodeDocumentos = XML.AddNode(aXMLFrag, "PLANOS");
            Documentoshp[] aDocumentosShp = servicioPub.findPlanosFromGeoTramite(geom.toText(), idTramite);
            aNodeDocumentos.setAttribute("count", String.valueOf(aDocumentosShp.length));
            for (Documentoshp aDocShp : aDocumentosShp) {
                NodeList aLista = XML.findNode("DOCUMENTO[@id=\"" + aDocShp.getDocumento().getIden() + "\"]", aNodeDocumentos);
                Element aNodeDoc=null;
                if (aLista == null || aLista.getLength() == 0) {
                    Documento aDocumento = srvPlan.get(Documento.class, aDocShp.getDocumento().getIden());
                    aNodeDoc = XML.AddNode(aNodeDocumentos,"DOCUMENTO");
                    aNodeDoc.setAttribute("id", String.valueOf(aDocumento.getIden()));
                    aNodeDoc.setAttribute("nombre", aDocumento.getNombre());
                } else {
                    aNodeDoc = (Element) aLista.item(0);
                }
                Element nodeHoja=XML.AddNode(aNodeDoc, "HOJA");
                nodeHoja.setAttribute("id", String.valueOf(aDocShp.getIden()));
                nodeHoja.setAttribute("nombre", aDocShp.getHoja());
            }
            return aXMLFrag;
        } catch (Exception ex) {        
            Logger.getLogger(GetInfoPreviewVisor.class.getName()).log(Level.ERROR, null, ex);
            return null;
        }
    }

    private DocumentFragment getEntidadesParaConsultaExtendida(Entidad[] aEntidades, Document aXML, String Idioma) {
        NodeList aLista = null;
        NodeList aListaGrupos = null;
        Element aNodeGrupo;
        Element aNodeCapa;
        Element aNodeEntidades;
        DocumentFragment aXMLFrag;

        try {
            aXMLFrag = aXML.createDocumentFragment();
            aNodeEntidades = XML.AddNode(aXMLFrag, "ENTIDADES");

            for (Entidad aEntidad : aEntidades) {
                Determinacion grupoDet = srvPlan.getGrupoEntidad(aEntidad.getIden());
                if (grupoDet != null) {
                    Capa[] capas = servicioPub.findCapaFromCodigoGrupo(grupoDet.getCodigo());
                    if (capas == null || capas.length == 0) {
                        List<Capa> listaCapasOtras = new ArrayList<Capa>();
                        listaCapasOtras.add(new Capa(Integer.MAX_VALUE, "Otras", Integer.MAX_VALUE));
                        capas = listaCapasOtras.toArray(new Capa[0]);
                    }
                    for (Capa capa : capas) {
                        aLista = XML.findNode("CAPA[@id=\"" + capa.getIden() + "\"]", aNodeEntidades);
                        if (aLista == null || aLista.getLength() == 0) {
                            aNodeCapa = aXML.createElement("CAPA");
                            aNodeCapa.setAttribute("id", String.valueOf(capa.getIden()));
                            aNodeCapa.setAttribute("nombre", capa.getNombre());
                            aNodeCapa.setAttribute("orden", String.valueOf(capa.getOrden()));
                            for (int iNodo = 0; iNodo < aNodeEntidades.getChildNodes().getLength(); iNodo++) {
                                Element aNodoAfter = (Element) aNodeEntidades.getChildNodes().item(iNodo);
                                if (capa.getOrden() < Integer.parseInt(aNodoAfter.getAttribute("orden"))) {
                                    aNodeEntidades.insertBefore(aNodeCapa, aNodoAfter);
                                    break;
                                }
                            }
                            if (aNodeCapa.getParentNode() == null) {
                                aNodeEntidades.appendChild(aNodeCapa);
                            }
                        } else {
                            aNodeCapa = (Element) aLista.item(0);
                        }

                        aListaGrupos = XML.findNode("GRUPO[@id=\"" + grupoDet.getIden() + "\"]", aNodeCapa);
                        if (aListaGrupos == null || aListaGrupos.getLength() == 0) {
                            aNodeGrupo = XML.AddNode(aNodeCapa, "GRUPO");
                            aNodeGrupo.setAttribute("id", String.valueOf(grupoDet.getIden()));
                            aNodeGrupo.setAttribute("nombre", grupoDet.getNombre());
                            aNodeGrupo.setAttribute("codigo", grupoDet.getCodigo());
                        } else {
                            aNodeGrupo = (Element) aListaGrupos.item(0);
                        }
                        //RELLENO ENTIDADES

                        fillEntidadExtendida(aXML, aEntidad, aNodeGrupo/*, aNodeDef*/);

                        //FIN DE RELLENO DE ENTIDADES
                    }

                }


            }

            return aXMLFrag;
        } catch (Exception ex) {
            Logger.getLogger(GetInfoPreviewVisor.class.getName()).log(Level.ERROR, null, ex);
            return null;
        }
    }

    private void fillEntidadExtendida(Document aXML, Entidad aEntidad, Element aNodeGrupo) {
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

            aNodeEntidad = XML.AddNode(aNodeGrupo, "ENTIDAD");
            aNodeEntidad.setAttribute("clave", aEntidad.getClave());
            aNodeEntidad.setAttribute("id", String.valueOf(aEntidad.getIden()));
            aNodeEntidad.setAttribute("nombre", aEntidad.getNombre());
            aNodeEntidad.setAttribute("orden", String.valueOf(aEntidad.getOrden()));

            idCaracterGrupoEntidad = Integer.valueOf(es.mitc.redes.urbanismoenred.utils.recursos.textos.Textos.getTexto("diccionario", "caracterdeterminacion.grupoentidades"));
            detsAplicadas = srvPlan.getDeterminacionesEntidad(aEntidad.getIden());
            if (detsAplicadas.length > 1) { //Excluímos el grupo
                aNodeCondiciones = XML.AddNode(aNodeEntidad, "CONDICIONES");
                for (Determinacion aDet : detsAplicadas) {
                    if (aDet.getIdcaracter() != idCaracterGrupoEntidad) {
                        aNodeCondicion = aXML.createElement("CONDICION");
                        aNodeCondicion.setAttribute("determinacion", aDet.getNombre());
                        if (aDet.getDeterminacionByIddeterminacionbase() != null) {
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
                                        if (detValor.getDeterminacionByIddeterminacionbase() != null) {
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
                                    aNodeRegEsps = XML.AddNode(aNodeValor, "REGIMENES-ESPECIFICOS");
                                    for (Regimenespecifico aRegEsp : regEsps) {
                                        aNodeRegEsp = aXML.createElement("REGIMEN-ESPECIFICO");
                                        aNodeRegEsp.setAttribute("nombre", aRegEsp.getNombre());
                                        aNodeRegEsp.setTextContent(aRegEsp.getTexto());
                                        aNodeRegEsps.appendChild(aNodeRegEsp);
                                        addRegimenEspecificoHijo(aRegEsp, aNodeRegEsp);
                                    }
                                } else {
                                    aNodeRegEsps = XML.AddNode(aNodeValor, "REGIMENES-ESPECIFICOS");
                                    aNodeRegEsp = XML.AddNode(aNodeRegEsps, "REGIMEN-ESPECIFICO");
                                }
                            }
                            //document.add(new Paragraph(" "));
                        }
                        aNodeCasos.setAttribute("ncasos", String.valueOf(aNodeCasos.getChildNodes().getLength()));
                    }
                }
            } else {
                aNodeCondiciones = XML.AddNode(aNodeEntidad, "CONDICIONES");
                aNodeCondicion = XML.AddNode(aNodeCondiciones, "CONDICION");
                aNodeCasos = XML.AddNode(aNodeCondicion, "CASOS");
                aNodeCaso = XML.AddNode(aNodeCasos, "CASO");
                aNodeValores = XML.AddNode(aNodeCaso, "VALORES");
                aNodeValor = XML.AddNode(aNodeValores, "VALOR");
                aNodeRegEsps = XML.AddNode(aNodeValor, "REGIMENES-ESPECIFICOS");
                aNodeRegEsp = XML.AddNode(aNodeRegEsps, "REGIMEN-ESPECIFICO");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private String addRegimenEspecificoHijo(Regimenespecifico aRegEspPadre, Element nodoPadre) {
        String aRespuesta = "";

        if (aRegEspPadre != null) {
            Regimenespecifico[] hijos = srvPlan.getRegimenesEspecificosHijos(aRegEspPadre.getIden());
            for (Regimenespecifico aHijo : hijos) {
                Element node = XML.AddNode(nodoPadre, "REGIMEN-ESPECIFICO");
                node.setAttribute("nombre", aHijo.getNombre());
                node.setTextContent(aHijo.getTexto());
                addRegimenEspecificoHijo(aHijo, node);
            }
        }

        return aRespuesta;
    }
}

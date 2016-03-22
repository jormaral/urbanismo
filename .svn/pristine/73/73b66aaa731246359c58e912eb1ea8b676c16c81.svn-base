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

package es.mitc.redes.urbanismoenred.serviciosweb;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 *
 * @author a.centeno
 */
public class consultasCatastro {

    public static String InverseGeocode(double x, double y, String srs){
        try {
            String url = null;
            Document aDoc;
            NodeList aNodeList;
            String aDireccion = null;
            String aDistancia = null;
            
            url = Textos.getTexto("urbrWS", "URL_CoordenadasCatastro");
            String data = leerURL(url + "/Consulta_RCCOOR_Distancia?SRS=" + srs + "&Coordenada_X=" + x + "&Coordenada_Y=" + y);
            aDoc = XMLws.XmlDocFromString(data);
            if (aDoc!=null){
                aNodeList = XMLws.findNode("/consulta_coordenadas_distancias/coordenadas_distancias/coordd/lpcd/pcd/ldt", aDoc.getDocumentElement());
                if (aNodeList.getLength() > 0) {
                    aDireccion = aNodeList.item(0).getTextContent();
                }
                aNodeList = XMLws.findNode("/consulta_coordenadas_distancias/coordenadas_distancias/coordd/lpcd/pcd/dis", aDoc.getDocumentElement());
                if (aNodeList.getLength() > 0) {
                    aDistancia = aNodeList.item(0).getTextContent();
                }
                //cerramos la entrada
                if (aDireccion==null){
                    data=null;
                }else{
                    data = aDireccion + " (" + aDistancia + " m)";
                }
                return data;
            }
            
        } catch (Exception ex) {
            Logger.getLogger(consultasCatastro.class.getName()).log(Level.INFO, null, ex);
            return "X=" + x + " Y=" + y;
        }
        return "X=" + x + " Y=" + y;
    }

    public static String referenciaCatastral(double x, double y, String srs){
        try {
            String url = null;
            Document aDoc;
            NodeList aNodeList;
            String aReferencia = null;
            
            url = Textos.getTexto("urbrWS", "URL_CoordenadasCatastro");
            String data = leerURL(url + "/Consulta_RCCOOR_Distancia?SRS=" + srs + "&Coordenada_X=" + x + "&Coordenada_Y=" + y);
            aDoc = XMLws.XmlDocFromString(data);
            aNodeList = XMLws.findNode("/consulta_coordenadas_distancias/coordenadas_distancias/coordd/lpcd/pcd/pc/pc1", aDoc.getDocumentElement());
            if (aNodeList.getLength() > 0) {
                aReferencia = aNodeList.item(0).getTextContent();
            }
            aNodeList = XMLws.findNode("/consulta_coordenadas_distancias/coordenadas_distancias/coordd/lpcd/pcd/pc/pc2", aDoc.getDocumentElement());
            if (aNodeList.getLength() > 0) {
                aReferencia += aNodeList.item(0).getTextContent();
            }

            return aReferencia;
        } catch (Exception ex) {
            Logger.getLogger(consultasCatastro.class.getName()).log(Level.INFO, null, ex);
            return "X=" + x + " Y=" + y;
        }

    }
    
    public static String CoordenadasRefCatastral(String refCatastral,String srs){
        try {
            String url = null;
            Document aDoc;
            NodeList aNodeList;
            String aX = null;
            String aY = null;

            url = Textos.getTexto("urbrWS", "URL_CoordenadasCatastro");
            String data = leerURL(url + "/Consulta_CPMRC?Provincia=&Municipio=&SRS=" + srs + "&RC=" + refCatastral );
            aDoc = XMLws.XmlDocFromString(data);
            aNodeList = XMLws.findNode("/consulta_coordenadas/coordenadas/coord/geo/xcen", aDoc.getDocumentElement());
            if (aNodeList.getLength() > 0) {
                aX = aNodeList.item(0).getTextContent();
            }
            aNodeList = XMLws.findNode("/consulta_coordenadas/coordenadas/coord/geo/ycen", aDoc.getDocumentElement());
            if (aNodeList.getLength() > 0) {
                aY = aNodeList.item(0).getTextContent();
            }
            //cerramos la entrada

            return "POINT (" + aX + " " + aY + ")";
        } catch (Exception ex) {
            Logger.getLogger(consultasCatastro.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

    }

    public static Document getTiposVias(){
        try {
            Document aXML;
            Element aNode;
            List<String> aTipos;

            aXML=XMLws.generarXMLDOC("TIPOS");
            
            aTipos=Textos.getKeys("urbrWS", "tipovia_");
            for (String aTipo:aTipos){
                aNode=aXML.createElement("TIPO");
                aNode.setAttribute("abrev", aTipo.split("@")[0]);
                aNode.setAttribute("nombre", aTipo.split("@")[1]);
                aXML.getDocumentElement().appendChild(aNode);
            }
            
            return aXML;
        } catch (Exception ex) {
            Logger.getLogger(consultasCatastro.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

    }


     public static Document getVias(String nombreProvincia,String nombreMunicipio,String abrevTipo,String nombreVia){
        String url=null;

        url=Textos.getTexto("urbrWS", "URL_CoordenadasCallejero");
        String data=leerURL(url + "/ConsultaVia?Provincia=" + nombreProvincia + "&Municipio=" + nombreMunicipio + "&TipoVia=" + abrevTipo + "&NombreVia=" + nombreVia);
        return  XMLws.XmlDocFromString(data);
    }

    public static Document getNumero(String nombreProvincia,String nombreMunicipio,String abrevTipo,String nombreVia,String numero,boolean encodeParams){
        try {
            String url = null;
            url = Textos.getTexto("urbrWS", "URL_CoordenadasCallejero");
            String data ;
            if (encodeParams){
                 data= leerURL(url + "/ConsultaNumero?Provincia=" + java.net.URLEncoder.encode(nombreProvincia, "UTF8") + "&Municipio=" + java.net.URLEncoder.encode(nombreMunicipio, "UTF8") + "&TipoVia=" + java.net.URLEncoder.encode(abrevTipo, "UTF8") + "&NomVia=" + java.net.URLEncoder.encode(nombreVia, "UTF8") + "&Numero=" + java.net.URLEncoder.encode(numero, "UTF8"));
            }else{
                 data = leerURL(url + "/ConsultaNumero?Provincia=" + nombreProvincia + "&Municipio=" + nombreMunicipio + "&TipoVia=" + abrevTipo + "&NomVia=" + nombreVia + "&Numero=" + numero);
            }
            return XMLws.XmlDocFromString(data);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(consultasCatastro.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public static Document getProvincias(){
        String url=null;

        url=Textos.getTexto("urbrWS", "URL_CoordenadasCallejero");
        String data=leerURL(url + "/ConsultaProvincia?");
        return  XMLws.XmlDocFromString(data);
    }

    public static Document getMunicipios(String nombreProvincia,String nombreMunicipio){
        String url=null;
        String data;
        
        url=Textos.getTexto("urbrWS", "URL_CoordenadasCallejero");
        if (nombreMunicipio!=null){
            data=leerURL(url + "/ConsultaMunicipio?Provincia=" + nombreProvincia + "&Municipio=" + nombreMunicipio);
        }else{
            data=leerURL(url + "/ConsultaMunicipio?Provincia=" + nombreProvincia + "&Municipio=");
        }
        return  XMLws.XmlDocFromString(data);
    }

   public static String leerURL(String urlString)
    {
        String estado="error COMIENZO - "+urlString;

            if(urlString!=null && urlString!="")
            {
                try
                {
                          //Indicamos la URL donde nos conectamos

                                URL url = new URL(urlString);

                                //url.openStream();
                          //Buffer con los datos recibidos
                                BufferedReader in = null;
                                  try
                                  {
                                        // Volcamos lo recibido al buffer

                                            in = new BufferedReader(new InputStreamReader(url.openStream(),"UTF-8"));

                                        //Transformamos el contenido del buffer a texto
                                             String inputLine;
                                             estado="";
                                        //Mientras haya cosas en el buffer las volcamos a las
                                        //cadenas de texto
                                             while ((inputLine = in.readLine()) != null)
                                             {
                                                estado = estado + inputLine;
                                             }
                                        //cerramos la entrada
                                             in.close();


                                  }
                                  catch(Throwable t)
                                  {
                                        estado="ERROR.BufferedReader."+t.getMessage();
                                  }
                }
                catch (MalformedURLException me)
                {
                    estado="ERROR.URL."+me.getMessage();
                }
                catch (IOException ioe)
                {
                    estado="Error.IO."+ioe.getMessage();
                }
            }
           return estado;
    }



}


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
package es.mitc.redes.urbanismoenred.geoserverapi;

import es.mitc.redes.urbanismoenred.rest.RESTClient;
import es.mitc.redes.urbanismoenred.serviciosweb.Textos;
import es.mitc.redes.urbanismoenred.serviciosweb.XMLws;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.PrintWriter;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.referencing.CRS;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.w3c.dom.Document;

/**
 *
 * @author a.centeno
 */
public class urbrGeoserver {

    private static String getWorkspace() {
        try {
            String strUrl = null;
            strUrl = Textos.getTexto("urbrWS", "URL_Geoserver");
            
            String WStemp=Textos.getTexto("urbrWS", "WorkSpace_Geoserver");
            String ret="<workspace><name>" + WStemp + "</name></workspace>";

            Document aXML;
            strUrl = strUrl + "/workspaces.xml";
            URL url = new URL(strUrl);
            String response=RESTClient.request(false, "GET", url, Textos.getTexto("urbrWS", "User_Geoserver"), Textos.getTexto("urbrWS", "Password_Geoserver"), null);
            aXML=XMLws.XmlDocFromString(response);
            if (XMLws.findNode("/workspaces/workspace[name=\"" + WStemp + "\"]", aXML.getDocumentElement()).getLength()==0){
                response=RESTClient.request(false, "POST", url, Textos.getTexto("urbrWS", "User_Geoserver"), Textos.getTexto("urbrWS", "Password_Geoserver"), new ByteArrayInputStream(ret.getBytes()));
            }
            if (response!=null){
                return WStemp;
            }else{
                return null;
            }
            
        } catch (Exception ex) {
            Logger.getLogger(urbrGeoserver.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } 

    }

    private static String getCoverageStore(String workSpace,String fullPath,String idServicio) {
        try {
            String strUrl = null;
            
            strUrl = Textos.getTexto("urbrWS", "URL_Geoserver");
            String Coverage=idServicio;
            String ret="<coverageStore><name>" + idServicio + "</name>" +
                        "<enabled>true</enabled>" +
                        "<workspace>" +
                        "<name>" + workSpace + "</name>" +
                        "</workspace>" +
                        "<type>WorldImage</type>" +
                        "<url>" +
                        fullPath.replace("\\", "/") +
                        "</url></coverageStore>";
            


            Document aXML;
            strUrl = strUrl + "/workspaces/" + workSpace + "/coveragestores.xml";
            URL url = new URL(strUrl);
            String response=RESTClient.request(false, "GET", url, Textos.getTexto("urbrWS", "User_Geoserver"), Textos.getTexto("urbrWS", "Password_Geoserver"), null);
            aXML=XMLws.XmlDocFromString(response);
            if (XMLws.findNode("/coverageStores/coverageStore[name=\"" + Coverage + "\"]", aXML.getDocumentElement()).getLength()==0){
                response=RESTClient.request(false, "POST", url, Textos.getTexto("urbrWS", "User_Geoserver"), Textos.getTexto("urbrWS", "Password_Geoserver"), new ByteArrayInputStream(ret.getBytes()));
            }
            if (response!=null){
                return Coverage;
            }else{
                return null;
            }

        } catch (Exception ex) {
            Logger.getLogger(urbrGeoserver.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

    }

    public static String creaServicioImagen(String fullPath,int idServicio,double minx,double miny,double maxx,double maxy) {
        String response=null;
        String workSpace=null;
        File aFilePrj=null;
        String strPrjFile=null;

        try
        {
            workSpace=getWorkspace();
            String Store=getCoverageStore(workSpace,fullPath,String.valueOf(idServicio));
            String strUrl=null;
            strUrl=Textos.getTexto("urbrWS", "URL_Geoserver");
            strUrl=strUrl + "/workspaces/" + workSpace + "/coveragestores/" + Store + "/coverages.xml";
            URL url = new URL(strUrl);
            String ret=null;
            CoordinateReferenceSystem crsOr;

            crsOr=CRS.decode(Textos.getTexto("urbrWS", "SRS_Datos"),true);
            try{
                aFilePrj=new File(fullPath);
                strPrjFile=aFilePrj.getParentFile() + java.io.File.separator + filenameSinExtension(aFilePrj) + ".prj";
                aFilePrj=new File(strPrjFile);

                PrintWriter writer = new PrintWriter(aFilePrj);
                writer.print(crsOr.toWKT());
                writer.close();
            }catch (Exception ex)
            {
                Logger.getLogger(urbrGeoserver.class.getName()).log(Level.SEVERE, null, ex);
            }

            response=RESTClient.request(false, "GET", url, Textos.getTexto("urbrWS", "User_Geoserver"), Textos.getTexto("urbrWS", "Password_Geoserver"), null);

            Document aXML;

            aXML=XMLws.XmlDocFromString(response);
            if (XMLws.findNode("/coverages/coverage[name=\"" + idServicio + "\"]", aXML.getDocumentElement()).getLength()==0){
                ret="<coverage><name>" + idServicio + "</name></coverage>";
                response=RESTClient.request(false, "POST", url, Textos.getTexto("urbrWS", "User_Geoserver"), Textos.getTexto("urbrWS", "Password_Geoserver"), new ByteArrayInputStream(ret.getBytes()));
            }

            strUrl=Textos.getTexto("urbrWS", "URL_Geoserver");
            strUrl=strUrl + "/workspaces/" + workSpace + "/coveragestores/" + Store + "/coverages/" + idServicio + ".xml";
            url = new URL(strUrl);


            response=RESTClient.request(false, "GET", url, Textos.getTexto("urbrWS", "User_Geoserver"), Textos.getTexto("urbrWS", "Password_Geoserver"), null);
            ret="<coverage>" +
                "<name>" + idServicio + "</name>" +
                "<title>" + idServicio + "</title>" +
                "<namespace><name>" + workSpace + "</name></namespace>" +
                "<description>Servicio Temporal " + idServicio + "</description>" +
                "<nativeCRS>" + crsOr.toWKT() + "</nativeCRS>" +
                "<srs>" + Textos.getTexto("urbrWS", "SRS_Datos") + "</srs>" +
                "<nativeBoundingBox>" +
                    "<minx>" + minx + "</minx>" +
                    "<maxx>" + maxx + "</maxx>" +
                    "<miny>" + miny + "</miny>" +
                    "<maxy>" + maxy + "</maxy>" +
                    "<crs>" + Textos.getTexto("urbrWS", "SRS_Datos") + "</crs>" +
                "</nativeBoundingBox>" +
                "<latLonBoundingBox>" +
                    "<minx>-180.0</minx>" +
                    "<maxx>180.0</maxx>" +
                    "<miny>-90.0</miny>" +
                    "<maxy>90.0</maxy>" +
                    "<crs>EPSG:4326</crs>" +
                "</latLonBoundingBox>" +
                "<enabled>true</enabled>" +
                "<store class=\"coverageStore\">" +
                "<name>" + idServicio + "</name>" +
                "</store>" +
                "<nativeFormat>WorldImage</nativeFormat>" +
                "</coverage>";


            response=RESTClient.request(false, "PUT", url, Textos.getTexto("urbrWS", "User_Geoserver"), Textos.getTexto("urbrWS", "Password_Geoserver"), new ByteArrayInputStream(ret.getBytes()));

        }
        catch(Exception e) {
            Logger.getLogger(urbrGeoserver.class.getName()).log(Level.SEVERE, null, e);
        }
        if (response!=null && response.equals("")){
           
                return Textos.getTexto("urbrWS", "URL_Geoserver").replace("/rest", "") + "/wms?SERVICE=WMS&LAYERS=" + workSpace + ":" + idServicio;
            
        }else{
            return response;
        }
    }

    private static String filenameSinExtension(File aFile) { // gets filename without extension
        int dot = aFile.getName().lastIndexOf(".");
        return aFile.getName().substring(0, dot);
      }
}


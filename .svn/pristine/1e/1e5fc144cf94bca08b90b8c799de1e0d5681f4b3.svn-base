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

package es.mitc.redes.urbanismoenred.consola.util.helpers;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKBWriter;
import com.vividsolutions.jts.io.WKTReader;
import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.CRS;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author Arnaiz Consultores
 */
public class GeometryHelper {
    public static Geometry unionGeoms(String[] WKT){
        Geometry res=null;
        for (String geoWKT : WKT){
            if (res==null){
                res=GeoFromWKT(geoWKT);
            }else{
                res=res.union(GeoFromWKT(geoWKT));
            }
        }
        return res;
    }
    
    public static Geometry GeoFromWKT(String WKT){
        try {
            GeometryFactory geometryFactory =new GeometryFactory();
            WKTReader reader = new WKTReader(geometryFactory);
            return  reader.read(WKT);
        } catch (ParseException ex) {
            Logger.getLogger(GeometryHelper.class.getName()).log(Level.SEVERE, null, ex);
            Logger.getLogger(GeometryHelper.class.getName()).log(Level.INFO,"WKT: " + WKT);
            return null;
        }
    }

    public static String GeoToWKT(Geometry aGeo){
        try {
            return aGeo.toText();
        } catch (Exception ex) {
            Logger.getLogger(GeometryHelper.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    public static byte[] GeoToWKB(Geometry aGeo){
        try {
            WKBWriter writer = new WKBWriter();
            return writer.write(aGeo);
        } catch (Exception ex) {
            Logger.getLogger(GeometryHelper.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    public static Geometry TransformGeometry(Geometry aGeo ,String srsOrigen,String srsDestino){
    CoordinateReferenceSystem crsOr;
    CoordinateReferenceSystem crsDest;
    Geometry projected;


        try {
            if ( srsOrigen==null || srsDestino==null || srsOrigen.equalsIgnoreCase(srsDestino) || srsDestino.equals("") || srsOrigen.equals("")){
                projected =  aGeo;
            }else{
                crsOr = CRS.decode(srsOrigen,true);
                crsDest = CRS.decode(srsDestino,true);


                // find a maths transform from WGS84 to the target CRS
                MathTransform mt = CRS.findMathTransform(crsOr, crsDest);
        
                projected= JTS.transform(aGeo, mt);

            }
            return projected;

        } catch(Exception ex) {
            ex.printStackTrace();
            return null;
        }


    }


}

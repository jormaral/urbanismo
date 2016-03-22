/*
* Copyright 2011 red.es
* Autores: IDOM Consulting
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

package com.mitc.redes.editorfip.servicios.informacionfip.consultafichaurbanisticas;


import java.util.logging.Level;
import java.util.logging.Logger;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKBWriter;
import com.vividsolutions.jts.io.WKTReader;

public class geoUtils {
    public static Geometry GeoFromWKT(String WKT){
        try {
            GeometryFactory geometryFactory =new GeometryFactory();
            WKTReader reader = new WKTReader(geometryFactory);
            return  reader.read(WKT);
        } catch (ParseException ex) {
            Logger.getLogger(geoUtils.class.getName()).log(Level.SEVERE, null, ex);
            Logger.getLogger(geoUtils.class.getName()).log(Level.INFO,"WKT: " + WKT);
            return null;
        }
    }

    public static String getWMS_BBOX(Geometry geometria){
        Envelope extension=new Envelope();
        extension.init(0, 0, 0, 0); //El BBOX siempre será el doble de ancho que de alto
        if (geometria.getEnvelopeInternal().getArea()==0){
            extension.expandBy(1000,500);
        }
        else
        {
            if (geometria.getEnvelopeInternal().getHeight()>geometria.getEnvelopeInternal().getWidth())
            {
                extension.expandBy(2*geometria.getEnvelopeInternal().getHeight(),geometria.getEnvelopeInternal().getHeight());
            }else{
                extension.expandBy(geometria.getEnvelopeInternal().getWidth(),geometria.getEnvelopeInternal().getWidth()/2);
            }
            if (extension.getHeight()<100){
                extension.expandBy(extension.getHeight()*2,extension.getHeight());
            }
            extension.expandBy(10,5);
        }
        extension.translate(geometria.getEnvelopeInternal().centre().x, geometria.getEnvelopeInternal().centre().y);
        return String.valueOf(extension.getMinX()) + "," + String.valueOf(extension.getMinY()) + "," + String.valueOf(extension.getMaxX()) + "," + String.valueOf(extension.getMaxY());
    }
    
    public static String GeoToWKT(Geometry aGeo){
        try {
            return aGeo.toString();
        } catch (Exception ex) {
            Logger.getLogger(geoUtils.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    public static byte[] GeoToWKB(Geometry aGeo){
        try {
            WKBWriter writer = new WKBWriter();
            return writer.write(aGeo);
        } catch (Exception ex) {
            Logger.getLogger(geoUtils.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
  
    /*
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
	*/
}

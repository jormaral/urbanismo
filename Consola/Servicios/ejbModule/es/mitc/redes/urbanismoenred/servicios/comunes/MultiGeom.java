package es.mitc.redes.urbanismoenred.servicios.comunes;

import com.vividsolutions.jts.geom.*;
import com.vividsolutions.jts.geom.util.LineStringExtracter;
import com.vividsolutions.jts.geom.util.PointExtracter;
import com.vividsolutions.jts.geom.util.PolygonExtracter;
import es.mitc.redes.urbanismoenred.utils.excepciones.RedesException;
import java.util.List;

public class MultiGeom {

    public static MultiPoint GetMultiPoint(Geometry geometria) throws RedesException{
        try {
            if (geometria ==null){
                return null;
            }
            MultiPoint punto;
            GeometryFactory fact=new GeometryFactory(geometria.getPrecisionModel(),geometria.getSRID());
            if (GeometryCollection.class.isAssignableFrom(geometria.getClass())){
                List<LineString> lineas=PointExtracter.getPoints(geometria);
                punto=fact.createMultiPoint(lineas.toArray(new Point[0]));
            }else if (Point.class.isAssignableFrom(geometria.getClass())){
                punto=fact.createMultiPoint(new Point[]{(Point)geometria});
            }else if (MultiPoint.class.isAssignableFrom(geometria.getClass())){
                punto=(MultiPoint)geometria;
            }else{
                return null;
            }
            return punto;
        } catch (Exception ex) {
            throw new RedesException("Error extrayendo puntos de colección",ex);
        }
    }
    
    public static MultiLineString GetMultiLine(Geometry geometria) throws RedesException{
        try {
            if (geometria ==null){
                return null;
            }
            MultiLineString linea;
            GeometryFactory fact=new GeometryFactory(geometria.getPrecisionModel(),geometria.getSRID());
            if (GeometryCollection.class.isAssignableFrom(geometria.getClass())){
                List<LineString> lineas=LineStringExtracter.getLines(geometria);
                linea=fact.createMultiLineString(lineas.toArray(new LineString[0]));
            }else if (LineString.class.isAssignableFrom(geometria.getClass())){
                linea=fact.createMultiLineString(new LineString[]{(LineString)geometria});
            }else if (MultiLineString.class.isAssignableFrom(geometria.getClass())){
                linea=(MultiLineString)geometria;
            }else{
                return null;
            }
            return linea;
        } catch (Exception ex) {
            throw new RedesException("Error extrayendo líneas de colección",ex);
        }
    }
    
    public static MultiPolygon GetMultiPolygon(Geometry geometria) throws RedesException{
        try {
            if (geometria ==null){
                return null;
            }
            MultiPolygon pol;
            GeometryFactory fact=new GeometryFactory(geometria.getPrecisionModel(),geometria.getSRID());
            if (GeometryCollection.class.isAssignableFrom(geometria.getClass())){
                List<LineString> lineas=PolygonExtracter.getPolygons(geometria);
                pol=fact.createMultiPolygon(lineas.toArray(new Polygon[0]));
            }else if (LineString.class.isAssignableFrom(geometria.getClass())){
                pol=fact.createMultiPolygon(new Polygon[]{(Polygon)geometria});
            }else if (MultiPolygon.class.isAssignableFrom(geometria.getClass())){
                pol=(MultiPolygon)geometria;
            }else{
                return null;
            }
            return pol;
        } catch (Exception ex) {
            throw new RedesException("Error extrayendo polígonos de colección",ex);
        }
    }
}
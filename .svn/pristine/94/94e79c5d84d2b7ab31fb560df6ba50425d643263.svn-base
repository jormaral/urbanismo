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


package com.mitc.redes.editorfip.servicios.geometria;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.geotools.data.DataUtilities;
import org.geotools.data.DefaultTransaction;
import org.geotools.data.Transaction;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.simple.SimpleFeatureStore;
import org.geotools.feature.FeatureCollections;
import org.geotools.feature.SchemaException;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.log.Log;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import com.icesoft.faces.context.Resource;
import com.icesoft.faces.context.Resource.Options;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Entidad;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Entidadlin;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Entidadpnt;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Entidadpol;
import com.vividsolutions.jts.geom.GeometryFactory;

@Stateless

@Name("servicioExportacionGeometria")
public class ServicioExportacionGeometriaBean implements ServicioExportacionGeometria {
	
	@PersistenceContext
	private EntityManager em;
	
	@Logger 
	private Log log;
	
	@In(create=true)
    FacesMessages facesMessages;
	
	static final String COLUMNANOMBRE="NOMBRE";
	static final String COLUMNACLAVE="CLAVE";
	static final String IDENTIDAD="IDENTIDAD";
	
	
	
	String urlGeometriaZIP = "";
	boolean geometriaGenerada = false;
	
	
	
	


	

	public void reiniciar() {
		
		urlGeometriaZIP = "";
		geometriaGenerada = false;
		
	}

	public boolean isGeometriaGenerada() {
		return geometriaGenerada;
	}

	public void setGeometriaGenerada(boolean geometriaGenerada) {
		this.geometriaGenerada = geometriaGenerada;
	}

	private void crearRuta(String rutaShape)
	{
		File carpetaInicial = new File (rutaShape); 	
		carpetaInicial.delete();
		
		carpetaInicial = new File (rutaShape+ File.separator);
		carpetaInicial.mkdir();
		log.info("[exportarGeometria] Creada carpeta: "+carpetaInicial.getAbsolutePath());
	}
	
	public void exportarGeometria (int idTramite) throws SchemaException 
	{
		log.info("[exportarGeometria] Exportando del idTramite: "+idTramite);
		
		
		String carpeta = System.getProperty("jboss.home.dir") + File.separator
		+ "var" + File.separator + "exportargeometria" + File.separator  ;
		
		File carpetaI = new File (carpeta); 		
		carpetaI.mkdir();
		
		
		String rutaShape = System.getProperty("jboss.home.dir") + File.separator
		+ "var" + File.separator + "exportargeometria" + File.separator + "geometria_tramite_"+idTramite+File.separator ;
		
		crearRuta(rutaShape);
		
		
		final SimpleFeatureType TYPE_MULTIPOINT = DataUtilities.createType(
                "Location",                   // <- the name for our feature type
                "geom:MultiPoint," + // <- the geometry attribute: Point type
                "IDENTIDAD:Integer, "  + "CLAVE:String, NOMBRE:String"         // <- a String attribute
        );
		
		final SimpleFeatureType TYPE_MULTILINESTRING = DataUtilities.createType(
                "Location",                   // <- the name for our feature type
                "geom:MultiLineString," + // <- the geometry attribute: Point type
                "IDENTIDAD:Integer, "  + "CLAVE:String, NOMBRE:String"         // <- a String attribute
        );
		
				
		final SimpleFeatureType TYPE_MULTIPOLYGON = DataUtilities.createType(
                "Location",                   // <- the name for our feature type
                "geom:MultiPolygon," + // <- the geometry attribute: Point type
                "IDENTIDAD:Integer, "  + "CLAVE:String, NOMBRE:String"         // <- a String attribute
        );
		
		// ----------------
		// Obtengo la geometria Poligonal
		// ----------------
		
		List<Entidadpol> listaRegistrosShapePOL=null;
    	
       	
    	String queryPOL =  "SELECT entpol FROM Entidadpol entpol where entpol.entidad.tramite.iden = "+idTramite;
		
    	 try {
         	
    		 listaRegistrosShapePOL = (List<Entidadpol>) em.createQuery(queryPOL).getResultList();
    		 
    		 
    		 crearShapePOL(TYPE_MULTIPOLYGON,rutaShape,listaRegistrosShapePOL,idTramite);
    		 
           
         } catch (NoResultException e) {
             log.warn("[exportarGeometria] No se han encontrado ninguna entidad con geometria poligonal\n" + e.getMessage());

         } catch (Exception ex) {
             log.error("[exportarGeometria] Error en la busqueda de geometria poligonal \n" + ex.getMessage());
             ex.printStackTrace();

         }
         
         // ----------------
 		// Obtengo la geometria Lineal
 		// ----------------
 		
 		List<Entidadlin> listaRegistrosShapeLIN=null;
     	
        	
     	String queryLIN =  "SELECT entlin FROM Entidadlin entlin where entlin.entidad.tramite.iden = "+idTramite;
 		
     	 try {
          	
     		listaRegistrosShapeLIN = (List<Entidadlin>) em.createQuery(queryLIN).getResultList();
     		 
     		 
     		 crearShapeLIN(TYPE_MULTILINESTRING,rutaShape,listaRegistrosShapeLIN,idTramite);
     		 
            
          } catch (NoResultException e) {
              log.warn("[exportarGeometria] No se han encontrado ninguna entidad con geometria lineal\n" + e.getMessage());

          } catch (Exception ex) {
              log.error("[exportarGeometria] Error en la busqueda de geometria lineal \n" + ex.getMessage());
              ex.printStackTrace();

          }
          
          
          // ----------------
   		// Obtengo la geometria Puntual
   		// ----------------
   		
   		List<Entidadpnt> listaRegistrosShapePUN=null;
       	
          	
       	String queryPUN =  "SELECT entpun FROM Entidadpnt entpun where entpun.entidad.tramite.iden = "+idTramite;
   		
       	 try {
            	
       		listaRegistrosShapePUN = (List<Entidadpnt>) em.createQuery(queryPUN).getResultList();
       		 
       		 
       		 crearShapePUN(TYPE_MULTIPOINT,rutaShape,listaRegistrosShapePUN,idTramite);
       		 
              
            } catch (NoResultException e) {
                log.warn("[exportarGeometria] No se han encontrado ninguna entidad con geometria puntual\n" + e.getMessage());

            } catch (Exception ex) {
                log.error("[exportarGeometria] Error en la busqueda de geometria puntual \n" + ex.getMessage());
                ex.printStackTrace();

            }
         
            // ---------------
            // Comprimimos la geometria
			// ----------------
			ZipUsingJavaUtil zipClass = new ZipUsingJavaUtil();
			
			zipClass.zipFiles(rutaShape, carpeta + "geometria_" + idTramite + ".zip");
			
			urlGeometriaZIP = carpeta + "geometria_" + idTramite + ".zip";
			geometriaGenerada = true;
	
	}
	
	private void crearShapePOL (SimpleFeatureType TYPE, String rutaShape, List<Entidadpol> listaRegistrosShape, int idTramite) throws Exception
	{
		//Exportar a shape los registros
	 	
		 // We create a FeatureCollection into which we will put each Feature created from a record
	        
	        SimpleFeatureCollection collection = FeatureCollections.newCollection();

	        // GeometryFactory will be used to create the geometry attribute of each feature 
	        
	        GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory(null);

	        SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(TYPE);
	        
	        for (Entidadpol geomrec : listaRegistrosShape)
	        {
	        	// Obtengo la Entidad
	        	Entidad entRec =  geomrec.getEntidad();
	        	
	        	
	        	// Recorro el tipo de geometria que no sea nulo
	        	
	        	featureBuilder.add(geomrec.getGeom());
	        		
	        	// Anado la etiqueta "IDENTIDAD:String, "  + "CLAVE:String, NOMBRE:String"
               featureBuilder.add(entRec.getIden());
               
               // Anado el CLAVE
               if (entRec.getClave()==null || entRec.getClave()=="")
               {
            	   featureBuilder.add("");
               }
               else
               {
            	   featureBuilder.add(entRec.getClave()); 
               }
               
               
               // Anado el NOMBRE
               if (entRec.getNombre()==null || entRec.getNombre()=="")
               {
            	   featureBuilder.add("");
               }
               else
               {
            	   featureBuilder.add(entRec.getNombre()); 
               }
               
               
               
               SimpleFeature feature = featureBuilder.buildFeature(null);
               
               // Lo anexo dentro de la coleccion
               collection.add(feature);
	        }
	        
	        
		 
		 
	        // Get an output file name and create the new shapefile
	         
	        File newFile = new File(rutaShape+idTramite+"_pol.shp");

	        ShapefileDataStoreFactory dataStoreFactory = new ShapefileDataStoreFactory();

	        Map<String, Serializable> params = new HashMap<String, Serializable>();
	        params.put("url", newFile.toURI().toURL());
	        params.put("create spatial index", Boolean.TRUE);

	        ShapefileDataStore newDataStore = (ShapefileDataStore) dataStoreFactory.createNewDataStore(params);
	        newDataStore.createSchema(TYPE);

       
	        // Write the features to the shapefile
	        
	        Transaction transaction = new DefaultTransaction("create");

	        String typeName = newDataStore.getTypeNames()[0];
	        SimpleFeatureSource featureSource = newDataStore.getFeatureSource(typeName);

	        if (featureSource instanceof SimpleFeatureStore) {
	        	SimpleFeatureStore featureStore = (SimpleFeatureStore) featureSource;

	            featureStore.setTransaction(transaction);
	            try {
	                featureStore.addFeatures(collection);
	                transaction.commit();

	            } catch (Exception problem) {
	                problem.printStackTrace();
	                transaction.rollback();

	            } finally {
	                transaction.close();
	            }
	           
	        } else {
	        	log.error("[crearShapePOL] "+typeName + " does not support read/write access");
	            
	        }
	    }
	
	
	
	
	private void crearShapeLIN (SimpleFeatureType TYPE, String rutaShape, List<Entidadlin> listaRegistrosShape, int idTramite) throws Exception
	{
		//Exportar a shape los registros
	 	
		 // We create a FeatureCollection into which we will put each Feature created from a record
	        
	        SimpleFeatureCollection collection = FeatureCollections.newCollection();

	        // GeometryFactory will be used to create the geometry attribute of each feature 
	        
	        GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory(null);

	        SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(TYPE);
	        
	        for (Entidadlin geomrec : listaRegistrosShape)
	        {
	        	// Obtengo la Entidad
	        	Entidad entRec =  geomrec.getEntidad();
	        	
	        	
	        	// Recorro el tipo de geometria que no sea nulo
	        	
	        	featureBuilder.add(geomrec.getGeom());
	        		
	        	// Anado la etiqueta "IDENTIDAD:String, "  + "CLAVE:String, NOMBRE:String"
               featureBuilder.add(entRec.getIden());
               
               // Anado el CLAVE
               if (entRec.getClave()==null || entRec.getClave()=="")
               {
            	   featureBuilder.add("");
               }
               else
               {
            	   featureBuilder.add(entRec.getClave()); 
               }
               
               
               // Anado el NOMBRE
               if (entRec.getNombre()==null || entRec.getNombre()=="")
               {
            	   featureBuilder.add("");
               }
               else
               {
            	   featureBuilder.add(entRec.getNombre()); 
               }
               
               
               
               SimpleFeature feature = featureBuilder.buildFeature(null);
               
               // Lo anexo dentro de la coleccion
               collection.add(feature);
	        }
	        
	        
		 
		 
	        // Get an output file name and create the new shapefile
	         
	        File newFile = new File(rutaShape+idTramite+"_lin.shp");

	        ShapefileDataStoreFactory dataStoreFactory = new ShapefileDataStoreFactory();

	        Map<String, Serializable> params = new HashMap<String, Serializable>();
	        params.put("url", newFile.toURI().toURL());
	        params.put("create spatial index", Boolean.TRUE);

	        ShapefileDataStore newDataStore = (ShapefileDataStore) dataStoreFactory.createNewDataStore(params);
	        newDataStore.createSchema(TYPE);

       
	        // Write the features to the shapefile
	        
	        Transaction transaction = new DefaultTransaction("create");

	        String typeName = newDataStore.getTypeNames()[0];
	        SimpleFeatureSource featureSource = newDataStore.getFeatureSource(typeName);

	        if (featureSource instanceof SimpleFeatureStore) {
	        	SimpleFeatureStore featureStore = (SimpleFeatureStore) featureSource;

	            featureStore.setTransaction(transaction);
	            try {
	                featureStore.addFeatures(collection);
	                transaction.commit();

	            } catch (Exception problem) {
	                problem.printStackTrace();
	                transaction.rollback();

	            } finally {
	                transaction.close();
	            }
	           
	        } else {
	        	log.error("[crearShapeLIN] "+typeName + " does not support read/write access");
	            
	        }
	    }
	
	
	private void crearShapePUN (SimpleFeatureType TYPE, String rutaShape, List<Entidadpnt> listaRegistrosShape, int idTramite) throws Exception
	{
		//Exportar a shape los registros
	 	
		 // We create a FeatureCollection into which we will put each Feature created from a record
	        
	        SimpleFeatureCollection collection = FeatureCollections.newCollection();

	        // GeometryFactory will be used to create the geometry attribute of each feature 
	        
	        GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory(null);

	        SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(TYPE);
	        
	        for (Entidadpnt geomrec : listaRegistrosShape)
	        {
	        	// Obtengo la Entidad
	        	Entidad entRec =  geomrec.getEntidad();
	        	
	        	
	        	// Recorro el tipo de geometria que no sea nulo
	        	
	        	featureBuilder.add(geomrec.getGeom());
	        		
	        	// Anado la etiqueta "IDENTIDAD:String, "  + "CLAVE:String, NOMBRE:String"
               featureBuilder.add(entRec.getIden());
               
               // Anado el CLAVE
               if (entRec.getClave()==null || entRec.getClave()=="")
               {
            	   featureBuilder.add("");
               }
               else
               {
            	   featureBuilder.add(entRec.getClave()); 
               }
               
               
               // Anado el NOMBRE
               if (entRec.getNombre()==null || entRec.getNombre()=="")
               {
            	   featureBuilder.add("");
               }
               else
               {
            	   featureBuilder.add(entRec.getNombre()); 
               }
               
               
               
               SimpleFeature feature = featureBuilder.buildFeature(null);
               
               // Lo anexo dentro de la coleccion
               collection.add(feature);
	        }
	        
	        
		 
		 
	        // Get an output file name and create the new shapefile
	         
	        File newFile = new File(rutaShape+idTramite+"_pun.shp");

	        ShapefileDataStoreFactory dataStoreFactory = new ShapefileDataStoreFactory();

	        Map<String, Serializable> params = new HashMap<String, Serializable>();
	        params.put("url", newFile.toURI().toURL());
	        params.put("create spatial index", Boolean.TRUE);

	        ShapefileDataStore newDataStore = (ShapefileDataStore) dataStoreFactory.createNewDataStore(params);
	        newDataStore.createSchema(TYPE);

       
	        // Write the features to the shapefile
	        
	        Transaction transaction = new DefaultTransaction("create");

	        String typeName = newDataStore.getTypeNames()[0];
	        SimpleFeatureSource featureSource = newDataStore.getFeatureSource(typeName);

	        if (featureSource instanceof SimpleFeatureStore) {
	        	SimpleFeatureStore featureStore = (SimpleFeatureStore) featureSource;

	            featureStore.setTransaction(transaction);
	            try {
	                featureStore.addFeatures(collection);
	                transaction.commit();

	            } catch (Exception problem) {
	                problem.printStackTrace();
	                transaction.rollback();

	            } finally {
	                transaction.close();
	            }
	           
	        } else {
	        	log.error("[crearShapePUN] "+typeName + " does not support read/write access");
	            
	        }
	    }
	
	
	public Resource obtenerFichero() {
		
		Resource fichero = null;
		
		if (!urlGeometriaZIP.equalsIgnoreCase(""))
		{
			fichero = new GeometriaURL(urlGeometriaZIP);
		}
		
		
		return fichero;
	}
	
	
	private class GeometriaURL implements Resource, Serializable {

		private String nombre;
		private final Date lastModified;
	    private InputStream inputStream;
		
		public GeometriaURL(String nombre) {
			this.nombre = nombre;
			this.lastModified = new Date();
		}

		@Override
		public String calculateDigest() {
			return nombre;
		}

		@Override
		public Date lastModified() {
			return lastModified;
		}

		@Override
		public InputStream open() throws IOException {
			
			File fichero = new File(nombre);
			
			if(fichero.exists()) {
				inputStream = new FileInputStream(fichero);
			} else {
				inputStream = new FileInputStream(new File("Fichero_Incorrecto.info"));
			}		
			
			return inputStream;
		}

		@Override
		public void withOptions(Options arg0) throws IOException {			
		}
		
	}
	
}

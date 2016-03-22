/*
* Copyright 2011 red.es
* Autores: IDOM Consulting
*
* Licencia con arreglo a la EUPL, Versiï¿½n 1.1 o -en cuanto
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

package com.mitc.redes.editorfip.servicios.administracion.configuracionvisor;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.faces.event.ActionEvent;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.faces.FacesManager;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.international.StatusMessage.Severity;
import org.jboss.seam.log.Log;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;

import com.icesoft.faces.component.inputfile.FileInfo;
import com.icesoft.faces.component.inputfile.InputFile;
import com.mitc.redes.editorfip.entidades.interfazgrafico.DocumentoDTO;
import com.mitc.redes.editorfip.entidades.interfazgrafico.GeometriaDTO;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Entidad;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Entidadlin;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Entidadpnt;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Entidadpol;
import com.mitc.redes.editorfip.servicios.basicos.diccionario.ServicioBasicoAmbitos;
import com.mitc.redes.editorfip.servicios.basicos.fip.entidades.ServicioBasicoEntidades;
import com.mitc.redes.editorfip.servicios.basicos.fip.entidades.ServicioCRUDEntidades;
import com.mitc.redes.editorfip.servicios.genericos.JsfUtil;
import com.mitc.redes.editorfip.servicios.mapas.ServicioCoordenadasAmbito;
import com.mitc.redes.editorfip.servicios.sesion.VariablesSesionGenerales;
import com.mitc.redes.editorfip.servicios.sesion.VariablesSesionUsuario;
import com.mitc.redes.editorfip.utilidades.Constantes;
import com.mitc.redes.editorfip.utilidades.Textos;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKBWriter;
import com.vividsolutions.jts.io.WKTReader;

/**
 * @author FGA
 *
 */
@Scope(ScopeType.SESSION)
@Stateful
@Name("servicioImportacionGeometriaAmbito")
public class ServicioImportacionGeometriaAmbitoBean implements ServicioImportacionGeometriaAmbito {
	
	   
	@Logger 
	private Log log;
	
	@In(create=true)
    FacesMessages facesMessages;
	
	@In(create = true, required = false)
	ServicioBasicoAmbitos servicioBasicoAmbitos;
	
	@In(create = true, required = false)
	ServicioCoordenadasAmbito servicioCoordenadasAmbito;

	
	
	private List<String> listadoErroresImportacion;
	private List<GeometriaDTO> geometriaLista = new ArrayList<GeometriaDTO>();
	List<DocumentoDTO> archivosTemporales = new ArrayList<DocumentoDTO>();
	
	static final String COLUMNACLAVE="CLAVE";
	
	private String nombreShape= ""; //"/Users/franguerreroaranda/Desarrollo/jboss-5.1.0.GA/var/importargeometria/752/ferrol.shp";
	private String nombreFichero = "";
	
	private int idAmbito=0;
	private String wktOrigen="";
	
   

	// Fichero a cargar
    private FileInfo currentFile = null;
    // porcentaje de carga del fichero
    private int fileProgress;
    
    private boolean puedeImportar = false;
    
    private boolean mostrarPanelAmbitos= false;
	
    
    @In(create = true, required = false)
	ServicioCRUDEntidades servicioCRUDEntidades;
    
    @In(create = true, required = false)
	ServicioBasicoEntidades servicioBasicoEntidades;
	
	@PersistenceContext
	EntityManager em;
	
	@In (create = true, required = false)
	VariablesSesionUsuario variablesSesionUsuario;
	
	@In (create = true, required = false)
	VariablesSesionGenerales variablesSesionGenerales;
	
	public ServicioImportacionGeometriaAmbitoBean() {
//		super();
		
		// Creacion del contexto
		listadoErroresImportacion = new ArrayList<String>();
		currentFile = null;
		archivosTemporales = new ArrayList<DocumentoDTO>();
		
		
	}
	
	
		
	public FileInfo getCurrentFile() {
        return currentFile;
    }

    public int getFileProgress() {
        return fileProgress;
    }

    public void uploadActionListener(ActionEvent actionEvent) {

		log.info("[uploadActionListener] Subiendo fichero ...");
		
		// Datos del fichero recien subido
        InputFile inputFile = (InputFile) actionEvent.getSource();
        currentFile = inputFile.getFileInfo();
        String clientSideFileName = currentFile.getFileName();
        log.debug("[uploadActionListener] Fichero subido: " + clientSideFileName);
        File uploadedFile = currentFile.getFile();
		
		if(Textos.validarExpresion(Constantes.EXPRESION_REGULAR_NOMBRE_ARCHIVO, clientSideFileName)) {
			log.debug("[uploadActionListener] Fichero subido: " + clientSideFileName);
			
			
		    // Directorio donde se almacenaran los shapes

            String dir = System.getProperty("jboss.home.dir") + File.separator + 
                "var" + File.separator + "importargeometria"+
                File.separator + variablesSesionUsuario.getIdTramiteEncargadoTrabajo();

            log.debug("[uploadActionListener] Directorio de la Importacion Geometria: " + dir);
            String nombreFicheroSubido = uploadedFile.getName();
            log.debug("[uploadActionListener] Nombre Geometria: " +nombreFicheroSubido );

            
            // Borramos los archivos relacionados si existen
            String[] ficheroSubido = nombreFicheroSubido.split("\\.");
            String nombreBase= ficheroSubido[0];
            
            if(archivosTemporales==null || archivosTemporales.size()==0)
            {
	            File oldFile = new File(dir, nombreBase + ".shp");
	    		oldFile.delete();
	    		
	    		oldFile = new File(dir, nombreBase + ".shx");
	    		oldFile.delete();
	    		
	    		oldFile = new File(dir, nombreBase + ".dbf");
	    		oldFile.delete();
            }
            
            File directorio = new File(dir);
            directorio.mkdirs();

            
            File newFile = new File(dir, nombreFicheroSubido);

            
            // Mover el fichero subido al sitio donde estar‡n todos los ficheros fips1
           uploadedFile.renameTo(newFile);
            log.debug("[uploadActionListener] Fichero movido correctamente a " + dir);

            

            // Si el fichero es SHP lo guardo en la variable
            String extensionFicheroSubido="";
       

            String[] ficheroSubidoSplit = nombreFicheroSubido.split("\\.");
            extensionFicheroSubido= ficheroSubidoSplit[1];

            log.debug("[uploadActionListener] extension="+extensionFicheroSubido);

            if (extensionFicheroSubido.equalsIgnoreCase("shp") 
            		|| extensionFicheroSubido.equalsIgnoreCase("dbf")
            		|| extensionFicheroSubido.equalsIgnoreCase("shx"))

            {
            	if (extensionFicheroSubido.equalsIgnoreCase("shp"))
            			nombreShape = newFile.getAbsolutePath();
                nombreFichero = newFile.getName().substring(0, newFile.getName().length()-3);
                log.debug("[uploadActionListener] nombreShape="+nombreShape);
                
                // TODO Habria que comprobar si tambien ha subido el dbf y el shx
                puedeImportar = true;

            }
	        
            leerArchivos();
	        
	        
		} else {
			facesMessages.addFromResourceBundle(Severity.ERROR,"El nombre del fichero no es valido", null);
		}
	}

    
   
    public void progressListener(EventObject event) {
        InputFile ifile = (InputFile) event.getSource();
        fileProgress = ifile.getFileInfo().getPercent();
    }

    

    public void guardarGeometriaAmbito()
    {
    	if (idAmbito!=0 && wktOrigen!="")
    	{
    		log.debug("[guardarGeometriaAmbito] idAmbito:"+idAmbito +"wktOrigen="+wktOrigen);
    		
    		
    		
    		
    		
    		
    		WKTReader geomWKT = new WKTReader();
    		String geomWKB=null;
    		try {
    			Geometry geom = geomWKT.read(wktOrigen);
    			
    			WKBWriter wkbWriter = new WKBWriter();
    			byte[] geomWKBByte = wkbWriter.write(geom);
    			geomWKB = WKBWriter.bytesToHex(geomWKBByte);
    			log.debug("[guardarGeometriaAmbito] geomWKB="+geomWKB);
    			
    			//servicioBasicoAmbitos.guardarGeomtriaAmbitosShp(idAmbito, geomWKB);
    			servicioBasicoAmbitos.guardarGeomtriaAmbitosShp(idAmbito, wktOrigen);
    			
    			// Ahora lo meto en la tabla Coordenadas
    			servicioCoordenadasAmbito.meterAmbitoEnTabla(idAmbito);
       		 	facesMessages.addFromResourceBundle(Severity.INFO,"Se ha insertado el ambito correctamente en AmbitoSHP y en CoordenadasAmbito");
    			
    			
    			
    		} catch (ParseException e1) {
    			// TODO Auto-generated catch block
    			e1.printStackTrace();
    			facesMessages.addFromResourceBundle(Severity.ERROR,"No es un WKT correcto");
    		} 
    		
    		
    	}
    	else
    	{
    		log.debug("[guardarGeometriaAmbito] ELSE idAmbito:"+idAmbito +"wktOrigen="+wktOrigen);
    		 // Informo del total de registros insertados
	        facesMessages.addFromResourceBundle(Severity.ERROR,"No se ha insertado el ambito");

    	}
    }
	
		
		
	public void importarGeometria () 
	{
		log.debug("[importarGeometria] Importando de SHP:"+nombreShape);
		// limpio la lista de errores
		listadoErroresImportacion.clear();
		
		//pase lo que pase, ya no va a poder a volver a insertar ese shape a menos que lo cargue de nuevo (se desactiva el boton)
		puedeImportar = false;
		
		if (nombreShape!=null)
		{
			// shapefile loader
            Map<Object,Serializable> shapeParams = new HashMap<Object,Serializable>();
            File shape = new File(nombreShape);
       
            try {
				shapeParams.put("url", shape.toURI().toURL());
				
				DataStore shapeDataStore = DataStoreFinder.getDataStore(shapeParams);
				
				String typeName = shapeDataStore.getTypeNames()[0];
		        SimpleFeatureSource source = shapeDataStore.getFeatureSource(typeName);

		        SimpleFeatureCollection features = source.getFeatures();
		        
		        
		        SimpleFeatureType sft = features.getSchema();
		        
		        List<AttributeDescriptor> atribList = sft.getAttributeDescriptors();
		        
		        
		        boolean existeColumnaClave = false;
		        for (AttributeDescriptor at : atribList)
		        {
		            if (at.getLocalName().equalsIgnoreCase(COLUMNACLAVE))
		            {
		                existeColumnaClave = true;
		            }
		        }
		        
		        if (existeColumnaClave)
		        {
		        	SimpleFeatureIterator itr =features.features();
			        
		        	// Almaceno en la lista de geometrias los campos del shape
			        
			        while (itr.hasNext())
			        {
			            SimpleFeature f = itr.next();
			            
			            GeometriaDTO geomet = new GeometriaDTO();
			            
			            geomet.setIdentificador(f.getID());
			            geomet.setClave((String)f.getAttribute(COLUMNACLAVE));
			            geomet.setGeometriaWKT((String)f.getDefaultGeometry().toString());
			           
			            geometriaLista.add(geomet);
			        } 
			        
			        // Tengo que recorrer la lista e ir buscando la entidad y si existe se inserta
			        int registrosSHPTotales= geometriaLista.size();
			        
			        int registrosInsertados=0;
			        
			        for (GeometriaDTO g : geometriaLista)
			        {
			        	int idEntidad = servicioBasicoEntidades.buscarEntidadPorTramiteYClave(variablesSesionUsuario.getIdTramiteEncargadoTrabajo(), g.getClave());
			        	
			        	if (idEntidad!=0)
			        	{
			        		// Se ha encontrado la entidad, paso a borrar la geometria y a–adir la geometria nueva
			        		//borro
			        		servicioCRUDEntidades.borrarGeometriaDeEntidad(idEntidad);
			        		guardarGeometriaEntidad(g.getGeometriaWKT(),idEntidad);
			        		registrosInsertados++;
			        		
			        		log.debug("[importarGeometria] Borrada e insertada el registro del SHP:"+g.getIdentificador()+" en la entidad="+idEntidad);
			        		
			        		
			        	}
			        	else
			        	{
			        		// No se ha encontrado la entidad con esa clave en ese tramite, no se inserta esa geometria
			        		listadoErroresImportacion.add("El registro del SHP con id="+g.getIdentificador()+" y clave="+g.getClave()+" no tiene " +
			        				" entidad con igual clave en el tramite encargado. No se insertar‡ la geometria");
			        		
			        	}
			        }
			        
			        if (registrosSHPTotales==registrosInsertados)
			        {
			        	 // Informo del total de registros insertados
				        facesMessages.addFromResourceBundle(Severity.INFO,"Insertados todos los registros del SHP. total de "+registrosSHPTotales, null);
				       
			        }
			        else
			        {
			        	 // Informo del total de registros insertados
			        	listadoErroresImportacion.add("Insertados "+registrosInsertados+" registros de un total de "+registrosSHPTotales);
				        facesMessages.addFromResourceBundle(Severity.WARN,"Insertados "+registrosInsertados+" registros de un total de "+registrosSHPTotales, null);
				       	
			        }
			        
			        
		        }
		        else
		        {
		        	log.info("[importarGeometria] El Shape no tiene campo 'clave'");
		        	facesMessages.addFromResourceBundle(Severity.ERROR,"El Shape no tiene campo 'clave'", null);
		        }
		        
		        
		                 
				
			} catch (MalformedURLException e) {
				facesMessages.addFromResourceBundle(Severity.ERROR,"Error al cargar el shape", null);
				e.printStackTrace();
			} catch (IOException e) {
				facesMessages.addFromResourceBundle(Severity.ERROR,"Error al cargar el shape", null);
				e.printStackTrace();
			}
			catch (Exception e) {
				
				e.printStackTrace();
				facesMessages.addFromResourceBundle(Severity.ERROR,"Error al cargar el shape", null);
			}
            
              
            
		}
		else
		{
			facesMessages.addFromResourceBundle(Severity.ERROR,"No se ha subido ningun Shape", null);
		}
	}

	
	public void importarGeometriaToEntidad () 
	{
		log.debug("[importarGeometria] Importando de SHP:"+nombreShape);
		// limpio la lista de errores
		listadoErroresImportacion.clear();
		
		//pase lo que pase, ya no va a poder a volver a insertar ese shape a menos que lo cargue de nuevo (se desactiva el boton)
		puedeImportar = false;
		
		if (nombreShape!=null)
		{
			// shapefile loader
            Map<Object,Serializable> shapeParams = new HashMap<Object,Serializable>();
            File shape = new File(nombreShape);
       
            try {
				shapeParams.put("url", shape.toURI().toURL());
				
				DataStore shapeDataStore = DataStoreFinder.getDataStore(shapeParams);
				
				String typeName = shapeDataStore.getTypeNames()[0];
		        SimpleFeatureSource source = shapeDataStore.getFeatureSource(typeName);

		        SimpleFeatureCollection features = source.getFeatures();
		        
		        
		        SimpleFeatureType sft = features.getSchema();
		        
		        List<AttributeDescriptor> atribList = sft.getAttributeDescriptors();
		        
		        
		        boolean existeColumnaClave = false;
		        for (AttributeDescriptor at : atribList)
		        {
		            if (at.getLocalName().equalsIgnoreCase(COLUMNACLAVE))
		            {
		                existeColumnaClave = true;
		            }
		        }
		        
		        if (existeColumnaClave)
		        {
		        	SimpleFeatureIterator itr =features.features();
			        
		        	// Almaceno en la lista de geometrias los campos del shape
		  
			        while (itr.hasNext())
			        {
			            SimpleFeature f = itr.next();
			            
			            GeometriaDTO geomet = new GeometriaDTO();
			            
			            geomet.setIdentificador(f.getID());
			            geomet.setClave((String)f.getAttribute(COLUMNACLAVE));
			            geomet.setGeometriaWKT((String)f.getDefaultGeometry().toString());
			           
			            geometriaLista.add(geomet);
			        } 
			        
			        // Tengo que recorrer la lista e ir buscando la entidad y si existe se inserta
			        int registrosSHPTotales= geometriaLista.size();
			        
			        //variablesSesionGenerales.setGeometriaWktSeleccionada(geometriaLista.get(0).getGeometriaWKT());
			        
			        
			        
			        int registrosInsertados=0;
			        
			        
		        }
		        else
		        {
		        	log.info("[importarGeometria] El Shape no tiene campo 'clave'");
		        	facesMessages.addFromResourceBundle(Severity.ERROR,"El Shape no tiene campo 'clave'", null);
		        }
		        
		        
		                 
				
			} catch (MalformedURLException e) {
				facesMessages.addFromResourceBundle(Severity.ERROR,"Error al cargar el shape", null);
				e.printStackTrace();
			} catch (IOException e) {
				facesMessages.addFromResourceBundle(Severity.ERROR,"Error al cargar el shape", null);
				e.printStackTrace();
			}
			catch (Exception e) {
				
				e.printStackTrace();
				facesMessages.addFromResourceBundle(Severity.ERROR,"Error al cargar el shape", null);
			}
            
              
            
		}
		else
		{
			facesMessages.addFromResourceBundle(Severity.ERROR,"No se ha subido ningun Shape", null);
		}
	}
	
	
	public boolean isPuedeImportar() {
		return puedeImportar;
	}



	private void guardarGeometriaEntidad(String geometriaWKT, int idEntidad) {
		try {
			
			// ------------------------
			// Guardo la geometria si se ha metido alguna geometria
			// ------------------------
			log.debug("[guardarGeometriaEntidad]  Inicio idEntidad="+idEntidad );
			
			Entidad entidad = em.find(Entidad.class, idEntidad);
			
			
			if (geometriaWKT!="")
			{
				WKTReader reader = new WKTReader();
		        try {
		            Geometry geoWKT = reader.read(geometriaWKT);
		            geoWKT.isValid();
		            
		            log.debug("[guardarGeometriaEntidad]  geometria valida " );
		            if (geometriaWKT.contains("MULTIPOLYGON") == true || geometriaWKT.contains("POLYGON") == true) {
						// Creo una nuevo entidadPOL
						Entidadpol entPOL_rmpAnadir = new Entidadpol();
						entPOL_rmpAnadir.setEntidad(entidad);
						entPOL_rmpAnadir.setGeom(((MultiPolygon)geoWKT));
						
						em.persist(entPOL_rmpAnadir);
						//servicioCRUDEntidades.crearGeometriaPoligono(entPOL_rmpAnadir);

						log.info("[guardarGeometriaEntidad]- EntidadPOL CREADA" );
						JsfUtil.addSuccessMessage("Geometria poligonal de la Entidad Guardada Correctamente");
					}

					if (geometriaWKT.contains("LINESTRING") == true || geometriaWKT.contains("MULTILINESTRING") == true) {
						// Creo una nuevo entidadLIN
						Entidadlin entLIN_rmpAnadir = new Entidadlin();

						entLIN_rmpAnadir.setEntidad(entidad);
						entLIN_rmpAnadir.setGeom((MultiLineString)geoWKT);

						em.persist(entLIN_rmpAnadir);
						//servicioCRUDEntidades.crearGeometriaLineal(entLIN_rmpAnadir);


						log.info("[guardarGeometriaEntidad]- EntidadLIN CREADA");
						JsfUtil.addSuccessMessage("Geometria lineal de la Entidad Guardada Correctamente");
					}

					if (geometriaWKT.contains("POINT") == true || geometriaWKT.contains("MULTIPOINT") == true) {
						// Creo una nuevo entidadPNT
						Entidadpnt entPNT_rmpAnadir = new Entidadpnt();

						entPNT_rmpAnadir.setEntidad(entidad);
						entPNT_rmpAnadir.setGeom((MultiPoint)geoWKT);

						em.persist(entPNT_rmpAnadir);
						//servicioCRUDEntidades.crearGeometriaPuntual(entPNT_rmpAnadir);

						log.info("[guardarGeometriaEntidad] - EntidadPNT CREADA " );
						JsfUtil.addSuccessMessage("Geometria puntual de la Entidad Guardada Correctamente");
					}
		           
		            geometriaWKT="";

		            
		        } 
		        catch (Exception ex) {
		        	log.error("[validacionFormatoWKT] Se ha producido un error guardando el dato geometrico; " + ex, ex);
		            JsfUtil.addErrorMessage("La geometria introducida no es valida. No se ha guardado la entidad");
		           
		        }
			}
			

		}

		catch (Exception e) {
			JsfUtil.addErrorMessage("Error guardando la geometria de la entidad.");
			e.printStackTrace();
		}

	}
	
	public List<String> getListadoErroresImportacion() {
		return listadoErroresImportacion;
	}
	
	
	

	public List<GeometriaDTO> getGeometriaLista() {
		return geometriaLista;
	}

	
	public void setGeometriaLista(List<GeometriaDTO> geometriaLista) {
		this.geometriaLista = geometriaLista;
	}



	public List<DocumentoDTO> getArchivosTemporales() {
		return archivosTemporales;
	}

	

	public void setArchivosTemporales(List<DocumentoDTO> archivosTemporales) {
		this.archivosTemporales = archivosTemporales;
	}
	
	
	public boolean archivosValidos()
	{
		boolean validoShp = false;
		boolean validoDbf = false;
		boolean validoShx = false;
		
		if (archivosTemporales!=null){
			if (archivosTemporales.size()>=3){
				
				for(DocumentoDTO documento : archivosTemporales)
				{
					// Caso JPG
					String extension = documento.getArchivo().substring(documento.getArchivo().length()-3, documento.getArchivo().length());
					if (extension.equalsIgnoreCase("shp")){
						validoShp = true;
					}
					else if (extension.equalsIgnoreCase("dbf")){
						validoDbf = true;
					}
					else if (extension.equalsIgnoreCase("shx")){
						validoShx = true;
					}
					
				}
			}
			if (!validoShx || !validoDbf || !validoShp)
				facesMessages.addFromResourceBundle(Severity.ERROR,"Debe contener tres documentos válidos. SHP, SHX y DBF", null);
		}
		
		return (validoShx && validoDbf && validoShp);
	}
	
	public boolean validarArchivo(String extension)
	{
		boolean valido = false;
		if (extension.equalsIgnoreCase("shp")){
			valido = true;
		}
		else if (extension.equalsIgnoreCase("dbf")){
			valido = true;
		}
		else if (extension.equalsIgnoreCase("shx")){
			valido = true;
		}
		
		return valido;
	}
	
	private void leerArchivos()
	{
		
		
		String dir = System.getProperty("jboss.home.dir") + File.separator + 
        "var" + File.separator + "importargeometria"+
        File.separator + variablesSesionUsuario.getIdTramiteEncargadoTrabajo();
		
		log.info(dir);
		
		archivosTemporales = new ArrayList<DocumentoDTO>();
		
		File directorio = new File(dir);
		String [] ficheros = directorio.list();
		if (ficheros!=null){
			for (int i = 0; i < ficheros.length; i++) {
		        log.info("Contenido del fichero " + ficheros[i]);
		        String[] ficheroSubido = ficheros[i].split("\\.");
	            String nombreBase= ficheroSubido[0] + ".";
		        if (!nombreFichero.equals("") && nombreBase.equals(nombreFichero)){
			        DocumentoDTO documento = new DocumentoDTO();
			        documento.setArchivo(ficheros[i]);
			        
			        archivosTemporales.add(documento);
		        }
			}
		}
		
	}
	
	

	public String getNombreFichero() {
		return nombreFichero;
	}



	public void setNombreFichero(String nombreFichero) {
		this.nombreFichero = nombreFichero;
	}

	public void reiniciarElementos(){
		
		nombreFichero = "";
		nombreShape = "";
		archivosTemporales = new ArrayList<DocumentoDTO>();
		listadoErroresImportacion = new ArrayList<String>();
		currentFile = null;
		FacesManager.instance().redirect("/gestionfip/importadores/GeometriasEntidades.xhtml");
	}
	
	public boolean isMostrarPanelAmbitos() {
		return mostrarPanelAmbitos;
	}

	public void setMostrarPanelAmbitos(boolean mostrarPanelAmbitos) {
		this.mostrarPanelAmbitos = mostrarPanelAmbitos;
	}
	
	public void mostrarPanelAmbitosAL(ActionEvent actionEvent) {
		mostrarPanelAmbitos = true;
	}
	
	 public int getIdAmbito() {
			return idAmbito;
		}



		public void setIdAmbito(int idAmbito) {
			this.idAmbito = idAmbito;
			
			setMostrarPanelAmbitos(false);
			
		}



		public String getWktOrigen() {
			return wktOrigen;
		}



		public void setWktOrigen(String wktOrigen) {
			this.wktOrigen = wktOrigen;
		}

	@Remove
	public void destroy(){};
}


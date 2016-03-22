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

package com.mitc.redes.editorfip.servicios.procesamientofip.obtencionfip1;

import java.nio.charset.Charset;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import org.jboss.seam.Component;
import org.jboss.seam.annotations.In;
import org.jboss.seam.log.Log;
import org.jboss.seam.log.Logging;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Ambitoaplicacionambito;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Determinacion;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Documento;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Documentocaso;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Documentoentidad;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Entidad;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Entidadlin;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Entidadpnt;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Entidadpol;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Plan;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Propiedadrelacion;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Relacion;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Tramite;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Vectorrelacion;
import com.mitc.redes.editorfip.excepciones.EditorFIPException;
import com.mitc.redes.editorfip.servicios.gestionfip.obtencionfip.LogImportacion;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKBReader;
import com.vividsolutions.jts.io.WKBWriter;
import com.vividsolutions.jts.io.WKTReader;

public class ImportadorTramite extends ImportadorBase {

	Log log = Logging.getLog(ImportadorTramite.class);
	
	@In (create = true, required = false)
	LogImportacion logImportacion;
	
	public Tramite tramite;
	public String xmlPath = "";
	public Plan plan;
	private Resultado resultado;
	
	// Contadores
	private int countEntidades = 0;
	
	// Mapas
	Map<String,Determinacion> mapDeterminacion = new HashMap<String,Determinacion>();
	
	/*
	 * Constructor
	 */
	public ImportadorTramite(Document xml, EntityManager em, Resultado resultado) {
		super(xml, em);
		this.resultado = resultado;
	}
	
	/*
	 *  IMPORTAR
	 * 
	 * (non-Javadoc)
	 * @see com.mitc.redes.editorfip.servicios.gestionfip.obtencionfip1.ImportadorBase#importar()
	 */
	@Override
	public void importar(Date fechaPV, String nombreTramite) throws EditorFIPException {
				
		
		logImportacion = (LogImportacion) Component.getInstance("logImportacion", true);
		
		// Coger datos del trámite
		NodeList nodes = evaluateXPath(xmlPath);
		
		// Si tenemos algún trámite ...
		if (nodes.getLength()>0) {
			
			// Mapeamos atributos del nodo
	    	attrMap = nodeToMap( nodes.item(0) );
	    	
	    	// Cogemos el texto del trámite
			NodeList n = evaluateXPath(xmlPath+"/TEXT");
			String texto = "";
			if (n.getLength()>0) texto = n.item(0).getNodeValue(); 
		 
			// Crear trámite
			// Si ya tenemos un trámite no lo creamos
			if (tramite == null) {
				tramite = new Tramite();
				tramite.setPlan(plan);
				tramite.setTexto(texto);
				tramite.setFecha(fechaPV);
				tramite.setFechaconsolidacion(new Date());
				tramite.setNombre(nombreTramite);
				int idTipoTramite = Integer.parseInt( (String) attrMap.get("tipoTramite"));
				tramite.setIdtipotramite(idTipoTramite);
				tramite.setCodigofip((String) attrMap.get("codigo"));
				log.info("Persistiendo TRAMITE ...");
				
				logImportacion.anadirLinea("Persistiendo TRAMITE ...");
				
				
				em.persist(tramite);
			}
			
			// -----| DOCUMENTOS |-----
			importarDocumentos();
			
			// -----| ENTIDADES |-----
			importarEntidades();
			
			// -----| DETERMINACIONES |-----
			ImportadorDeterminaciones importadorDet = new ImportadorDeterminaciones(xmlDoc, em, xmlPath, tramite, resultado);
			importadorDet.importar();
			
			// -----| CONDICIONES URBANISTICAS |-----
			ImportadorCondUrbanisticas importadorCondUrb = new ImportadorCondUrbanisticas(xmlDoc, em, xmlPath, tramite, resultado);
			importadorCondUrb.importar();
			
			// -----| OPERACIONES |-----
			// No hay operaciones en el FIP1
			
			// -----| APLICACION AMBITOS |-----
			importarAplicacionAmbitos();
			
			// -----| ADSCRIPCIONES |-----
			importarAdscripciones();
			
		}
	}
	
	/* 
	 *  IMPORTAR DOCUMENTOS
	 *  
	 */
	private void importarDocumentos() throws EditorFIPException  {
		
		// Coger los nodos tipo-ambito
		NodeList nodes = evaluateXPath(xmlPath + "/DOCUMENTOS/DOCUMENTO-REFUNDIDO");	
		log.info("Importando "+nodes.getLength()+" DOCUMENTO-REFUNDIDO ...");
		
		logImportacion.anadirLinea("Importando "+nodes.getLength()+" DOCUMENTO-REFUNDIDO ...");
		
		// Vars
		int contador=0;
		Node node;
		Documento documento;
		String codigo=null;
		
		// Por cada nodo ...
	    for (int i = 0; i < nodes.getLength(); i++) {
	    	node = nodes.item(i);
	    		
	    	try {
	    		
		    	// Mapeamos atributos del nodo
		    	attrMap = nodeToMap( node );
	
		    	// Nuevo documento
		    	documento = new Documento();
		    	codigo = (String) attrMap.get("codigo");
		    	documento.setNombre(codigo);
		    	documento.setArchivo(codigo);
		    	documento.setTramite(tramite);
		    	
		    	Integer grupo = Integer.parseInt((String) attrMap.get("grupo"));
		    	Integer tipo = Integer.parseInt((String) attrMap.get("tipo"));
		    	
		    	documento.setIdgrupodocumento(grupo);
		    	documento.setIdtipodocumento(tipo);
		    	
		    	Utiles.mapDocumento.put(tramite.getCodigofip()+"/"+codigo, documento);
	
		    	// Persistiendo DOCUMENTO
		    	log.info("Persistiedo DOCUMENTO, codigo: "+codigo);
		    	
		    	logImportacion.anadirLinea("Persistiedo DOCUMENTO, codigo: "+codigo);
		    	
	    		em.persist(documento);
	    		
	    	} catch (Exception e) {
	    		resultado.error("Error persistiendo DOCUMENTO-REFUNDIDO con codigo: "+codigo+", error: "+e.getMessage());
	    		
	    		logImportacion.anadirLinea("Error persistiendo DOCUMENTO-REFUNDIDO con codigo: "+codigo+", error: "+e.getMessage());
	    		
	    	}
	    }
	    
	    resultado.contador.put(xmlPath + "/DOCUMENTOS/DOCUMENTO-REFUNDIDO", contador);
	    resultado.info("TRAMITE "+tramite.getCodigofip()+", DOCUMENTO-REFUNDIDO: " + contador);
	    
	    logImportacion.anadirLinea("TRAMITE "+tramite.getCodigofip()+", DOCUMENTO-REFUNDIDO: " + contador);
	}	
	

	/* 
	 *  IMPORTAR ENTIDADES
	 *  
	 */
	private void importarEntidades() throws EditorFIPException  {
		
		log.info("Importando etiquetas de ENTIDAD ...");
		logImportacion.anadirLinea("Importando etiquetas de ENTIDAD ...");
		
		NodeList nodesEnt = evaluateXPath(xmlPath + "/" + "ENTIDADES/ENTIDAD");	
		int i = 0;
		
		// ---| Llamada recursiva |-------------------
		_importarEntidades(nodesEnt, null);
		resultado.contador.put(xmlPath + "/" + "ENTIDADES/ENTIDAD", countEntidades);
		resultado.info("TRAMITE "+tramite.getCodigofip()+", ENTIDADES: " + countEntidades);
		logImportacion.anadirLinea("TRAMITE "+tramite.getCodigofip()+", ENTIDADES: " + countEntidades);
		// -------------------------------------------
		
		//
		// Ahora tenemos que enganchar cada entidad con su base
	    //
		Iterator<?> itr = Utiles.mapEntidad.entrySet().iterator();
		Map<String,Entidad> mapEntBase = new HashMap<String,Entidad>();
	    while(itr.hasNext()) {
	    	
	    	// Coger la entidad
	    	@SuppressWarnings("unchecked")
			Map.Entry<String,Entidad> entry = (Map.Entry<String, Entidad>) itr.next();
	    	Entidad ent = entry.getValue();
	    	String codigoComp = ent.getBaseTramite()+"/"+ent.getBaseEntidad();
	    	
	    	if (ent.getBaseEntidad()!=null) {
	    	
		    	// Cogemos de la BD la entidad Base
	    		if (mapEntBase.get(codigoComp)==null) {
	    			
			    	List<?> result = em.createQuery(
							"select e from Entidad e, Tramite t "
							+ "where e.tramite=t.iden "
							+ "and e.codigo = '" + ent.getBaseEntidad() + "' "
							+ "and t.codigofip = '" + ent.getBaseTramite() + "'")
							.setMaxResults(1).getResultList();
			  
			    	// Si está vacio entonces hay un error en el FIP1
					if (result.isEmpty()) {
						throw new EditorFIPException("No existe la entidad base tramite/entidad (" + ent.getBaseTramite() + "/" + ent.getBaseEntidad() + ")");
					} else {
						ent.setEntidadByIdentidadbase((Entidad) result.get(0));
						mapEntBase.put(codigoComp, ent);
					}
	    		} else {
	    			
	    			ent = mapEntBase.get(codigoComp);
	    		}
				
				// Persistir la entidad pero ya con su base
		    	try {
		    		log.trace("Persistiendo ENTIDAD con BASE "+codigoComp+", código: " + ent.getCodigo());
		    		logImportacion.anadirLinea("Persistiendo ENTIDAD con BASE "+codigoComp+", código: " + ent.getCodigo());
		    		em.persist(ent);
		    		i++;
		    	} catch (Exception e2) {
		    		resultado.error("Error desconocido persistiendo ENTIDAD con codigo: "+codigoComp+", error: "+e2.getMessage());
		    		logImportacion.anadirLinea("Error desconocido persistiendo ENTIDAD con codigo: "+codigoComp+", error: "+e2.getMessage());
		    	}
	    	}
	  	}
	    log.trace("Persistidas ENTIDAD con BASE: "+i);
	    logImportacion.anadirLinea("Persistidas ENTIDAD con BASE: "+i);
	}
	// --------------------------------------------------
	// Función recursiva
	private void _importarEntidades(NodeList nodes, Entidad padre) throws EditorFIPException  {
		
		// Vars
		Node node=null;
		Entidad entidad=null;
		NodeList children;
		String codigo = null;
		boolean suspendida=false;
		
		// Por cada nodo ...
	    for (int i = 0; i < nodes.getLength(); i++) {
	    	
	    	try {
	    		
		    	node = nodes.item(i);
		    	if (node.getNodeName().compareTo("ENTIDAD")!=0) continue;
		    		    	
		    	// Mapeamos atributos del nodo
		    	attrMap = nodeToMap( node );
	
		    	// Nueva entidad
		    	entidad = new Entidad();
		    	codigo = (String) attrMap.get("codigo");
		    	entidad.setCodigo(codigo);
		    	entidad.setClave((String) attrMap.get("clave"));
		    	entidad.setNombre((String) attrMap.get("nombre"));
		    	entidad.setEtiqueta((String) attrMap.get("etiqueta"));
		    	suspendida = ((String) attrMap.get("suspendida")).compareTo("true") == 0;
		    	entidad.setBsuspendida(suspendida);
		    	entidad.setTramite(tramite);
		    	if (padre!=null) entidad.setEntidadByIdpadre(padre);
		    	Utiles.mapEntidad.put(tramite.getCodigofip()+"/"+codigo, entidad);
		    	
		    	// Persistiendo ENTIDAD
		    	log.info("Persistiendo ENTIDAD, código: " + codigo);
		    	logImportacion.anadirLinea("Persistiendo ENTIDAD, código: " + codigo);
	    		em.persist(entidad);
	    		countEntidades++;
	    		
	    	} catch (Exception e) {
	    		resultado.error("Error desconocido persistiendo ENTIDAD con codigo: "+codigo+", error: "+e.getMessage());
	    		logImportacion.anadirLinea("Error desconocido persistiendo ENTIDAD con codigo: "+codigo+", error: "+e.getMessage());
	    		entidad=null;
	    	}
	    	
	    	// Solo procesamos los hijos si hemos conseguido persistir la entidad    	
	    	if (entidad != null) {
	    	
		    	children = node.getChildNodes();
		    	for(int k=0;k<children.getLength();k++) {
	
		    		// DOCUMENTOS
		    		if (children.item(k).getNodeName().compareTo("DOCUMENTOS")==0) { 
		    			
		    			for(int n=0;n<children.item(k).getChildNodes().getLength();n++) {
		    				Node nn = children.item(k).getChildNodes().item(n);
		    				if (nn.getNodeName().compareTo("DOCUMENTO")==0) {
			    				String codDoc = (String) nn.getAttributes().item(0).getNodeValue();
			    				
			    				
			    				
			    				// Coger documento
								Documento doc = Utiles.getDocumento(tramite.getCodigofip(), codDoc);
								
								if (doc!=null)
								{
									// Nuevo objeto documento de entidad
									Documentoentidad docEntidad = new Documentoentidad();
				    				docEntidad.setEntidad(entidad);
				    				docEntidad.setDocumento(doc);

									// Persistir
				    		    	try {
				    		    		log.info("Persistiendo DOCUMENTO en ENTIDAD, código doc: " + codigo);
				    		    		logImportacion.anadirLinea("Persistiendo DOCUMENTO en ENTIDAD, código doc: " + codigo);
				    		    		em.persist(docEntidad);
				    		    	} catch (Exception e) {
				    		    		resultado.error("Error persistiendo DOCUMENTO en ENTIDAD con codigo doc: "+codDoc);
				    		    		logImportacion.anadirLinea("Error persistiendo DOCUMENTO en ENTIDAD con codigo doc: "+codDoc);
				    		    	} 
								}
								else
								{
									// No ha encontrado el documento. Informo de este hecho. El FIP.XML es erroneo
									resultado.error("Error persistiendo DOCUMENTO en ENTIDAD con codigo doc: " + codDoc +" No existe dicho documento en el FIP.xml y no se ha asociado");
									logImportacion.anadirLinea("Error persistiendo DOCUMENTO en ENTIDAD con codigo doc: " + codDoc +" No existe dicho documento en el FIP.xml y no se ha asociado");
								}
			    				
			    				
		    				}
		    			}
		    		}
		    		
		    		// GEOMETRIA
		    		if (children.item(k).getNodeName().compareTo("GEOMETRIA")==0) { 
		    			
		    			String value = children.item(k).getFirstChild().getNodeValue();
		    			if (value!=null && value.length()> 6) {
		    				String value2 = value.substring(0,6).toUpperCase();
		    				if ((value2.compareTo("POLYGO")==0) || (value2.compareTo("MULTIP")==0)) {
		    					
		    					Entidadpol geoEnt = new Entidadpol();
		    					geoEnt.setEntidad(entidad);
		    					geoEnt.setBsuspendida(suspendida);
		    					
		    					
		    					WKTReader geomWKT = new WKTReader();
		    					String geomWKB=null;
		    					try {
									Geometry geom = geomWKT.read(value);
									
									WKBWriter wkbWriter = new WKBWriter();
									byte[] geomWKBByte = wkbWriter.write(geom);
									geomWKB = WKBWriter.bytesToHex(geomWKBByte);
									geoEnt.setGeom(geomWKB);
									
								} catch (ParseException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
		    					
		    					
		    					// Persistir
			    		    	try {
			    		    		em.persist(geoEnt);
			    		    		
			    		    		
			    		    		em.flush(); 
			    		    		log.info(" Despues Persistida geometria  WKB: "+geoEnt.getGeom());
			    		    		logImportacion.anadirLinea(" Despues Persistida geometria  WKB: "+geoEnt.getGeom());
			    		    		
			    		    		/*
			    		    			// Meter geometria
				    		    		String sql = "update planeamiento.entidadpol set geom=geometryfromtext('" + value  + "') where iden=" + String.valueOf(geoEnt.getIden());
				    		    		
				    		    		//resultado.info("SQL: " + sql);
				                        em.createNativeQuery(sql).executeUpdate(); 
				                        em.flush(); 
				                        log.info(" Persistida geometria  WKB ");
			    		    		
			    		    		*/
			    		    		
			                        
			                        log.info("Persistida GEOMETRIA Poligonal ENTIDAD, código: " + entidad.getCodigo());
			                        logImportacion.anadirLinea("Persistida GEOMETRIA Poligonal ENTIDAD, código: " + entidad.getCodigo());
			    		    		
			    		    	} catch (Exception e) {
			    		    		resultado.error("Error persistiendo geometria: " + value2 + ", error: " + e.getMessage());
			    		    		logImportacion.anadirLinea("Error persistiendo geometria: " + value2 + ", error: " + e.getMessage());
			    		    	} 
		    					
		    				} else if ( (value2.compareTo("LINEST")==0) || (value2.compareTo("MULTIL")==0) ) {
		    					
		    					Entidadlin geoEnt = new Entidadlin();
		    					geoEnt.setEntidad(entidad);
		    					geoEnt.setBsuspendida(suspendida);
		    					geoEnt.setGeom(value);
		    					
		    					// Persistir
			    		    	try {
			    		    		em.persist(geoEnt);
			    		    		
			    		    		/**
			    		    		em.flush(); 
			    		    		
			    		    		// Meter geometria
			                        em.createNativeQuery(
			                        		"update planeamiento.entidadlin " +
			                        		"set geom=geometryfromtext('" + value  + "') where iden=" + String.valueOf(geoEnt.getIden())).executeUpdate(); 
			                        em.flush();
			                        */
			                        log.info("Persistida GEOMETRIA Lineal ENTIDAD, código: " + entidad.getCodigo());
			                        logImportacion.anadirLinea("Persistida GEOMETRIA Lineal ENTIDAD, código: " + entidad.getCodigo());
			    		    		
			    		    	} catch (Exception e) {
			    		    		resultado.error("Error persistiendo geometria: " + value2 + ", error: " + e.getMessage());
			    		    		logImportacion.anadirLinea("Error persistiendo geometria: " + value2 + ", error: " + e.getMessage());
			    		    	} 
		    					
		    				} else if (value2.compareTo("POINT(")==0) {
		    					
		    					Entidadpnt geoEnt = new Entidadpnt();
		    					geoEnt.setEntidad(entidad);
		    					geoEnt.setBsuspendida(suspendida);
		    					geoEnt.setGeom(value);
		    					
		    					// Persistir
			    		    	try {
			    		    		em.persist(geoEnt);
			    		    		/**
			    		    		em.flush(); 
			    		    		
			    		    		// Meter geometria
			                        em.createNativeQuery(
			                        		"update planeamiento.entidadpnt " +
			                        		"set geom=geometryfromtext('" + value  + "') where iden=" + String.valueOf(geoEnt.getIden())).executeUpdate(); 
			                        em.flush();
			                        */
			                        log.info("Persistida GEOMETRIA Puntual ENTIDAD, código: " + entidad.getCodigo());
			                        logImportacion.anadirLinea("Persistida GEOMETRIA Puntual ENTIDAD, código: " + entidad.getCodigo());
			    		    		
			    		    	} catch (Exception e) {
			    		    		resultado.error("Error persistiendo geometria " + value2);
			    		    		logImportacion.anadirLinea("Error persistiendo geometria " + value2);
			    		    	} 
		    					
		    				} else {
		    					
		    					// Si no es ninguno de los anteriores entonces la geometría 
		    					// viene en WKB
		    					
		    					// OJO: El String que tenemos en WKB es literalmente una cadena de bytes
		    					// así que el siguiente código hay que cambiarlo. Esto se va a dar muy pocas veces
		    					
		    					/*
		    					// Convertimos a WKT para saber el tipo
		    					WKBReader wkbReader = new WKBReader(); 
		    					Geometry geom;
								try {
									geom = wkbReader.read(value.getBytes(Charset.defaultCharset()));

									geom.isValid();
									value2 = geom.getGeometryType();

									// Según el tipo ...
									if ((value2.compareTo("Polygon") == 0) || (value2.compareTo("Multipolygon") == 0)) {

										Entidadpol geoEnt = new Entidadpol();
										geoEnt.setEntidad(entidad);
										geoEnt.setBsuspendida(suspendida);
										geoEnt.setGeom(value);
										em.persist(geoEnt);
										
									} else if (value2.compareTo("LineString") == 0) {

										Entidadlin geoEnt = new Entidadlin();
										geoEnt.setEntidad(entidad);
										geoEnt.setBsuspendida(suspendida);
										geoEnt.setGeom(value);
										em.persist(geoEnt);
										
									} else if (value2.compareTo("Point") == 0) {

										Entidadpnt geoEnt = new Entidadpnt();
										geoEnt.setEntidad(entidad);
										geoEnt.setBsuspendida(suspendida);
										geoEnt.setGeom(value);
										em.persist(geoEnt);
									}
								} catch (ParseException e) {
									resultado.error("Error al parsear la Geometria GWB, error: "+e.getMessage());
								}
								*/
	                            
		    					resultado.error("El tipo de geometria es desconocido: "+value2+" ...");
		    					logImportacion.anadirLinea("El tipo de geometria es desconocido: "+value2+" ...");
		    				}
		    			}
			    	}
		    		
		    		// HIJAS
		    		if (children.item(k).getNodeName().compareTo("HIJAS")==0) {
		    			_importarEntidades(children.item(k).getChildNodes(), entidad);	
		    		}
		    		
		    		// BASE
		    		if (children.item(k).getNodeName().compareTo("BASE")==0) {    
		    			Map<String, Object> attrMapBase = nodeToMap( children.item(k) );
		    			entidad.setBaseTramite((String) attrMapBase.get("tramite"));
		    			entidad.setBaseEntidad((String) attrMapBase.get("entidad"));
		    		}
		    	}	
	    	}
	    }
	}	
	
	
	
	
	/* 
	 *  IMPORTAR APLICACION AMBITOS
	 *  
	 */
	private void importarAplicacionAmbitos() throws EditorFIPException  {
		
		log.info("Importando APLICACION AMBITOS ...");
		logImportacion.anadirLinea("Importando APLICACION AMBITOS ...");
		NodeList nodes = evaluateXPath(xmlPath + "/APLICACION-AMBITOS/APLICACION-AMBITO");	
		
		// Vars
		Node node;
		int countApAmb = 0, idAmbito;
		String codEntidad;
		Entidad entidad;
		
		// Por cada APLICACION-AMBITO ...
		for (int i = 0; i < nodes.getLength(); i++) {

			try {

				// Nodo
				node = nodes.item(i);
		
				// Coger datos de los atributos
				attrMap = nodeToMap( node );
				idAmbito = Integer.parseInt((String) attrMap.get("ambito"));
				codEntidad = (String) attrMap.get("entidad");
				
				// Obtener entidad
				entidad = Utiles.getEntidad(tramite.getCodigofip(), codEntidad);
				if (entidad==null) throw new EditorFIPException("APLICACION-AMBITOS->APLICACION-AMBITO, la entidad no existe: "+tramite.getCodigofip()+"/"+codEntidad);
						
				// Crear AmbitoAplicacion
				Ambitoaplicacionambito ambitoAplicacion = new Ambitoaplicacionambito();
				ambitoAplicacion.setEntidad(entidad);
				ambitoAplicacion.setIdambito(idAmbito);
					
				// Persistir
				em.persist(ambitoAplicacion);
				countApAmb++;
				
			} catch (EditorFIPException e2) {	
				resultado.error("APLICACION-AMBITOS->APLICACION-AMBITO: " + e2.getMessage());
				logImportacion.anadirLinea("APLICACION-AMBITOS->APLICACION-AMBITO: " + e2.getMessage());
			} catch (Exception e) {				
				resultado.error("Error desconocido en APLICACION-AMBITOS->APLICACION-AMBITO: "+e.getMessage());
				logImportacion.anadirLinea("Error desconocido en APLICACION-AMBITOS->APLICACION-AMBITO: "+e.getMessage());
			}
	    }
		
		// Resultado
		resultado.contador.put(xmlPath + "/APLICACION-AMBITOS/APLICACION-AMBITO", countApAmb);
		resultado.info("Trámite "+tramite.getCodigofip()+", APLICACION-AMBITOS->APLICACION-AMBITO: " + countApAmb);
		logImportacion.anadirLinea("Trámite "+tramite.getCodigofip()+", APLICACION-AMBITOS->APLICACION-AMBITO: " + countApAmb);
	}
	
	
	
	/* 
	 *  IMPORTAR ADSCRIPCION AMBITOS
	 *  
	 */
	private void importarAdscripciones() throws EditorFIPException  {
		
		log.info("Importando ADSCRIPCIONES ...");
		logImportacion.anadirLinea("Importando ADSCRIPCIONES ...");
		NodeList nodes = evaluateXPath(xmlPath + "/ADSCRIPCIONES/ADSCRIPCION");		
		
		// Vars
		int contador = 0;
		Node node;
		String codEntidadOrigen, codEntidadDestino, texto=null, cuantia=null, codTramite, codDet;
		Entidad entidadOrigen, entidadDestino;
		Determinacion detUnidad=null, detTipo=null;
		Relacion relacion;
		Propiedadrelacion propRelacion, propRelacionTexto=null;
		Vectorrelacion vectorEntOrigen, vectorEntDestino, vectorDetUnidad, vectorDetTipo;
		
		// Por cada APLICACION-AMBITO ...
		for (int i = 0; i < nodes.getLength(); i++) {

			try {

				// Nodo
				node = nodes.item(i);
		
				// Coger datos de los atributos
				attrMap = nodeToMap( node );
				codEntidadOrigen = (String) attrMap.get("entidadOrigen");
				codEntidadDestino = (String) attrMap.get("entidadDestino");
				
				// Obtener entidad origen
				entidadOrigen = Utiles.getEntidad(tramite.getCodigofip(), codEntidadOrigen);
				if (entidadOrigen==null) 
					throw new EditorFIPException("ADSCRIPCION, la entidad origen no existe: "+tramite.getCodigofip()+"/"+codEntidadOrigen);
						
				// Obtener entidad destino
				entidadDestino = Utiles.getEntidad(tramite.getCodigofip(), codEntidadDestino);
				if (entidadDestino==null) 
					throw new EditorFIPException("ADSCRIPCION, la entidad destino no existe: "+tramite.getCodigofip()+"/"+codEntidadDestino);
						
				// Obtener elementos dentro de ADSCRIPCION
				for(int k=0;k<node.getChildNodes().getLength();k++) {
					
					// PROPIEDADES
					if (node.getChildNodes().item(k).getNodeName().compareTo("PROPIEDADES")==0) {
						
						// CUANTIA
						attrMap = nodeToMap( node.getChildNodes().item(k) );
						cuantia = (String) attrMap.get("cuantia");
						
						for(int j=0;j<node.getChildNodes().item(k).getChildNodes().getLength();j++) {
							
							Node propNode = node.getChildNodes().item(k).getChildNodes().item(j);						
							attrMap = nodeToMap( propNode );
							codTramite = (String) attrMap.get("tramite");
							codDet = (String) attrMap.get("determinacion");
						
							// UNIDAD
							if (propNode.getNodeName().compareTo("UNIDAD")==0) {

								// Obtener la determinación UNIDAD (es obligatorio para la adscripcion)
								detUnidad = Utiles.getDeterminacion(codTramite, codDet);
								if (detUnidad==null) throw new EditorFIPException("ADSCRIPCIONES->ADSCRIPCION, determinacion UNIDAD no existe, "+codTramite+"/"+codDet);
							}
							
							// TIPO
							if (propNode.getNodeName().compareTo("TIPO")==0) {
								
								//  Obtener la determinación TIPO (es obligatorio para la adscripcion)
								detTipo = Utiles.getDeterminacion(codTramite, codDet);
								if (detTipo==null) throw new EditorFIPException("ADSCRIPCIONES->ADSCRIPCION, determinacion TIPO no existe, "+codTramite+"/"+codDet);
							}
							
							// TEXTO
							if (propNode.getNodeName().compareTo("TEXTO")==0) {
								texto = propNode.getNodeValue();
							}
						}
					}
				}
				
				// Crear relación
				relacion = new Relacion();
				relacion.setIddefrelacion(_DEFREL_ADSCRIPCION_UNIDADES);
				relacion.setTramiteByIdtramitecreador(tramite);
				
				
				// Crear propiedad relación
				propRelacion = new Propiedadrelacion();
				propRelacion.setRelacion(relacion);
				propRelacion.setIddefpropiedad(_DEFPROP_CUANTIA_ADSCRIPCION);
				propRelacion.setValor(cuantia);
				
				// Si tenemos texto crear una propiedad de relación
				if (texto != null) {
					propRelacionTexto = new Propiedadrelacion();
					propRelacionTexto.setRelacion(relacion);
					propRelacionTexto.setIddefpropiedad(_DEFPROP_TEXTO_ADSCRIPCION);
					propRelacionTexto.setValor(texto);
				}
				
				// Crear vector => Entidad origen
				vectorEntOrigen = new Vectorrelacion();
				vectorEntOrigen.setRelacion(relacion);
				vectorEntOrigen.setIddefvector(_DEFVECT_ADSCRIPCION_ENTIDAD_ORIGEN);
				vectorEntOrigen.setValor(entidadOrigen.getIden());
				
				// Crear vector => Entidad destino
				vectorEntDestino = new Vectorrelacion();
				vectorEntDestino.setRelacion(relacion);
				vectorEntDestino.setIddefvector(_DEFVECT_ADSCRIPCION_ENTIDAD_DESTINO);
				vectorEntDestino.setValor(entidadDestino.getIden());
				
				// Crear vector => Determinacion unidad
				vectorDetUnidad = new Vectorrelacion();
				vectorDetUnidad.setRelacion(relacion);
				vectorDetUnidad.setIddefvector(_DEFVECT_ADSCRIPCION_UNIDAD);
				vectorDetUnidad.setValor(detUnidad.getIden());
				
				// Crear vector => Determinacion tipo
				vectorDetTipo = new Vectorrelacion();
				vectorDetTipo.setRelacion(relacion);
				vectorDetTipo.setIddefvector(_DEFVECT_ADSCRIPCION_TIPO);
				vectorDetTipo.setValor(detTipo.getIden());			

				// Persistir
				em.persist(relacion);
				em.persist(propRelacion);
				if (propRelacionTexto!=null) em.persist(propRelacionTexto);
				em.persist(vectorEntOrigen);
				em.persist(vectorEntDestino);
				em.persist(vectorDetUnidad);
				em.persist(vectorDetTipo);
				contador++;
				
			} catch (EditorFIPException e2) {	
				resultado.error("ADSCRIPCION: " + e2.getMessage());
				logImportacion.anadirLinea("ADSCRIPCION: " + e2.getMessage());
			} catch (Exception e) {				
				resultado.error("Error desconocido en ADSCRIPCION: "+e.getMessage());
				logImportacion.anadirLinea("Error desconocido en ADSCRIPCION: "+e.getMessage());
			}
	    }
		
		// Resultado
		resultado.contador.put(xmlPath + "/ADSCRIPCIONES/ADSCRIPCION", contador);
		resultado.info("Trámite "+tramite.getCodigofip()+", ADSCRIPCION: " + contador);
		logImportacion.anadirLinea("Trámite "+tramite.getCodigofip()+", ADSCRIPCION: " + contador);
	}
	
	
	
	
	/* 
	 *  IMPORTAR OPERACIONES
	 *  
	 */
	/*
	 	--- NO HAY OPERACIONES EN EL FIP1 --
	 
	private void importarOperaciones() throws EditorFIPException  {
		
		debug("Importando OPERACIONES ...");
		NodeList nodes = evaluateXPath(xmlPath + "/" + "OPERACIONES/OPERACIONES-DETERMINACIONES/OPERACION-DETERMINACION");	
		
		// Vars
		Node node;
		Operacion operacion;
		Operaciondeterminacion operacionDet;
		int tipoOperacion, orden, countOperDet = 0;
		String texto=null, codTramiteOperada=null, codTramiteOperadora=null, codDetOperada=null, codDetOperadora=null;
		Determinacion detOperada=null, detOperadora=null;
		
		// Por cada OPERACION-DETERMINACION ...
		for (int i = 0; i < nodes.getLength(); i++) {

			try {

				// Nodo
				node = nodes.item(i);
		
				// Coger datos de los atributos
				attrMap = nodeToMap( node );
				tipoOperacion = Integer.parseInt((String) attrMap.get("tipo"));
				orden = Integer.parseInt((String) attrMap.get("orden"));
						
				// Obtener los elementos dentro de OPERACION-DETERMINACION
				for(int k=0;k<node.getChildNodes().getLength();k++) {
					Node opNode = node.getChildNodes().item(k);
					
					// --- TEXTO ---
					if (opNode.getNodeName().compareTo("TEXTO")==0) {
						texto = (String) opNode.getNodeValue();			
					}		
					
					// --- OPERADA ---
					if (opNode.getNodeName().compareTo("OPERADA")==0) {
						attrMap = nodeToMap( opNode );	
						
						codTramiteOperada = (String) attrMap.get("tramite");
						codDetOperada = (String) attrMap.get("determinacion");	
						
						// Obtener Determinacion 
						detOperada = resultado.getDeterminacion(codTramiteOperada, codDetOperada);
	
						// Si no existe la determinacion, abortar
						if (detOperada==null) throw new EditorFIPException("La determinación Operada no existe: "+codTramiteOperada+"/"+codDetOperada);
					}	
					
					// --- OPERADORA ---
					if (opNode.getNodeName().compareTo("OPERADORA")==0) {
						attrMap = nodeToMap( opNode );
						
						codTramiteOperadora = (String) attrMap.get("tramite");
						codDetOperadora = (String) attrMap.get("determinacion");	
						
						// Obtener Determinacion 
						detOperadora = resultado.getDeterminacion(codTramiteOperadora, codDetOperadora);
						
						// Si no existe la determinacion, abortar
						if (detOperadora==null) throw new EditorFIPException("La determinación Operadora no existe: "+codTramiteOperadora+"/"+codDetOperadora);	
					}	
				}
				
				// Comprobar que los trámites de las dos determinaciones son el mismo
				if (codTramiteOperada.compareTo(codTramiteOperadora)!=0) 
					throw new EditorFIPException("Los trámites de las Determinaciónes son distintos: "+codTramiteOperada+" <> "+codTramiteOperadora);
				
				// Crear Operacion
				operacion = new Operacion();
				operacion.setTramite(tramite);
				operacion.setTexto(texto);
				operacion.setOrden(orden);
				
				// Crear Operacion Determinacion
				operacionDet = new Operaciondeterminacion();
				operacionDet.setOperacion(operacion);
				operacionDet.setIdtipooperaciondet(tipoOperacion);
				operacionDet.setDeterminacionByIddeterminacion(detOperada);
				operacionDet.setDeterminacionByIddeterminacionoperadora(detOperadora);
				
				// Persistir
				em.persist(operacion);
				em.persist(operacionDet);
				countOperDet++;
				
			} catch (EditorFIPException e2) {	
				resultado.error("OPERACIONES->OPERACION-DETERMINACION: " + e2.getMessage());
			} catch (Exception e) {				
				resultado.error("Error desconocido en OPERACION->OPERACION-DETERMINACION: "+e.getMessage());
			}
	    }
		
		// Resultado
		resultado.info("Trámite "+tramite.getCodigofip()+", OPERACIONES->OPERACION-DETERMINACION: " + countOperDet);
	}
	*/

}

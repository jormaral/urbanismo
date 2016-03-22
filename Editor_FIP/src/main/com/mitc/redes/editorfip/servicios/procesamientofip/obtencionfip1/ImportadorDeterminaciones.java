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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.EntityManager;

import org.jboss.seam.log.Log;
import org.jboss.seam.log.Logging;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Determinacion;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Determinaciongrupoentidad;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Documento;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Documentodeterminacion;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Documentoentidad;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Opciondeterminacion;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Plan;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Propiedadrelacion;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Relacion;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Tramite;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Vectorrelacion;
import com.mitc.redes.editorfip.excepciones.EditorFIPException;

public class ImportadorDeterminaciones extends ImportadorBase {

	Log log = Logging.getLog(ImportadorDeterminaciones.class);
	
	// Propiedades
	private Tramite tramite;
	private Determinacion determinacion;	
	public String xmlPath ="";
	public Plan plan;
	private Resultado resultado;
	
	// Contadores
	private int countDet = 0;
	private int countRegEsp = 0;
	
	// Clases de apoyo
	private class Unidad {
		public String definicion;
		public String abrev;
	}
	private class DeterminacionReguladora {
		int idenDeterminacionOrigen;
		int idenDeterminacionDestino;
		public String tramite;
		public String determinacion;
		public int caracter;
	}
	private class VinculoValorReferencia {
		Determinacion detOrigen;
		String tramite;
		String determinacion;
	}
	List<VinculoValorReferencia> valorRefList = new ArrayList<VinculoValorReferencia>();
	
	// Mapas
	Map<String,Unidad> mapUnidad = new HashMap<String,Unidad>();  
	Map<String,DeterminacionReguladora> mapDetReg = new HashMap<String,DeterminacionReguladora>();
	
	
	
	
	
	// -----------------------------------------------------------------------------
	
	
	
	/*
	 * 	CONSTRUCTOR
	 */
	public ImportadorDeterminaciones(Document xml, EntityManager em, String xmlPath, Tramite tramite, Resultado resultado) {
		super(xml, em);
		debugPrefix = "Tramite:";
		this.xmlPath = xmlPath;
		this.tramite = tramite;
		this.resultado = resultado;
	}
	
	/*
	 * 	IMPORTAR
	 * 
	 */
	@Override
	protected void importar() throws EditorFIPException {

		log.info("Importando Determinaciones");
		NodeList nodesDet = evaluateXPath(xmlPath + "/" + "DETERMINACIONES/DETERMINACION");	
		_importarDeterminaciones(nodesDet, null, 0);
		resultado.contador.put(xmlPath + "/DETERMINACIONES/DETERMINACION", countDet);
		resultado.info("Tramite "+tramite.getCodigofip()+", DETERMINACIONES: "+countDet);

		// Bases, Valores de referencia y Unidades
		establecerBases();
		establecerValoresReferencia();
		establecerUnidades();
		
		// Regulaciones especificas
		establecerDeterminacionesReguladoras();
		resultado.contador.put(xmlPath + "/DETERMINACIONES/DETERMINACION/REGULACIONES-ESPECIFICAS", countRegEsp);
		resultado.info("Tramite "+tramite.getCodigofip()+", REGULACIONES-ESPECIFICAS: "+countRegEsp);
	    
		establecerGruposAplicacion();
	}
	


	/*
	 * 	IMPORTAR DETERMINACIONES
	 * 
	 * 	Función recursiva para la importación de una determinación, ya que las determinaciones
	 *  forman un árbol de padre-hijo
	 * 
	 */
	private void _importarDeterminaciones(NodeList nodes, Determinacion padre, int level) throws EditorFIPException {
		
		// Vars
		String codigo=null;
		Node node;
		NodeList children;
		int ordenDeterminacion=1;
		Determinacion determinacion = null;
		
		// Por cada nodo ...		
		for (int i = 0; i < nodes.getLength(); i++) {
			
			node = nodes.item(i);
			if (node.getNodeName().compareTo("DETERMINACION") != 0) continue;
			attrMap = nodeToMap(node);

			// Persistir determinación
			try {
				
				// Nueva determinacion
				determinacion = new Determinacion();
				codigo = (String) attrMap.get("codigo");
				determinacion.setCodigo(codigo);
				int idCaracter = Integer.parseInt( (String) attrMap.get("caracter"));
				determinacion.setIdcaracter(idCaracter);
				determinacion.setApartado((String) attrMap.get("apartado"));
				determinacion.setNombre((String) attrMap.get("nombre"));
				determinacion.setEtiqueta((String) attrMap.get("etiqueta"));
				boolean suspendida = ((String) attrMap.get("suspendida")).compareTo("true") == 0;
				determinacion.setBsuspendida(suspendida);
				determinacion.setTramite(tramite);
				if (padre != null) determinacion.setDeterminacionByIdpadre(padre);
				
				// Orden de la determinación
				determinacion.setOrden(ordenDeterminacion);
				ordenDeterminacion++;
				
				// Guardar en cache de determinaciones
				Utiles.mapDet.put(tramite.getCodigofip()+"/"+codigo, determinacion);
				
				// Persistir
				em.persist(determinacion);
				log.info("Persistiendo DETERMINACION, codigo: " + codigo + ", nivel/orden: "+level+"/"+determinacion.getOrden());
				countDet++;
				
			} catch (Exception e) {
				resultado.error("Error desconocido persistiendo DETERMINACION, error: "+e.getMessage());
				e.printStackTrace();
			}
			
			
			if (codigo.compareTo("0000000167")==0) {
				log.info("*************************************************************************");
				log.info("*************************************************************************");
			}

			// Parseamos los elementos que tiene en el interior
			children = node.getChildNodes();
			Map<String, Object> attrs;
			for (int k = 0; k < children.getLength(); k++) {
				
				attrs = nodeToMap( children.item(k) );
				
				// TEXT .......................................................................
				if (children.item(k).getNodeName().compareTo("TEXTO") == 0) {
					if (children.item(k).getFirstChild()!=null)
					{
						determinacion.setTexto((String)children.item(k).getFirstChild().getTextContent());
					}
					
				}
				
				// VALOR DE REFERENCIA
				if (children.item(k).getNodeName().compareTo("VALORES-REFERENCIA") == 0) {

					for (int n = 0; n < children.item(k).getChildNodes().getLength(); n++) {
						Node nn = children.item(k).getChildNodes().item(n);
						if (nn.getNodeName().compareTo("VALOR-REFERENCIA") == 0) {
							
							attrs = nodeToMap( nn );
							VinculoValorReferencia vr = new VinculoValorReferencia();
							vr.detOrigen = determinacion;
							vr.tramite = (String) attrs.get("tramite");
							vr.determinacion = (String) attrs.get("determinacion");
							valorRefList.add(vr);
						}
					}
				}

				// HIJAS ......................................................................
				if (children.item(k).getNodeName().compareTo("HIJAS") == 0) {
					_importarDeterminaciones(children.item(k).getChildNodes(), determinacion, level + 1);
				}
				
				// DOCUMENTOS .................................................................. 
				if (children.item(k).getNodeName().compareTo("DOCUMENTOS") == 0) {

					for (int n = 0; n < children.item(k).getChildNodes().getLength(); n++) {
						Node nn = children.item(k).getChildNodes().item(n);
						if (nn.getNodeName().compareTo("DOCUMENTO") == 0) {
							String codDoc = (String) nn.getAttributes().item(0).getNodeValue();
							
							// Coger documento
							Documento doc = Utiles.getDocumento(tramite.getCodigofip(), codDoc);
							
							if (doc!=null)
							{
								// Nuevo objeto documento de determinacion
								Documentodeterminacion docDeterminacion = new Documentodeterminacion();
								docDeterminacion.setDeterminacion(determinacion);
								docDeterminacion.setDocumento(doc);

								// Persistir
			    		    	try {
			    		    		// Persistir
									log.info("Persistiendo DETERMINACION->DOCUMENTO, codigo doc: " + codDoc);
									em.persist(docDeterminacion);
			    		    	} catch (Exception e) {
			    		    		resultado.error("Error persistiendo DOCUMENTO en Determinacion con codigo doc: "+codDoc);
			    		    	} 
							}
							else
							{
								// No ha encontrado el documento. Informo de este hecho. El FIP.XML es erroneo
								resultado.error("Error persistiendo DOCUMENTO en Determinacion con codigo doc: " + codDoc +" No existe dicho documento en el FIP.xml y no se ha asociado");
							}
							
							
						}
					}
				}
				
				// UNIDAD ....................................................................
				if (children.item(k).getNodeName().compareTo("UNIDAD") == 0) {		
	    			determinacion.setUnidadTramite((String) attrs.get("tramite"));
	    			determinacion.setUnidadDeterminacion((String) attrs.get("determinacion"));
				}
				
				// BASE ......................................................................
				if (children.item(k).getNodeName().compareTo("BASE") == 0) {
	    			determinacion.setBaseTramite((String) attrs.get("tramite"));
	    			determinacion.setBaseDeterminacion((String) attrs.get("determinacion"));
				}
				
				// REGULACIÓN ................................................................ 
				if (children.item(k).getNodeName().compareTo("REGULACION") == 0) {
					
					log.info("Determinacion->Regulacion");
					Node regulacionNode = children.item(k);
					 
					// Por cada elemento dentro de REGULACION
					for (int l = 0; l < regulacionNode.getChildNodes().getLength(); l++) {
						
						// Localizar DETERMINACION-REGULADORA
						if (regulacionNode.getChildNodes().item(l).getNodeName().compareTo("DETERMINACIONES-REGULADORAS") == 0) {
							NodeList detRegNodes = regulacionNode.getChildNodes().item(l).getChildNodes();								

							// Por cada elemento dentro de DETERMINACIONES-REGULADORAS
							for (int u = 0; u < detRegNodes.getLength(); u++) {								
								if (detRegNodes.item(u).getNodeName().compareTo("DETERMINACION-REGULADORA")==0) {
									
									attrs = nodeToMap( detRegNodes.item(u) );
									
									// Coger la determinación a la que se regula
									String codTramite = (String)attrs.get("tramite");
									String codDet = (String)attrs.get("determinacion");
									Determinacion detDestino = Utiles.getDeterminacion(codTramite, codDet);
									if (detDestino==null) {
										
										resultado.error("DETERMINACION-REGULADORA: La determinación origen no existe: "+codTramite+"/"+codDet);
										
									} else {
										
										DeterminacionReguladora dr = new DeterminacionReguladora();
										dr.tramite = (String)attrs.get("tramite");
										dr.determinacion = (String)attrs.get("determinacion");
										dr.caracter = determinacion.getIdcaracter();
										dr.idenDeterminacionOrigen = determinacion.getIden();
										dr.idenDeterminacionDestino = detDestino.getIden();
										mapDetReg.put(codigo, dr);
									}
								}
							}
						}
						
						// Localizar REGULACION-ESPECIFICA
						if (regulacionNode.getChildNodes().item(l).getNodeName().compareTo("REGULACIONES-ESPECIFICAS") == 0) {
							NodeList regEspNodes = regulacionNode.getChildNodes().item(l).getChildNodes();								
							log.info("Determinacion->Regulacion->Regulaciones-especificas");
							// Por cada elemento dentro de DETERMINACIONES-REGULADORAS
							for (int u = 0; u < regEspNodes.getLength(); u++) {								
								if (regEspNodes.item(u).getNodeName().compareTo("REGULACION-ESPECIFICA")==0) {									
									persistirRegulacionEspecifica(determinacion, regEspNodes.item(u), null);							
								}
							}
						}
					}
				}
				
				// GRUPOS-APLICACION ....................................................................
				if (children.item(k).getNodeName().compareTo("GRUPOS-APLICACION") == 0) {
					
					// Por cada GRUPO-APLICACION
					for (int n = 0; n < children.item(k).getChildNodes().getLength(); n++) {
						Node nn = children.item(k).getChildNodes().item(n);
						if (nn.getNodeName().compareTo("GRUPO-APLICACION") == 0) {
							
							attrs = nodeToMap( nn );
							determinacion.gruposAplicaciones.add((String) attrs.get("tramite") + "/" + (String) attrs.get("determinacion"));
							//determinacion.setGrupoAplicacionTramite((String) attrs.get("tramite"));
			    			//determinacion.setGrupoAplicacionDeterminacion((String) attrs.get("determinacion"));
						}
					}
				}
			}
		}
	}
	
	

	/*
	 * 	ESTABLECER BASES
	 * 
	 * 	Recorrer todas las determinaciones creadas y en caso de que tenga
	 * 	determinación base asignado persistirlo.
	 * 
	 */
	private void establecerBases() throws EditorFIPException {

		log.info("Estableciendo bases para las determinaciónes creadas...");
		int countBases = 0;
		
		// Hash para guardar las determinaciones bases 
		Map<String,Determinacion> mapDetBase = new HashMap<String,Determinacion>();
		
		// Por cada determinación que hemos creado anteriormente ...
		for (Iterator<?> itr = Utiles.mapDet.entrySet().iterator(); itr.hasNext();) {
			
	    	// Coger la determinacion
	    	@SuppressWarnings("unchecked")
			Map.Entry<String,Determinacion> entry = (Map.Entry<String, Determinacion>) itr.next();
	    	
	    	// Datos de la determinación base
	    	Determinacion det = entry.getValue();
	    	String codDeterminacion = det.getBaseDeterminacion();
	    	String codTramite = det.getBaseTramite();
	    	String codigoComp = codTramite+"/"+codDeterminacion;
	    
	    	try {
		    	
	    		// La determinación actual tiene BASE ???
		    	if ( (codTramite != null) && (codDeterminacion!=null) ) {
				    	
			    		// Si la determinación es del actual trámite y tiene una determinación base ...
				    	Determinacion detBase = Utiles.getDeterminacion(codTramite, codDeterminacion, mapDetBase);
				    	if (detBase==null) throw new EditorFIPException("No existe la determinación padre tramite/determinacion: " + codigoComp);
						
						// Establecemos la determinación base a la determinación actual
						det.setDeterminacionByIddeterminacionbase(detBase);
						mapDetBase.put(codigoComp, detBase);
						
			    		// Persistir la  pero ya con su base
			    		log.info("Modif. det.: "+det.getCodigo()+" (id:"+det.getIden()+") " +
			    				"con DETERMINACION->BASE "+det.getBaseTramite()+"/"+det.getBaseDeterminacion() + " (id: "+detBase.getIden()+")");
			    		em.merge(det);
			    		countBases++;
		    	}
			    		
	    	} catch (EditorFIPException e1) {
	    		resultado.error(e1.getMessage());	
	    	} catch (Exception e2) {
	    		resultado.error("Error desconocido persistiendo DETERMINACION con codigo: " + det.getCodigo()+", error: "+e2.getMessage());
	    		e2.printStackTrace();
	    	}
	  	}
	    
	    // Resultados
	    resultado.contador.put(xmlPath + "/DETERMINACIONES/DETERMINACION/BASE", countBases);
	    resultado.info("Tramite "+tramite.getCodigofip()+", DETERMINACION->BASE: "+countBases);	    
	}
	
	

	/*
	 * 	ESTABLECER VALORES REFERENCIA
	 * 
	 * 	Recorrer todas las determinaciones creadas y en caso de que tenga
	 * 	valor de referencia asignado persistirlo.
	 * 
	 */
	private void establecerValoresReferencia() throws EditorFIPException {
		
		log.info("Estableciendo Valores de Referencia para las determinaciónes creadas...");
		int countValRef=0;		
		String codigoComp=null;
		Map<String,Determinacion> mapDetAux = new HashMap<String,Determinacion>();
		
		// Por cada vínculo de determinacion y valor de referencia ...
		for(VinculoValorReferencia v : valorRefList) {
			
			try {
				
	    		// Crear Opcion-determinacion
				codigoComp = v.tramite + "/" + v.determinacion;
	    		Opciondeterminacion opDet = new Opciondeterminacion();
	    		opDet.setDeterminacionByIddeterminacion(v.detOrigen);
	    		
	    		// Si está vacio entonces hay un error en el FIP1
	    		Determinacion d = Utiles.getDeterminacion(v.tramite, v.determinacion, mapDetAux);
				if (d==null) throw new EditorFIPException("No existe la determinación padre: "+codigoComp);
				opDet.setDeterminacionByIddeterminacionvalorref(d);
				
				// Persistir 
	    		em.persist(opDet);
	    		//em.flush();
	    		log.info("Pers. DET->VR (id:"+opDet.getIden()+"), val. ref. codigo: "+codigoComp+" (id: "+d.getIden()+")");
	    		String key = tramite.getCodigofip()+"/"+v.detOrigen.getCodigo()+"/"+codigoComp;
	    		Utiles.mapOpDet.put(key, opDet);
	    		countValRef++;
	    		
	    	} catch (EditorFIPException e1) {
	    		resultado.error("Error DETERMINACION->VALOR-REFERENCIA: "+e1.getMessage());
	    	} catch (Exception e2) {
	    		resultado.error("Error desconocido DETERMINACION->VALOR-REFERENCIA con codigo: "+codigoComp+", error: "+e2.getMessage());
	    	}	
		}
		
		Utiles.mapDet.putAll(mapDetAux);
	    
	    // Resultados
	    resultado.contador.put(xmlPath + "/DETERMINACIONES/DETERMINACION/VALOR-REFERENCIA", countValRef);
	    resultado.info("Tramite "+tramite.getCodigofip()+", DETERMINACION->VALOR-REFERENCIA: "+countValRef);	    
	}
	

	/*
	 * 	ESTABLECER UNIDADES
	 * 
	 * 	Recorrer todas las determinaciones creadas y en caso de que sea una
	 * 	determinación de tipo Unidad (caracter=18) la persiste
	 * 
	 */
private void establecerUnidades() throws EditorFIPException {
		
		log.info("Estableciendo Unidades para las determinaciónes creadas...");
		int countDetUnidad=0;
		int countDetRelUnidad=0;

		// Mapear las unidades del xml
		mapearUnidades();	
		
		// Vars
		Unidad unidad;
		Determinacion det;
		Entry<String,Determinacion> entry;
		Relacion relacion;
		Propiedadrelacion propiedadRel1, propiedadRel2;
		Vectorrelacion vectorRel, vectorRel2;
		
		// Por cada determinación creada ...
		Iterator<?> itrD = Utiles.mapDet.entrySet().iterator();
		
		List<Map.Entry<String, Determinacion>> itrD2 = new ArrayList<Map.Entry<String, Determinacion>>();
		
		while(itrD.hasNext()) {
			itrD2.add((Map.Entry<String, Determinacion>) itrD.next());
		}
		
		
	    for (Map.Entry<String, Determinacion> itr : itrD2)
		{
	    	
	    	// Variables
	    	
	    	det = itr.getValue();
	    	
	    	if (det.getTramite().getCodigofip().compareTo(tramite.getCodigofip())==0) {
	    	
	    		// ---| Crear la unidad |---------------------------------------------------
	    		// La determinación resulta que es una Unidad ya que tiene caracter 18. La creamos.
		    	
		    	if (det.getIdcaracter()==18) {
		    			
			    	try {
			    		
			    		// Crear relación
			    		relacion = new Relacion();
			    		relacion.setIddefrelacion(_DEFREL_DETERAMINACION_UNIDAD_PROPIEDADES);
			    		relacion.setTramiteByIdtramitecreador(tramite);
			    		
			    		// Crear Propiedad 1 
			    		propiedadRel1 = new Propiedadrelacion();
			    		propiedadRel1.setRelacion(relacion);
			    		propiedadRel1.setIddefpropiedad(_DEFPROP_ABREVIATURA);
			    		unidad = mapUnidad.get(det.getCodigo());
			    		if (unidad==null) throw new EditorFIPException("Tramite: "+tramite.getCodigofip() 
			    			+ " UNIDAD no encontrada con codigo determinacion "+det.getCodigo());
			    		propiedadRel1.setValor(unidad.abrev);
			    		
			    		// Crear Propiedad 2 
			    		propiedadRel2 = new Propiedadrelacion();
			    		propiedadRel2.setRelacion(relacion);
			    		propiedadRel2.setIddefpropiedad(_DEFPROP_DEFINICION); 
			    		propiedadRel2.setValor(unidad.definicion);
			    		
			    		// Crear Vector Relación
			    		vectorRel = new Vectorrelacion();
			    		vectorRel.setIddefvector(_DEFVECT_DETERMINACION_UNIDAD_ASIGNAR_PROPIEDADES);
			    		vectorRel.setRelacion(relacion);
			    		vectorRel.setValor(det.getIden());
			    		
						// Persistir 
			    		log.info("Persistiendo UNIDAD que es una DETERMINACION, codigo det: "+det.getCodigo());
			    		em.persist(relacion);
			    		em.persist(propiedadRel1);
			    		em.persist(propiedadRel2);
			    		em.persist(vectorRel);
			    		countDetUnidad++;
			    		
			    	} catch (EditorFIPException e1) {
			    		resultado.error(e1.getMessage());
			    	} catch (Exception e2) {
			    		resultado.error("Error desconocido UNIDAD que es una DETERMINACION, codigo det: "
			    				+det.getCodigo()+", error: "+e2.getMessage());
			    	}
		    	}
			    	
			    	
			    	
		    	// ---| Relacionar la determinación con la unidad |---------------------------
		    	// La dterminación está relacionada con una Unidad
		    	
		    	if (det.getUnidadDeterminacion()!=null) {
		    		
			    	try {
			    		
			    		// Crear relación
			    		relacion = new Relacion();
			    		relacion.setIddefrelacion(_DEFREL_VINCULO_DETERMINACION_UNIDAD);
			    		relacion.setTramiteByIdtramitecreador(tramite);
			    		
			    		// Crear Vector Relación
			    		vectorRel = new Vectorrelacion();
			    		vectorRel.setIddefvector(_DEFVECT_VINCULO_DETERMINACION_UNIDAD_ORIGEN);
			    		vectorRel.setRelacion(relacion);
			    		vectorRel.setValor(det.getIden());
			    		
			    		// Crear Vector Relación
			    		vectorRel2 = new Vectorrelacion();
			    		vectorRel2.setIddefvector(_DEFVECT_VINCULO_DETERMINACION_UNIDAD_DESTINO);
			    		vectorRel2.setRelacion(relacion);
			    		Determinacion d = Utiles.getDeterminacion(det.getUnidadTramite(), det.getUnidadDeterminacion());
			    		if (d==null) throw new EditorFIPException(
			    				"Error asignando valor a determinación, determinación unidad no existe, codigo: "+
			    				det.getUnidadTramite() + "/" + det.getUnidadDeterminacion());
			    		vectorRel2.setValor(d.getIden());
		    		
			    		// Persistir 
			    		log.info("Persistiendo relacion DETERMINACION " +
			    				"("+det.getTramite().getCodigofip() + "/" + det.getCodigo()+") " +
			    				"con UNIDAD ("+det.getUnidadTramite() + "/" + det.getUnidadDeterminacion()+")");
			    		em.persist(relacion);
			    		em.persist(vectorRel);
			    		em.persist(vectorRel2);
			    		countDetRelUnidad++;
			    		
			    	} catch (EditorFIPException e1) {
			    		resultado.error(e1.getMessage());
			    	} catch (Exception e2) {
			    		resultado.error("Error persistiendo UNIDAD que es una DETERMINACION, codigo det: "+
			    			det.getCodigo()+", error: "+e2.getMessage());
			    	}
		    	}
		    	
		    	
	    	}
	    	
	    	
	  	}
	    
	    // Resultados
	    resultado.contador.put(xmlPath + "/DETERMINACIONES/DETERMINACION/UNIDAD", countDetRelUnidad);
	    resultado.info("Tramite "+tramite.getCodigofip()+", Determinaciones que son unidades: "+countDetUnidad);
	    resultado.info("Tramite "+tramite.getCodigofip()+", Determinaciones relacionada con unidad: "+countDetRelUnidad);
	}
	
	
	
	

	/*
	 * 	ESTABLECER DETERMINACIONES REGULADORAS
	 * 
	 * 
	 */
	private void establecerDeterminacionesReguladoras() throws EditorFIPException {
		
		log.info("Estableciendo Determinaciones Reguladoras para las determinaciónes creadas...");
		
		// Vars
		int countDetReg = 0;
		DeterminacionReguladora detReg;
		
		// Por cada determinación creada ...
		Iterator<?> itr = mapDetReg.entrySet().iterator();
	    while(itr.hasNext()) {
	    	
	    	// Variables
	    	@SuppressWarnings("unchecked")
	    	Entry<String,DeterminacionReguladora> entry = (Entry<String, DeterminacionReguladora>) itr.next();
	    	detReg = entry.getValue();
	    	
	    	try {
	    		
	    		int idDefRelacion = 0;
	    		int vector = 0;
	    		switch (detReg.caracter) {
	                case 1: idDefRelacion = 4; vector = 8; break;
	                case 2: idDefRelacion = 5; vector = 10; break;
	                case 3: idDefRelacion = 6; vector = 12; break;
	                case 4: idDefRelacion = 7; vector = 14; break;
	                case 5: idDefRelacion = 8; vector = 16; break;
	                case 6: idDefRelacion = 9; vector = 18; break;
	                case 7: idDefRelacion = 10; vector = 20; break;
	                case 8: idDefRelacion = 11; vector = 22; break;
	                case 9: idDefRelacion = 12; vector = 24; break;
	                case 10: idDefRelacion = 13; vector = 26; break;
	                case 11: idDefRelacion = 14; vector = 28; break;
	                case 12: idDefRelacion = 15; vector = 30; break;
	                case 13: idDefRelacion = 16; vector = 32; break;
	                case 14: idDefRelacion = 17; vector = 34; break;
	                case 15: idDefRelacion = 18; vector = 36; break;
	                case 17: idDefRelacion = 19; vector = 38; break;
	    		}
	    		if (idDefRelacion==0 || vector==0) 
	    			throw new EditorFIPException("DETERMINACION-REGULADORA: El id de relación o de vector no está establecido");
	    		
	    		// Crear relación
	    		Relacion relacion = new Relacion();
	    		relacion.setIddefrelacion(idDefRelacion);
	    		relacion.setTramiteByIdtramitecreador(tramite);
	    		
	    		// Crear Vector 1
	    		Vectorrelacion vectorRel = new Vectorrelacion();
	    		vectorRel.setIddefvector(vector);
	    		vectorRel.setRelacion(relacion);
	    		vectorRel.setValor(detReg.idenDeterminacionOrigen);
	    		
	    		// Crear Vector 2
	    		Vectorrelacion vectorRel2 = new Vectorrelacion();
	    		vectorRel2.setIddefvector(vector + 1);
	    		vectorRel2.setRelacion(relacion);
	    		vectorRel2.setValor(detReg.idenDeterminacionDestino);
		    		
				// Persistir 
	    		log.info("Persistiendo DETERMINACION-REGULADORA, codigo det: "+detReg.determinacion);
	    		em.persist(relacion);
	    		em.persist(vectorRel);
	    		em.persist(vectorRel2);
	    		countDetReg++;
	    		
	    	} catch (EditorFIPException e1) {
	    		resultado.error(e1.getMessage());
	    	} catch (Exception e2) {
	    		resultado.error("Error desconocido persistiendo DETERMINACION-REGULADORA, codigo det: "+
	    			detReg.determinacion + ", error: "+e2.getMessage());
	    	}	    	
	  	}
	    
	    // Resultados
	    resultado.contador.put(xmlPath + "/DETERMINACIONES/DETERMINACION/DETERMINACIONES-REGULADORAS", countDetReg);
	    resultado.info("Tramite "+tramite.getCodigofip()+", DETERMINACIONES-REGULADORAS: "+countDetReg);
	}
	
	
	/*
	 * 	PERSISTIR REGULACION ESPECÍFICA
	 * 
	 * 	Por cada regulacion especifica se crea una relación con tres propiedades, y 
	 *  a su vez esta relación debe estar enganchada a la padre.
	 * 
	 */
	private void persistirRegulacionEspecifica(Determinacion det, Node node, Relacion padre) {

		log.info("Persistiendo REGULACIONES-ESPECIFICAS");
		
		String texto = "", nombre = "", orden="";
		try {
			
			// Coger todos los datos del nodo		
			Map<String, Object> attrs = nodeToMap( node );
			nombre = (String) attrs.get("nombre");
			orden = (String) attrs.get("orden");
			NodeList hijas = null;
			for(int k=0;k<node.getChildNodes().getLength();k++) {
				
				// TEXTO
				if (node.getChildNodes().item(k).getNodeName().compareTo("TEXTO")==0)
					texto = node.getChildNodes().item(k).getTextContent();
				if (texto==null) texto="";
			
				// HIJAS
				if (node.getChildNodes().item(k).getNodeName().compareTo("HIJAS")==0)
					hijas = node.getChildNodes().item(k).getChildNodes();
			}
			
					
			// Crear relación
			Relacion relacion = new Relacion();
    		relacion.setIddefrelacion(_DEFREL_TEXTO_REGULACION_DETERMINACION);
    		relacion.setTramiteByIdtramitecreador(tramite);
    		
			// Crear propiedad para la relación => Orden
    		Propiedadrelacion propiedadRel1 = new Propiedadrelacion();
    		propiedadRel1.setRelacion(relacion);
    		propiedadRel1.setIddefpropiedad(_DEFPROP_ORDEN_PRESENTACION); 
    		propiedadRel1.setValor(orden);
    		
			// Crear propiedad para la relación => Nombre
    		Propiedadrelacion propiedadRel2 = new Propiedadrelacion();
    		propiedadRel2.setRelacion(relacion);
    		propiedadRel2.setIddefpropiedad(_DEFPROP_NOMBRE); 
    		propiedadRel2.setValor(nombre);
    		
			// Crear propiedad para la relación => Texto
    		Propiedadrelacion propiedadRel3 = new Propiedadrelacion();
    		propiedadRel3.setRelacion(relacion);
    		propiedadRel3.setIddefpropiedad(_DEFPROP_TEXTO); 
    		propiedadRel3.setValor(texto);
	    	
    		
			// Crear Vector 1 => Determinación
    		Vectorrelacion vectorRel1 = new Vectorrelacion();
    		vectorRel1.setIddefvector(_DEFVECT_DETERMINACION_REGULADA_40);
    		vectorRel1.setRelacion(relacion);
    		vectorRel1.setValor(det.getIden());
    		
			// Crear Vector => Padre
    		Vectorrelacion vectorRel2 = null;
    		if (padre!=null) {
	    		vectorRel2 = new Vectorrelacion();
	    		vectorRel2.setIddefvector(_DEFVECT_REGULACION_PRECEDENTE);
	    		vectorRel2.setRelacion(relacion);
	    		vectorRel2.setValor(padre.getIden());
    		}else
    		{
    			vectorRel2 = new Vectorrelacion();
	    		vectorRel2.setIddefvector(_DEFVECT_REGULACION_PRECEDENTE);
	    		vectorRel2.setRelacion(relacion);
	    		vectorRel2.setValor(0);
    		}
		
			// Persistir 
			log.info("Persistiendo REGULACION-ESPECIFICA, codigo det/nombre: "+det.getCodigo()+"/"+nombre);
			em.persist(relacion);
			em.persist(propiedadRel1);
			em.persist(propiedadRel2);
			em.persist(propiedadRel3);
			em.persist(vectorRel1);
			em.persist(vectorRel2);
			
			countRegEsp++;
			
			// Ahora persistimos los hijos
			if (hijas!=null)
				for(int i=0;i<hijas.getLength();i++)
				{
					if (hijas.item(i).getNodeName().compareTo("REGULACION-ESPECIFICA")==0)
						persistirRegulacionEspecifica(det, hijas.item(i), relacion);
				}
					
			
		} catch (Exception e2) {
			resultado.error("Error desconocido persistiendo REGULACION-ESPECIFICA, codigo det/nombre: "+
				det.getCodigo()+"/"+nombre+", error: "+e2.getMessage());
		}	

	}

	

	/*
	 * 	ESTABLECER GRUPOS DE APLICACION
	 * 
	 * 
	 */
	private void establecerGruposAplicacion() throws EditorFIPException {
		
		log.info("Estableciendo Grupos de aplicación para las determinaciónes creadas...");
		int countGrupoAp=0;

		// Por cada determinación creada ...
		Iterator<?> itr = Utiles.mapDet.entrySet().iterator();
		Map<String,Determinacion> mapDetAux = new HashMap<String,Determinacion>();
	    while(itr.hasNext()) {
	    	
	    	// Variables
	    	@SuppressWarnings("unchecked")
			Map.Entry<String,Determinacion> entry = (Map.Entry<String, Determinacion>) itr.next();
	    	Determinacion det = entry.getValue();
	    	
	    	for(String c : det.gruposAplicaciones) {
		    	
	    		String cods[] = c.split("/");
		    	String codTramite = cods[0];
		    	String codDeterminacion = cods[1];
		    	String codigoComp = codTramite+"/"+codDeterminacion;
			    	
		    	// La determinación pertenece al trámite actual
			    if (det.getTramite().getCodigofip().compareTo(tramite.getCodigofip())==0) {
			    	
			    	// La determinacón tiene grupo de aplicacion ???
			    	if (codTramite!=null) {
				    	try {
				    		
				    		// Crear DeterminacionGrupoEntidad
				    		Determinaciongrupoentidad detGrupoEnt = new Determinaciongrupoentidad();
				    		detGrupoEnt.setDeterminacionByIddeterminacion(det);
				    		
				    		// Si está vacio entonces hay un error en el FIP1
				    		Determinacion d = Utiles.getDeterminacion(codTramite, codDeterminacion, mapDetAux);
							if (d==null) throw new EditorFIPException("No existe la determinación: "+codigoComp);		    		
				    		detGrupoEnt.setDeterminacionByIddeterminaciongrupo(d);
				    					
							// Persistir 
				    		em.persist(detGrupoEnt);
				    		log.info("Persistido DET(id: "+det.getIden()+", cod: "+det.getCodigo()+") GRUPO-APLICACION, codigo: "+codigoComp+" (id: "+d.getIden()+")");
				    		countGrupoAp++;
				    		
				    	} catch (EditorFIPException e1) {
				    		resultado.error(e1.getMessage());
				    	} catch (Exception e2) {
				    		e2.printStackTrace();
				    		resultado.error("Error persistiendo DETERMINACION->GRUPO-APLICACION con codigo: "+
				    			codigoComp+", error: "+e2.getMessage());
				    	}
			    	}
		    	}
	    	}
	  	}
	    
	    Utiles.mapDet.putAll(mapDetAux);
	    
	    // Resultados
	    resultado.contador.put(xmlPath + "/DETERMINACIONES/DETERMINACION/GRUPO-APLICACION", countGrupoAp);
	    resultado.info("Tramite "+tramite.getCodigofip()+", DETERMINACION->GRUPO-APLICACION: "+countGrupoAp);	    
	}
	

	
	
	// -----------------
	
	
	
	
	
	/*
	 * 	MAPEAR UNIDADES
	 * 
	 * 	Coge las unidades del trámite actual y las mapea para
	 *  después usarlo en la importación de determinaciones.
	 * 
	 */
	private void mapearUnidades() throws EditorFIPException {

		log.info("Importando UNIDADES ...");
		NodeList nodes = evaluateXPath(xmlPath + "/" + "UNIDADES/UNIDAD");	
		NodeList children;
		Node node;
		String codDeterminacion;
		
		for (int i = 0; i < nodes.getLength(); i++) {
			
			// Nodo y mapeamos atributos
			node = nodes.item(i);
			attrMap = nodeToMap(node);

			// Nueva unidad
			Unidad unidad = new Unidad();
			unidad.abrev = (String) attrMap.get("abreviatura");
			codDeterminacion = (String) attrMap.get("determinacion");
							
			// Coger el elemento DEFINICION
			children = node.getChildNodes();
			for (int k = 0; k < children.getLength(); k++) {
				if (children.item(k).getNodeName().compareTo("DEFINICION") == 0) {
					log.info("Mapeada unidad, codigo determinacion: "+codDeterminacion);
					unidad.definicion = (String)children.item(k).getFirstChild().getNodeValue();
				}
			}
			
			// Mapeamos
			mapUnidad.put(codDeterminacion, unidad);
		}
		
		// Resultado
		resultado.info("Tramite "+tramite.getCodigofip()+", UNIDADES mapeadas: "+mapUnidad.size());			
	}
	

	
}

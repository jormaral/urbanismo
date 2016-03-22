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

package com.mitc.redes.editorfip.servicios.gestionfip.importarfip;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import org.jboss.seam.log.Log;
import org.jboss.seam.log.Logging;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Casoentidaddeterminacion;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Determinacion;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Documento;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Documentocaso;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Entidad;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Entidaddeterminacion;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Entidaddeterminacionregimen;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Opciondeterminacion;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Plan;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Regimenespecifico;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Tramite;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Vinculocaso;
import com.mitc.redes.editorfip.excepciones.EditorFIPException;



public class ImportadorCondUrbanisticas extends ImportadorBase {

	Log log = Logging.getLog(ImportadorCondUrbanisticas.class);
	
	// Propiedades
	private Tramite tramite;
	private Entidaddeterminacion condUrbanistica;	
	public String xmlPath ="";
	public Plan plan;
	private Resultado resultado;
	
	// Contadores
	private int countCondUrb=0;
	private int countCasos = 0;
	private int countDocCaso = 0;
	private int countDetReg = 0;
	private int countCasoAp = 0;
	private int countOpDet = 0;
	private int countRegEsp = 0;
	
	// Clases de apoyo
	class VinculoCaso {
		Casoentidaddeterminacion casoOrigen;
		String tramite;
		String caso;
	}
	class CasoAplicacion {
		Entidaddeterminacionregimen regimenOrigen;
		String tramite;
		String caso;
	}
	private List<VinculoCaso> vinculoCasoList = new ArrayList<VinculoCaso>();
	private List<CasoAplicacion> casoAplicacionList = new ArrayList<CasoAplicacion>();

	
	// Mapas
	


	
	
	
	// -----------------------------------------------------------------------------
	
	
	
	/*
	 * 	CONSTRUCTOR
	 */
	public ImportadorCondUrbanisticas(
			Document xml, 
			EntityManager em, 
			String xmlPath, 
			Tramite tramite, 
			Resultado resultado
	) {
		super(xml, em);
		debugPrefix = "Cond.Urb.:";
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

		log.info("Importando Condiciones Urbanisticas ...");
		NodeList nodes = evaluateXPath(xmlPath + "/" + "CONDICIONES-URBANISTICAS/CONDICION-URBANISTICA");	
		
		

		// Por cada nodo de CONDICION-URBANISTICA ...
		Node node;
		NodeList children;
		for (int i = 0; i < nodes.getLength(); i++) {
			
			// Nodo y mapeamos atributos
			node = nodes.item(i);
			if (node.getNodeName().compareTo("CONDICION-URBANISTICA") != 0) continue;
			attrMap = nodeToMap(node);

			// Nueva condición urbanística
			condUrbanistica = new Entidaddeterminacion();

			// Parseamos los elementos que tiene en el interior
			children = node.getChildNodes();
			for (int k = 0; k < children.getLength(); k++) {
				
				// ... CODIGO ................................................................
				if (children.item(k).getNodeName().compareTo("CODIGO") == 0) {
		
					Entidad e=null;
					Determinacion d=null;
					
					try {	
						
						// Por cada elemento dentro de CODIGO
						for (int n = 0; n < children.item(k).getChildNodes().getLength(); n++) {
							
							Node nn = children.item(k).getChildNodes().item(n);
							Map<String, Object> attrsCodigo = nodeToMap( nn );

							if (nn.getNodeName().compareTo("ENTIDAD") == 0) {
								
								// Coger Entidad
								String codTramite = (String) attrsCodigo.get("tramite");
								String codEntidad = (String) attrsCodigo.get("entidad");
								e = Utiles.getEntidad(codTramite,codEntidad);						
								if (e==null) throw new EditorFIPException("No se encuentra la Entidad: "+codTramite+"/"+codEntidad);
								condUrbanistica.setEntidad(e);	
							}
							if (nn.getNodeName().compareTo("DETERMINACION") == 0) {
								
								// Coger Determinación
								String codTramite = (String) attrsCodigo.get("tramite");
								String codDet = (String) attrsCodigo.get("determinacion");
								d = Utiles.getDeterminacion(codTramite,codDet);
								if (d==null) throw new EditorFIPException("No se encuentra la Determinación: "+codTramite+"/"+codDet);
								condUrbanistica.setDeterminacion(d);						
							}				
						}
						
						// Persistir
						if (e!=null && d!=null) {
							em.persist(condUrbanistica);
							//em.flush();
							log.info("Persistida CONDICIONES-URBANISTICAS, (entidad/determinacion): " + e.getCodigo()+"/"+d.getCodigo() +"  // ID-BaseDatos Persistida (iden/entidad/determinacion)="+condUrbanistica.getIden()+"/"+condUrbanistica.getEntidad().getIden()+"/"+condUrbanistica.getDeterminacion().getIden());
							countCondUrb++;
						}
							
					} catch (EditorFIPException e1) {
			    		resultado.error(e1.getMessage());	
			    		e1.printStackTrace();
					} catch (Exception ex) {
						resultado.error("Error persistiendo CONDICION-URBANISTICA, error: "+ex.getMessage());
						ex.printStackTrace();
					}
				}
				
				
				// ... CASOS ................................................................
				if (children.item(k).getNodeName().compareTo("CASOS") == 0) {

					persistirCasos(children.item(k));
				}			
			}
		}
		
		
		// Vincular CASOS
		int countVinculoCasos = 0;
		for(VinculoCaso vc : vinculoCasoList) {
			
			// Coger el caso al que vincula
			Casoentidaddeterminacion c = Utiles.mapCasos.get(vc.tramite+"/"+vc.caso);
			
			if (c==null) {
				resultado.error("El caso "+vc.casoOrigen.getCodigo()+" está vinculado a "+vc.tramite+"/"+vc.caso+" que no existe");
			} else {
				
				try {
					
					// Crear vinculo y persistir
					Vinculocaso vinculo = new Vinculocaso();
					vinculo.setCasoentidaddeterminacionByIdcaso(vc.casoOrigen);
					vinculo.setCasoentidaddeterminacionByIdcasovinculado(c);			
					em.persist(vinculo);
					//em.flush();
					countVinculoCasos++;
				
				} catch (Exception e) {
					
					resultado.error("Error al persistir el CASO "+vc.casoOrigen.getCodigo()+" con "+vc.tramite+"/"+vc.caso);
					e.printStackTrace();
				}
			}	
		}
		
		// Casos de aplicación
		for(CasoAplicacion ca : casoAplicacionList) {
			
			// Coger caso		
			Casoentidaddeterminacion casoAp = Utiles.mapCasos.get(ca.tramite+"/"+ca.caso);
			
			// Si no existe error, si existe pues añadirla
			if (casoAp==null) 
				resultado.error("CASO->REGIMEN->CASO-APLICACION, el caso "+ca.tramite+"/"+ca.caso+" no existe.");
			else {
				
				try {
				// Persistir
				ca.regimenOrigen.setCasoentidaddeterminacionByIdcasoaplicacion(casoAp);
				em.persist(ca.regimenOrigen);
				//em.flush();
				countCasoAp++;
				} catch (Exception e) {
					
					resultado.error("Error al persistir el CASO ca.regimenOrigen"+ca.regimenOrigen);
					e.printStackTrace();
				}
			}
		}
		
		// Contadores
		resultado.contador.put(xmlPath + "/CONDICIONES-URBANISTICAS/CONDICION-URBANISTICA", countCondUrb);
		resultado.contador.put(xmlPath + "/CONDICIONES-URBANISTICAS/CONDICION-URBANISTICA/CASOS", countCasos);
		resultado.contador.put(xmlPath + "/CONDICIONES-URBANISTICAS/CONDICION-URBANISTICA/CASO/DOCUMENTOS", countDocCaso);
		resultado.contador.put(xmlPath + "/CONDICIONES-URBANISTICAS/CONDICION-URBANISTICA/CASO/VINCULOS", countVinculoCasos);
		resultado.contador.put(xmlPath + "/CONDICIONES-URBANISTICAS/CONDICION-URBANISTICA/CASO/REGIMEN", countDetReg);
		resultado.contador.put(xmlPath + "/CONDICIONES-URBANISTICAS/CONDICION-URBANISTICA/CASO/REGIMEN/CASO-APLICACION", countCasoAp);
		resultado.contador.put(xmlPath + "/CONDICIONES-URBANISTICAS/CONDICION-URBANISTICA/CASO/REGIMEN/VALOR-REFERENCIA", countOpDet);
		resultado.contador.put(xmlPath + "/CONDICIONES-URBANISTICAS/CONDICION-URBANISTICA/CASO/REGIMEN/REGIMEN-ESPECIFICO", countRegEsp);
		
	    // Resultados
	    resultado.info("Tramite "+tramite.getCodigofip()+", CONDICIONES-URBANISTICAS: "+countCondUrb);	  
	    resultado.info("Tramite "+tramite.getCodigofip()+", CONDICIONES-URBANISTICAS->CASOS: "+countCasos);	 
	    resultado.info("Tramite "+tramite.getCodigofip()+", CONDICIONES-URBANISTICAS->CASO->DOCUMENTOS: "+countDocCaso);	
	    resultado.info("Tramite "+tramite.getCodigofip()+", CONDICIONES-URBANISTICAS->CASO->VINCULOS: "+countVinculoCasos);	
	    resultado.info("Tramite "+tramite.getCodigofip()+", CONDICIONES-URBANISTICAS->CASO->REGIMEN: "+countDetReg);	
	    resultado.info("Tramite "+tramite.getCodigofip()+", CONDICIONES-URBANISTICAS->CASO->REGIMEN->CASO-APLICACION: "+countCasoAp);	
	    resultado.info("Tramite "+tramite.getCodigofip()+", CONDICIONES-URBANISTICAS->CASO->REGIMEN->VALOR-REFERNCIA, OPCION-DETERMINACION: "+countOpDet);	
	    resultado.info("Tramite "+tramite.getCodigofip()+", CONDICIONES-URBANISTICAS->CASO->REGIMEN->REGIMEN-ESPECIFICO: "+countRegEsp);
	}	
	
	
	/*
	 *  PERSISTIR CASOS
	 * 
	 * 	De una lista de casos los persiste.
	 * 
	 */
	private void persistirCasos(Node casosNode) throws EditorFIPException {
		
		// Vars
		String nombreCaso = null;
		String codigoCaso = null;		
		Map<String, Object> attrs;
		
		// Por cada elemento CASO
		for (int u = 0; u < casosNode.getChildNodes().getLength(); u++) {
			
			Node casoNode = casosNode.getChildNodes().item(u);
			
			try {
				
				// Atributos del CASO
				attrs = nodeToMap( casosNode.getChildNodes().item(u));
				nombreCaso = (String) attrs.get("nombre");
				codigoCaso = (String) attrs.get("codigo");
				
				// Nuevo objeto CASO
				Casoentidaddeterminacion caso = new Casoentidaddeterminacion();
				caso.setEntidaddeterminacion(condUrbanistica);
				caso.setCodigo(codigoCaso);
				caso.setNombre(nombreCaso);
				
				// Persistir CASO
				em.persist(caso);
				//em.flush();
				Utiles.mapCasos.put(tramite.getCodigofip()+"/"+codigoCaso, caso);
				countCasos++;
				
				List<Node> regEspNodeList = new ArrayList<Node>();
				
				// Elementos dentro de CASO
				for (int k = 0; k < casoNode.getChildNodes().getLength(); k++) {
					
					NodeList childrenCaso = casoNode.getChildNodes();
					
					// REGIMENES ...........................................................................
					if (childrenCaso.item(k).getNodeName().compareTo("REGIMENES") == 0) {
						
						for (int n = 0; n < childrenCaso.item(k).getChildNodes().getLength(); n++) {
							Node nn = childrenCaso.item(k).getChildNodes().item(n);
							if (nn.getNodeName().compareTo("REGIMEN") == 0) {
							
								// Creamis objeto para el nuevo REGIMEN
								Entidaddeterminacionregimen condUrbRegimen = new Entidaddeterminacionregimen();
								condUrbRegimen.setCasoentidaddeterminacionByIdcaso(caso);
								
								Determinacion detReg = null;
								Determinacion valorRefDet = null;
								for (int t = 0; t < nn.getChildNodes().getLength(); t++) {
									
									Node regimenNode = nn.getChildNodes().item(t);
									
									// COMENTARIO
									if (regimenNode.getNodeName().compareTo("COMENTARIO") == 0) {
										// no esta en tabla
									}
									

									// REGIMENES-ESPECIFICOS
									if (regimenNode.getNodeName().compareTo("REGIMENES-ESPECIFICOS") == 0) {
										 
										// Por cada elemento dentro de REGULACIONES-ESPECIFICOS
										for (int r = 0; r < regimenNode.getChildNodes().getLength(); r++) {
																										
											if (regimenNode.getChildNodes().item(r).getNodeName().compareTo("REGIMEN-ESPECIFICO")==0) {																				
												regEspNodeList.add(regimenNode.getChildNodes().item(r));								
											}
										}
									}
									
									
									// VALOR
									if (regimenNode.getNodeName().compareTo("VALOR") == 0) {
										//condUrbRegimen.setValor(regimenNode.getNodeValue());
										condUrbRegimen.setValor(regimenNode.getTextContent());
									}
									
									// DETERMINACION-REGIMEN
									if (regimenNode.getNodeName().compareTo("DETERMINACION-REGIMEN") == 0) {
										
										// Coger determinación
										Map<String, Object> attrsDR = nodeToMap( regimenNode );
										String codTramite = (String) attrsDR.get("tramite");
										String codDeterminacion = (String) attrsDR.get("determinacion");									
										detReg = Utiles.getDeterminacion(codTramite, codDeterminacion);
									}
									
									// VALOR-REFERENCIA
									// Guardamos la determinación para al final, junto con la 
									// determinacion-regimen cojamos el OpcionDeterminacion
									if (regimenNode.getNodeName().compareTo("VALOR-REFERENCIA") == 0) {
										
										Map<String, Object> attrsVR = nodeToMap( regimenNode );
										valorRefDet = Utiles.getDeterminacion(
												(String) attrsVR.get("tramite"),
												(String) attrsVR.get("determinacion")
										);
										
										if (valorRefDet==null) throw new EditorFIPException("CASO->VALOR-REFERENCIA, Determinación no encontrada: "
												+ (String) attrsVR.get("tramite") + "/" + (String) attrsVR.get("determinacion"));
									}
									
									// CASO-APLICACION
									if (regimenNode.getNodeName().compareTo("CASO-APLICACION") == 0) {
										
										// Guradar para procesarlo después
										Map<String, Object> attrsDR = nodeToMap( regimenNode );
										CasoAplicacion casoAp = new CasoAplicacion();
										casoAp.regimenOrigen = condUrbRegimen;
										casoAp.tramite = (String) attrsDR.get("tramite");
										casoAp.caso = (String) attrsDR.get("caso");
										casoAplicacionList.add(casoAp);
										
									}
								}
								
								// Vemos si tenemos DETERMINACION-REGIMEN y VALOR-REFERENCIA
								if (valorRefDet!=null) {
											
									// Si no tenemos Determinacion Regimen cogemos 
									String codTramite, codDet;
									if (detReg==null) {
										
										codTramite = condUrbanistica.getDeterminacion().getTramite().getCodigofip();
										codDet = condUrbanistica.getDeterminacion().getCodigo();
										
									} else {
										codTramite = detReg.getTramite().getCodigofip();
										codDet = detReg.getCodigo();
									}
									
									Opciondeterminacion opDet = Utiles.getOpcionDeterminacion(
										codTramite,
										codDet,
										valorRefDet.getTramite().getCodigofip(),
										valorRefDet.getCodigo()
									);
									String codCompOpDet = 
											codTramite + "/" + 
											codDet + "/" + 
											valorRefDet.getTramite().getCodigofip() + "/" + 
											valorRefDet.getCodigo();
									if (opDet==null) 
										resultado.error("CASO->VALOR-REFERENCIA, OPCION-DETERMINACION no encontrada: "+codCompOpDet); 
									else {
										condUrbRegimen.setOpciondeterminacion(opDet);
										countOpDet++;
									}
								}
								
								// Persistir REGIMEN
								try {
									em.persist(condUrbRegimen);
									//em.flush();
									countDetReg++;
								} catch (Exception e) {								
									resultado.error("Error al persistir COND-URB->CASO->REGIMEN");
									e.printStackTrace();
								}
								
								// Después de persistir guardamos los REGIMENES-ESPECIFICOS
								for(Node regEspNode:regEspNodeList) {
									persistirRegimenEspecifico(condUrbRegimen, regEspNode, null);
								}
							}
						}
					}
					
					
					// VINCULOS ...........................................................................
					if (childrenCaso.item(k).getNodeName().compareTo("VINCULOS") == 0) {
						
						for (int n = 0; n < childrenCaso.item(k).getChildNodes().getLength(); n++) {
							Node nn = childrenCaso.item(k).getChildNodes().item(n);
							if (nn.getNodeName().compareTo("VINCULO") == 0) {
								
								// Vamos guardando los vínculos para después
								VinculoCaso vinculoCaso = new VinculoCaso();
								vinculoCaso.casoOrigen = caso;
								vinculoCaso.tramite = (String) nn.getAttributes().item(1).getNodeValue();
								vinculoCaso.caso = (String) nn.getAttributes().item(0).getNodeValue();
								vinculoCasoList.add(vinculoCaso);
							}
						}
					}
					
					// DOCUMENTOS ..........................................................................
					if (childrenCaso.item(k).getNodeName().compareTo("DOCUMENTOS") == 0) {
						
						for (int n = 0; n < childrenCaso.item(k).getChildNodes().getLength(); n++) {
							Node nn = childrenCaso.item(k).getChildNodes().item(n);
							if (nn.getNodeName().compareTo("DOCUMENTO") == 0) {
								
								// Código documento
								String codDoc = (String) nn.getAttributes().item(0).getNodeValue();
								
								// Coger documento
								Documento doc = Utiles.getDocumento(tramite.getCodigofip(), codDoc);
								
								if (doc!=null)
								{
									// Nuevo objeto documento de caso
									Documentocaso docCaso = new Documentocaso();
									docCaso.setCasoentidaddeterminacion(caso);
									docCaso.setDocumento(doc);

									// Persistir
									try {
										log.info("Persistiendo CASO->DOCUMENTO, código doc: " + codDoc);
										em.persist(docCaso);
										//em.flush();
										countDocCaso++;
									} catch (Exception e) {
										resultado.error("Error persistiendo CASO->DOCUMENTO con codigo doc: " + codDoc);
										e.printStackTrace();
									}
								}
								else
								{
									// No ha encontrado el documento. Informo de este hecho. El FIP.XML es erroneo
									resultado.error("Error persistiendo CASO->DOCUMENTO con codigo doc: " + codDoc +" No existe dicho documento en el FIP.xml y no se ha asociado");
								}

								
							}
						}
					}
				}	
			
			} catch (Exception ex) {
				resultado.error("Error desconocido persistiendo CONDICION-URBANISTICA->CASO: "+codigoCaso+", error: "+ex.getMessage());
				ex.printStackTrace();
				throw new EditorFIPException("Error desconocido persistiendo CONDICION-URBANISTICA->CASO: "+codigoCaso+", error: "+ex.getMessage());
				
			}
		}	
	}
	
	/*
	 * 	PERSISTIR REGIMEN ESPECÍFICA
	 * 
	 */
	private void persistirRegimenEspecifico(Entidaddeterminacionregimen entDetReg, Node node, Regimenespecifico padre) {
		
		String idPadre="null";
		
		if (padre!=null) idPadre = padre.getIden()+"";
		log.info("Se van a Persistir REGIMENES-ESPECIFICOS de entDetReg.iden="+entDetReg.getIden()+ " padre RE="+idPadre);
		
		// Vars
		String texto = "", nombre = "";
		int orden;
		
		
		try {
			
			// Coger todos los datos del nodo		
			Map<String, Object> attrs = nodeToMap( node );
			nombre = (String) attrs.get("nombre");
			orden = Integer.parseInt((String) attrs.get("orden"));
			NodeList hijas = null;
			
			// Recorrer las etiquetas interiores
			log.info("REGIMEN-ESPECIFICO, nombre: "+nombre+", subetiquetas: " + node.getChildNodes().getLength());
			for(int k=0;k<node.getChildNodes().getLength();k++) {
				
				//log.info("Etiqueta: "+node.getChildNodes().item(k).getNodeName());
				
				// TEXTO
				if (node.getChildNodes().item(k).getNodeName().compareTo("TEXTO")==0) {
					//texto = node.getChildNodes().item(k).getNodeValue();
					texto = node.getChildNodes().item(k).getTextContent();
				}
				
				// HIJAS
				if (node.getChildNodes().item(k).getNodeName().compareTo("HIJAS")==0)
					hijas = node.getChildNodes().item(k).getChildNodes();
			}
			if (texto==null) texto="";
			
			// Crear regimen especifico
			Regimenespecifico regEsp = new Regimenespecifico();
			regEsp.setNombre(nombre);
			regEsp.setOrden(orden);
			regEsp.setTexto(texto);
			regEsp.setRegimenespecifico(padre);
			regEsp.setEntidaddeterminacionregimen(entDetReg);

			// Persistir 
			log.info("Persistiendo REGIMEN-ESPECIFICO, nombre: " + nombre );
			em.persist(regEsp);
			//em.flush();
			countRegEsp++;
			
			// Ahora persistimos los hijos
			if (hijas!=null)
				
					for(int i=0;i<hijas.getLength();i++)
					{
						if (hijas.item(i).getNodeName().compareTo("REGIMEN-ESPECIFICO")==0)
							persistirRegimenEspecifico(entDetReg, hijas.item(i), regEsp);
					}
					
			
		} catch (Exception e2) {
			resultado.error("Error desconocido persistiendo REGIMEN-ESPECIFICA, nombre: "+nombre+" error: "+e2.getMessage());
			e2.printStackTrace();
		}	
	}

}

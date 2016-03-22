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



import java.io.File;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.ejb.Remove;
import javax.ejb.SessionContext;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.async.Asynchronous;
import org.jboss.seam.faces.FacesManager;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.international.StatusMessage.Severity;
import org.jboss.seam.log.Log;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import com.mitc.redes.editorfip.entidades.rpm.gestionfip.FipsCargados;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Plan;
import com.mitc.redes.editorfip.servicios.basicos.diccionario.ServicioBasicoAmbitos;

@Stateful
@Scope(ScopeType.SESSION)
@Name ("gestionImportacionFIP1")
public class GestionImportacionFIP1Bean implements GestionImportacionFIP1{

	
	
	@In
	EntityManager entityManager;
	
	@In(create=true) FacesMessages facesMessages;

	@Logger private Log log;
	
	
	@In(create = true, required = false)
	ImportadorFIP importadorFIP;
	
	@In(create = true, required = false)
	ServicioBasicoAmbitos servicioBasicoAmbitos;
	
	
	
	// Creacion del contexto
    @Resource
    private SessionContext contextoTransaccion;
    
    private Integer idPlanBaseImportarFIP = null;
    
    private Boolean existePlanEncargado=null;
    
    public void reiniciarValores()
    {
    	log.info("[reiniciarValores] Reiniciando idPlanBaseImportarFIP y existePlanEncargado");
    	
    	idPlanBaseImportarFIP = null;
    	existePlanEncargado=null;
    	
    }


    public boolean comprobarExistePlanEncargado (Long idFipCargado)
    {
    	
    	if (existePlanEncargado==null)
    	{
    		boolean resultado = true;
        	log.info("[comprobarExistePlanEncargado]  idFipCargado="+idFipCargado);
        	
        	try { 
    			
        		Document xmlDoc = null;
        		
    			// Obtener el fip encargado seleccionado
        		FipsCargados fipCargado = entityManager.find(FipsCargados.class, idFipCargado);
    			
    			// Obtener el fichero XML con el FIP1
    			String ruta = fipCargado.getRuta() + File.separator + fipCargado.getIdentificador();
    			
    			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
    			DocumentBuilder docBuilder;
    			docBuilder = docBuilderFactory.newDocumentBuilder();	
    			xmlDoc = docBuilder.parse(new File(ruta));
    			xmlDoc.getDocumentElement().normalize();
    			log.info("[comprobarExistePlanEncargado] Leyendo fichero FIP1 (xml): OK"); 
    			
    			ImportadorBase impBase = new ImportadorBase(xmlDoc,null);
    			
    			log.info("Buscando en el xml nodo FIP ...");
    	    	NodeList nodeFIP = impBase.evaluateXPath("//FIP");
    	    	// Si tenemos fip ...
    			if (nodeFIP.getLength()==0) {
    				
    				log.info("[comprobarExistePlanEncargado] No hay FIP en el fichero XML.");
    				// Mostrar en pantalla el resultado
    				facesMessages.addFromResourceBundle("No hay FIP en el fichero XML.", null);
    				resultado = false;
    				
    			} else {
    				
    								
    				log.info("[comprobarExistePlanEncargado] Buscando en el xml nodo //PLANEAMIENTO-ENCARGADO ...");
    		    	NodeList nodes = impBase.evaluateXPath("//PLANEAMIENTO-ENCARGADO");
    				log.info("[comprobarExistePlanEncargado] Nodos encontrados: " + nodes.getLength());	
    		    	
    		        
    				// Si tenemos algún plan encargado ...
    				if (nodes.getLength()==0) {
    					
    					log.info("[comprobarExistePlanEncargado] No hay plan encargado en el fichero XML.");			
    					resultado = false;
    					
    				}
    				
    				
    			}
    			
        	} catch(Exception ie) {
    	    	log.error("[comprobarExistePlanEncargado] Error en la comprobacion si existe plan encargado: " + ie.getMessage());
    	    	
    	    	ie.printStackTrace();
    	    } 
        	
        	
        	existePlanEncargado= resultado;
    	}
    	else
    	{
    		log.info("[comprobarExistePlanEncargado] Asignado previamente");
    	}
    	
    	log.info("[comprobarExistePlanEncargado] existePlanEncargado="+existePlanEncargado);
    	return existePlanEncargado;
    	
    }

    public int idPlanBaseImportarFIP(Long idFipCargado)
    {
    	if (idPlanBaseImportarFIP==null)
    	{
    		
        	
        	log.info("[comprobarExistePlidPlanBaseImportarFIPanEncargado]  idFipCargado="+idFipCargado);
        	
        	// Por defecto lo pongo a cero
        	idPlanBaseImportarFIP = new Integer(0);
        	
        	try { 
    			
        		Document xmlDoc = null;
        		
    			// Obtener el fip encargado seleccionado
        		FipsCargados fipCargado = entityManager.find(FipsCargados.class, idFipCargado);
    			
    			// Obtener el fichero XML con el FIP1
    			String ruta = fipCargado.getRuta() + File.separator + fipCargado.getIdentificador();
    			
    			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
    			DocumentBuilder docBuilder;
    			docBuilder = docBuilderFactory.newDocumentBuilder();	
    			xmlDoc = docBuilder.parse(new File(ruta));
    			xmlDoc.getDocumentElement().normalize();
    			log.info("[idPlanBaseImportarFIP] Leyendo fichero FIP1 (xml): OK"); 
    			
    			ImportadorBase impBase = new ImportadorBase(xmlDoc,null);
    			
    			log.info("[idPlanBaseImportarFIP] Buscando en el xml nodo FIP ...");
    	    	NodeList nodeFIP = impBase.evaluateXPath("//FIP");
    	    	// Si tenemos fip ...
    			if (nodeFIP.getLength()==0) {
    				
    				log.info("[idPlanBaseImportarFIP] No hay FIP en el fichero XML.");
    				// Mostrar en pantalla el resultado
    				facesMessages.addFromResourceBundle("No hay FIP en el fichero XML.", null);
    				idPlanBaseImportarFIP = new Integer(0);
    				
    			} else {
    				
    				// Coger datos del trámite
    		    	NodeList nodes = impBase.evaluateXPath("//PLANEAMIENTO-VIGENTE");
    		    	
    		    	// Si tenemos plan vigente
    		    	if (nodes.getLength()>0) {
    		    		
    		    		// Mapeamos atributos del nodo
    			    	Map<String, Object> attrMap = impBase.nodeToMap( nodes.item(0) );
    			    	int idAmbito = Integer.parseInt( (String) attrMap.get("ambito"));
    			    	
    			    	log.info("[idPlanBaseImportarFIP] idAmbito="+idAmbito);
    			    	// Ya tenemos el idAmbito, tenemos que ver si anteriormente hay en la BD un plan con ese idAmbito
    			    	// En caso de que lo haya, tenemos que ver cual es el planbase de ese plan, con lo que ya no tendriamos que importar el plan base
    			    	
    			    	String queryPlanAmbito = " select plan from Plan plan where plan.idambito="+idAmbito;
    			    	
    			    	try
    			    	{
    			    		List<Plan> planListadoAmbito = (List<Plan>) entityManager.createQuery(queryPlanAmbito).getResultList();
    			    		
    			    		for (Plan planAmb : planListadoAmbito)
    			    		{
    			    			if (planAmb.getPlanByIdplanbase()!=null)
    			    			{
    			    				idPlanBaseImportarFIP = new Integer (planAmb.getPlanByIdplanbase().getIden());
    			    				log.info("[idPlanBaseImportarFIP] Hay algun plan en la BD el mismo idAmbito que el FIP y con Plan Base. No se importara el Plan Base sino que se asociara al Plan Base Existente con iden="+idPlanBaseImportarFIP);
    			    				
    			    			}
    			    		}
    			    		
    			    	}
    			    	catch (NoResultException e) {
    			            log.info("[idPlanBaseImportarFIP] No se ha encontrado el listado de planes un plan con ese idambito." + e.getMessage());
    			            
    			            fipCargado.setEstado("ERRORES");
    			    		fipCargado.setResultado("No se ha encontrado el listado de planes un plan con ese idambito." + e.getMessage());
    			    		entityManager.merge(fipCargado);
    			    		facesMessages.addFromResourceBundle(Severity.ERROR, "Error al importar el fip1. Consulte la informacion de los errores en el Listado de FIP1 Cargados");
    						FacesManager.instance().redirect("/error.xhtml");

    			        } catch (Exception ex) {
    			            log.error("[idPlanBaseImportarFIP] Error en la busqueda del listado de ambitos: " + ex.getMessage());
    			            ex.printStackTrace();
    			            
    			            
    		    	    	fipCargado.setEstado("ERRORES");
    			    		fipCargado.setResultado("Error en la busqueda del listado de ambitos: " + ex.getMessage());
    			    		entityManager.merge(fipCargado);
    			    		facesMessages.addFromResourceBundle(Severity.ERROR, "Error al importar el fip1. Consulte la informacion de los errores en el Listado de FIP1 Cargados");
    						FacesManager.instance().redirect("/error.xhtml");

    			        }
    			    	
    			    	
    			    	
    		    	}	
    				
    				
    			}
    			
        	} catch(Exception ie) {
    	    	log.error("[comprobarExistePlanEncargado] Error en la comprobacion si existe plan encargado: " + ie.getMessage());
    	    	
    	    	ie.printStackTrace();
    	    	// Obtener el fip encargado seleccionado
    	    	FipsCargados fipCargado = entityManager.find(FipsCargados.class, idFipCargado);
    	    	fipCargado.setEstado("ERRORES");
	    		fipCargado.setResultado("Error en la comprobacion si existe plan encargado: " + ie.getMessage());
	    		entityManager.merge(fipCargado);
			   
				
				facesMessages.addFromResourceBundle(Severity.ERROR, "Error al importar el fip1. Consulte la informacion de los errores en el Listado de FIP1 Cargados");
				FacesManager.instance().redirect("/error.xhtml");
    	    } 
    	}
    	else
    	{
    		log.info("[idPlanBaseImportarFIP] Asignado previamente");
    	}
    	
    	
    	
    	log.info("[idPlanBaseImportarFIP] idPlanBaseImportarFIP="+idPlanBaseImportarFIP);
    	return idPlanBaseImportarFIP;
    }
    
    @Asynchronous
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void importacionFIP(Long idFipCargado) { 
		
		FipsCargados fipCargado = new FipsCargados();
		
		log.info("[importacionFIP]  idFipCargado="+idFipCargado );
		
		reiniciarValores();
	    
		try { 
			
			/*
			int idPlanBase = idPlanBaseImportarFIP(idFipCargado);
			log.info("[importacionFIP]  idPlanBase="+idPlanBase);
			*/
			// FGA: Lo pongo a cero para que siempre se importe el plan base
			int idPlanBase = 0;
			
			boolean planeamientoEncargado = comprobarExistePlanEncargado(idFipCargado);
			log.info("[importacionFIP]  planeamientoEncargado en FIP.xml="+planeamientoEncargado);
			
			// Obtener el fip encargado seleccionado
			fipCargado = entityManager.find(FipsCargados.class, idFipCargado);
			
			// Obtener el fichero XML con el FIP1
			String ficheroXMLFIP1 = fipCargado.getRuta() + File.separator + fipCargado.getIdentificador();
			log.info("[importacionFIP]  Fichero FIP1...: " + ficheroXMLFIP1);

			// Iniciamos importaci—n ...
			log.info("[importacionFIP]  Iniciamos importaci—n, creando objeto ImportadorFIP1 ... ");
			
			
			String ruta = fipCargado.getRuta() + File.separator + fipCargado.getIdentificador();
			log.info("[importacionFIP]  Iniciando importaci—n, ruta: " + ruta);
		    String res = importadorFIP.importar(ruta,idFipCargado,idPlanBase,planeamientoEncargado);
		    
		    if (res.contains("ERRORES"))
		    {
		    	log.error("[importacionFIP] Se han producido ERRORES en la importacion del FIP1. Hago un rollback y no se persiste");
		    	contextoTransaccion.setRollbackOnly();
				
	    		fipCargado.setEstado("ERRORES");
	    		fipCargado.setResultado(res);
	    		entityManager.merge(fipCargado);
			   
				
				facesMessages.addFromResourceBundle(Severity.ERROR, "Error al importar el fip1. Consulte la informacion de los errores en el Listado de FIP1 Cargados");
				FacesManager.instance().redirect("/error.xhtml");
		    }
		    else
		    {
		    	 // Resultado
			    log.info("[importacionFIP]  Resultado de la importaci—n: Importacion con Exito");
			    fipCargado.setConsolidado(true);
			    fipCargado.setEstado("IMPORTADO");
			    fipCargado.setResultado(res+"\n IMPORTADO CON EXITO");
			    
			    entityManager.merge(fipCargado);
			   
			    
			    //servicioBasicoAmbitos.cargarAmbitoShpConGeometriaAmbitoTramite(fipCargado.)
			    
			    // Mostrar en pantalla el resultado
				facesMessages.addFromResourceBundle("elemento_importadook", null);
		    }
		    
		    
		   
			
	       
	    } catch(Exception ie) {
	    	log.error("[importacionFIP] Error en la importaci—n: " + ie.getMessage());
	    	log.error("[importacionFIP] ERROR: Hago un rollback");
	    	contextoTransaccion.setRollbackOnly();
			
	    	fipCargado = entityManager.find(FipsCargados.class, idFipCargado);
    		fipCargado.setEstado("ERRORES");
    		fipCargado.setResultado("ERRORES IMPORTANDO\n"+ie.getLocalizedMessage() +"\n"+ ie.getMessage());
    		entityManager.merge(fipCargado);
		   
			
			facesMessages.addFromResourceBundle(Severity.ERROR, "Error al consolidar el fip.  Consulte la informacion de los errores en el Listado de FIP1 Cargados");
			
	    	ie.printStackTrace();
	    } 
	    
	    log.info("[importacionFIP] Fin");
	} 
	


	
	
	
	
	@Remove 
	public void remove() {		
	}
}
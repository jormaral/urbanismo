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

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.log.Log;
import org.jboss.seam.log.Logging;
import org.jboss.seam.transaction.Transaction;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import bsh.ParseException;

import com.mitc.redes.editorfip.entidades.rpm.gestionfip.FipsCargados;
import com.mitc.redes.editorfip.entidades.rpm.gestionfip.PlanesEncargados;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Plan;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Tramite;
import com.mitc.redes.editorfip.excepciones.EditorFIPException;
import com.mitc.redes.editorfip.servicios.basicos.fip.tramites.ServicioBasicoTramites;
import com.mitc.redes.editorfip.servicios.gestionfip.obtencionfip.GestionPlanEncargado;
import com.mitc.redes.editorfip.servicios.gestionfip.obtencionfip.GestionPlanEncargadoBean;
import com.mitc.redes.editorfip.servicios.gestionfip.obtencionfip.LogImportacion;

@Stateless
@Name("importadorFIP")
public class ImportadorFIPBean implements ImportadorFIP
{
	
    //@In EntityManager entityManager;
    @Logger private Log log;
    
    //private Log  log = Logging.getLog(ImportadorFIPBean.class);
    private EntityManager entityManager;
    private Document xmlDoc = null;
    private Resultado resultado;
    private FipsCargados fipCargado;
    
  
    
    @In (create = true, required = false)
    GestionPlanEncargado gestionPlanEncargado;
    
    @In (create = true, required = false)
	 LogImportacion logImportacion;

    /**
     * 	CONSTRUCTOR
     * 
     * @param fipCargado
     * @param entityManager
     */
    /*
    public ImportadorFIPBean(FipsCargados fipCargado, EntityManager entityManager) {
    	super();
    	
    	this.entityManager = entityManager;
    	resultado = new Resultado(fipCargado, entityManager);
    	this.fipCargado = fipCargado;
    }
    */
	
    public void incializar(FipsCargados fipCargado, EntityManager entityManager) {
    	
    	
    	this.entityManager = entityManager;
    	resultado = new Resultado(fipCargado, entityManager);
    	this.fipCargado = fipCargado;
    }
	

	/**
     * 	IMPORTAR FICHERO FIP1
     * 
     * 	Importar desde un fichero el FIP1 a la base de datos
     * 
     */
    public Resultado importar(String file) throws EditorFIPException 
    {
    	return importar(file, null, "", "");
    }
    // .......................................
    public Resultado importar(String file, Integer planBaseIden, String codigoPlanBase, String codigoPlanVigente) throws EditorFIPException
    {
    	
    	resultado.info("----------------------------------------------------");
		resultado.info("  IMPORTANDO FIP DE TIPO 1 ");
		resultado.info("");
		resultado.info("    . Inicio importacion: " + new Date());
		resultado.info("    . ID de Plan base: " + planBaseIden);
		resultado.info("----------------------------------------------------");
		resultado.info("");
		
		logImportacion.anadirLinea("----------------------------------------------------");
		logImportacion.anadirLinea("  IMPORTANDO FIP DE TIPO 1 ");
		logImportacion.anadirLinea("");
		logImportacion.anadirLinea("    . Inicio importacion: " + new Date());
		logImportacion.anadirLinea("    . ID de Plan base: " + planBaseIden);
		logImportacion.anadirLinea("----------------------------------------------------");
		logImportacion.anadirLinea("");
		
		
    	
    	// Configurar Utiles
		Utiles.limpiar();
    	Utiles.em = entityManager;
    	
        // Cargar y abrir fiechero XML
		try {
			
			// Cargar fichero XML
			resultado.info("Leyendo fichero FIP1 (xml): " + file); 
			logImportacion.anadirLinea("Leyendo fichero FIP1 (xml): " + file); 
			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder;
			docBuilder = docBuilderFactory.newDocumentBuilder();	
			xmlDoc = docBuilder.parse(new File(file));
			xmlDoc.getDocumentElement().normalize();
			resultado.info("Leyendo fichero FIP1 (xml): OK"); 
			logImportacion.anadirLinea("Leyendo fichero FIP1 (xml): OK"); 
			
			// Cambiar el estado a CONSOLIDANDO
			fipCargado.setEstado("IMPORTANDO");
			fipCargado.setFechaInicio(new Date());
			entityManager.merge(fipCargado);
			entityManager.flush();
			resultado.info("FIP encargado, estado a IMPORTANDO");
			logImportacion.anadirLinea("FIP encargado, estado a IMPORTANDO");
			Transaction.instance().commit();
			Transaction.instance().begin();
			entityManager.joinTransaction();
			
	        // -----| DICCIONARIO |-----
			//log.debug("**********************************************");
			//log.debug("  DICCIONARIO ");
			//log.debug("**********************************************");
	        //log importadorDiccionario = new ImportadorDiccionario(xmlDoc, em);
	        //log.importar();
	        
			
			// -----| CATALOGO SISTEMATIZADO |-----
			// Si tenemos el parámetro planBaseIden a null entonces importamos el CS que sería
			// el plan base. De otro modo no necesitamos importar el CS.
			
			String query = "SELECT tram " +
	        " FROM Tramite tram where LOWER(tram.codigofip) like '" + codigoPlanBase.toLowerCase() + "'";
			Plan planBase = new Plan();
			
	        List<Tramite> lista = new ArrayList<Tramite>();
	        lista = (List<Tramite>) entityManager.createQuery(query).getResultList();
	    	if (lista!=null && lista.size()>0)
	    		planBase = lista.get(0).getPlan();
	    	else
	    		planBase = null;
	    	
			if (planBase == null) {
				log.info("");
				log.info("----------------------------------------------------");
				log.info("  IMPORTANDO CATÁLOGO SISTEMATIZADO ");
				log.info("----------------------------------------------------");
				log.info("");
				resultado.info("Importando Catalogo Sistematizado ....");
				
				logImportacion.anadirLinea("");
				logImportacion.anadirLinea("----------------------------------------------------");
				logImportacion.anadirLinea("  IMPORTANDO CATÁLOGO SISTEMATIZADO ");
				logImportacion.anadirLinea("----------------------------------------------------");
				logImportacion.anadirLinea("");
				logImportacion.anadirLinea("Importando Catalogo Sistematizado ....");
				
				Plan catSist = importarCatalogoSistematizado();
				planBaseIden = catSist.getIden();
			}
			else
				planBaseIden = planBase.getIden();
			
			
	        // -----| PLAN VIGENTE |-----
			
			query = "SELECT tram " +
	        " FROM Tramite tram where LOWER(tram.codigofip) like '" + codigoPlanVigente.toLowerCase() + "'";
			Plan planVigente = new Plan();
			
	        lista = new ArrayList<Tramite>();
	        lista = (List<Tramite>) entityManager.createQuery(query).getResultList();
	    	if (lista!=null && lista.size()>0)
	    		planVigente = lista.get(0).getPlan();
	    	else
	    		planVigente = null;
	    	
			
			if (planVigente==null)
			{
		        log.info("");
				log.info("----------------------------------------------------");
				log.info("  IMPORTANDO PLAN VIGENTE ");
				log.info("----------------------------------------------------");
				log.info("");
				resultado.info("Importando Plan Vigente ....");
				
				logImportacion.anadirLinea("");
				logImportacion.anadirLinea("----------------------------------------------------");
				logImportacion.anadirLinea("  IMPORTANDO PLAN VIGENTE ");
				logImportacion.anadirLinea("----------------------------------------------------");
				logImportacion.anadirLinea("");
				logImportacion.anadirLinea("Importando Plan Vigente ....");
				
				planVigente = importarPlanVigente();
				resultado.planVigente = planVigente;
			}
			
						
			// -----| PLAN ENCARGADO |-----
			log.info("");
			log.info("----------------------------------------------------");
			log.info("  IMPORTANDO PLAN ENCARGADO ");
			log.info("----------------------------------------------------");
			log.info("");
			resultado.info("Importando Plan Encargado ....");
			
			logImportacion.anadirLinea("");
			logImportacion.anadirLinea("----------------------------------------------------");
			logImportacion.anadirLinea("  IMPORTANDO PLAN ENCARGADO ");
			logImportacion.anadirLinea("----------------------------------------------------");
			logImportacion.anadirLinea("");
			logImportacion.anadirLinea("Importando Plan Encargado ....");
			
			gestionPlanEncargado.importar(planBaseIden, planVigente.getIden(), false, xmlDoc, entityManager, resultado);
			

			resultado.info("");
	    	resultado.info("----------------------------------------------------------");
			resultado.info("  FIN IMPORTACION FIP DE TIPO 1 ");
			resultado.info("");
			resultado.info("    . Fin importacion: " + new Date());
			// resultado.info("    . Numero de operaciones SQL: " + resultado.countSQL);	
			resultado.info("----------------------------------------------------------");
			resultado.info("");
			resultado.log();
			
			logImportacion.anadirLinea("");
	    	logImportacion.anadirLinea("----------------------------------------------------------");
			logImportacion.anadirLinea("  FIN IMPORTACION FIP DE TIPO 1 ");
			logImportacion.anadirLinea("");
			logImportacion.anadirLinea("    . Fin importacion: " + new Date());
			// logImportacion.anadirLinea("    . Numero de operaciones SQL: " + resultado.countSQL);	
			logImportacion.anadirLinea("----------------------------------------------------------");
			logImportacion.anadirLinea("");
			resultado.log();
			
			// Cambiar el estado a IMPORTADO
			fipCargado.setEstado("IMPORTADO");
			
		} catch (ParserConfigurationException e) {
			fipCargado.setEstado("ERRORES");
			resultado.error("Error en configuración para parsear XML: " + e.getMessage());		
			logImportacion.anadirLinea("Error en configuración para parsear XML: " + e.getMessage());	
		} catch (SAXException e) {
			fipCargado.setEstado("ERRORES");
			resultado.error("Error al parsear el fichero XML: " + e.getMessage());	
			logImportacion.anadirLinea("Error al parsear el fichero XML: " + e.getMessage());	
		} catch (IOException e) {	
			fipCargado.setEstado("ERRORES");
			resultado.error("Error al abrir el fichero de XML "+file+": "+e.getMessage());
			logImportacion.anadirLinea("Error al abrir el fichero de XML "+file+": "+e.getMessage());
		} catch (Exception e ) {
			fipCargado.setEstado("ERRORES");
			resultado.error("Error desconocido, error: " + e.getMessage());
			logImportacion.anadirLinea("Error desconocido, error: " + e.getMessage());
			e.printStackTrace();
		}

		// Si hay errores ...
		if (resultado.errores.size()>0) fipCargado.setEstado("ERRORES");

		// Cambiar el estado a CONSOLIDANDO
		fipCargado.setFechaConsolidacion(new Date());
		entityManager.merge(fipCargado);
		entityManager.flush();
		try {
			Transaction.instance().commit();
		} catch (Exception e) {
			resultado.error("Error al hacer commit de la importacion: "+e.getMessage());
			logImportacion.anadirLinea("Error al hacer commit de la importacion: "+e.getMessage());
			e.printStackTrace();
		}
		
		// Enseñar resultado
		return resultado;	
    }
    
    
    /*
     *  IMPORTAR CATALOGO SISTEMATIZADO
     *  
     */
    private Plan importarCatalogoSistematizado() throws EditorFIPException {
    	
    	ImportadorBase impBase = new ImportadorBase(xmlDoc,null);
    	
    	// Coger datos del trámite
    	NodeList nodes = impBase.evaluateXPath("//CATALOGO-SISTEMATIZADO");
    	
    	// Si tenemos plan vigente
    	if (nodes.getLength()>0) {
    		
    		// Mapeamos atributos del nodo
	    	Map<String, Object> attrMap = impBase.nodeToMap( nodes.item(0) );
	    	int idAmbito = Integer.parseInt( (String) attrMap.get("ambito"));
	    	
	    	// Necesitamos el código
	    	String codigo = Utiles.calcularCodigoPlan(idAmbito);
	    	
    		// Creamos Plan y persistimos
    		Plan planVigente = new Plan();
    		planVigente.setNombre( (String) attrMap.get("nombre"));
    		planVigente.setIdambito(idAmbito);
    		planVigente.setCodigo(codigo);
    		log.info("Persistiendo CATALOGO-SISTEMATIZADO ...");
    		logImportacion.anadirLinea("Persistiendo CATALOGO-SISTEMATIZADO ...");
    		entityManager.persist(planVigente);
    		
    		// Añadir trámite
    		ImportadorTramite impTramite = new ImportadorTramite(xmlDoc, entityManager, resultado);
            impTramite.xmlPath = "//CATALOGO-SISTEMATIZADO/TRAMITE";
            impTramite.plan = planVigente;
            impTramite.importar(new Date(), "Plan Base del ambito"); 
            
            return planVigente;
    	}	
    	
    	return null;
    }

    
    /*
     *  IMPORTAR PLAN VIGENTE
     *  
     */
    private Plan importarPlanVigente() throws EditorFIPException {
    	
    	ImportadorBase impBase = new ImportadorBase(xmlDoc,null);
    	
    	String fechaPlanVigente = "";
    	Date fechaPV = new Date();
    	SimpleDateFormat formatoDelTexto = new SimpleDateFormat("yyyy-MM-dd");
    	
    	log.info("Buscando en el xml nodo FIP ...");
    	logImportacion.anadirLinea("Buscando en el xml nodo FIP ...");
    	NodeList nodeFIP = impBase.evaluateXPath("//FIP");
    	// Si tenemos fip ...
		if (nodeFIP.getLength()==0) {
			
			log.info("No hay FIP en el fichero XML.");
			logImportacion.anadirLinea("No hay FIP en el fichero XML.");
			
		} else {
			
			// Obtener atributos
			Map<String, Object> attrs = impBase.nodeToMap( nodeFIP.item(0) );
			
						
			fechaPlanVigente = (String) attrs.get("FECHA");
			log.info("fechaPlanVigente="+fechaPlanVigente);
			logImportacion.anadirLinea("fechaPlanVigente="+fechaPlanVigente);
			
			try
			{
				fechaPV = formatoDelTexto.parse(fechaPlanVigente);
			}
			catch (java.text.ParseException e) {
				
				e.printStackTrace();
			}
			
		}
    	
    	// Coger datos del trámite
    	NodeList nodes = impBase.evaluateXPath("//PLANEAMIENTO-VIGENTE");
    	
    	// Si tenemos plan vigente
    	if (nodes.getLength()>0) {
    		
    		// Mapeamos atributos del nodo
	    	Map<String, Object> attrMap = impBase.nodeToMap( nodes.item(0) );
	    	int idAmbito = Integer.parseInt( (String) attrMap.get("ambito"));
	    	
	    	// Necesitamos el código
	    	String codigo = Utiles.calcularCodigoPlan(idAmbito);
	    	
    		// Creamos Plan y persistimos
    		Plan planVigente = new Plan();
    		planVigente.setNombre( (String) attrMap.get("nombre"));
    		planVigente.setIdambito(idAmbito);
    		planVigente.setCodigo(codigo);
    		
    		log.info("Persistiendo PLAN VIGENTE ...");
    		logImportacion.anadirLinea("Persistiendo PLAN VIGENTE ...");
    		entityManager.persist(planVigente);
    		
    		// Añadir trámite
    		ImportadorTramite impTramite = new ImportadorTramite(xmlDoc, entityManager, resultado);
            impTramite.xmlPath = "//PLANEAMIENTO-VIGENTE/TRAMITE";
            impTramite.plan = planVigente;
            impTramite.importar(fechaPV, "Plan Vigente de "+fechaPV.toString()); 
            
            return planVigente;
    	}	
    	
    	return null;
    }
    

    
    
    
}

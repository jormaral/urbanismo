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

package com.mitc.redes.editorfip.servicios.gestionfip.gestionprerefundido;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.log.Log;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.mitc.redes.editorfip.entidades.rpm.gestionfip.FipsCargados;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Tramite;
import com.mitc.redes.editorfip.excepciones.EditorFIPException;
import com.mitc.redes.editorfip.servicios.gestionfip.generacionSLD.GenerarSLDdeTramites;
import com.mitc.redes.editorfip.servicios.gestionfip.importarfip.ImportadorTramite;
import com.mitc.redes.editorfip.servicios.gestionfip.importarfip.Resultado;
import com.mitc.redes.editorfip.servicios.gestionfip.importarfip.Utiles;
import com.mitc.redes.editorfip.servicios.gestionfip.obtencionfip.LogImportacion;

@Stateless
@Name("importadorPrerefundido")
public class ImportadorPrerefundidoBean implements ImportadorPrerefundido {
	
    @In EntityManager entityManager;
    @Logger private Log log;
    
    @In(create = true, required = false)
	LogImportacion logImportacion;
    
    @In(create = true, required = false)
	GenerarSLDdeTramites generarSLDdeTramites;
    
    //private Log  log = Logging.getLog(ImportadorPrerefundidoBean.class);
    private Document xmlDoc = null;
    private FipsCargados fipCargado;

    /*
     * 	Importar prerefundido
     * 	=====================
     * 
     * (non-Javadoc)
     * @see com.mitc.redes.editorfip.servicios.procesamientofip.obtencionfip1.ImportadorPrerefundido#importar(java.lang.String, java.lang.Integer)
     */
    public int importar(String fichero, Integer idTramite) throws EditorFIPException {
    	
    	int idTramitePrerefundidoResult = 0;
    	Resultado resultado = new Resultado();
    	
    	resultado.info("----------------------------------------------------");
		resultado.info("  IMPORTANDO PRE-REFUNDIDO ");
		resultado.info("----------------------------------------------------");
		resultado.info("");
		
    	// Configurar Utiles
		Utiles.limpiar();
    	Utiles.em = entityManager;
    	
		try {
			
			// Ruta completa
			//fichero = System.getProperty("jboss.home.dir") + File.separator + "var" + File.separator + "FIPs.war" + File.separator + "prerefundido" + File.separator + fichero;
			
			// Cargar fichero XML
			log.info("Leyendo fichero XML del prerefundido: " + fichero); 
			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder;
			docBuilder = docBuilderFactory.newDocumentBuilder();	
			xmlDoc = docBuilder.parse(new File(fichero));
			xmlDoc.getDocumentElement().normalize();
			log.info("Leyendo fichero XML del prerefundido: OK"); 
			
			// Obtener el trámite
			log.info("Obteniendo tramite con id: " + idTramite);
			Tramite tramite = (Tramite) entityManager.find(Tramite.class, idTramite);
			log.info("Tramite obtenido, nombre: " + tramite.getNombre());
			
			// Importar Tramite
    		ImportadorTramite impTramite = new ImportadorTramite(xmlDoc, entityManager, resultado,logImportacion);
            //impTramite.xmlPath = "//FIP/TRAMITE";
            
            //impTramite.tramite = tramite;
            int idTramitePrerefundido = impTramite.importar(new Date(),"Prerefundido", tramite.getPlan(),"//FIP/TRAMITE");
            idTramitePrerefundidoResult = idTramitePrerefundido;
            
         // Ahora creamos los SLDs para el tr‡mite del plan vigente
		    log.info("Creando SLDs para el Plan Vigente ...");
		    //logImportacion.anadirLinea("Creando SLDs para el Plan Vigente ...");
		    
		    if (generarSLDdeTramites!=null)
		    {
		    	generarSLDdeTramites.generarSLD(idTramitePrerefundido, tramite.getPlan().getIdambito());
		    }		    	
		    else
		    {
		    	resultado.error("Error generarSLDdeTramites=null ");
				log.error("Error generarSLDdeTramites=null ");
				
		    }
			
			// Resultado
			resultado.info("");
	    	resultado.info("----------------------------------------------------------");
			resultado.info("  FIN IMPORTACION PRE-REFUNDIDO ");
			resultado.info("----------------------------------------------------------");
			resultado.info("");
			resultado.log();
			
		} catch (ParserConfigurationException e) {
			log.error("Error en configuración para parsear XML: " + e.getMessage());	
			throw new EditorFIPException(fichero);
		} catch (SAXException e) {
			log.error("Error al parsear el fichero XML: " + e.getMessage());	
			throw new EditorFIPException(fichero);
		} catch (IOException e) {	
			log.error("Error al abrir el fichero de XML "+fichero+": "+e.getMessage());
			throw new EditorFIPException(fichero);
		} catch (Exception e ) {
			log.error("Error desconocido, error: " + e.getMessage());	
			e.printStackTrace();
			throw new EditorFIPException(fichero);
		}

		// Enseñar resultado
		//return resultado;	
		
		log.info("idTramitePrerefundidoResult= "+idTramitePrerefundidoResult);
		return idTramitePrerefundidoResult;
    }
}

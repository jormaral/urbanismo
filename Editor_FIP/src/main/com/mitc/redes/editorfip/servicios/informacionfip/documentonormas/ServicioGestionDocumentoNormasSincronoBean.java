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

package com.mitc.redes.editorfip.servicios.informacionfip.documentonormas;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRXmlDataSource;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.async.Asynchronous;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.international.StatusMessage.Severity;
import org.jboss.seam.log.Log;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.mitc.redes.editorfip.entidades.fipxml.RegulacionEspecifica;
import com.mitc.redes.editorfip.entidades.rpm.gestionfip.DocumentosNormativaGenerados;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Casoentidaddeterminacion;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Entidad;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Entidaddeterminacion;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Entidaddeterminacionregimen;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Regimenespecifico;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Tramite;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Vinculocaso;
import com.mitc.redes.editorfip.servicios.informacionfip.consultafichaurbanisticas.ServicioBasicoFichaUrbanistica;
import com.mitc.redes.editorfip.servicios.informacionfip.consultafichaurbanisticas.XMLws;
import com.mitc.redes.editorfip.utilidades.Textos;

@Stateless
@Name("servicioGestionDocumentoNormasSincrono")
public class ServicioGestionDocumentoNormasSincronoBean implements ServicioGestionDocumentoNormasSincrono
{
    @Logger private Log log;
    
    @PersistenceContext
	EntityManager em;
    
    @In Map<String,String> messages;
    
    @In (create = true)
    FacesMessages facesMessages;
    
   
        
    @In(create = true, required= false)
    ServicioBasicoFichaUrbanistica servicioBasicoFichaUrbanistica;
    
    @In(create = true, required= false)
    ServicioGestionDocumentoNormas servicioGestionDocumentoNormas;
  
    
    
    
   
    private DocumentosNormativaGenerados enEjecucion (int idTramite)
    {
    	// Creo un nuevo objeto
    	DocumentosNormativaGenerados nuevoDoc = new DocumentosNormativaGenerados();
    	
    	nuevoDoc.setFechaGeneracion(new Date());
    	nuevoDoc.setIdTramiteDocumentoNormativa(idTramite);
    	
    	String nombreTramite = ((Tramite) em.find(Tramite.class, idTramite)).getNombre();
    	nuevoDoc.setNombreTramite(nombreTramite);
    	
    	// Guardo rapido antes del proceso
    	try
    	{
    		nuevoDoc.setEstado("CREANDO");
    		em.persist(nuevoDoc);
    		em.flush();
    		
    		facesMessages.addFromResourceBundle(Severity.INFO, "Se esta generando el documento de normas. Vaya al listado de documentos para mas informacion", null);
    	}
    	catch (Exception ex)
    	{
    		ex.printStackTrace();
    				
    		
    	}
    	
    	return nuevoDoc;
    }
    
   
    public void crearDocumentoNormasSincrono(int idTramite)
    {
    	log.info("[crearDocumentoNormas] idTramite="+idTramite);
    	DocumentosNormativaGenerados nuevoDoc = enEjecucion(idTramite);
    	
    	// Llamo a la funcion asincrona
    	    	
    	try
    	{
    		servicioGestionDocumentoNormas.crearDocumentoNormas(idTramite,nuevoDoc);
    		
    	}
    	catch (Exception e)
    	{
    		facesMessages.addFromResourceBundle(Severity.ERROR, "Error al guardar documento normas", null); 
    	}
    	
    	
    }
  
	  
}



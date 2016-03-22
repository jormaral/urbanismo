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

package com.mitc.redes.editorfip.servicios.gestionfip.importarfip;



import javax.ejb.Stateless;
import javax.persistence.EntityManager;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.international.StatusMessage.Severity;
import org.jboss.seam.log.Log;

import com.mitc.redes.editorfip.entidades.rpm.gestionfip.FipsCargados;

@Stateless
@Name ("gestionImportacionFIP1Sincrona")
public class GestionImportacionFIP1SincronaBean implements GestionImportacionFIP1Sincrona{

	
	
	@In
	EntityManager entityManager;
	
	@In(create=true) FacesMessages facesMessages;

	@Logger private Log log;
	
	
	@In(create = true, required = false)
	GestionImportacionFIP1 gestionImportacionFIP1;
	
	
	

    public void importacionFIPSincrono(Long idFipCargado)  
    {
    	log.info("[importacionFIPSincrono] idFipCargado="+idFipCargado);
    	// Obtener el fip encargado seleccionado
		FipsCargados fipCargado = entityManager.find(FipsCargados.class, idFipCargado);
		
		fipCargado.setEstado("En proceso");
		entityManager.merge(fipCargado);
		
		// Llamo a la funcion asincrona
    	
    	try
    	{
    		log.info("[importacionFIPSincrono] Llamo al asincrono");
    		gestionImportacionFIP1.importacionFIP(idFipCargado);
    		facesMessages.addFromResourceBundle(Severity.INFO, "Se ha empezado a importar. Este proceso puede tardar mucho tiempo. Vaya al listado para comprobar el avance", null); 
    		
    	}
    	catch (Exception e)
    	{
    		facesMessages.addFromResourceBundle(Severity.ERROR, "Error al guardar documento normas", null); 
    	}
    	
    	
    }
   
	

}
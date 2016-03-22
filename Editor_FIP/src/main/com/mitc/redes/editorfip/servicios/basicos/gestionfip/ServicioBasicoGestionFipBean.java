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

package com.mitc.redes.editorfip.servicios.basicos.gestionfip;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.log.Log;

@Stateless
@Name("servicioBasicoGestionFip")
public class ServicioBasicoGestionFipBean implements ServicioBasicoGestionFip
{
    @Logger private Log log;

    @PersistenceContext
	EntityManager em;
    
    
    public int obtenerIdTramiteBaseDeIdTramiteEncargado (int idTramiteEncargado)
    {
    	
    	int resultado = 0;
    	
    	String query =  "SELECT planesEnc.tramiteBase.iden " +
        " FROM PlanesEncargados planesEnc " +      
        " WHERE planesEnc.tramiteEncargado=" +idTramiteEncargado ;

		
		
		 try {
			 resultado = (Integer) em.createQuery(query).getSingleResult();
		} catch (NoResultException e) {
		    log.error("[obtenerIdTramiteBaseDeIdTramiteEncargado] No se ha encontrado el IdTramiteBase." + e.getMessage());
		
		} catch (Exception ex) {
		    log.error("[obtenerIdTramiteBaseDeIdTramiteEncargado] Error en la busqueda del IdTramiteBase: " + ex.getMessage());
		
		}
    	
    	
    	return resultado;
    	
    	
    }
    
    
    public int obtenerIdTramiteVigenteDeIdTramiteEncargado (int idTramiteEncargado)
    {
    	
    	int resultado = 0;
    	
    	String query =  "SELECT planesEnc.tramiteVigente.iden " +
        " FROM PlanesEncargados planesEnc " +      
        " WHERE planesEnc.tramiteEncargado=" +idTramiteEncargado ;

		
		
		 try {
			 resultado = (Integer) em.createQuery(query).getSingleResult();
		} catch (NoResultException e) {
		    log.error("[obtenerIdTramiteVigenteDeIdTramiteEncargado] No se ha encontrado el IdTramiteVigente." + e.getMessage());
		
		} catch (Exception ex) {
		    log.error("[obtenerIdTramiteVigenteDeIdTramiteEncargado] Error en la busqueda del IdTramiteVigente: " + ex.getMessage());
		
		}
    	
    	
    	return resultado;
    	
    	
    }

    
    

}

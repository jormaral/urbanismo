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

package com.mitc.redes.editorfip.servicios.basicos.fip.documentos;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.log.Log;

import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Determinacion;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Opciondeterminacion;




@Stateless
@Name("servicioBasicoOpcionDeterminacion")
public class ServicioBasicoOpcionDeterminacionBean implements ServicioBasicoOpcionDeterminacion
{
    @Logger private Log log;

 
    
    @PersistenceContext
	EntityManager em;

    
    public void removeByDeterminacion(int idDeterminacion){
    	
     	String query =  "SELECT opDet " +
    	" FROM Opciondeterminacion opDet"+
    	" WHERE opDet.determinacionByIddeterminacion="+idDeterminacion+
    	" OR opDet.determinacionByIddeterminacionvalorref="+idDeterminacion;

        Query qNombreAmbito = em.createQuery(query);
        
        try {
        	// Se borran todos los resultantes
        	List<Opciondeterminacion> result = (List<Opciondeterminacion>) qNombreAmbito.getResultList().get(0);
        	for (Opciondeterminacion valorReferencia : result)
            {
            	em.remove(valorReferencia);
        		
            }
        } catch (NoResultException e) {
            log.error("[removeByDeterminacionAndValorReferencia] No se ha encontrado la Opcion Determinacion." + e.getMessage());

        } catch (Exception ex) {
            log.error("[removeByDeterminacionAndValorReferencia] Error en la busqueda de la Opcion Determinacion: " + ex.getMessage());

        }
    	
    }

    public boolean crearValorRefenciaDeterminacionPadre (Determinacion hijo, int idPadre)
    {
    	boolean result = false;
    	try {
	    	
	    	Determinacion detPadre = em.find(Determinacion.class, idPadre);
	    	
	    	//Determinacion detHijo = em.find(Determinacion.class, idHijo);
	    	
	    	Opciondeterminacion opciondeterminacion = new Opciondeterminacion();
			
			opciondeterminacion.setDeterminacionByIddeterminacion(detPadre);
			opciondeterminacion.setDeterminacionByIddeterminacionvalorref(hijo);
			
			em.persist(opciondeterminacion);
			
			log.debug("OPCIÓN DETERMINACION CREADA");
	    	result = true;
		
	    } catch (NoResultException e) {
	        log.error("[guardarHijosComoValorReferencia] No se ha encontrado la Determinacion." + e.getMessage());
	
	    } catch (Exception ex) {
	        log.error("[guardarHijosComoValorReferencia] Error en la busqueda de la  Determinacion: " + ex.getMessage());
	
	    }
    	return result;
    }
    
}

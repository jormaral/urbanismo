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


package com.mitc.redes.editorfip.servicios.administracion.gestionusuario;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.log.Log;

import com.mitc.redes.editorfip.entidades.interfazgrafico.BusquedaGenericaDTO;
import com.mitc.redes.editorfip.entidades.rpm.administracionusuarios.Usuario;

@Stateful
@Name("servicioCRUDGestionUsuario")
public class ServicioCRUDGestionUsuarioBean implements ServicioCRUDGestionUsuario {

	
	@Logger private Log log;
    @PersistenceContext
	EntityManager em;
	
	@Override
	public List<Usuario> autocomplete(String word) {
		String query = "FROM Usuario u" +
        " WHERE lower(u.username) like '%" + word.toLowerCase() + "%' " +
        "ORDER BY u.username ASC";
    	
    	try {
    		
			return (List<Usuario>)em.createQuery(query).setMaxResults(10).getResultList();
			
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<Usuario> ();
		}
	}

	@Override
	public List<BusquedaGenericaDTO> autocompleteFiltros(String word) {
		
		List<BusquedaGenericaDTO> resDto = new ArrayList<BusquedaGenericaDTO>();
    	List<String> res = new ArrayList<String>();
    	
    	String query = "select u.username " +   
	    		"FROM Usuario u " +
	        "WHERE lower(u.username) like '%" + word.toLowerCase() + "%' " +
	        "ORDER BY u.username ASC";
	    	
	    	try {
	    		
	    		res = (List<String>)em.createQuery(query).setMaxResults(20).getResultList();
				log.debug("TAMAÑO DE LA LISTA" + res.size());
				for (String resObj : res)
	    		{
					
					BusquedaGenericaDTO obj = new BusquedaGenericaDTO();
					
					obj.setIdenUsuario(resObj);			
					resDto.add(obj);
	    		}
				
			} catch (Exception e) {
				e.printStackTrace();
				return new ArrayList<BusquedaGenericaDTO> ();
			}
    	
    	
    	return resDto;
	}

	@Remove
	public void destroy() {
		// TODO Auto-generated method stub

	}

}

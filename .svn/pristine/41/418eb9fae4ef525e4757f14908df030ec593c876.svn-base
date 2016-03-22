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

package com.mitc.redes.editorfip.servicios.basicos.fip.planes;

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
import com.mitc.redes.editorfip.entidades.rpm.gestionfip.PlanesEncargados;


@Stateful
@Name("servicioCRUDPlanesEncargados")
public class ServicioCRUDPlanesEncargadosBean implements ServicioCRUDPLanesEncargados {

	
	@Logger private Log log;
    @PersistenceContext
	EntityManager em;
	
	
	
	@Override
	public List<PlanesEncargados> autocomplete(String word) {
		String query = "FROM PlanesEncargados p" +
        "WHERE lower(p.nombre) like '%" + word.toLowerCase() + "%' " +
        "ORDER BY d.nombre  ASC";
    	
    	try {
    		
			return (List<PlanesEncargados>)em.createQuery(query).setMaxResults(10).getResultList();
			
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<PlanesEncargados> ();
		}
	}

	@Override
	public List<BusquedaGenericaDTO> autocompleteFiltros(String word){
		List<BusquedaGenericaDTO> resDto = new ArrayList<BusquedaGenericaDTO>();
    	List<Object> res = new ArrayList<Object>();
    	
    	String query = "select p.id, p.nombre " +   
	    		"FROM PlanesEncargados p " +
	        "WHERE lower(p.nombre) like '%" + word.toLowerCase() + "%' " +
	        "ORDER BY p.nombre  ASC";
	    	
	    	try {
	    		
	    		res = (List<Object>)em.createQuery(query).setMaxResults(20).getResultList();
				
				for (Object resObj : res)
	    		{
					Object[] regArrayObj = (Object[]) resObj;
					BusquedaGenericaDTO obj = new BusquedaGenericaDTO();
					
					obj.setIden((Long)regArrayObj[0]);
					if (((String)regArrayObj[1]).length()>50)
						obj.setNombre(((String)regArrayObj[1]).substring(0, 49) + "...");
					else
						obj.setNombre((String)regArrayObj[1]);
					
									
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

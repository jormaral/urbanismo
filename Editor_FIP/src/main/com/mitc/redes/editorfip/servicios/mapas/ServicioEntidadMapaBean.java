package com.mitc.redes.editorfip.servicios.mapas;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.log.Log;

import com.mitc.redes.editorfip.entidades.rpm.gestionfip.ColoresEntidades;



@Stateless
@Name("servicioEntidadMapa")
public class ServicioEntidadMapaBean implements ServicioEntidadMapa {
	
	 @Logger private Log log;
	    
	    @PersistenceContext
		EntityManager em;
	    
	    @In(create = true, required = false)
	   
	
	public List<EntidadMapaDTO> listadoEntidadesColorMapa(int idtramite, String codigo) 
	{
    	
	    	List <EntidadMapaDTO> listaEntidades = new ArrayList<EntidadMapaDTO>();
	    	
	    	//Seleccionamos todas las entidades con trámite y código iguales a los pasados
	    	//como parámetro
	    	
	    	String query = "select ent.iden, ent.nombre, ent.clave, ent.codigo, col.color, col.id	 from planeamiento.entidad, gestionfip.coloresentidades col"+
	    			" JOIN planeamiento.entidaddeterminacion ON ent.iden = entidaddeterminacion.identidad"+
	    			" JOIN planeamiento.casoentidaddeterminacion ON entidaddeterminacion.iden = casoentidaddeterminacion.identidaddeterminacion"+
	    			" JOIN planeamiento.entidaddeterminacionregimen ON casoentidaddeterminacion.iden = entidaddeterminacionregimen.idcaso"+
	    			" JOIN planeamiento.opciondeterminacion ON entidaddeterminacionregimen.idopciondeterminacion = opciondeterminacion.iden"+
	    			" JOIN planeamiento.determinacion  ON opciondeterminacion.iddeterminacionvalorref = determinacion.iden"+
	    			" JOIN planeamiento.determinacion determinacionaplicada ON entidaddeterminacion.iddeterminacion = determinacionaplicada.iden"+
	    			" where ent.idtramite = "+idtramite+" and determinacion.codigo='"+codigo+"'";
	    	
	    	try
	    	{
	    		List<Object> listaEntidadesCorrectas = em.createQuery(query).getResultList();
	    		log.info("[listadoEntidadesColorMapa] Entidades del trámite = "+idtramite+" pertenecientes al grupo con codigo"+codigo);
	    		
	    		for (Object entidadSeleccionada : listaEntidadesCorrectas)
	    		{
	    			// Por cada objeto creo un nuevo registro de EntidadMapaDTO
	    			
	    			EntidadMapaDTO entidadMapa = new EntidadMapaDTO();
	    			
	    			Object[] entidadSeleccionadaArray = (Object[]) entidadSeleccionada;
	    			
	    			// El indice 1 se corresponde al id de la entidad 
	    			entidadMapa.setIdentidad((Integer)entidadSeleccionadaArray[1]);
	    			
	    			// El indice 2 se corresponde al nombre de la entidad de la CU
	    			entidadMapa.setNombre((String)entidadSeleccionadaArray[2]);	   
	    			
	    			// El indice 3 se corresponde a la clave de la entidad
	    			entidadMapa.setClave((String)entidadSeleccionadaArray[3]);	  
	    			
	    			// El indice 4 se corresponde al codigo de la entidad
	    			entidadMapa.setCodigo((String)entidadSeleccionadaArray[4]);
	    			
	    			// El indice 5 se corresponde al color de la entidad
	    			entidadMapa.setColor((String)entidadSeleccionadaArray[5]);
	    			
	    			// El indice 6 se corresponde al color de la entidad
	    			entidadMapa.setIdentidadColor((Integer)entidadSeleccionadaArray[6]);
	    			
	    				    			
	    				    			
	    			listaEntidades.add(entidadMapa);
	    			
	    			
	    		}
	    		
	    		
	    	}
	    	catch (Exception ex)
	    	{
	    		log.error("[listadoEntidadesColorMapa] Fallo al buscar las etnidades del tramite y grupo. ERROR:"+ex.getMessage());
	    		ex.printStackTrace();
	    	}
	    	
	    	log.debug("listadoEntidadesColorMapa] Obtenidas "+listaEntidades.size()+" entidades para el tramite="+idtramite+"pertenecientes al grupo con codigo"+codigo);
	    	return listaEntidades;
	    
	}
	    
	public void guardarColor (){
		
		
		ColoresEntidades colorent;
		
		//String query = "select color from "
		
		
	}    
	    
	    

}

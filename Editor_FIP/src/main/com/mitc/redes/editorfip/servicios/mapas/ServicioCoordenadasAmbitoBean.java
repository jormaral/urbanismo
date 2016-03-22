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

package com.mitc.redes.editorfip.servicios.mapas;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.log.Log;

import com.mitc.redes.editorfip.entidades.rpm.gestionfip.CoordenadasAmbito;


@Stateless
@Name("servicioCoordenadasAmbito")

public class ServicioCoordenadasAmbitoBean implements ServicioCoordenadasAmbito {
	
	@Logger private Log log;
   
    @PersistenceContext
	EntityManager em;
    
   private CoordenadasAmbito coordenadasAmbito=null;
    
   
   public CoordenadasAmbito obtenerCoordenadasAmbito (int idAmbito)
   {
	   log.debug("[obtenerCoordenadasAmbito]Se van a obtener las coordenadas del ambito" + idAmbito);

	 
		String queryAmb = "SELECT ca " +
       " FROM CoordenadasAmbito ca " +
       " WHERE ca.idAmbito = " +idAmbito;
		
		try {
			coordenadasAmbito = (CoordenadasAmbito) em.createQuery(queryAmb).getSingleResult();
       	
       	
       	
       } catch (NoResultException e) {
           log.error("[obtenerCoordenadasAmbito] No se ha encontrado las coordenadas para el ambito:"+idAmbito + e.getMessage());

       } catch (Exception ex) {
           log.error("[obtenerCoordenadasAmbito] Error en la busqueda de las coordenadas para el ambito:"+idAmbito + ex.getMessage());

       }
       
       return coordenadasAmbito;
	   
   }
   
   public void meterTodosAmbitosEnTabla()
   {
	   
	   String queryAmb = "SELECT idambito FROM diccionario.ambitoshp ";
	   
	   List<Integer> resConsulta = new ArrayList<Integer>();
	   
	   try {
			  resConsulta = (List<Integer>) em.createNativeQuery(queryAmb).getResultList();
		     
		     
		     
		 } catch (NoResultException e) {
		     log.error("[meterTodosAmbitosEnTabla] NoResultException." + e.getMessage());
		
		 } catch (Exception ex) {
		     log.error("[meterTodosAmbitosEnTabla] Exception: " + ex.getMessage());
		
		 }
		 
		 
		 CoordenadasAmbito ca = null;
		 // Por cada ambito, inserto un registro con las coordenadas
		 for (Integer objConsulta : resConsulta)
		 {
			 ca = new CoordenadasAmbito();
			 ca.setIdAmbito(objConsulta);
			 
			 // Hago la consulta de las coordenadas de ese ambito
			 
			 String queryCoorAmb = "SELECT ST_xmin(ST_Extent(geom)) as bextent FROM diccionario.ambitoshp where idambito="+objConsulta
			                       +" UNION ALL SELECT ST_ymin(ST_Extent(geom)) as bextent FROM diccionario.ambitoshp where idambito="+objConsulta
			                       +" UNION ALL SELECT ST_xmax(ST_Extent(geom)) as bextent FROM diccionario.ambitoshp where idambito="+objConsulta
			                       +" UNION ALL SELECT ST_ymax(ST_Extent(geom)) as bextent FROM diccionario.ambitoshp where idambito="+objConsulta;
		 
			 List<Double> resCoord = new ArrayList<Double>();
			   
			   try {
				   resCoord = (List<Double>) em.createNativeQuery(queryCoorAmb).getResultList();
				     
				     ca.setCoorxmin(resCoord.get(0));
				     ca.setCoorymin(resCoord.get(1));
				     ca.setCoorxmax(resCoord.get(2));
				     ca.setCoorymax(resCoord.get(3));
				     
				     
				 } catch (NoResultException e) {
				     log.error("[meterTodosAmbitosEnTabla] NoResultException." + e.getMessage());
				
				 } catch (Exception ex) {
				     log.error("[meterTodosAmbitosEnTabla] Exception: " + ex.getMessage());
				
				 }
				 
				 // Guardamos el nuevo objeto
				 em.persist(ca);
		 }   
			                                                                                                                                               
   }
   
   public void meterAmbitoEnTabla(int idAmbito)
   {
	   
	   
		 
		 
		 CoordenadasAmbito ca = null;
		 
			 ca = new CoordenadasAmbito();
			 ca.setIdAmbito(idAmbito);
			 
			 // Hago la consulta de las coordenadas de ese ambito
			 
			 String queryCoorAmb = "SELECT ST_xmin(ST_Extent(geom)) as bextent FROM diccionario.ambitoshp where idambito="+idAmbito
			                       +" UNION ALL SELECT ST_ymin(ST_Extent(geom)) as bextent FROM diccionario.ambitoshp where idambito="+idAmbito
			                       +" UNION ALL SELECT ST_xmax(ST_Extent(geom)) as bextent FROM diccionario.ambitoshp where idambito="+idAmbito
			                       +" UNION ALL SELECT ST_ymax(ST_Extent(geom)) as bextent FROM diccionario.ambitoshp where idambito="+idAmbito;
		 
			 List<Double> resCoord = new ArrayList<Double>();
			   
			   try {
				   resCoord = (List<Double>) em.createNativeQuery(queryCoorAmb).getResultList();
				     
				     ca.setCoorxmin(resCoord.get(0));
				     ca.setCoorymin(resCoord.get(1));
				     ca.setCoorxmax(resCoord.get(2));
				     ca.setCoorymax(resCoord.get(3));
				     
				     
				 } catch (NoResultException e) {
				     log.error("[meterTodosAmbitosEnTabla] NoResultException." + e.getMessage());
				
				 } catch (Exception ex) {
				     log.error("[meterTodosAmbitosEnTabla] Exception: " + ex.getMessage());
				
				 }
				 
				 // Guardamos el nuevo objeto
				 em.persist(ca);
		  
			                                                                                                                                               
   }
    
	public double xmaxFromEntity(int id) {
		
		double result;
		
		
		String hql = "SELECT ST_XMax(geometria) FROM planeamiento.entidades_capa WHERE identidad =" + id;
		List<Double> lxmax = em.createNativeQuery(hql).getResultList();
		if(lxmax.size()!=1)
			result = 886101.428;	
		else
			result = lxmax.get(0);
		return result;
		
		
	    }
	
	public boolean existenCoordeandasAmbito (Integer amb){
		boolean existe = false;
		
		String hql = "SELECT idambito FROM gestionfip.coordenadasambito WHERE idambito="+amb;
		List<Integer> ambitos = em.createNativeQuery(hql).getResultList();
		if(ambitos.size()!=0)
			existe = true;
		return existe;
		
	}



}

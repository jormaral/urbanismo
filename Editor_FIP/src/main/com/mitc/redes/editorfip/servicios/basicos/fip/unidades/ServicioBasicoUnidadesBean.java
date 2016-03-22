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

package com.mitc.redes.editorfip.servicios.basicos.fip.unidades;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.log.Log;

import com.mitc.redes.editorfip.entidades.interfazgrafico.UnidadDTO;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Determinacion;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Propiedadrelacion;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Relacion;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Tramite;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Vectorrelacion;
import com.mitc.redes.editorfip.utilidades.Textos;


@Stateless
@Name("servicioBasicoUnidades")
public class ServicioBasicoUnidadesBean implements ServicioBasicoUnidades
{
    @Logger private Log log;

    
    
    @PersistenceContext
	EntityManager em;



	@Override
	public int borrarUnidadDeDeterminacion(int idDeterminacion) {
		int unidadBorradaCorrectamente = 0;
		
    	
		 log.debug("[borrarUnidadDeDeterminacion] Se va a borrar los detalles de la unidad. Determinacion Caracter unidad= " + idDeterminacion);
    	
		 Integer IdDefPropUnidad = Integer.parseInt(Textos.getCadena("diccionario","unidad.id_definicion_unidad"));
	        Integer IdDeterminacionDicc = Integer.parseInt(Textos.getCadena("diccionario","unidad.id_determinacion"));
	        Integer IdDefAbreviatura = Integer.parseInt(Textos.getCadena("diccionario","unidad.id_abreviatura"));
	        Integer IdDefDefinicion = Integer.parseInt(Textos.getCadena("diccionario","unidad.id_definicion"));


	        String queryUnidad= "Select r.iden, vr.iden as vriden," +
	        "(select iden from planeamiento.Propiedadrelacion where iddefpropiedad =" + IdDefAbreviatura + " and idrelacion=r.iden ) as pr1," +
	        "(select iden from planeamiento.Propiedadrelacion where iddefpropiedad =" + IdDefDefinicion + " and idrelacion=r.iden ) as pr2 " +
	        " From planeamiento.Relacion r, planeamiento.Vectorrelacion vr " +
	        " Where r.iddefrelacion=" + IdDefPropUnidad +
	        " And vr.iddefvector=" + IdDeterminacionDicc +
	        " And vr.valor=" + idDeterminacion +
	        " And vr.idrelacion=r.iden ";
			
			  try {
		            
		            List<Object[]> idTablasUnidadList = em.createNativeQuery(queryUnidad).getResultList();

		            if (idTablasUnidadList.size()>0){
		                for (Object[] unidad: idTablasUnidadList){
		                	
		                	// Borro Vector Relacion
		                	log.debug("[borrarUnidadDeDeterminacion] Borro Vector Relacion= " + (Integer)unidad[1]);		                	
		                	em.remove(em.find(Vectorrelacion.class, (Integer)unidad[1]));
		                	
		                	// Borro Vector Relacion
		                	log.debug("[borrarUnidadDeDeterminacion] Borro Propiedad Relacion1= " + (Integer)unidad[2]);		                	
		                	em.remove(em.find(Propiedadrelacion.class, (Integer)unidad[2]));
		                	
		                	// Borro Vector Relacion
		                	log.debug("[borrarUnidadDeDeterminacion] Borro Propiedad Relacion2= " + (Integer)unidad[3]);		                	
		                	em.remove(em.find(Propiedadrelacion.class, (Integer)unidad[3]));
		                	
		                	// Borro Relacion
		                	log.debug("[borrarUnidadDeDeterminacion] Borro Relacion= " + (Integer)unidad[0]);		                	
		                	em.remove(em.find(Relacion.class, (Integer)unidad[0]));
		                	
		                	// Meto un flush para que se borre en este momento
		                	em.flush();
		                	
		                    
		                }
		                
		                unidadBorradaCorrectamente = 1;
		            }

		        } catch (Exception ex) {
		        	log.error("[obtenerUnidadDeDeterminacion] Error al obtener la unidad de la idDeterminacion= " + idDeterminacion);
		        	ex.printStackTrace();
		        }
		        
		
    	
    	
    	
    	log.debug("[borrarUnidadDeDeterminacion] Resultado= " + unidadBorradaCorrectamente);
    	return unidadBorradaCorrectamente;
	}



	@Override
	public int crearUnidad(int idDeterminacion, String abreviatura, String definicion) {
		
		int unidadCreadaCorrectamente = 0;
		int caracterUnidad = 18;
		int idTramite = 0;
    	
		 log.debug("[guardarUnidad] Se va a crear los detalles de la unidad. Determinacion Caracter unidad= " + idDeterminacion+ " // Detalle Unidad: abreviatura="+abreviatura+" / definicion="+definicion);
    	
		 
		 // Compruebo si la determinacion es efectivamente de caracter unidad
		 
		try
	    {
			Determinacion detUnidad = (Determinacion) em.find(Determinacion.class, idDeterminacion);
			
			if (detUnidad.getIdcaracter()!=caracterUnidad)
			{
				log.error("[guardarUnidad] Error la determinacion no es de caracter unidad con id= "+idDeterminacion);
			}
			else
			{
				log.debug("[guardarUnidad] La determinacion es de caracter unidad con id= "+idDeterminacion);
				unidadCreadaCorrectamente = 1;
				idTramite = detUnidad.getTramite().getIden();
			}
			
	    }
    	catch (Exception e) {
    		unidadCreadaCorrectamente = 0;
			log.error("[guardarUnidad] Error la determinacion no existe id= "+idDeterminacion);
			e.printStackTrace();
		}
		 
		
    	if (unidadCreadaCorrectamente!=0)
    	{
    		try
        	{
        		// Creo una nueva relacion
            	Relacion relacionRPManadir = new Relacion();
            	
            	// Creo dos nuevas propiedadesrelacion
            	Propiedadrelacion propiedadRelacionRPManadir1 = new Propiedadrelacion ();
            	Propiedadrelacion propiedadRelacionRPManadir2 = new Propiedadrelacion ();
            	
            	// Creo un nuevo vector relacion
                Vectorrelacion vectorRelacionRPManadir = new Vectorrelacion();
                
                // Opero con la relacion
                relacionRPManadir.setIddefrelacion(1);
                relacionRPManadir.setTramiteByIdtramitecreador(em.find(Tramite.class, idTramite));
                em.persist(relacionRPManadir);
                log.debug("[guardarUnidad] --    CREADA Relacion " + relacionRPManadir.getIden());
                
                // Opero con la propiedadRelacionRPManadir1
                propiedadRelacionRPManadir1.setRelacion(relacionRPManadir);
                propiedadRelacionRPManadir1.setIddefpropiedad(1);
                propiedadRelacionRPManadir1.setValor(abreviatura);
                em.persist(propiedadRelacionRPManadir1);
                log.debug("[guardarUnidad] --    CREADA propiedadRelacion1 " + propiedadRelacionRPManadir1.getIden());
                
                
                // Opero con la propiedadRelacionRPManadir2
                propiedadRelacionRPManadir2.setRelacion(relacionRPManadir);
                propiedadRelacionRPManadir2.setIddefpropiedad(2);
                propiedadRelacionRPManadir2.setValor(definicion);
                em.persist(propiedadRelacionRPManadir2);
                log.debug("[guardarUnidad] --    CREADA propiedadRelacion2 " + propiedadRelacionRPManadir2.getIden());
            	
                // Opero con vectorRelacion
                vectorRelacionRPManadir.setRelacion(relacionRPManadir);
                vectorRelacionRPManadir.setIddefvector(1);
                vectorRelacionRPManadir.setValor(idDeterminacion);
                em.persist(vectorRelacionRPManadir);
                log.debug("[guardarUnidad] --    CREADA vectorRelacionRPManadir " + vectorRelacionRPManadir.getIden());
                
                unidadCreadaCorrectamente = 1;
                log.info("[guardarUnidad] Creada correctamente la unidad de la determinacionunidad con id= "+idDeterminacion);
                
                //Persisto
                em.flush();

        	}
        	catch (Exception e) {
        		unidadCreadaCorrectamente = 0;
    			log.error("[guardarUnidad] Error al crear la unidad de la determinacionunidad con id= "+idDeterminacion);
    			e.printStackTrace();
    		}
    	}
		 
    	
    	
    	
    	log.debug("[guardarUnidad] Resultado= " + unidadCreadaCorrectamente);
    	return unidadCreadaCorrectamente;
		
	}



	@Override
	public List<UnidadDTO> obtenerListaUnidadesTramite(int idTramite) {
		
		log.debug("[obtenerListaUnidadesTramite] idTramite= " + idTramite);
		int caracterUnidad = 18;
		
		List<UnidadDTO> listaUnidades = new ArrayList<UnidadDTO>();
		
		String queryDetUnidad = " select det.iden, det.nombre " +
				" from planeamiento.Determinacion det" +
				" where det.idcaracter="+caracterUnidad +
				" and det.idtramite="+idTramite;
		
		 try {
	            
	            List<Object[]> unidadTramList = em.createNativeQuery(queryDetUnidad).getResultList();

	            if (unidadTramList.size()>0){
	                for (Object[] detUnidad: unidadTramList){
	                	
	                	UnidadDTO unidad = new UnidadDTO();
	                	
	                	unidad.setIdDeterminacion((Integer)detUnidad[0]);
	                	unidad.setNombreDeterminacion((String)detUnidad[1]);
	                	
	                	// Obtengo la definicion y abreviatura
	                	UnidadDTO unidadAuxiliar = obtenerUnidadDeDeterminacion((Integer)detUnidad[0]);
	                	
	                	if (unidadAuxiliar.getAbreviatura()!=null)
	                		unidad.setAbreviatura(unidadAuxiliar.getAbreviatura());
	                	
	                	if (unidadAuxiliar.getDefinicion()!=null)
	                		unidad.setDefinicion(unidadAuxiliar.getDefinicion());
	                	
	                	
	                	listaUnidades.add(unidad);
	                    
	                }
	            }

	        } catch (Exception ex) {
	        	log.error("[obtenerUnidadDeDeterminacion] Error al obtener la lista de unidad de  idTramite= " + idTramite);
	        	ex.printStackTrace();
	        }
		
	   log.debug("[obtenerListaUnidadesTramite] Numero Unidades= " + listaUnidades.size());
		return listaUnidades;
	}



	@Override
	public UnidadDTO obtenerUnidadDeDeterminacion(int idDeterminacion) {
		
		log.debug("[obtenerUnidadDeDeterminacion] idDeterminacion= " + idDeterminacion);
		
		UnidadDTO resultadoUnidad = new UnidadDTO();
		
		Integer IdDefPropUnidad = Integer.parseInt(Textos.getCadena("diccionario","unidad.id_definicion_unidad"));
        Integer IdDeterminacionDicc = Integer.parseInt(Textos.getCadena("diccionario","unidad.id_determinacion"));
        Integer IdDefAbreviatura = Integer.parseInt(Textos.getCadena("diccionario","unidad.id_abreviatura"));
        Integer IdDefDefinicion = Integer.parseInt(Textos.getCadena("diccionario","unidad.id_definicion"));


        String queryUnidad= "Select r.iden," +
        "(select valor from planeamiento.Propiedadrelacion where iddefpropiedad =" + IdDefAbreviatura + " and idrelacion=r.iden ) as abreviatura," +
        "(select valor from planeamiento.Propiedadrelacion where iddefpropiedad =" + IdDefDefinicion + " and idrelacion=r.iden ) as definicion " +
        " From planeamiento.Relacion r, planeamiento.Vectorrelacion vr " +
        " Where r.iddefrelacion=" + IdDefPropUnidad +
        " And vr.iddefvector=" + IdDeterminacionDicc +
        " And vr.valor=" + idDeterminacion +
        " And vr.idrelacion=r.iden ";
		
		  try {
	            
	            List<Object[]> propiedadUnidad = em.createNativeQuery(queryUnidad).getResultList();

	            if (propiedadUnidad.size()>0){
	                for (Object[] aUnitsAds: propiedadUnidad){
	                	resultadoUnidad.setAbreviatura(aUnitsAds[1].toString());
	                	resultadoUnidad.setDefinicion(aUnitsAds[2].toString());
	                	resultadoUnidad.setIdDeterminacion(idDeterminacion);
	                	
	                    
	                }
	            }

	        } catch (Exception ex) {
	        	log.error("[obtenerUnidadDeDeterminacion] Error al obtener la unidad de la idDeterminacion= " + idDeterminacion);
	        	ex.printStackTrace();
	        }
	        
	        log.debug("[obtenerUnidadDeDeterminacion] Unidad: idDeterminacion= " + idDeterminacion+ " Abreviatua="+resultadoUnidad.getAbreviatura()+ " Definicion= "+resultadoUnidad.getDefinicion());
	        return resultadoUnidad;
	}

   
    

}

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

package com.mitc.redes.editorfip.servicios.basicos.diccionario;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.international.StatusMessages;
import org.jboss.seam.log.Log;

import com.mitc.redes.editorfip.entidades.comunes.ParIdentificadorTexto;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Plan;

@Stateless

@Name("servicioBasicoPlanes")
public class ServicioBasicoPlanesBean implements ServicioBasicoPlanes
{
    @Logger private Log log;

    @In StatusMessages statusMessages;
    
    @PersistenceContext
	EntityManager entityManager;

    public void servicioBasicoPlanes()
    {
        // implement your business logic here
        log.info("servicioBasicoAmbitos.servicioBasicoPlanes() action called");
        statusMessages.add("servicioBasicoPlanes");
    }

    public List<Object[]> findPlanesBase() {

    	List<Object[]> lista = new ArrayList<Object[]>();
		
		String queryPlanBase = "SELECT planBase.iden,planBase.nombre " +
        " FROM com.mitc.redes.editorfip.entidades.rpm.gestionfip.PlanesBase plan " +
        " JOIN plan.plan planBase";
		
		lista = entityManager.createQuery(queryPlanBase).getResultList();
        
        return lista;
    }
    
    public List<Object[]> findPlanesBaseInicial() {

    	List<Object[]> lista = new ArrayList<Object[]>();
		
		String queryPlanBase = "SELECT planBase.iden,planBase.nombre " +
        " FROM com.mitc.redes.editorfip.entidades.rpm.gestionfip.PlanesBase plan " +
        " JOIN plan.plan planBase where plan.id < 100";
		
		lista = entityManager.createQuery(queryPlanBase).getResultList();
        
        return lista;
    }
    
    public Plan obtenerPlanDeAmbito(int iden) {

    	log.debug("[obtenerPlanDeAmbito] iden="+iden);
    	List<Plan> lista = new ArrayList<Plan>();
		
		String queryPlanBase = "SELECT plan " +
        " FROM com.mitc.redes.editorfip.entidades.rpm.planeamiento.Plan plan " +
        " WHERE plan.idambito=" + iden;
		
		lista = entityManager.createQuery(queryPlanBase).getResultList();
        
		if (lista!=null && lista.size()>0)
			return lista.get(0);
		else
			return null;
    }

	public List<Object[]> obtenerPlanesVigentesDeAmbito(int iden) {
		List<Object[]> lista = new ArrayList<Object[]>();
		
		String queryPlanBase = "SELECT planVigente.plan.iden,planVigente.plan.nombre " +
        " FROM com.mitc.redes.editorfip.entidades.rpm.gestionfip.PlanesVigente planVigente " +
        " JOIN planVigente.plan plan " +
        " WHERE plan.idambito=" + iden;
		
		lista = entityManager.createQuery(queryPlanBase).getResultList();
        
        return lista;
	}
	
	
	 public List<ParIdentificadorTexto> obtenerListaInstrumetos() {

	    	List<ParIdentificadorTexto> result = new ArrayList<ParIdentificadorTexto>();
	    	
	        String queryInstrumeto =  "SELECT inst.id, trans.texto " +
	                " FROM Instrumentoplan inst " +
	                " JOIN inst.literal.traduccions trans" +
	                " WHERE trans.literal = inst.literal ORDER BY trans.texto ASC" ;

	        
	        
	        try {
	        	List<Object[]> instrumetoList = (List<Object[]>) entityManager.createQuery(queryInstrumeto).getResultList();
	        	
	        	for (Object[] amb : instrumetoList) {
	        		ParIdentificadorTexto item = new ParIdentificadorTexto((Integer)amb[0],(String)amb[1], "instrumento");
	        		result.add(item);
	            }
	        	
	        } catch (NoResultException e) {
	            log.error("[findAmbitos] No se ha encontrado el listado de instrumeto." + e.getMessage());

	        } catch (Exception ex) {
	            log.error("[findAmbitos] Error en la busqueda del listado de instrumeto: " + ex.getMessage());

	        }
	        
	        return result;
	    }
	 
	 
	 public List<ParIdentificadorTexto> obtenerListaTipoTramites() {

	    	List<ParIdentificadorTexto> result = new ArrayList<ParIdentificadorTexto>();
	    	
	        String queryTipoTramites =  "SELECT inst.id, trans.texto " +
	                " FROM Tipotramite inst " +
	                " JOIN inst.literal.traduccions trans" +
	                " WHERE trans.literal = inst.literal ORDER BY trans.texto ASC" ;

	        
	        
	        try {
	        	List<Object[]> tipoTramiteList = (List<Object[]>) entityManager.createQuery(queryTipoTramites).getResultList();
	        	
	        	for (Object[] amb : tipoTramiteList) {
	        		ParIdentificadorTexto item = new ParIdentificadorTexto((Integer)amb[0],(String)amb[1], "tipotramite");
	        		result.add(item);
	            }
	        	
	        } catch (NoResultException e) {
	            log.error("[findAmbitos] No se ha encontrado el listado de TipoTramite." + e.getMessage());

	        } catch (Exception ex) {
	            log.error("[findAmbitos] Error en la busqueda del listado de TipoTramite: " + ex.getMessage());

	        }
	        
	        return result;
	    }
	 
	 public List<SelectItem> obtenerListaTipoOperacionPlanSelectItem() {

	    	List<ParIdentificadorTexto> elementos = obtenerListaTipoOperacionPlan();
			
	    	List<SelectItem> res = new ArrayList<SelectItem>();
			for (ParIdentificadorTexto c : elementos) {
				SelectItem i = new SelectItem();
				i.setLabel((String) c.getTexto());
				i.setValue(c.getIdBaseDatos());
				res.add(i);
			}
			
			return res;
		}
	    
	 
	 public List<ParIdentificadorTexto> obtenerListaTipoOperacionPlan() {

	    	List<ParIdentificadorTexto> result = new ArrayList<ParIdentificadorTexto>();
	    	
	        String queryTipoTramites =  "SELECT inst.id, trans.texto " +
	                " FROM Tipooperacionplan inst " +
	                " JOIN inst.literal.traduccions trans" +
	                " WHERE trans.literal = inst.literal ORDER BY trans.texto ASC" ;

	        
	        
	        try {
	        	List<Object[]> tipoTramiteList = (List<Object[]>) entityManager.createQuery(queryTipoTramites).getResultList();
	        	
	        	for (Object[] amb : tipoTramiteList) {
	        		ParIdentificadorTexto item = new ParIdentificadorTexto((Integer)amb[0],(String)amb[1], "tipooperacionplan");
	        		result.add(item);
	            }
	        	
	        } catch (NoResultException e) {
	            log.error("[findAmbitos] No se ha encontrado el listado de Tipooperacionplan." + e.getMessage());

	        } catch (Exception ex) {
	            log.error("[findAmbitos] Error en la busqueda del listado de Tipooperacionplan: " + ex.getMessage());

	        }
	        
	        return result;
	    }
	 
	 
	 public List<Object[]> obtenerInstrumetos() {

		 List<Object[]> instrumetoList = new ArrayList<Object[]>();
	        String queryInstrumeto =  "SELECT inst.id, trans.texto " +
	                " FROM Instrumentoplan inst " +
	                " JOIN inst.literal.traduccions trans" +
	                " WHERE trans.literal = inst.literal ORDER BY trans.texto ASC" ;

	        
	        
	        try {
	        	instrumetoList = (List<Object[]>) entityManager.createQuery(queryInstrumeto).getResultList();
	        	
	        } catch (NoResultException e) {
	            log.error("[findAmbitos] No se ha encontrado el listado de instrumeto." + e.getMessage());

	        } catch (Exception ex) {
	            log.error("[findAmbitos] Error en la busqueda del listado de instrumeto: " + ex.getMessage());

	        }
	        
	        return instrumetoList;
	    }
	 
	 
	 public List<Object[]> obtenerTipoTramites() {

		 List<Object[]> tipoTramiteList = new ArrayList<Object[]>();
	       String queryTipoTramites =  "SELECT inst.id, trans.texto " +
	                " FROM Tipotramite inst " +
	                " JOIN inst.literal.traduccions trans" +
	                " WHERE trans.literal = inst.literal ORDER BY trans.texto ASC" ;

	        
	        
	        try {
	        	tipoTramiteList = (List<Object[]>) entityManager.createQuery(queryTipoTramites).getResultList();
	        	
	        	
	        } catch (NoResultException e) {
	            log.error("[findAmbitos] No se ha encontrado el listado de TipoTramite." + e.getMessage());

	        } catch (Exception ex) {
	            log.error("[findAmbitos] Error en la busqueda del listado de TipoTramite: " + ex.getMessage());

	        }
	        
	        return tipoTramiteList;
	    }
	 
	 public String instrumentoString(int id) {

	        String nombreAmbito ="";
	        String queryNombreAmbito =  "SELECT trans.texto " +
	                " FROM com.mitc.redes.editorfip.entidades.rpm.diccionario.Instrumentoplan ambito " +
	                " JOIN ambito.literal.traduccions trans" +
	                " WHERE ambito.iden=" +id +
	                " AND trans.literal = ambito.literal" ;

	        

	         try {
	            nombreAmbito = (String) entityManager.createQuery(queryNombreAmbito).getSingleResult();
	        } catch (NoResultException e) {
	            log.error("[ambitoString] No se ha encontrado el instrumento." + e.getMessage());

	        } catch (Exception ex) {
	            log.error("[ambitoString] Error en la busqueda del instrumento: " + ex.getMessage());

	        }
	        
	        return nombreAmbito;
	    }
	 
	 public String tipoOperacionPlanString(int id) {

	        String nombreAmbito ="";
	        String queryNombreAmbito =  "SELECT trans.texto " +
	                " FROM com.mitc.redes.editorfip.entidades.rpm.diccionario.Tipooperacionplan ambito " +
	                " JOIN ambito.literal.traduccions trans" +
	                " WHERE ambito.iden=" +id +
	                " AND trans.literal = ambito.literal" ;

	        

	         try {
	            nombreAmbito = (String) entityManager.createQuery(queryNombreAmbito).getSingleResult();
	        } catch (NoResultException e) {
	            log.error("[ambitoString] No se ha encontrado el Tipooperacionplan." + e.getMessage());

	        } catch (Exception ex) {
	            log.error("[ambitoString] Error en la busqueda del Tipooperacionplan: " + ex.getMessage());

	        }
	        
	        return nombreAmbito;
	    }
	 
	 public String tipoTramiteString(int id) {

	        String nombreAmbito ="";
	        String queryNombreAmbito =  "SELECT trans.texto " +
	                " FROM com.mitc.redes.editorfip.entidades.rpm.diccionario.Tipotramite ambito " +
	                " JOIN ambito.literal.traduccions trans" +
	                " WHERE ambito.iden=" +id +
	                " AND trans.literal = ambito.literal" ;

	        

	         try {
	            nombreAmbito = (String) entityManager.createQuery(queryNombreAmbito).getSingleResult();
	        } catch (NoResultException e) {
	            log.error("[ambitoString] No se ha encontrado el Tipotramite." + e.getMessage());

	        } catch (Exception ex) {
	            log.error("[ambitoString] Error en la busqueda del Tipotramite: " + ex.getMessage());

	        }
	        
	        return nombreAmbito;
	 }


}

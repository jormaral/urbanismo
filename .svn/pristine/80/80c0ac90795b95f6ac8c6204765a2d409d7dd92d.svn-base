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

package com.mitc.redes.editorfip.servicios.basicos.fip.tramites;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.international.StatusMessages;
import org.jboss.seam.log.Log;

import com.mitc.redes.editorfip.entidades.busqueda.BusquedaTramiteDTO;
import com.mitc.redes.editorfip.entidades.busqueda.FiltrosDTO;
import com.mitc.redes.editorfip.entidades.comunes.ParIdentificadorTexto;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Plan;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Tramite;

@Stateless 

@Name("servicioBasicoTramites")
public class ServicioBasicoTramitesBean implements ServicioBasicoTramites {
	
	@Logger private Log log;

    @In StatusMessages statusMessages;
    
    @PersistenceContext
	EntityManager em;
    
    public int idTramiteDeDeterminacion (int idDeterminacion){
    	
    	 log.debug("[idTramiteDeDeterminacion] idDeterminacion" + idDeterminacion);
    	int idTramite = 0;
    	
    	String query = "select idtramite from planeamiento.determinacion det where det.iden ="+idDeterminacion;
    	
    	 try {
    		 idTramite = (Integer) em.createNativeQuery(query).getSingleResult();
    		 
    		 
         	
         } catch (NoResultException e) {
             log.error("[idTramiteDeDeterminacion] No se ha encontrado ." + e.getMessage());

         } catch (Exception ex) {
             log.error("[idTramiteDeDeterminacion] Error en la busqueda" + ex.getMessage());

         }
    	
    	return idTramite;
    }
    
    public List<Tramite> obtenerTramitesRaizes() {
    	
    	List<Tramite> result = new ArrayList<Tramite>();
    	
        String queryNombreAmbito =  "FROM Tramite t WHERE t.plan.planByIdpadre is null";
        
        try {
        	result = (List<Tramite>) em.createQuery(queryNombreAmbito).getResultList();
        	
        } catch (NoResultException e) {
            log.error("[servicioBasicoPlanes - obtenerPlanesRaizes()] No se ha encontrado el listado de planes raices." + e.getMessage());

        } catch (Exception ex) {
            log.error("[servicioBasicoPlanes - obtenerPlanesRaizes()] Error en la busqueda del listado de planes raices: " + ex.getMessage());

        }
        
        return result;
    }
    
    
    public List<ParIdentificadorTexto> getArbolAsignacionTramiteUsuarioRaices() {
		List<ParIdentificadorTexto> result = new ArrayList<ParIdentificadorTexto>();

		String queryString = "SELECT DISTINCT plan.idambito, trans.texto " +
        " FROM Plan plan, Ambito ambito, Literal literal, Traduccion trans " +
        " WHERE plan.idambito= ambito.iden" +
        " AND literal.iden = ambito.literal AND trans.literal = literal.iden" +
        " ORDER BY trans.texto ASC";
        
        List<Object[]> listaAmbitosPlanes = em.createQuery(queryString).getResultList();
        for (Object[] ambitoPlan : listaAmbitosPlanes) {
           ParIdentificadorTexto item = new ParIdentificadorTexto((Integer)ambitoPlan[0], (String) ambitoPlan[1], "ambito");
           
           //Siempre va a tener hojas
           item.setHoja(false);
           
           result.add(item);
        }

		return result;
	}
    
    public List<ParIdentificadorTexto> getArbolAsignacionTramiteUsuarioHijos(int idAmbito) {
        
    	
    	List<ParIdentificadorTexto> result = new ArrayList<ParIdentificadorTexto>();
        
        // Obtengo el listado
        String queryTramitePorPlan = "SELECT tram " +
        " FROM Tramite tram " +
        " WHERE tram.plan.idambito=" + idAmbito +
        " ORDER BY tram.fecha ASC";

        
        try {
        	List<Tramite> tramiteList = (List<Tramite>) em.createQuery(queryTramitePorPlan).getResultList();
        	
        	for (Tramite tram : tramiteList) {
        		
        		String tipoTramite = tipoTramiteTexto(tram.getIdtipotramite());
        		//ParIdentificadorTexto item = new ParIdentificadorTexto(tram.getIden(), "Tramite: ("+tipoTramite+" - "+tram.getIteracion()+") "+tram.getNombre(), "tramite");
        		ParIdentificadorTexto item = new ParIdentificadorTexto(tram.getIden(), "Tramite: ("+tram.getIden()+") "+tram.getNombre(), "tramite");
        		//Siempre va a ser una hoja
                item.setHoja(true);
        		
        		result.add(item);
            }
        	
        } catch (NoResultException e) {
            log.error("[getTramitePorPlan] No se ha encontrado el listado de tramites para un plan." + e.getMessage());

        } catch (Exception ex) {
            log.error("[getTramitePorPlan] Error en la busqueda del listado de tramites para un plan " + ex.getMessage());

        }
		
		

        return result;
    }
    
    
    private String ambitoString(int idAmbito) {

        String nombreAmbito ="";
        String queryNombreAmbito =  "SELECT trans.texto " +
                " FROM Ambito ambito " +
                " JOIN ambito.literal.traduccions trans" +
                " WHERE ambito=" +idAmbito +
                " AND trans.literal = ambito.literal" ;

       
         try {
            nombreAmbito = (String) em.createQuery(queryNombreAmbito).getSingleResult();
        } catch (NoResultException e) {
            log.error("[ambitoString] No se ha encontrado el ambito." + e.getMessage());

        } catch (Exception ex) {
            log.error("[ambitoString] Error en la busqueda del ambito: " + ex.getMessage());

        }
        
        return nombreAmbito;
    }
    
    private String tipoTramiteTexto(int idtipoTramite) {
        String tipoTramiteTexto = "";
        String querytipoTramiteTexto = "SELECT trans.texto " +
                " FROM Tipotramite tipoTram " +
                " JOIN tipoTram.literal.traduccions trans" +
                " WHERE tipoTram=" +idtipoTramite+
                " AND trans.literal = tipoTram.literal" ;

        
        try {
            tipoTramiteTexto = (String) em.createQuery(querytipoTramiteTexto).getSingleResult();
        } catch (NoResultException e) {
            log.error("[tipoTramiteTexto] No se ha encontrado el tipoTramiteTexto." + e.getMessage());

        } catch (Exception ex) {
            log.error("[tipoTramiteTexto] Error en la busqueda del tipoTramiteTexto: " + ex.getMessage());

        }

        return tipoTramiteTexto;
    }
    
    public List<BusquedaTramiteDTO> resultadosBusquedaAvanzadaTramite(FiltrosDTO filtros) {
        
    	
    	List<BusquedaTramiteDTO> result = new ArrayList<BusquedaTramiteDTO>();
    	
    	SimpleDateFormat formatoDeFecha = new SimpleDateFormat("yyyy/MM/dd");
    	
        // Obtengo el listado
        String query = "SELECT tram " +
        " FROM Tramite tram ";
        
        String clausulas="";
        boolean puesto = false;
        
        if (filtros.getNombre()!=null && !filtros.getNombre().equals("")){
        	puesto = true;
        	clausulas = clausulas + " lower(tram.nombre) like '%" + filtros.getNombre().toLowerCase() + "%' ";
    	}
        
        if (filtros.getTipoTramite()!=0){
        	if (puesto)
        		clausulas = clausulas + filtros.getTipoFiltro() + " tram.idtipotramite=" + filtros.getTipoTramite();
        	else{
        		puesto=true;
        		clausulas = clausulas + " tram.idtipotramite=" + filtros.getTipoTramite();
        	}
        }
        
        if (filtros.getFechaAprobacion()!=null){
        	log.info("LA FECHA ES" + filtros.getFechaAprobacion());
        	if (puesto)
        		clausulas = clausulas + filtros.getTipoFiltro() + " tram.fecha<='" + formatoDeFecha.format(filtros.getFechaAprobacion()) + "'";
        	else{
        		puesto=true;
        		clausulas = clausulas + " tram.fecha<='" + formatoDeFecha.format(filtros.getFechaAprobacion()) + "'";
        	}
        }
        
        if (puesto)
        	query = query + " where " + clausulas; 
        
        query = query + " ORDER BY tram.fecha ASC";

        
        try {
        	
        	// Obtenemos los datos para la paginacion
        	int maxResults = filtros.getMaxResultados();
        	String consulta = "SELECT COUNT(*) FROM Tramite tram ";
        	
        	if (puesto){
        		consulta += " where " + clausulas;
        	}
        	
        	Long totalRegistros = (Long) em.createQuery(consulta).getSingleResult();     
        	filtros.setTotalRegistros(totalRegistros);
        	int firstResult = (filtros.getPagina() -1) * filtros.getMaxResultados();
        	if(firstResult > totalRegistros)
        		firstResult = 0;
        	
        	List<Tramite> tramiteList = (List<Tramite>) em.createQuery(query)
        	.setMaxResults(maxResults)
        	.setFirstResult(firstResult)
        	.getResultList();
        	
        	filtros.actualizarPaginas();
        	
        	for (Tramite tram : tramiteList) {
        		 
        		BusquedaTramiteDTO dto = new BusquedaTramiteDTO();
        		String tipoTramite = tipoTramiteTexto(tram.getIdtipotramite());
        		
        		dto.setNombre(tram.getNombre());
        		dto.setFechaAprobacion(tram.getFecha());
        		dto.setTipoTramite(tipoTramite);
        		
        		if (filtros.getTipoPlanSeleccionado().equals(obtenerTipoPlan(tram.getIden())))
        			result.add(dto);
            }
        	
        } catch (NoResultException e) {
            log.error("[resultadosBusquedaAvanzadaTramite] No se ha encontrado el listado de tramites para un plan." + e.getMessage());

        } catch (Exception ex) {
            log.error("[resultadosBusquedaAvanzadaTramite] Error en la busqueda del listado de tramites para un plan " + ex.getMessage());

        }	

        return result;
    }
    
    public String obtenerTipoPlan (int idTramite)
    {
    	String tipo = "Encargado";
    	
    	String query = "SELECT count(*)" +
        " FROM PlanesEncargados ent where ent.tramiteEncargado.iden=" + idTramite;
    	
    	Long total = (Long) em.createQuery(query).getSingleResult();
    	if (total>0)
    		return "Encargado";
    	
    	query = "SELECT count(*)" +
        " FROM PlanesEncargados ent where ent.tramiteVigente.iden=" + idTramite;
    	total = (Long) em.createQuery(query).getSingleResult();
    	if (total>0)
    		return "Vigente";
    	
    	query = "SELECT count(*)" +
        " FROM PlanesBase ent where ent.tramite.iden=" + idTramite;
    	total = (Long) em.createQuery(query).getSingleResult();
    	if (total>0)
    		return "Base";
    	
    	return tipo;
    	
    }
    
    public boolean existeTramiteDeCodigoFip(String codigo) {
      
        // Obtengo el listado
        String query = "SELECT count(*) " +
        " FROM Tramite tram where LOWER(tram.codigofip) like '" + codigo.toLowerCase() + "'";
        
        List<Tramite> lista = new ArrayList<Tramite>();
        Long total = (Long) em.createQuery(query).getSingleResult();
    	if (total>0)
    		return true;
    	else
    		return false;
        
    }
    
    public Plan obtenerPlanDeCodigoFip(String codigo) {
        
        // Obtengo el listado
        String query = "SELECT tram " +
        " FROM Tramite tram where LOWER(tram.codigofip) like '" + codigo.toLowerCase() + "'";
        
        List<Tramite> lista = new ArrayList<Tramite>();
        lista = (List<Tramite>) em.createQuery(query).getResultList();
    	if (lista!=null && lista.size()>0)
    		return lista.get(0).getPlan();
    	else
    		return null;
        
    }
    
    public String nombreTramite(int idTramite) {
        
        String resultado ="";
        
        Tramite tram = em.find(Tramite.class,idTramite);
        resultado = tram.getNombre();
        
        return resultado;
        
    }
}

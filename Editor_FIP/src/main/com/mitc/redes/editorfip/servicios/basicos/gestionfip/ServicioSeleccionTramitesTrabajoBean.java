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

package com.mitc.redes.editorfip.servicios.basicos.gestionfip;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.log.Log;

import com.mitc.redes.editorfip.entidades.comunes.ParIdentificadorTexto;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Tramite;

@Stateless
@Name("servicioSeleccionTramitesTrabajo")
public class ServicioSeleccionTramitesTrabajoBean implements ServicioSeleccionTramitesTrabajo
{
    @Logger private Log log;

    @PersistenceContext
	EntityManager em;
    
    
    public List<ParIdentificadorTexto> getArbolTramitesEncargadosRaicesAsignadoPorUsuario(String username) {
		
    	
    	List<ParIdentificadorTexto> result = new ArrayList<ParIdentificadorTexto>();

    	HashMap<Integer,Integer> listaAuxiliarAmbitosAsignados = new HashMap<Integer, Integer>();
		
    	// ----------------------
		// Miro en usuarioambito y cojo los ambitos asignados a ese usuario
    	// ----------------------
		String queryAmbAsignUsuario = "SELECT distinct ua.ambito.iden " +
        " FROM UsuarioAmbito ua, PlanesEncargados pe " +
        " WHERE pe.tramiteEncargado.plan.idambito=ua.ambito.iden and ua.usuario.username = '" +username+"'";
		
		try {
        	List<Integer> ambAsignUsuarioList = (List<Integer>) em.createQuery(queryAmbAsignUsuario).getResultList();
        	
        	for (Integer ambAsignado : ambAsignUsuarioList) {
        		       	
        		// El ambito asignado lo añado a la lista auxiliar para comprobar posteriormente contra los tramites asignados
        		listaAuxiliarAmbitosAsignados.put(ambAsignado, ambAsignado);
        		
        		// El ambito asignado lo añado al arbol raiz
        		ParIdentificadorTexto item = new ParIdentificadorTexto(ambAsignado, ambitoString(ambAsignado), "ambito");
        		//Nunca va a ser una hoja
                item.setHoja(false);
        		
        		result.add(item);
            }
        	
        } catch (NoResultException e) {
            log.error("[getArbolTramitesEncargadosRaicesAsignadoPorUsuario] No se ha encontrado el listado de ambitos asignados a ese usuario." + e.getMessage());

        } catch (Exception ex) {
            log.error("[getArbolTramitesEncargadosRaicesAsignadoPorUsuario] Error en la busqueda del listado de ambitos asignados a ese usuario " + ex.getMessage());

        }
       
		
        // ----------------------
		// Luego miro en usuariotramite y cojo los ambitos de los tramites asignado a ese usuario
        // ----------------------
        
		String queryUsuarioTramAsignUsuario = "SELECT DISTINCT ut.tramite.plan.idambito " +
        " FROM UsuarioTramite ut " +
        " WHERE ut.usuario.username = '" +username+"'";
		
		try {
        	List<Integer> ambDeTramAsignUsuarioList = (List<Integer>) em.createQuery(queryUsuarioTramAsignUsuario).getResultList();
        	
        	for (Integer ambDeTramAsignado : ambDeTramAsignUsuarioList) {
        		
        		
        		// ----------------------	
        		// Si el ambito del tramite NO esta en usuario ambito, añado ese ambito
                // ----------------------
        		if (!listaAuxiliarAmbitosAsignados.containsKey((Integer)ambDeTramAsignado))
        		{
        			// El ambito asignado lo añado al arbol raiz
            		ParIdentificadorTexto item = new ParIdentificadorTexto(ambDeTramAsignado, ambitoString(ambDeTramAsignado), "ambito");
            		//Nunca va a ser una hoja
                    item.setHoja(false);
            		
            		result.add(item);
        		}
      		
            }
        	
        } catch (NoResultException e) {
            log.error("[getArbolTramitesEncargadosRaicesAsignadoPorUsuario] No se ha encontrado el listado de los ambitos de los tramites asignado a ese usuario." + e.getMessage());

        } catch (Exception ex) {
            log.error("[getArbolTramitesEncargadosRaicesAsignadoPorUsuario] Error en la busqueda del listado de los ambitos de los tramites asignado a ese usuario " + ex.getMessage());

        }
		
    

		return result;
	}
    
    public List<ParIdentificadorTexto> getArbolTramitesEncargadosHijosAsignadoPorUsuario(String username, int idAmbito) {
        
    	List<ParIdentificadorTexto> result = new ArrayList<ParIdentificadorTexto>();
    	
    	// ----------------------	
    	// compruebo si ese ambito para ese usuario esta asignado en usuarioambito
    	// ----------------------	
    	
    	String queryAmbAsignUsuario = "SELECT  ua.ambito.iden " +
        " FROM UsuarioAmbito ua " +
        " WHERE ua.ambito.iden =" +idAmbito+
        " AND ua.usuario.username = '" +username+"'";
    	
    	try {
    		
    		// ----------------------	
        	// si esta asignado, cojo todos los tramites de ese ambito. FIN
        	// ----------------------	

        	em.createQuery(queryAmbAsignUsuario).getSingleResult();
        	
        	// Obtengo el listado
            String queryTramitePorPlan = "SELECT tram " +
            " FROM Tramite tram, PlanesEncargados pe " +
            " WHERE tram.iden=pe.tramiteEncargado.iden and tram.plan.idambito=" + idAmbito +
            " ORDER BY tram.iden ASC";

            
            try {
            	List<Tramite> tramiteList = (List<Tramite>) em.createQuery(queryTramitePorPlan).getResultList();
            	
            	for (Tramite tram : tramiteList) {
            		       		
            		ParIdentificadorTexto item = new ParIdentificadorTexto(tram.getIden(), "("+tram.getIden()+") "+tram.getNombre(), "tramite");
            		//Siempre va a ser una hoja
                    item.setHoja(true);
            		
            		result.add(item);
                }
            	
            } catch (NoResultException e) {
                log.error("[getArbolTramitesEncargadosHijosAsignadoPorUsuario] No se ha encontrado el listado de tramites para un ambito." + e.getMessage());

            } catch (Exception ex) {
                log.error("[getArbolTramitesEncargadosHijosAsignadoPorUsuario] Error en la busqueda del listado de tramites para un ambito " + ex.getMessage());

            }
        	
        	
        	
        } catch (NoResultException e) {
            log.debug("[getArbolTramitesEncargadosHijosAsignadoPorUsuario] No se ha encontrado el listado de ambitos asignados a ese usuario." + e.getMessage());


        	// ----------------------	
        	// si no esta asignado, voy a la tabla usuariotramite y cojo los tramites asignados al usuario pertenecientes a ese ambito 
        	// ----------------------	
        
            String queryUsuarioTramAsignUsuario = "SELECT  ut.tramite " +
            " FROM UsuarioTramite ut " +
            " WHERE ut.usuario.username = '" +username+"' "+
            " AND ut.tramite.plan.idambito="+idAmbito;
            
            try {
            	List<Tramite> tramiteList = (List<Tramite>) em.createQuery(queryUsuarioTramAsignUsuario).getResultList();
            	
            	for (Tramite tram : tramiteList) {
            		       		
            		ParIdentificadorTexto item = new ParIdentificadorTexto(tram.getIden(), "("+tram.getIden()+") "+tram.getNombre(), "tramite");
            		//Siempre va a ser una hoja
                    item.setHoja(true);
            		
            		result.add(item);
                }
            	
            } catch (NoResultException e1) {
                log.error("[getArbolTramitesEncargadosHijosAsignadoPorUsuario] No se ha encontrado el listado de tramites para un usuario." + e1.getMessage());

            } catch (Exception ex1) {
                log.error("[getArbolTramitesEncargadosHijosAsignadoPorUsuario] Error en la busqueda del listado de tramites para un usuario " + ex1.getMessage());

            }
            
            
        } catch (Exception ex) {
            log.error("[getArbolTramitesEncargadosHijosAsignadoPorUsuario] Error en la busqueda del listado de ambitos asignados a ese usuario " + ex.getMessage());

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

}

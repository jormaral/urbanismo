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


package com.mitc.redes.editorfip.servicios.administracion.gestionusuario;

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

import com.mitc.redes.editorfip.entidades.rpm.administracionusuarios.Rol;
import com.mitc.redes.editorfip.entidades.rpm.administracionusuarios.Usuario;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Determinacion;

@Stateless 

@Name("servicioBasicoRoles")
public class ServicioBasicoRolesBean implements ServicioBasicoRoles {
	
	@Logger private Log log;

    @In StatusMessages statusMessages;
    
    @PersistenceContext
	EntityManager em;
    
    @PersistenceContext
	EntityManager entityManager;
    
    public List<Rol> obtenerRoles() {
    	
    	List<Rol> result = new ArrayList<Rol>();        
        
        try {
        	result = (List<Rol>) entityManager.createQuery("FROM Rol").getResultList();
        	
        } catch (NoResultException e) {
            log.error("[serivicioBasicoRoles - obtenerRoles] No se ha encontrado el listado de roles." + e.getMessage());

        } catch (Exception ex) {
            log.error("[serivicioBasicoRoles - obtenerRoles] Error en la busqueda del listado de roles: " + ex.getMessage());

        }
        
        return result;
    }
    
    // Metodo para usuarios nuevos
    public boolean existeDniUsuario(String dni) {
    	
    	boolean existe = false;
    	
    	String hql = "SELECT u " + "FROM Usuario u "
		+ "WHERE u.dni like '" + dni + "'";
    		
    	List<Usuario> ldet = em.createQuery(hql).getResultList();
    	
    	if (ldet!=null && ldet.size()>0)
    		existe = true;
        return existe;
    }
    
    // Metodo para usuarios ya existentes
	public boolean existeDniUsuario(String dni, String username) {
    	
    	boolean existe = false;
    	
    	String hql = "SELECT u " + "FROM Usuario u "
		+ "WHERE u.dni like '" + dni + "' " 
		+ "AND u.username not like '" + username + "'";
    		
    	List<Usuario> ldet = em.createQuery(hql).getResultList();
    	
    	if (ldet!=null && ldet.size()>0)
    		existe = true;
        return existe;
    }

}

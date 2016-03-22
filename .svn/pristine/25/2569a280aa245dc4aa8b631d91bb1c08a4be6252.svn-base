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

package com.mitc.redes.editorfip.servicios.basicos.fip.operaciones;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.log.Log;

import com.mitc.redes.editorfip.entidades.busqueda.BusquedaOperacionDTO;
import com.mitc.redes.editorfip.entidades.busqueda.FiltrosDTO;
import com.mitc.redes.editorfip.entidades.interfazgrafico.OperacionDeterminacionDTO;
import com.mitc.redes.editorfip.entidades.interfazgrafico.OperacionEntidadDTO;
import com.mitc.redes.editorfip.entidades.interfazgrafico.OperacionesDTO;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Operacion;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Operaciondeterminacion;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Operacionentidad;

@Stateless
@Name("servicioBasicoOperaciones")
public class ServicioBasicoOperacionesBean implements ServicioBasicoOperaciones
{
    @Logger private Log log;
    
    @PersistenceContext
	EntityManager em;

    
    // ------------------------------
    // OPERACIONES
    // ------------------------------
    
    public void createOperacion(Operacion operacion)
    {
    	int ordenSig = nextOrden(operacion.getTramite().getIden());
    	operacion.setOrden(ordenSig);
        em.persist(operacion);
    }

	public void editOperacion(Operacion operacion){
		 em.merge(operacion);
    }

	public void removeOperacion(Operacion operacion){
		 em.remove(em.merge(operacion));
    }

	public Operacion findOperacion(Object id){
		return em.find(Operacion.class, id);
    }
	
	public Operacion findOperacionByOrden(int orden, int idTramite){
		return (Operacion)em.createQuery(" FROM Operacion op WHERE op.orden = " + orden
															 + " AND op.tramite.iden = "+ idTramite).getSingleResult();
    }

	public List<Operacion> findAllOperacion(){
		return em.createQuery("select object(o) from Operacion as o").getResultList();
    }
	
	public int findOrdenMaximo(int idTramite){
		return (Integer) em.createQuery("select MAX(op.orden) from Operacion as op" 
				+ " WHERE op.tramite.iden = "+ idTramite).getSingleResult();
    }
    
	 private int nextOrden(int idTramite){
	    	
	    	
	    	Integer result = 0;
	    	
	    	
	        String query =  "SELECT max(op.orden) " +
	        			" FROM Operacion op" +
	        			" WHERE op.tramite = "+idTramite;

	        

	         try {
	            result = (Integer) em.createQuery(query).getSingleResult();
	            
	            if (result==null)
	            {
	            	result = 0;
	            }
	            
	            
	        } catch (NoResultException e) {
	            log.error("[nextOrden] No se ha encontrado el orden." + e.getMessage());

	        } catch (Exception ex) {
	            log.error("[nextOrden] Error en la busqueda del orden: " + ex.getMessage());

	        }
	        
	        return ((result.intValue())+1);
	    	
	    }
    
    // ------------------------------
    // OPERACIONES ENTIDAD
    // ------------------------------
    
	public void createOperacionentidad(Operacionentidad operacionentidad){
		em.persist(operacionentidad);
    }

	public void editOperacionentidad(Operacionentidad operacionentidad){
		em.merge(operacionentidad);
    }

	public void removeOperacionentidad(Operacionentidad operacionentidad){
		em.remove(em.merge(operacionentidad));
    }

	public Operacionentidad findOperacionentidad(Object id){
		
		Operacionentidad oe = em.find(Operacionentidad.class, id);
		
		oe.getEntidadByIdentidad().getNombre();
		oe.getEntidadByIdentidadoperadora().getNombre();
		
		return oe;
    }

	public List<Operacionentidad> findAllOperacionentidad(){
		return em.createQuery("select object(o) from Operacionentidad as o").getResultList();
    }
    
	public List<Operacionentidad> obtenerListaOperacionEntidad(int idTramite){
		 log.debug("[obtenerListaOperacionEntidad] Obtener Lista de OperacionEntidad del Tramite [" + idTramite + "]");

	        // Variable a devolver
	        List<Operacionentidad> listaValores = null;

	        

	        // Obtengo el listado
	        String query = " SELECT opDets "+
		 	" FROM Operacion op " +
		 	" JOIN op.operacionentidads opDets "+
		 	" WHERE op.tramite = "+idTramite;

	        
	        try {
	            listaValores = (List<Operacionentidad>) em.createQuery(query).getResultList();
	            
	            /**
	             * Tengo que recuperar manualmente los campos que vaya a necesitar de
	             * objetos relacionados, vara evitar excepcion Lazy
	             */
	            for (Operacionentidad opdet : listaValores)
	            {
	            	if (opdet.getOperacion()!=null)
	            	{
	            		opdet.getOperacion().getOrden();
	            		if (opdet.getOperacion().getTramite()!=null)
	            		{
	            			opdet.getOperacion().getTramite().getNombre();
	            		}
	            	}
	            	
	            	// Recupero el nombre de la entidad operada
	            	if (opdet.getEntidadByIdentidad()!=null )
	            	{
	            		opdet.getEntidadByIdentidad().getNombre();
	            		opdet.getEntidadByIdentidad().getTramite().getPlan().getNombre();
	            	}
	            	
	            	if (opdet.getEntidadByIdentidadoperadora()!=null)
	            	{
	            		opdet.getEntidadByIdentidadoperadora().getNombre();
	            	}
	            	
	            }
	        } catch (NoResultException e) {
	            log.error("[obtenerListaOperacionEntidad] No se ha encontrado el listado de Operaciones de Entidad." + e.getMessage());

	        } catch (Exception ex) {
	            log.error("[obtenerListaOperacionEntidad] Error en la busqueda del listado de Operaciones de Entidad: " + ex.getMessage());

	        }

	        log.debug("[obtenerListaOperacionEntidad] Obtenida Lista de Operaciones de Entidad del Tramite [" + idTramite + "]. Obtenidas " + listaValores.size() + " Operaciones de Entidad");
	        return listaValores;
    }

	public List<OperacionesDTO> obtenerListaOperacionesDTO(int idTramite){
		 log.debug("[obtenerListaOperacionesDTO] Obtener Lista de OperacionEntidadDTO del Tramite [" + idTramite + "]");

	        // Variable a devolver
	        List<OperacionesDTO> arrayReturn = new ArrayList<OperacionesDTO>();

	        // Obtengo el listado
	        String query = "select op.iden as iden, op.orden as orden, opent.iden as opent, opdet.iden as opdet "+
			        " from planeamiento.operacion op "+
			        " left join planeamiento.operacionentidad opent on opent.idoperacion = op.iden "+
			        " left join planeamiento.operaciondeterminacion opdet on opdet.idoperacion = op.iden "+
			        " where op.idtramiteordenante="+idTramite +
			        " order by orden asc";

	        List<Object[]> listaValores;
	        try {
	            listaValores = (List<Object[]>) em.createNativeQuery(query).getResultList();
	            
	            for (Object[] valorOp : listaValores)
	            {
	            	OperacionesDTO nuevaOperacion = new OperacionesDTO();
	            	
	            	// Meto el id de la operacion
	            	nuevaOperacion.setIden((Integer)valorOp[0]);
	            	
	            	// Meto el Orden
	            	nuevaOperacion.setOrden((Integer)valorOp[1]);
	            	
	            	// Averiguo el tipo que es
	            	if (valorOp[2]!=null)
	            	{
	            		// Es de entidad
	            		nuevaOperacion.setOperacionEntDet("Entidad");
	            		
	            		// 
	            		Operacionentidad oe = em.find(Operacionentidad.class, (Integer)valorOp[2]);
	            		
	            		// Recupero el nombre de la entidad operada
		            	if (oe.getEntidadByIdentidad()!=null )
		            	{
		            		nuevaOperacion.setEntidadOperada(oe.getEntidadByIdentidad().getNombre());
		            		nuevaOperacion.setTramiteOperado(oe.getEntidadByIdentidad().getTramite().getPlan().getNombre());
		            	}
		            	
		            	if (oe.getEntidadByIdentidadoperadora()!=null)
		            	{
		            		nuevaOperacion.setEntidadOperadora(oe.getEntidadByIdentidadoperadora().getNombre());
		            	}
		            	
		            	nuevaOperacion.setTipoOperacion(tipoOperacionEntidadNombre(oe.getIdtipooperacionent()));
		            	
		            	// La meto en el array a devolver
		            	arrayReturn.add(nuevaOperacion);
	            		
	            	}
	            	else
	            	{
	            		// Es de determinacion, pero lo compruebo de todas formas
	            		if (valorOp[3]!=null)
	            		{
	            			nuevaOperacion.setOperacionEntDet("Determinacion");
		            		
		            		// 
		            		Operaciondeterminacion od = em.find(Operaciondeterminacion.class, (Integer)valorOp[3]);
		            		
		            		// Recupero el nombre de la entidad operada
			            	if (od.getDeterminacionByIddeterminacion()!=null )
			            	{
			            		nuevaOperacion.setEntidadOperada(od.getDeterminacionByIddeterminacion().getNombre());
			            		nuevaOperacion.setTramiteOperado(od.getDeterminacionByIddeterminacion().getTramite().getPlan().getNombre());
			            	}
			            	
			            	if (od.getDeterminacionByIddeterminacionoperadora()!=null)
			            	{
			            		nuevaOperacion.setEntidadOperadora(od.getDeterminacionByIddeterminacionoperadora().getNombre());
			            	}
			            	
			            	nuevaOperacion.setTipoOperacion(tipoOperacionDetNombre(od.getIdtipooperaciondet()));
			            	
			            	// La meto en el array a devolver
			            	arrayReturn.add(nuevaOperacion);
	            		}
	            	}
	            	
	            }
	            
	            
	            
	        } catch (NoResultException e) {
	            log.error("[obtenerListaOperacionesDTO] No se ha encontrado el listado de Operaciones." + e.getMessage());

	        } catch (Exception ex) {
	            log.error("[obtenerListaOperacionesDTO] Error en la busqueda del listado de Operaciones: " + ex.getMessage());

	        }
	        
	        log.debug("[obtenerListaOperacionesDTO] Obtenida Lista de Operaciones del Tramite [" + idTramite + "]. Obtenidas " + arrayReturn.size() + " Operaciones");
	        return arrayReturn;
   }
	
	public List<OperacionEntidadDTO> obtenerListaOperacionEntidadDTO(int idTramite){
		 log.debug("[obtenerListaOperacionEntidadDTO] Obtener Lista de OperacionEntidadDTO del Tramite [" + idTramite + "]");

	        // Variable a recorrer
	        List<Operacionentidad> listaValores = obtenerListaOperacionEntidad(idTramite);
	        // Variable a devolver
	        List<OperacionEntidadDTO> arrayReturn = new ArrayList<OperacionEntidadDTO>();

	        int i = 0;
	        for (Iterator<Operacionentidad> iterator = listaValores.iterator(); iterator.hasNext();) {
				
	        	Operacionentidad oe = (Operacionentidad) iterator.next();
				
	        	int iden = 0;
	        	int orden = 0;
	        	String tipoOperacionEntidad = "";
	        	String entidadOperadora = "";
	        	String tramiteOperado = "";
	        	String entidadOperada = "";
	        	
	        	iden = oe.getIden();
	        	if (oe.getOperacion()!=null)
	        		orden = oe.getOperacion().getOrden();
	        	if (oe.getOperacion()!=null)
	        		tipoOperacionEntidad = tipoOperacionEntidadNombre(oe.getIdtipooperacionent());
	        	if (oe.getOperacion()!=null)
	        		entidadOperadora = oe.getEntidadByIdentidadoperadora().getNombre();
	        	if (oe.getOperacion()!=null)
	        		tramiteOperado = oe.getEntidadByIdentidad().getTramite().getPlan().getNombre()+" ("+tipoTramiteTexto(oe.getEntidadByIdentidad().getTramite().getIdtipotramite())+")"+oe.getEntidadByIdentidad().getTramite().getNombre();

	        	if (oe.getOperacion()!=null)
	        		entidadOperada = oe.getEntidadByIdentidad().getNombre();
	        	//TODO Adscripciones
				
				OperacionEntidadDTO odDTO = new OperacionEntidadDTO(iden,orden,tipoOperacionEntidad,entidadOperadora,tramiteOperado,entidadOperada);
				arrayReturn.add(odDTO);		
				
			}
	        
	        log.debug("[obtenerListaOperacionEntidadDTO] Obtenida Lista de Operaciones de Entidad DTO del Tramite [" + idTramite + "]. Obtenidas " + arrayReturn.size() + " Operaciones de Entidad");
	        return arrayReturn;
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
	
	public String tipoOperacionEntidadNombre(int id){
		 String nombre ="";
	        String queryNombreAmbito =  "SELECT trans.texto " +
	                " FROM Tipooperacionentidad tod " +
	                " JOIN tod.literal.traduccions trans" +
	                " WHERE tod=" +id +
	                " AND trans.literal = tod.literal" ;

	       
	         try {
	            nombre = (String) em.createQuery(queryNombreAmbito).getSingleResult();
	        } catch (NoResultException e) {
	            log.error("[ambitoString] No se ha encontrado el Tipooperacionentidad." + e.getMessage());

	        } catch (Exception ex) {
	            log.error("[ambitoString] Error en la busqueda del Tipooperacionentidad: " + ex.getMessage());

	        }
	        
	        return nombre;
    }

	public OperacionEntidadDTO obtenerOperacionEntidadDTO(String idOpEnt){
    	
		Operacionentidad od = findOperacionentidad(new Integer(idOpEnt));
    	
    	int iden = 0;
        int orden = 0;
        String tipoOperacionDeterminacion = "";
        String determinacionOperadora = "";
        String tramiteOperado = "";
        String determinacionOperada = "";
        	
        iden = od.getIden();
        if (od.getOperacion()!=null)
        	orden = od.getOperacion().getOrden();
        if (od.getOperacion()!=null)
        	tipoOperacionDeterminacion = tipoOperacionEntNombre(od.getIdtipooperacionent());
        if (od.getOperacion()!=null)
        	determinacionOperadora = od.getEntidadByIdentidadoperadora().getNombre();
        if (od.getOperacion()!=null)
        	tramiteOperado = od.getEntidadByIdentidad().getTramite().getPlan().getNombre()+" ("+tipoTramiteTexto(od.getEntidadByIdentidad().getTramite().getIdtipotramite())+")"+od.getEntidadByIdentidad().getTramite().getNombre();
        if (od.getOperacion()!=null)
        	determinacionOperada = od.getEntidadByIdentidad().getNombre();
			
		OperacionEntidadDTO odDTO = new OperacionEntidadDTO(iden,orden,tipoOperacionDeterminacion,determinacionOperadora,tramiteOperado,determinacionOperada);
      
        return odDTO;
    }
	
	private String tipoOperacionEntNombre(int idTipoOperacionEnt) {
		
		String nombre ="";
        String queryNombreAmbito =  "SELECT trans.texto " +
                " FROM Tipooperacionentidad tod " +
                " JOIN tod.literal.traduccions trans" +
                " WHERE tod=" +idTipoOperacionEnt +
                " AND trans.literal = tod.literal" ;

        
         try {
            nombre = (String) em.createQuery(queryNombreAmbito).getSingleResult();
        } catch (NoResultException e) {
            log.error("[ambitoString] No se ha encontrado el Tipooperacionentidad." + e.getMessage());

        } catch (Exception ex) {
            log.error("[ambitoString] Error en la busqueda del Tipooperacionentidad: " + ex.getMessage());

        }
        
        return nombre;
	}
	
	public int tipoOperacionEntId(String tipoOperacionEntNombre) {

        int id = 0;
        String query =  "SELECT tod.iden " +
                " FROM Tipooperacionentidad tod " +
                " JOIN tod.literal.traduccions trans" +
                " WHERE trans.texto= '"+tipoOperacionEntNombre+"'"+
                " AND trans.literal = tod.literal" ;

       
         try {
            id = ((Integer) em.createQuery(query).getSingleResult()).intValue();
        } catch (NoResultException e) {
            log.error("[tipoOperacionEntId] No se ha encontrado el Tipooperacionentidad." + e.getMessage());

        } catch (Exception ex) {
            log.error("[tipoOperacionEntId] Error en la busqueda del Tipooperacionentidad: " + ex.getMessage());

        }
        
        return id;
    }
	
	public List<Object[]> tipoOperacionEntidad(){
		
		List<Object[]> lista = new ArrayList<Object[]>();
    	String cadena = "SELECT tipoOpDet.iden, trans.texto " +
   	 	" FROM Tipooperacionentidad tipoOpDet " +
   	 	" JOIN tipoOpDet.literal.traduccions trans" +
   	 	" WHERE trans.literal = tipoOpDet.literal";

			
		lista = em.createQuery(cadena).getResultList();
		
		return lista;
		
	}
	
	
    
    
    // ------------------------------
    // OPERACIONES DETERMINACION
    // ------------------------------
    
	public void createOperaciondeterminacion(Operaciondeterminacion operaciondeterminacion){
		em.persist(operaciondeterminacion);
    }

	public void editOperaciondeterminacion(Operaciondeterminacion operaciondeterminacion){
		em.merge(operaciondeterminacion);
    }

	public void removeOperaciondeterminacion(Operaciondeterminacion operaciondeterminacion){
		em.remove(em.merge(operaciondeterminacion));
    }

	public Operaciondeterminacion findOperaciondeterminacion(Object id){
		// Variable a devolver
        Operaciondeterminacion valor = null;

        

        // Obtengo el listado
        String query = " SELECT opDet "+
	 	" FROM Operaciondeterminacion opDet " +
	 	" WHERE opDet = "+id;

                
        try {
        	valor = (Operaciondeterminacion) em.createQuery(query).getSingleResult();
            
            /**
             * Tengo que recuperar manualmente los campos que vaya a necesitar de
             * objetos relacionados, vara evitar excepcion Lazy
             */
           	if (valor.getOperacion()!=null)
           	{
           		Operacion op = valor.getOperacion();
           		valor.getOperacion().getIden();
           		valor.getOperacion().getOrden();
           		if (valor.getOperacion().getTramite()!=null)
           		{
           			valor.getOperacion().getTramite().getNombre();
           		}
           	}
           	
           	// Recupero el nombre de la determinacion operada
           	if (valor.getDeterminacionByIddeterminacion()!=null )
           	{
           		valor.getDeterminacionByIddeterminacion().getNombre();
           	}
           	
           	if (valor.getDeterminacionByIddeterminacionoperadora()!=null)
           	{
           		valor.getDeterminacionByIddeterminacionoperadora().getNombre();
           	}
           	
        } catch (NoResultException e) {
            log.error("[obtenerListaOperacionDeterminacion] No se ha encontrado el listado de Operaciones de Determinacion." + e.getMessage());

        } catch (Exception ex) {
            log.error("[obtenerListaOperacionDeterminacion] Error en la busqueda del listado de Operaciones de Determinacion: " + ex.getMessage());

        }
        
        return valor;
    }

	public List<Operaciondeterminacion> findAllOperaciondeterminacion(){
		return em.createQuery("select object(o) from Operaciondeterminacion as o").getResultList();
    }

	public List<Operaciondeterminacion> obtenerListaOperacionDeterminacion(int idTramite){
		log.debug("[obtenerListaOperacionDeterminacion] Obtener Lista de OperacionDeterminacion del Tramite [" + idTramite + "]");

        // Variable a devolver
        List<Operaciondeterminacion> listaValores = null;

        

        // Obtengo el listado
        String query = " SELECT opDets "+
	 	" FROM Operacion op " +
	 	" JOIN op.operaciondeterminacions opDets "+
	 	" WHERE op.tramite = "+idTramite;

        
        try {
            listaValores = (List<Operaciondeterminacion>) em.createQuery(query).getResultList();
            
            /**
             * Tengo que recuperar manualmente los campos que vaya a necesitar de
             * objetos relacionados, vara evitar excepcion Lazy
             */
            for (Operaciondeterminacion opdet : listaValores)
            {
            	if (opdet.getOperacion()!=null)
            	{
            		opdet.getOperacion().getOrden();
            		if (opdet.getOperacion().getTramite()!=null)
            		{
            			opdet.getOperacion().getTramite().getNombre();
            			opdet.getOperacion().getTramite().getIdtipotramite();
            			opdet.getOperacion().getTramite().getPlan().getNombre();
            		}
            	}
            	
            	// Recupero el nombre de la determinacion operada
            	if (opdet.getDeterminacionByIddeterminacion()!=null )
            	{
            		opdet.getDeterminacionByIddeterminacion().getNombre();
            		opdet.getDeterminacionByIddeterminacion().getTramite().getPlan().getNombre();
            	}
            	
            	if (opdet.getDeterminacionByIddeterminacionoperadora()!=null)
            	{
            		opdet.getDeterminacionByIddeterminacionoperadora().getNombre();
            	}
            	
            }
        } catch (NoResultException e) {
            log.error("[obtenerListaOperacionDeterminacion] No se ha encontrado el listado de Operaciones de Determinacion." + e.getMessage());

        } catch (Exception ex) {
            log.error("[obtenerListaOperacionDeterminacion] Error en la busqueda del listado de Operaciones de Determinacion: " + ex.getMessage());

        }

        log.debug("[obtenerListaOperacionDeterminacion] Obtenida Lista de Operaciones de Determinacion del Tramite [" + idTramite + "]. Obtenidas " + listaValores.size() + " Operaciones de Determinacion");
        return listaValores;
    }
		
	
		
	public List<OperacionDeterminacionDTO> obtenerListaOperacionDeterminacionDTO(int idTramite){
		log.debug("[obtenerListaOperacionDeterminacionDTO] Obtener Lista de OperacionDeterminacionDTO del Tramite [" + idTramite + "]");

        // Variable a recorrer
        List<Operaciondeterminacion> listaValores = obtenerListaOperacionDeterminacion(idTramite);
        // Variable a devolver
        List<OperacionDeterminacionDTO>  arrayReturn = new ArrayList<OperacionDeterminacionDTO>();

        
        for (Iterator iterator = listaValores.iterator(); iterator.hasNext();) {
			
        	Operaciondeterminacion od = (Operaciondeterminacion) iterator.next();
			
        	int iden = 0;
        	int orden = 0;
        	String tipoOperacionDeterminacion = "";
        	String determinacionOperadora = "";
        	String tramiteOperado = "";
        	String determinacionOperada = "";
        	
        	iden = od.getIden();
        	if (od.getOperacion()!=null)
        		orden = od.getOperacion().getOrden();
        	if (od.getOperacion()!=null)
        		tipoOperacionDeterminacion = tipoOperacionDetNombre(od.getIdtipooperaciondet());
        	if (od.getOperacion()!=null)
        		determinacionOperadora = od.getDeterminacionByIddeterminacionoperadora().getNombre();
        	if (od.getOperacion()!=null)
        		tramiteOperado = od.getDeterminacionByIddeterminacion().getTramite().getPlan().getNombre()+" ("+tipoTramiteTexto(od.getDeterminacionByIddeterminacion().getTramite().getIdtipotramite())+")"+od.getDeterminacionByIddeterminacion().getTramite().getNombre();
        	if (od.getOperacion()!=null)
        		determinacionOperada = od.getDeterminacionByIddeterminacion().getNombre();
			
			OperacionDeterminacionDTO odDTO = new OperacionDeterminacionDTO(iden,orden,tipoOperacionDeterminacion,determinacionOperadora,tramiteOperado,determinacionOperada);
			arrayReturn.add(odDTO);		
			
		}
        
        log.debug("[obtenerListaOperacionDeterminacionDTO] Obtenida Lista de Operaciones de Determinacion DTO del Tramite [" + idTramite + "]. Obtenidas " + arrayReturn.size() + " Operaciones de Determinacion");
        return arrayReturn;
    }
		
	public OperacionDeterminacionDTO obtenerOperacionDeterminacionDTO(String idOpDet){
		
		Operaciondeterminacion od = findOperaciondeterminacion(new Integer(idOpDet));
    	
    	int iden = 0;
        int orden = 0;
        String tipoOperacionDeterminacion = "";
        String determinacionOperadora = "";
        String tramiteOperado = "";
        String determinacionOperada = "";
        	
        iden = od.getIden();
        if (od.getOperacion()!=null)
        	orden = od.getOperacion().getOrden();
        if (od.getOperacion()!=null)
        	tipoOperacionDeterminacion = tipoOperacionDetNombre(od.getIdtipooperaciondet());
        if (od.getOperacion()!=null)
        	determinacionOperadora = od.getDeterminacionByIddeterminacionoperadora().getNombre();
        if (od.getOperacion()!=null)
        	tramiteOperado = od.getDeterminacionByIddeterminacion().getTramite().getPlan().getNombre()+" ("+tipoTramiteTexto(od.getDeterminacionByIddeterminacion().getTramite().getIdtipotramite())+")"+od.getDeterminacionByIddeterminacion().getTramite().getNombre();
        if (od.getOperacion()!=null)
        	determinacionOperada = od.getDeterminacionByIddeterminacion().getNombre();
			
		OperacionDeterminacionDTO odDTO = new OperacionDeterminacionDTO(iden,orden,tipoOperacionDeterminacion,determinacionOperadora,tramiteOperado,determinacionOperada);
      
        return odDTO;
    }
	
	public String tipoOperacionDetNombre(int idTipoOperacionDet){
		 
		String nombre ="";
	        String queryNombreAmbito =  "SELECT trans.texto " +
	                " FROM Tipooperaciondeterminacion tod " +
	                " JOIN tod.literal.traduccions trans" +
	                " WHERE tod=" +idTipoOperacionDet +
	                " AND trans.literal = tod.literal" ;

	        

	         try {
	            nombre = (String) em.createQuery(queryNombreAmbito).getSingleResult();
	        } catch (NoResultException e) {
	            log.error("[tipoOperacionDetNombre] No se ha encontrado el Tipooperaciondeterminacion." + e.getMessage());

	        } catch (Exception ex) {
	            log.error("[tipoOperacionDetNombre] Error en la busqueda del Tipooperaciondeterminacion: " + ex.getMessage());

	        }
	        
	        return nombre;
    }
		
	public int tipoOperacionDetId(String tipoOperacionDetNombre){
		
		int id = 0;
        String query =  "SELECT tod.iden " +
                " FROM Tipooperaciondeterminacion tod " +
                " JOIN tod.literal.traduccions trans" +
                " WHERE trans.texto= '"+tipoOperacionDetNombre+"'"+
                " AND trans.literal = tod.literal" ;

        

         try {
            id = ((Integer) em.createQuery(query).getSingleResult()).intValue();
        } catch (NoResultException e) {
            log.error("[tipoOperacionDetId] No se ha encontrado el Tipooperaciondeterminacion." + e.getMessage());

        } catch (Exception ex) {
            log.error("[tipoOperacionDetId] Error en la busqueda del Tipooperaciondeterminacion: " + ex.getMessage());

        }
        
        return id;
    }
	
	public List<Object[]> tipoOperacionDeterminacion(){
		
		List<Object[]> lista = new ArrayList<Object[]>();
    	String cadena = "SELECT tipoOpDet.iden, trans.texto " +
   	 	" FROM Tipooperaciondeterminacion tipoOpDet " +
   	 	" JOIN tipoOpDet.literal.traduccions trans" +
   	 	" WHERE trans.literal = tipoOpDet.literal";

				
		lista = em.createQuery(cadena).getResultList();
		
		return lista;
		
	}
	
	public List<BusquedaOperacionDTO> resultadosBusquedaAvanzadaOperacion(FiltrosDTO filtros, int idTramite) {
        
    	// Caso de busqueda sobre entidad
		if (filtros.getClaseOperacion().equals("Operacionentidad")){
	    	List<BusquedaOperacionDTO> result = new ArrayList<BusquedaOperacionDTO>();
	    	
	        // Obtengo el listado
	        String query = "SELECT ent " +
	        " FROM " + filtros.getClaseOperacion() + " ent where ent.operacion.tramite.iden = " +idTramite;
	        
	        String clausulas="";
	        boolean puesto = false;
	        
	        if (filtros.getTipoOperacion()!=0){
	        	puesto =true;
	        	clausulas = clausulas + " ent.idtipooperacionent=" + filtros.getTipoOperacion();
	        }
	        
	        if (filtros.getNombreOperadora()!=null && !filtros.getNombreOperadora().equals("")){
	        	if (puesto)
	        		clausulas = clausulas + filtros.getTipoFiltro() + " lower(ent.entidadByIdentidadoperadora.nombre) like '%" + filtros.getNombreOperadora().toLowerCase() + "%' ";
	        	else{
	        		puesto = true;
	        		clausulas = clausulas + " lower(ent.entidadByIdentidadoperadora.nombre) like '%" + filtros.getNombreOperadora().toLowerCase() + "%' ";
	        	}
	        }
	        
	        if (filtros.getNombreOperada()!=null && !filtros.getNombreOperada().equals("")){
	        	if (puesto)
	        		clausulas = clausulas + filtros.getTipoFiltro() + " lower(ent.entidadByIdentidad.nombre) like '%" + filtros.getNombreOperada().toLowerCase() + "%' ";
	        	else{
	        		puesto = true;
	        		clausulas = clausulas + " lower(ent.entidadByIdentidad.nombre) like '%" + filtros.getNombreOperada().toLowerCase() + "%' ";
	        	}
	        }
	    
	        if (puesto)
	        	query = query + " and (" + clausulas + ")"; 
	        
	        try {
	        	// Obtenemos los datos para la paginacion
	        	int maxResults = filtros.getMaxResultados();
	        	String consulta = "SELECT COUNT(*)  FROM " + filtros.getClaseOperacion() + " ent where ent.operacion.tramite.iden = " +idTramite;
	        	
	        	if (puesto){
	        		consulta += " and (" + clausulas + ")";
	        	}
	        	
	        	Long totalRegistros = (Long) em.createQuery(consulta).getSingleResult();     
	        	filtros.setTotalRegistros(totalRegistros);
	        	int firstResult = (filtros.getPagina() -1) * filtros.getMaxResultados();
	        	if(firstResult > totalRegistros)
	        		firstResult = 0;
	        	
	        	List<Operacionentidad> resultList = (List<Operacionentidad>) em.createQuery(query)
	        	.setFirstResult(firstResult)
	        	.setMaxResults(maxResults)
	        	.getResultList();
	        	
	        	filtros.actualizarPaginas();
	        	
	        	for (Operacionentidad det : resultList) {
	        		 
	        		BusquedaOperacionDTO dto = new BusquedaOperacionDTO();
	        		
	        		dto.setNombreOperada(det.getEntidadByIdentidad().getNombre());
	        		dto.setId(det.getIden());
	        		dto.setNombreOperadora(det.getEntidadByIdentidadoperadora().getNombre());
	        		dto.setClase("Entidad");
	        		
	        		dto.setTipo(tipoOperacionEntidadNombre(det.getIdtipooperacionent()));
	        		
	        		result.add(dto);
	            }
	        	
	        } catch (NoResultException e) {
	            log.error("[getTramitePorPlan] No se ha encontrado el listado de tramites para un plan." + e.getMessage());
	
	        } catch (Exception ex) {
	            log.error("[getTramitePorPlan] Error en la busqueda del listado de tramites para un plan " + ex.getMessage());
	
	        }
			
	        return result;
		}
		// caso de busqueda sobre determinacion
		else
		{
			List<BusquedaOperacionDTO> result = new ArrayList<BusquedaOperacionDTO>();
	    	
	        // Obtengo el listado
	        String query = "SELECT ent " +
	        " FROM " + filtros.getClaseOperacion() + " ent where ent.operacion.tramite.iden = " +idTramite;
	        
	        String clausulas="";
	        boolean puesto = false;
	        
	        if (filtros.getTipoOperacion()!=0){
	        	puesto =true;
	        	clausulas = clausulas + " idtipooperaciondet=" + filtros.getTipoOperacion();
	        }
	        
	        if (filtros.getNombreOperadora()!=null && !filtros.getNombreOperadora().equals("")){
	        	if (puesto)
	        		clausulas = clausulas + filtros.getTipoFiltro() + " lower(ent.determinacionByIddeterminacionoperadora.nombre) like '%" + filtros.getNombreOperadora().toLowerCase() + "%' ";
	        	else{
	        		puesto = true;
	        		clausulas = clausulas + " lower(ent.determinacionByIddeterminacionoperadora.nombre) like '%" + filtros.getNombreOperadora().toLowerCase() + "%' ";
	        	}
	        }
	        
	        if (filtros.getNombreOperada()!=null && !filtros.getNombreOperada().equals("")){
	        	if (puesto)
	        		clausulas = clausulas + filtros.getTipoFiltro() + " lower(ent.determinacionByIddeterminacion.nombre) like '%" + filtros.getNombreOperada().toLowerCase() + "%' ";
	        	else{
	        		puesto = true;
	        		clausulas = clausulas + " lower(ent.determinacionByIddeterminacion.nombre) like '%" + filtros.getNombreOperada().toLowerCase() + "%' ";
	        	}
	        }
	    
	        if (puesto)
	        	query = query + " and (" + clausulas + ")"; 
	        
	        try {
	        	
	        	// Obtenemos los datos para la paginacion
	        	int maxResults = filtros.getMaxResultados();
	        	String consulta = "SELECT COUNT(*) FROM " + filtros.getClaseOperacion() + " ent where ent.operacion.tramite.iden = " +idTramite;
	        	
	        	if (puesto){
	        		consulta += " and (" + clausulas + ")";
	        	}
	        	
	        	Long totalRegistros = (Long) em.createQuery(consulta).getSingleResult();     
	        	filtros.setTotalRegistros(totalRegistros);
	        	int firstResult = (filtros.getPagina() -1) * filtros.getMaxResultados();
	        	if(firstResult > totalRegistros)
	        		firstResult = 0;
	        	
	        	List<Operaciondeterminacion> resultList = (List<Operaciondeterminacion>) em.createQuery(query)
	        	.setMaxResults(maxResults)
	        	.setFirstResult(firstResult)
	        	.getResultList();
	        	
	        	filtros.actualizarPaginas();
	        	
	        	for (Operaciondeterminacion det : resultList) {
	        		 
	        		BusquedaOperacionDTO dto = new BusquedaOperacionDTO();
	        		
	        		dto.setNombreOperada(det.getDeterminacionByIddeterminacion().getNombre());
	        		dto.setId(det.getIden());
	        		dto.setNombreOperadora(det.getDeterminacionByIddeterminacionoperadora().getNombre());
	        		dto.setClase("Determinacion");
	        		
	        		dto.setTipo(tipoOperacionDetNombre(det.getIdtipooperaciondet()));
	        		
	        		result.add(dto);
	            }
	        	
	        } catch (NoResultException e) {
	            log.error("[getTramitePorPlan] No se ha encontrado el listado de tramites para un plan." + e.getMessage());
	
	        } catch (Exception ex) {
	            log.error("[getTramitePorPlan] Error en la busqueda del listado de tramites para un plan " + ex.getMessage());
	
	        }
			
	        return result;
		}
    }
   
}

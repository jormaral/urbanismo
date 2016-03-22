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

package com.mitc.redes.editorfip.servicios.basicos.fip.determinaciones;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.jboss.seam.annotations.Begin;
import org.jboss.seam.annotations.FlushModeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.log.Log;

import com.mitc.redes.editorfip.entidades.interfazgrafico.DeterminacionBusquedaDTO;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Determinacion;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Determinaciongrupoentidad;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Documentodeterminacion;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Opciondeterminacion;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Operaciondeterminacion;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Relacion;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Vectorrelacion;


@Stateful
@Name("servicioCRUDDeterminaciones")
public class ServicioCRUDDeterminacionesBean implements ServicioCRUDDeterminaciones
{
    @Logger private Log log;

    
    
    @PersistenceContext
	EntityManager em;

    @In(create = true, required = false)
    ServicioBasicoDeterminaciones servicioBasicoDeterminaciones;
   

    // -----------------------
    // ACCIONES
    // -----------------------
    
    public void edit(Determinacion determinacion) {
        em.merge(determinacion);
    }
    
    public int create(Determinacion determinacion) {
    	
    	determinacion.setCodigo(nextCodigo(determinacion.getTramite().getIden()));
    	
    	
        em.persist(determinacion);
        
        return determinacion.getIden();

    }
    
    
public void borrarDeterminacionesYCUsAsociadasADeterminacionRecursivo(int idDeterminacion) throws Exception{
        
   		Determinacion p = em.find(Determinacion.class, idDeterminacion);
    	
   		if (p!=null)
   		{
	    	List<Determinacion> lista = obtenerDeterminacionesPorIdPadre(p.getIden());
	   		if (lista!=null && lista.size()>0){
	   			Iterator<Determinacion> it = lista.iterator();
	   			while (it.hasNext()){
	   				borrarDeterminacionesRecursivo((it.next()).getIden());
	   			}
	   		}
	   		
	   		// --------------------
	   		// Borro las CUs asociadas a esa determinacion antes que borrar la propia determinacion
	   		// --------------------
	   		borrarCUsAsociadasADeterminacion(p.getIden());
	   		
	   		
	   		// Debo borrar los OpcionDeterminacion de cada determinacion borrada
	    	Set<Opciondeterminacion> opciones = p.getOpciondeterminacionsForIddeterminacion();
	    	if (opciones!=null && !opciones.isEmpty()){
		    	for (Opciondeterminacion opcion : opciones) {
		    		em.remove(opcion);
				}
	    	}
	    	
	    	Set<Opciondeterminacion> opciones2 = p.getOpciondeterminacionsForIddeterminacionvalorref();
	    	if (opciones2!=null && !opciones2.isEmpty()){
		    	for (Opciondeterminacion opcion : opciones2) {
		    		em.remove(opcion);
				}
	    	}
	    	
	    	// Debo borrar los documentos determinacion de cada determinacion borrada
	    	Set<Documentodeterminacion> documentosABorrar = p.getDocumentodeterminacions();
	   		if (documentosABorrar!=null && !documentosABorrar.isEmpty()){
	   			for (Documentodeterminacion documentodeterminacion : documentosABorrar) {
	   				em.remove(documentodeterminacion);
	   			}
	   		}
	   		
	   		// Debo borrar las OperacionDeterminacion relacionadas
	   		Set<Operaciondeterminacion> operadas = p.getOperaciondeterminacionsForIddeterminacion();
	   		if (operadas!=null && !operadas.isEmpty()){
	   			for (Operaciondeterminacion operaciondeterminacion : operadas) {
	   				em.remove(operaciondeterminacion);
				}
	   		}
	   		
	   		Set<Operaciondeterminacion> operadoras = p.getOperaciondeterminacionsForIddeterminacionoperadora();
	   		if (operadoras!=null && !operadoras.isEmpty()){
	   			for (Operaciondeterminacion operaciondeterminacion : operadoras) {
	   				em.remove(operaciondeterminacion);
				}
	   		}
	   		  		
	   		// Debo borrar los DeterminacionGrupoEntidad relacionados
	   		Set<Determinaciongrupoentidad> dges = p.getDeterminaciongrupoentidadsForIddeterminacion();
	   		if (dges!=null && !dges.isEmpty())
	   			for (Determinaciongrupoentidad dge : dges) {
	   				em.remove(dge);
	   			}
	   		
	   		Set<Determinaciongrupoentidad> dges2 = p.getDeterminaciongrupoentidadsForIddeterminaciongrupo();
	   		if (dges2!=null && !dges2.isEmpty())
	   			for (Determinaciongrupoentidad dge : dges2) {
	   				em.remove(dge);
	   			}
	   		
	   		// Borro la Unidad Asociada:
			borrarUnidadDeDeterminacion (p.getIden());
	   		
	   		// Ahora si: borro la determinacion
	   		em.remove(p);
   		}

   }

	public void borrarCUsAsociadasADeterminacion(int idDeterminacion)throws Exception {
		
		// -------------------
		// Query para borrar el regimen especifico
		// -------------------
		String borrarRE = "delete from planeamiento.regimenespecifico where iden in "+
						" ( "+
						" select regesp.iden from planeamiento.entidaddeterminacion entdet, planeamiento.casoentidaddeterminacion caso, "+ 
						" planeamiento.entidaddeterminacionregimen reg, planeamiento.regimenespecifico regesp "+
						" where entdet.iddeterminacion = "+idDeterminacion +" and entdet.iden = caso.identidaddeterminacion and reg.idcaso = caso.iden and regesp.identidaddeterminacionregimen = reg.iden "+ 
						" )";
		
		// Ejecuto borrar el regimen especifico
		em.createNativeQuery(borrarRE).executeUpdate();
		
		// -------------------
		// Query para borrar la entidad-determinacion regimen 
		// -------------------
		String borrarEDReg = "delete from planeamiento.entidaddeterminacionregimen where iden in "+
							" ( "+
							" select reg.iden from planeamiento.entidaddeterminacion entdet, planeamiento.casoentidaddeterminacion caso, "+
							" planeamiento.entidaddeterminacionregimen reg "+
							" where entdet.iddeterminacion = "+idDeterminacion +" and entdet.iden = caso.identidaddeterminacion and reg.idcaso = caso.iden "+
							" )";
		
		// Ejecuto borrar la entidad-determinacion regimen 
		em.createNativeQuery(borrarEDReg).executeUpdate();
		
		// -------------------
		// Query para borrar el caso de la CU 
		// -------------------
		String borrarCasoED = "delete from planeamiento.casoentidaddeterminacion where iden in "+
								" ( "+
								" select caso.iden from planeamiento.entidaddeterminacion entdet, planeamiento.casoentidaddeterminacion caso "+
								" where entdet.iddeterminacion = "+idDeterminacion +" and entdet.iden = caso.identidaddeterminacion "+
								" )";

		// Ejecuto borrar el caso de la CU
		em.createNativeQuery(borrarCasoED).executeUpdate();
		
		// -------------------
		// Query para borrar la CU 
		// -------------------
		String borrarED = "delete from planeamiento.entidaddeterminacion where iden in "+
							" ( "+
							" select entdet.iden from planeamiento.entidaddeterminacion entdet "+
							" where entdet.iddeterminacion = "+idDeterminacion +
							" )";

		// Ejecuto borrar el caso de la CU
		em.createNativeQuery(borrarED).executeUpdate();
	


	}
 
	
	 public boolean borrarUnidadDeDeterminacion (int idDetContenedora)
	    {
	    	// Instancio la variable a devolver, por si no se borra de forma correcta que devuelva la variable inicializada.
	    	boolean borradocorrecto = false;
	    	
	    	Vectorrelacion vectorRelUnidad = null;
	    	
	    	
			String cadena = "SELECT vectorrelac2 " +
	        " FROM Vectorrelacion vectorrelac, " +
	        "		Vectorrelacion vectorrelac2" +           
	        " WHERE vectorrelac.iddefvector=2 " +
	        " AND vectorrelac.valor = "+idDetContenedora+
	        " AND vectorrelac.relacion =vectorrelac2.relacion " +
	        " AND vectorrelac2.iddefvector=3" ;
			
			
			
			try {
				
				vectorRelUnidad = (Vectorrelacion)em.createQuery(cadena).getSingleResult();
				
				// Borro el vector relacion que contiene a la determinacion unidad contenida
				em.remove(vectorRelUnidad);
				
				// Busco ahora la relacion que contiene a la determinacion unidad contenedora
				String cadena2 = "SELECT vectorrelac " +
		        " FROM Vectorrelacion vectorrelac " +       
		        " WHERE vectorrelac.iddefvector=2 " +
		        " AND vectorrelac.valor = "+idDetContenedora;
				
				
				vectorRelUnidad = (Vectorrelacion)em.createQuery(cadena2).getSingleResult();
				
				// Obtengo el objeto relacion de ese vector
				Relacion rel = vectorRelUnidad.getRelacion();
				
							
				// Borro el vector relacion que contiene a la determinacion unidad contenida
				em.remove(vectorRelUnidad);
				
				// Borro la relacion que contiene a la determinacion unidad contenida y contenedora
				em.remove(rel);
				
				
				borradocorrecto = true;
						
				
			} catch (NoResultException e) {
	            log.debug("[obtenerUnidadDeDeterminacion] No se han encontrado unidades para la determinacion:"+ idDetContenedora+"." + e.getMessage());

	        } catch (Exception ex) {
	            log.error("[obtenerUnidadDeDeterminacion] Error en la busqueda de unidades para la determinacion: "+ idDetContenedora+"." +ex.getMessage());

	        }
			
			return borradocorrecto;
	    }
	
@Begin(join=true,flushMode=FlushModeType.MANUAL)
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
 public void borrarDeterminacionesRecursivo(int idDeterminacion) throws Exception{
        
   		Determinacion p = em.find(Determinacion.class, idDeterminacion);
    	
   		if (p!=null)
   		{
	    	List<Determinacion> lista = obtenerDeterminacionesPorIdPadre(p.getIden());
	   		if (lista!=null && lista.size()>0){
	   			Iterator<Determinacion> it = lista.iterator();
	   			while (it.hasNext()){
	   				borrarDeterminacionesRecursivo((it.next()).getIden());
	   			}
	   		}
	   		
	   		// Debo borrar los OpcionDeterminacion de cada determinacion borrada
	    	Set<Opciondeterminacion> opciones = p.getOpciondeterminacionsForIddeterminacion();
	    	if (opciones!=null && !opciones.isEmpty()){
		    	for (Opciondeterminacion opcion : opciones) {
		    		em.remove(opcion);
				}
	    	}
	    	
	    	Set<Opciondeterminacion> opciones2 = p.getOpciondeterminacionsForIddeterminacionvalorref();
	    	if (opciones2!=null && !opciones2.isEmpty()){
		    	for (Opciondeterminacion opcion : opciones2) {
		    		em.remove(opcion);
				}
	    	}
	    	
	    	// Debo borrar los documentos determinacion de cada determinacion borrada
	    	Set<Documentodeterminacion> documentosABorrar = p.getDocumentodeterminacions();
	   		if (documentosABorrar!=null && !documentosABorrar.isEmpty()){
	   			for (Documentodeterminacion documentodeterminacion : documentosABorrar) {
	   				em.remove(documentodeterminacion);
	   			}
	   		}
	   		
	   		// Debo borrar las OperacionDeterminacion relacionadas
	   		Set<Operaciondeterminacion> operadas = p.getOperaciondeterminacionsForIddeterminacion();
	   		if (operadas!=null && !operadas.isEmpty()){
	   			for (Operaciondeterminacion operaciondeterminacion : operadas) {
	   				em.remove(operaciondeterminacion);
				}
	   		}
	   		
	   		Set<Operaciondeterminacion> operadoras = p.getOperaciondeterminacionsForIddeterminacionoperadora();
	   		if (operadoras!=null && !operadoras.isEmpty()){
	   			for (Operaciondeterminacion operaciondeterminacion : operadoras) {
	   				em.remove(operaciondeterminacion);
				}
	   		}
	   		  		
	   		// Debo borrar los DeterminacionGrupoEntidad relacionados
	   		Set<Determinaciongrupoentidad> dges = p.getDeterminaciongrupoentidadsForIddeterminacion();
	   		if (dges!=null && !dges.isEmpty())
	   			for (Determinaciongrupoentidad dge : dges) {
	   				em.remove(dge);
	   			}
	   		
	   		Set<Determinaciongrupoentidad> dges2 = p.getDeterminaciongrupoentidadsForIddeterminaciongrupo();
	   		if (dges2!=null && !dges2.isEmpty())
	   			for (Determinaciongrupoentidad dge : dges2) {
	   				em.remove(dge);
	   			}
	   		
	   		// Borro la Unidad Asociada:
			borrarUnidadDeDeterminacion (p.getIden());
			
	   		
	   		// Ahora si: borro la determinacion
	   		em.remove(p);
	   		
			em.flush();
   		}

   }
 
	private List<Determinacion> obtenerDeterminacionesPorIdPadre(int idPadre) {

		List<Determinacion> lista = new ArrayList<Determinacion>();
		String cadena = "SELECT determinacion "
				+ " FROM Determinacion determinacion "
				+ " WHERE determinacion.determinacionByIdpadre.iden=" + idPadre;

		try {
			lista = em.createQuery(cadena).getResultList();
		} catch (NoResultException e) {
			log
					.error("[obtenerDeterminacionesPorIdPadre] No se han encontrado determinaciones para ese padre."
							+ e.getMessage());

		} catch (Exception ex) {
			log
					.error("[obtenerDeterminacionesPorIdPadre] Error en la busqueda de las determinaciones hijas de un padre: "
							+ ex.getMessage());

		}

		return lista;
	}
    
    public String nextCodigo(int idBDTramite){
    	
     	String result = "";
     	
    	
     	String query =  "SELECT max(codigo) " +
    	" FROM Determinacion det"+
    	" WHERE codigo not like '7%' and det.tramite.iden="+idBDTramite;

        
        

        try {
            result = (String) em.createQuery(query).getSingleResult();
        } catch (NoResultException e) {
            log.error("[nextCodigo] No se ha encontrado el siguiente codigo de Determinacion." + e.getMessage());

        } catch (Exception ex) {
            log.error("[nextCodigo] Error en la busqueda del siguiente codigo de Determinacion: " + ex.getMessage());

        }
        
        String s;
        if (result!=null && !result.equals(""))
        {
        	// Hay que tratar el valor para añadir los ceros que falten si fuera necesario
            Long l = Long.valueOf(result);
            l++;
            s = l.toString();
            int cerosRestantes = 10-s.length();
            
            for (int j = cerosRestantes; j > 0; j--) {
    			s = "0"+s;
    		}
        }
        else
        {
        	s="0000000001";
        }
        
        
        return s;
    	
    }
    
    public List<Determinacion> autocomplete(String word, int idTramite) {
    	
    	String query = "FROM Determinacion d " +
        "WHERE d.tramite.iden =" + idTramite + " AND lower(d.nombre) like '%" + word.toLowerCase() + "%' " +
        "ORDER BY d.nombre  ASC";
    	
    	try {
    		
			return (List<Determinacion>)em.createQuery(query).setMaxResults(10).getResultList();
			
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<Determinacion> ();
		}
    }
    
    public List<DeterminacionBusquedaDTO> autocompleteFiltros(String word, int idTramite, boolean filtroNombre,
    		boolean filtroApartado, boolean filtroEtiqueta) {
    	
    	List<DeterminacionBusquedaDTO> resDto = new ArrayList<DeterminacionBusquedaDTO>();
    	List<Object> res = new ArrayList<Object>();
    	
    	if (!filtroApartado && !filtroEtiqueta && !filtroNombre){
	    	String query = "select d.iden, d.nombre, d.apartado, d.etiqueta " +   
	    		"FROM Determinacion d " +
	        "WHERE d.tramite.iden =" + idTramite + " AND lower(d.nombre) like '%" + word.toLowerCase() + "%' " +
	        "ORDER BY d.nombre  ASC";
	    	
	    	try {
	    		
	    		res = (List<Object>)em.createQuery(query).setMaxResults(20).getResultList();
				
				for (Object resObj : res)
	    		{
					Object[] regArrayObj = (Object[]) resObj;
					DeterminacionBusquedaDTO obj = new DeterminacionBusquedaDTO();
					
					obj.setIden((Integer)regArrayObj[0]);
					if (((String)regArrayObj[1]).length()>50)
						obj.setNombre(((String)regArrayObj[1]).substring(0, 49) + "...");
					else
						obj.setNombre((String)regArrayObj[1]);
					
					obj.setApartado((String)regArrayObj[2]);
					obj.setEtiqueta((String)regArrayObj[3]);
					
					resDto.add(obj);
	    		}
				
			} catch (Exception e) {
				e.printStackTrace();
				return new ArrayList<DeterminacionBusquedaDTO> ();
			}
    	}
    	else {
    		String query = "select d.iden, d.nombre, d.apartado, d.etiqueta " +   
    		"FROM Determinacion d " +
        "WHERE d.tramite.iden =" + idTramite;
    		boolean colocado = false;
    		if (filtroNombre){
    			query = query + " AND (lower(d.nombre) like '%" + word.toLowerCase() + "%' ";
    			colocado = true;
    		}
//    		if (filtroApartado && colocado)
//    			query = query + " OR lower(d.apartado) like '%" + word.toLowerCase() + "%' ";
//    		else if (filtroApartado && !colocado){
//    			query = query + " AND (lower(d.apartado) like '%" + word.toLowerCase() + "%' ";
//    			colocado = true;
//    		}
    		if (filtroEtiqueta && colocado)
    			query = query + " OR lower(d.etiqueta) like '%" + word.toLowerCase() + "%' ";
    		else if (filtroEtiqueta && !colocado){
    			query = query + " AND (lower(d.etiqueta) like '%" + word.toLowerCase() + "%' ";
    			colocado = true;
    		}
    		if (colocado)
    			query = query + ")";
    		query = query + " ORDER BY d.nombre  ASC";
    		
    		log.debug(query);
    	try {
    		
    		if (!filtroApartado)
    			res = (List<Object>)em.createQuery(query).setMaxResults(20).getResultList();
    		else
    			res = (List<Object>)em.createQuery(query).getResultList();
			
			for (Object resObj : res)
    		{
				
				Object[] regArrayObj = (Object[]) resObj;
				DeterminacionBusquedaDTO obj = new DeterminacionBusquedaDTO();
				
				//Si está filtro apartado tenemos que ver si cumple el criterio
				String apartado = servicioBasicoDeterminaciones.obtenerOrdenCompletoDeterminacion((Integer)regArrayObj[0]);
				
				if (filtroApartado){
					if (apartado.contains(word))
					{
						obj.setIden((Integer)regArrayObj[0]);
						if (((String)regArrayObj[1]).length()>50)
							obj.setNombre(((String)regArrayObj[1]).substring(0, 49) + "...");
						else
							obj.setNombre((String)regArrayObj[1]);
						
						obj.setApartado(apartado);
						obj.setEtiqueta((String)regArrayObj[3]);
									
						resDto.add(obj);
					}
				}
				else
				{
					obj.setIden((Integer)regArrayObj[0]);
					if (((String)regArrayObj[1]).length()>50)
						obj.setNombre(((String)regArrayObj[1]).substring(0, 49) + "...");
					else
						obj.setNombre((String)regArrayObj[1]);
					
					obj.setApartado(apartado);
					obj.setEtiqueta((String)regArrayObj[3]);
								
					resDto.add(obj);
				}
    		}
			
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<DeterminacionBusquedaDTO> ();
		}
    	}
    	
    	return resDto;
    }
    
    
    
    @Remove
    public void destroy() {}

}

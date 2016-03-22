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

package com.mitc.redes.editorfip.servicios.basicos.fip.entidades;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.log.Log;

import com.mitc.redes.editorfip.entidades.busqueda.BusquedaEntidadDTO;
import com.mitc.redes.editorfip.entidades.busqueda.FiltrosDTO;
import com.mitc.redes.editorfip.entidades.comunes.ParIdentificadorTexto;
import com.mitc.redes.editorfip.entidades.interfazgrafico.EntidadDTO;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Determinacion;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Determinaciongrupoentidad;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Documentodeterminacion;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Documentoentidad;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Entidad;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Opciondeterminacion;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Operaciondeterminacion;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Tramite;

@Stateless
@Name("servicioBasicoEntidades")
public class ServicioBasicoEntidadesBean implements ServicioBasicoEntidades
{
    @Logger private Log log;
    
    @PersistenceContext
	EntityManager em;
    
    
    // ------------------------------
    // GENERICAS
    // ------------------------------	

    public int buscarEntidadPorTramiteYClave(int idTramite, String clave)
    {
    	log.debug("[buscarEntidadPorTramiteYClave] idTramite" +idTramite + " clave="+clave);
    	int resultado=0;
    	
    	
    	
		String cadena = "SELECT ent.iden " +
        " FROM Entidad ent " +
        " WHERE ent.tramite.iden=" + idTramite + " and ent.clave='"+clave+"'";
		
		try {
			resultado = (Integer) em.createQuery(cadena).getSingleResult();
		} catch (NoResultException e) {
            log.error("[buscarEntidadPorTramiteYClave] No se han encontrado la entidad." + e.getMessage());

        }catch (Exception ex) {
            log.error("[buscarEntidadPorTramiteYClave] Error en la busqueda de la entidad " + ex.getMessage());

        }
    	
    	return resultado;
    }
    
    public Entidad obtenerEntidadPorTramiteYClave(int idTramite, String clave)
    {
    	log.debug("[buscarEntidadPorTramiteYClave] idTramite" +idTramite + " clave="+clave);
    	Entidad resultado = null;
    	
    	
    	
		String cadena = "SELECT ent " +
        " FROM Entidad ent " +
        " WHERE ent.tramite.iden=" + idTramite + " and ent.clave='"+clave+"'";
		
		try {
			resultado = (Entidad) em.createQuery(cadena).getSingleResult();
		} catch (NoResultException e) {
            log.error("[buscarEntidadPorTramiteYClave] No se han encontrado la entidad." + e.getMessage());

        }catch (Exception ex) {
            log.error("[buscarEntidadPorTramiteYClave] Error en la busqueda de la entidad " + ex.getMessage());

        }
    	
    	return resultado;
    }
	
	public Entidad buscarEntidad(Object id) {
		
		Entidad ent = new Entidad();
		
		ent = em.find(Entidad.class, id);
		
		// Cargo los valores de entidad padre y entidad base
		if (ent.getEntidadByIdentidadbase()!=null)
		{
			ent.getEntidadByIdentidadbase().getNombre();
		}
		
		if (ent.getEntidadByIdpadre()!=null)
		{
			ent.getEntidadByIdpadre().getNombre();
		}
		
		return ent;
	}
	
	
	
	// ------------------------------
    // ARBOL ENTIDADES
    // ------------------------------
	
    
    public List<ParIdentificadorTexto> getEntidadesCURaices(int idTramite) {
		List<ParIdentificadorTexto> result = new ArrayList<ParIdentificadorTexto>();

		// Obtengo todas las entidades raices
		String queryTodasEntidades = "SELECT d " +
        "FROM Entidad d " +
        "WHERE d.entidadByIdpadre IS NULL AND d.tramite.iden =" + idTramite + " " +
        "ORDER BY d.clave, d.nombre ASC";
		List<Entidad> listaTodasEntidades = em.createQuery(queryTodasEntidades).getResultList();
		
		// Obtengo las entidades raices que tienen CU
		String queryEntidadesConCU = "SELECT d " + "FROM Entidad d "
				+ " JOIN d.entidaddeterminacions cu "
				+ " WHERE d.entidadByIdpadre IS NULL AND d.tramite.iden ="+idTramite 
				+ " ORDER BY d.clave, d.nombre ASC";
		List<Entidad> listaEntidadesConCU = em.createQuery(queryEntidadesConCU).getResultList();
		
		// Voy a recorrer todas las entidades, y aquellas que tengan CU lo marcare en el objeto ParIdentificadorTexto
		for (Entidad ent : listaTodasEntidades) {
			
			if (listaEntidadesConCU.contains(ent))
			{
				ParIdentificadorTexto item = new ParIdentificadorTexto(ent
						.getIden(), ent.getNombre()+" ("+ent.getClave()+")",
						"sicu");
				
				// Compruebo si es una hoja (no tiene hijos)
				item.setHoja(!tieneHija(ent.getIden()));
				
				result.add(item);
			}
			else
			{
				ParIdentificadorTexto item = new ParIdentificadorTexto(ent
						.getIden(), ent.getNombre()+" ("+ent.getClave()+")",
						"nocu");
				
				// Compruebo si es una hoja (no tiene hijos)
				item.setHoja(!tieneHija(ent.getIden()));
				
				result.add(item);
			}
			
		}

		return result;
	}
    
    public List<ParIdentificadorTexto> getEntidadesCUHijas(int idPadre) {
        List<ParIdentificadorTexto> result = new ArrayList<ParIdentificadorTexto>();
        
        
        
     // Obtengo todas las entidades hijas
		String queryTodasEntidades = "SELECT d " +
        "FROM Entidad d " +
        "WHERE d.entidadByIdpadre.iden = " + idPadre + " " +
        "ORDER BY d.clave, d.nombre ASC";
		List<Entidad> listaTodasEntidades = em.createQuery(queryTodasEntidades).getResultList();
		
		// Obtengo las entidades hijas que tienen CU
		String queryEntidadesConCU = "SELECT d " +
        " FROM Entidad d " +
        " JOIN d.entidaddeterminacions cu "+
        " WHERE d.entidadByIdpadre.iden = " + idPadre + " " +
        " ORDER BY d.clave, d.nombre ASC";
		List<Entidad> listaEntidadesConCU = em.createQuery(queryEntidadesConCU).getResultList();
		
		// Voy a recorrer todas las entidades, y aquellas que tengan CU lo marcare en el objeto ParIdentificadorTexto
		for (Entidad ent : listaTodasEntidades) {
			
			if (listaEntidadesConCU.contains(ent))
			{
				ParIdentificadorTexto item = new ParIdentificadorTexto(ent
						.getIden(), ent.getNombre()+" ("+ent.getClave()+")",
						"sicu");
				
				// Compruebo si es una hoja (no tiene hijos)
				item.setHoja(!tieneHija(ent.getIden()));
				
				result.add(item);
			}
			else
			{
				ParIdentificadorTexto item = new ParIdentificadorTexto(ent
						.getIden(), ent.getNombre()+" ("+ent.getClave()+")",
						"nocu");
				
				// Compruebo si es una hoja (no tiene hijos)
				item.setHoja(!tieneHija(ent.getIden()));
				
				result.add(item);
			}
			
		}
		
		

        return result;
    }
   
    
	public List<ParIdentificadorTexto> getEntidadesRaices(int idTramite) {
		List<ParIdentificadorTexto> result = new ArrayList<ParIdentificadorTexto>();

		// Obtengo todas las entidades raices
		String queryTodasEntidades = "SELECT d " + "FROM Entidad d "
				+ "WHERE d.entidadByIdpadre IS NULL AND d.tramite.iden ="
				+ idTramite + " " + "ORDER BY d.clave, d.nombre ASC";
		
		List<Entidad> listaTodasEntidades = em.createQuery(queryTodasEntidades)
				.getResultList();

		for (Entidad ent : listaTodasEntidades) 
		{
			ParIdentificadorTexto item = new ParIdentificadorTexto(ent
					.getIden(), ent.getNombre() + " (" + ent.getClave() + ")",
					"entidad");

			// Compruebo si es una hoja (no tiene hijos)
			item.setHoja(!tieneHija(ent.getIden()));

			result.add(item);

		}

		return result;
	}
    
    public List<ParIdentificadorTexto> getEntidadesHijas(int idPadre) {
        List<ParIdentificadorTexto> result = new ArrayList<ParIdentificadorTexto>();
        
        
        
     // Obtengo todas las entidades hijas
		String queryTodasEntidades = "SELECT d " +
        "FROM Entidad d " +
        "WHERE d.entidadByIdpadre.iden = " + idPadre + " " +
        "ORDER BY d.clave, d.nombre ASC";
		List<Entidad> listaTodasEntidades = em.createQuery(queryTodasEntidades).getResultList();
		
		for (Entidad ent : listaTodasEntidades) 
		{
			ParIdentificadorTexto item = new ParIdentificadorTexto(ent
					.getIden(), ent.getNombre() + " (" + ent.getClave() + ")",
					"entidad");

			// Compruebo si es una hoja (no tiene hijos)
			item.setHoja(!tieneHija(ent.getIden()));

			result.add(item);

		}
		
		

        return result;
    }

    private boolean tieneHija(int idEntidad)
    {
    	boolean resul = false;
    	
    	// Obtengo todas las entidades hijas
		String queryTodasEntidades = "SELECT d " +
        "FROM Entidad d " +
        "WHERE d.entidadByIdpadre.iden = " + idEntidad;
		
		List<Entidad> listaTodasEntidades = em.createQuery(queryTodasEntidades).getResultList();
    	
		if (listaTodasEntidades.size()>0)
			resul = true;
    	
    	
    	return resul;
    }
    
    public int obtenerOrdenNuevaEntidad(int idTramite, int idEntPadre) {
    	
    	log.debug("[obtenerOrdenNuevaEntidad] idTramite=" + idTramite + " / idEntPadre="+idEntPadre);
    	int orden = 0;
    	if(idTramite != 0) {
    		String consulta = "SELECT MAX(ent.orden) FROM Entidad ent WHERE ent.tramite.iden = " + idTramite;
    		if(idEntPadre != 0) 
    			consulta += " AND ent.entidadByIdpadre.iden = " + idEntPadre;
    		else
    			consulta += " AND ent.entidadByIdpadre IS NULL";
    		
    		try {
				orden = (Integer) em.createQuery(consulta).getSingleResult();
				
				 
					orden++;
				
				
			} catch (NoResultException ex)
    		{
    			// Si no obtiene resultado, iniciamos el orden a 1
    			orden = 1;
    		}
    		catch (NullPointerException ex1)
    		{
    			// Si no obtiene resultado, iniciamos el orden a 1
    			orden = 1;
    		}
    		catch (Exception e) {
				log.error(e.getCause(), null);
				e.printStackTrace();
			}
    	}
    	
    	return orden;
    }   
    
    public String obtenerCodigoNuevaEntidad(int idTramite, int idEntPadre) {
    	int codigo = 0;
    	if(idTramite != 0) {
    		String consulta = "SELECT MAX(ent.codigo) FROM Entidad ent WHERE ent.tramite.iden = " + idTramite;
    		if(idEntPadre != 0) 
    			consulta += " AND ent.entidadByIdpadre.iden = " + idEntPadre;
    		else
    			consulta += " AND ent.entidadByIdpadre IS NULL";
    		
    		try {
    			String maxCodigo = (String) em.createQuery(consulta).getSingleResult();
    			codigo = Integer.parseInt(maxCodigo.trim()) + 1;
				
				
			} catch (Exception e) {
				log.error(e.getCause(), null);
				e.printStackTrace();
			}
    	}
    	
    	return String.valueOf(codigo);
    }
    
    public boolean tieneEntidadTramite(int idTramite)
    {
		
   	boolean resultado = false;
   	
   	List<Entidad> lista = new ArrayList<Entidad>();
   	
   	String cadena = "SELECT ent " +
       " FROM com.mitc.redes.editorfip.entidades.rpm.planeamiento.Entidad ent " +
       " WHERE ent.tramite ="+idTramite;

   	try
   	{
   		lista = em.createQuery(cadena).getResultList();
   		
   		if (lista.size()>0)
   		{
   			resultado = true;
   		}
   	}
   	catch (Exception e)
   	{
   		log.error("[tieneEntidadTramite] error="+e.getLocalizedMessage());
   		e.printStackTrace();
   	}
		
		
		return resultado;
	}
    
    public Entidad obtenerEquivalente(String nombre, int idTramiteBase)
	{	
		String hql = "SELECT d" +
        " FROM Entidad d" +
        " WHERE d.tramite.iden =" + idTramiteBase +
        " AND lower(d.nombre) = '" + nombre.toLowerCase() + "'" + 
        " ORDER BY  d.nombre ASC";
		
		List<Entidad> ldet = em.createQuery(hql).getResultList();
		
		if (ldet!=null && ldet.size()>0){
			return ldet.get(0);
		}
		else
			return null;
	}
    
    public Entidad equivalenciaEntidadPlanBase(String equivalencia, int idTramiteBase)
	{	
		String hql = "SELECT d" +
        " FROM Entidad d" +
        " WHERE d.tramite.iden =" + idTramiteBase +
        " AND lower(d.nombre) = '" + equivalencia.toLowerCase() + "'" + 
        " ORDER BY  d.nombre ASC";
		
		List<Entidad> ldet = em.createQuery(hql).getResultList();
		
		if (ldet!=null && ldet.size()>0){
			return ldet.get(0);
		}
		else
			return null;
	}
   
    public void borrarEntidadesTramite(int idTramite)
	{
		List<Object[]> lista = findAllByIdTramiteSelect(idTramite);
		for (Object[] obj:lista)
		{	
			removeRecursivoEnCascada((Integer)obj[0]);
		}
	}
    
    public List<Object[]> findAllByIdTramiteSelect(int idTramite) {
		
    	List<Object[]> lista = new ArrayList<Object[]>();
    	
    	String cadena = "SELECT det.iden, det.nombre " +
        " FROM com.mitc.redes.editorfip.entidades.rpm.planeamiento.Entidad det " +
        " WHERE det.tramite ="+idTramite;

    	lista = em.createQuery(cadena).getResultList();
		
		return lista;
	}
    
    private List<Determinacion> findDeterminacionesByPadre(int idPadre) {
		
		List<Determinacion> lista = new ArrayList<Determinacion>();
		String cadena = "SELECT determinacion " +
        " FROM com.mitc.redes.editorfip.entidades.rpm.planeamiento.Determinacion determinacion " +
        " WHERE determinacion.determinacionByIdpadre.iden=" + idPadre;
		
		try {
			lista = em.createQuery(cadena).getResultList();
		} catch (NoResultException e) {
            log.error("[findDeterminacionesByPadre] No se han encontrado determinaciones para ese padre." + e.getMessage());

        } catch (Exception ex) {
            log.error("[findDeterminacionesByPadre] Error en la busqueda de las determinaciones hijas de un padre: " + ex.getMessage());

        }
		
		return lista;
	}

    public void removeRecursivoEnCascada(int idPadre) {
        
   		Determinacion p = em.find(Determinacion.class, idPadre);
    	
   		if (p!=null)
   		{
	    	List<Determinacion> lista = findDeterminacionesByPadre(p.getIden());
	   		if (lista!=null && lista.size()>0){
	   			Iterator<Determinacion> it = lista.iterator();
	   			while (it.hasNext()){
	   				removeRecursivoEnCascada((it.next()).getIden());
	   			}
	   		}
	   		
	   		// Debo borrar los OpcionDeterminacion de cada determinacion borrada
	    	Set<Opciondeterminacion> opciones = p.getOpciondeterminacionsForIddeterminacion();
	    	if (opciones!=null && !opciones.isEmpty()){
		    	for (Opciondeterminacion opcion : opciones) {
		    		em.remove(opcion);
		    		em.flush();
				}
	    	}
	    	
	    	Set<Opciondeterminacion> opciones2 = p.getOpciondeterminacionsForIddeterminacionvalorref();
	    	if (opciones2!=null && !opciones2.isEmpty()){
		    	for (Opciondeterminacion opcion : opciones2) {
		    		em.remove(opcion);
		    		em.flush();
				}
	    	}
	    	
	    	// Debo borrar los documentos determinacion de cada determinacion borrada
	    	Set<Documentodeterminacion> documentosABorrar = p.getDocumentodeterminacions();
	   		if (documentosABorrar!=null && !documentosABorrar.isEmpty()){
	   			for (Documentodeterminacion documentodeterminacion : documentosABorrar) {
	   				em.remove(documentodeterminacion);
	   				em.flush();
	   			}
	   		}
	   		
	   		// Debo borrar las OperacionDeterminacion relacionadas
	   		Set<Operaciondeterminacion> operadas = p.getOperaciondeterminacionsForIddeterminacion();
	   		if (operadas!=null && !operadas.isEmpty()){
	   			for (Operaciondeterminacion operaciondeterminacion : operadas) {
					em.remove(operaciondeterminacion);
					em.flush();
				}
	   		}
	   		
	   		Set<Operaciondeterminacion> operadoras = p.getOperaciondeterminacionsForIddeterminacionoperadora();
	   		if (operadoras!=null && !operadoras.isEmpty()){
	   			for (Operaciondeterminacion operaciondeterminacion : operadoras) {
	   				em.remove(operaciondeterminacion);
					em.flush();
				}
	   		}
	   		  		
	   		// Debo borrar los DeterminacionGrupoEntidad relacionados
	   		Set<Determinaciongrupoentidad> dges = p.getDeterminaciongrupoentidadsForIddeterminacion();
	   		if (dges!=null && !dges.isEmpty())
	   			for (Determinaciongrupoentidad dge : dges) {
	   				em.remove(dge);
	   				em.flush();
	   			}
	   		
	   		Set<Determinaciongrupoentidad> dges2 = p.getDeterminaciongrupoentidadsForIddeterminaciongrupo();
	   		if (dges2!=null && !dges2.isEmpty())
	   			for (Determinaciongrupoentidad dge : dges2) {
	   				em.remove(dge);
	   				em.flush();
	   			}
	   		
	   		// Ahora si: borro la determinacion
	   		em.remove(p);
	   		em.flush();
   		}

   }
    
    public String nextCodigo(String idBDTramite){
    	
     	String result = "";
     	String query =  "SELECT MAX(ent.codigo) FROM Entidad ent WHERE ent.tramite.iden = "+idBDTramite;        

         try {
            result = (String) em.createQuery(query).getSingleResult();
        } catch (NoResultException e) {
            log.error("[nextCodigo] No se ha encontrado el siguiente codigo de Entidad." + e.getMessage());

        } catch (Exception ex) {
            log.error("[nextCodigo] Error en la busqueda del siguiente codigo de Entidad: " + ex.getMessage());

        }
        
        String s;
        if (result!=null)
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
    
    public List<BusquedaEntidadDTO> resultadosBusquedaAvanzadaTramite(FiltrosDTO filtros, int idTramite) {
        
    	
    	List<BusquedaEntidadDTO> result = new ArrayList<BusquedaEntidadDTO>();
    	boolean puesto = false;
    	
        // Obtengo el listado
        String query = "SELECT ent " +
        " FROM Entidad ent where ent.tramite.iden=" + idTramite;
        
        String clausulas = "";
        
        if (filtros.getNombre()!=null && !filtros.getNombre().equals("")){
        	puesto = true;
        	clausulas = clausulas + " lower(ent.nombre) like '%" + filtros.getNombre().toLowerCase() + "%' ";
        }
        //else
        //	query = query + " and (ent.nombre like '%' ";
        
        if (filtros.getClave()!=null && !filtros.getClave().equals("")){
        	if (puesto)
        		clausulas = clausulas + filtros.getTipoFiltro() + " lower(ent.clave) like '%" + filtros.getClave().toLowerCase() + "%' ";
        	else{
        		puesto = true;
        		clausulas = clausulas + " lower(ent.clave) like '%" + filtros.getClave().toLowerCase() + "%' ";
        	}
        }
        
        if (filtros.getEtiqueta()!=null && !filtros.getEtiqueta().equals("")){
        	if (puesto)
        		clausulas = clausulas + filtros.getTipoFiltro() + " lower(ent.etiqueta) like '%" + filtros.getEtiqueta().toLowerCase() + "%' ";
        	else{
        		puesto = true;
        		clausulas = clausulas + " lower(ent.etiqueta) like '%" + filtros.getEtiqueta().toLowerCase() + "%' ";
        	}
        }
        
        if (puesto)
        	query = query + " and (" + clausulas + ")"; 
        
        log.info("CONSULTA" + query);
        //query = query + " ORDER BY ent.fecha ASC";
        
        log.info("TIENE GEOMETRIA" + filtros.getTieneGeometria());
        Tramite tram = em.find(Tramite.class, idTramite);
        
        try {
        	
        	// Obtenemos los datos para la paginacion
        	int maxResults = filtros.getMaxResultados();
        	String consulta = "SELECT COUNT(*) FROM Entidad ent where ent.tramite.iden=" + idTramite;
        	
        	if (puesto){
        		consulta += " and (" + clausulas + ")";
        	}
        	
        	Long totalRegistros = (Long) em.createQuery(consulta).getSingleResult();     
        	filtros.setTotalRegistros(totalRegistros);
        	int firstResult = (filtros.getPagina() -1) * filtros.getMaxResultados();
        	if(firstResult > totalRegistros)
        		firstResult = 0;
        	
        	
        	List<Entidad> resultList = (List<Entidad>) em.createQuery(query)
        	.setMaxResults(maxResults)
        	.setFirstResult(firstResult)
        	.getResultList();
        	
        	filtros.actualizarPaginas();
        	
        	for (Entidad ent : resultList) {
        		 
        		BusquedaEntidadDTO dto = new BusquedaEntidadDTO();
        		
        		dto.setNombre(ent.getNombre());
        		dto.setId(ent.getIden());
        		dto.setClave(ent.getClave());
        		dto.setEtiqueta(ent.getEtiqueta());
        		dto.setTieneGeometria(tieneGeometria(ent.getIden()));
        		dto.setTipoPlan(obtenerTipoPlan(idTramite));
        		
        		
        		//Lo agregamos depende de la geometria
        		if (filtros.getTieneGeometria().equals("0"))
        			result.add(dto);
        		else if (filtros.getTieneGeometria().equals("2") && dto.getTieneGeometria().equals("No"))
        			result.add(dto);
        		else if (filtros.getTieneGeometria().equals("1") && dto.getTieneGeometria().equals("Si"))
        			result.add(dto);
            }
        	
        } catch (NoResultException e) {
            log.error("[getTramitePorPlan] No se ha encontrado el listado de tramites para un plan." + e.getMessage());

        } catch (Exception ex) {
            log.error("[getTramitePorPlan] Error en la busqueda del listado de tramites para un plan " + ex.getMessage());

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
    
    public String tieneGeometria (int idEntidad)
    {
    	String tiene = "No";
    	
    	String query = "SELECT count(*)" +
        " FROM Entidadpol ent where ent.entidad.iden=" + idEntidad;
    	
    	Long total = (Long) em.createQuery(query).getSingleResult();
    	if (total>0)
    		return "Si";
    	
    	query = "SELECT count(*)" +
        " FROM Entidadlin ent where ent.entidad.iden=" + idEntidad;
    	total = (Long) em.createQuery(query).getSingleResult();
    	if (total>0)
    		return "Si";
    	
    	query = "SELECT count(*)" +
        " FROM Entidadpnt ent where ent.entidad.iden=" + idEntidad;
    	total = (Long) em.createQuery(query).getSingleResult();
    	if (total>0)
    		return "Si";
    	
    	return tiene;
    	
    }
    
    public List<EntidadDTO> obtenerEntidadesDocumento(int idDoc) {

    	List<EntidadDTO> result = new ArrayList<EntidadDTO>();
        

        String query= "SELECT det.entidad " +
        " FROM Documentoentidad det " +
        " WHERE det.documento.iden = " +idDoc;
        
        try {
        	
        	List<Entidad> ldet = em.createQuery(query).getResultList();
            
            
            for (Entidad ent : ldet) {
            	
            	EntidadDTO dto = new EntidadDTO();
            	 
            	dto.setNombre(ent.getNombre());
        		dto.setIden(ent.getIden());
        		dto.setClave(ent.getClave());
        		dto.setEtiqueta(ent.getEtiqueta());
        		 
            	 result.add(dto);
            	                	
               
            }
        	
        }
        catch (NoResultException ex)
		{
        	log.error("[obtenerEntidadesDocumento] ERROR:"+ex.getCause());
        	ex.printStackTrace();
		}
		catch (NullPointerException ex1)
		{
			log.error("[obtenerEntidadesDocumento] ERROR:"+ex1.getCause());
			ex1.printStackTrace();
		}
		catch (Exception e) {
			log.error("[obtenerEntidadesDocumento] ERROR:"+e.getCause());
			e.printStackTrace();
		}
        

        return result;
    }
    
    public void borrarRelacionEntidadConDocumento(int idEntidad, int idDoc)
    {
    	String query= "SELECT det" +
        " FROM Documentoentidad det " +
        " WHERE det.documento.iden = " +idDoc + 
        " AND det.entidad.iden=" + idEntidad;
        
        try {
        	
        	List<Documentoentidad> ldet = em.createQuery(query).getResultList();
            
            if (ldet!=null && ldet.size()>0){
            	Documentoentidad det = ldet.get(0);
            	
            	em.remove(det);
            	em.flush();
            }
        	
        }
        catch (NoResultException ex)
		{
        	log.error("[borrarRelacionDeterminacionConDocumento] ERROR:"+ex.getCause());
        	ex.printStackTrace();
		}
		catch (NullPointerException ex1)
		{
			log.error("[borrarRelacionDeterminacionConDocumento] ERROR:"+ex1.getCause());
			ex1.printStackTrace();
		}
		catch (Exception e) {
			log.error("[borrarRelacionDeterminacionConDocumento] ERROR:"+e.getCause());
			e.printStackTrace();
		}
        
    }
    
    public int obtenerIdGrupoAplicacionPlanBase(String etiqueta, int idTramiteBase)
	{
		String query =  "SELECT det " +
		" FROM Determinacion det " +
		" WHERE det.etiqueta='"+etiqueta+"'" +
		" AND det.tramite.iden="+idTramiteBase;
		
		List<Determinacion> lista = (List<Determinacion>) em.createQuery(query).getResultList();
		
		if (lista!=null && lista.size()>0)
			return ((Determinacion)lista.get(0)).getIden();
		else
			return -1;
	}
}

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

package com.mitc.redes.editorfip.servicios.basicos.fip.documentos;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.log.Log;

import com.mitc.redes.editorfip.entidades.busqueda.BusquedaDocumentoDTO;
import com.mitc.redes.editorfip.entidades.busqueda.FiltrosDTO;
import com.mitc.redes.editorfip.entidades.interfazgrafico.DocumentoDTO;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Determinacion;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Documento;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Documentodeterminacion;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Documentoentidad;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Documentoshp;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Entidad;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Tramite;

@Stateless
@Name("servicioBasicoDocumentos")
public class ServicioBasicoDocumentosBean implements ServicioBasicoDocumentos
{
    @Logger private Log log;

 
    @PersistenceContext
	EntityManager em;
    
    public List<DocumentoDTO> obtenerListaDocumentoDTO(int idTramite) {
        
    	
    	List<DocumentoDTO> result = new ArrayList<DocumentoDTO>();
    	
        // Obtengo el listado
        String query = "SELECT ent " +
        " FROM Documento ent where ent.tramite.iden=" +idTramite + " order by ent.idgrupodocumento";
        
        Tramite tram = em.find(Tramite.class, idTramite);
        
        try {
        	List<Documento> resultList = (List<Documento>) em.createQuery(query).getResultList();
        	
        	for (Documento det : resultList) {
        		 
        		DocumentoDTO dto = new DocumentoDTO();
        		
        		dto.setNombre(det.getNombre());
        		dto.setArchivo(det.getArchivo());
        		dto.setIden(det.getIden());
        		
        		dto.setGrupoDocumento(grupoDocumentoNombre(det.getIdgrupodocumento()));

        		dto.setTipoDocumento(tipoDocumentoNombre(det.getIdtipodocumento()));
        		
        		if (dto.getArchivo()!=null &&
        				dto.getArchivo().contains(".")){
        			dto.setEsHoja("No");
        		}
        		else
        			dto.setEsHoja("Si");
        		result.add(dto);
            }
        	
        } catch (NoResultException e) {
            log.error("[getTramitePorPlan] No se ha encontrado el listado de tramites para un plan." + e.getMessage());

        } catch (Exception ex) {
            log.error("[getTramitePorPlan] Error en la busqueda del listado de tramites para un plan " + ex.getMessage());

        }
		
        return result;
    }
    
    public List<BusquedaDocumentoDTO> resultadosBusquedaAvanzadaDocumento(FiltrosDTO filtros, int idTramite) {
        
    	
    	List<BusquedaDocumentoDTO> result = new ArrayList<BusquedaDocumentoDTO>();
    	
        // Obtengo el listado
        String query = "SELECT ent " +
        " FROM Documento ent where ent.tramite.iden = " +idTramite;
        
        String clausulas="";
        boolean puesto = false;
        
        if (filtros.getTitulo()!=null && !filtros.getTitulo().equals("")){
        	if (puesto)
        		clausulas = clausulas + " lower(ent.nombre) like '%" + filtros.getTitulo().toLowerCase() + "%' ";
        	else{
        		puesto = true;
        		clausulas = clausulas + " lower(ent.nombre) like '%" + filtros.getTitulo().toLowerCase() + "%' ";
        	}
        }
        
        if (filtros.getNombreArchivo()!=null && !filtros.getNombreArchivo().equals("")){
        	if (puesto)
        		clausulas = clausulas + filtros.getTipoFiltro() + " lower(ent.archivo) like '%" + filtros.getNombreArchivo().toLowerCase() + "%' ";
        	else{
        		puesto = true;
        		clausulas = clausulas + " lower(ent.archivo) like '%" + filtros.getNombreArchivo().toLowerCase() + "%' ";
        	}
        }
        
        if (filtros.getGrupoDoc()!=0){
        	if (puesto)
        		clausulas = clausulas + filtros.getTipoFiltro() + " ent.idgrupodocumento=" + filtros.getGrupoDoc() + " ";
        	else{
        		puesto = true;
        		clausulas = clausulas + " ent.idgrupodocumento=" + filtros.getGrupoDoc() + " ";
        	}
        }
        
        if (filtros.getTipoDoc()!=0){
        	if (puesto)
        		clausulas = clausulas + filtros.getTipoFiltro() + " ent.idtipodocumento=" + filtros.getTipoDoc() + " ";
        	else{
        		puesto = true;
        		clausulas = clausulas + " ent.idtipodocumento=" + filtros.getTipoDoc() + " ";
        	}
        }
        
        if (puesto)
        	query = query + " and (" + clausulas + ")"; 
        
        Tramite tram = em.find(Tramite.class, idTramite);
        
        try {
        	
        	// Obtenemos los datos para la paginacion
        	int maxResults = filtros.getMaxResultados();
        	String consulta = "SELECT COUNT(*) FROM Documento ent where ent.tramite.iden = " +idTramite;
        	
        	if (puesto){
        		consulta += " and (" + clausulas + ")";
        	}
        	
        	Long totalRegistros = (Long) em.createQuery(consulta).getSingleResult();     
        	filtros.setTotalRegistros(totalRegistros);
        	int firstResult = (filtros.getPagina() -1) * filtros.getMaxResultados();
        	if(firstResult > totalRegistros)
        		firstResult = 0;
        	
        	log.debug("[resultadosBusquedaAvanzadaDocumento] query= "+query);
        	log.debug("[resultadosBusquedaAvanzadaDocumento] maxResults= "+maxResults);
        	log.debug("[resultadosBusquedaAvanzadaDocumento] firstResult= "+firstResult);
        	
        	List<Documento> resultList = (List<Documento>) em.createQuery(query)
        	.setMaxResults(maxResults)
        	.setFirstResult(firstResult)
        	.getResultList();
        	
        	log.debug("[resultadosBusquedaAvanzadaDocumento] resultList TOTAL= "+resultList.size());
        	
        	filtros.actualizarPaginas();
        	
        	for (Documento det : resultList) {
        		 
        		BusquedaDocumentoDTO dto = new BusquedaDocumentoDTO();
        		
        		dto.setTitulo(det.getNombre());
        		dto.setId(det.getIden());
        		dto.setNombreArchivo(det.getArchivo());
        		
        		dto.setGrupo(grupoDocumentoNombre(det.getIdgrupodocumento()));
        		dto.setPlan(tram.getPlan().getNombre());
        		dto.setTipo(tipoDocumentoNombre(det.getIdtipodocumento()));
        		
        		//miramos documento entidad
        		dto.setEntidadAsociada(entidadAsociada(det.getIden()));
        		dto.setDeterminacionAsociada(determinacionAsociada(det.getIden()));
        		
        		if(det.getArchivo()!=null && det.getArchivo().contains("."))
        			dto.setCarpeta(false);
        		else
        			dto.setCarpeta(true);
        		
        		// Contemplamos las posibilidades
        		if (filtros.getTipoFiltro().equals("and")){
        			if (filtros.getAsociadoEntidad()==0 && filtros.getAsociadoDeterminacion()==0)
        				result.add(dto);
        			else if (filtros.getAsociadoEntidad()==1 && !dto.getEntidadAsociada().equals(""))
        			{
        				if (filtros.getAsociadoDeterminacion()==1 && !dto.getDeterminacionAsociada().equals(""))
                			result.add(dto);
        			}
        			else if (filtros.getAsociadoEntidad()==1 && !dto.getEntidadAsociada().equals(""))
        			{
        				if (filtros.getAsociadoDeterminacion()==2 && dto.getDeterminacionAsociada().equals(""))
                			result.add(dto);
        			}
        			else if (filtros.getAsociadoEntidad()==2 && dto.getEntidadAsociada().equals(""))
        			{
        				if (filtros.getAsociadoDeterminacion()==1 && !dto.getDeterminacionAsociada().equals(""))
                			result.add(dto);
        			}
        			else if (filtros.getAsociadoEntidad()==0 && 
        					((filtros.getAsociadoDeterminacion()==1 && !dto.getDeterminacionAsociada().equals("")) ||
        							filtros.getAsociadoDeterminacion()==2 && dto.getDeterminacionAsociada().equals(""))){
        				result.add(dto);
        			}
        			else if (filtros.getAsociadoDeterminacion()==0 && 
        					((filtros.getAsociadoEntidad()==1 && !dto.getEntidadAsociada().equals("")) ||
        							filtros.getAsociadoEntidad()==2 && dto.getEntidadAsociada().equals(""))){
        				result.add(dto);
        			}
        			//else if ()
        		}
        		
        		if (filtros.getTipoFiltro().equals("or")){
        			if (filtros.getAsociadoEntidad()==0 || filtros.getAsociadoDeterminacion()==0)
        				result.add(dto);
        			else if ((filtros.getAsociadoDeterminacion()==1 && !dto.getDeterminacionAsociada().equals("")) ||
        					(filtros.getAsociadoDeterminacion()==2 && dto.getDeterminacionAsociada().equals("")) ||
        					(filtros.getAsociadoEntidad()==1 && !dto.getEntidadAsociada().equals("")) ||
        					(filtros.getAsociadoEntidad()==2 && dto.getEntidadAsociada().equals(""))){
        				result.add(dto);
        					}
        		}
            }
        	
        } catch (NoResultException e) {
            log.error("[getTramitePorPlan] No se ha encontrado el listado de tramites para un plan." + e.getMessage());

        } catch (Exception ex) {
            log.error("[getTramitePorPlan] Error en la busqueda del listado de tramites para un plan " + ex.getMessage());

        }
		
        return result;
    }
    
    public String grupoDocumentoNombre(int id) {

        String nombre ="";
        String queryNombreAmbito =  "SELECT trans.texto " +
                " FROM Grupodocumento tod " +
                " JOIN tod.literal.traduccions trans" +
                " WHERE tod=" +id +
                " AND trans.literal = tod.literal" ;

      

         try {
            nombre = (String) em.createQuery(queryNombreAmbito).getSingleResult();
        } catch (NoResultException e) {
            log.error("[grupoDocumentoNombre] No se ha encontrado el Grupodocumento." + e.getMessage());

        } catch (Exception ex) {
            log.error("[grupoDocumentoNombre] Error en la busqueda del Grupodocumento: " + ex.getMessage());

        }
        
        return nombre;
    }
    
    public String tipoDocumentoNombre(int id) {

        String nombre ="";
        String queryNombreAmbito =  "SELECT trans.texto " +
                " FROM Tipodocumento tod " +
                " JOIN tod.literal.traduccions trans" +
                " WHERE tod=" +id +
                " AND trans.literal = tod.literal" ;

       

         try {
            nombre = (String) em.createQuery(queryNombreAmbito).getSingleResult();
        } catch (NoResultException e) {
            log.error("[tipoDocumentoNombre] No se ha encontrado el Tipodocumento." + e.getMessage());

        } catch (Exception ex) {
            log.error("[tipoDocumentoNombre] Error en la busqueda del Tipodocumento: " + ex.getMessage());

        }
        
        return nombre;
    }
    
    public String entidadAsociada(int id) {

        String nombre ="";
        String queryNombreAmbito =  "SELECT dent.entidad.nombre " +
                " FROM Documentoentidad dent " +
                " WHERE dent.documento.id=" +id;             

      

         try {
            nombre = (String) em.createQuery(queryNombreAmbito).getSingleResult();
            log.info("DOCUMENTO ENTIDAD--" + nombre);
        } catch (NoResultException e) {
            log.error("[entidadAsociada] No se ha encontrado el Grupodocumento." + e.getMessage());

        } catch (Exception ex) {
            log.error("[entidadAsociada] Error en la busqueda del Grupodocumento: " + ex.getMessage());

        }
        
        return nombre;
    }
    
    public String determinacionAsociada(int id) {

        String nombre ="";
        String queryNombreAmbito =  "SELECT dent.determinacion.nombre " +
                " FROM Documentodeterminacion dent " +
                " WHERE dent.documento.id=" +id;             

      

         try {
            nombre = (String) em.createQuery(queryNombreAmbito).getSingleResult();
            
            log.info("DOCUMENTO DETERMINACION--" + nombre);
        } catch (NoResultException e) {
            log.error("[determinacionAsociada] No se ha encontrado el Grupodocumento." + e.getMessage());

        } catch (Exception ex) {
            log.error("[determinacionAsociada] Error en la busqueda del Grupodocumento: " + ex.getMessage());

        }
        
        return nombre;
    }
    
    public List<SelectItem> tiposDocumentoList() {

        String queryNombreAmbito =  "SELECT tod.iden, trans.texto " +
                " FROM Tipodocumento tod " +
                " JOIN tod.literal.traduccions trans" +
                " WHERE trans.literal = tod.literal" ;

       

        List<Object[]> elementos = em.createQuery(queryNombreAmbito).getResultList();
		
    	List<SelectItem> res = new ArrayList<SelectItem>();
    	
    	SelectItem todos = new SelectItem();
    	todos.setLabel("Todos");
    	todos.setValue(0);
    	res.add(todos);
    	
		for (Object[] c : elementos) {
			SelectItem i = new SelectItem();
			i.setLabel((String) c[1]);
			i.setValue(c[0]);
			res.add(i);
		}
		
		return res;
    }
    
    public List<SelectItem> grupoDocumentoList() {

        String queryNombreAmbito =  "SELECT tod.iden, trans.texto " +
        " FROM Grupodocumento tod " +
        " JOIN tod.literal.traduccions trans" +
        " WHERE trans.literal = tod.literal" ;

       

        List<Object[]> elementos = em.createQuery(queryNombreAmbito).getResultList();
		
    	List<SelectItem> res = new ArrayList<SelectItem>();
    	
    	SelectItem todos = new SelectItem();
    	todos.setLabel("Todos");
    	todos.setValue(0);
    	res.add(todos);
    	
		for (Object[] c : elementos) {
			SelectItem i = new SelectItem();
			i.setLabel((String) c[1]);
			i.setValue(c[0]);
			res.add(i);
		}
		
		return res;
    }
    
    public List<SelectItem> tiposDocumentoAltaList() {

        String queryNombreAmbito =  "SELECT tod.iden, trans.texto " +
                " FROM Tipodocumento tod " +
                " JOIN tod.literal.traduccions trans" +
                " WHERE trans.literal = tod.literal" ;

        List<Object[]> elementos = em.createQuery(queryNombreAmbito).getResultList();
    	List<SelectItem> res = new ArrayList<SelectItem>();
    	
		for (Object[] c : elementos) {
			SelectItem i = new SelectItem();
			i.setLabel((String) c[1]);
			i.setValue(c[0]);
			res.add(i);
		}
		
		return res;
    }
    
    public List<SelectItem> grupoDocumentoAltaList() {

        String queryNombreAmbito =  "SELECT tod.iden, trans.texto " +
        " FROM Grupodocumento tod " +
        " JOIN tod.literal.traduccions trans" +
        " WHERE trans.literal = tod.literal" ;

        List<Object[]> elementos = em.createQuery(queryNombreAmbito).getResultList();
		
    	List<SelectItem> res = new ArrayList<SelectItem>();
    	
		for (Object[] c : elementos) {
			SelectItem i = new SelectItem();
			i.setLabel((String) c[1]);
			i.setValue(c[0]);
			res.add(i);
		}
		
		return res;
    }
    
    public void crearDocumentoDeterminacion(int idDet, int idDoc)
    {
    	Documentodeterminacion docDet = new Documentodeterminacion();
    	
    	docDet.setDeterminacion(em.find(Determinacion.class, idDet));
    	docDet.setDocumento(em.find(Documento.class, idDoc));
    	
    	em.persist(docDet);
    	em.flush();
    
    }
    
    public void crearDocumentoEntidad(int idEnt, int idDoc)
    {
    	Documentoentidad docDet = new Documentoentidad();
    	
    	docDet.setEntidad(em.find(Entidad.class, idEnt));
    	docDet.setDocumento(em.find(Documento.class, idDoc));
    	
    	em.persist(docDet);
    	em.flush();
    
    }
    
    public List<DocumentoDTO> obtenerListaDocumentoHoja(int idDoc) {
        
    	
    	List<DocumentoDTO> result = new ArrayList<DocumentoDTO>();
    	
        // Obtengo el listado
        String query = "SELECT ent " +
        " FROM Documentoshp ent where ent.documento.iden=" +idDoc;
        
        try {
        	List<Documentoshp> resultList = (List<Documentoshp>) em.createQuery(query).getResultList();
        	
        	for (Documentoshp det : resultList) {
        		 
        		DocumentoDTO dto = new DocumentoDTO();
        		
        		dto.setArchivo(det.getHoja());
        		dto.setIden(det.getIden());
        		dto.setEsHoja("Si");        			
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
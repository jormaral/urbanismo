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

package com.mitc.redes.editorfip.servicios.produccionfip.gestiondiccionariodeterminaciones;

import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.faces.event.ActionEvent;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.faces.FacesManager;
import org.jboss.seam.international.StatusMessages;
import org.jboss.seam.international.StatusMessage.Severity;
import org.jboss.seam.log.Log;

import com.mitc.redes.editorfip.entidades.interfazgrafico.RegulacionEspecificaDTO;
import com.mitc.redes.editorfip.servicios.basicos.fip.determinaciones.ServicioBasicoDeterminaciones;
import com.mitc.redes.editorfip.servicios.genericos.JsfUtil;

@Scope(ScopeType.SESSION)
@Stateful
@Name("gestionRegulacionEspecifica")
public class GestionRegulacionEspecificaBean implements GestionRegulacionEspecifica
{
    @Logger private Log log;
    
    @In StatusMessages statusMessages;

    
    @In(create = true, required = false)
	ServicioBasicoDeterminaciones servicioBasicoDeterminaciones;
    
    @In (create = true, required = false)
	GestionDeterminaciones gestionDeterminaciones;
    
    // Variable que va a almacenar la determinacion a gestionar (crear, editar, etc)
	private RegulacionEspecificaDTO regulacionEspecifica = new RegulacionEspecificaDTO();;
	
	
	private int idRelacion;
	
    public RegulacionEspecificaDTO getRegulacionEspecifica() {
		return regulacionEspecifica;
	}


	public void setRegulacionEspecifica(RegulacionEspecificaDTO regulacionEspecifica) {
		this.regulacionEspecifica = regulacionEspecifica;
	}


	public String obtenerViewRedirect(String pageRedirect) {
		String parsedUrl = pageRedirect;
		if(pageRedirect != null && !pageRedirect.equalsIgnoreCase("")
		   && pageRedirect.contains("%2F")) {
			parsedUrl = pageRedirect.replace("%2F", "/");
		} 
		return parsedUrl;
	}	
	
		
	
	public void setIdRelacion(int idRelacion) {
		this.idRelacion = idRelacion;
		this.regulacionEspecifica = cargarRegulacionEspecifica(idRelacion);
	}

	private RegulacionEspecificaDTO cargarRegulacionEspecifica(int idRelacion)
    {
    	log.info("[cargarRegulacionEspecifica] Inicio");
    	RegulacionEspecificaDTO re = new RegulacionEspecificaDTO ();
    	
    	if(idRelacion != 0)
    		re = servicioBasicoDeterminaciones.buscarRegulacionEspecifica(new Integer(idRelacion));
    	
    	return re;
    }
	
		
	public void nuevoRegulacionEspecifica()
	{	
		
		this.regulacionEspecifica = new RegulacionEspecificaDTO();
       
		
	}
		
	
	public void borrarRegulacionEspecifica(ActionEvent event)
	{
		log.info("[borrarRegulacionEspecifica] Se va a borrar Regulacion Especifica: getIdRelacion:"+regulacionEspecifica.getIdRelacion());
		try{
			
				// Es una borrado
				servicioBasicoDeterminaciones.borrarRegulacionespecifica(new Integer(regulacionEspecifica.getIdRelacion()));
				
				// log de informacion
				log.info("[borrarRegulacionEspecifica] Regulacion Especifica Borrado Correctamente: id:"+regulacionEspecifica.getIdRelacion());
				statusMessages.addFromResourceBundleOrDefault(Severity.INFO,"regesp_borrado_ok","Regulacion Especifica Borrado Correctamente");
				
				// Pongo un regimen especifico vacia:
				nuevoRegulacionEspecifica();
				
				String pageRedirect = (String) event.getComponent().getAttributes().get("pageRedirect");
				FacesManager.instance().redirect(pageRedirect);
			
		}
		catch (Exception ex)
		{
			statusMessages.addFromResourceBundleOrDefault(Severity.ERROR,"regesp_modificada_error","ERROR: Se ha producido un error al borrar el Regulacion Especifica: "+ex.getLocalizedMessage() +ex.getMessage());
			log.error("[borrarRegulacionEspecifica] ERROR: Se ha producido un error al borrar el Regulacion Especifica: "+ex.getLocalizedMessage() +ex.getMessage());
		}
		
	}
	
	
	public void guardarRegulacionEspecifica(ActionEvent event)
	{	
		
		try{
			if (regulacionEspecifica.getIdRelacion()!=0){ // estoy editando
				servicioBasicoDeterminaciones.editarRegulacionespecifica(regulacionEspecifica);
				
				log.info("[guardarRegulacionEspecifica] Regulacion Especifica Modificada Correctamente: id:"+regulacionEspecifica.getIdRelacion());
				statusMessages.addFromResourceBundleOrDefault(Severity.INFO,"regesp_modificada_ok","Regulacion Especifica Modificado Correctamente");
			} else {
				
				int idDetTrabajo = gestionDeterminaciones.getIdDeterminacion();
				
				int idRel = servicioBasicoDeterminaciones.crearRegulacionespecifica(regulacionEspecifica, idDetTrabajo);
				if (idRel!=0)
				{
					
					regulacionEspecifica.setIdRelacion(idRel);
					log.info("[guardarRegulacionEspecifica] Regulacion Especifica Creada: id:"+regulacionEspecifica.getIdRelacion()+"idDeterminacion="+idDetTrabajo);

					statusMessages.addFromResourceBundleOrDefault("regesp_creada_ok","Regulacion Especifica Creada Correctamente");
				}				
			}
			
			String pageRedirect = (String) event.getComponent().getAttributes().get("pageRedirect");
			FacesManager.instance().redirect(pageRedirect);
		}
		catch (Exception ex)
		{
			statusMessages.addFromResourceBundleOrDefault(Severity.ERROR, "regesp_guardar_error","ERROR: Se ha producido un error al guardar el regimen: "+ex.getLocalizedMessage() +ex.getMessage());
			log.error("[guardarRegulacionEspecifica] ERROR: Se ha producido un error al guardar el regimen: "+ex.getLocalizedMessage() +ex.getMessage());
			ex.printStackTrace();
		}
		
		
		
	}
	
	public void cargarDatosPadre ()
	{
		String idPadre = JsfUtil.getRequestParameter("idRegulacionEspecificaPadre");
		regulacionEspecifica.setIdRegulacionEspPadre(new Integer(idPadre));
		
		String nombrePadre = JsfUtil.getRequestParameter("nombreRegulacionEspecificaPadre");
		regulacionEspecifica.setNombreRegulacionEspPadre(nombrePadre);
		
		
		
		
	}
	
	@Remove
	public void destroy()
	{
		
	}

}

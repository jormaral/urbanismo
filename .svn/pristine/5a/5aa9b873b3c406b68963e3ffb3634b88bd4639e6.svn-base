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

package com.mitc.redes.editorfip.servicios.sesion;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Startup;
import org.jboss.seam.faces.FacesManager;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.international.StatusMessage.Severity;
import org.jboss.seam.international.StatusMessages;
import org.jboss.seam.log.Log;

import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Tramite;
import com.mitc.redes.editorfip.servicios.basicos.diccionario.ServicioBasicoAmbitos;
import com.mitc.redes.editorfip.servicios.basicos.gestionfip.ServicioBasicoGestionFip;
import com.mitc.redes.editorfip.servicios.genericos.FacesUtils;

@Scope(ScopeType.SESSION)
@Startup
@Stateful
@Name("variablesSesionUsuario")
public class VariablesSesionUsuarioBean implements  Serializable, VariablesSesionUsuario
{
    @Logger private Log log;
    
    @In StatusMessages statusMessages;
    
    @In Map<String,String> messages;
    
   	@In(create = true)
   	ServicioBasicoAmbitos servicioBasicoAmbitos;
   	
   	@In(create = true)
   	ServicioBasicoGestionFip servicioBasicoGestionFip;
    
    @PersistenceContext
	EntityManager em;

    private int idAmbito=0;
    
    private int idTramiteTrabajoActual = 0;
    private int idTipoTramiteTrabajo = -1;
    private Tramite tramiteTrabajoActual;
    
    private Tramite tramiteEncargadoTrabajo;
    private Tramite tramiteVigenteTrabajo;
    private Tramite tramiteBaseTrabajo;
    private Tramite tramitePrerefundidoTrabajo;
    
    
    
    
    private int idTramiteEncargadoTrabajo=0;
    private int idTramiteVigenteTrabajo=0;
    private int idTramiteBaseTrabajo=0;
    private int idTramitePrerefundidoTrabajo=0;
    
   
    public boolean estaTramiteTrabajoEstablecido()
    {
    	boolean resultado = false;
    	
    	if (idTramiteTrabajoActual!=0)
    	{
    		resultado = true;
    	}
    	
    	return resultado;
    }
    
    public boolean estaTramitePrerefundidoEstablecido()
    {
    	boolean resultado = false;
    	
    	if (idTramitePrerefundidoTrabajo!=0)
    	{
    		resultado = true;
    	}
    	
    	return resultado;
    }
    
    
   
    
    public int getIdAmbito() {
		return idAmbito;
	}



	public String getAmbitoNombre()
    {
    	String nombreAmbito="";
    	
    	if (idAmbito!=0)
    	{
    		nombreAmbito = servicioBasicoAmbitos.ambitoString(idAmbito);
    	}
    	
    	return nombreAmbito;
    }
   


	/* (non-Javadoc)
	 * @see com.mitc.redes.editorfip.servicios.VariablesSesionUsuario#getTramiteEncargadoTrabajo()
	 */
	public Tramite getTramiteEncargadoTrabajo() {
			
		return tramiteEncargadoTrabajo;
	}





	/* (non-Javadoc)
	 * @see com.mitc.redes.editorfip.servicios.VariablesSesionUsuario#setTramiteEncargadoTrabajo(com.mitc.redes.editorfip.entidades.rpm.planeamiento.Tramite)
	 */
	public void setTramiteEncargadoTrabajo(Tramite tramiteEncargadoTrabajo) {
		this.tramiteEncargadoTrabajo = tramiteEncargadoTrabajo;
		this.idTramiteEncargadoTrabajo = tramiteEncargadoTrabajo.getIden();
		
		
	}





	/* (non-Javadoc)
	 * @see com.mitc.redes.editorfip.servicios.VariablesSesionUsuario#getTramiteVigenteTrabajo()
	 */
	public Tramite getTramiteVigenteTrabajo() {
		return tramiteVigenteTrabajo;
	}





	/* (non-Javadoc)
	 * @see com.mitc.redes.editorfip.servicios.VariablesSesionUsuario#setTramiteVigenteTrabajo(com.mitc.redes.editorfip.entidades.rpm.planeamiento.Tramite)
	 */
	public void setTramiteVigenteTrabajo(Tramite tramiteVigenteTrabajo) {
		this.tramiteVigenteTrabajo = tramiteVigenteTrabajo;
		this.idTramiteVigenteTrabajo = tramiteVigenteTrabajo.getIden();
		
		
	}





	/* (non-Javadoc)
	 * @see com.mitc.redes.editorfip.servicios.VariablesSesionUsuario#getTramiteBaseTrabajo()
	 */
	public Tramite getTramiteBaseTrabajo() {
		return tramiteBaseTrabajo;
	}





	/* (non-Javadoc)
	 * @see com.mitc.redes.editorfip.servicios.VariablesSesionUsuario#setTramiteBaseTrabajo(com.mitc.redes.editorfip.entidades.rpm.planeamiento.Tramite)
	 */
	public void setTramiteBaseTrabajo(Tramite tramiteBaseTrabajo) {
		
		this.tramiteBaseTrabajo = tramiteBaseTrabajo;
		this.idTramiteBaseTrabajo = tramiteBaseTrabajo.getIden();
		
		
	}



	public Tramite getTramitePrerefundidoTrabajo() {
		return tramitePrerefundidoTrabajo;
	}




	public void setTramitePrerefundidoTrabajo(Tramite tramitePrerefundidoTrabajo) {
		this.tramitePrerefundidoTrabajo = tramitePrerefundidoTrabajo;
	}




	/* (non-Javadoc)
	 * @see com.mitc.redes.editorfip.servicios.VariablesSesionUsuario#getIdTramiteEncargadoTrabajo()
	 */
	public int getIdTramiteEncargadoTrabajo() {
		return idTramiteEncargadoTrabajo;
	}





	/* (non-Javadoc)
	 * @see com.mitc.redes.editorfip.servicios.VariablesSesionUsuario#setIdTramiteEncargadoTrabajo(int)
	 */
	public void setIdTramiteEncargadoTrabajo(int idTramiteEncargadoTrabajo) {
		log.debug("[setIdTramiteEncargadoTrabajo] idTramiteEncargadoTrabajo: "+idTramiteEncargadoTrabajo);
		this.idTramiteEncargadoTrabajo = idTramiteEncargadoTrabajo;		
		this.tramiteEncargadoTrabajo = em.find(Tramite.class, idTramiteEncargadoTrabajo);
		
		// Tambien establezco el iAmbito, idPlanBase, idPlanVigente 
		
		//idAmbito
		idAmbito = tramiteEncargadoTrabajo.getPlan().getIdambito();
		
		//idTramiteBase
		setIdTramiteBaseTrabajo(servicioBasicoGestionFip.obtenerIdTramiteBaseDeIdTramiteEncargado(idTramiteEncargadoTrabajo));
		
		//idTramiteVigente
		setIdTramiteVigenteTrabajo(servicioBasicoGestionFip.obtenerIdTramiteVigenteDeIdTramiteEncargado(idTramiteEncargadoTrabajo));
		
		// Pongo el tramite de prerefundido a cero
		setIdTramitePrerefundidoTrabajo(0);
		
		
		
	}





	/* (non-Javadoc)
	 * @see com.mitc.redes.editorfip.servicios.VariablesSesionUsuario#getIdTramiteVigenteTrabajo()
	 */
	public int getIdTramiteVigenteTrabajo() {
		return idTramiteVigenteTrabajo;
	}





	/* (non-Javadoc)
	 * @see com.mitc.redes.editorfip.servicios.VariablesSesionUsuario#setIdTramiteVigenteTrabajo(int)
	 */
	public void setIdTramiteVigenteTrabajo(int idTramiteVigenteTrabajo) {
		log.debug("[setIdTramiteVigenteTrabajo] idTramiteVigenteTrabajo: "+idTramiteVigenteTrabajo);
		this.idTramiteVigenteTrabajo = idTramiteVigenteTrabajo;
		this.tramiteVigenteTrabajo = em.find(Tramite.class, idTramiteVigenteTrabajo);
		
		
	}





	/* (non-Javadoc)
	 * @see com.mitc.redes.editorfip.servicios.VariablesSesionUsuario#getIdTramiteBaseTrabajo()
	 */
	public int getIdTramiteBaseTrabajo() {
		return idTramiteBaseTrabajo;
	}





	/* (non-Javadoc)
	 * @see com.mitc.redes.editorfip.servicios.VariablesSesionUsuario#setIdTramiteBaseTrabajo(int)
	 */
	public void setIdTramiteBaseTrabajo(int idTramiteBaseTrabajo) {
		log.debug("[setIdTramiteBaseTrabajo] idTramiteBaseTrabajo: "+idTramiteBaseTrabajo);
		this.idTramiteBaseTrabajo = idTramiteBaseTrabajo;
		this.tramiteBaseTrabajo = em.find(Tramite.class, idTramiteBaseTrabajo);
		
		
	}




	public int getIdTramitePrerefundidoTrabajo() {
		return idTramitePrerefundidoTrabajo;
	}




	public void setIdTramitePrerefundidoTrabajo(int idTramitePrerefundidoTrabajo) {
		log.debug("[setIdTramiteBaseTrabajo] idTramitePrerefundidoTrabajo: "+idTramitePrerefundidoTrabajo);
		this.idTramitePrerefundidoTrabajo = idTramitePrerefundidoTrabajo;
		if (idTramitePrerefundidoTrabajo!=0)
			this.tramitePrerefundidoTrabajo = em.find(Tramite.class, idTramitePrerefundidoTrabajo);
		else
			this.tramitePrerefundidoTrabajo = null;
	}




	public int getIdTramiteTrabajoActual() {
		return idTramiteTrabajoActual;
	}
	
	
	
	
	public Tramite getTramiteTrabajoActual() {
		
		tramiteTrabajoActual = em.find(Tramite.class, idTramiteTrabajoActual);
		return tramiteTrabajoActual;
	}



	public int getIdTipoTramiteTrabajo() {
		return idTipoTramiteTrabajo;
	}

	public void setIdTipoTramiteTrabajo(int idTipoTramiteTrabajo) {
		
		log.debug("[setIdTipoTramiteTrabajo] idTipoTramiteTrabajo: "+idTipoTramiteTrabajo);
		
		
			// Si es 1, equivale a Tramite Base
			if (idTipoTramiteTrabajo == 1) {
				idTramiteTrabajoActual = getIdTramiteBaseTrabajo();
			}

			// Si es 2, equivale a Tramite Vigente
			if (idTipoTramiteTrabajo == 2) {
				idTramiteTrabajoActual = getIdTramiteVigenteTrabajo();
			}

			// Si es 3, equivale a Tramite Encargado
			if (idTipoTramiteTrabajo == 3) {
				idTramiteTrabajoActual = getIdTramiteEncargadoTrabajo();
			}
			
			// Si es 4, equivale a Tramite Prerefundido
			if (idTipoTramiteTrabajo == 4) {
				idTramiteTrabajoActual = getIdTramitePrerefundidoTrabajo();
			}
			
						
			this.idTipoTramiteTrabajo = idTipoTramiteTrabajo;
			
			
		
		
		
	}
	
	 public List<SelectItem> tipoPlanesSeleccion() {
			
	    	List<SelectItem> res = new ArrayList<SelectItem>();
	    	
	    	// Tramite Base  -  1
	    	SelectItem i1 = new SelectItem();
			i1.setLabel((String) messages.get("seleccionar_planbase"));
			i1.setValue(1);
			res.add(i1);
	    	
	    	// Tramite Vigente - 2
			SelectItem i2 = new SelectItem();
			i2.setLabel((String) messages.get("seleccionar_planvigente"));
			i2.setValue(2);
			res.add(i2);
	    	
	    	// Tramite Encargado - 3
			SelectItem i3 = new SelectItem();
			i3.setLabel((String) messages.get("seleccionar_planencargado"));
			i3.setValue(3);
			res.add(i3);
			
			return res;
		}
	 
	 
	 public void seleccionarTramite(int idTramiteEncargadoTrabajo, String pagDest) {
		 setIdTramiteEncargadoTrabajo(idTramiteEncargadoTrabajo);
		 
		 FacesMessages.instance().addFromResourceBundle(Severity.INFO, "tramiteencargado_seleccionado_ok", idTramiteEncargadoTrabajo, tramiteEncargadoTrabajo.getNombre());
		 FacesManager.instance().redirect(FacesUtils.decodeSlashURL(pagDest) + ".xhtml");		 
	 }
	
	 public void seleccionarPrerefundido(int idTramitePrerefundido, String pagDest) {
		 setIdTramitePrerefundidoTrabajo(idTramitePrerefundido);
		 
		 FacesMessages.instance().addFromResourceBundle(Severity.INFO, "prerefundido_seleccionado_ok", idTramitePrerefundido, tramitePrerefundidoTrabajo.getNombre());
		 FacesManager.instance().redirect(FacesUtils.decodeSlashURL(pagDest) + ".xhtml");
	 }
	

	/* (non-Javadoc)
	 * @see com.mitc.redes.editorfip.servicios.VariablesSesionUsuario#destroy()
	 */
	/* (non-Javadoc)
	 * @see com.mitc.redes.editorfip.servicios.VariablesSesionUsuario#destroy()
	 */
	@Remove
    public void destroy() {}

}

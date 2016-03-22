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

package com.mitc.redes.editorfip.servicios.informacionfip.busqueda;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remove;
import javax.ejb.Stateless;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.faces.FacesManager;
import org.jboss.seam.log.Log;

import com.mitc.redes.editorfip.entidades.busqueda.BusquedaDeterminacionDTO;
import com.mitc.redes.editorfip.entidades.busqueda.FiltrosDTO;
import com.mitc.redes.editorfip.servicios.basicos.diccionario.ServicioBasicoPlanes;
import com.mitc.redes.editorfip.servicios.basicos.fip.determinaciones.ServicioBasicoDeterminaciones;
import com.mitc.redes.editorfip.servicios.genericos.FacesUtils;
import com.mitc.redes.editorfip.servicios.produccionfip.gestiondiccionariodeterminaciones.GestionArbolDeterminaciones;
import com.mitc.redes.editorfip.servicios.produccionfip.gestiondiccionariodeterminaciones.GestionDeterminaciones;
import com.mitc.redes.editorfip.servicios.sesion.VariablesSesionUsuario;

@Stateless
@Name("servicioBusquedaDeterminacion")
public class ServicioBusquedaDeterminacionBean implements ServicioBusquedaDeterminacion
{
    @Logger private Log log;
    
    @PersistenceContext
	EntityManager em;
    
    FiltrosDTO filtros = new FiltrosDTO();
    
    List<BusquedaDeterminacionDTO> resultado;
    
    @In(create = true, required = false)
	ServicioBasicoDeterminaciones servicioBasicoDeterminaciones;
    
    @In (create = true, required = false)
   	VariablesSesionUsuario variablesSesionUsuario;
    
    @In (create = true, required = false)
	ServicioBasicoPlanes servicioBasicoPlanes;
    
    @In (create = true, required = false)
	GestionArbolDeterminaciones gestionArbolDeterminaciones;
    
    @In (create = true, required = false)
	GestionDeterminaciones gestionDeterminaciones;
    
    private List<SelectItem> listaTiposFiltro = null;
    private List<SelectItem> listaTipoPlan = null;
    
    public void buscar()
    {
    	// El tramite que pasamos el el que haya seleccionado en (encargado, vigente o base)
    	int idTramite = 0;
    	if (filtros.getTipoPlanSeleccionado().equals("Encargado"))
    		idTramite = variablesSesionUsuario.getIdTramiteEncargadoTrabajo();
    	else if (filtros.getTipoPlanSeleccionado().equals("Vigente"))
    		idTramite = variablesSesionUsuario.getIdTramiteVigenteTrabajo();
    	else
    		idTramite = variablesSesionUsuario.getIdTramiteBaseTrabajo();
    	
    	resultado = servicioBasicoDeterminaciones.resultadosBusquedaAvanzadaDeterminacion(filtros, idTramite);
    }
    
    public void verDetalleDeterminacion(int idDet, String tipo) {
    	
    	if (idDet==0)
    	{
    		String idDeterminacionString = FacesUtils.getRequestParameter("idDeterminacion");
            log.info("[verDetalleDeterminacion] idDeterminacionString= " +idDeterminacionString);
            
            String tipoString = FacesUtils.getRequestParameter("tipo");
            log.info("[verDetalleDeterminacion] tipoString= " +tipo);
            
            tipo = tipoString;
            idDet = new Integer(idDeterminacionString);
    	}
    	
    	
    	
    	log.debug("[verDetalleDeterminacion] idDet="+idDet+" tipo="+tipo);
    	
    	if("Base".equalsIgnoreCase(tipo))
    		variablesSesionUsuario.setIdTipoTramiteTrabajo(1);
    	else if ("Vigente".equalsIgnoreCase(tipo))
    		variablesSesionUsuario.setIdTipoTramiteTrabajo(2);
    	else
    		variablesSesionUsuario.setIdTipoTramiteTrabajo(3);
    	
    	
    	gestionArbolDeterminaciones.cargarYExpandirDeterminacion(idDet);
    	
    	
    	
    	if("Base".equalsIgnoreCase(tipo))
    		FacesManager.instance().redirect("/informacionfip/planbase/determinaciones/VerDeterminacionPlanBase.xhtml");
    	else if ("Vigente".equalsIgnoreCase(tipo))
    		FacesManager.instance().redirect("/informacionfip/planvigente/determinaciones/VerDeterminacionPlanVigente.xhtml");
    	else
    		FacesManager.instance().redirect("/produccionfip/gestiondiccionariodeterminaciones/VerDeterminacionPlanEncargado.xhtml");
    }
    
    @Remove
	public void destroy() {
	}

	public FiltrosDTO getFiltros() {
		return filtros;
	}

	public void setFiltros(FiltrosDTO filtros) {
		this.filtros = filtros;
	}

	public List<BusquedaDeterminacionDTO> getResultado() {
		return resultado;
	}

	public void setResultado(List<BusquedaDeterminacionDTO> resultado) {
		this.resultado = resultado;
	}
	
	protected List<SelectItem> cargaLista(List<Object[]> elementos) {

		List<SelectItem> res = new ArrayList<SelectItem>();
		for (Object[] c : elementos) {
			SelectItem i = new SelectItem();
			i.setLabel((String) c[1]);
			i.setValue(c[0]);
			res.add(i);
		}

		//Collections.sort(res, new ComparadorItems());

		return res;
	}

	public List<SelectItem> getListaTiposFiltro() {
		SelectItem or = new SelectItem();
		or.setValue("or");
		or.setLabel("Uno u otro");
		
		SelectItem and = new SelectItem();
		and.setValue("and");
		and.setLabel("Todos a la vez");
		
		listaTiposFiltro = new ArrayList<SelectItem>();
		listaTiposFiltro.add(or);
		listaTiposFiltro.add(and);
		
		return listaTiposFiltro;
	}

	public void setListaTiposFiltro(List<SelectItem> listaTiposFiltro) {
		this.listaTiposFiltro = listaTiposFiltro;
	}
    
	public boolean isRenderizarAnterior() {
		return resultado != null && filtros.getPagina() > 1;
	}
	
	public boolean isRenderizarSiguiente() {
		return resultado != null 
		&& filtros.getPagina() < filtros.getNumPaginas() &&
		filtros.getNumPaginas() > 1;
	}
	
	public void siguientePag() {
		filtros.setPagina(filtros.getPagina() + 1);
		this.buscar();
	}
	
	public void anteriorPag() {
		filtros.setPagina(filtros.getPagina() - 1);
		buscar();
	}
	
	public void ultimo() {
		filtros.setPagina(filtros.getNumPaginas());
		buscar();
	}
	
	public void primero() {
		filtros.setPagina(1);
		buscar();
	}
	
	public List<SelectItem> getListaTipoPlan() {
		
		SelectItem encargado = new SelectItem();
		encargado.setValue("Encargado");
		encargado.setLabel("Encargado");
		
		SelectItem vigente = new SelectItem();
		vigente.setValue("Vigente");
		vigente.setLabel("Vigente");
		
		SelectItem base = new SelectItem();
		base.setValue("Base");
		base.setLabel("Base");
		
		listaTipoPlan = new ArrayList<SelectItem>();
		listaTipoPlan.add(encargado);
		listaTipoPlan.add(vigente);
		listaTipoPlan.add(base);
		
		return listaTipoPlan;
	}

	public void setListaTipoPlan(List<SelectItem> listaTipoPlan) {
		this.listaTipoPlan = listaTipoPlan;
	}
}
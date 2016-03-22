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

package com.mitc.redes.editorfip.servicios.informacionfip.busqueda;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remove;
import javax.ejb.Stateless;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;

import org.jboss.seam.faces.FacesManager;
import org.jboss.seam.log.Log;


import com.mitc.redes.editorfip.entidades.busqueda.BusquedaEntidadDTO;
import com.mitc.redes.editorfip.entidades.busqueda.FiltrosDTO;
import com.mitc.redes.editorfip.servicios.basicos.diccionario.ServicioBasicoPlanes;
import com.mitc.redes.editorfip.servicios.basicos.fip.entidades.ServicioBasicoEntidades;
import com.mitc.redes.editorfip.servicios.genericos.FacesUtils;
import com.mitc.redes.editorfip.servicios.produccionfip.gestionentidades.GestionArbolEntidades;
import com.mitc.redes.editorfip.servicios.sesion.VariablesSesionUsuario;

@Stateless
@Name("servicioBusquedaEntidad")
public class ServicioBusquedaEntidadBean implements ServicioBusquedaEntidad
{
    @Logger private Log log;
    
    @PersistenceContext
	EntityManager em;
    
    FiltrosDTO filtros = new FiltrosDTO();
    
    List<BusquedaEntidadDTO> resultado;
    
    @In (create = true, required = false)
	ServicioBasicoEntidades servicioBasicoEntidades;
    
    @In (create = true, required = false)
   	VariablesSesionUsuario variablesSesionUsuario;
    
    @In (create = true, required = false)
	ServicioBasicoPlanes servicioBasicoPlanes;
    
    @In (create = true, required = false)
    GestionArbolEntidades gestionArbolEntidades;
    
    private List<SelectItem> listaTiposFiltro = null;
    
    private List<SelectItem> listaTipoPlan = null;
    
    private List<SelectItem> listaGeometria;
    
    boolean llamadaDetalle = false;
    
    
    
    public boolean isLlamadaDetalle() {
    	
    	log.info("[llamadaDetalle] llamadaDetalle="+llamadaDetalle);
		return llamadaDetalle;
	}

	public void reiniciar()
    {
    	
    	log.info("[reiniciar] reiniciar");
    	llamadaDetalle = false;
    }
    
    public void focoEnBusqueda (ActionEvent actionEvent)
    {
    	log.info("[reiniciar] focoEnBusqueda");
    	FacesManager.instance().redirect("/informacionfip/busqueda/busquedaEntidad.xhtml");
    }
    
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
    	
    	resultado = servicioBasicoEntidades.resultadosBusquedaAvanzadaTramite(filtros, idTramite);
    	
    	
    	
    	//reiniciar();
    }
    
    public void verDetalleEntidad(int idEntidad, String tipo) {
    	
    	llamadaDetalle = true;
    	
    	if (idEntidad==0)
    	{
    		String idEntidadString = FacesUtils.getRequestParameter("idEntidad");
            log.info("[verDetalleEntidad] idEntidadString= " +idEntidadString);
            
            String tipoString = FacesUtils.getRequestParameter("tipo");
            log.info("[verDetalleEntidad] tipoString= " +tipo);
            
            tipo = tipoString;
            idEntidad = new Integer(idEntidadString);
    	}

    	
        
        log.debug("[verDetalleEntidad] idEntidad="+idEntidad+" tipo="+tipo);
    	
    	if("Base".equalsIgnoreCase(tipo))
    		variablesSesionUsuario.setIdTipoTramiteTrabajo(1);
    	else if ("Vigente".equalsIgnoreCase(tipo))
    		variablesSesionUsuario.setIdTipoTramiteTrabajo(2);
    	else
    		variablesSesionUsuario.setIdTipoTramiteTrabajo(3);
    	
    	
    	gestionArbolEntidades.cargarYExpandirEntidad(idEntidad);
    	
    	
    	
    	if("Base".equalsIgnoreCase(tipo))
    	{
    		FacesManager.instance().redirect("/informacionfip/planbase/entidades/VerEntidadPlanBase.xhtml");
    	}
    		
    	else if ("Vigente".equalsIgnoreCase(tipo))
    		FacesManager.instance().redirect("/informacionfip/planvigente/entidades/VerEntidadPlanVigente.xhtml");
    	else
    		FacesManager.instance().redirect("/produccionfip/gestionentidades/VerEntidadPlanEncargado.xhtml");
    }
    
    

	public FiltrosDTO getFiltros() {
		return filtros;
	}

	public void setFiltros(FiltrosDTO filtros) {
		this.filtros = filtros;
	}

	public List<BusquedaEntidadDTO> getResultado() {
		return resultado;
	}

	public void setResultado(List<BusquedaEntidadDTO> resultado) {
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

	public List<SelectItem> getListaGeometria() {
		
		SelectItem todos = new SelectItem();
		todos.setValue("0");
		todos.setLabel("Todos");
		
		SelectItem tiene = new SelectItem();
		tiene.setValue("1");
		tiene.setLabel("Tiene geometría");
		
		SelectItem noTiene = new SelectItem();
		noTiene.setValue("2");
		noTiene.setLabel("No tiene geometría");
		
		listaGeometria = new ArrayList<SelectItem>();
		listaGeometria.add(todos);
		listaGeometria.add(tiene);
		listaGeometria.add(noTiene);
		
		return listaGeometria;
	}

	public void setListaGeometria(List<SelectItem> listaGeometria) {
		this.listaGeometria = listaGeometria;
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
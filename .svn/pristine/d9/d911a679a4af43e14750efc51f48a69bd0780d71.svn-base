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
import java.util.Map;
import java.util.TreeMap;

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

import com.mitc.redes.editorfip.entidades.busqueda.BusquedaOperacionDTO;
import com.mitc.redes.editorfip.entidades.busqueda.FiltrosDTO;
import com.mitc.redes.editorfip.servicios.basicos.diccionario.ServicioBasicoPlanes;
import com.mitc.redes.editorfip.servicios.basicos.fip.operaciones.ServicioBasicoOperaciones;
import com.mitc.redes.editorfip.servicios.genericos.FacesUtils;
import com.mitc.redes.editorfip.servicios.sesion.VariablesSesionUsuario;

@Stateless
@Name("servicioBusquedaOperacion")
public class ServicioBusquedaOperacionBean implements ServicioBusquedaOperacion
{
    @Logger private Log log;
    
    @PersistenceContext
	EntityManager em;
    
    FiltrosDTO filtros = new FiltrosDTO();
    
    List<BusquedaOperacionDTO> resultado;
    
    @In (create = true, required = false)
	ServicioBasicoOperaciones servicioBasicoOperaciones;
    
    @In (create = true, required = false)
   	VariablesSesionUsuario variablesSesionUsuario;
    
    @In (create = true, required = false)
	ServicioBasicoPlanes servicioBasicoPlanes;
    
    private List<SelectItem> listaTiposFiltro = null;
    private List<SelectItem> listaTipoPlan = null;
    
    private List<SelectItem> listaClaseOperacion;
    
    private List<SelectItem> listaTipoOperacion;
    
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
    	
    	resultado = servicioBasicoOperaciones.resultadosBusquedaAvanzadaOperacion(filtros, idTramite);
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

	public List<BusquedaOperacionDTO> getResultado() {
		return resultado;
	}

	public void setResultado(List<BusquedaOperacionDTO> resultado) {
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
	
	public List<SelectItem> tipoOperacionEntidad(){
		
		List<Object[]> elementos = servicioBasicoOperaciones.tipoOperacionEntidad();
		
    	List<SelectItem> res = new ArrayList<SelectItem>();
    	
    	SelectItem todos = new SelectItem();
    	todos.setValue(0);
    	todos.setLabel("Todos");
		
    	res.add(todos);
		for (Object[] c : elementos) {
			SelectItem i = new SelectItem();
			i.setLabel((String) c[1]);
			i.setValue(Integer.parseInt(c[0].toString()));
			res.add(i);
		}
		
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

	public List<SelectItem> getListaClaseOperacion() {
		
		SelectItem or = new SelectItem();
		or.setValue("Operacionentidad");
		or.setLabel("Entidades");
		
		SelectItem and = new SelectItem();
		and.setValue("Operaciondeterminacion");
		and.setLabel("Determinaciones");
		
		listaClaseOperacion = new ArrayList<SelectItem>();
		listaClaseOperacion.add(or);
		listaClaseOperacion.add(and);
		
		return listaClaseOperacion;
	}
	
	public void verDetalleOperacion(int idOperacion, String clase) {
		
		if (idOperacion==0)
    	{
    		String idOperacionString = FacesUtils.getRequestParameter("idOp");
            log.info("[verDetalleOperacion] idOperacionString= " +idOperacionString);
            
            String claseString = FacesUtils.getRequestParameter("clase");
            log.info("[verDetalleOperacion] claseString= " +claseString);
            
            clase = claseString;
            idOperacion = new Integer(idOperacionString);
    	}

    	
        
        log.debug("[verDetalleOperacion] idOperacion="+idOperacion+" clase="+clase);
		
		Map<String,Object> parameters = new TreeMap<String, Object> ();
		parameters.put("idOperacion", idOperacion);
		
		if("Entidad".equalsIgnoreCase(clase)) {
			FacesManager.instance().redirect("/produccionfip/gestionoperaciones/CrearOperacionEntidad.xhtml", parameters, false);
		} else if("Determinacion".equalsIgnoreCase(clase)) {
			FacesManager.instance().redirect("/produccionfip/gestionoperaciones/CrearOperacionDeterminacion.xhtml", parameters, false);
		}
	}

	public void setListaClaseOperacion(List<SelectItem> listaClaseOperacion) {
		this.listaClaseOperacion = listaClaseOperacion;
	}

	public List<SelectItem> getListaTipoOperacion() {
		listaTipoOperacion = tipoOperacionEntidad();
		return listaTipoOperacion;
	}

	public void setListaTipoOperacion(List<SelectItem> listaTipoOperacion) {
		this.listaTipoOperacion = listaTipoOperacion;
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
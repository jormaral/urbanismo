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
import org.jboss.seam.log.Log;

import com.mitc.redes.editorfip.entidades.busqueda.BusquedaDocumentoDTO;
import com.mitc.redes.editorfip.entidades.busqueda.FiltrosDTO;
import com.mitc.redes.editorfip.servicios.basicos.diccionario.ServicioBasicoPlanes;
import com.mitc.redes.editorfip.servicios.basicos.fip.documentos.ServicioBasicoDocumentos;
import com.mitc.redes.editorfip.servicios.sesion.VariablesSesionUsuario;

@Stateless
@Name("servicioBusquedaDocumento")
public class ServicioBusquedaDocumentoBean implements ServicioBusquedaDocumento
{
    @Logger private Log log;
    
    @PersistenceContext
	EntityManager em;
    
    FiltrosDTO filtros = new FiltrosDTO();
    
    List<BusquedaDocumentoDTO> resultado;
    
    @In (create = true, required = false)
	ServicioBasicoDocumentos servicioBasicoDocumentos;
    
    @In (create = true, required = false)
   	VariablesSesionUsuario variablesSesionUsuario;
    
    @In (create = true, required = false)
	ServicioBasicoPlanes servicioBasicoPlanes;
    
    private List<SelectItem> listaTiposFiltro = null;
    private List<SelectItem> listaTipoPlan = null;
    
    private List<SelectItem> listaTipoDocumento;
    
    private List<SelectItem> listaGrupoDocumento;
    
    private List<SelectItem> listaAsociadoEntidad;
    private List<SelectItem> listaAsociadoDeterminacion;
    
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
    	
    	log.info("[buscar] idTramite="+idTramite);
    	resultado = servicioBasicoDocumentos.resultadosBusquedaAvanzadaDocumento(filtros, idTramite);
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

	public List<BusquedaDocumentoDTO> getResultado() {
		return resultado;
	}

	public void setResultado(List<BusquedaDocumentoDTO> resultado) {
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

	public List<SelectItem> getListaAsociadoEntidad() {
		
		SelectItem todos = new SelectItem();
		todos.setValue(0);
		todos.setLabel("Todos");
		
		SelectItem asociado = new SelectItem();
		asociado.setValue(1);
		asociado.setLabel("Asociado");
		
		SelectItem noAsociado = new SelectItem();
		noAsociado.setValue(2);
		noAsociado.setLabel("No Asociado");
		
		listaAsociadoEntidad = new ArrayList<SelectItem>();
		listaAsociadoEntidad.add(todos);
		listaAsociadoEntidad.add(asociado);
		listaAsociadoEntidad.add(noAsociado);
		
		return listaAsociadoEntidad;
	}

	public void setListaAsociadoEntidad(List<SelectItem> listaAsociadoEntidad) {
		this.listaAsociadoEntidad = listaAsociadoEntidad;
	}

	public List<SelectItem> getListaAsociadoDeterminacion() {
		
		SelectItem todos = new SelectItem();
		todos.setValue(0);
		todos.setLabel("Todos");
		
		SelectItem asociado = new SelectItem();
		asociado.setValue(1);
		asociado.setLabel("Asociado");
		
		SelectItem noAsociado = new SelectItem();
		noAsociado.setValue(2);
		noAsociado.setLabel("No Asociado");
		
		listaAsociadoDeterminacion = new ArrayList<SelectItem>();
		listaAsociadoDeterminacion.add(todos);
		listaAsociadoDeterminacion.add(asociado);
		listaAsociadoDeterminacion.add(noAsociado);
		
		return listaAsociadoDeterminacion;
	}

	public void setListaAsociadoDeterminacion(
			List<SelectItem> listaAsociadoDeterminacion) {
		this.listaAsociadoDeterminacion = listaAsociadoDeterminacion;
	}

	public void setListaTiposFiltro(List<SelectItem> listaTiposFiltro) {
		this.listaTiposFiltro = listaTiposFiltro;
	}

	public List<SelectItem> getListaTipoDocumento() {
		listaTipoDocumento = servicioBasicoDocumentos.tiposDocumentoList();
		return listaTipoDocumento;
	}

	public void setListaTipoDocumento(List<SelectItem> listaTipoDocumento) {
		this.listaTipoDocumento = listaTipoDocumento;
	}

	public List<SelectItem> getListaGrupoDocumento() {
		listaGrupoDocumento = servicioBasicoDocumentos.grupoDocumentoList();
		return listaGrupoDocumento;
	}

	public void setListaGrupoDocumento(List<SelectItem> listaGrupoDocumento) {
		this.listaGrupoDocumento = listaGrupoDocumento;
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
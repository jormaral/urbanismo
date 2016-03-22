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

package com.mitc.redes.editorfip.servicios.gestionfip.validaciones;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.faces.event.ValueChangeEvent;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;

import com.mitc.redes.editorfip.entidades.rpm.validaciones.ElementoValidado;

@Scope(ScopeType.SESSION)
@Stateful
@Name("listaElementosValidados")
public class ListaElementosValidadosBean implements ListaElementosValidados {

	@Logger private Log log;
	
	private List<ElementoValidado> listaElementos;
	
	private int idProceso;
	
	private String filtroVal;
	
	@In (create = true)
	ServicioGestionValidaciones servicioGestionValidaciones;
	
	

	public void setListaElementos(List<ElementoValidado> listaProcesos) {
		this.listaElementos = listaProcesos;
	}

	@SuppressWarnings("unchecked")
	public List<ElementoValidado> getListaElementos() {
		
		if(listaElementos == null)
			this.actualizarLista();
		
		return listaElementos;
	}
	
	public void filtrarLista(ValueChangeEvent vce) {
		
		this.filtroVal = (String) vce.getNewValue();
		this.actualizarLista();
	}
	
	private void actualizarLista() {
		try {
			
			listaElementos = (List<ElementoValidado>) servicioGestionValidaciones.obtenerValidacionesProceso(idProceso, filtroVal);
		} catch (Exception e) {
			log.error(e.getStackTrace(), null);
			listaElementos = new ArrayList<ElementoValidado>();
		}
	}	

	public void setIdProceso(int idProceso) {
		this.idProceso = idProceso;
	}

	public int getIdProceso() {
		return idProceso;
	}

	public String getFiltroVal() {
		return filtroVal;
	}

	public void setFiltroVal(String filtroVal) {
		this.filtroVal = filtroVal;
	}
	
	@Remove
	public void destroy() {
	}
}

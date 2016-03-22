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

package com.mitc.redes.editorfip.servicios.gestionfip.gestionplaneamientoencargado;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.faces.FacesMessages;

import com.icesoft.faces.component.selectinputtext.SelectInputText;
import com.mitc.redes.editorfip.entidades.interfazgrafico.BusquedaGenericaDTO;
import com.mitc.redes.editorfip.entidades.rpm.gestionfip.PlanesEncargados;
import com.mitc.redes.editorfip.servicios.basicos.comunes.plantillas.ListaGenericaBean;
import com.mitc.redes.editorfip.servicios.basicos.fip.planes.ServicioCRUDPLanesEncargados;
import com.mitc.redes.editorfip.servicios.sesion.VariablesSesionUsuario;

@Stateful
@Name("servicioPlanEncargadoLista")
public class GestionPlaneamientoEncargadoListBean extends ListaGenericaBean implements GestionPlaneamientoEncargadoList {
	
	
	@In(create=true)
    FacesMessages facesMessages;
	
	private String idPlanSeleccionado;
	private HashMap<Long,Boolean> seleccion;
	
	
	@In (create = true, required = false)
   	VariablesSesionUsuario variablesSesionUsuario;
        
    @In(create = true, required = false)
	ServicioCRUDPLanesEncargados servicioCRUDPlanesEncargados;
 
    private List<SelectItem> resultadosBusqueda = new ArrayList<SelectItem>();
    private long planEncargadoSel;
	
	public List<SelectItem> getResultadosBusqueda() {
		return resultadosBusqueda;
	}


	public void setResultadosBusqueda(List<SelectItem> resultadosBusqueda) {
		this.resultadosBusqueda = resultadosBusqueda;
	}


	public long getPlanEncargadoSel() {
		return planEncargadoSel;
	}


	public void setPlanEncargadoSel(long planEncargadoSel) {
		this.planEncargadoSel = planEncargadoSel;
	}


	@SuppressWarnings("unchecked")
	public List<PlanesEncargados> getListaPlanesEncargados() {
		
		/** Codigo para el seleccionado de usuarios por CheckBox **/
		super.setOrderColumn("ambito_iden, fechainicio");
		super.setOrderDirection("DESC");
		List<PlanesEncargados> lista = (List<PlanesEncargados>) super.listaInicial;
		
		seleccion = new HashMap<Long,Boolean>();
		if(lista!=null) {
			for(PlanesEncargados temp : lista) {
				seleccion.put(temp.getId(), Boolean.FALSE);
			}
		}
		return (List<PlanesEncargados>) super.listaInicial;
	}
	
	
	/** Verifica que solo se ha seleccionado un usuario en 
	 * la lista de usuarios y redirige a la pagina de detalles
	 */
	public String verDetalle() {
		
		String resultado = "";
		
		
		return resultado;
	}
	
	public String verDetalleAL(ActionEvent ae) {
		
		String resultado = "";
		// List<Usuario> listaUsr = (List<Usuario>) super.getObjetosSeleccionados();
		
		
		
		return resultado;
	}
	
	@Remove
	public void remove() {
		try {
			this.finalize();
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	
	public String getIdPlanSeleccionado() {
		return idPlanSeleccionado;
	}


	public void setIdPlanSeleccionado(String idPlanSeleccionado) {
		this.idPlanSeleccionado = idPlanSeleccionado;
	}


	@Override
	public void darDebaja() {
		List<PlanesEncargados> lista = (List<PlanesEncargados>)super.listaInicial;
		// List<Usuario> listaUsr = (List<Usuario>) super.getObjetosSeleccionados();
		Integer numSeleccionados = 0;
		for(Iterator<Boolean> it = seleccion.values().iterator(); it.hasNext();) {
			if(it.next()) {
				numSeleccionados++;
			}
		}  
		
		if(numSeleccionados == 0) {
			facesMessages.addFromResourceBundle("elemento_almenosuno", null);
		} else {
			for(PlanesEncargados temp : lista) {			
				try {
					Boolean seleccionado = seleccion.get(temp.getId());
					if(seleccionado) {
						PlanesEncargados planTemp = entityManager.find(PlanesEncargados.class,temp.getId());
						//planTemp.setDadoBaja(true);
						entityManager.remove(planTemp);	
						entityManager.flush();
						facesMessages.addFromResourceBundle("planencargado_dadodebajaok", null);
					}				
					
				} catch (Exception e) {
					facesMessages.addFromResourceBundle("planencargado_errordardebaja", null);
					e.printStackTrace();
				} 
			}
			getListaPlanesEncargados();
		}
		
	}


	public HashMap getSeleccion() {
		return seleccion;
	}


	public void setSeleccion(HashMap seleccion) {
		this.seleccion = seleccion;
	}
	
	public void actualizarListaPlanesEncargados(ValueChangeEvent event) {
		
		String filtro = ((SelectInputText) event.getComponent()).getValue().toString();
		List<BusquedaGenericaDTO> resultados;
		
		SelectInputText autoComplete = (SelectInputText) event.getComponent();

		// si no se selecciona ponemos el valor a -1 para sacar un listado de elementos.
		
		if (autoComplete.getSelectedItem() != null)
		  planEncargadoSel = ((BusquedaGenericaDTO) autoComplete.getSelectedItem().getValue()).getIden();
		else
			planEncargadoSel=-1;
		
		if(!filtro.equalsIgnoreCase("")) {
			resultados = servicioCRUDPlanesEncargados.autocompleteFiltros(filtro);
			
			resultadosBusqueda.clear();
			
			for(BusquedaGenericaDTO resul : resultados)
				resultadosBusqueda.add(new SelectItem(resul, resul.getNombre()));
		}
			
		
	}
	
}

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

package com.mitc.redes.editorfip.servicios.produccionfip.gestionentidades;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.ejb.Stateless;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.faces.FacesManager;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.international.StatusMessage.Severity;
import org.jboss.seam.log.Log;

import com.icesoft.faces.component.ext.RowSelectorEvent;
import com.mitc.redes.editorfip.entidades.interfazgrafico.CondicionUrbanisticaSimplificadaDTO;
import com.mitc.redes.editorfip.entidades.interfazgrafico.DeterminacionDTO;
import com.mitc.redes.editorfip.servicios.basicos.fip.entidades.ServicioBasicoCondicionesUrbanisticas;
import com.mitc.redes.editorfip.servicios.genericos.SortableList;

@Stateless
@Name("listaCUSimplificada")
public class ListaCUSimplificadaBean extends SortableList implements
		ListaCUSimplificada {

	@Logger
	private Log log;
	
	@In (create = true, required = false)
	FacesMessages facesMessages;

	@In(create = true, required = false)
	ServicioBasicoCondicionesUrbanisticas servicioBasicoCondicionesUrbanisticas;

	@In(create = true, required = false)
	GestionEntidades gestionEntidades;
	
	@In(create = true, required = false)
	GestionCondicionesUrbanisticas gestionCondicionesUrbanisticas;
	
	@In(create = true, required = false)
	GestionArbolCopiarCU gestionArbolCopiarCU;

	private List<CondicionUrbanisticaSimplificadaDTO> cuSimplificadaList = new ArrayList<CondicionUrbanisticaSimplificadaDTO>();
	private static final String nombreColumnName = "nombre";
	private Integer idEntidadOLD=0;
	
	private List<CondicionUrbanisticaSimplificadaDTO> listaCUSeleccionadasParaCopiar = new ArrayList<CondicionUrbanisticaSimplificadaDTO>();
	
	private boolean seleccionadasTodas = false;
	private boolean entidadAplicadaGrupoAplicacion = false;
	

	public ListaCUSimplificadaBean() {
		super(nombreColumnName);
		
	}

	@Override
	public boolean isDefaultAscending(String nombreColumnName) {
		
		return true;
	}

	@Override
	public void sort() {
		
		log.debug("[sort] Inicio");
		
		final Collator comparador = Collator.getInstance();
		comparador.setStrength(Collator.PRIMARY);
		
		

        Comparator<Object> comparator = new Comparator<Object>() {

            public int compare(Object o1, Object o2) {
            	CondicionUrbanisticaSimplificadaDTO d1 = (CondicionUrbanisticaSimplificadaDTO) o1;
            	CondicionUrbanisticaSimplificadaDTO d2 = (CondicionUrbanisticaSimplificadaDTO) o2;
                if (sortColumnName == null) {
                    return 0;
                }
                if (sortColumnName.equals(nombreColumnName)) {
                	return ascending ? comparador.compare(d1.getNombreDeterminacion(), d2.getNombreDeterminacion()) : comparador.compare(d2.getNombreDeterminacion(), d1.getNombreDeterminacion());
                } else {
                    return 0;
                }
            }
        };
        Collections.sort(cuSimplificadaList, comparator);
        log.debug("[sort] Fin");

	}

	

	@Override
	public String getNombreColumnName() {		
		return nombreColumnName;
	}

	public void refrescarLista ()
	{
		log.debug("[refrescarLista] refrescar Lista para entTrabajo="+gestionEntidades.getIdEntidad());
		cuSimplificadaList = servicioBasicoCondicionesUrbanisticas.listadoTodasCUSimplificadaDeEntidad(gestionEntidades.getIdEntidad());
		this.seleccionadasTodas = false;
		listaCUSeleccionadasParaCopiar = new ArrayList<CondicionUrbanisticaSimplificadaDTO>();
		entidadAplicadaGrupoAplicacion = servicioBasicoCondicionesUrbanisticas.tieneAsignadaEntidadGrupoAplicacion(gestionEntidades.getIdEntidad());
	}
	
	public boolean isEntidadAplicadaGrupoAplicacion() {

		

		int entTrabajo = gestionEntidades.getIdEntidad();
		int entTrabajoOLD = gestionEntidades.getIdEntidadOLD();
		
		if (entTrabajoOLD!=entTrabajo)
		{
			entidadAplicadaGrupoAplicacion = servicioBasicoCondicionesUrbanisticas.tieneAsignadaEntidadGrupoAplicacion(gestionEntidades.getIdEntidad());
		}
		
		return entidadAplicadaGrupoAplicacion;
	}
	
	public List<CondicionUrbanisticaSimplificadaDTO> getCuSimplificadaList() {
		int entTrabajo = gestionEntidades.getIdEntidad();
		int entTrabajoOLD = gestionEntidades.getIdEntidadOLD();
		if (entTrabajo!=0)
		{
			
			log.debug("[getCuSimplificadaList] entTrabajo / entTrabajoOLD: "+entTrabajo+"/"+entTrabajoOLD);
			
			if (entTrabajoOLD!=entTrabajo)
			{
				cuSimplificadaList = servicioBasicoCondicionesUrbanisticas.listadoTodasCUSimplificadaDeEntidad(entTrabajo);
				//idEntidadOLD = entTrabajo;
			}
			
			/*
			// La voy a pedir siempre, para evitar problemas de refresco
			if (listaCUSeleccionadasParaCopiar.size() == 0)
			cuSimplificadaList = servicioBasicoCondicionesUrbanisticas.listadoTodasCUSimplificadaDeEntidad(entTrabajo);
			*/
			
			
			
		}
		else
		{
			log.debug("[getCuSimplificadaList] entTrabajo=0");
			// No se ha seleccionado todavia ninguna entTrabajo
			cuSimplificadaList = new ArrayList<CondicionUrbanisticaSimplificadaDTO>();
		}
		
		
		return cuSimplificadaList;
	}
	
	public void llamarPopUpCU() {
		
		// TODO: Implementar la llamada al PopUp de CU
		// ....
		
		FacesManager.instance().redirect("/produccionfip/gestionentidades/EntidadAnadirCU.xhtml");
	}
	
	public void llamarPopUpGA() {
		
		// TODO: Implementar la llamada al PopUp de GA
		// ....
		
		FacesManager.instance().redirect("/produccionfip/gestionentidades/EntidadAnadirGA.xhtml");
	}
	
	public void seleccionarTodas(boolean sel) {
		
		log.debug("[seleccionarTodas] sel: "+sel);
		this.seleccionadasTodas = sel;
		
		if(cuSimplificadaList != null && !cuSimplificadaList.isEmpty()) {			
			for(CondicionUrbanisticaSimplificadaDTO condicion : cuSimplificadaList) {
				condicion.setSeleccionada(seleccionadasTodas);
				log.debug("[seleccionarTodas] condicion: "+condicion.getIdRegimen());
			}
			
		}
	}

	public void setSeleccionadasTodas(boolean seleccionadasTodas) {
		this.seleccionadasTodas = seleccionadasTodas;
	}

	public boolean isSeleccionadasTodas() {
		return seleccionadasTodas;
	}
	
	public boolean isEsAlgunaSeleccionada() {
		boolean resultado = false;
		
		for(CondicionUrbanisticaSimplificadaDTO condicion : cuSimplificadaList) {
			log.debug("[isEsAlgunaSeleccionada] condicion: "+condicion.getIdRegimen()+" sel: "+condicion.isSeleccionada());
			if(condicion.isSeleccionada()) {
				resultado = true;
				break;
			}
		}
		
		return resultado;
	}
	
	public void seleccionarEntidadesCopiar() {
		
		log.debug("[seleccionarEntidadesCopiar] INICIO");
		
		if(isEsAlgunaSeleccionada()) {
			List<CondicionUrbanisticaSimplificadaDTO> condicionesSeleccionadas = new ArrayList<CondicionUrbanisticaSimplificadaDTO>();
			
			for(CondicionUrbanisticaSimplificadaDTO condicion : cuSimplificadaList) {
				if(condicion.isSeleccionada()) {
					condicionesSeleccionadas.add(condicion);
				}
			}
			
			gestionArbolCopiarCU.setModel(null);
			gestionArbolCopiarCU.setCondicionesSeleccionadas(condicionesSeleccionadas);
			
			FacesManager.instance().redirect("/produccionfip/gestionentidades/EntidadCopiarCU.xhtml");
		} else {
			facesMessages.addFromResourceBundle(Severity.ERROR, "entidad_copiarcu_nosel", null);
		}		
		
	}
	
	public void rowSelectionListener (RowSelectorEvent event)
	{
		int idCU = 0;
		
		//listaCUSeleccionadasParaCopiar
		
		log.info("[rowSelectionListener] Inicio");
		if(cuSimplificadaList != null) {
			for(CondicionUrbanisticaSimplificadaDTO cu : cuSimplificadaList) {
				if(cu.isSeleccionada()) {
					
					listaCUSeleccionadasParaCopiar.add(cu);
				}					
			}
		}
		log.info("[rowSelectionListener] listaCUSeleccionadasParaCopiar num elementos= "+listaCUSeleccionadasParaCopiar.size());
	}
	
	
	public void seleccionarEntidadesCopiarListener(ActionEvent ae) {
		
		log.debug("[seleccionarEntidadesCopiar] INICIO");
		
		if(isEsAlgunaSeleccionada()) {
			List<CondicionUrbanisticaSimplificadaDTO> condicionesSeleccionadas = new ArrayList<CondicionUrbanisticaSimplificadaDTO>();
			
			for(CondicionUrbanisticaSimplificadaDTO condicion : cuSimplificadaList) {
				if(condicion.isSeleccionada()) {
					condicionesSeleccionadas.add(condicion);
				}
			}
			
			gestionArbolCopiarCU.setModel(null);
			gestionArbolCopiarCU.setCondicionesSeleccionadas(condicionesSeleccionadas);
			
			FacesManager.instance().redirect("/produccionfip/gestionentidades/EntidadCopiarCU.xhtml");
		} else {
			facesMessages.addFromResourceBundle(Severity.ERROR, "entidad_copiarcu_nosel", null);
		}		
		
	}

}

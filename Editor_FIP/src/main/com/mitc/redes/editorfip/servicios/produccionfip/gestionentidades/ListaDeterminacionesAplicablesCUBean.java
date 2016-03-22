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

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.faces.FacesManager;
import org.jboss.seam.log.Log;

import com.icesoft.faces.component.ext.RowSelectorEvent;
import com.mitc.redes.editorfip.entidades.interfazgrafico.DeterminacionDTO;
import com.mitc.redes.editorfip.entidades.interfazgrafico.GrupoAplicacionTramiteDTO;
import com.mitc.redes.editorfip.servicios.basicos.fip.entidades.ServicioBasicoCondicionesUrbanisticas;
import com.mitc.redes.editorfip.servicios.genericos.SortableList;
import com.mitc.redes.editorfip.servicios.sesion.VariablesSesionUsuario;

@Stateless
@Name("listaDeterminacionesAplicablesCU")
public class ListaDeterminacionesAplicablesCUBean extends SortableList implements
		ListaDeterminacionesAplicablesCU {

	@Logger
	private Log log;

	@In(create = true, required = false)
	ServicioBasicoCondicionesUrbanisticas servicioBasicoCondicionesUrbanisticas;

	@In(create = true, required = false)
	GestionEntidades gestionEntidades;
	
	@In(create = true, required = false)
	GestionCondicionesUrbanisticas gestionCondicionesUrbanisticas;
	
	@In (create = true, required = false)
    VariablesSesionUsuario variablesSesionUsuario;

	private List<DeterminacionDTO> determinacionesCUList = new ArrayList<DeterminacionDTO>();
	private static final String nombreColumnName = "nombre";
	private static final String ordenColumnName = "orden";
	private Integer idEntidadOLD=0;
	
	private Integer idTipoTramiteTrabajoOLD=0;
	private int idTipoTramiteTrabajo = 3;
	
	DeterminacionDTO cuSeleccionada = new DeterminacionDTO();

	public ListaDeterminacionesAplicablesCUBean() {
		super(nombreColumnName);
		idEntidadOLD=0;
		idTipoTramiteTrabajoOLD=0;
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
            	DeterminacionDTO d1 = (DeterminacionDTO) o1;
            	DeterminacionDTO d2 = (DeterminacionDTO) o2;
                if (sortColumnName == null) {
                    return 0;
                }
                if (sortColumnName.equals(nombreColumnName)) {
                	return ascending ? comparador.compare(d1.getNombreDeterminacion(), d2.getNombreDeterminacion()) : comparador.compare(d2.getNombreDeterminacion(), d1.getNombreDeterminacion());
                } else {
                	if (sortColumnName.equals(ordenColumnName)) {
                    	return ascending ? comparador.compare(d1.getOrdenArbol(), d2.getOrdenArbol()) : comparador.compare(d2.getOrdenArbol(), d1.getOrdenArbol());
                    } else {
                        return 0;
                    }
                }
            }
        };
        Collections.sort(determinacionesCUList, comparator);
        log.debug("[sort] Fin");

	}
	
	
	private void ordenarNombre() {
		
		log.debug("[ordenarNombre] Inicio");
		
		final Collator comparador = Collator.getInstance();
		comparador.setStrength(Collator.PRIMARY);
		
		
		Comparator<Object> comparator = new Comparator<Object>() {

            public int compare(Object o1, Object o2) {
            	DeterminacionDTO d1 = (DeterminacionDTO) o1;
            	DeterminacionDTO d2 = (DeterminacionDTO) o2;
            	return ascending ? comparador.compare(d1.getNombreDeterminacion(), d2.getNombreDeterminacion()) : comparador.compare(d2.getNombreDeterminacion(), d1.getNombreDeterminacion());

            }
        };
        Collections.sort(determinacionesCUList, comparator);
        log.debug("[ordenarNombre] Fin");

	}

	

	@Override
	public String getNombreColumnName() {		
		return nombreColumnName;
	}

	public String getOrdenColumnName() {
		return ordenColumnName;
	}

	
	
	

	public List<DeterminacionDTO> getDeterminacionesCUList() {
		int entTrabajo = gestionEntidades.getIdEntidad();
		if (entTrabajo!=0)
		{
			if (entTrabajo!=idEntidadOLD || idTipoTramiteTrabajo!=idTipoTramiteTrabajoOLD)
			{
				// FGA: Lo pido siempre
				int idTramiteSeleccionado = 0;
				if (idTipoTramiteTrabajo==2)
					idTramiteSeleccionado = variablesSesionUsuario.getIdTramiteVigenteTrabajo();
				else if (idTipoTramiteTrabajo==3)
					idTramiteSeleccionado = variablesSesionUsuario.getIdTramiteEncargadoTrabajo();
				else
					idTramiteSeleccionado = variablesSesionUsuario.getIdTramiteBaseTrabajo();
				
				determinacionesCUList = servicioBasicoCondicionesUrbanisticas.obtenerDeterminacionesAplicablesComoCUPorTramite(entTrabajo, idTramiteSeleccionado);
				
				// Para ordenarlo por nombre
				
				ordenarNombre();
				
				idEntidadOLD = entTrabajo;
				idTipoTramiteTrabajoOLD = idTipoTramiteTrabajo;
			}
			
			
			/*
			//log.debug("[getDeterminacionesCUList] entTrabajo / idEntidadOLD: "+entTrabajo+"/"+idEntidadOLD);
			//System.out.println(idTipoTramiteTrabajo + "--" + idTipoTramiteTrabajoOLD);
			if (entTrabajo!=idEntidadOLD || idTipoTramiteTrabajo!=idTipoTramiteTrabajoOLD)
			{
				log.debug("[getDeterminacionesCUList] entTrabajo!=idEntidadOLD: Pido los valores de cuSimplificadaList"+entTrabajo+"/"+idEntidadOLD);
				
				int idTramiteSeleccionado = 0;
				if (idTipoTramiteTrabajo==2)
					idTramiteSeleccionado = variablesSesionUsuario.getIdTramiteVigenteTrabajo();
				else if (idTipoTramiteTrabajo==3)
					idTramiteSeleccionado = variablesSesionUsuario.getIdTramiteEncargadoTrabajo();
				else
					idTramiteSeleccionado = variablesSesionUsuario.getIdTramiteBaseTrabajo();
				
				determinacionesCUList = servicioBasicoCondicionesUrbanisticas.obtenerDeterminacionesAplicablesComoCUPorTramite(entTrabajo, idTramiteSeleccionado);
				
				// FGA: Para ordenarlo
				//sort();
				
				
				idEntidadOLD = entTrabajo;
				idTipoTramiteTrabajoOLD = idTipoTramiteTrabajo;
				
			}
			*/
			
			/*
			if (!oldSort.equals(sortColumnName) || oldAscending != ascending) {
				log.debug("[getDeterminacionesCUList] reordeno");
		          sort();
		          oldSort = sortColumnName;
		          oldAscending = ascending;
		    }
		    */
		}
		else
		{
			log.debug("[getCuSimplificadaList] entTrabajo=0");
			// No se ha seleccionado todavia ninguna entTrabajo
			determinacionesCUList = new ArrayList<DeterminacionDTO>();
		}
		
		
		return determinacionesCUList;
	}
	
	public void rowSelectionListener (RowSelectorEvent event)
	{
		int idCU = 0;
		log.info("[rowSelectionListener] Inicio");
		if(determinacionesCUList != null) {
			for(DeterminacionDTO cu : determinacionesCUList) {
				if(cu.isSeleccionada()) {
					
					idCU = cu.getIdDeterminacion();
					cuSeleccionada = cu;
					
					break;
				}					
			}
		}
	}
	
	
	
	public DeterminacionDTO getCuSeleccionada() {
		return cuSeleccionada;
	}

	public void seleccionarCU() {
		
		/*
		int idCU = 0;
		if(determinacionesCUList != null) {
			for(DeterminacionDTO cu : determinacionesCUList) {
				if(cu.isSeleccionada()) {
					idCU = cu.getIdDeterminacion();
					break;
				}
			}
		}
		
		gestionCondicionesUrbanisticas.crearDeterminacionCUSeleccionada(idCU);
		FacesManager.instance().redirect("/produccionfip/gestionentidades/CUSimplificadaContenido.xhtml");
		*/
		
		// FGA:
		int idCU = cuSeleccionada.getIdDeterminacion();
		
		gestionCondicionesUrbanisticas.crearDeterminacionCUSeleccionada(idCU);
		
		// Recargo la lista y la recorro quitando la seleccion
		getDeterminacionesCUList();
		
		if(determinacionesCUList != null) {
			for(DeterminacionDTO cu : determinacionesCUList) {
				if(cu.isSeleccionada()) {
					
					cu.setSeleccionada(false);
				}					
			}
		}
		
		// Quito la seleccionada
		cuSeleccionada = new DeterminacionDTO();
		
		FacesManager.instance().redirect("/produccionfip/gestionentidades/CUSimplificadaContenido.xhtml");
		
	}
	
	public boolean mostrarBotonSeleccionar() {
		boolean mostrar = false;
		
		if(determinacionesCUList != null) {
			for(DeterminacionDTO cu : determinacionesCUList) {
				if(cu.isSeleccionada()) {
					mostrar = true;
					break;
				}
			}
		}
		
		return mostrar;
	}
	
	public int getIdTipoTramiteTrabajo() {
		return idTipoTramiteTrabajo;
	}

	public void setIdTipoTramiteTrabajo(int idTipoTramiteTrabajo) {
		this.idTipoTramiteTrabajo = idTipoTramiteTrabajo;
	}
	
	
}

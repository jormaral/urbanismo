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

import java.text.Collator;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.ejb.Stateless;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.faces.FacesManager;
import org.jboss.seam.log.Log;

import com.mitc.redes.editorfip.entidades.interfazgrafico.DeterminacionDTO;
import com.mitc.redes.editorfip.servicios.basicos.fip.determinaciones.ServicioBasicoDeterminaciones;
import com.mitc.redes.editorfip.servicios.genericos.SortableList;
import com.mitc.redes.editorfip.servicios.sesion.VariablesSesionUsuario;

@Stateless
@Name("listaDeterminacionesBaseTramite")
public class ListaDeterminacionesBaseTramiteBean extends SortableList implements ListaDeterminacionesBaseTramite {

	@Logger
	private Log log;

	@In(create = true)
	ServicioBasicoDeterminaciones servicioBasicoDeterminaciones;

	@In
	VariablesSesionUsuario variablesSesionUsuario;
	
	@In
	GestionDeterminaciones gestionDeterminaciones;

	private List<DeterminacionDTO> determinacionesBase = null;
	private static final String nombreColumnName = "nombre";
	private static final String ordenColumnName = "orden";
	private Integer idDeterminacionTrabajoOLD=0;
	
	

	public ListaDeterminacionesBaseTramiteBean() {
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
        Collections.sort(determinacionesBase, comparator);
        log.debug("[sort] Fin");

	}

	

	@Override
	public String getNombreColumnName() {		
		return nombreColumnName;
	}

	
	
	
	public String getOrdenColumnName() {
		return ordenColumnName;
	}



	public List<DeterminacionDTO> getDeterminacionesBase() {
		
			try { 
				
				if (idDeterminacionTrabajoOLD != gestionDeterminaciones.getIdDeterminacion())
				{
					idDeterminacionTrabajoOLD = gestionDeterminaciones.getIdDeterminacion();
					
					determinacionesBase = servicioBasicoDeterminaciones.obtenerDeterminacionesBase(idDeterminacionTrabajoOLD,variablesSesionUsuario.getIdTramiteBaseTrabajo());
					
					log.debug("[getDeterminacionesBase] Numero de Determinaciones Base obtenidos="+determinacionesBase.size()+" para el Plan Base="+variablesSesionUsuario.getIdTramiteBaseTrabajo());
					
				}
				
				
				if (!oldSort.equals(sortColumnName) || oldAscending != ascending) {
				      sort();
				      oldSort = sortColumnName;
				      oldAscending = ascending;
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		
		
		return determinacionesBase;
	}
	
	public void seleccionarDeterminacionBase() {
		
		for(DeterminacionDTO det : determinacionesBase) {
			if(det.isSeleccionada()) {
				gestionDeterminaciones.setIdDeterminacionBase(det.getIdDeterminacion());
				break;
			}
		}
		
		gestionDeterminaciones.setMostrarPanelDetBase(false);
		FacesManager.instance().redirect("/produccionfip/gestiondiccionariodeterminaciones/VerDeterminacionPlanEncargado.xhtml");
	}
	
	public boolean detBaseSeleccionada() {
		boolean seleccionada = false;
		
		if(determinacionesBase != null) {
			for(DeterminacionDTO det : determinacionesBase) {
				if(det.isSeleccionada()) {
					seleccionada = true;
					break;
				}
			}
		}		
		return seleccionada;
	}
	

}

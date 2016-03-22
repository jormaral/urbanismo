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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.ejb.Stateless;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.log.Log;

import com.mitc.redes.editorfip.entidades.interfazgrafico.GrupoAplicacionTramiteDTO;
import com.mitc.redes.editorfip.servicios.basicos.fip.determinaciones.ServicioBasicoDeterminaciones;
import com.mitc.redes.editorfip.servicios.genericos.SortableList;
import com.mitc.redes.editorfip.servicios.sesion.VariablesSesionUsuario;

@Stateless
@Name("listaGruposAplicacionDeterminacionTramite")
public class ListaGruposAplicacionDeterminacionTramiteBean extends SortableList implements
		ListaGruposAplicacionDeterminacionTramite {

	@Logger
	private Log log;

	@In(create = true)
	ServicioBasicoDeterminaciones servicioBasicoDeterminaciones;

	@In
	VariablesSesionUsuario variablesSesionUsuario;

	private List<GrupoAplicacionTramiteDTO> gruposAplicacion = null;
	private static final String nombreColumnName = "nombre";
	private static final String ordenColumnName = "orden";
	private Integer idTramiteTrabajoGAOLD=0;
	
	

	public ListaGruposAplicacionDeterminacionTramiteBean() {
		super(nombreColumnName);
		
	}

	public void refrescar()
	{
		log.debug("[refrescar] refrescar");
		int tramTrabajo = variablesSesionUsuario.getIdTramiteBaseTrabajo();
		gruposAplicacion = servicioBasicoDeterminaciones.obtenerGruposAplicacionTramite(tramTrabajo);
		
		//List<GrupoAplicacionTramiteDTO> gruposSeleccionados = new ArrayList<GrupoAplicacionTramiteDTO> ();
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
            	GrupoAplicacionTramiteDTO d1 = (GrupoAplicacionTramiteDTO) o1;
            	GrupoAplicacionTramiteDTO d2 = (GrupoAplicacionTramiteDTO) o2;
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
        Collections.sort(gruposAplicacion, comparator);
        log.debug("[sort] Fin");

	}

	

	@Override
	public String getNombreColumnName() {		
		return nombreColumnName;
	}

	
	
	
	public String getOrdenColumnName() {
		return ordenColumnName;
	}



	public List<GrupoAplicacionTramiteDTO> getGruposAplicacion() {
			
			if (idTramiteTrabajoGAOLD != variablesSesionUsuario.getIdTramiteTrabajoActual())
			{
				
				
				int tramTrabajo = variablesSesionUsuario.getIdTramiteBaseTrabajo();
				
				gruposAplicacion = servicioBasicoDeterminaciones.obtenerGruposAplicacionTramite(tramTrabajo);
				log.debug("[getGruposAplicacion] Numero de Grupos de Aplicacion obtenidos="+gruposAplicacion.size());
				idTramiteTrabajoGAOLD = variablesSesionUsuario.getIdTramiteTrabajoActual();
			}
			
			
			/*
			if (!oldSort.equals(sortColumnName) || oldAscending != ascending) {
		          sort();
		          oldSort = sortColumnName;
		          oldAscending = ascending;
		    }
		    */
		
		
		
		return gruposAplicacion;
	}
	
	public int obtenerIdGrupoAplicacionSeleccionado() {
		
		int idGASel = 0;
		if(gruposAplicacion != null) {
			for(GrupoAplicacionTramiteDTO grupo : gruposAplicacion) {
				if(grupo.isSeleccionada())
					idGASel = grupo.getIdDeterminacion();
			}
		}
		
		return idGASel;
		
	}
	
	public List<GrupoAplicacionTramiteDTO> obtenerGruposAplicacionSeleccionados() {
		
		List<GrupoAplicacionTramiteDTO> gruposSeleccionados = new ArrayList<GrupoAplicacionTramiteDTO> ();
		
		if(gruposAplicacion != null) {
			for(GrupoAplicacionTramiteDTO grupo : gruposAplicacion) {
				if(grupo.isSeleccionada())
					gruposSeleccionados.add(grupo);
			}
		}
		
		return gruposSeleccionados;
	}
	
	public boolean mostrarBotonSeleccionar() {
		boolean mostrar = false;
		
		if(gruposAplicacion != null) {
			for(GrupoAplicacionTramiteDTO grupo : gruposAplicacion) {
				if(grupo.isSeleccionada()) {
					mostrar = true;
					break;
				}					
			}
		}
		
		return mostrar;
	}

}

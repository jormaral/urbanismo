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

import java.util.List;

import javax.ejb.Stateless;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.log.Log;

import com.icesoft.faces.component.ext.RowSelectorEvent;
import com.mitc.redes.editorfip.entidades.interfazgrafico.GrupoAplicacionTramiteDTO;
import com.mitc.redes.editorfip.servicios.basicos.fip.determinaciones.ServicioBasicoDeterminaciones;
import com.mitc.redes.editorfip.servicios.sesion.VariablesSesionUsuario;

@Stateless

@Name("listaGruposAplicacionTramite")
public class ListaGruposAplicacionTramiteBean  implements
		ListaGruposAplicacionTramite {

	@Logger
	private Log log;

	@In(create = true, required = false) 
	ServicioBasicoDeterminaciones servicioBasicoDeterminaciones;
	
	@In(create = true, required = false) 
	SeleccionarListaGruposAplicacionTramite seleccionarListaGruposAplicacionTramite;

	@In
	VariablesSesionUsuario variablesSesionUsuario;

	private List<GrupoAplicacionTramiteDTO> gruposAplicacion = null;

	private int idTramiteTrabajoArbol = -2;
	private int idTipoTramiteTrabajo = 1;
	private int idTipoTramiteTrabajoOLD = -1;
	
	private boolean mostrar = false;
	private int idGASel = 0;
	
	
	
	public void inicializarLista() {
		
		log.debug("[inicializarLista] inicializarLista: idTramiteTrabajoArbol="+idTramiteTrabajoArbol);
		
		// Si es -2 lo inicializo al plan base 
		if (idTramiteTrabajoArbol==-2)
		{
			idTramiteTrabajoArbol = variablesSesionUsuario.getIdTramiteBaseTrabajo();
			log.debug("[inicializarLista] Si es -2 lo inicializo al plan base: idTramiteTrabajoArbol="+idTramiteTrabajoArbol);
		}
		//int tramTrabajo = variablesSesionUsuario.getIdTramiteBaseTrabajo();
		gruposAplicacion = servicioBasicoDeterminaciones.obtenerGruposAplicacionTramite(idTramiteTrabajoArbol);
		log.debug("[inicializarLista] Numero de Grupos de Aplicacion obtenidos="+gruposAplicacion.size());
		
	}


	public void rowSelectionListener (RowSelectorEvent event)
	{
		log.debug("[rowSelectionListener] Antes de seleccionar. idGASel: "+idGASel);
		log.debug("[rowSelectionListener] Inicio");
		if(gruposAplicacion != null) {
			for(GrupoAplicacionTramiteDTO grupo : gruposAplicacion) {
				if(grupo.isSeleccionada()) {
					
					idGASel = grupo.getIdDeterminacion();
					mostrar = true;
					
					seleccionarListaGruposAplicacionTramite.setIdGASel(idGASel);
					seleccionarListaGruposAplicacionTramite.setMostrar(true);
					log.debug("[rowSelectionListener] mostrar = true: "+idGASel);
					break;
				}					
			}
		}
	}

	
/*

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
*/


	public List<GrupoAplicacionTramiteDTO> getGruposAplicacion() {
		
		boolean actualizaArbol = false;
		
		// Si se ha cambiado el tipo de tramite
		if(idTipoTramiteTrabajo != idTipoTramiteTrabajoOLD) {
			
			
			// Permito actualizar la lista
			actualizaArbol = true;
			
			switch(idTipoTramiteTrabajo) {
				case 3 : idTramiteTrabajoArbol = variablesSesionUsuario.getIdTramiteEncargadoTrabajo();
				 		break;
				case 2 : idTramiteTrabajoArbol = variablesSesionUsuario.getIdTramiteVigenteTrabajo();
						 break;
				case 1 : idTramiteTrabajoArbol = variablesSesionUsuario.getIdTramiteBaseTrabajo();
						 break;										 
			}
			
			idTipoTramiteTrabajoOLD = idTipoTramiteTrabajo;
		}
		
		

		
		
		
		if (gruposAplicacion==null || actualizaArbol)
			inicializarLista();
			
		
		
		
		return gruposAplicacion;
	}
	
	public void refrescar()
	{
		
		mostrar = false;
		idGASel = 0;
		gruposAplicacion = servicioBasicoDeterminaciones.obtenerGruposAplicacionTramite(idTramiteTrabajoArbol);
		seleccionarListaGruposAplicacionTramite.reiniciar();
		log.debug("[refrescar] Numero de Grupos de Aplicacion obtenidos="+gruposAplicacion.size());
	}
	
	public int obtenerIdGrupoAplicacionSeleccionado() {
		
		log.debug("[obtenerIdGrupoAplicacionSeleccionado] idGASel: "+idGASel);		
		return idGASel;
		
	}
	
	
	
	public boolean mostrarBotonSeleccionar() {
	
		return mostrar;
	}
	
	public int getIdTipoTramiteTrabajo() {
		return idTipoTramiteTrabajo;
	}

	public void setIdTipoTramiteTrabajo(int idTipoTramiteTrabajo) {
		this.idTipoTramiteTrabajo = idTipoTramiteTrabajo;
	}


}

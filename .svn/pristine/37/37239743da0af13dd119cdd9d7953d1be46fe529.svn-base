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

import java.util.List;

import javax.ejb.Stateless;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.log.Log;

import com.icesoft.faces.component.ext.RowSelectorEvent;
import com.mitc.redes.editorfip.entidades.interfazgrafico.DeterminacionDTO;
import com.mitc.redes.editorfip.servicios.basicos.fip.determinaciones.ServicioBasicoDeterminaciones;
import com.mitc.redes.editorfip.servicios.sesion.VariablesSesionUsuario;

@Stateless
@Name("listaDeterminacionesRegimenCU")
public class ListaDeterminacionesRegimenCUBean implements ListaDeterminacionesRegimenCU {

	@Logger
	private Log log;

		
	@In(create = true, required = false)
	ServicioBasicoDeterminaciones servicioBasicoDeterminaciones;
	
	@In(create = true, required = false) 
	SeleccionarListaDeterminacionRegimenCU seleccionarListaDeterminacionRegimenCU;

	@In(create = true, required = false)
	VariablesSesionUsuario variablesSesionUsuario;

	private List<DeterminacionDTO> determinacionesRegimenCUList = null;
	
	private int idTramiteTrabajoArbol = 0;
	private int idTipoTramiteTrabajo = 3;
	private int idTipoTramiteTrabajoOLD = -1;
	
	private boolean mostrar = false;
	private int idGASel = 0;
	
	

	public void inicializarLista() {
		
		log.debug("[inicializarLista] inicializarLista: idTramiteTrabajoArbol="+idTramiteTrabajoArbol);
		
		// Si es cero lo inicializo al plan base 
		if (idTramiteTrabajoArbol==0)
		{
			idTramiteTrabajoArbol = variablesSesionUsuario.getIdTramiteBaseTrabajo();
			log.debug("[inicializarLista] Si es cero lo inicializo al plan base: idTramiteTrabajoArbol="+idTramiteTrabajoArbol);
		}
		//int tramTrabajo = variablesSesionUsuario.getIdTramiteBaseTrabajo();
		determinacionesRegimenCUList = servicioBasicoDeterminaciones.getListDeterminacionRegimenDeTramite(idTramiteTrabajoArbol);
		log.debug("[inicializarLista] Numero de determinacionesRegimenCUList obtenidos="+determinacionesRegimenCUList.size());
		
	}


	public void rowSelectionListener (RowSelectorEvent event)
	{
		log.debug("[rowSelectionListener] Antes de seleccionar. idGASel: "+idGASel);
		log.debug("[rowSelectionListener] Inicio");
		if(determinacionesRegimenCUList != null) {
			for(DeterminacionDTO grupo : determinacionesRegimenCUList) {
				if(grupo.isSeleccionada()) {
					
					idGASel = grupo.getIdDeterminacion();
					mostrar = true;
					
					seleccionarListaDeterminacionRegimenCU.setIdGASel(idGASel);
					seleccionarListaDeterminacionRegimenCU.setMostrar(true);
					log.debug("[rowSelectionListener] mostrar = true: "+idGASel);
					break;
				}					
			}
		}
	}



	public List<DeterminacionDTO> getDeterminacionesRegimenCUList() {
		
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
		
		
		
		if (determinacionesRegimenCUList==null || actualizaArbol)
			inicializarLista();
			
		
		
		
		return determinacionesRegimenCUList;
	}
	
	public void refrescar()
	{
		
		mostrar = false;
		idGASel = 0;
		determinacionesRegimenCUList = servicioBasicoDeterminaciones.getListDeterminacionRegimenDeTramite(idTramiteTrabajoArbol);
		seleccionarListaDeterminacionRegimenCU.reiniciar();
		log.debug("[refrescar] Numero de determinacionesRegimenCUList obtenidos="+determinacionesRegimenCUList.size());
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

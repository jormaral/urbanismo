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

package com.mitc.redes.editorfip.servicios.produccionfip.gestionadscripciones;

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

import com.icesoft.faces.component.ext.RowSelectorEvent;
import com.mitc.redes.editorfip.entidades.interfazgrafico.AdscripcionesTramiteDTO;
import com.mitc.redes.editorfip.entidades.interfazgrafico.GrupoAplicacionTramiteDTO;
import com.mitc.redes.editorfip.servicios.basicos.fip.determinaciones.ServicioBasicoDeterminaciones;
import com.mitc.redes.editorfip.servicios.genericos.SortableList;
import com.mitc.redes.editorfip.servicios.sesion.VariablesSesionUsuario;
import com.mitc.redes.editorfip.utilidades.Constantes;

@Stateless
@Name("listaTipoAdscripciones")
public class ListaTipoAdscripcionesBean extends SortableList implements ListaTipoAdscripciones
{
   
	@Logger private Log log;

	@In(create = true, required = false)
    ServicioBasicoDeterminaciones servicioBasicoDeterminaciones;
    
    @In
	VariablesSesionUsuario variablesSesionUsuario;

    
   
    private static final String nombreColumnName = Constantes.NOMBRE_COLUMNA_NOMBRE;
    
 // Variable que contiene el identificador del tramite sobre el que se esta trabajando
    private Integer idTipoTramiteTrabajoSUOLD=0;
	private Integer idTipoTramiteTrabajoSU=1; 
	private Integer idTramiteTrabajoSUOLD=0;
	
	private List<AdscripcionesTramiteDTO> listaAdscripciones = null;
    
  
    
    public ListaTipoAdscripcionesBean() {
		super(nombreColumnName);
		
	}

    public Integer getIdTipoTramiteTrabajoSU() {
		return idTipoTramiteTrabajoSU;
	}



	public void setIdTipoTramiteTrabajoSU(Integer idTipoTramiteTrabajoSU) {
		this.idTipoTramiteTrabajoSU = idTipoTramiteTrabajoSU;
	}


	@Override
	public boolean isDefaultAscending(String sortColumn) {
		return true;
	}

	
	@Override
	public void sort() {
		
		log.debug("[sort] Inicio");
		
		final Collator comparador = Collator.getInstance();
		comparador.setStrength(Collator.PRIMARY);
		
		
		Comparator<Object> comparator = new Comparator<Object>() {

            public int compare(Object o1, Object o2) {
            	AdscripcionesTramiteDTO d1 = (AdscripcionesTramiteDTO) o1;
            	AdscripcionesTramiteDTO d2 = (AdscripcionesTramiteDTO) o2;
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
        Collections.sort(listaAdscripciones, comparator);
        log.debug("[sort] Fin");

	}
	
	
    
    
	public List<AdscripcionesTramiteDTO> obtenerListaAdscripcionesTipo() {
		
		log.info("[getUnidades] idTipoTramiteTrabajoSU="+idTipoTramiteTrabajoSU
				+" idTipoTramiteTrabajoSUOLD="+idTipoTramiteTrabajoSUOLD
				+" idTramiteTrabajoSUOLD="+idTramiteTrabajoSUOLD
				+" variablesSesionUsuario.getIdTramiteTrabajoActual()="+variablesSesionUsuario.getIdTramiteTrabajoActual()
				+" oldSort="+oldSort+" sortColumnName="+sortColumnName+" oldAscending="+oldAscending+" ascending="+ascending);
		
			if (idTipoTramiteTrabajoSU==0)
			{
				idTipoTramiteTrabajoSU = variablesSesionUsuario.getIdTipoTramiteTrabajo();
				log.debug("[obtenerListaAdscripcionesTipo]idTipoTramiteTrabajoGA==0. AHORA: idTipoTramiteTrabajoGA="+idTipoTramiteTrabajoSU);
			}
			
			if (idTipoTramiteTrabajoSU!=idTipoTramiteTrabajoSUOLD || idTramiteTrabajoSUOLD != variablesSesionUsuario.getIdTramiteTrabajoActual())
			{
				log.debug("[obtenerListaAdscripcionesTipo] idTipoTramiteTrabajoGA="+idTipoTramiteTrabajoSU);
				
				int tramTrabajo=0;
				
				if (idTipoTramiteTrabajoSU==1)
				{
					tramTrabajo = variablesSesionUsuario.getIdTramiteBaseTrabajo();
				}
				if (idTipoTramiteTrabajoSU==2)
				{
					tramTrabajo = variablesSesionUsuario.getIdTramiteVigenteTrabajo();
				}
				if (idTipoTramiteTrabajoSU==3)
				{
					tramTrabajo = variablesSesionUsuario.getIdTramiteEncargadoTrabajo();
				}
				
				log.debug("[obtenerListaAdscripcionesTipo] obtenerDeterminacionesAdscripcionesTramite tramTrabajo="+tramTrabajo);
				listaAdscripciones = servicioBasicoDeterminaciones.obtenerDeterminacionesAdscripcionesTramite(tramTrabajo);
					
				
				log.debug("[obtenerListaAdscripcionesTipo] Numero de listaAdscripciones obtenidos="+listaAdscripciones.size());
				idTipoTramiteTrabajoSUOLD = idTipoTramiteTrabajoSU;
				idTramiteTrabajoSUOLD = variablesSesionUsuario.getIdTramiteTrabajoActual();
				
				// Como es la primera vez, lo ordeno por nombre y ascendente
				sortColumnName = "nombre";
				ascending = true;
				
			}
			
			
			/*
			if (!oldSort.equals(sortColumnName) || oldAscending != ascending) {
		          sort();
		          oldSort = sortColumnName;
		          oldAscending = ascending;
		    }
		    */
		
		
		
	
		
		
		return listaAdscripciones;
	}
	
	
	public void rowSelectionListener (RowSelectorEvent event)
	{
		
		log.debug("[rowSelectionListener] Inicio");
		
	}
	
	public String getNombreColumnName() {
		return nombreColumnName;
	}
	
	public void seleccionarTipoAdscripcion(int idDetTipoAdscripcion) {
		for(int i=0; i<listaAdscripciones.size(); i++) {
			int idDet = listaAdscripciones.get(i).getIdDeterminacion();
			listaAdscripciones.get(i).setSeleccionada(idDet == idDetTipoAdscripcion);
		}
	}
	
	public AdscripcionesTramiteDTO obtenerTipoAdscripcionSeleccionado() {
		
		AdscripcionesTramiteDTO tipoSel = null;
		for(AdscripcionesTramiteDTO tipoAds : listaAdscripciones) {
			if(tipoAds.isSeleccionada()) {
				tipoSel = tipoAds;
				break;
			}
		}
		
		return tipoSel;
	}
    
	

}

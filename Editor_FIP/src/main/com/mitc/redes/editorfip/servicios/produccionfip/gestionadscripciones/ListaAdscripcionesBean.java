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

import com.mitc.redes.editorfip.entidades.interfazgrafico.AdscripcionesDTO;
import com.mitc.redes.editorfip.servicios.basicos.fip.adscripciones.ServicioBasicoAdscripciones;
import com.mitc.redes.editorfip.servicios.genericos.SortableList;
import com.mitc.redes.editorfip.servicios.sesion.VariablesSesionUsuario;
import com.mitc.redes.editorfip.utilidades.Constantes;

@Stateless
@Name("listaAdscripciones")
public class ListaAdscripcionesBean extends SortableList implements ListaAdscripciones
{
   
	@Logger private Log log;

	@In(create = true, required = false)
    ServicioBasicoAdscripciones servicioBasicoAdscripciones;
    
    @In
	VariablesSesionUsuario variablesSesionUsuario;

    
   
    private static final String nombreColumnName = Constantes.NOMBRE_COLUMNA_NOMBRE;
    
 // Variable que contiene el identificador del tramite sobre el que se esta trabajando
	private int idTramiteTrabajoOLD;
	
	private List<AdscripcionesDTO> listaAdscripciones = null;
    
    private boolean listaRecargada = false;
    
    public ListaAdscripcionesBean() {
		super(nombreColumnName);
		
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
            	AdscripcionesDTO d1 = (AdscripcionesDTO) o1;
            	AdscripcionesDTO d2 = (AdscripcionesDTO) o2;
                if (sortColumnName == null) {
                    return 0;
                }
                if (sortColumnName.equals(nombreColumnName)) {
                	return ascending ? comparador.compare(d1.getNombreEntidadOrigen(), d2.getNombreEntidadOrigen()) : comparador.compare(d2.getNombreEntidadOrigen(), d1.getNombreEntidadOrigen());
                } else {
               	
                        return 0;
                    
                }
            }
        };
        Collections.sort(listaAdscripciones, comparator);
        log.debug("[sort] Fin");

	}
	
	
    
    
	public List<AdscripcionesDTO> obtenerListaAdscripciones() {
		
		int idTramiteTrabajo = variablesSesionUsuario.getIdTramiteTrabajoActual();
		
		if(listaAdscripciones == null)
			listaRecargada = false;
		
		if (idTramiteTrabajo!=0)
		{
			log.debug("[obtenerListaAdscripciones] idTramiteTrabajo="+idTramiteTrabajo);
			
			if (!listaRecargada || idTramiteTrabajo!=idTramiteTrabajoOLD)
			{
				log.debug("[obtenerListaAdscripciones] Pido los Grupos de Aplicacion");
				log.debug("[obtenerListaAdscripciones] idTramiteTrabajo!=idTramiteTrabajoOLD: "+idTramiteTrabajo+"/"+idTramiteTrabajoOLD);
				listaAdscripciones = servicioBasicoAdscripciones.obtenerListaAdscripcionesReducidaDeTramite(idTramiteTrabajo);
				idTramiteTrabajoOLD = idTramiteTrabajo;
				listaRecargada = true;
				
			}
			
			
			if (!oldSort.equals(sortColumnName) || oldAscending != ascending) {
		          sort();
		          oldSort = sortColumnName;
		          oldAscending = ascending;
		    }
		}
		else
		{
			log.debug("[obtenerListaAdscripciones] idTramiteTrabajo=0");
			// No se ha seleccionado todavia ninguna TramiteTrabajo
			listaAdscripciones = new ArrayList<AdscripcionesDTO>();
		}
		
	
		
		
		return listaAdscripciones;
	}
	
	
	public void refrescarLista()
	{
		int idTramiteTrabajo = variablesSesionUsuario.getIdTramiteTrabajoActual();
		log.debug("[refrescarLista] refresco la lista para el tramite="+idTramiteTrabajo);
		
		listaAdscripciones = servicioBasicoAdscripciones.obtenerListaAdscripcionesReducidaDeTramite(idTramiteTrabajo);
		listaRecargada = true;
	}
	
	public String getNombreColumnName() {
		return nombreColumnName;
	}
    
	

}

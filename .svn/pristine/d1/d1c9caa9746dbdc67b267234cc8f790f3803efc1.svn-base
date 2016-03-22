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
import org.jboss.seam.log.Log;

import com.mitc.redes.editorfip.entidades.interfazgrafico.DeterminacionDTO;
import com.mitc.redes.editorfip.servicios.basicos.fip.determinaciones.ServicioBasicoDeterminaciones;
import com.mitc.redes.editorfip.servicios.genericos.SortableList;

@Stateless
@Name("listaDeterminacionesValorReferenciaCU")
public class ListaDeterminacionesValorReferenciaCUBean extends SortableList implements ListaDeterminacionesValorReferenciaCU {

	@Logger
	private Log log;

		
	@In(create = true, required = false)
	ServicioBasicoDeterminaciones servicioBasicoDeterminaciones;

	@In(create = true, required = false)
	GestionCondicionesUrbanisticas gestionCondicionesUrbanisticas;

	private List<DeterminacionDTO> determinacionesValorReferenciaCUList = new ArrayList<DeterminacionDTO>();
	private static final String nombreColumnName = "nombre";
	private static final String ordenColumnName = "orden";
	private Integer idDetAplicadaDeCUSeleccionadaOLD=0;
	
	
	

	public ListaDeterminacionesValorReferenciaCUBean() {
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
        Collections.sort(determinacionesValorReferenciaCUList, comparator);
        log.debug("[sort] Fin");

	}

	

	@Override
	public String getNombreColumnName() {		
		return nombreColumnName;
	}

	public String getOrdenColumnName() {
		return ordenColumnName;
	}

	
	
	

	public List<DeterminacionDTO> getDeterminacionesValorReferenciaCUList() {
		
		
		int idDetAplicadaDeCUSeleccionada = 0;
		
		if (gestionCondicionesUrbanisticas.getCuSeleccionada().getIdDeterminacionRegimen()==0)
		{
			
			
			idDetAplicadaDeCUSeleccionada = gestionCondicionesUrbanisticas.getCuSeleccionada().getIdDeterminacion();
			log.debug("[getDeterminacionesValorReferenciaCUList] Cogemos VR de idDeterminacion:"+idDetAplicadaDeCUSeleccionada);
		}
		else
		{
			idDetAplicadaDeCUSeleccionada = gestionCondicionesUrbanisticas.getCuSeleccionada().getIdDeterminacionRegimen();
			log.debug("[getDeterminacionesValorReferenciaCUList] Cogemos VR de IdDeterminacionRegimen:"+idDetAplicadaDeCUSeleccionada);
		}
			
			
			
		
		
		if (idDetAplicadaDeCUSeleccionada!=0)
		{
			//log.debug("[getDeterminacionesValorReferenciaCUList] idDetAplicadaDeCUSeleccionada / idDetAplicadaDeCUSeleccionadaOLD: "+idDetAplicadaDeCUSeleccionada+"/"+idDetAplicadaDeCUSeleccionadaOLD);
			
			
			
			if (idDetAplicadaDeCUSeleccionada!=idDetAplicadaDeCUSeleccionadaOLD )
			{
				log.debug("[getDeterminacionesValorReferenciaCUList] idDetAplicadaDeCUSeleccionada!=idDetAplicadaDeCUSeleccionadaOLD: Pido los valores de getListValoresReferenciaDeDeterminacion:"+idDetAplicadaDeCUSeleccionada+"/"+idDetAplicadaDeCUSeleccionadaOLD);
				
				
				determinacionesValorReferenciaCUList = servicioBasicoDeterminaciones.getListValoresReferenciaDeDeterminacion(idDetAplicadaDeCUSeleccionada);
				idDetAplicadaDeCUSeleccionadaOLD = idDetAplicadaDeCUSeleccionada;
				
			}
			
			
			if (!oldSort.equals(sortColumnName) || oldAscending != ascending) {
				log.debug("[getDeterminacionesValorReferenciaCUList] reordeno");
		          sort();
		          oldSort = sortColumnName;
		          oldAscending = ascending;
		    }
		}
		else
		{
			log.debug("[getDeterminacionesValorReferenciaCUList] idDetAplicadaDeCUSeleccionada=0");
			// No se ha seleccionado todavia ninguna idTramiteTrabajo
			determinacionesValorReferenciaCUList = new ArrayList<DeterminacionDTO>();
		}
		
		
		return determinacionesValorReferenciaCUList;
	}
	
	public boolean mostrarBotonSeleccionar() {
		boolean mostrar = false;
		
		if(determinacionesValorReferenciaCUList != null) {
			for(DeterminacionDTO val : determinacionesValorReferenciaCUList) {
				if(val.isSeleccionada()) {
					mostrar = true;
					break;
				}
			}
		}
		
		return mostrar;
	}
	
	public int obtenerIdValRefSel() {
		int idValRef = 0;
		
		if(determinacionesValorReferenciaCUList != null) {
			for(DeterminacionDTO val : determinacionesValorReferenciaCUList) {
				if(val.isSeleccionada()) {
					idValRef = val.getIdDeterminacion();
					val.setSeleccionada(false);
					break;
				}
			}
		}
		
		log.debug("[obtenerIdValRefSel] idValRef="+idValRef);
		return idValRef;
	}

}

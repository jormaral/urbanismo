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

package com.mitc.redes.editorfip.servicios.produccionfip.gestionoperaciones;

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

import com.mitc.redes.editorfip.entidades.interfazgrafico.OperacionEntidadDTO;
import com.mitc.redes.editorfip.servicios.basicos.fip.operaciones.ServicioBasicoOperaciones;
import com.mitc.redes.editorfip.servicios.genericos.SortableList;
import com.mitc.redes.editorfip.servicios.sesion.VariablesSesionUsuario;

@Stateless
@Name("listaOperEntidad")
public class ListaOperEntidadBean extends SortableList implements ListaOperEntidad {

	@Logger
	private Log log;

		
	@In(create = true, required = false)
	ServicioBasicoOperaciones servicioBasicoOperaciones;
	
	@In(create = true, required = false)
	VariablesSesionUsuario variablesSesionUsuario;

	

	private List<OperacionEntidadDTO> operacionEntidadList = new ArrayList<OperacionEntidadDTO>();
	private static final String nombreColumnName = "nombreentoperadora";
	private static final String ordenColumnName = "orden";
	
	
	
	

	public ListaOperEntidadBean() {
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
            	OperacionEntidadDTO d1 = (OperacionEntidadDTO) o1;
            	OperacionEntidadDTO d2 = (OperacionEntidadDTO) o2;
                if (sortColumnName == null) {
                    return 0;
                }
                if (sortColumnName.equals(nombreColumnName)) {
                	return ascending ? comparador.compare(d1.getEntidadOperadora(), d2.getEntidadOperadora()) : comparador.compare(d2.getEntidadOperadora(), d1.getEntidadOperadora());
                } else {
                	if (sortColumnName.equals(ordenColumnName)) {
                    	return ascending ? comparador.compare(d1.getOrden(), d2.getOrden()) : comparador.compare(d2.getOrden(), d1.getOrden());
                    } else {
                        return 0;
                    }
                }
            }
        };
        Collections.sort(operacionEntidadList, comparator);
        log.debug("[sort] Fin");

	}

	

	@Override
	public String getNombreColumnName() {		
		return nombreColumnName;
	}

	public String getOrdenColumnName() {
		return ordenColumnName;
	}

	
	public void refrescarLista()
	{
		int idTramiteEncargado = variablesSesionUsuario.getIdTramiteEncargadoTrabajo();
		log.info("[refrescarLista] idTramiteEncargado="+idTramiteEncargado);

        if (idTramiteEncargado==0){
        	operacionEntidadList = new ArrayList<OperacionEntidadDTO>();
        } else {
        	
        	
        	operacionEntidadList = servicioBasicoOperaciones.obtenerListaOperacionEntidadDTO(idTramiteEncargado);
        }
        
        log.info("[refrescarLista] Longitud operacionEntidadList:" + operacionEntidadList.size());
	}
	

	public List<OperacionEntidadDTO> getOperacionEntidadList() {
		
		
		
		if (!oldSort.equals(sortColumnName) || oldAscending != ascending) {
			log.debug("[getOperacionEntidadList] reordeno");
	          sort();
	          oldSort = sortColumnName;
	          oldAscending = ascending;
	    }
		
		
		return operacionEntidadList;
	}
	
	

}

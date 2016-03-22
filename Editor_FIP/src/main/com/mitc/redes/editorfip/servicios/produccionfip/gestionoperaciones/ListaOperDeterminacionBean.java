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

import com.mitc.redes.editorfip.entidades.interfazgrafico.OperacionDeterminacionDTO;
import com.mitc.redes.editorfip.servicios.basicos.fip.operaciones.ServicioBasicoOperaciones;
import com.mitc.redes.editorfip.servicios.genericos.SortableList;
import com.mitc.redes.editorfip.servicios.sesion.VariablesSesionUsuario;

@Stateless
@Name("listaOperDeterminacion")
public class ListaOperDeterminacionBean extends SortableList implements ListaOperDeterminacion {

	@Logger
	private Log log;

		
	@In(create = true, required = false)
	ServicioBasicoOperaciones servicioBasicoOperaciones;
	
	@In(create = true, required = false)
	VariablesSesionUsuario variablesSesionUsuario;

	

	private List<OperacionDeterminacionDTO> operacionDeterminacionList = new ArrayList<OperacionDeterminacionDTO>();
	private static final String nombreColumnName = "nombreentoperadora";
	private static final String ordenColumnName = "orden";
	
	
	
	

	public ListaOperDeterminacionBean() {
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
            	OperacionDeterminacionDTO d1 = (OperacionDeterminacionDTO) o1;
            	OperacionDeterminacionDTO d2 = (OperacionDeterminacionDTO) o2;
                if (sortColumnName == null) {
                    return 0;
                }
                if (sortColumnName.equals(nombreColumnName)) {
                	return ascending ? comparador.compare(d1.getDeterminacionOperadora(), d2.getDeterminacionOperadora()) : comparador.compare(d2.getDeterminacionOperadora(), d1.getDeterminacionOperadora());
                } else {
                	if (sortColumnName.equals(ordenColumnName)) {
                    	return ascending ? comparador.compare(d1.getOrden(), d2.getOrden()) : comparador.compare(d2.getOrden(), d1.getOrden());
                    } else {
                        return 0;
                    }
                }
            }
        };
        Collections.sort(operacionDeterminacionList, comparator);
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
        	operacionDeterminacionList = new ArrayList<OperacionDeterminacionDTO>();
        } else {
        	
        	
        	operacionDeterminacionList = servicioBasicoOperaciones.obtenerListaOperacionDeterminacionDTO(idTramiteEncargado);
        }
        
        log.info("[refrescarLista] Longitud operacionEntidadList:" + operacionDeterminacionList.size());
	}
	

	public List<OperacionDeterminacionDTO> getOperacionDeterminacionList() {
		
		
		
		if (!oldSort.equals(sortColumnName) || oldAscending != ascending) {
			log.debug("[getOperacionEntidadList] reordeno");
	          sort();
	          oldSort = sortColumnName;
	          oldAscending = ascending;
	    }
		
		
		return operacionDeterminacionList;
	}
	
	

}

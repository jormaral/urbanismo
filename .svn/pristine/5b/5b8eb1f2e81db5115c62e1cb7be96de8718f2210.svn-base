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

import com.mitc.redes.editorfip.entidades.interfazgrafico.OperacionesDTO;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Operacion;
import com.mitc.redes.editorfip.servicios.basicos.fip.operaciones.ServicioBasicoOperaciones;
import com.mitc.redes.editorfip.servicios.genericos.SortableList;
import com.mitc.redes.editorfip.servicios.sesion.VariablesSesionUsuario;

@Stateless
@Name("ordenarOperaciones")
public class OrdenarOperacionesBean extends SortableList implements OrdenarOperaciones {

	@Logger
	private Log log;

		
	@In(create = true, required = false)
	ServicioBasicoOperaciones servicioBasicoOperaciones;
	
	@In(create = true, required = false)
	VariablesSesionUsuario variablesSesionUsuario;

	

	private List<OperacionesDTO> operacionesList = new ArrayList<OperacionesDTO>();
	private static final String nombreColumnName = "nombreentoperadora";
	private static final String ordenColumnName = "orden";
	
	
	
	

	public OrdenarOperacionesBean() {
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
            	OperacionesDTO d1 = (OperacionesDTO) o1;
            	OperacionesDTO d2 = (OperacionesDTO) o2;
                if (sortColumnName == null) {
                    return 0;
                }
                if (sortColumnName.equals(ordenColumnName)) {
                	return ascending ? comparador.compare(d1.getOrden(), d2.getOrden()) : comparador.compare(d2.getOrden(), d1.getOrden());
                	
                } else {
                    return 0;
                }
            }
        };
        Collections.sort(operacionesList, comparator);
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
        	operacionesList = new ArrayList<OperacionesDTO>();
        } else {
        	
        	
        	operacionesList = servicioBasicoOperaciones.obtenerListaOperacionesDTO(idTramiteEncargado);
        }
        
        log.info("[refrescarLista] Longitud operacionList:" + operacionesList.size());
	}
	

	public List<OperacionesDTO> getOperacionesList() {
		
		
		
		if (!oldSort.equals(sortColumnName) || oldAscending != ascending) {
			log.debug("[getOperacionesList] reordeno");
	          sort();
	          oldSort = sortColumnName;
	          oldAscending = ascending;
	    }
		
		
		return operacionesList;
	}

	@Override
	public void bajarOrden(int idOperacion) {
		
		log.info("[bajarOrden] idOperacion:" + idOperacion);
		
		// Buscamos la operacion a modificar
		Operacion op = servicioBasicoOperaciones.findOperacion(idOperacion);		
		
		// Obtenemos el orden actual y calculamos el nuevo orden
		int ordenOLD = op.getOrden();
		int ordenNEW = ordenOLD + 1;
		
		// Buscamos la operacion con el nuevo orden para ser modificada.
		Operacion opReemplazar = servicioBasicoOperaciones.findOperacionByOrden(ordenNEW,variablesSesionUsuario.getIdTramiteTrabajoActual());		
		
		// Aplicamos los nuevos ordenes
		op.setOrden(ordenNEW);
		opReemplazar.setOrden(ordenOLD);
		
		// Actualizamos las operaciones
		servicioBasicoOperaciones.editOperacion(op);
		servicioBasicoOperaciones.editOperacion(opReemplazar);		
		
	}

	@Override
	public void subirOrden(int idOperacion) {
		
		log.info("[subirOrden] idOperacion:" + idOperacion);
		
		// Buscamos la operacion a modificar
		Operacion op = servicioBasicoOperaciones.findOperacion(idOperacion);		
		
		// Obtenemos el orden actual y calculamos el nuevo orden
		int ordenOLD = op.getOrden();
		int ordenNEW = ordenOLD - 1;
		
		// Buscamos la operacion con el nuevo orden para ser modificada.
		Operacion opReemplazar = servicioBasicoOperaciones.findOperacionByOrden(ordenNEW, variablesSesionUsuario.getIdTramiteTrabajoActual());		
		
		// Aplicamos los nuevos ordenes
		op.setOrden(ordenNEW);
		opReemplazar.setOrden(ordenOLD);
		
		// Actualizamos las operaciones
		servicioBasicoOperaciones.editOperacion(op);
		servicioBasicoOperaciones.editOperacion(opReemplazar);
		
	}
	
	public int getOrdenMaximo() {
		return servicioBasicoOperaciones.findOrdenMaximo(variablesSesionUsuario.getIdTramiteTrabajoActual());
	}
	
	

}

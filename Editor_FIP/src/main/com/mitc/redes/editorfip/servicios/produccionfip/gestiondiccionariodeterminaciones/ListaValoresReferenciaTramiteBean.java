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

import com.mitc.redes.editorfip.entidades.interfazgrafico.ValorReferenciaTramiteDTO;
import com.mitc.redes.editorfip.servicios.basicos.fip.determinaciones.ServicioBasicoDeterminaciones;
import com.mitc.redes.editorfip.servicios.genericos.SortableList;
import com.mitc.redes.editorfip.servicios.sesion.VariablesSesionUsuario;

@Stateless
@Name("listaValoresReferenciaTramite")
public class ListaValoresReferenciaTramiteBean extends SortableList implements
		ListaValoresReferenciaTramite {

	@Logger
	private Log log;

	@In(create = true)
	ServicioBasicoDeterminaciones servicioBasicoDeterminaciones;

	@In
	VariablesSesionUsuario variablesSesionUsuario;
	
	@In
	GestionDeterminaciones gestionDeterminaciones;

	private List<ValorReferenciaTramiteDTO> valoresReferencia = null;
	private static final String nombreColumnName = "nombre";
	private static final String ordenColumnName = "orden";
	private Integer idTramiteTrabajoVROLD=0;
	private boolean refrescar = true;
	
	

	public ListaValoresReferenciaTramiteBean() {
		super(nombreColumnName);
		
	}

	


	public void setRefrescar(boolean refrescar) {
		this.refrescar = refrescar;
	}




	@Override
	public boolean isDefaultAscending(String nombreColumnName) {
		
		return true;
	}
	
	/*
	@Override
	public void sort() {
		
	}
	*/

	
	@Override
	public void sort() {
		
		log.debug("[sort] Inicio");
		
		final Collator comparador = Collator.getInstance();
		comparador.setStrength(Collator.PRIMARY);
		
		

        Comparator<Object> comparator = new Comparator<Object>() {

            public int compare(Object o1, Object o2) {
            	ValorReferenciaTramiteDTO d1 = (ValorReferenciaTramiteDTO) o1;
            	ValorReferenciaTramiteDTO d2 = (ValorReferenciaTramiteDTO) o2;
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
        Collections.sort(valoresReferencia, comparator);
        log.debug("[sort] Fin");

	}
	
	

	@Override
	public String getNombreColumnName() {		
		return nombreColumnName;
	}

	
	
	
	public String getOrdenColumnName() {
		return ordenColumnName;
	}



	public List<ValorReferenciaTramiteDTO> getValoresReferencia() {
		
			try { 
				
				if (refrescar || idTramiteTrabajoVROLD != variablesSesionUsuario.getIdTramiteTrabajoActual())
				{
					
					int tramTrabajo = variablesSesionUsuario.getIdTramiteTrabajoActual();
					
					int idDeterminacion = gestionDeterminaciones.getIdDeterminacion();
					if(idDeterminacion!=0)
						valoresReferencia = servicioBasicoDeterminaciones.obtenerValoresReferenciaTramiteYDeterminacion(tramTrabajo, idDeterminacion);
					else
						valoresReferencia = servicioBasicoDeterminaciones.obtenerValoresReferenciaTramite(tramTrabajo);
					
					log.debug("[getValoresReferencia] Numero de Valores de Referencia obtenidos="+valoresReferencia.size());
					idTramiteTrabajoVROLD = variablesSesionUsuario.getIdTramiteTrabajoActual();
					refrescar=false;
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
		
		
		
		return valoresReferencia;
	}
	
	public List<ValorReferenciaTramiteDTO> obtenerValoresReferenciaSeleccionados() {
		List<ValorReferenciaTramiteDTO> seleccionados = new ArrayList<ValorReferenciaTramiteDTO>();
		if(valoresReferencia != null){
			for(ValorReferenciaTramiteDTO valor : valoresReferencia) {
				if(valor.isSeleccionada())
					seleccionados.add(valor);
			}
		}
		
		return seleccionados;
	}

}

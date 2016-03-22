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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.ejb.Remove;
import javax.ejb.Stateless;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.log.Log;

import com.mitc.redes.editorfip.entidades.interfazgrafico.UnidadTramiteDTO;
import com.mitc.redes.editorfip.servicios.basicos.fip.determinaciones.ServicioBasicoDeterminaciones;
import com.mitc.redes.editorfip.servicios.genericos.SortableList;
import com.mitc.redes.editorfip.servicios.sesion.VariablesSesionUsuario;


@Stateless
@Name("seleccionarUnidadTramite")
public class SeleccionarUnidadTramiteBean extends SortableList implements
		SeleccionarUnidadTramite {

	@Logger
	private Log log;

	@In(create = true)
	ServicioBasicoDeterminaciones servicioBasicoDeterminaciones;

	@In
	VariablesSesionUsuario variablesSesionUsuario;

	private List<UnidadTramiteDTO> unidades = null;
	private static final String nombreColumnName = "nombre";
	private static final String ordenColumnName = "orden";
	private Integer idTipoTramiteTrabajoSUOLD=0;
	private Integer idTipoTramiteTrabajoSU=1; 
	private Integer idTramiteTrabajoSUOLD=0;
	
	
	

	public SeleccionarUnidadTramiteBean() {
		super(nombreColumnName);
		
	}
	
	

	public Integer getIdTipoTramiteTrabajoSU() {
		return idTipoTramiteTrabajoSU;
	}



	public void setIdTipoTramiteTrabajoSU(Integer idTipoTramiteTrabajoSU) {
		this.idTipoTramiteTrabajoSU = idTipoTramiteTrabajoSU;
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
            	UnidadTramiteDTO d1 = (UnidadTramiteDTO) o1;
            	UnidadTramiteDTO d2 = (UnidadTramiteDTO) o2;
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
        Collections.sort(unidades, comparator);
        log.debug("[sort] Fin");

	}

	

	@Override
	public String getNombreColumnName() {		
		return nombreColumnName;
	}

	
	
	
	public String getOrdenColumnName() {
		return ordenColumnName;
	}



	public List<UnidadTramiteDTO> getUnidades() {
		
		log.info("[getUnidades] idTipoTramiteTrabajoSU="+idTipoTramiteTrabajoSU
				+" idTipoTramiteTrabajoSUOLD="+idTipoTramiteTrabajoSUOLD
				+" idTramiteTrabajoSUOLD="+idTramiteTrabajoSUOLD
				+" variablesSesionUsuario.getIdTramiteTrabajoActual()="+variablesSesionUsuario.getIdTramiteTrabajoActual()
				+" oldSort="+oldSort+" sortColumnName="+sortColumnName+" oldAscending="+oldAscending+" ascending="+ascending);
		
			if (idTipoTramiteTrabajoSU==0)
			{
				idTipoTramiteTrabajoSU = variablesSesionUsuario.getIdTipoTramiteTrabajo();
				log.debug("[getUnidades]idTipoTramiteTrabajoGA==0. AHORA: idTipoTramiteTrabajoGA="+idTipoTramiteTrabajoSU);
			}
			
			if (idTipoTramiteTrabajoSU!=idTipoTramiteTrabajoSUOLD || idTramiteTrabajoSUOLD != variablesSesionUsuario.getIdTramiteTrabajoActual())
			{
				log.debug("[getUnidades] idTipoTramiteTrabajoGA="+idTipoTramiteTrabajoSU);
				
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
				
				unidades = servicioBasicoDeterminaciones.obtenerUnidadTramite(tramTrabajo);
					
				
				log.debug("[getUnidades] Numero de Unidades obtenidos="+unidades.size());
				idTipoTramiteTrabajoSUOLD = idTipoTramiteTrabajoSU;
				idTramiteTrabajoSUOLD = variablesSesionUsuario.getIdTramiteTrabajoActual();
				
				// Como es la primera vez, lo ordeno por nombre y ascendente
				sortColumnName = "nombre";
				ascending = true;
				
			}
			
			
			if (!oldSort.equals(sortColumnName) || oldAscending != ascending) {
		          sort();
		          oldSort = sortColumnName;
		          oldAscending = ascending;
		    }
		
		
		
		return unidades;
	}
	
	public void seleccionarUnidad(int idDetUnidad) {
		for(int i=0; i<unidades.size(); i++) {
			int idDet = unidades.get(i).getIdDeterminacion();
			unidades.get(i).setSeleccionada(idDet == idDetUnidad);
		}
	}
	
	public void deSeleccionarTodasUnidad() {
		
		if (unidades!=null)
		{
			for(int i=0; i<unidades.size(); i++) {
				
				unidades.get(i).setSeleccionada(false);
			}
		}
		
	}
	
	public UnidadTramiteDTO obtenerUnidadSeleccionada() {
		UnidadTramiteDTO unidadSel = null;
		for(UnidadTramiteDTO unidad : unidades) {
			if(unidad.isSeleccionada()) {
				unidadSel = unidad;
				break;
			}
		}
		
		
		
		return unidadSel;
	}
	
	@Remove
    public void destroy() {}

}

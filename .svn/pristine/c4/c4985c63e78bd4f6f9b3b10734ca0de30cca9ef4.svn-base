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

import com.mitc.redes.editorfip.entidades.interfazgrafico.DeterminacionReguladoraTramiteDTO;
import com.mitc.redes.editorfip.servicios.basicos.fip.determinaciones.ServicioBasicoDeterminaciones;
import com.mitc.redes.editorfip.servicios.genericos.SortableList;
import com.mitc.redes.editorfip.servicios.sesion.VariablesSesionUsuario;

@Stateless
@Name("listaDeterminacionesReguladorasTramite")
public class ListaDeterminacionesReguladorasTramiteBean extends SortableList implements
ListaDeterminacionesReguladorasTramite {

	@Logger
	private Log log;

	@In(create = true)
	ServicioBasicoDeterminaciones servicioBasicoDeterminaciones;

	@In
	VariablesSesionUsuario variablesSesionUsuario;
	
	@In
	GestionDeterminaciones gestionDeterminaciones;

	private List<DeterminacionReguladoraTramiteDTO> determinacionesReguladoras = null;
	private static final String nombreColumnName = "nombre";
	private static final String ordenColumnName = "orden";
	private Integer idTipoTramiteTrabajoDROLD=0;
	private Integer idTipoTramiteTrabajoDR=1; 
	private Integer idTramiteTrabajoDROLD=0;
	
	

	public ListaDeterminacionesReguladorasTramiteBean() {
		super(nombreColumnName);
		
	}
	
	

	public Integer getIdTipoTramiteTrabajoDR() {
		return idTipoTramiteTrabajoDR;
	}



	public void setIdTipoTramiteTrabajoDR(Integer idTipoTramiteTrabajoDR) {
		this.idTipoTramiteTrabajoDR = idTipoTramiteTrabajoDR;
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
            	DeterminacionReguladoraTramiteDTO d1 = (DeterminacionReguladoraTramiteDTO) o1;
            	DeterminacionReguladoraTramiteDTO d2 = (DeterminacionReguladoraTramiteDTO) o2;
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
        Collections.sort(determinacionesReguladoras, comparator);
        log.debug("[sort] Fin");

	}

	

	@Override
	public String getNombreColumnName() {		
		return nombreColumnName;
	}

	
	
	
	public String getOrdenColumnName() {
		return ordenColumnName;
	}



	public List<DeterminacionReguladoraTramiteDTO> getDeterminacionesReguladoras() {
		
		log.info("[getDeterminacionesReguladoras] idTipoTramiteTrabajoDR="+idTipoTramiteTrabajoDR
				+" idTipoTramiteTrabajoDROLD="+idTipoTramiteTrabajoDROLD
				+" idTramiteTrabajoDROLD="+idTramiteTrabajoDROLD
				+" variablesSesionUsuario.getIdTramiteTrabajoActual()="+variablesSesionUsuario.getIdTramiteTrabajoActual()
				+" oldSort="+oldSort+" sortColumnName="+sortColumnName+" oldAscending="+oldAscending+" ascending="+ascending);
		
			if (idTipoTramiteTrabajoDR==0)
			{
				idTipoTramiteTrabajoDR = variablesSesionUsuario.getIdTipoTramiteTrabajo();
				log.debug("[getDeterminacionesReguladoras]idTipoTramiteTrabajoGA==0. AHORA: idTipoTramiteTrabajoGA="+idTipoTramiteTrabajoDR);
			}
			
			if (idTipoTramiteTrabajoDR!=idTipoTramiteTrabajoDROLD || idTramiteTrabajoDROLD != variablesSesionUsuario.getIdTramiteTrabajoActual())
			{
				log.debug("[getDeterminacionesReguladoras] idTipoTramiteTrabajoGA="+idTipoTramiteTrabajoDR);
				
				int tramTrabajo=0;
				
				if (idTipoTramiteTrabajoDR==1)
				{
					tramTrabajo = variablesSesionUsuario.getIdTramiteBaseTrabajo();
				}
				if (idTipoTramiteTrabajoDR==2)
				{
					tramTrabajo = variablesSesionUsuario.getIdTramiteVigenteTrabajo();
				}
				if (idTipoTramiteTrabajoDR==3)
				{
					tramTrabajo = variablesSesionUsuario.getIdTramiteEncargadoTrabajo();
				}
				
				// int idDeterminacion = gestionDeterminaciones.getIdDeterminacion();
				// if(idDeterminacion!=0)
					// determinacionesReguladoras = servicioBasicoDeterminaciones.obtenerDeterminacionesReguladorasTramiteYDeterminacion(tramTrabajo, idDeterminacion);
				// else
					determinacionesReguladoras = servicioBasicoDeterminaciones.obtenerDeterminacionesReguladorasTramite(tramTrabajo);
				
				log.debug("[getDeterminacionesReguladoras] Numero de Grupos de Aplicacion obtenidos="+determinacionesReguladoras.size());
				idTipoTramiteTrabajoDROLD = idTipoTramiteTrabajoDR;
				idTramiteTrabajoDROLD = variablesSesionUsuario.getIdTramiteTrabajoActual();
			}
			
			
			if (!oldSort.equals(sortColumnName) || oldAscending != ascending) {
		          sort();
		          oldSort = sortColumnName;
		          oldAscending = ascending;
		    }
		
		
		
		return determinacionesReguladoras;
	}
	
	public List<DeterminacionReguladoraTramiteDTO> obtenerDetRegSeleccionadas() {
		List<DeterminacionReguladoraTramiteDTO> seleccionadas = new ArrayList<DeterminacionReguladoraTramiteDTO>();
		if(determinacionesReguladoras != null) {
			for(DeterminacionReguladoraTramiteDTO det : determinacionesReguladoras) {
				if(det.isSeleccionada())
					seleccionadas.add(det);
			}
		}
		
		return seleccionadas;
	}

}

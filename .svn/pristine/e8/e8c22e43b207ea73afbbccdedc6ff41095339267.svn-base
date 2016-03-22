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

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import javax.ejb.Stateless;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.faces.FacesManager;
import org.jboss.seam.log.Log;

import com.mitc.redes.editorfip.entidades.interfazgrafico.DeterminacionReguladoraTramiteDTO;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Determinacion;
import com.mitc.redes.editorfip.servicios.basicos.fip.determinaciones.ServicioBasicoDeterminaciones;
import com.mitc.redes.editorfip.servicios.genericos.SortableList;
import com.mitc.redes.editorfip.utilidades.Constantes;

@Stateless
@Name("listaDeterminacionesReguladoras")
public class ListaDeterminacionesReguladorasBean extends SortableList implements ListaDeterminacionesReguladoras
{
   
	@Logger private Log log;

    @In(create=true)
    ServicioBasicoDeterminaciones servicioBasicoDeterminaciones;
    
    @In
    GestionDeterminaciones gestionDeterminaciones;
    
    private Determinacion[] determinacionesReguladoras = null;
    private static final String nombreColumnName = Constantes.NOMBRE_COLUMNA_NOMBRE;
    
    private int idDeterminacionOLD =0;
    
    private boolean listaRecargada = false;
    
    public ListaDeterminacionesReguladorasBean() {
		super(nombreColumnName);
		
	}

    

	@Override
	public boolean isDefaultAscending(String sortColumn) {
		return true;
	}

	@Override
	public void sort() {
		log.debug("[sort] Inicio");
        Comparator<Object> comparator = new Comparator<Object>() {

            public int compare(Object o1, Object o2) {
                Determinacion d1 = (Determinacion) o1;
                Determinacion d2 = (Determinacion) o2;
                if (sortColumnName == null) {
                    return 0;
                }
                if (sortColumnName.equals(nombreColumnName)) {
                	return ascending ? (d1.getNombre()).compareTo(d2.getNombre()) : (d2.getNombre()).compareTo(d1.getNombre());
                } else {
                    return 0;
                }
            }
        };
        Arrays.sort(determinacionesReguladoras, comparator);
        log.debug("[sort] Fin");
		
	}
    
    
	public Determinacion[] getDeterminacionesReguladoras() {
		
		int detTrabajo = gestionDeterminaciones.getIdDeterminacion();
		
		if(determinacionesReguladoras == null)
			listaRecargada = false;
		
		if (detTrabajo!=0)
		{
			log.debug("[getDeterminacionesReguladoras] detTrabajo="+detTrabajo);
			
			if (!listaRecargada || detTrabajo!=idDeterminacionOLD)
			{
				log.debug("[getDeterminacionesReguladoras] Pido los Grupos de Aplicacion");
				log.debug("[getDeterminacionesReguladoras] detTrabajo!=idDeterminacionOLD: "+detTrabajo+"/"+idDeterminacionOLD);
				determinacionesReguladoras = servicioBasicoDeterminaciones.getArrayReguladoras(detTrabajo);
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
			log.debug("[getDeterminacionesReguladoras] detTrabajo=0");
			// No se ha seleccionado todavia ninguna determinacion
			determinacionesReguladoras = new Determinacion[0];
		}
		
	
		
		
		return determinacionesReguladoras;
	}
	
	public String borrarDeterminacionReguladoraDeterminacion(int idDetReg){
		
		String resultado = "error";
		int detTrabajo = gestionDeterminaciones.getIdDeterminacion();
			
		try
		{
			servicioBasicoDeterminaciones.borrarDeterminacionReguladoraDeterminacion(detTrabajo, idDetReg);
			listaRecargada = false;
			resultado = "success";
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		
		
		
		return resultado;
	}
	
	
	public String getNombreColumnName() {
		return nombreColumnName;
	}
    
	@Override
	public void anadirDeterminacionesReguladoras(List<DeterminacionReguladoraTramiteDTO> determinacionesReg, String nuevaPag) {
		
		for(DeterminacionReguladoraTramiteDTO regSel : determinacionesReg) {
			if(regSel.isSeleccionada() && !grupoAnadido(regSel.getIdDeterminacion()))
				servicioBasicoDeterminaciones.guardarDeterminacionReguladoraDeterminacion(gestionDeterminaciones.getIdDeterminacion(), regSel.getIdDeterminacion());
		}	
		listaRecargada = false;
		gestionDeterminaciones.setMostrarPanelGrupAp(false);
		
		FacesManager.instance().redirect(nuevaPag);
		
	}
	
	private boolean grupoAnadido(int idDetReg) {
		boolean anadido = false;
		for(int i=0; i<getDeterminacionesReguladoras().length; i++) {
			if(determinacionesReguladoras[i].getIden() == idDetReg) {
				anadido = true;
				break;
			}
		}
		return anadido;
	}

}

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

package com.mitc.redes.editorfip.servicios.gestionfip.gestiondocumental;

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
import com.mitc.redes.editorfip.servicios.sesion.VariablesSesionGenerales;
import com.mitc.redes.editorfip.utilidades.Constantes;

@Stateless
@Name("listaDeterminacionesDocumento")
public class ListaDeterminacionesDocumentoBean extends SortableList implements ListaDeterminacionesDocumento
{
   
	@Logger private Log log;

    @In(create=true)
    ServicioBasicoDeterminaciones servicioBasicoDeterminaciones;
    
    @In (create = true, required = false)
   	VariablesSesionGenerales variablesSesionGenerales;
    
    private List<DeterminacionDTO> listaDeterminaciones = null;
    private static final String nombreColumnName = Constantes.NOMBRE_COLUMNA_NOMBRE;
    
    private int idDeterminacionOLD =0;
    
    private boolean listaRecargada = false;
    
	private static final String ordenColumnName = "orden";
    
    public ListaDeterminacionesDocumentoBean() {
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
        Collections.sort(listaDeterminaciones, comparator);
        log.debug("[sort] Fin");

	}
    
    
	public List<DeterminacionDTO> getDeterminacionesDocumento() {
		listaDeterminaciones = new ArrayList<DeterminacionDTO>();
		
		if (variablesSesionGenerales.getIdDocumentoSeleccionado()!=0){
			int idDoc = variablesSesionGenerales.getIdDocumentoSeleccionado();
			
			if(listaDeterminaciones == null)
				listaRecargada = false;
			
			if (idDoc!=0)
			{
				log.debug("[getDeterminaciones] idDoc="+idDoc);
				
				if (!listaRecargada || idDoc!=idDeterminacionOLD)
				{
					log.debug("[getGruposAplicacion] Pido los Grupos de Aplicacion");
					log.debug("[getGruposAplicacion] detTrabajo!=idDeterminacionOLD: "+idDoc+"/"+idDeterminacionOLD);
					listaDeterminaciones = servicioBasicoDeterminaciones.obtenerDeterminacionesDocumento(idDoc);
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
				log.debug("[getGruposAplicacion] detTrabajo=0");
				// No se ha seleccionado todavia ninguna determinacion
				listaDeterminaciones = new ArrayList<DeterminacionDTO>();
			}
		}
		
		return listaDeterminaciones;
	}
	
	
	public String getNombreColumnName() {
		return nombreColumnName;
	}
    
	
}

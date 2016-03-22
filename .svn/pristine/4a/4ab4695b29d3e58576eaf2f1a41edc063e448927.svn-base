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

import com.mitc.redes.editorfip.entidades.interfazgrafico.EntidadDTO;
import com.mitc.redes.editorfip.servicios.basicos.fip.entidades.ServicioBasicoEntidades;
import com.mitc.redes.editorfip.servicios.genericos.SortableList;
import com.mitc.redes.editorfip.servicios.sesion.VariablesSesionGenerales;
import com.mitc.redes.editorfip.utilidades.Constantes;

@Stateless
@Name("listaEntidadesDocumento")
public class ListaEntidadesDocumentoBean extends SortableList implements ListaEntidadesDocumento
{
   
	@Logger private Log log;

    @In(create=true)
    ServicioBasicoEntidades servicioBasicoEntidades;
    
    @In (create = true, required = false)
   	VariablesSesionGenerales variablesSesionGenerales;
    
    private List<EntidadDTO> listaEntidades = null;
    private static final String nombreColumnName = Constantes.NOMBRE_COLUMNA_NOMBRE;
    
    private int idDeterminacionOLD =0;
    
    private boolean listaRecargada = false;
    
	private static final String ordenColumnName = "orden";
    
    public ListaEntidadesDocumentoBean() {
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
            	EntidadDTO d1 = (EntidadDTO) o1;
            	EntidadDTO d2 = (EntidadDTO) o2;
                if (sortColumnName == null) {
                    return 0;
                }
                if (sortColumnName.equals(nombreColumnName)) {
                	return ascending ? comparador.compare(d1.getNombre(), d2.getNombre()) : comparador.compare(d2.getNombre(), d1.getNombre());
                } else {
                        return 0;
                }
            }
        };
        Collections.sort(listaEntidades, comparator);
        log.debug("[sort] Fin");

	}
    
    
	public List<EntidadDTO> getEntidadesDocumento() {
		
		int idDoc = variablesSesionGenerales.getIdDocumentoSeleccionado();
		
		if(listaEntidades == null)
			listaRecargada = false;
		
		if (idDoc!=0)
		{
			log.debug("[getDeterminaciones] idDoc="+idDoc);
			
			if (!listaRecargada || idDoc!=idDeterminacionOLD)
			{
				log.debug("[getGruposAplicacion] Pido los Grupos de Aplicacion");
				log.debug("[getGruposAplicacion] detTrabajo!=idDeterminacionOLD: "+idDoc+"/"+idDeterminacionOLD);
				listaEntidades = servicioBasicoEntidades.obtenerEntidadesDocumento(idDoc);
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
			listaEntidades = new ArrayList<EntidadDTO>();
		}
		
	
		
		
		return listaEntidades;
	}
	
	
	public String getNombreColumnName() {
		return nombreColumnName;
	}
    
	
}

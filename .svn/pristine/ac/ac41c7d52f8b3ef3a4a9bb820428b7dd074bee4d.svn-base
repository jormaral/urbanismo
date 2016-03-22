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

import com.mitc.redes.editorfip.entidades.comunes.ParIdentificadorTexto;
import com.mitc.redes.editorfip.servicios.basicos.fip.determinaciones.ServicioBasicoDeterminaciones;
import com.mitc.redes.editorfip.servicios.genericos.SortableList;

@Stateless
@Name("listaEntidadesDondeSeAplicaDeterminacion")
public class ListaEntidadesDondeSeAplicaDeterminacionBean extends SortableList implements
ListaEntidadesDondeSeAplicaDeterminacion {

	@Logger
	private Log log;

	@In(create = true)
	ServicioBasicoDeterminaciones servicioBasicoDeterminaciones;

	@In
	GestionDeterminaciones gestionDeterminaciones;

	List<ParIdentificadorTexto> listaEntidadesDondeAplicaDet = new ArrayList<ParIdentificadorTexto>();
		
	
	private static final String nombreColumnName = "nombre";
	private Integer idDeterminacionOLD=0;
	


	public ListaEntidadesDondeSeAplicaDeterminacionBean() {
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
            	ParIdentificadorTexto d1 = (ParIdentificadorTexto) o1;
            	ParIdentificadorTexto d2 = (ParIdentificadorTexto) o2;
                if (sortColumnName == null) {
                    return 0;
                }
                if (sortColumnName.equals(nombreColumnName)) {
                	return ascending ? comparador.compare(d1.getTexto(), d2.getTexto()) : comparador.compare(d2.getTexto(), d1.getTexto());
                } else {
                    return 0;
                }
            }
        };
        Collections.sort(listaEntidadesDondeAplicaDet, comparator);
        log.debug("[sort] Fin");

	}

	
	@Override
	public String getNombreColumnName() {		
		return nombreColumnName;
	}

	@Override
	public List<ParIdentificadorTexto> getListaEntidadesDondeAplicaDet() {
		int detTrabajo = gestionDeterminaciones.getIdDeterminacion();
		if (detTrabajo!=0)
		{
			log.debug("[listaEntidadesDondeAplicaDet] detTrabajo="+detTrabajo);
			
			if (detTrabajo!=idDeterminacionOLD)
			{
				
				log.debug("[listaEntidadesDondeAplicaDet] detTrabajo!=idDeterminacionOLD: "+detTrabajo+"/"+idDeterminacionOLD);
				log.debug("[listaEntidadesDondeAplicaDet] Pido obtenerEntidadesDondeSeAplicaDeterminacion");
				listaEntidadesDondeAplicaDet = servicioBasicoDeterminaciones.obtenerEntidadesDondeSeAplicaDeterminacion(detTrabajo);
				
				idDeterminacionOLD = detTrabajo;
			}
			
			
			if (!oldSort.equals(sortColumnName) || oldAscending != ascending) {
		          sort();
		          oldSort = sortColumnName;
		          oldAscending = ascending;
		    }
		}
		
		
		
		return listaEntidadesDondeAplicaDet;
	}
	
	

}

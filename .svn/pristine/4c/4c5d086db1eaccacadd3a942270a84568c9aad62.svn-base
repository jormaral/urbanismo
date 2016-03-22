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

package com.mitc.redes.editorfip.servicios.produccionfip.gestionunidades;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.ejb.Stateless;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.faces.FacesManager;
import org.jboss.seam.log.Log;

import com.mitc.redes.editorfip.entidades.interfazgrafico.UnidadDTO;
import com.mitc.redes.editorfip.servicios.basicos.fip.unidades.ServicioBasicoUnidades;
import com.mitc.redes.editorfip.servicios.genericos.SortableList;
import com.mitc.redes.editorfip.servicios.produccionfip.gestiondiccionariodeterminaciones.GestionArbolDeterminaciones;
import com.mitc.redes.editorfip.servicios.sesion.VariablesSesionUsuario;

@Stateless
@Name("listaUnidades")
public class ListaUnidadesBean extends SortableList implements ListaUnidades {

	@Logger private Log log;
	@In(create = true, required = false) ServicioBasicoUnidades servicioBasicoUnidades;
	@In(create = true, required = false) GestionArbolDeterminaciones gestionArbolDeterminaciones;
	@In(create = true, required = false) VariablesSesionUsuario variablesSesionUsuario;


	private List<UnidadDTO> unidadesList = new ArrayList<UnidadDTO>();
	private static final String nombreColumnName = "nombreDeterminacion";
	private static final String abrevColumnName = "abreviatura";
	private int idTramiteTrabajoOLD;
	private boolean listaRecargada = false;



	public ListaUnidadesBean() {
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
				UnidadDTO d1 = (UnidadDTO) o1;
				UnidadDTO d2 = (UnidadDTO) o2;
				if (sortColumnName == null) {
					return 0;
				}
				if (sortColumnName.equals(nombreColumnName)) {
					return ascending ? comparador.compare(d1.getNombreDeterminacion(), d2.getNombreDeterminacion()) : comparador.compare(d2.getNombreDeterminacion(), d1.getNombreDeterminacion());
				} else {
					if (sortColumnName.equals(abrevColumnName)) {
						return ascending ? comparador.compare(d1.getAbreviatura(), d2.getAbreviatura()) : comparador.compare(d2.getAbreviatura(), d1.getAbreviatura());
					} else {
						return 0;
					}
				}
			}
		};
		Collections.sort(unidadesList, comparator);
		log.debug("[sort] Fin");

	}

	public String getNombrecolumnname() {
		return nombreColumnName;
	}

	public String getAbrevcolumnname() {
		return abrevColumnName;
	}

	public void refrescarLista() {

		int idTramiteEncargado = variablesSesionUsuario.getIdTramiteEncargadoTrabajo();
		log.info("[refrescarLista] idTramiteEncargado="+idTramiteEncargado);

		if (idTramiteEncargado==0){
			unidadesList = new ArrayList<UnidadDTO>();
		} else {


			unidadesList = servicioBasicoUnidades.obtenerListaUnidadesTramite(idTramiteEncargado);
			listaRecargada=true;
		}

		log.info("[refrescarLista] Longitud operacionEntidadList:" + unidadesList.size());
	}


	public List<UnidadDTO> getUnidadesList() {

		int idTramiteTrabajo = variablesSesionUsuario.getIdTramiteTrabajoActual();

		if(unidadesList == null)
			listaRecargada = false;

		if (idTramiteTrabajo!=0)
		{
			log.debug("[getUnidadesList] idTramiteTrabajo="+idTramiteTrabajo);

			if (!listaRecargada || idTramiteTrabajo!=idTramiteTrabajoOLD)
			{
				log.debug("[getUnidadesList] Pido los Grupos de Aplicacion");
				log.debug("[getUnidadesList] idTramiteTrabajo!=idTramiteTrabajoOLD: "+idTramiteTrabajo+"/"+idTramiteTrabajoOLD);
				unidadesList = servicioBasicoUnidades.obtenerListaUnidadesTramite(idTramiteTrabajo);
				idTramiteTrabajoOLD =idTramiteTrabajo;
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
			log.debug("[getUnidadesList] idTramiteTrabajo=0");
			// No se ha seleccionado todavia ninguna TramiteTrabajo
			unidadesList = new ArrayList<UnidadDTO>();
		}

		return unidadesList;
	}

	public void verDetalleUnidad(int idDeterminacion) {
		gestionArbolDeterminaciones.cargarYExpandirDeterminacion(idDeterminacion);
		FacesManager.instance().redirect("/produccionfip/gestiondiccionariodeterminaciones/VerDeterminacionPlanEncargado.xhtml");
	}

}

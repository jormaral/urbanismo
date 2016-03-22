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

package com.mitc.redes.editorfip.servicios.basicos.fip.planes;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Remove;

import com.mitc.redes.editorfip.entidades.interfazgrafico.BusquedaGenericaDTO;
import com.mitc.redes.editorfip.entidades.rpm.gestionfip.PlanesEncargados;

/**
 * Servicio CRUD(Create-Read-Update-Delete) de Planes Encargados 
 * 
 * @author fguerrero
 *
 */
@Local
public interface ServicioCRUDPLanesEncargados {

	/**
	 * Metodo de busqueda de planes encargados que permite autocompletar
	 * un campo de texto de busqueda.
	 * 
	 * @param word palabra 'filtro' del campo de texto.
	 * @return lista de objetos de tipo PlanesEncargados.
	 */
	public List<PlanesEncargados> autocomplete(String word);
	
	/**
	 * Metodo de busqueda de planes encargados que permite autocompletar
	 * un campo de texto de busqueda aplicando filtros especificos.
	 * 
	 * @param word palabra 'filtro' del campo de texto.
	 * @return lista de objetos de tipo BusquedaGenericaDTO.
	 */
	 public List<BusquedaGenericaDTO> autocompleteFiltros(String word);
	
	@Remove
    public void destroy();
}

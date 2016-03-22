/*
* Copyright 2011 red.es
* Autores: IDOM Consulting
*
* Licencia con arreglo a la EUPL, Versiï¿½n 1.1 o -en cuanto
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


package com.mitc.redes.editorfip.servicios.administracion.gestionusuario;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Remove;

import com.mitc.redes.editorfip.entidades.interfazgrafico.BusquedaGenericaDTO;
import com.mitc.redes.editorfip.entidades.rpm.administracionusuarios.Usuario;

/**
 * Este servicio se encarga de la gestión deoperaciones basicas de E/S de datos de usuario: 
 * Create(Creación), Read(Lectura), Update(Modificación) y Delete(Eliminación).
 * 
 * @author fguerrero
 *
 */
@Local
public interface ServicioCRUDGestionUsuario {
	
	/**
	 * Metodo que devuelve una lista de usuarios la cual ha sido filtrada por todos
	 * sus campos en base a una palabra pasada como parametro. Este método se utiliza
	 * para autocompletar un campo de entrada de texto en la pantalla.
	 * 
	 * @param word palabra por la cual se va a filtrar la lista
	 * @return lista de usuarios filtrada.
	 *
	 */
	public List<Usuario> autocomplete(String word);
	
	/**
	 * Metodo que devuelve una lista de objetos de tipo BusquedaGenericaDTO la cual ha sido filtrada por todos
	 * sus campos en base a una palabra pasada como parametro. Este método se utiliza
	 * para autocompletar un campo de entrada de texto en la pantalla.
	 * 
	 * @param word palabra por la cual se va a filtrar la lista
	 * @return lista filtrada con objetos del tipo BusquedaGenericaDTO.
	 *
	 */
	 public List<BusquedaGenericaDTO> autocompleteFiltros(String word);
	
	@Remove
   public void destroy();
	

}

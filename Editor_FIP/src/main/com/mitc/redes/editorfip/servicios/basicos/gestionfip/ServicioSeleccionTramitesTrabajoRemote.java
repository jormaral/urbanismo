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

package com.mitc.redes.editorfip.servicios.basicos.gestionfip;

import java.util.List;

import javax.ejb.Remote;

import com.mitc.redes.editorfip.entidades.comunes.ParIdentificadorTexto;

/**
 * Servicio que controla las operaciones de seleccion de trámites. (Interfaz Remota)
 * 
 * @author fguerrero
 *
 */
@Remote
public interface ServicioSeleccionTramitesTrabajoRemote
{
	/**
	 * Obtiene la lista de tramites raices para el arbol de asignación
	 * de trámites. 
	 * 
	 * @param username Identificador de usuario para cual se quiere obtener los datos del arbol.
	 * @return lista de objetos tipo ParIdentificadorTexto.
	 */
	public List<ParIdentificadorTexto> getArbolTramitesEncargadosRaicesAsignadoPorUsuario(String username);
	
	/**
	 * Obtiene la lista de tramites raices para el arbol de asignación
	 * de trámites. 
	 * 
	 * @param username Identificador de usuario para cual se quiere obtener los datos del arbol.
	 * @param idAmbito Id del trámite padre.
	 * @return lista de objetos tipo ParIdentificadorTexto.
	 */
	public List<ParIdentificadorTexto> getArbolTramitesEncargadosHijosAsignadoPorUsuario(String username, int idAmbito);

}

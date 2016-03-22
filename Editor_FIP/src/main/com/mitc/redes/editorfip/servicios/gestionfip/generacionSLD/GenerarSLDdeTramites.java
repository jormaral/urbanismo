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

package com.mitc.redes.editorfip.servicios.gestionfip.generacionSLD;

import javax.ejb.Local;

/**
 * Servicio encargado de generar SLD's en base a trámites.
 * 
 * @author bccs1
 *
 */
@Local
public interface GenerarSLDdeTramites {
	
	/**
	 * Recorre todos los planes encargados existentes y 
	 * generar sus SLD's
	 * 
	 */
	public void generarPlanesEncargados();
	
	/**
	 * 
	 */
	public void generarSLDTodosTramites();
	
	/**
	 * Genera todos los SLDs dado un tramite y ambito partiendo de las
	 * plantillas
	 * 
	 * @param idtramite ID del tramite del cual se va a generar el SLD
	 * @param idambito ID del ámbito del cual se va a generar el SLD
	 */
	public void generarSLD(Integer idtramite, Integer idambito);
	
}

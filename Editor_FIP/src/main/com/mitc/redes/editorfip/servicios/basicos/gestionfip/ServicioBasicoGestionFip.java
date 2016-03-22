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

import javax.ejb.Local;

/**
 * Servicio basico de operaciones de gestion FIP
 * 
 * @author fguerrero
 *
 */
@Local
public interface ServicioBasicoGestionFip
{
   /**
    * Obtiene el ID del tramite base asociado a un trámite encargado.
    * 
    * @param idTramiteEncargado ID del tramite encargado asociado al tramite base.
    * @return ID del tramite base en formato Integer.
    */
	public int obtenerIdTramiteBaseDeIdTramiteEncargado (int idTramiteEncargado);
	
	/**
    * Obtiene el ID del tramite vigente asociado a un trámite encargado.
    * 
    * @param idTramiteEncargado ID del tramite encargado asociado al tramite base.
    * @return ID del tramite vigente en formato Integer.
    */
	public int obtenerIdTramiteVigenteDeIdTramiteEncargado (int idTramiteEncargado);
	
}

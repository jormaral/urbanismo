/*
* Copyright 2011 red.es
* Autores: Arnaiz Consultores.
*
** Licencia con arreglo a la EUPL, Versi√≥n 1.1 o -en cuanto
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
package com.mitc.redes.editorfip.servicios.gestionfip.gestionprerefundido;

import javax.ejb.Local;

/**
 * Servicio que se encarga de la exportacion de FIP's.
 * 
 * @author fguerrero
 *
 */
@Local
public interface ClsExportacion {
	
	/**
	 * MÈtodo que se encarga de exportar un FIP en base a su tr·mite.
	 * 
	 * @param idTramite ID del tr·mite actual.
	 * @return ID del FIP exportado.
	 */
	public String exportarFIP(int idTramite);
}


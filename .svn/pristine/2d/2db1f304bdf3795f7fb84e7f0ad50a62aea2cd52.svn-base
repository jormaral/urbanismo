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

package com.mitc.redes.editorfip.servicios.gestionfip.generacionfip;

import javax.ejb.Local;

/**
 * EJB que permite la generacion de FIP's.
 * 
 * @author fguerrero
 *
 */
@Local
public interface GenerarFipSincrono
{

	
	/**
	 * Método que genera FIP's apartir de una lista de tramites.
	 * 
	 * @param seleccionados lista de trámites en formato ArbolGenericoObject.
	 */
	public void generarFipSincrono(int idTramite);
	
	public void borrarFIP2(int idFIP2);
	
	public void generarFIP2ZIP(int idFIP2);
}
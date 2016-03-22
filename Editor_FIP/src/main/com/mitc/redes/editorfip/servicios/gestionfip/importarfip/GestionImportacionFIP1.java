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

package com.mitc.redes.editorfip.servicios.gestionfip.importarfip;

import javax.ejb.Local;
import javax.ejb.Remove;

import org.jboss.seam.annotations.async.Asynchronous;
@Local
public interface GestionImportacionFIP1 
{
	
	
	
	@Remove
	public void remove();
	
	
	public boolean comprobarExistePlanEncargado (Long idFipCargado);
	
	public int idPlanBaseImportarFIP(Long idFipCargado);
	
	
	@Asynchronous
	public void importacionFIP(Long idFipCargado)  ;
	
	public void reiniciarValores();
	
	
}
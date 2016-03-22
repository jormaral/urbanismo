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

package com.mitc.redes.editorfip.servicios.gestionfip.obtencionfip;

import java.util.EventObject;

import javax.ejb.Local;
import javax.ejb.Remove;
import javax.faces.event.ActionEvent;

import com.icesoft.faces.component.inputfile.FileInfo;
@Local
public interface CargadorArchivoZIP
{
	/**
	 * Obtiene el valor de la propiedad ''
	 * 
	 * @return valor de tipo FileInfo
	 */
	public FileInfo getCurrentFile();
	
	public void uploadFIPZIPActionListener(ActionEvent actionEvent);
	
	/**
	 * Obtiene el valor de la propiedad ''
	 * 
	 * @return valor de tipo int
	 */
	public int getFileProgress();

	public void progressListenerFIPZIP(EventObject event);
	
	@Remove
	public void destroy();
}
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

package com.mitc.redes.editorfip.servicios.sesion;

import javax.ejb.Remove;

public interface VariablesSesionGenerales {
	
	

	/* (non-Javadoc)
	 * @see com.mitc.redes.editorfip.servicios.VariablesSesionUsuario#destroy()
	 */
	@Remove
	public void destroy();
	
	/**
	 * Método que devuelve el identificado del documento seleccionado
	 * @return
	 */
	public int getIdDocumentoSeleccionado();
	
	/**
	 * Establece el idenficador del documento seleccionado en este momento
	 * @param idDocumentoSeleccionado Identificado del documento seleccionado
	 */
	public void setIdDocumentoSeleccionado(int idDocumentoSeleccionado);
	
	public int getIdDocumentoShpSeleccionado();
	
	public void setIdDocumentoShpSeleccionado(int idDocumentoShpSeleccionado);
	
	public String getGeometriaWktSeleccionada();
	
	public void setGeometriaWktSeleccionada(String geometriaWktSeleccionada);
}
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

package com.mitc.redes.editorfip.servicios.gestionfip.validaciones;

import java.util.List;

import javax.ejb.Local;

import com.mitc.redes.editorfip.entidades.rpm.validaciones.ProcesoValidacion;
import com.mitc.redes.editorfip.servicios.basicos.comunes.plantillas.ListaGenerica;

@Local
public interface ListaProcesosValidacion  {

	/**
	 * Modifica el valor de la propiedad '@param listaProcesos'
	 * 
	 * @param listaProcesos valor de tipo 
	 */
	public void setListaProcesos(List<ProcesoValidacion> listaProcesos);

	/**
	 * Obtiene el valor de la propiedad ''
	 * 
	 * @return valor de tipo List<ProcesoValidacion>
	 */
	public List<ProcesoValidacion> getListaProcesos();
	
	public void verElementosVal(int idProceso);
	
	public void validarTramite();
	
	public void refrescarLista();
	
	public void validarTramiteSincrono(int idTramite);
	
	public void  borrarProcVal(int idProceso);
}

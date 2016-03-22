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
import javax.faces.event.ValueChangeEvent;

import com.mitc.redes.editorfip.entidades.rpm.validaciones.ElementoValidado;

@Local
public interface ListaElementosValidados {

	public void filtrarLista(ValueChangeEvent vce);
	
	/**
	 * Modifica el valor de la propiedad '@param listaElementos'
	 * 
	 * @param listaElementos valor de tipo 
	 */
	public void setListaElementos(List<ElementoValidado> listaElementos);
	/**
	 * Obtiene el valor de la propiedad ''
	 * 
	 * @return valor de tipo List<ElementoValidado>
	 */
	public List<ElementoValidado> getListaElementos();
	
	/**
	 * Modifica el valor de la propiedad '@param idProceso'
	 * 
	 * @param idProceso valor de tipo 
	 */
	public void setIdProceso(int idProceso);
	/**
	 * Obtiene el valor de la propiedad ''
	 * 
	 * @return valor de tipo int
	 */
	public int getIdProceso();
	
	/**
	 * Obtiene el valor de la propiedad ''
	 * 
	 * @return valor de tipo String
	 */
	public String getFiltroVal();
	/**
	 * Modifica el valor de la propiedad '@param filtroVal'
	 * 
	 * @param filtroVal valor de tipo 
	 */
	public void setFiltroVal(String filtroVal);
	
	public void destroy();
}

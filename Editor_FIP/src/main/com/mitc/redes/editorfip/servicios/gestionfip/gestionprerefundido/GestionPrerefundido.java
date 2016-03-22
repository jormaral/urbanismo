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

package com.mitc.redes.editorfip.servicios.gestionfip.gestionprerefundido;

import javax.ejb.Local;

/**
 * Servicio de gestion de Pre-refundido.
 * 
 * @author fguerrero
 *
 */
@Local
public interface GestionPrerefundido
{
    
	/**
	 * M�todo que consol�da un pre-refundido.
	 * 
	 * @param idPrerefundido ID del pre-refundido a consolidar.
	 */
	public void consolidarPrerefundido(int idPrerefundido);
	
	/**
	 * M�todo que elimina un pre-refundido.
	 * 
	 * @param idPrerefundido ID del pre-refundido a eliminar.
	 */
	public void borrarPrerefundido(int idPrerefundido);
	 
	/**
	 * M�todo que guarda un pre-refundido.
	 * 
	 */
	public void guardarPrerefundido();
	
	/**
	 * Obtiene el valor de la propiedad 'idTipoOperacionPlan'
	 * 
	 * @return valor de tipo int
	 */
	public int getIdTipoOperacionPlan();
	
	/**
	 * M�todo que modifica la propiedad 'idTipoOperacionPlan'
	 * 
	 * @param idTipoOperacionPlan ID del nuevo valor de la propiedad 'idTipoOperacionPlan'
	 */
	public void setIdTipoOperacionPlan(int idTipoOperacionPlan);
	
	public void destroy();
	

}

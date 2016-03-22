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

package com.mitc.redes.editorfip.servicios.gestionfip.generacionfip;

import java.util.HashMap;
import java.util.List;

import javax.ejb.Local;

import org.jboss.seam.annotations.Destroy;

import com.mitc.redes.editorfip.entidades.rpm.gestionfip.FipsGenerados;
import com.mitc.redes.editorfip.servicios.basicos.comunes.plantillas.ListaGenerica;

/**
 * Servicio que gestiona el listado de FIP's generados.
 * 
 * @author fguerrero
 *
 */
@Local
public interface ListadoFipGenerado 
{
	/**
	 * Devuelve la lista actual de objetos seleccionados.
	 * 
	 * @return lista en formato HashMap.
	 */
	public HashMap getSeleccion();
	
	/**
	 * Modifica la lista actual de objetos seleccionados.
	 * 
	 * @param seleccion lista en formato HashMap.
	 */
	public void setSeleccion(HashMap seleccion);
	
	/**
	 * Obtiene la lista completa de FIP's generados.
	 * 
	 * @return lista de objetos de tipo FipsGenerados.
	 */
	public List<FipsGenerados> getListado();
	
	@Destroy
	public void destroy();
	
	public void refrescarLista();
	
	/**
	 * 
	 * Obtiene el ID del actual objeto seleccionado.
	 * @return
	 */
	public String getIdSeleccionado();
	
	/**
	 * Obtiene el valor de la propiedad que guarda los datos del 
	 * FIP generado.
	 * 
	 * @return objeto de tipo FipsGenerados
	 */
	public FipsGenerados getFipGenerado();
	
	/**
	 * Modifica el valor de la propiedad que guarda los datos del 
	 * FIP generado.
	 * 
	 * @param fipGenerado
	 */
	public void setFipGenerado(FipsGenerados fipGenerado);
	
	/**
	 * Obtiene el valor de la propiedad 'Identificador'
	 * 
	 * @return ID en formato Long.
	 */
	public Long getIdentificador();
	
	/**
	 * Modifica el valor de la propiedad 'Identificador'
	 * 
	 * @param identificador ID en formato Long.
	 */
	public void setIdentificador(Long identificador);
	
	public void mostrarInfo();
	
}
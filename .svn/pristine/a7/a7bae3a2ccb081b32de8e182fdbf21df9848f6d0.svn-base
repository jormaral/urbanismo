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

import java.util.HashMap;
import java.util.List;

import javax.ejb.Local;
import javax.ejb.Remove;

import org.jboss.seam.annotations.async.Asynchronous;

import com.mitc.redes.editorfip.entidades.rpm.gestionfip.FipsCargados;
import com.mitc.redes.editorfip.servicios.basicos.comunes.plantillas.ListaGenerica;
@Local
public interface ListadoFips extends ListaGenerica
{
	
	public HashMap getSeleccion();
	
	public List<FipsCargados> getListado();

	
	public String getIdSeleccionado();
	public void darDebaja();
	
	@Remove
	public void remove();
	
	//@Destroy
	//public void destroy();
	
	public void borrarRegistroFIPCargado(int idFipCargadoBorrar);
	
	
	public String getUsuarioBloqueante();
	public boolean isEstaBloqueado();
	public void desbloquearConsolidado();
	public void bloquearConsolidado();
	
	public boolean isPuedeBloquear();
	public boolean isPuedeDesbloquear();
	public boolean isPuedeConsolidar();
	public void desbloquearServicio();
	
	public boolean isMostrarPanelFip();
	public FipsCargados getFipCargado();
	public void cargarFipSeleccionado();
	
	/**
	 * Modifica el valor de la propiedad '@param mostrarPanelFip'
	 * 
	 * @param mostrarPanelFip valor de tipo 
	 */
	public void setMostrarPanelFip(boolean mostrarPanelFip);
	
	public void importarFipSeleccionado();
	
	public boolean isMostrarPanelBloquear();
	/**
	 * Modifica el valor de la propiedad '@param mostrarPanelBloquear'
	 * 
	 * @param mostrarPanelBloquear valor de tipo 
	 */
	public void setMostrarPanelBloquear(boolean mostrarPanelBloquear);
	
	public boolean isMostrarPanelInformacion();
	/**
	 * Modifica el valor de la propiedad '@param mostrarPanelInformacion'
	 * 
	 * @param mostrarPanelInformacion valor de tipo 
	 */
	public void setMostrarPanelInformacion(boolean mostrarPanelInformacion);
	
	/**
	 * Modifica el valor de la propiedad '@param mostrarBotonesImportar'
	 * 
	 * @param mostrarBotonesImportar valor de tipo 
	 */
	public void setMostrarBotonesImportar(boolean mostrarBotonesImportar);
	public boolean isMostrarBotonesImportar();
	
	public void mostrarInfo();
}
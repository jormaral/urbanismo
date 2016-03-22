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

package com.mitc.redes.editorfip.servicios.ayuda.faq;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.mitc.redes.editorfip.entidades.rpm.ayuda.Faq;

@Local
public interface GestionFaq {

	/**
	 * Obtiene la lista completa de FAQ's
	 * @return listado de FAQ's
	 */
	List<Faq> obtenerListadoFaq ();
	
	/**
	 * Devuelve el resultado de la busqueda de FAQ's
	 * 
	 * @return listado de FAQ's
	 */
	public ArrayList<Faq> getResultado();
	
	/**
	 * Modifica el resultado de la busqueda de FAQ's
	 * 
	 * @param resultado listado de FAQ's
	 */
	public void setResultado(ArrayList<Faq> resultado); 
	
	/**
	 * Guarda los datos de una FAQ en el sistema y actualiza
	 * la lista de FAQ's.
	 */
	public void guardarFaq();
	
	/**
	 * Elimina una FAQ de la lista y refleja dicho cambio en BD.
	 * 
	 * @param idFaq ID de la FAQ a eliminar.
	 */
	public void eliminarFaq(Long idFaq);
	
	/**
	 * Modifica la propiedad que indica si se muestra el panel de 
	 * pop-up que permite añadir nuevas FAQ's.
	 * 
	 * @param mostrarPanelNuevaFaq
	 */
	public void setMostrarPanelNuevaFaq(boolean mostrarPanelNuevaFaq);
	
	/**
	 * Obtiene la propiedad que indica si se muestra el panel de 
	 * pop-up que permite añadir nuevas FAQ's.
	 * 
	 * @return Propiedad en formato boolean
	 */
	public boolean isMostrarPanelNuevaFaq();
	
	/**
	 * Obtiene el objeto que almacena la nueva FAQ.
	 * 
	 * @return
	 */
	public Faq getNuevaFaq();
	
	/**
	 * Modifica el objeto que almacena la nueva FAQ.
	 * 
	 * @param nuevaFaq
	 */
	public void setNuevaFaq(Faq nuevaFaq);

	public void destroy();

}

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

package com.mitc.redes.editorfip.servicios.gestionfip.gestionprerefundido;

import java.util.List;

import javax.ejb.Local;

import com.mitc.redes.editorfip.entidades.rpm.gestionfip.Prerefundido;

/**
 * Servicio que gestiona los prerefundidos
 * 
 * @author fguerrero
 *
 */
@Local
public interface ServicioBasicoPrerefundido
{
	/**
	 * Método que se encarga de modificar la propiedad de estado de un prerefundido a 'true'.
	 * 
	 * @param idPrerefundido ID del prerefundido a modificar.
	 * @return Objeto de tipo boolean que nos indica si la operacion se ha realizado de forma correcta.
	 */
	public boolean ponerConsolidadoATrue(int idPrerefundido);
	
	/**
	 * Método que obtiene un prerefundido mediante su ID.
	 * 
	 * @param idPrerefundido ID del prerefundido a obtener.
	 * @return Objeto de tipo Prerefundido asociado a dicho ID.
	 */
	public Prerefundido obtenerPrerefundido(int idPrerefundido);
	
	/**
	 * Obtiene la lista completa de Prerefundidos.
	 * 
	 * @return Lista con objetos del tipo Prerefundido.
	 */
	public List<Prerefundido> obtenerListaPrerefundido();
	
	/**
	 * Obtiene la lista completa de Prerefundidos asociados a un plan encargado.
	 * 
	 * @param idPlanEncargado ID del plan encargado asociado a los prerefundidos.
	 * @return Lista con objetos del tipo Prerefundido.
	 */
	public List<Prerefundido> obtenerListaInfoPrerefundido(int idPlanEncargado);
	
	/**
	 * Método que se encarga de crear un prerefundido.
	 * 
	 * @param idTramitePrerefundido ID del trámite del prerefundido.
	 * @param idTramiteEncargado ID del plan encargado.
	 * @param idTramiteVigente ID del plan vigente.
	 * @param idTipoOperacion Indicador del tipo de operacion.
	 * @param rutaFichero Ruta en la cual se guardará el fichero generado por el prerefundido.
	 * @return ID del prerefundido creado.
	 */
	public int crearPrerefundido(int idTramitePrerefundido, int idTramiteEncargado,int idTramiteVigente, int idTipoOperacion, String rutaFichero);
	
	/**
	 * Método que se encarga de realizar las operaciones previas a un prerefundido.
	 * 
	 * @param idTramiteEncargado ID del plan encargado.
	 * @param idTramiteVigente ID del plan vigente.
	 * @param idTipoOperacion Indicador del tipo de operacion.
	 * @return Indicador de operacion.
	 */
	public int operacionesPlanPreviaPrerefundido(int idTramiteEncargado,int idTramiteVigente,int idTramiteBase, int idTipoOperacion);
	
	/**
	 * Obtiene la siguiente version de un prerefundido en base a su trámite.
	 * 
	 * @param idTramite ID del tramite.
	 * @return valor de tipo Integer.
	 */
	public int obtenerSiguienteVersion (int idTramite);
}

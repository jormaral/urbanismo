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

package com.mitc.redes.editorfip.servicios.gestionfip.importadores;
import java.util.List;

import javax.ejb.Local;
import javax.ejb.Remove;

import com.mitc.redes.editorfip.entidades.interfazgrafico.CUExcelDTO;

/**
 * Servicio que se encarga de importar condiciones urbanísticas a partír de un fichero tipo Excel.
 * 
 * @author fguerrero
 *
 */
@Local
public interface ImportadorCUExcel {
	
	/**
	 * Método que se encarga de leer los datos de un fichero tipo EXCEL en 
	 * donde se encuentran las condiciones urbanísticas a importar, para luego
	 * persistir los datos en BD.
	 * 
	 * @param idTramite ID del trámite actual.
	 * @param listaCuenExcel lista de CU a importar.
	 * @return lista de IDS de las CU importadas.
	 */
	public List<String> persistirCUdeExcel(int idTramite,
			List<CUExcelDTO> listaCuenExcel);
	
	/**
	 * Método que se encarga de leer los datos de un fichero tipo EXCEL en 
	 * donde se encuentran las condiciones urbanísticas a importar, para luego
	 * persistir los datos en BD.
	 * 
	 * @param idTramite ID del trámite actual.
	 * @param listaCuenExcel lista de CU a importar.
	 * @return lista de IDS de las CU importadas.
	 */
	public List<String> persistirCUdeExcelRus(int idTramite,
			List<CUExcelDTO> listaCuenExcel);
	
	/**
	 * Metodo que busca y devuelve el ID de una entidad en base al trámite y su clave.
	 * 
	 * @param idTramite ID del trámite actual.
	 * @param claveEntidad clave asociada a la entidad.
	 * @return ID de la entidad encontrada.
	 */
	public int buscarEntidadPorTramiteYClave(int idTramite, String claveEntidad);
	
	/**
	 * Metodo que busca y devuelve el ID de una determinación en base al trámite y su apartado.
	 * 
	 * @param idTramite ID del trámite actual.
	 * @param apartado apartado asociado a la determinación.
	 * @return ID de la determinación encontrada.
	 */
	public int buscarDeterminacionPorTramiteYApartado(int idTramite,
			String apartado);
	
	@Remove
	public void destroy();
}

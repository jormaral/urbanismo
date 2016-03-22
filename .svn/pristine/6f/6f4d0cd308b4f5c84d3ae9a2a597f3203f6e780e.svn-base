package com.mitc.redes.editorfip.servicios.gestionfip.exportadores;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Remove;

import com.mitc.redes.editorfip.entidades.interfazgrafico.CUExcelDTO;

/**
 * Servicio de exportación de condiciones urbanísticas. 
 * 
 * @author fguerrero
 *
 */
@Local
public interface ServicioExportacionCU {
	
	
	@Remove
	public void destroy();
	
	/**
	 * Obtiene el listado de condiciones urbanísticas en formato EXCEL
	 * 
	 * @return listado de objetos de tipo CUExcelDTO
	 */
	public List<CUExcelDTO> getListadoExcelDTO ();
	
}
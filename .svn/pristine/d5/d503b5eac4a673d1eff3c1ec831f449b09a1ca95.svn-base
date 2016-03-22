package com.mitc.redes.editorfip.servicios.gestionfip.exportadores;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Remove;

import com.mitc.redes.editorfip.entidades.interfazgrafico.EntidadExcelDTO;

/**
 * Servicio de exportación de entidades. 
 * 
 * @author fguerrero
 *
 */
@Local
public interface ServicioExportacionEntidad {
	
	
	@Remove
	public void destroy();
	
	/**
	 * Obtiene el listado de entidades en formato EXCEL
	 * 
	 * @return listado de objetos de tipo EntidadExcelDTO
	 */
	public List<EntidadExcelDTO> getListadoExcelDTO ();
	
}
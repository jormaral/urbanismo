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

package com.mitc.redes.editorfip.servicios.gestionfip.gestiondocumental;

import java.util.List;

import javax.ejb.Local;

import com.mitc.redes.editorfip.entidades.interfazgrafico.DeterminacionDTO;
import com.mitc.redes.editorfip.servicios.genericos.SortableListIF;

/**
 * 
 * Servicio que se encarga de las operaciones referentes a la asignacion 
 * de determinaciones a documentos.
 * 
 * @author fguerero
 *
 */
@Local
public interface ListaDeterminacionesDocumento extends SortableListIF
{
    /**
     * Obtiene la lista completa de determinaciones asociadas al
     * un documento, obteniendo el ID del documento actual de trabajo
     * del bean VariablesSesionGenerales.
     * 
     * @return lista de objetos de tipo DeterminacionDTO
     */
	public List<DeterminacionDTO> getDeterminacionesDocumento();
	
	/**
	 * Obtiene la actual columna de ordenacion.
	 * 
	 * @return nombre de la columna en formato String.
	 */
	public String getNombreColumnName();
	
	public void sort();
	
	/**
	 * Comprueba si la lista está ordenada por una columna en concreto.
	 * 
	 * @param sortColumn nombre de la columna.
	 * @return resultado de la comprobación.
	 */
	public boolean isDefaultAscending(String sortColumn);
	
	
}

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

package com.mitc.redes.editorfip.servicios.produccionfip.gestiondiccionariodeterminaciones;

import java.util.List;

import javax.ejb.Local;
import javax.faces.event.ActionEvent;

import com.mitc.redes.editorfip.entidades.interfazgrafico.ValorReferenciaTramiteDTO;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Determinacion;
import com.mitc.redes.editorfip.servicios.genericos.SortableListIF;

@Local
public interface ListaValoresReferencia extends SortableListIF
{
	
	/**
	 * Funcionalidad para asociar los hijos como valor de referencia
	 * @param ae
	 */
	public void guardarHijosComoValorReferencia(ActionEvent ae);
	
	
	/**
	 * 
	 * @return
	 */
	public int tamanoValoresReferencia();
	
    
	public void setListaRecargada(boolean listaRecargada);
	/**
	 * Obtiene el valor de la propiedad ''
	 * 
	 * @return valor de tipo Determinacion[]
	 */
	public List<ValorReferenciaTramiteDTO> getValoresReferencia();
	
	/**
	 * Obtiene el valor de la propiedad ''
	 * 
	 * @return valor de tipo String
	 */
	public String getNombreColumnName();
	
	public String getOrdenColumnName() ;
	
	public void sort();
	
	/**
	 * Obtiene el valor de la propiedad ''
	 * 
	 * @return valor de tipo boolean
	 */
	public boolean isDefaultAscending(String sortColumn);
	
	public String borrarValorReferenciaDeterminacion(int idValorReferencia);

	public void anadirValoresReferencia(List<ValorReferenciaTramiteDTO> valores, String nuevaPag);

}
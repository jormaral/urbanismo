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

import com.mitc.redes.editorfip.entidades.interfazgrafico.DeterminacionReguladoraTramiteDTO;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Determinacion;
import com.mitc.redes.editorfip.servicios.genericos.SortableListIF;

@Local
public interface ListaDeterminacionesReguladoras extends SortableListIF
{
    
	/**
	 * Obtiene el valor de la propiedad ''
	 * 
	 * @return valor de tipo Determinacion[]
	 */
	public Determinacion[] getDeterminacionesReguladoras();
	
	/**
	 * Obtiene el valor de la propiedad ''
	 * 
	 * @return valor de tipo String
	 */
	public String getNombreColumnName();
	
	public void sort();
	
	/**
	 * Obtiene el valor de la propiedad ''
	 * 
	 * @return valor de tipo boolean
	 */
	public boolean isDefaultAscending(String sortColumn);
	
	public String borrarDeterminacionReguladoraDeterminacion(int idDetReg);
	
	public void anadirDeterminacionesReguladoras(List<DeterminacionReguladoraTramiteDTO> determinacionesReg, String nuevaPag);
	

}

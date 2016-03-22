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

package com.mitc.redes.editorfip.servicios.genericos;

public interface SortableListIF {

	/**
	 * Gets the sortColumnName column.
	 *
	 * @return column to sortColumnName
	 */
	public String getSortColumnName();

	/**
	 * Sets the sortColumnName column
	 *
	 * @param sortColumnName column to sortColumnName
	 */
	public void setSortColumnName(String sortColumnName);

	/**
	 * Is the sortColumnName ascending.
	 *
	 * @return true if the ascending sortColumnName otherwise false.
	 */
	public boolean isAscending();

	/**
	 * Set sortColumnName type.
	 *
	 * @param ascending true for ascending sortColumnName, false for desending sortColumnName.
	 */
	public void setAscending(boolean ascending);

}
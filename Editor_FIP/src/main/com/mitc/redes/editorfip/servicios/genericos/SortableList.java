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

public abstract class SortableList implements SortableListIF {

    protected String sortColumnName;
    protected boolean ascending;

    // we only want to resort if the oder or column has changed.
    protected String oldSort;
    protected boolean oldAscending;


    protected SortableList(String defaultSortColumn) {
        sortColumnName = defaultSortColumn;
        ascending = isDefaultAscending(defaultSortColumn);
        oldSort = sortColumnName;
        // make sure sortColumnName on first render
        oldAscending = !ascending;
    }

    /**
     * Sort the list.
     */
    protected abstract void sort();

    /**
     * Is the default sortColumnName direction for the given column "ascending" ?
     */
    protected abstract boolean isDefaultAscending(String sortColumn);

    /* (non-Javadoc)
	 * @see com.mitc.redes.editorfip.servicios.genericos.SortableListIF#getSortColumnName()
	 */
    public String getSortColumnName() {
        return sortColumnName;
    }

    /* (non-Javadoc)
	 * @see com.mitc.redes.editorfip.servicios.genericos.SortableListIF#setSortColumnName(java.lang.String)
	 */
    public void setSortColumnName(String sortColumnName) {
        oldSort = this.sortColumnName;
        this.sortColumnName = sortColumnName;

    }

    /* (non-Javadoc)
	 * @see com.mitc.redes.editorfip.servicios.genericos.SortableListIF#isAscending()
	 */
    public boolean isAscending() {
        return ascending;
    }

    /* (non-Javadoc)
	 * @see com.mitc.redes.editorfip.servicios.genericos.SortableListIF#setAscending(boolean)
	 */
    public void setAscending(boolean ascending) {
        oldAscending = this.ascending;
        this.ascending = ascending;
    }
}

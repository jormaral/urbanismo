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



import javax.swing.tree.DefaultMutableTreeNode;

import com.icesoft.faces.component.tree.IceUserObject;
import com.mitc.redes.editorfip.entidades.comunes.ParIdentificadorTexto;

public class ArbolGenericoObject extends IceUserObject {

    private ParIdentificadorTexto datosIdTextoArbolGenerico;
    boolean seleccionado;

    public ArbolGenericoObject(DefaultMutableTreeNode defaultMutableTreeNode) {
        super(defaultMutableTreeNode);
        
        setLeafIcon("tree_document.gif");//Icono ultimo nivel
        setBranchContractedIcon("tree_folder_closed.gif");//Icono carpeta cerrada tree_folder_closed.gif
        setBranchExpandedIcon("tree_folder_open.gif");//Icono carpeta abiertat
        setExpanded(true);
    }

	public ParIdentificadorTexto getDatosIdTextoArbolGenerico() {
		return datosIdTextoArbolGenerico;
	}

	public void setDatosIdTextoArbolGenerico(
			ParIdentificadorTexto datosIdTextoArbolGenerico) {
		this.datosIdTextoArbolGenerico = datosIdTextoArbolGenerico;
	}

	public boolean isSeleccionado() {
		return seleccionado;
	}

	public void setSeleccionado(boolean seleccionado) {
		this.seleccionado = seleccionado;
	}

    
    
    
}

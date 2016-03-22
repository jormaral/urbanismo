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

import javax.ejb.Local;
import javax.ejb.Remove;
import javax.faces.event.ActionEvent;
import javax.swing.tree.DefaultTreeModel;

import com.mitc.redes.editorfip.servicios.genericos.GestionArbolGenericoIF;

@Local
public interface GestionArbolRegulacionesEspecificas extends GestionArbolGenericoIF
{
	
	public void refrescar(ActionEvent e);
	
	public void menuEvent(ActionEvent event);

	public void nodeSelected(ActionEvent event);
	
	public void nodeSelected(String nodoId);

	public void cargarArbolInicial();
	
	public DefaultTreeModel getModel();

	public void setModel(DefaultTreeModel model);
	

	@Remove
	public void destroy();

}

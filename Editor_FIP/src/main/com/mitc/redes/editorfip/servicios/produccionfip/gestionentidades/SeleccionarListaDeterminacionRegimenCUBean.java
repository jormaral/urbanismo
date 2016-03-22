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

package com.mitc.redes.editorfip.servicios.produccionfip.gestionentidades;

import javax.ejb.Remove;
import javax.ejb.Stateful;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;

import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Determinacion;
import com.mitc.redes.editorfip.servicios.basicos.fip.determinaciones.ServicioBasicoDeterminaciones;

@Stateful
@Scope(ScopeType.SESSION)
@Name("seleccionarListaDeterminacionRegimenCU")
public class SeleccionarListaDeterminacionRegimenCUBean  implements SeleccionarListaDeterminacionRegimenCU {

	@Logger
	private Log log;
 
	@In(create = true, required = false) 
	ServicioBasicoDeterminaciones servicioBasicoDeterminaciones;
	
	private boolean mostrar = false;
	private int idGASel = 0;
	
	private Determinacion seleccionado = new Determinacion();
	
	
	public boolean isMostrar() {
		return mostrar;
	}
	public void setMostrar(boolean mostrar) {
		this.mostrar = mostrar;
	}
	public int getIdGASel() {
		int idGASelReturn = idGASel;
		reiniciar();
		log.debug("[getIdGASel] idGASelReturn="+idGASelReturn);
		return idGASelReturn;
	}
	public void setIdGASel(int idGASel) {
		this.idGASel = idGASel;
		seleccionado = servicioBasicoDeterminaciones.buscarDeterminacion(idGASel);
	}
	
	
	
	
	public Determinacion getSeleccionado() {
		return seleccionado;
	}
	@Remove
	public void destroy() {
		log.debug("[destroy]");
	}
	
	
	public void reiniciar() {
		 mostrar = false;
		 idGASel = 0;
		 seleccionado = new Determinacion();
		
	}
	


}

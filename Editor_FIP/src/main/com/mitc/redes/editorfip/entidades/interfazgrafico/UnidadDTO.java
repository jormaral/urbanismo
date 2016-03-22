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

package com.mitc.redes.editorfip.entidades.interfazgrafico;

public class UnidadDTO {

	private int idDeterminacion;
	private String nombreDeterminacion;
	private String abreviatura;	
	private String definicion;
	
	public UnidadDTO() {

	}

	public int getIdDeterminacion() {
		return idDeterminacion;
	}

	public void setIdDeterminacion(int idDeterminacion) {
		this.idDeterminacion = idDeterminacion;
	}

	public String getNombreDeterminacion() {
		return nombreDeterminacion;
	}

	public void setNombreDeterminacion(String nombreDeterminacion) {
		this.nombreDeterminacion = nombreDeterminacion;
	}

	public String getAbreviatura() {
		return abreviatura;
	}

	public void setAbreviatura(String abreviatura) {
		this.abreviatura = abreviatura;
	}

	public String getDefinicion() {
		return definicion;
	}

	public void setDefinicion(String definicion) {
		this.definicion = definicion;
	}
	
	

	
	
	

}

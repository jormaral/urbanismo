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

public class EntidadBusquedaDTO {
	
	private int iden;
	private String nombre;
	private String nombreDeterminacion;
	private String clave;
	private String nombreCompleto;
	
	public EntidadBusquedaDTO(){
		
	}
	
	public EntidadBusquedaDTO(int iden, String nombre,
			String nombreDeterminacion, String clave, String nombreCompleto) {
		super();
		this.iden = iden;
		this.nombre = nombre;
		this.nombreDeterminacion = nombreDeterminacion;
		this.clave = clave;
		this.nombreCompleto = nombreCompleto;
	}

	public int getIden() {
		return iden;
	}

	public void setIden(int iden) {
		this.iden = iden;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getNombreDeterminacion() {
		return nombreDeterminacion;
	}

	public void setNombreDeterminacion(String nombreDeterminacion) {
		this.nombreDeterminacion = nombreDeterminacion;
	}

	public String getClave() {
		return clave;
	}

	public void setClave(String clave) {
		this.clave = clave;
	}

	public String getNombreCompleto() {
		return nombreCompleto;
	}

	public void setNombreCompleto(String nombreCompleto) {
		this.nombreCompleto = nombreCompleto;
	}
		
}

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

/**
* Clase usada para almacenar los datos a mostrar en la interfaz
*
* @author Aitor
*/
public class OperacionEntidadDTO {

	private int iden;
	private int orden;
	private String tipoOperacionEntidad;
	private String entidadOperadora;
	private String tramiteOperado;
	private String entidadOperada;
	//TODO Adscripciones
	
	public OperacionEntidadDTO() {
		super();
	}
	public OperacionEntidadDTO(int iden, int orden,
			String tipoOperacionEntidad, String entidadOperadora,
			String tramiteOperado, String entidadOperada) {
		super();
		this.iden = iden;
		this.orden = orden;
		this.tipoOperacionEntidad = tipoOperacionEntidad;
		this.entidadOperadora = entidadOperadora;
		this.tramiteOperado = tramiteOperado;
		this.entidadOperada = entidadOperada;
	}
	public int getIden() {
		return iden;
	}
	public void setIden(int iden) {
		this.iden = iden;
	}
	public int getOrden() {
		return orden;
	}
	public void setOrden(int orden) {
		this.orden = orden;
	}
	public String getTipoOperacionEntidad() {
		return tipoOperacionEntidad;
	}
	public void setTipoOperacionEntidad(String tipoOperacionEntidad) {
		this.tipoOperacionEntidad = tipoOperacionEntidad;
	}
	public String getEntidadOperadora() {
		return entidadOperadora;
	}
	public void setEntidadOperadora(String entidadOperadora) {
		this.entidadOperadora = entidadOperadora;
	}
	public String getTramiteOperado() {
		return tramiteOperado;
	}
	public void setTramiteOperado(String tramiteOperado) {
		this.tramiteOperado = tramiteOperado;
	}
	public String getEntidadOperada() {
		return entidadOperada;
	}
	public void setEntidadOperada(String entidadOperada) {
		this.entidadOperada = entidadOperada;
	}
	
}

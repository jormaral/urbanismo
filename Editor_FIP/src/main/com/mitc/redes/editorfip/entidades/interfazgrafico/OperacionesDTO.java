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
public class OperacionesDTO {

	private int iden;
	private int orden;
	private String operacionEntDet;
	private String tipoOperacion;
	private String entidadOperadora;
	private String tramiteOperado;
	private String entidadOperada;
	
	
	public OperacionesDTO() {
		super();
	}


	
	public OperacionesDTO(int iden, int orden, String operacionEntDet,
			String tipoOperacion, String entidadOperadora,
			String tramiteOperado, String entidadOperada) {
		super();
		this.iden = iden;
		this.orden = orden;
		this.operacionEntDet = operacionEntDet;
		this.tipoOperacion = tipoOperacion;
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


	public String getOperacionEntDet() {
		return operacionEntDet;
	}


	public void setOperacionEntDet(String operacionEntDet) {
		this.operacionEntDet = operacionEntDet;
	}


	public String getTipoOperacion() {
		return tipoOperacion;
	}


	public void setTipoOperacion(String tipoOperacion) {
		this.tipoOperacion = tipoOperacion;
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

package com.mitc.redes.editorfip.entidades.interfazgrafico;

import java.io.Serializable;

public class GeometriaDTO implements Serializable {
	
	private String identificador;
	private String clave;
	private String geometriaWKT;
	
	
	
	
	public GeometriaDTO() {
		super();
		
	}




	public String getIdentificador() {
		return identificador;
	}




	public void setIdentificador(String identificador) {
		this.identificador = identificador;
	}




	public String getClave() {
		return clave;
	}




	public void setClave(String clave) {
		this.clave = clave;
	}




	public String getGeometriaWKT() {
		return geometriaWKT;
	}




	public void setGeometriaWKT(String geometriaWKT) {
		this.geometriaWKT = geometriaWKT;
	}
	
		



}

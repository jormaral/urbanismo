package com.mitc.redes.editorfip.servicios.mapas;

import java.io.Serializable;


public class EntidadMapaDTO implements Serializable{
	
	public int identidadColor;
	public int identidad;
	public String nombre;
	public String clave;
	public String codigo;
	public String color;
	
	private boolean seleccionada = false;
	
	public EntidadMapaDTO() {
		
	}
	
	public int getIdentidadColor() {
		return identidadColor;
	}

	public void setIdentidadColor(int identidadColor) {
		this.identidadColor = identidadColor;
	}

	

	public int getIdentidad() {
		return identidad;
	}

	public void setIdentidad(int identidad) {
		this.identidad = identidad;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getClave() {
		return clave;
	}

	public void setClave(String clave) {
		this.clave = clave;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}
	
	
	
}


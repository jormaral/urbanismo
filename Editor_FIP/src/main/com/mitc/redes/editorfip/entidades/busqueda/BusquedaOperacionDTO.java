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

package com.mitc.redes.editorfip.entidades.busqueda;

import java.io.Serializable;

/**
 *
 * @author Sancho
 */
public class BusquedaOperacionDTO implements Serializable{
	

	private int id;
	private String clase;
	private String tipo;
	private String nombreOperadora;
	
	private String nombreOperada;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getClase() {
		return clase;
	}

	public void setClase(String clase) {
		this.clase = clase;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getNombreOperadora() {
		return nombreOperadora;
	}

	public void setNombreOperadora(String nombreOperadora) {
		this.nombreOperadora = nombreOperadora;
	}

	public String getNombreOperada() {
		return nombreOperada;
	}

	public void setNombreOperada(String nombreOperada) {
		this.nombreOperada = nombreOperada;
	}
	
	
	
	
}
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

package com.mitc.redes.editorfip.entidades.rpm.gestionfip;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.jboss.seam.annotations.Name;

import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Entidad;

@Entity
@Name("coloresEntidades")
@Table(name="coloresentidades", schema = "gestionfip")
public class ColoresEntidades implements Serializable
{
	int id;
	int identidad;
	String color;	
	
	public ColoresEntidades() {
		super();
		// TODO Auto-generated constructor stub
	}


	public ColoresEntidades(int id, int identidad, String color) {
		super();
		this.id = id;
		this.identidad = identidad;
		this.color = color;
	}
	
	@Id
	@Column(name = "id", unique = true, nullable = false)
	@GeneratedValue(strategy=GenerationType.SEQUENCE)
	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}

	
	public int getIdentidad() {
		return identidad;
	}


	public void setIdentidad(int identidad) {
		this.identidad = identidad;
	}


	public String getColor() {
		return color;
	}


	public void setColor(String color) {
		this.color = color;
	}	
	
	
	
}


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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Columns;
import org.jboss.seam.annotations.Name;

@Entity
@Name("coordenadasAmbito")
@Table(name="coordenadasambito", schema = "gestionfip")
@SequenceGenerator( name = "DETSEQ", sequenceName = "gestionfip.gestionfip_coordenadasambito_id_seq",allocationSize=1 )
public class CoordenadasAmbito implements Serializable
{
	private Long id;
	int idAmbito;
	double coorxmin;
	double coorxmax;
	double coorymin;
	double coorymax;
	String proyeccion;
	
	
	
	
	
	public CoordenadasAmbito() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	
	
	




	public CoordenadasAmbito(Long id, int idAmbito, double coorxmin,
			double coorxmax, double coorymin, double coorymax) {
		super();
		this.id = id;
		this.idAmbito = idAmbito;
		this.coorxmin = coorxmin;
		this.coorxmax = coorxmax;
		this.coorymin = coorymin;
		this.coorymax = coorymax;
	}









	@Id
	@Column(name = "id", unique = true, nullable = false, insertable = false)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="DETSEQ")
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public int getIdAmbito() {
		return idAmbito;
	}
	public void setIdAmbito(int idAmbito) {
		this.idAmbito = idAmbito;
	}
	public double getCoorxmin() {
		return coorxmin;
	}









	public void setCoorxmin(double coorxmin) {
		this.coorxmin = coorxmin;
	}









	public double getCoorxmax() {
		return coorxmax;
	}









	public void setCoorxmax(double coorxmax) {
		this.coorxmax = coorxmax;
	}









	public double getCoorymin() {
		return coorymin;
	}









	public void setCoorymin(double coorymin) {
		this.coorymin = coorymin;
	}









	public double getCoorymax() {
		return coorymax;
	}









	public void setCoorymax(double coorymax) {
		this.coorymax = coorymax;
	}








	@Column(name = "proyeccion")
	public String getProyeccion() {
		return proyeccion;
	}









	public void setProyeccion(String proyeccion) {
		this.proyeccion = proyeccion;
	}


	


	
	
}
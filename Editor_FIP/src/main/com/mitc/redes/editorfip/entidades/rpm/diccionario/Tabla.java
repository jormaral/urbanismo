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

package com.mitc.redes.editorfip.entidades.rpm.diccionario;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;

@Entity
@Table(name = "tabla", schema = "diccionario")
public class Tabla implements java.io.Serializable {

	private int iden;
	private String nombre;
	private String clausulawhere;
	private String observaciones;
	private String esquema;
	private Set<Defvector> defvectors = new HashSet<Defvector>(0);
	private Set<Defvector> defvectors_1 = new HashSet<Defvector>(0);

	public Tabla() {
	}

	public Tabla(int iden, String nombre, String clausulawhere,
			String observaciones) {
		this.iden = iden;
		this.nombre = nombre;
		this.clausulawhere = clausulawhere;
		this.observaciones = observaciones;
	}

	public Tabla(int iden, String nombre, String clausulawhere,
			String observaciones, String esquema, Set<Defvector> defvectors,
			Set<Defvector> defvectors_1) {
		this.iden = iden;
		this.nombre = nombre;
		this.clausulawhere = clausulawhere;
		this.observaciones = observaciones;
		this.esquema = esquema;
		this.defvectors = defvectors;
		this.defvectors_1 = defvectors_1;
	}

	@Id
	@Column(name = "iden", unique = true, nullable = false)
	public int getIden() {
		return this.iden;
	}

	public void setIden(int iden) {
		this.iden = iden;
	}

	@Column(name = "nombre", nullable = false, length = 32)
	@NotNull
	@Length(max = 32)
	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	@Column(name = "clausulawhere", nullable = false, length = 2048)
	@NotNull
	@Length(max = 2048)
	public String getClausulawhere() {
		return this.clausulawhere;
	}

	public void setClausulawhere(String clausulawhere) {
		this.clausulawhere = clausulawhere;
	}

	@Column(name = "observaciones", nullable = false)
	@NotNull
	public String getObservaciones() {
		return this.observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	@Column(name = "esquema", length = 32)
	@Length(max = 32)
	public String getEsquema() {
		return this.esquema;
	}

	public void setEsquema(String esquema) {
		this.esquema = esquema;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "tabla")
	public Set<Defvector> getDefvectors() {
		return this.defvectors;
	}

	public void setDefvectors(Set<Defvector> defvectors) {
		this.defvectors = defvectors;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "tabla")
	public Set<Defvector> getDefvectors_1() {
		return this.defvectors_1;
	}

	public void setDefvectors_1(Set<Defvector> defvectors_1) {
		this.defvectors_1 = defvectors_1;
	}

}

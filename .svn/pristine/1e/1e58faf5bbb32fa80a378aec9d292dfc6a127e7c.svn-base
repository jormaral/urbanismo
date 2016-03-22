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

package com.mitc.redes.editorfip.entidades.rpm.planeamiento;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.validator.NotNull;

@Entity
@Table(name = "opciondeterminacion", schema = "planeamiento")
@SequenceGenerator( name = "DETSEQ", sequenceName = "planeamiento.planeamiento_opciondeterminacion_iden_seq",allocationSize=1 )
public class Opciondeterminacion implements java.io.Serializable {

	private int iden;
	private Determinacion determinacionByIddeterminacion;
	private Determinacion determinacionByIddeterminacionvalorref;
	private Set<Entidaddeterminacionregimen> entidaddeterminacionregimens = new HashSet<Entidaddeterminacionregimen>(
			0);
	private Set<Entidaddeterminacionregimen> entidaddeterminacionregimens_1 = new HashSet<Entidaddeterminacionregimen>(
			0);

	public Opciondeterminacion() {
	}

	public Opciondeterminacion(int iden,
			Determinacion determinacionByIddeterminacion,
			Determinacion determinacionByIddeterminacionvalorref) {
		this.iden = iden;
		this.determinacionByIddeterminacion = determinacionByIddeterminacion;
		this.determinacionByIddeterminacionvalorref = determinacionByIddeterminacionvalorref;
	}

	public Opciondeterminacion(int iden,
			Determinacion determinacionByIddeterminacion,
			Determinacion determinacionByIddeterminacionvalorref,
			Set<Entidaddeterminacionregimen> entidaddeterminacionregimens,
			Set<Entidaddeterminacionregimen> entidaddeterminacionregimens_1) {
		this.iden = iden;
		this.determinacionByIddeterminacion = determinacionByIddeterminacion;
		this.determinacionByIddeterminacionvalorref = determinacionByIddeterminacionvalorref;
		this.entidaddeterminacionregimens = entidaddeterminacionregimens;
		this.entidaddeterminacionregimens_1 = entidaddeterminacionregimens_1;
	}

	@Id
	@Column(name = "iden", unique = true, nullable = false, insertable = false)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="DETSEQ")
	public int getIden() {
		return this.iden;
	}

	public void setIden(int iden) {
		this.iden = iden;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "iddeterminacion", nullable = false)
	@NotNull
	public Determinacion getDeterminacionByIddeterminacion() {
		return this.determinacionByIddeterminacion;
	}

	public void setDeterminacionByIddeterminacion(
			Determinacion determinacionByIddeterminacion) {
		this.determinacionByIddeterminacion = determinacionByIddeterminacion;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "iddeterminacionvalorref", nullable = false)
	@NotNull
	public Determinacion getDeterminacionByIddeterminacionvalorref() {
		return this.determinacionByIddeterminacionvalorref;
	}

	public void setDeterminacionByIddeterminacionvalorref(
			Determinacion determinacionByIddeterminacionvalorref) {
		this.determinacionByIddeterminacionvalorref = determinacionByIddeterminacionvalorref;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "opciondeterminacion")
	public Set<Entidaddeterminacionregimen> getEntidaddeterminacionregimens() {
		return this.entidaddeterminacionregimens;
	}

	public void setEntidaddeterminacionregimens(
			Set<Entidaddeterminacionregimen> entidaddeterminacionregimens) {
		this.entidaddeterminacionregimens = entidaddeterminacionregimens;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "opciondeterminacion")
	public Set<Entidaddeterminacionregimen> getEntidaddeterminacionregimens_1() {
		return this.entidaddeterminacionregimens_1;
	}

	public void setEntidaddeterminacionregimens_1(
			Set<Entidaddeterminacionregimen> entidaddeterminacionregimens_1) {
		this.entidaddeterminacionregimens_1 = entidaddeterminacionregimens_1;
	}

}

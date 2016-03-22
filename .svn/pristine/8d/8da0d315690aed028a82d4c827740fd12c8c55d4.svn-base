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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;

@Entity
@Table(name = "instrumentoplan", schema = "diccionario")
public class Instrumentoplan implements java.io.Serializable {

	private int iden;
	private Naturaleza naturaleza;
	private Literal literal;
	private String nemo;
	private Set<Instrumentotipooperacionplan> instrumentotipooperacionplans = new HashSet<Instrumentotipooperacionplan>(
			0);
	private Set<Instrumentotipooperacionplan> instrumentotipooperacionplans_1 = new HashSet<Instrumentotipooperacionplan>(
			0);

	public Instrumentoplan() {
	}

	public Instrumentoplan(int iden, String nemo) {
		this.iden = iden;
		this.nemo = nemo;
	}

	public Instrumentoplan(int iden, Naturaleza naturaleza, Literal literal,
			String nemo,
			Set<Instrumentotipooperacionplan> instrumentotipooperacionplans,
			Set<Instrumentotipooperacionplan> instrumentotipooperacionplans_1) {
		this.iden = iden;
		this.naturaleza = naturaleza;
		this.literal = literal;
		this.nemo = nemo;
		this.instrumentotipooperacionplans = instrumentotipooperacionplans;
		this.instrumentotipooperacionplans_1 = instrumentotipooperacionplans_1;
	}

	@Id
	@Column(name = "iden", unique = true, nullable = false)
	public int getIden() {
		return this.iden;
	}

	public void setIden(int iden) {
		this.iden = iden;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idnaturaleza")
	public Naturaleza getNaturaleza() {
		return this.naturaleza;
	}

	public void setNaturaleza(Naturaleza naturaleza) {
		this.naturaleza = naturaleza;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idliteral")
	@Cascade( { org.hibernate.annotations.CascadeType.ALL})
	public Literal getLiteral() {
		return this.literal;
	}

	public void setLiteral(Literal literal) {
		this.literal = literal;
	}

	@Column(name = "nemo", nullable = false, length = 12)
	@NotNull
	@Length(max = 12)
	public String getNemo() {
		return this.nemo;
	}

	public void setNemo(String nemo) {
		this.nemo = nemo;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "instrumentoplan")
	public Set<Instrumentotipooperacionplan> getInstrumentotipooperacionplans() {
		return this.instrumentotipooperacionplans;
	}

	public void setInstrumentotipooperacionplans(
			Set<Instrumentotipooperacionplan> instrumentotipooperacionplans) {
		this.instrumentotipooperacionplans = instrumentotipooperacionplans;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "instrumentoplan")
	public Set<Instrumentotipooperacionplan> getInstrumentotipooperacionplans_1() {
		return this.instrumentotipooperacionplans_1;
	}

	public void setInstrumentotipooperacionplans_1(
			Set<Instrumentotipooperacionplan> instrumentotipooperacionplans_1) {
		this.instrumentotipooperacionplans_1 = instrumentotipooperacionplans_1;
	}

}

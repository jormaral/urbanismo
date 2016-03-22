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

import org.hibernate.validator.NotNull;

@Entity
@Table(name = "naturaleza", schema = "diccionario")
public class Naturaleza implements java.io.Serializable {

	private int iden;
	private Literal literal;
	private Set<Instrumentoplan> instrumentoplans = new HashSet<Instrumentoplan>(
			0);
	private Set<Instrumentoplan> instrumentoplans_1 = new HashSet<Instrumentoplan>(
			0);

	public Naturaleza() {
	}

	public Naturaleza(int iden, Literal literal) {
		this.iden = iden;
		this.literal = literal;
	}

	public Naturaleza(int iden, Literal literal,
			Set<Instrumentoplan> instrumentoplans,
			Set<Instrumentoplan> instrumentoplans_1) {
		this.iden = iden;
		this.literal = literal;
		this.instrumentoplans = instrumentoplans;
		this.instrumentoplans_1 = instrumentoplans_1;
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
	@JoinColumn(name = "idliteral", nullable = false)
	@NotNull
	public Literal getLiteral() {
		return this.literal;
	}

	public void setLiteral(Literal literal) {
		this.literal = literal;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "naturaleza")
	public Set<Instrumentoplan> getInstrumentoplans() {
		return this.instrumentoplans;
	}

	public void setInstrumentoplans(Set<Instrumentoplan> instrumentoplans) {
		this.instrumentoplans = instrumentoplans;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "naturaleza")
	public Set<Instrumentoplan> getInstrumentoplans_1() {
		return this.instrumentoplans_1;
	}

	public void setInstrumentoplans_1(Set<Instrumentoplan> instrumentoplans_1) {
		this.instrumentoplans_1 = instrumentoplans_1;
	}

}

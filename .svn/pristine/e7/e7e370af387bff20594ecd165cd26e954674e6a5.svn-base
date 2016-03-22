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
@Table(name = "ambito", schema = "diccionario")
public class Ambito implements java.io.Serializable {

	private int iden;
	private Literal literal;
	private Tipoambito tipoambito;
	private String codigoine;
	private Set<Organigramaambito> organigramaambitosForIdambitohijo = new HashSet<Organigramaambito>(
			0);
	private Set<Organigramaambito> organigramaambitosForIdambitopadre = new HashSet<Organigramaambito>(
			0);
	private Set<Organigramaambito> organigramaambitosForIdambitopadre_1 = new HashSet<Organigramaambito>(
			0);
	private Set<Organo> organos = new HashSet<Organo>(0);
	private Set<Organo> organos_1 = new HashSet<Organo>(0);
	private Set<Organigramaambito> organigramaambitosForIdambitohijo_1 = new HashSet<Organigramaambito>(
			0);

	public Ambito() {
	}

	public Ambito(int iden, Tipoambito tipoambito) {
		this.iden = iden;
		this.tipoambito = tipoambito;
	}

	public Ambito(int iden, Literal literal, Tipoambito tipoambito,
			String codigoine,
			Set<Organigramaambito> organigramaambitosForIdambitohijo,
			Set<Organigramaambito> organigramaambitosForIdambitopadre,
			Set<Organigramaambito> organigramaambitosForIdambitopadre_1,
			Set<Organo> organos, Set<Organo> organos_1,
			Set<Organigramaambito> organigramaambitosForIdambitohijo_1) {
		this.iden = iden;
		this.literal = literal;
		this.tipoambito = tipoambito;
		this.codigoine = codigoine;
		this.organigramaambitosForIdambitohijo = organigramaambitosForIdambitohijo;
		this.organigramaambitosForIdambitopadre = organigramaambitosForIdambitopadre;
		this.organigramaambitosForIdambitopadre_1 = organigramaambitosForIdambitopadre_1;
		this.organos = organos;
		this.organos_1 = organos_1;
		this.organigramaambitosForIdambitohijo_1 = organigramaambitosForIdambitohijo_1;
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
	@JoinColumn(name = "idliteral")
	@Cascade( { org.hibernate.annotations.CascadeType.ALL})
	public Literal getLiteral() {
		return this.literal;
	}
	public void setLiteral(Literal literal) {
		this.literal = literal;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idtipoambito", nullable = false)
	@NotNull
	@Cascade( { org.hibernate.annotations.CascadeType.ALL})
	public Tipoambito getTipoambito() {
		return this.tipoambito;
	}
	public void setTipoambito(Tipoambito tipoambito) {
		this.tipoambito = tipoambito;
	}

	@Column(name = "codigoine", length = 5)
	@Length(max = 5)
	public String getCodigoine() {
		return this.codigoine;
	}

	public void setCodigoine(String codigoine) {
		this.codigoine = codigoine;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "ambitoByIdambitohijo")
	public Set<Organigramaambito> getOrganigramaambitosForIdambitohijo() {
		return this.organigramaambitosForIdambitohijo;
	}

	public void setOrganigramaambitosForIdambitohijo(
			Set<Organigramaambito> organigramaambitosForIdambitohijo) {
		this.organigramaambitosForIdambitohijo = organigramaambitosForIdambitohijo;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "ambitoByIdambitopadre")
	public Set<Organigramaambito> getOrganigramaambitosForIdambitopadre() {
		return this.organigramaambitosForIdambitopadre;
	}

	public void setOrganigramaambitosForIdambitopadre(
			Set<Organigramaambito> organigramaambitosForIdambitopadre) {
		this.organigramaambitosForIdambitopadre = organigramaambitosForIdambitopadre;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "ambitoByIdambitopadre")
	public Set<Organigramaambito> getOrganigramaambitosForIdambitopadre_1() {
		return this.organigramaambitosForIdambitopadre_1;
	}

	public void setOrganigramaambitosForIdambitopadre_1(
			Set<Organigramaambito> organigramaambitosForIdambitopadre_1) {
		this.organigramaambitosForIdambitopadre_1 = organigramaambitosForIdambitopadre_1;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "ambito")
	public Set<Organo> getOrganos() {
		return this.organos;
	}

	public void setOrganos(Set<Organo> organos) {
		this.organos = organos;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "ambito")
	public Set<Organo> getOrganos_1() {
		return this.organos_1;
	}

	public void setOrganos_1(Set<Organo> organos_1) {
		this.organos_1 = organos_1;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "ambitoByIdambitohijo")
	public Set<Organigramaambito> getOrganigramaambitosForIdambitohijo_1() {
		return this.organigramaambitosForIdambitohijo_1;
	}

	public void setOrganigramaambitosForIdambitohijo_1(
			Set<Organigramaambito> organigramaambitosForIdambitohijo_1) {
		this.organigramaambitosForIdambitohijo_1 = organigramaambitosForIdambitohijo_1;
	}
	
}

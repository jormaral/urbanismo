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

@Entity
@Table(name = "caracterdeterminacion", schema = "diccionario")
public class Caracterdeterminacion implements java.io.Serializable {

	private int iden;
	private Literal literal;
	private Integer nmaxaplicaciones;
	private Integer nminaplicaciones;
	private Set<Operacioncaracter> operacioncaractersForIdcaracteroperado = new HashSet<Operacioncaracter>(
			0);
	private Set<Operacioncaracter> operacioncaractersForIdcaracteroperador = new HashSet<Operacioncaracter>(
			0);
	private Set<Operacioncaracter> operacioncaractersForIdcaracteroperado_1 = new HashSet<Operacioncaracter>(
			0);
	private Set<Operacioncaracter> operacioncaractersForIdcaracteroperador_1 = new HashSet<Operacioncaracter>(
			0);

	public Caracterdeterminacion() {
	}

	public Caracterdeterminacion(int iden) {
		this.iden = iden;
	}

	public Caracterdeterminacion(int iden, Literal literal,
			Integer nmaxaplicaciones, Integer nminaplicaciones,
			Set<Operacioncaracter> operacioncaractersForIdcaracteroperado,
			Set<Operacioncaracter> operacioncaractersForIdcaracteroperador,
			Set<Operacioncaracter> operacioncaractersForIdcaracteroperado_1,
			Set<Operacioncaracter> operacioncaractersForIdcaracteroperador_1) {
		this.iden = iden;
		this.literal = literal;
		this.nmaxaplicaciones = nmaxaplicaciones;
		this.nminaplicaciones = nminaplicaciones;
		this.operacioncaractersForIdcaracteroperado = operacioncaractersForIdcaracteroperado;
		this.operacioncaractersForIdcaracteroperador = operacioncaractersForIdcaracteroperador;
		this.operacioncaractersForIdcaracteroperado_1 = operacioncaractersForIdcaracteroperado_1;
		this.operacioncaractersForIdcaracteroperador_1 = operacioncaractersForIdcaracteroperador_1;
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

	@Column(name = "nmaxaplicaciones")
	public Integer getNmaxaplicaciones() {
		return this.nmaxaplicaciones;
	}

	public void setNmaxaplicaciones(Integer nmaxaplicaciones) {
		this.nmaxaplicaciones = nmaxaplicaciones;
	}

	@Column(name = "nminaplicaciones")
	public Integer getNminaplicaciones() {
		return this.nminaplicaciones;
	}

	public void setNminaplicaciones(Integer nminaplicaciones) {
		this.nminaplicaciones = nminaplicaciones;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "caracterdeterminacionByIdcaracteroperado")
	public Set<Operacioncaracter> getOperacioncaractersForIdcaracteroperado() {
		return this.operacioncaractersForIdcaracteroperado;
	}

	public void setOperacioncaractersForIdcaracteroperado(
			Set<Operacioncaracter> operacioncaractersForIdcaracteroperado) {
		this.operacioncaractersForIdcaracteroperado = operacioncaractersForIdcaracteroperado;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "caracterdeterminacionByIdcaracteroperador")
	public Set<Operacioncaracter> getOperacioncaractersForIdcaracteroperador() {
		return this.operacioncaractersForIdcaracteroperador;
	}

	public void setOperacioncaractersForIdcaracteroperador(
			Set<Operacioncaracter> operacioncaractersForIdcaracteroperador) {
		this.operacioncaractersForIdcaracteroperador = operacioncaractersForIdcaracteroperador;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "caracterdeterminacionByIdcaracteroperado")
	public Set<Operacioncaracter> getOperacioncaractersForIdcaracteroperado_1() {
		return this.operacioncaractersForIdcaracteroperado_1;
	}

	public void setOperacioncaractersForIdcaracteroperado_1(
			Set<Operacioncaracter> operacioncaractersForIdcaracteroperado_1) {
		this.operacioncaractersForIdcaracteroperado_1 = operacioncaractersForIdcaracteroperado_1;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "caracterdeterminacionByIdcaracteroperador")
	public Set<Operacioncaracter> getOperacioncaractersForIdcaracteroperador_1() {
		return this.operacioncaractersForIdcaracteroperador_1;
	}

	public void setOperacioncaractersForIdcaracteroperador_1(
			Set<Operacioncaracter> operacioncaractersForIdcaracteroperador_1) {
		this.operacioncaractersForIdcaracteroperador_1 = operacioncaractersForIdcaracteroperador_1;
	}

}

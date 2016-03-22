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
@Table(name = "tipoambito", schema = "diccionario")
public class Tipoambito implements java.io.Serializable {

	private int iden;
	private Literal literal;
	private Integer idpadre;
	private Set<Ambito> ambitos = new HashSet<Ambito>(0);
	private Set<Ambito> ambitos_1 = new HashSet<Ambito>(0);

	public Tipoambito() {
	}

	public Tipoambito(int iden) {
		this.iden = iden;
	}

	public Tipoambito(int iden, Literal literal, Integer idpadre,
			Set<Ambito> ambitos, Set<Ambito> ambitos_1) {
		this.iden = iden;
		this.literal = literal;
		this.idpadre = idpadre;
		this.ambitos = ambitos;
		this.ambitos_1 = ambitos_1;
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

	@Column(name = "idpadre")
	public Integer getIdpadre() {
		return this.idpadre;
	}

	public void setIdpadre(Integer idpadre) {
		this.idpadre = idpadre;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "tipoambito")
	public Set<Ambito> getAmbitos() {
		return this.ambitos;
	}

	public void setAmbitos(Set<Ambito> ambitos) {
		this.ambitos = ambitos;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "tipoambito")
	public Set<Ambito> getAmbitos_1() {
		return this.ambitos_1;
	}

	public void setAmbitos_1(Set<Ambito> ambitos_1) {
		this.ambitos_1 = ambitos_1;
	}

}

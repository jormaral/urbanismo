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
import org.hibernate.validator.NotNull;

@Entity
@Table(name = "defrelacion", schema = "diccionario")
public class Defrelacion implements java.io.Serializable {

	private int iden;
	private Literal literal;
	private int nmininstancias;
	private int nmaxinstancias;
	private Set<Defpropiedad> defpropiedads = new HashSet<Defpropiedad>(0);
	private Set<Defvector> defvectors = new HashSet<Defvector>(0);
	private Set<Defvector> defvectors_1 = new HashSet<Defvector>(0);
	private Set<Defpropiedad> defpropiedads_1 = new HashSet<Defpropiedad>(0);

	public Defrelacion() {
	}

	public Defrelacion(int iden, Literal literal, int nmininstancias,
			int nmaxinstancias) {
		this.iden = iden;
		this.literal = literal;
		this.nmininstancias = nmininstancias;
		this.nmaxinstancias = nmaxinstancias;
	}

	public Defrelacion(int iden, Literal literal, int nmininstancias,
			int nmaxinstancias, Set<Defpropiedad> defpropiedads,
			Set<Defvector> defvectors, Set<Defvector> defvectors_1,
			Set<Defpropiedad> defpropiedads_1) {
		this.iden = iden;
		this.literal = literal;
		this.nmininstancias = nmininstancias;
		this.nmaxinstancias = nmaxinstancias;
		this.defpropiedads = defpropiedads;
		this.defvectors = defvectors;
		this.defvectors_1 = defvectors_1;
		this.defpropiedads_1 = defpropiedads_1;
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
	@Cascade( { org.hibernate.annotations.CascadeType.ALL})
	@NotNull
	public Literal getLiteral() {
		return this.literal;
	}

	public void setLiteral(Literal literal) {
		this.literal = literal;
	}

	@Column(name = "nmininstancias", nullable = false)
	public int getNmininstancias() {
		return this.nmininstancias;
	}

	public void setNmininstancias(int nmininstancias) {
		this.nmininstancias = nmininstancias;
	}

	@Column(name = "nmaxinstancias", nullable = false)
	public int getNmaxinstancias() {
		return this.nmaxinstancias;
	}

	public void setNmaxinstancias(int nmaxinstancias) {
		this.nmaxinstancias = nmaxinstancias;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "defrelacion")
	public Set<Defpropiedad> getDefpropiedads() {
		return this.defpropiedads;
	}

	public void setDefpropiedads(Set<Defpropiedad> defpropiedads) {
		this.defpropiedads = defpropiedads;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "defrelacion")
	public Set<Defvector> getDefvectors() {
		return this.defvectors;
	}

	public void setDefvectors(Set<Defvector> defvectors) {
		this.defvectors = defvectors;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "defrelacion")
	public Set<Defvector> getDefvectors_1() {
		return this.defvectors_1;
	}

	public void setDefvectors_1(Set<Defvector> defvectors_1) {
		this.defvectors_1 = defvectors_1;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "defrelacion")
	public Set<Defpropiedad> getDefpropiedads_1() {
		return this.defpropiedads_1;
	}

	public void setDefpropiedads_1(Set<Defpropiedad> defpropiedads_1) {
		this.defpropiedads_1 = defpropiedads_1;
	}

}

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
@Table(name = "tipooperaciondeterminacion", schema = "diccionario")
public class Tipooperaciondeterminacion implements java.io.Serializable {

	private int iden;
	private Literal literal;
	private Set<Operacioncaracter> operacioncaracters = new HashSet<Operacioncaracter>(
			0);
	private Set<Operacioncaracter> operacioncaracters_1 = new HashSet<Operacioncaracter>(
			0);

	public Tipooperaciondeterminacion() {
	}

	public Tipooperaciondeterminacion(int iden) {
		this.iden = iden;
	}

	public Tipooperaciondeterminacion(int iden, Literal literal,
			Set<Operacioncaracter> operacioncaracters,
			Set<Operacioncaracter> operacioncaracters_1) {
		this.iden = iden;
		this.literal = literal;
		this.operacioncaracters = operacioncaracters;
		this.operacioncaracters_1 = operacioncaracters_1;
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

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "tipooperaciondeterminacion")
	public Set<Operacioncaracter> getOperacioncaracters() {
		return this.operacioncaracters;
	}

	public void setOperacioncaracters(Set<Operacioncaracter> operacioncaracters) {
		this.operacioncaracters = operacioncaracters;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "tipooperaciondeterminacion")
	public Set<Operacioncaracter> getOperacioncaracters_1() {
		return this.operacioncaracters_1;
	}

	public void setOperacioncaracters_1(
			Set<Operacioncaracter> operacioncaracters_1) {
		this.operacioncaracters_1 = operacioncaracters_1;
	}

}

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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.validator.NotNull;

@Entity
@Table(name = "defpropiedad", schema = "diccionario")
public class Defpropiedad implements java.io.Serializable {

	private int iden;
	private Tipodefpropiedad tipodefpropiedad;
	private Literal literal;
	private Defrelacion defrelacion;

	public Defpropiedad() {
	}

	public Defpropiedad(int iden, Tipodefpropiedad tipodefpropiedad,
			Literal literal, Defrelacion defrelacion) {
		this.iden = iden;
		this.tipodefpropiedad = tipodefpropiedad;
		this.literal = literal;
		this.defrelacion = defrelacion;
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
	@JoinColumn(name = "idtipodefpropiedad", nullable = false)
	@NotNull
	public Tipodefpropiedad getTipodefpropiedad() {
		return this.tipodefpropiedad;
	}

	public void setTipodefpropiedad(Tipodefpropiedad tipodefpropiedad) {
		this.tipodefpropiedad = tipodefpropiedad;
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

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "iddefrelacion", nullable = false)
	@NotNull
	public Defrelacion getDefrelacion() {
		return this.defrelacion;
	}

	public void setDefrelacion(Defrelacion defrelacion) {
		this.defrelacion = defrelacion;
	}

}

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

import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;

@Entity
@Table(name = "centroproduccion", schema = "diccionario")
public class Centroproduccion implements java.io.Serializable {

	private int iden;
	private Literal literal;
	private String codigo;
	private String passwordmd5;
	private String correo;

	public Centroproduccion() {
	}

	public Centroproduccion(int iden, String codigo) {
		this.iden = iden;
		this.codigo = codigo;
	}

	public Centroproduccion(int iden, Literal literal, String codigo,
			String passwordmd5, String correo) {
		this.iden = iden;
		this.literal = literal;
		this.codigo = codigo;
		this.passwordmd5 = passwordmd5;
		this.correo = correo;
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
	public Literal getLiteral() {
		return this.literal;
	}

	public void setLiteral(Literal literal) {
		this.literal = literal;
	}

	@Column(name = "codigo", nullable = false, length = 5)
	@NotNull
	@Length(max = 5)
	public String getCodigo() {
		return this.codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	@Column(name = "passwordmd5", length = 32)
	@Length(max = 32)
	public String getPasswordmd5() {
		return this.passwordmd5;
	}

	public void setPasswordmd5(String passwordmd5) {
		this.passwordmd5 = passwordmd5;
	}

	@Column(name = "correo", length = 40)
	@Length(max = 40)
	public String getCorreo() {
		return this.correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

}

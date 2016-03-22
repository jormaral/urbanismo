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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.validator.NotNull;

@Entity
@Table(name = "operacioncaracter", schema = "diccionario")
public class Operacioncaracter implements java.io.Serializable {

	private int iden;
	private Tipooperaciondeterminacion tipooperaciondeterminacion;
	private Caracterdeterminacion caracterdeterminacionByIdcaracteroperado;
	private Caracterdeterminacion caracterdeterminacionByIdcaracteroperador;

	public Operacioncaracter() {
	}

	public Operacioncaracter(int iden,
			Caracterdeterminacion caracterdeterminacionByIdcaracteroperado,
			Caracterdeterminacion caracterdeterminacionByIdcaracteroperador) {
		this.iden = iden;
		this.caracterdeterminacionByIdcaracteroperado = caracterdeterminacionByIdcaracteroperado;
		this.caracterdeterminacionByIdcaracteroperador = caracterdeterminacionByIdcaracteroperador;
	}

	public Operacioncaracter(int iden,
			Tipooperaciondeterminacion tipooperaciondeterminacion,
			Caracterdeterminacion caracterdeterminacionByIdcaracteroperado,
			Caracterdeterminacion caracterdeterminacionByIdcaracteroperador) {
		this.iden = iden;
		this.tipooperaciondeterminacion = tipooperaciondeterminacion;
		this.caracterdeterminacionByIdcaracteroperado = caracterdeterminacionByIdcaracteroperado;
		this.caracterdeterminacionByIdcaracteroperador = caracterdeterminacionByIdcaracteroperador;
	}

	@Id
	@Column(name = "iden", unique = true, nullable = false)
	@GeneratedValue(strategy=GenerationType.SEQUENCE)
	public int getIden() {
		return this.iden;
	}

	public void setIden(int iden) {
		this.iden = iden;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idtipooperaciondet")
	public Tipooperaciondeterminacion getTipooperaciondeterminacion() {
		return this.tipooperaciondeterminacion;
	}

	public void setTipooperaciondeterminacion(
			Tipooperaciondeterminacion tipooperaciondeterminacion) {
		this.tipooperaciondeterminacion = tipooperaciondeterminacion;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idcaracteroperado", nullable = false)
	@NotNull
	public Caracterdeterminacion getCaracterdeterminacionByIdcaracteroperado() {
		return this.caracterdeterminacionByIdcaracteroperado;
	}

	public void setCaracterdeterminacionByIdcaracteroperado(
			Caracterdeterminacion caracterdeterminacionByIdcaracteroperado) {
		this.caracterdeterminacionByIdcaracteroperado = caracterdeterminacionByIdcaracteroperado;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idcaracteroperador", nullable = false)
	@NotNull
	public Caracterdeterminacion getCaracterdeterminacionByIdcaracteroperador() {
		return this.caracterdeterminacionByIdcaracteroperador;
	}

	public void setCaracterdeterminacionByIdcaracteroperador(
			Caracterdeterminacion caracterdeterminacionByIdcaracteroperador) {
		this.caracterdeterminacionByIdcaracteroperador = caracterdeterminacionByIdcaracteroperador;
	}

}

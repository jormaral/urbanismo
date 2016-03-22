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

import org.hibernate.validator.NotNull;

@Entity
@Table(name = "instrumentotipooperacionplan", schema = "diccionario")
public class Instrumentotipooperacionplan implements java.io.Serializable {

	private int iden;
	private Tipooperacionplan tipooperacionplan;
	private Instrumentoplan instrumentoplan;

	public Instrumentotipooperacionplan() {
	}

	public Instrumentotipooperacionplan(int iden,
			Tipooperacionplan tipooperacionplan, Instrumentoplan instrumentoplan) {
		this.iden = iden;
		this.tipooperacionplan = tipooperacionplan;
		this.instrumentoplan = instrumentoplan;
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
	@JoinColumn(name = "idtipooperacionplan", nullable = false)
	@NotNull
	public Tipooperacionplan getTipooperacionplan() {
		return this.tipooperacionplan;
	}

	public void setTipooperacionplan(Tipooperacionplan tipooperacionplan) {
		this.tipooperacionplan = tipooperacionplan;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idinstrumentoplan", nullable = false)
	@NotNull
	public Instrumentoplan getInstrumentoplan() {
		return this.instrumentoplan;
	}

	public void setInstrumentoplan(Instrumentoplan instrumentoplan) {
		this.instrumentoplan = instrumentoplan;
	}

}

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

package com.mitc.redes.editorfip.entidades.rpm.explotacion;

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
@Table(name = "rangovalor", schema = "explotacion")
public class Rangovalor implements java.io.Serializable {

	private int iden;
	private Capa capa;
	private int idplanbase;
	private double valormax;
	private int idsimbologia;
	private String valortematico;

	public Rangovalor() {
	}

	public Rangovalor(int iden, Capa capa, int idplanbase, double valormax,
			int idsimbologia, String valortematico) {
		this.iden = iden;
		this.capa = capa;
		this.idplanbase = idplanbase;
		this.valormax = valormax;
		this.idsimbologia = idsimbologia;
		this.valortematico = valortematico;
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
	@JoinColumn(name = "idcapa", nullable = false)
	@NotNull
	public Capa getCapa() {
		return this.capa;
	}

	public void setCapa(Capa capa) {
		this.capa = capa;
	}

	@Column(name = "idplanbase", nullable = false)
	public int getIdplanbase() {
		return this.idplanbase;
	}

	public void setIdplanbase(int idplanbase) {
		this.idplanbase = idplanbase;
	}

	@Column(name = "valormax", nullable = false, precision = 17, scale = 17)
	public double getValormax() {
		return this.valormax;
	}

	public void setValormax(double valormax) {
		this.valormax = valormax;
	}

	@Column(name = "idsimbologia", nullable = false)
	public int getIdsimbologia() {
		return this.idsimbologia;
	}

	public void setIdsimbologia(int idsimbologia) {
		this.idsimbologia = idsimbologia;
	}

	@Column(name = "valortematico", nullable = false, length = 50)
	@NotNull
	@Length(max = 50)
	public String getValortematico() {
		return this.valortematico;
	}

	public void setValortematico(String valortematico) {
		this.valortematico = valortematico;
	}

}

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

import org.hibernate.validator.NotNull;

@Entity
@Table(name = "leyenda_vt", schema = "explotacion")
public class LeyendaVt implements java.io.Serializable {

	private int iden;
	private Capa capa;
	private int iddeterminacionbase;
	private int ordenVt;

	public LeyendaVt() {
	}

	public LeyendaVt(int iden, Capa capa, int iddeterminacionbase, int ordenVt) {
		this.iden = iden;
		this.capa = capa;
		this.iddeterminacionbase = iddeterminacionbase;
		this.ordenVt = ordenVt;
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

	@Column(name = "iddeterminacionbase", nullable = false)
	public int getIddeterminacionbase() {
		return this.iddeterminacionbase;
	}

	public void setIddeterminacionbase(int iddeterminacionbase) {
		this.iddeterminacionbase = iddeterminacionbase;
	}

	@Column(name = "orden_vt", nullable = false)
	public int getOrdenVt() {
		return this.ordenVt;
	}

	public void setOrdenVt(int ordenVt) {
		this.ordenVt = ordenVt;
	}

}

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
@Table(name = "procedimiento_rg", schema = "explotacion")
public class ProcedimientoRg implements java.io.Serializable {

	private int iden;
	private Capa capa;
	private int idopciondeterminacionbase;
	private Integer ordenRg;

	public ProcedimientoRg() {
	}

	public ProcedimientoRg(int iden, Capa capa, int idopciondeterminacionbase) {
		this.iden = iden;
		this.capa = capa;
		this.idopciondeterminacionbase = idopciondeterminacionbase;
	}

	public ProcedimientoRg(int iden, Capa capa, int idopciondeterminacionbase,
			Integer ordenRg) {
		this.iden = iden;
		this.capa = capa;
		this.idopciondeterminacionbase = idopciondeterminacionbase;
		this.ordenRg = ordenRg;
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

	@Column(name = "idopciondeterminacionbase", nullable = false)
	public int getIdopciondeterminacionbase() {
		return this.idopciondeterminacionbase;
	}

	public void setIdopciondeterminacionbase(int idopciondeterminacionbase) {
		this.idopciondeterminacionbase = idopciondeterminacionbase;
	}

	@Column(name = "orden_rg")
	public Integer getOrdenRg() {
		return this.ordenRg;
	}

	public void setOrdenRg(Integer ordenRg) {
		this.ordenRg = ordenRg;
	}

}

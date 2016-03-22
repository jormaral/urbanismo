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

import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;

@Entity
@Table(name = "capagrupo", schema = "explotacion")
public class Capagrupo implements java.io.Serializable {

	private int iden;
	private Capa capa;
	private int orden;
	private String codigodeterminaciongrupo;
	private Set<ProcedimientoVt> procedimientoVts = new HashSet<ProcedimientoVt>(
			0);
	private Set<ProcedimientoGr> procedimientoGrs = new HashSet<ProcedimientoGr>(
			0);

	public Capagrupo() {
	}

	public Capagrupo(int iden, Capa capa, int orden,
			String codigodeterminaciongrupo) {
		this.iden = iden;
		this.capa = capa;
		this.orden = orden;
		this.codigodeterminaciongrupo = codigodeterminaciongrupo;
	}

	public Capagrupo(int iden, Capa capa, int orden,
			String codigodeterminaciongrupo,
			Set<ProcedimientoVt> procedimientoVts,
			Set<ProcedimientoGr> procedimientoGrs) {
		this.iden = iden;
		this.capa = capa;
		this.orden = orden;
		this.codigodeterminaciongrupo = codigodeterminaciongrupo;
		this.procedimientoVts = procedimientoVts;
		this.procedimientoGrs = procedimientoGrs;
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

	@Column(name = "orden", nullable = false)
	public int getOrden() {
		return this.orden;
	}

	public void setOrden(int orden) {
		this.orden = orden;
	}

	@Column(name = "codigodeterminaciongrupo", nullable = false, length = 10)
	@NotNull
	@Length(max = 10)
	public String getCodigodeterminaciongrupo() {
		return this.codigodeterminaciongrupo;
	}

	public void setCodigodeterminaciongrupo(String codigodeterminaciongrupo) {
		this.codigodeterminaciongrupo = codigodeterminaciongrupo;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "capagrupo")
	public Set<ProcedimientoVt> getProcedimientoVts() {
		return this.procedimientoVts;
	}

	public void setProcedimientoVts(Set<ProcedimientoVt> procedimientoVts) {
		this.procedimientoVts = procedimientoVts;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "capagrupo")
	public Set<ProcedimientoGr> getProcedimientoGrs() {
		return this.procedimientoGrs;
	}

	public void setProcedimientoGrs(Set<ProcedimientoGr> procedimientoGrs) {
		this.procedimientoGrs = procedimientoGrs;
	}

}

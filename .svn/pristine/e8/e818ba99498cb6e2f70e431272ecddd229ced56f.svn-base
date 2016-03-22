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
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "procedimiento_pd", schema = "explotacion")
public class ProcedimientoPd implements java.io.Serializable {

	private int iden;
	private int iddetbasegrupoentidad;
	private Set<LeyendaPd> leyendaPds = new HashSet<LeyendaPd>(0);

	public ProcedimientoPd() {
	}

	public ProcedimientoPd(int iden, int iddetbasegrupoentidad) {
		this.iden = iden;
		this.iddetbasegrupoentidad = iddetbasegrupoentidad;
	}

	public ProcedimientoPd(int iden, int iddetbasegrupoentidad,
			Set<LeyendaPd> leyendaPds) {
		this.iden = iden;
		this.iddetbasegrupoentidad = iddetbasegrupoentidad;
		this.leyendaPds = leyendaPds;
	}

	@Id
	@Column(name = "iden", unique = true, nullable = false)
	public int getIden() {
		return this.iden;
	}

	public void setIden(int iden) {
		this.iden = iden;
	}

	@Column(name = "iddetbasegrupoentidad", nullable = false)
	public int getIddetbasegrupoentidad() {
		return this.iddetbasegrupoentidad;
	}

	public void setIddetbasegrupoentidad(int iddetbasegrupoentidad) {
		this.iddetbasegrupoentidad = iddetbasegrupoentidad;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "procedimientoPd")
	public Set<LeyendaPd> getLeyendaPds() {
		return this.leyendaPds;
	}

	public void setLeyendaPds(Set<LeyendaPd> leyendaPds) {
		this.leyendaPds = leyendaPds;
	}

}

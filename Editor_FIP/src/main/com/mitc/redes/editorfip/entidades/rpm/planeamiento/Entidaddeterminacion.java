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

package com.mitc.redes.editorfip.entidades.rpm.planeamiento;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.validator.NotNull;

@Entity
@Table(name = "entidaddeterminacion", schema = "planeamiento")
@SequenceGenerator( name = "DETSEQ", sequenceName = "planeamiento.planeamiento_entidaddeterminacion_iden_seq",allocationSize=1 )
public class Entidaddeterminacion implements java.io.Serializable {

	private int iden;
	private Entidad entidad;
	private Determinacion determinacion;
	private Set<Casoentidaddeterminacion> casoentidaddeterminacions = new HashSet<Casoentidaddeterminacion>(
			0);
	private Set<Casoentidaddeterminacion> casoentidaddeterminacions_1 = new HashSet<Casoentidaddeterminacion>(
			0);

	public Entidaddeterminacion() {
	}

	public Entidaddeterminacion(int iden, Entidad entidad,
			Determinacion determinacion) {
		this.iden = iden;
		this.entidad = entidad;
		this.determinacion = determinacion;
	}

	public Entidaddeterminacion(int iden, Entidad entidad,
			Determinacion determinacion,
			Set<Casoentidaddeterminacion> casoentidaddeterminacions,
			Set<Casoentidaddeterminacion> casoentidaddeterminacions_1) {
		this.iden = iden;
		this.entidad = entidad;
		this.determinacion = determinacion;
		this.casoentidaddeterminacions = casoentidaddeterminacions;
		this.casoentidaddeterminacions_1 = casoentidaddeterminacions_1;
	}

	@Id
	@Column(name = "iden", unique = true, nullable = false, insertable = false)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="DETSEQ")
	public int getIden() {
		return this.iden;
	}
	public void setIden(int iden) {
		this.iden = iden;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "identidad", nullable = false)
	@NotNull
	public Entidad getEntidad() {
		return this.entidad;
	}

	public void setEntidad(Entidad entidad) {
		this.entidad = entidad;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "iddeterminacion", nullable = false)
	@NotNull
	public Determinacion getDeterminacion() {
		return this.determinacion;
	}

	public void setDeterminacion(Determinacion determinacion) {
		this.determinacion = determinacion;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "entidaddeterminacion")
	public Set<Casoentidaddeterminacion> getCasoentidaddeterminacions() {
		return this.casoentidaddeterminacions;
	}

	public void setCasoentidaddeterminacions(
			Set<Casoentidaddeterminacion> casoentidaddeterminacions) {
		this.casoentidaddeterminacions = casoentidaddeterminacions;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "entidaddeterminacion")
	public Set<Casoentidaddeterminacion> getCasoentidaddeterminacions_1() {
		return this.casoentidaddeterminacions_1;
	}

	public void setCasoentidaddeterminacions_1(
			Set<Casoentidaddeterminacion> casoentidaddeterminacions_1) {
		this.casoentidaddeterminacions_1 = casoentidaddeterminacions_1;
	}

}

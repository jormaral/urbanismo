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
@Table(name = "relacion", schema = "planeamiento")
@SequenceGenerator( name = "DETSEQ", sequenceName = "planeamiento.planeamiento_relacion_iden_seq",allocationSize=1 )
public class Relacion implements java.io.Serializable {

	private int iden;
	private Tramite tramiteByIdtramiteborrador;
	private Tramite tramiteByIdtramitecreador;
	private int iddefrelacion;
	private Set<Propiedadrelacion> propiedadrelacions = new HashSet<Propiedadrelacion>(
			0);
	private Set<Operacionrelacion> operacionrelacions = new HashSet<Operacionrelacion>(
			0);
	private Set<Propiedadrelacion> propiedadrelacions_1 = new HashSet<Propiedadrelacion>(
			0);
	private Set<Vectorrelacion> vectorrelacions = new HashSet<Vectorrelacion>(0);
	private Set<Operacionrelacion> operacionrelacions_1 = new HashSet<Operacionrelacion>(
			0);
	private Set<Vectorrelacion> vectorrelacions_1 = new HashSet<Vectorrelacion>(
			0);

	public Relacion() {
	}

	public Relacion(int iden, Tramite tramiteByIdtramitecreador,
			int iddefrelacion) {
		this.iden = iden;
		this.tramiteByIdtramitecreador = tramiteByIdtramitecreador;
		this.iddefrelacion = iddefrelacion;
	}

	public Relacion(int iden, Tramite tramiteByIdtramiteborrador,
			Tramite tramiteByIdtramitecreador, int iddefrelacion,
			Set<Propiedadrelacion> propiedadrelacions,
			Set<Operacionrelacion> operacionrelacions,
			Set<Propiedadrelacion> propiedadrelacions_1,
			Set<Vectorrelacion> vectorrelacions,
			Set<Operacionrelacion> operacionrelacions_1,
			Set<Vectorrelacion> vectorrelacions_1) {
		this.iden = iden;
		this.tramiteByIdtramiteborrador = tramiteByIdtramiteborrador;
		this.tramiteByIdtramitecreador = tramiteByIdtramitecreador;
		this.iddefrelacion = iddefrelacion;
		this.propiedadrelacions = propiedadrelacions;
		this.operacionrelacions = operacionrelacions;
		this.propiedadrelacions_1 = propiedadrelacions_1;
		this.vectorrelacions = vectorrelacions;
		this.operacionrelacions_1 = operacionrelacions_1;
		this.vectorrelacions_1 = vectorrelacions_1;
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
	@JoinColumn(name = "idtramiteborrador")
	public Tramite getTramiteByIdtramiteborrador() {
		return this.tramiteByIdtramiteborrador;
	}

	public void setTramiteByIdtramiteborrador(Tramite tramiteByIdtramiteborrador) {
		this.tramiteByIdtramiteborrador = tramiteByIdtramiteborrador;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idtramitecreador", nullable = false)
	@NotNull
	public Tramite getTramiteByIdtramitecreador() {
		return this.tramiteByIdtramitecreador;
	}

	public void setTramiteByIdtramitecreador(Tramite tramiteByIdtramitecreador) {
		this.tramiteByIdtramitecreador = tramiteByIdtramitecreador;
	}

	@Column(name = "iddefrelacion", nullable = false)
	public int getIddefrelacion() {
		return this.iddefrelacion;
	}

	public void setIddefrelacion(int iddefrelacion) {
		this.iddefrelacion = iddefrelacion;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "relacion")
	public Set<Propiedadrelacion> getPropiedadrelacions() {
		return this.propiedadrelacions;
	}

	public void setPropiedadrelacions(Set<Propiedadrelacion> propiedadrelacions) {
		this.propiedadrelacions = propiedadrelacions;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "relacion")
	public Set<Operacionrelacion> getOperacionrelacions() {
		return this.operacionrelacions;
	}

	public void setOperacionrelacions(Set<Operacionrelacion> operacionrelacions) {
		this.operacionrelacions = operacionrelacions;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "relacion")
	public Set<Propiedadrelacion> getPropiedadrelacions_1() {
		return this.propiedadrelacions_1;
	}

	public void setPropiedadrelacions_1(
			Set<Propiedadrelacion> propiedadrelacions_1) {
		this.propiedadrelacions_1 = propiedadrelacions_1;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "relacion")
	public Set<Vectorrelacion> getVectorrelacions() {
		return this.vectorrelacions;
	}

	public void setVectorrelacions(Set<Vectorrelacion> vectorrelacions) {
		this.vectorrelacions = vectorrelacions;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "relacion")
	public Set<Operacionrelacion> getOperacionrelacions_1() {
		return this.operacionrelacions_1;
	}

	public void setOperacionrelacions_1(
			Set<Operacionrelacion> operacionrelacions_1) {
		this.operacionrelacions_1 = operacionrelacions_1;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "relacion")
	public Set<Vectorrelacion> getVectorrelacions_1() {
		return this.vectorrelacions_1;
	}

	public void setVectorrelacions_1(Set<Vectorrelacion> vectorrelacions_1) {
		this.vectorrelacions_1 = vectorrelacions_1;
	}

}

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
@Table(name = "operacion", schema = "planeamiento")
@SequenceGenerator( name = "DETSEQ", sequenceName = "planeamiento.planeamiento_operacion_iden_seq",allocationSize=1 )
public class Operacion implements java.io.Serializable {

	private int iden;
	private Tramite tramite;
	private String texto;
	private int orden;
	private Set<Operacionentidad> operacionentidads = new HashSet<Operacionentidad>(
			0);
	private Set<Operacionrelacion> operacionrelacions = new HashSet<Operacionrelacion>(
			0);
	private Set<Operaciondeterminacion> operaciondeterminacions = new HashSet<Operaciondeterminacion>(
			0);
	private Set<Operacionrelacion> operacionrelacions_1 = new HashSet<Operacionrelacion>(
			0);
	private Set<Operacionentidad> operacionentidads_1 = new HashSet<Operacionentidad>(
			0);
	private Set<Operaciondeterminacion> operaciondeterminacions_1 = new HashSet<Operaciondeterminacion>(
			0);

	public Operacion() {
	}

	public Operacion(int iden, Tramite tramite, int orden) {
		this.iden = iden;
		this.tramite = tramite;
		this.orden = orden;
	}

	public Operacion(int iden, Tramite tramite, String texto, int orden,
			Set<Operacionentidad> operacionentidads,
			Set<Operacionrelacion> operacionrelacions,
			Set<Operaciondeterminacion> operaciondeterminacions,
			Set<Operacionrelacion> operacionrelacions_1,
			Set<Operacionentidad> operacionentidads_1,
			Set<Operaciondeterminacion> operaciondeterminacions_1) {
		this.iden = iden;
		this.tramite = tramite;
		this.texto = texto;
		this.orden = orden;
		this.operacionentidads = operacionentidads;
		this.operacionrelacions = operacionrelacions;
		this.operaciondeterminacions = operaciondeterminacions;
		this.operacionrelacions_1 = operacionrelacions_1;
		this.operacionentidads_1 = operacionentidads_1;
		this.operaciondeterminacions_1 = operaciondeterminacions_1;
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
	@JoinColumn(name = "idtramiteordenante", nullable = false)
	@NotNull
	public Tramite getTramite() {
		return this.tramite;
	}

	public void setTramite(Tramite tramite) {
		this.tramite = tramite;
	}

	@Column(name = "texto")
	public String getTexto() {
		return this.texto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}

	@Column(name = "orden", nullable = false)
	public int getOrden() {
		return this.orden;
	}

	public void setOrden(int orden) {
		this.orden = orden;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "operacion")
	public Set<Operacionentidad> getOperacionentidads() {
		return this.operacionentidads;
	}

	public void setOperacionentidads(Set<Operacionentidad> operacionentidads) {
		this.operacionentidads = operacionentidads;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "operacion")
	public Set<Operacionrelacion> getOperacionrelacions() {
		return this.operacionrelacions;
	}

	public void setOperacionrelacions(Set<Operacionrelacion> operacionrelacions) {
		this.operacionrelacions = operacionrelacions;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "operacion")
	public Set<Operaciondeterminacion> getOperaciondeterminacions() {
		return this.operaciondeterminacions;
	}

	public void setOperaciondeterminacions(
			Set<Operaciondeterminacion> operaciondeterminacions) {
		this.operaciondeterminacions = operaciondeterminacions;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "operacion")
	public Set<Operacionrelacion> getOperacionrelacions_1() {
		return this.operacionrelacions_1;
	}

	public void setOperacionrelacions_1(
			Set<Operacionrelacion> operacionrelacions_1) {
		this.operacionrelacions_1 = operacionrelacions_1;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "operacion")
	public Set<Operacionentidad> getOperacionentidads_1() {
		return this.operacionentidads_1;
	}

	public void setOperacionentidads_1(Set<Operacionentidad> operacionentidads_1) {
		this.operacionentidads_1 = operacionentidads_1;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "operacion")
	public Set<Operaciondeterminacion> getOperaciondeterminacions_1() {
		return this.operaciondeterminacions_1;
	}

	public void setOperaciondeterminacions_1(
			Set<Operaciondeterminacion> operaciondeterminacions_1) {
		this.operaciondeterminacions_1 = operaciondeterminacions_1;
	}

}

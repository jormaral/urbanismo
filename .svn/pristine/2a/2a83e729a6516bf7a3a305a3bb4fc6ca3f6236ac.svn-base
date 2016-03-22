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

import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;

@Entity
@Table(name = "regimenespecifico", schema = "planeamiento")
@SequenceGenerator( name = "DETSEQ", sequenceName = "planeamiento.planeamiento_regimenespecifico_iden_seq",allocationSize=1 )
public class Regimenespecifico implements java.io.Serializable {

	private int iden;
	private Entidaddeterminacionregimen entidaddeterminacionregimen;
	private Regimenespecifico regimenespecifico;
	private int orden;
	private String nombre;
	private String texto;
	private Set<Regimenespecifico> regimenespecificos = new HashSet<Regimenespecifico>(
			0);
	private Set<Regimenespecifico> regimenespecificos_1 = new HashSet<Regimenespecifico>(
			0);

	public Regimenespecifico() {
	}

	public Regimenespecifico(int iden,
			Entidaddeterminacionregimen entidaddeterminacionregimen, int orden) {
		this.iden = iden;
		this.entidaddeterminacionregimen = entidaddeterminacionregimen;
		this.orden = orden;
	}

	public Regimenespecifico(int iden,
			Entidaddeterminacionregimen entidaddeterminacionregimen,
			Regimenespecifico regimenespecifico, int orden, String nombre,
			String texto, Set<Regimenespecifico> regimenespecificos,
			Set<Regimenespecifico> regimenespecificos_1) {
		this.iden = iden;
		this.entidaddeterminacionregimen = entidaddeterminacionregimen;
		this.regimenespecifico = regimenespecifico;
		this.orden = orden;
		this.nombre = nombre;
		this.texto = texto;
		this.regimenespecificos = regimenespecificos;
		this.regimenespecificos_1 = regimenespecificos_1;
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
	@JoinColumn(name = "identidaddeterminacionregimen", nullable = false)
	@NotNull
	public Entidaddeterminacionregimen getEntidaddeterminacionregimen() {
		return this.entidaddeterminacionregimen;
	}

	public void setEntidaddeterminacionregimen(
			Entidaddeterminacionregimen entidaddeterminacionregimen) {
		this.entidaddeterminacionregimen = entidaddeterminacionregimen;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idpadre")
	public Regimenespecifico getRegimenespecifico() {
		return this.regimenespecifico;
	}

	public void setRegimenespecifico(Regimenespecifico regimenespecifico) {
		this.regimenespecifico = regimenespecifico;
	}

	@Column(name = "orden", nullable = false)
	public int getOrden() {
		return this.orden;
	}

	public void setOrden(int orden) {
		this.orden = orden;
	}

	@Column(name = "nombre", length = 100)
	@Length(max = 100)
	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	@Column(name = "texto")
	public String getTexto() {
		return this.texto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "regimenespecifico")
	public Set<Regimenespecifico> getRegimenespecificos() {
		return this.regimenespecificos;
	}

	public void setRegimenespecificos(Set<Regimenespecifico> regimenespecificos) {
		this.regimenespecificos = regimenespecificos;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "regimenespecifico")
	public Set<Regimenespecifico> getRegimenespecificos_1() {
		return this.regimenespecificos_1;
	}

	public void setRegimenespecificos_1(
			Set<Regimenespecifico> regimenespecificos_1) {
		this.regimenespecificos_1 = regimenespecificos_1;
	}

}

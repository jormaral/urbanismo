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
@Table(name = "entidaddeterminacionregimen", schema = "planeamiento")
@SequenceGenerator( name = "DETSEQ", sequenceName = "planeamiento.planeamiento_entidaddeterminacionregimen_iden_seq",allocationSize=1 )
public class Entidaddeterminacionregimen implements java.io.Serializable {

	private int iden;
	private Opciondeterminacion opciondeterminacion;
	private Casoentidaddeterminacion casoentidaddeterminacionByIdcasoaplicacion;
	private Casoentidaddeterminacion casoentidaddeterminacionByIdcaso;
	private Determinacion determinacion;
	private String valor;
	private Integer superposicion;
	private Set<Regimenespecifico> regimenespecificos = new HashSet<Regimenespecifico>(
			0);
	private Set<Regimenespecifico> regimenespecificos_1 = new HashSet<Regimenespecifico>(
			0);

	public Entidaddeterminacionregimen() {
	}

	public Entidaddeterminacionregimen(int iden,
			Casoentidaddeterminacion casoentidaddeterminacionByIdcaso) {
		this.iden = iden;
		this.casoentidaddeterminacionByIdcaso = casoentidaddeterminacionByIdcaso;
	}

	public Entidaddeterminacionregimen(
			int iden,
			Opciondeterminacion opciondeterminacion,
			Casoentidaddeterminacion casoentidaddeterminacionByIdcasoaplicacion,
			Casoentidaddeterminacion casoentidaddeterminacionByIdcaso,
			Determinacion determinacion, String valor, Integer superposicion,
			Set<Regimenespecifico> regimenespecificos,
			Set<Regimenespecifico> regimenespecificos_1) {
		this.iden = iden;
		this.opciondeterminacion = opciondeterminacion;
		this.casoentidaddeterminacionByIdcasoaplicacion = casoentidaddeterminacionByIdcasoaplicacion;
		this.casoentidaddeterminacionByIdcaso = casoentidaddeterminacionByIdcaso;
		this.determinacion = determinacion;
		this.valor = valor;
		this.superposicion = superposicion;
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
	@JoinColumn(name = "idopciondeterminacion")
	public Opciondeterminacion getOpciondeterminacion() {
		return this.opciondeterminacion;
	}

	public void setOpciondeterminacion(Opciondeterminacion opciondeterminacion) {
		this.opciondeterminacion = opciondeterminacion;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idcasoaplicacion")
	public Casoentidaddeterminacion getCasoentidaddeterminacionByIdcasoaplicacion() {
		return this.casoentidaddeterminacionByIdcasoaplicacion;
	}

	public void setCasoentidaddeterminacionByIdcasoaplicacion(
			Casoentidaddeterminacion casoentidaddeterminacionByIdcasoaplicacion) {
		this.casoentidaddeterminacionByIdcasoaplicacion = casoentidaddeterminacionByIdcasoaplicacion;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idcaso", nullable = false)
	@NotNull
	public Casoentidaddeterminacion getCasoentidaddeterminacionByIdcaso() {
		return this.casoentidaddeterminacionByIdcaso;
	}

	public void setCasoentidaddeterminacionByIdcaso(
			Casoentidaddeterminacion casoentidaddeterminacionByIdcaso) {
		this.casoentidaddeterminacionByIdcaso = casoentidaddeterminacionByIdcaso;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "iddeterminacionregimen")
	public Determinacion getDeterminacion() {
		return this.determinacion;
	}

	public void setDeterminacion(Determinacion determinacion) {
		this.determinacion = determinacion;
	}

	@Column(name = "valor", length = 500)
	@Length(max = 500)
	public String getValor() {
		return this.valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

	@Column(name = "superposicion")
	public Integer getSuperposicion() {
		return this.superposicion;
	}

	public void setSuperposicion(Integer superposicion) {
		this.superposicion = superposicion;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "entidaddeterminacionregimen")
	public Set<Regimenespecifico> getRegimenespecificos() {
		return this.regimenespecificos;
	}

	public void setRegimenespecificos(Set<Regimenespecifico> regimenespecificos) {
		this.regimenespecificos = regimenespecificos;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "entidaddeterminacionregimen")
	public Set<Regimenespecifico> getRegimenespecificos_1() {
		return this.regimenespecificos_1;
	}

	public void setRegimenespecificos_1(
			Set<Regimenespecifico> regimenespecificos_1) {
		this.regimenespecificos_1 = regimenespecificos_1;
	}

}

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
@Table(name = "casoentidaddeterminacion", schema = "planeamiento")
@SequenceGenerator( name = "DETSEQ", sequenceName = "planeamiento.planeamiento_casoentidaddeterminacion_iden_seq",allocationSize=1 )
public class Casoentidaddeterminacion implements java.io.Serializable {

	private int iden;
	private Entidaddeterminacion entidaddeterminacion;
	private String nombre;
	private String codigo;
	private int orden;
	private Set<Entidaddeterminacionregimen> entidaddeterminacionregimensForIdcaso = new HashSet<Entidaddeterminacionregimen>(
			0);
	private Set<Documentocaso> documentocasos = new HashSet<Documentocaso>(0);
	private Set<Entidaddeterminacionregimen> entidaddeterminacionregimensForIdcasoaplicacion = new HashSet<Entidaddeterminacionregimen>(
			0);
	private Set<Documentocaso> documentocasos_1 = new HashSet<Documentocaso>(0);
	private Set<Vinculocaso> vinculocasosForIdcaso = new HashSet<Vinculocaso>(0);
	private Set<Vinculocaso> vinculocasosForIdcasovinculado = new HashSet<Vinculocaso>(
			0);
	private Set<Entidaddeterminacionregimen> entidaddeterminacionregimensForIdcaso_1 = new HashSet<Entidaddeterminacionregimen>(
			0);
	private Set<Vinculocaso> vinculocasosForIdcasovinculado_1 = new HashSet<Vinculocaso>(
			0);
	private Set<Entidaddeterminacionregimen> entidaddeterminacionregimensForIdcasoaplicacion_1 = new HashSet<Entidaddeterminacionregimen>(
			0);
	private Set<Vinculocaso> vinculocasosForIdcaso_1 = new HashSet<Vinculocaso>(
			0);

	public Casoentidaddeterminacion() {
	}

	public Casoentidaddeterminacion(int iden,
			Entidaddeterminacion entidaddeterminacion, int orden) {
		this.iden = iden;
		this.entidaddeterminacion = entidaddeterminacion;
		this.orden = orden;
	}

	public Casoentidaddeterminacion(
			int iden,
			Entidaddeterminacion entidaddeterminacion,
			String nombre,
			int orden,
			Set<Entidaddeterminacionregimen> entidaddeterminacionregimensForIdcaso,
			Set<Documentocaso> documentocasos,
			Set<Entidaddeterminacionregimen> entidaddeterminacionregimensForIdcasoaplicacion,
			Set<Documentocaso> documentocasos_1,
			Set<Vinculocaso> vinculocasosForIdcaso,
			Set<Vinculocaso> vinculocasosForIdcasovinculado,
			Set<Entidaddeterminacionregimen> entidaddeterminacionregimensForIdcaso_1,
			Set<Vinculocaso> vinculocasosForIdcasovinculado_1,
			Set<Entidaddeterminacionregimen> entidaddeterminacionregimensForIdcasoaplicacion_1,
			Set<Vinculocaso> vinculocasosForIdcaso_1) {
		this.iden = iden;
		this.entidaddeterminacion = entidaddeterminacion;
		this.nombre = nombre;
		this.orden = orden;
		this.entidaddeterminacionregimensForIdcaso = entidaddeterminacionregimensForIdcaso;
		this.documentocasos = documentocasos;
		this.entidaddeterminacionregimensForIdcasoaplicacion = entidaddeterminacionregimensForIdcasoaplicacion;
		this.documentocasos_1 = documentocasos_1;
		this.vinculocasosForIdcaso = vinculocasosForIdcaso;
		this.vinculocasosForIdcasovinculado = vinculocasosForIdcasovinculado;
		this.entidaddeterminacionregimensForIdcaso_1 = entidaddeterminacionregimensForIdcaso_1;
		this.vinculocasosForIdcasovinculado_1 = vinculocasosForIdcasovinculado_1;
		this.entidaddeterminacionregimensForIdcasoaplicacion_1 = entidaddeterminacionregimensForIdcasoaplicacion_1;
		this.vinculocasosForIdcaso_1 = vinculocasosForIdcaso_1;
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
	@JoinColumn(name = "identidaddeterminacion", nullable = false)
	@NotNull
	public Entidaddeterminacion getEntidaddeterminacion() {
		return this.entidaddeterminacion;
	}

	public void setEntidaddeterminacion(
			Entidaddeterminacion entidaddeterminacion) {
		this.entidaddeterminacion = entidaddeterminacion;
	}
	
	@Column(name = "codigo", length = 20)
	@Length(max = 20)
	public String getCodigo() {
		return this.codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	@Column(name = "nombre", length = 200)
	@Length(max = 200)
	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	@Column(name = "orden", nullable = false)
	public int getOrden() {
		return this.orden;
	}

	public void setOrden(int orden) {
		this.orden = orden;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "casoentidaddeterminacionByIdcaso")
	public Set<Entidaddeterminacionregimen> getEntidaddeterminacionregimensForIdcaso() {
		return this.entidaddeterminacionregimensForIdcaso;
	}

	public void setEntidaddeterminacionregimensForIdcaso(
			Set<Entidaddeterminacionregimen> entidaddeterminacionregimensForIdcaso) {
		this.entidaddeterminacionregimensForIdcaso = entidaddeterminacionregimensForIdcaso;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "casoentidaddeterminacion")
	public Set<Documentocaso> getDocumentocasos() {
		return this.documentocasos;
	}

	public void setDocumentocasos(Set<Documentocaso> documentocasos) {
		this.documentocasos = documentocasos;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "casoentidaddeterminacionByIdcasoaplicacion")
	public Set<Entidaddeterminacionregimen> getEntidaddeterminacionregimensForIdcasoaplicacion() {
		return this.entidaddeterminacionregimensForIdcasoaplicacion;
	}

	public void setEntidaddeterminacionregimensForIdcasoaplicacion(
			Set<Entidaddeterminacionregimen> entidaddeterminacionregimensForIdcasoaplicacion) {
		this.entidaddeterminacionregimensForIdcasoaplicacion = entidaddeterminacionregimensForIdcasoaplicacion;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "casoentidaddeterminacion")
	public Set<Documentocaso> getDocumentocasos_1() {
		return this.documentocasos_1;
	}

	public void setDocumentocasos_1(Set<Documentocaso> documentocasos_1) {
		this.documentocasos_1 = documentocasos_1;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "casoentidaddeterminacionByIdcaso")
	public Set<Vinculocaso> getVinculocasosForIdcaso() {
		return this.vinculocasosForIdcaso;
	}

	public void setVinculocasosForIdcaso(Set<Vinculocaso> vinculocasosForIdcaso) {
		this.vinculocasosForIdcaso = vinculocasosForIdcaso;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "casoentidaddeterminacionByIdcasovinculado")
	public Set<Vinculocaso> getVinculocasosForIdcasovinculado() {
		return this.vinculocasosForIdcasovinculado;
	}

	public void setVinculocasosForIdcasovinculado(
			Set<Vinculocaso> vinculocasosForIdcasovinculado) {
		this.vinculocasosForIdcasovinculado = vinculocasosForIdcasovinculado;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "casoentidaddeterminacionByIdcaso")
	public Set<Entidaddeterminacionregimen> getEntidaddeterminacionregimensForIdcaso_1() {
		return this.entidaddeterminacionregimensForIdcaso_1;
	}

	public void setEntidaddeterminacionregimensForIdcaso_1(
			Set<Entidaddeterminacionregimen> entidaddeterminacionregimensForIdcaso_1) {
		this.entidaddeterminacionregimensForIdcaso_1 = entidaddeterminacionregimensForIdcaso_1;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "casoentidaddeterminacionByIdcasovinculado")
	public Set<Vinculocaso> getVinculocasosForIdcasovinculado_1() {
		return this.vinculocasosForIdcasovinculado_1;
	}

	public void setVinculocasosForIdcasovinculado_1(
			Set<Vinculocaso> vinculocasosForIdcasovinculado_1) {
		this.vinculocasosForIdcasovinculado_1 = vinculocasosForIdcasovinculado_1;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "casoentidaddeterminacionByIdcasoaplicacion")
	public Set<Entidaddeterminacionregimen> getEntidaddeterminacionregimensForIdcasoaplicacion_1() {
		return this.entidaddeterminacionregimensForIdcasoaplicacion_1;
	}

	public void setEntidaddeterminacionregimensForIdcasoaplicacion_1(
			Set<Entidaddeterminacionregimen> entidaddeterminacionregimensForIdcasoaplicacion_1) {
		this.entidaddeterminacionregimensForIdcasoaplicacion_1 = entidaddeterminacionregimensForIdcasoaplicacion_1;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "casoentidaddeterminacionByIdcaso")
	public Set<Vinculocaso> getVinculocasosForIdcaso_1() {
		return this.vinculocasosForIdcaso_1;
	}

	public void setVinculocasosForIdcaso_1(
			Set<Vinculocaso> vinculocasosForIdcaso_1) {
		this.vinculocasosForIdcaso_1 = vinculocasosForIdcaso_1;
	}

	

	// ---| TRANSIENTS |-----------------------
	

	
	
}

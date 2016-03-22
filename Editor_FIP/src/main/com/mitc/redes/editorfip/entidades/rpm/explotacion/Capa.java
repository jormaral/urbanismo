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
import javax.persistence.UniqueConstraint;

import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;

@Entity
@Table(name = "capa", schema = "explotacion", uniqueConstraints = @UniqueConstraint(columnNames = "orden"))
public class Capa implements java.io.Serializable {

	private int iden;
	private String nombre;
	private int orden;
	private Set<Capagrupo> capagrupos = new HashSet<Capagrupo>(0);
	private Set<LeyendaAl> leyendaAls = new HashSet<LeyendaAl>(0);
	private Set<Capaentidadlista> capaentidadlistas = new HashSet<Capaentidadlista>(
			0);
	private Set<ProcedimientoRg> procedimientoRgs = new HashSet<ProcedimientoRg>(
			0);
	private Set<LeyendaPd> leyendaPds = new HashSet<LeyendaPd>(0);
	private Set<Rangovalor> rangovalors = new HashSet<Rangovalor>(0);
	private Set<LeyendaVt> leyendaVts = new HashSet<LeyendaVt>(0);
	private Set<Capaentidadconjunto> capaentidadconjuntos = new HashSet<Capaentidadconjunto>(
			0);

	public Capa() {
	}

	public Capa(int iden, String nombre, int orden) {
		this.iden = iden;
		this.nombre = nombre;
		this.orden = orden;
	}

	public Capa(int iden, String nombre, int orden, Set<Capagrupo> capagrupos,
			Set<LeyendaAl> leyendaAls, Set<Capaentidadlista> capaentidadlistas,
			Set<ProcedimientoRg> procedimientoRgs, Set<LeyendaPd> leyendaPds,
			Set<Rangovalor> rangovalors, Set<LeyendaVt> leyendaVts,
			Set<Capaentidadconjunto> capaentidadconjuntos) {
		this.iden = iden;
		this.nombre = nombre;
		this.orden = orden;
		this.capagrupos = capagrupos;
		this.leyendaAls = leyendaAls;
		this.capaentidadlistas = capaentidadlistas;
		this.procedimientoRgs = procedimientoRgs;
		this.leyendaPds = leyendaPds;
		this.rangovalors = rangovalors;
		this.leyendaVts = leyendaVts;
		this.capaentidadconjuntos = capaentidadconjuntos;
	}

	@Id
	@Column(name = "iden", unique = true, nullable = false)
	public int getIden() {
		return this.iden;
	}

	public void setIden(int iden) {
		this.iden = iden;
	}

	@Column(name = "nombre", nullable = false, length = 50)
	@NotNull
	@Length(max = 50)
	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	@Column(name = "orden", unique = true, nullable = false)
	public int getOrden() {
		return this.orden;
	}

	public void setOrden(int orden) {
		this.orden = orden;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "capa")
	public Set<Capagrupo> getCapagrupos() {
		return this.capagrupos;
	}

	public void setCapagrupos(Set<Capagrupo> capagrupos) {
		this.capagrupos = capagrupos;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "capa")
	public Set<LeyendaAl> getLeyendaAls() {
		return this.leyendaAls;
	}

	public void setLeyendaAls(Set<LeyendaAl> leyendaAls) {
		this.leyendaAls = leyendaAls;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "capa")
	public Set<Capaentidadlista> getCapaentidadlistas() {
		return this.capaentidadlistas;
	}

	public void setCapaentidadlistas(Set<Capaentidadlista> capaentidadlistas) {
		this.capaentidadlistas = capaentidadlistas;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "capa")
	public Set<ProcedimientoRg> getProcedimientoRgs() {
		return this.procedimientoRgs;
	}

	public void setProcedimientoRgs(Set<ProcedimientoRg> procedimientoRgs) {
		this.procedimientoRgs = procedimientoRgs;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "capa")
	public Set<LeyendaPd> getLeyendaPds() {
		return this.leyendaPds;
	}

	public void setLeyendaPds(Set<LeyendaPd> leyendaPds) {
		this.leyendaPds = leyendaPds;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "capa")
	public Set<Rangovalor> getRangovalors() {
		return this.rangovalors;
	}

	public void setRangovalors(Set<Rangovalor> rangovalors) {
		this.rangovalors = rangovalors;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "capa")
	public Set<LeyendaVt> getLeyendaVts() {
		return this.leyendaVts;
	}

	public void setLeyendaVts(Set<LeyendaVt> leyendaVts) {
		this.leyendaVts = leyendaVts;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "capa")
	public Set<Capaentidadconjunto> getCapaentidadconjuntos() {
		return this.capaentidadconjuntos;
	}

	public void setCapaentidadconjuntos(
			Set<Capaentidadconjunto> capaentidadconjuntos) {
		this.capaentidadconjuntos = capaentidadconjuntos;
	}

}

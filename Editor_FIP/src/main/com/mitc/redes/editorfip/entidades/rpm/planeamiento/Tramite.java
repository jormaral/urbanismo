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

import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;

import com.mitc.redes.editorfip.entidades.fipxml.PropiedadesAdscripcion;

@Entity
@Table(name = "tramite", schema = "planeamiento")
@SequenceGenerator( name = "DETSEQ", sequenceName = "planeamiento.planeamiento_tramite_iden_seq",allocationSize=1 )
public class Tramite implements java.io.Serializable {

	private int iden;
	private Plan plan;
	private int idtipotramite;
	private Integer idorgano;
	private Integer idsentido;
	private Date fecha;
	private String texto;
	private String comentario;
	private String numeroacuerdo;
	private String nombre;
	private int idcentroproduccion;
	private String codigofip;
	private int iteracion;
	private Date fechaconsolidacion;
	private Set<Boletintramite> boletintramites = new HashSet<Boletintramite>(0);
	private Set<Relacion> relacionsForIdtramitecreador = new HashSet<Relacion>(
			0);
	private Set<Operacion> operacions = new HashSet<Operacion>(0);
	private Set<Determinacion> determinacions = new HashSet<Determinacion>(0);
	private Set<Operacion> operacions_1 = new HashSet<Operacion>(0);
	private Set<Relacion> relacionsForIdtramiteborrador = new HashSet<Relacion>(
			0);
	private Set<Determinacion> determinacions_1 = new HashSet<Determinacion>(0);
	private Set<Documento> documentos = new HashSet<Documento>(0);
	private Set<Boletintramite> boletintramites_1 = new HashSet<Boletintramite>(
			0);
	private Set<Entidad> entidads = new HashSet<Entidad>(0);
	private Set<Relacion> relacionsForIdtramiteborrador_1 = new HashSet<Relacion>(
			0);
	private Set<Documento> documentos_1 = new HashSet<Documento>(0);
	private Set<Entidad> entidads_1 = new HashSet<Entidad>(0);
	private Set<Relacion> relacionsForIdtramitecreador_1 = new HashSet<Relacion>(
			0);
	
	// Para generar FIP XML
	private Set<PropiedadesAdscripcion> adscripciones;

	public Tramite() {
	}

	public Tramite(int iden, Plan plan, int idtipotramite,
			int idcentroproduccion, int iteracion) {
		this.iden = iden;
		this.plan = plan;
		this.idtipotramite = idtipotramite;
		this.idcentroproduccion = idcentroproduccion;
		this.iteracion = iteracion;
	}

	public Tramite(int iden, Plan plan, int idtipotramite, Integer idorgano,
			Integer idsentido, Date fecha, String texto, String comentario,
			String numeroacuerdo, String nombre, int idcentroproduccion,
			String codigofip, int iteracion, Date fechaconsolidacion,
			Set<Boletintramite> boletintramites,
			Set<Relacion> relacionsForIdtramitecreador,
			Set<Operacion> operacions, Set<Determinacion> determinacions,
			Set<Operacion> operacions_1,
			Set<Relacion> relacionsForIdtramiteborrador,
			Set<Determinacion> determinacions_1, Set<Documento> documentos,
			Set<Boletintramite> boletintramites_1, Set<Entidad> entidads,
			Set<Relacion> relacionsForIdtramiteborrador_1,
			Set<Documento> documentos_1, Set<Entidad> entidads_1,
			Set<Relacion> relacionsForIdtramitecreador_1) {
		this.iden = iden;
		this.plan = plan;
		this.idtipotramite = idtipotramite;
		this.idorgano = idorgano;
		this.idsentido = idsentido;
		this.fecha = fecha;
		this.texto = texto;
		this.comentario = comentario;
		this.numeroacuerdo = numeroacuerdo;
		this.nombre = nombre;
		this.idcentroproduccion = idcentroproduccion;
		this.codigofip = codigofip;
		this.iteracion = iteracion;
		this.fechaconsolidacion = fechaconsolidacion;
		this.boletintramites = boletintramites;
		this.relacionsForIdtramitecreador = relacionsForIdtramitecreador;
		this.operacions = operacions;
		this.determinacions = determinacions;
		this.operacions_1 = operacions_1;
		this.relacionsForIdtramiteborrador = relacionsForIdtramiteborrador;
		this.determinacions_1 = determinacions_1;
		this.documentos = documentos;
		this.boletintramites_1 = boletintramites_1;
		this.entidads = entidads;
		this.relacionsForIdtramiteborrador_1 = relacionsForIdtramiteborrador_1;
		this.documentos_1 = documentos_1;
		this.entidads_1 = entidads_1;
		this.relacionsForIdtramitecreador_1 = relacionsForIdtramitecreador_1;
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
	@JoinColumn(name = "idplan", nullable = false)
	@NotNull
	public Plan getPlan() {
		return this.plan;
	}

	public void setPlan(Plan plan) {
		this.plan = plan;
	}

	@Column(name = "idtipotramite", nullable = false)
	public int getIdtipotramite() {
		return this.idtipotramite;
	}

	public void setIdtipotramite(int idtipotramite) {
		this.idtipotramite = idtipotramite;
	}

	@Column(name = "idorgano")
	public Integer getIdorgano() {
		return this.idorgano;
	}

	public void setIdorgano(Integer idorgano) {
		this.idorgano = idorgano;
	}

	@Column(name = "idsentido")
	public Integer getIdsentido() {
		return this.idsentido;
	}

	public void setIdsentido(Integer idsentido) {
		this.idsentido = idsentido;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "fecha", length = 13)
	public Date getFecha() {
		return this.fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	@Column(name = "texto")
	public String getTexto() {
		return this.texto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}

	@Column(name = "comentario")
	public String getComentario() {
		return this.comentario;
	}

	public void setComentario(String comentario) {
		this.comentario = comentario;
	}

	@Column(name = "numeroacuerdo", length = 20)
	@Length(max = 20)
	public String getNumeroacuerdo() {
		return this.numeroacuerdo;
	}

	public void setNumeroacuerdo(String numeroacuerdo) {
		this.numeroacuerdo = numeroacuerdo;
	}

	@Column(name = "nombre")
	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	@Column(name = "idcentroproduccion", nullable = false)
	public int getIdcentroproduccion() {
		return this.idcentroproduccion;
	}

	public void setIdcentroproduccion(int idcentroproduccion) {
		this.idcentroproduccion = idcentroproduccion;
	}

	@Column(name = "codigofip", length = 32)
	@Length(max = 32)
	public String getCodigofip() {
		return this.codigofip;
	}

	public void setCodigofip(String codigofip) {
		this.codigofip = codigofip;
	}

	@Column(name = "iteracion", nullable = false)
	public int getIteracion() {
		return this.iteracion;
	}

	public void setIteracion(int iteracion) {
		this.iteracion = iteracion;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "fechaconsolidacion", length = 13)
	public Date getFechaconsolidacion() {
		return this.fechaconsolidacion;
	}

	public void setFechaconsolidacion(Date fechaconsolidacion) {
		this.fechaconsolidacion = fechaconsolidacion;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "tramite")
	public Set<Boletintramite> getBoletintramites() {
		return this.boletintramites;
	}

	public void setBoletintramites(Set<Boletintramite> boletintramites) {
		this.boletintramites = boletintramites;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "tramiteByIdtramitecreador")
	public Set<Relacion> getRelacionsForIdtramitecreador() {
		return this.relacionsForIdtramitecreador;
	}

	public void setRelacionsForIdtramitecreador(
			Set<Relacion> relacionsForIdtramitecreador) {
		this.relacionsForIdtramitecreador = relacionsForIdtramitecreador;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "tramite")
	public Set<Operacion> getOperacions() {
		return this.operacions;
	}

	public void setOperacions(Set<Operacion> operacions) {
		this.operacions = operacions;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "tramite")
	public Set<Determinacion> getDeterminacions() {
		return this.determinacions;
	}

	public void setDeterminacions(Set<Determinacion> determinacions) {
		this.determinacions = determinacions;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "tramite")
	public Set<Operacion> getOperacions_1() {
		return this.operacions_1;
	}

	public void setOperacions_1(Set<Operacion> operacions_1) {
		this.operacions_1 = operacions_1;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "tramiteByIdtramiteborrador")
	public Set<Relacion> getRelacionsForIdtramiteborrador() {
		return this.relacionsForIdtramiteborrador;
	}

	public void setRelacionsForIdtramiteborrador(
			Set<Relacion> relacionsForIdtramiteborrador) {
		this.relacionsForIdtramiteborrador = relacionsForIdtramiteborrador;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "tramite")
	public Set<Determinacion> getDeterminacions_1() {
		return this.determinacions_1;
	}

	public void setDeterminacions_1(Set<Determinacion> determinacions_1) {
		this.determinacions_1 = determinacions_1;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "tramite")
	public Set<Documento> getDocumentos() {
		return this.documentos;
	}

	public void setDocumentos(Set<Documento> documentos) {
		this.documentos = documentos;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "tramite")
	public Set<Boletintramite> getBoletintramites_1() {
		return this.boletintramites_1;
	}

	public void setBoletintramites_1(Set<Boletintramite> boletintramites_1) {
		this.boletintramites_1 = boletintramites_1;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "tramite")
	public Set<Entidad> getEntidads() {
		return this.entidads;
	}

	public void setEntidads(Set<Entidad> entidads) {
		this.entidads = entidads;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "tramiteByIdtramiteborrador")
	public Set<Relacion> getRelacionsForIdtramiteborrador_1() {
		return this.relacionsForIdtramiteborrador_1;
	}

	public void setRelacionsForIdtramiteborrador_1(
			Set<Relacion> relacionsForIdtramiteborrador_1) {
		this.relacionsForIdtramiteborrador_1 = relacionsForIdtramiteborrador_1;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "tramite")
	public Set<Documento> getDocumentos_1() {
		return this.documentos_1;
	}

	public void setDocumentos_1(Set<Documento> documentos_1) {
		this.documentos_1 = documentos_1;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "tramite")
	public Set<Entidad> getEntidads_1() {
		return this.entidads_1;
	}

	public void setEntidads_1(Set<Entidad> entidads_1) {
		this.entidads_1 = entidads_1;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "tramiteByIdtramitecreador")
	public Set<Relacion> getRelacionsForIdtramitecreador_1() {
		return this.relacionsForIdtramitecreador_1;
	}

	public void setRelacionsForIdtramitecreador_1(
			Set<Relacion> relacionsForIdtramitecreador_1) {
		this.relacionsForIdtramitecreador_1 = relacionsForIdtramitecreador_1;
	}
	
	@Transient
	 public Set<PropiedadesAdscripcion> getPropiedadesAdscripcion() {
        return this.adscripciones;
    }

    public void setPropiedadesAdscripcion(Set<PropiedadesAdscripcion> adscripciones) {
        this.adscripciones = adscripciones;
    }

}

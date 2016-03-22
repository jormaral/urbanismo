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
@Table(name = "documento", schema = "planeamiento")
@SequenceGenerator( name = "DETSEQ", sequenceName = "planeamiento.planeamiento_documento_iden_seq",allocationSize=1 )
public class Documento implements java.io.Serializable {

	private int iden;
	private Tramite tramite;
	private Documento documento;
	private String nombre;
	private String archivo;
	private String comentario;
	private Integer escala;
	private Integer idtipodocumento;
	private Integer idgrupodocumento;
	private Set<Documentoshp> documentoshps = new HashSet<Documentoshp>(0);
	private Set<Documentocaso> documentocasos = new HashSet<Documentocaso>(0);
	private Set<Documentoentidad> documentoentidads = new HashSet<Documentoentidad>(
			0);
	private Set<Documentocaso> documentocasos_1 = new HashSet<Documentocaso>(0);
	private Set<Documentodeterminacion> documentodeterminacions = new HashSet<Documentodeterminacion>(
			0);
	private Set<Documentodeterminacion> documentodeterminacions_1 = new HashSet<Documentodeterminacion>(
			0);
	private Set<Documento> documentos = new HashSet<Documento>(0);
	private Set<Documentoshp> documentoshps_1 = new HashSet<Documentoshp>(0);
	private Set<Documento> documentos_1 = new HashSet<Documento>(0);
	private Set<Documentoentidad> documentoentidads_1 = new HashSet<Documentoentidad>(
			0);

	public Documento() {
	}

	public Documento(int iden, Tramite tramite, String nombre, String archivo) {
		this.iden = iden;
		this.tramite = tramite;
		this.nombre = nombre;
		this.archivo = archivo;
	}

	public Documento(int iden, Tramite tramite, Documento documento,
			String nombre, String archivo, String comentario, Integer escala,
			Integer idtipodocumento, Integer idgrupodocumento,
			Set<Documentoshp> documentoshps, Set<Documentocaso> documentocasos,
			Set<Documentoentidad> documentoentidads,
			Set<Documentocaso> documentocasos_1,
			Set<Documentodeterminacion> documentodeterminacions,
			Set<Documentodeterminacion> documentodeterminacions_1,
			Set<Documento> documentos, Set<Documentoshp> documentoshps_1,
			Set<Documento> documentos_1,
			Set<Documentoentidad> documentoentidads_1) {
		this.iden = iden;
		this.tramite = tramite;
		this.documento = documento;
		this.nombre = nombre;
		this.archivo = archivo;
		this.comentario = comentario;
		this.escala = escala;
		this.idtipodocumento = idtipodocumento;
		this.idgrupodocumento = idgrupodocumento;
		this.documentoshps = documentoshps;
		this.documentocasos = documentocasos;
		this.documentoentidads = documentoentidads;
		this.documentocasos_1 = documentocasos_1;
		this.documentodeterminacions = documentodeterminacions;
		this.documentodeterminacions_1 = documentodeterminacions_1;
		this.documentos = documentos;
		this.documentoshps_1 = documentoshps_1;
		this.documentos_1 = documentos_1;
		this.documentoentidads_1 = documentoentidads_1;
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
	@JoinColumn(name = "idtramite", nullable = false)
	@NotNull
	public Tramite getTramite() {
		return this.tramite;
	}

	public void setTramite(Tramite tramite) {
		this.tramite = tramite;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "iddocumentooriginal")
	public Documento getDocumento() {
		return this.documento;
	}

	public void setDocumento(Documento documento) {
		this.documento = documento;
	}

	@Column(name = "nombre", nullable = false)
	@NotNull
	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	@Column(name = "archivo", nullable = false)
	@NotNull
	public String getArchivo() {
		return this.archivo;
	}

	public void setArchivo(String archivo) {
		this.archivo = archivo;
	}

	@Column(name = "comentario")
	public String getComentario() {
		return this.comentario;
	}

	public void setComentario(String comentario) {
		this.comentario = comentario;
	}

	@Column(name = "escala")
	public Integer getEscala() {
		return this.escala;
	}

	public void setEscala(Integer escala) {
		this.escala = escala;
	}

	@Column(name = "idtipodocumento")
	public Integer getIdtipodocumento() {
		return this.idtipodocumento;
	}

	public void setIdtipodocumento(Integer idtipodocumento) {
		this.idtipodocumento = idtipodocumento;
	}

	@Column(name = "idgrupodocumento")
	public Integer getIdgrupodocumento() {
		return this.idgrupodocumento;
	}

	public void setIdgrupodocumento(Integer idgrupodocumento) {
		this.idgrupodocumento = idgrupodocumento;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "documento")
	public Set<Documentoshp> getDocumentoshps() {
		return this.documentoshps;
	}

	public void setDocumentoshps(Set<Documentoshp> documentoshps) {
		this.documentoshps = documentoshps;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "documento")
	public Set<Documentocaso> getDocumentocasos() {
		return this.documentocasos;
	}

	public void setDocumentocasos(Set<Documentocaso> documentocasos) {
		this.documentocasos = documentocasos;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "documento")
	public Set<Documentoentidad> getDocumentoentidads() {
		return this.documentoentidads;
	}

	public void setDocumentoentidads(Set<Documentoentidad> documentoentidads) {
		this.documentoentidads = documentoentidads;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "documento")
	public Set<Documentocaso> getDocumentocasos_1() {
		return this.documentocasos_1;
	}

	public void setDocumentocasos_1(Set<Documentocaso> documentocasos_1) {
		this.documentocasos_1 = documentocasos_1;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "documento")
	public Set<Documentodeterminacion> getDocumentodeterminacions() {
		return this.documentodeterminacions;
	}

	public void setDocumentodeterminacions(
			Set<Documentodeterminacion> documentodeterminacions) {
		this.documentodeterminacions = documentodeterminacions;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "documento")
	public Set<Documentodeterminacion> getDocumentodeterminacions_1() {
		return this.documentodeterminacions_1;
	}

	public void setDocumentodeterminacions_1(
			Set<Documentodeterminacion> documentodeterminacions_1) {
		this.documentodeterminacions_1 = documentodeterminacions_1;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "documento")
	public Set<Documento> getDocumentos() {
		return this.documentos;
	}

	public void setDocumentos(Set<Documento> documentos) {
		this.documentos = documentos;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "documento")
	public Set<Documentoshp> getDocumentoshps_1() {
		return this.documentoshps_1;
	}

	public void setDocumentoshps_1(Set<Documentoshp> documentoshps_1) {
		this.documentoshps_1 = documentoshps_1;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "documento")
	public Set<Documento> getDocumentos_1() {
		return this.documentos_1;
	}

	public void setDocumentos_1(Set<Documento> documentos_1) {
		this.documentos_1 = documentos_1;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "documento")
	public Set<Documentoentidad> getDocumentoentidads_1() {
		return this.documentoentidads_1;
	}

	public void setDocumentoentidads_1(Set<Documentoentidad> documentoentidads_1) {
		this.documentoentidads_1 = documentoentidads_1;
	}

}

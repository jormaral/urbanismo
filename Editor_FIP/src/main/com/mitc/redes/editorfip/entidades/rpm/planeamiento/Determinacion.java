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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
import javax.persistence.Transient;

import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;

@Entity
@Table(name = "determinacion", schema = "planeamiento")
@SequenceGenerator( name = "DETSEQ", sequenceName = "planeamiento.planeamiento_determinacion_iden_seq",allocationSize=1 )
public class Determinacion implements java.io.Serializable {

	private int iden;
	private Tramite tramite;
	private Determinacion determinacionByIddeterminacionoriginal;
	private Determinacion determinacionByIdpadre;
	private Determinacion determinacionByIddeterminacionbase;
	private int idcaracter;
	private String apartado;
	private String nombre;
	private String texto;
	private String etiqueta;
	private String codigo;
	private boolean bsuspendida;
	private int orden;
	
	private String apartadoCompleto;
	
	private Set<Documentodeterminacion> documentodeterminacions = new HashSet<Documentodeterminacion>(
			0);
	private Set<Determinacion> determinacionsForIdpadre = new HashSet<Determinacion>(
			0);
	private Set<Entidaddeterminacion> entidaddeterminacions = new HashSet<Entidaddeterminacion>(
			0);
	private Set<Documentodeterminacion> documentodeterminacions_1 = new HashSet<Documentodeterminacion>(
			0);
	private Set<Determinacion> determinacionsForIddeterminacionoriginal = new HashSet<Determinacion>(
			0);
	private Set<Opciondeterminacion> opciondeterminacionsForIddeterminacionvalorref = new HashSet<Opciondeterminacion>(
			0);
	private Set<Determinacion> determinacionsForIddeterminacionoriginal_1 = new HashSet<Determinacion>(
			0);
	private Set<Opciondeterminacion> opciondeterminacionsForIddeterminacion = new HashSet<Opciondeterminacion>(
			0);
	private Set<Determinaciongrupoentidad> determinaciongrupoentidadsForIddeterminacion = new HashSet<Determinaciongrupoentidad>(
			0);
	private Set<Entidaddeterminacionregimen> entidaddeterminacionregimens = new HashSet<Entidaddeterminacionregimen>(
			0);
	private Set<Determinacion> determinacionsForIddeterminacionbase = new HashSet<Determinacion>(
			0);
	private Set<Determinacion> determinacionsForIddeterminacionbase_1 = new HashSet<Determinacion>(
			0);
	private Set<Determinaciongrupoentidad> determinaciongrupoentidadsForIddeterminaciongrupo = new HashSet<Determinaciongrupoentidad>(
			0);
	private Set<Opciondeterminacion> opciondeterminacionsForIddeterminacion_1 = new HashSet<Opciondeterminacion>(
			0);
	private Set<Operaciondeterminacion> operaciondeterminacionsForIddeterminacionoperadora = new HashSet<Operaciondeterminacion>(
			0);
	private Set<Determinaciongrupoentidad> determinaciongrupoentidadsForIddeterminaciongrupo_1 = new HashSet<Determinaciongrupoentidad>(
			0);
	private Set<Determinaciongrupoentidad> determinaciongrupoentidadsForIddeterminacion_1 = new HashSet<Determinaciongrupoentidad>(
			0);
	private Set<Operaciondeterminacion> operaciondeterminacionsForIddeterminacionoperadora_1 = new HashSet<Operaciondeterminacion>(
			0);
	private Set<Operaciondeterminacion> operaciondeterminacionsForIddeterminacion = new HashSet<Operaciondeterminacion>(
			0);
	private Set<Determinacion> determinacionsForIdpadre_1 = new HashSet<Determinacion>(
			0);
	private Set<Operaciondeterminacion> operaciondeterminacionsForIddeterminacion_1 = new HashSet<Operaciondeterminacion>(
			0);
	private Set<Opciondeterminacion> opciondeterminacionsForIddeterminacionvalorref_1 = new HashSet<Opciondeterminacion>(
			0);
	private Set<Entidaddeterminacionregimen> entidaddeterminacionregimens_1 = new HashSet<Entidaddeterminacionregimen>(
			0);
	private Set<Entidaddeterminacion> entidaddeterminacions_1 = new HashSet<Entidaddeterminacion>(
			0);

	public Determinacion() {
	}

	public Determinacion(int iden, Tramite tramite, int idcaracter,
			String nombre, String codigo, boolean bsuspendida, int orden) {
		this.iden = iden;
		this.tramite = tramite;
		this.idcaracter = idcaracter;
		this.nombre = nombre;
		this.codigo = codigo;
		this.bsuspendida = bsuspendida;
		this.orden = orden;
	}

	public Determinacion(
			int iden,
			Tramite tramite,
			Determinacion determinacionByIddeterminacionoriginal,
			Determinacion determinacionByIdpadre,
			Determinacion determinacionByIddeterminacionbase,
			int idcaracter,
			String apartado,
			String nombre,
			String texto,
			String etiqueta,
			String codigo,
			boolean bsuspendida,
			int orden,
			Set<Documentodeterminacion> documentodeterminacions,
			Set<Determinacion> determinacionsForIdpadre,
			Set<Entidaddeterminacion> entidaddeterminacions,
			Set<Documentodeterminacion> documentodeterminacions_1,
			Set<Determinacion> determinacionsForIddeterminacionoriginal,
			Set<Opciondeterminacion> opciondeterminacionsForIddeterminacionvalorref,
			Set<Determinacion> determinacionsForIddeterminacionoriginal_1,
			Set<Opciondeterminacion> opciondeterminacionsForIddeterminacion,
			Set<Determinaciongrupoentidad> determinaciongrupoentidadsForIddeterminacion,
			Set<Entidaddeterminacionregimen> entidaddeterminacionregimens,
			Set<Determinacion> determinacionsForIddeterminacionbase,
			Set<Determinacion> determinacionsForIddeterminacionbase_1,
			Set<Determinaciongrupoentidad> determinaciongrupoentidadsForIddeterminaciongrupo,
			Set<Opciondeterminacion> opciondeterminacionsForIddeterminacion_1,
			Set<Operaciondeterminacion> operaciondeterminacionsForIddeterminacionoperadora,
			Set<Determinaciongrupoentidad> determinaciongrupoentidadsForIddeterminaciongrupo_1,
			Set<Determinaciongrupoentidad> determinaciongrupoentidadsForIddeterminacion_1,
			Set<Operaciondeterminacion> operaciondeterminacionsForIddeterminacionoperadora_1,
			Set<Operaciondeterminacion> operaciondeterminacionsForIddeterminacion,
			Set<Determinacion> determinacionsForIdpadre_1,
			Set<Operaciondeterminacion> operaciondeterminacionsForIddeterminacion_1,
			Set<Opciondeterminacion> opciondeterminacionsForIddeterminacionvalorref_1,
			Set<Entidaddeterminacionregimen> entidaddeterminacionregimens_1,
			Set<Entidaddeterminacion> entidaddeterminacions_1) {
		this.iden = iden;
		this.tramite = tramite;
		this.determinacionByIddeterminacionoriginal = determinacionByIddeterminacionoriginal;
		this.determinacionByIdpadre = determinacionByIdpadre;
		this.determinacionByIddeterminacionbase = determinacionByIddeterminacionbase;
		this.idcaracter = idcaracter;
		this.apartado = apartado;
		this.nombre = nombre;
		this.texto = texto;
		this.etiqueta = etiqueta;
		this.codigo = codigo;
		this.bsuspendida = bsuspendida;
		this.orden = orden;
		this.documentodeterminacions = documentodeterminacions;
		this.determinacionsForIdpadre = determinacionsForIdpadre;
		this.entidaddeterminacions = entidaddeterminacions;
		this.documentodeterminacions_1 = documentodeterminacions_1;
		this.determinacionsForIddeterminacionoriginal = determinacionsForIddeterminacionoriginal;
		this.opciondeterminacionsForIddeterminacionvalorref = opciondeterminacionsForIddeterminacionvalorref;
		this.determinacionsForIddeterminacionoriginal_1 = determinacionsForIddeterminacionoriginal_1;
		this.opciondeterminacionsForIddeterminacion = opciondeterminacionsForIddeterminacion;
		this.determinaciongrupoentidadsForIddeterminacion = determinaciongrupoentidadsForIddeterminacion;
		this.entidaddeterminacionregimens = entidaddeterminacionregimens;
		this.determinacionsForIddeterminacionbase = determinacionsForIddeterminacionbase;
		this.determinacionsForIddeterminacionbase_1 = determinacionsForIddeterminacionbase_1;
		this.determinaciongrupoentidadsForIddeterminaciongrupo = determinaciongrupoentidadsForIddeterminaciongrupo;
		this.opciondeterminacionsForIddeterminacion_1 = opciondeterminacionsForIddeterminacion_1;
		this.operaciondeterminacionsForIddeterminacionoperadora = operaciondeterminacionsForIddeterminacionoperadora;
		this.determinaciongrupoentidadsForIddeterminaciongrupo_1 = determinaciongrupoentidadsForIddeterminaciongrupo_1;
		this.determinaciongrupoentidadsForIddeterminacion_1 = determinaciongrupoentidadsForIddeterminacion_1;
		this.operaciondeterminacionsForIddeterminacionoperadora_1 = operaciondeterminacionsForIddeterminacionoperadora_1;
		this.operaciondeterminacionsForIddeterminacion = operaciondeterminacionsForIddeterminacion;
		this.determinacionsForIdpadre_1 = determinacionsForIdpadre_1;
		this.operaciondeterminacionsForIddeterminacion_1 = operaciondeterminacionsForIddeterminacion_1;
		this.opciondeterminacionsForIddeterminacionvalorref_1 = opciondeterminacionsForIddeterminacionvalorref_1;
		this.entidaddeterminacionregimens_1 = entidaddeterminacionregimens_1;
		this.entidaddeterminacions_1 = entidaddeterminacions_1;
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
	
	
	
	@Transient
	public String getApartadoCompleto() {
		return apartadoCompleto;
	}

	public void setApartadoCompleto(String apartadoCompleto) {
		this.apartadoCompleto = apartadoCompleto;
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
	@JoinColumn(name = "iddeterminacionoriginal")
	public Determinacion getDeterminacionByIddeterminacionoriginal() {
		return this.determinacionByIddeterminacionoriginal;
	}

	public void setDeterminacionByIddeterminacionoriginal(
			Determinacion determinacionByIddeterminacionoriginal) {
		this.determinacionByIddeterminacionoriginal = determinacionByIddeterminacionoriginal;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idpadre")
	public Determinacion getDeterminacionByIdpadre() {
		return this.determinacionByIdpadre;
	}

	public void setDeterminacionByIdpadre(Determinacion determinacionByIdpadre) {
		this.determinacionByIdpadre = determinacionByIdpadre;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "iddeterminacionbase")
	public Determinacion getDeterminacionByIddeterminacionbase() {
		return this.determinacionByIddeterminacionbase;
	}

	public void setDeterminacionByIddeterminacionbase(
			Determinacion determinacionByIddeterminacionbase) {
		this.determinacionByIddeterminacionbase = determinacionByIddeterminacionbase;
	}

	@Column(name = "idcaracter", nullable = false)
	public int getIdcaracter() {
		return this.idcaracter;
	}

	public void setIdcaracter(int idcaracter) {
		this.idcaracter = idcaracter;
	}

	@Column(name = "apartado", length = 100)
	@Length(max = 100)
	public String getApartado() {
		return this.apartado;
	}

	public void setApartado(String apartado) {
		this.apartado = apartado;
	}

	@Column(name = "nombre", nullable = false, length = 1000)
	@NotNull
	@Length(max = 1000)
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

	@Column(name = "etiqueta", length = 100)
	@Length(max = 100)
	public String getEtiqueta() {
		return this.etiqueta;
	}

	public void setEtiqueta(String etiqueta) {
		this.etiqueta = etiqueta;
	}

	@Column(name = "codigo", nullable = false, length = 10)
	@NotNull
	@Length(max = 10)
	public String getCodigo() {
		return this.codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	@Column(name = "bsuspendida", nullable = false)
	public boolean isBsuspendida() {
		return this.bsuspendida;
	}

	public void setBsuspendida(boolean bsuspendida) {
		this.bsuspendida = bsuspendida;
	}

	@Column(name = "orden", nullable = false)
	public int getOrden() {
		return this.orden;
	}

	public void setOrden(int orden) {
		this.orden = orden;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "determinacion")
	public Set<Documentodeterminacion> getDocumentodeterminacions() {
		return this.documentodeterminacions;
	}

	public void setDocumentodeterminacions(
			Set<Documentodeterminacion> documentodeterminacions) {
		this.documentodeterminacions = documentodeterminacions;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "determinacionByIdpadre")
	public Set<Determinacion> getDeterminacionsForIdpadre() {
		return this.determinacionsForIdpadre;
	}

	public void setDeterminacionsForIdpadre(
			Set<Determinacion> determinacionsForIdpadre) {
		this.determinacionsForIdpadre = determinacionsForIdpadre;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "determinacion")
	public Set<Entidaddeterminacion> getEntidaddeterminacions() {
		return this.entidaddeterminacions;
	}

	public void setEntidaddeterminacions(
			Set<Entidaddeterminacion> entidaddeterminacions) {
		this.entidaddeterminacions = entidaddeterminacions;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "determinacion")
	public Set<Documentodeterminacion> getDocumentodeterminacions_1() {
		return this.documentodeterminacions_1;
	}

	public void setDocumentodeterminacions_1(
			Set<Documentodeterminacion> documentodeterminacions_1) {
		this.documentodeterminacions_1 = documentodeterminacions_1;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "determinacionByIddeterminacionoriginal")
	public Set<Determinacion> getDeterminacionsForIddeterminacionoriginal() {
		return this.determinacionsForIddeterminacionoriginal;
	}

	public void setDeterminacionsForIddeterminacionoriginal(
			Set<Determinacion> determinacionsForIddeterminacionoriginal) {
		this.determinacionsForIddeterminacionoriginal = determinacionsForIddeterminacionoriginal;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "determinacionByIddeterminacionvalorref")
	public Set<Opciondeterminacion> getOpciondeterminacionsForIddeterminacionvalorref() {
		return this.opciondeterminacionsForIddeterminacionvalorref;
	}

	public void setOpciondeterminacionsForIddeterminacionvalorref(
			Set<Opciondeterminacion> opciondeterminacionsForIddeterminacionvalorref) {
		this.opciondeterminacionsForIddeterminacionvalorref = opciondeterminacionsForIddeterminacionvalorref;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "determinacionByIddeterminacionoriginal")
	public Set<Determinacion> getDeterminacionsForIddeterminacionoriginal_1() {
		return this.determinacionsForIddeterminacionoriginal_1;
	}

	public void setDeterminacionsForIddeterminacionoriginal_1(
			Set<Determinacion> determinacionsForIddeterminacionoriginal_1) {
		this.determinacionsForIddeterminacionoriginal_1 = determinacionsForIddeterminacionoriginal_1;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "determinacionByIddeterminacion")
	public Set<Opciondeterminacion> getOpciondeterminacionsForIddeterminacion() {
		return this.opciondeterminacionsForIddeterminacion;
	}

	public void setOpciondeterminacionsForIddeterminacion(
			Set<Opciondeterminacion> opciondeterminacionsForIddeterminacion) {
		this.opciondeterminacionsForIddeterminacion = opciondeterminacionsForIddeterminacion;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "determinacionByIddeterminacion")
	public Set<Determinaciongrupoentidad> getDeterminaciongrupoentidadsForIddeterminacion() {
		return this.determinaciongrupoentidadsForIddeterminacion;
	}

	public void setDeterminaciongrupoentidadsForIddeterminacion(
			Set<Determinaciongrupoentidad> determinaciongrupoentidadsForIddeterminacion) {
		this.determinaciongrupoentidadsForIddeterminacion = determinaciongrupoentidadsForIddeterminacion;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "determinacion")
	public Set<Entidaddeterminacionregimen> getEntidaddeterminacionregimens() {
		return this.entidaddeterminacionregimens;
	}

	public void setEntidaddeterminacionregimens(
			Set<Entidaddeterminacionregimen> entidaddeterminacionregimens) {
		this.entidaddeterminacionregimens = entidaddeterminacionregimens;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "determinacionByIddeterminacionbase")
	public Set<Determinacion> getDeterminacionsForIddeterminacionbase() {
		return this.determinacionsForIddeterminacionbase;
	}

	public void setDeterminacionsForIddeterminacionbase(
			Set<Determinacion> determinacionsForIddeterminacionbase) {
		this.determinacionsForIddeterminacionbase = determinacionsForIddeterminacionbase;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "determinacionByIddeterminacionbase")
	public Set<Determinacion> getDeterminacionsForIddeterminacionbase_1() {
		return this.determinacionsForIddeterminacionbase_1;
	}

	public void setDeterminacionsForIddeterminacionbase_1(
			Set<Determinacion> determinacionsForIddeterminacionbase_1) {
		this.determinacionsForIddeterminacionbase_1 = determinacionsForIddeterminacionbase_1;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "determinacionByIddeterminaciongrupo")
	public Set<Determinaciongrupoentidad> getDeterminaciongrupoentidadsForIddeterminaciongrupo() {
		return this.determinaciongrupoentidadsForIddeterminaciongrupo;
	}

	public void setDeterminaciongrupoentidadsForIddeterminaciongrupo(
			Set<Determinaciongrupoentidad> determinaciongrupoentidadsForIddeterminaciongrupo) {
		this.determinaciongrupoentidadsForIddeterminaciongrupo = determinaciongrupoentidadsForIddeterminaciongrupo;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "determinacionByIddeterminacion")
	public Set<Opciondeterminacion> getOpciondeterminacionsForIddeterminacion_1() {
		return this.opciondeterminacionsForIddeterminacion_1;
	}

	public void setOpciondeterminacionsForIddeterminacion_1(
			Set<Opciondeterminacion> opciondeterminacionsForIddeterminacion_1) {
		this.opciondeterminacionsForIddeterminacion_1 = opciondeterminacionsForIddeterminacion_1;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "determinacionByIddeterminacionoperadora")
	public Set<Operaciondeterminacion> getOperaciondeterminacionsForIddeterminacionoperadora() {
		return this.operaciondeterminacionsForIddeterminacionoperadora;
	}

	public void setOperaciondeterminacionsForIddeterminacionoperadora(
			Set<Operaciondeterminacion> operaciondeterminacionsForIddeterminacionoperadora) {
		this.operaciondeterminacionsForIddeterminacionoperadora = operaciondeterminacionsForIddeterminacionoperadora;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "determinacionByIddeterminaciongrupo")
	public Set<Determinaciongrupoentidad> getDeterminaciongrupoentidadsForIddeterminaciongrupo_1() {
		return this.determinaciongrupoentidadsForIddeterminaciongrupo_1;
	}

	public void setDeterminaciongrupoentidadsForIddeterminaciongrupo_1(
			Set<Determinaciongrupoentidad> determinaciongrupoentidadsForIddeterminaciongrupo_1) {
		this.determinaciongrupoentidadsForIddeterminaciongrupo_1 = determinaciongrupoentidadsForIddeterminaciongrupo_1;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "determinacionByIddeterminacion")
	public Set<Determinaciongrupoentidad> getDeterminaciongrupoentidadsForIddeterminacion_1() {
		return this.determinaciongrupoentidadsForIddeterminacion_1;
	}

	public void setDeterminaciongrupoentidadsForIddeterminacion_1(
			Set<Determinaciongrupoentidad> determinaciongrupoentidadsForIddeterminacion_1) {
		this.determinaciongrupoentidadsForIddeterminacion_1 = determinaciongrupoentidadsForIddeterminacion_1;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "determinacionByIddeterminacionoperadora")
	public Set<Operaciondeterminacion> getOperaciondeterminacionsForIddeterminacionoperadora_1() {
		return this.operaciondeterminacionsForIddeterminacionoperadora_1;
	}

	public void setOperaciondeterminacionsForIddeterminacionoperadora_1(
			Set<Operaciondeterminacion> operaciondeterminacionsForIddeterminacionoperadora_1) {
		this.operaciondeterminacionsForIddeterminacionoperadora_1 = operaciondeterminacionsForIddeterminacionoperadora_1;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "determinacionByIddeterminacion")
	public Set<Operaciondeterminacion> getOperaciondeterminacionsForIddeterminacion() {
		return this.operaciondeterminacionsForIddeterminacion;
	}

	public void setOperaciondeterminacionsForIddeterminacion(
			Set<Operaciondeterminacion> operaciondeterminacionsForIddeterminacion) {
		this.operaciondeterminacionsForIddeterminacion = operaciondeterminacionsForIddeterminacion;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "determinacionByIdpadre")
	public Set<Determinacion> getDeterminacionsForIdpadre_1() {
		return this.determinacionsForIdpadre_1;
	}

	public void setDeterminacionsForIdpadre_1(
			Set<Determinacion> determinacionsForIdpadre_1) {
		this.determinacionsForIdpadre_1 = determinacionsForIdpadre_1;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "determinacionByIddeterminacion")
	public Set<Operaciondeterminacion> getOperaciondeterminacionsForIddeterminacion_1() {
		return this.operaciondeterminacionsForIddeterminacion_1;
	}

	public void setOperaciondeterminacionsForIddeterminacion_1(
			Set<Operaciondeterminacion> operaciondeterminacionsForIddeterminacion_1) {
		this.operaciondeterminacionsForIddeterminacion_1 = operaciondeterminacionsForIddeterminacion_1;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "determinacionByIddeterminacionvalorref")
	public Set<Opciondeterminacion> getOpciondeterminacionsForIddeterminacionvalorref_1() {
		return this.opciondeterminacionsForIddeterminacionvalorref_1;
	}

	public void setOpciondeterminacionsForIddeterminacionvalorref_1(
			Set<Opciondeterminacion> opciondeterminacionsForIddeterminacionvalorref_1) {
		this.opciondeterminacionsForIddeterminacionvalorref_1 = opciondeterminacionsForIddeterminacionvalorref_1;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "determinacion")
	public Set<Entidaddeterminacionregimen> getEntidaddeterminacionregimens_1() {
		return this.entidaddeterminacionregimens_1;
	}

	public void setEntidaddeterminacionregimens_1(
			Set<Entidaddeterminacionregimen> entidaddeterminacionregimens_1) {
		this.entidaddeterminacionregimens_1 = entidaddeterminacionregimens_1;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "determinacion")
	public Set<Entidaddeterminacion> getEntidaddeterminacions_1() {
		return this.entidaddeterminacions_1;
	}

	public void setEntidaddeterminacions_1(
			Set<Entidaddeterminacion> entidaddeterminacions_1) {
		this.entidaddeterminacions_1 = entidaddeterminacions_1;
	}
	
	
	
	// ---| TRANSIENTS |-----------------------
	


	String baseTramite;
	String baseDeterminacion;
	String valorReferenciaTramite;
	String valorReferenciaDeterminacion;
	String unidadTramite;
	String unidadDeterminacion;
	String grupoAplicacionTramite;
	String grupoAplicacionDeterminacion;
	
	@Transient
	public List<String> gruposAplicaciones = new ArrayList<String>();

	@Transient
	public String getBaseTramite() {
		return baseTramite;
	}
	public void setBaseTramite(String baseTramite) {
		this.baseTramite = baseTramite;
	}
	
	@Transient
	public String getValorReferenciaTramite() {
		return valorReferenciaTramite;
	}
	public void setValorReferenciaTramite(String valorReferenciaTramite) {
		this.valorReferenciaTramite = valorReferenciaTramite;
	}

	@Transient
	public String getValorReferenciaDeterminacion() {
		return valorReferenciaDeterminacion;
	}
	public void setValorReferenciaDeterminacion(String valorReferenciaDeterminacion) {
		this.valorReferenciaDeterminacion = valorReferenciaDeterminacion;
	}

	@Transient
	public String getBaseDeterminacion() {
		return baseDeterminacion;
	}
	public void setBaseDeterminacion(String baseEntidad) {
		this.baseDeterminacion = baseEntidad;
	}
	
	
	@Transient
	public String getUnidadTramite() {
		return unidadTramite;
	}
	public void setUnidadTramite(String unidadTramite) {
		this.unidadTramite = unidadTramite;
	}

	@Transient
	public String getUnidadDeterminacion() {
		return unidadDeterminacion;
	}
	public void setUnidadDeterminacion(String unidadDeterminacion) {
		this.unidadDeterminacion = unidadDeterminacion;
	}

	@Transient
	public String getGrupoAplicacionDeterminacion() {
		return grupoAplicacionDeterminacion;
	}
	public void setGrupoAplicacionDeterminacion(String grupoAplicacionDeterminacion) {
		this.grupoAplicacionDeterminacion = grupoAplicacionDeterminacion;
	}
	
	@Transient
	public String getGrupoAplicacionTramite() {
		return grupoAplicacionTramite;
	}
	public void setGrupoAplicacionTramite(String grupoAplicacionTramite) {
		this.grupoAplicacionTramite = grupoAplicacionTramite;
	}

}

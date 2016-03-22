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
import javax.persistence.Transient;

import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;

@Entity
@Table(name = "entidad", schema = "planeamiento")
@SequenceGenerator( name = "DETSEQ", sequenceName = "planeamiento.planeamiento_entidad_iden_seq",allocationSize=1 )
public class Entidad implements java.io.Serializable, Cloneable {

	private int iden;
	private Tramite tramite;
	private Entidad entidadByIdentidadoriginal;
	private Entidad entidadByIdentidadbase;
	private Entidad entidadByIdpadre;
	private String nombre;
	private String clave;
	private String etiqueta;
	private String codigo;
	private boolean bsuspendida;
	private int orden;
	private Set<Conjuntoentidad> conjuntoentidadsForIdentidadmiembro = new HashSet<Conjuntoentidad>(
			0);
	private Set<Documentoentidad> documentoentidads = new HashSet<Documentoentidad>(
			0);
	private Set<Documentoentidad> documentoentidads_1 = new HashSet<Documentoentidad>(
			0);
	private Set<Operacionentidad> operacionentidadsForIdentidad = new HashSet<Operacionentidad>(
			0);
	private Set<Ambitoaplicacionambito> ambitoaplicacionambitos = new HashSet<Ambitoaplicacionambito>(
			0);
	private Set<Entidad> entidadsForIdentidadoriginal = new HashSet<Entidad>(0);
	private Set<Operacionentidad> operacionentidadsForIdentidadoperadora = new HashSet<Operacionentidad>(
			0);
	private Set<Entidadlin> entidadlins = new HashSet<Entidadlin>(0);
	private Set<Planentidadordenacion> planentidadordenacions = new HashSet<Planentidadordenacion>(
			0);
	private Set<Entidad> entidadsForIdpadre = new HashSet<Entidad>(0);
	private Set<Entidad> entidadsForIdpadre_1 = new HashSet<Entidad>(0);
	private Set<Entidadpnt> entidadpnts = new HashSet<Entidadpnt>(0);
	private Set<Ambitoaplicacionambito> ambitoaplicacionambitos_1 = new HashSet<Ambitoaplicacionambito>(
			0);
	private Set<Entidadlin> entidadlins_1 = new HashSet<Entidadlin>(0);
	private Set<Planentidadordenacion> planentidadordenacions_1 = new HashSet<Planentidadordenacion>(
			0);
	private Set<Conjuntoentidad> conjuntoentidadsForIdentidadmiembro_1 = new HashSet<Conjuntoentidad>(
			0);
	private Set<Entidadpnt> entidadpnts_1 = new HashSet<Entidadpnt>(0);
	private Set<Entidaddeterminacion> entidaddeterminacions = new HashSet<Entidaddeterminacion>(
			0);
	private Set<Operacionentidad> operacionentidadsForIdentidad_1 = new HashSet<Operacionentidad>(
			0);
	private Set<Entidad> entidadsForIdentidadbase = new HashSet<Entidad>(0);
	private Set<Conjuntoentidad> conjuntoentidadsForIdentidadconjunto = new HashSet<Conjuntoentidad>(
			0);
	private Set<Entidaddeterminacion> entidaddeterminacions_1 = new HashSet<Entidaddeterminacion>(
			0);
	private Set<Conjuntoentidad> conjuntoentidadsForIdentidadconjunto_1 = new HashSet<Conjuntoentidad>(
			0);
	private Set<Entidad> entidadsForIdentidadbase_1 = new HashSet<Entidad>(0);
	private Set<Entidad> entidadsForIdentidadoriginal_1 = new HashSet<Entidad>(
			0);
	private Set<Entidadpol> entidadpols = new HashSet<Entidadpol>(0);
	private Set<Entidadpol> entidadpols_1 = new HashSet<Entidadpol>(0);
	private Set<Operacionentidad> operacionentidadsForIdentidadoperadora_1 = new HashSet<Operacionentidad>(
			0);

	public Entidad() {
	}
	
	public Entidad clone() throws CloneNotSupportedException {
        return (Entidad) super.clone();
}

	public Entidad(int iden, Tramite tramite, String codigo,
			boolean bsuspendida, int orden) {
		this.iden = iden;
		this.tramite = tramite;
		this.codigo = codigo;
		this.bsuspendida = bsuspendida;
		this.orden = orden;
	}

	public Entidad(int iden, Tramite tramite,
			Entidad entidadByIdentidadoriginal, Entidad entidadByIdentidadbase,
			Entidad entidadByIdpadre, String nombre, String clave,
			String etiqueta, String codigo, boolean bsuspendida, int orden,
			Set<Conjuntoentidad> conjuntoentidadsForIdentidadmiembro,
			Set<Documentoentidad> documentoentidads,
			Set<Documentoentidad> documentoentidads_1,
			Set<Operacionentidad> operacionentidadsForIdentidad,
			Set<Ambitoaplicacionambito> ambitoaplicacionambitos,
			Set<Entidad> entidadsForIdentidadoriginal,
			Set<Operacionentidad> operacionentidadsForIdentidadoperadora,
			Set<Entidadlin> entidadlins,
			Set<Planentidadordenacion> planentidadordenacions,
			Set<Entidad> entidadsForIdpadre, Set<Entidad> entidadsForIdpadre_1,
			Set<Entidadpnt> entidadpnts,
			Set<Ambitoaplicacionambito> ambitoaplicacionambitos_1,
			Set<Entidadlin> entidadlins_1,
			Set<Planentidadordenacion> planentidadordenacions_1,
			Set<Conjuntoentidad> conjuntoentidadsForIdentidadmiembro_1,
			Set<Entidadpnt> entidadpnts_1,
			Set<Entidaddeterminacion> entidaddeterminacions,
			Set<Operacionentidad> operacionentidadsForIdentidad_1,
			Set<Entidad> entidadsForIdentidadbase,
			Set<Conjuntoentidad> conjuntoentidadsForIdentidadconjunto,
			Set<Entidaddeterminacion> entidaddeterminacions_1,
			Set<Conjuntoentidad> conjuntoentidadsForIdentidadconjunto_1,
			Set<Entidad> entidadsForIdentidadbase_1,
			Set<Entidad> entidadsForIdentidadoriginal_1,
			Set<Entidadpol> entidadpols, Set<Entidadpol> entidadpols_1,
			Set<Operacionentidad> operacionentidadsForIdentidadoperadora_1) {
		this.iden = iden;
		this.tramite = tramite;
		this.entidadByIdentidadoriginal = entidadByIdentidadoriginal;
		this.entidadByIdentidadbase = entidadByIdentidadbase;
		this.entidadByIdpadre = entidadByIdpadre;
		this.nombre = nombre;
		this.clave = clave;
		this.etiqueta = etiqueta;
		this.codigo = codigo;
		this.bsuspendida = bsuspendida;
		this.orden = orden;
		this.conjuntoentidadsForIdentidadmiembro = conjuntoentidadsForIdentidadmiembro;
		this.documentoentidads = documentoentidads;
		this.documentoentidads_1 = documentoentidads_1;
		this.operacionentidadsForIdentidad = operacionentidadsForIdentidad;
		this.ambitoaplicacionambitos = ambitoaplicacionambitos;
		this.entidadsForIdentidadoriginal = entidadsForIdentidadoriginal;
		this.operacionentidadsForIdentidadoperadora = operacionentidadsForIdentidadoperadora;
		this.entidadlins = entidadlins;
		this.planentidadordenacions = planentidadordenacions;
		this.entidadsForIdpadre = entidadsForIdpadre;
		this.entidadsForIdpadre_1 = entidadsForIdpadre_1;
		this.entidadpnts = entidadpnts;
		this.ambitoaplicacionambitos_1 = ambitoaplicacionambitos_1;
		this.entidadlins_1 = entidadlins_1;
		this.planentidadordenacions_1 = planentidadordenacions_1;
		this.conjuntoentidadsForIdentidadmiembro_1 = conjuntoentidadsForIdentidadmiembro_1;
		this.entidadpnts_1 = entidadpnts_1;
		this.entidaddeterminacions = entidaddeterminacions;
		this.operacionentidadsForIdentidad_1 = operacionentidadsForIdentidad_1;
		this.entidadsForIdentidadbase = entidadsForIdentidadbase;
		this.conjuntoentidadsForIdentidadconjunto = conjuntoentidadsForIdentidadconjunto;
		this.entidaddeterminacions_1 = entidaddeterminacions_1;
		this.conjuntoentidadsForIdentidadconjunto_1 = conjuntoentidadsForIdentidadconjunto_1;
		this.entidadsForIdentidadbase_1 = entidadsForIdentidadbase_1;
		this.entidadsForIdentidadoriginal_1 = entidadsForIdentidadoriginal_1;
		this.entidadpols = entidadpols;
		this.entidadpols_1 = entidadpols_1;
		this.operacionentidadsForIdentidadoperadora_1 = operacionentidadsForIdentidadoperadora_1;
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
	
	private Determinacion grupo = null;
	
	

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
	@JoinColumn(name = "identidadoriginal")
	public Entidad getEntidadByIdentidadoriginal() {
		return this.entidadByIdentidadoriginal;
	}

	public void setEntidadByIdentidadoriginal(Entidad entidadByIdentidadoriginal) {
		this.entidadByIdentidadoriginal = entidadByIdentidadoriginal;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "identidadbase")
	public Entidad getEntidadByIdentidadbase() {
		return this.entidadByIdentidadbase;
	}

	public void setEntidadByIdentidadbase(Entidad entidadByIdentidadbase) {
		this.entidadByIdentidadbase = entidadByIdentidadbase;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idpadre")
	public Entidad getEntidadByIdpadre() {
		return this.entidadByIdpadre;
	}

	public void setEntidadByIdpadre(Entidad entidadByIdpadre) {
		this.entidadByIdpadre = entidadByIdpadre;
	}

	@Column(name = "nombre")
	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	@Column(name = "clave", length = 150)
	@Length(max = 150)
	public String getClave() {
		return this.clave;
	}

	public void setClave(String clave) {
		this.clave = clave;
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

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "entidadByIdentidadmiembro")
	public Set<Conjuntoentidad> getConjuntoentidadsForIdentidadmiembro() {
		return this.conjuntoentidadsForIdentidadmiembro;
	}

	public void setConjuntoentidadsForIdentidadmiembro(
			Set<Conjuntoentidad> conjuntoentidadsForIdentidadmiembro) {
		this.conjuntoentidadsForIdentidadmiembro = conjuntoentidadsForIdentidadmiembro;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "entidad")
	public Set<Documentoentidad> getDocumentoentidads() {
		return this.documentoentidads;
	}

	public void setDocumentoentidads(Set<Documentoentidad> documentoentidads) {
		this.documentoentidads = documentoentidads;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "entidad")
	public Set<Documentoentidad> getDocumentoentidads_1() {
		return this.documentoentidads_1;
	}

	public void setDocumentoentidads_1(Set<Documentoentidad> documentoentidads_1) {
		this.documentoentidads_1 = documentoentidads_1;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "entidadByIdentidad")
	public Set<Operacionentidad> getOperacionentidadsForIdentidad() {
		return this.operacionentidadsForIdentidad;
	}

	public void setOperacionentidadsForIdentidad(
			Set<Operacionentidad> operacionentidadsForIdentidad) {
		this.operacionentidadsForIdentidad = operacionentidadsForIdentidad;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "entidad")
	public Set<Ambitoaplicacionambito> getAmbitoaplicacionambitos() {
		return this.ambitoaplicacionambitos;
	}

	public void setAmbitoaplicacionambitos(
			Set<Ambitoaplicacionambito> ambitoaplicacionambitos) {
		this.ambitoaplicacionambitos = ambitoaplicacionambitos;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "entidadByIdentidadoriginal")
	public Set<Entidad> getEntidadsForIdentidadoriginal() {
		return this.entidadsForIdentidadoriginal;
	}

	public void setEntidadsForIdentidadoriginal(
			Set<Entidad> entidadsForIdentidadoriginal) {
		this.entidadsForIdentidadoriginal = entidadsForIdentidadoriginal;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "entidadByIdentidadoperadora")
	public Set<Operacionentidad> getOperacionentidadsForIdentidadoperadora() {
		return this.operacionentidadsForIdentidadoperadora;
	}

	public void setOperacionentidadsForIdentidadoperadora(
			Set<Operacionentidad> operacionentidadsForIdentidadoperadora) {
		this.operacionentidadsForIdentidadoperadora = operacionentidadsForIdentidadoperadora;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "entidad")
	public Set<Entidadlin> getEntidadlins() {
		return this.entidadlins;
	}

	public void setEntidadlins(Set<Entidadlin> entidadlins) {
		this.entidadlins = entidadlins;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "entidad")
	public Set<Planentidadordenacion> getPlanentidadordenacions() {
		return this.planentidadordenacions;
	}

	public void setPlanentidadordenacions(
			Set<Planentidadordenacion> planentidadordenacions) {
		this.planentidadordenacions = planentidadordenacions;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "entidadByIdpadre")
	public Set<Entidad> getEntidadsForIdpadre() {
		return this.entidadsForIdpadre;
	}

	public void setEntidadsForIdpadre(Set<Entidad> entidadsForIdpadre) {
		this.entidadsForIdpadre = entidadsForIdpadre;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "entidadByIdpadre")
	public Set<Entidad> getEntidadsForIdpadre_1() {
		return this.entidadsForIdpadre_1;
	}

	public void setEntidadsForIdpadre_1(Set<Entidad> entidadsForIdpadre_1) {
		this.entidadsForIdpadre_1 = entidadsForIdpadre_1;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "entidad")
	public Set<Entidadpnt> getEntidadpnts() {
		return this.entidadpnts;
	}

	public void setEntidadpnts(Set<Entidadpnt> entidadpnts) {
		this.entidadpnts = entidadpnts;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "entidad")
	public Set<Ambitoaplicacionambito> getAmbitoaplicacionambitos_1() {
		return this.ambitoaplicacionambitos_1;
	}

	public void setAmbitoaplicacionambitos_1(
			Set<Ambitoaplicacionambito> ambitoaplicacionambitos_1) {
		this.ambitoaplicacionambitos_1 = ambitoaplicacionambitos_1;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "entidad")
	public Set<Entidadlin> getEntidadlins_1() {
		return this.entidadlins_1;
	}

	public void setEntidadlins_1(Set<Entidadlin> entidadlins_1) {
		this.entidadlins_1 = entidadlins_1;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "entidad")
	public Set<Planentidadordenacion> getPlanentidadordenacions_1() {
		return this.planentidadordenacions_1;
	}

	public void setPlanentidadordenacions_1(
			Set<Planentidadordenacion> planentidadordenacions_1) {
		this.planentidadordenacions_1 = planentidadordenacions_1;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "entidadByIdentidadmiembro")
	public Set<Conjuntoentidad> getConjuntoentidadsForIdentidadmiembro_1() {
		return this.conjuntoentidadsForIdentidadmiembro_1;
	}

	public void setConjuntoentidadsForIdentidadmiembro_1(
			Set<Conjuntoentidad> conjuntoentidadsForIdentidadmiembro_1) {
		this.conjuntoentidadsForIdentidadmiembro_1 = conjuntoentidadsForIdentidadmiembro_1;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "entidad")
	public Set<Entidadpnt> getEntidadpnts_1() {
		return this.entidadpnts_1;
	}

	public void setEntidadpnts_1(Set<Entidadpnt> entidadpnts_1) {
		this.entidadpnts_1 = entidadpnts_1;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "entidad")
	public Set<Entidaddeterminacion> getEntidaddeterminacions() {
		return this.entidaddeterminacions;
	}

	public void setEntidaddeterminacions(
			Set<Entidaddeterminacion> entidaddeterminacions) {
		this.entidaddeterminacions = entidaddeterminacions;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "entidadByIdentidad")
	public Set<Operacionentidad> getOperacionentidadsForIdentidad_1() {
		return this.operacionentidadsForIdentidad_1;
	}

	public void setOperacionentidadsForIdentidad_1(
			Set<Operacionentidad> operacionentidadsForIdentidad_1) {
		this.operacionentidadsForIdentidad_1 = operacionentidadsForIdentidad_1;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "entidadByIdentidadbase")
	public Set<Entidad> getEntidadsForIdentidadbase() {
		return this.entidadsForIdentidadbase;
	}

	public void setEntidadsForIdentidadbase(
			Set<Entidad> entidadsForIdentidadbase) {
		this.entidadsForIdentidadbase = entidadsForIdentidadbase;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "entidadByIdentidadconjunto")
	public Set<Conjuntoentidad> getConjuntoentidadsForIdentidadconjunto() {
		return this.conjuntoentidadsForIdentidadconjunto;
	}

	public void setConjuntoentidadsForIdentidadconjunto(
			Set<Conjuntoentidad> conjuntoentidadsForIdentidadconjunto) {
		this.conjuntoentidadsForIdentidadconjunto = conjuntoentidadsForIdentidadconjunto;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "entidad")
	public Set<Entidaddeterminacion> getEntidaddeterminacions_1() {
		return this.entidaddeterminacions_1;
	}

	public void setEntidaddeterminacions_1(
			Set<Entidaddeterminacion> entidaddeterminacions_1) {
		this.entidaddeterminacions_1 = entidaddeterminacions_1;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "entidadByIdentidadconjunto")
	public Set<Conjuntoentidad> getConjuntoentidadsForIdentidadconjunto_1() {
		return this.conjuntoentidadsForIdentidadconjunto_1;
	}

	public void setConjuntoentidadsForIdentidadconjunto_1(
			Set<Conjuntoentidad> conjuntoentidadsForIdentidadconjunto_1) {
		this.conjuntoentidadsForIdentidadconjunto_1 = conjuntoentidadsForIdentidadconjunto_1;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "entidadByIdentidadbase")
	public Set<Entidad> getEntidadsForIdentidadbase_1() {
		return this.entidadsForIdentidadbase_1;
	}

	public void setEntidadsForIdentidadbase_1(
			Set<Entidad> entidadsForIdentidadbase_1) {
		this.entidadsForIdentidadbase_1 = entidadsForIdentidadbase_1;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "entidadByIdentidadoriginal")
	public Set<Entidad> getEntidadsForIdentidadoriginal_1() {
		return this.entidadsForIdentidadoriginal_1;
	}

	public void setEntidadsForIdentidadoriginal_1(
			Set<Entidad> entidadsForIdentidadoriginal_1) {
		this.entidadsForIdentidadoriginal_1 = entidadsForIdentidadoriginal_1;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "entidad")
	public Set<Entidadpol> getEntidadpols() {
		return this.entidadpols;
	}

	public void setEntidadpols(Set<Entidadpol> entidadpols) {
		this.entidadpols = entidadpols;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "entidad")
	public Set<Entidadpol> getEntidadpols_1() {
		return this.entidadpols_1;
	}

	public void setEntidadpols_1(Set<Entidadpol> entidadpols_1) {
		this.entidadpols_1 = entidadpols_1;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "entidadByIdentidadoperadora")
	public Set<Operacionentidad> getOperacionentidadsForIdentidadoperadora_1() {
		return this.operacionentidadsForIdentidadoperadora_1;
	}

	public void setOperacionentidadsForIdentidadoperadora_1(
			Set<Operacionentidad> operacionentidadsForIdentidadoperadora_1) {
		this.operacionentidadsForIdentidadoperadora_1 = operacionentidadsForIdentidadoperadora_1;
	}

	
	// ---| TRANSIENTS |-----------------------
	
	String baseTramite;
	String baseEntidad;

	@Transient
	public String getBaseTramite() {
		return baseTramite;
	}
	public void setBaseTramite(String baseTramite) {
		this.baseTramite = baseTramite;
	}

	@Transient
	public String getBaseEntidad() {
		return baseEntidad;
	}
	public void setBaseEntidad(String baseEntidad) {
		this.baseEntidad = baseEntidad;
	}
	
	
}

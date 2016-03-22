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
@Table(name = "plan", schema = "planeamiento")
@SequenceGenerator( name = "DETSEQ", sequenceName = "planeamiento.planeamiento_plan_iden_seq",allocationSize=1 )
public class Plan implements java.io.Serializable {

	private int iden;
	private Plan planByIdpadre;
	private Plan planByIdplanbase;
	private String nombre;
	private String codigo;
	private String texto;
	private int idambito;
	private boolean bsuspendido;
	private int orden;
	private Set<Planentidadordenacion> planentidadordenacions = new HashSet<Planentidadordenacion>(
			0);
	private Set<Plan> plansForIdplanbase = new HashSet<Plan>(0);
	private Set<Operacionplan> operacionplansForIdplanoperado = new HashSet<Operacionplan>(
			0);
	private Set<Operacionplan> operacionplansForIdplanoperado_1 = new HashSet<Operacionplan>(
			0);
	private Set<Plan> plansForIdpadre = new HashSet<Plan>(0);
	private Set<Plan> plansForIdpadre_1 = new HashSet<Plan>(0);
	private Set<Plan> plansForIdplanbase_1 = new HashSet<Plan>(0);
	private Set<Planentidadordenacion> planentidadordenacions_1 = new HashSet<Planentidadordenacion>(
			0);
	private Set<Operacionplan> operacionplansForIdplanoperador = new HashSet<Operacionplan>(
			0);
	private Set<Planshp> planshps = new HashSet<Planshp>(0);
	private Set<Planshp> planshps_1 = new HashSet<Planshp>(0);
	private Set<Tramite> tramites = new HashSet<Tramite>(0);
	private Set<Operacionplan> operacionplansForIdplanoperador_1 = new HashSet<Operacionplan>(
			0);
	private Set<Tramite> tramites_1 = new HashSet<Tramite>(0);

	public Plan() {
	}

	public Plan(int iden, String nombre, String codigo, int idambito,
			boolean bsuspendido, int orden) {
		this.iden = iden;
		this.nombre = nombre;
		this.codigo = codigo;
		this.idambito = idambito;
		this.bsuspendido = bsuspendido;
		this.orden = orden;
	}

	public Plan(int iden, Plan planByIdpadre, Plan planByIdplanbase,
			String nombre, String codigo, String texto, int idambito,
			boolean bsuspendido, int orden,
			Set<Planentidadordenacion> planentidadordenacions,
			Set<Plan> plansForIdplanbase,
			Set<Operacionplan> operacionplansForIdplanoperado,
			Set<Operacionplan> operacionplansForIdplanoperado_1,
			Set<Plan> plansForIdpadre, Set<Plan> plansForIdpadre_1,
			Set<Plan> plansForIdplanbase_1,
			Set<Planentidadordenacion> planentidadordenacions_1,
			Set<Operacionplan> operacionplansForIdplanoperador,
			Set<Planshp> planshps, Set<Planshp> planshps_1,
			Set<Tramite> tramites,
			Set<Operacionplan> operacionplansForIdplanoperador_1,
			Set<Tramite> tramites_1) {
		this.iden = iden;
		this.planByIdpadre = planByIdpadre;
		this.planByIdplanbase = planByIdplanbase;
		this.nombre = nombre;
		this.codigo = codigo;
		this.texto = texto;
		this.idambito = idambito;
		this.bsuspendido = bsuspendido;
		this.orden = orden;
		this.planentidadordenacions = planentidadordenacions;
		this.plansForIdplanbase = plansForIdplanbase;
		this.operacionplansForIdplanoperado = operacionplansForIdplanoperado;
		this.operacionplansForIdplanoperado_1 = operacionplansForIdplanoperado_1;
		this.plansForIdpadre = plansForIdpadre;
		this.plansForIdpadre_1 = plansForIdpadre_1;
		this.plansForIdplanbase_1 = plansForIdplanbase_1;
		this.planentidadordenacions_1 = planentidadordenacions_1;
		this.operacionplansForIdplanoperador = operacionplansForIdplanoperador;
		this.planshps = planshps;
		this.planshps_1 = planshps_1;
		this.tramites = tramites;
		this.operacionplansForIdplanoperador_1 = operacionplansForIdplanoperador_1;
		this.tramites_1 = tramites_1;
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
	@JoinColumn(name = "idpadre")
	public Plan getPlanByIdpadre() {
		return this.planByIdpadre;
	}

	public void setPlanByIdpadre(Plan planByIdpadre) {
		this.planByIdpadre = planByIdpadre;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idplanbase")
	public Plan getPlanByIdplanbase() {
		return this.planByIdplanbase;
	}

	public void setPlanByIdplanbase(Plan planByIdplanbase) {
		this.planByIdplanbase = planByIdplanbase;
	}

	@Column(name = "nombre", nullable = false)
	@NotNull
	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	@Column(name = "codigo", nullable = false, length = 5)
	@NotNull
	@Length(max = 5)
	public String getCodigo() {
		return this.codigo;
	}

	public void setCodigo(String codigo) {		
		this.codigo = codigo;
	}

	@Column(name = "texto")
	public String getTexto() {
		return this.texto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}

	@Column(name = "idambito", nullable = false)
	public int getIdambito() {
		return this.idambito;
	}

	public void setIdambito(int idambito) {
		this.idambito = idambito;
	}

	@Column(name = "bsuspendido", nullable = false)
	public boolean isBsuspendido() {
		return this.bsuspendido;
	}

	public void setBsuspendido(boolean bsuspendido) {
		this.bsuspendido = bsuspendido;
	}

	@Column(name = "orden", nullable = false)
	public int getOrden() {
		return this.orden;
	}

	public void setOrden(int orden) {
		this.orden = orden;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "plan")
	public Set<Planentidadordenacion> getPlanentidadordenacions() {
		return this.planentidadordenacions;
	}

	public void setPlanentidadordenacions(
			Set<Planentidadordenacion> planentidadordenacions) {
		this.planentidadordenacions = planentidadordenacions;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "planByIdplanbase")
	public Set<Plan> getPlansForIdplanbase() {
		return this.plansForIdplanbase;
	}

	public void setPlansForIdplanbase(Set<Plan> plansForIdplanbase) {
		this.plansForIdplanbase = plansForIdplanbase;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "planByIdplanoperado")
	public Set<Operacionplan> getOperacionplansForIdplanoperado() {
		return this.operacionplansForIdplanoperado;
	}

	public void setOperacionplansForIdplanoperado(
			Set<Operacionplan> operacionplansForIdplanoperado) {
		this.operacionplansForIdplanoperado = operacionplansForIdplanoperado;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "planByIdplanoperado")
	public Set<Operacionplan> getOperacionplansForIdplanoperado_1() {
		return this.operacionplansForIdplanoperado_1;
	}

	public void setOperacionplansForIdplanoperado_1(
			Set<Operacionplan> operacionplansForIdplanoperado_1) {
		this.operacionplansForIdplanoperado_1 = operacionplansForIdplanoperado_1;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "planByIdpadre")
	public Set<Plan> getPlansForIdpadre() {
		return this.plansForIdpadre;
	}

	public void setPlansForIdpadre(Set<Plan> plansForIdpadre) {
		this.plansForIdpadre = plansForIdpadre;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "planByIdpadre")
	public Set<Plan> getPlansForIdpadre_1() {
		return this.plansForIdpadre_1;
	}

	public void setPlansForIdpadre_1(Set<Plan> plansForIdpadre_1) {
		this.plansForIdpadre_1 = plansForIdpadre_1;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "planByIdplanbase")
	public Set<Plan> getPlansForIdplanbase_1() {
		return this.plansForIdplanbase_1;
	}

	public void setPlansForIdplanbase_1(Set<Plan> plansForIdplanbase_1) {
		this.plansForIdplanbase_1 = plansForIdplanbase_1;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "plan")
	public Set<Planentidadordenacion> getPlanentidadordenacions_1() {
		return this.planentidadordenacions_1;
	}

	public void setPlanentidadordenacions_1(
			Set<Planentidadordenacion> planentidadordenacions_1) {
		this.planentidadordenacions_1 = planentidadordenacions_1;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "planByIdplanoperador")
	public Set<Operacionplan> getOperacionplansForIdplanoperador() {
		return this.operacionplansForIdplanoperador;
	}

	public void setOperacionplansForIdplanoperador(
			Set<Operacionplan> operacionplansForIdplanoperador) {
		this.operacionplansForIdplanoperador = operacionplansForIdplanoperador;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "plan")
	public Set<Planshp> getPlanshps() {
		return this.planshps;
	}

	public void setPlanshps(Set<Planshp> planshps) {
		this.planshps = planshps;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "plan")
	public Set<Planshp> getPlanshps_1() {
		return this.planshps_1;
	}

	public void setPlanshps_1(Set<Planshp> planshps_1) {
		this.planshps_1 = planshps_1;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "plan")
	public Set<Tramite> getTramites() {
		return this.tramites;
	}

	public void setTramites(Set<Tramite> tramites) {
		this.tramites = tramites;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "planByIdplanoperador")
	public Set<Operacionplan> getOperacionplansForIdplanoperador_1() {
		return this.operacionplansForIdplanoperador_1;
	}

	public void setOperacionplansForIdplanoperador_1(
			Set<Operacionplan> operacionplansForIdplanoperador_1) {
		this.operacionplansForIdplanoperador_1 = operacionplansForIdplanoperador_1;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "plan")
	public Set<Tramite> getTramites_1() {
		return this.tramites_1;
	}

	public void setTramites_1(Set<Tramite> tramites_1) {
		this.tramites_1 = tramites_1;
	}

}

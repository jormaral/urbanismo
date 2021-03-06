package es.mitc.redes.urbanismoenred.data.rpm.refundido;

// Generated 03-abr-2012 12:30:29 by Hibernate Tools 3.4.0.CR1

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Entidaddeterminacionregimen generated by hbm2java
 */
@Entity
@Table(name = "entidaddeterminacionregimen")
public class Entidaddeterminacionregimen implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2102492270135642884L;
	private int iden;
	private Opciondeterminacion opciondeterminacion;
	private Casoentidaddeterminacion casoentidaddeterminacionByIdcasoaplicacion;
	private Casoentidaddeterminacion casoentidaddeterminacionByIdcaso;
	private Determinacion determinacion;
	private String valor;
	private Integer superposicion;
	private Set<Regimenespecifico> regimenespecificos = new HashSet<Regimenespecifico>(
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
			Set<Regimenespecifico> regimenespecificos) {
		this.iden = iden;
		this.opciondeterminacion = opciondeterminacion;
		this.casoentidaddeterminacionByIdcasoaplicacion = casoentidaddeterminacionByIdcasoaplicacion;
		this.casoentidaddeterminacionByIdcaso = casoentidaddeterminacionByIdcaso;
		this.determinacion = determinacion;
		this.valor = valor;
		this.superposicion = superposicion;
		this.regimenespecificos = regimenespecificos;
	}

	@Id
	@Column(name = "iden", unique = true, nullable = false)
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

	@Column(name = "valor", length = 50)
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

}

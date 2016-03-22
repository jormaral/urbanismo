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
 * Opciondeterminacion generated by hbm2java
 */
@Entity
@Table(name = "opciondeterminacion")
public class Opciondeterminacion implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5775792484114107326L;
	private int iden;
	private Determinacion determinacionByIddeterminacion;
	private Determinacion determinacionByIddeterminacionvalorref;
	private Set<Entidaddeterminacionregimen> entidaddeterminacionregimens = new HashSet<Entidaddeterminacionregimen>(
			0);

	public Opciondeterminacion() {
	}

	public Opciondeterminacion(int iden,
			Determinacion determinacionByIddeterminacion,
			Determinacion determinacionByIddeterminacionvalorref) {
		this.iden = iden;
		this.determinacionByIddeterminacion = determinacionByIddeterminacion;
		this.determinacionByIddeterminacionvalorref = determinacionByIddeterminacionvalorref;
	}

	public Opciondeterminacion(int iden,
			Determinacion determinacionByIddeterminacion,
			Determinacion determinacionByIddeterminacionvalorref,
			Set<Entidaddeterminacionregimen> entidaddeterminacionregimens) {
		this.iden = iden;
		this.determinacionByIddeterminacion = determinacionByIddeterminacion;
		this.determinacionByIddeterminacionvalorref = determinacionByIddeterminacionvalorref;
		this.entidaddeterminacionregimens = entidaddeterminacionregimens;
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
	@JoinColumn(name = "iddeterminacion", nullable = false)
	public Determinacion getDeterminacionByIddeterminacion() {
		return this.determinacionByIddeterminacion;
	}

	public void setDeterminacionByIddeterminacion(
			Determinacion determinacionByIddeterminacion) {
		this.determinacionByIddeterminacion = determinacionByIddeterminacion;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "iddeterminacionvalorref", nullable = false)
	public Determinacion getDeterminacionByIddeterminacionvalorref() {
		return this.determinacionByIddeterminacionvalorref;
	}

	public void setDeterminacionByIddeterminacionvalorref(
			Determinacion determinacionByIddeterminacionvalorref) {
		this.determinacionByIddeterminacionvalorref = determinacionByIddeterminacionvalorref;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "opciondeterminacion")
	public Set<Entidaddeterminacionregimen> getEntidaddeterminacionregimens() {
		return this.entidaddeterminacionregimens;
	}

	public void setEntidaddeterminacionregimens(
			Set<Entidaddeterminacionregimen> entidaddeterminacionregimens) {
		this.entidaddeterminacionregimens = entidaddeterminacionregimens;
	}

}

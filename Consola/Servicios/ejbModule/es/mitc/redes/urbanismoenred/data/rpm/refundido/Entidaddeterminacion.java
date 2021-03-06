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
 * Entidaddeterminacion generated by hbm2java
 */
@Entity
@Table(name = "entidaddeterminacion")
public class Entidaddeterminacion implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1295175083497869815L;
	private int iden;
	private Entidad entidad;
	private Determinacion determinacion;
	private Set<Casoentidaddeterminacion> casoentidaddeterminacions = new HashSet<Casoentidaddeterminacion>(
			0);

	public Entidaddeterminacion() {
	}

	public Entidaddeterminacion(int iden, Entidad entidad,
			Determinacion determinacion) {
		this.iden = iden;
		this.entidad = entidad;
		this.determinacion = determinacion;
	}

	public Entidaddeterminacion(int iden, Entidad entidad,
			Determinacion determinacion,
			Set<Casoentidaddeterminacion> casoentidaddeterminacions) {
		this.iden = iden;
		this.entidad = entidad;
		this.determinacion = determinacion;
		this.casoentidaddeterminacions = casoentidaddeterminacions;
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
	@JoinColumn(name = "identidad", nullable = false)
	public Entidad getEntidad() {
		return this.entidad;
	}

	public void setEntidad(Entidad entidad) {
		this.entidad = entidad;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "iddeterminacion", nullable = false)
	public Determinacion getDeterminacion() {
		return this.determinacion;
	}

	public void setDeterminacion(Determinacion determinacion) {
		this.determinacion = determinacion;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "entidaddeterminacion")
	public Set<Casoentidaddeterminacion> getCasoentidaddeterminacions() {
		return this.casoentidaddeterminacions;
	}

	public void setCasoentidaddeterminacions(
			Set<Casoentidaddeterminacion> casoentidaddeterminacions) {
		this.casoentidaddeterminacions = casoentidaddeterminacions;
	}

}

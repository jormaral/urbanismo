package es.mitc.redes.urbanismoenred.data.rpm.refundido;

// Generated 03-abr-2012 12:30:29 by Hibernate Tools 3.4.0.CR1

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Entidadpol generated by hbm2java
 */
@Entity
@Table(name = "entidadpol")
public class Entidadpol implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4773505628100701792L;
	private int iden;
	private Entidad entidad;
	private String geom;
	private boolean bsuspendida;

	public Entidadpol() {
	}

	public Entidadpol(int iden, Entidad entidad, boolean bsuspendida) {
		this.iden = iden;
		this.entidad = entidad;
		this.bsuspendida = bsuspendida;
	}

	public Entidadpol(int iden, Entidad entidad, String geom,
			boolean bsuspendida) {
		this.iden = iden;
		this.entidad = entidad;
		this.geom = geom;
		this.bsuspendida = bsuspendida;
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

	@Column(name = "geom")
	public String getGeom() {
		return this.geom;
	}

	public void setGeom(String geom) {
		this.geom = geom;
	}

	@Column(name = "bsuspendida", nullable = false)
	public boolean isBsuspendida() {
		return this.bsuspendida;
	}

	public void setBsuspendida(boolean bsuspendida) {
		this.bsuspendida = bsuspendida;
	}

}
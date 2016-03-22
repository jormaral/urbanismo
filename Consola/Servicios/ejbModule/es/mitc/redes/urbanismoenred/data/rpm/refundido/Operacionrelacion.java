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
 * Operacionrelacion generated by hbm2java
 */
@Entity
@Table(name = "operacionrelacion")
public class Operacionrelacion implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5518000497958584525L;
	private int iden;
	private Relacion relacion;
	private Operacion operacion;
	private int idtipooperacionrel;

	public Operacionrelacion() {
	}

	public Operacionrelacion(int iden, Relacion relacion, Operacion operacion,
			int idtipooperacionrel) {
		this.iden = iden;
		this.relacion = relacion;
		this.operacion = operacion;
		this.idtipooperacionrel = idtipooperacionrel;
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
	@JoinColumn(name = "idrelacion", nullable = false)
	public Relacion getRelacion() {
		return this.relacion;
	}

	public void setRelacion(Relacion relacion) {
		this.relacion = relacion;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idoperacion", nullable = false)
	public Operacion getOperacion() {
		return this.operacion;
	}

	public void setOperacion(Operacion operacion) {
		this.operacion = operacion;
	}

	@Column(name = "idtipooperacionrel", nullable = false)
	public int getIdtipooperacionrel() {
		return this.idtipooperacionrel;
	}

	public void setIdtipooperacionrel(int idtipooperacionrel) {
		this.idtipooperacionrel = idtipooperacionrel;
	}

}
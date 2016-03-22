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
 * Ambitoaplicacionambito generated by hbm2java
 */
@Entity
@Table(name = "ambitoaplicacionambito")
public class Ambitoaplicacionambito implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3683679696594751562L;
	private int iden;
	private Entidad entidad;
	private int idambito;

	public Ambitoaplicacionambito() {
	}

	public Ambitoaplicacionambito(int iden, Entidad entidad, int idambito) {
		this.iden = iden;
		this.entidad = entidad;
		this.idambito = idambito;
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

	@Column(name = "idambito", nullable = false)
	public int getIdambito() {
		return this.idambito;
	}

	public void setIdambito(int idambito) {
		this.idambito = idambito;
	}

}

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
 * Documentoentidad generated by hbm2java
 */
@Entity
@Table(name = "documentoentidad")
public class Documentoentidad implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5477949846826030522L;
	private int iden;
	private Documento documento;
	private Entidad entidad;

	public Documentoentidad() {
	}

	public Documentoentidad(int iden, Documento documento, Entidad entidad) {
		this.iden = iden;
		this.documento = documento;
		this.entidad = entidad;
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
	@JoinColumn(name = "iddocumento", nullable = false)
	public Documento getDocumento() {
		return this.documento;
	}

	public void setDocumento(Documento documento) {
		this.documento = documento;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "identidad", nullable = false)
	public Entidad getEntidad() {
		return this.entidad;
	}

	public void setEntidad(Entidad entidad) {
		this.entidad = entidad;
	}

}

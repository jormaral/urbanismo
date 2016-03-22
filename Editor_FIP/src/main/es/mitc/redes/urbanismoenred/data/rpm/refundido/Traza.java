package es.mitc.redes.urbanismoenred.data.rpm.refundido;

// Generated 26-abr-2012 14:16:08 by Hibernate Tools 3.4.0.CR1

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Traza generated by hbm2java
 */
@Entity
@Table(name = "traza")
public class Traza implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2623442090738416728L;
	private TrazaId id;
	private int idrefundido;

	public Traza() {
	}

	public Traza(TrazaId id, int idrefundido) {
		this.id = id;
		this.idrefundido = idrefundido;
	}

	@EmbeddedId
	@AttributeOverrides({
			@AttributeOverride(name = "idproceso", column = @Column(name = "idproceso", nullable = false)),
			@AttributeOverride(name = "tabla", column = @Column(name = "tabla", nullable = false, length = 256)),
			@AttributeOverride(name = "idplaneamiento", column = @Column(name = "idplaneamiento", nullable = false)) })
	public TrazaId getId() {
		return this.id;
	}

	public void setId(TrazaId id) {
		this.id = id;
	}

	@Column(name = "idrefundido", nullable = false)
	public int getIdrefundido() {
		return this.idrefundido;
	}

	public void setIdrefundido(int idrefundido) {
		this.idrefundido = idrefundido;
	}

}

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

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;

@Embeddable
public class VersionId implements java.io.Serializable {

	private int iden;
	private String version;
	private String comentario;

	public VersionId() {
	}

	public VersionId(int iden, String version, String comentario) {
		this.iden = iden;
		this.version = version;
		this.comentario = comentario;
	}

	@Column(name = "iden", nullable = false)
	@GeneratedValue(strategy=GenerationType.SEQUENCE)
	public int getIden() {
		return this.iden;
	}

	public void setIden(int iden) {
		this.iden = iden;
	}

	@Column(name = "version", nullable = false, length = 20)
	@NotNull
	@Length(max = 20)
	public String getVersion() {
		return this.version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	@Column(name = "comentario", nullable = false)
	@NotNull
	public String getComentario() {
		return this.comentario;
	}

	public void setComentario(String comentario) {
		this.comentario = comentario;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof VersionId))
			return false;
		VersionId castOther = (VersionId) other;

		return (this.getIden() == castOther.getIden())
				&& ((this.getVersion() == castOther.getVersion()) || (this
						.getVersion() != null && castOther.getVersion() != null && this
						.getVersion().equals(castOther.getVersion())))
				&& ((this.getComentario() == castOther.getComentario()) || (this
						.getComentario() != null
						&& castOther.getComentario() != null && this
						.getComentario().equals(castOther.getComentario())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + this.getIden();
		result = 37 * result
				+ (getVersion() == null ? 0 : this.getVersion().hashCode());
		result = 37
				* result
				+ (getComentario() == null ? 0 : this.getComentario()
						.hashCode());
		return result;
	}

}

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

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.validator.NotNull;

@Entity
@Table(name = "version", schema = "planeamiento")
public class Version implements java.io.Serializable {

	private VersionId id;

	public Version() {
	}

	public Version(VersionId id) {
		this.id = id;
	}

	@EmbeddedId
	@AttributeOverrides({
			@AttributeOverride(name = "iden", column = @Column(name = "iden", nullable = false)),
			@AttributeOverride(name = "version", column = @Column(name = "version", nullable = false, length = 20)),
			@AttributeOverride(name = "comentario", column = @Column(name = "comentario", nullable = false)) })
	@NotNull
	public VersionId getId() {
		return this.id;
	}

	public void setId(VersionId id) {
		this.id = id;
	}

}

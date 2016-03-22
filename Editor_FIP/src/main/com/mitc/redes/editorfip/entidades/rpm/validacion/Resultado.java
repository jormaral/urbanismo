/*
* Copyright 2011 red.es
* Autores: Arnaiz Consultores.
*
** Licencia con arreglo a la EUPL, Versión 1.1 o -en cuanto
** sean aprobadas por la Comision Europea- versiones
** posteriores de la EUPL (la <<Licencia>>);
** Solo podra usarse esta obra si se respeta la Licencia.
* Puede obtenerse una copia de la Licencia en:
*
* http://ec.europa.eu/idabc/eupl5
*
** Salvo cuando lo exija la legislacion aplicable o se acuerde
* por escrito, el programa distribuido con arreglo a la
* Licencia se distribuye <<TAL CUAL>>,
** SIN GARANTIAS NI CONDICIONES DE NINGUN TIPO, ni expresas
** ni implicitas.
** Vease la Licencia en el idioma concreto que rige
* los permisos y limitaciones que establece la Licencia.
*/
package com.mitc.redes.editorfip.entidades.rpm.validacion;

// Generated 18-jul-2011 17:35:19 by Hibernate Tools 3.4.0.CR1

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Resultado generated by hbm2java
 */
@Entity
@Table(name = "resultado", schema="validacion")
public class Resultado implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7191708824172262929L;
	private ResultadoId id;
	private Validacion validacion;
	private Proceso proceso;
	private Date fecha;
	private boolean exito;
	private Set<Error> errors = new HashSet<Error>(0);

	public Resultado() {
	}

	public Resultado(ResultadoId id, Validacion validacion, Proceso proceso,
			Date fecha, boolean exito) {
		this.id = id;
		this.validacion = validacion;
		this.proceso = proceso;
		this.fecha = fecha;
		this.exito = exito;
	}

	public Resultado(ResultadoId id, Validacion validacion, Proceso proceso,
			Date fecha, boolean exito, Set<Error> errors) {
		this.id = id;
		this.validacion = validacion;
		this.proceso = proceso;
		this.fecha = fecha;
		this.exito = exito;
		this.errors = errors;
	}

	@EmbeddedId
	@AttributeOverrides({
			@AttributeOverride(name = "idproceso", column = @Column(name = "idproceso", nullable = false)),
			@AttributeOverride(name = "idvalidacion", column = @Column(name = "idvalidacion", nullable = false)) })
	public ResultadoId getId() {
		return this.id;
	}

	public void setId(ResultadoId id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idvalidacion", nullable = false, insertable = false, updatable = false)
	public Validacion getValidacion() {
		return this.validacion;
	}

	public void setValidacion(Validacion validacion) {
		this.validacion = validacion;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idproceso", nullable = false, insertable = false, updatable = false)
	public Proceso getProceso() {
		return this.proceso;
	}

	public void setProceso(Proceso proceso) {
		this.proceso = proceso;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "fecha", nullable = false, length = 13)
	public Date getFecha() {
		return this.fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	@Column(name = "exito", nullable = false)
	public boolean isExito() {
		return this.exito;
	}

	public void setExito(boolean exito) {
		this.exito = exito;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "resultado")
	public Set<Error> getErrors() {
		return this.errors;
	}

	public void setErrors(Set<Error> errors) {
		this.errors = errors;
	}

}
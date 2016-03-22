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
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.validator.NotNull;

@Entity
@Table(name = "operacionentidad", schema = "planeamiento")
@SequenceGenerator( name = "DETSEQ", sequenceName = "planeamiento.planeamiento_operacionentidad_iden_seq",allocationSize=1 )
public class Operacionentidad implements java.io.Serializable {

	private int iden;
	private Entidad entidadByIdentidadoperadora;
	private Entidad entidadByIdentidad;
	private Operacion operacion;
	private int idtipooperacionent;

	public Operacionentidad() {
	}

	public Operacionentidad(int iden, Entidad entidadByIdentidadoperadora,
			Entidad entidadByIdentidad, Operacion operacion,
			int idtipooperacionent) {
		this.iden = iden;
		this.entidadByIdentidadoperadora = entidadByIdentidadoperadora;
		this.entidadByIdentidad = entidadByIdentidad;
		this.operacion = operacion;
		this.idtipooperacionent = idtipooperacionent;
	}

	@Id
	@Column(name = "iden", unique = true, nullable = false, insertable = false)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="DETSEQ")
	public int getIden() {
		return this.iden;
	}

	public void setIden(int iden) {
		this.iden = iden;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "identidadoperadora", nullable = false)
	@NotNull
	public Entidad getEntidadByIdentidadoperadora() {
		return this.entidadByIdentidadoperadora;
	}

	public void setEntidadByIdentidadoperadora(
			Entidad entidadByIdentidadoperadora) {
		this.entidadByIdentidadoperadora = entidadByIdentidadoperadora;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "identidad", nullable = false)
	@NotNull
	public Entidad getEntidadByIdentidad() {
		return this.entidadByIdentidad;
	}

	public void setEntidadByIdentidad(Entidad entidadByIdentidad) {
		this.entidadByIdentidad = entidadByIdentidad;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idoperacion", nullable = false)
	@NotNull
	public Operacion getOperacion() {
		return this.operacion;
	}

	public void setOperacion(Operacion operacion) {
		this.operacion = operacion;
	}

	@Column(name = "idtipooperacionent", nullable = false)
	public int getIdtipooperacionent() {
		return this.idtipooperacionent;
	}

	public void setIdtipooperacionent(int idtipooperacionent) {
		this.idtipooperacionent = idtipooperacionent;
	}

}

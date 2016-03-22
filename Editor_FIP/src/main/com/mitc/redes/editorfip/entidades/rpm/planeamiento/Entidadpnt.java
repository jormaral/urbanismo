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

import org.hibernate.annotations.Formula;
import org.hibernate.annotations.Type;
import org.hibernate.validator.NotNull;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.MultiPoint;

@Entity
@Table(name = "entidadpnt", schema = "planeamiento")
@SequenceGenerator( name = "DETSEQ", sequenceName = "planeamiento.planeamiento_entidadpnt_iden_seq",allocationSize=1 )
public class Entidadpnt implements java.io.Serializable {

	private int iden;
	private Entidad entidad;
	private Geometry geom;
	private boolean bsuspendida;

	public Entidadpnt() {
	}

	public Entidadpnt(int iden, Entidad entidad, boolean bsuspendida) {
		this.iden = iden;
		this.entidad = entidad;
		this.bsuspendida = bsuspendida;
	}

	public Entidadpnt(int iden, Entidad entidad, MultiPoint geom,
			boolean bsuspendida) {
		this.iden = iden;
		this.entidad = entidad;
		this.geom = geom;
		this.bsuspendida = bsuspendida;
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
	@JoinColumn(name = "identidad", nullable = false)
	@NotNull
	public Entidad getEntidad() {
		return this.entidad;
	}

	public void setEntidad(Entidad entidad) {
		this.entidad = entidad;
	}


	@Column(name = "geom")
	@Type(type = "org.hibernatespatial.GeometryUserType")
	public Geometry getGeom() {
		return this.geom;
	}

	public void setGeom(Geometry geom) {
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

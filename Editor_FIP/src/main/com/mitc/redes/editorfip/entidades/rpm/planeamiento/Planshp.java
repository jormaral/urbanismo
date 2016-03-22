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

@Entity
@Table(name = "planshp", schema = "planeamiento")
@SequenceGenerator( name = "DETSEQ", sequenceName = "planeamiento.planeamiento_planshp_iden_seq",allocationSize=1 )
public class Planshp implements java.io.Serializable {

	private int iden;
	private Plan plan;
	private int objectid;
	private String shape;
	private String geom;

	public Planshp() {
	}

	public Planshp(int iden, int objectid) {
		this.iden = iden;
		this.objectid = objectid;
	}

	public Planshp(int iden, Plan plan, int objectid, String shape,
			String geom) {
		this.iden = iden;
		this.plan = plan;
		this.objectid = objectid;
		this.shape = shape;
		this.geom = geom;
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
	@JoinColumn(name = "idplan")
	public Plan getPlan() {
		return this.plan;
	}

	public void setPlan(Plan plan) {
		this.plan = plan;
	}

	@Column(name = "objectid", nullable = false)
	public int getObjectid() {
		return this.objectid;
	}

	public void setObjectid(int objectid) {
		this.objectid = objectid;
	}

	@Column(name = "shape")
	public String getShape() {
		return this.shape;
	}

	public void setShape(String shape) {
		this.shape = shape;
	}

	@Column(name = "geom")
	public String getGeom() {
		return this.geom;
	}

	public void setGeom(String geom) {
		this.geom = geom;
	}

}

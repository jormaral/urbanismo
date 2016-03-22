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

package com.mitc.redes.editorfip.entidades.rpm.diccionario;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Formula;


@Entity
@Table(name = "ambitoshp", schema = "diccionario")
@SequenceGenerator( name = "DETSEQ", sequenceName = "diccionario.diccionario_ambitoshp_iden_seq",allocationSize=1 )
public class Ambitoshp implements java.io.Serializable {

	private int iden;
	private Integer idambito;
	private String geom;

	public Ambitoshp() {
	}

	public Ambitoshp(int iden) {
		this.iden = iden;
	}

	public Ambitoshp(int iden, Integer idambito, String geom) {
		this.iden = iden;
		this.idambito = idambito;
		this.geom = geom;
	}

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="DETSEQ")
	@Column(name = "iden", unique = true, nullable = false)
	public int getIden() {
		return this.iden;
	}

	public void setIden(int iden) {
		this.iden = iden;
	}

	@Column(name = "idambito")
	public Integer getIdambito() {
		return this.idambito;
	}

	public void setIdambito(Integer idambito) {
		this.idambito = idambito;
	}

	@Formula(value = "AsText(geom)")
	@Column(name = "geom")
	public String getGeom() {
		return this.geom;
	}

	public void setGeom(String geom) {
		this.geom = geom;
	}

}

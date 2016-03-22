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

import java.util.Date;

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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;

@Entity
@Table(name = "boletintramite", schema = "planeamiento")
@SequenceGenerator( name = "DETSEQ", sequenceName = "planeamiento.planeamiento_boletintramite_iden_seq",allocationSize=1 )
public class Boletintramite implements java.io.Serializable {

	private int iden;
	private Tramite tramite;
	private int idboletin;
	private Date fecha;
	private String numero;

	public Boletintramite() {
	}

	public Boletintramite(int iden, Tramite tramite, int idboletin) {
		this.iden = iden;
		this.tramite = tramite;
		this.idboletin = idboletin;
	}

	public Boletintramite(int iden, Tramite tramite, int idboletin, Date fecha,
			String numero) {
		this.iden = iden;
		this.tramite = tramite;
		this.idboletin = idboletin;
		this.fecha = fecha;
		this.numero = numero;
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
	@JoinColumn(name = "idtramite", nullable = false)
	@NotNull
	public Tramite getTramite() {
		return this.tramite;
	}

	public void setTramite(Tramite tramite) {
		this.tramite = tramite;
	}

	@Column(name = "idboletin", nullable = false)
	public int getIdboletin() {
		return this.idboletin;
	}

	public void setIdboletin(int idboletin) {
		this.idboletin = idboletin;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "fecha", length = 13)
	public Date getFecha() {
		return this.fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	@Column(name = "numero", length = 20)
	@Length(max = 20)
	public String getNumero() {
		return this.numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

}

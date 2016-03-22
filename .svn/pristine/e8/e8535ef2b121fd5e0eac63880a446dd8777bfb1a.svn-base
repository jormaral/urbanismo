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

package com.mitc.redes.editorfip.entidades.rpm.gestionfip;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.jboss.seam.annotations.Name;

@Entity
@Name("fipsCargados")
@Table(name="fipscargados", schema = "gestionfip")
@SequenceGenerator( name = "DETSEQ", sequenceName = "gestionfip.gestionfip_fipscargados_id_seq",allocationSize=1 )
public class FipsCargados implements Serializable
{
	private Long id;
	private String identificador;
	private String ruta;
	private Date fechaIntroduccion;
	private Date fechaInicio;
	private Date fechaConsolidacion;
	private boolean consolidado;
	private int idambito;
	private String estado;
	private String resultado;
	private int idplaneamientoencargado;

	
	
	
	public int getIdplaneamientoencargado() {
		return idplaneamientoencargado;
	}
	public void setIdplaneamientoencargado(int idplaneamientoencargado) {
		this.idplaneamientoencargado = idplaneamientoencargado;
	}
	
	@Column(length=50000)
	public String getResultado() {
		return resultado;
	}
	public void setResultado(String resultado) {
		this.resultado = resultado;
	}
	
	public Date getFechaInicio() {
		return fechaInicio;
	}
	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}
	
	
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public String getIdentificador() {
		return identificador;
	}
	public void setIdentificador(String identificador) {
		this.identificador = identificador;
	}
	public Date getFechaIntroduccion() {
		return fechaIntroduccion;
	}
	public void setFechaIntroduccion(Date fechaIntroduccion) {
		this.fechaIntroduccion = fechaIntroduccion;
	}
	public Date getFechaConsolidacion() {
		return fechaConsolidacion;
	}
	public void setFechaConsolidacion(Date fechaConsolidacion) {
		this.fechaConsolidacion = fechaConsolidacion;
	}
	public boolean isConsolidado() {
		return consolidado;
	}
	public void setConsolidado(boolean consolidado) {
		this.consolidado = consolidado;
	}
	
	@Id
	@Column(name = "id", unique = true, nullable = false, insertable = false)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="DETSEQ")
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getRuta() {
		return ruta;
	}
	public void setRuta(String ruta) {
		this.ruta = ruta;
	}
	public int getIdambito() {
		return idambito;
	}
	public void setIdambito(int idambito) {
		this.idambito = idambito;
	}
	
	
	
	
}
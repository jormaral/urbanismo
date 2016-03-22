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

package com.mitc.redes.editorfip.entidades.busqueda;

import java.io.Serializable;

/**
 *
 * @author Sancho
 */
public class BusquedaCondicionDTO implements Serializable{
	

	private int id;
	private String plan;
	private String entidadAplicada;
	private String detAplicada;
	
	private String detRegimen;
	private String valorReferencia;
	private String valor;
	private String regimenEspecifico;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getPlan() {
		return plan;
	}
	public void setPlan(String plan) {
		this.plan = plan;
	}
	public String getEntidadAplicada() {
		return entidadAplicada;
	}
	public void setEntidadAplicada(String entidadAplicada) {
		this.entidadAplicada = entidadAplicada;
	}
	public String getDetAplicada() {
		return detAplicada;
	}
	public void setDetAplicada(String detAplicada) {
		this.detAplicada = detAplicada;
	}
	
	public String getDetRegimen() {
		return detRegimen;
	}
	public void setDetRegimen(String detRegimen) {
		this.detRegimen = detRegimen;
	}
	public String getValorReferencia() {
		return valorReferencia;
	}
	public void setValorReferencia(String valorReferencia) {
		this.valorReferencia = valorReferencia;
	}
	public String getValor() {
		return valor;
	}
	public void setValor(String valor) {
		this.valor = valor;
	}
	public String getRegimenEspecifico() {
		return regimenEspecifico;
	}
	public void setRegimenEspecifico(String regimenEspecifico) {
		this.regimenEspecifico = regimenEspecifico;
	}
	
}
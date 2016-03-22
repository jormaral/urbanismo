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

package com.mitc.redes.editorfip.entidades.interfazgrafico;

public class EntidadDTO {
	
	private int iden;
	private String entidadPadre;
	private String nombre;
	private String clave;
	private String etiqueta;
	private boolean suspendida;
	private String entidadBase;
	private String geometria;
	
	public EntidadDTO(){
		
	}
	
	public EntidadDTO(int iden, String entidadPadre, String nombre,
			String clave, String etiqueta, boolean suspendida,
			String entidadBase, String geometria) {
		super();
		this.iden = iden;
		this.entidadPadre = entidadPadre;
		this.nombre = nombre;
		this.clave = clave;
		this.etiqueta = etiqueta;
		this.suspendida = suspendida;
		this.entidadBase = entidadBase;
		this.geometria = geometria;
	}
	public int getIden() {
		return iden;
	}
	public void setIden(int iden) {
		this.iden = iden;
	}
	public String getEntidadPadre() {
		return entidadPadre;
	}
	public void setEntidadPadre(String entidadPadre) {
		this.entidadPadre = entidadPadre;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getClave() {
		return clave;
	}
	public void setClave(String clave) {
		this.clave = clave;
	}
	public String getEtiqueta() {
		return etiqueta;
	}
	public void setEtiqueta(String etiqueta) {
		this.etiqueta = etiqueta;
	}
	public boolean isSuspendida() {
		return suspendida;
	}
	public void setSuspendida(boolean suspendida) {
		this.suspendida = suspendida;
	}
	public String getEntidadBase() {
		return entidadBase;
	}
	public void setEntidadBase(String entidadBase) {
		this.entidadBase = entidadBase;
	}
	public String getGeometria() {
		return geometria;
	}
	public void setGeometria(String geometria) {
		this.geometria = geometria;
	}
	
	

}

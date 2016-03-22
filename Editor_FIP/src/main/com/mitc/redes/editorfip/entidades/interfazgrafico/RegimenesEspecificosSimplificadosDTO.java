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

import java.io.Serializable;

public class RegimenesEspecificosSimplificadosDTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3631305702837335080L;
	private String nombreRegimenEspecifico="";
	private String textoRegimenEspecifico ="";
	
	private int idRegimenEspecifico = 0;
	
	
	public RegimenesEspecificosSimplificadosDTO() {
		super();
		
	}


	public RegimenesEspecificosSimplificadosDTO(String nombreRegimenEspecifico,
			String textoRegimenEspecifico) {
		super();
		this.nombreRegimenEspecifico = nombreRegimenEspecifico;
		this.textoRegimenEspecifico = textoRegimenEspecifico;
		this.idRegimenEspecifico = 0;
	}


	public String getNombreRegimenEspecifico() {
		return nombreRegimenEspecifico;
	}


	public void setNombreRegimenEspecifico(String nombreRegimenEspecifico) {
		this.nombreRegimenEspecifico = nombreRegimenEspecifico;
	}


	public String getTextoRegimenEspecifico() {
		return textoRegimenEspecifico;
	}


	public void setTextoRegimenEspecifico(String textoRegimenEspecifico) {
		this.textoRegimenEspecifico = textoRegimenEspecifico;
	}


	public int getIdRegimenEspecifico() {
		return idRegimenEspecifico;
	}


	public void setIdRegimenEspecifico(int idRegimenEspecifico) {
		this.idRegimenEspecifico = idRegimenEspecifico;
	}


	
	
	
	
	
}

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

public class DEExcelDTO implements Serializable{
	
	int idListaPadre;
	String nombreDeterminacion;
	int idCaracter;
	String textoCaracter;
	int orden;
	String apartadoArticulo;
	int idDeterminacionBase;
	int idDeterminacionPersistida;
	String etiqueta;
	int idGrupoAplicacion;
	String textoGrupoAplicacion;
	String textoUnidad;
	int idUnidad;
	String referencia;
	int idTramiteBaseTrabajo;
	
	String textoRegulacion1;
	String nombreRegulacion1;
	String textoRegulacion2;
	String nombreRegulacion2;
	
	String textoRegulacion3;
	String nombreRegulacion3;
	String textoRegulacion4;
	String nombreRegulacion4;
	String textoRegulacion5;
	String nombreRegulacion5;
	
	String ordenNivel_1;
	String nombreNivel_1;
	
	String ordenNivel_2;
	String nombreNivel_2;
	
	String ordenNivel_3;
	String nombreNivel_3;
	
	String ordenNivel_4;
	String nombreNivel_4;
	
	String ordenNivel_5;
	String nombreNivel_5;
	
	String ordenNivel_6;
	String nombreNivel_6;
	
	String ordenNivel_7;
	String nombreNivel_7;
	
	String ordenNivel_8;
	String nombreNivel_8;
	
	String equivale;
	
	public DEExcelDTO() {
		// TODO Auto-generated constructor stub
		idDeterminacionBase =-1;
		textoGrupoAplicacion = "";
		
		textoRegulacion1 = "";
		nombreRegulacion1 = "";
		textoRegulacion2 = "";
		nombreRegulacion2 = "";
		
		textoRegulacion3 = "";
		nombreRegulacion3 = "";
		textoRegulacion4 = "";
		nombreRegulacion4 = "";
		textoRegulacion5 = "";
		nombreRegulacion5 = "";
		
		ordenNivel_1 = "";
		nombreNivel_1 = "";
		
		ordenNivel_2 = "";
		nombreNivel_2 = "";
		
		ordenNivel_3 = "";
		nombreNivel_3 = "";
		
		ordenNivel_4 = "";
		nombreNivel_4 = "";
		
		ordenNivel_5 = "";
		nombreNivel_5 = "";
		
		ordenNivel_6 = "";
		nombreNivel_6 = "";
		
		ordenNivel_7 = "";
		nombreNivel_7 = "";
		
		ordenNivel_8 = "";
		nombreNivel_8 = "";
		
		equivale = "";
	}
	
	public int getIdListaPadre() {
		return idListaPadre;
	}
	public void setIdListaPadre(int idListaPadre) {
		this.idListaPadre = idListaPadre;
	}
	public String getNombreDeterminacion() {
		return nombreDeterminacion;
	}
	public void setNombreDeterminacion(String nombreDeterminacion) {
		this.nombreDeterminacion = nombreDeterminacion;
	}
	public int getIdCaracter() {
		return idCaracter;
	}
	public void setIdCaracter(int idCaracter) {
		this.idCaracter = idCaracter;
	}
	public int getOrden() {
		return orden;
	}
	public void setOrden(int orden) {
		this.orden = orden;
	}
	public String getApartadoArticulo() {
		return apartadoArticulo;
	}
	public void setApartadoArticulo(String apartadoArticulo) {
		this.apartadoArticulo = apartadoArticulo;
	}
	public int getIdDeterminacionBase() {
		return idDeterminacionBase;
	}
	public void setIdDeterminacionBase(int idDeterminacionBase) {
		this.idDeterminacionBase = idDeterminacionBase;
	}
	public int getIdDeterminacionPersistida() {
		return idDeterminacionPersistida;
	}
	public void setIdDeterminacionPersistida(int idDeterminacionPersistida) {
		this.idDeterminacionPersistida = idDeterminacionPersistida;
	}
	public String getEtiqueta() {
		return etiqueta;
	}
	public void setEtiqueta(String etiqueta) {
		this.etiqueta = etiqueta;
	}
	public int getIdGrupoAplicacion() {
		return idGrupoAplicacion;
	}
	public void setIdGrupoAplicacion(int idGrupoAplicacion) {
		this.idGrupoAplicacion = idGrupoAplicacion;
	}
	public int getIdUnidad() {
		return idUnidad;
	}
	public void setIdUnidad(int idUnidad) {
		this.idUnidad = idUnidad;
	}
	public String getReferencia() {
		return referencia;
	}
	public void setReferencia(String referencia) {
		this.referencia = referencia;
	}
	public String getTextoCaracter() {
		return textoCaracter;
	}
	public void setTextoCaracter(String textoCaracter) {
		this.textoCaracter = textoCaracter;
	}

	public String getTextoRegulacion1() {
		return textoRegulacion1;
	}

	public void setTextoRegulacion1(String textoRegulacion1) {
		this.textoRegulacion1 = textoRegulacion1;
	}

	public String getNombreRegulacion1() {
		return nombreRegulacion1;
	}

	public void setNombreRegulacion1(String nombreRegulacion1) {
		this.nombreRegulacion1 = nombreRegulacion1;
	}

	public String getEquivale() {
		return equivale;
	}

	public void setEquivale(String equivale) {
		this.equivale = equivale;
	}

	public String getTextoRegulacion2() {
		return textoRegulacion2;
	}

	public void setTextoRegulacion2(String textoRegulacion2) {
		this.textoRegulacion2 = textoRegulacion2;
	}

	public String getNombreRegulacion2() {
		return nombreRegulacion2;
	}

	public void setNombreRegulacion2(String nombreRegulacion2) {
		this.nombreRegulacion2 = nombreRegulacion2;
	}

	public String getTextoRegulacion3() {
		return textoRegulacion3;
	}

	public void setTextoRegulacion3(String textoRegulacion3) {
		this.textoRegulacion3 = textoRegulacion3;
	}

	public String getNombreRegulacion3() {
		return nombreRegulacion3;
	}

	public void setNombreRegulacion3(String nombreRegulacion3) {
		this.nombreRegulacion3 = nombreRegulacion3;
	}

	public String getTextoRegulacion4() {
		return textoRegulacion4;
	}

	public void setTextoRegulacion4(String textoRegulacion4) {
		this.textoRegulacion4 = textoRegulacion4;
	}

	public String getNombreRegulacion4() {
		return nombreRegulacion4;
	}

	public void setNombreRegulacion4(String nombreRegulacion4) {
		this.nombreRegulacion4 = nombreRegulacion4;
	}

	public String getTextoRegulacion5() {
		return textoRegulacion5;
	}

	public void setTextoRegulacion5(String textoRegulacion5) {
		this.textoRegulacion5 = textoRegulacion5;
	}

	public String getNombreRegulacion5() {
		return nombreRegulacion5;
	}

	public void setNombreRegulacion5(String nombreRegulacion5) {
		this.nombreRegulacion5 = nombreRegulacion5;
	}

	public String getTextoGrupoAplicacion() {
		return textoGrupoAplicacion;
	}

	public void setTextoGrupoAplicacion(String textoGrupoAplicacion) {
		this.textoGrupoAplicacion = textoGrupoAplicacion;
	}

	public int getIdTramiteBaseTrabajo() {
		return idTramiteBaseTrabajo;
	}

	public void setIdTramiteBaseTrabajo(int idTramiteBaseTrabajo) {
		this.idTramiteBaseTrabajo = idTramiteBaseTrabajo;
	}

	public String getTextoUnidad() {
		return textoUnidad;
	}

	public void setTextoUnidad(String textoUnidad) {
		this.textoUnidad = textoUnidad;
	}

	public String getOrdenNivel_1() {
		return ordenNivel_1;
	}

	public void setOrdenNivel_1(String ordenNivel_1) {
		this.ordenNivel_1 = ordenNivel_1;
	}

	public String getNombreNivel_1() {
		return nombreNivel_1;
	}

	public void setNombreNivel_1(String nombreNivel_1) {
		this.nombreNivel_1 = nombreNivel_1;
	}

	public String getOrdenNivel_2() {
		return ordenNivel_2;
	}

	public void setOrdenNivel_2(String ordenNivel_2) {
		this.ordenNivel_2 = ordenNivel_2;
	}

	public String getNombreNivel_2() {
		return nombreNivel_2;
	}

	public void setNombreNivel_2(String nombreNivel_2) {
		this.nombreNivel_2 = nombreNivel_2;
	}

	public String getOrdenNivel_3() {
		return ordenNivel_3;
	}

	public void setOrdenNivel_3(String ordenNivel_3) {
		this.ordenNivel_3 = ordenNivel_3;
	}

	public String getNombreNivel_3() {
		return nombreNivel_3;
	}

	public void setNombreNivel_3(String nombreNivel_3) {
		this.nombreNivel_3 = nombreNivel_3;
	}

	public String getOrdenNivel_4() {
		return ordenNivel_4;
	}

	public void setOrdenNivel_4(String ordenNivel_4) {
		this.ordenNivel_4 = ordenNivel_4;
	}

	public String getNombreNivel_4() {
		return nombreNivel_4;
	}

	public void setNombreNivel_4(String nombreNivel_4) {
		this.nombreNivel_4 = nombreNivel_4;
	}

	public String getOrdenNivel_5() {
		return ordenNivel_5;
	}

	public void setOrdenNivel_5(String ordenNivel_5) {
		this.ordenNivel_5 = ordenNivel_5;
	}

	public String getNombreNivel_5() {
		return nombreNivel_5;
	}

	public void setNombreNivel_5(String nombreNivel_5) {
		this.nombreNivel_5 = nombreNivel_5;
	}

	public String getOrdenNivel_6() {
		return ordenNivel_6;
	}

	public void setOrdenNivel_6(String ordenNivel_6) {
		this.ordenNivel_6 = ordenNivel_6;
	}

	public String getNombreNivel_6() {
		return nombreNivel_6;
	}

	public void setNombreNivel_6(String nombreNivel_6) {
		this.nombreNivel_6 = nombreNivel_6;
	}
	
	public String getOrdenNivel_7() {
		return ordenNivel_7;
	}

	public void setOrdenNivel_7(String ordenNivel_7) {
		this.ordenNivel_7 = ordenNivel_7;
	}
	
	public String getNombreNivel_7() {
		return nombreNivel_7;
	}

	public void setNombreNivel_7(String nombreNivel_7) {
		this.nombreNivel_7 = nombreNivel_7;
	}
	
	public String getNombreNivel_8() {
		return nombreNivel_8;
	}

	public void setNombreNivel_8(String nombreNivel_8) {
		this.nombreNivel_8 = nombreNivel_8;
	}
	
	
	public String getOrdenNivel_8() {
		return ordenNivel_8;
	}

	public void setOrdenNivel_8(String ordenNivel_8) {
		this.ordenNivel_8 = ordenNivel_8;
	}
	
	
}
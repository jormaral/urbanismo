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

/**
* Clase usada para almacenar los datos a mostrar en la interfaz
* 
* @author Aitor
*/
public class DocumentoDTO {


	private int iden;
	private String nombre;
	private String grupoDocumento;
	private String tipoDocumento;
	private int escala;
	private String comentario;
	private String archivo;
	
	private String esHoja;
	/**
	 *  Cadena de control para almacenar si el documento esta siendo creado o 
	 *  modificado desde DETERMINACION, ENTIDAD o CASO
	 */
//	private String vinculo;
	
	public DocumentoDTO(int iden, String nombre, String grupoDocumento,
			String tipoDocumento, int escala, String comentario, String archivo, String esHoja) {
		super();
		this.iden = iden;
		this.nombre = nombre;
		this.grupoDocumento = grupoDocumento;
		this.tipoDocumento = tipoDocumento;
		this.escala = escala;
		this.comentario = comentario;
		this.archivo = archivo;
		this.esHoja = esHoja;
	}
	
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getGrupoDocumento() {
		return grupoDocumento;
	}
	public void setGrupoDocumento(String grupoDocumento) {
		this.grupoDocumento = grupoDocumento;
	}
	public String getTipoDocumento() {
		return tipoDocumento;
	}
	public void setTipoDocumento(String tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}
	public int getEscala() {
		return escala;
	}
	public void setEscala(int escala) {
		this.escala = escala;
	}
	public String getComentario() {
		return comentario;
	}
	public void setComentario(String comentario) {
		this.comentario = comentario;
	}
	
	
	public DocumentoDTO() {
		super();
	}

	public int getIden() {
		return iden;
	}
	public void setIden(int iden) {
		this.iden = iden;
	}
	public void setArchivo(String archivo) {
		this.archivo = archivo;
	}
	public String getArchivo() {
		return archivo;
	}

	public String getEsHoja() {
		return esHoja;
	}

	public void setEsHoja(String esHoja) {
		this.esHoja = esHoja;
	}
	
	
//	public void setVinculo(String vinculo) {
//		this.vinculo = vinculo;
//	}
//
//	public String getVinculo() {
//		return vinculo;
//	}
	
}

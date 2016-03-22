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
@Name("documentosNormativaGenerados")
@Table(name="documentosnormativagenerados", schema = "gestionfip")
@SequenceGenerator( name = "DETSEQ", sequenceName = "gestionfip.gestionfip_documentosnormativagenerados_id_seq",allocationSize=1 )
public class DocumentosNormativaGenerados implements Serializable
{
	private Long id;
	private String nombreTramite;
	private int idTramiteDocumentoNormativa;
	private Date fechaGeneracion;
	private String urlDocNormativaEntidades;
	private String urlDocNormativaDeterminacion;
	private String estado;
	private String observaciones;
	
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	private int version;
	
	@Id
	@Column(name = "id", unique = true, nullable = false, insertable = false)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="DETSEQ")
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public int getVersion() {
		return version;
	}
	public void setVersion(int version) {
		this.version = version;
	}
	public String getNombreTramite() {
		return nombreTramite;
	}
	public void setNombreTramite(String nombreTramite) {
		this.nombreTramite = nombreTramite;
	}
	public int getIdTramiteDocumentoNormativa() {
		return idTramiteDocumentoNormativa;
	}
	public void setIdTramiteDocumentoNormativa(int idTramiteDocumentoNormativa) {
		this.idTramiteDocumentoNormativa = idTramiteDocumentoNormativa;
	}
	public Date getFechaGeneracion() {
		return fechaGeneracion;
	}
	public void setFechaGeneracion(Date fechaGeneracion) {
		this.fechaGeneracion = fechaGeneracion;
	}
	public String getUrlDocNormativaEntidades() {
		return urlDocNormativaEntidades;
	}
	public void setUrlDocNormativaEntidades(String urlDocNormativaEntidades) {
		this.urlDocNormativaEntidades = urlDocNormativaEntidades;
	}
	public String getUrlDocNormativaDeterminacion() {
		return urlDocNormativaDeterminacion;
	}
	public void setUrlDocNormativaDeterminacion(String urlDocNormativaDeterminacion) {
		this.urlDocNormativaDeterminacion = urlDocNormativaDeterminacion;
	}
	public String getObservaciones() {
		return observaciones;
	}
	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}
	
	
	
}
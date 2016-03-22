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

package com.mitc.redes.editorfip.entidades.rpm.validaciones;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.validator.Length;
import org.jboss.seam.annotations.Name;

@Entity
@Name("tipoValidacion")
@Table(name="tipovalidacion", schema = "validaciones")
@SequenceGenerator( name = "DETSEQ", sequenceName = "validaciones.tipovalidacion_iden_seq",allocationSize=1 )
public class TipoValidacion implements Serializable
{
	private Long id;
	private String codigo;
	private String nombrevalidacion;
	
	public TipoValidacion() {
		super();
		
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

	@Length(max = 3)
	public String getCodigo() {
		return codigo;
	}


	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}


	public String getNombrevalidacion() {
		return nombrevalidacion;
	}


	public void setNombrevalidacion(String nombrevalidacion) {
		this.nombrevalidacion = nombrevalidacion;
	}

	
	
}
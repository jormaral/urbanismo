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

package com.mitc.redes.editorfip.entidades.rpm.ayuda;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.validator.NotNull;
import org.jboss.seam.annotations.Name;

@Entity
@Name("faq")
@Table(name="faq", schema = "ayuda")
@SequenceGenerator( name = "DETSEQ", sequenceName = "ayuda.ayuda_faq_id_seq",allocationSize=1 )
public class Faq implements Serializable
{
	private Long id;
	private int orden;
	private String nombrefaq;
	private String textofaq;
	
	
	
	public Faq() {
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

	
	@NotNull
	public int getOrden() {
		return orden;
	}


	public void setOrden(int orden) {
		this.orden = orden;
	}


	public String getNombrefaq() {
		return nombrefaq;
	}


	public void setNombrefaq(String nombrefaq) {
		this.nombrefaq = nombrefaq;
	}


	public String getTextofaq() {
		return textofaq;
	}


	public void setTextofaq(String textofaq) {
		this.textofaq = textofaq;
	}

	

	
	
}
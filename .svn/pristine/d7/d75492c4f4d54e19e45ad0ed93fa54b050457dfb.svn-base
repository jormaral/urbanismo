package com.mitc.redes.editorfip.entidades.rpm.ayuda;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.validator.NotNull;
import org.jboss.seam.annotations.Name;

@Entity
@Name("manual")
@Table(name="manual", schema = "ayuda")
public class Manual implements Serializable {
	
	private Long id;
	private int orden;
	private String titulo;
	private String archivo;
	private String descripcion;
	
	@Id @GeneratedValue
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
	
	public String getTitulo() {
		return titulo;
	}
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	
	public String getArchivo() {
		return archivo;
	}
	
	public void setArchivo(String archivo) {
		this.archivo = archivo;
	}
	
	public String getDescripcion() {
		return descripcion;
	}
	
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	
}

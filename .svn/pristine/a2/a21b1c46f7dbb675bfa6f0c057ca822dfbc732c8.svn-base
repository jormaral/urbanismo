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

package com.mitc.redes.editorfip.entidades.rpm.administracionusuarios;


import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.validator.Email;
import org.hibernate.validator.NotNull;
import org.jboss.seam.annotations.Name;

@Entity
@Name("detalleUsuario")
@Table(name="detalleusuario", schema = "administracionusuarios")
@SequenceGenerator( name = "DETSEQ", sequenceName = "administracionusuarios.administracionusuarios_detalleusuario_id",allocationSize=1 )
public class DetalleUsuario implements Serializable
{
   
    private Long id;
    private Date fechaAlta;
    private Usuario usuario;
    private String nombre;
    private String primerApellido;
    private String segundoApellido;
    private String mail;
    
   
    

   

    @Id
	@Column(name = "id", unique = true, nullable = false, insertable = false)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="DETSEQ")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    @Basic @Temporal(TemporalType.DATE) 
    @NotNull
    public Date getFechaAlta()
    {
       return fechaAlta;
    }
    public void setFechaAlta(Date fechaAlta)
    {
       this.fechaAlta = fechaAlta;
    }
    
    @OneToOne @NotNull
    public Usuario getUsuario()
    {
       return usuario;
    }
    public void setUsuario(Usuario usuario)
    {
       this.usuario = usuario;
    }

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getPrimerApellido() {
		return primerApellido;
	}

	public void setPrimerApellido(String primerApellido) {
		this.primerApellido = primerApellido;
	}

	public String getSegundoApellido() {
		return segundoApellido;
	}

	public void setSegundoApellido(String segundoApellido) {
		this.segundoApellido = segundoApellido;
	}


	@Email
	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	

	
	
	
	
	
    
    

   

}

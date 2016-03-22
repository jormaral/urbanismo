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
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.validator.Length;
import org.jboss.seam.annotations.Name;

@Entity
@Name("elementoValidado")
@Table(name="elementovalidado", schema = "validaciones")
@SequenceGenerator( name = "DETSEQ", sequenceName = "validaciones.elementovalidado_iden_seq",allocationSize=1 )
public class ElementoValidado implements Serializable
{

    private Long id;
    private String objetovalidado;
    private String nombrevalidacion;
    private boolean validacionok;
    private String observacionesvalidacion;
    private ProcesoValidacion procesoValidacion;
    private TipoValidacion tipoValidacion;
    
    public ElementoValidado() {
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


	public String getObjetovalidado() {
		return objetovalidado;
	}


	public void setObjetovalidado(String objetovalidado) {
		this.objetovalidado = objetovalidado;
	}

	@Length(max = 2000)
	public String getNombrevalidacion() {
		return nombrevalidacion;
	}


	public void setNombrevalidacion(String nombrevalidacion) {
		this.nombrevalidacion = nombrevalidacion;
	}


	public boolean isValidacionok() {
		return validacionok;
	}


	public void setValidacionok(boolean validacionok) {
		this.validacionok = validacionok;
	}

	@Length(max = 2000)
	public String getObservacionesvalidacion() {
		return observacionesvalidacion;
	}


	public void setObservacionesvalidacion(String observacionesvalidacion) {
		this.observacionesvalidacion = observacionesvalidacion;
	}

	@ManyToOne
	public ProcesoValidacion getProcesoValidacion() {
		return procesoValidacion;
	}


	public void setProcesoValidacion(ProcesoValidacion procesoValidacion) {
		this.procesoValidacion = procesoValidacion;
	}

	@OneToOne
	public TipoValidacion getTipoValidacion() {
		return tipoValidacion;
	}


	public void setTipoValidacion(TipoValidacion tipoValidacion) {
		this.tipoValidacion = tipoValidacion;
	}

    
    
    
    
    
    

}

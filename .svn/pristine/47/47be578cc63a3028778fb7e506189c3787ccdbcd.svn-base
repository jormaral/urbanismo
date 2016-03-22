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
import javax.persistence.Transient;
import javax.persistence.Version;

import org.hibernate.validator.NotNull;
import org.jboss.seam.annotations.Name;

import com.mitc.redes.editorfip.entidades.rpm.diccionario.Ambito;
import com.mitc.redes.editorfip.entidades.rpm.diccionario.Instrumentoplan;
import com.mitc.redes.editorfip.entidades.rpm.diccionario.Tipotramite;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Plan;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Tramite;

@Entity
@Name("planesEncargados")
@Table(name="planesencargados", schema = "gestionfip")
@SequenceGenerator( name = "DETSEQ", sequenceName = "gestionfip.gestionfip_planesencargados_id_seq",allocationSize=1 )
public class PlanesEncargados implements Serializable
{
    
    private Long id;
    private Integer version;
    
    private Ambito ambito;
    private String nombre;
    private String codigoFip;
    private String proyeccion;
    private int iteracion;
    private Instrumentoplan instrumento;
    private Tipotramite tipotramite;
    private String ambitoaplicacion;
    
    private Plan planEncargado;
    private Tramite tramiteEncargado;
    
    private Tramite tramiteBase;
    private Tramite tramiteVigente;
    
    private Date fechaInicio;
    private Date fechaFinEncargado;
    
   

    // add additional entity attributes

    // seam-gen attribute getters/setters with annotations (you probably should edit)

    @Id
	@Column(name = "id", unique = true, nullable = false, insertable = false)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="DETSEQ")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Version
    public Integer getVersion() {
        return version;
    }

    private void setVersion(Integer version) {
        this.version = version;
    }

    @OneToOne @NotNull
	public Ambito getAmbito() {
		return ambito;
	}

	public void setAmbito(Ambito ambito) {
		this.ambito = ambito;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	
	
	public String getProyeccion() {
		return proyeccion;
	}

	public void setProyeccion(String proyeccion) {
		this.proyeccion = proyeccion;
	}

	public String getCodigoFip() {
		return codigoFip;
	}

	public void setCodigoFip(String codigoFip) {
		this.codigoFip = codigoFip;
	}

	public int getIteracion() {
		return iteracion;
	}

	public void setIteracion(int iteracion) {
		this.iteracion = iteracion;
	}

	 @OneToOne @NotNull
	public Instrumentoplan getInstrumento() {
		return instrumento;
	}

	public void setInstrumento(Instrumentoplan instrumento) {
		this.instrumento = instrumento;
	}

	 @OneToOne @NotNull
	public Tipotramite getTipotramite() {
		return tipotramite;
	}

	public void setTipotramite(Tipotramite tipotramite) {
		this.tipotramite = tipotramite;
	}

	public String getAmbitoaplicacion() {
		return ambitoaplicacion;
	}

	public void setAmbitoaplicacion(String ambitoaplicacion) {
		this.ambitoaplicacion = ambitoaplicacion;
	}

		
	
	@OneToOne @NotNull
	public Plan getPlanEncargado() {
		return planEncargado;
	}
	
	
	public void setPlanEncargado(Plan planEncargado) {
		this.planEncargado = planEncargado;
	}
	
	@OneToOne @NotNull
	public Tramite getTramiteEncargado() {
		return tramiteEncargado;
	}

	public void setTramiteEncargado(Tramite tramiteEncargado) {
		this.tramiteEncargado = tramiteEncargado;
	}

	@OneToOne @NotNull
	public Tramite getTramiteBase() {
		return tramiteBase;
	}

	public void setTramiteBase(Tramite tramiteBase) {
		this.tramiteBase = tramiteBase;
	}

	@OneToOne
	public Tramite getTramiteVigente() {
		return tramiteVigente;
	}

	public void setTramiteVigente(Tramite tramiteVigente) {
		this.tramiteVigente = tramiteVigente;
	}

	@Basic @Temporal(TemporalType.DATE) 
	public Date getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	@Basic @Temporal(TemporalType.DATE) 
	public Date getFechaFinEncargado() {
		return fechaFinEncargado;
	}

	public void setFechaFinEncargado(Date fechaFinEncargado) {
		this.fechaFinEncargado = fechaFinEncargado;
	}
    
    @Transient
    public String getEstado()
    {
    	if (fechaFinEncargado!=null)
    		return "Terminado";
    	else
    		return "En proceso";
    }
    

   

}

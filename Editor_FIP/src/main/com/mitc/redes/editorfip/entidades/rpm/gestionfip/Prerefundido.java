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
import javax.persistence.Version;

import org.jboss.seam.annotations.Name;

import com.mitc.redes.editorfip.entidades.rpm.diccionario.Ambito;
import com.mitc.redes.editorfip.entidades.rpm.diccionario.Tipooperacionplan;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Plan;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Tramite;

@Entity
@Name("prerefundido")
@Table(name="prerefundido", schema = "gestionfip")
@SequenceGenerator( name = "DETSEQ", sequenceName = "gestionfip.gestionfip_prerefundido_id_seq",allocationSize=1 )
public class Prerefundido implements Serializable
{
    
    private Long id;
    private Integer version;
    
    private Ambito ambito;
    private String ficheroXML;
    private Tipooperacionplan tipoopplan; 
    
    private int idTramiteEncargado;
    private int idTramitePrerefundido;
    private int idTramiteVigente;
    
    
    
    private Date fechaInicio;
    private boolean consolidado;

    
    private int idultimotramprerefundido;
    private int idprocesorefundido;
    
  
    

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

    public void setVersion(Integer version) {
        this.version = version;
    }

    @OneToOne
	public Ambito getAmbito() {
		return ambito;
	}

	public void setAmbito(Ambito ambito) {
		this.ambito = ambito;
	}

	public String getFicheroXML() {
		return ficheroXML;
	}

	public void setFicheroXML(String ficheroXML) {
		this.ficheroXML = ficheroXML;
	}

	@OneToOne
	public Tipooperacionplan getTipoopplan() {
		return tipoopplan;
	}

	public void setTipoopplan(Tipooperacionplan tipoopplan) {
		this.tipoopplan = tipoopplan;
	}

	
	
	

	public int getIdTramiteEncargado() {
		return idTramiteEncargado;
	}

	public void setIdTramiteEncargado(int idTramiteEncargado) {
		this.idTramiteEncargado = idTramiteEncargado;
	}

	public int getIdTramitePrerefundido() {
		return idTramitePrerefundido;
	}

	public void setIdTramitePrerefundido(int idTramitePrerefundido) {
		this.idTramitePrerefundido = idTramitePrerefundido;
	}

	public int getIdTramiteVigente() {
		return idTramiteVigente;
	}

	public void setIdTramiteVigente(int idTramiteVigente) {
		this.idTramiteVigente = idTramiteVigente;
	}

	@Basic @Temporal(TemporalType.TIMESTAMP) 
	public Date getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public boolean isConsolidado() {
		return consolidado;
	}

	public void setConsolidado(boolean consolidado) {
		this.consolidado = consolidado;
	}

	public int getIdultimotramprerefundido() {
		return idultimotramprerefundido;
	}

	public void setIdultimotramprerefundido(int idultimotramprerefundido) {
		this.idultimotramprerefundido = idultimotramprerefundido;
	}

	public int getIdprocesorefundido() {
		return idprocesorefundido;
	}

	public void setIdprocesorefundido(int idprocesorefundido) {
		this.idprocesorefundido = idprocesorefundido;
	}

	

}

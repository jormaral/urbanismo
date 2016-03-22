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

package com.mitc.redes.editorfip.entidades.rpm.conversorfipsipu;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Version;

import org.hibernate.validator.NotNull;
import org.jboss.seam.annotations.Name;

import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Entidad;

@Entity
@Name("datosAuxiliaresImportarEntidad")
@Table(name="datosauxiliaresimportarentidad", schema = "conversorfipsipu")
@SequenceGenerator( name = "DETSEQ", sequenceName = "conversorfipsipu.conversorfipsipu_datosauxiliaresimportarentidad_id_seq",allocationSize=1 )
public class DatosAuxiliaresImportarEntidad implements Serializable
{
    
    private Long id;
    private Integer version;
    private String descripcionEtiqueta;
    private String observaciones;
    private Entidad entidad;
    

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

	public String getDescripcionEtiqueta() {
		return descripcionEtiqueta;
	}

	public void setDescripcionEtiqueta(String descripcionEtiqueta) {
		this.descripcionEtiqueta = descripcionEtiqueta;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	@OneToOne
	public Entidad getEntidad() {
		return entidad;
	}

	public void setEntidad(Entidad entidad) {
		this.entidad = entidad;
	}

    

}

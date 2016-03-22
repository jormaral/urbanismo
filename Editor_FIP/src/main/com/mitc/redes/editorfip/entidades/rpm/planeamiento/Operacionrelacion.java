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

package com.mitc.redes.editorfip.entidades.rpm.planeamiento;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.validator.NotNull;

import com.mitc.redes.editorfip.entidades.fipxml.PropiedadesAdscripcion;

@Entity
@Table(name = "operacionrelacion", schema = "planeamiento")
@SequenceGenerator( name = "DETSEQ", sequenceName = "planeamiento.planeamiento_operacionrelacion_iden_seq",allocationSize=1 )
public class Operacionrelacion implements java.io.Serializable {

	private int iden;
	private Relacion relacion;
	private Operacion operacion;
	private int idtipooperacionrel;
	
	private PropiedadesAdscripcion propiedadesadscripcion;

	public Operacionrelacion() {
	}

	public Operacionrelacion(int iden, Relacion relacion, Operacion operacion,
			int idtipooperacionrel) {
		this.iden = iden;
		this.relacion = relacion;
		this.operacion = operacion;
		this.idtipooperacionrel = idtipooperacionrel;
	}

	@Id
	@Column(name = "iden", unique = true, nullable = false, insertable = false)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="DETSEQ")
	public int getIden() {
		return this.iden;
	}

	public void setIden(int iden) {
		this.iden = iden;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idrelacion", nullable = false)
	@NotNull
	public Relacion getRelacion() {
		return this.relacion;
	}

	public void setRelacion(Relacion relacion) {
		this.relacion = relacion;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idoperacion", nullable = false)
	@NotNull
	public Operacion getOperacion() {
		return this.operacion;
	}

	public void setOperacion(Operacion operacion) {
		this.operacion = operacion;
	}

	@Column(name = "idtipooperacionrel", nullable = false)
	public int getIdtipooperacionrel() {
		return this.idtipooperacionrel;
	}

	public void setIdtipooperacionrel(int idtipooperacionrel) {
		this.idtipooperacionrel = idtipooperacionrel;
	}
	
	@Transient
	 public PropiedadesAdscripcion getPropiedadesAdscripcion() {
        return this.propiedadesadscripcion;
    }
    
    public void setPropiedadesAdscripcion(PropiedadesAdscripcion propiedadesadscripcion) {
        this.propiedadesadscripcion = propiedadesadscripcion;
    }


}

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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.validator.Length;
import org.jboss.seam.annotations.Name;

@Entity
@Name("regimenesEspecificosSimplificadosDeterminacion")
@Table(name="regimenesespecificossimplificadosdeterminacion", schema = "gestionfip")
@SequenceGenerator( name = "DETSEQ", sequenceName = "gestionfip.gestionfip_regespecificossimplificadosdeterminacion_id_seq",allocationSize=1 )
public class RegimenesEspecificosSimplificadosDeterminacion implements Serializable{

	/**
	 * 
	 */
	private int id;
	private static final long serialVersionUID = 3631305702837335080L;
	private String nombreRegimenEspecifico="";
	private String textoRegimenEspecifico ="";
	
	private int idRegimenEspecifico = 0;
	
	private CUAdaptadaSipu cuAdaptadaSipu;
	
	
	public RegimenesEspecificosSimplificadosDeterminacion() {
		super();
		
	}

	public RegimenesEspecificosSimplificadosDeterminacion(String nombreRegimenEspecifico,
			String textoRegimenEspecifico) {
		super();
		this.nombreRegimenEspecifico = nombreRegimenEspecifico;
		this.textoRegimenEspecifico = textoRegimenEspecifico;
		this.idRegimenEspecifico = 0;
	}

	@Id
	@Column(name = "id", unique = true, nullable = false, insertable = false)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="DETSEQ")
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

	public String getNombreRegimenEspecifico() {
		return nombreRegimenEspecifico;
	}


	public void setNombreRegimenEspecifico(String nombreRegimenEspecifico) {
		this.nombreRegimenEspecifico = nombreRegimenEspecifico;
	}

	public String getTextoRegimenEspecifico() {
		return textoRegimenEspecifico;
	}


	public void setTextoRegimenEspecifico(String textoRegimenEspecifico) {
		this.textoRegimenEspecifico = textoRegimenEspecifico;
	}


	public int getIdRegimenEspecifico() {
		return idRegimenEspecifico;
	}


	public void setIdRegimenEspecifico(int idRegimenEspecifico) {
		this.idRegimenEspecifico = idRegimenEspecifico;
	}

	@ManyToOne
	public CUAdaptadaSipu getCuAdaptadaSipu() {
		return cuAdaptadaSipu;
	}


	public void setCuAdaptadaSipu(CUAdaptadaSipu cuAdaptadaSipu) {
		this.cuAdaptadaSipu = cuAdaptadaSipu;
	}


	
	
	
	
	
}

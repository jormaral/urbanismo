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
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.validator.Length;
import org.jboss.seam.annotations.Name;

import com.mitc.redes.editorfip.entidades.interfazgrafico.RegimenesEspecificosSimplificadosDTO;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Determinacion;

@Entity
@Name("cUAdaptadaSipu")
@Table(name="cuadaptadasipu", schema = "gestionfip")
@SequenceGenerator( name = "DETSEQ", sequenceName = "gestionfip.gestionfip_cuadaptadasipu_id_seq",allocationSize=1 )
public class CUAdaptadaSipu implements Serializable{
	
	/**
	 * 
	 */
	private int id;
	private int idEntidad;
	private String claveEntidad;
	private String nombreEntidad;
	
	private int idUsoActo;
	private String apartadoUsoActo;
	private String nombreUsoActo;
	
	private int idDetUsoActo;
	private int idDetPBUsoActo;
	private String apartadoDetUsoActo;
	private String nombreDetUsoActo;
	
	private int idDetValorUsoActo;
	private int idDetPBValorUsoActo;
	private String apartadoDetValorUsoActo;
	private String nombreDetValorUsoActo;
	
	private List<RegimenesEspecificosSimplificadosUsoActos> regEspecificoUsoActo;
	
	private int idDeterminacion;
	private String apartadoDeterminacion;
	private String nombreDeterminacion;
	
	private String valor;
	private int idDetValorReferencia;
	private String apartadoValorReferencia;
	private String nombreValorReferencia;
	
	private int idRegimen;
	private int idCaso;
	private int idTramite;
	
	private int idCasoAplicacion;
	private int idFilaExcel;
	
	private List<RegimenesEspecificosSimplificadosDeterminacion> regEspecificoDeterminacion;

	private boolean cuAdptadaAntigual;
	public CUAdaptadaSipu() {
		// TODO Auto-generated constructor stub
		idRegimen = 0;
		idCaso = 0;
		idCasoAplicacion = 0;
		cuAdptadaAntigual = false;
		regEspecificoUsoActo = new ArrayList<RegimenesEspecificosSimplificadosUsoActos>();
		regEspecificoDeterminacion = new ArrayList<RegimenesEspecificosSimplificadosDeterminacion>();
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
    
	public int getIdEntidad() {
		return idEntidad;
	}

	public void setIdEntidad(int idEntidad) {
		this.idEntidad = idEntidad;
	}

	public String getClaveEntidad() {
		return claveEntidad;
	}

	public void setClaveEntidad(String claveEntidad) {
		this.claveEntidad = claveEntidad;
	}

	public String getNombreEntidad() {
		return nombreEntidad;
	}

	public void setNombreEntidad(String nombreEntidad) {
		this.nombreEntidad = nombreEntidad;
	}

	public int getIdUsoActo() {
		return idUsoActo;
	}

	public void setIdUsoActo(int idUsoActo) {
		this.idUsoActo = idUsoActo;
	}

	public String getApartadoUsoActo() {
		return apartadoUsoActo;
	}

	public void setApartadoUsoActo(String apartadoUsoActo) {
		this.apartadoUsoActo = apartadoUsoActo;
	}

	@Column(name = "nombreusoacto", length = 1000)
	@Length(max = 1000)
	public String getNombreUsoActo() {
		return nombreUsoActo;
	}

	public void setNombreUsoActo(String nombreUsoActo) {
		this.nombreUsoActo = nombreUsoActo;
	}

	public int getIdDetUsoActo() {
		return idDetUsoActo;
	}

	public void setIdDetUsoActo(int idDetUsoActo) {
		this.idDetUsoActo = idDetUsoActo;
	}

	public int getIdDetPBUsoActo() {
		return idDetPBUsoActo;
	}

	public void setIdDetPBUsoActo(int idDetPBUsoActo) {
		this.idDetPBUsoActo = idDetPBUsoActo;
	}

	public String getApartadoDetUsoActo() {
		return apartadoDetUsoActo;
	}

	public void setApartadoDetUsoActo(String apartadoDetUsoActo) {
		this.apartadoDetUsoActo = apartadoDetUsoActo;
	}
	
	@Column(name = "nombredetusoacto", length = 1000)
	@Length(max = 1000)
	public String getNombreDetUsoActo() {
		return nombreDetUsoActo;
	}

	public void setNombreDetUsoActo(String nombreDetUsoActo) {
		this.nombreDetUsoActo = nombreDetUsoActo;
	}

	public int getIdDetValorUsoActo() {
		return idDetValorUsoActo;
	}

	public void setIdDetValorUsoActo(int idDetValorUsoActo) {
		this.idDetValorUsoActo = idDetValorUsoActo;
	}

	public int getIdDetPBValorUsoActo() {
		return idDetPBValorUsoActo;
	}

	public void setIdDetPBValorUsoActo(int idDetPBValorUsoActo) {
		this.idDetPBValorUsoActo = idDetPBValorUsoActo;
	}

	public String getApartadoDetValorUsoActo() {
		return apartadoDetValorUsoActo;
	}

	public void setApartadoDetValorUsoActo(String apartadoDetValorUsoActo) {
		this.apartadoDetValorUsoActo = apartadoDetValorUsoActo;
	}

	@Column(name = "nombredetvalorusoacto", length = 1000)
	@Length(max = 1000)
	public String getNombreDetValorUsoActo() {
		return nombreDetValorUsoActo;
	}

	public void setNombreDetValorUsoActo(String nombreDetValorUsoActo) {
		this.nombreDetValorUsoActo = nombreDetValorUsoActo;
	}
	
	@OneToMany
	public List<RegimenesEspecificosSimplificadosUsoActos> getRegEspecificoUsoActo() {
		return regEspecificoUsoActo;
	}

	public void setRegEspecificoUsoActo(
			List<RegimenesEspecificosSimplificadosUsoActos> regEspecificoUsoActo) {
		this.regEspecificoUsoActo = regEspecificoUsoActo;
	}

	public int getIdDeterminacion() {
		return idDeterminacion;
	}

	public void setIdDeterminacion(int idDeterminacion) {
		this.idDeterminacion = idDeterminacion;
	}

	public String getApartadoDeterminacion() {
		return apartadoDeterminacion;
	}

	public void setApartadoDeterminacion(String apartadoDeterminacion) {
		this.apartadoDeterminacion = apartadoDeterminacion;
	}

	@Column(name = "nombredeterminacion", length = 1000)
	@Length(max = 1000)
	public String getNombreDeterminacion() {
		return nombreDeterminacion;
	}

	public void setNombreDeterminacion(String nombreDeterminacion) {
		this.nombreDeterminacion = nombreDeterminacion;
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

	public int getIdDetValorReferencia() {
		return idDetValorReferencia;
	}

	public void setIdDetValorReferencia(int idDetValorReferencia) {
		this.idDetValorReferencia = idDetValorReferencia;
	}

	public String getApartadoValorReferencia() {
		return apartadoValorReferencia;
	}

	public void setApartadoValorReferencia(String apartadoValorReferencia) {
		this.apartadoValorReferencia = apartadoValorReferencia;
	}

	public String getNombreValorReferencia() {
		return nombreValorReferencia;
	}

	public void setNombreValorReferencia(String nombreValorReferencia) {
		this.nombreValorReferencia = nombreValorReferencia;
	}

	public int getIdRegimen() {
		return idRegimen;
	}

	public void setIdRegimen(int idRegimen) {
		this.idRegimen = idRegimen;
	}
	
	@OneToMany
	public List<RegimenesEspecificosSimplificadosDeterminacion> getRegEspecificoDeterminacion() {
		return regEspecificoDeterminacion;
	}

	public void setRegEspecificoDeterminacion(
			List<RegimenesEspecificosSimplificadosDeterminacion> regEspecificoDeterminacion) {
		this.regEspecificoDeterminacion = regEspecificoDeterminacion;
	}

	public int getIdCaso() {
		return idCaso;
	}

	public void setIdCaso(int idCaso) {
		this.idCaso = idCaso;
	}

	public boolean isCuAdptadaAntigual() {
		return cuAdptadaAntigual;
	}

	public void setCuAdptadaAntigual(boolean cuAdptadaAntigual) {
		this.cuAdptadaAntigual = cuAdptadaAntigual;
	}

	public int getIdTramite() {
		return idTramite;
	}

	public void setIdTramite(int idTramite) {
		this.idTramite = idTramite;
	}

	public int getIdCasoAplicacion() {
		return idCasoAplicacion;
	}

	public void setIdCasoAplicacion(int idCasoAplicacion) {
		this.idCasoAplicacion = idCasoAplicacion;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getIdFilaExcel() {
		return idFilaExcel;
	}

	public void setIdFilaExcel(int idFilaExcel) {
		this.idFilaExcel = idFilaExcel;
	}
	
}

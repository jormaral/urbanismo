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

package com.mitc.redes.editorfip.entidades.rpm.explotacion;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;

@Entity
@Table(name = "procedimiento_gr", schema = "explotacion")
public class ProcedimientoGr implements java.io.Serializable {

	private int iden;
	private Capagrupo capagrupo;
	private int idsimbologia;
	private String valortematico;
	private int ordenGr;

	public ProcedimientoGr() {
	}

	public ProcedimientoGr(int iden, Capagrupo capagrupo, int idsimbologia,
			String valortematico, int ordenGr) {
		this.iden = iden;
		this.capagrupo = capagrupo;
		this.idsimbologia = idsimbologia;
		this.valortematico = valortematico;
		this.ordenGr = ordenGr;
	}

	@Id
	@Column(name = "iden", unique = true, nullable = false)
	public int getIden() {
		return this.iden;
	}

	public void setIden(int iden) {
		this.iden = iden;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idcapagrupo", nullable = false)
	@NotNull
	public Capagrupo getCapagrupo() {
		return this.capagrupo;
	}

	public void setCapagrupo(Capagrupo capagrupo) {
		this.capagrupo = capagrupo;
	}

	@Column(name = "idsimbologia", nullable = false)
	public int getIdsimbologia() {
		return this.idsimbologia;
	}

	public void setIdsimbologia(int idsimbologia) {
		this.idsimbologia = idsimbologia;
	}

	@Column(name = "valortematico", nullable = false, length = 50)
	@NotNull
	@Length(max = 50)
	public String getValortematico() {
		return this.valortematico;
	}

	public void setValortematico(String valortematico) {
		this.valortematico = valortematico;
	}

	@Column(name = "orden_gr", nullable = false)
	public int getOrdenGr() {
		return this.ordenGr;
	}

	public void setOrdenGr(int ordenGr) {
		this.ordenGr = ordenGr;
	}

}

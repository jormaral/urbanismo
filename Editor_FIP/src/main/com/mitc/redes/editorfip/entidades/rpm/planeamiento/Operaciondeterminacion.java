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

import org.hibernate.validator.NotNull;

@Entity
@Table(name = "operaciondeterminacion", schema = "planeamiento")
@SequenceGenerator( name = "DETSEQ", sequenceName = "planeamiento.planeamiento_operaciondeterminacion_iden_seq",allocationSize=1 )
public class Operaciondeterminacion implements java.io.Serializable {

	private int iden;
	private Operacion operacion;
	private Determinacion determinacionByIddeterminacion;
	private Determinacion determinacionByIddeterminacionoperadora;
	private int idtipooperaciondet;

	public Operaciondeterminacion() {
	}

	public Operaciondeterminacion(int iden, Operacion operacion,
			Determinacion determinacionByIddeterminacion,
			Determinacion determinacionByIddeterminacionoperadora,
			int idtipooperaciondet) {
		this.iden = iden;
		this.operacion = operacion;
		this.determinacionByIddeterminacion = determinacionByIddeterminacion;
		this.determinacionByIddeterminacionoperadora = determinacionByIddeterminacionoperadora;
		this.idtipooperaciondet = idtipooperaciondet;
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
	@JoinColumn(name = "idoperacion", nullable = false)
	@NotNull
	public Operacion getOperacion() {
		return this.operacion;
	}

	public void setOperacion(Operacion operacion) {
		this.operacion = operacion;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "iddeterminacion", nullable = false)
	@NotNull
	public Determinacion getDeterminacionByIddeterminacion() {
		return this.determinacionByIddeterminacion;
	}

	public void setDeterminacionByIddeterminacion(
			Determinacion determinacionByIddeterminacion) {
		this.determinacionByIddeterminacion = determinacionByIddeterminacion;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "iddeterminacionoperadora", nullable = false)
	@NotNull
	public Determinacion getDeterminacionByIddeterminacionoperadora() {
		return this.determinacionByIddeterminacionoperadora;
	}

	public void setDeterminacionByIddeterminacionoperadora(
			Determinacion determinacionByIddeterminacionoperadora) {
		this.determinacionByIddeterminacionoperadora = determinacionByIddeterminacionoperadora;
	}

	@Column(name = "idtipooperaciondet", nullable = false)
	public int getIdtipooperaciondet() {
		return this.idtipooperaciondet;
	}

	public void setIdtipooperaciondet(int idtipooperaciondet) {
		this.idtipooperaciondet = idtipooperaciondet;
	}

}

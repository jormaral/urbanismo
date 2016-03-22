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
@Table(name = "vectorrelacion", schema = "planeamiento")
@SequenceGenerator( name = "DETSEQ", sequenceName = "planeamiento.planeamiento_vectorrelacion_iden_seq",allocationSize=1 )
public class Vectorrelacion implements java.io.Serializable {

	private int iden;
	private Relacion relacion;
	private int iddefvector;
	private int valor;

	public Vectorrelacion() {
	}

	public Vectorrelacion(int iden, Relacion relacion, int iddefvector,
			int valor) {
		this.iden = iden;
		this.relacion = relacion;
		this.iddefvector = iddefvector;
		this.valor = valor;
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

	@Column(name = "iddefvector", nullable = false)
	public int getIddefvector() {
		return this.iddefvector;
	}

	public void setIddefvector(int iddefvector) {
		this.iddefvector = iddefvector;
	}

	@Column(name = "valor", nullable = false)
	public int getValor() {
		return this.valor;
	}

	public void setValor(int valor) {
		this.valor = valor;
	}

}

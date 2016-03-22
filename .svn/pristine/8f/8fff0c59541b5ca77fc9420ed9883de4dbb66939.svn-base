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

package com.mitc.redes.editorfip.entidades.rpm.diccionario;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.validator.Length;

@Entity
@Table(name = "literal", schema = "diccionario")
public class Literal implements java.io.Serializable {

	private int iden;
	private String comentario;
	private Set<Tipooperacionplan> tipooperacionplans = new HashSet<Tipooperacionplan>(
			0);
	private Set<Instrumentoplan> instrumentoplans = new HashSet<Instrumentoplan>(
			0);
	private Set<Tipooperaciondeterminacion> tipooperaciondeterminacions = new HashSet<Tipooperaciondeterminacion>(
			0);
	private Set<Organo> organos = new HashSet<Organo>(0);
	private Set<Centroproduccion> centroproduccions = new HashSet<Centroproduccion>(
			0);
	private Set<Tipodocumento> tipodocumentos = new HashSet<Tipodocumento>(0);
	private Set<Sentido> sentidos = new HashSet<Sentido>(0);
	private Set<Boletin> boletins = new HashSet<Boletin>(0);
	private Set<Tipoentidad> tipoentidads = new HashSet<Tipoentidad>(0);
	private Set<Naturaleza> naturalezas = new HashSet<Naturaleza>(0);
	private Set<Tipooperacionplan> tipooperacionplans_1 = new HashSet<Tipooperacionplan>(
			0);
	private Set<Tipooperaciondeterminacion> tipooperaciondeterminacions_1 = new HashSet<Tipooperaciondeterminacion>(
			0);
	private Set<Boletin> boletins_1 = new HashSet<Boletin>(0);
	private Set<Tipooperacionrelacion> tipooperacionrelacions = new HashSet<Tipooperacionrelacion>(
			0);
	private Set<Tipooperacionrelacion> tipooperacionrelacions_1 = new HashSet<Tipooperacionrelacion>(
			0);
	private Set<Defpropiedad> defpropiedads = new HashSet<Defpropiedad>(0);
	private Set<Centroproduccion> centroproduccions_1 = new HashSet<Centroproduccion>(
			0);
	private Set<Defrelacion> defrelacions = new HashSet<Defrelacion>(0);
	private Set<Tipooperacionentidad> tipooperacionentidads = new HashSet<Tipooperacionentidad>(
			0);
	private Set<Grupodocumento> grupodocumentos = new HashSet<Grupodocumento>(0);
	private Set<Defrelacion> defrelacions_1 = new HashSet<Defrelacion>(0);
	private Set<Tipodocumento> tipodocumentos_1 = new HashSet<Tipodocumento>(0);
	private Set<Caracterdeterminacion> caracterdeterminacions = new HashSet<Caracterdeterminacion>(
			0);
	private Set<Naturaleza> naturalezas_1 = new HashSet<Naturaleza>(0);
	private Set<Tipoentidad> tipoentidads_1 = new HashSet<Tipoentidad>(0);
	private Set<Defvector> defvectors = new HashSet<Defvector>(0);
	private Set<Sentido> sentidos_1 = new HashSet<Sentido>(0);
	private Set<Traduccion> traduccions = new HashSet<Traduccion>(0);
	private Set<Traduccion> traduccions_1 = new HashSet<Traduccion>(0);
	private Set<Defpropiedad> defpropiedads_1 = new HashSet<Defpropiedad>(0);
	private Set<Tipotramite> tipotramites = new HashSet<Tipotramite>(0);
	private Set<Tipooperacionentidad> tipooperacionentidads_1 = new HashSet<Tipooperacionentidad>(
			0);
	private Set<Organo> organos_1 = new HashSet<Organo>(0);
	private Set<Tipodefpropiedad> tipodefpropiedads = new HashSet<Tipodefpropiedad>(
			0);
	private Set<Ambito> ambitos = new HashSet<Ambito>(0);
	private Set<Tipodefpropiedad> tipodefpropiedads_1 = new HashSet<Tipodefpropiedad>(
			0);
	private Set<Tipotramite> tipotramites_1 = new HashSet<Tipotramite>(0);
	private Set<Tipoambito> tipoambitos = new HashSet<Tipoambito>(0);
	private Set<Tipoambito> tipoambitos_1 = new HashSet<Tipoambito>(0);
	private Set<Defvector> defvectors_1 = new HashSet<Defvector>(0);
	private Set<Caracterdeterminacion> caracterdeterminacions_1 = new HashSet<Caracterdeterminacion>(
			0);
	private Set<Instrumentoplan> instrumentoplans_1 = new HashSet<Instrumentoplan>(
			0);
	private Set<Ambito> ambitos_1 = new HashSet<Ambito>(0);
	private Set<Grupodocumento> grupodocumentos_1 = new HashSet<Grupodocumento>(
			0);

	public Literal() {
	}

	public Literal(int iden) {
		this.iden = iden;
	}

	public Literal(int iden, String comentario,
			Set<Tipooperacionplan> tipooperacionplans,
			Set<Instrumentoplan> instrumentoplans,
			Set<Tipooperaciondeterminacion> tipooperaciondeterminacions,
			Set<Organo> organos, Set<Centroproduccion> centroproduccions,
			Set<Tipodocumento> tipodocumentos, Set<Sentido> sentidos,
			Set<Boletin> boletins, Set<Tipoentidad> tipoentidads,
			Set<Naturaleza> naturalezas,
			Set<Tipooperacionplan> tipooperacionplans_1,
			Set<Tipooperaciondeterminacion> tipooperaciondeterminacions_1,
			Set<Boletin> boletins_1,
			Set<Tipooperacionrelacion> tipooperacionrelacions,
			Set<Tipooperacionrelacion> tipooperacionrelacions_1,
			Set<Defpropiedad> defpropiedads,
			Set<Centroproduccion> centroproduccions_1,
			Set<Defrelacion> defrelacions,
			Set<Tipooperacionentidad> tipooperacionentidads,
			Set<Grupodocumento> grupodocumentos,
			Set<Defrelacion> defrelacions_1,
			Set<Tipodocumento> tipodocumentos_1,
			Set<Caracterdeterminacion> caracterdeterminacions,
			Set<Naturaleza> naturalezas_1, Set<Tipoentidad> tipoentidads_1,
			Set<Defvector> defvectors, Set<Sentido> sentidos_1,
			Set<Traduccion> traduccions, Set<Traduccion> traduccions_1,
			Set<Defpropiedad> defpropiedads_1, Set<Tipotramite> tipotramites,
			Set<Tipooperacionentidad> tipooperacionentidads_1,
			Set<Organo> organos_1, Set<Tipodefpropiedad> tipodefpropiedads,
			Set<Ambito> ambitos, Set<Tipodefpropiedad> tipodefpropiedads_1,
			Set<Tipotramite> tipotramites_1, Set<Tipoambito> tipoambitos,
			Set<Tipoambito> tipoambitos_1, Set<Defvector> defvectors_1,
			Set<Caracterdeterminacion> caracterdeterminacions_1,
			Set<Instrumentoplan> instrumentoplans_1, Set<Ambito> ambitos_1,
			Set<Grupodocumento> grupodocumentos_1) {
		this.iden = iden;
		this.comentario = comentario;
		this.tipooperacionplans = tipooperacionplans;
		this.instrumentoplans = instrumentoplans;
		this.tipooperaciondeterminacions = tipooperaciondeterminacions;
		this.organos = organos;
		this.centroproduccions = centroproduccions;
		this.tipodocumentos = tipodocumentos;
		this.sentidos = sentidos;
		this.boletins = boletins;
		this.tipoentidads = tipoentidads;
		this.naturalezas = naturalezas;
		this.tipooperacionplans_1 = tipooperacionplans_1;
		this.tipooperaciondeterminacions_1 = tipooperaciondeterminacions_1;
		this.boletins_1 = boletins_1;
		this.tipooperacionrelacions = tipooperacionrelacions;
		this.tipooperacionrelacions_1 = tipooperacionrelacions_1;
		this.defpropiedads = defpropiedads;
		this.centroproduccions_1 = centroproduccions_1;
		this.defrelacions = defrelacions;
		this.tipooperacionentidads = tipooperacionentidads;
		this.grupodocumentos = grupodocumentos;
		this.defrelacions_1 = defrelacions_1;
		this.tipodocumentos_1 = tipodocumentos_1;
		this.caracterdeterminacions = caracterdeterminacions;
		this.naturalezas_1 = naturalezas_1;
		this.tipoentidads_1 = tipoentidads_1;
		this.defvectors = defvectors;
		this.sentidos_1 = sentidos_1;
		this.traduccions = traduccions;
		this.traduccions_1 = traduccions_1;
		this.defpropiedads_1 = defpropiedads_1;
		this.tipotramites = tipotramites;
		this.tipooperacionentidads_1 = tipooperacionentidads_1;
		this.organos_1 = organos_1;
		this.tipodefpropiedads = tipodefpropiedads;
		this.ambitos = ambitos;
		this.tipodefpropiedads_1 = tipodefpropiedads_1;
		this.tipotramites_1 = tipotramites_1;
		this.tipoambitos = tipoambitos;
		this.tipoambitos_1 = tipoambitos_1;
		this.defvectors_1 = defvectors_1;
		this.caracterdeterminacions_1 = caracterdeterminacions_1;
		this.instrumentoplans_1 = instrumentoplans_1;
		this.ambitos_1 = ambitos_1;
		this.grupodocumentos_1 = grupodocumentos_1;
	}

	@Id
	@Column(name = "iden", unique = true, nullable = false)
	@GeneratedValue(strategy=GenerationType.SEQUENCE)
	public int getIden() {
		return this.iden;
	}

	public void setIden(int iden) {
		this.iden = iden;
	}

	@Column(name = "comentario", length = 1000)
	@Length(max = 1000)
	public String getComentario() {
		return this.comentario;
	}

	public void setComentario(String comentario) {
		this.comentario = comentario;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "literal")
	public Set<Tipooperacionplan> getTipooperacionplans() {
		return this.tipooperacionplans;
	}

	public void setTipooperacionplans(Set<Tipooperacionplan> tipooperacionplans) {
		this.tipooperacionplans = tipooperacionplans;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "literal")
	public Set<Instrumentoplan> getInstrumentoplans() {
		return this.instrumentoplans;
	}

	public void setInstrumentoplans(Set<Instrumentoplan> instrumentoplans) {
		this.instrumentoplans = instrumentoplans;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "literal")
	public Set<Tipooperaciondeterminacion> getTipooperaciondeterminacions() {
		return this.tipooperaciondeterminacions;
	}

	public void setTipooperaciondeterminacions(
			Set<Tipooperaciondeterminacion> tipooperaciondeterminacions) {
		this.tipooperaciondeterminacions = tipooperaciondeterminacions;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "literal")
	public Set<Organo> getOrganos() {
		return this.organos;
	}

	public void setOrganos(Set<Organo> organos) {
		this.organos = organos;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "literal")
	public Set<Centroproduccion> getCentroproduccions() {
		return this.centroproduccions;
	}

	public void setCentroproduccions(Set<Centroproduccion> centroproduccions) {
		this.centroproduccions = centroproduccions;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "literal")
	public Set<Tipodocumento> getTipodocumentos() {
		return this.tipodocumentos;
	}

	public void setTipodocumentos(Set<Tipodocumento> tipodocumentos) {
		this.tipodocumentos = tipodocumentos;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "literal")
	public Set<Sentido> getSentidos() {
		return this.sentidos;
	}

	public void setSentidos(Set<Sentido> sentidos) {
		this.sentidos = sentidos;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "literal")
	public Set<Boletin> getBoletins() {
		return this.boletins;
	}

	public void setBoletins(Set<Boletin> boletins) {
		this.boletins = boletins;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "literal")
	public Set<Tipoentidad> getTipoentidads() {
		return this.tipoentidads;
	}

	public void setTipoentidads(Set<Tipoentidad> tipoentidads) {
		this.tipoentidads = tipoentidads;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "literal")
	public Set<Naturaleza> getNaturalezas() {
		return this.naturalezas;
	}

	public void setNaturalezas(Set<Naturaleza> naturalezas) {
		this.naturalezas = naturalezas;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "literal")
	public Set<Tipooperacionplan> getTipooperacionplans_1() {
		return this.tipooperacionplans_1;
	}

	public void setTipooperacionplans_1(
			Set<Tipooperacionplan> tipooperacionplans_1) {
		this.tipooperacionplans_1 = tipooperacionplans_1;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "literal")
	public Set<Tipooperaciondeterminacion> getTipooperaciondeterminacions_1() {
		return this.tipooperaciondeterminacions_1;
	}

	public void setTipooperaciondeterminacions_1(
			Set<Tipooperaciondeterminacion> tipooperaciondeterminacions_1) {
		this.tipooperaciondeterminacions_1 = tipooperaciondeterminacions_1;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "literal")
	public Set<Boletin> getBoletins_1() {
		return this.boletins_1;
	}

	public void setBoletins_1(Set<Boletin> boletins_1) {
		this.boletins_1 = boletins_1;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "literal")
	public Set<Tipooperacionrelacion> getTipooperacionrelacions() {
		return this.tipooperacionrelacions;
	}

	public void setTipooperacionrelacions(
			Set<Tipooperacionrelacion> tipooperacionrelacions) {
		this.tipooperacionrelacions = tipooperacionrelacions;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "literal")
	public Set<Tipooperacionrelacion> getTipooperacionrelacions_1() {
		return this.tipooperacionrelacions_1;
	}

	public void setTipooperacionrelacions_1(
			Set<Tipooperacionrelacion> tipooperacionrelacions_1) {
		this.tipooperacionrelacions_1 = tipooperacionrelacions_1;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "literal")
	public Set<Defpropiedad> getDefpropiedads() {
		return this.defpropiedads;
	}

	public void setDefpropiedads(Set<Defpropiedad> defpropiedads) {
		this.defpropiedads = defpropiedads;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "literal")
	public Set<Centroproduccion> getCentroproduccions_1() {
		return this.centroproduccions_1;
	}

	public void setCentroproduccions_1(Set<Centroproduccion> centroproduccions_1) {
		this.centroproduccions_1 = centroproduccions_1;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "literal")
	public Set<Defrelacion> getDefrelacions() {
		return this.defrelacions;
	}

	public void setDefrelacions(Set<Defrelacion> defrelacions) {
		this.defrelacions = defrelacions;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "literal")
	public Set<Tipooperacionentidad> getTipooperacionentidads() {
		return this.tipooperacionentidads;
	}

	public void setTipooperacionentidads(
			Set<Tipooperacionentidad> tipooperacionentidads) {
		this.tipooperacionentidads = tipooperacionentidads;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "literal")
	public Set<Grupodocumento> getGrupodocumentos() {
		return this.grupodocumentos;
	}

	public void setGrupodocumentos(Set<Grupodocumento> grupodocumentos) {
		this.grupodocumentos = grupodocumentos;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "literal")
	public Set<Defrelacion> getDefrelacions_1() {
		return this.defrelacions_1;
	}

	public void setDefrelacions_1(Set<Defrelacion> defrelacions_1) {
		this.defrelacions_1 = defrelacions_1;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "literal")
	public Set<Tipodocumento> getTipodocumentos_1() {
		return this.tipodocumentos_1;
	}

	public void setTipodocumentos_1(Set<Tipodocumento> tipodocumentos_1) {
		this.tipodocumentos_1 = tipodocumentos_1;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "literal")
	public Set<Caracterdeterminacion> getCaracterdeterminacions() {
		return this.caracterdeterminacions;
	}

	public void setCaracterdeterminacions(
			Set<Caracterdeterminacion> caracterdeterminacions) {
		this.caracterdeterminacions = caracterdeterminacions;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "literal")
	public Set<Naturaleza> getNaturalezas_1() {
		return this.naturalezas_1;
	}

	public void setNaturalezas_1(Set<Naturaleza> naturalezas_1) {
		this.naturalezas_1 = naturalezas_1;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "literal")
	public Set<Tipoentidad> getTipoentidads_1() {
		return this.tipoentidads_1;
	}

	public void setTipoentidads_1(Set<Tipoentidad> tipoentidads_1) {
		this.tipoentidads_1 = tipoentidads_1;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "literal")
	public Set<Defvector> getDefvectors() {
		return this.defvectors;
	}

	public void setDefvectors(Set<Defvector> defvectors) {
		this.defvectors = defvectors;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "literal")
	public Set<Sentido> getSentidos_1() {
		return this.sentidos_1;
	}

	public void setSentidos_1(Set<Sentido> sentidos_1) {
		this.sentidos_1 = sentidos_1;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "literal")
	public Set<Traduccion> getTraduccions() {
		return this.traduccions;
	}

	public void setTraduccions(Set<Traduccion> traduccions) {
		this.traduccions = traduccions;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "literal")
	public Set<Traduccion> getTraduccions_1() {
		return this.traduccions_1;
	}

	public void setTraduccions_1(Set<Traduccion> traduccions_1) {
		this.traduccions_1 = traduccions_1;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "literal")
	public Set<Defpropiedad> getDefpropiedads_1() {
		return this.defpropiedads_1;
	}

	public void setDefpropiedads_1(Set<Defpropiedad> defpropiedads_1) {
		this.defpropiedads_1 = defpropiedads_1;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "literal")
	public Set<Tipotramite> getTipotramites() {
		return this.tipotramites;
	}

	public void setTipotramites(Set<Tipotramite> tipotramites) {
		this.tipotramites = tipotramites;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "literal")
	public Set<Tipooperacionentidad> getTipooperacionentidads_1() {
		return this.tipooperacionentidads_1;
	}

	public void setTipooperacionentidads_1(
			Set<Tipooperacionentidad> tipooperacionentidads_1) {
		this.tipooperacionentidads_1 = tipooperacionentidads_1;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "literal")
	public Set<Organo> getOrganos_1() {
		return this.organos_1;
	}

	public void setOrganos_1(Set<Organo> organos_1) {
		this.organos_1 = organos_1;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "literal")
	public Set<Tipodefpropiedad> getTipodefpropiedads() {
		return this.tipodefpropiedads;
	}

	public void setTipodefpropiedads(Set<Tipodefpropiedad> tipodefpropiedads) {
		this.tipodefpropiedads = tipodefpropiedads;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "literal")
	public Set<Ambito> getAmbitos() {
		return this.ambitos;
	}

	public void setAmbitos(Set<Ambito> ambitos) {
		this.ambitos = ambitos;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "literal")
	public Set<Tipodefpropiedad> getTipodefpropiedads_1() {
		return this.tipodefpropiedads_1;
	}

	public void setTipodefpropiedads_1(Set<Tipodefpropiedad> tipodefpropiedads_1) {
		this.tipodefpropiedads_1 = tipodefpropiedads_1;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "literal")
	public Set<Tipotramite> getTipotramites_1() {
		return this.tipotramites_1;
	}

	public void setTipotramites_1(Set<Tipotramite> tipotramites_1) {
		this.tipotramites_1 = tipotramites_1;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "literal")
	public Set<Tipoambito> getTipoambitos() {
		return this.tipoambitos;
	}

	public void setTipoambitos(Set<Tipoambito> tipoambitos) {
		this.tipoambitos = tipoambitos;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "literal")
	public Set<Tipoambito> getTipoambitos_1() {
		return this.tipoambitos_1;
	}

	public void setTipoambitos_1(Set<Tipoambito> tipoambitos_1) {
		this.tipoambitos_1 = tipoambitos_1;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "literal")
	public Set<Defvector> getDefvectors_1() {
		return this.defvectors_1;
	}

	public void setDefvectors_1(Set<Defvector> defvectors_1) {
		this.defvectors_1 = defvectors_1;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "literal")
	public Set<Caracterdeterminacion> getCaracterdeterminacions_1() {
		return this.caracterdeterminacions_1;
	}

	public void setCaracterdeterminacions_1(
			Set<Caracterdeterminacion> caracterdeterminacions_1) {
		this.caracterdeterminacions_1 = caracterdeterminacions_1;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "literal")
	public Set<Instrumentoplan> getInstrumentoplans_1() {
		return this.instrumentoplans_1;
	}

	public void setInstrumentoplans_1(Set<Instrumentoplan> instrumentoplans_1) {
		this.instrumentoplans_1 = instrumentoplans_1;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "literal")
	public Set<Ambito> getAmbitos_1() {
		return this.ambitos_1;
	}

	public void setAmbitos_1(Set<Ambito> ambitos_1) {
		this.ambitos_1 = ambitos_1;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "literal")
	public Set<Grupodocumento> getGrupodocumentos_1() {
		return this.grupodocumentos_1;
	}

	public void setGrupodocumentos_1(Set<Grupodocumento> grupodocumentos_1) {
		this.grupodocumentos_1 = grupodocumentos_1;
	}

}

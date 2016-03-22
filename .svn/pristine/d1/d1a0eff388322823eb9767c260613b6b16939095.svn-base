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

package com.mitc.redes.editorfip.entidades.busqueda;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.model.SelectItem;

/**
 *
 * @author Sancho
 */
public class FiltrosDTO implements Serializable{
	
	private String nombre;
	private Date fechaAprobacion;
	private int idAmbito;
	private int tipoTramite;
	
	private int tipoPlan;
	private String clave;
	private String etiqueta;
	private String tipoFiltro;
	
	private String apartado;
	private String articulado;
	
	// Plan seleccionado (vigente, encargado, base)
	private String tipoPlanSeleccionado;
	
	private String tieneGeometria;
	
	private String entAplicada;
	private String detAplicada;
	private String detRegimen;
	private String detValorRef;
	
	private String valor;
	private String nombreRegEspecifico;
	private String textoRefEspecifico;
	
	private String titulo;
	private int grupoDoc;
	private int tipoDoc;
	private String nombreArchivo;
	private int asociadoEntidad;
	private int asociadoDeterminacion;
	
	private String claseOperacion;
	private Integer tipoOperacion;
	private String nombreOperada;
	private String nombreOperadora;
	
	/// Paginacion 
	private int pagina = 1;
	private int maxResultados = 200;
	private int numPaginas;
	private Long totalRegistros = 0L;
	private List<SelectItem> listaPaginas;
	
	/** Actualiza el numero de paginas y renderiza la lista. Util cuando
     * se elimina un registro.
     */
	public void actualizarPaginas() {
		
		Double paginasD = new Double(((float)totalRegistros) / ((float)maxResultados));
    	numPaginas = (int)Math.ceil(paginasD);
    	if(numPaginas == 0)
    		numPaginas = 1;
    	
    	if(pagina > numPaginas)
    		pagina = numPaginas;
    	
		listaPaginas = new ArrayList<SelectItem>();    	
    	for(int i=1; i <= numPaginas; i++) {
			SelectItem item = new SelectItem(i);
			listaPaginas.add(item);
		}
	}
	
	public int getPagina() {
		return pagina;
	}
	public void setPagina(int pagina) {
		this.pagina = pagina;
	}
	public int getMaxResultados() {
		return maxResultados;
	}
	public void setMaxResultados(int maxResultados) {
		this.maxResultados = maxResultados;
	}
	public int getNumPaginas() {
		return numPaginas;
	}
	public void setNumPaginas(int numPaginas) {
		this.numPaginas = numPaginas;
	}
	public Long getTotalRegistros() {
		return totalRegistros;
	}
	public void setTotalRegistros(Long totalRegistros) {
		this.totalRegistros = totalRegistros;
	}
	public List<SelectItem> getListaPaginas() {
		return listaPaginas;
	}
	public void setListaPaginas(List<SelectItem> listaPaginas) {
		this.listaPaginas = listaPaginas;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public Date getFechaAprobacion() {
		return fechaAprobacion;
	}
	public void setFechaAprobacion(Date fechaAprobacion) {
		this.fechaAprobacion = fechaAprobacion;
	}
	public int getIdAmbito() {
		return idAmbito;
	}
	public void setIdAmbito(int idAmbito) {
		this.idAmbito = idAmbito;
	}
	
	public int getTipoTramite() {
		return tipoTramite;
	}
	public void setTipoTramite(int tipoTramite) {
		this.tipoTramite = tipoTramite;
	}
	public int getTipoPlan() {
		return tipoPlan;
	}
	public void setTipoPlan(int tipoPlan) {
		this.tipoPlan = tipoPlan;
	}
	public String getTipoFiltro() {
		return tipoFiltro;
	}
	public void setTipoFiltro(String tipoFiltro) {
		this.tipoFiltro = tipoFiltro;
	}
	public String getClave() {
		return clave;
	}
	public void setClave(String clave) {
		this.clave = clave;
	}
	public String getEtiqueta() {
		return etiqueta;
	}
	public void setEtiqueta(String etiqueta) {
		this.etiqueta = etiqueta;
	}
	public String getApartado() {
		return apartado;
	}
	public void setApartado(String apartado) {
		this.apartado = apartado;
	}
	public String getArticulado() {
		return articulado;
	}
	public void setArticulado(String articulado) {
		this.articulado = articulado;
	}
	public String getTieneGeometria() {
		return tieneGeometria;
	}
	public void setTieneGeometria(String tieneGeometria) {
		this.tieneGeometria = tieneGeometria;
	}
	public String getEntAplicada() {
		return entAplicada;
	}
	public void setEntAplicada(String entAplicada) {
		this.entAplicada = entAplicada;
	}
	public String getDetAplicada() {
		return detAplicada;
	}
	public void setDetAplicada(String detAplicada) {
		this.detAplicada = detAplicada;
	}
	public String getDetRegimen() {
		return detRegimen;
	}
	public void setDetRegimen(String detRegimen) {
		this.detRegimen = detRegimen;
	}
	public String getDetValorRef() {
		return detValorRef;
	}
	public void setDetValorRef(String detValorRef) {
		this.detValorRef = detValorRef;
	}
	public String getValor() {
		return valor;
	}
	public void setValor(String valor) {
		this.valor = valor;
	}
	public String getNombreRegEspecifico() {
		return nombreRegEspecifico;
	}
	public void setNombreRegEspecifico(String nombreRegEspecifico) {
		this.nombreRegEspecifico = nombreRegEspecifico;
	}
	public String getTextoRefEspecifico() {
		return textoRefEspecifico;
	}
	public void setTextoRefEspecifico(String textoRefEspecifico) {
		this.textoRefEspecifico = textoRefEspecifico;
	}
	public String getTitulo() {
		return titulo;
	}
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	public int getGrupoDoc() {
		return grupoDoc;
	}
	public void setGrupoDoc(int grupoDoc) {
		this.grupoDoc = grupoDoc;
	}
	public int getTipoDoc() {
		return tipoDoc;
	}
	public void setTipoDoc(int tipoDoc) {
		this.tipoDoc = tipoDoc;
	}
	public String getNombreArchivo() {
		return nombreArchivo;
	}
	public void setNombreArchivo(String nombreArchivo) {
		this.nombreArchivo = nombreArchivo;
	}
	
	public int getAsociadoEntidad() {
		return asociadoEntidad;
	}

	public void setAsociadoEntidad(int asociadoEntidad) {
		this.asociadoEntidad = asociadoEntidad;
	}

	public int getAsociadoDeterminacion() {
		return asociadoDeterminacion;
	}

	public void setAsociadoDeterminacion(int asociadoDeterminacion) {
		this.asociadoDeterminacion = asociadoDeterminacion;
	}

	public String getClaseOperacion() {
		return claseOperacion;
	}
	public void setClaseOperacion(String claseOperacion) {
		this.claseOperacion = claseOperacion;
	}
	public Integer getTipoOperacion() {
		return tipoOperacion;
	}
	public void setTipoOperacion(Integer tipoOperacion) {
		this.tipoOperacion = tipoOperacion;
	}
	public String getNombreOperada() {
		return nombreOperada;
	}
	public void setNombreOperada(String nombreOperada) {
		this.nombreOperada = nombreOperada;
	}
	public String getNombreOperadora() {
		return nombreOperadora;
	}
	public void setNombreOperadora(String nombreOperadora) {
		this.nombreOperadora = nombreOperadora;
	}

	public String getTipoPlanSeleccionado() {
		return tipoPlanSeleccionado;
	}

	public void setTipoPlanSeleccionado(String tipoPlanSeleccionado) {
		this.tipoPlanSeleccionado = tipoPlanSeleccionado;
	}
	
}
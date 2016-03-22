/*
* Copyright 2011 red.es
* Autores: Arnaiz Consultores.
*
** Licencia con arreglo a la EUPL, VersiÃ³n 1.1 o -en cuanto
** sean aprobadas por la Comision Europea- versiones
** posteriores de la EUPL (la <<Licencia>>);
** Solo podra usarse esta obra si se respeta la Licencia.
* Puede obtenerse una copia de la Licencia en:
*
* http://ec.europa.eu/idabc/eupl5
*
** Salvo cuando lo exija la legislacion aplicable o se acuerde
* por escrito, el programa distribuido con arreglo a la
* Licencia se distribuye <<TAL CUAL>>,
** SIN GARANTIAS NI CONDICIONES DE NINGUN TIPO, ni expresas
** ni implicitas.
** Vease la Licencia en el idioma concreto que rige
* los permisos y limitaciones que establece la Licencia.
*/
package es.mitc.redes.urbanismoenred.data.rpm.planeamiento;
// Generated 27-jul-2009 13:12:50 by Hibernate Tools 3.2.1.GA

import es.mitc.redes.urbanismoenred.data.rpm.diccionario.Tipotramite;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Tramite generated by hbm2java
 */
public class Tramite implements java.io.Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = -7751067118893015736L;
	private int iden;
    private Plan plan;
    private int idtipotramite;
    private Tipotramite tipotramite;
    private Integer idorgano;
    private Integer idsentido;
    private int iteracion;
    private Date fecha;
    private Date fechaconsolidacion;
    private String texto;
    private String comentario;
    private String numeroacuerdo;
    private String nombre;
    private int idcentroproduccion;
    private String codigofip;
    private Set<Relacion> relacionsForIdtramitecreador = new HashSet<Relacion>(0);
    private Set<Determinacion> determinacions = new HashSet<Determinacion>(0);
    private Set<Boletintramite> boletintramites = new HashSet<Boletintramite>(0);
    private Set<Documento> documentos = new HashSet<Documento>(0);
    private Set<Operacion> operacions = new HashSet<Operacion>(0);
    private Set<Entidad> entidads = new HashSet<Entidad>(0);

    public Tramite() {
    }

    public Tramite(int iden, Plan plan, int idtipotramite, Integer idsentido, int iteracion, int idcentroproduccion) {
        this.iden = iden;
        this.plan = plan;
        this.idtipotramite = idtipotramite;
        this.idsentido = idsentido;
        this.iteracion = iteracion;
        this.idcentroproduccion = idcentroproduccion;
    }

    public Tramite(int iden, Plan plan, int idtipotramite, Integer idorgano, int idsentido, int iteracion, Date fecha, String texto, String comentario, String numeroacuerdo, String nombre, int idcentroproduccion, String codigofip, Set<Relacion> relacionsForIdtramitecreador, Set<Determinacion> determinacions, Set<Boletintramite> boletintramites, Set<Documento> documentos, Set<Operacion> operacions, Set<Entidad> entidads, Tipotramite tipotramite) {
        this.iden = iden;
        this.plan = plan;
        this.idtipotramite = idtipotramite;
        this.idorgano = idorgano;
        this.idsentido = idsentido;
        this.iteracion = iteracion;
        this.fecha = fecha;
        this.texto = texto;
        this.comentario = comentario;
        this.numeroacuerdo = numeroacuerdo;
        this.nombre = nombre;
        this.idcentroproduccion = idcentroproduccion;
        this.codigofip = codigofip;
        this.relacionsForIdtramitecreador = relacionsForIdtramitecreador;
        this.determinacions = determinacions;
        this.boletintramites = boletintramites;
        this.documentos = documentos;
        this.operacions = operacions;
        this.entidads = entidads;
        this.tipotramite = tipotramite;
    }

    public int getIden() {
        return this.iden;
    }

    public void setIden(int iden) {
        this.iden = iden;
    }

    public Plan getPlan() {
        return this.plan;
    }

    public void setPlan(Plan plan) {
        this.plan = plan;
    }

    public Tipotramite getTipotramite() {
        return this.tipotramite;
    }

    public void setTipotramite(Tipotramite tipotramite) {
        this.tipotramite = tipotramite;
    }

    public int getIdtipotramite() {
        return this.idtipotramite;
    }

    public void setIdtipotramite(int idtipotramite) {
        this.idtipotramite = idtipotramite;
    }

    public Integer getIdorgano() {
        return this.idorgano;
    }

    public void setIdorgano(Integer idorgano) {
        this.idorgano = idorgano;
    }

    public Integer getIdsentido() {
        return this.idsentido;
    }

    public void setIdsentido(Integer idsentido) {
        this.idsentido = idsentido;
    }

    public int getIteracion() {
        return this.iteracion;
    }

    public void setIteracion(int iteracion) {
        this.iteracion = iteracion;
    }

    public Date getFecha() {
        return this.fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Date getFechaconsolidacion() {
        return this.fechaconsolidacion;
    }

    public void setFechaconsolidacion(Date fechaconsolidacion) {
        this.fechaconsolidacion = fechaconsolidacion;
    }

    public String getTexto() {
        return this.texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public String getComentario() {
        return this.comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public String getNumeroacuerdo() {
        return this.numeroacuerdo;
    }

    public void setNumeroacuerdo(String numeroacuerdo) {
        this.numeroacuerdo = numeroacuerdo;
    }

    public String getNombre() {
        return this.nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getIdcentroproduccion() {
        return this.idcentroproduccion;
    }

    public void setIdcentroproduccion(int idcentroproduccion) {
        this.idcentroproduccion = idcentroproduccion;
    }

    public String getCodigofip() {
        return this.codigofip;
    }

    public void setCodigofip(String codigofip) {
        this.codigofip = codigofip;
    }

    public Set<Relacion> getRelacionsForIdtramitecreador() {
        return this.relacionsForIdtramitecreador;
    }

    public void setRelacionsForIdtramitecreador(Set<Relacion> relacionsForIdtramitecreador) {
        this.relacionsForIdtramitecreador = relacionsForIdtramitecreador;
    }

    public Set<Determinacion> getDeterminacions() {
        return this.determinacions;
    }

    public void setDeterminacions(Set<Determinacion> determinacions) {
        this.determinacions = determinacions;
    }

    public Set<Boletintramite> getBoletintramites() {
        return this.boletintramites;
    }

    public void setBoletintramites(Set<Boletintramite> boletintramites) {
        this.boletintramites = boletintramites;
    }

    public Set<Documento> getDocumentos() {
    	return this.documentos;
    }

    public void setDocumentos(Set<Documento> documentos) {
        this.documentos = documentos;
    }

    public Set<Operacion> getOperacions() {
        return this.operacions;
    }

    public void setOperacions(Set<Operacion> operacions) {
        this.operacions = operacions;
    }

    public Set<Entidad> getEntidads() {
        return this.entidads;
    }

    public void setEntidads(Set<Entidad> entidads) {
        this.entidads = entidads;
    }
}


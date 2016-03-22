/*
* Copyright 2011 red.es
* Autores: Arnaiz Consultores.
*
** Licencia con arreglo a la EUPL, Versión 1.1 o -en cuanto
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

import java.util.HashSet;
import java.util.Set;
/**
 * Documento generated by hbm2java
 */
public class Documento  implements java.io.Serializable {


     /**
	 * 
	 */
	private static final long serialVersionUID = 1513250309149342379L;
	private int iden;
     private Tramite tramite;
     private Documento documento;
     private String nombre;
     private String archivo;
     private String comentario;
     private Integer escala;
     private Integer idtipodocumento;
     private Integer idgrupodocumento;
     private Set<Documentoentidad> documentoentidads = new HashSet<Documentoentidad>(0);
     private Set<Documentocaso> documentocasos = new HashSet<Documentocaso>(0);
     private Set<Documentodeterminacion> documentodeterminacions = new HashSet<Documentodeterminacion>(0);
     private Set<Documento> documentos = new HashSet<Documento>(0);
     private Set<Documentoshp> documentoshps = new HashSet<Documentoshp>(0);

    public Documento() {
    }

	
    public Documento(int iden, Tramite tramite, String nombre, String archivo) {
        this.iden = iden;
        this.tramite = tramite;
        this.nombre = nombre;
        this.archivo = archivo;
    }
    public Documento(int iden, Tramite tramite, Documento documento, String nombre, String archivo, String comentario, Integer escala, Integer idtipodocumento, Integer idgrupodocumento, Set<Documentoentidad> documentoentidads, Set<Documentocaso> documentocasos, Set<Documentodeterminacion> documentodeterminacions, Set<Documento> documentos, Set<Documentoshp> documentoshps) {
       this.iden = iden;
       this.tramite = tramite;
       this.documento = documento;
       this.nombre = nombre;
       this.archivo = archivo;
       this.comentario = comentario;
       this.escala = escala;
       this.idtipodocumento = idtipodocumento;
       this.idgrupodocumento = idgrupodocumento;
       this.documentoentidads = documentoentidads;
       this.documentocasos = documentocasos;
       this.documentodeterminacions = documentodeterminacions;
       this.documentos = documentos;
       this.documentoshps = documentoshps;
    }

    public int getIden() {
        return this.iden;
    }
    
    public void setIden(int iden) {
        this.iden = iden;
    }
    public Tramite getTramite() {
        return this.tramite;
    }
    
    public void setTramite(Tramite tramite) {
        this.tramite = tramite;
    }
    public Documento getDocumento() {
        return this.documento;
    }
    
    public void setDocumento(Documento documento) {
        this.documento = documento;
    }
    public String getNombre() {
        return this.nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getArchivo() {
        return this.archivo;
    }
    
    public void setArchivo(String archivo) {
        this.archivo = archivo;
    }
    public String getComentario() {
        return this.comentario;
    }
    
    public void setComentario(String comentario) {
        this.comentario = comentario;
    }
    public Integer getEscala() {
        return this.escala;
    }
    
    public void setEscala(Integer escala) {
        this.escala = escala;
    }
    public Integer getIdtipodocumento() {
        return this.idtipodocumento;
    }
    
    public void setIdtipodocumento(Integer idtipodocumento) {
        this.idtipodocumento = idtipodocumento;
    }
    public Integer getIdgrupodocumento() {
        return this.idgrupodocumento;
    }
    
    public void setIdgrupodocumento(Integer idgrupodocumento) {
        this.idgrupodocumento = idgrupodocumento;
    }
    public Set<Documentoentidad> getDocumentoentidads() {
        return this.documentoentidads;
    }
    
    public void setDocumentoentidads(Set<Documentoentidad> documentoentidads) {
        this.documentoentidads = documentoentidads;
    }
    public Set<Documentocaso> getDocumentocasos() {
        return this.documentocasos;
    }
    
    public void setDocumentocasos(Set<Documentocaso> documentocasos) {
        this.documentocasos = documentocasos;
    }
    public Set<Documentodeterminacion> getDocumentodeterminacions() {
        return this.documentodeterminacions;
    }
    
    public void setDocumentodeterminacions(Set<Documentodeterminacion> documentodeterminacions) {
        this.documentodeterminacions = documentodeterminacions;
    }
    public Set<Documento> getDocumentos() {
        return this.documentos;
    }
    
    public void setDocumentos(Set<Documento> documentos) {
        this.documentos = documentos;
    }
    public Set<Documentoshp> getDocumentoshps() {
        return this.documentoshps;
    }
    
    public void setDocumentoshps(Set<Documentoshp> documentoshps) {
        this.documentoshps = documentoshps;
    }
}


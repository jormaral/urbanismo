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
package es.mitc.redes.urbanismoenred.data.rpm.seguridad;
// Generated 22-ene-2010 18:08:31 by Hibernate Tools 3.2.1.GA


import java.util.Date;

/**
 * Fip1 generated by hbm2java
 */
public class Fip1  implements java.io.Serializable {


     /**
	 * 
	 */
	private static final long serialVersionUID = 729621962985797945L;
	private int iden;
     private String codfip;
     private Date fechacreacion;
     private Date fecharefundido;
     private String ruta;
     private Boolean obsoleto;
     private Date fechadescarga;
     private int idambito;

    public Fip1() {
    }

	
    public Fip1(int iden) {
        this.iden = iden;
    }
    public Fip1(int iden, String codfip, Date fechacreacion, Date fecharefundido, String ruta, Boolean obsoleto, Date fechadescarga,int idambito) {
       this.iden = iden;
       this.codfip = codfip;
       this.fechacreacion = fechacreacion;
       this.fecharefundido = fecharefundido;
       this.ruta = ruta;
       this.obsoleto = obsoleto;
       this.fechadescarga = fechadescarga;
       this.idambito=idambito;
    }
   
    public int getIden() {
        return this.iden;
    }
    
    public void setIden(int iden) {
        this.iden = iden;
    }
    public String getCodfip() {
        return this.codfip;
    }
    
    public void setCodfip(String codfip) {
        this.codfip = codfip;
    }
    public Date getFechacreacion() {
        return this.fechacreacion;
    }
    
    public void setFechacreacion(Date fechacreacion) {
        this.fechacreacion = fechacreacion;
    }
    public Date getFecharefundido() {
        return this.fecharefundido;
    }
    
    public void setFecharefundido(Date fecharefundido) {
        this.fecharefundido = fecharefundido;
    }
    public String getRuta() {
        return this.ruta;
    }
    
    public void setRuta(String ruta) {
        this.ruta = ruta;
    }
    public Boolean getObsoleto() {
        return this.obsoleto;
    }
    
    public void setObsoleto(Boolean obsoleto) {
        this.obsoleto = obsoleto;
    }
    public Date getFechadescarga() {
        return this.fechadescarga;
    }
    
    public void setFechadescarga(Date fechadescarga) {
        this.fechadescarga = fechadescarga;
    }

     public int getIdambito() {
        return this.idambito;
    }

    public void setIdambito(int idambito) {
        this.idambito = idambito;
    }

}


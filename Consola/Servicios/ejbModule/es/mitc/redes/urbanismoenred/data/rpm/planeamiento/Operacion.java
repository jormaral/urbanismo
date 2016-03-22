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
 * Operacion generated by hbm2java
 */
public class Operacion  implements java.io.Serializable {


     /**
	 * 
	 */
	private static final long serialVersionUID = 5624245697281712368L;
	private int iden;
     private Tramite tramite;
     private String texto;
     private int orden;
     private Set<Operacionentidad> operacionentidads = new HashSet<Operacionentidad>(0);
     private Set<Operaciondeterminacion> operaciondeterminacions = new HashSet<Operaciondeterminacion>(0);
     private Set<Operacionrelacion> operacionrelacions = new HashSet<Operacionrelacion>(0);

    public Operacion() {
    }

	
    public Operacion(int iden, Tramite tramite, int orden) {
        this.iden = iden;
        this.tramite = tramite;
        this.orden = orden;
    }
    public Operacion(int iden, Tramite tramite, String texto, int orden, Set<Operacionentidad> operacionentidads, Set<Operaciondeterminacion> operaciondeterminacions, Set<Operacionrelacion> operacionrelacions) {
       this.iden = iden;
       this.tramite = tramite;
       this.texto = texto;
       this.orden = orden;
       this.operacionentidads = operacionentidads;
       this.operaciondeterminacions = operaciondeterminacions;
       this.operacionrelacions = operacionrelacions;
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
    public String getTexto() {
        return this.texto;
    }
    
    public void setTexto(String texto) {
        this.texto = texto;
    }
    public int getOrden() {
        return this.orden;
    }
    
    public void setOrden(int orden) {
        this.orden = orden;
    }
    public Set<Operacionentidad> getOperacionentidads() {
        return this.operacionentidads;
    }
    
    public void setOperacionentidads(Set<Operacionentidad> operacionentidads) {
        this.operacionentidads = operacionentidads;
    }
    public Set<Operaciondeterminacion> getOperaciondeterminacions() {
        return this.operaciondeterminacions;
    }
    
    public void setOperaciondeterminacions(Set<Operaciondeterminacion> operaciondeterminacions) {
        this.operaciondeterminacions = operaciondeterminacions;
    }
    public Set<Operacionrelacion> getOperacionrelacions() {
        return this.operacionrelacions;
    }
    
    public void setOperacionrelacions(Set<Operacionrelacion> operacionrelacions) {
        this.operacionrelacions = operacionrelacions;
    }




}


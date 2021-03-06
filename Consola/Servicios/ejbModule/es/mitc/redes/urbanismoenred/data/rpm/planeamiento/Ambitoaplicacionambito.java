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



/**
 * Ambitoaplicacionambito generated by hbm2java
 */
public class Ambitoaplicacionambito  implements java.io.Serializable {


     /**
	 * 
	 */
	private static final long serialVersionUID = 525868630998101764L;
	private int iden;
     private Entidad entidad;
     private int idambito;

    public Ambitoaplicacionambito() {
    }

    public Ambitoaplicacionambito(int iden, Entidad entidad, int idambito) {
       this.iden = iden;
       this.entidad = entidad;
       this.idambito = idambito;
    }
   
    public int getIden() {
        return this.iden;
    }
    
    public void setIden(int iden) {
        this.iden = iden;
    }
    public Entidad getEntidad() {
        return this.entidad;
    }
    
    public void setEntidad(Entidad entidad) {
        this.entidad = entidad;
    }
    public int getIdambito() {
        return this.idambito;
    }
    
    public void setIdambito(int idambito) {
        this.idambito = idambito;
    }




}



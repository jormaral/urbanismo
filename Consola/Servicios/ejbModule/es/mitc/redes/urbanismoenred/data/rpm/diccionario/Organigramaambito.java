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
package es.mitc.redes.urbanismoenred.data.rpm.diccionario;
// Generated 08-jul-2009 12:36:15 by Hibernate Tools 3.2.1.GA





/**
 * Organigramaambito generated by hbm2java
 */
public class Organigramaambito  implements java.io.Serializable {


     /**
	 * 
	 */
	private static final long serialVersionUID = 8135489042629704078L;
	private int iden;
     private Ambito ambitoByIdambitohijo;
     private Ambito ambitoByIdambitopadre;

    public Organigramaambito() {
    }

    public Organigramaambito(int iden, Ambito ambitoByIdambitohijo, Ambito ambitoByIdambitopadre) {
       this.iden = iden;
       this.ambitoByIdambitohijo = ambitoByIdambitohijo;
       this.ambitoByIdambitopadre = ambitoByIdambitopadre;
    }
   
    public int getIden() {
        return this.iden;
    }
    
    public void setIden(int iden) {
        this.iden = iden;
    }
    public Ambito getAmbitoByIdambitohijo() {
        return this.ambitoByIdambitohijo;
    }
    
    public void setAmbitoByIdambitohijo(Ambito ambitoByIdambitohijo) {
        this.ambitoByIdambitohijo = ambitoByIdambitohijo;
    }
    public Ambito getAmbitoByIdambitopadre() {
        return this.ambitoByIdambitopadre;
    }
    
    public void setAmbitoByIdambitopadre(Ambito ambitoByIdambitopadre) {
        this.ambitoByIdambitopadre = ambitoByIdambitopadre;
    }




}


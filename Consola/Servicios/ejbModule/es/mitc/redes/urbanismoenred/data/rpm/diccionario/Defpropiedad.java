/*
* Copyright 2011 red.es
* Autores: Arnaiz Consultores.
*
** Licencia con arreglo a la EUPL, Versión 1.1 o -en cuanto
* sean aprobadas por la Comision Europea- versiones
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
 * Defpropiedad generated by hbm2java
 */
public class Defpropiedad  implements java.io.Serializable {


     /**
	 * 
	 */
	private static final long serialVersionUID = 4916945987428919257L;
	private int iden;
     private Tipodefpropiedad tipodefpropiedad;
     private Literal literal;
     private Defrelacion defrelacion;

    public Defpropiedad() {
    }

    public Defpropiedad(int iden, Tipodefpropiedad tipodefpropiedad, Literal literal, Defrelacion defrelacion) {
       this.iden = iden;
       this.tipodefpropiedad = tipodefpropiedad;
       this.literal = literal;
       this.defrelacion = defrelacion;
    }
   
    public int getIden() {
        return this.iden;
    }
    
    public void setIden(int iden) {
        this.iden = iden;
    }
    public Tipodefpropiedad getTipodefpropiedad() {
        return this.tipodefpropiedad;
    }
    
    public void setTipodefpropiedad(Tipodefpropiedad tipodefpropiedad) {
        this.tipodefpropiedad = tipodefpropiedad;
    }
    public Literal getLiteral() {
        return this.literal;
    }
    
    public void setLiteral(Literal literal) {
        this.literal = literal;
    }
    public Defrelacion getDefrelacion() {
        return this.defrelacion;
    }
    
    public void setDefrelacion(Defrelacion defrelacion) {
        this.defrelacion = defrelacion;
    }




}


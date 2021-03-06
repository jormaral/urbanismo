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
 * Defvector generated by hbm2java
 */
public class Defvector  implements java.io.Serializable {


     /**
	 * 
	 */
	private static final long serialVersionUID = 1490314504994598881L;
	private int iden;
     private Defrelacion defrelacion;
     private Tabla tabla;
     private Literal literal;
     private boolean basignacion;

     

    public Defvector() {
    }

    public Defvector(int iden, Literal literal, Tabla tabla, Defrelacion defrelacion, boolean basignacion) {
       this.iden = iden;
       this.literal = literal;
       this.tabla = tabla;
       this.defrelacion = defrelacion;
       this.basignacion = basignacion;
    }
   
    public int getIden() {
        return this.iden;
    }
    
    public void setIden(int iden) {
        this.iden = iden;
    }
    public Literal getLiteral() {
        return this.literal;
    }
    
    public void setLiteral(Literal literal) {
        this.literal = literal;
    }
    public Tabla getTabla() {
        return this.tabla;
    }
    
    public void setTabla(Tabla tabla) {
        this.tabla = tabla;
    }
    public Defrelacion getDefrelacion() {
        return this.defrelacion;
    }
    
    public void setDefrelacion(Defrelacion defrelacion) {
        this.defrelacion = defrelacion;
    }

    public boolean getBasignacion() {
        return basignacion;
    }

    public void setBasignacion(boolean basignacion) {
        this.basignacion = basignacion;
    }



}



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
package es.mitc.redes.urbanismoenred.data.rpm.explotacion;


public class Peticion  implements java.io.Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 6948415325643159462L;
	private int iden;
    private byte[] geom;
    private String codigo;

    public Peticion() {
    }

    public Peticion(int iden, byte[] geom,String codigo) {
        this.iden = iden;
        this.geom = geom;
        this.codigo = codigo;
    }
   
    public int getIden() {
        return this.iden;
    }
    
    public void setIden(int iden) {
        this.iden = iden;
    }

    public byte[] getGeom() {
        return this.geom;
    }

    public void setGeom(byte[] geom) {
        this.geom = geom;
    }

    public String getCodigo() {
        return this.codigo;
    }

    public void setCodigo( String codigo) {
        this.codigo = codigo;
    }
}


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
 * Instrumentotipooperacionplan generated by hbm2java
 */
public class Instrumentotipooperacionplan implements java.io.Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = -8682321693027686277L;
	private int iden;
    private Tipooperacionplan tipooperacionplan;
    private Instrumentoplan instrumentoplan;

    public Instrumentotipooperacionplan() {
    }

    public Instrumentotipooperacionplan(int iden, Tipooperacionplan tipooperacionplan, Instrumentoplan instrumentoplan) {
        this.iden = iden;
        this.tipooperacionplan = tipooperacionplan;
        this.instrumentoplan = instrumentoplan;
    }

    public int getIden() {
        return this.iden;
    }

    public void setIden(int iden) {
        this.iden = iden;
    }

    public Tipooperacionplan getTipooperacionplan() {
        return this.tipooperacionplan;
    }

    public void setTipooperacionplan(Tipooperacionplan tipooperacionplan) {
        this.tipooperacionplan = tipooperacionplan;

    }

    public Instrumentoplan getInstrumentoplan() {
        return this.instrumentoplan;
    }

    public void setInstrumentoplan(Instrumentoplan instrumentoplan) {
        this.instrumentoplan = instrumentoplan;
    }

}



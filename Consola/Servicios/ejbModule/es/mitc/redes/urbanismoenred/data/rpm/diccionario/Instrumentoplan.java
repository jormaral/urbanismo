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

import java.util.HashSet;
import java.util.Set;

/**
 * Instrumentoplan generated by hbm2java
 */
public class Instrumentoplan implements java.io.Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = -8591287424721825886L;
	private int iden;
    private Naturaleza naturaleza;
    private Literal literal;
    private String nemo;
    private Set<Instrumentotipooperacionplan> instrumentotipooperacionplans = new HashSet<Instrumentotipooperacionplan>(0);

    public Instrumentoplan() {
    }

    public Instrumentoplan(int iden, String nemo) {
        this.iden = iden;
        this.nemo = nemo;
    }

    public Instrumentoplan(int iden, Naturaleza naturaleza, Literal literal, String nemo, Set<Instrumentotipooperacionplan> instrumentotipooperacionplans) {
        this.iden = iden;
        this.naturaleza = naturaleza;
        this.literal = literal;
        this.nemo = nemo;
        this.instrumentotipooperacionplans = instrumentotipooperacionplans;
    }

    public int getIden() {
        return this.iden;
    }

    public void setIden(int iden) {
        this.iden = iden;
    }

    public Naturaleza getNaturaleza() {
        return this.naturaleza;
    }

    public void setNaturaleza(Naturaleza naturaleza) {
        this.naturaleza = naturaleza;
    }

    public Literal getLiteral() {
        return this.literal;
    }

    public void setLiteral(Literal literal) {
        this.literal = literal;
    }

    public String getNemo() {
        return this.nemo;
    }

    public void setNemo(String nemo) {
        this.nemo = nemo;
    }

    public Set<Instrumentotipooperacionplan> getInstrumentotipooperacionplans() {
        return this.instrumentotipooperacionplans;
    }

    public void setInstrumentotipooperacionplans(Set<Instrumentotipooperacionplan> instrumentotipooperacionplans) {
        this.instrumentotipooperacionplans = instrumentotipooperacionplans;
    }
}


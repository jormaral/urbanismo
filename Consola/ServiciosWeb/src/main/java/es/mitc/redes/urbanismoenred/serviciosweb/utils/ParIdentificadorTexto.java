/*
* Copyright 2011 red.es
* Autores: Arnaiz Consultores.
*
** Licencia con arreglo a la EUPL, Versi�n 1.1 o -en cuanto
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

package es.mitc.redes.urbanismoenred.serviciosweb.utils;

/**
 *
 * @author Arnaiz Consultores
 */
public class ParIdentificadorTexto {

    private long idBaseDatos;
    private String texto;
    private String tipo;

    public ParIdentificadorTexto() {
    }

    public ParIdentificadorTexto(long idBaseDatos, String texto, String tipo) {
        this.idBaseDatos = idBaseDatos;
        this.texto = texto;
        this.tipo=tipo;
    }



    public long getIdBaseDatos() {
        return idBaseDatos;
    }

    public void setIdBaseDatos(long idBaseDatos) {
        this.idBaseDatos = idBaseDatos;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }


}

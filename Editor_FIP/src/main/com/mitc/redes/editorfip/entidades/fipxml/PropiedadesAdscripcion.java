/*
* Copyright 2011 red.es
* Autores: IDOM Consulting
*
* Licencia con arreglo a la EUPL, Versi�n 1.1 o -en cuanto
* sean aprobadas por la Comision Europea- versiones
* posteriores de la EUPL (la <<Licencia>>);
* Solo podra usarse esta obra si se respeta la Licencia.
* Puede obtenerse una copia de la Licencia en:
*
* http://ec.europa.eu/idabc/eupl5
*
* Salvo cuando lo exija la legislacion aplicable o se acuerde.
* por escrito, el programa distribuido con arreglo a la
* Licencia se distribuye <<TAL CUAL>>,
* SIN GARANTIAS NI CONDICIONES DE NINGUN TIPO, ni expresas
* ni implicitas.
* Vease la Licencia en el idioma concreto que rige
* los permisos y limitaciones que establece la Licencia.
*/ 

package com.mitc.redes.editorfip.entidades.fipxml;

import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Determinacion;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Entidad;







public class PropiedadesAdscripcion  implements java.io.Serializable {

    private int iden;
    private double cuantia;
    private String texto;
    private Determinacion unidad;
    private Determinacion tipo;
    private Entidad origen;
    private Entidad destino;

    

    public PropiedadesAdscripcion() {
    }

	
    public PropiedadesAdscripcion(int iden) {
        this.iden = iden;
    }



    public PropiedadesAdscripcion(int iden,Entidad origen,Entidad destino,  double cuantia, Determinacion unidad, Determinacion tipo, String texto) {
       this.iden = iden;
       this.cuantia = cuantia;
       this.unidad = tipo;
       this.tipo = tipo;
       this.texto=texto;
       this.origen = origen;
       this.destino=destino;
    }

   

    public int getIden() {
        return this.iden;
    }
    
    public void setIden(int iden) {
        this.iden = iden;
    }
    
    public String getTexto() {
        return this.texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public double getCuantia() {
        return this.cuantia;
    }
    
    public void setCuantia(double cuantia) {
        this.cuantia = cuantia;
    }
    public Determinacion getUnidad() {
        return this.unidad;
    }
    
    public void setUnidad(Determinacion unidad) {
        this.unidad = unidad;
    }
    

    public Determinacion getTipo() {
        return this.tipo;
    }
    
    public void setTipo(Determinacion tipo) {
        this.tipo = tipo;
    }


    public Entidad getOrigen() {
        return this.origen;
    }

    public void setOrigen(Entidad origen) {
        this.origen = origen;
    }

    public Entidad getDestino() {
        return this.destino;
    }

    public void setDestino(Entidad destino) {
        this.destino = destino;
    }

}



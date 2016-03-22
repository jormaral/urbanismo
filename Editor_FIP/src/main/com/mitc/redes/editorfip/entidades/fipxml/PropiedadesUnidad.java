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

public class PropiedadesUnidad  implements java.io.Serializable {

    private int iden;
    private String abreviatura;
    private String definicion;
    private Determinacion determinacion;
   

   
    

    public PropiedadesUnidad() {
    }

	
    public PropiedadesUnidad(int iden) {
        this.iden = iden;
    }



    public PropiedadesUnidad(int iden,  String abreviatura, Determinacion determinacion, String definicion) {
       this.iden = iden;
       this.abreviatura = abreviatura;
       this.determinacion = determinacion;
       this.definicion = definicion;
    }

    

    public int getIden() {
        return this.iden;
    }
    
    public void setIden(int iden) {
        this.iden = iden;
    }
    
    public String getDefinicion() {
        return this.definicion;
    }

    public void setDefinicion(String definicion) {
        this.definicion = definicion;
    }

    public String getAbreviatura() {
        return this.abreviatura;
    }

    public void setAbreviatura(String abreviatura) {
        this.abreviatura = abreviatura;
    }


    public Determinacion getDeterminacion() {
        return this.determinacion;
    }
    
    public void setDeterminacion(Determinacion determinacion) {
        this.determinacion = determinacion;
    }
    




}



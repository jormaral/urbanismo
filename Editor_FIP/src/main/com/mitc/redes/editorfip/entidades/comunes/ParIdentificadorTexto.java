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

package com.mitc.redes.editorfip.entidades.comunes;

import java.io.Serializable;

/**
 * Clase basica que almacena el id de base de datos y el nombre que queremos que se muestre en la vista
 * Adicionalmente tambien puede almacenar campos como tipo de dato y si es una hoja o no
 * @author Fran
 */
public class ParIdentificadorTexto implements Serializable{

    private Integer idBaseDatos;
    private String texto="";
    private String tipo="";
    private boolean hoja = false;

    public ParIdentificadorTexto() {
    }

    
    public ParIdentificadorTexto(Integer idBaseDatos, String texto) {
		super();
		this.idBaseDatos = idBaseDatos;
		this.texto = texto;
		
	}
    

    public ParIdentificadorTexto(Integer idBaseDatos, String texto, String tipo) {
		super();
		this.idBaseDatos = idBaseDatos;
		this.texto = texto;
		this.tipo = tipo;
	}

	public Integer getIdBaseDatos() {
        return idBaseDatos;
    }

    public void setIdBaseDatos(Integer idBaseDatos) {
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
	
	public String toString() {
		return texto;
	}


	public boolean isHoja() {
		return hoja;
	}


	public void setHoja(boolean hoja) {
		this.hoja = hoja;
	}
	
	

}

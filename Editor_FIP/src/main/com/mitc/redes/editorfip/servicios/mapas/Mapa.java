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

package com.mitc.redes.editorfip.servicios.mapas;

import java.util.List;

import javax.ejb.Local;

@Local

public interface Mapa {
	
/**
 * Obtiene el valor de la propiedad ''
 * 
 * @return valor de tipo int
 */
public int getI();	
public double xmaxFromEntity(int id);
public double xminFromEntity(int id);
public double ymaxFromEntity(int id);
public double yminFromEntity(int id);
public String projection(int id);
public String existGeom(int id);
public String projectionTramite(int id, String layer);
public String existGeomTramite(int id, String layer);  
public List<Double> getBoundsFromAmbito(int id);
public int obtenerAmbitoDeTramite(int i);

}

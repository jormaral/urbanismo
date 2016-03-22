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

package com.mitc.redes.editorfip.servicios.informacionfip.consultafichaurbanisticas;

import javax.ejb.Local;

import com.icesoft.faces.context.Resource;

@Local
public interface ServicioGestionFichaUrbanistica {

	public void generarFichaUrbanistica(String X, String Y);
	public void generarFichaUrbanistica(String X, String Y, String SRS);
	public void generarFichaUrbanistica();
	
	/**
	 * Obtiene el valor de la propiedad ''
	 * 
	 * @return valor de tipo String
	 */
	public String getX();
	/**
	 * Modifica el valor de la propiedad '@param x'
	 * 
	 * @param x valor de tipo 
	 */
	public void setX(String x);

	/**
	 * Obtiene el valor de la propiedad ''
	 * 
	 * @return valor de tipo String
	 */
	public String getY();
	/**
	 * Modifica el valor de la propiedad '@param y'
	 * 
	 * @param y valor de tipo 
	 */
	public void setY(String y);
	
	/**
	 * Obtiene el valor de la propiedad ''
	 * 
	 * @return valor de tipo String
	 */
	public String getSRS();
	/**
	 * Modifica el valor de la propiedad '@param sRS'
	 * 
	 * @param sRS valor de tipo 
	 */
	public void setSRS(String sRS);
	
	/**
	 * Obtiene el valor de la propiedad ''
	 * 
	 * @return valor de tipo Resource
	 */
	public Resource getPdfFichaGenerada();
	/**
	 * Modifica el valor de la propiedad '@param pdfFichaGenerada'
	 * 
	 * @param pdfFichaGenerada valor de tipo 
	 */
	public void setPdfFichaGenerada(Resource pdfFichaGenerada);

	/**
	 * Obtiene el valor de la propiedad ''
	 * 
	 * @return valor de tipo boolean
	 */
	public boolean isFichaGenerada();
	/**
	 * Modifica el valor de la propiedad '@param fichaGenerada'
	 * 
	 * @param fichaGenerada valor de tipo 
	 */
	public void setFichaGenerada(boolean fichaGenerada);
	
	public String textoFichaPdf();	

}

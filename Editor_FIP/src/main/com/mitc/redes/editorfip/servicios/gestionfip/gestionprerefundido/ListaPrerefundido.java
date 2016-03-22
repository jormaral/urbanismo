/*
* Copyright 2011 red.es
* Autores: IDOM Consulting
*
* Licencia con arreglo a la EUPL, Versiï¿½n 1.1 o -en cuanto
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

package com.mitc.redes.editorfip.servicios.gestionfip.gestionprerefundido;

import java.util.List;

import javax.ejb.Local;

import com.icesoft.faces.context.Resource;
import com.mitc.redes.editorfip.entidades.rpm.gestionfip.Prerefundido;
import com.mitc.redes.editorfip.servicios.genericos.SortableListIF;

/**
 * Servicio que gestiona las operaciones referentes a la lista
 * de pre-refundidos.
 * 
 * @author fguerrero
 *
 */
@Local
public interface ListaPrerefundido extends SortableListIF
{
    
	/**
	 * Obtiene el valor de la propiedad 'defaultAscending' en base 
	 * al nombre de una columna.
	 * 
	 * @return valor de tipo boolean
	 */
	public boolean isDefaultAscending(String sortColumn);
	
	/**
	 * Método que ordena la lista.
	 * 
	 */
	public void sort();
	
	/**
	 * Obtiene la lista completa de pre-refundidos.
	 * 
	 * @return
	 */
	public List<Prerefundido> obtenerListaPrerefundido();
	
	/**
	 * Recarga de nuevo la lista de pre-refundidos.
	 * 
	 */
	public void refrescarLista();
	
	
	/**
	 * Obtiene el valor de la propiedad 'nombreColumnAmbito'
	 * 
	 * @return valor de tipo String
	 */
	public String getNombreColumnAmbito();
	
	/**
	 * Obtiene el fichero fisico generado por el pre-refundido.
	 * 
	 * @param nombre Nombre del fichero.
	 * @return objeto de tipo Resoruce.
	 */
	public Resource obtenerFichero(String nombre);
	
	public boolean refundidoCorrecto (int idproceso);
	public String rutaFip (int idproceso);
	public String rutaLog (int idproceso);
	
	
	
	
	

}

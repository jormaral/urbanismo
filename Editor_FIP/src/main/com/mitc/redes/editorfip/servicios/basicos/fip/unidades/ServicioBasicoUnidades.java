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

package com.mitc.redes.editorfip.servicios.basicos.fip.unidades;

import java.util.List;

import javax.ejb.Local;

import com.mitc.redes.editorfip.entidades.interfazgrafico.UnidadDTO;

/**
 * Servicio que gestiona las unidades
 * 
 * @author fguerrero
 *
 */

@Local
public interface ServicioBasicoUnidades
{
	
	
	/**
	 * Metodo que crea una unidad a partir de una determinacion de caracter unidad
	 * @param idDeterminacion identificador de la determinacion de caracter unidad
	 * @param abreviatura 
	 * @param definicion
	 * @return cero en caso de error o distinto de cero si es correcto
	 */
	int crearUnidad(int idDeterminacion, String abreviatura, String definicion);
	
	
	/**
	 * Metodo que permite obtener los valores propios de unidad de una determinacion de caracter unidad
	 * @param idDeterminacion identificador de la determinacion de caracter unidad que se quieren obtener los valores propios de unidad
	 * @return Objeto unidad relleno o un objeto vacio en caso de que la determinacion no tenga unidad propia o no sea de caracter unidad
	 */
	UnidadDTO obtenerUnidadDeDeterminacion (int idDeterminacion);
	
	/**
	 * Metodo que devuelve la lista de todas las unidades definidas en un tramite
	 * @param idTramite identificador del tramite
	 * @return lista de unidades
	 */
	List<UnidadDTO> obtenerListaUnidadesTramite (int idTramite);
	
	/**
	 * Metodo que borra los detalles de unidad propia de una determinacion de caracter unidad
	 * @param idDeterminacion identificador de la determinacion de caracter unidad
	 * @return cero en caso de error o distinto de cero si es correcto
	 */
	int borrarUnidadDeDeterminacion (int idDeterminacion);
}

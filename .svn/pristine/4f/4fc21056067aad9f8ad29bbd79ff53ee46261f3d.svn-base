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
package es.mitc.redes.urbanismoenred.servicios.dal;
import javax.ejb.Local;

import es.mitc.redes.urbanismoenred.data.fip.FIP;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Tramite;
import es.mitc.redes.urbanismoenred.utils.excepciones.RedesException;

/**
 * 
 * @author Arnaiz Consultores
 *
 */
@Local
public interface GeneradorFipLocal {
	
	/**
	 * Genera el FIP1 plantilla con los datos del ámbito especificado como 
	 * parámetro. El FIP plantilla contiene los siguientes datos:
	 * 
	 * 		Diccionario
	 * 		Plan base
	 * 		Plan refundido (si lo hubiera)
	 * 
	 * @param idAmbito Identificador del ámbito.
	 * @param idioma Idioma en el que se generarán aquellos textos que puedan
	 * ser susceptibles de traducción.
	 * 
	 * @return Objeto FIP que representa en memoria el XML.
	 * @throws RedesException
	 */
	FIP generarPlantilla(int idAmbito, String idioma) throws RedesException;
	
	/**
	 * 
	 * @param tramite
	 * @return
	 * @throws RedesException
	 */
	FIP generarFIP2(Tramite tramite) throws RedesException;
}

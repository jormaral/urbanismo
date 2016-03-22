/*
* Copyright 2011 red.es
* Autores: Arnaiz Consultores.
*
** Licencia con arreglo a la EUPL, Versión 1.1 o -en cuanto
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
package es.mitc.redes.urbanismoenred.servicios.dal;
import javax.ejb.Local;

import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Tramite;

/**
 * Interfaz que define la funcionalidad ofrecida por los gestores de trámites
 * almacenados en base de datos.
 *  
 * @author jgarzon
 *
 */
@Local
public interface GestorTramitesLocal {
	/**
	 * Devuelve el trámite a partir del código FIP asociado a su plan.
	 *  
	 * @param codigoFip Código FIP del trámite.
	 * @return El trámite cuyo código de plan coincida con el pasado como
	 * parámetro. Si no se encuentra ningún trámite con ese valor, o si se 
	 * encuentran varios trámites con el mismo código devuelve el valor nulo.
	 */
	Tramite getTramite(String codigoFip);
}

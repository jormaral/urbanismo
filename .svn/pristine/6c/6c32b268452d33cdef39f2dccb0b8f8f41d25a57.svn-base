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
package es.mitc.redes.urbanismoenred.servicios.refundido;
import java.util.List;

import javax.ejb.Local;

import es.mitc.redes.urbanismoenred.data.rpm.refundido.Entidad;

/**
 * 
 * @author Arnaiz Consultores
 *
 */
@Local
public interface GestorGeometriasLocal {
	
	/**
	 * Calcula si una entidad intersecta con alguna otra de un trámite concreto.
	 * 
	 * @param origen Entidad de la que se quiere calcular la intersección.
	 * @param entidades Lista de entidades con las que calcular la intersección.
	 * 
	 * @return Lista de entidades que intersectan con la origen. Si
	 * no hay ninguna devuelve una lista vacía.
	 */
	Entidad[] calcularInterseccion(Entidad origen, List<Integer> entidades);
}

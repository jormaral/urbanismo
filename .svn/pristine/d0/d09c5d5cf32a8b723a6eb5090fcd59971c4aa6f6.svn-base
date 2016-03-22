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
package es.mitc.redes.urbanismoenred.servicios.planeamiento;
import java.util.Date;

import javax.ejb.Local;

import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Determinacion;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidad;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Plan;

/**
 * Interfaz del servicio de búsqueda de objetos del esquema de planeamiento.
 * 
 * @author Arnaiz Consultores
 *
 */
@Local
public interface ServicioBusquedaLocal {
	/**
	 * Busca aquellas determinaciones que cumplan las restricciones indicadas.
	 * 
	 * @param raiz Identificador del nodo raíz desde el que se realiza la 
	 * búsqueda.
	 * @param tipoRaiz Clase a la que pertenece el nodo raíz.
	 * @param nombre Nombre de la determinación.
	 * @param codigo Código de la determinación.
	 * @param apartado Apartado de la determinación
	 * @param modalidad Permite definir el ámbito (no geográfico sino 
	 * validación, consolidados, refundido...) sobre el que se realiza la 
	 * búsqueda.
	 * @return Determinaciones que cumplen los parámetros. Si no se encuentra
	 * ninguna devolverá una lista vacía.
	 */
	Determinacion[] buscarDeterminaciones(Integer raiz, 
			Class<?> tipoRaiz, 
			String nombre, 
			String codigo, 
			String apartado, 
			ModalidadPlanes modalidad);
	/**
	 * Busca aquellas entidades que cumplan las restricciones indicadas.
	 * 
	 * @param raiz Identificador del nodo raíz desde el que se realiza la 
	 * búsqueda.
	 * @param tipoRaiz Clase a la que pertenece el nodo raíz.
	 * @param nombre Nombre de la entidad.
	 * @param codigo Código de la entidad.
	 * @param apartado Apartado de la entidad.
	 * @param modalidad Permite definir el ámbito (no geográfico sino 
	 * validación, consolidados, refundido...) sobre el que se realiza la 
	 * búsqueda.
	 * @return Entidades que cumplen los parámetros. Si no se encuentra
	 * ninguna devolverá una lista vacía.
	 */
	Entidad[] buscarEntidades(Integer raiz, Class<?> tipoRaiz, String nombre, String codigo, String apartado, ModalidadPlanes modalidad);
	/**
	 * Busca aquellos planes que cumplan las restricciones indicadas.
	 * 
	 * @param raiz Identificador del nodo raíz desde el que se realiza la 
	 * búsqueda.
	 * @param tipoRaiz Clase a la que pertenece el nodo raíz.
	 * @param nombrePlan Nombre del plan.
	 * @param codigoPlan Código del plan.
	 * @param desde Permite buscar por un rango de fechas dentro del campo fecha
	 * de un plan. 
	 * @param hasta Permite buscar por un rango de fechas dentro del campo fecha
	 * de un plan.
	 * @param modalidad Permite definir el ámbito (no geográfico sino 
	 * validación, consolidados, refundido...) sobre el que se realiza la 
	 * búsqueda.
	 * @return Planes que cumplen los parámetros. Si no se encuentra
	 * ninguno devolverá una lista vacía.
	 */
	Plan[] buscarPlanes(Integer raiz, Class<?> tipoRaiz, String nombrePlan, String codigoPlan, Date desde, Date hasta, ModalidadPlanes modalidad);
}

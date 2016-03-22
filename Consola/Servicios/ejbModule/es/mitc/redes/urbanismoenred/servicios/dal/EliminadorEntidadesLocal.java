/*
* Copyright 2011 red.es
* Autores: Arnaiz Consultores.
*
* Licencia con arreglo a la EUPL, Versión 1.1 o -en cuanto
** sean aprobadas por la Comision Europea- versiones
** posteriores de la EUPL (la <<Licencia>>);
** Solo podra usarse esta obra si se respeta la Licencia.
* Puede obtenerse una copia de la Licencia en:
*
* http://ec.europa.eu/idabc/eupl5
*
** Salvo cuando lo exija la legislacion aplicable o se acuerde.
* por escrito, el programa distribuido con arreglo a la
** Licencia se distribuye <<TAL CUAL>>,
** SIN GARANTIAS NI CONDICIONES DE NINGUN TIPO, ni expresas
** ni implicitas.
** Vease la Licencia en el idioma concreto que rige
* los permisos y limitaciones que establece la Licencia.
*/
package es.mitc.redes.urbanismoenred.servicios.dal;
import javax.ejb.Local;

import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Determinacion;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidad;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidaddeterminacion;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Operacion;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Plan;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Relacion;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Tramite;

/**
 * Bean encargado de eliminar de forma ordenada las entidades del esquema de
 * planeamiento.
 * Se crea porque durante el proceso de refundido el borrado en cascada
 * presenta problemas de sincronismo del estado del EntityManager y de la
 * base de datos. 
 * 
 * @author Arnaiz Consultores
 *
 */
@Local
public interface EliminadorEntidadesLocal {
	/**
	 * 
	 * @param determinacion
	 * @param listener
	 */
	void eliminar(Determinacion determinacion, EliminadorListener listener);
	/**
	 * 
	 * @param entidad
	 * @param listener
	 */
	void eliminar(Entidad entidad, EliminadorListener listener);
	/**
	 * 
	 * @param ed
	 * @param listener
	 */
	void eliminar(Entidaddeterminacion ed, EliminadorListener listener);
	/**
	 * 
	 * @param operacion
	 * @param listener
	 */
	void eliminar(Operacion operacion, EliminadorListener listener);
	/**
	 * 
	 * @param plan
	 * @param listener
	 */
	void eliminar(Plan plan, EliminadorListener listener);
	/**
	 * 
	 * @param relacion
	 * @param listener
	 */
	void eliminar(Relacion relacion, EliminadorListener listener);
	/**
	 * 
	 * @param tramite
	 * @param incluido Si se elimina también el trámite o no
	 * @param listener Receptor del progreso del borrado.
	 */
	void eliminar(Tramite tramite, boolean incluido, EliminadorListener listener);
}

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

import es.mitc.redes.urbanismoenred.data.rpm.refundido.Determinacion;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Documento;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Entidad;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Entidaddeterminacion;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Relacion;

/**
 * Servicio que se encarga de eliminar registros del esquema de refundido.
 * 
 * Esto se podría realizar configurando un borrado en cascada en las entidades
 * de persistencia, pero en ocasiones se necesita un control más fino del 
 * borrado. Y sobre todo, se han detectado inconsistencias en la sincronización
 * del contexto de persistencia que hace que el entity manager devuelva
 * objetos que ya no existen en base de datos.
 * 
 * Seguramente esta pérdida de la sincronización de entidades se deba a la
 * utilización de sentencias SQL. Si se eliminan dichas sentencias puede que 
 * esto no sea necesario.
 * 
 * @author Arnaiz Consultores.
 *
 */
@Local
public interface EliminadorEntidadesRefundidoLocal {

	/**
	 * 
	 * @param det
	 */
	void eliminar(Determinacion det);

	/**
	 * 
	 * @param doc
	 */
	void eliminar(Documento doc);

	/**
	 * 
	 * @param ent
	 */
	void eliminar(Entidad ent);

	/**
	 * 
	 * @param ed
	 */
	void eliminar(Entidaddeterminacion ed);

	/**
	 * 
	 * @param regulacion
	 */
	void eliminar(Relacion relacion);

}

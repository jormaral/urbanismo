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

/**
 * Interfaz que define la funcionalidad del gestor encargado de persistir
 * la información de los FIP en la base de datos RPM.
 * 
 * @author Arnaiz Consultores
 *
 */
@Local
public interface GestorFipLocal {
	
	/**
	 * Añade un observador a los eventos generados por el gestor. Si ya está 
	 * registrado no hace nada. 
	 * 
	 * @param listener Observador que aceptará los eventos generados.
	 */
	public void addListener(GestorFipListener listener);
	/**
	 * Borra el trámite de un FIP. Este borrado elimina todos los elementos
	 * asociados al trámite definidos en el FIP. El trámite en sí no se borra.
	 * 
	 * @param codigoTramite Código FIP del trámite
	 * 
	 * @throws ExcepcionPersistencia
	 */
	void borrarFip(String codigoTramite) throws ExcepcionPersistencia;
	
	/**
	 * Borra el trámite de un FIP. Este borrado elimina todos los elementos
	 * asociados al trámite definidos en el FIP. El trámite en sí no se borra.
	 * 
	 * @param idTramite Identificador del trámite en base de datos.
	 * 
	 * @throws ExcepcionPersistencia
	 */
	void borrarFip(int idTramite) throws ExcepcionPersistencia;
	
	
	/**
	 * Devuelve todos los tramites que se encuentran pendientes de consolidar.
	 * 
	 * @return Listado de trámites pendientes de subir su FIP. Si no hay 
	 * trámites pendientes devuelve la lista vacía.
	 * @throws ExcepcionPersistencia
	 */
	Tramite[] getTramitesPendientes() throws ExcepcionPersistencia;
	
	/**
	 * Guarda la información de un FIP en base de datos.
	 * 
	 * @param fip Objeto FIP que se va a persistir en base de datos.
	 * 
	 * @return Identificador del trámite en base de datos.
	 * @throws ExcepcionPersistencia
	 */
	int guardarFip(FIP fip) throws ExcepcionPersistencia;
	
	/**
	 * Elimina el observador de la lista de entidades que reciben eventos del 
	 * gestor. Si el observador no está registrado no hace nada.
	 * 
	 * @param listener Observador registrado en los eventos generados.
	 */
	public void removeListener(GestorFipListener listener);
}

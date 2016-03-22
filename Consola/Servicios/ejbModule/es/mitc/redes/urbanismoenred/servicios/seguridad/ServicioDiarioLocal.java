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
package es.mitc.redes.urbanismoenred.servicios.seguridad;

import java.util.Date;

import javax.ejb.Local;

import es.mitc.redes.urbanismoenred.data.rpm.seguridad.Diario;
import es.mitc.redes.urbanismoenred.data.rpm.seguridad.Subsistema;
import es.mitc.redes.urbanismoenred.servicios.dal.ExcepcionPersistencia;

/**
 * Servicio de gestión de registro de acciones de usuario en el portal.
 * 
 * @author Arnaiz Consultores
 *
 */
@Local
public interface ServicioDiarioLocal {
	
	/**
	 * 
	 * @param identificador
	 * @return
	 */
	Diario getRegistro(int identificador);
	
	/**
	 * 
	 * @param inicio
	 * @param fin
	 * @return
	 */
	Diario[] getRegistros(Date inicio, Date fin);
	/**
	 * 
	 * @param idUsuario
	 * @return
	 */
	Diario[] getRegistrosSubsistema(int idUsuario);
	/**
	 * 
	 * @param idUsuario
	 * @return
	 */
	Diario[] getRegistrosUsuario(int idUsuario);
	/**
	 * 
	 * @param idUsuario
	 * @param elementos
	 * @param pagina
	 * @return
	 */
	Diario[] getRegistrosUsuario(int idUsuario, int elementos, int pagina);
	
	/**
	 * 
	 * @return
	 */
	Subsistema[] getSubsistemas();
	
	/**
	 * 
	 * @param usuario Nombre del usuario que realiza la acción
	 * @param ss Subsistema al que se aplica.
	 * @param textoAccion Descripción de la acción realizada
	 * @throws ExcepcionPersistencia
	 */
	void nuevoRegistroDiario(String usuario, TipoSubsistema ss, String textoAccion) throws ExcepcionPersistencia;
	
	/**
	 * 
	 * @param usuario
	 * @param ss
	 * @param textoAccion
	 * @param log
	 * @throws ExcepcionPersistencia
	 */
	void nuevoRegistroDiario(String usuario, TipoSubsistema ss, String textoAccion, String log) throws ExcepcionPersistencia;
}

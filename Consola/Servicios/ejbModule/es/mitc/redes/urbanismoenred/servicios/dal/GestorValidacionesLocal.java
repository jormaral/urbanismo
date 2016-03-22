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

import java.util.List;
import java.util.Map;

import javax.ejb.Local;

import es.mitc.redes.urbanismoenred.data.validacion.Validacion;
import es.mitc.redes.urbanismoenred.servicios.validacion.ContextoValidacion;

/**
 * Interfaz que define la funcionalidad ofrecida por el gestor de 
 * validaciones. El gestor de validaciones permite obtener información
 * sobre las validaciones almacenadas en base de datos.
 * 
 * @author Arnaiz Consultores
 *
 */
@Local
public interface GestorValidacionesLocal {
	static final int TODAS_VALIDACIONES = 0;
	static final int VALIDACIONES_CONDICIONES_URBANISTICAS = 1;
	static final int VALIDACIONES_DETERMINACIONES = 2;
	static final int VALIDACIONES_DOCUMENTOS = 5;
	static final int VALIDACIONES_ENTIDADES = 3;
	static final int VALIDACIONES_TRAMITE = 4;
	static final int VALIDACIONES_OTRAS = 6;
	
	/**
	 * Recupera todas las validaciones disponibles de un tipo determinado.
	 * 
	 * @param tipo Tipo de validación al que pertenecen las validaciones.
	 * @return Listado de validaciones aplicables a ese tipo. Si no existen 
	 * validaciones para ese tipo devuelve una lista vacía.
	 * 
	 * @throws ExcepcionPersistencia
	 */
	List<Validacion> obtenerValidaciones(int tipo) throws ExcepcionPersistencia;
	
	/**
	 * Ejecuta las validaciones correspondientes al tipo especificado para un 
	 * FIP identificado por idFip
	 * @param idFip Identificador del FIP
	 * @param tipo Tipo de validaciones aplicadas. 
	 * @param parametros Parámetros a aplicar en las validaciones.
	 * @return Verdadero si las validaciones se aplicaron correctamente y no 
	 * se obtuvo ninguna validación incorrecta. Falso en caso contrario.
	 * @throws ExcepcionPersistencia
	 */
	boolean ejecutarValidaciones(String idFip, int tipo, Map<String, Object> parametros) throws ExcepcionPersistencia;
	
	void ejecutarValidaciones(ContextoValidacion contexto) throws ExcepcionPersistencia;
}

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
package es.mitc.redes.urbanismoenred.servicios.validacion;

import java.util.List;

import javax.ejb.Local;

import es.mitc.redes.urbanismoenred.data.validacion.Proceso;
import es.mitc.redes.urbanismoenred.data.validacion.Resultado;
import es.mitc.redes.urbanismoenred.data.validacion.Validacion;
import es.mitc.redes.urbanismoenred.servicios.dal.ExcepcionPersistencia;
import es.mitc.redes.urbanismoenred.servicios.seguridad.Usuario;

/**
 * @author Arnaiz Consultores
 *
 */
@Local
public interface ServicioResultadosValidacionLocal extends ServicioResultadosValidacionInterface{

	
	/**
	 * Finaliza el proceso actual asociado al FIP especificado como parámetro.
	 *  
	 * @param idFip código del FP sobre el que se realiza la validación.
	 * 
	 * @throws ExcepcionPersistencia
	 */
	void finalizarProceso(String idFip) throws ExcepcionPersistencia;
	
	/**
	 * Devuelve el proceso cuyo identificador corresponda con el pasado como
	 * parámetro.
	 * 
	 * @param identificador Identificador del proceso
	 * @return Proceso cuyo identificador corresponda con el pasado como
	 * parámetro. Si no existe tal proceso devuelve el valor nulo.
	 */
	Proceso getProceso(int identificador);
	
	/**
	 * Devuelve el proceso en el que se ha consolidado un determinado trámite.
	 * 
	 * @param idFip Identificador del FIP.
	 * @return El proceso que ha sido consolidado o null si no hay proceso 
	 * consolidado o si por cualquier error en el sistema hay más de un proceso
	 * consolidado.
	 */
	Proceso getProcesoConsolidado(String idFip);
	
	/**
	 * Devuelve todos los procesos de validación realizados.
	 * 
	 * @return Lista de procesos de validación. Si no hay procesos devuelve la 
	 * lista vacía.
	 */
	List<Proceso> getProcesos();
	
	/**
	 * Devuelve todos los procesos de validación realizados sobre un FIP.
	 * 
	 * @param idFip Código del FIP sobre el que se han realizado las validaciones.
	 * @return Lista de procesos de validación. Si no hay procesos devuelve la 
	 * lista vacía.
	 */
	List<Proceso> getProcesos(String idFip);
	
	/**
	 * Devuelve todos los procesos de validación realizados sobre FIPs cuyo
	 * ámbito es validable por el usuario especificado.
	 * 
	 * @param usuario Usuario por el que se desean filtrar los procesos.
	 * @return Lista de procesos de validación. Si no hay procesos devuelve la 
	 * lista vacía.
	 */
	List<Proceso> getProcesos(Usuario usuario);
	
	/**
	 * Devuelve los resultados obtenidos de un proceso de validación dado.
	 * 
	 * @param idProceso Identificador del proceso.
	 * @param tipo Tipo de resultados a mostrar. Puede ser ERRORES, CORRECTOS,
     * AVISOS y TODOS 
	 * @param codigoValidacion Tipo de validación de la que podemos obtener los
     * resultados obtenidos (DETERMINACIONES, ENTIDADES, TODAS)
     * 
	 * @return Lista con los resultados de la validación de ese FIP. Si no hay 
	 * resultados devuelve una lista vacía
	 * @throws ExcepcionPersistencia
	 */
	List<Resultado> getResultados(int idProceso, TipoResultado tipo, TipoValidacion codigoValidacion) throws ExcepcionPersistencia;
    
    /**
     * Devuelve la lista de los resultados obtenidos del proceso de validación 
     * activo de un FIP.
     * 
     * @param idFip Identificador del FIP validado
     * @param tipo Tipo de resultados a mostrar. Puede ser ERRORES, CORRECTOS,
     * AVISOS y TODOS
     * @param codigoValidacion Tipo de validación de la que podemos obtener los
     * resultados obtenidos (DETERMINACIONES, ENTIDADES, TODAS)
     * 
     * @return Lista con los resultados de la validación de ese FIP. Si no hay 
	 * resultados devuelve una lista vacía
     * @throws ExcepcionPersistencia Si no hay proceso activo.
     */
    List<Resultado> getResultados(String idFip, TipoResultado tipo, TipoValidacion codigoValidacion) throws ExcepcionPersistencia;
    
    /**
     * Inicia un proceso de validación para un FIP identificado por el código 
     * de su trámite. Sólo puede haber un proceso de validación inicado sobre 
     * un FIP.
     * 
     * @param idFip Código del trámite que va dentro del FIP.
     * @return Identificador del proceso de validación iniciado
     * @throws ExcepcionPersistencia Si no se puede iniciar el proceso.
     */
    int iniciarProceso(String idFip) throws ExcepcionPersistencia;
    
    /**
     * Registra un resultado de un proceso de validación en base de datos.
     * Si el proceso de validación no ha sido iniciado se crea.
     * 
     * @param idFip Identificador del FIP a validar
     * @param validacion Validacion aplicada
     * @param exito Indica si la validación se ha realizado correctamente o no.
     * @param resultados En caso de que se haya producido un error, contiene una 
     * lista de datos de los elementos que han producido el error
     * @throws ExcepcionPersistencia Devuelve una excepción si no puede registrar el resultado.
     */
    void registrarResultado(String idFip, Validacion validacion, boolean exito, List<?> resultados) throws ExcepcionPersistencia;

    /**
     * Registra un resultado de un proceso de validación en base de datos.
     * Si el proceso de validación no ha sido iniciado se crea.
     * 
     * @param idFip Identificador del FIP a validar
     * @param idValidacion Identificador de la validación aplicada
     * @param exito Indica si la validación se ha realizado correctamente o no.
     * @param mensajes En caso de que se haya producido un error, contiene una 
     * lista de datos de los elementos que han producido el error
     * @throws ExcepcionPersistencia Devuelve una excepción si no puede registrar el resultado.
     */
	void registrarResultado(String idFip, int idValidacion, boolean exito, List<String> mensajes) throws ExcepcionPersistencia;
}

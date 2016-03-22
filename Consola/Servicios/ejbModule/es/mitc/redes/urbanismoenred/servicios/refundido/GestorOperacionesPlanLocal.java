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

import javax.ejb.Local;

/**
 * 
 * @author Arnaiz Consultores
 *
 */
@Local
public interface GestorOperacionesPlanLocal {
	static final String SOLO_OPERADORES ="operaciones.plan.solooperadores";
	static final String TRAMITE_OPERADO = "operaciones.plan.tramite.operado";
	static final String TRAMITE_OPERADOR = "operaciones.plan.tramite.operador";
	
	/**
	 * Un plan desarrollador NO desarrolla directamente a su plan padre, sino al
	 * plan raiz de su rama en el árbol de planes; es decir, aquel precedente que
	 * tiene idPadre nulo.
	 * El procedimiento consiste recortar gráficamente todas las entidades del
	 * trámite operado que están dentro de la entidad idAA (entidad 'Ámbito de Aplicación' del
	 * trámite operador) y que pertenecen a alguno de los grupos de entidades contemplados
	 * en el trámite operador, y añadir éste al trámite operado.
	 * 
	 * @param contexto
	 * @return
	 */
	boolean aplicarDesarrollo(ContextoRefundido contexto);
	/**
	 * 
	 * @param contexto
	 */
	void aplicarIncorporacion(ContextoRefundido contexto);
	
	/**
	 * 
	 * @param contexto
	 * @throws ExcepcionRefundido
	 */
	void aplicarModificacion(ContextoRefundido contexto) throws ExcepcionRefundido;
	
	/**
	 * 
	 * @param contexto
	 * @throws ExcepcionRefundido 
	 */
	void aplicarRefundido(ContextoRefundido contexto) throws ExcepcionRefundido;
	
	/**
	 * 
	 * @param contexto
	 */
	void aplicarRevocacionParcial(ContextoRefundido contexto);
	
	/**
	 * No se tiene en cuenta el Ámbito de Aplicación del trámite suspendedor iTramiteR para capturar
	 * las entidades del trámite que se va a suspender. De éste, se seleccionan todas las entidades.
	 * 
	 * @param contexto
	 */
	void aplicarRevocacionTotal(ContextoRefundido contexto);
	
	/**
	 * 
	 * @param contexto
	 */
	void aplicarSuspensionParcial(ContextoRefundido contexto);
	
	/**
	 *  No se tiene en cuenta el Ámbito de Aplicación del trámite suspendedor iTramiteR para capturar
	 *  las entidades del trámite que se va a suspender. De éste, se seleccionan todas las entidades.
	 *  
	 * @param contexto
	 */
	void aplicarSuspensionTotal(ContextoRefundido contexto);
	
	/**
	 * 
	 * @param contexto
	 * @throws ExcepcionRefundido 
	 */
	void aplicarSustitucion(ContextoRefundido contexto) throws ExcepcionRefundido;
}

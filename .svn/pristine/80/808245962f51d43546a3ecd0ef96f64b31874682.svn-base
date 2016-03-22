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
package es.mitc.redes.urbanismoenred.servicios.refundido;
import java.util.List;

import javax.ejb.Local;

import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Plan;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Entidad;

/**
 * Servicio que realiza operaciones básicas sobre entidades del esquema de 
 * refundido.
 * 
 * @author Arnaiz Consultores
 *
 */
@Local
public interface OperadorEntidadesRefundidoLocal {
	
	/**
	 * Indica si se ha creado la carpeta Entidades Aportadas para el código
	 * de plan indicado.
	 * 
	 * @param codigoPlan Código del plan.
	 * @param contexto Contexto de refundido.
	 * @return Verdadero si la carpeta ya existe y falso en caso contrario.
	 */
	boolean existeCarpetaAportadas(String codigoPlan, ContextoRefundido contexto);
	
	/**
	 * Devuelve una lista con todas las entidades aportadas creadas
	 * en este contexto de refundido.
	 * 
	 * @param contexto Contexto de refundido.
	 * @return Lista de entidades carpeta de aportadas por los diversos 
	 * planes. Si no ha habido entidades aportadas devuelve una lista 
	 * vacía.
	 */
	List<Entidad> getAportadas(ContextoRefundido contexto);
	
	/**
	 * Devuelve el último código de entidad empleado en el trámite de refundido.
	 * 
	 * @param contexto
	 * @return
	 */
	String getSiguienteCodigo(ContextoRefundido contexto);
	
	/**
	 * Devuelve el ámbito de aplicación del trámite especificado.
	 * 
	 * @param plan Plan del que se quiere obtener el ámbito.
	 * @param idproceso Identificador del proceso de refundido.
	 * @return Ámbito del trámite o valor nulo si no lo localiza.
	 */
	Entidad obtenerAmbitoAplicacion(Plan plan, int idproceso);

	/**
	 * Devuelve la carpeta que contiene a su vez las carpetas con las entidades
	 * aportadas por cada uno de los planes
	 * 
	 * @param contexto Contexto de refundido
	 * @return Carpeta de Entidades Aportadas.
	 * @throws ExcepcionRefundido
	 */
	Entidad obtenerCarpetaEntidadesAportadas(ContextoRefundido contexto) throws ExcepcionRefundido;

	/**
	 * Obtiene la carpeta de entidades aportadas por el plan con código
	 * codigoPlan dentro del trámite refundido. 
	 * 
	 * @param plan Plan que aporta las entidades.
	 * @param contexto Contexto de refundido
	 * @param crear Indica si se debe crear la carpeta en caso de que no exista.
	 * @return Carpeta de Entidades Aportadas por el plan o null si no 
	 * existe esa carpeta y crear se ha especificado como false.
	 * @throws ExcepcionRefundido 
	 */
	Entidad obtenerCarpetaEntidadesAportadas(Plan plan,
			ContextoRefundido contexto, boolean crear) throws ExcepcionRefundido;
}

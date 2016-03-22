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
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Determinacion;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Tramite;

/**
 * Servicio que ofrece operaciones básicas sobre determinaciones del esquema 
 * de refundido.
 * 
 * @author Arnaiz Consultores
 *
 */
@Local
public interface OperadorDeterminacionesRefundidoLocal {
	
	/**
	 * Devuelve una lista con todas las determinaciones aportadas creadas
	 * en este contexto de refundido.
	 * 
	 * @param contexto Contexto de refundido.
	 * @return Lista de determinaciones carpeta de aportadas por los diversos 
	 * planes. Si no ha habido determinaciones aportadas devuelve una lista 
	 * vacía.
	 */
	List<Determinacion> getAportadas(ContextoRefundido contexto);
	
	/**
	 * Deuelve el siguiente código de determinación para el trámite refundido.
	 * 
	 * @param contexto Cotnexto de refundido
	 * @return Código de determinación disponible.
	 */
	String getSiguienteCodigo(ContextoRefundido contexto);
	
	/**
	 * Indica si se ha creado la carpeta Determinaciones aportadas del plan
	 * indicado.
	 * 
	 * @param codigoPlan Código del plan que aporta las determinaciones.
	 * @param contexto Contexto de refundido.
	 * @return Verdadero si la carpeta existe, falso en caso contrario.
	 */
	boolean existeCarpetaAportadas(String codigoPlan, ContextoRefundido contexto);
	
	/**
	 * Devuelve la determinación Afección poligonal del plan base del contexto
	 * de refundido.
	 * 
	 * @param contexto Contexto de refundido.
	 * @return Determinación Afección (poligonal) o valor nulo si no existe.
	 */
	Determinacion obtenerAfeccion(ContextoRefundido contexto);
	
	/**
	 * Devuelve la determinación que contiene las carpetas de las 
	 * determinaciones aportadas por los diversos planes.
	 * 
	 * @param contexto Contexto de refundido.
	 * @return Determinacion Carpeta de Determinaciones Aportadas por refundido.
	 */
	Determinacion obtenerCarpetaDeterminacionesAportadas(ContextoRefundido contexto);

	/**
	 * Obtiene la carpeta de determinaciones aportadas por el plan con código
	 * codigoPlan dentro del trámite refundido. 
	 * 
	 * @param plan Plan que aporta las determinaciones.
	 * @param contexto Contexto de refundido.
	 * @param crear Indica si se debe crear la carpeta en caso de que no exista.
	 * @return Determinacion Carpeta de Determinaciones Aportadas o null si no 
	 * existe esa carpeta y crear se ha especificado como false.
	 */
	Determinacion obtenerCarpetaDeterminacionesAportadas(Plan plan,
			ContextoRefundido contexto, boolean crear);

	/**
	 * Devuelve la determinación "Carpeta" de uno de los trámites base 
     * usados, si es que se está usando alguno.
     * En caso contrario, se busca la determinación en el trámite 
     * solicitado y, si no existe, se crea en él.
	 * @param contexto Contexto de refundido
     * 
	 * @return
	 */
	Determinacion obtenerDeterminacionCarpeta(ContextoRefundido contexto);

	/**
	 * 
	 * @param tramite
	 * @return
	 */
	Determinacion obtenerDeterminacionGrupoDeEntidades(Tramite tramite);

}

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

import es.mitc.redes.urbanismoenred.data.rpm.refundido.Plan;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Tramite;

/**
 * Interfaz para transportar entidades desde el esquema de planeamiento al
 * esquema de refundido.
 * 
 * @author Arnaiz Consultores
 *
 */
@Local
public interface ReplicadorRefundidoLocal {

	/**
	 * Copia un plan desde el esquema de planeamiento al esquema de refundido.
	 * Sólo copia los datos básicos necesarios para el refundido.
	 * 
	 * @param proceso Identificador del proceso de refundido sobr el que se 
	 * realiza la copia.
	 * @param original Plan que se va a copiar
	 * @param incluirTramite Si es verdadero incluye en el plan copiado el 
	 * trámite vigente del plan.
	 * @recalcular Indica si se tienen que volver a calcular los códigos de las
	 * entidades y las determinaciones.
	 * @return El plan copiado en el esquema de refundido.
	 */
	Plan copiar(int proceso, 
			es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Plan original,
			boolean incluirTramite,
			boolean recalcular);
	/**
	 * Copia el trámite en el plan indicado.
	 * 
	 * @param proceso Identificador del proceso de refundido sobre el que se 
	 * realiza la copia.
	 * @param original Trámite que se va a copiar
	 * @param destino Trámite al que se van a copiar los datos del original. Si
	 * se especifica este trámite se copia el contenido del trámite, pero no
	 * los datos del trámite en sí. Si se especifica el valor nulo se intentará
	 * crear un trámite con esos valores. 
	 * @recalcular Indica si se tienen que volver a calcular los códigos de las
	 * entidades y las determinaciones.
	 * @return Trámite copiado en el esquema de refundido
	 */
	Tramite copiar(int proceso,
			es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Tramite original,
			Tramite destino,
			boolean recalcular);
	
	/**
	 * Permite obtener la correspondencia en el esquema de refundido para una
	 * entidad del esquema de planeamiento. Esta correspondencia es relativa
	 * a un proceso de refundido (ya que una misma entidad de planeamiento 
	 * puede tomar parte en distintos refundidos con distintos resultados).
	 * 
	 * @param proceso Identificador de proceso de refundido. El identificador
	 * de proceso 0 está reservado para entidades comunes a todos los refundidos
	 * como pueden ser las entidades de los planes base.
	 * @param clase Clase del objeto a la que corresponde el identificador.
	 * @param idplaneamiento Identificador de la entidad en la tabla de planeamiento.
	 * @return Devuelve la entidad o el valor nulo si no encuentra
	 * correspondencia.
	 */
	<T> T getReferencia(int proceso, Class<T> clase, int idplaneamiento);
	
	/**
	 * Permite obtener la correspondencia en el esquema de refundido para una
	 * entidad del esquema de planeamiento. Esta correspondencia es relativa
	 * a un proceso de refundido (ya que una misma entidad de planeamiento 
	 * puede tomar parte en distintos refundidos con distintos resultados).
	 * 
	 * @param proceso Identificador de proceso de refundido. El identificador
	 * de proceso 0 está reservado para entidades comunes a todos los refundidos
	 * como pueden ser las entidades de los planes base.
	 * @param tabla Nombre de la tabla a la que corresponde el identificador.
	 * @param idplaneamiento Identificador de la entidad en la tabla de planeamiento.
	 * @return Devuelve el identificador de la entidad o 0 si no encuentra
	 * correspondencia.
	 */
	int getReferencia(int proceso, String tabla, int idplaneamiento);
	

}

package es.mitc.redes.urbanismoenred.servicios.refundido;
import javax.ejb.Local;

import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidadlin;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidadpnt;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidadpol;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Documento;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Relacion;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Tramite;

/**
 * Interfaz para copiar datos desde el esquema de planeamiento al esquema de
 * refundido.
 * 
 * @author Arnaiz Consultores
 *
 */
@Local
public interface CopiadorRefundidoLocal {
	/**
	 * 
	 * @param proceso
	 * @param original
	 */
	void copiar(int proceso, Entidadlin original);
	
	/**
	 * 
	 * @param proceso
	 * @param original
	 */
	void copiar(int proceso, Entidadpnt original);
	
	/**
	 * 
	 * @param proceso
	 * @param original
	 */
	void copiar(int proceso, Entidadpol original);
	
	/**
	 * Copia un documento desde el esquema de planeamiento al esquema de 
	 * refundido. 
	 * 
	 * @param idProceso Identificador del proceso de refundido sobre el que se 
	 * realiza la copia.
	 * @param documento Documento que se va a copiar.
	 * @param tramite Trámite al que va a pertenecer el documento copiado.
	 * @return Documento copiado en el esquema de refundido.
	 */
	Documento copiar(
			int idProceso,
			es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Documento documento,
			Tramite tramite);
	
	/**
	 * 
	 * @param proceso
	 * @param original
	 * @param relacion
	 */
	void copiar(int proceso,
			es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Propiedadrelacion original, 
			Relacion relacion);
	
	/**
	 * 
	 * @param proceso
	 * @param relacion
	 * @param origen
	 * @param copiaTramite
	 * @return
	 */
	Relacion copiar(int proceso,
			es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Relacion relacion,
			es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Vectorrelacion origen, 
			Tramite copiaTramite);
	
	/**
	 * 
	 * @param proceso
	 * @param original
	 * @param copiaTramite
	 */
	void copiar(int proceso, es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Vectorrelacion original, Tramite copiaTramite);
	
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
	
	/**
	 * 
	 * @param proceso
	 * @param tabla
	 * @param idplaneamiento
	 * @param idrefundido
	 */
	void guardarReferencia(int proceso, String tabla, int idplaneamiento,
			int idrefundido);
}

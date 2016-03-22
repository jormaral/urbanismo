package es.mitc.redes.urbanismoenred.servicios.refundido;
import javax.ejb.Local;

import es.mitc.redes.urbanismoenred.data.rpm.refundido.Determinacion;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Entidad;

/**
 * 
 * @author Arnaiz Consultores
 *
 */
@Local
public interface GestorAportacionesLocal {

	/**
	 * Copia una determinación del esquema de planeamiento a la carpeta de 
	 * determinaciones aportadas del refundido correspondiente al plan al que 
	 * pertenece la determinación aportada.
	 * 
	 * Si la determinación aportada tiene una determinación padre, la 
	 * determinación padre también será aportada para mantener la jerarquía
	 * y orden del plan original.
	 * 
	 * @param aportada Determinación que se va a aportar.
	 * @param padre Determinación padre sobre la que se va a aportar. Si no se
	 * especifica se colocará en el nivel más bajo del plan refundido. 
	 * @param orden Define el nuevo orden de la determinación. Si no se
	 * especifica mantiene el que tuviera.
	 * @param contexto Contexto de refundido sobre el que se realiza la 
	 * aportación.
	 * @return Determinación de refundido correspondiente a la aportada.
	 * 
	 * @throws ExcepcionRefundido 
	 */
	Determinacion aportar(
			es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Determinacion aportada,
			Determinacion padre, Integer orden, ContextoRefundido contexto) throws ExcepcionRefundido;
	/**
	 * Copia una entidad del esquema de planeamiento a la carpeta de entidades 
	 * aportadas del refundido correspondiente al plan al que pertenece la
	 * entidad aportada.
	 * 
	 * Si la entidad aportada tiene una entidad padre, la entidad padre también
	 * será aportada para mantener la jerarquía y orden del plan original.
	 * 
	 * @param aportada Entidad que se va a aportar.
	 * @param padre Entidad padre sobre la que se va a aportar. Si no se
	 * especifica y tiene padre mantendrá su padre y su padre será también 
	 * aportado. Si no tiene padre se ubicará bajo la entidad Aportadas
	 * correspondiente a su plan.
	 * @param orden Define el nuevo orden de la determinación. Si no se
	 * especifica mantiene el que tuviera.
	 * @param contexto Contexto de refundido sobre el que se realiza la 
	 * aportación.
	 * @return Entidad de refundido correspondiente a la aportada.
	 * @throws ExcepcionRefundido 
	 */
	Entidad aportar(es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidad aportada, 
			Entidad padre, 
			Integer orden, 
			ContextoRefundido contexto) throws ExcepcionRefundido;
	
	/**
	 * Copia los datos de un trámite en la carpetas de entidades 
	 * y determinaciones aportadas por dicho trámite.
	 * 
	 * @param aportado Trámite que se va a aportar.
	 *
	 * @param contexto Contexto de refundido sobre el que se realiza la 
	 * aportación.
	 * 
	 * @throws ExcepcionRefundido 
	 */
	void aportar(es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Tramite aportado, ContextoRefundido contexto) throws ExcepcionRefundido;
	
	/**
	 * Indica si una determinación ha sido aportada en el contexto de refundido.
	 * 
	 * @param idDeterminacion Identificador de refundido de la determinación.
	 * @param contexto Contexto de refundido.
	 * @return Verdadero si ha sido aportada y falso en caso contrario.
	 */
	boolean determinacionEsAportada(int idDeterminacion, ContextoRefundido contexto);
}

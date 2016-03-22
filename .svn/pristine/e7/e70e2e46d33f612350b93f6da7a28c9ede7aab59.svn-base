package es.mitc.redes.urbanismoenred.servicios.dal;
import java.util.Map;

import javax.ejb.Local;

/**
 * 
 * @author Arnaiz Consultores
 *
 */
@Local
public interface CoordinadorGeneracionFIPLocal {
	/**
	 * Inicia un proceso de generación de FIP.
	 * 
	 * @param idtramite
	 * @return
	 * @throws ProcesoYaIniciado
	 */
	Map<String, Object> iniciarProceso(int idtramite) throws ProcesoYaIniciado;
	
	/**
	 * Devuelve el contenedor donde se van guardando los distintos bloques que
	 * constituyen el FIP.
	 * 
	 * @param idtramite Trámite para el que se va a generar el FIP
	 * @return Puede devolver null si no hay proceso iniciado.
	 */
	Map<String, Object> getProceso(int idtramite);
	
	/**
	 * Finaliza el proceso de generación de FIP.
	 * 
	 * @param idtramite
	 */
	void finalizarProceso(int idtramite);
}

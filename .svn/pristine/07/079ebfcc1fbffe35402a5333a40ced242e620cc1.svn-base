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
import java.util.List;

import javax.ejb.Local;

import es.mitc.redes.urbanismoenred.data.rpm.refundido.Proceso;
import es.mitc.redes.urbanismoenred.servicios.seguridad.Usuario;

/**
 * 
 * @author Arnaiz Consultores
 *
 */
@Local
public interface GestorContextosRefundidoLocal {
	
	/**
	 * 
	 * @param contexto
	 */
	void finalizarProceso(ContextoRefundido contexto);
	
	/**
	 * 
	 * @param tramites
	 * @param usuario
	 * @return
	 * @throws ExcepcionRefundido
	 */
	ContextoRefundido generarContexto(List<Integer> tramites, Usuario usuario, boolean crearTramite) throws ExcepcionRefundido;
	/**
	 * 
	 * @param idAmbito
	 * @return
	 */
	ContextoRefundido[] getContextos(int idAmbito);
	/**
	 * 
	 * @param usuario
	 * @return
	 */
	ContextoRefundido[] getContextos(Usuario usuario);
	
	Proceso getProceso(int idProceso);
	
	Proceso[] getProcesos(Usuario usuario) throws ExcepcionRefundido;
	/**
	 * 
	 * @param contexto
	 * @throws ExcepcionRefundido
	 */
	void iniciarProceso(ContextoRefundido contexto) throws ExcepcionRefundido;
}

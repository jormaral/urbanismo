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

import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Tramite;

/**
 * Define la interfaz del bean encargado de crear el trámite refundido en
 * base de datos.
 * 
 * Se ha extraido a un EJB distinto a RefundidoBean porque si se realizaba
 * dentro de RefundidoBean la operación se cancelaba al hacer setRollBackOnly()
 * aunque se marcara la función como REQUIRES_NEW.
 * 
 * Otra opción es incluir la creación dentro de RefundidoLocal y que sea el que 
 * llame el encargado de crear el Trámite, pero eso implica que el llamante 
 * también debe encargarse de iniciar y finalizar el proceso de refundido, cosa
 * que ahora es transparente. 
 * 
 * @author Arnaiz Consultores
 *
 */
@Local
public interface GestorTramitesRefundidoLocal {

	/**
	 * Al finalizar un proceso de refundido se creará el trámite de refundido en 
	 * aquel Plan que tenga una operacion de tipo 10 contra el plan 
	 * superviviente del proceso. Si no existe ningún plan se crea uno nuevo con 
	 * este tipo de operacion en el mismo ambito que el plan superviviente.
	 * 
	 * @param contexto
	 * @return 
	 * @throws ExcepcionRefundido
	 */
	Tramite crearTramiteRefundido(ContextoRefundido contexto)
			throws ExcepcionRefundido;

	/**
	 * Una vez creado el trámite de refundido y generado su FIP, se registra
	 * el trámite en la tabla FIP1 para poder realizar su validación.
	 * 
	 * @param contexto
	 */
	void registrarFip1(ContextoRefundido contexto);

}

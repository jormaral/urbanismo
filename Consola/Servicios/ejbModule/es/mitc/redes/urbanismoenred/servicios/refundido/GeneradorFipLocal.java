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

import java.io.File;

import javax.ejb.Local;


/**
 * @author Arnaiz Consultores
 *
 */
@Local
public interface GeneradorFipLocal {
	/**
	 * Genera el FIP 2 correspondiente a un trámite refundido.
	 * 
	 * @param contexto Trámite del que se va a generar el FIP2
	 * @param destino Archivo en el que se almacena el FIP generado.
	 * @throws ExcepcionRefundido 
	 */
	void generarFIP2(ContextoRefundido contexto, File destino) throws ExcepcionRefundido;
}

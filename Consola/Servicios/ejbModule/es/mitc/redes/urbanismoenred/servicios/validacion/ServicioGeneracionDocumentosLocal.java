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
package es.mitc.redes.urbanismoenred.servicios.validacion;

import javax.ejb.Local;

@Local
public interface ServicioGeneracionDocumentosLocal {
	
	
	/**
	 * Genera un documento con todos los resultados de un proceso de validación
	 * 
	 * @param proceso Id del proceso de validación
	 * 
	 * @return El pdf en bytes para su representación en el navegador
	 */
	byte[] generarDocumento(int proceso);
	
	/**
	 * Este método nos devuelve los datos de la validación según el fip y el tipo
	 * @param fip Id del fip que se esta validando
	 * @param validacion Tipo de la validacion cuyos errores seran representados
	 * 
	 * @return El pdf en bytes para su representación en el navegador
	 */
	byte[] generarDocumento(int proceso, TipoValidacion validacion);
	
	/**
	 * Genera un documento con los errores del proceso de validación
	 * 
	 * @param proceso Id del proceso de validacion
	 * @param visor nombre del municipio
	 * @return El pdf en bytes para su representación en el navegador
	 */
	byte[] generarDocumentoErrores(int proceso);
	
	/**
	 * Genera un documento con los errores de un subconjunto específico de
	 * validaciones del proceso de validación.
	 * 
	 * @param proceso Id del proceso de validación
	 * @param validacion Tipo de la validacion cuyos errores seran representados 
	 * 
	 * @return El pdf en bytes para su representación en el navegador
	 */
	byte[] generarDocumentoErrores(int proceso, TipoValidacion validacion);
	
}


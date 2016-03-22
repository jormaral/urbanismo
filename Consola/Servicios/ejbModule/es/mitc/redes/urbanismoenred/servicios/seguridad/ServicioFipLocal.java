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
package es.mitc.redes.urbanismoenred.servicios.seguridad;
import java.io.File;

import javax.ejb.Local;

/**
 * 
 * @author Arnaiz Consultores
 *
 */
@Local
public interface ServicioFipLocal {
	/**
	 * 
	 * @param codigoFip
	 * @return
	 * @throws ExcepcionSeguridad
	 */
	boolean esObsoleto(String codigoFip) throws ExcepcionSeguridad;

	/**
	 * Devuelve el archivo que contiene la plantilla correspondiente al 
	 * ámbito especificado.
	 * 
	 * @param idAmbito Identificador de ámbito.
	 * @param soloXml Si queremos que el ZIP devuelto contenga sólo el XML o el
	 * XML y sus documentos asociados.
	 * @return Archivo comprimido.
	 * @throws ExcepcionSeguridad
	 */
	File getTemplateFromAmbito(Integer idAmbito, boolean soloXml) throws ExcepcionSeguridad;
}

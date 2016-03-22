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
package es.mitc.redes.urbanismoenred.servicios.dal;

import javax.ejb.Local;

import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Documento;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Documentoshp;

/**
 * Interfaz que encapsula la funcionalidad relacionada con la gestión de
 * documentos en la base de datos de planeamiento.
 * 
 * @author jgarzon
 *
 */
@Local
public interface GestorDocumentosLocal {
	
	/**
	 * Obtiene todos los documentos asociados a un trámite a partir del código
	 * de dicho trámite.
	 * 
	 * @param codigoTramite Código del trámite.
	 * @return	Lista con los daocumentos asociados a un trámite. Si no tiene
	 * documentos devuelve la lista vacía.
	 */
	public Documento[] getDocumentos(String codigoTramite);
	/**
	 * Obtiene todas las hojas asociadas a un trámite a partir del código
	 * de dicho trámite.
	 * 
	 * @param codigoTramite Código del trámite.
	 * @return	Lista con los daocumentos asociados a un trámite. Si no tiene
	 * documentos devuelve la lista vacía.
	 */
	public Documentoshp[] getHojas(String codigoTramite);
}

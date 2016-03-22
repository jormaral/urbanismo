/*
* Copyright 2011 red.es
* Autores: Arnaiz Consultores.
*
* Licencia con arreglo a la EUPL, Versión 1.1 o -en cuanto
** sean aprobadas por la Comision Europea- versiones
** posteriores de la EUPL (la <<Licencia>>);
** Solo podra usarse esta obra si se respeta la Licencia.
* Puede obtenerse una copia de la Licencia en:
*
* http://ec.europa.eu/idabc/eupl5
*
** Salvo cuando lo exija la legislacion aplicable o se acuerde.
* por escrito, el programa distribuido con arreglo a la
** Licencia se distribuye <<TAL CUAL>>,
** SIN GARANTIAS NI CONDICIONES DE NINGUN TIPO, ni expresas
** ni implicitas.
** Vease la Licencia en el idioma concreto que rige
* los permisos y limitaciones que establece la Licencia.
*/
package es.mitc.redes.urbanismoenred.servicios.comunes;

import javax.ejb.Local;

import es.mitc.redes.urbanismoenred.data.fip.FIP;
import es.mitc.redes.urbanismoenred.utils.excepciones.RedesException;

/**
 * 
 * @author Arnaiz Consultores
 *
 */
@Local
public interface ServicioGestionXMLLocal {
	/**
     * Servicio que devuelve un objeto FIP a partir del XML que se le pasa como parametro
     * @param xmlFile 
     * @return
     * @throws es.mitc.redes.urbanismoenred.utils.excepciones.RedesException
     */
    public FIP obtenerObjetoFIPdelXML(String xmlFile) throws RedesException;
    
    /**
     * Servicio que devuelve un objeto FIP a partir del XML que se le pasa como parametro.
     * 
     * @param xmlFile Ruta del archivo XML que contiene el FIP
     * @param validar Verdadero si se debe validar el FIP con su esquema, falso
     * en caso contrario.
     *  
     * @return El objeto FIP definido en el archivo XML.
     * 
     * @throws es.mitc.redes.urbanismoenred.utils.excepciones.RedesException
     */
    public FIP obtenerObjetoFIPdelXML(String xmlFile, boolean validar) throws RedesException;
}

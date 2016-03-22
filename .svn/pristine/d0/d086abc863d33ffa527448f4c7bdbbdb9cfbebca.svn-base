/*
* Copyright 2011 red.es
* Autores: Arnaiz Consultores.
*
* Licencia con arreglo a la EUPL, Versión 1.1 o -en cuanto
* sean aprobadas por la Comision Europea- versiones
* posteriores de la EUPL (la <<Licencia>>);
* Solo podra usarse esta obra si se respeta la Licencia.
* Puede obtenerse una copia de la Licencia en:
*
* http://ec.europa.eu/idabc/eupl5
*
* Salvo cuando lo exija la legislacion aplicable o se acuerde
* por escrito, el programa distribuido con arreglo a la
* Licencia se distribuye <<TAL CUAL>>,
* SIN GARANTIAS NI CONDICIONES DE NINGUN TIPO, ni expresas
* ni implicitas.
* Vease la Licencia en el idioma concreto que rige
* los permisos y limitaciones que establece la Licencia.
*/

package es.mitc.redes.urbanismoenred.servicios.comunes;

import javax.ejb.Remote;

import es.mitc.redes.urbanismoenred.utils.excepciones.RedesException;

/**
 *
 * @author Arnaiz Consultores
 */
@Remote
public interface GestionIntroduccionFIPenSistemaRemote {

	/**
     * Copia el contenido del archivo comprimido subido al servidor en el
     * directorio de FIPs, donde es descomprimido para poder
     * iniciar el procesamiento.
     * El FIP se descomprime en un directorio cuyo nombre se compone con el
     * identificador del Trámite contenido en el FIP.
     * 
     * @param codTramite Identificador del trámite del FIP enviado.
     * @param sourceFile Archivo con el FIP y sus adjuntos.
     * 
     * @throws es.mitc.redes.urbanismoenred.utils.excepciones.RedesException
     */
    public void desplegarFIP(String codTramite, String sourceFile) throws RedesException;


    /**
	 * Dado un fichero zip cuya ruta completa se obtiene a partir
     * del codigo del trámite asociado al FIP, se carga en memoria y se guarda 
     * la informacion FIP completa.
     * 
     * @param codigoFip Código del trámite incluido en el FIP.
	 */
	public void guardarDatosXML(String codigoFip) throws RedesException;
}


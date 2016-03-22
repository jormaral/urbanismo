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

import javax.ejb.Local;


import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Tramite;
import es.mitc.redes.urbanismoenred.servicios.validacion.ContextoValidacion;
import es.mitc.redes.urbanismoenred.utils.excepciones.RedesException;


/**
 *
 * @author Arnaiz Consultores
 */
@Local
public interface GestionIntroduccionFIPenSistemaLocal {
	
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
     * Copia el contenido del archivo comprimido subido al servidor en el
     * directorio de FIPs, donde es descomprimido para poder
     * iniciar el procesamiento.
     * El FIP se descomprime en un directorio cuyo nombre se compone con el
     * identificador del Trámite contenido en el FIP.
     * 
     * @param contexto Contexto de validación en el que se realiza el despliegue.
     * @throws Redesexception
     */
    public void desplegarFIP(ContextoValidacion contexto) throws RedesException;
    
    /**
     * Devuelve los tramites no consolidados con FIP desplegados en el sistema.
     * 
     * @return Lista con los códigos de los FIP desplegados en el sistema.
     * @throws RedesException
     */
    public Tramite[] getTramitesDesplegados() throws RedesException;
    
    /**
     * Devuelve los trámites pendientes de recibir el archivo FIP.
     * 
     * @return Lisgta con los trámites pendientes de recibir los ficheros FIp. Si
     * no hay trámites pendientes devuelve una lista vacía.
     * @throws RedesException
     */
    public Tramite[] getTramitesPendientes() throws RedesException;
	
	/**
	 * Devuelve la última versión de un FIP almacenado en el sistema de archivos 
	 * dado el código del trámite del FIP.
	 * 
	 * @param codFip Código del trámite contenido en el FIP
	 * @return Devuelve la ruta al fichero XML que contiene el FIP. 
	 * 
	 * @throws RedesException Si por cualquier motivo no puede encontrar el FIP.
	 */
	public String getUltimaVersionFIP(String codFip) throws RedesException;

	/**
	 * Dado un fichero zip cuya ruta completa se obtiene a partir
     * del codigo del trámite asociado al FIP, se carga en memoria y se guarda 
     * la informacion FIP completa.
     * 
     * @param codigoFip Código del trámite incluido en el FIP.
	 */
	public void guardarDatosXML(String codigoFip) throws RedesException;
	
	/**
	 * Dado un fichero zip cuya ruta completa se obtiene a partir
     * del codigo del trámite asociado al FIP, se carga en memoria y se guarda 
     * la informacion FIP completa.
     * 
	 * @param contexto Contexto de validación en el que se realiza el guardado.
	 * @throws RedesException
	 */
	public void guardarDatosXML(ContextoValidacion contexto) throws RedesException;
}


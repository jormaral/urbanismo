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

import javax.ejb.Remote;

/**
 * @author Arnaiz Consultores
 */

@Remote
public interface ValidacionFipRemote {
	
	
    
    /**
     * Despliega un FIP en el sistema. El despliegue consiste en los siguientes 
	 * pasos:
	 * 	1: Recuperar el fichero ZIP subido al servidor y copiarlo en el 
	 * directorio de trabajo.
	 * 	2: Descomprimir el fichero ZIP
	 * 	3: Borrar cualquier información anterior del trámite en base de datos y 
	 * sustituirla por la contenida en el FIP.
	 * 
     * @param contexto Contexto de validación sobre el que se ejecuta el despliegue.
     * @throws ExcepcionValidacion
     */
    public void desplegarFip(ContextoValidacion contexto) throws ExcepcionValidacion;
    
    /**
     * Genera un informe sobre el resultado de la última validación ejecutada.
     * Si no hay validaciones completadas devuelve una excepción.
     * El formato del informe vendrá dado por lo indicado en el contexto, 
     * actualmente sólo se generan documentos PDF.
     * 
     * @param proceso Identificador del proceso de validación sobre el que se genera el informe.
     * @return Array de bytes con los datos del informe.
     * 
     * @throws ExcepcionValidacion
     */
    public byte[] generarInforme(int proceso) throws ExcepcionValidacion;
    
    
    /**
     * Genera los contextos de validación de los FIP pendientes de validar que 
     * pueden ser validados por el usuario indicado.
     * 
     * @param nombreUsuario Nombre de usuario del sistema.
     * @return Lista de contextos de validación disponibles para ese usuario. Si
     * no tiene ningún contexto pendiente devuelve una lista vacía.
     */
    public ContextoValidacion[] getFipPendientes(String nombreUsuario) ;
     
    /**
     * Inicia el proceso de validación del trámite especificado en el contexto.
     * 
     * @param contexto Contexto de validación sobre el que se realiza la
     * validación.
     * @throws ExcepcionValidacion
     */
    public void validar(ContextoValidacion contexto) throws ExcepcionValidacion;
}

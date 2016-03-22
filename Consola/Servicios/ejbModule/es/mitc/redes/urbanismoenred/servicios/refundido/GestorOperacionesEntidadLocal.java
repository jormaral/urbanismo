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

import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidad;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Operacionentidad;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Tramite;

/**
 * 
 * @author Arnaiz Consultores
 *
 */
@Local
public interface GestorOperacionesEntidadLocal {
	
	/**
     * Se cambia el idTramite de la entidad iEntR, y se cambia su idPadre para
     * que apunte a la entidad "Entidades aportadas por..." que se
     * debe crear en el trámite de iEntO
     * 
     * @param iEntO
     * @param iEntR
     * @param contexto
	 * @throws ExcepcionRefundido 
     */
	void aportacionEntidad(Entidad iEntidadAAo, Entidad iEntidadR, ContextoRefundido contexto) throws ExcepcionRefundido;

	/**
	 * Crea la carpeta de "Entidades aportadas" en el trámite iTramiteO con
	 * el nombre del trámite iTramiteR, si no existe en iTramiteO.
	 * De ella, se colgarán todas las entidades del iTramiteR que deban
	 * ser reasignadas al iTramiteO
	 * 
	 * @param tramiteO
	 * @param iTramiteR
	 * @param gListaDatosDoc
	 * @param listaIdTramitesBase
	 * @return
	 * @throws ExcepcionRefundido 
	 */
	Entidad crearCarpetaEntidadesAportadas(Tramite tramiteO, Tramite iTramiteR, ContextoRefundido contexto) throws ExcepcionRefundido;
	
	/**
	 * 
	 * @param iOperacionEntidad
	 * @param listaIdTramitesBase
	 * @throws ExcepcionRefundido 
	 */
	void ejecutar(Operacionentidad iOperacionEntidad, ContextoRefundido contexto) throws ExcepcionRefundido;
	
	/**
     * 1 - Elimina las determinaciones de carácter 'Enunciado' (1) o 'Enunciado Complementario' (11) que
     * no tengan hijas, y que no esten vinculadas a ningún documento ni relación.
     * 2 - Elimina las entidades de grupo 'Carpeta' que no tengan hijas, y que no esten vinculadas a
     * ningún documento ni relación.
     * 
     * @param idTramite
     * @param contexto
	 * @throws ExcepcionRefundido 
     */
	void eliminarCarpetasVacias(Tramite tramite, ContextoRefundido contexto) throws ExcepcionRefundido;
	
	/**
     * Comprueba si la entidad tiene un único tipo de geometría. Los posibles
     * valores de retorno son:
     * "Entidadpol"    = tiene, al menos, geometría en esta tabla.
     * "Entidadpnt"    = tiene, al menos, geometría en esta tabla.
     * "Entidadlin"    = tiene, al menos, geometría en esta tabla.
     * ""              = No tiene geometría.
     * "Error"         = Tiene más de un tipo de geometría.
     * 
     * @param iEnt
     * @param contexto
     * @return
     */
	String getGeometria(Entidad iEnt, ContextoRefundido contexto);
	
	/**
     * @param iEntO
	 * @param iEntR
	 * @param listaDatosDoc
	 * @param listaIdTramitesBase
     * @return
     */
	boolean incorporacionEntidad(Entidad iEntO, Entidad iEntR, ContextoRefundido contexto);
	
	/**
	 * 
	 * @param iEntO
	 * @param iEntR
	 * @param contexto
	 * @throws ExcepcionRefundido 
	 */
	void levantamientoParcialSuspension(Entidad iEntO, Entidad iEntR, ContextoRefundido contexto) throws ExcepcionRefundido;
	
	/**
	 * Reasignar el idTramite de la entidad iEntR al de iEntO, cambiando su
	 * código para evitar la restricción que impide la duplicación de códigos en
	 * un mismo trámite.
	 * Primero, se averigua cuál es el último código de entidad en
	 * el trámite iTramiteO.
	 * Segundo, se reasigna el trámite de iEntR desde iTramiteR a iTramiteO
	 * Se elimina el registro de PlanEntidadOrdenacion si los planes origen y
	 * destino son diferentes
	 * También se cambia el idTramite de sus elementos dependientes:
	 * - Entidades hijas
	 * - Documentos
	 * - Valores (determinaciones aplicadas, excepto la
	 * 		determinación 'Grupo de Entidades', determinaciones de
	 * 		régimen, y los valores de referencia). La determinación 'Grupo
	 * 		de Entidades' aplicada y su valor de referencia se cambian
	 * 		por sus equivalentes en el nuevo trámite iTramiteO (siempre 
	 * 		que no sean del plan base).
	 * El parámetro "orden" indica el orden que se le debe asignar a la entidad. Si es cero, significa
	 * que debe asignarse el último + 1. En cualquier caso, el orden del resto de entidades debe
	 * moverse para acomodar iEntR en su sitio.
	 * 
	 * @param iEntO
	 * @param iEntPadre
	 * @param iEntR
	 * @param orden
	 * @param listaIdTramitesBase
	 * @return
	 */
	boolean reasignarEntidad(Entidad iEntO, Entidad iEntPadre, Entidad iEntR, int orden, ContextoRefundido contexto);

	/**
	 * 
	 * @param iEntO
	 * @param iEntR
	 * @param contexto
	 * @throws ExcepcionRefundido 
	 */
	void suspensionParcial(Entidad iEntO, Entidad iEntR, ContextoRefundido contexto) throws ExcepcionRefundido;
	
	/**
     * Comprueba si las entidades tienen geometría y, además, son del
     * tipo adecuado:
     * iEntR --> polígono
     * iEntO --> polígono, punto, o línea
     * @param iEntO
     * @param iEntR
     * @param tramiteOrdenante
     * @param contexto
	 * @throws ExcepcionRefundido 
     */
	void sustraccionGrafica(Entidad iEntidadO, Entidad iEntidadAAr, Tramite tramiteOrdenante, ContextoRefundido contexto) throws ExcepcionRefundido;
}

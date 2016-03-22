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

import java.util.List;

import javax.ejb.Local;

import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Determinacion;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Operaciondeterminacion;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Tramite;
/**
 * 
 * @author Arnaiz Consultores
 *
 */
@Local
public interface GestorOperacionesDeterminacionLocal {
	 /**
     * Crea la carpeta de "Determinaciones aportadas" en el trámite tramiteO con
     * el nombre del trámite tramiteR, si no existe en tramiteO.
     * De ella, se colgarán todas las determinaciones del tramiteR que deban
     * ser reasignadas al tramiteO
     * 
     * @param tramiteO
     * @param tramiteR
     * @return
     */
	Determinacion crearCarpetaDeterminacionesAportadas(Tramite tramiteO, Tramite tramiteR);
	/**
	 * 
	 * @param iOperacionDeterminacion
	 * @param contexto
	 * @throws ExcepcionRefundido
	 */
	void ejecutar(Operaciondeterminacion iOperacionDeterminacion, ContextoRefundido contexto) throws ExcepcionRefundido;
	/**
     * Devuelve la determinación "Carpeta" de uno de los trámites base 
     * usados, si es que se está usando alguno.
     * En caso contrario, se busca la determinación en el trámite 
     * solicitado y, si no existe, se crea en él.
     * 
     * @param iTramite
     * @param listaIdTramitesBase
     * @return
     */
	Determinacion getDeterminacionCarpetaTramiteBase(Tramite iTramite, List<Integer> listaIdTramitesBase);
	/**
     * Reasignar el idTramite de la determinación iDetR al de iDetO, cambiando su
     * código para evitar la restricción que impide la duplicación de códigos en
     *  un mismo trámite.
     *  
     *  Primero, se averigua cuál es el último código de determinación en
     *  el trámite iTramiteO.
     *  
     *  Segundo, se reasigna el trámite de iDetR desde iTramiteR a iTramiteO
     *  
     *  Si la determinación es 'Grupo de Entidades' o algunos de sus valores de referencia, y
     *  ya existe en el trámite iTramiteO, no se hace el cambio, ya que en cada trámite sólo
     *  puede haber un conjunto de determinaciones de este tipo.
     *  
     *  El parámetro "orden" indica el orden que se le debe asignar a la determinación. Si es cero, significa
     *  que debe asignarse el último + 1. En cualquier caso, el orden del resto de determinaciones debe
     *  moverse para acomodar iDetR en su sitio.
     *  
     *  Reasigna el idTramiteCreador de sus relaciones
     *  
     * @param iDetO
     * @param iDetPadre
     * @param iDetR
     * @param orden
     * @param listaIdTramitesBase
     * @return
     */
	boolean reasignarDeterminacion(Determinacion iDetO, Determinacion iDetPadre, Determinacion iDetR, int orden, ContextoRefundido contexto);
}

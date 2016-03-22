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
package es.mitc.redes.urbanismoenred.servicios.dal;
import java.util.List;

import javax.ejb.Local;

import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Determinacion;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidad;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Tramite;

/**
 * 
 * 
 * @author Arnaiz Consultores
 *
 */
@Local
public interface GestorConsultasLocal {
	
	/**
	 * Averigua si el trámite pertenece a un plan que opere con una operación de desarrollo.
	 * 
	 * @param idTramite
	 * @return
	 */
	boolean esDesarrollo(int idTramite);
	
	/**
	 * El código del trámite refundido (sin encriptar en MD5) se compone de
	 * la concatenación de los siguientes 20 caracteres: aaaaaapppppp0000ttii
	 * donde:
	 * 	aaaaaa = iden del ámbito
	 * 	pppppp = iden del plan
	 * 	0000tt = iden del tipo de trámite
	 * 	ii = iteración
	 * 
	 * @param tramite
	 * @return
     * @throws ExcepcionPersistencia 
	 */
	String generarCodigoTramite(Tramite tramite) throws ExcepcionPersistencia;
	
	/**
	 * 
	 * @param idTramite
	 * @return
	 */
	String getSiguienteCodigoEntidad(int idTramite);
	
	/**
	 * 
	 * @return
	 */
	String getSiguienteCodigoPlan();
	
	/**
	 * 
	 * @param tramite
	 * @return
	 */
	Entidad obtenerAmbitoAplicacionPorTramite(Tramite tramite);
	
	/**
	 * Dada una entidad y un trámite, se devuelve la determinación valor de
	 * referencia de 'Grupo de Entidades' del trámite, que se corresponde con
	 * la determinación valor de referencia de 'Grupo de Entidades' cargada en
	 * la entidad. El nexo de unión es su vínculo con una determinación del
	 * plan base o, en su defecto, el nombre de la determinación.
	 * Si la determinación VR de iEnt1 es del plan base, se devuelve ésta.
	 * 
	 * @param entidad
	 * @param tramite
	 * @param listaIdTramitesBase
	 * @return
	 */
	Determinacion obtenerDeterminacionGrupoEquivalentePorEntidad(Entidad entidad, Tramite tramite, List<Integer> listaIdTramitesBase);
	/**
	 * 
	 * @param idTramite
	 * @return
	 */
	String getSiguienteCodigoDeterminacion(int idTramite);
	/**
	 * Recupera los documentos de todos los tramites (exceptuando los de determinación, entidad y caso)
	 * 
	 * @param tramites
	 * @return
	 */
	List<Object[]> obtenerDocumentos(Tramite[] tramites);
	
	/**
	 * 
	 * @param iTramite
	 * @return
	 */
	Determinacion obtenerDeterminacionGrupoDeEntidadesPorTramite(Tramite iTramite);
	
	/**
	 * 
	 * @param detPadre
	 * @param idTramite
	 * @param incrementar
	 * @return
	 */
	String obtenerUltimoApartadoDeterminacion(Determinacion detPadre, int idTramite, boolean incrementar);
}

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
package es.mitc.redes.urbanismoenred.utils.serviciosweb;

import es.mitc.redes.urbanismoenred.data.rpm.diccionario.Ambito;
import es.mitc.redes.urbanismoenred.data.rpm.explotacion.Capa;
import es.mitc.redes.urbanismoenred.data.rpm.explotacion.Peticion;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Documentoshp;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidad;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Plan;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Tramite;
import java.util.List;
import javax.ejb.Local;


/**
 * 
 * @author Arnaiz Consultores
 *
 */
@Local
public interface ServicioPublicacionLocal {
    Entidad[] findEntidadesRefundidoFromGeo(String geom,Integer idAmbito) throws ExcepcionPublicacion;
    Entidad[] findEntidadesFromGeo(String geom,Integer idTramite) throws ExcepcionPublicacion;
    Entidad[] findEntidadesLikeName_Plan(String nombreEntidad,String nombrePlan,Integer idAmbito) throws ExcepcionPublicacion;
    Entidad[] findEntidadesLikeName(String nombreEntidad,Integer idAmbito) throws ExcepcionPublicacion;
    Entidad[] findEntidadesLikeClave(String claveEntidad,Integer idAmbito) throws ExcepcionPublicacion;
    Plan[] findPlanesRaiz(Integer idAmbito) throws ExcepcionPublicacion;
    Plan[] findPlanesHijos(Integer idPadre) throws ExcepcionPublicacion;
    Plan[] findPlanesGeom(String geom,Integer idAmbito,boolean soloRaiz) throws ExcepcionPublicacion;
    Plan[] findPlanesHijosGeom(String geom,Integer idPadre) throws ExcepcionPublicacion;
    Plan[] findPlanesLikeName(String nombrePlan, Integer idAmbito) throws ExcepcionPublicacion;
    Tramite findUltimoTramiteConsolidado(Integer idPlan) throws ExcepcionPublicacion;
    Tramite findUltimoTramiteRefundido(Integer idAmbito) throws ExcepcionPublicacion;
    Tramite[] findTramitesGeom(String geom) throws ExcepcionPublicacion;
    Tramite[] findTramitesGeom(String geom,List<String> codigos) throws ExcepcionPublicacion;
    String getNombreTramiteCompleto(String codigoTramite,String idioma) throws ExcepcionPublicacion;
    void findSuperficiesRefundido(String geom, Integer idAmbito, List<Entidad> entidades, List<Double> superficies, List<Double> superficiesIntersectadas)  throws ExcepcionPublicacion;
    void findSuperficies(String geom, String codigoTramite, List<Entidad> entidades, List<Double> superficies, List<Double> superficiesIntersectadas)  throws ExcepcionPublicacion;
    boolean saveGeomPeticion (Peticion peticion) throws ExcepcionPublicacion;
    boolean isPlanRefundido (Integer idPlan) throws ExcepcionPublicacion;
    Ambito[] findAmbitosFromGeo(String geom) throws ExcepcionPublicacion;
    Ambito[] findAmbitosPadres() throws ExcepcionPublicacion;
    Ambito[] findAmbitosHijos(Integer idPadre) throws ExcepcionPublicacion;
    Ambito[] findAmbitosWithPlan() throws ExcepcionPublicacion;
    Ambito[] findAmbitosHijosWithPlan(Integer idPadre) throws ExcepcionPublicacion;
    Capa[] findCapaFromCodigoGrupo(String codigo) throws ExcepcionPublicacion;
    Documentoshp[] findPlanosRefundidoFromGeo(String geom, Integer idAmbito) throws ExcepcionPublicacion;
    Documentoshp[] findPlanosFromGeoTramite(String geom, Integer idTramite) throws ExcepcionPublicacion;
}

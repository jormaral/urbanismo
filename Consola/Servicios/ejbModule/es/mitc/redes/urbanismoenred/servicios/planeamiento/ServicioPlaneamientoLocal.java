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
package es.mitc.redes.urbanismoenred.servicios.planeamiento;
import java.io.File;
import java.util.Date;

import javax.ejb.Local;

import es.mitc.redes.urbanismoenred.data.rpm.diccionario.Instrumentoplan;
import es.mitc.redes.urbanismoenred.data.rpm.diccionario.Instrumentotipooperacionplan;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Ambitoaplicacionambito;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Casoentidaddeterminacion;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Determinacion;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Determinaciongrupoentidad;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Documento;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Documentoshp;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidad;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidaddeterminacionregimen;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Opciondeterminacion;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Operaciondeterminacion;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Operacionentidad;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Operacionplan;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Plan;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.PropiedadesUnidad;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Regimenespecifico;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.RegulacionEspecifica;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Tramite;

/**
 * Interfaz que encapsula las consultas realizadas para el esquema de 
 * planeamiento.
 * 
 * Esta interfaz obtiene objetos de persistencia cuyas relaciones están 
 * establecidas por defecto con lazy = true, por lo que cualquier intento
 * de acceder a dichas relaciones a través de objetos que no sean EJB darán
 * errores.
 *  
 * @author Arnaiz Consultores
 *
 */
@Local
public interface ServicioPlaneamientoLocal {
	/**
	 * 
	 * @param idPlan
	 * @param instrumento
	 * @throws ExcepcionPlaneamiento
	 */
	void asociarOperacion(int idPlan, int instrumento) throws ExcepcionPlaneamiento;
	/**
	 * 
	 * @param idPlan
	 * @param instrumento
	 * @param planesOperados
	 * @param operaciones
	 * @throws ExcepcionPlaneamiento
	 */
	void asociarOperacion(int idPlan, int instrumento, Integer[] planesOperados, Integer[] operaciones) throws ExcepcionPlaneamiento;
	/**
	 * 
	 * @param objeto
	 * @throws ExcepcionPlaneamiento
	 */
	void borrar(Object objeto) throws ExcepcionPlaneamiento;
	/**
	 * 
	 * @param identificador
	 * @throws ExcepcionPlaneamiento
	 */
	void borrarDeterminacion(int identificador) throws ExcepcionPlaneamiento;
	/**
	 * 
	 * @param identificador
	 * @throws ExcepcionPlaneamiento
	 */
	void borrarEntidad(int identificador) throws ExcepcionPlaneamiento;
	/**
	 * 
	 * @param nombre
	 * @param ambito
	 * @param padre
	 * @param base
	 * @return
	 * @throws ExcepcionPlaneamiento
	 */
	Plan crearPlan(String nombre, int ambito, String texto, Integer padre, Integer base) throws ExcepcionPlaneamiento;
	/**
	 * 
	 * @param tipo
	 * @param idPlan
	 * @param nombre
	 * @param texto
	 * @param comentario
	 * @param centroProduccion
	 * @param organo
	 * @param sentido
	 * @param fecha
	 * @return
	 * @throws ExcepcionPlaneamiento
	 */
	Tramite crearTramite(int tipo, int idPlan, String nombre, String texto, String comentario, int centroProduccion, Integer idorgano, Integer idsentido, Date fecha) throws ExcepcionPlaneamiento;
	/**
	 * 
	 * @param clase
	 * @return
	 */
	<T> T[] get(Class<T> clase);
	/**
	 * 
	 * @param clase
	 * @param identificador
	 * @return
	 */
	<T> T get(Class<T> clase, Object identificador);
	/**
	 * 
	 * @param idTramite
	 * @return
	 */
	Ambitoaplicacionambito[] getAmbitos(int idTramite); 
	/**
	 * 
	 * @param idDeterminacion
	 * @return
	 */
	Entidad[] getAplicacionesDeterminacion(int idDeterminacion);
	/**
	 * 
	 * @param tramite
	 * @param archivo
	 * @return
	 * @throws ExcepcionPlaneamiento
	 */
	File getArchivo(String tramite, String archivo) throws ExcepcionPlaneamiento;
	/**
	 * 
	 * @param idEntidad
	 * @param idDeterminacion
	 * @return
	 */
	Casoentidaddeterminacion[] getCasos(int idEntidad, int idDeterminacion);
	/**
	 * 
	 * @param idCaso
	 * @return
	 */
	Casoentidaddeterminacion[] getCasosVinculados(int idCaso);
	/**
	 * 
	 * @param idEntidad
	 * @return
	 */
	Determinacion[] getDeterminacionesActoEntidad(int idEntidad);
	/**
	 * 
	 * @param idEntidad
	 * @return
	 */
	Determinacion[] getDeterminacionesEntidad(int idEntidad);
	/**
	 * 
	 * @param idPadre
	 * @return
	 */
	Determinacion[] getDeterminacionesHijas(int idPadre);
	/**
	 * 
	 * @param idPadre
	 * @return
	 */
	Determinacion[] getDeterminacionesHijasPorActo(int idPadre);
	/**
	 * 
	 * @param idPadre
	 * @return
	 */
	Determinacion[] getDeterminacionesHijasPorRegimenDirecto(int idPadre);
	/**
	 * 
	 * @param idPadre
	 * @return
	 */
	Determinacion[] getDeterminacionesHijasPorUso(int idPadre);
	/**
	 * 
	 * @param idTramite
	 * @param idCaracter
	 * @return
	 */
	Determinacion[] getDeterminacionesPorCaracter(int idTramite, int idCaracter);
	/**
	 * 
	 * @param idTramite
	 * @return
	 */
	Determinacion[] getDeterminacionesRaiz(int idTramite);
	/**
	 * 
	 * @param idEntidad
	 * @return
	 */
	Determinacion[] getDeterminacionesRegimenDirectoEntidad(int idEntidad);
	/**
	 * 
	 * @param identificador
	 * @return
	 */
	Determinacion[] getDeterminacionesReguladoras(int identificador);
	/**
	 * 
	 * @param idEntidad
	 * @return
	 */
	Determinacion[] getDeterminacionesUsoEntidad(int idEntidad);
	/**
	 * 
	 * @param identificador
	 * @return
	 */
	Documento[] getDocumentosDeterminacion(int identificador);
	/**
	 * 
	 * @param idEntidad
	 * @return
	 */
	Documento[] getDocumentosEntidad(int idEntidad);
	/**
	 * 
	 * @param idTramite
	 * @return
	 */
	Documento[] getDocumentosTramite(int idTramite);
	/**
	 * 
	 * @param idPadre
	 * @return
	 */
	Entidad[] getEntidadesHijas(int idPadre);
	/**
	 * 
	 * @param idTramite
	 * @return
	 */
	Entidad[] getEntidadesRaiz(int idTramite);
	/**
	 * 
	 * @param idEntidad
	 * @return
	 */
	String[] getGeometriaEntidad(int idEntidad);
	/**
	 * 
	 * @param idEntidad
	 * @return
	 */
	Determinacion getGrupoEntidad(int idEntidad);
	/**
	 * 
	 * @param idDeterminacion
	 * @return
	 */
	Determinaciongrupoentidad[] getGruposAplicacion(int idDeterminacion);
	/**
	 * 
	 * @param idDocumento
	 * @return
	 */
	Documentoshp[] getHojas(int idDocumento);
	/**
	 * 
	 * @return
	 */
	Instrumentoplan[] getInstrumentosPlan();
	/**
	 * 
	 * @param id
	 * @param idInstrumento
	 * @param tipo
	 * @return
	 */
	int getNumeroPlanesHijos(Integer id, Integer idInstrumento, ModalidadPlanes tipo);
	/**
	 * 
	 * @param identificador
	 * @return
	 */
	Opciondeterminacion[] getOpcionesDeterminacion(int identificador);
	/**
	 * 
	 * @param idDeterminacion
	 * @return
	 */
	Operaciondeterminacion[] getOperacionesPorDeterminacionOperada(int idDeterminacion);
	/**
	 * 
	 * @param idDeterminacion
	 * @return
	 */
	Operaciondeterminacion[] getOperacionesPorDeterminacionOperadora(int idDeterminacion);
	/**
	 * 
	 * @param idEntidad
	 * @return
	 */
	Operacionentidad[] getOperacionesPorEntidadOperada(int idEntidad);
	/**
	 * 
	 * @param idEntidad
	 * @return
	 */
	Operacionentidad[] getOperacionesPorEntidadOperadora(int idEntidad);
	/**
	 * 
	 * @param idInstrumento
	 * @return
	 */
	Instrumentotipooperacionplan[] getOperacionesPorInstrumento(int idInstrumento);
	/**
	 * 
	 * @param ambito
	 * @param tipo
	 * @return
	 */
	Plan[] getPlanes(Integer ambito, ModalidadPlanes tipo);
	/**
	 * 
	 * @return
	 */
	Plan[] getPlanesBase();
	/**
	 * 
	 * @param id
	 * @param idInstrumento
	 * @param tipo
	 * @return
	 */
	Plan[] getPlanesHijos(Integer id, Integer idInstrumento, ModalidadPlanes tipo);
	/**
	 * 
	 * @param identificador
	 * @return
	 */
	Operacionplan[] getPlanesOperados(Integer identificador);
	/**
	 * 
	 * @param ambito
	 * @param tipo
	 * @return
	 */
	Plan[] getPlanesRaiz(Integer ambito, ModalidadPlanes tipo);
	/**
	 * 
	 * @param idPlan
	 * @return
	 */
	String[] getPoligonosPlan(int idPlan);
	/**
	 * 
	 * @param idTramite
	 * @return
	 */
	String getPoligonoTramite(int idTramite);
	/**
	 * 
	 * @param idEntidad
	 * @return
	 */
	PropiedadesAdscripcion[] getPropiedadesAdscripcionEntidad(int idEntidad);
	/**
	 * 
	 * @param idTramite
	 * @return
	 */
	PropiedadesAdscripcion[] getPropiedadesAdscripcionTramite(int idTramite);
	/**
	 * 
	 * @param idUnidad
	 * @return
	 */
	PropiedadesUnidad getPropiedadesUnidad(int idUnidad);
	/**
	 * 
	 * @param idCaso
	 * @return
	 */
	Entidaddeterminacionregimen[] getRegimenDeCaso(int idCaso);
	/**
	 * 
	 * @param identificador
	 * @return
	 */
	Regimenespecifico[] getRegimenesEspecificos(int identificador);
	/**
	 * 
	 * @param identificador
	 * @return
	 */
	Regimenespecifico[] getRegimenesEspecificosHijos(int identificador);
	/**
	 * 
	 * @param identificador
	 * @return
	 */
	RegulacionEspecifica[] getRegulacionesEspecificas(int identificador);
	
	/**
	 * 
	 * @param codigo
	 * @return
	 */
	Tramite getTramitePorCodigo(String codigo);
	/**
	 * 
	 * @param idPlan
	 * @param tipo
	 * @return
	 */
	Tramite[] getTramitesPorPlan(int idPlan, ModalidadPlanes tipo);
	/**
	 * 
	 * @param idDeterminacion
	 * @return
	 */
	Determinacion getUnidad(int idDeterminacion);
	/**
	 * 
	 * @param datos
	 */
	void guardar(Object datos);
	/**
	 * 
	 * @param idTramite
	 * @return
	 * @throws ExcepcionPlaneamiento
	 */
	boolean isTramiteRefundible(Integer idTramite) throws ExcepcionPlaneamiento;
}

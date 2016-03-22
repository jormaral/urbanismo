/*
* Copyright 2011 red.es
* Autores: IDOM Consulting
*
* Licencia con arreglo a la EUPL, Versiï¿½n 1.1 o -en cuanto
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

package com.mitc.redes.editorfip.servicios.basicos.fip.determinaciones;

import java.util.List;

import javax.ejb.Local;
import javax.faces.model.SelectItem;

import com.mitc.redes.editorfip.entidades.busqueda.BusquedaDeterminacionDTO;
import com.mitc.redes.editorfip.entidades.busqueda.FiltrosDTO;
import com.mitc.redes.editorfip.entidades.comunes.ParIdentificadorTexto;
import com.mitc.redes.editorfip.entidades.interfazgrafico.AdscripcionesTramiteDTO;
import com.mitc.redes.editorfip.entidades.interfazgrafico.DeterminacionDTO;
import com.mitc.redes.editorfip.entidades.interfazgrafico.DeterminacionReguladoraTramiteDTO;
import com.mitc.redes.editorfip.entidades.interfazgrafico.DocumentoDTO;
import com.mitc.redes.editorfip.entidades.interfazgrafico.GrupoAplicacionTramiteDTO;
import com.mitc.redes.editorfip.entidades.interfazgrafico.RegulacionEspecificaDTO;
import com.mitc.redes.editorfip.entidades.interfazgrafico.UnidadTramiteDTO;
import com.mitc.redes.editorfip.entidades.interfazgrafico.ValorReferenciaTramiteDTO;
import com.mitc.redes.editorfip.entidades.rpm.gestionfip.PlanesEncargados;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Determinacion;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Documento;
/**
 * Este servicio se encarga de la gestión de las determinaciones en el esquema planeamiento
 * 
 * @author fguerrero
 *
 */

@Local
public interface ServicioBasicoDeterminaciones
{
   
	// ------------------------------
    // ARBOL DETERMINACION
    // ------------------------------
	
	/**
	 * Metodo que devuelve aquellas determinaciones del tramite que no tienen padre
	 * @param idTramite identificador del tramite del que se quieren obtener las determinaciones raices
	 * @return listado con el identificador, nombre y si es hoja o no de cada determinacion raiz
	 */
	public List<ParIdentificadorTexto> getDeterminacionesRaices(int idTramite);

	/**
	 * Metodo que devuelve aquellas determinaciones hija de la determinación que se pasa como parámetro
	 * @param idDetPadre identificador de la determinación de la que se quieren conocer los hijos
	 * @return listado con el identificador, nombre y si es hoja o no de cada determinacion hija
	 */
	public List<ParIdentificadorTexto> getDeterminacionesHijasDeDet(
			int idDetPadre);
	
	// ------------------------------
    // GENERICAS
    // ------------------------------

	/**
	 * Metodo que devuelve el nombre del tipo de documento que se pasa como parametro
	 * @param id identificador de bd del diccionario.tipodocumento
	 * @return nombre (String) del tipo de documento
	 */
	public String tipoDocumentoNombre(int id);

	/**
	 * 
	 * Metodo que devuelve el nombre del grupo de documento que se pasa como parametro
	 * @param id identificador de bd del diccionario.tipodocumento
	 * @return nombre (String) del tipo de documento
	 */
	public String grupoDocumentoNombre(int id);

	/**
	 * Metodo que devuelve el nombre del caracter de una determinacion a partir del identificador de caracter que se pasa como parametro
	 * @param idCaracter identificador de bd del diccionario.caracterdeterminacion
	 * @return
	 */
	public String caracterString(int idCaracter);

	/**
	 * Metodo que devuelve un listado de objeto que encapsula el identificador y el nombre de los caracteres de las determinaciones que existen en el diccionario (diccionario.caracterdeterminacion)
	 * @return listado de objeto que encapsula el identificador(int) y el nombre (String)
	 */
	public List<Object[]> findCaracterTexto();

	/**
	 * Metodo que devuelve un listado de SelectItem con el id y el nombre de los caracter de una determinacion
	 * @return listado de SelectItem con el id y el nombre
	 */
	public List<SelectItem> findCaracterTextoSelectItem();

	/**
	 * Metodo que devuelve una determinacion a partir de su identificador de BD 
	 * @param id identificador de BD de la determinacion
	 * @return Determinacion (se devuelve tambien el nombre de la determinacion padre y base a la que este asociado para evitar lazy)
	 */
	public Determinacion buscarDeterminacion(Object id);
	
	/**
	 * Metodo que devuelve la determinación a partir del id del tramite y del orden global que se le pasa como parametro
	 * @param idTramite identificador del tramtie donde se quiere buscar
	 * @param apartado orden global de la determinacion, separado por puntos: ejemplo: 1.1.2.3
	 * @return identificador de la determinacion encontrada, o cero si no ha encontrado nada
	 */
	public int buscarDeterminacionPorTramiteYOrden (int idTramite, String apartado);
	
	/**
	 * Metodo que devuelve el orden global de una determinación que se pasa como parámetro
	 * @param idDeterminacion identificador de la determinacion que se pretende obtener el orden global
	 * @return orden global de la determinacion, separado por puntos: Ej: 01.01.12.03
	 */
	public String obtenerOrdenCompletoDeterminacion (int idDeterminacion);
	
	/**
	 * Metodo que devuelve el orden que le corresponde a una nueva determinacion que se quiera crear, a partir del tramite y de cual sea su determinacion padre
	 * @param idTramite identificador del tramite donde se va a crear la determinacion
	 * @param idDetPadre identificador de la determinacion padre 
	 * @return entero con el orden, o 1 si es la primera determinacion hija
	 */
	public int obtenerOrderNuevaDeterminacion(int idTramite, int idDetPadre);
	
	
	// ------------------------------
    // DETERMINACIONES BASE
    // ------------------------------
    
	/**
	 * Metodo que devuelve un listado de determinaciones base que pueden ser aplicable a la determinacion que se pasa como parametro.
	 * Para que sean aplicables, las determinaciones base deben ser del mismo tipo
	 * @param idDeterminacion identificador de la determinación de la que se quiere obtener las determinaciones base aplicables
	 * @param idTramiteBase identificador del tramite base donde buscar
	 * @return listado de determinaciones base aplicables
	 */
    public List<DeterminacionDTO> obtenerDeterminacionesBase (int idDeterminacion, int idTramiteBase);
    
	// ------------------------------
    // UNIDADES
    // ------------------------------

    /**
     * Metodo que devuelve la unidad de la determinacion que se le pasa como parametro
     * @param idDet identificador de la determinacion de la que se quiere obtener la unidad
	 * @return identificador(int) y nombre(String) de la determinacion unidad asociada a la determinacion o un objeto vacio en caso de no encontrarlo
     */
	public ParIdentificadorTexto obtenerUnidadDeDeterminacion(
			int idDet);

	/**
	 * Metodo que devuelve el plan al que pertenece la unidad de la determinacion que se le pasa como parametro
     * @param idDet identificador de la determinacion de la que se quiere obtener la unidad
	 * @return identificador(int) y nombre(String) del plan de la determinacion unidad asociada a la determinacion o un objeto vacio en caso de no encontrarlo
	 */
	public ParIdentificadorTexto obtenerPlandeUnidadDeDeterminacion(
			int idDet);

	/**
	 * Metodo que guarda la asociación de una unidad a una determinacion
	 * @param idDet identificador de la determinacion a la cual se le va a asignar una unidad
	 * @param idDetUnidad identificador de la determinacion unidad que va a ser asignada
	 * @return true si todo ha ido correcto, false si ha habido problemas
	 */
	public boolean guardarUnidadDeDeterminacion(int idDet,int idDetUnidad);

	/**
	 * Metodo que borra la asociación de una unidad asignada en una determinacion
	 * @param idDet identificador de la determinacion que se quiere borrar la asignacion de la unidad
	 * @return true si todo ha ido correcto, false si ha habido problemas
	 */
	public boolean borrarUnidadDeDeterminacion(int idDet);
	
	/**
	 * Metodo que devuelve una lista con todas las determinacion de caracter unidades de un tramite que se pasa como parametro
	 * @param idTramite identificador del tramite que queremos saber que unidades que tiene
	 * @return lista con todas las determinacion de caracter unidades
	 */
	public List<UnidadTramiteDTO> obtenerUnidadTramite (int idTramite);

	// ------------------------------
    // DOCUMENTOS
    // ------------------------------
	
	/**
	 * Metodo que devuelve una lista de documentos asociados a una determinacion que se pasa como parametro
	 * @param idDeterminacion identificador de la determinacion de la que se quieren obtener los documentos asociados
	 * @return lista con todos los documentos asociados a la determinacion o null si no hay ninguno
	 */
	public List<Documento> obtenerListaDocumentosDeterminacion(
			String idDeterminacion);

	
	/**
	 * Metodo que devuelve un array de los Documentos a mostrar asociados a una determinacion
	 * @param idDeterminacion identificador de la determinacion de la que se quieren obtener los documentos asociados
	 * @return array con todos los documentos asociados a la determinacion o null si no hay ninguno
	 */
	public DocumentoDTO[] obtenerArrayDocumentoDeterminacionDTO(
			String idDeterminacion);
	
	
	
	
	// ------------------------------
    // GRUPOS APLICACION
    // ------------------------------
	
	/**
	 * Metodo que devuelve un array con las determinaciones de grupode aplicación asociados a una determinacion
	 * @param idDetTrabajo identificador de la determinacion de la que se quieren obtener los grupos de aplicacion asociados
	 * @return array de determinaciones grupos de aplicacion asociadas
	 */
	public Determinacion[] getArrayGruposAplicacion(int idDetTrabajo);
	
	/**
	 * Metodo que elimina una asociacion de un grupo de aplicacion en una determinacion
	 * @param idDeterminacion identificador de la determinacion de la que se quiere borrar la relacion
	 * @param idGrupoAplicacion identificador del grupo de aplicación asociado que se quiere eliminar
	 */
	public void borrarRelacionDeterminacionConGrupoAplicacion(int idDeterminacion, int idGrupoAplicacion);
	
	/**
	 * Metodo que guarda una asociacion de grupo de aplicacion entre una determinacion y el grupo aplicado
	 * @param idDeterminacion identificador de la determinacion en la que se quiere guardar la relacion
	 * @param idGrupoAplicacion identificador del grupo de aplicacion que se quiere asociar
	 */
	public void guardarGrupoAplicacion(int idDeterminacion, int idGrupoAplicacion);
	
	/**
	 * Metodo que permite obtener los grupos de aplicación de un tramite (los valores de referencia de una determinacion de caracter 'grupo de entidades')
	 * @param idTramite identificador del tramite 
	 * @return lista de grupos de aplicacion
	 */
	public List<GrupoAplicacionTramiteDTO> obtenerGruposAplicacionTramite (int idTramite);
	
	// ------------------------------
    // VALORES REFERENCIA
    // ------------------------------
    /**
     * Metodo que guarda todos los hijos que sean valor de referencia como valores de referencia de la determinacion que se pasa como parametro
     */
	public boolean guardarHijosComoValorReferencia (int idDeterminacion);
	 
	
	public List<ValorReferenciaTramiteDTO> obtenerValoresReferenciaDeterminacion (int idDeterminacion);
	
	/**
	 * Metodo que devuelve un array con las determinaciones de valores de referencia asociados a una determinacion
	 * @param idDeterminacion identificador de la determinacion de la que se quieren obtener los valores de referencia asociados
	 * @return array de determinaciones valroes de referencia asociadas
	 */
    public Determinacion[] getArrayValoresReferencia(int idDeterminacion);
    
    /**
     * Metodo que elimina todas las asociaciones de valor de referencia en una determinacion
	 * @param idDeterminacion identificador de la determinacion de la que se quieren borrar todas las relaciones
     */
    public void borrarTodasRelacionesDeterminacionConValorReferencia(int idDeterminacion);
    
    /**
     * Metodo que elimina una asociacion de un valor de referencia en una determinacion
	 * @param idDeterminacion identificador de la determinacion de la que se quiere borrar la relacion
	 * @param idValorReferencia identificador del valor de referencia asociado que se quiere eliminar
     */
    public boolean borrarRelacionDeterminacionConValorReferencia(int idDeterminacion, int idValorReferencia);
    
    /**
     * Metodo que obtiene todas las determinaciones de caracter valor de referencia de un tramite
     * @param idTramite identificador del tramite que se quieren obtener los valores de referencia
     * @return listado de valores de referencia
     */
    public List<ValorReferenciaTramiteDTO> obtenerValoresReferenciaTramite (int idTramite);
    
    /**
     * Metodo que devuelve los valores de referencia que se pueden aplicar a la determinacion que se pasa como parametro, 
     * excepto los que ya esten aplicados a dicha determinacion
     * @param idTramite identificador del tramite
     * @param idDeterminacion identificador de la determinacion
     * @return
     */
    public List<ValorReferenciaTramiteDTO> obtenerValoresReferenciaTramiteYDeterminacion (int idTramite, int idDeterminacion);
    
    /**
     * Metodo que permite guardar un listado de asociaciones de valores de referencia a una determinacion
     * @param idDeterminacion identificador de la determinacion a la que se le van a asociar los valores de referencia
     * @param idValoresReferencia listado de identificadores de valores de referencia a asociar
     */
    public void guardarValoresReferencia(int idDeterminacion, List<Integer> idValoresReferencia);
    
    /**
     * 
     * Metodo que devuelve un listado con las determinaciones de valores de referencia asociados a una determinacion
	 * @param idDeterminacion identificador de la determinacion de la que se quieren obtener los valores de referencia asociados
	 * @return listado de determinaciones valores de referencia asociadas
     */
    public List<DeterminacionDTO> getListValoresReferenciaDeDeterminacion(int idDeterminacion);
    
    // ------------------------------
    // DETERMINACION REGULADORA
    // ------------------------------
    
    /**
     * Metodo que devuelve un array con las determinaciones reguladoras asociados a una determinacion
	 * @param idDeterminacion identificador de la determinacion de la que se quieren obtener las determinaciones reguladoras asociados
	 * @return array de determinaciones reguladoras asociadas

     */
    public Determinacion[] getArrayReguladoras(int idDeterminacion);
    
    /**
     * Metodo que permite guardar una asociacion de una determinacion reguladora en una determinaicon
     * @param idDetContenedora identificador de la determinacion a la que se le va a asociar la determinacion reguladora
     * @param idDetReguladora identificador de la determinacion reguladora a asociar
     */
    public void guardarDeterminacionReguladoraDeterminacion(int idDetContenedora, int idDetReguladora);
    
    /**
     * Metodo que permite borrar una asociacion de una determinacion reguladora en una determinaicon
     * @param idDetContenedora identificador de la determinacion a la que se le va a borrar la asociacion a la determinacion reguladora
     * @param idDetReguladora identificador de la determinacion reguladora a borrar la asociacion
     */
    public void borrarDeterminacionReguladoraDeterminacion(int idDetContenedora, int idDetReguladora);
    
    /**
     * Metodo que devuelve todas las determinaciones de caracter reguladora de un tramite
     * @param idTramite identificador del tramite
     * @return listado de determinaciones reguladoras
     */
    public List<DeterminacionReguladoraTramiteDTO> obtenerDeterminacionesReguladorasTramite (int idTramite);
    
   // public List<DeterminacionReguladoraTramiteDTO> obtenerDeterminacionesReguladorasTramiteYDeterminacion (int idTramite, int idDeterminacion);
    
    
    // ------------------------------------
    // ADSCRIPCIONES
    // ------------------------------------
    public List<AdscripcionesTramiteDTO> obtenerDeterminacionesAdscripcionesTramite (int idTramite);
  
    // ------------------------------
    // REGULACIONES ESPECIFICAS
    // ------------------------------
   
    public int crearRegulacionespecifica (RegulacionEspecificaDTO regEspec, int idDeterminacion);
    
    public boolean editarRegulacionespecifica (RegulacionEspecificaDTO regEspec);
    
    public RegulacionEspecificaDTO buscarRegulacionEspecifica(int idRelacion);
    
    public boolean borrarRegulacionespecifica (int idRelacion);
    
    public List<ParIdentificadorTexto> getRegulacionespecificaRaices(int idDeterminacion);
   
    public List<ParIdentificadorTexto> getRegulacionespecificaHijas(int idREPadre);
    
    // -----------------------------------
    // Determinacion Regimen
    // -----------------------------------
    public List<DeterminacionDTO> getListDeterminacionRegimenDeTramite(int idTramite);
        
    
    // ------------------------------
    // Entidades a las que esta aplicada
    // ------------------------------
    
    public List<ParIdentificadorTexto> obtenerEntidadesDondeSeAplicaDeterminacion(int idDeterminacion);
    
    public Determinacion findByBaseGrupo(int idTramiteBase, String grupo);
    public boolean tieneDeterminacionTramite(int idTramite);
    
    public int getIdentificadorCaracter(String literal);
    public Determinacion obtenerEquivalente(String nombre, int idTramiteBase);
    
    public void borrarDeterminacionesTramite(int idTramite);
    
    public String nextCodigo(String idBDTramite);
    public Determinacion findByNombreTramite(int idTramiteBase, String nombre);
    
    public void removeByDeterminacion(int idDeterminacion);
    public boolean crearValorRefenciaDeterminacionPadre (Determinacion hijo, int idPadre);
    
    // ------------------------------
    // BUSQUEDA
    // ------------------------------
    public List<BusquedaDeterminacionDTO> resultadosBusquedaAvanzadaDeterminacion(FiltrosDTO filtros, int idTramite);
    
    // ------------------------------
    // GESTION DOCUMENTAL
    // ------------------------------

	/**
     * Método que devuelve las determinaciones asociadas a un documento
     * @param idDoc
     * @return listado de determinacionDTO
     */
     
    public List<DeterminacionDTO> obtenerDeterminacionesDocumento(int idDoc);
    public void borrarRelacionDeterminacionConDocumento(int idDeterminacion, int idDoc);
    
    public void crearDeterminacionesUnidades (PlanesEncargados planEncargado);
    public UnidadTramiteDTO obtenerUnidadNoProcedeTramite (int idTramite);
    
    public boolean tieneUnidadDeDeterminacion (int idDetContenedora);
    
    public Determinacion obtenerEquivalenteOrden(String orden, String nombre, int idTramiteBase);
    
    public Determinacion buscarDeterminacionBase(int idDeterminacion);
    
    public Determinacion getValorReferenciaDeDeterminacion(int idDeterminacion, String nombre);
    
    public boolean esValorReferenciaDeDeterminacion(int idDeterminacion, int idDetValorRef);
    
    public boolean determinacionAplicadaPorGrupo(int idDetTrabajo, int idDetGrupo);
}

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

package com.mitc.redes.editorfip.servicios.basicos.diccionario;

import java.util.List;

import javax.ejb.Local;
import javax.faces.model.SelectItem;

import com.mitc.redes.editorfip.entidades.comunes.ParIdentificadorTexto;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Plan;

/**
 * Este servicio se encarga de la gestión básica de los planes, tanto para los esquemas de gestionfip como planeamiento
 * @author fguerrero
 *
 */
@Local
public interface ServicioBasicoPlanes
{
    
	/**
	 * Contructor por defecto
	 */
    public void servicioBasicoPlanes();
    
    /**
     * Metodo que devuelve una lista de id y nombre de los planes base de la tabla gestionfip.planesbase
     * @return lista de id(int) y nombre(String) encapsulada en un array de Object
     */
    public List<Object[]> findPlanesBase();
    
    public List<Object[]> findPlanesBaseInicial();
    
    /**
     * Metodo que devuelve una lista con los planes vigentes de un ambito que se pasa como parametro
     * @param iden idAmbito del ambito que se quiere conocer los planes vigentes
     * @return lista de id(int) y nombre(String) encapsulada en un array de Object
     */
    public List<Object[]> obtenerPlanesVigentesDeAmbito(int iden);
    
    /**
     * Metodo que devuelve el primer plan que se encuentra en la tabla planeamiento.plan para el ambito que se pasa como parametro
     * @param iden idAmbito del ambito
     * @return Primer plan que encuentra o null si no encuentra ninguno
     */
    public Plan obtenerPlanDeAmbito(int iden);
    
    /**
     * Metodo que devuelve un listado con el identificador y el nombre de los tipos de tramites que existen en el diccionario (diccionario.tipotramite)
     * @return listado con el identificador y el nombre
     */
    public List<ParIdentificadorTexto> obtenerListaTipoTramites();
    
    /**
     * Metodo que devuelve un listado con el identificador y el nombre de los instrumento del plan que existen en el diccionario (diccionario.instrumentoplan)
     * @return
     */
    public List<ParIdentificadorTexto> obtenerListaInstrumetos();
    
    /**
     * Metodo que devuelve un listado con el identificador y el nombre de los tipo de operacion de planes que existen en el diccionario (diccionario.tipooperacionplan)
     * @return
     */
    public List<ParIdentificadorTexto> obtenerListaTipoOperacionPlan();
    
    /**
     * Metodo que devuelve un listado de SelectItem con el id y el nombre de los tipo de operacion de planes
	 * @return listado de SelectItem con el id y el nombre
     */
    public List<SelectItem> obtenerListaTipoOperacionPlanSelectItem();
    
    /**
     * Metodo que devuelve un listado de objeto que encapsula el identificador y el nombre de los instrumento del plan que existen en el diccionario (diccionario.instrumentoplan)
     * @return listado de objeto que encapsula el identificador(int) y el nombre (String)
     */
    public List<Object[]> obtenerInstrumetos();
    
    /**
     * Metodo que devuelve un listado de objeto que encapsula el identificador y el nombre de los tipos de tramites que existen en el diccionario (diccionario.tipotramite)
     * @return listado de objeto que encapsula el identificador(int) y el nombre (String)
     */
    public List<Object[]> obtenerTipoTramites();
    
    /**
     * Metodo que devuelve el nombre del tipo de tramite a partir del identificador que se le pasa como parametro
     * @param id identificador de Bd del tipo de tramite
     * @return nombre del tramite
     */
    public String tipoTramiteString(int id);
    
    /**
     * Metodo que devuelve el nombre del instrumento a partir del identificador que se le pasa como parametro
     * @param id identificador de Bd del instrumento
     * @return nombre del instrumento
     */
    public String instrumentoString(int id);
    
    /**
     * Metodo que devuelve el tipo de operacion del plan a partir del identificador que se le pasa como parametro
     * @param id identificador de Bd del tipooperacionplan
     * @return nombre del tipooperacionplan
     */
    public String tipoOperacionPlanString(int id);

}

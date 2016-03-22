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

import com.mitc.redes.editorfip.entidades.comunes.ParIdentificadorTexto;

/**
 * Este servicio se encarga de la gestión de los ámbitos en el esquema diccionario
 * @author fguerrero
 *
 */
@Local
public interface ServicioBasicoAmbitos
{
    /**
     * Constructor por defecto
     */
    public void servicioBasicoAmbitos();

    /**
     * Metodo que devuelve el nombre del ambito a partir del id
     * 
     * @param idAmbito 
     * @return
     */
    public String ambitoString(int idAmbito);
    
    /**
     * Metodo que devuelve un listado con los idAmbito y el nombre del Ambito ordenados por el nombre de todos los municipios del diccionario
     * 
     * @return Listado con idAmbito y nombreAmbito
     */
    public List<ParIdentificadorTexto> findAmbitos();
    
    /**
     * Metodo que devuelve un listado con los idAmbito y el nombre del Ambito de aquellos Ambitos que en el OrganigramaAmbito no tienen padre (Comunidades Autonomas)
     * @return Listado con los idAmbito y el nombre del Ambito
     */
    public List<ParIdentificadorTexto> obtenerAmbitosRaices();
    
    /**
     * Metodo que devuelve un listado con los hijos del ambito que se pasa como parametro, a traves de la tabla de organigrama ambito
     * @param idAmbitoPadre id del ambito del cual se quieren conocer sus hijos
     * @return
     */
    public List<ParIdentificadorTexto> obtenerAmbitosHijos(int idAmbitoPadre);
    
    public List<ParIdentificadorTexto> listadoAmbitosShp();
    
    public void borrarGeomtriaAmbitosShp(int idAmbito) ;
    
    public void guardarGeomtriaAmbitosShp(int idAmbito, String wKTOrigen);
    
    public boolean tieneGeometriaAmbitoShp (int idAmbito);
    
    public int establecerGeometriaEntidadComoAmbitoShp (int idEntidad);
    
    public int cargarAmbitoShpConGeometriaAmbitoTramite (int idTramite);
    
    public int cargarAmbitoShpConGeometriaAmbitoPV (int idAmbito);
    
    
}

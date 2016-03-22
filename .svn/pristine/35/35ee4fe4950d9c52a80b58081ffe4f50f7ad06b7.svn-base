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

package com.mitc.redes.editorfip.servicios.basicos.fip.tramites;

import java.util.List;

import javax.ejb.Local;

import com.mitc.redes.editorfip.entidades.busqueda.BusquedaTramiteDTO;
import com.mitc.redes.editorfip.entidades.busqueda.FiltrosDTO;
import com.mitc.redes.editorfip.entidades.comunes.ParIdentificadorTexto;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Plan;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Tramite;

/**
 * Servicio con operaciones basicas sobre trámites
 * 
 * @author fguerrero
 *
 */
@Local
public interface ServicioBasicoTramites {
	
	public String nombreTramite(int idTramite);
	
	/**
	 * Obtiene la lista de trámites raices que sera utiliza en el arbol de 
	 * seleccion de trámite.
	 * 
	 * @return lista con objetos de tipo Trámite.
	 */
	public List<Tramite> obtenerTramitesRaizes();
	
	/**
	 * Obtiene la lista de tramites raices para el arbol de asignacion de 
	 * tramites a usuarios.
	 * 
	 * @return lista con objetos de tipo ParIdentificadorTexto.
	 */
	public List<ParIdentificadorTexto> getArbolAsignacionTramiteUsuarioRaices();
	
	/**
	 * Obtiene la lista de tramites hijos para el arbol de asignacion de 
	 * tramites a usuarios.
	 * 
	 * @param idAmbito ID del trámite padre.
	 * @return lista con objetos de tipo ParIdentificadorTexto.
	 */
	public List<ParIdentificadorTexto> getArbolAsignacionTramiteUsuarioHijos(int idAmbito);
	
	/**
	 * Realiza una busqueda filtrada y devuelve los resultados en formato de lista.
	 * 
	 * @param filtros objeto con los filtros a aplicar sobre la lista
	 * @return lista con objetos de tipo BusquedaOperacionDTO.
	 */
	public List<BusquedaTramiteDTO> resultadosBusquedaAvanzadaTramite(FiltrosDTO filtros);
	
	public boolean existeTramiteDeCodigoFip(String codigo);
	
	public Plan obtenerPlanDeCodigoFip(String codigo);
	
	public int idTramiteDeDeterminacion (int idDeterminacion);
}

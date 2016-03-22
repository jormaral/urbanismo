/*
* Copyright 2011 red.es
* Autores: Arnaiz Consultores.
*
** Licencia con arreglo a la EUPL, Versi√≥n 1.1 o -en cuanto
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
package com.mitc.redes.editorfip.servicios.gestionfip.gestionprerefundido;

import java.util.List;

import javax.ejb.Local;

import com.mitc.redes.editorfip.entidades.comunes.ParIdentificadorTexto;

/**
 * Servicio que gestiona las operaciones referentes al Refundido.
 * 
 * @author fguerrero
 *
 */
@Local
public interface Refundido {
	

	/**
	 * MÈtodo que se encarga de refundir una lista de tramites.
	 * 
	 * @param listaIdTramites ID's de los tr·mites a refundir.
	 * @param usuario usuario el cual realiza el refundido.
	 * @return Lista de tr·mites los cuales estan en proceso de refundido.
	 * @throws Exception
	 */
    public ParIdentificadorTexto refundirTramites (List listaIdTramites, String usuario)throws Exception;
    
    /**
	 * MÈtodo que se encarga de refundir una lista de tramites.
	 * 
	 * @param listaIdTramites ID's de los tr·mites a refundir.
	 * @param usuario usuario el cual realiza el refundido.
	 * @param bCrearTramite indicador para crear tr·mite.
	 * @return Lista de tr·mites los cuales estan en proceso de refundido.
	 * @throws Exception
	 */
    public ParIdentificadorTexto refundirTramites (List listaIdTramites, String usuario, boolean bCrearTramite)throws Exception;
    /**
     * Obtiene el valor de la propiedad 'nombreAmbitoRefundido'
     * 
     * @return valor de tipo String
     */
    public String getNombreAmbitoRefundido();
}


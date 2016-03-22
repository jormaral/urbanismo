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


package com.mitc.redes.editorfip.servicios.basicos.comunes;

import java.util.List;

import javax.ejb.Local;

import com.mitc.redes.editorfip.entidades.rpm.gestionfip.TokenUtils;

/**
 * 
 * Servicio basico que contiene metodos comunes a la apliación.
 * 
 * @author fguerrero
 *
 */
@Local
public interface ServicioBasicoGeneral
{
    /**
     * Constructor por defecto.
     */
    public void servicioBasicoGeneral();
    
    /**
     * Obtiene todos los planes base existentes en BD,
     * 
     * @return lista de planes en formato Object[]
     */
    public List<Object[]> findPlanesBase();
    
    /**
     * Obtiene un objeto del tipo TokenUtil mediante su ID. Estos 
     * objetos son utilizados para determinar estados de procesos
     * ejecutados de forma asíncrona.
     * 
     * @param iden Identificador del toke.
     * @return Objeto del tipo TokenUtil asociado a dicho identificador.
     */
    public TokenUtils obtenerTokenUtilPorNombre(String iden);
    
    public String obtenerListaVistasPlaneamientoPorAmbito();
    
}

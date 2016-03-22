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

/**
 * 
 * Servocio que traduce e interpreta consultas de EJB3 a
 * DB (SQL) y viceversa.
 * 
 * @author fguerrero
 *
 */
@Local
public interface ServicioConsultasEJB3aBD
{
    /**
     * Constructor por defecto.
     */
    public void servicioConsultasEJB3aBD();

    /**
     * Crea una nueva consulta de interpretacion EJB3-BD.
     */
    public void consultaEJB3BD ();
    
    /**
     * Interpreta, traduce y ejecuta una consulta EJB3, 
     * devolviendo una String que contiene un mensaje con 
     * el resultado de dicha operación.
     * 
     * @return resulta de la operación.
     */
    public String consultaEJB3BDString ();
    
    /**
     * Obtiene el valor de la propiedad que guarda el 
     * resultado de la operación.
     * 
     * @return String con el resultado de la operación.
     */
    public String getRespuesta();
    
    /**
     * Modifica el valor de la propiedad que guarda el 
     * resultado de la operación.
     * 
     * @param respuesta 
     */
    public void setRespuesta(String respuesta);
    
    /**
     * Ejecuta una consulta EJB3.
     * 
     * @param query consulta a ejecutar.
     * @return lista de objetos referentes a los valores que se 
     * desea obtener al ejecutar la consulta.
     * @throws Exception
     */
    public List<Object> executeQueryEJB3(String query) throws Exception;
    
    /**
     * Obtiene el valor actual de la consulta.
     * 
     * @return
     */
    public String getValue();
    
    /**
     * Modifica el valor actual de la consulta.
     * 
     * @param value
     */
    public void setValue(String value);

}

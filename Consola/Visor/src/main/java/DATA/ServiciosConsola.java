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
package DATA;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ServiciosConsola {

    private HashMap hm = new HashMap();

    public ServiciosConsola() {

        //SERVICIOS WEB
        hm.put("CONSULTA_GRAFICA", "ServletForWebServices");
        hm.put("CONSULTA_GRAFICA_EXTENDIDA", "ServletForWebServices");
        hm.put("CREAR_WMS", "ServletForWebServices");
        hm.put("GET_GEOPOS_INV_CATASTRO", "ServletForWebServices");
        hm.put("GET_VERSION", "ServletForWebServices");
        hm.put("GET_LEYENDA", "ServletForWebServices");
        hm.put("GET_ID_AMBITO", "ServletForWebServices");
        //BUSQUEDAS
        hm.put("GET_PROVINCIAS", "ServletForBusquedas");
        hm.put("GET_MUNICIPIOS_SEGUN_PROVINCIA", "ServletForBusquedas");
        hm.put("BUSCAR_ENTIDADES", "ServletForBusquedas");
        hm.put("BUSCAR_PLANES", "ServletForBusquedas");
        hm.put("GET_CATASTRO_POR_DIRECCION", "ServletForBusquedas");
        hm.put("GET_VIAS_POR_NOMBRE","ServletForBusquedas");
        hm.put("GET_CATASTRO_POR_REFERENCIA", "ServletForBusquedas");
        hm.put("GET_CATASTRO_POR_POL_PARC", "ServletForBusquedas");
        hm.put("GET_AMBITOS", "ServletForBusquedas");
        hm.put("UBICAR_ENTIDAD", "ServletForBusquedas");
        hm.put("UBICAR_PLAN", "ServletForBusquedas");
        hm.put("UBICAR_AMBITO", "ServletForBusquedas");
        hm.put("BUSCAR_ENTIDADES_SEGUN_PLAN", "ServletForBusquedas");
        hm.put("GET_GEOMETRIA_DE_ENTIDAD", "ServletForBusquedas");
        hm.put("GET_GEOMETRIA_DE_PLAN", "ServletForBusquedas");
        //SERVICIOS GENERALES
        hm.put("CROSS-DOMAIN", "ServletForGenericServices");
        hm.put("REMOVE_FILE_FROM_SERVER", "ServletForGenericServices");
        hm.put("EXPORT_VECTOR", "ServletForGenericServices");
        //CONFIGURACION
        /*hm.put("GET_LAYERCONFIG_DE_AMBITO", "ServletForConfig");
        hm.put("SAVE_LAYERCONFIG_DE_AMBITO", "ServletForConfig");*/
    }

    public String getServive(String servicio) {
        return (String) hm.get(servicio);
    }
}

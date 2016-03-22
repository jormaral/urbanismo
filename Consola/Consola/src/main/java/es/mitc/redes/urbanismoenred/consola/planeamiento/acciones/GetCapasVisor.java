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
package es.mitc.redes.urbanismoenred.consola.planeamiento.acciones;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;

import es.mitc.redes.urbanismoenred.consola.planeamiento.IAccion;
import es.mitc.redes.urbanismoenred.utils.recursos.textos.Textos;

/**
 * @author Arnaiz Consultores
 *
 */
@Stateless(name="GET_CAPAS_WMS")
public class GetCapasVisor implements IAccion {
	
	private final static Logger log = Logger.getLogger(GetCapasVisor.class);

	/* (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.consola.planeamiento.IAccion#ejecutar(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public void ejecutar(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String WMS = Textos.getTexto("visorConsola", "WMS");//Si no encuentra el properties devuelve vacio
        Map<String, Object> data = new HashMap<String, Object>();
        response.setHeader("Content-type", "application-json");
        List<Object> listaCapas = new ArrayList<Object>();//Catastro, Cartociudad...
        if (!WMS.equalsIgnoreCase("")) {
            String[] Capas = WMS.split(",");
            //Recorrer todas las Capas del properties
            for (int i = 0; i < Capas.length; i++) {
                String Capa = Textos.getTexto("visorConsola", "WMS." + Capas[i]);
                String Atributos[] = Capa.split(",");
                Map<String, String> mapAtributos = new HashMap<String, String>();
                mapAtributos.put("nombre", Capas[i]);//se añade el nombre de la capa como atributo
                //Recorrer todos los atributos de cada capa del properties
                for (int j = 0; j < Atributos.length; j++) {
                    String Atributo = Atributos[j];
                    String Valor = Textos.getTexto("visorConsola", "WMS." + Capas[i] + "." + Atributo);
                    mapAtributos.put(Atributo, Valor);
                }
                listaCapas.add(mapAtributos);
            }
        } else {
            log.warn("Error.No existe properties de WMS");
        }
        // Write JSON data
        data.put("data", listaCapas);
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getWriter(), data);
	}

}

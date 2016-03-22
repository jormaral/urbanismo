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

import javax.ejb.Stateless;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;

import es.mitc.redes.urbanismoenred.consola.planeamiento.IAccion;
import es.mitc.redes.urbanismoenred.consola.util.helpers.IJsonHelper;


/**
 * @author Arnaiz Consultores
 *
 */
@Stateless(name="GET_DATA")
public class GetDatos implements IAccion {
	
	/* (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.consola.planeamiento.IAccion#ejecutar(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public void ejecutar(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		ObjectMapper om = new ObjectMapper();
		
		String id = request.getParameter("ID");
		String tipo = request.getParameter("TIPO");
		String idioma = request.getParameter("idioma");
		
        if (tipo != null) {
        	
        	InitialContext icontext;
    		try {
    			icontext = new InitialContext();
    			
    			IJsonHelper helper = (IJsonHelper)icontext.lookup("java:global/UrbanismoEnRedV2/Urbanismoenred-ConsolaV2/"+tipo+"!es.mitc.redes.urbanismoenred.consola.util.helpers.IJsonHelper");
    			
    			response.setContentType("application-json;charset=UTF-8");
        		om.writeValue(response.getWriter(), helper.marshal(id != null ? Integer.parseInt(id):null, idioma != null? idioma : "es"));
    			
    	    } catch (NamingException e) {
    	    	response.getWriter().print("No se ha podido obtener información. Causa: " + e.getMessage());
    		}
        	
        } else {
        	response.getWriter().print("Solicitud incompleta");
        }
	}

}

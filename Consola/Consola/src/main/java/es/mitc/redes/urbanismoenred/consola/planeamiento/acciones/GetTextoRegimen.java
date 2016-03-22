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
import java.util.HashMap;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;

import es.mitc.redes.urbanismoenred.consola.planeamiento.IAccion;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidaddeterminacionregimen;
import es.mitc.redes.urbanismoenred.servicios.planeamiento.ServicioPlaneamientoLocal;

/**
 * @author Arnaiz Consultores
 *
 */
@Stateless(name ="GET_TEXTO_REGDIR")
public class GetTextoRegimen implements IAccion {
	
	@EJB
	private ServicioPlaneamientoLocal servicioPlaneamiento;

	/* (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.consola.planeamiento.IAccion#ejecutar(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public void ejecutar(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String idEdr = request.getParameter("idEdr");
		if (idEdr != null) {
			Entidaddeterminacionregimen edr = servicioPlaneamiento.get(Entidaddeterminacionregimen.class, idEdr);
			if ( edr != null) {
				Map<String, Object> data = new HashMap<String, Object>();
				data.put("id", edr.getIden());
				data.put("valor", edr.getValor());
				data.put("determinacion", edr.getDeterminacion() != null ? edr.getDeterminacion().getNombre() : "");

		        if (edr.getOpciondeterminacion() != null) {
		            data.put("opcion-determinacion", edr.getOpciondeterminacion().getDeterminacionByIddeterminacion() == null ? "" : edr.getOpciondeterminacion().getDeterminacionByIddeterminacion().getNombre());

		            data.put("opcion-valor", edr.getOpciondeterminacion().getDeterminacionByIddeterminacionvalorref() == null ? "" : edr.getOpciondeterminacion().getDeterminacionByIddeterminacionvalorref().getNombre());
		        } else {
		        	data.put("opcion-determinacion", "");
		            data.put("opcion-valor", "");
		        }

		        data.put("superposicion", edr.getSuperposicion());
		        
		        response.setHeader("Content-type", "application-json");
		        ObjectMapper mapper = new ObjectMapper();
	            mapper.writeValue(response.getWriter(), data);
			} else {
				response.getWriter().print("Entidad relacion " + idEdr + " desconocida.");
			}
			
		} else {
			response.getWriter().print("Solicitud incompleta");
		}

	}

}

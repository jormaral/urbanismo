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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;

import es.mitc.redes.urbanismoenred.consola.planeamiento.IAccion;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Tramite;
import es.mitc.redes.urbanismoenred.servicios.planeamiento.ExcepcionPlaneamiento;
import es.mitc.redes.urbanismoenred.servicios.planeamiento.ServicioPlaneamientoLocal;

/**
 * @author Arnaiz Consultores
 *
 */
@Stateless(name="ADD_TRAMITE")
public class CrearTramite implements IAccion {
	
	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

	@EJB
	private ServicioPlaneamientoLocal servicioPlaneamiento;

	/* (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.consola.planeamiento.IAccion#ejecutar(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public void ejecutar(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String idTipoTramite = request.getParameter("idTipoTramite");
		String texto = request.getParameter("texto");
        String comentario = request.getParameter("comentario");
        String idPlan = request.getParameter("idPlan");
        String nombre = request.getParameter("nombre");
        String idCentroProduccion = request.getParameter("idCentroProduccion");
        String organo = request.getParameter("organo");
        String sentido = request.getParameter("sentido");
        String fecha = request.getParameter("fecha");
        
        Map<String, Object> datos = new HashMap<String, Object>(); 
        
        response.setContentType("application-json;charset=UTF-8");
        
        if (idTipoTramite != null && idPlan != null && idCentroProduccion != null) {
        	
        	Tramite tramite;
			try {
				tramite = servicioPlaneamiento.crearTramite(Integer.parseInt(idTipoTramite), 
						Integer.parseInt(idPlan), 
						nombre, 
						texto, 
						comentario, 
						Integer.parseInt(idCentroProduccion),
						organo != null && !organo.isEmpty() ? Integer.parseInt(organo):null,
						sentido != null && !sentido.isEmpty() ? Integer.parseInt(sentido):null,
						(fecha != null && !fecha.isEmpty())? sdf.parse(fecha):null);
				
				datos.put("estado", Boolean.TRUE);
				datos.put("idTramite", tramite.getIden());
			} catch (NumberFormatException e) {
				datos.put("estado", Boolean.FALSE);
				datos.put("error", "Parámetros erróneos");
			} catch (ExcepcionPlaneamiento e) {
				datos.put("estado", Boolean.FALSE);
				datos.put("error", e.getMessage());
			} catch (ParseException e) {
				datos.put("estado", Boolean.FALSE);
				datos.put("error", "Fecha del trámite incorrecta.");
			}
        	
        } else {
        	datos.put("estado", Boolean.FALSE);
			datos.put("error", "Parámetros insuficientes");
        }
        
        ObjectMapper om = new ObjectMapper();
        om.writeValue(response.getWriter(), datos);
	}
}

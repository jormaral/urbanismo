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
import es.mitc.redes.urbanismoenred.servicios.planeamiento.ServicioPlaneamientoLocal;

/**
 * @author Arnaiz Consultores
 *
 */
@Stateless(name = "UPDATE_TRAMITE")
public class ModificarTramite implements IAccion {

	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

	@EJB
	private ServicioPlaneamientoLocal servicioPlaneamiento;

	/* (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.consola.planeamiento.IAccion#ejecutar(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public void ejecutar(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String idTramite = request.getParameter("idTramite");
        String organo = request.getParameter("organo");
        String sentido = request.getParameter("sentido");
        String fecha = request.getParameter("fecha");
        String nombre = request.getParameter("nombre");
        String comentario = request.getParameter("comentario");
        String texto = request.getParameter("texto");
        
        Map<String, Object> datos = new HashMap<String, Object>(); 
        
        response.setContentType("application-json;charset=UTF-8");
        
        if (idTramite != null) {
        	
        	Tramite tramite;
			try {
				tramite = servicioPlaneamiento.get(Tramite.class, Integer.parseInt(idTramite));
				
				if (tramite != null) {
					tramite.setNombre(nombre);
					tramite.setTexto(texto);
					tramite.setComentario(comentario);
					tramite.setIdorgano(organo != null && !organo.isEmpty() ? Integer.parseInt(organo):null);
					tramite.setIdsentido(sentido != null && !sentido.isEmpty() ? Integer.parseInt(sentido):null);
					tramite.setFecha((fecha != null && !fecha.isEmpty())? sdf.parse(fecha):null);
				} else {
					datos.put("estado", Boolean.FALSE);
					datos.put("error", "Trámite no encontrado.");
				}
				
				datos.put("estado", Boolean.TRUE);
				datos.put("idTramite", tramite.getIden());
			} catch (NumberFormatException e) {
				datos.put("estado", Boolean.FALSE);
				datos.put("error", "Parámetros erróneos");
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

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
package es.mitc.redes.urbanismoenred.consola.refundido.acciones;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;

import es.mitc.redes.urbanismoenred.data.rpm.diccionario.Ambito;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Archivo;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Proceso;
import es.mitc.redes.urbanismoenred.servicios.planeamiento.ServicioDiccionariosLocal;
import es.mitc.redes.urbanismoenred.servicios.refundido.ExcepcionRefundido;
import es.mitc.redes.urbanismoenred.servicios.refundido.GestorContextosRefundidoLocal;
import es.mitc.redes.urbanismoenred.servicios.seguridad.ServicioUsuariosLocal;
import es.mitc.redes.urbanismoenred.servicios.seguridad.Usuario;

/**
 * @author Arnaiz Consultores
 *
 */
@Stateless(name = "GET_PROCESOS_REFUNDIDO")
public class ObtenerProcesos implements IAccion {
	
	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	
	@EJB
	private GestorContextosRefundidoLocal gestorContextos;
	@EJB
	private ServicioDiccionariosLocal servicioDiccionario;
	@EJB
	private ServicioUsuariosLocal servicioUsuarios;

	/* (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.consola.refundido.acciones.IAccion#ejecutar(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public void ejecutar(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");

		ObjectMapper om = new ObjectMapper();
		
		try {
			Proceso[] procesos = gestorContextos.getProcesos(usuario);
			Map<String, Object> data = new HashMap<String, Object>();
			List<Map<String, Object>> lista = new ArrayList<Map<String, Object>>();
			Map<String, Object> procesoJson;
			for (int i = 0; i < procesos.length; i++) {
				procesoJson = new HashMap<String, Object>();
				procesoJson.put("idProceso", procesos[i].getIden());
				procesoJson.put("ambito", servicioDiccionario.getTraduccion(Ambito.class, procesos[i].getAmbito(), "es"));
				procesoJson.put("usuario", servicioUsuarios.getUsuario(procesos[i].getUsuario()).getNombre());
				procesoJson.put("inicio", sdf.format(procesos[i].getInicio()));
				if (procesos[i].getFin() != null) {
					procesoJson.put("fin", sdf.format(procesos[i].getFin()));
				}
				procesoJson.put("log", false);
				procesoJson.put("fip", false);
				for (Archivo documento : procesos[i].getArchivos()) {
					if (documento.getTipo().equals("LOG")) {
						procesoJson.put("log", true);
					} else {
						procesoJson.put("fip", true);
					}
				}
				lista.add(procesoJson);
			}
			data.put("data", lista);
			data.put("pages", 1);
			data.put("total", lista.size());
			response.setContentType("application-json;charset=UTF-8");
    		om.writeValue(response.getWriter(), data);
		} catch (ExcepcionRefundido e) {
			response.getWriter().print(e.getMessage());
		}
	}

}

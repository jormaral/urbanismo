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
package es.mitc.redes.urbanismoenred.consola.validacion.acciones;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;

import es.mitc.redes.urbanismoenred.consola.validacion.IAccion;
import es.mitc.redes.urbanismoenred.servicios.seguridad.Usuario;
import es.mitc.redes.urbanismoenred.servicios.validacion.ContextoValidacion;
import es.mitc.redes.urbanismoenred.servicios.validacion.GestorContextosValidacionLocal;

/**
 * @author Arnaiz Consultores
 *
 */
@Stateless( name = "GET_VALIDATION_STATUS")
public class ObtenerEstadoValidacion implements IAccion {
	
	@EJB
	private GestorContextosValidacionLocal gestorContextos;

	/* (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.consola.validacion.IAccion#ejecutar(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public void ejecutar(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		
		Usuario usuario = (Usuario)request.getSession().getAttribute("usuario");
		
		if (usuario != null) {
			ContextoValidacion[] contextos = gestorContextos.getContextos(usuario);
			// Transformamos los contextos en la siguiente estructura JSON
//			{
//				"[codigoFip1]": {"estado": "XXXX", "progreso" : "X%"}
//				"[codigoFip2]": {""}
//			}
			
			ObjectMapper mapper = new ObjectMapper();
			
			List<Map<String,Object>> contextosJson = new ArrayList<Map<String,Object>>();
			Map<String,Object> ctxtJson;
			Map<String,Object> estado;
			for (ContextoValidacion ctxt : contextos) {
				ctxtJson = new HashMap<String, Object>();
				ctxtJson.put("codigo", ctxt.getCodigoFip());
				estado = new HashMap<String, Object>();
				estado.put("codigo", ctxt.getEstado().getCodigo());
				estado.put("descripcion", ctxt.getEstado().getDescripcion());
				ctxtJson.put("estado", estado);
				switch (ctxt.getEstado()) {
					case GUARDADO:
					case VALIDADO:
					case VALIDACION_ERRONEA:
						ctxtJson.put("progreso","Finalizado.");
						break;
					default:
						if (ctxt.getParametro(ContextoValidacion.PROGRESO)!= null) {
							int progreso = Integer.parseInt(ctxt.getParametro(ContextoValidacion.PROGRESO).toString());
							if (progreso <= 0) {
								ctxtJson.put("progreso","No iniciado.");
							} else if (progreso >= 100) {
								ctxtJson.put("progreso","Finalizado.");
							} else {
								ctxtJson.put("progreso",progreso+"%");
							}
							
						} else {
							ctxtJson.put("progreso","-");
						}
						break;
				}
				
				if (ctxt.getParametro(ContextoValidacion.ERROR) != null) {
					ctxtJson.put("error", ctxt.getParametro(ContextoValidacion.ERROR));
				}
				
				contextosJson.add(ctxtJson);
			}
			
			mapper.writeValue(response.getWriter(), contextosJson);
		}
	}

}

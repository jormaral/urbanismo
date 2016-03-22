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

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;

import es.mitc.redes.urbanismoenred.consola.filtros.FileUploadListener;
import es.mitc.redes.urbanismoenred.consola.validacion.IAccion;
import es.mitc.redes.urbanismoenred.servicios.validacion.ContextoValidacion;
import es.mitc.redes.urbanismoenred.servicios.validacion.Estado;
import es.mitc.redes.urbanismoenred.servicios.validacion.GestorContextosValidacionLocal;

/**
 * 
 * @author Arnaiz Consultores
 *
 */
@Stateless ( name = "GET_UPLOAD_STATUS")
public class ObtenerProgresoSubida implements IAccion {
	
	private static final Logger log = Logger.getLogger(ObtenerProgresoSubida.class);
	
	@EJB
	private GestorContextosValidacionLocal gestorContextos;

	/* (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.consola.validacion.IAccion#ejecutar(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public void ejecutar(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		long bytesRead = 1;
		long contentLength = 0;
		long porcentaje;
		String tramite = request.getParameter("tramite");
		
		FileUploadListener listener;
		if (tramite != null) {
			ContextoValidacion ctxt = gestorContextos.getContexto(tramite);
			
			if (ctxt != null) {
				listener = (FileUploadListener) ctxt.getParametro("listener");
				
				if (listener == null) {
					listener = (FileUploadListener) request.getSession().getAttribute(FileUploadListener.NOMBRE_ATRIBUTO);
					ctxt.putParametro("listener", listener);
				} else {
					log.debug("El contexto ya dispone de listener.");
				}
				
				ObjectMapper mapper = new ObjectMapper();
				
				List<Map<String,Object>> contextosJson = new ArrayList<Map<String,Object>>();
				Map<String,Object> ctxtJson = new HashMap<String, Object>();
				
				ctxtJson.put("codigo", ctxt.getCodigoFip());
				
				if (listener != null) {
					bytesRead = listener.getBytesRead();
					contentLength = listener.getContentLength();
					log.debug("Tramite: " + tramite + " leido " + bytesRead + "/"+contentLength);
					//comprobacion para evitar la division por 0
	                if (contentLength != 0) {
	                    porcentaje = ((100 * bytesRead) / contentLength);
	                } else {
	                    porcentaje = 0;
	                }
	        
					if (porcentaje <= 0) {
						ctxt.putParametro(ContextoValidacion.ESTADO_INICIAL, ctxt.getEstado());
						ctxtJson.put("progreso", "No iniciado.");
						ctxt.setEstado(Estado.SUBIENDO);
					} else if (porcentaje >= 100) {
						if (ctxt.getEstado() != Estado.SUBIENDO) {
							ctxt.putParametro("listener",null);
		                	ctxtJson.put("finalizado", "true");
						}
						ctxtJson.put("progreso", "Finalizado.");
					} else {
						ctxtJson.put("progreso", ctxt.getParametro(ContextoValidacion.PROGRESO)+"%");
						ctxt.putParametro(ContextoValidacion.PROGRESO, porcentaje);
						if (ctxt.getEstado() != Estado.SUBIENDO) {
							ctxt.putParametro(ContextoValidacion.ESTADO_INICIAL, ctxt.getEstado());
							ctxt.setEstado(Estado.SUBIENDO);
						}
						
					}
					
				} else {
					log.warn("No se ha podido obtener el listener.");
				}
				Map<String,Object> estado = new HashMap<String, Object>();
				estado.put("codigo", ctxt.getEstado().getCodigo());
				estado.put("descripcion", ctxt.getEstado().getDescripcion());
				ctxtJson.put("estado", estado);
				
				contextosJson.add(ctxtJson);
                
				mapper.writeValue(response.getWriter(), contextosJson);
			} else {
				log.warn("No existe contexto asociado al trámite: " + tramite);
			}
		} else {
			log.warn("No se ha especificado trámite.");
		}
	}

}

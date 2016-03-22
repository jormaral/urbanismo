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

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.ejb.Stateless;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringEscapeUtils;
import org.codehaus.jackson.map.ObjectMapper;

import es.mitc.redes.urbanismoenred.servicios.refundido.ContextoRefundidoBasico;
import es.mitc.redes.urbanismoenred.servicios.refundido.Estado;

/**
 * @author Arnaiz Consultores
 *
 */
@Stateless(name="LOG_REFUNDIDO")
public class ObtenerFicheroLog implements IAccion {
	
	private static final String ULTIMA_LINEA = "refundido.log.linea";
	private static final int LINEAS_TRANSMITIDAS = 25;

	/* (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.consola.refundido.acciones.IAccion#ejecutar(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public void ejecutar(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		
		ContextoRefundidoBasico contexto = (ContextoRefundidoBasico) request.getSession().getAttribute("refundido.contexto");
		ObjectMapper om = new ObjectMapper();
		Map<String, Object> datos = new HashMap<String, Object>();
		StringBuffer sb = new StringBuffer();
		
        if (contexto != null) {
        	int i = 0;
        	if (contexto.getArchivoLog() != null) {
        		FileReader f = new FileReader(contexto.getArchivoLog());
                String str = null;

                BufferedReader in = new BufferedReader(f);
                int ultimaLinea = contexto.getParametro(ULTIMA_LINEA) != null? (Integer)contexto.getParametro(ULTIMA_LINEA):0;
                for (i = 0; (str = in.readLine()) != null && i < ultimaLinea+LINEAS_TRANSMITIDAS; i++) {
                	if (i >= ultimaLinea) {
                		sb.append(StringEscapeUtils.escapeHtml(str));
                		sb.append("<br/>");
                	}
                }
                contexto.putParametro(ULTIMA_LINEA, i);
                
                in.close();
                f.close();
        	}
            
            datos.put("linea", i);
            datos.put("log", sb.toString());
            datos.put("estado", contexto.getEstado().toString());
        } else {
        	datos.put("estado", Estado.PENDIENTE.toString());
        	datos.put("error", "No hay proceso de refundido activo.");
        	datos.put("log", "");
        	datos.put("linea", 0);
        }
        
        om.writeValue(response.getWriter(), datos);
	}

}

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
import java.util.HashMap;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;

import es.mitc.redes.urbanismoenred.data.rpm.diccionario.Tipotramite;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Tramite;
import es.mitc.redes.urbanismoenred.servicios.planeamiento.ServicioDiccionariosLocal;
import es.mitc.redes.urbanismoenred.servicios.planeamiento.ServicioPlaneamientoLocal;
import es.mitc.redes.urbanismoenred.servicios.refundido.RefundidoLocal;

/**
 * @author Arnaiz Consultores
 *
 */
@Stateless(name="GET_INFO_TRAMITE_REFUNDIDO")
public class ObtenerInformacionTramiteRefundido implements IAccion {
	
	@EJB
	private ServicioDiccionariosLocal servicioDiccionario;
	@EJB
	private ServicioPlaneamientoLocal servicioPlaneamiento;
	@EJB(beanName = "ServicioRefundido")
	private RefundidoLocal servicioRefundido;

	/* (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.consola.refundido.acciones.IAccion#ejecutar(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public void ejecutar(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		if (request.getParameter("idTram") != null &&
        		request.getParameter("idioma") != null) {
        	int idTram;
            try {
                idTram = Integer.parseInt(request.getParameter("idTram"));
            } catch (Exception e) {
                // el parámetro idTramite no es un número
                response.getWriter().print("N");
                return;
            }
            String idioma = request.getParameter("idioma");
            Map<String, Object> data = new HashMap<String, Object>();
            Tramite trm = servicioPlaneamiento.get(Tramite.class, idTram);
            data.put("idTram", idTram);
            data.put("nombreTram", trm.getNombre());
            data.put("idPlan", trm.getPlan().getIden());
            data.put("nombrePlan", trm.getPlan().getNombre());
            data.put("nombreTipoTramite", servicioDiccionario.getTraduccion(Tipotramite.class, trm.getIdtipotramite(), idioma));
            data.put("fecha", trm.getFecha().toString());
            data.put("refundible", servicioRefundido.esRefundible(trm.getIden()));
            
            response.setHeader("Content-type", "application-json");

            // Write JSON data
            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(response.getWriter(), data);

        } else {
        	response.getWriter().print("No se han indicado los parámetros de la petición");
        }
	}

}

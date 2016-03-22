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

import es.mitc.redes.urbanismoenred.data.rpm.diccionario.Ambito;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Plan;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Tramite;
import es.mitc.redes.urbanismoenred.servicios.planeamiento.ModalidadPlanes;
import es.mitc.redes.urbanismoenred.servicios.planeamiento.ServicioDiccionariosLocal;
import es.mitc.redes.urbanismoenred.servicios.planeamiento.ServicioPlaneamientoLocal;
import es.mitc.redes.urbanismoenred.servicios.refundido.RefundidoLocal;

/**
 * @author Arnaiz Consultores
 *
 */
@Stateless(name="GET_INFO_AMBITO_REFUNDIDO")
public class ObtenerInformacionAmbitoRefundido implements IAccion {
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
		if (request.getParameter("idAmb") != null 
    			&& request.getParameter("idioma") != null) {

            int idAmb;
            try {
                idAmb = Integer.parseInt(request.getParameter("idAmb"));
            } catch (Exception e) {
                // el parámetro idTramite no es un número
                return;
            }
            String idioma = request.getParameter("idioma");
			
			// Write JSON data
            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(response.getWriter(), getInformacion(idAmb,idioma));
			
    	} else {
    		response.getWriter().print("No se han indicado los parámetros de la petición");
    	}
	}
	
	protected Map<String, Object> getInformacion(int idAmb, String idioma) {
    	Ambito ambito = servicioDiccionario.get(Ambito.class, idAmb);
		Map<String, Object> data = new HashMap<String, Object>();
		if (ambito != null) {
			data.put("idAmb", ambito.getIden());
	        data.put("nombreAmb", servicioDiccionario.getTraduccion(Ambito.class, idAmb, idioma));
	        
	        // LOW jgarzon Se puede optimizar esta llamada si sólo se obtiene
	        // información sobre planes refundibles
	        Plan[] planes = servicioPlaneamiento.getPlanes(idAmb, ModalidadPlanes.RPM);
	        int numPlanesRefundibles = 0;
	        int numTramitesRefundibles = 0;
	        int numTramites = 0;
	        boolean refundible = false;
	        for (int i = 0; i< planes.length;i++) {
	        	for(Tramite tramite : planes[i].getTramites()) {
	        		if (servicioRefundido.esRefundible(tramite.getIden())) {
	        			numTramitesRefundibles++;
	        			refundible = true;
	        		}
	        		numTramites++;
	        	}
	        	if (refundible) {
	        		numPlanesRefundibles++;
	        		refundible = false;
	        	}
	        }
	        data.put("numPlanes", planes.length);
	        data.put("numPlanesRef", numPlanesRefundibles);
	        data.put("numTrams", numTramites);
	        data.put("numTramsRef", numTramitesRefundibles);
		}
		return data;
	}

}

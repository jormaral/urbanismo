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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;

import es.mitc.redes.urbanismoenred.data.rpm.diccionario.Tipotramite;
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
@Stateless(name="GET_TRAMITES_REFUNDIBLES_DE_AMBITO")
public class ObtenerTramitesRefundibles implements IAccion {
	
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
		if (request.getParameter("idAmb") != null &&
        		request.getParameter("idioma") != null) {
        	int idAmb;
            try {
            	idAmb = Integer.parseInt(request.getParameter("idAmb"));
            } catch (Exception e) {
                // el parámetro idTramite no es un número
                response.getWriter().print("N");
                return;
            }
            String idioma = request.getParameter("idioma");        

            List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();

            Map<String, Object> item;
            
            Plan[] planes = servicioPlaneamiento.getPlanes(idAmb, ModalidadPlanes.REF);
            
            for (Plan pln : planes) {
                Tramite ultimoTramite = null;

                for (Tramite trm : pln.getTramites()) {
                    if (servicioRefundido.esRefundible(trm.getIden())) {
                        if (ultimoTramite != null) {
                            if (trm.getFecha().after(ultimoTramite.getFecha())) {
                                ultimoTramite = trm;
                            }
                        } else {
                            ultimoTramite = trm;
                        } // esTramiteRefundible
                    }
                    if (ultimoTramite != null) {
                        item = new HashMap<String, Object>();
                        item.put("idTram", ultimoTramite.getIden());
                        item.put("tipoTramite", ultimoTramite.getTipotramite().getIden());
                        item.put("nombreTram", servicioDiccionario.getTraduccion(Tipotramite.class, trm.getIdtipotramite(), idioma) + (trm.getNombre() != null? " " + trm.getNombre():""));
                        item.put("idPlan", pln.getIden());
                        item.put("nombrePlan", pln.getNombre());
                        data.add(item);
                    }
                }
            }
           
            response.setHeader("Content-type", "application-json");

            // Write JSON data
            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(response.getWriter(), data);
            
        } else {
        	response.getWriter().print("No se han indicado los parámetros de la petición");
        }
	}

}

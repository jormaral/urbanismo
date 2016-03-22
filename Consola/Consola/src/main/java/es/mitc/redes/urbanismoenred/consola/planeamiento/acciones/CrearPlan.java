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
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Plan;
import es.mitc.redes.urbanismoenred.servicios.planeamiento.ExcepcionPlaneamiento;
import es.mitc.redes.urbanismoenred.servicios.planeamiento.ServicioPlaneamientoLocal;

/**
 * @author Arnaiz Consultores
 *
 */
@Stateless(name="ADD_PLAN")
public class CrearPlan implements IAccion {
	
	@EJB
	private ServicioPlaneamientoLocal servicioPlaneamiento;

	/* (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.consola.planeamiento.IAccion#ejecutar(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public void ejecutar(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String idPlan = request.getParameter("idPlan");
		String nombre = request.getParameter("nombre");
        String idAmbito = request.getParameter("idAmbito");
        String idInstrumento = request.getParameter("idInstrumento");
        String idPadre = request.getParameter("idPadre");
        String idBase = request.getParameter("idBase");
        String listaPlanesOperados = request.getParameter("listaPlanesOperados");
        String listaTipoOperacionOperados = request.getParameter("listaTipoOperacionOperados");
        String texto = request.getParameter("texto");

        Map<String, Object> datos = new HashMap<String, Object>();
        response.setContentType("application-json;charset=UTF-8");
        if (nombre != null && idAmbito != null) {
        	try {
        		Plan plan;
        		if (idPlan != null) {
        			plan = servicioPlaneamiento.get(Plan.class,Integer.parseInt(idPlan));
        			
        			if (plan != null) {
        				plan.setNombre(nombre);
        				plan.setTexto(texto);
        				plan.setIdambito(Integer.parseInt(idAmbito));
        			} else {
        				datos.put("estado", Boolean.FALSE);
        				datos.put("error", "Plan desconocido");
        			}
        		} else {
        			plan = servicioPlaneamiento.crearPlan(nombre, 
    						Integer.parseInt(idAmbito),
    						texto,
    						idPadre != null && !idPadre.isEmpty()? new Integer(idPadre):null, 
    						idBase != null && !idBase.isEmpty()? new Integer(idBase):null);
        		}
				
        		if (plan != null) {
					datos.put("idPlan", plan.getIden());
					
					if (listaPlanesOperados != null && !listaPlanesOperados.trim().isEmpty()) {
						String[] arrayOperados = listaPlanesOperados.split(",");
						String[] arrayOperaciones = listaTipoOperacionOperados.split(",");
						
						if (arrayOperaciones.length == arrayOperados.length) {	
							Integer[] planesOperados = new Integer[arrayOperados.length];
							Integer[] operaciones = new Integer[arrayOperaciones.length];
							
							for (int i = 0; i < arrayOperados.length; i++) {
								planesOperados[i] = new Integer(arrayOperados[i]);
								operaciones[i] = new Integer(arrayOperaciones[i]);
							}
							
							try {
								servicioPlaneamiento.asociarOperacion(plan.getIden(), Integer.parseInt(idInstrumento), planesOperados, operaciones);
								datos.put("estado", Boolean.TRUE);
							} catch (ExcepcionPlaneamiento e) {
								servicioPlaneamiento.borrar(plan);
								datos.put("estado", Boolean.FALSE);
								datos.put("error", e.getMessage());
							}
						} else {
							servicioPlaneamiento.borrar(plan);
							datos.put("estado", Boolean.FALSE);
							datos.put("error", "No coincide la lista de planes operados con la lista de operaciones");
						}
					} else {
						if (idInstrumento != null) {
							try {
								servicioPlaneamiento.asociarOperacion(plan.getIden(), Integer.parseInt(idInstrumento));
								datos.put("estado", Boolean.TRUE);
							} catch (ExcepcionPlaneamiento e) {
								datos.put("estado", Boolean.FALSE);
								datos.put("error", e.getMessage());
							}
						} 
					}
        		}
			} catch (NumberFormatException e) {
				datos.put("estado", Boolean.FALSE);
				datos.put("error", "Parámetros incorrectos");
			} catch (ExcepcionPlaneamiento e) {
				datos.put("estado", Boolean.FALSE);
				datos.put("error", e.getMessage());
			}
        } else {
        	datos.put("estado", Boolean.FALSE);
			datos.put("error", "Faltan parámetross");
        }
        
        ObjectMapper om = new ObjectMapper();
        om.writeValue(response.getWriter(), datos);
	}
}

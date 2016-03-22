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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;

import es.mitc.redes.urbanismoenred.consola.planeamiento.IAccion;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Determinacion;
import es.mitc.redes.urbanismoenred.servicios.planeamiento.ServicioPlaneamientoLocal;

/**
 * @author Arnaiz Consultores
 *
 */
@Stateless(name="GET_DETS_ACTOS_DE_ENTIDAD")
public class GetDeterminacionesActosEntidad implements IAccion {
	
	@EJB
	private ServicioPlaneamientoLocal servicioPlaneamiento;

	/* (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.consola.planeamiento.IAccion#ejecutar(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public void ejecutar(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String idEnt = request.getParameter("idEnt");
		if (idEnt != null) {
			Determinacion[] determinaciones = servicioPlaneamiento.getDeterminacionesActoEntidad(Integer.valueOf(idEnt));
	    	
	    	List<Map<String, Object>> determinacionesJSON = new ArrayList<Map<String,Object>>();
	    	Map<String, Object> detJSON;
	    	for (Determinacion det : determinaciones) {
            	detJSON = new HashMap<String, Object>();
            	determinacionesJSON.add(detJSON);
            	
            	detJSON.put("iden", det.getIden());
            	detJSON.put("nombre", det.getNombre());
            	detJSON.put("idDetPadre", det.getDeterminacionByIdpadre() != null ? det.getDeterminacionByIdpadre().getIden(): "-");
            	detJSON.put("detPadre", det.getDeterminacionByIdpadre() != null ? det.getDeterminacionByIdpadre().getNombre(): "-");
            	detJSON.put("idTramite", det.getTramite().getIden());
            	detJSON.put("idCaracter", det.getIdcaracter());
            	detJSON.put("apartado", det.getApartado());
            	detJSON.put("texto", det.getTexto());
            	detJSON.put("etiqueta", det.getEtiqueta());
            	detJSON.put("codigo", det.getCodigo());
            	detJSON.put("base", det.getDeterminacionByIddeterminacionbase() != null ? det.getDeterminacionByIddeterminacionbase().getNombre(): "-");
          
            }
	    	
	    	ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(response.getWriter(), determinacionesJSON);
		} else {
			response.getWriter().print("Solicitud incompleta");
		}
	}

}

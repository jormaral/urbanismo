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

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;

import es.mitc.redes.urbanismoenred.consola.planeamiento.IAccion;
import es.mitc.redes.urbanismoenred.consola.util.helpers.GeometryHelper;
import es.mitc.redes.urbanismoenred.servicios.planeamiento.ServicioPlaneamientoLocal;

/**
 * @author Arnaiz Consultores
 *
 */
@Stateless(name="GET_WKT_FROM_ENTIDAD")
public class GetPoligonosEntidad implements IAccion {
	
	private final static Logger log = Logger.getLogger(GetPoligonosEntidad.class);
	
	@EJB
	private ServicioPlaneamientoLocal servicioPlaneamiento;

	/* (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.consola.planeamiento.IAccion#ejecutar(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public void ejecutar(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String idEnt = request.getParameter("ID");
		if (idEnt != null) {
			String[] geometrias = servicioPlaneamiento.getGeometriaEntidad(Integer.valueOf(idEnt));
            Map<String, Object> data = new HashMap<String, Object>();

            Geometry geometry;
            Envelope ext=null;
            for (String geometria : geometrias) {
                try {
                    geometry = GeometryHelper.GeoFromWKT(geometria);
                    if (ext==null){
                        ext=geometry.getEnvelope().getEnvelopeInternal();
                    }else{
                        ext.expandToInclude(geometry.getEnvelope().getEnvelopeInternal());
                    }
                } catch (Exception e) {
                    log.warn("No se pudo procesar la geometria de la entidad " + idEnt + ". Causa: " + e.getMessage(), e);
                }
            }
            if (ext!=null){
                data.put("extension", ext.toString());
            }else{
                log.warn("No se encontro la geometría de la entidad " + idEnt);
            }
            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(response.getWriter(), data);
		} else {
			response.getWriter().print("Solicitud incompleta");
		}
	}

}

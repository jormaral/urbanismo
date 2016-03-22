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

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.vividsolutions.jts.geom.Geometry;

import es.mitc.redes.urbanismoenred.consola.planeamiento.IAccion;
import es.mitc.redes.urbanismoenred.consola.util.helpers.GeometryHelper;
import es.mitc.redes.urbanismoenred.servicios.planeamiento.ServicioPlaneamientoLocal;
import es.mitc.redes.urbanismoenred.utils.recursos.textos.Textos;

/**
 * @author Arnaiz Consultores
 *
 */
@Stateless(name = "GET_EXTENSION_DE_TRAMITE")
public class GetExtensionTramite implements IAccion {

    @EJB
    private ServicioPlaneamientoLocal servicioPlaneamiento;

    /* (non-Javadoc)
     * @see es.mitc.redes.urbanismoenred.consola.planeamiento.IAccion#ejecutar(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    public void ejecutar(HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        String idTramite = request.getParameter("idTramite");
        if (idTramite != null) {
            String poligono = servicioPlaneamiento.getPoligonoTramite(Integer.parseInt(idTramite));

            if (poligono != null) {
                Geometry extension = null;
                extension = GeometryHelper.GeoFromWKT(poligono);
                if (extension != null) {
                    if (request.getParameter("SRS")!=null && !"".equals(request.getParameter("SRS"))){
                        extension=GeometryHelper.TransformGeometry(extension, Textos.getTexto("consola", "projection" ), request.getParameter("SRS"));
                    }
                    response.getWriter().print(extension.getEnvelope().toText());
                } else {
                    response.getWriter().print("");
                }
            }else{
                response.getWriter().print("");
            }
        } else {
            response.getWriter().print("Solicitud incompleta");
        }

    }
}

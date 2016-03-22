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

import org.apache.log4j.Logger;

import com.vividsolutions.jts.geom.Geometry;

import es.mitc.redes.urbanismoenred.consola.planeamiento.IAccion;
import es.mitc.redes.urbanismoenred.consola.util.helpers.GeometryHelper;
import es.mitc.redes.urbanismoenred.servicios.planeamiento.ServicioDiccionariosLocal;

/**
 * @author Arnaiz Consultores
 *
 */
@Stateless(name = "GET_EXTENSION_DE_AMBITO")
public class GetExtensionAmbito implements IAccion {

    private final static Logger log = Logger.getLogger(GetExtensionAmbito.class);
    @EJB
    private ServicioDiccionariosLocal servicioDiccionario;

    /* (non-Javadoc)
     * @see es.mitc.redes.urbanismoenred.consola.planeamiento.IAccion#ejecutar(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    public void ejecutar(HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        String idAmbito = request.getParameter("idAmbito");
        if (idAmbito != null) {
            String[] poligonos = servicioDiccionario.getPoligonosAmbito(Integer.parseInt(idAmbito));


            Geometry geometria = null;
            Geometry extension = null;
            for (String poligono : poligonos) {
                try {
                    geometria = GeometryHelper.GeoFromWKT(poligono);
                    if (extension != null) {
                        extension = extension.union(geometria);
                    } else {
                        extension = geometria;
                    }
                } catch (Exception e) {
                    log.warn("No se pudo procesar el polígono del ámbito " + idAmbito + ". Causa: " + e.getMessage(), e);
                }
            }

            if (extension != null) {
                response.getWriter().print(extension.getEnvelope().toText());
            } else {
                response.getWriter().print("");
            }
        } else {
            response.getWriter().print("Solicitud incompleta");
        }

    }
}

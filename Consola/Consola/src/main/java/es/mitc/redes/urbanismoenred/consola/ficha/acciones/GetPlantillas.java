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
package es.mitc.redes.urbanismoenred.consola.ficha.acciones;

import es.mitc.redes.urbanismoenred.consola.ficha.IAccion;
import es.mitc.redes.urbanismoenred.utils.configuracion.RedesConfig;
import es.mitc.redes.urbanismoenred.utils.recursos.textos.Textos;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

import javax.ejb.Stateless;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Arnaiz Consultores
 *
 */
@Stateless(name = "GET_PLANTILLAS_FICHAS")
public class GetPlantillas implements IAccion {

    /* (non-Javadoc)
     * @see es.mitc.redes.urbanismoenred.consola.planeamiento.IAccion#ejecutar(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    public void ejecutar(HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        try {
            File dir = new File(RedesConfig.getREDES_PATH() + File.separator + "conf" + File.separator + "Plantillas");
            String[] children = findInDir("*.jrxml", dir);
             response.getWriter().print(children);
        } catch (Exception ex) {
            response.getWriter().print("");
        }
        response.getWriter().flush();
    }

    private String[] findInDir(final String patron, File dir) {
        FilenameFilter filter = new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.toLowerCase().endsWith(patron.toLowerCase());
            }
        };
        String[] children = dir.list(filter);
        return children;
    }
}

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
import es.mitc.redes.urbanismoenred.data.rpm.ficha.Conjuntogrupo;
import es.mitc.redes.urbanismoenred.servicios.ficha.ExcepcionFicha;
import es.mitc.redes.urbanismoenred.servicios.ficha.ServicioFichaLocal;
import java.io.IOException;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Arnaiz Consultores
 *
 */
@Stateless(name = "GUARDAR_CONJUNTO_FICHA")
public class GuardarConjuntoFicha implements IAccion {

    @EJB
    private ServicioFichaLocal servicioFichas;

    /* (non-Javadoc)
     * @see es.mitc.redes.urbanismoenred.consola.planeamiento.IAccion#ejecutar(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    public void ejecutar(HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        try {
            Conjuntogrupo conjunto = null;
            if (request.getParameter("idConjunto")!=null){
                conjunto = servicioFichas.guardarConjuntoGrupo(
                    0,
                    Long.parseLong(request.getParameter("idConjunto")),
                    request.getParameter("nombreGrupo"),
                    Long.parseLong(request.getParameter("ordenGrupo")),
                    Boolean.parseBoolean(request.getParameter("regimen_directo")),
                    Boolean.parseBoolean(request.getParameter("usos")),
                    Boolean.parseBoolean(request.getParameter("actos")));
            }else{
                conjunto = servicioFichas.guardarConjuntoGrupo(
                    Long.parseLong(request.getParameter("idFicha")),
                    0,
                    request.getParameter("nombreGrupo"),
                    Long.parseLong(request.getParameter("ordenGrupo")),
                    Boolean.parseBoolean(request.getParameter("regimen_directo")),
                    Boolean.parseBoolean(request.getParameter("usos")),
                    Boolean.parseBoolean(request.getParameter("actos")));
            }
            response.getWriter().print(conjunto != null);
        } catch (ExcepcionFicha ex) {
            response.getWriter().print(false);
        }
        response.getWriter().flush();
    }
}

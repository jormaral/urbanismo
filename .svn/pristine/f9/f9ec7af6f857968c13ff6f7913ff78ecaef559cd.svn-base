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
import es.mitc.redes.urbanismoenred.data.rpm.ficha.Conjuntodeterminaciongrupo;
import es.mitc.redes.urbanismoenred.data.rpm.ficha.Conjuntogrupo;
import es.mitc.redes.urbanismoenred.data.rpm.ficha.Grupodeterminacion;
import es.mitc.redes.urbanismoenred.data.rpm.ficha.Grupodeterminaciondeterminacion;
import es.mitc.redes.urbanismoenred.data.rpm.ficha.Regimenesacto;
import es.mitc.redes.urbanismoenred.data.rpm.ficha.Regimenesuso;
import es.mitc.redes.urbanismoenred.data.rpm.ficha.Valoresclasifacto;
import es.mitc.redes.urbanismoenred.data.rpm.ficha.Valoresclasifuso;
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
@Stateless(name = "SUBIR_ELEMENTO_FICHA")
public class SubirElementoFicha implements IAccion {

    @EJB
    private ServicioFichaLocal servicioFichas;

    /* (non-Javadoc)
     * @see es.mitc.redes.urbanismoenred.consola.planeamiento.IAccion#ejecutar(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    public void ejecutar(HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        try {
            if ("determinacionregimenuso".equals(request.getParameter("tipo"))) {
                response.getWriter().print(servicioFichas.up(Regimenesuso.class, Long.valueOf(request.getParameter("idElemento"))));
            } else if ("determinacionregimenacto".equals(request.getParameter("tipo"))) {
                response.getWriter().print(servicioFichas.up(Regimenesacto.class, Long.valueOf(request.getParameter("idElemento"))));
            } else if ("valordetclasfiuso".equals(request.getParameter("tipo"))) {
                response.getWriter().print(servicioFichas.up(Valoresclasifuso.class, Long.valueOf(request.getParameter("idElemento"))));
            } else if ("valordetclasfiacto".equals(request.getParameter("tipo"))) {
                response.getWriter().print(servicioFichas.up(Valoresclasifacto.class, Long.valueOf(request.getParameter("idElemento"))));
            } else if ("grupo".equals(request.getParameter("tipo"))) {
                response.getWriter().print(servicioFichas.up(Conjuntodeterminaciongrupo.class, Long.valueOf(request.getParameter("idElemento"))));
            } else if ("conjuntogrupodeterminacion".equals(request.getParameter("tipo"))) {
                response.getWriter().print(servicioFichas.up(Grupodeterminaciondeterminacion.class, Long.valueOf(request.getParameter("idElemento"))));
            } else if ("conjuntogrupo".equals(request.getParameter("tipo"))) {
                response.getWriter().print(servicioFichas.up(Conjuntogrupo.class, Long.valueOf(request.getParameter("idElemento"))));
            } else if ("grupodeterminacion".equals(request.getParameter("tipo"))) {
                response.getWriter().print(servicioFichas.up(Grupodeterminacion.class, Long.valueOf(request.getParameter("idElemento"))));
            } else {
                response.getWriter().print(false);
            }
        } catch (ExcepcionFicha ex) {
            response.getWriter().print(false);
        }
        response.getWriter().flush();
    }
}

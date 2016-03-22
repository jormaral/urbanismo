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
import java.util.Map;

import javax.ejb.Stateless;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Arnaiz Consultores
 *
 */
@Stateless(name="GET_INFO_AMBITO_REFUNDIDO_TABLE")
public class ObtenerTablaAmbitoRefundido extends ObtenerInformacionAmbitoRefundido implements IAccion {

	/* (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.consola.refundido.acciones.IAccion#ejecutar(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public void ejecutar(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		if (request.getParameter("idAmb") != null &&
        		request.getParameter("idioma") != null) {
            response.getWriter().print("No se han indicado los parámetros de la petición");
            return;
        }

        int idAmb;
        try {
            idAmb = Integer.parseInt(request.getParameter("idAmb"));
        } catch (Exception e) {
            // el parámetro idTramite no es un número
            return;
        }
        String idioma = request.getParameter("idioma");
        
        Map<String, Object> data = getInformacion(idAmb, idioma);

        String html =
                "  <input type='hidden' id='idAmbSelec' name='idAmbSelec' value='" + data.get("idAmb") + "'/>" +
                "  <table> " +
                "    <tr> " +
                "        <th>Planes:</th> " +
                "        <td>" + data.get("numPlanes") + "</td> " +
                "        <th>P. refundibles:</th> " +
                "        <td>" + data.get("numPlanesRef") + "</td> " +
                "    </tr> " +
                "    <tr> " +
                "        <th>Trámites:</th> " +
                "        <td>" + data.get("numTrams") + "</td> " +
                "        <th>T. refundibles:</th> " +
                "        <td>" + data.get("numTramsRef") + "</td> " +
                "    </tr> " +
                "  </table>";
        response.getWriter().print(html);
	}
}

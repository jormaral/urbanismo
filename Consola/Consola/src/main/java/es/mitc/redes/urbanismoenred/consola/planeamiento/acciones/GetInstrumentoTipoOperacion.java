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
import es.mitc.redes.urbanismoenred.data.rpm.diccionario.Instrumentotipooperacionplan;
import es.mitc.redes.urbanismoenred.data.rpm.diccionario.Tipooperacionplan;
import es.mitc.redes.urbanismoenred.servicios.planeamiento.ServicioDiccionariosLocal;
import es.mitc.redes.urbanismoenred.servicios.planeamiento.ServicioPlaneamientoLocal;
import es.mitc.redes.urbanismoenred.utils.recursos.textos.Textos;

/**
 * @author Arnaiz Consultores
 *
 */
@Stateless(name="GET_INSTRUMENTOSTIPOOPERACION")
public class GetInstrumentoTipoOperacion implements IAccion {
	
	@EJB
	private ServicioPlaneamientoLocal servicioPlaneamiento;
	private ServicioDiccionariosLocal servicioDiccionario;

	/* (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.consola.planeamiento.IAccion#ejecutar(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public void ejecutar(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String idInstrumento = request.getParameter("idInstrumento");
		
		if (idInstrumento != null) {
			Instrumentotipooperacionplan[] instrumentos = servicioPlaneamiento.getOperacionesPorInstrumento(Integer.parseInt(idInstrumento));
			
            Map<String, Object> data = new HashMap<String, Object>();
            List<Object> columnlist = new ArrayList<Object>();
            for (Instrumentotipooperacionplan o : instrumentos) {
                String texto = Textos.getTexto("consola", "idTramiteNoProcede");
                if (o.getTipooperacionplan().getIden() != Integer.valueOf(texto).intValue()) {
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("id", o.getIden());
                    map.put("nombre", servicioDiccionario.getTraduccion(Tipooperacionplan.class, o.getTipooperacionplan().getIden(), "es"));
                    columnlist.add(map);
                }
            }
            data.put("data", columnlist);
            ObjectMapper om = new ObjectMapper();
            om.writeValue(response.getWriter(), data);
		}
	}

}

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

import es.mitc.redes.urbanismoenred.consola.planeamiento.IAccion;
import es.mitc.redes.urbanismoenred.data.rpm.diccionario.Instrumentoplan;
import es.mitc.redes.urbanismoenred.data.rpm.diccionario.Instrumentotipooperacionplan;
import es.mitc.redes.urbanismoenred.data.rpm.diccionario.Tipooperacionplan;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Operacionplan;
import es.mitc.redes.urbanismoenred.servicios.planeamiento.ServicioDiccionariosLocal;
import es.mitc.redes.urbanismoenred.servicios.planeamiento.ServicioPlaneamientoLocal;

/**
 * @author Arnaiz Consultores
 *
 */
@Stateless(name="OBTENER_OPERADORES_DE_UNA_DETERMINACION")
public class GetOperadoresDeterminacion implements IAccion {
	
	@EJB
	private ServicioPlaneamientoLocal servicioPlaneamiento;
	@EJB
	private ServicioDiccionariosLocal servicioDiccionario;

	/* (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.consola.planeamiento.IAccion#ejecutar(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public void ejecutar(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		
		String idPlan = request.getParameter("idPlan");
        if (idPlan != null) {
        	String resultado = "";
        	String tipoOperacion;
        	String instrumento;
        	Instrumentotipooperacionplan ito;
        	for (Operacionplan op : servicioPlaneamiento.getPlanesOperados(Integer.parseInt(idPlan))) {
        		ito = servicioDiccionario.get(Instrumentotipooperacionplan.class, op.getIdinstrumentotipooperacion());
        		tipoOperacion = servicioDiccionario.getTraduccion(Tipooperacionplan.class, ito.getTipooperacionplan().getIden(), "es");
        		instrumento = servicioDiccionario.getTraduccion(Instrumentoplan.class, ito.getInstrumentoplan(), "es");
        		// TODO jgarzon el resultado debería estar en formato JSON
        		resultado = resultado + op.getPlanByIdplanoperador().getNombre() + "|" + tipoOperacion + "|" + instrumento + "|||";
        	}
            
            response.getWriter().print(resultado);
        } else {
        	response.getWriter().print("Solicitud incompleta");
        }

	}

}

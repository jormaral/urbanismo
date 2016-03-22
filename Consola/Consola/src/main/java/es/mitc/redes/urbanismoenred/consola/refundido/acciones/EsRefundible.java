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

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import es.mitc.redes.urbanismoenred.servicios.refundido.RefundidoLocal;

/**
 * @author Arnaiz Consultores
 *
 */
@Stateless(name="TRAMITE_ES_REFUNDIBLE")
public class EsRefundible implements IAccion {
	
	private static final Logger log = Logger.getLogger(EsRefundible.class);
	
	@EJB(beanName = "ServicioRefundido")
	private RefundidoLocal servicioRefundido;

	/* (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.consola.refundido.acciones.IAccion#ejecutar(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public void ejecutar(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String result = "N";

        try {
            if (request.getParameter("idTramite") != null) {
           
	            int idTram;
	            try {
	                idTram = Integer.parseInt(request.getParameter("idTramite"));
	                if (servicioRefundido.esRefundible(idTram)) {
	                	result = "S";
	                }
	            } catch (Exception e) {
	                // el parámetro idTramite no es un número
	                response.getWriter().print("N");
	                return;
	            }
	         } else { 
	        	 result = "No se han indicado los parámetros de la petición";
	         }

        } catch (Exception e) {
            log.warn(e.getMessage(),e);
        }
        
        response.getWriter().print(result);
	}

}

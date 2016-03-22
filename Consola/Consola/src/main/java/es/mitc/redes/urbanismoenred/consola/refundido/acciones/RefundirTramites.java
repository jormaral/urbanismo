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
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import es.mitc.redes.urbanismoenred.servicios.refundido.ContextoRefundido;
import es.mitc.redes.urbanismoenred.servicios.refundido.GestorContextosRefundidoLocal;
import es.mitc.redes.urbanismoenred.servicios.refundido.RefundidoLocal;
import es.mitc.redes.urbanismoenred.servicios.seguridad.ServicioDiarioLocal;
import es.mitc.redes.urbanismoenred.servicios.seguridad.Usuario;

/**
 * @author Arnaiz Consultores
 *
 */
@Stateless(name = "REFUNDIR_TRAMITES")
public class RefundirTramites implements IAccion {
	
	private static final Logger log = Logger.getLogger(RefundirTramites.class);

	@EJB
	private ServicioDiarioLocal servicioDiario;
	@EJB
	private GestorContextosRefundidoLocal gestorContextos;
	@EJB(beanName = "NuevoServicioRefundido")
	private RefundidoLocal servicioRefundido;

	/* (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.consola.refundido.acciones.IAccion#ejecutar(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public void ejecutar(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		
		Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
		
		String txtTramites = request.getParameter("listaTramites");

        boolean crearTramite = true;
        if (request.getParameter("crearTramite") != null) {
        	crearTramite =  (request.getParameter("crearTramite").equalsIgnoreCase("true"));
        }
        
        try {
        	servicioDiario.nuevoRegistroDiario(usuario.getNombre(), 
        			es.mitc.redes.urbanismoenred.servicios.seguridad.TipoSubsistema.REFUNDIDO, 
        			"Lanzamiento del refundido de: " + txtTramites);
        	
        	String[] arrTramites = txtTramites.split(",");
        	List<Integer> lstTramites = new ArrayList<Integer>();
        	for (int i = 0; i < arrTramites.length; i++) {
        		if (!arrTramites[i].trim().isEmpty()) {
        			lstTramites.add(Integer.parseInt(arrTramites[i].trim()));
        		}
        	}
        	if (lstTramites.size() > 0) {
        		ContextoRefundido contexto = gestorContextos.generarContexto(lstTramites, usuario, crearTramite);
            	request.getSession().setAttribute("refundido.contexto", contexto);
            	servicioRefundido.refundirTramites(contexto);
            	
            	servicioDiario.nuevoRegistroDiario(usuario.getNombre(), es.mitc.redes.urbanismoenred.servicios.seguridad.TipoSubsistema.REFUNDIDO, "Refundido sobre el ámbito" + contexto.getIdAmbito() +" finalizado");
            	response.getWriter().print("TRUE");
        	} else {
        		servicioDiario.nuevoRegistroDiario(usuario.getNombre(), es.mitc.redes.urbanismoenred.servicios.seguridad.TipoSubsistema.REFUNDIDO, "Refundido abortado.");
        		response.getWriter().print("No se han especificado trámites a refundir");
        	}
        } catch (Exception e) {
        	log.warn("Error al invocar refundido: " + e.getMessage(),e);
        	response.getWriter().print("Error al invocar el refundido: " + e.getMessage());
        }
	}

}

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
package es.mitc.redes.urbanismoenred.consola.servlets;

import java.io.IOException;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import es.mitc.redes.urbanismoenred.consola.planeamiento.IAccion;
import es.mitc.redes.urbanismoenred.servicios.seguridad.TipoRol;
import es.mitc.redes.urbanismoenred.servicios.seguridad.Usuario;

/**
 * Servlet implementation class ActionServletGestionPlanes
 * 
 * @author Arnaiz Consultores
 */
public class ActionServletGestionPlanes extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private final static Logger log = Logger.getLogger(ActionServletGestionPlanes.class);

	private InitialContext icontext;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ActionServletGestionPlanes() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
    	
		try {
			icontext = new InitialContext();
	    } catch (NamingException e) {
			throw new ServletException("No se ha podido inicializar correctamente el servlet. Causa: " + e.getMessage());
		}
	}
	
	/**
     * 
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
        	Usuario usuario = (Usuario)request.getSession().getAttribute("usuario");
        	String wsName = request.getParameter("wsName");
        	
        	if (usuario != null) {
        		if (usuario.tieneRol(TipoRol.ADMINISTRADOR.getCodigo()) ||
        				usuario.tieneRol(TipoRol.GESTOR_PLAN.getCodigo())) {
        			
        			IAccion accion = (IAccion)icontext.lookup("java:global/UrbanismoEnRedV2/Urbanismoenred-ConsolaV2/"+wsName+"!es.mitc.redes.urbanismoenred.consola.planeamiento.IAccion");
        			
        			accion.ejecutar(request, response);
        		}
        	}
        } catch (Exception ex) {
            log.error(ActionServletRPM.class.getName() + "Error en processRequest ActionServlet Gestión de Planes. " + ex.getMessage(), ex);
        } 
    }

}

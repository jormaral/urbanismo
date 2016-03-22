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

import es.mitc.redes.urbanismoenred.servicios.consolidacion.ExcepcionConsolidacion;
import es.mitc.redes.urbanismoenred.servicios.consolidacion.ServicioConsolidacionLocal;
import es.mitc.redes.urbanismoenred.servicios.seguridad.TipoRol;
import es.mitc.redes.urbanismoenred.servicios.seguridad.Usuario;

import java.io.IOException;
import java.io.PrintWriter;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

/**
 * @author Arnaiz Consultores
 */
public class ActionServletConsolidacion extends HttpServlet {

    /**
	 * 
	 */
	private static final long serialVersionUID = -397457047588317069L;
	
	private static final Logger log = Logger.getLogger(ActionServletConsolidacion.class);
	private ServicioConsolidacionLocal servicioConsolidacion;
	
	/*
     * (non-Javadoc)
     * @see javax.servlet.GenericServlet#init(javax.servlet.ServletConfig)
     */
    @Override
    public void init(ServletConfig config) throws ServletException {
    	super.init(config);
    	InitialContext icontext;
		try {
			icontext = new InitialContext();
			servicioConsolidacion = (ServicioConsolidacionLocal)icontext.lookup("java:global/UrbanismoEnRedV2/Urbanismoenred-serviciosV2/ServicioConsolidacion!es.mitc.redes.urbanismoenred.servicios.consolidacion.ServicioConsolidacionLocal");
		} catch (NamingException e) {
			throw new ServletException("No se ha podido inicializar correctamente el servlet. Causa: " + e.getMessage());
		}
    }

	/**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
            
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
        
        String resultado = "OK";
        
        if (request.getSession().getAttribute("usuario") != null) {
        	if (usuario.tieneRol(TipoRol.CONSOLIDACION.getCodigo()) || 
        			usuario.tieneRol(TipoRol.ADMINISTRADOR.getCodigo())) {   
        		String wsName = request.getParameter("wsName");

        		if (wsName != null && "CONSOLIDAR_TRAMITES".equals(wsName)) {
        			try {
        				String txtTramite = request.getParameter("idTramite");
                    	
                    	if (txtTramite == null) {
                    		out.write("Llamada al servicio CONSOLIDAR_TRAMITES incompleta, falta parámetro idTramite.");
                    		out.close();
                    		return;
                    	}
                                    // Invocar la consolidacion
                    	servicioConsolidacion.consolidar(usuario, Integer.parseInt(txtTramite.trim()));

        			} catch (ExcepcionConsolidacion e) {
        				log.error("Error al consolidar " + request.getParameter("idTramite") + ": " + e.getMessage(),e);
        				resultado = "Error al consolidar \n" + e.getMessage();
                    }
                             
        		} else {
        			resultado="Llamada al servicio incompleta, falta parámetro wsName.";
        		}
            } else {
            	resultado = "El usuario no tiene permisos para ejecutar esta acción";      
            }
        	
        	out.print(resultado);
        } else {
        	getServletContext().getRequestDispatcher("/").forward(request, response);
        }
    	
    }

    /**
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
}

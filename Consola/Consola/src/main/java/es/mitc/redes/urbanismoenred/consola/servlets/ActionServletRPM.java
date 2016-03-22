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

import es.mitc.redes.urbanismoenred.consola.planeamiento.IAccion;
import es.mitc.redes.urbanismoenred.servicios.seguridad.TipoRol;
import es.mitc.redes.urbanismoenred.servicios.seguridad.Usuario;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import org.apache.log4j.Logger;

/**
 *
 * @author Arnaiz Consultores
 */
public class ActionServletRPM extends HttpServlet {

    /**
	 * 
	 */
	private static final long serialVersionUID = -2439991437703603607L;
	
    private final static Logger log = Logger.getLogger(ActionServletRPM.class);
    
    private InitialContext icontext;
    
    /*
     * (non-Javadoc)
     * @see javax.servlet.GenericServlet#init(javax.servlet.ServletConfig)
     */
    @Override
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
        		if (usuario.tieneRol(TipoRol.CONSOLA.getCodigo()) ||
        				usuario.tieneRol(TipoRol.ADMINISTRADOR.getCodigo()) ||
        				usuario.tieneRol(TipoRol.REFUNDIDO.getCodigo()) ||
        				usuario.tieneRol(TipoRol.VALIDACION.getCodigo()) ||
        				usuario.tieneRol(TipoRol.GESTOR_PLAN.getCodigo()) ||
        				usuario.tieneRol(TipoRol.CONFIGURADOR.getCodigo())) {
        			
        			IAccion accion = (IAccion)icontext.lookup("java:global/UrbanismoEnRedV2/Urbanismoenred-ConsolaV2/"+wsName+"!es.mitc.redes.urbanismoenred.consola.planeamiento.IAccion");
        			
        			accion.ejecutar(request, response);
        		}
        	}
        } catch (Exception ex) {
            log.error(ActionServletRPM.class.getName() + "Error en processRequest ActionServlet RPM. " + ex.getMessage(), ex);
        } 
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
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

    /**
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="JSON Generation for visualización">
}

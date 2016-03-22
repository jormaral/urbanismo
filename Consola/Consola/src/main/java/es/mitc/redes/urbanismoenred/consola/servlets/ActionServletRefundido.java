package es.mitc.redes.urbanismoenred.consola.servlets;

import java.io.IOException;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import es.mitc.redes.urbanismoenred.consola.refundido.acciones.IAccion;
import es.mitc.redes.urbanismoenred.servicios.seguridad.TipoRol;
import es.mitc.redes.urbanismoenred.servicios.seguridad.Usuario;

/**
 *	Servlet de gestión del proceso de refundido.
 * @author Arnaiz Consultores
 */
public class ActionServletRefundido extends HttpServlet {

    /**
	 * 
	 */
	private static final long serialVersionUID = -389766780936614491L;

	private InitialContext icontext;
	
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
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        //comprobación de roles
        
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
        
        if (usuario != null) {
        	if (usuario.tieneRol(TipoRol.REFUNDIDO.getCodigo()) ||
        			usuario.tieneRol(TipoRol.ADMINISTRADOR.getCodigo())) {
        		
        		String wsName = request.getParameter("wsName");
            
        		IAccion accion;
				try {
					accion = (IAccion)icontext.lookup("java:global/UrbanismoEnRedV2/Urbanismoenred-ConsolaV2/"+wsName+"!es.mitc.redes.urbanismoenred.consola.refundido.acciones.IAccion");
					accion.ejecutar(request, response);
				} catch (NamingException e) {
					response.getWriter().print("Servicio no encontrado: " + wsName);
				}	
        	}
        }
    }
}

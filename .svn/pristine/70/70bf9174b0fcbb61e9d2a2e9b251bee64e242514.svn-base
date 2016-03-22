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

import es.mitc.redes.urbanismoenred.consola.validacion.IAccion;
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
 *
 * @author Arnaiz Consultores
 */
public class ActionServletValidacionFIP extends HttpServlet {
	
	private static final Logger log = Logger.getLogger(ActionServletValidacionFIP.class);

    /**
	 * 
	 */
	private static final long serialVersionUID = 1025716071907343031L;
	
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
     * @throws IOException
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        try {
        	Usuario usuario = (Usuario)request.getSession().getAttribute("usuario");
        	
        	if (usuario != null && 
        			(usuario.tieneRol(TipoRol.VALIDACION.getCodigo()) 
        					|| usuario.tieneRol(TipoRol.ADMINISTRADOR.getCodigo()))) {
        		String wsName = request.getParameter("wsName");
                
        		IAccion accion = (IAccion)icontext.lookup("java:global/UrbanismoEnRedV2/Urbanismoenred-ConsolaV2/"+wsName+"!es.mitc.redes.urbanismoenred.consola.validacion.IAccion");
    			
    			accion.ejecutar(request, response);
        	} else {
        		PrintWriter out = response.getWriter();
                out.print("El usuario no tiene permisos para ejecutar esta acción");
                out.close();
                return;
        	}
            
        } catch (Exception e) {
            log.error("Ha fallado la llamada a un servicio. " + e.getMessage(),e);
        } finally {
        }
    }

	/*
     * (non-Javadoc)
     * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (IOException ex) {
            log.error("Ha fallado la llamada a un servicio. " + ex);
        }
    }

    /*
     * (non-Javadoc)
     * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        try {
            processRequest(request, response);
        } catch (IOException ex) {
            log.error("Ha fallado la llamada a un servicio. " + ex);
        }
    }

    /*
     * (non-Javadoc)
     * @see javax.servlet.GenericServlet#getServletInfo()
     */
    @Override
    public String getServletInfo() {
        return "ActionServletValidacionFIP";
    }// </editor-fold>
}

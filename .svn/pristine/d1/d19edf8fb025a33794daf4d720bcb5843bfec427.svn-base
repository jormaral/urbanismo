package es.mitc.redes.urbanismoenred.consola.servlets;

import es.mitc.redes.urbanismoenred.consola.visor.IAccion;
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

import es.mitc.redes.urbanismoenred.servicios.seguridad.TipoRol;
import es.mitc.redes.urbanismoenred.servicios.seguridad.Usuario;

/**
 *
 * @author Arnaiz Consultores
 */
public class ActionServletPreviewVisor extends HttpServlet {

    /**
	 * 
	 */
	private static final long serialVersionUID = 4505242047866014293L;

	private static final Logger log = Logger.getLogger(ActionServletPreviewVisor.class);

    /*
     * (non-Javadoc)
     * @see javax.servlet.GenericServlet#init(javax.servlet.ServletConfig)
     */
	private InitialContext icontext;

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
            Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");

            if (usuario != null
                    && (usuario.tieneRol(TipoRol.CONSOLA.getCodigo()))) {
                String wsName = request.getParameter("wsName");

                if (wsName != null) {
                	try {
	                    IAccion accion = (IAccion)icontext.lookup("java:global/UrbanismoEnRedV2/Urbanismoenred-ConsolaV2/"+wsName+"!es.mitc.redes.urbanismoenred.consola.visor.IAccion");
	        			
                            accion.ejecutar(request, response);
                	} catch (Exception ex) {
                        log.error("Error en al procesar accion. " + ex.getMessage(), ex);
                    } 
                } else {
                    log.warn("Accion " + wsName + " desconocida. Ignorando solicitud.");
                }
            } else {
                PrintWriter out = response.getWriter();
                out.print("'El usuario no tiene permisos para ejecutar esta accion'");
                out.close();
                return;
            }

        } catch (Exception e) {
            log.error("Ha fallado la llamada a un servicio. " + e.getMessage(), e);
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

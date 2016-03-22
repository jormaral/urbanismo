package es.mitc.redes.urbanismoenred.serviciosweb;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;

import javax.activation.MimetypesFileTypeMap;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import es.mitc.redes.urbanismoenred.servicios.seguridad.ExcepcionSeguridad;
import es.mitc.redes.urbanismoenred.servicios.seguridad.ServicioFipLocal;

/**
 * Servlet implementation class GestorFips
 */
@WebServlet(description = "Servlet que permite recuperar FIPs generados.", urlPatterns = { "/GestorFips" })
public class GestorFips extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private static final String ID_AMBITO = "idAmbito";
	private MimetypesFileTypeMap mimeMapper = new MimetypesFileTypeMap();
	private ServicioFipLocal servicioFip;
	
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GestorFips() {
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
			InitialContext context = new InitialContext();
			servicioFip = (ServicioFipLocal) context.lookup("java:global/UrbanismoEnRedV2/Urbanismoenred-serviciosV2/ServicioFip!es.mitc.redes.urbanismoenred.servicios.seguridad.ServicioFipLocal");
	    } catch (NamingException e) {
			throw new ServletException("No se ha podido inicializar correctamente el servlet. Causa: " + e.getMessage());
		} 
	}

	/**
	 * 
	 * @param request
	 * @param response
	 */
	private void processRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		File archivo = null;
		String ambito = null;
		if (request.getParameter(ID_AMBITO) != null) {
			ambito = request.getParameter(ID_AMBITO);
		} else {
			String[] path = request.getPathInfo().split("/");
			
			if (path.length > 1) {
				ambito = path[1];
			}
		}
		
		try {
			int idAmbito = Integer.parseInt(ambito);
			
			archivo = servicioFip.getTemplateFromAmbito(idAmbito, true);
		} catch (NumberFormatException nfe) {
			PrintWriter out = response.getWriter();
            out.print("El identificador de ámbito especificado no es válido: " + request.getParameter(ID_AMBITO));
            out.close();
            return;
		} catch (ExcepcionSeguridad e) {
			PrintWriter out = response.getWriter();
            out.print(e.getMessage());
            out.close();
            return;
		}
		
		ServletOutputStream op = response.getOutputStream();
        response.setContentType(mimeMapper.getContentType(archivo));
        response.setHeader("Content-Disposition", "filename=" + archivo.getName());
        response.setContentLength((int) archivo.length());

        byte[] bbuf = new byte[1024 * 256];
        DataInputStream in = new DataInputStream(new FileInputStream(archivo));
        int length = 0;
        while ((in != null) && ((length = in.read(bbuf)) != -1)) {
            op.write(bbuf, 0, length);
        }
        in.close();
        op.flush();
        op.close();
	}

}

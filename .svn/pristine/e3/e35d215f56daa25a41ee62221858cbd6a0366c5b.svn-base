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
package es.mitc.redes.urbanismoenred.productor.servlets;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import es.mitc.redes.urbanismoenred.data.rpm.diccionario.Ambito;
import es.mitc.redes.urbanismoenred.data.rpm.diccionario.Centroproduccion;
import es.mitc.redes.urbanismoenred.data.rpm.diccionario.Tipotramite;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Tramite;
import es.mitc.redes.urbanismoenred.servicios.comunes.EncriptacionCodigoTramite;
import es.mitc.redes.urbanismoenred.servicios.planeamiento.ServicioDiccionariosLocal;
import es.mitc.redes.urbanismoenred.servicios.seguridad.GestorFip1Local;

/**
 *
 * @author Arnaiz Consultores
 */
public class ActionServletFip1 extends HttpServlet {

    /**
	 * 
	 */
	private static final long serialVersionUID = 8993379130481981824L;
	private static final String CENTROS_PRODUCCION = "produccion.centros";
	private static final String TRAMITES = "produccion.tramites";
	private GestorFip1Local gestorFip1;
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	private ServicioDiccionariosLocal servicioDiccionario;
	
	
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
			
			gestorFip1 = (GestorFip1Local)icontext.lookup("java:global/UrbanismoEnRedV2/Urbanismoenred-serviciosV2/GestorFip1");
			servicioDiccionario = (ServicioDiccionariosLocal)icontext.lookup("java:global/UrbanismoEnRedV2/Urbanismoenred-serviciosV2/ServicioDiccionarios");
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
        
        String wsName = request.getParameter("wsName");
        String idioma = request.getParameter("idioma");
        if (wsName != null) {
            if (wsName.equals("GENERAR_FIP1")) {
                try {

                    String txtTramite = request.getParameter("tramite");

                    // Invocar al servicio de descarga de FIP
                    File f = gestorFip1.generarFip1(txtTramite, idioma != null ? idioma:"es");
                    
                    int length = 0;
                    ServletOutputStream op =  response.getOutputStream();

                    response.setContentType("application/octet-stream");
                    response.setContentLength((int) f.length());
                    response.setHeader("Content-Disposition", "attachment; filename=\""+f.getName() +"\"");

                    byte[] bbuf = new byte[1024 * 256];
                    DataInputStream in = new DataInputStream(new FileInputStream(f));
                    while ((in != null) && ((length = in.read(bbuf)) != -1)) { 
                    	op.write(bbuf, 0, length);
                    }
                    
                    in.close();
                    op.flush();
                    op.close();
                } catch (Exception e) {
                    response.getWriter().print("Error al invocar el refundido \n" + e.getMessage());
                }
            } else if (wsName.equalsIgnoreCase("VALIDAR_CP")) {
                this.validarCP(request, response);
            } else if (wsName.equalsIgnoreCase("DESCONECTAR")) {
            	request.getSession().setAttribute("logged-in", null);
            	response.sendRedirect("pages/login.jsp");
            }
        } else {
        	if (request.getSession().getAttribute("logged-in") != null 
        			&& "S".equals(request.getSession().getAttribute("logged-in"))) {
        		
        		request.getSession().setAttribute(TRAMITES, 
        			generarInformacionTramites((Integer)request.getSession().getAttribute("userid")
        				, idioma != null ? idioma:"es"));
        		
          		response.sendRedirect("pages/generaFip.jsp");
         	} else {
         		
         		HashMap<Integer, String> centros = new HashMap<Integer, String>();
         		
         		for(Centroproduccion cp : gestorFip1.getCentrosProduccion()) {
         			centros.put(cp.getIden(), servicioDiccionario.getTraduccion(Centroproduccion.class, cp.getIden(), "es"));
         		}
         		request.getSession().setAttribute(CENTROS_PRODUCCION, centros);
          		response.sendRedirect("pages/login.jsp");
         	}
        }
    }
    
    /**
     * 
     * @param idioma 
     * @param idUsuario
     * @return
     */
    private List<Map<String,Object>> generarInformacionTramites(int centroProduccion, String idioma) {
    	List<Map<String,Object>> resultado = new ArrayList<Map<String,Object>>();
    	Map<String,Object> columnas;
    	for (Tramite tramite : gestorFip1.getTramites(centroProduccion)) {
    		columnas = new HashMap<String, Object>();
    		columnas.put("codigoFip", tramite.getCodigofip());
    		columnas.put("tipo", servicioDiccionario.getTraduccion(Tipotramite.class, tramite.getIdtipotramite(), idioma));
    		columnas.put("plan", tramite.getPlan().getNombre());
    		columnas.put("nombre", tramite.getNombre() != null? tramite.getNombre():"");
    		columnas.put("iteracion", tramite.getIteracion());
    		columnas.put("fecha", tramite.getFecha() != null ? sdf.format(tramite.getFecha()):"");
    		
    		columnas.put("municipio", servicioDiccionario.getTraduccion(Ambito.class, tramite.getPlan().getIdambito(), idioma));
    		
    		resultado.add(columnas);
    	}
    	
    	return resultado;
    }

    /**
     * 
     * @param request
     * @param response
     * @throws IOException
     */
    private void validarCP(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String resultado = "N";
        try {

            String idioma = request.getParameter("idioma");
            String cprod = request.getParameter("cprod");
            String pass = request.getParameter("pass");

            if (cprod != null) {
            	Centroproduccion cp = gestorFip1.getCentroProduccion(Integer.valueOf(cprod));

            	if (cp != null) {
            		if (EncriptacionCodigoTramite.getEncoded(pass).equalsIgnoreCase(cp.getPasswordmd5())) {
            			resultado = "S";
            			HttpSession sess = request.getSession();
                        sess.setAttribute("logged-in", "S");
                        sess.setAttribute("language", idioma);
                        sess.setAttribute("login", servicioDiccionario.getTraduccion(Centroproduccion.class, cp.getIden(), idioma));
                        sess.setAttribute("userid", cp.getIden());
            		}
            	}
            }
        } catch (Exception e) {
            resultado = "N";
        } finally {
            response.getWriter().write(resultado);
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
}

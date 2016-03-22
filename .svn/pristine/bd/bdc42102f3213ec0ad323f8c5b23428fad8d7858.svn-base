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

import java.io.PrintWriter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;

import es.mitc.redes.urbanismoenred.servicios.comunes.EncriptacionCodigoTramite;
import es.mitc.redes.urbanismoenred.servicios.dal.ExcepcionPersistencia;
import es.mitc.redes.urbanismoenred.servicios.seguridad.Ambito;
import es.mitc.redes.urbanismoenred.servicios.seguridad.Rol;
import es.mitc.redes.urbanismoenred.servicios.seguridad.ServicioDiarioLocal;
import es.mitc.redes.urbanismoenred.servicios.seguridad.ServicioUsuariosLocal;
import es.mitc.redes.urbanismoenred.servicios.seguridad.TipoRol;
import es.mitc.redes.urbanismoenred.servicios.seguridad.TipoSubsistema;
import es.mitc.redes.urbanismoenred.servicios.seguridad.Usuario;

/**
 *
 * @author Arnaiz Consultores
 */
public class ActionServletSeguridad extends HttpServlet {
	
	private static final Logger log = Logger.getLogger(ActionServletSeguridad.class);

    /**
	 * 
	 */
	private static final long serialVersionUID = -4456745438060768782L;
	private static final String USUARIO = "usuario";
	
	private ServicioUsuariosLocal servicioUsuarios;
	
	private ServicioDiarioLocal servicioDiario;
	
	private Map<String,TipoRol> roles;
	
	private void allowedModule(HttpServletRequest request, HttpServletResponse response) throws IOException {
         
        ObjectMapper mapper = new ObjectMapper();
        
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
		
		Map<String,Object> permisos = new HashMap<String,Object>();
		List<Rol> roles = usuario.getRoles();
		
		for (Rol rol : roles) {
			permisos.put(rol.getCodigo(), "S");
		}
		
		mapper.writeValue(response.getWriter(), permisos);
    }

	private void checkIsAdminUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String resultado = "N";
        Usuario usuario = (Usuario) request.getAttribute(USUARIO);
        if (usuario != null && usuario.tieneRol(TipoRol.ADMINISTRADOR.getCodigo())) {
        	resultado = "S";
        }
        response.getWriter().print(resultado);
    }

    private void checkIsGestorPlanesUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String resultado = "N";
        
        Usuario usuario = (Usuario) request.getAttribute(USUARIO);
        if (usuario != null && usuario.tieneRol(TipoRol.GESTOR_PLAN.getCodigo())) {        
           resultado = "S";
        }
        
        response.getWriter().print(resultado);
    }

	private void disconnectUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
       request.getSession().invalidate();
       
       response.getWriter().print("true");
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

    private void getRolNeedsAmbito(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String res = "N";
        
        String rol = request.getParameter("rol");
            
        if (roles.containsKey(rol) && roles.get(rol).necesitaAmbito()) {
        	res = "S";
        }

        response.getWriter().print(res);
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
    	InitialContext icontext;
		try {
			icontext = new InitialContext();
			servicioUsuarios = (ServicioUsuariosLocal)icontext.lookup("java:global/UrbanismoEnRedV2/Urbanismoenred-serviciosV2/ServicioUsuarios");
			servicioDiario = (ServicioDiarioLocal)icontext.lookup("java:global/UrbanismoEnRedV2/Urbanismoenred-serviciosV2/ServicioDiario");
		} catch (NamingException e) {
			throw new ServletException("No se ha podido inicializar correctamente el servlet. Causa: " + e.getMessage());
		}
		
		roles = new HashMap<String, TipoRol>();
		roles.put(TipoRol.ADMINISTRADOR.getCodigo(),TipoRol.ADMINISTRADOR);
		roles.put(TipoRol.CONFIGURADOR.getCodigo(),TipoRol.CONFIGURADOR);
		roles.put(TipoRol.CONSOLA.getCodigo(),TipoRol.CONSOLA);
		roles.put(TipoRol.CONSOLIDACION.getCodigo(),TipoRol.CONSOLIDACION);
		roles.put(TipoRol.GESTOR_PLAN.getCodigo(),TipoRol.GESTOR_PLAN);
		roles.put(TipoRol.REFUNDIDO.getCodigo(),TipoRol.REFUNDIDO);
		roles.put(TipoRol.VALIDACION.getCodigo(),TipoRol.VALIDACION);
		roles.put(TipoRol.FICHAS.getCodigo(), TipoRol.FICHAS);
    }

    /**
     * 
     * @param usuario
     * @param response
     * @throws IOException
     */
    private void obtenerUsuario(Usuario usuario,
			HttpServletResponse response) throws IOException{
        
    	response.setContentType("application/x-json;charset=UTF-8");
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> map = new HashMap<String, Object>();
        if (usuario != null) {
             map.put("nombre", usuario.getNombre());
             map.put("correo", usuario.getCorreo());
             map.put("dni", usuario.getDni());
             List<Map<String,Object>> rolesJson = new ArrayList<Map<String,Object>>();
             map.put("roles", rolesJson);
             for (Rol rol : usuario.getRoles()) {
            	 Map<String, Object> rolAmbito = new HashMap<String, Object>();
            	 rolesJson.add(rolAmbito);
            	 rolAmbito.put("nombre", rol.getNombre());
            	 rolAmbito.put("codigo", rol.getCodigo());
            	 List<Map<String,Object>> ambitosJson = new ArrayList<Map<String,Object>>();
            	 rolAmbito.put("ambitos", ambitosJson);
            	 for (Ambito ambito : usuario.getAmbitos(rol.getCodigo())) {
            		 Map<String, Object> ambitoJson = new HashMap<String, Object>();
            		 ambitoJson.put("nombre", ambito.getNombre());
            		 ambitoJson.put("codigo", ambito.getCodigo());
            		 ambitoJson.put("tipo", ambito.getTipo());
            		 ambitosJson.add(ambitoJson);
            	 }
             }
        } else {
        	map.put("error", "No hay usuario registrado.");
        }
        mapper.writeValue(response.getWriter(), map);
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
        try {

            String wsName = request.getParameter("wsName");
            if (wsName != null) {
            	Usuario usuario = (Usuario) request.getSession().getAttribute(USUARIO);
            	if (usuario  != null ) {
            		if (wsName.equalsIgnoreCase("USUARIO_ESTA_LOGUEADO")) {
                        usuarioRegistrado(request, response);
                    } else if (wsName.equalsIgnoreCase("GET_USUARIO")) {
                        obtenerUsuario(usuario, response);
                    } else if (wsName.equalsIgnoreCase("CHECK_IS_ADMIN_USER")) {
                        checkIsAdminUser(request, response);
                    } else if (wsName.equalsIgnoreCase("CHECK_IS_GESTOR_PLANES_USER")) {
                        checkIsGestorPlanesUser(request, response);
                    } else if (wsName.equalsIgnoreCase("ALLOWED_MODULE")) {
                        this.allowedModule(request, response);
                    } else if(wsName.equalsIgnoreCase("GET_ROL_NEEDS_AMBITO")){
                        this.getRolNeedsAmbito(request, response);
                    } else if(wsName.equalsIgnoreCase("DISCONNECT_USER")){
                        this.disconnectUser(request, response);
                    }
                } else {
                	if (wsName.equalsIgnoreCase("VALIDAR_USUARIO")) {
                        validarUsuario(request, response);
                    } else if (wsName.equalsIgnoreCase("USUARIO_ESTA_LOGUEADO")) {
                        usuarioRegistrado(request, response);
                    } else if (wsName.equalsIgnoreCase("GET_USUARIO")) {
                        obtenerUsuario(usuario, response);
                    }else {
                    	out.print("Usuario no registrado");
                        return;
                    }
                }
            }
            
        } finally {
            out.close();
        }
    }

    private void usuarioRegistrado(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String resultado = "N";
        if (request.getSession().getAttribute(USUARIO) != null) {
            resultado = "S";
        }
        response.getWriter().print(resultado);
    }

    /**
     * Comprueba si el usuario se ha registrado correctamente o no.
     * 
     * @param request
     * @param response
     * @throws IOException
     */
    private void validarUsuario(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        String username = request.getParameter("USUARIO");
        String password = request.getParameter("PASSWD");
        String dni = request.getParameter("DNI");
        String resultado = "";

        Usuario usuario = null;
        
        if (dni == null) {
        	usuario = servicioUsuarios.getUsuario(username);
        	if (usuario != null && !usuario.getClave().equalsIgnoreCase(EncriptacionCodigoTramite.getEncoded(password))) {
        		resultado = "Error: Contraseña incorrecta para usuario " + username;
        		usuario = null;
        	}
        } else {
        	usuario = servicioUsuarios.getUsuarioPorDNI(dni);
        }
        
        session.setAttribute("usuario", usuario);
        
        if (usuario != null) {
        	
        	session.setAttribute("login", usuario.getNombre());
        	session.setAttribute("logged-in", "S");
        	session.setAttribute("login-timestamp", System.currentTimeMillis());
        	session.setMaxInactiveInterval(-1);
                
        	try {
				servicioDiario.nuevoRegistroDiario(usuario.getNombre(), TipoSubsistema.LOGIN, "Login en el sistema. Usuario " + usuario.getNombre());
			} catch (ExcepcionPersistencia e) {
				log.warn("No se ha podido registrar el acceso del usuario " + usuario.getNombre());
			}
                
        	resultado = "OK";
        } else {
        	if (resultado.isEmpty()) {
        		resultado = "No se ha encontrado el usuario.";
        	} 
        }
            
        response.getWriter().print(resultado);
    }
}

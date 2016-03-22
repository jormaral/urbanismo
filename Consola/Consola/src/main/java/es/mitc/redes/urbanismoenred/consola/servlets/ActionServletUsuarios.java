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

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import es.mitc.redes.urbanismoenred.servicios.comunes.EncriptacionCodigoTramite;
import es.mitc.redes.urbanismoenred.servicios.dal.ExcepcionPersistencia;
import es.mitc.redes.urbanismoenred.servicios.seguridad.Ambito;
import es.mitc.redes.urbanismoenred.servicios.seguridad.Rol;
import es.mitc.redes.urbanismoenred.servicios.seguridad.ServicioDiarioLocal;
import es.mitc.redes.urbanismoenred.servicios.seguridad.ServicioUsuariosLocal;
import es.mitc.redes.urbanismoenred.servicios.seguridad.TipoRol;
import es.mitc.redes.urbanismoenred.servicios.seguridad.Usuario;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;


/**
 *
 * @author Arnaiz Consultores
 */
public class ActionServletUsuarios extends HttpServlet {

    /**
	 * 
	 */
	private static final long serialVersionUID = 5409318104528830806L;
    
    private ServicioUsuariosLocal servicioUsuarios;
    
	private ServicioDiarioLocal servicioDiario;
	
	
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
        
        if (usuario != null && usuario.tieneRol(TipoRol.ADMINISTRADOR.getCodigo())) {
            

            String wsName = request.getParameter("wsName");
            if ("FORM_USUARIO".equalsIgnoreCase(wsName)) {
                formUsuario(request, response);
            } else if ("GET_LISTA_USUARIOS".equalsIgnoreCase(wsName)) {
                getListaUsuarios(request, response);
            } else if ("GUARDAR_USUARIO".equalsIgnoreCase(wsName)) {
                guardarUsuario(usuario, request, response);
            } else if ("GET_LISTA_ROLES_USUARIO".equalsIgnoreCase(wsName)) {
                getListaRolesUsuario(request, response);
            } else if ("GET_LISTA_AMBITOS_POR_NOMBRE".equalsIgnoreCase(wsName)) {
                getAmbitosPorNombre(request, response);
            } else if ("GET_AMBITO_ID".equalsIgnoreCase(wsName)) {
                getAmbitoId(request, response);
            } else if ("EXISTE_NOMBRE_USUARIO".equalsIgnoreCase(wsName)) {
                existeNombreUsuario(request, response);
            } else if ("GET_DATOS_USUARIO_JSON".equalsIgnoreCase(wsName)) {
                getDatosUsuarioJson(request, response);
            } else if ("BORRAR_USUARIO".equalsIgnoreCase(wsName)) {
                borrarUsuario(request, response);
            } 
        } else {
        	out.print("'El usuario no tiene permisos para ejecutar esta acción'");
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
        return "Servlet de gestión de usaurios";
    }// </editor-fold>

    // <editor-fold desc="Gestión de Usuarios">
    private void formUsuario(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        /*
         * Redireccionar a lanzaderas/gestionusuarios/frmUsuario.jsp
         *
         * Parámetro: "idUsuario"
         */
        getServletContext().getRequestDispatcher("/lanzaderas/gestionusuarios/formUsuario.jsp").forward(request, response);
    }

    /**
     * 
     * @param request
     * @param response
     * @throws IOException 
     * @throws JsonMappingException 
     * @throws JsonGenerationException 
     */
    private void getListaUsuarios(HttpServletRequest request, HttpServletResponse response) throws IOException {
        
        Usuario[] usuarios = servicioUsuarios.getUsuarios();
           
        response.setContentType("application/x-json;charset=UTF-8");
        ObjectMapper mapper = new ObjectMapper();
        List<Object> rowlist = new ArrayList<Object>();
        Map<String, Object> row;
        for (int i = 0; i < usuarios.length; i++) {
          row = new HashMap<String, Object>();
          row.put("idUsuario", usuarios[i].getIdentificador());
          row.put("nombreUsuario", usuarios[i].getNombre());
          rowlist.add(row);
        }
        
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("data", rowlist);
        mapper.writeValue(response.getWriter(), data);
    }

    /**
     * 
     * @param usuario 
     * @param request
     * @param response
     * @throws IOException 
     */
	private void guardarUsuario(Usuario usuario, HttpServletRequest request, HttpServletResponse response) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> resultado = new HashMap<String, Object>();
        
        try {
        	resultado.put("accion", "guardarUsuario");
        	
            if (request.getParameter("userData") != null) {
            	Usuario nuevoUsuario = generarUsuario(request.getParameter("userData"));
            	
                
                String accion = "Guardado datos de usuario: " + nuevoUsuario.getNombre();
                
                servicioUsuarios.guardarUsuario(nuevoUsuario);
                
                Map<String, Object> data = new HashMap<String, Object>();
            	data.put("isNew", true);
                data.put("idUsuario", usuario.getIdentificador());
                data.put("dni", usuario.getDni());
                data.put("nombre", usuario.getNombre());
                data.put("correo", usuario.getCorreo());
                data.put("roles", generarJsonRoles(usuario));
              
                resultado.put("usuario", data);
                
                if (usuario.getNombre().equals(nuevoUsuario)) {
                	request.getSession().setAttribute("usuario", nuevoUsuario);
                }
                
                servicioDiario.nuevoRegistroDiario(nuevoUsuario.getNombre(), es.mitc.redes.urbanismoenred.servicios.seguridad.TipoSubsistema.GESTION_USUARIOS, accion);           
            } else {
            	resultado.put("error", "No se han indicado los datos del usuario");
            }
        } catch (Exception ex) {
        	resultado.put("error", ex.getMessage());
        }
        
        mapper.writeValue(response.getWriter(), resultado);
    }
	
	/**
	 * Devuelve la lista de roles de un usuario en formato JSON siguiendo el 
	 * siguiente esquema:
	 * [
     * 		{
     * 			idRol: 
     * 			nombreRol:
     * 			idAmbito:
     * 			nombreAmbito:
     * 			idTipoAmbito:
     * 			nombreTipoAmbito:
     * 		},
     * 		...
     * 	]
     * 
	 * @param usuario
	 * @return
	 */
	private List<Object> generarJsonRoles(Usuario usuario) {
		List<Rol> roles = usuario.getRoles();
		List<Object> rolesJson = new ArrayList<Object>();
        Map<String, Object> rolJson;
        for (Rol rol: roles) {
        	Ambito[] ambitos = usuario.getAmbitos(rol.getCodigo());
        	if (ambitos.length > 0) {
        		for (int i = 0; i < ambitos.length; i++) {
            		rolJson = new HashMap<String, Object>();
            		rolJson.put("idRol", rol.getCodigo());
            		rolJson.put("nombreRol", rol.getNombre());
            		rolJson.put("idAmbito", ambitos[i].getCodigo());
            		rolJson.put("nombreAmbito", ambitos[i].getNombre());
            		rolJson.put("idTipoAmbito", ambitos[i].getIdTipo());
            		rolJson.put("nombreTipoAmbito", ambitos[i].getTipo());
            		
            		rolesJson.add(rolJson);
            	}
        	} else {
        		rolJson = new HashMap<String, Object>();
        		rolJson.put("idRol", rol.getCodigo());
        		rolJson.put("nombreRol", rol.getNombre());
        		rolJson.put("idAmbito", null);
        		rolJson.put("nombreAmbito", null);
        		rolesJson.add(rolJson);
        	}
        	
        }
        return rolesJson;
	}

	/**
	 * 
	 * @param datos
	 * @return
	 * @throws Exception
	 */
    @SuppressWarnings("unchecked")
	private Usuario generarUsuario(String datos) throws Exception{
    	ObjectMapper mapper = new ObjectMapper();

        Map<String, Object> userData = new HashMap<String, Object>();
        userData = mapper.readValue(datos, Map.class);
       
        
        Map<String,Rol> listadoRoles = new HashMap<String, Rol>();
		Map<Rol, List<Ambito>> roles = new HashMap<Rol, List<Ambito>>();
		List<Map<String,Object>> listaRoles = (List<Map<String,Object>>)userData.get("roles");
		Rol rol;
		List<Ambito> ambitos;
		
		for (Map<String,Object> rolJson : listaRoles) {
			if (listadoRoles.containsKey(rolJson.get("idRol").toString())) {
				rol = listadoRoles.get(rolJson.get("idRol").toString());
				ambitos = roles.get(rol);
			} else {
				rol = new Rol(rolJson.get("nombreRol").toString(),rolJson.get("idRol").toString());
				listadoRoles.put(rol.getNombre(), rol);
				ambitos = new ArrayList<Ambito>();
				roles.put(rol,ambitos);
			}
			
			if (rolJson.get("idAmbito") != null) {
				ambitos.add(new Ambito(Integer.parseInt(rolJson.get("idAmbito").toString()),rolJson.get("nombreAmbito").toString(), (Integer)rolJson.get("idTipoAmbito"), null));
			}
			
		}
        
		boolean nuevo = Boolean.parseBoolean(userData.get("isNew").toString());
        if (nuevo) {
        	if (userData.get("password") != null) {
            	if (userData.get("password").equals(userData.get("passwordConfirm"))) {
            		return new Usuario(
    						-1,
    						(String)userData.get("nombre"), 
    						EncriptacionCodigoTramite.getEncoded((String)userData.get("password")), 
    						(String)userData.get("correo"), 
    						(String)userData.get("DNI"), 
            				roles );
            	} else {
            		throw new Exception("Las contraseñas deben ser iguales.");
            	}
            } else {
            	throw new Exception("Contraseña vacía.");
            }
            	
        } else {
        	String password;
        	if (userData.get("password") != null) {
            	if (userData.get("password").equals(userData.get("passwordConfirm"))) {
            		password = EncriptacionCodigoTramite.getEncoded((String)userData.get("password"));
            	} else {
            		throw new Exception("Las contraseñas deben ser iguales.");
            	}
            } else {
            	Usuario usuario = servicioUsuarios.getUsuario(Integer.parseInt(userData.get("id").toString()));
            	if (usuario != null) {
            		password = usuario.getClave();
            	} else {
            		throw new Exception("No existe usuario con id " + userData.get("id"));
            	}
            }
        	return new Usuario(
					Integer.parseInt(userData.get("id").toString()),
					(String)userData.get("nombre"), 
					password, 
					(String)userData.get("correo"), 
					(String)userData.get("DNI"), 
    				roles );
        }     
	}

	/**
	 * Devuelve la lista de roles de un usuario en formato JSON siguiendo el 
	 * siguiente esquema:
	 * [
     * 		{
     * 			idRol: 
     * 			nombreRol:
     * 			idAmbito:
     * 			nombreAmbito:
     * 		},
     * 		...
     * 	]
	 * 
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
    private void getListaRolesUsuario(HttpServletRequest request, HttpServletResponse response) throws IOException {
    	ObjectMapper mapper = new ObjectMapper();
    	if (request.getParameter("idUsuario") != null) {
    		Usuario usuario = servicioUsuarios.getUsuario(Integer.parseInt(request.getParameter("idUsuario")));
    		
    		mapper.writeValue(response.getWriter(), generarJsonRoles(usuario));
        } else {
        	Map<String, Object> data = new HashMap<String, Object>();
        	data.put("error", "No se ha indicado el idUsuario");	
        }
    }
    // </editor-fold>

    /**
     * Devuelve una lista de ámbitos por nombre en formato JSON.
     * 
     * @param request
     * @param response
     * @throws IOException 
     */
    private void getAmbitosPorNombre(HttpServletRequest request, HttpServletResponse response) throws IOException {
    	
    	String idioma = request.getParameter("idioma");
        String nombre = request.getParameter("nombre");
        ObjectMapper mapper = new ObjectMapper();
        response.setHeader("Content-type", "application-json");
        
        if (nombre != null &&
        	idioma != null) {
        	 nombre = nombre.toLowerCase()+"%";

             es.mitc.redes.urbanismoenred.servicios.seguridad.Ambito[] ambitos = servicioUsuarios.getAmbitos(nombre, idioma);
                 
             List<Object> rowlist = new ArrayList<Object>();

             // TODO jgarzon revisar los datos devueltos, no me convence esto de concatenar.
             for (int i = 0; i < ambitos.length; i++) {
             	rowlist.add(ambitos[i].getNombre() + " - " + ambitos[i].getTipo());
             }

             mapper.writeValue(response.getWriter(), rowlist);
           
        } else {
        	Map<String, Object> data = new HashMap<String, Object>();
        	data.put("error", "No se ha indicado el nombre o el idioma");
        	mapper.writeValue(response.getWriter(), data);
        }       
    }

    /**
     * 
     * @param request
     * @param response
     * @throws IOException 
     */
    private void getAmbitoId(HttpServletRequest request, HttpServletResponse response) throws IOException {
    	// TODO jgarzon este método parece redundante con getAmbitosPorNombre. Revisar y si corresponde eliminar. Lo más probable es que ya tengan el id en la nueva versión.
    	
    	String idioma = request.getParameter("idioma");
        String nombre = request.getParameter("nombre");
        
        ObjectMapper mapper = new ObjectMapper();
        if (nombre != null &&
    		   idioma != null) {
        	
        	nombre = nombre.toLowerCase();
            String tipoAmbito = request.getParameter("tipoambito");
            
            es.mitc.redes.urbanismoenred.servicios.seguridad.Ambito[] ambitos = servicioUsuarios.getAmbitos(nombre, idioma);
            
            if (ambitos.length >0) {
            	 response.setHeader("Content-type", "application-json");
                 
                 if (tipoAmbito == null) {
                	 mapper.writeValue(response.getWriter(), ambitos[0].getCodigo());
                 } else {
                	tipoAmbito = tipoAmbito.toLowerCase();
                	for (int i = 0; i < ambitos.length; i++) {
                		if (tipoAmbito.equalsIgnoreCase(ambitos[i].getTipo())) {
                			mapper.writeValue(response.getWriter(), ambitos[i].getCodigo());
                			break;
                		}
                	}
                 	
                 }
            } else {
            	Map<String, Object> data = new HashMap<String, Object>();
            	data.put("error", "No se ha obtenido ningún ámbito con nombre" + request.getParameter("nombre"));
            	mapper.writeValue(response.getWriter(), data);
            }
    	  
    	  
        } else {
        	Map<String, Object> data = new HashMap<String, Object>();
        	data.put("error", "No se ha indicado el nombre o el idioma");
        	mapper.writeValue(response.getWriter(), data);
        }
    }

    /**
     * 
     * @param request
     * @param response
     * @throws IOException 
     */
    private void existeNombreUsuario(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (request.getParameter("username") == null) {
        	String result = "N";
            String username = request.getParameter("username");
           
            if (servicioUsuarios.existeNombre(username)) {
                result = "S";
            }
            response.getWriter().print(result);
        } else {
        	response.getWriter().print("No se ha indicado el nombre");
        }
            
    }

    /**
     * Devuelve los datos de un usuario en formato JSON siguiendo la siguiente 
     * estructura.
     * 
     * {
     * 	isNew : (true|false)
     *  idusuario:
     * 	nombre:
     * 	correo:
     * 	roles: [
     * 		{
     * 			idRol: 
     * 			nombreRol:
     * 			idAmbito:
     * 			nombreAmbito:
     * 		},
     * 		...
     * 	]
     * }
     * @param request
     * @param response
     * @throws IOException 
     */
    private void getDatosUsuarioJson(HttpServletRequest request, HttpServletResponse response) throws IOException {  
    	ObjectMapper mapper = new ObjectMapper();
    	Map<String, Object> data = new HashMap<String, Object>();
    	
        if (request.getParameter("idUsuario") != null) {
            Usuario usuario = servicioUsuarios.getUsuario(Integer.parseInt(request.getParameter("idUsuario")));
            	
            if (usuario != null) {
                data.put("isNew", false);
                data.put("idUsuario", usuario.getIdentificador());
                data.put("dni", usuario.getDni());
                data.put("nombre", usuario.getNombre());
                data.put("correo", usuario.getCorreo());
                data.put("roles", generarJsonRoles(usuario));
                
                mapper.writeValue(response.getWriter(), data); 
            } else {
            	data.put("error", "No se ha encontrado usuario " + request.getParameter("idUsuario")); 
            }
        } else {
        	data.put("error", "No se ha indicado el identificador del usuario");
        }    
        
        response.setHeader("Content-type", "application-json");
        mapper.writeValue(response.getWriter(), data); 
    }

    /**
     * 
     * @param request
     * @param response
     * @throws IOException
     */
    private void borrarUsuario(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (request.getParameter("idUsuario") != null) {
        	try {
        		servicioUsuarios.borrarUsuario(Integer.parseInt(request.getParameter("idUsuario")));
        		response.getWriter().print("OK");
			} catch (ExcepcionPersistencia e) {
				response.getWriter().print(e.getMessage());
			}
        } else {
        	response.getWriter().print("No se ha indicado el id del usuario a borrar");
        }
    }
}


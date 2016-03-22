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

import es.mitc.redes.urbanismoenred.data.rpm.diccionario.Ambito;
import es.mitc.redes.urbanismoenred.servicios.configuracion.ConfiguracionVisorLocal;
import es.mitc.redes.urbanismoenred.servicios.planeamiento.ServicioDiccionariosLocal;
import es.mitc.redes.urbanismoenred.servicios.seguridad.TipoRol;
import es.mitc.redes.urbanismoenred.servicios.seguridad.Usuario;
import es.mitc.redes.urbanismoenred.utils.recursos.textos.Textos;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
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

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.apache.log4j.Logger;

/**
 *
 * @author Arnaiz Consultores
 */
public class ActionServletConfig extends HttpServlet {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1853816485677419743L;
	private static final Logger log = Logger.getLogger(ActionServletConfig.class);
	private ConfiguracionVisorLocal configuradorVisor;
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
			configuradorVisor = (ConfiguracionVisorLocal)icontext.lookup("java:global/UrbanismoEnRedV2/Urbanismoenred-serviciosV2/ConfiguradorVisor!es.mitc.redes.urbanismoenred.servicios.configuracion.ConfiguracionVisorLocal");
			servicioDiccionario = (ServicioDiccionariosLocal)icontext.lookup("java:global/UrbanismoEnRedV2/Urbanismoenred-serviciosV2/ServicioDiccionarios!es.mitc.redes.urbanismoenred.servicios.planeamiento.ServicioDiccionariosLocal");
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
        
        
        //comprobación de roles
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
        
        if (request.getSession().getAttribute("usuario") != null) {
        	if (usuario.tieneRol(TipoRol.CONSOLA.getCodigo()) || 
        			usuario.tieneRol(TipoRol.CONFIGURADOR.getCodigo()) ||
        			usuario.tieneRol(TipoRol.VALIDACION.getCodigo())) {
        		String wsName = request.getParameter("wsName");
        		if (wsName != null) {
        			if (wsName.equals("GET_LAYERCONFIG_DE_AMBITO")) {
        	            getLayerConfigDeAmbito(request, response, usuario);
        	        } else if (wsName.equals("SAVE_LAYERCONFIG_DE_AMBITO")) {
        	            saveLayerConfigDeAmbito(request, response, usuario);
        	        } else if (wsName.equals("CROSS-DOMAIN")) {
        	            leerURL(request, response);
        	        } else if (wsName.equals("GET_WMS_SERVER_DE_ENT")) {
        	            getWMSserverDeEnt(request, response);
        	        } else if (wsName.equals("GET_TEXTO_SEGUN_IDIOMA")) {
        	            getTextoSegunIdioma(request, response);
        	        } else if (wsName.equals("GET_AMBITOS_CONFIGURACION")) {
        	        	getAmbitosConfiguracion(request, response, usuario);
        	        }
        		}
        	} else {
        		response.getWriter().print("'El usuario no tiene permisos para ejecutar esta acción'");
        	}
        } else {
        	getServletContext().getRequestDispatcher("/").forward(request, response);
        }
    }
    
    protected void getAmbitosConfiguracion(HttpServletRequest request, HttpServletResponse response, Usuario usuario) throws JsonGenerationException, JsonMappingException, IOException {
    	response.setContentType("application-json;charset=UTF-8");
    	Map<String, Object> data = new HashMap<String, Object>();
		
		List<Map<String, Object>> lista = new ArrayList<Map<String, Object>>();
		
		Map<String, Object> elemento;
		for (es.mitc.redes.urbanismoenred.servicios.seguridad.Ambito ambito : usuario.getAmbitos(TipoRol.CONFIGURADOR.getCodigo())) {
			elemento = new HashMap<String, Object>();
			elemento.put("idAmbito", ambito.getCodigo());
			elemento.put("nombre", servicioDiccionario.getTraduccion(Ambito.class, ambito.getCodigo(), "es"));
			lista.add(elemento);
		}
		
		data.put("data", lista);
		data.put("pages", 1);
		data.put("total", lista.size());
		
		ObjectMapper om = new ObjectMapper();
		om.writeValue(response.getWriter(),data);
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
     * 
     * @param request
     * @param response
     * @param usuario 
     * @throws ServletException
     * @throws IOException
     */
    private void getLayerConfigDeAmbito(HttpServletRequest request, HttpServletResponse response, Usuario usuario) throws ServletException, IOException {
    	Map<String, Object> datos = new HashMap<String, Object>();
    	response.setContentType("application-json;charset=UTF-8");
    	if (request.getParameter("idAmbito") != null) {
    		int idAmbito;
            try {
                idAmbito = Integer.valueOf(request.getParameter("idAmbito"));
                InputStream configIS = configuradorVisor.getLayerConfigDeAmbito(idAmbito);
                if (configIS != null) {

                    InputStreamReader isr = new InputStreamReader(configIS);
                    BufferedReader in = new BufferedReader(isr);
                    String str;
                    StringBuffer sb = new StringBuffer();
                    while ((str = in.readLine()) != null) {
                    	sb.append(str);
                    }
                    datos.put("xml", sb.toString());
                } else {
                	log.warn("No se ha podido encontrar la configuración para el ámbito solicitado " + idAmbito);
                	datos.put("codigoError", 1);
                	datos.put("error", "No se ha podido encontrar la configuración para el ámbito solicitado");
                }
            } catch (Exception e) {
                log.error("El parámetro idAmbito debe tener valor numérico", e);
                datos.put("codigoError", 2);
                datos.put("error", "El parámetro idAmbito debe tener valor numérico");
            }   
        } else {
        	datos.put("codigoError", 3);
            datos.put("error", "Parámetros de llamada incompletos");
        }
    	
    	ObjectMapper om = new ObjectMapper();
    	om.writeValue(response.getOutputStream(), datos);
    }

    /**
     * 
     * @param request
     * @param response
     * @param usuario 
     * @throws IOException
     */
    private void saveLayerConfigDeAmbito(HttpServletRequest request, HttpServletResponse response, Usuario usuario) throws IOException {
    	if (request.getParameter("idAmbito") != null 
        		&& request.getParameter("configXML") != null) {
    		int idAmbito;
            try {
                idAmbito = Integer.valueOf(request.getParameter("idAmbito"));

                if (configuradorVisor.guardarConfiguracion(idAmbito, request.getParameter("configXML"))) {
                	response.getWriter().print("Configuración guardada con éxito");
                } else {
                    response.getWriter().print("Error al guardar la configuración");
                }
            } catch (IOException e) {
                log.error("[ActionServletConfig.saveLayerconfigDeAmbito] Error al acceder al fichero de configuración", e);
                response.getWriter().print("Error al acceder al fichero de configuración" + e.getMessage());
                return;
            } catch (Exception e) {
                log.error("El parámetro idAmbito debe tener valor numérico", e);
                response.getWriter().print("El parámetro idAmbito debe tener valor numérico");
                return;
            }
        } else {
        	response.getWriter().print("Parámetros de llamada incompletos");
            log.warn("Parámetros de llamada incompletos");
        }
        
    }

    /**
     * 
     * @param request
     * @param response
     * @throws IOException
     */
    private void leerURL(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();
        response.setContentType("text/html;charset=UTF-8");
        try {
            String urlString = request.getParameter("url");
            String estado = "error COMIENZO - " + urlString;
            if (urlString != null && !urlString.equalsIgnoreCase("")) {
                try {
                    //Indicamos la URL donde nos conectamos
                    URL url = new URL(urlString);
                    url.openStream();
                    //Buffer con los datos recibidos
                    BufferedReader in = null;
                    try {
                        // Volcamos lo recibido al buffer
                        in = new BufferedReader(new InputStreamReader(url.openStream()));
                        //Transformamos el contenido del buffer a texto
                        String inputLine;
                        estado = "";
                        //Mientras haya cosas en el buffer las volcamos a las cadenas de texto
                        while ((inputLine = in.readLine()) != null) {
                            estado = estado + inputLine;
                        }
                        in.close();
                    } catch (Throwable t) {
                        estado = "ERROR.BufferedReader." + t.getMessage();
                    }
                } catch (MalformedURLException me) {
                    estado = "ERROR.URL." + me.getMessage();
                } catch (IOException ioe) {
                    estado = "Error.IO." + ioe.getMessage();
                }
                out.print(estado);
            }
        } catch (Exception e) {
            response.getWriter().print("Error inesperado " + e.getMessage());
        } finally {
            out.close();
        }
    }

   /**
    * 
    * @param request
    * @param response
    * @throws IOException
    */
    private void getTextoSegunIdioma(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();
        try {
            String texto = request.getParameter("texto");
            String idioma = request.getParameter("idioma");
            out.print(Textos.getTexto(idioma, texto));
        } catch (Exception e) {
            response.getWriter().print("Error inesperado " + e.getMessage());
        } finally {
            out.close();
        }
    }

    /**
     * 
     * @param request
     * @param response
     * @throws IOException
     */
    private void getWMSserverDeEnt(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            String zona = request.getParameter("zona");
            if (zona == null) {
                zona="RPM";
            }

            ObjectMapper mapper = new ObjectMapper();

           
            String url="", layers="",format = "";
            Map<String, String> data = new HashMap<String, String>();


//            if (zona.equalsIgnoreCase("RPM")){
                url = Textos.getTexto("visorConsola", "WMS.ENTIDADES_RPM.url" );
                layers = Textos.getTexto("visorConsola", "WMS.ENTIDADES_RPM.layers");
                format = Textos.getTexto("visorConsola", "WMS.ENTIDADES_RPM.format");
//            } else if (zona.equalsIgnoreCase("VAL")){
//                url = Textos.getTexto("visorConsola", "WMS.ENTIDADES_VAL.url" );
//                layers = Textos.getTexto("visorConsola", "WMS.ENTIDADES_VAL.layers");
//                format = Textos.getTexto("visorConsola", "WMS.ENTIDADES_VAL.format");
//            } else {
//                mapper.writeValue(response.getWriter(),"Zona no reconocida");
//                return;
//            }
            data.put("url", url);
            data.put("layers", layers);
            data.put("format", format);
            mapper.writeValue(response.getWriter(), data);

        } catch (Exception e) {
            log.error("[getWMSserverDeEnt",e);
            response.getWriter().print("Error inesperado " + e.getMessage());
        }
    }
}

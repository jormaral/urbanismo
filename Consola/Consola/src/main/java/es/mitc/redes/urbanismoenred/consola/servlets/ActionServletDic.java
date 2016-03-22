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
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;

import es.mitc.redes.urbanismoenred.data.rpm.diccionario.Boletin;
import es.mitc.redes.urbanismoenred.data.rpm.diccionario.Caracterdeterminacion;
import es.mitc.redes.urbanismoenred.data.rpm.diccionario.Centroproduccion;
import es.mitc.redes.urbanismoenred.data.rpm.diccionario.Grupodocumento;
import es.mitc.redes.urbanismoenred.data.rpm.diccionario.Instrumentoplan;
import es.mitc.redes.urbanismoenred.data.rpm.diccionario.Instrumentotipooperacionplan;
import es.mitc.redes.urbanismoenred.data.rpm.diccionario.Naturaleza;
import es.mitc.redes.urbanismoenred.data.rpm.diccionario.Operacioncaracter;
import es.mitc.redes.urbanismoenred.data.rpm.diccionario.Organo;
import es.mitc.redes.urbanismoenred.data.rpm.diccionario.Tipodocumento;
import es.mitc.redes.urbanismoenred.data.rpm.diccionario.Tipooperaciondeterminacion;
import es.mitc.redes.urbanismoenred.data.rpm.diccionario.Tipooperacionplan;
import es.mitc.redes.urbanismoenred.data.rpm.diccionario.Tipotramite;
import es.mitc.redes.urbanismoenred.servicios.dal.ExcepcionPersistencia;
import es.mitc.redes.urbanismoenred.servicios.planeamiento.ExcepcionDiccionario;
import es.mitc.redes.urbanismoenred.servicios.planeamiento.ServicioDiccionariosLocal;
import es.mitc.redes.urbanismoenred.servicios.seguridad.ServicioDiarioLocal;
import es.mitc.redes.urbanismoenred.servicios.seguridad.TipoRol;
import es.mitc.redes.urbanismoenred.servicios.seguridad.Usuario;
import es.mitc.redes.urbanismoenred.utils.recursos.textos.Textos;

/**
 *
 * @author Arnaiz Consultores
 */
public class ActionServletDic extends HttpServlet {

    /**
	 * 
	 */
	private static final long serialVersionUID = -5833819097557010632L;
	
	private ServicioDiarioLocal servicioDiario;
	private ServicioDiccionariosLocal servicioDiccionario;
	
	
	/**
	 * 
	 * @param request
	 * @param response
	 * @param usuario
	 * @throws IOException
	 */
    private void addItemDiccionario(HttpServletRequest request, HttpServletResponse response, String usuario) throws IOException {

        String datos = request.getParameter("data");
        String tipo = request.getParameter("tipo");
        String idioma = request.getParameter("idioma");
        
        if (datos != null &&
            	tipo != null &&
            	idioma != null) {
        	ObjectMapper mapper = new ObjectMapper();
            @SuppressWarnings("unchecked")
			Map<String, Object> data = mapper.readValue(datos, Map.class);
            try {
	            if (tipo.equalsIgnoreCase("TipoTramite")) {	
	            	servicioDiccionario.crearTipoTramite(data.get("nombre").toString(), "Descripcion de " + data.get("nombre"), idioma);
	            } else if (tipo.equalsIgnoreCase("Boletin")) {
	            	servicioDiccionario.crearBoletin(data.get("nombre").toString(), "Descripcion de " + data.get("nombre"), idioma);
	            } else if (tipo.equalsIgnoreCase("Caracterdeterminacion")) {
	            	servicioDiccionario.crearCaracterDeterminacion(data.get("nombre").toString(),
	            		data.get("nminaplicaciones") != null ? Integer.parseInt(data.get("nminaplicaciones").toString()): null,
	            		data.get("nmaxaplicaciones") != null ? Integer.parseInt(data.get("nmaxaplicaciones").toString()):null,
	            		"Descripcion de " + data.get("nombre"), 
	            		idioma);
	            } else if (tipo.equalsIgnoreCase("Centroproduccion")) {
	                servicioDiccionario.crearCentroProduccion(data.get("nombre").toString(), 
	                		data.get("mail") != null ? data.get("mail").toString():null, 
	                		data.get("password") != null ?data.get("password").toString():null, 
	                		"Descripcion de " + data.get("nombre"), 
	                		idioma);
	            } else if (tipo.equalsIgnoreCase("Grupodocumento")) {
	                servicioDiccionario.crearGrupoDocumento(data.get("nombre").toString(),
	                		"Descripcion de " + data.get("nombre"), idioma);
	            } else if (tipo.equalsIgnoreCase("Instrumentoplan")) {
	                servicioDiccionario.crearInstrumentoPlan(data.get("nombre").toString(), 
	                		data.get("nemo") != null ? data.get("nemo").toString():null,
	                				"Descripcion de " + data.get("nombre"), 
	                				idioma);
	            } else if (tipo.equalsIgnoreCase("Instrumentotipooperacion")) {
	            	if (data.containsKey("tipooperacionplan") && data.containsKey("instrumentoplan")) {
	            		servicioDiccionario.crearInstrumentoTipoOperacionPlan(Integer.parseInt(data.get("tipooperacionplan").toString()),
	            				Integer.parseInt(data.get("instrumentoplan").toString()));
	            	} else {
	            		response.getWriter().print("Parámetros de llamada incompletos");
	            	}
	                
	            } else if (tipo.equalsIgnoreCase("Naturaleza")) {
	                servicioDiccionario.crearNaturaleza(data.get("nombre").toString(),
	                		"Descripcion de " + data.get("nombre"), 
	                		idioma);
	            } else if (tipo.equalsIgnoreCase("Operacioncaracter")) {
	            	if (data.containsKey("idCaracterOperado") 
	            			&& data.containsKey("idCaracterOperador") 
	            			&& data.containsKey("idTipoOperacionDet")) {
	            		servicioDiccionario.crearOperacionCaracter(Integer.parseInt(data.get("idCaracterOperado").toString()),
	            				Integer.parseInt(data.get("idCaracterOperador").toString()), 
	            				Integer.parseInt(data.get("idTipoOperacionDet").toString()));
	            	} else {
	            		response.getWriter().print("Parámetros de llamada incompletos");
	            	}
	            } else if (tipo.equalsIgnoreCase("Organo")) {
	            	servicioDiccionario.crearOrgano(data.get("nombre").toString(),
	                		"Descripcion de " + data.get("nombre"), 
	                		idioma);
	            } else if (tipo.equalsIgnoreCase("Tipodocumento")) {
	            	servicioDiccionario.crearTipodocumento(data.get("nombre").toString(),
	                		"Descripcion de " + data.get("nombre"), 
	                		idioma);
	            } else if (tipo.equalsIgnoreCase("Tipooperacionplan")) {
	            	servicioDiccionario.crearTipoOperacionPlan(data.get("nombre").toString(),
	                		"Descripcion de " + data.get("nombre"), 
	                		idioma);
	            }
	
	            servicioDiario.nuevoRegistroDiario(usuario, es.mitc.redes.urbanismoenred.servicios.seguridad.TipoSubsistema.GESTION_DICCIONARIO, "Creado en la tabla " + tipo + " el elemento " + data);
	            // LOW jgarzon con esto el javascript no sabe de manera clara si ha ido bien o mal. Revisar.
	            response.getWriter().print("Elemento creado");
            } catch (ExcepcionDiccionario ed) {
            	response.getWriter().print(ed.getMessage());
            } catch (ExcepcionPersistencia ep) {
            	response.getWriter().print("Error al escribir en el diario: " + ep.getMessage());
            }
        } else {
        	response.getWriter().print("Parámetros de llamada incompletos");
        }
    }

	/**
	 * 
	 * @param request
	 * @param response
	 * @param usuario 
	 * @throws IOException
	 */
    private void delItemDiccionario(HttpServletRequest request, HttpServletResponse response, String usuario) throws IOException {
        
        String id = request.getParameter("id");
        String tipo = request.getParameter("tipo");
        
        if (id != null && tipo != null) {
        	try {
        		if (tipo.equalsIgnoreCase("TipoTramite")) {
            		servicioDiccionario.borrar(Tipotramite.class, Integer.parseInt(id));
                } else if (tipo.equalsIgnoreCase("Boletin")) {
                	servicioDiccionario.borrar(Boletin.class, Integer.parseInt(id));
                } else if (tipo.equalsIgnoreCase("Caracterdeterminacion")) {
                	servicioDiccionario.borrar(Caracterdeterminacion.class, Integer.parseInt(id));
                } else if (tipo.equalsIgnoreCase("Centroproduccion")) {
                	servicioDiccionario.borrar(Centroproduccion.class, Integer.parseInt(id));
                } else if (tipo.equalsIgnoreCase("Grupodocumento")) {
                	servicioDiccionario.borrar(Grupodocumento.class, Integer.parseInt(id));
                } else if (tipo.equalsIgnoreCase("Instrumentoplan")) {
                	servicioDiccionario.borrar(Instrumentoplan.class, Integer.parseInt(id));
                } else if (tipo.equalsIgnoreCase("Instrumentotipooperacion")) {
                	servicioDiccionario.borrar(Instrumentotipooperacionplan.class, Integer.parseInt(id));
                } else if (tipo.equalsIgnoreCase("Naturaleza")) {
                	servicioDiccionario.borrar(Naturaleza.class, Integer.parseInt(id));
                } else if (tipo.equalsIgnoreCase("Operacioncaracter")) {
                	servicioDiccionario.borrar(Operacioncaracter.class, Integer.parseInt(id));
                } else if (tipo.equalsIgnoreCase("Organo")) {
                	servicioDiccionario.borrar(Organo.class, Integer.parseInt(id));
                } else if (tipo.equalsIgnoreCase("Tipodocumento")) {
                	servicioDiccionario.borrar(Tipodocumento.class, Integer.parseInt(id));
                } else if (tipo.equalsIgnoreCase("Tipooperacionplan")) {
                	servicioDiccionario.borrar(Tipooperacionplan.class, Integer.parseInt(id));
                }

            	servicioDiario.nuevoRegistroDiario(usuario, es.mitc.redes.urbanismoenred.servicios.seguridad.TipoSubsistema.GESTION_DICCIONARIO, "Eliminado de la tabla " + tipo + " el iden " + id);
            	
            	response.getWriter().print("Correcto");	
        	} catch (ExcepcionDiccionario ed) {
        		response.getWriter().print(ed.getMessage());
        	} catch (ExcepcionPersistencia ep) {
        		response.getWriter().print("Error al escribir en el diario: " + ep.getMessage());
        	}
        	    
        } else {
        	response.getWriter().print("Parámetros de llmada incompletos");	
        }
            
    }

    /**
     * 
     * @param request
     * @param response
     * @throws IOException
     */
    private void dicVisible(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String result = "N";
        try {
            String opt = Textos.getTexto("consola", "diccionariosvisibles");
            if (opt.equalsIgnoreCase("true")) {
                result = "S";
            }

        } catch (Exception e) {
           
            result = "Error " + e.getMessage();
        } finally {
            response.getWriter().print(result);
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
        try {
            processRequest(request, response);
        } catch (NamingException ex) {
            Logger.getLogger(ActionServletDic.class.getName()).log(Level.SEVERE, null, ex);
        }
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
        try {
            processRequest(request, response);
        } catch (NamingException ex) {
            Logger.getLogger(ActionServletDic.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * 
     * @param request
     * @param response
     * @param usuario 
     * @throws IOException
     * @throws NamingException
     */
    private void getName(HttpServletRequest request, HttpServletResponse response, String usuario) throws IOException, NamingException {
    	
    	String id = request.getParameter("id");
    	String idioma = request.getParameter("idioma");
    	String tipo = request.getParameter("tipo");
        if (request.getParameter("id") == null ||
        		request.getParameter("tipo") == null ||
        		request.getParameter("idioma") == null) {
        	String name = "-";
            String accion = "";
            if (tipo.equalsIgnoreCase("tipooperacionplan")) {
                name = servicioDiccionario.getTraduccion(Tipooperacionplan.class, Integer.parseInt(id), idioma);
                accion = "Consulta de Tipo operacion plan " + name;
            } else if (tipo.equalsIgnoreCase("instrumentoplan")) {
                name = servicioDiccionario.getTraduccion(Instrumentoplan.class, Integer.parseInt(id), idioma);
                accion = "Consulta de instrumento plan " + name;
            } else if (tipo.equalsIgnoreCase("caracterdeterminacion")) {
                name = servicioDiccionario.getTraduccion(Caracterdeterminacion.class, Integer.parseInt(id), idioma);
                accion = "Consulta de caracter determinación " + name;
            } else if (tipo.equalsIgnoreCase("tipooperaciondeterminacion")) {
                name = servicioDiccionario.getTraduccion(Tipooperaciondeterminacion.class, Integer.parseInt(id), idioma);
                accion = "Consulta de Tipo operacion determinacion " + name;
            }
            
            if (!accion.equalsIgnoreCase("")) {
            	try {
					servicioDiario.nuevoRegistroDiario(usuario, es.mitc.redes.urbanismoenred.servicios.seguridad.TipoSubsistema.GESTION_DICCIONARIO, accion);
				} catch (ExcepcionPersistencia ep) {
					response.getWriter().print("Error al escribir en el diario: " + ep.getMessage());
				}
            }

            response.getWriter().print(name);
        } else {
        	response.getWriter().print("Parámetros de llamada incompletos");
        }
    }

    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Servlet de gestión de diccionarios";
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
			servicioDiccionario = (ServicioDiccionariosLocal)icontext.lookup("java:global/UrbanismoEnRedV2/Urbanismoenred-serviciosV2/ServicioDiccionarios");
			servicioDiario = (ServicioDiarioLocal)icontext.lookup("java:global/UrbanismoEnRedV2/Urbanismoenred-serviciosV2/ServicioDiario");
		} catch (NamingException e) {
			throw new ServletException("No se ha podido inicializar correctamente el servlet. Causa: " + e.getMessage());
		}
    }

    /**
     * 
     * @param request
     * @param response
     * @param usuario 
     * @throws IOException
     */
    private void modItemDiccionario(HttpServletRequest request, HttpServletResponse response, String usuario) throws IOException {
        String id = request.getParameter("id");
        String datos = request.getParameter("data");
        String tipo = request.getParameter("tipo");
        String idioma = request.getParameter("idioma");
        if (datos != null &&
            	id != null &&
            	tipo != null &&
            	idioma != null) {
        	ObjectMapper mapper = new ObjectMapper();
        	
			@SuppressWarnings("unchecked")
			Map<String, Object> data = mapper.readValue(datos, Map.class);
			try {
				if (tipo.equalsIgnoreCase("TipoTramite")) {
					servicioDiccionario.modificarTipoTramite(
							Integer.parseInt(id),
							data.get("nombre").toString(), 
							"Descripcion de " + data.get("nombre"), 
							idioma);
					servicioDiario.nuevoRegistroDiario(usuario, 
							es.mitc.redes.urbanismoenred.servicios.seguridad.TipoSubsistema.GESTION_DICCIONARIO, 
							"Modificada la tabla " + tipo + " el iden " + id);
					response.getWriter().print("Elemento modificado");
	            } else if (tipo.equalsIgnoreCase("CentroProduccion")) {
	            	servicioDiccionario.modificarCentroProduccion(
	            			Integer.parseInt(id),
	            			data.get("nombre").toString(), 
	                		data.get("codigo").toString(), 
	                		data.get("mail") != null ? data.get("mail").toString():null, 
	                		data.get("password") != null ?data.get("password").toString():null, 
	                		"Descripcion de " + data.get("nombre"), 
	                		idioma);
	            	servicioDiario.nuevoRegistroDiario(usuario, 
							es.mitc.redes.urbanismoenred.servicios.seguridad.TipoSubsistema.GESTION_DICCIONARIO, 
							"Modificada la tabla " + tipo + " el iden " + id);
	            	response.getWriter().print("Elemento modificado");
	            } else {
	            	response.getWriter().print("Elemento no modificable");
	            }
			} catch (ExcepcionDiccionario ed) {
				response.getWriter().print(ed.getMessage());
			} catch (ExcepcionPersistencia ep) {
				response.getWriter().print("Error al escribir en el diario: " + ep.getMessage());
			}
        } else {
        	response.getWriter().print("Parámetros de llamada incompletos");
        }
    }

    /**
     * 
     * @param request
     * @param response
     * @throws IOException
     */
    private void planBaseVisible(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String result = "N";
        try {
            String opt = Textos.getTexto("consola", "planbasevisible");
            if (opt.equalsIgnoreCase("true")) {
                result = "S";
            }

        } catch (Exception e) {
            
            result = "Error " + e.getMessage();
        } finally {
            response.getWriter().print(result);
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
            throws ServletException, IOException, NamingException {
        response.setContentType("text/html;charset=UTF-8");
        
        //comprobación de roles
        Usuario usuario = (Usuario)request.getSession().getAttribute("usuario");
        	
        if (usuario != null && usuario.tieneRol(TipoRol.ADMINISTRADOR.getCodigo())) {

            String wsName = request.getParameter("wsName");
            if (wsName.equalsIgnoreCase("ADD_ITEM_DIC")) {
                this.addItemDiccionario(request, response, usuario.getNombre());
            } else if (wsName.equalsIgnoreCase("DEL_ITEM_DIC")) {
                this.delItemDiccionario(request, response, usuario.getNombre());
            } else if (wsName.equalsIgnoreCase("MOD_ITEM_DIC")) {
                this.modItemDiccionario(request, response, usuario.getNombre());
            } else if (wsName.equalsIgnoreCase("DIC_VISIBLE")) {
                this.dicVisible(request, response);
            } else if (wsName.equalsIgnoreCase("GET_NAME")) {
                this.getName(request, response, usuario.getNombre());
            } else if (wsName.equalsIgnoreCase("PLANBASE_VISIBLE")) {
                this.planBaseVisible(request, response);
            }

        } 
    }
}

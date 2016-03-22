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

import es.mitc.redes.urbanismoenred.data.rpm.seguridad.Diario;
import es.mitc.redes.urbanismoenred.data.rpm.seguridad.Subsistema;
import es.mitc.redes.urbanismoenred.servicios.seguridad.ServicioDiarioLocal;
import es.mitc.redes.urbanismoenred.servicios.seguridad.TipoRol;
import es.mitc.redes.urbanismoenred.servicios.seguridad.Usuario;
import es.mitc.redes.urbanismoenred.utils.consola.OgridColumnModel;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletOutputStream;
import org.codehaus.jackson.map.ObjectMapper;
import org.apache.log4j.Logger;

/**
 *
 * @author Arnaiz Consultores
 */
public class ActionServletDiarioDeOperaciones extends HttpServlet {

    /**
	 * 
	 */
	private static final long serialVersionUID = 3448253519546222213L;
	private static final Logger log = Logger.getLogger(ActionServletDiarioDeOperaciones.class);
	
	private DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
	private ServicioDiarioLocal servicioDiario;

    /**
     * 
     * @param response
     * @param idDiario
     * @param tipo
     * @throws IOException
     */
    private void descargarArchivo(HttpServletResponse response, int idDiario, String tipo) throws IOException {   
    	Diario diario = servicioDiario.getRegistro(idDiario);
    	if (diario != null) {
			// Adaptado de: http://snippets.dzone.com/posts/show/4629
	        // Leer el fichero y pasárselo al cliente por streaming
    		if (diario.getLog() != null) {
    			log.info("Descargando el fichero '" + diario.getLog() + "'");
    	        File f = new File(diario.getLog());
    	        int length = 0;
    	        ServletOutputStream op = response.getOutputStream();
    	        //
    	        // Set the response and go!
    	        //
    	        response.setContentType("application/octet-stream");
    	        response.setContentLength((int) f.length());
    	        if(tipo.equalsIgnoreCase("FIP2")){
                    response.setHeader("Content-Disposition", "attachment; filename=\"fip.xml\"");
                }else{
                    response.setHeader("Content-Disposition", "attachment; filename=\"log.txt\"");
                }
    	        //
    	        // Stream to the requester
    	        //
    	        byte[] bbuf = new byte[1024 * 256];
    	        DataInputStream in = new DataInputStream(new FileInputStream(f));
    	        while ((in != null) && ((length = in.read(bbuf)) != -1)) {
    	            op.write(bbuf, 0, length);
    	        }
    	        in.close();
    	        op.flush();
    	        op.close();
    		}
	          
	    } else {
	    	response.getWriter().print("Registro no existe.");
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
            log.error("Error en ActionServlet Diario de operaciones doGet"+ ex);
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
           log.error("Error en ActionServlet Diario de operaciones doPost"+ ex);
        }
    }

    /**
     * 
     * @param request
     * @param response
     * @throws IOException
     */
    private void getColumnModel(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ObjectMapper om = new ObjectMapper();
        
        List<Map<String, Object>> lcms = new ArrayList<Map<String, Object>>();

        lcms.add((new OgridColumnModel("Usuario", "user", "string", 100)).toJSON());
        lcms.add((new OgridColumnModel("Fecha", "fecha", "string", 180)).toJSON());
        lcms.add((new OgridColumnModel("Operacion", "operacion", "string", 250)).toJSON());
        lcms.add((new OgridColumnModel("Subsistema", "subsistema", "string", 150)).toJSON());
        lcms.add((new OgridColumnModel("Documentos", "documentos", "String", 80)).toJSON());

        
        om.writeValue(response.getWriter(), lcms);
        response.getWriter().close();
    }

    /**
     * 
     * @param request
     * @param response
     * @throws IOException
     * @throws NamingException
     */
    private void getDataDiaOp(HttpServletRequest request, HttpServletResponse response) throws IOException, NamingException {
    	String campo = request.getParameter("campo");
        String valor = request.getParameter("valor");
        String pagina = request.getParameter("page");
        String elementos = request.getParameter("perpage");
        
        if (campo != null && 
        		valor != null &&
        		pagina != null &&
        		elementos != null) {
            Map<String, Object> data = new HashMap<String, Object>();

            if (campo.equalsIgnoreCase("Usuario")) {
                int usuario = Integer.parseInt(valor);
                data = transformarJson(servicioDiario.getRegistrosUsuario(usuario),
                				Integer.parseInt(elementos),
                				Integer.parseInt(pagina));
            } else if (campo.equalsIgnoreCase("Subsistema")) {
            	int subsistema = Integer.parseInt(valor);
                data = transformarJson(servicioDiario.getRegistrosSubsistema(subsistema),
                				Integer.parseInt(elementos),
                				Integer.parseInt(pagina));
            } else if (campo.equalsIgnoreCase("Fecha")) {
                try {
                	data = transformarJson(servicioDiario.getRegistros(dateFormat.parse(valor), 
                			GregorianCalendar.getInstance().getTime()),
            				Integer.parseInt(elementos),
            				Integer.parseInt(pagina));
                } catch (ParseException ex) {
                    response.getWriter().print("La fecha no ha podido ser parseado");
                    log.error("La fecha no ha podido ser parseada", ex);
                }
            }

            ObjectMapper om = new ObjectMapper();
            om.writeValue(response.getWriter(), data);
            response.getWriter().close();
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
        return "Atiende peticiones del Diario de Operaciones";
    }// </editor-fold>

	/**
     * 
     * @param request
     * @param response
     * @throws NamingException
     * @throws IOException
     */
    private void getSubsistemas(HttpServletRequest request, HttpServletResponse response) throws NamingException, IOException {
        //devolver la tabla de subsistemas
        Subsistema[] subsistemas = servicioDiario.getSubsistemas();
        
        ObjectMapper om = new ObjectMapper();
        om.writeValue(response.getWriter(), transformarJson(subsistemas));
    }

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
            throws ServletException, IOException, NamingException {
        
    	Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
        if (usuario != null && usuario.tieneRol(TipoRol.ADMINISTRADOR.getCodigo())) {
            String wsName = request.getParameter("wsName");

            if (wsName.equals("GET_DIA_OP")) {
                this.getDataDiaOp(request, response);
            } else if (wsName.equals("GET_COLUMNMODEL_DIA_OP")) {
                this.getColumnModel(request, response);
            } else if (wsName.equals("GET_LISTA_SUBSISTEMAS")) {
                this.getSubsistemas(request, response);
            } else if (wsName.equalsIgnoreCase("DOWNLOAD_LOG_OP")) {
            	String idDiario = request.getParameter("idDiario");
            	if (idDiario != null) {
            		descargarArchivo(response, Integer.parseInt(idDiario), "LOG");
            	} else {
            		response.getWriter().print("Faltan parámetros en la llamada.");
            	}
            } else if (wsName.equalsIgnoreCase("DOWNLOAD_FIP2")) {
            	String idDiario = request.getParameter("idDiario");
            	
            	if (idDiario != null) {
            		descargarArchivo(response, Integer.parseInt(idDiario), "FIP");
            	} else {
            		response.getWriter().print("Faltan parámetros en la llamada.");
            	}
            } else {
                response.getWriter().write("Servicio no encontrado");
            }
        } else {
        	response.getWriter().write("Error: Se necesitan permisos de adminsitración.");
        }
    }
    
    /**
     * 
     * @param registros
     * @param elementos
     * @param pagina
     * @return
     */
    private Map<String, Object> transformarJson(Diario[] registros, int elementos, int pagina) {
    	List<Map<String, Object>> lista = new ArrayList<Map<String,Object>>();
    	Map<String, Object> res;
    	int inicio = elementos*(pagina-1);
    	
    	int fin = inicio+elementos;
    	fin = (fin > registros.length)? registros.length: fin;
    	
    	for (int i = inicio; i < fin; i++) {
    		res = new HashMap<String, Object>();
    		res.put("iden", registros[i].getIden());
            res.put("operacion", registros[i].getOperaciones().getNombre());
            res.put("user", registros[i].getUsuario().getNombre());
            res.put("fecha", registros[i].getFechalogin().toString());
            res.put("subsistema", registros[i].getSubsistema().getNombre());
            if (registros[i].getLog() == null || registros[i].getLog().equalsIgnoreCase("")) {
                res.put("log", "");
            } else {
                String dir = registros[i].getLog().trim();
                if(dir.substring(dir.length()-3,dir.length()).equals("xml")){
                    res.put("log", "FIP2");
                }else{
                    res.put("log", "Disponible");
                }
            }
            
            lista.add(res);
    	}
        
    	Map<String, Object> data = new HashMap<String, Object>();
    	data.put("page", pagina);
    	data.put("total", registros.length);
    	data.put("data", lista);
    	return data;
	} 

    /**
     * 
     * @param registros
     * @return
     */
    private Map<String, Object> transformarJson(Subsistema[] registros) {
    	List<Map<String, Object>> lista = new ArrayList<Map<String,Object>>();
    	Map<String, Object> res;
    	
    	for (int i = 0; i < registros.length; i++) {
    		res = new HashMap<String, Object>();
    		res.put("iden", registros[i].getIden());
            res.put("nombre", registros[i].getNombre());
            
            lista.add(res);
    	}
        
    	Map<String, Object> data = new HashMap<String, Object>();
    	data.put("page", 1);
    	data.put("total", registros.length);
    	data.put("data", lista);
    	return data;
	}
}

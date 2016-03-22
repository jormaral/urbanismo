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
package es.mitc.redes.urbanismoenred.consola.filtros;

import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;


/**
 * 
 * Comprueba peticiones multipart que se realicen. Estas peticiones hacen que 
 * los parámetros pasados al servlet no se encuentren en request.getParameter.
 * 
 * Este filtro no es necesario si se utiliza la especificación 3.0 de Servlets.
 * 
 * @author Arnaiz Consultores
 *
 */
public class FiltroArchivos implements Filter {
	
	private static final Logger log = Logger.getLogger(FiltroArchivos.class);

	private long maxFileSize = -1;

	/* (non-Javadoc)
	 * @see javax.servlet.Filter#destroy()
	 */
	@Override
	public void destroy() {
	}

	/* (non-Javadoc)
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		
		if (request instanceof HttpServletRequest) {
            chain.doFilter(parseRequest((HttpServletRequest)request), response);
        } else {
            // Not a HttpServletRequest.
            chain.doFilter(request, response);
        }
	}

	/* (non-Javadoc)
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		String maxFileSize = filterConfig.getInitParameter("maxFileSize");
        if (maxFileSize != null) {
        	try {
        		this.maxFileSize = Long.parseLong(maxFileSize);
        	} catch (NumberFormatException nfe) {
        		log.warn("No se ha podido procesar el tamaño máximo de archivo. " + nfe.getMessage());
        	}
        }
	}
	
	@SuppressWarnings("unchecked") // ServletFileUpload#parseRequest() does not return generic type.
    private HttpServletRequest parseRequest(HttpServletRequest request) throws ServletException {

        // Comprueba si el request es una solicitud multipart/form-data.
        if (!ServletFileUpload.isMultipartContent(request)) {
            // Si no lo es no se hace nada
        	log.debug("No es un formulario multipart");
            return request;
        }
        
        log.debug("Procesando invocación multipart..." + request.getContextPath());

        List<FileItem> multipartItems = null;
        FileUploadListener listener = new FileUploadListener();
        
        try {
        	
        	ServletFileUpload sfu = new ServletFileUpload(new DiskFileItemFactory());
        	request.getSession().setAttribute(FileUploadListener.NOMBRE_ATRIBUTO, listener);
        	
            // Parse the multipart request items.
        	sfu.setProgressListener(listener);
        	
        	log.debug("Procesando solicitud");
            multipartItems = sfu.parseRequest(request);
            log.debug("Solicitud procesada");
            // Note: we could use ServletFileUpload#setFileSizeMax() here, but that would throw a
            // FileUploadException immediately without processing the other fields. So we're
            // checking the file size only if the items are already parsed. See processFileField().
        } catch (FileUploadException e) {
            throw new ServletException("Cannot parse multipart request: " + e.getMessage(),e);
        } catch (Exception e) {
        	// intento capturar la excepción que se produce al cortar una subida de archivo por cambiar de página.
        	throw new ServletException(e.getMessage(),e);
        }

        Map<String, String[]> parameterMap = new HashMap<String, String[]>();

        for (FileItem multipartItem : multipartItems) {
            if (multipartItem.isFormField()) {
                // Procesa campos "estándar" de formularios (input type="text|radio|checkbox|etc", select, etc).
                procesarCamposEstandar(multipartItem, parameterMap);
            } else {
                // Process form file field (input type="file").
                procesarCamposArchivo(multipartItem, request);
            }
        }

        // Wrap the request with the parameter map which we just created and return it.
        return wrapRequest(request, parameterMap);
    }
	
	private void procesarCamposArchivo(FileItem fileField, HttpServletRequest request) {
		log.debug("Procesando campo archivo: " + fileField.getName());
        if (fileField.getName().length() <= 0) {
            // No file uploaded.
            request.setAttribute(fileField.getFieldName(), null);
        } else if (maxFileSize > 0 && fileField.getSize() > maxFileSize) {
            // File size exceeds maximum file size.
            request.setAttribute(fileField.getFieldName(), new FileUploadException(
                "File size exceeds maximum file size of " + maxFileSize + " bytes."));
            // Immediately delete temporary file to free up memory and/or disk space.
            fileField.delete();
        } else {
            // File uploaded with good size.
            request.setAttribute(fileField.getFieldName(), fileField);
        }
    }
	
	private void procesarCamposEstandar(FileItem formField, Map<String, String[]> parameterMap) {
        String name = formField.getFieldName();
        String value = formField.getString();
        String[] values = parameterMap.get(name);
        log.debug("Procesando campo estándar " + name);
        
        if (values == null) {
            // Not in parameter map yet, so add as new value.
            parameterMap.put(name, new String[] { value });
        } else {
            // Multiple field values, so add new value to existing array.
            int length = values.length;
            String[] newValues = new String[length + 1];
            System.arraycopy(values, 0, newValues, 0, length);
            newValues[length] = value;
            parameterMap.put(name, newValues);
        }
    }

	private HttpServletRequest wrapRequest(
	        HttpServletRequest request, final Map<String, String[]> parameterMap)
	{
	   return new HttpServletRequestWrapper(request) {
		   public Map<String, String[]> getParameterMap() {
			   return parameterMap;
	       }
	       
		   public String[] getParameterValues(String name) {
			   return parameterMap.get(name);
	       }
	       
		   public String getParameter(String name) {
			   String[] params = getParameterValues(name);
	           return params != null && params.length > 0 ? params[0] : null;
	       }
	       
		   public Enumeration<String> getParameterNames() {
			   return Collections.enumeration(parameterMap.keySet());
		   }
	    };
	}
}

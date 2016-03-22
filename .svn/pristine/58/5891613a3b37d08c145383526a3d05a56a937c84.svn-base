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
package es.mitc.redes.urbanismoenred.consola.refundido.acciones;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.activation.MimetypesFileTypeMap;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import es.mitc.redes.urbanismoenred.data.rpm.refundido.Archivo;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Proceso;
import es.mitc.redes.urbanismoenred.servicios.refundido.GestorContextosRefundidoLocal;

/**
 * @author Arnaiz Consultores
 *
 */
@Stateless(name = "GET_ARCHIVO_REFUNDIDO")
public class ObtenerArchivoRefundido implements IAccion {
	
	private static final MimetypesFileTypeMap mimeMapper = new MimetypesFileTypeMap();
	
	@EJB
	private GestorContextosRefundidoLocal gestorContextos;

	/* (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.consola.refundido.acciones.IAccion#ejecutar(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public void ejecutar(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String idProceso = request.getParameter("idProceso");
		
        String tipo = request.getParameter("tipo");
		if (idProceso != null &&
				tipo != null) {
			Proceso proceso = gestorContextos.getProceso(Integer.parseInt(idProceso));
	        
	        if (proceso != null) {
	        	for(Archivo doc : proceso.getArchivos()) {
	        		if (doc.getTipo().equals(tipo)) {
	        			File f = new File(doc.getContenido());
    					if (f.exists()) {
    						int length = 0;
	    	    	        ServletOutputStream op = response.getOutputStream();

	    	    	        response.setContentType(mimeMapper.getContentType(f));
	    	    	        response.setHeader("Content-Disposition", "filename=" + f.getName());
	    	    	        response.setContentLength((int) f.length());
	    	    	    
	    	    	        byte[] bbuf = new byte[1024 * 256];
	    	    	        DataInputStream in = new DataInputStream(new FileInputStream(f));
	    	    	        while ((in != null) && ((length = in.read(bbuf)) != -1)) {
	    	    	            op.write(bbuf, 0, length);
	    	    	        }
	    	    	        
	    	    	        in.close();
	    	    	        op.flush();
	    	    	        op.close();
	    	    	        return;
    					} else {
    						response.getWriter().print("El archivo solicitado no existe.");
    					}
	        		}
	        	}
	        	response.getWriter().print("El archivo solicitado no existe.");
	        } else {
	        	response.getWriter().print("El proceso no existe en base de datos.");
	        }
	        
        } else {
        	 response.getWriter().print("Solicitud incompleta");
        }
	}

}

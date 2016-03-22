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
package es.mitc.redes.urbanismoenred.consola.validacion.acciones;

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

import es.mitc.redes.urbanismoenred.consola.validacion.IAccion;
import es.mitc.redes.urbanismoenred.data.validacion.Proceso;
import es.mitc.redes.urbanismoenred.servicios.validacion.ServicioResultadosValidacionLocal;

/**
 * @author Arnaiz Consultores
 *
 */
@Stateless( name = "DESCARGAR_FIP")
public class DescargarFIP implements IAccion {
	
	private static final MimetypesFileTypeMap mimeMapper = new MimetypesFileTypeMap();

	private static final int BUFFER = 2024;
	
	@EJB
	private ServicioResultadosValidacionLocal servicioProcesos;

	/* (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.consola.validacion.IAccion#ejecutar(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public void ejecutar(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String idProceso = request.getParameter("idProceso");
		
		if (idProceso != null) {
			try {
				Proceso proceso = servicioProcesos.getProceso(Integer.parseInt(idProceso));
				if (proceso != null) {
					File fip = new File(proceso.getBackup());
					File zip = new File(fip.getParentFile(),"fip.zip");
					if (zip.exists()) {
						int length = 0;
		    	        ServletOutputStream op = response.getOutputStream();
		    	        
		    	        response.setContentType(mimeMapper.getContentType(zip));
		    	        response.setHeader("Content-Disposition", "filename=" + zip.getName());
		    	        response.setContentLength((int) zip.length());
		    	    
		    	        byte[] bbuf = new byte[BUFFER];
		    	        DataInputStream in = new DataInputStream(new FileInputStream(zip));
		    	        while ((in != null) && ((length = in.read(bbuf)) != -1)) {
		    	            op.write(bbuf, 0, length);
		    	        }
		    	        
		    	        in.close();
		    	        op.flush();
		    	        op.close();
					} else {
						response.getWriter().print("El archivo solicitado ya no se encuentra en el servidor.");
					}
				} else {
					response.getWriter().print("El proceso no existe.");
				}
			} catch (NumberFormatException nfe) {
				response.getWriter().print("Identificador incorrecto.");
			}
		} else {
			response.getWriter().print("Solicitud incompleta.");
		}

	}

}

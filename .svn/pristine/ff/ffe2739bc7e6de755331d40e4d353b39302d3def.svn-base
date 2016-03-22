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
package es.mitc.redes.urbanismoenred.consola.planeamiento.acciones;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.activation.MimetypesFileTypeMap;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import es.mitc.redes.urbanismoenred.consola.planeamiento.IAccion;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Documento;
import es.mitc.redes.urbanismoenred.servicios.planeamiento.ExcepcionPlaneamiento;
import es.mitc.redes.urbanismoenred.servicios.planeamiento.ServicioPlaneamientoLocal;

/**
 * @author Arnaiz Consultores
 *
 */
@Stateless(name="GET_DOC_URL")
public class GetDocumento implements IAccion {
	
	@EJB
	private ServicioPlaneamientoLocal servicioPlaneamiento;
	
	private static final MimetypesFileTypeMap mimeMapper = new MimetypesFileTypeMap();

	/* (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.consola.planeamiento.IAccion#ejecutar(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public void ejecutar(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String idDoc = request.getParameter("idDoc");
		
        String idioma = request.getParameter("idioma");
		if (idDoc != null &&
						idioma != null) {
			Documento doc = servicioPlaneamiento.get(Documento.class, Integer.parseInt(idDoc));
	        
	        if (doc != null) {
				try {
					String archivo = doc.getArchivo();
					if ("\\".equals(File.separator)) {
						archivo  = archivo.replace("/", File.separator);
					} else {
						archivo = archivo.replace("\\", File.separator);
					}
					File f = servicioPlaneamiento.getArchivo(doc.getTramite().getCodigofip(), archivo);
					int length = 0;
	    	        ServletOutputStream op = response.getOutputStream();
	    	        
	    	        if (f.isDirectory()) {
	    	        	File zipFile = new File(f.getParentFile(), f.getName()+".zip");
	    	        	
	    	        	if (!zipFile.exists()) {
	    	        		comprimir(f, zipFile);
    	        		} 
	    	        	
	    	        	f = zipFile;
	    	        }
	    	        
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
				} catch (ExcepcionPlaneamiento e) {
					response.getWriter().print("Error al obtener el documento: " + e.getMessage());
				}
    	        
	        }
	        
        } else {
        	 response.getWriter().print("Solicitud incompleta");
        }
	}
	
	/**
	 * 
	 * @param f
	 * @return
	 * @throws ExcepcionPlaneamiento 
	 */
	private void comprimir(File origen, File destino) throws ExcepcionPlaneamiento {
		int BUFFER = 2048;
		BufferedInputStream origin = null;
        FileOutputStream dest;
		try {
			dest = new FileOutputStream(destino);
			ZipOutputStream out = new ZipOutputStream(new 
					BufferedOutputStream(dest));
	        //out.setMethod(ZipOutputStream.DEFLATED);
	        byte data[] = new byte[BUFFER];
	        // get a list of files from current directory
	        Map<String, File> entradas = new HashMap<String, File>();
	        recopilarEntradas("", origen, entradas);

	        for (String ruta : entradas.keySet()) {
	        	if (!entradas.get(ruta).isDirectory()) {
	        		FileInputStream fi = new FileInputStream(entradas.get(ruta));
	 	           	origin = new BufferedInputStream(fi, BUFFER);
	 	           	ZipEntry entry = new ZipEntry(ruta);
	 	           	out.putNextEntry(entry);
	 	           	int count;
	 	           	while((count = origin.read(data, 0, 
	 	        		   BUFFER)) != -1) {
	 	           		out.write(data, 0, count);
	 	           	}
	 	           origin.close();
	        	}
	        }
	        out.close();          
		} catch (FileNotFoundException e) {
			throw new ExcepcionPlaneamiento("No se ha podido generar el archivo zip", e);
		} catch (IOException e) {
			throw new ExcepcionPlaneamiento("No se ha podido generar el archivo zip", e);
		}
        
	}
	
	void recopilarEntradas(String ruta, File padre, Map<String, File> entradas) {
		String rutaPadre;
		if (!ruta.isEmpty()) {
			rutaPadre = ruta +File.separator+padre.getName();
		} else {
			rutaPadre = padre.getName();
		}
		entradas.put(rutaPadre, padre);
		File hijos[] = padre.listFiles();

        for (int i=0; i < hijos.length; i++) {
        	if (hijos[i].isDirectory()) {
        		recopilarEntradas(rutaPadre, hijos[i], entradas);
        	} else {
        		entradas.put(rutaPadre+File.separator+hijos[i].getName(), hijos[i]);
        	}
        }
	}

}

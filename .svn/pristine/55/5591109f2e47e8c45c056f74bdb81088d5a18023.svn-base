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
package es.mitc.redes.urbanismoenred.servicios.refundido;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;

import es.mitc.redes.urbanismoenred.data.rpm.diccionario.Ambito;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Documento;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Tramite;
import es.mitc.redes.urbanismoenred.servicios.planeamiento.ExcepcionPlaneamiento;
import es.mitc.redes.urbanismoenred.servicios.planeamiento.ServicioDiccionariosLocal;
import es.mitc.redes.urbanismoenred.servicios.refundido.ContextoRefundido.LOG;

/**
 * Genberador de FIPs a partir del contenido del esquema de refundido.
 * 
 * @author Arnaiz Consultores
 *
 */
@Stateless( name = "refundido.exportacion.fip")
public class GestorExportacionRefundido implements GestorExportacionLocal {

	private static final Logger log = Logger.getLogger(GestorExportacionRefundido.class);

	private static final String DIRECTORIO_TRABAJO = "var";

	private static final int BUFFER = 2048;
	
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	
	@PersistenceContext(unitName = "rpmv2")
    private EntityManager em;
	
	@EJB( beanName = "refundido.generador.fip.stream")
	private GeneradorFipLocal generadorFip;

	@EJB
	private ServicioDiccionariosLocal servicioDiccionario;
	
	/**
     * 
     * @param path
     * @return
     */
    private boolean borrarDirectorio(File path) {
        if( path.exists() ) {
          File[] files = path.listFiles();
          for(int i=0; i<files.length; i++) {
             if(files[i].isDirectory()) {
            	 if (!borrarDirectorio(files[i])) {
            		 log.warn("No se pudo borrar el directorio: " + files[i].getAbsolutePath());
            	 }
             } else {
            	 if (!files[i].delete()) {
            		 log.warn("No se pudo borrar el archivo: " + files[i].getAbsolutePath());
            	 }
             }
          }
        }
        return path.delete();
      }
    
    /**
	 * 
	 * @param f
	 * @return
	 * @throws ExcepcionPlaneamiento 
	 */
	private void comprimir(File origen, File destino) throws ExcepcionRefundido {
		BufferedInputStream origin = null;
        FileOutputStream dest;
		try {
			dest = new FileOutputStream(destino);
			ZipOutputStream out = new ZipOutputStream(new 
					BufferedOutputStream(dest), Charset.forName("Cp437"));
	        
	        byte data[] = new byte[BUFFER];
	       
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
			throw new ExcepcionRefundido("No se ha podido generar el archivo zip", 4001, e);
		} catch (IOException e) {
			throw new ExcepcionRefundido("No se ha podido generar el archivo zip", 4002, e);
		}
        
	}

    /**
	 * Copia un directorio
	 * 
	 * @param sourceLocation
	 * @param targetLocation
	 * @throws IOException
	 */
	public void copiarDirectorio(File sourceLocation , File targetLocation)
			throws IOException {
        
        if (sourceLocation.isDirectory()) {
            if (!targetLocation.exists()) {
                targetLocation.mkdir();
            }
            
            String[] children = sourceLocation.list();
            for (int i=0; i<children.length; i++) {
                copiarDirectorio(new File(sourceLocation, children[i]),
                        new File(targetLocation, children[i]));
            }
        } else {
            
            InputStream in = new FileInputStream(sourceLocation);
            OutputStream out = new FileOutputStream(targetLocation);
           
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
        }
	}
	
	/*
     * (non-Javadoc)
     * @see es.mitc.redes.urbanismoenred.servicios.refundido.GestorExportacionLocal#exportarFIP(es.mitc.redes.urbanismoenred.servicios.refundido.ContextoRefundido)
     */
	@Override
	public File exportarFIP(ContextoRefundido contexto) throws ExcepcionRefundido {

		contexto.logTraducido(LOG.INFO, "refundido.exportar.fip.inicio");

        Tramite tramiteRefundido = (Tramite) contexto.getParametro(ContextoRefundido.TRAMITE_REFUNDIDO);
        
        em.refresh(tramiteRefundido);

        // Establece la ruta donde se va a copiar el archivo FIP
        String baseDirPath = System.getenv("REDES_PATH")+File.separator+ DIRECTORIO_TRABAJO + File.separator+"refundido" + File.separator + "FIPs" + File.separator + tramiteRefundido.getCodigofip();
        File dir = new File(baseDirPath);
        if (!dir.exists()) {
			dir.mkdirs();
		}
        
        limpiarTramite(tramiteRefundido, dir);
    
        log.info("Ruta del archivo FIP: " + dir.getAbsolutePath());

        File f = new File(dir,"fip.xml");
        
        generadorFip.generarFIP2(contexto, f);

        contexto.logTraducido(LOG.INFO, "refundido.exportar.fip.xml.generado");
        
        // El nombre del fichero debe ser:
        // Si hay INE: <INE del municipio>_<código del trámite refundido>_FechaRefundido
        // Si no hay INE: <código del trámite refundido>_FechaRefundido
        StringBuffer nombreArchivo = new StringBuffer();
        Ambito ambito = servicioDiccionario.get(Ambito.class,tramiteRefundido.getPlan().getIdambito());
        if (ambito != null) {
        	if (ambito.getCodigoine() != null && !ambito.getCodigoine().isEmpty()) {
        		nombreArchivo.append(ambito.getCodigoine());
        		nombreArchivo.append("_");
        	}
        }
        nombreArchivo.append(tramiteRefundido.getCodigofip());
        nombreArchivo.append("_");
        nombreArchivo.append(sdf.format(tramiteRefundido.getFecha()));
        nombreArchivo.append(".zip");
        
        File ficheroComprimido = new File(dir.getParentFile(), nombreArchivo.toString());
        
        comprimir(dir, ficheroComprimido);
        
        borrarDirectorio(dir);
        
        contexto.logTraducido(LOG.INFO, "refundido.exportar.fip.finalizado");
    
        return ficheroComprimido;
	}
	
	/**
	 * 
	 * @param rutasDocumentos
	 * @param base
	 * @return
	 */
	private boolean limpiar(List<String> rutasDocumentos, File base) {
		boolean resultado = false;
		for(File archivo : base.listFiles()) {
			if (!rutasDocumentos.contains(archivo.getAbsolutePath())) {
				if (archivo.isDirectory()) { 
					if (!limpiar(rutasDocumentos,archivo)) {
						borrarDirectorio(archivo);
					}
				} else {
					if (!archivo.delete()) {
	           		 log.warn("No se pudo borrar el archivo: " + archivo.getAbsolutePath());
	           	 	}
				}
			} else {
				resultado = true;
			}
		}
		return resultado;
	}

	/**
	 * Elimina aquellos archivos o directorios que no estando en el trámite
	 * aparezcan en el directorio de trabajo.
	 * 
	 * @param tramiteRefundido
	 * @param base 
	 */
	private void limpiarTramite(Tramite tramiteRefundido, File base) {
		List<String> rutasDocumentos = new ArrayList<String>();
		File ruta;
		for (Documento documento : tramiteRefundido.getDocumentos()) {
			// Por alguna razón no se están actualizando correctamente los datos
			em.refresh(documento);
			ruta = new File(base,documento.getArchivo());
			
			// Se han eliminado los documentos que son sólo de trámite
			// por lo que sus archivos físicos también se eliminan
			if (documento.getDocumentocasos().size() != 0 ||
					documento.getDocumentodeterminacions().size() != 0 ||
					documento.getDocumentoentidads().size() != 0) {
				rutasDocumentos.add(ruta.getAbsolutePath());
			}
		}
		
		limpiar(rutasDocumentos, base);
	}

	/**
	 * 
	 * @param ruta
	 * @param padre
	 * @param entradas
	 */
	private void recopilarEntradas(String ruta, File padre, Map<String, File> entradas) {
		File hijos[] = padre.listFiles();

        for (int i=0; i < hijos.length; i++) {
        	if (hijos[i].isDirectory()) {
        		recopilarEntradas(ruta.isEmpty()? hijos[i].getName(): ruta + File.separator + hijos[i].getName(), hijos[i], entradas);
        	} else {
        		entradas.put(ruta.isEmpty()? hijos[i].getName(): ruta+File.separator+hijos[i].getName(), hijos[i]);
        	}
        }
	}

}

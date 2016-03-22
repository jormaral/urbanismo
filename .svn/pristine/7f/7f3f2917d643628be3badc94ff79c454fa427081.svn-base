/*
* Copyright 2011 red.es
* Autores: Arnaiz Consultores.
*
* Licencia con arreglo a la EUPL, Versión 1.1 o -en cuanto
** sean aprobadas por la Comision Europea- versiones
** posteriores de la EUPL (la <<Licencia>>);
** Solo podra usarse esta obra si se respeta la Licencia.
* Puede obtenerse una copia de la Licencia en:
*
* http://ec.europa.eu/idabc/eupl5
*
** Salvo cuando lo exija la legislacion aplicable o se acuerde.
* por escrito, el programa distribuido con arreglo a la
** Licencia se distribuye <<TAL CUAL>>,
** SIN GARANTIAS NI CONDICIONES DE NINGUN TIPO, ni expresas
** ni implicitas.
** Vease la Licencia en el idioma concreto que rige
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
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.apache.log4j.Logger;

import es.mitc.redes.urbanismoenred.data.fip.FIP;
import es.mitc.redes.urbanismoenred.data.rpm.diccionario.Ambito;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Documento;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Tramite;
import es.mitc.redes.urbanismoenred.servicios.comunes.GestionIntroduccionFIPenSistemaLocal;
import es.mitc.redes.urbanismoenred.servicios.dal.GeneradorFipLocal;
import es.mitc.redes.urbanismoenred.servicios.planeamiento.ExcepcionPlaneamiento;
import es.mitc.redes.urbanismoenred.servicios.planeamiento.ServicioDiccionariosLocal;
import es.mitc.redes.urbanismoenred.servicios.refundido.ContextoRefundido.LOG;
import es.mitc.redes.urbanismoenred.servicios.seguridad.ExcepcionSeguridad;
import es.mitc.redes.urbanismoenred.utils.excepciones.RedesException;


/**
 * Session Bean implementation class GestorExportacionBean
 */
@Stateless(name = "GestorExportacion")
public class GestorExportacionBean implements GestorExportacionLocal {

	private static final Logger log = Logger.getLogger(GestorExportacionBean.class);

	private static final String DIRECTORIO_TRABAJO = "var";

	private static final int BUFFER = 2048;
	
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	
	@EJB( beanName = "GeneradorFipRefundido")
	private GeneradorFipLocal generadorFip;
	@EJB
	private GestionIntroduccionFIPenSistemaLocal gestorFips;
	@EJB
	private ServicioDiccionariosLocal servicioDiccionario;
	
    /**
     * Default constructor. 
     */
    public GestorExportacionBean() {
    }
    
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

	/**
	 * 
	 * @param contexto
	 * @throws RedesException 
	 */
	@SuppressWarnings("unchecked")
	private void copiarDocumentos(ContextoRefundido contexto, File dir) throws RedesException {
		Map<String,List<Integer>> documentosDEC = (Map<String,List<Integer>>)contexto.getParametro("refundido.documentos.dec");
        if (documentosDEC != null) {
        	contexto.log(LOG.INFO, "Copiando documentos refundidos...");
        	Tramite tramiteRefundido = (Tramite) contexto.getParametro(ContextoRefundidoBasico.TRAMITE_REFUNDIDO);
        	
        	// Recorro los documentos del trámite, si esto resultara demasiado
        	// lento habría que utilizar un EntityManager y la consulta empleada
        	// para generar el Mapa.
        	File dirBase;
        	File archivoFuente;
        	File archivoDestino;
        	FileChannel fuente;
        	FileChannel destino;
        	String archivo;
        	for (Documento doc : tramiteRefundido.getDocumentos()) {
        		if (doc.getDocumentocasos().size() > 0 || 
        				doc.getDocumentodeterminacions().size() > 0 || 
        				doc.getDocumentoentidads().size() > 0) {
        			for(String codigoFip : documentosDEC.keySet()) {
        				if (documentosDEC.get(codigoFip).contains(doc.getIden())) {
        					dirBase = new File(gestorFips.getUltimaVersionFIP(codigoFip)).getParentFile();
        					if (dirBase.exists() && dirBase.isDirectory()) {
        						archivo = doc.getArchivo();
        						if ("\\".equals(File.separator)) {
        							archivo = archivo.replace("/", File.separator);
        						} else {
        							archivo = archivo.replace("\\", File.separator);
        						}
        						archivoFuente = new File(dirBase, archivo);
        						archivoDestino = new File(dir, archivo);
        						if (archivoFuente.exists()) {
        							try {
        								if (archivoFuente.isDirectory()) {
	        								copiarDirectorio(archivoFuente, archivoDestino);
	        							} else {
											fuente = new FileInputStream(archivoFuente).getChannel();
											if (!archivoDestino.exists()) {
												archivoDestino.getParentFile().mkdirs();
												if (archivoDestino.createNewFile()) {
													destino = new FileOutputStream(archivoDestino).getChannel();
			        								
			        								long count = 0;
			        								long size = fuente.size();
			        								while((count += destino.transferFrom(fuente, 0, size))<size);
			        								fuente.close();
			        								destino.close();
												} else {
													contexto.log(LOG.AVISO, "No se ha podido crear el documento " +doc.getArchivo() +" del trámite " + codigoFip);
												}
											} else {
												contexto.log(LOG.AVISO, "El documento " +doc.getArchivo() +" del trámite " + codigoFip + " ya existe");
											}
	        							}
        							}  catch (FileNotFoundException e) {
        								contexto.log(LOG.AVISO, "No se ha podido encontrar el documento " +doc.getArchivo() +" del trámite " + codigoFip);
									} catch (IOException e) {
										contexto.log(LOG.AVISO, "No se ha podido copiar el documento " +doc.getArchivo() +" del trámite " + codigoFip);
									}
        						} else {
        							contexto.log(LOG.AVISO, "No se ha podido encontrar el documento " +doc.getArchivo() +" del trámite " + codigoFip);
        						}
        					} else {
        						contexto.log(LOG.AVISO, "No se han podido copiar los documentos del trámite " + codigoFip +". No se ha encontrado el directorio raíz.");
        					}
        				}
        			}
        		}
        	}
        }
	}
	
	/*
	 * (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.servicios.refundido.GestorExportacionLocal#exportarFIP(es.mitc.redes.urbanismoenred.servicios.refundido.ContextoRefundido)
	 */
	@Override
	public File exportarFIP(ContextoRefundido contexto) throws ExcepcionRefundido {

        try{
            contexto.log(LOG.INFO, "Exportando a FIP...");

            Tramite tramiteRefundido = (Tramite) contexto.getParametro(ContextoRefundido.TRAMITE_REFUNDIDO);
            
            contexto.log(LOG.INFO, "Trámite refundido: código=[" + tramiteRefundido.getCodigofip() + "], Plan [" + tramiteRefundido.getPlan().getCodigo() + "]");

            // Establece la ruta donde se va a copiar el archivo FIP
            String baseDirPath = System.getenv("REDES_PATH")+File.separator+ DIRECTORIO_TRABAJO + File.separator+"refundido" + File.separator + "FIPs" + File.separator + tramiteRefundido.getCodigofip();
            File dir = new File(baseDirPath);
            if (!dir.exists()) {
				dir.mkdirs();
			}
        
            log.info("    Ruta del archivo FIP: " + dir.getAbsolutePath());

            File f = new File(dir,"fip.xml");

            contexto.log(LOG.INFO, "Generando FIP...");
            
            FIP fip = generadorFip.generarFIP2(tramiteRefundido);

            contexto.log(LOG.INFO, "Guardando FIP en disco...");
            
            try {
    			JAXBContext jc = JAXBContext.newInstance("es.mitc.redes.urbanismoenred.data.fip");
    			
    			Marshaller marshaller = jc.createMarshaller();
    			
    			marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
    			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
    			
    			marshaller.marshal(fip, f);
    		} catch (JAXBException e) {
    			throw new ExcepcionSeguridad("Error al generar el archivo " + f.getAbsolutePath() + ". Causa: " + e.getMessage());
    		}
            
            contexto.log(LOG.INFO, "Archivo FIP guardado.");
            
            copiarDocumentos(contexto, dir);
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
        
            return ficheroComprimido;
        } catch(Exception e){
            throw new ExcepcionRefundido("Error al exportar a FIP. " + e.getMessage(), 4003);
        }
	}
	
	/**
	 * 
	 * @param ruta
	 * @param padre
	 * @param entradas
	 */
	void recopilarEntradas(String ruta, File padre, Map<String, File> entradas) {
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

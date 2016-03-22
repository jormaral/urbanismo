/*
* Copyright 2011 red.es
* Autores: Arnaiz Consultores.
*
* Licencia con arreglo a la EUPL, Versión 1.1 o -en cuanto
* sean aprobadas por la Comision Europea- versiones
* posteriores de la EUPL (la <<Licencia>>);
* Solo podra usarse esta obra si se respeta la Licencia.
* Puede obtenerse una copia de la Licencia en:
*
* http://ec.europa.eu/idabc/eupl5
*
* Salvo cuando lo exija la legislacion aplicable o se acuerde
* por escrito, el programa distribuido con arreglo a la
* Licencia se distribuye <<TAL CUAL>>,
* SIN GARANTIAS NI CONDICIONES DE NINGUN TIPO, ni expresas
* ni implicitas.
* Vease la Licencia en el idioma concreto que rige
* los permisos y limitaciones que establece la Licencia.
*/
package es.mitc.redes.urbanismoenred.servicios.comunes;

import es.mitc.redes.urbanismoenred.data.fip.FIP;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Tramite;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;

import es.mitc.redes.urbanismoenred.servicios.dal.ExcepcionPersistencia;
import es.mitc.redes.urbanismoenred.servicios.dal.GestorFipListener;
import es.mitc.redes.urbanismoenred.servicios.dal.GestorFipLocal;
import es.mitc.redes.urbanismoenred.servicios.validacion.ContextoValidacion;
import es.mitc.redes.urbanismoenred.servicios.validacion.Estado;
import es.mitc.redes.urbanismoenred.utils.excepciones.RedesException;


/**
 *
 * @author Arnaiz Consultores
 */
@Stateless
public class GestionIntroduccionFIPenSistemaBean implements GestionIntroduccionFIPenSistemaRemote,
		GestionIntroduccionFIPenSistemaLocal {
    
    private final static Logger log = Logger.getLogger(GestionIntroduccionFIPenSistemaBean.class);
    private final static String FIP_FILENAME = "fip.xml";
    private final static String ZIP_FIP_FILENAME = "fip.zip";
    // TODO jgarzon pasar esto a archivo de configuración.
    private final static int NUM_MAX_VERSIONES = 1000;
	private static final String DIRECTORIO_TRABAJO = "var"+File.separator+"FIPs";

    // --- Variables de conexion a otros EJB --- //
    @EJB
    private GestorFipLocal gestorFip;
	
	// Conexion con el bean de gestion de recintos
	@EJB
    private ServicioGestionXMLLocal gestXMLSvc;
	
	private class GuardadoFipListener implements GestorFipListener {

		private ContextoValidacion contexto;
		
		public GuardadoFipListener(ContextoValidacion contexto) {
			this.contexto = contexto;
		}
		
		@Override
		public void estadoActualizado(int paso, int total, int accion,
				int totalAcciones) {
			// Fórmula mágica que me da el porcentaje de progreso.
			// Estimo que el borrado de un FIP tarda el 30% de tiempo mientras
			// que el guardado lleva el 70% restante.
			
			// Esto es un cálculo orientativo, ya que parte de la tarea se 
			// ejecuta cuando se finaliza la llamada a guardar, que es cuando
			// se ejecuta el commit de la transacción.
			int porcentaje = 0;
			if (total == PASOS_BORRADO) {
				porcentaje = (paso-1)*5+(accion*5)/totalAcciones;
			} else {
				porcentaje = 35+(paso-1)*7+(accion*7)/totalAcciones;
			}
			
			contexto.putParametro(ContextoValidacion.PROGRESO, porcentaje);
		}
		
	}
	
	/**
	 * Borra un directorio que contiene un FIP.
	 * 
	 * @param root
	 * @return
	 */
	private boolean borrarDirectorio(File root) {
		boolean exito = true;
		
		if (root.isDirectory()) {
			File[] children = root.listFiles();
			for(int i = 0; i < children.length; i++) {
				if (children[i].isDirectory()) {
					exito &= borrarDirectorio(children[i]);
				} else {
					exito &= children[i].delete();
				}
			}
		}
		exito &= root.delete();
		
		return exito;
	}

	/**
	 * 
	 * @param sourceFileName
	 * @param codTramite
	 * @return
	 * @throws RedesException
	 */
	private String copiarZIP(String sourceFileName, String codTramite) throws  RedesException {

		if (System.getenv("REDES_PATH") == null) {
			throw new RedesException("No se ha definido la variable de entorno REDES_PATH. Revise la configuración del sistema.");
		}
		String baseDirPath = System.getenv("REDES_PATH")+ File.separator + DIRECTORIO_TRABAJO + File.separator + codTramite;
		
		File sourceFile = new File( sourceFileName );
		
		FileChannel source = null;
		FileChannel destination = null;

		// Se permite enviar un mismo FIP varias veces.
		// Sólo se podrá enviar un FIP si no hay ningún proceso de validación activo sobre la última versión del FIP.
		// Cada versión del FIP recibida se almacenará para preservar la versión que se recibió.
		// Para ello lo que se hará será crear un diretorio cuyo nombre sea el del código del trámite del FIP.
		// Se crearán subdirectorios por cada versión del FIP. Se denominarán con un cardinal que indique el número de versión recibida.
		String dirVersion = crearDirectorioVersion(baseDirPath);
		
		String destFileName = dirVersion + File.separator + ZIP_FIP_FILENAME;
		
		log.debug("Se copia el fichero que se ha subido al sistema en:"+ sourceFileName+" a "+destFileName+" para posteriormente desplegarlo" );
		
		File destFile = new File( destFileName );
		
		if (destFile.exists()) {
			throw new RedesException("Ya existe el archivo " + destFileName);
		}
		
		try {
			// Si no es capaz de moverlo intento copiarlo por otro mecanismo
			if (!sourceFile.renameTo( destFile )) {

				source = new FileInputStream(sourceFile).getChannel();
				destFile.createNewFile();
				destination = new FileOutputStream(destFile).getChannel();
				
				long count = 0;
				long size = source.size();
				while((count += destination.transferFrom(source, 0, size))<size);

			} else {
				log.debug("Copiado ok.");
			}

		} catch (Exception e) {
			log.error("Error copiando archivo."+e);
			// Rename proccess
            throw new RedesException("Error copiando archivo." + e);

		} finally {
			try {
				if(source != null) {
					source.close();
				}
				if(destination != null) {
					destination.close();
				}
			} catch (IOException e) {
				log.warn(e.getMessage());
			}
		}
		return dirVersion;
	}
	
	/**
	 * 
	 * @param base
	 * @return
	 * @throws RedesException
	 */
	private String crearDirectorioVersion(String base) throws  RedesException {
		File dirBase = new File(base);
		File destDir;
		
		if (!dirBase.exists()) {
			if (!dirBase.mkdirs()) {
				throw new RedesException("No se ha podido crear el directorio " + base);
			}
		}
		
		for (int contador = 0; contador < NUM_MAX_VERSIONES; contador++) {
			destDir = new File(base + System.getProperty("file.separator")+contador);
			if (!destDir.exists()) {
				destDir.mkdir();
				return base + System.getProperty("file.separator")+contador;
			}
		}
		
		throw new RedesException("Se ha alcanzado el número máximo de versiones ("+NUM_MAX_VERSIONES+")");
	}
	
	/**
	 * 
	 * @param zipName
	 * @param contexto 
	 * @param estadoPrevio 
	 * @throws RedesException
	 */
	private void descomprimir(String zipName, ContextoValidacion contexto, Estado estadoPrevio) throws RedesException {
		try {
			FileInputStream fin = new FileInputStream(zipName + File.separator + ZIP_FIP_FILENAME);
			ZipInputStream zis = new ZipInputStream(fin,Charset.forName("Cp437"));
			
            log.info("Descomprimiendo fichero zip " + zipName);

            ZipEntry entry;
            FileOutputStream fout;
            BufferedOutputStream bos;
            int size;
            File f;
            byte[] buffer = new byte[4096];
            
            int total = 0;
            int c = 0;
            
            // Lo recorro una vez para ver su tamaño
            while ((entry = zis.getNextEntry()) != null) {
            	total++;
            	zis.closeEntry();
            }
            zis.close();
            fin.close();
           
            fin = new FileInputStream(zipName + File.separator + ZIP_FIP_FILENAME);
            zis = new ZipInputStream(fin,Charset.forName("Cp437"));
            
            // Luego procedo a descomprimir
            while ((entry = zis.getNextEntry()) != null) {
            	c++;
                log.info("Descomprimiendo el fichero " + entry.getName() + " " + c +"/"+total);
                
                if (entry.isDirectory()) {
                	f = new File(zipName + File.separator + entry.getName());
                	if (!f.exists()) {
                		if (!f.mkdirs()) {
                			log.warn("No se ha podido crear el directorio " + 
                					zipName + 
                					File.separator + 
                					entry.getName());
                		}
                	}
                	
                } else{
                	// En ocasiones un archivo se presenta antes que su directorio.
                	// esta comprobación nos evitará sustos.
                	f = new File(zipName + File.separator + entry.getName());
                	f = new File(f.getParent());
                	
                	if (!f.exists()) {
                		if (!f.mkdirs()) {
                			log.warn("No se ha podido crear el directorio " + 
                					f.getPath());
                		}
                	}
                	
                	fout = new FileOutputStream(zipName + File.separator + entry.getName());
                	
                	bos = new BufferedOutputStream(fout, buffer.length);
                	while ((size = zis.read(buffer, 0, buffer.length)) != -1) {
                		bos.write(buffer, 0, size);
                    }
                	bos.flush();
                    bos.close();
                    zis.closeEntry();
                    fout.close();
                }   
                contexto.putParametro(ContextoValidacion.PROGRESO, new Integer((c*100)/total));
            }
            zis.close();
            fin.close();
        } catch (IOException ioe) {
            log.error("Se ha producido un error descomprimiendo el fichero zip. ", ioe);
            contexto.setEstado(estadoPrevio);
            throw new RedesException("Fichero ZIP inválido ");
        }
	}
	
	/*
	 * (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.servicios.basicos.validacion.GestionIntroduccionFIPenSistemaLocal#desplegarFIP(es.mitc.redes.urbanismoenred.servicios.validacion.ContextoValidacion)
	 */
	@Override
	public void desplegarFIP(ContextoValidacion contexto) throws RedesException {
		
		String codTramite = contexto.getCodigoFip();
		
		String sourceFile = (String)contexto.getParametro(ContextoValidacion.RUTA_FIP);
		log.info("Iniciando proceso de despliegue para trámite " + codTramite  + " en archivo " + sourceFile );
		Estado estadoPrevio = contexto.getEstado();
	    contexto.setEstado(Estado.DESPLEGANDO);
	    contexto.putParametro(ContextoValidacion.PROGRESO, 0);
	       
	    String zipName = copiarZIP(sourceFile, codTramite);
	       
	    descomprimir(zipName,contexto, estadoPrevio);
	    
	    contexto.setEstado(Estado.DESPLEGADO);
	    log.info("Archivo FIP para el tramite " + codTramite +" descomprimido correctamente.");
	}
	
	/*
	 * (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.servicios.basicos.validacion.GestionIntroduccionFIPenSistemaRemote#desplegarFIP(java.lang.String, java.lang.String)
	 */
	@Override
	public void desplegarFIP(String codTramite, String sourceFile) throws RedesException {
		ContextoValidacion contexto = new ContextoValidacion(codTramite);
		contexto.putParametro(ContextoValidacion.RUTA_FIP, sourceFile);
		desplegarFIP(contexto);
    }
	
	/*
	 * (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.servicios.validacion.GestionIntroduccionFIPenSistemaLocal#getTramitesDesplegados()
	 */
	@Override
	public Tramite[] getTramitesDesplegados() throws RedesException {
		if (System.getenv("REDES_PATH") == null) {
			throw new RedesException("No se ha definido la variable de entorno REDES_PATH. Revise la configuración del sistema.");
		}
		
		List<Tramite> desplegados = new ArrayList<Tramite>();
		File baseDir = new File(System.getenv("REDES_PATH") + File.separator + DIRECTORIO_TRABAJO);
		
		// Se asume que en el directorio de trabajo sólo va a haber directorios
		// de FIPs desplegados.
		if (baseDir.exists() && baseDir.isDirectory()) {
			
			Tramite[] tramites;
			try {
				tramites = gestorFip.getTramitesPendientes();
				File f;
				for (int i = 0 ; i < tramites.length; i++) {
					f = new File(baseDir,tramites[i].getCodigofip());
					if (f.exists() && f.isDirectory()) {
						desplegados.add(tramites[i]);
					}
				}
			} catch (ExcepcionPersistencia e) {
				throw new RedesException("No se ha podido cargar información sobre trámites. Causa: " + e.getMessage(),e);
			}
		} else {
			throw new RedesException("El directorio base no existe.");
		}
		
		return desplegados.toArray(new Tramite[0]);
	}
	
	/*
	 * (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.servicios.comunes.GestionIntroduccionFIPenSistemaLocal#getTramitesPendientes()
	 */
	@Override
	public Tramite[] getTramitesPendientes() throws RedesException {
		if (System.getenv("REDES_PATH") == null) {
			throw new RedesException("No se ha definido la variable de entorno REDES_PATH. Revise la configuración del sistema.");
		}
		
		List<Tramite> pendientes = new ArrayList<Tramite>();
		File baseDir = new File(System.getenv("REDES_PATH") + File.separator + DIRECTORIO_TRABAJO);
		
		// Se asume que en el directorio de trabajo sólo va a haber directorios
		// de FIPs desplegados.
		try {
			if (baseDir.exists() && baseDir.isDirectory()) {
				
				Tramite[] tramites = gestorFip.getTramitesPendientes();
				File f;
				for (int i = 0 ; i < tramites.length; i++) {
					if (tramites[i].getCodigofip() != null) {
						f = new File(baseDir,tramites[i].getCodigofip());
						if (!f.exists()) {
							pendientes.add(tramites[i]);
						} else {
							if (!f.isDirectory()) {
								pendientes.add(tramites[i]);
							}
						}
					} else {
						log.warn("El trámite " + tramites[i].getIden() 
							+ " no tiene código FIP, por lo que se excluye de la lista.");
					}
				}
				
			} else {
				if (!baseDir.mkdirs()) {
					return gestorFip.getTramitesPendientes();
				} else {
					throw new RedesException("El directorio base no existe y no ha podido ser creado.");
				}
			}
		} catch (ExcepcionPersistencia e) {
			throw new RedesException("No se ha podido cargar información sobre trámites. Causa: " + e.getMessage(),e);
		}
		
		return pendientes.toArray(new Tramite[0]);
	}
	
	/*
	 * (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.servicios.basicos.validacion.GestionIntroduccionFIPenSistemaLocal#getUltimaVersionFIP(java.lang.String)
	 */
	@Override
	public String getUltimaVersionFIP(String codigoFip) throws RedesException {
		if (System.getenv("REDES_PATH") == null) {
			throw new RedesException("No se ha definido la variable de entorno REDES_PATH. Revise la configuración del sistema.");
		}
		
		String baseDirPath = System.getenv("REDES_PATH") + File.separator + DIRECTORIO_TRABAJO + File.separator + codigoFip;
		
		File baseDir = new File(baseDirPath);
		File destDir;
		
		if (!baseDir.exists()) {
			throw new RedesException("No se ha encontrado el directorio " + baseDirPath);
		}
		
		for (int contador = NUM_MAX_VERSIONES; contador >= 0; contador--) {
			destDir = new File(baseDirPath + File.separator 
					+ contador + File.separator + FIP_FILENAME);
			if (destDir.exists()) {
				return destDir.getAbsolutePath();
			} 
		}
		
		throw new RedesException("No se ha encontrado ningún fichero FIP para el trámite " + codigoFip);
	}

	/*
	 * (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.servicios.basicos.validacion.GestionIntroduccionFIPenSistemaRemote#guardarDatosXML(java.lang.String)
	 */
	@Override
	public void guardarDatosXML(String codigoFip) throws  RedesException {
		guardarDatosXML(new ContextoValidacion(codigoFip));
	}
	
	/*
	 * (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.servicios.basicos.validacion.GestionIntroduccionFIPenSistemaLocal#guardarDatosXML(es.mitc.redes.urbanismoenred.servicios.validacion.ContextoValidacion)
	 */
	@Override
	public void guardarDatosXML(ContextoValidacion contexto)
			throws RedesException {
		log.debug("Guardando el fip.xml en la base de datos para el trámite:"+contexto.getCodigoFip());
		
        FIP fip = null;

		// Cargamos en el objeto cons el fip.xml que esta en el sistema
		String xmlFip = getUltimaVersionFIP(contexto.getCodigoFip());
		
		log.debug("Cargando FIP xml: "+xmlFip );
		fip = gestXMLSvc.obtenerObjetoFIPdelXML( xmlFip );
		
		if (!contexto.getCodigoFip().equals(fip.getTRAMITE().getCodigo())) {
			// Si intentan introducir un FIP con un trámite distinto al que se
			// pretende subir se elimina del directorio.
			File f = new File(xmlFip);
			borrarDirectorio(f.getParentFile());
			
			throw new RedesException("El FIP subido tiene un código distinto a " + contexto.getCodigoFip());
		}
        
        log.info("Objeto FIP cargado en memoria con exito");
	
         // Guardamos el objeto FIP en base de datos
        GuardadoFipListener listener = null;
        try {
        	contexto.setEstado(Estado.GUARDANDO);
        	contexto.putParametro(ContextoValidacion.PROGRESO, 0);
        	
        	// Primero borro cualquier dato que hubiera anteriormente sobre el 
        	// trámite.
        	listener = new GuardadoFipListener(contexto);
        	gestorFip.addListener(listener);
        	
        	gestorFip.borrarFip(contexto.getCodigoFip());
        	
			gestorFip.guardarFip(fip);
			
			contexto.setEstado(Estado.GUARDADO);
			
		} catch (ExcepcionPersistencia e) {
			if (listener != null) {
				gestorFip.removeListener(listener);
			}
			log.error(e.getMessage());
			contexto.setEstado(Estado.SUBIDO);
			throw new RedesException(e.getMessage(), e);
		}
		
		contexto.putParametro(ContextoValidacion.PROGRESO, new Integer(100));
	}
}


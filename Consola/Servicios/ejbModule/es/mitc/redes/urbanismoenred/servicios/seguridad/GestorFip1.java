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
package es.mitc.redes.urbanismoenred.servicios.seguridad;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.apache.log4j.Logger;

import es.mitc.redes.urbanismoenred.data.fip.FIP;
import es.mitc.redes.urbanismoenred.data.fip.PlanDigital;
import es.mitc.redes.urbanismoenred.data.rpm.diccionario.Ambito;
import es.mitc.redes.urbanismoenred.data.rpm.diccionario.Centroproduccion;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Documento;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Planshp;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Tramite;
import es.mitc.redes.urbanismoenred.data.rpm.seguridad.Fip1;
import es.mitc.redes.urbanismoenred.servicios.comunes.GestionIntroduccionFIPenSistemaLocal;
import es.mitc.redes.urbanismoenred.servicios.comunes.ServicioGestionXMLLocal;
import es.mitc.redes.urbanismoenred.servicios.comunes.ServicioMailLocal;
import es.mitc.redes.urbanismoenred.servicios.dal.GeneradorFipLocal;
import es.mitc.redes.urbanismoenred.servicios.planeamiento.ExcepcionPlaneamiento;
import es.mitc.redes.urbanismoenred.servicios.planeamiento.ServicioDiccionariosLocal;
import es.mitc.redes.urbanismoenred.servicios.planeamiento.ServicioPlaneamientoLocal;
import es.mitc.redes.urbanismoenred.utils.excepciones.RedesException;

/**
 * Session Bean implementation class GertorFip1
 * 
 * @author Arnaiz Consultores
 */
@Stateless(name = "GestorFip1")
public class GestorFip1 implements GestorFip1Local {
	
	private static final int BUFFER = 2048;
	
	private static final String DIRECTORIO_TRABAJO = "var";
	
	// Establece la ruta donde se va a copiar el archivo FIP
    private static String baseDirPath = System.getenv("REDES_PATH")+File.separator+ DIRECTORIO_TRABAJO + File.separator + "fip1" + File.separator;
	
	private static final Logger log = Logger.getLogger(GestorFip1.class);
	
	@PersistenceContext(unitName = "rpmv2")
    private EntityManager em;
	
	@EJB ( beanName = "GeneradorFipAsincrono")
	private GeneradorFipLocal generadorFip;
	
	@EJB
	private GestionIntroduccionFIPenSistemaLocal gestorFip;
	
	@EJB
	private ServicioGestionXMLLocal gestorXml;
	
	@EJB
	private ServicioMailLocal servicioCorreo;
	
	@EJB
	private ServicioPlaneamientoLocal servicioPlaneamiento;
	
	@EJB
	private ServicioDiccionariosLocal servicioDiccionario;

    /**
     * Default constructor. 
     */
    public GestorFip1() {
    }

    /**
	 * 
	 * @param fip
	 * @param tramite
	 */
	private void agregarPlanEncargado(FIP fip, Tramite tramite) {
		
		DecimalFormat format6 = new DecimalFormat("000000");
		PlanDigital planEncargado = new PlanDigital();
		planEncargado.setAmbito(format6.format(tramite.getPlan().getIdambito()));
		
		if (tramite.getPlan().getPlanshps().size() > 0) {
			planEncargado.setAMBITOAPLICACION(tramite.getPlan().getPlanshps().toArray(new Planshp[0])[0].getGeom());
		} 
		
		planEncargado.setCodigoTramite(tramite.getCodigofip());
		
		planEncargado.setInstrumento(format6.format(tramite.getPlan().getIden()));
		planEncargado.setIteracion(new BigInteger(String.valueOf(tramite.getIteracion())));
		planEncargado.setTipoTramite(format6.format(tramite.getIdtipotramite()));
		planEncargado.setNombre(tramite.getPlan().getNombre());
		
		fip.setPLANEAMIENTOENCARGADO(planEncargado);
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
	private void comprimir(File[] origenes, File destino) throws ExcepcionSeguridad {
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
	        for (File origen : origenes) {
	        	if (origen.isDirectory()) {
	        		recopilarEntradas("", origen, entradas);
	        	} else {
	        		entradas.put(origen.getName(), origen);
	        	}
	        	
	        }

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
			throw new ExcepcionSeguridad("No se ha podido generar el archivo zip", e);
		} catch (IOException e) {
			throw new ExcepcionSeguridad("No se ha podido generar el archivo zip", e);
		}
	}

	/**
	 * 
	 * @param sourceLocation
	 * @param targetLocation
	 * @throws IOException
	 */
	private void copiarDirectorio(File sourceLocation , File targetLocation)
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
	 * @param codigo
	 * @param directorio
	 * @throws RedesException 
	 */
	private void copiarDocumentos(String codigo, File directorio) throws RedesException {
		Tramite tramite = servicioPlaneamiento.getTramitePorCodigo(codigo);
		// Recorro los documentos del trámite, si esto resultara demasiado
    	// lento habría que utilizar un EntityManager y la consulta empleada
    	// para generar el Mapa.
		File dirBase = null;
		
    	
    	try {
    		dirBase = new File(gestorFip.getUltimaVersionFIP(codigo)).getParentFile();
    	} catch (RedesException re) {
    		log.warn("No se puede proceder a la copia de documentos para plantilla porque no se ha encontrado directorio del trámite " + codigo, re);
    		return;
    	}
    	
    	if (dirBase.exists() && dirBase.isDirectory()) {
    		File archivoFuente;
        	File archivoDestino;
        	FileChannel fuente;
        	FileChannel destino;
        	String archivo;
        	
    		for (Documento doc : tramite.getDocumentos()) {
    			archivo = doc.getArchivo();
				if ("\\".equals(File.separator)) {
					archivo = archivo.replace("/", File.separator);
				} else {
					archivo = archivo.replace("\\", File.separator);
				}
				archivoFuente = new File(dirBase, archivo);
				archivoDestino = new File(directorio, archivo);
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
    								
    								destino.close();
								} else {
									log.warn( "No se ha podido crear el documento " +doc.getArchivo() +" del trámite " + codigo);
								}
							} else {
								log.warn( "El documento " +doc.getArchivo() +" del trámite " + codigo + " ya existe");
							}
							
							fuente.close();
						}
					}  catch (FileNotFoundException e) {
						log.warn( "No se ha podido encontrar el documento " +doc.getArchivo() +" del trámite " + codigo);
					} catch (IOException e) {
						log.warn( "No se ha podido copiar el documento " +doc.getArchivo() +" del trámite " + codigo);
					}
				} else {
					log.warn( "No se ha podido encontrar el documento " +doc.getArchivo() +" del trámite " + codigo);
				}
        	} 
    	} else {
			log.warn( "No se han podido copiar los documentos del trámite " + codigo +". No se ha encontrado el directorio raíz.");
		}
	}

	/*
     * (non-Javadoc)
     * @see es.mitc.redes.urbanismoenred.servicios.seguridad.GestorFip1Local#generarFip1(java.lang.String, java.lang.String)
     */
	@SuppressWarnings("unchecked")
	@Override
	public File generarFip1(String codigoFip, String idioma) throws ExcepcionSeguridad {
		
        File dir = new File(baseDirPath);
        if (!dir.exists()) {
			dir.mkdirs();
		}
        
        log.debug("Ruta del directorio FIP1: " + dir.getAbsolutePath());
        try {
        	Tramite tramite = (Tramite) em.createNamedQuery("Tramite.findTramiteFromCodFip")
					.setParameter("codigoFip", codigoFip)
					.getSingleResult();
        	
			List<Fip1> fips1 = (List<Fip1>) em.createNamedQuery("Fip1.obtenerFip1")
					.setParameter("codigoFip", codigoFip)
					.getResultList();
			Fip1 fip1;
		
			if (fips1.size() > 0) {
				fip1 = fips1.get(0);
			} else {
					fip1 = new Fip1();
					fip1.setCodfip(codigoFip);
					fip1.setFechacreacion(Calendar.getInstance().getTime());
					fip1.setIdambito(tramite.getPlan().getIdambito());
					fip1.setObsoleto(true);
					
					em.persist(fip1);
			}
			
			fip1.setRuta(codigoFip+".zip");
			
			File f = new File(baseDirPath + fip1.getRuta());
			File directorioFip1 = new File(baseDirPath + fip1.getCodfip());
			if (fip1.getObsoleto() || !f.exists()) {
				if (!directorioFip1.exists()) {
					directorioFip1.mkdirs();
				}
				
				File archivoPlantilla = new File(baseDirPath + tramite.getPlan().getIdambito() + File.separator + "fip.xml");
				
				getPlantilla(tramite.getPlan().getIdambito(), idioma, false);
				
				FIP fip = gestorXml.obtenerObjetoFIPdelXML(archivoPlantilla.getAbsolutePath(), false);
				
				agregarPlanEncargado(fip, tramite);
				
				guardarFip(fip, new File(directorioFip1, "fip.xml"));
				
				comprimir( new File[] { archivoPlantilla.getParentFile(), directorioFip1}, f);
				
				fip1.setObsoleto(false);
			} 
			
			fip1.setFechadescarga(Calendar.getInstance().getTime());
			return f;
        } catch (NoResultException nre) {
			throw new ExcepcionSeguridad("No existe trámite con código FIP " + codigoFip);
		} catch (RedesException e) {
			throw new ExcepcionSeguridad("Error al cargar archivo FIP: " + e.getMessage());
		} 
	}

	/*
	 * (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.servicios.seguridad.GestorFip1Local#getCentroProduccion(int)
	 */
	@Override
	public Centroproduccion getCentroProduccion(int centroProduccion) {
		return em.find(Centroproduccion.class, centroProduccion);
	}

	/*
	 * (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.servicios.seguridad.GestorFip1Local#getCentrosProduccion()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Centroproduccion[] getCentrosProduccion() {
		List<Centroproduccion> centros = em.createNamedQuery("Centroproduccion.obtenerTodos")
				.getResultList();
		return centros.toArray(new Centroproduccion[0]);
	}

	/*
	 * (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.servicios.seguridad.GestorFip1Local#generarPlantilla(int, java.lang.String)
	 */
	@Override
	public File getPlantilla(int idAmbito, String idioma, boolean soloXML) throws ExcepcionSeguridad {
		File dir = new File(baseDirPath);
        if (!dir.exists()) {
			dir.mkdirs();
		}
		
		Fip1 plantilla = obtenerPlantilla(idAmbito);
		
		FIP fip;
		
		File archivoPlantilla = new File(baseDirPath + plantilla.getIdambito() + File.separator + "fip.xml");
		File directorio = new File(baseDirPath + plantilla.getIdambito());
		File destino = new File(dir,plantilla.getRuta());
		
		// El 13/02/2013 se ha solicitado la posibilidad de obtener sólo el XML
		// de la plantilla. Se ha pedido igualmente que el nombre del archivo
		// generado sea: codigoine+codigofip de refundido , o si no hay refundido
		// codigoine+nombreambito o si no hay codigoine el nombre del ámbito.
		// Al no haber un nombre concreto, se opta por crear un directorio
		// donde dejar el archivo y coger lo que ahí halla.
		// Como sólo se puede saber el código fip al generar el XML lo que se 
		// hace es crear ambos archivos al generar la plantilla.
		File directorioSoloXML = new File(dir,plantilla.getIdambito() + "XML");
		
		servicioDiccionario.get(Ambito.class, idAmbito);
		File destinoSoloXML = new File(baseDirPath, plantilla.getIdambito() + ".zip"); 
		
		try {
			if (plantilla.getObsoleto() == null || plantilla.getObsoleto() || !archivoPlantilla.exists()) {
				log.info("No hay plantilla actualizada, se procede a generar una...");
				// Si ya existiera el directorio lo borro para empezar de 0
				if (directorio.exists()) {
					borrarDirectorio(directorio);
				} 
				
				if (directorioSoloXML.exists()) {
					borrarDirectorio(directorioSoloXML);
				}
				
				directorio.mkdirs();
				directorioSoloXML.mkdirs();
				
				fip = generadorFip.generarPlantilla(idAmbito, idioma);
				
				log.info("Plantilla generada, guardando archivo...");
				
				guardarFip(fip, archivoPlantilla);
				
				log.info("Archivo guardado, copiando documentos adjuntos...");
				
				copiarDocumentos(fip.getCATALOGOSISTEMATIZADO().getTRAMITE().getCodigo(), directorio);
				
				Ambito ambito = servicioDiccionario.get(Ambito.class, idAmbito);
				StringBuffer nombreArchivoSoloXML = new StringBuffer();
				if (ambito.getCodigoine() != null) {
					nombreArchivoSoloXML.append(ambito.getCodigoine());
				}
				
				if (fip.getPLANEAMIENTOVIGENTE() != null) {
					nombreArchivoSoloXML.append("_");
					nombreArchivoSoloXML.append(fip.getPLANEAMIENTOVIGENTE().getTRAMITE().getCodigo());
					copiarDocumentos(fip.getPLANEAMIENTOVIGENTE().getTRAMITE().getCodigo(), directorio);
				} else {
					nombreArchivoSoloXML.append("_");
					nombreArchivoSoloXML.append(servicioDiccionario.getTraduccion(Ambito.class, idAmbito, idioma));
				}
				log.info("Documentos copiados, creando archivo comprimido " + destino.getAbsolutePath());
				nombreArchivoSoloXML.append(".zip");
				
				comprimir(new File[] {directorio}, destino);
				
				destinoSoloXML = new File(directorioSoloXML, nombreArchivoSoloXML.toString());
				comprimir (new File[] {archivoPlantilla}, destinoSoloXML);
			} else {
				// Sólo debería haber un archivo.
				if (directorioSoloXML.listFiles().length > 0) {
					destinoSoloXML = directorioSoloXML.listFiles()[0];
				} else {
					throw new ExcepcionSeguridad("No se ha encontrado archivo ZIP que contiene sólo el XML.");
				}
			}
			
			plantilla.setObsoleto(false);
			plantilla.setFechacreacion(Calendar.getInstance().getTime());
			
			if (soloXML) {
				return destinoSoloXML;
			} else {
				return destino;
			}
			
		} catch (RedesException re) {
			plantilla.setObsoleto(true);
			
			throw new ExcepcionSeguridad("Error al generar plantilla. Causa: " + re.getMessage(), re);
		} 
	}

	/*
	 * (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.servicios.seguridad.GestorFip1Local#getTramites(int)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Tramite[] getTramites(int centroProduccion) {
		List<Tramite> tramites = em.createNamedQuery("Tramite.buscarPorCentroproduccion")
				.setParameter("idCentro", centroProduccion)
				.getResultList();
		return tramites.toArray(new Tramite[0]);
	}

	/**
	 * Guarda el XML del FIP en la ruta especificada.
	 * @param fip Objeto FIP que coniene el XML.
	 * @param ruta Ruta absoluta del archivo donde se guardará el XML.
	 * @throws ExcepcionSeguridad Si ocurre cualquier error al guardar el XML en
	 * el archivo.
	 */
	private void guardarFip(FIP fip, File ruta) throws ExcepcionSeguridad {
		try {
			JAXBContext jc = JAXBContext.newInstance("es.mitc.redes.urbanismoenred.data.fip");
			
			Marshaller marshaller = jc.createMarshaller();
			
			marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			
			marshaller.marshal(fip, ruta);
		} catch (JAXBException e) {
			throw new ExcepcionSeguridad("Error al generar el archivo " + ruta + ". Causa: " + e.getMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.servicios.seguridad.GestorFip1Local#marcarComoObsoleto(java.lang.Integer, java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void marcarComoObsoleto(Integer ambito, String codigoFip) {
		List<Fip1> fips;
		if (codigoFip != null) {
			fips = em.createNamedQuery("Fip1.obtenerFip1")
					.setParameter("codigoFip", codigoFip)
					.getResultList();
		} else {
			if (ambito != null) {
				fips = em.createNamedQuery("Fip1.buscarPorAmbito")
						.setParameter("idAmbito", ambito)
						.getResultList();
			} else {
				fips = em.createNamedQuery("Fip1.obtenerTodos")
						.getResultList();
			}
		}
		
		for (Fip1 fip : fips) {
			if (fip.getObsoleto() != null && !fip.getObsoleto()) {
				fip.setObsoleto(true);
				if (fip.getFechadescarga() != null) {
					notificarFipObsoleto(fip.getCodfip());
				}
				
			}
		}
	}

	/**
	 * Envía correos a los centros de producción que se hayan descargado un FIP
	 * que ha quedado obsoleto.
	 * 
	 * @param codfip Código FIP
	 */
	private void notificarFipObsoleto(String codfip) {
		Centroproduccion centro = null;
		try {
			Tramite tramite = (Tramite) em.createNamedQuery("Tramite.findTramiteFromCodFip")
					.setParameter("codigoFip", codfip)
					.getSingleResult();
			if (tramite.getFechaconsolidacion() != null) {
				centro = em.find(Centroproduccion.class, tramite.getIdcentroproduccion());
				if (centro != null && centro.getMail() != null) {
					servicioCorreo.sendEmail("FIP1 " + codfip + " obsoleto.", 
							"El FIP1 descargado correspondiente al trámite " + codfip + " ha quedado obsoleto por cambios en la base de datos. Por favor, proceda a la descarga del FIP1 actualizado.", 
								new InternetAddress(centro.getMail()));
				}
			}
		} catch (NoResultException nre) {
			// No está, raro pero no afecta.
			log.warn("No se ha encontrado trámite con código: " + codfip);
		} catch (AddressException e) {
			log.warn("No se ha podido notificar al centro de producción " + centro.getIden() + " porque su dirección de correo es inválida");
		}
	}

	/**
	 * Devuelve el registro FIP1 correspondiente al ámbito especificado.
	 * 
	 * Si no existe lo crea.
	 * 
	 * @param idAmbito
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Fip1 obtenerPlantilla(int idAmbito) {
		List<Fip1> plantillas = (List<Fip1>) em.createNamedQuery("Fip1.obtenerPlantilla")
				.setParameter("idAmbito", idAmbito)
				.getResultList();
		
		Fip1 plantilla;
		if (plantillas.size() > 0) {
			plantilla = plantillas.get(0);
		} else {
			plantilla = new Fip1();
			plantilla.setFechacreacion(Calendar.getInstance().getTime());
			plantilla.setIdambito(idAmbito);
			plantilla.setObsoleto(true);
			plantilla.setRuta(idAmbito+".zip");
			
			em.persist(plantilla);
		}
		
		return plantilla;
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
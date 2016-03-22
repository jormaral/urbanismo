package es.mitc.redes.urbanismoenred.servicios.refundido;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;

import es.mitc.redes.urbanismoenred.data.rpm.refundido.Determinacion;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Documento;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Documentoshp;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Entidad;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Entidadlin;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Entidadpnt;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Entidadpol;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Propiedadrelacion;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Traza;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.TrazaId;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Vectorrelacion;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Relacion;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Tramite;
import es.mitc.redes.urbanismoenred.servicios.comunes.GestionIntroduccionFIPenSistemaLocal;
import es.mitc.redes.urbanismoenred.utils.excepciones.RedesException;

/**
 * Session Bean implementation class CopiadorRefundido
 */
@Stateless
public class CopiadorRefundido implements CopiadorRefundidoLocal {
	
	private static final Logger log = Logger.getLogger(CopiadorRefundido.class);
	
	private static final String DIRECTORIO_TRABAJO = "var";
	
	private static final String UPDATE_GEOM = "UPDATE refundido.%s SET geom=multi(geometryfromtext('%s')) where iden= %d";
	
	@PersistenceContext(unitName = "rpmv2")
	private EntityManager em;
	
	@EJB
	private GestionIntroduccionFIPenSistemaLocal gestorFips;

    /**
     * Default constructor. 
     */
    public CopiadorRefundido() {
    }

    /*
	 * (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.servicios.refundido.CopiadorRefundidoLocal#copiar(int, es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Documento, es.mitc.redes.urbanismoenred.data.rpm.refundido.Tramite)
	 */
	@Override
	public Documento copiar(int proceso,
			es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Documento original,
			Tramite copiaTramite) {
		Documento copia = getReferencia(proceso, Documento.class, original.getIden());
		
		if (copia == null) {
			copia = new Documento();
			copia.setArchivo(original.getArchivo());
			copia.setComentario(original.getComentario());
			if (original.getDocumento() != null) {
				copia.setDocumento(copiar(proceso, original.getDocumento(), copiaTramite));
			}
			copia.setEscala(original.getEscala());
			copia.setIdgrupodocumento(original.getIdgrupodocumento());
			copia.setIdtipodocumento(original.getIdtipodocumento());
			copia.setNombre(original.getNombre());
			copia.setTramite(copiaTramite);
			
			em.persist(copia);
			em.flush();
			
			guardarReferencia(proceso, Documento.class.getSimpleName(), original.getIden(), copia.getIden());
			
			for (es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Documentoshp ds : original.getDocumentoshps() ) {
				copiar(proceso, ds, copiaTramite);
			}
			
			copiarFicheros(copia, original);
		}
		return copia;
	}
	
	/**
	 * 
	 * @param proceso
	 * @param original
	 * @param copiaTramite
	 */
	private void copiar(int proceso, 
			es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Documentoshp original, 
			Tramite copiaTramite) {
		Documentoshp copia = getReferencia(proceso, Documentoshp.class, original.getIden());
		
		if (copia == null) {
			copia = new Documentoshp();
			
			copia.setDocumento(copiar(proceso, original.getDocumento(),copiaTramite));
			copia.setGeom(original.getGeom());
			copia.setHoja(original.getHoja());
			
			em.persist(copia);
			em.flush();
			
			guardarReferencia(proceso, Documentoshp.class.getSimpleName(), original.getIden(), copia.getIden());
			
			em.createNativeQuery(String.format(UPDATE_GEOM, "documentoshp", original.getGeom(), copia.getIden())).executeUpdate();
			em.flush();
			em.refresh(copia);
		}
	}
	
	/*
     * (non-Javadoc)
     * @see es.mitc.redes.urbanismoenred.servicios.refundido.CopiadorRefundidoLocal#copiar(int, es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidadlin)
     */
    @Override
    public void copiar(int idProceso, es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidadlin original) {
		
		Entidadlin copia = em.find(Entidadlin.class, 
				getReferencia(idProceso, Entidadlin.class.getSimpleName(), original.getIden()));
		if (copia == null) {
			copia = new Entidadlin();
			
			copia.setEntidad(getReferencia(idProceso, Entidad.class, original.getEntidad().getIden()));
			copia.setGeom(original.getGeom());
			
			em.persist(copia);
			em.flush();
			
			guardarReferencia(idProceso, Entidadlin.class.getSimpleName(), original.getIden(), copia.getIden());
			
			em.createNativeQuery(String.format(UPDATE_GEOM, "entidadlin", original.getGeom(), copia.getIden())).executeUpdate();
			em.flush();
			em.refresh(copia);
		}
	}
	
	/*
     * (non-Javadoc)
     * @see es.mitc.redes.urbanismoenred.servicios.refundido.CopiadorRefundidoLocal#copiar(int, es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidadpnt)
     */
    @Override
    public void copiar(int idProceso, es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidadpnt original) {
		Entidadpnt copia = em.find(Entidadpnt.class, 
				getReferencia(idProceso, Entidadpnt.class.getSimpleName(), original.getIden()));
		if (copia == null) {
			copia = new Entidadpnt();
			
			copia.setEntidad(getReferencia(idProceso, Entidad.class, original.getEntidad().getIden()));
			copia.setGeom(original.getGeom());
			
			em.persist(copia);
			em.flush();
			
			guardarReferencia(idProceso, Entidadpnt.class.getSimpleName(), original.getIden(), copia.getIden());
			
			em.createNativeQuery(String.format(UPDATE_GEOM, "entidadpnt", original.getGeom(), copia.getIden())).executeUpdate();
			em.flush();
			em.refresh(copia);
		}
	}

	/*
     * (non-Javadoc)
     * @see es.mitc.redes.urbanismoenred.servicios.refundido.CopiadorRefundidoLocal#copiar(int, es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidadpol)
     */
    @Override
    public void copiar(int idProceso, es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidadpol original) {
		
		Entidadpol copia = em.find(Entidadpol.class, 
				getReferencia(idProceso, Entidadpol.class.getSimpleName(), original.getIden()));
		if (copia == null) {
			copia = new Entidadpol();
			
			copia.setEntidad(getReferencia(idProceso, Entidad.class, original.getEntidad().getIden()));
			copia.setGeom(original.getGeom());
			
			em.persist(copia);
			em.flush();
			
			guardarReferencia(idProceso, Entidadpol.class.getSimpleName(), original.getIden(), copia.getIden());
			
			em.createNativeQuery(String.format(UPDATE_GEOM, "entidadpol", original.getGeom(), copia.getIden())).executeUpdate();
			em.flush();
			em.refresh(copia);
		}
	}

	/*
     * (non-Javadoc)
     * @see es.mitc.redes.urbanismoenred.servicios.refundido.CopiadorRefundidoLocal#copiar(int, es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Propiedadrelacion, es.mitc.redes.urbanismoenred.data.rpm.refundido.Relacion)
     */
	@Override
	public void copiar(int proceso, es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Propiedadrelacion original,
			Relacion relacion) {
		Propiedadrelacion copia = em.find(Propiedadrelacion.class, 
				getReferencia(proceso, Propiedadrelacion.class.getSimpleName(), original.getIden()));
		
		if (copia == null) {
			copia = new Propiedadrelacion();
			copia.setIddefpropiedad(original.getIddefpropiedad());
			copia.setRelacion(relacion);
			copia.setValor(original.getValor());
			
			em.persist(copia);
			em.flush();
			
			guardarReferencia(proceso, Propiedadrelacion.class.getSimpleName(), original.getIden(), copia.getIden());
		}
	}

	/*
	 * (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.servicios.refundido.CopiadorRefundidoLocal#copiar(int, es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Relacion, es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Vectorrelacion, es.mitc.redes.urbanismoenred.data.rpm.refundido.Tramite)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Relacion copiar(
			int proceso,
			es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Relacion relacion,
			es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Vectorrelacion origen, Tramite copiaTramite) {
		Relacion copia = getReferencia(proceso, Relacion.class, relacion.getIden());
		
		if (copia == null && 
				getReferencia(proceso, Relacion.class.getSimpleName(), relacion.getIden()) == 0) {
			copia = new Relacion();
			copia.setIddefrelacion(relacion.getIddefrelacion());
			copia.setTramiteByIdtramitecreador(copiaTramite);
			
			em.persist(copia);
			em.flush();
			
			guardarReferencia(proceso, Relacion.class.getSimpleName(), relacion.getIden(), copia.getIden());
			
			for (es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Propiedadrelacion pr : relacion.getPropiedadrelacions()) {
				copiar(proceso, pr, copia);
			}
			
			// Copiamos las relaciones a través de los vectores, por lo que
			// aquí sólo nos aseguramos de copiar aquellos vectores que no sean
			// del que se parte. Así evitamos ciclos.
			for(es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Vectorrelacion vr : relacion.getVectorrelacions()) {
				if (vr != origen) {
					copiar(proceso, vr, copiaTramite);
				}
			}
			
			// Copiamos aquellos vectores que apunten a esta relación como
			// precedente (en regulaciones específicas
			List<es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Vectorrelacion> vectores = em.createQuery("SELECT vr FROM es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Vectorrelacion vr WHERE vr.iddefvector = 41 AND vr.valor = :valor")
					.setParameter("valor", relacion.getIden()).getResultList();

			for (es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Vectorrelacion vr : vectores) {
				copiar(proceso, vr, copiaTramite);
			}
		}
		return copia;
	}

    /*
	 * (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.servicios.refundido.CopiadorRefundidoLocal#copiar(int, es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Vectorrelacion, es.mitc.redes.urbanismoenred.data.rpm.refundido.Tramite)
	 */
	@Override
	public void copiar(int proceso, es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Vectorrelacion original,
			Tramite copiaTramite) {
		Vectorrelacion copia = em.find(Vectorrelacion.class, 
				getReferencia(proceso, Vectorrelacion.class.getSimpleName(), original.getIden()));
		String tabla;
		if (copia == null) {
			copia = new Vectorrelacion();
			copia.setIddefvector(original.getIddefvector());
			switch (original.getIddefvector()) {
				case 4:
				case 5:
				case 42:
					tabla = Entidad.class.getSimpleName();
					break;
				case 41:
					tabla = Relacion.class.getSimpleName();
					break;
				default:
					tabla = Determinacion.class.getSimpleName();
					break;
			}
			Relacion relacion = copiar(proceso, original.getRelacion(), original, copiaTramite);
			if (relacion != null) {
				copia.setRelacion(relacion);
				
				copia.setValor(getReferencia(proceso, tabla, original.getValor()));
				
				em.persist(copia);
				em.flush();
				
				guardarReferencia(proceso, Vectorrelacion.class.getSimpleName(), original.getIden(), copia.getIden());
			}
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
                if (!targetLocation.mkdirs()) {
                	log.warn("No se ha podido crear el directorio " + targetLocation.getAbsolutePath());
                }
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
     * @param destino
     * @param origen
     */
    private void copiarFicheros(Documento destino, 
    		es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Documento origen) {
		try {
			File dirBase = new File(gestorFips.getUltimaVersionFIP(origen.getTramite().getCodigofip())).getParentFile();
			// Establece la ruta donde se va a copiar el archivo FIP
	        String baseDirPath = System.getenv("REDES_PATH")+File.separator
	        		+ DIRECTORIO_TRABAJO + File.separator+"refundido" 
	        		+ File.separator + "FIPs" + File.separator 
	        		+ destino.getTramite().getCodigofip();
	        
	        File dir = new File(baseDirPath);
	        if (!dir.exists()) {
				dir.mkdirs();
			}
	    	
			if (dirBase.exists() && dirBase.isDirectory()) {
				String archivo = origen.getArchivo();
				if ("\\".equals(File.separator)) {
					archivo = archivo.replace("/", File.separator);
				} else {
					archivo = archivo.replace("\\", File.separator);
				}
				File archivoFuente = new File(dirBase, archivo);
				File archivoDestino = new File(dir, archivo);
				log.debug("Copiando " + archivoFuente.getAbsolutePath() + " a " + archivoDestino.getAbsolutePath());
				if (archivoFuente.exists()) {
					try {
						if (archivoFuente.isDirectory()) {
							copiarDirectorio(archivoFuente, archivoDestino);
						} else {
							FileChannel fuente = new FileInputStream(archivoFuente).getChannel();
							if (!archivoDestino.exists()) {
								archivoDestino.getParentFile().mkdirs();
								if (archivoDestino.createNewFile()) {
									FileChannel salida = new FileOutputStream(archivoDestino).getChannel();
									
									long count = 0;
									long size = fuente.size();
									while((count += salida.transferFrom(fuente, 0, size))<size);
									fuente.close();
									salida.close();
								} else {
									log.warn("No se ha podido crear el documento " +origen.getArchivo() +" del trámite " + origen.getTramite().getCodigofip());
								}
							} else {
								log.warn("El documento " +origen.getArchivo() +" del trámite " + origen.getTramite().getCodigofip() + " ya existe");
							}
						}
					}  catch (FileNotFoundException e) {
						log.warn("No se ha podido encontrar el documento " +origen.getArchivo() +" del trámite " + origen.getTramite().getCodigofip());
					} catch (IOException e) {
						log.warn("No se ha podido copiar el documento " +origen.getArchivo() +" del trámite " + origen.getTramite().getCodigofip());
					}
				} else {
					log.warn("No se ha podido encontrar el documento " +origen.getArchivo() +" del trámite " + origen.getTramite().getCodigofip());
				}
			} else {
				log.warn("No se han podido copiar los documentos del trámite " + origen.getTramite().getCodigofip() +". No se ha encontrado el directorio raíz.");
			}
		} catch (RedesException e) {
			log.warn("No se ha podido obtener la última versión del FIP. " + e.getMessage());
		}
    }
    
    /*
     * (non-Javadoc)
     * @see es.mitc.redes.urbanismoenred.servicios.refundido.CopiadorRefundidoLocal#getReferencia(int, java.lang.Class, int)
     */
    @Override
	public <T> T getReferencia(int proceso, Class<T> clase, int idplaneamiento) {
		T objeto = em.find(clase, getReferencia(proceso, clase.getSimpleName(), idplaneamiento));
		if (objeto != null) {
			// Las relaciones inversas no se están actualizando
			em.refresh(objeto);
		}
		
		return objeto;
	}

	/*
	 * (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.servicios.refundido.CopiadorRefundidoLocal#getReferencia(int, java.lang.String, int)
	 */
	@Override
	public int getReferencia(int proceso, String tabla, int idplaneamiento) {
		Traza traza = em.find(Traza.class, new TrazaId(proceso,tabla, idplaneamiento));
		if (traza != null) {
			return traza.getIdrefundido();
		} else {
			return 0;
		}
	}

	/*
	 * (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.servicios.refundido.CopiadorRefundidoLocal#guardarReferencia(int, java.lang.String, int, int)
	 */
	@Override
	public void guardarReferencia(int proceso, String tabla,
			int idplaneamiento, int idrefundido) {
		log.debug("Guardando referencia " + proceso + ", " + tabla + ", " + idplaneamiento + ", "+ idrefundido);
		Traza traza = new Traza(new TrazaId(proceso, tabla, idplaneamiento),idrefundido);
		em.persist(traza);
	}

}

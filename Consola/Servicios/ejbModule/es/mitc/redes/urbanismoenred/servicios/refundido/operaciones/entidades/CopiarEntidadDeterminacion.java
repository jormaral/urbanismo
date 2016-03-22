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
package es.mitc.redes.urbanismoenred.servicios.refundido.operaciones.entidades;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;

import es.mitc.redes.urbanismoenred.data.rpm.refundido.Casoentidaddeterminacion;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Documento;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Documentocaso;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Documentoshp;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Entidad;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Entidaddeterminacion;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Entidaddeterminacionregimen;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Regimenespecifico;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Tramite;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Vinculocaso;
import es.mitc.redes.urbanismoenred.servicios.comunes.GestionIntroduccionFIPenSistemaLocal;
import es.mitc.redes.urbanismoenred.servicios.refundido.ContextoRefundido;
import es.mitc.redes.urbanismoenred.servicios.refundido.ContextoRefundido.LOG;
import es.mitc.redes.urbanismoenred.servicios.refundido.CopiadorRefundidoLocal;
import es.mitc.redes.urbanismoenred.servicios.refundido.ExcepcionRefundido;
import es.mitc.redes.urbanismoenred.servicios.refundido.GestorAportacionesLocal;
import es.mitc.redes.urbanismoenred.utils.excepciones.RedesException;

/**
 * @author Arnaiz Consultores
 *
 */
@Stateless( name = "SuperposicionYAcumulacion")
public class CopiarEntidadDeterminacion implements CopiarEntidadDeterminacionLocal {
	private static final String DIRECTORIO_TRABAJO = "var";
	
	private static final Logger log = Logger.getLogger(CopiarEntidadDeterminacion.class);

	protected static final String LISTA_CARACTERES = "refundido.operacion.entidad.listaCaracteres";

	protected static final String ES_SUPERPOSICION = "refundido.operacion.entidad.esSuperposicion";
	
	@PersistenceContext(unitName = "rpmv2")
	protected EntityManager em;
	
	@EJB
	protected GestorAportacionesLocal gestorAportaciones;
	
	@EJB
	private GestionIntroduccionFIPenSistemaLocal gestorFips;
	
	@EJB
	protected CopiadorRefundidoLocal referencias;

	/**
	 * 
	 * @param proceso
	 * @param original
	 * @param copiaTramite
	 * @return
	 */
	private Documento copiar(int proceso,
			es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Documento original,
			Tramite copiaTramite) {
		Documento copia = referencias.getReferencia(proceso, Documento.class, original.getIden());
		
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
			
			referencias.guardarReferencia(proceso, Documento.class.getSimpleName(), original.getIden(), copia.getIden());
			
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
	private void copiar(int proceso, Documentocaso original, Casoentidaddeterminacion ced, Tramite copiaTramite) {
		Documentocaso copia = referencias.getReferencia(proceso, Documentocaso.class, original.getIden());
		if (copia == null) {
			copia = new Documentocaso();
			copia.setCasoentidaddeterminacion(ced);
			copia.setDocumento(original.getDocumento());
			em.persist(copia);
			em.flush();
		}
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
		Documentoshp copia = referencias.getReferencia(proceso, Documentoshp.class, original.getIden());
		
		if (copia == null) {
			copia = new Documentoshp();
			
			copia.setDocumento(copiar(proceso, original.getDocumento(),copiaTramite));
			copia.setGeom(original.getGeom());
			copia.setHoja(original.getHoja());
			
			em.persist(copia);
			em.flush();
			
			referencias.guardarReferencia(proceso, Documentoshp.class.getSimpleName(), original.getIden(), copia.getIden());
		}
	}
	
	/**
	 * 
	 * @param proceso
	 * @param original
	 * @param caso 
	 * @param copiaTramite
	 * @return
	 */
	private Entidaddeterminacionregimen copiar(int proceso, 
			Entidaddeterminacionregimen original, 
			Integer superposicion, 
			Casoentidaddeterminacion caso, 
			Casoentidaddeterminacion casoAplicacion) {
		Entidaddeterminacionregimen copia = new Entidaddeterminacionregimen();
		copia.setCasoentidaddeterminacionByIdcaso(caso);
		copia.setCasoentidaddeterminacionByIdcasoaplicacion(casoAplicacion);
		copia.setDeterminacion(original.getDeterminacion());
		copia.setOpciondeterminacion(original.getOpciondeterminacion()); 
		copia.setSuperposicion(superposicion);
		copia.setValor(original.getValor());
			
		em.persist(copia);
		em.flush();
			
		for (Regimenespecifico re : original.getRegimenespecificos()) {
			if (re.getRegimenespecifico() == null) {
				copiar(proceso, re, null, copia);
			}
		}
		
		return copia;
	}
	
	/**
	 * 
	 * @param proceso
	 * @param original
	 * @param copiaTramite
	 * @return
	 */
	private Regimenespecifico copiar(int proceso,
			Regimenespecifico original, 
			Regimenespecifico padre,
			Entidaddeterminacionregimen edr) {
		Regimenespecifico copia = new Regimenespecifico();
			
		copia.setEntidaddeterminacionregimen(edr);
		copia.setNombre(original.getNombre());
		copia.setOrden(original.getOrden());
		copia.setTexto(original.getTexto());
		if (padre != null) {
			copia.setRegimenespecifico(padre);
		}
		
		em.persist(copia);
		em.flush();
		
		for (Regimenespecifico hijo : original.getRegimenespecificos()) {
			copiar(proceso, hijo, copia, edr);
		}
			
		return copia;
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
     * @see es.mitc.redes.urbanismoenred.servicios.refundido.operaciones.entidades.EjecutorLocal#ejecutar(int, int, es.mitc.redes.urbanismoenred.servicios.refundido.ContextoRefundido)
     */
	@SuppressWarnings("unchecked")
	@Override
	public void ejecutar(Entidad operada, Entidad operadora,
			ContextoRefundido contexto) throws ExcepcionRefundido {
		
		boolean isSuperposicion = (boolean) (contexto.getParametro(ES_SUPERPOSICION) != null ? contexto.getParametro(ES_SUPERPOSICION):false);
		Integer superposicion = 0;
		String texto = isSuperposicion ? "refundido.operacion.entidad.superposicion.mensaje" : "refundido.operacion.entidad.acumulacion.mensaje";
		
		contexto.logTraducido(LOG.INFO, texto, operadora.getCodigo(), operadora.getTramite().getPlan().getCodigo(), operada.getCodigo());
		
		List<Integer> listaCaracteres = (List<Integer>) contexto.getParametro(LISTA_CARACTERES);
		
		if (listaCaracteres == null) {
			ResourceBundle traductor = (ResourceBundle)contexto.getParametro(ContextoRefundido.TRADUCTOR);
			throw new ExcepcionRefundido(traductor.getString("refundido.operacion.entidad.acumulacion.error.sinListaCaracteres"), 7012);
		}
		
		List <Entidaddeterminacion> eds = em.createNamedQuery("refundido.Entidaddeterminacion.obtenerParaCopia")
        		.setParameter("idOperadora", operadora.getIden())
        		.setParameter("listaCaracteres", listaCaracteres )
        		.setParameter("idOperada", operada.getIden()).getResultList();
		
		if (isSuperposicion) {
			try {
				superposicion = (Integer) em.createNamedQuery("refundido.Entidaddeterminacionregimen.obtenerMaximaSuperposicion")
						.setParameter("idEntidad", operada.getIden())
						.getSingleResult();
				if (superposicion == null) {
					superposicion = 0;
				} else {
					superposicion++;
				}
			} catch(NoResultException nre) {
				// Si no se encuentra,se aplica el 0
			}
		}
		
		Entidaddeterminacion edRefundido;
		Casoentidaddeterminacion nuevoced;
		Casoentidaddeterminacion nuevoCasoAplicacion;
		Vinculocaso vinculoRefundido;
		Map<Integer, Casoentidaddeterminacion> correspondenciasCaso = new HashMap<Integer, Casoentidaddeterminacion>();
		for (Entidaddeterminacion ed : eds) {
			List<Entidaddeterminacion> edsOperada = em.createNamedQuery("refundido.Entidaddeterminacion.obtenerPorEntidadYDeterminacion")
	            	.setParameter("idDeterminacion", ed.getDeterminacion().getIden())
	            	.setParameter("idEntidad", operada.getIden())
	            	.getResultList();
            	
			if (edsOperada.size() > 0) {
        		edRefundido = edsOperada.get(0);
        	} else {
        		edRefundido = new Entidaddeterminacion();
        		edRefundido.setEntidad(operada);
        		
        		edRefundido.setDeterminacion(ed.getDeterminacion());
            	em.persist(edRefundido);
            	em.flush();
            }
        	
        	for (Casoentidaddeterminacion ced : ed.getCasoentidaddeterminacions()){
            	nuevoced = new Casoentidaddeterminacion();
            	
            	nuevoced.setEntidaddeterminacion(edRefundido);
            	nuevoced.setNombre((operadora.getClave() != null? operadora.getClave() + " " : "") + 
	            	(operadora.getNombre() != null ? operadora.getNombre()+ " " : "") + 
	            	(ced.getNombre() != null? ced.getNombre() : ""));
            	
            	if (nuevoced.getNombre().length() > 150) {
            		nuevoced.setNombre(nuevoced.getNombre().substring(0,150));
            	}
            	
            	em.persist(nuevoced);
            	em.flush();
            	
            	for (Documentocaso dcPlaneamiento : ced.getDocumentocasos()) {
            		copiar((int)contexto.getParametro(ContextoRefundido.ID_PROCESO), dcPlaneamiento, nuevoced, operada.getTramite());
            	}
            	correspondenciasCaso.put(ced.getIden(), nuevoced);
            }
		}
		// Una vez copiados los casos se copian los edr y los vínculos,
		// para asegurar que los casos vinculados o aplicados han sido también
		// copiados.
		// Pero esto sólo funcionará correctamente si los casos aplicados tienen
		// la misma entidad que los casos del edr.
		for (Entidaddeterminacion ed : eds) {	
        	for (Casoentidaddeterminacion ced : ed.getCasoentidaddeterminacions()){
            	nuevoced = correspondenciasCaso.containsKey(ced.getIden()) ? correspondenciasCaso.get(ced.getIden()):ced;
            	// Vinculos
				for(Vinculocaso vinculo : ced.getVinculocasosForIdcaso()) {
					vinculoRefundido = new Vinculocaso();
					vinculoRefundido.setCasoentidaddeterminacionByIdcaso(nuevoced);
					vinculoRefundido.setCasoentidaddeterminacionByIdcasovinculado(correspondenciasCaso.containsKey(vinculo.getCasoentidaddeterminacionByIdcasovinculado().getIden()) ? correspondenciasCaso.get(vinculo.getCasoentidaddeterminacionByIdcasovinculado().getIden()):vinculo.getCasoentidaddeterminacionByIdcasovinculado());
					em.persist(vinculoRefundido);
					em.flush();
				}
				
				// EntidadDeterminacionRegimen
				
				for (Entidaddeterminacionregimen edr : ced.getEntidaddeterminacionregimensForIdcaso()) {
					if (edr.getCasoentidaddeterminacionByIdcasoaplicacion() != null) {
						nuevoCasoAplicacion = correspondenciasCaso.containsKey(edr.getCasoentidaddeterminacionByIdcasoaplicacion().getIden()) ? 
								correspondenciasCaso.get(edr.getCasoentidaddeterminacionByIdcasoaplicacion().getIden()): edr.getCasoentidaddeterminacionByIdcasoaplicacion();
					} else {
						nuevoCasoAplicacion = null;
					}
					copiar((int)contexto.getParametro(ContextoRefundido.ID_PROCESO), 
							edr, 
							isSuperposicion ? superposicion : 
							edr.getSuperposicion(), 
							nuevoced,
							nuevoCasoAplicacion);
				}
            }
		}
		correspondenciasCaso.clear();
		em.flush();
	}
}

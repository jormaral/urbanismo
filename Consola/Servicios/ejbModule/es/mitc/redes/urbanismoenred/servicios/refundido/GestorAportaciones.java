package es.mitc.redes.urbanismoenred.servicios.refundido;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

import org.apache.log4j.Logger;

import es.mitc.redes.urbanismoenred.data.rpm.refundido.Casoentidaddeterminacion;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Determinacion;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Documento;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Documentocaso;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Documentoentidad;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Documentoshp;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Entidad;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Entidaddeterminacion;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Entidaddeterminacionregimen;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Opciondeterminacion;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Regimenespecifico;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Tramite;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Vinculocaso;
import es.mitc.redes.urbanismoenred.servicios.comunes.GestionIntroduccionFIPenSistemaLocal;
import es.mitc.redes.urbanismoenred.utils.excepciones.RedesException;


/**
 * Session Bean implementation class GestorAportaciones
 * 
 * @author Arnaiz Consultores
 */
@Stateless
public class GestorAportaciones implements GestorAportacionesLocal {
	
	private static final String DIRECTORIO_TRABAJO = "var";
	
	private static final Logger log = Logger.getLogger(GestorAportaciones.class);
	
	private static final String UPDATE_GEOM = "UPDATE refundido.%s SET geom=multi(geometryfromtext('%s')) where iden = %d";

	private static final String DETERMINACIONES_APORTADAS = "refundido.gestor.aportaciones.determinaciones.aportadas";
	private static final String TRAMITE_APORTADO = "refundido.gestor.aportaciones.tramite.aportado.";
	
	@PersistenceContext(unitName = "rpmv2")
	private EntityManager em;
	
	@EJB
	private CopiadorRefundidoLocal copiador;
	
	@EJB
	private GestionIntroduccionFIPenSistemaLocal gestorFips;

	@EJB
	private OperadorDeterminacionesRefundidoLocal operadorDeterminaciones;
	
	@EJB
	private OperadorEntidadesRefundidoLocal operadorEntidades;
	
    /**
     * Default constructor. 
     */
    public GestorAportaciones() {
        
    }

    /**
	 * 
	 * @param caso
	 * @param contexto
     * @param tramiteOrigen 
     * @throws ExcepcionRefundido 
	 */
	private Casoentidaddeterminacion aportar(
			es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Casoentidaddeterminacion caso,
			es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Tramite tramiteOrigen,
			ContextoRefundido contexto) throws ExcepcionRefundido {
		int idProceso = (Integer)contexto.getParametro(ContextoRefundido.ID_PROCESO);
		
		Entidaddeterminacion ed = aportar(caso.getEntidaddeterminacion(), tramiteOrigen, contexto);
		
		Casoentidaddeterminacion copia = em.find(Casoentidaddeterminacion.class, 
				copiador.getReferencia(idProceso, Casoentidaddeterminacion.class.getSimpleName(), caso.getIden()));
				
		if (copia == null && ed != null) {
			copia = new Casoentidaddeterminacion();
			copia.setEntidaddeterminacion(ed);
			copia.setNombre(copia.getNombre());
			copia.setOrden(caso.getOrden());
			
			em.persist(copia);
			em.flush();
			
			copiador.guardarReferencia(idProceso, Casoentidaddeterminacion.class.getSimpleName(), caso.getIden(), copia.getIden());
			
			for (es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Documentocaso dc : caso.getDocumentocasos()) {
				aportar(dc, contexto);
			}
			
			log.debug("aportando edr para el caso");
			for (es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidaddeterminacionregimen edr : caso.getEntidaddeterminacionregimensForIdcaso()) {
				aportar(edr, tramiteOrigen, contexto);
			}
			
			log.debug("aportando edr caso aplicacion");
			for (es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidaddeterminacionregimen edr : caso.getEntidaddeterminacionregimensForIdcasoaplicacion()) {
				aportar(edr, tramiteOrigen, contexto);
			}
			
			log.debug("aportando vínculo caso");
			for (es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Vinculocaso vc : caso.getVinculocasosForIdcaso()) {
				aportar(vc, tramiteOrigen, contexto);
			}
			
			log.debug("aportando vínculo por caso vinculado");
			for (es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Vinculocaso vc : caso.getVinculocasosForIdcasovinculado()) {
				aportar(vc, tramiteOrigen, contexto);
			}
		} else {
			if (copia == null) {
				log.debug("No se copia el caso de la entidad " +caso.getEntidaddeterminacion().getEntidad().getCodigo() + " " + caso.getEntidaddeterminacion().getEntidad().getTramite().getPlan().getCodigo() + " y determinacion " + caso.getEntidaddeterminacion().getDeterminacion().getCodigo() + " " + caso.getEntidaddeterminacion().getDeterminacion().getTramite().getPlan().getCodigo() + " porque la ed original no pertenece al tramite aportado");
			}
		}
		return copia;
	}
	
	/*
	 * (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.servicios.refundido.GestorAportacionesLocal#aportar(es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Determinacion, es.mitc.redes.urbanismoenred.data.rpm.refundido.Determinacion, java.lang.Integer, es.mitc.redes.urbanismoenred.servicios.refundido.ContextoRefundido)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Determinacion aportar(
			es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Determinacion aportada,
			Determinacion padre, Integer orden, ContextoRefundido contexto) throws ExcepcionRefundido {
		int idProceso = (Integer)contexto.getParametro(ContextoRefundido.ID_PROCESO);

		Determinacion copia = copiador.getReferencia(idProceso,
						Determinacion.class, 
						aportada.getIden());
		
		// Si no existe la determinación en base de datos y esa determinación no ha sido borrada la crea.
		if (copia != null) {
			copia.setDeterminacionByIdpadre(padre);
			
			if (orden != null) {
				copia.setOrden(orden);
				
				// reordenamos las determinaciones que estén al mismo nivels 
				// que tengan el mismo orden o mayor para que vayan detrás.
				Determinacion[] hermanas = new Determinacion[0];
				if (padre != null) {
					hermanas = padre.getDeterminacionsForIdpadre().toArray(new Determinacion[0]);
				} else {
					hermanas = ((List<Determinacion>)em.createNamedQuery("refundido.Determinacion.obtenerRaiz")
							.setParameter("idTramite", copia.getTramite().getIden())
							.getResultList())
							.toArray(new Determinacion[0]);
				}
				
				for (Determinacion hermana : hermanas) {
					if (hermana.getOrden() >= orden) {
						hermana.setOrden(hermana.getOrden()+1);
					}
				}
			}
			
			em.flush();
			
			List<Integer> aportadas = (List<Integer>) contexto.getParametro(DETERMINACIONES_APORTADAS);
			
			if (aportadas == null) {
				aportadas = new ArrayList<Integer>();
				contexto.putParametro(DETERMINACIONES_APORTADAS, aportadas);
			}
			
			if (!aportadas.contains(copia.getIden())) {
				aportadas.add(copia.getIden());
				
				// Aseguramos que las determinaciones relacionadas con esta o con 
				// sus hijas son marcadas como aportadas. Esto es especialmente
				// útil para las determinaciones valor de referencia.
				for(es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Determinacion hija : aportada.getDeterminacionsForIdpadre()) {
					aportar(hija,copia,null,contexto);
				}
				
				for (es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Opciondeterminacion od : aportada.getOpciondeterminacionsForIddeterminacion()) {
					aportar(od.getDeterminacionByIddeterminacionvalorref(), 
							od.getDeterminacionByIddeterminacionvalorref().getDeterminacionByIdpadre() != null? 
							copiador.getReferencia(idProceso,
									Determinacion.class, 
									od.getDeterminacionByIddeterminacionvalorref().getDeterminacionByIdpadre().getIden()):null, 
							null, contexto);
				}
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
	private Documento aportar(es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Documento original,
			ContextoRefundido contexto) {
		int idProceso = (Integer)contexto.getParametro(ContextoRefundido.ID_PROCESO);
		
		Documento copia = copiador.getReferencia(idProceso, Documento.class, original.getIden());
		
		if (copia == null) {
			copia = new Documento();
			
			Tramite tramiteRefundido = (Tramite) contexto.getParametro(ContextoRefundido.TRAMITE_REFUNDIDO);
			
			copia.setArchivo(original.getArchivo());
			copia.setComentario(original.getComentario());
			if (original.getDocumento() != null) {
				copia.setDocumento(aportar(original.getDocumento(), contexto));
			}
			copia.setEscala(original.getEscala());
			copia.setIdgrupodocumento(original.getIdgrupodocumento());
			copia.setIdtipodocumento(original.getIdtipodocumento());
			copia.setNombre(original.getNombre());
			copia.setTramite(tramiteRefundido);
			
			em.persist(copia);
			em.flush();
			
			copiador.guardarReferencia(idProceso, Documento.class.getSimpleName(), original.getIden(), copia.getIden());
			
			for (es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Documentoshp ds : original.getDocumentoshps() ) {
				aportar(ds, contexto);
			}
			
			copiarFicheros(copia, original);
		}
		return copia;
	}
	
	/**
	 * 
	 * @param original
	 * @param contexto
	 * @throws ExcepcionRefundido 
	 */
	private void aportar(es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Documentocaso original, ContextoRefundido contexto) throws ExcepcionRefundido {
		int idProceso = (Integer)contexto.getParametro(ContextoRefundido.ID_PROCESO);
		Documentocaso copia = copiador.getReferencia(idProceso, Documentocaso.class, original.getIden());
		if (copia == null) {
			copia = new Documentocaso();
			copia.setCasoentidaddeterminacion(aportar(original.getCasoentidaddeterminacion(), original.getDocumento().getTramite(), contexto));
			copia.setDocumento(aportar(original.getDocumento(), contexto));
			em.persist(copia);
			em.flush();
			
			copiador.guardarReferencia(idProceso, Documentocaso.class.getSimpleName(), original.getIden(), copia.getIden());
		}
	}

	/**
	 * 
	 * @param de
	 * @param contexto
	 * @throws ExcepcionRefundido 
	 */
	private void aportar(es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Documentoentidad original, ContextoRefundido contexto) throws ExcepcionRefundido {
		int idProceso = (Integer)contexto.getParametro(ContextoRefundido.ID_PROCESO);
		Documentoentidad copia = copiador.getReferencia(idProceso, Documentoentidad.class, original.getIden());
		
		if (copia == null) {
			copia = new Documentoentidad();
			
			copia.setDocumento(aportar(original.getDocumento(), contexto));
			copia.setEntidad(aportar(original.getEntidad(), null, null, contexto));
			
			em.persist(copia);
			em.flush();
			
			copiador.guardarReferencia(idProceso, Documentoentidad.class.getSimpleName(), original.getIden(), copia.getIden());
		}
	}

	/**
	 * 
	 * @param proceso
	 * @param original
	 * @param copiaTramite
	 */
	private void aportar(es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Documentoshp original, 
			ContextoRefundido contexto) {
		int idProceso = (Integer)contexto.getParametro(ContextoRefundido.ID_PROCESO);
		
		Documentoshp copia = copiador.getReferencia(idProceso, Documentoshp.class, original.getIden());
		
		if (copia == null) {
			copia = new Documentoshp();
			
			copia.setDocumento(aportar(original.getDocumento(), contexto));
			copia.setGeom(original.getGeom());
			copia.setHoja(original.getHoja());
			
			em.persist(copia);
			em.flush();
			
			copiador.guardarReferencia(idProceso, Documentoshp.class.getSimpleName(), original.getIden(), copia.getIden());
			
			em.createNativeQuery(String.format(UPDATE_GEOM, "documentoshp", original.getGeom(), copia.getIden())).executeUpdate();
			em.flush();
			em.refresh(copia);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.servicios.refundido.GestorAportacionesLocal#aportar(es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidad, es.mitc.redes.urbanismoenred.data.rpm.refundido.Entidad, java.lang.Integer, es.mitc.redes.urbanismoenred.servicios.refundido.ContextoRefundido)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Entidad aportar(
			es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidad aportada,
			Entidad padre, 
			Integer orden,
			ContextoRefundido contexto) throws ExcepcionRefundido {
		int idProceso = (Integer)contexto.getParametro(ContextoRefundido.ID_PROCESO);
		
		Entidad copia = copiador.getReferencia(idProceso, Entidad.class, aportada.getIden());
		
		// Si la entidad no existe en el refundido y no ha sido borrada previamente la crea.
		if (copia == null) {
			if (copiador.getReferencia(idProceso, Entidad.class.getSimpleName(), aportada.getIden()) == 0) {
				log.debug("Aportando entidad " + aportada.getCodigo() + " del plan " + aportada.getTramite().getPlan().getCodigo());
				copia = new Entidad();
				
				Tramite tramiteRefundido = (Tramite) contexto.getParametro(ContextoRefundido.TRAMITE_REFUNDIDO);
				
				copia.setBsuspendida(aportada.isBsuspendida());
				copia.setClave(aportada.getClave());
				copia.setCodigo(operadorEntidades.getSiguienteCodigo(contexto));
				copia.setEtiqueta(aportada.getEtiqueta());
				copia.setNombre(aportada.getNombre());
				copia.setOrden(orden != null ? orden : aportada.getOrden());
				copia.setTramite(tramiteRefundido);
				
				em.persist(copia);
				em.flush();
				
				copiador.guardarReferencia(idProceso, Entidad.class.getSimpleName(), aportada.getIden(), copia.getIden());
				
				if (aportada.getEntidadByIdentidadbase() != null) {
					log.debug("Obteniendo base entidad " + aportada.getCodigo() + " del plan " + aportada.getTramite().getPlan().getCodigo());
					copia.setEntidadByIdentidadbase(aportar(aportada.getEntidadByIdentidadbase(), padre, null, contexto));
				}
				
				if (aportada.getEntidadByIdentidadoriginal() != null) {
					log.debug("Obteniendo original de entidad " + aportada.getCodigo() + " del plan " + aportada.getTramite().getPlan().getCodigo());
					copia.setEntidadByIdentidadoriginal(aportar(aportada.getEntidadByIdentidadoriginal(), padre, null, contexto));
				}
				
				if (padre != null) {
					copia.setEntidadByIdpadre(padre);
				} else {
					if (aportada.getEntidadByIdpadre() != null) {
						log.debug("obteniendo padre de entidad " + aportada.getCodigo() + " del plan " + aportada.getTramite().getPlan().getCodigo());
						copia.setEntidadByIdpadre(aportar(aportada.getEntidadByIdpadre(),padre, null, contexto));
					} else {
						copia.setEntidadByIdpadre(operadorEntidades.obtenerCarpetaEntidadesAportadas(aportada.getTramite().getPlan(), contexto, true));
					}
				}
				
				// Se copian las referencias a esta entidad a través de adscripciones.
				List<es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Vectorrelacion> vectores = em.createQuery("SELECT vr FROM es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Vectorrelacion vr WHERE vr.iddefvector IN (4,5) AND vr.valor = :valor")
						.setParameter("valor", aportada.getIden()).getResultList();
				log.debug("obteniendo vectores de entidad " + aportada.getCodigo() + " del plan " + aportada.getTramite().getPlan().getCodigo());
				for (es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Vectorrelacion vr : vectores) {
					copiador.copiar(idProceso, vr, tramiteRefundido);
				}
				
				// Una vez copiados los datos básicos de una entidad copio aquellos
				// datos que dependen única y exclusivamente de la entidad.
				
				for (es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidadlin linea : aportada.getEntidadlins()) {
					copiador.copiar(idProceso, linea);
				}
				
				for (es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidadpnt punto : aportada.getEntidadpnts()) {
					copiador.copiar(idProceso, punto);
				}
				
				for (es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidadpol poligono : aportada.getEntidadpols()) {
					copiador.copiar(idProceso, poligono);
				}
				
				log.debug("obteniendo documentos entidad " + aportada.getCodigo() + " del plan " + aportada.getTramite().getPlan().getCodigo());
				for (es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Documentoentidad de : aportada.getDocumentoentidads()) {
					aportar(de, contexto);
				}
				
				log.debug("obteniendo entidaddeterminacion de entidad " + aportada.getCodigo() + " del plan " + aportada.getTramite().getPlan().getCodigo());
				for (es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidaddeterminacion ed : aportada.getEntidaddeterminacions()) {
					aportar(ed, aportada.getTramite(), contexto);
				}
				
				for(es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidad hija : aportada.getEntidadsForIdpadre()) {
					aportar(hija,copia, null,contexto);
				}
				em.flush();
			} 
		} else {
			if (padre != null) {
				copia.setEntidadByIdpadre(padre);
				if (orden != null) {
					copia.setOrden(orden);
				}
			}
		}
		return copia;
	}

	/**
	 * Sólo se aportan si tanto la entidad como la determinación son aportadas.
	 * 
	 * @param ed
	 * @param contexto
	 * @return Enitdaddeterminacion en refundido. Si la determinación o la 
	 * entidad no han sido aportadas y no pertenecen ambas al trámite origen no 
	 * se incluye y devuelve null.
	 * @throws ExcepcionRefundido 
	 */
	private Entidaddeterminacion aportar(es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidaddeterminacion ed, es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Tramite tramiteOrigen, ContextoRefundido contexto) throws ExcepcionRefundido {
		int idProceso = (Integer)contexto.getParametro(ContextoRefundido.ID_PROCESO);
		Entidaddeterminacion copia = copiador.getReferencia(idProceso, 
						Entidaddeterminacion.class, 
						ed.getIden());
		if (copia == null) {
			log.debug("Iniciando la creación de la ed " + ed.getIden());
			Entidad entidad = copiador.getReferencia(idProceso, Entidad.class, ed.getEntidad().getIden());
			Determinacion determinacion = copiador.getReferencia(idProceso, Determinacion.class, ed.getDeterminacion().getIden());
			
			if (entidad == null && ed.getEntidad().getTramite().getIden() == tramiteOrigen.getIden()) {
				entidad = aportar(ed.getEntidad(), null, null, contexto);
			} 
			
			if (determinacion == null && ed.getDeterminacion().getTramite().getIden() == tramiteOrigen.getIden()) {
				determinacion = aportar(ed.getDeterminacion(),null,null,contexto);
			}
			
			copia = copiador.getReferencia(idProceso, 
					Entidaddeterminacion.class, 
					ed.getIden());
			if (copia == null) {
				if (entidad != null && determinacion != null) {
					copia = new Entidaddeterminacion();
					copia.setDeterminacion(determinacion);
					copia.setEntidad(entidad);
					em.persist(copia);
					em.flush();
					copiador.guardarReferencia(idProceso, Entidaddeterminacion.class.getSimpleName(), ed.getIden(), copia.getIden());
					
					log.debug("Aportando casos para entidad " +ed.getEntidad().getCodigo() + " " + ed.getEntidad().getTramite().getPlan().getCodigo() + " y determinacion " + ed.getDeterminacion().getCodigo() + " " + ed.getDeterminacion().getTramite().getPlan().getCodigo());
					for (es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Casoentidaddeterminacion caso : ed.getCasoentidaddeterminacions()) {
						aportar(caso, tramiteOrigen,contexto);
					}
				} else {
					log.debug("No se crea la ed "+ ed.getIden() +" porque alguno de sus componentes no pertenece al trámite aportado");
				}
			}
		} else {
			log.debug("Ya existe la entidaddeterminacion para entidad " +ed.getEntidad().getCodigo() + " " + ed.getEntidad().getTramite().getPlan().getCodigo() + " y determinacion " + ed.getDeterminacion().getCodigo() + " " + ed.getDeterminacion().getTramite().getPlan().getCodigo());
		}
		
		return copia;
	}
	
	/**
	 * 
	 * @param edr
	 * @param contexto
	 * @param tramiteOrigen 
	 * @throws ExcepcionRefundido 
	 */
	private Entidaddeterminacionregimen aportar(es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidaddeterminacionregimen original, 
			es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Tramite tramiteOrigen,
			ContextoRefundido contexto) throws ExcepcionRefundido {
		int idProceso = (Integer)contexto.getParametro(ContextoRefundido.ID_PROCESO);
		
		Entidaddeterminacionregimen copia = copiador.getReferencia(idProceso, Entidaddeterminacionregimen.class, original.getIden());
		if (copia == null) {
			Casoentidaddeterminacion caso = aportar(original.getCasoentidaddeterminacionByIdcaso(), tramiteOrigen, contexto);
			copia = copiador.getReferencia(idProceso, Entidaddeterminacionregimen.class, original.getIden());
			if (copia == null) {
				if (caso != null) {
					log.debug("Aportando edr " + original.getIden());
					copia = new Entidaddeterminacionregimen();
					
					copia.setCasoentidaddeterminacionByIdcaso(caso);
			
					copia.setSuperposicion(original.getSuperposicion());
					copia.setValor(original.getValor());
					
					em.persist(copia);
					em.flush();
					
					copiador.guardarReferencia(idProceso, Entidaddeterminacionregimen.class.getSimpleName(), original.getIden(), copia.getIden());
					
					if (original.getCasoentidaddeterminacionByIdcasoaplicacion() != null) {
						log.debug("Aportando caso aplicacion");
						copia.setCasoentidaddeterminacionByIdcasoaplicacion(aportar(original.getCasoentidaddeterminacionByIdcasoaplicacion(), tramiteOrigen, contexto));
					}
					
					if (original.getDeterminacion() != null) {
						log.debug("Aportando determinacion de edr");
						copia.setDeterminacion(aportar(original.getDeterminacion(), null, null, contexto));
					}
					
					if (original.getOpciondeterminacion() != null) {
						log.debug("Aportando opciondeterminacion de edr");
						copia.setOpciondeterminacion(aportar(original.getOpciondeterminacion(), tramiteOrigen, contexto));
					} 
					
					for (es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Regimenespecifico re : original.getRegimenespecificos()) {
						aportar(re, tramiteOrigen, contexto);
					}
				} else {
					log.debug("No se crea la edr porque el caso con entidad" + 
							original.getCasoentidaddeterminacionByIdcaso().getEntidaddeterminacion().getEntidad().getCodigo() + 
							", " + 
							original.getCasoentidaddeterminacionByIdcaso().getEntidaddeterminacion().getEntidad().getTramite().getPlan().getCodigo()+
							" determinacion "+
							original.getCasoentidaddeterminacionByIdcaso().getEntidaddeterminacion().getDeterminacion().getCodigo() + 
							", " + 
							original.getCasoentidaddeterminacionByIdcaso().getEntidaddeterminacion().getDeterminacion().getTramite().getPlan().getCodigo()+
							" no se ha creado.");
				}
			}
		}
		
		return copia;
	}
	
	/**
	 * 
	 * @param opciondeterminacion
	 * @param contexto
	 * @param tramiteOrigen 
	 * @return
	 * @throws ExcepcionRefundido 
	 */
	private Opciondeterminacion aportar(
			es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Opciondeterminacion original, es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Tramite tramiteOrigen,
			ContextoRefundido contexto) throws ExcepcionRefundido {
		int idProceso = (Integer)contexto.getParametro(ContextoRefundido.ID_PROCESO);
		Opciondeterminacion copia = copiador.getReferencia(idProceso, Opciondeterminacion.class, original.getIden());
		if (copia == null) {
			Determinacion determinacion = copiador.getReferencia(idProceso, Determinacion.class, original.getDeterminacionByIddeterminacion().getIden());
			
			if (determinacion == null && original.getDeterminacionByIddeterminacion().getTramite().getIden() == tramiteOrigen.getIden()) {
				determinacion = aportar(original.getDeterminacionByIddeterminacion(), null, null, contexto);
			}
			
			Determinacion valorReferencia = copiador.getReferencia(idProceso, Determinacion.class, original.getDeterminacionByIddeterminacionvalorref().getIden());
			
			if (valorReferencia == null && original.getDeterminacionByIddeterminacionvalorref().getTramite().getIden() == tramiteOrigen.getIden()) {
				valorReferencia = aportar(original.getDeterminacionByIddeterminacionvalorref(), null, null, contexto);
			}
			
			copia = copiador.getReferencia(idProceso, Opciondeterminacion.class, original.getIden());
			if (copia == null) {
				if (determinacion != null && valorReferencia != null) {
					copia = new Opciondeterminacion();
					log.debug("Creando opcion para determinacion"+
					original.getDeterminacionByIddeterminacion().getCodigo() + 
					" " + original.getDeterminacionByIddeterminacion().getTramite().getPlan().getCodigo()+
					" valor de referencia " +
					original.getDeterminacionByIddeterminacionvalorref().getCodigo() + 
					" " + original.getDeterminacionByIddeterminacionvalorref().getTramite().getPlan().getCodigo());
					copia.setDeterminacionByIddeterminacion(determinacion);
					copia.setDeterminacionByIddeterminacionvalorref(valorReferencia);
					em.persist(copia);
					em.flush();
					
					copiador.guardarReferencia(idProceso, Opciondeterminacion.class.getSimpleName(), original.getIden(), copia.getIden());
					
					for (es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidaddeterminacionregimen edr : original.getEntidaddeterminacionregimens()) {
						aportar(edr, tramiteOrigen, contexto);
					}
				} else {
					log.debug("No se crea la opcion " + original.getIden() + " porque alguna de sus determinaciones no pertenecen al trámite aportado");
				}
			}
		}
		return copia;
	}
	
	/**
	 * 
	 * @param re
	 * @param contexto
	 * @param tramiteOrigen 
	 * @throws ExcepcionRefundido 
	 */
	private Regimenespecifico aportar(es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Regimenespecifico original, 
			es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Tramite tramiteOrigen, 
			ContextoRefundido contexto) throws ExcepcionRefundido {
		int idProceso = (Integer)contexto.getParametro(ContextoRefundido.ID_PROCESO);
		Regimenespecifico copia = copiador.getReferencia(idProceso, Regimenespecifico.class, original.getIden());
		
		if (copia == null) {
			copia = new Regimenespecifico();
			
			copia.setEntidaddeterminacionregimen(aportar(original.getEntidaddeterminacionregimen(), tramiteOrigen, contexto));
			copia.setNombre(original.getNombre());
			copia.setOrden(original.getOrden());
			if (original.getRegimenespecifico() != null) {
				copia.setRegimenespecifico(aportar(original.getRegimenespecifico(), tramiteOrigen, contexto));
			}
			copia.setTexto(original.getTexto());
			
			em.persist(copia);
			em.flush();
			
			copiador.guardarReferencia(idProceso, Regimenespecifico.class.getSimpleName(), original.getIden(), copia.getIden());
		}
		
		return copia;
	}
	
	/*
	 * (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.servicios.refundido.GestorAportacionesLocal#aportar(es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Tramite, es.mitc.redes.urbanismoenred.servicios.refundido.ContextoRefundido)
	 */
	@Override
	public void aportar(
			es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Tramite aportado,
			ContextoRefundido contexto) throws ExcepcionRefundido {
		
		Boolean estaAportado = (Boolean) contexto.getParametro(TRAMITE_APORTADO + aportado.getCodigofip());
		// Sólo se inicia el proceso de aportacion si no se ha realizado antes.
		// Y si el plan aportado no tiene padre, lo que sugiere una figura
		// principal que debería haber sido incorporada de otra forma.
		if ( (estaAportado == null || !estaAportado) 
				&& aportado.getPlan().getPlanByIdpadre() != null) {
			
			// Hay que asegurarse que los planes padre también están aportados.
			// Porque en ocasiones se utilizan las determinaciones de dichos padres
			List<Integer> tramitesRefundido = contexto.getTramites();
			for (es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Tramite tramitePadre : aportado.getPlan().getPlanByIdpadre().getTramites()) {
				if (tramitesRefundido.contains(tramitePadre.getIden())) {
					aportar(tramitePadre,contexto);
				}
			}
			
			int proceso = (int) contexto.getParametro(ContextoRefundido.ID_PROCESO);
			
			Tramite destino = (Tramite) contexto.getParametro(ContextoRefundido.TRAMITE_REFUNDIDO);
			
			log.debug("Copiando contenido del trámite " + aportado.getCodigofip());
			
			for (es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Documento documento: aportado.getDocumentos()) {
				copiador.copiar(proceso, documento, destino);
			}
			
			Entidad entidadesAportadas = operadorEntidades.obtenerCarpetaEntidadesAportadas(aportado.getPlan(), contexto, true);
			try {
				log.debug("Copiando entidades...");
				Object resultado = em.createNativeQuery("SELECT refundido.copiarEntidad(:idProceso, :idOrigen, :idDestino, :idPadre, true)")
						.setParameter("idProceso", proceso)
						.setParameter("idOrigen", aportado.getIden())
						.setParameter("idDestino", destino)
						.setParameter("idPadre", entidadesAportadas.getIden()).getSingleResult();
				log.debug(resultado + " copiadas.");
				
				Determinacion determinacionesAportadas =  operadorDeterminaciones.obtenerCarpetaDeterminacionesAportadas(aportado.getPlan(), 
						contexto, 
						true);
				
				log.debug("Copiando determinaciones...");
				resultado = em.createNativeQuery("SELECT refundido.copiarDeterminacion(:idProceso, :idOrigen, :idDestino, :idPadre, true)")
						.setParameter("idProceso", proceso)
						.setParameter("idOrigen", aportado.getIden())
						.setParameter("idDestino", destino)
						.setParameter("idPadre", determinacionesAportadas.getIden()).getSingleResult();
				log.debug(resultado + " copiadas.");
				log.debug("Copiando poligonos...");
				resultado = em.createNativeQuery("SELECT refundido.copiarPoligono(:idProceso, :idOrigen, :idDestino)")
						.setParameter("idProceso", proceso)
						.setParameter("idOrigen", aportado.getIden())
						.setParameter("idDestino", destino).getSingleResult();
				log.debug(resultado + " copiados.");
				log.debug("Copiando líneas...");
				resultado = em.createNativeQuery("SELECT refundido.copiarLinea(:idProceso, :idOrigen, :idDestino)")
						.setParameter("idProceso", proceso)
						.setParameter("idOrigen", aportado.getIden())
						.setParameter("idDestino", destino).getSingleResult();
				log.debug(resultado + " copiadas.");
				log.debug("Copiando puntos...");
				resultado = em.createNativeQuery("SELECT refundido.copiarPunto(:idProceso, :idOrigen, :idDestino)")
						.setParameter("idProceso", proceso)
						.setParameter("idOrigen", aportado.getIden())
						.setParameter("idDestino", destino).getSingleResult();
				log.debug(resultado + " copiados.");
				log.debug("Copiando determinaciongrupo...");
				resultado = em.createNativeQuery("SELECT refundido.copiardeterminaciongrupo(:idProceso, :idOrigen, :idDestino)")
						.setParameter("idProceso", proceso)
						.setParameter("idOrigen", aportado.getIden())
						.setParameter("idDestino", destino).getSingleResult();
				log.debug(resultado + " copiados.");
				log.debug("Copiando opciondeterminacion...");
				resultado = em.createNativeQuery("SELECT refundido.copiaropciondeterminacion(:idProceso, :idOrigen, :idDestino)")
						.setParameter("idProceso", proceso)
						.setParameter("idOrigen", aportado.getIden())
						.setParameter("idDestino", destino).getSingleResult();
				log.debug(resultado + " copiados.");
				log.debug("Copiando entidaddeterminacion...");
				resultado = em.createNativeQuery("SELECT refundido.copiarentidaddeterminacion(:idProceso, :idOrigen, :idDestino)")
						.setParameter("idProceso", proceso)
						.setParameter("idOrigen", aportado.getIden())
						.setParameter("idDestino", destino).getSingleResult();
				log.debug(resultado + " copiados.");
				log.debug("Copiando casos...");
				resultado = em.createNativeQuery("SELECT refundido.copiarcasoentidaddeterminacion(:idProceso, :idOrigen, :idDestino)")
						.setParameter("idProceso", proceso)
						.setParameter("idOrigen", aportado.getIden())
						.setParameter("idDestino", destino).getSingleResult();
				log.debug(resultado + " copiados.");
				log.debug("Copiando documentocaso...");
				resultado = em.createNativeQuery("SELECT refundido.copiarDocumentocaso(:idProceso, :idOrigen, :idDestino)")
						.setParameter("idProceso", proceso)
						.setParameter("idOrigen", aportado.getIden())
						.setParameter("idDestino", destino).getSingleResult();
				log.debug(resultado + " copiados.");
				log.debug("Copiando documentodeterminacion...");
				resultado = em.createNativeQuery("SELECT refundido.copiardocumentodeterminacion(:idProceso, :idOrigen, :idDestino)")
						.setParameter("idProceso", proceso)
						.setParameter("idOrigen", aportado.getIden())
						.setParameter("idDestino", destino).getSingleResult();
				log.debug(resultado + " copiados.");
				log.debug("Copiando documentoentidad...");
				resultado = em.createNativeQuery("SELECT refundido.copiardocumentoentidad(:idProceso, :idOrigen, :idDestino)")
						.setParameter("idProceso", proceso)
						.setParameter("idOrigen", aportado.getIden())
						.setParameter("idDestino", destino).getSingleResult();
				log.debug(resultado + " copiados.");
				log.debug("Copiando entidaddeterminacionregimen...");
				resultado = em.createNativeQuery("SELECT refundido.copiarentidaddeterminacionregimen(:idProceso, :idOrigen, :idDestino)")
						.setParameter("idProceso", proceso)
						.setParameter("idOrigen", aportado.getIden())
						.setParameter("idDestino", destino).getSingleResult();
				log.debug(resultado + " copiados.");
				log.debug("Copiando vinculos...");
				resultado = em.createNativeQuery("SELECT refundido.copiarVinculocaso(:idProceso, :idOrigen, :idDestino)")
						.setParameter("idProceso", proceso)
						.setParameter("idOrigen", aportado.getIden())
						.setParameter("idDestino", destino).getSingleResult();
				log.debug(resultado + " copiados.");
				log.debug("Copiando regimenes...");
				resultado = em.createNativeQuery("SELECT refundido.copiarregimenespecifico(:idProceso, :idOrigen, :idDestino)")
						.setParameter("idProceso", proceso)
						.setParameter("idOrigen", aportado.getIden())
						.setParameter("idDestino", destino).getSingleResult();
				log.debug(resultado + " copiados.");
				log.debug("Copiando relaciones...");
				resultado = em.createNativeQuery("SELECT refundido.copiarRelacion(:idProceso, :idOrigen, :idDestino)")
						.setParameter("idProceso", proceso)
						.setParameter("idOrigen", aportado.getIden())
						.setParameter("idDestino", destino).getSingleResult();
				log.debug(resultado + " copiados.");
				log.debug("Copiando propiedad...");
				resultado = em.createNativeQuery("SELECT refundido.copiarPropiedadrelacion(:idProceso, :idOrigen, :idDestino)")
						.setParameter("idProceso", proceso)
						.setParameter("idOrigen", aportado.getIden())
						.setParameter("idDestino", destino).getSingleResult();
				log.debug(resultado + " copiados.");
				log.debug("Copiando vectores...");
				resultado = em.createNativeQuery("SELECT refundido.copiarVectorrelacion(:idProceso, :idOrigen, :idDestino)")
						.setParameter("idProceso", proceso)
						.setParameter("idOrigen", aportado.getIden())
						.setParameter("idDestino", destino).getSingleResult();
				log.debug(resultado + " copiados."); 
			
				em.flush();
				em.refresh(entidadesAportadas);
				em.refresh(determinacionesAportadas);
				
				contexto.putParametro(TRAMITE_APORTADO + aportado.getCodigofip(), true);
			} catch (PersistenceException e) {
				ResourceBundle traductor = (ResourceBundle) contexto.getParametro(ContextoRefundido.TRADUCTOR);
				throw new ExcepcionRefundido(String.format(traductor.getString("refundido.aportar.error.copia.tramite"), e.getMessage()), 4010);
			}
		} else {
			log.debug("El trámite " + aportado.getCodigofip() + " ya ha sido aportado, no se hace nada.");
		}
	}
	
	/**
	 * 
	 * @param vc
	 * @param contexto
	 * @param tramiteOrigen 
	 * @throws ExcepcionRefundido 
	 */
	private void aportar(es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Vinculocaso original, 
			es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Tramite tramiteOrigen, 
			ContextoRefundido contexto) throws ExcepcionRefundido {
		int idProceso = (Integer)contexto.getParametro(ContextoRefundido.ID_PROCESO);
		Vinculocaso copia = copiador.getReferencia(idProceso, Vinculocaso.class, original.getIden());
		if (copia == null) {
			
			Casoentidaddeterminacion caso = aportar(original.getCasoentidaddeterminacionByIdcaso(), tramiteOrigen, contexto);
			Casoentidaddeterminacion vinculado = aportar(original.getCasoentidaddeterminacionByIdcasovinculado(), tramiteOrigen, contexto);
			
			if (caso != null && vinculado != null) {
				copia = new Vinculocaso();
				copia.setCasoentidaddeterminacionByIdcaso(caso);
				copia.setCasoentidaddeterminacionByIdcasovinculado(vinculado);
				
				em.persist(copia);
				em.flush();
				
				copiador.guardarReferencia(idProceso, Vinculocaso.class.getSimpleName(), original.getIden(), copia.getIden());
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
     * @see es.mitc.redes.urbanismoenred.servicios.refundido.GestorAportacionesLocal#determinacionEsAportada(int, es.mitc.redes.urbanismoenred.servicios.refundido.ContextoRefundido)
     */
	@SuppressWarnings("unchecked")
	@Override
	public boolean determinacionEsAportada(int idDeterminacion,
			ContextoRefundido contexto) {
		List<Integer> aportadas = (List<Integer>) contexto.getParametro(DETERMINACIONES_APORTADAS);
		
		if (aportadas != null) {
			return aportadas.contains(idDeterminacion);
		}
		return false;
	}
}

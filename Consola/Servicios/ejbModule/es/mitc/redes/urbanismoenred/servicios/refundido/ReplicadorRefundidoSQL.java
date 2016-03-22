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

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;

import es.mitc.redes.urbanismoenred.data.rpm.refundido.Plan;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Tramite;

/**
 * @author Arnaiz Consultores.
 *
 */
@Stateless( name = "refundido.replicador.sql")
public class ReplicadorRefundidoSQL implements ReplicadorRefundidoLocal {

	private static final Logger log = Logger.getLogger(ReplicadorRefundidoSQL.class);
	
	@EJB
	private CopiadorRefundidoLocal copiador;
	
	@PersistenceContext(unitName = "rpmv2")
	private EntityManager em;
	
	/**
	 * 
	 */
	public ReplicadorRefundidoSQL() {
	}

	/*
	 * (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.servicios.refundido.ReplicadorRefundidoLocal#copiar(int, es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Plan, boolean, boolean)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Plan copiar(int proceso,
			es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Plan original,
			boolean incluirTramite,
			boolean recalcular) {
		Plan copia = getReferencia(proceso, Plan.class, original.getIden());
		if (copia == null) {
			log.debug("Copiando  plan " + original.getCodigo());
			copia = new Plan();
			
			copia.setBsuspendido(original.getBsuspendido());
			copia.setCodigo(original.getCodigo());
			copia.setIdambito(original.getIdambito());
			copia.setNombre(original.getNombre());
			copia.setOrden(original.getOrden());
			copia.setTexto(original.getTexto());
			
			if (original.getPlanByIdplanbase() != null) {
				Plan copiaPlanBase =  getReferencia(proceso, Plan.class, original.getPlanByIdplanbase().getIden());
				if (copiaPlanBase == null) {
					copiaPlanBase = copiar(proceso, original.getPlanByIdplanbase(), true, false);
				}
				copia.setPlanByIdplanbase(copiaPlanBase);
			}
			
			if (original.getPlanByIdpadre() != null) {
				copia.setPlanByIdpadre(copiar(proceso, original.getPlanByIdpadre(),true, recalcular));
			}
			
			em.persist(copia);
			em.flush();
			
			copiador.guardarReferencia(proceso, Plan.class.getSimpleName(), original.getIden(), copia.getIden());
			
			if (incluirTramite) {
				List<es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Tramite> vigentes = em.createNamedQuery("Tramite.findVigente")
						.setParameter("idPlan", original.getIden()).getResultList();
				
				if (vigentes.size() > 0) {
					copiar(proceso, vigentes.get(0),null, recalcular);
				} else {
					// Los planes base en ocasiones no tienen el trámite consolidado
					if (original.getPlanByIdplanbase() == null) {
						vigentes = em.createNamedQuery("Tramite.obtenerPendientesPlan")
								.setParameter("idPlan", original.getIden()).getResultList();
						if (vigentes.size() > 0) {
							copiar(proceso, vigentes.get(0), null, recalcular);
						}
					}
				}
			}
		}
		
		return copia;
	}

	/*
	 * (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.servicios.refundido.ReplicadorRefundidoLocal#copiar(int, es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Tramite, es.mitc.redes.urbanismoenred.data.rpm.refundido.Tramite, boolean)
	 */
	@Override
	public Tramite copiar(
			int proceso,
			es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Tramite original,
			Tramite destino, boolean recalcular) {
		if (destino == null) {
			log.debug("Copiando tramite del plan " + original.getPlan().getCodigo());
			
			destino = new Tramite();
			
			destino.setCodigofip(original.getCodigofip());
			destino.setComentario(original.getComentario());
			destino.setFecha(original.getFecha());
			destino.setFechaconsolidacion(original.getFechaconsolidacion());
			destino.setIdcentroproduccion(original.getIdcentroproduccion());
			destino.setIdorgano(original.getIdorgano());
			destino.setIdsentido(original.getIdsentido());
			destino.setIdtipotramite(original.getIdtipotramite());
			destino.setIteracion(original.getIteracion());
			destino.setNombre(original.getNombre());
			destino.setNumeroacuerdo(original.getNumeroacuerdo());
			destino.setPlan(copiar(proceso, original.getPlan(), false, recalcular));
			destino.setTexto(original.getTexto());
			
			em.persist(destino);
			em.flush();
			
			copiador.guardarReferencia(proceso, Tramite.class.getSimpleName(), original.getIden(), destino.getIden());
		}
		
		log.debug("Copiando contenido del trámite " + original.getCodigofip());
		
		for (es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Documento documento: original.getDocumentos()) {
			copiador.copiar(proceso, documento, destino);
		}
		
		em.flush();
		
		log.debug("Copiando entidades...");
		Object resultado = em.createNativeQuery("SELECT refundido.copiarEntidad(:idProceso, :idOrigen, :idDestino, :idPadre, :recalcular)")
				.setParameter("idProceso", proceso)
				.setParameter("idOrigen", original.getIden())
				.setParameter("idDestino", destino.getIden())
				.setParameter("idPadre", 0)
				.setParameter("recalcular", recalcular).getSingleResult();
		log.debug(resultado + " copiadas.");
		log.debug("Copiando determinaciones...");
		resultado = em.createNativeQuery("SELECT refundido.copiarDeterminacion(:idProceso, :idOrigen, :idDestino, :idPadre, :recalcular)")
				.setParameter("idProceso", proceso)
				.setParameter("idOrigen", original.getIden())
				.setParameter("idDestino", destino.getIden())
				.setParameter("idPadre", 0)
				.setParameter("recalcular", recalcular).getSingleResult();
		log.debug(resultado + " copiadas.");
		log.debug("Copiando poligonos...");
		resultado = em.createNativeQuery("SELECT refundido.copiarPoligono(:idProceso, :idOrigen, :idDestino)")
				.setParameter("idProceso", proceso)
				.setParameter("idOrigen", original.getIden())
				.setParameter("idDestino", destino.getIden()).getSingleResult();
		log.debug(resultado + " copiados.");
		log.debug("Copiando líneas...");
		resultado = em.createNativeQuery("SELECT refundido.copiarLinea(:idProceso, :idOrigen, :idDestino)")
				.setParameter("idProceso", proceso)
				.setParameter("idOrigen", original.getIden())
				.setParameter("idDestino", destino.getIden()).getSingleResult();
		log.debug(resultado + " copiadas.");
		log.debug("Copiando puntos...");
		resultado = em.createNativeQuery("SELECT refundido.copiarPunto(:idProceso, :idOrigen, :idDestino)")
				.setParameter("idProceso", proceso)
				.setParameter("idOrigen", original.getIden())
				.setParameter("idDestino", destino.getIden()).getSingleResult();
		log.debug(resultado + " copiados.");
		log.debug("Copiando determinaciongrupo...");
		resultado = em.createNativeQuery("SELECT refundido.copiardeterminaciongrupo(:idProceso, :idOrigen, :idDestino)")
				.setParameter("idProceso", proceso)
				.setParameter("idOrigen", original.getIden())
				.setParameter("idDestino", destino.getIden()).getSingleResult();
		log.debug(resultado + " copiados.");
		log.debug("Copiando opciondeterminacion...");
		resultado = em.createNativeQuery("SELECT refundido.copiaropciondeterminacion(:idProceso, :idOrigen, :idDestino)")
				.setParameter("idProceso", proceso)
				.setParameter("idOrigen", original.getIden())
				.setParameter("idDestino", destino.getIden()).getSingleResult();
		log.debug(resultado + " copiados.");
		log.debug("Copiando entidaddeterminacion...");
		resultado = em.createNativeQuery("SELECT refundido.copiarentidaddeterminacion(:idProceso, :idOrigen, :idDestino)")
				.setParameter("idProceso", proceso)
				.setParameter("idOrigen", original.getIden())
				.setParameter("idDestino", destino.getIden()).getSingleResult();
		log.debug(resultado + " copiados.");
		log.debug("Copiando casos...");
		resultado = em.createNativeQuery("SELECT refundido.copiarcasoentidaddeterminacion(:idProceso, :idOrigen, :idDestino)")
				.setParameter("idProceso", proceso)
				.setParameter("idOrigen", original.getIden())
				.setParameter("idDestino", destino.getIden()).getSingleResult();
		log.debug(resultado + " copiados.");
		log.debug("Copiando documentocaso...");
		resultado = em.createNativeQuery("SELECT refundido.copiarDocumentocaso(:idProceso, :idOrigen, :idDestino)")
				.setParameter("idProceso", proceso)
				.setParameter("idOrigen", original.getIden())
				.setParameter("idDestino", destino.getIden()).getSingleResult();
		log.debug(resultado + " copiados.");
		log.debug("Copiando documentodeterminacion...");
		resultado = em.createNativeQuery("SELECT refundido.copiardocumentodeterminacion(:idProceso, :idOrigen, :idDestino)")
				.setParameter("idProceso", proceso)
				.setParameter("idOrigen", original.getIden())
				.setParameter("idDestino", destino.getIden()).getSingleResult();
		log.debug(resultado + " copiados.");
		log.debug("Copiando documentoentidad...");
		resultado = em.createNativeQuery("SELECT refundido.copiardocumentoentidad(:idProceso, :idOrigen, :idDestino)")
				.setParameter("idProceso", proceso)
				.setParameter("idOrigen", original.getIden())
				.setParameter("idDestino", destino.getIden()).getSingleResult();
		log.debug(resultado + " copiados.");
		log.debug("Copiando entidaddeterminacionregimen...");
		resultado = em.createNativeQuery("SELECT refundido.copiarentidaddeterminacionregimen(:idProceso, :idOrigen, :idDestino)")
				.setParameter("idProceso", proceso)
				.setParameter("idOrigen", original.getIden())
				.setParameter("idDestino", destino.getIden()).getSingleResult();
		log.debug(resultado + " copiados.");
		log.debug("Copiando vinculos...");
		resultado = em.createNativeQuery("SELECT refundido.copiarVinculocaso(:idProceso, :idOrigen, :idDestino)")
				.setParameter("idProceso", proceso)
				.setParameter("idOrigen", original.getIden())
				.setParameter("idDestino", destino.getIden()).getSingleResult();
		log.debug(resultado + " copiados.");
		log.debug("Copiando regimenes...");
		resultado = em.createNativeQuery("SELECT refundido.copiarregimenespecifico(:idProceso, :idOrigen, :idDestino)")
				.setParameter("idProceso", proceso)
				.setParameter("idOrigen", original.getIden())
				.setParameter("idDestino", destino.getIden()).getSingleResult();
		log.debug(resultado + " copiados.");
		log.debug("Copiando relaciones...");
		resultado = em.createNativeQuery("SELECT refundido.copiarRelacion(:idProceso, :idOrigen, :idDestino)")
				.setParameter("idProceso", proceso)
				.setParameter("idOrigen", original.getIden())
				.setParameter("idDestino", destino.getIden()).getSingleResult();
		log.debug(resultado + " copiados.");
		log.debug("Copiando propiedad...");
		resultado = em.createNativeQuery("SELECT refundido.copiarPropiedadrelacion(:idProceso, :idOrigen, :idDestino)")
				.setParameter("idProceso", proceso)
				.setParameter("idOrigen", original.getIden())
				.setParameter("idDestino", destino.getIden()).getSingleResult();
		log.debug(resultado + " copiados.");
		log.debug("Copiando vectores...");
		resultado = em.createNativeQuery("SELECT refundido.copiarVectorrelacion(:idProceso, :idOrigen, :idDestino)")
				.setParameter("idProceso", proceso)
				.setParameter("idOrigen", original.getIden())
				.setParameter("idDestino", destino.getIden()).getSingleResult();
		log.debug(resultado + " copiados.");
		
		return destino;
	}

	/* (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.servicios.refundido.ReplicadorRefundidoLocal#getReferencia(int, java.lang.Class, int)
	 */
	@Override
	public <T> T getReferencia(int proceso, Class<T> clase, int idplaneamiento) {
		return copiador.getReferencia(proceso, clase, idplaneamiento);
	}

	/* (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.servicios.refundido.ReplicadorRefundidoLocal#getReferencia(int, java.lang.String, int)
	 */
	@Override
	public int getReferencia(int proceso, String tabla, int idplaneamiento) {
		return copiador.getReferencia(proceso, tabla, idplaneamiento);
	}

}

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

import es.mitc.redes.urbanismoenred.data.rpm.refundido.Casoentidaddeterminacion;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Determinacion;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Determinaciongrupoentidad;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Documento;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Documentocaso;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Documentodeterminacion;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Documentoentidad;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Entidad;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Entidaddeterminacion;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Entidaddeterminacionregimen;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Opciondeterminacion;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Plan;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Regimenespecifico;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Tramite;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Vinculocaso;

/**
 * Genera una copia de objetos del esquema de planeamiento en el esquema de
 * refundido para su uso durante el refundido.
 * 
 * 
 * @author Arnaiz consultores
 *
 */
@Stateless(name = "refundido.replicador.basico")
public class ReplicadorRefundidoBean implements ReplicadorRefundidoLocal {
	
	private static final Logger log = Logger.getLogger(ReplicadorRefundidoBean.class);

	@EJB
	private CopiadorRefundidoLocal copiador;
	
	@PersistenceContext(unitName = "rpmv2")
	private EntityManager em;
	
    /**
     * Default constructor. 
     */
    public ReplicadorRefundidoBean() {
    }
    
    /**
	 * 
	 * @param original
	 * @param copiaTramite
     * @param idTramiteOrigen 
	 * @return 
	 */
	private Casoentidaddeterminacion copiar(int proceso, es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Casoentidaddeterminacion original, int idTramiteOrigen, Tramite copiaTramite) {
		Entidaddeterminacion ed = copiar(proceso, original.getEntidaddeterminacion(), idTramiteOrigen, copiaTramite);
		
		Casoentidaddeterminacion copia = getReferencia(proceso, 
						Casoentidaddeterminacion.class, 
						original.getIden());
		if (copia == null && ed != null) {
			log.debug("Copiando caso para la determinación " + original.getEntidaddeterminacion().getDeterminacion().getCodigo() + " y la entidad " + original.getEntidaddeterminacion().getEntidad().getCodigo());
			
			copia = new Casoentidaddeterminacion();
			
			copia.setEntidaddeterminacion(ed);
			copia.setNombre(original.getNombre());
			copia.setOrden(original.getOrden());
			
			em.persist(copia);
			em.flush();
			
			copiador.guardarReferencia(proceso, Casoentidaddeterminacion.class.getSimpleName(), original.getIden(), copia.getIden());
			
			for (es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Documentocaso dc : original.getDocumentocasos()) {
				copiar(proceso, dc, copiaTramite);
			}
			
			for (es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidaddeterminacionregimen edr : original.getEntidaddeterminacionregimensForIdcaso()) {
				copiar(proceso, edr, idTramiteOrigen, copiaTramite);
			}
			
			for (es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidaddeterminacionregimen edr : original.getEntidaddeterminacionregimensForIdcasoaplicacion()) {
				copiar(proceso, edr, idTramiteOrigen, copiaTramite);
			}
			
			for (es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Vinculocaso vc : original.getVinculocasosForIdcaso()) {
				copiar(proceso, vc, idTramiteOrigen, copiaTramite);
			}
			
			for (es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Vinculocaso vc : original.getVinculocasosForIdcasovinculado()) {
				copiar(proceso, vc, idTramiteOrigen, copiaTramite);
			}
		}
		return copia;
	}
    
    /**
	 * Copia las determinaciones desde el esquema de planeamiento al esquema
	 * de refundido.
	 * 
	 * @param proceso Identificador del proceso sobre el que se copia la 
	 * determinación.
	 * @param original Determinación que se va a copiar del esquema de 
	 * planeamiento.
     * @param tramiteOrigen 
	 * @param copiaTramite
	 * @return Determinación copiada en el esquema de refundido.
	 */
	@SuppressWarnings("unchecked")
	private Determinacion copiar(int proceso, es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Determinacion original, int idTramiteOrigen, Tramite copiaTramite) {
		
		// Comprobamos si ya se ha copiado la determinación en algún momento anterior
		Determinacion copia = getReferencia(proceso, Determinacion.class, original.getIden());
		if (copia == null && 
				original.getTramite().getIden() == idTramiteOrigen) {
			log.debug("Copiando determinacion " + original.getCodigo() + " del plan " + original.getTramite().getPlan().getCodigo());
			
			copia = new Determinacion();
			
			copia.setApartado(original.getApartado());
			copia.setBsuspendida(original.isBsuspendida());
			// Las determinaciones de los planes base no deben cambiar de código
			if (original.getTramite().getPlan().getPlanByIdplanbase() != null) {
				copia.setCodigo(getCodigoDeterminacion(copiaTramite.getIden()));
			} else {
				copia.setCodigo(original.getCodigo());
			}
			
			copia.setEtiqueta(original.getEtiqueta());
			copia.setIdcaracter(original.getIdcaracter());
			copia.setNombre(original.getNombre());
			copia.setOrden(original.getOrden());
			copia.setTexto(original.getTexto());
			copia.setTramite(copiaTramite);
			em.persist(copia);
			em.flush();
			
			copiador.guardarReferencia(proceso, Determinacion.class.getSimpleName(), original.getIden(), copia.getIden());
			
			if (original.getDeterminacionByIddeterminacionbase() != null) {
				copia.setDeterminacionByIddeterminacionbase(copiar(proceso, original.getDeterminacionByIddeterminacionbase(), idTramiteOrigen, copiaTramite));
			}
			if (original.getDeterminacionByIddeterminacionoriginal() != null) {
				copia.setDeterminacionByIddeterminacionbase(copiar(proceso, original.getDeterminacionByIddeterminacionoriginal(), idTramiteOrigen, copiaTramite));
			}
			if (original.getDeterminacionByIdpadre() != null) {
				copia.setDeterminacionByIdpadre(copiar(proceso, original.getDeterminacionByIdpadre(), idTramiteOrigen, copiaTramite));
			}
			
			// Se comprueba si esta determinación es unidad o tiene unidad asociada, 
			// si tiene regulaciones, o si forman parte de adscripciones y se copian los datos correspondientes
			List<es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Vectorrelacion> vectores = em.createNamedQuery("Vectorrelacion.obtenerDeDeterminacion")
					.setParameter("valor", original.getIden()).getResultList();
			for (es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Vectorrelacion vr : vectores) {
				copiador.copiar(proceso, vr, copiaTramite);
			}
			
			// Copiamos las referencias inversas que no presentan un problema 
			// a la hora de desbordar la pila
			for(es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Determinaciongrupoentidad dge : original.getDeterminaciongrupoentidadsForIddeterminacion()) {
				copiar(proceso, dge, idTramiteOrigen, copiaTramite);
			}
			for(es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Determinaciongrupoentidad dge : original.getDeterminaciongrupoentidadsForIddeterminaciongrupo()) {
				copiar(proceso, dge, idTramiteOrigen, copiaTramite);
			}
			for(es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Documentodeterminacion dd : original.getDocumentodeterminacions()) {
				copiar(proceso, dd, copiaTramite);
			}
			for (es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidaddeterminacion ed : original.getEntidaddeterminacions()) {
				copiar(proceso, ed, idTramiteOrigen, copiaTramite);
			}
			for(es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidaddeterminacionregimen edr : original.getEntidaddeterminacionregimens()) {
				copiar(proceso, edr, idTramiteOrigen, copiaTramite);
			}
		} 
		
		return copia;
	}
    
    /**
     * Sólo se copia si las determinaciones implicadas pertenecen al mismo 
     * trámite que el originador del proceso de copia. O si pertenece a un
     * trámite distinto pero ya existe ese elemento en el esquema de refundido.
     * 
     * Este orden es menos eficiente, pero asegura que no hay ciclos y que no 
     * se copian referencias de otros planes.
     * 
	 * @param proceso
	 * @param original
	 * @param copiaTramite
     * @param tramiteOrigen 
	 */
	private void copiar(int proceso, es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Determinaciongrupoentidad original, int idTramiteOrigen, Tramite copiaTramite) {
		Determinacion determinacion = copiar(proceso, original.getDeterminacionByIddeterminacion(), idTramiteOrigen, copiaTramite);
		
		if (determinacion != null) {
			Determinacion determinacionGrupo = null;
			
			
			if (original.getDeterminacionByIddeterminaciongrupo() != null) {
				determinacionGrupo = copiar(proceso, original.getDeterminacionByIddeterminaciongrupo(), idTramiteOrigen, copiaTramite);
				if (determinacionGrupo == null) {
					return;
				}
			}
			
			Determinaciongrupoentidad copia = getReferencia(proceso, Determinaciongrupoentidad.class, original.getIden());
			if (copia == null) {
				log.debug("Copiando determinaciongrupoentidad para determinación " + original.getDeterminacionByIddeterminacion().getCodigo() + " y grupo " + original.getDeterminacionByIddeterminaciongrupo()!= null ? original.getDeterminacionByIddeterminaciongrupo().getCodigo():"");
				
				copia = new Determinaciongrupoentidad();
				copia.setDeterminacionByIddeterminacion(determinacion);
				copia.setDeterminacionByIddeterminaciongrupo(determinacionGrupo);
				
				em.persist(copia);
				em.flush();
				
				copiador.guardarReferencia(proceso, Determinaciongrupoentidad.class.getSimpleName(), original.getIden(), copia.getIden());
			}
		}
		
	}

	/**
	 * 
	 * @param proceso
	 * @param original
	 * @param copiaTramite
	 */
	private void copiar(int proceso, es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Documentocaso original, Tramite copiaTramite) {
		Documentocaso copia = getReferencia(proceso, Documentocaso.class, original.getIden());
		if (copia == null) {
			copia = new Documentocaso();
			copia.setCasoentidaddeterminacion(getReferencia(proceso, Casoentidaddeterminacion.class,original.getCasoentidaddeterminacion().getIden()));
			copia.setDocumento(copiador.copiar(proceso, original.getDocumento(),copiaTramite));
			em.persist(copia);
			em.flush();
			
			copiador.guardarReferencia(proceso, Documentocaso.class.getSimpleName(), original.getIden(), copia.getIden());
		}
		
	}

	/**
	 * 
	 * @param proceso
	 * @param original
	 * @param copiaTramite
	 */
	private void copiar(int proceso, 
			es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Documentodeterminacion original,
			Tramite copiaTramite) {
		Documentodeterminacion copia = getReferencia(proceso, Documentodeterminacion.class, original.getIden());
		
		if (copia == null) {
			copia = new Documentodeterminacion();

			copia.setDeterminacion(getReferencia(proceso, Determinacion.class, original.getDeterminacion().getIden()));
			
			Documento documento = getReferencia(proceso, Documento.class, original.getDocumento().getIden());
			if (documento == null) {
				documento = copiador.copiar(proceso, original.getDocumento(),copiaTramite);
			}
			copia.setDocumento(documento);
			
			em.persist(copia);
			em.flush();
			
			copiador.guardarReferencia(proceso, Documentodeterminacion.class.getSimpleName(), original.getIden(), copia.getIden());
		}
		
	}

	/**
	 * 
	 * @param proceso
	 * @param original
	 * @param copiaTramite
	 */
	private void copiar(int proceso, 
			es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Documentoentidad original, 
			Tramite copiaTramite) {
		Documentoentidad copia = getReferencia(proceso, Documentoentidad.class, original.getIden());
		
		if (copia == null) {
			copia = new Documentoentidad();
			
			copia.setDocumento(copiador.copiar(proceso, original.getDocumento(), copiaTramite));
			copia.setEntidad(getReferencia(proceso, Entidad.class, original.getEntidad().getIden()));
			
			em.persist(copia);
			em.flush();
			
			copiador.guardarReferencia(proceso, Documentoentidad.class.getSimpleName(), original.getIden(), copia.getIden());
		}
		
	}

	/**
	 * 
	 * @param proceso
	 * @param original
	 * @param copiaTramite
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Entidad copiar(int proceso, 
			es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidad original, 
			int idTramiteOrigen,
			Tramite copiaTramite) {
		
		Entidad copia = getReferencia(proceso, Entidad.class, original.getIden());
		
		if (copia == null && original.getTramite().getIden() == idTramiteOrigen) {
			log.debug("Copiando entidad " + original.getCodigo() + " del plan " + original.getTramite().getPlan().getCodigo());
			
			copia = new Entidad();
			
			copia.setBsuspendida(original.isBsuspendida());
			copia.setClave(original.getClave());
			copia.setCodigo(original.getCodigo());
			copia.setEtiqueta(original.getEtiqueta());
			copia.setNombre(original.getNombre());
			copia.setOrden(original.getOrden());
			copia.setTramite(copiaTramite);
			
			em.persist(copia);
			em.flush();
			
			copiador.guardarReferencia(proceso, Entidad.class.getSimpleName(), original.getIden(), copia.getIden());
			
			if (original.getEntidadByIdentidadbase() != null) {
				copia.setEntidadByIdentidadbase(copiar(proceso, original.getEntidadByIdentidadbase(), idTramiteOrigen,copiaTramite));
			}
			if (original.getEntidadByIdentidadoriginal() != null) {
				copia.setEntidadByIdentidadoriginal(copiar(proceso, original.getEntidadByIdentidadoriginal(), idTramiteOrigen,copiaTramite));
			}
			if (original.getEntidadByIdpadre() != null) {
				copia.setEntidadByIdpadre(copiar(proceso, original.getEntidadByIdpadre(), idTramiteOrigen,copiaTramite));
			}
			
			// Se copian las referencias a esta entidad a través de adscripciones.
			List<es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Vectorrelacion> vectores = em.createQuery("SELECT vr FROM es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Vectorrelacion vr WHERE vr.iddefvector IN (4,5) AND vr.valor = :valor")
					.setParameter("valor", original.getIden()).getResultList();
			for (es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Vectorrelacion vr : vectores) {
				copiador.copiar(proceso, vr, copiaTramite);
			}
			
			// Una vez copiados los datos básicos de una entidad copio aquellos
			// datos que dependen única y exclusivamente de la entidad.
			
			for (es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidadlin linea : original.getEntidadlins()) {
				copiador.copiar(proceso, linea);
			}
			
			for (es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidadpnt punto : original.getEntidadpnts()) {
				copiador.copiar(proceso, punto);
			}
			
			for (es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidadpol poligono : original.getEntidadpols()) {
				copiador.copiar(proceso, poligono);
			}
			
			for (es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Documentoentidad de : original.getDocumentoentidads()) {
				copiar(proceso, de, copiaTramite);
			}
			
			for (es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidaddeterminacion ed : original.getEntidaddeterminacions()) {
				copiar(proceso, ed, idTramiteOrigen, copiaTramite);
			}
		}
		return copia;
	}

	/**
	 * 
	 * @param proceso
	 * @param original
	 * @param idTramiteOrigen
	 * @param copiaTramite
	 * @return
	 */
	private Entidaddeterminacion copiar(int proceso,
			es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidaddeterminacion original, 
			int idTramiteOrigen, 
			Tramite copiaTramite) {
		
		// Primero nos aseguramos que tanto la entidad como la determinación
		// ya están en el trámite refundido
		Entidad entidad = copiar(proceso, original.getEntidad(), idTramiteOrigen, copiaTramite);
					
		Determinacion determinacion = copiar(proceso, original.getDeterminacion(), idTramiteOrigen,copiaTramite);
		
		Entidaddeterminacion copia = getReferencia(proceso, Entidaddeterminacion.class, original.getIden());
		if (copia == null && entidad != null && determinacion != null) {
			log.debug("Copiando entidaddeterminacion entidad " + original.getEntidad().getCodigo() + " determinacion " + original.getDeterminacion().getCodigo());
			copia = new Entidaddeterminacion();
			
			copia.setEntidad(entidad);
			copia.setDeterminacion(determinacion);
			
			em.persist(copia);
			em.flush();
			
			copiador.guardarReferencia(proceso, Entidaddeterminacion.class.getSimpleName(), original.getIden(), copia.getIden());
			
			for (es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Casoentidaddeterminacion ced: original.getCasoentidaddeterminacions()) {
				copiar(proceso, ced, idTramiteOrigen, copiaTramite);
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
	private Entidaddeterminacionregimen copiar(int proceso, 
			es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidaddeterminacionregimen original, 
			int idTramiteOrigen,
			Tramite copiaTramite) {
		
		Casoentidaddeterminacion caso = copiar(proceso, original.getCasoentidaddeterminacionByIdcaso(), idTramiteOrigen,copiaTramite);
		
		Entidaddeterminacionregimen copia = getReferencia(proceso, Entidaddeterminacionregimen.class, original.getIden());
		if (copia == null && caso != null) {
			copia = new Entidaddeterminacionregimen();
			copia.setCasoentidaddeterminacionByIdcaso(caso);
			 
			copia.setSuperposicion(original.getSuperposicion());
			copia.setValor(original.getValor());
			
			em.persist(copia);
			em.flush();
			
			copiador.guardarReferencia(proceso, Entidaddeterminacionregimen.class.getSimpleName(), original.getIden(), copia.getIden());
			
			if (original.getCasoentidaddeterminacionByIdcasoaplicacion() != null) {
				copia.setCasoentidaddeterminacionByIdcasoaplicacion(copiar(proceso, original.getCasoentidaddeterminacionByIdcasoaplicacion(), idTramiteOrigen, copiaTramite));
			}
			
			if (original.getDeterminacion() != null) {
				copia.setDeterminacion(copiar(proceso, original.getDeterminacion(), idTramiteOrigen, copiaTramite));
			}
			
			if (original.getOpciondeterminacion() != null) {
				copia.setOpciondeterminacion(copiar(proceso, original.getOpciondeterminacion(), idTramiteOrigen, copiaTramite));
			}
			
			for (es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Regimenespecifico re : original.getRegimenespecificos()) {
				copiar(proceso, re, copiaTramite);
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
	private Opciondeterminacion copiar(int proceso,
			es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Opciondeterminacion original,
			int idTramiteOrigen,
			Tramite copiaTramite) {
		Determinacion determinacion = copiar(proceso, original.getDeterminacionByIddeterminacion(), idTramiteOrigen,copiaTramite);
		
		Determinacion valorReferencia = copiar(proceso, original.getDeterminacionByIddeterminacionvalorref(), idTramiteOrigen, copiaTramite);
		
		Opciondeterminacion copia = getReferencia(proceso, Opciondeterminacion.class, original.getIden());
		if (copia == null && determinacion != null && valorReferencia != null) {
			copia = new Opciondeterminacion();
			copia.setDeterminacionByIddeterminacion(determinacion);
			copia.setDeterminacionByIddeterminacionvalorref(valorReferencia);
			em.persist(copia);
			em.flush();
			
			copiador.guardarReferencia(proceso, Opciondeterminacion.class.getSimpleName(), original.getIden(), copia.getIden());
		}
		return copia;
	}

	/*
	 * (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.servicios.refundido.ReplicadorRefundidoLocal#copiar(int, es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Plan, boolean, boolean)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Plan copiar(int proceso,
			es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Plan plan, 
			boolean incluirTramite,
			boolean recalcular) {
		
		Plan copia = getReferencia(proceso, Plan.class, plan.getIden());
		if (copia == null) {
			log.debug("Copiando  plan " + plan.getCodigo());
			copia = new Plan();
			
			copia.setBsuspendido(plan.getBsuspendido());
			copia.setCodigo(plan.getCodigo());
			copia.setIdambito(plan.getIdambito());
			copia.setNombre(plan.getNombre());
			copia.setOrden(plan.getOrden());
			copia.setTexto(plan.getTexto());
			
			if (plan.getPlanByIdplanbase() != null) {
				int referencia = getReferencia(proceso, "plan", plan.getPlanByIdplanbase().getIden());
				Plan copiaPlanBase = em.find(Plan.class, referencia);
				if (copiaPlanBase == null) {
					copiaPlanBase = copiar(proceso, plan.getPlanByIdplanbase(), true, false);
				}
				copia.setPlanByIdplanbase(copiaPlanBase);
			}
			
			if (plan.getPlanByIdpadre() != null) {
				copia.setPlanByIdpadre(copiar(proceso, plan.getPlanByIdpadre(),true, recalcular));
			}
			
			em.persist(copia);
			em.flush();
			
			copiador.guardarReferencia(proceso, Plan.class.getSimpleName(), plan.getIden(), copia.getIden());
			
			if (incluirTramite) {
				List<es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Tramite> vigentes = em.createNamedQuery("Tramite.findVigente")
						.setParameter("idPlan", plan.getIden()).getResultList();
				
				if (vigentes.size() > 0) {
					copiar(proceso, vigentes.get(0),null, recalcular);
				} else {
					// Los planes base en ocasiones no tienen el trámite consolidado
					if (plan.getPlanByIdplanbase() == null) {
						vigentes = em.createNamedQuery("Tramite.obtenerPendientesPlan")
								.setParameter("idPlan", plan.getIden()).getResultList();
						if (vigentes.size() > 0) {
							copiar(proceso, vigentes.get(0), null, recalcular);
						}
					}
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
	private Regimenespecifico copiar(int proceso,
			es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Regimenespecifico original, 
			Tramite copiaTramite) {
		Regimenespecifico copia = getReferencia(proceso, Regimenespecifico.class, original.getIden());
		
		if (copia == null) {
			copia = new Regimenespecifico();
			
			copia.setEntidaddeterminacionregimen(getReferencia(proceso, Entidaddeterminacionregimen.class,original.getEntidaddeterminacionregimen().getIden()));
			copia.setNombre(original.getNombre());
			copia.setOrden(original.getOrden());
			copia.setTexto(original.getTexto());
			
			em.persist(copia);
			em.flush();
			
			copiador.guardarReferencia(proceso, Regimenespecifico.class.getSimpleName(), original.getIden(), copia.getIden());
			
			if (original.getRegimenespecifico() != null) {
				copia.setRegimenespecifico(copiar(proceso, original.getRegimenespecifico(),copiaTramite));
			}
		}
		
		return copia;
	}

	/*
	 * (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.servicios.refundido.ReplicadorRefundidoLocal#copiar(int, es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Tramite, es.mitc.redes.urbanismoenred.data.rpm.refundido.Tramite, boolean)
	 */
	@Override
	public Tramite copiar(int proceso,
			es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Tramite tramite,
			Tramite destino,
			boolean recalcular) {
		
		if (destino == null) {
			log.debug("Copiando tramite del plan " + tramite.getPlan().getCodigo());
			
			destino = new Tramite();
			
			destino.setCodigofip(tramite.getCodigofip());
			destino.setComentario(tramite.getComentario());
			destino.setFecha(tramite.getFecha());
			destino.setFechaconsolidacion(tramite.getFechaconsolidacion());
			destino.setIdcentroproduccion(tramite.getIdcentroproduccion());
			destino.setIdorgano(tramite.getIdorgano());
			destino.setIdsentido(tramite.getIdsentido());
			destino.setIdtipotramite(tramite.getIdtipotramite());
			destino.setIteracion(tramite.getIteracion());
			destino.setNombre(tramite.getNombre());
			destino.setNumeroacuerdo(tramite.getNumeroacuerdo());
			destino.setPlan(copiar(proceso, tramite.getPlan(), false, recalcular));
			destino.setTexto(tramite.getTexto());
			
			em.persist(destino);
			em.flush();
			
			copiador.guardarReferencia(proceso, Tramite.class.getSimpleName(), tramite.getIden(), destino.getIden());
		}
		
		log.debug("Copiando contenido del trámite " + tramite.getCodigofip());
		
		for (es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Documento documento: tramite.getDocumentos()) {
			copiador.copiar(proceso, documento, destino);
		}
		
		for (es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Determinacion determinacion : tramite.getDeterminacions()) {
			copiar(proceso, determinacion, determinacion.getTramite().getIden(), destino);
		}
		
		for (es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidad entidad : tramite.getEntidads()) {
			copiar(proceso, entidad, tramite.getIden(), destino);
		}
		
		return destino;
	}

	/**
	 * 
	 * @param proceso
	 * @param original
	 * @param copiaTramite
	 */
	private void copiar(int proceso,
			es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Vinculocaso original, 
			int idTramiteOrigen,
			Tramite copiaTramite) {
		
		Casoentidaddeterminacion caso = copiar(proceso, original.getCasoentidaddeterminacionByIdcaso(), idTramiteOrigen, copiaTramite);
		Casoentidaddeterminacion vinculado = copiar(proceso, original.getCasoentidaddeterminacionByIdcasovinculado(), idTramiteOrigen, copiaTramite);
		
		Vinculocaso copia = getReferencia(proceso, Vinculocaso.class, original.getIden());
		if (copia == null && caso != null && vinculado != null) {
			copia = new Vinculocaso();
			copia.setCasoentidaddeterminacionByIdcaso(caso);
			copia.setCasoentidaddeterminacionByIdcasovinculado(vinculado);
			
			em.persist(copia);
			em.flush();
			
			copiador.guardarReferencia(proceso, Vinculocaso.class.getSimpleName(), original.getIden(), copia.getIden());
		}
	}

	/**
	 * 
	 * @param idTramite
	 * @return
	 */
	private String getCodigoDeterminacion(int idTramite) {
		String codigoDet = (String)em.createNamedQuery("refundido.Determinacion.obtenerMaximoCodigo")
        		.setParameter("idTramite", idTramite)
        		.getSingleResult();
		return String.format("%010d", codigoDet != null? Integer.parseInt(codigoDet)+1 : 1);
	}
	
	/*
	 * (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.servicios.refundido.ReplicadorRefundidoLocal#getReferencia(int, java.lang.Class, int)
	 */
	@Override
	public <T> T getReferencia(int proceso, Class<T> clase, int idplaneamiento) {
		return copiador.getReferencia(proceso, clase, idplaneamiento);
	}

	/*
	 * (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.servicios.refundido.ReplicadorRefundidoLocal#getReferencia(int, java.lang.String, int)
	 */
	@Override
	public int getReferencia(int proceso, String tabla, int idplaneamiento) {
		return copiador.getReferencia(proceso, tabla, idplaneamiento);
	}
}

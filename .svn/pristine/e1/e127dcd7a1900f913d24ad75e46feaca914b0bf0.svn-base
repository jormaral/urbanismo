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
package es.mitc.redes.urbanismoenred.servicios.dal;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;

import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Ambitoaplicacionambito;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Boletintramite;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Casoentidaddeterminacion;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Determinacion;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Determinaciongrupoentidad;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Documento;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Documentocaso;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Documentodeterminacion;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Documentoentidad;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Documentoshp;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidad;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidaddeterminacion;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidaddeterminacionregimen;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidadlin;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidadpnt;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidadpol;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Opciondeterminacion;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Operacion;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Operaciondeterminacion;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Operacionentidad;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Operacionplan;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Operacionrelacion;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Plan;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Planentidadordenacion;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Planshp;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Propiedadrelacion;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Regimenespecifico;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Relacion;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Tramite;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Vectorrelacion;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Vinculocaso;

/**
 * Session Bean implementation class EliminadorEntidadesBean
 * 
 * @author Arnaiz Consultores
 */
@Stateless(name = "EliminadorEntidades")
public class EliminadorEntidadesBean implements EliminadorEntidadesLocal {
	
	private static final Logger log = Logger.getLogger(EliminadorEntidadesBean.class);
	
	@PersistenceContext(unitName = "rpmv2")
	private EntityManager em;

    /**
     * Default constructor. 
     */
    public EliminadorEntidadesBean() {
    }

    /**
     * 
     * @param ced
     */
    @SuppressWarnings("unchecked")
	private void eliminar(Casoentidaddeterminacion ced) {
    	
    	if (em.contains(ced)) {
    		log.debug("Eliminando ced " + ced.getIden());
    		List<Documentocaso> dcs = em.createQuery("select dc from es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Documentocaso dc where dc.casoentidaddeterminacion.iden = " + ced.getIden()).getResultList();
			for (Documentocaso dc: dcs) {
				log.debug("Eliminando dc " + dc.getIden());
				em.remove(dc);
			}
			List<Entidaddeterminacionregimen> edrs = em.createQuery("select edr from es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidaddeterminacionregimen edr where edr.casoentidaddeterminacionByIdcaso.iden = " + ced.getIden()).getResultList();
			for (Entidaddeterminacionregimen edr : edrs) {
				log.debug("Eliminando edr por idcaso " + edr.getIden());
				eliminar(edr);
			}
			
			edrs = em.createQuery("select edr from es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidaddeterminacionregimen edr where edr.casoentidaddeterminacionByIdcasoaplicacion.iden = " + ced.getIden()).getResultList();
			for (Entidaddeterminacionregimen edr : edrs) {
				log.debug("Eliminando edr por caso aplicacion " + edr.getIden());
				eliminar(edr);
			}
			
			List<Vinculocaso> vcs = em.createQuery("select vc from es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Vinculocaso vc where vc.casoentidaddeterminacionByIdcaso.iden = " + ced.getIden()).getResultList();
			for (Vinculocaso vc : vcs) {
				log.debug("Eliminando vc " + vc.getIden());
				em.remove(vc);
			}
			
			vcs = em.createQuery("select vc from es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Vinculocaso vc where vc.casoentidaddeterminacionByIdcasovinculado.iden = " + ced.getIden()).getResultList();
			for (Vinculocaso vc : vcs) {
				log.debug("Eliminando vc caso vinculado" + vc.getIden());
				em.remove(vc);
			}
			
			em.remove(ced);
			em.flush();
			log.debug("ced  eliminado" + ced.getIden());
    	}else {
			log.warn("No ced en EM " + ced.getIden());
		}
	}

    /*
	 * (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.servicios.dal.EliminadorEntidadesLocal#eliminar(es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Determinacion)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eliminar(Determinacion determinacion, EliminadorListener listener) {
		if (em.contains(determinacion)) {
			log.debug("Eliminando determinacion " + determinacion.getIden() + " " + determinacion.getApartado() + " " + determinacion.getNombre() + " " + determinacion.getCodigo());
			
			List<Determinaciongrupoentidad> dges = em.createQuery("select dge from es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Determinaciongrupoentidad dge where dge.determinacionByIddeterminacion.iden = " + determinacion.getIden()).getResultList();
			for (Determinaciongrupoentidad dge : dges) {
				log.debug("Eliminando dge iddeterminacion " + dge.getIden());
				em.remove(dge);
			}
			
			dges = em.createQuery("select dge from es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Determinaciongrupoentidad dge where dge.determinacionByIddeterminaciongrupo.iden = " + determinacion.getIden()).getResultList();
			for (Determinaciongrupoentidad dge : dges) {
				log.debug("Eliminando dge idgrupo " + dge.getIden());
				em.remove(dge);
			}
			
			List<Determinacion> determinaciones = em.createQuery("select d from es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Determinacion d where d.determinacionByIddeterminacionbase.iden = " + determinacion.getIden()).getResultList();
			for (Determinacion d : determinaciones) {
				eliminar(d, listener);
			}
			
			determinaciones = em.createQuery("select d from es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Determinacion d where d.determinacionByIddeterminacionoriginal.iden = " + determinacion.getIden()).getResultList();
			for (Determinacion d : determinaciones) {
				eliminar(d, listener);
			}
			
			determinaciones = em.createQuery("select d from es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Determinacion d where d.determinacionByIdpadre.iden = " + determinacion.getIden()).getResultList();
			for (Determinacion d : determinaciones) {
				eliminar(d, listener);
			}
			
			List<Documentodeterminacion> docs = em.createQuery("select d from es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Documentodeterminacion d where d.determinacion.iden = " + determinacion.getIden()).getResultList();
			for (Documentodeterminacion dd : docs) {
				log.debug("Eliminando dds " + dd.getIden());
				em.remove(dd);
			}
			
			List<Entidaddeterminacionregimen> edrs = em.createQuery("select edr from es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidaddeterminacionregimen edr where edr.determinacion.iden = " + determinacion.getIden()).getResultList();
			for (Entidaddeterminacionregimen edr : edrs) {
				log.debug("Eliminando edrs " + edr.getIden());
				eliminar(edr);
			}
			
			List<Entidaddeterminacion> eds = em.createQuery("select ed from es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidaddeterminacion ed where ed.determinacion.iden = " + determinacion.getIden()).getResultList();
			for (Entidaddeterminacion ed : eds) {
				log.debug("Eliminando eds " + ed.getIden());
				eliminar(ed, null);
			}
			
			List<Opciondeterminacion> ods = em.createQuery("select od from es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Opciondeterminacion od where od.determinacionByIddeterminacion.iden = " + determinacion.getIden()).getResultList();
			for (Opciondeterminacion od : ods) {
				log.debug("Eliminando od iddet " + od.getIden());
				eliminar(od);
			}
			
			ods = em.createQuery("select od from es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Opciondeterminacion od where od.determinacionByIddeterminacionvalorref.iden = " + determinacion.getIden()).getResultList();
			for (Opciondeterminacion od : ods) {
				log.debug("Eliminando od idvalorref " + od.getIden());
				eliminar(od);
			}
			
			List<Operaciondeterminacion> opds = em.createQuery("select od from es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Operaciondeterminacion od where od.determinacionByIddeterminacion.iden = " + determinacion.getIden()).getResultList();
			for (Operaciondeterminacion od : opds) {
				log.debug("Eliminando operacion operado " + od.getIden());
				em.remove(od);
			}
			
			opds = em.createQuery("select od from es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Operaciondeterminacion od where od.determinacionByIddeterminacionoperadora.iden = " + determinacion.getIden()).getResultList();
			for (Operaciondeterminacion od : opds) {
				log.debug("Eliminando operacion operador " + od.getIden());
				em.remove(od);
			}
			
			em.remove(determinacion);
			em.flush();
			log.debug("determinacion eliminada " + determinacion.getIden());
		} else {
			log.warn("determinacion no está en EM " + determinacion.getIden());
		}
	}

	/**
	 * 
	 * @param documento
	 * @param listener 
	 */
	@SuppressWarnings("unchecked")
	private void eliminar(Documento documento, EliminadorListener listener) {
		log.debug("Eliminando documento " + documento.getIden());
		if (em.contains(documento)) {
			
			List<Documentoentidad> des = em.createQuery("select d from es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Documentoentidad d where d.documento.iden = " + documento.getIden()).getResultList();
			for (Documentoentidad de : des) {
				log.debug("Eliminando documentoentidad " + de.getIden());
				em.remove(de);
			}
			
			List<Documentocaso> dcs = em.createQuery("select d from es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Documentocaso d where d.documento.iden = " + documento.getIden()).getResultList();
			for (Documentocaso d : dcs) {
				log.debug("Eliminando documentocaso " + d.getIden());
				em.remove(d);
			}
			
			List<Documentodeterminacion> dds = em.createQuery("select d from es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Documentodeterminacion d where d.documento.iden = " + documento.getIden()).getResultList();
			for (Documentodeterminacion d : dds) {
				log.debug("Eliminando Documentodeterminacion " + d.getIden());
				em.remove(d);
			}
			
			List<Documento> ds = em.createQuery("select d from es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Documento d where d.documento.iden = " + documento.getIden()).getResultList();
			for (Documento d : ds) {
				log.debug("Eliminando documento original " + d.getIden());
				em.remove(d);
			}
			
			List<Documentoshp> dss = em.createQuery("select d from es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Documentoshp d where d.documento.iden = " + documento.getIden()).getResultList();
			for (Documentoshp d : dss) {
				log.debug("Eliminando Documentoshp " + d.getIden());
				em.remove(d);
			}
			
			em.remove(documento);
			em.flush();
		}
	}

	/*
	 * (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.servicios.dal.EliminadorEntidadesLocal#eliminar(es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidad)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eliminar(Entidad entidad, EliminadorListener listener) {
		log.debug("Eliminando entidad " + entidad.getIden() + " " + entidad.getCodigo() + " " + entidad.getNombre());
		if (em.contains(entidad)) {
			
			List<Documentoentidad> des = em.createQuery("select d from es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Documentoentidad d where d.entidad.iden = " + entidad.getIden()).getResultList();
			for (Documentoentidad de : des) {
				log.debug("Eliminando de " + de.getIden());
				em.remove(de);
			}
			
			List<Operacionentidad> oes = em.createQuery("select o from es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Operacionentidad o where o.entidadByIdentidad.iden = " + entidad.getIden()).getResultList();
			for (Operacionentidad oe : oes) {
				log.debug("Eliminando operacion por operada " + oe.getIden());
				em.remove(oe);
			}
			
			oes = em.createQuery("select o from es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Operacionentidad o where o.entidadByIdentidadoperadora.iden = " + entidad.getIden()).getResultList();
			for (Operacionentidad oe : oes) {
				log.debug("Eliminando operacion por operadora " + oe.getIden());
				em.remove(oe);
			}
			
			List<Ambitoaplicacionambito> aaas = em.createQuery("select a from es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Ambitoaplicacionambito a where a.entidad.iden = " + entidad.getIden()).getResultList();
			for (Ambitoaplicacionambito aaa : aaas) {
				log.debug("Eliminando aaa " + aaa.getIden());
				em.remove(aaa);
			}
			
			List<Entidad> entidades = em.createQuery("select e from es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidad e where e.entidadByIdentidadbase.iden = " + entidad.getIden()).getResultList();
			for (Entidad e : entidades) {
				log.debug("Eliminando entidad  base" + e.getIden());
				eliminar(e, listener);
			}
			
			entidades = em.createQuery("select e from es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidad e where e.entidadByIdentidadoriginal.iden = " + entidad.getIden()).getResultList();
			for (Entidad e : entidades) {
				log.debug("Eliminando entidad original" + e.getIden());
				eliminar(e, listener);
			}
			
			entidades = em.createQuery("select e from es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidad e where e.entidadByIdpadre.iden = " + entidad.getIden()).getResultList();
			for (Entidad e : entidades) {
				log.debug("Eliminando entidad padre" + e.getIden());
				eliminar(e, listener);
			}
			
			List<Entidaddeterminacion> eds = em.createQuery("select e from es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidaddeterminacion e where e.entidad.iden = " + entidad.getIden()).getResultList();
			for(Entidaddeterminacion ed : eds) {
				log.debug("Eliminando ed " + ed.getIden());
				eliminar(ed, null);
			}
			
			List<Planentidadordenacion> peos = em.createQuery("select p from es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Planentidadordenacion p where p.entidad.iden = " + entidad.getIden()).getResultList();
			for (Planentidadordenacion peo: peos) {
				if (em.contains(peo)) {
					log.debug("Eliminando peo " + peo.getIden());
					em.remove(peo);
				} else {
					log.warn("Peo no pertenece al EM " + peo.getIden());
				}
			}
			
			List<Entidadpol> eps = em.createQuery("select e from es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidadpol e where e.entidad.iden = " + entidad.getIden()).getResultList();
			for (Entidadpol ep: eps) {
				if (em.contains(ep)) {
					log.debug("Eliminando polilinea " + ep.getIden());
					em.remove(ep);
				} else {
					log.warn("Polilinea no pertenece al EM " + ep.getIden());
				}
			}
			
			List<Entidadlin> els = em.createQuery("select e from es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidadlin e where e.entidad.iden = " + entidad.getIden()).getResultList();
			for (Entidadlin el: els) {
				if (em.contains(el)) {
					log.debug("Eliminando linea " + el.getIden());
					em.remove(el);
				} else {
					log.warn("Línea no pertenece al EM " + el.getIden());
				}
			}
			
			List<Entidadpnt> epnts = em.createQuery("select e from es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidadpnt e where e.entidad.iden = " + entidad.getIden()).getResultList();
			for (Entidadpnt epnt: epnts) {
				if (em.contains(epnt)) {
					log.debug("Eliminando punto " + epnt.getIden());
					em.remove(epnt);
				} else {
					log.warn("Punto no pertenece al EM " + epnt.getIden());
				}
			}
			
			em.remove(entidad);
			em.flush();
			log.debug("entidad eliminada " + entidad.getIden() + " " + entidad.getCodigo() + " " + entidad.getNombre());
		}else {
			log.warn("entidad no está en EM " + entidad.getIden());
		}
	}

	/*
	 * (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.servicios.dal.EliminadorEntidadesLocal#eliminar(es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidaddeterminacion)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eliminar(Entidaddeterminacion ed, EliminadorListener listener) {
		log.debug("Eliminando ed " + ed.getIden() + " identidad " +ed.getEntidad().getIden() + " iddeterminacion " + ed.getDeterminacion().getIden());
		if (em.contains(ed)) {
			
			List<Casoentidaddeterminacion> ceds = em.createQuery("select ced from es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Casoentidaddeterminacion ced where ced.entidaddeterminacion.iden = " + ed.getIden()).getResultList();
			for(Casoentidaddeterminacion ced : ceds) {
				eliminar(ced);
			}
			
			em.remove(ed);
			em.flush();
			log.debug("ed eliminada " + ed.getIden());
		} else {
			log.warn("ed no está en EM " + ed.getIden());
		}
	}

	/**
	 * 
	 * @param edr
	 */
	@SuppressWarnings("unchecked")
	private void eliminar(Entidaddeterminacionregimen edr) {
		
		if (em.contains(edr)) {
			log.debug("Eliminando edr " + edr.getIden());
			
			List<Regimenespecifico> res = em.createQuery("select r from es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Regimenespecifico r where r.entidaddeterminacionregimen.iden = " + edr.getIden()).getResultList();
			for(Regimenespecifico re: res) {
				eliminar(re);
			}
			
			em.remove(edr);
			em.flush();
			log.debug("edr eliminado " + edr.getIden());
		} else {
			log.warn("edr no está en EM " + edr.getIden());
		}
	}

	/**
	 * 
	 * @param od
	 */
	@SuppressWarnings("unchecked")
	private void eliminar(Opciondeterminacion od) {
		if (em.contains(od)) {
			log.debug("Eliminando opcion " + od.getIden());
			
			List<Entidaddeterminacionregimen> edrs = em.createQuery("select edr from es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidaddeterminacionregimen edr where edr.opciondeterminacion.iden = " + od.getIden()).getResultList();
			for (Entidaddeterminacionregimen edr : edrs) {
				log.debug("Eliminando edr " + od.getIden());
				eliminar(edr);
			}
			
			em.remove(od);
			em.flush();
		} else {
			log.warn("OD no está en EM " + od.getIden());
		}
		
	}

	/*
	 * (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.servicios.dal.EliminadorEntidadesLocal#eliminar(es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Operacion)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eliminar(Operacion operacion, EliminadorListener listener) {
		log.debug("Eliminando operacion " + operacion.getIden());
		if (em.contains(operacion)) {
		
			List<Operaciondeterminacion> ods = em.createQuery("select o from es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Operaciondeterminacion o where o.operacion.iden = " + operacion.getIden()).getResultList();
			for (Operaciondeterminacion op : ods) {
				if (em.contains(op)) {
					log.debug("Eliminando operacion " + op.getIden());
					em.remove(op);
				} else {
					log.warn("Operacion no pertenece al EM " + op.getIden());
				}
			}
			
			List<Operacionentidad> oes = em.createQuery("select o from es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Operacionentidad o where o.operacion.iden = " + operacion.getIden()).getResultList();
			for (Operacionentidad op : oes) {
				if (em.contains(op)) {
					log.debug("Eliminando operacion " + op.getIden());
					em.remove(op);
				} else {
					log.warn("Operacion no pertenece al EM " + op.getIden());
				}
			}
			
			List<Operacionrelacion> ors = em.createQuery("select o from es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Operacionrelacion o where o.operacion.iden = " + operacion.getIden()).getResultList();
			for (Operacionrelacion op : ors) {
				if (em.contains(op)) {
					log.debug("Eliminando operacion " + op.getIden());
					em.remove(op);
				} else {
					log.warn("Operacion no pertenece al EM " + op.getIden());
				}
			}
			
			em.remove(operacion);
			em.flush();
		}
	}

	/*
	 * (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.servicios.dal.EliminadorEntidadesLocal#eliminar(es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Plan)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eliminar(Plan plan, EliminadorListener listener) {
		log.debug("Eliminando plan " + plan.getIden());
		em.flush();
		
		if (em.contains(plan)) {
			
			List<Operacionplan> ops = em.createQuery("select o from es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Operacionplan o where o.planByIdplanoperado.iden = " + plan.getIden()).getResultList();
			for (Operacionplan op: ops) {
				if (em.contains(op)) {
					log.debug("Eliminando operacion por plan operado " + op.getIden());
					em.remove(op);
				} else {
					log.warn("Operacion no pertenece al EM " + op.getIden());
				}
			}
			
			ops = em.createQuery("select o from es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Operacionplan o where o.planByIdplanoperador.iden = " + plan.getIden()).getResultList();
			for (Operacionplan op: ops) {
				if (em.contains(op)) {
					log.debug("Eliminando operacion " + op.getIden());
					em.remove(op);
				} else {
					log.warn("Operacion no pertenece al EM " + op.getIden());
				}
			}
			
			List<Planentidadordenacion> peos = em.createQuery("select p from es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Planentidadordenacion p where p.plan.iden = " + plan.getIden()).getResultList();
			for (Planentidadordenacion peo: peos) {
				if (em.contains(peo)) {
					log.debug("Eliminando peo " + peo.getIden());
					em.remove(peo);
				} else {
					log.warn("Peo no pertenece al EM " + peo.getIden());
				}
			}
			
			List<Plan> planes = em.createQuery("select p from es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Plan p where p.planByIdpadre.iden = " + plan.getIden()).getResultList();
			for (Plan p: planes) {
				eliminar(p, listener);
			}
			
			planes = em.createQuery("select p from es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Plan p where p.planByIdplanbase.iden = " + plan.getIden()).getResultList();
			for (Plan p: planes) {
				eliminar(p, listener);
			}
			
			List<Planshp> ps = em.createQuery("select p from es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Planshp p where p.plan.iden = " + plan.getIden()).getResultList();
			for (Planshp pshp: ps) {
				if (em.contains(pshp))
					em.remove(pshp);
			}
			
			List<Tramite> tramites = em.createQuery("select t from es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Tramite t where t.plan.iden = " + plan.getIden()).getResultList();
			for (Tramite tramite: tramites) {
				eliminar(tramite, true, listener);
			}
			
			em.remove(plan);
			em.flush();
			log.debug("plan eliminado " + plan.getIden());
		} else {
			log.warn("plan no está en EM " + plan.getIden());
		}
	}

	/**
	 * 
	 * @param re
	 */
	@SuppressWarnings("unchecked")
	private void eliminar(Regimenespecifico re) {
		log.debug("Eliminando re " + re.getIden() + " " + re.getNombre() + " " +re.getTexto());
		if (em.contains(re)) {
			List<Regimenespecifico> regimenes = em.createQuery("select r from es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Regimenespecifico r where r.regimenespecifico.iden = " + re.getIden()).getResultList();
			for (Regimenespecifico regimen : regimenes) {
				eliminar(regimen);
			}
			em.remove(re);
			em.flush();
			log.debug("re eliminado " + re.getIden());
		} else {
			log.warn("re no está en EM " + re.getIden());
		}
	}

	/*
	 * (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.servicios.dal.EliminadorEntidadesLocal#eliminar(es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Relacion)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eliminar(Relacion relacion, EliminadorListener listener) {
		if (em.contains(relacion)) {
			log.debug("Eliminando relacion " + relacion.getIden());
			
			List<Operacionrelacion> ors = em.createQuery("select o from es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Operacionrelacion o where o.relacion.iden = " + relacion.getIden()).getResultList();
			for (Operacionrelacion or : ors) {
				em.remove(or);
			}
			
			List<Propiedadrelacion> prs = em.createQuery("select p from es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Propiedadrelacion p where p.relacion.iden = " + relacion.getIden()).getResultList();
			for (Propiedadrelacion pr : prs) {
				em.remove(pr);
			}
			
			List<Vectorrelacion> vrs = em.createQuery("select v from es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Vectorrelacion v where v.relacion.iden = " + relacion.getIden()).getResultList();
			for (Vectorrelacion vr : vrs) {
				em.remove(vr);
			}
			
			em.remove(relacion);
			em.flush();
		} else {
			log.debug("Relacion " + relacion.getIden() + " ya está eliminada");
		}
	}

	/*
	 * (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.servicios.dal.EliminadorEntidadesLocal#eliminar(es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Tramite, boolean, es.mitc.redes.urbanismoenred.servicios.dal.EliminadorListener)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eliminar(Tramite tramite, boolean incluido, EliminadorListener listener) {
		log.debug("Eliminando tramite " + tramite.getIden());
		if (em.contains(tramite)) {
			List<Boletintramite> boletines = em.createQuery("select b from es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Boletintramite b where b.tramite.iden = " + tramite.getIden()).getResultList();
			int c = 1;
			int total = boletines.size();
			for (Boletintramite boletin: boletines) {
				em.remove(boletin);
				if (listener != null)
					listener.actualizacionProgreso(1, 7, c++, total);
			}
			
			List<Determinacion> determinaciones = em.createQuery("select d from es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Determinacion d where d.tramite.iden = " + tramite.getIden()).getResultList();
			c = 1;
			total = determinaciones.size();
			for (Determinacion determinacion: determinaciones) {
				eliminar(determinacion, listener);
				if (listener != null)
					listener.actualizacionProgreso(2, 7, c++, total);
			}
			
			List<Entidad> entidades = em.createQuery("select e from es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidad e where e.tramite.iden = " + tramite.getIden()).getResultList();
			c = 1;
			total = entidades.size();
			for (Entidad entidad : entidades) {
				eliminar(entidad, listener);
				if (listener != null)
					listener.actualizacionProgreso(3, 7, c++, total);
			}
			
			List<Operacion> operaciones = em.createQuery("select o from es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Operacion o where o.tramite.iden = " + tramite.getIden()).getResultList();
			c = 1;
			total = operaciones.size();
			for (Operacion operacion : operaciones) {
				eliminar(operacion, listener);
				if (listener != null)
					listener.actualizacionProgreso(4, 7, c++, total);
			}
			
			List<Relacion> relaciones = em.createQuery("select r from es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Relacion r where r.tramiteByIdtramitecreador.iden = " + tramite.getIden()).getResultList();
			c = 1;
			total = relaciones.size();
			for (Relacion relacion: relaciones) {
				eliminar(relacion, listener);
				if (listener != null)
					listener.actualizacionProgreso(5, 7, c++, total);
			}
			
			List<Documento> documentos = em.createQuery("select d from es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Documento d where d.tramite.iden = " + tramite.getIden()).getResultList();
			c = 1;
			total = documentos.size();
			for (Documento documento: documentos) {
				eliminar(documento, null);
				if (listener != null)
					listener.actualizacionProgreso(6, 7, c++, total);
			}
			
			if (incluido) {
				em.remove(tramite);
			}
			
			if (listener != null)
				listener.actualizacionProgreso(7, 7, 1, 1);
			em.flush();
			log.debug("tramite eliminado " + tramite.getIden());
		}else {
			log.warn("tramite no está en EM " + tramite.getIden());
		}
	}
}

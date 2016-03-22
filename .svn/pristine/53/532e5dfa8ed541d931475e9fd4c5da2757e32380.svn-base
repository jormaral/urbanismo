/*
* Copyright 2011 red.es
* Autores: Arnaiz Consultores.
*
** Licencia con arreglo a la EUPL, Versión 1.1 o -en cuanto
** sean aprobadas por la Comision Europea- versiones
** posteriores de la EUPL (la <<Licencia>>);
** Solo podra usarse esta obra si se respeta la Licencia.
* Puede obtenerse una copia de la Licencia en:
*
* http://ec.europa.eu/idabc/eupl5
*
** Salvo cuando lo exija la legislacion aplicable o se acuerde
* por escrito, el programa distribuido con arreglo a la
* Licencia se distribuye <<TAL CUAL>>,
** SIN GARANTIAS NI CONDICIONES DE NINGUN TIPO, ni expresas
** ni implicitas.
** Vease la Licencia en el idioma concreto que rige
* los permisos y limitaciones que establece la Licencia.
*/
package es.mitc.redes.urbanismoenred.servicios.dal;

import java.io.File;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;

import es.mitc.redes.urbanismoenred.data.rpm.refundido.Ambitoaplicacionambito;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Casoentidaddeterminacion;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Determinacion;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Determinaciongrupoentidad;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Documento;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Documentocaso;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Documentodeterminacion;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Documentoentidad;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Documentoshp;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Entidad;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Entidaddeterminacion;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Entidaddeterminacionregimen;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Entidadlin;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Entidadpnt;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Entidadpol;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Opciondeterminacion;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Operacion;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Operaciondeterminacion;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Operacionentidad;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Operacionrelacion;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Propiedadrelacion;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Regimenespecifico;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Relacion;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Vectorrelacion;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Vinculocaso;

/**
 * Session Bean implementation class EliminadorEntidades
 * 
 * @author Arnaiz Consultores
 */
@Stateless(name = "EliminadorEntidadesRefundido")
public class EliminadorEntidadesRefundido implements EliminadorEntidadesRefundidoLocal {
	
	private static final String DIRECTORIO_TRABAJO = "var";
	
	private static final Logger log = Logger.getLogger(EliminadorEntidadesRefundido.class);
	
	@PersistenceContext(unitName = "rpmv2")
	private EntityManager em;

    /**
     * Default constructor. 
     */
    public EliminadorEntidadesRefundido() {
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
	 * @param ced
	 */
	private void eliminar(Casoentidaddeterminacion ced) {
		if (em.contains(ced)) {
			em.refresh(ced);
    		log.debug("Eliminando ced " + ced.getIden());
    		
			for (Documentocaso dc: ced.getDocumentocasos()) {
				log.debug("Eliminando dc " + dc.getIden());
				eliminar(dc.getDocumento());
			}
			
			for (Entidaddeterminacionregimen edr : ced.getEntidaddeterminacionregimensForIdcaso()) {
				log.debug("Eliminando edr por idcaso " + edr.getIden());
				eliminar(edr);
			}
			
			for (Entidaddeterminacionregimen edr : ced.getEntidaddeterminacionregimensForIdcasoaplicacion()) {
				log.debug("Eliminando edr por caso aplicacion " + edr.getIden());
				eliminar(edr);
			}
			
			for (Vinculocaso vc : ced.getVinculocasosForIdcaso()) {
				log.debug("Eliminando vc " + vc.getIden());
				em.remove(vc);
			}
			
			for (Vinculocaso vc : ced.getVinculocasosForIdcasovinculado()) {
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
	 * @see es.mitc.redes.urbanismoenred.servicios.refundido.EliminadorEntidadesLocal#eliminar(es.mitc.redes.urbanismoenred.data.rpm.refundido.Determinacion)
	 */
	@Override
	public void eliminar(Determinacion determinacion) {
		if (em.contains(determinacion)) {
			em.refresh(determinacion);
			log.debug("Eliminando determinacion " + determinacion.getIden() + " " + determinacion.getApartado() + " " + determinacion.getNombre() + " " + determinacion.getCodigo());
			
			
			for (Determinaciongrupoentidad dge : determinacion.getDeterminaciongrupoentidadsForIddeterminacion()) {
				log.debug("Eliminando dge iddeterminacion " + dge.getIden());
				em.remove(dge);
			}
			
			for (Determinaciongrupoentidad dge : determinacion.getDeterminaciongrupoentidadsForIddeterminaciongrupo()) {
				log.debug("Eliminando dge idgrupo " + dge.getIden());
				em.remove(dge);
			}
			
			for (Determinacion d : determinacion.getDeterminacionsForIddeterminacionbase()) {
				eliminar(d);
			}
			
			for (Determinacion d : determinacion.getDeterminacionsForIddeterminacionoriginal()) {
				eliminar(d);
			}
			
			for (Determinacion d : determinacion.getDeterminacionsForIdpadre()) {
				eliminar(d);
			}
			
			for (Documentodeterminacion dd : determinacion.getDocumentodeterminacions()) {
				log.debug("Eliminando dds " + dd.getIden());
				
				eliminar(dd.getDocumento());
			}
			
			for (Entidaddeterminacionregimen edr : determinacion.getEntidaddeterminacionregimens()) {
				log.debug("Eliminando edrs " + edr.getIden());
				eliminar(edr);
			}
			
			for (Entidaddeterminacion ed : determinacion.getEntidaddeterminacions()) {
				log.debug("Eliminando eds " + ed.getIden());
				eliminar(ed);
			}
			
			for (Opciondeterminacion od : determinacion.getOpciondeterminacionsForIddeterminacion()) {
				log.debug("Eliminando od iddet " + od.getIden());
				eliminar(od);
			}
			
			for (Opciondeterminacion od : determinacion.getOpciondeterminacionsForIddeterminacionvalorref()) {
				log.debug("Eliminando od idvalorref " + od.getIden());
				eliminar(od);
			}
			
			for (Operaciondeterminacion od : determinacion.getOperaciondeterminacionsForIddeterminacion()) {
				log.debug("Eliminando operacion operado " + od.getIden());
				em.remove(od);
			}
			
			for (Operaciondeterminacion od : determinacion.getOperaciondeterminacionsForIddeterminacionoperadora()) {
				log.debug("Eliminando operacion operador " + od.getIden());
				em.remove(od);
			}
			
			em.remove(determinacion);
			em.flush();
			log.debug("determinacion eliminada " + determinacion.getIden());
		}
	}

	/*
	 * (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.servicios.refundido.EliminadorEntidadesLocal#eliminar(es.mitc.redes.urbanismoenred.data.rpm.refundido.Documento)
	 */
	@Override
	public void eliminar(Documento documento) {
		log.debug("Eliminando documento " + documento.getIden());
		if (em.contains(documento)) {
			em.refresh(documento);
			
			String baseDirPath = System.getenv("REDES_PATH")+File.separator+ DIRECTORIO_TRABAJO + File.separator+"refundido" + File.separator + "FIPs" + File.separator + documento.getTramite().getCodigofip();
			File dir = new File(baseDirPath);
			File archivo = new File(dir, documento.getArchivo());
			if (archivo.exists()) {
				if (archivo.isDirectory()) {
					if (!borrarDirectorio(archivo)) {
						log.warn("No se ha podido eliminar el archivo : " + archivo.getAbsolutePath());
					}
				} else {
					archivo.delete();
				}
			}
			
			for (Documentoentidad de : documento.getDocumentoentidads()) {
				log.debug("Eliminando documentoentidad " + de.getIden());
				em.remove(de);
			}
			
			for (Documentocaso d : documento.getDocumentocasos()) {
				log.debug("Eliminando documentocaso " + d.getIden());
				em.remove(d);
			}
			
			for (Documentodeterminacion d : documento.getDocumentodeterminacions()) {
				log.debug("Eliminando Documentodeterminacion " + d.getIden());
				em.remove(d);
			}
			
			for (Documento d : documento.getDocumentos()) {
				log.debug("Eliminando documento original " + d.getIden());
				eliminar(documento);
			}
			
			for (Documentoshp d : documento.getDocumentoshps()) {
				log.debug("Eliminando Documentoshp " + d.getIden());
				em.remove(d);
			}
			
			em.remove(documento);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.servicios.refundido.EliminadorEntidadesLocal#eliminar(es.mitc.redes.urbanismoenred.data.rpm.refundido.Entidad)
	 */
	@Override
	public void eliminar(Entidad entidad) {
		log.debug("Eliminando entidad " + entidad.getIden() + " " + entidad.getCodigo() + " " + entidad.getNombre());
		if (em.contains(entidad)) {
			em.refresh(entidad);
			for (Documentoentidad de : entidad.getDocumentoentidads()) {
				log.debug("Eliminando de " + de.getIden());
				eliminar(de.getDocumento());
			}
			
			for (Operacionentidad oe : entidad.getOperacionentidadsForIdentidad()) {
				log.debug("Eliminando operacion por operada " + oe.getIden());
				em.remove(oe);
			}
			
			for (Operacionentidad oe : entidad.getOperacionentidadsForIdentidadoperadora()) {
				log.debug("Eliminando operacion por operadora " + oe.getIden());
				em.remove(oe);
			}
			
			for (Ambitoaplicacionambito aaa : entidad.getAmbitoaplicacionambitos()) {
				log.debug("Eliminando aaa " + aaa.getIden());
				em.remove(aaa);
			}
			
			for (Entidad e : entidad.getEntidadsForIdentidadbase()) {
				log.debug("Eliminando entidad  base" + e.getIden());
				eliminar(e);
			}
			
			for (Entidad e : entidad.getEntidadsForIdentidadoriginal()) {
				log.debug("Eliminando entidad original" + e.getIden());
				eliminar(e);
			}
			
			
			for (Entidad e : entidad.getEntidadsForIdpadre()) {
				log.debug("Eliminando entidad padre" + e.getIden());
				eliminar(e);
			}
			
			for(Entidaddeterminacion ed : entidad.getEntidaddeterminacions()) {
				log.debug("Eliminando ed " + ed.getIden());
				eliminar(ed);
			}
			
			for (Entidadpol ep: entidad.getEntidadpols()) {
				if (em.contains(ep)) {
					log.debug("Eliminando polilinea " + ep.getIden());
					em.remove(ep);
				} else {
					log.warn("Polilinea no pertenece al EM " + ep.getIden());
				}
			}
			
			for (Entidadlin el: entidad.getEntidadlins()) {
				if (em.contains(el)) {
					log.debug("Eliminando linea " + el.getIden());
					em.remove(el);
				} else {
					log.warn("Línea no pertenece al EM " + el.getIden());
				}
			}
			
			for (Entidadpnt epnt: entidad.getEntidadpnts()) {
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
	 * @see es.mitc.redes.urbanismoenred.servicios.dal.EliminadorEntidadesRefundidoLocal#eliminar(es.mitc.redes.urbanismoenred.data.rpm.refundido.Entidaddeterminacion)
	 */
	@Override
	public void eliminar(Entidaddeterminacion ed) {
		log.debug("Eliminando ed " + ed.getIden() + " identidad " +ed.getEntidad().getIden() + " iddeterminacion " + ed.getDeterminacion().getIden());
		if (em.contains(ed)) {
			em.refresh(ed);
			for(Casoentidaddeterminacion ced : ed.getCasoentidaddeterminacions()) {
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
	private void eliminar(Entidaddeterminacionregimen edr) {
		if (em.contains(edr)) {
			log.debug("Eliminando edr " + edr.getIden());
			em.refresh(edr);
			for(Regimenespecifico re: edr.getRegimenespecificos()) {
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
	private void eliminar(Opciondeterminacion od) {
		if (em.contains(od)) {
			log.debug("Eliminando opcion " + od.getIden());
			em.refresh(od);
			for (Entidaddeterminacionregimen edr : od.getEntidaddeterminacionregimens()) {
				log.debug("Eliminando edr " + od.getIden());
				eliminar(edr);
			}
			
			em.remove(od);
		}
		
	}

	/**
	 * 
	 * @param operacion
	 */
	private void eliminar(Operacion operacion) {
		if (em.contains(operacion)) {
			em.remove(operacion);
		}	
	}

	/**
	 * 
	 * @param or
	 */
	private void eliminar(Operacionrelacion or) {
		if (em.contains(or)) {
			
			eliminar(or.getOperacion());
			
			em.remove(or);
		}
	}

	/**
	 * 
	 * @param pr
	 */
	private void eliminar(Propiedadrelacion pr) {
		if (em.contains(pr)) {
			em.remove(pr);
		}
	}

	/**
	 * 
	 * @param re
	 */
	private void eliminar(Regimenespecifico re) {
		log.debug("Eliminando re " + re.getIden() + " " + re.getNombre() + " " +re.getTexto());
		if (em.contains(re)) {
			em.refresh(re);
			for (Regimenespecifico regimen : re.getRegimenespecificos()) {
				eliminar(regimen);
			}
			em.remove(re);
			log.debug("re eliminado " + re.getIden());
		} else {
			log.warn("re no está en EM " + re.getIden());
		}
	}

	/*
	 * (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.servicios.dal.EliminadorEntidadesRefundidoLocal#eliminar(es.mitc.redes.urbanismoenred.data.rpm.refundido.Relacion)
	 */
	@Override
	public void eliminar(Relacion relacion) {
		if (em.contains(relacion)) {
			em.refresh(relacion);
			for (Vectorrelacion vr : relacion.getVectorrelacions()) {
				eliminar(vr);
			}
			
			for (Propiedadrelacion pr : relacion.getPropiedadrelacions()) {
				eliminar(pr);
			}
			
			for (Operacionrelacion or : relacion.getOperacionrelacions()) {
				eliminar(or);
			}
			
			em.remove(relacion);
		}
	}

	/**
	 * 
	 * @param vr
	 */
	private void eliminar(Vectorrelacion vr) {
		if (em.contains(vr)) {
			em.remove(vr);
		}
	}

}

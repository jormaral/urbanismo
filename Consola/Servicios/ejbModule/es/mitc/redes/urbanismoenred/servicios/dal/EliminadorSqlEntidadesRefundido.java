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
package es.mitc.redes.urbanismoenred.servicios.dal;

import java.io.File;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;

import es.mitc.redes.urbanismoenred.data.rpm.refundido.Documentoentidad;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Determinacion;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Documento;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Documentodeterminacion;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Entidad;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Entidaddeterminacion;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Relacion;

/**
 * 
 * @author Arnaiz Consultores
 *
 */
@Stateless( name = "EliminadorSqlEntidadesRefundido")
public class EliminadorSqlEntidadesRefundido implements
		EliminadorEntidadesRefundidoLocal {
	
	private static final String DIRECTORIO_TRABAJO = "var";
	
	private static final Logger log = Logger.getLogger(EliminadorSqlEntidadesRefundido.class);
	
	private static final String SQL_ELIMINAR_DETERMINACION = "SELECT refundido.eliminardeterminacion(:iden);";
	private static final String SQL_ELIMINAR_DOCUMENTO = "SELECT refundido.eliminardocumento(:iden);";
	private static final String SQL_ELIMINAR_ENTIDAD = "SELECT refundido.eliminarentidad(:iden);";
	private static final String SQL_ELIMINAR_ENTIDADDETERMINACION = "SELECT refundido.eliminarentidaddeterminacion(:iden);";
	private static final String SQL_ELIMINAR_RELACION = "SELECT refundido.eliminarrelacion(:iden);";

	@PersistenceContext(unitName = "rpmv2")
	private EntityManager em;
	
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
	
	/* (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.servicios.dal.EliminadorEntidadesRefundidoLocal#eliminar(es.mitc.redes.urbanismoenred.data.rpm.refundido.Determinacion)
	 */
	@Override
	public void eliminar(Determinacion determinacion) {
		em.flush();
		if (em.contains(determinacion)) {
			em.refresh(determinacion);
			
			for(Documentodeterminacion dd : determinacion.getDocumentodeterminacions()) {
				eliminar(dd.getDocumento());
			}
			
			em.createNativeQuery(SQL_ELIMINAR_DETERMINACION)
				.setParameter("iden", determinacion.getIden())
				.getSingleResult();
			
			em.flush();
		}
	}

	/* (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.servicios.dal.EliminadorEntidadesRefundidoLocal#eliminar(es.mitc.redes.urbanismoenred.data.rpm.refundido.Documento)
	 */
	@Override
	public void eliminar(Documento documento) {
		em.flush();
		if (em.contains(documento)) {
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
			
			em.createNativeQuery(SQL_ELIMINAR_DOCUMENTO)
				.setParameter("iden", documento.getIden())
				.getSingleResult();
			
			em.flush();
		}
	}

	/* (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.servicios.dal.EliminadorEntidadesRefundidoLocal#eliminar(es.mitc.redes.urbanismoenred.data.rpm.refundido.Entidad)
	 */
	@Override
	public void eliminar(Entidad entidad) {
		em.flush();
		if (em.contains(entidad)) {
			em.refresh(entidad);
			
			for (Documentoentidad de : entidad.getDocumentoentidads()) {
				eliminar(de.getDocumento());
			}
			
			em.createNativeQuery(SQL_ELIMINAR_ENTIDAD)
				.setParameter("iden", entidad.getIden())
				.getSingleResult();
			
			em.flush();
		}
	}

	/* (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.servicios.dal.EliminadorEntidadesRefundidoLocal#eliminar(es.mitc.redes.urbanismoenred.data.rpm.refundido.Entidaddeterminacion)
	 */
	@Override
	public void eliminar(Entidaddeterminacion ed) {
		em.flush();
		if (em.contains(ed)) {
			em.createNativeQuery(SQL_ELIMINAR_ENTIDADDETERMINACION)
				.setParameter("iden", ed.getIden())
				.getSingleResult();
			
			em.flush();
		}
	}

	/* (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.servicios.dal.EliminadorEntidadesRefundidoLocal#eliminar(es.mitc.redes.urbanismoenred.data.rpm.refundido.Relacion)
	 */
	@Override
	public void eliminar(Relacion relacion) {
		em.flush();
		if (em.contains(relacion)) {
			em.createNativeQuery(SQL_ELIMINAR_RELACION)
				.setParameter("iden", relacion.getIden())
				.getSingleResult();
			
			em.flush();
		}
	}

}

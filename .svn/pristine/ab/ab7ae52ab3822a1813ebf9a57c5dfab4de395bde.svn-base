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

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;

import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Operacion;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Operacionrelacion;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Relacion;
import es.mitc.redes.urbanismoenred.servicios.dal.EliminadorEntidadesLocal;
import es.mitc.redes.urbanismoenred.servicios.refundido.ContextoRefundido.LOG;

/**
 * Session Bean implementation class GestorOperacionesRelacionBean
 */
@Stateless(name = "GestorOperacionesRelacion")
public class GestorOperacionesRelacionBean implements GestorOperacionesRelacionLocal {
	
	private static final Logger mLog = Logger.getLogger(GestorOperacionesRelacionBean.class);
	
	@PersistenceContext(unitName = "rpmv2")
	private EntityManager em;
	
	@EJB
	private EliminadorEntidadesLocal eliminadorEntidades;

    /**
     * Default constructor. 
     */
    public GestorOperacionesRelacionBean() {
        
    }

    /**
	 * Para que una relacion se convierta en vigente, sólo debe eliminarse
	 * de la tabla OperacionRelacion
	 * 
	 * @param iRelO
     * @throws ExcepcionRefundido 
	 */
	private void adicion(Relacion iRelO) throws ExcepcionRefundido{
        try{
        	mLog.debug("Eliminando operacionrelacion de la relacion: "+ iRelO.getIden());
        	for(Operacionrelacion or : iRelO.getOperacionrelacions()) {
        		if (em.contains(or)) {
        			if (!em.contains(iRelO)) {
        				em.refresh(or);
        			}
        			em.remove(or);
        		} else {
        			mLog.debug("La operacionrelacion: "+ or.getIden() + " ya esta eliminada.");
        		}
        	}
        } catch (Exception e){
            throw new ExcepcionRefundido("Fallo en la operación adicion. " + e.getMessage(),  23402);
        }
    }
	
	/*
	 * (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.servicios.refundido.GestorOperacionesRelacionLocal#ejecutar(es.mitc.redes.urbanismoenred.servicios.refundido.ContextoRefundido)
	 */
	@Override
	public void ejecutar(ContextoRefundido contexto) throws ExcepcionRefundido {
		
		Operacionrelacion operacionRelacion = (Operacionrelacion)contexto.getParametro(OPERACION_RELACION);
		try {
			if (operacionRelacion != null) {     
				String s="(" + operacionRelacion.getOperacion().getIden() + ") ";
	
		        contexto.log(LOG.INFO, "         Relación operada: iden=" + operacionRelacion.getRelacion().getIden());
		        contexto.log(LOG.INFO, "         Tipo de operación: " + operacionRelacion.getIdtipooperacionrel());
	
		        switch (operacionRelacion.getIdtipooperacionrel()) {
		            case 1:
		                // Eliminación
		            	contexto.log(LOG.INFO, s + "Relación iden=" + operacionRelacion.getRelacion().getIden() 
		                		+ " eliminada por el plan [" 
		                		+ operacionRelacion.getOperacion().getTramite().getPlan().getCodigo() + "]");
			            	Relacion relacion = operacionRelacion.getRelacion();
		        			eliminadorEntidades.eliminar(relacion, null);
		                break;
		            case 2:
		                // Adición
		            	contexto.log(LOG.INFO, s + "Relación iden=" + operacionRelacion.getRelacion().getIden() 
		                		+ " adicionada por el plan [" 
		                		+ operacionRelacion.getOperacion().getTramite().getPlan().getCodigo() + "]");
		                adicion(operacionRelacion.getRelacion());
		                break;
		            default:
		               contexto.log(LOG.AVISO, "*** " + s + "         No se opera. La operación iden=" 
		            		   + operacionRelacion.getIdtipooperacionrel() + " no está implementada en esta versión.");
		               break;
		        }
		        
		        // Se elimina la operación antes de ejecutarla, para evitar el "aviso" de que se ha eliminado
		        //  una operación que aún no ha sido ejecutada.
		        // operacionrelacion probablemente haya sido borrada en cascada, por lo que para 
		        // borrar la operación es necesario asegurarnos que está en el contexto del EM.
		        Operacion operacion = operacionRelacion.getOperacion();
		        eliminadorEntidades.eliminar(operacion, null);
		        em.flush();
			} else {
				throw new ExcepcionRefundido("No se ha definido operación", 40001);
			}
		} catch(ExcepcionRefundido er) {
			throw er;
		} catch (Exception e) {
			throw new ExcepcionRefundido("Error inesperado: ", 40002);
		}
	}
}

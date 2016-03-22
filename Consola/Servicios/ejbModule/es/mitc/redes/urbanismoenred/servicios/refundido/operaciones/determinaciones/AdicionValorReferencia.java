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
package es.mitc.redes.urbanismoenred.servicios.refundido.operaciones.determinaciones;

import java.util.HashMap;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import es.mitc.redes.urbanismoenred.data.rpm.refundido.Determinacion;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Entidaddeterminacionregimen;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Opciondeterminacion;
import es.mitc.redes.urbanismoenred.servicios.refundido.ContextoRefundido;
import es.mitc.redes.urbanismoenred.servicios.refundido.ContextoRefundido.LOG;
import es.mitc.redes.urbanismoenred.servicios.refundido.CopiadorRefundidoLocal;
import es.mitc.redes.urbanismoenred.servicios.refundido.ExcepcionRefundido;

/**
 * Copiar los valores de referencia de la determinación operadora a la 
 * determinación operada, y reasignar los punteros de 
 * EntidadDeterminacionRegimen que apuntan a las opciones de la operadora para
 * que apunten a las nuevas opciones de la operada.
 * 
 * @author Arnaiz Consultores
 *
 */
@Stateless(name = "refundido.operacion.determinaciones.adicionValorReferencia")
public class AdicionValorReferencia implements EjecutorLocal {
	
	@PersistenceContext(unitName = "rpmv2")
	private EntityManager em;
	
	@EJB
	private CopiadorRefundidoLocal referencias;
	

	/* (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.servicios.refundido.operaciones.determinaciones.EjecutorLocal#ejecutar(int, int, es.mitc.redes.urbanismoenred.servicios.refundido.ContextoRefundido)
	 */
	@Override
	public void ejecutar(int idOperada, int idOperadora,
			ContextoRefundido contexto) throws ExcepcionRefundido {
    	
    	es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Determinacion operadoraPlaneamiento = em.find(es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Determinacion.class, idOperadora);
    	es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Determinacion operadaPlaneamiento = em.find(es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Determinacion.class, idOperada);
    	
    	contexto.logTraducido(LOG.INFO, "refundido.operacion.determinacion.adicionValorReferencia.mensaje", 
    			operadoraPlaneamiento.getCodigo(), 
    			operadoraPlaneamiento.getTramite().getPlan().getCodigo(),
    			operadaPlaneamiento.getCodigo(), 
    			operadaPlaneamiento.getTramite().getPlan().getCodigo());
    	
    	Determinacion operada = referencias.getReferencia((int)contexto.getParametro(ContextoRefundido.ID_PROCESO), Determinacion.class, idOperada);	
    	Determinacion operadora = referencias.getReferencia((int)contexto.getParametro(ContextoRefundido.ID_PROCESO), Determinacion.class, idOperadora);
    	
    	if (operada != null && operadora != null) {
    		if (!operadora.isBsuspendida()) {
    			Map<Integer, Opciondeterminacion> valoresReferenciaOperada = new HashMap<Integer, Opciondeterminacion>();
            	
            	// Para verificar de forma cómoda si un valor de referencia ya está aplicado
            	// para la determinación operada, guardamos todos sus valores de referencia
            	// en un map.
            	for(Opciondeterminacion odr : operada.getOpciondeterminacionsForIddeterminacion()) {
            		valoresReferenciaOperada.put(odr.getDeterminacionByIddeterminacionvalorref().getIden(), odr);
            	}
            	
            	Opciondeterminacion odNueva;
            	for (Opciondeterminacion od : operadora.getOpciondeterminacionsForIddeterminacion()) {
            		
            		// Comprueba si ya existe la Opcion para la determinación operada
        			if (valoresReferenciaOperada.containsKey(od.getDeterminacionByIddeterminacionvalorref().getIden())) {
        				odNueva = valoresReferenciaOperada.get(od.getDeterminacionByIddeterminacionvalorref().getIden());
        			} else {
        				odNueva = new Opciondeterminacion();
                        odNueva.setDeterminacionByIddeterminacion(operada);
                        odNueva.setDeterminacionByIddeterminacionvalorref(od.getDeterminacionByIddeterminacionvalorref());
                        em.persist(odNueva);
                        em.flush();
        			}
            		
            		// Ahora se copian los EntidadDeterminacionRegimen de la operadora
            		// y se trasladan a la operada
            		Entidaddeterminacionregimen nuevaEdr;
            		for (Entidaddeterminacionregimen edr : od.getEntidaddeterminacionregimens()) {
            			nuevaEdr = new Entidaddeterminacionregimen();
            			
            			nuevaEdr.setCasoentidaddeterminacionByIdcaso(edr.getCasoentidaddeterminacionByIdcaso());
            			nuevaEdr.setCasoentidaddeterminacionByIdcasoaplicacion(edr.getCasoentidaddeterminacionByIdcasoaplicacion()); 			
            			nuevaEdr.setDeterminacion(operada);
            			nuevaEdr.setOpciondeterminacion(odNueva);
            			nuevaEdr.setValor(edr.getValor());
            			nuevaEdr.setSuperposicion(edr.getSuperposicion());
            			em.persist(nuevaEdr);
            		}
            		
            		// Mueve la determinación valor de referencia a la carpeta
            		// de entidades aportadas si no tiene padre y es
            		// la determinación operadora lo sustituye por la operada.
            		if (od.getDeterminacionByIddeterminacionvalorref().getDeterminacionByIdpadre() != null && 
                    		operadora.getIden() == od.getDeterminacionByIddeterminacionvalorref().getDeterminacionByIdpadre().getIden()) {
            			int orden = 1;
            			for(Determinacion hija : operada.getDeterminacionsForIdpadre()) {
            				if (hija.getOrden() > orden) {
            					orden = hija.getOrden();
            				}
            			}
            			
            			od.getDeterminacionByIddeterminacionvalorref().setDeterminacionByIdpadre(operada);
            			od.getDeterminacionByIddeterminacionvalorref().setOrden(++orden);
                    } 
            	}
    		} else {
    			contexto.logTraducido(LOG.AVISO,"refundido.operacion.determinacion.aviso.operadoraSuspendida");
    		}
    	} else {
    		
    		if (operada == null) {
    			contexto.logTraducido(LOG.AVISO, "refundido.operacion.determinacion.aviso.noOperada");
    		}
    		
    		if (operadora == null) {
    			contexto.logTraducido(LOG.AVISO, "refundido.operacion.determinacion.aviso.noOperadora");
    		}
    	}
	}

}

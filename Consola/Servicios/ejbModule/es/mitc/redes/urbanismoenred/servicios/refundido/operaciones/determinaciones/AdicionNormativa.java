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
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import es.mitc.redes.urbanismoenred.data.rpm.refundido.Determinacion;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Documentodeterminacion;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Propiedadrelacion;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Relacion;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Tramite;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Vectorrelacion;
import es.mitc.redes.urbanismoenred.servicios.refundido.ContextoRefundido;
import es.mitc.redes.urbanismoenred.servicios.refundido.ContextoRefundido.LOG;
import es.mitc.redes.urbanismoenred.servicios.refundido.CopiadorRefundidoLocal;
import es.mitc.redes.urbanismoenred.servicios.refundido.ExcepcionRefundido;

/**
 * Consiste en copiar las propiedades y vectores de Regulación de
 * la determinación determinacionOperadora a la determinación determinacionOperada.
 * Sólo se tienen en cuenta las relaciones que no están apuntadas
 * desde OperacionRelacion con el tipo de operación "Adición", ya
 * que estas relaciones no son vigentes, sino que están pendientes de
 * ser ejecutadas como operaciones.
 * 
 * También se copian los documentos.
 * 
 * @author Arnaiz Consultores
 *
 */
@Stateless(name = "refundido.operacion.determinaciones.adicionNormativa")
public class AdicionNormativa implements EjecutorLocal {
	
	private static final int ID_DEF_VECTOR_TEXTO_REGULACION_DETERMINACION = 40;
	private static final int ID_DEF_VECTOR_HERENCIA_CLAVE = 41;
	
	@PersistenceContext(unitName = "rpmv2")
	private EntityManager em;
	
	@EJB
	private CopiadorRefundidoLocal referencias;

	/* (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.servicios.refundido.operaciones.determinaciones.EjecutorLocal#ejecutar(int, int, es.mitc.redes.urbanismoenred.servicios.refundido.ContextoRefundido)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void ejecutar(int idDeterminacionOperada, int idDeterminacionOperadora,
			ContextoRefundido contexto) throws ExcepcionRefundido {
		
		es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Determinacion determinacionOperadoraPlaneamiento = em.find(es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Determinacion.class, idDeterminacionOperadora);
		es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Determinacion determinacionOperadaPlaneamiento = em.find(es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Determinacion.class, idDeterminacionOperada);
    	
		contexto.logTraducido(LOG.INFO, "refundido.operacion.determinacion.adicionNormativa.mensaje", 
				determinacionOperadoraPlaneamiento.getCodigo(),
				determinacionOperadoraPlaneamiento.getTramite().getPlan().getCodigo(),
				determinacionOperadaPlaneamiento.getCodigo(),
				determinacionOperadaPlaneamiento.getTramite().getPlan().getCodigo());
		
    	Determinacion determinacionOperada = referencias.getReferencia((int)contexto.getParametro(ContextoRefundido.ID_PROCESO), Determinacion.class, idDeterminacionOperada);	
    	Determinacion determinacionOperadora = referencias.getReferencia((int)contexto.getParametro(ContextoRefundido.ID_PROCESO), Determinacion.class, idDeterminacionOperadora);
    	
    	if (determinacionOperada != null && determinacionOperadora != null) {
    		if (!determinacionOperadora.isBsuspendida()) {
    			// Se copian las regulaciones específicas
        		// En el original se exceptuaban aquellas que eran sujeto de una
        		// operación de adición. Pero en el nuevo algoritmo en principio
        		// no tendría sentido.
        		List<Relacion> relaciones = em.createNamedQuery("refundido.Relacion.obtenerRegulacionesEspecificas")
        				.setParameter("idDeterminacion", determinacionOperadora.getIden())
        				.getResultList();
        		Map<Integer, Integer> correspondenciaRelaciones = new HashMap<Integer, Integer>();
        		
        		for (Relacion regulacion : relaciones) {
        			Relacion nuevaRelacion = new Relacion();
        			nuevaRelacion.setIddefrelacion(regulacion.getIddefrelacion());
        			nuevaRelacion.setTramiteByIdtramitecreador((Tramite) contexto.getParametro(ContextoRefundido.TRAMITE_REFUNDIDO));
        			em.persist(nuevaRelacion);
        			em.flush();
        			correspondenciaRelaciones.put(regulacion.getIden(), nuevaRelacion.getIden());
        		}
        		
        		for (Relacion regulacion : relaciones) {
        			Relacion nuevaRelacion = em.find(Relacion.class, correspondenciaRelaciones.get(regulacion.getIden()));
        			
        			for(Propiedadrelacion propiedad : regulacion.getPropiedadrelacions()) {
        				Propiedadrelacion nuevaPropiedad = new Propiedadrelacion();
        				nuevaPropiedad.setIddefpropiedad(propiedad.getIddefpropiedad());
        				nuevaPropiedad.setValor(propiedad.getValor());
        				nuevaPropiedad.setRelacion(nuevaRelacion);
        				em.persist(nuevaPropiedad);
        			}
        			
        			for (Vectorrelacion vector : regulacion.getVectorrelacions()) {
        				Vectorrelacion nuevoVector = new Vectorrelacion();
        				nuevoVector.setIddefvector(vector.getIddefvector());
        				nuevoVector.setRelacion(nuevaRelacion);
        				
        				// Estoy asumiendo que todos los datos se guardan utilizando
        				// la propia consola. Si no es así esto puede fallar, debido
        				// a que en el diccionario hay múltiples valores posibles 
        				// para este campo. Pero en GestorFipBean sólo se utiliza este.
        				if (ID_DEF_VECTOR_TEXTO_REGULACION_DETERMINACION == vector.getIddefvector()) {
        					nuevoVector.setValor(determinacionOperada.getIden());
        				} else if ( ID_DEF_VECTOR_HERENCIA_CLAVE == vector.getIddefvector()){
        					// Si no debe ser el vector que apunta a la regulación padre
        					if (vector.getValor() != 0) {
        						nuevoVector.setValor(correspondenciaRelaciones.get(vector.getValor()));
        					} else {
        						nuevoVector.setValor(vector.getValor());
        					}
        					
        				}
        				
        				em.persist(nuevoVector);
        			}
        		}
        		
        		// Se asignan los documentos de la determinación operadora a la
        		// determinación operada.
        		Documentodeterminacion nuevoDocumento;
        		for (Documentodeterminacion documento : determinacionOperadora.getDocumentodeterminacions()) {
        			nuevoDocumento = new Documentodeterminacion();
        			nuevoDocumento.setDeterminacion(determinacionOperada);
        			nuevoDocumento.setDocumento(documento.getDocumento());
        			
        			em.persist(nuevoDocumento);
        		}
    		} else {
    			contexto.logTraducido(LOG.AVISO,"refundido.operacion.determinacion.aviso.operadoraSuspendida");
    		}
    	} else {
    		
    		if (determinacionOperada == null) {
    			contexto.logTraducido(LOG.AVISO, "refundido.operacion.determinacion.aviso.noOperada");
    		}
    		
    		if (determinacionOperadora == null) {
    			contexto.logTraducido(LOG.AVISO, "refundido.operacion.determinacion.aviso.noOperadora");
    		} 		
    	}
	}
}

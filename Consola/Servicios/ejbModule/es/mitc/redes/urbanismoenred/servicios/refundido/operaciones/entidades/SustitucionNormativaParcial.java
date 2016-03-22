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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import es.mitc.redes.urbanismoenred.data.rpm.refundido.Documentoentidad;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Entidad;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Entidaddeterminacion;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Propiedadrelacion;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Relacion;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Vectorrelacion;
import es.mitc.redes.urbanismoenred.servicios.dal.EliminadorEntidadesRefundidoLocal;
import es.mitc.redes.urbanismoenred.servicios.refundido.ClsDatos;
import es.mitc.redes.urbanismoenred.servicios.refundido.ContextoRefundido;
import es.mitc.redes.urbanismoenred.servicios.refundido.ContextoRefundido.LOG;
import es.mitc.redes.urbanismoenred.servicios.refundido.ExcepcionRefundido;

/**
 * 
 * @author Arnaiz Consultores
 *
 */
@Stateless( name = "refundido.operacion.entidades.sustitucionNormativaParcial")
public class SustitucionNormativaParcial extends CopiarEntidadDeterminacion implements EjecutorLocal {
	
	private static final List<Integer> CARACTERES = Arrays.asList(ClsDatos.ID_CARACTER_ACTODEEJECUCION, ClsDatos.ID_CARACTER_ENUNCIADO, 
			ClsDatos.ID_CARACTER_ENUNCIADOCOMPLEMENTARIO, ClsDatos.ID_CARACTER_GRUPODEACTOS, ClsDatos.ID_CARACTER_GRUPODEUSOS, 
			ClsDatos.ID_CARACTER_NORMAGENERALGRAFICA, ClsDatos.ID_CARACTER_NORMAGENERALLITERAL, ClsDatos.ID_CARACTER_OPERADORA, 
			ClsDatos.ID_CARACTER_REGULACION, ClsDatos.ID_CARACTER_UNIDAD, ClsDatos.ID_CARACTER_USO, ClsDatos.ID_CARACTER_VALORREFERENCIA, ClsDatos.ID_CARACTER_VIRTUAL);
	
	@EJB ( beanName = "EliminadorEntidadesRefundido" )
	private EliminadorEntidadesRefundidoLocal eliminadorEntidades;
	
	/**
	 * 
	 * @param propiedad
	 * @param copia
	 */
	private void copiar(es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Propiedadrelacion propiedad, Relacion relacion) {
		Propiedadrelacion copia = new Propiedadrelacion();
		
		copia.setIddefpropiedad(propiedad.getIddefpropiedad());
		copia.setValor(propiedad.getValor());
		copia.setRelacion(relacion);
		
		em.persist(copia);
	}

	/**
	 * 
	 * @param relacion
	 * @param operada
	 * @param idOperadora 
	 * @param contexto 
	 * @throws ExcepcionRefundido 
	 */
	private void copiar(
			es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Relacion relacion,
			Entidad operada, int idOperadora, ContextoRefundido contexto) throws ExcepcionRefundido {
		Relacion copia = new Relacion();
		
		copia.setIddefrelacion(relacion.getIddefrelacion());
		copia.setTramiteByIdtramitecreador(operada.getTramite());
		
		em.persist(copia);
		
		for (es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Propiedadrelacion propiedad : relacion.getPropiedadrelacions()) {
			copiar(propiedad, copia);
		}
		
		for (es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Vectorrelacion vector : relacion.getVectorrelacions()) {
			copiar(vector, operada, idOperadora, copia, contexto);
		}
	}

	/**
	 * 
	 * @param vector
	 * @param operada
	 * @param idOperadora
	 * @param copia
	 * @throws ExcepcionRefundido 
	 */
	private void copiar(es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Vectorrelacion vector, Entidad operada,
			int idOperadora, Relacion relacion, ContextoRefundido contexto) throws ExcepcionRefundido {
		Vectorrelacion copia = new Vectorrelacion();
		
		copia.setIddefvector(vector.getIddefvector());
		
		copia.setRelacion(relacion);
		
		if (vector.getValor() == idOperadora) {
			copia.setValor(operada.getIden());
		} else {
			int idrefundido = referencias.getReferencia((int)contexto.getParametro(ContextoRefundido.ID_PROCESO), Entidad.class.getSimpleName(), idOperadora);
			
			if (idrefundido != 0) {
				copia.setValor(idrefundido);
			} else {
				es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidad referencia = em.find(es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidad.class, vector.getValor());
				gestorAportaciones.aportar(referencia, null, null, contexto);
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.servicios.refundido.operaciones.entidades.EjecutorLocal#ejecutar(int, int, es.mitc.redes.urbanismoenred.servicios.refundido.ContextoRefundido)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void ejecutar(int idOperada, int idOperadora,
			ContextoRefundido contexto) throws ExcepcionRefundido {
		es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidad operadoraPlaneamiento = em.find(es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidad.class, idOperadora);
		es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidad operadaPlaneamiento = em.find(es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidad.class, idOperada);
		
		contexto.logTraducido(LOG.INFO, "refundido.operacion.entidad.sustitucionNormativaParcial.mensaje", 
				operadoraPlaneamiento.getCodigo(), 
				operadoraPlaneamiento.getTramite().getPlan().getCodigo(), 
				operadaPlaneamiento.getCodigo(),
				operadaPlaneamiento.getTramite().getPlan().getCodigo());
		
		Entidad operada = gestorAportaciones.aportar(operadaPlaneamiento, null, null, contexto);
		Entidad operadora = gestorAportaciones.aportar(operadoraPlaneamiento, null, null, contexto);
		
		if (operada != null && operadora != null) {
			if (!operadora.isBsuspendida()) {
				List<Integer> determinacionesOperadora = new ArrayList<Integer>();
				for (Entidaddeterminacion edOperadora : operadora.getEntidaddeterminacions()) {
					determinacionesOperadora.add(edOperadora.getDeterminacion().getIden());
				}
				
				for (Entidaddeterminacion ed : operada.getEntidaddeterminacions()) {
					if (ed.getDeterminacion().getIdcaracter() != ClsDatos.ID_CARACTER_GRUPODEENTIDADES && determinacionesOperadora.contains(ed.getDeterminacion().getIden())) {
						eliminadorEntidades.eliminar(ed);
					}
				}
				
				contexto.putParametro(LISTA_CARACTERES, CARACTERES);
				contexto.putParametro(ES_SUPERPOSICION, false);
				
				super.ejecutar(operada, operadora, contexto);
				
				List<Relacion> relaciones = em.createNamedQuery("refundido.Relacion.buscarRelacionesEntreEntidades")
						.setParameter("idEntidad", operada.getIden())
						.getResultList();
				
				for (Relacion relacion : relaciones) {
					eliminadorEntidades.eliminar(relacion);
				}
				
				List<es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Relacion> relacionesOperadora =  em.createNamedQuery("refundido.Relacion.buscarRelacionesEntreEntidades")
						.setParameter("idEntidad", idOperadora)
						.getResultList();
				
				for(es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Relacion relacion : relacionesOperadora) {
					copiar(relacion, operada, idOperadora, contexto);
				}
				
				// Se borran los documentos de la entidad operada, y se reasignan los de la operadora
	            //  a la operada.
				
				for (Documentoentidad de : operada.getDocumentoentidads()) {
					em.remove(de);
				}
				
				Documentoentidad nuevode;
				for (Documentoentidad de : operadora.getDocumentoentidads()) {
					nuevode = new Documentoentidad();
					nuevode.setDocumento(de.getDocumento());
					nuevode.setEntidad(operada);
					em.persist(nuevode);
				}
				
				operada.setNombre(operadora.getNombre());
				operada.setClave(operadora.getClave());
				
				if (operadora.getEntidadByIdentidadbase() != null) {
					operada.setEntidadByIdentidadbase(operadora.getEntidadByIdentidadbase());
				}
			} else {
				contexto.logTraducido(LOG.AVISO,"refundido.operacion.entidad.aviso.operadoraSuspendida");
			}
		} else {
			if (operada == null) {
    			contexto.logTraducido(LOG.AVISO,"refundido.operacion.entidad.aviso.noOperada");
    		}
    		
    		if (operadora == null) {
    			contexto.logTraducido(LOG.AVISO, "refundido.operacion.entidad.aviso.noOperadora");
    		}
		}
	}
}

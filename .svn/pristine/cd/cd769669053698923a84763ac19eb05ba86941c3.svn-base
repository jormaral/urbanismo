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

import java.util.Arrays;
import java.util.List;

import javax.ejb.Stateless;

import es.mitc.redes.urbanismoenred.data.rpm.refundido.Entidad;
import es.mitc.redes.urbanismoenred.servicios.refundido.ClsDatos;
import es.mitc.redes.urbanismoenred.servicios.refundido.ContextoRefundido;
import es.mitc.redes.urbanismoenred.servicios.refundido.ExcepcionRefundido;
import es.mitc.redes.urbanismoenred.servicios.refundido.ContextoRefundido.LOG;

/**
 * No se tiene en cuenta el carácter de la determinación
 * Se añaden al recinto operado, las determinaciones del operador que no
 * le sean comunes.
 * 
 * @author Arnaiz Consultores
 *
 */
@Stateless(name = "refundido.operacion.entidades.acumulacionCompleta")
public class AcumulacionCompleta extends CopiarEntidadDeterminacion implements EjecutorLocal {

	private static final List<Integer> CARACTERES = Arrays.asList(ClsDatos.ID_CARACTER_ACTODEEJECUCION, ClsDatos.ID_CARACTER_ENUNCIADO, 
			ClsDatos.ID_CARACTER_ENUNCIADOCOMPLEMENTARIO, ClsDatos.ID_CARACTER_GRUPODEACTOS, ClsDatos.ID_CARACTER_GRUPODEUSOS, 
			ClsDatos.ID_CARACTER_NORMAGENERALGRAFICA, ClsDatos.ID_CARACTER_NORMAGENERALLITERAL, ClsDatos.ID_CARACTER_OPERADORA, 
			ClsDatos.ID_CARACTER_REGULACION, ClsDatos.ID_CARACTER_UNIDAD, ClsDatos.ID_CARACTER_USO, ClsDatos.ID_CARACTER_VALORREFERENCIA, ClsDatos.ID_CARACTER_VIRTUAL);

	/* (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.servicios.refundido.operaciones.entidades.EjecutorLocal#ejecutar(int, int, es.mitc.redes.urbanismoenred.servicios.refundido.ContextoRefundido)
	 */
	@Override
	public void ejecutar(int idOperada, int idOperadora,
			ContextoRefundido contexto) throws ExcepcionRefundido {
		es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidad operadoraPlaneamiento = em.find(es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidad.class, idOperadora);
		es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidad operadaPlaneamiento = em.find(es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidad.class, idOperada);
		
		contexto.logTraducido(LOG.INFO, "refundido.operacion.entidad.acumulacionCompleta.mensaje", 
				operadoraPlaneamiento.getCodigo(), 
				operadoraPlaneamiento.getTramite().getPlan().getCodigo(), 
				operadaPlaneamiento.getCodigo(),
				operadaPlaneamiento.getTramite().getPlan().getCodigo());
		
		Entidad operada = gestorAportaciones.aportar(operadaPlaneamiento, null, null, contexto);
		Entidad operadora = gestorAportaciones.aportar(operadoraPlaneamiento, null, null, contexto);
		
		if (operada != null && operadora != null) {
			if (!operadora.isBsuspendida()) {
				contexto.putParametro(ES_SUPERPOSICION, false);
				contexto.putParametro(LISTA_CARACTERES, CARACTERES);
				super.ejecutar(operada, operadora, contexto);
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

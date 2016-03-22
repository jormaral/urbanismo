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
 * 
 * Se tiene en cuenta el carácter de la determinación. Sólo se contemplan
 * las de carácter "Acto de Ejecución" y "Grupo de Actos"
 * Se añaden al recinto operado, las determinaciones del operador que no
 * le sean comunes.
 * Se llama a la función CopiarRecintoDeterminacion() con el argumento False de superposición
 * 
 * Session Bean implementation class AcumulacionActosEntidades
 * 
 * @author Arnaiz Consultores
 */
@Stateless(name = "refundido.operacion.entidades.acumulacionActos")
public class AcumulacionActos extends CopiarEntidadDeterminacion implements EjecutorLocal {
	
	private static List<Integer> CARACTERES = Arrays.asList(ClsDatos.ID_CARACTER_ACTODEEJECUCION, ClsDatos.ID_CARACTER_GRUPODEACTOS);

    /**
     * Default constructor. 
     */
    public AcumulacionActos() {
    }

    /*
     * (non-Javadoc)
     * @see es.mitc.redes.urbanismoenred.servicios.refundido.operaciones.EjecutorEntidadesLocal#ejecutar(int, int, es.mitc.redes.urbanismoenred.servicios.refundido.ContextoRefundido)
     */
	@Override
	public void ejecutar(int idOperada, int idOperadora,
			ContextoRefundido contexto) throws ExcepcionRefundido {
		es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidad operadoraPlaneamiento = em.find(es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidad.class, idOperadora);
		es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidad operadaPlaneamiento = em.find(es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidad.class, idOperada);
		
		contexto.logTraducido(LOG.INFO, "refundido.operacion.entidad.acumulacionActos.mensaje", 
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

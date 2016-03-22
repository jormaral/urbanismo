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
package es.mitc.redes.urbanismoenred.consola.util.helpers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import es.mitc.redes.urbanismoenred.data.rpm.diccionario.Instrumentoplan;
import es.mitc.redes.urbanismoenred.data.rpm.diccionario.Tipooperacionentidad;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Operacionentidad;
import es.mitc.redes.urbanismoenred.servicios.planeamiento.ServicioDiccionariosLocal;
import es.mitc.redes.urbanismoenred.servicios.planeamiento.ServicioPlaneamientoLocal;

/**
 * @author Arnaiz Consultores
 *
 */
@Stateless(name="EntidadOperaA")
public class EntidadOperaAHelper implements IJsonHelper {
	
	@EJB
	private ServicioPlaneamientoLocal servicioPlaneamiento;
	@EJB
	private ServicioDiccionariosLocal servicioDiccionario;

	/* (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.consola.util.IJsonHelper#marshal(es.mitc.redes.urbanismoenred.servicios.planeamiento.ServicioPlaneamientoLocal, es.mitc.redes.urbanismoenred.servicios.planeamiento.ServicioDiccionariosLocal, java.lang.Object)
	 */
	@Override
	public Map<String, Object> marshal(Object id, String idioma) {
		Map<String, Object> data = new HashMap<String, Object>();
		List<Map<String, Object>> lista = new ArrayList<Map<String, Object>>();
		Map<String, Object> elemento;
		String nombreInstrumento = "-";
		Instrumentoplan instrumento;
		for (Operacionentidad oe : servicioPlaneamiento.getOperacionesPorEntidadOperada((Integer)id)) {
			elemento = new HashMap<String, Object>();
			elemento.put("iden", oe.getIden());
			
			elemento.put("entidad", (oe.getEntidadByIdentidad().getClave() != null? oe.getEntidadByIdentidad().getClave() +" " : "" )
					+ (oe.getEntidadByIdentidad().getNombre() != null? oe.getEntidadByIdentidad().getNombre():""));
			elemento.put("tipo", servicioDiccionario.getTraduccion(Tipooperacionentidad.class, oe.getIdtipooperacionent(), idioma));
			elemento.put("codigo", oe.getEntidadByIdentidad().getCodigo());
			instrumento = servicioDiccionario.getInstrumento(oe.getEntidadByIdentidad().getTramite().getPlan().getIden());
		    
		    if (instrumento != null)
		    	nombreInstrumento = servicioDiccionario.getTraduccion(Instrumentoplan.class, instrumento.getIden(), idioma);
		    else
		    	nombreInstrumento = "-";
		    
		    elemento.put("instrumento", nombreInstrumento);
			elemento.put("plan", oe.getEntidadByIdentidad().getTramite().getPlan().getNombre());
			elemento.put("regimen", oe.getOperacion().getTexto());
			lista.add(elemento);
		}
		
		data.put("data", lista);
		data.put("pages", 1);
		data.put("total", lista.size());
		return data;
	}

}

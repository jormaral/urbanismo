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

import es.mitc.redes.urbanismoenred.servicios.planeamiento.PropiedadesAdscripcion;
import es.mitc.redes.urbanismoenred.servicios.planeamiento.ServicioPlaneamientoLocal;

/**
 * @author Arnaiz Consultores
 *
 */
@Stateless(name="PropiedadesAdscripcion")
public class PropiedadesAdscripcionHelper implements IJsonHelper {
	
	@EJB
	private ServicioPlaneamientoLocal servicioPlaneamiento;

	/* (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.consola.util.IJsonHelper#marshal(es.mitc.redes.urbanismoenred.servicios.planeamiento.ServicioPlaneamientoLocal, es.mitc.redes.urbanismoenred.servicios.planeamiento.ServicioDiccionariosLocal, java.lang.Object)
	 */
	@Override
	public Map<String, Object> marshal(Object id, String idioma) {
		Map<String, Object> data = new HashMap<String, Object>();
		List<Map<String, Object>> lista = new ArrayList<Map<String, Object>>();
		Map<String, Object> elemento;
		StringBuffer nombre = new StringBuffer();
		for (PropiedadesAdscripcion propiedades : servicioPlaneamiento.getPropiedadesAdscripcionEntidad((Integer)id)) {
			elemento = new HashMap<String, Object>();
			elemento.put("iden", propiedades.getIden());
			nombre.delete(0, nombre.length());
			if (propiedades.getOrigen() != null) {
				if (propiedades.getOrigen().getClave() != null) {
					nombre.append(propiedades.getOrigen().getClave());
					nombre.append(" ");
				}
				nombre.append(propiedades.getOrigen().getNombre());
			}
			elemento.put("origen", nombre.toString());
			
			nombre.delete(0, nombre.length());
			if (propiedades.getDestino() != null) {
				if (propiedades.getDestino().getClave() != null) {
					nombre.append(propiedades.getDestino().getClave());
					nombre.append(" ");
				}
				nombre.append(propiedades.getDestino().getNombre());
			}
			elemento.put("destino", nombre.toString());
			elemento.put("cuantia", propiedades.getCuantia());
			elemento.put("texto", propiedades.getTexto());
			elemento.put("tipo",propiedades.getTipo() != null? propiedades.getTipo().getNombre():"-");
			elemento.put("unidad",propiedades.getUnidad() != null? propiedades.getUnidad().getNombre():"-");
	        lista.add(elemento);
		}
		
		data.put("data", lista);
		data.put("pages", 1);
		data.put("total", lista.size());
		return data;
	}

}

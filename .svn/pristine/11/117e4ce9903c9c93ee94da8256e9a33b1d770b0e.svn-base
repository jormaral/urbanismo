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

import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Opciondeterminacion;
import es.mitc.redes.urbanismoenred.servicios.planeamiento.ServicioDiccionariosLocal;
import es.mitc.redes.urbanismoenred.servicios.planeamiento.ServicioPlaneamientoLocal;

/**
 * @author Arnaiz Consultores
 *
 */
@Stateless(name="valoresReferencia")
public class ValoresReferenciaHelper extends AbstractDeterminacionHelper implements IJsonHelper {
	
	@EJB
	private ServicioPlaneamientoLocal servicioPlaneamiento;
	@EJB
	private ServicioDiccionariosLocal servicioDiccionario;

	/*
	 * (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.consola.util.IJsonHelper#marshal(es.mitc.redes.urbanismoenred.servicios.planeamiento.ServicioPlaneamientoLocal, es.mitc.redes.urbanismoenred.servicios.planeamiento.ServicioDiccionariosLocal, java.lang.Object)
	 */
	@Override
	public Map<String, Object> marshal(Object id, String idioma) {
		Opciondeterminacion[] opciones = servicioPlaneamiento.getOpcionesDeterminacion(Integer.parseInt(id.toString()));
		Map<String, Object> data = new HashMap<String, Object>();
		List<Object> listaDets = new ArrayList<Object>();
		
		for (Opciondeterminacion od : opciones) {
			listaDets.add(procesarDeterminacion(od.getDeterminacionByIddeterminacionvalorref(),
					servicioPlaneamiento.getUnidad(od.getDeterminacionByIddeterminacionvalorref().getIden())
					, servicioDiccionario, idioma));
		}
		data.put("page", "1");
		data.put("total", "1");
		data.put("data", listaDets);
		return data;
	}

}

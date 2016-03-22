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

import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Determinaciongrupoentidad;
import es.mitc.redes.urbanismoenred.servicios.planeamiento.ServicioDiccionariosLocal;
import es.mitc.redes.urbanismoenred.servicios.planeamiento.ServicioPlaneamientoLocal;

/**
 * @author Arnaiz Consultores
 *
 */
@Stateless(name="GruposAplicacion")
public class GruposAplicacionHelper extends AbstractDeterminacionHelper implements IJsonHelper {
	
	@EJB
	private ServicioPlaneamientoLocal servicioPlaneamiento;
	@EJB
	private ServicioDiccionariosLocal servicioDiccionario;

	/*
	 * (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.servicios.planeamiento.helpers.IJsonHelper#marshal(java.lang.Object)
	 */
	@Override
	public Map<String, Object> marshal(Object id, String idioma) {
		Map<String, Object> data = new HashMap<String, Object>();
		List<Object> lista = new ArrayList<Object>();
		Determinaciongrupoentidad[] gruposAplicacion = servicioPlaneamiento.getGruposAplicacion((Integer)id);
		List<Integer> determinaciones = new ArrayList<Integer>();
		Map<String,Object> detJson;
		for (Determinaciongrupoentidad grupo : gruposAplicacion) {
			if (!determinaciones.contains(grupo.getDeterminacionByIddeterminaciongrupo().getIden())) {
				determinaciones.add(grupo.getDeterminacionByIddeterminaciongrupo().getIden());
				detJson = procesarDeterminacion(grupo.getDeterminacionByIddeterminaciongrupo()
						, servicioPlaneamiento.getUnidad(grupo.getDeterminacionByIddeterminaciongrupo().getIden())
						, servicioDiccionario, idioma);
				detJson.put("plan", grupo.getDeterminacionByIddeterminaciongrupo().getTramite().getPlan().getNombre());
				lista.add(detJson);
			}
		}
		data.put("data", lista);
		data.put("pages", 1);
		data.put("total", lista.size());
		return data;
	}

}

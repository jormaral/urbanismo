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

import es.mitc.redes.urbanismoenred.data.fip.CaracterDeterminacion;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Determinacion;
import es.mitc.redes.urbanismoenred.servicios.planeamiento.ServicioDiccionariosLocal;
import es.mitc.redes.urbanismoenred.servicios.planeamiento.ServicioPlaneamientoLocal;

/**
 * @author Arnaiz Consultores
 *
 */
@Stateless(name="detRegDirecto")
public class DetRegDirectoHelper implements IJsonHelper {
	
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
		List<Map<String, Object>> listaDets = new ArrayList<Map<String, Object>>();
		Determinacion[] determinaciones = servicioPlaneamiento.getDeterminacionesRegimenDirectoEntidad((Integer)id);
		Map<String, Object> detJson;
		for (Determinacion determinacion : determinaciones) {
			detJson = new HashMap<String, Object>();
			detJson.put("iden", determinacion.getIden());
			detJson.put("nombre", determinacion.getNombre());
			detJson.put("detOriginal", determinacion.getDeterminacionByIddeterminacionoriginal() != null ? determinacion.getDeterminacionByIddeterminacionoriginal().getNombre() : "-");
			detJson.put("idDetPadre", determinacion.getDeterminacionByIdpadre() != null ? determinacion.getDeterminacionByIdpadre().getIden(): null);
			detJson.put("detPadre", determinacion.getDeterminacionByIdpadre() != null ? determinacion.getDeterminacionByIdpadre().getNombre() : "-");
			detJson.put("idTramite", determinacion.getTramite().getIden());
			detJson.put("idCaracter", determinacion.getIdcaracter());
			detJson.put("caracter", servicioDiccionario.getTraduccion(CaracterDeterminacion.class, determinacion.getIdcaracter(), idioma));
			detJson.put("apartado", determinacion.getApartado());
			detJson.put("texto", determinacion.getTexto());
			detJson.put("etiqueta", determinacion.getEtiqueta());
			detJson.put("codigo", determinacion.getCodigo());
			listaDets.add(detJson);
		}
		
		data.put("data", listaDets);
		data.put("pages", 1);
		data.put("total", listaDets.size());
		return data;
	}

}

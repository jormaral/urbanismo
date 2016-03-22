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

import es.mitc.redes.urbanismoenred.data.rpm.diccionario.Centroproduccion;
import es.mitc.redes.urbanismoenred.servicios.planeamiento.ServicioDiccionariosLocal;

/**
 * @author Arnaiz Consultores
 *
 */
@Stateless(name ="CentroProduccion")
public class CentroProduccionHelper implements IJsonHelper {

	@EJB
	private ServicioDiccionariosLocal servicioDiccionario;
	
	/*
	 * (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.servicios.planeamiento.helpers.IJsonHelper#marshal(java.lang.Object)
	 */
	@Override
	public Map<String, Object> marshal(Object id, String idioma) {
		Map<String, Object> data = new HashMap<String, Object>();
		List<Object> listaCentros = new ArrayList<Object>();
		
		Centroproduccion[] centros = servicioDiccionario.get(Centroproduccion.class);
		Map<String, Object> cj;
		for (int i = 0; i < centros.length; i++) {
			cj = new HashMap<String, Object>();
			cj.put("iden", centros[i].getIden());
			cj.put("nombre", servicioDiccionario.getTraduccion(Centroproduccion.class, centros[i].getIden(), idioma));
			cj.put("codigo", centros[i].getCodigo());
			cj.put("mail", centros[i].getMail());
			listaCentros.add(cj);
		}
		
		data.put("data", listaCentros);
		data.put("pages", 1);
		data.put("total", listaCentros.size());
		return data;
	}

}

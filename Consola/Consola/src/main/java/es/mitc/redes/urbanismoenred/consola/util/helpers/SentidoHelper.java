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

import es.mitc.redes.urbanismoenred.data.rpm.diccionario.Sentido;
import es.mitc.redes.urbanismoenred.servicios.planeamiento.ServicioDiccionariosLocal;

/**
 * @author Arnaiz consultores
 *
 */
@Stateless(name ="Sentido")
public class SentidoHelper implements IJsonHelper {

	@EJB
	private ServicioDiccionariosLocal servicioDiccionario;

	/*
	 * (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.servicios.planeamiento.helpers.IJsonHelper#marshal(java.lang.Object)
	 */
	@Override
	public Map<String, Object> marshal(Object id, String idioma) {
		Map<String, Object> data = new HashMap<String, Object>();
		List<Object> listaSentidos = new ArrayList<Object>();
		Map<String, Object> res;
		for (Sentido sentido : servicioDiccionario.get(Sentido.class)) {
			res = new HashMap<String, Object>();

	        res.put("iden", sentido.getIden());
	        res.put("nombre", servicioDiccionario.getTraduccion(Sentido.class, sentido.getIden(), idioma));
	        listaSentidos.add(res);
		}
		
		data.put("data", listaSentidos);
		data.put("pages", 1);
		data.put("total", listaSentidos.size());
		return data;
	}

}

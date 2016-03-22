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

import es.mitc.redes.urbanismoenred.data.rpm.diccionario.Operacioncaracter;
import es.mitc.redes.urbanismoenred.servicios.planeamiento.ServicioDiccionariosLocal;

/**
 * @author Arnaiz Consultores
 *
 */
@Stateless(name="OperacionCaracter")
public class OperacionCaracterHelper implements IJsonHelper {
	
	@EJB
	private ServicioDiccionariosLocal servicioDiccionario;

	/*
	 * (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.servicios.planeamiento.helpers.IJsonHelper#marshal(java.lang.Object)
	 */
	@Override
	public Map<String, Object> marshal(Object id, String idioma) {
		Map<String, Object> data = new HashMap<String, Object>();
		List<Object> listaOperaciones = new ArrayList<Object>();
		Map<String, Object> res;
		for (Operacioncaracter operacion : servicioDiccionario.get(Operacioncaracter.class)) {
			res = new HashMap<String, Object>();
			res.put("iden", operacion.getIden());
	        res.put("idCaracterOperado", operacion.getCaracterdeterminacionByIdcaracteroperado().getIden());
	        res.put("idCaracterOperador", operacion.getCaracterdeterminacionByIdcaracteroperador().getIden());
	        res.put("idTipoOperacionDet", operacion.getTipooperaciondeterminacion() != null? operacion.getTipooperaciondeterminacion().getIden():-1);
	        
	        listaOperaciones.add(res);
		}
		
		data.put("data", listaOperaciones);
		data.put("pages", 1);
		data.put("total", listaOperaciones.size());
		return data;
	}

}

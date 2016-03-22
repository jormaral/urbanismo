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

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Determinacion;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidad;
import es.mitc.redes.urbanismoenred.servicios.planeamiento.ServicioDiccionariosLocal;

/**
 * @author Arnaiz Consultores
 *
 */
public abstract class AbstractEntidadHelper {
	protected Map<String, Object> procesarEntidad(Entidad entidad, Determinacion grupo, ServicioDiccionariosLocal servicioDiccionario, String idioma) {
		Map<String, Object> res = new HashMap<String, Object>();
		
		ResourceBundle traductor = PropertyResourceBundle.getBundle("es.mitc.redes.urbanismoenred.consola.util.Traducciones"
				, new Locale(idioma != null? idioma:"es","ES"));

        //OBTENGO EL NOMBRE DE LA ENT. ORIGINAL
        Entidad e = entidad.getEntidadByIdentidadoriginal();
        String nomEntOriginal = "-";
        if (e != null) {
            nomEntOriginal = e.getNombre();
        }

        //OBTENGO EL NOMBRE DE LA ENT. PADRE
        e = entidad.getEntidadByIdpadre();
        String nomEntPadre = "-";
        Integer idEntPadre = null;
        if (e != null) {
            nomEntPadre = e.getNombre();
            idEntPadre = e.getIden();
        }
        
        res.put("iden", entidad.getIden());
        res.put("nombre", entidad.getNombre());
        res.put("entOriginal", nomEntOriginal);
        res.put("idEntPadre", idEntPadre);
        res.put("entPadre", nomEntPadre);
        res.put("idTramite", entidad.getTramite().getIden());
        res.put("grupo", grupo != null ? grupo.getNombre() + " ("+ grupo.getTramite().getPlan().getNombre() +")" : "-");
        res.put("clave", entidad.getClave());
        res.put("etiqueta", entidad.getEtiqueta());
        res.put("codigo", entidad.getCodigo());
        String nombreEntBase = "-";
        if(entidad.getEntidadByIdentidadbase()!=null){
            nombreEntBase = entidad.getEntidadByIdentidadbase().getNombre();
        }
        res.put("base", nombreEntBase);
        if (entidad.isBsuspendida()) {
        	res.put("suspendida", traductor.getString("si"));
        } else {
        	res.put("suspendida", traductor.getString("no"));
        }

        return res;
	}
}

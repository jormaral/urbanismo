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

import es.mitc.redes.urbanismoenred.data.rpm.diccionario.Caracterdeterminacion;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Determinacion;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Tramite;
import es.mitc.redes.urbanismoenred.servicios.planeamiento.ServicioDiccionariosLocal;

/**
 * @author Arnaiz consultores
 *
 */
public abstract class AbstractDeterminacionHelper {
	protected Map<String, Object> procesarDeterminacion(Determinacion det, Determinacion unidad, ServicioDiccionariosLocal servicioDiccionario, String idioma) {
		
		ResourceBundle traductor = PropertyResourceBundle.getBundle("es.mitc.redes.urbanismoenred.consola.util.Traducciones"
				, new Locale(idioma,"ES"));
		
		Map<String, Object> detJson = new HashMap<String, Object>();
		//OBTENGO EL NOMBRE DE LA DET. ORIGINAL
        Determinacion d = det.getDeterminacionByIddeterminacionoriginal();
        String nomDetOriginal = "-";
        if (d != null) {
            nomDetOriginal = d.getNombre();
        }

        //OBTENGO EL NOMBRE DE LA DET. PADRE
        d = det.getDeterminacionByIdpadre();
        String nomDetPadre = "-";
        Integer idDetPadre = null;
        if (d != null) {
            nomDetPadre = d.getNombre();
            idDetPadre = d.getIden();
        }
        //OBTENGO EL NOMBRE DEL CARACTER
        int idCaracter = det.getIdcaracter();
        String nomCaracter = "-";
        if (idCaracter >= 0) {
            
            nomCaracter = servicioDiccionario.getTraduccion(Caracterdeterminacion.class, idCaracter, idioma);
        }

        //OBTENGO El TRAMITE AL QUE PERTENECE LA DETERMINACION
        Integer idTramite = null;
        Tramite trm = det.getTramite();
        if (trm != null) {
            idTramite = trm.getIden();
        }


        detJson.put("iden", det.getIden());
        detJson.put("nombre", det.getNombre());
        detJson.put("detOriginal", nomDetOriginal);
        detJson.put("idDetPadre", idDetPadre);
        detJson.put("detPadre", nomDetPadre);
        detJson.put("idTramite", idTramite);
        detJson.put("idCaracter", idCaracter);
        detJson.put("caracter", nomCaracter);
        detJson.put("apartado", det.getApartado());
        detJson.put("texto", det.getTexto() != null ? det.getTexto() : "-");
        detJson.put("etiqueta", det.getEtiqueta() != null ? det.getEtiqueta() : "-");
        detJson.put("codigo", det.getCodigo());
        detJson.put("unidad", unidad != null ? unidad.getNombre():"");
        detJson.put("suspendida", det.isBsuspendida() ? traductor.getString("si"):traductor.getString("no"));
        String nombreDetBase = "-";
        if(det.getDeterminacionByIddeterminacionbase() != null) {
            nombreDetBase = det.getDeterminacionByIddeterminacionbase().getNombre();
        }
        detJson.put("base", nombreDetBase);
        
        return detJson;
	}
}

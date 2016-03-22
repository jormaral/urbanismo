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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import es.mitc.redes.urbanismoenred.data.rpm.diccionario.Ambito;
import es.mitc.redes.urbanismoenred.data.rpm.diccionario.Centroproduccion;
import es.mitc.redes.urbanismoenred.data.rpm.diccionario.Organo;
import es.mitc.redes.urbanismoenred.data.rpm.diccionario.Sentido;
import es.mitc.redes.urbanismoenred.data.rpm.diccionario.Tipotramite;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Ambitoaplicacionambito;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Tramite;
import es.mitc.redes.urbanismoenred.servicios.planeamiento.ServicioDiccionariosLocal;
import es.mitc.redes.urbanismoenred.servicios.planeamiento.ServicioPlaneamientoLocal;

/**
 * @author Arnaiz Consultores
 *
 */
@Stateless(name="tramite")
public class TramiteHelper implements IJsonHelper {
	
	@EJB
	private ServicioPlaneamientoLocal servicioPlaneamiento;
	@EJB
	private ServicioDiccionariosLocal servicioDiccionario;
	
	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

	/*
	 * (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.servicios.planeamiento.helpers.IJsonHelper#marshal(java.lang.Object)
	 */
	@Override
	public Map<String, Object> marshal(Object id, String idioma) {
		Tramite tramite = servicioPlaneamiento.get(Tramite.class, id);
		Map<String, Object> data = new HashMap<String, Object>();
        //GENERO LA LISTA DE COLUMNAS (EN ESTE CASO HABRA UNA SOLO)
        List<Object> columnlist = new ArrayList<Object>();
        
        Map<String, Object> linea = new HashMap<String, Object>();

        //OBTENGO EL TIPO DE TRAMITE
        String nomTipoTramite = "-";
        if (tramite.getTipotramite() != null) {
            nomTipoTramite = servicioDiccionario.getTraduccion(Tipotramite.class, tramite.getIdtipotramite(), idioma);
        }
        
        //OBTENGO EL CENTRO DE PRODUCCION
        String nomCentroProduccion = servicioDiccionario.getTraduccion(Centroproduccion.class, tramite.getIdcentroproduccion(), idioma);

        linea.put("iden", tramite.getIden());
        linea.put("nombre",tramite.getNombre() != null ? tramite.getNombre(): "-");
        linea.put("tipoTramite", nomTipoTramite);
        linea.put("idTipoTramite", tramite.getIdtipotramite());
        linea.put("iteracion", tramite.getIteracion());
        String fechaTramite = "";
        Date date = tramite.getFecha();
        if (date != null) {
            fechaTramite = sdf.format(date);
        }
        linea.put("fecha", fechaTramite);
        linea.put("texto", tramite.getTexto() != null ? tramite.getTexto(): "-");
        linea.put("comentario", tramite.getComentario() != null ? tramite.getComentario(): "-");
        linea.put("centroProduccion", nomCentroProduccion);
        linea.put("codigoFip", tramite.getCodigofip());
        date = null;
        date = tramite.getFechaconsolidacion();
        String fechaConsolidacion = "";
        if (date != null) {
            fechaConsolidacion = sdf.format(date);
        }
        linea.put("fechaConsolidacion", fechaConsolidacion);
        linea.put("organo", tramite.getIdorgano() != null? servicioDiccionario.getTraduccion(Organo.class, tramite.getIdorgano(), idioma):"-");
        linea.put("sentido", tramite.getIdsentido() != null? servicioDiccionario.getTraduccion(Sentido.class, tramite.getIdsentido(), idioma):"-");
        
        StringBuffer ambito = new StringBuffer();
        Ambitoaplicacionambito[] ambitosAplicacion = servicioPlaneamiento.getAmbitos(tramite.getIden());
        for(int i = 0; i < ambitosAplicacion.length; i++) {
        	ambito.append(servicioDiccionario.getTraduccion(Ambito.class, ambitosAplicacion[i].getIdambito(), idioma));
        	if (i +1 < ambitosAplicacion.length) {
        		ambito.append(" / ");
        	}
        }
        
        linea.put("ambito", ambito.toString());
        
        columnlist.add(linea);
        
        data.put("page", "1");
        data.put("total", "1");
        data.put("data", columnlist);
        
        return data;
	}

}

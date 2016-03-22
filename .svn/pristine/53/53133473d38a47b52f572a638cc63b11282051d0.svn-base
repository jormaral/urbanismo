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
package es.mitc.redes.urbanismoenred.consola.visor.acciones;

import es.mitc.redes.urbanismoenred.consola.visor.IAccion;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Determinacion;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Opciondeterminacion;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Plan;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Tramite;
import es.mitc.redes.urbanismoenred.servicios.planeamiento.ModalidadPlanes;
import es.mitc.redes.urbanismoenred.servicios.planeamiento.ServicioPlaneamientoLocal;
import es.mitc.redes.urbanismoenred.utils.recursos.textos.Textos;
import es.mitc.redes.urbanismoenred.utils.recursos.xml.XML;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.codehaus.jackson.map.ObjectMapper;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

/**
 * @author Arnaiz Consultores
 *
 */
@Stateless(name = "GET_GRUPOS_TRAMITE")
public class GetGruposTramite implements IAccion {

    /* (non-Javadoc)
     * @see es.mitc.redes.urbanismoenred.consola.planeamiento.IAccion#ejecutar(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @EJB
    private ServicioPlaneamientoLocal servicioPlanes;

    @Override
    public void ejecutar(HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        try {
            int idTramite = Integer.valueOf(request.getParameter("idTramite"));
            Tramite tramite = servicioPlanes.get(Tramite.class, idTramite);
            Plan plan = servicioPlanes.get(Plan.class, tramite.getPlan().getIden());
            Plan planBase = servicioPlanes.get(Plan.class, plan.getPlanByIdplanbase().getIden());
            Tramite tramiteBase = null;
            if (planBase != null) {
                tramiteBase = servicioPlanes.getTramitesPorPlan(planBase.getIden(), ModalidadPlanes.TODOS)[0];
            }

            Determinacion[] determinacionesGrupo = null;
            Opciondeterminacion[] opciones = null;
            Determinacion determinacion = null;
            Map<String, String> mapGrupos = new HashMap<String, String>();

            if (tramiteBase != null) {
                determinacionesGrupo = servicioPlanes.getDeterminacionesPorCaracter(tramiteBase.getIden(), Integer.parseInt(Textos.getTexto("diccionario", "caracterdeterminacion.grupoentidades")));
                for (Determinacion detGrupo : determinacionesGrupo) {
                    opciones = servicioPlanes.getOpcionesDeterminacion(detGrupo.getIden());
                    for (Opciondeterminacion opc : opciones) {
                        determinacion = servicioPlanes.get(Determinacion.class, opc.getDeterminacionByIddeterminacionvalorref().getIden());
                            mapGrupos.put(determinacion.getCodigo(), determinacion.getNombre());
                    }
                }
            }

            determinacionesGrupo = servicioPlanes.getDeterminacionesPorCaracter(tramite.getIden(), Integer.parseInt(Textos.getTexto("diccionario", "caracterdeterminacion.grupoentidades")));
            for (Determinacion detGrupo : determinacionesGrupo) {
                opciones = servicioPlanes.getOpcionesDeterminacion(detGrupo.getIden());
                for (Opciondeterminacion opc : opciones) {
                    determinacion = servicioPlanes.get(Determinacion.class, opc.getDeterminacionByIddeterminacionvalorref().getIden());
                    if (!mapGrupos.containsValue(determinacion.getCodigo()) && !mapGrupos.containsValue(determinacion.getNombre())) {
                        mapGrupos.put(determinacion.getCodigo(), determinacion.getNombre());
                    }
                }
            }

            response.setHeader("Content-type", "application-json");

            // Write JSON data
            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(response.getWriter(), ordenaPorValor(mapGrupos));

        } catch (Exception ex) {
            response.getWriter().print("");

        }
        response.getWriter().flush();
    }

    private Map ordenaPorValor(Map unsortMap) {

        List list = new LinkedList(unsortMap.entrySet());

        //sort list based on comparator
        Collections.sort(list, new Comparator() {
            public int compare(Object o1, Object o2) {
                Collator comparador = Collator.getInstance();
                return comparador.compare(((Map.Entry) (o1)).getValue().toString(),((Map.Entry) (o2)).getValue().toString());
                //return ((Comparable) ((Map.Entry) (o1)).getValue()).compareTo(((Map.Entry) (o2)).getValue());
            }
        });

        //put sorted list into map again
        Map sortedMap = new LinkedHashMap();
        for (Iterator it = list.iterator(); it.hasNext();) {
            Map.Entry entry = (Map.Entry) it.next();
            sortedMap.put(entry.getKey(), entry.getValue());
        }
        return sortedMap;
    }
}

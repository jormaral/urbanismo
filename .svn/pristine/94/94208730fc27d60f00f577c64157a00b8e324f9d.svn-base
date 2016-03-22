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
package es.mitc.redes.urbanismoenred.consola.planeamiento.acciones;

import java.io.IOException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import es.mitc.redes.urbanismoenred.consola.planeamiento.IAccion;
import es.mitc.redes.urbanismoenred.data.rpm.diccionario.Tipotramite;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Tramite;
import es.mitc.redes.urbanismoenred.servicios.planeamiento.ModalidadPlanes;
import es.mitc.redes.urbanismoenred.servicios.planeamiento.ServicioDiccionariosLocal;
import es.mitc.redes.urbanismoenred.servicios.planeamiento.ServicioPlaneamientoLocal;

/**
 * @author Arnaiz Consultores
 *
 */
@Stateless(name="GET_NODOS_TRAMS_DE_PLAN")
public class GetTramitesPlan implements IAccion {
	private static SimpleDateFormat sdf = new SimpleDateFormat("(dd/MM/yyyy) ");
	@EJB
	private ServicioPlaneamientoLocal servicioPlaneamiento;
	@EJB
	private ServicioDiccionariosLocal servicioDiccionario;

	/* (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.consola.planeamiento.IAccion#ejecutar(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public void ejecutar(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String plan = request.getParameter("idPlan");
		String load = request.getParameter("load");
		String idioma = request.getParameter("idioma");
		String tipo = request.getParameter("tipo");
		
		if (plan != null && tipo != null) {
			response.setContentType("text/xml;charset=UTF-8");
			int idPlan = Integer.valueOf(plan);
			XMLOutputFactory xof = XMLOutputFactory.newInstance();
            XMLStreamWriter xtw;
    		try {
    			xtw = xof.createXMLStreamWriter(response.getWriter());
    			xtw.writeStartDocument("utf-8","1.0");
                xtw.writeStartElement("nodes");
                
                for (Tramite tramite : servicioPlaneamiento.getTramitesPorPlan(idPlan, ModalidadPlanes.valueOf(tipo))) {
                	xtw.writeStartElement("node");
                	xtw.writeAttribute("text", servicioDiccionario.getTraduccion(Tipotramite.class, tramite.getIdtipotramite() , idioma) 
                			+ " - " 
                			+ (tramite.getFecha() != null? sdf.format(tramite.getFecha()): "")
                			+ (tramite.getNombre() != null? tramite.getNombre():""));
                	xtw.writeAttribute("tipo", "tramite");
                	xtw.writeAttribute("idNode", String.valueOf(tramite.getIden()));
                	if ( load != null && Boolean.parseBoolean(load)) {
                		xtw.writeAttribute("load", "GestionConsola?wsName=GET_NODOS_INFO_DE_TRAM&idTram=" + tramite.getIden() + "&idioma=" + URLEncoder.encode(idioma,"UTF-8"));
                	}
                	if (ModalidadPlanes.valueOf(tipo) == ModalidadPlanes.FICHAS) {
                        xtw.writeAttribute("load", "GestionConsola?wsName=GET_NODOS_FICHAS_DE_TRAM&idTram=" + tramite.getIden() + "&idioma=" + URLEncoder.encode(idioma, "UTF-8"));
                    }
                	xtw.writeAttribute("idTipoTramite", String.valueOf(tramite.getIdtipotramite()));
                	xtw.writeEndElement();
                }
                xtw.writeEndElement();
                xtw.writeEndDocument();
               
                response.getWriter().flush();
    		} catch (XMLStreamException e) {
    			response.getWriter().print("Error al generar el xml. Consulte con su administrador.");
    		}
        } else {
        	response.getWriter().print("Solicitud incompleta");
        }
	}

}

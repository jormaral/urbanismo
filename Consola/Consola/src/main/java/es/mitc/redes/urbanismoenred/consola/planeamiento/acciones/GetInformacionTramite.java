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

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import es.mitc.redes.urbanismoenred.consola.planeamiento.IAccion;
import es.mitc.redes.urbanismoenred.servicios.planeamiento.ServicioPlaneamientoLocal;

/**
 * @author Arnaiz Consultores
 *
 */
@Stateless(name="GET_NODOS_INFO_DE_TRAM")
public class GetInformacionTramite implements IAccion {
	
	@EJB
	private ServicioPlaneamientoLocal servicioPlaneamiento;

	/* (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.consola.planeamiento.IAccion#ejecutar(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public void ejecutar(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String idTram = request.getParameter("idTram");
		String idioma = request.getParameter("idioma");
		if ( idTram != null 
        		&& idioma != null) {
			response.setContentType("text/xml;charset=UTF-8");
			
			XMLOutputFactory xof = XMLOutputFactory.newInstance();
	        XMLStreamWriter xtw;
			try {
				xtw = xof.createXMLStreamWriter(response.getWriter());
				xtw.writeStartDocument("utf-8","1.0");
	            xtw.writeStartElement("nodes");
	           
	            xtw.writeStartElement("node");
	            xtw.writeAttribute("text", "Determinaciones (" + servicioPlaneamiento.getDeterminacionesRaiz(Integer.parseInt(idTram)).length +")");
	            xtw.writeAttribute("tipo", "info-determinacion");
	            xtw.writeAttribute("idNode", idTram);
	            xtw.writeAttribute("load", "GestionConsola?wsName=GET_NODOS_DETERM_DE_TRAMITE&idTramite=" + idTram + "&idioma=" + URLEncoder.encode(idioma,"UTF-8"));
	            xtw.writeEndElement();
	            
	            xtw.writeStartElement("node");
	            xtw.writeAttribute("text", "Entidades (" + servicioPlaneamiento.getEntidadesRaiz(Integer.parseInt(idTram)).length +")");
	            xtw.writeAttribute("tipo", "info-entidad");
	            xtw.writeAttribute("idNode", idTram);
	            xtw.writeAttribute("load", "GestionConsola?wsName=GET_NODOS_ENTIDAD_DE_TRAMITE&idTramite=" + idTram + "&idioma=" + URLEncoder.encode(idioma,"UTF-8"));
	            xtw.writeEndElement();
	            
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

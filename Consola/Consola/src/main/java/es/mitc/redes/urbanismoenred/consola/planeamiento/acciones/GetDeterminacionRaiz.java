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

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import es.mitc.redes.urbanismoenred.consola.planeamiento.IAccion;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Determinacion;
import es.mitc.redes.urbanismoenred.servicios.planeamiento.ServicioPlaneamientoLocal;

/**
 * @author Arnaiz Consultores
 *
 */
@Stateless(name="GET_NODOS_DETERM_DE_TRAMITE")
public class GetDeterminacionRaiz implements IAccion {
	
	@EJB
	private ServicioPlaneamientoLocal servicioPlaneamiento;

	/* (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.consola.planeamiento.IAccion#ejecutar(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public void ejecutar(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String idTramite = request.getParameter("idTramite");
		if (idTramite != null) {
			Determinacion[] determinaciones = servicioPlaneamiento.getDeterminacionesRaiz(Integer.valueOf(idTramite));
	    	XMLOutputFactory xof = XMLOutputFactory.newInstance();
	        XMLStreamWriter xtw;
			try {
				response.setContentType("text/xml;charset=UTF-8");
				xtw = xof.createXMLStreamWriter(response.getWriter());
				xtw.writeStartDocument("utf-8","1.0");
	            xtw.writeStartElement("nodes");
	            
	            for (Determinacion det : determinaciones) {
	            	xtw.writeStartElement("node");
	            	xtw.writeAttribute("text", (det.getApartado()!= null ? det.getApartado() +" ":"")+det.getNombre());
	            	xtw.writeAttribute("tipo", "determinacion");
	            	xtw.writeAttribute("caracter", String.valueOf(det.getIdcaracter()));
	            	xtw.writeAttribute("idNode", String.valueOf(det.getIden()));
	            	if (det.getDeterminacionsForIdpadre().size() > 0)
	            		xtw.writeAttribute("load", "GestionConsola?wsName=GET_NODOS_DETERM_DE_DETERM&idDet=" + det.getIden());
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

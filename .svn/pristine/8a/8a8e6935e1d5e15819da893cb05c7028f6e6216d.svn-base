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
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.RegulacionEspecifica;
import es.mitc.redes.urbanismoenred.servicios.planeamiento.ServicioPlaneamientoLocal;

/**
 * @author Arnaiz Consultores
 *
 */
@Stateless(name="GET_NODOS_REGESP_DET_RPM")
public class GetNodosRegulacionEspecifica implements IAccion {
	
	@EJB
	private ServicioPlaneamientoLocal servicioPlaneamiento;

	/* (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.consola.planeamiento.IAccion#ejecutar(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public void ejecutar(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		
		if (request.getParameter("idDet") != null) {
			try {
				int idDet = Integer.parseInt(request.getParameter("idDet"));
		        
		        response.setContentType("text/xml;charset=UTF-8");
		        XMLOutputFactory xof = XMLOutputFactory.newInstance();
		        XMLStreamWriter xtw;
		        RegulacionEspecifica[] sregs = servicioPlaneamiento.getRegulacionesEspecificas(idDet);
	        
				xtw = xof.createXMLStreamWriter(response.getWriter());
				xtw.writeStartDocument("utf-8","1.0");
	            xtw.writeStartElement("nodes");
	            
	            for (RegulacionEspecifica re : sregs) {
	            	xtw.writeStartElement("node");
	            	xtw.writeAttribute("text", re.getNombre());
	            	xtw.writeAttribute("comment", re.getTexto());
	            	xtw.writeAttribute("idNode", String.valueOf(re.getIden()));
	            	xtw.writeEndElement();
	            }
	            xtw.writeEndElement();
	            xtw.writeEndDocument();
	           
	            response.getWriter().flush();
			} catch (XMLStreamException e) {
				response.getWriter().print("Error al generar el xml. Consulte con su administrador.");
			} catch (NumberFormatException nfe) {
				response.getWriter().print("El identificador especificado no es correcto.");
			}
        } else {
        	response.getWriter().print("No se han indicado los parámetros de la petición");
        }
	}

}

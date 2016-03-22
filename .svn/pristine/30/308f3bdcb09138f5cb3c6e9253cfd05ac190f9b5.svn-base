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
import es.mitc.redes.urbanismoenred.servicios.planeamiento.ModalidadPlanes;
import es.mitc.redes.urbanismoenred.servicios.planeamiento.ServicioPlaneamientoLocal;

/**
 * @author Arnaiz Consultores
 *
 */
@Stateless(name="GET_NODOS_INFO_DE_PLAN")
public class GetInformacionPlan implements IAccion {
	
	
	@EJB
	private ServicioPlaneamientoLocal servicioPlaneamiento;

	/* (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.consola.planeamiento.IAccion#ejecutar(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public void ejecutar(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String idPlan = request.getParameter("idPlan");
		String idioma = request.getParameter("idioma");
	    String operacion = request.getParameter("op");
	    String tipo = request.getParameter("tipo");
	    
		if (idPlan != null &&
        		operacion != null &&
        		idioma != null &&
        		tipo != null) {
			response.setContentType("text/xml;charset=UTF-8");
			
			XMLOutputFactory xof = XMLOutputFactory.newInstance();
            XMLStreamWriter xtw;
    		try {
    			xtw = xof.createXMLStreamWriter(response.getWriter());
    			xtw.writeStartDocument("utf-8","1.0");
                xtw.writeStartElement("nodes");
                
                int num = servicioPlaneamiento.getTramitesPorPlan(Integer.parseInt(idPlan), ModalidadPlanes.valueOf(tipo)).length;
               
                if (num > 0) {
                	xtw.writeStartElement("node");
                    xtw.writeAttribute("text", "Trámites (" + num + ")");
                    xtw.writeAttribute("load", "GestionConsola?wsName=GET_NODOS_TRAMS_DE_PLAN&idPlan=" 
                        			+ idPlan 
                        			+ "&op=" 
                        			+ URLEncoder.encode(operacion,"UTF-8") 
                        			+ "&idioma=" 
                        			+ URLEncoder.encode(idioma,"UTF-8")
                        			+"&tipo="+tipo);
                    	xtw.writeEndElement();
                }
                	
                num = servicioPlaneamiento.getNumeroPlanesHijos(Integer.parseInt(idPlan),null, ModalidadPlanes.valueOf(tipo));
                
                if (num > 0) {
                	xtw.writeStartElement("node");
                    xtw.writeAttribute("text", "Planes (" + num + ")");
                    xtw.writeAttribute("tipo", "infoplan");
                    xtw.writeAttribute("load", "GestionConsola?wsName=GET_INSTRUMENTOSPLAN&idPlan=" 
                        			+ idPlan 
                        			+ "&op=" 
                        			+ URLEncoder.encode(operacion,"UTF-8") 
                        			+ "&idioma=" 
                        			+ URLEncoder.encode(idioma,"UTF-8")
                        			+"&tipo="+tipo);
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

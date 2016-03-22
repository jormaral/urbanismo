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
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Casoentidaddeterminacion;
import es.mitc.redes.urbanismoenred.servicios.planeamiento.ServicioPlaneamientoLocal;

/**
 * @author Arnaiz Consultores
 *
 */
@Stateless(name="GET_NODOS_CASOS_APLICACION")
public class GetCasosAplicacion implements IAccion {
	
	@EJB
	private ServicioPlaneamientoLocal servicioPlaneamiento;

	/* (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.consola.planeamiento.IAccion#ejecutar(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public void ejecutar(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String idDet = request.getParameter("idDet");
		String idioma = request.getParameter("idioma");
		String idEnt = request.getParameter("idEnt");
		
		
		if (idDet != null &&
        		idioma != null && 
        		idEnt != null) {
			response.setContentType("text/xml;charset=UTF-8");
				
        	XMLOutputFactory xof = XMLOutputFactory.newInstance();
            XMLStreamWriter xtw;
    		try {
    			xtw = xof.createXMLStreamWriter(response.getWriter());
    			xtw.writeStartDocument("utf-8","1.0");
                xtw.writeStartElement("nodes");
                
                Casoentidaddeterminacion[] casos = servicioPlaneamiento.getCasos(Integer.parseInt(idEnt), Integer.parseInt(idDet));
                
                
                for (int i = 0; i< casos.length; i++) {
                		
                	xtw.writeStartElement("node");
                    xtw.writeAttribute("text", casos[i].getNombre() != null ? casos[i].getNombre() : "Caso" + (i+1));
                    xtw.writeAttribute("tipo", "casoentidaddeterminacion");
                    xtw.writeAttribute("idNode", String.valueOf(casos[i].getIden()));
                    xtw.writeAttribute("load", 
                    		"GestionConsola?wsName=GET_NODOS_VALORES_APLICACION&idCaso=" 
                        					+ casos[i].getIden() 
                        					+ "&idioma=" 
                        					+ URLEncoder.encode(idioma,"UTF-8")
                        					+ "&idDet=" + idDet);
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

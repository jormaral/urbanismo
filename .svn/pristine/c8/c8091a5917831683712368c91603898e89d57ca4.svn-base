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
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import es.mitc.redes.urbanismoenred.consola.planeamiento.IAccion;
import es.mitc.redes.urbanismoenred.data.rpm.diccionario.Instrumentoplan;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidad;
import es.mitc.redes.urbanismoenred.servicios.planeamiento.ServicioDiccionariosLocal;
import es.mitc.redes.urbanismoenred.servicios.planeamiento.ServicioPlaneamientoLocal;

/**
 * @author Arnaiz Consultores
 *
 */
@Stateless(name="GET_NODOS_PLANES_APLICACION")
public class GetPlanesAplicacion implements IAccion {
	
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
		String idDet = request.getParameter("idDet");
		String idioma = request.getParameter("idioma");
		
		if (idDet != null &&
        		idioma != null) {
			response.setContentType("text/xml;charset=UTF-8");
			
			XMLOutputFactory xof = XMLOutputFactory.newInstance();
            XMLStreamWriter xtw;
    		try {
    			xtw = xof.createXMLStreamWriter(response.getWriter());
    			xtw.writeStartDocument("utf-8","1.0");
                xtw.writeStartElement("nodes");
                
                Entidad[] entidades = servicioPlaneamiento.getAplicacionesDeterminacion(Integer.parseInt(idDet));
                List<Integer> planesIncluidos = new ArrayList<Integer>();
                String texto = "";
                Instrumentoplan instrumento;
                for (int i = 0; i< entidades.length; i++) {
                	if (!planesIncluidos.contains(entidades[i].getTramite().getPlan().getIden())) {
                		instrumento = servicioDiccionario.getInstrumento(entidades[i].getTramite().getPlan().getIden());
                    	
                    	if (instrumento != null) {
                    		texto = servicioDiccionario.getTraduccion(Instrumentoplan.class, instrumento.getIden(), idioma)+ " - " + entidades[i].getTramite().getPlan().getNombre();	
                    	} else {
                    		texto = entidades[i].getTramite().getPlan().getNombre();
                    	}
                		
                		xtw.writeStartElement("node");
                    	xtw.writeAttribute("text", texto);
                    	xtw.writeAttribute("tipo", "plan");
                    	xtw.writeAttribute("idNode", String.valueOf(entidades[i].getTramite().getPlan().getIden()));
                    	xtw.writeAttribute("load", 
                    			"GestionConsola?wsName=GET_NODOS_ENTIDADES_APLICACION&idPlan=" 
                        					+ entidades[i].getTramite().getPlan().getIden() 
                        					+ "&idioma=" 
                        					+ URLEncoder.encode(idioma,"UTF-8")
                        					+ "&idDet=" + idDet);
                    	xtw.writeEndElement();
                    	
                    	planesIncluidos.add(entidades[i].getTramite().getPlan().getIden());
                	}
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

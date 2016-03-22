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
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Determinacion;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidad;
import es.mitc.redes.urbanismoenred.servicios.planeamiento.ServicioPlaneamientoLocal;

/**
 * @author Arnaiz Consultores
 *
 */
@Stateless(name="GET_NODOS_ENTIDADES_APLICACION")
public class GetEntidadesAplicacion implements IAccion {
	
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
		String idPlan = request.getParameter("idPlan");
		
		if (idDet != null &&
        		idioma != null && 
        		idPlan != null) {
			response.setContentType("text/xml;charset=UTF-8");
			
			XMLOutputFactory xof = XMLOutputFactory.newInstance();
            XMLStreamWriter xtw;
    		try {
    			xtw = xof.createXMLStreamWriter(response.getWriter());
    			xtw.writeStartDocument("utf-8","1.0");
                xtw.writeStartElement("nodes");
                
                Entidad[] entidades = servicioPlaneamiento.getAplicacionesDeterminacion(Integer.parseInt(idDet));
                List<Integer> planesIncluidos = new ArrayList<Integer>();
                int plan = Integer.parseInt(idPlan);
                Determinacion grupoEntidad = null;
                for (int i = 0; i< entidades.length; i++) {
                	if (entidades[i].getTramite().getPlan().getIden() == plan) {
                		grupoEntidad = servicioPlaneamiento.getGrupoEntidad(entidades[i].getIden());
                		xtw.writeStartElement("node");
                    	xtw.writeAttribute("text", entidades[i].getClave() + " " + entidades[i].getNombre());
                    	xtw.writeAttribute("tipo", "entidad");
                    	xtw.writeAttribute("idNode", String.valueOf(entidades[i].getIden()));
                    	xtw.writeAttribute("load", 
                    			"GestionConsola?wsName=GET_NODOS_CASOS_APLICACION&idEnt=" 
                        					+ entidades[i].getIden() 
                        					+ "&idioma=" 
                        					+ URLEncoder.encode(idioma,"UTF-8")
                        					+ "&idDet=" + idDet);
                    	if (grupoEntidad != null) {
    	            		xtw.writeAttribute("codigoGrupoEntidad", grupoEntidad.getCodigo());
    		            	xtw.writeAttribute("nombreGrupoEntidad", grupoEntidad.getNombre());
    	            	} else {
    	            		xtw.writeAttribute("codigoGrupoEntidad", "desconocido");
    		            	xtw.writeAttribute("nombreGrupoEntidad", "no asignado");
    	            	}
                    	
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

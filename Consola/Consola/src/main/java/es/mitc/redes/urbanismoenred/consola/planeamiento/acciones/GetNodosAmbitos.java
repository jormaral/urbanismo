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
import java.text.Collator;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Locale;

import javax.ejb.Stateless;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import es.mitc.redes.urbanismoenred.consola.planeamiento.IAccion;
import es.mitc.redes.urbanismoenred.servicios.seguridad.Ambito;
import es.mitc.redes.urbanismoenred.servicios.seguridad.Usuario;

/**
 * 
 * @author Arnaiz Consultores
 *
 */
@Stateless(name="GET_NODOS_AMBITOS")
public class GetNodosAmbitos implements IAccion {
	
	private class ComparadorAmbitos implements Comparator<Ambito> {
		Collator collator;
		public ComparadorAmbitos(String idioma) {
			collator = Collator.getInstance(new Locale(idioma, "ES"));
		}
		
		/*
		 * (non-Javadoc)
		 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
		 */
		@Override
		public int compare(Ambito o1, Ambito o2) {
			return collator.compare(o1.getNombre(), o2.getNombre());
		}
		
	}
	/* (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.consola.planeamiento.IAccion#ejecutar(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public void ejecutar(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		
		String operacion = request.getParameter("op");
        String idioma = request.getParameter("idioma");
        String tipo = request.getParameter("tipo");
        
		if (operacion != null &&
        		idioma != null && 
        		tipo != null) {
			response.setContentType("text/xml;charset=UTF-8");

	        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
	        
	        Ambito[] ambitos = usuario.getAmbitos(operacion);
	        
	        XMLOutputFactory xof = XMLOutputFactory.newInstance();
	        XMLStreamWriter xtw;
			try {
				xtw = xof.createXMLStreamWriter(response.getWriter());
				xtw.writeStartDocument("utf-8","1.0");
	            xtw.writeStartElement("nodes");
	            Arrays.sort(ambitos, new ComparadorAmbitos(idioma));
	            for (Ambito ambito : ambitos) {
	            	xtw.writeStartElement("node");
	            	xtw.writeAttribute("text", ambito.getNombre());
	            	xtw.writeAttribute("tipo", "ambito");
	            	xtw.writeAttribute("idNode", String.valueOf(ambito.getCodigo()));
	            	xtw.writeAttribute("load", "GestionConsola?wsName=GET_NODOS_PLANES_DE_AMBITO&idAmbito=" 
	            			+ ambito.getCodigo() 
	            			+ "&op=" + URLEncoder.encode(operacion,"UTF-8") 
	            			+ "&idioma=" + URLEncoder.encode(idioma,"UTF-8")
	            			+ "&tipo=" + tipo);
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

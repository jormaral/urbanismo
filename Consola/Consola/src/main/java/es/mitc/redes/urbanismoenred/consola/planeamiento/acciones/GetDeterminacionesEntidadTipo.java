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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
@Stateless(name="GET_NODOS_DETS_DE_ENTIDAD_POR_TIPO")
public class GetDeterminacionesEntidadTipo implements IAccion {
	private class Arbol {
		private Map<Integer, Nodo> nodos = new HashMap<Integer, GetDeterminacionesEntidadTipo.Nodo>();
		private Nodo raiz;
		
		public Arbol() {
			raiz = new Nodo(null);
		}
		
		public Nodo addNodo(Determinacion det) {
			if (!nodos.containsKey(det.getIden())) {
				Nodo nodo = new Nodo(det);
				nodos.put(det.getIden(), nodo);
				if (det.getDeterminacionByIdpadre() != null) {
					Nodo padre = addNodo(det.getDeterminacionByIdpadre());
					padre.addHijo(nodo);
				} else {
					raiz.addHijo(nodo);
				}
				return nodo;
			} else {
				return nodos.get(det.getIden());
			}
		}

		public Nodo getRaiz() {
			return raiz;
		}
	}

	private class Nodo {
		private List<Nodo> hijos = new ArrayList<GetDeterminacionesEntidadTipo.Nodo>();
		private Determinacion determinacion;
		
		public Nodo(Determinacion determinacion) {
			this.determinacion = determinacion;
		}
		
		public void addHijo(Nodo hijo) {
			hijos.add(hijo);
		}
		
		public Determinacion getDeterminacion() {
			return determinacion;
		}

		public List<Nodo> getHijos() {
			return hijos;
		}
	}
	
	@EJB
	private ServicioPlaneamientoLocal servicioPlaneamiento;

	/**
	 * 
	 * @param nodo
	 * @param idEnt
	 * @param idioma
	 * @param xtw
	 * @throws XMLStreamException
	 */
	private void crearArbol(Nodo nodo, String idEnt, String idioma, XMLStreamWriter xtw) throws XMLStreamException {
		xtw.writeStartElement("node");
    	xtw.writeAttribute("text", nodo.getDeterminacion().getApartado() +" "+ nodo.getDeterminacion().getNombre());
    	xtw.writeAttribute("tipo", "determinacion");
    	xtw.writeAttribute("caracter", String.valueOf(nodo.getDeterminacion().getIdcaracter()));
    	xtw.writeAttribute("idNode", String.valueOf(nodo.getDeterminacion().getIden()));
    	if (nodo.getHijos().size() > 0) {
    		for (Nodo hijo : nodo.getHijos()) {
        		crearArbol(hijo, idEnt, idioma, xtw);
        	}
    	} else {
    		xtw.writeAttribute("load", "GestionConsola?wsName=GET_NODOS_CASOS_APLICACION&idEnt=" 
					+ idEnt 
					+ "&idioma=" 
					+ idioma
					+ "&idDet=" + nodo.getDeterminacion().getIden());
    	}
    	
    	xtw.writeEndElement();
	}
	
	/**
	 * 
	 * @param determinaciones
	 * @param idEnt
	 * @param idioma
	 * @param xtw
	 * @throws XMLStreamException 
	 */
	private void crearRepresentacionArbol(Determinacion[] determinaciones, String idEnt, String idioma, XMLStreamWriter xtw) throws XMLStreamException {
		// Creo el árbol de determinaciones:
        Arbol arbol = new Arbol();
        for (Determinacion det : determinaciones) {
        	arbol.addNodo(det);
        }
        
        // Luego creo el XML
        for (Nodo hijo: arbol.getRaiz().getHijos()) {
        	crearArbol(hijo, idEnt, idioma, xtw);
        }
	}
	
	/**
	 * 
	 * @param determinaciones
	 * @param idEnt
	 * @param idioma
	 * @param xtw
	 * @throws XMLStreamException 
	 */
	private void crearRepresentacionPlana(Determinacion[] determinaciones,
			String idEnt, String idioma, XMLStreamWriter xtw) throws XMLStreamException {
		for (Determinacion determinacion : determinaciones) {
			xtw.writeStartElement("node");
	    	xtw.writeAttribute("text", determinacion.getApartado() +" "+ determinacion.getNombre());
	    	xtw.writeAttribute("tipo", "determinacion");
	    	xtw.writeAttribute("caracter", String.valueOf(determinacion.getIdcaracter()));
	    	xtw.writeAttribute("idNode", String.valueOf(determinacion.getIden()));
	    	xtw.writeAttribute("load", "GestionConsola?wsName=GET_NODOS_CASOS_APLICACION&idEnt=" 
					+ idEnt 
					+ "&idioma=" 
					+ idioma
					+ "&idDet=" + determinacion.getIden());
	    	xtw.writeEndElement();
		}
	}

	/* (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.consola.planeamiento.IAccion#ejecutar(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public void ejecutar(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String idEnt = request.getParameter("idEnt");
		String tipo = request.getParameter("tipo");
		String idioma = request.getParameter("idioma");
		String formato = request.getParameter("formato");
		
		if (idEnt != null && tipo != null) {
			Determinacion[] determinaciones;
	    	
	    	if (tipo.equalsIgnoreCase("regimendirecto")) {
	    		determinaciones = servicioPlaneamiento.getDeterminacionesRegimenDirectoEntidad(Integer.parseInt(idEnt));
	        } else if (tipo.equalsIgnoreCase("acto")) {
	        	determinaciones = servicioPlaneamiento.getDeterminacionesActoEntidad(Integer.parseInt(idEnt));
	        } else if (tipo.equalsIgnoreCase("uso")) {
	        	determinaciones = servicioPlaneamiento.getDeterminacionesUsoEntidad(Integer.parseInt(idEnt));
	        } else {
	        	determinaciones = new Determinacion[0];
	        }
	    	
	    	XMLOutputFactory xof = XMLOutputFactory.newInstance();
	        XMLStreamWriter xtw;
			try {
				response.setContentType("text/xml;charset=UTF-8");
				xtw = xof.createXMLStreamWriter(response.getWriter());
				xtw.writeStartDocument("utf-8","1.0");
	            xtw.writeStartElement("nodes");
	            
	            if (formato !=null && "plano".equals(formato)) {
	            	crearRepresentacionPlana(determinaciones, idEnt, idioma, xtw);
	            } else {
	            	crearRepresentacionArbol(determinaciones, idEnt, idioma, xtw);
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

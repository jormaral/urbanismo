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
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import es.mitc.redes.urbanismoenred.consola.planeamiento.IAccion;
import es.mitc.redes.urbanismoenred.data.rpm.diccionario.Tipodocumento;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Casoentidaddeterminacion;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Determinacion;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Documentocaso;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidaddeterminacionregimen;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Regimenespecifico;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Vinculocaso;
import es.mitc.redes.urbanismoenred.servicios.planeamiento.ServicioDiccionariosLocal;
import es.mitc.redes.urbanismoenred.servicios.planeamiento.ServicioPlaneamientoLocal;

/**
 * @author Arnaiz Consultores
 *
 */
@Stateless(name="GET_NODOS_VALORES_APLICACION")
public class GetValoresAplicacion implements IAccion {
	
	@EJB
	private ServicioDiccionariosLocal servicioDiccionario;
	@EJB
	private ServicioPlaneamientoLocal servicioPlaneamiento;

	/* (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.consola.planeamiento.IAccion#ejecutar(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public void ejecutar(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String idCaso = request.getParameter("idCaso");
		String idioma = request.getParameter("idioma");
		String idDet = request.getParameter("idDet");
		
		ResourceBundle traductor = PropertyResourceBundle.getBundle("es.mitc.redes.urbanismoenred.consola.util.Traducciones"
				, new Locale(idioma != null? idioma:"es","ES"));
		if (idCaso != null &&
				idDet != null) {
			response.setContentType("text/xml;charset=UTF-8");
			
			XMLOutputFactory xof = XMLOutputFactory.newInstance();
            XMLStreamWriter xtw;
    		try {
    			xtw = xof.createXMLStreamWriter(response.getWriter());
    			xtw.writeStartDocument("utf-8","1.0");
                xtw.writeStartElement("nodes");
                
                Entidaddeterminacionregimen[] regimenes = servicioPlaneamiento.getRegimenDeCaso(Integer.parseInt(idCaso));
                Determinacion unidad = servicioPlaneamiento.getUnidad(Integer.parseInt(idDet));
                String nombreUnidad = "";
                if (unidad != null) {
                	nombreUnidad = " " + unidad.getNombre();
                }
                xtw.writeStartElement("node");
            	xtw.writeAttribute("text", traductor.getString("valores"));
            	xtw.writeAttribute("tipo", "valorescaso");
                for (int i = 0; i< regimenes.length; i++) {
                	Arbol arbol = new Arbol();
                	for(Regimenespecifico  re: regimenes[i].getRegimenespecificos()) {
                		arbol.addNodo(re);
                	}
                	
                	// Debe tener o valor u opciondeterminacion
                	if (regimenes[i].getValor() != null && !regimenes[i].getValor().isEmpty()) {
                		xtw.writeStartElement("node");
                        xtw.writeAttribute("text", regimenes[i].getValor() != null? regimenes[i].getValor() +", " + nombreUnidad: traductor.getString("valor.NoAsignado"));
                        xtw.writeAttribute("tipo", "regimen");
                        xtw.writeAttribute("idNode", String.valueOf(regimenes[i].getIden()));
                        for (Nodo hijo: arbol.getRaiz().getHijos()) {
        	            	crearArbol(hijo, xtw);
        	            }
                        if (regimenes[i].getCasoentidaddeterminacionByIdcasoaplicacion() != null) {
                    		xtw.writeStartElement("node");
        	            	xtw.writeAttribute("text", (regimenes[i].getDeterminacion() != null? regimenes[i].getDeterminacion().getNombre().trim()+ ": " : "") 
        	            			+ traductor.getString("caso.aplicacion.vinculado") + regimenes[i].getCasoentidaddeterminacionByIdcasoaplicacion().getEntidaddeterminacion().getDeterminacion().getApartado() 
        	            			+" "+regimenes[i].getCasoentidaddeterminacionByIdcasoaplicacion().getEntidaddeterminacion().getDeterminacion().getNombre());
        	            	xtw.writeAttribute("tipo", "determinacion");
        	            	xtw.writeAttribute("caracter", String.valueOf(regimenes[i].getCasoentidaddeterminacionByIdcasoaplicacion().getEntidaddeterminacion().getDeterminacion().getIdcaracter()));
        	            	xtw.writeAttribute("idNode", String.valueOf(regimenes[i].getCasoentidaddeterminacionByIdcasoaplicacion().getEntidaddeterminacion().getDeterminacion().getIden()));
        	            	xtw.writeEndElement();
                    	}
                        xtw.writeEndElement();
                	} else {
                		if (regimenes[i].getOpciondeterminacion() != null) {
                    		xtw.writeStartElement("node");
        	            	xtw.writeAttribute("text", (regimenes[i].getDeterminacion() != null? regimenes[i].getDeterminacion().getNombre().trim()+ ": " : "") 
        	            			+ regimenes[i].getOpciondeterminacion().getDeterminacionByIddeterminacionvalorref().getApartado() 
        	            			+" "+regimenes[i].getOpciondeterminacion().getDeterminacionByIddeterminacionvalorref().getNombre());
        	            	xtw.writeAttribute("tipo", "determinacion");
        	            	xtw.writeAttribute("caracter", String.valueOf(regimenes[i].getOpciondeterminacion().getDeterminacionByIddeterminacionvalorref().getIdcaracter()));
        	            	xtw.writeAttribute("idNode", String.valueOf(regimenes[i].getOpciondeterminacion().getDeterminacionByIddeterminacionvalorref().getIden()));
        	            	for (Nodo hijo: arbol.getRaiz().getHijos()) {
	        	            	crearArbol(hijo, xtw);
	        	            }
        	            	if (regimenes[i].getCasoentidaddeterminacionByIdcasoaplicacion() != null) {
                        		xtw.writeStartElement("node");
            	            	xtw.writeAttribute("text", (regimenes[i].getDeterminacion() != null? regimenes[i].getDeterminacion().getNombre().trim()+ ": " : "") 
            	            			+ regimenes[i].getCasoentidaddeterminacionByIdcasoaplicacion().getEntidaddeterminacion().getDeterminacion().getApartado() 
            	            			+" "+regimenes[i].getCasoentidaddeterminacionByIdcasoaplicacion().getEntidaddeterminacion().getDeterminacion().getNombre());
            	            	xtw.writeAttribute("tipo", "determinacion");
            	            	xtw.writeAttribute("caracter", String.valueOf(regimenes[i].getCasoentidaddeterminacionByIdcasoaplicacion().getEntidaddeterminacion().getDeterminacion().getIdcaracter()));
            	            	xtw.writeAttribute("idNode", String.valueOf(regimenes[i].getCasoentidaddeterminacionByIdcasoaplicacion().getEntidaddeterminacion().getDeterminacion().getIden()));
            	            	xtw.writeEndElement();
                        	}
        	            	xtw.writeEndElement();
                    	} else {
	                		if (regimenes[i].getRegimenespecificos().size() >0) {
		                		xtw.writeStartElement("node");
		                        xtw.writeAttribute("text", traductor.getString("valor.NoAsignado"));
		                        xtw.writeAttribute("tipo", "regimen");
		                        xtw.writeAttribute("idNode", String.valueOf(regimenes[i].getIden()));
		                        for (Nodo hijo: arbol.getRaiz().getHijos()) {
		        	            	crearArbol(hijo, xtw);
		        	            }
		                        if (regimenes[i].getCasoentidaddeterminacionByIdcasoaplicacion() != null) {
		                    		xtw.writeStartElement("node");
		        	            	xtw.writeAttribute("text", (regimenes[i].getDeterminacion() != null? regimenes[i].getDeterminacion().getNombre().trim()+ ": " : "") 
		        	            			+ regimenes[i].getCasoentidaddeterminacionByIdcasoaplicacion().getEntidaddeterminacion().getDeterminacion().getApartado() 
		        	            			+" "+regimenes[i].getCasoentidaddeterminacionByIdcasoaplicacion().getEntidaddeterminacion().getDeterminacion().getNombre());
		        	            	xtw.writeAttribute("tipo", "determinacion");
		        	            	xtw.writeAttribute("caracter", String.valueOf(regimenes[i].getCasoentidaddeterminacionByIdcasoaplicacion().getEntidaddeterminacion().getDeterminacion().getIdcaracter()));
		        	            	xtw.writeAttribute("idNode", String.valueOf(regimenes[i].getCasoentidaddeterminacionByIdcasoaplicacion().getEntidaddeterminacion().getDeterminacion().getIden()));
		        	            	xtw.writeEndElement();
		                    	}
		                        xtw.writeEndElement();
	                		}
                    	}
                	}
                }
                xtw.writeEndElement();
                
                Casoentidaddeterminacion caso = servicioPlaneamiento.get(Casoentidaddeterminacion.class, Integer.parseInt(idCaso));
                
                if (caso != null) {
                	if (caso.getDocumentocasos().size() > 0) {
	                	xtw.writeStartElement("node");
	                	xtw.writeAttribute("text", traductor.getString("documentos"));
	                	xtw.writeAttribute("tipo", "documentoscaso");
	                	for (Documentocaso dc : caso.getDocumentocasos()) {
	                		xtw.writeStartElement("node");
	                    	xtw.writeAttribute("text", dc.getDocumento().getNombre());
	                    	String tipodocumento = null;
	                    	if (dc.getDocumento().getIdtipodocumento() != null) {
	                    		tipodocumento = servicioDiccionario.getTraduccion(Tipodocumento.class, dc.getDocumento().getIdtipodocumento(), idioma);
	                    		xtw.writeAttribute("tipoDocumento", tipodocumento);
	                    	}
	                    	xtw.writeAttribute("nombre", dc.getDocumento().getNombre() + (tipodocumento != null ? "(" +tipodocumento+")":""));
	                    	
	                    	xtw.writeAttribute("comentario", dc.getDocumento().getComentario() != null ? dc.getDocumento().getComentario():"");
	                    	xtw.writeAttribute("tipo", "documento");
	                    	xtw.writeAttribute("idNode", String.valueOf(dc.getDocumento().getIden()));
	                    	xtw.writeEndElement();
	                	}
	                	xtw.writeEndElement();
                	}
                	
                	if (caso.getVinculocasosForIdcaso().size() > 0) {
                		xtw.writeStartElement("node");
                    	xtw.writeAttribute("text", traductor.getString("caso.vinculos"));
                    	xtw.writeAttribute("tipo", "vinculosCaso");
                    	int numCaso = 0;
                    	for (Vinculocaso vinculo : caso.getVinculocasosForIdcaso()) {
                    		xtw.writeStartElement("node");
							xtw.writeAttribute("text", (vinculo.getCasoentidaddeterminacionByIdcasovinculado().getNombre() != null ? vinculo.getCasoentidaddeterminacionByIdcasovinculado().getNombre() : "Caso" + numCaso++)
									+ " (" 
									+ (vinculo.getCasoentidaddeterminacionByIdcasovinculado().getEntidaddeterminacion().getDeterminacion().getApartado() != null ? vinculo.getCasoentidaddeterminacionByIdcasovinculado().getEntidaddeterminacion().getDeterminacion().getApartado(): "")
									+ ", "	
									+ (vinculo.getCasoentidaddeterminacionByIdcasovinculado().getEntidaddeterminacion().getDeterminacion().getNombre() != null ? vinculo.getCasoentidaddeterminacionByIdcasovinculado().getEntidaddeterminacion().getDeterminacion().getNombre(): "")
									+ ")");
        	            	xtw.writeAttribute("tipo", "casoentidaddeterminacion");
        	            	xtw.writeAttribute("idNode", String.valueOf(vinculo.getCasoentidaddeterminacionByIdcasovinculado().getIden()));
        	            	xtw.writeAttribute("load", 
                            		"GestionConsola?wsName=GET_NODOS_VALORES_APLICACION&idCaso=" 
                                					+ vinculo.getCasoentidaddeterminacionByIdcasovinculado().getIden() 
                                					+ "&idioma=" 
                                					+ URLEncoder.encode(idioma,"UTF-8")
                                					+ "&idDet=" + vinculo.getCasoentidaddeterminacionByIdcasovinculado().getEntidaddeterminacion().getDeterminacion().getIden());
        	            	xtw.writeEndElement();
                    	}
                    	xtw.writeEndElement();
                	}
                }
                
                xtw.writeEndElement();
                xtw.writeEndDocument();
               
                response.getWriter().flush();
    		} catch (XMLStreamException e) {
    			response.getWriter().print(traductor.getString("error.xml.generar"));
    		}
        } else {
        	response.getWriter().print(traductor.getString("error.solicitud.incompleta"));
        }
	}
	
	private void crearArbol(Nodo nodo, XMLStreamWriter xtw) throws XMLStreamException {
		xtw.writeStartElement("node");
    	xtw.writeAttribute("text", nodo.getRegimenEspecifico().getNombre() != null ? nodo.getRegimenEspecifico().getNombre() : "");
    	xtw.writeAttribute("texto", nodo.getRegimenEspecifico().getTexto() != null? nodo.getRegimenEspecifico().getTexto() : "");
    	xtw.writeAttribute("tipo", "regimenespecifico");
    	xtw.writeAttribute("idNode", String.valueOf(nodo.getRegimenEspecifico().getIden()));
    	for (Nodo hijo : nodo.getHijos()) {
    		crearArbol(hijo, xtw);
    	}
    	xtw.writeEndElement();
	}
	
	private class Arbol {
		private Map<Integer, Nodo> nodos = new HashMap<Integer, Nodo>();
		private Nodo raiz;
		
		public Arbol() {
			raiz = new Nodo(null);
		}
		
		public Nodo addNodo(Regimenespecifico re) {
			if (!nodos.containsKey(re.getIden())) {
				Nodo nodo = new Nodo(re);
				nodos.put(re.getIden(), nodo);
				if (re.getRegimenespecifico() != null) {
					Nodo padre = addNodo(re.getRegimenespecifico());
					padre.addHijo(nodo);
				} else {
					raiz.addHijo(nodo);
				}
				return nodo;
			} else {
				return nodos.get(re.getIden());
			}
		}

		public Nodo getRaiz() {
			return raiz;
		}
	}
	
	private class Nodo {
		private List<Nodo> hijos = new ArrayList<Nodo>();
		private Regimenespecifico re;
		
		public Nodo(Regimenespecifico re) {
			this.re = re;
		}
		
		public void addHijo(Nodo hijo) {
			hijos.add(hijo);
		}
		
		public List<Nodo> getHijos() {
			return hijos;
		}

		public Regimenespecifico getRegimenEspecifico() {
			return re;
		}
	}

}

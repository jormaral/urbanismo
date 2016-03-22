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
package es.mitc.redes.urbanismoenred.consola.ficha.acciones;

import es.mitc.redes.urbanismoenred.consola.ficha.IAccion;
import es.mitc.redes.urbanismoenred.data.rpm.ficha.Conjuntogrupo;
import es.mitc.redes.urbanismoenred.data.rpm.ficha.Determinacionclasifacto;
import es.mitc.redes.urbanismoenred.data.rpm.ficha.Determinacionclasifuso;
import es.mitc.redes.urbanismoenred.data.rpm.ficha.Regimenesacto;
import es.mitc.redes.urbanismoenred.data.rpm.ficha.Regimenesuso;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Determinacion;
import es.mitc.redes.urbanismoenred.servicios.ficha.ServicioFichaLocal;
import es.mitc.redes.urbanismoenred.servicios.planeamiento.ServicioPlaneamientoLocal;
import java.io.IOException;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

/**
 * @author Arnaiz Consultores
 *
 */
@Stateless(name="GET_GRUPOS_FICHA")
public class GetGruposFicha implements IAccion {
	
	@EJB
	private ServicioFichaLocal servicioFichas;
        @EJB
	private ServicioPlaneamientoLocal servicioPlanes;
    /* (non-Javadoc)
     * @see es.mitc.redes.urbanismoenred.consola.planeamiento.IAccion#ejecutar(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    public void ejecutar(HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        String ficha = request.getParameter("idFicha");

        if (ficha != null) {
            response.setContentType("text/xml;charset=UTF-8");
            int idFicha = Integer.valueOf(ficha);
            XMLOutputFactory xof = XMLOutputFactory.newInstance();
            XMLStreamWriter xtw;
            try {
                xtw = xof.createXMLStreamWriter(response.getWriter());
                xtw.writeStartDocument("utf-8", "1.0");
                xtw.writeStartElement("nodes");
                
                xtw.writeStartElement("node");
                xtw.writeAttribute("text","Capas");
                xtw.writeAttribute("tipo", "capas");
                for (Conjuntogrupo con : servicioFichas.getGruposFromFicha(idFicha)) {
                    xtw.writeStartElement("node");
                    xtw.writeAttribute("text", con.getNombre());
                    xtw.writeAttribute("tipo", "conjuntogrupo");
                    xtw.writeAttribute("idNode", String.valueOf(con.getIden()));
                    xtw.writeAttribute("load", "GestionConsola?wsName=GET_GRUPOS_FROM_CONJUNTO_FICHA&idFicha=" + idFicha + "&idConjunto=" + con.getIden());
                    xtw.writeEndElement();
                }
                xtw.writeEndElement();
                
                xtw.writeStartElement("node");
                xtw.writeAttribute("text","Usos");
                xtw.writeAttribute("tipo", "usos");
                
                xtw.writeStartElement("node");
                Determinacionclasifuso[] detsClasUso=servicioFichas.getDeterminacionesClasificatoriasUso(idFicha);
                xtw.writeAttribute("text","Determinaciones clasificatorias (" + detsClasUso.length + ")");
                xtw.writeAttribute("tipo", "detclasifuso");
                for (Determinacionclasifuso detClas : detsClasUso) {
                    Determinacion det=servicioPlanes.get(Determinacion.class,(int)detClas.getIddeterminacion());
                    xtw.writeStartElement("node");
                    xtw.writeAttribute("text", det.getNombre() + " (" + det.getTramite().getPlan().getNombre() + ")");
                    xtw.writeAttribute("tipo", "determinacionclasifuso");
                    xtw.writeAttribute("idNode", String.valueOf(detClas.getIden()));
                    xtw.writeAttribute("load", "GestionConsola?wsName=GET_VALORES_FICHA_DET_CLASIF_USO&idFicha=" + idFicha + "&idDeterminacion=" + det.getIden());
                    xtw.writeEndElement();
                }
                xtw.writeEndElement();
                
                xtw.writeStartElement("node");
                Regimenesuso[] detsRegimenUso=servicioFichas.getDeterminacionesRegimenUso(idFicha);
                xtw.writeAttribute("text","Otras determinaciones de régimen (" + detsRegimenUso.length + ")");
                xtw.writeAttribute("tipo", "detregimenuso");
                for (Regimenesuso regUso : detsRegimenUso) {
                    Determinacion det=servicioFichas.get(Determinacion.class, (int) regUso.getIddeterminacion());
                    xtw.writeStartElement("node");
                    xtw.writeAttribute("text", det.getNombre() + " (" + det.getTramite().getPlan().getNombre() + ")");
                    xtw.writeAttribute("tipo", "determinacionregimenuso");
                    xtw.writeAttribute("idNode", String.valueOf(regUso.getIden()));
                    xtw.writeEndElement();
                }
                xtw.writeEndElement();
                                
                xtw.writeEndElement();
                
                xtw.writeStartElement("node");
                xtw.writeAttribute("text","Actos");
                xtw.writeAttribute("tipo", "actos");
                
                xtw.writeStartElement("node");
                Determinacionclasifacto[] detsClasActo=servicioFichas.getDeterminacionesClasificatoriasActo(idFicha);
                xtw.writeAttribute("text","Determinaciones clasificatorias (" + detsClasActo.length + ")");
                xtw.writeAttribute("tipo", "detclasifacto");
                for (Determinacionclasifacto detClas : detsClasActo) {
                    Determinacion det=servicioPlanes.get(Determinacion.class,(int)detClas.getIddeterminacion());
                    xtw.writeStartElement("node");
                    xtw.writeAttribute("text", det.getNombre() + " (" + det.getTramite().getPlan().getNombre() + ")");
                    xtw.writeAttribute("tipo", "determinacionclasifacto");
                    xtw.writeAttribute("idNode", String.valueOf(detClas.getIden()));
                    xtw.writeAttribute("load", "GestionConsola?wsName=GET_VALORES_FICHA_DET_CLASIF_ACTO&idFicha=" + idFicha + "&idDeterminacion=" + det.getIden());
                    xtw.writeEndElement();
                }
                xtw.writeEndElement();
                
                xtw.writeStartElement("node");
                Regimenesacto[] detsRegimenActo=servicioFichas.getDeterminacionesRegimenActo(idFicha);
                xtw.writeAttribute("text","Otras determinaciones de régimen (" + detsRegimenActo.length + ")");
                xtw.writeAttribute("tipo", "detregimenacto");
                for (Regimenesacto reg : detsRegimenActo) {
                    Determinacion det=servicioFichas.get(Determinacion.class,(int) reg.getIddeterminacion());
                    xtw.writeStartElement("node");
                    xtw.writeAttribute("text", det.getNombre() + " (" + det.getTramite().getPlan().getNombre() + ")");
                    xtw.writeAttribute("tipo", "determinacionregimenacto");
                    xtw.writeAttribute("idNode", String.valueOf(reg.getIden()));
                    xtw.writeEndElement();
                }
                xtw.writeEndElement();
                
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


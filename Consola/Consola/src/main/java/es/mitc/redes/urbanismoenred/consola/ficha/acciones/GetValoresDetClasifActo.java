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
import es.mitc.redes.urbanismoenred.data.rpm.ficha.Valoresclasifacto;
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
@Stateless(name="GET_VALORES_FICHA_DET_CLASIF_ACTO")
public class GetValoresDetClasifActo implements IAccion {
	
	@EJB
	private ServicioFichaLocal servicioFichas;
        @EJB
	private ServicioPlaneamientoLocal servicioPlan;
    /* (non-Javadoc)
     * @see es.mitc.redes.urbanismoenred.consola.planeamiento.IAccion#ejecutar(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    public void ejecutar(HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        String determinacion = request.getParameter("idDeterminacion");
        String ficha = request.getParameter("idFicha");

        if (determinacion != null && ficha != null) {
            response.setContentType("text/xml;charset=UTF-8");
            int idDeterminacion = Integer.valueOf(determinacion);
            int idFicha = Integer.valueOf(ficha);
            XMLOutputFactory xof = XMLOutputFactory.newInstance();
            XMLStreamWriter xtw;
            try {
                xtw = xof.createXMLStreamWriter(response.getWriter());
                xtw.writeStartDocument("utf-8", "1.0");
                xtw.writeStartElement("nodes");
                
                Valoresclasifacto[] vals=servicioFichas.getValoresFromDetClasifActo(idFicha,idDeterminacion);

                for (Valoresclasifacto val : vals) {
                    Determinacion det = servicioPlan.get(Determinacion.class,(int)val.getIddeterminacionvalorregimen());
                    xtw.writeStartElement("node");
                    xtw.writeAttribute("text", det.getNombre() + " (" + det.getTramite().getPlan().getNombre() + ")");
                    xtw.writeAttribute("tipo", "valordetclasfiacto");
                    xtw.writeAttribute("idNode", String.valueOf(val.getIden()));
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


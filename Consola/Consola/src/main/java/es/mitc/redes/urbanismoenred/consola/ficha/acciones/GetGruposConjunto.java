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
import es.mitc.redes.urbanismoenred.data.rpm.ficha.Conjuntodeterminaciongrupo;
import es.mitc.redes.urbanismoenred.data.rpm.ficha.Grupodeterminacion;
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
@Stateless(name="GET_GRUPOS_FROM_CONJUNTO_FICHA")
public class GetGruposConjunto implements IAccion {
	
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
        String conjunto = request.getParameter("idConjunto");
        String ficha = request.getParameter("idFicha");

        if (conjunto != null && ficha != null) {
            response.setContentType("text/xml;charset=UTF-8");
            int idConjunto = Integer.valueOf(conjunto);
            int idFicha = Integer.valueOf(ficha);
            XMLOutputFactory xof = XMLOutputFactory.newInstance();
            XMLStreamWriter xtw;
            try {
                xtw = xof.createXMLStreamWriter(response.getWriter());
                xtw.writeStartDocument("utf-8", "1.0");
                xtw.writeStartElement("nodes");
                
                Conjuntodeterminaciongrupo[] grps=servicioFichas.getGruposFromConjunto(idConjunto);
                xtw.writeStartElement("node");
                xtw.writeAttribute("text","Grupos de entidad (" + grps.length +")");
                xtw.writeAttribute("tipo", "grupoentidad");
                for (Conjuntodeterminaciongrupo grp : grps) {
                    Determinacion det=servicioPlanes.get(Determinacion.class,(int)grp.getIddeterminacion());
                    xtw.writeStartElement("node");
                    xtw.writeAttribute("text", det.getNombre() + " (" + det.getTramite().getPlan().getNombre() + ")");
                    xtw.writeAttribute("tipo", "grupo");
                    xtw.writeAttribute("idNode", String.valueOf(grp.getIden()));
                    //xtw.writeAttribute("load", "GestionConsola?wsName=GET_GRUPOS_FROM_CONJUNTO_FICHA&idConjunto=" + con.getIden());
                    xtw.writeEndElement();
                }
                xtw.writeEndElement();
                
                Grupodeterminacion[] grpDet=servicioFichas.getGruposDeterminacionFromFichaConjunto(idFicha,idConjunto);
                xtw.writeStartElement("node");
                xtw.writeAttribute("text","Grupos de determinacion (" + grpDet.length +")");
                xtw.writeAttribute("tipo", "grupodeterminacionenunciado");
                for (Grupodeterminacion grp : grpDet) {
                    xtw.writeStartElement("node");
                    xtw.writeAttribute("text", grp.getNombre());
                    xtw.writeAttribute("tipo", "grupodeterminacion");
                    xtw.writeAttribute("idNode", String.valueOf(grp.getIden()));
                    xtw.writeAttribute("load", "GestionConsola?wsName=GET_DETERMINACIONES_FROM_GRUPO_DETERMINACION&idGrupo=" + grp.getIden());
                    xtw.writeEndElement();
                }
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


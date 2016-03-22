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
package es.mitc.redes.urbanismoenred.servicios.refundido;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.apache.log4j.Logger;

import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.RegulacionEspecifica;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Casoentidaddeterminacion;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Determinacion;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Determinaciongrupoentidad;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Documento;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Documentocaso;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Documentodeterminacion;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Documentoentidad;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Documentoshp;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Entidad;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Entidaddeterminacion;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Entidaddeterminacionregimen;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Opciondeterminacion;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Propiedadrelacion;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Regimenespecifico;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Relacion;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Tramite;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Vectorrelacion;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Vinculocaso;
import es.mitc.redes.urbanismoenred.servicios.refundido.ContextoRefundido.LOG;
import es.mitc.redes.urbanismoenred.utils.recursos.textos.Textos;

/**
 * @author Arnaiz Consultores
 *
 */
@Stateless( name = "refundido.generador.fip.stream")
public class GeneradorFipStream implements GeneradorFipLocal {
	
	private static final int idDefCuantia = Integer.parseInt(Textos.getTexto("diccionario", "adscripcion.id_def_cuantia"));
	private static final int idDefEntidadDestino = Integer.parseInt(Textos.getTexto("diccionario", "adscripcion.id_def_destino"));
	private static final int idDefEntidadOrigen = Integer.parseInt(Textos.getTexto("diccionario", "adscripcion.id_def_origen"));
	private static final int idDefPropAdscripcion = Integer.parseInt(Textos.getTexto("diccionario", "adscripcion.id_definicion_adscripcion"));
	private static final int idDefTexto = Integer.parseInt(Textos.getTexto("diccionario", "adscripcion.id_def_texto"));
	private static final int idDefTipo = Integer.parseInt(Textos.getTexto("diccionario", "adscripcion.id_def_tipo"));
	private static final int idDefUnidad = Integer.parseInt(Textos.getTexto("diccionario", "adscripcion.id_def_unidad"));
	
	private static final Logger log = Logger.getLogger(GeneradorFipStream.class);
	
	@PersistenceContext(unitName = "rpmv2")
    private EntityManager em;
	
	private DecimalFormat format4 = new DecimalFormat("000000");
    private DecimalFormat format6 = new DecimalFormat("000000");
    private DecimalFormat format10 = new DecimalFormat("0000000000");
    
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	/**
	 * 
	 * @param caso
	 * @param xtw
	 * @throws XMLStreamException 
	 */
	private void generar(Casoentidaddeterminacion caso, XMLStreamWriter xtw) throws XMLStreamException {
		em.refresh(caso);
		
		xtw.writeStartElement("CASO");
		
		xtw.writeAttribute("codigo", format10.format(caso.getIden()));
		
		if (caso.getNombre() != null && !caso.getNombre().isEmpty()) {
			xtw.writeAttribute("nombre", caso.getNombre());
		} else {
			xtw.writeAttribute("nombre", "-");
		}

        if (caso.getEntidaddeterminacionregimensForIdcaso().size()>0){
        	xtw.writeStartElement("REGIMENES");
            for (Entidaddeterminacionregimen reg : caso.getEntidaddeterminacionregimensForIdcaso()){
                generar(reg, xtw);
            }
            
            xtw.writeEndElement();
        }

        if (caso.getVinculocasosForIdcaso().size()>0){
        	xtw.writeStartElement("VINCULOS");
            for (Vinculocaso vinc : caso.getVinculocasosForIdcaso()){
            	xtw.writeStartElement("VINCULO");
            	xtw.writeAttribute("tramite", vinc.getCasoentidaddeterminacionByIdcasovinculado()
            			.getEntidaddeterminacion()
            			.getEntidad().getTramite().getCodigofip());
            	xtw.writeAttribute("caso", 
            			format10.format(vinc.getCasoentidaddeterminacionByIdcasovinculado().getIden()));
            	xtw.writeEndElement();
                
            }
            xtw.writeEndElement();
        }

        if (caso.getDocumentocasos()!=null &&  caso.getDocumentocasos().size()>0){
        	xtw.writeStartElement("DOCUMENTOS");
            for (Documentocaso doc: caso.getDocumentocasos()){
            	xtw.writeStartElement("DOCUMENTO");
            	xtw.writeAttribute("codigo", format10.format(doc.getDocumento().getIden()));
            	xtw.writeEndElement();
            }
            xtw.writeEndElement();
        }
        
        xtw.writeEndElement();
	}

	/**
	 * 
	 * @param determinacion
	 * @param xtw
	 * @throws XMLStreamException
	 */
	@SuppressWarnings("unchecked")
	private void generar(Determinacion determinacion, XMLStreamWriter xtw) throws XMLStreamException {
		em.refresh(determinacion);
		xtw.writeStartElement("DETERMINACION");
		
		xtw.writeAttribute("apartado", determinacion.getApartado());
		xtw.writeAttribute("caracter", format6.format(determinacion.getIdcaracter()));
		xtw.writeAttribute("codigo", determinacion.getCodigo());
		xtw.writeAttribute("nombre", determinacion.getNombre());
		if (determinacion.getEtiqueta() != null) {
			xtw.writeAttribute("etiqueta", determinacion.getEtiqueta());
		}
		xtw.writeAttribute("suspendida", Boolean.toString(determinacion.isBsuspendida()));
		
		if (determinacion.getTexto() != null && !determinacion.getTexto().isEmpty()) {
			xtw.writeStartElement("TEXTO");
			xtw.writeCharacters(determinacion.getTexto());
			xtw.writeEndElement();
		}

        if (determinacion.getOpciondeterminacionsForIddeterminacion().size()>0){
        	xtw.writeStartElement("VALORES-REFERENCIA");
            for (Opciondeterminacion opc:determinacion.getOpciondeterminacionsForIddeterminacion()) {
            	xtw.writeStartElement("VALOR-REFERENCIA");
            	xtw.writeAttribute("tramite", opc.getDeterminacionByIddeterminacionvalorref().getTramite().getCodigofip());
            	xtw.writeAttribute("determinacion", opc.getDeterminacionByIddeterminacionvalorref().getCodigo());
                xtw.writeEndElement();
            }
            xtw.writeEndElement();
        }

        List<Determinacion> hijas = em.createNamedQuery("refundido.Determinacion.obtenerHijas")
        		.setParameter("idPadre", determinacion.getIden())
        		.getResultList();
        if (hijas.size()>0){
        	xtw.writeStartElement("HIJAS");
            for(Determinacion detHija : hijas){
                generar(detHija, xtw);
            }
            xtw.writeEndElement();
        }

        if (determinacion.getDocumentodeterminacions()!=null && determinacion.getDocumentodeterminacions().size()>0){
        	xtw.writeStartElement("DOCUMENTOS");
            for (Documentodeterminacion doc : determinacion.getDocumentodeterminacions()){
            	xtw.writeStartElement("DOCUMENTO");
            	xtw.writeAttribute("codigo", format10.format(doc.getDocumento().getIden()));
                xtw.writeEndElement();
            }
            xtw.writeEndElement();
        }
        
        Determinacion unidad = getUnidad(determinacion.getIden());
        if (unidad!=null){
        	xtw.writeStartElement("UNIDAD");
        	xtw.writeAttribute("tramite", unidad.getTramite().getCodigofip());
        	xtw.writeAttribute("determinacion", unidad.getCodigo());
            xtw.writeEndElement();
        }
        
        RegulacionEspecifica[] regulacionesEspecificas = getRegulacionesEspecificas(determinacion.getIden());
        List<Determinacion> determinacionesReguladoras = em.createNamedQuery("refundido.Determinacion.obtenerReguladoras").setParameter("iden", determinacion.getIden()).getResultList();

        if (regulacionesEspecificas.length > 0 || determinacionesReguladoras.size() > 0){
        	xtw.writeStartElement("REGULACION");
            if (determinacionesReguladoras.size() >0){
            	xtw.writeStartElement("DETERMINACIONES-REGULADORAS");
                for (Determinacion detReg : determinacionesReguladoras){
                	xtw.writeStartElement("DETERMINACION-REGULADORA");
                	xtw.writeAttribute("tramite", detReg.getTramite().getCodigofip());
                	xtw.writeAttribute("determinacion", detReg.getCodigo());
                    xtw.writeEndElement();
                }
                xtw.writeEndElement();
            }
            if (regulacionesEspecificas.length > 0){
            	xtw.writeStartElement("REGULACIONES-ESPECIFICAS");
                for (RegulacionEspecifica regEsp:regulacionesEspecificas){
                    generar(regEsp, xtw);
                }
                xtw.writeEndElement();
            }
            xtw.writeEndElement();
        }
        
        if (determinacion.getDeterminacionByIddeterminacionbase()!=null){
            xtw.writeStartElement("BASE");
        	xtw.writeAttribute("tramite", determinacion.getDeterminacionByIddeterminacionbase().getTramite().getCodigofip());
        	xtw.writeAttribute("determinacion", determinacion.getDeterminacionByIddeterminacionbase().getCodigo());
            xtw.writeEndElement();
        }
        
        if (determinacion.getDeterminaciongrupoentidadsForIddeterminacion().size()>0){
        	xtw.writeStartElement("GRUPOS-APLICACION");
            for (Determinaciongrupoentidad grp: determinacion.getDeterminaciongrupoentidadsForIddeterminacion()){
            	xtw.writeStartElement("GRUPO-APLICACION");
            	xtw.writeAttribute("tramite", grp.getDeterminacionByIddeterminaciongrupo().getTramite().getCodigofip());
            	xtw.writeAttribute("determinacion", grp.getDeterminacionByIddeterminaciongrupo().getCodigo());
                xtw.writeEndElement();
            }
            xtw.writeEndElement();
        }
        
        xtw.writeEndElement();
	}
	
	/**
	 * 
	 * @param documento
	 * @param xtw 
	 * @throws XMLStreamException 
	 */
	private void generar(Documento documento, XMLStreamWriter xtw) throws XMLStreamException {
		xtw.writeStartElement("DOCUMENTO");
		
		if (documento.getIdgrupodocumento()!= null){
			xtw.writeAttribute("grupo", format6.format(documento.getIdgrupodocumento()));
        }
        
		xtw.writeAttribute("codigo", format10.format(documento.getIden()));
		xtw.writeAttribute("archivo", documento.getArchivo());
		xtw.writeAttribute("nombre", documento.getNombre());
		xtw.writeAttribute("tipo", format6.format(documento.getIdtipodocumento()));
		if (documento.getEscala() != null){
			xtw.writeAttribute("tipo", documento.getEscala().toString());
       	}
     
		if (documento.getComentario() != null && !documento.getComentario().isEmpty()) {
			xtw.writeStartElement("COMENTARIO");
			xtw.writeCharacters(documento.getComentario());
			xtw.writeEndElement();
		}
       
        if (documento.getDocumentoshps().size()>0){
        	xtw.writeStartElement("HOJAS");
            for(Documentoshp shp : documento.getDocumentoshps()){
                generar(shp, xtw);
            }
            xtw.writeEndElement();
        }
        xtw.writeEndElement();
	}

	/**
	 * 
	 * @param shp
	 * @param xtw
	 * @throws XMLStreamException 
	 */
	private void generar(Documentoshp shp, XMLStreamWriter xtw) throws XMLStreamException {
		xtw.writeStartElement("HOJA");
		xtw.writeAttribute("nombre", shp.getHoja());
		xtw.writeStartElement("GEOMETRIA");
		xtw.writeCharacters(shp.getGeom());
		xtw.writeEndElement();
		xtw.writeEndElement();
	}
	
	/**
	 * 
	 * @param entidad
	 * @param xtw
	 * @throws XMLStreamException 
	 */
	@SuppressWarnings("unchecked")
	private void generar(Entidad entidad, XMLStreamWriter xtw) throws XMLStreamException {
		em.refresh(entidad);
		
		xtw.writeStartElement("ENTIDAD");
		
		xtw.writeAttribute("codigo", entidad.getCodigo());
		if (entidad.getEtiqueta() != null) {
			xtw.writeAttribute("etiqueta", entidad.getEtiqueta());
		}
		xtw.writeAttribute("clave", entidad.getClave());
		xtw.writeAttribute("nombre", entidad.getNombre());
		xtw.writeAttribute("suspendida", Boolean.toString(entidad.isBsuspendida()));

        if (entidad.getEntidadpols().size() > 0) {
        	xtw.writeStartElement("GEOMETRIA");
        	xtw.writeCharacters(entidad.getEntidadpols().iterator().next().getGeom());
        	xtw.writeEndElement();
        } else if (entidad.getEntidadlins().size() > 0) {
        	xtw.writeStartElement("GEOMETRIA");
        	xtw.writeCharacters(entidad.getEntidadlins().iterator().next().getGeom());
        	xtw.writeEndElement();
        } else if (entidad.getEntidadpnts().size() > 0) {
        	xtw.writeStartElement("GEOMETRIA");
        	xtw.writeCharacters(entidad.getEntidadpnts().iterator().next().getGeom());
        	xtw.writeEndElement();
        }

        List<Entidad> hijas = em.createNamedQuery("refundido.Entidad.obtenerHijas")
        		.setParameter("idPadre", entidad.getIden())
        		.getResultList();
        
        if (hijas.size() > 0) {
            xtw.writeStartElement("HIJAS");
            for (Entidad entHija : hijas) {
                generar(entHija,xtw);
            }
            xtw.writeEndElement();
        }

        if (entidad.getDocumentoentidads()!=null && entidad.getDocumentoentidads().size() > 0) {
        	xtw.writeStartElement("DOCUMENTOS");
            for (Documentoentidad doc : entidad.getDocumentoentidads()) {
            	xtw.writeStartElement("DOCUMENTO");
            	xtw.writeAttribute("codigo", format10.format(doc.getDocumento().getIden()));
                xtw.writeEndElement();
            }
            xtw.writeEndElement();
        }

        if (entidad.getEntidadByIdentidadbase() != null) {
        	xtw.writeStartElement("BASE");
        	xtw.writeAttribute("tramite", entidad.getEntidadByIdentidadbase().getTramite().getCodigofip());
        	xtw.writeAttribute("entidad", entidad.getEntidadByIdentidadbase().getCodigo());
        	xtw.writeEndElement();
        }
        
        xtw.writeEndElement();
	}
	
	/**
	 * 
	 * @param ed
	 * @param xtw
	 * @throws XMLStreamException 
	 */
	private void generar(Entidaddeterminacion ed, XMLStreamWriter xtw) throws XMLStreamException {
		em.refresh(ed);
		
		xtw.writeStartElement("CONDICION-URBANISTICA");
		
		xtw.writeStartElement("CODIGO");
		xtw.writeStartElement("ENTIDAD");
		xtw.writeAttribute("tramite", ed.getEntidad().getTramite().getCodigofip());
    	xtw.writeAttribute("entidad", ed.getEntidad().getCodigo());
		xtw.writeEndElement();
		xtw.writeStartElement("DETERMINACION");
		xtw.writeAttribute("tramite", ed.getDeterminacion().getTramite().getCodigofip());
    	xtw.writeAttribute("determinacion", ed.getDeterminacion().getCodigo());
		xtw.writeEndElement();
		xtw.writeEndElement();
        
		xtw.writeStartElement("CASOS");
		
        for(Casoentidaddeterminacion caso : ed.getCasoentidaddeterminacions()){
           generar(caso, xtw);
        }
        xtw.writeEndElement();

        xtw.writeEndElement();
	}

	/**
	 * 
	 * @param reg
	 * @param xtw
	 * @throws XMLStreamException 
	 */
	private void generar(Entidaddeterminacionregimen edr, XMLStreamWriter xtw) throws XMLStreamException {
		em.refresh(edr);
		
		xtw.writeStartElement("REGIMEN");
		
        if (edr.getSuperposicion()!=null){
        	xtw.writeAttribute("superposicion", edr.getSuperposicion().toString());
        } else {
        	xtw.writeAttribute("superposicion", Integer.toString(0));
        }

		if (edr.getRegimenespecificos().size()>0){
			xtw.writeStartElement("REGIMENES-ESPECIFICOS");
            for (Regimenespecifico reg: edr.getRegimenespecificos()){
            	// Sólo los padres
            	if (reg.getRegimenespecifico() == null) {
            		generar(reg, xtw);
            	}
            }
            xtw.writeEndElement();
        }
		
        if (edr.getValor()!=null && !edr.getValor().trim().isEmpty()){
        	xtw.writeStartElement("VALOR");
        	xtw.writeCharacters(edr.getValor());
        	xtw.writeEndElement();
        } else if (edr.getOpciondeterminacion()!= null) {
        	xtw.writeStartElement("VALOR-REFERENCIA");
        	xtw.writeAttribute("tramite", edr.getOpciondeterminacion()
        			.getDeterminacionByIddeterminacionvalorref().getTramite().getCodigofip());
        	xtw.writeAttribute("determinacion", edr.getOpciondeterminacion()
        			.getDeterminacionByIddeterminacionvalorref().getCodigo());
        	xtw.writeEndElement();
        }

        if (edr.getDeterminacion()!=null ){
        	xtw.writeStartElement("DETERMINACION-REGIMEN");
        	xtw.writeAttribute("tramite", edr.getDeterminacion()
        			.getTramite().getCodigofip());
        	xtw.writeAttribute("determinacion", edr.getDeterminacion()
        			.getCodigo());
        	xtw.writeEndElement();
        }

        if (edr.getCasoentidaddeterminacionByIdcasoaplicacion()!=null ){
        	xtw.writeStartElement("CASO-APLICACION");
        	xtw.writeAttribute("tramite", edr.getCasoentidaddeterminacionByIdcasoaplicacion()
        			.getEntidaddeterminacion()
        			.getEntidad().getTramite().getCodigofip());
        	xtw.writeAttribute("caso", 
        			format10.format(edr.getCasoentidaddeterminacionByIdcasoaplicacion().getIden()));
        	xtw.writeEndElement();
        }
        
        xtw.writeEndElement();
	}

	/**
	 * 
	 * @param aAdscr
	 * @param xtw
	 * @throws XMLStreamException 
	 */
	private void generar(PropiedadesAdscripcion propiedadesAdscripcion, XMLStreamWriter xtw) throws XMLStreamException {
		xtw.writeStartElement("PROPIEDADES");
        
		xtw.writeAttribute("cuantia", Double.toString(propiedadesAdscripcion.getCuantia()));
        
		if (propiedadesAdscripcion.getUnidad() != null) {
			xtw.writeStartElement("UNIDAD");
			xtw.writeAttribute("tramite", propiedadesAdscripcion.getUnidad().getTramite().getCodigofip());
        	xtw.writeAttribute("determinacion", propiedadesAdscripcion.getUnidad().getCodigo());
        	xtw.writeEndElement();
        }

        if (propiedadesAdscripcion.getTipo() != null) {
        	xtw.writeStartElement("TIPO");
			xtw.writeAttribute("tramite", propiedadesAdscripcion.getTipo().getTramite().getCodigofip());
        	xtw.writeAttribute("determinacion", propiedadesAdscripcion.getTipo().getCodigo());
        	xtw.writeEndElement();
        }

        if (propiedadesAdscripcion.getTexto() != null && !propiedadesAdscripcion.getTexto().isEmpty()) {
        	xtw.writeStartElement("TEXTO");
        	xtw.writeCharacters(propiedadesAdscripcion.getTexto());
        	xtw.writeEndElement();
        }
        
        xtw.writeEndElement();
	}

	/**
	 * 
	 * @param reg
	 * @param xtw
	 * @throws XMLStreamException 
	 */
	private void generar(Regimenespecifico regesp, XMLStreamWriter xtw) throws XMLStreamException {
		em.refresh(regesp);
		
		xtw.writeStartElement("REGIMEN-ESPECIFICO");
        
		xtw.writeAttribute("orden", Integer.toString(regesp.getOrden()));
		xtw.writeAttribute("nombre", regesp.getNombre());
        if (regesp.getTexto() != null) {
        	xtw.writeStartElement("TEXTO");
    		xtw.writeCharacters(regesp.getTexto());
    		xtw.writeEndElement();
        }
		
        if (regesp.getRegimenespecificos().size()>0){
        	xtw.writeStartElement("HIJOS");
            for(Regimenespecifico regHijo : regesp.getRegimenespecificos()){
                generar(regHijo, xtw);
            }
            xtw.writeEndElement();
        }
        
        xtw.writeEndElement();
	}

	/**
	 * 
	 * @param regEsp
	 * @param xtw
	 * @throws XMLStreamException 
	 */
	private void generar(RegulacionEspecifica regulacion, XMLStreamWriter xtw) throws XMLStreamException {
		xtw.writeStartElement("REGULACION-ESPECIFICA");
		xtw.writeAttribute("nombre", regulacion.getNombre());
		xtw.writeAttribute("orden", Integer.toString(regulacion.getOrden()));
		
		xtw.writeStartElement("TEXTO");
		xtw.writeCharacters(regulacion.getTexto());
		xtw.writeEndElement();

        if (regulacion.getRegulacionesespecificas().size()>0){
        	xtw.writeStartElement("HIJAS");
            for(RegulacionEspecifica regHija : regulacion.getRegulacionesespecificas()){
                generar(regHija, xtw);
            }
            xtw.writeEndElement();
        }
        
        xtw.writeEndElement();
	}

	/*
	 * (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.servicios.refundido.GeneradorFipLocal#generarFIP2(es.mitc.redes.urbanismoenred.servicios.refundido.ContextoRefundido, java.io.File)
	 */
	@Override
	public void generarFIP2(ContextoRefundido contexto, File destino)
			throws ExcepcionRefundido {
		XMLOutputFactory xof = XMLOutputFactory.newInstance();
        XMLStreamWriter xtw = null;
        FileOutputStream fos = null;
		try {
			em.flush();
			fos = new FileOutputStream(destino);
			
			xtw = xof.createXMLStreamWriter(fos, "UTF-8");
			xtw.writeStartDocument("utf-8","1.0");
            xtw.writeStartElement("FIP");
            xtw.writeAttribute("FECHA", sdf.format(GregorianCalendar.getInstance().getTime()));
            xtw.writeAttribute("PAIS", Textos.getTexto("consola", "fip.pais"));
            xtw.writeAttribute("SRS", Textos.getTexto("consola", "projection"));
            xtw.writeAttribute("VERSION", Textos.getTexto("consola", "fip.version"));
            
            generarTramite(contexto, xtw);
            
            xtw.writeEndElement();
            xtw.writeEndDocument();
            xtw.flush();
		} catch (XMLStreamException e) {
			ResourceBundle traductor = (ResourceBundle) contexto.getParametro(ContextoRefundido.TRADUCTOR);
			throw new ExcepcionRefundido(String.format(traductor.getString("refundido.generar.fip.error.xml"), e.getMessage()), 4008);
		} catch (IOException e) {
			ResourceBundle traductor = (ResourceBundle) contexto.getParametro(ContextoRefundido.TRADUCTOR);
			throw new ExcepcionRefundido(String.format(traductor.getString("refundido.generar.fip.error.archivo"), e.getMessage()), 4009);
		} finally {
			if (xtw != null) {
				try {
					xtw.close();
				} catch (XMLStreamException e) {
					log.warn("Error al cerrar el flujo de escritura del XML: " + e.getMessage());
				}
			}
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					log.warn("Error al cerrar el flujo de escritura del archivo: " + e.getMessage());
				}
			}
		}
	}
	
	/**
     * 
     * @param relaciones
     * @return
     */
    private PropiedadesAdscripcion[] generarPropiedadesAdscripcion(List<Relacion> relaciones) {
        PropiedadesAdscripcion propiedades;
        List<PropiedadesAdscripcion> lista = new ArrayList<PropiedadesAdscripcion>();
        for (Relacion relacion : relaciones) {
            propiedades = new PropiedadesAdscripcion();
            
            for (Propiedadrelacion propiedad : relacion.getPropiedadrelacions()) {
            	if (idDefCuantia == propiedad.getIddefpropiedad()) {
            		propiedades.setCuantia(Double.parseDouble(propiedad.getValor()));
            	} else if (idDefTexto == propiedad.getIddefpropiedad()) {
            		propiedades.setTexto(propiedad.getValor());
            	}
            }
            
            for (Vectorrelacion vector : relacion.getVectorrelacions()) {
            	if (idDefEntidadDestino == vector.getIddefvector()) {
            		propiedades.setDestino(em.find(Entidad.class, vector.getValor()));
            	} else if (idDefEntidadOrigen == vector.getIddefvector()) {
            		propiedades.setOrigen(em.find(Entidad.class, vector.getValor()));
            	} else if (idDefTipo == vector.getIddefvector()) {
            		propiedades.setTipo(em.find(Determinacion.class, vector.getValor()));
            	} else if (idDefUnidad == vector.getIddefvector()) {
            		propiedades.setUnidad(em.find(Determinacion.class, vector.getValor()));
            	}
            }

            propiedades.setIden(relacion.getIden());

            lista.add(propiedades);
        }
        return lista.toArray(new PropiedadesAdscripcion[0]);
    }

    /**
     * 
     * @param contexto
     * @param xtw
     * @throws XMLStreamException
     */
	@SuppressWarnings("unchecked")
	private void generarTramite(ContextoRefundido contexto, XMLStreamWriter xtw) throws XMLStreamException {
		
		Tramite tramite = (Tramite) contexto.getParametro(ContextoRefundido.TRAMITE_REFUNDIDO);
		em.refresh(tramite);
		
		xtw.writeStartElement("TRAMITE");
		xtw.writeAttribute("codigo", tramite.getCodigofip());
		xtw.writeAttribute("tipoTramite", format4.format(tramite.getIdtipotramite()));
        
        if (tramite.getTexto() != null && !tramite.getTexto().isEmpty()) {
        	xtw.writeStartElement("TEXTO");
        	xtw.writeCharacters(tramite.getTexto());
        	xtw.writeEndElement();
        }
        
        // Se ha optado por no incluir los documentos del trámite.
        List<Documento> documentos = em.createNamedQuery("refundido.Documento.obtenerNoTramite")
        		.setParameter("idTramite", tramite.getIden())
        		.getResultList();
        contexto.logTraducido(LOG.INFO, "refundido.generar.fip.documentos");
        if (documentos.size() > 0) {
        	xtw.writeStartElement("DOCUMENTOS");
        	// Se ha optado por no incluir los documentos del trámite.
            for (Documento documento : documentos) {
            	generar(documento, xtw);
            }
            
            xtw.writeEndElement();
        }

        List<Entidad> entidades = em.createNamedQuery("refundido.Entidad.obtenerRaiz")
        		.setParameter("idTramite", tramite.getIden())
        		.getResultList();
        contexto.logTraducido(LOG.INFO, "refundido.generar.fip.entidades");
        xtw.writeStartElement("ENTIDADES");
        if (entidades.size() > 0) {
            for (Entidad entidad : entidades) {
                generar(entidad, xtw);
            }
        }
        xtw.writeEndElement();

        List<Determinacion> determinaciones =em.createNamedQuery("refundido.Determinacion.obtenerRaiz")
        		.setParameter("idTramite", tramite.getIden())
        		.getResultList();
        contexto.logTraducido(LOG.INFO, "refundido.generar.fip.determinaciones");
        xtw.writeStartElement("DETERMINACIONES");
        if (determinaciones.size() > 0) {
            for (Determinacion determinacion : determinaciones) {
               generar(determinacion, xtw);
            }
        }
        xtw.writeEndElement();

        List<Entidaddeterminacion> condiciones = em.createNamedQuery("refundido.Entidaddeterminacion.obtenerCondicionesUrbanisticas")
        		.setParameter("idTramite", tramite.getIden())
        		.getResultList();
        
        //Condiciones Urbanísticas
        if (condiciones.size() > 0) {
        	contexto.logTraducido(LOG.INFO, "refundido.generar.fip.condicionesUrbanisticas");
        	xtw.writeStartElement("CONDICIONES-URBANISTICAS");
            
            for (Entidaddeterminacion ed : condiciones) {
            	generar(ed, xtw);
            }
            
            xtw.writeEndElement();
        }
    
        //Unidades
        determinaciones = em.createNamedQuery("refundido.Determinacion.obtenerUnidadesTramite")
        		.setParameter("idTramite", tramite.getIden())
        		.getResultList();
        if (determinaciones.size() > 0) {
        	contexto.logTraducido(LOG.INFO, "refundido.generar.fip.unidades");
        	xtw.writeStartElement("UNIDADES");
            for (Determinacion determinacion : determinaciones) {
            	generarUnidad(determinacion, xtw);
            }
            xtw.writeEndElement();
        }
        
        String origen = "";
        String destino = "";
        PropiedadesAdscripcion[] adscripciones = getPropiedadesAdscripcionTramite(tramite.getIden());
        if (adscripciones.length > 0) {
        	contexto.logTraducido(LOG.INFO, "refundido.generar.fip.adscripciones");
        	
        	xtw.writeStartElement("ADSCRIPCIONES");
            
            for (PropiedadesAdscripcion aAdscr : adscripciones) {
            	xtw.writeStartElement("ADSCRIPCION");
                
                origen = aAdscr.getOrigen().getCodigo();
                destino = aAdscr.getDestino().getCodigo();
                if (origen != null) {
                	xtw.writeAttribute("entidadOrigen", origen);
                }
                
                if (destino != null) {
                	xtw.writeAttribute("entidadDestino", destino);
                }
                
                generar(aAdscr, xtw);
                
                xtw.writeEndElement();
            }
            
            xtw.writeEndElement();
        }
        
        xtw.writeEndElement();
	}
	
	/**
	 * 
	 * @param propiedades
	 * @return
	 * @throws XMLStreamException 
	 */
    @SuppressWarnings("unchecked")
	protected void generarUnidad(Determinacion determinacion, XMLStreamWriter xtw) throws XMLStreamException {
    	
    	List<Relacion> relaciones = em.createNamedQuery("refundido.Relacion.obtenerPropiedadesUnidad")
    			.setParameter("idDeterminacion", determinacion.getIden())
    			.getResultList();
    	
    	Integer idDefAbreviatura = Integer.parseInt(Textos.getTexto("diccionario", "unidad.id_abreviatura"));
        Integer idDefDefinicion = Integer.parseInt(Textos.getTexto("diccionario", "unidad.id_definicion"));

        xtw.writeStartElement("UNIDAD");
		
        xtw.writeAttribute("determinacion", determinacion.getCodigo());
        
		for (Propiedadrelacion propiedad : relaciones.get(0).getPropiedadrelacions()) {
			if (propiedad.getIddefpropiedad() == idDefAbreviatura) {
				xtw.writeAttribute("abreviatura", propiedad.getValor());
			}
		}
		
		for (Propiedadrelacion propiedad : relaciones.get(0).getPropiedadrelacions()) {
			if (propiedad.getIddefpropiedad() == idDefDefinicion) {
				xtw.writeStartElement("DEFINICION");
				xtw.writeCharacters(propiedad.getValor());
				xtw.writeEndElement();
			}
		}
		
		xtw.writeEndElement();
	}

	/**
     * 
     * @param idTramite
     * @return
     */
    @SuppressWarnings("unchecked")
	private PropiedadesAdscripcion[] getPropiedadesAdscripcionTramite(
            int idTramite) {
        List<Relacion> relaciones = em.createNamedQuery("refundido.Relacion.obtenerAdscripcionesTramite")
        		.setParameter("idTramite", idTramite)
        		.setParameter("idDefEntidadOrigen", idDefEntidadOrigen)
        		.setParameter("idDefEntidadDestino", idDefEntidadDestino)
        		.setParameter("idDefPropAdscripcion", idDefPropAdscripcion)
        		.getResultList();

        return generarPropiedadesAdscripcion(relaciones);
    }

	/**
	 * 
	 * @param identificador
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private RegulacionEspecifica[] getRegulacionesEspecificas(int identificador) {
        Determinacion determinacion = em.find(Determinacion.class, identificador);

        List<RegulacionEspecifica> regulaciones = new ArrayList<RegulacionEspecifica>();

        if (determinacion != null) {

        	Query consulta = em.createNamedQuery("refundido.Relacion.obtenerRegulacionesEspecificasPorPadre");
        	
            List<Object[]> resultados = consulta.setParameter("iden", determinacion.getIden())
            		.setParameter("padre", 0)
            		.getResultList();

            RegulacionEspecifica regulacion;

            Map<Object, RegulacionEspecifica> res = new HashMap<Object, RegulacionEspecifica>();
            HashSet<RegulacionEspecifica> regHijas;
            for (Object[] registro : resultados) {
                if (!res.containsKey(registro[0])) {
                    regulacion = new RegulacionEspecifica(Integer.parseInt(registro[0].toString()),
                            Integer.parseInt(registro[1].toString()),
                            registro[2].toString(),
                            registro[3].toString());
                    regHijas = new HashSet<RegulacionEspecifica>();
                    List<Object[]> hijas = consulta.setParameter("iden", regulacion.getIden())
                    		.setParameter("padre", determinacion.getIden())
                    		.getResultList();
                    for (Object[] hija : hijas) {
                        regHijas.add(new RegulacionEspecifica(Integer.parseInt(hija[0].toString()),
                                Integer.parseInt(hija[1].toString()),
                                hija[2].toString(),
                                hija[3].toString()));
                    }
                    regulacion.setRegulacionesespecificas(regHijas);
                    regulaciones.add(regulacion);
                }
            }
        }
        return regulaciones.toArray(new RegulacionEspecifica[0]);
    }

	/**
	 * 
	 * @param idDeterminacion
	 * @return
	 */
	private Determinacion getUnidad(int idDeterminacion) {
        @SuppressWarnings("unchecked")
        List<Object> resultados = em.createNamedQuery("refundido.Vectorrelacion.obtenerUnidades")
        	.setParameter("idDeterminacion", idDeterminacion)
        	.getResultList();
        if (resultados.size() > 0) {
            return em.find(Determinacion.class, Integer.parseInt(resultados.get(0).toString()));
        }
        return null;
    }

}

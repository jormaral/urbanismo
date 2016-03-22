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
import java.math.BigInteger;
import java.text.DecimalFormat;
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
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;

import org.apache.log4j.Logger;

import es.mitc.redes.urbanismoenred.data.fip.Adscripcion;
import es.mitc.redes.urbanismoenred.data.fip.Caso;
import es.mitc.redes.urbanismoenred.data.fip.Caso.DOCUMENTOS.DOCUMENTO;
import es.mitc.redes.urbanismoenred.data.fip.Caso.REGIMENES;
import es.mitc.redes.urbanismoenred.data.fip.Caso.VINCULOS;
import es.mitc.redes.urbanismoenred.data.fip.CodigoCaso;
import es.mitc.redes.urbanismoenred.data.fip.CodigoCondicionUrbanistica;
import es.mitc.redes.urbanismoenred.data.fip.CodigoDeterminacion;
import es.mitc.redes.urbanismoenred.data.fip.CodigoEntidad;
import es.mitc.redes.urbanismoenred.data.fip.CondicionUrbanistica;
import es.mitc.redes.urbanismoenred.data.fip.CondicionUrbanistica.CASOS;
import es.mitc.redes.urbanismoenred.data.fip.Determinacion.GRUPOSAPLICACION;
import es.mitc.redes.urbanismoenred.data.fip.Determinacion.HIJAS;
import es.mitc.redes.urbanismoenred.data.fip.Determinacion.REGULACION;
import es.mitc.redes.urbanismoenred.data.fip.Determinacion.REGULACION.DETERMINACIONESREGULADORAS;
import es.mitc.redes.urbanismoenred.data.fip.Determinacion.REGULACION.REGULACIONESESPECIFICAS;
import es.mitc.redes.urbanismoenred.data.fip.Determinacion.UNIDAD;
import es.mitc.redes.urbanismoenred.data.fip.Determinacion.VALORESREFERENCIA;
import es.mitc.redes.urbanismoenred.data.fip.Documento.HOJAS;
import es.mitc.redes.urbanismoenred.data.fip.FIP;
import es.mitc.redes.urbanismoenred.data.fip.Hoja;
import es.mitc.redes.urbanismoenred.data.fip.OperacionDeterminacion;
import es.mitc.redes.urbanismoenred.data.fip.OperacionEntidad;
import es.mitc.redes.urbanismoenred.data.fip.Regimen;
import es.mitc.redes.urbanismoenred.data.fip.Regimen.REGIMENESESPECIFICOS;
import es.mitc.redes.urbanismoenred.data.fip.RegimenEspecifico;
import es.mitc.redes.urbanismoenred.data.fip.RegimenEspecifico.HIJOS;
import es.mitc.redes.urbanismoenred.data.fip.Tramite.ADSCRIPCIONES;
import es.mitc.redes.urbanismoenred.data.fip.Tramite.CONDICIONESURBANISTICAS;
import es.mitc.redes.urbanismoenred.data.fip.Tramite.DETERMINACIONES;
import es.mitc.redes.urbanismoenred.data.fip.Tramite.DOCUMENTOS;
import es.mitc.redes.urbanismoenred.data.fip.Tramite.ENTIDADES;
import es.mitc.redes.urbanismoenred.data.fip.Tramite.UNIDADES;
import es.mitc.redes.urbanismoenred.data.fip.Unidad;
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
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Operaciondeterminacion;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Operacionentidad;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Operacionrelacion;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Propiedadrelacion;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Regimenespecifico;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Relacion;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Tramite;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Vectorrelacion;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Vinculocaso;
import es.mitc.redes.urbanismoenred.utils.excepciones.RedesException;
import es.mitc.redes.urbanismoenred.utils.recursos.textos.Textos;

/**
 * 
 * @author Arnaiz Consultores
 *
 */
@Stateless (name = "refundido.generador.fip")
public class GeneradorFip implements GeneradorFipLocal {
	
	private static final int idDefCuantia = Integer.parseInt(Textos.getTexto("diccionario", "adscripcion.id_def_cuantia"));
	private static final int idDefEntidadDestino = Integer.parseInt(Textos.getTexto("diccionario", "adscripcion.id_def_destino"));
	private static final int idDefEntidadOrigen = Integer.parseInt(Textos.getTexto("diccionario", "adscripcion.id_def_origen"));
	private static final int idDefPropAdscripcion = Integer.parseInt(Textos.getTexto("diccionario", "adscripcion.id_definicion_adscripcion"));
	private static final int idDefTexto = Integer.parseInt(Textos.getTexto("diccionario", "adscripcion.id_def_texto"));
	private static final int idDefTipo = Integer.parseInt(Textos.getTexto("diccionario", "adscripcion.id_def_tipo"));
	private static final int idDefUnidad = Integer.parseInt(Textos.getTexto("diccionario", "adscripcion.id_def_unidad"));
	
	protected static final Logger log = Logger.getLogger(GeneradorFip.class);
	
	@PersistenceContext(unitName = "rpmv2")
    private EntityManager em;
    private DecimalFormat format10 = new DecimalFormat("0000000000");
    
    private DecimalFormat format6 = new DecimalFormat("000000");


	/**
     * Default constructor. 
     */
    public GeneradorFip() {
    }

	/**
	 * 
	 * @param caso
	 * @return
	 */
	protected Caso generar(Casoentidaddeterminacion caso) {
		em.refresh(caso);
		
		Caso xml = new Caso();
		
		xml.setCodigo(format10.format(caso.getIden()));
		if (caso.getNombre() != null && !caso.getNombre().isEmpty()) {
			xml.setNombre(caso.getNombre());
		} else {
			xml.setNombre("-");
		}

        if (caso.getEntidaddeterminacionregimensForIdcaso().size()>0){
            REGIMENES regimenes = new REGIMENES();
            xml.setREGIMENES(regimenes);
            for (Entidaddeterminacionregimen reg:caso.getEntidaddeterminacionregimensForIdcaso()){
                regimenes.getREGIMEN().add(generar(reg));
            }
        }

        if (caso.getVinculocasosForIdcaso().size()>0){
            VINCULOS vinculos = new VINCULOS();
            xml.setVINCULOS(vinculos);
            for (Vinculocaso vinc: caso.getVinculocasosForIdcaso()){
                vinculos.getVINCULO().add(getCodigo(vinc.getCasoentidaddeterminacionByIdcasovinculado()));
            }
        }

        if (caso.getDocumentocasos()!=null &&  caso.getDocumentocasos().size()>0){
            es.mitc.redes.urbanismoenred.data.fip.Caso.DOCUMENTOS documentos = new es.mitc.redes.urbanismoenred.data.fip.Caso.DOCUMENTOS();
            xml.setDOCUMENTOS(documentos);
            for (Documentocaso doc: caso.getDocumentocasos()){
            	documentos.getDOCUMENTO().add(getCodigo(doc));
            	
            }
        }

		return xml;
	}
	
	/**
	 * 
	 * @param determinacion
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected es.mitc.redes.urbanismoenred.data.fip.Determinacion generar(
			Determinacion determinacion) {
		em.refresh(determinacion);
		
		es.mitc.redes.urbanismoenred.data.fip.Determinacion xml = new es.mitc.redes.urbanismoenred.data.fip.Determinacion();
		
    	xml.setApartado(determinacion.getApartado());
    	xml.setCaracter(format6.format(determinacion.getIdcaracter()));
    	xml.setCodigo(format10.format(Long.parseLong(determinacion.getCodigo())));
    	xml.setNombre(determinacion.getNombre());
    	xml.setEtiqueta(determinacion.getEtiqueta());
    	xml.setSuspendida(determinacion.isBsuspendida());
    	xml.setTEXTO(determinacion.getTexto());
    	
        log.debug("Generando elemento para Determinación " + determinacion.getIden()
        			+ " código: " + determinacion.getCodigo()
        			+ " apartado: " + determinacion.getApartado()
        			+ " nombre: " + determinacion.getNombre());

        if (determinacion.getOpciondeterminacionsForIddeterminacion().size()>0){
        	log.debug("Generando valores de referencia...");
            VALORESREFERENCIA vr = new VALORESREFERENCIA();
            xml.setVALORESREFERENCIA(vr);
            for (Opciondeterminacion opc:determinacion.getOpciondeterminacionsForIddeterminacion()){
                vr.getVALORREFERENCIA().add(getCodigo(opc.getDeterminacionByIddeterminacionvalorref()));
            }
        }

        List<Determinacion> hijas = em.createNamedQuery("refundido.Determinacion.obtenerHijas")
        		.setParameter("idPadre", determinacion.getIden())
        		.getResultList();
        if (hijas.size()>0){
            HIJAS hijasXML = new HIJAS();
            xml.setHIJAS(hijasXML);
            
            for(Determinacion detHija : hijas){
                hijasXML.getDETERMINACION().add(generar(detHija));
            }
        }

        if (determinacion.getDocumentodeterminacions()!=null && determinacion.getDocumentodeterminacions().size()>0){
        	log.debug("Generando documentos determinacion...");
            es.mitc.redes.urbanismoenred.data.fip.Determinacion.DOCUMENTOS docs = new es.mitc.redes.urbanismoenred.data.fip.Determinacion.DOCUMENTOS();
			xml.setDOCUMENTOS(docs);
            for (Documentodeterminacion doc:determinacion.getDocumentodeterminacions()){
                docs.getDOCUMENTO().add(generar(doc));
            }
        }
        
        Determinacion unidad = getUnidad(determinacion.getIden());
        if (unidad!=null){
        	log.debug("Generando unidad...");
        	UNIDAD unidadXML = new UNIDAD();
        	xml.setUNIDAD(unidadXML);
        	unidadXML.setDeterminacion(format10.format(Long.parseLong(unidad.getCodigo())));
            unidadXML.setTramite(unidad.getTramite().getCodigofip());
        }
        
        RegulacionEspecifica[] regulacionesEspecificas = getRegulacionesEspecificas(determinacion.getIden());
        List<Determinacion> determinacionesReguladoras = em.createNamedQuery("refundido.Determinacion.obtenerReguladoras").setParameter("iden", determinacion.getIden()).getResultList();

        if (regulacionesEspecificas.length > 0 || determinacionesReguladoras.size() > 0){
        	log.debug("Generando regulaciones específicas...");
            REGULACION regulacion = new REGULACION();
            xml.setREGULACION(regulacion);
            if (determinacionesReguladoras.size() >0){
            	log.debug("Generando determinaciones reguladoras...");
                DETERMINACIONESREGULADORAS dr = new DETERMINACIONESREGULADORAS();
                regulacion.setDETERMINACIONESREGULADORAS(dr);
                for (Determinacion detReg:determinacionesReguladoras){
                    dr.getDETERMINACIONREGULADORA().add(getCodigo(detReg));
                }
            }
            if (regulacionesEspecificas.length > 0){
            	log.debug("Generando regulaciones específicas...");
                REGULACIONESESPECIFICAS re = new REGULACIONESESPECIFICAS();
                regulacion.setREGULACIONESESPECIFICAS(re);
                for (RegulacionEspecifica regEsp:regulacionesEspecificas){
                    re.getREGULACIONESPECIFICA().add(generar(regEsp));
                }
            }
        }
        
        if (determinacion.getDeterminacionByIddeterminacionbase()!=null){
        	log.debug("Generando determinacion base...");
            xml.setBASE(getCodigo(determinacion.getDeterminacionByIddeterminacionbase()));
        }
        
        if (determinacion.getDeterminaciongrupoentidadsForIddeterminacion().size()>0){
        	log.debug("Generando grupos de aplicacion...");
            GRUPOSAPLICACION ga = new GRUPOSAPLICACION();
            xml.setGRUPOSAPLICACION(ga);
            
            for (Determinaciongrupoentidad grp: determinacion.getDeterminaciongrupoentidadsForIddeterminacion()){
                ga.getGRUPOAPLICACION().add(getCodigo(grp.getDeterminacionByIddeterminaciongrupo()));
            }
        }
        
		return xml;
	}
	
	/**
	 * 
	 * @param documento
	 * @return
	 */
	protected es.mitc.redes.urbanismoenred.data.fip.Documento generar(
			Documento documento) {
		es.mitc.redes.urbanismoenred.data.fip.Documento xml = new es.mitc.redes.urbanismoenred.data.fip.Documento();
		em.refresh(documento);
    	
        if (documento.getIdgrupodocumento()!= null){
        	xml.setGrupo(format6.format(documento.getIdgrupodocumento()));
        }
        
        xml.setCodigo(format10.format(documento.getIden()));
        xml.setArchivo(documento.getArchivo());
        xml.setCOMENTARIO(documento.getComentario());
        xml.setNombre(documento.getNombre());
        xml.setTipo(format6.format(documento.getIdtipodocumento()));
       
        if (documento.getEscala() != null){
        	 xml.setEscala(Long.valueOf(documento.getEscala()));
        }

        if (documento.getDocumentoshps().size()>0){
            HOJAS hojas = new HOJAS();
			xml.setHOJAS(hojas);
            for(Documentoshp shp : documento.getDocumentoshps()){
                hojas.getHOJA().add(generar(shp));
            }
        }
        
		return xml;
	}

	/**
	 * 
	 * @param doc
	 * @return
	 */
	protected es.mitc.redes.urbanismoenred.data.fip.Determinacion.DOCUMENTOS.DOCUMENTO generar(
			Documentodeterminacion doc) {
		es.mitc.redes.urbanismoenred.data.fip.Determinacion.DOCUMENTOS.DOCUMENTO xml = new es.mitc.redes.urbanismoenred.data.fip.Determinacion.DOCUMENTOS.DOCUMENTO();
		
		xml.setCodigo(format10.format( doc.getDocumento().getIden()));
		
		return xml;
	}

	/**
	 * 
	 * @param shp
	 * @return
	 */
	protected Hoja generar(Documentoshp shp) {
		Hoja hoja = new Hoja();
		
		hoja.setNombre(shp.getHoja());
		hoja.setGEOMETRIA(shp.getGeom());

		return hoja;
	}

	/**
	 * 
	 * @param entidad
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected es.mitc.redes.urbanismoenred.data.fip.Entidad generar(
			Entidad entidad) {
		em.refresh(entidad);
		
		es.mitc.redes.urbanismoenred.data.fip.Entidad xml = new es.mitc.redes.urbanismoenred.data.fip.Entidad();
		
    	xml.setCodigo(format10.format(Long.parseLong(entidad.getCodigo().trim())));
    	xml.setClave(entidad.getClave());
    	xml.setEtiqueta(entidad.getEtiqueta());
    	xml.setNombre(entidad.getNombre());
    	xml.setSuspendida(entidad.isBsuspendida());
    	
        log.debug("Generando elemento para Entidad " + entidad.getIden()
    			+ " código: " + entidad.getCodigo()
    			+ " clave: " + entidad.getClave()
    			+ " nombre: " + entidad.getNombre());

        if (entidad.getEntidadpols().size() > 0) {
        	xml.setGEOMETRIA(entidad.getEntidadpols().iterator().next().getGeom());
        } else if (entidad.getEntidadlins().size() > 0) {
        	xml.setGEOMETRIA(entidad.getEntidadlins().iterator().next().getGeom());
        } else if (entidad.getEntidadpnts().size() > 0) {
        	xml.setGEOMETRIA(entidad.getEntidadpnts().iterator().next().getGeom());
        }

        List<Entidad> hijas = em.createNamedQuery("refundido.Entidad.obtenerHijas")
        		.setParameter("idPadre", entidad.getIden())
        		.getResultList();
        
        if (hijas.size() > 0) {
            es.mitc.redes.urbanismoenred.data.fip.Entidad.HIJAS hijasXML = new es.mitc.redes.urbanismoenred.data.fip.Entidad.HIJAS();
			xml.setHIJAS(hijasXML);
            for (Entidad entHija : hijas) {
                hijasXML.getENTIDAD().add(generar(entHija));
            }
        }

        if (entidad.getDocumentoentidads()!=null && entidad.getDocumentoentidads().size() > 0) {
            es.mitc.redes.urbanismoenred.data.fip.Entidad.DOCUMENTOS docsXML = new es.mitc.redes.urbanismoenred.data.fip.Entidad.DOCUMENTOS();
			xml.setDOCUMENTOS(docsXML);
            for (Documentoentidad doc : entidad.getDocumentoentidads()) {
                docsXML.getDOCUMENTO().add(getCodigo(doc));
            }
        }

        if (entidad.getEntidadByIdentidadbase() != null) {
        	xml.setBASE(getCodigo(entidad.getEntidadByIdentidadbase()));
        }

        return xml;
	}

	/**
	 * 
	 * @param ed
	 * @return
	 */
	protected CondicionUrbanistica generar(Entidaddeterminacion ed) {
		em.refresh(ed);
		
		CondicionUrbanistica cu = new CondicionUrbanistica();
		
		cu.setCODIGO(getCodigo(ed));
        
        if (ed.getCasoentidaddeterminacions().size()>0){
        	CASOS casos = new CASOS();
            for(Casoentidaddeterminacion caso : ed.getCasoentidaddeterminacions()){
            	casos.getCASO().add(generar(caso));
            }
            cu.setCASOS(casos);
        } else {
        	log.warn("No tiene casos asociados!");
        }

		return cu;
	}

	/**
	 * 
	 * @param reg
	 * @return
	 */
	protected Regimen generar(Entidaddeterminacionregimen edr) {
		em.refresh(edr);
		
		Regimen regimen = new Regimen();
		
        if (edr.getSuperposicion()!=null){
            regimen.setSuperposicion(BigInteger.valueOf(edr.getSuperposicion()));
        } else {
        	regimen.setSuperposicion(BigInteger.ZERO);
        }

		if (edr.getRegimenespecificos().size()>0){
            REGIMENESESPECIFICOS re = new REGIMENESESPECIFICOS();
            regimen.setREGIMENESESPECIFICOS(re);
            for (Regimenespecifico reg: edr.getRegimenespecificos()){
            	// Sólo los padres
            	if (reg.getRegimenespecifico() == null) {
            		re.getREGIMENESPECIFICO().add(generar(reg));
            	}
            }
        }
		
        if (edr.getValor()!=null && !edr.getValor().trim().isEmpty()){
        	regimen.setVALOR(edr.getValor());
        } else if(edr.getOpciondeterminacion()!=null){
        	regimen.setVALORREFERENCIA(getCodigo(edr.getOpciondeterminacion()
            			.getDeterminacionByIddeterminacionvalorref()));
        }

        if (edr.getDeterminacion()!=null ){
            regimen.setDETERMINACIONREGIMEN(getCodigo(edr.getDeterminacion()));
        }

        if (edr.getCasoentidaddeterminacionByIdcasoaplicacion()!=null ){
        	regimen.setCASOAPLICACION(getCodigo(edr.getCasoentidaddeterminacionByIdcasoaplicacion()));
        }

		return regimen;
	}

	/**
	 * 
	 * @param opDet
	 * @return
	 */
	protected OperacionDeterminacion generar(Operaciondeterminacion operacion) {
		em.refresh(operacion);
        OperacionDeterminacion od = new OperacionDeterminacion();
        
        od.setTipo(format6.format(operacion.getIdtipooperaciondet()));
        od.setOrden(operacion.getOperacion().getOrden());
        
        od.setOPERADA(getCodigo(operacion.getDeterminacionByIddeterminacion()));
        od.setOPERADORA(getCodigo(operacion.getDeterminacionByIddeterminacionoperadora()));
        
        od.setTEXTO(operacion.getOperacion().getTexto());
        
		return od;
	}

	/**
     * 
     * @param operacionEntidad
     * @return
     */
    protected OperacionEntidad generar(Operacionentidad operacionEntidad) {
    	em.refresh(operacionEntidad);
    	
    	OperacionEntidad operacion = new OperacionEntidad();
    	
    	operacion.setTipo(format6.format(operacionEntidad.getIdtipooperacionent()));
    	operacion.setOrden(operacionEntidad.getOperacion().getOrden());
    	
    	operacion.setOPERADA(getCodigo(operacionEntidad.getEntidadByIdentidad()));
    	operacion.setOPERADORA(getCodigo(operacionEntidad.getEntidadByIdentidadoperadora()));
    	
    	operacion.setTEXTO(operacionEntidad.getOperacion().getTexto());
    	
		return operacion;
	}

	/**
     * 
     * @param operacionRelacion
     * @return
     */
    protected OperacionEntidad generar(Operacionrelacion operacionRelacion) {
    	OperacionEntidad operacion = new OperacionEntidad();
    	
    	em.refresh(operacionRelacion);
    	
    	operacion.setTEXTO(operacionRelacion.getOperacion().getTexto());
    	operacion.setTipo(format6.format(operacionRelacion.getIdtipooperacionrel()));
    	operacion.setOrden(operacionRelacion.getOperacion().getOrden());
    	
    	return operacion;
	}

	/**
     * 
     * @param propiedadesAdscripcion
     * @return
     */
    protected es.mitc.redes.urbanismoenred.data.fip.PropiedadesAdscripcion generar(
			PropiedadesAdscripcion propiedadesAdscripcion) {
    	
        es.mitc.redes.urbanismoenred.data.fip.PropiedadesAdscripcion propiedades = new es.mitc.redes.urbanismoenred.data.fip.PropiedadesAdscripcion();
        
        propiedades.setCuantia(propiedadesAdscripcion.getCuantia());
		
        if (propiedadesAdscripcion.getUnidad() != null) {
        	propiedades.setUNIDAD(getCodigo(propiedadesAdscripcion.getUnidad()));
        }

        if (propiedadesAdscripcion.getTipo() != null) {
        	propiedades.setTIPO(getCodigo(propiedadesAdscripcion.getTipo()));
        }

        propiedades.setTEXTO(propiedadesAdscripcion.getTexto());

		return propiedades;
	}

	/**
	 * 
	 * @param reg
	 * @return
	 */
	protected RegimenEspecifico generar(Regimenespecifico regesp) {
		em.refresh(regesp);
		
		RegimenEspecifico re = new RegimenEspecifico();
        
        re.setOrden(BigInteger.valueOf(regesp.getOrden()));
        re.setNombre(regesp.getNombre());
        
        re.setTEXTO(regesp.getTexto());
        
        if (regesp.getRegimenespecificos().size()>0){
            HIJOS hijos = new HIJOS();
            re.setHIJOS(hijos);
            for(Regimenespecifico regHijo : regesp.getRegimenespecificos()){
                hijos.getREGIMENESPECIFICO().add(generar(regHijo));
            }
        }

		return re;
	}

	/**
	 * 
	 * @param regEsp
	 * @return
	 */
	protected es.mitc.redes.urbanismoenred.data.fip.RegulacionEspecifica generar(
			RegulacionEspecifica regulacion) {
		es.mitc.redes.urbanismoenred.data.fip.RegulacionEspecifica xml = new es.mitc.redes.urbanismoenred.data.fip.RegulacionEspecifica();
		
		xml.setNombre(regulacion.getNombre());
		xml.setOrden(BigInteger.valueOf(regulacion.getOrden()));
		xml.setTEXTO(regulacion.getTexto());

        if (regulacion.getRegulacionesespecificas().size()>0){
            es.mitc.redes.urbanismoenred.data.fip.RegulacionEspecifica.HIJAS hijas = new es.mitc.redes.urbanismoenred.data.fip.RegulacionEspecifica.HIJAS();
			xml.setHIJAS(hijas);
            for(RegulacionEspecifica regHija : regulacion.getRegulacionesespecificas()){
                hijas.getREGULACIONESPECIFICA().add(generar(regHija));
            }
        }
        
		return xml;
	}

    /**
     * 
     * @param tramite
     * @return
     * @throws RedesException
     */
    @SuppressWarnings("unchecked")
	protected es.mitc.redes.urbanismoenred.data.fip.Tramite generar(ContextoRefundido contexto) throws RedesException {
        try {
        	Tramite tramite = (Tramite) contexto.getParametro(ContextoRefundido.TRAMITE_REFUNDIDO);
        	
        	getEm().flush();
        	getEm().refresh(tramite);
        	log.debug("Generando FIP para trámite " + tramite.getCodigofip());
        	es.mitc.redes.urbanismoenred.data.fip.Tramite resultado = new es.mitc.redes.urbanismoenred.data.fip.Tramite();

            DecimalFormat format4 = new DecimalFormat("000000");

            resultado.setCodigo(tramite.getCodigofip());
            resultado.setTipoTramite(format4.format(tramite.getIdtipotramite()));
            
            if (tramite.getTexto() != null && !tramite.getTexto().isEmpty()) {
            	resultado.setTEXTO(tramite.getTexto());
            }
            
            if (tramite.getDocumentos()!=null && tramite.getDocumentos().size() > 0) {
            	DOCUMENTOS documentos = new DOCUMENTOS();
            	// Se ha optado por no incluir los documentos del trámite.
            	
                for (Documento documento : tramite.getDocumentos()) {
                	em.refresh(documento);
                	if (documento.getDocumentocasos().size() > 0 || 
                			documento.getDocumentodeterminacions().size() > 0 || 
                			documento.getDocumentoentidads().size() > 0) {
                		documentos.getDOCUMENTO().add(generar(documento));
                	}
                }
                
                if (documentos.getDOCUMENTO().size() > 0)
                	resultado.setDOCUMENTOS(documentos);
            }

            List<Entidad> entidades = getEm().createNamedQuery("refundido.Entidad.obtenerRaiz")
            		.setParameter("idTramite", tramite.getIden())
            		.getResultList();
            if (entidades.size() > 0) {
                ENTIDADES entidadesXml = new ENTIDADES();
				resultado.setENTIDADES(entidadesXml);
                for (Entidad entidad : entidades) {
                    entidadesXml.getENTIDAD().add(generar(entidad));
                }
            }

            List<Determinacion> determinaciones = getEm().createNamedQuery("refundido.Determinacion.obtenerRaiz")
            		.setParameter("idTramite", tramite.getIden())
            		.getResultList();
            if (determinaciones.size() > 0) {
                DETERMINACIONES determinacionesXml = new DETERMINACIONES();
                resultado.setDETERMINACIONES(determinacionesXml);
                
                for (Determinacion determinacion : determinaciones) {
                   determinacionesXml.getDETERMINACION().add(generar(determinacion));
                }
            }

            entidades = getEm().createNamedQuery("refundido.Entidad.obtenerPorTramite")
            		.setParameter("idTramite", tramite.getIden())
            		.getResultList();
            
            //Condiciones Urbanísticas
            if (entidades.size() > 0) {
                CONDICIONESURBANISTICAS cus = new CONDICIONESURBANISTICAS();
                for (Entidad entidad : entidades) {
                	em.refresh(entidad);
                	for (Entidaddeterminacion ed : entidad.getEntidaddeterminacions()) {
                		em.refresh(ed);
                		if(ed.getCasoentidaddeterminacions().size() > 0) {
                			cus.getCONDICIONURBANISTICA().add(generar(ed));
                		}
                    }
                }
                
                if (cus.getCONDICIONURBANISTICA().size() > 0) {
                	resultado.setCONDICIONESURBANISTICAS(cus);
                }
            }
        
            //Unidades
            determinaciones = getEm().createNamedQuery("refundido.Determinacion.obtenerUnidadesTramite")
            		.setParameter("idTramite", tramite.getIden())
            		.getResultList();
            if (determinaciones.size() > 0) {
                UNIDADES unidades = new UNIDADES();
                for (Determinacion determinacion : determinaciones) {
                	unidades.getUNIDAD().add(generarPropiedadesUnidad(determinacion));
                }
                if (unidades.getUNIDAD().size() > 0) {
                    resultado.setUNIDADES(unidades);
                }
            }
            
            String origen = "";
            String destino = "";
            PropiedadesAdscripcion[] adscripciones = getPropiedadesAdscripcionTramite(tramite.getIden());
            if (adscripciones.length > 0) {
            	ADSCRIPCIONES adscripcionesXml = new ADSCRIPCIONES();
                resultado.setADSCRIPCIONES(adscripcionesXml);
                
                for (PropiedadesAdscripcion aAdscr : adscripciones) {
                    Adscripcion adscripcion = new Adscripcion();
                    
                    origen = aAdscr.getOrigen().getCodigo();
                    destino = aAdscr.getDestino().getCodigo();
                    if (origen != null) {
                        adscripcion.setEntidadOrigen(origen);
                    }
                    if (destino != null) {
                    	adscripcion.setEntidadDestino(destino);
                    }
                    adscripcion.setPROPIEDADES(generar(aAdscr));
                    
                    adscripcionesXml.getADSCRIPCION().add(adscripcion);
                }
            }

            return resultado;
        } catch (Exception ex) {
        	throw new RedesException("Error al generar FIP: " + ex.getMessage());
        }
    }

    /*
     * (non-Javadoc)
     * @see es.mitc.redes.urbanismoenred.servicios.refundido.GeneradorFipLocal#generarFIP2(es.mitc.redes.urbanismoenred.servicios.refundido.ContextoRefundido, java.io.File)
     */
	@Override
	public void generarFIP2(ContextoRefundido contexto, File destino) throws ExcepcionRefundido {
		try {
			FIP fip = new FIP();
			
			fip.setFECHA(DatatypeFactory.newInstance().newXMLGregorianCalendar((GregorianCalendar)GregorianCalendar.getInstance()));
			fip.setPAIS(Textos.getTexto("consola", "fip.pais"));
			fip.setSRS(Textos.getTexto("consola", "projection"));
			fip.setVERSION(Textos.getTexto("consola", "fip.version"));
			fip.setTRAMITE(generar(contexto));
			
			JAXBContext jc = JAXBContext.newInstance("es.mitc.redes.urbanismoenred.data.fip");
			
			Marshaller marshaller = jc.createMarshaller();
			
			marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			
			marshaller.marshal(fip, destino);
		} catch (JAXBException e) {
			ResourceBundle traductor = (ResourceBundle) contexto.getParametro(ContextoRefundido.TRADUCTOR);
			throw new ExcepcionRefundido(String.format(traductor.getString("refundido.generar.fip.error.xml"), e.getMessage()), 4005);
		} catch (DatatypeConfigurationException e) {
			ResourceBundle traductor = (ResourceBundle) contexto.getParametro(ContextoRefundido.TRADUCTOR);
			throw new ExcepcionRefundido(String.format(traductor.getString("refundido.generar.fip.error.xml"), e.getMessage()), 4006);
		} catch (RedesException e) {
			ResourceBundle traductor = (ResourceBundle) contexto.getParametro(ContextoRefundido.TRADUCTOR);
			throw new ExcepcionRefundido(String.format(traductor.getString("refundido.generar.fip.error.xml"), e.getMessage()), 4007);
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
	 * @param propiedades
	 * @return
	 */
    @SuppressWarnings("unchecked")
	protected Unidad generarPropiedadesUnidad(Determinacion determinacion) {
    	
    	List<Relacion> relaciones = em.createNamedQuery("refundido.Relacion.obtenerPropiedadesUnidad")
    			.setParameter("idDeterminacion", determinacion.getIden())
    			.getResultList();
    	
    	Integer idDefAbreviatura = Integer.parseInt(Textos.getTexto("diccionario", "unidad.id_abreviatura"));
        Integer idDefDefinicion = Integer.parseInt(Textos.getTexto("diccionario", "unidad.id_definicion"));

		Unidad unidad = new Unidad();
		
		for (Propiedadrelacion propiedad : relaciones.get(0).getPropiedadrelacions()) {
			if (propiedad.getIddefpropiedad() == idDefAbreviatura) {
				unidad.setAbreviatura(propiedad.getValor());
			} else if (propiedad.getIddefpropiedad() == idDefDefinicion) {
				unidad.setDEFINICION(propiedad.getValor());
			}
		}
		
		unidad.setDeterminacion(format10.format(Long.parseLong(determinacion.getCodigo())));
        
		return unidad;
	}

	/**
	 * 
	 * @param casoentidaddeterminacionByIdcasovinculado
	 * @return
	 */
	protected CodigoCaso getCodigo(
			Casoentidaddeterminacion caso) {
		CodigoCaso vinculo = new CodigoCaso();

		vinculo.setCaso(format10.format(caso.getIden()));
		vinculo.setTramite(caso.getEntidaddeterminacion().getEntidad().getTramite().getCodigofip());

		return vinculo;
	}

	/**
     * 
     * @param determinacion
     * @return
     */
    protected CodigoDeterminacion getCodigo(Determinacion determinacion) {
		CodigoDeterminacion codigo = new CodigoDeterminacion();
		
		codigo.setTramite(determinacion.getTramite().getCodigofip());
		codigo.setDeterminacion(format10.format(Long.parseLong(determinacion.getCodigo())));
		
		return codigo;
	}

	/**
	 * 
	 * @param doc
	 * @return
	 */
	protected DOCUMENTO getCodigo(Documentocaso doc) {
		DOCUMENTO documento = new DOCUMENTO();
		
		documento.setCodigo(format10.format( doc.getDocumento().getIden()));
		return documento;
	}

	/**
	 * 
	 * @param doc
	 * @return
	 */
	protected es.mitc.redes.urbanismoenred.data.fip.Entidad.DOCUMENTOS.DOCUMENTO getCodigo(
			Documentoentidad doc) {
		es.mitc.redes.urbanismoenred.data.fip.Entidad.DOCUMENTOS.DOCUMENTO xml = new es.mitc.redes.urbanismoenred.data.fip.Entidad.DOCUMENTOS.DOCUMENTO();
		xml.setCodigo(format10.format(doc.getDocumento().getIden()));
		return xml;
	}

	/**
     * 
     * @param entidad
     * @return
     */
	protected CodigoEntidad getCodigo(Entidad entidad) {
		CodigoEntidad codigo = new CodigoEntidad();
		
		codigo.setEntidad(format10.format(Long.parseLong(entidad.getCodigo())));
		codigo.setTramite(entidad.getTramite().getCodigofip());
		
		return codigo;
	}

	
    
    /**
	 * 
	 * @param ed
	 * @return
	 */
	protected CodigoCondicionUrbanistica getCodigo(Entidaddeterminacion ed) {
		
		CodigoCondicionUrbanistica ccu = new CodigoCondicionUrbanistica();
		
		ccu.setDETERMINACION(getCodigo(ed.getDeterminacion()));
		ccu.setENTIDAD(getCodigo(ed.getEntidad()));
		
		return ccu;
	}

	/**
	 * @return the em
	 */
	protected EntityManager getEm() {
		return em;
	}
	
	/**
	 * @return the format10
	 */
	protected DecimalFormat getFormat10() {
		return format10;
	}
	
	/**
	 * @return the format6
	 */
	protected DecimalFormat getFormat6() {
		return format6;
	}

	/**
     * 
     * @param idTramite
     * @return
     */
    @SuppressWarnings("unchecked")
	private PropiedadesAdscripcion[] getPropiedadesAdscripcionTramite(
            int idTramite) {
        List<Relacion> relaciones = em.createNamedQuery("refundido.Relacion.obtenerAdscripcionesTramite").setParameter("idTramite", idTramite).setParameter("idDefEntidadOrigen", idDefEntidadOrigen).setParameter("idDefEntidadDestino", idDefEntidadDestino).setParameter("idDefPropAdscripcion", idDefPropAdscripcion).getResultList();

        return generarPropiedadesAdscripcion(relaciones);
    }

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

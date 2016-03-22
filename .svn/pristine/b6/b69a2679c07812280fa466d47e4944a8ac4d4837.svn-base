package es.mitc.redes.urbanismoenred.servicios.dal;

import java.math.BigInteger;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;

import es.mitc.redes.urbanismoenred.data.fip.CodigoDeterminacion;
import es.mitc.redes.urbanismoenred.data.fip.Determinacion.GRUPOSAPLICACION;
import es.mitc.redes.urbanismoenred.data.fip.Determinacion.HIJAS;
import es.mitc.redes.urbanismoenred.data.fip.Determinacion.REGULACION;
import es.mitc.redes.urbanismoenred.data.fip.Determinacion.REGULACION.DETERMINACIONESREGULADORAS;
import es.mitc.redes.urbanismoenred.data.fip.Determinacion.REGULACION.REGULACIONESESPECIFICAS;
import es.mitc.redes.urbanismoenred.data.fip.Determinacion.UNIDAD;
import es.mitc.redes.urbanismoenred.data.fip.Determinacion.VALORESREFERENCIA;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Determinacion;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Determinaciongrupoentidad;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Documentodeterminacion;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Opciondeterminacion;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.RegulacionEspecifica;
import es.mitc.redes.urbanismoenred.servicios.planeamiento.ServicioPlaneamientoLocal;

/**
 * 
 * @author Arnaiz Consultores
 *
 */
@MessageDriven(
		activationConfig = { 
			@ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
			@ActivationConfigProperty(propertyName = "destination", propertyValue = "queue/GeneradorFip"),
			@ActivationConfigProperty(propertyName = "messageSelector", propertyValue = "TramiteDeterminaciones IS NOT NULL"),
			@ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge")
			})
public class GeneradorDeterminacionesFIP implements MessageListener {
	
	protected class ComparadorDeterminaciones implements Comparator<Determinacion> {

		public ComparadorDeterminaciones() {
		}

		@Override
		public int compare(Determinacion det1, Determinacion det2) {
			return det1.getOrden() - det2.getOrden();
		}
		
	}
	
	protected static final Logger log = Logger.getLogger(GeneradorDeterminacionesFIP.class);
	
	@PersistenceContext(unitName = "rpmv2")
    private EntityManager em;
	
	@EJB
	private ServicioPlaneamientoLocal servicioPlaneamiento;
	@EJB
	private CoordinadorGeneracionFIPLocal coordinador;
	
	private DecimalFormat format10 = new DecimalFormat("0000000000");
	private DecimalFormat format6 = new DecimalFormat("000000");

    /**
     * Default constructor. 
     */
    public GeneradorDeterminacionesFIP() {
    }
	
	/**
	 * 
	 * @param determinacion
	 * @return
	 */
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

        if (determinacion.getDeterminacionsForIdpadre().size()>0){
            HIJAS hijasXML = new HIJAS();
            xml.setHIJAS(hijasXML);
            Determinacion[] hijas = determinacion.getDeterminacionsForIdpadre().toArray(new Determinacion[0]);
            log.debug("Generando determinaciones hijas...");
            Arrays.sort(hijas, new ComparadorDeterminaciones());
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
        
        Determinacion unidad = servicioPlaneamiento.getUnidad(determinacion.getIden());
        if (unidad!=null){
        	log.debug("Generando unidad...");
        	UNIDAD unidadXML = new UNIDAD();
        	xml.setUNIDAD(unidadXML);
        	unidadXML.setDeterminacion(format10.format(Long.parseLong(unidad.getCodigo())));
            unidadXML.setTramite(unidad.getTramite().getCodigofip());
        }
        
        RegulacionEspecifica[] regulacionesEspecificas = servicioPlaneamiento.getRegulacionesEspecificas(determinacion.getIden());
        Determinacion[] determinacionesReguladoras = servicioPlaneamiento.getDeterminacionesReguladoras(determinacion.getIden());

        if (regulacionesEspecificas.length > 0 || determinacionesReguladoras.length>0){
        	log.debug("Generando regulaciones específicas...");
            REGULACION regulacion = new REGULACION();
            xml.setREGULACION(regulacion);
            if (determinacionesReguladoras.length >0){
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
     * @see MessageListener#onMessage(Message)
     */
    @SuppressWarnings("unchecked")
	public void onMessage(Message message) {
    	try {
     	   int idtramite = message.getIntProperty("TramiteDeterminaciones");
     	   log.debug("Iniciando generación de determinaciones para trámite " + idtramite);
 		
     	   List <Determinacion> determinaciones = em.createNamedQuery("Determinacion.buscarRaiz").setParameter("idTramite", idtramite).getResultList();
     	   
     	   List<es.mitc.redes.urbanismoenred.data.fip.Determinacion> determinacionesFIP = new ArrayList<>();
     	   
     	   for (Determinacion determinacion : determinaciones) {
     		  determinacionesFIP.add(generar(determinacion));
     	   }
     	   
     	   Map<String, Object> resultados= coordinador.getProceso(idtramite);
	   	   if (resultados != null) {
	   		   resultados.put("Determinaciones", determinacionesFIP);
	   	   }
     	   
     	   log.debug("Finalizada generación de determinaciones para trámite " + idtramite);
        } catch (JMSException e) {
     	   log.error("Error al generar entidades de un FIP. Causa: " + e.getMessage(), e);
        }
    }

}

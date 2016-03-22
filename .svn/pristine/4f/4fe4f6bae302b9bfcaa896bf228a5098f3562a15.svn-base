package es.mitc.redes.urbanismoenred.servicios.dal;

import java.text.DecimalFormat;
import java.util.ArrayList;
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

import es.mitc.redes.urbanismoenred.data.fip.CodigoEntidad;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Documentoentidad;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidad;

/**
 * 
 * @author Arnaiz Consultores
 *
 */
@MessageDriven(
		activationConfig = { 
		@ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"), 
		@ActivationConfigProperty(propertyName = "destination", propertyValue = "queue/GeneradorFip"),
		@ActivationConfigProperty(propertyName = "messageSelector", propertyValue = "TramiteEntidades IS NOT NULL"),
		@ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge")}, 
		mappedName = "GeneracionEntidadesFIP")
public class GeneradorEntidadesFIP implements MessageListener {
	
	protected static final Logger log = Logger.getLogger(GeneradorEntidadesFIP.class);
	
	@PersistenceContext(unitName = "rpmv2")
    private EntityManager em;
	
	@EJB
	private CoordinadorGeneracionFIPLocal coordinador;
	
	private DecimalFormat format10 = new DecimalFormat("0000000000");

    /**
     * Default constructor. 
     */
    public GeneradorEntidadesFIP() {
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

        List<Entidad> hijas = em.createNamedQuery("Entidad.buscarHijas")
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
     * @see MessageListener#onMessage(Message)
     */
    @SuppressWarnings("unchecked")
	public void onMessage(Message message) {
       try {
    	   
    	   int idtramite = message.getIntProperty("TramiteEntidades");
    	   log.debug("Iniciando generación de entidades para trámite " + idtramite);
    	   
    	   List <Entidad> entidades = em.createNamedQuery("Entidad.buscarRaiz").setParameter("idTramite", idtramite).getResultList();
    	   
    	   List<es.mitc.redes.urbanismoenred.data.fip.Entidad> entidadesFIP = new ArrayList<>();
    	   
    	   for (Entidad entidad : entidades) {
    		   entidadesFIP.add(generar(entidad));
    	   }
    	   
    	   Map<String, Object> resultados= coordinador.getProceso(idtramite);
    	   if (resultados != null) {
    		   resultados.put("Entidades", entidadesFIP);
    	   }
    	   
    	   log.debug("Finalizada generación de entidades para trámite " + idtramite);
       } catch (JMSException e) {
    	   log.error("Error al generar entidades de un FIP. Causa: " + e.getMessage(), e);
       }  
    }

}

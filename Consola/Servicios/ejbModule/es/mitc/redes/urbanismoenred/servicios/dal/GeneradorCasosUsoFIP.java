package es.mitc.redes.urbanismoenred.servicios.dal;

import java.math.BigInteger;
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
import es.mitc.redes.urbanismoenred.data.fip.Regimen;
import es.mitc.redes.urbanismoenred.data.fip.Regimen.REGIMENESESPECIFICOS;
import es.mitc.redes.urbanismoenred.data.fip.RegimenEspecifico;
import es.mitc.redes.urbanismoenred.data.fip.RegimenEspecifico.HIJOS;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Casoentidaddeterminacion;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Determinacion;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Documentocaso;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidad;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidaddeterminacion;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidaddeterminacionregimen;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Regimenespecifico;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Vinculocaso;

/**
 * 
 * @author Arnaiz Consultores
 *
 */
@MessageDriven(
		activationConfig = { 
			@ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
			@ActivationConfigProperty(propertyName = "destination", propertyValue = "queue/GeneradorFip"),
			@ActivationConfigProperty(propertyName = "messageSelector", propertyValue = "TramiteCondicionesUrbanisticas IS NOT NULL")
			}
		)
public class GeneradorCasosUsoFIP implements MessageListener {
	
	protected static final Logger log = Logger.getLogger(GeneradorCasosUsoFIP.class);
	
	@PersistenceContext(unitName = "rpmv2")
    private EntityManager em;
	
	@EJB
	private CoordinadorGeneracionFIPLocal coordinador;
	
	private DecimalFormat format10 = new DecimalFormat("0000000000");

    /**
     * Default constructor. 
     */
    public GeneradorCasosUsoFIP() {
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
     * @see MessageListener#onMessage(Message)
     */
    @SuppressWarnings("unchecked")
	public void onMessage(Message message) {
    	try {
    		int idtramite = message.getIntProperty("TramiteCondicionesUrbanisticas");
     	   	log.debug("Iniciando generación de casos de uso para trámite " + idtramite);
 		
     	   	List <Entidad> entidades = em.createNamedQuery("Entidad.buscarPorTramite").setParameter("idTramite", idtramite).getResultList();
     	   
     	   	List<CondicionUrbanistica> cuFIP = new ArrayList<>();
     	   
     	   	for (Entidad entidad : entidades) {
     	   		for (Entidaddeterminacion ed : entidad.getEntidaddeterminacions()) {
     	   			cuFIP.add(generar(ed));
     	   		}
     	   	}
     	   
     	   	Map<String, Object> resultados= coordinador.getProceso(idtramite);
     	   	if (resultados != null) {
     	   		resultados.put("CondicionesUrbanisticas", cuFIP);
     	   	}
     	   
     	   	log.debug("Finalizada generación de casos de uso para trámite " + idtramite);
        } catch (JMSException e) {
     	   log.error("Error al generar casos de uso de un FIP. Causa: " + e.getMessage(), e);
        }
    }

}

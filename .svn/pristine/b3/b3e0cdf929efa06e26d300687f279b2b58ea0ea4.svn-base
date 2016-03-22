package es.mitc.redes.urbanismoenred.servicios.dal;

import java.text.DecimalFormat;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;

import org.apache.log4j.Logger;

import es.mitc.redes.urbanismoenred.data.fip.Adscripcion;
import es.mitc.redes.urbanismoenred.data.fip.CodigoDeterminacion;
import es.mitc.redes.urbanismoenred.data.fip.CodigoEntidad;
import es.mitc.redes.urbanismoenred.data.fip.CondicionUrbanistica;
import es.mitc.redes.urbanismoenred.data.fip.Determinacion;
import es.mitc.redes.urbanismoenred.data.fip.PlanRegistrado;
import es.mitc.redes.urbanismoenred.data.fip.Documento.HOJAS;
import es.mitc.redes.urbanismoenred.data.fip.Entidad;
import es.mitc.redes.urbanismoenred.data.fip.FIP;
import es.mitc.redes.urbanismoenred.data.fip.Hoja;
import es.mitc.redes.urbanismoenred.data.fip.OperacionDeterminacion;
import es.mitc.redes.urbanismoenred.data.fip.OperacionEntidad;
import es.mitc.redes.urbanismoenred.data.fip.Tramite.ADSCRIPCIONES;
import es.mitc.redes.urbanismoenred.data.fip.Tramite.CONDICIONESURBANISTICAS;
import es.mitc.redes.urbanismoenred.data.fip.Tramite.DETERMINACIONES;
import es.mitc.redes.urbanismoenred.data.fip.Tramite.DOCUMENTOS;
import es.mitc.redes.urbanismoenred.data.fip.Tramite.ENTIDADES;
import es.mitc.redes.urbanismoenred.data.fip.Tramite.OPERACIONES;
import es.mitc.redes.urbanismoenred.data.fip.Tramite.OPERACIONES.OPERACIONESDETERMINACIONES;
import es.mitc.redes.urbanismoenred.data.fip.Tramite.OPERACIONES.OPERACIONESENTIDADES;
import es.mitc.redes.urbanismoenred.data.fip.Tramite.UNIDADES;
import es.mitc.redes.urbanismoenred.data.fip.Unidad;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Documento;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Documentoshp;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Operacion;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Operaciondeterminacion;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Operacionentidad;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Operacionrelacion;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Plan;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.PropiedadesUnidad;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Tramite;
import es.mitc.redes.urbanismoenred.servicios.planeamiento.PropiedadesAdscripcion;
import es.mitc.redes.urbanismoenred.servicios.planeamiento.ServicioPlaneamientoLocal;
import es.mitc.redes.urbanismoenred.utils.excepciones.RedesException;
import es.mitc.redes.urbanismoenred.utils.recursos.textos.Textos;

/**
 * Session Bean implementation class GeneradorFipAsincrono
 */
@Stateless (name = "GeneradorFipAsincrono")
@Local( { GeneradorFipLocal.class })
public class GeneradorFipAsincrono implements GeneradorFipLocal {
	
	private static final Logger log = Logger.getLogger(GeneradorFipAsincrono.class);
	
	private DecimalFormat format10 = new DecimalFormat("0000000000");
    private DecimalFormat format6 = new DecimalFormat("000000");
	
	@Resource(name = "java:/ConnectionFactory")
	private ConnectionFactory connectionFactory;
	
	@Resource(mappedName = "java:/queue/GeneradorFip")
	private static Queue queue;
	
	@PersistenceContext(unitName = "rpmv2")
	private EntityManager em;
		
	@EJB
	private GeneradorDiccionarioLocal generadorDiccionario;
	@EJB
	private ServicioPlaneamientoLocal servicioPlaneamiento;
	@EJB
	private CoordinadorGeneracionFIPLocal coordinador;

    /**
     * Default constructor. 
     */
    public GeneradorFipAsincrono() {
    }

	/**
	 * 
	 * @param documento
	 * @return
	 */
	private es.mitc.redes.urbanismoenred.data.fip.Documento generar(
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
	 * @param shp
	 * @return
	 */
	private Hoja generar(Documentoshp shp) {
		Hoja hoja = new Hoja();
		
		hoja.setNombre(shp.getHoja());
		hoja.setGEOMETRIA(shp.getGeom());

		return hoja;
	}

	/**
	 * 
	 * @param opDet
	 * @return
	 */
	private OperacionDeterminacion generar(Operaciondeterminacion operacion) {
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
	private OperacionEntidad generar(Operacionentidad operacionEntidad) {
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
	private OperacionEntidad generar(Operacionrelacion operacionRelacion) {
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
	private es.mitc.redes.urbanismoenred.data.fip.PropiedadesAdscripcion generar(
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
	 * @param propiedades
	 * @return
	 */
	private Unidad generar(PropiedadesUnidad propiedades) {

		Unidad unidad = new Unidad();
		
		unidad.setAbreviatura(propiedades.getAbreviatura());
		unidad.setDEFINICION(propiedades.getDefinicion());
		
		unidad.setDeterminacion(format10.format(Long.parseLong(propiedades.getDeterminacion().getCodigo())));
        
		return unidad;
	}

    /*
     * (non-Javadoc)
     * @see es.mitc.redes.urbanismoenred.servicios.dal.GeneradorFipLocal#generarFIP2(es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Tramite)
     */
    @Override
	public FIP generarFIP2(Tramite tramite) throws RedesException {
    	FIP fip = new FIP();
		try {
			fip.setFECHA(DatatypeFactory.newInstance().newXMLGregorianCalendar((GregorianCalendar)GregorianCalendar.getInstance()));
			fip.setPAIS(Textos.getTexto("consola", "fip.pais"));
			fip.setSRS(Textos.getTexto("consola", "projection"));
			fip.setVERSION(Textos.getTexto("consola", "fip.version"));
			
			Map<String, Object> resultados = coordinador.iniciarProceso(tramite.getIden());
			
			iniciarGeneracion(tramite);
			
			while(resultados.size() < 3) {
				Thread.sleep(30000);
			}
			
			fip.setTRAMITE(generar(tramite, resultados));
			
			resultados.clear();
			
			coordinador.finalizarProceso(tramite.getIden());
			
		} catch (DatatypeConfigurationException e) {
			throw new RedesException("Error al obtener la fecha de generación del FIP. " + e.getMessage());
		} catch (Exception e) {
			throw new RedesException("Error al obtener la fecha de generación del FIP. " + e.getMessage());
		}

        return fip;
	}

    /**
     * 
     * @param tramite
     * @param resultados
     * @return
     * @throws RedesException 
     */
    @SuppressWarnings("unchecked")
	private es.mitc.redes.urbanismoenred.data.fip.Tramite generar(
			Tramite tramite, Map<String, Object> resultados) throws RedesException {
    	try {

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
				resultado.setDOCUMENTOS(documentos);
				List<es.mitc.redes.urbanismoenred.data.fip.Documento> listaDocumentos = documentos.getDOCUMENTO();
                for (Documento documento : tramite.getDocumentos()) {
                    listaDocumentos.add(generar(documento));
                }
            }

            List<Entidad> entidades = (List<Entidad>) resultados.get("Entidades");
            if (entidades.size() > 0) {
                ENTIDADES entidadesXml = new ENTIDADES();
				resultado.setENTIDADES(entidadesXml);
                for (Entidad entidad : entidades) {
                    entidadesXml.getENTIDAD().add(entidad);
                }
            }

            List<Determinacion> determinaciones = (List<Determinacion>) resultados.get("Determinaciones");
            if (determinaciones.size() > 0) {
                DETERMINACIONES determinacionesXml = new DETERMINACIONES();
                resultado.setDETERMINACIONES(determinacionesXml);
                
                for (Determinacion determinacion : determinaciones) {
                    determinacionesXml.getDETERMINACION().add(determinacion);
                }
            }

            //Condiciones Urbanísticas
            List<CondicionUrbanistica> condiciones = (List<CondicionUrbanistica>) resultados.get("CondicionesUrbanisticas");
            if (condiciones.size() > 0) {
                CONDICIONESURBANISTICAS cus = new CONDICIONESURBANISTICAS();
                for (CondicionUrbanistica cu : condiciones) {
                	cus.getCONDICIONURBANISTICA().add(cu);
                }
                
                resultado.setCONDICIONESURBANISTICAS(cus);
            }

            if (tramite.getOperacions().size() > 0) {
            	OPERACIONES operaciones = new OPERACIONES();
            	
                for (Operacion operacion : tramite.getOperacions()) {
                    if (operacion.getOperaciondeterminacions().size() > 0) {
                        OPERACIONESDETERMINACIONES od = new OPERACIONESDETERMINACIONES();
                        for (Operaciondeterminacion opDet : operacion.getOperaciondeterminacions()) {
                            od.getOPERACIONDETERMINACION().add(generar(opDet));
                        }
                        if (od.getOPERACIONDETERMINACION().size() > 0) {
                        	operaciones.getOPERACIONESDETERMINACIONES().add(od);
                        }
                    }
                    
                    if (operacion.getOperacionentidads().size() > 0) {
                        OPERACIONESENTIDADES oe = new OPERACIONESENTIDADES();
                        for (Operacionentidad opEnt : operacion.getOperacionentidads()) {
                            oe.getOPERACIONENTIDAD().add(generar(opEnt));
                        }
                        if (oe.getOPERACIONENTIDAD().size() > 0) {
                        	operaciones.getOPERACIONESENTIDADES().add(oe);
                        }
                    }
                    if (operacion.getOperacionrelacions().size() > 0) {
                    	OPERACIONESENTIDADES oe = new OPERACIONESENTIDADES();
                        for (Operacionrelacion opRel : operacion.getOperacionrelacions()) {
                        	oe.getOPERACIONENTIDAD().add(generar(opRel));
                        }
                        if (oe.getOPERACIONENTIDAD().size() > 0) {
                        	operaciones.getOPERACIONESENTIDADES().add(oe);
                        }
                    }
                }
            }
        
            //Unidades
            List<es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Determinacion> detsPlaneamiento = em.createNamedQuery("Determinacion.obtenerUnidadesTramite")
            		.setParameter("idTramite", tramite.getIden())
            		.getResultList();
            if (detsPlaneamiento.size() > 0) {
            	log.debug("Generando " + detsPlaneamiento.size() + " unidades... ");
                UNIDADES unidades = new UNIDADES();
                for (es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Determinacion determinacion : detsPlaneamiento) {
                	log.debug("Generando unidad: " + determinacion.getCodigo());
                	PropiedadesUnidad propiedades = servicioPlaneamiento.getPropiedadesUnidad(determinacion.getIden());
                	if (propiedades != null) {
                		unidades.getUNIDAD().add(generar(propiedades));
                	}
                }
                if (unidades.getUNIDAD().size() > 0) {
                    resultado.setUNIDADES(unidades);
                }
            }
            
            log.debug("Unidades generadas...");
            
            String origen = "";
            String destino = "";
            PropiedadesAdscripcion[] adscripciones = servicioPlaneamiento.getPropiedadesAdscripcionTramite(tramite.getIden());
            if (adscripciones.length > 0) {
            	log.debug("Generando " + adscripciones.length + " adscripciones... ");
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
     * @see es.mitc.redes.urbanismoenred.servicios.dal.GeneradorFipLocal#generarPlantilla(int, java.lang.String)
     */
	@SuppressWarnings("unchecked")
	@Override
	public FIP generarPlantilla(int idAmbito, String idioma)
			throws RedesException {
		FIP fip = new FIP();
		try {
			fip.setFECHA(DatatypeFactory.newInstance().newXMLGregorianCalendar((GregorianCalendar)GregorianCalendar.getInstance()));
			fip.setDICCIONARIO(generadorDiccionario.crearDiccionario(idioma));
			fip.setPAIS(Textos.getTexto("consola", "fip.pais"));
			fip.setSRS(Textos.getTexto("consola", "projection"));
			fip.setVERSION(Textos.getTexto("consola", "fip.version"));
			List<Plan> planes = em.createNamedQuery("Plan.buscarPlanesBase")
					.getResultList();
			if (planes.size() > 0) {
				List<Tramite> tramites = em.createNamedQuery("Tramite.findVigente")
						.setParameter("idPlan", planes.get(0).getIden())
						.getResultList();
				if (tramites.size() > 0) {
					fip.setCATALOGOSISTEMATIZADO(crearPlanRegistrado(tramites.get(0)));
				}
			}
			
			List<Tramite> tramitesRefundidos = em.createNamedQuery("Tramite.obtenerTramiteRefundidoVigente")
					.setParameter("idAmbito", idAmbito)
					.getResultList();
			
			if (tramitesRefundidos.size() > 0) {
				fip.setPLANEAMIENTOVIGENTE(crearPlanRegistrado(tramitesRefundidos.get(0)));
			}
			
		} catch (DatatypeConfigurationException e) {
			throw new RedesException("Error al obtener la fecha de generación del FIP. " + e.getMessage());
		}
		
		return fip;
	}
	
	/**
	 * 
	 * @param tramite
	 * @return
	 * @throws RedesException 
	 */
	private PlanRegistrado crearPlanRegistrado(Tramite tramite) throws RedesException {
		PlanRegistrado planRegistrado = new PlanRegistrado();
		planRegistrado.setAmbito(format6.format(tramite.getPlan().getIdambito()));
		planRegistrado.setNombre(tramite.getPlan().getNombre());
		
		Map<String, Object> resultados = coordinador.iniciarProceso(tramite.getIden());
		
		iniciarGeneracion(tramite);
		
		while(resultados.size() < 3) {
			try {
				Thread.sleep(30000);
			} catch (InterruptedException e) {
			}
		}
		
		planRegistrado.setTRAMITE(generar(tramite, resultados));
		
		resultados.clear();
		
		coordinador.finalizarProceso(tramite.getIden());
		
		return planRegistrado;
	}

	/**
     * 
     * @param determinacion
     * @return
     */
	private CodigoDeterminacion getCodigo(es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Determinacion determinacion) {
		CodigoDeterminacion codigo = new CodigoDeterminacion();
		
		codigo.setTramite(determinacion.getTramite().getCodigofip());
		codigo.setDeterminacion(format10.format(Long.parseLong(determinacion.getCodigo())));
		
		return codigo;
	}
	
	/**
     * 
     * @param entidad
     * @return
     */
	private CodigoEntidad getCodigo(es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidad entidad) {
		CodigoEntidad codigo = new CodigoEntidad();
		
		codigo.setEntidad(format10.format(Long.parseLong(entidad.getCodigo())));
		codigo.setTramite(entidad.getTramite().getCodigofip());
		
		return codigo;
	}

	/**
	 * 
	 */
	private void iniciarGeneracion(Tramite tramite) {
		try {
			Connection conexion = connectionFactory.createConnection();
			
			Session sesion = conexion.createSession(true, Session.AUTO_ACKNOWLEDGE);
			
			MessageProducer productor = sesion.createProducer(queue);
			
			MapMessage mensaje = sesion.createMapMessage();
			
			mensaje.setIntProperty("TramiteEntidades", tramite.getIden());
			
			productor.send(mensaje);
			
			mensaje = sesion.createMapMessage();
			
			mensaje.setIntProperty("TramiteDeterminaciones", tramite.getIden());
			
			productor.send(mensaje);
			
			mensaje = sesion.createMapMessage();
			
			mensaje.setIntProperty("TramiteCondicionesUrbanisticas", tramite.getIden());
			
			productor.send(mensaje);
			
			sesion.commit();
			
			sesion.close();
			conexion.close();
			
		} catch (JMSException e) {
			log.warn("No se ha podido generar el trámite: " + e.getMessage());
		}
	}
}

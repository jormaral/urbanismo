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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;

import es.mitc.redes.urbanismoenred.data.rpm.diccionario.Instrumentotipooperacionplan;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Operacionplan;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Plan;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Tramite;
import es.mitc.redes.urbanismoenred.data.rpm.seguridad.Fip1;
import es.mitc.redes.urbanismoenred.servicios.dal.ExcepcionPersistencia;
import es.mitc.redes.urbanismoenred.servicios.dal.GestorConsultasLocal;
import es.mitc.redes.urbanismoenred.servicios.planeamiento.ServicioDiccionariosLocal;
import es.mitc.redes.urbanismoenred.utils.recursos.textos.Textos;

/**
 * Session Bean implementation class CreadorTramitesBean
 * @author Arnaiz Consultores
 */
@Stateless(name = "GestorTramitesRefundido")
public class GestorTramitesRefundidoBean implements GestorTramitesRefundidoLocal {
	
	private static final Logger log = Logger.getLogger(GestorTramitesRefundidoBean.class);
	
	private final static List<Integer> tipoTramitesRefundibles;

    static {
        tipoTramitesRefundibles = new ArrayList<Integer>();

        String txtTtsRefundibles = Textos.getTexto("consola", "tipotramitesrefundibles");
        String[] arrTtsRefundibles = txtTtsRefundibles.split(",");
        for (String tt : arrTtsRefundibles) {
            try {
                tipoTramitesRefundibles.add(Integer.valueOf(tt));
            } catch (Exception e) {
                //Si no es numérico no lo añadimos a la caché y pasamos al siguiente
            }
        }
    }
	
	private SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy");
	
	@PersistenceContext(unitName = "rpmv2")
    private EntityManager em;
	
	@EJB
    private GestorConsultasLocal gestorConsultas;
	@EJB
    private ServicioDiccionariosLocal servicioDiccionario;

    /**
     * Default constructor. 
     */
    public GestorTramitesRefundidoBean() {
    }

    /*
     * (non-Javadoc)
     * @see es.mitc.redes.urbanismoenred.servicios.refundido.CreadorTramitesLocal#crearTramiteRefundido(es.mitc.redes.urbanismoenred.servicios.refundido.ContextoRefundido)
     */
	@SuppressWarnings("unchecked")
	@Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public Tramite crearTramiteRefundido(ContextoRefundido contexto) throws ExcepcionRefundido{
    	contexto.log(ContextoRefundidoBasico.LOG.INFO,"Inicio de la creación del trámite refundido en la base de datos.");
    	
    	try {
			Tramite tramiteRef = (Tramite) contexto.getParametro(ContextoRefundido.TRAMITE_REFUNDIDO);
			log.debug("Se procede a generar XML para el trámite " + tramiteRef.getIden());
			
			Instrumentotipooperacionplan itop = obtenerInstrumento(contexto);
			// Fecha que se va a asignar al trámite refundido
	        Calendar cal = Calendar.getInstance();

	        // Crea el nuevo registro en Plan
	        List<Plan> planesRefundidos = em.createNamedQuery("Plan.obtenerPlanResultanteRefundido")
	        	.setParameter("idPlan", tramiteRef.getPlan().getIden())
	        	.setParameter("idItop", itop.getIden())
	        	.getResultList();
	        Plan plan = new Plan();
	        if (planesRefundidos.size() > 0) {
	        	plan = planesRefundidos.get(0);
	        } else {
	        	plan.setNombre("Plan refundido de [" + contexto.getParametro(ContextoRefundido.NOMBRE_AMBITO_REFUNDIDO) + "]");
		        plan.setCodigo(gestorConsultas.getSiguienteCodigoPlan());
		        // Al ser una transacción nueva a veces se da un error si se intenta reutilizar entidades de otra transacción
		        // Para evitar esto obtenemos de nuevo la entidad para que no haya problemas
		        if (tramiteRef.getPlan().getPlanByIdplanbase() != null) {
		        	plan.setPlanByIdplanbase(em.find(Plan.class, tramiteRef.getPlan().getPlanByIdplanbase().getIden()));
		        }
		        
		        plan.setIdambito(tramiteRef.getPlan().getIdambito());
		        plan.setBsuspendido(tramiteRef.getPlan().getBsuspendido());
		        int orden = 0;
		        try {
		        	orden = (Integer)em.createNativeQuery("Select 1+Max(orden) " +
		        			"From planeamiento.Plan Where idAmbito=" + tramiteRef.getPlan().getIdambito()).getSingleResult();
		        } catch (NoResultException nre) {
		        	log.warn("No se ha encontrado ningún plan del ámbito especificado ("+tramiteRef.getPlan().getIdambito()+"). " + nre.getMessage());
		        }
		        plan.setOrden(orden);
		        em.persist(plan);
		        
		        Operacionplan op = new Operacionplan();
		        op.setPlanByIdplanoperador(plan);
		        op.setPlanByIdplanoperado(em.find(Plan.class, tramiteRef.getPlan().getIden()));
		        op.setIdinstrumentotipooperacion(itop.getIden());
		        em.persist(op);
	        }
	        
	        Integer iteracion = 1;
	        try {
	        	iteracion = (Integer)em.createNativeQuery("Select 1+Max(iteracion) From planeamiento.Tramite Where idplan=" 
	        			+ plan.getIden())
	        			.getSingleResult();
	        } catch (NoResultException nre) {
	        	iteracion = 1;
	        } 
		    if (iteracion == null) {
		    	iteracion = 1;
		    }

	        Tramite tramite = new Tramite();
	        tramite.setIdtipotramite(ClsDatos.ID_TIPOTRAMITE_REFUNDIDO);
	        tramite.setIdorgano(tramiteRef.getIdorgano());
	        tramite.setIdsentido(tramiteRef.getIdsentido());
	        tramite.setPlan(plan);
	        tramite.setFecha(cal.getTime());
	        tramite.setTexto("Plan refundido: [" + plan.getCodigo() + "]");
	        tramite.setNombre(plan.getNombre());
	        tramite.setIdcentroproduccion(tramiteRef.getIdcentroproduccion());
	        tramite.setIteracion(iteracion);
	        tramite.setComentario(tramiteRef.getComentario());
	        
	        //  Calcula un código FIP nuevo en función del ámbito, plan, tipo de trámite, e iteración.
	        // Encripta el código FIP calculado
            String codigoFIPencriptado = gestorConsultas.generarCodigoTramite(tramite);
            contexto.log(ContextoRefundido.LOG.INFO,"Código de Trámite encriptado   : [" + codigoFIPencriptado + "]");
	        tramite.setCodigofip(codigoFIPencriptado);
	        
	        // Mensaje con los datos del trámite que se va a crear
	        contexto.log(ContextoRefundidoBasico.LOG.INFO,"    Nombre   : " + plan.getNombre());
	        contexto.log(ContextoRefundidoBasico.LOG.INFO,"    Plan     : " + plan.getCodigo());
	        contexto.log(ContextoRefundidoBasico.LOG.INFO,"    Fecha    : " + sdf.format(cal.getTime()));
	        contexto.log(ContextoRefundidoBasico.LOG.INFO,"    Iteración: " + iteracion);
	        contexto.log(ContextoRefundidoBasico.LOG.INFO,"    Código   : " + codigoFIPencriptado);
	        
	        em.persist(tramite);
	        
	        return tramite;
    	} catch (SecurityException e) {
			throw new ExcepcionRefundido("Error al crear trámite refundido.", 999999);
		} catch (IllegalStateException e) {
			throw new ExcepcionRefundido("Error al crear trámite refundido.", 999999);
		} catch (ExcepcionPersistencia e) {
			throw new ExcepcionRefundido("Error al crear trámite refundido. " + e.getMessage(), 999999);
		} 
    }

	/**
	 * Se va a distinguir entre refundidos parciales y refundidos totales.
	 * Los refundidos totales serán aquellos que engloban a todos los trámites
	 * refundibles de un determinado ámbito. Los refundidos parciales serán
	 * el resto.
	 * 
	 * En esta versión no está soportado el refundido sobre planes refundidos.
	 * En versiones siguientes donde sí se contemple el que existan planes
	 * operando sobre planes refundidos habrá que tener esto en cuenta para
	 * decidir si un refundido es total o no.
	 * 
	 * En teoría sólo los refundidos totales pueden ser consolidados.
	 * 
	 * @param contexto Contexto de refundido.
	 * @return Instrumento para operaciones de refundido totales o parciales.
	 */
	private Instrumentotipooperacionplan obtenerInstrumento(
			ContextoRefundido contexto) {
		List<?> tramitesRefundibles = em.createNamedQuery("Tramite.buscarRefundiblesPorAmbito")
				.setParameter("idAmbito", contexto.getIdAmbito())
				.setParameter("listaRefundibles", tipoTramitesRefundibles)
				.getResultList();
		
		if (contexto.getTramites().size() == tramitesRefundibles.size()) {
			return servicioDiccionario.getInstrumentoTipoOperacion(
    				ClsDatos.ID_TIPOOPERACIONPLAN_REFUNDIDO, 
    				ClsDatos.ID_INSTRUMENTOPLAN_REFUNDIDO);
		} else {
			return servicioDiccionario.getInstrumentoTipoOperacion(
	        				ClsDatos.ID_TIPOOPERACIONPLAN_REFUNDIDO, 
	        				ClsDatos.ID_INSTRUMENTOPLAN_REFUNDIDO_PARCIAL);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.servicios.refundido.GestorTramitesRefundidoLocal#registrarFip1(es.mitc.redes.urbanismoenred.servicios.refundido.ContextoRefundido)
	 */
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void registrarFip1(ContextoRefundido contexto) {
		Tramite tramite = (Tramite) contexto.getParametro(ContextoRefundido.TRAMITE_REFUNDIDO);
		Fip1 fip1 = new Fip1();
		fip1.setCodfip(tramite.getCodigofip());
		fip1.setFechacreacion(tramite.getFecha());
		fip1.setFecharefundido(tramite.getFecha());
		fip1.setIdambito(tramite.getPlan().getIdambito());
		fip1.setObsoleto(false);
		
		em.persist(fip1);
	}
}

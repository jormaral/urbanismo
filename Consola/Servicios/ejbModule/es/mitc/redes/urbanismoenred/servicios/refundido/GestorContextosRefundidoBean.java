package es.mitc.redes.urbanismoenred.servicios.refundido;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Singleton;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;

import es.mitc.redes.urbanismoenred.data.rpm.refundido.Archivo;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Proceso;
import es.mitc.redes.urbanismoenred.servicios.seguridad.Usuario;
import es.mitc.redes.urbanismoenred.utils.recursos.textos.Textos;


/**
 * Session Bean implementation class GestorContextosRefundidoBean
 */
@Singleton
public class GestorContextosRefundidoBean implements GestorContextosRefundidoLocal, PropertyChangeListener {
	
	private static final Logger log = Logger.getLogger(GestorContextosRefundidoBean.class);

	@PersistenceContext(unitName = "rpmv2")
	private EntityManager em;
	
	private Map<Integer, List<ContextoRefundido>> contextosPorAmbito = new HashMap<Integer, List<ContextoRefundido>>();
	private Map<Integer, List<ContextoRefundido>> contextosPorUsuario = new HashMap<Integer, List<ContextoRefundido>>();
	private Map<ContextoRefundido, Integer> procesos = new HashMap<ContextoRefundido, Integer>();
	

    /**
     * Default constructor. 
     */
    public GestorContextosRefundidoBean() {
    }

    /**
	 * 
	 * @param contexto
	 */
	private void agregarContexto(ContextoRefundido contexto) {
		List<ContextoRefundido> contextos = contextosPorAmbito.get(contexto.getIdAmbito());
		if (contextos == null) {
			contextos = new ArrayList<ContextoRefundido>();
			contextosPorAmbito.put(contexto.getIdAmbito(), contextos);
		}
		contextos.add(contexto);
		
		contextos = contextosPorUsuario.get(contexto.getUsuario().getIdentificador());
		
		if (contextos == null) {
			contextos = new ArrayList<ContextoRefundido>();
			contextosPorUsuario.put(contexto.getUsuario().getIdentificador(), contextos);
		}
		
		contexto.agregarListener(this);
	}

	/*
	 * (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.servicios.refundido.GestorContextosRefundidoLocal#finalizarProceso(es.mitc.redes.urbanismoenred.servicios.refundido.ContextoRefundido)
	 */
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void finalizarProceso(ContextoRefundido contexto) {
		Proceso proceso;
		contexto.finalizarProceso();
		
		switch (contexto.getEstado()) {
		case FINALIZADO:
			if (procesos.containsKey(contexto)) {
				proceso = em.find(Proceso.class, procesos.get(contexto));
				proceso.setFin(GregorianCalendar.getInstance().getTime());
				proceso.setCorrecto(true);
				Archivo doclog = new Archivo();
				doclog.setProceso(proceso);
				doclog.setTipo("LOG");
				doclog.setContenido(contexto.getArchivoLog().getAbsolutePath());
				em.persist(doclog);
				Archivo fip = new Archivo();
				fip.setProceso(proceso);
				fip.setTipo("FIP");
				fip.setContenido(contexto.getFIP().getAbsolutePath());
				em.persist(fip);
				log.debug("Proceso de refundido finalizado " + proceso.getIden());
			} else {
				log.warn("Se ha recibido notificación de un contexto no registrado. ");
			}
			break;
		case ERRONEO:
			if (procesos.containsKey(contexto)) {
				proceso = em.find(Proceso.class, procesos.get(contexto));
				proceso.setFin(GregorianCalendar.getInstance().getTime());
				proceso.setCorrecto(false);
				Archivo log = new Archivo();
				log.setProceso(proceso);
				log.setTipo("LOG");
				log.setContenido(contexto.getArchivoLog().getAbsolutePath());
				em.persist(log);
			} else {
				log.warn("Se ha recibido notificación de un contexto no registrado. ");
			}
			break;
		default:
			break;
		}
		contexto.eliminarListener(this);
		procesos.remove(contexto);
		if (contextosPorAmbito.containsKey(contexto.getIdAmbito()))
			contextosPorAmbito.get(contexto.getIdAmbito()).remove(contexto);
		if (contextosPorUsuario.containsKey(contexto.getUsuario().getIdentificador()))
			contextosPorUsuario.get(contexto.getUsuario().getIdentificador()).remove(contexto);
	}

	/*
	 * (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.servicios.refundido.GestorContextosRefundidoLocal#generarContexto(java.util.List, es.mitc.redes.urbanismoenred.servicios.seguridad.Usuario, boolean)
	 */
	@Override
	public ContextoRefundido generarContexto(List<Integer> tramites,
			Usuario usuario, boolean crearTramite) throws ExcepcionRefundido {
		
		int idAmbito = obtenerAmbito(tramites);
		
		for (ContextoRefundido ctxt : getContextos(idAmbito)) {
			if (Estado.INICIADO.equals(ctxt.getEstado())) {
				throw new ExcepcionRefundido("Ya existe un proceso de refundido iniciado para el ámbito: " + idAmbito, 3001);
			}
		}
		
		ContextoRefundido contexto = new ContextoRefundidoBasico(tramites, usuario, crearTramite);
		contexto.setIdAmbito(idAmbito);
		contexto.putParametro(ContextoRefundido.NIVEL_ERRORES, Textos.getTexto("refundido", ContextoRefundido.NIVEL_ERRORES));
		
		agregarContexto(contexto);
		
		return contexto;
	}

	/*
	 * (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.servicios.refundido.GestorContextosRefundidoLocal#getContextos(int)
	 */
	@Override
	public ContextoRefundido[] getContextos(int idAmbito) {
		if (contextosPorAmbito.containsKey(idAmbito)) {
			return contextosPorAmbito.get(idAmbito).toArray(new ContextoRefundido[0]);
		} else {
			return new ContextoRefundido[0];
		}
	}

	/*
	 * (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.servicios.refundido.GestorContextosRefundidoLocal#getContextos(es.mitc.redes.urbanismoenred.data.rpm.seguridad.Usuario)
	 */
	@Override
	public ContextoRefundido[] getContextos(Usuario usuario) {
		if (contextosPorUsuario.containsKey(usuario.getIdentificador())) {
			return contextosPorUsuario.get(usuario.getIdentificador()).toArray(new ContextoRefundido[0]);
		} else {
			return new ContextoRefundido[0];
		}
	}

	/*
	 * (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.servicios.refundido.GestorContextosRefundidoLocal#getProceso(int)
	 */
	@Override
	public Proceso getProceso(int idProceso) {
		return em.find(Proceso.class, idProceso);
	}

	/*
	 * (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.servicios.refundido.GestorContextosRefundidoLocal#getProcesos(es.mitc.redes.urbanismoenred.servicios.seguridad.Usuario)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Proceso[] getProcesos(Usuario usuario) throws ExcepcionRefundido {
		return ((List<Proceso>)em.createNamedQuery("Proceso.obtenerPorUsuario")
				.setParameter("idUsuario", usuario.getIdentificador())
				.getResultList()).toArray(new Proceso[0]);
	}

	/*
	 * (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.servicios.refundido.GestorContextosRefundidoLocal#iniciarProceso(es.mitc.redes.urbanismoenred.servicios.refundido.ContextoRefundido)
	 */
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void iniciarProceso(ContextoRefundido contexto)
			throws ExcepcionRefundido {
		for (ContextoRefundido cr : contextosPorAmbito.get(contexto.getIdAmbito())) {
			if (Estado.INICIADO.equals(cr.getEstado())) {
				throw new ExcepcionRefundido("Existe un proceso de refundido iniciado sobre el mismo ámbito.", 3002);
			}
		}
		
		contexto.iniciarProceso();
		Proceso proceso = new Proceso();
		proceso.setAmbito(contexto.getIdAmbito());
		proceso.setInicio(GregorianCalendar.getInstance().getTime());
		proceso.setUsuario(contexto.getUsuario().getIdentificador());
		
		em.persist(proceso);
		em.flush();
		contexto.putParametro(ContextoRefundido.ID_PROCESO, proceso.getIden());
		procesos.put(contexto, proceso.getIden());
		log.debug("Proceso de refundido iniciado " + proceso.getIden());
	}

	/**
	 * 
	 * @param tramites
	 * @return
	 * @throws ExcepcionRefundido 
	 */
	@SuppressWarnings("unchecked")
	private int obtenerAmbito(List<Integer> tramites) throws ExcepcionRefundido {
		// ámbito de los trámites
    	List<Object> ambitos = em.createNamedQuery("Ambito.obtenerAmbitoRefundido")
    		.setParameter("listaTramites", tramites).getResultList();
        
        if (ambitos.size() == 0) {
        	throw new ExcepcionRefundido("No se ha encontrado el ámbito para refundir.", 3003);
        }
        if (ambitos.size() > 1) {
        	throw new ExcepcionRefundido("Se han encontrado " + ambitos.size() + " ámbitos para refundir.", 3004);
        }
        
        return Integer.parseInt(ambitos.get(0).toString());
	}

	/*
	 * (non-Javadoc)
	 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
	 */
	@Override
	public void propertyChange(PropertyChangeEvent event) {
		ContextoRefundido contexto = (ContextoRefundido)event.getSource();
		if (ContextoRefundido.AMBITO.equals(event.getPropertyName())) {
			contextosPorAmbito.get(event.getOldValue()).remove(contexto);
			List<ContextoRefundido> contextos = contextosPorAmbito.get(event.getNewValue());
			if (contextos == null) {
				contextos = new ArrayList<ContextoRefundido>();
			}
			contextos.add(contexto);
		} 		
	}

}

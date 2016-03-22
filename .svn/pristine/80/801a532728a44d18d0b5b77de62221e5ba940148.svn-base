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
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import es.mitc.redes.urbanismoenred.data.rpm.diccionario.Ambito;
import es.mitc.redes.urbanismoenred.data.rpm.diccionario.Instrumentotipooperacionplan;
import es.mitc.redes.urbanismoenred.data.rpm.diccionario.Tipotramite;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Determinacion;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidad;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Operacion;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Operaciondeterminacion;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Operacionentidad;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Operacionplan;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Operacionrelacion;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Plan;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Tramite;
import es.mitc.redes.urbanismoenred.data.rpm.seguridad.Fip1;
import es.mitc.redes.urbanismoenred.servicios.dal.ExcepcionPersistencia;
import es.mitc.redes.urbanismoenred.servicios.dal.GestorConsultasLocal;
import es.mitc.redes.urbanismoenred.servicios.planeamiento.ServicioDiccionariosLocal;
import es.mitc.redes.urbanismoenred.servicios.refundido.ContextoRefundido.LOG;
import es.mitc.redes.urbanismoenred.utils.recursos.textos.Textos;

/**
 * 
 * @author Arnaiz Consultores
 *
 */
@Stateless(name = "NuevoServicioRefundido")
public class ServicioRefundidoBean implements RefundidoLocal {
	private static final String BLOQUES_OPERACIONES = "refunido.operaciones.bloques";
	private static final Logger log = Logger.getLogger(ServicioRefundidoBean.class);
	
	private static final String OPERACIONES = "refundido.operaciones";
	
	private static final int TIPOOPERACIONPLAN_DESARROLLO = 2;
	private static final int TIPOOPERACIONPLAN_LEVANTAMIENTOPARCIAL = 8;
	private static final int TIPOOPERACIONPLAN_LEVANTAMIENTOTOTAL = 6;
	private static final int TIPOOPERACIONPLAN_MODIFICACION = 1;
	private static final int TIPOOPERACIONPLAN_NOPROCEDE = 5;
	private static final int TIPOOPERACIONPLAN_REFUNDIDO = 10;
	private static final int TIPOOPERACIONPLAN_SUSPENSIONPARCIAL = 7;
	private static final int TIPOOPERACIONPLAN_SUSPENSIONTOTAL = 3;
	private static final int TIPOOPERACIONPLAN_SUSTITUCION = 4;
	private final static Set<Integer> tipoTramitesRefundibles;
	private static final String TRAMITES_ORDENADOS = "refundido.tramites.ordenados";
	
	static {
		tipoTramitesRefundibles = new HashSet<Integer>();
        
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
	
	@PersistenceContext(unitName = "rpmv2")
	private EntityManager em;
	
	@EJB (beanName = "refundido.exportacion.fip")
	private GestorExportacionLocal exportador;
	
	@EJB
	private GestorConsultasLocal gestorConsultas;
	
	@EJB
    private GestorContextosRefundidoLocal gestorContextos;
	
	@EJB
	private LimpiadorRefundidoLocal limpiador;
	
	private SimpleDateFormat sdf= new SimpleDateFormat("dd/MM/yyyy");
	
	@EJB
    private ServicioDiccionariosLocal servicioDiccionario;

    /**
     * Default constructor. 
     */
    public ServicioRefundidoBean() {
    }

	/**
     * 
     * @param tramiteOperador
     * @param planOperado
     * @param operaciones
     * @param bloques
	 * @param traductor 
     * @throws ExcepcionRefundido
     */
    @SuppressWarnings("unchecked")
	private void agregarOperacionesDesarrollo(Tramite tramiteOperador,
			Plan planOperado, List<OperacionRefundido> operaciones,
			Map<Integer, List<Integer>> bloques, ResourceBundle traductor) throws ExcepcionRefundido {
    	
    	log.debug("Agregando operaciones derivadas de un desarrollo del plan " + tramiteOperador.getPlan().getCodigo());
    	
    	List<Tramite> tramites = em.createNamedQuery("Tramite.findVigente")
				.setParameter("idPlan", planOperado.getIden())
				.getResultList();
    	
    	if (tramites.size() > 0) {
    		
    		// Me aseguro la aportación completa de los planes involucrados.
    		// Este es un mecanismo de optimización para acelerar el proceso
    		// de aportación.
    		List<Integer> bloque;
            if (!bloques.containsKey(tramiteOperador.getPlan().getIden())) {
    			bloque = new ArrayList<Integer>();
    			bloques.put(tramiteOperador.getPlan().getIden(), bloque);
    		} else {
    			bloque = bloques.get(tramiteOperador.getPlan().getIden());
    		}
            
            bloque.add(operaciones.size());
            
            operaciones.add(new OperacionPlan(tramiteOperador.getPlan().getIden(), planOperado.getIden(), OperacionPlan.APORTACION));
    		
    		// Primero aplico las operaciones que tenga definidas. Luego 
    		// incorporo todo lo demás. Esto es un cambio con respecto a la
    		// versión anterior, que lo hacía al revés.
    		agregarOperacionesModificacion(tramiteOperador, null, operaciones, bloques, traductor);
    		
    		Tramite tramiteOperado = tramites.get(0);
    		
    		// Averigua cuál es la entidad 'Ámbito de Aplicación' del trámite operador.
            Entidad ambitoAplicacionOperador=gestorConsultas.obtenerAmbitoAplicacionPorTramite(tramiteOperador);
            
            if (ambitoAplicacionOperador == null) {
            	throw new ExcepcionRefundido(String.format(traductor.getString("refundido.agregar.desarrollo.error.sinAmbito"), 
            			tramiteOperador.getCodigofip(), 
            			tramiteOperador.getPlan().getCodigo()), 1001);
            }
            
            // Averigua cuál es la entidad 'Ámbito de Aplicación' del trámite operado.
            Entidad ambitoAplicacionOperado = gestorConsultas.obtenerAmbitoAplicacionPorTramite(tramiteOperado);
            
            if (ambitoAplicacionOperado == null) {
            	throw new ExcepcionRefundido(String.format(traductor.getString("refundido.agregar.desarrollo.error.sinAmbito"), 
            			tramiteOperado.getCodigofip(), 
            			tramiteOperado.getPlan().getCodigo()), 1002);
            }
            
            List<Entidad> entidadesOperador = new ArrayList<>();
            entidadesOperador.addAll(tramiteOperador.getEntidads());
            entidadesOperador.remove(ambitoAplicacionOperador);
            
            List<Determinacion> gruposEntidades = obtenerGrupos(entidadesOperador);
            
            for (Determinacion grupo : gruposEntidades) {
            	List<Entidad> afectadas = getEntidadesPorGrupoTramite(grupo, tramiteOperado);
            	// Se asegura que sólo se recortan entidades del trámite operador
				// que tengan el mismo grupo que la entidad incorporada.
            	for (Entidad entidad : afectadas) {
            			bloque.add(operaciones.size());
                    	operaciones.add(new OperacionEntidades(ambitoAplicacionOperador.getIden(), entidad.getIden(), OperacionEntidades.SUSTRACCION_GRAFICA));
				}
            }
            
            List<Determinacion> determinaciones = em.createNamedQuery("Determinacion.buscarAplicacionDesarrollo")
            		.setParameter("idTramite", tramiteOperador.getIden())
            		.setParameter("idCaracterVirtual", ClsDatos.ID_CARACTER_VIRTUAL)
            		.setParameter("idCaracterGrupoEntidades", ClsDatos.ID_CARACTER_GRUPODEENTIDADES)
            		.getResultList();
            
            log.debug("Calculando aportaciones de determinaciones..");
            // Aporto las determinaciones sin indicar la determinación operada,
            // que será calculada al ejecutar la operación (Creando la 
            // determinación "Aportadas por...").
            for (Determinacion determinacion : determinaciones){
               bloque.add(operaciones.size());
               operaciones.add(new OperacionDeterminaciones(determinacion.getIden(), 0, OperacionDeterminaciones.APORTACION));
            }
            
            log.debug("Fin desarrollo...");
    	} else {
    		throw new ExcepcionRefundido(String.format(traductor.getString("refundido.agregar.desarrollo.error.sinTramite"), 
    				tramiteOperador.getPlan().getCodigo(), 
    				planOperado.getCodigo()), 1003);
    	}
	}
    
    /**
	 * Devuelve todos los grupos a los que pertenecen la lista de entidades
	 * 
	 * @param incorporadas
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private List<Determinacion> obtenerGrupos(List<Entidad> incorporadas) {
		List<Determinacion> grupos = new ArrayList<Determinacion>();
		
		Query consulta = em.createNamedQuery("Determinacion.obtenerGrupoEntidad")
        		.setParameter("idCaracter",ClsDatos.ID_CARACTER_GRUPODEENTIDADES);
		List<Determinacion> grupo;
		for (Entidad incorporada : incorporadas) {
			grupo = consulta.setParameter("idEntidad", incorporada.getIden())
					.getResultList();
			
			if (!grupo.isEmpty() && !grupos.contains(grupo.get(0))) {
				grupos.add(grupo.get(0));
			}
		}
		return grupos;
	}
    
    /**
	 * Se devuelve una lista de entidades cuyo grupo de entidad es la 
	 * determinación especificada o cualquier otra que tenga la misma 
	 * determinación base que ella.
	 * 
	 * @param detGrupoEntidades
	 * @param tramite
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private List <Entidad> getEntidadesPorGrupoTramite(Determinacion detGrupoEntidades, Tramite tramite) {
    	
        List<Entidad> resultado =new ArrayList<Entidad>();

        // 1 Entidades que tienen como alguno de sus valores de referencia a detGrupoEntidades
        resultado = em.createNamedQuery("Entidad.obtenerPorValorRefTramite")
        		.setParameter("idValorRef", detGrupoEntidades.getIden())
        		.setParameter("idTramite", tramite.getIden())
        		.getResultList();

        if (detGrupoEntidades.getDeterminacionByIddeterminacionbase() != null) {
        	// 2 Entidades que tienen como alguno de sus valores de referencia a alguna
            // determinación cuyo determinación base es la misma que la de detGrupoEntidades
            resultado.addAll(em.createNamedQuery("Entidad.obtenerPorValorRefBaseTramite")
            		.setParameter("idValorRef", detGrupoEntidades.getIden())
            		.setParameter("idBase",detGrupoEntidades.getDeterminacionByIddeterminacionbase().getIden())
            		.setParameter("idTramite", tramite.getIden())
            		.getResultList());
        }
   
        return resultado;
    }
    
    /**
     * 
     * @param tramiteOperador
     * @param planOperado
     * @param operaciones
     * @param bloques
     * @param traductor 
     * @throws ExcepcionRefundido
     */
    @SuppressWarnings("unchecked")
	private void agregarOperacionesLevantamientoParcial(Tramite tramiteOperador,
			Plan planOperado, List<OperacionRefundido> operaciones,
			Map<Integer, List<Integer>> bloques, ResourceBundle traductor) throws ExcepcionRefundido {
    	log.debug("Agregando operaciones derivadas del levantamiento parcial del plan " + tramiteOperador.getPlan().getCodigo());
    	List<Tramite> tramites = em.createNamedQuery("Tramite.findVigente")
				.setParameter("idPlan", planOperado.getIden())
				.getResultList();
    	
    	if (tramites.size() > 0) {
    		List<Integer> bloque;
            if (!bloques.containsKey(tramiteOperador.getPlan().getIden())) {
    			bloque = new ArrayList<Integer>();
    			bloques.put(tramiteOperador.getPlan().getIden(), bloque);
    		} else {
    			bloque = bloques.get(tramiteOperador.getPlan().getIden());
    		}
            
            bloque.add(operaciones.size());
            
            operaciones.add(new OperacionPlan(tramiteOperador.getPlan().getIden(), planOperado.getIden(), OperacionPlan.APORTACION));
            
            bloque.add(operaciones.size());
            
            operaciones.add(new OperacionPlan(tramiteOperador.getPlan().getIden(), planOperado.getIden(), OperacionPlan.APORTACION));
            
    		// Se levanta parcialmente la suspensión de todas las entidades del tramite
            //  operado mediante el ámbito de aplicación del trámite operador.
//            Entidad entidadOperadora = gestorConsultas.obtenerAmbitoAplicacionPorTramite(tramiteOperador);
//            if (entidadOperadora!=null){ 
//                List<Entidad> entidadesOperadas = em.createNamedQuery("Entidad.buscarPorTramite")
//                		.setParameter("idTramite", tramites.get(0).getIden())
//                		.getResultList();
//                for (Entidad entidadOperada: entidadesOperadas){
//                	bloque.add(operaciones.size());
//                	operaciones.add(new OperacionEntidades(entidadOperadora.getIden(), entidadOperada.getIden(), OperacionEntidades.LEVANTAMIENTO_PARCIAL_SUSPENSION));
//                }
//            } else {
//            	log.warn(String.format(traductor.getString("refundido.agregar.levantamientoParcial.aviso.sinAmbito"), planOperado.getCodigo(), tramiteOperador.getPlan().getCodigo()));
//            }
    	} else {
    		throw new ExcepcionRefundido(String.format(traductor.getString("refundido.agregar.levantamientoParcial.error.sinTramite"), 
    				tramiteOperador.getPlan().getCodigo(), 
    				planOperado.getCodigo()), 1004);
    	}
	}

    /**
     * 
     * @param tramite Trámite operador
     * @param planOperado 
     * @param operacionesRefundido
     * @param bloques
     * @param traductor 
     */
	@SuppressWarnings("unchecked")
	private void agregarOperacionesModificacion(Tramite tramite,
			Plan planOperado, List<OperacionRefundido> operacionesRefundido,
			Map<Integer, List<Integer>> bloques, ResourceBundle traductor) {
		log.debug("Agregando operaciones derivadas de la modificación ordenada por el plan " + tramite.getPlan().getCodigo());
		
		// Ordena las operaciones del trámite operador por el campo 'orden'
        List<Operacion> operaciones = em.createNamedQuery("Operacion.buscarPorTramite")
        		.setParameter("idTramite", tramite.getIden())
        		.getResultList();
		int posicion;
		List<Integer> bloque;
		if (!bloques.containsKey(tramite.getPlan().getIden())) {
			bloque = new ArrayList<Integer>();
			bloques.put(tramite.getPlan().getIden(), bloque);
		} else {
			bloque = bloques.get(tramite.getPlan().getIden());
		}
		
		if (planOperado != null) {
			bloque.add(operacionesRefundido.size());
			operacionesRefundido.add(new OperacionPlan(tramite.getPlan().getIden(), planOperado.getIden(), OperacionPlan.APORTACION));
		}
		
        for (Operacion operacion : operaciones) {
        	posicion = operacionesRefundido.size();
        	// Se da por hecho que una Operacion sólo puede ser apuntada por
            // una OperacionDeterminacion o una OperacionEntidad. Por lo tanto,
            // aunque la clase Operacion contiene un Set de OperacionDeterminacion,
            // sólo debe contener un elemento y, por consiguiente, solamente se
            // toma el primero.
        	if (operacion.getOperaciondeterminacions().size() > 0) {
        		
        		Operaciondeterminacion od = operacion.getOperaciondeterminacions().toArray(new Operaciondeterminacion[0])[0];
        		log.debug("Agregando operación de determinación tipo " + od.getIdtipooperaciondet());
        		
        		operacionesRefundido.add(new OperacionDeterminaciones(od.getDeterminacionByIddeterminacionoperadora().getIden(), 
        				od.getDeterminacionByIddeterminacion().getIden(), 
        				1000+od.getIdtipooperaciondet()));
        		bloque.add(posicion);
        	} else if (operacion.getOperacionentidads().size() > 0) {
        		Operacionentidad oe = operacion.getOperacionentidads().toArray(new Operacionentidad[0])[0];
        		log.debug("Agregando operación de entidad tipo " + oe.getIdtipooperacionent());
        		
        		operacionesRefundido.add(new OperacionEntidades(oe.getEntidadByIdentidadoperadora().getIden(), 
        				oe.getEntidadByIdentidad().getIden(), 
        				2000+oe.getIdtipooperacionent()));
        		
        		bloque.add(posicion);
        	} else if (operacion.getOperacionrelacions().size() > 0) {
        		Operacionrelacion or = operacion.getOperacionrelacions().toArray(new Operacionrelacion[0])[0];
        		log.debug("Agregando operación de relación tipo " + or.getIdtipooperacionrel());
        		
        		operacionesRefundido.add(new OperacionRelacion(or.getRelacion().getIden(), 
        				3000+or.getIdtipooperacionrel()));
        		
        		bloque.add(posicion);
        	} else {
        		log.warn(String.format(traductor.getString("refundido.agregar.modificacion.aviso.operacionDesconocida"), operacion.getIden()));
        	}
        }
	}

	/**
     * 
     * @param tramiteOperador
     * @param planOperado
     * @param operaciones
     * @param bloques
	 * @param traductor 
     * @throws ExcepcionRefundido
     */
    @SuppressWarnings("unchecked")
	private void agregarOperacionesSuspensionParcial(Tramite tramiteOperador,
			Plan planOperado, List<OperacionRefundido> operaciones,
			Map<Integer, List<Integer>> bloques, ResourceBundle traductor) throws ExcepcionRefundido {
    	log.debug("Agregando operaciones derivadas de la suspensión parcial del plan " + tramiteOperador.getPlan().getCodigo());
    	
    	List<Tramite> tramites = em.createNamedQuery("Tramite.findVigente")
				.setParameter("idPlan", planOperado.getIden())
				.getResultList();
    	
    	if (tramites.size() > 0) {
    		List<Integer> bloque;
            if (!bloques.containsKey(tramiteOperador.getPlan().getIden())) {
    			bloque = new ArrayList<Integer>();
    			bloques.put(tramiteOperador.getPlan().getIden(), bloque);
    		} else {
    			bloque = bloques.get(tramiteOperador.getPlan().getIden());
    		}
            
            bloque.add(operaciones.size());
            
            operaciones.add(new OperacionPlan(tramiteOperador.getPlan().getIden(), planOperado.getIden(), OperacionPlan.APORTACION));
            
            bloque.add(operaciones.size());
            
            operaciones.add(new OperacionPlan(tramiteOperador.getPlan().getIden(), planOperado.getIden(), OperacionPlan.SUSPENSION_PARCIAL));
            
    		// Se suspenden parcialmente todas las entidades del tramite
            //  operado mediante el ámbito de aplicación del trámite operador.
//            Entidad entidadOperadora = gestorConsultas.obtenerAmbitoAplicacionPorTramite(tramiteOperador);
//            if (entidadOperadora!=null){ 
//                List<Entidad> entidadesOperadas = em.createNamedQuery("Entidad.buscarPorTramite")
//                		.setParameter("idTramite", tramites.get(0).getIden())
//                		.getResultList();
//                boolean mismaGeometria = false;
//                for (Entidad entidadOperada: entidadesOperadas){
//                	if (!entidadOperadora.getEntidadpols().isEmpty() && !entidadOperada.getEntidadpols().isEmpty()) {
//                		mismaGeometria = true;
//                	} else if (!entidadOperadora.getEntidadlins().isEmpty() && !entidadOperada.getEntidadlins().isEmpty()) {
//                		mismaGeometria = true;
//                	} else if (!entidadOperadora.getEntidadpnts().isEmpty() && !entidadOperada.getEntidadpnts().isEmpty()) {
//                		mismaGeometria = true;
//                	} else {
//                		mismaGeometria = false;
//                	}
//                	if (mismaGeometria) {
//                		bloque.add(operaciones.size());
//                    	operaciones.add(new OperacionEntidades(entidadOperadora.getIden(), entidadOperada.getIden(), OperacionEntidades.SUSPENSION_PARCIAL));
//                	}
//                }
//            } else {
//            	log.warn(String.format(traductor.getString("refundido.agregar.suspensionParcial.aviso.sinAmbito"), planOperado.getCodigo(), tramiteOperador.getPlan().getCodigo()));
//            }
    	} else {
    		throw new ExcepcionRefundido(String.format(traductor.getString("refundido.agregar.suspensionParcial.error.sinTramite"), 
    				tramiteOperador.getPlan().getCodigo(), 
    				planOperado.getCodigo()), 1005);
    	}
	}

	/**
     * 
     * @param tramite
     * @param operaciones
     * @param bloques
	 * @param traductor 
     */
    private void agregarOperacionIncorporacion(Tramite tramiteOperador,
			List<OperacionRefundido> operaciones,
			Map<Integer, List<Integer>> bloques, ResourceBundle traductor) {
    	log.debug("Agregando operación de incorporación del plan " + tramiteOperador.getPlan().getCodigo());
    	
    	List<Integer> bloque;
        if (!bloques.containsKey(tramiteOperador.getPlan().getIden())) {
			bloque = new ArrayList<Integer>();
			bloques.put(tramiteOperador.getPlan().getIden(), bloque);
		} else {
			bloque = bloques.get(tramiteOperador.getPlan().getIden());
		}
        
        bloque.add(operaciones.size());
        operaciones.add(new OperacionPlan(tramiteOperador.getPlan().getIden()
        		, 0
        		, OperacionPlan.INCORPORACION));
	}

	/**
     * 
     * @param tramiteOperador
     * @param planOperado
     * @param operaciones
     * @param bloques
	 * @param traductor 
     */
    private void agregarOperacionLevantamientoTotal(Tramite tramiteOperador,
			Plan planOperado, List<OperacionRefundido> operaciones,
			Map<Integer, List<Integer>> bloques, ResourceBundle traductor) {
    	log.debug("Agregando operación de levantamiento total del plan " + tramiteOperador.getPlan().getCodigo());
    	
    	List<Integer> bloque;
        if (!bloques.containsKey(tramiteOperador.getPlan().getIden())) {
			bloque = new ArrayList<Integer>();
			bloques.put(tramiteOperador.getPlan().getIden(), bloque);
		} else {
			bloque = bloques.get(tramiteOperador.getPlan().getIden());
		}
        
        bloque.add(operaciones.size());
        operaciones.add(new OperacionPlan(tramiteOperador.getPlan().getIden()
        		, planOperado.getIden()
        		, OperacionPlan.LEVANTAMIENTO_TOTAL));
	}

	/**
     * 
     * @param tramite
     * @param planByIdplanoperado
     * @param operaciones
     * @param bloques
	 * @param traductor 
     */
	private void agregarOperacionRefundido(Tramite tramiteOperador,
			Plan planOperado, List<OperacionRefundido> operaciones,
			Map<Integer, List<Integer>> bloques, ResourceBundle traductor) {
		log.debug("Agregando operaciones derivadas del refundido del plan " + tramiteOperador.getPlan().getCodigo());
		
		List<Integer> bloque;
        if (!bloques.containsKey(tramiteOperador.getPlan().getIden())) {
			bloque = new ArrayList<Integer>();
			bloques.put(tramiteOperador.getPlan().getIden(), bloque);
		} else {
			bloque = bloques.get(tramiteOperador.getPlan().getIden());
		}
        
        bloque.add(operaciones.size());
		operaciones.add(new OperacionPlan(tramiteOperador.getPlan().getIden()
        		, planOperado.getIden()
        		, OperacionPlan.SUSPENSION_TOTAL));
		
		bloque.add(operaciones.size());
		operaciones.add(new OperacionPlan(tramiteOperador.getPlan().getIden()
        		, planOperado.getIden()
        		, OperacionPlan.INCORPORACION));
	}

	/**
     * 
     * @param tramiteOperador
     * @param planOperado
     * @param operaciones
     * @param bloques
	 * @param traductor 
     */
    private void agregarOperacionSuspensionTotal(Tramite tramiteOperador,
			Plan planOperado, List<OperacionRefundido> operaciones,
			Map<Integer, List<Integer>> bloques, ResourceBundle traductor) {
    	log.debug("Agregando operaciones de suspensión total del plan " + tramiteOperador.getPlan().getCodigo());
    	List<Integer> bloque;
        if (!bloques.containsKey(tramiteOperador.getPlan().getIden())) {
			bloque = new ArrayList<Integer>();
			bloques.put(tramiteOperador.getPlan().getIden(), bloque);
		} else {
			bloque = bloques.get(tramiteOperador.getPlan().getIden());
		}
        
        bloque.add(operaciones.size());
        operaciones.add(new OperacionPlan(tramiteOperador.getPlan().getIden()
        		, planOperado.getIden()
        		, OperacionPlan.SUSPENSION_TOTAL));
	}

	/**
     * 
     * @param tramiteOperador
     * @param planOperado
     * @param operaciones
     * @param bloques
	 * @param traductor 
     */
    private void agregarOperacionSustitucion(Tramite tramiteOperador,
			Plan planOperado, List<OperacionRefundido> operaciones,
			Map<Integer, List<Integer>> bloques, ResourceBundle traductor) {
    	log.debug("Agregando operaciones derivadas de la sustitución del plan " + tramiteOperador.getPlan().getCodigo());
    	
    	List<Integer> bloque;
        if (!bloques.containsKey(tramiteOperador.getPlan().getIden())) {
			bloque = new ArrayList<Integer>();
			bloques.put(tramiteOperador.getPlan().getIden(), bloque);
		} else {
			bloque = bloques.get(tramiteOperador.getPlan().getIden());
		}
        
        // Si el trámite sustituto no tiene entidades suspendidas y no existen 
        // operaciones de incorporación definidas podemos cancelar la 
        // ejecución de las operaciones definidas por ese plan y sus  hijos.
        boolean cancelar = em.createNamedQuery("Operacion.buscarOperacionesIncorporacion")
        		.setParameter("orden", tramiteOperador.getPlan().getOrden())
        		.getResultList().size() == 0 
        		&& em.createNamedQuery("Entidad.BuscarSuspendidasPorTramite")
        		.setParameter("idTramite", tramiteOperador.getIden())
        		.getResultList().size() == 0;

        if (cancelar) {
        	bloque.add(operaciones.size());
            operaciones.add(new OperacionPlan(tramiteOperador.getPlan().getIden()
            		, planOperado.getIden()
            		, OperacionPlan.SUSPENSION_TOTAL));
        } else {
        	// Se cancela las operaciones del plan sustituido que no sean a su 
        	// vez de sustitución o de incorporación
        	OperacionRefundido operacion;
        	for(Integer indice : bloques.get(planOperado.getIden())) {
        		operacion = operaciones.get(indice);
        		
        		if (OperacionPlan.SUSTITUCION != operacion.getTipo() 
        				&& OperacionEntidades.INCORPORACION != operacion.getTipo()
        				&& OperacionPlan.INCORPORACION != operacion.getTipo()) {
        			operacion.setCancelada(true);
        		}
        	}
        	
        	bloque.add(operaciones.size());
            operaciones.add(new OperacionPlan(tramiteOperador.getPlan().getIden()
            		, planOperado.getIden()
            		, OperacionPlan.PROPONER_ELIMINAR));
        }
        
        bloque.add(operaciones.size());
        operaciones.add(new OperacionPlan(tramiteOperador.getPlan().getIden()
        		, planOperado.getIden()
        		, OperacionPlan.SUSTITUCION));
        
        // Si tiene operaciones definidas o entidades suspendidas se aplican
        // dichas operaciones y se borran los objetos que no han sido 
        // incorporados al plan que sustituye.
        if (!cancelar) {
        	agregarOperacionesModificacion(tramiteOperador, null, operaciones, bloques, traductor);
        	
        	bloque.add(operaciones.size());
            operaciones.add(new OperacionPlan(tramiteOperador.getPlan().getIden()
            		, planOperado.getIden()
            		, OperacionPlan.ELIMINAR_PROPUESTOS));
        }
	}

	/**
	 * Se debe recorrer todas las operaciones para cancelar la ejecución de 
	 * aquellas que tengan que ver con planes suspendidos o que hayan sido 
	 * sustituidos por otros que a su vez no tengan operaciones de incorporación
	 * ni entidades suspendidas.
	 * 
	 * @param contexto
	 */
	@SuppressWarnings("unchecked")
	private void aplicarCancelaciones(ContextoRefundido contexto) {
		contexto.logTraducido(LOG.INFO, "refundido.cancelacion.operaciones");
		OperacionRefundido[] operaciones = (OperacionRefundido[])contexto.getParametro(OPERACIONES);
		Map<Integer, List<Integer>> bloques = (Map<Integer, List<Integer>>)contexto.getParametro(BLOQUES_OPERACIONES);
		List<Integer> planesSuspendidos = new ArrayList<Integer>();
		OperacionPlan operacion;
		ResourceBundle traductor = (ResourceBundle) contexto.getParametro(ContextoRefundido.TRADUCTOR);
		
		// Primero se recorren las operaciones para revisar las suspensiones.
		for (int i = 0; i < operaciones.length; i++) {
			switch (operaciones[i].getTipo()) {
				case OperacionPlan.SUSPENSION_TOTAL:
						operacion = (OperacionPlan)operaciones[i];
						if (planesSuspendidos.contains(operacion.getPlanOperado())) {
							Plan plan = em.find(Plan.class, operacion.getPlanOperado());
							if (plan != null) {
								contexto.log(LOG.AVISO, String.format(traductor.getString("refundido.aplicar.suspensiones.aviso.planSuspendido"), plan.getCodigo()));
							} else {
								log.warn("Detectada operación de suspensión sobre plan inexistente: " + operacion.getPlanOperado());
							}
						} else {
							planesSuspendidos.add(operacion.getPlanOperado());
						}
					break;
				case OperacionPlan.LEVANTAMIENTO_TOTAL:
						operacion = (OperacionPlan)operaciones[i];
						if (planesSuspendidos.contains(operacion.getPlanOperado())) {
							planesSuspendidos.remove(Integer.valueOf(operacion.getPlanOperado()));
						} else {
							Plan plan = em.find(Plan.class, operacion.getPlanOperado());
							if (plan != null) {
								contexto.log(LOG.AVISO, String.format(traductor.getString("refundido.aplicar.suspensiones.aviso.planNoSuspendido"), plan.getCodigo()));
							} else {
								log.warn("Detectada operación de suspensión sobre plan inexistente: " + operacion.getPlanOperado());
							}
						}
					break;
				default:
					break;
			}
		}
		
		// Una vez que se tiene identificados los planes suspendidos
		// se procede a cancelar todas sus operaciones y las de sus planes hijos.
		Plan planSuspendido;
		for (Integer idPlanSuspendido : planesSuspendidos) {
			planSuspendido = em.find(Plan.class, idPlanSuspendido);
			if (planSuspendido != null) {
				cancelar(planSuspendido, operaciones, bloques);
			} else {
				log.warn("El plan suspendido no existe: " + idPlanSuspendido);
			}
			
		}
	}
	
	/**
	 * Cancela todas las operaciones de un plan y de sus planes hijos.
	 * 
	 * @param idPlanSuspendido
	 * @param operaciones
	 * @param bloques
	 */
	private void cancelar(Plan planSuspendido,
			OperacionRefundido[] operaciones,
			Map<Integer, List<Integer>> bloques) {
		// Se cancelan sus operaciones
		if (bloques.containsKey(planSuspendido.getIden())) {
			log.debug("Cancelando plan " + planSuspendido.getCodigo() + " id " + planSuspendido.getIden());
			for (int indice : bloques.get(planSuspendido.getIden())) {
				operaciones[indice].setCancelada(true);
			}
		} else {
			log.warn("El plan " + planSuspendido.getIden() + " no tiene operaciones que cancelar.");
		}
		// Luego se cancelan las operaciones de sus planes hijos
		for (Plan hijo : planSuspendido.getPlansForIdpadre()) {
			cancelar(hijo,operaciones, bloques);
		}
	}

	/**
	 * El trámite resultante del refundido se guarda en un plan cuyo instrumento
	 * sea "Plan Refundido" que se debe crear en el esquema de planeamiento.
	 * Dicho plan debe operar con instrumento "Plan Refundido" y operación 
	 * "Refundido" sobre el plan que se utiliza como base para el refundido.
	 * 
	 * @param contexto
	 * @throws ExcepcionRefundido 
	 */
	@SuppressWarnings("unchecked")
	private void crearTrámiteRefundido(ContextoRefundido contexto) throws ExcepcionRefundido {
		// Obtenemos el plan sobre el que se basará el refundido. Para ello 
		// recorremos las operaciones buscando el plan que incorpore sus datos
		// o el que sustituya a dicho plan.
		OperacionRefundido[] operaciones = (OperacionRefundido[])contexto.getParametro(OPERACIONES);
		ResourceBundle traductor = (ResourceBundle) contexto.getParametro(ContextoRefundido.TRADUCTOR);
		
		int idPlanBase = 0;
		for(OperacionRefundido operacion : operaciones) {
			if (operacion.getTipo() == OperacionPlan.INCORPORACION) {
				idPlanBase = ((OperacionPlan)operacion).getPlanOperador();
			} else if (operacion.getTipo() == OperacionPlan.SUSTITUCION) {
				if (idPlanBase == ((OperacionPlan)operacion).getPlanOperado()) {
					idPlanBase = ((OperacionPlan)operacion).getPlanOperador();
				}
			}
		}
		
		if (idPlanBase != 0) {
			Plan planBase = em.find(Plan.class, idPlanBase);
			try {
				Instrumentotipooperacionplan itop = obtenerInstrumento(contexto);
				
				List<Plan> planesRefundidos = em.createNamedQuery("Plan.obtenerPlanResultanteRefundido")
			        	.setParameter("idPlan", idPlanBase)
			        	.setParameter("idItop", itop.getIden())
			        	.getResultList();
				Plan plan = new Plan();
		        if (planesRefundidos.size() > 0) {
		        	plan = planesRefundidos.get(0);
		        } else {
		        	plan.setNombre(String.format(traductor.getString("refundido.plan.refundido.nombre"), contexto.getParametro(ContextoRefundido.NOMBRE_AMBITO_REFUNDIDO)));
			        plan.setCodigo(gestorConsultas.getSiguienteCodigoPlan());
			        plan.setPlanByIdplanbase(planBase.getPlanByIdplanbase());
			        plan.setIdambito(planBase.getIdambito());
			        plan.setBsuspendido(planBase.getBsuspendido());
			        int orden = 0;
			        try {
			        	orden = (Integer)em.createNativeQuery("Select 1+Max(orden) " +
			        			"From planeamiento.Plan Where idAmbito=" + planBase.getIdambito()).getSingleResult();
			        } catch (NoResultException nre) {
			        	log.warn("No se ha encontrado ningún plan del ámbito especificado ("+planBase.getIdambito()+"). " + nre.getMessage());
			        }
			        plan.setOrden(orden);
			        em.persist(plan);
			        
			        Operacionplan op = new Operacionplan();
			        op.setPlanByIdplanoperador(plan);
			        op.setPlanByIdplanoperado(planBase);
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
		        
		        tramite.setPlan(plan);
		        tramite.setFecha(Calendar.getInstance().getTime());
		        tramite.setTexto(String.format(traductor.getString("refundido.tramite.refundido.texto"), plan.getCodigo()));
		        tramite.setNombre(plan.getNombre());
		        tramite.setIteracion(iteracion);
		        
		        
		        //  Calcula un código FIP nuevo en función del ámbito, plan, tipo de trámite, e iteración.
		        // Encripta el código FIP calculado
	            String codigoFIPencriptado = gestorConsultas.generarCodigoTramite(tramite);
	            
		        tramite.setCodigofip(codigoFIPencriptado);
		        
		        // Mensaje con los datos del trámite que se va a crear
		        contexto.log(ContextoRefundidoBasico.LOG.INFO,String.format(traductor.getString("refundido.plan.refundido.mostrar.nombre"), plan.getNombre()));
		        contexto.log(ContextoRefundidoBasico.LOG.INFO,String.format(traductor.getString("refundido.plan.refundido.mostrar.codigo"), plan.getCodigo()));
		        contexto.log(ContextoRefundidoBasico.LOG.INFO,String.format(traductor.getString("refundido.tramite.refundido.mostrar.fecha"), sdf.format(tramite.getFecha())));
		        contexto.log(ContextoRefundidoBasico.LOG.INFO,String.format(traductor.getString("refundido.tramite.refundido.mostrar.iteracion"), iteracion));
		        contexto.log(ContextoRefundidoBasico.LOG.INFO,String.format(traductor.getString("refundido.tramite.refundido.mostrar.codigofip"), codigoFIPencriptado));
		        
		        em.persist(tramite);
		        
		        contexto.putParametro(ContextoRefundido.ESQUELETO_TRAMITE_REFUNDIDO, tramite);
			} catch (NoResultException nre) {
				throw new ExcepcionRefundido(traductor.getString("refundido.crear.tramite.refundido.error.operacion.refundido"), 1006);
			} catch (NonUniqueResultException nure) {
				throw new ExcepcionRefundido(traductor.getString("refundido.crear.tramite.refundido.error.multiplesInstrumentos"), 1007);
			} catch (ExcepcionPersistencia e) {
				throw new ExcepcionRefundido(String.format(traductor.getString("refundido.crear.tramite.refundido.error.crear"), e.getMessage()), 1008);
			}
		} else {
			throw new ExcepcionRefundido(traductor.getString("refundido.crear.tramite.refundido.error.noPlanBase"), 1009);
		}
	}
	
	/**
	 * Ejecuta las operaciones de refundido definidas en el contexto de 
	 * refundido que no se encuentren canceladas.
	 * 
	 * @param contexto Contexto de refundido.
	 * @throws ExcepcionRefundido 
	 */
	private void ejecutarOperaciones(ContextoRefundido contexto) throws ExcepcionRefundido {
		contexto.logTraducido(LOG.INFO, "refundido.ejecutar.operaciones.inicio");
		OperacionRefundido[] operaciones = (OperacionRefundido[])contexto.getParametro(OPERACIONES);
		int contador = 0;
		int total = operaciones.length;
		for(OperacionRefundido operacion : operaciones) {
			contexto.logTraducido(LOG.INFO, "refundido.ejecutar.operaciones.contador", ++contador, total);
			
			operacion.ejecutar(contexto);
			em.flush();
		}
		
		contexto.logTraducido(LOG.INFO, "refundido.ejecutar.operaciones.fin");
	}
	
	/*
	 * (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.servicios.refundido.RefundidoLocal#esRefundible(int)
	 */
	@Override
    public boolean esRefundible(int idTramite) {
    	Tramite tramite = em.find(Tramite.class, idTramite);
		if (tramite != null) {
			return tipoTramitesRefundibles.contains(tramite.getIdtipotramite());
		}
		return false;
    }

	/**
     * A partir de los trámites involucrados en el refundido se generan las 
     * acciones concretas en las que se desglosan las operaciones definidas 
     * entre los trámites.
     * 
     * @param contexto Contexto donde se almacena la información necesaria para
     * realizar el refundido.
     * @throws ExcepcionRefundido
     */
    private void generarOperaciones(ContextoRefundido contexto) throws ExcepcionRefundido {
    	
    	contexto.logTraducido(LOG.INFO, "refundido.generar.operaciones.inicio");
    	List<OperacionRefundido> operaciones = new ArrayList<OperacionRefundido>();
    	
    	// En esta estructura almacenamos la referencia a las operaciones de los 
    	// planes dentro de la lista de operaciones
    	Map<Integer,List<Integer>> bloques = new HashMap<Integer, List<Integer>>();
    	
    	Tramite[] tramites = (Tramite[]) contexto.getParametro(TRAMITES_ORDENADOS);
    	
    	// En los refundidos parciales pueden definirse operaciones sobre planes
    	// no incluidos en el refundido.
    	List<Integer> planesIncluidos = new ArrayList<>();
    	
    	for (Tramite tramite: tramites) {
			if (!planesIncluidos.contains(tramite.getPlan().getIden())) {
				planesIncluidos.add(tramite.getPlan().getIden());
			}
		}
    	
    	ResourceBundle traductor = (ResourceBundle) contexto.getParametro(ContextoRefundido.TRADUCTOR);
		
		Instrumentotipooperacionplan itop;
		for (Tramite tramite: tramites) {
			contexto.logTraducido(LOG.INFO, "refundido.generar.operaciones.plan", tramite.getPlan().getCodigo());
			// Obtenemos los planes contra los que opera este trámite.
			for (Operacionplan op : tramite.getPlan().getOperacionplansForIdplanoperador()) {
				itop = em.find(Instrumentotipooperacionplan.class, op.getIdinstrumentotipooperacion());
				
				if (op.getPlanByIdplanoperado() == null || planesIncluidos.contains(op.getPlanByIdplanoperado().getIden())) {
					
					switch (itop.getTipooperacionplan().getIden()) {
						case TIPOOPERACIONPLAN_MODIFICACION:
							agregarOperacionesModificacion(tramite, op.getPlanByIdplanoperado(), operaciones, bloques, traductor);
							break;
						case TIPOOPERACIONPLAN_DESARROLLO:
							agregarOperacionesDesarrollo(tramite, op.getPlanByIdplanoperado(), operaciones, bloques, traductor);
							break;
						case TIPOOPERACIONPLAN_SUSPENSIONTOTAL:
							agregarOperacionSuspensionTotal(tramite,op.getPlanByIdplanoperado(), operaciones, bloques, traductor);
							break;
						case TIPOOPERACIONPLAN_SUSTITUCION:
							agregarOperacionSustitucion(tramite, op.getPlanByIdplanoperado(), operaciones, bloques, traductor);
							break;
						case TIPOOPERACIONPLAN_NOPROCEDE:
							// Sólo si la operacion "no procede" es la única.
							if (tramite.getPlan().getOperacionplansForIdplanoperador().size() == 1) {
								agregarOperacionIncorporacion(tramite, operaciones,bloques, traductor);
							}
							break;
						case TIPOOPERACIONPLAN_LEVANTAMIENTOTOTAL:
							agregarOperacionLevantamientoTotal(tramite,op.getPlanByIdplanoperado(), operaciones, bloques, traductor);
							break;
						case TIPOOPERACIONPLAN_SUSPENSIONPARCIAL:
							agregarOperacionesSuspensionParcial(tramite, op.getPlanByIdplanoperado(), operaciones, bloques, traductor);
							break;
						case TIPOOPERACIONPLAN_LEVANTAMIENTOPARCIAL:
							agregarOperacionesLevantamientoParcial(tramite, op.getPlanByIdplanoperado(), operaciones, bloques, traductor);
							break;
						case TIPOOPERACIONPLAN_REFUNDIDO:
							agregarOperacionRefundido(tramite, op.getPlanByIdplanoperado(), operaciones, bloques, traductor);
							break;
						default:
							break;
					}
				} else {
					// Si es un refundido parcial y no se ha incluido el plan
					// sustituido, se cambia por una incorporación, siempre y 
					// cuando sea una figura principal.
					// Esto aún puede fallar si se cogen planes aislados
					// p.ej. si se coge el primer plan general, se deja el
					// que lo sustituye y se coge el que sustituye al segundo.
					if (itop.getTipooperacionplan().getIden() == TIPOOPERACIONPLAN_SUSTITUCION 
							&& tramite.getPlan().getPlanByIdpadre() == null) {
						agregarOperacionIncorporacion(tramite, operaciones,bloques, traductor);
					} else {
						contexto.logTraducido(LOG.AVISO, 
								"refundido.generar.operaciones.aviso.planNoIncluido",
								op.getPlanByIdplanoperador().getCodigo(), 
								op.getPlanByIdplanoperado().getCodigo());
					}
				}
			}
			
			if (log.isDebugEnabled() && bloques.containsKey(tramite.getPlan().getIden())) {
				StringBuffer sb = new StringBuffer();
				for (Integer indice : bloques.get(tramite.getPlan().getIden())) {
					sb.append(indice);
					sb.append(", ");
				}
				
				log.debug("Operaciones del plan " + tramite.getPlan().getCodigo() + " id " + tramite.getIden() + ": " + sb.toString());
			}
		}
		
		contexto.putParametro(OPERACIONES, operaciones.toArray(new OperacionRefundido[0]));
		contexto.putParametro(BLOQUES_OPERACIONES, bloques);
		contexto.logTraducido(LOG.INFO, "refundido.generar.operaciones.fin");
    }

    /**
     * 
     * @param listaIdTramites
     * @throws ExcepcionRefundido
     */
	private void mostrarDatos(ContextoRefundido contexto) throws ExcepcionRefundido {
        String nombreAmbitoRefundido = servicioDiccionario.getTraduccion(Ambito.class, contexto.getIdAmbito(), "es");
        
        ResourceBundle traductor = (ResourceBundle) contexto.getParametro(ContextoRefundido.TRADUCTOR);
        
        contexto.log(ContextoRefundido.LOG.INFO,String.format(traductor.getString("refundido.ambito"), nombreAmbitoRefundido));
     
        contexto.putParametro(ContextoRefundido.NOMBRE_AMBITO_REFUNDIDO, nombreAmbitoRefundido);
        // Trámites
        
        Tramite tramite;
        for (int i = 0; i < contexto.getTramites().size(); i++) {
        	tramite = em.find(Tramite.class, contexto.getTramites().get(i));
        	if (tramite != null) {
        		 contexto.logTraducido(LOG.INFO,"refundido.mostrar.tramites"
        				 ,i
        				 ,servicioDiccionario.getTraduccion(Tipotramite.class, tramite.getIdtipotramite(), "es")
        				 ,tramite.getFecha() != null?sdf.format(tramite.getFecha()):"No especificada"
        				 ,tramite.getPlan().getCodigo()
        				 ,tramite.getPlan().getNombre());
        	} else {
        		throw new ExcepcionRefundido(String.format(traductor.getString("refundido.mostrar.error.tramite"), contexto.getTramites().get(i)), 1011);
        	}
        }
    }

	/**
     * Muestra las operaciones que se van a realizar
     * 
     * @param contexto
     */
    @SuppressWarnings("unchecked")
	private void mostrarOperaciones(ContextoRefundido contexto) {
    	Tramite[] tramites = (Tramite[])contexto.getParametro(TRAMITES_ORDENADOS);
    	
    	ResourceBundle traductor = (ResourceBundle) contexto.getParametro(ContextoRefundido.TRADUCTOR);
    			
    	Query query = em.createNamedQuery("Tramite.mostrarOperaciones");
    	List<Object[]> datos;
		for (int i = 0; i< tramites.length;i++) {
			datos = query.setParameter("idPlan", tramites[i].getPlan().getIden())
				.setParameter("idioma", "es").getResultList();
			for (Object[] fila : datos) {
				contexto.logTraducido(LOG.INFO, "refundido.mostrar.operaciones", 
						i,
						tramites[i].getPlan().getCodigo(),
						tramites[i].getCodigofip(),
						tramites[i].getFecha() != null? sdf.format(tramites[i].getFecha()): traductor.getString("refundido.noEspecificada"),
						fila[0],
						fila[1] != null? fila[1] : traductor.getString("refundido.noEspecificado"));
			}
		}
	}

	/**
     * 
     * @param contexto
     * @return
     * @throws ExcepcionRefundido
     */
    @SuppressWarnings("unchecked")
	private void obtenerAmbito(ContextoRefundido contexto) throws ExcepcionRefundido {
    	 // Ámbito de los trámites
    	List<Object> ambitos = em.createNamedQuery("Ambito.obtenerAmbitoRefundido")
    		.setParameter("listaTramites", contexto.getTramites()).getResultList();
        
        if (ambitos.size() == 0) {
        	throw new ExcepcionRefundido("No se ha encontrado el ámbito para refundir.", 1012);
        }
        if (ambitos.size() > 1) {
        	throw new ExcepcionRefundido("Se han encontrado " + ambitos.size() + " ámbitos para refundir.", 1013);
        }
                        
        contexto.setIdAmbito(Integer.parseInt(ambitos.get(0).toString()));
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
	 * @throws ExcepcionRefundido 
	 */
	private Instrumentotipooperacionplan obtenerInstrumento(
			ContextoRefundido contexto) throws ExcepcionRefundido {
		List<?> tramitesRefundibles = em.createNamedQuery("Tramite.buscarRefundiblesPorAmbito")
				.setParameter("idAmbito", contexto.getIdAmbito())
				.setParameter("listaRefundibles", tipoTramitesRefundibles)
				.getResultList();
		try {
			if (contexto.getTramites().size() == tramitesRefundibles.size()) {
				return servicioDiccionario.getInstrumentoTipoOperacion(
	    				ClsDatos.ID_TIPOOPERACIONPLAN_REFUNDIDO, 
	    				ClsDatos.ID_INSTRUMENTOPLAN_REFUNDIDO);
			} else {
				return servicioDiccionario.getInstrumentoTipoOperacion(
		        				ClsDatos.ID_TIPOOPERACIONPLAN_REFUNDIDO, 
		        				ClsDatos.ID_INSTRUMENTOPLAN_REFUNDIDO_PARCIAL);
			}
		} catch (NoResultException nre) {
			ResourceBundle traductor = (ResourceBundle) contexto.getParametro(ContextoRefundido.TRADUCTOR);
    		throw new ExcepcionRefundido(traductor.getString("refundido.error.noInstrumento"), 1016);
		}
	}

	/**
	 * Ordena los trámites según su fecha y as operaciones definidas entre 
	 * planes. Los trámites ordenados se guardan en la propiedad 
	 * TRAMITES_ORDENADOS del contexto.
	 * 
	 * @param contexto Lista con los identificadores de los trámites 
	 * incluidos en el refundido.
	 * @throws ExcepcionRefundido 
	 */
    @SuppressWarnings("unchecked")
	private void ordenarTramitesParaRefundido(ContextoRefundido contexto) throws ExcepcionRefundido {
        // El orden de los trámites viene dado por el orden del plan.
    	List<Tramite> tramites = em.createNamedQuery("Tramite.ordenar")
			.setParameter("listaTramites", contexto.getTramites())
			.getResultList();
    	
    	// Comprueba si la lista está completa o si hay algún trámite que no existe.
    	if (tramites.size() == contexto.getTramites().size()) {
    		List<Tramite> tramitesOrdenados = new ArrayList<Tramite>();
        	Map<String, Tramite> tramitePorCodigoPlan = new HashMap<String, Tramite>();
        	
        	for (Tramite tramite : tramites) {
        		tramitePorCodigoPlan.put(tramite.getPlan().getCodigo(), tramite);
        	}
        	
        	for (Tramite tramite : tramites) {
        		int indice = tramitesOrdenados.size();
        		if (!tramitesOrdenados.contains(tramite)) {
        			for (Operacionplan operacionPlan : tramite.getPlan().getOperacionplansForIdplanoperador()) {
        				if (operacionPlan.getPlanByIdplanoperado() != null) {
        					Tramite tramiteOperado = tramitePorCodigoPlan.get(operacionPlan.getPlanByIdplanoperado().getCodigo());
            				// Si el plan operado no es un plan raíz se coloca el plan
            				// operador por delante
                			if(tramitesOrdenados.indexOf(tramiteOperado) < indice &&
                					tramitesOrdenados.indexOf(tramiteOperado) > 0
                					&& tramiteOperado.getPlan().getPlanByIdpadre() != null) {
                				indice = tramitesOrdenados.indexOf(tramiteOperado);
                			}
        				}
            		}
        			
        			tramitesOrdenados.add(indice, tramite);
        		}
        	}
    		contexto.putParametro(TRAMITES_ORDENADOS, tramitesOrdenados.toArray(new Tramite[0]));
    		contexto.logTraducido(LOG.INFO,"refundido.tramitesOrdenados");
    	} else {
    		ResourceBundle traductor = (ResourceBundle) contexto.getParametro(ContextoRefundido.TRADUCTOR);
    		throw new ExcepcionRefundido(traductor.getString("refundido.ordenar.tramites.error"), 1014);
    	}
        
    }

    /**
     * 
     * @param contexto
     * @throws ExcepcionRefundido 
     */
	protected void postRefundido(ContextoRefundido contexto) throws ExcepcionRefundido {
		
		limpiador.limpiarRefundido(contexto);
		
		contexto.setFIP(exportador.exportarFIP(contexto));
		
		registrarFIP1(contexto);
	}

	/**
	 * Operaciones previas al proceso de refundido.
	 * 
	 * @param contexto Contexto de refundido.
	 * @throws ExcepcionRefundido 
	 */
	protected void preRefundido(ContextoRefundido contexto) throws ExcepcionRefundido {
		// Si no está definido el idioma establezco uno por defecto
		
		contexto.logTraducido(LOG.INFO,"refundido.inicio", sdf.format(GregorianCalendar.getInstance().getTime()));
		
		obtenerAmbito(contexto);
    	
    	mostrarDatos(contexto);
    	
    	// Averigua los identificadores de los trámites base usados por los
        //  trámites que se van a refundir.
        
        // Lista de trámites Base. Los trámites que se van a refundir pueden
        //  usar diferentes trámites base que, incluso, pueden ser de diferentes
        //  planes.
        contexto.putParametro(ContextoRefundido.ID_TRAMITES_BASE, em.createNamedQuery("Tramite.obtenerIdTramitesBase")
            	.setParameter("idTipoTramite", ClsDatos.ID_TIPOTRAMITE_PLANBASE)
            	.setParameter("listaTramites", contexto.getTramites()).getResultList());
    	
        ordenarTramitesParaRefundido(contexto);
        
        mostrarOperaciones(contexto);
	}

	/**
	 * 
	 * @param contexto
	 * @throws ExcepcionRefundido
	 */
	protected void refundir(ContextoRefundido contexto) throws ExcepcionRefundido {
		
		generarOperaciones(contexto);
		
		aplicarCancelaciones(contexto);
		
		crearTrámiteRefundido(contexto);
		
		ejecutarOperaciones(contexto);
		
		em.flush();
		em.refresh(contexto.getParametro(ContextoRefundido.TRAMITE_REFUNDIDO));
	}
	
	/*
	 * (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.servicios.refundido.RefundidoLocal#refundirTramites(es.mitc.redes.urbanismoenred.servicios.refundido.ContextoRefundido)
	 */
	@Override
    public void refundirTramites(ContextoRefundido contexto) throws ExcepcionRefundido {
    	try {
        	gestorContextos.iniciarProceso(contexto);
        	
        	preRefundido(contexto);
        	
        	refundir(contexto);
        	
        	postRefundido(contexto);
            
    	} catch (ExcepcionRefundido er) {
    		ResourceBundle traductor = (ResourceBundle) contexto.getParametro(ContextoRefundido.TRADUCTOR);
        	contexto.log(ContextoRefundido.LOG.ERROR,String.format(traductor.getString("refundido.error.mensaje"), er.getMessage(), er.getCodigo()));
        	throw er;
        } catch (Throwable e) {
        	contexto.log(ContextoRefundido.LOG.ERROR,e.getMessage());
        	throw new ExcepcionRefundido("Error inesperado. " + e.getMessage(), 1015, e);
        } finally {
        	gestorContextos.finalizarProceso(contexto);
        }
    }
	
	/**
	 * 
	 * @param contexto
	 */
	private void registrarFIP1(ContextoRefundido contexto) {
		Tramite tramite = (Tramite) contexto.getParametro(ContextoRefundido.ESQUELETO_TRAMITE_REFUNDIDO);
		Fip1 fip1 = new Fip1();
		fip1.setCodfip(tramite.getCodigofip());
		fip1.setFechacreacion(tramite.getFecha());
		fip1.setFecharefundido(tramite.getFecha());
		fip1.setIdambito(tramite.getPlan().getIdambito());
		fip1.setObsoleto(false);
		
		em.persist(fip1);
	}

}

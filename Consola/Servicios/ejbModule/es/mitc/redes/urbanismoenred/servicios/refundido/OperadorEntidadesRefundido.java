/*
* Copyright 2011 red.es
* Autores: Arnaiz Consultores.
*
** Licencia con arreglo a la EUPL, Versión 1.1 o -en cuanto
** sean aprobadas por la Comision Europea- versiones
** posteriores de la EUPL (la <<Licencia>>);
** Solo podra usarse esta obra si se respeta la Licencia.
* Puede obtenerse una copia de la Licencia en:
*
* http://ec.europa.eu/idabc/eupl5
*
** Salvo cuando lo exija la legislacion aplicable o se acuerde
* por escrito, el programa distribuido con arreglo a la
* Licencia se distribuye <<TAL CUAL>>,
** SIN GARANTIAS NI CONDICIONES DE NINGUN TIPO, ni expresas
** ni implicitas.
** Vease la Licencia en el idioma concreto que rige
* los permisos y limitaciones que establece la Licencia.
*/
package es.mitc.redes.urbanismoenred.servicios.refundido;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;

import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Plan;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Casoentidaddeterminacion;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Determinacion;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Entidad;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Entidaddeterminacion;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Entidaddeterminacionregimen;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Opciondeterminacion;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Tramite;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Traza;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.TrazaId;

/**
 * Session Bean implementation class OperadorEntidadesRefundido
 * 
 * @author Arnaiz Consultores
 */
@Stateless
public class OperadorEntidadesRefundido implements OperadorEntidadesRefundidoLocal {
	
	private static final String APORTADAS = "refundido.entidades.aportadas";
	private static final String CARPETA_ENTIDADES_APORTADAS = "refundido.entidades.aportadas.carpeta";
	
	@PersistenceContext(unitName = "rpmv2")
	private EntityManager em;
	
	@EJB
	private OperadorDeterminacionesRefundidoLocal operadorDeterminaciones;
    /**
     * Default constructor. 
     */
    public OperadorEntidadesRefundido() {
    }
    
    /*
     * (non-Javadoc)
     * @see es.mitc.redes.urbanismoenred.servicios.dal.OperadorEntidadesRefundidoLocal#existeCarpetaAportadas(java.lang.String, es.mitc.redes.urbanismoenred.servicios.refundido.ContextoRefundido)
     */
    @SuppressWarnings("unchecked")
	@Override
	public boolean existeCarpetaAportadas(String codigoPlan,
			ContextoRefundido contexto) {
    	if (contexto.getParametro(APORTADAS) != null) {
    		return ((Map<String, Integer>)contexto.getParametro(APORTADAS)).containsKey(codigoPlan);
    	} else {
    		return false;
    	}
	}

    /*
     * (non-Javadoc)
     * @see es.mitc.redes.urbanismoenred.servicios.dal.OperadorEntidadesRefundidoLocal#getAportadas(es.mitc.redes.urbanismoenred.servicios.refundido.ContextoRefundido)
     */
    @SuppressWarnings("unchecked")
	@Override
	public List<Entidad> getAportadas(ContextoRefundido contexto) {
    	List<Entidad> resultado = new ArrayList<Entidad>();
		
		Map<String, Integer> aportadas = (Map<String, Integer>) contexto.getParametro(APORTADAS);
		
		if (aportadas != null) {
			Entidad determinacion;
			for (Integer id : aportadas.values()) {
				determinacion = em.find(Entidad.class, id);
				
				if (determinacion != null) {
					resultado.add(determinacion);
				}
			}
		}
		return resultado;
	}

	/**
	 * Devuelve el orden máximo de las entidades hijas de la entidad padre
	 * especificada.
	 * 
	 * @param padre Entidad padre.
	 * @return Es el orden más alto de las entidades raíz más uno.
	 */
	private int getOrden(Entidad padre) {
		try {
    		Integer orden = (Integer) em.createNamedQuery("refundido.Entidad.obtenerOrdenMaximoPadre")
    				.setParameter("idTramite", padre.getTramite().getIden())
    				.setParameter("idPadre", padre.getIden())
    				.getSingleResult();
    		if (orden != null) {
    			return ++orden;
    		} else {
    			return 1;
    		}
	    } catch (NoResultException nre) {
	    	return 1;
	    }
	}
	
    /**
     * Devuelve el orden máximo de las entidades raíz del trámite.
     * 
     * @param tramite Trámite al que pertenecen las entidades.
     * @return Órden máximo. Es el orden más alto de las entidades raíz más uno.
     */
    private int getOrden(Tramite tramite) {
    	try {
    		Integer orden = (Integer) em.createNamedQuery("refundido.Entidad.obtenerOrdenMaximo")
    				.setParameter("idTramite", tramite.getIden())
    				.getSingleResult();
    		if (orden != null) {
    			return ++orden;
    		} else {
    			return 1;
    		}
	    } catch (NoResultException nre) {
	    	return 1;
	    }
	}

	/*
     * (non-Javadoc)
     * @see es.mitc.redes.urbanismoenred.servicios.dal.OperadorEntidadesRefundidoLocal#getSiguienteCodigo(es.mitc.redes.urbanismoenred.servicios.refundido.ContextoRefundido)
     */
	@Override
	public String getSiguienteCodigo(ContextoRefundido contexto) {
		Tramite tramite = (Tramite) contexto.getParametro(ContextoRefundido.TRAMITE_REFUNDIDO);
		return getSiguienteCodigo(tramite.getIden());
	}

	/**
	 * Devuelve el siguiente código disponible para una entidad dentro del trámite.
	 * 
	 * @param idTramite Identificador del trámite.
	 * @return Código disponible.
	 */
	private String getSiguienteCodigo(int idTramite) {
		em.flush();
		String codigoEntidad = (String)em.createNamedQuery("refundido.Entidad.obtenerMaximoCodigo")
        		.setParameter("idTramite", idTramite)
        		.getSingleResult();
		
		return String.format("%010d", codigoEntidad != null ? Integer.parseInt(codigoEntidad) +1:1);
	}
	
	/*
	 * (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.servicios.refundido.OperadorEntidadesRefundidoLocal#obtenerAmbitoAplicacion(es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Plan, int)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public Entidad obtenerAmbitoAplicacion(Plan plan, int idproceso) {
		
		List<es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Tramite> vigentes = em.createNamedQuery("Tramite.findVigente")
				.setParameter("idPlan", plan.getIden())
				.getResultList();

		if (!vigentes.isEmpty()) {
			List<es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidad> ambitosAplicacion = em.createNamedQuery("Entidad.obtenerAmbitoAplicacion")
					.setParameter("idTramite", vigentes.get(0).getIden())
					.setParameter("codigoAmbitoAplicacion", ClsDatos.CODIGO_DETERMINACIONBASE_GRUPO_AMBITOAPLICACION)
					.getResultList();

			if (!ambitosAplicacion.isEmpty()) {
				Traza traza = em.find(Traza.class, new TrazaId(idproceso, "Entidad", ambitosAplicacion.get(0).getIden()));
				
				if (traza != null) {
					return em.find(Entidad.class, traza.getIdrefundido());
				}
			} else {
				ambitosAplicacion = em.createNamedQuery("Entidad.obtenerAmbitoAplicacionDerivado")
						.setParameter("idTramite", vigentes.get(0).getIden())
						.setParameter("codigoAmbitoAplicacion", ClsDatos.CODIGO_DETERMINACIONBASE_GRUPO_AMBITOAPLICACION)
						.getResultList();
				
				if (!ambitosAplicacion.isEmpty()) {
					Traza traza = em.find(Traza.class, new TrazaId(idproceso, "Entidad", ambitosAplicacion.get(0).getIden()));
					
					if (traza != null) {
						return em.find(Entidad.class, traza.getIdrefundido());
					}
				}
			}
		}
		
        return null;
	}

	/*
	 * (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.servicios.dal.OperadorEntidadesRefundidoLocal#obtenerCarpetaEntidadesAportadas(es.mitc.redes.urbanismoenred.servicios.refundido.ContextoRefundido)
	 */
    @SuppressWarnings("unchecked")
    @Override
	public Entidad obtenerCarpetaEntidadesAportadas(ContextoRefundido contexto) throws ExcepcionRefundido {
    	
    	Tramite tramite = (Tramite)contexto.getParametro(ContextoRefundido.TRAMITE_REFUNDIDO);
    	ResourceBundle traductor = (ResourceBundle) contexto.getParametro(ContextoRefundido.TRADUCTOR);
    	
    	Integer idAportadas = (Integer) contexto.getParametro(CARPETA_ENTIDADES_APORTADAS);
    	
    	if (idAportadas != null) { 
    		return em.find(Entidad.class, idAportadas);
    	}  else {
            // Añadir su valor para la determinación 'Grupo de entidades' (es 
            //  la determinación 'Carpeta', lo que anteriormente era el grupo 'Grupo')
            Determinacion grupoDeEntidades=null;
            Determinacion carpeta=null;
            
            List<Tramite> listaTramites = (List<Tramite>)em.createNamedQuery("refundido.Tramite.obtenerTramitesBase")
            		.setParameter("idProceso", contexto.getParametro(ContextoRefundido.ID_PROCESO))
            		.getResultList();
            
            for (Tramite tramiteBase : listaTramites){
                grupoDeEntidades=operadorDeterminaciones.obtenerDeterminacionGrupoDeEntidades(tramiteBase);
                if (grupoDeEntidades!=null){
                    carpeta=operadorDeterminaciones.obtenerDeterminacionCarpeta(contexto);
                    if (carpeta != null) {
                    	try {
                        	Opciondeterminacion iOpcion=(Opciondeterminacion) em.createNamedQuery("refundido.Opciondeterminacion.buscarPorDetYValorRef")
                    			.setParameter("idDeterminacion", grupoDeEntidades.getIden())
                    			.setParameter("idValorRef", carpeta.getIden()).getSingleResult();
                        	
                        	Entidad aportadas = new Entidad();
                            aportadas.setTramite(tramite);
                            aportadas.setClave("");
                            aportadas.setNombre(traductor.getString("refundido.aportadasPor"));
                            aportadas.setCodigo(getSiguienteCodigo(tramite.getIden()));
                            aportadas.setBsuspendida(false);
                            
                            aportadas.setOrden(getOrden(tramite));
                            em.persist(aportadas);
                            em.flush();

                        	// Inserta el valor en las tablas de valores
                        	Entidaddeterminacion ed = new Entidaddeterminacion();
                        	ed.setEntidad(aportadas);
                        	ed.setDeterminacion(grupoDeEntidades);
                        	em.persist(ed);
                        	
                        	Casoentidaddeterminacion ced = new Casoentidaddeterminacion();
                        	ced.setEntidaddeterminacion(ed);
                        	em.persist(ced);
                        	
                        	Entidaddeterminacionregimen edr = new Entidaddeterminacionregimen();
                        	edr.setCasoentidaddeterminacionByIdcaso(ced);
                        	edr.setOpciondeterminacion(iOpcion);
                        	edr.setSuperposicion(0);
                        	em.persist(edr);
                        	
                        	contexto.putParametro(CARPETA_ENTIDADES_APORTADAS, aportadas.getIden());
                        	return aportadas;
                        } catch (NoResultException nre) {
                        	throw new ExcepcionRefundido(traductor.getString("refundido.crear.carpeta.entidadesAportadas.error")+
                        			String.format(traductor.getString("refundido.crear.carpeta.entidadesAportadas.error.noOpcion"),  
                        			grupoDeEntidades.getCodigo(), 
                        			carpeta.getCodigo()), 9001);
                        }
                    } else {
                    	StringBuffer sb = new StringBuffer();
                    	sb.append(traductor.getString("refundido.crear.carpeta.entidadesAportadas.error"));
                    	sb.append(" ");
                    	sb.append(traductor.getString("refundido.crear.carpeta.entidadesAportadas.error.noDeterminacion"));
                    	
                    	throw new ExcepcionRefundido(sb.toString(), 9002);
                    }
                }
            }
            StringBuffer sb = new StringBuffer();
        	sb.append(traductor.getString("refundido.crear.carpeta.entidadesAportadas.error"));
        	if (grupoDeEntidades == null) {
        		sb.append(" ");
        		sb.append(traductor.getString("refundido.crear.carpeta.entidadesAportadas.error.noGrupo"));
        	}
        	
        	throw new ExcepcionRefundido(sb.toString(), 9003);          	
    	} 
    }

	/*
	 * (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.servicios.refundido.OperadorEntidadesRefundidoLocal#obtenerCarpetaEntidadesAportadas(es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Plan, es.mitc.redes.urbanismoenred.servicios.refundido.ContextoRefundido, boolean)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Entidad obtenerCarpetaEntidadesAportadas(Plan plan,
			ContextoRefundido contexto, boolean crear) throws ExcepcionRefundido {
        Entidad entidad = null;
        
        Map<String, Integer> aportadas = (Map<String, Integer>) contexto.getParametro(APORTADAS);
		if (aportadas == null) {
			aportadas = new HashMap<String, Integer>();
			contexto.putParametro(APORTADAS, aportadas);
		}
		
        if (!aportadas.containsKey(plan.getCodigo()) ) {
        	if (crear) {
        		Tramite tramite = (Tramite)contexto.getParametro(ContextoRefundido.TRAMITE_REFUNDIDO);
        		entidad = new Entidad();
                entidad.setTramite(tramite);
                entidad.setClave(tramite.getPlan().getCodigo());
                
                ResourceBundle traductor = (ResourceBundle) contexto.getParametro(ContextoRefundido.TRADUCTOR);
        		
                String txtEntidad = traductor.getString("refundido.entidad.aportadaspor") + " ["+ plan.getCodigo() +"] " + plan.getNombre();
                
                // Se limita la longitud del nombre de carpeta a 100 caracteres
                if (txtEntidad.length() > 100) {
                    txtEntidad = txtEntidad.substring(0, 100);
                }
                
                entidad.setNombre(txtEntidad);

                entidad.setEntidadByIdpadre(obtenerCarpetaEntidadesAportadas(contexto));
                entidad.setCodigo(getSiguienteCodigo(tramite.getIden()));
                entidad.setOrden(getOrden(entidad.getEntidadByIdpadre()));
                entidad.setBsuspendida(false);
                em.persist(entidad);
                
                em.flush();
                
                aportadas.put(plan.getCodigo(), entidad.getIden());
                
                // Añadir su valor para la determinación 'Grupo de entidades' (es la determinación 'Carpeta', lo que anteriormente era el grupo 'Grupo')
                if (tramite.getPlan().getPlanByIdplanbase() != null) {
                	Tramite iTramiteBase=tramite.getPlan()
                    		.getPlanByIdplanbase()
                    		.getTramites()
                    		.toArray(new Tramite[0])[0];
                    Determinacion grupoDeEntidades= operadorDeterminaciones.obtenerDeterminacionGrupoDeEntidades(iTramiteBase);
                    Determinacion carpeta=operadorDeterminaciones.obtenerDeterminacionCarpeta(contexto);
                    if (grupoDeEntidades !=null && carpeta!=null){
                    	try {
                    		Opciondeterminacion iOpcion=(Opciondeterminacion) em.createNamedQuery("refundido.Opciondeterminacion.buscarPorDetYValorRef")
    	                			.setParameter("idDeterminacion", grupoDeEntidades.getIden())
    	                			.setParameter("idValorRef", carpeta.getIden()).getSingleResult();
    	
    	                    // Inserta el valor en las tablas de valores
    	                    Entidaddeterminacion ed = new Entidaddeterminacion();
    	                    ed.setDeterminacion(grupoDeEntidades);
    	                    ed.setEntidad(entidad);
    	                    em.persist(ed);
    	                    
    	                    Casoentidaddeterminacion ced = new Casoentidaddeterminacion();
    	                    ced.setEntidaddeterminacion(ed);
    	                    
    	                    em.persist(ced);
    	                    
    	                    Entidaddeterminacionregimen edr = new Entidaddeterminacionregimen();
    	                    edr.setCasoentidaddeterminacionByIdcaso(ced);
    	                    edr.setOpciondeterminacion(iOpcion);
    	                    edr.setSuperposicion(0);
    	                    em.persist(edr);
                    	} catch (NoResultException nre) {
                    		throw new ExcepcionRefundido(String.format(traductor.getString("refundido.excepcion.entidad.crearCarpetaAportadas.noOpcion"),
                    				plan.getCodigo()), 9004);
                    	} catch (NonUniqueResultException nure) {
                    		throw new ExcepcionRefundido(String.format(traductor.getString("refundido.excepcion.entidad.crearCarpetaAportadas.multipleOpcion"),
                    				plan.getCodigo()), 9005);
                    	}
                    } else {
                    	throw new ExcepcionRefundido(String.format(traductor.getString("refundido.excepcion.entidad.crearCarpetaAportadas.noGrupoOCarpeta"),
                				plan.getCodigo()), 9006);
                    }
                } else {
                	throw new ExcepcionRefundido(String.format(traductor.getString("refundido.excepcion.entidad.crearCarpetaAportadas.noPlanBase")
                			, plan.getCodigo()), 9007);
                }
                
                // Es necesario confirmar los cambios para que la asignación de código
                // tenga efecto y no se duplique en futuras llamadas.
                em.flush();
        	}
        } else {
            entidad = em.find(Entidad.class, aportadas.get(plan.getCodigo()));
        }
        
        return entidad;
	}
}

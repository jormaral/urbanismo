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

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;

import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Plan;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Determinacion;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Opciondeterminacion;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Tramite;

/**
 * Session Bean implementation class OperadorDeterminacionesRefundido
 * 
 * @author Arnaiz Consultores
 */
@Stateless
public class OperadorDeterminacionesRefundido implements OperadorDeterminacionesRefundidoLocal {
	
	private static final String APORTADAS = "refundido.determinaciones.aportadas";
	private static final String CARPETA_APORTADAS = "refundido.determinaciones.aportadas.carpeta";
	
	private static final Logger log = Logger.getLogger(OperadorDeterminacionesRefundido.class);
	
	@PersistenceContext(unitName = "rpmv2")
	private EntityManager em;

    /**
     * Default constructor. 
     */
    public OperadorDeterminacionesRefundido() {
    }

    /*
     * (non-Javadoc)
     * @see es.mitc.redes.urbanismoenred.servicios.dal.OperadorDeterminacionesRefundidoLocal#existeCarpetaAportadas(java.lang.String, es.mitc.redes.urbanismoenred.servicios.refundido.ContextoRefundido)
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
	
	/**
	 * 
	 * @param idTramite
	 * @return
	 */
	private String getApartado(Determinacion padre) {
        int numApartado = 0;
        for (Determinacion hija : padre.getDeterminacionsForIdpadre()) {
        	try {
        		if (Integer.parseInt(hija.getApartado()) > numApartado) {
        			numApartado = Integer.parseInt(hija.getApartado());
        		}
        	} catch (NumberFormatException nfe) {
        		// No es un número, no aplica
        	}
        }
        numApartado++;
        
        return String.valueOf(numApartado);
	}
	
	/*
	 * (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.servicios.dal.OperadorDeterminacionesRefundidoLocal#getAportadas(es.mitc.redes.urbanismoenred.servicios.refundido.ContextoRefundido)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Determinacion> getAportadas(ContextoRefundido contexto) {
		List<Determinacion> resultado = new ArrayList<Determinacion>();
		
		Map<String, Integer> aportadas = (Map<String, Integer>) contexto.getParametro(APORTADAS);
		
		if (aportadas != null) {
			Determinacion determinacion;
			for (Integer id : aportadas.values()) {
				determinacion = em.find(Determinacion.class, id);
				
				if (determinacion != null) {
					resultado.add(determinacion);
				}
			}
		}
		return resultado;
	}
	
	/**
	 * 
	 * @param idTramite
	 * @return
	 */
	private String getCodigo(int idTramite) {
		em.flush();
		String codigoDet = (String)em.createNamedQuery("refundido.Determinacion.obtenerMaximoCodigo")
        		.setParameter("idTramite", idTramite)
        		.getSingleResult();
		return String.format("%010d", codigoDet != null? Integer.parseInt(codigoDet)+1:1);
	}

	/*
	 * (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.servicios.dal.OperadorDeterminacionesRefundidoLocal#getSiguienteCodigo(es.mitc.redes.urbanismoenred.servicios.refundido.ContextoRefundido)
	 */
	@Override
	public String getSiguienteCodigo(ContextoRefundido contexto) {
		Tramite tramite = (Tramite) contexto.getParametro(ContextoRefundido.TRAMITE_REFUNDIDO);
		return getCodigo(tramite.getIden());
	}

	/*
	 * (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.servicios.refundido.OperadorDeterminacionesRefundidoLocal#obtenerAfeccion(es.mitc.redes.urbanismoenred.servicios.refundido.ContextoRefundido)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Determinacion obtenerAfeccion(ContextoRefundido contexto) {
		List<Determinacion> afecciones = em.createNamedQuery("refundido.Determinacion.buscarPorCodigoYProceso")
			.setParameter("codigo", ClsDatos.CODIGO_DETERMINACIONBASE_GRUPO_AFECCION)
			.setParameter("idProceso", contexto.getParametro(ContextoRefundido.ID_PROCESO))
			.getResultList();
		
		if (!afecciones.isEmpty()) {
			return afecciones.get(0);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.servicios.dal.OperadorDeterminacionesRefundidoLocal#obtenerCarpetaDeterminacionesAportadas(es.mitc.redes.urbanismoenred.servicios.refundido.ContextoRefundido)
	 */
	@Override
	public Determinacion obtenerCarpetaDeterminacionesAportadas(ContextoRefundido contexto) {
		Determinacion dAportadas;
		if (contexto.getParametro(CARPETA_APORTADAS) == null) {
			ResourceBundle traductor = (ResourceBundle) contexto.getParametro(ContextoRefundido.TRADUCTOR);
			Tramite tramite = (Tramite) contexto.getParametro(ContextoRefundido.TRAMITE_REFUNDIDO);
			// Crea la determinación "Aportadas", de la que van a colgar todas las
	        //  determinaciones "Aportadas por..." que se han creado durante el
	        //  proceso de refundido.
	        Integer orden = (Integer)em.createNamedQuery("refundido.Determinacion.obtenerOrdenMaximo")
	        		.setParameter("idTramite", tramite.getIden())
	        		.getSingleResult();
	        
	        if (orden == null) {
	        	orden = 1;
	        }
	        dAportadas = new Determinacion();
	        dAportadas.setTramite(tramite);
	        dAportadas.setIdcaracter(ClsDatos.ID_CARACTER_ENUNCIADO);
	        dAportadas.setApartado(traductor.getString("refundido.anexo"));
	        dAportadas.setNombre(traductor.getString("refundido.aportadasPor"));
	        dAportadas.setCodigo(getCodigo(tramite.getIden()));
	        dAportadas.setBsuspendida(false);
	        dAportadas.setOrden(++orden);
	        em.persist(dAportadas);
	        em.flush();
	        
	        contexto.putParametro(CARPETA_APORTADAS, dAportadas.getIden());
		} else {
			dAportadas = em.find(Determinacion.class, contexto.getParametro(CARPETA_APORTADAS));
			em.refresh(dAportadas);
		}
		
		return dAportadas;
	}

	/*
	 * (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.servicios.refundido.OperadorDeterminacionesRefundidoLocal#obtenerCarpetaDeterminacionesAportadas(es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Plan, es.mitc.redes.urbanismoenred.servicios.refundido.ContextoRefundido, boolean)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Determinacion obtenerCarpetaDeterminacionesAportadas(
			Plan plan, ContextoRefundido contexto, boolean crear) {

		Map<String, Integer> aportadas = (Map<String, Integer>) contexto.getParametro(APORTADAS);
		if (aportadas == null) {
			aportadas = new HashMap<String, Integer>();
			contexto.putParametro(APORTADAS, aportadas);
		}
        // Si la determinación ya existe por haber sido creada con anterioridad
        // entonces debe estar en el contexto
        Determinacion determinacion = null;
		if (!aportadas.containsKey(plan.getCodigo())) {
			if (crear) {
				
				Determinacion padre = obtenerCarpetaDeterminacionesAportadas(contexto);
				
				Tramite tramite = (Tramite)contexto.getParametro(ContextoRefundido.TRAMITE_REFUNDIDO);
				determinacion = new Determinacion();
	            
	            determinacion.setBsuspendida(false);
	            determinacion.setCodigo(getCodigo(tramite.getIden()));
	            determinacion.setIdcaracter(ClsDatos.ID_CARACTER_ENUNCIADO);
	            ResourceBundle traductor = (ResourceBundle) contexto.getParametro(ContextoRefundido.TRADUCTOR);
	    		
	    		String txtDeterminacion = traductor.getString("refundido.determinacion.aportadaspor") + " ["+plan.getCodigo()+"] " + plan.getNombre();
	            if (txtDeterminacion.length() > 220) {
	                txtDeterminacion = txtDeterminacion.substring(0, 220);
	            }
	            determinacion.setNombre(txtDeterminacion);
	            determinacion.setTramite(tramite);
	            determinacion.setDeterminacionByIdpadre(padre);
	            determinacion.setApartado(getApartado(padre));
	            
	            Integer orden = (Integer)em.createNamedQuery("refundido.Determinacion.obtenerOrdenMaximoPadre")
	            		.setParameter("idTramite", tramite.getIden())
	            		.setParameter("idPadre", padre.getIden())
	            		.getSingleResult();
	        
	            if (orden != null) {
	            	determinacion.setOrden(++orden);
	            } else {
	            	determinacion.setOrden(1);
	            }
	            
	            
	            em.persist(determinacion);
	            em.flush();
	            
	            aportadas.put(plan.getCodigo(), determinacion.getIden());
	            log.debug("Creada carpeta determinaciones aportadas: " 
	            		+ determinacion.getApartado() + " " 
	            		+ determinacion.getNombre() + 
	            		" código: " + determinacion.getCodigo()
	            		+ " sobre el trámite " + tramite.getIden()
	            		+  " con código " + tramite.getCodigofip());
			}
            
        } else {
            determinacion = em.find(Determinacion.class, aportadas.get(plan.getCodigo()));
        }
		
		return determinacion;
	}

	/*
	 * (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.servicios.dal.OperadorDeterminacionesRefundidoLocal#obtenerDeterminacionCarpetaTramiteBase(es.mitc.redes.urbanismoenred.data.rpm.refundido.Tramite, java.util.List)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Determinacion obtenerDeterminacionCarpeta(ContextoRefundido contexto) {
		
		Tramite tramite = (Tramite) contexto.getParametro(ContextoRefundido.TRAMITE_REFUNDIDO);
    	
        // Primero se busca en los trámites base.
        List<Determinacion> lista = em.createNamedQuery("refundido.Determinacion.buscarPorCodigoYProceso")
        		.setParameter("codigo", ClsDatos.CODIGO_DETERMINACIONBASE_GRUPO_CARPETA)
        		.setParameter("idProceso", contexto.getParametro(ContextoRefundido.ID_PROCESO))
        		.getResultList();
        if (lista.size()>0) {
            return lista.get(0);
        }    
        
        // Si no existe la determinación 'Carpeta' en un trámite base,
        // se busca en el trámite solicitado. Para ello, se busca entre
        // los valores de referencia de cualquiera de las determinaciones
        // con caracter ClsDatos.ID_CARACTER_GRUPODEENTIDADES que están
        // aplicadas a alguna entidad del trámite en curso. 
        // Primero se intenta buscar una determinación 'Grupo de Entidades'
        // dentro del propio trámite y, si no se encuentra, se busca en
        // cualquier otro trámite, aunque siempre sobre entidades del
        // trámite en curso.
        // Para localizar la determinación 'Carpeta' dentro de todos los
        // valores de referencia encontrados, se miran dos cosas: puede que
        // tenga el mismo código que la determinación carpeta del plan
        // base, o puede que está vinculada a la determinación del plan base
        // que tenga dicho código. Cualquiera de las dos condiciones sirve
        // para identificar la determinación 'Carpeta'.
        
        // Primero, dentro de las determinaciones del trámite en curso.
        
        lista = em.createNamedQuery("refundido.Determinacion.obtenerCarpeta")
        		.setParameter("idTramite", tramite.getIden())
        		.setParameter("idCaracter", ClsDatos.ID_CARACTER_GRUPODEENTIDADES)
        		.setParameter("codigoVR", ClsDatos.CODIGO_DETERMINACIONBASE_GRUPO_CARPETA)
        		.setParameter("codigo", ClsDatos.CODIGO_DETERMINACIONBASE_GRUPO_CARPETA)
        		.getResultList();
        
        if (lista.size()>0) {
            return lista.get(0);
        }
        
        // En segundo lugar, dentro de las determinaciones de cualquier otro
        //  trámite que están vinculadas a las entidades del trámite en curso.
        lista = em.createNamedQuery("refundido.Determinacion.obtenerCarpetaCualquierTramite")
        		.setParameter("idCaracter", ClsDatos.ID_CARACTER_GRUPODEENTIDADES)
        		.setParameter("codigoVR", ClsDatos.CODIGO_DETERMINACIONBASE_GRUPO_CARPETA)
        		.setParameter("codigo", ClsDatos.CODIGO_DETERMINACIONBASE_GRUPO_CARPETA)
        		.setParameter("idProceso", contexto.getParametro(ContextoRefundido.ID_PROCESO))
        		.getResultList();
        
        if (lista.size()>0) {
            return lista.get(0);
        }
        
        // Si en este punto no se ha logrado identificar ninguna determinación
        // 'Carpeta', hay que añadir una en el trámite en curso, que se 
        // añadirá como valor de referencia de la determinación 'Grupo de Entidades'
        Determinacion grupoDeEntidades = obtenerDeterminacionGrupoDeEntidades(tramite);

        // Si no existe ninguna determinación 'Grupo de Entidades' o bien 
        //  es de un plan base, no se le pueden añadir valores de referencia. 
        //  Por lo tanto, también hay que crear una determinacion 'Grupo de Entidades' 
        //  en el trámite en curso.

        if (grupoDeEntidades==null || grupoDeEntidades.getTramite().getPlan().getPlanByIdplanbase() == null){
        	log.warn("No existe ninguna determinación 'Grupo de Entidades' disponible para el idTramite=" + tramite.getIden());
            log.warn("Se va a crear una en dicho trámite.");
            
            grupoDeEntidades = new Determinacion();
            
            Determinacion padre = obtenerCarpetaDeterminacionesAportadas(contexto);
            
            grupoDeEntidades.setBsuspendida(false);
            
            grupoDeEntidades.setCodigo(getCodigo(tramite.getIden()));
            grupoDeEntidades.setDeterminacionByIdpadre(padre);
            grupoDeEntidades.setIdcaracter(ClsDatos.ID_CARACTER_GRUPODEENTIDADES);
            grupoDeEntidades.setTramite(tramite);
            grupoDeEntidades.setNombre("Grupo de entidades");
            int orden = 0;
            try {
            	orden = (Integer)em.createNamedQuery("refundido.Determinacion.obtenerOrdenMaximoPadre")
                		.setParameter("idTramite", tramite.getIden())
                		.setParameter("idPadre", padre.getIden())
                		.getSingleResult();
            } catch (NoResultException nre) {
            }
     
            grupoDeEntidades.setOrden(++orden);
            
            grupoDeEntidades.setApartado(getApartado(padre));
            
            em.persist(grupoDeEntidades);
            em.flush();
        }
        
        // Crear la determinación 'Carpeta'
        int orden = (Integer)em.createNamedQuery("refundido.Determinacion.obtenerOrdenMaximoPadre")
        		.setParameter("idTramite", tramite.getIden())
        		.setParameter("idPadre", grupoDeEntidades.getIden())
        		.getSingleResult();
        
        Determinacion carpeta = new Determinacion();
        carpeta.setTramite(tramite);
        carpeta.setDeterminacionByIdpadre(grupoDeEntidades);
        carpeta.setIdcaracter(ClsDatos.ID_CARACTER_VALORREFERENCIA);
        carpeta.setApartado(getApartado(grupoDeEntidades));
        carpeta.setNombre("Grupo de entidades");
        carpeta.setCodigo(getCodigo(tramite.getIden()));
        carpeta.setBsuspendida(false);
        carpeta.setOrden(++orden);
        
        em.persist(carpeta);
        
        // Crear la opción correspondiente
        Opciondeterminacion od = new Opciondeterminacion();
        od.setDeterminacionByIddeterminacion(grupoDeEntidades);
        od.setDeterminacionByIddeterminacionvalorref(carpeta);
        em.persist(od);

        return carpeta;
    }

	/*
	 * (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.servicios.dal.OperadorDeterminacionesRefundidoLocal#obtenerDeterminacionGrupoDeEntidades(es.mitc.redes.urbanismoenred.data.rpm.refundido.Tramite)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Determinacion obtenerDeterminacionGrupoDeEntidades(
			Tramite tramite) {
		log.debug("Obteniendo determinacion grupo de entidades por tramite...");
        List<Determinacion> determinaciones = em.createNamedQuery("refundido.Determinacion.obtenerPorTramiteYCaracter")
        		.setParameter("idTramite", tramite.getIden())
        		.setParameter("idCaracter", ClsDatos.ID_CARACTER_GRUPODEENTIDADES)
        		.getResultList();
        if (determinaciones.size() > 0) {
        	log.debug("Determinación obtenida: " + determinaciones.get(0).getIden());
            // Se toma la primera, aunque puede haber más, debido a que se van
            //  reasignando las determinaciones desde un trámite hasta otro.
            return determinaciones.get(0);
        } else {
        	log.debug("Obteniendo cualquier determinacion grupo de entidades por trámite");
        	// Si no se ha encontrado en el trámite en cuestión, es porque el trámite
            //  está usando la determinación 'Grupo de Entidades' de otro 
            //  trámite, que puede ser del plan base.
            // Por lo tanto, se toma cualquier determinación de caracter 'GRUPODEENTIDADES'
            //  que está aplicada en alguna de las entidades del trámite.
        	determinaciones = em.createNamedQuery("refundido.Determinacion.obtenerCualquieraPorTramiteYCaracter")
        			.setParameter("idTramite", tramite.getIden())
            		.setParameter("idCaracter", ClsDatos.ID_CARACTER_GRUPODEENTIDADES)
            		.getResultList();
            
            if (determinaciones.size() > 0) {
            	log.debug("Determinación obtenida: " + determinaciones.get(0).getIden());
                // Se toma la primera, aunque puede haber más
                return determinaciones.get(0);
            }
        }
        
        log.warn("No se ha encontrado determinación grupo de entidades");
		return null;
	}

}

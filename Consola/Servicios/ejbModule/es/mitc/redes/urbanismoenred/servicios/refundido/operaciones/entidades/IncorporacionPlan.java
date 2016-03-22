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
package es.mitc.redes.urbanismoenred.servicios.refundido.operaciones.entidades;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.TopologyException;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;

import es.mitc.redes.urbanismoenred.data.rpm.refundido.Casoentidaddeterminacion;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Determinacion;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Documentoentidad;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Entidad;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Entidaddeterminacion;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Entidaddeterminacionregimen;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Entidadlin;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Entidadpnt;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Entidadpol;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Opciondeterminacion;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Tramite;
import es.mitc.redes.urbanismoenred.servicios.refundido.ClsDatos;
import es.mitc.redes.urbanismoenred.servicios.refundido.ContextoRefundido;
import es.mitc.redes.urbanismoenred.servicios.refundido.ContextoRefundido.LOG;
import es.mitc.redes.urbanismoenred.servicios.refundido.CopiadorRefundidoLocal;
import es.mitc.redes.urbanismoenred.servicios.refundido.ExcepcionRefundido;
import es.mitc.redes.urbanismoenred.servicios.refundido.OperadorDeterminacionesRefundidoLocal;
import es.mitc.redes.urbanismoenred.servicios.refundido.OperadorEntidadesRefundidoLocal;

/**
 * En el refundido aparecerán todas las entidades del plan seleccionado salvo 
 * su ámbito de aplicación y la entidad cuyo grupo sea el mismo que el de la 
 * entidad incorporadora.
 * 
 * Dentro de la entidad operadora deben incorporarse todas las entidades del 
 * plan incorporado excepto su ámbito de aplicación y la entidad del mismo 
 * grupo que la que tiene la incorporadora (si la entidad incorporadora es un 
 * sector, no se incorporará el sector del plan incorporado). Las entidades 
 * incorporadas recortarán a las existentes de su mismo grupo del plan que 
 * contiene la entidad incorporadora.
 * 
 * @author Arnaiz Consultores
 *
 */
@Stateless( name = "refundido.operacion.entidades.incorporacionPlan")
public class IncorporacionPlan implements EjecutorLocal {

	private static final String CODIGO_GRUPO_ENTIDADES = "7777777777";
	private static final String CODIGO_GRUPO = "7000000001";
	private static final String DETERMINACIONES_A_ELIMINAR = "refundido.operaciones.plan.eliminar.determinaciones";
	private static final String DOCUMENTOS_A_ELIMINAR = "refundido.operaciones.plan.eliminar.documentos";
	private static final String ENTIDADES_A_ELIMINAR = "refundido.operaciones.plan.eliminar.entidades";
	
	private static final String SOLO_AVISOS = "Avisos";
	private static final String TIPO_LINEA = "LINE";
	private static final String TIPO_POLIGONO = "POLYGON";
	private static final String TIPO_PUNTO = "POINT";
	
	private static final String UPDATE_GEOM = "UPDATE refundido.%s SET geom=multi(geometryfromtext(:geom)) where iden= :iden";

	
	@PersistenceContext(unitName = "rpmv2")
	private EntityManager em;
	
	@EJB
	private OperadorDeterminacionesRefundidoLocal operadorDeterminaciones;
	@EJB
	private OperadorEntidadesRefundidoLocal operadorEntidades;
	@EJB
	private CopiadorRefundidoLocal referencias;
	@EJB ( beanName = "refundido.operacion.entidades.sustraccionGrafica")
	private EjecutorLocal sustraccion;
	
	
	
	/**
     * 
     * @param operada
     * @param operadora
     * @param tipoOperacion
     * @return
	 * @throws ExcepcionRefundido 
     */
    private Geometry calcularInterseccion(Geometry operada, Geometry operadora, ContextoRefundido contexto) throws ExcepcionRefundido{
        boolean fallo=true;
        double tol=0.000001;
        Geometry geomResult=null;
        int nIntentos=0;
        
        while (fallo) {
            try{
            	geomResult=operada.intersection(operadora);
                fallo=false;
            } catch (TopologyException te){
                // La operación da error y hay que repetirla con tolerancia.
                nIntentos++;
                
                operada=operada.buffer(tol);
                operadora=operadora.buffer(tol);
                tol=tol*2;
                
                if (nIntentos>=10){      	
                	ResourceBundle traductor = (ResourceBundle)contexto.getParametro(ContextoRefundido.TRADUCTOR);
                	throw new ExcepcionRefundido(String.format(traductor.getString("refundido.operacion.entidad.adicionGrafica.error.operarGeometrias"),te.getLocalizedMessage()), 7017);
                }
            } catch (IllegalArgumentException iae) {
            	ResourceBundle traductor = (ResourceBundle)contexto.getParametro(ContextoRefundido.TRADUCTOR);
            	throw new ExcepcionRefundido(String.format(traductor.getString("refundido.operacion.entidad.adicionGrafica.error.operarGeometrias.geometriaInvalida"),iae.getLocalizedMessage()), 7018);
            }
        }
        
        return limpiarGeometria(operada.getGeometryType().toUpperCase(), geomResult);
    }

	/**
	 * Cambia el grupo de la entidad incorporada por 
	 * @param incorporada
	 * @param operadora
	 */
	@SuppressWarnings("unchecked")
	private void cambiarGrupo(Entidad incorporada, int proceso) {
		List<Entidaddeterminacionregimen> edrs = em.createNamedQuery("refundido.Entidaddeterminacionregimen.obtenerGrupoEntidad")
				.setParameter("idEntidad", incorporada.getIden())
				.setParameter("idCaracter", ClsDatos.ID_CARACTER_GRUPODEENTIDADES)
				.getResultList();
		
		if (!edrs.isEmpty()) {
			List<Determinacion> gruposEntidades = em.createNamedQuery("refundido.Determinacion.buscarPorCodigoYProceso")
					.setParameter("codigo", CODIGO_GRUPO_ENTIDADES)
					.setParameter("idProceso", proceso).getResultList();
			List<Determinacion> grupos = em.createNamedQuery("refundido.Determinacion.buscarPorCodigoYProceso")
					.setParameter("codigo", CODIGO_GRUPO)
					.setParameter("idProceso", proceso).getResultList();
			if (!gruposEntidades.isEmpty() && !grupos.isEmpty()) {
				List<Opciondeterminacion> ods = em.createNamedQuery("refundido.Opciondeterminacion.buscarPorDetYValorRef")
						.setParameter("idDeterminacion", gruposEntidades.get(0).getIden())
						.setParameter("idValorRef", grupos.get(0).getIden())
						.getResultList();
				
				if (!ods.isEmpty()) {
					Entidaddeterminacionregimen edr = edrs.get(0);
					edr.setOpciondeterminacion(ods.get(0));
					em.flush();
				}
			}
		}
		
	}

	/** 
	 * Recupera recursivamente determinaciones marcadas para su eliminación.
	 * 
	 * @param determinacion
	 * @param paraEliminar
	 * @param contexto 
	 */
	@SuppressWarnings("unchecked")
	private void cancelarEliminacion(Determinacion determinacion,
			List<Integer> paraEliminar, ContextoRefundido contexto) {
		if (determinacion != null && paraEliminar.contains(Integer.valueOf(determinacion.getIden()))) {
			paraEliminar.remove(Integer.valueOf(determinacion.getIden()));
			
			// Se cambia el código para evitar duplicidades
			determinacion.setCodigo(operadorDeterminaciones.getSiguienteCodigo(contexto));
			
			cancelarEliminacion(determinacion.getDeterminacionByIddeterminacionbase(), paraEliminar, contexto);
			cancelarEliminacion(determinacion.getDeterminacionByIdpadre(), paraEliminar, contexto);
			
			List<Integer> unidades = em.createNamedQuery("refundido.Vectorrelacion.obtenerUnidades")
					.setParameter("idDeterminacion", determinacion.getIden())
					.getResultList();
			
			if (!unidades.isEmpty()) {
				cancelarEliminacion(em.find(Determinacion.class, unidades.get(0)), paraEliminar, contexto);
			}
		}
	}
	
	/**
	 * Cancela la eliminación de todas las determinaciones que estén 
	 * directamente relacionadas con la entidad incorporada.
	 * así como de los documentos que estén asociados a la entidad.
	 * 
	 * @param incorporada Entidad que se ha incorporado.
	 * @param contexto Contexto de refundido
	 * @param recursivo Si también cancela la eliminación de sus entidades hijas.
	 */
	@SuppressWarnings("unchecked")
	private void cancelarEliminacion(Entidad incorporada, ContextoRefundido contexto, boolean recursivo) {
		em.refresh(incorporada);
		List<Integer> paraEliminar = (List<Integer>) contexto.getParametro(ENTIDADES_A_ELIMINAR);
		
		paraEliminar.remove(new Integer(incorporada.getIden()));
		
		// Se cambia el código de la entidad incorporada para evitar duplicidades
		incorporada.setCodigo(operadorEntidades.getSiguienteCodigo(contexto));
		
		paraEliminar = (List<Integer>) contexto.getParametro(DOCUMENTOS_A_ELIMINAR);
		
		for (Documentoentidad de : incorporada.getDocumentoentidads()) {
			paraEliminar.remove(new Integer(de.getDocumento().getIden()));
		}
		
		paraEliminar = (List<Integer>) contexto.getParametro(DETERMINACIONES_A_ELIMINAR);
		for (Entidaddeterminacion ed : incorporada.getEntidaddeterminacions()) {
			cancelarEliminacion(ed.getDeterminacion(), paraEliminar, contexto);
			
			for (Casoentidaddeterminacion caso : ed.getCasoentidaddeterminacions()) {
				for (Entidaddeterminacionregimen edr : caso.getEntidaddeterminacionregimensForIdcaso()) {
					cancelarEliminacion(edr.getDeterminacion(), paraEliminar, contexto);
					if (edr.getOpciondeterminacion() != null) {
						cancelarEliminacion(edr.getOpciondeterminacion().getDeterminacionByIddeterminacion(), paraEliminar, contexto);
						cancelarEliminacion(edr.getOpciondeterminacion().getDeterminacionByIddeterminacionvalorref(), paraEliminar, contexto);
					}
				}
			}
		}
		
		if (recursivo) {
			for (Entidad hija: incorporada.getEntidadsForIdpadre()) {
				cancelarEliminacion(hija, contexto, recursivo);
			}
		}
	}

	/* (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.servicios.refundido.operaciones.entidades.EjecutorLocal#ejecutar(int, int, es.mitc.redes.urbanismoenred.servicios.refundido.ContextoRefundido)
	 */
	@Override
	public void ejecutar(int idOperada, int idOperadora,
			ContextoRefundido contexto) throws ExcepcionRefundido {
		es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidad operadoraPlaneamiento = em.find(es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidad.class, idOperadora);
		es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidad operadaPlaneamiento = em.find(es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidad.class, idOperada);
		
		contexto.logTraducido(LOG.INFO, "refundido.operacion.entidad.incorporacionPlan.mensaje", 
				operadaPlaneamiento.getCodigo(),
				operadaPlaneamiento.getTramite().getPlan().getCodigo(), 
				operadoraPlaneamiento.getCodigo(), 
				operadoraPlaneamiento.getTramite().getPlan().getCodigo());
		
		Entidad operadora = referencias.getReferencia((int)contexto.getParametro(ContextoRefundido.ID_PROCESO), Entidad.class, idOperadora);
		
		if (operadora != null && !operadora.isBsuspendida()) {
			Entidad aportadas = operadorEntidades.obtenerCarpetaEntidadesAportadas(operadaPlaneamiento.getTramite().getPlan(), contexto, false);
			
			if (aportadas != null) {
				Entidad ambitoOperado = operadorEntidades.obtenerAmbitoAplicacion(operadaPlaneamiento.getTramite().getPlan(), (int)contexto.getParametro(ContextoRefundido.ID_PROCESO));
				
				Determinacion grupoOperadora=(Determinacion) em.createNamedQuery("refundido.Determinacion.obtenerGrupoEntidad")
	            		.setParameter("idEntidad", operadora.getIden())
	            		.setParameter("idCaracter",ClsDatos.ID_CARACTER_GRUPODEENTIDADES)
	            		.getSingleResult();
				
				List<Entidad> incorporadas = obtenerIncorporadas(aportadas, grupoOperadora, ambitoOperado);
				
				List<Determinacion> gruposIncorporadas = obtenerGrupos(incorporadas);
				
				// Se recortan del plan operador todas aquellas entidades que
				// coinciden con alguno de los grupos de las incorporadas.
				for (Determinacion grupoEntidad : gruposIncorporadas) {
					List<Entidad> mismoGrupo = getEntidadesPorGrupoTramite(grupoEntidad, operadora.getTramite());
					es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidad original;
					for (Entidad entidad : mismoGrupo) {
						original = getEntidadOriginal(entidad, (int)contexto.getParametro(ContextoRefundido.ID_PROCESO));
						// Se asegura que sólo se recortan entidades del trámite operador
						// que tengan el mismo grupo que la entidad incorporada.
						if (original != null && original.getTramite().getIden() == operadoraPlaneamiento.getTramite().getIden()) {
							if (!entidad.getEntidadpols().isEmpty() || 
									!entidad.getEntidadlins().isEmpty() || 
									!entidad.getEntidadpnts().isEmpty()) {
								sustraccion.ejecutar(original.getIden(), 
										operadoraPlaneamiento.getIden(), 
										contexto);
							}
						}
					}
				}
				
				// Se recortan las entidades incorporadas con la incorporadora.
				for (Entidad incorporada : incorporadas) {
					if (recortar(incorporada,
							operadora,
							contexto)) {
						cancelarEliminacion(incorporada, contexto, false);
						
						if (incorporada.getEntidadByIdpadre().getIden() == ambitoOperado.getIden() 
								|| incorporada.getEntidadByIdpadre().getIden() == aportadas.getIden()) {
							incorporada.setEntidadByIdpadre(operadora);
						}
					}
				}
			}
			em.flush();
		} else {
			if (operadora == null) {
				contexto.logTraducido(LOG.AVISO,"refundido.operacion.entidad.aviso.noOperadora");
			} else {
				contexto.logTraducido(LOG.AVISO,"refundido.operacion.entidad.aviso.operadoraSuspendida");
			}
		}
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
        resultado = em.createNamedQuery("refundido.Entidad.obtenerPorValorRefTramite")
        		.setParameter("idValorRef", detGrupoEntidades.getIden())
        		.setParameter("idTramite", tramite.getIden())
        		.getResultList();

        if (detGrupoEntidades.getDeterminacionByIddeterminacionbase() != null) {
        	// 2 Entidades que tienen como alguno de sus valores de referencia a alguna
            // determinación cuyo determinación base es la misma que la de detGrupoEntidades
            resultado.addAll(em.createNamedQuery("refundido.Entidad.obtenerPorValorRefBaseTramite")
            		.setParameter("idValorRef", detGrupoEntidades.getIden())
            		.setParameter("idBase",detGrupoEntidades.getDeterminacionByIddeterminacionbase().getIden())
            		.setParameter("idTramite", tramite.getIden())
            		.getResultList());
        }
   
        return resultado;
    }
	
	/**
	 * 
	 * @param entidad
	 * @param idProceso
	 * @return
	 */
	private es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidad getEntidadOriginal(Entidad entidad, int idProceso) {
		try {
			Integer idPlaneamiento = (Integer) em.createNamedQuery("refundido.Traza.obtenerIdPlaneamiento")
					.setParameter("tabla", entidad.getClass().getSimpleName())
					.setParameter("idProceso", idProceso)
					.setParameter("idRefundido", entidad.getIden())
					.getSingleResult();
			
			return em.find(es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidad.class, idPlaneamiento);
		} catch (NoResultException nre) {
			
		} catch (NonUniqueResultException nure) {
			
		}
		return null;
	}
	
	/**
     * 
     * @param tipoGeom
     * @param aGeom
     * @return
     * @throws ExcepcionRefundido 
     */
    private Geometry limpiarGeometria(String tipoGeom, Geometry geometria) {
    	if (!geometria.getGeometryType().toUpperCase().contains("COLLECTION")) {
        	return geometria;
        } else {
        	String tipo = TIPO_POLIGONO;
        	
        	if (tipoGeom.contains(TIPO_LINEA)) {
        		tipo = TIPO_LINEA;
        	} else if (tipoGeom.contains(TIPO_PUNTO)) {
        		tipo = TIPO_PUNTO;
        	}
        	
        	Geometry geometriaLimpia = null;
        	Geometry temp = null;
        	
        	for (int i = 0; i < geometria.getNumGeometries();i++) {
        		temp = geometria.getGeometryN(i);
        		if (temp.getGeometryType().toUpperCase().contains(tipo)) {
        			if (geometriaLimpia != null) {
        				geometriaLimpia = geometriaLimpia.union(temp);
        			} else {
        				geometriaLimpia = temp;
        			}
        		}
        	}
        	
        	return geometriaLimpia;
        }
    }
    
    /**
	 * 
	 * @param incorporadas
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private List<Determinacion> obtenerGrupos(List<Entidad> incorporadas) {
		List<Determinacion> grupos = new ArrayList<Determinacion>();
		
		Query consulta = em.createNamedQuery("refundido.Determinacion.obtenerGrupoEntidad")
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
	 * Obtiene las entidades a incorporar, que básicamente son todas las del
	 * plan excepto las que pertenezcan al mismo grupo que la incorporadora
	 * y la entidad ámbito de aplicación.
	 * 
	 * @param aportadas
	 * @param grupoOperadora
	 * @param ambitoOperado
	 * @return
	 */
	private List<Entidad> obtenerIncorporadas(Entidad entidad,
			Determinacion grupoOperadora, Entidad ambitoOperado) {
		List<Entidad> incorporadas = new ArrayList<Entidad>();
		if (entidad.getIden() != ambitoOperado.getIden()) {
			Determinacion grupoEntidad=(Determinacion) em.createNamedQuery("refundido.Determinacion.obtenerGrupoEntidad")
            		.setParameter("idEntidad", entidad.getIden())
            		.setParameter("idCaracter",ClsDatos.ID_CARACTER_GRUPODEENTIDADES)
            		.getSingleResult();
			
			if (grupoEntidad.getIden() != grupoOperadora.getIden()) {
				incorporadas.add(entidad);
				for(Entidad hija : entidad.getEntidadsForIdpadre()) {
					incorporadas.addAll(obtenerIncorporadas(hija, grupoOperadora, ambitoOperado));
				}
			}
		}  else {
			for(Entidad hija : entidad.getEntidadsForIdpadre()) {
				incorporadas.addAll(obtenerIncorporadas(hija, grupoOperadora, ambitoOperado));
			}
		}
		
		return incorporadas;
	}

	/**
	 * 
	 * @param incorporada
	 * @param operadora
	 * @param contexto 
	 * @throws ExcepcionRefundido 
	 */
	private boolean recortar(Entidad incorporada, Entidad operadora, ContextoRefundido contexto) throws ExcepcionRefundido {
		Geometry geometriaOperadora = null;
		Geometry geometriaOperada = null;
		WKTReader reader = new WKTReader();
		Object geometria;
		ResourceBundle traductor = (ResourceBundle) contexto.getParametro(ContextoRefundido.TRADUCTOR);
		try {
			em.refresh(incorporada);
			// En un futuro lo suyo es eliminar la distinción entre las tablas
			// de polígono, línea o punto, ya que en cualquiera se puede
			// guardar cualquier geometría
			if (operadora.getEntidadpols().size() > 0) {
				geometriaOperadora = reader.read(operadora.getEntidadpols().toArray(new Entidadpol[0])[0].getGeom());
				
				if (incorporada.getEntidadpols().size() > 0) {
					geometria = incorporada.getEntidadpols().toArray(new Entidadpol[0])[0];
					geometriaOperada = reader.read(((Entidadpol)geometria).getGeom());
				} else if (incorporada.getEntidadlins().size() > 0) {
					geometria = incorporada.getEntidadlins().toArray(new Entidadlin[0])[0];
					geometriaOperada = reader.read(((Entidadlin)geometria).getGeom());
				} else if (incorporada.getEntidadpnts().size() > 0) {
					geometria = incorporada.getEntidadpnts().toArray(new Entidadpnt[0])[0];
					geometriaOperada = reader.read(((Entidadlin)geometria).getGeom());
				} else {
					// No tiene geometría, no se hace nada, y se incorpora
					return true;
				}
				
				if (geometriaOperada.intersects(geometriaOperadora)) {
					geometriaOperada = calcularInterseccion(geometriaOperada, geometriaOperadora, contexto);
					
					if (em.createNativeQuery(String.format(UPDATE_GEOM, geometria.getClass().getSimpleName()))
							.setParameter("geom", geometriaOperada.toText())
							.setParameter("iden", (int)geometria.getClass().getDeclaredMethod("getIden").invoke(geometria)).executeUpdate() != 1) {
						es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidad operadaPlaneamiento = getEntidadOriginal(incorporada, (int)contexto.getParametro(ContextoRefundido.ID_PROCESO));
						es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidad operadoraPlaneamiento = getEntidadOriginal(operadora, (int)contexto.getParametro(ContextoRefundido.ID_PROCESO));
						throw new ExcepcionRefundido(String.format(traductor.getString("refundido.operacion.entidad.incorporacion.error.actualizarGeometria"), 
								operadaPlaneamiento.getCodigo(),
								operadaPlaneamiento.getTramite().getPlan().getCodigo(), 
								operadoraPlaneamiento.getCodigo(),
								operadoraPlaneamiento.getTramite().getPlan().getCodigo())
								, 7020);
		            }
					
					em.flush();
					em.refresh(geometria);
					return true;
				} else {
					// Si no intersecta se elimina la geometría y se cambia
					// su grupo de entidad a "Grupo", pero sólo si tiene hijas.
					if (!incorporada.getEntidadsForIdpadre().isEmpty()) {
						em.remove(geometria);
						cambiarGrupo(incorporada, (int)contexto.getParametro(ContextoRefundido.ID_PROCESO));
						
						return true;
					} 
				}
			} else {
				es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidad operadaPlaneamiento = getEntidadOriginal(incorporada, (int)contexto.getParametro(ContextoRefundido.ID_PROCESO));
				es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidad operadoraPlaneamiento = getEntidadOriginal(operadora, (int)contexto.getParametro(ContextoRefundido.ID_PROCESO));
				throw new ExcepcionRefundido(String.format(traductor.getString("refundido.operacion.entidad.incorporacion.error.operadora.noPoligonal"), 
						operadaPlaneamiento.getCodigo(),
						operadaPlaneamiento.getTramite().getPlan().getCodigo(), 
						operadoraPlaneamiento.getCodigo(),
						operadoraPlaneamiento.getTramite().getPlan().getCodigo())
						, 7022);
			}
		} catch (PersistenceException pe) {
			es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidad operadaPlaneamiento = getEntidadOriginal(incorporada, (int)contexto.getParametro(ContextoRefundido.ID_PROCESO));
			es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidad operadoraPlaneamiento = getEntidadOriginal(operadora, (int)contexto.getParametro(ContextoRefundido.ID_PROCESO));
			throw new ExcepcionRefundido(String.format(traductor.getString("refundido.operacion.entidad.incorporacion.error.actualizarGeometria"), 
					operadaPlaneamiento.getCodigo(),
					operadaPlaneamiento.getTramite().getPlan().getCodigo(), 
					operadoraPlaneamiento.getCodigo(),
					operadoraPlaneamiento.getTramite().getPlan().getCodigo())
					, 7021);
		} catch (ParseException e) {
			es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidad operadaPlaneamiento = getEntidadOriginal(incorporada, (int)contexto.getParametro(ContextoRefundido.ID_PROCESO));
			es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidad operadoraPlaneamiento = getEntidadOriginal(operadora, (int)contexto.getParametro(ContextoRefundido.ID_PROCESO));
			if (contexto.getParametro(ContextoRefundido.NIVEL_ERRORES) != null && SOLO_AVISOS.equalsIgnoreCase(contexto.getParametro(ContextoRefundido.NIVEL_ERRORES).toString())) {
				contexto.logTraducido(LOG.AVISO, "refundido.operacion.entidad.error.procesarGeometrias", 
						operadaPlaneamiento.getCodigo(),
						operadaPlaneamiento.getTramite().getPlan().getCodigo(), 
						operadoraPlaneamiento.getCodigo(),
						operadoraPlaneamiento.getTramite().getPlan().getCodigo());
			} else {
				throw new ExcepcionRefundido(String.format(traductor.getString("refundido.operacion.entidad.error.procesarGeometrias"), 
					operadaPlaneamiento.getCodigo(),
					operadaPlaneamiento.getTramite().getPlan().getCodigo(), 
					operadoraPlaneamiento.getCodigo(),
					operadoraPlaneamiento.getTramite().getPlan().getCodigo())
					, 7052);
			}
		} catch (IllegalAccessException | IllegalArgumentException
				| InvocationTargetException | NoSuchMethodException
				| SecurityException e) {
			throw new ExcepcionRefundido(String.format(traductor.getString("refundido.excepcion.invocacion"), 
					e.getMessage())
					, 7055);
		}
		
		return false;
	}

}

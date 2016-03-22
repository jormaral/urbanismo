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

import org.apache.log4j.Logger;

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
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Tramite;
import es.mitc.redes.urbanismoenred.servicios.dal.EliminadorEntidadesRefundidoLocal;
import es.mitc.redes.urbanismoenred.servicios.refundido.ClsDatos;
import es.mitc.redes.urbanismoenred.servicios.refundido.ContextoRefundido;
import es.mitc.redes.urbanismoenred.servicios.refundido.OperadorDeterminacionesRefundidoLocal;
import es.mitc.redes.urbanismoenred.servicios.refundido.OperadorEntidadesRefundidoLocal;
import es.mitc.redes.urbanismoenred.servicios.refundido.ContextoRefundido.LOG;
import es.mitc.redes.urbanismoenred.servicios.refundido.CopiadorRefundidoLocal;
import es.mitc.redes.urbanismoenred.servicios.refundido.ExcepcionRefundido;

/**
 * La incorporación coge una entidad de un plan previo y la incluye en el
 * plan operador.
 *
 *
 * @author Arnaiz Consultores
 *
 */
@Stateless( name = "refundido.operacion.entidades.incorporacion")
public class Incorporacion implements EjecutorLocal {
	
	private static final String DETERMINACIONES_A_ELIMINAR = "refundido.operaciones.plan.eliminar.determinaciones";
	private static final String DOCUMENTOS_A_ELIMINAR = "refundido.operaciones.plan.eliminar.documentos";
	private static final String ENTIDADES_A_ELIMINAR = "refundido.operaciones.plan.eliminar.entidades";
	
	private static final Logger log = Logger.getLogger(Incorporacion.class);
	
	private static final String TIPO_LINEA = "LINE";
	private static final String TIPO_POLIGONO = "POLYGON";
	private static final String TIPO_PUNTO = "POINT";
	
	private static final String UPDATE_GEOM = "UPDATE refundido.%s SET geom=multi(geometryfromtext(:geom)) where iden= :iden";
	
	@PersistenceContext(unitName = "rpmv2")
	private EntityManager em;
	@EJB
	private CopiadorRefundidoLocal referencias;
	@EJB ( beanName = "EliminadorEntidadesRefundido" )
	private EliminadorEntidadesRefundidoLocal eliminadorEntidades;
	@EJB
	private OperadorDeterminacionesRefundidoLocal operadorDeterminaciones;
	@EJB
	private OperadorEntidadesRefundidoLocal operadorEntidades;

	/**
	 * 
	 * @param geometriaOperada
	 * @param geometriaOperadora
	 * @param contexto
	 * @return
	 * @throws ExcepcionRefundido 
	 */
	private Geometry aplicarSustraccion(Geometry geometriaOperada,
			Geometry geometriaOperadora, ContextoRefundido contexto) throws ExcepcionRefundido {
		boolean fallo=true;
        double tol=0.000001;
        Geometry geomResult=null;
        int nIntentos=0;
        
        while (fallo) {
            try{
            	geomResult = geometriaOperada.difference(geometriaOperadora); 	
                fallo=false;
            } catch (TopologyException te){
                // La operación da error y hay que repetirla con tolerancia.
                nIntentos++;
                
                geometriaOperada=geometriaOperada.buffer(tol);
                geometriaOperadora=geometriaOperadora.buffer(tol);
                tol=tol*2;
                
                if (nIntentos>=10){      	
                	ResourceBundle traductor = (ResourceBundle)contexto.getParametro(ContextoRefundido.TRADUCTOR);
                	throw new ExcepcionRefundido(String.format(traductor.getString("refundido.operacion.entidad.suspensionParcial.error.operarGeometrias"),te.getLocalizedMessage()), 7015);
                }
            } catch (IllegalArgumentException iae) {
            	ResourceBundle traductor = (ResourceBundle)contexto.getParametro(ContextoRefundido.TRADUCTOR);
            	throw new ExcepcionRefundido(String.format(traductor.getString("refundido.operacion.entidad.suspensionParcial.error.operarGeometrias.geometriaInvalida"),iae.getLocalizedMessage()), 7016);
            }
        }
        
        geomResult = limpiarGeometria(geometriaOperada.getGeometryType().toUpperCase(), geomResult);
        
        return geomResult;
	}
	
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
			log.debug("Cancelando la eliminación de la determinación incorporada con id " + determinacion.getIden() + " " + determinacion.getCodigo() + " nombre: " + determinacion.getNombre());
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
	 */
	@SuppressWarnings("unchecked")
	private void cancelarEliminacion(Entidad incorporada, ContextoRefundido contexto) {
		List<Integer> paraEliminar = (List<Integer>) contexto.getParametro(ENTIDADES_A_ELIMINAR);
		log.debug("Cancelando la eliminación de la entidad incorporada con id " + incorporada.getIden() + " " + incorporada.getCodigo() + " nombre: " + incorporada.getNombre());
		
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
		
		
	}
	
	/* (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.servicios.refundido.operaciones.entidades.EjecutorLocal#ejecutar(int, int, es.mitc.redes.urbanismoenred.servicios.refundido.ContextoRefundido)
	 */
	@Override
	public void ejecutar(int idOperada, int idOperadora,
			ContextoRefundido contexto) throws ExcepcionRefundido {
		es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidad operadoraPlaneamiento = em.find(es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidad.class, idOperadora);
		es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidad operadaPlaneamiento = em.find(es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidad.class, idOperada);
		
		contexto.logTraducido(LOG.INFO, "refundido.operacion.entidad.incorporacion.mensaje", 
				operadaPlaneamiento.getCodigo(),
				operadaPlaneamiento.getTramite().getPlan().getCodigo(), 
				operadoraPlaneamiento.getCodigo(), 
				operadoraPlaneamiento.getTramite().getPlan().getCodigo());
		
		Entidad operada = referencias.getReferencia((Integer)contexto.getParametro(ContextoRefundido.ID_PROCESO), Entidad.class, idOperada);
		
		Entidad operadora = referencias.getReferencia((Integer)contexto.getParametro(ContextoRefundido.ID_PROCESO), Entidad.class, idOperadora);
		
		ResourceBundle traductor = (ResourceBundle)contexto.getParametro(ContextoRefundido.TRADUCTOR);
		
		if (operadora != null && operada != null) {
			if (!operadora.isBsuspendida()) {
				Geometry geometriaOperadora;
				Geometry geometriaOperada;
				WKTReader reader = new WKTReader();
				Object geometria;
				try {
					em.refresh(operada);
					em.refresh(operadora);
					// En un futuro lo suyo es eliminar la distinción entre las tablas
					// de polígono, línea o punto, ya que en cualquiera se puede
					// guardar cualquier geometría
					if (operadora.getEntidadpols().size() > 0) {
						geometriaOperadora = reader.read(operadora.getEntidadpols().toArray(new Entidadpol[0])[0].getGeom());
						
						if (operada.getEntidadpols().size() > 0) {
							geometria = operada.getEntidadpols().toArray(new Entidadpol[0])[0];
							geometriaOperada = reader.read(((Entidadpol)geometria).getGeom());
						} else if (operada.getEntidadlins().size() > 0) {
							geometria = operada.getEntidadlins().toArray(new Entidadlin[0])[0];
							geometriaOperada = reader.read(((Entidadlin)geometria).getGeom());
						} else if (operada.getEntidadpnts().size() > 0) {
							geometria = operada.getEntidadpnts().toArray(new Entidadlin[0])[0];
							geometriaOperada = reader.read(((Entidadlin)geometria).getGeom());
						} else {
							throw new ExcepcionRefundido(String.format(traductor.getString("refundido.operacion.entidad.incorporacion.error.operada.sinGeometria"), 
									operadaPlaneamiento.getCodigo(),
									operadaPlaneamiento.getTramite().getPlan().getCodigo())
									, 7019);
						}
						
						geometriaOperada = calcularInterseccion(geometriaOperada, geometriaOperadora, contexto);
						try {
							
							if (em.createNativeQuery(String.format(UPDATE_GEOM, geometria.getClass().getSimpleName()))
									.setParameter("geom", geometriaOperada.toText())
									.setParameter("iden", (int)geometria.getClass().getDeclaredMethod("getIden").invoke(geometria)).executeUpdate() != 1) {
								throw new ExcepcionRefundido(String.format(traductor.getString("refundido.operacion.entidad.incorporacion.error.actualizarGeometria"), 
										operadaPlaneamiento.getCodigo(),
										operadaPlaneamiento.getTramite().getPlan().getCodigo(), 
										operadoraPlaneamiento.getCodigo(),
										operadoraPlaneamiento.getTramite().getPlan().getCodigo())
										, 7020);
				            }
							em.flush();
							em.refresh(geometria);
						} catch (PersistenceException pe) {
							throw new ExcepcionRefundido(String.format(traductor.getString("refundido.operacion.entidad.incorporacion.error.actualizarGeometria"), 
									operadaPlaneamiento.getCodigo(),
									operadaPlaneamiento.getTramite().getPlan().getCodigo(), 
									operadoraPlaneamiento.getCodigo(),
									operadoraPlaneamiento.getTramite().getPlan().getCodigo())
									, 7021);
						} 
						
					} else {
						throw new ExcepcionRefundido(String.format(traductor.getString("refundido.operacion.entidad.incorporacion.error.operadora.noPoligonal"), 
								operadaPlaneamiento.getCodigo(),
								operadaPlaneamiento.getTramite().getPlan().getCodigo(), 
								operadoraPlaneamiento.getCodigo(),
								operadoraPlaneamiento.getTramite().getPlan().getCodigo())
								, 7022);
					}
					
					Entidad aportadas = operadorEntidades.obtenerCarpetaEntidadesAportadas(operadaPlaneamiento.getTramite().getPlan(), contexto, true);
					
					int orden = 0;
					em.refresh(aportadas);
					for (Entidad hija : aportadas.getEntidadsForIdpadre()) {
						if (orden < hija.getOrden()) {
							orden = hija.getOrden()+1;
						}
					}
					operada.setEntidadByIdpadre(aportadas);
					operada.setOrden(orden);
					
					while (aportadas != null) {
						cancelarEliminacion(aportadas, contexto);
						aportadas = aportadas.getEntidadByIdpadre();
					}
					cancelarEliminacion(operada, contexto);
					
					// Recorta las entidades del trámite incorporador cuyo grupo (valor
		            // para la determinación 'Grupo de entidades' sea el grupo de la
		            // entidad incorporada.
		            // Se excluyen: la entidad incorporadora y la incorporada
		            // También se excluyen aquellas entidades que hayan sido 
		            // previamente incorporadas al plan incorporador, y siempre que la 
		            // carpeta que las contenga pertenezca a un plan de orden 
		            // anterior, de modo que sólo queden excluídas las entidades
		            // aportadas por la operación de incorporación, y no las que
		            // provengan de desarrollos o aportaciones. La carpeta 'padre'
		            // debe ser una de las 'Aportadas por...'
					Determinacion detGrupoEntidades=(Determinacion) em.createNamedQuery("refundido.Determinacion.obtenerGrupoEntidad")
		            		.setParameter("idEntidad", operada.getIden())
		            		.setParameter("idCaracter",ClsDatos.ID_CARACTER_GRUPODEENTIDADES)
		            		.getSingleResult();
					
					Geometry geometriaEntidad;
					es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidad entidadOriginal;
					for(Entidad entidad : getEntidadesPorGrupoTramite(detGrupoEntidades, operadora.getTramite())) {
						em.refresh(entidad);
						entidadOriginal = getEntidadOriginal(entidad, (Integer)contexto.getParametro(ContextoRefundido.ID_PROCESO));
						if (entidad != operadora && 
								entidad != operada
								&& entidadOriginal != null 
								&& entidadOriginal.getTramite().getIden() == operadoraPlaneamiento.getTramite().getIden()) {
							Object geometriaAnterior = null;
							if (entidad.getEntidadpols().size() > 0) {
								Entidadpol poligono = entidad.getEntidadpols().toArray(new Entidadpol[0])[0];
								geometriaEntidad = reader.read(poligono.getGeom());
								geometriaAnterior = poligono;
							} else if (entidad.getEntidadlins().size() > 0) {
								Entidadlin linea = entidad.getEntidadlins().toArray(new Entidadlin[0])[0];
								geometriaEntidad = reader.read(linea.getGeom());
								geometriaAnterior = linea;
							} else if (entidad.getEntidadpnts().size() > 0) {
								Entidadpnt punto = entidad.getEntidadpnts().toArray(new Entidadpnt[0])[0];
								geometriaEntidad = reader.read(punto.getGeom());
								geometriaAnterior = punto;
							} else {
								geometriaEntidad = null;
							}
							
							if (geometriaEntidad != null) {
								// Se comprueba si hay solapamiento
								if (geometriaOperada.intersects(geometriaEntidad)) {
									geometriaEntidad = aplicarSustraccion(geometriaEntidad, geometriaOperada, contexto);
									try {
										if (geometriaEntidad != null && !geometriaEntidad.isEmpty()) {
											em.remove(geometriaAnterior);
											String tipo = geometriaEntidad.getGeometryType().toUpperCase();
											
											if (tipo.contains("POLYGON")) {
												Entidadpol poligono = new Entidadpol();
												poligono.setBsuspendida(false);
												poligono.setGeom(geometriaEntidad.toText());
												poligono.setEntidad(entidad);
												em.persist(poligono);
												em.flush();
												
												geometria = poligono;
											} else if (tipo.contains("LINE")) {
												Entidadlin linea = new Entidadlin();
												linea.setBsuspendida(false);
												linea.setGeom(geometriaEntidad.toText());
												linea.setEntidad(entidad);
												em.persist(linea);
												em.flush();
												
												geometria = linea;
											} else if (tipo.contains("POINT")) {
												Entidadpnt punto = new Entidadpnt();
												punto.setBsuspendida(false);
												punto.setGeom(geometriaEntidad.toText());
												punto.setEntidad(entidad);
												em.persist(punto);
												em.flush();
												
												geometria = punto;
											} else {
												throw new ExcepcionRefundido(traductor.getString("refundido.operacion.entidad.error.geometriaDesconocida"), 7033);
											}
											
											if (em.createNativeQuery(String.format(UPDATE_GEOM, geometria.getClass().getSimpleName()))
													.setParameter("geom", geometriaEntidad.toText())
													.setParameter("iden", (int)geometria.getClass().getDeclaredMethod("getIden").invoke(geometria)).executeUpdate() != 1) {
												throw new ExcepcionRefundido(String.format(traductor.getString("refundido.operacion.entidad.incorporacion.error.actualizarGeometria"), 
														operadaPlaneamiento.getCodigo(),
														operadaPlaneamiento.getTramite().getPlan().getCodigo(), 
														operadoraPlaneamiento.getCodigo(),
														operadoraPlaneamiento.getTramite().getPlan().getCodigo())
														, 7023);
								            }
											em.flush();
											em.refresh(geometria);
										} else {
											
											contexto.log(LOG.AVISO, 
													String.format(traductor.getString("refundido.operacion.entidad.incorporacion.aviso.entidadEliminada")
													, entidad.getNombre()
													,entidad.getCodigo()));
											eliminadorEntidades.eliminar(entidad);
										}

									} catch (PersistenceException pe) {
										throw new ExcepcionRefundido(String.format(traductor.getString("refundido.operacion.entidad.incorporacion.error.actualizarGeometria"), 
												entidad.getCodigo(),
												entidad.getTramite().getPlan().getCodigo(), 
												operadoraPlaneamiento.getCodigo(),
												operadoraPlaneamiento.getTramite().getPlan().getCodigo())
												, 7026);
									}
								}
							} 
						}
					}
					
					em.flush();
				} catch (ParseException pe) {
					throw new ExcepcionRefundido(String.format(traductor.getString("refundido.operacion.entidad.adicionGrafica.error.procesarGeometrias"), 
							operadaPlaneamiento.getCodigo(),
							operadaPlaneamiento.getTramite().getPlan().getCodigo(), 
							operadoraPlaneamiento.getCodigo(),
							operadoraPlaneamiento.getTramite().getPlan().getCodigo())
							, 7027);
				} catch (IllegalAccessException | IllegalArgumentException
						| InvocationTargetException | NoSuchMethodException
						| SecurityException e) {
					throw new ExcepcionRefundido(String.format(traductor.getString("refundido.excepcion.invocacion"), 
							e.getMessage())
							, 7055);
				} 
			} else {
				contexto.logTraducido(LOG.AVISO,"refundido.operacion.entidad.aviso.operadoraSuspendida");
			}
		} else {
			if (operada == null) {
				throw new ExcepcionRefundido(String.format(traductor.getString("refundido.operacion.entidad.error.noOperada"), 
						operadaPlaneamiento.getCodigo(), 
						operadaPlaneamiento.getTramite().getPlan().getCodigo())
						, 7028);
			} else {
				throw new ExcepcionRefundido(String.format(traductor.getString("refundido.operacion.entidad.error.noOperadaora"), 
						operadoraPlaneamiento.getCodigo(), 
						operadoraPlaneamiento.getTramite().getPlan().getCodigo())
						, 7029);
			}
		}
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

}

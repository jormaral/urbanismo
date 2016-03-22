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
import java.util.ResourceBundle;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.TopologyException;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;

import es.mitc.redes.urbanismoenred.data.rpm.refundido.Casoentidaddeterminacion;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Entidad;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Entidaddeterminacion;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Entidaddeterminacionregimen;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Entidadlin;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Entidadpnt;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Entidadpol;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Regimenespecifico;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Vinculocaso;
import es.mitc.redes.urbanismoenred.servicios.refundido.ContextoRefundido;
import es.mitc.redes.urbanismoenred.servicios.refundido.ContextoRefundido.LOG;
import es.mitc.redes.urbanismoenred.servicios.refundido.ExcepcionRefundido;
import es.mitc.redes.urbanismoenred.servicios.refundido.GestorAportacionesLocal;
import es.mitc.redes.urbanismoenred.servicios.refundido.OperadorEntidadesRefundidoLocal;

/**
 * 
 * @author Arnaiz Consultores
 *
 */
@Stateless( name = "refundido.operacion.entidades.suspensionParcial")
public class SuspensionParcial implements EjecutorLocal {
	
	private static final String SOLO_AVISOS = "Avisos";
	
	private static final String UPDATE_GEOM = "UPDATE refundido.%s SET geom=multi(geometryfromtext(:geom)) where iden= :iden";
	
	@PersistenceContext(unitName = "rpmv2")
	private EntityManager em;
	
	@EJB ( beanName = "refundido.operacion.entidades.eliminacion")
	private EjecutorLocal eliminacion;
	@EJB
	private GestorAportacionesLocal gestorAportaciones;
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
            	geomResult = limpiarGeometria(geometriaOperada.getGeometryType(), geomResult);
            	
                fallo=false;
            } catch (TopologyException te){
                // La operación da error y hay que repetirla con tolerancia.
                nIntentos++;
                
                geometriaOperada=geometriaOperada.buffer(tol);
                geometriaOperadora=geometriaOperadora.buffer(tol);
                tol=tol*2;
                
                if (nIntentos>=10){      	
                	ResourceBundle traductor = (ResourceBundle)contexto.getParametro(ContextoRefundido.TRADUCTOR);
                	throw new ExcepcionRefundido(String.format(traductor.getString("refundido.operacion.entidad.error.operarGeometrias"),te.getLocalizedMessage()), 7030);
                }
            } catch (IllegalArgumentException iae) {
            	ResourceBundle traductor = (ResourceBundle)contexto.getParametro(ContextoRefundido.TRADUCTOR);
            	throw new ExcepcionRefundido(String.format(traductor.getString("refundido.operacion.entidad.error.operarGeometrias.geometriaInvalida"),iae.getLocalizedMessage()), 7031);
            }
        }
        
        return geomResult;
	}
	
	/**
     * 
     * @param original
     * @param edcopia
     */
	private void copiar(Casoentidaddeterminacion original,
			Entidaddeterminacion edcopia) {
		Casoentidaddeterminacion copia = new Casoentidaddeterminacion();
		
		copia.setNombre(original.getNombre());
		copia.setOrden(original.getOrden());
		copia.setEntidaddeterminacion(edcopia);
		
		em.persist(copia);
		
		for (Entidaddeterminacionregimen edr : original.getEntidaddeterminacionregimensForIdcaso()) {
			Entidaddeterminacionregimen edrCopia = new Entidaddeterminacionregimen();
			edrCopia.setCasoentidaddeterminacionByIdcaso(copia);
			edrCopia.setCasoentidaddeterminacionByIdcasoaplicacion(edr.getCasoentidaddeterminacionByIdcasoaplicacion());
			
			edrCopia.setDeterminacion(edr.getDeterminacion());
			
			edrCopia.setOpciondeterminacion(edr.getOpciondeterminacion());
			
			edrCopia.setSuperposicion(edr.getSuperposicion());
			
			edrCopia.setValor(edr.getValor());
			
			em.persist(edrCopia);
			
			for (Regimenespecifico re : edr.getRegimenespecificos()) {
				copiar(re,edrCopia);
			}
		}
		
		for (Entidaddeterminacionregimen edr : original.getEntidaddeterminacionregimensForIdcasoaplicacion()) {
			Entidaddeterminacionregimen edrCopia = new Entidaddeterminacionregimen();
			edrCopia.setCasoentidaddeterminacionByIdcaso(edr.getCasoentidaddeterminacionByIdcaso());
			edrCopia.setCasoentidaddeterminacionByIdcasoaplicacion(copia);
			
			edrCopia.setDeterminacion(edr.getDeterminacion());
			
			edrCopia.setOpciondeterminacion(edr.getOpciondeterminacion());
			
			edrCopia.setSuperposicion(edr.getSuperposicion());
			
			edrCopia.setValor(edr.getValor());
			
			em.persist(edrCopia);
			
			for (Regimenespecifico re : edr.getRegimenespecificos()) {
				copiar(re,edrCopia);
			}
		}
		
		for (Vinculocaso vc : original.getVinculocasosForIdcaso()) {
			Vinculocaso vcCopia = new Vinculocaso();
			
			vcCopia.setCasoentidaddeterminacionByIdcaso(copia);
			vcCopia.setCasoentidaddeterminacionByIdcasovinculado(vc.getCasoentidaddeterminacionByIdcasovinculado());
			
			em.persist(vcCopia);
		}
		
		for (Vinculocaso vc : original.getVinculocasosForIdcasovinculado()) {
			Vinculocaso vcCopia = new Vinculocaso();
			
			vcCopia.setCasoentidaddeterminacionByIdcaso(vc.getCasoentidaddeterminacionByIdcaso());
			vcCopia.setCasoentidaddeterminacionByIdcasovinculado(copia);
			
			em.persist(vcCopia);
		}
	}

    /**
	 * 
	 * @param original
	 * @param edrCopia
	 * @return
	 */
	private Regimenespecifico copiar(Regimenespecifico original,
			Entidaddeterminacionregimen edrCopia) {
		Regimenespecifico copia = new Regimenespecifico();
		
		copia.setEntidaddeterminacionregimen(edrCopia);
		copia.setNombre(original.getNombre());
		copia.setOrden(original.getOrden());
		if (original.getRegimenespecifico() != null) {
			copia.setRegimenespecifico(copiar(original.getRegimenespecifico(),edrCopia));
		}
		copia.setTexto(original.getTexto());
		em.persist(copia);
		
		return copia;
	}

	/**
     * Realiza una copia de una entidad que ha sufrido una suspensión parcial.
     * La copia se deja en la carpeta de entidades aportadas.
     * @param padre 
     * 
     * @param original
     * @return
	 * @throws ExcepcionRefundido 
     */
    private Entidad copiarEntidad(Entidad original, Entidad padre, Geometry geometria, ContextoRefundido contexto) throws ExcepcionRefundido {
    	
    	ResourceBundle traductor = (ResourceBundle) contexto.getParametro(ContextoRefundido.TRADUCTOR);
		Entidad copia = new Entidad();
		copia.setBsuspendida(true);
		copia.setClave(original.getClave());
		copia.setCodigo(operadorEntidades.getSiguienteCodigo(contexto));
		copia.setEtiqueta(original.getEtiqueta());
		copia.setNombre(traductor.getString("refundido.entidad.nombre.suspendida") + original.getNombre());
		
		if (copia.getNombre().length() > 100) {
			copia.setNombre(copia.getNombre().substring(0, 100));
		}
		copia.setOrden(original.getOrden());
		copia.setTramite(original.getTramite());
		
		copia.setEntidadByIdpadre(padre);
		copia.setEntidadByIdentidadbase(original.getEntidadByIdentidadbase());
		copia.setEntidadByIdentidadoriginal(original);
		
		em.persist(copia);
		
		for (Entidaddeterminacion ed : original.getEntidaddeterminacions()) {
			Entidaddeterminacion edcopia = new Entidaddeterminacion();
			edcopia.setDeterminacion(ed.getDeterminacion());
			edcopia.setEntidad(copia);
			
			em.persist(edcopia);
			
			for (Casoentidaddeterminacion ced : ed.getCasoentidaddeterminacions()) {
				copiar(ced,edcopia);
			}
		}
		
		if (geometria != null) {
			String tipo = geometria.getGeometryType().toUpperCase();
			Object registro;
			if (tipo.contains("POLYGON")) {
				Entidadpol poligono = new Entidadpol();
				poligono.setBsuspendida(false);
				poligono.setEntidad(copia);
				poligono.setGeom(geometria.toText());
				em.persist(poligono);
				em.flush();
				registro = poligono;
			} else if (tipo.contains("LINE")) {
				Entidadlin linea = new Entidadlin();
				linea.setBsuspendida(false);
				linea.setEntidad(copia);
				linea.setGeom(geometria.toText());
				em.persist(linea);
				em.flush();
				registro = linea;
			} else if (tipo.contains("POINT")) {
				Entidadpnt punto = new Entidadpnt();
				punto.setBsuspendida(false);
				punto.setEntidad(copia);
				punto.setGeom(geometria.toText());
				em.persist(punto);
				em.flush();
				registro = punto;
			} else {
				throw new ExcepcionRefundido(traductor.getString("refundido.operacion.entidad.error.geometriaDesconocida"), 7033);
			}
			
			try {
				if (em.createNativeQuery(String.format(UPDATE_GEOM, registro.getClass().getSimpleName()))
						.setParameter("geom", geometria.toText())
						.setParameter("iden", (int)registro.getClass().getDeclaredMethod("getIden").invoke(registro)).executeUpdate() != 1) {
					throw new ExcepcionRefundido(traductor.getString("refundido.operacion.entidad.error.guardarGeometria"), 7032);
				}
			} catch (IllegalAccessException | IllegalArgumentException
					| InvocationTargetException | NoSuchMethodException
					| SecurityException e) {
				throw new ExcepcionRefundido(String.format(traductor.getString("refundido.excepcion.invocacion"), 
						e.getMessage())
						, 7057);
			}
			em.flush();
			em.refresh(registro);
		}
		
		em.flush();
		
		return copia;
	}

	/* (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.servicios.refundido.operaciones.entidades.EjecutorLocal#ejecutar(int, int, es.mitc.redes.urbanismoenred.servicios.refundido.ContextoRefundido)
	 */
	@Override
	public void ejecutar(int idOperada, int idOperadora,
			ContextoRefundido contexto) throws ExcepcionRefundido {
		
		es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidad operadoraPlaneamiento = em.find(es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidad.class, idOperadora);
		es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidad operadaPlaneamiento = em.find(es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidad.class, idOperada);
		
		contexto.logTraducido(LOG.INFO, "refundido.operacion.entidad.suspensionParcial.mensaje", 
				operadaPlaneamiento.getCodigo(),
				operadaPlaneamiento.getTramite().getPlan().getCodigo(), 
				operadoraPlaneamiento.getCodigo(), 
				operadoraPlaneamiento.getTramite().getPlan().getCodigo());
		
		Entidad operada = gestorAportaciones.aportar(operadaPlaneamiento, null, null, contexto);
		Entidad operadora = gestorAportaciones.aportar(operadoraPlaneamiento, null, null, contexto);
		
		ResourceBundle traductor = (ResourceBundle) contexto.getParametro(ContextoRefundido.TRADUCTOR);
		
		if (operada != null && operadora != null) {
			if (!operadora.isBsuspendida()) {
				Geometry geometriaOperadora = null;
				Geometry geometriaOperada = null;
				WKTReader reader = new WKTReader();
				Object registro;
				try {
					if (operada.getEntidadpols().size() > 0) {
						if (operadora.getEntidadpols().size() > 0) {
							geometriaOperadora = reader.read(operadora.getEntidadpols().toArray(new Entidadpol[0])[0].getGeom());
						} else {
							if (contexto.getParametro(ContextoRefundido.NIVEL_ERRORES) != null && SOLO_AVISOS.equalsIgnoreCase(contexto.getParametro(ContextoRefundido.NIVEL_ERRORES).toString())) {
								contexto.logTraducido(LOG.AVISO, "refundido.operacion.entidad.error.geometriasDiferentes", operadaPlaneamiento.getCodigo(),
									operadaPlaneamiento.getTramite().getPlan().getCodigo(), 
									operadoraPlaneamiento.getCodigo(),
									operadoraPlaneamiento.getTramite().getPlan().getCodigo());
								return;
							} else {
								throw new ExcepcionRefundido(String.format(traductor.getString("refundido.operacion.entidad.error.geometriasDiferentes"), 
									operadaPlaneamiento.getCodigo(),
									operadaPlaneamiento.getTramite().getPlan().getCodigo(), 
									operadoraPlaneamiento.getCodigo(),
									operadoraPlaneamiento.getTramite().getPlan().getCodigo())
									, 7036);
							}
						}
						
						registro = operada.getEntidadpols().toArray(new Entidadpol[0])[0];
						geometriaOperada = reader.read(((Entidadpol)registro).getGeom());
					} else if (operada.getEntidadlins().size() > 0) {
						if (operadora.getEntidadlins().size() > 0) {
							geometriaOperadora = reader.read(operadora.getEntidadlins().toArray(new Entidadlin[0])[0].getGeom());
						} else {
							if (contexto.getParametro(ContextoRefundido.NIVEL_ERRORES) != null && SOLO_AVISOS.equalsIgnoreCase(contexto.getParametro(ContextoRefundido.NIVEL_ERRORES).toString())) {
								contexto.logTraducido(LOG.AVISO, "refundido.operacion.entidad.error.geometriasDiferentes", operadaPlaneamiento.getCodigo(),
									operadaPlaneamiento.getTramite().getPlan().getCodigo(), 
									operadoraPlaneamiento.getCodigo(),
									operadoraPlaneamiento.getTramite().getPlan().getCodigo());
								return;
							} else {
								throw new ExcepcionRefundido(String.format(traductor.getString("refundido.operacion.entidad.error.geometriasDiferentes"), 
									operadaPlaneamiento.getCodigo(),
									operadaPlaneamiento.getTramite().getPlan().getCodigo(), 
									operadoraPlaneamiento.getCodigo(),
									operadoraPlaneamiento.getTramite().getPlan().getCodigo())
									, 7038);
							}
						}
						registro = operada.getEntidadpols().toArray(new Entidadlin[0])[0];
						geometriaOperada = reader.read(((Entidadlin)registro).getGeom());
					} else if (operada.getEntidadpnts().size() > 0) {
						if (operadora.getEntidadpnts().size() > 0) {
							geometriaOperadora = reader.read(operadora.getEntidadpnts().toArray(new Entidadpnt[0])[0].getGeom());
						} else {
							if (contexto.getParametro(ContextoRefundido.NIVEL_ERRORES) != null && SOLO_AVISOS.equalsIgnoreCase(contexto.getParametro(ContextoRefundido.NIVEL_ERRORES).toString())) {
								contexto.logTraducido(LOG.AVISO, "refundido.operacion.entidad.error.geometriasDiferentes", operadaPlaneamiento.getCodigo(),
									operadaPlaneamiento.getTramite().getPlan().getCodigo(), 
									operadoraPlaneamiento.getCodigo(),
									operadoraPlaneamiento.getTramite().getPlan().getCodigo());
								return;
							} else {
								throw new ExcepcionRefundido(String.format(traductor.getString("refundido.operacion.entidad.error.geometriasDiferentes"), 
									operadaPlaneamiento.getCodigo(),
									operadaPlaneamiento.getTramite().getPlan().getCodigo(), 
									operadoraPlaneamiento.getCodigo(),
									operadoraPlaneamiento.getTramite().getPlan().getCodigo())
									, 7040);
							}
						}
						registro = operada.getEntidadpols().toArray(new Entidadpnt[0])[0];
						geometriaOperada = reader.read(((Entidadpnt)registro).getGeom());
					} else {
						throw new ExcepcionRefundido(String.format(traductor.getString("refundido.operacion.entidad.error.sinGeometria"), 
								operadaPlaneamiento.getCodigo(),
								operadaPlaneamiento.getTramite().getPlan().getCodigo())
								, 7042);
					}
					
					Geometry resultado = intersectar(geometriaOperada, geometriaOperadora, contexto);
					
					copiarEntidad(operada, operadorEntidades.obtenerCarpetaEntidadesAportadas(operadoraPlaneamiento.getTramite().getPlan(), contexto, true), resultado, contexto);
					
					geometriaOperada = aplicarSustraccion(geometriaOperada, geometriaOperadora, contexto);
					
					if (geometriaOperada != null) {
						if (em.createNativeQuery(String.format(UPDATE_GEOM, registro.getClass().getSimpleName()))
								.setParameter("geom", geometriaOperada.toText())
								.setParameter("iden", (int)registro.getClass().getDeclaredMethod("getIden").invoke(registro)).executeUpdate() != 1) {
							throw new ExcepcionRefundido(traductor.getString("refundido.operacion.entidad.error.guardarGeometria"), 7037);
						}
					} else {
						contexto.logTraducido(LOG.AVISO, "refundido.operacion.entidad.aviso.sinGeometria", operadaPlaneamiento.getCodigo(), operadaPlaneamiento.getTramite().getPlan().getCodigo());
						eliminacion.ejecutar(idOperada, idOperadora, contexto);
					}
					em.flush();
				} catch (ParseException pe) {
					throw new ExcepcionRefundido(String.format(traductor.getString("refundido.operacion.entidad.error.procesarGeometrias"), 
							operadaPlaneamiento.getCodigo(),
							operadaPlaneamiento.getTramite().getPlan().getCodigo(), 
							operadora.getCodigo(),
							operadora.getTramite().getPlan().getCodigo())
							, 7043);
				} catch (IllegalAccessException | IllegalArgumentException
						| InvocationTargetException | NoSuchMethodException
						| SecurityException e) {
					throw new ExcepcionRefundido(String.format(traductor.getString("refundido.excepcion.invocacion"), 
							e.getMessage())
							, 7058);
				}
			} else {
				contexto.logTraducido(LOG.AVISO,"refundido.operacion.entidad.aviso.operadoraSuspendida");
			}
		} else {
			if (operada == null) {
    			contexto.logTraducido(LOG.AVISO,"refundido.operacion.entidad.aviso.noOperada");
    		}
    		
    		if (operadora == null) {
    			contexto.logTraducido(LOG.AVISO, "refundido.operacion.entidad.aviso.noOperadora");
    		}
		}
	}
	
	/**
	 * 
	 * @param geometriaOperada
	 * @param geometriaOperadora
	 * @param contexto
	 * @return
	 * @throws ExcepcionRefundido 
	 */
	private Geometry intersectar(Geometry geometriaOperada,
			Geometry geometriaOperadora, ContextoRefundido contexto) throws ExcepcionRefundido {
		boolean fallo=true;
        double tol=0.000001;
        Geometry geomResult=null;
        int nIntentos=0;
        
        while (fallo) {
            try{
            	if (geometriaOperada.intersects(geometriaOperadora)) {
            		geomResult = geometriaOperada.intersection(geometriaOperadora);
            		geomResult = limpiarGeometria(geometriaOperada.getGeometryType(), geomResult);
            	} else {
            		ResourceBundle traductor = (ResourceBundle)contexto.getParametro(ContextoRefundido.TRADUCTOR);
                	throw new ExcepcionRefundido(String.format(traductor.getString("refundido.operacion.entidad.suspensionParcial.error.noInterseccion")), 7044); 
            	}
            	
                fallo=false;
            } catch (TopologyException te){
                // La operación da error y hay que repetirla con tolerancia.
                nIntentos++;
                
                geometriaOperada=geometriaOperada.buffer(tol);
                geometriaOperadora=geometriaOperadora.buffer(tol);
                tol=tol*2;
                
                if (nIntentos>=10){      	
                	ResourceBundle traductor = (ResourceBundle)contexto.getParametro(ContextoRefundido.TRADUCTOR);
                	throw new ExcepcionRefundido(String.format(traductor.getString("refundido.operacion.entidad.error.operarGeometrias"),te.getLocalizedMessage()), 7045);
                }
            } catch (IllegalArgumentException iae) {
            	ResourceBundle traductor = (ResourceBundle)contexto.getParametro(ContextoRefundido.TRADUCTOR);
            	throw new ExcepcionRefundido(String.format(traductor.getString("refundido.operacion.entidad.error.operarGeometrias.geometriaInvalida"),iae.getLocalizedMessage()), 7046);
            }
        }
        
        return geomResult;
	}
	
	/**
     * 
     * @param tipoGeom
     * @param aGeom
     * @return
     * @throws ExcepcionRefundido 
     */
    private Geometry limpiarGeometria(String tipoGeom, Geometry aGeom) {
        Geometry iGeom;
        Geometry iGeomR=null;
        String sTipo="";
        
        if (aGeom !=null && aGeom.getGeometryType().toUpperCase().contains("COLLECTION")){
        	int numGeom= aGeom.getNumGeometries();

            for (int i=0;i<numGeom;i++){
                iGeom=aGeom.getGeometryN(i);
                if (iGeom.getGeometryType().contains(sTipo)){
                    if (iGeomR==null){
                        iGeomR=iGeom;
                    } else {
                    	iGeomR= iGeomR.union(iGeom);
                    }
                }
            }
            return iGeomR;
        } else {
        	return aGeom;
        }
    }
}

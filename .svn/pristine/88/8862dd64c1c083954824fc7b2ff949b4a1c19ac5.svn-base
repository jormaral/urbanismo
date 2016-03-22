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

import java.util.ResourceBundle;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.TopologyException;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;

import es.mitc.redes.urbanismoenred.data.rpm.refundido.Entidad;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Entidadlin;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Entidadpnt;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Entidadpol;
import es.mitc.redes.urbanismoenred.servicios.refundido.ContextoRefundido;
import es.mitc.redes.urbanismoenred.servicios.refundido.ContextoRefundido.LOG;
import es.mitc.redes.urbanismoenred.servicios.refundido.ExcepcionRefundido;
import es.mitc.redes.urbanismoenred.servicios.refundido.GestorAportacionesLocal;

/**
 * 
 * @author Arnaiz Consultores
 *
 */
@Stateless ( name = "refundido.operacion.entidades.sustraccionGrafica")
public class SustraccionGrafica implements EjecutorLocal {
	
	private static final String UPDATE_GEOM = "UPDATE refundido.%s SET geom=multi(geometryfromtext(:geom)) where iden = :iden";
	private static final String SOLO_AVISOS = "Avisos";
	
	@PersistenceContext(unitName = "rpmv2")
	private EntityManager em;
	
	@EJB ( beanName = "refundido.operacion.entidades.eliminacion")
	private EjecutorLocal eliminacion;
	
	@EJB
	private GestorAportacionesLocal gestorAportaciones;

	/* (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.servicios.refundido.operaciones.entidades.EjecutorLocal#ejecutar(int, int, es.mitc.redes.urbanismoenred.servicios.refundido.ContextoRefundido)
	 */
	@Override
	public void ejecutar(int idOperada, int idOperadora,
			ContextoRefundido contexto) throws ExcepcionRefundido {
		es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidad operadoraPlaneamiento = em.find(es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidad.class, idOperadora);
		es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidad operadaPlaneamiento = em.find(es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidad.class, idOperada);
		ResourceBundle traductor = (ResourceBundle) contexto.getParametro(ContextoRefundido.TRADUCTOR);
		
		contexto.logTraducido(LOG.INFO, "refundido.operacion.entidad.sustraccionGrafica.mensaje", 
				operadoraPlaneamiento.getCodigo(), 
				operadoraPlaneamiento.getTramite().getPlan().getCodigo(), 
				operadaPlaneamiento.getCodigo(),
				operadaPlaneamiento.getTramite().getPlan().getCodigo());
		
		Entidad operada = gestorAportaciones.aportar(operadaPlaneamiento, null, null, contexto);
		Entidad operadora = gestorAportaciones.aportar(operadoraPlaneamiento, null, null, contexto);
		
		if (operada != null && operadora != null) {
			if (!operadora.isBsuspendida()) {
				Geometry geometriaOperadora = null;
				Geometry geometriaOperada = null;
				Geometry resultado = null;
				WKTReader reader = new WKTReader();
				try {
					if (operadora.getEntidadpols().size() > 0) {
						geometriaOperadora = reader.read(operadora.getEntidadpols().toArray(new Entidadpol[0])[0].getGeom());
						
						if (operada.getEntidadpols().size() > 0) {
							Entidadpol ep = operada.getEntidadpols().toArray(new Entidadpol[0])[0];
							geometriaOperada = reader.read(ep.getGeom());
							
							resultado = aplicarSustraccion(geometriaOperada, geometriaOperadora, contexto);
							
							if (resultado != null) {
								if (em.createNativeQuery(String.format(UPDATE_GEOM, "Entidadpol"))
										.setParameter("geom", resultado.toText())
										.setParameter("iden", ep.getIden())
										.executeUpdate() != 1) {
									throw new ExcepcionRefundido(traductor.getString("refundido.operacion.entidad.error.guardarGeometria"), 7047);
								}
								em.flush();
								em.refresh(ep);
							} else {
								contexto.logTraducido(LOG.AVISO, "refundido.operacion.entidad.aviso.sinGeometria", operadaPlaneamiento.getCodigo(), operadaPlaneamiento.getTramite().getPlan().getCodigo());
								eliminacion.ejecutar(idOperada, idOperadora, contexto);
							}
							
						} else if (operada.getEntidadlins().size() > 0) {
							Entidadlin el = operada.getEntidadlins().toArray(new Entidadlin[0])[0];
							geometriaOperada = reader.read(el.getGeom());
							
							resultado = aplicarSustraccion(geometriaOperada, geometriaOperadora, contexto);
							
							if (resultado != null) {
								if (em.createNativeQuery(String.format(UPDATE_GEOM, "Entidadlin"))
										.setParameter("geom", resultado.toText())
										.setParameter("iden", el.getIden())
										.executeUpdate() != 1) {
									throw new ExcepcionRefundido(traductor.getString("refundido.operacion.entidad.error.guardarGeometria"), 7048);
								}
								em.flush();
								em.refresh(el);
							} else {
								contexto.logTraducido(LOG.AVISO, "refundido.operacion.entidad.aviso.sinGeometria", operadaPlaneamiento.getCodigo(), operadaPlaneamiento.getTramite().getPlan().getCodigo());
								eliminacion.ejecutar(idOperada, idOperadora, contexto);
							}
							
						} else if (operada.getEntidadpnts().size() > 0) {
							Entidadpnt ep = operada.getEntidadpnts().toArray(new Entidadpnt[0])[0];
							geometriaOperada = reader.read(ep.getGeom());
							
							resultado = aplicarSustraccion(geometriaOperada, geometriaOperadora, contexto);
							
							if (resultado != null) {
								if (em.createNativeQuery(String.format(UPDATE_GEOM, "Entidadpnt"))
										.setParameter("geom", resultado.toText())
										.setParameter("iden", ep.getIden())
										.executeUpdate() != 1) {
									throw new ExcepcionRefundido(traductor.getString("refundido.operacion.entidad.error.guardarGeometria"), 7049);
								}
								em.flush();
								em.refresh(ep);
							} else {
								contexto.logTraducido(LOG.AVISO, "refundido.operacion.entidad.aviso.sinGeometria", operadaPlaneamiento.getCodigo(), operadaPlaneamiento.getTramite().getPlan().getCodigo());
								eliminacion.ejecutar(idOperada, idOperadora, contexto);
							}
							
						} else {
							contexto.logTraducido(LOG.AVISO, "refundido.operacion.entidad.error.sinGeometria", 
									operadaPlaneamiento.getCodigo(),
									operadaPlaneamiento.getTramite().getPlan().getCodigo());
						}
					} else {	
						contexto.logTraducido(LOG.AVISO, "refundido.operacion.entidad.sustraccionGrafica.error.noPoligono", 
							operadaPlaneamiento.getCodigo(),
							operadaPlaneamiento.getTramite().getPlan().getCodigo());
					}
					
				} catch (ParseException pe) {
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
                	throw new ExcepcionRefundido(String.format(traductor.getString("refundido.operacion.entidad.error.operarGeometrias"),te.getLocalizedMessage()), 7053);
                }
            } catch (IllegalArgumentException iae) {
            	ResourceBundle traductor = (ResourceBundle)contexto.getParametro(ContextoRefundido.TRADUCTOR);
            	throw new ExcepcionRefundido(String.format(traductor.getString("refundido.operacion.entidad.error.operarGeometrias.geometriaInvalida"),iae.getLocalizedMessage()), 7054);
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

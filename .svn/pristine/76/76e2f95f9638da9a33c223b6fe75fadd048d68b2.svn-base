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
 * @author Arnaiz Consultores
 *
 */
@Stateless(name = "refundido.operacion.entidades.adicionGrafica")
public class AdicionGrafica implements EjecutorLocal {
	
	private static final String SOLO_AVISOS = "Avisos";
	
	private static final String UPDATE_GEOM = "UPDATE refundido.%s SET geom=multi(geometryfromtext(:geom)) where iden = :iden";
	
	@PersistenceContext(unitName = "rpmv2")
	private EntityManager em;
	
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
		
		contexto.logTraducido(LOG.INFO, "refundido.operacion.entidad.adicionGrafica.mensaje", 
				operadoraPlaneamiento.getCodigo(), 
				operadoraPlaneamiento.getTramite().getPlan().getCodigo(), 
				operadaPlaneamiento.getCodigo(),
				operadaPlaneamiento.getTramite().getPlan().getCodigo());
		
		Entidad operada = gestorAportaciones.aportar(operadaPlaneamiento, null, null, contexto);
		Entidad operadora = gestorAportaciones.aportar(operadoraPlaneamiento, null, null, contexto);
		
		ResourceBundle traductor = (ResourceBundle)contexto.getParametro(ContextoRefundido.TRADUCTOR);
		
		if (operada != null && operadora != null) {
			
			if (!operadora.isBsuspendida()) {
				Geometry geometriaOperadora;
				Geometry geometriaOperada;
				WKTReader reader = new WKTReader();
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
									, 7001);
							}
						}
						
						Entidadpol ep = operada.getEntidadpols().toArray(new Entidadpol[0])[0];
						geometriaOperada = reader.read(ep.getGeom());
						
						Geometry resultado = operarGeometrias(geometriaOperada, geometriaOperadora, contexto);
						
						if (em.createNativeQuery(String.format(UPDATE_GEOM, "entidadpol"))
								.setParameter("geom", resultado.toText())
								.setParameter("iden", ep.getIden()).executeUpdate() != 1) {
							throw new ExcepcionRefundido(String.format(traductor.getString("refundido.operacion.entidad.error.actualizarGeometria"), 
									operadaPlaneamiento.getCodigo(),
									operadaPlaneamiento.getTramite().getPlan().getCodigo(), 
									operadoraPlaneamiento.getCodigo(),
									operadoraPlaneamiento.getTramite().getPlan().getCodigo())
									, 7002);
			            }
						// Si no hago este flush y luego el refresh los cambios 
						// no quedan consolidados.
						em.flush();
						em.refresh(ep);
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
									, 7003);
							}
						}
						Entidadlin el = operada.getEntidadlins().toArray(new Entidadlin[0])[0];
						geometriaOperada = reader.read(el.getGeom());
						
						Geometry resultado = operarGeometrias(geometriaOperada, geometriaOperadora, contexto);
						
						if (em.createNativeQuery(String.format(UPDATE_GEOM, "entidadlin"))
								.setParameter("geom", resultado.toText())
								.setParameter("iden", el.getIden()).executeUpdate() != 1) {
							throw new ExcepcionRefundido(String.format(traductor.getString("refundido.operacion.entidad.error.actualizarGeometria"), 
									operadaPlaneamiento.getCodigo(),
									operadaPlaneamiento.getTramite().getPlan().getCodigo(), 
									operadoraPlaneamiento.getCodigo(),
									operadoraPlaneamiento.getTramite().getPlan().getCodigo())
									, 7004);
			            }
						em.flush();
						em.refresh(el);
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
									, 7005);
							}
						}
						
						Entidadpnt ep = operada.getEntidadpnts().toArray(new Entidadpnt[0])[0];
						geometriaOperada = reader.read(ep.getGeom());
						
						Geometry resultado = operarGeometrias(geometriaOperada, geometriaOperadora, contexto);
						
						if (em.createNativeQuery(String.format(UPDATE_GEOM, "entidadpnt"))
								.setParameter("geom", resultado.toText())
								.setParameter("iden", ep.getIden()).executeUpdate() != 1) {
							throw new ExcepcionRefundido(String.format(traductor.getString("refundido.operacion.entidad.error.actualizarGeometria"), 
									operadaPlaneamiento.getCodigo(),
									operadaPlaneamiento.getTramite().getPlan().getCodigo(), 
									operadoraPlaneamiento.getCodigo(),
									operadoraPlaneamiento.getTramite().getPlan().getCodigo())
									, 7006);
			            }
						em.flush();
						em.refresh(ep);
					} else {
						if (contexto.getParametro(ContextoRefundido.NIVEL_ERRORES) != null && SOLO_AVISOS.equalsIgnoreCase(contexto.getParametro(ContextoRefundido.NIVEL_ERRORES).toString())) {
							contexto.logTraducido(LOG.AVISO, "refundido.operacion.entidad.error.sinGeometria", operadaPlaneamiento.getCodigo(),
									operadaPlaneamiento.getTramite().getPlan().getCodigo());
							return;
						} else {
							throw new ExcepcionRefundido(String.format(traductor.getString("refundido.operacion.entidad.error.sinGeometria"), 
								operadaPlaneamiento.getCodigo(),
								operadaPlaneamiento.getTramite().getPlan().getCodigo())
								, 7007);
						}
					}
				} catch (ParseException pe) {
					if (contexto.getParametro(ContextoRefundido.NIVEL_ERRORES) != null && SOLO_AVISOS.equalsIgnoreCase(contexto.getParametro(ContextoRefundido.NIVEL_ERRORES).toString())) {
						contexto.logTraducido(LOG.AVISO, "refundido.operacion.entidad.error.procesarGeometrias", 
							operadaPlaneamiento.getCodigo(),
							operadaPlaneamiento.getTramite().getPlan().getCodigo(), 
							operadoraPlaneamiento.getCodigo(),
							operadoraPlaneamiento.getTramite().getPlan().getCodigo());
						return;
					} else {
						throw new ExcepcionRefundido(String.format(traductor.getString("refundido.operacion.entidad.error.procesarGeometrias"), 
							operadaPlaneamiento.getCodigo(),
							operadaPlaneamiento.getTramite().getPlan().getCodigo(), 
							operadoraPlaneamiento.getCodigo(),
							operadoraPlaneamiento.getTramite().getPlan().getCodigo())
							, 7008);
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
     * @param operada
     * @param operadora
     * @param tipoOperacion
     * @return
	 * @throws ExcepcionRefundido 
     */
    private Geometry operarGeometrias(Geometry operada, Geometry operadora, ContextoRefundido contexto) throws ExcepcionRefundido{
        boolean fallo=true;
        double tol=0.000001;
        Geometry geomResult=null;
        int nIntentos=0;
        
        while (fallo) {
            try{
            	geomResult=operada.union(operadora);
                fallo=false;
            } catch (TopologyException te){
                // La operación da error y hay que repetirla con tolerancia.
                nIntentos++;
                
                operada=operada.buffer(tol);
                operadora=operadora.buffer(tol);
                tol=tol*2;
                
                if (nIntentos>=10){      	
                	ResourceBundle traductor = (ResourceBundle)contexto.getParametro(ContextoRefundido.TRADUCTOR);
                	throw new ExcepcionRefundido(String.format(traductor.getString("refundido.operacion.entidad.error.operarGeometrias"),te.getLocalizedMessage()), 7009);
                }
            } catch (IllegalArgumentException iae) {
            	ResourceBundle traductor = (ResourceBundle)contexto.getParametro(ContextoRefundido.TRADUCTOR);
            	throw new ExcepcionRefundido(String.format(traductor.getString("refundido.operacion.entidad.error.operarGeometrias.geometriaInvalida"),iae.getLocalizedMessage()), 7010);
            }
        }
        
        return geomResult;    
    }

}

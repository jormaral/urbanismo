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

import es.mitc.redes.urbanismoenred.data.rpm.refundido.Entidad;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Entidadlin;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Entidadpnt;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Entidadpol;
import es.mitc.redes.urbanismoenred.servicios.refundido.ContextoRefundido;
import es.mitc.redes.urbanismoenred.servicios.refundido.ContextoRefundido.LOG;
import es.mitc.redes.urbanismoenred.servicios.refundido.ExcepcionRefundido;
import es.mitc.redes.urbanismoenred.servicios.refundido.GestorAportacionesLocal;

/**
 * Se copian los registros gráficos de la operadora a la operada. Primero se
 * comprueba que la operada no tenga previamente ninguna geometría, y
 * que la operadora tenga alguna.
 * 
 * 
 * @author Arnaiz Consultores
 *
 */
@Stateless( name = "refundido.operacion.entidades.creacionGrafica")
public class CreacionGrafica implements EjecutorLocal {
	
	private static final String SQL_UPDATE = "update refundido.%s set geom=multi(geometryfromtext(:geom)) where iden=:iden";

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
		
		contexto.logTraducido(LOG.INFO, "refundido.operacion.entidad.creacionGrafica.mensaje", 
				operadoraPlaneamiento.getCodigo(), 
				operadoraPlaneamiento.getTramite().getPlan().getCodigo(), 
				operadaPlaneamiento.getCodigo(),
				operadaPlaneamiento.getTramite().getPlan().getCodigo());
		
		Entidad operada = gestorAportaciones.aportar(operadaPlaneamiento, null, null, contexto);
		Entidad operadora = gestorAportaciones.aportar(operadoraPlaneamiento, null, null, contexto);
		
		if (operada != null && operadora != null) {
			if (!operadora.isBsuspendida()) {
				em.refresh(operada);
				if (operada.getEntidadpols().size() == 0 && 
						operada.getEntidadlins().size() == 0 && 
						operada.getEntidadpnts().size() == 0) {
					em.refresh(operadora);
					
					String geometria;
					Object registro;
					if (operadora.getEntidadpols().size() > 0) {
						Entidadpol poligono = new Entidadpol();
			            poligono.setEntidad(operada);
			            geometria = operadora.getEntidadpols().toArray(new Entidadpol[0])[0].getGeom();
			            poligono.setGeom(geometria);
			            em.persist(poligono);
			            em.flush();
			            
			            registro = poligono;
					} else if (operadora.getEntidadlins().size() > 0) {
						Entidadlin poligono = new Entidadlin();
			            poligono.setEntidad(operada);
			            geometria = operadora.getEntidadlins().toArray(new Entidadlin[0])[0].getGeom();
			            poligono.setGeom(geometria);
			            em.persist(poligono);
			            em.flush();
			            
			            registro = poligono;
					} else if (operadora.getEntidadpnts().size() > 0) {
						Entidadpnt poligono = new Entidadpnt();
			            poligono.setEntidad(operada);
			            geometria = operadora.getEntidadpnts().toArray(new Entidadpnt[0])[0].getGeom();
			            poligono.setGeom(geometria);
			            em.persist(poligono);
			            em.flush();
			            
			            registro = poligono;
					} else {
						ResourceBundle traductor = (ResourceBundle)contexto.getParametro(ContextoRefundido.TRADUCTOR);
						throw new ExcepcionRefundido(String.format(traductor.getString("refundido.operacion.entidad.creacionGrafica.error.operadoraSinGeometria"), operadora.getCodigo(),operadora.getTramite().getPlan().getCodigo()), 7013);
					}
					
					try {
						if (em.createNativeQuery(String.format(SQL_UPDATE, registro.getClass().getSimpleName()))
								.setParameter("geom", geometria)
								.setParameter("iden", (int)registro.getClass().getDeclaredMethod("getIden").invoke(registro))
								.executeUpdate() != 1) {
							ResourceBundle traductor = (ResourceBundle)contexto.getParametro(ContextoRefundido.TRADUCTOR);
							es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidad entidad = em.find(es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidad.class, idOperada);
							throw new ExcepcionRefundido(String.format(traductor.getString("refundido.operacion.entidad.creacionGrafica.error.guardar"), entidad.getCodigo(), entidad.getTramite().getPlan().getCodigo()), 7014);
						}
						
						em.flush();
			            em.refresh(registro);
					} catch (IllegalAccessException | IllegalArgumentException
							| InvocationTargetException | NoSuchMethodException
							| SecurityException e) {
						ResourceBundle traductor = (ResourceBundle)contexto.getParametro(ContextoRefundido.TRADUCTOR);
						throw new ExcepcionRefundido(String.format(traductor.getString("refundido.excepcion.invocacion"), 
								e.getMessage())
								, 7056);
					}
				} else {
					contexto.logTraducido(LOG.AVISO, "refundido.operacion.entidad.creacionGrafica.aviso.operadaConGeometria", operada.getCodigo());
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

}

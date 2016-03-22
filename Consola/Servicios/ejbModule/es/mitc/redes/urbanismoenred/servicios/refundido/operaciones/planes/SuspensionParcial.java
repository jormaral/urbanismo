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
package es.mitc.redes.urbanismoenred.servicios.refundido.operaciones.planes;

import java.util.ResourceBundle;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import es.mitc.redes.urbanismoenred.data.rpm.refundido.Casoentidaddeterminacion;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Determinacion;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Entidad;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Entidaddeterminacion;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Entidaddeterminacionregimen;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Entidadpol;
import es.mitc.redes.urbanismoenred.data.rpm.refundido.Opciondeterminacion;
import es.mitc.redes.urbanismoenred.servicios.refundido.ContextoRefundido;
import es.mitc.redes.urbanismoenred.servicios.refundido.ContextoRefundido.LOG;
import es.mitc.redes.urbanismoenred.servicios.refundido.ExcepcionRefundido;
import es.mitc.redes.urbanismoenred.servicios.refundido.OperadorDeterminacionesRefundidoLocal;
import es.mitc.redes.urbanismoenred.servicios.refundido.OperadorEntidadesRefundidoLocal;

/**
 * @author Arnaiz Consultores
 *
 */
@Stateless (name = "refundido.operacion.planes.suspensionParcial")
public class SuspensionParcial implements EjecutorLocal {
	
	private static final String UPDATE_GEOM = "UPDATE refundido.entidadpol SET geom=multi(geometryfromtext(:geom)) where iden = :iden";
	
	@PersistenceContext(unitName = "rpmv2")
	private EntityManager em;
	
	@EJB
	private OperadorDeterminacionesRefundidoLocal operadorDeterminaciones;
	@EJB
	private OperadorEntidadesRefundidoLocal operadorEntidades;

	/* (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.servicios.refundido.operaciones.planes.EjecutorLocal#ejecutar(int, int, es.mitc.redes.urbanismoenred.servicios.refundido.ContextoRefundido)
	 */
	@Override
	public void ejecutar(int idOperado, int idOperador,
			ContextoRefundido contexto) throws ExcepcionRefundido {
		es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Plan planOperador = em.find(es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Plan.class, idOperador);
		es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Plan planOperado = em.find(es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Plan.class, idOperado);
		
		contexto.logTraducido(LOG.INFO, "refundido.operacion.plan.suspensionParcial.mensaje", planOperado.getCodigo(), planOperador.getCodigo());
		
		Determinacion afeccion = operadorDeterminaciones.obtenerAfeccion(contexto);
		
		ResourceBundle traductor = (ResourceBundle) contexto.getParametro(ContextoRefundido.TRADUCTOR);
		if (afeccion != null) {
			Entidad entidadAfeccion = new Entidad();
			entidadAfeccion.setBsuspendida(false);
			entidadAfeccion.setClave(traductor.getString("refundido.entidad.afeccion.clave"));
			entidadAfeccion.setCodigo(operadorEntidades.getSiguienteCodigo(contexto));
			entidadAfeccion.setEtiqueta("");
			entidadAfeccion.setNombre(String.format(traductor.getString("refundido.entidad.afeccion.nombre"), planOperador.getNombre()));
			
			
			Entidad aportadasOperado = operadorEntidades.obtenerCarpetaEntidadesAportadas(planOperado, contexto, true);
			
			int orden = 0;
			
			for (Entidad hija : aportadasOperado.getEntidadsForIdpadre()) {
				if (hija.getOrden() > orden) {
					orden = hija.getOrden();
				}
			}
			entidadAfeccion.setOrden(++orden);
			
			entidadAfeccion.setTramite(aportadasOperado.getTramite());
			
			entidadAfeccion.setEntidadByIdpadre(aportadasOperado);
			
			em.persist(entidadAfeccion);
			
			Entidaddeterminacion ed = new Entidaddeterminacion();
			ed.setDeterminacion(afeccion.getDeterminacionByIdpadre());
			ed.setEntidad(entidadAfeccion);
			
			em.persist(ed);
			
			Casoentidaddeterminacion ced = new Casoentidaddeterminacion();
			ced.setEntidaddeterminacion(ed);
			ced.setNombre(traductor.getString("refundido.caso.afeccion.nombre"));
			ced.setOrden(0);
			
			em.persist(ced);
			
			Opciondeterminacion od = null;
			
			for (Opciondeterminacion opcion : afeccion.getOpciondeterminacionsForIddeterminacionvalorref()) {
				if (opcion.getDeterminacionByIddeterminacion() == afeccion.getDeterminacionByIdpadre()) {
					od = opcion;
				}
			}
			
			if (od != null) {
				Entidaddeterminacionregimen edr = new Entidaddeterminacionregimen();
				edr.setCasoentidaddeterminacionByIdcaso(ced);
				edr.setOpciondeterminacion(od);
				
				em.persist(edr);
				
				em.flush();
				
				// Una vez creada toda la estructura de datos le asignamos a 
				// la nueva entidad la geometría del ámbito de aplicación
				// del plan operador.
				Entidad ambito = operadorEntidades.obtenerAmbitoAplicacion(planOperador, (int)contexto.getParametro(ContextoRefundido.ID_PROCESO));
				
				if (ambito != null) {
					if (!ambito.getEntidadpols().isEmpty()) {
						Entidadpol poligono = new Entidadpol();
						poligono.setEntidad(entidadAfeccion);
						em.persist(poligono);
						em.flush();
						
						if (em.createNativeQuery(UPDATE_GEOM)
								.setParameter("geom", ambito.getEntidadpols().toArray(new Entidadpol[0])[0].getGeom())
								.setParameter("iden", poligono.getIden())
								.executeUpdate() != 1) {
							contexto.logTraducido(LOG.AVISO, "refundido.operacion.plan.suspensionParcial.aviso.geometria");
						}
						em.flush();
						em.refresh(poligono);
					} else {
						throw new ExcepcionRefundido(traductor.getString("refundido.operacion.plan.suspensionParcial.error.poligono"), 8014);
					}
				} else {
					throw new ExcepcionRefundido(traductor.getString("refundido.operacion.plan.suspensionParcial.error.ambito"), 8013);
				}
			} else {
				throw new ExcepcionRefundido(traductor.getString("refundido.operacion.plan.suspensionParcial.error.opcion"), 8012);
			}
		} else {
			throw new ExcepcionRefundido(traductor.getString("refundido.operacion.plan.suspensionParcial.error.afeccion"), 8011);
		}

	}

}

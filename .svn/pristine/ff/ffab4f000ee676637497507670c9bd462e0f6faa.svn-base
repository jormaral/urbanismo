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
package es.mitc.redes.urbanismoenred.servicios.planeamiento;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import es.mitc.redes.urbanismoenred.data.rpm.diccionario.Ambito;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Determinacion;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidad;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Plan;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Tramite;
import es.mitc.redes.urbanismoenred.utils.recursos.textos.Textos;

/**
 * Session Bean implementation class ServicioBusquedaBean
 * 
 * @author Arnaiz Consultores
 */
@Stateless(name = "ServicioBusquedaPlaneamiento")
public class ServicioBusquedaBean implements ServicioBusquedaLocal {
	
	private static final String BUSQUEDA_DETERMINACIONES = "SELECT det FROM es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Determinacion det WHERE ";
	private static final String BUSQUEDA_ENTIDADES = "SELECT ent FROM es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidad ent WHERE ";
	private static final String BUSQUEDA_PLANES = "SELECT DISTINCT tra.plan FROM es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Tramite tra WHERE ";
	
	@PersistenceContext(unitName = "rpmv2")
    private EntityManager em;

    /**
     * Default constructor. 
     */
    public ServicioBusquedaBean() {
    }

    /*
     * (non-Javadoc)
     * @see es.mitc.redes.urbanismoenred.servicios.planeamiento.ServicioBusquedaLocal#buscarDeterminaciones(java.lang.Integer, java.lang.Class, java.lang.String, java.lang.String, java.lang.String, es.mitc.redes.urbanismoenred.servicios.planeamiento.ModalidadPlanes)
     */
	@SuppressWarnings("unchecked")
	@Override
	public Determinacion[] buscarDeterminaciones(Integer raiz,
			Class<?> tipoRaiz, String nombre, String codigo, String apartado, ModalidadPlanes modalidad) {
		
		// Primero creo la consulta
		StringBuffer consulta = new StringBuffer(BUSQUEDA_DETERMINACIONES);
		boolean condicion = false;
		if (nombre != null && !nombre.trim().isEmpty()) {
			consulta.append(" det.nombre LIKE :nombre ");
			condicion = true;
		}
		if (codigo != null && !codigo.trim().isEmpty()) {
			if (condicion)
				consulta.append(" AND ");
			else 
				condicion = true;
			consulta.append(" det.codigo LIKE :codigo ");
		}
		if (apartado != null && !apartado.trim().isEmpty()) {
			if (condicion)
				consulta.append(" AND ");
			else 
				condicion = true;
			consulta.append(" det.apartado LIKE :apartado ");
		}
		
		switch (modalidad) {
		case FICHAS:
			break;
		case REF:
			break;
		case RPM:
			if (condicion)
				consulta.append(" AND ");
			else 
				condicion = true;
			consulta.append(" det.tramite.fechaconsolidacion IS NOT NULL");
			break;
		case TODOS:
			break;
		case VALIDACION:
			if (condicion)
				consulta.append(" AND ");
			else 
				condicion = true;
			consulta.append(" det.tramite.fechaconsolidacion IS NULL");
			break;
		}
		
		// Una vez tengo la consulta definida creo el objeto query
		Query query = em.createQuery(consulta.toString());
		
		// Asigno los parámetros
		if (nombre != null && !nombre.trim().isEmpty()) {
			query.setParameter("nombre", "%"+nombre+"%");
		}
		if (codigo != null && !codigo.trim().isEmpty()) {
			query.setParameter("codigo", "%"+codigo+"%");
		}
		if (apartado != null && !apartado.trim().isEmpty()) {
			query.setParameter("apartado", "%"+apartado+"%");
		}
		
		List<Determinacion> determinaciones = query.getResultList();
		
		// Ahora compruebo si pertenecen a la raíz especificada,
		// Como puede implicar un proceso muy recursivo existen dos mecanismos
		// la utilización de procesos almacenados o mediante código
		
		if (raiz != null && tipoRaiz != null) {
			Object objetoRaiz = em.find(tipoRaiz, raiz);
			if (objetoRaiz != null) {
				if (objetoRaiz instanceof Plan) {
					return filtrarPorPlan(determinaciones,(Plan)objetoRaiz);
				} else if (objetoRaiz instanceof Ambito) {
					return filtrarPorAmbito(determinaciones,(Ambito)objetoRaiz);
				} else if (objetoRaiz instanceof Determinacion) {
					return filtrarPorDeterminacion(determinaciones, (Determinacion)objetoRaiz);
				} else if (objetoRaiz instanceof Tramite) {
					return filtrarPorTramite(determinaciones, (Tramite)objetoRaiz);
				}
			}
		} 
		
		return determinaciones.toArray(new Determinacion[0]);
	}

	/*
	 * (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.servicios.planeamiento.ServicioBusquedaLocal#buscarEntidades(java.lang.Integer, java.lang.Class, java.lang.String, java.lang.String, java.lang.String, es.mitc.redes.urbanismoenred.servicios.planeamiento.ModalidadPlanes)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Entidad[] buscarEntidades(Integer raiz, Class<?> tipoRaiz,
			String nombre, String codigo, String clave, ModalidadPlanes modalidad) {
		// Primero creo la consulta
		StringBuffer consulta = new StringBuffer(BUSQUEDA_ENTIDADES);
		boolean condicion = false;
		if (nombre != null && !nombre.trim().isEmpty()) {
			consulta.append(" ent.nombre LIKE :nombre ");
			condicion = true;
		}
		if (codigo != null && !codigo.trim().isEmpty()) {
			if (condicion)
				consulta.append(" AND ");
			else 
				condicion = true;
			consulta.append(" ent.codigo LIKE :codigo ");
		}
		if (clave != null && !clave.trim().isEmpty()) {
			if (condicion)
				consulta.append(" AND ");
			else
				condicion = true;
			consulta.append(" ent.clave LIKE :clave ");
		}
		
		switch (modalidad) {
		case FICHAS:
			break;
		case REF:
			break;
		case RPM:
			if (condicion)
				consulta.append(" AND ");
			else 
				condicion = true;
			consulta.append(" ent.tramite.fechaconsolidacion IS NOT NULL");
			break;
		case TODOS:
			break;
		case VALIDACION:
			if (condicion)
				consulta.append(" AND ");
			else 
				condicion = true;
			consulta.append(" ent.tramite.fechaconsolidacion IS NULL");
			break;
		}
		
		// Una vez tengo la consulta definida creo el objeto query
		Query query = em.createQuery(consulta.toString());
		
		// Asigno los parámetros
		if (nombre != null && !nombre.trim().isEmpty()) {
			query.setParameter("nombre", "%"+nombre+"%");
		}
		if (codigo != null && !codigo.trim().isEmpty()) {
			query.setParameter("codigo", "%"+codigo+"%");
		}
		if (clave != null && !clave.trim().isEmpty()) {
			query.setParameter("clave", "%"+clave+"%");
		}
		
		List<Entidad> entidades = query.getResultList();
		
		// Ahora compruebo si pertenecen a la raíz especificada,
		// Como puede implicar un proceso muy recursivo existen dos mecanismos
		// la utilización de procesos almacenados o mediante código
		
		if (raiz != null && tipoRaiz != null) {
			Object objetoRaiz = em.find(tipoRaiz, raiz);
			if (objetoRaiz != null) {
				if (objetoRaiz instanceof Plan) {
					return filtrarEntidadesPorPlan(entidades,(Plan)objetoRaiz);
				} else if (objetoRaiz instanceof Ambito) {
					return filtrarEntidadesPorAmbito(entidades,(Ambito)objetoRaiz);
				} else if (objetoRaiz instanceof Entidad) {
					return filtrarEntidadesPorEntidad(entidades, (Entidad)objetoRaiz);
				} else if (objetoRaiz instanceof Tramite) {
					return filtrarEntidadesPorTramite(entidades, (Tramite)objetoRaiz);
				}
			}
		} 
		
		return entidades.toArray(new Entidad[0]);		
	}

	/*
	 * (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.servicios.planeamiento.ServicioBusquedaLocal#buscarPlanes(java.lang.Integer, java.lang.Class, java.lang.String, java.lang.String, java.util.Date, java.util.Date, es.mitc.redes.urbanismoenred.servicios.planeamiento.ModalidadPlanes)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Plan[] buscarPlanes(Integer raiz, Class<?> tipoRaiz,
			String nombre, String codigo, Date desde, Date hasta, ModalidadPlanes modalidad) {
		// Primero creo la consulta
		StringBuffer consulta = new StringBuffer(BUSQUEDA_PLANES);
		boolean condicion = false;
		if (nombre != null && !nombre.trim().isEmpty()) {
			consulta.append(" tra.plan.nombre LIKE :nombre ");
			condicion = true;
		}
		if (codigo != null && !codigo.trim().isEmpty()) {
			if (condicion)
				consulta.append(" AND ");
			else
				condicion = true;
			consulta.append(" tra.plan.codigo LIKE :codigo ");
		}
		if (desde != null) {
			if (condicion)
				consulta.append(" AND ");
			else 
				condicion = true;
			consulta.append(" tra.fecha >= :desde ");
		}
		
		if (hasta != null) {
			if (condicion)
				consulta.append(" AND ");
			else 
				condicion = true;
			consulta.append(" tra.fecha <= :hasta ");
		}
		
		switch (modalidad) {
		case FICHAS:
			break;
		case REF:
			if (condicion)
				consulta.append(" AND ");
			else 
				condicion = true;
			consulta.append(" tra.idtipotramite IN (");
			consulta.append(Textos.getTexto("consola", "tipotramitesrefundibles"));
			consulta.append(") ");
			break;
		case RPM:
			if (condicion)
				consulta.append(" AND ");
			else 
				condicion = true;
			consulta.append(" tra.fechaconsolidacion IS NOT NULL");
			break;
		case TODOS:
			break;
		case VALIDACION:
			if (condicion)
				consulta.append(" AND ");
			else 
				condicion = true;
			consulta.append(" tra.fechaconsolidacion IS NULL");
			break;
		}
		

		// Una vez tengo la consulta definida creo el objeto query
		Query query = em.createQuery(consulta.toString());

		// Asigno los parámetros
		if (nombre != null && !nombre.trim().isEmpty()) {
			query.setParameter("nombre", "%"+nombre+"%");
		}
		if (codigo != null && !codigo.trim().isEmpty()) {
			query.setParameter("codigo", "%"+codigo+"%");
		}
		if (desde != null) {
			query.setParameter("desde", desde);
		}
		if (hasta != null) {
			query.setParameter("hasta", hasta);
		}

		List<Plan> planes = query.getResultList();

		// Ahora compruebo si pertenecen a la raíz especificada,
		// Como puede implicar un proceso muy recursivo existen dos mecanismos
		// la utilización de procesos almacenados o mediante código

		if (raiz != null && tipoRaiz != null) {
			Object objetoRaiz = em.find(tipoRaiz, raiz);
			if (objetoRaiz != null) {
				if (objetoRaiz instanceof Plan) {
					return filtrarPlanesPorPlan(planes,(Plan)objetoRaiz);
				} else if (objetoRaiz instanceof Ambito) {
					return filtrarPlanesPorAmbito(planes,(Ambito)objetoRaiz);
				} 
			}
		} 

		return planes.toArray(new Plan[0]);
	}

	/**
	 * 
	 * @param determinacion
	 * @param detRaiz
	 * @return
	 */
	private boolean esDescendiente(Determinacion determinacion,
			Determinacion detRaiz) {
		if (determinacion != null) {
			if (determinacion.getIden() == detRaiz.getIden()) {
				return true;
			} else {
				return esDescendiente(determinacion.getDeterminacionByIdpadre(), detRaiz);
			}
		}
		return false;
	}

	/**
	 * 
	 * @param entidad
	 * @param entidadRaiz
	 * @return
	 */
	private boolean esDescendiente(Entidad entidad, Entidad entidadRaiz) {
		if (entidad != null) {
			if (entidad.getIden() == entidadRaiz.getIden()) {
				return true;
			} else {
				return esDescendiente(entidad.getEntidadByIdpadre(), entidadRaiz);
			}
		}
		return false;
	}

	/**
	 * 
	 * @param plan
	 * @param planRaiz
	 * @return
	 */
	private boolean esDescendiente(Plan plan, Plan planRaiz) {
		if (plan != null) {
			if (plan.getIden() != planRaiz.getIden() ) {
				return esDescendiente(plan.getPlanByIdpadre() , planRaiz);
			} else {
				return true;
			}
		} 
		
		return false;
	}

	/**
	 * 
	 * @param entidades
	 * @param ambito
	 * @return
	 */
	private Entidad[] filtrarEntidadesPorAmbito(List<Entidad> entidades,
			Ambito ambito) {
		List<Entidad> resultado = new ArrayList<Entidad>();
		for (Entidad entidad : entidades) {
			if (entidad.getTramite().getPlan().getIdambito() == ambito.getIden()) {
				resultado.add(entidad);
			}
		}
		return resultado.toArray(new Entidad[0]);
	}

	/**
	 * 
	 * @param entidades
	 * @param entidadRaiz
	 * @return
	 */
	private Entidad[] filtrarEntidadesPorEntidad(List<Entidad> entidades,
			Entidad entidadRaiz) {
		List<Entidad> resultado = new ArrayList<Entidad>();
		for (Entidad entidad : entidades) {
			if (esDescendiente(entidad.getEntidadByIdpadre(), entidadRaiz)) {
				resultado.add(entidad);
			}
		}
		return resultado.toArray(new Entidad[0]);
	}

	/**
	 * 
	 * @param entidades
	 * @param objetoRaiz
	 * @return
	 */
	private Entidad[] filtrarEntidadesPorPlan(List<Entidad> entidades, Plan planRaiz) {
		List<Entidad> resultado = new ArrayList<Entidad>();
		for (Entidad entidad : entidades) {
			if (esDescendiente(entidad.getTramite().getPlan(),planRaiz)) {
				resultado.add(entidad);
			}
		}
		return resultado.toArray(new Entidad[0]);
	}

	/**
	 * 
	 * @param entidades
	 * @param objetoRaiz
	 * @return
	 */
	private Entidad[] filtrarEntidadesPorTramite(List<Entidad> entidades,
			Tramite tramite) {
		List<Entidad> resultado = new ArrayList<Entidad>();
		for (Entidad entidad : entidades) {
			if (entidad.getTramite().getIden() == tramite.getIden()) {
				resultado.add(entidad);
			}
		}
		return resultado.toArray(new Entidad[0]);
	}

	/**
	 * 
	 * @param planes
	 * @param ambito
	 * @return
	 */
	private Plan[] filtrarPlanesPorAmbito(List<Plan> planes, Ambito ambito) {
		List<Plan> resultado = new ArrayList<Plan>();
		for (Plan plan: planes) {
			if (plan.getIdambito() == ambito.getIden()) {
				resultado.add(plan);
			}
		}
		return resultado.toArray(new Plan[0]);
	}

	/**
	 * 
	 * @param planes
	 * @param planRaiz
	 * @return
	 */
	private Plan[] filtrarPlanesPorPlan(List<Plan> planes, Plan planRaiz) {
		List<Plan> resultado = new ArrayList<Plan>();
		for (Plan plan : planes) {
			if (esDescendiente(plan.getPlanByIdpadre(), planRaiz)) {
				resultado.add(plan);
			}
		}
		return resultado.toArray(new Plan[0]);
	}

	/**
	 * 
	 * @param determinaciones
	 * @param ambito
	 * @return
	 */
	private Determinacion[] filtrarPorAmbito(
			List<Determinacion> determinaciones, Ambito ambito) {
		List<Determinacion> resultado = new ArrayList<Determinacion>();
		for (Determinacion determinacion : determinaciones) {
			if (determinacion.getTramite().getPlan().getIdambito() == ambito.getIden()) {
				resultado.add(determinacion);
			}
		}
		return resultado.toArray(new Determinacion[0]);
	}

	/**
	 * 
	 * @param determinaciones
	 * @param detRaiz
	 * @return
	 */
	private Determinacion[] filtrarPorDeterminacion(
			List<Determinacion> determinaciones, Determinacion detRaiz) {
		List<Determinacion> resultado = new ArrayList<Determinacion>();
		for (Determinacion determinacion : determinaciones) {
			if (esDescendiente(determinacion.getDeterminacionByIdpadre(), detRaiz)) {
				resultado.add(determinacion);
			}
		}
		return resultado.toArray(new Determinacion[0]);
	}

	/**
	 * 
	 * @param determinaciones
	 * @param planRaiz
	 * @return
	 */
	private Determinacion[] filtrarPorPlan(List<Determinacion> determinaciones,
			Plan planRaiz) {
		List<Determinacion> resultado = new ArrayList<Determinacion>();
		for (Determinacion determinacion : determinaciones) {
			if (esDescendiente(determinacion.getTramite().getPlan(),planRaiz)) {
				resultado.add(determinacion);
			}
		}
		return resultado.toArray(new Determinacion[0]);
	}

	/**
	 * 
	 * @param determinaciones
	 * @param objetoRaiz
	 * @return
	 */
	private Determinacion[] filtrarPorTramite(
			List<Determinacion> determinaciones, Tramite tramite) {
		List<Determinacion> resultado = new ArrayList<Determinacion>();
		for (Determinacion determinacion : determinaciones) {
			if (determinacion.getTramite().getIden() == tramite.getIden()) {
				resultado.add(determinacion);
			}
		}
		return resultado.toArray(new Determinacion[0]);
	}

}

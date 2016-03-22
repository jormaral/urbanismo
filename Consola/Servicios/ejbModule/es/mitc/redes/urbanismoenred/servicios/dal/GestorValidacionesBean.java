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
package es.mitc.redes.urbanismoenred.servicios.dal;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import es.mitc.redes.urbanismoenred.data.validacion.Validacion;
import es.mitc.redes.urbanismoenred.servicios.validacion.ContextoValidacion;
import es.mitc.redes.urbanismoenred.servicios.validacion.ServicioResultadosValidacionLocal;
import es.mitc.redes.urbanismoenred.servicios.validacion.TipoValidacion;

/**
 * Implementación de gestor de validaciones basado en una tabla de consultas
 * SQL en las que se definen las validaciones a realizar.
 * 
 * @author Arnaiz Consultores
 *
 */
@Stateless
public class GestorValidacionesBean implements GestorValidacionesLocal {
	
	private static final Logger log = Logger.getLogger(GestorValidacionesBean.class);
	
	private static Map<Integer,String> CODIGOS_VALIDACION;
	
	@PersistenceContext(unitName = "rpmv2")
	private EntityManager em;
	@EJB
	private ServicioResultadosValidacionLocal servicioResultados;
	
	static {
		CODIGOS_VALIDACION = new HashMap<Integer, String>();
		CODIGOS_VALIDACION.put(VALIDACIONES_CONDICIONES_URBANISTICAS, "validacion.condicionesUrbanisticas");
		CODIGOS_VALIDACION.put(VALIDACIONES_DETERMINACIONES, "validacion.determinaciones");
		CODIGOS_VALIDACION.put(VALIDACIONES_ENTIDADES, "validacion.entidades");
		CODIGOS_VALIDACION.put(VALIDACIONES_TRAMITE, "validacion.tramite");
		CODIGOS_VALIDACION.put(VALIDACIONES_OTRAS, "validacion.otras");
	}
	
	/*
	 * (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.servicios.dal.GestorValidacionesLocal#ejecutarValidaciones(es.mitc.redes.urbanismoenred.servicios.validacion.ContextoValidacion)
	 */
	@Override
	public void ejecutarValidaciones(ContextoValidacion contexto)
			throws ExcepcionPersistencia {
		log.debug("Ejecutando validaciones.");
		boolean exito = true;
		boolean resultado = true;
		int porcentaje = 0;
		
		if (contexto.getParametro(ContextoValidacion.TIPO_VALIDACION) != null) {
			TipoValidacion tipo = (TipoValidacion)contexto.getParametro(ContextoValidacion.TIPO_VALIDACION);
			
			List<Validacion> validaciones = obtenerValidaciones(tipo.getCodigo());
			
			String sql;
			Query query;
			List<?> resultados;
			Map<String, Object> parametros = contexto.getParametros();
			int c = 0;
			int size = validaciones.size();
			
			for (Validacion validacion : validaciones) {
				c++;
				
				log.info("Ejecutando validación " + c + " de "+ size + ": " + validacion.getDescripcion());
				
				sql = validacion.getSentencia();
				
				log.debug("Sentencia: " + validacion.getSentencia());
					
				query = em.createNativeQuery(sql);
					
				// Introducimos los parámetros que se soliciten en la sentencia SQL
				// Si se introduce un parámetro que no está en el SQL da un
				// IllegalArgumentException.
				// Todas las sentencias tiene al menos un parámetro, el id del trámite.
				for (String clave : parametros.keySet()) {
					if (sql.contains(clave)) {
						query.setParameter(clave, parametros.get(clave));
					}
				}
					
				log.debug("Ejecutando consulta.");
				resultados = query.getResultList();
				
					
				// Sólo se contempla que los resultado puedan ser "null" o "no null"
				// Si hubiera que añadir más habrá que cambiar esto
				if ("null".equalsIgnoreCase(validacion.getResultadoesperado())) {
					if (!resultados.isEmpty()) {
						if (validacion.getTipoerror() == 1) {
							resultado = exito = false;
						} else {
							exito = false;
						}
					} else {
						exito = true;
					}
				} else {
					if (resultados.isEmpty()) {
						if (validacion.getTipoerror() == 1) {
							resultado = exito = false;
						} else {
							exito = false;
						}
						
					} else {
						exito = true;
					}
				}
				porcentaje = (c*100)/size;
				contexto.putParametro(ContextoValidacion.PROGRESO, porcentaje);
				log.info("Validación ejecutada "+ ((exito)? "correctamente.":" con errores."));
				servicioResultados.registrarResultado(contexto.getCodigoFip(), 
						validacion, 
						exito, 
						resultados);
				
			}
			
			contexto.putParametro(ContextoValidacion.RESULTADO_VALIDACION, (contexto.getParametro(ContextoValidacion.RESULTADO_VALIDACION) != null)? resultado & (Boolean)contexto.getParametro(ContextoValidacion.RESULTADO_VALIDACION):resultado);
			
		} else {
			throw new ExcepcionPersistencia("No se ha especificado el tipo de validación.");
		}
		
		log.debug("Validaciones finalizadas.");
	}

	/*
	 * (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.servicios.dal.GestorValidacionesLocal#ejecutarValidaciones(long, int, java.util.Map)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean ejecutarValidaciones(String idFip, int tipo, Map<String, Object> parametros)
			throws ExcepcionPersistencia {
		log.debug("Ejecutando validaciones.");
		boolean exito = true;
		boolean resultado = true;
		List<Validacion> validaciones = obtenerValidaciones(tipo);
		String sql;
		Query query;
		List<Object[]> resultados;
		int c = 0;
		int size = validaciones.size();
		for (Validacion validacion : validaciones) {
			c++;
			log.info("Ejecutando validación " + c + " de "+ size + ": " + validacion.getDescripcion());
			if (validacion.isActivado()) {
				sql = validacion.getSentencia();
			
				log.debug("Sentencia: " + validacion.getSentencia());
				
				query = em.createNativeQuery(sql);
				
				// Introducimos los parámetros que se soliciten en la sentencia SQL
				// Si se introduce un parámetro que no está en el SQL da un
				// IllegalArgumentException.
				// Todas las sentencias tiene al menos un parámetro, el id del trámite.
				for (String clave : parametros.keySet()) {
					if (sql.contains(clave)) {
						query.setParameter(clave, parametros.get(clave));
					}
				}
				
				log.debug("Ejecutando consulta.");
				resultados = (List<Object[]>)query.getResultList();
				
				// Sólo se contempla que los resultado puedan ser "null" o "no null"
				// Si hubiera que añadir más habrá que cambiar esto
				if ("null".equalsIgnoreCase(validacion.getResultadoesperado())) {
					if (!resultados.isEmpty()) {
						resultado = exito = false;
					} else {
						exito = true;
					}
				} else {
					if (resultados.isEmpty()) {
						resultado = exito = false;
					} else {
						exito = true;
					}
				}
				log.info("Validación ejecutada "+ ((exito)? "correctamente.":" con errores."));
				servicioResultados.registrarResultado(idFip, 
						validacion, 
						exito, 
						resultados);
			} else {
				log.info("Validación desactivada:");
			}
		}
		log.debug("Validaciones finalizadas.");
		return resultado;
	}
	
	/* (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.servicios.dal.GestorValidacionesLocal#obtenerValidaciones(int)
	 */
	@Override
	@SuppressWarnings("unchecked")
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Validacion> obtenerValidaciones(int tipo)
			throws ExcepcionPersistencia {
		
		List<Validacion> validaciones;
		if (tipo != TipoValidacion.TODAS.getCodigo()) {
			validaciones = (List<Validacion>)em.createNamedQuery("Validacion.obtenerPorTipo")
				.setParameter("tipoValidacion", tipo).getResultList();
		} else {
			validaciones = (List<Validacion>)em.createNamedQuery("Validacion.obtenerTodas")
				.getResultList();
		}
		
		return validaciones;
	}

}

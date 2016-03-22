/*
* Copyright 2011 red.es
* Autores: IDOM Consulting
*
* Licencia con arreglo a la EUPL, Versiï¿½n 1.1 o -en cuanto
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

package com.mitc.redes.editorfip.servicios.gestionfip.validaciones;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.async.Asynchronous;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.international.StatusMessage.Severity;
import org.jboss.seam.log.Log;

import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Tramite;
import com.mitc.redes.editorfip.entidades.rpm.validacion.Error;
import com.mitc.redes.editorfip.entidades.rpm.validacion.Proceso;
import com.mitc.redes.editorfip.entidades.rpm.validacion.Resultado;
import com.mitc.redes.editorfip.entidades.rpm.validacion.ResultadoId;
import com.mitc.redes.editorfip.entidades.rpm.validacion.Validacion;
import com.mitc.redes.editorfip.excepciones.EditorFIPException;







@Stateless
@Name("servicioGestionValidacionesNuevo")
public class ServicioGestionValidacionesNuevoBean implements ServicioGestionValidacionesNuevo
{
    @Logger private Log log;
    
    @PersistenceContext
	EntityManager em;
    
    @In Map<String,String> messages;
    
    @In (create = true)
	FacesMessages facesMessages;
    
    
    // ------------------------------
    // GLOBAL
    // ------------------------------	

	/* (non-Javadoc)
	 * @see com.mitc.redes.editorfip.servicios.gestionfip.validaciones.ServicioGestionValidaciones#validarTramite(int)
	 */
    @Asynchronous
    public void validarTramiteAsincrono (int idTramite, Proceso procesoValidacion)
	{
		log.debug("[validarTramite] Se inicia el proceso de validación para idTramite="+idTramite);
		
		Tramite tram = em.find(Tramite.class,idTramite);
		
		try {
			
			//Tramite tram = em.find(Tramite.class, idTramite);
			
			
        	Map<String, Object> parametros = new HashMap<String, Object>();
        	parametros.put("codigoFip", tram.getCodigofip());
			boolean resultado = ejecutarValidaciones(procesoValidacion,tram.getCodigofip(), 0, parametros);
    
            log.info("Validados condiciones urbanisticas del FIP: " + tram.getCodigofip() + " con resultado= " + resultado);
          
        } catch (Exception ex) {
            log.error(ex.getLocalizedMessage(),ex);
            ex.printStackTrace();
            
        }
		
		
		
		// Establezco el estado
		procesoValidacion.setEstado("VALIDADO");
		procesoValidacion.setFin(new Date());
		em.merge(procesoValidacion);
		log.info("[validarTramite] FIN del proceso de validación para idTramite="+idTramite);
		facesMessages.addFromResourceBundle(Severity.INFO,"Proceso de validado completado", null);
		
		
		
	}
    
    
	public void validarTramite (int idTramite)
	{
		log.debug("[validarTramite] Se inicia el proceso de validación para idTramite="+idTramite);
		// ------------------------
		// Creo un nuevo Proceso de validacion
		// ------------------------
		
		// Instancio el objeto
		Proceso procesoValidacion = new Proceso();
		
		// Obtengo la referencia al idTramite
		//Tramite tram = em.find(Tramite.class, idTramite);
		procesoValidacion.setIdTramite(idTramite);
		
		// Compruebo si hay algun proceso de validacion anterior de este trámite
		int iteracionProceso = 1;
		
		String queryNumeroIteracion =  "SELECT max(proc.iteracionValidacion) " +
                " FROM Proceso proc " +
                " WHERE proc.idTramite=" +idTramite;

        

         try {
        	 iteracionProceso = (Integer) em.createQuery(queryNumeroIteracion).getSingleResult();
        } catch (NoResultException e) {
            log.info("[validarTramite] No se ha iteracion para el tramite. Se le asignará 1." + e.getMessage());

        } catch (Exception ex) {
            log.warn("[validarTramite] Error: No se ha iteracion para el tramite. Se le asignará 1 " + ex.getMessage());

        }
		
		procesoValidacion.setIteracionValidacion(iteracionProceso + 1);
		
		// Establezco la fecha
		procesoValidacion.setInicio(new Date());
		
		// Establezco el estado
		procesoValidacion.setEstado("VALIDANDO...");
		
		// Guardo el CodigoFIP
		Tramite tram = em.find(Tramite.class, idTramite);
		procesoValidacion.setIdfip(tram.getCodigofip());
		
		
		
		// Persisto el proceso en base de datos
		
		em.persist(procesoValidacion);
		log.debug("[validarTramite] Guardado en BD nuevo proceso de validación para idTramite="+idTramite);
		
		try {
			
			//Tramite tram = em.find(Tramite.class, idTramite);
			
			
        	Map<String, Object> parametros = new HashMap<String, Object>();
        	parametros.put("codigoFip", tram.getCodigofip());
			boolean resultado = ejecutarValidaciones(procesoValidacion,tram.getCodigofip(), 0, parametros);
    
            log.info("Validados condiciones urbanisticas del FIP: " + tram.getCodigofip() + " con resultado= " + resultado);
          
        } catch (Exception ex) {
            log.error(ex.getLocalizedMessage(),ex);
            ex.printStackTrace();
            
        }
		
		
		
		// Establezco el estado
		procesoValidacion.setEstado("VALIDADO");
		procesoValidacion.setFin(new Date());
		em.merge(procesoValidacion);
		log.info("[validarTramite] FIN del proceso de validación para idTramite="+idTramite);
		facesMessages.addFromResourceBundle(Severity.INFO,"Proceso de validado completado", null);
		
		
		
		
	}
	
	
	public boolean ejecutarValidaciones(Proceso proceso,String idFip, int tipo, Map<String, Object> parametros)
	throws EditorFIPException {
		log.debug("[ejecutarValidaciones] Ejecutando validaciones.");
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
			log.info("Ejecutando validaci—n " + c + " de "+ size + ": " + validacion.getDescripcion());
			if (validacion.isActivado()) {
				sql = validacion.getSentencia();
			
				log.debug("Sentencia: " + validacion.getSentencia());
				
				query = em.createNativeQuery(sql);
				
				// Introducimos los par‡metros que se soliciten en la sentencia SQL
				// Si se introduce un par‡metro que no est‡ en el SQL da un
				// IllegalArgumentException.
				// Todas las sentencias tiene al menos un par‡metro, el id del tr‡mite.
				for (String clave : parametros.keySet()) {
					if (sql.contains(clave)) {
						query.setParameter(clave, parametros.get(clave));
					}
				}
				
				log.debug("Ejecutando consulta.");
				resultados = (List<Object[]>)query.getResultList();
				
				// S—lo se contempla que los resultado puedan ser "null" o "no null"
				// Si hubiera que a–adir m‡s habr‡ que cambiar esto
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
				log.info("Validaci—n ejecutada "+ ((exito)? "correctamente.":" con errores."));
				registrarResultado(proceso,idFip, 
						validacion, 
						exito, 
						resultados);
			} else {
				log.info("Validaci—n desactivada:");
			}
		}
		log.debug("Validaciones finalizadas.");
		return resultado;
		}
		
		/* (non-Javadoc)
		* @see es.mitc.redes.urbanismoenred.servicios.dal.GestorValidacionesLocal#obtenerValidaciones(int)
		*/
		
		@SuppressWarnings("unchecked")
		@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
		public List<Validacion> obtenerValidaciones(int tipo)
			throws EditorFIPException {
			
			log.info("[obtenerValidaciones] obtenerValidaciones" );
		
			List<Validacion> validaciones;
			
			// Las obtengo todas
			
			String query = "SELECT val FROM Validacion val WHERE val.activado = true ORDER BY val.nemo";
			
			
			 try {
				 validaciones = (List<Validacion>) em.createQuery(query).getResultList();
	        } catch (Exception e) {
	            log.info("[obtenerValidaciones]Error al obtener todas las validaciones:" + e.getMessage());
	            e.printStackTrace();
	            
	            throw new EditorFIPException("Error al obtener todas las validaciones");
	
	        } 
			return validaciones;
		}
		
		public void registrarResultado( Proceso proceso, String idFip, Validacion validacion, boolean exito,
				List<?> resultados) throws EditorFIPException{
			
			log.info("[registrarResultado] Inicio" );
			
			try {
				
				
				
				// Por œltimo se crea el resultado de validaci—n 
				Resultado resultado = new Resultado();
				ResultadoId	id = new ResultadoId();
				id.setIdproceso(proceso.getIden());
				id.setIdvalidacion(validacion.getIden());
				resultado.setId(id);
				resultado.setValidacion(validacion);
				resultado.setExito(exito);
				resultado.setFecha(GregorianCalendar.getInstance().getTime());
				resultado.setProceso(proceso);
				
				em.persist(resultado);
				
				if (!exito) {
					Error error;
					if (!resultados.isEmpty()) {
						String mensaje;
						for (Object datos : resultados) {
							error = new Error();
							error.setResultado(resultado);
							if (datos.getClass().isArray()) {
								Object valores[] = (Object[])datos;
								mensaje = validacion.getMensaje();
								for (int i = 0; i < valores.length; i++) {
									mensaje = mensaje.replace("%"+(i+1), valores[i].toString());
								}
							} else {
								mensaje = validacion.getMensaje().replace("%1", datos.toString());
							}
							error.setMensaje(mensaje);
							em.persist(error);
						}
					} else {
						error = new Error();
						error.setResultado(resultado);
						error.setMensaje("No se han encontrado elementos que cumplan la validaci—n.");
						em.persist(error);
					}
				}
				
			} catch (Exception nre) {
				
				log.error("[registrarResultado] Error al registrar el resultado: " + nre.getLocalizedMessage());
				nre.printStackTrace();
				throw new EditorFIPException("[registrarResultado] Error al registrar el resultado: " + nre.getLocalizedMessage());
				
			}
		}

	

    
}

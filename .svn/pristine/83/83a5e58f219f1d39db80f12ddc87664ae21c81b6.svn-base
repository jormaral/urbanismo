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
package es.mitc.redes.urbanismoenred.servicios.validacion;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;

import es.mitc.redes.urbanismoenred.data.validacion.Proceso;
import es.mitc.redes.urbanismoenred.servicios.dal.ExcepcionPersistencia;
import es.mitc.redes.urbanismoenred.servicios.dal.GestorValidacionesLocal;
import es.mitc.redes.urbanismoenred.servicios.seguridad.Usuario;

/**
 * Implementación del servicio de validación.
 * 
 * @author Arnaiz Consultores
 *
 */
@Stateless
public class ServicioValidacionBean implements ServicioValidacionLocal, ServicioValidacionRemote {

	private static final Logger log = Logger.getLogger(ServicioValidacionBean.class);
    
    // --- Variables de conexion a otros EJB --- //
    @EJB 
    private GestorValidacionesLocal gestorValidaciones;
    // Conexion con el bean de validacion de integridad
    @EJB
    private ServicioValidacionIntegridadLocal valIntegSvc;

    @EJB
    private ServicioResultadosValidacionLocal servicioResultados;
    
    // -- METODOS -- //
	
	/*
	 * (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.servicios.validacion.ServicioValidacionLocal#finalizar(java.lang.String)
	 */
	@Override
	public void finalizar(String idFip) throws ExcepcionValidacion {
    	try {
			servicioResultados.finalizarProceso(idFip);
		} catch (ExcepcionPersistencia e) {
			throw new ExcepcionValidacion("No se ha podido finalizar el proceso de validación del trámite " 
					+ idFip + ". Causa: " + e.getMessage());
		}
	}
	
	/*
     * (non-Javadoc)
     * @see es.mitc.redes.urbanismoenred.servicios.validacion.ServicioValidacionLocal#iniciar(java.lang.String)
     */
	@Override
	public int iniciar(String idFip) throws ExcepcionValidacion {
		try {
			List<Proceso> procesos = servicioResultados.getProcesos(idFip);
			for (Proceso proceso : procesos) {
				if (proceso.getConsolidado() != null) {
					throw new ExcepcionValidacion("No se ha podido iniciar el proceso de validación del trámite " 
							+ idFip + ". Causa: el trámite ya está consolidado");
				}
			}
			return servicioResultados.iniciarProceso(idFip);
		} catch (ExcepcionPersistencia e) {
			throw new ExcepcionValidacion("No se ha podido iniciar el proceso de validación del trámite " 
					+ idFip + ". Causa: " + e.getMessage());
		}
	}
    
	/*
	 * (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.servicios.validacion.ServicioValidacionLocal#validaCondicionesUrbanisticas(java.lang.String)
	 */
    @Override
    public boolean validaCondicionesUrbanisticas(String idFip) {
        log.info("Se van a validar las condiciones urbanisticas del FIP: " + idFip);

        try {
        	Map<String, Object> parametros = new HashMap<String, Object>();
        	parametros.put("codigoFip", idFip);
			boolean resultado = gestorValidaciones.ejecutarValidaciones(idFip, 
					GestorValidacionesLocal.VALIDACIONES_CONDICIONES_URBANISTICAS, 
					parametros);
    
            log.info("Validados condiciones urbanisticas del FIP: " + idFip + " con resultado= " + resultado);
            return resultado;
        } catch (Exception ex) {
            log.error(ex.getLocalizedMessage(),ex);
            return false;
        }

    }

    /*
     * (non-Javadoc)
     * @see es.mitc.redes.urbanismoenred.servicios.validacion.ServicioValidacionLocal#validaIntegridad(long, java.lang.String)
     */
    @Override
    public boolean validaIntegridad(String idFip, String nombreUsuario) {
        try {      
			// Obtengo el resultado de la validacion de integridad
            return valIntegSvc.validar(idFip, nombreUsuario);
        } catch (Exception ex) {
            log.error(ex.getLocalizedMessage(), ex);
            return false;
        }
    }

    /*
     * (non-Javadoc)
     * @see es.mitc.redes.urbanismoenred.servicios.validacion.ServicioValidacionLocal#validaDeterminaciones(long)
     */
    @Override
    public boolean validaDeterminaciones(String idFip) {
       
        log.info("Se van a validar las determinaciones del FIP: " + idFip);

        try {
        	Map<String, Object> parametros = new HashMap<String, Object>();
        	parametros.put("codigoFip", idFip);
        	
			boolean resultado = gestorValidaciones.ejecutarValidaciones(idFip, 
					GestorValidacionesLocal.VALIDACIONES_DETERMINACIONES, 
					parametros);
			
			return resultado;
        } catch (Exception ex) {
            log.error(ex.getLocalizedMessage(),ex);
            return false;
        }
    }

    /*
     * (non-Javadoc)
     * @see es.mitc.redes.urbanismoenred.servicios.validacion.ServicioValidacionLocal#validaTramite(long)
     */
    @Override
    public boolean validaTramite(String idFip) {
        log.info("Se va a validar el tramite del FIP: " + idFip);

        try {

        	Map<String, Object> parametros = new HashMap<String, Object>();
        	parametros.put("codigoFip", idFip);
        	
			boolean resultado = gestorValidaciones.ejecutarValidaciones(idFip, 
					GestorValidacionesLocal.VALIDACIONES_TRAMITE, 
					parametros);
			
			log.info("Validados el tramite del FIP: " + idFip + " con resultado= " + resultado);
			return resultado;
        } catch (Exception ex) {
           
            log.error(ex.getLocalizedMessage(), ex);
            return false;
        }
    }

    /*
     * (non-Javadoc)
     * @see es.mitc.redes.urbanismoenred.servicios.validacion.ServicioValidacionLocal#validaEntidades(long)
     */
    @Override
    public boolean validaEntidades(String idFip) {
        log.info("Se van a validar las entidades del FIP: " + idFip);

        try {

        	Map<String, Object> parametros = new HashMap<String, Object>();
        	parametros.put("codigoFip", idFip);
        	
			boolean resultado = gestorValidaciones.ejecutarValidaciones(idFip, 
					GestorValidacionesLocal.VALIDACIONES_ENTIDADES, 
					parametros);
			
            log.info("Validados las entidades del FIP: " + idFip + " con resultado= " + resultado);
            return resultado;

        } catch (Exception ex) {
            log.error(ex.getLocalizedMessage(),ex);
            return false;
        }
    }

    /*
     * (non-Javadoc)
     * @see es.mitc.redes.urbanismoenred.servicios.validacion.ServicioValidacionLocal#validaOtras(long)
     */
    @Override
    public boolean validaOtras(String idFip) {
        log.info("Se van a validar otras validaciones del FIP: " + idFip);

        try {

        	Map<String, Object> parametros = new HashMap<String, Object>();
        	parametros.put("codigoFip", idFip);
        	
			boolean resultado = gestorValidaciones.ejecutarValidaciones(idFip, 
					GestorValidacionesLocal.VALIDACIONES_OTRAS, 
					parametros);

			log.info("Validados otras validaciones del FIP: " + idFip + " con resultado= " + resultado);
			return resultado;
        } catch (Exception ex) {
            log.error(ex.getLocalizedMessage(),ex);
            return false;
        }
    }

    /*
     * (non-Javadoc)
     * @see es.mitc.redes.urbanismoenred.servicios.validacion.ServicioValidacionLocal#validar(es.mitc.redes.urbanismoenred.servicios.validacion.ContextoValidacion)
     */
	@Override
	public void validar(ContextoValidacion contexto) throws ExcepcionValidacion {	
		if (contexto.getParametro(ContextoValidacion.TIPO_VALIDACION) != null) {
			TipoValidacion tipo = (TipoValidacion)contexto.getParametro(ContextoValidacion.TIPO_VALIDACION);
			Usuario usuario = (Usuario)contexto.getParametro(ContextoValidacion.USUARIO);
			if (usuario == null) {
				throw new ExcepcionValidacion("No se puede iniciar la validación, el trámite no ha sido asignado a ningún usuario");
			}
			
			switch (tipo) {
				case INTEGRIDAD:
					// Las validaciones de integridad son dos. No es muy limpio pero por ahora vale
					contexto.putParametro(ContextoValidacion.RESULTADO_VALIDACION, valIntegSvc.validar(contexto.getCodigoFip(), usuario.getNombre()));		
					break;
				case TODAS:
					contexto.putParametro(ContextoValidacion.RESULTADO_VALIDACION,valIntegSvc.validar(contexto.getCodigoFip(), 
							usuario.getNombre()));
					
				case CONDICIONES_URBANISTICAS:
				case DETERMINACIONES:
				case ENTIDADES:
				case OTRAS:
				case TRAMITE:
				default:
					try {
						gestorValidaciones.ejecutarValidaciones(contexto);
					} catch (ExcepcionPersistencia e) {
						throw new ExcepcionValidacion("No se han podido ejecutar las validaciones de base de datos.",
								(Integer)contexto.getParametro(ContextoValidacion.ID_VALIDACION),
								e);
					}
					break;
			}
		} else {
			throw new ExcepcionValidacion("No se ha definido el tipo de validación a realizar.");
		}
		
	}
}


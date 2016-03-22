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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Tramite;
import es.mitc.redes.urbanismoenred.data.validacion.Proceso;
import es.mitc.redes.urbanismoenred.servicios.comunes.GestionIntroduccionFIPenSistemaLocal;
import es.mitc.redes.urbanismoenred.servicios.seguridad.ServicioUsuariosLocal;
import es.mitc.redes.urbanismoenred.servicios.seguridad.Usuario;
import es.mitc.redes.urbanismoenred.utils.excepciones.RedesException;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import org.apache.log4j.Logger;

/**
 * @author Arnaiz Consultores
 */
@Stateless (name = "ServicioValidacionFIP")
public class ValidacionFipBean implements ValidacionFipRemote, ValidacionFipLocal {

    private static final Logger log = Logger.getLogger(ValidacionFipBean.class);
   
    @EJB
    private GestorContextosValidacionLocal gestorContextos;
    @EJB
	private ServicioResultadosValidacionLocal gestorProcesos;
    @EJB
    private ServicioGeneracionDocumentosLocal servicioDocumentos;
    @EJB
    private GestionIntroduccionFIPenSistemaLocal servicioIntroduccionFip;
    @EJB
    private ServicioValidacionLocal servicioValidacion;
    @EJB
    private ServicioUsuariosLocal servicioUsuarios;
    
    /*
     * (non-Javadoc)
     * @see es.mitc.redes.urbanismoenred.servicios.validacion.ValidacionFipRemote#desplegarFip(es.mitc.redes.urbanismoenred.servicios.validacion.ContextoValidacion)
     */
    @Override
	public void desplegarFip(ContextoValidacion contexto)
			throws ExcepcionValidacion {
    	//1º El Servlet ya se ha encargado de Subir el fichero al Servidor, y me pasa el path al fichero en el pathFip.
    	String pathFip = (String)contexto.getParametro(ContextoValidacion.RUTA_FIP);
    	if ( pathFip == null)
    		throw new ExcepcionValidacion("No se ha especificado la ruta al FIP");
    		
    	Usuario usuario = (Usuario)contexto.getParametro(ContextoValidacion.USUARIO);
    	try {
	    	// Sólo se puede desplegar un FIP si el usuario tiene permisos
	    	// de validación sobre el trámite afectado.
	    	if (usuario != null) {
	    		// Esto lo tengo que hacer así porque la lista devuelta por Arrays.asList es inmutable
	    		List<Tramite> desplegados = Arrays.asList(servicioIntroduccionFip.getTramitesDesplegados());
	    		List<Tramite> pendientes = Arrays.asList(servicioIntroduccionFip.getTramitesPendientes());
	    		List<Tramite> tramites = new ArrayList<Tramite>(desplegados.size()+pendientes.size());
	    		tramites.addAll(desplegados);
	    		tramites.addAll(pendientes);
	    		pendientes = null;
	    		desplegados = null;
	    		Tramite tramite = null;
	    		
	    		for ( int i = 0; i < tramites.size(); i++) {
	    			if (tramites.get(i).getCodigofip().equals(contexto.getCodigoFip())) {
	    				tramite = tramites.get(i);
	    				break;
	    			}
	    		}
	    		
	    		if (tramite != null) {
	    			if (!usuario.puedeValidar(tramite.getPlan().getIdambito())) {
		    			throw new ExcepcionValidacion("El usuario " + 
		                		usuario.getNombre() +
		                		" no tiene permisos para validar el trámite " +
		                		contexto.getCodigoFip());
		    		}
	    		} else {
	    			throw new ExcepcionValidacion("No existe trámite pendiente de FIP asociado al código " + 
	                		contexto.getCodigoFip());
	    		}
	    		
	        } else {
	            throw new ExcepcionValidacion("El contexto no tiene usuario definido. ");
	        }
    	
	    	// una vez pasadas todas estas comprobaciones procedemos a desplegar el FIP
    	
        	// Antes de desplegar un FIP nos aseguramos que no hay ningún proceso de validación
            // en ejecución o que el tramite de dicho FIP está ya consolidado.
            List<Proceso> procesos = gestorProcesos.getProcesos(contexto.getCodigoFip());
            
            for (Proceso proceso : procesos) {
         	   if (proceso.getFin() == null) {
         		   throw new ExcepcionValidacion("Existe un proceso de validación ya iniciado para el trámite " 
         				   + contexto.getCodigoFip() + ". Se debe finalizar primero ese proceso.");
         	   }
         	   if (proceso.getConsolidado() != null) {
         		   throw new ExcepcionValidacion("El trámite " 
         				   + contexto.getCodigoFip() + " ya se encuentra consolidado. No se pueden subir nuevas versiones.");
         	   }
            }
            
            // Se despliega el FIP en el sistema de archivos
        
			servicioIntroduccionFip.desplegarFIP(contexto);
			
			// Una vez desplegado correctamente el FIP se guarda en base de datos.
			servicioIntroduccionFip.guardarDatosXML(contexto);
			
		} catch (RedesException e) {
			throw new ExcepcionValidacion(e.getMessage(), -1,e);
		} catch (Exception e) {
			throw new ExcepcionValidacion(e.getMessage(), -1,e);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.servicios.validacion.ValidacionFipRemote#generarInforme(int)
	 */
	@Override
	public byte[] generarInforme(int proceso)
			throws ExcepcionValidacion {
		return servicioDocumentos.generarDocumento(proceso);
	}

    /*
	 * (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.servicios.validacion.ValidacionFipLocal#getFipPendientes(java.lang.String)
	 */
    @Override
	public ContextoValidacion[] getFipPendientes(String nombreUsuario) {
		
		Usuario usuario = servicioUsuarios.getUsuario(nombreUsuario);
		
		if (usuario != null) {
			return gestorContextos.getContextos(usuario);
		} else {
			log.warn("El usuario " + nombreUsuario + " no se encuentra en base de datos.");
		}
		
		return new ContextoValidacion[0];
	}

	/*
	 * (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.servicios.validacion.ValidacionFipRemote#validar(es.mitc.redes.urbanismoenred.servicios.validacion.ContextoValidacion)
	 */
	@Override
	public void validar(ContextoValidacion contexto) throws ExcepcionValidacion {
		
		int idProceso = servicioValidacion.iniciar(contexto.getCodigoFip());
		Estado estadoInicial = contexto.getEstado();
		contexto.setEstado(Estado.VALIDANDO);
		contexto.putParametro(ContextoValidacion.PROGRESO, 0);
		contexto.putParametro(ContextoValidacion.ID_VALIDACION, idProceso);
		
		servicioValidacion.validar(contexto);
		
		servicioValidacion.finalizar(contexto.getCodigoFip());
		
		if (contexto.getParametro(ContextoValidacion.RESULTADO_VALIDACION) != null) {
			if ((Boolean)contexto.getParametro(ContextoValidacion.RESULTADO_VALIDACION)) {
				contexto.setEstado(Estado.VALIDADO);
			} else {
				contexto.setEstado(Estado.VALIDACION_ERRONEA);
			}
		} else {
			contexto.setEstado(estadoInicial);
		}
		contexto.putParametro(ContextoValidacion.USUARIO, null);
	}
}
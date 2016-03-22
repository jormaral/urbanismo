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
package es.mitc.redes.urbanismoenred.consola.validacion.acciones;

import java.io.DataOutputStream;
import java.io.IOException;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import es.mitc.redes.urbanismoenred.consola.validacion.IAccion;
import es.mitc.redes.urbanismoenred.data.validacion.Proceso;
import es.mitc.redes.urbanismoenred.servicios.validacion.ContextoValidacion;
import es.mitc.redes.urbanismoenred.servicios.validacion.ExcepcionValidacion;
import es.mitc.redes.urbanismoenred.servicios.validacion.GestorContextosValidacionLocal;
import es.mitc.redes.urbanismoenred.servicios.validacion.ServicioResultadosValidacionLocal;
import es.mitc.redes.urbanismoenred.servicios.validacion.ValidacionFipLocal;

/**
 * @author Arnaiz Consultores
 *
 */
@Stateless (name = "DESCARGAR_INFORME_VALIDACION")
public class DescargarInformeValidacion implements IAccion {
	private static final Logger log = Logger.getLogger(DescargarInformeValidacion.class);
	
	@EJB
	private ValidacionFipLocal servicioValidacion;
	
	@EJB
	private ServicioResultadosValidacionLocal servicioResultados;
	
	@EJB
	private GestorContextosValidacionLocal gestorContextos;

	/* (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.consola.validacion.IAccion#ejecutar(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public void ejecutar(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String idProceso = request.getParameter("idProceso");
		String tramite = request.getParameter("tramite");
		
		byte[] informe = null;
		try {
			if (idProceso != null) {
				informe = servicioValidacion.generarInforme(Integer.parseInt(idProceso));
				Proceso proceso = servicioResultados.getProceso(Integer.parseInt(idProceso));
				
				tramite = proceso.getIdfip();
			} else {
				if (tramite != null) {
					ContextoValidacion contexto = gestorContextos.getContexto(tramite);
					
					if(contexto != null) {
						informe = servicioValidacion.generarInforme((Integer)contexto.getParametro(ContextoValidacion.ID_VALIDACION));
					} else {
						log.error("No existe contexto asociado al trámite " + tramite);
						throw new IOException("No existe contexto asociado al trámite " + tramite);
					}
				} else {
					log.error("No se han detectado contextos en la sesión.");
					throw new IOException("No se han detectado contextos en la sesión.");
				}
			}
		} catch (ExcepcionValidacion e) {
			log.error("Error al recuperar el informe: " + e.getMessage());
			throw new IOException("Error al recuperar el informe: " + e.getMessage());
		} catch (NumberFormatException nfe) {
			log.error("Error al recuperar el informe: Identificador de proceso incorrecto");
			throw new IOException("Error al recuperar el informe: Identificador de proceso incorrecto");
		}
		
		response.setContentType("application/pdf");
		response.setHeader("Content-disposition", "inline; filename="+tramite+".pdf" );
		response.setContentLength(informe.length);
		DataOutputStream dos = new DataOutputStream(response.getOutputStream());
		dos.write(informe);
		dos.flush();
		dos.close();
	}

}

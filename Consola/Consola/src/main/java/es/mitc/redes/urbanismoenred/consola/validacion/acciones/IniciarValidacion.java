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

import java.io.IOException;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import es.mitc.redes.urbanismoenred.consola.validacion.IAccion;
import es.mitc.redes.urbanismoenred.servicios.seguridad.Usuario;
import es.mitc.redes.urbanismoenred.servicios.validacion.ContextoValidacion;
import es.mitc.redes.urbanismoenred.servicios.validacion.Estado;
import es.mitc.redes.urbanismoenred.servicios.validacion.GestorContextosValidacionLocal;
import es.mitc.redes.urbanismoenred.servicios.validacion.TipoValidacion;
import es.mitc.redes.urbanismoenred.servicios.validacion.ValidacionFipLocal;

/**
 * @author Arnaiz Consultores
 *
 */
@Stateless( name = "START_VALIDATION")
public class IniciarValidacion implements IAccion {
	
	private static final Logger log = Logger.getLogger(IniciarValidacion.class);
	
	@EJB
	private ValidacionFipLocal servicioValidacion;
	
	@EJB 
	private GestorContextosValidacionLocal gestorContextos;

	/* (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.consola.validacion.IAccion#ejecutar(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public void ejecutar(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		if (request.getParameter("tramite") != null) {
			
			ContextoValidacion contexto = gestorContextos.getContexto(request.getParameter("tramite"));
			
			if (contexto != null) {
				Usuario usuario = (Usuario)request.getSession().getAttribute("usuario");
				Usuario usuarioContexto = (Usuario) contexto.getParametro(ContextoValidacion.USUARIO);
				if (usuarioContexto == null || usuarioContexto.getNombre().equals(usuario.getNombre())) {
					contexto.putParametro(ContextoValidacion.TIPO_VALIDACION, TipoValidacion.TODAS);
					contexto.putParametro(ContextoValidacion.ERROR, null);
					
					try {
						contexto.putParametro(ContextoValidacion.ESTADO_INICIAL, contexto.getEstado());
						contexto.putParametro(ContextoValidacion.USUARIO, usuario);
						servicioValidacion.validar(contexto);
					} catch (Exception e) {
						log.warn("Error durante el proceso de validación: " + e.getMessage());
						contexto.setEstado((Estado)contexto.getParametro(ContextoValidacion.ESTADO_INICIAL));
						contexto.putParametro(ContextoValidacion.PROGRESO, null);
						contexto.putParametro(ContextoValidacion.ERROR, e.getMessage());
					}
					contexto.putParametro(ContextoValidacion.USUARIO, null);
				} else {
					log.warn("El contexto pertenece al usuario " + usuarioContexto.getNombre() + " no puede ser iniciado por el usuario " + usuario.getNombre());
				}
			} else {
				log.warn("No hay contexto para el FIP " + request.getParameter("tramite") +". Ignorando solicitud.");
			}
			
			
		} else {
			log.warn("No se ha especificado trámite. Ignorando solicitud.");
		}
	}

}

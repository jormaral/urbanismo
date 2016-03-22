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
package es.mitc.redes.urbanismoenred.consola.planeamiento.acciones;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;

import es.mitc.redes.urbanismoenred.consola.planeamiento.IAccion;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Boletintramite;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Tramite;
import es.mitc.redes.urbanismoenred.servicios.dal.ExcepcionPersistencia;
import es.mitc.redes.urbanismoenred.servicios.planeamiento.ExcepcionPlaneamiento;
import es.mitc.redes.urbanismoenred.servicios.planeamiento.ServicioPlaneamientoLocal;
import es.mitc.redes.urbanismoenred.servicios.seguridad.ServicioDiarioLocal;
import es.mitc.redes.urbanismoenred.servicios.seguridad.TipoSubsistema;
import es.mitc.redes.urbanismoenred.servicios.seguridad.Usuario;

/**
 * @author Arnaiz Consultores
 *
 */
@Stateless( name = "MODIFICAR_PUBLICACION")
public class ModificarPublicacion implements IAccion {

	private static final Logger log = Logger.getLogger(BorrarPublicacion.class);
	
	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	
	@EJB
	private ServicioPlaneamientoLocal servicioPlaneamiento;
	
	@EJB
	private ServicioDiarioLocal servicioDiario;
	
	@Resource
    private SessionContext sessionContext;

	/* (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.consola.planeamiento.IAccion#ejecutar(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public void ejecutar(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String id = request.getParameter("id");
		String idTramite = request.getParameter("idTramite");
		String idBoletin = request.getParameter("idBoletin");
		String fecha = request.getParameter("fecha");
		String numero = request.getParameter("numero");

		Map<String, Object> data = new HashMap<String, Object>();
		data.put("id", id);
		if (id != null) {
			
			try {
				Boletintramite publicacion = servicioPlaneamiento.get(Boletintramite.class, Integer.parseInt(id));
				if (publicacion != null) {
					if (idTramite != null) {
						Tramite tramite = servicioPlaneamiento.get(Tramite.class, Integer.parseInt(idTramite));
						if (tramite != null) {
							publicacion.setTramite(tramite);
						} else {
							throw new ExcepcionPlaneamiento("No se ha localizado el trámite indicado.");
						}
					}
					
					if (idBoletin != null) {
						publicacion.setIdboletin(Integer.parseInt(idBoletin));
					}
					
					if (fecha != null) {
						publicacion.setFecha(sdf.parse(fecha));
					}
					
					publicacion.setNumero(numero);
					
					servicioDiario.nuevoRegistroDiario(((Usuario)request.getSession().getAttribute("usuario")).getNombre()
							, TipoSubsistema.PLANBASE
							, "Modificación de la publicación " + id);
					data.put("error", false);
				} else {
					data.put("error", true);
					data.put("result", "La publicación no existen en base de datos");
				}
				
			} catch (NumberFormatException e) {
				data.put("error", true);
				data.put("result", e.getMessage());
				sessionContext.setRollbackOnly();
			} catch (ExcepcionPersistencia e) {
				log.warn("No se pudo guardar el registro en el diario. " + e.getMessage());
			} catch (ParseException e) {
				data.put("error", true);
				data.put("result", "No se pudo procesar la fecha correctamente.");
				sessionContext.setRollbackOnly();
			} catch (ExcepcionPlaneamiento e) {
				data.put("error", true);
				data.put("result", e.getMessage());
			}
		} else {
			data.put("error", true);
        	data.put("result", "Solicitud incompleta");
		}
		
		ObjectMapper om = new ObjectMapper();
        om.writeValue(response.getWriter(), data);
	}

}

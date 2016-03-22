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
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;

import es.mitc.redes.urbanismoenred.consola.planeamiento.IAccion;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidad;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Plan;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Tramite;
import es.mitc.redes.urbanismoenred.servicios.dal.GestorConsultasLocal;
import es.mitc.redes.urbanismoenred.servicios.planeamiento.ServicioPlaneamientoLocal;

/**
 * @author Arnaiz Consultores
 *
 */
@Stateless(name="GUARDAR_ENT_PLAN_BASE")
public class GuardarEntidadPlanBase implements IAccion {
	
	private static final Logger log = Logger.getLogger(GuardarEntidadPlanBase.class);
	
	@Resource(name = "java:/ConnectionFactory")
	private ConnectionFactory connectionFactory;
	
	@Resource(mappedName = "java:/queue/CambiosBD")
	private static Queue queue;
	
	@EJB 
	private GestorConsultasLocal gestorConsultas;
	@EJB
	private ServicioPlaneamientoLocal servicioPlaneamiento;

	/* (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.consola.planeamiento.IAccion#ejecutar(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public void ejecutar(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String idEnt = request.getParameter("idEnt");
		String idTramite = request.getParameter("idTramite");
		String etiqueta = request.getParameter("txtEtiqueta");
		String clave = request.getParameter("txtClave");
		String nombre = request.getParameter("txtNombre");
		String padre = request.getParameter("idEntPadre");
		
		Map<String, Object> data = new HashMap<String, Object>();
        
        
        data.put("affectedId", null);
        
        if (idEnt != null && !idEnt.isEmpty()) {
			// Es una modificación, por lo que cambio aquellos valores que pueden ser modificados.
			Entidad entidad = servicioPlaneamiento.get(Entidad.class,Integer.parseInt(idEnt));
			if (entidad != null) {
				entidad.setEtiqueta(etiqueta);
				entidad.setNombre(nombre);
				entidad.setClave(clave);
				data.put("affectedId", entidad.getIden());
				
				notificarModificacion(entidad.getTramite().getPlan().getIdambito());
			} else {
				throw new IOException("No se ha encontrado la entidad con identificador " + idEnt);
			}
		} else {
			// Es un nuevo objeto.
			Tramite tramitePlanBase = null;
			if (idTramite != null && !idTramite.isEmpty()) {
				tramitePlanBase = servicioPlaneamiento.get(Tramite.class,Integer.parseInt(idTramite));
			} else {
				// buscamos un plan base y cogemos un trámite. La consulta ya los devuelve ordenados.
				Plan[] planesBase = servicioPlaneamiento.getPlanesBase();
				if (planesBase.length>0) {
					for (Tramite tramite : planesBase[planesBase.length-1].getTramites()) {
						if (tramitePlanBase == null) {
							tramitePlanBase = tramite;
						} else {
							if (tramitePlanBase.getFecha().before(tramite.getFecha())) {
								tramitePlanBase = tramite;
							}
						}
					}
				} else {
					throw new IOException("No se han encontrado planes base.");
				}
			}
			
			Entidad entidad = new Entidad();
			entidad.setCodigo(gestorConsultas.getSiguienteCodigoEntidad(tramitePlanBase.getIden()));
			entidad.setBsuspendida(false);
			entidad.setTramite(tramitePlanBase);
			entidad.setEtiqueta(etiqueta);
			entidad.setNombre(nombre);
			entidad.setClave(clave);
			
			entidad.setEntidadByIdpadre(padre != null && !padre.isEmpty()? servicioPlaneamiento.get(Entidad.class, Integer.parseInt(padre)):null);
			
			servicioPlaneamiento.guardar(entidad);
			
			notificarModificacion(tramitePlanBase.getPlan().getIdambito());
			data.put("affectedId", entidad.getIden());
		}
		
		ObjectMapper om = new ObjectMapper();
        om.writeValue(response.getWriter(), data);
	}
	
	/**
	 * @param ambito 
	 * 
	 */
	private void notificarModificacion(int ambito) {
		try {
			Connection conexion = connectionFactory.createConnection();
			
			Session sesion = conexion.createSession(true, Session.AUTO_ACKNOWLEDGE);
			
			MessageProducer productor = sesion.createProducer(queue);
			
			MapMessage mensaje = sesion.createMapMessage();
			
			mensaje.setStringProperty("DatosModificados", "PlanBase");
			mensaje.setInt("Ambito", ambito);
			productor.send(mensaje);
			
			sesion.commit();
			
			sesion.close();
			conexion.close();
			
		} catch (JMSException e) {
			log.warn("No se ha podido notificar el cambio en el diccionario: " + e.getMessage());
		}
	}

}

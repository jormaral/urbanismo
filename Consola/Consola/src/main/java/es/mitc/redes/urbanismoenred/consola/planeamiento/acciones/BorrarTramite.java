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

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;

import es.mitc.redes.urbanismoenred.consola.planeamiento.IAccion;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Tramite;
import es.mitc.redes.urbanismoenred.servicios.comunes.GestionIntroduccionFIPenSistemaLocal;
import es.mitc.redes.urbanismoenred.servicios.planeamiento.ExcepcionPlaneamiento;
import es.mitc.redes.urbanismoenred.servicios.planeamiento.ServicioPlaneamientoLocal;
import es.mitc.redes.urbanismoenred.utils.excepciones.RedesException;

/**
 * @author Arnaiz Consultores
 *
 */
@Stateless(name = "BORRAR_TRAMITE")
public class BorrarTramite implements IAccion {
	
	@EJB
	private GestionIntroduccionFIPenSistemaLocal gestorFips;
	
	@EJB
	private ServicioPlaneamientoLocal servicioPlaneamiento;
	
	/**
	 * Borra un directorio que contiene un FIP.
	 * 
	 * @param root
	 * @return
	 */
	private boolean borrarDirectorio(File root) {
		boolean exito = true;
		
		if (root.isDirectory()) {
			File[] children = root.listFiles();
			for(int i = 0; i < children.length; i++) {
				if (children[i].isDirectory()) {
					exito &= borrarDirectorio(children[i]);
				} else {
					exito &= children[i].delete();
				}
			}
		}
		exito &= root.delete();
		
		return exito;
	}

	/* (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.consola.planeamiento.IAccion#ejecutar(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public void ejecutar(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String idTramite = request.getParameter("idTramite");

		Map<String, Object> data = new HashMap<String, Object>();

		if (idTramite != null) {
			
			try {
				Tramite tramite = servicioPlaneamiento.get(Tramite.class,Integer.parseInt(idTramite));
				if (tramite != null) {
					servicioPlaneamiento.borrar(tramite);
					
					try {
						String fip = gestorFips.getUltimaVersionFIP(tramite.getCodigofip());
						File archivoFip = new File(fip);
						// Borramos el directorio completo del trámite
						File directorioTramite = archivoFip.getParentFile().getParentFile();
						
						// Comprobación de seguridad para no borrar lo que no es
						if (directorioTramite.getName().equalsIgnoreCase(tramite.getCodigofip())) {
							borrarDirectorio(directorioTramite);
						}
					} catch (RedesException e) {
						// No se ha encontrado archivo FIP, por lo que no hay nada
						// que borrar.
					}
					
					data.put("exito", true);
					data.put("mensaje", "Trámite " + tramite.getCodigofip() + " borrado");
				} else {
					data.put("exito", false);
					data.put("mensaje", "No existe trámite con id " + idTramite);
				}
				
			} catch (NumberFormatException e) {
				data.put("exito", false);
				data.put("mensaje", e.getMessage());
			} catch (ExcepcionPlaneamiento e) {
				data.put("exito", false);
				data.put("mensaje", e.getMessage());
			}
		} else {
			data.put("exito", false);
        	data.put("mensaje", "Solicitud incompleta");
		}
		
		ObjectMapper om = new ObjectMapper();
        om.writeValue(response.getWriter(), data);

	}

}

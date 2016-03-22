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

import java.io.File;
import java.io.IOException;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.log4j.Logger;

import es.mitc.redes.urbanismoenred.servicios.validacion.ContextoValidacion;
import es.mitc.redes.urbanismoenred.servicios.validacion.Estado;
import es.mitc.redes.urbanismoenred.servicios.validacion.GestorContextosValidacionLocal;
import es.mitc.redes.urbanismoenred.servicios.validacion.ValidacionFipLocal;
import es.mitc.redes.urbanismoenred.utils.recursos.textos.Textos;
import es.mitc.redes.urbanismoenred.consola.validacion.IAccion;

/**
 * Acción que carga un fichero FIP enviado desde un navegador.
 * 
 * @author Arnaiz Consultores
 *
 */
@Stateless ( name = "UPLOAD_FIP_FILE")
public class SubirFIP implements IAccion {
	
	private static final Logger log = Logger.getLogger(SubirFIP.class);
	
	private final String fipPath = System.getenv("REDES_PATH") + Textos.getTexto("rutas", "FipsPath");
	
	@EJB
	private ValidacionFipLocal servicioValidacion;
	
	@EJB
	private GestorContextosValidacionLocal gestorContextos;

	/**
	 * 
	 */
	public SubirFIP() {
		
	}

	/* (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.validacion.IAccion#ejecutar(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public void ejecutar(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		
		
		if (request.getParameter("tramite") != null) {
			ContextoValidacion contexto = gestorContextos.getContexto(request.getParameter("tramite"));
			if (contexto != null) {
				if (request.getAttribute("txtFile") != null) {
					if (request.getAttribute("txtFile") instanceof FileItem) {
						FileItem archivo = (FileItem)request.getAttribute("txtFile");

						File directorio = new File(fipPath);
						if (!directorio.exists()) {
							if (directorio.mkdirs()) {
								log.debug("Directorio " + directorio.getAbsolutePath() + " creado");
							} else {
								log.warn("Directorio " + directorio.getAbsolutePath() + " no ha podido ser creado.");
							}
						}
					        
						File f = new File(fipPath, archivo.getName());
						contexto.putParametro(ContextoValidacion.ERROR, null);
						contexto.putParametro(ContextoValidacion.RUTA_FIP, f.getAbsolutePath());
						contexto.putParametro(ContextoValidacion.USUARIO, request.getSession().getAttribute("usuario"));
						
						try {
							// Una vez subido el archivo inicio el proceso de despliegue
							if (contexto.getEstado().equals(Estado.PENDIENTE) || 
									contexto.getEstado().equals(Estado.SUBIENDO) || 
									contexto.getEstado().equals(Estado.GUARDADO) ||
									contexto.getEstado().equals(Estado.VALIDACION_ERRONEA) ||
									contexto.getEstado().equals(Estado.VALIDADO)) {
								if (contexto.getParametro(ContextoValidacion.ESTADO_INICIAL) == null) {
									contexto.putParametro(ContextoValidacion.ESTADO_INICIAL, contexto.getEstado());
								}
								contexto.setEstado(Estado.SUBIDO);
								
								archivo.write(f);
								
								log.debug("Archivo subido, invocando despliegue...");
								
								servicioValidacion.desplegarFip(contexto);
								// Una vez desplegado el FIP vuelvo a liberar la propiedad del contexto.
								contexto.putParametro(ContextoValidacion.USUARIO, null);
							} else {
								log.warn("El contexto del trámite " + contexto.getCodigoFip() + " no se encuentra en un estado que permita incorporar un nuevo FIP. Ignorando archivo recibido");
							}
							
						} catch (Exception e) {
							log.error("Error al copiar archivo descargado desde el directorio temporal: " + e.getMessage(),e);
							if (contexto.getParametro(ContextoValidacion.ESTADO_INICIAL) != null)
								contexto.setEstado((Estado)contexto.getParametro(ContextoValidacion.ESTADO_INICIAL));
							contexto.putParametro(ContextoValidacion.ERROR, e.getMessage());
							contexto.putParametro(ContextoValidacion.USUARIO, null);
							contexto.putParametro(ContextoValidacion.PROGRESO, null);
						}
					} else {
						log.warn("El parámetro pasado no es un archivo.");
					}
				} else {
					log.warn("No se ha recibido archivo FIP");
				}
			} else {
				log.warn("No existe contexto para el trámite " + request.getParameter("tramite"));
			}
		} else {
			log.warn("No se ha especificado trámite.");
		}
		
	}

}

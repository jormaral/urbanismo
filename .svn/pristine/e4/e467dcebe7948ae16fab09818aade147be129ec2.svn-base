/*
 * Copyright 2011 red.es
 * Autores: IDOM Consulting
 *
 * Licencia con arreglo a la EUPL, Versi�n 1.1 o -en cuanto
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

package com.mitc.redes.editorfip.servicios.produccionfip.gestionadscripciones;

import javax.ejb.Remove;
import javax.ejb.Stateful;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.faces.FacesManager;
import org.jboss.seam.international.StatusMessages;
import org.jboss.seam.log.Log;

import com.mitc.redes.editorfip.entidades.interfazgrafico.AdscripcionesDTO;
import com.mitc.redes.editorfip.entidades.interfazgrafico.AdscripcionesTramiteDTO;
import com.mitc.redes.editorfip.entidades.interfazgrafico.UnidadTramiteDTO;
import com.mitc.redes.editorfip.servicios.basicos.fip.adscripciones.ServicioBasicoAdscripciones;
import com.mitc.redes.editorfip.servicios.genericos.JsfUtil;
import com.mitc.redes.editorfip.servicios.produccionfip.gestionentidades.GestionArbolEntidadesPadre;
import com.mitc.redes.editorfip.servicios.sesion.VariablesSesionUsuario;

@Stateful
@Scope(ScopeType.SESSION)
@Name("gestionAdscripciones")
public class GestionAdscripcionesBean implements GestionAdscripciones
{

	@Logger private Log log;
	@In(create = true, required = false) ServicioBasicoAdscripciones servicioBasicoAdscripciones;
	@In(create = true, required = false) ListaAdscripciones listaAdscripciones;
	@In(create = true, required = false) GestionArbolEntidadesPadre gestionArbolEntidadesPadre;
	@In(create = true, required = false) VariablesSesionUsuario variablesSesionUsuario;
	@In StatusMessages statusMessages;

	private AdscripcionesDTO adscripcionesDTO = null;
	private boolean mostrarPanelEntidadDestino = true;
	private boolean mostrarPanelEntidadOrigen = true;
	private boolean mostrarPanelTipoAdscripcion = true;
	private boolean mostrarPanelUnidadAdscripcion = true;


	public void nuevaAdscripcion()
	{
		log.info("[nuevaAdscripcion] nuevaAdscripcion");
		adscripcionesDTO = new AdscripcionesDTO();
	}


	public void verAdscripcion(int idAdscripcion, String ruta)
	{
		log.info("[verAdscripcion] idAdscripcion="+idAdscripcion);
		if (idAdscripcion!=0){

			adscripcionesDTO = servicioBasicoAdscripciones.buscarAdscripcion(new Integer(idAdscripcion));	

			statusMessages.addFromResourceBundleOrDefault("adscripcion_seleccionada_ok","Adscripcion seleccionada correctamente");

		} else
		{
			statusMessages.addFromResourceBundleOrDefault("adscripcion_seleccionada_fallo","No hay Adscripcion seleccionada");
		}

		FacesManager.instance().redirect(ruta);
	}


	public void borrarAdscripcion(int idAdscripcion)
	{

		log.info("[borrarAdscripcion] idAdscripcion="+idAdscripcion);

		try
		{
			if (idAdscripcion!=0){

				servicioBasicoAdscripciones.borrarAdscripcion(new Integer(idAdscripcion));

				// Recargo la lista con la adscripcion borrada
				listaAdscripciones.refrescarLista();

				statusMessages.addFromResourceBundleOrDefault("adscripcion_borrada_ok","Adscripcion borrada correctamente");
				log.info("[borrarAdscripcion] Adscripcion borrada correctamente");
			} else
			{

				statusMessages.addFromResourceBundleOrDefault("adscripcion_seleccionada_fallo","No hay Adscripcion seleccionada");
				log.info("[borrarAdscripcion] No hay Adscripcion seleccionada");
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			log.error("[guardarAdscripcion] ERROR borrarAdscripcion ");
			statusMessages.addFromResourceBundleOrDefault("adscripcion_borrada_fallo","Adscripcion borrada fallo");

		}



	}



	public void guardarAdscripcion()
	{

		log.info("[guardarAdscripcion] Inicio");

		try {

			// Si no es nulo estoy editando
			if (adscripcionesDTO!=null && adscripcionesDTO.getIdRelacion()!=0){

				statusMessages.addFromResourceBundleOrDefault("adscripcion_noedicion","No esta implementada la edicion");

				log.info("[guardarAdscripcion] No esta implementada la edicion");

				// Si es nula es porque estoy creando uno nuevo
			} else {

				// Guardo la adscripcion
				int idRelacion = servicioBasicoAdscripciones.crearAdscripcion(adscripcionesDTO, variablesSesionUsuario.getIdTramiteEncargadoTrabajo());

				adscripcionesDTO.setIdRelacion(idRelacion);

				// Recargo la lista con la nueva adscripcion
				listaAdscripciones.refrescarLista();

				statusMessages.addFromResourceBundleOrDefault("adscripcion_creada_ok","Adscripcion creada correctamente");
				log.info("[guardarAdscripcion] Adscripcion creada correctamente");

			}

		}
		catch (Exception e) {
			e.printStackTrace();
			log.error("[guardarAdscripcion] ERROR Adscripcion creada ");
			statusMessages.addFromResourceBundleOrDefault("adscripcion_creada_fallo","Error al crear la Adscripcion");

		}


	}

	public AdscripcionesDTO getAdscripcionesDTO() {
		return adscripcionesDTO;
	}



	public void setAdscripcionesDTO(AdscripcionesDTO adscripcionesDTO) {
		this.adscripcionesDTO = adscripcionesDTO;
	}


	// ---------
	// Carga de datos
	// --------
	public void cargarDatosEntidadOrigen(int idEntidadOrigen, String nombreEntidadOrigen)
	{

		log.info("[cargarDatosEntidadOrigen] idEntidadOrigen="+idEntidadOrigen+" nombreEntidadOrigen="+nombreEntidadOrigen);
		this.adscripcionesDTO.setIdEntidadOrigen(idEntidadOrigen);
		this.adscripcionesDTO.setNombreEntidadOrigen(nombreEntidadOrigen);

		mostrarPanelEntidadOrigen= false;
	}

	public void cargarDatosEntidadDestino(int idEntidadDestino, String nombreEntidadDestino)
	{
		log.info("[cargarDatosEntidadDestino] idEntidadDestino="+idEntidadDestino+" nombreEntidadDestino="+nombreEntidadDestino);

		this.adscripcionesDTO.setIdEntidadDestino(idEntidadDestino);
		this.adscripcionesDTO.setNombreEntidadDestino(nombreEntidadDestino);

		mostrarPanelEntidadDestino=false;
	}



	public void cargarDatosDeterminacionUnidad(UnidadTramiteDTO unidad, String volverPag)
	{
		int idDeterminacionUnidad = unidad.getIdDeterminacion();
		String nombreDeterminacionUnidad = unidad.getNombreDeterminacion();

		log.info("[cargarDatosDeterminacionUnidad] idDeterminacionUnidad="+idDeterminacionUnidad+" nombreDeterminacionUnidad="+nombreDeterminacionUnidad);


		this.adscripcionesDTO.setIdDeterminacionUnidad(idDeterminacionUnidad);
		this.adscripcionesDTO.setNombreDeterminacionUnidad(nombreDeterminacionUnidad);

		mostrarPanelUnidadAdscripcion=false;

		FacesManager.instance().redirect(volverPag);
	}

	public void cargarDatosDeterminacionTipo(AdscripcionesTramiteDTO tipoAdscripcion, String volverPag)
	{
		int idDeterminacionTipo = tipoAdscripcion.getIdDeterminacion();
		String nombreDeterminacionTipo = tipoAdscripcion.getNombreDeterminacion();


		log.info("[cargarDatosDeterminacionTipo] idDeterminacionTipo="+idDeterminacionTipo+" nombreDeterminacionTipo="+nombreDeterminacionTipo);

		this.adscripcionesDTO.setIdDeterminacionTipo(idDeterminacionTipo);
		this.adscripcionesDTO.setNombreDeterminacionTipo(nombreDeterminacionTipo);

		mostrarPanelTipoAdscripcion = false;

		FacesManager.instance().redirect(volverPag);
	}


	// Paneles POPUP
	public boolean isMostrarPanelEntidadDestino() {
		return mostrarPanelEntidadDestino;
	}


	public void setMostrarPanelEntidadDestino(boolean mostrarPanelEntidadDestino) {

		log.info("[setMostrarPanelEntidadDestino] ");

		// Reinicio el arbol
		gestionArbolEntidadesPadre.setModel(null);
		this.mostrarPanelEntidadDestino = mostrarPanelEntidadDestino;
	}


	public boolean isMostrarPanelEntidadOrigen() {
		return mostrarPanelEntidadOrigen;
	}


	public void setMostrarPanelEntidadOrigen(boolean mostrarPanelEntidadOrigen) {

		log.info("[setMostrarPanelEntidadDestino] ");
		// Reinicio el arbol
		gestionArbolEntidadesPadre.setModel(null);

		this.mostrarPanelEntidadOrigen = mostrarPanelEntidadOrigen;
	}



	public boolean isMostrarPanelTipoAdscripcion() {
		return mostrarPanelTipoAdscripcion;
	}


	public void setMostrarPanelTipoAdscripcion(boolean mostrarPanelTipoAdscripcion) {
		this.mostrarPanelTipoAdscripcion = mostrarPanelTipoAdscripcion;
	}


	public boolean isMostrarPanelUnidadAdscripcion() {
		return mostrarPanelUnidadAdscripcion;
	}


	public void setMostrarPanelUnidadAdscripcion(
		boolean mostrarPanelUnidadAdscripcion) {
		this.mostrarPanelUnidadAdscripcion = mostrarPanelUnidadAdscripcion;
	}


	@Remove
	public void destroy() {
	}
}

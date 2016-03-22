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

package com.mitc.redes.editorfip.servicios.produccionfip.gestionoperaciones;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.faces.model.SelectItem;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.web.RequestParameter;
import org.jboss.seam.faces.FacesManager;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.international.StatusMessages;
import org.jboss.seam.international.StatusMessage.Severity;
import org.jboss.seam.log.Log;

import com.mitc.redes.editorfip.entidades.interfazgrafico.AdscripcionesDTO;
import com.mitc.redes.editorfip.entidades.interfazgrafico.AdscripcionesTramiteDTO;
import com.mitc.redes.editorfip.entidades.interfazgrafico.UnidadTramiteDTO;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Determinacion;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Entidad;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Operacion;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Operaciondeterminacion;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Operacionentidad;
import com.mitc.redes.editorfip.servicios.basicos.fip.determinaciones.ServicioBasicoDeterminaciones;
import com.mitc.redes.editorfip.servicios.basicos.fip.entidades.ServicioBasicoEntidades;
import com.mitc.redes.editorfip.servicios.basicos.fip.operaciones.ServicioBasicoOperaciones;
import com.mitc.redes.editorfip.servicios.genericos.JsfUtil;
import com.mitc.redes.editorfip.servicios.sesion.VariablesSesionUsuario;

@Scope(ScopeType.SESSION)
@Stateful
@Name("gestionOperaciones")
public class GestionOperacionesBean implements GestionOperaciones {

	@Logger private Log logger;
	@In StatusMessages statusMessages;
	@In FacesMessages facesMessages;
	@In (create = true, required = false) VariablesSesionUsuario variablesSesionUsuario;
	@In(create = true, required = false) ServicioBasicoOperaciones servicioBasicoOperaciones;
	@In(create = true, required = false) ServicioBasicoEntidades servicioBasicoEntidades;
	@In(create = true, required = false) ServicioBasicoDeterminaciones servicioBasicoDeterminaciones;
	@RequestParameter("idOperacion") Integer idOperacion;


	private Operacionentidad operacionEntidad = null;
	private Operaciondeterminacion operacionDeterminacion = null;
	private int idTipoOperacionEntidad = 0;
	private int idTipoOperacionDeterminacion = 0;
	private boolean mostrarPopUpEntidadOperadora;
	private boolean mostrarPopUpEntidadOperada;
	private boolean mostrarPopUpDeterminacionOperadora;
	private boolean mostrarPopUpDeterminacionOperada;
	
	private AdscripcionesDTO adscripcionesDTO = new AdscripcionesDTO();
	private boolean mostrarPanelTipoAdscripcion = true;
	private boolean mostrarPanelUnidadAdscripcion = true;
	
	
	// -------
	// Adscripciones
	// -----
	
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
	
	public void cargarDatosDeterminacionUnidad(UnidadTramiteDTO unidad, String volverPag)
	{
		int idDeterminacionUnidad = unidad.getIdDeterminacion();
		String nombreDeterminacionUnidad = unidad.getNombreDeterminacion();

		logger.info("[cargarDatosDeterminacionUnidad] idDeterminacionUnidad="+idDeterminacionUnidad+" nombreDeterminacionUnidad="+nombreDeterminacionUnidad);


		this.adscripcionesDTO.setIdDeterminacionUnidad(idDeterminacionUnidad);
		this.adscripcionesDTO.setNombreDeterminacionUnidad(nombreDeterminacionUnidad);

		mostrarPanelUnidadAdscripcion=false;

		FacesManager.instance().redirect(volverPag);
	}

	public void cargarDatosDeterminacionTipo(AdscripcionesTramiteDTO tipoAdscripcion, String volverPag)
	{
		int idDeterminacionTipo = tipoAdscripcion.getIdDeterminacion();
		String nombreDeterminacionTipo = tipoAdscripcion.getNombreDeterminacion();


		logger.info("[cargarDatosDeterminacionTipo] idDeterminacionTipo="+idDeterminacionTipo+" nombreDeterminacionTipo="+nombreDeterminacionTipo);

		this.adscripcionesDTO.setIdDeterminacionTipo(idDeterminacionTipo);
		this.adscripcionesDTO.setNombreDeterminacionTipo(nombreDeterminacionTipo);

		mostrarPanelTipoAdscripcion = false;

		FacesManager.instance().redirect(volverPag);
	}
	
	public void nuevaAdscripcion()
	{
		logger.info("[nuevaAdscripcion] nuevaAdscripcion");
		adscripcionesDTO = new AdscripcionesDTO();
	}

	
	public AdscripcionesDTO getAdscripcionesDTO() {
		return adscripcionesDTO;
	}



	public void setAdscripcionesDTO(AdscripcionesDTO adscripcionesDTO) {
		this.adscripcionesDTO = adscripcionesDTO;
	}


	// ------------------------------
	// OPERACIONES ENTIDAD
	// ------------------------------

	public String nuevoOperacionEntidad()
	{
		String resultado = "";
		
		
		if (idOperacion != null)
		{
			if(idOperacion != 0) {
				logger.info("[nuevoOperacionEntidad] OPERACION CARGADA PARA EDITAR", null);
				operacionEntidad = servicioBasicoOperaciones.findOperacionentidad(idOperacion);
				resultado="editarOperacionEntidad";
			} else {
				logger.info("[nuevoOperacionEntidad] OPERACION NUEVA CARGADA", null);
				operacionEntidad = new Operacionentidad();
				resultado="crearOperacionEntidad";
			}
		}
		else
		{
			if (operacionEntidad==null)
			{
				logger.info("[nuevoOperacionEntidad] OPERACION NUEVA CARGADA", null);
				operacionEntidad = new Operacionentidad();
				resultado="crearOperacionEntidad";
			}
		}
		
		

		return resultado;
	}

	public String borrarOperacionEntidad(int idOpEnt){

		if (idOpEnt!=0){
			Operacionentidad opEnt = servicioBasicoOperaciones.findOperacionentidad(new Integer(idOpEnt));

			// Tengo que eliminar tambien la Operacion, cojo el id de la Operacion
			Operacion op = opEnt.getOperacion();

			// Borro la OperacionEntidad
			servicioBasicoOperaciones.removeOperacionentidad(opEnt);


			// Borro la Operacion
			servicioBasicoOperaciones.removeOperacion(op);


			// Tendria que recargar la lista de OperacionEntidadLista que es de request


			JsfUtil.addSuccessMessage("Operación Entidad borrada correctamente");
		} else
			JsfUtil.addErrorMessage("No hay Operación Entidad seleccionada");

		return "gestionOperaciones";

	}

	public String guardarOperacionEntidad(){

		Operacion op = null;
		Operacionentidad opEnt = operacionEntidad;
		try{
			// Valido la operacion entidad
			if(validarOperacionEntidad(opEnt)) {

				// Si no es nulo estoy editando
				if (opEnt!=null && opEnt.getIden()!=0){

					op = servicioBasicoOperaciones.findOperacion(new Integer(opEnt.getOperacion().getIden()));

					if (op==null){ // La Operacion no existe => la creo

						op = new Operacion();

						op.setTramite(variablesSesionUsuario.getTramiteEncargadoTrabajo());


						servicioBasicoOperaciones.createOperacion(op);
					} else { // La operacion ya existe => solo modifico Operacion Entidad


						op.setTramite(variablesSesionUsuario.getTramiteEncargadoTrabajo());
						opEnt.setIdtipooperacionent(idTipoOperacionEntidad);
						opEnt.setOperacion(op);

						servicioBasicoOperaciones.editOperacionentidad(opEnt);

					}
					// Si es nula es porque estoy creando uno nuevo
				} else {

					// 1) Creo la operacion
					op = new Operacion();

					op.setTramite(variablesSesionUsuario.getTramiteEncargadoTrabajo());


					servicioBasicoOperaciones.createOperacion(op);

					// 2) Creo la Operacion Entidad


					opEnt.setIdtipooperacionent(idTipoOperacionEntidad);
					opEnt.setOperacion(op);

					servicioBasicoOperaciones.createOperacionentidad(opEnt);

					// Tendria que recargar la lista de operaciones

					JsfUtil.addSuccessMessage("Operación Entidad creada correctamente");

				}

				FacesManager.instance().redirect("/produccionfip/gestionoperaciones/listaOperacionesEntidad.xhtml");
			}


		}
		catch (Exception e) {
			e.printStackTrace();
		}

		return "crearOperacionEntidad";

	}

	public List<SelectItem> tipoOperacionEntidad(){

		List<Object[]> elementos = servicioBasicoOperaciones.tipoOperacionEntidad();

		List<SelectItem> res = new ArrayList<SelectItem>();
		for (Object[] c : elementos) {
			SelectItem i = new SelectItem();
			i.setLabel((String) c[1]);
			i.setValue(c[0]);
			res.add(i);
		}

		return res;

	}

	public void seleccionarEntidadOperadora(int idEntidad) {
		try {
			Entidad entidadOperadora = servicioBasicoEntidades.buscarEntidad(idEntidad);
			operacionEntidad.setEntidadByIdentidadoperadora(entidadOperadora);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			setMostrarPopUpEntidadOperadora(false);
		}	
	}

	public void seleccionarEntidadOperada(int idEntidad) {
		try {
			Entidad entidadOperada = servicioBasicoEntidades.buscarEntidad(idEntidad);
			operacionEntidad.setEntidadByIdentidad(entidadOperada);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			setMostrarPopUpEntidadOperada(false);
		}	
	}



	private boolean validarOperacionEntidad(Operacionentidad opEnt) {
		boolean valida = true;

		if(opEnt != null) {

			if(opEnt.getEntidadByIdentidad() == null || opEnt.getEntidadByIdentidad().getIden() == 0) {
				facesMessages.addFromResourceBundle(Severity.ERROR, "operacion_no_entidad_operada", null);
				valida = false;
			} else if(opEnt.getEntidadByIdentidadoperadora() == null || opEnt.getEntidadByIdentidadoperadora().getIden() == 0) {
				facesMessages.addFromResourceBundle(Severity.ERROR, "operacion_no_entidad_operadora", null);
				valida = false;
			}

		}

		return valida;
	}

	public Operacionentidad getOperacionEntidad() {
		return operacionEntidad;
	}

	public void setOperacionEntidad(Operacionentidad operacionEntidad) {
		this.operacionEntidad = operacionEntidad;
	}

	public int getIdTipoOperacionEntidad() {
		
		if (operacionEntidad.getOperacion()!=null)
		{
			
			idTipoOperacionEntidad = operacionEntidad.getIdtipooperacionent();
		}
		return idTipoOperacionEntidad;
	}

	public void setIdTipoOperacionEntidad(int idTipoOperacionEntidad) {
		this.idTipoOperacionEntidad = idTipoOperacionEntidad;
	}

	public boolean isMostrarPopUpEntidadOperadora() {
		return mostrarPopUpEntidadOperadora;
	}

	public void setMostrarPopUpEntidadOperadora(boolean mostrarPopUpEntidadOperadora) {
		this.mostrarPopUpEntidadOperadora = mostrarPopUpEntidadOperadora;
	}

	public boolean isMostrarPopUpEntidadOperada() {
		return mostrarPopUpEntidadOperada;
	}

	public void setMostrarPopUpEntidadOperada(boolean mostrarPopUpEntidadOperada) {
		this.mostrarPopUpEntidadOperada = mostrarPopUpEntidadOperada;
	}

	// ------------------------------
	// OPERACIONES DETERMINACION
	// ------------------------------

	public String nuevoOperacionDeterminacion(){

		String resultado = "";

		if(idOperacion != 0) {
			logger.info("[nuevoOperacionDeterminacion] OPERACION CARGADA PARA EDITAR", null);
			resultado = "crearOperacionDeterminacion";
			operacionDeterminacion = servicioBasicoOperaciones.findOperaciondeterminacion(idOperacion);
		} else {
			logger.info("[nuevoOperacionDeterminacion] OPERACION NUEVA CARGADA", null);
			resultado = "crearOperacionDeterminacion";
			operacionDeterminacion = new Operaciondeterminacion();
		}


		return "crearOperacionDeterminacion";

	}

	public String borrarOperacionDeterminacion(int idOpDet){

		if (idOpDet!=0){
			Operaciondeterminacion opDet = servicioBasicoOperaciones.findOperaciondeterminacion(new Integer(idOpDet));

			// Tengo que eliminar tambien la Operacion, cojo el id de la Operacion
			Operacion op = opDet.getOperacion();

			// Borro la OperacionDeterminacion
			servicioBasicoOperaciones.removeOperaciondeterminacion(opDet);

			// Borro la Operacion
			servicioBasicoOperaciones.removeOperacion(op);

			// Tendria que actualizar la lista de operaciones determinacion

			JsfUtil.addSuccessMessage("Operación Determinación borrada correctamente");
		} else
			JsfUtil.addErrorMessage("No hay Operación Determinación seleccionada");

		return "gestionOperaciones";

	}

	public String guardarOperacionDeterminacion(){

		Operacion op = null;
		Operaciondeterminacion opDet = operacionDeterminacion;
		try{

			if(validarOperacionDeterminacion(opDet)) {

				// Si no es nulo estoy editando
				if (opDet!=null && opDet.getIden()!=0){

					op = servicioBasicoOperaciones.findOperacion(new Integer(opDet.getOperacion().getIden()));

					if (op==null){ // La Operacion no existe => la creo

						op = new Operacion();

						op.setTramite(variablesSesionUsuario.getTramiteEncargadoTrabajo());


						servicioBasicoOperaciones.createOperacion(op);
					} else { // La operacion ya existe => solo modifico Operacion Determinacion


						opDet.setIdtipooperaciondet(idTipoOperacionDeterminacion);
						op.setTramite(variablesSesionUsuario.getTramiteEncargadoTrabajo());
						opDet.setOperacion(op);

						servicioBasicoOperaciones.editOperaciondeterminacion(opDet);

					}
					// Si es nula es porque estoy creando uno nuevo
				} else {

					// 1) Creo la operacion
					op = new Operacion();

					op.setTramite(variablesSesionUsuario.getTramiteEncargadoTrabajo());

					servicioBasicoOperaciones.createOperacion(op);

					// 2) Creo la Operacion Determinacion


					opDet.setIdtipooperaciondet(idTipoOperacionDeterminacion);
					opDet.setOperacion(op);

					servicioBasicoOperaciones.createOperaciondeterminacion(opDet);

					// Tengo que actualizar la lista de operaciones determinacion

					JsfUtil.addSuccessMessage("Operación Determinación creada correctamente");

				}

				FacesManager.instance().redirect("/produccionfip/gestionoperaciones/listaOperacionesDeterminacion.xhtml");
			}		

		}
		catch (Exception e) {
			e.printStackTrace();
		}

		return "crearOperacionDeterminacion";

	}

	public List<SelectItem> tipoOperacionDeterminacion(){



		List<Object[]> elementos = servicioBasicoOperaciones.tipoOperacionDeterminacion();

		List<SelectItem> res = new ArrayList<SelectItem>();
		for (Object[] c : elementos) {
			SelectItem i = new SelectItem();
			i.setLabel((String) c[1]);
			i.setValue(c[0]);
			res.add(i);
		}

		return res;



	}

	public void seleccionarDeterminacionOperada(int idDeterminacion) {
		try {
			Determinacion determinacionOperada = servicioBasicoDeterminaciones.buscarDeterminacion(idDeterminacion);
			operacionDeterminacion.setDeterminacionByIddeterminacion(determinacionOperada);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			setMostrarPopUpDeterminacionOperada(false);
		}	
	}

	public void seleccionarDeterminacionOperadora(int idDeterminacion) {
		try {
			Determinacion determinacionOperadora = servicioBasicoDeterminaciones.buscarDeterminacion(idDeterminacion);
			operacionDeterminacion.setDeterminacionByIddeterminacionoperadora(determinacionOperadora);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			setMostrarPopUpDeterminacionOperadora(false);
		}	
	}


	private boolean validarOperacionDeterminacion(Operaciondeterminacion opDet) {
		boolean valida = true;

		if(opDet != null) {

			if(opDet.getDeterminacionByIddeterminacion() == null || opDet.getDeterminacionByIddeterminacion().getIden() == 0) {
				facesMessages.addFromResourceBundle(Severity.ERROR, "operacion_no_determinacion_operada", null);
				valida = false;
			} else if(opDet.getDeterminacionByIddeterminacionoperadora() == null || opDet.getDeterminacionByIddeterminacionoperadora().getIden() == 0) {
				facesMessages.addFromResourceBundle(Severity.ERROR, "operacion_no_determinacion_operadora", null);
				valida = false;
			}

		}

		return valida;
	}


	public Operaciondeterminacion getOperacionDeterminacion() {
		return operacionDeterminacion;
	}

	public void setOperacionDeterminacion(Operaciondeterminacion operacionDeterminacion) {
		this.operacionDeterminacion = operacionDeterminacion;
	}

	public int getIdTipoOperacionDeterminacion() {
		return idTipoOperacionDeterminacion;
	}

	public void setIdTipoOperacionDeterminacion(int idTipoOperacionDeterminacion) {
		this.idTipoOperacionDeterminacion = idTipoOperacionDeterminacion;
	}	

	public boolean isMostrarPopUpDeterminacionOperadora() {
		return mostrarPopUpDeterminacionOperadora;
	}

	public void setMostrarPopUpDeterminacionOperadora(
			boolean mostrarPopUpDeterminacionOperadora) {
		this.mostrarPopUpDeterminacionOperadora = mostrarPopUpDeterminacionOperadora;
	}

	public boolean isMostrarPopUpDeterminacionOperada() {
		return mostrarPopUpDeterminacionOperada;
	}

	public void setMostrarPopUpDeterminacionOperada(boolean mostrarPopUpDeterminacionOperada) {
		this.mostrarPopUpDeterminacionOperada = mostrarPopUpDeterminacionOperada;
	}
	
	
	public Integer getIdOperacion() {
		
		return idOperacion;
	}

	public void setIdOperacion(Integer idOperacion) {
		logger.info("[setIdOperacion] idOperacion="+idOperacion, null);
		this.idOperacion = idOperacion;
	}
	

	@Remove
	public void destroy() {
	}
}

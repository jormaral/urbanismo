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

package com.mitc.redes.editorfip.servicios.produccionfip.gestiondiccionariodeterminaciones;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import org.hibernate.exception.ConstraintViolationException;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.faces.FacesManager;
import org.jboss.seam.international.StatusMessages;
import org.jboss.seam.international.StatusMessage.Severity;
import org.jboss.seam.log.Log;

import com.icesoft.faces.component.selectinputtext.SelectInputText;
import com.mitc.redes.editorfip.entidades.interfazgrafico.DeterminacionBusquedaDTO;
import com.mitc.redes.editorfip.entidades.interfazgrafico.UnidadDTO;
import com.mitc.redes.editorfip.entidades.interfazgrafico.UnidadTramiteDTO;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Determinacion;
import com.mitc.redes.editorfip.excepciones.EditorFIPException;
import com.mitc.redes.editorfip.servicios.basicos.fip.determinaciones.ServicioBasicoDeterminaciones;
import com.mitc.redes.editorfip.servicios.basicos.fip.determinaciones.ServicioCRUDDeterminaciones;
import com.mitc.redes.editorfip.servicios.basicos.fip.unidades.ServicioBasicoUnidades;
import com.mitc.redes.editorfip.servicios.sesion.VariablesSesionUsuario;

@Scope(ScopeType.SESSION)
@Stateful
@Name("gestionDeterminaciones")
public class GestionDeterminacionesBean implements GestionDeterminaciones
{

	@Logger private Log logger;
	@In StatusMessages statusMessages;
	@In (create = true, required = false) VariablesSesionUsuario variablesSesionUsuario;
	@In(create = true, required = false) ServicioBasicoDeterminaciones servicioBasicoDeterminaciones;
	@In(create = true, required = false) ServicioBasicoUnidades servicioBasicoUnidades;
	@In(create = true, required = false) ServicioCRUDDeterminaciones servicioCRUDDeterminaciones;
	@In(create = true, required = false) GestionArbolDeterminaciones gestionArbolDeterminaciones;
	@In(create = true, required = false) ListaValoresReferencia listaValoresReferencia;
	@In(create = true, required = false) ListaValoresReferenciaTramite listaValoresReferenciaTramite;

	private int idDeterminacion = 0;
	private Determinacion determinacion = new Determinacion();
	private int idDeterminacionOLD = 0;
	private int idTramiteTrabajo = 0;

	private boolean mostrarPanelDetBase = true;
	private boolean mostrarPanelDetPadre = true;
	private boolean mostrarPanelValRef = true;
	private boolean mostrarPanelGrupAp = true;
	private boolean mostrarPanelDetReg = true;
	private boolean mostrarPanelUnidad = true;
	private boolean mostrarRegulacionesEspecificas = true;

	private List<SelectItem> resultadosBusqueda = new ArrayList<SelectItem>();
	private Determinacion determinacionFiltro = new Determinacion();
	private boolean mostrarPanelFiltros = false;
	private boolean mostrarPopUpErrorElminDet = false;
	private boolean filtroNombre = true;
	private boolean filtroApartado = false;
	private boolean filtroEtiqueta = false;

	private int determinacionSel;

	UnidadDTO unidadDeDeterminacion;


	public int getIdDeterminacion() {
		return idDeterminacion;
	}

	public void setIdDeterminacion(int idDeterminacion) {
		logger.info("Estableciendo determinacion, id: " + idDeterminacion);
		this.idDeterminacion = idDeterminacion;
		cargarDeterminacion();
		logger.info("Determinacion establecida, id: " + idDeterminacion);
	}

	public int getIdDeterminacionBase() {
		int id = 0;

		if(determinacion.getDeterminacionByIddeterminacionbase() != null) {
			id = determinacion.getDeterminacionByIddeterminacionbase().getIden();
		}

		return id;
	}

	public void setIdDeterminacionBase(int idDeterminacionBase) {
		
		if (idDeterminacionBase!=0)
		{
			Determinacion detBase = servicioBasicoDeterminaciones.buscarDeterminacion(idDeterminacionBase);
			determinacion.setDeterminacionByIddeterminacionbase(detBase) ;	
		}
		else
		{
			determinacion.setDeterminacionByIddeterminacionbase(null) ;	
		}
		
		mostrarPanelDetBase = false;
	}

	public int getIdDeterminacionPadre() {
		int id = 0;

		if(determinacion.getDeterminacionByIdpadre() != null) {
			id = determinacion.getDeterminacionByIdpadre().getIden();
		}

		return id;
	}

	public void setIdDeterminacionPadre(int idDeterminacionPadre) {
		
		if (idDeterminacionPadre!=0)
		{
			Determinacion detPadre = servicioBasicoDeterminaciones.buscarDeterminacion(idDeterminacionPadre);
			determinacion.setDeterminacionByIdpadre(detPadre);
		}
		else
		{
			// Si es cero la colgamos de la raiz
			determinacion.setDeterminacionByIdpadre(null);
			
		}
		
		mostrarPanelDetPadre = false;
	}


	private void cargarDeterminacion() {

		// Si el idTramiteTrabajo es cero, tengo que cargarlo previamente
		// para evitar que la primera vez que se seleccione una entidad no se cargue nada
		if (idTramiteTrabajo ==0) {
			logger.info("No hay trámite de trabajo seleccionado.");
			idTramiteTrabajo=variablesSesionUsuario.getIdTramiteTrabajoActual();
		}

		if (idDeterminacion==0 || idTramiteTrabajo!=variablesSesionUsuario.getIdTramiteTrabajoActual()) {

			logger.info("[cargarDeterminacion] idTramiteTrabajo="+idTramiteTrabajo+" / variablesSesionUsuario.getIdTramiteTrabajoActual()="+variablesSesionUsuario.getIdTramiteTrabajoActual());
			determinacion = new Determinacion();
			idTramiteTrabajo = variablesSesionUsuario.getIdTramiteTrabajoActual();

		} else	{

			//logger.info("[cargarDeterminacion] idDeterminacion="+idDeterminacion+" / idDeterminacionOLD="+idDeterminacionOLD);

			//if (idDeterminacionOLD!=idDeterminacion) {
				this.determinacion = servicioBasicoDeterminaciones.buscarDeterminacion(idDeterminacion);
				cargarUnidadDeDeterminacion();
				
				// Pongo a false el valor de recargar las listas de valores de referencia
				listaValoresReferencia.setListaRecargada(false);
				idDeterminacionOLD=idDeterminacion;
			//}

			logger.info("[cargarDeterminacion] determinacion="+determinacion.getIden()+" / apartado="+determinacion.getApartado());
		}
	}

	public void cancelar() {

		determinacion = new Determinacion();
		if (getIdDeterminacion()!=0){
			this.determinacion = servicioBasicoDeterminaciones.buscarDeterminacion(idDeterminacion);
			cargarUnidadDeDeterminacion();
			idDeterminacionOLD=idDeterminacion;
			logger.info("[cargarDeterminacion] determinacion="+determinacion.getIden());
		} else {
			if (idDeterminacion == 0 && idDeterminacionOLD==0){
				setIdDeterminacion(0);	
			}else{
				this.determinacion = servicioBasicoDeterminaciones.buscarDeterminacion(idDeterminacionOLD);
				cargarUnidadDeDeterminacion();
				idDeterminacionOLD=getIdDeterminacionPadre();
				logger.info("[cargarDeterminacion] determinacion="+idDeterminacionOLD);
			}
		}

	}

	public Determinacion getDeterminacion() {
		logger.info("Obteniendo la determinación, id: " + determinacion.getIden());
		return determinacion;
	}

	public void setDeterminacion(Determinacion determinacion) {
		this.determinacion = determinacion;
	}

	public Determinacion getDeterminacionFiltro() {
		return determinacionFiltro;
	}

	public void setDeterminacionFiltro(Determinacion determinacionFiltro) {
		this.determinacionFiltro = determinacionFiltro;
	}



	public void borrarSoloDeterminacionEHijas(ActionEvent action)
	{	
		logger.info("[borrarSoloDeterminacionEHijas] Se va a proceder a borrar la determinacion (e hijas) id:"+determinacion.getIden());

		try{

			// Es una borrado
			servicioCRUDDeterminaciones.borrarDeterminacionesRecursivo(determinacion.getIden());
			// log de informacion
			logger.info("[borrarSoloDeterminacionEHijas] Determinacion Borrada Correctamente: id:"+determinacion.getIden());
			statusMessages.addFromResourceBundleOrDefault("determinacion_borrar_ok","Determinacion Borrada Correctamente");

			// Pongo una determinacion vacia:
			//Inicializo la determinacion
			determinacion = new Determinacion();
			setIdDeterminacion(0);



		} catch (Exception ex) {

			Throwable error = ex.getCause();
			if(error != null)
				error = error.getCause();

			if(error instanceof ConstraintViolationException) {
				mostrarPopUpErrorElminDet = true;
			} else {
				statusMessages.addFromResourceBundleOrDefault(Severity.ERROR,"determinacion_borrar_error","ERROR: Se ha producido un error al borrar la determinacion: "+ex.getLocalizedMessage() +ex.getMessage());
			}

			logger.error("[borrarSoloDeterminacionEHijas] ERROR: Se ha producido un error al borrar la determinacion: "+ex.getLocalizedMessage() +ex.getMessage());

		} 		
	}

	public void borrarDeterminacionYCUEHijas(ActionEvent action)
	{	
		logger.info("[borrarDeterminacionYCUEHijas] Se va a proceder a borrar la determinacion (e hijas) Y CUs Asociadas.id:"+determinacion.getIden());

		try{

			// Es una borrado
			servicioCRUDDeterminaciones.borrarDeterminacionesYCUsAsociadasADeterminacionRecursivo(determinacion.getIden());

			// log de informacion
			logger.info("[borrarDeterminacionYCUEHijas] Determinacion Y CUs Asociadas A Determinacion Borrada Correctamente: id:"+determinacion.getIden());
			statusMessages.addFromResourceBundleOrDefault("determinacionycu_borrar_ok"," Determinacion Y CUs Asociadas A Determinacion borrada Correctamente");

			// Pongo una determinacion vacia:
			//Inicializo la determinacion y el nodo seleccionado
			determinacion = new Determinacion();
			setIdDeterminacion(0);


		}
		catch (Exception ex)
		{
			statusMessages.addFromResourceBundleOrDefault(Severity.ERROR,"determinacionycu_borrar_error","ERROR: Se ha producido un error al borrarDeterminacion Y CUs Asociadas A Determinacion: "+ex.getLocalizedMessage() +ex.getMessage());
			logger.error("[borrarDeterminacionYCUEHijas] ERROR: Se ha producido un error al borrar laDeterminacion Y CUs Asociadas A Determinacion: "+ex.getLocalizedMessage() +ex.getMessage());

			ex.printStackTrace();		
		}

	}

	public void borrarSoloCUDeDeterminacion(ActionEvent action)
	{	
		logger.info("[borrarSoloCUDeDeterminacion] Se va a proceder a borrar las CUs Asociadas A Determinacion. id:"+determinacion.getIden());

		try{

			
			// Es una borrado
			servicioCRUDDeterminaciones.borrarCUsAsociadasADeterminacion(determinacion.getIden());

			// log de informacion
			logger.info("[borrarSoloCUDeDeterminacion] CUs Asociadas A DeterminacionBorradas Correctamente: id:"+determinacion.getIden());
			statusMessages.addFromResourceBundleOrDefault("cudedeterminacion_borrar_ok","CUs Asociadas A DeterminacionBorradas Correctamente");




		}
		catch (Exception ex)
		{
			statusMessages.addFromResourceBundleOrDefault(Severity.ERROR,"cudedeterminacion_borrar_error","ERROR: Se ha producido un error al borrar la CUs Asociadas A Determinacion: "+ex.getLocalizedMessage() +ex.getMessage());
			logger.error("[borrarSoloCUDeDeterminacion] ERROR: Se ha producido un error al borrar CUs Asociadas A Determinacion: "+ex.getLocalizedMessage() +ex.getMessage());

			ex.printStackTrace();

		}
	}



	public void guardarDeterminacion(ActionEvent action) {
	
		logger.info("[guardarDeterminacion] determinacion.getIden():"+determinacion.getIden());

		try {
			if (determinacion.getIden()==0) {

				// Es una nueva determinacion
				// Guardo el tramite al que pertenece la determinacion (tramite de trabajo)

				int idTramTrabajo =variablesSesionUsuario.getIdTramiteTrabajoActual();
				determinacion.setTramite(variablesSesionUsuario.getTramiteTrabajoActual());

				// Comprobar que la determinacion tiene tramite
				if (determinacion.getTramite() == null)
					throw new EditorFIPException("Intenando guardar una determinacion sin tramite relacionado");


				// Compruebo si se ha introducido una determinacion base porque de lo contrario hay que ponerlo a null
				if (determinacion.getDeterminacionByIddeterminacionbase()==null)
				{
					determinacion.setDeterminacionByIddeterminacionbase(null);
				}

				// Compruebo si se ha introducido una determinacion padre porque de lo contrario hay que ponerlo a null
				int idDetPadre = 0;
				if (determinacion.getDeterminacionByIdpadre()==null)
				{
					determinacion.setDeterminacionByIdpadre(null);
				} else {
					idDetPadre = determinacion.getDeterminacionByIdpadre().getIden();
				}

				determinacion.setOrden(servicioBasicoDeterminaciones.obtenerOrderNuevaDeterminacion(idTramTrabajo, idDetPadre));
				
				// La persisto en la base de datos
				int idDetNueva = servicioCRUDDeterminaciones.create(determinacion);
				
				// Veamos si la unidad No Procede existe
				UnidadTramiteDTO unidad = servicioBasicoDeterminaciones.obtenerUnidadNoProcedeTramite(idTramTrabajo);
				if (unidad!=null)
					servicioBasicoDeterminaciones.guardarUnidadDeDeterminacion(idDetNueva, unidad.getIdDeterminacion());
				
				setMostrarPanelUnidad(false);
				
				// log de informacion
				logger.info("[guardarDeterminacion] Determinacion Creada Correctamente: id:"+idDetNueva +" para el tramite de trabajo:"+idTramTrabajo);
				statusMessages.addFromResourceBundleOrDefault("determinacion_creada_ok", "Determinacion Creada Correctamente");

				// Como ya se ha persistido la cojo de BD para que tenga el id de referencia
				setIdDeterminacion(idDetNueva);

			} else	{

				logger.info("[guardarDeterminacion] Modificar: determinacion con determinacion.getApartado():"+determinacion.getApartado());

				// Es una edicion
				servicioCRUDDeterminaciones.edit(determinacion);

				this.determinacion = servicioBasicoDeterminaciones.buscarDeterminacion(idDeterminacion);
				cargarUnidadDeDeterminacion();

				// log de informacion
				logger.info("[guardarDeterminacion] Determinacion Modificada Correctamente: id:"+determinacion.getIden());
				statusMessages.addFromResourceBundleOrDefault("determinacion_modificada_ok","Determinacion Modificada Correctamente");
			}

		} catch (Exception ex) {

			statusMessages.addFromResourceBundleOrDefault("determinacion_guardar_error","ERROR: Se ha producido un error al guardar la determinacion: "+ex.getLocalizedMessage() +ex.getMessage());
			logger.error("[guardarDeterminacion] ERROR: Se ha producido un error al guardar la determinacion: "+ex.getLocalizedMessage() +ex.getMessage());

			ex.printStackTrace();
		}
	}

	public void actualizarLista(ValueChangeEvent event) {

		String filtro = ((SelectInputText) event.getComponent()).getValue().toString();
		int idTramite = variablesSesionUsuario.getIdTramiteTrabajoActual(); 
		List<Determinacion> resultados;

		SelectInputText autoComplete = (SelectInputText) event.getComponent();

		// si no se selecciona ponemos el valor a -1 para sacar un listado de elementos.

		if (autoComplete.getSelectedItem() != null)
			determinacionSel = (Integer) autoComplete.getSelectedItem().getValue();
		else
			determinacionSel=-1;

		if(!filtro.equalsIgnoreCase("") && idTramite != 0) {
			resultados = servicioCRUDDeterminaciones.autocomplete(filtro, idTramite);

			resultadosBusqueda.clear();

			for(Determinacion resul : resultados)
				resultadosBusqueda.add(new SelectItem(resul.getIden(), resul.getNombre()));
		}


	}

	public void actualizarListaDeterminacion(ValueChangeEvent event) {

		String filtro = ((SelectInputText) event.getComponent()).getValue().toString();
		int idTramite = variablesSesionUsuario.getIdTramiteTrabajoActual(); 
		List<DeterminacionBusquedaDTO> resultados;

		SelectInputText autoComplete = (SelectInputText) event.getComponent();

		// si no se selecciona ponemos el valor a -1 para sacar un listado de elementos.

		if (autoComplete.getSelectedItem() != null)
			determinacionSel = ((DeterminacionBusquedaDTO) autoComplete.getSelectedItem().getValue()).getIden();
		else
			determinacionSel=-1;

		if(!filtro.equalsIgnoreCase("") && idTramite != 0) {
			resultados = servicioCRUDDeterminaciones.autocompleteFiltros(filtro, idTramite, filtroNombre,
					filtroApartado, filtroEtiqueta);

			resultadosBusqueda.clear();

			for(DeterminacionBusquedaDTO resul : resultados)
				resultadosBusqueda.add(new SelectItem(resul, resul.getNombre()));
		}


	}

	public void prepararParaCrear(ActionEvent actionEvent) {

		int idDetPadre = new Integer(idDeterminacion);

		//Inicializo la determinacion
		determinacion = new Determinacion();
		setIdDeterminacion(0);

		if(idDetPadre != 0) {
			Determinacion detPadre = servicioBasicoDeterminaciones.buscarDeterminacion(idDetPadre);
			determinacion.setDeterminacionByIdpadre(detPadre);
		}		
	}	

	public void seleccionarUnidad(UnidadTramiteDTO unidad, String volverPag) {
		servicioBasicoDeterminaciones.guardarUnidadDeDeterminacion(getIdDeterminacion(), unidad.getIdDeterminacion());
		setMostrarPanelUnidad(false);

		FacesManager.instance().redirect(volverPag);
	}

	public void aceptarFiltros(ActionEvent event)
	{
		mostrarPanelFiltros=false;
	}

	public void mostrarPanel()
	{
		mostrarPanelFiltros=true;
	}

	public void seleccionarUnidad(UnidadTramiteDTO unidad) {
		servicioBasicoDeterminaciones.guardarUnidadDeDeterminacion(getIdDeterminacion(), unidad.getIdDeterminacion());
		setMostrarPanelUnidad(false);
	}

	private void cargarUnidadDeDeterminacion() {
		if(determinacion.getIdcaracter() == 18) {
			this.unidadDeDeterminacion = servicioBasicoUnidades.obtenerUnidadDeDeterminacion(idDeterminacion);

			if(unidadDeDeterminacion == null) {
				unidadDeDeterminacion = new UnidadDTO();
				unidadDeDeterminacion.setIdDeterminacion(0);
			}
		}
	}

	public void guardarUnidadDeDeterminacion() {
		int resultado = servicioBasicoUnidades.crearUnidad(idDeterminacion, unidadDeDeterminacion.getAbreviatura(), unidadDeDeterminacion.getDefinicion());



		// Si el resultado es distinto de cero, se ha hecho correctamente, avisamos
		if (resultado!=0)
		{
			// Tengo que recargar la unidaddeterminacion
			cargarUnidadDeDeterminacion();
			statusMessages.addFromResourceBundleOrDefault("determinacion_detalleunidad_creada_ok","Detalle Unidad Creada Correctamente");
			logger.info("[guardarUnidadDeDeterminacion] Unidad creada correctamente");
		}
		else
		{
			statusMessages.addFromResourceBundleOrDefault("determinacion_detalleunidad_creada_fallo","Fallo al Crear Detalle Unidad");
			logger.error("[guardarUnidadDeDeterminacion] Error al crear la Unidad");
		}
	}

	public void borrarUnidadDeDeterminacion() {
		int resultado = servicioBasicoUnidades.borrarUnidadDeDeterminacion(idDeterminacion);

		// Si el resultado es distinto de cero, se ha hecho correctamente, avisamos
		if (resultado!=0)
		{
			// Tengo que recargar la unidaddeterminacion
			unidadDeDeterminacion =null;
			cargarUnidadDeDeterminacion();
			statusMessages.addFromResourceBundleOrDefault("determinacion_detalleunidad_borrada_ok","Detalle Unidad Borrada Correctamente");
			logger.info("[borrarUnidadDeDeterminacion] Unidad borrada correctamente");
		}
		else
		{
			statusMessages.addFromResourceBundleOrDefault("determinacion_detalleunidad_borrada_fallo","Fallo al Borrar Detalle Unidad");
			logger.error("[borrarUnidadDeDeterminacion] Error al borrar la Unidad");
		}

	}
	
	public void desasociarUnidadDeDeterminacion() {
		boolean resultado = servicioBasicoDeterminaciones.borrarUnidadDeDeterminacion(idDeterminacion);

		// Si el resultado es distinto de cero, se ha hecho correctamente, avisamos
		if (resultado)
		{
			// Tengo que recargar la unidaddeterminacion
			unidadDeDeterminacion =null;
			cargarUnidadDeDeterminacion();
			statusMessages.addFromResourceBundleOrDefault("determinacion_detalleunidad_borrada_ok","Detalle Unidad Borrada Correctamente");
			logger.info("[desasociarUnidadDeDeterminacion] Unidad desasociada correctamente");
		}
		else
		{
			statusMessages.addFromResourceBundleOrDefault("determinacion_detalleunidad_borrada_fallo","Fallo al Borrar Detalle Unidad");
			logger.error("[desasociarUnidadDeDeterminacion] Error al desasociar la Unidad");
		}

	}

	public boolean isRenderizarUnidadDeDeterminacion() {

		boolean renderizar = determinacion != null && determinacion.getIden() != 0 && determinacion.getIdcaracter() == 18;
		return renderizar;
	}

	public int getDeterminacionSel() {
		return determinacionSel;
	}

	public void setDeterminacionSel(int determinacionSel) {
		this.determinacionSel = determinacionSel;
	}


	public UnidadDTO getUnidadDeDeterminacion() {
		return unidadDeDeterminacion;
	}

	public void setUnidadDeDeterminacion(UnidadDTO unidadDeDeterminacion) {
		this.unidadDeDeterminacion = unidadDeDeterminacion;
	}

	public boolean isMostrarPanelDetBase() {
		return mostrarPanelDetBase;
	}

	public void setMostrarPanelDetBase(boolean mostrarPanelDetBase) {
		this.mostrarPanelDetBase = mostrarPanelDetBase;
	}

	public boolean isMostrarPanelDetPadre() {
		return mostrarPanelDetPadre;
	}

	public void setMostrarPanelDetPadre(boolean mostrarPanelDetPadre) {
		this.mostrarPanelDetPadre = mostrarPanelDetPadre;
	}

	public boolean isMostrarPanelValRef() {
		return mostrarPanelValRef;
	}

	public void setMostrarPanelValRef(boolean mostrarPanelValRef) {
		this.mostrarPanelValRef = mostrarPanelValRef;
		
		listaValoresReferenciaTramite.setRefrescar(true);
	}

	public boolean isMostrarPanelGrupAp() {
		return mostrarPanelGrupAp;
	}

	public void setMostrarPanelGrupAp(boolean mostrarPanelGrupAp) {
		this.mostrarPanelGrupAp = mostrarPanelGrupAp;
	}

	public boolean isMostrarPanelDetReg() {
		return mostrarPanelDetReg;
	}

	public void setMostrarPanelDetReg(boolean mostrarPanelDetReg) {
		this.mostrarPanelDetReg = mostrarPanelDetReg;
	}

	public boolean isMostrarPanelUnidad() {
		return mostrarPanelUnidad;
	}

	public void setMostrarPanelUnidad(boolean mostrarPanelUnidad) {
		this.mostrarPanelUnidad = mostrarPanelUnidad;
	}

	public boolean isMostrarRegulacionesEspecificas() {
		return mostrarRegulacionesEspecificas;
	}

	public void setMostrarRegulacionesEspecificas(
			boolean mostrarRegulacionesEspecificas) {
		this.mostrarRegulacionesEspecificas = mostrarRegulacionesEspecificas;
	}

	public List<SelectItem> getResultadosBusqueda() {
		return resultadosBusqueda;
	}

	public void setResultadosBusqueda(List<SelectItem> resultadosBusqueda) {
		this.resultadosBusqueda = resultadosBusqueda;
	}

	public void mostrarPanelDetBaseAL(ActionEvent actionEvent) {
		mostrarPanelDetBase = true;
	}

	public void mostrarPanelDetPadreAL(ActionEvent actionEvent) {
		mostrarPanelDetPadre = true;
	}

	public void mostrarPanelValRefAL(ActionEvent actionEvent) {
		mostrarPanelValRef = true;
	}

	public void mostrarPanelGrupApAL(ActionEvent actionEvent) {
		mostrarPanelGrupAp = true;
	}

	@Remove
	public void destroy() {
	}

	public boolean isMostrarPanelFiltros() {
		return mostrarPanelFiltros;
	}

	public void setMostrarPanelFiltros(boolean mostrarPanelFiltros) {
		this.mostrarPanelFiltros = mostrarPanelFiltros;
	}

	public boolean isFiltroNombre() {
		return filtroNombre;
	}

	public void setFiltroNombre(boolean filtroNombre) {
		this.filtroNombre = filtroNombre;
	}

	public boolean isFiltroApartado() {
		return filtroApartado;
	}

	public void setFiltroApartado(boolean filtroApartado) {
		this.filtroApartado = filtroApartado;
	}

	public boolean isFiltroEtiqueta() {
		return filtroEtiqueta;
	}

	public void setFiltroEtiqueta(boolean filtroEtiqueta) {
		this.filtroEtiqueta = filtroEtiqueta;
	}

	public boolean isMostrarPopUpErrorElminDet() {
		return mostrarPopUpErrorElminDet;
	}

	public void setMostrarPopUpErrorElminDet(boolean mostrarPopUpErrorElminDet) {
		this.mostrarPopUpErrorElminDet = mostrarPopUpErrorElminDet;
	}

}

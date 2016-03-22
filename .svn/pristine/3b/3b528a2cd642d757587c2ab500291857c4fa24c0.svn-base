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

package com.mitc.redes.editorfip.servicios.produccionfip.gestionentidades;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.faces.event.ActionEvent;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.faces.FacesManager;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.international.StatusMessages;
import org.jboss.seam.international.StatusMessage.Severity;
import org.jboss.seam.log.Log;

import com.mitc.redes.editorfip.entidades.interfazgrafico.CondicionUrbanisticaSimplificadaDTO;
import com.mitc.redes.editorfip.entidades.interfazgrafico.RegimenesEspecificosSimplificadosDTO;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Determinacion;
import com.mitc.redes.editorfip.servicios.basicos.fip.determinaciones.ServicioBasicoDeterminaciones;
import com.mitc.redes.editorfip.servicios.basicos.fip.entidades.ServicioBasicoCondicionesUrbanisticas;
import com.mitc.redes.editorfip.servicios.genericos.JsfUtil;
import com.mitc.redes.editorfip.servicios.sesion.VariablesSesionUsuario;

@Scope(ScopeType.SESSION)
@Stateful
@Name("gestionCondicionesUrbanisticas")
public class GestionCondicionesUrbanisticasBean implements GestionCondicionesUrbanisticas
{

	@Logger private Log log;
	
	@PersistenceContext
	EntityManager em;
	
	@In StatusMessages statusMessages;
	
	@In FacesMessages facesMessages;
	@In (create = true, required = false) VariablesSesionUsuario variablesSesionUsuario;
	@In(create = true, required = false) ServicioBasicoCondicionesUrbanisticas servicioBasicoCondicionesUrbanisticas;
	@In(create = true, required = false) ServicioBasicoDeterminaciones servicioBasicoDeterminaciones;
	@In(create = true, required = false) GestionEntidades gestionEntidades;
	@In(create = true, required = false) ListaCUSimplificada listaCUSimplificada;

	private boolean cuSeleccionadaModificada = false;
	private CondicionUrbanisticaSimplificadaDTO cuSeleccionada = new CondicionUrbanisticaSimplificadaDTO();
	private String nuevoValor="";
	private int idRegimenEspecifico = 0;
	private String nuevoNombreRegimenEspecifico;
	private String nuevoTextoRegimenEspecifico;
	private int idDetRegimen;
	private int idValReferencia;
	private boolean mostrarPanelDetReg = false;
	private boolean mostrarPanelValRef = false;
	private boolean entidadAplicadaGrupoAplicacion = false;


	public int caracterCUSeleccionada()
	{
		int resultado = 0;
		
		
		if (cuSeleccionada.getIdDeterminacion()!=0)
		{
			try
			{
				Determinacion det = em.find(Determinacion.class, cuSeleccionada.getIdDeterminacion());
				resultado = det.getIdcaracter();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			
		}
		
		return resultado;
	}

	public boolean isEntidadAplicadaGrupoAplicacion() {

		entidadAplicadaGrupoAplicacion = servicioBasicoCondicionesUrbanisticas.tieneAsignadaEntidadGrupoAplicacion(gestionEntidades.getIdEntidad());

		return entidadAplicadaGrupoAplicacion;
	}

	public void borrarRegimen (ActionEvent event)
	{
		log.info("[borrarRegimen] Inicio");

		String idRegimenCU = JsfUtil.getRequestParameter("idRegimenCU");
		log.info("[borrarRegimen] idRegimenCU="+idRegimenCU);

		// Cargamos el idRegimen
		try
		{
			int idReg = Integer.parseInt(idRegimenCU);





			if (servicioBasicoCondicionesUrbanisticas.borrarRegimen(idReg))
			{
				log.info("[borrarRegimen] Se ha borrado correctamente el regimen");
				statusMessages.addFromResourceBundle(Severity.INFO, "regimen_borrado_ok", "Se ha borrado correctamente el regimen");
				listaCUSimplificada.refrescarLista();

			}
			else
			{
				log.error("[borrarRegimen] ATENCION: No se ha podido borrar el regimen de la Condicion Urbanistica Seleccionada");
				statusMessages.addFromResourceBundle(Severity.ERROR,"regimen_borrado_error","ATENCION: No se ha podido borrar el regimen de la Condicion Urbanistica Seleccionada");
			}


		}
		catch (NumberFormatException ex)
		{
			log.error("[borrarRegimen] Error al convertir en numero la variable: idRegimenCU="+idRegimenCU);
			ex.printStackTrace();

		}
		catch (Exception ex2)
		{
			log.error("[borrarRegimen] Error");
			ex2.printStackTrace();

		}


	}


	public void copiarCUsSeleccionadasEnNuevaEntidad (List<CondicionUrbanisticaSimplificadaDTO> listaCUaCopiar, List<Integer> listaIdEntidadDestino)
	{
		log.debug("[copiarCUsSeleccionadasEnNuevaEntidad] listaCUaCopiar="+listaCUaCopiar.size()+" / listaIdEntidadDestino="+listaIdEntidadDestino.size());


		// Cargamos la entidad donde iran las copias de las CUs
		try
		{

			for (Integer idEntCopia:listaIdEntidadDestino)
			{

				log.debug("[copiarCUsSeleccionadasEnNuevaEntidad] idEntCopia="+idEntCopia);

				int numCUNoCopiadas = 0;
				int numCUSeleccionadas = 0;


				// Si la cu no ha sido modificada, se podra elegir una nueva
				for (CondicionUrbanisticaSimplificadaDTO cuSimpl : listaCUaCopiar)
				{


					log.debug("[copiarCUsSeleccionadasEnNuevaEntidad] Seleccionada la CU: Entidad="+cuSimpl.getIdEntidad()+" / Det="+cuSimpl.getIdDeterminacion()+" / Regimen="+cuSimpl.getIdRegimen()+" Num RE="+cuSimpl.getRegimenesEspecificos().size());
					numCUSeleccionadas++;

					// Tendriamos que copiar esta CU en la entidad destino
					cuSimpl.setIdEntidad(idEntCopia);
					try
					{
						int idRegimen = servicioBasicoCondicionesUrbanisticas.crearCU(cuSimpl);

						if (idRegimen == 0)
						{
							JsfUtil.addErrorMessage("ATENCION: Error al copiar la Condicion Urbanistica de Determinacion= "+cuSimpl.getNombreDeterminacion()+ " en la Entidad Destino Seleccionada. Probablemente esa determinacion no tenga como grupo de aplicacion el aplicado para esa entidad");
							facesMessages.addFromResourceBundle(Severity.ERROR, "no_grupo_aplicacion", cuSimpl.getNombreDeterminacion());
							numCUNoCopiadas++;
						}

						if (idRegimen==-1)
						{
							JsfUtil.addErrorMessage("ATENCION: Error al copiar la Condicion Urbanistica de Determinacion= "+cuSimpl.getNombreDeterminacion()+ " en la Entidad Destino Seleccionada. La Entidad Destino Seleccionada aun no tiene aplicado ningun Grupo de Aplicacion. Debe aplicar primero un Grupo a la entidad antes de construir otras Condiciones Urbanisticas");
							facesMessages.addFromResourceBundle(Severity.ERROR, "no_grupo_aplicacion_agrupar", cuSimpl.getNombreDeterminacion());
							numCUNoCopiadas++;
						}
						if (idRegimen==-2)
						{
							JsfUtil.addErrorMessage("ATENCION: Error al copiar la Condicion Urbanistica de Determinacion= "+cuSimpl.getNombreDeterminacion()+ " en la Entidad Destino Seleccionada. No se ha encontrado en la determinacion a aplicar que tenga como grupo a la determinacion de grupo aplicada a la entidad. Agregue el Grupo de Aplicacion de la Entidad Destino a la determinacion: "+cuSimpl.getNombreDeterminacion());
							Object[] params = new Object[2];
							params[0]= cuSimpl.getNombreDeterminacion();
							params[1]= cuSimpl.getNombreDeterminacion();
							facesMessages.addFromResourceBundle(Severity.ERROR, "no_grupo_aplicacion_a_determinacion", params);
							numCUNoCopiadas++;
						}

					}
					catch (Exception ex)
					{
						JsfUtil.addErrorMessage("ATENCION: Error al copiar la Condicion Urbanistica de Determinacion= "+cuSimpl.getNombreDeterminacion()+ " en la Entidad Destino Seleccionada. Probablemente esa determinacion no tenga como grupo de aplicacion el aplicado para esa entidad");
						facesMessages.addFromResourceBundle(Severity.ERROR, "no_copiar_cu", cuSimpl.getNombreDeterminacion());
						numCUNoCopiadas++;
					}


				}

				if (numCUNoCopiadas>0)
				{
					JsfUtil.addErrorMessage("ATENCION: No se han podido copiar "+numCUNoCopiadas+" Condiciones Urbanisticas de las "+numCUSeleccionadas+" seleccionadas");
					Object[] params = new Object[2];
					params[0]= numCUNoCopiadas;
					params[1]= numCUSeleccionadas;
					facesMessages.addFromResourceBundle(Severity.ERROR, "no_se_ha_podido_copiar_cu", params);
					
				}
				else
				{
					JsfUtil.addSuccessMessage("Se han copiado correctamente "+numCUSeleccionadas+" Condiciones Urbanisticas");
					facesMessages.addFromResourceBundle(Severity.INFO, "se_ha_podido_copiar_cu", numCUSeleccionadas);
				}
			}

			FacesManager.instance().redirect("/produccionfip/gestionentidades/VerEntidadPlanEncargado.xhtml");

		}

		catch (Exception ex2)
		{
			log.error("[copiarCUsSeleccionadasEnNuevaEntidad] Error");
			ex2.printStackTrace();

		}
	}

	public void establecerCUSeleccionada (ActionEvent event) {

		log.info("[establecerCUSeleccionada - ActionEvent] Inicio");

		String idRegimenCU = JsfUtil.getRequestParameter("idRegimenCU");
		log.info("[establecerCUSeleccionada - ActionEvent] idRegimenCU="+idRegimenCU);

		// Cargamos el idRegimen
		try
		{
			int idReg = Integer.parseInt(idRegimenCU);


			if (!cuSeleccionadaModificada)
			{
				// Si la cu no ha sido modificada, se podra elegir una nueva
				int i;
				boolean encontradaCUTemp = false;
				List<CondicionUrbanisticaSimplificadaDTO> cuSimplificadaList = listaCUSimplificada.getCuSimplificadaList();
				for (i=0; i<cuSimplificadaList.size() & !encontradaCUTemp; i++)
				{
					if (cuSimplificadaList.get(i).getIdRegimen() == idReg)
					{
						log.info("[establecerCUSeleccionada] Seleccionada la CU: Entidad="+cuSimplificadaList.get(i).getIdEntidad()+" / Det="+cuSimplificadaList.get(i).getIdDeterminacion()+" Num RE="+cuSimplificadaList.get(i).getRegimenesEspecificos().size());
						cuSeleccionada = cuSimplificadaList.get(i);


						// Salgo del bucle
						encontradaCUTemp = true;

					}
				}

				if (!encontradaCUTemp)
				{
					log.warn("[establecerCUSeleccionada] No se ha seleccionado ninguna CU");
					statusMessages.addFromResourceBundle(Severity.ERROR,"ATENCION: No se ha podido cargar la Condicion Urbanistica Seleccionada");
				}
				else
				{
					FacesManager.instance().redirect("/produccionfip/gestionentidades/CUSimplificadaContenido.xhtml");
				}
			}
			else
			{
				statusMessages.addFromResourceBundle(Severity.ERROR,"ATENCION: Ha modificado la CU seleccionada anteriormente");

			}

		}
		catch (NumberFormatException ex)
		{
			log.error("[establecerCUSeleccionada] Error al convertir en numero la variable: idRegimenCU="+idRegimenCU);
			ex.printStackTrace();

		}
		catch (Exception ex2)
		{
			log.error("[establecerCUSeleccionada] Error");
			ex2.printStackTrace();

		}


	}


	public void establecerCUSeleccionada (int idReg) {

		log.info("[establecerCUSeleccionada] Inicio. idReg="+idReg);

		if (!cuSeleccionadaModificada)
		{
			// Si la cu no ha sido modificada, se podra elegir una nueva
			int i;
			boolean encontradaCUTemp = false;
			List<CondicionUrbanisticaSimplificadaDTO> cuSimplificadaList = listaCUSimplificada.getCuSimplificadaList();
			for (i=0; i<cuSimplificadaList.size() & !encontradaCUTemp; i++)
			{
				if (cuSimplificadaList.get(i).getIdRegimen() == idReg)
				{
					log.info("[establecerCUSeleccionada] Seleccionada la CU: Entidad="+cuSimplificadaList.get(i).getIdEntidad()+" / Det="+cuSimplificadaList.get(i).getIdDeterminacion()+" Num RE="+cuSimplificadaList.get(i).getRegimenesEspecificos().size());
					cuSeleccionada = cuSimplificadaList.get(i);


					// Salgo del bucle
					encontradaCUTemp = true;

				}
			}

			if (!encontradaCUTemp)
			{
				log.warn("[establecerCUSeleccionada] No se ha seleccionado ninguna CU");
				statusMessages.addFromResourceBundle(Severity.ERROR,"ATENCION: No se ha podido cargar la Condicion Urbanistica Seleccionada");
			}
		}
		else
		{
			statusMessages.addFromResourceBundle(Severity.ERROR,"ATENCION: Ha modificado la CU seleccionada anteriormente");

		}





	}


	public void crearDeterminacionCUSeleccionada(int idDet)
	{
		log.info("[crearDeterminacionCUSeleccionada] Inicio");
		log.info("[crearDeterminacionCUSeleccionada]idDeterminacionCU="+idDet);

		// Cargamos el Determinacion Regimen
		try
		{

			// Cargo una nueva
			cuSeleccionada = new CondicionUrbanisticaSimplificadaDTO();

			// Le meto los valores de entidad y determinacion de la CU
			cuSeleccionada.setIdEntidad(gestionEntidades.getIdEntidad());

			cuSeleccionada.setIdDeterminacion(idDet);
			cuSeleccionada.setNombreDeterminacion(servicioBasicoCondicionesUrbanisticas.nombreDeterminacion(idDet));


			int idRegimen = servicioBasicoCondicionesUrbanisticas.crearCU(cuSeleccionada);

			if (idRegimen>0)
			{

				// Actualizo de nuevo la lista
				listaCUSimplificada.refrescarLista();

				boolean encontradaCUSelecTemp = false;

				// Busco la CU que teniamos seleccionada anteriormente pero sin modificar los datos
				establecerCUSeleccionada(idRegimen);

				statusMessages.addFromResourceBundle(Severity.INFO,"cu_creada_ok","Condicion Urbanistica creada Correctamente");

				FacesManager.instance().redirect("/produccionfip/gestionentidades/CUSimplificadaContenido.xhtml");
			}
			else
			{
				if (idRegimen==0)
					statusMessages.addFromResourceBundle(Severity.ERROR,"cu_creada_error0","Error al guardar la Condicion Urbanistica ");
				if (idRegimen==-1)
					statusMessages.addFromResourceBundle(Severity.ERROR,"cu_creada_error1","Error: La Entidad aun no tiene aplicado ningun Grupo de Aplicacion. Debe aplicar primero un Grupo a la entidad antes de construir otras Condiciones Urbanisticas");
				if (idRegimen==-2)
					statusMessages.addFromResourceBundle(Severity.ERROR,"cu_creada_error2","Error: No se ha encontrado en la determinacion a aplicar que tenga como grupo a la determinacion de grupo aplicada a la entidad. Agregue el Grupo de Aplicacion a la determinacion: "+cuSeleccionada.getNombreDeterminacion());


			}



		}
		catch (NumberFormatException ex)
		{
			log.error("[crearDeterminacionCUSeleccionada] Error al convertir en numero la variable: idDeterminacionCU="+idDet);
			ex.printStackTrace();

		}
		catch (Exception ex2)
		{
			log.error("[crearDeterminacionCUSeleccionada] Error");
			ex2.printStackTrace();

		}


	}


	public void guardarCU()
	{


		// Cargamos el valor de referencia
		try
		{
			int resActualizar = servicioBasicoCondicionesUrbanisticas.actualizarCU(cuSeleccionada);

			// Actualizo de nuevo la lista
			listaCUSimplificada.refrescarLista();

			// Despues de guardar pongo la CU modificada a false
			cuSeleccionadaModificada = false;

			FacesManager.instance().redirect("/produccionfip/gestionentidades/VerEntidadPlanEncargado.xhtml");

			if (resActualizar!=0)
				statusMessages.addFromResourceBundle(Severity.INFO,"cu_guardar_ok","Condicion Urbanistica guardada correctamente");
			else
				statusMessages.addFromResourceBundle(Severity.ERROR,"cu_guardar_error","Error al actualizar la Condicion Urbanistica ");


		}
		catch (Exception ex)
		{
			statusMessages.addFromResourceBundle(Severity.ERROR,"cu_guardar_error","Error al actualizar la Condicion Urbanistica ");
		}


	}


	public void cancelarCambiosCU()
	{
		log.debug("[cancelarCambiosCU] Cancelo los cambios de la CU");
		establecerCUSeleccionada (cuSeleccionada.getIdRegimen());
		FacesManager.instance().redirect("/produccionfip/gestionentidades/VerEntidadPlanEncargado.xhtml");
		JsfUtil.addSuccessMessage("Los cambios en la Condicion Urbanistica se han cancelado");

	}

	public void crearGrupoAplicacionEntidadSeleccionada(int idVRGA)
	{


		int idTramiteBase= variablesSesionUsuario.getIdTramiteBaseTrabajo();
		log.info("[crearGrupoAplicacionEntidadSeleccionada] Inicio: idTramiteBase ="+idTramiteBase+" / idVRGrupoAplicacion ="+idVRGA);


		try
		{


			int idDet = 0;

			// Cargo una nueva
			cuSeleccionada = new CondicionUrbanisticaSimplificadaDTO();

			// Le meto los valores de entidad y determinacion de la CU
			cuSeleccionada.setIdEntidad(gestionEntidades.getIdEntidad());

			// Tengo que buscar cual es el id de la Determinacion de caracter 'Grupo de Aplicacion' del Tramite Base


			idDet = servicioBasicoCondicionesUrbanisticas.obtenerIdDeterminacionGrupoAplicacionDeTramite(idTramiteBase);

			if (idDet!=0)
			{
				// Ha encontrado la determinacion del caracter grupo de aplicacion de ese tramite
				cuSeleccionada.setIdDeterminacion(idDet);


				// Meto el Valor de Referencia del Grupo Aplicacion
				cuSeleccionada.setIdDeterminacionValorReferencia(idVRGA);


				int idRegimen = servicioBasicoCondicionesUrbanisticas.crearCUGrupoAplicacion(cuSeleccionada);

				if (idRegimen>0)
				{
					// Limpio la cuSeleccionada por si acaso
					cuSeleccionada = new CondicionUrbanisticaSimplificadaDTO();

					// Actualizo de nuevo la lista
					//actualizarTablaCUEntidad(idEntidadSeleccionada);

					JsfUtil.addSuccessMessage("Condicion Urbanistica de Grupo de Aplicacion creada Correctamente");
				}
				else
				{
					if (idRegimen==0)
						JsfUtil.addErrorMessage("Error al guardar la Condicion Urbanistica de Grupo de Aplicacion ");
					if (idRegimen==-1)
						JsfUtil.addErrorMessage("Error: La Entidad aun no tiene aplicado ningun Grupo de Aplicacion. Debe aplicar primero un Grupo a la entidad antes de construir otras Condiciones Urbanisticas");
				}
			}
			else
			{
				JsfUtil.addErrorMessage("Error: No se ha encontrado la determinacion Grupo de Aplicacion para el tramite seleccionado o hay mas de una determinacion Grupo de Aplicacion para ese tramite");
			}


		}

		catch (Exception ex2)
		{
			log.error("[crearGrupoAplicacionEntidadSeleccionada] Error");
			ex2.printStackTrace();

		}

		FacesManager.instance().redirect("/produccionfip/gestionentidades/VerEntidadPlanEncargado.xhtml");

	}

	// CARGAR EN CU
	public void cargarDeterminacionRegimenCUSeleccionada(int idDeterminacionRegimen)
	{
		log.debug("[cargarDeterminacionRegimenCUSeleccionada] idDeterminacionRegimen="+idDeterminacionRegimen);


		// Cargamos el Determinacion Regimen
		try
		{

			String nombreDetRegimen = servicioBasicoCondicionesUrbanisticas.nombreDeterminacion(idDeterminacionRegimen);
			cuSeleccionada.setIdDeterminacionRegimen(idDeterminacionRegimen);
			cuSeleccionada.setNombreDetRegimen(nombreDetRegimen);



			// La CU seleccionada ha sido modificada
			cuSeleccionadaModificada = true;

			log.debug("[cargarDeterminacionRegimenCUSeleccionada] Determinacion Regimen guardado Correctamente. nombreDetRegimen="+nombreDetRegimen);
			JsfUtil.addSuccessMessage("Determinacion Regimen guardado Correctamente");
			setMostrarPanelDetReg(false);
		}
		catch (Exception ex)
		{
			JsfUtil.addErrorMessage("Error al guardar la Determinacion Regimen ");
			setMostrarPanelDetReg(false);
		}


	}

	public void cargarValorReferenciaCUSeleccionada(int idValorReferenciaRegimen)
	{
		log.debug("[cargarValorReferenciaCUSeleccionada] idValorReferenciaRegimen="+idValorReferenciaRegimen);

		// Cargamos el valor de referencia
		try
		{
			String nombreDetValorRef=servicioBasicoCondicionesUrbanisticas.nombreDeterminacion(idValorReferenciaRegimen);

			cuSeleccionada.setIdDeterminacionValorReferencia(idValorReferenciaRegimen);
			cuSeleccionada.setNombreDetValorRef(nombreDetValorRef);

			// La CU seleccionada ha sido modificada
			cuSeleccionadaModificada = true;

			log.debug("[cargarValorReferenciaCUSeleccionada] Valor Referencia Regimen guardado Correctamente setNombreDetValorRef="+nombreDetValorRef);
			JsfUtil.addSuccessMessage("Valor Referencia Regimen guardado Correctamente");
			setMostrarPanelValRef(false);
		}
		catch (Exception ex)
		{
			JsfUtil.addErrorMessage("Error al guardar el Valor Referencia Regimen ");
			setMostrarPanelValRef(false);
		}


	}

	// CU MODIFICADA
	public boolean isCuSeleccionadaModificada() {
		return cuSeleccionadaModificada;
	}

	// CU SELECCIONADA
	public CondicionUrbanisticaSimplificadaDTO getCuSeleccionada() {
		return cuSeleccionada;
	}


	public void setCuSeleccionada(CondicionUrbanisticaSimplificadaDTO cuSeleccionada) {
		this.cuSeleccionada = cuSeleccionada;
	}

	// VALOR
	public void cargarNuevoValor()
	{
		if (nuevoValor!=null)
		{
			cuSeleccionada.setValor(nuevoValor);

			// La CU seleccionada ha sido modificada
			cuSeleccionadaModificada = true;
			statusMessages.addFromResourceBundle(Severity.INFO,"cu_valor_ok","Valor guardado Correctamente");
		}
		else
		{
			statusMessages.addFromResourceBundle(Severity.ERROR,"cu_valor_error","Error al guardar el Valor");
		}



	}

	public String getNuevoValor() {
		return "";
	}


	public void setNuevoValor(String nuevoValor) {
		this.nuevoValor = nuevoValor;
	}


	// REGIMENES ESPECIFICOS

	public void borrarRegimenEspecifico()
	{
		String idREParaBorrarString = JsfUtil.getRequestParameter("idRE");



		// Cargamos el valor de referencia
		try
		{
			int idREParaBorrar = Integer.parseInt(idREParaBorrarString);

			List<RegimenesEspecificosSimplificadosDTO> regimenesEspecificosList = cuSeleccionada.getRegimenesEspecificos();

			boolean encontradaREBorrar = false;
			int i = 0;
			for (i=0; i<regimenesEspecificosList.size() & !encontradaREBorrar;i++)			
			{
				if (regimenesEspecificosList.get(i).getIdRegimenEspecifico() == idREParaBorrar)
				{
					encontradaREBorrar = true;
				}
			}

			// Borro de la lista el RE que hemos encontrado
			regimenesEspecificosList.remove(i-1);

			// La CU seleccionada ha sido modificada
			cuSeleccionadaModificada = true;

			JsfUtil.addSuccessMessage("Regimen Especifico borrado Correctamente");
		}
		catch (NumberFormatException ex)
		{
			JsfUtil.addErrorMessage("Error al borrar el Regimen Especifico ");
		}


	}

	public void cargarRegimenEspecifico()
	{
		String nombreRegimenEsp = nuevoNombreRegimenEspecifico;
		String textoRegimenEsp = nuevoTextoRegimenEspecifico;

		// Cargamos el regimen especifico

		RegimenesEspecificosSimplificadosDTO nuevoRE = new RegimenesEspecificosSimplificadosDTO(nombreRegimenEsp, textoRegimenEsp);
		// Pongo el idFicticio para que se pueda borrar
		nuevoRE.setIdRegimenEspecifico(idRegimenEspecifico++);

		List<RegimenesEspecificosSimplificadosDTO> regimenesEspecificosList = cuSeleccionada.getRegimenesEspecificos();

		if (regimenesEspecificosList.isEmpty())
		{
			// Si esta vacio porque no hay ningun Reg Especifico, creo el arraylist
			regimenesEspecificosList = new ArrayList <RegimenesEspecificosSimplificadosDTO> ();

			regimenesEspecificosList.add(nuevoRE);
			cuSeleccionada.setRegimenesEspecificos(regimenesEspecificosList);


		}
		else
		{
			// Si ya existia algun Regimen Especifico, solo tengo que anadir el nuevo
			regimenesEspecificosList.add(nuevoRE);
		}

		nuevoNombreRegimenEspecifico="";
		nuevoTextoRegimenEspecifico="";
		// La CU seleccionada ha sido modificada
		cuSeleccionadaModificada = true;

		JsfUtil.addSuccessMessage("Regimen Especifico guardado Correctamente");


	}

	public String selecionarDetRegimen() {
		String resultado = "success";
		if(idDetRegimen != 0) {
			Determinacion detReg = servicioBasicoDeterminaciones.buscarDeterminacion(idDetRegimen);
			cuSeleccionada.setIdDeterminacionRegimen(detReg.getIden());
			cuSeleccionada.setNombreDetRegimen(detReg.getNombre());
		}

		setMostrarPanelDetReg(false);
		return resultado;
	}

	public String selecionarValReferencia() {
		String resultado = "success";
		if(idValReferencia != 0) {
			Determinacion valRef = servicioBasicoDeterminaciones.buscarDeterminacion(idValReferencia);
			cuSeleccionada.setIdDeterminacionValorReferencia(valRef.getIden());
			cuSeleccionada.setNombreDetValorRef(valRef.getNombre());
		}

		setMostrarPanelValRef(false);
		return resultado;
	}

	public String getNuevoNombreRegimenEspecifico() {
		return nuevoNombreRegimenEspecifico;
	}


	public void setNuevoNombreRegimenEspecifico(String nuevoNombreRegimenEspecifico) {
		this.nuevoNombreRegimenEspecifico = nuevoNombreRegimenEspecifico;
	}


	public String getNuevoTextoRegimenEspecifico() {
		return nuevoTextoRegimenEspecifico;
	}


	public void setNuevoTextoRegimenEspecifico(String nuevoTextoRegimenEspecifico) {
		this.nuevoTextoRegimenEspecifico = nuevoTextoRegimenEspecifico;
	}

	public void setMostrarPanelDetReg(boolean mostrarPanelDetReg) {
		this.mostrarPanelDetReg = mostrarPanelDetReg;
	}

	public boolean isMostrarPanelDetReg() {
		return mostrarPanelDetReg;
	}

	public void setMostrarPanelValRef(boolean mostrarPanelValRef) {
		this.mostrarPanelValRef = mostrarPanelValRef;
	}

	public boolean isMostrarPanelValRef() {
		return mostrarPanelValRef;
	}

	public void setIdDetRegimen(int idDetRegimen) {
		this.idDetRegimen = idDetRegimen;
	}

	public int getIdDetRegimen() {
		return idDetRegimen;
	}

	public void setIdValReferencia(int idValReferencia) {
		this.idValReferencia = idValReferencia;
	}

	public int getIdValReferencia() {
		return idValReferencia;
	}

	@Remove
	public void destroy() {
	}
}

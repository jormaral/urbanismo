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

package com.mitc.redes.editorfip.servicios.gestionfip.gestionplaneamientoencargado;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.international.StatusMessage.Severity;
import org.jboss.seam.log.Log;
import org.jboss.seam.security.digest.DigestUtils;

import com.mitc.redes.editorfip.entidades.rpm.diccionario.Ambito;
import com.mitc.redes.editorfip.entidades.rpm.diccionario.Instrumentoplan;
import com.mitc.redes.editorfip.entidades.rpm.diccionario.Tipotramite;
import com.mitc.redes.editorfip.entidades.rpm.gestionfip.PlanesEncargados;
import com.mitc.redes.editorfip.entidades.rpm.gestionfip.PlanesVigente;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Plan;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Tramite;
import com.mitc.redes.editorfip.servicios.basicos.diccionario.ServicioBasicoAmbitos;
import com.mitc.redes.editorfip.servicios.basicos.diccionario.ServicioBasicoPlanes;
import com.mitc.redes.editorfip.servicios.basicos.fip.determinaciones.ServicioBasicoDeterminaciones;
import com.mitc.redes.editorfip.servicios.gestionfip.generacionSLD.GenerarSLDdeTramites;
import com.mitc.redes.editorfip.utilidades.Textos;

@Stateful
@Name("servicioPlanEncargado")
public class GestionPlaneamientoEncargadoBean implements GestionPlaneamientoEncargado {
	
	@In 
    EntityManager entityManager;
	
	@In(create = true, required = false)
	GenerarSLDdeTramites generarSLDdeTramites;
	
	//@In(create = true)
    //PlaneamientoEncargadoHome planEncargadoHome;
	
	@In(create=true)
    FacesMessages facesMessages;
	
	@Logger 
	private Log log;
	
	private Integer idAmbSel;
	private boolean esPlanNuevo;   
	private boolean mostrarPanelAmbitos;
	
	private boolean ambitoConPlanBase;
	private boolean generarPlanVigente;
	private Plan planBase;
	
	private PlanesEncargados planEncargado;
	private Plan planVigente;
	
	private boolean modificacion;
	
	private List<SelectItem> planesBase = null;
	private List<SelectItem> planesVigentes = null;
	private List<SelectItem> listaInstrumentos = null;
	private List<SelectItem> listaTiposTramite = null;
	
	private List<SelectItem> proyecciones = null;
	
	
	@In (create = true)
	ServicioBasicoAmbitos servicioBasicoAmbitos;
	
	@In (create = true)
	ServicioBasicoPlanes servicioBasicoPlanes;
	
	@In (create = true)
	ServicioBasicoDeterminaciones servicioBasicoDeterminaciones;
	
	@Remove
	public void remove() {
	}
	
	public void cargarPlan(String iden) {
		esPlanNuevo = iden == null || iden.equalsIgnoreCase("");
		mostrarPanelAmbitos = false;
		
		//Cargamos las listas de combos
		planesBase = cargaLista(servicioBasicoPlanes.findPlanesBaseInicial());
		listaInstrumentos = cargaLista(servicioBasicoPlanes.obtenerInstrumetos());
		listaTiposTramite = cargaLista(servicioBasicoPlanes.obtenerTipoTramites());
		proyecciones = cargarProyecciones();
		
		planVigente = new Plan();
		if(esPlanNuevo){
			createInstance();
			modificacion = false;
			idAmbSel = null;			
		}
		else
		{
			planEncargado = entityManager.find(PlanesEncargados.class, Long.parseLong(iden));
			
			// Asegurar que tiene plan base
			ambitoConPlanBase=false;
			modificacion =true;
			Plan planRes = servicioBasicoPlanes.obtenerPlanDeAmbito(planEncargado.getAmbito().getIden());
			if (planRes!=null){
				ambitoConPlanBase= true;
			}
			
			planesVigentes = cargaLista(servicioBasicoPlanes.obtenerPlanesVigentesDeAmbito(planEncargado.getAmbito().getIden()));
			planBase = planEncargado.getPlanEncargado().getPlanByIdplanbase();
			if (this.planEncargado.getTramiteVigente()!=null)
				planVigente = this.planEncargado.getTramiteVigente().getPlan();
		}
	}
	
	protected PlanesEncargados createInstance() {
		
		this.planEncargado = new PlanesEncargados();
		this.planEncargado.setAmbito(new Ambito());
		this.planEncargado.setTipotramite(new Tipotramite());
		this.planEncargado.setInstrumento(new Instrumentoplan());
		
		ambitoConPlanBase = false;
		planBase = null;
		
		Plan planaux = new Plan();
		planaux.setIden(-1);
		planBase = planaux;
		planVigente = planaux;
		return this.planEncargado;
	}

	public void mostrarPanelAmbitosAL(ActionEvent actionEvent) {
		mostrarPanelAmbitos = true;
	}

	public boolean isMostrarPanelAmbitos() {
		return mostrarPanelAmbitos;
	}

	public void setMostrarPanelAmbitos(boolean mostrarPanelAmbitos) {
		this.mostrarPanelAmbitos = mostrarPanelAmbitos;
	}
	
	
	public void descartarCambios() {
		
		entityManager.clear();
		if (planEncargado.getId()!=null)
			this.cargarPlan(planEncargado.getId()+"");
		else
			this.cargarPlan("");
	}

	public Integer getIdAmbSel() {
		return idAmbSel;
	}

	public void setIdAmbSel(Integer idAmbSel) {
		this.idAmbSel = idAmbSel;
	}
	
	public void seleccionarAmbito(Integer idAmbitoSel) {
		try {
			log.debug("ID seleccionado" + idAmbitoSel);
			Ambito ambito = entityManager.find(Ambito.class, idAmbitoSel);
			
			this.planEncargado.setAmbito(ambito);
			
			String cadenaCodigoFIP = idAmbitoSel + "" + System.currentTimeMillis();
			String md5CodigoFIP = DigestUtils.md5Hex(cadenaCodigoFIP);
			
			this.planEncargado.setCodigoFip(md5CodigoFIP);
			
			setMostrarPanelAmbitos(false);
			
			//Si el ambito seleccionado tiene plan base lo asignamos.
			ambitoConPlanBase=false;
			Plan planRes = servicioBasicoPlanes.obtenerPlanDeAmbito(ambito.getIden());
			if (planRes!=null){
				ambitoConPlanBase=true;
				generarPlanVigente=false;
								
				// Busco el tramite base para asociarlo a partir del planBase
				Plan planResBase = entityManager.find(Plan.class, planRes.getIden());
				log.info("planResBase=" + planResBase.getIden());
				String queryTramiteBase = " select tram from Tramite tram where tram.plan.iden ="+planResBase.getPlanByIdplanbase().getIden();
				
				List<Tramite> tramBaseList = (List<Tramite>) entityManager.createQuery(queryTramiteBase).getResultList();
				
				planBase = planResBase;
				this.planEncargado.setTramiteBase(tramBaseList.get(0));
				planesVigentes = cargaLista(servicioBasicoPlanes.obtenerPlanesVigentesDeAmbito(ambito.getIden()));
				log.debug("Planes Vigentes" + planesVigentes.size());
				//mostramos un listado con los planes vigentes para seleccionar
			}
			else
			{
				ambitoConPlanBase=false;
				generarPlanVigente=true;
				planBase = new Plan();
				//Crear nuevo plan vigente y tramite vigente. Primero plan y luego tramite
				
			}
				
			
		} catch (Exception e) {
			log.error("[servicioUsuarioBean - seleccionarPlan] Error al seleccionar el Plan: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	public String borrar()
	{
		String resultado="OK";
		log.info("Se va a borrar el planeamiento encargado con IDEN Planeamiento= "+this.planEncargado.getId());
		
		try{
			
			int idTramiteEncargadoBorrar = this.planEncargado.getTramiteEncargado().getIden();
			
			// Borro el registro de planeamiento encargado
			entityManager.remove(this.planEncargado);
			entityManager.flush();
			
			// Borrados PREVIOS
			
			// De coloresentidad
			String borrarColoresEntidad = "delete from gestionfip.coloresentidades where identidad in "+
											" (select iden from planeamiento.entidad where idtramite="+idTramiteEncargadoBorrar+")";
			
			entityManager.createNativeQuery(borrarColoresEntidad).executeUpdate();
			
			
			// Prerefundido
			String borrarPrerefundido = "delete from gestionfip.prerefundido where idtramiteencargado ="+idTramiteEncargadoBorrar;
			
			entityManager.createNativeQuery(borrarPrerefundido).executeUpdate();
			
			this.planEncargado = new PlanesEncargados();
			
			// Borro el tramite encargado
			String selectBorrarTramite = "select planeamiento.\"limpiaTramite\"("+idTramiteEncargadoBorrar+")";
			entityManager.createNativeQuery(selectBorrarTramite).getSingleResult();
			
			// Borro el plan encargado
			String selectBorrarPlaneamiento = "select planeamiento.\"limpiaTramitePlan\"("+idTramiteEncargadoBorrar+")";
			entityManager.createNativeQuery(selectBorrarPlaneamiento).getSingleResult();
			
			facesMessages.addFromResourceBundle(Severity.INFO,"Planeamiento Encargado Borrado Correctamente", null);
			
			
			
		}		
		catch (Exception e) {
			// TODO: handle exception
	
			facesMessages.addFromResourceBundle(Severity.ERROR,"Error Borrando el Planeamiento Encargado", null);
			e.printStackTrace();
			
			resultado="ERROR";
		}
		
		 
		return resultado;
	}
	
	public String guardar()
	{
		//Guardamos el plan encargado.
		
		if (!validarFormulario())
			return "";
		try {
			
			// Sólo cuando no es modificación
			if (!modificacion)
			{
				//Si el planBase no es nulo es porque se ha cargado desde un ambito que tenía plan base
				if (!ambitoConPlanBase)
				{
					
					planBase = entityManager.find(Plan.class, planBase.getIden());
					log.debug(planBase.getIden());
					log.debug(planBase.getCodigo());
					this.planEncargado.setTramiteBase(planBase.getTramites().iterator().next());
				}
				
				//Caso en el que el plan vigente ha sido seleccionado de la lista de disponibles.
				if (planVigente!=null && planVigente.getIden()!=-1)
				{
					planVigente = entityManager.find(Plan.class, planVigente.getIden());
					this.planEncargado.setTramiteVigente(planVigente.getTramites().iterator().next());
				}
				else
				{
					/** COMENTAMOS TODO ESTO PARA QUE NO SE CREE UN PLAN VIGENTE A PETICIÓN DEL CLIENTE */
					//En este caso es necesario crear un plan vigente nuevo
					Tramite trVigente = new Tramite();
					Plan plVigente = new Plan();
					
					//Guardamos el Plan Vigente primero
					plVigente.setIdambito(planEncargado.getAmbito().getIden());
					
					//Los nombre los sacamos a archivos de recursos
					log.debug("TEXTO" + Textos.getCadena("nombre_plan_vigente_vacio"));
					plVigente.setNombre("Plan Vigente Vacio");
					plVigente.setPlanByIdplanbase(planBase);
					plVigente.setCodigo("00000");
					entityManager.persist(plVigente);
					
					trVigente.setIdtipotramite(11);
					Date fechaPV = new Date();
					fechaPV.setTime(1);
					
					trVigente.setFecha(fechaPV);
					
					String cadenaCodigoFIP = "" + System.currentTimeMillis();
					String md5CodigoFIP = DigestUtils.md5Hex(cadenaCodigoFIP);
					trVigente.setCodigofip(md5CodigoFIP);
					
					trVigente.setPlan(plVigente);
					trVigente.setNombre(Textos.getCadena("nombre_tramite_vigente_vacio"));
					entityManager.persist(trVigente);
					
					PlanesVigente pv = new PlanesVigente();
					pv.setPlan(plVigente);
					pv.setTramite(trVigente);
					entityManager.persist(pv);
					
					this.planEncargado.setTramiteVigente(trVigente);
					
					
				}
				
				//Cargamos los valores del instrumento y del tipo de trámite
				planEncargado.setInstrumento(entityManager.find(Instrumentoplan.class, planEncargado.getInstrumento().getIden()));
				planEncargado.setTipotramite(entityManager.find(Tipotramite.class, planEncargado.getTipotramite().getIden()));
				
			}
			
			// modificamos los datos del plan encargado si ya existía. En otro caso lo creamos
			if (modificacion)
			{

				//PlanesEncargados planEncargado = entityManager.find(PlanesEncargados.class, planEncargado.getId());
				//planEncargado.getPlanEncargado().setNombre(planEncargado.getNombre());
				//planEncargado.getPlanEncargado().setIdambito(planEncargado.getAmbito().getIden());
				
				//Modificamos con merge
				entityManager.merge(planEncargado);
				
				// Tambien vamos a modificar del tramite el nombre y el codigo fip
				Tramite tram = entityManager.find(Tramite.class, planEncargado.getTramiteEncargado().getIden());
				tram.setNombre(planEncargado.getNombre());
				tram.setCodigofip(planEncargado.getCodigoFip());
				entityManager.merge(tram);
				
				entityManager.flush();
			}
			else{
				log.debug("creando nuevo");
				Plan plEncargado = new Plan();
				plEncargado.setIdambito(planEncargado.getAmbito().getIden());
				plEncargado.setNombre(planEncargado.getNombre());
				// si el vigente es -1 (vacio) no le ponemos plan padre
				if (planVigente!=null)
				{
					if (planVigente.getIden()!=-1)
					{
						plEncargado.setPlanByIdpadre(planVigente);
					}
				}
				
				plEncargado.setCodigo("00000");
				
				plEncargado.setPlanByIdplanbase(planBase);
				plEncargado.setCodigo("");
				entityManager.persist(plEncargado);
				
				//Creamos el trámite encargado
				Tramite trEncargado = new Tramite();		
				trEncargado.setPlan(plEncargado);
				
				trEncargado.setNombre(planEncargado.getNombre());
				trEncargado.setCodigofip(planEncargado.getCodigoFip());
				trEncargado.setIdtipotramite(planEncargado.getTipotramite().getIden());
				trEncargado.setIdsentido(2);
				trEncargado.setIdorgano(1);
				trEncargado.setFecha(new Date());
				
				entityManager.persist(trEncargado);
				
				//Persistimos el plan encargado. Objeto nuestro
				planEncargado.setPlanEncargado(plEncargado);
				planEncargado.setTramiteEncargado(trEncargado);
				planEncargado.setFechaInicio(new Date());
				
				//Creamos con persist
				entityManager.persist(planEncargado);
				entityManager.flush();
				
				
				 
				
				// Creamos las determinaciones necesarias de unidades
				 log.info("Creamos las determinaciones necesarias de unidades ...");
				servicioBasicoDeterminaciones.crearDeterminacionesUnidades(planEncargado);
				
				log.info("Creando SLDs para el Plan Encargado ...");
				generarSLDdeTramites.generarSLD(planEncargado.getTramiteEncargado().getIden(), planEncargado.getAmbito().getIden());
				log.info("FIN Creando SLDs para el Plan Encargado ...");
				
				
			}
			
		}
		catch (Exception e) {
			// TODO: handle exception
	
			facesMessages.addFromResourceBundle(Textos.getCadena("error_guardando_planes_encargados"), null);
			e.printStackTrace();
			
			return "";
		}
		facesMessages.addFromResourceBundle(Textos.getCadena("plan_encargado_guardado_correctamente"), null);
		return "success";
		
	}
	public void eliminarAmbito(Integer idAmbito) {
		//planEncargadoHome.getInstance().setAmbito(null);
	}
	
	public String obtenerNombreAmbito(Integer idAmbito) {
		try {
			return servicioBasicoAmbitos.ambitoString(idAmbito);
		} catch (Exception e) {
			log.error("[servicioUsuarioBean - obtenerNombreAmbito] Error al obtener el nombre del ambito: " + e.getMessage());
			return "";
		}
	}

	public boolean isEsPlanNuevo() {
		return esPlanNuevo;
	}

	public void setEsPlanNuevo(boolean esPlanNuevo) {
		this.esPlanNuevo = esPlanNuevo;
	}

	public PlanesEncargados getPlanEncargado() {
		return planEncargado;
	}

	public void setPlanEncargado(PlanesEncargados planEncargado) {
		this.planEncargado = planEncargado;
	}
	
	public String getLiteralInstrumento()
	{
		if (this.planEncargado!=null && this.getPlanEncargado().getInstrumento()!=null
				&& this.getPlanEncargado().getInstrumento().getIden()>0)
			return servicioBasicoPlanes.instrumentoString(this.getPlanEncargado().getInstrumento().getIden());
		else
			return "";
	}
	
	public String getLiteralTipoTramite()
	{
		if (this.planEncargado!=null && this.getPlanEncargado().getTipotramite()!=null
				&& this.getPlanEncargado().getTipotramite().getIden()>0)
			return servicioBasicoPlanes.tipoTramiteString(this.getPlanEncargado().getTipotramite().getIden());
		else
			return "";
	}
	
	public String getLiteralAmbito()
	{
		if (this.planEncargado!=null && this.getPlanEncargado().getAmbito()!=null
				&& this.getPlanEncargado().getAmbito().getIden()>0)
			return servicioBasicoAmbitos.ambitoString(this.getPlanEncargado().getAmbito().getIden());
		else
			return "";
	}
	
	protected List<SelectItem> cargaLista(List<Object[]> elementos) {

		List<SelectItem> res = new ArrayList<SelectItem>();
		for (Object[] c : elementos) {
			SelectItem i = new SelectItem();
			i.setLabel((String) c[1]);
			i.setValue(c[0]);
			res.add(i);
		}

		//Collections.sort(res, new ComparadorItems());

		return res;
	}
	
	private List<SelectItem> cargarProyecciones()
	{
		List<SelectItem> res = new ArrayList<SelectItem>();
		
		SelectItem i = new SelectItem();
			
		i.setLabel("EPSG: 23030");
		i.setValue("EPSG:23030");
		res.add(i);
		
		i = new SelectItem();
		i.setLabel("EPSG: 23029");
		i.setValue("EPSG:23029");
		res.add(i);
		
		i = new SelectItem();
		i.setLabel("EPSG: 23028");
		i.setValue("EPSG:23028");
		res.add(i);
		
		
		
		i = new SelectItem();		
		i.setLabel("EPSG: 25828");
		i.setValue("EPSG:25828");
		res.add(i);
		
		i = new SelectItem();		
		i.setLabel("EPSG: 25830");
		i.setValue("EPSG:25830");
		res.add(i);
		
		i = new SelectItem();		
		i.setLabel("EPSG: 25831");
		i.setValue("EPSG:25831");
		res.add(i);
		
		i = new SelectItem();		
		i.setLabel("EPSG: 32630");
		i.setValue("EPSG:32630");
		res.add(i);
		
		i = new SelectItem();		
		i.setLabel("EPSG: 32628");
		i.setValue("EPSG:32628");
		res.add(i);
		
		i = new SelectItem();		
		i.setLabel("EPSG: 900913");
		i.setValue("EPSG:900913");
		res.add(i);
		
		return res;
	}
	
	public List<SelectItem> getPlanesBase() {
		return planesBase;
	}

	public void setPlanesBase(List<SelectItem> planesBase) {
		this.planesBase = planesBase;
	}

	public boolean isAmbitoConPlanBase() {
		return ambitoConPlanBase;
	}

	public void setAmbitoConPlanBase(boolean ambitoConPlanBase) {
		this.ambitoConPlanBase = ambitoConPlanBase;
	}

	public boolean isGenerarPlanVigente() {
		return generarPlanVigente;
	}

	public void setGenerarPlanVigente(boolean generarPlanVigente) {
		this.generarPlanVigente = generarPlanVigente;
	}

	public Plan getPlanBase() {
		return planBase;
	}

	public void setPlanBase(Plan planBase) {
		this.planBase = planBase;
	}

	public Plan getPlanVigente() {
		return planVigente;
	}

	public void setPlanVigente(Plan planVigente) {
		this.planVigente = planVigente;
	}

	public List<SelectItem> getPlanesVigentes() {
		return planesVigentes;
	}

	public void setPlanesVigentes(List<SelectItem> planesVigentes) {
		this.planesVigentes = planesVigentes;
	}

	public List<SelectItem> getListaInstrumentos() {
		return listaInstrumentos;
	}

	public void setListaInstrumentos(List<SelectItem> listaInstrumentos) {
		this.listaInstrumentos = listaInstrumentos;
	}

	public List<SelectItem> getListaTiposTramite() {
		return listaTiposTramite;
	}

	public void setListaTiposTramite(List<SelectItem> listaTiposTramite) {
		this.listaTiposTramite = listaTiposTramite;
	}

	public boolean isModificacion() {
		return modificacion;
	}

	public void setModificacion(boolean modificacion) {
		this.modificacion = modificacion;
	}
	
	private boolean validarFormulario()
	{
		boolean valido = true;
		
		if (planEncargado.getCodigoFip()==null || planEncargado.getCodigoFip().equals(""))
		{
			facesMessages.addFromResourceBundle(Severity.ERROR, "obligatorio_codigo_fip", null);
			return false;
		}
		if (planEncargado.getNombre()==null || planEncargado.getNombre().equals(""))
		{
			facesMessages.addFromResourceBundle(Severity.ERROR, "obligatorio_nombre", null);
			return false;
		}
		if (this.planEncargado==null || this.getPlanEncargado().getAmbito()==null
				|| this.getPlanEncargado().getAmbito().getIden()<=0)
		{
			facesMessages.addFromResourceBundle(Severity.ERROR, "obligatorio_ambito", null);
			return false;
		}
		
		return valido;
	}

	public List<SelectItem> getProyecciones() {
		return proyecciones;
	}

	public void setProyecciones(List<SelectItem> proyecciones) {
		this.proyecciones = proyecciones;
	}
	
	
	
}

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

package com.mitc.redes.editorfip.servicios.produccionfip.gestionentidades;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.faces.FacesManager;
import org.jboss.seam.international.StatusMessage.Severity;
import org.jboss.seam.international.StatusMessages;
import org.jboss.seam.log.Log;

import com.icesoft.faces.component.selectinputtext.SelectInputText;
import com.mitc.redes.editorfip.entidades.interfazgrafico.DocumentoDTO;
import com.mitc.redes.editorfip.entidades.interfazgrafico.EntidadBusquedaDTO;
import com.mitc.redes.editorfip.entidades.interfazgrafico.GeometriaDTO;
import com.mitc.redes.editorfip.entidades.rpm.gestionfip.ColoresEntidades;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Entidad;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Entidadlin;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Entidadpnt;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Entidadpol;
import com.mitc.redes.editorfip.servicios.basicos.fip.entidades.ServicioBasicoEntidades;
import com.mitc.redes.editorfip.servicios.basicos.fip.entidades.ServicioCRUDEntidades;
import com.mitc.redes.editorfip.servicios.genericos.JsfUtil;
import com.mitc.redes.editorfip.servicios.gestionfip.importadores.ServicioImportacionGeometrias;
import com.mitc.redes.editorfip.servicios.informacionfip.entidades.ServicioCopiarEntidades;
import com.mitc.redes.editorfip.servicios.sesion.VariablesSesionUsuario;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.io.WKTReader;

@Scope(ScopeType.SESSION)
@Stateful
@Name("gestionEntidades")
public class GestionEntidadesBean implements GestionEntidades
{

	@Logger private Log logger;
	@In StatusMessages statusMessages;
	@In (create = true, required = false) VariablesSesionUsuario variablesSesionUsuario;
	@In(create = true, required = false) ServicioBasicoEntidades servicioBasicoEntidades;
	@In(create = true, required = false) ServicioCRUDEntidades servicioCRUDEntidades;
	@In(create = true, required = false) ServicioCopiarEntidades servicioCopiarEntidades;
	
	@In(create = true, required = false) ServicioImportacionGeometrias servicioImportacionGeometrias;

	private int idEntidad = 0;
	private Entidad entidad = new Entidad();
	
	private int idEntidadOLD = 0;
	private int idTramiteTrabajo = 0;
	private int idEntidadSeleccionado;
	private List<SelectItem> resultadosBusqueda = new ArrayList<SelectItem>();
	private List<EntidadBusquedaDTO> resultadosEntidad = new ArrayList<EntidadBusquedaDTO>();
	private boolean filtroDetAplicada;
	private boolean filtroNombre = true;
	private boolean filtroClave;
	private boolean mostrarPanelFiltros;
	private boolean mostrarPanelEntidadBase;
	private boolean mostrarPanelEntidadPadre;

	// variable para la geometria WKT
	private String geometriaWKT="";
	
	@PersistenceContext
	EntityManager em;





	public int getIdEntidad() {
		return idEntidad;
	}

	public void setIdEntidad(int idEntidad) {
		logger.info("Estableciendo entidad ...");
		this.idEntidadOLD = this.idEntidad;
		this.idEntidad = idEntidad;
		cargarEntidad();
		logger.info("Entidad establecida.");
	}

	public int getIdEntidadBase() {
		int id = 0;

		if(entidad.getEntidadByIdentidadbase() != null) {
			id = entidad.getEntidadByIdentidadbase().getIden();
		}

		return id;
	}

	public void setIdEntidadBase(int idEntidadBase) {
		
		if (idEntidadBase!=0)
		{
			Entidad entBase = servicioBasicoEntidades.buscarEntidad(idEntidadBase);
			entidad.setEntidadByIdentidadbase(entBase) ;
		}
		else
		{
			entidad.setEntidadByIdentidadbase(null) ;
		}
		
		mostrarPanelEntidadBase = false;
	}

	public int getIdEntidadPadre() {
		int id = 0;

		if(entidad.getEntidadByIdpadre() != null) {
			id = entidad.getEntidadByIdpadre().getIden();
		}

		return id;
	}

	public void setIdEntidadPadre(int idEntidadPadre) {
		
		if (idEntidadPadre!=0)
		{
			Entidad entPadre = servicioBasicoEntidades.buscarEntidad(idEntidadPadre);
			entidad.setEntidadByIdpadre(entPadre);
		}
		else
		{
			entidad.setEntidadByIdpadre(null);
		}
		
		mostrarPanelEntidadPadre = false;
	}

	private void cargarEntidad()
	{
		// Si el idTramiteTrabajo es cero, tengo que cargarlo previamente
		// para evitar que la primera vez que se seleccione una entidad no se cargue nada
		if (idTramiteTrabajo ==0)
		{
			idTramiteTrabajo=variablesSesionUsuario.getIdTramiteTrabajoActual();
		}


		// Si la entidad es cero o se ha cambiado de tramite, cargamos una entidad vacia
		if (idEntidad==0 || idTramiteTrabajo!=variablesSesionUsuario.getIdTramiteTrabajoActual())
		{
			logger.info("[cargarEntidad] idEntidad="+idEntidad+" / idEntidadOLD="+idEntidadOLD);
			entidad = new Entidad();
			idTramiteTrabajo = variablesSesionUsuario.getIdTramiteTrabajoActual();
		}
		else
		{
			logger.info("[cargarEntidad] idEntidad="+idEntidad+" / idEntidadOLD="+idEntidadOLD);

			if (idEntidadOLD!=idEntidad)
			{
				this.entidad = servicioBasicoEntidades.buscarEntidad(idEntidad);
				idEntidadOLD=idEntidad;
			}

			logger.info("[cargarEntidad] Entidad="+entidad.getIden()+" / nombre="+entidad.getNombre());
			//logger.info("[cargarEntidad] Entidad Plan Base="+entidad.getEntidadByIdentidadbase().getNombre());

			if (entidad.getEntidadByIdentidadbase()!=null)
				logger.info("[cargarEntidad] Entidad Plan Base="+entidad.getEntidadByIdentidadbase().getNombre());

		}


	}


	public Entidad getEntidad() {



		return entidad;
	}




	public void setEntidad(Entidad entidad) {
		this.entidad = entidad;
	}




	public void guardarEntidad(ActionEvent action)
	{	
		logger.info("[guardarEntidad] Entidad.getIden():"+entidad.getIden());

		try{
			if (entidad.getIden()==0)
			{
				// Es una nueva entidad
				// Guardo el tramite al que pertenece la entidad (tramite de trabajo)

				int idTramTrabajo =variablesSesionUsuario.getIdTramiteTrabajoActual();
				entidad.setTramite(variablesSesionUsuario.getTramiteTrabajoActual());

				// Compruebo si se ha introducido una entidad base porque de lo contrario hay que ponerlo a null
				if (entidad.getEntidadByIdentidadbase()==null)
				{
					entidad.setEntidadByIdentidadbase(null);
				}

				// Compruebo si se ha introducido una entidad padre porque de lo contrario hay que ponerlo a null
				int idEntidadPadre = 0;
				if (entidad.getEntidadByIdpadre()==null)
				{
					entidad.setEntidadByIdpadre(null);
				} else 
				{
					idEntidadPadre = entidad.getEntidadByIdpadre().getIden();
				}

				entidad.setOrden(servicioBasicoEntidades.obtenerOrdenNuevaEntidad(idTramTrabajo, idEntidadPadre));
				entidad.setCodigo(servicioBasicoEntidades.nextCodigo(idTramTrabajo+""));

				// La persisto en la base de datos
				int idEntNueva = servicioCRUDEntidades.create(entidad);
				
				// Le asigno un color a la entidad
				ColoresEntidades entidadColor = new ColoresEntidades();
				entidadColor.setIdentidad(entidad.getIden());
				entidadColor.setColor("#"+(Math.round(Math.random() * 100000)));
				servicioCRUDEntidades.createColor(entidadColor);

				// log de informacion
				logger.info("[guardarEntidad] Entidad Creada Correctamente: id:"+idEntNueva +" para el tramite de trabajo:"+idTramTrabajo);
				statusMessages.addFromResourceBundleOrDefault("entidad_creada_ok", "Entidad Creada Correctamente");

				// Como ya se ha persistido la cojo de BD para que tenga el id de referencia
				setIdEntidad(idEntNueva);

			}
			else
			{
				logger.info("[guardarEntidad] Modificar: entidad con entidad.getNombre():"+entidad.getNombre());
				// Es una edicion
				servicioCRUDEntidades.edit(entidad);
				

				// log de informacion
				logger.info("[guardarEntidad] Entidad Modificada Correctamente: id:"+entidad.getIden());
				statusMessages.addFromResourceBundleOrDefault("entidad_modificada_ok","Entidad Modificada Correctamente");
			}
		}
		catch (Exception ex)
		{
			statusMessages.addFromResourceBundleOrDefault("entidad_guardar_error","ERROR: Se ha producido un error al guardar la entidad: "+ex.getLocalizedMessage() +ex.getMessage());
			logger.error("[guardarEntidad] ERROR: Se ha producido un error al guardar la entidad: "+ex.getLocalizedMessage() +ex.getMessage());

			ex.printStackTrace();
		}



	}


	public void guardarGeometriaEntidad() {
		try {

			// ------------------------
			// Guardo la geometria si se ha metido alguna geometria
			// ------------------------
			logger.info("[guardarGeometriaEntidad]Inicio " );
			if (geometriaWKT!="")
			{
				WKTReader reader = new WKTReader();
				try {
					Geometry geoWKT = reader.read(geometriaWKT);
					geoWKT.isValid();
					geoWKT.setSRID(-1);

					logger.info("[guardarGeometriaEntidad]geometria valida " );

					if (geometriaWKT.contains("MULTIPOLYGON") == true || geometriaWKT.contains("POLYGON") == true) {
						// Creo una nuevo entidadPOL
						Entidadpol entPOL_rmpAnadir = new Entidadpol();
						entPOL_rmpAnadir.setEntidad(entidad);
						entPOL_rmpAnadir.setGeom(((MultiPolygon)geoWKT));
						
						em.persist(entPOL_rmpAnadir);
						//servicioCRUDEntidades.crearGeometriaPoligono(entPOL_rmpAnadir);

						logger.info("[guardarGeometriaEntidad]- EntidadPOL CREADA" );
						JsfUtil.addSuccessMessage("Geometria poligonal de la Entidad Guardada Correctamente");
					}

					if (geometriaWKT.contains("LINESTRING") == true || geometriaWKT.contains("MULTILINESTRING") == true) {
						// Creo una nuevo entidadLIN
						Entidadlin entLIN_rmpAnadir = new Entidadlin();

						entLIN_rmpAnadir.setEntidad(entidad);
						entLIN_rmpAnadir.setGeom((MultiLineString)geoWKT);

						em.persist(entLIN_rmpAnadir);
						//servicioCRUDEntidades.crearGeometriaLineal(entLIN_rmpAnadir);


						logger.info("[guardarGeometriaEntidad]- EntidadLIN CREADA");
						JsfUtil.addSuccessMessage("Geometria lineal de la Entidad Guardada Correctamente");
					}

					if (geometriaWKT.contains("POINT") == true || geometriaWKT.contains("MULTIPOINT") == true) {
						// Creo una nuevo entidadPNT
						Entidadpnt entPNT_rmpAnadir = new Entidadpnt();

						entPNT_rmpAnadir.setEntidad(entidad);
						entPNT_rmpAnadir.setGeom((MultiPoint)geoWKT);

						em.persist(entPNT_rmpAnadir);
						//servicioCRUDEntidades.crearGeometriaPuntual(entPNT_rmpAnadir);

						logger.info("[guardarGeometriaEntidad] - EntidadPNT CREADA " );
						JsfUtil.addSuccessMessage("Geometria puntual de la Entidad Guardada Correctamente");
					}

					geometriaWKT="";


				} 
				catch (Exception ex) {
					logger.error("[validacionFormatoWKT] Se ha producido un error guardando el dato geometrico; " + ex, ex);
					JsfUtil.addErrorMessage("La geometria introducida no es valida. No se ha guardado la entidad");

				}
			}


		}

		catch (Exception e) {
			JsfUtil.addErrorMessage("Error guardando la geometria de la entidad.");
			e.printStackTrace();
		}

	}
	
	public void guardarGeometriaShapeEntidad(String geometria, String nombreBase) {
		try {

			// ------------------------
			// Guardo la geometria si se ha metido alguna geometria
			// ------------------------
			logger.info("[guardarGeometriaEntidad]Inicio " );
			if (geometria!="")
			{
				WKTReader reader = new WKTReader();
				try {
					Geometry geoWKT = reader.read(geometria);
					geoWKT.isValid();
					geoWKT.setSRID(-1);

					logger.info("[guardarGeometriaEntidad]geometria valida " );

					if (geometria.contains("MULTIPOLYGON") == true || geometria.contains("POLYGON") == true) {
						// Creo una nuevo entidadPOL
						Entidadpol entPOL_rmpAnadir = new Entidadpol();
						entPOL_rmpAnadir.setEntidad(entidad);
						entPOL_rmpAnadir.setGeom(((MultiPolygon)geoWKT));

						
						em.persist(entPOL_rmpAnadir);
						//servicioCRUDEntidades.crearGeometriaPoligono(entPOL_rmpAnadir);

						logger.info("[guardarGeometriaEntidad]- EntidadPOL CREADA" );
						JsfUtil.addSuccessMessage("Geometria poligonal de la Entidad Guardada Correctamente");
					}

					if (geometria.contains("LINESTRING") == true || geometria.contains("MULTILINESTRING") == true) {
						// Creo una nuevo entidadLIN
						Entidadlin entLIN_rmpAnadir = new Entidadlin();

						entLIN_rmpAnadir.setEntidad(entidad);
						entLIN_rmpAnadir.setGeom((MultiLineString)geoWKT);

						em.persist(entLIN_rmpAnadir);
						//servicioCRUDEntidades.crearGeometriaLineal(entLIN_rmpAnadir);
						
						


						logger.info("[guardarGeometriaEntidad]- EntidadLIN CREADA");
						JsfUtil.addSuccessMessage("Geometria lineal de la Entidad Guardada Correctamente");
					}

					if (geometria.contains("POINT") == true || geometria.contains("MULTIPOINT") == true) {
						// Creo una nuevo entidadPNT
						Entidadpnt entPNT_rmpAnadir = new Entidadpnt();

						entPNT_rmpAnadir.setEntidad(entidad);
						entPNT_rmpAnadir.setGeom((MultiPoint)geoWKT);

						em.persist(entPNT_rmpAnadir);
						//servicioCRUDEntidades.crearGeometriaPuntual(entPNT_rmpAnadir);

						logger.info("[guardarGeometriaEntidad] - EntidadPNT CREADA " );
						JsfUtil.addSuccessMessage("Geometria puntual de la Entidad Guardada Correctamente");
					}

					geometria="";
					
					// Borramos los archivos cargados. shp, dbf y shx
					String dir = System.getProperty("jboss.home.dir") + File.separator + 
	                "var" + File.separator + "importargeometria"+
	                File.separator + variablesSesionUsuario.getIdTramiteEncargadoTrabajo();
					
		    		File oldFile = new File(dir, nombreBase + "shp");
		    		oldFile.delete();
		    		
		    		oldFile = new File(dir, nombreBase + "shx");
		    		oldFile.delete();
		    		
		    		oldFile = new File(dir, nombreBase + "dbf");
		    		oldFile.delete();
		    		
		    		List<GeometriaDTO> geometriaLista = new ArrayList<GeometriaDTO>();
		    		List<DocumentoDTO> archivosTemporales = new ArrayList<DocumentoDTO>();
		    		servicioImportacionGeometrias.setArchivosTemporales(archivosTemporales);
		    		servicioImportacionGeometrias.setGeometriaLista(geometriaLista);
		    		
		    		//Redirigimos a la página
					FacesManager.instance().redirect("/produccionfip/gestionentidades/VerEntidadPlanEncargado.xhtml");

				} 
				catch (Exception ex) {
					logger.error("[validacionFormatoWKT] Se ha producido un error guardando el dato geometrico; " + ex, ex);
					JsfUtil.addErrorMessage("La geometria introducida no es valida. No se ha guardado la entidad");

				}
			}


		}

		catch (Exception e) {
			JsfUtil.addErrorMessage("Error guardando la geometria de la entidad.");
			e.printStackTrace();
		}

	}
	
	public void cerrarAnadirGeometriaShape(String nombreBase) {
		try {

			// ------------------------
			// Guardo la geometria si se ha metido alguna geometria
			// ------------------------
			logger.info("[cerrarAnadirGeometriaShape]Inicio " );
			if (nombreBase!="")
			{
				
					
				// Borramos los archivos cargados. shp, dbf y shx
				String dir = System.getProperty("jboss.home.dir") + File.separator + 
                "var" + File.separator + "importargeometria"+
                File.separator + variablesSesionUsuario.getIdTramiteEncargadoTrabajo();
				
	    		File oldFile = new File(dir, nombreBase + "shp");
	    		oldFile.delete();
	    		
	    		oldFile = new File(dir, nombreBase + "shx");
	    		oldFile.delete();
	    		
	    		oldFile = new File(dir, nombreBase + "dbf");
	    		oldFile.delete();
	    		
	    		List<GeometriaDTO> geometriaLista = new ArrayList<GeometriaDTO>();
	    		List<DocumentoDTO> archivosTemporales = new ArrayList<DocumentoDTO>();
	    		servicioImportacionGeometrias.setArchivosTemporales(archivosTemporales);
	    		servicioImportacionGeometrias.setGeometriaLista(geometriaLista);
	    		
	    		//Redirigimos a la página
				FacesManager.instance().redirect("/produccionfip/gestionentidades/VerEntidadPlanEncargado.xhtml");

				
			}
			else
			{
				//Redirigimos a la página
				FacesManager.instance().redirect("/produccionfip/gestionentidades/VerEntidadPlanEncargado.xhtml");
			}


		}

		catch (Exception e) {
			JsfUtil.addErrorMessage("Error guardando la geometria de la entidad.");
			e.printStackTrace();
		}

	}
	
	public void duplicarEntidadConCUs(ActionEvent action)
	{
		logger.info("[duplicarEntidadConCUs] Se va a proceder a duplicar la Entidad. id:"+entidad.getIden());
		
		servicioCopiarEntidades.duplicarEntidad(entidad.getIden());
		
	}


	public void borrarGeometriaEntidad(ActionEvent action)
	{	
		logger.info("[borrarGeometriaEntidad] Se va a proceder a borrar Geometria de la Entidad. id:"+entidad.getIden());

		try{

			// Es una borrado
			servicioCRUDEntidades.borrarGeometriaDeEntidad(entidad.getIden());
			
			// Borro tambien el color asociado
			servicioCRUDEntidades.borrarColorDeEntidad(entidad.getIden());

			// log de informacion
			logger.info("[borrarGeometriaEntidad] Geometria de la EntidadBorradas Correctamente: id:"+entidad.getIden());
			statusMessages.addFromResourceBundleOrDefault(Severity.INFO,"entidadgeom_borrar_ok","Geometria de la EntidadBorradas Correctamente");




		}
		catch (Exception ex)
		{
			statusMessages.addFromResourceBundleOrDefault(Severity.ERROR,"entidadgeom_borrar_error","ERROR: Se ha producido un error al borrar la Geometria de la Entidad: "+ex.getLocalizedMessage() +ex.getMessage());
			logger.error("[borrarGeometriaEntidad] ERROR: Se ha producido un error al borrar Geometria de la Entidad: "+ex.getLocalizedMessage() +ex.getMessage());

			ex.printStackTrace();

		}



	}


	public void borrarEntidadYCUEHijas(ActionEvent action)
	{	
		logger.info("[borrarEntidadYCUEHijas] Se va a proceder a borrar la entidad (e hijas) Y CUs Asociadas.id:"+entidad.getIden());

		try{

			// Es una borrado
			servicioCRUDEntidades.borrarEntidadYCURecursivo(entidad.getIden());

			// log de informacion
			logger.info("[borrarDeterminacionYCUEHijas] entidad Y CUs Asociadas A Determinacion Borrada Correctamente: id:"+entidad.getIden());
			statusMessages.addFromResourceBundleOrDefault("entidadycu_borrar_ok"," entidad Y CUs Asociadas A entidad borrada Correctamente");

			// Pongo una determinacion vacia:
			//Inicializo la determinacion y el nodo seleccionado
			entidad = new Entidad();
			setIdEntidad(0);


		}
		catch (Exception ex)
		{
			statusMessages.addFromResourceBundleOrDefault(Severity.ERROR,"entidadycu_borrar_error","ERROR: Se ha producido un error al borrarentidad Y CUs Asociadas A entidad: "+ex.getLocalizedMessage() +ex.getMessage());
			logger.error("[borrarEntidadYCUEHijas] ERROR: Se ha producido un error al borrar laentidad Y CUs Asociadas A entidad: "+ex.getLocalizedMessage() +ex.getMessage());

			ex.printStackTrace();

		}



	}


	public void actualizarLista(ValueChangeEvent event) {

		String filtro = ((SelectInputText) event.getComponent()).getValue().toString();
		int idTramite = variablesSesionUsuario.getIdTramiteTrabajoActual(); 
		List<Entidad> resultados;

		SelectInputText autoComplete = (SelectInputText) event.getComponent();

		resultadosBusqueda.clear();

		// si no se selecciona ponemos el valor a -1 para sacar un listado de elementos.

		if (autoComplete.getSelectedItem() != null)
			idEntidadSeleccionado = (Integer) autoComplete.getSelectedItem().getValue();
		else
			idEntidadSeleccionado=-1;

		if(!filtro.equalsIgnoreCase("") && idTramite != 0) {
			resultados = servicioCRUDEntidades.autocomplete(filtro, idTramite);

			resultadosBusqueda.clear();

			for(Entidad resul : resultados)
				resultadosBusqueda.add(new SelectItem(resul.getIden(), resul.getNombre()));
		}


	}

	public void actualizarListaEntidades(ValueChangeEvent event) {

		String filtro = ((SelectInputText) event.getComponent()).getValue().toString();
		int idTramite = variablesSesionUsuario.getIdTramiteTrabajoActual(); 

		SelectInputText autoComplete = (SelectInputText) event.getComponent();

		resultadosEntidad.clear();

		// si no se selecciona ponemos el valor a -1 para sacar un listado de elementos.

		if (autoComplete.getSelectedItem() != null)
			idEntidadSeleccionado = ((EntidadBusquedaDTO) autoComplete.getSelectedItem().getValue()).getIden();
		else
			idEntidadSeleccionado=-1;

		if(!filtro.equalsIgnoreCase("") && idTramite != 0) {
			//resultadosEntidad = servicioCRUDEntidades.autocomplete(filtro, idTramite);
			resultadosEntidad = servicioCRUDEntidades.autocompleteFiltros(filtro, idTramite, 
					filtroNombre, filtroDetAplicada, filtroClave);
			resultadosBusqueda.clear();

			for(EntidadBusquedaDTO resul : resultadosEntidad)
				resultadosBusqueda.add(new SelectItem(resul, resul.getNombreCompleto()));

		}
		logger.info("TAMAÑO----" + resultadosEntidad.size());


	}
	
	public void cerrarVentanaFiltros(ActionEvent event)
	{
		mostrarPanelFiltros=false;

	}

	public void aceptarFiltros(ActionEvent event)
	{
		mostrarPanelFiltros=false;

		logger.info("VALORES DE LOS FILTROS--" + filtroNombre + "--" + filtroDetAplicada);
	}

	// ActionListener's para lanzar los pop'ups de seleccion de entidad base y padre.
	public void mostrarPanelBaseAL(ActionEvent event){
		mostrarPanelEntidadBase = true;
	}

	public void mostrarPanelPadreAL(ActionEvent event){
		mostrarPanelEntidadPadre = true;
	}

	public void mostrarPanel()
	{
		mostrarPanelFiltros=true;
	}



	public void prepararParaCrear() {
		int idEntidadPadre = new Integer(idEntidad);
		int idTramite = variablesSesionUsuario.getTramiteTrabajoActual().getIden();

		setIdEntidad(0);

		if(idEntidadPadre != 0) {
			Entidad entPadre = servicioBasicoEntidades.buscarEntidad(idEntidadPadre);
			entidad.setEntidadByIdpadre(entPadre);
		}		
	}

	public void cancelar()
	{
		entidad = new Entidad();
		if (getIdEntidad()!=0){
			this.entidad = servicioBasicoEntidades.buscarEntidad(idEntidad);
			idEntidadOLD=idEntidad;
			logger.info("[cargarEntidad] entidad="+entidad.getIden());
		}else{
			if (idEntidad == 0 && idEntidadOLD == 0){
				setIdEntidad(0);
			}
			else{
				this.entidad = servicioBasicoEntidades.buscarEntidad(idEntidadOLD);
				idEntidadOLD=getIdEntidadPadre();
				logger.info("[cargarEntidad] entidad="+idEntidadOLD);
			}

		}
	}




	public String obtenerVistaActual() {
		return FacesContext.getCurrentInstance().getViewRoot().getViewId();
	}


	public int getIdEntidadSeleccionado() {
		return idEntidadSeleccionado;
	}

	public void setIdEntidadSeleccionado(int idEntidadSeleccionado) {
		this.idEntidadSeleccionado = idEntidadSeleccionado;
	}

	public List<SelectItem> getResultadosBusqueda() {
		return resultadosBusqueda;
	}

	public void setResultadosBusqueda(List<SelectItem> resultadosBusqueda) {
		this.resultadosBusqueda = resultadosBusqueda;
	}
	public List<EntidadBusquedaDTO> getResultadosEntidad() {
		return resultadosEntidad;
	}

	public void setResultadosEntidad(List<EntidadBusquedaDTO> resultadosEntidad) {
		this.resultadosEntidad = resultadosEntidad;
	}

	public boolean isFiltroDetAplicada() {
		return filtroDetAplicada;
	}

	public void setFiltroDetAplicada(boolean filtroDetAplicada) {
		this.filtroDetAplicada = filtroDetAplicada;
	}

	public boolean isFiltroNombre() {
		return filtroNombre;
	}

	public void setFiltroNombre(boolean filtroNombre) {
		this.filtroNombre = filtroNombre;
	}

	public boolean isFiltroClave() {
		return filtroClave;
	}

	public void setFiltroClave(boolean filtroClave) {
		this.filtroClave = filtroClave;
	}

	public boolean isMostrarPanelFiltros() {
		return mostrarPanelFiltros;
	}

	public void setMostrarPanelFiltros(boolean mostrarPanelFiltros) {
		this.mostrarPanelFiltros = mostrarPanelFiltros;
	}

	public boolean isMostrarPanelEntidadBase() {
		return mostrarPanelEntidadBase;
	}

	public void setMostrarPanelEntidadBase(boolean mostrarPanelEntidadBase) {
		this.mostrarPanelEntidadBase = mostrarPanelEntidadBase;
	}

	public boolean isMostrarPanelEntidadPadre() {
		return mostrarPanelEntidadPadre;
	}

	public void setMostrarPanelEntidadPadre(boolean mostrarPanelEntidadPadre) {
		this.mostrarPanelEntidadPadre = mostrarPanelEntidadPadre;
	}

	public String getGeometriaWKT() {
		return geometriaWKT;
	}

	public void setGeometriaWKT(String geometriaWKT) {
		this.geometriaWKT = geometriaWKT;
	}
	
	

	public int getIdEntidadOLD() {
		return idEntidadOLD;
	}

	@Remove
	public void destroy() {
	}
}

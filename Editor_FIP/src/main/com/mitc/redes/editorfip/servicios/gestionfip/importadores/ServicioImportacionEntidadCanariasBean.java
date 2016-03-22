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

package com.mitc.redes.editorfip.servicios.gestionfip.importadores;

import java.io.IOException;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.biff.CountryCode;
import jxl.read.biff.BiffException;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.faces.FacesManager;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.log.Log;

import com.icesoft.faces.component.inputfile.FileInfo;
import com.icesoft.faces.component.inputfile.InputFile;
import com.mitc.redes.editorfip.entidades.interfazgrafico.CUExcelDTO;
import com.mitc.redes.editorfip.entidades.interfazgrafico.CondicionUrbanisticaSimplificadaDTO;
import com.mitc.redes.editorfip.entidades.rpm.conversorfipsipu.DatosAuxiliaresImportarEntidad;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Entidad;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Tramite;
import com.mitc.redes.editorfip.servicios.basicos.fip.determinaciones.ServicioBasicoDeterminaciones;
import com.mitc.redes.editorfip.servicios.basicos.fip.entidades.ServicioBasicoCondicionesUrbanisticas;
import com.mitc.redes.editorfip.servicios.basicos.fip.entidades.ServicioBasicoEntidades;
import com.mitc.redes.editorfip.servicios.genericos.JsfUtil;
import com.mitc.redes.editorfip.servicios.produccionfip.gestionentidades.GestionEntidades;
import com.mitc.redes.editorfip.servicios.sesion.VariablesSesionUsuario;

/**
 * @author FGA
 *
 */
@Scope(ScopeType.SESSION)
@Stateful
@Name("servicioImportacionEntidadCanariasBean")
public class ServicioImportacionEntidadCanariasBean implements ServicioImportacionEntidadCanarias {
	
	   
	@Logger 
	private Log log;
	
	@In(create=true)
    FacesMessages facesMessages;

	private Workbook workbook ;
	private List<SelectItem> sheetListItems;
	private int[] selectedSheet;
	
	private List<String> listadoErroresImportacion;
	
	private List <String> listadoErrores;
	private List <Integer> listadoGruposInsertar;
	
	private static int indEtiqueta = 11;
	private static int indNombre = 5;
	private static int equivale = 8;
	private static int equivale_zon_uso = 9;
	private static int clave = 10;
	
    // Fichero a cargar
    private FileInfo currentFile = null;
    // porcentaje de carga del fichero
    private int fileProgress;
	
	@In(create = true)
	ServicioBasicoEntidades servicioBasicoEntidades;
	
	@In(create = true, required = false) ServicioBasicoCondicionesUrbanisticas servicioBasicoCondicionesUrbanisticas;
	@In(create = true, required = false) GestionEntidades gestionEntidades;
	
	@PersistenceContext
	EntityManager em;
	
	@In (create = true, required = false)
	VariablesSesionUsuario variablesSesionUsuario;
	
	public ServicioImportacionEntidadCanariasBean() {
//		super();
		
		// Creacion del contexto
		listadoErroresImportacion = new ArrayList<String>();
		currentFile = null;
		sheetListItems = new ArrayList<SelectItem>();
		
	}
	
		
	public FileInfo getCurrentFile() {
        return currentFile;
    }

    public int getFileProgress() {
        return fileProgress;
    }

    public void uploadActionListener(ActionEvent actionEvent) {
        InputFile inputFile = (InputFile) actionEvent.getSource();
        currentFile = inputFile.getFileInfo();
        
        try {
        	if(currentFile.getStatus() == FileInfo.SAVED)
        	{
        		WorkbookSettings wbSettings = new WorkbookSettings();
        		wbSettings.setEncoding("ISO-8859-1");
        		wbSettings.setLocale(new Locale("es", "ES"));
        		wbSettings.setExcelDisplayLanguage("ES"); wbSettings.setExcelRegionalSettings("ES"); wbSettings.setCharacterSet(CountryCode.SPAIN.getValue());
        		
        		workbook = Workbook.getWorkbook(currentFile.getFile(),wbSettings);
        		setSheetListItems(cargaLista(workbook.getSheets()));
        		listadoErroresImportacion = null;
        	}
        	else
        	{
        		setSheetListItems(null);
    			facesMessages.addFromResourceBundle("eldocumentonopudocargarse", null);
    			listadoErroresImportacion = null;
        	}

		} catch (BiffException e) {
			facesMessages.addFromResourceBundle("excelnovalido", null);
			setSheetListItems(null);
			listadoErroresImportacion = null;
			e.printStackTrace();
		} catch (IOException e) {
			setSheetListItems(null);
			listadoErroresImportacion = null;
			e.printStackTrace();
		}
        
    }

    
   
    public void progressListener(EventObject event) {
        InputFile ifile = (InputFile) event.getSource();
        fileProgress = ifile.getFileInfo().getPercent();
    }

    

	protected List<SelectItem> cargaLista(Sheet[] hojas) {

		List<SelectItem> res = new ArrayList<SelectItem>();
		int index = 0;
		for (Sheet hoja : hojas) {
			SelectItem i = new SelectItem();
			i.setLabel(hoja.getName());
			i.setValue(index++);
			res.add(i);
		}

		return res;
	}
		
		
	public void importarENExcel ()
	{
		int indiceFila=0;
		
			Sheet sheet = null;  
			try{
				sheet = workbook.getSheet(selectedSheet[0]); 
				
				if (sheet==null)
					facesMessages.addFromResourceBundle("errorleyendoexcelcopiarsinformato", null);
			}
			catch (Exception e) {
				facesMessages.addFromResourceBundle("errorleyendoexcelcopiarsinformato", null);
			}
			List <CUExcelDTO> listaCUenExcel = new ArrayList<CUExcelDTO>();
			listadoErrores = new ArrayList<String>();
			listadoGruposInsertar = new ArrayList<Integer>();
			
			boolean fincu = false;
			boolean anteriorFueFinGrupo = false;
			boolean anteriorFueSubGrupo = false;
			
			//Ponemos el trámite
			try {
				
			Tramite tramite  = em.find(Tramite.class, variablesSesionUsuario.getIdTramiteEncargadoTrabajo());
			
			
			if (!servicioBasicoEntidades.tieneEntidadTramite(variablesSesionUsuario.getIdTramiteEncargadoTrabajo()))
			{
				// Si hay 14 columnas, es correcto el formato y se puede seguir					
				if (sheet.getColumns()==17 && comprobarCabecerasEntidad(sheet.getRow(2),listadoErrores) 
						&& validarExcel(sheet))
				{
					// Cargo el registro de ámbito que será el padre de todos
					
					sheet = workbook.getSheet(selectedSheet[0]); 
					Cell[] registroAmbito = sheet.getRow(3);
					Cell[] registroTramite = sheet.getRow(1);
					if (registroAmbito!=null && registroAmbito[0].getContents().equalsIgnoreCase("AMB")
							&& Integer.parseInt(registroTramite[1].getContents())==variablesSesionUsuario.getIdTramiteEncargadoTrabajo())
					{
						// Grabamos el ámbito
						Entidad ambito = new Entidad();
						ambito.setClave(registroAmbito[clave].getContents());
						ambito.setEtiqueta(registroAmbito[indEtiqueta].getContents());
						ambito.setNombre(registroAmbito[indNombre].getContents());
						
						// Ponemos la equivalencia
						Entidad entEq= servicioBasicoEntidades.equivalenciaEntidadPlanBase(getEquivalenteFormateado(registroAmbito[8].getContents()),getIdTramiteBaseTrabajo());
						if (entEq!=null){
							System.out.println("ASOCIADO AHIIIIIIIIIIIIIIIIIII ,tostaita");
							ambito.setEntidadByIdentidadbase(entEq);
						}
						
						ambito.setTramite(tramite);
						ambito.setCodigo(servicioBasicoEntidades.nextCodigo(tramite.getIden()+""));
 						em.persist(ambito);
						//em.flush();
						
 						// Ambito de aplicación
						int idGrupoAplicacion = servicioBasicoEntidades.obtenerIdGrupoAplicacionPlanBase(registroAmbito[0].getContents(), variablesSesionUsuario.getIdTramiteBaseTrabajo());
						if (idGrupoAplicacion!=-1)
							crearGrupoAplicacionEntidadSeleccionada(idGrupoAplicacion, ambito.getIden());
						
						Entidad entidadBase = new Entidad();
						Entidad entidadNivel_2 = new Entidad();
						Entidad entidadNivel_3 = new Entidad();
						Entidad entidadNivel_4 = new Entidad();
						
						DatosAuxiliaresImportarEntidad datosAux = new DatosAuxiliaresImportarEntidad();
						
						// Empiezo a coger registros desde la fila 5 hasta que llegue al final de la hoja
						for (indiceFila=4; !fincu & indiceFila<sheet.getRows(); indiceFila++)
						{
							
							Cell[] registroCU = sheet.getRow(indiceFila);
							
							
							int indiceColumna = 0;
							
							if (registroCU[indiceColumna].getContents().equalsIgnoreCase("finentidades"))
							{
								// Se han terminado de leer todas las filas con condiciones urbanisticas para todas las entidades
								fincu = true;
							}
							else
							{
								if (registroCU[indiceColumna].getContents().equalsIgnoreCase("fingrupo"))
								{
									// Se han terminado de leer todas condiciones urbanisticas para la entidad, aviso porque hay que coger el nombre en la siguiente
									anteriorFueFinGrupo = true;
									
								}
								else
								{
									
									// Si el anterior fue fin de entidad, en este tengo que coger el codigo y nombre de entidad para dejarlo en memoria
									// para los siguientes y coger el grupo de aplicacion
									if (anteriorFueFinGrupo || indiceFila==5)
									{
										// El padre de la nueva entidad es el ambito anteriormente creado
										entidadBase = new Entidad();
										entidadBase.setEntidadByIdpadre(ambito);
										entidadBase.setTramite(tramite);
										
										String nombreEntidad = registroCU[0].getContents();
										
										// Nombre de la entidad
										if (existeCodigoEntidad(nombreEntidad))
										{
											entidadBase.setNombre(registroCU[1].getContents());
										}
										else
											listadoErrores.add("No se ha encontrado codigo entidad para la fila: "+(indiceFila+1));
										
										// Clave y Etiqueta
										
										String claveEntidad = registroCU[clave].getContents();
										
										if (claveEntidad!="" && !claveEntidad.equalsIgnoreCase(""))
										{
											entidadBase.setClave(claveEntidad);
										}
										else
											listadoErrores.add("No se ha encontrado clave para la fila: "+(indiceFila+1));
										
										Entidad ent = null;
										if (registroCU[equivale_zon_uso].getContents()!=null && !registroCU[equivale_zon_uso].getContents().equals("")){
											ent= servicioBasicoEntidades.equivalenciaEntidadPlanBase(getEquivalenteFormateado(registroCU[equivale_zon_uso].getContents()),getIdTramiteBaseTrabajo());
											if (ent!=null)
												entidadBase.setEntidadByIdentidadbase(ent);
											else
												listadoErrores.add("No se ha encontrado el Equivalente ZON_USO de la fila: "+(indiceFila+1));
										}
										else if (registroCU[equivale].getContents()!=null && !registroCU[equivale].getContents().equals("")){
											ent= servicioBasicoEntidades.equivalenciaEntidadPlanBase(getEquivalenteFormateado(registroCU[equivale].getContents()),getIdTramiteBaseTrabajo());
											if (ent!=null)
												entidadBase.setEntidadByIdentidadbase(ent);
											else
												listadoErrores.add("No se ha encontrado el Equivalente de la fila: "+(indiceFila+1));
										}
										
										String etiqueta = registroCU[indEtiqueta].getContents();
										if (etiqueta!="" && !etiqueta.equalsIgnoreCase(""))
											entidadBase.setEtiqueta(etiqueta);
										
										// Creamos la nueva entidad  
										entidadBase.setCodigo(servicioBasicoEntidades.nextCodigo(entidadBase.getTramite().getIden()+""));
										em.persist(entidadBase);
										
										datosAux = new DatosAuxiliaresImportarEntidad();
										datosAux.setEntidad(entidadBase);
										datosAux.setDescripcionEtiqueta(registroCU[12].getContents());
										datosAux.setObservaciones(registroCU[13].getContents());
										
										em.persist(datosAux);
										
										// Creamos la condición urbanistica
										idGrupoAplicacion = servicioBasicoEntidades.obtenerIdGrupoAplicacionPlanBase(registroCU[0].getContents(), variablesSesionUsuario.getIdTramiteBaseTrabajo());
										if (idGrupoAplicacion!=-1)
											crearGrupoAplicacionEntidadSeleccionada(idGrupoAplicacion, entidadBase.getIden());
										
										//em.flush();
										
										//Pongo a false la variable anteriorFueFinGrupo para que no entre mas aqui
										anteriorFueFinGrupo = false;
										anteriorFueSubGrupo = false;
									}
									// Si es del nuvel 2....
									else if (registroCU[3].getContents()!=null && !registroCU[3].getContents().equalsIgnoreCase(""))
									{
										
										// El padre de la nueva entidad es el ambito anteriormente creado
										entidadNivel_2 = new Entidad();
										entidadNivel_2.setEntidadByIdpadre(entidadBase);
										entidadNivel_2.setTramite(tramite);
									
										entidadNivel_2.setNombre(registroCU[3].getContents());
										
										//Orden
										String ordenEntidad = registroCU[2].getContents();
										
										if (ordenEntidad!=null && isNumeric(ordenEntidad))
										{
											entidadNivel_2.setOrden(Integer.parseInt(ordenEntidad));
										}
										else
											listadoErrores.add("No se ha encontrado orden numérico para la fila: "+(indiceFila+1));
										
										// Clave y Etiqueta
										
										String claveEntidad = registroCU[clave].getContents();
										
										if (claveEntidad!=null && !claveEntidad.equalsIgnoreCase(""))
										{
											entidadNivel_2.setClave(claveEntidad);
										}
										else
											listadoErrores.add("No se ha encontrado clave para la fila: "+(indiceFila+1));
										
										String etiqueta = registroCU[indEtiqueta].getContents();
										if (etiqueta!="" && !etiqueta.equalsIgnoreCase(""))
											entidadNivel_2.setEtiqueta(etiqueta);
										
										Entidad ent = null;
										if (registroCU[equivale_zon_uso].getContents()!=null && !registroCU[equivale_zon_uso].getContents().equals("")){
											ent= servicioBasicoEntidades.equivalenciaEntidadPlanBase(getEquivalenteFormateado(registroCU[equivale_zon_uso].getContents()),getIdTramiteBaseTrabajo());
											if (ent!=null)
												entidadNivel_2.setEntidadByIdentidadbase(ent);
											else
												listadoErrores.add("No se ha encontrado el Equivalente ZON_USO de la fila: "+(indiceFila+1));
										}
										else if (registroCU[equivale].getContents()!=null && !registroCU[equivale].getContents().equals("")){
											ent= servicioBasicoEntidades.equivalenciaEntidadPlanBase(getEquivalenteFormateado(registroCU[equivale].getContents()),getIdTramiteBaseTrabajo());
											if (ent!=null)
												entidadNivel_2.setEntidadByIdentidadbase(ent);
											else
												listadoErrores.add("No se ha encontrado el Equivalente de la fila: "+(indiceFila+1));
										}
										
										// Creamos la nueva entidad  
										entidadNivel_2.setCodigo(servicioBasicoEntidades.nextCodigo(entidadNivel_2.getTramite().getIden()+""));
										em.persist(entidadNivel_2);
										
										// GUARDAMOS LA ENTIDAD
										datosAux = new DatosAuxiliaresImportarEntidad();
										datosAux.setEntidad(entidadNivel_2);
										datosAux.setDescripcionEtiqueta(registroCU[12].getContents());
										datosAux.setObservaciones(registroCU[13].getContents());
										
										em.persist(datosAux);
										
										anteriorFueSubGrupo = true;
										
										// Creamos la condición urbanistica
										idGrupoAplicacion = servicioBasicoEntidades.obtenerIdGrupoAplicacionPlanBase(registroCU[0].getContents(), variablesSesionUsuario.getIdTramiteBaseTrabajo());
										if (idGrupoAplicacion!=-1)
											crearGrupoAplicacionEntidadSeleccionada(idGrupoAplicacion, entidadNivel_2.getIden());
										
									}
									// Si es de nivel 3
									else if (registroCU[5].getContents()!=null && !registroCU[5].getContents().equalsIgnoreCase(""))
									{
										// El padre de la nueva entidad es el ambito anteriormente creado
										entidadNivel_3 = new Entidad();
										entidadNivel_3.setTramite(tramite);
										
										// Si el subgrupo está vacío el padre es la entidad base
										if (!anteriorFueSubGrupo)
											entidadNivel_3.setEntidadByIdpadre(entidadBase);
										else
											entidadNivel_3.setEntidadByIdpadre(entidadNivel_2);
										
										entidadNivel_3.setNombre(registroCU[5].getContents());
										
										//Orden
										String ordenEntidad = registroCU[4].getContents();
										
										if (ordenEntidad!=null && isNumeric(ordenEntidad))
										{
											entidadNivel_3.setOrden(Integer.parseInt(ordenEntidad));
										}
										else
											listadoErrores.add("No se ha encontrado orden numérico para la fila: "+(indiceFila+1));
										
										// Clave y Etiqueta
										
										String claveEntidad = registroCU[clave].getContents();
										
										if (claveEntidad!="" && !claveEntidad.equalsIgnoreCase(""))
										{
											entidadNivel_3.setClave(claveEntidad);
										}
										else
											listadoErrores.add("No se ha encontrado clave para la fila: "+(indiceFila+1));
										
										String etiqueta = registroCU[indEtiqueta].getContents();
										if (etiqueta!="" && !etiqueta.equalsIgnoreCase(""))
											entidadNivel_3.setEtiqueta(etiqueta);
										
										Entidad ent = null;
										if (registroCU[equivale_zon_uso].getContents()!=null && !registroCU[equivale_zon_uso].getContents().equals("")){
											ent= servicioBasicoEntidades.equivalenciaEntidadPlanBase(getEquivalenteFormateado(registroCU[equivale_zon_uso].getContents()),getIdTramiteBaseTrabajo());
											if (ent!=null)
												entidadNivel_3.setEntidadByIdentidadbase(ent);
											else
												listadoErrores.add("No se ha encontrado el Equivalente ZON_USO de la fila: "+(indiceFila+1));
										}
										else if (registroCU[equivale].getContents()!=null && !registroCU[equivale].getContents().equals("")){
											ent= servicioBasicoEntidades.obtenerEquivalente(getEquivalenteFormateado(registroCU[equivale].getContents()),getIdTramiteBaseTrabajo());
											if (ent!=null)
												entidadNivel_3.setEntidadByIdentidadbase(ent);
											else
												listadoErrores.add("No se ha encontrado el Equivalente de la fila: "+(indiceFila+1));
										}
										
										// Creamos la nueva entidad
										entidadNivel_3.setCodigo(servicioBasicoEntidades.nextCodigo(entidadNivel_3.getTramite().getIden()+""));
										em.persist(entidadNivel_3);
										//em.flush();
										
										// GUARDAMOS LA ENTIDAD
										datosAux = new DatosAuxiliaresImportarEntidad();
										datosAux.setEntidad(entidadNivel_3);
										datosAux.setDescripcionEtiqueta(registroCU[12].getContents());
										datosAux.setObservaciones(registroCU[13].getContents());
										
										em.persist(datosAux);
										
										// Creamos la condición urbanistica
										idGrupoAplicacion = servicioBasicoEntidades.obtenerIdGrupoAplicacionPlanBase(registroCU[0].getContents(), variablesSesionUsuario.getIdTramiteBaseTrabajo());
										if (idGrupoAplicacion!=-1)
											crearGrupoAplicacionEntidadSeleccionada(idGrupoAplicacion, entidadNivel_3.getIden());
										
									}
									// Si es de nivel 4
									else if (registroCU[7].getContents()!=null && !registroCU[7].getContents().equalsIgnoreCase(""))
									{
										// El padre de la nueva entidad es el ambito anteriormente creado
										entidadNivel_4 = new Entidad();
										entidadNivel_4.setTramite(tramite);
										
										// Si el subgrupo está vacío el padre es la entidad base
										if (!anteriorFueSubGrupo)
											entidadNivel_4.setEntidadByIdpadre(entidadBase);
										else
											entidadNivel_4.setEntidadByIdpadre(entidadNivel_3);
										
										entidadNivel_4.setNombre(registroCU[7].getContents());
										
										//Orden
										String ordenEntidad = registroCU[6].getContents();
										
										if (ordenEntidad!=null && isNumeric(ordenEntidad))
										{
											entidadNivel_4.setOrden(Integer.parseInt(ordenEntidad));
										}
										else
											listadoErrores.add("No se ha encontrado orden numérico para la fila: "+(indiceFila+1));
										
										// Clave y Etiqueta
										
										String claveEntidad = registroCU[clave].getContents();
										
										if (claveEntidad!="" && !claveEntidad.equalsIgnoreCase(""))
										{
											entidadNivel_4.setClave(claveEntidad);
										}
										else
											listadoErrores.add("No se ha encontrado clave para la fila: "+(indiceFila+1));
										
										String etiqueta = registroCU[indEtiqueta].getContents();
										if (etiqueta!="" && !etiqueta.equalsIgnoreCase(""))
											entidadNivel_4.setEtiqueta(etiqueta);
										
										Entidad ent = null;
										if (registroCU[equivale_zon_uso].getContents()!=null && !registroCU[equivale_zon_uso].getContents().equals("")){
											ent= servicioBasicoEntidades.equivalenciaEntidadPlanBase(getEquivalenteFormateado(registroCU[equivale_zon_uso].getContents()),getIdTramiteBaseTrabajo());
											if (ent!=null)
												entidadNivel_4.setEntidadByIdentidadbase(ent);
											else
												listadoErrores.add("No se ha encontrado el Equivalente ZON_USO de la fila: "+(indiceFila+1));
										}
										else if (registroCU[equivale].getContents()!=null && !registroCU[equivale].getContents().equals("")){
											ent= servicioBasicoEntidades.obtenerEquivalente(getEquivalenteFormateado(registroCU[equivale].getContents()),getIdTramiteBaseTrabajo());
											if (ent!=null)
												entidadNivel_4.setEntidadByIdentidadbase(ent);
											else
												listadoErrores.add("No se ha encontrado el Equivalente de la fila: "+(indiceFila+1));
										}
										
										// Creamos la nueva entidad
										entidadNivel_4.setCodigo(servicioBasicoEntidades.nextCodigo(entidadNivel_4.getTramite().getIden()+""));
										em.persist(entidadNivel_4);
										//em.flush();
										
										// GUARDAMOS LA ENTIDAD
										datosAux = new DatosAuxiliaresImportarEntidad();
										datosAux.setEntidad(entidadNivel_4);
										datosAux.setDescripcionEtiqueta(registroCU[12].getContents());
										datosAux.setObservaciones(registroCU[13].getContents());
										
										em.persist(datosAux);
										
										// Creamos la condición urbanistica
										idGrupoAplicacion = servicioBasicoEntidades.obtenerIdGrupoAplicacionPlanBase(registroCU[0].getContents(), variablesSesionUsuario.getIdTramiteBaseTrabajo());
										if (idGrupoAplicacion!=-1)
											crearGrupoAplicacionEntidadSeleccionada(idGrupoAplicacion, entidadNivel_4.getIden());
										
									}
								}
								
								
							}
						}
					}
					
					// Si no ha alcanzado 'finentidades' es un error
					if (!fincu)
					{
						listadoErrores.add("No se ha encontrado 'finentidades' en el excel o el numero de trámite indicado " +
								"no coincide con el del excel");

					}
				}
				else
				{
					// No es correcto el formato de las columnas
					listadoErrores.add("No es correcto el formato de las columnas en el excel. Comprueba que tiene SOLO 17 Columnas. Vuelva a Copiar en una nueva Hoja esas 17 columnas");
				}
				
				
				// Si han habido errores 'previos', no se manda al servicio
				if (listadoErrores.size()>0)
				{
					if (servicioBasicoEntidades.tieneEntidadTramite(variablesSesionUsuario.getIdTramiteEncargadoTrabajo()))
					{
						servicioBasicoEntidades.borrarEntidadesTramite(variablesSesionUsuario.getIdTramiteEncargadoTrabajo());
					}
					
					log.error("Hay "+listadoErrores.size()+" Errores Previos. ");
					facesMessages.addFromResourceBundle("erroralimportarelexcel", null);
					
					listadoErroresImportacion = listadoErrores;
									
					
				}
				else
				{
					facesMessages.addFromResourceBundle("excelimportadook", null);
					
				}
				
			}
			
			else
			{
				facesMessages.addFromResourceBundle("eltramitecontieneentidades", null);

			}
			
			
			
			
			
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			facesMessages.addFromResourceBundle("errorinesperadoexcel", null);
			facesMessages.addFromResourceBundle("eltramitecontieneentidades", null);
			
			facesMessages.addFromResourceBundle("problemasenlafila", new Object[]{indiceFila+1});
			
			//JsfUtil.addErrorMessage("Error INESPEDERADO el EXCEL:");
			//JsfUtil.addErrorMessage("Problemas en la fila: "+(indiceFila+1));
		
			//JsfUtil.addErrorMessage(e.getMessage());
			//JsfUtil.addErrorMessage(e.toString());

		}
		em.flush();
		
	}

	public List<String> getListadoErroresImportacion() {
		return listadoErroresImportacion;
	}
	
	private int getIdTramiteBaseTrabajo() {
		
		return variablesSesionUsuario.getIdTramiteBaseTrabajo();
	}

	private boolean existeCodigoEntidad(String codigoEntidad)
	{
		if (codigoEntidad==null)
			return false;
		else if (codigoEntidad.equalsIgnoreCase("ACC") || codigoEntidad.equalsIgnoreCase("AFE")
				|| codigoEntidad.equalsIgnoreCase("ALN") || codigoEntidad.equalsIgnoreCase("AFE(L)")
				|| codigoEntidad.equalsIgnoreCase("AMB") || codigoEntidad.equalsIgnoreCase("CLA")
				|| codigoEntidad.equalsIgnoreCase("DES") || codigoEntidad.equalsIgnoreCase("GES")
				|| codigoEntidad.equalsIgnoreCase("GES_AP") || codigoEntidad.equalsIgnoreCase("GES_S")
				|| codigoEntidad.equalsIgnoreCase("GES_UA") || codigoEntidad.equalsIgnoreCase("GES_UE")
				|| codigoEntidad.equalsIgnoreCase("LDO") || codigoEntidad.equalsIgnoreCase("PRO")
				|| codigoEntidad.equalsIgnoreCase("LDO") || codigoEntidad.equalsIgnoreCase("PRO")
				|| codigoEntidad.equalsIgnoreCase("PRO(L)") || codigoEntidad.equalsIgnoreCase("PRO(P)")
				|| codigoEntidad.equalsIgnoreCase("RES") || codigoEntidad.equalsIgnoreCase("RG_ENP")
				|| codigoEntidad.equalsIgnoreCase("RST") || codigoEntidad.equalsIgnoreCase("RUS")
				|| codigoEntidad.equalsIgnoreCase("RUS_SUB") || codigoEntidad.equalsIgnoreCase("RUS_SUB(L)")
				|| codigoEntidad.equalsIgnoreCase("RUS_SUB(P)") || codigoEntidad.equalsIgnoreCase("RVP")
				|| codigoEntidad.equalsIgnoreCase("SG") || codigoEntidad.equalsIgnoreCase("SG(L)")
				|| codigoEntidad.equalsIgnoreCase("SG(P)") || codigoEntidad.equalsIgnoreCase("SL")
				|| codigoEntidad.equalsIgnoreCase("SL(L)") || codigoEntidad.equalsIgnoreCase("SL(P)")
				|| codigoEntidad.equalsIgnoreCase("SU") || codigoEntidad.equalsIgnoreCase("SU_SUB")
				|| codigoEntidad.equalsIgnoreCase("SUR") || codigoEntidad.equalsIgnoreCase("SUR_SUB")
				|| codigoEntidad.equalsIgnoreCase("UG") || codigoEntidad.equalsIgnoreCase("ZON_EDIF")
				|| codigoEntidad.equalsIgnoreCase("ZON_ENP") || codigoEntidad.equalsIgnoreCase("ZON_USO")
				)
			return true;
		else
			return false;
	}

	public List<SelectItem> getSheetListItems() {
		return sheetListItems;
	}


	public void setSheetListItems(List<SelectItem> sheetListItems) {
		this.sheetListItems = sheetListItems;
	}


	public int[] getSelectedSheet() {
		return selectedSheet;
	}


	public void setSelectedSheet(int[] selectedSheet) {
		this.selectedSheet = selectedSheet;
	}
	
	private static String getEquivalenteFormateado(String codigo)
	{
		return codigo.trim();
	}
	
	public boolean comprobarCabecerasEntidad(Cell[] row, List <String> listadoErrores)
	{
		boolean correcto= true;
		
		if (!row[0].getContents().toLowerCase().equals("grupo"))
		{
			listadoErrores.add("Fallo en la columna de: 'grupo'");
			correcto=false;
		}
		else if (!row[2].getContents().toLowerCase().equals("subgrupo"))
		{
			listadoErrores.add("Fallo en la columna de: 'subgrupo'");
			correcto=false;
		}
		else if (!row[4].getContents().toLowerCase().equals("entidades"))
		{
			listadoErrores.add("Fallo en la columna de: 'entidades'");
			correcto=false;
		}
		else if (!row[6].getContents().toLowerCase().equals("entidades - nivel 1"))
		{
			listadoErrores.add("Fallo en la columna de: 'Entidades - Nivel 1'");
			correcto=false;
		}
		else if (!row[8].getContents().toLowerCase().equals("equivalencia"))
		{
			listadoErrores.add("Fallo en la columna de: 'equivalencia'");
			correcto=false;
		}
		else if (!row[9].getContents().toLowerCase().equals("equivalencia (subcategoria zon_uso)"))
		{
			listadoErrores.add("Fallo en la columna de: 'Equivalencia (Subcategoria ZON_USO)'");
			correcto=false;
		}
		else if (!row[10].getContents().toLowerCase().equals("clave"))
		{
			listadoErrores.add("Fallo en la columna de: 'clave'");
			correcto=false;
		}
		else if (!row[11].getContents().toLowerCase().equals("etiqueta"))
		{
			listadoErrores.add("Fallo en la columna de: 'etiqueta'");
			correcto=false;
		}
		else if (!row[12].getContents().toLowerCase().equals("descripcion_etiqueta"))
		{
			listadoErrores.add("Fallo en la columna de: 'Descripcion_Etiqueta'");
			correcto=false;
		}
		else if (!row[13].getContents().toLowerCase().equals("observaciones"))
		{
			listadoErrores.add("Fallo en la columna de: 'Observaciones'");
			correcto=false;
		}
		
		return correcto;
		
	}
	
	public boolean isNumeric(String cadena){
		try {
			Integer.parseInt(cadena);
			return true;
		} catch (NumberFormatException nfe){
			return false;
		}
	}
	
	public boolean validarExcel(Sheet sheet){
		
		// Empiezo a coger registros desde la fila 5 hasta que llegue al final de la hoja
		int indiceFila = 4;
		boolean fin = false;
		boolean anteriorFueFinGrupo = false;
		
		for (indiceFila=4; !fin & indiceFila<sheet.getRows(); indiceFila++)
		{
			
			Cell[] registroCU = sheet.getRow(indiceFila);
			
			
			int indiceColumna = 0;
			
			if (registroCU[indiceColumna].getContents().equalsIgnoreCase("finentidades"))
			{
				// Se han terminado de leer todas las filas con condiciones urbanisticas para todas las entidades
				fin = true;
			}
			else
			{
				if (registroCU[indiceColumna].getContents().equalsIgnoreCase("fingrupo"))
				{
					// Se han terminado de leer todas condiciones urbanisticas para la entidad, aviso porque hay que coger el nombre en la siguiente
					anteriorFueFinGrupo = true;
					
				}
				else
				{
					Entidad ent = null;
					
					// Nombre de la entidad
					if (!existeCodigoEntidad(registroCU[0].getContents()))
					{
						listadoErrores.add("No se ha encontrado codigo entidad para la fila: "+(indiceFila+1));
					}
					
					if (registroCU[equivale_zon_uso].getContents()!=null && !registroCU[equivale_zon_uso].getContents().equals("")){
						ent= servicioBasicoEntidades.equivalenciaEntidadPlanBase(getEquivalenteFormateado(registroCU[equivale_zon_uso].getContents()),getIdTramiteBaseTrabajo());
						if (ent==null)
							listadoErrores.add("No se ha encontrado el Equivalente ZON_USO de la fila: "+(indiceFila+1));
					}
					else if (registroCU[equivale].getContents()!=null && !registroCU[equivale].getContents().equals("")){
						ent= servicioBasicoEntidades.equivalenciaEntidadPlanBase(getEquivalenteFormateado(registroCU[equivale].getContents()),getIdTramiteBaseTrabajo());
						if (ent==null)
							listadoErrores.add("No se ha encontrado el Equivalente de la fila: "+(indiceFila+1));
					}
				
				
					if (registroCU[3].getContents()!=null && !registroCU[3].getContents().equalsIgnoreCase("")){
						//Orden
						String ordenEntidad = registroCU[2].getContents();
						
						if (ordenEntidad==null || !isNumeric(ordenEntidad))
							listadoErrores.add("No se ha encontrado orden numérico para la fila: "+(indiceFila+1));
					}
					if (registroCU[5].getContents()!=null && !registroCU[5].getContents().equalsIgnoreCase("")){
						//Orden
						String ordenEntidad = registroCU[4].getContents();
						
						if (ordenEntidad==null || !isNumeric(ordenEntidad))
							listadoErrores.add("No se ha encontrado orden numérico para la fila: "+(indiceFila+1));
					}
					if (registroCU[7].getContents()!=null && !registroCU[7].getContents().equalsIgnoreCase("")){
						//Orden
						String ordenEntidad = registroCU[6].getContents();
						
						if (ordenEntidad==null || !isNumeric(ordenEntidad))
							listadoErrores.add("No se ha encontrado orden numérico para la fila: "+(indiceFila+1));
					}
					// Clave y Etiqueta
					
					String claveEntidad = registroCU[clave].getContents();
					if (claveEntidad=="" || claveEntidad.equalsIgnoreCase(""))
						listadoErrores.add("No se ha encontrado clave para la fila: "+(indiceFila+1));
				
				}
			}
		}
		
		if (!fin)
			listadoErrores.add("No se ha encontrado finentidades");
		
		if (listadoErrores==null || listadoErrores.size()==0)
			return true;
		else
			return false;
	}
	
	@Remove
	public void destroy(){}


	public List<String> getListadoErrores() {
		return listadoErrores;
	}


	public void setListadoErrores(List<String> listadoErrores) {
		this.listadoErrores = listadoErrores;
	};
	
	public void crearGrupoAplicacionEntidadSeleccionada(int idVRGA, int idEntidad)
	{


		int idTramiteBase= variablesSesionUsuario.getIdTramiteBaseTrabajo();
		log.info("[crearGrupoAplicacionEntidadSeleccionada] Inicio: idTramiteBase ="+idTramiteBase+" / idVRGrupoAplicacion ="+idVRGA);


		try
		{


			int idDet = 0;

			// Cargo una nueva
			CondicionUrbanisticaSimplificadaDTO cuSeleccionada = new CondicionUrbanisticaSimplificadaDTO();

			// Le meto los valores de entidad y determinacion de la CU
			cuSeleccionada.setIdEntidad(idEntidad);

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
}


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
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.log.Log;

import com.icesoft.faces.component.inputfile.FileInfo;
import com.icesoft.faces.component.inputfile.InputFile;
import com.mitc.redes.editorfip.entidades.comunes.ParIdentificadorTexto;
import com.mitc.redes.editorfip.entidades.interfazgrafico.DEExcelDTO;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Determinacion;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Determinaciongrupoentidad;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Tramite;
import com.mitc.redes.editorfip.servicios.basicos.fip.determinaciones.ServicioBasicoDeterminaciones;
import com.mitc.redes.editorfip.servicios.sesion.VariablesSesionUsuario;


@Scope(ScopeType.SESSION)
@Stateful
@Name("servicioImportacionDECanariasBean")
public class ServicioImportacionDECanariasBean implements ServicioImportacionDECanarias{
	
	   
	@Logger private Log log;

	private Workbook workbook ;
	private List<SelectItem> sheetListItems;
	private int[] selectedSheet;
	
	private List<String> listadoErroresImportacion;
	
	private List<String> listadoErroresDeterminacion;
	
    // Fichero a cargar
    private FileInfo currentFile = null;
    // porcentaje de carga del fichero
    private int fileProgress;
  //servicios
    
	@In(create=true)
    FacesMessages facesMessages;
	
	@In (create = true, required = false)
	VariablesSesionUsuario variablesSesionUsuario;
	
	@In(create = true, required = false)
	ServicioBasicoDeterminaciones servicioBasicoDeterminaciones;
	
	@In (create = true, required = false)
	ImportadorDECanariasExcel importadorDECanariasExcel;
	
	@PersistenceContext
	EntityManager em;
	
    private static int caracter=18;
    private static int referencia=19;
    private static int equivale=20;
    private static int estructura=24;
    private static int unidad=22;
    private static int regulacion=26;
    private static int grupoAplicacion=23;
    
	public ServicioImportacionDECanariasBean() {
//		super();
			
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
		listadoErroresDeterminacion = new ArrayList<String>();
		int indiceFila=0;
		try {
			//Workbook workbook = Workbook.getWorkbook(new File("C:\\Fran_Compartida\\Prueba_Importador_Alcala.xls"));
			
			
			Sheet sheet = workbook.getSheet(selectedSheet[0]); 
					
			List <DEExcelDTO> listaDEenExcel = new ArrayList<DEExcelDTO>();
			List <String> listadoErrores = new ArrayList<String>();
			
			boolean fincu = false;
			
			log.debug("Num Columnas="+sheet.getColumns());
			
			
			//Ponemos el trámite
			
			Tramite tramite  = em.find(Tramite.class, getIdTramiteTrabajo());
			
			if (!servicioBasicoDeterminaciones.tieneDeterminacionTramite(getIdTramiteTrabajo()))
			{
				// Si hay 36 columnas, es correcto el formato y se puede seguir
				//log.debug("Columnas " + sheet.getColumns());
				if (sheet.getColumns()==36 
						&& comprobarCabecerasDeterminacion(sheet.getRow(1),listadoErrores)
						&& validarOrden(sheet, listadoErrores))
				{
					
					// Creamos una Determinación para cada nivel
					DEExcelDTO determinacion = new DEExcelDTO();
					
					// Vamos a crear unos enteros para saber cual la referencia del ultimo nivel
					int ordenN1 = -1;
					int ordenN2 = -1;
					int ordenN3 = -1;
					int ordenN4 = -1;
					
					int ordenN5 = -1;
					int ordenN6 = -1;
					int ordenN7 = -1;
					int ordenN8 = -1;
					
					// Empiezo a coger registros desde la fila 3 hasta que llegue al final de la hoja
					for (indiceFila=2; !fincu & indiceFila<sheet.getRows(); indiceFila++)
					{
						
						Cell[] registroCU = sheet.getRow(indiceFila);
						
						int indiceColumna = 0;
						//log.debug("Estamos en " + indiceFila + "---" + registroCU[indiceColumna].getContents());
						//log.debug(registroCU);
						if (registroCU[indiceColumna].getContents().equalsIgnoreCase("findeterminaciones"))
						{
							// Se han terminado de leer todas las filas con condiciones urbanisticas para todas las entidades
							fincu = true;
						}
						else
						{
							int idCaracter = servicioBasicoDeterminaciones.getIdentificadorCaracter(registroCU[caracter].getContents());
							if (idCaracter!=-1)
							{
							
								// Comprobamos si es nivel 1
								if (registroCU[1].getContents()!=null && !registroCU[1].getContents().equalsIgnoreCase(""))
								{
									//log.debug("-NIVEL 1");
									// La determinación Base no tiene padre
									determinacion = cargarDEDTOValoresBase (registroCU, 0, idCaracter,indiceFila+1);
									
									Determinacion dt = null;
									if (registroCU[equivale].getContents()!=null && !registroCU[equivale].getContents().equals("")){
										dt= servicioBasicoDeterminaciones.obtenerEquivalenteOrden(getOrdenEquivalente(registroCU[equivale].getContents()), getNombreEquivalente(registroCU[equivale].getContents()),getIdTramiteBaseTrabajo());
										if (dt!=null)
											determinacion.setIdDeterminacionBase(dt.getIden());
										else
											listadoErrores.add("No se ha encontrado el Equivalente de la fila: "+(indiceFila+1));
									}
									determinacion.setIdListaPadre(-1);
									// Creamos la nueva entidad  
									listaDEenExcel.add(determinacion);
									
									//Ponemos a nulos el resto de niveles
									ordenN1 = indiceFila - 2;
									ordenN2 = -1;
									ordenN3 = -1;
									ordenN4 = -1;
									
									ordenN5 = -1;
									ordenN6 = -1;
									ordenN7 = -1;
									ordenN8 = -1;
								}
								// Si es del nivel 2....
								else if (registroCU[3].getContents()!=null && !registroCU[3].getContents().equalsIgnoreCase(""))
								{
									//log.debug("--NIVEL 2");
									// Comprobamos que nivel 1 exista
									if (ordenN1!=-1)
									{
										// La determinación Base no tiene padre
										determinacion = cargarDEDTOValoresBase (registroCU, 2, idCaracter, indiceFila+1);
										
										Determinacion dt = null;
										if (registroCU[equivale].getContents()!=null && !registroCU[equivale].getContents().equals("")){
											dt= servicioBasicoDeterminaciones.obtenerEquivalenteOrden(getOrdenEquivalente(registroCU[equivale].getContents()), getNombreEquivalente(registroCU[equivale].getContents()),getIdTramiteBaseTrabajo());
											if (dt!=null)
												determinacion.setIdDeterminacionBase(dt.getIden());
											else
												listadoErrores.add("No se ha encontrado el Equivalente de la fila: "+(indiceFila+1));
										}
										determinacion.setIdListaPadre(ordenN1);
										// Creamos la nueva entidad  
										listaDEenExcel.add(determinacion);
										
										//Ponemos a nulos el resto de niveles
										ordenN2 = indiceFila - 2;
										ordenN3 = -1;
										ordenN4 = -1;
										ordenN5 = -1;
										ordenN6 = -1;
										ordenN7 = -1;
										ordenN8 = -1;
									}
									else
										listadoErrores.add("El árbol del excel está mal construido en la fila: "+(indiceFila+1));
									
								}
								// Si es del nivel 3....
								else if (registroCU[5].getContents()!=null && !registroCU[5].getContents().equalsIgnoreCase(""))
								{
									//log.debug("---NIVEL 3");
									// Comprobamos que nivel 1 exista
									if (ordenN2!=-1)
									{
										// La determinación Base no tiene padre
										determinacion = cargarDEDTOValoresBase (registroCU, 4, idCaracter, indiceFila+1);
										
										Determinacion dt = null;
										
										if (registroCU[equivale].getContents()!=null && !registroCU[equivale].getContents().equals("")){
											dt= servicioBasicoDeterminaciones.obtenerEquivalenteOrden(getOrdenEquivalente(registroCU[equivale].getContents()), getNombreEquivalente(registroCU[equivale].getContents()),getIdTramiteBaseTrabajo());
											if (dt!=null)
												determinacion.setIdDeterminacionBase(dt.getIden());
											else
												listadoErrores.add("No se ha encontrado el Equivalente de la fila: "+(indiceFila+1));
										}
										determinacion.setIdListaPadre(ordenN2);
										// Creamos la nueva entidad  
										listaDEenExcel.add(determinacion);
										
										//Ponemos a nulos el resto de niveles
										ordenN3 = indiceFila - 2;
										ordenN4 = -1;
										ordenN5 = -1;
										ordenN6 = -1;
										ordenN7 = -1;
										ordenN8 = -1;
									}
									else
										listadoErrores.add("El árbol del excel está mal construido en la fila: "+(indiceFila+1));
								}
								// Si es del nivel 4....
								else if (registroCU[7].getContents()!=null && !registroCU[7].getContents().equalsIgnoreCase(""))
								{
									//log.debug("----NIVEL 4");
									// Comprobamos que nivel 1 exista
									if (ordenN3!=-1)
									{
										// La determinación Base no tiene padre
										determinacion = cargarDEDTOValoresBase (registroCU, 6, idCaracter, indiceFila+1);
										
										Determinacion dt = null;
										if (registroCU[equivale].getContents()!=null && !registroCU[equivale].getContents().equals("")){
											dt= servicioBasicoDeterminaciones.obtenerEquivalenteOrden(getOrdenEquivalente(registroCU[equivale].getContents()), getNombreEquivalente(registroCU[equivale].getContents()),getIdTramiteBaseTrabajo());
											if (dt!=null)
												determinacion.setIdDeterminacionBase(dt.getIden());
											else
												listadoErrores.add("No se ha encontrado el Equivalente de la fila: "+(indiceFila+1));
										}
										determinacion.setIdListaPadre(ordenN3);
										// Creamos la nueva entidad  
										listaDEenExcel.add(determinacion);
										
										//Ponemos a nulos el resto de niveles
										ordenN4 = indiceFila - 2;
										ordenN5 = -1;
										ordenN6 = -1;
										ordenN7 = -1;
										ordenN8 = -1;
									}
									else
										listadoErrores.add("El árbol del excel está mal construido en la fila: "+(indiceFila+1));
								}
								// Si es del nivel 5....
								else if (registroCU[9].getContents()!=null && !registroCU[9].getContents().equalsIgnoreCase(""))
								{
									//log.debug("-----NIVEL 5");
									// Comprobamos que nivel 1 exista
									if (ordenN4!=-1)
									{
										// La determinación Base no tiene padre
										determinacion = cargarDEDTOValoresBase (registroCU, 8, idCaracter, indiceFila+1);
										
										Determinacion dt = null;
										if (registroCU[equivale].getContents()!=null && !registroCU[equivale].getContents().equals("")){
											dt= servicioBasicoDeterminaciones.obtenerEquivalenteOrden(getOrdenEquivalente(registroCU[equivale].getContents()), getNombreEquivalente(registroCU[equivale].getContents()),getIdTramiteBaseTrabajo());
											if (dt!=null)
												determinacion.setIdDeterminacionBase(dt.getIden());
											else
												listadoErrores.add("No se ha encontrado el Equivalente de la fila: "+(indiceFila+1));
										}
										determinacion.setIdListaPadre(ordenN4);
										// Creamos la nueva entidad  
										listaDEenExcel.add(determinacion);
										
										//Ponemos a nulos el resto de niveles
										ordenN5 = indiceFila - 2;
										ordenN6 = -1;
										ordenN7 = -1;
										ordenN8 = -1;
									}
									else
										listadoErrores.add("El árbol del excel está mal construido en la fila: "+(indiceFila+1));
								}
								// Si es del nivel 6....
								else if (registroCU[11].getContents()!=null && !registroCU[11].getContents().equalsIgnoreCase(""))
								{
									//log.debug("------NIVEL 6");
									// Comprobamos que nivel 1 exista
									if (ordenN5!=-1)
									{
										// La determinación Base no tiene padre
										determinacion = cargarDEDTOValoresBase (registroCU, 10, idCaracter, indiceFila+1);
										
										Determinacion dt = null;
										if (registroCU[equivale].getContents()!=null && !registroCU[equivale].getContents().equals("")){
											dt= servicioBasicoDeterminaciones.obtenerEquivalenteOrden(getOrdenEquivalente(registroCU[equivale].getContents()), getNombreEquivalente(registroCU[equivale].getContents()),getIdTramiteBaseTrabajo());
											if (dt!=null)
												determinacion.setIdDeterminacionBase(dt.getIden());
											else
												listadoErrores.add("No se ha encontrado el Equivalente de la fila: "+(indiceFila+1));
										}
										determinacion.setIdListaPadre(ordenN5);
										// Creamos la nueva entidad  
										listaDEenExcel.add(determinacion);
										
										//Ponemos a nulos el resto de niveles
										ordenN6 = indiceFila - 2;
										ordenN7 = -1;
										ordenN8 = -1;
									}
									else
										listadoErrores.add("El árbol del excel está mal construido en la fila: "+(indiceFila+1));
								}
								// Si es del nivel 7....
								else if (registroCU[13].getContents()!=null && !registroCU[13].getContents().equalsIgnoreCase(""))
								{
									//log.debug("-------NIVEL 7");
									// Comprobamos que nivel anterior exista
									if (ordenN6!=-1)
									{
										// La determinación Base no tiene padre
										determinacion = cargarDEDTOValoresBase (registroCU, 12, idCaracter, indiceFila+1);
										
										Determinacion dt = null;
										if (registroCU[equivale].getContents()!=null && !registroCU[equivale].getContents().equals("")){
											dt= servicioBasicoDeterminaciones.obtenerEquivalenteOrden(getOrdenEquivalente(registroCU[equivale].getContents()), getNombreEquivalente(registroCU[equivale].getContents()),getIdTramiteBaseTrabajo());
											if (dt!=null)
												determinacion.setIdDeterminacionBase(dt.getIden());
											else
												listadoErrores.add("No se ha encontrado el Equivalente de la fila: "+(indiceFila+1));
										}
										determinacion.setIdListaPadre(ordenN6);
										// Creamos la nueva entidad  
										listaDEenExcel.add(determinacion);
										
										//Ponemos a nulos el resto de niveles
										ordenN7 = indiceFila - 2;
										ordenN8 = -1;
									}
									else
										listadoErrores.add("El árbol del excel está mal construido en la fila: "+(indiceFila+1));
								}
								// Si es del nivel 8....
								else if (registroCU[15].getContents()!=null && !registroCU[15].getContents().equalsIgnoreCase(""))
								{
									//log.debug("--------NIVEL 8");
									// Comprobamos que nivel anterior exista
									if (ordenN7!=-1)
									{
										// La determinación Base no tiene padre
										determinacion = cargarDEDTOValoresBase (registroCU, 14, idCaracter, indiceFila+1);
										
										Determinacion dt = null;
										if (registroCU[equivale].getContents()!=null && !registroCU[equivale].getContents().equals("")){
											dt= servicioBasicoDeterminaciones.obtenerEquivalenteOrden(getOrdenEquivalente(registroCU[equivale].getContents()), getNombreEquivalente(registroCU[equivale].getContents()),getIdTramiteBaseTrabajo());
											if (dt!=null)
												determinacion.setIdDeterminacionBase(dt.getIden());
											else
												listadoErrores.add("No se ha encontrado el Equivalente de la fila: "+(indiceFila+1));
										}
										determinacion.setIdListaPadre(ordenN7);
										// Creamos la nueva entidad  
										listaDEenExcel.add(determinacion);
										
										//Ponemos a nulos el resto de niveles
										ordenN8 = indiceFila - 2;
									}
									else
										listadoErrores.add("El árbol del excel está mal construido en la fila: "+(indiceFila+1));
								}
								// Si es del nivel 9....
								else if (registroCU[17].getContents()!=null && !registroCU[17].getContents().equalsIgnoreCase(""))
								{
									//log.debug("--------NIVEL 9");
									// Comprobamos que nivel anterior exista
									if (ordenN8!=-1)
									{
										// La determinación Base no tiene padre
										determinacion = cargarDEDTOValoresBase (registroCU, 16, idCaracter, indiceFila+1);
										
										Determinacion dt = null;
										if (registroCU[equivale].getContents()!=null && !registroCU[equivale].getContents().equals("")){
											dt= servicioBasicoDeterminaciones.obtenerEquivalenteOrden(getOrdenEquivalente(registroCU[equivale].getContents()), getNombreEquivalente(registroCU[equivale].getContents()),getIdTramiteBaseTrabajo());
											if (dt!=null)
												determinacion.setIdDeterminacionBase(dt.getIden());
											else
												listadoErrores.add("No se ha encontrado el Equivalente de la fila: "+(indiceFila+1));
										}
										determinacion.setIdListaPadre(ordenN8);
										// Creamos la nueva entidad  
										listaDEenExcel.add(determinacion);
										
										//Ponemos a nulos el resto de niveles
		
									}
									else
										listadoErrores.add("El árbol del excel está mal construido en la fila: "+(indiceFila+1));
								}
							}
							else
								listadoErrores.add("El carácter no es válido en la fila: "+(indiceFila+1));
							
						}
							
					}
					
					// Si no ha alcanzado 'finentidades' es un error
					if (!fincu)
					{
						listadoErrores.add("No se ha encontrado 'findeterminaciones' en el excel");

					}
				}
				else
				{
					// No es correcto el formato de las columnas
					listadoErrores.add("No es correcto el formato de las columnas en el excel. Por favor verifique el nombre, numero y orden de las columnas del EXCEL. En caso que sea correcto, copie en una hoja aparte las 36 columnas");
				}
				
				
				listadoErrores.addAll(listadoErroresDeterminacion);
				// Si han habido errores 'previos', no se manda al servicio
				if (listadoErrores.size()>0)
				{
					log.error("Hay "+listadoErrores.size()+" Errores Previos. No mandamos a BD");
					facesMessages.addFromResourceBundle("erroresexcelnumero", new Object[]{listadoErrores.size()});
					
					servicioBasicoDeterminaciones.borrarDeterminacionesTramite(getIdTramiteTrabajo());
					listadoErroresImportacion = listadoErrores;
									
					
				}
				else
				{
					facesMessages.addFromResourceBundle("procesofinalizado", null);
					log.debug("Numero de Regimenes que se mandan al Servicio:"+listaDEenExcel.size());
					listadoErrores = comprobarRepetidos(listaDEenExcel);
					if (listadoErrores.size()>0)
					{
						log.error("Hay "+listadoErrores.size()+" Errores Previos. No mandamos a BD");
						facesMessages.addFromResourceBundle("erroresexcelnumero", new Object[]{listadoErrores.size()});
						
						servicioBasicoDeterminaciones.borrarDeterminacionesTramite(getIdTramiteTrabajo());
						listadoErroresImportacion = listadoErrores;
										
						
					}
					else{
					
						List<String> erroresServicio = importadorDECanariasExcel.persistirDEdeExcel(getIdTramiteTrabajo(), listaDEenExcel);
						
						if (erroresServicio.size()>0)
						{
							
							log.error("Hay "+erroresServicio.size()+" Errores detectados por el Servicio. No se persiste en BD");
							facesMessages.addFromResourceBundle("erroresexcelservicio", new Object[]{erroresServicio.size()});
							
							servicioBasicoDeterminaciones.borrarDeterminacionesTramite(getIdTramiteTrabajo());
							
							listadoErroresImportacion = erroresServicio;
											
							
						}
						else
						{
							facesMessages.addFromResourceBundle("excelimportadook", null);
						}
					}
					
				}
				
				
			}
			else
			{
				facesMessages.addFromResourceBundle("errordeterminacionesprevias", null);

			}
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			facesMessages.addFromResourceBundle("errorinesperadoexcel", null);
			facesMessages.addFromResourceBundle("eltramitecontieneentidades", null);
			
			facesMessages.addFromResourceBundle("problemasenlafila", new Object[]{indiceFila+1});
		} 
	}

	public List<String> getListadoErroresImportacion() {
		return listadoErroresImportacion;
	}
	
	// Metodo que contiene el identificador del tramite sobre el que se esta trabajando
	
	private int getIdTramiteTrabajo() {
		return variablesSesionUsuario.getIdTramiteEncargadoTrabajo();
	}
	
	private int getIdTramiteBaseTrabajo() {
		return variablesSesionUsuario.getIdTramiteBaseTrabajo();
	}
	
	private boolean existeCodigoEntidad(String codigoEntidad)
	{
		if (codigoEntidad==null)
			return false;
		else if (codigoEntidad.equalsIgnoreCase("CLA") || codigoEntidad.equalsIgnoreCase("CAT")
				|| codigoEntidad.equalsIgnoreCase("ZON") || codigoEntidad.equalsIgnoreCase("GES")
				|| codigoEntidad.equalsIgnoreCase("AFE") || codigoEntidad.equalsIgnoreCase("PRO")
				|| codigoEntidad.equalsIgnoreCase("RES") || codigoEntidad.equalsIgnoreCase("ACC")
				|| codigoEntidad.equalsIgnoreCase("SIS"))
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
	
	private static int getOrden(String codigo)
	{
		System.out.println("------------" + codigo);
		int inicio = -1;
		if (codigo.lastIndexOf(".")!=-1)
			inicio = codigo.lastIndexOf(".");
		
		codigo = codigo.substring(inicio+1, codigo.length());	
		return Integer.parseInt(codigo);
	}
	
	private static String getEquivalenteFormateado(String codigo)
	{
		int inicio = -1;
		if (codigo.lastIndexOf(".")!=-1)
			inicio = codigo.lastIndexOf(".");
		
		codigo = codigo.substring(inicio+1, codigo.length());	
		//log.debug("getEquivalenteFormateado--" + codigo);
		return codigo;
	}
	
	private static String getNombreEquivalente(String codigo)
	{
		int i = codigo.indexOf("-");
		return codigo.substring(i+1, codigo.length()).trim();
	}
	
	private static String getOrdenEquivalente(String codigo)
	{
		String[] cadena = codigo.split("-");
		return cadena[0].trim();
	}
	
	private DEExcelDTO cargarDEDTOValoresBase (Cell[] registroCU, int campo, int idCaracter, int fila)
	{
		DEExcelDTO determinacion = new DEExcelDTO();
		// Nombre de la entidad
		String nombre = registroCU[campo+1].getContents();
		if(nombre.length()>1000)
			determinacion.setNombreDeterminacion(nombre.substring(0, 999));
		else
			determinacion.setNombreDeterminacion(nombre);
		
		// Cargamos el orden
		determinacion.setOrden(getOrden(registroCU[campo].getContents()));
		
		// Tramite base trabajo
		determinacion.setIdTramiteBaseTrabajo(getIdTramiteBaseTrabajo());
		// Resto de valores
		determinacion.setIdCaracter(idCaracter);
		determinacion.setApartadoArticulo(registroCU[estructura].getContents());
		determinacion.setReferencia(registroCU[referencia].getContents());
		determinacion.setTextoCaracter(registroCU[caracter].getContents());
		
		determinacion.setTextoUnidad(registroCU[unidad].getContents());
		determinacion.setNombreRegulacion1(registroCU[regulacion].getContents());
		determinacion.setTextoRegulacion1(registroCU[regulacion+1].getContents());
		
		if (determinacion.getNombreRegulacion1()!=null && !determinacion.getNombreRegulacion1().equals("")
				&& (determinacion.getTextoRegulacion1()==null || determinacion.getTextoRegulacion1().equals("")))
		{
			listadoErroresDeterminacion.add("El Texto regulación 1 es vacio en la fila: "+fila);
		}
		
		
		determinacion.setNombreRegulacion2(registroCU[regulacion+2].getContents());
		determinacion.setTextoRegulacion2(registroCU[regulacion+3].getContents());
		
		if (determinacion.getNombreRegulacion2()!=null && !determinacion.getNombreRegulacion2().equals("")
				&& (determinacion.getTextoRegulacion2()==null || determinacion.getTextoRegulacion2().equals("")))
		{
			listadoErroresDeterminacion.add("El Texto regulación 2 es vacio en la fila: "+fila);
		}
		
		determinacion.setNombreRegulacion3(registroCU[regulacion+4].getContents());
		determinacion.setTextoRegulacion3(registroCU[regulacion+5].getContents());
		
		if (determinacion.getNombreRegulacion3()!=null && !determinacion.getNombreRegulacion3().equals("")
				&& (determinacion.getTextoRegulacion3()==null || determinacion.getTextoRegulacion3().equals("")))
		{
			listadoErroresDeterminacion.add("El Texto regulación 3 es vacio en la fila: "+fila);
		}
		
		determinacion.setNombreRegulacion4(registroCU[regulacion+6].getContents());
		determinacion.setTextoRegulacion4(registroCU[regulacion+7].getContents());
		
		if (determinacion.getNombreRegulacion4()!=null && !determinacion.getNombreRegulacion4().equals("")
				&& (determinacion.getTextoRegulacion4()==null || determinacion.getTextoRegulacion4().equals("")))
		{
			listadoErroresDeterminacion.add("El Texto regulación 4 es vacio en la fila: "+fila);
		}
		
		determinacion.setNombreRegulacion5(registroCU[regulacion+8].getContents());
		determinacion.setTextoRegulacion5(registroCU[regulacion+9].getContents());
		
		if (determinacion.getNombreRegulacion5()!=null && !determinacion.getNombreRegulacion5().equals("")
				&& (determinacion.getTextoRegulacion5()==null || determinacion.getTextoRegulacion5().equals("")))
		{
			listadoErroresDeterminacion.add("El Texto regulación 5 es vacio en la fila: "+fila);
		}
		
		
		// Comprobamos el grupo aplicaciones
		if (!grupoAplicacionCorrecto(registroCU[caracter].getContents(), registroCU[grupoAplicacion].getContents()))
		{
			listadoErroresDeterminacion.add("El Grupo Aplicación está vacío cuando se espera relleno o viceversa en la fila: "+fila);
		}
		else
			determinacion.setTextoGrupoAplicacion(registroCU[grupoAplicacion].getContents());
		
		return determinacion;
	}
	
	private boolean grupoAplicacionCorrecto(String caracter, String grupo)
	{
		boolean correcto = true;
		//Comprobar si el grupo es de un tipo determinado es obligatorio el grupo
		
		if (quitarTildesLower(caracter).equals("norma general literal") 
				|| quitarTildesLower(caracter).equals("norma general grafica")
				|| quitarTildesLower(caracter).equals("acto de ejecucion")
				|| quitarTildesLower(caracter).equals("uso")
				|| quitarTildesLower(caracter).equals("grupo de actos")
				|| quitarTildesLower(caracter).equals("grupo de usos")
				|| quitarTildesLower(caracter).equals("norma de actos")
				|| quitarTildesLower(caracter).equals("norma de usos")
				|| quitarTildesLower(caracter).equals("norma de adscripciones")
				|| quitarTildesLower(caracter).equals("regimen de actos")
				|| quitarTildesLower(caracter).equals("regimen de usos"))
		{
			if (grupo==null || grupo.equals(""))
				correcto=false;
		}
		else if(grupo!=null & !grupo.equals(""))
			correcto = false;
			
		return correcto;
	}
	
	private String quitarTildesLower(String cadena)
	{
		//log.debug(cadena);
		if (cadena!=null)
		{
			cadena = cadena.replace ('á','a');
			cadena = cadena.replace ('é','e');
			cadena = cadena.replace ('í','i');
			cadena = cadena.replace ('ó','o');
			cadena = cadena.replace ('ú','u');
			
			return cadena.toLowerCase().trim();
		}
		else
			return "";
	}
	
	public boolean comprobarCabecerasDeterminacion(Cell[] row, List <String> listadoErrores)
	{
		boolean correcto= true;
		
		if (!row[0].getContents().toLowerCase().equals("nivel 1"))
		{
			listadoErrores.add("Fallo en la columna de: 'nivel 1'");
			correcto=false;
		}
			
		else if (!row[2].getContents().toLowerCase().equals("nivel 2"))
		{
			listadoErrores.add("Fallo en la columna de: 'nivel 2'");
			correcto=false;
		}
		else if (!row[4].getContents().toLowerCase().equals("nivel 3"))
		{
			listadoErrores.add("Fallo en la columna de: 'nivel 3'");
			correcto=false;
		}
		else if (!row[6].getContents().toLowerCase().equals("nivel 4"))
		{
			listadoErrores.add("Fallo en la columna de: 'nivel 4'");
			correcto=false;
		}
		else if (!row[8].getContents().toLowerCase().equals("nivel 5"))
		{
			listadoErrores.add("Fallo en la columna de: 'nivel 5'");
			correcto=false;
		}
		else if (!row[10].getContents().toLowerCase().equals("nivel 6"))
		{
			listadoErrores.add("Fallo en la columna de: 'nivel 6'");
			correcto=false;
		}
		else if (!row[12].getContents().toLowerCase().equals("nivel 7"))
		{
			listadoErrores.add("Fallo en la columna de: 'nivel 7'");
			correcto=false;
		}
		else if (!row[14].getContents().toLowerCase().equals("nivel 8"))
		{
			listadoErrores.add("Fallo en la columna de: 'nivel 8'");
			correcto=false;
		}
		else if (!row[16].getContents().toLowerCase().equals("nivel 9"))
		{
			listadoErrores.add("Fallo en la columna de: 'nivel 9'");
			correcto=false;
		}
		else if (!quitarTildesLower(row[18].getContents()).equals("caracter"))
		{
			listadoErrores.add("Fallo en la columna de: 'caracter'");
			correcto=false;
		}
		
//		else if (!quitarTildesLower(row[19].getContents()).equalsIgnoreCase("¿Es valor de referencia del padre? (SI / NO)"))
//			correcto=false;
//		else if (!quitarTildesLower(row[20].getContents()).equalsIgnoreCase("Equivale a… (Elegir Desplegable)"))
//			correcto=false;
//		else if (!quitarTildesLower(row[21].getContents()).equalsIgnoreCase("Regula a… (No se cargara en el Importador)"))
//			correcto=false;
//		else if (!quitarTildesLower(row[22].getContents()).equalsIgnoreCase("Unidad"))
//		{
//			listadoErrores.add("Fallo en la columna de: 'Unidad'");
//			correcto=false;
//		}
//		else if (!quitarTildesLower(row[23].getContents()).equalsIgnoreCase("Grupos de Aplicacion (Elegir Desplegable o Separado Comas)"))
//			correcto=false;
//		
//		else if (!quitarTildesLower(row[24].getContents()).equalsIgnoreCase("Estructura o Articulo"))
//			correcto=false;
		else if (!quitarTildesLower(row[25].getContents()).equalsIgnoreCase("Etiqueta"))
		{
			listadoErrores.add("Fallo en la columna de: 'Etiqueta'");
			correcto=false;
		}
//		else if (!quitarTildesLower(row[26].getContents()).equalsIgnoreCase("Regulaciones Especificas 1 (nombre/texto regulacion)"))
//			correcto=false;
//		else if (!quitarTildesLower(row[28].getContents()).equalsIgnoreCase("Regulaciones Especificas 2  (nombre/texto regulacion)"))
//			correcto=false;
//		else if (!quitarTildesLower(row[30].getContents()).equalsIgnoreCase("Regulaciones Especificas 3  (nombre/texto regulacion)"))
//			correcto=false;
//		else if (!quitarTildesLower(row[32].getContents()).equalsIgnoreCase("Regulaciones Especificas4  (nombre/texto regulacion)"))
//			correcto=false;
		else if (!quitarTildesLower(row[34].getContents()).equalsIgnoreCase("Regulaciones Especificas 5  (nombre/texto regulacion)"))
		{
			listadoErrores.add("Fallo en la columna de: 'Regulaciones Especificas 5  (nombre/texto regulacion)'");
			correcto=false;
		}
		
		return correcto;
		
	}
	
	public boolean validarOrden(Sheet sheet, List <String> listadoErrores)
	{
		boolean correcto = true;
		int indiceFila=0;
		boolean finTotal=false;
		for (indiceFila=2; !finTotal & indiceFila<sheet.getRows(); indiceFila++)
		{
			int indice = indiceFila + 1;
			boolean fin = false;
			
			Cell[] registro = sheet.getRow(indiceFila);
			if (registro[0].getContents().equalsIgnoreCase("findeterminaciones"))
			{
				// Se han terminado de leer todas las filas con condiciones urbanisticas para todas las entidades
				finTotal = true;
			}
			else
			{
				String orden_1 = obtenerOrden(registro);
				for (indice=indiceFila+1; !fin & indice<sheet.getRows(); indice++)
				{
					Cell[] regAux = sheet.getRow(indice);
					String orden_2 = obtenerOrden(regAux);
					if (regAux[0].getContents().equalsIgnoreCase("findeterminaciones"))
					{
						// Se han terminado de leer todas las filas con condiciones urbanisticas para todas las entidades
						fin = true;
					}
					else if (orden_1.equals(orden_2))
					{
						listadoErrores.add("El orden se encuentra repetido en las filas: " + (indiceFila+1) + " y " + (indice+1));
						correcto=false;
					}
				}
			}
		}
		
		// Si no ha alcanzado 'finentidades' es un error
		if (!finTotal)
		{
			listadoErrores.clear();
			listadoErrores.add("No se ha encontrado 'findeterminaciones' en el excel");

		}
		
		return correcto;
	}
	
	public String obtenerOrden(Cell[] reg)
	{
		String orden = "";
		
		if (reg[0].getContents()!=null && !reg[0].getContents().equals(""))
			orden = reg[0].getContents();
		else if (reg[2].getContents()!=null && !reg[2].getContents().equals(""))
			orden = reg[2].getContents();
		else if (reg[4].getContents()!=null && !reg[4].getContents().equals(""))
			orden = reg[4].getContents();
		else if (reg[6].getContents()!=null && !reg[6].getContents().equals(""))
			orden = reg[6].getContents();
		else if (reg[8].getContents()!=null && !reg[8].getContents().equals(""))
			orden = reg[8].getContents();
		else if (reg[10].getContents()!=null && !reg[10].getContents().equals(""))
			orden = reg[10].getContents();
		else if (reg[12].getContents()!=null && !reg[12].getContents().equals(""))
			orden = reg[12].getContents();
		else if (reg[14].getContents()!=null && !reg[14].getContents().equals(""))
			orden = reg[14].getContents();
		else if (reg[16].getContents()!=null && !reg[16].getContents().equals(""))
			orden = reg[16].getContents();
		
		return orden;
	}

	public List<String> comprobarRepetidos (List<DEExcelDTO> listadoDto){
		List<String> listaErrores = new ArrayList<String>();
		
		for (int t=0; t<(listadoDto.size()-1); t++) {
			for (int i=t+1; i<(listadoDto.size()-1); i++) {
				if (listadoDto.get(t).getNombreDeterminacion().equalsIgnoreCase(listadoDto.get(i).getNombreDeterminacion())
						&& listadoDto.get(t).getIdCaracter()==listadoDto.get(i).getIdCaracter()
						&& listadoDto.get(t).getIdListaPadre()==listadoDto.get(i).getIdListaPadre()){
				listaErrores.add("La determinación con nombre --" + listadoDto.get(t).getNombreDeterminacion() + "-- está repetida en el mismo nivel");
				}
			}
		}
		
			return listaErrores;
		
	}
	
	@Remove
	public void destroy(){};
	
	public static void main(String[] args) {
			String codigo = "97.5.1.1";
			int inicio = -1;
			if (codigo.lastIndexOf(".")!=-1)
				inicio = codigo.lastIndexOf(".");
			
			codigo = codigo.substring(inicio+1, codigo.length());	
			System.out.println(Integer.parseInt(codigo));
	}
	
	private List<Determinacion> listaAplicar;
	int contador = 0;
	
	public void actualizarGruposPlanBase(){
		
		Determinacion det = em.find(Determinacion.class, 4459);
		
		listaAplicar = new ArrayList<Determinacion>();
		
		Determinacion detAplicar321 = em.find(Determinacion.class, 321);
		Determinacion detAplicar322 = em.find(Determinacion.class, 322);
		Determinacion detAplicar10 = em.find(Determinacion.class, 10);
		Determinacion detAplicar11 = em.find(Determinacion.class, 11);
		
		Determinacion detAplicar9 = em.find(Determinacion.class, 9);
		Determinacion detAplicar5 = em.find(Determinacion.class, 5);
		Determinacion detAplicar4 = em.find(Determinacion.class, 4);
		Determinacion detAplicar14 = em.find(Determinacion.class, 14);
		
		Determinacion detAplicar13 = em.find(Determinacion.class, 13);
		Determinacion detAplicar270 = em.find(Determinacion.class, 270);
		Determinacion detAplicar269 = em.find(Determinacion.class, 269);
		Determinacion detAplicar8 = em.find(Determinacion.class, 8);
		
		Determinacion detAplicar261 = em.find(Determinacion.class, 261);
		Determinacion detAplicar260 = em.find(Determinacion.class, 260);
		Determinacion detAplicar259 = em.find(Determinacion.class, 259);
		Determinacion detAplicar264 = em.find(Determinacion.class, 264);
		
		listaAplicar.add(detAplicar321);
		listaAplicar.add(detAplicar322);
		listaAplicar.add(detAplicar10);
		listaAplicar.add(detAplicar11);
		
		listaAplicar.add(detAplicar9);
		listaAplicar.add(detAplicar5);
		listaAplicar.add(detAplicar4);
		listaAplicar.add(detAplicar14);
		
		listaAplicar.add(detAplicar13);
		listaAplicar.add(detAplicar270);
		listaAplicar.add(detAplicar269);
		listaAplicar.add(detAplicar8);
		
		listaAplicar.add(detAplicar261);
		listaAplicar.add(detAplicar260);
		listaAplicar.add(detAplicar259);
		listaAplicar.add(detAplicar264);
		
		listaAplicar.add(detAplicar264);
		listaAplicar.add(detAplicar264);
		listaAplicar.add(detAplicar264);
		listaAplicar.add(detAplicar264);
		
		actualizarGruposPlanBase(det);
		
		facesMessages.addFromResourceBundle("Proceso_de_Asignación_Correcto", null);
	}
	
	public void actualizarGruposPlanBase(Determinacion det){
		
		contador=contador+1;
		System.out.println("APLICANDO--" + det.getNombre() + "--" + contador);
		
		List<ParIdentificadorTexto> listaHijas = servicioBasicoDeterminaciones.getDeterminacionesHijasDeDet(det.getIden());
		
		Iterator itHijas = listaHijas.iterator();
		
		while (itHijas.hasNext()){
			ParIdentificadorTexto parId = (ParIdentificadorTexto) itHijas.next();
			actualizarGruposPlanBase(em.find(Determinacion.class, parId.getIdBaseDatos()));
		}
		
		Iterator it = listaAplicar.iterator();
		
		while (it.hasNext()){
			Determinacion detAplicar = (Determinacion)it.next();
			
			if (!servicioBasicoDeterminaciones.determinacionAplicadaPorGrupo(det.getIden(), detAplicar.getIden())){
				Determinaciongrupoentidad detGrupoEntidad = new Determinaciongrupoentidad();
				detGrupoEntidad.setDeterminacionByIddeterminacion(det);
				detGrupoEntidad.setDeterminacionByIddeterminaciongrupo(detAplicar);
				
				em.persist(detGrupoEntidad);
			}
		}
		
	}
	
	public void crearScriptTercerNivel()
	{
		String queryNivelCero = "select det.iden from Determinacion det where det.determinacionByIdpadre is null";
		
		List<Integer> listaNivelCero = (List<Integer>)em.createQuery(queryNivelCero).getResultList();
		
		Iterator it = listaNivelCero.iterator();
		
		while (it.hasNext()){
			Integer detIden = (Integer)it.next();
			//log.info("[crearScriptTercerNivel] Determinacion nivel cero:" + detIden);
			sacarTercerNivel(detIden, 1);
			
		}
	}
	
	public void sacarTercerNivel(int detIdenPadre, int nivel)
	{
		String queryNivelCero = "select det.iden from Determinacion det where det.determinacionByIdpadre=" + detIdenPadre;
		
		List<Integer> listaNivel = (List<Integer>)em.createQuery(queryNivelCero).getResultList();
		Iterator it = listaNivel.iterator();
		nivel=nivel+1;
		while (it.hasNext()){
			Integer detIden = (Integer)it.next();
			log.info("[sacarTercerNivel] Determinacion nivel :" + nivel + "--Det:" + detIden);
			
			sacarTercerNivel(detIden, nivel);
			
		}
	}
}


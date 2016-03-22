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
import com.mitc.redes.editorfip.entidades.interfazgrafico.CUExcelDTO;
import com.mitc.redes.editorfip.entidades.interfazgrafico.CondicionUrbanisticaSimplificadaDTO;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Determinacion;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Entidad;
import com.mitc.redes.editorfip.servicios.basicos.fip.determinaciones.ServicioBasicoDeterminaciones;
import com.mitc.redes.editorfip.servicios.basicos.fip.entidades.ServicioBasicoCondicionesUrbanisticas;
import com.mitc.redes.editorfip.servicios.basicos.fip.entidades.ServicioBasicoEntidades;
import com.mitc.redes.editorfip.servicios.sesion.VariablesSesionUsuario;


@Scope(ScopeType.SESSION)
@Stateful
@Name("servicioImportacionCUCanariasBean")
public class ServicioImportacionCUCanariasBean implements ServicioImportacionCUCanarias{
	
	   
	@Logger 
	private Log log;

	private Workbook workbook ;
	private List<SelectItem> sheetListItems;
	private int[] selectedSheet;
	
	private List<String> listadoErroresImportacion;
    
    @In (create = true, required = false)
	VariablesSesionUsuario variablesSesionUsuario;
    
    @In (create = true, required = false)
	ImportadorCUExcel importadorCUExcel;
    
    @In (create = true, required = false)
	ServicioBasicoEntidades servicioBasicoEntidades;
    
    @In (create = true, required = false)
	ServicioBasicoDeterminaciones servicioBasicoDeterminaciones;
    
    @In(create = true, required = false)
	ServicioBasicoCondicionesUrbanisticas servicioBasicoCondicionesUrbanisticas;
    
    @In(create=true)
    FacesMessages facesMessages;
    
    private List<CondicionUrbanisticaSimplificadaDTO> cuSimplificadaList = new ArrayList<CondicionUrbanisticaSimplificadaDTO>();
   
    // Fichero a cargar
    private FileInfo currentFile = null;
    // porcentaje de carga del fichero
    private int fileProgress;
	
	
	public ServicioImportacionCUCanariasBean() {
//		
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
		
		
	public void importarCUExcel ()
	{
		try {
			//Workbook workbook = Workbook.getWorkbook(new File("C:\\Fran_Compartida\\Prueba_Importador_Alcala.xls"));
			
			
			Sheet sheet = workbook.getSheet(selectedSheet[0]); 
			Determinacion grupoAplicacion = null;
			List <CUExcelDTO> listaCUenExcel = new ArrayList<CUExcelDTO>();
			List <String> listadoErrores = new ArrayList<String>();
			
			int indiceFila=0;
			boolean fincu = false;
			boolean anteriorFueFinEntidad = false;
			
			String anteriorCodigoEntidad = "";
			String anteriorNombreEntidad = "";
			
			String anteriorApartadoDeterminacion = "";
			String anteriorNombreDeterminacion = "";
			
			log.debug("Num Columnas="+sheet.getColumns());
				
			// Si hay 12 columnas, es correcto el formato y se puede seguir
			if (sheet.getColumns()==12)
			{
				// Empiezo a coger registros desde la fila 3 hasta que llegue al final de la hoja
				for (indiceFila=2; !fincu & indiceFila<sheet.getRows(); indiceFila++)
				{
					
					Cell[] registroCU = sheet.getRow(indiceFila);
					
					
					int indiceColumna = 0;
					if (registroCU[indiceColumna].getContents().equalsIgnoreCase("fincondicionurbanistica"))
					{
						// Se han terminado de leer todas las filas con condiciones urbanisticas para todas las entidades
						fincu = true;
					}
					else
					{
						if (registroCU[indiceColumna].getContents().equalsIgnoreCase("finentidad"))
						{
							// Se han terminado de leer todas condiciones urbanisticas para la entidad, aviso porque hay que coger el nombre en la siguiente
							anteriorFueFinEntidad = true;
							
						}
						else
						{
							
							// Si el anterior fue fin de entidad, en este tengo que coger el codigo y nombre de entidad para dejarlo en memoria
							// para los siguientes y coger el grupo de aplicacion
							if (anteriorFueFinEntidad || indiceFila==2)
							{
								// El codigo cero en las columnas se corresponde con codigoEntidad
								String codigoEntidad = registroCU[0].getContents();
								
								if (codigoEntidad=="" || codigoEntidad==null)
								{
									listadoErrores.add("No se ha encontrado codigo entidad para la fila: "+(indiceFila+1));
								}
								
								// El codigo uno en las columnas se corresponde con nombreEntidad
								
								String nombreEntidad = registroCU[1].getContents();
								
								if (nombreEntidad=="" || nombreEntidad==null)
								{
									listadoErrores.add("No se ha encontrado nombre entidad para la fila: "+(indiceFila+1));
								}
								
								grupoAplicacion = null;
								// Comprobamos que la entidad existe y está aplicada
								if (!nombreEntidad.equals("") && !codigoEntidad.equals("")){
									Entidad  entidad = servicioBasicoEntidades.obtenerEntidadPorTramiteYClave(variablesSesionUsuario.getIdTramiteTrabajoActual(), codigoEntidad);
									
									if (entidad!=null){
										boolean aplicadaCorrectamente = false;
										cuSimplificadaList = servicioBasicoCondicionesUrbanisticas.listadoTodasCUSimplificadaDeEntidad(entidad.getIden());
										if (cuSimplificadaList!=null){
											Iterator it = cuSimplificadaList.iterator();
											while (it.hasNext()){
												CondicionUrbanisticaSimplificadaDTO cu = (CondicionUrbanisticaSimplificadaDTO)it.next();
												Determinacion det = servicioBasicoDeterminaciones.buscarDeterminacion(cu.getIdDeterminacionValorReferencia());
												if (det.getEtiqueta().equalsIgnoreCase("RUS") || 
														det.getEtiqueta().equalsIgnoreCase("RUS_SUB") ||
														det.getEtiqueta().equalsIgnoreCase("RUS_ENG") ||
														det.getEtiqueta().equalsIgnoreCase("RG_ENP")){
													
													aplicadaCorrectamente = true;
													grupoAplicacion = det;
												}
											}
										}
										if (!aplicadaCorrectamente)
											listadoErrores.add("La entidad seleccionada no está aplicada correctamente por alguna CU: "+(indiceFila+1));
									}
									else
										listadoErrores.add("La entidad seleccionada no existe para el trámite seleccinado: "+(indiceFila+1));
								}
								
								// El codigo once en las columnas se corresponde con nombreGrupoAplicacion
//								String nombreGrupoAplicacion = registroCU[11].getContents();
//								
//								if (nombreGrupoAplicacion=="" || nombreGrupoAplicacion==null)
//								{
//									listadoErrores.add("No se ha encontrado nombre Grupo Aplicacion para la fila: "+(indiceFila+1));
//								}
								
								// Como el anterior fue fin de entidad, guardo estos nombres para los siguientes
								anteriorCodigoEntidad = codigoEntidad;
								anteriorNombreEntidad = nombreEntidad;
								
								// Creo un nuevo objeto CUExcelDTO (que es Grupo Aplicacion)
								CUExcelDTO cuGA = new CUExcelDTO(codigoEntidad, nombreEntidad, getIdTramiteBaseTrabajo()+"", "", true);
								cuGA.setDetGrupoAplicacion(grupoAplicacion);
								listaCUenExcel.add(cuGA);
								
								//Pongo a false la variable anteriorFueFinEntidad para que no entre mas aqui
								anteriorFueFinEntidad = false;
							}
							// CASO: Se corresponde con lo nuevo de Canarias
							else if(registroCU[2].getContents()!=null && !registroCU[2].getContents().equals(""))
							{
								// Comprobamos el uso/acto (Apartado-Nombre)
								String nombreUso = getNombreEquivalente(registroCU[2].getContents());
								String ordenUso = getOrdenEquivalente(registroCU[2].getContents());
								if (nombreUso.equals("") || ordenUso.equals(""))
									listadoErrores.add("El Uso/Acto no es correcto para la fila: "+(indiceFila+1));
								
								// Comprobamos el régimen. Tenemos que comprobar que el id de la determinación del plan base y con nombre que viene del excel está
								// en nuestro plan en el idDeterminacionBase. Sino está nos quedamos con el id anterior y si está nos quedamos con el recuperado
								String regimen = registroCU[4].getContents().trim();
								Determinacion detRegimenBase = servicioBasicoDeterminaciones.obtenerEquivalente(regimen, variablesSesionUsuario.getIdTramiteBaseTrabajo());
								Determinacion detRegimenActual = servicioBasicoDeterminaciones.buscarDeterminacionBase(detRegimenBase.getIden());
								int idDetRegimen;
								
								if (detRegimenActual!=null)
									idDetRegimen = detRegimenActual.getIden();
								else
									idDetRegimen = detRegimenBase.getIden();
								
								
								// Cogemos el valor de referencia del plan resultante de la operacion anterior y desde ahi cogemos el equivalente del plan actual
								String valorRef = registroCU[5].getContents().trim();
								Determinacion valorReferenciaBase = servicioBasicoDeterminaciones.getValorReferenciaDeDeterminacion(idDetRegimen, valorRef);
								Determinacion valorReferenciaActual = servicioBasicoDeterminaciones.buscarDeterminacionBase(valorReferenciaBase.getIden());
								
								int idReferencia;
								if (valorReferenciaActual!=null){
									idReferencia = valorReferenciaActual.getIden();
									System.out.println("VALOR REF ACTUAL --> " + idDetRegimen + "--" + valorReferenciaActual.getIden());
								}
								else{
									idReferencia = valorReferenciaBase.getIden();
									System.out.println("VALOR REF BASE --> " + idDetRegimen + "--" + valorReferenciaBase.getIden());
								}
								
								
								
							}
							// CASO: PROCESANDO LO ANTIGUO
							else
							{
								String codigoEntidad = anteriorCodigoEntidad;
								String nombreEntidad = anteriorNombreEntidad;
								
								// El codigo dos en las columnas se corresponde con apartadoDeterminacion
								String apartadoDeterminacion = registroCU[8].getContents();
								
								if (apartadoDeterminacion=="" || apartadoDeterminacion==null)
								{
									if (anteriorApartadoDeterminacion!="")
									{
										apartadoDeterminacion = anteriorApartadoDeterminacion;
									}
									else
									{
										listadoErrores.add("No se ha encontrado apartado Determinacion para la fila: "+(indiceFila+1));
									}
									
								}
								else
								{
									anteriorApartadoDeterminacion = apartadoDeterminacion;
								}
								
								// El codigo tres en las columnas se corresponde con  NombreDeterminacion
								String nombreDeterminacion = registroCU[9].getContents();
								log.info("[] nombreDeterminacion="+nombreDeterminacion);
								
								if (nombreDeterminacion=="" || nombreDeterminacion==null)
								{
									if (anteriorApartadoDeterminacion!="")
									{
										nombreDeterminacion = anteriorNombreDeterminacion;
									}
									else
									{
										listadoErrores.add("No se ha encontrado nombre Determinacion para la fila: "+(indiceFila+1));
									}
									
								}
								else
								{
									anteriorNombreDeterminacion = nombreDeterminacion;
								}
								
								// El codigo cuatro en las columnas se corresponde con apartadoDeterminacionRegimen
								//String apartadoDeterminacionRegimen = registroCU[4].getContents();
								
								// El codigo cinco en las columnas se corresponde con nombreValor
								//String nombreDeterminacionRegimen = registroCU[5].getContents();
								
								// El codigo seis en las columnas se corresponde con apartadoGrupoAplicacion
								String apartadoValorReferencia = registroCU[10].getContents();
								
								// El codigo siete en las columnas se corresponde con nombreValor
								String nombreValor = registroCU[11].getContents();
								
								// El codigo ocho en las columnas se corresponde con tituloRegEsp
								String tituloRegEsp = registroCU[6].getContents();
								
								// El codigo nueve en las columnas se corresponde con textoRegEsp
								String textoRegEsp = registroCU[7].getContents();
								
								if (apartadoValorReferencia=="" & nombreValor=="" & tituloRegEsp=="" & textoRegEsp=="")
								{
									listadoErrores.add("Ningun campo apartadoDeterminacionRegimen, nombreDeterminacionRegimen, apartadoValorReferencia, nombreValor, tituloRegEsp, textoRegEsp esta relleno para la fila: "+(indiceFila+1));
								}
								
								// Creo un nuevo objeto CUExcelDTO 
								CUExcelDTO cuGA = new CUExcelDTO(codigoEntidad, nombreEntidad, apartadoDeterminacion, nombreDeterminacion, "", "", apartadoValorReferencia, nombreValor, tituloRegEsp, textoRegEsp);
								cuGA.setDetGrupoAplicacion(grupoAplicacion);
								listaCUenExcel.add(cuGA);
								
							}
						}
						
						
					}
				}
				
				// Si no ha alcanzado 'fincondicionurbanistica' es un error
				if (!fincu)
				{
					listadoErrores.add("No se ha encontrado 'fincondicionurbanistica' en el excel");

				}
			}
			else
			{
				// No es correcto el formato de las columnas
				listadoErrores.add("No es correcto el formato de las columnas en el excel o tiene mas de 1000 filas. Si tiene mas de 1000 filas trocee las condiciones urbanisticas en varias hojas.");
			}
			
			
			
			// Si han habido errores 'previos', no se manda al servicio
			if (listadoErrores.size()>0)
			{
				log.error("Hay "+listadoErrores.size()+" Errores Previos. No mandamos a BD");
				facesMessages.addFromResourceBundle("erroresexcelnumero", new Object[]{listadoErrores.size()});
				
				listadoErroresImportacion = listadoErrores;
								
				
			}
			else
			{
				facesMessages.addFromResourceBundle("procesofinalizado", null);
				log.debug("Numero de Regimenes que se mandan al Servicio:"+listaCUenExcel.size());
				
				
				List<String> erroresServicio = importadorCUExcel.persistirCUdeExcelRus(getIdTramiteTrabajo(), listaCUenExcel);
				
				if (erroresServicio.size()>0)
				{
					log.error("Hay "+erroresServicio.size()+" Errores detectados por el Servicio. No se persiste en BD");
					facesMessages.addFromResourceBundle("erroresexcelservicio", new Object[]{erroresServicio.size()});
					
					listadoErroresImportacion = erroresServicio;
									
					
				}
				else
				{
					facesMessages.addFromResourceBundle("excelimportadook", null);
				}
				
			}
			
			
			
			
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			facesMessages.addFromResourceBundle("errorinesperadoexcel", null);

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


	@Remove
	public void destroy(){};
	
	private static String getNombreEquivalente(String codigo)
	{
		String[] cadena = codigo.split("-");
		return cadena[1].trim();
	}
	
	private static String getOrdenEquivalente(String codigo)
	{
		String[] cadena = codigo.split("-");
		return cadena[0].trim();
	}
}


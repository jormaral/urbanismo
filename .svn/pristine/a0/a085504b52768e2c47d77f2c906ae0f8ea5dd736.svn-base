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

package com.mitc.redes.editorfip.servicios.gestionfip.importadores;

import java.io.IOException;
import java.util.ArrayList;
import java.util.EventObject;
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
import com.mitc.redes.editorfip.servicios.sesion.VariablesSesionUsuario;


@Scope(ScopeType.SESSION)
@Stateful
@Name("servicioImportacionCUBean")
public class ServicioImportacionCUBean implements ServicioImportacionCU{
	
	   
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
    
    @In(create=true)
    FacesMessages facesMessages;
   
    // Fichero a cargar
    private FileInfo currentFile = null;
    // porcentaje de carga del fichero
    private int fileProgress;
	
	
	public ServicioImportacionCUBean() {
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
			
			/*
			// Compruebo el numero de filas que hay
			int numeroFilasMaximo = 0;
			boolean superaNumeroMaximoFilas = false;
			
			for (numeroFilasMaximo = 0;!fincu & indiceFila<sheet.getRows(); numeroFilasMaximo++)
			{
				Cell[] registroCU = sheet.getRow(numeroFilasMaximo);
				
				if (registroCU[0].getContents().equalsIgnoreCase("fincondicionurbanistica"))
				{
					// Se han terminado de leer todas las filas con condiciones urbanisticas para todas las entidades
					fincu = true;
				}
			}
			// Lo establezco el maximo a 100000 filas
			if (numeroFilasMaximo > 100000)
			{
				superaNumeroMaximoFilas = true;
			}
			
			// Vuelvo a poner el valor a false
			fincu = false;
			
			// Si hay 12 columnas, es correcto el formato y se puede seguir
			if (sheet.getColumns()==12 & !superaNumeroMaximoFilas)
			
			*/
				
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
								
								/*
								 * FGA: Ya no es necesario 080211 -> Tira del Plan Base
								// El codigo diez en las columnas se corresponde con apartadoGrupoAplicacion
								String apartadoGrupoAplicacion = registroCU[10].getContents();
								
								if (apartadoGrupoAplicacion=="" || apartadoGrupoAplicacion==null)
								{
									listadoErrores.add("No se ha encontrado apartado Grupo Aplicacion para la fila: "+(indiceFila+1));
								}
								*/
								
								// El codigo once en las columnas se corresponde con nombreGrupoAplicacion
								String nombreGrupoAplicacion = registroCU[11].getContents();
								
								if (nombreGrupoAplicacion=="" || nombreGrupoAplicacion==null)
								{
									listadoErrores.add("No se ha encontrado nombre Grupo Aplicacion para la fila: "+(indiceFila+1));
								}
								
								// Como el anterior fue fin de entidad, guardo estos nombres para los siguientes
								anteriorCodigoEntidad = codigoEntidad;
								anteriorNombreEntidad = nombreEntidad;
								
								// Creo un nuevo objeto CUExcelDTO (que es Grupo Aplicacion)
								CUExcelDTO cuGA = new CUExcelDTO(codigoEntidad, nombreEntidad, getIdTramiteBaseTrabajo()+"", nombreGrupoAplicacion, true);
								listaCUenExcel.add(cuGA);
								
								//Pongo a false la variable anteriorFueFinEntidad para que no entre mas aqui
								anteriorFueFinEntidad = false;
							}
							else
							{
								String codigoEntidad = anteriorCodigoEntidad;
								String nombreEntidad = anteriorNombreEntidad;
								
								// El codigo dos en las columnas se corresponde con apartadoDeterminacion
								String apartadoDeterminacion = registroCU[2].getContents();
								
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
								String nombreDeterminacion = registroCU[3].getContents();
								log.info("[] nombreDeterminacion="+nombreDeterminacion);
								log.info("[] registroCU[3].getContents()="+registroCU[3].getContents());
								log.info("[] registroCU[3].getContents().getBytes()="+registroCU[3].getContents().getBytes());
								
								
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
								String apartadoDeterminacionRegimen = registroCU[4].getContents();
								
								// El codigo cinco en las columnas se corresponde con nombreValor
								String nombreDeterminacionRegimen = registroCU[5].getContents();
								
								// El codigo seis en las columnas se corresponde con apartadoGrupoAplicacion
								String apartadoValorReferencia = registroCU[6].getContents();
								
								// El codigo siete en las columnas se corresponde con nombreValor
								String nombreValor = registroCU[7].getContents();
								
								// El codigo ocho en las columnas se corresponde con tituloRegEsp
								String tituloRegEsp = registroCU[8].getContents();
								
								// El codigo nueve en las columnas se corresponde con textoRegEsp
								String textoRegEsp = registroCU[9].getContents();
								
								if (apartadoDeterminacionRegimen=="" & nombreDeterminacionRegimen=="" & apartadoValorReferencia=="" & nombreValor=="" & tituloRegEsp=="" & textoRegEsp=="")
								{
									listadoErrores.add("Ningun campo apartadoDeterminacionRegimen, nombreDeterminacionRegimen, apartadoValorReferencia, nombreValor, tituloRegEsp, textoRegEsp esta relleno para la fila: "+(indiceFila+1));
								}
								
								// Creo un nuevo objeto CUExcelDTO 
								CUExcelDTO cuGA = new CUExcelDTO(codigoEntidad, nombreEntidad, apartadoDeterminacion, nombreDeterminacion, apartadoDeterminacionRegimen, nombreDeterminacionRegimen, apartadoValorReferencia, nombreValor, tituloRegEsp, textoRegEsp);
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
				
				
				List<String> erroresServicio = importadorCUExcel.persistirCUdeExcel(getIdTramiteTrabajo(), listaCUenExcel);
				
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
	
	
}


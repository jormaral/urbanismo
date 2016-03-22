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
import com.mitc.redes.editorfip.entidades.interfazgrafico.CUAdaptadaSipuDTO;
import com.mitc.redes.editorfip.entidades.interfazgrafico.CUExcelDTO;
import com.mitc.redes.editorfip.entidades.interfazgrafico.CondicionUrbanisticaSimplificadaDTO;
import com.mitc.redes.editorfip.entidades.rpm.gestionfip.CUAdaptadaSipu;
import com.mitc.redes.editorfip.entidades.rpm.gestionfip.RegimenesEspecificosSimplificadosDeterminacion;
import com.mitc.redes.editorfip.entidades.rpm.gestionfip.RegimenesEspecificosSimplificadosUsoActos;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Determinacion;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Entidad;
import com.mitc.redes.editorfip.servicios.basicos.fip.determinaciones.ServicioBasicoDeterminaciones;
import com.mitc.redes.editorfip.servicios.basicos.fip.entidades.ServicioBasicoCondicionesUrbanisticas;
import com.mitc.redes.editorfip.servicios.basicos.fip.entidades.ServicioBasicoEntidades;
import com.mitc.redes.editorfip.servicios.sesion.VariablesSesionUsuario;


@Scope(ScopeType.SESSION)
@Stateful
@Name("servicioImportacionCURusCanariasBean")
public class ServicioImportacionCURusCanariasBean implements ServicioImportacionCURusCanarias{
	
	   
	@Logger 
	private Log log;
	
	@PersistenceContext
    private EntityManager em;

	private Workbook workbook ;
	private List<SelectItem> sheetListItems;
	private int[] selectedSheet;
	
	private List<String> listadoErroresImportacion;
    
    @In (create = true, required = false)
	VariablesSesionUsuario variablesSesionUsuario;
    
    @In (create = true, required = false)
	ImportadorCUCanariasExcel importadorCUCanariasExcel;
    
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
	
    boolean mostrarBotonImportar = false;
	
	public ServicioImportacionCURusCanariasBean() {
		mostrarBotonImportar = false;
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
			Entidad entidadAplicar = null;
			List <CUAdaptadaSipu> listaCUenExcel = new ArrayList<CUAdaptadaSipu>();
			List <String> listadoErrores = new ArrayList<String>();
			
			// CU General para cada caso
			CUAdaptadaSipu cuAdaptadaCaso = new CUAdaptadaSipu();
			CUAdaptadaSipu cuAdaptadaDetermacion = new CUAdaptadaSipu();
			
			int indiceFila=0;
			boolean fincu = false;
			boolean finentidad = false;
			boolean anteriorFueFinEntidad = false;
			boolean insertandoAntiguos = false;
			
			mostrarBotonImportar = false;
			
			String anteriorCodigoEntidad = "";
			String anteriorNombreEntidad = "";
			
			String anteriorApartadoDeterminacion = "";
			String anteriorNombreDeterminacion = "";
			
			System.out.println("Num Columnas="+sheet.getColumns());
				
			// Si hay 12 columnas, es correcto el formato y se puede seguir
			if (sheet.getColumns()==16)
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
							insertandoAntiguos = false;
							log.info("FIN-ENTIDAD");
							
						}
						// En este caso preparamos para que se vayan insertando despues de la forma antigua
						else if (registroCU[indiceColumna].getContents().equalsIgnoreCase("finuso")){
							insertandoAntiguos = true;
						}
						// Lo inserta
						else if (insertandoAntiguos){
							String nombreDet = registroCU[11].getContents();
							String ordenDet = registroCU[10].getContents();
							int idDeterminacion = -1;
							Determinacion determinacion = null;
							
							if (nombreDet.equals("") || ordenDet.equals(""))
								listadoErrores.add("Nombre u orden vacios. La Determinacion no es correcta para la fila: "+(indiceFila+1));
							else{
								idDeterminacion = servicioBasicoDeterminaciones.buscarDeterminacionPorTramiteYOrden(variablesSesionUsuario.getIdTramiteTrabajoActual(), ordenDet);
								if(idDeterminacion==0)
									listadoErrores.add("No existe la determinacion. La Determinacion no es correcta para la fila: "+(indiceFila+1));
								else{
									determinacion = servicioBasicoDeterminaciones.buscarDeterminacion(idDeterminacion);
									if (!determinacion.getNombre().equals(nombreDet))
										listadoErrores.add("El Valor del Uso/Acto no coincide con el código indicado para la fila: "+(indiceFila+1));
								}
							}
							
							cuAdaptadaDetermacion = new CUAdaptadaSipu();
							cuAdaptadaDetermacion.setCuAdptadaAntigual(true);
							
							cuAdaptadaDetermacion.setIdTramite(variablesSesionUsuario.getIdTramiteTrabajoActual());
							cuAdaptadaDetermacion.setIdFilaExcel(indiceFila+1);
							
							if (entidadAplicar!=null){
								cuAdaptadaDetermacion.setIdEntidad(entidadAplicar.getIden());
								cuAdaptadaDetermacion.setNombreEntidad(entidadAplicar.getNombre());
								cuAdaptadaDetermacion.setClaveEntidad(entidadAplicar.getClave());
							}
							
							if (determinacion!=null)
								cuAdaptadaDetermacion.setIdDeterminacion(determinacion.getIden());
							cuAdaptadaDetermacion.setApartadoDeterminacion(ordenDet);
							cuAdaptadaDetermacion.setNombreDeterminacion(nombreDet);
							
							// Si registroCU[12] y registroCU[13] están rellenos es que tenemos un valor de referencia
							String apartadoValor = registroCU[12].getContents();
							String nombreValor = registroCU[13].getContents();
							int idDeterminacionValor;
							Determinacion determinacionValor = null;
							if (!apartadoValor.equals("") && !nombreValor.equals("")){
								idDeterminacionValor = servicioBasicoDeterminaciones.buscarDeterminacionPorTramiteYOrden(variablesSesionUsuario.getIdTramiteTrabajoActual(), apartadoValor);
								if(idDeterminacionValor==0)
									listadoErrores.add("Valor no encontrado. La determinacion no es correcta para la fila: "+(indiceFila+1));
								else{
									determinacionValor = servicioBasicoDeterminaciones.buscarDeterminacion(idDeterminacionValor);
									if (!determinacionValor.getNombre().equals(nombreValor))
										listadoErrores.add("El Valor del Uso/Acto no coincide con el código indicado para la fila: "+(indiceFila+1));
								}
								
								// Añadimos los valores de la determinacion
								cuAdaptadaDetermacion.setIdDetValorReferencia(idDeterminacionValor);
								cuAdaptadaDetermacion.setApartadoValorReferencia(apartadoValor);
								cuAdaptadaDetermacion.setNombreValorReferencia(nombreValor);
							}
							else if (!nombreValor.equals(""))
								cuAdaptadaDetermacion.setValor(nombreValor);
							
							// Añadimos el primer régimen específico si tiene
							if (!registroCU[14].getContents().equals("") || !registroCU[15].getContents().equals("")){
								RegimenesEspecificosSimplificadosDeterminacion regEsp = new RegimenesEspecificosSimplificadosDeterminacion();
								if (registroCU[14].getContents()==null || registroCU[14].getContents().equals(""))
									regEsp.setNombreRegimenEspecifico("Observaciones");
								else
									regEsp.setNombreRegimenEspecifico(registroCU[14].getContents());
								
								regEsp.setTextoRegimenEspecifico(registroCU[15].getContents());
								
								cuAdaptadaDetermacion.getRegEspecificoDeterminacion().add(regEsp);
							}
							// Añadimos el elemento a insertar
							listaCUenExcel.add(cuAdaptadaDetermacion);
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
								entidadAplicar = null;
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
												if (det!=null && det.getEtiqueta()!=null){
//													if (det.getEtiqueta().equalsIgnoreCase("RUS") || 
//															det.getEtiqueta().equalsIgnoreCase("RUS_SUB") ||
//															det.getEtiqueta().equalsIgnoreCase("RUS_ENG") ||
//															det.getEtiqueta().equalsIgnoreCase("RG_ENP") ||
//															det.getEtiqueta().equalsIgnoreCase("ZON_ENP") ||
//															det.getEtiqueta().equalsIgnoreCase("ZON_EDIF") ||
//															det.getEtiqueta().equalsIgnoreCase("GES_UA") ||
//															det.getEtiqueta().equalsIgnoreCase("DES")
//															){
														
														
														aplicadaCorrectamente = true;
														entidadAplicar = entidad;
														grupoAplicacion = det;
														
														// Reseteamos los valores
														cuAdaptadaCaso = new CUAdaptadaSipu();
														cuAdaptadaCaso.setIdEntidad(entidadAplicar.getIden());
														cuAdaptadaCaso.setNombreEntidad(entidadAplicar.getNombre());
														cuAdaptadaCaso.setClaveEntidad(entidadAplicar.getClave());
														cuAdaptadaCaso.setIdTramite(variablesSesionUsuario.getIdTramiteTrabajoActual());
//													}
													
												}
											}
										}
										if (!aplicadaCorrectamente)
											listadoErrores.add("La entidad seleccionada no está aplicada correctamente por alguna CU: "+(indiceFila+1));
									}
									else
										listadoErrores.add("La entidad seleccionada no existe para el trámite seleccinado: "+(indiceFila+1));
								}
								
								// Como el anterior fue fin de entidad, guardo estos nombres para los siguientes
								anteriorCodigoEntidad = codigoEntidad;
								anteriorNombreEntidad = nombreEntidad;
								
								//Pongo a false la variable anteriorFueFinEntidad para que no entre mas aqui
								anteriorFueFinEntidad = false;
							}
							// CASO: Es un uso base para una entidad
							else if(registroCU[2].getContents()!=null && !registroCU[2].getContents().equals(""))
							{
								// Reiniciamos la cuAdaptadaCaso
								cuAdaptadaCaso = new CUAdaptadaSipu();
								
								// Asignamos el id de tramite
								cuAdaptadaCaso.setIdTramite(variablesSesionUsuario.getIdTramiteTrabajoActual());
								cuAdaptadaCaso.setIdFilaExcel(indiceFila+1);
								
								// USO/ACTO
								String nombreUso = registroCU[3].getContents();
								String ordenUso = registroCU[2].getContents();
								String caracterUso = "";
								
								Determinacion detUsoActo = null;
								int idUsoActo = -1;
								if (nombreUso.equals("") || ordenUso.equals(""))
									listadoErrores.add("El Uso/Acto no es correcto para la fila: "+(indiceFila+1));
								else{
									idUsoActo = servicioBasicoDeterminaciones.buscarDeterminacionPorTramiteYOrden(variablesSesionUsuario.getIdTramiteTrabajoActual(), ordenUso);
									if(idUsoActo==0)
										listadoErrores.add("El Uso/Acto no es correcto para la fila: "+(indiceFila+1));
									else{
										detUsoActo = servicioBasicoDeterminaciones.buscarDeterminacion(idUsoActo);
										caracterUso = servicioBasicoDeterminaciones.caracterString(detUsoActo.getIdcaracter());
										if (!detUsoActo.getNombre().equals(nombreUso))
											listadoErrores.add("El nombre del Uso/Acto no coincide con el código indicado para la fila: "+(indiceFila+1));
									}
								}
								
								// Determinacion Uso/Acto
								String nombreDetUso = registroCU[5].getContents();
								String ordenDetUso = registroCU[4].getContents();
								String caracterDetUsoActo = "";
								
								int idDetUsoActo = -1;
								Determinacion detDeterminacionUsoActo = null;
								if (nombreDetUso.equals("") || ordenDetUso.equals(""))
									listadoErrores.add("La Determinacion Uso/Acto no es correcto para la fila: "+(indiceFila+1));
								else{
									idDetUsoActo = servicioBasicoDeterminaciones.buscarDeterminacionPorTramiteYOrden(variablesSesionUsuario.getIdTramiteTrabajoActual(), ordenDetUso);
									if(idDetUsoActo==0)
										listadoErrores.add("La Determinación Uso/Acto no es correcto para la fila: "+(indiceFila+1));
									else{
										detDeterminacionUsoActo = servicioBasicoDeterminaciones.buscarDeterminacion(idDetUsoActo);
										
										if (!detDeterminacionUsoActo.getNombre().equals(nombreDetUso))
											listadoErrores.add("El nombre de la Determinacion Uso/Acto no coincide con el código indicado para la fila: "+(indiceFila+1));
										
										caracterDetUsoActo = servicioBasicoDeterminaciones.caracterString(detDeterminacionUsoActo.getIdcaracter());
										if (caracterUso.toLowerCase().contains("uso") && !(caracterDetUsoActo.toLowerCase().contains("gimen de uso")))
											listadoErrores.add("La Determinacion Uso/Acto debe ser USO y no lo es: "+(indiceFila+1));
										
										if (caracterUso.toLowerCase().contains("acto") && !(caracterDetUsoActo.toLowerCase().contains("gimen de acto")))
											listadoErrores.add("La Determinacion Uso/Acto debe ser ACTO y no lo es: "+(indiceFila+1));
										
									}
								}
								
								// Valores Usos/Actos
								String nombreValUso = registroCU[7].getContents();
								String ordenValUso = registroCU[6].getContents();
								
								// Para comprobar si tiene regimen específico de uso/acto
								String regUsoActo = registroCU[9].getContents();
								
								int idValUsoActo = -1;
								Determinacion detValorUsoActo = null;
								if ((nombreValUso.equals("") || ordenValUso.equals("")) && regUsoActo.equals(""))
									listadoErrores.add("La Determinacion Uso/Acto no es correcto para la fila: "+(indiceFila+1));
								//else if (regUsoActo.equals("")){
								else{
									idValUsoActo = servicioBasicoDeterminaciones.buscarDeterminacionPorTramiteYOrden(variablesSesionUsuario.getIdTramiteTrabajoActual(), ordenValUso);
									if(idValUsoActo==0)
										listadoErrores.add("El Valor del Uso/Acto no es correcto para la fila: "+(indiceFila+1));
									else{
										detValorUsoActo = servicioBasicoDeterminaciones.buscarDeterminacion(idValUsoActo);
										if (!detValorUsoActo.getNombre().equals(nombreValUso))
											listadoErrores.add("El Valor del Uso/Acto no coincide con el código indicado para la fila: "+(indiceFila+1));
										
										if (detDeterminacionUsoActo!=null && !servicioBasicoDeterminaciones.esValorReferenciaDeDeterminacion(detDeterminacionUsoActo.getIden(), detValorUsoActo.getIden()))
											listadoErrores.add("El Valor del Uso/Acto no es Valor de Referencia para la Determinacion Uso/Acto para la fila: "+(indiceFila+1));
									}
								}
								
								
								// Cargamos el elemento base
								if (entidadAplicar!=null){
									cuAdaptadaCaso.setIdEntidad(entidadAplicar.getIden());
									cuAdaptadaCaso.setNombreEntidad(entidadAplicar.getNombre());
									cuAdaptadaCaso.setClaveEntidad(entidadAplicar.getClave());
								}
								
								cuAdaptadaCaso.setIdUsoActo(idUsoActo);
								cuAdaptadaCaso.setNombreUsoActo(nombreUso);
								cuAdaptadaCaso.setApartadoUsoActo(ordenUso);
									
								cuAdaptadaCaso.setIdDetUsoActo(idDetUsoActo);
								cuAdaptadaCaso.setNombreDetUsoActo(nombreDetUso);
								cuAdaptadaCaso.setApartadoDetUsoActo(ordenDetUso);
								
								if (detDeterminacionUsoActo!=null && detDeterminacionUsoActo.getDeterminacionByIddeterminacionbase()!=null)
									cuAdaptadaCaso.setIdDetPBUsoActo(detDeterminacionUsoActo.getDeterminacionByIddeterminacionbase().getIden());

								cuAdaptadaCaso.setIdDetValorUsoActo(idValUsoActo);
								cuAdaptadaCaso.setNombreDetValorUsoActo(nombreValUso);
								cuAdaptadaCaso.setApartadoDetValorUsoActo(ordenValUso);
								if (detValorUsoActo!=null && detValorUsoActo.getDeterminacionByIddeterminacionbase()!=null)
									cuAdaptadaCaso.setIdDetPBValorUsoActo(detValorUsoActo.getDeterminacionByIddeterminacionbase().getIden());
								
								// En el caso de que tenga regimen específico hacemos de uso/acto hacemos
								if (registroCU[9].getContents()!=null && !registroCU[9].getContents().equals("")){
									RegimenesEspecificosSimplificadosUsoActos regEsp = new RegimenesEspecificosSimplificadosUsoActos();
									if (registroCU[8].getContents()==null || registroCU[8].getContents().equals(""))
										regEsp.setNombreRegimenEspecifico("Observaciones");
									else
										regEsp.setNombreRegimenEspecifico(registroCU[8].getContents());
									
									regEsp.setTextoRegimenEspecifico(registroCU[9].getContents());
									
									cuAdaptadaCaso.getRegEspecificoUsoActo().add(regEsp);
								}
								
								
								listaCUenExcel.add(cuAdaptadaCaso);
							}
							//CASO: Caso de determinación uso/acto diferente para el uso/acto anterior
							else if (registroCU[5].getContents()!=null && !registroCU[5].getContents().equals("")){
								
								// Copiamos los valor anteriores solo
								String apartadoUsoActo =  cuAdaptadaCaso.getApartadoUsoActo();
								String nombreUsoActo =  cuAdaptadaCaso.getNombreUsoActo();
								int idUsoActo =  cuAdaptadaCaso.getIdUsoActo();
								
								cuAdaptadaCaso = new CUAdaptadaSipu();
								
								// Asignamos el id de tramite
								cuAdaptadaCaso.setIdTramite(variablesSesionUsuario.getIdTramiteTrabajoActual());
								cuAdaptadaCaso.setIdFilaExcel(indiceFila+1);
								
								cuAdaptadaCaso.setIdUsoActo(idUsoActo);
								cuAdaptadaCaso.setApartadoUsoActo(apartadoUsoActo);
								cuAdaptadaCaso.setNombreUsoActo(nombreUsoActo);
								
								// Determinacion Uso/Acto
								String nombreDetUso = registroCU[5].getContents();
								String ordenDetUso = registroCU[4].getContents();
								
								int idDetUsoActo = -1;
								Determinacion detDeterminacionUsoActo = null;
								
								if (nombreDetUso.equals("") || ordenDetUso.equals(""))
									listadoErrores.add("La Determinacion Uso/Acto no es correcto para la fila: "+(indiceFila+1));
								else{
									idDetUsoActo = servicioBasicoDeterminaciones.buscarDeterminacionPorTramiteYOrden(variablesSesionUsuario.getIdTramiteTrabajoActual(), ordenDetUso);
									if(idDetUsoActo==0)
										listadoErrores.add("La Determinación Uso/Acto no es correcto para la fila: "+(indiceFila+1));
									else{
										detDeterminacionUsoActo = servicioBasicoDeterminaciones.buscarDeterminacion(idDetUsoActo);
										if (!detDeterminacionUsoActo.getNombre().equals(nombreDetUso))
											listadoErrores.add("El nombre de la Determinacion Uso/Acto no coincide con el código indicado para la fila: "+(indiceFila+1));
									}
								}
								
								// Valores Usos/Actos
								String nombreValUso = registroCU[7].getContents();
								String ordenValUso = registroCU[6].getContents();
								
								// Para comprobar si tiene regimen específico de uso/acto
								String regUsoActo = registroCU[9].getContents();
								
								int idValUsoActo = -1;
								Determinacion detValorUsoActo = null;
								if (!nombreValUso.equals("") && ordenValUso.equals("")){
									cuAdaptadaCaso.setIdDetValorUsoActo(0);
									cuAdaptadaCaso.setIdDetPBValorUsoActo(0);
									
									cuAdaptadaCaso.setApartadoDetValorUsoActo("");
									cuAdaptadaCaso.setNombreDetValorUsoActo(nombreValUso);
								}
								else if ((nombreValUso.equals("") || ordenValUso.equals("")) && regUsoActo.equals(""))
									listadoErrores.add("El Valor del Uso/Acto no es correcto para la fila: "+(indiceFila+1));
								else if (regUsoActo.equals("")){
									idValUsoActo = servicioBasicoDeterminaciones.buscarDeterminacionPorTramiteYOrden(variablesSesionUsuario.getIdTramiteTrabajoActual(), ordenValUso);
									if(idValUsoActo==0)
										listadoErrores.add("El Valor del Uso/Acto no es correcto para la fila: "+(indiceFila+1));
									else{
										detValorUsoActo = servicioBasicoDeterminaciones.buscarDeterminacion(idValUsoActo);
										if (!detValorUsoActo.getNombre().equals(nombreValUso))
											listadoErrores.add("El Valor del Uso/Acto no coincide con el código indicado para la fila: "+(indiceFila+1));
									}
								}
								
								
								// Cargamos el elemento base
								if (entidadAplicar!=null){
									cuAdaptadaCaso.setIdEntidad(entidadAplicar.getIden());
									cuAdaptadaCaso.setNombreEntidad(entidadAplicar.getNombre());
									cuAdaptadaCaso.setClaveEntidad(entidadAplicar.getClave());
								}
								
								cuAdaptadaCaso.setIdDetUsoActo(idDetUsoActo);
								
								cuAdaptadaCaso.setNombreDetUsoActo(nombreDetUso);
								cuAdaptadaCaso.setApartadoDetUsoActo(ordenDetUso);
								if (detDeterminacionUsoActo!=null){
									if (detDeterminacionUsoActo.getDeterminacionByIddeterminacionbase()!=null)
										cuAdaptadaCaso.setIdDetPBUsoActo(detDeterminacionUsoActo.getDeterminacionByIddeterminacionbase().getIden());
									else
										listadoErrores.add("La determinacion con id=" + detDeterminacionUsoActo.getIden() + " no tiene determinacion base");
								}
								
								cuAdaptadaCaso.setIdDetValorUsoActo(idValUsoActo);
								cuAdaptadaCaso.setNombreDetValorUsoActo(nombreValUso);
								cuAdaptadaCaso.setApartadoDetValorUsoActo(ordenValUso);
								if (detValorUsoActo!=null)
									cuAdaptadaCaso.setIdDetPBValorUsoActo(detValorUsoActo.getDeterminacionByIddeterminacionbase().getIden());
								
								// En el caso de que tenga regimen específico hacemos de uso/acto hacemos
								if (registroCU[9].getContents()!=null && !registroCU[9].getContents().equals("")){
									RegimenesEspecificosSimplificadosUsoActos regEsp = new RegimenesEspecificosSimplificadosUsoActos();
									if (registroCU[8].getContents()==null || registroCU[8].getContents().equals(""))
										regEsp.setNombreRegimenEspecifico("Observaciones");
									else
										regEsp.setNombreRegimenEspecifico(registroCU[8].getContents());
									
									regEsp.setTextoRegimenEspecifico(registroCU[9].getContents());
									
									cuAdaptadaCaso.getRegEspecificoUsoActo().add(regEsp);
								}
								
								
								listaCUenExcel.add(cuAdaptadaCaso);
								
							}
							// CASO: Añadir régimen de uso/
							else if (registroCU[9].getContents()!=null && !registroCU[9].getContents().equals("") 
									&& (registroCU[7].getContents()==null || registroCU[7].getContents().equals(""))){
								// Caso en el que hay que añadir regimenes al listado de RegEspecificoUsoActo de CUAdaptadaSipu
								
								// Añadimos el régimen específico si existe
								if (!registroCU[8].getContents().equals("") || !registroCU[9].getContents().equals("")){
									RegimenesEspecificosSimplificadosUsoActos regEsp = new RegimenesEspecificosSimplificadosUsoActos();
									if (registroCU[8].getContents()==null || registroCU[8].getContents().equals(""))
										regEsp.setNombreRegimenEspecifico("Observaciones");
									else
										regEsp.setNombreRegimenEspecifico(registroCU[8].getContents());
									
									regEsp.setTextoRegimenEspecifico(registroCU[9].getContents());
									
									listaCUenExcel.get(listaCUenExcel.size()-1).getRegEspecificoUsoActo().add(regEsp);
								}
							}
							// CASO: Añadimos un clon de la anterior porque el valor es distinto
							else if (registroCU[9].getContents()!=null && !registroCU[9].getContents().equals("") 
									&& registroCU[7].getContents()!=null && !registroCU[7].getContents().equals("")){
								// Caso en el que hay que añadir regimenes al listado de RegEspecificoUsoActo de CUAdaptadaSipu
								
								int idDetUsoActo = -1;
								Determinacion detDeterminacionUsoActo = null;
								// Determinacion Uso/Acto
								
								CUAdaptadaSipu cuAdaptadaAux = copiarDatosCU(cuAdaptadaCaso);
								
								cuAdaptadaAux.setIdFilaExcel(indiceFila+1);
								// Valores Usos/Actos
								String nombreValUso = registroCU[7].getContents();
								String ordenValUso = registroCU[6].getContents();
								
								// Para comprobar si tiene regimen específico de uso/acto
								String regUsoActo = registroCU[9].getContents();
								
								int idValUsoActo = -1;
								Determinacion detValorUsoActo = null;
								if (!nombreValUso.equals("") && ordenValUso.equals("")){
									cuAdaptadaAux.setIdDetValorUsoActo(0);
									cuAdaptadaAux.setIdDetPBValorUsoActo(0);
									
									cuAdaptadaAux.setApartadoDetValorUsoActo("");
									cuAdaptadaAux.setNombreDetValorUsoActo(nombreValUso);
								}
								else if ((nombreValUso.equals("") || ordenValUso.equals("")) && regUsoActo.equals(""))
									listadoErrores.add("El Valor del Uso/Acto no es correcto para la fila: "+(indiceFila+1));
								else if (regUsoActo.equals("")){
									idValUsoActo = servicioBasicoDeterminaciones.buscarDeterminacionPorTramiteYOrden(variablesSesionUsuario.getIdTramiteTrabajoActual(), ordenValUso);
									if(idValUsoActo==0)
										listadoErrores.add("El Valor del Uso/Acto no es correcto para la fila: "+(indiceFila+1));
									else{
										detValorUsoActo = servicioBasicoDeterminaciones.buscarDeterminacion(idValUsoActo);
										if (!detValorUsoActo.getNombre().equals(nombreValUso))
											listadoErrores.add("El Valor del Uso/Acto no coincide con el código indicado para la fila: "+(indiceFila+1));
									}
								}
								
								
								// Cargamos el elemento base
								if (entidadAplicar!=null){
									cuAdaptadaAux.setIdEntidad(entidadAplicar.getIden());
									cuAdaptadaAux.setNombreEntidad(entidadAplicar.getNombre());
									cuAdaptadaAux.setClaveEntidad(entidadAplicar.getClave());
								}
								
								if (detDeterminacionUsoActo!=null){
									if (detDeterminacionUsoActo.getDeterminacionByIddeterminacionbase()!=null)
										cuAdaptadaAux.setIdDetPBUsoActo(detDeterminacionUsoActo.getDeterminacionByIddeterminacionbase().getIden());
									else
										listadoErrores.add("La determinacion con id=" + detDeterminacionUsoActo.getIden() + " no tiene determinacion base");
								}
								
								cuAdaptadaAux.setIdDetValorUsoActo(idValUsoActo);
								cuAdaptadaAux.setNombreDetValorUsoActo(nombreValUso);
								cuAdaptadaAux.setApartadoDetValorUsoActo(ordenValUso);
								if (detValorUsoActo!=null)
									cuAdaptadaAux.setIdDetPBValorUsoActo(detValorUsoActo.getDeterminacionByIddeterminacionbase().getIden());
								
								// En el caso de que tenga regimen específico hacemos de uso/acto hacemos
								if (registroCU[9].getContents()!=null && !registroCU[9].getContents().equals("")){
									RegimenesEspecificosSimplificadosUsoActos regEsp = new RegimenesEspecificosSimplificadosUsoActos();
									if (registroCU[8].getContents()==null || registroCU[8].getContents().equals(""))
										regEsp.setNombreRegimenEspecifico("Observaciones");
									else
										regEsp.setNombreRegimenEspecifico(registroCU[8].getContents());
									
									regEsp.setTextoRegimenEspecifico(registroCU[9].getContents());
									
									cuAdaptadaAux.getRegEspecificoUsoActo().add(regEsp);
								}
								
								
								listaCUenExcel.add(cuAdaptadaAux);
								
							}
							// CASO Una nueva determinacion
							else if ( registroCU[10].getContents()!=null &&  !registroCU[10].getContents().equals(""))
							{
								String nombreDet = registroCU[11].getContents();
								String ordenDet = registroCU[10].getContents();
								int idDeterminacion = -1;
								Determinacion determinacion = null;
								
								if (nombreDet.equals("") || ordenDet.equals(""))
									listadoErrores.add("Nombre u orden vacios. La Determinacion no es correcta para la fila: "+(indiceFila+1));
								else{
									idDeterminacion = servicioBasicoDeterminaciones.buscarDeterminacionPorTramiteYOrden(variablesSesionUsuario.getIdTramiteTrabajoActual(), ordenDet);
									if(idDeterminacion==0)
										listadoErrores.add("No se encuentra la determinación. La Determinacion no es correcta para la fila: "+(indiceFila+1));
									else{
										determinacion = servicioBasicoDeterminaciones.buscarDeterminacion(idDeterminacion);
										if (!determinacion.getNombre().equals(nombreDet))
											listadoErrores.add("El Valor del Uso/Acto no coincide con el código indicado para la fila: "+(indiceFila+1));
									}
								}
								
								cuAdaptadaDetermacion = copiarDatosCU(cuAdaptadaCaso);
								
								// Si idUsoActo de cuAdaptadaCaso es 0 es de las Antiguas
								if(cuAdaptadaCaso.getIdUsoActo()==0)
									cuAdaptadaDetermacion.setCuAdptadaAntigual(true);
								
								// Caso en el que en el excel no hay elementos que rellenen cuAdaptadaCaso para la entidad
								// Esto es 
								if (cuAdaptadaDetermacion.getIdEntidad()==0){
									// Cargamos el elemento base
									if (entidadAplicar!=null){
										cuAdaptadaDetermacion.setIdEntidad(entidadAplicar.getIden());
										cuAdaptadaDetermacion.setNombreEntidad(entidadAplicar.getNombre());
										cuAdaptadaDetermacion.setClaveEntidad(entidadAplicar.getClave());
										
										cuAdaptadaDetermacion.setIdTramite(variablesSesionUsuario.getIdTramiteTrabajoActual());
										
										cuAdaptadaDetermacion.setCuAdptadaAntigual(true);
									}
								}
								
								cuAdaptadaDetermacion.setIdFilaExcel(indiceFila+1);
								//cuAdaptadaDetermacion = cuAdaptadaCaso;
								
								if (determinacion!=null)
									cuAdaptadaDetermacion.setIdDeterminacion(determinacion.getIden());
								cuAdaptadaDetermacion.setApartadoDeterminacion(ordenDet);
								cuAdaptadaDetermacion.setNombreDeterminacion(nombreDet);
								
								// Si registroCU[12] y registroCU[13] están rellenos es que tenemos un valor de referencia
								String apartadoValor = registroCU[12].getContents();
								String nombreValor = registroCU[13].getContents();
								int idDeterminacionValor;
								Determinacion determinacionValor = null;
								if (!apartadoValor.equals("") && !nombreValor.equals("")){
									idDeterminacionValor = servicioBasicoDeterminaciones.buscarDeterminacionPorTramiteYOrden(variablesSesionUsuario.getIdTramiteTrabajoActual(), apartadoValor);
									if(idDeterminacionValor==0)
										listadoErrores.add("No se encuentra el valor. La determinacion no es correcta para la fila: "+(indiceFila+1));
									else{
										determinacionValor = servicioBasicoDeterminaciones.buscarDeterminacion(idDeterminacionValor);
										if (!determinacionValor.getNombre().equals(nombreValor))
											listadoErrores.add("El Valor del Uso/Acto no coincide con el código indicado para la fila: "+(indiceFila+1));
									}
									
									// Añadimos los valores de la determinacion
									cuAdaptadaDetermacion.setIdDetValorReferencia(idDeterminacionValor);
									cuAdaptadaDetermacion.setApartadoValorReferencia(apartadoValor);
									cuAdaptadaDetermacion.setNombreValorReferencia(nombreValor);
								}
								else if (!nombreValor.equals(""))
									cuAdaptadaDetermacion.setValor(nombreValor);
								
								System.out.println("INDICE FILA--" + apartadoValor);
								// Añadimos el primer régimen específico si tiene
								if (!registroCU[14].getContents().equals("") || !registroCU[15].getContents().equals("")){
									RegimenesEspecificosSimplificadosDeterminacion regEsp = new RegimenesEspecificosSimplificadosDeterminacion();
									if (registroCU[14].getContents()==null || registroCU[14].getContents().equals(""))
										regEsp.setNombreRegimenEspecifico("Observaciones");
									else
										regEsp.setNombreRegimenEspecifico(registroCU[14].getContents());
									
									regEsp.setTextoRegimenEspecifico(registroCU[15].getContents());
									
									cuAdaptadaDetermacion.getRegEspecificoDeterminacion().add(regEsp);
								}
								// Añadimos el elemento a insertar
								listaCUenExcel.add(cuAdaptadaDetermacion);
							}
							else if (registroCU[13].getContents().equals("")){
								// Caso en el que hay que añadir regimenes al listado de RegEspecificoDeterminacion de CUAdaptadaSipu
								// Añadimos el regimen específico si tiene
								if (!registroCU[14].getContents().equals("") || !registroCU[15].getContents().equals("")){
									RegimenesEspecificosSimplificadosDeterminacion regEsp = new RegimenesEspecificosSimplificadosDeterminacion();
									if (registroCU[14].getContents()==null || registroCU[14].getContents().equals(""))
										regEsp.setNombreRegimenEspecifico("Observaciones");
									else
										regEsp.setNombreRegimenEspecifico(registroCU[14].getContents());
									
									regEsp.setTextoRegimenEspecifico(registroCU[15].getContents());
									
									listaCUenExcel.get(listaCUenExcel.size()-1).getRegEspecificoDeterminacion().add(regEsp);
								}
							}
							// caso de nueva determinacion copiando la anterior
							else{
								// Cogemos el anterior y lo copiamos campo a campo
								CUAdaptadaSipu cuAdaptadaAux = listaCUenExcel.get(listaCUenExcel.size()-1); 
								
								cuAdaptadaDetermacion = copiarDatosCU(cuAdaptadaAux);
								cuAdaptadaDetermacion.setIdFilaExcel(indiceFila+1);
								
								cuAdaptadaDetermacion.setIdDeterminacion(cuAdaptadaAux.getIdDeterminacion());
								cuAdaptadaDetermacion.setApartadoDeterminacion(cuAdaptadaAux.getApartadoDeterminacion());
								cuAdaptadaDetermacion.setNombreDeterminacion(cuAdaptadaAux.getNombreDeterminacion());
								
								// Si registroCU[12] y registroCU[13] están rellenos es que tenemos un valor de referencia
								String apartadoValor = registroCU[12].getContents();
								String nombreValor = registroCU[13].getContents();
								int idDeterminacionValor;
								Determinacion determinacionValor = null;
								if (!apartadoValor.equals("") && !nombreValor.equals("")){
									idDeterminacionValor = servicioBasicoDeterminaciones.buscarDeterminacionPorTramiteYOrden(variablesSesionUsuario.getIdTramiteTrabajoActual(), apartadoValor);
									if(idDeterminacionValor==0)
										listadoErrores.add("No se encuentra el valor. La determinacion no es correcta para la fila: "+(indiceFila+1));
									else{
										determinacionValor = servicioBasicoDeterminaciones.buscarDeterminacion(idDeterminacionValor);
										if (!determinacionValor.getNombre().equals(nombreValor))
											listadoErrores.add("El Valor del Uso/Acto no coincide con el código indicado para la fila: "+(indiceFila+1));
									}
									
									// Añadimos los valores de la determinacion
									cuAdaptadaDetermacion.setIdDetValorReferencia(idDeterminacionValor);
									cuAdaptadaDetermacion.setApartadoValorReferencia(apartadoValor);
									cuAdaptadaDetermacion.setNombreValorReferencia(nombreValor);
								}
								else if (!nombreValor.equals(""))
									cuAdaptadaDetermacion.setValor(nombreValor);
								
								// Añadimos el primer régimen específico si tiene
								if (!registroCU[14].getContents().equals("") || !registroCU[15].getContents().equals("")){
									RegimenesEspecificosSimplificadosDeterminacion regEsp = new RegimenesEspecificosSimplificadosDeterminacion();
									if (registroCU[14].getContents()==null || registroCU[14].getContents().equals(""))
										regEsp.setNombreRegimenEspecifico("Observaciones");
									else
										regEsp.setNombreRegimenEspecifico(registroCU[14].getContents());
									
									regEsp.setTextoRegimenEspecifico(registroCU[15].getContents());
									
									cuAdaptadaDetermacion.getRegEspecificoDeterminacion().add(regEsp);
								}
								// Añadimos el elemento a insertar
								listaCUenExcel.add(cuAdaptadaDetermacion);
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
				listadoErrores = elementosDuplicados(listaCUenExcel);
				
				if (listadoErrores.size()>0){
					log.error("Hay "+listadoErrores.size()+" Errores Previos. No mandamos a BD");
					facesMessages.addFromResourceBundle("erroresexcelnumero", new Object[]{listadoErrores.size()});
					
					listadoErroresImportacion = listadoErrores;
				}
				else{
					facesMessages.addFromResourceBundle("procesofinalizado", null);
					log.debug("Numero de Regimenes que se mandan al Servicio:"+listaCUenExcel.size());
					
					
					List<String> erroresServicio = importadorCUCanariasExcel.persistirCUdeExcelRus(getIdTramiteTrabajo(), listaCUenExcel);
					
					if (erroresServicio.size()>0)
					{
						log.error("Hay "+erroresServicio.size()+" Errores detectados por el Servicio. No se persiste en BD");
						facesMessages.addFromResourceBundle("erroresexcelservicio", new Object[]{erroresServicio.size()});
						
						listadoErroresImportacion = erroresServicio;
										
						
					}
					else
					{
						facesMessages.addFromResourceBundle("Excel importado correctamente. Pulse el botón Crear Cond. Importadas para cargar las Conds. Urbanísticas", null);
						mostrarBotonImportar = true;
					}
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
	
	public CUAdaptadaSipu copiarDatosCU(CUAdaptadaSipu cuAdaptadaCaso)
	{
		CUAdaptadaSipu cuReturn = new CUAdaptadaSipu();
		
		//FGA: añadido porque fallaba a Sonia: Cuando habia 2 valores, ponia una vez TRUE y otra FALSE
		cuReturn.setCuAdptadaAntigual(cuAdaptadaCaso.isCuAdptadaAntigual());
		//
		
		cuReturn.setIdTramite(cuAdaptadaCaso.getIdTramite());
		
		cuReturn.setIdEntidad(cuAdaptadaCaso.getIdEntidad());
		cuReturn.setClaveEntidad(cuAdaptadaCaso.getClaveEntidad());
		cuReturn.setNombreEntidad(cuAdaptadaCaso.getNombreEntidad());
		
		cuReturn.setIdUsoActo(cuAdaptadaCaso.getIdUsoActo());
		cuReturn.setNombreUsoActo(cuAdaptadaCaso.getNombreUsoActo());
		cuReturn.setApartadoUsoActo(cuAdaptadaCaso.getApartadoUsoActo());
			
		cuReturn.setIdDetUsoActo(cuAdaptadaCaso.getIdDetUsoActo());
		cuReturn.setNombreDetUsoActo(cuAdaptadaCaso.getNombreDetUsoActo());
		cuReturn.setApartadoDetUsoActo(cuAdaptadaCaso.getApartadoDetUsoActo());
		cuReturn.setIdDetPBUsoActo(cuAdaptadaCaso.getIdDetPBUsoActo());
		
		cuReturn.setIdDetValorUsoActo(cuAdaptadaCaso.getIdDetValorUsoActo());
		cuReturn.setNombreDetValorUsoActo(cuAdaptadaCaso.getNombreDetValorUsoActo());
		cuReturn.setApartadoDetValorUsoActo(cuAdaptadaCaso.getApartadoDetValorUsoActo());
		cuReturn.setIdDetPBValorUsoActo(cuAdaptadaCaso.getIdDetPBValorUsoActo());
		
		return cuReturn;
	}


	public boolean isMostrarBotonImportar() {
		return mostrarBotonImportar;
	}


	public void setMostrarBotonImportar(boolean mostrarBotonImportar) {
		this.mostrarBotonImportar = mostrarBotonImportar;
	}
	
	/**
	 * Esta funcion comprueba dos cosas
	 * 1- Que no haya dos usos/acto repetidos para la misma entidad
	 * 2- Que un padre y un hijo no puedan estar al mismo nivel en la misma entidad
	 * @param listadoDto
	 * @return
	 */
	public List<String> elementosDuplicados(List <CUAdaptadaSipu> listadoDto){
		
		List<String> listaErrores = new ArrayList<String>();
		
		for (int t=0; t<(listadoDto.size()-1); t++) {
			Determinacion det = em.find(Determinacion.class, listadoDto.get(t).getIdUsoActo());
			for (int i=0; i<(listadoDto.size()-1); i++) {
				if (i!=t){
					
					// FGA: Comento esta validacion ya que no es correcta, habria que mirar tambien en valores... Lo que pasa en CU_ZON-ENP_1 de Las Cumbres_v3
					/*
					if (listadoDto.get(t).getIdEntidad()==listadoDto.get(i).getIdEntidad()
							&& listadoDto.get(t).getIdUsoActo()==listadoDto.get(i).getIdUsoActo()
							&& listadoDto.get(t).getNombreUsoActo()!=null
							&& !listadoDto.get(t).getNombreUsoActo().equals("")
							&& listadoDto.get(t).getIdDetUsoActo()==listadoDto.get(i).getIdDetUsoActo()){
						// Si tienen sólo valor da igual que sen iguales está OK
						if (listadoDto.get(t).getNombreDetValorUsoActo()!=null && listadoDto.get(i).getNombreDetValorUsoActo()!=null
								&& !listadoDto.get(t).getNombreDetValorUsoActo().equals("") && !listadoDto.get(i).getNombreDetValorUsoActo().equals("")
								&& listadoDto.get(t).getIdDetValorUsoActo()<=0 && listadoDto.get(i).getIdDetValorUsoActo()<=0)
							;
						else
							listaErrores.add("El uso acto nombre --" + listadoDto.get(t).getNombreUsoActo() + "-- está repetido para la misma entidad: " + listadoDto.get(t).getNombreEntidad());
					}
					*/
					if (listadoDto.get(t).getIdEntidad()==listadoDto.get(i).getIdEntidad()){
						
						if (det!=null && det.getDeterminacionByIdpadre()!=null  && det.getDeterminacionByIdpadre().getIden()==listadoDto.get(i).getIdUsoActo())
							listaErrores.add("El uso acto nombre --" + listadoDto.get(t).getNombreUsoActo() + "-- está al mismo nivel que su padre en la endidad: " + listadoDto.get(t).getNombreEntidad());
					}
				}
				
			}
		}
		
			return listaErrores;
		
	}
}


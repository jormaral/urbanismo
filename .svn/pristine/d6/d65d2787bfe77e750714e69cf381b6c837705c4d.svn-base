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
import com.mitc.redes.editorfip.entidades.interfazgrafico.CUExcelDTO;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Entidad;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Tramite;
import com.mitc.redes.editorfip.servicios.basicos.fip.entidades.ServicioBasicoEntidades;
import com.mitc.redes.editorfip.servicios.sesion.VariablesSesionUsuario;

/**
 * @author FGA
 *
 */
@Scope(ScopeType.SESSION)
@Stateful
@Name("servicioImportacionEntidadBean")
public class ServicioImportacionEntidadBean implements ServicioImportacionEntidad {
	
	   
	@Logger 
	private Log log;
	
	@In(create=true)
    FacesMessages facesMessages;

	private Workbook workbook ;
	private List<SelectItem> sheetListItems;
	private int[] selectedSheet;
	
	private List<String> listadoErroresImportacion;
	
	private static int indEtiqueta = 8;
	private static int equivale = 9;
	private static int clave = 11;
	
    // Fichero a cargar
    private FileInfo currentFile = null;
    // porcentaje de carga del fichero
    private int fileProgress;
	
	@In(create = true)
	ServicioBasicoEntidades servicioBasicoEntidades;
	
	@PersistenceContext
	EntityManager em;
	
	@In (create = true, required = false)
	VariablesSesionUsuario variablesSesionUsuario;
	
	public ServicioImportacionEntidadBean() {
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
		try {
			
			Sheet sheet = workbook.getSheet(selectedSheet[0]); 
			
			List <CUExcelDTO> listaCUenExcel = new ArrayList<CUExcelDTO>();
			List <String> listadoErrores = new ArrayList<String>();
			
			boolean fincu = false;
			boolean anteriorFueFinGrupo = false;
			boolean anteriorFueSubGrupo = false;
			
			
			log.debug("Num Columnas="+sheet.getColumns());
			
			
			
			//Ponemos el trámite
			
			Tramite tramite  = em.find(Tramite.class, variablesSesionUsuario.getIdTramiteEncargadoTrabajo());
			
			
			if (!servicioBasicoEntidades.tieneEntidadTramite(variablesSesionUsuario.getIdTramiteEncargadoTrabajo()))
			{
				// Si hay 14 columnas, es correcto el formato y se puede seguir					
				if (sheet.getColumns()==14 && comprobarCabecerasEntidad(sheet.getRow(2),listadoErrores))
				{
					// Cargo el registro de ámbito que será el padre de todos
					
					Cell[] registroAmbito = sheet.getRow(3);
					if (registroAmbito!=null && registroAmbito[2].getContents().equalsIgnoreCase("AMB"))
					{
						// Grabamos el ámbito
						Entidad ambito = new Entidad();
						ambito.setClave(registroAmbito[clave].getContents());
						ambito.setEtiqueta(registroAmbito[indEtiqueta].getContents());
						ambito.setNombre("Ámbito");
						
						ambito.setTramite(tramite);
						ambito.setCodigo(servicioBasicoEntidades.nextCodigo(tramite.getIden()+""));
 						em.persist(ambito);
						//em.flush();
						
						Entidad entidadBase = new Entidad();
						Entidad entidadNivel_2 = new Entidad();
						Entidad entidadNivel_3 = new Entidad();
						
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
										
										String nombreEntidad = registroCU[2].getContents();
										
										// Nombre de la entidad
										if (existeCodigoEntidad(nombreEntidad))
										{
											entidadBase.setNombre(registroCU[3].getContents());
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
										if (registroCU[equivale].getContents()!=null && !registroCU[equivale].getContents().equals("")){
											ent= servicioBasicoEntidades.obtenerEquivalente(getEquivalenteFormateado(registroCU[equivale].getContents()),getIdTramiteBaseTrabajo());
											if (ent!=null)
												entidadBase.setEntidadByIdentidadbase(ent);
											else
												listadoErrores.add("No se ha encontrado el Equivalente de la fila: "+(indiceFila+1));
										}
										
										// Creamos la nueva entidad  
										entidadBase.setCodigo(servicioBasicoEntidades.nextCodigo(entidadBase.getTramite().getIden()+""));
										em.persist(entidadBase);
										//em.flush();
										
										//Pongo a false la variable anteriorFueFinGrupo para que no entre mas aqui
										anteriorFueFinGrupo = false;
										anteriorFueSubGrupo = false;
									}
									// Si es del nuvel 2....
									else if (registroCU[5].getContents()!=null && !registroCU[5].getContents().equalsIgnoreCase(""))
									{
										
										// El padre de la nueva entidad es el ambito anteriormente creado
										entidadNivel_2 = new Entidad();
										entidadNivel_2.setEntidadByIdpadre(entidadBase);
										entidadNivel_2.setTramite(tramite);
									
										entidadNivel_2.setNombre(registroCU[5].getContents());
										
										// Clave y Etiqueta
										
										String claveEntidad = registroCU[clave].getContents();
										
										if (claveEntidad!="" && !claveEntidad.equalsIgnoreCase(""))
										{
											entidadNivel_2.setClave(claveEntidad);
										}
										else
											listadoErrores.add("No se ha encontrado clave para la fila: "+(indiceFila+1));
										
										String etiqueta = registroCU[indEtiqueta].getContents();
										if (etiqueta!="" && !etiqueta.equalsIgnoreCase(""))
											entidadNivel_2.setEtiqueta(etiqueta);
										
										Entidad ent = null;
										if (registroCU[equivale].getContents()!=null && !registroCU[equivale].getContents().equals("")){
											ent= servicioBasicoEntidades.obtenerEquivalente(getEquivalenteFormateado(registroCU[equivale].getContents()),getIdTramiteBaseTrabajo());
											if (ent!=null)
												entidadNivel_2.setEntidadByIdentidadbase(ent);
											else
												listadoErrores.add("No se ha encontrado el Equivalente de la fila: "+(indiceFila+1));
										}
										
										// Creamos la nueva entidad  
										entidadNivel_2.setCodigo(servicioBasicoEntidades.nextCodigo(entidadNivel_2.getTramite().getIden()+""));
										em.persist(entidadNivel_2);
										//em.flush();
										
										anteriorFueSubGrupo = true;
										
									}
									else if (registroCU[7].getContents()!=null && !registroCU[7].getContents().equalsIgnoreCase(""))
									{
										// El padre de la nueva entidad es el ambito anteriormente creado
										entidadNivel_3 = new Entidad();
										entidadNivel_3.setTramite(tramite);
										
										// Si el subgrupo está vacío el padre es la entidad base
										if (!anteriorFueSubGrupo)
											entidadNivel_3.setEntidadByIdpadre(entidadBase);
										else
											entidadNivel_3.setEntidadByIdpadre(entidadNivel_2);
										
										entidadNivel_3.setNombre(registroCU[7].getContents());
										
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
										if (registroCU[equivale].getContents()!=null && !registroCU[equivale].getContents().equals("")){
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
										
									}
								}
								
								
							}
						}
					}
					
					// Si no ha alcanzado 'finentidades' es un error
					if (!fincu)
					{
						listadoErrores.add("No se ha encontrado 'finentidades' en el excel");

					}
				}
				else
				{
					// No es correcto el formato de las columnas
					listadoErrores.add("No es correcto el formato de las columnas en el excel. Comprueba que tiene SOLO 14 Columnas. Vuelva a Copiar en una nueva Hoja esas 14 columnas");
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
			//servicioBasicoEntidades.borrarEntidadesTramite(getIdTramiteTrabajo());
			
			
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
	
	private static String getEquivalenteFormateado(String codigo)
	{
		int inicio = -1;
		if (codigo.lastIndexOf(".")!=-1)
			inicio = codigo.lastIndexOf(".");
		
		codigo = codigo.substring(inicio+1, codigo.length());	
		return codigo;
	}
	
	public boolean comprobarCabecerasEntidad(Cell[] row, List <String> listadoErrores)
	{
		boolean correcto= true;
		
		if (!row[0].getContents().toLowerCase().equals("mun."))
		{
			listadoErrores.add("Fallo en la columna de: 'mun.'");
			correcto=false;
		}
		else if (!row[1].getContents().toLowerCase().equals("plan"))
		{
			listadoErrores.add("Fallo en la columna de: 'plan'");
			correcto=false;
		}
		else if (!row[2].getContents().toLowerCase().equals("grupo"))
		{
			listadoErrores.add("Fallo en la columna de: 'grupo'");
			correcto=false;
		}
		else if (!row[4].getContents().toLowerCase().equals("subgrupo"))
		{
			listadoErrores.add("Fallo en la columna de: 'subgrupo'");
			correcto=false;
		}
		else if (!row[6].getContents().toLowerCase().equals("entidades"))
		{
			listadoErrores.add("Fallo en la columna de: 'entidades'");
			correcto=false;
		}
		else if (!row[8].getContents().toLowerCase().equals("etiqueta"))
		{
			listadoErrores.add("Fallo en la columna de: 'etiqueta'");
			correcto=false;
		}
		else if (!row[9].getContents().toLowerCase().equals("equivalencias"))
		{
			listadoErrores.add("Fallo en la columna de: 'equivalencias'");
			correcto=false;
		}
		else if (!row[10].getContents().toLowerCase().equals("fuente"))
		{
			listadoErrores.add("Fallo en la columna de: 'fuente'");
			correcto=false;
		}
		else if (!row[11].getContents().toLowerCase().equals("clave"))
		{
			listadoErrores.add("Fallo en la columna de: 'clave'");
			correcto=false;
		}
		else if (!row[12].getContents().toLowerCase().equals("observaciones"))
		{
			listadoErrores.add("Fallo en la columna de: 'observaciones'");
			correcto=false;
		}
		else if (!row[13].getContents().toLowerCase().equals("notas idom"))
		{
			listadoErrores.add("Fallo en la columna de: 'notas idom'");
			correcto=false;
		}
		
		return correcto;
		
	}
	
	@Remove
	public void destroy(){};
}


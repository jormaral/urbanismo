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

package com.mitc.redes.editorfip.servicios.gestionfip.obtencionfip;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.faces.component.html.HtmlOutputText;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;

@Stateful
@Scope(ScopeType.SESSION)
@Name("logImportacion") 
public class LogImportacionBean implements LogImportacion
{
	
	@Logger private Log log;
    
    private HtmlOutputText campoDinamico = new HtmlOutputText();
    
    private static final String RUTA_LOG = System.getProperty("jboss.home.dir") + System.getProperty("file.separator")
	   + "var" + System.getProperty("file.separator") + "tmp" + System.getProperty("file.separator") + "Importacion.log";
    
    private static final String SALTO_LINEA = System.getProperty("line.separator");
    
    
    /* (non-Javadoc)
	 * @see com.mitc.redes.editorfip.servicios.gestionfip.obtencionfip.LogImportacion#inicalizarFichero()
	 */
    @Override
	public void inicalizarFichero() {
    	
    	File ficheroLog = new File(RUTA_LOG);
    	
    	if(ficheroLog.exists())
    		ficheroLog.delete(); 	
    	
    	try {
    		ficheroLog.createNewFile();
    		
		} catch (Exception e) {
			log.error("ERROR AL INICIALIZAR EL LOG: " + e.getCause(), null);
			e.printStackTrace();
		}
    	
    }
        
   /*
    public void iniciarProceso() {
    	
    	File ficheroLog = new File(RUTA_LOG);
    	try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(ficheroLog));
			
			for(int i = 0; i<7; i++) {  
				
				String linea = this.obtenerFechaHora() +  " ---->   Mi proceso en log: " + i + SALTO_LINEA;
				log.info("LINEA OBTENIDA: " + linea, null);
				bw.write(linea);
				bw.flush();
				Thread.sleep(5000);
			}
			
		} catch (IOException e) {
			log.error(e.getCause(), null);
			e.printStackTrace();
		} catch (InterruptedException e) {
			log.error(e.getCause(), null);
			e.printStackTrace();
		}
    }
    */
    
    
    /* (non-Javadoc)
	 * @see com.mitc.redes.editorfip.servicios.gestionfip.obtencionfip.LogImportacion#anadirLinea(java.lang.String)
	 */
    @Override
	public void anadirLinea(String linea) {
    		try {
    			
    			
    			File ficheroLog = new File(RUTA_LOG);
    			FileWriter escritorFichero = new FileWriter(ficheroLog,true);
    			
    			String lineaCompleta = this.obtenerFechaHora() +  " ----> " +  linea + SALTO_LINEA;
    			
    			escritorFichero.append(lineaCompleta);
    			escritorFichero.flush();
    			
			} catch (Exception e) {
				log.error("ERROR AL AÑADIR LA LINEA AL LOG: " + e.getCause(), null);
				e.printStackTrace();
			}
    }
	
    private String obtenerFechaHora() {
    	
    	SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");    	
    	Date fecha = new Date();
    	return formatoFecha.format(fecha);
    	
    }
    

	/* (non-Javadoc)
	 * @see com.mitc.redes.editorfip.servicios.gestionfip.obtencionfip.LogImportacion#getCampoDinamico()
	 */
	@Override
	public HtmlOutputText getCampoDinamico() {
		return campoDinamico;
	}


	/* (non-Javadoc)
	 * @see com.mitc.redes.editorfip.servicios.gestionfip.obtencionfip.LogImportacion#setCampoDinamico(javax.faces.component.html.HtmlOutputText)
	 */
	@Override
	public void setCampoDinamico(HtmlOutputText campoDinamico) {
		this.campoDinamico = campoDinamico;
	}


	/* (non-Javadoc)
	 * @see com.mitc.redes.editorfip.servicios.gestionfip.obtencionfip.LogImportacion#actualizarLog()
	 */
	@Override
	public void actualizarLog() {
		File ficheroLog = new File(RUTA_LOG);
		String texto = "";
		try {
			BufferedReader reader = new BufferedReader(new FileReader(ficheroLog));
			String linea = reader.readLine() + "\n";
			while(linea != null){
				texto += linea + "\n";
				linea = reader.readLine();
			}
			
			campoDinamico.setValue(texto);
			
			reader.close();
			
			
		} catch (FileNotFoundException e) {
			log.error(e.getCause(), null);
			e.printStackTrace();
		} catch (IOException e) {
			log.error(e.getCause(), null);
			e.printStackTrace();
		}
		
	}
	

	/* (non-Javadoc)
	 * @see com.mitc.redes.editorfip.servicios.VariablesSesionUsuario#destroy()
	 */
	/* (non-Javadoc)
	 * @see com.mitc.redes.editorfip.servicios.VariablesSesionUsuario#destroy()
	 */
    
    
	/* (non-Javadoc)
	 * @see com.mitc.redes.editorfip.servicios.gestionfip.obtencionfip.LogImportacion#destroy()
	 */
	@Override
	@Remove
    public void destroy() {}
    

}

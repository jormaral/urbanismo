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

package com.mitc.redes.editorfip.servicios.gestionfip.importarfip;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jboss.seam.log.Log;
import org.jboss.seam.log.Logging;



public class Resultado implements Serializable {
	
	Log log = Logging.getLog(Resultado.class);

	// Vars
	public  int countSQL = 0;
	public  Map<String,Integer> contador = new HashMap<String,Integer>();
	
	
	// Infor y errores
	public  List<String> errores = new ArrayList<String>();
	public  List<String> infos = new ArrayList<String>();
	
	
	public Resultado() {
		
	}
	
	public  void error(String err) {
		log.error(err);
		errores.add(err);
	}
	
	public  void info(String s) {
		log.info(s);
		infos.add(s);
		
		
	}
	
	public void log() {
		
		log.info("");
		log.info("==================================");
		log.info("  R E S U L T A D O");
		log.info("==================================");
		
		log.info("");
		for(String info:infos) {			
			log.info("  "+info);		
		}
		
		if (errores.size()>0) {
			log.info("");	
			log.info("ERRORES");
			for(String error:errores) {			
				log.info("  "+error);		
			}	
		}	
	}
	
	public String toString() {
		
		String s = "";
		
		s += "\r";
		s += "==================================\r";
		s += "  R E S U L T A D O\r";
		s += "==================================\r";
		
		s += "\r";
		for(String info:infos) {			
			s += " * ["+(new SimpleDateFormat("dd/MM/yyyy, HH:mm")).format(new Date())+"] "+info+"\r";		
		}
		
		if (errores.size()>0) {
			s += "\r";	
			s += "ERRORES\r";
			for(String error:errores) {			
				s += "  ["+(new SimpleDateFormat("dd/MM/yyyy, HH:mm")).format(new Date())+"] "+error+"\r";		
			}	
		}
		return s;
	}
	
}

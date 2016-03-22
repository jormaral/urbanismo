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

package com.mitc.redes.editorfip.servicios.gestionfip.generacionSLD;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.log.Log;
import org.jboss.seam.log.Logging;

import com.mitc.redes.editorfip.entidades.rpm.gestionfip.PlanesEncargados;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Tramite;
import com.mitc.redes.editorfip.servicios.gestionfip.obtencionfip.LogImportacion;


@Stateless
@Name("generarSLDdeTramites")
public class GenerarSLDdeTramitesBean implements GenerarSLDdeTramites {

	@PersistenceContext EntityManager em;
	private Log  log = Logging.getLog(GenerarSLDdeTramitesBean.class);
	
	
	
	
	/*
	 * 	GENERAR PLANES ENCARGADOS
	 * 
	 * 	Recorrer todos los planes encargados existentes y generar sus SLD's
	 * 
	 * (non-Javadoc)
	 * @see com.mitc.redes.editorfip.servicios.gestionfip.generacionSLD.GenerarSLDdeTramites#generarPlanesEncargados()
	 */
	public void generarPlanesEncargados() {

		// Obtener todos los planes encargados que esten IMPORTADOS
		log.info("Obteniendo todos los planes encargados ...");
		//logImportacion.anadirLinea("Obteniendo todos los planes encargados ...");
		List<PlanesEncargados> planesEncargados = em.createQuery("select pe from PlanesEncargados pe ").getResultList();
		log.info("Planes encargados: " + planesEncargados.size());
		//logImportacion.anadirLinea("Planes encargados: " + planesEncargados.size());
		
		// Por cada plan ...
		for(PlanesEncargados plan : planesEncargados) {
			
			// Crear SLD's
			log.info("- - - - - - - - - - - - - - - - ");
			log.info("Creando SLD's del plan encargado, id: " + plan.getId());
			
			//logImportacion.anadirLinea("- - - - - - - - - - - - - - - - ");
			//logImportacion.anadirLinea("Creando SLD's del plan encargado, id: " + plan.getId());
			generarSLD(plan.getTramiteEncargado().getIden(), plan.getAmbito().getIden());
		}
	}
	
	
	public void generarSLDTodosTramites() {
		
		List<Tramite> tramList = em.createQuery("select t from Tramite t ").getResultList();
		
		// Por cada tramite ...
		for(Tramite trm : tramList) {
			
			// Crear SLD's
			log.info("- - - - - - - - - - - - - - - - ");
			log.info("Creando SLD's del tramite, id: " + trm.getIden());
			
			//logImportacion.anadirLinea("- - - - - - - - - - - - - - - - ");
			//logImportacion.anadirLinea("Creando SLD's del plan encargado, id: " + plan.getId());
			generarSLD(trm.getIden(), trm.getPlan().getIdambito());
		}
		
		
	}
	
	/*
	 * 	GENERAR SLD
	 * 
	 * 	Genera todos los SLDs dado un tramite y ambito partiendo de las
	 * 	plantillas
	 * 
	 * (non-Javadoc)
	 * @see com.mitc.redes.editorfip.servicios.gestionfip.generacionSLD.GenerarSLDdeTramites#generarSLD(java.lang.Integer, java.lang.Integer)
	 */
	public void generarSLD(Integer idTramite, Integer idAmbito) {
		
		log.info("Generando SLDs ...");
		//logImportacion.anadirLinea("Generando SLDs ...");
		
		String fichero, nombreNuevoFichero, linea;
		String[] sufijos = {"acc","afe","amb","cat","cla","ges","pro","sis","zon","ali","ras","sin"};
		File nuevoFichero, ficheroPlantilla;
		String rutaEscritura = System.getProperty("jboss.home.dir")+File.separator+"var"+File.separator+"sld.war"+File.separator;	
		String rutaLectura = System.getProperty("jboss.home.dir")+File.separator+"var"+File.separator+"sldPlantillas"+File.separator;	
		FileReader fr;
		BufferedReader br;
		PrintWriter pw;
		int numLineas=0;
		
		// Por cada fichero template
		for(String sufijo : sufijos) {
			
			log.info(" ==> Sufijo: " + sufijo);
			//logImportacion.anadirLinea(" ==> Sufijo: " + sufijo);
			
			// Abrir fichero plantills SLD
			fichero = "idtramite_"+sufijo+".xml";
			log.info("Abriendo fichero plantilla SLD: " + rutaLectura + fichero);
			//logImportacion.anadirLinea("Abriendo fichero plantilla SLD: " + rutaLectura + fichero);
			
			ficheroPlantilla = new File(rutaLectura + fichero);
			try {
				fr = new FileReader(ficheroPlantilla);
		        br = new BufferedReader(fr);
			} catch (Exception e) {
				log.error("Error abriendo fichero de lectura "+rutaLectura+fichero+": "+e.getMessage());
				//logImportacion.anadirLinea("Error abriendo fichero de lectura "+rutaLectura+fichero+": "+e.getMessage());
				continue;
			}
		        
			// Crear y abrir fichero de escritura
			nombreNuevoFichero = idTramite + "_" + sufijo + ".xml";
			log.info("Nombre del fichero a guardar: " + nombreNuevoFichero);
			//logImportacion.anadirLinea("Nombre del fichero a guardar: " + nombreNuevoFichero);
			
			nuevoFichero = new File(rutaEscritura + nombreNuevoFichero);
			try {
				nuevoFichero.createNewFile();
				pw = new PrintWriter(nuevoFichero);
			} catch (Exception e) {
				log.error("Error creando fichero de escritura "+rutaEscritura+nombreNuevoFichero+": "+e.getMessage());
				//logImportacion.anadirLinea("Error creando fichero de escritura "+rutaEscritura+nombreNuevoFichero+": "+e.getMessage());
				continue;
			}
		    
			try {
				
		        // Lectura del fichero y escritura 
				numLineas = 0;
		        while ((linea = br.readLine()) != null) {
		            linea = linea.replace("##idTramite##", idTramite.toString());
		            linea = linea.replace("##idAmbito##", idAmbito.toString());
		            pw.println(linea);
		            numLineas++;
		        } 

			} catch (Exception e) {
				e.printStackTrace();
			}
		        
			// Terminando el proceso
			pw.close();
			try {
				fr.close();
			} catch (IOException e) {
				log.error("Error cerrando fichero de lectura: " + e.getMessage());
				//logImportacion.anadirLinea("Error cerrando fichero de lectura: " + e.getMessage());
			}
			log.info("Fichero guardado correctamente, lineas guardadas: " + numLineas);
			//logImportacion.anadirLinea("Fichero guardado correctamente, lineas guardadas: " + numLineas);
		}
		
		// Creamos la vista para ese ambito
		log.info("Creamos la vista para ese ambito");
		
		String queryParaVistaDeAmbito = " CREATE OR REPLACE VIEW planeamiento.ambito_"+idAmbito+" AS "
										+" SELECT entidades_capa.identidad, entidades_capa.claveentidadbase, entidades_capa.identidadbase, entidades_capa.etiqueta, entidades_capa.entidad, entidades_capa.clave, entidades_capa.codigofip AS tramite, entidades_capa.idtipotramite, entidades_capa.idtramite, entidades_capa.idgrupo, entidades_capa.grupo, entidades_capa.geometria AS geom "
										+" FROM planeamiento.entidades_capa entidades_capa "
										+" WHERE entidades_capa.idambito = "+idAmbito;
		em.createNativeQuery(queryParaVistaDeAmbito).executeUpdate();
		
		String queryParaVistaDeAmbitoSinCapa = " CREATE OR REPLACE VIEW planeamiento.ambito_sincapa_"+idAmbito+" AS "
										+ " SELECT entsincapa.iden, entsincapa.idtramite, entsincapa.nombre, entsincapa.clave, entsincapa.geometria "
										+  " FROM planeamiento.tramite tram, planeamiento.plan pl, diccionario.ambito amb, planeamiento.entidades_sincapa entsincapa "
										+ " WHERE amb.iden = "+idAmbito+" AND pl.iden = tram.idplan AND amb.iden = pl.idambito AND tram.iden = entsincapa.idtramite";

		em.createNativeQuery(queryParaVistaDeAmbitoSinCapa).executeUpdate();
	}
}

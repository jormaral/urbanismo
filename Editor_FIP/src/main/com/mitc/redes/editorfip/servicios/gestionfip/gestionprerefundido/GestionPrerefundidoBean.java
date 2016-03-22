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

package com.mitc.redes.editorfip.servicios.gestionfip.gestionprerefundido;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.international.StatusMessage.Severity;
import org.jboss.seam.international.StatusMessages;
import org.jboss.seam.log.Log;

import com.mitc.redes.editorfip.entidades.comunes.ParIdentificadorTexto;
import com.mitc.redes.editorfip.entidades.rpm.gestionfip.Prerefundido;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Tramite;
import com.mitc.redes.editorfip.excepciones.EditorFIPException;
import com.mitc.redes.editorfip.servicios.gestionfip.gestionprerefundidoconsola.GestionPrerefundidoAsincrono;
import com.mitc.redes.editorfip.servicios.sesion.VariablesSesionUsuario;

import es.mitc.redes.urbanismoenred.data.rpm.refundido.Archivo;

@Stateful
@Scope(ScopeType.SESSION)
@Name("gestionPrerefundido")
public class GestionPrerefundidoBean implements GestionPrerefundido
{
   
	@Logger private Log log;

	@In(create = true, required = false)
    ServicioBasicoPrerefundido servicioBasicoPrerefundido;
	
	@In(create = true, required = false)
    Refundido refundido;
	
	@In(create = true, required = false)
	ImportadorPrerefundido importadorPrerefundido;
	
	@In
	VariablesSesionUsuario variablesSesionUsuario;
	
	@In (create = true, required = false)
    GestionPrerefundidoAsincrono gestionPrerefundidoAsincrono;
	
	@PersistenceContext
	EntityManager em;
	
    @In StatusMessages statusMessages;
    
  
    private int idTipoOperacionPlan = 0;

	@Override
	public void borrarPrerefundido(int idPrerefundido) {
		
		log.debug("[borrarPrerefundido] idPrerefundido="+idPrerefundido);
		
		try {
			Prerefundido preref = servicioBasicoPrerefundido.obtenerPrerefundido(idPrerefundido);			
			em.remove(preref);
			
			statusMessages.addFromResourceBundle(Severity.INFO, "Se ha borrado el prerefundido correctamente", null);
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
			statusMessages.addFromResourceBundle(Severity.ERROR, "Se han producido problemas al borrar el prerefundido y no se ha podido llevar a cabo", null);
			
		}
		
		
		log.debug("[borrarPrerefundido] Fin");
		
	}
	
	public String rutaFip (int idproceso)
	{
		String resultado = "";
		
		log.info("[rutaFip] idproceso="+idproceso);
		
		String query = "from es.mitc.redes.urbanismoenred.data.rpm.refundido.Archivo arc where arc.tipo = 'FIP' and arc.proceso = "+idproceso;
		
		try
		{
			Archivo arch = (Archivo)em.createQuery(query).getSingleResult();
			resultado = arch.getContenido();
			
		}
		catch (Exception e)
		{
			log.error("Error rutaLog");
			e.printStackTrace();
		}
		
		
		return resultado;
		
	}
	
 private void unzip(String strZipFile) {
         
         try
         {
                 /*
                  * STEP 1 : Create directory with the name of the zip file
                  *
                  * For e.g. if we are going to extract c:/demo.zip create c:/demo
                  * directory where we can extract all the zip entries
                  *
                  */
                 File fSourceZip = new File(strZipFile);
                 String zipPath = strZipFile.substring(0, strZipFile.length()-4);
                 File temp = new File(zipPath);
                 temp.mkdir();
                 log.info(zipPath + " created");
                
                 /*
                  * STEP 2 : Extract entries while creating required
                  * sub-directories
                  *
                  */
                 ZipFile zipFile = new ZipFile(fSourceZip);
                 Enumeration e = zipFile.entries();
                
                 while(e.hasMoreElements())
                 {
                         ZipEntry entry = (ZipEntry)e.nextElement();
                         File destinationFilePath = new File(zipPath,entry.getName());

                         //create directories if required.
                         destinationFilePath.getParentFile().mkdirs();
                        
                         //if the entry is directory, leave it. Otherwise extract it.
                         if(entry.isDirectory())
                         {
                                 continue;
                         }
                         else
                         {
                                 log.info("Extracting " + destinationFilePath);
                                
                                 /*
                                  * Get the InputStream for current entry
                                  * of the zip file using
                                  *
                                  * InputStream getInputStream(Entry entry) method.
                                  */
                                 BufferedInputStream bis = new BufferedInputStream(zipFile.getInputStream(entry));
                                                                                                                
                                 int b;
                                 byte buffer[] = new byte[1024];

                                 /*
                                  * read the current entry from the zip file, extract it
                                  * and write the extracted file.
                                  */
                                 FileOutputStream fos = new FileOutputStream(destinationFilePath);
                                 BufferedOutputStream bos = new BufferedOutputStream(fos,
                                                                 1024);

                                 while ((b = bis.read(buffer, 0, 1024)) != -1) {
                                                 bos.write(buffer, 0, b);
                                 }
                                
                                 //flush the output stream and close it.
                                 bos.flush();
                                 bos.close();
                                
                                 //close the input stream.
                                 bis.close();
                         }
                 }
         }
         catch(IOException ioe)
         {
                 log.error("IOError :" + ioe);
         }
        
 }
 
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void consolidarPrerefundido(int idPrerefundido) {
		
		log.debug("[consolidarPrerefundido] Inicio");
		
		Prerefundido preref = servicioBasicoPrerefundido.obtenerPrerefundido(idPrerefundido);
		//TODO Llamar a la funcion del bauti
		String rutaZIP = rutaFip(preref.getIdprocesorefundido());
		
		log.info("[consolidarPrerefundido] rutaZIP ="+rutaZIP);
		// Descomprimimos ZIP
		unzip(rutaZIP);
		
		String rutaFIP = rutaZIP.substring(0, rutaZIP.length()-4)+ File.separator + "fip.xml";
		
		log.info("[consolidarPrerefundido] Llamar a Consolidar. idTramite="+preref.getIdTramitePrerefundido()+" ruta="+rutaFIP);
		try {
			int idTramitePreref = importadorPrerefundido.importar(rutaFIP, preref.getIdTramitePrerefundido());
			log.debug("[consolidarPrerefundido] Consolidado OK");
			
			//Pongo el consolidado a true
			boolean r = servicioBasicoPrerefundido.ponerConsolidadoATrue(idPrerefundido);
			
			if (r)
			{
				if (idTramitePreref==0)
				{
					// Voy a hacer un apano de poner bien el tramite del prerefundido porque al consolidr crea uno nuevo
					int idTramitePrerefundidoOLD = preref.getIdTramitePrerefundido();
					
					Tramite tramPreOLD = em.find (Tramite.class,idTramitePrerefundidoOLD);
					
					// Selecciono por orden de mayor a menor iden los tramites que tengan el mismo codigo fip que el que se ha creado de prerefundido
					String queryConsultaUltimoTramitePorPlanPreref = "select tr from Tramite tr where tr.codigofip='"+tramPreOLD.getCodigofip()+"' order by tr.iden DESC";
					
					List<Tramite> listaTram = em.createQuery(queryConsultaUltimoTramitePorPlanPreref).getResultList();
					
					// Cojo el primero
					preref.setIdTramitePrerefundido(listaTram.get(0).getIden());
					
					em.merge(preref);
				}
				else
				{
					preref.setIdTramitePrerefundido(idTramitePreref);					
					em.merge(preref);
				}
				
				
				
								
				statusMessages.addFromResourceBundle(Severity.INFO, "Consolidado el prerefundido correctamente", null);
			}
			else
			{
				statusMessages.addFromResourceBundle(Severity.ERROR, "Error al consolidar el prerefundido, al poner true el consolidado en BD", null);
			}
			
		} catch (EditorFIPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			statusMessages.addFromResourceBundle(Severity.ERROR, "Error al consolidar el prerefundido", null);
		}
		log.debug("[consolidarPrerefundido] Fin");
		
	}



	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void guardarPrerefundido() {
		
		log.debug("[guardarPrerefundido] Inicio");
		
		int idTramiteEncargado = variablesSesionUsuario.getIdTramiteEncargadoTrabajo();
		int idTramiteVigente = variablesSesionUsuario.getIdTramiteVigenteTrabajo();
		int idTramiteBase = variablesSesionUsuario.getIdTramiteBaseTrabajo();
		
		// Inicialmente tengo que comprobar si en la tabla operacionplan estan esos planes y con que operacion, 
		// y borrarlo en el caso que existan y meter la nueva operacion de prerefundido
		int res = servicioBasicoPrerefundido.operacionesPlanPreviaPrerefundido(idTramiteEncargado, idTramiteVigente,idTramiteBase, idTipoOperacionPlan);
		
		if (res>0)
		{
			// Hago el prerefundido y obtengo el idTramitePrerefundido
			
			// La lista es el idTramiteVigente y el idTramiteEncargado
			List<Integer> listaTramRef = new ArrayList<Integer>();
	    	listaTramRef.add(new Integer(idTramiteEncargado));
	    	listaTramRef.add(new Integer(idTramiteVigente));
			
			try {
				
				// Ejecuto antes el script para quitar las constrains
				String queryQuitarConstraints = "select gestionfip.\"borrarConstraintPrerefundido\"() ";
				try
				{
					em.createNativeQuery(queryQuitarConstraints).getSingleResult();
					log.debug("[guardarPrerefundido] Ejecuto antes el script para quitar las constrains OK");
				}
				catch (Exception e) {
					log.error("[guardarPrerefundido] Problema con la constrains, pero se puede continuar");
				}
				
				
				// Creo el Registro de Prerefundido
				// Guardo el registro en la BD
				int idRegistroPreref = servicioBasicoPrerefundido.crearPrerefundido(-1, idTramiteEncargado, idTramiteVigente, idTipoOperacionPlan, "");
				
				
				String listaTramitesARefundir = ""+idTramiteEncargado+","+idTramiteVigente;
				gestionPrerefundidoAsincrono.refundirTramiteAsincrono(listaTramitesARefundir,idRegistroPreref);
				
				log.debug("[guardarPrerefundido] Prerefundido generandose OK");
				statusMessages.addFromResourceBundle(Severity.INFO, "Se ha procedido a generar el prerefundido. Este proceso puede tardar varios minutos. ", null);
				statusMessages.addFromResourceBundle(Severity.WARN, "Vaya a GESTION FIP -> PROCESO PRE-REFUNDIDO para comprobar el estado del prerefundido ", null);
				
				
				
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				// Ha habido un error en la generacion del prerefundido, lo informo en pantalla
				statusMessages.addFromResourceBundle(Severity.ERROR, "Error al generar el prerefundido. Excepcion Grave. Consulte el log del administrador", null);
			}
		}
		
		else
		{
			// Ha habido un error en la generacion del prerefundido, lo informo en pantalla
			if (res==0)
			statusMessages.addFromResourceBundle(Severity.ERROR, "Error al generar el prerefundido. No se ha podido crear la operacion entre plan vigente y plan encargado", null);
			if (res==-1)
				statusMessages.addFromResourceBundle(Severity.ERROR, "Error: Esa operacion no esta implementada para el diccionario del refundido", null);

		}
		
		
		
		
	}
	
	
/*
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void guardarPrerefundido() {
		
		log.debug("[guardarPrerefundido] Inicio");
		
		int idTramiteEncargado = variablesSesionUsuario.getIdTramiteEncargadoTrabajo();
		int idTramiteVigente = variablesSesionUsuario.getIdTramiteVigenteTrabajo();
		int idTramiteBase = variablesSesionUsuario.getIdTramiteBaseTrabajo();
		
		// Inicialmente tengo que comprobar si en la tabla operacionplan estan esos planes y con que operacion, 
		// y borrarlo en el caso que existan y meter la nueva operacion de prerefundido
		int res = servicioBasicoPrerefundido.operacionesPlanPreviaPrerefundido(idTramiteEncargado, idTramiteVigente, idTipoOperacionPlan);
		
		if (res>0)
		{
			// Hago el prerefundido y obtengo el idTramitePrerefundido
			
			// La lista es el idTramiteVigente y el idTramiteEncargado
			List<Integer> listaTramRef = new ArrayList<Integer>();
	    	listaTramRef.add(new Integer(idTramiteEncargado));
	    	listaTramRef.add(new Integer(idTramiteVigente));
			
			try {
				ParIdentificadorTexto idTramitePrerefundidoYRuta = refundido.refundirTramites(listaTramRef,"fran",true);
				
				int idTramitePrerefundido = idTramitePrerefundidoYRuta.getIdBaseDatos();
				String ruta = idTramitePrerefundidoYRuta.getTexto();
				
				if (idTramitePrerefundido!=0)
				{
					// Es correcto, se ha generado bien el prerefundido
					
					// Guardo el registro en la BD
					servicioBasicoPrerefundido.crearPrerefundido(idTramitePrerefundido, idTramiteEncargado, idTramiteVigente, idTipoOperacionPlan, ruta);
					
					log.debug("[guardarPrerefundido] Prerefundido OK");
					statusMessages.addFromResourceBundle(Severity.INFO, "Generar el prerefundido correctamente", null);
				}
				else
				{
					// Ha habido un error en la generacion del prerefundido, lo informo en pantalla
					statusMessages.addFromResourceBundle(Severity.ERROR, "Error al generar el prerefundido", null);
				}
				
				
				
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				// Ha habido un error en la generacion del prerefundido, lo informo en pantalla
				statusMessages.addFromResourceBundle(Severity.ERROR, "Error al generar el prerefundido. Excepcion Grave. Consulte el log del administrador", null);
			}
		}
		
		else
		{
			// Ha habido un error en la generacion del prerefundido, lo informo en pantalla
			if (res==0)
			statusMessages.addFromResourceBundle(Severity.ERROR, "Error al generar el prerefundido. No se ha podido crear la operacion entre plan vigente y plan encargado", null);
			if (res==-1)
				statusMessages.addFromResourceBundle(Severity.ERROR, "Error: Esa operacion no esta implementada para el diccionario del refundido", null);

		}
		
		
		
		
	}

	
	*/

	public int getIdTipoOperacionPlan() {
		return idTipoOperacionPlan;
	}



	public void setIdTipoOperacionPlan(int idTipoOperacionPlan) {
		this.idTipoOperacionPlan = idTipoOperacionPlan;
	}



	@Remove
	public void destroy() {
	}
	
	

}

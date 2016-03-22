/*
* Copyright 2011 red.es
* Autores: Arnaiz Consultores.
*
* Licencia con arreglo a la EUPL, Versi�n 1.1 o -en cuanto
** sean aprobadas por la Comision Europea- versiones
** posteriores de la EUPL (la <<Licencia>>);
** Solo podra usarse esta obra si se respeta la Licencia.
* Puede obtenerse una copia de la Licencia en:
*
* http://ec.europa.eu/idabc/eupl5
*
** Salvo cuando lo exija la legislacion aplicable o se acuerde.
* por escrito, el programa distribuido con arreglo a la
** Licencia se distribuye <<TAL CUAL>>,
** SIN GARANTIAS NI CONDICIONES DE NINGUN TIPO, ni expresas
** ni implicitas.
** Vease la Licencia en el idioma concreto que rige
* los permisos y limitaciones que establece la Licencia.
*/
/*


 */

package com.mitc.redes.editorfip.servicios.gestionfip.gestionprerefundido;


import java.io.File;
import java.io.FileWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.log.Log;
import org.w3c.dom.Document;

import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Plan;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Tramite;
import com.mitc.redes.editorfip.utilidades.EncriptacionCodigoTramite;
import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;


@Stateless
@Name("clsExportacion")
public class ClsExportacionBean implements ClsExportacion {
    
	
	@Logger private Log mLog;
	
	 @PersistenceContext
	EntityManager gEM;
	 
	

   @Resource
   SessionContext sc;
   
   @In (create = true, required = false)
	ConvertirFIP2 convertirFIP2;
   
   @In (create = true, required = false)
	FipFacade fipFacade;
   
	
    public static String rutaFip;

    public String exportarFIP(int idTramite){
        Tramite iTramite;
        String dir;
        String rutaXML = "";
        String texto;
        Document documento;
        String codigoFIPencriptado="";
        String s;
        String codigoPlan;
        String iteracion;
        Plan iPlan;
        int idPlan;
        List lista=new ArrayList();
        Iterator it;
        Object obj[];

        try{
            mLog.info("");
            mLog.info("Exportando a FIP...");

            mLog.info("    Leyendo trámite refundido...");
            iTramite=(Tramite) ClsConexion.dameEntity(Tramite.class, idTramite);
            iTramite=fipFacade.findComplete(idTramite);
            
            // Averigua el nuevo código de plan que se va a asignar al refundido.
            codigoPlan=ClsMain.maximoCodigoPlan(1);
            ClsDatos.gCodigoPlanRefundido=codigoPlan;
            
            // Actualiza el campo "comentario" del trámite con los datos del refundido.
            //  --> Plan creado:
            // TODO FGA: Comentado porque da algun fallo y no es necesario
            //ClsMain.actualizarComentarioTramite(iTramite.getIden());
            
            // Después de actualizar el campo 'Comentario', se actualizan los datos
            //  del plan refundido para poder exportar a FIP con los nuevos datos.
            idPlan=ClsMain.idPlanPorIdTramite(iTramite.getIden());
            s="Update planeamiento.Plan Set codigo='" + codigoPlan + "' " +
                "Where iden=" + idPlan + " ";
            ClsConexion.ejecutar(s);
            
            // Asigna la iteración 01, ya que se trata de un nuevo plan con un único trámite.
            iteracion="01";
            s="Update planeamiento.tramite Set iteracion='" + iteracion + "' " +
                "Where iden=" + iTramite.getIden();
            ClsConexion.ejecutar(s);

            // Cambia el tipo de trámite a Refundido (11)
            s="Update planeamiento.tramite Set idtipotramite=" + ClsDatos.ID_TIPOTRAMITE_REFUNDIDO + " " +
                "Where iden=" + iTramite.getIden();
            ClsConexion.ejecutar(s);

            //  Calcula un código FIP nuevo en función del ámbito, plan, tipo de trámite, e iteración.
            iTramite=(Tramite) ClsConexion.dameEntity(Tramite.class, idTramite);
            ClsDatos.gCodigoFIPrefundido=ClsMain.codigoTramite(iTramite);
            mLog.info("        Código de Trámite sin encriptar: [" + ClsDatos.gCodigoFIPrefundido + "]");

            // Encripta el código FIP calculado
            
            codigoFIPencriptado=EncriptacionCodigoTramite.getEncoded(ClsDatos.gCodigoFIPrefundido).toUpperCase();
            
            mLog.info("        Código de Trámite encriptado   : [" + codigoFIPencriptado + "]");

            if (ClsDatos.B_USAR_HIBERNATE==true){
                iTramite.setCodigofip(codigoFIPencriptado);
                gEM.flush();
            } else {
                s="Update planeamiento.tramite " +
                    "Set codigofip='" + codigoFIPencriptado + "' " +
                    "Where iden=" + iTramite.getIden();
                if (ClsConexion.ejecutar(s)!=1){
                    mLog.warn("Error al actualizar el código FIP. Código 23063." );
                }
            }
            iTramite=(Tramite) ClsConexion.dameEntity(Tramite.class, idTramite);
            mLog.info("    Trámite refundido: código=[" + codigoFIPencriptado + "], Plan [" + codigoPlan + "]");

            //TODO FGA: Comentado por obsoleto
            /*
            // Establece la ruta donde se va a copiar el archivo FIP
            dir= System.getenv("REDES_PATH") + File.separator +  "var" + File.separator +"FIPs" + File.separator + "refundido" + File.separator + codigoFIPencriptado + ".xml";
            mLog.info("    Ruta del archivo FIP: " + dir);
            rutaFip = dir;
            */
            
            // TODO FGA
            
            dir = System.getProperty("jboss.home.dir") + File.separator
			+ "var" + File.separator + "FIPs.war" + File.separator
			+ "prerefundido" + File.separator;
            
            //rutaXML =  File.separator + idTramite + File.separator+ "fip" + new Date().getTime() + ".xml";
            rutaXML =  "fip"  + "_" + idTramite + "_" + new Date().getTime() + ".xml";
            
            String fileDir = System.getProperty("jboss.home.dir") + File.separator
			+ "var" + File.separator + "FIPs.war" + File.separator
			+ "prerefundido" + File.separator + rutaXML;
            
            String os = System.getProperty("os.name").toLowerCase();
            mLog.info(" Estamos en: " + os);
            
            if (os.indexOf("win") >= 0) {
    			

    			File directorio = new File(dir);
    			directorio.mkdirs();
    			mLog.info(" Estamos en Windows: Se crearan en: " + dir);
    		} else {

    			
    			File directorio = new File(dir);
    			directorio.mkdirs();

    			mLog.info(" Estamos en Linux: Se crearan en "+dir);
    		}
            

            File Guardar = new File(fileDir);
            
            // FIN DE CAMBIOS FGA
            
            //File Guardar = new File(file); // Del original
            
            if(Guardar !=null){
                FileWriter Guardx=new FileWriter(Guardar);
                mLog.info("    Generando FIP...");
                
                // TODO
                // Comentado FGA: documento=iTramite.FIP2();
                
                documento= convertirFIP2.FIP2(iTramite);

                mLog.info("    Formateando FIP...");
                OutputFormat format=new OutputFormat(documento);
                format.setLineWidth(65);
                format.setIndenting(true);
                format.setIndent(5);
                
                //FGA TODO format.setEncoding(ClsDatos.CODIFICACION);
                
                if (os.indexOf("win") >= 0) {
					format.setEncoding("ISO-8859-1");
				} else {
					if (os.indexOf("mac") >= 0) {
						format.setEncoding("ISO-8859-1");
						
					}
					else
					{
						// linux
						format.setEncoding("UTF-8");
					}
					
					
				}
                
                StringWriter writer=new StringWriter();
                XMLSerializer serializer= new XMLSerializer(writer,format);
                serializer.serialize(documento);
                texto=writer.toString();

                mLog.info("    Guardando FIP en disco...");
                Guardx.write(texto);
                Guardx.close();
                mLog.info("    Archivo FIP guardado.");
            }
        } catch(Exception e){
            mLog.error("Error al exportar a FIP. " + e + ". Código 23037." );
            e.printStackTrace();
            return "";
        }

        return codigoFIPencriptado+"%"+rutaXML;
    }

}

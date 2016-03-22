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

package com.mitc.redes.editorfip.servicios.informacionfip.documentonormas;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRXmlDataSource;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.async.Asynchronous;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.international.StatusMessage.Severity;
import org.jboss.seam.log.Log;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.mitc.redes.editorfip.entidades.fipxml.RegulacionEspecifica;
import com.mitc.redes.editorfip.entidades.rpm.gestionfip.DocumentosNormativaGenerados;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Casoentidaddeterminacion;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Entidad;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Entidaddeterminacion;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Entidaddeterminacionregimen;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Regimenespecifico;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Tramite;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Vinculocaso;
import com.mitc.redes.editorfip.servicios.informacionfip.consultafichaurbanisticas.ServicioBasicoFichaUrbanistica;
import com.mitc.redes.editorfip.servicios.informacionfip.consultafichaurbanisticas.XMLws;
import com.mitc.redes.editorfip.utilidades.Textos;

@Stateless
@Name("servicioGestionDocumentoNormas")
public class ServicioGestionDocumentoNormasBean implements ServicioGestionDocumentoNormas
{
    @Logger private Log log;
    
    @PersistenceContext
	EntityManager em;
    
    @In Map<String,String> messages;
    
    @In (create = true)
    FacesMessages facesMessages;
    
   
        
    @In(create = true, required= false)
    ServicioBasicoFichaUrbanistica servicioBasicoFichaUrbanistica;
    
  
    
    // ------------------------------
    // GLOBAL
    // ------------------------------	
    public String fncObtenerStringDoc(Document doc)throws Exception{ 
        TransformerFactory tFactory = TransformerFactory.newInstance(); 
        Transformer transformer = tFactory.newTransformer(); 
        DOMSource source = new DOMSource(doc); 
        StringWriter sw=new StringWriter(); 
        StreamResult result = new StreamResult(sw); 
        transformer.transform(source, result); 
        String xmlString=sw.toString(); 
        return xmlString; 
         
    }  
    
   
    
    @Asynchronous
    public void crearDocumentoNormas(int idTramite, DocumentosNormativaGenerados nuevoDoc)
    {
    	log.info("[crearDocumentoNormas] (Asincrono) idTramite="+idTramite);
    	
    	
    	// Inicio el proceso
    	try
    	{
    		
    		
    		// Genero las determinaciones
    		String rutaDocDeterminaciones = generarDocumentoNormasDeterminacion(idTramite);
    		nuevoDoc.setUrlDocNormativaDeterminacion(rutaDocDeterminaciones);
    		
    		// Genero las entidades
    		String rutaDocEntidades = generarDocumentoNormasEntidad(idTramite);
    		nuevoDoc.setUrlDocNormativaEntidades(rutaDocEntidades);
    		
    		// Pongo el estado a OK
    		nuevoDoc.setEstado("GENERADO");
    		
    	}
    	catch (Exception ex)
    	{
    		ex.printStackTrace();
    		
    		nuevoDoc.setEstado("ERROR");
    		
    		// Alamceno la traza de error en DocumentosNormativaGenerados observaciones
    		StringWriter errors = new StringWriter();
    		ex.printStackTrace(new PrintWriter(errors));
    		nuevoDoc.setObservaciones(errors.toString());
    		
    		// Guardo
    		em.merge(nuevoDoc);
    		
    		facesMessages.addFromResourceBundle(Severity.ERROR, "Error al generar documento normas", null); 
    		
    	}
    	
    	// Persisto el DocumentosNormativaGenerados
    	
    	try
    	{
    		// Guardo
    		em.merge(nuevoDoc);
    		facesMessages.addFromResourceBundle(Severity.INFO, "Ha terminado el proceso de creacion de documento normas", null);
    		
    	}
    	catch (Exception e)
    	{
    		facesMessages.addFromResourceBundle(Severity.ERROR, "Error al guardar documento normas", null); 
    	}
    	
    	
    }
    
    private String rutaGlobal(int idTramite)
    {
    	String resultado ="";
    		
    	String os = System.getProperty("os.name").toLowerCase();
    	
    	String dir = System.getProperty("jboss.home.dir") + File.separator
		+ "var" + File.separator + "FIPs.war" + File.separator
		+ "documentonormas" + File.separator + idTramite + File.separator;
    	
    	if (os.indexOf("win") >= 0) {
			
			File directorio = new File(dir);
			directorio.mkdirs();
			log.debug(" Estamos en Windows: Se crearan en: " + dir);
		} else {

			
			File directorio = new File(dir);
			directorio.mkdirs();

			log.debug(" Estamos en Linux: Se crearan en :"+dir);
		}

    	resultado=dir;
    	
    	return resultado;
    }
    
	public String generarDocumentoNormasDeterminacion(int idTramite) {
		
		log.info("[generarDocumentoNormasDeterminacion] idTramite="+idTramite);
		
		// Obtengo la ruta
		String resultado = "NO GENERADO";
		
		String rutaDir = rutaGlobal(idTramite);
		String nombreArchivo = "docnormadeterminacion"  + "_" + new Date().getTime() + ".pdf";
		String fileDir = rutaDir +nombreArchivo;

		try {
		    
		    Document aXML;
		
		    aXML = consultaTodasDeterminaciones(idTramite);
		    log.debug("[generarDocumentoNormasDeterminacion] consultaDetemrinaciones");
		    
		    if (aXML != null) {
		    	
		    	log.info("[generarDocumentoNormasDeterminacion]Todo el XML que se pasa al Report = \n "+fncObtenerStringDoc(aXML));
		    	
		    	String fileSep = System.getProperty("file.separator");
	            
				String rutaPlantilla = System.getProperty("jboss.home.dir") + fileSep + "var" 
																			+ fileSep + "ficha" 
																			+ fileSep + "PlantillaDocNormaDet.jrxml";
	       
	            JasperReport aReport = JasperCompileManager.compileReport(rutaPlantilla);
	
	            
	            JRXmlDataSource xmlDataSource = new JRXmlDataSource(aXML,"/XML/DETERMINACIONES//DETERMINACION");
	            //JRXmlDataSource xmlDataSource = new JRXmlDataSource(aXML,"/XML/DETERMINACIONES/descendant::*");
	            //log.info("xmlDataSource=\n"+xmlDataSource.subDocument().getNodeName());
	            
	            //JRXmlDataSource xmlDataSource = new JRXmlDataSource(aXML,"XML/DETERMINACIONES//DETERMINACION");
	            
	              
	            
	            
	            /*
	            log.info("[generarDocumentoNormasDeterminacion]aXML getChildNodes= "+ aXML.getChildNodes().getLength());
	            log.info("[generarDocumentoNormasDeterminacion]aXML getFirstChild getChildNodes= "+ aXML.getFirstChild().getChildNodes().getLength());
	            log.info("[generarDocumentoNormasDeterminacion]aXML getFirstChild getFirstChild getChildNodes= "+ aXML.getFirstChild().getFirstChild().getChildNodes().getLength());
				
	            NodeList det = aXML.getFirstChild().getFirstChild().getChildNodes();
	            
	           for (int i =0; i<det.getLength(); i++)
	           {
	        	   log.info("[generarDocumentoNormasDeterminacion]nodoDeterminacion="+det.item(i).getAttributes().getNamedItem("nombre").toString());
	           }
	           */
	           
	            JasperPrint jasperPrint = JasperFillManager.fillReport(aReport,new HashMap() , xmlDataSource);
	
	            byte[] datos = JasperExportManager.exportReportToPdf(jasperPrint);
	            	            

	            try {
	                
		     
		    	    //convert array of bytes into file
		    	    FileOutputStream fileOuputStream =  new FileOutputStream(fileDir); 
		    	    fileOuputStream.write(datos);
		    	    fileOuputStream.close();
		     
	            }catch(Exception e){
	                e.printStackTrace();
	            }
	            resultado = fileDir;
	            facesMessages.addFromResourceBundle(Severity.INFO, "Generado documento normas determinaciones", null); 
		         
		    } else {
		    	
		    	
		        log.info("[generarDocumentoNormasDeterminacion] Tramite sin Determinaciones");
		        
		        
		    }
		} catch (Exception e) {
		    e.printStackTrace();
		    log.error("[generarFichaUrbanistica]ERROR= "+e.getMessage());
		    facesMessages.addFromResourceBundle(Severity.ERROR, "ERROR Generando documento normas", null);
		} 
		
		log.info("[generarDocumentoNormasDeterminacion] FIN");
		
		return resultado;

	}

    public String generarDocumentoNormasEntidad(int idTramite){

    	// Obtengo la ruta
		String resultado = "NO GENERADO";
		
		String rutaDir = rutaGlobal(idTramite);
		String nombreArchivo = "docnormaentidad"  + "_" + new Date().getTime() + ".pdf";
		String fileDir = rutaDir +nombreArchivo;	
		
		int i;
		int iEntidad;
		Element aNode;
		Element aNode2;
		Element aNodeDef;
		Element aNodeGrupo;
		Element aNodeCapa;
		Element aNodePlanes;
		NodeList NodosEntidad;
		
		Document aXMLRetorno;
		try {
		
		
		    
		    Document aXML;
		
		    aXMLRetorno = XMLws.generarXMLDOC("FICHA");
		    log.info("[generarDocumentoNormasEntidad] aXMLRetorno");
		    
		    
		
		    aXML = consultaTodasEntidades(idTramite);
		    log.debug("[generarDocumentoNormasEntidad] consultaEntidades");
		    
		    if (aXML != null) {
		        //response.setContentType("application/pdf");
		
		        NodeList aNodeList;
		        NodeList aNodeListCapa;
		
		        aNodeList = XMLws.findNode("/XML/ENTIDADES/AMBITO/CAPA/GRUPO", aXML.getDocumentElement());
		
		        if (aNodeList == null || aNodeList.getLength() == 0) {
		            
		        	
		            
		            log.info("[generarDocumentoNormasEntidad] fichacu_sindatos.txt");
		       		            
		            
		        } else {
		            String situacion="X=1111 Y= 2222";
		            
		            aNode=this.AddNode(aXMLRetorno.getDocumentElement(), "SITUACION");
		            log.debug("[generarDocumentoNormasEntidad] Situacion Establecida");
		            
		            aNode.setTextContent(situacion);
		            aNode = null;
		
		            // TODO FGA Comento lo siguiente porque no quiero que salga nada de Planeamiento Vigente
		            //aNodePlanes = this.AddNode(aXMLRetorno.getDocumentElement(), "PLANEAMIENTO") ;//aXMLRetorno.createElement("PLANEAMIENTO");
		            //fillPlaneamientoVigente(aNodePlanes,geoConsulta);
		            // // FGA: Comentado anteriormente a lo anterior   aXMLRetorno.getDocumentElement().appendChild(aNodePlanes);
		            
		            aNodeListCapa = XMLws.findNode("/XML/ENTIDADES/AMBITO/CAPA", aXML.getDocumentElement());
		
		            aNodeDef = aXMLRetorno.createElement("DEFINICIONES");
		            aXMLRetorno.getDocumentElement().appendChild(aNodeDef);
		            for (int iCapa = 0; iCapa < aNodeListCapa.getLength(); iCapa++) {
		                aNodeCapa = aXMLRetorno.createElement("CAPA");
		                aNodeCapa.setAttribute("nombre", ((Element) aNodeListCapa.item(iCapa)).getAttribute("nombre"));
		                aNodeCapa.setAttribute("orden", ((Element) aNodeListCapa.item(iCapa)).getAttribute("orden"));
		                aXMLRetorno.getDocumentElement().appendChild(aNodeCapa);
		
		                aNodeGrupo = (Element) aNodeListCapa.item(iCapa);
		                aNodeList = XMLws.findNode("GRUPO", aNodeGrupo);
		                for (i = 0; i < aNodeList.getLength(); i++) {
		                    aNode2 = (Element) aNodeList.item(i);
		
		                    aNode = AddNode(aNodeCapa,"GRUPO");
		                    aNode.setAttribute("nombre", aNode2.getAttribute("nombre"));
		
		                    NodosEntidad = aNode2.getChildNodes();
		
		                    for (iEntidad = 0; iEntidad < NodosEntidad.getLength(); iEntidad++) {
		                        fillEntidad((Element) NodosEntidad.item(iEntidad), aNode, aNodeDef);
		                    }
		//                    aNodeCapa.appendChild(aNode);
		
		                    aNode = null;
		                }
		            }
		
		            aNode = null;
		            
		            String fileSep = System.getProperty("file.separator");
		            
					String rutaPlantilla = System.getProperty("jboss.home.dir") + fileSep + "var" 
																				+ fileSep + "ficha" 
																				+ fileSep + "PlantillaDocNorma.jrxml";
		       
		            JasperReport aReport = JasperCompileManager.compileReport(rutaPlantilla);
		
		            JRXmlDataSource xmlDataSource = new JRXmlDataSource(aXMLRetorno,"FICHA/CAPA/GRUPO/ENTIDADES/ENTIDAD/CONDICIONES/CONDICION/CASOS/CASO/VALORES/VALOR/REGIMENES-ESPECIFICOS/REGIMEN-ESPECIFICO"); //"/FICHA/DEFINICIONES/DEFINICION"
		
		            HashMap hashMap = new HashMap();
		            hashMap.put("SRS", Textos.getCadena("urbrWS", "SRS_Datos"));
		            
		            // FGA Comentado hashMap.put("ID_AMBITO", Integer.parseInt(request.getParameter("idAmbito"))); 
		            JasperPrint jasperPrint = JasperFillManager.fillReport(aReport, hashMap, xmlDataSource);
		
		            byte[] datos = JasperExportManager.exportReportToPdf(jasperPrint);
		            
		            
    	            

		            try {
		                
			     
			    	    //convert array of bytes into file
			    	    FileOutputStream fileOuputStream =  new FileOutputStream(fileDir); 
			    	    fileOuputStream.write(datos);
			    	    fileOuputStream.close();
			     
		            }catch(Exception e){
		                e.printStackTrace();
		            }
		            resultado = fileDir;
		            
		           
		            
		            facesMessages.addFromResourceBundle(Severity.INFO, "Generado documento normas entidades", null);
		            
		           
		         }
		    } else {
		    	
		    	
		        log.info("[generarDocumentoNormasEntidad] Tramite sin Entidades");
		        
		       
		    }
		} catch (Exception e) {
		    e.printStackTrace();
		    log.error("[generarDocumentoNormasEntidad]ERROR= "+e.getMessage());
		    facesMessages.addFromResourceBundle(Severity.ERROR, "ERROR Generando documento normas", null);
		} 
		
		log.info("[generarDocumentoNormasEntidad] FIN");
		return resultado;
    }

	private Document consultaTodasEntidades(int idTramite) {

        try
        {
            Document aXML;
            

            aXML=XMLws.generarXMLDOC("XML");
            aXML.getDocumentElement().appendChild(servicioBasicoFichaUrbanistica.getEntidadesTodas(idTramite,aXML));
            return aXML;
        }catch (Exception e){
        	e.printStackTrace();
		    log.error("[consultaTodasEntidades]ERROR= "+e.getMessage());
            return null;
        }


    }
	
	private Document consultaTodasDeterminaciones(int idTramite) {

        try
        {
            Document aXML;
            

            aXML=XMLws.generarXMLDOC("XML");
            aXML.getDocumentElement().appendChild(servicioBasicoFichaUrbanistica.getDeterminacionesTodas(idTramite,aXML));
            return aXML;
        }catch (Exception e){
        	e.printStackTrace();
		    log.error("[consultaTodasEntidades]ERROR= "+e.getMessage());
            return null;
        }


    }
    
    private void fillEntidad(Element aNode, Element aNodeGrupo, Element aNodeDefiniciones) {
        try {
            Entidad aEntidad;
            Set<Entidaddeterminacion> aConds;
            int iCaso;
            String aTexto;
            int idCaracterGrupoEntidad = 20;
            Element aNodeCondiciones;
            Element aNodeCondicion;
            Element aNodeCasos;
            Element aNodeCaso;
            Element aNodeValores;
            Element aNodeValor;
            Element aNodeRegEsps;
            Element aNodeRegEsp;
            Element aNodeEntidades;
            Element aNodeEntidad;
            Element aNodeDefinicion;
            Element aNodeAdscripciones;
            Element aNodeAdscripcion;
            NodeList aLista;
            String aTipoDet;
            int aOrdenTipoDet;
            String[] valores;

//            aNodeEntidades = aXMLRetorno.createElement("ENTIDADES");
            
            aNodeEntidades=AddNode(aNodeGrupo,"ENTIDADES");

            aNodeEntidad = AddNode(aNodeEntidades,"ENTIDAD");
            aNodeEntidad.setAttribute("clave", aNode.getAttribute("clave"));
            aNodeEntidad.setAttribute("nombre", aNode.getAttribute("nombre"));

//            aNodeEntidades.appendChild(aNodeEntidad);
//            aNodeGrupo.appendChild(aNodeEntidades);

            
            aEntidad = servicioBasicoFichaUrbanistica.findForFicha(Integer.valueOf(aNode.getAttribute("id")));
            aConds = aEntidad.getEntidaddeterminacions();

            aNodeEntidad.setAttribute("orden", String.valueOf(aEntidad.getOrden()));

            idCaracterGrupoEntidad = Integer.valueOf(Textos.getCadena("urbrWS", "IdCaracterGrupoEntidades")).intValue();

            if (aConds.size() > 1) {
//                aNodeCondiciones = aXMLRetorno.createElement("CONDICIONES");
//                aNodeEntidad.appendChild(aNodeCondiciones);
                aNodeCondiciones=AddNode(aNodeEntidad,"CONDICIONES");
                for (Entidaddeterminacion aCond : aConds) {
                    if (aCond.getDeterminacion().getIdcaracter() != idCaracterGrupoEntidad) {
                        //Definición de la determinación
                        aLista = XMLws.findNode("DEFINICION[@id=\"" + aCond.getDeterminacion().getIden() + "\"]", aNodeDefiniciones);
                        if (aLista == null || aLista.getLength() == 0) {
                            aNodeDefinicion=this.AddNode(aNodeDefiniciones, "DEFINICION");
                            aNodeDefinicion.setAttribute("id", String.valueOf(aCond.getDeterminacion().getIden()));
                            aNodeDefinicion.setAttribute("nombre", aCond.getDeterminacion().getNombre());
                            aNodeDefinicion.setAttribute("apartado", aCond.getDeterminacion().getApartadoCompleto());
                            aNodeDefinicion.setAttribute("orden", String.valueOf(aCond.getDeterminacion().getOrden()));
                        } else {
                            aNodeDefinicion = (Element) aLista.item(0);
                        }
                        String definicion = "";
                        if (aCond.getDeterminacion().getTexto() != null && !aCond.getDeterminacion().getTexto().equals("")) {
                            definicion = definicion + aCond.getDeterminacion().getTexto() + "\n";
                        }
                        Set<RegulacionEspecifica> regulaciones = servicioBasicoFichaUrbanistica.getNodosRegulacionEspecificaDeDet(aCond.getDeterminacion());
                        if (regulaciones != null) {
                            for (RegulacionEspecifica regesp : regulaciones) {
                                definicion = definicion + regesp.getNombre() + " :  " + regesp.getTexto() + "\n";
                            }
                        }
                        aNodeDefinicion.setTextContent(definicion);
                        aNodeCondicion=this.AddNode(aNodeCondiciones, "CONDICION");
                        aNodeCondicion.setAttribute("determinacion", aCond.getDeterminacion().getNombre());
                        aNodeCondicion.setAttribute("iddeterminacion", String.valueOf(aCond.getDeterminacion().getIden()));
                        aNodeCondicion.setAttribute("apartado", aCond.getDeterminacion().getApartadoCompleto());
                        aNodeCondicion.setAttribute("ordendeterminacion", String.valueOf(aCond.getDeterminacion().getOrden()));

                        valores = Textos.getCadena("consola", "caracterregimendirecto").split(",");
                        aTipoDet = "";
                        aOrdenTipoDet = 0;
                        for (String valor : valores) {
                            if (aCond.getDeterminacion().getIdcaracter() == Integer.valueOf(valor)) {
                                aTipoDet = "RD";
                                aOrdenTipoDet = 1;
                                break;
                            }
                        }
                        if (aTipoDet.equals("")) {
                            valores = Textos.getCadena("consola", "caracteruso").split(",");
                            for (String valor : valores) {
                                if (aCond.getDeterminacion().getIdcaracter() == Integer.valueOf(valor)) {
                                    aTipoDet = "USO";
                                    aOrdenTipoDet = 2;
                                    break;
                                }
                            }
                        }
                        if (aTipoDet.equals("")) {
                            valores = Textos.getCadena("consola", "caracteracto").split(",");
                            for (String valor : valores) {
                                if (aCond.getDeterminacion().getIdcaracter() == Integer.valueOf(valor)) {
                                    aTipoDet = "ACTO";
                                    aOrdenTipoDet = 3;
                                    break;
                                }
                            }
                        }
                        //if (aCond.getDeterminacion().getIdcaracter()== Textos.getCadena("consola",
                        
                        aNodeCondicion.setAttribute("tipo", aTipoDet);
                        aNodeCondicion.setAttribute("orden", String.valueOf(aOrdenTipoDet));
                        

                        iCaso = 0;
                        aNodeCasos=this.AddNode(aNodeCondicion, "CASOS");

                        for (Casoentidaddeterminacion aCaso : aCond.getCasoentidaddeterminacions()) {
                            aNodeCaso =AddNode(aNodeCasos,"CASO");
                            //if (aCond.getCasoentidaddeterminacions().size()>1){
                            if (aCaso.getNombre() != null) {
                                aNodeCaso.setAttribute("nombre", aCaso.getNombre());
                            } else {
                                aNodeCaso.setAttribute("nombre", "Caso " + String.valueOf(iCaso));
                            }
                            iCaso++;
                            //}
                           
                            aNodeValores=this.AddNode(aNodeCaso, "VALORES");
                            for (Entidaddeterminacionregimen aReg : aCaso.getEntidaddeterminacionregimensForIdcaso()) {
                                //Determinación de Régimen
                                aNodeValor=this.AddNode(aNodeValores, "VALOR");
                                if (aReg.getDeterminacion() != null) {
                                    aTexto = "";
                                    if (aReg.getOpciondeterminacion() != null) {
                                        aTexto = aTexto + aReg.getOpciondeterminacion().getDeterminacionByIddeterminacionvalorref().getNombre();
                                    }
                                    aNodeValor.setAttribute("determinacionRegimen", aReg.getDeterminacion().getNombre());
                                    aNodeValor.setAttribute("valor", aTexto);
                                    aNodeValor.setAttribute("ordenDetRegimen", String.valueOf(aReg.getDeterminacion().getOrden()));
                                } else {
                                    if (aReg.getOpciondeterminacion() == null) {
                                        if (aReg.getValor() != null && !aReg.getValor().equals("")) {
                                        	
                                        	aNodeValor.setAttribute("valor", aReg.getValor()); // FGA: Esto lo tengo que borrar luego 
                                        	// TODO FGA: Por ahora comento lo de las unidades
                                        	
                                        	/*
                                        	List<Map<String, Object>> aUnidades = aFacadeDet.getUnidades(aCond.getDeterminacion().getIden());
                                            if (aUnidades != null && aUnidades.size() > 0) {
                                                aNodeValor.setAttribute("valor", aReg.getValor() + " " + aUnidades.get(0).get("nombre"));
                                            } else {
                                                aNodeValor.setAttribute("valor", aReg.getValor());
                                            }
                                            */
                                        }
                                    } else {
                                        aNodeValor.setAttribute("valor", aReg.getOpciondeterminacion().getDeterminacionByIddeterminacionvalorref().getNombre());
                                    }
                                }
                                //Caso de aplicación
                                if (aReg.getCasoentidaddeterminacionByIdcasoaplicacion() != null) 
                                {
                                    aNodeValor.setAttribute("caso_aplicacion", aReg.getCasoentidaddeterminacionByIdcasoaplicacion().getEntidaddeterminacion().getDeterminacion().getNombre());
                                }
                                //Régimen Específico
                                if (aReg.getRegimenespecificos().size() > 0) {
//                                    aNodeRegEsps = aXMLRetorno.createElement("REGIMENES-ESPECIFICOS");
//                                    aNodeValor.appendChild(aNodeRegEsps);
                                    aNodeRegEsps=AddNode(aNodeValor,"REGIMENES-ESPECIFICOS");
                                    for (Regimenespecifico aRegEsp : aReg.getRegimenespecificos()) {
                                        if (aRegEsp.getRegimenespecifico()==null){ //Solo los padres
                                            aNodeRegEsp=this.AddNode(aNodeRegEsps, "REGIMEN-ESPECIFICO");
                                            aNodeRegEsp.setAttribute("nombre", aRegEsp.getNombre());
                                            aNodeRegEsp.setTextContent(textoRegimenEspecifico(aRegEsp));
                                        }
                                    }
                                } else {
                                    aNodeRegEsps = AddNode(aNodeValor, "REGIMENES-ESPECIFICOS");
                                    aNodeRegEsp = AddNode(aNodeRegEsps, "REGIMEN-ESPECIFICO");
                                }
                            }
                            //Vinculos
                            for (Vinculocaso vinc :aCaso.getVinculocasosForIdcaso()) 
                            {
                                Element aNodeVinculo=this.AddNode(aNodeCaso, "VINCULO");
                                aNodeVinculo.setAttribute("caso_vinculado", vinc.getCasoentidaddeterminacionByIdcasovinculado().getEntidaddeterminacion().getDeterminacion().getNombre());
                            }
                        }
                        aNodeCasos.setAttribute("ncasos", String.valueOf(aNodeCasos.getChildNodes().getLength()));
                    } else {
                        //Logger.getLogger(FichaUrbanistica.class.getName()).log(Level.SEVERE, null, "Grupo");
                    }
                }
            } else {
                aNodeCondiciones = AddNode(aNodeEntidad, "CONDICIONES");
                aNodeCondicion = AddNode(aNodeCondiciones, "CONDICION");
                aNodeCasos = AddNode(aNodeCondicion, "CASOS");
                aNodeCaso = AddNode(aNodeCasos, "CASO");
                aNodeValores = AddNode(aNodeCaso, "VALORES");
                aNodeValor = AddNode(aNodeValores, "VALOR");
                aNodeRegEsps = AddNode(aNodeValor, "REGIMENES-ESPECIFICOS");
                aNodeRegEsp = AddNode(aNodeRegEsps, "REGIMEN-ESPECIFICO");
            }
            NodeList nodeAdscripciones = XMLws.findNode("ADSCRIPCION", aNode);
            aNodeAdscripciones= AddNode(aNodeEntidad, "ADSCRIPCIONES");
            for (int iAds = 0;iAds < nodeAdscripciones.getLength();iAds++){
                Element ads = (Element) nodeAdscripciones.item(iAds);
                aNodeAdscripcion= AddNode(aNodeAdscripciones, "ADSCRIPCION");
                if (ads.getAttribute("tipo")!=null){
                    aNodeAdscripcion.setAttribute("tipo", ads.getAttribute("tipo"));
                }
                if (ads.getAttribute("origen")!=null){
                    aNodeAdscripcion.setAttribute("origen", ads.getAttribute("origen"));
                }
                if (ads.getAttribute("destino")!=null){
                    aNodeAdscripcion.setAttribute("destino", ads.getAttribute("destino"));
                }
                if (ads.getAttribute("cuantia")!=null){
                    aNodeAdscripcion.setAttribute("cuantia", ads.getAttribute("cuantia"));
                }
                if (ads.getAttribute("unidad")!=null){
                    aNodeAdscripcion.setAttribute("unidad", ads.getAttribute("unidad"));
                }
                if (ads.getAttribute("texto")!=null){
                    aNodeAdscripcion.setAttribute("texto", ads.getAttribute("texto"));
                }
            }
        } catch (Exception ex) {
        	log.error("[fillEntidad] Error: "+ex.getCause());
        	ex.printStackTrace();
        }
    }

    private Element AddNode(Element padre, String nombre) {
        Element aNode = padre.getOwnerDocument().createElement(nombre);
        padre.appendChild(aNode);
        return aNode;
    }
    

    
    private String textoRegimenEspecifico(Regimenespecifico aRegEsp) {
        String aRespuesta = "";

        if (aRegEsp != null) {
            //aRespuesta = "- " + aRegEsp.getNombre();
            if (aRegEsp.getTexto()!=null && !aRegEsp.getTexto().equals(""))
            {
                aRespuesta = aRespuesta + aRegEsp.getTexto() ;
            }
            for (Regimenespecifico aHijo : aRegEsp.getRegimenespecificos()) {
                aRespuesta = aRespuesta + "\n" + textoRegimenEspecifico(aHijo);
            }
        }

        return aRespuesta;
    }
    
 
	  
}



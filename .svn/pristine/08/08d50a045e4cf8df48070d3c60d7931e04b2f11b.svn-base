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

package com.mitc.redes.editorfip.servicios.informacionfip.consultafichaurbanisticas;

import java.io.ByteArrayInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.Stateless;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRXmlDataSource;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.international.StatusMessage.Severity;
import org.jboss.seam.log.Log;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.icesoft.faces.context.Resource;
import com.mitc.redes.editorfip.entidades.fipxml.RegulacionEspecifica;
import com.mitc.redes.editorfip.entidades.rpm.gestionfip.PlanesEncargados;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Casoentidaddeterminacion;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Entidad;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Entidaddeterminacion;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Entidaddeterminacionregimen;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Plan;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Regimenespecifico;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Tramite;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Vinculocaso;
import com.mitc.redes.editorfip.servicios.gestionfip.gestionplaneamientoencargado.PlaneamientoEncargadoHome;
import com.mitc.redes.editorfip.servicios.sesion.VariablesSesionUsuario;
import com.mitc.redes.editorfip.utilidades.Textos;
import com.vividsolutions.jts.geom.Geometry;

@Stateless
@Name("servicioGestionFichaUrbanistica")
public class ServicioGestionFichaUrbanisticaBean implements ServicioGestionFichaUrbanistica
{
    @Logger private Log log;
    
    @PersistenceContext
	EntityManager em;
    
    @In Map<String,String> messages;
    
    @In (create = true)
    FacesMessages facesMessages;
    
    @In(value="#{facesContext.externalContext}")
	private ExternalContext extCtx;
    
    @In(value="#{facesContext}")
	FacesContext facesContext;
    
    @In(create = true, required= false)
    ServicioBasicoFichaUrbanistica servicioBasicoFichaUrbanistica;
    
    @In(create = true, required= false)
    VariablesSesionUsuario variablesSesionUsuario;
    
    private String X;
    private String Y;
    private String SRS;
    
    private Resource pdfFichaGenerada;
    private boolean fichaGenerada = false;
    
    // ------------------------------
    // GLOBAL
    // ------------------------------	

   
	public void generarFichaUrbanistica(String X, String Y, String SRS) {
    	log.info("[generarFichaUrbanistica] Peticion de ficha urbanistica. X="+X+ " Y="+Y+" SRS="+SRS);
    	
    	this.X=X;
    	this.Y=Y;
    	this.SRS=SRS;
    	
    	generarFichaUrbanistica();
    	
	}  
    
   
	public void generarFichaUrbanistica(String X, String Y) {
    	log.info("[generarFichaUrbanistica] Peticion de ficha urbanistica. X="+X+ " Y="+Y);
    	
    	this.X=X;
    	this.Y=Y;
    	
    	obtenerSRS();
    	
    	
    	generarFichaUrbanistica();
    	
	}  
	
	private void obtenerSRS()
	{
		// Obtengo el SRS
    	
    	String SRSObtenido="";
    	
    	int idTramiteEncargado = variablesSesionUsuario.getIdTramiteEncargadoTrabajo();
    	
    	// Obtengo el PlaneamientoEncargado
    	String queryPlaneamientoEncargado="select pe from PlanesEncargados pe where pe.tramiteEncargado.iden="+idTramiteEncargado;
    	PlanesEncargados planEncargado = (PlanesEncargados)em.createQuery(queryPlaneamientoEncargado).getSingleResult();
    	SRSObtenido = planEncargado.getProyeccion();
    	
    	
    	
    	log.info("[generarFichaUrbanistica] SRSObtenido="+SRSObtenido);
    	this.SRS=SRSObtenido;
	}
    
    public void generarFichaUrbanistica(){

		//response.setContentType("text/html;charset=UTF-8");
		//PrintWriter out = response.getWriter();
		
		//response.setContentType("text/xml");
		
		
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
			
			obtenerSRS();
		
			log.info("[generarFichaUrbanistica] Peticion de ficha urbanistica. X="+X+ " Y="+Y+" SRS="+SRS);
		    
		    Document aXML;
		
		    aXMLRetorno = XMLws.generarXMLDOC("FICHA");
		    log.info("[generarFichaUrbanistica] aXMLRetorno");
		    
		    Geometry geoConsulta = geoUtils.GeoFromWKT("POINT(" + X + " "  + Y + ")");
		    log.info("[generarFichaUrbanistica] geoConsulta");
		    
		    // Comentado por FGA
		    /*
		    if (X!=null && !X.equals("") ){
		        geoConsulta=geoUtils.TransformGeometry(geoConsulta,SRS,Textos.getCadena("urbrWS", "SRS_Datos"));
		    }
		    */
		
		    aXML = consultaEntidades("POINT(" + X + " " + Y + ")", SRS);
		    log.debug("[generarFichaUrbanistica] consultaEntidades");
		    
		    if (aXML != null) {
		        //response.setContentType("application/pdf");
		
		        NodeList aNodeList;
		        NodeList aNodeListCapa;
		
		        aNodeList = XMLws.findNode("/XML/ENTIDADES/AMBITO/CAPA/GRUPO", aXML.getDocumentElement());
		
		        if (aNodeList == null || aNodeList.getLength() == 0) {
		            
		        	/*
		        	String situacion=null;
		            
		            // Comentado FGA situacion = ServicioWeb.getAddress(Double.valueOf(X),Double.valueOf(Y),SRS);
		            if (situacion==null){
		                if ( SRS !=null){
		                    situacion = "X="+ X + " Y=" + Y + "  ( " + SRS + " )";
		                }else{
		                    situacion = "X="+ X + " Y=" + Y;
		                }
		            }
		            aNode=this.AddNode(aXMLRetorno.getDocumentElement(), "SITUACION");
		            log.debug("[generarFichaUrbanistica] Situacion Establecida");
		            
		            aNode.setTextContent(situacion);
		            aNode = null;
		            
		            HashMap hashMap = new HashMap();
		            hashMap.put("SRS", Textos.getCadena("urbrWS", "SRS_Datos"));
		            hashMap.put("BBOX", geoUtils.getWMS_BBOX(geoConsulta));
		            
		            String fileSep = System.getProperty("file.separator");
		            
					String rutaPlantilla = System.getProperty("jboss.home.dir") + fileSep + "var" 
																				+ fileSep + "ficha" 
																				+ fileSep + "PlantillaFicha.jrxml";
		       
		            JasperReport aReport = JasperCompileManager.compileReport(rutaPlantilla);
		            
		            JRXmlDataSource xmlDataSource = new JRXmlDataSource(aXMLRetorno,"FICHA/CAPA/GRUPO/ENTIDADES/ENTIDAD/CONDICIONES/CONDICION/CASOS/CASO/VALORES/VALOR/REGIMENES-ESPECIFICOS/REGIMEN-ESPECIFICO"); //"/FICHA/DEFINICIONES/DEFINICION"
		    		
		            
		            JasperPrint jasperPrint = JasperFillManager.fillReport(aReport, hashMap, xmlDataSource);
		            
		            byte[] datos = JasperExportManager.exportReportToPdf(jasperPrint);
		            
		            pdfFichaGenerada = new PdfFicha("Ficha_CU.pdf", datos);
		            fichaGenerada = true;
		            */
		            
		            log.info("[generarFichaUrbanistica] fichacu_sindatos.txt");
		       		            
		            facesMessages.addFromResourceBundle(Severity.WARN, "fichacu_sindatos", null);
		        } else {
		            String situacion=null;
		            
		            // Comentado FGA situacion = ServicioWeb.getAddress(Double.valueOf(X),Double.valueOf(Y),SRS);
		            if (situacion==null){
		                if ( SRS !=null){
		                    situacion = "X="+ X + " Y=" + Y + "  ( " + SRS + " )";
		                }else{
		                    situacion = "X="+ X + " Y=" + Y;
		                }
		            }
		            aNode=this.AddNode(aXMLRetorno.getDocumentElement(), "SITUACION");
		            log.debug("[generarFichaUrbanistica] Situacion Establecida");
		            
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
																				+ fileSep + "PlantillaFicha.jrxml";
		       
		            JasperReport aReport = JasperCompileManager.compileReport(rutaPlantilla);
		
		            JRXmlDataSource xmlDataSource = new JRXmlDataSource(aXMLRetorno,"FICHA/CAPA/GRUPO/ENTIDADES/ENTIDAD/CONDICIONES/CONDICION/CASOS/CASO/VALORES/VALOR/REGIMENES-ESPECIFICOS/REGIMEN-ESPECIFICO"); //"/FICHA/DEFINICIONES/DEFINICION"
		
		            HashMap hashMap = new HashMap();
		            hashMap.put("SRS", Textos.getCadena("urbrWS", "SRS_Datos"));
		            hashMap.put("BBOX", geoUtils.getWMS_BBOX(geoConsulta));
		            // FGA Comentado hashMap.put("ID_AMBITO", Integer.parseInt(request.getParameter("idAmbito"))); 
		            JasperPrint jasperPrint = JasperFillManager.fillReport(aReport, hashMap, xmlDataSource);
		
		            byte[] datos = JasperExportManager.exportReportToPdf(jasperPrint);
		            pdfFichaGenerada = new PdfFicha("Ficha_CU.pdf", datos);
		            fichaGenerada = true;
		            
		            /*
		            OutputStream out = new FileOutputStream("fichaOK.pdf");
		            
		            log.info("[generarFichaUrbanistica] fichaOK.pdf");
		            out.write(bytes);
		            out.close();
		           */
		            
		            facesMessages.addFromResourceBundle(Severity.INFO, "fichacu_generadaok", null);
		            
		           
		         }
		    } else {
		    	
		    	/*
		    	HashMap hashMap = new HashMap();
	            hashMap.put("SRS", Textos.getCadena("urbrWS", "SRS_Datos"));
	            hashMap.put("BBOX", geoUtils.getWMS_BBOX(geoConsulta));
	            
	            String fileSep = System.getProperty("file.separator");
	            
				String rutaPlantilla = System.getProperty("jboss.home.dir") + fileSep + "var" 
																			+ fileSep + "ficha" 
																			+ fileSep + "PlantillaSinDatos.jrxml";
	       
	            JasperReport aReport = JasperCompileManager.compileReport(rutaPlantilla);
	            
	            JasperPrint jasperPrint = JasperFillManager.fillReport(aReport, hashMap);
	            
	            byte[] datos = JasperExportManager.exportReportToPdf(jasperPrint);
	            
	            pdfFichaGenerada = new PdfFicha("Ficha_CU.pdf", datos);
	            fichaGenerada = true;
	       		            
	            
		    	*/
		        log.info("[generarFichaUrbanistica] fichaSinDatos2.txt");
		        
		        facesMessages.addFromResourceBundle(Severity.ERROR, "fichacu_sindatos", null);
		    }
		} catch (Exception e) {
		    e.printStackTrace();
		    log.error("[generarFichaUrbanistica]ERROR= "+e.getMessage());
		    facesMessages.addFromResourceBundle(Severity.ERROR, "fichacu_error", null);
		} finally {
			
			log.info("[generarFichaUrbanistica] finally");
		}
		
		log.info("[generarFichaUrbanistica] FIN");
    }

	private Document consultaEntidades(
            String wktGeometry,
            String srs) {

        try
        {
            Document aXML;
            Geometry aGeometriaTransformada;
            Geometry aGeometriaConsulta;


            aGeometriaConsulta=geoUtils.GeoFromWKT(wktGeometry);

            if (srs!=null ){
                if (srs.equals("")){
                    aGeometriaTransformada=aGeometriaConsulta;
                    srs=Textos.getCadena("urbrWS", "SRS_Datos");
                }else{
                    
                	// FGA aGeometriaTransformada=geoUtils.TransformGeometry(aGeometriaConsulta,srs,Textos.getCadena("urbrWS", "SRS_Datos"));
                	aGeometriaTransformada = aGeometriaConsulta;
                }
            }else{
                aGeometriaTransformada=aGeometriaConsulta;
            }

            aXML=XMLws.generarXMLDOC("XML");
            aXML.getDocumentElement().appendChild(servicioBasicoFichaUrbanistica.getEntidades(aGeometriaTransformada,aXML));
            return aXML;
        }catch (Exception e){
        	e.printStackTrace();
		    log.error("[consultaEntidades]ERROR= "+e.getMessage());
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
    
 private void fillPlaneamientoVigente(Element padre,Geometry geoConsulta){
        
        
        List<Plan> planes =servicioBasicoFichaUrbanistica.findPlanesGeom(geoConsulta.toText());
        for (Plan plan : planes)
        {
            Tramite aTramite = plan.getTramites().iterator().next();
            if (aTramite!=null){
                
                    Element node = this.AddNode(padre, "PLAN");
                    node.setAttribute("nombre",plan.getNombre());
                    node.setAttribute("tramite",aTramite.getIdtipotramite()+"");
                    node.setAttribute("iteracion",String.valueOf(aTramite.getIteracion()));
                
            }
        }
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
    
    public String textoFichaPdf() {
    	return "FichaUrbanistica_" + X + "_" + Y + ".pdf";
    	
    }

	public String getX() {
		return X;
	}

	public void setX(String x) {
		X = x;
	}

	public String getY() {
		return Y;
	}

	public void setY(String y) {
		Y = y;
	}

	public String getSRS() {
		return SRS;
	}

	public void setSRS(String sRS) {
		SRS = sRS;
	}

	public Resource getPdfFichaGenerada() {
		return pdfFichaGenerada;
	}

	public void setPdfFichaGenerada(Resource pdfFichaGenerada) {
		this.pdfFichaGenerada = pdfFichaGenerada;
	}

	public boolean isFichaGenerada() {
		return fichaGenerada;
	}

	public void setFichaGenerada(boolean fichaGenerada) {
		this.fichaGenerada = fichaGenerada;
	}

	  
}

class PdfFicha implements Resource, Serializable{
    private String nombrePdf;
    private InputStream inputStream;
    private final Date lastModified;
    private ExternalContext extContext;
    private byte[] datosPdf;

    public PdfFicha(String nombrePdf, byte[] datos) {
        this.extContext = FacesContext.getCurrentInstance().getExternalContext();
        this.nombrePdf = nombrePdf;
        this.lastModified = new Date();   
        this.datosPdf = datos;
    }
    
    /**
     * This intermediate step of reading in the files from the JAR, into a
     * byte array, and then serving the Resource from the ByteArrayInputStream,
     * is not strictly necessary, but serves to illustrate that the Resource
     * content need not come from an actual file, but can come from any source,
     * and also be dynamically generated. In most cases, applications need not
     * provide their own concrete implementations of Resource, but can instead
     * simply make use of com.icesoft.faces.context.ByteArrayResource,
     * com.icesoft.faces.context.FileResource, com.icesoft.faces.context.JarResource.
     */
    public InputStream open() throws IOException {
        if (inputStream == null) {
            inputStream = new ByteArrayInputStream(datosPdf);
        }
        return inputStream;
    }
    
    public String calculateDigest() {
        return nombrePdf;
    }

    public Date lastModified() {
        return lastModified;
    }

    public void withOptions(Options arg0) throws IOException {
    }

	public ExternalContext getExtContext() {
		return extContext;
	}

	public void setExtContext(ExternalContext extContext) {
		this.extContext = extContext;
	}

	public byte[] getDatosPdf() {
		return datosPdf;
	}

	public void setDatosPdf(byte[] datosPdf) {
		this.datosPdf = datosPdf;
	}
} 


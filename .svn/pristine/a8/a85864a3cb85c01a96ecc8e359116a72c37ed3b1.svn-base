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

package com.mitc.redes.editorfip.servicios.procesamientofip.obtencionfip1;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.faces.model.SelectItem;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.log.Log;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.mitc.redes.editorfip.excepciones.EditorFIPException;


@Stateless
@Name("ImportadorFIP1Page")
public class ImportadorFIP1PageBean implements ImportadorFIP1Page {

    @Logger private Log log;
    @In(create=true) FacesMessages facesMessages;
	
    //private String url = "http://urbanismoenred.depourense.es/urbanismoenredWS/urbrWS?WSDL";
    //private String url = "http://91.116.138.164/urbanismoenredWS/urbrWS?WSDL";
    private String url;
    private String msjError = "";
    private String msjDescargarError = "";
    private String urlDescarga;
    private boolean verUrlDescarga = false;
    
    
 
    public boolean isVerUrlDescarga() {
		return verUrlDescarga;
	}


	public void setVerUrlDescarga(boolean verUrlDescarga) {
		this.verUrlDescarga = verUrlDescarga;
	}


	public ImportadorFIP1PageBean() {
    	
    	url = "";
    	urlDescarga = "";
    }
    
    
    public String getMsjDescargarError() {
		return msjDescargarError;
	}
	public void setMsjDescargarError(String msjDescargarError) {
		this.msjDescargarError = msjDescargarError;
	}


	private String msjOk = "";
	private List<SelectItem> ambitos = new ArrayList<SelectItem>();
	private String ambito;
	
	public String getAmbito() {
		return ambito;
	}
	public void setAmbito(String ambito) {
		this.ambito = ambito;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
	public String getMsjError() {
		return msjError;
	}
	public void setMsjError(String msjError) {
		this.msjError = msjError;
	}
	public String getMsjOk() {
		return msjOk;
	}
	public void setMsjOk(String msjOk) {
		this.msjOk = msjOk;
	}
	
	
	
	@Override
	public List<String> getAmbitosFromWS() {
		
		log.info("Obteniendo ambitos del WS: " + url);

		String soapRequestAmbitosWithPlan =  
			"<soapenv:Envelope  "+
			"xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" " +
			"xmlns:ser=\"http://serviciosweb.urbanismoenred.redes.mitc.es/\"> "+
			"<soapenv:Header/> "+
			"<soapenv:Body> "+
			"  <ser:AmbitosWithPlan> "+
			"   <idioma>?</idioma> "+
			"</ser:AmbitosWithPlan> "+
			"</soapenv:Body> "+
			"</soapenv:Envelope>";

		try {
			
			
			URL u = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) u.openConnection();
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type",  "application/soap+xml");
			conn.setRequestProperty("Content-Length", Integer.toString(soapRequestAmbitosWithPlan.getBytes().length));
			conn.setConnectTimeout(8000); // 8 segundos
			conn.setDoOutput(true);
			conn.setDoInput(true);
			
			// Send request
			log.info("Haciendo peticion a URL: "+url);
			DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
			wr.writeBytes(soapRequestAmbitosWithPlan);
			wr.flush();
			wr.close();
			
			// Read response
			log.info("Esperando respuesta ...");
			BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line, responseStr = "";
			while((line = rd.readLine()) != null) {		
				responseStr += line;
			}			
			log.info("Respuesta: "+responseStr);
			
			// Creamos el documento XML
			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder;
			docBuilder = docBuilderFactory.newDocumentBuilder();	
			Document xmlDoc = docBuilder.parse(new InputSource(new ByteArrayInputStream(responseStr.getBytes("utf-8"))));
			xmlDoc.getDocumentElement().normalize();
			
			// Buscar nodo "return"
			NodeList nodes=null;
		    XPathExpression expr;
		    XPathFactory factory = XPathFactory.newInstance();
		    XPath xpath = factory.newXPath();
			expr = xpath.compile("//return");
		    Object result = expr.evaluate(xmlDoc, XPathConstants.NODESET);
		    nodes = (NodeList) result;
			
		    // Obtener el XML con los ambitos
		    String xmlAmbitos = nodes.item(0).getTextContent();
		    log.info("XML de ‡mbitos: " + xmlAmbitos);
		    xmlDoc = docBuilder.parse(new InputSource(new ByteArrayInputStream(xmlAmbitos.getBytes("utf-8"))));
			xmlDoc.getDocumentElement().normalize();
			
			// Buscar todos los elementos AMBITO en el XML
			expr = xpath.compile("//AMBITO");
			result = expr.evaluate(xmlDoc, XPathConstants.NODESET);
		    nodes = (NodeList) result;
			
		    // Rellenar ambitos
		    Node n;
		    SelectItem newSelectItem;
		    log.info("Elementos <AMBITO>: " + nodes.getLength());
		    for(int i=0; i<nodes.getLength(); i++) {
		    	
		    	n = nodes.item(i);
		    	
		    	newSelectItem = new SelectItem();
		    	for(int j=0; j<n.getChildNodes().getLength(); j++ ){
		    		if (n.getChildNodes().item(j).getNodeName().compareTo("id")==0) {
		    			newSelectItem.setValue(n.getChildNodes().item(j).getTextContent());		
		    		}

		    		if (n.getChildNodes().item(j).getNodeName().compareTo("nombre")==0) {
		    			newSelectItem.setLabel(n.getChildNodes().item(j).getTextContent());		
		    		}
		    	}
		    	log.info("Nodo, id:" + newSelectItem.getValue() + "  etiqueta: " + newSelectItem.getLabel());
		    					
		    	ambitos.add(newSelectItem);
		    }
		    
		    // msjOk = "Servicio Web correcto.";
		    facesMessages.addFromResourceBundle("servicio_web_ok", null);
			
		} catch (XPathExpressionException e) {
			facesMessages.addFromResourceBundle("error_servicio_web", null);
			//msjError = "Error al llamar al servicio web";
			log.error("Error al parsear XML: " + e.getMessage());
		} catch (Exception e) {
			facesMessages.addFromResourceBundle("error_servicio_web", null);
			//msjError = "Error al llamar al servicio web";
			log.error("Error llamando al servicio web");
			e.printStackTrace();			
		}

		return null;
	}
	
	
	

	public String getUrlDescarga() {
		return urlDescarga;
	}
	
	@Override
	public void descargarFIP1() {
		
		log.info("Descargar FIP1 del ‡mbito: " + ambito);

		String soapRequestFechaRefundido = 
			"<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ser=\"http://serviciosweb.urbanismoenred.redes.mitc.es/\"> "+
			   "<soapenv:Header/>"+
			   "<soapenv:Body>"+
			   "   <ser:fechaRefundido>"+
			   "      <idAmbito>"+ambito+"</idAmbito>"+
			   "   </ser:fechaRefundido>"+
			   "</soapenv:Body>"+
			"</soapenv:Envelope>";

		try {
			
			URL u = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) u.openConnection();
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type",  "application/soap+xml");
			conn.setRequestProperty("Content-Length", Integer.toString(soapRequestFechaRefundido.getBytes().length));
			conn.setDoOutput(true);
			conn.setDoInput(true);
			
			// Enviar peticion de servicio web
			DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
			wr.writeBytes(soapRequestFechaRefundido);
			wr.flush();
			wr.close();
			
			// Read response
			BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line, responseStr = "";
			while((line = rd.readLine()) != null) {		
				responseStr += line;
			}			
			log.info("Respuesta: "+responseStr);
			
			
			// Creamos el documento XML
			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder;
			docBuilder = docBuilderFactory.newDocumentBuilder();	
			Document xmlDoc = docBuilder.parse(new InputSource(new ByteArrayInputStream(responseStr.getBytes("utf-8"))));
			xmlDoc.getDocumentElement().normalize();
			
			// Buscar nodo "return"
			NodeList nodes=null;
		    XPathExpression expr;
		    XPathFactory factory = XPathFactory.newInstance();
		    XPath xpath = factory.newXPath();
			expr = xpath.compile("//return");
		    Object result = expr.evaluate(xmlDoc, XPathConstants.NODESET);
		    nodes = (NodeList) result;
		    if (nodes.getLength()==0) throw new EditorFIPException("No hay refundido disponible.");
			
		    // Obtener el XML con los ambitos
		    String xmlAmbitos = nodes.item(0).getTextContent();
		    log.info("XML de ‡mbitos: " + xmlAmbitos);
		    xmlDoc = docBuilder.parse(new InputSource(new ByteArrayInputStream(xmlAmbitos.getBytes("utf-8"))));
			xmlDoc.getDocumentElement().normalize();
			
			// Buscar todos los elementos AMBITO en el XML
			expr = xpath.compile("//REFUNDIDO");
			result = expr.evaluate(xmlDoc, XPathConstants.NODESET);
		    nodes = (NodeList) result;
		    Node node = nodes.item(0);
			
		    // Coger los atributos
			NamedNodeMap attrs = node.getAttributes();
			Map<String,Object> attrMap = new HashMap<String, Object>();
			Attr attr;
			if (attrs!=null) {
				for (int a = 0; a < attrs.getLength(); a++) {
					attr = (Attr) attrs.item(a);
					attrMap.put(attr.getName(), attr.getValue());
				}
			}
		    urlDescarga = (String) attrMap.get("fip1");
		    log.info("URL del FIP1: " + urlDescarga);
		    
		    
		    verUrlDescarga = true;
		    
		    /*
		    // Empezar a descargar
		    log.info("Iniciando la descarga del archivo FIP1 ...");
		    URL url = new URL(fip1Url);
		    InputStream response = url.openStream();
		    BufferedReader reader = new BufferedReader(new InputStreamReader(response));
		    File file = new File("cobarde.fip1");
		    FileWriter fw = new FileWriter(file);
		    BufferedWriter bw = new BufferedWriter(fw);
		    int count = 0;
		    for (String l; (l=reader.readLine())!=null;) {
		    	bw.write(l);
		    	count++;
		    	if (count % 500 == 0) {
		    		tamanioFichero = file.length();
		    		log.info("Tama–o de fichero: " + tamanioFichero + " bytes");
		    	}
		    }
		   	  */  
		    
		    /*			
		} catch (XPathExpressionException e) {
			msjDescargarError = "Error al llamar al servicio web";
			log.error("Error al parsear XML: " + e.getMessage());
		*/
		    
		} catch (EditorFIPException e) {
			facesMessages.addFromResourceBundle("no_hay_refundido", null);
			//msjDescargarError = e.getMessage();
			log.error("Error llamando al servicio web: " + e.getMessage());
			
		} catch (Exception e) {
			facesMessages.addFromResourceBundle("Error_servicio_web", null);
			//msjDescargarError = "Error al llamar al servicio web";
			log.error("Error llamando al servicio web");
			e.printStackTrace();			
		}
	}

	
	
	public List<SelectItem> getAmbitos() {
		return ambitos;
	}
}

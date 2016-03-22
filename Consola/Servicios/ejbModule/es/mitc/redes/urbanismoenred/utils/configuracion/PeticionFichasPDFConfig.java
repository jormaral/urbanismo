package es.mitc.redes.urbanismoenred.utils.configuracion;

import es.mitc.redes.urbanismoenred.utils.excepciones.RedesConfigException;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class PeticionFichasPDFConfig {

	
	
	//static private String CONFIG_FILE = "PeticionFichasPDFConfig.xml";
	
	static private String CONFIG_FILE;
	static private Document docXML;
	static private String REDES_PATH;
	
	static private Logger log = Logger.getLogger( PeticionFichasPDFConfig.class );
	
	
	static private void loadEnvironmentVariable() throws RedesConfigException {

		try {

			// Get environment variable REDES_PATH
			REDES_PATH = System.getenv("REDES_PATH");
			
			// Check envarionment variable
			if ( (REDES_PATH == null) || (REDES_PATH.equals("")) ) {
				log.fatal("The environment variable REDES_PATH doesn't exist. Please, create it.");
				throw new RedesConfigException("FATAL ERROR !!! The environment variable REDES_PATH doesn't exist. Please, create it.");
			}
			log.debug("Environment variable REDES_PATH: " + CONFIG_FILE);
			
			
			// Check REDES_PATH
			File dir = new File(REDES_PATH);
			if (!dir.isDirectory()) {
				log.fatal("FATAL ERROR !!! Directory doesn't exist: " + REDES_PATH);
				throw new RedesConfigException("FATAL ERROR !!! Directory doesn't exist: " + REDES_PATH);
			}
			log.debug("Directory of RedesRootFileSystem is correct.");
			
			char s = File.separatorChar;
			CONFIG_FILE = dir.getAbsolutePath() + s +"conf"+ s +"PeticionFichasPDFConfig.xml";
			log.debug("Redes config file: " + CONFIG_FILE);
		
		} catch (Exception e) {
			log.error("Unknow error getting enivonment variable: " + e.getMessage());
			e.printStackTrace();
		}
	}

	
	/**
	 * 	Get parameter
	 * 
	 * @param xpathString
	 * @return
	 * @throws RedesConfigException 
	 * @throws Exception 
	 */
	public static String getConfigParameter(String xpathString) throws RedesConfigException {
		
		String configParameter = null;
		
		if (CONFIG_FILE==null) {
			loadEnvironmentVariable();
		}
		
		
		if ( docXML == null ) {
			loadXML(CONFIG_FILE);
		}
		
		try {

		    // XPath object
		    XPathFactory factory = XPathFactory.newInstance();
		    XPath xpath = factory.newXPath();
		    XPathExpression expr = xpath.compile(xpathString);

		    Object result = expr.evaluate(docXML, XPathConstants.NODESET);
		    NodeList nodes = (NodeList) result;
		    		    
		    if (nodes.getLength()>0) {
		    	configParameter = nodes.item(0).getNodeValue().trim();
		    }			
		} catch (XPathExpressionException e) {
			log.error("Error RedesConfig al capturar configuracion: "+xpathString+"\n"+e.getMessage());
			throw new RedesConfigException("Error al obtener parametro de configuracion: "+xpathString);
		}

		return configParameter;
	    	
	}
	
	
	public static List<Node> getConfigParameterList(String xpathString) throws RedesConfigException { 
	
		// Vars
		List<Node> resultList = new ArrayList<Node>();
		
		
		if (CONFIG_FILE==null) {
			loadEnvironmentVariable();
		}
		
		
		if ( docXML == null ) {
			loadXML(CONFIG_FILE);
		}
		
		try {
	
		    // XPath object
		    XPathFactory factory = XPathFactory.newInstance();
		    XPath xpath = factory.newXPath();
		    XPathExpression expr = xpath.compile(xpathString);
	
		    Object result = expr.evaluate(docXML, XPathConstants.NODESET);
		    NodeList nodes = (NodeList) result;
		    

		    
		    // Fill list
		    Node node = null;
		    for (int i=0; i<nodes.getLength(); i++) {
		    
		    	// Get node
		    	node = nodes.item(i);
		    	
		    	resultList.add( node );
		    }

		    
		} catch (XPathExpressionException e) {
			log.error("Error RedesConfig al capturar configuracion: "+xpathString+"\n"+e.getMessage());
			throw new RedesConfigException("Error al obtener parametro de configuracion: "+xpathString);
		}
	
		return resultList;
	    	
	}	
	
	
	/**
	 * 	Load configuration file
	 * 
	 * @param xmlFile
	 * @throws Exception
	 */
	private static void loadXML(String xmlFile) throws RedesConfigException {

		DocumentBuilderFactory domFactory = DocumentBuilderFactory
				.newInstance();
		domFactory.setNamespaceAware(true); // never forget this!
		DocumentBuilder builder;
		try {
			builder = domFactory.newDocumentBuilder();
			docXML = builder.parse( xmlFile );
		} catch (Exception e) {
			
			throw new RedesConfigException("Error al abrir fichero de configuracion PeticionFichasPDFConfig.xml: "+e.getMessage());

		}

	}


	public static String getREDES_PATH() {
		return REDES_PATH;
	}
	
	
	
}

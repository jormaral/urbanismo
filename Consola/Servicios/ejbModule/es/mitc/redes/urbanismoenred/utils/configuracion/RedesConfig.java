package es.mitc.redes.urbanismoenred.utils.configuracion;

import es.mitc.redes.urbanismoenred.utils.excepciones.RedesConfigException;
import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

public class RedesConfig {


	// static private String CONFIG_FILE = "RedesConfig.xml";
	static private String CONFIG_FILE;
	static private Document docXML;
	static private String REDES_PATH;
	
	static private Logger log = Logger.getLogger( RedesConfig.class );
	
	
	static private void loadEnvironmentVariable() throws RedesConfigException {

		try {

			// Get environment variable REDES_PATH
			REDES_PATH = getREDES_PATH();
			
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
			
			
			CONFIG_FILE = dir.getAbsolutePath() + "/conf/RedesConfig.xml";
			log.debug("Redes config file: " + CONFIG_FILE);
		
		} catch (Exception e) {
			log.error("Unknow error getting enivonment variable: " + e.getMessage());
			e.printStackTrace();
		}
	}

	
//	/**
//	 * Check configuration file 
//	 * 
//	 * @return boolean
//	 * @throws RedesConfigException 
//	 */
//	public static boolean isConfigFileOk() {
//		
//		try {
//			loadEnvironmentVariable();
//		} catch (RedesConfigException e) {
//			return false;
//		}
//		
//		File f = new File(CONFIG_FILE);
//		return f.exists();
//	}
	
	
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
		    	configParameter = nodes.item(0).getTextContent().trim();
		    }			
		} catch (XPathExpressionException e) {
			log.error("Error RedesConfig al capturar configuracion: "+xpathString+"\n"+e.getMessage());
			throw new RedesConfigException("Error al obtener parametro de configuracion: "+xpathString);
		}

		return configParameter;
	    	
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
			
			throw new RedesConfigException("Error al abrir fichero de configuracion RedesConfig.xml: "+e.getMessage());

		}

	}


	public static String getREDES_PATH() {
		return System.getenv("REDES_PATH");
	}
	
	
	
}

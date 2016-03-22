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


package com.mitc.redes.editorfip.utilidades;

import java.io.IOException;
import java.io.InputStream;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Textos {
	
	private static final String NO_PROPERTIES_FOUND = "Fichero de recursos no encontrado: ";
	private static final String NO_RESOURCE_FOUND = "Recurso no encontrado: ";
	private static final String PROPERTIES_PACKAGE = "com.mitc.redes.editorfip.utilidades";
	public static final String PROPERTIES_RESOURCE_ES = "messages_es.properties";
	public static final String PROPERTIES_RESOURCE_EN= "messages_en.properties";
	public static final String PROPERTIES_DICCIONARIO = "diccionario.properties";

	/**
	 * Metodo que devuelve el texto indicado en un fichero de recursos
	 * @param propertiesFile Nombre del fichero de recursos (sin extension .properties)
	 * @param key Texto que se desea obtener de dicho fichero de recursos
	 * @return String texto incluido en dicho fichero de recursos
	 */
	public static String getTexto (String propertiesFile, String key)
	{
		String texto = "";
		
		try
		{
		ResourceBundle bundle = ResourceBundle.getBundle(PROPERTIES_PACKAGE + "." + propertiesFile);
		texto = getStringSafely(bundle, key, null);
		}
		catch (MissingResourceException mrex) // En caso de no existir el fichero de recursos
		{
			texto = NO_PROPERTIES_FOUND + propertiesFile ;
		}
		return texto;
	}
	
	public static String getTexto (String key)
	{
		String texto = "";
		
		try
		{
		ResourceBundle bundle = ResourceBundle.getBundle(PROPERTIES_PACKAGE + "." + PROPERTIES_RESOURCE_ES);
		texto = getStringSafely(bundle, key, null);
		}
		catch (MissingResourceException mrex) // En caso de no existir el fichero de recursos
		{
			texto = NO_PROPERTIES_FOUND + "messages_es.properties";
		}
		return texto;
	}
	
	/**
     * Metodo interno para devolver un mensaje adecuado en caso de no existir la clave
     */
    private static String getStringSafely(ResourceBundle bundle, String key, String defaultValue) {
        String resource = null;
        try {
            resource = bundle.getString(key);
        }
        catch (MissingResourceException mrex) { // En caso de no existir dicho texto
            if (defaultValue != null) {
                resource = defaultValue;
            }
            else {
                resource = NO_RESOURCE_FOUND + key;
            }
        }
        return resource;
    }
    
    public static String getCadena (String key)
	{
		
		InputStream inputStream = Textos.class.getClassLoader()  
        .getResourceAsStream("messages_es.properties"); 
		Properties properties = new Properties();  
		String propValue = "";
        // load the inputStream using the Properties  
        try {
			properties.load(inputStream);
			// get the value of the property  
	        propValue = properties.getProperty(key);  
	        
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			propValue = NO_PROPERTIES_FOUND + "messages_es.properties";
		
		} catch (Exception e) {
			
			propValue = "Property value not found.";
		}
		return propValue;
	}
    
    public static String getCadena (String resource, String key)
	{
		
		InputStream inputStream = Textos.class.getClassLoader()  
        .getResourceAsStream(resource + ".properties"); 
		Properties properties = new Properties();  
		String propValue = "";
        // load the inputStream using the Properties  
        try {
			properties.load(inputStream);
			// get the value of the property  
	        propValue = properties.getProperty(key);  
	        
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			propValue = NO_PROPERTIES_FOUND + "resource" + ".properties";
		
		} catch (Exception e) {
			
			propValue = "Property value not found.";
		}
		return propValue;
	}
    
    public static boolean validarExpresion(String expresion, String cadena)
    {
    	Pattern p = Pattern.compile(expresion);
		Matcher m = p.matcher(cadena);
		
		int cont = 0;
		while(m.find()) {
			cont++;
		}
		if (cont == cadena.length())
			return true;
		else
			return false;
    }
    
   
}

/*
* Copyright 2011 red.es
* Autores: Arnaiz Consultores.
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
package es.mitc.redes.urbanismoenred.serviciosweb;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Textos {
	
	private static final String NO_PROPERTIES_FOUND = "Fichero de recursos no encontrado: ";
	private static final String NO_RESOURCE_FOUND = "Recurso no encontrado: ";
	private static final String PROPERTIES_PACKAGE = "main.java.es.mitc.redes.urbanismoenred.serviciosweb";

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

    /**

	 */
	public static List<String> getKeys (String propertiesFile, String patronInicio)
	{
		Enumeration<String> textos ;
		String aTexto;
        List<String> aRespuesta;
		try
		{
            ResourceBundle bundle = ResourceBundle.getBundle(PROPERTIES_PACKAGE + "." + propertiesFile);
            textos=bundle.getKeys();
            aTexto=textos.nextElement();
            aRespuesta=new ArrayList<String>();
            while (textos.hasMoreElements()){
                if (aTexto.contains( patronInicio)){
                    aRespuesta.add(aTexto.replace(patronInicio, "") + "@" + getStringSafely (bundle,aTexto,""));
                }
                aTexto=textos.nextElement();
            }
            return aRespuesta;
		}
		catch (MissingResourceException mrex) // En caso de no existir el fichero de recursos
		{
			return null;
		}
		
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
}


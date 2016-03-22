/*
* Copyright 2011 red.es
* Autores: Arnaiz Consultores.
*
** Licencia con arreglo a la EUPL, Versión 1.1 o -en cuanto
** sean aprobadas por la Comision Europea- versiones
** posteriores de la EUPL (la <<Licencia>>);
** Solo podra usarse esta obra si se respeta la Licencia.
* Puede obtenerse una copia de la Licencia en:
*
* http://ec.europa.eu/idabc/eupl5
*
** Salvo cuando lo exija la legislacion aplicable o se acuerde
* por escrito, el programa distribuido con arreglo a la
* Licencia se distribuye <<TAL CUAL>>,
** SIN GARANTIAS NI CONDICIONES DE NINGUN TIPO, ni expresas
** ni implicitas.
** Vease la Licencia en el idioma concreto que rige
* los permisos y limitaciones que establece la Licencia.
*/
package es.mitc.redes.urbanismoenred.servicios.comunes;

import java.security.*;

/**
* @author Javier.Molina
* Encripta un String con el algoritmo MD5.
* @return El algoritmo encriptado
* @param palabra
*/

public class EncriptacionCodigoTramite {

    /** * Clase con métodos estáticos de cifrado * */
    public static String getEncoded(String text, String algorithm) throws NoSuchAlgorithmException {
       
        //texto de salida = texto de entrada codificado
        MessageDigest md;
        byte[] textBytes = text.getBytes();

        //raw-bytes del texto de entrada
        md = MessageDigest.getInstance  (algorithm);

        //método factoría
        md.update(textBytes);

        //actualización
        byte[] outputBytes = md.digest();

        String s = "";
        for (int i = 0; i < outputBytes.length; i++){
            s += Integer.toHexString((outputBytes[i] >> 4) & 0xf);
            s += Integer.toHexString(outputBytes[i] & 0xf);
        }
        return s;
    }

    public static String getEncoded(String text) {
        try {
			return getEncoded(text, "MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
    }
}

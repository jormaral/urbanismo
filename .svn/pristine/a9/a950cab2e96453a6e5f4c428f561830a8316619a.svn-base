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

package es.mitc.redes.urbanismoenred.rest;

import java.net.Authenticator;
import java.net.PasswordAuthentication;

/**
 *
 * @author a.centeno
 */
public class SimpleAuthenticator
   extends Authenticator
{
   private String username,
                  password;

   public SimpleAuthenticator(String username,String password)
   {
      this.username = username;
      this.password = password;
   }

   protected PasswordAuthentication getPasswordAuthentication()
   {
      return new PasswordAuthentication(
             username,password.toCharArray());
   }
}



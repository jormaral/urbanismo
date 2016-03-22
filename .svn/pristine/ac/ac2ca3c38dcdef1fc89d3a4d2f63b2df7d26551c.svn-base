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

package com.mitc.redes.editorfip.servicios.basicos.planeamiento.entidades.ejemplo;

import java.io.Serializable;

import javax.ejb.Remove;
import javax.ejb.Stateful;

import org.jboss.seam.annotations.Begin;
import org.jboss.seam.annotations.End;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.log.Log;



@Stateful
@Name("entidadConversation")
public class EntidadConversationBean implements EntidadConversation, Serializable
{
    @Logger private Log log;
    

    
    private int value;

    @Begin
    public String begin()
    {
        // implement your begin conversation business logic
        log.info("beginning conversation");
        return "success";
    }
  
    public String increment()
    {
        log.info("incrementing");
        value++;
        return "success";
    }
  
    // add additional action methods that participate in this conversation
  
    @End
    public String end()
    {
        // implement your end conversation business logic
        log.info("ending conversation");
        return "home";
    }
  
    public int getValue()
    {
        return value;
    }
  
    @Remove
    public void destroy() {}

}

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

import javax.ejb.Remove;
import javax.ejb.Stateful;

import org.hibernate.validator.Length;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.international.StatusMessages;
import org.jboss.seam.log.Log;

@Stateful
@Name("entidadEditar")
public class EntidadEditarBean implements EntidadEditar
{
    @Logger private Log log;

    @In StatusMessages statusMessages;

    private String value;

    public void entidadEditar()
    {
        // implement your business logic here
        log.info("entidadEditar.entidadEditar() action called with: #{entidadEditar.value}");
        statusMessages.add("entidadEditar #{entidadEditar.value}");
    }

    // add additional action methods

    @Length(max = 10)
    public String getValue()
    {
        return value;
    }

    public void setValue(String value)
    {
        this.value = value;
    }

    @Remove
    public void destroy() {}

}

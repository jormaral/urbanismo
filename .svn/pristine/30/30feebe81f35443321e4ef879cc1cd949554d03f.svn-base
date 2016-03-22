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

package com.mitc.redes.editorfip.servicios.genericos;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

public class StringLimiterConverter implements Converter {

	private static final String LIMIT_PARAMETER_NAME = "limit";
    private static final int DEFAULT_LIMIT = 50;

    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        return limit(value, getLimitAttribute(component));
    }

    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (! (value instanceof String))
            return null;
        else {
            return limit(value.toString(), getLimitAttribute(component));
        }
    }

    private int getLimitAttribute(UIComponent component) {
        Object att = component.getAttributes().get(LIMIT_PARAMETER_NAME);
        if (att == null)
            return DEFAULT_LIMIT;
        else
            return Integer.parseInt((String)component.getAttributes().get(LIMIT_PARAMETER_NAME));
    }

    private String limit(String s, int limit) {
        String limited = s;
        if (! (s.length() <= limit))
        {
        	limited = s.substring(0, limit);
        	limited+=" ...";
        }
            
        
        return limited;
    }

}

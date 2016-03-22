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

import javax.faces.event.ValueChangeEvent;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.icesoft.faces.context.effects.Effect;
import com.icesoft.faces.context.effects.Highlight;


public class BaseBean {
    //the logger for this class
	protected final Log logger = LogFactory.getLog(this.getClass());

    // effect that shows a value binding chance on there server
    protected Effect valueChangeEffect;

    public BaseBean() {
        valueChangeEffect = new Highlight("#fda505");
        valueChangeEffect.setFired(true);
    }

    public void effectChangeListener(ValueChangeEvent event){
        valueChangeEffect.setFired(false);
    }


	protected void init() {

    }

    public Effect getValueChangeEffect() {
        return valueChangeEffect;
    }

    public void setValueChangeEffect(Effect valueChangeEffect) {
        this.valueChangeEffect = valueChangeEffect;
    }
}

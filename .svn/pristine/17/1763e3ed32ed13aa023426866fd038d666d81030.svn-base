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

package com.mitc.redes.editorfip.servicios.gestionfip.gestionplaneamientoencargado;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

import com.mitc.redes.editorfip.entidades.rpm.diccionario.Ambito;
import com.mitc.redes.editorfip.entidades.rpm.gestionfip.PlanesEncargados;
import com.mitc.redes.editorfip.servicios.seam.EntidadHome;
import com.mitc.redes.editorfip.servicios.seam.TramiteHome;

@Name("planeamientoEncargadoHome")
public class PlaneamientoEncargadoHome extends EntityHome<PlanesEncargados> {
	
	@In(create = true)
	EntidadHome entidadHome;
	@In(create = true)
	TramiteHome tramiteHome;

	public void setPlanesEncargadosIden(Integer id) {
		setId(id);
	}

	public Integer getEntidadIden() {
		return (Integer) getId();
	}

	@Override
	protected PlanesEncargados createInstance() {
		
		PlanesEncargados planEncargado = new PlanesEncargados();
		planEncargado.setAmbito(new Ambito());
		setInstance(planEncargado);
		
		return planEncargado;
	}

	public PlanesEncargados getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}
	
	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public void wire() {
		getInstance();
	}

	public boolean isWired() {
		if (getInstance() == null)
			return false;
		return true;
	}
	
}

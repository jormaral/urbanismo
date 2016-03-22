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

package com.mitc.redes.editorfip.servicios.sesion;

import java.io.Serializable;
import java.util.Map;

import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Startup;
import org.jboss.seam.international.StatusMessages;
import org.jboss.seam.log.Log;

@Scope(ScopeType.SESSION)
@Startup
@Stateful
@Name("variablesSesionGenerales")
public class VariablesSesionGeneralesBean implements  Serializable, VariablesSesionGenerales
{
    @Logger private Log log;
    
    @In StatusMessages statusMessages;
    
    @In Map<String,String> messages;
    
    @PersistenceContext
	EntityManager em;

    private int idDocumentoSeleccionado;
    
    private int idDocumentoShpSeleccionado;
    
    private String geometriaWktSeleccionada;
	/* (non-Javadoc)
	 * @see com.mitc.redes.editorfip.servicios.VariablesSesionUsuario#destroy()
	 */
	/* (non-Javadoc)
	 * @see com.mitc.redes.editorfip.servicios.VariablesSesionUsuario#destroy()
	 */
    
    
    
	@Remove
    public void destroy() {}

	public int getIdDocumentoSeleccionado() {
		return idDocumentoSeleccionado;
	}

	public void setIdDocumentoSeleccionado(int idDocumentoSeleccionado) {
		this.idDocumentoSeleccionado = idDocumentoSeleccionado;
	}

	public int getIdDocumentoShpSeleccionado() {
		return idDocumentoShpSeleccionado;
	}

	public void setIdDocumentoShpSeleccionado(int idDocumentoShpSeleccionado) {
		this.idDocumentoShpSeleccionado = idDocumentoShpSeleccionado;
	}

	public String getGeometriaWktSeleccionada() {
		return geometriaWktSeleccionada;
	}

	public void setGeometriaWktSeleccionada(String geometriaWktSeleccionada) {
		this.geometriaWktSeleccionada = geometriaWktSeleccionada;
	}

	
}

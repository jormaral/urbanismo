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

import java.util.List;

import javax.ejb.Remove;
import javax.faces.model.SelectItem;

import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Tramite;

public interface VariablesSesionUsuario {
	
	public boolean estaTramiteTrabajoEstablecido();
	
	public Tramite getTramiteTrabajoActual();
	
	public void setIdTipoTramiteTrabajo(int idTipoTramiteTrabajo);
	
	public int getIdTramiteTrabajoActual();
	
	public int getIdAmbito();
	
	public String getAmbitoNombre();

	public Tramite getTramiteEncargadoTrabajo();

	public void setTramiteEncargadoTrabajo(Tramite tramiteEncargadoTrabajo);

	public Tramite getTramiteVigenteTrabajo();

	public void setTramiteVigenteTrabajo(Tramite tramiteVigenteTrabajo);

	public Tramite getTramiteBaseTrabajo();

	public void setTramiteBaseTrabajo(Tramite tramiteBaseTrabajo);

	
	public int getIdTramiteEncargadoTrabajo();

	public void setIdTramiteEncargadoTrabajo(int idTramiteEncargadoTrabajo);

	public int getIdTramiteVigenteTrabajo();

	public void setIdTramiteVigenteTrabajo(int idTramiteVigenteTrabajo);

	public int getIdTramiteBaseTrabajo();

	public void setIdTramiteBaseTrabajo(int idTramiteBaseTrabajo);
	
	public int getIdTipoTramiteTrabajo();
	
	public List<SelectItem> tipoPlanesSeleccion();
	
	public void seleccionarTramite(int idTramiteEncargadoTrabajo, String pagDest);

	// Prerefundido
	public boolean estaTramitePrerefundidoEstablecido();
	
	public void seleccionarPrerefundido(int idTramitePrerefundido, String pagDest);
	
	public Tramite getTramitePrerefundidoTrabajo();
	
	public void setTramitePrerefundidoTrabajo(Tramite tramitePrerefundidoTrabajo);
	
	public int getIdTramitePrerefundidoTrabajo();
	
	public void setIdTramitePrerefundidoTrabajo(int idTramitePrerefundidoTrabajo) ;
	

	/* (non-Javadoc)
	 * @see com.mitc.redes.editorfip.servicios.VariablesSesionUsuario#destroy()
	 */
	@Remove
	public void destroy();

}
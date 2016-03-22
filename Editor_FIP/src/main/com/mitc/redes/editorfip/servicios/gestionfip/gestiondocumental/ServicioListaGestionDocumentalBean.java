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

package com.mitc.redes.editorfip.servicios.gestionfip.gestiondocumental;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remove;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.log.Log;

import com.mitc.redes.editorfip.entidades.interfazgrafico.DocumentoDTO;
import com.mitc.redes.editorfip.servicios.basicos.fip.documentos.ServicioBasicoDocumentos;
import com.mitc.redes.editorfip.servicios.genericos.SortableList;
import com.mitc.redes.editorfip.servicios.sesion.VariablesSesionUsuario;

@Stateless
@Name("servicioListaGestionDocumental")
public class ServicioListaGestionDocumentalBean extends SortableList implements ServicioListaGestionDocumental {
	
	@In 
    EntityManager entityManager;
	
	//@In(create = true)
    //PlaneamientoEncargadoHome planEncargadoHome;
	
	@In(create=true)
    FacesMessages facesMessages;
	
	@Logger 
	private Log log;
	
	@In(create = true, required = false)
	VariablesSesionUsuario variablesSesionUsuario;
	
	@In (create = true, required = false)
	ServicioBasicoDocumentos servicioBasicoDocumentos;
	
	private List<DocumentoDTO> listaResultados = new ArrayList<DocumentoDTO>();
	private static final String nombreColumnName = "nombreentoperadora";
	private static final String ordenColumnName = "orden";
	
	@Remove
	public void remove() {
	}

	public ServicioListaGestionDocumentalBean() {
		super(nombreColumnName);
		
	}

	@Override
	public boolean isDefaultAscending(String nombreColumnName) {
		
		return true;
	}

	public String getNombreColumnName() {		
		return nombreColumnName;
	}

	public String getOrdenColumnName() {
		return ordenColumnName;
	}

	
	public void refrescarLista()
	{
		int idTramiteTrabajoActual = variablesSesionUsuario.getIdTramiteTrabajoActual();
		log.info("[refrescarLista] idTramiteTrabajoActual="+idTramiteTrabajoActual);

        if (idTramiteTrabajoActual==0){
        	listaResultados = new ArrayList<DocumentoDTO>();
        } else {
        	
        	
        	listaResultados = servicioBasicoDocumentos.obtenerListaDocumentoDTO(idTramiteTrabajoActual);
        }
        
        log.info("[refrescarLista] Longitud listaResultadosDocumentos:" + listaResultados.size());
	}
	

	public List<DocumentoDTO> getListaResultados() {
		return listaResultados;
	}
	

	@Override
	protected void sort() {
		// TODO Auto-generated method stub
		
	}
	
}

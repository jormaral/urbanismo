/*
* Copyright 2011 red.es
* Autores: IDOM Consulting
*
* Licencia con arreglo a la EUPL, Versiï¿½n 1.1 o -en cuanto
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

package com.mitc.redes.editorfip.servicios.gestionfip.generacionfip;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.ejb.Remove;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.web.RequestParameter;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.log.Log;

import com.mitc.redes.editorfip.entidades.rpm.gestionfip.FipsGenerados;
import com.mitc.redes.editorfip.servicios.basicos.comunes.ServicioBasicoGeneral;

@Stateless
@Name ("listadoFipGenerado")
public class ListadoFipGeneradoBean  implements ListadoFipGenerado{

	
	@In 
    EntityManager entityManager;
	
	@In(create=true)
    FacesMessages facesMessages;
	
	@In (create = true)
	ServicioBasicoGeneral servicioBasicoGeneral;
	
	@Logger 
	private Log log;
	
	private FipsGenerados fipGenerado;
	
	private HashMap<Long,Boolean> seleccion;
	
	
	private String idSeleccionado;
	
	@RequestParameter
	private Long identificador;
	
	@SuppressWarnings("unchecked")
	public List<FipsGenerados> getListado() {
		
		log.debug("[getListado] Inicio");
		
		/*
		
		try {
			super.setOrderColumn("fechaGeneracion");
			super.setOrderDirection("DESC");
			super.inicializarLista("com.mitc.redes.editorfip.entidades.rpm.gestionfip.FipsGenerados");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List<FipsGenerados> lista = (List<FipsGenerados>) super.listaInicial;
		//log.debug("TAMAÑO DE LA LISTA" + listaInicial.size());
		
		seleccion = new HashMap<Long,Boolean>();
		if(lista!=null) {
			for(FipsGenerados temp : lista) {
				seleccion.put(temp.getId(), Boolean.FALSE);
			}
		}
		*/
		List<FipsGenerados> listaFipsGenerados = new ArrayList <FipsGenerados> ();
		
		String queryListado = "from com.mitc.redes.editorfip.entidades.rpm.gestionfip.FipsGenerados fg order by fg.fechaGeneracion DESC";
		
		listaFipsGenerados = (List<FipsGenerados>) entityManager.createQuery(queryListado).getResultList();
		
		return listaFipsGenerados;
	}
	
	public void refrescarLista()
	{
		getListado();
	}
	
	public HashMap getSeleccion() {
		return seleccion;
	}
	
	public void setSeleccion(HashMap seleccion) {
		this.seleccion = seleccion;
	}
	
	public String getIdSeleccionado() {
		return idSeleccionado;
	}

	public void setIdSeleccionado(String idSeleccionado) {
		this.idSeleccionado = idSeleccionado;
	}

	@Remove
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	public FipsGenerados getFipGenerado() {
		return fipGenerado;
	}

	public void setFipGenerado(FipsGenerados fipGenerado) {
		this.fipGenerado = fipGenerado;
	}

	public Long getIdentificador() {
		return identificador;
	}

	public void setIdentificador(Long identificador) {
		this.identificador = identificador;
	}
	
	public void mostrarInfo()
	{
		
	}
}
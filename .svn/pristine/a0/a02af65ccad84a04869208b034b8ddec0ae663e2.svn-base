package com.mitc.redes.editorfip;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.log.Log;

import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Determinacion;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Entidad;
import com.mitc.redes.editorfip.servicios.produccionfip.gestiondiccionariodeterminaciones.GestionDeterminaciones;
import com.mitc.redes.editorfip.servicios.produccionfip.gestionentidades.GestionEntidades;
import com.mitc.redes.editorfip.servicios.produccionfip.gestionentidades.GestionCondicionesUrbanisticas;
import com.mitc.redes.editorfip.servicios.produccionfip.gestionunidades.ListaUnidades;
import com.mitc.redes.editorfip.servicios.produccionfip.gestionadscripciones.GestionAdscripciones;
import com.mitc.redes.editorfip.entidades.interfazgrafico.UnidadTramiteDTO;
import com.mitc.redes.editorfip.entidades.interfazgrafico.AdscripcionesTramiteDTO;




@Stateless
@Name("testingProxy")
public  class TestingProxyBean implements TestingProxy {

	@Logger private Log log;
	
	@PersistenceContext
	EntityManager em;
	
	@In(create = true, required = false) GestionDeterminaciones gestionDeterminaciones;
	@In(create = true, required = false) GestionEntidades gestionEntidades;
	@In(create = true, required = false) GestionCondicionesUrbanisticas gestionCondicionesUrbanisticas;
	@In(create = true, required = false) ListaUnidades listaUnidades;
	@In(create = true, required = false) GestionAdscripciones gestionAdscripciones;

	
	// Gestion Determinaciones ......................................
	public Determinacion gestionDeterminaciones_getDeterminacion() {
		return gestionDeterminaciones.getDeterminacion();
	}
	public void gestionDeterminaciones_setIdDeterminacion(int idDet) {
		gestionDeterminaciones.setIdDeterminacion(idDet);
	}
	public void gestionDeterminaciones_setDeterminacion(Determinacion determinacion) {
		gestionDeterminaciones.setDeterminacion(determinacion);
	}
	public void gestionDeterminaciones_guardarDeterminacion(int idDet) {
		gestionDeterminaciones.setIdDeterminacion(idDet);
		gestionDeterminaciones.guardarDeterminacion(null);
	}

	// Gestion Entidades ...........................................
	public Entidad gestionEntidades_getEntidad() {
		return gestionEntidades.getEntidad();
	}
	public void gestionEntidades_setIdEntidad(int idEntidad) {
		gestionEntidades.setIdEntidad(idEntidad);
	}
	
	// Gestion Condiciones Urbanisticas ...............................
	public void gestionCondicionesUrbanisticas_establecerCUSeleccionada(int idReg) {
		gestionCondicionesUrbanisticas.establecerCUSeleccionada(idReg);
	}

	// Lista unidades .................................................
	public boolean listaUnidades_isDefaultAscending(String nombreColumnName) {
		return listaUnidades.isDefaultAscending(nombreColumnName);
	}

	// Gestion de adscripciones .................................................
	public void gestionAdscripciones_cargarDatosDeterminacionUnidad(UnidadTramiteDTO unidad, String volverPag) {
		gestionAdscripciones.cargarDatosDeterminacionUnidad(unidad, volverPag);
	}
	public void gestionAdscripciones_cargarDatosDeterminacionTipo(AdscripcionesTramiteDTO tipoAdscripcion, String volverPag) {
		gestionAdscripciones.cargarDatosDeterminacionTipo(tipoAdscripcion, volverPag);
	}
	public void gestionAdscripciones_setMostrarPanelEntidadOrigen(boolean mostrarPanelEntidadOrigen) { 
		gestionAdscripciones.setMostrarPanelEntidadOrigen(false);
	}
}













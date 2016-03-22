package com.mitc.redes.editorfip;

import javax.ejb.Remote;

import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Determinacion;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Entidad;
import com.mitc.redes.editorfip.entidades.interfazgrafico.UnidadTramiteDTO;
import com.mitc.redes.editorfip.entidades.interfazgrafico.AdscripcionesTramiteDTO;

@Remote
public interface TestingProxy {
	
	// GestionDeterminacion
	public Determinacion gestionDeterminaciones_getDeterminacion();
	public void gestionDeterminaciones_setIdDeterminacion(int idDet);
	public void gestionDeterminaciones_setDeterminacion(Determinacion determinacion);
	public void gestionDeterminaciones_guardarDeterminacion(int idDet);
	
	// Gestion Entidades
	public Entidad gestionEntidades_getEntidad();
	public void gestionEntidades_setIdEntidad(int idEntidad);

	// Gestion Condiciones Urbanisticas
	public void gestionCondicionesUrbanisticas_establecerCUSeleccionada(int idReg);

	// Lista unidades
	public boolean listaUnidades_isDefaultAscending(String nombreColumnName);

	// Gestion adscripciones
	public void gestionAdscripciones_cargarDatosDeterminacionUnidad(UnidadTramiteDTO unidad, String volverPag);
	public void gestionAdscripciones_cargarDatosDeterminacionTipo(AdscripcionesTramiteDTO tipoAdscripcion, String volverPag);
	public void gestionAdscripciones_setMostrarPanelEntidadOrigen(boolean mostrarPanelEntidadOrigen);
}

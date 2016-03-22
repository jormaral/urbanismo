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

package com.mitc.redes.editorfip.servicios.produccionfip.gestionentidades;

import java.util.List;

import javax.ejb.Local;
import javax.faces.event.ActionEvent;

import com.mitc.redes.editorfip.entidades.interfazgrafico.CondicionUrbanisticaSimplificadaDTO;

@Local
public interface GestionCondicionesUrbanisticas {
	
	public int caracterCUSeleccionada();
	
	public boolean isEntidadAplicadaGrupoAplicacion();
	public void copiarCUsSeleccionadasEnNuevaEntidad (List<CondicionUrbanisticaSimplificadaDTO> listaCUaCopiar, List<Integer> listaIdEntidadDestino);
	public void crearDeterminacionCUSeleccionada(int idDet);
	public void borrarRegimen (ActionEvent event);
	public void establecerCUSeleccionada (ActionEvent event);
	public void establecerCUSeleccionada (int idReg);
	public void guardarCU();
	public void cancelarCambiosCU();
	public CondicionUrbanisticaSimplificadaDTO getCuSeleccionada();
	public void setCuSeleccionada(CondicionUrbanisticaSimplificadaDTO cuSeleccionada);
	
	// CREAR GA
	public void crearGrupoAplicacionEntidadSeleccionada(int idVRGA);
	
	// CU MODIFICADA
	public boolean isCuSeleccionadaModificada();
	
	// CARGAR EN CU
	public void cargarDeterminacionRegimenCUSeleccionada(int idDeterminacionRegimen);
	public void cargarValorReferenciaCUSeleccionada(int idValorReferenciaRegimen);
	
	// VALOR
	public void cargarNuevoValor();
	public String getNuevoValor();
	public void setNuevoValor(String nuevoValor);
	
	
	// DETERMINACION REGIMEN
	public void setMostrarPanelDetReg(boolean mostrarPanelDetReg);	 
	public boolean isMostrarPanelDetReg();
	public void setIdDetRegimen(int idDetRegimen);
	public int getIdDetRegimen();
	public String selecionarDetRegimen();
	
	// VALOR REFERENCIA
	public void setMostrarPanelValRef(boolean mostrarPanelValRef);	 
	public boolean isMostrarPanelValRef();
	public void setIdValReferencia(int idValReferencia);
	public int getIdValReferencia();
	public String selecionarValReferencia();
	
	
	// REGIMENES ESPECIFICOS
	public void borrarRegimenEspecifico();
	public void cargarRegimenEspecifico();
	public String getNuevoNombreRegimenEspecifico();
	public void setNuevoNombreRegimenEspecifico(String nuevoNombreRegimenEspecifico);
	public String getNuevoTextoRegimenEspecifico();
	public void setNuevoTextoRegimenEspecifico(String nuevoTextoRegimenEspecifico);


	public void destroy();
}

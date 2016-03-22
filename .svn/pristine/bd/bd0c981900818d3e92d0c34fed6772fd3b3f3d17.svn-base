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

package com.mitc.redes.editorfip.servicios.produccionfip.gestiondiccionariodeterminaciones;

import java.util.List;

import javax.ejb.Local;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import com.mitc.redes.editorfip.entidades.interfazgrafico.UnidadDTO;
import com.mitc.redes.editorfip.entidades.interfazgrafico.UnidadTramiteDTO;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Determinacion;

@Local
public interface GestionDeterminaciones {
	
	/**
	 * Obtiene el valor de la propiedad ''
	 * 
	 * @return valor de tipo Determinacion
	 */
	public Determinacion getDeterminacion();

	/**
	 * Modifica el valor de la propiedad '@param determinacion'
	 * 
	 * @param determinacion valor de tipo 
	 */
	public void setDeterminacion(Determinacion determinacion);

	public void guardarDeterminacion(ActionEvent action);
	
	// BORRAR
	public void borrarSoloDeterminacionEHijas(ActionEvent action);
	
	public void borrarDeterminacionYCUEHijas(ActionEvent action);
	
	public void borrarSoloCUDeDeterminacion(ActionEvent action);
	
	// 
	
	public void actualizarLista(ValueChangeEvent event);
	
	public void seleccionarUnidad(UnidadTramiteDTO unidad, String volverPag);

	public void destroy();
	
	/**
	 * Obtiene el valor de la propiedad ''
	 * 
	 * @return valor de tipo int
	 */
	public int getIdDeterminacion();
	
	/**
	 * Modifica el valor de la propiedad '@param idDeterminacion'
	 * 
	 * @param idDeterminacion valor de tipo 
	 */
	public void setIdDeterminacion(int idDeterminacion);
	
	/**
	 * Obtiene el valor de la propiedad ''
	 * 
	 * @return valor de tipo boolean
	 */
	public boolean isMostrarPanelDetBase();
	
	/**
	 * Modifica el valor de la propiedad '@param mostrarPanelDetBase'
	 * 
	 * @param mostrarPanelDetBase valor de tipo 
	 */
	public void setMostrarPanelDetBase(boolean mostrarPanelDetBase);
	
	/**
	 * Obtiene el valor de la propiedad ''
	 * 
	 * @return valor de tipo int
	 */
	public int getIdDeterminacionBase();
    
    /**
     * Modifica el valor de la propiedad '@param idDeterminacionBase'
     * 
     * @param idDeterminacionBase valor de tipo 
     */
    public void setIdDeterminacionBase(int idDeterminacionBase);
    
    /**
     * Obtiene el valor de la propiedad ''
     * 
     * @return valor de tipo Determinacion
     */
    public Determinacion getDeterminacionFiltro();

	/**
	 * Modifica el valor de la propiedad '@param determinacionFiltro'
	 * 
	 * @param determinacionFiltro valor de tipo 
	 */
	public void setDeterminacionFiltro(Determinacion determinacionFiltro);
    
	/**
	 * Obtiene el valor de la propiedad ''
	 * 
	 * @return valor de tipo int
	 */
	public int getDeterminacionSel();

	/**
	 * Modifica el valor de la propiedad '@param determinacionSel'
	 * 
	 * @param determinacionSel valor de tipo 
	 */
	public void setDeterminacionSel(int determinacionSel);


    /**
     * Obtiene el valor de la propiedad ''
     * 
     * @return valor de tipo boolean
     */
    public boolean isMostrarPanelDetPadre();
	
	/**
	 * Modifica el valor de la propiedad '@param mostrarPanelDetBase'
	 * 
	 * @param mostrarPanelDetBase valor de tipo 
	 */
	public void setMostrarPanelDetPadre(boolean mostrarPanelDetBase);
	
	/**
	 * Obtiene el valor de la propiedad ''
	 * 
	 * @return valor de tipo boolean
	 */
	public boolean isMostrarPanelValRef();

	/**
	 * Modifica el valor de la propiedad '@param mostrarPanelValRef'
	 * 
	 * @param mostrarPanelValRef valor de tipo 
	 */
	public void setMostrarPanelValRef(boolean mostrarPanelValRef);
	
	/**
	 * Obtiene el valor de la propiedad ''
	 * 
	 * @return valor de tipo boolean
	 */
	public boolean isMostrarPanelGrupAp();

	/**
	 * Modifica el valor de la propiedad '@param mostrarPanelGrupAp'
	 * 
	 * @param mostrarPanelGrupAp valor de tipo 
	 */
	public void setMostrarPanelGrupAp(boolean mostrarPanelGrupAp);
	
	/**
	 * Obtiene el valor de la propiedad ''
	 * 
	 * @return valor de tipo boolean
	 */
	public boolean isMostrarPanelDetReg();

	/**
	 * Modifica el valor de la propiedad '@param mostrarPanelDetReg'
	 * 
	 * @param mostrarPanelDetReg valor de tipo 
	 */
	public void setMostrarPanelDetReg(boolean mostrarPanelDetReg);

	/**
	 * Obtiene el valor de la propiedad ''
	 * 
	 * @return valor de tipo boolean
	 */
	public boolean isMostrarPanelUnidad();

	/**
	 * Modifica el valor de la propiedad '@param mostrarPanelUnidad'
	 * 
	 * @param mostrarPanelUnidad valor de tipo 
	 */
	public void setMostrarPanelUnidad(boolean mostrarPanelUnidad);
	
	/**
	 * Obtiene el valor de la propiedad ''
	 * 
	 * @return valor de tipo boolean
	 */
	public boolean isMostrarPopUpErrorElminDet();
	
	/**
	 * Modifica el valor de la propiedad '@param mostrarPopUpErrorElminDet'
	 * 
	 * @param mostrarPopUpErrorElminDet valor de tipo 
	 */
	public void setMostrarPopUpErrorElminDet(boolean mostrarPopUpErrorElminDet);
	
	/**
	 * Obtiene el valor de la propiedad ''
	 * 
	 * @return valor de tipo int
	 */
	public int getIdDeterminacionPadre();
	
	/**
	 * Obtiene el valor de la propiedad ''
	 * 
	 * @return valor de tipo List<SelectItem>
	 */
	public List<SelectItem> getResultadosBusqueda();
	
	/**
	 * Modifica el valor de la propiedad '@param resultadosBusqueda'
	 * 
	 * @param resultadosBusqueda valor de tipo 
	 */
	public void setResultadosBusqueda(List<SelectItem> resultadosBusqueda);
    
    /**
     * Modifica el valor de la propiedad '@param idDeterminacionBase'
     * 
     * @param idDeterminacionBase valor de tipo 
     */
    public void setIdDeterminacionPadre(int idDeterminacionBase);
    
    /**
     * Obtiene el valor de la propiedad ''
     * 
     * @return valor de tipo UnidadDTO
     */
    public UnidadDTO getUnidadDeDeterminacion();
    
    public void guardarUnidadDeDeterminacion();
	
	public void borrarUnidadDeDeterminacion();
	
	/**
	 * Obtiene el valor de la propiedad ''
	 * 
	 * @return valor de tipo boolean
	 */
	public boolean isRenderizarUnidadDeDeterminacion();

	/**
	 * Modifica el valor de la propiedad '@param unidadDeDeterminacion'
	 * 
	 * @param unidadDeDeterminacion valor de tipo 
	 */
	public void setUnidadDeDeterminacion(UnidadDTO unidadDeDeterminacion);
    
    public void mostrarPanelDetPadreAL(ActionEvent actionEvent);	
    
    public void mostrarPanelValRefAL(ActionEvent actionEvent);
    
    public void mostrarPanelDetBaseAL(ActionEvent actionEvent);	
    
    public void mostrarPanelGrupApAL(ActionEvent actionEvent);
    
    /**
     * Obtiene el valor de la propiedad ''
     * 
     * @return valor de tipo boolean
     */
    public boolean isMostrarRegulacionesEspecificas();
    
    /**
     * Modifica el valor de la propiedad '@param mostrarRegulacionesEspecificas'
     * 
     * @param mostrarRegulacionesEspecificas valor de tipo 
     */
    public void setMostrarRegulacionesEspecificas(boolean mostrarRegulacionesEspecificas);
	
    /**
     * Obtiene el valor de la propiedad ''
     * 
     * @return valor de tipo boolean
     */
    public boolean isMostrarPanelFiltros();
	/**
	 * Modifica el valor de la propiedad '@param mostrarPanelFiltros'
	 * 
	 * @param mostrarPanelFiltros valor de tipo 
	 */
	public void setMostrarPanelFiltros(boolean mostrarPanelFiltros);
	
	/**
	 * Obtiene el valor de la propiedad ''
	 * 
	 * @return valor de tipo boolean
	 */
	public boolean isFiltroNombre();
	/**
	 * Modifica el valor de la propiedad '@param filtroNombre'
	 * 
	 * @param filtroNombre valor de tipo 
	 */
	public void setFiltroNombre(boolean filtroNombre);
	
	/**
	 * Obtiene el valor de la propiedad ''
	 * 
	 * @return valor de tipo boolean
	 */
	public boolean isFiltroApartado();
	/**
	 * Modifica el valor de la propiedad '@param filtroApartado'
	 * 
	 * @param filtroApartado valor de tipo 
	 */
	public void setFiltroApartado(boolean filtroApartado);
	
	/**
	 * Obtiene el valor de la propiedad ''
	 * 
	 * @return valor de tipo boolean
	 */
	public boolean isFiltroEtiqueta();
	/**
	 * Modifica el valor de la propiedad '@param filtroEtiqueta'
	 * 
	 * @param filtroEtiqueta valor de tipo 
	 */
	public void setFiltroEtiqueta(boolean filtroEtiqueta);
	
	public void aceptarFiltros(ActionEvent event);
	public void mostrarPanel();
	
	public void actualizarListaDeterminacion(ValueChangeEvent event);
	
	public void prepararParaCrear(ActionEvent actionEvent);
	
	public void cancelar();
	
	public void desasociarUnidadDeDeterminacion();

}

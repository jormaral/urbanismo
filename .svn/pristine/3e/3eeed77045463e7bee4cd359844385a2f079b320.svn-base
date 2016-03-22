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

package com.mitc.redes.editorfip.servicios.gestionfip.gestionplaneamientoencargado;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Remove;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import com.mitc.redes.editorfip.entidades.rpm.gestionfip.PlanesEncargados;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Plan;

/**
 * Servicio de gestión de planes encargados.
 * 
 * @author fguerrero
 *
 */
@Local
public interface GestionPlaneamientoEncargado {
		
	@Remove
	public void remove();
	
	/**
	 * Método(evento) que se encarga de mostrar el panel de 
	 * asignacion de ámbitos.
	 * 
	 * @param actionEvent parametro obligatorio para lanzar el evento.
	 */
	public void mostrarPanelAmbitosAL(ActionEvent actionEvent);
	
	/**
	 * Obtiene el valor de la propiedad 'mostrarPanelÁmbitos'
	 * 
	 * @return valor en formato Integer.
	 */
	public boolean isMostrarPanelAmbitos();
	
	/**
	 * Obtiene el valor de la propiedad 'idAmbSel'
	 * 
	 * @return
	 */
	public Integer getIdAmbSel();
	
	/**
	 * Descarta los cambios en el formulario cargando 
	 * los datos originales del plan encargado.
	 * 
	 */
	public void descartarCambios();
	
	/**
	 * Carga los datos de un plan.
	 * 
	 * @param iden ID del plan a cargar.
	 */
	public void cargarPlan(String iden);
	
	/**
	 * Selecciona un ambito y lo asocia al plan encargado.
	 * 
	 * @param idAmbitoSel ID del ambito a asociar.
	 */
	public void seleccionarAmbito(Integer idAmbitoSel);
	
	/**
	 * Elimina la relacion de un ambito con el plan encargado.
	 * 
	 * @param idAmbito ID del ambito a eliminar.
	 */
	public void eliminarAmbito(Integer idAmbito);
	
	/**
	 * Obtiene el nombre de un ambito mediante su ID
	 * 
	 * @param idAmbito ID del ambito a buscar en formato Integer.
	 * @return
	 */
	public String obtenerNombreAmbito(Integer idAmbito);
	
	/**
	 * Obtiene el valor de la propiedad 'planEncargado'
	 * 
	 * @return valor en formato
	 */
	public PlanesEncargados getPlanEncargado();
	
	/**
	 * Modifica el valor de la propiedad 'mostrarPanelAmbitos'
	 * 
	 * @param mostrarPanelAmbitos valor de tipo Boolean.
	 */
	public void setMostrarPanelAmbitos(boolean mostrarPanelAmbitos);
	
	
	/**
	 * Obtiene el valor de la propiedad 'literalAmbito'
	 * 
	 * @return valor de tipo String
	 */
	public String getLiteralAmbito();
	
	/**
	 * Obtiene el valor de la propiedad 'planesBase'
	 * 
	 * @return valor de tipo List<SelectItem>
	 */
	public List<SelectItem> getPlanesBase();
	
	/**
	 * Obtiene el valor de la propiedad 'ambitoConPlanBase'
	 * 
	 * @return valor de tipo boolean
	 */
	public boolean isAmbitoConPlanBase();
	
	/**
	 * Obtiene el valor de la propiedad 'generarPlanVigente'
	 * 
	 * @return valor de tipo Boolean
	 */
	public boolean isGenerarPlanVigente();
	
	/**
	 * Obtiene el valor de la propiedad 'planBase'
	 * 
	 * @return valor de tipo Plan
	 */
	public Plan getPlanBase();
	
	
	/**
	 * Obtiene el valor de la propiedad 'planVigente'
	 * 
	 * @return valor de tipo Plan
	 */
	public Plan getPlanVigente();
	
	/**
	 * Obtiene el valor de la propiedad 'planesVigentes'
	 * 
	 * @return valor de tipo List<SelectItem>
	 */
	public List<SelectItem> getPlanesVigentes();
	
	/**
	 * Guarda los datos del plan encargado en BD. 
	 * 
	 */
	public String guardar();
	
	/**
	 * Obtiene el valor de la propiedad '@listaInstrumentos'
	 * 
	 * @return valor de tipo List<SelectItem>
	 */
	public List<SelectItem> getListaInstrumentos();
	
	/**
	 * Obtiene el valor de la propiedad 'listaTiposTramite'
	 * 
	 * @return valor de tipo List<SelectItem>
	 */
	public List<SelectItem> getListaTiposTramite();
	
	/**
	 * Obtiene el valor de la propiedad 'modificacion'
	 * 
	 * @return valor de tipo boolean
	 */
	public boolean isModificacion();
	
	
	/**
	 * Obtiene el valor de la propiedad 'literalInstrumento'
	 * 
	 * @return valor de tipo String
	 */
	public String getLiteralInstrumento();
	
	/**
	 * Obtiene el valor de la propiedad 'literalTipoTramite'
	 * 
	 * @return valor de tipo String
	 */
	public String getLiteralTipoTramite();
	
	
	/**
	 * Obtiene el valor de la propiedad 'proyecciones'
	 * 
	 * @return valor de tipo List<SelectItem>
	 */
	public List<SelectItem> getProyecciones();
	
	public String borrar();
	
}

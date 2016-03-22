/*
* Copyright 2011 red.es
* Autores: Arnaiz Consultores.
*
** Licencia con arreglo a la EUPL, Versión 1.1 o -en cuanto
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
package es.mitc.redes.urbanismoenred.servicios.validacion;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.HashMap;
import java.util.Map;

/**
 * Clase que encapsula información sobre el proceso de validación iniciado
 * para un trámite.
 * 
 * @author Arnaiz Consultores
 *
 */
public class ContextoValidacion {
	
	public static final String CODIGO_FIP = "codigoFip";
	public static final String ERROR = "validacion.error";
	public static final String ESTADO = "validacion.estado";
	public static final String ESTADO_INICIAL = "validacion.estado.inicial";
	public static final String ID_VALIDACION = "validacion.id";
	public static final String NOMBRE_PLAN = "plan.nombre";
	public static final String PROGRESO = "validacion.estado.progreso";
	public static final String RESULTADO_VALIDACION = "validacion.resultado";
	public static final String RUTA_FIP = "fip.path";
	public static final String TIPO_VALIDACION = "validacion.tipo";
	public static final String USUARIO = "validacion.usuario";
	public static final String VALIDACIONES_TOTAL = "validacion.total";
	public static final String VALIDACIONES_ACTUAL = "validacion.actual";

	private Estado estado = Estado.PENDIENTE;
	private Map<String, Object> parametros = new HashMap<String, Object>();
	private PropertyChangeSupport soporteEventos;
	private int totalValidaciones;
	private int validacionActual;

	/**
	 * 
	 * @param codigoFip
	 */
	public ContextoValidacion(String codigoFip) {
		parametros.put(CODIGO_FIP, codigoFip);
		parametros.put(PROGRESO, 0);
		
		soporteEventos = new PropertyChangeSupport(this);
	}
	
	/**
	 * 
	 * @param codigoFip
	 * @param parametros
	 */
	public ContextoValidacion(String codigoFip, Map<String, Object> parametros) {
		this(codigoFip);
		this.parametros.putAll(parametros);
	}
	
	/**
	 * 
	 * @param listener
	 */
	public void agregarListener(PropertyChangeListener listener) {
		soporteEventos.addPropertyChangeListener(listener);
	}
	
	/**
	 * 
	 * @param propiedad
	 * @param listener
	 */
	public void agregarListener(String propiedad, PropertyChangeListener listener) {
		soporteEventos.addPropertyChangeListener(propiedad, listener);
	}
	
	/**
	 * 
	 * @param listener
	 */
	public void eliminarListener(PropertyChangeListener listener) {
		soporteEventos.removePropertyChangeListener(listener);
	}
	
	/**
	 * 
	 * @param propiedad
	 * @param listener
	 */
	public void eliminarListener(String propiedad, PropertyChangeListener listener) {
		soporteEventos.removePropertyChangeListener(propiedad, listener);
	}
	
	
	/**
	 * 
	 * @return
	 */
	public String getCodigoFip() {
		return (String)parametros.get(CODIGO_FIP);
	}

	/**
	 * @return the estado
	 */
	public Estado getEstado() {
		return estado;
	}
	
	/**
	 * 
	 * @param clave
	 * @return
	 */
	public Object getParametro(String clave) {
		return parametros.get(clave);
	}
	
	/**
	 * @return the parametros
	 */
	public Map<String, Object> getParametros() {
		return parametros;
	}

	/**
	 * @return the totalValidaciones
	 */
	public int getTotalValidaciones() {
		return totalValidaciones;
	}

	/**
	 * @return the validacionActual
	 */
	public int getValidacionActual() {
		return validacionActual;
	}
	
	/**
	 * 
	 * @param clave
	 * @param valor
	 */
	public void putParametro(String clave, Object valor) {
		
		PropertyChangeEvent evt = new PropertyChangeEvent(this, clave, parametros.get(clave), valor);
		parametros.put(clave, valor);
		soporteEventos.firePropertyChange(evt);
	}

	/**
	 * @param estado the estado to set
	 */
	public void setEstado(Estado estado) {
		PropertyChangeEvent evt = new PropertyChangeEvent(this, ESTADO, this.estado, estado);
		this.estado = estado;
		soporteEventos.firePropertyChange(evt);
	}

	/**
	 * @param totalValidaciones the totalValidaciones to set
	 */
	public void setTotalValidaciones(int totalValidaciones) {
		PropertyChangeEvent evt = new PropertyChangeEvent(this, VALIDACIONES_TOTAL, this.totalValidaciones, totalValidaciones);
		this.totalValidaciones = totalValidaciones;
		soporteEventos.firePropertyChange(evt);
	}

	/**
	 * @param validacionActual the validacionActual to set
	 */
	public void setValidacionActual(int validacionActual) {
		PropertyChangeEvent evt = new PropertyChangeEvent(this, VALIDACIONES_ACTUAL, this.validacionActual, validacionActual);
		this.validacionActual = validacionActual;
		soporteEventos.firePropertyChange(evt);
	}
	
}

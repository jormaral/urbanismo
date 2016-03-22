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
package es.mitc.redes.urbanismoenred.servicios.refundido;

import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.List;

import es.mitc.redes.urbanismoenred.servicios.seguridad.Usuario;

/**
 * Un contexto de refundido contiene toda la información requerida para 
 * ejecutar el refundido de una serie de trámites.
 * 
 * @author Arnaiz Consultores
 *
 */
public interface ContextoRefundido {
	static enum LOG {AVISO, ERROR, INFO}
	
	final static String AMBITO ="refundido.ambito.id";
	final static String EJECUTAR ="operaciones.ejecutar";
	final static String ESQUELETO_TRAMITE_REFUNDIDO = "planeamiento.tramite.refundido";
	final static String ESTADO = "refundido.estado";
	final static String ID_PROCESO ="refundido.proceso.iden";
	final static String ID_TRAMITES_BASE ="refundido.tramites.base.iden";
	final static String IDIOMA ="refundido.idioma";
	final static String TRAMITE_REFUNDIDO = "refundido.tramite.refundido";
	final static String NOMBRE_AMBITO_REFUNDIDO = "refundido.ambito.nombre";
	final static String TRADUCTOR = "refundido.traductor";
	final static String NIVEL_ERRORES = "refundido.errores.nivel";
	
	/**
	 * 
	 * @param listener
	 */
	void agregarListener(PropertyChangeListener listener);
	
	/**
	 * 
	 * @param propiedad
	 * @param listener
	 */
	void agregarListener(String propiedad, PropertyChangeListener listener);
	
	/**
	 * 
	 * @param listener
	 */
	void eliminarListener(PropertyChangeListener listener);
	
	/**
	 * 
	 * @param propiedad
	 * @param listener
	 */
	void eliminarListener(String propiedad, PropertyChangeListener listener);
	
	/**
	 * 
	 */
	void finalizarProceso();
	
	/**
	 * 
	 * @return
	 */
	File getArchivoLog();
	
	/**
	 * 
	 * @return
	 */
	Estado getEstado();
	
	/**
	 * 
	 * @return
	 */
	int getIdAmbito();
	
	/**
	 * 
	 * @return
	 */
	String getIdioma();
	
	/**
	 * 
	 * @return
	 */
	File getFIP();
	
	/**
	 * 
	 * @param clave
	 * @return
	 */
	Object getParametro(String clave);
	
	/**
	 * 
	 * @return
	 */
	List<Integer> getTramites();
	
	/**
	 * 
	 * @return
	 */
	Usuario getUsuario();
	
	/**
	 * 
	 * @throws ExcepcionRefundido
	 */
	void iniciarProceso() throws ExcepcionRefundido;
	
	/**
	 * 
	 * @return
	 */
	boolean isCrearTramite();
	
	/**
	 * Escribe el texto traducido en el fichero de log. 
	 * 
	 * 
	 * @param tipo
	 * @param mensaje Identificador de la cadena de texto que se va a insertar.
	 * Este identificador debe existir en el fichero utilizado para las 
	 * traducciones.
	 * @param valores
	 */
	void logTraducido(LOG tipo, String mensaje, Object... valores);
	
	/**
	 * Escribe el texto en el fichero de log. 
	 * 
	 * Esto se podría delegar en LOG4J, pero debido a la implementación de 
	 * JBoss es imposible asignar un appender de forma dinámica.
	 * 
	 * @param tipo
	 * @param mensaje
	 */
	void log(LOG tipo, String mensaje);
	
	/**
	 * 
	 * @param clave
	 * @param valor
	 */
	void putParametro(String clave, Object valor);
	
	/**
	 * 
	 * @param idioma
	 */
	void setIdioma(String idioma);
	
	/**
	 * 
	 * @param idAmbito
	 */
	void setIdAmbito(int idAmbito);
	
	/**
	 * 
	 * @param fip
	 */
	void setFIP(File fip);
}

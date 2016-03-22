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
package es.mitc.redes.urbanismoenred.servicios.seguridad;
import java.io.File;

import javax.ejb.Local;

import es.mitc.redes.urbanismoenred.data.rpm.diccionario.Centroproduccion;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Tramite;

/**
 * 
 * @author Arnaiz Consultores
 *
 */
@Local
public interface GestorFip1Local {
	
	/**
	 * Genera el archivo FIP1 del trámite cuyo código FIP coincida con el 
	 * especificado como parámetro. El FIP1 contiene:
	 * 		El diccionario de datos
	 * 		Planes Base
	 * 		Plan Refundido (si lo hubiera)
	 * 		Plan encargado
	 * 
	 * @param codigoFip Código FIP del trámite
	 * @param idioma Código del idioma en el que se genera el FIP.
	 * @return Archivo donde se ha guardado el FIP1 generado.
	 * @throws ExcepcionSeguridad
	 */
	File generarFip1(String codigoFip, String idioma) throws ExcepcionSeguridad;
	/**
	 * Genera el FIP1 plantilla del ámbito indicado. El FIP1 plantilla es un
	 * FIP que contiene:
	 * 		El diccionario de datos
	 * 		Planes base
	 * 		Plan refundido del ámbito (si lo hubiera)
	 * @param idAmbito Identificador del ámbito para el que se genera la 
	 * plantilla.
	 * @param idioma Código del idioma en el que se genera el FIP.
	 * @param soloXML Indica si devuelve sólo el XML o el ZIP con el XML y sus 
	 * documentos asociados.
	 * 
	 * @return Archivo comprimido con la información de la plantilla.
	 * @throws ExcepcionSeguridad
	 */
	File getPlantilla(int idAmbito, String idioma, boolean soloXML) throws ExcepcionSeguridad;
	
	/**
	 * Devuelve el centro de producción cuyo identificador coincide con el
	 * pasado como parámetro.
	 * 
	 * @param centroProduccion Identificador del centro de producción.
	 * @return Centro de produccion o valor nulo si no hay ninguno con ese
	 * identificador.
	 */
	Centroproduccion getCentroProduccion(int centroProduccion);
	
	/**
	 * Devuelve los centros de producción dados de alta en el sistema.
	 * 
	 * @return Lista de los centros de producción.
	 */
	Centroproduccion[] getCentrosProduccion();
	
	/**
	 * Devuelve los trámites para los que un centro de producción puede 
	 * descargar Fip1.
	 * 
	 * @param centroProduccion
	 * @return
	 */
	Tramite[] getTramites(int centroProduccion);
	
	/**
	 * Marca los FIP1 como obsoletos. Si se especifica el ámbito marca todos
	 * los FIP1 de ese ámbito como obsoletos. Si se especifica código FIP sólo
	 * se marcan como obsoletos los FIP1 de ese trámite.
	 * 
	 * Si no se especifica ni ámbito ni código FIP se marcan todos como obsoletos.
	 * 
	 * @param ambito Identificador del ámbito.
	 * @param codigoFip Código FIP del trámite.
	 */
	void marcarComoObsoleto(Integer ambito, String codigoFip);
}

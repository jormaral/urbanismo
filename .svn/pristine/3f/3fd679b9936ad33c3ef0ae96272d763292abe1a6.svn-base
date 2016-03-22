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

import javax.ejb.Local;

import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Tramite;
import es.mitc.redes.urbanismoenred.servicios.seguridad.Usuario;

/**
 * 
 * @author Arnaiz Consultores
 *
 */
@Local
public interface GestorContextosValidacionLocal {
	/**
	 * Devueleve el contexto de validación asociado al código de trámite 
	 * especificado.
	 * 
	 * @param codigoFip Código FIP del trámite.
	 * @return Contexto de validación asociado al trámite o valor nulo si no
	 * existe.
	 */
	ContextoValidacion getContexto(String codigoFip);
	
	/**
	 * Devuelve todos los contextos de validación asociados a un usuario.
	 * 
	 * @param usuario
	 * @return
	 */
	ContextoValidacion[] getContextos(Usuario usuario);
	
	/**
	 * 
	 * @param tramite
	 * @return
	 * @throws ExcepcionValidacion
	 */
	ContextoValidacion agregarContexto(Tramite tramite) throws ExcepcionValidacion;
}

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
package es.mitc.redes.urbanismoenred.servicios.planeamiento;

/**
 * Enumerado que permite definir qué modalidad de planes se puede requerir a la
 * hora de recuperar información sobre planes.
 * 
 * @author Arnaiz Consultores
 *
 */
public enum ModalidadPlanes {
	VALIDACION("VALIDACION"),
	RPM("RPM"),
	REF("REF"),
	FICHAS("FICHAS"),
	TODOS("TODOS");
	
	private String codigo;
	
	private ModalidadPlanes(String codigo) {
		this.codigo = codigo;
	}
	
	public String getCodigo() {
		return codigo;
	}
}

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

/**
 * @author Arnaiz Consultores
 *
 */
public enum TipoSubsistema {
	VALIDACION (1, "validación"),
    CONSOLIDACION (2,"consolidación"),
    REFUNDIDO (5,"refundido"),
    PLANBASE (6, "gestión del plan base"),
    GESTION_USUARIOS (7, "gestion de usuarios"),
    PRODUCTOR (8, "web del productor"),
    LOGIN (9, "Login en el sistema"),
    GESTION_DICCIONARIO (10, "Mto. diccionarios"),
    DESCARGA_FIP1 (11,"Descarga FIP 1");
	
	private int codigo;
	private String descripcion;
	
	private TipoSubsistema(int codigo, String descripcion) {
		this.codigo = codigo;
		this.descripcion = descripcion;
	}
	
	public int getCodigo() {
		return codigo;
	}
	
	public String getDescripcion() {
		return descripcion;
	}
}

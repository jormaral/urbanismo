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

/**
 * 
 * @author Arnaiz Consultores
 *
 */
public enum TipoValidacion {
	TODAS(0, "Validación completa"), 
	CONDICIONES_URBANISTICAS(1, "Validación de condiciones urbanísticas"), 
	DETERMINACIONES(2, "Validación de determinaciones"), 
	ENTIDADES(3,"Validación de entidades"),
	TRAMITE(4, "Validación del trámite"), 
	DOCUMENTOS (5, "Validación de documentos"), 
	OTRAS(6, "Otras validaciones"),
	INTEGRIDAD(7,"Validación de integridad");
	
	private int codigo;
	private String descripcion;
	
	private TipoValidacion(int codigo, String descripcion) {
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

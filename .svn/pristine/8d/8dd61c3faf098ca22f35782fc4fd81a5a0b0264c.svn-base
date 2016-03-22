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
 * @author Arnaiz Consultores
 *
 */
public enum Estado {
		PENDIENTE(0,"Pendiente"),
		SUBIENDO(5,"Subiendo"),
		SUBIDO(10,"Subido"), 
		DESPLEGANDO(15,"Descomprimiendo FIP"),
		DESPLEGADO(20,"Desplegado"),
		GUARDANDO(25,"Guardando"),
		GUARDADO(30,"Guardado"), 
		VALIDANDO(40,"Validando"), 
		VALIDACION_ERRONEA(50,"Validación errónea"), 
		VALIDADO(60, "Validado"), 
		CONSOLIDADO(70, "Consolidado");
	
	private int codigo;
	private String descripcion;
	
	private Estado(int codigo, String descripcion) {
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

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
 * 
 * @author Arnaiz Consultores
 *
 */
public class Ambito {
	
	private int codigo;
	private Integer idTipo;
	private String nombre;
	private String tipo;

	public Ambito(int codigo, String nombre, Integer idTipo, String tipo) {
		this.codigo = codigo;
		this.nombre = nombre;
		this.tipo = tipo;
	}
	
	public int getCodigo() {
		return codigo;
	}
	
	public Integer getIdTipo() {
		return idTipo;
	}

	public String getNombre() {
		return nombre;
	}

	public String getTipo() {
		return tipo;
	}
}

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
 * Tipos de roles existentes en base de datos.
 * 
 * @author Arnaiz Consultores
 *
 */
public enum TipoRol {
    CONSOLA ("CON", "Consola", true),
    VALIDACION ("VAL","Validación", true),
    CONSOLIDACION ("CSD","Consolidación", true),
    REFUNDIDO ("REF", "Refundido", true),
    FICHAS("FIC", "Configurador de Fichas", true),
    ADMINISTRADOR ("ADM", "Administrador", false),
    GESTOR_PLAN ("PLA", "Gestión de Planes", true),
    CONFIGURADOR ("CFV", "Configurador Visor", true);
	
	private String codigo;
	private String descripcion;
	private boolean ambito;
	
	private TipoRol(String codigo, String descripcion, boolean ambito) {
		this.codigo = codigo;
		this.descripcion = descripcion;
		this.ambito = ambito;
	}
	
	public boolean necesitaAmbito() {
		return ambito;
	}
	
	public String getCodigo() {
		return codigo;
	}
	
	public String getDescripcion() {
		return descripcion;
	}
}

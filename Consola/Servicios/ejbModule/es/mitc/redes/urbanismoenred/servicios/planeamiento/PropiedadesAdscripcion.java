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

import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Determinacion;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Entidad;

/**
 * @author Arnaiz Consultores
 *
 */
public class PropiedadesAdscripcion {
	private int iden;
    private double cuantia;
    private String texto;
    private Determinacion unidad;
    private Determinacion tipo;
    private Entidad origen;
    private Entidad destino;
    
    public PropiedadesAdscripcion() {
    	
    }
    
	public PropiedadesAdscripcion(int iden, double cuantia, String texto,
			Determinacion unidad, Determinacion tipo, Entidad origen,
			Entidad destino) {
		super();
		this.iden = iden;
		this.cuantia = cuantia;
		this.texto = texto;
		this.unidad = unidad;
		this.tipo = tipo;
		this.origen = origen;
		this.destino = destino;
	}
	/**
	 * @return the iden
	 */
	public int getIden() {
		return iden;
	}
	/**
	 * @return the cuantia
	 */
	public double getCuantia() {
		return cuantia;
	}
	/**
	 * @return the texto
	 */
	public String getTexto() {
		return texto;
	}
	/**
	 * @return the unidad
	 */
	public Determinacion getUnidad() {
		return unidad;
	}
	/**
	 * @return the tipo
	 */
	public Determinacion getTipo() {
		return tipo;
	}
	/**
	 * @return the origen
	 */
	public Entidad getOrigen() {
		return origen;
	}
	/**
	 * @return the destino
	 */
	public Entidad getDestino() {
		return destino;
	}
	/**
	 * @param iden the iden to set
	 */
	public void setIden(int iden) {
		this.iden = iden;
	}
	/**
	 * @param cuantia the cuantia to set
	 */
	public void setCuantia(double cuantia) {
		this.cuantia = cuantia;
	}
	/**
	 * @param texto the texto to set
	 */
	public void setTexto(String texto) {
		this.texto = texto;
	}
	/**
	 * @param unidad the unidad to set
	 */
	public void setUnidad(Determinacion unidad) {
		this.unidad = unidad;
	}
	/**
	 * @param tipo the tipo to set
	 */
	public void setTipo(Determinacion tipo) {
		this.tipo = tipo;
	}
	/**
	 * @param origen the origen to set
	 */
	public void setOrigen(Entidad origen) {
		this.origen = origen;
	}
	/**
	 * @param destino the destino to set
	 */
	public void setDestino(Entidad destino) {
		this.destino = destino;
	}
}

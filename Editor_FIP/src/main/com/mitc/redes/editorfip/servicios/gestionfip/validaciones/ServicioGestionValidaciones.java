/*
* Copyright 2011 red.es
* Autores: IDOM Consulting
*
* Licencia con arreglo a la EUPL, Versi�n 1.1 o -en cuanto
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

package com.mitc.redes.editorfip.servicios.gestionfip.validaciones;

import java.util.List;

import javax.ejb.Local;

import org.jboss.seam.annotations.async.Asynchronous;

import com.mitc.redes.editorfip.entidades.rpm.validaciones.ElementoValidado;
import com.mitc.redes.editorfip.entidades.rpm.validaciones.ProcesoValidacion;

@Local
public interface ServicioGestionValidaciones {

	
	// ------------------------------
    // GLOBAL
    // ------------------------------	
	
	@Asynchronous
    public void validarTramiteAsincrono (int idTramite, ProcesoValidacion procesoValidacion);
	
	public void validarTramite(int idTramite);
	
	public List<ElementoValidado> obtenerValidacionesProceso(int idProceso, String filtro);
	
	// ------------------------------
    // VALIDACIONES TRAMITE
    // ------------------------------

	public List<ElementoValidado> validacionesTramite(int idTramite, ProcesoValidacion procVal);

	public List<ElementoValidado> validacion211(int idTramite, ProcesoValidacion procVal);

	public List<ElementoValidado> validacion212(int idTramite, ProcesoValidacion procVal);

	public List<ElementoValidado> validacion213(int idTramite, ProcesoValidacion procVal);

	// ------------------------------
    // VALIDACIONES DOCUMENTOS
    // ------------------------------	
	
	public List<ElementoValidado> validacionesDocumentos(int idTramite, ProcesoValidacion procVal);

	public List<ElementoValidado> validacion221(int idTramite, ProcesoValidacion procVal);

	public List<ElementoValidado> validacion222(int idTramite, ProcesoValidacion procVal);

	
	// ------------------------------
    // VALIDACIONES ENTIDADES
    // ------------------------------	
	
	public List<ElementoValidado> validacionesEntidades(int idTramite, ProcesoValidacion procVal);

	public List<ElementoValidado> validacion231(int idTramite, ProcesoValidacion procVal);

	public List<ElementoValidado> validacion232(int idTramite, ProcesoValidacion procVal);

	public List<ElementoValidado> validacion233(int idTramite, ProcesoValidacion procVal);

	public List<ElementoValidado> validacion234(int idTramite, ProcesoValidacion procVal);

	public List<ElementoValidado> validacion235(int idTramite, ProcesoValidacion procVal);

	public List<ElementoValidado> validacion236(int idTramite, ProcesoValidacion procVal);

	public List<ElementoValidado> validacion237(int idTramite, ProcesoValidacion procVal);

	public List<ElementoValidado> validacion238(int idTramite, ProcesoValidacion procVal);

	public List<ElementoValidado> validacion239(int idTramite, ProcesoValidacion procVal);

	
	// ------------------------------
    // VALIDACIONES DETERMINACIONES
    // ------------------------------	
	
	public List<ElementoValidado> validacionesDeterminaciones(int idTramite, ProcesoValidacion procVal);

	public List<ElementoValidado> validacion241(int idTramite, ProcesoValidacion procVal);

	public List<ElementoValidado> validacion242(int idTramite, ProcesoValidacion procVal);

	public List<ElementoValidado> validacion243(int idTramite, ProcesoValidacion procVal);

	public List<ElementoValidado> validacion244(int idTramite, ProcesoValidacion procVal);

	public List<ElementoValidado> validacion245(int idTramite, ProcesoValidacion procVal);

	public List<ElementoValidado> validacion246(int idTramite, ProcesoValidacion procVal);

	public List<ElementoValidado> validacion247(int idTramite, ProcesoValidacion procVal);

	public List<ElementoValidado> validacion248(int idTramite, ProcesoValidacion procVal);

	public List<ElementoValidado> validacion249(int idTramite, ProcesoValidacion procVal);

	public List<ElementoValidado> validacion2410(int idTramite, ProcesoValidacion procVal);

	public List<ElementoValidado> validacion2411(int idTramite, ProcesoValidacion procVal);

	
	// ------------------------------
    // VALIDACIONES CONDICIONES URBANISTICAS
    // ------------------------------	
	
	public List<ElementoValidado> validacionesCondicionesUrbanisticas(int idTramite, ProcesoValidacion procVal);

	public List<ElementoValidado> validacion251(int idTramite, ProcesoValidacion procVal);

	public List<ElementoValidado> validacion252(int idTramite, ProcesoValidacion procVal);

	public List<ElementoValidado> validacion253(int idTramite, ProcesoValidacion procVal);

	public List<ElementoValidado> validacion254(int idTramite, ProcesoValidacion procVal);

	public List<ElementoValidado> validacion255(int idTramite, ProcesoValidacion procVal);

	public List<ElementoValidado> validacion256(int idTramite, ProcesoValidacion procVal);

	public List<ElementoValidado> validacion257(int idTramite, ProcesoValidacion procVal);

	public List<ElementoValidado> validacion258(int idTramite, ProcesoValidacion procVal);

	public List<ElementoValidado> validacion259(int idTramite, ProcesoValidacion procVal);

	public List<ElementoValidado> validacion2510(int idTramite, ProcesoValidacion procVal);
	
	// ------------------------------
    // VALIDACIONES OPERACIONES
    // ------------------------------	

	public List<ElementoValidado> validacionesOperaciones(int idTramite, ProcesoValidacion procVal);

	public List<ElementoValidado> validacion261(int idTramite, ProcesoValidacion procVal);

	public List<ElementoValidado> validacion262(int idTramite, ProcesoValidacion procVal);

	public List<ElementoValidado> validacion263(int idTramite, ProcesoValidacion procVal);

	public List<ElementoValidado> validacion264(int idTramite, ProcesoValidacion procVal);

	
	// ------------------------------
    // VALIDACIONES ADSCRIPCIONES
    // ------------------------------	
	
	public List<ElementoValidado> validacionesAdscripciones(int idTramite, ProcesoValidacion procVal);

	public List<ElementoValidado> validacion271(int idTramite, ProcesoValidacion procVal);
	


}

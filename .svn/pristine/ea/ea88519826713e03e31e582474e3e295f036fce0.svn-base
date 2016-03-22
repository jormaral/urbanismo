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

package com.mitc.redes.editorfip.servicios.procesamientofip.generacionfip2;

import java.util.List;
import java.util.Set;

import javax.ejb.Local;
import javax.faces.event.ActionEvent;

import org.jboss.seam.annotations.async.Asynchronous;

import com.mitc.redes.editorfip.entidades.fipxml.RegulacionEspecifica;
import com.mitc.redes.editorfip.entidades.rpm.gestionfip.FipsGenerados;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Determinacion;
import com.mitc.redes.editorfip.servicios.genericos.ArbolGenericoObject;
@Local
public interface ConversionFIPXML 
{
	
	public FipsGenerados guardarFIPenBD(int idTramite);
	
	public void postGenerarFip(ActionEvent ae);
	
	@Asynchronous
	public void generarFip(List<ArbolGenericoObject> seleccionados);
	
	@Asynchronous
	public void crearFIP2Asincrono(int idTramite, FipsGenerados fipGenerado);
	
	public String nombreAmbitoDelTramite (int idTramite);
	
	/**
	 * Obtiene el valor de la propiedad ''
	 * 
	 * @return valor de tipo Set<RegulacionEspecifica>
	 */
	public Set<RegulacionEspecifica> getRegulacionEspecifica(Determinacion det) ;
}
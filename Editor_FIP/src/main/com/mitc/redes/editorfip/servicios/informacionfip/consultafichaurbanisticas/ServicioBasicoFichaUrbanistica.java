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

package com.mitc.redes.editorfip.servicios.informacionfip.consultafichaurbanisticas;

import java.util.List;
import java.util.Set;

import javax.ejb.Local;

import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;

import com.mitc.redes.editorfip.entidades.fipxml.RegulacionEspecifica;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Determinacion;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Entidad;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Plan;
import com.vividsolutions.jts.geom.Geometry;

@Local
public interface ServicioBasicoFichaUrbanistica {

	
	
	public List<Plan> findPlanesGeom(String WKT);
	
	public Entidad findForFicha(Object id);
	
	/**
	 * Obtiene el valor de la propiedad ''
	 * 
	 * @return valor de tipo Set<RegulacionEspecifica>
	 */
	public Set<RegulacionEspecifica> getNodosRegulacionEspecificaDeDet(Determinacion det);
	
	/**
	 * Obtiene el valor de la propiedad ''
	 * 
	 * @return valor de tipo DocumentFragment
	 */
	public DocumentFragment getEntidades(Geometry aGeo,Document aXML);
	
	public DocumentFragment getEntidadesTodas(int idTramite, Document aXML);
	
	 public DocumentFragment getDeterminacionesTodas(int idTramite, Document aXML);
}

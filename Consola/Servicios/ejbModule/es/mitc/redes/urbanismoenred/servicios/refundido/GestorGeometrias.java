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
package es.mitc.redes.urbanismoenred.servicios.refundido;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import es.mitc.redes.urbanismoenred.data.rpm.refundido.Entidad;

/**
 * Session Bean implementation class GestorGeometrias
 * 
 * @author Arnaiz Consultores
 */
@Stateless
public class GestorGeometrias implements GestorGeometriasLocal {
	
	private static final String SELECT_INTERSECCION_POLIGONOS = "Select e2.iden From refundido.Entidad e2 " +
            "refundido.entidadpol eg1, refundido.entidadpol eg2 " +
            "Where eg2.identidad=e2.iden " +
            "And eg1.identidad= %d " +
            "And eg2.identidad IN (%s) " +
            "And Intersects(eg1.geom, eg2.geom)=true ";
	
	@PersistenceContext(unitName = "rpmv2")
	private EntityManager em;

    /**
     * Default constructor. 
     */
    public GestorGeometrias() {
    }

    /*
     * (non-Javadoc)
     * @see es.mitc.redes.urbanismoenred.servicios.refundido.GestorGeometriasLocal#calcularInterseccion(es.mitc.redes.urbanismoenred.data.rpm.refundido.Entidad, java.util.List)
     */
	@SuppressWarnings("unchecked")
	@Override
	public Entidad[] calcularInterseccion(
			Entidad origen,
			List<Integer> entidades) {
		
		StringBuffer sb = new StringBuffer();
		
		for(Integer id : entidades) {
			if (sb.length() > 0) {
				sb.append(",");
			}
			sb.append(id);
		}
		
		List<Integer> eInterseccion;
		List<Entidad> resultado = new ArrayList<>();
		Entidad intersectada;
		if (origen.getEntidadpols().size() > 0) {
			 eInterseccion =em.createNativeQuery(String.format(SELECT_INTERSECCION_POLIGONOS, origen.getIden(), sb.toString()))
            		.getResultList();
			 for(Integer idEntidad : eInterseccion) {
				 intersectada = em.find(Entidad.class, idEntidad);
				 if (!resultado.contains(intersectada)) {
					 resultado.add(intersectada);
				 }
			 }
    	}
		
		return resultado.toArray(new Entidad[0]);
	}

}

/*
* Copyright 2011 red.es
* Autores: IDOM Consulting
*
* Licencia con arreglo a la EUPL, Versiï¿½n 1.1 o -en cuanto
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


package com.mitc.redes.editorfip.servicios.basicos.comunes;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;

@Stateless

@Name("servicioBusquedaGenerica")
public class ServicioBusquedaGenericaBean implements ServicioBusquedaGenerica {
	
	@In EntityManager entityManager;
	
	public HashMap buscarEnTodosCampos(String entidad, String filtro, int pagina, int maxResul) throws ClassNotFoundException {
		
		Class claseEntidad = Class.forName(entidad);
		List<Field> campos = Arrays.asList(claseEntidad.getFields());
		HashMap resultados = buscarEnCamposConcretos(entidad, filtro, campos, pagina, maxResul);
		return resultados;
	}

	@SuppressWarnings("unchecked")
	public HashMap buscarEnCamposConcretos(String entidad, String filtro, List<Field> campos, int pagina, int maxResul) throws ClassNotFoundException {
		
		List<Object> lista = new ArrayList<Object>();
		List<Field> camposEntidad = campos;
		
		HashMap resultados = new HashMap();
		
		// Variable que se utilizará para calcular los indices de paginación
		Long totalRegistros = 0L;
		
		String stringConsulta = "FROM " + entidad + " WHERE ";
		for(int i=0; i<camposEntidad.size(); i++) {
			
			stringConsulta += camposEntidad.get(i).getName() + " LIKE '%" + filtro +"%' ";
			if(i < (camposEntidad.size() - 1))
				stringConsulta += " OR ";
		}
		 // Una vez construida la consulta, calculamos el total de registros (antes de que sea paginada).
		totalRegistros = (Long)entityManager.createQuery("SELECT COUNT (*) " + stringConsulta).getSingleResult();
		
		// Verificamos si la pagina concuerda con el numero max de resultados por pagina
		Double paginasD = new Double(((float)totalRegistros) / ((float)maxResul));
		int numPaginas = (int)Math.ceil(paginasD);
    	if(numPaginas == 0)
    		numPaginas = 1;
    	if(pagina > numPaginas)
    		pagina = numPaginas;
		
		// Ejecutamos la consulta paginada
		Query consulta = entityManager.createQuery(stringConsulta);
		consulta.setMaxResults(maxResul)
				 .setFirstResult((pagina -1) * maxResul); 
		lista = consulta.getResultList();
		
		resultados.put("resultados", lista);
		resultados.put("totalRegistros", totalRegistros);		
		
		return resultados;
	}
}

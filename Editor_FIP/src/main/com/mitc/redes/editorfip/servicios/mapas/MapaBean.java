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

package com.mitc.redes.editorfip.servicios.mapas;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.log.Log;

import com.mitc.redes.editorfip.entidades.rpm.gestionfip.PlanesEncargados;
import com.mitc.redes.editorfip.servicios.sesion.VariablesSesionUsuario;


@Stateless
@Name("mapa")

public class MapaBean implements Mapa {
	
	@Logger private Log log;
   
    @PersistenceContext
	EntityManager em;
    
    @In(create = true, required= false)
    VariablesSesionUsuario variablesSesionUsuario;
    
    public int i=5;
    
    public int getI(){
    	return i;
    }
    
	public double xmaxFromEntity(int id) {
		
		double result;
		
		
		String hql = "SELECT ST_XMax(geometria) FROM planeamiento.entidades_capa WHERE identidad =" + id;
		List<Double> lxmax = em.createNativeQuery(hql).getResultList();
		if(lxmax.size()!=1)
			result = 886101.428;	
		else
			result = lxmax.get(0);
		return result;
		
		
	    }



	public double xminFromEntity(int id) {
		
		double result;
		
		String hql1 = "SELECT ST_XMin(geometria) FROM planeamiento.entidades_capa WHERE identidad =" + id;
		List<Double> lxmin = em.createNativeQuery(hql1).getResultList();
		if(lxmin.size()!=1)
			result = 875261.25;	
		else
			result = lxmin.get(0);
		return result;
	}

	public double ymaxFromEntity(int id) {
		
		double result;
		
		String hql2 = "SELECT ST_YMax(geometria) FROM planeamiento.entidades_capa WHERE identidad =" + id;
		List<Double> lymax = em.createNativeQuery(hql2).getResultList();
		if(lymax.size()!=1)
			result = 4322055.5;	
		else
			result = lymax.get(0);
		return result;
	}

	public double yminFromEntity(int id) {
		double result;
		
		String hql3 = "SELECT ST_YMin(geometria) FROM planeamiento.entidades_capa WHERE identidad =" + id;
		List<Double> lymin = em.createNativeQuery(hql3).getResultList();
		if(lymin.size()!=1)
			result = 4312128;	
		else
			result = lymin.get(0);
		return result;
		
	}
	
	public String projection(int id){
		
		String result = null;
		//String hql = "SELECT t.proyeccion FROM planeamiento.entidades_capa e, gestionfip.planesencargados t WHERE e.idtramite = t.tramiteencargado_iden AND e.identidad =" + id;
		
		String hql = "SELECT distinct t.proyeccion FROM planeamiento.entidad e, planeamiento.tramite tram, planeamiento.plan plan, gestionfip.planesencargados t "+
						" WHERE e.idtramite = tram.iden and tram.idplan = plan.iden and plan.idambito=t.ambito_iden AND e.iden="+ id;		
		List<String> lista = em.createNativeQuery(hql).getResultList();
		if(lista.size()!=1)
			result = "EPSG:23030";	
		else
			result = lista.get(0);
		return result;
	}
	
public String projectionTramite(int id, String layer){
		
		log.debug("[projectionTramite] id="+id+" layer="+layer);
		String result = "ERROR";
		
		int idTramiteEncargado = variablesSesionUsuario.getIdTramiteEncargadoTrabajo();
    	
    	// Obtengo el PlaneamientoEncargado
    	String queryPlaneamientoEncargado="select pe from PlanesEncargados pe where pe.tramiteEncargado.iden="+idTramiteEncargado;
    	PlanesEncargados planEncargado = (PlanesEncargados)em.createQuery(queryPlaneamientoEncargado).getSingleResult();
    	result = planEncargado.getProyeccion();
		
    	log.debug("[projectionTramite] result="+result);
		return result;
	}
	

public String existGeom(int id){
		
		String result = null;
		String hql = "SELECT t.proyeccion FROM planeamiento.entidades_capa e, gestionfip.planesencargados t WHERE e.idtramite = t.tramiteencargado_iden AND e.identidad =" + id;
		List<String> lista = em.createNativeQuery(hql).getResultList();
		if(lista.size()!=1)
			result = " ";	
		else
			result = " .(Esta entidad no tiene Geometría)";
		return result;
	}
	
public String existGeomTramite(int id, String layer){
	
	String result = null;
	String hql = "SELECT t.proyeccion FROM planeamiento.ambito_"+layer+" e, gestionfip.planesencargados t WHERE e.idtramite = t.tramiteencargado_iden AND e.idtramite =" + id;
	List<String> lista = em.createNativeQuery(hql).getResultList();
	if(lista.size()!=1)
		result = " ";	
	else
		result = " .(Esta entidad no tiene Geometría)";
	return result;
}

public List<Double> getBoundsFromAmbito(int id){
	
	String hql = "SELECT ST_xmin(ST_Extent(geom)) as bextent FROM planeamiento.ambito_"+id+" UNION ALL SELECT ST_ymin(ST_Extent(geom)) as bextent FROM planeamiento.ambito_"+id+" UNION ALL SELECT ST_xmax(ST_Extent(geom)) as bextent FROM planeamiento.ambito_"+id+" UNION ALL SELECT ST_ymax(ST_Extent(geom)) as bextent FROM planeamiento.ambito_"+id ;
	List<Double> lista = em.createNativeQuery(hql).getResultList();
	return lista;

}

@Override
public int obtenerAmbitoDeTramite(int idTramite) {

	int idAmbito;
	String hql = "select ent.iden from planeamiento.entidad ent JOIN planeamiento.entidaddeterminacion ON ent.iden = entidaddeterminacion.identidad JOIN planeamiento.casoentidaddeterminacion ON entidaddeterminacion.iden = casoentidaddeterminacion.identidaddeterminacion JOIN planeamiento.entidaddeterminacionregimen ON casoentidaddeterminacion.iden = entidaddeterminacionregimen.idcaso JOIN planeamiento.opciondeterminacion ON entidaddeterminacionregimen.idopciondeterminacion = opciondeterminacion.iden JOIN planeamiento.determinacion  ON opciondeterminacion.iddeterminacionvalorref = determinacion.iden JOIN planeamiento.determinacion determinacionaplicada ON entidaddeterminacion.iddeterminacion = determinacionaplicada.iden where ent.idtramite = "+idTramite+" and determinacion.codigo='7000000002'";
		
	List<Integer> lista = em.createNativeQuery(hql).getResultList();
	
	if(lista.size()==1)
		idAmbito=lista.get(0);
	else
		idAmbito=0;
	
	log.debug("[obtenerAmbitoDeTramite] El ambito de idTramite="+idTramite+" es la entidad:"+idAmbito);
	return idAmbito;
}
}
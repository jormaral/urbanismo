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

package com.mitc.redes.editorfip.servicios.gestionfip.importadores;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.ejb.Remove;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.log.Log;

import com.mitc.redes.editorfip.entidades.fipxml.RegulacionEspecifica;
import com.mitc.redes.editorfip.entidades.interfazgrafico.CUAdaptadaSipuDTO;
import com.mitc.redes.editorfip.entidades.interfazgrafico.CUExcelDTO;
import com.mitc.redes.editorfip.entidades.interfazgrafico.CondicionUrbanisticaSimplificadaDTO;
import com.mitc.redes.editorfip.entidades.interfazgrafico.RegimenesEspecificosSimplificadosDTO;
import com.mitc.redes.editorfip.entidades.rpm.gestionfip.CUAdaptadaSipu;
import com.mitc.redes.editorfip.entidades.rpm.gestionfip.RegimenesEspecificosSimplificadosDeterminacion;
import com.mitc.redes.editorfip.entidades.rpm.gestionfip.RegimenesEspecificosSimplificadosUsoActos;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Determinacion;
import com.mitc.redes.editorfip.servicios.basicos.fip.determinaciones.ServicioBasicoDeterminaciones;
import com.mitc.redes.editorfip.servicios.basicos.fip.entidades.ServicioBasicoCondicionesUrbanisticas;
import com.mitc.redes.editorfip.servicios.sesion.VariablesSesionUsuario;

/**
 * Session Bean implementation class ImportadorCUExcel
 */
@Stateless
@Name("importadorCUCanariasExcel")
public class ImportadorCUCanariasExcelBean implements ImportadorCUCanariasExcel {

	@PersistenceContext
    private EntityManager em;
    
	@Logger 
	private Log log;
	
	@In (create = true, required = false)
	VariablesSesionUsuario variablesSesionUsuario;
    
    Map<String, String> mapaGrupo = new HashMap<String, String>();

    
    // Creacion del contexto
    @Resource
    private SessionContext contextoTransaccion;
    
    @In(create = true, required = false)
	ServicioBasicoCondicionesUrbanisticas servicioBasicoCondicionesUrbanisticas;
    
    @In(create = true, required = false)
	ServicioBasicoDeterminaciones servicioBasicoDeterminaciones;
    
    public ImportadorCUCanariasExcelBean() {
		// TODO Auto-generated constructor stub
    	
    	mapaGrupo.put("amb", "7000000002");
        mapaGrupo.put("cla", "7000000003");
        mapaGrupo.put("cat", "7000000004");
        
        mapaGrupo.put("zon", "7000000011");
        mapaGrupo.put("ges", "7000000016");
        mapaGrupo.put("afe", "7000000031");
        
        mapaGrupo.put("pro", "7000000013");
        mapaGrupo.put("sis", "7000000008");
        mapaGrupo.put("res", "7000000030");
        mapaGrupo.put("acc", "7000000043");
	}
    
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public List<String> persistirCUdeExcel (int idTramite, List<CUAdaptadaSipu> listaCuenExcel)
    {
    	List<String> listaErroresPersistir = new ArrayList<String>();
    	
    	
    	
    	return listaErroresPersistir;
    }
    
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public List<String> persistirCUdeExcelRus (int idTramite, List<CUAdaptadaSipu> listaCuenExcel)
    {
    	List<String> listaErroresPersistir = new ArrayList<String>();
    	
    	Iterator it = listaCuenExcel.iterator();
    	
    	
    	// Primero hay que borrar los elementos del tramite seleccionado
    	borrarElementosCuAdaptada(variablesSesionUsuario.getIdTramiteTrabajoActual());
    	
    	while(it.hasNext())
    	{
    		CUAdaptadaSipu cuAdaptada = (CUAdaptadaSipu) it.next();
    		
    		Iterator itReg = cuAdaptada.getRegEspecificoDeterminacion().iterator();
    		em.persist(cuAdaptada);
    		while (itReg.hasNext()){
    			RegimenesEspecificosSimplificadosDeterminacion regEsp = (RegimenesEspecificosSimplificadosDeterminacion) itReg.next();
    			regEsp.setCuAdaptadaSipu(cuAdaptada);
    			em.persist(regEsp);
    		}
    		
    		Iterator itRegUso = cuAdaptada.getRegEspecificoUsoActo().iterator();
    		while (itRegUso.hasNext()){
    			RegimenesEspecificosSimplificadosUsoActos regEsp = (RegimenesEspecificosSimplificadosUsoActos) itRegUso.next();
    			regEsp.setCuAdaptadaSipu(cuAdaptada);
    			em.persist(regEsp);
    		}
    		
    		//em.persist(cuAdaptada);
    	}
    	
    	em.flush();
    	return listaErroresPersistir;
    }
    
    public int buscarEntidadPorTramiteYClave (int idTramite, String claveEntidad)
    {
    	int idEnt=0;
    	
    	// Compruebo si ya existe una Entidaddeterminacion con esa determinacion y esa entidad
    	String query="SELECT ent.iden " +
        " FROM com.mitc.redes.editorfip.entidades.rpm.planeamiento.Entidad ent " + 
        " WHERE ent.tramite.iden =  " +idTramite+
        " AND ent.clave = '"+claveEntidad+"'";
    	
    	try
    	{
    		idEnt = (Integer)em.createQuery(query).getSingleResult();
    	}
    	catch (Exception ex)
    	{
    		log.warn("[buscarEntidadPorTramiteYClave] Fallo al buscar  la entidad con idTramite="+idTramite+" y clave="+claveEntidad +". ERROR:"+ex.getMessage());
    		ex.printStackTrace();
    	}
    	
    	
    	
    	return idEnt;
    }
    
    private static String quitarTildesLower(String cadena)
	{
		////System.outout.println(cadena);
		if (cadena!=null)
		{
			cadena = cadena.replace ('á','a');
			cadena = cadena.replace ('é','e');
			cadena = cadena.replace ('í','i');
			cadena = cadena.replace ('ó','o');
			cadena = cadena.replace ('ú','u');
			
			return cadena.toLowerCase().trim();
		}
		else
			return "";
	}
    
    public int buscarDeterminacionPorTramiteYApartado (int idTramite, String apartado)
    {
    	int idDet=0;
    	
    	String[] numApartados = apartado.split("\\.");
		 
		String queryNueva="";
		
		// Construyo la query para que rescate la determinacion a partir del apartado
		for (int i=(numApartados.length-1); i>-1; i--)
		{
			if (i==0)
			{
				queryNueva+= " SELECT d.iden " +
		         " FROM com.mitc.redes.editorfip.entidades.rpm.planeamiento.Determinacion d " +
		         " WHERE d.determinacionByIdpadre IS NULL AND d.tramite.iden = "+idTramite+
		         " AND d.orden = '"+numApartados[0]+"'";
				
				for (int j=0; j<numApartados.length; j++)
				{
					queryNueva+= ")";
				}
			}
			else
			{
				queryNueva+=" SELECT d"+i+".iden " +
		         " FROM com.mitc.redes.editorfip.entidades.rpm.planeamiento.Determinacion d"+i+" " +
		         " WHERE  d"+i+".tramite.iden = "+idTramite+" AND d"+i+".orden = '"+numApartados[i]+"' " +
		         " AND d"+i+".determinacionByIdpadre = (";
			}
		}
    	
		// Lanzo la query para que encuentre la determinacion
    	try
    	{
    		idDet = (Integer)em.createQuery(queryNueva).getSingleResult();
    	}
    	catch (Exception ex)
    	{
    		log.warn("[buscarDeterminacionPorTramiteYApartado] Fallo al buscar  la determinacion con idTramite="+idTramite+" y apartado="+apartado +". ERROR:"+ex.getMessage());
    		ex.printStackTrace();
    	}
    	
    	
    	
    	return idDet;
    }
    
    public void borrarElementosCuAdaptada(int idTramite)
    {
    	String sql = "delete from cuadaptadasipu_regimenesespecificossimplificadosdeterminacion where cuadaptadasipu_id in (select id from gestionfip.cuadaptadasipu where idtramite=" + idTramite + ")";
		em.createNativeQuery(sql).executeUpdate();
		
		sql = "delete from cuadaptadasipu_regimenesespecificossimplificadosusoactos where cuadaptadasipu_id in (select id from gestionfip.cuadaptadasipu where idtramite=" + idTramite + ")";
		em.createNativeQuery(sql).executeUpdate();
		
		sql = "delete from gestionfip.regimenesespecificossimplificadosdeterminacion where cuadaptadasipu_id in (select id from gestionfip.cuadaptadasipu where idtramite=" + idTramite + ")";
		em.createNativeQuery(sql).executeUpdate();
		
		sql = "delete from gestionfip.regimenesespecificossimplificadosusoactos where cuadaptadasipu_id in (select id from gestionfip.cuadaptadasipu where idtramite=" + idTramite + ")";
		em.createNativeQuery(sql).executeUpdate();
		
		sql = "delete from gestionfip.cuadaptadasipu where idtramite=" + idTramite;
		em.createNativeQuery(sql).executeUpdate();
		
		em.flush();
		
		
    }
    
    @Remove
	public void destroy(){};

}

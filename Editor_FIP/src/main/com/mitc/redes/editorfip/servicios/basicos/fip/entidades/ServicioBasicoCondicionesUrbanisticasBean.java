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

package com.mitc.redes.editorfip.servicios.basicos.fip.entidades;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.log.Log;

import com.mitc.redes.editorfip.entidades.busqueda.BusquedaCondicionDTO;
import com.mitc.redes.editorfip.entidades.busqueda.FiltrosDTO;
import com.mitc.redes.editorfip.entidades.interfazgrafico.CondicionUrbanisticaSimplificadaDTO;
import com.mitc.redes.editorfip.entidades.interfazgrafico.DeterminacionDTO;
import com.mitc.redes.editorfip.entidades.interfazgrafico.RegimenesEspecificosSimplificadosDTO;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Casoentidaddeterminacion;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Determinacion;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Determinaciongrupoentidad;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Entidad;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Entidaddeterminacion;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Entidaddeterminacionregimen;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Opciondeterminacion;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Regimenespecifico;
import com.mitc.redes.editorfip.servicios.basicos.fip.determinaciones.ServicioBasicoDeterminaciones;

@Stateless
@Name("servicioBasicoCondicionesUrbanisticas")
public class ServicioBasicoCondicionesUrbanisticasBean implements ServicioBasicoCondicionesUrbanisticas
{
    @Logger private Log log;
    
    @PersistenceContext
	EntityManager em;
    
    @In(create = true, required = false)
    ServicioBasicoDeterminaciones servicioBasicoDeterminaciones;
   
	
	// ------------------------------
    // CONDICIONES URBANISTICAS
    // ------------------------------	
    
    public List<DeterminacionDTO> obtenerDeterminacionesAplicablesComoCUPorTramite (int idEntidad, int idTramite)
    {
    	log.debug("[obtenerDeterminacionesAplicablesComoCU] Se van a obtener las determinaciones aplicables como CU para La entidad="+idEntidad+ " el tramite es:"+idTramite);

    	List<DeterminacionDTO> listaDeterminacionesCU=new ArrayList<DeterminacionDTO>();
    	   	
    	
    	// Obtengo el VR del Grupo de Entidad Aplicado en la determinacion
		 int idVRGAEntidad = obtenerVRdeCUdeEntidadAplicadaAGA(idEntidad);
		 
		 // Obtengo la determinacion para obtener el codigo
		 
		 Determinacion detVRGA = em.find(Determinacion.class, idVRGAEntidad);
    	
    	// Obtengo las Determinaciones que pueden ser CU
		// Codigos Caracter Usados:
        // Enunciado = 000001
        // Valor de Referencia = 000012
        // Tipo de Adscripcion = 000019
        // Unidad = 000018
        // Enunciado Complementario = 000011
        // Regulacion = 0000013
		String queryDeterminacionConCU = "select det.iden,det.nombre from planeamiento.determinacion det, planeamiento.determinacion det2, planeamiento.determinaciongrupoentidad dgrupo "+
										" where det.iden = dgrupo.iddeterminacion "+
										" and det.idtramite = "+idTramite+
										" and dgrupo.iddeterminaciongrupo = det2.iden "+
										" and det2.codigo = '"+detVRGA.getCodigo()+"'"+
										" and det.idcaracter!=1 "+
										" and det.idcaracter!=11 "+
										" and det.idcaracter!=12 "+
										" and det.idcaracter!=13 "+
										" and det.idcaracter!=18 "+
										" and det.idcaracter!=19 " +
										" order by det.orden, det.nombre asc";
		
		 
		
    	 try {
         	
    		 List<Object[]> listaDeterminacionConCU = (List<Object[]>)em.createNativeQuery(queryDeterminacionConCU).getResultList();
    		 log.debug("[obtenerDeterminacionesAplicablesComoCU] "+listaDeterminacionConCU.size()+" elementos de CU para el tramite ="+idTramite+ " y codigo VR="+ detVRGA.getCodigo());
    		 
    		 // Obtengo las CU que ya aplican a esa entidad para no incluirlas
    		 List<CondicionUrbanisticaSimplificadaDTO> listadoCUYaAplicadas = listadoTodasCUSimplificadaDeEntidad (idEntidad);
    		 
    		 boolean cuExistente = false;
    		 
    		 
    		 for (Object[] detIdenNombre : listaDeterminacionConCU)
    		 {
    			 cuExistente = false;
    			 
    			 for (int i=0; i<listadoCUYaAplicadas.size()&!cuExistente;i++)
            	 {
            		 if ((Integer)detIdenNombre[0]==listadoCUYaAplicadas.get(i).getIdDeterminacion())
            		 {
            			 cuExistente = true;
            			 log.debug("[obtenerDeterminacionesAplicablesComoCU] No se va a añadir a la lista de CU la det="+(Integer)detIdenNombre[0]);
            			 
            		 }
            	 }
            	 
            	 // Si no existe la CU, puede ser añadida a la lista si cumple las condiciones
            	 if (!cuExistente)
            	 {
            		 DeterminacionDTO unTram = new DeterminacionDTO();
                	 
                	 unTram.setSeleccionada(false);
                	 unTram.setIdDeterminacion((Integer)detIdenNombre[0]);
                	 unTram.setNombreDeterminacion((String)detIdenNombre[1]);
                	 unTram.setOrdenArbol(servicioBasicoDeterminaciones.obtenerOrdenCompletoDeterminacion((Integer)detIdenNombre[0]));
                	 
                	 listaDeterminacionesCU.add(unTram);
                	 log.debug("[obtenerDeterminacionesAplicablesComoCU] Añadimos a la lista de CU la det="+unTram.getIdDeterminacion());
            	 }
    		 }
    		 
          
            
         } catch (NoResultException e) {
             log.error("[obtenerDeterminacionesAplicablesComoCU] No se han encontrado determinaciones aplicables como CU para el tramite:"+idTramite+"\n" + e.getMessage());

         } catch (Exception ex) {
             log.error("[obtenerDeterminacionesAplicablesComoCU] Error en la busqueda de determinaciones aplicables como CU para el tramite:"+idTramite+"\n" + ex.getMessage());
             ex.printStackTrace();
         }
    	
    	
         log.debug("[obtenerDeterminacionesAplicablesComoCU] Se devuelven "+listaDeterminacionesCU.size()+" determinaciones aplicables como CU para el Tramite: idTramite="+idTramite);
    	return listaDeterminacionesCU;
    }
    
    public List<DeterminacionDTO> obtenerDeterminacionesAplicablesComoCU (int idEntidad)
    {
    	log.debug("[obtenerDeterminacionesAplicablesComoCU] Se van a obtener las determinaciones aplicables como CU para la entidad="+idEntidad);

    	List<DeterminacionDTO> listaDeterminacionesCU=new ArrayList<DeterminacionDTO>();
    	
    	// Averiguo cual es el tramite de esa entidad
    	Entidad ent = em.find(Entidad.class, idEntidad);
    	
    	int idTramite = ent.getTramite().getIden();
    	log.debug("[obtenerDeterminacionesAplicablesComoCU] La entidad="+idEntidad+ " el tramite es:"+idTramite);
    	
    	// Obtengo el VR del Grupo de Entidad Aplicado en la determinacion
		 int idVRGAEntidad = obtenerVRdeCUdeEntidadAplicadaAGA(idEntidad);
    	
    	// Obtengo las Determinacion raices que pueden ser CU
		// Codigos Caracter Usados:
        // Enunciado = 000001
        // Valor de Referencia = 000012
        // Tipo de Adscripcion = 000019
        // Unidad = 000018
        // Enunciado Complementario = 000011
        // Regulacion = 0000013
		String queryDeterminacionConCU = "select det.iden,det.nombre from planeamiento.determinacion det, planeamiento.determinaciongrupoentidad dgrupo "+
										" where det.iden = dgrupo.iddeterminacion "+
										" and det.idtramite = "+idTramite+
										" and dgrupo.iddeterminaciongrupo = "+idVRGAEntidad+
										" and det.idcaracter!=1 "+
										" and det.idcaracter!=11 "+
										" and det.idcaracter!=12 "+
										" and det.idcaracter!=13 "+
										" and det.idcaracter!=18 "+
										" and det.idcaracter!=19 " +
										" order by det.orden, det.nombre asc";
		
		 
		
    	 try {
         	
    		 List<Object[]> listaDeterminacionConCU = (List<Object[]>)em.createNativeQuery(queryDeterminacionConCU).getResultList();
               		
    		 
    		 // Obtengo las CU que ya aplican a esa entidad para no incluirlas
    		 List<CondicionUrbanisticaSimplificadaDTO> listadoCUYaAplicadas = listadoTodasCUSimplificadaDeEntidad (idEntidad);
    		 
    		 boolean cuExistente = false;
    		 
    		 
    		 for (Object[] detIdenNombre : listaDeterminacionConCU)
    		 {
    			 for (int i=0; i<listadoCUYaAplicadas.size()&!cuExistente;i++)
            	 {
            		 if ((Integer)detIdenNombre[0]==listadoCUYaAplicadas.get(i).getIdDeterminacion())
            		 {
            			 cuExistente = true;
            			 log.debug("[obtenerDeterminacionesAplicablesComoCU] No se va a añadir a la lista de CU la det="+(Integer)detIdenNombre[0]);
            			 
            		 }
            	 }
            	 
            	 // Si no existe la CU, puede ser añadida a la lista si cumple las condiciones
            	 if (!cuExistente)
            	 {
            		 DeterminacionDTO unTram = new DeterminacionDTO();
                	 
                	 unTram.setSeleccionada(false);
                	 unTram.setIdDeterminacion((Integer)detIdenNombre[0]);
                	 unTram.setNombreDeterminacion((String)detIdenNombre[1]);
                	 unTram.setOrdenArbol(servicioBasicoDeterminaciones.obtenerOrdenCompletoDeterminacion((Integer)detIdenNombre[0]));
                	 
                	 listaDeterminacionesCU.add(unTram);
                	 log.debug("[obtenerDeterminacionesAplicablesComoCU] Añadimos a la lista de CU la det="+unTram.getIdDeterminacion());
            	 }
    		 }
    		 
          
            
         } catch (NoResultException e) {
             log.error("[obtenerDeterminacionesAplicablesComoCU] No se han encontrado determinaciones aplicables como CU para el tramite:"+idTramite+"\n" + e.getMessage());

         } catch (Exception ex) {
             log.error("[obtenerDeterminacionesAplicablesComoCU] Error en la busqueda de determinaciones aplicables como CU para el tramite:"+idTramite+"\n" + ex.getMessage());
             ex.printStackTrace();
         }
    	
    	
         log.debug("[obtenerDeterminacionesAplicablesComoCU] Se devuelven "+listaDeterminacionesCU.size()+" determinaciones aplicables como CU para el Tramite: idTramite="+idTramite);
    	return listaDeterminacionesCU;
    }
    
	 public List<CondicionUrbanisticaSimplificadaDTO> listadoTodasCUSimplificadaDeEntidad (int idEntidad)
	    {
		 
		 log.debug("listadoTodasCUSimplificadaDeEntidad] busco CU para entidad="+idEntidad);
		 
	    	List<CondicionUrbanisticaSimplificadaDTO> cuSimplList = new ArrayList<CondicionUrbanisticaSimplificadaDTO> ();
	    	
	    	
	    	// Compruebo si ya existe una Entidaddeterminacion con esa determinacion y esa entidad
	    	String query="SELECT cuReg.iden," +
	    			"cuReg.casoentidaddeterminacionByIdcaso.entidaddeterminacion.entidad.iden," +
	         		"cuReg.casoentidaddeterminacionByIdcaso.entidaddeterminacion.entidad.nombre," +
		      		"cuReg.casoentidaddeterminacionByIdcaso.entidaddeterminacion.determinacion.iden," +
		      		"cuReg.casoentidaddeterminacionByIdcaso.entidaddeterminacion.determinacion.nombre " +		     		
					" FROM Entidaddeterminacionregimen cuReg " +      
				    " WHERE cuReg.casoentidaddeterminacionByIdcaso.entidaddeterminacion.entidad = "+ idEntidad +
				    " ORDER BY cuReg.iden ASC";
		
	    	
	    	try
	    	{
	    		List<Object> regimenesCUTramite = em.createQuery(query).getResultList();
	    		log.info("[listadoTodasCUSimplificadaDeEntidad] Regimenes de la entidad= "+idEntidad+".  Numero regimenes="+regimenesCUTramite.size());
	    		
	    		for (Object regCUObjList : regimenesCUTramite)
	    		{
	    			// Por cada objeto creo un nuevo registro de CondicionUrbanisticaSimplificadaDTO
	    			
	    			CondicionUrbanisticaSimplificadaDTO cuSimpl = new CondicionUrbanisticaSimplificadaDTO();
	    			
	    			Object[] regCUArrayObj = (Object[]) regCUObjList;
	    			
	    			// El indice 1 se corresponde al id de la entidad de la CU
	    			cuSimpl.setIdEntidad((Integer)regCUArrayObj[1]);
	    			
	    			// El indice 2 se corresponde al nombre de la entidad de la CU
	    			cuSimpl.setNombreEntidad((String)regCUArrayObj[2]);
	    			
	    			// El indice 3 se corresponde al id de la determinacion de la CU
	    			cuSimpl.setIdDeterminacion((Integer)regCUArrayObj[3]);
	    			
	    			// El indice 4 se corresponde al nombre de la determinacion de la CU
	    			cuSimpl.setNombreDeterminacion((String)regCUArrayObj[4]);
	    			
	    			// El indice 4 se corresponde al id del regimen
	    			int idRegimen = (Integer)regCUArrayObj[0];
	    			cuSimpl.setIdRegimen(idRegimen);
	    			
	    			// Obtengo el regimen de la CU
	    			Entidaddeterminacionregimen cuReg = em.find(Entidaddeterminacionregimen.class, idRegimen);
	    			
	    			// Almaceno el valor
	    			cuSimpl.setValor(cuReg.getValor());
	    			
	    			// Almaceno, si existe, el nombre y id del valor de referencia
	    			if (cuReg.getOpciondeterminacion()!=null)
	    			{
	    				if (cuReg.getOpciondeterminacion().getDeterminacionByIddeterminacionvalorref()!=null)
		    			{
	    					cuSimpl.setIdDeterminacionValorReferencia(cuReg.getOpciondeterminacion().getDeterminacionByIddeterminacionvalorref().getIden());
	    					cuSimpl.setNombreDetValorRef(cuReg.getOpciondeterminacion().getDeterminacionByIddeterminacionvalorref().getNombre());
	    			
		    			}
	    			}
	    			
	    			// Almaceno, si existe, el nombre y id de la determinacion de regimen
	    			if (cuReg.getDeterminacion()!=null)
	    			{
	    				cuSimpl.setIdDeterminacionRegimen(cuReg.getDeterminacion().getIden());
	    				cuSimpl.setNombreDetRegimen(cuReg.getDeterminacion().getNombre());
	    			}
	    			
	    			// Obtengo los regimenes especificos del regimen
	    			Set<Regimenespecifico> regimenespecificos = cuReg.getRegimenespecificos();
	    			
	    			Iterator regespecificosIt = regimenespecificos.iterator();
	    			
	    			List<RegimenesEspecificosSimplificadosDTO> regEspecificosSimplList = new ArrayList<RegimenesEspecificosSimplificadosDTO> ();
	    			
	    			while (regespecificosIt.hasNext())
	    			{
	    				Regimenespecifico regesp = (Regimenespecifico)regespecificosIt.next();
	    				RegimenesEspecificosSimplificadosDTO regEspSimpl = new RegimenesEspecificosSimplificadosDTO(regesp.getNombre(), regesp.getTexto());
	    				regEspSimpl.setIdRegimenEspecifico(regesp.getIden());
	    				regEspecificosSimplList.add(regEspSimpl);
	    			}
	    			
	    			// Almaceno los regimenes especificos en el objeto a devolver
	    			cuSimpl.setRegimenesEspecificos(regEspecificosSimplList);
	    			
	    			
	    			cuSimplList.add(cuSimpl);
	    			
	    			
	    		}
	    		
	    		
	    	}
	    	catch (Exception ex)
	    	{
	    		log.error("[listadoTodasCUSimplificadaDeEntidad] Fallo al buscar los Regimenes del tramite. ERROR:"+ex.getMessage());
	    		ex.printStackTrace();
	    	}
	    	
	    	log.debug("listadoTodasCUSimplificadaDeEntidad] Obtenidas "+cuSimplList.size()+" CU para entidad="+idEntidad);
	    	return cuSimplList;
	    }
	
	
   
	 
	  public String nombreEntidad (int idEntidad)
	    {
	    	String nombreEnt = "";
	    	
	    	// Compruebo si ya existe una Entidaddeterminacion con esa determinacion y esa entidad
	    	String query="SELECT ent.nombre " +
	         " FROM Entidad ent " + 
	         " WHERE ent.iden = "+idEntidad ;
	    	
	    	try
	    	{
	    		nombreEnt = (String)em.createQuery(query).getSingleResult();
	    	}
	    	catch (Exception ex)
	    	{
	    		log.warn("[nombreEntidad] Fallo al buscar el nombre de la entidad con id="+idEntidad+". ERROR:"+ex.getMessage());
	    		ex.printStackTrace();
	    	}
	    	
	    	
	    	
	    	return nombreEnt;
	    }
	    
	    public String nombreDeterminacion (int idDeterminacion)
	    {
	    	String nombreEnt = "";
	    	
	    	// Compruebo si ya existe una Entidaddeterminacion con esa determinacion y esa entidad
	    	String query="SELECT ent.nombre " +
	         " FROM Determinacion ent " + 
	         " WHERE ent.iden = "+idDeterminacion ;
	    	
	    	try
	    	{
	    		nombreEnt = (String)em.createQuery(query).getSingleResult();
	    	}
	    	catch (Exception ex)
	    	{
	    		log.warn("[nombreEntidad] Fallo al buscar el nombre de la Determinacion con id="+idDeterminacion+". ERROR:"+ex.getMessage());
	    		ex.printStackTrace();
	    	}
	    	
	    	
	    	
	    	return nombreEnt;
	    }
	
	    
	    public int actualizarCU (CondicionUrbanisticaSimplificadaDTO cuSimplificada)
	    {
	    	int resultado = 0;
	    	
	    	log.info("[actualizarCU] cuSimplificada: idRegimen="+cuSimplificada.getIdRegimen() +" Det="+cuSimplificada.getIdDeterminacion()+" Ent="+cuSimplificada.getIdEntidad());
	    	
	    	
	    		
		    	// ----------------------
		    	// CU
		    	// ----------------------
		    	Entidaddeterminacion condicionurbanistica;
		    	
		    	// -----------------------
		    	// Caso
		    	// -----------------------
		    	Casoentidaddeterminacion casoUnico;
		    	
		    	
		    	
		    	// Compruebo si ya existe una Entidaddeterminacion con esa determinacion y esa entidad
		    	String query="SELECT entDet " +
		         " FROM Entidaddeterminacionregimen entDet " + 
		         " WHERE entDet.iden = "+cuSimplificada.getIdRegimen();
		    	
		    	try
		    	{
		    		Entidaddeterminacionregimen regimenCU =  (Entidaddeterminacionregimen)em.createQuery(query).getSingleResult();
		    		
		    		
		    		
				    	
				    	
				    	// Actualizo el valor
				    	if (cuSimplificada.getValor()!="" & cuSimplificada.getValor()!=null)
				    	{
				    		regimenCU.setValor(cuSimplificada.getValor());
				    	}
				    	else
				    	{
				    		regimenCU.setValor(null);
				    	}
				    	
				    	// Actualizo el Valor Referencia
				    	if (cuSimplificada.getIdDeterminacionValorReferencia()!= 0)
				    	{
				    		// Si el regimen tiene una Determinacion de Regimen, el valor de referencia enlaza a la determinacion de regimen 
				    		// con el valor de referencia
				    		
				    		if (cuSimplificada.getIdDeterminacionRegimen()!= 0)
					    	{
				    			Opciondeterminacion regValorReferencia;
					    		
					    		String queryidRE= "SELECT opcdet " +
					            " FROM Opciondeterminacion opcdet " +
					            " WHERE opcdet.determinacionByIddeterminacion.iden = " +cuSimplificada.getIdDeterminacionRegimen() +
					            " AND opcdet.determinacionByIddeterminacionvalorref.iden = "+cuSimplificada.getIdDeterminacionValorReferencia();
					    		
					            regValorReferencia = (Opciondeterminacion)em.createQuery(queryidRE).getSingleResult();
					            				    		
					    		regimenCU.setOpciondeterminacion(regValorReferencia);
					    	}
				    		else
				    		{
				    			// Si el regimen NO tiene una Determinacion de Regimen, el valor de referencia enlaza a la determinacion de la CU 
					    		// con el valor de referencia
				    			Opciondeterminacion regValorReferencia;
					    		
					    		String queryidRE= "SELECT opcdet " +
					            " FROM Opciondeterminacion opcdet " +
					            " WHERE opcdet.determinacionByIddeterminacion.iden = " +cuSimplificada.getIdDeterminacion() +
					            " AND opcdet.determinacionByIddeterminacionvalorref.iden = "+cuSimplificada.getIdDeterminacionValorReferencia();
					    		
					            regValorReferencia = (Opciondeterminacion)em.createQuery(queryidRE).getSingleResult();
					            				    		
					    		regimenCU.setOpciondeterminacion(regValorReferencia);
				    		}
				    		
				    		
				    	}
				    	else
				    	{
				    		regimenCU.setOpciondeterminacion(null);
				    	}
				    	
				    	// Actualizo la Determinacion Regimen
				    	if (cuSimplificada.getIdDeterminacionRegimen()!= 0)
				    	{
				    		
				    		Determinacion detRegimen = em.find(Determinacion.class,cuSimplificada.getIdDeterminacionRegimen());	    		
				    		regimenCU.setDeterminacion(detRegimen);
				    		
				    	}
				    	else
				    	{
				    		regimenCU.setDeterminacion(null);
				    	}
				    	
				    	// Actualizamos el regimen
				    	em.merge(regimenCU);
				    	
				    	
				    	// ----------------------------
				    	// Creamos/Actualizamos el/los regimenes especificos
				    	// ----------------------------
				    	
				    	// Inicialmente recorremos todos los regimenes especificos que tuviera el regimen y los borramos
				    	Iterator<Regimenespecifico> regEspecificoOLDIt = regimenCU.getRegimenespecificos().iterator();
				    	
				    	while (regEspecificoOLDIt.hasNext())
				    	{
				    		em.remove(regEspecificoOLDIt.next());
				    	}
				    	
				    	// Agregamos los nuevos regimenes especificos
				    	List<RegimenesEspecificosSimplificadosDTO> regEspecificoSimplificadosList = cuSimplificada.getRegimenesEspecificos();
				    	
				    	int orden = 1;
				    	for (RegimenesEspecificosSimplificadosDTO regEspSimplificado : regEspecificoSimplificadosList)
				    	{
				    		if (regEspSimplificado.getTextoRegimenEspecifico()!="" & regEspSimplificado.getTextoRegimenEspecifico()!=null)
					    	{
					    		Regimenespecifico regEspecifico = new Regimenespecifico();
					    		
					    		regEspecifico.setEntidaddeterminacionregimen(regimenCU);
					    		regEspecifico.setOrden(orden);
					    		
					    		regEspecifico.setTexto(regEspSimplificado.getTextoRegimenEspecifico());
					    		
					    		if (regEspSimplificado.getNombreRegimenEspecifico()!="" & regEspSimplificado.getNombreRegimenEspecifico()!=null)
					    		{
					    			regEspecifico.setNombre(regEspSimplificado.getNombreRegimenEspecifico());
					    		}
					    		else
					    		{
					    			regEspecifico.setNombre("Observaciones");
					    		}
					    		
					    		// Persistimos el regimen especifico
						    	em.persist(regEspecifico);
						    	
						    	orden++;
					    		
					    	}
				    		
				    	}
				    	
				    	    	
				    	
				    	log.info("[actualizarCU] Actualizacion correcta");
				    		resultado = regimenCU.getIden();
		    			
		    		
		    	}
		    	catch (NoResultException ex)
		    	{
		    		
			    	log.error("[actualizarCU] No existe el regimen que se pretende actualizar");
			    	ex.printStackTrace();
		    	}
		    	catch (NonUniqueResultException ex1)
		    	{
		    		log.error("[actualizarCU] Existe mas de un resultado para ese regimen.");
		    		ex1.printStackTrace();
		    		
		    		
		    	}
		    	catch (Exception ex2)
		    	{
		    		log.error("[actualizarCU] Error al actualizar.");
		    		ex2.printStackTrace();
		    		
		    		
		    	}
		         
	    	
	    	
	    	return resultado;
	    	
	    }
	    
	public boolean tieneAsignadaEntidadGrupoAplicacion(int idEntidad) {
		boolean asignadoGA = false;

		Entidaddeterminacion condicionurbanistica;

		String query = " SELECT entDet "
				+ " FROM Entidaddeterminacion entDet "
				+ " WHERE entDet.entidad.iden = " + idEntidad
				+ " AND entDet.determinacion.idcaracter = 20";

		try {
			
			condicionurbanistica = (Entidaddeterminacion)em.createQuery(query).getSingleResult();
			
			// Se ha encontrado una CU con caracter grupo para la entidad
			asignadoGA = true;
			log.info("[tieneAsignadaEntidadGrupoAplicacion] Se ha encontrado una CU (id) ="+condicionurbanistica.getIden() +" con caracter grupo para la entidad(id) ="+idEntidad);


		}

		catch (NoResultException ex) {
			
			log.warn("[tieneAsignadaEntidadGrupoAplicacion] No se ha encontrado determinacion de caracter grupo aplicada a la entidad (id) ="+idEntidad);

			
		} catch (NonUniqueResultException ex1) {
			log.warn("[tieneAsignadaEntidadGrupoAplicacion] Se han encontrado mas de una determinacion de caracter grupo aplicada a la entidad (id) ="+idEntidad);

			
		}

		return asignadoGA;
	}
	
	public boolean tieneDeterminacionAAplicarEnGAelVRdeCUdeEntidadAplicadaAGA(int idEntidad, int idDeterminacionAAplicar) {
		
		boolean resultado = false;
		
		log.debug("[tieneDeterminacionAAplicarEnGAelVRdeCUdeEntidadAplicadaAGA]  Inicio: entidad (id) ="+idEntidad+ " / "+" idDeterminacionAAplicar="+idDeterminacionAAplicar);


		Entidaddeterminacion cuDeEntidadAplicadaAGA;

		String query = " SELECT entDet "
				+ " FROM Entidaddeterminacion entDet "
				+ " WHERE entDet.entidad.iden = " + idEntidad
				+ " AND entDet.determinacion.idcaracter = 20";

		try {
			
			cuDeEntidadAplicadaAGA = (Entidaddeterminacion)em.createQuery(query).getSingleResult();
			
			// Se ha encontrado una CU con caracter grupo para la entidad
			log.debug("[tieneDeterminacionAAplicarEnGAelVRdeCUdeEntidadAplicadaAGA] Se ha encontrado una CU (id) ="+cuDeEntidadAplicadaAGA.getIden() +" con caracter grupo para la entidad(id) ="+idEntidad);

			// Obtengo el VR de la CU con caracter grupo para la entidad
			
			
			
			String queryRegimenesCaso = " SELECT caso.entidaddeterminacionregimensForIdcaso "
						+ " FROM Casoentidaddeterminacion caso "
						+ " WHERE caso.entidaddeterminacion.iden = "+cuDeEntidadAplicadaAGA.getIden();
			
			List<Entidaddeterminacionregimen> regsCaso = em.createQuery(queryRegimenesCaso).getResultList();
		        
			// Cojo el primer regimen
		    String queryDetVRCUGA = " SELECT reg.opciondeterminacion.determinacionByIddeterminacionvalorref "
						+ " FROM Entidaddeterminacionregimen reg "
						+ " WHERE reg.iden = "+regsCaso.get(0).getIden();
		    
		    Determinacion detVRCUGA = (Determinacion) em.createQuery(queryDetVRCUGA).getSingleResult();
			
			//Determinacion detVRCUGA = cuDeEntidadAplicadaAGA.getCasoentidaddeterminacions().iterator().next().getEntidaddeterminacionregimensForIdcaso().iterator().next().getOpciondeterminacion().getDeterminacionByIddeterminacionvalorref();
			
			String queryGA =  "SELECT opDet " +
	    	" FROM Determinaciongrupoentidad opDet"+
	    	" WHERE opDet.determinacionByIddeterminacion="+idDeterminacionAAplicar+
	    	" AND opDet.determinacionByIddeterminaciongrupo="+detVRCUGA.getIden();
			
			try {
				
				Determinaciongrupoentidad detGE = (Determinaciongrupoentidad)em.createQuery(queryGA).getSingleResult();
				
				resultado = true;
				
			}
			catch (NoResultException ex4) {
				
				log.warn("[tieneDeterminacionAAplicarEnGAelVRdeCUdeEntidadAplicadaAGA] No se ha encontrado en la determinacion a aplicar (id) = "+idDeterminacionAAplicar+" que tenga como grupo a la determinacion (id)"+detVRCUGA.getIden());

				
			} catch (NonUniqueResultException ex5) {
				log.warn("[tieneDeterminacionAAplicarEnGAelVRdeCUdeEntidadAplicadaAGA] Se han encontrado en la determinacion a aplicar (id) = "+idDeterminacionAAplicar+" que tenga como grupo a la determinacion (id)"+detVRCUGA.getIden());

				
			}
			

		}

		catch (NoResultException ex) {
			
			log.warn("[tieneDeterminacionAAplicarEnGAelVRdeCUdeEntidadAplicadaAGA] No se ha encontrado determinacion de caracter grupo aplicada a la entidad (id) ="+idEntidad);

			
		} catch (NonUniqueResultException ex1) {
			log.warn("[tieneDeterminacionAAplicarEnGAelVRdeCUdeEntidadAplicadaAGA] Se han encontrado mas de una determinacion de caracter grupo aplicada a la entidad (id) ="+idEntidad);

			
		}
		catch (Exception ex2) {
			log.error("[tieneDeterminacionAAplicarEnGAelVRdeCUdeEntidadAplicadaAGA] Error");
			ex2.printStackTrace();

			
		}

		return resultado;
	}
	
	public boolean tieneDeterminacionAAplicarElGA(int idDetVRCUGA, int idDeterminacionAAplicar) {
		
		boolean resultado = false;
		
		log.debug("[tieneDeterminacionAAplicarElGA]  Inicio: idDetVRCUGA (id) ="+idDetVRCUGA+ " / "+" idDeterminacionAAplicar="+idDeterminacionAAplicar);


		
			String queryGA =  "SELECT opDet " +
	    	" FROM Determinaciongrupoentidad opDet"+
	    	" WHERE opDet.determinacionByIddeterminacion="+idDeterminacionAAplicar+
	    	" AND opDet.determinacionByIddeterminaciongrupo="+idDetVRCUGA;
			
			try {
				
				Determinaciongrupoentidad detGE = (Determinaciongrupoentidad)em.createQuery(queryGA).getSingleResult();
				
				resultado = true;
				
			}
			catch (NoResultException ex4) {
				
				log.warn("[tieneDeterminacionAAplicarElGA] No se ha encontrado en la determinacion a aplicar (id) = "+idDeterminacionAAplicar+" que tenga como grupo a la determinacion (id)"+idDetVRCUGA);

				
			} catch (NonUniqueResultException ex5) {
				log.warn("[tieneDeterminacionAAplicarElGA] Se han encontrado en la determinacion a aplicar (id) = "+idDeterminacionAAplicar+" que tenga como grupo a la determinacion (id)"+idDetVRCUGA);

				
			}
			

		

		return resultado;
	}
	
	
	
	public int obtenerVRdeCUdeEntidadAplicadaAGA(int idEntidad) {
		
		int resultado = 0;
		
		log.debug("[obtenerVRdeCUdeEntidadAplicadaAGA]  Inicio: entidad (id) ="+idEntidad);


		Entidaddeterminacion cuDeEntidadAplicadaAGA;

		String query = " SELECT entDet "
				+ " FROM Entidaddeterminacion entDet "
				+ " WHERE entDet.entidad.iden = " + idEntidad
				+ " AND entDet.determinacion.idcaracter = 20";

		try {
			
			cuDeEntidadAplicadaAGA = (Entidaddeterminacion)em.createQuery(query).getSingleResult();
			
			// Se ha encontrado una CU con caracter grupo para la entidad
			log.debug("[obtenerVRdeCUdeEntidadAplicadaAGA] Se ha encontrado una CU (id) ="+cuDeEntidadAplicadaAGA.getIden() +" con caracter grupo para la entidad(id) ="+idEntidad);

			// Obtengo el VR de la CU con caracter grupo para la entidad
			
			
			
			String queryRegimenesCaso = " SELECT caso.entidaddeterminacionregimensForIdcaso "
						+ " FROM Casoentidaddeterminacion caso "
						+ " WHERE caso.entidaddeterminacion.iden = "+cuDeEntidadAplicadaAGA.getIden();
			
			List<Entidaddeterminacionregimen> regsCaso = em.createQuery(queryRegimenesCaso).getResultList();
		        
			// Cojo el primer regimen
		    String queryDetVRCUGA = " SELECT reg.opciondeterminacion.determinacionByIddeterminacionvalorref "
						+ " FROM Entidaddeterminacionregimen reg "
						+ " WHERE reg.iden = "+regsCaso.get(0).getIden();
		    
		    Determinacion detVRCUGA = (Determinacion) em.createQuery(queryDetVRCUGA).getSingleResult();
		    resultado = detVRCUGA.getIden();
			
			
			

		}

		catch (NoResultException ex) {
			
			log.warn("[obtenerVRdeCUdeEntidadAplicadaAGA] No se ha encontrado determinacion de caracter grupo aplicada a la entidad (id) ="+idEntidad);

			
		} catch (NonUniqueResultException ex1) {
			log.warn("[obtenerVRdeCUdeEntidadAplicadaAGA] Se han encontrado mas de una determinacion de caracter grupo aplicada a la entidad (id) ="+idEntidad);

			
		}
		catch (Exception ex2) {
			log.error("[obtenerVRdeCUdeEntidadAplicadaAGA] Error");
			ex2.printStackTrace();

			
		}

		log.debug("[obtenerVRdeCUdeEntidadAplicadaAGA] detVRCUGA.getIden() ="+resultado);

		return resultado;
	}
	
	
	public int obtenerIdDeterminacionGrupoAplicacionDeTramite (int idTramite)
	{
		int resultado = 0;
		
		Determinacion deter;
		
		String query = " SELECT det "
			+ " FROM Determinacion det "
			+ " WHERE det.tramite.iden = " + idTramite
			+ " AND det.idcaracter = 20";
		
		try {
			
			deter = (Determinacion)em.createQuery(query).getSingleResult();
			
			// Se ha encontrado una Determinacion con caracter grupo para el tramite
			resultado = deter.getIden();
			log.info("[obtenerIdDeterminacionGrupoAplicacionDeTramite] Se ha encontrado una Determinacion (id) ="+resultado +" con caracter grupo para el tramite ="+idTramite);


		}

		catch (NoResultException ex) {
			
			log.warn("[tieneAsignadaEntidadGrupoAplicacion] No se ha encontrado determinacion de caracter grupo aplicada al tramite (id) ="+idTramite);

			
		} catch (NonUniqueResultException ex1) {
			log.warn("[tieneAsignadaEntidadGrupoAplicacion] Se han encontrado mas de una determinacion de caracter grupo aplicada al tramite (id) ="+idTramite);

			
		}

		
		return resultado;
	}
	
	
	public boolean borrarRegimen (int idRegimen)
	{
		boolean resultado = false;
		
		String query = " SELECT reg "
			+ " FROM Entidaddeterminacionregimen reg "
			+ " WHERE reg.iden = " + idRegimen;
		
		try {
			Entidaddeterminacionregimen regCU = (Entidaddeterminacionregimen)em.createQuery(query).getSingleResult();
			
			// Debo borrar RECURSIVAMENTE los RegimenesEspecifico de cada
			// EntidadDeterminacionRegimen borrada
			Set<Regimenespecifico> especificos = regCU.getRegimenespecificos();
			if (especificos != null && !especificos.isEmpty()) {
				for (Regimenespecifico regimenespecifico : especificos) {

					em.remove(regimenespecifico);

				}
			}
			
			// Compruebo si el caso al que pertenece tiene mas regimenes, en caso de ser el ultimo regimen se debe borrar tambien la CU
			Casoentidaddeterminacion casoCU = regCU.getCasoentidaddeterminacionByIdcaso();
			
			if (casoCU.getEntidaddeterminacionregimensForIdcaso().size()>1)
			{
				// Quedan mas de un regimen, por tanto solo borro el regimen en cuestion
				em.remove(em.merge(regCU));
			}
			else
			{
				Entidaddeterminacion cu = casoCU.getEntidaddeterminacion();
				
				// Borro el regimen
				em.remove(em.merge(regCU));
				
				// Borro el caso
				em.remove(em.merge(casoCU));
				
				// Compruebo si la condicion urbanistica tiene mas casos, si tiene no borro aun la condicion urbanistica
				
				if (cu.getCasoentidaddeterminacions().size()==1)
				{
					// Borro la CU
					em.remove(em.merge(cu));
				}
				
				
				
			}
			
			
			
			
			
			resultado = true;
			
		}
		
		catch (NoResultException ex) {
			
			log.warn("[borrarRegimen] No se ha encontrado el regimen de la CU  (idRegimen) ="+idRegimen);

			
		} catch (NonUniqueResultException ex1) {
			log.warn("[borrarRegimen] Se han encontrado mas de un regimen de la CU  (idRegimen) ="+idRegimen);

			
		}
		catch (Exception ex2) {
			log.error("[borrarRegimen] Error al borrar el regimen (idRegimen)="+idRegimen);
			ex2.printStackTrace();

			
		}
		
		return resultado;

		
	}
	    
	
		/*
		 *  Crea una CU a partir de los datos mandados en la cuSimplificada
		 *  
		 *  Devuelve:
		 *  /-> Un numero mayor que cero: El id de la nueva CU creada
		 *  /-> Cero: Ha habia algun error al crear la CU
		 *  /-> (-1): La entidad no tiene aun Grupo de Aplicacion aplicado y por tanto no se puede crear la CU
		 * 	/-> (-2): No se ha encontrado en la determinacion a aplicar  que tenga como grupo a la determinacion de grupo aplicada a la entidad
		 */
	    public int crearCU (CondicionUrbanisticaSimplificadaDTO cuSimplificada)
	    {
	    	int resultado = 0;
	    	
	    	log.info("[crearCasoCU] cuSimplificada:  Det="+cuSimplificada.getIdDeterminacion()+" Ent="+cuSimplificada.getIdEntidad());
	    	
	    	// ------------------------------
	    	// Lo primero que hago es comprobar si la entidad tiene aplicada una determinacion de caracter grupo
	    	// ya que de no tenerla no se puede crear la CU y se devuelve -1
	    	// ------------------------------
	    	
	    	if (tieneAsignadaEntidadGrupoAplicacion(cuSimplificada.getIdEntidad()))
	    	{
	    		
	    		if (tieneDeterminacionAAplicarEnGAelVRdeCUdeEntidadAplicadaAGA(cuSimplificada.getIdEntidad(),cuSimplificada.getIdDeterminacion()))
	    		{
	    			// Si tiene la entidad asignada una CU con una determinacion de caracter grupo, se prosigue
			    	try{
			    		
				    	// ----------------------
				    	// Creamos la CU
				    	// ----------------------
				    	Entidaddeterminacion condicionurbanistica = new Entidaddeterminacion();
				    	
				    	// -----------------------
				    	// Creamos el caso
				    	// -----------------------
				    	Casoentidaddeterminacion casoUnico = new Casoentidaddeterminacion();
				    	
				    	
				    	
				    	// Compruebo si ya existe una Entidaddeterminacion con esa determinacion y esa entidad
				    	String query="SELECT entDet " +
				         " FROM Entidaddeterminacion entDet " + 
				         " WHERE entDet.entidad = "+cuSimplificada.getIdEntidad() +
				         " AND entDet.determinacion = " +cuSimplificada.getIdDeterminacion();
				    	
				    	try
				    	{
				    		condicionurbanistica = (Entidaddeterminacion)em.createQuery(query).getSingleResult();
				    		log.info("[crearCU] Ya existia previamente una Entidaddeterminacion para esa entidad y determinacion");
				    		
				    		// -----------------------
					    	// Obtenemos el Caso
					    	// -----------------------
				    		
				    		String queryCasos="SELECT caso " +
					         " FROM Casoentidaddeterminacion caso " + 
					         " WHERE caso.entidaddeterminacion.iden = "+condicionurbanistica.getIden();
				    		
				    		List<Casoentidaddeterminacion> casosList = (List<Casoentidaddeterminacion> ) em.createQuery(queryCasos).getResultList();
				    		log.info("[crearCU] Numero de casos:"+casosList.size()+" para la CU (id)"+condicionurbanistica.getIden());
				    		
				    		
				    		//Set<Casoentidaddeterminacion> casosList = condicionurbanistica.getCasoentidaddeterminacions();
				    		
				    		if (casosList.iterator().hasNext())
				    		{
				    			casoUnico = casosList.iterator().next();
				    			log.info("[crearCU] Ya existia previamente el caso. Cogemos el primero");
				    		}
				    		else
				    		{
				    			// Si no tiene caso. Creamos uno
				    			casoUnico.setNombre("Caso");
			    		    	
						    	casoUnico.setEntidaddeterminacion(condicionurbanistica);
						    	
						    	// Persistimos el caso
						    	em.persist(casoUnico);
						    	log.info("[crearCU] Se crea el caso porque no hay aunque existia previamente la Entidaddeterminacion");
				    		}
				    		
				    		
				    	}
				    	catch (NoResultException ex)
				    	{
				    		Determinacion detCU = em.find(Determinacion.class,cuSimplificada.getIdDeterminacion());
					    	condicionurbanistica.setDeterminacion(detCU);
					    		    	   		    	
					    	Entidad entCU = em.find(Entidad.class, cuSimplificada.getIdEntidad());
					    	condicionurbanistica.setEntidad(entCU);
					    	
					    	// Persistimos la CU
					    	em.persist(condicionurbanistica);
					    	log.info("[crearCU] Se crea una Entidaddeterminacion para esa entidad y determinacion");
					    	
					    	// -----------------------
					    	// Creamos el caso
					    	// -----------------------
					    	
					    	casoUnico.setNombre("Caso");
					    		    	
					    	casoUnico.setEntidaddeterminacion(condicionurbanistica);
					    	
					    	// Persistimos el caso
					    	em.persist(casoUnico);
					    	log.info("[crearCU] Se crea el caso ya que no existia previamente la Entidaddeterminacion");
				    	}
				    	catch (NonUniqueResultException ex1)
				    	{
				    		log.warn("[crearCU] Existe mas de un resultado para esa Entidaddeterminacion. Se lo asocio al primero ");
				    		
				    		List<Entidaddeterminacion> condicionurbanisticaList = em.createQuery(query).getResultList();
				    		condicionurbanistica = condicionurbanisticaList.get(0);
				    		
				    		// -----------------------
					    	// Obtenemos el Caso
					    	// -----------------------
				    		String queryCasos="SELECT caso " +
					         " FROM Casoentidaddeterminacion caso " + 
					         " WHERE caso.entidaddeterminacion.iden = "+condicionurbanistica.getIden();
				    		
				    		List<Casoentidaddeterminacion> casosList = (List<Casoentidaddeterminacion> ) em.createQuery(queryCasos).getResultList();
				    		log.info("[crearCU] Numero de casos:"+casosList.size()+" para la CU (id)"+condicionurbanistica.getIden());
				    		
				    		
				    		//Set<Casoentidaddeterminacion> casosList = condicionurbanistica.getCasoentidaddeterminacions();
				    		
				    		if (casosList.iterator().hasNext())
				    		{
				    			casoUnico = casosList.iterator().next();
				    			log.info("[crearCU] Ya existia previamente el caso. Cogemos el primero");
				    		}
				    		else
				    		{
				    			// Si no tiene caso. Creamos uno
				    			casoUnico.setNombre("Caso");
			    		    	
						    	casoUnico.setEntidaddeterminacion(condicionurbanistica);
						    	
						    	// Persistimos el caso
						    	em.persist(casoUnico);
						    	log.info("[crearCU] Se crea el caso porque no hay aunque existia previamente la Entidaddeterminacion");
				    		}
				    	}
				         
				    	
				    	
				    	
				    	
				    	
				    		    	
				    	// ----------------------------
				    	// Creamos el regimen del caso
				    	// ----------------------------
				    	
				    	Entidaddeterminacionregimen regimenCU = new Entidaddeterminacionregimen();
				    	regimenCU.setCasoentidaddeterminacionByIdcaso(casoUnico);
				    	
				    	if (cuSimplificada.getIdCasoAplicacion()!=0)
				    	{
				    		regimenCU.setCasoentidaddeterminacionByIdcasoaplicacion(em.find(Casoentidaddeterminacion.class, cuSimplificada.getIdCasoAplicacion()));
				    		
				    	}
				    	
				    	if (cuSimplificada.getValor()!="" & cuSimplificada.getValor()!=null)
				    	{
				    		regimenCU.setValor(cuSimplificada.getValor());
				    	}
				    	
				    	if (cuSimplificada.getIdDeterminacionValorReferencia()> 0)
				    	{
				    		// Si el regimen tiene una Determinacion de Regimen, el valor de referencia enlaza a la determinacion de regimen 
				    		// con el valor de referencia
				    		
				    		if (cuSimplificada.getIdDeterminacionRegimen()!= 0)
					    	{
				    			Opciondeterminacion regValorReferencia;
					    		
					    		String queryidRE= "SELECT opcdet " +
					            " FROM Opciondeterminacion opcdet " +
					            " WHERE opcdet.determinacionByIddeterminacion.iden = " +cuSimplificada.getIdDeterminacionRegimen() +
					            " AND opcdet.determinacionByIddeterminacionvalorref.iden = "+cuSimplificada.getIdDeterminacionValorReferencia();
					    		try
					    		{
					    			regValorReferencia = (Opciondeterminacion)em.createQuery(queryidRE).getSingleResult();       				    		
						    		regimenCU.setOpciondeterminacion(regValorReferencia);
					    		}
					    		catch (NoResultException exN)
					    		{
					    			// Puede ocurrir para Canarias que tenga una determinacion regimen pero que no tenga valor de referencia
					    			log.info("[crearCU] Tiene determinacion de regimen, pero no tiene valor de referencia");
					    		}
					            
					    	}
				    		else
				    		{
				    			// Si el regimen NO tiene una Determinacion de Regimen, el valor de referencia enlaza a la determinacion de la CU 
					    		// con el valor de referencia
				    			Opciondeterminacion regValorReferencia;
					    		
					    		String queryidRE= "SELECT opcdet " +
					            " FROM Opciondeterminacion opcdet " +
					            " WHERE opcdet.determinacionByIddeterminacion.iden = " +cuSimplificada.getIdDeterminacion() +
					            " AND opcdet.determinacionByIddeterminacionvalorref.iden = "+cuSimplificada.getIdDeterminacionValorReferencia();
					    		
					            regValorReferencia = (Opciondeterminacion)em.createQuery(queryidRE).getSingleResult();
					            				    		
					    		regimenCU.setOpciondeterminacion(regValorReferencia);
				    		}
				    	}
				    	else
				    	{
				    		// Si es cero, puede ser que venga en Valor
				    		if (cuSimplificada.getNombreDetValorRef()!=null & cuSimplificada.getNombreDetValorRef()!="")
				    		{
				    			regimenCU.setValor(cuSimplificada.getNombreDetValorRef());
				    		}
				    	}
				    	
				    	if (cuSimplificada.getIdDeterminacionRegimen()!= 0)
				    	{
				    		
				    		Determinacion detRegimen = em.find(Determinacion.class,cuSimplificada.getIdDeterminacionRegimen());	    		
				    		regimenCU.setDeterminacion(detRegimen);
				    		
				    	}
				    	
				    	// Persistimos el regimen
				    	em.persist(regimenCU);
				    	
				    	// ----------------------------
				    	// Creamos el/los regimenes especificos
				    	// ----------------------------
				    	List<RegimenesEspecificosSimplificadosDTO> regEspecificoSimplificadosList = cuSimplificada.getRegimenesEspecificos();
				    	
				    	
				    	if (regEspecificoSimplificadosList!=null)
				    	{
				    		int orden = 1;
					    	for (RegimenesEspecificosSimplificadosDTO regEspSimplificado : regEspecificoSimplificadosList)
					    	{
					    		if (regEspSimplificado.getTextoRegimenEspecifico()!="" & regEspSimplificado.getTextoRegimenEspecifico()!=null)
						    	{
						    		Regimenespecifico regEspecifico = new Regimenespecifico();
						    		
						    		regEspecifico.setEntidaddeterminacionregimen(regimenCU);
						    		regEspecifico.setOrden(orden);
						    		
						    		regEspecifico.setTexto(regEspSimplificado.getTextoRegimenEspecifico());
						    		
						    		if (regEspSimplificado.getNombreRegimenEspecifico()!="" & regEspSimplificado.getNombreRegimenEspecifico()!=null)
						    		{
						    			regEspecifico.setNombre(regEspSimplificado.getNombreRegimenEspecifico());
						    		}
						    		else
						    		{
						    			regEspecifico.setNombre("Observaciones");
						    		}
						    		
						    		// Persistimos el regimen especifico
							    	em.persist(regEspecifico);
							    	
							    	orden++;
						    		
						    	}
					    		
					    	}
				    	}
				    	
				    	
				    	    	
				    	
				    	
				    		resultado = regimenCU.getIden();
				    		log.info("[crearCasoCU] Creada la Condicion Urbanistica: idCU="+condicionurbanistica.getIden()+". Se devuelve idRegimen:"+resultado);
			    	}
			    	catch (Exception ex)
			    	{
			    		log.error("[crearCasoCU] Fallo al crear la Condicion Urbanistica. ERROR:"+ex.getMessage());
			    		ex.printStackTrace();
			    	}
	    		}
	    		else
	    		{
	    			//  No se ha encontrado en la determinacion a aplicar  que tenga como grupo a la determinacion de grupo aplicada a la entidad
		    		resultado = -2;
	    		}
	    	
	    		
	    	
	    	}
	    	else
	    	{
	    		// La entidad no tiene CU de caracter grupo aplicada, devuelvo -1
	    		resultado = -1;
	    	}
	    	
	    	return resultado;
	    	
	    }
	    
	    /*
		 *  Crea una CU del Grupo de Aplicacion a partir de los datos mandados en la cuSimplificada
		 *  
		 *  Devuelve:
		 *  /-> Un numero mayor que cero: El id de la nueva CU creada
		 *  /-> Cero: Ha habia algun error al crear la CU
		 * 	
		 */
	    public int crearCUGrupoAplicacion (CondicionUrbanisticaSimplificadaDTO cuSimplificada)
	    {
	    	int resultado = 0;
	    	
	    	log.info("[crearCUGrupoAplicacion] cuSimplificada:  Det="+cuSimplificada.getIdDeterminacion()+" Ent="+cuSimplificada.getIdEntidad());
	    	
	    	// ----------------------
	    	// Creamos la CU
	    	// ----------------------
	    	Entidaddeterminacion condicionurbanistica = new Entidaddeterminacion();
	    	
	    	// -----------------------
	    	// Creamos el caso
	    	// -----------------------
	    	Casoentidaddeterminacion casoUnico = new Casoentidaddeterminacion();
	    	
	    		// Si tiene la entidad asignada una CU con una determinacion de caracter grupo, se prosigue
		    	try{
		    		
			    	
			    	
			    	
			    	
			    	// Compruebo si ya existe una Entidaddeterminacion con esa determinacion y esa entidad
			    	String query="SELECT entDet " +
			         " FROM Entidaddeterminacion entDet " + 
			         " WHERE entDet.entidad = "+cuSimplificada.getIdEntidad() +
			         " AND entDet.determinacion = " +cuSimplificada.getIdDeterminacion();
			    	
			    	try
			    	{
			    		condicionurbanistica = (Entidaddeterminacion)em.createQuery(query).getSingleResult();
			    		log.info("[crearCUGrupoAplicacion] Ya existia previamente una Entidaddeterminacion para esa entidad y determinacion");
			    		
			    		// -----------------------
				    	// Obtenemos el Caso
				    	// -----------------------
			    		Set<Casoentidaddeterminacion> casosList = condicionurbanistica.getCasoentidaddeterminacions();
			    		
			    		if (casosList.iterator().hasNext())
			    		{
			    			casoUnico = casosList.iterator().next();
			    			log.info("[crearCUGrupoAplicacion] Ya existia previamente el caso. Cogemos el primero");
			    		}
			    		else
			    		{
			    			// Si no tiene caso. Creamos uno
			    			casoUnico.setNombre("Caso");
		    		    	
					    	casoUnico.setEntidaddeterminacion(condicionurbanistica);
					    	
					    	// Persistimos el caso
					    	em.persist(casoUnico);
					    	log.info("[crearCUGrupoAplicacion] Se crea el caso porque no hay aunque existia previamente la Entidaddeterminacion");
			    		}
			    		
			    		
			    	}
			    	catch (NoResultException ex)
			    	{
			    		Determinacion detCU = em.find(Determinacion.class,cuSimplificada.getIdDeterminacion());
				    	condicionurbanistica.setDeterminacion(detCU);
				    		    	   		    	
				    	Entidad entCU = em.find(Entidad.class, cuSimplificada.getIdEntidad());
				    	condicionurbanistica.setEntidad(entCU);
				    	
				    	// Persistimos la CU
				    	em.persist(condicionurbanistica);
				    	log.info("[crearCUGrupoAplicacion] Se crea una Entidaddeterminacion para esa entidad y determinacion");
				    	
				    	// -----------------------
				    	// Creamos el caso
				    	// -----------------------
				    	
				    	casoUnico.setNombre("Caso");
				    		    	
				    	casoUnico.setEntidaddeterminacion(condicionurbanistica);
				    	
				    	// Persistimos el caso
				    	em.persist(casoUnico);
				    	log.info("[crearCUGrupoAplicacion] Se crea el caso ya que no existia previamente la Entidaddeterminacion");
			    	}
			    	catch (NonUniqueResultException ex1)
			    	{
			    		log.warn("[crearCUGrupoAplicacion] Existe mas de un resultado para esa Entidaddeterminacion. Se lo asocio al primero ");
			    		
			    		List<Entidaddeterminacion> condicionurbanisticaList = em.createQuery(query).getResultList();
			    		condicionurbanistica = condicionurbanisticaList.get(0);
			    		
			    		// -----------------------
				    	// Obtenemos el Caso
				    	// -----------------------
			    		Set<Casoentidaddeterminacion> casosList = condicionurbanistica.getCasoentidaddeterminacions();
			    		
			    		if (casosList.iterator().hasNext())
			    		{
			    			casoUnico = casosList.iterator().next();
			    			log.info("[crearCUGrupoAplicacion] Ya existia previamente el caso. Cogemos el primero");
			    		}
			    		else
			    		{
			    			// Si no tiene caso. Creamos uno
			    			casoUnico.setNombre("Caso");
		    		    	
					    	casoUnico.setEntidaddeterminacion(condicionurbanistica);
					    	
					    	// Persistimos el caso
					    	em.persist(casoUnico);
					    	log.info("[crearCUGrupoAplicacion] Se crea el caso porque no hay aunque existia previamente la Entidaddeterminacion");
			    		}
			    	}
			         
			    	
			    		    	
			    	// ----------------------------
			    	// Creamos el regimen del caso
			    	// ----------------------------
			    	
			    	Entidaddeterminacionregimen regimenCU = new Entidaddeterminacionregimen();
			    	regimenCU.setCasoentidaddeterminacionByIdcaso(casoUnico);
		    	
			    	
			    	if (cuSimplificada.getIdDeterminacionValorReferencia()!= 0)
			    	{
			    		Opciondeterminacion regValorReferencia;
			    		
			    		String queryidRE= "SELECT opcdet " +
			            " FROM Opciondeterminacion opcdet " +
			            " WHERE opcdet.determinacionByIddeterminacion.iden = " +cuSimplificada.getIdDeterminacion() +
			            " AND opcdet.determinacionByIddeterminacionvalorref.iden = "+cuSimplificada.getIdDeterminacionValorReferencia();
			    		
			            regValorReferencia = (Opciondeterminacion)em.createQuery(queryidRE).getSingleResult();
			            				    		
			    		regimenCU.setOpciondeterminacion(regValorReferencia);
			    	}
			    	
			    	
			    	
			    	// Persistimos el regimen
			    	em.persist(regimenCU);
			    	
			    	
			    	
			    	    	
			    	
			    	
			    		resultado = regimenCU.getIden();
			    		log.info("[crearCUGrupoAplicacion] Creada la Condicion Urbanistica: idCU="+condicionurbanistica.getIden()+". Se devuelve idRegimen:"+resultado);
		    	}
		    	catch (Exception ex)
		    	{
		    		em.remove(condicionurbanistica);
		    		em.remove(casoUnico);
		    		log.error("[crearCUGrupoAplicacion] Fallo al crear la Condicion Urbanistica. (Se ha borrado la CU y caso creado temporalmente. ERROR:"+ex.getMessage());
		    		
		    		ex.printStackTrace();
		    	}
	    	
	    	
	    	
	    	return resultado;
	    	
	    }
	    
	    
	    public List<CondicionUrbanisticaSimplificadaDTO> listadoTodasCUSimplificadaDeTramite (int idTramite)
	    {
	    	List<CondicionUrbanisticaSimplificadaDTO> cuSimplList = new ArrayList<CondicionUrbanisticaSimplificadaDTO> ();
	    	
	    	
	    	// Compruebo si ya existe una Entidaddeterminacion con esa determinacion y esa entidad
	    	String query="SELECT cuReg.iden," +
	    			"cuReg.casoentidaddeterminacionByIdcaso.entidaddeterminacion.entidad.iden," +
	         		"cuReg.casoentidaddeterminacionByIdcaso.entidaddeterminacion.entidad.nombre," +
		      		"cuReg.casoentidaddeterminacionByIdcaso.entidaddeterminacion.determinacion.iden," +
		      		"cuReg.casoentidaddeterminacionByIdcaso.entidaddeterminacion.determinacion.nombre, " +
		      		"cuReg.casoentidaddeterminacionByIdcaso.entidaddeterminacion.entidad.clave" +
					" FROM Entidaddeterminacionregimen cuReg " +      
				    " WHERE cuReg.casoentidaddeterminacionByIdcaso.entidaddeterminacion.entidad.tramite = "+ idTramite + 
				    " order by cuReg.iden asc";
		
	    	
	    	try
	    	{
	    		List<Object> regimenesCUTramite = em.createQuery(query).getResultList();
	    		log.info("[listadoTodasCUSimplificadaDeTramite] Regimenes del tramite= "+idTramite+".  Numero regimenes="+regimenesCUTramite.size());
	    		
	    		for (Object regCUObjList : regimenesCUTramite)
	    		{
	    			// Por cada objeto creo un nuevo registro de CondicionUrbanisticaSimplificadaDTO
	    			
	    			CondicionUrbanisticaSimplificadaDTO cuSimpl = new CondicionUrbanisticaSimplificadaDTO();
	    			
	    			Object[] regCUArrayObj = (Object[]) regCUObjList;
	    			
	    			// El indice 1 se corresponde al id de la entidad de la CU
	    			cuSimpl.setIdEntidad((Integer)regCUArrayObj[1]);
	    			
	    			// El indice 2 se corresponde al nombre de la entidad de la CU
	    			cuSimpl.setNombreEntidad((String)regCUArrayObj[2]);
	    			
	    			// El indice 3 se corresponde al id de la determinacion de la CU
	    			cuSimpl.setIdDeterminacion((Integer)regCUArrayObj[3]);
	    			
	    			// El indice 4 se corresponde al nombre de la determinacion de la CU
	    			cuSimpl.setNombreDeterminacion((String)regCUArrayObj[4]);
	    			
	    			// El indice 5 se corresponde a la clave de la entidad
	    			cuSimpl.setClaveEntidad((String)regCUArrayObj[5]);
	    			
	    			// El indice 4 se corresponde al id del regimen
	    			int idRegimen = (Integer)regCUArrayObj[0];
	    			cuSimpl.setIdRegimen(idRegimen);
	    			
	    			// Obtengo el regimen de la CU
	    			Entidaddeterminacionregimen cuReg = em.find(Entidaddeterminacionregimen.class, idRegimen);
	    			
	    			// Almaceno el valor
	    			cuSimpl.setValor(cuReg.getValor());
	    			
	    			// Almaceno, si existe, el nombre y id del valor de referencia
	    			if (cuReg.getOpciondeterminacion()!=null)
	    			{
	    				if (cuReg.getOpciondeterminacion().getDeterminacionByIddeterminacionvalorref()!=null)
		    			{
	    					cuSimpl.setIdDeterminacionValorReferencia(cuReg.getOpciondeterminacion().getDeterminacionByIddeterminacionvalorref().getIden());
	    					cuSimpl.setNombreDetValorRef(cuReg.getOpciondeterminacion().getDeterminacionByIddeterminacionvalorref().getNombre());
	    			
		    			}
	    			}
	    			
	    			// Almaceno, si existe, el nombre y id de la determinacion de regimen
	    			if (cuReg.getDeterminacion()!=null)
	    			{
	    				cuSimpl.setIdDeterminacionRegimen(cuReg.getDeterminacion().getIden());
	    				cuSimpl.setNombreDetRegimen(cuReg.getDeterminacion().getNombre());
	    			}
	    			
	    			// Obtengo los regimenes especificos del regimen
	    			Set<Regimenespecifico> regimenespecificos = cuReg.getRegimenespecificos();
	    			
	    			Iterator regespecificosIt = regimenespecificos.iterator();
	    			
	    			List<RegimenesEspecificosSimplificadosDTO> regEspecificosSimplList = new ArrayList<RegimenesEspecificosSimplificadosDTO> ();
	    			
	    			while (regespecificosIt.hasNext())
	    			{
	    				Regimenespecifico regesp = (Regimenespecifico)regespecificosIt.next();
	    				RegimenesEspecificosSimplificadosDTO regEspSimpl = new RegimenesEspecificosSimplificadosDTO(regesp.getNombre(), regesp.getTexto());
	    				
	    				regEspecificosSimplList.add(regEspSimpl);
	    			}
	    			
	    			// Almaceno los regimenes especificos en el objeto a devolver
	    			cuSimpl.setRegimenesEspecificos(regEspecificosSimplList);
	    			
	    			
	    			cuSimplList.add(cuSimpl);
	    			
	    			
	    		}
	    		
	    		
	    	}
	    	catch (Exception ex)
	    	{
	    		log.error("[listadoTodasCUSimplificadaDeTramite] Fallo al buscar los Regimenes del tramite. ERROR:"+ex.getMessage());
	    		ex.printStackTrace();
	    	}
	    	
	    	
	    	return cuSimplList;
	    }
	    
	    
	    
	   
	 
	    
	    /**
	     * Funcion que generará automaticamente los grupos de aplicacion para las entidades del tramite que no lo tengan asignado
	     * 
	     * Devuelve:
	     * 0 Si no ha habido ningun error
	     * 1 error: no hay entidades para ese tramite
	     * 2 error: no especificado
	     * 3 error: No se ha encontrado clave de busqueda para la entidad
	     * 4 error: No se ha encontrado el valor de referencia de la clave de busqueda y del tramite
	     * 
	     * @param idTramite
	     * @return
	     */
	    public int generarAutomaticamenteGrupoAplicacionParaEntidadesNuevoconPlanBase (int idTramite, int idTramiteBase)
	    {
	    	int resultado = 0;
	    	
	    	int gaAnadidos = 0;
	    	
	    	log.info("[generarAutomaticamenteGrupoAplicacionParaEntidades] Se van a generar automaticamente los Grupos Aplicacion para las entidades del Tramite = "+idTramite);
	    	
	    	List<Entidad> entidadesDelTramite;

			String query = " SELECT ent "
					+ " FROM Entidad ent"
					+ " WHERE ent.tramite.iden = " + idTramite;

			try {
				
				// Obtengo todas las entidades del tramite
				entidadesDelTramite = (List<Entidad>)em.createQuery(query).getResultList();				
				log.info("[generarAutomaticamenteGrupoAplicacionParaEntidades] Se han encontrado "+entidadesDelTramite.size() +" entidades para el tramite ="+idTramite);

				// Para cada entidad, hay que comprobar si tiene Grupo de Aplicacion aplicado
				for (Entidad entidadTram : entidadesDelTramite)
				{
					if (tieneAsignadaEntidadGrupoAplicacion(entidadTram.getIden()))
					{
						// Si tiene asignado Grupo Aplicacion no hacemos nada
						log.info("[generarAutomaticamenteGrupoAplicacionParaEntidades] La Entidad (id="+entidadTram.getIden()+") '"+entidadTram.getNombre()+"' tiene ya asignado Grupo Aplicacion");
					}
					else
					{
						// Si no tiene asignado Grupo Aplicacion, se lo asignamos de forma automática
						
						// Tengo que averiguar el id del valor de referencia del Grupo de Aplicacion
						int idVRGA = 0;
						
						String claveBusqueda ="";
						
						if (entidadTram.getClave().contains("AMB"))
						{
							claveBusqueda = "7000000002";
						}
						
						if (entidadTram.getClave().contains("CLA"))
						{
							claveBusqueda = "7000000003";
						}
						
						if (entidadTram.getClave().contains("CAT"))
						{
							claveBusqueda = "7000000004";
						}
						
						if (entidadTram.getClave().contains("ZON"))
						{
							claveBusqueda = "7000000011";
						}
						
						if (entidadTram.getClave().contains("GES"))
						{
							claveBusqueda = "7000000016";
						}
						
						if (entidadTram.getClave().contains("SIS"))
						{
							claveBusqueda = "7000000008";
						}
						
						if (entidadTram.getClave().contains("PRO"))
						{
							claveBusqueda = "7000000013";
						}
						
						if (entidadTram.getClave().contains("AFE"))
						{
							claveBusqueda = "7000000031";
						}
						
						if (entidadTram.getClave().contains("RES"))
						{
							claveBusqueda = "7000000030";
						}
						
						if (entidadTram.getClave().contains("ACC"))
						{
							claveBusqueda = "7000000043";
						}
						
						// Si no ha encontrado ninguna clave de Busqueda, es un error
						
						if (claveBusqueda == "" || claveBusqueda==null)
						{
							resultado = 3;
							log.error("[generarAutomaticamenteGrupoAplicacionParaEntidades] Error: No se ha encontrado clave de busqueda para la entidad : "+entidadTram.getIden()+ " '"+entidadTram.getNombre()+"' con clave: "+entidadTram.getClave());

						}
						else
						{
							
							try
							{
								// Buscamos el id del valor de referencia del Grupo de Aplicacion
								
								String hql = " SELECT det "
									+ " FROM Determinacion det "
									+ " WHERE det.codigo = '"+claveBusqueda+"' " +
									  " and det.tramite.iden = "+idTramiteBase +
									  " and det.determinacionByIdpadre.iden in (select det2.iden from es.idom.sev.cs.urbanismoenred.editorfip.data.rpm.planeamiento.Determinacion det2 where det2.idcaracter=20)";

								Determinacion detVR = (Determinacion)em.createQuery(hql).getSingleResult();
								idVRGA = detVR.getIden();
								
								
								// Una vez que ya tenemos el identificador del valor de referencia del Grupo de Aplicacion, podemos proceder a crearlo
								int idDet = 0;
								
								
								// Tengo que crear una CUSimplificadaDTO
								// Cargo una nueva
								CondicionUrbanisticaSimplificadaDTO cuSeleccionada = new CondicionUrbanisticaSimplificadaDTO();
								
								// Le meto los valores de entidad y determinacion de la CU
								cuSeleccionada.setIdEntidad(entidadTram.getIden());
								
								// Tengo que buscar cual es el id de la Determinacion de caracter 'Grupo de Aplicacion' del Tramite seleccionado
								
								
								idDet = obtenerIdDeterminacionGrupoAplicacionDeTramite(idTramiteBase);
								
								if (idDet!=0)
								{
									// Ha encontrado la determinacion del caracter grupo de aplicacion de ese tramite
									cuSeleccionada.setIdDeterminacion(idDet);
									
									
									// Meto el Valor de Referencia del Grupo Aplicacion
									cuSeleccionada.setIdDeterminacionValorReferencia(idVRGA);
									
									
									int idRegimen = crearCUGrupoAplicacion(cuSeleccionada);
									
									if (idRegimen>0)
									{
										log.info("[generarAutomaticamenteGrupoAplicacionParaEntidades] Condicion Urbanistica de Grupo de Aplicacion creada Correctamente para La Entidad (id="+entidadTram.getIden()+") '"+entidadTram.getNombre()+"'");
										gaAnadidos++;
									}
									else
									{
										resultado = 2;
										
										if (idRegimen==0)
											log.error("[generarAutomaticamenteGrupoAplicacionParaEntidades] Error al guardar la Condicion Urbanistica de Grupo de Aplicacion ");
										if (idRegimen==-1)
											log.error("[generarAutomaticamenteGrupoAplicacionParaEntidades] Error: La Entidad aun no tiene aplicado ningun Grupo de Aplicacion. Debe aplicar primero un Grupo a la entidad antes de construir otras Condiciones Urbanisticas");
									}
								}
								else
								{
									resultado = 2;
									log.error("[generarAutomaticamenteGrupoAplicacionParaEntidades] Error: No se ha encontrado la determinacion Grupo de Aplicacion para el tramite seleccionado o hay mas de una determinacion Grupo de Aplicacion para ese tramite");
								}
							}
							catch (NoResultException ex) {
								
								resultado = 4;
								log.error("[generarAutomaticamenteGrupoAplicacionParaEntidades] No se ha encontrado el id del valor de referencia del Grupo de Aplicacion. Tramite="+idTramite+ " clavebusqueda="+claveBusqueda);

								
							} catch (NonUniqueResultException ex1) {
								
								resultado = 4;
								log.error("[generarAutomaticamenteGrupoAplicacionParaEntidades] Se han encontrado mas de un id del valor de referencia del Grupo de Aplicacion.  Tramite="+idTramite+ " clavebusqueda="+claveBusqueda);

								
							}
							catch (Exception ex2) {
								resultado = 4;
								log.error("[generarAutomaticamenteGrupoAplicacionParaEntidades] Error al buscar el id del valor de referencia del Grupo de Aplicacion. Tramite="+idTramite+ " clavebusqueda="+claveBusqueda);
								ex2.printStackTrace();

								
							}
							
						}
						
						
					}
					
					
				}

			}

			catch (NoResultException ex) {
				
				log.warn("[generarAutomaticamenteGrupoAplicacionParaEntidades] No se ha encontrado entidades para el tramite ="+idTramite);
				resultado = 1;
				
			} 
	    	
	    	 log.info("[generarAutomaticamenteGrupoAplicacionParaEntidades]  -----   Grupos Aplicacion Añadidos = "+gaAnadidos);
	    	return resultado;
	    	
	    }
	    
	    
	    
	    public int entidadesSinGrupoAplicacion (int idTramite)
	    {
	    	int resultado = 0;
	    	
	    		    	
	    	List<Entidad> entidadesDelTramite;

			String query = " SELECT ent "
					+ " FROM Entidad ent"
					+ " WHERE ent.tramite.iden = " + idTramite;

			try {
				
				// Obtengo todas las entidades del tramite
				entidadesDelTramite = (List<Entidad>)em.createQuery(query).getResultList();				
				log.info("[entidadesSinGrupoAplicacion] Se han encontrado "+entidadesDelTramite.size() +" entidades para el tramite ="+idTramite);

				// Para cada entidad, hay que comprobar si tiene Grupo de Aplicacion aplicado
				for (Entidad entidadTram : entidadesDelTramite)
				{
					if (tieneAsignadaEntidadGrupoAplicacion(entidadTram.getIden()))
					{
						// Si tiene asignado Grupo Aplicacion no hacemos nada
					}
					else
					{
						// Si no tiene asignado Grupo Aplicacion, se lo asignamos de forma automática
						resultado++;
											
						
					}
					
					
				}

			}

			catch (NoResultException ex) {
				
				log.warn("[entidadesSinGrupoAplicacion] No se ha encontrado entidades para el tramite ="+idTramite);
				resultado = 1;
				
			} 
	    	
	    	 log.info("[entidadesSinGrupoAplicacion]  -----   Entidades Sin Grupos Aplicacion = "+resultado);
	    	return resultado;
	    	
	    }
	    
	    public List<CondicionUrbanisticaSimplificadaDTO> resultadosBusquedaAvanzadaCondicion(FiltrosDTO filtros, int idTramite) {
	        
	    	
	    	List<BusquedaCondicionDTO> result = new ArrayList<BusquedaCondicionDTO>();
	    	
	        // Obtengo el listado
	    	// Compruebo si ya existe una Entidaddeterminacion con esa determinacion y esa entidad
	    	String query="SELECT cuReg.iden," +
	    			"cuReg.casoentidaddeterminacionByIdcaso.entidaddeterminacion.entidad.iden," +
	         		"cuReg.casoentidaddeterminacionByIdcaso.entidaddeterminacion.entidad.nombre," +
		      		"cuReg.casoentidaddeterminacionByIdcaso.entidaddeterminacion.determinacion.iden," +
		      		"cuReg.casoentidaddeterminacionByIdcaso.entidaddeterminacion.determinacion.nombre " +		     		
					" FROM Entidaddeterminacionregimen cuReg " +      
				    " WHERE cuReg.casoentidaddeterminacionByIdcaso.entidaddeterminacion.entidad.tramite.iden = "+ idTramite;
	        
	    	 String clausulas="";
	         boolean puesto = false;
	         
	        if (filtros.getEntAplicada()!=null && !filtros.getEntAplicada().equals("")){
	        	if (puesto)
	        		clausulas = clausulas + " lower(cuReg.casoentidaddeterminacionByIdcaso.entidaddeterminacion.entidad.nombre) like '%" + filtros.getEntAplicada().toLowerCase() + "%' ";
	        	else{
	        		puesto = true;
	        		clausulas = clausulas + " lower(cuReg.casoentidaddeterminacionByIdcaso.entidaddeterminacion.entidad.nombre) like '%" + filtros.getEntAplicada().toLowerCase() + "%' ";
	        	}
	        }		
	        
	        if (filtros.getDetAplicada()!=null && !filtros.getDetAplicada().equals("")){
	        	if (puesto)
	        		clausulas = clausulas + filtros.getTipoFiltro() + " lower(cuReg.casoentidaddeterminacionByIdcaso.entidaddeterminacion.determinacion.nombre) like '%" + filtros.getDetAplicada().toLowerCase() + "%' ";
	        	else{
	        		puesto = true;
	        		clausulas = clausulas + " lower(cuReg.casoentidaddeterminacionByIdcaso.entidaddeterminacion.determinacion.nombre) like '%" + filtros.getDetAplicada().toLowerCase() + "%' ";
	        	}
	        }
	        
	        if (filtros.getValor()!=null && !filtros.getValor().equals("")){
	        	if (puesto)
	        		clausulas = clausulas + filtros.getTipoFiltro() + " lower(cuReg.valor) like '%" + filtros.getValor().toLowerCase() + "%' ";
	        	else{
	        		puesto = true;
	        		clausulas = clausulas + " lower(cuReg.valor) like '%" + filtros.getValor().toLowerCase() + "%' ";
	        	}
	        }
	        
	        if (filtros.getDetValorRef()!=null && !filtros.getDetValorRef().equals("")){
	        	if (puesto)
	        		clausulas = clausulas + filtros.getTipoFiltro() + " lower(cuReg.opciondeterminacion.determinacionByIddeterminacionvalorref.nombre) like '%" + filtros.getDetValorRef().toLowerCase() + "%' ";
	        	else{
	        		puesto = true;
	        		clausulas = clausulas + " lower(cuReg.opciondeterminacion.determinacionByIddeterminacionvalorref.nombre) like '%" + filtros.getDetValorRef().toLowerCase() + "%' ";
	        	}
	        }
	        
	        if (filtros.getDetRegimen()!=null && !filtros.getDetRegimen().equals("")){
	        	if (puesto)
	        		clausulas = clausulas + filtros.getTipoFiltro() + " lower(cuReg.determinacion.nombre) like '%" + filtros.getDetRegimen().toLowerCase() + "%' ";
	        	else{
	        		puesto = true;
	        		clausulas = clausulas + " lower(cuReg.determinacion.nombre) like '%" + filtros.getDetRegimen().toLowerCase() + "%' ";
	        	}
	        }
	        
	        if (puesto)
	        	query = query + " and (" + clausulas + ")"; 
	        query = query + " ORDER BY cuReg.iden ASC";
	         
	    	List<CondicionUrbanisticaSimplificadaDTO> cuSimplList = new ArrayList<CondicionUrbanisticaSimplificadaDTO> ();
	    	
	    	
	    	try
	    	{
	    		// Obtenemos los datos para la paginacion
	        	int maxResults = filtros.getMaxResultados();
	        	String consulta = "SELECT COUNT(*) FROM Entidaddeterminacionregimen cuReg " +      
				    " WHERE cuReg.casoentidaddeterminacionByIdcaso.entidaddeterminacion.entidad.tramite.iden = "+ idTramite;
	        	
	        	if (puesto){
	        		consulta += " and (" + clausulas + ")";
	        	}
	        	
	        	Long totalRegistros = (Long) em.createQuery(consulta).getSingleResult();     
	        	filtros.setTotalRegistros(totalRegistros);
	        	int firstResult = (filtros.getPagina() -1) * filtros.getMaxResultados();
	        	if(firstResult > totalRegistros)
	        		firstResult = 0;
	        	
	    		List<Object> regimenesCUTramite = em.createQuery(query)
	    		.setMaxResults(maxResults)
	    		.setFirstResult(firstResult)
	    		.getResultList();
	    		
	    		filtros.actualizarPaginas();
	    		
	    		for (Object regCUObjList : regimenesCUTramite)
	    		{
	    			// Por cada objeto creo un nuevo registro de CondicionUrbanisticaSimplificadaDTO
	    			
	    			CondicionUrbanisticaSimplificadaDTO cuSimpl = new CondicionUrbanisticaSimplificadaDTO();
	    			
	    			Object[] regCUArrayObj = (Object[]) regCUObjList;
	    			
	    			// El indice 1 se corresponde al id de la entidad de la CU
	    			cuSimpl.setIdEntidad((Integer)regCUArrayObj[1]);
	    			
	    			// El indice 2 se corresponde al nombre de la entidad de la CU
	    			cuSimpl.setNombreEntidad((String)regCUArrayObj[2]);
	    			
	    			// El indice 3 se corresponde al id de la determinacion de la CU
	    			cuSimpl.setIdDeterminacion((Integer)regCUArrayObj[3]);
	    			
	    			// El indice 4 se corresponde al nombre de la determinacion de la CU
	    			cuSimpl.setNombreDeterminacion((String)regCUArrayObj[4]);
	    			
	    			// El indice 4 se corresponde al id del regimen
	    			int idRegimen = (Integer)regCUArrayObj[0];
	    			cuSimpl.setIdRegimen(idRegimen);
	    			
	    			// Obtengo el regimen de la CU
	    			Entidaddeterminacionregimen cuReg = em.find(Entidaddeterminacionregimen.class, idRegimen);
	    			
	    			// Almaceno el valor
	    			cuSimpl.setValor(cuReg.getValor());
	    			
	    			// Almaceno, si existe, el nombre y id del valor de referencia
	    			if (cuReg.getOpciondeterminacion()!=null)
	    			{
	    				if (cuReg.getOpciondeterminacion().getDeterminacionByIddeterminacionvalorref()!=null)
		    			{
	    					cuSimpl.setIdDeterminacionValorReferencia(cuReg.getOpciondeterminacion().getDeterminacionByIddeterminacionvalorref().getIden());
	    					cuSimpl.setNombreDetValorRef(cuReg.getOpciondeterminacion().getDeterminacionByIddeterminacionvalorref().getNombre());
	    			
		    			}
	    			}
	    			
	    			// Almaceno, si existe, el nombre y id de la determinacion de regimen
	    			if (cuReg.getDeterminacion()!=null)
	    			{
	    				cuSimpl.setIdDeterminacionRegimen(cuReg.getDeterminacion().getIden());
	    				cuSimpl.setNombreDetRegimen(cuReg.getDeterminacion().getNombre());
	    			}
	    			
	    			// Obtengo los regimenes especificos del regimen
	    			Set<Regimenespecifico> regimenespecificos = cuReg.getRegimenespecificos();
	    			
	    			Iterator regespecificosIt = regimenespecificos.iterator();
	    			
	    			List<RegimenesEspecificosSimplificadosDTO> regEspecificosSimplList = new ArrayList<RegimenesEspecificosSimplificadosDTO> ();
	    			
	    			while (regespecificosIt.hasNext())
	    			{
	    				Regimenespecifico regesp = (Regimenespecifico)regespecificosIt.next();
	    				RegimenesEspecificosSimplificadosDTO regEspSimpl = new RegimenesEspecificosSimplificadosDTO(regesp.getNombre(), regesp.getTexto());
	    				regEspSimpl.setIdRegimenEspecifico(regesp.getIden());
	    				regEspecificosSimplList.add(regEspSimpl);
	    			}
	    			
	    			// Almaceno los regimenes especificos en el objeto a devolver
	    			cuSimpl.setRegimenesEspecificos(regEspecificosSimplList);
	    			
	    			boolean almacenar = true;
	    			
	    			// Comprobamos determinacion regimen
//	    			if (filtros.getTipoFiltro().equals("and") && !filtros.getDetRegimen().equals("")){
//	    				if (cuReg.getDeterminacion()!=null && cuReg.getDeterminacion().getNombre().toLowerCase().contains(filtros.getDetRegimen().toLowerCase()))
//	    					log.info("aplicado el filtro");
//	    				else
//	    					almacenar=false;
//	    			}
	    			//else if (filtros.getTipoFiltro().equals("or") && !filtros.getDetRegimen().equals("") && (cuReg.getDeterminacion()!=null && cuReg.getDeterminacion().getNombre().toLowerCase().contains(filtros.getDetRegimen().toLowerCase())))
	    				
	    			cuSimplList.add(cuSimpl);
	    			
	    			
	    		}
	    		
	    		
	    	}
	    	catch (Exception ex)
	    	{
	    		log.error("[listadoTodasCUSimplificadaDeEntidad] Fallo al buscar los Regimenes del tramite. ERROR:"+ex.getMessage());
	    		ex.printStackTrace();
	    	}
	    	
	        return cuSimplList;
	    }
	    
	    public List<Integer> entidadesDeTramiteAplicadasAUnGrupo (int idTramite, String codigoGrupo)
	    {
	    	log.debug("[entidadesDeTramiteAplicadasAUnGrupo]  idTramite="+idTramite+ " codigoGrupo="+codigoGrupo);
	    	List<Integer> listaEntidades = new ArrayList<Integer>();
	    	
	    	String queryCU = " select ent.iden from planeamiento.entidad ent "+
	    		" JOIN planeamiento.entidaddeterminacion ON ent.iden = entidaddeterminacion.identidad "+
	    		" JOIN planeamiento.casoentidaddeterminacion ON entidaddeterminacion.iden = casoentidaddeterminacion.identidaddeterminacion "+
	    		" JOIN planeamiento.entidaddeterminacionregimen ON casoentidaddeterminacion.iden = entidaddeterminacionregimen.idcaso "+
	    		" JOIN planeamiento.opciondeterminacion ON entidaddeterminacionregimen.idopciondeterminacion = opciondeterminacion.iden "+
	    		" JOIN planeamiento.determinacion  ON opciondeterminacion.iddeterminacionvalorref = determinacion.iden "+
	    		" JOIN planeamiento.determinacion determinacionaplicada ON entidaddeterminacion.iddeterminacion = determinacionaplicada.iden "+
	    		" where ent.idtramite = "+idTramite +" and determinacion.codigo='"+codigoGrupo+"'";



	    	try {
	    		listaEntidades = (List<Integer>)em.createNativeQuery(queryCU).getResultList();
	    	
		    } catch (NoResultException e) {
	            log.warn("[obtenerDeterminacionesAplicablesComoCU] No se han encontrado entidadesDeTramiteAplicadasAUnGrupo.  idTramite="+idTramite+ " codigoGrupo="+codigoGrupo+"\n" + e.getMessage());
	
	        } catch (Exception ex) {
	            log.error("[obtenerDeterminacionesAplicablesComoCU] Error en la busqueda de entidadesDeTramiteAplicadasAUnGrupo  idTramite="+idTramite+ " codigoGrupo="+codigoGrupo+"\n" + ex.getMessage());
	            ex.printStackTrace();
	        }
	    	





	    	
	    	log.debug("[entidadesDeTramiteAplicadasAUnGrupo]  numero entidades aplicadas="+listaEntidades.size());
	    	return listaEntidades;
	    }
   
}

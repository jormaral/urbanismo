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

package com.mitc.redes.editorfip.servicios.gestionfip.validaciones;

import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.faces.FacesManager;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.international.StatusMessage.Severity;
import org.jboss.seam.log.Log;

import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Tramite;
import com.mitc.redes.editorfip.entidades.rpm.validacion.Proceso;
import com.mitc.redes.editorfip.entidades.rpm.validacion.Resultado;
import com.mitc.redes.editorfip.entidades.rpm.validacion.Error;
import com.mitc.redes.editorfip.servicios.sesion.VariablesSesionUsuario;

@Stateless
@Name("listaProcesosValidacionNuevo")
public class ListaProcesosValidacionNuevoBean  implements ListaProcesosValidacionNuevo {

	
	private List<Proceso> listaProcesos;
	
	@Logger private Log log;
	
	@PersistenceContext
	EntityManager em;
	
	@In (create = true)
	FacesMessages facesMessages;
	
	@In (create = true)
	ServicioGestionValidacionesNuevo servicioGestionValidacionesNuevo;
	
	
	@In
    VariablesSesionUsuario variablesSesionUsuario;

	
	
	public void refrescarLista()
	{
		log.debug("[refrescarLista] Refresco la lista de Procesos");
		int idTramiteVigente = variablesSesionUsuario.getIdTramiteVigenteTrabajo();
    	int idTramiteEncargado = variablesSesionUsuario.getIdTramiteEncargadoTrabajo();
    	
    	String queryListadoNormas = " select proc from Proceso proc where proc.idTramite="
    		+idTramiteVigente+" or proc.idTramite="+idTramiteEncargado+" order by proc.inicio desc";
    	listaProcesos = em.createQuery(queryListadoNormas).getResultList();
    	
    	log.info("[refrescarLista] Elementos listaProcesos="+listaProcesos.size());
	}

	@SuppressWarnings("unchecked")
	public List<Proceso> getListaProcesos() {
		
		
		if(listaProcesos == null) 
		{
			log.debug("[getListaProcesos] Peticion de la lista de Procesos");
			refrescarLista();		
		}
		
		log.debug("[getListaProcesos] Numero de Procesos Obtenidos= "+listaProcesos.size());
		return listaProcesos;
	}
	
	
	
	public void  borrarProcVal(int idProceso) {
		log.debug("[borrarProcVal] Borrar los elementos del Proceso: "+idProceso);
		
		try
		{
			
			//Long idTramiteLong = new Long(idProceso);
			Proceso proc = em.find(Proceso.class, idProceso);
			
			// Borra todos los elementos
			
			// Por cada proceso tengo que ver si hay errores y borrar los registros
			List<Error> errorResultadoLista = em.createQuery("select err from Error err where err.resultado.proceso.iden="+idProceso).getResultList(); 
			log.debug("[borrarProcVal] elemento errorResultadoLista: "+errorResultadoLista.size());
			
			for (Error err : errorResultadoLista)
			{
				
				em.remove(err);
			
			}
			
			// Por cada resultado tengo que ver si hay errores y borrar los registros
			List<Resultado> resultadoProcLista = em.createQuery("select res from Resultado res where res.proceso.iden="+idProceso).getResultList();
			
			log.debug("[borrarProcVal] elemento resultadoProcLista: "+resultadoProcLista.size());
			
			for (Resultado res : resultadoProcLista)
			{
				
				em.remove(res);
			
			}
			
			
			
			em.remove(proc);
			em.flush();
			
			// refresco la lista
			refrescarLista();
			
			// Informo por pantalla
			facesMessages.addFromResourceBundle(Severity.INFO,"Se ha borrado correctamente", null);
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
			facesMessages.addFromResourceBundle(Severity.ERROR,"Error en el proceso de borrado", null);
			
			
		}
		
		
		
	}
	
	
	
	public void validarTramiteSincrono(int idTramite) {

		try {
			log.debug("[validarTramiteSincrono] Se inicia el proceso de validación para idTramite="+idTramite);
			// ------------------------
			// Creo un nuevo Proceso de validacion
			// ------------------------
			
			// Instancio el objeto
			Proceso procesoValidacion = new Proceso();
			
			// Obtengo la referencia al idTramite
			//Tramite tram = em.find(Tramite.class, idTramite);
			procesoValidacion.setIdTramite(idTramite);
			
			// Compruebo si hay algun proceso de validacion anterior de este trámite
			int iteracionProceso = 1;
			
			String queryNumeroIteracion =  "SELECT max(proc.iteracionValidacion) " +
	                " FROM Proceso proc " +
	                " WHERE proc.idTramite=" +idTramite;

	        

	         try {
	        	 iteracionProceso = (Integer) em.createQuery(queryNumeroIteracion).getSingleResult();
	        } catch (NoResultException e) {
	            log.info("[validarTramiteSincrono] No se ha iteracion para el tramite. Se le asignará 1." + e.getMessage());

	        } catch (Exception ex) {
	            log.warn("[validarTramiteSincrono] Error: No se ha iteracion para el tramite. Se le asignará 1 " + ex.getMessage());

	        }
			
			procesoValidacion.setIteracionValidacion(iteracionProceso + 1);
			
			// Establezco la fecha
			procesoValidacion.setInicio(new Date());
			
			// Establezco el estado
			procesoValidacion.setEstado("VALIDANDO");
			
			// Guardo el CodigoFIP
			Tramite tram = em.find(Tramite.class, idTramite);
			procesoValidacion.setIdfip(tram.getCodigofip());
			
			
			
			// Persisto el proceso en base de datos
			
			em.persist(procesoValidacion);
			em.flush();

			log.debug("[validarTramite] Guardado en BD nuevo proceso de validación para idTramite="
					+ idTramite);
			
			// Llamo a la funcion asincrona
			servicioGestionValidacionesNuevo.validarTramiteAsincrono(idTramite, procesoValidacion);
			
			
			// Aviso por pantalla
			facesMessages.addFromResourceBundle(Severity.INFO,"Se esta generando la validacion del plan encargado. Vaya al listado de validaciones para mas informacion", null);
			
		} catch (Exception e) {
			e.printStackTrace();
			facesMessages.clear();

			facesMessages.addFromResourceBundleOrDefault(Severity.ERROR,
					"error_validacion_tramite",
					"Se ha producido el siguiente error al validar el (los) tramite(s): "
							+ e.getMessage(), null);
		}

		

	}
}

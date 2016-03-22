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

import com.mitc.redes.editorfip.entidades.rpm.validaciones.ElementoValidado;
import com.mitc.redes.editorfip.entidades.rpm.validaciones.ProcesoValidacion;
import com.mitc.redes.editorfip.servicios.sesion.VariablesSesionUsuario;

@Stateless
@Name("listaProcesosValidacion")
public class ListaProcesosValidacionBean  implements ListaProcesosValidacion {

	
	private List<ProcesoValidacion> listaProcesos;
	
	@Logger private Log log;
	
	@PersistenceContext
	EntityManager em;
	
	@In (create = true)
	FacesMessages facesMessages;
	
	@In (create = true)
	ListaElementosValidados listaElementosValidados;
	
	@In (create = true)
	ServicioGestionValidaciones servicioGestionValidaciones;
	
	@In (create = true)
	GestionArbolValidacionnTramite gestionArbolValidacionTramite;
	
	@In
    VariablesSesionUsuario variablesSesionUsuario;

	public void setListaProcesos(List<ProcesoValidacion> listaProcesos) {
		this.listaProcesos = listaProcesos;
	}
	
	public void refrescarLista()
	{
		log.debug("[refrescarLista] Refresco la lista de Procesos");
		int idTramiteVigente = variablesSesionUsuario.getIdTramiteVigenteTrabajo();
    	int idTramiteEncargado = variablesSesionUsuario.getIdTramiteEncargadoTrabajo();
    	
    	String queryListadoNormas = " select normas from ProcesoValidacion normas where normas.idTramite="
    		+idTramiteVigente+" or normas.idTramite="+idTramiteEncargado+" order by normas.fechaRealizada desc";
    	listaProcesos = em.createQuery(queryListadoNormas).getResultList();
    	
    	log.info("[refrescarLista] Elementos listaProcesos="+listaProcesos.size());
	}

	@SuppressWarnings("unchecked")
	public List<ProcesoValidacion> getListaProcesos() {
		
		
		if(listaProcesos == null) 
		{
			log.debug("[getListaProcesos] Peticion de la lista de Procesos");
			refrescarLista();		
		}
		
		log.debug("[getListaProcesos] Numero de Procesos Obtenidos= "+listaProcesos.size());
		return listaProcesos;
	}
	
	public void verElementosVal(int idProceso) {
		log.debug("[verElementosVal] Ver los elementos del Proceso: "+idProceso);
		listaElementosValidados.setIdProceso(idProceso);
		listaElementosValidados.setFiltroVal("T");
		listaElementosValidados.setListaElementos(null);
		FacesManager.instance().redirect("/gestionfip/validaciones/ElementosValidadosList.xhtml");
	}
	
	public void  borrarProcVal(int idProceso) {
		log.debug("[borrarProcVal] Borrar los elementos del Proceso: "+idProceso);
		
		try
		{
			
			Long idTramiteLong = new Long(idProceso);
			ProcesoValidacion proc = em.find(ProcesoValidacion.class, idTramiteLong);
			
			// Borra todos los elementos
			List<ElementoValidado> elementoValidadoLista = em.createQuery("select ev from ElementoValidado ev where ev.procesoValidacion.id="+idTramiteLong).getResultList();
			log.debug("[borrarProcVal] elementoValidadoLista: "+elementoValidadoLista.size());
			
			for (ElementoValidado elval : elementoValidadoLista)
			{
				em.remove(elval);
			
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
	
	public void validarTramite() {
		
		try {
			List<Integer> idsTramites = gestionArbolValidacionTramite.obtenerIdsTramitesSeleccionados();
			for(Integer id : idsTramites) {
				servicioGestionValidaciones.validarTramite(id);
			}			
			facesMessages.clear();
			facesMessages.addFromResourceBundleOrDefault(Severity.INFO, "validacion_tramite_ok", "Se han validado el(los) tramite(s) correctamente. ", null);
		} catch (Exception e) {
			e.printStackTrace();
			facesMessages.clear();
			facesMessages.addFromResourceBundleOrDefault(Severity.ERROR, "error_validacion_tramite", "Se ha producido el siguiente error al validar el (los) tramite(s): " + e.getMessage(), null);
		}
		
		FacesManager.instance().redirect("/gestionfip/validaciones/ProcesoValidacionList.xhtml");
		
	}
	
	public void validarTramiteSincrono(int idTramite) {

		try {
			log.debug("[validarTramite] Se inicia el proceso de validación para idTramite="
					+ idTramite);
			// ------------------------
			// Creo un nuevo Proceso de validacion
			// ------------------------

			// Instancio el objeto
			ProcesoValidacion procesoValidacion = new ProcesoValidacion();

			// Obtengo la referencia al idTramite
			//Tramite tram = em.find(Tramite.class, idTramite);
			procesoValidacion.setIdTramite(idTramite);

			// Compruebo si hay algun proceso de validacion anterior de este
			// trámite
			int iteracionProceso = 1;

			String queryNumeroIteracion = "SELECT max(proc.iteracionValidacion) "
					+ " FROM ProcesoValidacion proc "
					+ " WHERE proc.idTramite=" + idTramite;

			try {
				iteracionProceso = (Integer) em.createQuery(
						queryNumeroIteracion).getSingleResult();
			} catch (NoResultException e) {
				log.info("[validarTramite] No se ha iteracion para el tramite. Se le asignará 1."
						+ e.getMessage());

			} catch (Exception ex) {
				log.warn("[validarTramite] Error: No se ha iteracion para el tramite. Se le asignará 1 "
						+ ex.getMessage());

			}

			procesoValidacion.setIteracionValidacion(iteracionProceso + 1);

			// Establezco la fecha
			procesoValidacion.setFechaRealizada(new Date());

			// Establezco el estado
			procesoValidacion.setEstado("VALIDANDO");

			// Persisto el proceso en base de datos

			em.persist(procesoValidacion);
			em.flush();

			log.debug("[validarTramite] Guardado en BD nuevo proceso de validación para idTramite="
					+ idTramite);
			
			// Llamo a la funcion asincrona
			servicioGestionValidaciones.validarTramiteAsincrono(idTramite, procesoValidacion);
			
			
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

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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.async.Asynchronous;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.international.StatusMessage.Severity;
import org.jboss.seam.log.Log;

import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Tramite;
import com.mitc.redes.editorfip.entidades.rpm.validaciones.ElementoValidado;
import com.mitc.redes.editorfip.entidades.rpm.validaciones.ProcesoValidacion;
import com.mitc.redes.editorfip.entidades.rpm.validaciones.TipoValidacion;

@Stateless
@Name("servicioGestionValidaciones")
public class ServicioGestionValidacionesBean implements ServicioGestionValidaciones
{
    @Logger private Log log;
    
    @PersistenceContext
	EntityManager em;
    
    @In Map<String,String> messages;
    
    @In (create = true)
	FacesMessages facesMessages;
    
    
    // ------------------------------
    // GLOBAL
    // ------------------------------	

	/* (non-Javadoc)
	 * @see com.mitc.redes.editorfip.servicios.gestionfip.validaciones.ServicioGestionValidaciones#validarTramite(int)
	 */
    @Asynchronous
    public void validarTramiteAsincrono (int idTramite, ProcesoValidacion procesoValidacion)
	{
		log.debug("[validarTramite] Se inicia el proceso de validación para idTramite="+idTramite);
		
		
		// Llamo a las distintas validaciones y las voy agregando
		List<ElementoValidado> elementosValidados = new ArrayList<ElementoValidado>();
		
		// Validaciones de TRAMITE
		elementosValidados.clear();
		elementosValidados = validacionesTramite(idTramite,procesoValidacion);
		for (ElementoValidado elemVal: elementosValidados)
		{
			em.joinTransaction();
			em.persist(elemVal);
		}
		log.debug("[validarTramite] Guardado en BD las validaciones de TRAMITE para idTramite="+idTramite);
		
		// Validaciones de DOCUMENTO
		elementosValidados.clear();
		elementosValidados = validacionesDocumentos(idTramite,procesoValidacion);
		for (ElementoValidado elemVal: elementosValidados)
		{
			em.joinTransaction();
			em.persist(elemVal);
		}
		log.debug("[validarTramite] Guardado en BD las validaciones de DOCUMENTO para idTramite="+idTramite);
		
		// Validaciones de ENTIDADES
		elementosValidados.clear();
		elementosValidados = validacionesEntidades(idTramite,procesoValidacion);
		for (ElementoValidado elemVal: elementosValidados)
		{
			em.joinTransaction();
			em.persist(elemVal);
		}
		log.debug("[validarTramite] Guardado en BD las validaciones de ENTIDADES para idTramite="+idTramite);
		
		// Validaciones de DETERMINACIONES
		elementosValidados.clear();
		elementosValidados = validacionesDeterminaciones(idTramite,procesoValidacion);
		for (ElementoValidado elemVal: elementosValidados)
		{
			em.joinTransaction();
			em.persist(elemVal);
		}
		log.debug("[validarTramite] Guardado en BD las validaciones de DETERMINACIONES para idTramite="+idTramite);
		
		// Validaciones de CONDICIONES URBANISTICAS
		elementosValidados.clear();
		elementosValidados = validacionesCondicionesUrbanisticas(idTramite,procesoValidacion);
		for (ElementoValidado elemVal: elementosValidados)
		{
			em.joinTransaction();
			em.persist(elemVal);
		}
		log.debug("[validarTramite] Guardado en BD las validaciones de CONDICIONES URBANISTICAS para idTramite="+idTramite);
		
		// Validaciones de OPERACIONES
		elementosValidados.clear();
		elementosValidados = validacionesOperaciones(idTramite,procesoValidacion);
		for (ElementoValidado elemVal: elementosValidados)
		{
			em.joinTransaction();
			em.persist(elemVal);
		}
		log.debug("[validarTramite] Guardado en BD las validaciones de OPERACIONES para idTramite="+idTramite);
		
		// Validaciones de ADSCRIPCIONES
		elementosValidados.clear();
		elementosValidados = validacionesAdscripciones(idTramite,procesoValidacion);
		for (ElementoValidado elemVal: elementosValidados)
		{
			em.joinTransaction();
			em.persist(elemVal);
		}
		log.debug("[validarTramite] Guardado en BD las validaciones de ADSCRIPCIONES para idTramite="+idTramite);
		
		
		// Establezco el estado
		procesoValidacion.setEstado("VALIDADO");
		em.merge(procesoValidacion);
		log.info("[validarTramite] FIN del proceso de validación para idTramite="+idTramite);
		
		
		
		
	}
    
    
	public void validarTramite (int idTramite)
	{
		log.debug("[validarTramite] Se inicia el proceso de validación para idTramite="+idTramite);
		// ------------------------
		// Creo un nuevo Proceso de validacion
		// ------------------------
		
		// Instancio el objeto
		ProcesoValidacion procesoValidacion = new ProcesoValidacion();
		
		// Obtengo la referencia al idTramite
		//Tramite tram = em.find(Tramite.class, idTramite);
		procesoValidacion.setIdTramite(idTramite);
		
		// Compruebo si hay algun proceso de validacion anterior de este trámite
		int iteracionProceso = 1;
		
		String queryNumeroIteracion =  "SELECT max(proc.iteracionValidacion) " +
                " FROM ProcesoValidacion proc " +
                " WHERE proc.idTramite=" +idTramite;

        

         try {
        	 iteracionProceso = (Integer) em.createQuery(queryNumeroIteracion).getSingleResult();
        } catch (NoResultException e) {
            log.info("[validarTramite] No se ha iteracion para el tramite. Se le asignará 1." + e.getMessage());

        } catch (Exception ex) {
            log.warn("[validarTramite] Error: No se ha iteracion para el tramite. Se le asignará 1 " + ex.getMessage());

        }
		
		procesoValidacion.setIteracionValidacion(iteracionProceso + 1);
		
		// Establezco la fecha
		procesoValidacion.setFechaRealizada(new Date());
		
		// Establezco el estado
		procesoValidacion.setEstado("VALIDANDO...");
		
		// Persisto el proceso en base de datos
		
		em.persist(procesoValidacion);
		log.debug("[validarTramite] Guardado en BD nuevo proceso de validación para idTramite="+idTramite);
		
		// Llamo a las distintas validaciones y las voy agregando
		List<ElementoValidado> elementosValidados = new ArrayList<ElementoValidado>();
		
		// Validaciones de TRAMITE
		elementosValidados.clear();
		elementosValidados = validacionesTramite(idTramite,procesoValidacion);
		for (ElementoValidado elemVal: elementosValidados)
		{
			em.joinTransaction();
			em.persist(elemVal);
		}
		log.debug("[validarTramite] Guardado en BD las validaciones de TRAMITE para idTramite="+idTramite);
		
		// Validaciones de DOCUMENTO
		elementosValidados.clear();
		elementosValidados = validacionesDocumentos(idTramite,procesoValidacion);
		for (ElementoValidado elemVal: elementosValidados)
		{
			em.joinTransaction();
			em.persist(elemVal);
		}
		log.debug("[validarTramite] Guardado en BD las validaciones de DOCUMENTO para idTramite="+idTramite);
		
		// Validaciones de ENTIDADES
		elementosValidados.clear();
		elementosValidados = validacionesEntidades(idTramite,procesoValidacion);
		for (ElementoValidado elemVal: elementosValidados)
		{
			em.joinTransaction();
			em.persist(elemVal);
		}
		log.debug("[validarTramite] Guardado en BD las validaciones de ENTIDADES para idTramite="+idTramite);
		
		// Validaciones de DETERMINACIONES
		elementosValidados.clear();
		elementosValidados = validacionesDeterminaciones(idTramite,procesoValidacion);
		for (ElementoValidado elemVal: elementosValidados)
		{
			em.joinTransaction();
			em.persist(elemVal);
		}
		log.debug("[validarTramite] Guardado en BD las validaciones de DETERMINACIONES para idTramite="+idTramite);
		
		// Validaciones de CONDICIONES URBANISTICAS
		elementosValidados.clear();
		elementosValidados = validacionesCondicionesUrbanisticas(idTramite,procesoValidacion);
		for (ElementoValidado elemVal: elementosValidados)
		{
			em.joinTransaction();
			em.persist(elemVal);
		}
		log.debug("[validarTramite] Guardado en BD las validaciones de CONDICIONES URBANISTICAS para idTramite="+idTramite);
		
		// Validaciones de OPERACIONES
		elementosValidados.clear();
		elementosValidados = validacionesOperaciones(idTramite,procesoValidacion);
		for (ElementoValidado elemVal: elementosValidados)
		{
			em.joinTransaction();
			em.persist(elemVal);
		}
		log.debug("[validarTramite] Guardado en BD las validaciones de OPERACIONES para idTramite="+idTramite);
		
		// Validaciones de ADSCRIPCIONES
		elementosValidados.clear();
		elementosValidados = validacionesAdscripciones(idTramite,procesoValidacion);
		for (ElementoValidado elemVal: elementosValidados)
		{
			em.joinTransaction();
			em.persist(elemVal);
		}
		log.debug("[validarTramite] Guardado en BD las validaciones de ADSCRIPCIONES para idTramite="+idTramite);
		
		
		// Establezco el estado
		procesoValidacion.setEstado("VALIDADO");
		em.merge(procesoValidacion);
		log.info("[validarTramite] FIN del proceso de validación para idTramite="+idTramite);
		facesMessages.addFromResourceBundle(Severity.INFO,"Proceso de validado completado", null);
		
		
		
		
	}
	
	/* (non-Javadoc)
	 * @see com.mitc.redes.editorfip.servicios.gestionfip.validaciones.ServicioGestionValidaciones#obtenerValidacionesProceso(int,String)
	 */
	public List<ElementoValidado> obtenerValidacionesProceso(int idProceso, String filtro) {
		
		String consulta = "FROM ElementoValidado ev WHERE ev.procesoValidacion.id = " + idProceso;
		
		if(filtro.equalsIgnoreCase("V"))
			consulta += " AND ev.validacionok = 'true' ";
		else if (filtro.equalsIgnoreCase("NV"))
			consulta += " AND ev.validacionok = 'false' ";
		
		consulta += " ORDER BY ev.tipoValidacion.nombrevalidacion, ev.objetovalidado";
		
		List<ElementoValidado> elementos;
		
		try {
			if(idProceso != 0) {
				elementos = (List<ElementoValidado>) em.createQuery(consulta).getResultList();
			} else {
				elementos = new ArrayList<ElementoValidado>();
			}
		} catch (Exception e) {
			log.error(e.getStackTrace(), null);
			elementos = new ArrayList<ElementoValidado>();
		}
			
		
		return elementos;
	}
	
	
	// ------------------------------
    // VALIDACIONES TRAMITE
    // ------------------------------	
	
	/* (non-Javadoc)
	 * @see com.mitc.redes.editorfip.servicios.gestionfip.validaciones.ServicioGestionValidaciones#validacionesTramite(int)
	 */
	public List<ElementoValidado> validacionesTramite(int idTramite, ProcesoValidacion procVal) {
		log.debug("[validacionesTramite] validacionesTramite para idTramite="+ idTramite);
		List<ElementoValidado> resultado = new ArrayList<ElementoValidado>();

		//

		// Obtengo el tipo de validacion
		TipoValidacion tipoVal=null;

		String queryTipoValidacion = "SELECT tipo "
				+ " FROM TipoValidacion tipo " 
				+ " WHERE tipo.codigo='TRA'";

		try {
			tipoVal = (TipoValidacion) em.createQuery(queryTipoValidacion).getSingleResult();
		} catch (NoResultException e) {
			log.error("[validacionesTramite] No se ha encontrado el TipoValidacion."+ e.getMessage());

		} catch (Exception ex) {
			log.error("[validacionesTramite] Error en la busqueda del TipoValidacion: "+ ex.getMessage());

		}
		
		// Agrego las validaciones de los subapartados
		List<ElementoValidado> resulValidacion211 = validacion211(idTramite,procVal);
		resultado.addAll(resulValidacion211);
		
		List<ElementoValidado> resulValidacion212 = validacion212(idTramite,procVal);
		resultado.addAll(resulValidacion212);
		
		List<ElementoValidado> resulValidacion213 = validacion213(idTramite,procVal);
		resultado.addAll(resulValidacion213);
		
		// Si hay tipo de validacion, recorro todos los resultados estableciendolo
		if (tipoVal!=null)
		{
			for (ElementoValidado elemVal: resultado)
			{
				elemVal.setTipoValidacion(tipoVal);
			}
		}
		

		log.debug("[validacionesTramite] elementos validados="+ resultado.size());
		return resultado;
	}
	
	
	/* (non-Javadoc)
	 * @see com.mitc.redes.editorfip.servicios.gestionfip.validaciones.ServicioGestionValidaciones#validacion211(int)
	 */
	public List<ElementoValidado> validacion211 (int idTramite, ProcesoValidacion procVal)
	{
		log.debug("[validacion211] validacion211 para idTramite="+idTramite);
		List<ElementoValidado> resultado = new ArrayList<ElementoValidado>();
		
		String nombreValidacion = (String)messages.get("validacion_211_nombre");
		
		
		 String queryValidacion211 =  "SELECT count(det) " +
         " FROM Determinacion det " +
         " WHERE det.tramite.iden=" +idTramite;

		 long numDeterminaciones = 0;

		  try {
		     numDeterminaciones = (Long) em.createQuery(queryValidacion211).getSingleResult();
		     
		     
		     
		 } catch (NoResultException e) {
		     log.error("[validacion211] No se ha encontrado validacion211." + e.getMessage());
		
		 } catch (Exception ex) {
		     log.error("[validacion211] Error en la busqueda del validacion211: " + ex.getMessage());
		
		 }
		 
		 
		 // Creo el objeto resultado
		 ElementoValidado elemVal = new ElementoValidado();
		 
		 elemVal.setNombrevalidacion(nombreValidacion);
		 elemVal.setProcesoValidacion(procVal);
		 elemVal.setObjetovalidado("Tramite id="+idTramite);
		 
		 // Validacion Correcta
		 if (numDeterminaciones>0)
	     {
			elemVal.setValidacionok(true);
			elemVal.setObservacionesvalidacion("Numero Determinaciones="+numDeterminaciones);
	     }
		 else
		 {
			 // Validacion Incorrecta
			 elemVal.setValidacionok(false);
			 
		 }
		 
		 // Lo añado al resultado
		 resultado.add(elemVal);
		 
		
		log.debug("[validacion211] elementos validados="+resultado.size());
		return resultado;
	}
	
	/* (non-Javadoc)
	 * @see com.mitc.redes.editorfip.servicios.gestionfip.validaciones.ServicioGestionValidaciones#validacion212(int)
	 */
	public List<ElementoValidado> validacion212 (int idTramite, ProcesoValidacion procVal)
	{
		log.debug("[validacion212] validacion212 para idTramite="+idTramite);
		List<ElementoValidado> resultado = new ArrayList<ElementoValidado>();
		
		String nombreValidacion = (String)messages.get("validacion_212_nombre");
		
		
		 String queryValidacion212 =  "SELECT count(ent) " +
        " FROM Entidad ent " +
        " WHERE ent.tramite.iden=" +idTramite;

		 long numEntidades = 0;

		  try {
			  numEntidades = (Long) em.createQuery(queryValidacion212).getSingleResult();
		     
		     
		     
		 } catch (NoResultException e) {
		     log.error("[validacion212] No se ha encontrado validacion212." + e.getMessage());
		
		 } catch (Exception ex) {
		     log.error("[validacion212] Error en la busqueda del validacion212: " + ex.getMessage());
		
		 }
		 
		 
		 // Creo el objeto resultado
		 ElementoValidado elemVal = new ElementoValidado();
		 
		 elemVal.setNombrevalidacion(nombreValidacion);
		 elemVal.setProcesoValidacion(procVal);
		 elemVal.setObjetovalidado("Tramite id="+idTramite);
		 
		 // Validacion Correcta
		 if (numEntidades>0)
	     {
			elemVal.setValidacionok(true);
			elemVal.setObservacionesvalidacion("Numero Entidades="+numEntidades);
	     }
		 else
		 {
			 // Validacion Incorrecta
			 elemVal.setValidacionok(false);
			 
		 }
		 
		 // Lo añado al resultado
		 resultado.add(elemVal);
		
		log.debug("[validacion212] elementos validados="+resultado.size());
		return resultado;
	}
	
	/* (non-Javadoc)
	 * @see com.mitc.redes.editorfip.servicios.gestionfip.validaciones.ServicioGestionValidaciones#validacion213(int)
	 */
	public List<ElementoValidado> validacion213 (int idTramite, ProcesoValidacion procVal)
	{
		log.debug("[validacion213] validacion213 para idTramite="+idTramite);
		List<ElementoValidado> resultado = new ArrayList<ElementoValidado>();
		
		String nombreValidacion = (String)messages.get("validacion_213_nombre");
		
		
		 String queryValidacion213 =  "SELECT tram.codigofip " +
								       " FROM Tramite tram " +
								       " WHERE tram.iden=" +idTramite;

		 String codigoFIP = "";

		  try {
			  codigoFIP = (String) em.createQuery(queryValidacion213).getSingleResult();
		     
		     
		     
		 } catch (NoResultException e) {
		     log.error("[validacion213] No se ha encontrado validacion213." + e.getMessage());
		
		 } catch (Exception ex) {
		     log.error("[validacion213] Error en la busqueda del validacion213: " + ex.getMessage());
		
		 }
		 
		 
		 // Creo el objeto resultado
		 ElementoValidado elemVal = new ElementoValidado();
		 
		 elemVal.setNombrevalidacion(nombreValidacion);
		 elemVal.setProcesoValidacion(procVal);
		 elemVal.setObjetovalidado("Tramite id="+idTramite);
		 
		 // Validacion Correcta
		 if (codigoFIP.length()==32)
	     {
			elemVal.setValidacionok(true);
			elemVal.setObservacionesvalidacion("Codigo FIP="+codigoFIP);
	     }
		 else
		 {
			 // Validacion Incorrecta
			 elemVal.setValidacionok(false);
			 elemVal.setObservacionesvalidacion("Codigo FIP mal formado. No tiene 32 digitos: "+codigoFIP);
			 
		 }
		 
		 // Lo añado al resultado
		 resultado.add(elemVal);
		 
		
		log.debug("[validacion213] elementos validados="+resultado.size());
		return resultado;
	}
	
	
	// ------------------------------
    // VALIDACIONES DOCUMENTOS
    // ------------------------------	
	
	/* (non-Javadoc)
	 * @see com.mitc.redes.editorfip.servicios.gestionfip.validaciones.ServicioGestionValidaciones#validacionesDocumentos(int)
	 */
	public List<ElementoValidado> validacionesDocumentos (int idTramite, ProcesoValidacion procVal)
	{
		log.debug("[validacionesDocumentos] validacionesDocumentos para idTramite="+ idTramite);
		List<ElementoValidado> resultado = new ArrayList<ElementoValidado>();

		//

		// Obtengo el tipo de validacion
		TipoValidacion tipoVal=null;

		String queryTipoValidacion = "SELECT tipo "
				+ " FROM TipoValidacion tipo " 
				+ " WHERE tipo.codigo='DOC'";

		try {
			tipoVal = (TipoValidacion) em.createQuery(queryTipoValidacion).getSingleResult();
		} catch (NoResultException e) {
			log.error("[validacionesDocumentos] No se ha encontrado el TipoValidacion."+ e.getMessage());

		} catch (Exception ex) {
			log.error("[validacionesDocumentos] Error en la busqueda del TipoValidacion: "+ ex.getMessage());

		}
		
		// Agrego las validaciones de los subapartados
		List<ElementoValidado> resulValidacion221 = validacion221(idTramite,procVal);
		resultado.addAll(resulValidacion221);
		
		List<ElementoValidado> resulValidacion222 = validacion222(idTramite,procVal);
		resultado.addAll(resulValidacion222);
		
		
		
		// Si hay tipo de validacion, recorro todos los resultados estableciendolo
		if (tipoVal!=null)
		{
			for (ElementoValidado elemVal: resultado)
			{
				elemVal.setTipoValidacion(tipoVal);
			}
		}
		

		log.debug("[validacionesDocumentos] elementos validados="+ resultado.size());
		return resultado;
	}
	
	/* (non-Javadoc)
	 * @see com.mitc.redes.editorfip.servicios.gestionfip.validaciones.ServicioGestionValidaciones#validacion221(int)
	 */
	public List<ElementoValidado> validacion221 (int idTramite, ProcesoValidacion procVal)
	{
		log.debug("[validacion221] validacion221 para idTramite="+idTramite);
		List<ElementoValidado> resultado = new ArrayList<ElementoValidado>();
		
		String nombreValidacion = (String)messages.get("validacion_221_nombre");
		
		
		String queryValidacion221 =  "select doc.nombre,doc.idgrupodocumento,doc.idtipodocumento " +
		 		"from planeamiento.documento doc where doc.idtramite = " +idTramite+
		 		" group by  doc.nombre,doc.idgrupodocumento,doc.idtipodocumento " +
		 		"having (count(doc.idgrupodocumento)>1 or count(doc.idgrupodocumento)>1 or count(doc.nombre)>1)";

		 List<Object[]> resConsulta = new ArrayList<Object[]>();

		  try {
			  resConsulta = (List<Object[]>) em.createNativeQuery(queryValidacion221).getResultList();
		     
		     
		     
		 } catch (NoResultException e) {
		     log.error("[validacion221] No se ha encontrado validacion221." + e.getMessage());
		
		 } catch (Exception ex) {
		     log.error("[validacion221] Error en la busqueda del validacion221: " + ex.getMessage());
		
		 }
		 
		 
		 // Validacion Correcta
		 if (resConsulta.size()==0)
	     {
			// Creo el objeto resultado
			 ElementoValidado elemVal = new ElementoValidado();
			 
			 elemVal.setNombrevalidacion(nombreValidacion);
			 elemVal.setProcesoValidacion(procVal);
			 elemVal.setObjetovalidado("Todos los documentos del Tramite id="+idTramite);
			 elemVal.setValidacionok(true);
			 
			 
			 // Lo añado al resultado
			 resultado.add(elemVal);
	     }
		 else
		 {
			 // Validacion Incorrecta
			 
			 for (Object[] objConsulta : resConsulta)
			 {
				// Creo el objeto resultado
				 ElementoValidado elemVal = new ElementoValidado();
				 
				 elemVal.setNombrevalidacion(nombreValidacion);
				 elemVal.setProcesoValidacion(procVal);
				 elemVal.setObjetovalidado("Documento Repetido: '"+(String)objConsulta[0]+"'");
				 elemVal.setValidacionok(false);
				 elemVal.setObservacionesvalidacion("Nombre Archivo Repetido: '"+(String)objConsulta[0]+"' Grupo:"+(Integer)objConsulta[1] +" Tipo:"+(Integer)objConsulta[2] );
				 
				 
				 // Lo añado al resultado
				 resultado.add(elemVal);
			 }
			
			 
			 
		 }
		 
		 
		
		log.debug("[validacion221] elementos validados="+resultado.size());
		return resultado;
	}
	
	/* (non-Javadoc)
	 * @see com.mitc.redes.editorfip.servicios.gestionfip.validaciones.ServicioGestionValidaciones#validacion222(int)
	 */
	public List<ElementoValidado> validacion222 (int idTramite, ProcesoValidacion procVal)
	{
		log.debug("[validacion222] validacion222 para idTramite="+idTramite);
		List<ElementoValidado> resultado = new ArrayList<ElementoValidado>();
		
		String nombreValidacion = (String)messages.get("validacion_222_nombre");
		
		String queryValidacion = "select  docshp.iddocumento, doc.nombre, docshp.hoja " + 
		"from planeamiento.documento doc, planeamiento.documentoshp docshp " + 
		"where doc.idtramite = " + idTramite + " and docshp.iddocumento = doc.iden and not (geometrytype(docshp.geom) = 'MULTIPOLYGON' or geometrytype(geom) = 'POLYGON')";

		List<Object[]> resConsulta = new ArrayList<Object[]>();

		  try {
			  resConsulta = (List<Object[]>) em.createNativeQuery(queryValidacion).getResultList();
		     
		     
		     
		 } catch (NoResultException e) {
		     log.error("[validacion222] No se ha encontrado validacion236." + e.getMessage());
		
		 } catch (Exception ex) {
		     log.error("[validacion222] Error en la busqueda del validacion236: " + ex.getMessage());
		
		 }
		 
		 
		 // Validacion Correcta
		 if (resConsulta.size()==0)
	     {
			// Creo el objeto resultado
			 ElementoValidado elemVal = new ElementoValidado();
			 
			 elemVal.setNombrevalidacion(nombreValidacion);
			 elemVal.setProcesoValidacion(procVal);
			 elemVal.setObjetovalidado("Todos las geometrias de los documentos del Tramite id="+idTramite);
			 elemVal.setValidacionok(true);
			 
			 
			 // Lo añado al resultado
			 resultado.add(elemVal);
	     }
		 else
		 {
			 // Validacion Incorrecta
			 
			 for (Object[] objConsulta : resConsulta)
			 {
				// Creo el objeto resultado
				 ElementoValidado elemVal = new ElementoValidado();
				 
				 elemVal.setNombrevalidacion(nombreValidacion);
				 elemVal.setProcesoValidacion(procVal);
				 elemVal.setObjetovalidado("Geometria de documento '" + (String)objConsulta[1] + "' del Tramite id="+idTramite);
				 elemVal.setValidacionok(false);
				 elemVal.setObservacionesvalidacion("Geometria de documento: '"+(String)objConsulta[1]+"' Id:"+(Integer)objConsulta[0]);
				 
				 
				 // Lo añado al resultado
				 resultado.add(elemVal);
			 }
		 }
		
		log.debug("[validacion222] elementos validados="+resultado.size());
		return resultado;
	}
	
	
	// ------------------------------
    // VALIDACIONES ENTIDADES
    // ------------------------------	
	
	/* (non-Javadoc)
	 * @see com.mitc.redes.editorfip.servicios.gestionfip.validaciones.ServicioGestionValidaciones#validacionesEntidades(int)
	 */
	public List<ElementoValidado> validacionesEntidades (int idTramite, ProcesoValidacion procVal)
	{
		log.debug("[validacionesEntidades] validacionesEntidades para idTramite="+ idTramite);
		List<ElementoValidado> resultado = new ArrayList<ElementoValidado>();

		//

		// Obtengo el tipo de validacion
		TipoValidacion tipoVal=null;

		String queryTipoValidacion = "SELECT tipo "
				+ " FROM TipoValidacion tipo " 
				+ " WHERE tipo.codigo='ENT'";

		try {
			tipoVal = (TipoValidacion) em.createQuery(queryTipoValidacion).getSingleResult();
		} catch (NoResultException e) {
			log.error("[validacionesEntidades] No se ha encontrado el TipoValidacion."+ e.getMessage());

		} catch (Exception ex) {
			log.error("[validacionesEntidades] Error en la busqueda del TipoValidacion: "+ ex.getMessage());

		}
		
		// Agrego las validaciones de los subapartados
		List<ElementoValidado> resulValidacion231 = validacion231(idTramite,procVal);
		resultado.addAll(resulValidacion231);
		
		List<ElementoValidado> resulValidacion232 = validacion232(idTramite,procVal);
		resultado.addAll(resulValidacion232);
		
		List<ElementoValidado> resulValidacion233 = validacion233(idTramite,procVal);
		resultado.addAll(resulValidacion233);
		
		List<ElementoValidado> resulValidacion234 = validacion234(idTramite,procVal);
		resultado.addAll(resulValidacion234);
		
		//List<ElementoValidado> resulValidacion235= validacion235(idTramite,procVal);
		//resultado.addAll(resulValidacion235);
		
		List<ElementoValidado> resulValidacion236 = validacion236(idTramite,procVal);
		resultado.addAll(resulValidacion236);
		
		List<ElementoValidado> resulValidacion237 = validacion237(idTramite,procVal);
		resultado.addAll(resulValidacion237);
		
		List<ElementoValidado> resulValidacion238 = validacion238(idTramite,procVal);
		resultado.addAll(resulValidacion238);
		
		List<ElementoValidado> resulValidacion239 = validacion239(idTramite,procVal);
		resultado.addAll(resulValidacion239);
		
		// Si hay tipo de validacion, recorro todos los resultados estableciendolo
		if (tipoVal!=null)
		{
			for (ElementoValidado elemVal: resultado)
			{
				elemVal.setTipoValidacion(tipoVal);
			}
		}
		

		log.debug("[validacionesEntidades] elementos validados="+ resultado.size());
		return resultado;
	}
	
	/* (non-Javadoc)
	 * @see com.mitc.redes.editorfip.servicios.gestionfip.validaciones.ServicioGestionValidaciones#validacion231(int)
	 */
	public List<ElementoValidado> validacion231 (int idTramite, ProcesoValidacion procVal)
	{
		log.debug("[validacion231] validacion231 para idTramite="+idTramite);
		List<ElementoValidado> resultado = new ArrayList<ElementoValidado>();
		
		//
		
		log.debug("[validacion231] elementos validados="+resultado.size());
		return resultado;
	}
	
	/* (non-Javadoc)
	 * @see com.mitc.redes.editorfip.servicios.gestionfip.validaciones.ServicioGestionValidaciones#validacion232(int)
	 */
	public List<ElementoValidado> validacion232 (int idTramite, ProcesoValidacion procVal)
	{
		log.debug("[validacion232] validacion232 para idTramite="+idTramite);
		List<ElementoValidado> resultado = new ArrayList<ElementoValidado>();
		
		//
		
		log.debug("[validacion232] elementos validados="+resultado.size());
		return resultado;
	}
	
	/* (non-Javadoc)
	 * @see com.mitc.redes.editorfip.servicios.gestionfip.validaciones.ServicioGestionValidaciones#validacion233(int)
	 */
	public List<ElementoValidado> validacion233 (int idTramite, ProcesoValidacion procVal)
	{
		log.debug("[validacion233] validacion233 para idTramite="+idTramite);
		List<ElementoValidado> resultado = new ArrayList<ElementoValidado>();
		
		//
		
		log.debug("[validacion233] elementos validados="+resultado.size());
		return resultado;
	}
	
	/* (non-Javadoc)
	 * @see com.mitc.redes.editorfip.servicios.gestionfip.validaciones.ServicioGestionValidaciones#validacion234(int)
	 */
	public List<ElementoValidado> validacion234 (int idTramite, ProcesoValidacion procVal)
	{
		log.debug("[validacion234] validacion234 para idTramite="+idTramite);
		List<ElementoValidado> resultado = new ArrayList<ElementoValidado>();
		
		String nombreValidacion = (String)messages.get("validacion_234_nombre");
		
		String queryValidacion = "select ent.iden, ent.nombre, ent.identidadbase " + 
								"from planeamiento.entidad ent " +
								"where ent.idtramite = " + idTramite + " and ent.identidadbase is not null and ent.identidadbase not in " +
								"(select e.iden from planeamiento.entidad e where e.idtramite in " +
								"(select pe.tramitebase_iden from gestionfip.planesencargados pe where pe.tramiteencargado_iden = " + idTramite + "))";
		
		List<Object[]> resConsulta = new ArrayList<Object[]>();

		  try {
			  resConsulta = (List<Object[]>) em.createNativeQuery(queryValidacion).getResultList();
		     
		     
		     
		 } catch (NoResultException e) {
		     log.error("[validacion234] No se ha encontrado validacion234." + e.getMessage());
		
		 } catch (Exception ex) {
		     log.error("[validacion234] Error en la busqueda del validacion234: " + ex.getMessage());
		
		 }
		 
		 
		 // Validacion Correcta
		 if (resConsulta.size()==0)
	     {
			// Creo el objeto resultado
			 ElementoValidado elemVal = new ElementoValidado();
			 
			 elemVal.setNombrevalidacion(nombreValidacion);
			 elemVal.setProcesoValidacion(procVal);
			 elemVal.setObjetovalidado("Todas las entidades base asociadas a entidades del tramite id="+idTramite);
			 elemVal.setValidacionok(true);
			 
			 
			 // Lo añado al resultado
			 resultado.add(elemVal);
	     }
		 else
		 {
			 // Validacion Incorrecta
			 
			 for (Object[] objConsulta : resConsulta)
			 {
				// Creo el objeto resultado
				 ElementoValidado elemVal = new ElementoValidado();
				 
				 elemVal.setNombrevalidacion(nombreValidacion);
				 elemVal.setProcesoValidacion(procVal);
				 elemVal.setObjetovalidado("Entidad que tiene asociada una entidad base no correcta: '" + (String)objConsulta[1] + "' .Tramite id="+idTramite);
				 elemVal.setValidacionok(false);
				 elemVal.setObservacionesvalidacion("Entidad que tiene asociada una entidad base que no pertenece al plan base: "+(String)objConsulta[1]+
						 " Id:"+(Integer)objConsulta[0] +
						 " Id Entidad Plan Base:"+(Integer)objConsulta[2]);
				 
				 
				 // Lo añado al resultado
				 resultado.add(elemVal);
			 }
		 }
		
		log.debug("[validacion234] elementos validados="+resultado.size());
		return resultado;
	}
	
	/* (non-Javadoc)
	 * @see com.mitc.redes.editorfip.servicios.gestionfip.validaciones.ServicioGestionValidaciones#validacion235(int)
	 */
	public List<ElementoValidado> validacion235 (int idTramite, ProcesoValidacion procVal)
	{
		log.debug("[validacion235] validacion235 para idTramite="+idTramite);
		List<ElementoValidado> resultado = new ArrayList<ElementoValidado>();
		
		String nombreValidacion = (String)messages.get("validacion_235_nombre");
		
		
		 String queryValidacion =  "select distinct ent.iden, ent.nombre, ST_IsValid(geom), ent.idtramite " + 
		 "from planeamiento.entidadpol epol, planeamiento.entidad ent " + 
		 "where epol.identidad=ent.iden and ent.idtramite=" +  idTramite + " and ST_IsValid(geom) is false ";

		 List<Object[]> resConsulta = new ArrayList<Object[]>();

		  try {
			  resConsulta = (List<Object[]>) em.createNativeQuery(queryValidacion).getResultList();
		     
		     
		     
		 } catch (NoResultException e) {
		     log.error("[validacion235] No se ha encontrado validacion235." + e.getMessage());
		
		 } catch (Exception ex) {
		     log.error("[validacion235] Error en la busqueda del validacion235: " + ex.getMessage());
		
		 }
		 
		 
		 // Validacion Correcta
		 if (resConsulta.size()==0)
	     {
			// Creo el objeto resultado
			 ElementoValidado elemVal = new ElementoValidado();
			 
			 elemVal.setNombrevalidacion(nombreValidacion);
			 elemVal.setProcesoValidacion(procVal);
			 elemVal.setObjetovalidado("Todos las geometrías de las entidades del Tramite id="+idTramite);
			 elemVal.setValidacionok(true);
			 
			 
			 // Lo añado al resultado
			 resultado.add(elemVal);
	     }
		 else
		 {
			 // Validacion Incorrecta
			 
			 for (Object[] objConsulta : resConsulta)
			 {
				// Creo el objeto resultado
				 ElementoValidado elemVal = new ElementoValidado();
				 
				 elemVal.setNombrevalidacion(nombreValidacion);
				 elemVal.setProcesoValidacion(procVal);
				 elemVal.setObjetovalidado("Geometría incorrecta de la entidad '" + (String)objConsulta[1] + "' del Tramite id="+idTramite);
				 elemVal.setValidacionok(false);
				 elemVal.setObservacionesvalidacion("Geometría incorrecta de la entidad: '"+(String)objConsulta[1]+"' Id:"+(Integer)objConsulta[0] +" Tipo:"+(Integer)objConsulta[2] );
				 
				 
				 // Lo añado al resultado
				 resultado.add(elemVal);
			 }
			
			 
			 
		 }
		 
		 
		
		log.debug("[validacion235] elementos validados="+resultado.size());
		return resultado;
	}
	
	/* (non-Javadoc)
	 * @see com.mitc.redes.editorfip.servicios.gestionfip.validaciones.ServicioGestionValidaciones#validacion236(int)
	 */
	public List<ElementoValidado> validacion236 (int idTramite, ProcesoValidacion procVal)
	{
		log.debug("[validacion236] validacion236 para idTramite="+idTramite);
		List<ElementoValidado> resultado = new ArrayList<ElementoValidado>();
		
		String nombreValidacion = (String)messages.get("validacion_236_nombre");
		
		String queryValidacion = "select ent.iden,ent.nombre from planeamiento.entidad ent  where ent.iden in " + 
		"(select docent.identidad from planeamiento.documentoentidad docent where docent.identidad in " + 
		"(select ent2.iden from planeamiento.entidad ent2 where ent2.idtramite=" + idTramite + ") " + 
		" group by  docent.identidad,docent.iddocumento having (count(docent.identidad)>1 and count(docent.iddocumento)>1 ))";

		List<Object[]> resConsulta = new ArrayList<Object[]>();

		  try {
			  resConsulta = (List<Object[]>) em.createNativeQuery(queryValidacion).getResultList();
		     
		     
		     
		 } catch (NoResultException e) {
		     log.error("[validacion236] No se ha encontrado validacion236." + e.getMessage());
		
		 } catch (Exception ex) {
		     log.error("[validacion236] Error en la busqueda del validacion236: " + ex.getMessage());
		
		 }
		 
		 
		 // Validacion Correcta
		 if (resConsulta.size()==0)
	     {
			// Creo el objeto resultado
			 ElementoValidado elemVal = new ElementoValidado();
			 
			 elemVal.setNombrevalidacion(nombreValidacion);
			 elemVal.setProcesoValidacion(procVal);
			 elemVal.setObjetovalidado("Todas las entidades del Tramite id="+idTramite);
			 elemVal.setValidacionok(true);
			 
			 
			 // Lo añado al resultado
			 resultado.add(elemVal);
	     }
		 else
		 {
			 // Validacion Incorrecta
			 
			 for (Object[] objConsulta : resConsulta)
			 {
				// Creo el objeto resultado
				 ElementoValidado elemVal = new ElementoValidado();
				 
				 elemVal.setNombrevalidacion(nombreValidacion);
				 elemVal.setProcesoValidacion(procVal);
				 elemVal.setObjetovalidado("Entidad '" + (String)objConsulta[1] + "' del Tramite id="+idTramite);
				 elemVal.setValidacionok(false);
				 elemVal.setObservacionesvalidacion("Nombre Entidad: '"+(String)objConsulta[1]+"' Id:"+(Integer)objConsulta[0]);
				 
				 
				 // Lo añado al resultado
				 resultado.add(elemVal);
			 }
			
			 
			 
		 }
		
		log.debug("[validacion236] elementos validados="+resultado.size());
		return resultado;
	}
	
	/* (non-Javadoc)
	 * @see com.mitc.redes.editorfip.servicios.gestionfip.validaciones.ServicioGestionValidaciones#validacion237(int)
	 */
	public List<ElementoValidado> validacion237 (int idTramite, ProcesoValidacion procVal)
	{
		log.debug("[validacion237] validacion237 para idTramite="+idTramite);
		List<ElementoValidado> resultado = new ArrayList<ElementoValidado>();
		
		String nombreValidacion = (String)messages.get("validacion_237_nombre");
		
		String queryValidacion = "select ent.iden, ent.nombre from planeamiento.determinacion det, planeamiento.entidaddeterminacion cu, planeamiento.entidad ent " + 
								"where det.idtramite = " + idTramite + " and cu.iddeterminacion = det.iden and cu.identidad = ent.iden and (det.idcaracter =20) " + 
								"group by ent.iden, ent.nombre " +
								"having count(ent.iden)>1";
		
		List<Object[]> resConsulta = new ArrayList<Object[]>();

		  try {
			  resConsulta = (List<Object[]>) em.createNativeQuery(queryValidacion).getResultList();
		     
		     
		     
		 } catch (NoResultException e) {
		     log.error("[validacion237] No se ha encontrado validacion237." + e.getMessage());
		
		 } catch (Exception ex) {
		     log.error("[validacion237] Error en la busqueda del validacion237: " + ex.getMessage());
		
		 }
		 
		 
		 // Validacion Correcta
		 if (resConsulta.size()==0)
	     {
			// Creo el objeto resultado
			 ElementoValidado elemVal = new ElementoValidado();
			 
			 elemVal.setNombrevalidacion(nombreValidacion);
			 elemVal.setProcesoValidacion(procVal);
			 elemVal.setObjetovalidado("Todas las entidades del tramite id="+idTramite);
			 elemVal.setValidacionok(true);
			 
			 
			 // Lo añado al resultado
			 resultado.add(elemVal);
	     }
		 else
		 {
			 // Validacion Incorrecta
			 
			 for (Object[] objConsulta : resConsulta)
			 {
				// Creo el objeto resultado
				 ElementoValidado elemVal = new ElementoValidado();
				 
				 elemVal.setNombrevalidacion(nombreValidacion);
				 elemVal.setProcesoValidacion(procVal);
				 elemVal.setObjetovalidado("Entidad '" + (String)objConsulta[1] + "' del Tramite id="+idTramite);
				 elemVal.setValidacionok(false);
				 elemVal.setObservacionesvalidacion("Entidad sin una única aplicación a una determinacion de caracter grupo: '"+(String)objConsulta[1]+"' Id:"+(Integer)objConsulta[0]);
				 
				 
				 // Lo añado al resultado
				 resultado.add(elemVal);
			 }
		 }
		
		log.debug("[validacion237] elementos validados="+resultado.size());
		return resultado;
	}
	
	/* (non-Javadoc)
	 * @see com.mitc.redes.editorfip.servicios.gestionfip.validaciones.ServicioGestionValidaciones#validacion238(int)
	 */
	public List<ElementoValidado> validacion238 (int idTramite, ProcesoValidacion procVal)
	{
		log.debug("[validacion238] validacion238 para idTramite="+idTramite);
		List<ElementoValidado> resultado = new ArrayList<ElementoValidado>();
		
		//
		
		log.debug("[validacion238] elementos validados="+resultado.size());
		return resultado;
	}
	
	/* (non-Javadoc)
	 * @see com.mitc.redes.editorfip.servicios.gestionfip.validaciones.ServicioGestionValidaciones#validacion239(int)
	 */
	public List<ElementoValidado> validacion239 (int idTramite, ProcesoValidacion procVal)
	{
		log.debug("[validacion239] validacion239 para idTramite="+idTramite);
		List<ElementoValidado> resultado = new ArrayList<ElementoValidado>();
		
		//
		
		log.debug("[validacion239] elementos validados="+resultado.size());
		return resultado;
	}
	
	
	
	// ------------------------------
    // VALIDACIONES DETERMINACIONES
    // ------------------------------	
	
	/* (non-Javadoc)
	 * @see com.mitc.redes.editorfip.servicios.gestionfip.validaciones.ServicioGestionValidaciones#validacionesDeterminaciones(int)
	 */
	public List<ElementoValidado> validacionesDeterminaciones (int idTramite, ProcesoValidacion procVal)
	{
		
		log.debug("[validacionesDeterminaciones] validacionesDeterminaciones para idTramite="+ idTramite);
		List<ElementoValidado> resultado = new ArrayList<ElementoValidado>();

		//

		// Obtengo el tipo de validacion
		TipoValidacion tipoVal=null;

		String queryTipoValidacion = "SELECT tipo "
				+ " FROM TipoValidacion tipo " 
				+ " WHERE tipo.codigo='DET'";

		try {
			tipoVal = (TipoValidacion) em.createQuery(queryTipoValidacion).getSingleResult();
		} catch (NoResultException e) {
			log.error("[validacionesDeterminaciones] No se ha encontrado el TipoValidacion."+ e.getMessage());

		} catch (Exception ex) {
			log.error("[validacionesDeterminaciones] Error en la busqueda del TipoValidacion: "+ ex.getMessage());

		}
		
		// Agrego las validaciones de los subapartados
		List<ElementoValidado> resulValidacion241 = validacion241(idTramite,procVal);
		resultado.addAll(resulValidacion241);
		
		List<ElementoValidado> resulValidacion242 = validacion242(idTramite,procVal);
		resultado.addAll(resulValidacion242);
		
		List<ElementoValidado> resulValidacion243 = validacion243(idTramite,procVal);
		resultado.addAll(resulValidacion243);
		
		List<ElementoValidado> resulValidacion244 = validacion244(idTramite,procVal);
		resultado.addAll(resulValidacion244);
		
		List<ElementoValidado> resulValidacion245= validacion245(idTramite,procVal);
		resultado.addAll(resulValidacion245);
		
		List<ElementoValidado> resulValidacion246 = validacion246(idTramite,procVal);
		resultado.addAll(resulValidacion246);
		
		List<ElementoValidado> resulValidacion247 = validacion247(idTramite,procVal);
		resultado.addAll(resulValidacion247);
		
		List<ElementoValidado> resulValidacion248 = validacion248(idTramite,procVal);
		resultado.addAll(resulValidacion248);
		
		List<ElementoValidado> resulValidacion249 = validacion249(idTramite,procVal);
		resultado.addAll(resulValidacion249);
		
		List<ElementoValidado> resulValidacion2410 = validacion2410(idTramite,procVal);
		resultado.addAll(resulValidacion2410);
		
		List<ElementoValidado> resulValidacion2411 = validacion2411(idTramite,procVal);
		resultado.addAll(resulValidacion2411);
		
		// Si hay tipo de validacion, recorro todos los resultados estableciendolo
		if (tipoVal!=null)
		{
			for (ElementoValidado elemVal: resultado)
			{
				elemVal.setTipoValidacion(tipoVal);
			}
		}
		

		log.debug("[validacionesDeterminaciones] elementos validados="+ resultado.size());
		return resultado;
		
	}
	
	/* (non-Javadoc)
	 * @see com.mitc.redes.editorfip.servicios.gestionfip.validaciones.ServicioGestionValidaciones#validacion241(int)
	 */
	public List<ElementoValidado> validacion241 (int idTramite, ProcesoValidacion procVal)
	{
		log.debug("[validacion241] validacion241 para idTramite="+idTramite);
		List<ElementoValidado> resultado = new ArrayList<ElementoValidado>();
		
		//
		
		log.debug("[validacion241] elementos validados="+resultado.size());
		return resultado;
	}
	
	/* (non-Javadoc)
	 * @see com.mitc.redes.editorfip.servicios.gestionfip.validaciones.ServicioGestionValidaciones#validacion242(int)
	 */
	public List<ElementoValidado> validacion242 (int idTramite, ProcesoValidacion procVal)
	{
		log.debug("[validacion242] validacion242 para idTramite="+idTramite);
		List<ElementoValidado> resultado = new ArrayList<ElementoValidado>();
		
		//
		
		log.debug("[validacion242] elementos validados="+resultado.size());
		return resultado;
	}
	
	/* (non-Javadoc)
	 * @see com.mitc.redes.editorfip.servicios.gestionfip.validaciones.ServicioGestionValidaciones#validacion243(int)
	 */
	public List<ElementoValidado> validacion243 (int idTramite, ProcesoValidacion procVal)
	{
		log.debug("[validacion243] validacion243 para idTramite="+idTramite);
		List<ElementoValidado> resultado = new ArrayList<ElementoValidado>();
		
		//
		
		log.debug("[validacion243] elementos validados="+resultado.size());
		return resultado;
	}
	
	/* (non-Javadoc)
	 * @see com.mitc.redes.editorfip.servicios.gestionfip.validaciones.ServicioGestionValidaciones#validacion244(int)
	 */
	public List<ElementoValidado> validacion244 (int idTramite, ProcesoValidacion procVal)
	{
		log.debug("[validacion244] validacion244 para idTramite="+idTramite);
		List<ElementoValidado> resultado = new ArrayList<ElementoValidado>();
		
		//
		
		log.debug("[validacion244] elementos validados="+resultado.size());
		return resultado;
	}
	
	/* (non-Javadoc)
	 * @see com.mitc.redes.editorfip.servicios.gestionfip.validaciones.ServicioGestionValidaciones#validacion245(int)
	 */
	public List<ElementoValidado> validacion245 (int idTramite, ProcesoValidacion procVal)
	{
		log.debug("[validacion245] validacion245 para idTramite="+idTramite);
		List<ElementoValidado> resultado = new ArrayList<ElementoValidado>();
		
		String nombreValidacion = (String)messages.get("validacion_245_nombre");
		
		String queryValidacion = "select det.iden, det.nombre, det.idcaracter from planeamiento.determinacion det, planeamiento.opciondeterminacion detvr " + 
								"where det.idtramite = " + idTramite + " and detvr.iddeterminacionvalorref = det.iden and det.idcaracter !=12";
		
		List<Object[]> resConsulta = new ArrayList<Object[]>();

		  try {
			  resConsulta = (List<Object[]>) em.createNativeQuery(queryValidacion).getResultList();
		     
		     
		     
		 } catch (NoResultException e) {
		     log.error("[validacion245] No se ha encontrado validacion245." + e.getMessage());
		
		 } catch (Exception ex) {
		     log.error("[validacion245] Error en la busqueda del validacion245: " + ex.getMessage());
		
		 }
		 
		 
		 // Validacion Correcta
		 if (resConsulta.size()==0)
	     {
			// Creo el objeto resultado
			 ElementoValidado elemVal = new ElementoValidado();
			 
			 elemVal.setNombrevalidacion(nombreValidacion);
			 elemVal.setProcesoValidacion(procVal);
			 elemVal.setObjetovalidado("Todas las determinaciones que son valor de referencia del tramite id="+idTramite);
			 elemVal.setValidacionok(true);
			 
			 
			 // Lo añado al resultado
			 resultado.add(elemVal);
	     }
		 else
		 {
			 // Validacion Incorrecta
			 
			 for (Object[] objConsulta : resConsulta)
			 {
				// Creo el objeto resultado
				 ElementoValidado elemVal = new ElementoValidado();
				 
				 elemVal.setNombrevalidacion(nombreValidacion);
				 elemVal.setProcesoValidacion(procVal);
				 elemVal.setObjetovalidado("Determinacion de valor de referencia '" + (String)objConsulta[1] + "' del Tramite id="+idTramite);
				 elemVal.setValidacionok(false);
				 elemVal.setObservacionesvalidacion("Determinacion que es valor de referencia sin caracter valor de referencia: '"+(String)objConsulta[1]+"' Id:"+(Integer)objConsulta[0] + 
						 		" Id Valor Referencia:"+(Integer)objConsulta[2]);
				 
				 
				 // Lo añado al resultado
				 resultado.add(elemVal);
			 }
		 }
		
		log.debug("[validacion245] elementos validados="+resultado.size());
		return resultado;
	}
	
	/* (non-Javadoc)
	 * @see com.mitc.redes.editorfip.servicios.gestionfip.validaciones.ServicioGestionValidaciones#validacion246(int)
	 */
	public List<ElementoValidado> validacion246 (int idTramite, ProcesoValidacion procVal)
	{
		log.debug("[validacion246] validacion246 para idTramite="+idTramite);
		List<ElementoValidado> resultado = new ArrayList<ElementoValidado>();
		
		String nombreValidacion = (String)messages.get("validacion_246_nombre");
		
		String queryValidacion = "select det.iden, det.nombre, det.idcaracter, cu.identidad, ent.nombre from planeamiento.determinacion det, planeamiento.entidaddeterminacion cu, planeamiento.entidad ent " + 
							"where det.idtramite = " + idTramite + " and cu.iddeterminacion = det.iden and cu.identidad = ent.iden " +
							"and (det.idcaracter =1 or det.idcaracter =12 or det.idcaracter =19 or det.idcaracter =18 or det.idcaracter =11 or det.idcaracter =13)";
		
		List<Object[]> resConsulta = new ArrayList<Object[]>();

		  try {
			  resConsulta = (List<Object[]>) em.createNativeQuery(queryValidacion).getResultList();
		     
		     
		     
		 } catch (NoResultException e) {
		     log.error("[validacion246] No se ha encontrado validacion246." + e.getMessage());
		
		 } catch (Exception ex) {
		     log.error("[validacion246] Error en la busqueda del validacion246: " + ex.getMessage());
		
		 }
		 
		 
		 // Validacion Correcta
		 if (resConsulta.size()==0)
	     {
			// Creo el objeto resultado
			 ElementoValidado elemVal = new ElementoValidado();
			 
			 elemVal.setNombrevalidacion(nombreValidacion);
			 elemVal.setProcesoValidacion(procVal);
			 elemVal.setObjetovalidado("Todas las  determinaciones de tipo Enunciado, Valor de Referencia, Adscripcion, Unidad, Enunciado y Regulacion del tramite id="+idTramite);
			 elemVal.setValidacionok(true);
			 
			 
			 // Lo añado al resultado
			 resultado.add(elemVal);
	     }
		 else
		 {
			 // Validacion Incorrecta
			 
			 for (Object[] objConsulta : resConsulta)
			 {
				// Creo el objeto resultado
				 ElementoValidado elemVal = new ElementoValidado();
				 
				 elemVal.setNombrevalidacion(nombreValidacion);
				 elemVal.setProcesoValidacion(procVal);
				 elemVal.setObjetovalidado("Determinacion '" + (String)objConsulta[1] + "' del Tramite id="+idTramite);
				 elemVal.setValidacionok(false);
				 elemVal.setObservacionesvalidacion("Determinacion: '"+(String)objConsulta[1]+"' Id:"+(Integer)objConsulta[0] + 
						 		" Id Caracter:"+(Integer)objConsulta[2] + 
						 		" Id CU:"+(Integer)objConsulta[3] + 
						 		" Nombre Entidad:"+(String)objConsulta[4]);
				 
				 
				 // Lo añado al resultado
				 resultado.add(elemVal);
			 }
		 }
		
		log.debug("[validacion246] elementos validados="+resultado.size());
		return resultado;
	}
	
	/* (non-Javadoc)
	 * @see com.mitc.redes.editorfip.servicios.gestionfip.validaciones.ServicioGestionValidaciones#validacion247(int)
	 */
	public List<ElementoValidado> validacion247 (int idTramite, ProcesoValidacion procVal)
	{
		log.debug("[validacion247] validacion247 para idTramite="+idTramite);
		List<ElementoValidado> resultado = new ArrayList<ElementoValidado>();
		
		String nombreValidacion = (String)messages.get("validacion_247_nombre");
		
		String queryValidacion = "select d.iden, d.nombre, d.idcaracter from planeamiento.determinacion d where " +
		"d.idcaracter!=7 and d.idcaracter!=8 and d.idcaracter!=2 and d.idcaracter!=3 and d.idcaracter!=17 and d.idcaracter!=20 " + 
		"and d.iden in ( " +
		" select distinct det.iden from planeamiento.determinacion det, planeamiento.opciondeterminacion detvr " +
		"where idtramite = " + idTramite + " and detvr.iddeterminacion = det.iden)";
		
		List<Object[]> resConsulta = new ArrayList<Object[]>();

		  try {
			  resConsulta = (List<Object[]>) em.createNativeQuery(queryValidacion).getResultList();
		     
		     
		     
		 } catch (NoResultException e) {
		     log.error("[validacion247] No se ha encontrado validacion247." + e.getMessage());
		
		 } catch (Exception ex) {
		     log.error("[validacion247] Error en la busqueda del validacion247: " + ex.getMessage());
		
		 }
		 
		 
		 // Validacion Correcta
		 if (resConsulta.size()==0)
	     {
			// Creo el objeto resultado
			 ElementoValidado elemVal = new ElementoValidado();
			 
			 elemVal.setNombrevalidacion(nombreValidacion);
			 elemVal.setProcesoValidacion(procVal);
			 elemVal.setObjetovalidado("Todas las determinaciones que tienen asignados valores de referencia del tramite id="+idTramite);
			 elemVal.setValidacionok(true);
			 
			 
			 // Lo añado al resultado
			 resultado.add(elemVal);
	     }
		 else
		 {
			 // Validacion Incorrecta
			 
			 for (Object[] objConsulta : resConsulta)
			 {
				// Creo el objeto resultado
				 ElementoValidado elemVal = new ElementoValidado();
				 
				 elemVal.setNombrevalidacion(nombreValidacion);
				 elemVal.setProcesoValidacion(procVal);
				 elemVal.setObjetovalidado("Determinacion con valor de referencia incorrecta '" + (String)objConsulta[1] + "' del Tramite id="+idTramite);
				 elemVal.setValidacionok(false);
				 elemVal.setObservacionesvalidacion("Determinacion con valor de referencia incorrecta: '"+(String)objConsulta[1]+"' Id:"+(Integer)objConsulta[0]);
				 
				 
				 // Lo añado al resultado
				 resultado.add(elemVal);
			 }
		 }
		
		log.debug("[validacion247] elementos validados="+resultado.size());
		return resultado;
	}
	
	/* (non-Javadoc)
	 * @see com.mitc.redes.editorfip.servicios.gestionfip.validaciones.ServicioGestionValidaciones#validacion248(int)
	 */
	public List<ElementoValidado> validacion248 (int idTramite, ProcesoValidacion procVal)
	{
		log.debug("[validacion248] validacion248 para idTramite="+idTramite);
		List<ElementoValidado> resultado = new ArrayList<ElementoValidado>();
		
		String nombreValidacion = (String)messages.get("validacion_248_nombre");
		
		String queryValidacion = "select det.iden, det.nombre from planeamiento.determinacion det " + 
		"where idtramite = " + idTramite + " and (det.idcaracter=2 or det.idcaracter=3 or det.idcaracter=4 or " +
		"det.idcaracter=5 or det.idcaracter=6 or det.idcaracter=7 or det.idcaracter=8 or det.idcaracter=9 or det.idcaracter=10 " +
		" or det.idcaracter=14 or det.idcaracter=15) and det.iden not in " +
		"(select det.iden from planeamiento.determinacion det, planeamiento.determinaciongrupoentidad detge " +
		"where idtramite = " + idTramite + " and detge.iddeterminacion = det.iden)";
		
		List<Object[]> resConsulta = new ArrayList<Object[]>();

		  try {
			  resConsulta = (List<Object[]>) em.createNativeQuery(queryValidacion).getResultList();
		     
		     
		     
		 } catch (NoResultException e) {
		     log.error("[validacion248] No se ha encontrado validacion248." + e.getMessage());
		
		 } catch (Exception ex) {
		     log.error("[validacion248] Error en la busqueda del validacion248: " + ex.getMessage());
		
		 }
		 
		 
		 // Validacion Correcta
		 if (resConsulta.size()==0)
	     {
			// Creo el objeto resultado
			 ElementoValidado elemVal = new ElementoValidado();
			 
			 elemVal.setNombrevalidacion(nombreValidacion);
			 elemVal.setProcesoValidacion(procVal);
			 elemVal.setObjetovalidado("Todos las determinaciones de tipo NGL, NGG, Acto, Uso, GA, GU, NA, NU, RA, RU, Adscripciones del tramite id="+idTramite);
			 elemVal.setValidacionok(true);
			 
			 
			 // Lo añado al resultado
			 resultado.add(elemVal);
	     }
		 else
		 {
			 // Validacion Incorrecta
			 
			 for (Object[] objConsulta : resConsulta)
			 {
				// Creo el objeto resultado
				 ElementoValidado elemVal = new ElementoValidado();
				 
				 elemVal.setNombrevalidacion(nombreValidacion);
				 elemVal.setProcesoValidacion(procVal);
				 elemVal.setObjetovalidado("Determinacion sin grupo de aplicación '" + (String)objConsulta[1] + "' del Tramite id="+idTramite);
				 elemVal.setValidacionok(false);
				 elemVal.setObservacionesvalidacion("Determinacion sin grupo de aplicación: '"+(String)objConsulta[1]+"' Id:"+(Integer)objConsulta[0]);
				 
				 
				 // Lo añado al resultado
				 resultado.add(elemVal);
			 }
		 }
		
		log.debug("[validacion248] elementos validados="+resultado.size());
		return resultado;
	}
	
	/* (non-Javadoc)
	 * @see com.mitc.redes.editorfip.servicios.gestionfip.validaciones.ServicioGestionValidaciones#validacion249(int)
	 */
	public List<ElementoValidado> validacion249 (int idTramite, ProcesoValidacion procVal)
	{
		log.debug("[validacion249] validacion249 para idTramite="+idTramite);
		List<ElementoValidado> resultado = new ArrayList<ElementoValidado>();
		
		//
		
		log.debug("[validacion249] elementos validados="+resultado.size());
		return resultado;
	}
	
	/* (non-Javadoc)
	 * @see com.mitc.redes.editorfip.servicios.gestionfip.validaciones.ServicioGestionValidaciones#validacion2410(int)
	 */
	public List<ElementoValidado> validacion2410 (int idTramite, ProcesoValidacion procVal)
	{
		log.debug("[validacion2410] validacion2410 para idTramite="+idTramite);
		List<ElementoValidado> resultado = new ArrayList<ElementoValidado>();
		
		String nombreValidacion = (String)messages.get("validacion_2410_nombre");
		
		String queryValidacion = "select det.iden,det.nombre from planeamiento.determinacion det  where det.iden in " + 
		"(select docdet.iddeterminacion from planeamiento.documentodeterminacion docdet where docdet.iddeterminacion in " +
		"(select det2.iden from planeamiento.determinacion det2 where det2.idtramite=" + idTramite + ") " + 
		"group by docdet.iddeterminacion,docdet.iddocumento having (count(docdet.iddeterminacion)>1 and count(docdet.iddocumento)>1 ))";

		List<Object[]> resConsulta = new ArrayList<Object[]>();

		  try {
			  resConsulta = (List<Object[]>) em.createNativeQuery(queryValidacion).getResultList();
		     
		     
		     
		 } catch (NoResultException e) {
		     log.error("[validacion2410] No se ha encontrado validacion2410." + e.getMessage());
		
		 } catch (Exception ex) {
		     log.error("[validacion2410] Error en la busqueda del validacion2410: " + ex.getMessage());
		
		 }
		 
		 
		 // Validacion Correcta
		 if (resConsulta.size()==0)
	     {
			// Creo el objeto resultado
			 ElementoValidado elemVal = new ElementoValidado();
			 
			 elemVal.setNombrevalidacion(nombreValidacion);
			 elemVal.setProcesoValidacion(procVal);
			 elemVal.setObjetovalidado("Todos las asignaciones de documentos a determinaciones del Tramite id="+idTramite);
			 elemVal.setValidacionok(true);
			 
			 
			 // Lo añado al resultado
			 resultado.add(elemVal);
	     }
		 else
		 {
			 // Validacion Incorrecta
			 
			 for (Object[] objConsulta : resConsulta)
			 {
				// Creo el objeto resultado
				 ElementoValidado elemVal = new ElementoValidado();
				 
				 elemVal.setNombrevalidacion(nombreValidacion);
				 elemVal.setProcesoValidacion(procVal);
				 elemVal.setObjetovalidado("Determinacion '" + (String)objConsulta[1] + "' del Tramite id="+idTramite);
				 elemVal.setValidacionok(false);
				 elemVal.setObservacionesvalidacion("Nombre Determinacion: '"+(String)objConsulta[1]+"' Id:"+(Integer)objConsulta[0]);
				 
				 
				 // Lo añado al resultado
				 resultado.add(elemVal);
			 }
		 }
		
		log.debug("[validacion2410] elementos validados="+resultado.size());
		return resultado;
	}
	
	/* (non-Javadoc)
	 * @see com.mitc.redes.editorfip.servicios.gestionfip.validaciones.ServicioGestionValidaciones#validacion2411(int)
	 */
	public List<ElementoValidado> validacion2411 (int idTramite, ProcesoValidacion procVal)
	{
		log.debug("[validacion2411] validacion2411 para idTramite="+idTramite);
		List<ElementoValidado> resultado = new ArrayList<ElementoValidado>();
		
		String nombreValidacion = (String)messages.get("validacion_2411_nombre");
		
		String queryValidacion = "select det.nombre,det.idcaracter from planeamiento.determinacion det where det.idtramite = " + idTramite +
		" group by det.idcaracter, det.nombre, det.idpadre " +
		"having (count(det.idcaracter)>1 and count(det.nombre)>1 and count(det.idpadre)>1)";
		
		List<Object[]> resConsulta = new ArrayList<Object[]>();

		  try {
			  resConsulta = (List<Object[]>) em.createNativeQuery(queryValidacion).getResultList();
		     
		     
		     
		 } catch (NoResultException e) {
		     log.error("[validacion2411] No se ha encontrado validacion2411." + e.getMessage());
		
		 } catch (Exception ex) {
		     log.error("[validacion2411] Error en la busqueda del validacion2411: " + ex.getMessage());
		
		 }
		 
		 
		 // Validacion Correcta
		 if (resConsulta.size()==0)
	     {
			// Creo el objeto resultado
			 ElementoValidado elemVal = new ElementoValidado();
			 
			 elemVal.setNombrevalidacion(nombreValidacion);
			 elemVal.setProcesoValidacion(procVal);
			 elemVal.setObjetovalidado("Todas las determinaciones del tramite id="+idTramite);
			 elemVal.setValidacionok(true);
			 
			 
			 // Lo añado al resultado
			 resultado.add(elemVal);
	     }
		 else
		 {
			 // Validacion Incorrecta
			 
			 for (Object[] objConsulta : resConsulta)
			 {
				// Creo el objeto resultado
				 ElementoValidado elemVal = new ElementoValidado();
				 
				 elemVal.setNombrevalidacion(nombreValidacion);
				 elemVal.setProcesoValidacion(procVal);
				 elemVal.setObjetovalidado("Determinacion con nombre, caracter y unidad no unicos: '" + (String)objConsulta[0] + "' del Tramite id="+idTramite);
				 elemVal.setValidacionok(false);
				 elemVal.setObservacionesvalidacion("Determinacion con nombre, caracter y unidad no unicos: '"+(String)objConsulta[0]+"' Id Caracter:"+(Integer)objConsulta[1]);
				 
				 
				 // Lo añado al resultado
				 resultado.add(elemVal);
			 }
		 }
		
		log.debug("[validacion2411] elementos validados="+resultado.size());
		return resultado;
	}
	
	
	// ------------------------------
    // VALIDACIONES CONDICIONES URBANISTICAS
    // ------------------------------	
	
	/* (non-Javadoc)
	 * @see com.mitc.redes.editorfip.servicios.gestionfip.validaciones.ServicioGestionValidaciones#validacionesCondicionesUrbanisticas(int)
	 */
	public List<ElementoValidado> validacionesCondicionesUrbanisticas (int idTramite, ProcesoValidacion procVal)
	{
		log.debug("[validacionesCondicionesUrbanisticas] validacionesCondicionesUrbanisticas para idTramite="+ idTramite);
		List<ElementoValidado> resultado = new ArrayList<ElementoValidado>();

		//

		// Obtengo el tipo de validacion
		TipoValidacion tipoVal=null;

		String queryTipoValidacion = "SELECT tipo "
				+ " FROM TipoValidacion tipo " 
				+ " WHERE tipo.codigo='CUR'";

		try {
			tipoVal = (TipoValidacion) em.createQuery(queryTipoValidacion).getSingleResult();
		} catch (NoResultException e) {
			log.error("[validacionesCondicionesUrbanisticas] No se ha encontrado el TipoValidacion."+ e.getMessage());

		} catch (Exception ex) {
			log.error("[validacionesCondicionesUrbanisticas] Error en la busqueda del TipoValidacion: "+ ex.getMessage());

		}
		
		// Agrego las validaciones de los subapartados
		List<ElementoValidado> resulValidacion251 = validacion251(idTramite,procVal);
		resultado.addAll(resulValidacion251);
		
		List<ElementoValidado> resulValidacion252 = validacion252(idTramite,procVal);
		resultado.addAll(resulValidacion252);
		
		List<ElementoValidado> resulValidacion253 = validacion253(idTramite,procVal);
		resultado.addAll(resulValidacion253);
		
		List<ElementoValidado> resulValidacion254 = validacion254(idTramite,procVal);
		resultado.addAll(resulValidacion254);
		
		List<ElementoValidado> resulValidacion255= validacion255(idTramite,procVal);
		resultado.addAll(resulValidacion255);
		
		List<ElementoValidado> resulValidacion256 = validacion256(idTramite,procVal);
		resultado.addAll(resulValidacion256);
		
		List<ElementoValidado> resulValidacion257 = validacion257(idTramite,procVal);
		resultado.addAll(resulValidacion257);
		
		List<ElementoValidado> resulValidacion258 = validacion258(idTramite,procVal);
		resultado.addAll(resulValidacion258);
		
		List<ElementoValidado> resulValidacion259 = validacion259(idTramite,procVal);
		resultado.addAll(resulValidacion259);
		
		List<ElementoValidado> resulValidacion2510 = validacion2510(idTramite,procVal);
		resultado.addAll(resulValidacion2510);
				
		// Si hay tipo de validacion, recorro todos los resultados estableciendolo
		if (tipoVal!=null)
		{
			for (ElementoValidado elemVal: resultado)
			{
				elemVal.setTipoValidacion(tipoVal);
			}
		}
		

		log.debug("[validacionesCondicionesUrbanisticas] elementos validados="+ resultado.size());
		return resultado;
	}
	
	/* (non-Javadoc)
	 * @see com.mitc.redes.editorfip.servicios.gestionfip.validaciones.ServicioGestionValidaciones#validacion251(int)
	 */
	public List<ElementoValidado> validacion251 (int idTramite, ProcesoValidacion procVal)
	{
		log.debug("[validacion251] validacion251 para idTramite="+idTramite);
		List<ElementoValidado> resultado = new ArrayList<ElementoValidado>();
		
		//
		
		log.debug("[validacion251] elementos validados="+resultado.size());
		return resultado;
	}
	
	/* (non-Javadoc)
	 * @see com.mitc.redes.editorfip.servicios.gestionfip.validaciones.ServicioGestionValidaciones#validacion252(int)
	 */
	public List<ElementoValidado> validacion252 (int idTramite, ProcesoValidacion procVal)
	{
		log.debug("[validacion252] validacion252 para idTramite="+idTramite);
		List<ElementoValidado> resultado = new ArrayList<ElementoValidado>();
		
		//
		
		log.debug("[validacion252] elementos validados="+resultado.size());
		return resultado;
	}
	
	/* (non-Javadoc)
	 * @see com.mitc.redes.editorfip.servicios.gestionfip.validaciones.ServicioGestionValidaciones#validacion253(int)
	 */
	public List<ElementoValidado> validacion253 (int idTramite, ProcesoValidacion procVal)
	{
		log.debug("[validacion253] validacion253 para idTramite="+idTramite);
		List<ElementoValidado> resultado = new ArrayList<ElementoValidado>();
		
		//
		
		log.debug("[validacion253] elementos validados="+resultado.size());
		return resultado;
	}
	
	/* (non-Javadoc)
	 * @see com.mitc.redes.editorfip.servicios.gestionfip.validaciones.ServicioGestionValidaciones#validacion254(int)
	 */
	public List<ElementoValidado> validacion254 (int idTramite, ProcesoValidacion procVal)
	{
		log.debug("[validacion254] validacion254 para idTramite="+idTramite);
		List<ElementoValidado> resultado = new ArrayList<ElementoValidado>();
		
		String nombreValidacion = (String)messages.get("validacion_254_nombre");
		
		String queryValidacion = "select ent.iden, ent.nombre from planeamiento.entidad ent where ent.iden in " +
								"(select cu.identidad " +
								"from planeamiento.entidad ent, planeamiento.entidaddeterminacion cu " +
								"where ent.idtramite = " + idTramite + " and cu.identidad = ent.iden " +
								"group by cu.identidad, cu.iddeterminacion " +
								"having (count(cu.identidad)>1 and count(cu.iddeterminacion)>1)) ";
		
		List<Object[]> resConsulta = new ArrayList<Object[]>();

		  try {
			  resConsulta = (List<Object[]>) em.createNativeQuery(queryValidacion).getResultList();
		     
		     
		     
		 } catch (NoResultException e) {
		     log.error("[validacion254] No se ha encontrado validacion254." + e.getMessage());
		
		 } catch (Exception ex) {
		     log.error("[validacion254] Error en la busqueda del validacion254: " + ex.getMessage());
		
		 }
		 
		 
		 // Validacion Correcta
		 if (resConsulta.size()==0)
	     {
			// Creo el objeto resultado
			 ElementoValidado elemVal = new ElementoValidado();
			 
			 elemVal.setNombrevalidacion(nombreValidacion);
			 elemVal.setProcesoValidacion(procVal);
			 elemVal.setObjetovalidado("Todas las entidades con determinaciones aplicadas del tramite id="+idTramite);
			 elemVal.setValidacionok(true);
			 
			 
			 // Lo añado al resultado
			 resultado.add(elemVal);
	     }
		 else
		 {
			 // Validacion Incorrecta
			 
			 for (Object[] objConsulta : resConsulta)
			 {
				// Creo el objeto resultado
				 ElementoValidado elemVal = new ElementoValidado();
				 
				 elemVal.setNombrevalidacion(nombreValidacion);
				 elemVal.setProcesoValidacion(procVal);
				 elemVal.setObjetovalidado("Entidad con la misma determinación aplicada mas de una vez '" + (String)objConsulta[1] + "' del Tramite id="+idTramite);
				 elemVal.setValidacionok(false);
				 elemVal.setObservacionesvalidacion("Entidad con la misma determinación aplicada mas de una vez: '"+(String)objConsulta[1]+
						 "' Id:"+(Integer)objConsulta[0]);
				 
				 
				 // Lo añado al resultado
				 resultado.add(elemVal);
			 }
		 }
		
		log.debug("[validacion254] elementos validados="+resultado.size());
		return resultado;
	}
	
	/* (non-Javadoc)
	 * @see com.mitc.redes.editorfip.servicios.gestionfip.validaciones.ServicioGestionValidaciones#validacion255(int)
	 */
	public List<ElementoValidado> validacion255 (int idTramite, ProcesoValidacion procVal)
	{
		log.debug("[validacion255] validacion255 para idTramite="+idTramite);
		List<ElementoValidado> resultado = new ArrayList<ElementoValidado>();
		
		String nombreValidacion = (String)messages.get("validacion_255_nombre");
		
		String queryValidacion = "select ent.iden, ent.nombre, det.iden, det.nombre " +
								"from planeamiento.entidad ent, planeamiento.determinacion det, planeamiento.entidaddeterminacion cu " +
								"where ent.idtramite = " + idTramite + " and cu.identidad = ent.iden and cu.iddeterminacion=det.iden and cu.iden not in " +
								"(select entdet.iden from planeamiento.entidad ent " +
								"JOIN planeamiento.entidaddeterminacion entdet ON entdet.identidad=ent.iden " +
								"JOIN planeamiento.casoentidaddeterminacion caso ON caso.identidaddeterminacion = entdet.iden " +
								"where ent.idtramite = " + idTramite + ")";
		
		List<Object[]> resConsulta = new ArrayList<Object[]>();

		  try {
			  resConsulta = (List<Object[]>) em.createNativeQuery(queryValidacion).getResultList();
		     
		     
		     
		 } catch (NoResultException e) {
		     log.error("[validacion255] No se ha encontrado validacion255." + e.getMessage());
		
		 } catch (Exception ex) {
		     log.error("[validacion255] Error en la busqueda del validacion255: " + ex.getMessage());
		
		 }
		 
		 
		 // Validacion Correcta
		 if (resConsulta.size()==0)
	     {
			// Creo el objeto resultado
			 ElementoValidado elemVal = new ElementoValidado();
			 
			 elemVal.setNombrevalidacion(nombreValidacion);
			 elemVal.setProcesoValidacion(procVal);
			 elemVal.setObjetovalidado("Las determinaciones aplicadas a una entidad del tramite id="+idTramite);
			 elemVal.setValidacionok(true);
			 
			 
			 // Lo añado al resultado
			 resultado.add(elemVal);
	     }
		 else
		 {
			 // Validacion Incorrecta
			 
			 for (Object[] objConsulta : resConsulta)
			 {
				// Creo el objeto resultado
				 ElementoValidado elemVal = new ElementoValidado();
				 
				 elemVal.setNombrevalidacion(nombreValidacion);
				 elemVal.setProcesoValidacion(procVal);
				 elemVal.setObjetovalidado("Determinacion aplicada a una entidad y sin ningun caso: Entidad: '" + (String)objConsulta[1] + "' Det: '"+(String)objConsulta[3]+"' del Tramite id="+idTramite);
				 elemVal.setValidacionok(false);
				 elemVal.setObservacionesvalidacion("Determinaciones aplicadas a una entidad y sin ningun caso: Entidad: '"+(String)objConsulta[1]+
						 "' Id:"+(Integer)objConsulta[0] +
						 " Id Determinacion:"+(Integer)objConsulta[2] +
						 " Nombre Det:"+(String)objConsulta[3]);
				 
				 
				 // Lo añado al resultado
				 resultado.add(elemVal);
			 }
		 }
		
		log.debug("[validacion255] elementos validados="+resultado.size());
		return resultado;
	}
	
	/* (non-Javadoc)
	 * @see com.mitc.redes.editorfip.servicios.gestionfip.validaciones.ServicioGestionValidaciones#validacion256(int)
	 */
	public List<ElementoValidado> validacion256 (int idTramite, ProcesoValidacion procVal)
	{
		log.debug("[validacion256] validacion256 para idTramite="+idTramite);
		List<ElementoValidado> resultado = new ArrayList<ElementoValidado>();
		
		String nombreValidacion = (String)messages.get("validacion_256_nombre");
		
		String queryValidacion = "select ent.iden, ent.nombre, caso.nombre from planeamiento.entidad ent, " + 
								"planeamiento.entidaddeterminacion entdet, planeamiento.casoentidaddeterminacion caso " + 
								"where ent.idtramite = " + idTramite + " and ent.iden = entdet.identidad and entdet.iden = caso.identidaddeterminacion and caso.iden in " +
								"(select doccaso.idcaso from planeamiento.documentocaso doccaso group by " +  
								"doccaso.idcaso,doccaso.iddocumento having (count(doccaso.idcaso)>1 and count(doccaso.iddocumento)>1)) ";
		
		List<Object[]> resConsulta = new ArrayList<Object[]>();

		  try {
			  resConsulta = (List<Object[]>) em.createNativeQuery(queryValidacion).getResultList();
		     
		     
		     
		 } catch (NoResultException e) {
		     log.error("[validacion256] No se ha encontrado validacion256." + e.getMessage());
		
		 } catch (Exception ex) {
		     log.error("[validacion256] Error en la busqueda del validacion256: " + ex.getMessage());
		
		 }
		 
		 
		 // Validacion Correcta
		 if (resConsulta.size()==0)
	     {
			// Creo el objeto resultado
			 ElementoValidado elemVal = new ElementoValidado();
			 
			 elemVal.setNombrevalidacion(nombreValidacion);
			 elemVal.setProcesoValidacion(procVal);
			 elemVal.setObjetovalidado("Todos los casos de las CUs del tramite id="+idTramite);
			 elemVal.setValidacionok(true);
			 
			 
			 // Lo añado al resultado
			 resultado.add(elemVal);
	     }
		 else
		 {
			 // Validacion Incorrecta
			 
			 for (Object[] objConsulta : resConsulta)
			 {
				// Creo el objeto resultado
				 ElementoValidado elemVal = new ElementoValidado();
				 
				 elemVal.setNombrevalidacion(nombreValidacion);
				 elemVal.setProcesoValidacion(procVal);
				 elemVal.setObjetovalidado("Caso con mas de una asignación al mismo documento. Entidad: '" + (String)objConsulta[1] + "' del Tramite id="+idTramite);
				 elemVal.setValidacionok(false);
				 elemVal.setObservacionesvalidacion("Caso con mas de una asignación al mismo documento: Entidad: '"+(String)objConsulta[1]+
						 "' Id:"+(Integer)objConsulta[0] +
						 " Caso:"+(String)objConsulta[2]);
				 
				 
				 // Lo añado al resultado
				 resultado.add(elemVal);
			 }
		 }
		
		log.debug("[validacion256] elementos validados="+resultado.size());
		return resultado;
	}
	
	/* (non-Javadoc)
	 * @see com.mitc.redes.editorfip.servicios.gestionfip.validaciones.ServicioGestionValidaciones#validacion257(int)
	 */
	public List<ElementoValidado> validacion257 (int idTramite, ProcesoValidacion procVal)
	{
		log.debug("[validacion257] validacion257 para idTramite="+idTramite);
		List<ElementoValidado> resultado = new ArrayList<ElementoValidado>();
		
		String nombreValidacion = (String)messages.get("validacion_257_nombre");
		
		String queryValidacion = "select ent.iden, ent.nombre, det.iden, det.nombre " + 
		"from planeamiento.entidad ent, planeamiento.determinacion det, planeamiento.entidaddeterminacion cu, planeamiento.casoentidaddeterminacion caso " + 
		"where ent.idtramite = " + idTramite + " and cu.identidad = ent.iden and cu.iddeterminacion=det.iden and caso.identidaddeterminacion = cu.iden " + 
		"and (caso.iden not in " + 
		"(select caso.iden from planeamiento.entidad ent " + 
		"JOIN planeamiento.entidaddeterminacion entdet ON entdet.identidad=ent.iden " + 
		"JOIN planeamiento.casoentidaddeterminacion caso ON caso.identidaddeterminacion = entdet.iden " + 
		"JOIN planeamiento.entidaddeterminacionregimen reg ON reg.idcaso = caso.iden " + 
		"where ent.idtramite = " + idTramite + ") " + 
		"and caso.iden not in " + 
		"(select idcaso from planeamiento.documentocaso)) ";
		
		List<Object[]> resConsulta = new ArrayList<Object[]>();

		  try {
			  resConsulta = (List<Object[]>) em.createNativeQuery(queryValidacion).getResultList();
		     
		     
		     
		 } catch (NoResultException e) {
		     log.error("[validacion257] No se ha encontrado validacion257." + e.getMessage());
		
		 } catch (Exception ex) {
		     log.error("[validacion257] Error en la busqueda del validacion257: " + ex.getMessage());
		
		 }
		 
		 
		 // Validacion Correcta
		 if (resConsulta.size()==0)
	     {
			// Creo el objeto resultado
			 ElementoValidado elemVal = new ElementoValidado();
			 
			 elemVal.setNombrevalidacion(nombreValidacion);
			 elemVal.setProcesoValidacion(procVal);
			 elemVal.setObjetovalidado("Los casos de las CUs del tramite id="+idTramite);
			 elemVal.setValidacionok(true);
			 
			 
			 // Lo añado al resultado
			 resultado.add(elemVal);
	     }
		 else
		 {
			 // Validacion Incorrecta
			 
			 for (Object[] objConsulta : resConsulta)
			 {
				// Creo el objeto resultado
				 ElementoValidado elemVal = new ElementoValidado();
				 
				 elemVal.setNombrevalidacion(nombreValidacion);
				 elemVal.setProcesoValidacion(procVal);
				 elemVal.setObjetovalidado("Caso sin documento ni regimen. Entidad que contiene caso: '" + (String)objConsulta[1] + "' del Tramite id="+idTramite);
				 elemVal.setValidacionok(false);
				 elemVal.setObservacionesvalidacion("Caso sin documento ni regimen: Entidad:'"+(String)objConsulta[1]+
						 "' Id:"+(Integer)objConsulta[0] +
						 " Id Det:"+(Integer)objConsulta[2] +
						 " Nombre Det:'"+(String)objConsulta[3]+"'");
				 
				 
				 // Lo añado al resultado
				 resultado.add(elemVal);
			 }
		 }
		
		log.debug("[validacion257] elementos validados="+resultado.size());
		return resultado;
	}
	
	/* (non-Javadoc)
	 * @see com.mitc.redes.editorfip.servicios.gestionfip.validaciones.ServicioGestionValidaciones#validacion258(int)
	 */
	public List<ElementoValidado> validacion258 (int idTramite, ProcesoValidacion procVal)
	{
		log.debug("[validacion258] validacion258 para idTramite="+idTramite);
		List<ElementoValidado> resultado = new ArrayList<ElementoValidado>();
		
		//
		
		log.debug("[validacion258] elementos validados="+resultado.size());
		return resultado;
	}
	
	/* (non-Javadoc)
	 * @see com.mitc.redes.editorfip.servicios.gestionfip.validaciones.ServicioGestionValidaciones#validacion259(int)
	 */
	public List<ElementoValidado> validacion259 (int idTramite, ProcesoValidacion procVal)
	{
		log.debug("[validacion259] validacion259 para idTramite="+idTramite);
		List<ElementoValidado> resultado = new ArrayList<ElementoValidado>();
		
		//
		
		log.debug("[validacion259] elementos validados="+resultado.size());
		return resultado;
	}
	
	/* (non-Javadoc)
	 * @see com.mitc.redes.editorfip.servicios.gestionfip.validaciones.ServicioGestionValidaciones#validacion2510(int)
	 */
	public List<ElementoValidado> validacion2510 (int idTramite, ProcesoValidacion procVal)
	{
		log.debug("[validacion2510] validacion2510 para idTramite="+idTramite);
		List<ElementoValidado> resultado = new ArrayList<ElementoValidado>();
		
		//
		
		log.debug("[validacion2510] elementos validados="+resultado.size());
		return resultado;
	}
	
	// ------------------------------
    // VALIDACIONES OPERACIONES
    // ------------------------------	
	
	/* (non-Javadoc)
	 * @see com.mitc.redes.editorfip.servicios.gestionfip.validaciones.ServicioGestionValidaciones#validacionesOperaciones(int)
	 */
	public List<ElementoValidado> validacionesOperaciones (int idTramite, ProcesoValidacion procVal)
	{
		log.debug("[validacionesOperaciones] validacionesOperaciones para idTramite="+ idTramite);
		List<ElementoValidado> resultado = new ArrayList<ElementoValidado>();

		//

		// Obtengo el tipo de validacion
		TipoValidacion tipoVal=null;

		String queryTipoValidacion = "SELECT tipo "
				+ " FROM TipoValidacion tipo " 
				+ " WHERE tipo.codigo='OPE'";

		try {
			tipoVal = (TipoValidacion) em.createQuery(queryTipoValidacion).getSingleResult();
		} catch (NoResultException e) {
			log.error("[validacionesOperaciones] No se ha encontrado el TipoValidacion."+ e.getMessage());

		} catch (Exception ex) {
			log.error("[validacionesOperaciones] Error en la busqueda del TipoValidacion: "+ ex.getMessage());

		}
		
		// Agrego las validaciones de los subapartados
		List<ElementoValidado> resulValidacion261 = validacion261(idTramite,procVal);
		resultado.addAll(resulValidacion261);
		
		List<ElementoValidado> resulValidacion262 = validacion262(idTramite,procVal);
		resultado.addAll(resulValidacion262);
		
		List<ElementoValidado> resulValidacion263 = validacion263(idTramite,procVal);
		resultado.addAll(resulValidacion263);
		
		List<ElementoValidado> resulValidacion264 = validacion264(idTramite,procVal);
		resultado.addAll(resulValidacion264);
						
		// Si hay tipo de validacion, recorro todos los resultados estableciendolo
		if (tipoVal!=null)
		{
			for (ElementoValidado elemVal: resultado)
			{
				elemVal.setTipoValidacion(tipoVal);
			}
		}
		

		log.debug("[validacionesOperaciones] elementos validados="+ resultado.size());
		return resultado;
	}
	
	/* (non-Javadoc)
	 * @see com.mitc.redes.editorfip.servicios.gestionfip.validaciones.ServicioGestionValidaciones#validacion261(int)
	 */
	public List<ElementoValidado> validacion261 (int idTramite, ProcesoValidacion procVal)
	{
		log.debug("[validacion261] validacion261 para idTramite="+idTramite);
		List<ElementoValidado> resultado = new ArrayList<ElementoValidado>();
		
		//
		
		log.debug("[validacion261] elementos validados="+resultado.size());
		return resultado;
	}
	
	/* (non-Javadoc)
	 * @see com.mitc.redes.editorfip.servicios.gestionfip.validaciones.ServicioGestionValidaciones#validacion262(int)
	 */
	public List<ElementoValidado> validacion262 (int idTramite, ProcesoValidacion procVal)
	{
		log.debug("[validacion262] validacion262 para idTramite="+idTramite);
		List<ElementoValidado> resultado = new ArrayList<ElementoValidado>();
		
		//
		
		log.debug("[validacion262] elementos validados="+resultado.size());
		return resultado;
	}
	
	/* (non-Javadoc)
	 * @see com.mitc.redes.editorfip.servicios.gestionfip.validaciones.ServicioGestionValidaciones#validacion263(int)
	 */
	public List<ElementoValidado> validacion263 (int idTramite, ProcesoValidacion procVal)
	{
		log.debug("[validacion263] validacion263 para idTramite="+idTramite);
		List<ElementoValidado> resultado = new ArrayList<ElementoValidado>();
		
		//
		
		log.debug("[validacion263] elementos validados="+resultado.size());
		return resultado;
	}
	
	/* (non-Javadoc)
	 * @see com.mitc.redes.editorfip.servicios.gestionfip.validaciones.ServicioGestionValidaciones#validacion264(int)
	 */
	public List<ElementoValidado> validacion264 (int idTramite, ProcesoValidacion procVal)
	{
		log.debug("[validacion264] validacion264 para idTramite="+idTramite);
		List<ElementoValidado> resultado = new ArrayList<ElementoValidado>();
		
		//
		
		log.debug("[validacion264] elementos validados="+resultado.size());
		return resultado;
	}
	
	// ------------------------------
    // VALIDACIONES ADSCRIPCIONES
    // ------------------------------	
	
	/* (non-Javadoc)
	 * @see com.mitc.redes.editorfip.servicios.gestionfip.validaciones.ServicioGestionValidaciones#validacionesAdscripciones(int)
	 */
	public List<ElementoValidado> validacionesAdscripciones (int idTramite, ProcesoValidacion procVal)
	{
		log.debug("[validacionesAdscripciones] validacionesAdscripciones para idTramite="+ idTramite);
		List<ElementoValidado> resultado = new ArrayList<ElementoValidado>();

		//

		// Obtengo el tipo de validacion
		TipoValidacion tipoVal=null;

		String queryTipoValidacion = "SELECT tipo "
				+ " FROM TipoValidacion tipo " 
				+ " WHERE tipo.codigo='ADS'";

		try {
			tipoVal = (TipoValidacion) em.createQuery(queryTipoValidacion).getSingleResult();
		} catch (NoResultException e) {
			log.error("[validacionesAdscripciones] No se ha encontrado el TipoValidacion."+ e.getMessage());

		} catch (Exception ex) {
			log.error("[validacionesAdscripciones] Error en la busqueda del TipoValidacion: "+ ex.getMessage());

		}
		
		// Agrego las validaciones de los subapartados
		List<ElementoValidado> resulValidacion271 = validacion271(idTramite,procVal);
		resultado.addAll(resulValidacion271);
								
		// Si hay tipo de validacion, recorro todos los resultados estableciendolo
		if (tipoVal!=null)
		{
			for (ElementoValidado elemVal: resultado)
			{
				elemVal.setTipoValidacion(tipoVal);
			}
		}
		

		log.debug("[validacionesAdscripciones] elementos validados="+ resultado.size());
		return resultado;
	}
	
	/* (non-Javadoc)
	 * @see com.mitc.redes.editorfip.servicios.gestionfip.validaciones.ServicioGestionValidaciones#validacion271(int)
	 */
	public List<ElementoValidado> validacion271 (int idTramite, ProcesoValidacion procVal)
	{
		log.debug("[validacion271] validacion271 para idTramite="+idTramite);
		List<ElementoValidado> resultado = new ArrayList<ElementoValidado>();
		
		//
		
		log.debug("[validacion271] elementos validados="+resultado.size());
		return resultado;
	}
    
}

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

package com.mitc.redes.editorfip.servicios.gestionfip.gestionprerefundido;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.log.Log;

import com.mitc.redes.editorfip.entidades.rpm.diccionario.Ambito;
import com.mitc.redes.editorfip.entidades.rpm.diccionario.Instrumentotipooperacionplan;
import com.mitc.redes.editorfip.entidades.rpm.diccionario.Tipooperacionplan;
import com.mitc.redes.editorfip.entidades.rpm.gestionfip.FipsGenerados;
import com.mitc.redes.editorfip.entidades.rpm.gestionfip.Prerefundido;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Operacionplan;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Plan;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Tramite;

@Stateless
@Name("servicioBasicoPrerefundido")
public class ServicioBasicoPrerefundidoBean implements	ServicioBasicoPrerefundido {
	
	@Logger
	private Log log;

	@PersistenceContext
	EntityManager em;

	
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public int operacionesPlanPreviaPrerefundido(int idTramiteEncargado,int idTramiteVigente,int idTramiteBase, int idTipoOperacion)
	{
		log.debug("[operacionesPlanPreviaPrerefundido] idTramiteBase="+idTramiteBase+" idTramiteEncargado="+idTramiteEncargado+" idTramiteVigente="+idTramiteVigente+" idTipoOperacion="+idTipoOperacion);
		int resultado = 0;
		
		
		// Si la operacion es de sustitucion (4),SUSPENSIONTOTAL (3) o Revocacion TOTAL (6)  tengo que quitar el IDPADRE del tramiteEncargado
		if (idTipoOperacion==4 || idTipoOperacion==3 || idTipoOperacion==6  )
		{
			log.debug("[operacionesPlanPreviaPrerefundido] la operacion es de sustitucion (4),SUSPENSIONTOTAL (3) o Revocacion TOTAL (6)  tengo que quitar el IDPADRE del tramiteEncargado");
			Tramite trEncargado = em.find(Tramite.class, idTramiteEncargado);
			Plan plEncargado = em.find(Plan.class, trEncargado.getPlan().getIden());
			
			plEncargado.setPlanByIdpadre(null);
			
			em.merge(plEncargado);
			em.flush();
			
		}
		
		// Inicialmente tengo que comprobar si en la tabla operacionplan estan esos planes y con que operacion, 
		// y borrarlo en el caso que existan y meter la nueva operacion de prerefundido
		
		// Obtengo los idPlan
		int idPlanEncargado;
		int idPlanVigente;
		int idPlanBase;
		
		Tramite tramEncargado = em.find(Tramite.class, idTramiteEncargado);
		idPlanEncargado = tramEncargado.getPlan().getIden();
		// Actualizo la fecha consolidado
		tramEncargado.setFechaconsolidacion(new Date());
		em.merge(tramEncargado);
		em.flush();
		
		Tramite tramVigente = em.find(Tramite.class, idTramiteVigente);
		idPlanVigente = tramVigente.getPlan().getIden();
		// Actualizo la fecha consolidado
		tramVigente.setFechaconsolidacion(new Date());
		em.merge(tramVigente);
		em.flush();
		
		
		
		Tramite tramBase = em.find(Tramite.class, idTramiteBase);
		idPlanBase = tramBase.getPlan().getIden();
		// Actualizo la fecha consolidado
		tramBase.setFechaconsolidacion(new Date());
		em.merge(tramBase);
		em.flush();
		
		
		
		// ----
		//
		// -----
		
		String queryBuscarOperacionesPV = " SELECT opplan from Operacionplan opplan" +
				" where opplan.planByIdplanoperador.iden = "+idPlanVigente;
		
		List<Operacionplan> listaOperacionesPV = null;
		
		try {
			listaOperacionesPV = (List<Operacionplan>) em.createQuery(queryBuscarOperacionesPV).getResultList();
			
			for(Operacionplan op : listaOperacionesPV)
			{
				em.remove(op);
			}
			
			em.flush();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
			
		
		// Inserto la  operacion de No Procede en el Pla Vigente
		Operacionplan opPlanNuevaPV = new Operacionplan();
		
		opPlanNuevaPV.setIdinstrumentotipooperacion(16);
		opPlanNuevaPV.setPlanByIdplanoperador(tramVigente.getPlan());
		
		em.persist(opPlanNuevaPV);
		em.flush();
		
		// -----
		//
		// ------
		
		String queryBuscarOperacionesPB = " SELECT opplan from Operacionplan opplan" +
				" where opplan.planByIdplanoperador.iden = "+idPlanBase;
		
		List<Operacionplan> listaOperacionesPB = null;
		
		try {
			listaOperacionesPB = (List<Operacionplan>) em.createQuery(queryBuscarOperacionesPB).getResultList();
			
			for(Operacionplan op : listaOperacionesPV)
			{
				em.remove(op);
			}
			
			em.flush();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
			
		
		// Inserto la  operacion de No Procede en el Pla Base
		Operacionplan opPlanNuevaBase = new Operacionplan();
				
		opPlanNuevaBase.setIdinstrumentotipooperacion(6);
		opPlanNuevaBase.setPlanByIdplanoperador(tramBase.getPlan());
				
		em.persist(opPlanNuevaBase);
		em.flush();
		
		// Busco si en la BD en la tabla operacion plan hay alguna operacion con estos 2 planes, en caso de haberla, la borro
		
		String queryBuscarOperaciones = " SELECT opplan from Operacionplan opplan" +
				" where opplan.planByIdplanoperado.iden = "+idPlanVigente+
				" and opplan.planByIdplanoperador.iden = "+idPlanEncargado;
		
		List<Operacionplan> listaOperaciones = null;
		
		try {
			listaOperaciones = (List<Operacionplan>) em.createQuery(queryBuscarOperaciones).getResultList();
			
			// Si hay operaciones, las borro
			if (listaOperaciones.size()>0)
			{
				log.debug("[operacionesPlanPreviaPrerefundido] listaOperaciones="+listaOperaciones.size());
				
				for (Operacionplan opplan:listaOperaciones)
				{
					em.remove(opplan);
				}
			}
			
			// Del tipo de operacion plan, busco un instrumetotipooperacion para insertarlo
			String queryBuscarInstrumentotipooperacion = " SELECT instplan from Instrumentotipooperacionplan instplan" +
			" where instplan.tipooperacionplan.iden = "+idTipoOperacion;
			
			List<Instrumentotipooperacionplan> listaInstrumentos = null;
			
			 try {
				listaInstrumentos = (List<Instrumentotipooperacionplan>) em.createQuery(queryBuscarInstrumentotipooperacion).getResultList();
				
				if (listaInstrumentos.size()>0)
				{
					// Cojo uno cualquiera
					Instrumentotipooperacionplan tipoInstr = listaInstrumentos.get(0);
					log.debug("[operacionesPlanPreviaPrerefundido] tipoInstr="+tipoInstr.getIden());
					
					
					// Inserto la nueva operacion
					Operacionplan opPlanNueva = new Operacionplan();
					
					opPlanNueva.setIdinstrumentotipooperacion(tipoInstr.getIden());
					opPlanNueva.setPlanByIdplanoperado(tramVigente.getPlan());
					opPlanNueva.setPlanByIdplanoperador(tramEncargado.getPlan());
					
					em.persist(opPlanNueva);
					em.flush();
					
					resultado = opPlanNueva.getIden();
					log.debug("[operacionesPlanPreviaPrerefundido] resultado="+resultado);
					
					
				}
				else
				{
					log.error("[operacionesPlanPreviaPrerefundido] Operacion no implementada en la BD.");
					resultado = -1;
				}
				
				 
	        } catch (NoResultException e) {
	            log.error("[operacionesPlanPreviaPrerefundido] No se ha encontrado Instrumentotipooperacionplan." + e.getMessage());
	            resultado = -1;
	            e.printStackTrace();

	        } catch (Exception ex) {
	            log.error("[operacionesPlanPreviaPrerefundido] Error en la busqueda de Instrumentotipooperacionplan: " + ex.getMessage());
	            ex.printStackTrace();
	        }
			
			
		}
        catch (Exception ex) {
           log.error("[operacionesPlanPreviaPrerefundido] Error en la busqueda de listaOperaciones: " + ex.getMessage());
           ex.printStackTrace();
       }
		
		
		return resultado;
		
	}

	@Override
	public List<Prerefundido> obtenerListaPrerefundido() {

		log.debug("[obtenerListaPrerefundido] lista de prerefundido");
		List<Prerefundido> listaPrerefundido = new ArrayList<Prerefundido>();
		
	        String queryListaPrerefundido =  "SELECT preref " +
	                " FROM Prerefundido preref ORDER BY preref.id DESC" ;

	         try {
	        	 listaPrerefundido = (List<Prerefundido>) em.createQuery(queryListaPrerefundido).getResultList();
	        } catch (NoResultException e) {
	            log.error("[obtenerListaPrerefundido] No se ha encontrado listaPrerefundido." + e.getMessage());

	        } catch (Exception ex) {
	            log.error("[obtenerListaPrerefundido] Error en la busqueda de listaPrerefundido: " + ex.getMessage());
	            ex.printStackTrace();
	        }

		
		log.debug("[obtenerListaPrerefundido] Obtenida lista de Prerefundido. Elementos="+listaPrerefundido.size());
		return listaPrerefundido;
	}
	
	@Override
	public List<Prerefundido> obtenerListaInfoPrerefundido(int idPlanEncargado) {

		log.debug("[obtenerListaInfoPrerefundido] lista de prerefundido (Info)");
		List<Prerefundido> listaPrerefundido = new ArrayList<Prerefundido>();
		
	        String queryListaPrerefundido =  "SELECT preref " +
	                " FROM Prerefundido preref " +
	                " WHERE preref.consolidado = true " +
	                " AND preref.idTramiteEncargado = " +idPlanEncargado +" ORDER BY preref.fechaInicio DESC" ;

	         try {
	        	 listaPrerefundido = (List<Prerefundido>) em.createQuery(queryListaPrerefundido).getResultList();
	        } catch (NoResultException e) {
	            log.error("[obtenerListaPrerefundido] No se ha encontrado listaPrerefundido." + e.getMessage());

	        } catch (Exception ex) {
	            log.error("[obtenerListaPrerefundido] Error en la busqueda de listaPrerefundido: " + ex.getMessage());
	            ex.printStackTrace();
	        }

		
		log.debug("[obtenerListaPrerefundido] Obtenida lista de Prerefundido. Elementos="+listaPrerefundido.size());
		return listaPrerefundido;
	}
	
	
	public int crearPrerefundido(int idTramitePrerefundido, int idTramiteEncargado,int idTramiteVigente, int idTipoOperacion, String rutaFichero)
	{
		log.debug("[crearPrerefundido] idTramitePrerefundido="+idTramitePrerefundido+" idTramiteVigente="+idTramiteVigente+" idTramiteEncargado="+idTramiteEncargado+" idTipoOperacion="+idTipoOperacion+" rutaFichero="+rutaFichero);
		int resultado = 0;
		
		try {
			// Me creo el objeto que se pretende persistir
			Prerefundido preref = new Prerefundido();
			
			// Establezco la ruta
			preref.setFicheroXML(rutaFichero);
			
			// Establezo la fecha
			preref.setFechaInicio(new Date());
			
			// Establezco que aun no esta consolidado
			preref.setConsolidado(false);
			
			// Establezco el tipo de operacion entre planes
			preref.setTipoopplan(em.find(Tipooperacionplan.class, idTipoOperacion));
			
			// Pong el idproceso a cero (es la relacion con el esquema refundido)
			preref.setIdprocesorefundido(0);
			
			
			// Establezo el tramite de prerefundido
			preref.setIdTramitePrerefundido( idTramitePrerefundido);
			
			// Establezco el tramite encargado
			Tramite tramEncargado = em.find(Tramite.class, idTramiteEncargado);
			
			preref.setIdTramiteEncargado(tramEncargado.getIden());
			
			// Establezco el ambito - Lo cogo del tramite encargado	
			preref.setAmbito(em.find(Ambito.class, tramEncargado.getPlan().getIdambito()));
			
			// Establezo el tramite vigente
			preref.setIdTramiteVigente(idTramiteVigente);
			
			// Obtengo el idtramite del ultimo tramite prerefundido, de lo contrario pongo 0
			String queryUltimoTram = "from Tramite tr where tr.idtipotramite = 11 order by iden desc";
			
			List<Tramite> tramList = (List<Tramite>)em.createQuery(queryUltimoTram).getResultList();
			
			int idTramUltimoPrerefundido = 0;
			if (tramList.size()!=0)
			{
				Tramite tram = tramList.get(0);
				idTramUltimoPrerefundido = tram.getIden();
			}
			
			preref.setIdultimotramprerefundido(idTramUltimoPrerefundido);
			
			
			
			// Persisto
			em.persist(preref);
			
			// Hago flush
			em.flush();
			resultado = preref.getId().intValue();
		
		} catch (Exception ex) {
	        log.error("[crearPrerefundido] Error en la creacion de prerefundido: " + ex.getMessage());
	        ex.printStackTrace();
	
	    }
		
		
		
		
		
		
		
		
		log.debug("[crearPrerefundido] terminado con resultado= "+resultado);
		return resultado;
	}

	public boolean ponerConsolidadoATrue(int idPrerefundido)
	{
		boolean res=false;
		log.debug("[ponerConsolidadoATrue] idPrerefundido= "+idPrerefundido);
		
		Prerefundido preref = obtenerPrerefundido(idPrerefundido);
		try
		{
			preref.setConsolidado(true);
			em.merge(preref);
			em.flush();
			res=true;
		}
		catch (Exception ex) {
	        log.error("[ponerConsolidadoATrue] Error: " + ex.getMessage());
	        ex.printStackTrace();
	
	    }
		
		
		
		log.debug("[ponerConsolidadoATrue] terminado con resultado= "+res);
		return res;
	}
	
	public Prerefundido obtenerPrerefundido(int idPrerefundido)
	{
		
		Prerefundido preref = em.find(Prerefundido.class, (long)idPrerefundido);
		
		return preref;
	}
	
	public int obtenerSiguienteVersion (int idTramite)
	{
		Long total;
		String query = "SELECT count(*)" +
        " FROM FipsGenerados ent where ent.idTramiteEncargado=" + idTramite;
		try {
			total = (Long) em.createQuery(query).getSingleResult();
		
			if (total!=null)
				return Integer.parseInt(total.toString());
			else
				return 0;
		}
		catch (Exception e) {
			// TODO: handle exception
		}
		
		return 0;
			
	}

}

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
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.Remove;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.international.StatusMessage.Severity;
import org.jboss.seam.log.Log;

import com.mitc.redes.editorfip.entidades.interfazgrafico.CondicionUrbanisticaSimplificadaDTO;
import com.mitc.redes.editorfip.entidades.interfazgrafico.DeterminacionDTO;
import com.mitc.redes.editorfip.entidades.interfazgrafico.RegimenesEspecificosSimplificadosDTO;
import com.mitc.redes.editorfip.entidades.rpm.gestionfip.CUAdaptadaSipu;
import com.mitc.redes.editorfip.entidades.rpm.gestionfip.RegimenesEspecificosSimplificadosDeterminacion;
import com.mitc.redes.editorfip.entidades.rpm.gestionfip.RegimenesEspecificosSimplificadosUsoActos;
import com.mitc.redes.editorfip.servicios.basicos.fip.determinaciones.ServicioBasicoDeterminaciones;
import com.mitc.redes.editorfip.servicios.basicos.fip.entidades.ServicioBasicoCondicionesUrbanisticas;
import com.mitc.redes.editorfip.servicios.sesion.VariablesSesionUsuario;

/**
 * Session Bean implementation class ImportadorCUCanariasBDAuxiliar
 */
@Stateless
@Name("importadorCUCanariasBDAuxiliar")
public class ImportadorCUCanariasBDAuxiliarBean implements
		ImportadorCUCanariasBDAuxiliar {

	@PersistenceContext
	private EntityManager em;
	
	 // Creacion del contexto
    @Resource
    private SessionContext contextoTransaccion;

	@Logger
	private Log log;
	
	private boolean mostrarBotonImportar;

	@In(create = true, required = false)
	ServicioBasicoCondicionesUrbanisticas servicioBasicoCondicionesUrbanisticas;

	@In(create = true, required = false)
	ServicioBasicoDeterminaciones servicioBasicoDeterminaciones;
	
	@In (create = true, required = false)
	VariablesSesionUsuario variablesSesionUsuario;
	
	@In(create=true)
    FacesMessages facesMessages;
	
	private List<String> listadoErroresImportacion;
	
	
	public List<String> getListadoErroresImportacion() {
		return listadoErroresImportacion;
	}
	

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public List<String> persistirCUdeBDAuxiliar() {
		
		int idTramite = variablesSesionUsuario.getTramiteTrabajoActual().getIden();
		log.info("[persistirCUdeBDAuxiliar] Se van a persistir las CU del tramite="+idTramite);
		
		List<String> listaErroresPersistir = new ArrayList<String>();

		// Lista de entidades distintas que tienen CU de ese tramite

		String queryEntidadesDistintas = "select distinct identidad from gestionfip.cuadaptadasipu where idtramite = "+idTramite;

		try {

			List<Integer> listaEntidadesDistintas = (List<Integer>) em.createNativeQuery(queryEntidadesDistintas).getResultList();
			
			log.debug("[persistirCUdeBDAuxiliar] Se han encontrado "+listaEntidadesDistintas.size()+" entidades distintas");
			
			// Por cada entidad distinta
			
			for (Integer idEntidadCU : listaEntidadesDistintas)
			{
				// -----------
				// Busco todas las entidades de ese tramite que tengan el cuadaptadaantigua a TRUE
				// -----------
				
				String queryEntidadCUAdaptadaAnt = "select cuadapt from CUAdaptadaSipu cuadapt where cuadapt.idTramite = "+idTramite 
													+ " and cuadapt.idEntidad = "+idEntidadCU
													+ " and cuadapt.cuAdptadaAntigual is true";
				
				
				try {
					
					List<CUAdaptadaSipu> cuAdaptFormaAntiguaLista = (List<CUAdaptadaSipu>) em.createQuery(queryEntidadCUAdaptadaAnt).getResultList();
					log.debug("[persistirCUdeBDAuxiliar] Se han encontrado "+cuAdaptFormaAntiguaLista.size()+" registros de CUAdaptadaSipu para la entidad= "+idEntidadCU+" con cuAdptadaAntigual=true");
					
					// TODO Adaptarla a CUSimplificadaDTO
					// Por cada registro de cuAdaptFormaAntiguaLista tengo que adaptarlo al objeto CondicionUrbanisticaSimplificadaDTO
					
					for (CUAdaptadaSipu cuAdaptFormaAntigua: cuAdaptFormaAntiguaLista)
					{
						CondicionUrbanisticaSimplificadaDTO cuUso = new CondicionUrbanisticaSimplificadaDTO();
						
						// Adapto al modelos de CUSImpl antiguo para poder persistir
						// Las adaptaciones son las siguientes:
						// CondicionUrbanisticaSimplificadaDTO   <-- CUAdaptadaSipu
						// idEntidad						<--  idEntidad
						// nombreEntidad					<-- nombreEntidad
						// claveEntidad						<-- claveEntidad
						// idDeterminacion					<-- idDeterminacion
						// nombreDeterminacion				<-- nombreDeterminacion
						// valor							<-- valor
						// idDeterminacionValorReferencia	<-- idDeterminacionValorReferencia
						// nombreDetValorRef				<-- nombreDetValorRef
						// regimenesEspecificos				<-- regEspecificoDeterminacion
						
						cuUso.setIdEntidad(cuAdaptFormaAntigua.getIdEntidad());
						cuUso.setNombreEntidad(cuAdaptFormaAntigua.getNombreEntidad());
						cuUso.setClaveEntidad(cuAdaptFormaAntigua.getClaveEntidad());
						cuUso.setIdDeterminacion(cuAdaptFormaAntigua.getIdDeterminacion());
						cuUso.setNombreDeterminacion(cuAdaptFormaAntigua.getNombreDeterminacion());
						cuUso.setValor(cuAdaptFormaAntigua.getValor());
						cuUso.setIdDeterminacionValorReferencia(cuAdaptFormaAntigua.getIdDetValorReferencia());
						cuUso.setNombreDetValorRef(cuAdaptFormaAntigua.getNombreValorReferencia());
						
						List<RegimenesEspecificosSimplificadosDTO> regEspList = new ArrayList <RegimenesEspecificosSimplificadosDTO> ();
						
						for (RegimenesEspecificosSimplificadosDeterminacion regEspDet : cuAdaptFormaAntigua.getRegEspecificoDeterminacion() )
						{
							RegimenesEspecificosSimplificadosDTO regEsp = new RegimenesEspecificosSimplificadosDTO();
							
							regEsp.setNombreRegimenEspecifico(regEspDet.getNombreRegimenEspecifico());
							regEsp.setTextoRegimenEspecifico(regEspDet.getTextoRegimenEspecifico());
							
							regEspList.add(regEsp);
						}
						
						cuUso.setRegimenesEspecificos(regEspList);
						
						// Ya tengo la transformacion hecha, la persisto
						
						int resultadoPersistir =  servicioBasicoCondicionesUrbanisticas.crearCU(cuUso);
						
						log.debug("[persistirCUdeBDAuxiliar] Se ha persistido la CU. El Iden resultadoPersistir="+resultadoPersistir);
						
						// Si el resultado es menor o igual que cero es porque se ha producido un error a la hora de persistir la CU
						if (resultadoPersistir<=0)
						{
							if (resultadoPersistir==0)
							{
								String errorPersitir= "FILA "+(cuAdaptFormaAntigua.getIdFilaExcel())+": Ha ocurrido un error al persistir la Condicion Urbanistica de la fila";
								listaErroresPersistir.add(errorPersitir);
								log.debug("[persistirCUdeBDAuxiliar]"+errorPersitir);

							}
							if (resultadoPersistir==-1)
							{
								String errorPersitir=("FILA "+(cuAdaptFormaAntigua.getIdFilaExcel())+": La entidad no tiene aun Grupo de Aplicacion aplicado y por tanto no se puede crear la CU. Ha ocurrido un error al persistir la Condicion Urbanistica de la fila");
								listaErroresPersistir.add(errorPersitir);
								log.debug("[persistirCUdeBDAuxiliar]"+errorPersitir);
							}
							if (resultadoPersistir==-2)
							{
								String errorPersitir=("FILA "+(cuAdaptFormaAntigua.getIdFilaExcel())+": No se ha encontrado en la determinacion a aplicar  que tenga como grupo a la determinacion de grupo aplicada a la entidad. Ha ocurrido un error al persistir la Condicion Urbanistica de la fila");
								listaErroresPersistir.add(errorPersitir);
								log.debug("[persistirCUdeBDAuxiliar]"+errorPersitir);
							}
							if (resultadoPersistir<=-3)
							{
								String errorPersitir=("FILA "+(cuAdaptFormaAntigua.getIdFilaExcel())+": Ha ocurrido un error al persistir la Condicion Urbanistica de la fila");
								listaErroresPersistir.add(errorPersitir);
								log.debug("[persistirCUdeBDAuxiliar]"+errorPersitir);
							}
								
						}
						else
						{
							// Se ha persistido correctamente, se devuelve el id de la CU
							// Tenemos que obtener el idRegimen y el idCaso porque van a ser necesarios 
							log.debug("[persistirCUdeBDAuxiliar] Se ha persistido la CU correctamente. El Iden resultadoPersistir="+resultadoPersistir);
							log.debug("[persistirCUdeBDAuxiliar] Tenemos que obtener el idRegimen y el idCaso porque van a ser necesarios ");
							log.debug("[persistirCUdeBDAuxiliar] idRegimen = El Iden resultadoPersistir="+resultadoPersistir);
							
							int idCasoPersitido = 0;
							
							String queryEntidaddeterminacionregimen = "select edr.casoentidaddeterminacionByIdcaso.iden from Entidaddeterminacionregimen edr where edr.iden = "+resultadoPersistir;
							
							idCasoPersitido = (Integer) em.createQuery(queryEntidaddeterminacionregimen).getSingleResult();
							log.debug("[persistirCUdeBDAuxiliar] idCasoPersitido = "+idCasoPersitido);
							
							// Guardo ambos en el registro  de cuAdaptFormaNuevaIdDetCero que le corresponde y hago merge
							cuAdaptFormaAntigua.setIdRegimen(resultadoPersistir);
							cuAdaptFormaAntigua.setIdCaso(idCasoPersitido);
							
							em.merge(cuAdaptFormaAntigua);
							log.debug("[persistirCUdeBDAuxiliar] Merge de cuAdaptFormaAntigua con  idRegimen y idCasoPersitido ");
							
							
						}
					}
					
				}
				catch (Exception ex) {
								
					String errorString = "[persistirCUdeBDAuxiliar] Error en la busqueda de las entidades de ese tramite que tengan el cuadaptadaantigua a TRUE. entidad:"+ idEntidadCU;
					log.error(errorString+ "\n" + ex.getMessage());
					listaErroresPersistir.add(errorString);
					ex.printStackTrace();
				}
				
				
				// -----------
				// Busco todas las entidades de ese tramite que tengan el cuadaptadaantigua a FALSE
				// -----------
				
				String queryEntidadCUAdaptadaNueva = "select cuadapt from CUAdaptadaSipu cuadapt where cuadapt.idTramite = "+idTramite 
													+ " and cuadapt.idEntidad = "+idEntidadCU
													+ " and cuadapt.cuAdptadaAntigual is false";
				
				
				try {
					
					List<CUAdaptadaSipu> cuAdaptFormaNueva = (List<CUAdaptadaSipu>) em.createQuery(queryEntidadCUAdaptadaNueva).getResultList();
					log.debug("[persistirCUdeBDAuxiliar] Se han encontrado "+cuAdaptFormaNueva.size()+" registros de CUAdaptadaSipu para la entidad= "+idEntidadCU+" con cuAdptadaAntigual=false");
					
					// -----------
					// Busco todas las entidades de ese tramite que tengan el cuadaptadaantigua a FALSE y iddeterminacion = 0
					// -----------
					
					String queryEntidadCUAdaptadaNuevaIdDetCero = "select cuadapt from CUAdaptadaSipu cuadapt where cuadapt.idTramite = "+idTramite 
					+ " and cuadapt.idEntidad = "+idEntidadCU
					+ " and cuadapt.cuAdptadaAntigual is false and cuadapt.idDeterminacion = 0";


					try {
					
						List<CUAdaptadaSipu> cuAdaptFormaNuevaIdDetCeroLista = (List<CUAdaptadaSipu>) em.createQuery(queryEntidadCUAdaptadaNuevaIdDetCero).getResultList();
						log.debug("[persistirCUdeBDAuxiliar] Se han encontrado "+cuAdaptFormaNuevaIdDetCeroLista.size()+" registros de CUAdaptadaSipu para la entidad= "+idEntidadCU+" con cuAdptadaAntigual=false y iddeterminacion = 0");
						
						// TODO Adaptarla a CUSimplificadaDTO
						
						// Por cada registro de cuAdaptFormaNuevaIdDetCeroLista tengo que adaptarlo al objeto CondicionUrbanisticaSimplificadaDTO
						// Tengo que tener en cuenta que cuAdaptFormaNuevaIdDetCero es como antes una determinacion regimen y su valor de referencia
						
						for (CUAdaptadaSipu cuAdaptFormaNuevaIdDetCero: cuAdaptFormaNuevaIdDetCeroLista)
						{
							CondicionUrbanisticaSimplificadaDTO cuUso = new CondicionUrbanisticaSimplificadaDTO();
							
							// Adapto al modelos de CUSImpl antiguo para poder persistir
							// Las adaptaciones son las siguientes:
							// CondicionUrbanisticaSimplificadaDTO   <-- CUAdaptadaSipu
							// idEntidad						<--  idEntidad
							// nombreEntidad					<-- nombreEntidad
							// claveEntidad						<-- claveEntidad
							// idDeterminacion					<-- idUsoActo
							// nombreDeterminacion				<-- nombreUsoActo
							// idDeterminacionRegimen			<-- idDetUsoActo
							// nombreDetRegimen					<-- nombreDetUsoActo
							// idDeterminacionValorReferencia	<-- idDetValorUsoActo
							// nombreDetValorRef				<-- nombreDetValorUsoActo
							// regimenesEspecificos				<-- regEspecificoUsoActo
							
							cuUso.setIdEntidad(cuAdaptFormaNuevaIdDetCero.getIdEntidad());
							cuUso.setNombreEntidad(cuAdaptFormaNuevaIdDetCero.getNombreEntidad());
							cuUso.setClaveEntidad(cuAdaptFormaNuevaIdDetCero.getClaveEntidad());
							cuUso.setIdDeterminacion(cuAdaptFormaNuevaIdDetCero.getIdUsoActo());
							cuUso.setNombreDeterminacion(cuAdaptFormaNuevaIdDetCero.getNombreUsoActo());
							cuUso.setIdDeterminacionRegimen(cuAdaptFormaNuevaIdDetCero.getIdDetUsoActo());
							cuUso.setNombreDetRegimen(cuAdaptFormaNuevaIdDetCero.getNombreDetUsoActo());
							cuUso.setIdDeterminacionValorReferencia(cuAdaptFormaNuevaIdDetCero.getIdDetValorUsoActo());
							cuUso.setNombreDetValorRef(cuAdaptFormaNuevaIdDetCero.getNombreDetValorUsoActo());
							
							List<RegimenesEspecificosSimplificadosDTO> regEspList = new ArrayList <RegimenesEspecificosSimplificadosDTO> ();
							
							for (RegimenesEspecificosSimplificadosUsoActos regEspUsoActo : cuAdaptFormaNuevaIdDetCero.getRegEspecificoUsoActo() )
							{
								RegimenesEspecificosSimplificadosDTO regEsp = new RegimenesEspecificosSimplificadosDTO();
								
								regEsp.setNombreRegimenEspecifico(regEspUsoActo.getNombreRegimenEspecifico());
								regEsp.setTextoRegimenEspecifico(regEspUsoActo.getTextoRegimenEspecifico());
								
								regEspList.add(regEsp);
							}
							
							cuUso.setRegimenesEspecificos(regEspList);
							
							// Ya tengo la transformacion hecha, la persisto
							
							int resultadoPersistir =  servicioBasicoCondicionesUrbanisticas.crearCU(cuUso);
							
							log.debug("[persistirCUdeBDAuxiliar] Se ha persistido la CU. El Iden resultadoPersistir="+resultadoPersistir);
    						
							// Si el resultado es menor o igual que cero es porque se ha producido un error a la hora de persistir la CU
    						if (resultadoPersistir<=0)
							{
								if (resultadoPersistir==0)
								{
									String errorPersitir= "FILA "+(cuAdaptFormaNuevaIdDetCero.getIdFilaExcel())+": Ha ocurrido un error al persistir la Condicion Urbanistica de la fila";
									listaErroresPersistir.add(errorPersitir);
									log.debug("[persistirCUdeBDAuxiliar]"+errorPersitir);

								}
								if (resultadoPersistir==-1)
								{
									String errorPersitir=("FILA "+(cuAdaptFormaNuevaIdDetCero.getIdFilaExcel())+": La entidad no tiene aun Grupo de Aplicacion aplicado y por tanto no se puede crear la CU. Ha ocurrido un error al persistir la Condicion Urbanistica de la fila");
									listaErroresPersistir.add(errorPersitir);
									log.debug("[persistirCUdeBDAuxiliar]"+errorPersitir);
								}
								if (resultadoPersistir==-2)
								{
									String errorPersitir=("FILA "+(cuAdaptFormaNuevaIdDetCero.getIdFilaExcel())+": No se ha encontrado en la determinacion a aplicar  que tenga como grupo a la determinacion de grupo aplicada a la entidad. Ha ocurrido un error al persistir la Condicion Urbanistica de la fila");
									listaErroresPersistir.add(errorPersitir);
									log.debug("[persistirCUdeBDAuxiliar]"+errorPersitir);
								}
								if (resultadoPersistir<=-3)
								{
									String errorPersitir=("FILA "+(cuAdaptFormaNuevaIdDetCero.getIdFilaExcel())+": Ha ocurrido un error al persistir la Condicion Urbanistica de la fila");
									listaErroresPersistir.add(errorPersitir);
									log.debug("[persistirCUdeBDAuxiliar]"+errorPersitir);
								}
									
							}
    						else
    						{
    							// Se ha persistido correctamente, se devuelve el id de la CU
    							// Tenemos que obtener el idRegimen y el idCaso porque van a ser necesarios 
    							log.debug("[persistirCUdeBDAuxiliar] Se ha persistido la CU correctamente. El Iden resultadoPersistir="+resultadoPersistir);
    							log.debug("[persistirCUdeBDAuxiliar] Tenemos que obtener el idRegimen y el idCaso porque van a ser necesarios ");
    							log.debug("[persistirCUdeBDAuxiliar] idRegimen = El Iden resultadoPersistir="+resultadoPersistir);
    							
    							int idCasoPersitido = 0;
    							
    							String queryEntidaddeterminacionregimen = "select edr.casoentidaddeterminacionByIdcaso.iden from Entidaddeterminacionregimen edr where edr.iden = "+resultadoPersistir;
    							
    							idCasoPersitido = (Integer) em.createQuery(queryEntidaddeterminacionregimen).getSingleResult();
    							log.debug("[persistirCUdeBDAuxiliar] idCasoPersitido = "+idCasoPersitido);
    							
    							// Guardo ambos en el registro  de cuAdaptFormaNuevaIdDetCero que le corresponde y hago merge
    							cuAdaptFormaNuevaIdDetCero.setIdRegimen(resultadoPersistir);
    							cuAdaptFormaNuevaIdDetCero.setIdCaso(idCasoPersitido);
    							
    							em.merge(cuAdaptFormaNuevaIdDetCero);
    							log.debug("[persistirCUdeBDAuxiliar] Merge de cuAdaptFormaNuevaIdDetCero con  idRegimen y idCasoPersitido ");
    							
    							
    						}
							
							
							
							
						}
						
						
						
					
					
					
					}
					catch (Exception ex) {
									
						String errorString = "[persistirCUdeBDAuxiliar] Error en la busqueda de las entidades de ese tramite que tengan el cuadaptadaantigua a FALSE y iddeterminacion = 0. entidad:"+ idEntidadCU;
						log.error(errorString+ "\n" + ex.getMessage());
						listaErroresPersistir.add(errorString);
						ex.printStackTrace();
					}
					
					
					

					// -----------
					// Busco todas las entidades de ese tramite que tengan el cuadaptadaantigua a FALSE y iddeterminacion DISTINTO 0
					// -----------
					
					String queryEntidadCUAdaptadaNuevaIdDetDistintoCero = "select cuadapt from CUAdaptadaSipu cuadapt where cuadapt.idTramite = "+idTramite 
					+ " and cuadapt.idEntidad = "+idEntidadCU
					+ " and cuadapt.cuAdptadaAntigual is false and cuadapt.idDeterminacion != 0" +
							" order by  cuadapt.idDeterminacion ASC";


					try {
					
						List<CUAdaptadaSipu> cuAdaptFormaNuevaIdDetDistintoCeroLista = (List<CUAdaptadaSipu>) em.createQuery(queryEntidadCUAdaptadaNuevaIdDetDistintoCero).getResultList();
						log.debug("[persistirCUdeBDAuxiliar] Se han encontrado "+cuAdaptFormaNuevaIdDetDistintoCeroLista.size()+" registros de CUAdaptadaSipu para la entidad= "+idEntidadCU+" con cuAdptadaAntigual=false y iddeterminacion distinto 0");
						
						//  Por cada uno de los registros, tengo que buscar el idcaso persistido anteriormente y meterlo en idcasoaplicacion y pasarlo
						for (CUAdaptadaSipu cuAdaptFormaNuevaIdDetDistintoCero: cuAdaptFormaNuevaIdDetDistintoCeroLista)
						{
							// Busco el idCaso relacionado a ese registro que se ha persistido anteriormente
							// el idCaso debe ser de un registro con igual: idTramite, idEntidad, idusoacto, iddetusoacto, iddetvalorusoacto.  iddeterminacion=0
							
							String queryCUAdaptadaCaso = "select cuadapt from CUAdaptadaSipu cuadapt where cuadapt.idTramite = "+idTramite 
							+ " and cuadapt.idEntidad = "+cuAdaptFormaNuevaIdDetDistintoCero.getIdEntidad()
							+ " and cuadapt.idUsoActo = "+cuAdaptFormaNuevaIdDetDistintoCero.getIdUsoActo()
							+ " and cuadapt.idDetUsoActo = "+cuAdaptFormaNuevaIdDetDistintoCero.getIdDetUsoActo()
							+ " and cuadapt.idDetValorUsoActo = "+cuAdaptFormaNuevaIdDetDistintoCero.getIdDetValorUsoActo()
							+ " and cuadapt.idDeterminacion = 0";
							
							try {
								
								CUAdaptadaSipu cuAdaptParaCaso = (CUAdaptadaSipu) em.createQuery(queryCUAdaptadaCaso).getSingleResult();
								
								log.debug("[persistirCUdeBDAuxiliar] Para el registro con iden= "+cuAdaptFormaNuevaIdDetDistintoCero.getId()+ " el caso relacionado es el del registro con iden="+cuAdaptParaCaso.getId()+ " que tiene un idCaso="+cuAdaptParaCaso.getIdCaso());
								
								// Asignamo el idCaso en idCasoAplicacion
								cuAdaptFormaNuevaIdDetDistintoCero.setIdCasoAplicacion(cuAdaptParaCaso.getIdCaso());
								
								//TODO Adaptarlo y Pasarlo
								
								CondicionUrbanisticaSimplificadaDTO cuUso = new CondicionUrbanisticaSimplificadaDTO();
								
								// Adapto al modelos de cuAdaptFormaNuevaIdDetDistintoCero  para poder persistir
								// Las adaptaciones son las siguientes:
								// CondicionUrbanisticaSimplificadaDTO   <-- CUAdaptadaSipu
								// idEntidad						<--  idEntidad
								// nombreEntidad					<-- nombreEntidad
								// claveEntidad						<-- claveEntidad
								// idDeterminacion					<-- idDeterminacion
								// nombreDeterminacion				<-- nombreDeterminacion
								// valor							<-- valor
								// idDeterminacionValorReferencia	<-- idDeterminacionValorReferencia
								// nombreDetValorRef				<-- nombreDetValorRef
								// regimenesEspecificos				<-- regEspecificoDeterminacion
								
								cuUso.setIdEntidad(cuAdaptFormaNuevaIdDetDistintoCero.getIdEntidad());
								cuUso.setNombreEntidad(cuAdaptFormaNuevaIdDetDistintoCero.getNombreEntidad());
								cuUso.setClaveEntidad(cuAdaptFormaNuevaIdDetDistintoCero.getClaveEntidad());
								cuUso.setIdDeterminacion(cuAdaptFormaNuevaIdDetDistintoCero.getIdDeterminacion());
								cuUso.setNombreDeterminacion(cuAdaptFormaNuevaIdDetDistintoCero.getNombreDeterminacion());
								cuUso.setValor(cuAdaptFormaNuevaIdDetDistintoCero.getValor());
								cuUso.setIdDeterminacionValorReferencia(cuAdaptFormaNuevaIdDetDistintoCero.getIdDetValorReferencia());
								cuUso.setNombreDetValorRef(cuAdaptFormaNuevaIdDetDistintoCero.getNombreValorReferencia());
								
								cuUso.setIdCasoAplicacion(cuAdaptFormaNuevaIdDetDistintoCero.getIdCasoAplicacion());
								
								List<RegimenesEspecificosSimplificadosDTO> regEspList = new ArrayList <RegimenesEspecificosSimplificadosDTO> ();
								
								for (RegimenesEspecificosSimplificadosDeterminacion regEspDet : cuAdaptFormaNuevaIdDetDistintoCero.getRegEspecificoDeterminacion() )
								{
									RegimenesEspecificosSimplificadosDTO regEsp = new RegimenesEspecificosSimplificadosDTO();
									
									regEsp.setNombreRegimenEspecifico(regEspDet.getNombreRegimenEspecifico());
									regEsp.setTextoRegimenEspecifico(regEspDet.getTextoRegimenEspecifico());
									
									regEspList.add(regEsp);
								}
								
								cuUso.setRegimenesEspecificos(regEspList);
								
								// Ya tengo la transformacion hecha, la persisto
								
								int resultadoPersistir =  servicioBasicoCondicionesUrbanisticas.crearCU(cuUso);
								
								log.debug("[persistirCUdeBDAuxiliar] Se ha persistido la CU. El Iden resultadoPersistir="+resultadoPersistir);
								
								// Si el resultado es menor o igual que cero es porque se ha producido un error a la hora de persistir la CU
								if (resultadoPersistir<=0)
								{
									if (resultadoPersistir==0)
									{
										String errorPersitir= "FILA "+(cuAdaptFormaNuevaIdDetDistintoCero.getIdFilaExcel())+": Ha ocurrido un error al persistir la Condicion Urbanistica de la fila";
										listaErroresPersistir.add(errorPersitir);
										log.debug("[persistirCUdeBDAuxiliar]"+errorPersitir);

									}
									if (resultadoPersistir==-1)
									{
										String errorPersitir=("FILA "+(cuAdaptFormaNuevaIdDetDistintoCero.getIdFilaExcel())+": La entidad no tiene aun Grupo de Aplicacion aplicado y por tanto no se puede crear la CU. Ha ocurrido un error al persistir la Condicion Urbanistica de la fila");
										listaErroresPersistir.add(errorPersitir);
										log.debug("[persistirCUdeBDAuxiliar]"+errorPersitir);
									}
									if (resultadoPersistir==-2)
									{
										String errorPersitir=("FILA "+(cuAdaptFormaNuevaIdDetDistintoCero.getIdFilaExcel())+": No se ha encontrado en la determinacion a aplicar  que tenga como grupo a la determinacion de grupo aplicada a la entidad. Ha ocurrido un error al persistir la Condicion Urbanistica de la fila");
										listaErroresPersistir.add(errorPersitir);
										log.debug("[persistirCUdeBDAuxiliar]"+errorPersitir);
									}
									if (resultadoPersistir<=-3)
									{
										String errorPersitir=("FILA "+(cuAdaptFormaNuevaIdDetDistintoCero.getIdFilaExcel())+": Ha ocurrido un error al persistir la Condicion Urbanistica de la fila");
										listaErroresPersistir.add(errorPersitir);
										log.debug("[persistirCUdeBDAuxiliar]"+errorPersitir);
									}
										
								}
								else
								{
									// Se ha persistido correctamente, se devuelve el id de la CU
									// Tenemos que obtener el idRegimen y el idCaso porque van a ser necesarios 
									log.debug("[persistirCUdeBDAuxiliar] Se ha persistido la CU correctamente. El Iden resultadoPersistir="+resultadoPersistir);
									log.debug("[persistirCUdeBDAuxiliar] Tenemos que obtener el idRegimen y el idCaso porque van a ser necesarios ");
									log.debug("[persistirCUdeBDAuxiliar] idRegimen = El Iden resultadoPersistir="+resultadoPersistir);
									
									int idCasoPersitido = 0;
									
									String queryEntidaddeterminacionregimen = "select edr.casoentidaddeterminacionByIdcaso.iden from Entidaddeterminacionregimen edr where edr.iden = "+resultadoPersistir;
									
									idCasoPersitido = (Integer) em.createQuery(queryEntidaddeterminacionregimen).getSingleResult();
									log.debug("[persistirCUdeBDAuxiliar] idCasoPersitido = "+idCasoPersitido);
									
									// Guardo ambos en el registro  de cuAdaptFormaNuevaIdDetCero que le corresponde y hago merge
									cuAdaptFormaNuevaIdDetDistintoCero.setIdRegimen(resultadoPersistir);
									
									
									em.merge(cuAdaptFormaNuevaIdDetDistintoCero);
									log.debug("[persistirCUdeBDAuxiliar] Merge de cuAdaptFormaNuevaIdDetDistintoCero con  idRegimen y idCasoPersitido ");
									
									
								}
								
								
								
							}
							catch (NoResultException e) {
								
								String errorString = "[persistirCUdeBDAuxiliar] No se han encontrado registro para obtener el caso asociado a la determinacion de la fila= "+cuAdaptFormaNuevaIdDetDistintoCero.getIdFilaExcel() +" del EXCEL";
								log.error(errorString+ "\n" + e.getMessage());
								listaErroresPersistir.add(errorString);

							}
							catch (NonUniqueResultException e2) {
								
								String errorString = "[persistirCUdeBDAuxiliar] Se han encontrado m‡s de un registro para obtener el caso asociado a la determinacion de la fila= "+cuAdaptFormaNuevaIdDetDistintoCero.getIdFilaExcel() +" del EXCEL";
								log.error(errorString+ "\n" + e2.getMessage());
								listaErroresPersistir.add(errorString);

							}
							
							catch (Exception ex) {
											
								String errorString = "[persistirCUdeBDAuxiliar] Error en la busqueda del registro para obtener el caso asociado a la determinacion de la fila= "+cuAdaptFormaNuevaIdDetDistintoCero.getIdFilaExcel() +" del EXCEL";
								log.error(errorString+ "\n" + ex.getMessage());
								listaErroresPersistir.add(errorString);
								ex.printStackTrace();
							}
							
							
						}
					
					}
					catch (Exception ex) {
									
						String errorString = "[persistirCUdeBDAuxiliar] Error en la busqueda de las entidades de ese tramite que tengan el cuadaptadaantigua a FALSE y iddeterminacion distinto 0. entidad:"+ idEntidadCU;
						log.error(errorString+ "\n" + ex.getMessage());
						listaErroresPersistir.add(errorString);
						ex.printStackTrace();
					}

					
					
					
					
				}
				catch (Exception ex) {
								
					String errorString = "[persistirCUdeBDAuxiliar] Error en la busqueda de las entidades de ese tramite que tengan el cuadaptadaantigua a FALSE. entidad:"+ idEntidadCU;
					log.error(errorString+ "\n" + ex.getMessage());
					listaErroresPersistir.add(errorString);
					ex.printStackTrace();
				}
				
				
				
			}
			
		

		} catch (NoResultException e) {
			
			String errorString = "[persistirCUdeBDAuxiliar] No se han encontrado entidades para importar para el tramite:"+ idTramite;
			log.error(errorString+ "\n" + e.getMessage());
			listaErroresPersistir.add(errorString);

		} catch (Exception ex) {
						
			String errorString = "[persistirCUdeBDAuxiliar] Error en la busqueda de entidades para importar para el tramite:"+ idTramite;
			log.error(errorString+ "\n" + ex.getMessage());
			listaErroresPersistir.add(errorString);
			ex.printStackTrace();
		}
		
		
		// Se informa a traves de log si se han encontrado errores
		
		// ------------------------------------------------------------------
    	// Si ha habido algun error tengo que volver atras la transaccion
    	// ------------------------------------------------------------------
    	if (listaErroresPersistir.size()!=0)
    	{
    		contextoTransaccion.setRollbackOnly();
    		log.error("[persistirCUdeBDAuxiliar] Se han encontrado "+listaErroresPersistir.size() +" errores. No se persiste");
    		facesMessages.addFromResourceBundle(Severity.ERROR, "Error al crear las condiciones urbanísticas importadas", null);
    	}
		
		else
		{
			log.info("[persistirCUdeBDAuxiliar] No se ha encontrado ningun errores. Se persisten las CUs");
			
			//borrarElementosCuAdaptada(variablesSesionUsuario.getIdTramiteTrabajoActual());
			
			facesMessages.addFromResourceBundle("procesofinalizadook", null);
		}

    	listadoErroresImportacion = listaErroresPersistir;
		return listaErroresPersistir;
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
	
	
	public boolean isMostrarBotonImportar() {
		String query = "select cu from CUAdaptadaSipu cu where cu.idTramite=" + variablesSesionUsuario.getTramiteTrabajoActual().getIden();
		List<CUAdaptadaSipu> lista = em.createQuery(query).getResultList();
		if (lista!=null && lista.size()>0)
			return true;
		else
			return false;
	}


	public void setMostrarBotonImportar(boolean mostrarBotonImportar) {
		this.mostrarBotonImportar = mostrarBotonImportar;
	}


	@Remove
	public void destroy() {
	};
	
}

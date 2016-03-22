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

package com.mitc.redes.editorfip.servicios.gestionfip.importarfip;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.log.Log;
import org.jboss.seam.security.digest.DigestUtils;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.mitc.redes.editorfip.entidades.rpm.diccionario.Ambito;
import com.mitc.redes.editorfip.entidades.rpm.diccionario.Instrumentoplan;
import com.mitc.redes.editorfip.entidades.rpm.diccionario.Tipotramite;
import com.mitc.redes.editorfip.entidades.rpm.gestionfip.FipsCargados;
import com.mitc.redes.editorfip.entidades.rpm.gestionfip.PlanesBase;
import com.mitc.redes.editorfip.entidades.rpm.gestionfip.PlanesEncargados;
import com.mitc.redes.editorfip.entidades.rpm.gestionfip.PlanesVigente;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Entidad;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Entidadpol;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Plan;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Tramite;
import com.mitc.redes.editorfip.excepciones.EditorFIPException;
import com.mitc.redes.editorfip.servicios.basicos.diccionario.ServicioBasicoAmbitos;
import com.mitc.redes.editorfip.servicios.basicos.fip.determinaciones.ServicioBasicoDeterminaciones;
import com.mitc.redes.editorfip.servicios.basicos.fip.entidades.ServicioBasicoCondicionesUrbanisticas;
import com.mitc.redes.editorfip.servicios.gestionfip.generacionSLD.GenerarSLDdeTramites;
import com.mitc.redes.editorfip.servicios.gestionfip.obtencionfip.LogImportacion;
import com.mitc.redes.editorfip.servicios.mapas.ServicioCoordenadasAmbito;

@Stateless
@Name("importadorFIP")
public class ImportadorFIPBean implements ImportadorFIP {

	@Logger
	private Log log;

	@PersistenceContext
	EntityManager entityManager;

	@In(create = true, required = false)
	ServicioBasicoDeterminaciones servicioBasicoDeterminaciones;
	
	@In(create = true, required = false)
	LogImportacion logImportacion;
	
	@In(create = true, required = false)
	GenerarSLDdeTramites generarSLDdeTramites;
	


	/**
	 * IMPORTAR FICHERO FIP1
	 * 
	 * Importar desde un fichero el FIP1 a la base de datos
	 * 
	 */
	public String importar(String file, Long idFipCargado, int idPlanBase,	boolean planeamientoEncargado) throws EditorFIPException

	{
		Document xmlDoc = null;
		Resultado resultado = new Resultado();
		FipsCargados fipCargado;

		Plan catSist;
		Plan planVigente;
		Plan planEncargado;

		// Obtener el fip encargado seleccionado
		fipCargado = entityManager.find(FipsCargados.class, idFipCargado);

		resultado.info("----------------------------------------------------");
		resultado.info("  IMPORTANDO FIP DE TIPO 1 ");
		resultado.info("");
		resultado.info("    . Inicio importacion: " + new Date());
		resultado.info("----------------------------------------------------");
		resultado.info("");

		// Configurar Utiles
		Utiles.limpiar();
		Utiles.em = entityManager;

		// Cargar y abrir fiechero XML
		try {

			// Cargar fichero XML
			resultado.info("Leyendo fichero FIP1 (xml): " + file);

			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder docBuilder;
			docBuilder = docBuilderFactory.newDocumentBuilder();
			xmlDoc = docBuilder.parse(new File(file));
			xmlDoc.getDocumentElement().normalize();
			resultado.info("Leyendo fichero FIP1 (xml): OK");

			// Cambiar el estado a CONSOLIDANDO
			fipCargado.setEstado("IMPORTANDO");
			fipCargado.setFechaInicio(new Date());
			entityManager.merge(fipCargado);
			entityManager.flush();
			resultado.info("FIP cargado, estado a IMPORTANDO");

			// -----| DICCIONARIO |-----
			// log.debug("**********************************************");
			// log.debug("  DICCIONARIO ");
			// log.debug("**********************************************");
			// log importadorDiccionario = new ImportadorDiccionario(xmlDoc,
			// em);
			// log.importar();

			// -----| CATALOGO SISTEMATIZADO |-----

			log.info("");
			log.info("----------------------------------------------------");
			log.info("  IMPORTANDO CATÁLOGO SISTEMATIZADO ");
			log.info("----------------------------------------------------");
			log.info("");

			if (idPlanBase != 0) {
				resultado
						.info("No Importando Catalogo Sistematizado del XML puesto que lo voy a asociar con uno del mismo ambito ya existente en la BD");
				log.info("No Importando Catalogo Sistematizado del XML puesto que lo voy a asociar con uno del mismo ambito ya existente en la BD");

				catSist = entityManager.find(Plan.class, idPlanBase);

			} else {
				resultado.info("Importando Catalogo Sistematizado del XML....");
				log.info("Importando Catalogo Sistematizado del XML....");

				catSist = importarCatalogoSistematizado(xmlDoc, resultado);
				
				// Tengo que crear un nuevo registro en gestionfip.planbase para incluir este nuevo plan base
				PlanesBase pb = new PlanesBase();
				pb.setPlan(catSist);
				// Busco el tramite base para asociarlo a partir del planBase
				String queryTramiteBase = " select tram from Tramite tram where tram.plan.iden ="+catSist.getIden();
				
				List<Tramite> tramBaseList = (List<Tramite>) entityManager.createQuery(queryTramiteBase).getResultList();
				
				// Asocio el primero que encuentro			
				pb.setTramite(tramBaseList.get(0));
				
				// Persisto el registro
				entityManager.persist(pb);
				
			}

			// -----| PLAN VIGENTE |-----

			log.info("");
			log.info("----------------------------------------------------");
			log.info("  IMPORTANDO PLAN VIGENTE ");
			log.info("----------------------------------------------------");
			log.info("");
			resultado.info("Importando Plan Vigente ....");

			planVigente = importarPlanVigente(xmlDoc, resultado,catSist);
			
			// Tengo que crear un nuevo registro en planVigente para incluir este nuevo plan base
			PlanesVigente pv = new PlanesVigente();
			pv.setPlan(planVigente);
			// Busco el tramite base para asociarlo a partir del planBase
			String queryTramiteVigente = " select tram from Tramite tram where tram.plan.iden ="+planVigente.getIden();
			
			List<Tramite> tramVigenteList = (List<Tramite>) entityManager.createQuery(queryTramiteVigente).getResultList();
			
			// Asocio el primero que encuentro			
			pv.setTramite(tramVigenteList.get(0));
			
			// Persisto el registro
			entityManager.persist(pv);
			
			// Hago un flush porque me va a hacer falta antes de entrar en el plan encargado
			entityManager.flush();
			
			// Ahora creamos los SLDs para el tr‡mite del plan vigente
		    log.info("Creando SLDs para el Plan Vigente ...");
		    //logImportacion.anadirLinea("Creando SLDs para el Plan Vigente ...");
		    
		    if (generarSLDdeTramites!=null)
		    {
		    	generarSLDdeTramites.generarSLD(pv.getTramite().getIden(), planVigente.getIdambito());
		    }		    	
		    else
		    {
		    	resultado.error("Error generarSLDdeTramites=null ");
				log.error("Error generarSLDdeTramites=null ");
				throw new EditorFIPException("Error generarSLDdeTramites=null ");
		    }
		    
		  
		    
		    

			// -----| PLAN ENCARGADO |-----
			log.info("");
			log.info("----------------------------------------------------");
			log.info("  IMPORTANDO PLAN ENCARGADO ");
			log.info("----------------------------------------------------");
			log.info("");

			if (planeamientoEncargado) {
				resultado
						.info("Existe Plan Encargado en el FIP.xml y lo paso a importar....");
				log.info("Existe Plan Encargado en el FIP.xml y lo paso a importar....");

				int idPlaneamientoEncargado = importarPlaneamientoEncargado(xmlDoc,resultado, catSist, planVigente);
				// Lo guardo en FIPCargado para tener siempre la relacion de que FIP Cargado se ha importado contra que PlaneamientoEncargado
				fipCargado.setIdplaneamientoencargado(idPlaneamientoEncargado);
				entityManager.merge(fipCargado);
				entityManager.flush();
				
				
			} else {
				resultado
						.info("No Existe Plan Encargado en el FIP.xml. Se creara uno nuevo ficticio para ese ambito y plan vigente....");
				log.info("No Existe Plan Encargado en el FIP.xml. Se creara uno nuevo ficticio para ese ambito y plan vigente....");

				planEncargado = crearPlaneamientoEncargado(resultado, catSist,planVigente);
			}
			
			
			



			resultado.info("");
			resultado.info("----------------------------------------------------------");
			resultado.info("  FIN IMPORTACION FIP DE TIPO 1 ");
			resultado.info("");
			resultado.info("    . Fin importacion: " + new Date());
			// resultado.info("    . Numero de operaciones SQL: " +
			// resultado.countSQL);
			resultado.info("----------------------------------------------------------");
			resultado.info("");
			resultado.log();

		} catch (ParserConfigurationException e) {
			fipCargado.setEstado("ERRORES");
			resultado.error("Error en configuración para parsear XML: "	+ e.getMessage());
			e.printStackTrace();
			throw new EditorFIPException("ParserConfigurationException: "+ e.getMessage());
		} catch (SAXException e) {
			fipCargado.setEstado("ERRORES");
			resultado.error("Error al parsear el fichero XML: "	+ e.getMessage());
			e.printStackTrace();
			throw new EditorFIPException("SAXException: " + e.getMessage());
		} catch (IOException e) {
			fipCargado.setEstado("ERRORES");
			resultado.error("Error al abrir el fichero de XML " + file + ": "+ e.getMessage());
			e.printStackTrace();
			throw new EditorFIPException("IOException: " + e.getMessage());
		} catch (Exception e) {
			fipCargado.setEstado("ERRORES");
			resultado.error("Error desconocido, error: " + e.getMessage());
			e.printStackTrace();
			throw new EditorFIPException("Exception: " + e.getMessage());
		}

		// Enseñar resultado
		return resultado.toString();
	}

	/*
	 * IMPORTAR PlaneamientoEncargado
	 */
	private int importarPlaneamientoEncargado(Document xmlDoc,	Resultado resultado, Plan planBaseOrig, Plan planVigenteOrig)
			throws EditorFIPException {
		
		int idenPlaneamientoEncargadoDevolver = 0;
		
		Plan planeamientoEncargado = null;
		int iteracion, codTipoTramite, codAmbito, codInstrumento;
		Tipotramite tipoTramite, tipoTramiteFind;
		Ambito ambito, ambitoFind;
		Instrumentoplan instrumento, instrumentoFind;
		String proyeccion = "EPSG:23030";
		String nombre, codigoTramite;

		// Cargar y abrir fiechero XML
		try {
			
			Plan planBase = entityManager.find(Plan.class, planBaseOrig.getIden());
			Plan planVigente = entityManager.find(Plan.class, planVigenteOrig.getIden());

			// Coger datos del trámite y configurar Importador resultado e
			// iniciar transacción

			ImportadorBase impBase = new ImportadorBase(xmlDoc, null);

			log.info("Buscando en el xml nodo FIP ...");
			NodeList nodeFIP = impBase.evaluateXPath("//FIP");
			// Si tenemos fip ...
			if (nodeFIP.getLength() == 0) {

				log.info("No hay FIP en el fichero XML.");

			} else {

				// Obtener atributos
				Map<String, Object> attrs = impBase.nodeToMap(nodeFIP.item(0));

				proyeccion = (String) attrs.get("SRS");
				log.info("proyeccion=" + (String) attrs.get("SRS"));

			}

			log.info("Buscando en el xml nodo //PLANEAMIENTO-ENCARGADO ...");
			NodeList nodes = impBase.evaluateXPath("//PLANEAMIENTO-ENCARGADO");
			log.info("Nodos encontrados: " + nodes.getLength());

			// Si tenemos algún plan encargado ...
			if (nodes.getLength() == 0) {

				log.info("No hay plan encargado en el fichero XML.");

			} else {

				// Obtener atributos
				Map<String, Object> attrs = impBase.nodeToMap(nodes.item(0));
				codAmbito = Integer.parseInt((String) attrs.get("ambito"));
				nombre = (String) attrs.get("nombre");
				codInstrumento = Integer.parseInt((String) attrs
						.get("instrumento"));
				iteracion = Integer.parseInt((String) attrs.get("iteracion"));
				codTipoTramite = Integer.parseInt((String) attrs
						.get("tipoTramite"));
				codigoTramite = (String) attrs.get("codigoTramite");

				// Obtener ambito
				ambito = entityManager.find(Ambito.class, codAmbito);
				if (ambito == null)
					throw new EditorFIPException(
							"Plan Encargado: Ambito no encontrado, código: "
									+ codAmbito);
				log.info("Ambito, iden: " + ambito.getIden() + " INE: "
						+ ambito.getCodigoine());

				// Obtener tipo trámite
				tipoTramite = entityManager.find(Tipotramite.class,
						codTipoTramite);
				if (tipoTramite == null)
					throw new EditorFIPException(
							"Plan Encargado: Tipo tramite no encontrado, código: "
									+ codTipoTramite);
				log.info("Tipo tramite, iden: " + tipoTramite.getIden());

				// Obtener instrumento
				instrumento = entityManager.find(Instrumentoplan.class,	codInstrumento);
				if (instrumento == null)
				{
					log.warn("Plan Encargado: Instrumento no encontrado en el diccionario, código: "+ codInstrumento+" le asigno Plan General (8)");
					instrumento = entityManager.find(Instrumentoplan.class,	8);
				}
					
				log.info("Instrumento, iden: " + instrumento.getIden());

				//
				// P L A N E N C A R G A D O - - - - - - - - - - - - - -
				//

				// Calcular código
				String codigo = Utiles.calcularCodigoPlan(codAmbito);
				log.info("Nuevo codigo para el plan encargado (cod. ambigo: "
						+ codAmbito + "): " + codigo);

				// Crear Plan
				Plan plan = new Plan();
				plan.setCodigo(codigo);
				plan.setNombre(nombre);
				plan.setIdambito(ambito.getIden());
				plan.setPlanByIdplanbase(planBase);
				plan.setPlanByIdpadre(planVigente);
				

				// Crear Trámite
				Tramite tramite = new Tramite();
				tramite.setPlan(plan);
				tramite.setFecha(new Date());
				tramite.setCodigofip(codigoTramite);
				tramite.setNombre(nombre);
				tramite.setIdtipotramite(tipoTramite.getIden());

				// Crear Plan Encargado
				PlanesEncargados planEncargado = new PlanesEncargados();
				planEncargado.setAmbito(ambito);
				// planEncargado.setAmbitoaplicacion(ambitoaplicacion);
				planEncargado.setCodigoFip(codigoTramite);
				planEncargado.setFechaInicio(new Date());
				planEncargado.setInstrumento(instrumento);
				planEncargado.setIteracion(iteracion);
				planEncargado.setNombre(nombre);
				planEncargado.setPlanEncargado(plan);
				planEncargado.setTipotramite(tipoTramite);
				planEncargado.setTramiteEncargado(tramite);
				
				
				// Busco el tramite base para asociarlo a partir del planBase
				String queryTramiteBase = " select tram from Tramite tram where tram.plan.iden ="+planBase.getIden();
				
				List<Tramite> tramBaseList = (List<Tramite>) entityManager.createQuery(queryTramiteBase).getResultList();
				
				// Asocio el primero que encuentro			
				planEncargado.setTramiteBase(tramBaseList.get(0));
				
				// Busco el tramite vigente para asociarlo a partir del planVigente
				String queryTramiteVigente = " select tram from Tramite tram where tram.plan.iden ="+planVigente.getIden();
				
				List<Tramite> tramVigenteList = (List<Tramite>) entityManager.createQuery(queryTramiteVigente).getResultList();
				
				// Asocio el primero que encuentro			
				planEncargado.setTramiteVigente(tramVigenteList.get(0));
				

				planEncargado.setProyeccion(proyeccion);

				//
				// Persistir
				//
				log.info("Persistiendo ... plan, tramite, planEncargado, planBase, planVigente");
				entityManager.persist(plan);
				entityManager.persist(tramite);
				entityManager.persist(planEncargado);

				entityManager.flush();
				
				// El ID es el que tengo que devolver
				idenPlaneamientoEncargadoDevolver = planEncargado.getId().intValue();
				
				// SLDs para el plan encargado
			    log.info("Creando SLDs para el Plan Encargado ...");
			    //logImportacion.anadirLinea("Creando SLDs para el Plan Encargado ...");
			    generarSLDdeTramites.generarSLD(tramite.getIden(), plan.getIdambito());

				log.info("Plan encargado y demas persistido.");

				// Creamos las determinaciones necesarias de unidades
				log.info("Creamos las determinaciones necesarias de unidades del Plan Encargado.");
				servicioBasicoDeterminaciones
						.crearDeterminacionesUnidades(planEncargado);
			}

		} catch (EditorFIPException e1) {
			resultado.error(e1.getMessage());
			throw new EditorFIPException("Error desconocido, error: "
					+ e1.getMessage());
		} catch (Exception e) {
			resultado.error("Error desconocido, error: " + e.getMessage());
			e.printStackTrace();
			throw new EditorFIPException("Error desconocido, error: "
					+ e.getMessage());
		}

		return idenPlaneamientoEncargadoDevolver;

	}

	/*
	 * Funcion para cuando en el FIP.xml no viene planeamiento encargado
	 */
	private Plan crearPlaneamientoEncargado(Resultado resultado, Plan planBaseOrig,Plan planVigenteOrig) throws EditorFIPException {

		Plan planeamientoEncargado = null;
		int iteracion, codTipoTramite, codAmbito, codInstrumento;
		Tipotramite tipoTramite, tipoTramiteFind;
		Ambito ambito;
		Instrumentoplan instrumento, instrumentoFind;
		String proyeccion = "EPSG:23030";
		String nombre, codigoTramite;

		// Cargar y abrir fiechero XML
		try {

			Plan planBase = entityManager.find(Plan.class, planBaseOrig.getIden());
			Plan planVigente = entityManager.find(Plan.class, planVigenteOrig.getIden());
			
			int idAmbito = planVigente.getIdambito();

			ambito = entityManager.find(Ambito.class, idAmbito);

			nombre = "Plan Encargado Automatico para el ambito " + idAmbito;
			codInstrumento = 1;
			iteracion = 0;
			codTipoTramite = 4;
			codigoTramite = DigestUtils.md5Hex(nombre + new Date());

			// Obtener tipo trámite
			tipoTramite = entityManager.find(Tipotramite.class, codTipoTramite);
			if (tipoTramite == null)
				throw new EditorFIPException(
						"Plan Encargado: Tipo tramite no encontrado, código: "
								+ codTipoTramite);
			log.info("Tipo tramite, iden: " + tipoTramite.getIden());

			// Obtener instrumento
			instrumento = entityManager.find(Instrumentoplan.class,
					codInstrumento);
			if (instrumento == null)
				throw new EditorFIPException(
						"Plan Encargado: Instrumento no encontrado, código: "
								+ codInstrumento);
			log.info("Instrumento, iden: " + instrumento.getIden());

			//
			// P L A N E N C A R G A D O - - - - - - - - - - - - - -
			//

			// Calcular código
			String codigo = Utiles.calcularCodigoPlan(idAmbito);

			// Crear Plan
			Plan plan = new Plan();
			plan.setCodigo(codigo);
			plan.setNombre(nombre);
			plan.setIdambito(ambito.getIden());
			plan.setPlanByIdplanbase(planBase);
			plan.setPlanByIdpadre(planVigente);

			// Crear Trámite
			Tramite tramite = new Tramite();
			tramite.setPlan(plan);
			tramite.setFecha(new Date());
			tramite.setCodigofip(codigoTramite);
			tramite.setNombre(nombre);
			tramite.setIdtipotramite(tipoTramite.getIden());

			// Crear Plan Encargado
			PlanesEncargados planEncargado = new PlanesEncargados();
			planEncargado.setAmbito(ambito);
			// planEncargado.setAmbitoaplicacion(ambitoaplicacion);
			planEncargado.setCodigoFip(codigoTramite);
			planEncargado.setFechaInicio(new Date());
			planEncargado.setInstrumento(instrumento);
			planEncargado.setIteracion(iteracion);
			planEncargado.setNombre(nombre);
			planEncargado.setPlanEncargado(plan);
			planEncargado.setTipotramite(tipoTramite);
			planEncargado.setTramiteEncargado(tramite);
			
			// Busco el tramite base para asociarlo a partir del planBase
			String queryTramiteBase = " select tram from Tramite tram where tram.plan.iden ="+planBase.getIden();
			
			List<Tramite> tramBaseList = (List<Tramite>) entityManager.createQuery(queryTramiteBase).getResultList();
			
			// Asocio el primero que encuentro			
			planEncargado.setTramiteBase(tramBaseList.get(0));
			
			// Busco el tramite vigente para asociarlo a partir del planVigente
			String queryTramiteVigente = " select tram from Tramite tram where tram.plan.iden ="+planVigente.getIden();
			
			List<Tramite> tramVigenteList = (List<Tramite>) entityManager.createQuery(queryTramiteVigente).getResultList();
			
			// Asocio el primero que encuentro			
			planEncargado.setTramiteVigente(tramVigenteList.get(0));

			planEncargado.setProyeccion(proyeccion);
			

		    


			//
			// Persistir
			//
			log.info("Persistiendo ... plan, tramite, planEncargado, planBase, planVigente");
			entityManager.persist(plan);
			entityManager.persist(tramite);
			entityManager.persist(planEncargado);

			entityManager.flush();
			

		    // SLDs para el plan encargado
		    log.info("Creando SLDs para el Plan Encargado ...");
		    logImportacion.anadirLinea("Creando SLDs para el Plan Encargado ...");
		    generarSLDdeTramites.generarSLD(tramite.getIden(), plan.getIdambito());

			log.info("Plan encargado y demas persistido.");

			// Creamos las determinaciones necesarias de unidades
			log.info("Creamos las determinaciones necesarias de unidades del Plan Encargado.");
			servicioBasicoDeterminaciones
					.crearDeterminacionesUnidades(planEncargado);

		} catch (EditorFIPException e1) {
			resultado.error(e1.getMessage());
			throw new EditorFIPException("Error desconocido, error: "
					+ e1.getMessage());
		} catch (Exception e) {
			resultado.error("Error desconocido, error: " + e.getMessage());
			e.printStackTrace();
			throw new EditorFIPException("Error desconocido, error: "
					+ e.getMessage());
		}

		return planeamientoEncargado;

	}
	
	

	/*
	 * IMPORTAR CATALOGO SISTEMATIZADO
	 */
	private Plan importarCatalogoSistematizado(Document xmlDoc,	Resultado resultado) throws EditorFIPException {
		
		Plan planBase;

		ImportadorBase impBase = new ImportadorBase(xmlDoc, null);

		// Coger datos del trámite
		NodeList nodes = impBase.evaluateXPath("//CATALOGO-SISTEMATIZADO");

		// Si tenemos plan vigente
		if (nodes.getLength() > 0) {

			// Mapeamos atributos del nodo
			Map<String, Object> attrMap = impBase.nodeToMap(nodes.item(0));
			int idAmbito = Integer.parseInt((String) attrMap.get("ambito"));

			// Necesitamos el código
			String codigo = Utiles.calcularCodigoPlan(idAmbito);

			// Creamos Plan y persistimos
			planBase = new Plan();
			planBase.setNombre((String) attrMap.get("nombre"));
			planBase.setIdambito(idAmbito);
			planBase.setCodigo(codigo);
			log.info("Persistiendo CATALOGO-SISTEMATIZADO ...");
			entityManager.persist(planBase);

			// Añadir trámite
			ImportadorTramite impTramite = new ImportadorTramite(xmlDoc,
					entityManager, resultado,logImportacion);
			//impTramite.xmlPath = "//CATALOGO-SISTEMATIZADO/TRAMITE";
			//impTramite.plan = planBase;
			impTramite.importar(new Date(), "Plan Base del ambito", planBase,"//CATALOGO-SISTEMATIZADO/TRAMITE");
			entityManager.flush();

			
		}
		else
		{
			log.error("No existe CATALOGO-SISTEMATIZADO en el FIP.xml Cargado");
			resultado.error("No existe CATALOGO-SISTEMATIZADO en el FIP.xml Cargado");
			
			throw new EditorFIPException("No existe CATALOGO-SISTEMATIZADO en el FIP.xml Cargado");
		}
		
		return planBase;
	}

	/*
	 * IMPORTAR PLAN VIGENTE
	 */
	private Plan importarPlanVigente(Document xmlDoc, Resultado resultado, Plan planBase)
			throws EditorFIPException {

		ImportadorBase impBase = new ImportadorBase(xmlDoc, null);
		Plan planVigente;

		String fechaPlanVigente = "";
		Date fechaPV = new Date();
		SimpleDateFormat formatoDelTexto = new SimpleDateFormat("yyyy-MM-dd");

		log.info("Buscando en el xml nodo FIP ...");
		NodeList nodeFIP = impBase.evaluateXPath("//FIP");
		// Si tenemos fip ...
		if (nodeFIP.getLength() == 0) {

			log.info("No hay FIP en el fichero XML.");

		} else {

			// Obtener atributos
			Map<String, Object> attrs = impBase.nodeToMap(nodeFIP.item(0));

			fechaPlanVigente = (String) attrs.get("FECHA");
			log.info("fechaPlanVigente=" + fechaPlanVigente);

			try {
				fechaPV = formatoDelTexto.parse(fechaPlanVigente);
			} catch (java.text.ParseException e) {

				e.printStackTrace();
			}

		}

		// Coger datos del trámite
		NodeList nodes = impBase.evaluateXPath("//PLANEAMIENTO-VIGENTE");

		// Si tenemos plan vigente
		if (nodes.getLength() > 0) {

			// Mapeamos atributos del nodo
			Map<String, Object> attrMap = impBase.nodeToMap(nodes.item(0));
			int idAmbito = Integer.parseInt((String) attrMap.get("ambito"));

			// Necesitamos el código
			String codigo = Utiles.calcularCodigoPlan(idAmbito);

			// Creamos Plan y persistimos
			planVigente = new Plan();
			planVigente.setNombre((String) attrMap.get("nombre"));
			planVigente.setIdambito(idAmbito);
			planVigente.setCodigo(codigo);
			planVigente.setPlanByIdplanbase(planBase);

			log.info("Persistiendo PLAN VIGENTE ...");
			entityManager.persist(planVigente);

			// Añadir trámite
			ImportadorTramite impTramite = new ImportadorTramite(xmlDoc,
					entityManager, resultado,logImportacion);
			//impTramite.xmlPath = "//PLANEAMIENTO-VIGENTE/TRAMITE";
			//impTramite.plan = planVigente;
			impTramite.importar(fechaPV,"Plan Vigente de " + fechaPV.toString(), planVigente,"//PLANEAMIENTO-VIGENTE/TRAMITE");
			entityManager.flush();

			
		}
		else
		{
			/* FGA Lo comento, pero no va a ser un error (antes si lo era) 
			log.error("No existe PLANEAMIENTO-VIGENTE en el FIP.xml Cargado");
			resultado.error("No existe PLANEAMIENTO-VIGENTE en el FIP.xml Cargado");
			
			throw new EditorFIPException("No existe PLANEAMIENTO-VIGENTE en el FIP.xml Cargado");
			*/
			
			log.info("No existe PLANEAMIENTO-VIGENTE en el FIP.xml Cargado. Creo un PLANEAMIENTO-VIGENTE VACIO");
			
			/*
			// Si tenemos fip ...
			if (nodeFIP.getLength() == 0) {

				log.info("No hay FIP en el fichero XML.");

			} else {

				// Obtener atributos
				Map<String, Object> attrs = impBase.nodeToMap(nodeFIP.item(0));

				proyeccion = (String) attrs.get("SRS");
				log.info("proyeccion=" + (String) attrs.get("SRS"));

			}
			*/

			log.info("Buscando en el xml nodo //PLANEAMIENTO-ENCARGADO ...");
			NodeList nodesPE = impBase.evaluateXPath("//PLANEAMIENTO-ENCARGADO");
			log.info("Nodos encontrados: " + nodes.getLength());

			// Si tenemos algún plan encargado ...
			if (nodesPE.getLength() == 0) {

				log.info("No hay plan encargado en el fichero XML.");
				
				log.error("No existe PLANEAMIENTO-VIGENTE ni PLANEAMIENTO-ENCARGADO en el FIP.xml Cargado");
				resultado.error("No existe PLANEAMIENTO-VIGENTE ni PLANEAMIENTO-ENCARGADO en el FIP.xml Cargado");
				
				throw new EditorFIPException("No existe PLANEAMIENTO-VIGENTE ni PLANEAMIENTO-ENCARGADO en el FIP.xml Cargado");

			} else {

				// Obtener atributos
				Map<String, Object> attrs = impBase.nodeToMap(nodesPE.item(0));
				int codAmbito = Integer.parseInt((String) attrs.get("ambito"));
				
				int codInstrumento = 8; // Plan General
				
				int codTipoTramite = 11; // Refundido
				

				// Obtener ambito
				Ambito ambito = entityManager.find(Ambito.class, codAmbito);
				if (ambito == null)
					throw new EditorFIPException("Plan Encargado: Ambito no encontrado, código: "+ codAmbito);
				log.info("Ambito, iden: " + ambito.getIden() + " INE: "+ ambito.getCodigoine());
				
				String codigoTramite = codigoTramite = DigestUtils.md5Hex(ambito.toString() + new Date());;

				// Obtener tipo trámite
				Tipotramite tipoTramite = entityManager.find(Tipotramite.class,	codTipoTramite);
				if (tipoTramite == null)
					throw new EditorFIPException("Plan Encargado: Tipo tramite no encontrado, código: "+ codTipoTramite);
				log.info("Tipo tramite, iden: " + tipoTramite.getIden());

				// Obtener instrumento
				Instrumentoplan instrumento = entityManager.find(Instrumentoplan.class,	codInstrumento);
				if (instrumento == null)
					throw new EditorFIPException("Plan Encargado: Instrumento no encontrado, código: "+ codInstrumento);
				log.info("Instrumento, iden: " + instrumento.getIden());

				//
				// P L A N vigente - - - - - - - - - - - - - -
				//

				// Calcular código
				String codigo = Utiles.calcularCodigoPlan(codAmbito);
				log.info("Nuevo codigo para el plan vigente (cod. ambigo: "+ codAmbito + "): " + codigo);

				// Creamos Plan y persistimos
				planVigente = new Plan();
				planVigente.setNombre("Plan Vigente Vacio");
				planVigente.setIdambito(ambito.getIden());
				planVigente.setCodigo(codigo);
				planVigente.setPlanByIdplanbase(planBase);
				
				

				// Crear Trámite
				Tramite tramite = new Tramite();
				tramite.setPlan(planVigente);
				tramite.setFecha(new Date());
				tramite.setCodigofip(codigoTramite);
				tramite.setNombre("Plan Vigente Vacio");
				tramite.setIdtipotramite(tipoTramite.getIden());

				
				

				//
				// Persistir
				//
				log.info("Persistiendo ... plan, tramite, planEncargado, planBase, planVigente");
				entityManager.persist(planVigente);
				entityManager.persist(tramite);
				//entityManager.persist(planEncargado);

				entityManager.flush();
			}
			
		}

		return planVigente;
	}

}

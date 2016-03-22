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

package com.mitc.redes.editorfip.servicios.procesamientofip.generacionfip2;

import java.io.File;
import java.io.FileWriter;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.ejb.Stateless;
import javax.faces.event.ActionEvent;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.xml.parsers.DocumentBuilderFactory;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.async.Asynchronous;
import org.jboss.seam.faces.FacesManager;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.international.StatusMessage.Severity;
import org.jboss.seam.log.Log;
import org.jboss.seam.security.Credentials;
import org.jboss.seam.transaction.Transaction;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.mitc.redes.editorfip.entidades.fipxml.PropiedadesAdscripcion;
import com.mitc.redes.editorfip.entidades.fipxml.PropiedadesUnidad;
import com.mitc.redes.editorfip.entidades.fipxml.RegulacionEspecifica;
import com.mitc.redes.editorfip.entidades.rpm.gestionfip.FipsGenerados;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Casoentidaddeterminacion;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Determinacion;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Determinaciongrupoentidad;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Documento;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Documentocaso;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Documentodeterminacion;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Documentoentidad;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Documentoshp;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Entidad;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Entidaddeterminacion;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Entidaddeterminacionregimen;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Entidadlin;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Entidadpnt;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Entidadpol;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Opciondeterminacion;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Operacion;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Operaciondeterminacion;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Operacionentidad;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Operacionrelacion;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Regimenespecifico;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Tramite;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Vinculocaso;
import com.mitc.redes.editorfip.servicios.basicos.comunes.ServicioBasicoGeneral;
import com.mitc.redes.editorfip.servicios.genericos.ArbolGenericoObject;
import com.mitc.redes.editorfip.servicios.gestionfip.gestionprerefundido.ServicioBasicoPrerefundido;
import com.mitc.redes.editorfip.utilidades.Textos;
import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;

@Stateless
@Name("conversionFIPXML")
public class ConversionFIPXMLBean implements ConversionFIPXML {

	@PersistenceContext
	EntityManager em;

	@In(create = true)
	FacesMessages facesMessages;

	@Logger
	private Log log;
	
	@In (create = true)
	ServicioBasicoPrerefundido servicioBasicoPrerefundido;

	@In
	Credentials credentials;

	private static final String PROPERTIES = "diccionario";

	public String nombreAmbitoDelTramite(int idTramite) {
		String nombreAmbito = "NoEncontrado";

		String queryNombreAmbito = "SELECT tra.texto "
				+ " FROM Tramite tram, "
				+ " Plan pl, "
				+ " Ambito amb, "
				+ " Literal lit, "
				+ " Traduccion tra "
				+ " WHERE tram.iden="
				+ idTramite
				+ " AND pl.iden = tram.plan.iden AND amb.iden = pl.idambito AND lit.iden = amb.literal.iden AND tra.literal.iden = lit.iden";

		try {

			Query qPlan = em.createQuery(queryNombreAmbito);

			nombreAmbito = (String) qPlan.getSingleResult();

		} catch (Exception ex) {
			ex.printStackTrace();
			log.warn("[nombreAmbitoDelTramite] Error al obtener el nombre del ambito para el idTramite:"
					+ idTramite);
		}
 
		return nombreAmbito;
	}

	/*
	 * 	GENERAR FIP
	 * 
	 * (non-Javadoc)
	 * @see com.mitc.redes.editorfip.servicios.procesamientofip.generacionfip2.ConversionFIPXML#generarFip(java.util.List)
	 */
	@Asynchronous
	public void generarFip(List<ArbolGenericoObject> seleccionados) {

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try {
			
			// Creamos un nuevo elemento e iteramos sobre los ids seleccionados
			// Element raizTram = document.createElement("tramites"); // creamos
			// el elemento raiz

			Iterator it = seleccionados.iterator();
			while (it.hasNext()) {

				ArbolGenericoObject obj = (ArbolGenericoObject) it.next();

				// Guardamos los objetos FIP
				// FipsGenerados fip = new FipsGenerados();
				FipsGenerados fip = guardarFIPenBD(obj.getDatosIdTextoArbolGenerico().getIdBaseDatos(), obj);

				//em.persist(fip);
				//em.flush();

			}

		} catch (Exception e) {
			e.printStackTrace();
			facesMessages.addFromResourceBundle(Severity.ERROR, "generacionFip2Error", null);
			System.err.println("Error: " + e);
		}		
	}
	
	public void postGenerarFip(ActionEvent ae) {
		
		facesMessages.addFromResourceBundle(Severity.INFO,"Se esta generando el FIP2.xml. Actualice la pagina en unos minutos.", null);
		FacesManager.instance().redirect("/gestionfip/generacionfip2/Fip2GeneradosList.xhtml");
	}

	
	public FipsGenerados guardarFIPenBD(int idTramite) {
		return guardarFIPenBD(idTramite, null);
	}
	
	@Asynchronous
	public void crearFIP2Asincrono(int idTramite, FipsGenerados fipGenerado) {
		log.info("    Trámite para crear FIP2: iden=" + idTramite);
		
		String os = System.getProperty("os.name").toLowerCase();
		

		try {
			String nombreAmbito = nombreAmbitoDelTramite(idTramite);
			
			String dir = System.getProperty("jboss.home.dir") + File.separator
			+ "var" + File.separator + "FIPs.war" + File.separator
			+ "refundido" + File.separator + idTramite + File.separator;
			
			File directorio = new File(dir);
			directorio.mkdirs();
			log.info(" Se crearan en: " + dir);
			
			
			String fileDir = fipGenerado.getXml();
			
			
			String texto = "";
			Tramite iTramite = null;
			iTramite = findComplete(idTramite);
			log.info("    Tramite completo cargado en memoria ");
			
			File guardar = new File(fileDir);
			if (guardar != null) {
				FileWriter guardx = new FileWriter(guardar);
				log.info("    Generando FIP...");
				Document documento = FIP2(iTramite);

				log.info("    Formateando FIP...");
				OutputFormat format = new OutputFormat(documento);
				format.setLineWidth(65);
				format.setIndenting(true);
				format.setIndent(5);

				if (os.indexOf("win") >= 0) {
					format.setEncoding("ISO-8859-1");
				} else {
					format.setEncoding("UTF-8");
				}

				StringWriter writer = new StringWriter();
				XMLSerializer serializer = new XMLSerializer(writer, format);
				serializer.serialize(documento);
				texto = writer.toString();

				log.info("    Guardando FIP en disco...");
				guardx.write(texto);
				guardx.close();
				log.info("    Archivo FIP guardado");

			}
			
			// Cambiar estado a CREADO
			
			//log.info("Obteniendo FIP2 ...");
			//fip = em.find(FipsGenerados.class, fip.getId());
			log.info("Cambiando FIP2 a estado: CREADO");
			fipGenerado.setEstado("CREADO");
			em.merge(fipGenerado);
			em.flush();
			try {
				Transaction.instance().commit();
				Transaction.instance().begin();
				em.joinTransaction();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
			
			log.error("ERRORES creando el FIP2. Cambiando FIP2 a estado: ERRORES");
			fipGenerado.setEstado("ERRORES");
			
			em.merge(fipGenerado);
			em.flush();
			try {
				Transaction.instance().commit();
				Transaction.instance().begin();
				em.joinTransaction();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}


	}
	
	public FipsGenerados guardarFIPenBD(int idTramite, ArbolGenericoObject obj) {
		log.info("    Trámite refundido: iden=" + idTramite);
		
		String os = System.getProperty("os.name").toLowerCase();
		String nombreAmbito = nombreAmbitoDelTramite(idTramite);
		
		String nombreArchivo = "fip"  + "_" + idTramite + "_" + new Date().getTime() + ".xml";
		
		String dir = System.getProperty("jboss.home.dir") + File.separator
				+ "var" + File.separator + "FIPs.war" + File.separator
				+ "refundido" + File.separator + nombreAmbito + File.separator;
		String fileDir = System.getProperty("jboss.home.dir") + File.separator
				+ "var" + File.separator + "FIPs.war" + File.separator
				+ "refundido" + File.separator + nombreAmbito + File.separator
				+ nombreArchivo;
		
		
		
		String url = "/FIPs/refundido/" + nombreAmbito + "/" + nombreArchivo;
		log.info(dir);
		log.info(fileDir);
		
		// Cambiar estado a CREANDO
		FipsGenerados fip = new FipsGenerados();
		fip.setEstado("CREANDO");
		fip.setXml(fileDir);
		fip.setUrl(url);
		
		// Miramos que versión toca
		fip.setVersion(servicioBasicoPrerefundido.obtenerSiguienteVersion(obj.getDatosIdTextoArbolGenerico().getIdBaseDatos()));

		fip.setFechaGeneracion(new Date());
		if (obj != null) {
			fip.setIdTramiteEncargado(obj.getDatosIdTextoArbolGenerico().getIdBaseDatos());
			fip.setNombre(obj.getDatosIdTextoArbolGenerico().getTexto());
		}
		fip.setNombreArchivo(nombreArchivo);
		em.persist(fip);
		em.flush();
		try {
			Transaction.instance().commit();
			Transaction.instance().begin();
			em.joinTransaction();
		} catch (Exception e) {
			e.printStackTrace();
		}	
		

		

		if (os.indexOf("win") >= 0) {
			// dir="C:\\FIPsXML\\"+nombreAmbito;
			// fileDir="C:\\FIPsXML\\"+nombreAmbito+"\\fip" + idTramite +
			// ".xml";

			File directorio = new File(dir);
			directorio.mkdirs();
			log.info(" Estamos en Windows: Se crearan en: " + dir);
		} else {

			// dir="/home/redes/jboss/var/FIPs/refundido/"+nombreAmbito;
			// fileDir="/home/redes/jboss/var/FIPs/refundido/"+nombreAmbito+"/fip"
			// + idTramite + ".xml";
			File directorio = new File(dir);
			directorio.mkdirs();

			log.info(" Estamos en Linux: Se crearan en :"+dir);
		}

		log.info("    Ruta del archivo FIP: " + dir);

		

		try {
			
			String texto = "";
			Tramite iTramite = null;
			iTramite = findComplete(idTramite);
			log.info("    Tramite completo cargado en memoria ");
			
			File guardar = new File(fileDir);
			if (guardar != null) {
				FileWriter guardx = new FileWriter(guardar);
				log.info("    Generando FIP...");
				Document documento = FIP2(iTramite);

				log.info("    Formateando FIP...");
				OutputFormat format = new OutputFormat(documento);
				format.setLineWidth(65);
				format.setIndenting(true);
				format.setIndent(5);

				if (os.indexOf("win") >= 0) {
					format.setEncoding("ISO-8859-1");
				} else {
					format.setEncoding("UTF-8");
				}

				StringWriter writer = new StringWriter();
				XMLSerializer serializer = new XMLSerializer(writer, format);
				serializer.serialize(documento);
				texto = writer.toString();

				log.info("    Guardando FIP en disco...");
				guardx.write(texto);
				guardx.close();
				log.info("    Archivo FIP guardado");

			}
			
			// Cambiar estado a CREADO
			
			//log.info("Obteniendo FIP2 ...");
			//fip = em.find(FipsGenerados.class, fip.getId());
			log.info("Cambiando FIP2 a estado: CREADO");
			fip.setEstado("CREADO");
			em.merge(fip);
			em.flush();
			try {
				Transaction.instance().commit();
				Transaction.instance().begin();
				em.joinTransaction();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
			
			log.error("ERRORES creando el FIP2. Cambiando FIP2 a estado: ERRORES");
			fip.setEstado("ERRORES");
			em.merge(fip);
			em.flush();
			try {
				Transaction.instance().commit();
				Transaction.instance().begin();
				em.joinTransaction();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}


		
		
		
		return fip;
	}

	public Tramite findComplete(Object id) {

		int iden = Integer.parseInt(id.toString());
		Tramite tram = em.find(Tramite.class, iden);
		PropiedadesAdscripcion propsAds;

		em.refresh(tram);
		// CARGAMOS LAS ADSCRIPCIONES
		log.debug("Cargando adscripciones");
		Set<PropiedadesAdscripcion> aa = getAdscripciones(tram);
		tram.setPropiedadesAdscripcion(aa);

		log.debug("Cargando documentos");
		if (tram.getDocumentos() == null) {
			em.persist(tram);
			List<Documento> docTrm = (List<Documento>) tram.getDocumentos();
		}
		tram.getDocumentos().size();
		// log.debug("Numero Documentos del Tramite=" +
		// tram.getDocumentos().size());

		for (Documento doc : tram.getDocumentos()) {
			if (doc.getDocumentoshps() == null) {
				em.persist(doc);
				List<Documentoshp> docShp = (List<Documentoshp>) doc
						.getDocumentoshps();
			}
			doc.getDocumentoshps().size();
			// log.debug("Numero Hojas del Documento=" +
			// doc.getDocumentoshps().size());
		}

		log.debug("Cargando entidades");
		if (tram.getEntidads() == null) {
			em.persist(tram);
			List<Entidad> entTrm = (List<Entidad>) tram.getEntidads();
		}
		tram.getEntidads().size();
		// log.debug("Entidades del Tramite=" +
		// tram.getEntidads().size());

		for (Entidad ent : tram.getEntidads()) {
			if (ent.getEntidadByIdpadre() == null) {
				getEntidad(ent);
			}
		}

		log.debug("Cargando determinaciones");
		if (tram.getDeterminacions() == null) {
			em.persist(tram);
			List<Determinacion> detTrm = (List<Determinacion>) tram
					.getDeterminacions();
		}
		tram.getDeterminacions().size();
		// log.debug("Determinaciones del Tramite=" +
		// tram.getDeterminacions().size());
		for (Determinacion det : tram.getDeterminacions()) {
			if (det.getDeterminacionByIdpadre() == null) {
				getDeterminacion(det);
			}
		}

		log.debug("Cargando operaciones");
		if (tram.getOperacions() == null) {
			em.persist(tram);
			List<Operacion> oper = (List<Operacion>) tram.getOperacions();
		}
		tram.getOperacions().size();
		// log.debug("Operaciones del Tramite=" +
		// tram.getOperacions().size());
		for (Operacion oper : tram.getOperacions()) {
			for (Operacionentidad opEnt : oper.getOperacionentidads()) {
				opEnt.getEntidadByIdentidad().getNombre();
				opEnt.getEntidadByIdentidad().getTramite().getCodigofip();
				opEnt.getEntidadByIdentidadoperadora().getNombre();
				opEnt.getEntidadByIdentidadoperadora().getTramite()
						.getCodigofip();
			}

			for (Operaciondeterminacion opDet : oper
					.getOperaciondeterminacions()) {
				opDet.getDeterminacionByIddeterminacion().getNombre();
				opDet.getDeterminacionByIddeterminacion().getTramite()
						.getCodigofip();
				opDet.getDeterminacionByIddeterminacionoperadora().getNombre();
				opDet.getDeterminacionByIddeterminacionoperadora().getTramite()
						.getCodigofip();
			}
			for (Operacionrelacion opRel : oper.getOperacionrelacions()) {
				// CARGAMOS LAS OPERACIONES DE ADSCRIPCIÓN
				propsAds = getPropiedadesAdscripcion(opRel.getRelacion()
						.getIden());
				opRel.setPropiedadesAdscripcion(propsAds);
			}
		}

		return tram;

	}

	private Set<PropiedadesAdscripcion> getAdscripciones(Tramite tram) {
		String s;
		Query aQuery;
		PropiedadesAdscripcion aProp;
		Set<PropiedadesAdscripcion> aProps = null;

		try {
			aProps = new HashSet<PropiedadesAdscripcion>();

			Integer IdDefPropAdscripcion = Integer.parseInt(Textos.getCadena(
					PROPERTIES, "adscripcion.id_definicion_adscripcion"));
			Integer IdDefEntidadOrigen = Integer.parseInt(Textos.getCadena(
					PROPERTIES, "adscripcion.id_def_origen"));
			Integer IdDefEntidadDestino = Integer.parseInt(Textos.getCadena(
					PROPERTIES, "adscripcion.id_def_destino"));
			s = "select iden "
					+ "from planeamiento.relacion rel "
					+ "where iddefrelacion= "
					+ IdDefPropAdscripcion
					+ " and "
					+ "(select idtramite from planeamiento.entidad where iden in "
					+ "(select valor from planeamiento.vectorrelacion where idrelacion=rel.iden and iddefvector="
					+ IdDefEntidadOrigen
					+ ")) = "
					+ tram.getIden()
					+ " and "
					+ "(select idtramite from planeamiento.entidad where iden in "
					+ "(select valor from planeamiento.vectorrelacion where idrelacion=rel.iden and iddefvector="
					+ IdDefEntidadDestino
					+ ")) = "
					+ tram.getIden()
					+ " and iden not in (select idrelacion from planeamiento.operacionrelacion)";
			aQuery = em.createNativeQuery(s);

			int contador = 0;
			for (Object Resultado : aQuery.getResultList()) {
				aProp = getPropiedadesAdscripcion(Integer.parseInt(Resultado
						.toString()));
				aProps.add(aProp);
				aProp = null;
				contador++;
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return aProps;
	}

	private PropiedadesAdscripcion getPropiedadesAdscripcion(int iden) {
		PropiedadesAdscripcion aProps;
		String s;
		Query aQuery;
		List<Object[]> Resultado;

		try {
			Integer IdDefPropAdscripcion = Integer.parseInt(Textos.getCadena(
					"diccionario", "adscripcion.id_definicion_adscripcion"));
			Integer IdDefEntidadOrigen = Integer.parseInt(Textos.getCadena(
					"diccionario", "adscripcion.id_def_origen"));
			Integer IdDefEntidadDestino = Integer.parseInt(Textos.getCadena(
					"diccionario", "adscripcion.id_def_destino"));
			Integer IdDefUnidad = Integer.parseInt(Textos.getCadena(
					"diccionario", "adscripcion.id_def_unidad"));
			Integer IdDefTipo = Integer.parseInt(Textos.getCadena(
					"diccionario", "adscripcion.id_def_tipo"));
			Integer IdDefCuantia = Integer.parseInt(Textos.getCadena(
					"diccionario", "adscripcion.id_def_cuantia"));
			Integer IdDefTexto = Integer.parseInt(Textos.getCadena(
					"diccionario", "adscripcion.id_def_texto"));

			s = "Select r.iden,"
					+ "(select valor from planeamiento.vectorrelacion where iddefvector ="
					+ IdDefEntidadOrigen
					+ " and idrelacion=r.iden ) as identidadorigen,"
					+ "(select valor from planeamiento.vectorrelacion where iddefvector ="
					+ IdDefEntidadDestino
					+ " and idrelacion=r.iden ) as identidaddestino,"
					+ "(select valor from planeamiento.vectorrelacion where iddefvector ="
					+ IdDefUnidad
					+ " and idrelacion=r.iden ) as idunidad,"
					+ "(select valor from planeamiento.vectorrelacion where iddefvector ="
					+ IdDefTipo
					+ " and idrelacion=r.iden ) as idtipo,"
					+ "(select valor from planeamiento.Propiedadrelacion where iddefpropiedad ="
					+ IdDefCuantia
					+ " and idrelacion=r.iden ) as cuantia,"
					+ "(select valor from planeamiento.Propiedadrelacion where iddefpropiedad ="
					+ IdDefTexto
					+ " and idrelacion=r.iden ) as texto "
					+ " From planeamiento.Relacion r, planeamiento.Vectorrelacion vr "
					+ " Where r.iddefrelacion="
					+ IdDefPropAdscripcion
					+ " And r.iden="
					+ iden
					+ " And vr.iddefvector="
					+ IdDefEntidadOrigen + " And vr.idrelacion=r.iden ";

			aQuery = em.createNativeQuery(s);

			Resultado = aQuery.getResultList();

			aProps = null;
			String Texto = "";
			if (Resultado.size() > 0) {
				for (Object[] aPropsAds : Resultado) {

					if (aPropsAds[6] != null) {
						Texto = aPropsAds[6].toString();
					} else {
						Texto = "";
					}
					aProps = new PropiedadesAdscripcion(
							Integer.parseInt(aPropsAds[0].toString()), em.find(
									Entidad.class, aPropsAds[1]), em.find(
									Entidad.class, aPropsAds[2]),
							Double.parseDouble(aPropsAds[5].toString()),
							em.find(Determinacion.class, aPropsAds[3]),
							em.find(Determinacion.class, aPropsAds[4]), Texto);
				}
			}

			return aProps;

		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	private void getDeterminacion(Determinacion det) {
		PropiedadesUnidad propsUni;

		Determinacion aDeterminacion;
		// log.debug("----------------------------------------------------------");
		if (det.getDocumentodeterminacions() == null) {
			em.persist(det);
			List<Documentodeterminacion> docDet = (List<Documentodeterminacion>) det
					.getDocumentodeterminacions();
		}
		det.getDocumentodeterminacions().size();
		// log.debug("Documentos de la Determinacion=" +
		// det.getDocumentodeterminacions().size());

		if (det.getOpciondeterminacionsForIddeterminacion() == null) {
			em.persist(det);
			List<Opciondeterminacion> optDet = (List<Opciondeterminacion>) det
					.getOpciondeterminacionsForIddeterminacion();
		}
		det.getOpciondeterminacionsForIddeterminacion().size();
		// log.debug("Opciones de la Determinacion=" +
		// det.getOpciondeterminacionsForIddeterminacion().size());

		if (det.getDeterminaciongrupoentidadsForIddeterminacion() == null) {
			em.persist(det);
			List<Determinaciongrupoentidad> optGrp = (List<Determinaciongrupoentidad>) det
					.getDeterminaciongrupoentidadsForIddeterminacion();
		}
		det.getDeterminaciongrupoentidadsForIddeterminacion().size();
		// log.debug("Grupos de aplicación de la Determinacion=" +
		// det.getDeterminaciongrupoentidadsForIddeterminacion().size());
		for (Determinaciongrupoentidad grp : det
				.getDeterminaciongrupoentidadsForIddeterminacion()) {
			grp.getDeterminacionByIddeterminaciongrupo().getTramite()
					.getCodigofip();
			grp.getDeterminacionByIddeterminaciongrupo().getNombre();
		}

		if (det.getDeterminacionsForIdpadre() == null) {
			em.persist(det);
			List<Determinacion> detHijs = (List<Determinacion>) det
					.getDeterminacionsForIdpadre();
		}
		det.getDeterminacionsForIdpadre().size();
		// log.debug("Hijas de la Determinación=" +
		// det.getDeterminacionsForIdpadre().size());

		if (det.getDeterminacionByIddeterminacionbase() == null) {
			em.persist(det);
			Determinacion detBas = (Determinacion) det
					.getDeterminacionByIddeterminacionbase();
		}

		if (det.getDeterminacionByIddeterminacionbase() != null) {
			det.getDeterminacionByIddeterminacionbase().getNombre();
			det.getDeterminacionByIddeterminacionbase().getTramite()
					.getCodigofip();
		}

		if (det.getTramite() == null) {
			em.persist(det);
			Tramite trmDet = (Tramite) det.getTramite();
		}

		det.getTramite().getCodigofip();
		// log.debug("Código del Trámite de la Determinacion=" +
		// det.getTramite().getCodigofip());

		aDeterminacion = getUnidad(det);
		if (aDeterminacion != null) {
			aDeterminacion.getNombre();
			aDeterminacion.getTramite().getCodigofip();
		}

		if (getDeterminacionesReguladoras(det) != null) {
			for (Determinacion detReg : getDeterminacionesReguladoras(det)) {
				detReg.getNombre();
				detReg.getTramite().getCodigofip();
			}
		}

		if (getRegulacionEspecifica(det) == null) {

			List<RegulacionEspecifica> regEsp = (List<RegulacionEspecifica>) getRegulacionEspecifica(det);
		}
		getRegulacionEspecifica(det).size();

		/*
		 * //CARGAMOS LAS PROPIEDADES DE UNIDAD propsUni =
		 * getPropiedadesUnidad(det); det.setPropiedadesUnidad(propsUni);
		 */
		for (Determinacion detHija : det.getDeterminacionsForIdpadre()) {
			getDeterminacion(detHija);
		}

	}

	public Determinacion getUnidad(Determinacion det) {
		Integer IdDefUnidad;
		Integer IdDefDeterminacion;
		Integer IdDefDeterminacionUnidad;
		Integer idUnidad;
		String s;
		Query aQuery;
		Determinacion unidad = null;

		try {
			if (unidad == null) {
				IdDefUnidad = Integer.parseInt(Textos.getCadena("diccionario",
						"id_definicion_unidad")); // Integer.parseInt(es.mitc.redes.urbanismoenred.utils.configuracion.RedesConfig.getConfigParameter("redes/produccion/codigos/unidades/id_definicion"));
				IdDefDeterminacion = Integer.parseInt(Textos.getCadena(
						"diccionario", "id_determinacion_unidad_aplic"));
				IdDefDeterminacionUnidad = Integer.parseInt(Textos.getCadena(
						"diccionario", "id_determinacion_unidad"));
				s = "select valor from planeamiento.vectorrelacion where "
						+ " idRelacion in "
						+ " (select idrelacion from planeamiento.vectorrelacion where valor="
						+ det.getIden()
						+ " and iddefvector="
						+ IdDefDeterminacion
						+ ")"
						+ " and idrelacion in (select idrelacion from planeamiento.relacion where iddefrelacion in ("
						+ IdDefUnidad + "))" + " and iddefvector="
						+ IdDefDeterminacionUnidad;

				aQuery = em.createNativeQuery(s);
				if (aQuery.getResultList().size() > 0) {
					idUnidad = (Integer) aQuery.getSingleResult();
					unidad = em.find(Determinacion.class, idUnidad);
					if (unidad.getNombre() == null) {
						em.persist(unidad);
					}
					if (unidad.getTramite().getCodigofip() == null) {
						em.persist(unidad);
					}
				} else {
					unidad = null;
				}
			}
			return unidad;

		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	private List<Determinacion> getUnidades(int idTramite) {

		String s;
		Query aQuery;
		List<Determinacion> unidadesTramite = null;

		try {

			s = "SELECT d " + " FROM Determinacion d "
					+ " WHERE d.idcaracter=18 AND d.tramite.iden = "
					+ idTramite;

			aQuery = em.createQuery(s);

			unidadesTramite = aQuery.getResultList();

		} catch (Exception ex) {
			ex.printStackTrace();

		}

		return unidadesTramite;
	}

	public Set<Determinacion> getDeterminacionesReguladoras(Determinacion det) {
		String IdDefRegulacion;
		String IdDefDeterminacionRegulada;
		String IdDefDeterminacionReguladora;
		String s;
		Query aQuery;
		List Resultado;
		Determinacion aReguladora;
		Set<Determinacion> determinacionesReguladoras = null;

		try {

			if (determinacionesReguladoras == null) {

				IdDefRegulacion = Textos.getCadena("diccionario",
						"id_definicion_regulacion");// es.mitc.redes.urbanismoenred.utils.configuracion.RedesConfig.getConfigParameter("redes/produccion/codigos/determinaciones_reguladoras/id_definicion");
				IdDefDeterminacionRegulada = Textos.getCadena("diccionario",
						"id_definicion_regulada");
				IdDefDeterminacionReguladora = Textos.getCadena("diccionario",
						"id_definicion_reguladora");

				s = "select valor from planeamiento.vectorrelacion where "
						+ " idRelacion in "
						+ " (select idrelacion from planeamiento.vectorrelacion where valor="
						+ det.getIden()
						+ " and iddefvector in ("
						+ IdDefDeterminacionRegulada
						+ "))"
						+ " and idrelacion in (select idrelacion from planeamiento.relacion where iddefrelacion in ("
						+ IdDefRegulacion + "))" + " and iddefvector in ("
						+ IdDefDeterminacionReguladora + ")";

				aQuery = em.createNativeQuery(s);
				determinacionesReguladoras = new HashSet<Determinacion>(0);

				Resultado = aQuery.getResultList();
				if (Resultado.size() > 0) {
					for (Object idDeterminacion : Resultado) {
						aReguladora = em.find(Determinacion.class,
								(Integer) idDeterminacion);
						aReguladora.getTramite().getCodigofip();
						aReguladora.getNombre();
						determinacionesReguladoras.add(aReguladora);
					}
				}
			}
			return determinacionesReguladoras;

		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public Set<RegulacionEspecifica> getRegulacionEspecifica(Determinacion det) {
		String IdDefRegulacionEsp;
		Integer IdDefDeterminacionRegulada;
		Integer IdDefRegulacionPadre;
		Integer IdNombre;
		Integer IdOrden;
		Integer IdTexto;
		String s;
		Query aQuery;
		List<Object[]> Resultado;
		Set<RegulacionEspecifica> regulacionEspecifica = null;

		try {
			if (regulacionEspecifica == null) {
				IdDefRegulacionEsp = Textos.getCadena("diccionario",
						"id_definicion_regulacionespecifica");// es.mitc.redes.urbanismoenred.utils.configuracion.RedesConfig.getConfigParameter("redes/produccion/codigos/regulacion_especifica/id_definicion");
				IdDefDeterminacionRegulada = Integer.parseInt(Textos.getCadena(
						"diccionario", "id_def_regulada"));
				IdDefRegulacionPadre = Integer.parseInt(Textos.getCadena(
						"diccionario", "id_def_reg_padre"));
				IdNombre = Integer.parseInt(Textos.getCadena("diccionario",
						"id_nombre"));
				IdOrden = Integer.parseInt(Textos.getCadena("diccionario",
						"id_orden"));
				IdTexto = Integer.parseInt(Textos.getCadena("diccionario",
						"id_texto"));

				s = "Select r.iden,"
						+ "(select valor from planeamiento.Propiedadrelacion where iddefpropiedad ="
						+ IdOrden
						+ " and idrelacion=r.iden ) as orden,"
						+ "(select valor from planeamiento.Propiedadrelacion where iddefpropiedad ="
						+ IdNombre
						+ " and idrelacion=r.iden ) as nombre,"
						+ "(select valor from planeamiento.Propiedadrelacion where iddefpropiedad ="
						+ IdTexto
						+ " and idrelacion=r.iden ) as texto"
						+ " From planeamiento.Relacion r, planeamiento.Vectorrelacion vr "
						+ " Where r.iddefrelacion="
						+ IdDefRegulacionEsp
						+ " And vr.iddefvector="
						+ IdDefDeterminacionRegulada
						+ " And vr.valor="
						+ det.getIden()
						+ " And vr.idrelacion=r.iden "
						+ " And (select valor from planeamiento.Vectorrelacion where iddefvector="
						+ IdDefRegulacionPadre + " and idrelacion=r.iden) =0";

				aQuery = em.createNativeQuery(s);
				regulacionEspecifica = new HashSet<RegulacionEspecifica>(0);

				Resultado = aQuery.getResultList();

				if (Resultado.size() > 0) {
					for (Object[] regulacion : Resultado) {
						RegulacionEspecifica Regulacion;
						Regulacion = new RegulacionEspecifica(
								Integer.parseInt(regulacion[0].toString()),
								Integer.parseInt(regulacion[1].toString()),
								regulacion[2].toString(),
								regulacion[3].toString());
						Regulacion
								.setRegulacionesespecificas(RegulacionesHijas(
										Regulacion, det));
						regulacionEspecifica.add(Regulacion);
					}
				}
			}
			return regulacionEspecifica;

		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	private Set<RegulacionEspecifica> RegulacionesHijas(
			RegulacionEspecifica Padre, Determinacion det) {
		String IdDefRegulacionEsp;
		Integer IdDefDeterminacionRegulada;
		Integer IdDefRegulacionPadre;
		Integer IdNombre;
		Integer IdOrden;
		Integer IdTexto;
		String s;
		Query aQuery;
		List<Object[]> Resultado;
		Set<RegulacionEspecifica> regulaciones = new HashSet<RegulacionEspecifica>(
				0);

		try {
			IdDefRegulacionEsp = Textos.getCadena("diccionario",
					"id_definicion_regulacionespecifica");// es.mitc.redes.urbanismoenred.utils.configuracion.RedesConfig.getConfigParameter("redes/produccion/codigos/regulacion_especifica/id_definicion");
			IdDefDeterminacionRegulada = Integer.parseInt(Textos.getCadena(
					"diccionario", "id_def_regulada"));
			IdDefRegulacionPadre = Integer.parseInt(Textos.getCadena(
					"diccionario", "id_def_reg_padre"));
			IdNombre = Integer.parseInt(Textos.getCadena("diccionario",
					"id_nombre"));
			IdOrden = Integer.parseInt(Textos.getCadena("diccionario",
					"id_orden"));
			IdTexto = Integer.parseInt(Textos.getCadena("diccionario",
					"id_texto"));

			s = "Select r.iden,"
					+ "(select valor from planeamiento.Propiedadrelacion where iddefpropiedad ="
					+ IdOrden
					+ " and idrelacion=r.iden ) as orden,"
					+ "(select valor from planeamiento.Propiedadrelacion where iddefpropiedad ="
					+ IdNombre
					+ " and idrelacion=r.iden ) as nombre,"
					+ "(select valor from planeamiento.Propiedadrelacion where iddefpropiedad ="
					+ IdTexto
					+ " and idrelacion=r.iden ) as texto"
					+ " From planeamiento.Relacion r, planeamiento.Vectorrelacion vr "
					+ " Where r.iddefrelacion="
					+ IdDefRegulacionEsp
					+ " And vr.iddefvector="
					+ IdDefDeterminacionRegulada
					+ " And vr.valor="
					+ det.getIden()
					+ " And vr.idrelacion=r.iden "
					+ " And (select valor from planeamiento.Vectorrelacion where iddefvector="
					+ IdDefRegulacionPadre + " and idrelacion=r.iden) ="
					+ Padre.getIden();

			aQuery = em.createNativeQuery(s);

			Resultado = aQuery.getResultList();

			if (Resultado.size() > 0) {
				for (Object[] regulacion : Resultado) {
					RegulacionEspecifica Regulacion;
					Regulacion = new RegulacionEspecifica(
							Integer.parseInt(regulacion[0].toString()),
							Integer.parseInt(regulacion[1].toString()),
							regulacion[2].toString(), regulacion[3].toString());
					Regulacion.setRegulacionesespecificas(RegulacionesHijas(
							Regulacion, det));
					regulaciones.add(Regulacion);
				}
			}

			return regulaciones;

		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	private void getEntidad(Entidad ent) {
		// log.debug("----------------------------------------------------------");
		if (ent.getDocumentoentidads() == null) {
			em.persist(ent);
			List<Documentoentidad> docEnt = (List<Documentoentidad>) ent
					.getDocumentoentidads();
		}
		ent.getDocumentoentidads().size();
		// log.debug("Documentos de la Entidad=" +
		// ent.getDocumentoentidads().size());

		if (ent.getEntidadpols() == null) {
			em.persist(ent);
			List<Entidadpol> geo = (List<Entidadpol>) ent.getEntidadpols();
		}
		ent.getEntidadpols().size();

		if (ent.getEntidadlins() == null) {
			em.persist(ent);
			List<Entidadlin> geo = (List<Entidadlin>) ent.getEntidadlins();
		}
		ent.getEntidadlins().size();

		if (ent.getEntidadpnts() == null) {
			em.persist(ent);
			List<Entidadpnt> geo = (List<Entidadpnt>) ent.getEntidadpnts();
		}
		ent.getEntidadpnts().size();

		if (ent.getEntidadsForIdpadre() == null) {
			em.persist(ent);
			List<Entidad> entHijs = (List<Entidad>) ent.getEntidadsForIdpadre();
		}
		ent.getEntidadsForIdpadre().size();

		if (ent.getEntidadByIdentidadbase() == null) {
			em.persist(ent);
			Entidad entBas = (Entidad) ent.getEntidadByIdentidadbase();
		} else if (ent.getEntidadByIdentidadbase() != null) {
			ent.getEntidadByIdentidadbase().getNombre();
			ent.getEntidadByIdentidadbase().getTramite().getCodigofip();
		}

		if (ent.getTramite() == null) {
			em.persist(ent);
			Tramite trmEnt = (Tramite) ent.getTramite();
		}
		ent.getTramite().getCodigofip();

		if (ent.getEntidaddeterminacions() == null) {
			em.persist(ent);
			List<Entidaddeterminacion> entDet = (List<Entidaddeterminacion>) ent
					.getEntidaddeterminacions();
		}
		ent.getEntidaddeterminacions().size();

		for (Entidaddeterminacion entDet : ent.getEntidaddeterminacions()) {
			getEntidadDeterminacion(entDet);
		}

		for (Entidad entHija : ent.getEntidadsForIdpadre()) {
			getEntidad(entHija);
		}
	}

	private void getEntidadDeterminacion(Entidaddeterminacion entDet) {

		if (entDet.getCasoentidaddeterminacions() == null) {
			em.persist(entDet);
			List<Casoentidaddeterminacion> aCaso = (List<Casoentidaddeterminacion>) entDet
					.getCasoentidaddeterminacions();
		}

		entDet.getCasoentidaddeterminacions().size();
		entDet.getDeterminacion().getNombre();
		entDet.getDeterminacion().getTramite().getCodigofip();

		for (Casoentidaddeterminacion aCaso : entDet
				.getCasoentidaddeterminacions()) {
			getCaso(aCaso);
		}
	}

	private void getCaso(Casoentidaddeterminacion aCaso) {
		try {

			if (aCaso.getDocumentocasos() == null) {
				em.persist(aCaso);
				List<Documentocaso> docCas = (List<Documentocaso>) aCaso
						.getDocumentocasos();
			}
			aCaso.getDocumentocasos().size();

			if (aCaso.getVinculocasosForIdcaso() == null) {
				em.persist(aCaso);
				List<Vinculocaso> vinc = (List<Vinculocaso>) aCaso
						.getVinculocasosForIdcaso();
			}
			aCaso.getVinculocasosForIdcaso().size();

			for (Vinculocaso vinc : aCaso.getVinculocasosForIdcaso()) {
				getCaso(vinc.getCasoentidaddeterminacionByIdcasovinculado());
			}

			if (aCaso.getEntidaddeterminacionregimensForIdcaso() == null) {
				em.persist(aCaso);
				List<Entidaddeterminacionregimen> vinc = (List<Entidaddeterminacionregimen>) aCaso
						.getEntidaddeterminacionregimensForIdcaso();
			}
			aCaso.getEntidaddeterminacionregimensForIdcaso().size();

			for (Entidaddeterminacionregimen reg : aCaso
					.getEntidaddeterminacionregimensForIdcaso()) {
				getRegimen(reg);
			}

		} catch (Exception ex) {
			log.debug("ERROR: " + ex.toString());
		}
	}

	private void getRegimen(Entidaddeterminacionregimen aReg) {
		if (aReg.getRegimenespecificos() == null) {
			em.persist(aReg);
			List<Regimenespecifico> reg = (List<Regimenespecifico>) aReg
					.getRegimenespecificos();
		}
		aReg.getRegimenespecificos().size();

		for (Regimenespecifico reg : aReg.getRegimenespecificos()) {
			if (reg.getRegimenespecifico() == null) {
				getRegimenEspecifico(reg);
			}
		}

		if (aReg.getOpciondeterminacion() == null) {
			em.persist(aReg);
			Opciondeterminacion opc = (Opciondeterminacion) aReg
					.getOpciondeterminacion();
		} else {
			aReg.getOpciondeterminacion()
					.getDeterminacionByIddeterminacionvalorref().getNombre();
			aReg.getOpciondeterminacion()
					.getDeterminacionByIddeterminacionvalorref().getTramite()
					.getCodigofip();
		}

		if (aReg.getDeterminacion() == null) {
			em.persist(aReg);
			Determinacion det = (Determinacion) aReg.getDeterminacion();
		} else {
			aReg.getDeterminacion().getNombre();
			aReg.getDeterminacion().getTramite().getCodigofip();
		}

		if (aReg.getCasoentidaddeterminacionByIdcasoaplicacion() == null) {
			em.persist(aReg);
			Casoentidaddeterminacion cas = (Casoentidaddeterminacion) aReg
					.getCasoentidaddeterminacionByIdcasoaplicacion();
		} else {
			aReg.getCasoentidaddeterminacionByIdcasoaplicacion().getNombre();
		}

	}

	private void getRegimenEspecifico(Regimenespecifico aReg) {
		if (aReg.getRegimenespecificos() == null) {
			em.persist(aReg);
			List<Regimenespecifico> reg = (List<Regimenespecifico>) aReg
					.getRegimenespecificos();
		}
		aReg.getRegimenespecificos().size();
		for (Regimenespecifico reg : aReg.getRegimenespecificos()) {
			getRegimenEspecifico(reg);
		}
	}

	public Document FIP2(Tramite tram) {
		try {
			Document aXML;

			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			aXML = XML.generarXMLDOC("FIP");
			aXML.getDocumentElement().setAttribute("VERSION",
					String.valueOf("2.0"));
			aXML.getDocumentElement().setAttribute("FECHA",
					dateFormat.format(new Date()));
			aXML.getDocumentElement().setAttribute("PAIS", "es");
			aXML.getDocumentElement().setAttribute("SRS", "23030");
			aXML.getDocumentElement().appendChild(this.FIP(aXML, tram));

			return aXML;

		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public DocumentFragment FIP(Document aXML, Tramite tram) {
		try {

			DocumentFragment aXMLFrag;
			Node aNodo;
			Element aNodo2;
			Node aNodoOpEntidad;
			Element aRoot;

			DecimalFormat format4 = new DecimalFormat("000000");
			DecimalFormat format10 = new DecimalFormat("0000000000");

			aRoot = aXML.createElement("TRAMITE");
			aRoot.setAttribute("tipoTramite",
					format4.format(tram.getIdtipotramite()));
			aRoot.setAttribute("codigo", String.valueOf(tram.getCodigofip()));

			if (tram.getTexto() != null && !tram.getTexto().trim().equals("")) {
				aNodo = aXML.createElement("TEXTO");
				aNodo.setTextContent(tram.getTexto());
				aRoot.appendChild(aNodo);
			}
			if (tram.getDocumentos().size() > 0) {
				aNodo = aXML.createElement("DOCUMENTOS");
				for (Documento aDocumento : tram.getDocumentos()) {
					aNodo.appendChild(documentoFIP(aXML, aDocumento));
				}
				aRoot.appendChild(aNodo);
			}

			if (tram.getEntidads().size() > 0) {
				aNodo = aXML.createElement("ENTIDADES");
				for (Entidad aEntidad : tram.getEntidads()) {
					if (aEntidad.getEntidadByIdpadre() == null) {
						aNodo.appendChild(entidadFIP(aXML, aEntidad));
					}
				}
				aRoot.appendChild(aNodo);
			}

			if (tram.getDeterminacions().size() > 0) {
				aNodo = aXML.createElement("DETERMINACIONES");
				
				
				//FGA
				// Obtengo el listado de las determinaciones que no tienen padre y ordenadas por campo orden
				String consultaRaiceDetOrdenadas = "select det from Determinacion det where det.determinacionByIdpadre is null and det.tramite.iden="+tram.getIden()+
													" order by det.orden asc";
				
				List<Determinacion> detList = (List<Determinacion>) em.createQuery(consultaRaiceDetOrdenadas).getResultList();
				
				for (Determinacion aDeterminacion : detList) {
					aNodo.appendChild(determinacionFIP(aXML, aDeterminacion));
				}
				
				
				/* Original
				for (Determinacion aDeterminacion : tram.getDeterminacions()) {
					if (aDeterminacion.getDeterminacionByIdpadre() == null) {
						aNodo.appendChild(determinacionFIP(aXML, aDeterminacion));
					}
				}
				*/
				aRoot.appendChild(aNodo);
			}

			// Condiciones Urbanísticas
			if (tram.getEntidads().size() > 0) {
				aNodo = null;
				for (Entidad aEntidad : tram.getEntidads()) {
					aNodo = addCondicionesToFIP(aEntidad, aNodo, aXML);
				}
				if (aNodo != null) {
					aRoot.appendChild(aNodo);
				}
			}

			if (tram.getOperacions().size() > 0) {
				aNodo = aXML.createElement("OPERACIONES");
				for (Operacion aOperacion : tram.getOperacions()) {
					if (aOperacion.getOperaciondeterminacions().size() > 0) {
						aNodo2 = aXML
								.createElement("OPERACIONES-DETERMINACIONES");
						for (Operaciondeterminacion opDet : aOperacion
								.getOperaciondeterminacions()) {
							aNodo2.appendChild(operacionDeterminacionFIP(aXML,
									opDet));
						}
						aNodo.appendChild(aNodo2);
						aNodo2 = null;
					}
				}
				for (Operacion aOperacion : tram.getOperacions()) {
					aNodoOpEntidad = null;
					if (aOperacion.getOperacionentidads().size() > 0) {
						aNodoOpEntidad = aXML
								.createElement("OPERACIONES-ENTIDADES");
						for (Operacionentidad opEnt : aOperacion
								.getOperacionentidads()) {
							aNodoOpEntidad.appendChild(operacionEntidadFIP(
									aXML, opEnt));
						}

					}
					if (aOperacion.getOperacionrelacions().size() > 0) {
						if (aNodoOpEntidad == null) {
							aNodoOpEntidad = aXML
									.createElement("OPERACIONES-ENTIDADES");
						}
						for (Operacionrelacion opRel : aOperacion
								.getOperacionrelacions()) {
							aNodoOpEntidad.appendChild(operacionRelacionFIP(
									aXML, opRel));
						}
					}
					if (aNodoOpEntidad != null) {
						aNodo.appendChild(aNodoOpEntidad);
					}
					aNodoOpEntidad = null;
				}
				aRoot.appendChild(aNodo);
			}

			// Unidades
			/*
			 * FGA: Antiguo if (tram.getDeterminacions().size() > 0) { aNodo =
			 * null; for (Determinacion aDeterminacion :
			 * tram.getDeterminacions()) { aNodo =
			 * addUnidadesToFIP(aDeterminacion, aNodo, aXML); } if (aNodo !=
			 * null) { aRoot.appendChild(aNodo); } }
			 */

			List<Determinacion> listaUnidades = getUnidades(tram.getIden());
			if (listaUnidades.size() > 0) {
				aNodo = aXML.createElement("UNIDADES");
				for (Determinacion aUnidad : listaUnidades) {

					aNodo.appendChild(addUnidadToFIP(aXML, aUnidad));

				}
				aRoot.appendChild(aNodo);
			}

			String origen = "";
			String destino = "";
			if (tram.getPropiedadesAdscripcion() != null) {
				if (tram.getPropiedadesAdscripcion().size() > 0) {
					aNodo = aXML.createElement("ADSCRIPCIONES");
					for (PropiedadesAdscripcion aAdscr : tram
							.getPropiedadesAdscripcion()) {
						aNodo2 = aXML.createElement("ADSCRIPCION");
						origen = aAdscr.getOrigen().getCodigo();
						destino = aAdscr.getDestino().getCodigo();
						if (!origen.equals(null)) {
							aNodo2.setAttribute("entidadOrigen",
									format10.format(Long.parseLong(origen)));
						}
						if (!destino.equals(null)) {
							aNodo2.setAttribute("entidadDestino",
									format10.format(Long.parseLong(destino)));
						}
						aNodo2.appendChild(propiedadesAdscripcionFIP(aXML,
								"PROPIEDADES", aAdscr));
						aNodo.appendChild(aNodo2);
					}
					aRoot.appendChild(aNodo);
				}
			}

			aXMLFrag = aXML.createDocumentFragment();
			aXMLFrag.appendChild(aRoot);
			return aXMLFrag;

		} catch (Exception ex) {
			log.debug("Error en tramite. " + ex);
			ex.printStackTrace();
			return null;
		}

	}

	public DocumentFragment propiedadesAdscripcionFIP(Document aXML,
			String nombre, PropiedadesAdscripcion propAdscrp) {
		try {
			Node aNodo;
			Element aRoot;

			DocumentFragment aXMLFrag;

			aRoot = aXML.createElement(nombre);

			aRoot.setAttribute("cuantia",
					String.valueOf(propAdscrp.getCuantia()));

			if (propAdscrp.getUnidad() != null) {
				aRoot.appendChild(determinacionCodigoFIP(aXML, "UNIDAD",
						propAdscrp.getUnidad()));
			}

			if (propAdscrp.getTipo() != null) {
				aRoot.appendChild(determinacionCodigoFIP(aXML, "TIPO",
						propAdscrp.getTipo()));
			}

			if (propAdscrp.getTexto() != null
					&& !propAdscrp.getTexto().trim().equals("")) {
				aNodo = aXML.createElement("TEXTO");
				aNodo.setTextContent(propAdscrp.getTexto());
				aRoot.appendChild(aNodo);
			}

			aXMLFrag = aXML.createDocumentFragment();
			aXMLFrag.appendChild(aRoot);
			return aXMLFrag;

		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}

	}

	public DocumentFragment operacionRelacionFIP(Document aXML,
			Operacionrelacion oprel) {
		try {
			// Solo las operaciones de creación o borrado de adscripción
			DocumentFragment aXMLFrag;
			Node aNodo;
			Element aRoot;
			DecimalFormat format6 = new DecimalFormat("000000");

			aRoot = aXML.createElement("OPERACION-ENTIDAD");
			aRoot.setAttribute("tipo",
					format6.format(oprel.getIdtipooperacionrel()));
			aRoot.setAttribute("orden",
					String.valueOf(oprel.getOperacion().getOrden()));

			if (oprel.getPropiedadesAdscripcion() != null) {
				aRoot.appendChild(entidadCodigoFIP(aXML, "OPERADA", oprel
						.getPropiedadesAdscripcion().getOrigen()));
				aRoot.appendChild(entidadCodigoFIP(aXML, "OPERADORA", oprel
						.getPropiedadesAdscripcion().getDestino()));
			}
			if (oprel.getOperacion().getTexto() != null
					&& !oprel.getOperacion().getTexto().trim().equals("")) {
				aNodo = aXML.createElement("TEXTO");
				aNodo.setTextContent(oprel.getOperacion().getTexto());
				aRoot.appendChild(aNodo);
			}

			// PROPIEDADES DE LA ADSCRIPCION (CREACIÓN)

			if (oprel.getPropiedadesAdscripcion() != null) {
				aRoot.appendChild(propiedadesAdscripcionFIP(aXML,
						"PROPIEDADES-ADSCRIPCION",
						oprel.getPropiedadesAdscripcion()));
			}

			aXMLFrag = aXML.createDocumentFragment();
			aXMLFrag.appendChild(aRoot);
			return aXMLFrag;

		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}

	}

	public DocumentFragment operacionEntidadFIP(Document aXML,
			Operacionentidad opent) {
		try {

			DocumentFragment aXMLFrag;
			Node aNodo;
			Element aRoot;
			DecimalFormat format6 = new DecimalFormat("000000");

			aRoot = aXML.createElement("OPERACION-ENTIDAD");
			aRoot.setAttribute("tipo",
					format6.format(opent.getIdtipooperacionent()));
			aRoot.setAttribute("orden",
					String.valueOf(opent.getOperacion().getOrden()));

			aRoot.appendChild(entidadCodigoFIP(aXML, "OPERADA",
					opent.getEntidadByIdentidad()));
			aRoot.appendChild(entidadCodigoFIP(aXML, "OPERADORA",
					opent.getEntidadByIdentidadoperadora()));

			if (opent.getOperacion().getTexto() != null
					&& !opent.getOperacion().getTexto().trim().equals("")) {
				aNodo = aXML.createElement("TEXTO");
				aNodo.setTextContent(opent.getOperacion().getTexto());
				aRoot.appendChild(aNodo);
			}

			aXMLFrag = aXML.createDocumentFragment();
			aXMLFrag.appendChild(aRoot);
			return aXMLFrag;

		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}

	}

	public DocumentFragment operacionDeterminacionFIP(Document aXML,
			Operaciondeterminacion opdet) {
		try {

			DocumentFragment aXMLFrag;
			Node aNodo;
			Element aRoot;
			DecimalFormat format6 = new DecimalFormat("000000");

			aRoot = aXML.createElement("OPERACION-DETERMINACION");
			aRoot.setAttribute("tipo",
					format6.format(opdet.getIdtipooperaciondet()));
			aRoot.setAttribute("orden",
					String.valueOf(opdet.getOperacion().getOrden()));

			aRoot.appendChild(determinacionCodigoFIP(aXML, "OPERADA",
					opdet.getDeterminacionByIddeterminacion()));
			aRoot.appendChild(determinacionCodigoFIP(aXML, "OPERADORA",
					opdet.getDeterminacionByIddeterminacionoperadora()));

			if (opdet.getOperacion().getTexto() != null
					&& !opdet.getOperacion().getTexto().trim().equals("")) {
				aNodo = aXML.createElement("TEXTO");
				aNodo.setTextContent(opdet.getOperacion().getTexto());
				aRoot.appendChild(aNodo);
			}

			aXMLFrag = aXML.createDocumentFragment();
			aXMLFrag.appendChild(aRoot);
			return aXMLFrag;

		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}

	}

	public DocumentFragment determinacionFIP(Document aXML, Determinacion det) {
		try {
			Node aNodo2;
			Node aNodo;
			Element aRoot;
			Element aElement;
			DocumentFragment aXMLFrag;
			DecimalFormat format10 = new DecimalFormat("0000000000");
			DecimalFormat format6 = new DecimalFormat("000000");

			aRoot = aXML.createElement("DETERMINACION");

			aRoot.setAttribute("codigo",
					format10.format(Long.parseLong(det.getCodigo())));
			aRoot.setAttribute("caracter", format6.format(det.getIdcaracter()));
			aRoot.setAttribute("apartado", String.valueOf(det.getApartado()));
			aRoot.setAttribute("nombre", String.valueOf(det.getNombre()));

			if (det.getEtiqueta() != null) {
				aRoot.setAttribute("etiqueta",
						String.valueOf(det.getEtiqueta()));
			}

			aRoot.setAttribute("suspendida",
					String.valueOf(det.isBsuspendida()));

			if (det.getTexto() != null && !det.getTexto().trim().equals("")) {
				aNodo = aXML.createElement("TEXTO");
				aNodo.setTextContent(det.getTexto());
				aRoot.appendChild(aNodo);
			}

			if (det.getOpciondeterminacionsForIddeterminacion().size() > 0) {
				aNodo = aXML.createElement("VALORES-REFERENCIA");
				for (Opciondeterminacion opc : det
						.getOpciondeterminacionsForIddeterminacion()) {
					aNodo.appendChild(determinacionCodigoFIP(aXML,
							"VALOR-REFERENCIA",
							opc.getDeterminacionByIddeterminacionvalorref()));
				}
				aRoot.appendChild(aNodo);
			}

			if (det.getDeterminacionsForIdpadre().size() > 0) {
				aNodo = aXML.createElement("HIJAS");
				
				//FGA
				// Obtengo el listado de las determinaciones que no tienen padre y ordenadas por campo orden
				String consultaHijasDetOrdenadas = "select det from Determinacion det where det.determinacionByIdpadre="+det.getIden()+
													" order by det.orden asc";
				
				List<Determinacion> detList = (List<Determinacion>) em.createQuery(consultaHijasDetOrdenadas).getResultList();
				
				for (Determinacion detHija: detList) {
					aNodo.appendChild(determinacionFIP(aXML, detHija));
				}
				
				/* Original
				for (Determinacion detHija : det.getDeterminacionsForIdpadre()) {
					aNodo.appendChild(determinacionFIP(aXML, detHija));
				}
				*/
				aRoot.appendChild(aNodo);
			}

			if (det.getDocumentodeterminacions().size() > 0) {
				aNodo = aXML.createElement("DOCUMENTOS");
				for (Documentodeterminacion doc : det
						.getDocumentodeterminacions()) {
					aElement = aXML.createElement("DOCUMENTO");
					aElement.setAttribute("codigo",
							format10.format(doc.getDocumento().getIden()));
					aNodo.appendChild(aElement);
				}
				aRoot.appendChild(aNodo);
			}

			if (getUnidad(det) != null) {
				aRoot.appendChild(determinacionCodigoFIP(aXML, "UNIDAD",
						getUnidad(det)));
			}

			if (getRegulacionEspecifica(det).size() > 0
					|| getDeterminacionesReguladoras(det).size() > 0) {
				aNodo = aXML.createElement("REGULACION");
				if (getDeterminacionesReguladoras(det).size() > 0) {
					aNodo2 = aXML.createElement("DETERMINACIONES-REGULADORAS");
					for (Determinacion detReg : getDeterminacionesReguladoras(det)) {
						aNodo2.appendChild(determinacionCodigoFIP(aXML,
								"DETERMINACION-REGULADORA", detReg));
					}
					aNodo.appendChild(aNodo2);
					aNodo2 = null;
				}
				if (getRegulacionEspecifica(det).size() > 0) {
					aNodo2 = aXML.createElement("REGULACIONES-ESPECIFICAS");
					for (RegulacionEspecifica regEsp : getRegulacionEspecifica(det)) {
						aNodo2.appendChild(regEsp.FIP(aXML));
					}
					aNodo.appendChild(aNodo2);
					aNodo2 = null;
				}
				aRoot.appendChild(aNodo);
			}

			if (det.getDeterminacionByIddeterminacionbase() != null) {
				aRoot.appendChild(determinacionCodigoFIP(aXML, "BASE",
						det.getDeterminacionByIddeterminacionbase()));
			}
			if (det.getDeterminaciongrupoentidadsForIddeterminacion().size() > 0) {
				aNodo = aXML.createElement("GRUPOS-APLICACION");
				for (Determinaciongrupoentidad grp : det
						.getDeterminaciongrupoentidadsForIddeterminacion()) {
					aNodo.appendChild(determinacionCodigoFIP(aXML,
							"GRUPO-APLICACION",
							grp.getDeterminacionByIddeterminaciongrupo()));
				}
				aRoot.appendChild(aNodo);
			}

			aXMLFrag = aXML.createDocumentFragment();
			aXMLFrag.appendChild(aRoot);
			return aXMLFrag;

		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}

	}

	// FGA: Para las unidades
	public DocumentFragment addUnidadToFIP(Document aXML, Determinacion det) {
		try {

			Node aNodo;
			Element aRoot;
			Element aElement;
			DocumentFragment aXMLFrag;

			DecimalFormat format10 = new DecimalFormat("0000000000");

			aRoot = aXML.createElement("UNIDAD");

			aRoot.setAttribute("determinacion",
					format10.format(Long.parseLong(det.getCodigo())));
			aRoot.setAttribute("abreviatura", det.getNombre());

			Node aNodo2 = aXML.createElement("DEFINICION");
			aNodo2.setTextContent(det.getNombre());

			aRoot.appendChild(aNodo2);

			aXMLFrag = aXML.createDocumentFragment();
			aXMLFrag.appendChild(aRoot);
			return aXMLFrag;

		} catch (Exception ex) {
			log.debug("Error en Entidad " + ex);
			ex.printStackTrace();
			return null;
		}

	}

	public DocumentFragment entidadFIP(Document aXML, Entidad entidad) {
		try {

			Node aNodo;
			Element aRoot;
			Element aElement;
			DocumentFragment aXMLFrag;

			DecimalFormat format10 = new DecimalFormat("0000000000");

			aRoot = aXML.createElement("ENTIDAD");

			aRoot.setAttribute("codigo",
					format10.format(Long.parseLong(entidad.getCodigo().trim())));
			if (entidad.getEtiqueta() != null) {
				aRoot.setAttribute("etiqueta",
						String.valueOf(entidad.getEtiqueta()));
			}

			aRoot.setAttribute("clave", String.valueOf(entidad.getClave()));
			aRoot.setAttribute("nombre", String.valueOf(entidad.getNombre()));
			aRoot.setAttribute("suspendida",
					String.valueOf(entidad.isBsuspendida()));

			if (entidad.getEntidadpols().size() > 0) {
				aNodo = aXML.createElement("GEOMETRIA");
				aNodo.setTextContent(entidad.getEntidadpols().iterator().next()
						.getGeom().toText());
				aRoot.appendChild(aNodo);
			} else if (entidad.getEntidadlins().size() > 0) {
				aNodo = aXML.createElement("GEOMETRIA");
				aNodo.setTextContent(entidad.getEntidadlins().iterator().next()
						.getGeom().toText());
				aRoot.appendChild(aNodo);
			} else if (entidad.getEntidadpnts().size() > 0) {
				aNodo = aXML.createElement("GEOMETRIA");
				aNodo.setTextContent(entidad.getEntidadpnts().iterator().next()
						.getGeom().toText());
				aRoot.appendChild(aNodo);
			}

			if (entidad.getEntidadsForIdpadre().size() > 0) {
				aNodo = aXML.createElement("HIJAS");
				for (Entidad entHija : entidad.getEntidadsForIdpadre()) {
					aNodo.appendChild(entidadFIP(aXML, entHija));
				}
				aRoot.appendChild(aNodo);
			}

			if (entidad.getDocumentoentidads().size() > 0) {
				aNodo = aXML.createElement("DOCUMENTOS");
				for (Documentoentidad doc : entidad.getDocumentoentidads()) {
					aElement = aXML.createElement("DOCUMENTO");
					aElement.setAttribute("codigo",
							format10.format(doc.getDocumento().getIden()));
					aNodo.appendChild(aElement);
				}
				aRoot.appendChild(aNodo);
			}

			if (entidad.getEntidadByIdentidadbase() != null) {
				aRoot.appendChild(entidadCodigoFIP(aXML, "BASE",
						entidad.getEntidadByIdentidadbase()));
			}

			aXMLFrag = aXML.createDocumentFragment();
			aXMLFrag.appendChild(aRoot);
			return aXMLFrag;

		} catch (Exception ex) {
			log.debug("Error en Entidad " + ex);
			ex.printStackTrace();
			return null;
		}

	}

	public DocumentFragment entidadCodigoFIP(Document aXML, String Nombre,
			Entidad ent) {
		try {

			Element aRoot;
			DocumentFragment aXMLFrag;

			DecimalFormat format10 = new DecimalFormat("0000000000");

			aRoot = aXML.createElement(Nombre);

			aRoot.setAttribute("tramite", ent.getTramite().getCodigofip());
			aRoot.setAttribute("entidad",
					format10.format(Long.parseLong(ent.getCodigo())));
			aXMLFrag = aXML.createDocumentFragment();
			aXMLFrag.appendChild(aRoot);
			return aXMLFrag;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public DocumentFragment documentoFIP(Document aXML, Documento aDocumento) {
		try {

			Node aNodo;
			Element aRoot;
			DocumentFragment aXMLFrag;
			DecimalFormat format10 = new DecimalFormat("0000000000");
			DecimalFormat format6 = new DecimalFormat("000000");
			aRoot = aXML.createElement("DOCUMENTO");
			if (aDocumento.getIdgrupodocumento() != null) {
				aRoot.setAttribute("grupo",
						format6.format(aDocumento.getIdgrupodocumento()));
			}
			aRoot.setAttribute("tipo",
					format6.format(aDocumento.getIdtipodocumento()));
			if (aDocumento.getEscala() != null) {
				aRoot.setAttribute("escala",
						String.valueOf(aDocumento.getEscala()));
			}

			// Establezco la ruta inicial que tendra el archivo dependiendo del
			// grupo
			String preruta = "";
			
			// FGA: Pongo esta preruta para los documentos ya que se almacenan asi en el editor
			preruta = aDocumento.getTramite().getIden()+File.separator+aDocumento.getIdgrupodocumento()+File.separator;
			
			// FGA: Lo comento para canarias puesto en el a BD directamente ya ponemos la preruta
				/*
			preruta ="";
			if (aDocumento.getIdgrupodocumento() == 1) {
				preruta = "TNP\\";
			}

			if (aDocumento.getIdgrupodocumento() == 2) {
				preruta = "EA\\";
			}

			if (aDocumento.getIdgrupodocumento() == 3) {
				preruta = "PIP\\";
			}

			if (aDocumento.getIdgrupodocumento() == 4) {
				preruta = "POP\\";
			}

			if (aDocumento.getIdgrupodocumento() == 5) {
				preruta = "TIP\\";
			}

			if (aDocumento.getIdgrupodocumento() == 6) {
				preruta = "SJ\\";
			}

			if (aDocumento.getIdgrupodocumento() == 7) {
				preruta = "TNI\\";
			}

			if (aDocumento.getIdgrupodocumento() == 8) {
				preruta = "POI\\";
			}

			if (aDocumento.getIdgrupodocumento() == 9) {
				preruta = "TII\\";
			}

			if (aDocumento.getIdgrupodocumento() == 10) {
				preruta = "PII\\";
			}
			*/

			aRoot.setAttribute("archivo",
					preruta + String.valueOf(aDocumento.getArchivo()));
			aRoot.setAttribute("nombre", String.valueOf(aDocumento.getNombre()));
			aRoot.setAttribute("codigo", format10.format(aDocumento.getIden()));

			if (aDocumento.getComentario() != null
					&& !aDocumento.getComentario().trim().equals("")) {
				aNodo = aXML.createElement("COMENTARIO");
				aNodo.setTextContent(aDocumento.getComentario());
				aRoot.appendChild(aNodo);
			}

			if (aDocumento.getDocumentoshps().size() > 0) {
				aNodo = aXML.createElement("HOJAS");
				for (Documentoshp aDocumentoshp : aDocumento.getDocumentoshps()) {
					aNodo.appendChild(documentoShpFIP(aXML, aDocumentoshp));
				}
				aRoot.appendChild(aNodo);
			}
			aXMLFrag = aXML.createDocumentFragment();
			aXMLFrag.appendChild(aRoot);
			return aXMLFrag;

		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}

	}

	public DocumentFragment documentoShpFIP(Document aXML,
			Documentoshp aDocumentoshp) {

		try {
			Node aNodo;
			Element aRoot;
			DocumentFragment aXMLFrag;

			aRoot = aXML.createElement("HOJA");
			aRoot.setAttribute("nombre",File.separator+"PG"+File.separator+
					String.valueOf(aDocumentoshp.getHoja()));

			if (aDocumentoshp.getGeom() != null) {
				aNodo = aXML.createElement("GEOMETRIA");
				aNodo.setTextContent(aDocumentoshp.getGeom().toString());
				aRoot.appendChild(aNodo);
			}

			aXMLFrag = aXML.createDocumentFragment();
			aXMLFrag.appendChild(aRoot);
			return aXMLFrag;

		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public DocumentFragment entidadDeterminacionFIP(Document aXML,
			Entidaddeterminacion entdet) {
		try {

			DocumentFragment aXMLFrag;
			Node aNodo;
			Element aRoot;

			aRoot = aXML.createElement("CONDICION-URBANISTICA");

			aRoot.appendChild(entidadDeterminacionCodigoFIP(aXML, "CODIGO",
					entdet));

			if (entdet.getCasoentidaddeterminacions().size() > 0) {
				aNodo = aXML.createElement("CASOS");
				for (Casoentidaddeterminacion aCaso : entdet
						.getCasoentidaddeterminacions()) {
					aNodo.appendChild(casoFIP(aXML, aCaso));
				}
				aRoot.appendChild(aNodo);
			}

			aXMLFrag = aXML.createDocumentFragment();
			aXMLFrag.appendChild(aRoot);
			return aXMLFrag;

		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}

	}

	public DocumentFragment entidadDeterminacionCodigoFIP(Document aXML,
			String Nombre, Entidaddeterminacion entdet) {
		try {
			Element aNodo;
			Element aRoot;
			DocumentFragment aXMLFrag;

			DecimalFormat format10 = new DecimalFormat("0000000000");

			aRoot = aXML.createElement(Nombre);

			aNodo = aXML.createElement("ENTIDAD");
			aNodo.setAttribute("tramite", entdet.getEntidad().getTramite()
					.getCodigofip());
			aNodo.setAttribute("entidad", format10.format(Long.parseLong(entdet
					.getEntidad().getCodigo())));
			aRoot.appendChild(aNodo);
			aNodo = null;

			aNodo = aXML.createElement("DETERMINACION");
			aNodo.setAttribute("tramite", entdet.getDeterminacion()
					.getTramite().getCodigofip());
			aNodo.setAttribute("determinacion", format10.format(Long
					.parseLong(entdet.getDeterminacion().getCodigo())));
			aRoot.appendChild(aNodo);
			aNodo = null;

			aXMLFrag = aXML.createDocumentFragment();
			aXMLFrag.appendChild(aRoot);
			return aXMLFrag;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public DocumentFragment casoFIP(Document aXML, Casoentidaddeterminacion caso) {
		try {

			DocumentFragment aXMLFrag;
			Node aNodo;
			Element aElement;
			Element aRoot;
			DecimalFormat format10 = new DecimalFormat("0000000000");

			aRoot = aXML.createElement("CASO");
			aRoot.setAttribute("nombre", caso.getNombre());
			aRoot.setAttribute("codigo", format10.format(caso.getIden()));

			// aRoot.appendChild(this.CodigoFIP(aXML,"CODIGO"));

			if (caso.getEntidaddeterminacionregimensForIdcaso().size() > 0) {
				aNodo = aXML.createElement("REGIMENES");
				for (Entidaddeterminacionregimen reg : caso
						.getEntidaddeterminacionregimensForIdcaso()) {
					aNodo.appendChild(entidadDeterminacionRegimenFIP(aXML, reg));
				}
				aRoot.appendChild(aNodo);
			}

			if (caso.getVinculocasosForIdcaso().size() > 0) {
				aNodo = aXML.createElement("VINCULOS");
				for (Vinculocaso vinc : caso.getVinculocasosForIdcaso()) {
					aNodo.appendChild(casoEntidadDeterminacionCodigoFIP(aXML,
							"VINCULO",
							vinc.getCasoentidaddeterminacionByIdcasovinculado()));
				}
				aRoot.appendChild(aNodo);
			}

			if (caso.getDocumentocasos().size() > 0) {
				aNodo = aXML.createElement("DOCUMENTOS");
				for (Documentocaso doc : caso.getDocumentocasos()) {
					aElement = aXML.createElement("DOCUMENTO");
					aElement.setAttribute("codigo",
							format10.format(doc.getDocumento().getIden()));
					aNodo.appendChild(aElement);
				}
				aRoot.appendChild(aNodo);
			}

			aXMLFrag = aXML.createDocumentFragment();
			aXMLFrag.appendChild(aRoot);
			return aXMLFrag;

		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}

	}

	public DocumentFragment regimenEspecificoFIP(Document aXML,
			Regimenespecifico regesp) {
		try {
			Node aNodo;
			Element aRoot;
			Element aElement;
			DocumentFragment aXMLFrag;

			aRoot = aXML.createElement("REGIMEN-ESPECIFICO");

			aRoot.setAttribute("orden", String.valueOf(regesp.getOrden()));
			aRoot.setAttribute("nombre", String.valueOf(regesp.getNombre()));

			if (regesp.getTexto() != null && !regesp.getTexto().equals("")) {
				aNodo = aXML.createElement("TEXTO");
				aNodo.setTextContent(regesp.getTexto());
				aRoot.appendChild(aNodo);
			}

			if (regesp.getRegimenespecificos().size() > 0) {
				aNodo = aXML.createElement("HIJOS");
				for (Regimenespecifico regHijo : regesp.getRegimenespecificos()) {
					aNodo.appendChild(regimenEspecificoFIP(aXML, regHijo));
				}
				aRoot.appendChild(aNodo);
			}

			aXMLFrag = aXML.createDocumentFragment();
			aXMLFrag.appendChild(aRoot);
			return aXMLFrag;

		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}

	}

	public DocumentFragment entidadDeterminacionRegimenFIP(Document aXML,
			Entidaddeterminacionregimen regimen) {
		try {

			DocumentFragment aXMLFrag;
			Node aNodo;
			Element aElement;
			Element aRoot;

			if (regimen.getIden() == 3770) {
				log.debug("");
			}

			aRoot = aXML.createElement("REGIMEN");

			if (regimen.getSuperposicion() != null) {
				aRoot.setAttribute("superposicion",
						String.valueOf(regimen.getSuperposicion()));
			}

			if (regimen.getRegimenespecificos().size() > 0) {
				aNodo = aXML.createElement("REGIMENES-ESPECIFICOS");
				for (Regimenespecifico reg : regimen.getRegimenespecificos()) {

					aNodo.appendChild(regimenEspecificoFIP(aXML, reg));
				}
				aRoot.appendChild(aNodo);
			}

			if (regimen.getValor() != null
					&& !regimen.getValor().trim().equals("")) {
				aNodo = aXML.createElement("VALOR");
				aNodo.setTextContent(regimen.getValor());
				aRoot.appendChild(aNodo);
				aNodo = null;
			} else if (regimen.getOpciondeterminacion() != null) {
				aRoot.appendChild(determinacionCodigoFIP(aXML,
						"VALOR-REFERENCIA", regimen.getOpciondeterminacion()
								.getDeterminacionByIddeterminacionvalorref()));
			}

			if (regimen.getDeterminacion() != null) {
				aRoot.appendChild(determinacionCodigoFIP(aXML,
						"DETERMINACION-REGIMEN", regimen.getDeterminacion()));
			}

			if (regimen.getCasoentidaddeterminacionByIdcasoaplicacion() != null) {
				aRoot.appendChild(casoEntidadDeterminacionCodigoFIP(aXML,
						"CASO-APLICACION",
						regimen.getCasoentidaddeterminacionByIdcasoaplicacion()));
			}

			aXMLFrag = aXML.createDocumentFragment();
			aXMLFrag.appendChild(aRoot);
			return aXMLFrag;

		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}

	}

	public DocumentFragment determinacionCodigoFIP(Document aXML,
			String Nombre, Determinacion det) {
		try {

			Element aRoot;
			DocumentFragment aXMLFrag;

			DecimalFormat format10 = new DecimalFormat("0000000000");

			aRoot = aXML.createElement(Nombre);

			aRoot.setAttribute("tramite", det.getTramite().getCodigofip());
			aRoot.setAttribute("determinacion",
					format10.format(Long.parseLong(det.getCodigo())));
			aXMLFrag = aXML.createDocumentFragment();
			aXMLFrag.appendChild(aRoot);
			return aXMLFrag;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public DocumentFragment casoEntidadDeterminacionCodigoFIP(Document aXML,
			String Nombre, Casoentidaddeterminacion caso) {
		try {
			Element aNodo;
			Element aRoot;

			DocumentFragment aXMLFrag;

			DecimalFormat format10 = new DecimalFormat("0000000000");

			aRoot = aXML.createElement(Nombre);

			aRoot.setAttribute("tramite", caso.getEntidaddeterminacion()
					.getEntidad().getTramite().getCodigofip());
			aRoot.setAttribute("caso", format10.format(caso.getIden()));

			aXMLFrag = aXML.createDocumentFragment();
			aXMLFrag.appendChild(aRoot);
			return aXMLFrag;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	private Node addCondicionesToFIP(Entidad aEnt, Node aNodoCon, Document aXML) {

		try {
			if (aEnt.getEntidaddeterminacions().size() > 0) {
				if (aNodoCon == null) {
					aNodoCon = aXML.createElement("CONDICIONES-URBANISTICAS");
				}
				for (Entidaddeterminacion aCond : aEnt
						.getEntidaddeterminacions()) {
					aNodoCon.appendChild(entidadDeterminacionFIP(aXML, aCond));
				}
			}

			return aNodoCon;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	private Node addUnidadesToFIP(Determinacion aDet, Node aNodoCon,
			Document aXML) {

		aNodoCon = aXML.createElement("UNIDADES");

		try {
			if (getPropiedadesUnidad(aDet) != null) {
				if (aNodoCon == null) {
					aNodoCon = aXML.createElement("UNIDADES");
				}
				aNodoCon.appendChild(propiedadesUnidadFIP(aXML, "UNIDAD",
						getPropiedadesUnidad(aDet)));
			}

			return aNodoCon;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}

	}

	public DocumentFragment propiedadesUnidadFIP(Document aXML, String nombre,
			PropiedadesUnidad propUnidad) {
		try {
			Node aNodo;
			Element aRoot;
			Element aElement;
			DocumentFragment aXMLFrag;
			DecimalFormat format10 = new DecimalFormat("0000000000");

			aRoot = aXML.createElement(nombre);

			aRoot.setAttribute("determinacion", format10.format(Long
					.parseLong(propUnidad.getDeterminacion().getCodigo())));
			aRoot.setAttribute("abreviatura", propUnidad.getAbreviatura());

			if (propUnidad.getDefinicion() != null
					&& !propUnidad.getDefinicion().trim().equals("")) {
				aNodo = aXML.createElement("DEFINICION");
				aNodo.setTextContent(propUnidad.getDefinicion());
				aRoot.appendChild(aNodo);
			}

			aXMLFrag = aXML.createDocumentFragment();
			aXMLFrag.appendChild(aRoot);
			return aXMLFrag;

		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}

	}

	public PropiedadesUnidad getPropiedadesUnidad(Determinacion determinacion) {
		PropiedadesUnidad aProps;
		String s;
		Query aQuery;
		List<Object[]> Resultado;

		try {

			Integer IdDefPropUnidad = Integer.parseInt(Textos.getCadena(
					"diccionario", "unidad.id_definicion_unidad"));
			Integer IdDeterminacion = Integer.parseInt(Textos.getCadena(
					"diccionario", "unidad.id_determinacion"));
			Integer IdDefAbreviatura = Integer.parseInt(Textos.getCadena(
					"diccionario", "unidad.id_abreviatura"));
			Integer IdDefDefinicion = Integer.parseInt(Textos.getCadena(
					"diccionario", "unidad.id_definicion"));

			s = "Select r.iden,"
					+ "(select valor from planeamiento.Propiedadrelacion where iddefpropiedad ="
					+ IdDefAbreviatura
					+ " and idrelacion=r.iden ) as abreviatura,"
					+ "(select valor from planeamiento.Propiedadrelacion where iddefpropiedad ="
					+ IdDefDefinicion
					+ " and idrelacion=r.iden ) as definicion "
					+ " From planeamiento.Relacion r, planeamiento.Vectorrelacion vr "
					+ " Where r.iddefrelacion=" + IdDefPropUnidad
					+ " And vr.iddefvector=" + IdDeterminacion
					+ " And vr.valor=" + determinacion.getIden()
					+ " And vr.idrelacion=r.iden ";

			aQuery = em.createNativeQuery(s);

			Resultado = aQuery.getResultList();
			aProps = null;
			if (Resultado.size() > 0) {
				for (Object[] aUnitsAds : Resultado) {
					aProps = new PropiedadesUnidad(
							Integer.parseInt(aUnitsAds[0].toString()),
							aUnitsAds[1].toString(), determinacion,
							aUnitsAds[2].toString());
				}
			}

			return aProps;

		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

}
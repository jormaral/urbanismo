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

package com.mitc.redes.editorfip.servicios.procesamientofip.obtencionfip1;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;

import org.jboss.seam.log.Log;
import org.jboss.seam.log.Logging;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.mitc.redes.editorfip.entidades.rpm.diccionario.Ambito;
import com.mitc.redes.editorfip.entidades.rpm.diccionario.Ambitoshp;
import com.mitc.redes.editorfip.entidades.rpm.diccionario.Caracterdeterminacion;
import com.mitc.redes.editorfip.entidades.rpm.diccionario.Defrelacion;
import com.mitc.redes.editorfip.entidades.rpm.diccionario.Grupodocumento;
import com.mitc.redes.editorfip.entidades.rpm.diccionario.Instrumentoplan;
import com.mitc.redes.editorfip.entidades.rpm.diccionario.Literal;
import com.mitc.redes.editorfip.entidades.rpm.diccionario.Operacioncaracter;
import com.mitc.redes.editorfip.entidades.rpm.diccionario.Organigramaambito;
import com.mitc.redes.editorfip.entidades.rpm.diccionario.Tipoambito;
import com.mitc.redes.editorfip.entidades.rpm.diccionario.Tipodefpropiedad;
import com.mitc.redes.editorfip.entidades.rpm.diccionario.Tipodocumento;
import com.mitc.redes.editorfip.entidades.rpm.diccionario.Tipoentidad;
import com.mitc.redes.editorfip.entidades.rpm.diccionario.Tipooperaciondeterminacion;
import com.mitc.redes.editorfip.entidades.rpm.diccionario.Tipooperacionentidad;
import com.mitc.redes.editorfip.entidades.rpm.diccionario.Tipotramite;
import com.mitc.redes.editorfip.excepciones.EditorFIPException;

public class ImportadorDiccionario extends ImportadorBase {
	
	Log log = Logging.getLog(ImportadorDiccionario.class);
	
	Utiles resultado;

	// Mapas
	Map<Integer,Object> mapTipoEntidad;
	Map<Integer,Object> mapTipoAmbito;
	Map<Integer,Object> mapTipoOperacionDeterminacion;
	Map<Integer,Caracterdeterminacion> mapCaracterDeterminacion;
	
	// Contadores
	private int countAmbitos = 0;
	private int countOrgAmitos = 0;
	private int countCarDet = 0;
	private int countTipoOpEnt = 0;
	private int countOpCar = 0;
	private int countInstrumentos = 0;
	private int countGeneral = 0;
	
	
	
	/*
	 * Constructor
	 */
	public ImportadorDiccionario(Document xml, EntityManager em, Utiles resultado) {
		super(xml, em);
		debugPrefix = "Diccionario:";
		this.resultado = resultado;
	}
	
	@Override
	protected void importar() {
		
		// Empezar la importación
		try {
			
			// -----| DEFINICIONES |-----
			// Esto no viene del XML sino que son impuestas por la BD
			//crearDefiniciones();
			
			// -----| TIPO AMBITOS |-----
			mapTipoAmbito = importarTiposGeneral("TIPO-AMBITO", Tipoambito.class);
			resultado.info("DICCIONARIO->TIPO-AMBITO: " + countGeneral);
			
			// -----| AMBITOS |-----
			importarAmbitos();
			resultado.info("DICCIONARIO->AMBITO: " + countAmbitos);
			
			// -----| ORGANIGRAMA AMBITOS |-----
			importarOrganigramaAmbitos();
			resultado.info("DICCIONARIO->ORGANIGRAMA-AMBITO: " + countOrgAmitos);
			
			// -----| CARACTERES DETERMINACION |-----
			importarCaracteresDeterminacion();
			resultado.info("DICCIONARIO->CARACTERES-DETERMINACION: " + countCarDet);
			
			// -----| TIPO ENTIDAD |-----
			mapTipoEntidad = importarTiposGeneral("TIPO-ENTIDAD", Tipoentidad.class);
			resultado.info("DICCIONARIO->TIPO-ENTIDAD: " + countGeneral);
			
			// -----| TIPOS OPERACION ENTIDAD |-----
			importarTiposOperacionEntidad();
			resultado.info("DICCIONARIO->OPERACION-ENTIDAD: " + countTipoOpEnt);
			
			// -----| TIPO OPERACION DETERMINACION |-----
			mapTipoOperacionDeterminacion = importarTiposGeneral(
					"TIPO-OPERACION-DETERMINACION", 
					Tipooperaciondeterminacion.class
			);
			resultado.info("DICCIONARIO->TIPO-OPERACION-DETERMINACION: " + countGeneral);
			
			// -----| OPERACIONES CARACTERES |-----
			importarOperacionesCaracteres();
			resultado.info("DICCIONARIO->OPERACION-CARACTER: " + countOpCar);
			
			// -----| TIPO DOCUMENTO |-----
			importarTiposGeneral("TIPO-DOCUMENTO", Tipodocumento.class);
			resultado.info("DICCIONARIO->TIPO-DOCUMENTO: " + countGeneral);
			
			// -----| GRUPO DOCUMENTO |-----
			importarTiposGeneral("GRUPO-DOCUMENTO", Grupodocumento.class);
			resultado.info("DICCIONARIO->GRUPO-DOCUMENTO: " + countGeneral);
			
			// -----| TIPO TRAMITE |-----
			importarTiposGeneral("TIPO-TRAMITE", Tipotramite.class);
			resultado.info("DICCIONARIO->TIPO-TRAMITE: " + countGeneral);
			
			// -----| INSTRUMENTOS |-----
			importarInstrumento();
			resultado.info("DICCIONARIO->INSTRUMENTOS: " + countInstrumentos);
			

			
		} catch (Exception e) {
			resultado.error("Error desconocido importando DICCIONARIO, error: "+e.getMessage());
			log.error(e.getMessage());
		}
	}
	
	/* ........................................................................................
	 * 
	 *  Importar Ámbitos
	 *  
	 */
	private void importarAmbitos() throws EditorFIPException {
		
		// Coger los nodos tipo-ambito
		NodeList nodes = evaluateXPath("//AMBITOS/AMBITO");	
		log.info("Importando "+nodes.getLength()+" Ambitos ...");
		
		// Vars
		Literal literal;
		Node node;
		NodeList children;
		Tipoambito tipoAmbito;
		Ambitoshp ambitoShp=null;
		Integer iden=null;
		
		// Por cada nodo AMBITO ...
	    for (int i = 0; i < nodes.getLength(); i++) {
	    	
	    	try {
	    		
		    	// Nodo y mapeamos atributos
		    	node = nodes.item(i);
		    	attrMap = nodeToMap( node );
	
		    	// Entidad - AMBITO
	 	    	Ambito ambito;
		    	ambito = new Ambito();
	
		    	// Código y codigo IME
		    	iden = Integer.parseInt( (String) attrMap.get("codigo"));
		    	ambito.setIden( iden );
		    	ambito.setCodigoine((String) attrMap.get("ine") );
		    	
		    	// Nombre
		    	literal = new Literal();
		    	literal.setComentario((String) attrMap.get("nombre"));
		    	ambito.setLiteral(literal);  
		    	
		    	// Mirar si el ámbito tiene geometría
		    	children = node.getChildNodes();
		    	if (children.getLength()>0) {
		    		
		    		Node geoNode = children.item(1);
		    		
		    		ambitoShp = new Ambitoshp();
		    		ambitoShp.setGeom(geoNode.getNodeValue());
		    		ambitoShp.setIdambito(iden);
		    		//debug("Importando geometría para el ambito con codigo " + iden);
		    		
		    	} else {
		    		if (i%100==0) log.info("Importando 100 AMBITOS (#"+i+"), último código " + iden);
		    	}
		    	
		    	// Id Tipo Ambito
		    	int idTipoAmbito = Integer.parseInt( (String) attrMap.get("tipoAmbito")); 
		    	tipoAmbito = (Tipoambito) mapTipoAmbito.get(idTipoAmbito);
		    	ambito.setTipoambito(tipoAmbito);
	    	
		    	// Persistiendo AMBITO
	    		em.persist(ambito);
	    		countAmbitos++;
	    		if (ambitoShp!=null) em.persist(ambitoShp);
	    		
	    	} catch (Exception e) {
	    		resultado.error("Error desconocido persistiendo AMBITO con codigo: " + iden+", error: "+e.getMessage());
	    		e.printStackTrace();
	    	}
	    }
	}	
	
	/* ........................................................................................
	 * 
	 *  Importar Organigrama Ámbitos
	 *  
	 */
	private void importarOrganigramaAmbitos() throws EditorFIPException {
	
		// Coger los nodos tipo-ambito
		NodeList nodes = evaluateXPath("//ORGANIGRAMA-AMBITOS/RELACION");	
		log.info("Importando "+nodes.getLength()+" Organigramas-Ambitos ...");
		
		// Vars 
		int idPadre, idHijo;
		Ambito ambitoPadre, ambitoHijo;
		
		// Por cada nodo RELACION ...
	    for (int i = 0; i < nodes.getLength(); i++) {
	    	
	    	try {
	    		
		    	// Mapeamos atributos del nodo
		    	attrMap = nodeToMap( nodes.item(i));
	
		    	idPadre = Integer.parseInt( (String) attrMap.get("PADRE"));
		    	idHijo = Integer.parseInt( (String) attrMap.get("HIJO"));
		    	
		    	// Entidad - OrganibramaAmbito
		    	Organigramaambito orgAmbito = new Organigramaambito();
		    	ambitoHijo = em.find(Ambito.class, idHijo);
		    	if (ambitoHijo==null) throw new EditorFIPException("Ambito no encontrado, codigo: " + idHijo);
		    	ambitoPadre = em.find(Ambito.class, idPadre);
		    	if (ambitoPadre==null) throw new EditorFIPException("Ambito no encontrado, codigo: " + idPadre);
		    	orgAmbito.setAmbitoByIdambitohijo(ambitoHijo);
		    	orgAmbito.setAmbitoByIdambitopadre(ambitoPadre);
		    	
		    	if (i % 100 == 0) log.info("Persistiendo 100 ORGANISMO-AMBITO (#"+i+")");
		    	em.persist(orgAmbito);
		    	countOrgAmitos++;
		    	
	    	} catch (Exception e) {
	    		resultado.error("Error desconocido persistiendo RELACION, error: "+e.getMessage());
	    		e.printStackTrace();
	    	}
	    }
	}
	
	/* ........................................................................................
	 * 
	 *  Importar CARACTERES-DETERMINACION
	 *  
	 */
	private void importarCaracteresDeterminacion() {

		// Coger los nodos
		NodeList nodes = evaluateXPath("//CARACTERES-DETERMINACION/CARACTER-DETERMINACION");
		log.info("Importando "+nodes.getLength()+" Caracteres-determinacion ...");
		
		// Crear mapa para resultado
		mapCaracterDeterminacion = new HashMap<Integer, Caracterdeterminacion>();
		
		// Por cada nodo ...	
		Caracterdeterminacion carDet;
		int iden;
		Literal literal;
	    for (int i = 0; i < nodes.getLength(); i++) {
	    	
	    	try {
	    		
		    	// Mapeamos atributos del nodo
		    	attrMap = nodeToMap( nodes.item(i));
		    	
		    	// Codigo
		    	carDet = new Caracterdeterminacion();
		    	iden = Integer.parseInt( (String) attrMap.get("codigo"));
		    	carDet.setIden(iden);
		    	
		    	// Nombre
		    	literal = new Literal();
		    	literal.setComentario((String)attrMap.get("nombre"));
		    	carDet.setLiteral(literal);
		    	
		    	// Aplicaciones max y min
		    	try {
		    		int nmax = Integer.parseInt( (String) attrMap.get("aplicaciones_max"));
		    		carDet.setNmaxaplicaciones(nmax);
		    	} catch (NumberFormatException e) {
		    		// No hacer nada, saltar
		    	}
		    	try {
			    	int nmin = Integer.parseInt( (String) attrMap.get("aplicaciones_min"));
			    	carDet.setNminaplicaciones(nmin);
		    	} catch (NumberFormatException e) {
		    		// No hacer nada, saltar
		    	}
		    	
		    	// Persistir
		    	//debug("Persistiendo CARACTER-DETERMINACION, codigo: " + iden);
		    	em.persist(carDet);
		    	countCarDet++;
		    	
		    	// Añadir al mapa
		    	mapCaracterDeterminacion.put(iden, carDet);
		    	
		    } catch (Exception e) {
		    	resultado.error("Error desconocido persistiendo CARACTER-DETERMINACION, error: "+e.getMessage());
	    		e.printStackTrace();
	    	}
	    }
	}
	
	/* ........................................................................................
	 * 
	 *  Importar TIPOS-OPERACION-ENTIDAD
	 *  
	 */
	private void importarTiposOperacionEntidad() {

		// Coger los nodos
		NodeList nodes = evaluateXPath("//TIPOS-OPERACION-ENTIDAD/TIPO-OPERACION-ENTIDAD");
		log.info("Importando "+nodes.getLength()+" TIPOS-OPERACION-ENTIDAD ...");
		
		// Por cada nodo ...	
		Tipooperacionentidad tipoOpEnt;
		int iden, tipoEntidad;
		Literal literal;
	    for (int i = 0; i < nodes.getLength(); i++) {
	    	
	    	try {
	    		
		    	// Mapeamos atributos del nodo
		    	attrMap = nodeToMap( nodes.item(i));
		    	
		    	// Codigo
		    	tipoOpEnt = new Tipooperacionentidad();
		    	iden = Integer.parseInt( (String) attrMap.get("codigo"));
		    	tipoOpEnt.setIden(iden);
		    	
		    	// Nombre
		    	literal = new Literal();
		    	literal.setComentario((String)attrMap.get("nombre"));
		    	tipoOpEnt.setLiteral(literal);
		    	
		    	// Tipo entidad
		    	tipoEntidad = Integer.parseInt( (String) attrMap.get("tipoEntidad"));
		    	tipoOpEnt.setTipoentidad( (Tipoentidad) mapTipoEntidad.get(tipoEntidad));
		    	
		    	//debug("Persistiendo TIPO-OPERACION-ENTIDAD, codigo: " + iden);
		    	em.persist(tipoOpEnt); 
		    	countTipoOpEnt++;
		    	
		    } catch (Exception e) {
		    	resultado.error("Error desconocido persistiendo TIPO-OPERACION-ENTIDAD, error: "+e.getMessage());
	    		e.printStackTrace();
	    	}
	    }
	}
	

	/* ........................................................................................
	 * 
	 *  Importar OPERACIONES-CARACTERES
	 *  
	 */
	private void importarOperacionesCaracteres() {

		// Coger los nodos
		NodeList nodes = evaluateXPath("//OPERACIONES-CARACTERES/OPERACION-CARACTER");
		log.info("Importando "+nodes.getLength()+" OPERACION-CARACTER ...");
		
		// Por cada nodo ...	
		Operacioncaracter opCar;
		int operadora, operada, operacion;
	    for (int i = 0; i < nodes.getLength(); i++) {
	    	
	    	try {
	    		
		    	// Mapeamos atributos del nodo
		    	attrMap = nodeToMap( nodes.item(i));
		    	opCar = new Operacioncaracter();
		    	
		    	// Caracter operada
		    	operadora = Integer.parseInt( (String) attrMap.get("CaracterOperadora"));
		    	opCar.setCaracterdeterminacionByIdcaracteroperado(mapCaracterDeterminacion.get(operadora));
		    	
		    	// Caracter operadora
		    	operada = Integer.parseInt( (String) attrMap.get("CaracterOperada"));
		    	opCar.setCaracterdeterminacionByIdcaracteroperador(mapCaracterDeterminacion.get(operada));
		    	
		    	// Tipo operacion determinacion
		    	operacion = Integer.parseInt( (String) attrMap.get("TipoOperacionDeterminacion"));
		    	opCar.setTipooperaciondeterminacion( 
		    			(Tipooperaciondeterminacion) mapTipoOperacionDeterminacion.get(operacion));
		    	
		    	//debug("Persistiendo OPERACION-CARACTER");
		    	em.persist(opCar); 
		    	countOpCar++;
		    	
		    } catch (Exception e) {
		    	resultado.error("Error desconocido persistiendo OPERACION-CARACTER, error: "+e.getMessage());
	    		e.printStackTrace();
	    	}
	    }
	}
	
	/* ........................................................................................
	 * 
	 *  Importar INSTRUMENTO
	 *  
	 */
	private void importarInstrumento() {

		// Coger los nodos
		NodeList nodes = evaluateXPath("//INSTRUMENTOS/INSTRUMENTO");
		log.info("Importando "+nodes.getLength()+" INSTRUMENTO ...");
		
		// Por cada nodo ...	
		Instrumentoplan instrumento;
		int iden;
		Literal literal;
		String nombre;
	    for (int i = 0; i < nodes.getLength(); i++) {
	    	
	    	try {
	    		
		    	// Mapeamos atributos del nodo
		    	attrMap = nodeToMap( nodes.item(i));
		    	
		    	// Codigo
		    	instrumento = new Instrumentoplan();
		    	iden = Integer.parseInt( (String) attrMap.get("codigo"));
		    	instrumento.setIden(iden);
		    	
		    	// Nombre
		    	literal = new Literal();
		    	nombre = (String)attrMap.get("nombre");
		    	literal.setComentario(nombre);
		    	instrumento.setLiteral(literal);
		    	
		    	// Nemo
		    	instrumento.setNemo(nombre.substring(0,Math.min(11, nombre.length())));
		    	
		    	//debug("Persistiendo INSTRUMENTO, codigo: " + iden);
		    	em.persist(instrumento); 
		    	countInstrumentos++;
		    	
		    } catch (Exception e) {
		    	resultado.error("Error desconocido persistiendo INSTRUMENTO, error: "+e.getMessage());
	    		e.printStackTrace();
	    	}
	    }
	}
	
	
	/* ........................................................................................
	 * 
	 *  Importador general de tipos
	 *  
	 *  Ya que muchas de las conversiones son muy parecidas teniendo solo
	 *  codigo y nombre pues creamos una funcion general para todas
	 *  
	 *  http://www.rgagnon.com/javadetails/java-0351.html
	 *  
	 */
	private Map<Integer,Object> importarTiposGeneral(String entidadXML, Class<?> clase) {

		// Coger los nodos
		NodeList nodes = evaluateXPath("//" + entidadXML);
		log.info("Importando "+nodes.getLength()+" "+entidadXML+" ...");
		
		// Vars
		Map<Integer,Object> mapa = new HashMap<Integer, Object>();
		Object obj;
		Integer iden;
		Literal literal;
		Constructor<?> constructor;
		Class<?>[] argsClass;
		Method objMethod;
		Object[] args;
		countGeneral = 0;
		
		// Por cada nodo ...	
	    for (int i = 0; i < nodes.getLength(); i++) {
	    	
	    	// Mapeamos atributos del nodo
	    	attrMap = nodeToMap( nodes.item(i));
	    	
	    	try {
	    		
		    	// Crear objeto
				constructor = clase.getConstructor();
				obj = constructor.newInstance();
	
				// Codigo
				iden = Integer.parseInt( (String) attrMap.get("codigo"));
				argsClass = new Class[] { int.class };
				objMethod = clase.getMethod("setIden", argsClass);
				args = new Object[] {iden};
				objMethod.invoke(obj, args);
		    	
		    	// Nombre
		    	literal = new Literal();
		    	literal.setComentario((String)attrMap.get("nombre"));
		    	argsClass = new Class[] { Literal.class };
				objMethod = clase.getMethod("setLiteral", argsClass);
				args = new Object[] {literal};
				objMethod.invoke(obj, args);
				
				// Persistir
		    	//debug("Persistiendo "+entidadXML+", codigo: " + iden);
		    	em.persist(obj); 
		    	countGeneral++;
		    	
		    	// Añadir al mapa
		    	mapa.put(iden, obj);
		    	
	    	 } catch (Exception e) {
	    		 resultado.error("Error desconocido persistiendo "+entidadXML+", error: "+e.getMessage());
		    	e.printStackTrace();
	    	 }
	    }
	    
		return mapa;
	}
	
	
	/*
	 * 	CREAR DEFINICIONES
	 * 
	 * 	Esta función NO ESTÁ TERMINADA. Al final se ha decidido suponer que el diccionario
	 * 	lo tienen ya y no importarlo del FIP1
	 * 
	 */
	private void crearDefiniciones() {
				
		int defRelacionArray[][] = {
			{1,1,1},
			{2,0,1},
			{3,1,1,11},
			{4,0,2147483647},
			{5,0,2147483647},
			{6,0,2147483647},
			{7,0,2147483647},
			{8,0,2147483647},
			{9,0,2147483647},
			{10,0,2147483647},
			{11,0,2147483647},
			{12,0,2147483647},
			{13,0,2147483647},
			{14,0,2147483647},
			{15,0,2147483647},
			{16,0,2147483647},
			{17,0,2147483647},
			{18,0,2147483647},
			{19,0,2147483647},
			{20,0,2147483647},
			{21,0,1}
		};
		
		// Definiciones de relaciones
		for(int[] row : defRelacionArray) {
			
			Defrelacion defRelacion = new Defrelacion();
			defRelacion.setIden(row[0]);
			defRelacion.setLiteral(new Literal());
			defRelacion.setNmininstancias(row[1]);
			defRelacion.setNmaxinstancias(row[2]);
			em.persist(defRelacion);		
		}
	
		
		// Tipo Defeinicion Propiedad
		for(int i=1;i<4;i++) {
			
			Tipodefpropiedad tipoDefPropiedad = new Tipodefpropiedad();
			tipoDefPropiedad.setLiteral(new Literal());
			em.persist(tipoDefPropiedad);
		}	
		
		// .... Por hacer ...
	
	}
}

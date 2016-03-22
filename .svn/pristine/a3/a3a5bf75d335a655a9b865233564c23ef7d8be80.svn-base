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

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Plan;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Tramite;
import com.mitc.redes.editorfip.excepciones.EditorFIPException;


public class ImportadorBase {
	
	// ---| Constantes |----------------------------------------
	
	// Definición relacion
	public static final int _DEFREL_DETERAMINACION_UNIDAD_PROPIEDADES = 1;
	public static final int _DEFREL_VINCULO_DETERMINACION_UNIDAD = 2;
	public static final int _DEFREL_ADSCRIPCION_UNIDADES = 3;
	public static final int _DEFREL_TEXTO_REGULACION_DETERMINACION = 20;
	
	// Definición propiedad
	public static final int _DEFPROP_ABREVIATURA = 1;
	public static final int _DEFPROP_DEFINICION = 2;
	public static final int _DEFPROP_CUANTIA_ADSCRIPCION = 3;
	public static final int _DEFPROP_TEXTO_ADSCRIPCION = 4;
	public static final int _DEFPROP_ORDEN_PRESENTACION = 5;
	public static final int _DEFPROP_NOMBRE = 6;
	public static final int _DEFPROP_TEXTO = 7;
	
	// Definición vector
	public static final int _DEFVECT_DETERMINACION_UNIDAD_ASIGNAR_PROPIEDADES = 1;  
	public static final int _DEFVECT_VINCULO_DETERMINACION_UNIDAD_ORIGEN = 2;
	public static final int _DEFVECT_VINCULO_DETERMINACION_UNIDAD_DESTINO = 3;
	public static final int _DEFVECT_ADSCRIPCION_ENTIDAD_ORIGEN = 4;
	public static final int _DEFVECT_ADSCRIPCION_ENTIDAD_DESTINO = 5;
	public static final int _DEFVECT_ADSCRIPCION_UNIDAD= 6;
	public static final int _DEFVECT_ADSCRIPCION_TIPO= 7;
	public static final int _DEFVECT_DETERMINACION_REGULADA_40 = 40;
	public static final int _DEFVECT_REGULACION_PRECEDENTE = 41;
	
	// Diccionario -> Tipos de operacion determinacion
	public static final int _TOD_ADICION_VALOR_REFERENCIA = 8;

	
	protected Document xmlDoc;
	protected String debugPrefix = "";
	protected XPath xpath;
	protected EntityManager em;
	
	//static Logger logger = Logger.getLogger(ImportadorBase.class);
	Map<String,Object> attrMap;

	/*
	 * Constructores
	 */
	public ImportadorBase(Document xml, EntityManager emParam) {
		super();
		
		xmlDoc = xml;
		em = emParam;
		
		// Creamos objetos para el xpath
		XPathFactory factory = XPathFactory.newInstance();
	    xpath = factory.newXPath();
	}
	
	/*
	 * Evalua un XPath
	 */
	public NodeList evaluateXPath(String xpathString) { 
		NodeList nodes=null;
	    XPathExpression expr;
		try {
			expr = xpath.compile(xpathString);
		    Object result = expr.evaluate(xmlDoc, XPathConstants.NODESET);
		    nodes = (NodeList) result;
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return nodes;
	}

	
	/*
	 * 	Importa el contenido del XML en la base de datos
	 */
	protected void importar() throws EditorFIPException {};
	
	
	/*
	 * 	Importa el contenido del XML en la base de datos
	 */
	protected void importar(Date fecha,String nombreTramite) throws EditorFIPException {};
	
	protected int importar(Date fechaPV, String nombreTramite, Plan plan, String xmlPath ) throws EditorFIPException {
		return 0;
	};


	public Map<String,Object> nodeToMap(Node node ) {
		
		// Cogemos los atributos del nodo
		NamedNodeMap attrs = node.getAttributes();
		Map<String,Object> attrMap = new HashMap<String, Object>();

		// Por cada atributo lo vamos metiendo en el map
		Attr attr;
		if (attrs!=null) {
			for (int a = 0; a < attrs.getLength(); a++) {
				attr = (Attr) attrs.item(a);
				attrMap.put(attr.getName(), attr.getValue());
			}
		}
		
		return attrMap;
	}
}

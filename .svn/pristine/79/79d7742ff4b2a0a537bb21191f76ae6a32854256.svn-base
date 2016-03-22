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

package com.mitc.redes.editorfip.servicios.gestionfip.generacionfip;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.xml.parsers.DocumentBuilderFactory;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.faces.FacesManager;
import org.jboss.seam.log.Log;
import org.jboss.seam.security.Credentials;

import com.mitc.redes.editorfip.entidades.comunes.ParIdentificadorTexto;
import com.mitc.redes.editorfip.entidades.rpm.gestionfip.FipsGenerados;
import com.mitc.redes.editorfip.servicios.genericos.ArbolGenericoObject;
import com.mitc.redes.editorfip.servicios.procesamientofip.generacionfip2.ConversionFIPXML;

@Scope(ScopeType.SESSION)
@Stateful
@Name("generarFip")
public class GenerarFipBean implements GenerarFip
{
    @Logger private Log log;
    
    @PersistenceContext
	EntityManager entityManager;
    
    @In
    ConversionFIPXML conversionFIPXML;
    
    @In
	Credentials credentials;
    
    private static final String JAVA_ENCODING = "8859_1";
    
    public void generarFip(List<ArbolGenericoObject> seleccionados)
    {
    	
    	DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    	try{
	    	
	    	// Creamos un nuevo elemento e iteramos sobre los ids seleccionados
	    	//Element raizTram = document.createElement("tramites");  // creamos el elemento raiz
	    	
	    	
	    	Iterator it = seleccionados.iterator();
	    	while (it.hasNext())
	    	{
//	    		DocumentBuilder builder = factory.newDocumentBuilder();
//		    	DOMImplementation implementation = builder.getDOMImplementation();
//		    	Document document = implementation.createDocument(null, "xml", null);
//		    	
//		    	Element raiz = document.createElement("raiz");  // creamos el elemento raiz
//		    	Element elemento = document.createElement("Correo"); //creamos un nuevo elemento
//		    	Text text = document.createTextNode("ssancho@idom.com"); //Ingresamos la info
//		    	document.setXmlVersion("1.0");
//		    	document.getDocumentElement().appendChild(raiz);  //pegamos la raiz al documento
//		    	
//		    	raiz.appendChild(elemento); //pegamos el elemento hijo a la raiz
//		    	elemento.appendChild(text);
		    	
	    		ArbolGenericoObject obj = (ArbolGenericoObject) it.next();
		    	//Text textTram = document.createTextNode(obj.getDatosIdTextoArbolGenerico().getTexto());
		    	
//		    	Element elementoTram = document.createElement("tramite"); //creamos un nuevo elemento
//		    	elementoTram.appendChild(textTram);
//		    	Element raizTram = document.createElement("tramites");
//		    	raizTram.appendChild(elementoTram); // pegamos los trámites a la raiz
//		    	
//		    	document.getDocumentElement().appendChild(raizTram);
//		    	
//		    	
//		    	Transformer transformer = TransformerFactory.newInstance().newTransformer();
//		    	//transformer.transform(source, result);
//		    	//transformer.transform(source, console);
//		    	
//		    	transformer.setOutputProperty(OutputKeys.INDENT, "yes");
//		    	
//		    	// Guardamos el archivo+
//		    	StreamResult resultString = new StreamResult(new StringWriter());
//		    	Source sourceDom = new DOMSource(document);
//		    	transformer.transform(sourceDom, resultString);
//
//		    	// GUARDAMOS LOS VALORES
//		    	String xmlString = resultString.getWriter().toString();
//		    	UUID uuid = UUID.randomUUID();
//           	 	String newFileName = "fip2-" + uuid.toString() + ".xml";
           	 	
           	 	//Guardamos los objetos FIP
		    	
		    	FipsGenerados fip = conversionFIPXML.guardarFIPenBD(obj.getDatosIdTextoArbolGenerico().getIdBaseDatos());
		    	
		    	fip.setFechaGeneracion(new Date());
		    	fip.setIdTramiteEncargado(obj.getDatosIdTextoArbolGenerico().getIdBaseDatos());
		    	fip.setNombre(obj.getDatosIdTextoArbolGenerico().getTexto());
		   
		    	fip.setVersion(0);
		    	fip.setUsername(credentials.getUsername());
		    	
		    	entityManager.persist(fip);
		    	entityManager.flush();
		    	
		    	//guardarDocumentoXML(xmlString, newFileName);
		    	
	    	}
	    	
	    	//document.getDocumentElement().appendChild(raizTram);  //pegamos la raiz al documento
	    	
	    	//Source source = new DOMSource(document);
	    	//Result result = new StreamResult(new java.io.File("resultado.xml")); //nombre del archivo
	    	//Result console= new StreamResult(System.out);
	    	
//	    	Transformer transformer = TransformerFactory.newInstance().newTransformer();
//	    	//transformer.transform(source, result);
//	    	//transformer.transform(source, console);
//	    	
//	    	transformer.setOutputProperty(OutputKeys.INDENT, "yes");
//	    	
//	    	// Guardamos el archivo+
//	    	StreamResult resultString = new StreamResult(new StringWriter());
//	    	Source sourceDom = new DOMSource(document);
//	    	transformer.transform(sourceDom, resultString);
//
//	    	String xmlString = resultString.getWriter().toString();
//	    	guardarDocumentoXML(xmlString);
	    
    	}catch(Exception e){
    	
    		System.err.println("Error: "+e);
    	}
    		
    	FacesManager.instance().redirect("/gestionfip/generacionfip2/Fip2GeneradosList.xhtml");
    }
    
    public static void main(String[] args) {
    	
    	List<ArbolGenericoObject> seleccionados = new ArrayList<ArbolGenericoObject>();
    	
    	ParIdentificadorTexto datosIdTextoArbolGenerico = new ParIdentificadorTexto(1, "adsadsadsad");
    	ArbolGenericoObject obj = new ArbolGenericoObject(new DefaultMutableTreeNode());
    	obj.setDatosIdTextoArbolGenerico(datosIdTextoArbolGenerico);
    	
    	seleccionados.add(obj);
    	
    	datosIdTextoArbolGenerico = new ParIdentificadorTexto(2, "DDDDDDDDDDDDDD");
    	ArbolGenericoObject obj2 = new ArbolGenericoObject(new DefaultMutableTreeNode());
    	obj2.setDatosIdTextoArbolGenerico(datosIdTextoArbolGenerico);
    	seleccionados.add(obj2);
    	
    	datosIdTextoArbolGenerico = new ParIdentificadorTexto(3, "GGGGGGGGGGGG");
    	ArbolGenericoObject obj3 = new ArbolGenericoObject(new DefaultMutableTreeNode());
    	obj3.setDatosIdTextoArbolGenerico(datosIdTextoArbolGenerico);
    	seleccionados.add(obj3);
    	
    	GenerarFipBean gfb= new GenerarFipBean();
    	gfb.generarFip(seleccionados);
    	
    
	}
    
    public void guardarDocumentoXML(String texto, String nombre) {
    	   try {
    		   
    		   	String fips2Dir = System.getProperty("jboss.home.dir") + File.separator + "var" + File.separator + "tmp" + File.separator;
           	  	log.info("Directorio de los FIPs 2: " + fips2Dir);
           	  	
           	  	OutputStream fout = new FileOutputStream(fips2Dir + nombre);
           	  	OutputStream bout = new BufferedOutputStream(fout);
           	  	OutputStreamWriter out = new OutputStreamWriter(bout, JAVA_ENCODING);
           	  	out.write(texto);
           	  	out.flush();
           	  	out.close();
    	      
    	   } catch (UnsupportedEncodingException e) {
    	   //etc, bla, bla, catchichurri
    	} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    @Remove
	public void remove()
    {
    	
    }
    
}
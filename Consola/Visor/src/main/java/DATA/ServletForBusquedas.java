/*
 * Copyright 2011 red.es
 * Autores: Arnaiz Consultores.
 *
 ** Licencia con arreglo a la EUPL, Versión 1.1 o -en cuanto
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
package DATA;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.codehaus.jackson.map.ObjectMapper;
import org.xml.sax.InputSource;

/**
 *
 * @author jorge.bodas
 */
public class ServletForBusquedas extends HttpServlet {

    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            String wsName = request.getParameter("wsName");
            if (wsName.equals("GET_PROVINCIAS")) {
                Document doc = this.getDocumentoXML(request, response);

                //Filtrar las provincias del XML por la etiqueta np
                NodeList listaNodos = doc.getElementsByTagName("np");
                System.out.println("Numero de Provincias: " + listaNodos.getLength());

                Map<String, Object> mapa_provincias = new HashMap<String, Object>();
                //Vector<String> v = new  Vector<String>();
                ArrayList listaProvincias = new ArrayList();

                for (int i = 0; i < listaNodos.getLength(); i++) {
                    //  v.add (listaNodos.item(i).getTextContent());
                    listaProvincias.add(listaNodos.item(i).getTextContent());
                    //System.out.println("el valor del nodo es "+ listaNodos.item(i).getTextContent());
                }
                mapa_provincias.put("provincias", listaProvincias);
                //out.print(v);

                //JSON
                ObjectMapper om = new ObjectMapper();
                om.writeValue(out, mapa_provincias);

            } else if (wsName.equals("GET_MUNICIPIOS_SEGUN_PROVINCIA")) {

                Document doc = this.getDocumentoXML(request, response);

                //Filtrar los municipios por la etiqueta nm
                NodeList listaNodos = doc.getElementsByTagName("nm");
                System.out.println("Numero de Municipios:" + listaNodos.getLength());

                Map<String, Object> mapa_municipios = new HashMap<String, Object>();
                ArrayList listaMunicipios = new ArrayList();

                for (int i = 0; i < listaNodos.getLength(); i++) {
                    listaMunicipios.add(listaNodos.item(i).getTextContent());
                    //System.out.println("el valor del nodo es "+ listaNodos.item(i).getTextContent());
                }
                mapa_municipios.put("municipios", listaMunicipios);

                //JSON
                ObjectMapper om = new ObjectMapper();
                om.writeValue(out, mapa_municipios);

            } else if (wsName.equals("GET_VIAS_POR_NOMBRE")) {

                Document doc = this.getDocumentoXML(request, response);

                String url = request.getParameter("urlData");
                String xml = this.crossDomain(url, response);

                //Filtrar las calles por la etiqueta nv
                NodeList listaNodos = doc.getElementsByTagName("nv");

                Map<String, Object> m = new HashMap<String, Object>();
                m.put("xml", xml);
                if (listaNodos.getLength() == 0) {
                    ArrayList listaErr = new ArrayList();
                    NodeList err = doc.getElementsByTagName("des");
                    listaErr.add(err.item(0).getTextContent().replace("Í", "I"));
                    System.out.println(err.item(0).getTextContent().replace("Í", "I"));
                    m.put("error", listaErr);
                } else {
                    ArrayList lista = new ArrayList();
                    for (int i = 0; i < listaNodos.getLength(); i++) {
                        lista.add(listaNodos.item(i).getTextContent());
                    }

                    m.put("via", lista);

                }
                //JSON
                ObjectMapper om = new ObjectMapper();
                om.writeValue(out, m);
            } else if (wsName.equals("GET_CATASTRO_POR_DIRECCION")) {

                Document doc = this.getDocumentoXML(request, response);

                String url = request.getParameter("urlData");
                String xml = this.crossDomain(url, response);

                //Filtrar los municipios por la etiqueta pc1,pc2
                NodeList listaNodos = doc.getElementsByTagName("pc1");
                NodeList listaNodos2 = doc.getElementsByTagName("pc2");
                NodeList numerosNodos = doc.getElementsByTagName("pnp");


                Map<String, Object> m = new HashMap<String, Object>();
                m.put("xml", xml);

                ArrayList listaErr = new ArrayList();
                NodeList err = doc.getElementsByTagName("err");
                for (int iErr = 0; iErr < err.getLength(); iErr++) {
                    for (int iDesErr = 0; iDesErr < err.item(iErr).getChildNodes().getLength(); iDesErr++) {
                        if (err.item(iErr).getChildNodes().item(iDesErr).getNodeName().equalsIgnoreCase("des")) {
                            listaErr.add(err.item(iErr).getChildNodes().item(iDesErr).getTextContent());
                            System.out.println(err.item(iErr).getChildNodes().item(iDesErr).getTextContent());
                        }
                    }
                }
                if (listaErr.size() > 0) {
                    m.put("error", listaErr);
                }
                if (listaNodos.getLength() > 0) {
                    ArrayList lista = new ArrayList();
                    ArrayList lista2 = new ArrayList();
                    ArrayList numeros = new ArrayList();
                    for (int i = 0; i < listaNodos.getLength(); i++) {
                        lista.add(listaNodos.item(i).getTextContent());
                        lista2.add(listaNodos2.item(i).getTextContent());
                        numeros.add(numerosNodos.item(i).getTextContent());
                        //System.out.println("el valor del nodo es "+ listaNodos.item(i).getTextContent());
                    }

                    m.put("ref1", lista);
                    m.put("ref2", lista2);
                    m.put("numeros", numeros);
                }
                //JSON
                ObjectMapper om = new ObjectMapper();
                om.writeValue(out, m);
            } else if (wsName.equals("GET_CATASTRO_POR_REFERENCIA")) {

                Document doc = this.getDocumentoXML(request, response);
                Map<String, Object> m = new HashMap<String, Object>();
                if (doc == null) {
                    ArrayList listaErr = new ArrayList();
                    NodeList err = doc.getElementsByTagName("des");
                    listaErr.add(err.item(0).getTextContent());
                    System.out.println("ERROR  accediendo a catastro");
                    m.put("error", listaErr);
                } else {
                    NodeList listaNodos = doc.getElementsByTagName("ldt");

                    

                    // Caso de que la busqueda nod evuel
                    if (listaNodos.getLength() == 0) {
                        ArrayList listaErr = new ArrayList();
                        NodeList err = doc.getElementsByTagName("des");
                        listaErr.add(err.item(0).getTextContent());
                        System.out.println("ERROR  " + err.item(0).getTextContent());
                        m.put("error", listaErr);
                    } else {
                        ArrayList lista = new ArrayList();

                        //Añadir en listas los elementos que cumplen la busqueda
                        for (int i = 0; i < listaNodos.getLength(); i++) {
                            lista.add(listaNodos.item(i).getTextContent());
                        }
                        m.put("calle", lista);
                    }
                    //JSON
                    
                }
                ObjectMapper om = new ObjectMapper();
                om.writeValue(out, m);
            } else if (wsName.equals("GET_CATASTRO_POR_POL_PARC")) {

                Document doc = this.getDocumentoXML(request, response);
                
                
                NodeList listaNodos = doc.getElementsByTagName("ldt");
                if (listaNodos.getLength() == 0) {
                    listaNodos = doc.getElementsByTagName("npa");
                }
                //NodeList listaNodos = doc.getElementsByTagName("es");
                //NodeList listaNodos2 = doc.getElementsByTagName("pt");
                //NodeList listaNodos3 = doc.getElementsByTagName("pu");

                //La referencia catastral es necesario para centrarlo en el visor
                NodeList listaNodosRef = doc.getElementsByTagName("pc1");
                NodeList listaNodosRef2 = doc.getElementsByTagName("pc2");
                NodeList listaCodigosParajes = doc.getElementsByTagName("cpaj");
                Map<String, Object> m = new HashMap<String, Object>();

                if (listaNodos.getLength() == 0) {
                    ArrayList listaErr = new ArrayList();
                    NodeList err = doc.getElementsByTagName("des");
                    listaErr.add(err.item(0).getTextContent());
                    System.out.println("ERRROR  " + err.item(0).getTextContent());
                    m.put("error", listaErr);
                } else {
                    ArrayList lista = new ArrayList();
                    ArrayList listaRef = new ArrayList();
                    //ArrayList lista2 = new ArrayList();
                    //ArrayList lista3 = new ArrayList();
                    List<String> parajes=new ArrayList<String>();
                    for (int i = 0; i < listaNodos.getLength(); i++) {
                        if (!parajes.contains(listaCodigosParajes.item(i).getTextContent())){
                            lista.add(listaNodos.item(i).getTextContent());
                            listaRef.add(listaNodosRef.item(i).getTextContent() + listaNodosRef2.item(i).getTextContent());
                            parajes.add(listaCodigosParajes.item(i).getTextContent());
                        }
                        //lista2.add(listaNodos2.item(i).getTextContent());
                        //lista3.add(listaNodos3.item(i).getTextContent());
                        //System.out.println("el valor del nodo es "+ listaNodos.item(i).getTextContent());
                    }

                    m.put("parcela", lista);
                    m.put("ref", listaRef);
                    // m.put("escalera", lista);
                    // m.put("planta", lista2);
                    // m.put("puerta", lista3);
                }
                //JSON
                ObjectMapper om = new ObjectMapper();
                om.writeValue(out, m);
            } else if (wsName.equals("BUSCAR_ENTIDADES_SEGUN_PLAN")) {
                out.print(DAO.entidadesFromNombrePlan(
                        getServletContext().getInitParameter("wsdl-url"),
                        request.getParameter("nombre"),
                        request.getParameter("plan"),
                        Integer.parseInt(request.getParameter("idAmbito"))));
            } else if (wsName.equals("BUSCAR_ENTIDADES")) {
                out.print(DAO.entidadesFromNombre(
                        getServletContext().getInitParameter("wsdl-url"),
                        request.getParameter("nombre"),
                        request.getParameter("idAmbito")));
            } else if (wsName.equals("BUSCAR_PLANES")) {
                out.print(DAO.planesFromNombre(
                        getServletContext().getInitParameter("wsdl-url"),
                        request.getParameter("nombre"),
                        request.getParameter("idAmbito")));
            } else if (wsName.equals("GET_AMBITOS")) {
                response.setContentType("text/html;charset=UTF-8");
                out.print(DAO.DameAmbitos(
                        getServletContext().getInitParameter("wsdl-url"),
                        request.getParameter("idioma")));
            } else if (wsName.equals("UBICAR_ENTIDAD")) {
                out.print(DAO.extensionEntidad(
                        getServletContext().getInitParameter("wsdl-url"),
                        request.getParameter("idEntidad"),
                        request.getParameter("srs")));
            } else if (wsName.equals("UBICAR_PLAN")) {
                out.print(DAO.extensionPlan(
                        getServletContext().getInitParameter("wsdl-url"),
                        request.getParameter("idPlan"),
                        request.getParameter("srs")));
            } else if (wsName.equals("UBICAR_AMBITO")) {
                out.print(DAO.extensionAmbito(
                        getServletContext().getInitParameter("wsdl-url"),
                        request.getParameter("idAmbito"),
                        request.getParameter("srs")));
            } else if (wsName.equals("GET_GEOMETRIA_DE_ENTIDAD")) {
                out.print(DAO.geometriaEntidad(
                        getServletContext().getInitParameter("wsdl-url"),
                        Integer.valueOf(request.getParameter("idEntidad")),
                        Integer.valueOf(request.getParameter("maxPoints")),
                        request.getParameter("srs")));
            } else if (wsName.equals("GET_GEOMETRIA_DE_PLAN")) {
                out.print(DAO.geometriaPlan(
                        getServletContext().getInitParameter("wsdl-url"),
                        Integer.valueOf(request.getParameter("idPlan")),
                        request.getParameter("srs")));
            }
        }catch (Exception ex){
            ex.printStackTrace();
        } finally {
            out.close();

        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** 
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    public String crossDomain(String url, HttpServletResponse response) {
        if (url != null) {

            url = url.replace(" ", "%20");
            url = url.replace("ñ", "%F1");
            url = url.replace("á", "%E1");
            url = url.replace("é", "%E9");
            url = url.replace("í", "%ED");
            url = url.replace("ó", "%F3");
            url = url.replace("ú", "%FA");
            url = url.replace("ç", "%E7");
            url = url.replace("Ñ", "%D1");
            url = url.replace("Á", "%C1");
            url = url.replace("É", "%C9");
            url = url.replace("Í", "%CD");
            url = url.replace("Ó", "%D3");
            url = url.replace("Ú", "%DA");
            url = url.replace("Ç", "%C7");
            String data = DAO.leerURL(url);

            return data;
        } else {
            return null;
        }

    }

    private Document XmlDocFromString(String aXML) {
        Document doc = null;

        try {
            // Create a Reader Object
            Reader xmlReader = new StringReader(aXML);
            // Create an InputSource
            InputSource is = new InputSource(xmlReader);
            // Get the DocumentBuilder
            DocumentBuilder docBuild = (DocumentBuilder) ((DocumentBuilderFactory) DocumentBuilderFactory.newInstance()).newDocumentBuilder();
            // Parse the XML file
            doc = (Document) docBuild.parse(is);

        } catch (SAXException ex) {
            Logger.getLogger(ServletForBusquedas.class.getName()).log(Level.SEVERE, "Error parseando: {0}", aXML);
        } catch (IOException ex) {
            Logger.getLogger(ServletForBusquedas.class.getName()).log(Level.SEVERE, "Error parseando: {0}", aXML);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(ServletForBusquedas.class.getName()).log(Level.SEVERE, "Error parseando: {0}", aXML);
        }
        return doc;
    }

    public Document getDocumentoXML(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String url = request.getParameter("urlData");

        String xml = this.crossDomain(url, response);

        Document doc = XmlDocFromString(xml);
        return doc;

    }
}

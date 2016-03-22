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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;


public class ServletForGenericServices extends HttpServlet {
   
    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @SuppressWarnings("static-access")
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        String methodName=request.getMethod();
        
        try {
            String wsName = request.getParameter("wsName");
            if(wsName.equals("CROSS-DOMAIN"))
            {
                if (methodName.equals("GET"))
                {
                    PrintWriter out =response.getWriter();
                    out.print(leerURL(request.getParameter("url")));
                    out.close();
                }
                else if (methodName.equals("POST"))
                {
                    DAO.redirigePost(request,response);
                }
            }
            else if(wsName.equals("REMOVE_FILE_FROM_SERVER"))
            {
                   String idFile=request.getParameter("id");
                   String extension=request.getParameter("extension");
                   File f = new File(this.getServletContext().getRealPath("\\data\\KML\\"),idFile+"."+extension);
                   if(f.exists())
                   {
                       f.delete();
                   }
            }
            else if(wsName.equals("EXPORT_VECTOR"))
            {
                String wkt=request.getParameter("WKT");
                String usuario=request.getParameter("USUARIO");

                //SE GENERA EL XML
                    Document xmlDoc;
                    xmlDoc=this.generarXMLDOC("root");
                        Element root =xmlDoc.getDocumentElement();
                //GENERO LOS ELEMENTOS QUE VA A CONTENER EL XML
                           Text textoID_USER=xmlDoc.createTextNode(usuario);
                           Element iduser=xmlDoc.createElement("IDUSER");
                                     iduser.appendChild(textoID_USER);
                                            root.appendChild(iduser);

                           Element WKT=xmlDoc.createElement("WKT");
                           Text WKTText=xmlDoc.createTextNode(wkt);
                                        WKT.appendChild(WKTText);
                                            root.appendChild(WKT);
                //INSERTAMOS LA RUTA AL XML----------------------------------------------------
                              PrintWriter out =response.getWriter();
                              try {
                                 this.generarXMLFile_De_XMLDOC(xmlDoc,
                                         getServletContext().getRealPath("data/WKT"),
                                         usuario+".xml");
                                 out.print("OK");
                              } catch (TransformerConfigurationException ex) {
                                 out.print("null");
                                 Logger.getLogger(ActionServlet.class.getName()).log(Level.SEVERE, null, ex);
                              } catch (TransformerException ex) {
                                 out.print("null");
                                 Logger.getLogger(ActionServlet.class.getName()).log(Level.SEVERE, null, ex);
                              } catch (FileNotFoundException ex) {
                                 out.print("null");
                                 Logger.getLogger(ActionServlet.class.getName()).log(Level.SEVERE, null, ex);
                              }
                              out.close();
            }
        } 
        catch(Exception e)
        {
            e.printStackTrace();
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


    /**
     * METODO PARA GENERAR UNA INSTACIA DE DOCUMENT XML
     *
     * @param  rootName  String con el nombre del root del XML
     * @return      Document - El documento xml generado
     * @see         org.w3c.dom.Document
     */
    public static Document generarXMLDOC(String rootName)
    {
        Document dom=null;

        try
        {
              //GENERO LA IMPLEMENTACION
                  DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                  factory.setNamespaceAware(true);
                  DocumentBuilder builder = factory.newDocumentBuilder();
                  DOMImplementation impl = builder.getDOMImplementation();

              //GENERO EL DOCUMENTO
                  dom = impl.createDocument(null,rootName, null);
        }
        catch(ParserConfigurationException p)
        {
            p.printStackTrace();
            dom=null;
        }

        return dom;
    }

    /**
     * METODO PARA GENERAR UN FICHERO XML DESDE UNA INSTANCIA DOCUMENT
     *
     * @param  doc  Document con el contenido xml
     * @param  rutaSalida  Ruta de carpetas donde se almacenara el archivo
     * @param  nombreArchivo  Nombre del archivo (Ej: 'nombre.xml')
     * @return  void
     * @see         org.w3c.dom.Document
     */
    public static void generarXMLFile_De_XMLDOC(Document doc,String rutaSalida,String nombreArchivo) throws TransformerConfigurationException, TransformerException, FileNotFoundException, IOException
    {
        //TRANSFORMO EL XMLDOM A STRING
            TransformerFactory tfactory = TransformerFactory.newInstance();
            Transformer transformer = tfactory.newTransformer();
            StringWriter sw = new StringWriter();
            StreamResult result = new StreamResult(sw);
            DOMSource source = new DOMSource(doc);
            transformer.transform(source, result);
            String xmlString = sw.toString();
        //GENERO EL FICHERO XML EN LA RUTA RECIBIDA
            File fileCarpetas = new File(rutaSalida);
            File file = new File(fileCarpetas,nombreArchivo);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
            bw.write(xmlString);
            bw.flush();
            bw.close();
    }
    public String leerURL(String urlString)
    {
        urlString=urlString.replace(" ","%20");
        urlString=urlString.replace("ñ","%F1");
        urlString=urlString.replace("á","%E1");
        urlString=urlString.replace("é","%E9");
        urlString=urlString.replace("í","%ED");
        urlString=urlString.replace("ó","%F3");
        urlString=urlString.replace("ú","%FA");
        urlString=urlString.replace("ç","%E7");
        urlString=urlString.replace("Ñ","%D1");
        urlString=urlString.replace("Á","%C1");
        urlString=urlString.replace("É","%C9");
        urlString=urlString.replace("Í","%CD");
        urlString=urlString.replace("Ó","%D3");
        urlString=urlString.replace("Ú","%DA");
        urlString=urlString.replace("Ç","%C7");
        String data=DAO.leerURL(urlString);
        return data;
    }

}

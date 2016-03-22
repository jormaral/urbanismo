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

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;
import javax.xml.transform.URIResolver;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;



public class readKML extends HttpServlet {

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

                String resultado=getNSaveFile(request);
                response.setContentType("text/html");
                out.print(resultado);

        } finally {
            out.close();
        }
    }


    public String getNSaveFile(HttpServletRequest request) {
        String resultado="ERROR.alSalvarFichero";
        try {
            // Chequea que sea un request multipart (que se este enviando un file)
            boolean isMultipart = ServletFileUpload.isMultipartContent(request);

            DiskFileItemFactory factory = new DiskFileItemFactory();
            // maxima talla que sera guardada en memoria
            factory.setSizeThreshold(4096);
            // si se excede de la anterior talla, se ira guardando temporalmente, en la sgte direccion
            //factory.setRepository(new File(request));
            ServletFileUpload upload = new ServletFileUpload(factory);
            //maxima talla permisible (si se excede salta al catch)
            //upload.setSizeMax(2097152);

            List fileItems = upload.parseRequest(request);
            //obtiene el file enviado
            Iterator i = fileItems.iterator();
            FileItem fi = (FileItem)i.next();
            //graba el file enviado en el servidor local
            //path y Nombre del archivo destino (en el server)
            //System.out.println(this.getServletContext().getRealPath(request.getContextPath()+"\\data\\KML\\"));
            String path=this.getServletContext().getRealPath("/data/KML/");
            HttpSession session=request.getSession(true);
            String fileName=session.getId()+".kml";
            File file=new File(path, fileName);
            //file.mkdirs();
            fi.write(file);
            resultado= session.getId();
        }
        catch(Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            resultado = "ERROR.Excepcion."+e.getMessage();
        }
        return resultado;
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

}

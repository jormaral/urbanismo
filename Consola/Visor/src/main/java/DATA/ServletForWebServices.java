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
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class ServletForWebServices extends HttpServlet {
   
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
        String urlWS=getServletContext().getInitParameter("wsdl-url");
        try {
            String wsName = request.getParameter("wsName");
            if(wsName.equals("CONSULTA_GRAFICA"))
            {
                String wkt=request.getParameter("wkt");
                String srs=request.getParameter("srs");
                String idioma=request.getParameter("idioma");
                String respuesta=DAO.consultaGrafica(urlWS,wkt, srs, idioma);
                out.print(respuesta);
            }
            else if(wsName.equals("CONSULTA_GRAFICA_EXTENDIDA"))
            {
                String wkt=request.getParameter("wkt");
                String srs=request.getParameter("srs");
                String idioma=request.getParameter("idioma");
                String respuesta=DAO.consultaGraficaExtendida(urlWS,wkt, srs, idioma);
                out.print(respuesta);
            }
            else if(wsName.equals("GET_ID_AMBITO"))
            {
                Double  x=Double.valueOf(request.getParameter("X"));
                Double  y=Double.valueOf(request.getParameter("Y"));
                String srs=request.getParameter("srs");
                Long respuesta=DAO.DameIdAmbito(urlWS,x,y,srs);
                out.print(respuesta);
            }
            else if(wsName.equals("CREAR_WMS"))
            {
                String idHoja=request.getParameter("idHoja");
                String respuesta=DAO.creaWMSDoc(urlWS,idHoja);
                out.print(respuesta);
            }
            else if(wsName.equals("GET_LEYENDA"))
            {
                String idHoja=request.getParameter("idHoja");
                String respuesta=DAO.getLeyenda(urlWS,idHoja);
                out.print(respuesta);
            }
            else if(wsName.equals("GET_EXTENSION_PLAN"))
            {
                out.print(DAO.extensionPlan(urlWS,request.getParameter("id"), request.getParameter("SRS")));
            }
            else if(wsName.equals("GET_VERSION"))
            {
                out.print(DAO.getVersion(urlWS));
            }
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

}

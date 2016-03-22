/*
 * Copyright 2011 red.es
 * Autores: Arnaiz Consultores.
 *
 ** Licencia con arreglo a la EUPL, Versi�n 1.1 o -en cuanto
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
package es.mitc.redes.urbanismoenred.serviciosweb;

import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Documento;
import es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Tramite;
import es.mitc.redes.urbanismoenred.servicios.planeamiento.ExcepcionPlaneamiento;
import es.mitc.redes.urbanismoenred.servicios.planeamiento.ServicioPlaneamientoLocal;
import es.mitc.redes.urbanismoenred.servicios.seguridad.ExcepcionSeguridad;
import es.mitc.redes.urbanismoenred.servicios.seguridad.ServicioFipLocal;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.activation.MimetypesFileTypeMap;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Arnaiz Consultores
 *
 */
public class GetDocumento extends HttpServlet {

    private ServicioPlaneamientoLocal _servicioPlaneamiento = null;//getServicioPlaneamientoLocal();
    private ServicioFipLocal servicioFip = getServicioFipLocal();
    private static final MimetypesFileTypeMap mimeMapper = new MimetypesFileTypeMap();

    private ServicioPlaneamientoLocal servicioPlaneamiento() {
        if (_servicioPlaneamiento == null) {
            _servicioPlaneamiento = getServicioPlaneamientoLocal();
        }
        return _servicioPlaneamiento;
    }

    private ServicioPlaneamientoLocal getServicioPlaneamientoLocal() {
        Context context;
        try {
            context = new InitialContext();
            // Conexion con el bean de validacion de recintos
            return (ServicioPlaneamientoLocal) context.lookup("java:global/UrbanismoEnRedV2/Urbanismoenred-serviciosV2/ServicioPlaneamiento!es.mitc.redes.urbanismoenred.servicios.planeamiento.ServicioPlaneamientoLocal");

        } catch (NamingException e) {
            e.printStackTrace();
            return null;
        }
    }

    private ServicioFipLocal getServicioFipLocal() {
        Context context;
        try {
            context = new InitialContext();
            // Conexion con el bean de validacion de recintos
            return (ServicioFipLocal) context.lookup("java:global/UrbanismoEnRedV2/Urbanismoenred-serviciosV2/ServicioFip!es.mitc.redes.urbanismoenred.servicios.seguridad.ServicioFipLocal");

        } catch (NamingException e) {
            e.printStackTrace();
            return null;
        }
    }
    /*
     * (non-Javadoc) @see
     * es.mitc.redes.urbanismoenred.consola.planeamiento.IAccion#ejecutar(javax.servlet.http.HttpServletRequest,
     * javax.servlet.http.HttpServletResponse)
     */

    private void ejecutar(HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        String idDoc = request.getParameter("idDoc");
        String isLeyend = request.getParameter("leyenda");
        String hoja = request.getParameter("hoja");
        String fip1 = request.getParameter("fip1");
        String idAmbito = request.getParameter("idAmbito");
        
        if (idAmbito==null){  
            for (String param :request.getParameterMap().keySet()){
                if (param.endsWith("idAmbito")){
                    idAmbito=request.getParameter(param);
                }
            }
        }
        try {
            File result = null;
            if (fip1 != null) {
                result = servicioFip.getTemplateFromAmbito(Integer.valueOf(idAmbito), false);
            } else if (idDoc != null) {
                Documento doc = servicioPlaneamiento().get(Documento.class, Integer.parseInt(idDoc));
                Tramite trm = servicioPlaneamiento().get(Tramite.class, doc.getTramite().getIden());
                if (doc != null) {

                    File f = servicioPlaneamiento().getArchivo(trm.getCodigofip(), doc.getArchivo());
                    if (f.isDirectory()) {
                        if (isLeyend == null) {
                            File zipFile = new File(f.getParentFile(), f.getName() + ".zip");

                            if (!zipFile.exists()) {
                                comprimir(f, zipFile);
                            }

                            result = zipFile;
                        } else {
                            result = this.getLegend(f, hoja);
                        }
                    } else {
                        result = f;
                    }
                }
            }
            int length = 0;

            if (result != null) {
                ServletOutputStream op = response.getOutputStream();
                response.setContentType(mimeMapper.getContentType(result));
                response.setHeader("Content-Disposition", "filename=" + result.getName());
                response.setContentLength((int) result.length());

                byte[] bbuf = new byte[1024 * 256];
                DataInputStream in = new DataInputStream(new FileInputStream(result));
                while ((in != null) && ((length = in.read(bbuf)) != -1)) {
                    op.write(bbuf, 0, length);
                }
                in.close();
                op.flush();
                op.close();

            } else {
                response.getWriter().print("Solicitud incompleta");
            }

        } catch (ExcepcionPlaneamiento e) {
            response.getWriter().print("Error al obtener el documento: " + e.getMessage());
        } catch (ExcepcionSeguridad e) {
            response.getWriter().print("Error al obtener el documento: " + e.getMessage());
        }
    }

    private File getLegend(File dir, String hoja) {
        String[] children = findInDir("Leyenda.", dir);
        if (children != null && children.length > 0) {
            return new File(dir, children[0]);
        }

        children = findInDir("L.", dir);
        if (children != null && children.length > 0) {
            return new File(dir, children[0]);
        }
        if (hoja != null) {
            children = findInDir(hoja.replace("Recorte", "Leyenda"), dir);
            if (children != null && children.length > 0) {
                return new File(dir, children[0]);
            }

            children = findInDir(hoja.replace("G", "L"), dir);
            if (children != null && children.length > 0) {
                return new File(dir, children[0]);
            }

            children = findInDir(hoja.replace("R", "L"), dir);
            if (children != null && children.length > 0) {
                return new File(dir, children[0]);
            }
        }
        return null;
    }

    private String[] findInDir(final String inicioNombre, File dir) {
        FilenameFilter filter = new FilenameFilter() {

            public boolean accept(File dir, String name) {
                return name.toLowerCase().startsWith(inicioNombre.toLowerCase());
            }
        };
        String[] children = dir.list(filter);
        return children;
    }

    /**
     *
     * @param f
     * @return
     * @throws ExcepcionPlaneamiento
     */
    private void comprimir(File origen, File destino) throws ExcepcionPlaneamiento {
        int BUFFER = 2048;
        BufferedInputStream origin = null;
        FileOutputStream dest;
        try {
            dest = new FileOutputStream(destino);
            ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(dest));
            //out.setMethod(ZipOutputStream.DEFLATED);
            byte data[] = new byte[BUFFER];
            // get a list of files from current directory
            Map<String, File> entradas = new HashMap<String, File>();
            recopilarEntradas("", origen, entradas);

            for (String ruta : entradas.keySet()) {
                if (!entradas.get(ruta).isDirectory()) {
                    FileInputStream fi = new FileInputStream(entradas.get(ruta));
                    origin = new BufferedInputStream(fi, BUFFER);
                    ZipEntry entry = new ZipEntry(ruta);
                    out.putNextEntry(entry);
                    int count;
                    while ((count = origin.read(data, 0,
                            BUFFER)) != -1) {
                        out.write(data, 0, count);
                    }
                    origin.close();
                }
            }
            out.close();
        } catch (FileNotFoundException e) {
            throw new ExcepcionPlaneamiento("No se ha podido generar el archivo zip", e);
        } catch (IOException e) {
            throw new ExcepcionPlaneamiento("No se ha podido generar el archivo zip", e);
        }

    }

    void recopilarEntradas(String ruta, File padre, Map<String, File> entradas) {
        String rutaPadre;
        if (!ruta.isEmpty()) {
            rutaPadre = ruta + File.separator + padre.getName();
        } else {
            rutaPadre = padre.getName();
        }
        entradas.put(rutaPadre, padre);
        File hijos[] = padre.listFiles();

        for (int i = 0; i < hijos.length; i++) {
            if (hijos[i].isDirectory()) {
                recopilarEntradas(rutaPadre, hijos[i], entradas);
            } else {
                entradas.put(rutaPadre + File.separator + hijos[i].getName(), hijos[i]);
            }
        }
    }

    /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {

            ejecutar(request, response);
        } finally {
            // Aquí no puede ir un response.getWriter().close() porque si la
            // llamada ha sido a GET_LAYERCONFIG_DE_AMBITO y se llama después de
            // response.getOutputStream, se genera una excepción
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP
     * <code>GET</code> method.
     *
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
     * Handles the HTTP
     * <code>POST</code> method.
     *
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
}

<%
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
%>
<%@page import="java.io.FilenameFilter"%>
<%@page import="java.io.File"%>
<%@page import="es.mitc.redes.urbanismoenred.utils.configuracion.RedesConfig"%>
<%@page import="es.mitc.redes.urbanismoenred.data.rpm.ficha.Ficha"%>
<%@page import="es.mitc.redes.urbanismoenred.servicios.ficha.ServicioFichaLocal"%>
<%@page import="es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Plan"%>
<%@page import="es.mitc.redes.urbanismoenred.data.rpm.diccionario.Tipotramite"%>
<%@page import="es.mitc.redes.urbanismoenred.servicios.planeamiento.ServicioDiccionariosLocal"%>
<%@page import="es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Tramite"%>
<%@page contentType="text/html" pageEncoding="UTF-8"
        import="java.util.Map"
        import="java.util.List"
        import="es.mitc.redes.urbanismoenred.servicios.planeamiento.ServicioPlaneamientoLocal"
        import="javax.naming.InitialContext"
        %>




<%
    // Comprobación de parámetros de entrada
    if (request.getParameter("idioma") == null) {
%><div class="gd-info gd-error">No se ha indicado el idioma </div>
<%
        return;
    }
    String idioma = request.getParameter("idioma");
%>

<div id="">

    <%
        Tramite trm = null;
        Plan pln = null;
        long idFicha = 0;
        String tipoTramite = "";
        String nombreTramite = "";
        InitialContext context;
        Ficha ficha = null;
        String nombreFicha = "";
        String rutaFicha = "";
        String xPathFicha = "FICHA/entidades/entidad/conjunto/row";
        try {
            context = new InitialContext();
            ServicioFichaLocal srvFicha = (ServicioFichaLocal) context.lookup("java:global/UrbanismoEnRedV2/Urbanismoenred-serviciosV2/ServicioFicha!es.mitc.redes.urbanismoenred.servicios.ficha.ServicioFichaLocal");
            ServicioPlaneamientoLocal srvPlan = (ServicioPlaneamientoLocal) context.lookup("java:global/UrbanismoEnRedV2/Urbanismoenred-serviciosV2/ServicioPlaneamiento!es.mitc.redes.urbanismoenred.servicios.planeamiento.ServicioPlaneamientoLocal");
            ServicioDiccionariosLocal servicioDiccionario = (ServicioDiccionariosLocal) context.lookup("java:global/UrbanismoEnRedV2/Urbanismoenred-serviciosV2/ServicioDiccionarios!es.mitc.redes.urbanismoenred.servicios.planeamiento.ServicioDiccionariosLocal");
            if (request.getParameter("idFicha") != null) {
                idFicha = Long.valueOf(request.getParameter("idFicha"));
                ficha = srvFicha.get(Ficha.class, idFicha);
                nombreFicha = ficha.getNombre();
                rutaFicha = ficha.getPath();
                xPathFicha = ficha.getXpath();
                trm = srvPlan.get(Tramite.class, (int) ficha.getIdtramite());
            } else {
                trm = srvPlan.get(Tramite.class, Integer.valueOf(request.getParameter("idTramite")));
            }
            pln = srvPlan.get(Plan.class, trm.getPlan().getIden());
            tipoTramite = servicioDiccionario.getTraduccion(Tipotramite.class, trm.getIdtipotramite(), idioma);
            if (trm != null) {
                nombreTramite = pln.getNombre() + " " + tipoTramite + " " + trm.getIteracion();
            }
        } catch (Exception ex) {
            nombreTramite = request.getParameter("idTramite");
        }
    %>

    <label for="txtTramite">Trámite: </label>
    <label name="txtTramite" readonly="readonly" id="txtTramite" style="width: 100%;"><%=nombreTramite%></label><br/>

    <label for="txtNombre">Nombre</label>
    <input type="text" name="txtNombre" id="txtNombreFicha" value="<%=nombreFicha%>" style="width: 100%;"/><br/>

    <label for="txtRuta">Ruta</label>
    <select type="text" name="txtRuta" id="txtRutaFicha" style="width: 100%;">
        <% File dir = new File(RedesConfig.getREDES_PATH() + File.separator + "conf" + File.separator + "Plantillas");
            FilenameFilter filter = new FilenameFilter() {
                public boolean accept(File dir, String name) {
                    return name.toLowerCase().endsWith(".jrxml".toLowerCase());
                }
            };
            File[] children = dir.listFiles(filter);
            for (File plnt : children) {
                if (plnt.getPath().equals(rutaFicha)) {
                %>
                <option value="<%=plnt.getPath()%>" selected="selected">
                    <%=plnt.getName()%>
                </option>
                <%
                } else {
                %>
                <option value="<%=plnt.getPath()%>">
                    <%=plnt.getName()%>
                </option>
                <%
                }

            }
        %>
    </select>
    <br/>
    <!--<input type="text" name="txtRuta" id="txtRutaFicha" value="<%=rutaFicha%>" style="width: 100%;"/><br/>-->

    <!--<label for="txtXpath">Consulta XPath</label>-->
    <input type="hidden" name="txtXpath" id="txtXpathFicha" value="<%=xPathFicha%>" style="width: 100%;"/><br/>

    <div id="btnGuardarFicha" class="fichasSaveButton" title="Guardar cambios">
    </div>
</div>
<script type="text/javascript">
    $('btnGuardarFicha').addEvent('click',function(){
        var result = runService({
            wsName: 'GUARDAR_FICHA',
            idTram : <%=trm.getIden()%>,
            idFicha : <%=idFicha%>,
            nombreFicha : $('txtNombreFicha').value,
            xPath : $('txtXpathFicha').value,
            ruta : $('txtRutaFicha').value
        });
        if (result.toString().toLowerCase()=='true'){
            MochaUI.closeAll();
            MUI.notification("Ficha guardada correctamente");
        }else{
            new PopupMsg("Error guardando ficha");
        }
    })
</script>
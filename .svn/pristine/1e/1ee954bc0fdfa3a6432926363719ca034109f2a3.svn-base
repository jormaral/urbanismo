<%
    /*
     * Copyright 2011 red.es Autores: Arnaiz Consultores.
     *
     ** Licencia con arreglo a la EUPL, Versi�n 1.1 o -en cuanto sean aprobadas
     * por la Comision Europea- versiones posteriores de la EUPL (la
     * <<Licencia>>); Solo podra usarse esta obra si se respeta la Licencia.
     * Puede obtenerse una copia de la Licencia en:
     *
     * http://ec.europa.eu/idabc/eupl5
     *
     * Salvo cuando lo exija la legislacion aplicable o se acuerde. por escrito,
     * el programa distribuido con arreglo a la Licencia se distribuye <<TAL
     * CUAL>>, SIN GARANTIAS NI CONDICIONES DE NINGUN TIPO, ni expresas ni
     * implicitas. Vease la Licencia en el idioma concreto que rige los permisos
     * y limitaciones que establece la Licencia.
     */
%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Opciondeterminacion"%>
<%@page import="es.mitc.redes.urbanismoenred.servicios.planeamiento.ModalidadPlanes"%>
<%@page import="es.mitc.redes.urbanismoenred.utils.recursos.textos.Textos"%>
<%@page import="es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Determinacion"%>
<%@page import="org.w3c.dom.Element"%>
<%@page import="org.w3c.dom.Document"%>
<%@page import="es.mitc.redes.urbanismoenred.utils.recursos.xml.XML"%>
<%@page import="es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Plan"%>
<%@page import="es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Tramite"%>
<%@page import="es.mitc.redes.urbanismoenred.servicios.planeamiento.ServicioPlaneamientoLocal"%>
<%@page import="es.mitc.redes.urbanismoenred.data.rpm.ficha.Ficha"%>
<%@page import="es.mitc.redes.urbanismoenred.servicios.ficha.ServicioFichaLocal"%>
<%@page import="javax.naming.InitialContext"%>
<%
    Document dataArbol = null;
    Integer idCaracter = 0;
    if (request.getParameter("idFicha") != null && request.getParameter("tipo") != null) {
        InitialContext context = new InitialContext();
        ServicioFichaLocal srvFicha = (ServicioFichaLocal) context.lookup("java:global/UrbanismoEnRedV2/Urbanismoenred-serviciosV2/ServicioFicha!es.mitc.redes.urbanismoenred.servicios.ficha.ServicioFichaLocal");
        ServicioPlaneamientoLocal srvPlan = (ServicioPlaneamientoLocal) context.lookup("java:global/UrbanismoEnRedV2/Urbanismoenred-serviciosV2/ServicioPlaneamiento!es.mitc.redes.urbanismoenred.servicios.planeamiento.ServicioPlaneamientoLocal");

        Ficha ficha = srvFicha.get(Ficha.class, Long.valueOf(request.getParameter("idFicha")));
        Tramite tramite = srvPlan.get(Tramite.class, (int) ficha.getIdtramite());
        Plan plan = srvPlan.get(Plan.class, tramite.getPlan().getIden());
        Plan planBase = srvPlan.get(Plan.class, plan.getPlanByIdplanbase().getIden());
        Tramite tramiteBase = null;
        if (planBase != null) {
            tramiteBase = srvPlan.getTramitesPorPlan(planBase.getIden(), ModalidadPlanes.RPM)[0];
        }
        dataArbol = XML.generarXMLDOC("nodes");
        Element nodoTramite = null;
        Determinacion[] determinaciones = null;

        if (request.getParameter("tipo").equals("uso")) {
            idCaracter = Integer.parseInt(Textos.getTexto("diccionario", "caracterdeterminacion.regimenusos"));
        } else if (request.getParameter("tipo").equals("acto")) {
            idCaracter = Integer.parseInt(Textos.getTexto("diccionario", "caracterdeterminacion.regimenactos"));
        }
        if (planBase != null) {
            nodoTramite = XML.AddNode(dataArbol.getDocumentElement(), "node");
            nodoTramite.setAttribute("text", planBase.getNombre());
            determinaciones = null;
            determinaciones = srvPlan.getDeterminacionesPorCaracter(tramiteBase.getIden(), idCaracter);

            for (Determinacion determinacion : determinaciones) {
                Element nodoDeterminacion = XML.AddNode(nodoTramite, "node");
                nodoDeterminacion.setAttribute("id", String.valueOf(determinacion.getIden()));
                nodoDeterminacion.setAttribute("caracter", String.valueOf(determinacion.getIdcaracter()));
                nodoDeterminacion.setAttribute("text", determinacion.getNombre());
            }
        }

        nodoTramite = XML.AddNode(dataArbol.getDocumentElement(), "node");

        nodoTramite.setAttribute(
                "text", plan.getNombre());
        determinaciones = srvPlan.getDeterminacionesPorCaracter(tramite.getIden(), idCaracter);

        for (Determinacion determinacion : determinaciones) {
            Element nodoDeterminacion = XML.AddNode(nodoTramite, "node");
            nodoDeterminacion.setAttribute("id", String.valueOf(determinacion.getIden()));
            nodoDeterminacion.setAttribute("caracter", String.valueOf(determinacion.getIdcaracter()));
            nodoDeterminacion.setAttribute("text", determinacion.getNombre());
        }
    }
%>
<div id="">
    <label for="txtOrdenDetRegimen">Orden</label>
    <input type="text" name="txtOrdenDetRegimen" id="txtOrdenDetRegimen" value="" style="width: 100%;"/><br/>
    <div id="arbolDeterminacionRegimenFichas"></div>
    <div id="btnGuardarDeterminacionRegimen" class="fichasSaveButton" title="Guardar cambios">
    </div>
</div>
<script type="text/javascript">
    $("txtOrdenDetRegimen").addEvent('keydown',function(event) {
        // Allow only backspace and delete
        //alert(event.code);
        if ( event.code == 46 || event.code == 8 ) {
            // let it happen, don't do anything
        }
        else {
            // Ensure that it is a number and stop the keypress
            if ((event.code < 48 || event.code > 57) && (event.code < 96 || event.code > 105 )) {
                event.stop(); 
            }   
        }
    });
    
    var arbolDets=new MooTreeControl({
        div: 'arbolDeterminacionRegimenFichas',
        mode: 'folders',
        grid: true,
        treeType:'determinaciones',
        iconBar: ['javascriptsV2/mooTree2/Determinaciones.gif']
    },{
        text: cargarTextoSegunIdioma('Grupos de entidad'),
        open: true
    });
    arbolDets.root.loadData(jsIO.leerXMLFromString('<%=XML.XmlDocToString(dataArbol)%>'));
    if ($('txtOrdenDetRegimen').value==null){
        $('txtOrdenDetRegimen').value=Number.MAX_VALUE;
    }
    $('btnGuardarDeterminacionRegimen').addEvent('click',function(){
        if ($('txtOrdenDetRegimen').value==''){
            MUI.notification("El orden es un dato obligatorio");
        }else if (this.arbolDets.selected && this.arbolDets.selected.id){
            var result = runService({
                wsName: 'GUARDAR_DET_REGIMEN_FICHA',
                idFicha: <%=request.getParameter("idFicha")%>,
                idDeterminacion : this.arbolDets.selected.id,
                tipo:'<%=request.getParameter("tipo")%>',
                orden: $('txtOrdenDetRegimen').value
            });
            if (result.toString().toLowerCase()=='true'){
                MochaUI.closeAll();
                MUI.notification("Determinacion de régimen guardada correctamente");
            }else{
                new PopupMsg("Error guardando determinación de régimen");
            }
        }else{
            MUI.notification("Debe seleccionar una determinación válida");
        }
    }.bind({arbolDets:arbolDets}))
</script>
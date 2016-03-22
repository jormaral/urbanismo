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
    if (request.getParameter("idFicha") != null) {
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


        if (tramiteBase != null) {
            nodoTramite = XML.AddNode(dataArbol.getDocumentElement(), "node");
            nodoTramite.setAttribute("text", planBase.getNombre());
            nodoTramite.setAttribute("load", "GestionConsola?wsName=GET_NODOS_DETERM_DE_TRAMITE&idTramite=" + String.valueOf(tramiteBase.getIden()) + "&idioma=" + request.getParameter("idioma"));
        }
        nodoTramite = XML.AddNode(dataArbol.getDocumentElement(), "node");
        nodoTramite.setAttribute("text", plan.getNombre());
        nodoTramite.setAttribute("load", "GestionConsola?wsName=GET_NODOS_DETERM_DE_TRAMITE&idTramite=" + String.valueOf(tramite.getIden()) + "&idioma=" + request.getParameter("idioma"));

    }
%>
<div id="">
    <label for="txtOrdenGrupoDeterminacionFicha">Orden</label>
    <input type="text" name="txtOrdenGrupoDeterminacionFicha" id="txtOrdenGrupoDeterminacionFicha" value="" style="width: 100%;"/><br/>
    <input type="checkbox"  name="chkRegimenEsp" id="chkRegimenEsp"/>
    <label for="chkRegimenEsp">Mostrar régimen específico</label><br/>
    <div id="arbolDeterminacionesFichas"></div>
    <div id="btnGuardarDeterminacion_GrupoDet" class="fichasSaveButton" title="Guardar cambios">
    </div>
</div>
<script type="text/javascript">
    
    $("txtOrdenGrupoDeterminacionFicha").addEvent('keydown',function(event) {
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
        div: 'arbolDeterminacionesFichas',
        mode: 'folders',
        grid: true,
        treeType:'determinaciones',
        iconBar: ['javascriptsV2/mooTree2/Determinaciones.gif']
    },{
        text: cargarTextoSegunIdioma('Determinaciones'),
        open: true
    });
    arbolDets.root.loadData(jsIO.leerXMLFromString('<%=XML.XmlDocToString(dataArbol)%>'));
    if ($('txtOrdenGrupoDeterminacionFicha').value==null){
        $('txtOrdenGrupoDeterminacionFicha').value=Number.MAX_VALUE;
    }
    $('btnGuardarDeterminacion_GrupoDet').addEvent('click',function(){
        if ($('txtOrdenGrupoDeterminacionFicha').value==''){
            MUI.notification("El orden es un dato obligatorio");
        }else if (this.arbolDets.selected && this.arbolDets.selected.data.idNode){
            var result = runService({
                wsName: 'GUARDAR_DETERMINACION_GRUPO_DET',
                idGrupoDeterminacion: <%=request.getParameter("idGrupoDeterminacion")%>,
                idDeterminacion : this.arbolDets.selected.data.idNode,
                orden: $('txtOrdenGrupoDeterminacionFicha').value,
                regEsp: $('chkRegimenEsp').checked 
            });
            if (result.toString().toLowerCase()=='true'){
                MochaUI.closeAll();
                MUI.notification("Determinación guardada correctamente");
            }else{
                new PopupMsg("Error guardando determinación");
            }
        }else{
            MUI.notification("Debe seleccionar una determinación válida");
        }
    }.bind({arbolDets:arbolDets}))
</script>
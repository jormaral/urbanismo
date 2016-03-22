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
<%@page import="es.mitc.redes.urbanismoenred.data.rpm.ficha.Determinacionclasifacto"%>
<%@page import="es.mitc.redes.urbanismoenred.data.rpm.ficha.Determinacionclasifuso"%>
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

    if (request.getParameter("idDetClasif") != null && request.getParameter("tipo") != null) {
        InitialContext context = new InitialContext();
        ServicioFichaLocal srvFicha = (ServicioFichaLocal) context.lookup("java:global/UrbanismoEnRedV2/Urbanismoenred-serviciosV2/ServicioFicha!es.mitc.redes.urbanismoenred.servicios.ficha.ServicioFichaLocal");
        ServicioPlaneamientoLocal srvPlan = (ServicioPlaneamientoLocal) context.lookup("java:global/UrbanismoEnRedV2/Urbanismoenred-serviciosV2/ServicioPlaneamiento!es.mitc.redes.urbanismoenred.servicios.planeamiento.ServicioPlaneamientoLocal");
        dataArbol = XML.generarXMLDOC("nodes");
        Determinacion detPadre = null;
        if (request.getParameter("tipo").equals("uso")) {
            Determinacionclasifuso detClasif = srvFicha.get(Determinacionclasifuso.class, Long.parseLong(request.getParameter("idDetClasif")));
            detPadre = srvPlan.get(Determinacion.class, (int) detClasif.getIddeterminacion());
        } else if (request.getParameter("tipo").equals("acto")) {
            Determinacionclasifacto detClasif = srvFicha.get(Determinacionclasifacto.class, Long.parseLong(request.getParameter("idDetClasif")));
            detPadre = srvPlan.get(Determinacion.class, (int) detClasif.getIddeterminacion());
        }
        if (detPadre != null) {
            Element nodoDetClasif = XML.AddNode(dataArbol.getDocumentElement(), "node");
            nodoDetClasif.setAttribute("text", detPadre.getNombre());
            nodoDetClasif.setAttribute("caracter", String.valueOf(detPadre.getIdcaracter()));
            Opciondeterminacion[] opciones = srvPlan.getOpcionesDeterminacion(detPadre.getIden());
            for (Opciondeterminacion opc : opciones) {
                Determinacion determinacion = srvPlan.get(Determinacion.class, opc.getDeterminacionByIddeterminacionvalorref().getIden());
                Element nodoDeterminacion = XML.AddNode(nodoDetClasif, "node");
                nodoDeterminacion.setAttribute("id", String.valueOf(determinacion.getIden()));
                nodoDeterminacion.setAttribute("text", determinacion.getNombre());
                nodoDeterminacion.setAttribute("caracter", String.valueOf(determinacion.getIdcaracter()));
            }
        }
    }
%>
<div id="">
    <label for="txtOrdenValorDetClasif">Orden</label>
    <input type="text" name="txtOrdenValorDetClasif" id="txtOrdenValorDetClasif" value="" style="width: 100%;"/><br/>
    <div id="arbolDeterminacionValoresClasifFichas"></div>
    <div id="btnGuardarValorDeterminacionClasificatoria" class="fichasSaveButton" title="Guardar cambios">
    </div>
</div>
<script type="text/javascript">
    $("txtOrdenValorDetClasif").addEvent('keydown',function(event) {
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
        div: 'arbolDeterminacionValoresClasifFichas',
        mode: 'folders',
        grid: true,
        treeType:'determinaciones',
        iconBar: ['javascriptsV2/mooTree2/Fichas.gif']
    },{
        text: cargarTextoSegunIdioma('Determinaciones'),
        open: true
    });
    arbolDets.root.loadData(jsIO.leerXMLFromString('<%=XML.XmlDocToString(dataArbol)%>'));
    if ($('txtOrdenValorDetClasif').value==null){
        $('txtOrdenValorDetClasif').value=Number.MAX_VALUE;
    }
    $('btnGuardarValorDeterminacionClasificatoria').addEvent('click',function(){
        if ($('txtOrdenValorDetClasif').value==''){
            MUI.notification("El orden es un dato obligatorio");
        }else if (this.arbolDets.selected && this.arbolDets.selected.id){
            var result = runService({
                wsName: 'GUARDAR_VALOR_DET_CLASIF_FICHA',
                idDeterminacionClasif : <%=request.getParameter("idDetClasif")%>,
                idDeterminacion : this.arbolDets.selected.id,
                orden: $('txtOrdenValorDetClasif').value,
                tipo:'<%=request.getParameter("tipo")%>'
            });
            if (result.toString().toLowerCase()=='true'){
                MochaUI.closeAll();
                MUI.notification("Determinacion clasificatoria guardada correctamente");
            }else{
                new PopupMsg("Error guardando determinación clasificatoria");
            }
        }else{
            MUI.notification("Debe seleccionar una determinación válida");
        }
    }.bind({arbolDets:arbolDets}))
</script>

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
     * implicitas.arnaiz Vease la Licencia en el idioma concreto que rige los
     * permisos y limitaciones que establece la Licencia.
     */
%>
<%@page import="es.mitc.redes.urbanismoenred.data.rpm.ficha.Grupodeterminacion"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="javax.naming.InitialContext"%>
<%@page import="es.mitc.redes.urbanismoenred.servicios.ficha.ServicioFichaLocal"%>
<%@page import="es.mitc.redes.urbanismoenred.data.rpm.ficha.Conjuntogrupo"%>
<%
    String nombreGrupo = "";
    String ordenGrupo = "";
    if (request.getParameter("idGrupoDeterminacion") != null) {
        InitialContext context = new InitialContext();
        ServicioFichaLocal srvFicha = (ServicioFichaLocal) context.lookup("java:global/UrbanismoEnRedV2/Urbanismoenred-serviciosV2/ServicioFicha!es.mitc.redes.urbanismoenred.servicios.ficha.ServicioFichaLocal");
        Grupodeterminacion grupo = srvFicha.get(Grupodeterminacion.class, Long.valueOf(request.getParameter("idGrupoDeterminacion")));
        nombreGrupo = grupo.getNombre();
        ordenGrupo = String.valueOf(grupo.getFichagrupodeterminacions().iterator().next().getOrden());
    }
%>
<div id="">
    <label for="txtNombreGrupoDeterminacion">Nombre</label>
    <input type="text" name="txtNombreGrupoDeterminacion" id="txtNombreGrupoDeterminacion" value="<%=nombreGrupo%>" style="width: 100%;"/><br/>

    <label for="txtOrdenGrupo">Orden</label>
    <input type="text" name="txtOrdenGrupo" id="txtOrdenGrupo" value="<%=ordenGrupo%>" style="width: 100%;"/><br/>

    <div id="btnGuardarGrupoDeterminacion" class="fichasSaveButton" title="Guardar cambios">
    </div>
</div>
<script type="text/javascript">
    $("txtOrdenGrupo").addEvent('keydown',function(event) {
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
    if ($('txtOrdenGrupo').value==null){
        $('txtOrdenGrupo').value=Number.MAX_VALUE;
    }
    $('btnGuardarGrupoDeterminacion').addEvent('click',function(){
        if ($('txtOrdenGrupo').value==''){
            MUI.notification("El orden es un dato obligatorio");
        }else{
            var result = runService({
                wsName: 'GUARDAR_GRUPO_DETERMINACION_FICHA',
                idGrupoDeterminacion: <%=request.getParameter("idGrupoDeterminacion")%>,
                idFicha : <%=request.getParameter("idFicha")%>,
                idConjunto: <%=request.getParameter("idConjunto")%>,
                nombreGrupo : $('txtNombreGrupoDeterminacion').value,
                ordenGrupo: $('txtOrdenGrupo').value
            });
        }
        if (result.toString().toLowerCase()=='true'){
            MochaUI.closeAll();
            MUI.notification("Grupo de determinación guardado correctamente");
        }else{
            new PopupMsg("Error guardando de determinación");
        }

    })
</script>
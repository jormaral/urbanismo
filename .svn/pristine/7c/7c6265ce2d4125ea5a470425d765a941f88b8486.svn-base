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
<%@page import="javax.naming.InitialContext"%>
<%@page import="es.mitc.redes.urbanismoenred.servicios.ficha.ServicioFichaLocal"%>
<%@page import="es.mitc.redes.urbanismoenred.data.rpm.ficha.Conjuntogrupo"%>
<%
    String nombreGrupo = "";
    String ordenGrupo = "";
    Boolean regimen_directo = false;
    Boolean usos = false;
    Boolean actos = false;
    if (request.getParameter("idConjunto") != null) {
        InitialContext context = new InitialContext();
        ServicioFichaLocal srvFicha = (ServicioFichaLocal) context.lookup("java:global/UrbanismoEnRedV2/Urbanismoenred-serviciosV2/ServicioFicha!es.mitc.redes.urbanismoenred.servicios.ficha.ServicioFichaLocal");
        Conjuntogrupo conjuntoGrupo = srvFicha.get(Conjuntogrupo.class, Long.valueOf(request.getParameter("idConjunto")));
        nombreGrupo = conjuntoGrupo.getNombre();
        regimen_directo = conjuntoGrupo.getFichaconjuntogrupos().iterator().next().isRegimen_directo();
        usos = conjuntoGrupo.getFichaconjuntogrupos().iterator().next().isUsos();
        actos = conjuntoGrupo.getFichaconjuntogrupos().iterator().next().isActos();
        ordenGrupo = String.valueOf(conjuntoGrupo.getFichaconjuntogrupos().iterator().next().getOrden());
    }
%>
<div id="">
    <label for="txtNombreConjuntoGrupo">Nombre</label>
    <input type="text" name="txtNombreConjuntogrupo" id="txtNombreConjuntoGrupo" value="<%=nombreGrupo%>" style="width: 100%;"/><br/>
    <label for="txtOrdenConjunto">Orden</label>
    <input type="text" name="txtOrdenConjunto" id="txtOrdenConjunto" value="<%=ordenGrupo%>" style="width: 100%;"/><br/>
    <input type="checkbox"  name="chkRegimenDirecto" id="chkRegimenDirecto" <%=regimen_directo ? "checked" : ""%>/>
    <label for="chkRegimenDirecto">Mostrar en régimen directo</label><br/>
    <input type="checkbox"  name="chkUsos" id="chkUsos" <%=usos ? "checked" : ""%>/>
    <label for="chkUsos">Mostrar en usos</label><br/>
    <input type="checkbox"  name="chkActos" id="chkActos" <%=actos ? "checked" : ""%>/>
    <label for="chkActos">Mostrar en actos</label><br/>
    <div id="btnGuardarConjunto" class="fichasSaveButton" title="Guardar cambios">
    </div>
</div>
<script type="text/javascript">

    $("txtOrdenConjunto").addEvent('keydown',function(event) {
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
    if ($('txtOrdenConjunto').value==null){
        $('txtOrdenConjunto').value=Number.MAX_VALUE;
    }
    $('btnGuardarConjunto').addEvent('click',function(){
        if ($('txtOrdenConjunto').value==''){
            MUI.notification("El orden es un dato obligatorio");
        }else{
            var result = runService({
                wsName: 'GUARDAR_CONJUNTO_FICHA',
                idFicha : <%=request.getParameter("idFicha")%>,
                idConjunto: <%=request.getParameter("idConjunto")%>,
                nombreGrupo : $('txtNombreConjuntoGrupo').value,
                ordenGrupo: $('txtOrdenConjunto').value,
                regimen_directo:$('chkRegimenDirecto').checked,
                usos:$('chkUsos').checked,
                actos:$('chkActos').checked
            });
            if (result.toString().toLowerCase()=='true'){
                MochaUI.closeAll();
                MUI.notification("Capa guardada correctamente");
            }else{
                new PopupMsg("Error guardando capa");
            }
        }
    })
</script>
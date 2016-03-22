<%-- 
    Document   : arbolambCoords
    Created on : 18-nov-2009, 15:52:02
    Author     : Alvaro.Martin
--%>


<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">


<%
        // Control de parámetros
        String coorx = request.getParameter("x");
        if (coorx == null) {
%> <p>No se ha indicado el parámetro x</p>  <%
            return;
        }

        String coory = request.getParameter("y");
        if (coory == null) {
%> <p>No se ha indicado el parámetro y</p>  <%
            return;
        }

        String srs = request.getParameter("srs");
        if (srs == null) {
%> <p>No se ha indicado el parámetro srs</p>  <%
            return;
        }

        String rutaServ = request.getParameter("rutaServ");
        if (rutaServ == null) {
%> <p>No se ha indicado el parámetro rutaConsola</p>  <%
            return;
        }

        String txtDebugMode = request.getParameter("debugMode");
        boolean debug = false;
        if (txtDebugMode != null) {
            if (txtDebugMode.equalsIgnoreCase("on")) {
                debug = true;
            }
        }

%>


<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Ámbitos por localización</title>


        <link href="<%= rutaServ%>javascripts/mooTree2/mootree.css" rel="stylesheet" type="text/css"/>

    </head>
    <body>


        <div id="arbolambCoords">
            <!--<h4>Ámbitos en la localización</h4>-->
        </div>
    </body>

    <%if (debug) { // si estamos en modo debug cargamos en local las librerías mootools%>
    <script type="text/javascript" src="<%= rutaServ%>javascripts/MT12/mootools-1.2.4-core-yc.js"></script>
    <script type="text/javascript" src="<%= rutaServ%>javascripts/MT12/mootools-1.2.4.2-more.js"></script>
    <%}%>

    <script src="<%= rutaServ%>javascripts/mooTree2/mootree.js" type="text/javascript"></script>
    <script src="<%= rutaServ%>lanzaderas/arbolambitosCoords/js/arbolambCoords.js" type="text/javascript"></script>


    <script type="text/javascript">
        var idioma = 'es';

        var arbAmb = new ArbolAmbCoords({
            x: <%= coorx%>,
            y: <%= coory%>,
            srs: '<%= srs%>',
            rutaServ: '<%= rutaServ%>',
            divtree: 'arbolambCoords',
            titulo: 'LISTADO de ÁMBITOS',
            idioma: idioma,
            onSelectNode: onSelectNodeAmb
        });
        arbAmb.display();


        function onSelectNodeAmb(node, state){
            var rutaServ = '<%= rutaServ%>';
            
            if (state && node.data.tipo === 'documento'){

                var txt = node.text + " ["+ node.data.tipo + "] " + node.data.idNode;

                var req = new Request({
                    url: rutaServ+'RestMethod?wsName=GET_DOC_URL&idDoc='+node.data.idNode+'&idioma='+escape(idioma),
                    method: 'get',
                    onSuccess: function(t){

                        window.open(t,"Documento",'');
                    }
                });
                req.send();
            }
        }
    </script>

</html>


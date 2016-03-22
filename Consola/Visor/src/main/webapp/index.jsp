<%-- 
    Document   : index
    Created on : 24-nov-2009, 11:31:07
    Author     : jorge.bodas
--%>

<%@page contentType="text/html" pageEncoding="ISO-8859-1"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <!-- <meta http-equiv="X-UA-Compatible" content="IE=EmulateIE7" />-->
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
        <!-- <link href="styles/reset.css" rel="stylesheet" type="text/css" media="screen" /> -->
        <link href="styles/base.css" rel="stylesheet" type="text/css" media="screen" />
        <link href="styles/SplashScreen.css" rel="stylesheet" type="text/css" media="screen" />
        <link href="styles/Cabecera.css" rel="stylesheet" type="text/css" media="screen" />
        <link href="styles/Pie.css" rel="stylesheet" type="text/css" media="screen" />
        <link href="styles/BotoneraPrincipal.css" rel="stylesheet" type="text/css" media="screen" />
        <link href="styles/BotoneraPrincipal.css" rel="stylesheet" type="text/css" media="screen" />
        <link href="styles/BotoneraGoogle.css" rel="stylesheet" type="text/css" media="screen" />
        <link href="styles/ZoomBar.css" rel="stylesheet" type="text/css" media="screen" />
        <link href="styles/JobWindow.css" rel="stylesheet" type="text/css" media="screen" />
        <link href="styles/JobStyler.css" rel="stylesheet" type="text/css" media="screen" />
        <link href="javascripts/squeezeBox/assets/SqueezeBox.css" rel="stylesheet" type="text/css" media="screen" />
        <link rel="stylesheet" href="javascripts/MooScroll_0.59/css/example.css" type="text/css" media="all" />
        <link rel="stylesheet" href="javascripts/MooScroll_0.59/css/mooScroll.css" type="text/css" media="all" />
        <link rel="stylesheet" href="javascripts/mooTree2/mootree.css" type="text/css" media="all" />
        <link rel="stylesheet" href="javascripts/mooTree2_Capas/mootree_Capas.css" type="text/css" media="all" />
        <link rel="stylesheet" href="javascripts/MooFlow/MooFlow.css" type="text/css" media="all" />
        <link href="styles/Busqueda.css" rel="stylesheet" type="text/css" media="screen" />
        <link href="styles/lectorWMS.css" rel="stylesheet" type="text/css" media="screen" />
        <link href="styles/lectorWFS.css" rel="stylesheet" type="text/css" media="screen" />
        <link href="styles/facetip.css" rel="stylesheet" type="text/css" media="screen" />
        <link href="styles/Impresion.css" rel="stylesheet" type="text/css" media="screen" />
        <link href="styles/consulta_ext.css" rel="stylesheet" type="text/css" media="screen" />

        <!--[if IE 6]>
                <link rel="stylesheet" href="javascripts/MooScroll_0.59/css/mooScroll-ie6.css" type="text/css" media="all" charset="utf-8" />
        <![endif]-->
        <style>
            .olHandlerBoxZoomBox
            {
                    border: 1px solid white;
                    position: absolute;
                    background-color: black;
                    opacity: 0.50;
                    font-size: 1px;
                    filter: alpha(opacity=50);
            }
        </style>
        <title>Urbanismo En Red - Visor</title>

    </head>
    <body>
        <div id="map"></div>
        <div id="aux" style="visibility:hidden"></div>
        <div id="auxCapas" style="visibility:hidden"></div>
        <div id="fullscreen"></div>
    </body>
    <!-- LIBRARIES -->
    <%
        String debug=request.getParameter("debug");
        if(debug!=null && debug.equals("true"))
        {
            debug="_debug";
        }
        else
        {
            debug="";
        }
    %>

    <%
        String visor=request.getParameter("visor");
        if(visor==null)
        {
            visor="0";
        }
    %>

    <script>
        var debug="<%= debug%>";

        var numVisor=<%= visor%>

    /*var googleLoadScript=document.createElement('script');
        googleLoadScript.type='text/javascript';
        googleLoadScript.src='http://www.google.com/jsapi?key='+{
            'localhost:8087':'ABQIAAAACy1bjZ2KDyfsWsjxYn9L_hRgB02SOlT7GPiWtPZ2AqBuO7C_WhSv_FIy5uwqMLuYKWg5LkpR5gAVyw'
        }[window.location.host];
        document.body.appendChild(googleLoadScript);*/


    </script>

    <!-- <script type="text/javascript" charset="ISO-8859-1" src="http://maps.google.com/maps/api/js?sensor=false&language=es&region=ES"></script> -->

    
    <!-- URBR <script type="text/javascript" charset="ISO-8859-1" src="http://www.google.com/jsapi?key=ABQIAAAACy1bjZ2KDyfsWsjxYn9L_hRgB02SOlT7GPiWtPZ2AqBuO7C_WhSv_FIy5uwqMLuYKWg5LkpR5gAVyw"></script> -->
    

    
    <script src="javascripts/editableSelect.js" type="text/javascript" charset="ISO-8859-1"></script>


    <script src="javascripts/MT13/<%=debug%>/mootools-core-1.3.2.js" type="text/javascript" charset="ISO-8859-1"></script>
    <script src="javascripts/MT13/<%=debug%>/mootools-more-1.3.2.1.js" type="text/javascript" charset="ISO-8859-1"></script> 

    <!--<script src="javascripts/MT12/mootools-1.2.4-core_debug.js" type="text/javascript" charset="ISO-8859-1"></script>
    <script src="javascripts/MT12/mootools-1.2.4.2-more_debug.js" type="text/javascript" charset="ISO-8859-1"></script>
    
     <script src="javascripts/MT12/mui.text.js" type="text/javascript" charset="ISO-8859-1"></script>
    <script src="javascripts/MT12/mui.combobox.js" type="text/javascript" charset="ISO-8859-1"></script> -->
    <script src="javascripts/squeezeBox/SqueezeBox.js" type="text/javascript" charset="ISO-8859-1"></script>
    <script src="javascripts/MooScroll_0.59/js/MooScroll_0.59.js" type="text/javascript" charset="ISO-8859-1"></script>
    <script src="javascripts/mooTree2/mootree.js" type="text/javascript" charset="ISO-8859-1"></script>
    <script src="javascripts/mooTree2_Capas/mootree_Capas.js" type="text/javascript" charset="ISO-8859-1"></script>
    <script src="javascripts/MooFlow/MooFlow.js" type="text/javascript" charset="ISO-8859-1"></script>

    <!-- Cargar la librería API V3 de Google -->
    <script src="http://maps.google.com/maps/api/js?sensor=false"></script>

    <!--<script src="http://www.openlayers.org/api/OpenLayers.js" type="text/javascript" charset="ISO-8859-1"></script>-->
    
    <script src="javascripts/OL211/<%=debug%>/OpenLayers.js" type="text/javascript" charset="ISO-8859-1"></script>
    <script src="javascripts/OL211/OpenLayers/Lang/es.js" type="text/javascript" charset="ISO-8859-1"></script>
    <script src="javascripts/OL211/proj4js/proj4js-combined.js" type="text/javascript" charset="ISO-8859-1"></script>
    <!--<script src="javascripts/OL211/WFST_v1_1_0.js" type="text/javascript" charset="ISO-8859-1"></script>-->
    <script src="javascripts/OL211/GML_v3.js" type="text/javascript" charset="ISO-8859-1"></script>
    <script src="javascripts/OL211/redefinicionesOL.js" type="text/javascript" charset="ISO-8859-1"></script>
    <script src="javascripts/OL211/Label.js" type="text/javascript" charset="ISO-8859-1"></script>

    <script src="javascripts/loader.js" type="text/javascript" charset="ISO-8859-1"></script>
    <script src="javascripts/jsIO.js" type="text/javascript" charset="ISO-8859-1"></script>
    <script src="javascripts/Notificacion.js" type="text/javascript" charset="ISO-8859-1" ></script>
    <script src="javascripts/SplashScreen.js" type="text/javascript" charset="ISO-8859-1"></script>
    <script src="javascripts/Visor.js" type="text/javascript" charset="ISO-8859-1"></script>
    <script src="javascripts/Cabecera.js" type="text/javascript" charset="ISO-8859-1"></script>
    <script src="javascripts/Pie.js" type="text/javascript" charset="ISO-8859-1"></script>
    <script src="javascripts/BotoneraPrincipal.js" type="text/javascript" charset="ISO-8859-1"></script>
    <script src="javascripts/BotoneraGoogle.js" type="text/javascript" charset="ISO-8859-1"></script>
    <script src="javascripts/ZoomBar.js" type="text/javascript" charset="ISO-8859-1"></script>
    <script src="javascripts/JobWindow.js" type="text/javascript" charset="ISO-8859-1"></script>
    <script src="javascripts/ajaxupload.js" type="text/javascript" charset="ISO-8859-1"></script>
    <script src="javascripts/jsBUSQUEDAS.js" type="text/javascript" charset="ISO-8859-1"></script>
    <script src="javascripts/JobStyler.js" type="text/javascript" charset="ISO-8859-1"></script><script src="javascripts/facetip.js" type="text/javascript" charset="ISO-8859-1"></script>
    <script src="javascripts/CurvyCorners/curvycorners.src.js" type="text/javascript" charset="ISO-8859-1"></script>
    <script src="javascripts/jsRelocate.js" type="text/javascript" charset="ISO-8859-1"></script>
    
    <script src="javascripts/Print/PagePrinter.js" type="text/javascript" charset="ISO-8859-1"></script>
    <script src="javascripts/Print/JobPrinter.js" type="text/javascript" charset="ISO-8859-1"></script>
    <!-- \LIBRARIES -->
</html>

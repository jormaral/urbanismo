<!-- 
    Document   : index
    Created on : 01-nov-2011, 17:00:00
    Author     : Alvaro.Montero
-->

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!-- HTML 4
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">  -->
    
<!-- HTML 5 -->
<!DOCTYPE HTML>
<html>
    <head>

        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta http-equiv="X-UA-Compatible" content="IE=8" />
        
        <title>CONSOLA DE PLANEAMIENTO</title>       
        
        <meta name="description" content="Consola de planeamiento" />   
        
        <link rel="stylesheet" type="text/css" href="themes/default/css/Content.css" />
		<link rel="stylesheet" type="text/css" href="themes/default/css/Core.css"/>
		<link rel="stylesheet" type="text/css" href="themes/default/css/Layout.css"/>
		<!-- <link rel="stylesheet" type="text/css" href="themes/default/css/Dock.css"/> -->
		<link rel="stylesheet" type="text/css" href="themes/default/css/Tabs.css"/>
		<link rel="stylesheet" type="text/css" href="themes/default/css/Window.css"/>
        
        <link rel="stylesheet" href="styles/baseStyles.css" type="text/css"/>
        <link rel="stylesheet" href="styles/fromsStyles.css" type="text/css"/>
        
        <!-- ConsoleOptions NO VISOR GIS -->
        <link rel="stylesheet" href="styles/GestionLogin.css" type="text/css"/>
        <link rel="stylesheet" href="styles/GestionUsuarios.css" type="text/css"/>
        <link rel="stylesheet" href="styles/GestionRefundio.css" type="text/css"/> 
        <link rel="stylesheet" href="styles/GestionValidacionConsolidacion.css" type="text/css"/>
        <link rel="stylesheet" href="styles/GestionDiccionario.css" type="text/css"/>
        <link rel="stylesheet" href="styles/GestionPlanBase.css" type="text/css"/>
        <link rel="stylesheet" href="styles/GestionDiarioDeOperacion.css" type="text/css"/>
        
        <!-- COMPONENTES EXTERNOS -->        
        <link rel="stylesheet" href="styles/popupMsg.css" type="text/css"/>     
        <link rel="stylesheet" href="javascriptsV2/mooTree2/mootree.css"  type="text/css"/>
        <link rel="stylesheet" href="javascriptsV2/omniGrid/omnigrid.css" type="text/css"/>        
        <link rel="stylesheet" href="javascriptsV2/mooList/moolist.css" type="text/css" />
        <link rel="stylesheet" href="javascriptsV2/formval/formval.css" type="text/css"/>
        <link rel="stylesheet" href="javascriptsV2/autocomplete/Autocompleter.css" type="text/css"/>
        <link rel="stylesheet" href="javascriptsV2/Calendar/skins/dashboard/dashboard.css" type="text/css"/>        
        <link rel="stylesheet" href="javascriptsV2/OL211/theme/default/ZoomBar.css" type="text/css"/>
        <link rel="stylesheet" href="javascriptsV2/OL211/theme/default/ToolBar.css" type="text/css"/>          
                
        <!-- Configurador VisorGIS -->
        <link rel="stylesheet" href="styles/GestionConfigVisor.css" type="text/css"/>        
        <link rel="stylesheet" href="styles/GestionFormularios.css" type="text/css" media="screen" />
                
        <!-- Configurador Fichas -->
        <link rel="stylesheet" href="styles/GestionFichas.css" type="text/css"/> 
		
        <!-- Preview de Visor -->
        <link rel="stylesheet" href="javascriptsV2/ConsoleOptions/previewVisor/arbolCapas/mootree_Capas.css"/>
        <link rel="stylesheet" href="styles/Pie.css"/>
        <link rel="stylesheet" href="styles/PreviewVisor.css"/>        											
    </head>
    
    <body>
    <div id="desktop">
            
        <div id="pnlSuperior">
        	<div id="logo"></div>
			<div id="logoSecundario"></div>
            <div id="cabTituloPpal">CONTROL DE ACCESO</div>
            <div id="cabTitulo">
            	<div id="cabAddInfo">
                </div>
                <div id="cabTituloSeccion">SELECCIONE UNA OPCIÓN DE ACCESO</div>
                <div id="cabLogin">
                	<label id="labelUserState">Usuario desconectado</label> | Versión : 2.0.0 | <a id="opcionLoginUSERLink" href="#">Iniciar Sesión</a>
                </div>                

            </div>
            <div id="botoneraSuperior"></div>
        </div>
        
        <div id="pnlCentral"></div>
        
        <div id="pnlInferiorWrapper">
        	<div id="pnlInferior">
                <div id="botonera" class="centerElement">
                    <div id="opcionLoginUSER" class="opcionBotonera"></div>
                    <div id="opcionLoginCERTIFICADO" class="opcionBotonera"></div>
                </div>
        	</div>
        </div>

    </div>    		
    </body>
    
    <!-- Esto debera cargarse para el caso de IE -->	
	<!--[if IE]>
		<script type="text/javascript" src="javascriptsV2/mocha/excanvas_r43.js"></script>
	<![endif]-->	
    
    <script type="text/javascript" src="javascriptsV2/mocha/mootools-1.2.4-core_debug.js"></script>
   	<script type="text/javascript" src="javascriptsV2/mocha/mootools-1.2.4.2-more_debug.js"></script>
      	   	
   	<script type="text/javascript" src="javascriptsV2/mocha/mocha.js"></script>   	
   	
 	<script src="javascriptsV2/Utilities/Meio.Mask.js" type="text/javascript"></script>
   	
   	<!-- 	
    <script type="text/javascript" src="javascriptsV2/OL28/OpenLayers_2.8.js"></script>
    <script type="text/javascript" src="javascriptsV2/OL28/proj4js-combined.js"></script>
    <script type="text/javascript" src="javascriptsV2/OL28/redefinicionesOL.js"></script>
    -->
    <script type="text/javascript" src="javascriptsV2/OL211/OpenLayers.js"></script>
    <script type="text/javascript" src="javascriptsV2/OL211/proj4js/lib/proj4js-compressed.js"></script>
    <script type="text/javascript" src="javascriptsV2/OL211/OpenLayers/Lang/es.js" charset="ISO-8859-1"></script>
    <script type="text/javascript" src="javascriptsV2/mooTree2/mootree.js"></script>
    <script type="text/javascript" src="javascriptsV2/omniGrid/omnigrid.js"></script>
    <script type="text/javascript" src="javascriptsV2/mooList/moolist.js"></script>
    <script type="text/javascript" src="javascriptsV2/formval/formval.js"></script>
    <script type="text/javascript" src="javascriptsV2/autocomplete/Observer.js"></script>
    <script type="text/javascript" src="javascriptsV2/autocomplete/Autocompleter.js"></script>
    <script type="text/javascript" src="javascriptsV2/autocomplete/Autocompleter.Local.js"></script>
    <script type="text/javascript" src="javascriptsV2/autocomplete/Autocompleter.Request.js"></script>
    <script type="text/javascript" src="javascriptsV2/Utilities/popupMsg.js"></script>
    <script type="text/javascript" src="javascriptsV2/Utilities/highlighter.js"></script>
    <script type="text/javascript" src="javascriptsV2/Calendar/calendar.js"></script>      
    
    <script type="text/javascript" src="javascriptsV2/init.js"></script>
    <script type="text/javascript" src="javascriptsV2/Utilities/updateConsoleStatus.js"></script>
    <script type="text/javascript" src="javascriptsV2/Utilities/transitions.js"></script>   	       	      
    <script type="text/javascript" src="javascriptsV2/loging.js"></script>
    <script type="text/javascript" src="javascriptsV2/console.js"></script>
    
    <script type="text/javascript" src="javascriptsV2/Utilities/jsonData.js"></script>

    <script type="text/javascript" src="javascriptsV2/ConsoleOptions/PLANES/UtilitiesPLANES.js"></script>
    <script type="text/javascript" src="javascriptsV2/ConsoleOptions/PLANES/DeterminacionesPLANES.js"></script>
    <script type="text/javascript" src="javascriptsV2/ConsoleOptions/PLANES/EntidadesPLANES.js"></script>
    <script type="text/javascript" src="javascriptsV2/ConsoleOptions/PLANES/DataTramitesVisorPLANES.js"></script>
    <script type="text/javascript" src="javascriptsV2/ConsoleOptions/PLANES/loaderVisorPLANES.js"></script>
    <script type="text/javascript" src="javascriptsV2/ConsoleOptions/PLANES/MapForEntity.js"></script>
    <script type="text/javascript" src="javascriptsV2/ConsoleOptions/PLANES/MapTools/ToolBar.js"></script>
    <script type="text/javascript" src="javascriptsV2/ConsoleOptions/PLANES/MapTools/ZoomBar.js"></script>
    <script type="text/javascript" src="javascriptsV2/ConsoleOptions/PLANES/MapTools/MapClick.js"></script>
    
    <script type="text/javascript" src="javascriptsV2/ConsoleOptions/previewFichas/MapForFicha.js"></script>
    
    <script type="text/javascript" src="javascriptsV2/ConsoleOptions/loaderAdministracion.js"></script>
    <script type="text/javascript" src="javascriptsV2/ConsoleOptions/loaderConsolidador.js"></script>
    <script type="text/javascript" src="javascriptsV2/ConsoleOptions/loaderDiarioDeOperacion.js"></script>
    <script type="text/javascript" src="javascriptsV2/ConsoleOptions/loaderGestionDiccionario.js"></script>
    <script type="text/javascript" src="javascriptsV2/ConsoleOptions/loaderRefundido.js"></script>
    <script type="text/javascript" src="javascriptsV2/ConsoleOptions/loaderValidacion.js"></script>
    <script type="text/javascript" src="javascriptsV2/ConsoleOptions/loaderPlanBase.js"></script>
    
    <script type="text/javascript" src="javascriptsV2/ConsoleOptions/VisorGIS/loaderVisorGIS.js"></script>
    <script type="text/javascript" src="javascriptsV2/ConsoleOptions/VisorGIS/Configurador.js"></script>
    <script type="text/javascript" src="javascriptsV2/ConsoleOptions/VisorGIS/ConfiguradorLayerWMS.js"></script>
    <!--  <script type="text/javascript" src="javascriptsV2/ConsoleOptions/VisorGIS/ConfiguradorLayerWFS.js"></script>  -->
    <script type="text/javascript" src="javascriptsV2/ConsoleOptions/VisorGIS/curvycorners.src.js"></script>
    <script type="text/javascript" src="javascriptsV2/ConsoleOptions/VisorGIS/jsIO.js"></script>             
    
    <script type="text/javascript" src="javascriptsV2/ConsoleOptions/previewVisor/previewVisor.js"></script> 
    <script type="text/javascript" src="javascriptsV2/ConsoleOptions/previewVisor/redefinicionesOL.js"></script>     
    <script type="text/javascript" src="javascriptsV2/ConsoleOptions/previewVisor/arbolCapas/mootree_Capas.js"></script> 
    <script type="text/javascript" src="javascriptsV2/ConsoleOptions/previewVisor/Pie.js"></script> 
    <script src="http://maps.google.com/maps/api/js?sensor=false"></script>

</html>

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

<%@page import="java.util.Map"%>

<html>
    <head>

        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta http-equiv="X-UA-Compatible" content="IE=8" />
        
        <title>ENTORNO DEL PRODUCTOR</title>       
        
        <meta name="description" content="Entorno del Productor" />   
        
        <link rel="stylesheet" type="text/css" href="../themes/default/css/Content.css" />
		<link rel="stylesheet" type="text/css" href="../themes/default/css/Core.css"/>
		<link rel="stylesheet" type="text/css" href="../themes/default/css/Layout.css"/>
		<!-- <link rel="stylesheet" type="text/css" href="../themes/default/css/Dock.css"/>
		<link rel="stylesheet" type="text/css" href="../themes/default/css/Tabs.css"/>
		<link rel="stylesheet" type="text/css" href="../themes/default/css/Window.css"/>-->
        
        <link rel="stylesheet" href="../styles/baseStyles.css" type="text/css"/>
        <link rel="stylesheet" href="../styles/fromsStyles.css" type="text/css"/>
        
        <!-- ConsoleOptions NO VISOR GIS -->
        <link rel="stylesheet" href="../styles/GestionLogin.css" type="text/css"/>
        <link rel="stylesheet" href="../styles/GestionValidacionConsolidacion.css" type="text/css"/>        											
    </head>
    
    <body>
    
    <div id="desktop">    
            
        <div id="pnlSuperior">
        	<div id="logo"></div>
			<div id="logosecundario"></div>
            <div id="cabTituloPpal"></div>
            <div id="cabTitulo">
            	<div id="cabAddInfo"></div>
                <div id="cabTituloSeccion"></div>
                <div id="cabLogin">                	
                </div>                

            </div>
            <div id="botoneraSuperior"></div>
        </div>
        
        <div id="pnlCentral">
        	 <div id="formLogin">
			     <div id="lblLogin"></div>
			     <div id="lblLoginUsuario">CENTRO DE PRODUCCI&Oacute;N</div>
			     <select id="loginTxtUsuario">
			                <% 
			                Map<Integer, String> centros = (Map<Integer,String>)session.getAttribute("produccion.centros");
				            	if (centros != null) {
				            		for (Integer id : centros.keySet()) {%>
				                    <option id="<%=id%>"><%=centros.get(id)%></option>
				        		<%	}	            	
			                	}
			                	else {%>
				            		<option id="-1">No se han localizado centros de producci&oacute;n</option>
				            	<%}%>
			            </select>
			     <!-- <input id="loginTxtUsuario" type="text" value=""/>  -->
			     <div id="lblLoginPasswd">CONTRASE&Ntilde;A</div>
			     <input id="loginTxtPasswd" type="password" value=""/>      
			     <span class="iconWrapper"><img id="loginBtnSubmit" class="buttonImage loginBtnSubmit" src="../styles/images/validar.png" alt="Acceder" title="Acceder a la aplicaci&oacute;n"/></span> 
			 </div>
        </div>
        
        <div id="pnlInferiorWrapper">
        	<div id="pnlInferior">                
        	</div>
        </div>

    </div>    		
    </body>
    
        <!-- Esto debera cargarse para el caso de IE -->	
	<!--[if IE]>
		<script type="text/javascript" src="../javascriptsV2/mocha/excanvas_r43.js"></script>
	<![endif]-->	
    
    <script type="text/javascript" src="../javascriptsV2/mocha/mootools-1.2.4-core_debug.js"></script>
   	<script type="text/javascript" src="../javascriptsV2/mocha/mootools-1.2.4.2-more_debug.js"></script> 

    <script type="text/javascript" src="../javascriptsV2/Utilities/transitions.js"></script>   	       	      
    <script type="text/javascript" src="../javascriptsV2/loging.js"></script>       
    
    <script type="text/javascript">
    function initializeGlobalEvents() {

        // Deactivate menu header links
        $$('a.returnFalse').each(function (el) {
            el.addEvent('click', function (e) {
                new Event(e).stop();
            });
        });
    }

    //Esto inicializa el manejo de mascaras
    window.addEvent('load',function(){
    	
    	initializeGlobalEvents();

    	initializeLogingUSEREvents();
    });

    window.addEvent('resize',function(){
    	
    	window.removeEvent('resize');
    	
    	ajustarPnlCentral();	   
    });
	</script>

</html>

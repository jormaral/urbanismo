<!-- 
    Document   : index
    Created on : 01-nov-2011, 17:00:00
    Author     : Alvaro.Montero
-->
<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!-- HTML 4
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">  -->
    
<!-- HTML 5 -->
<!DOCTYPE HTML>

<%@page import="java.text.SimpleDateFormat" %>
<%@page import="es.mitc.redes.urbanismoenred.data.rpm.planeamiento.Tramite"%>
<%
if (session.getAttribute("logged-in") == null || !session.getAttribute("logged-in").equals("S")) {
    response.sendRedirect("../../index.jsp");
}

if (session.getAttribute("produccion.tramites") != null) {
	//session.getAttribute("logged-in") != null && session.getAttribute("logged-in").equals("S") && 
		
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

	List<Map<String,Object>> tramites = (List<Map<String,Object>>)session.getAttribute("produccion.tramites");
%>

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
            <div id="cabTituloPpal">CONTROL DE ACCESO</div>
            <div id="cabTitulo">
            	<div id="cabAddInfo"></div>
                <div id="cabTituloSeccion"></div>
                <div id="cabLogin"> 
	                Versión : 2.0.0 |
	                <a id="opcionLoginUSERLink" href="#">Desconectar</a>
                </div>         
            </div>
            <div id="botoneraSuperior"></div>
        </div>
        
        <div id="pnlCentral">
        	<div class="tablaValidacion" id="contenedorTablaValidacionesRealizadas">
				<table style="width:100%; border-width: 1px; border-style: solid; border-color: #EEEEEE;">
			    	<tr class="cabeceraTablaValidacion">
			    		<td class="filaValidacionCelda" width="400px">Municipio</td>
			    		<td class="filaValidacionCelda" width="400px">Código FIP</td>
			    		<td class="filaValidacionCelda" width="300px">Tipo</td>
			    		<td class="filaValidacionCelda" width="40%">Plan</td>
			    		<td class="filaValidacionCelda" width="110px">Iteracion</td>
			    		<td class="filaValidacionCelda" width="200px">Nombre</td>
			    		<td class="filaValidacionCelda" width="100px">Generado</td>
			    		<td class="filaValidacionCelda" width="70px">Fip</td>
			    	</tr>
			    	<% 
			    	int i = 0;
			    	for(Map<String,Object> actual : tramites) {  
			    		i++;
			    	%>
			    	
				    	<tr <%=((i%2) == 0)?"class=\"filaParValidacion\"":"class=\"filaImparValidacion\""%>>
				    		<td class="filaValidacionCelda" width="400px">
				    			<%=actual.get("municipio")%>    			
				    		</td>
				    		<td class="filaValidacionCelda" width="400px">
				    			<%=actual.get("codigoFip")%>    			
				    		</td>
				    		<td class="filaValidacionCelda" width="300px">
				    			<%=actual.get("tipo")%>    			
				    		</td>
				    		<td class="filaValidacionCelda" width="40%">
				    			<%=actual.get("plan")%>    			
				    		</td>
				    		<td class="filaValidacionCelda" width="110px">
				    			<%=actual.get("iteracion")%>    			
				    		</td>
				    		<td class="filaValidacionCelda" width="200px" style="text-align: center;">
				    			<%=actual.get("nombre")%>
				    		</td>
				    		<td class="filaValidacionCelda" width="100px" style="text-align: center;">
				    			 <%=actual.get("fecha")%>
				    		</td>
				    		<td class="filaValidacionCelda" width="70px">
				    			<span class="iconWrapper" id="btnFIP-<%=actual.get("codigoFip")%>" onclick="window.open('../ActionServletFip1?wsName=GENERAR_FIP1&tramite=<%=actual.get("codigoFip")%>','_blank');">
				    				<img class="toolBoxAction" src="../styles/images/FIP.png" alt="Descargar Fip" title="Descargar Fip"/>
				    			</span>
				    		</td>
				    	</tr>
			    	<%} %>
			    	</table>
			    <br/><br/>
			</div>
			<% } else {%> 
				<h2 class="title-panel-header">No se encuentran FIPs a descargar</h2>
			<% } %> 
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

    	initializeUnLogingUSEREvents();
    });
	</script>
    
</html>
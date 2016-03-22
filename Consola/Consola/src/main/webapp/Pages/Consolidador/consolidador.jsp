
<%@page contentType="text/html" pageEncoding="UTF-8"import="java.util.Iterator"%>
<%@page import="javax.naming.NamingException"%>
<%@page import="javax.naming.InitialContext"%>
<%@page import="java.text.SimpleDateFormat" %>
<%@page import="es.mitc.redes.urbanismoenred.servicios.consolidacion.ProcesoConsolidable"%>
<%@page import="es.mitc.redes.urbanismoenred.servicios.consolidacion.ServicioConsolidacionLocal"%>
<%@page import="es.mitc.redes.urbanismoenred.servicios.planeamiento.ServicioDiccionariosLocal"%>
<%@page import="es.mitc.redes.urbanismoenred.data.rpm.diccionario.Ambito"%>
<%@page import="es.mitc.redes.urbanismoenred.data.rpm.diccionario.Instrumentoplan"%>
<%@page import="es.mitc.redes.urbanismoenred.data.rpm.diccionario.Tipotramite"%>
<%@page import="es.mitc.redes.urbanismoenred.servicios.seguridad.Usuario"%>

<%
	if (session.getAttribute("usuario") != null) {		
		InitialContext context;
	    try {
			context = new InitialContext();
			
			if (session.getAttribute("ServicioConsolidacion") == null) {
				session.setAttribute("ServicioConsolidacion", context.lookup("java:global/UrbanismoEnRedV2/Urbanismoenred-serviciosV2/ServicioConsolidacion"));
			}
			
			if (session.getAttribute("servicioDiccionario") == null) {
				session.setAttribute("servicioDiccionario", context.lookup("java:global/UrbanismoEnRedV2/Urbanismoenred-serviciosV2/ServicioDiccionarios!es.mitc.redes.urbanismoenred.servicios.planeamiento.ServicioDiccionariosLocal"));
			}
	    } catch (NamingException e) {
			throw new ServletException("No se ha podido inicializar correctamente el servlet. Causa: " + e.getMessage());
		}
		
		ServicioConsolidacionLocal servicioConsolidacion = (ServicioConsolidacionLocal)session.getAttribute("ServicioConsolidacion");
		ServicioDiccionariosLocal servicioDiccionario = (ServicioDiccionariosLocal)session.getAttribute("servicioDiccionario");
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		
		ProcesoConsolidable[] procesos = servicioConsolidacion.getProcesosConsolidables((Usuario)session.getAttribute("usuario"));
%>
<input id="numProcesos" type="hidden" value="<%=procesos.length%>">

<div id="valCargaFip">
<div class="tablaValidacion" id="contenedorTablaValidaciones">
	<div id="tituloConsolidador" class="centerElement"><h3>Lista de tr&aacute;mites a consolidar</h3></div>
	<table id="tablaConsolidacion" style="width:100%; border-width: 1px; border-style: solid; border-color: #EEEEEE;">
    	<tr class="cabeceraTablaValidacion">
    		<td class="filaValidacionCelda" width="14%">&Aacute;mbito</td>
    		<td class="filaValidacionCelda" width="14%">Tipo de instrumento</td>
    		<td class="filaValidacionCelda" width="14%">Nombre del plan</td>
    		<td class="filaValidacionCelda" width="14%">Nombre del tr&aacute;mite</td>
    		<td class="filaValidacionCelda" width="14%">Tipo del tr&aacute;mite</td> 
    		<td class="filaValidacionCelda" width="14%">Fecha del tr&aacute;mite</td>
    		<td class="filaValidacionCelda" width="14%">Nombre del FIP</td>
    		<td class="filaValidacionCelda" width="70px"></td>
    	</tr>
    	<%         
    	    	    	    	
    	for(int i =0; i < procesos.length;i++) {
    		
    		String instrumento =  "";
    		if (procesos[i].getTipoInstrumento()!= null)
    			{instrumento = servicioDiccionario.getTraduccion(Instrumentoplan.class, procesos[i].getTipoInstrumento(), "es");}
    		String tipoTramite =  "";
    		tipoTramite = servicioDiccionario.getTraduccion(Tipotramite.class, procesos[i].getTipoTramite(), "es");
    	
    		String fecha = "";
    		if (procesos[i].getFecha() != null && procesos[i].getFecha() instanceof java.util.Date) 
			{fecha = sdf.format(procesos[i].getFecha());}
    		
    		String nombreTramite = "";
    		if (procesos[i].getNombreTramite() != null)
    		{ nombreTramite = procesos[i].getNombreTramite(); }
    	%>
	    	<tr <%=((i%2) == 0)?"class=\"filaParValidacion\"":"class=\"filaImparValidacion\""%>>
	    		<td class="filaValidacionCelda" width="14%" style ="border-right: hidden">
	    			<%=servicioDiccionario.getTraduccion(Ambito.class, procesos[i].getAmbito(),"es")%>
	    		</td>
	    		<td class="filaValidacionCelda" width="14%">
	    			<%= instrumento %>
	    		</td>
	    		<td class="filaValidacionCelda" width="14%">
	    			<%= procesos[i].getNombrePlan() %>
	    		</td>
	    		<td class="filaValidacionCelda" width="14%">
	    			<%= nombreTramite %>
	    		</td>
	    		<td class="filaValidacionCelda" width="14%">
	    			<%= tipoTramite %>
	    		</td>
	    		<td class="filaValidacionCelda" width="14%">
	    			<%= fecha %>
	    		</td>
	    		<td class="filaValidacionCelda" width="14%">
	    			<%= procesos[i].getCodigoTramite() %>
	    		</td>
	    		<td class="filaValidacionCelda" width="70px">
	    			<span class="iconWrapper" id="btnRConsolidar-<%=procesos[i].getProceso()%>" onclick="onbtnRConsolidarClick('<%=procesos[i].getProceso()%>','<%=procesos[i].getCodigoTramite()%>');">
	    				<img id="BtnVisorPlanesBuscar" class="toolBoxAction" src="styles/images/refundir.png" alt="Consolidar" title="Consolidar"/>
	    			</span>
	    		</td>
	    	</tr>
    	<%} %>
    	</table>
    <br/><br/>
</div>
</div>
<% } %>
